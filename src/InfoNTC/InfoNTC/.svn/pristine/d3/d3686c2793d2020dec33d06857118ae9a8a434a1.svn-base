package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import lk.informatics.ntc.model.dto.CourtCaseDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class ManageCourtCaseServiceImpl implements ManageCourtCaseService {

	public String retrieveLastNoForNumberGeneration(String code) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String appNo = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT app_no FROM nt_r_number_generation WHERE code=?";
			ps = con.prepareStatement(sql);

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

	public void updateOffenceCodeInNumberGenTable(String code, String loginUser) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_r_number_generation" + " SET  app_no=?,  modified_by=?, modified_date=?"
					+ " WHERE code=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, code);

			stmt.setString(2, loginUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, "COT");

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

	public String generateCourtCaseCode() {

		String last = retrieveLastNoForNumberGeneration("COT");
		long no = Long.parseLong(last.substring(3));
		String next = String.valueOf((no + 1));
		String appNo = "COT" + StringUtils.leftPad(next, 7, '0');

		return appNo;
	}

	public void insertdata(CourtCaseDTO courtCaseDTO, String user, String orgin, String destination) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String strDate2 = null;

		if (courtCaseDTO.getInspectStartDate() != null) {
			strDate2 = dateFormat.format(courtCaseDTO.getInspectStartDate());

		}

		try {

			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_court_cases");

			String sql = "INSERT INTO public.nt_m_court_cases\r\n"
					+ "(coc_seq, coc_vehicle_no, coc_permit_no, coc_route_no, coc_court_name,\r\n"
					+ "coc_case_no, coc_court_date, coc_group_code, coc_status, coc_created_by, coc_created_date,coc_inspection_date,coc_remarks,coc_action_taken,coc_route_orgin,coc_route_destination)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, courtCaseDTO.getVehicleNo());
			stmt.setString(3, courtCaseDTO.getPermitNo());
			stmt.setString(4, courtCaseDTO.getRouteNo());
			stmt.setString(5, courtCaseDTO.getCourtName());
			stmt.setString(6, courtCaseDTO.getCaseNo());
			stmt.setString(7, dateFormat.format(courtCaseDTO.getDateofCourtCase()));
			stmt.setString(8, courtCaseDTO.getGroupCode());
			stmt.setString(9, courtCaseDTO.getStatus());
			stmt.setString(10, user);
			stmt.setTimestamp(11, timestamp);
			stmt.setString(12, strDate2);
			stmt.setString(13, courtCaseDTO.getRemarks());
			stmt.setString(14, courtCaseDTO.getActionByCourt());
			stmt.setString(15, orgin);
			stmt.setString(16, destination);

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

	public void updatedata(CourtCaseDTO courtCaseDTO, String user) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		String strDate2 = null;

		String strDate = dateFormat.format(courtCaseDTO.getDateofCourtCase());
		if (courtCaseDTO.getInspectionDate() != null) {
			strDate = dateFormat.format(courtCaseDTO.getInspectionDate());

		}
		if (courtCaseDTO.getInspectStartDate() != null) {
			strDate2 = dateFormat.format(courtCaseDTO.getInspectStartDate());

		}

		try {

			con = ConnectionManager.getConnection();

			String sql1 = "INSERT into public.nt_h_court_cases  "
					+ " (SELECT * FROM public.nt_m_court_cases WHERE coc_seq  = ?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setLong(1, courtCaseDTO.getCourtCaseSeq());
			stmt1.executeUpdate();

			String sql = "UPDATE public.nt_m_court_cases\r\n" + "SET coc_court_name=?, coc_court_date=?,\r\n"
					+ "coc_group_code=?, coc_status=?,\r\n"
					+ "coc_modified_by=?, coc_modified_date=?, coc_remarks=?,coc_action_taken=? , coc_inspection_date=? \r\n"
					+ "WHERE coc_seq=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, courtCaseDTO.getCourtName());
			stmt.setString(2, strDate);
			stmt.setString(3, courtCaseDTO.getGroupCode());
			stmt.setString(4, courtCaseDTO.getStatus());
			stmt.setString(5, user);
			stmt.setTimestamp(6, timestamp);
			stmt.setString(7, courtCaseDTO.getRemarks());
			stmt.setString(8, courtCaseDTO.getActionByCourt());
			stmt.setString(9, strDate2);
			stmt.setLong(10, courtCaseDTO.getCourtCaseSeq());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(con);

		}

	}

	public ArrayList<CourtCaseDTO> getcourtcaseList(String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		ArrayList<CourtCaseDTO> CourtCaseDTOlist = new ArrayList<CourtCaseDTO>();
		try {
			con = ConnectionManager.getConnection();
			if (status.equalsIgnoreCase("C")) {
				sql = "SELECT coc_seq, coc_vehicle_no, coc_permit_no,group_name,\r\n"
						+ "coc_route_no, coc_court_name, coc_case_no,\r\n"
						+ "coc_court_date, coc_group_code, coc_status, \r\n" + "coc_created_by, coc_created_date, \r\n"
						+ "coc_modified_by, coc_modified_date, coc_remarks,coc_action_taken , coc_inspection_date \r\n"
						+ "FROM public.nt_m_court_cases,nt_r_court_group\r\n"
						+ "where coc_group_code=code and nt_m_court_cases.coc_status != 'C' \r\n"
						+ "order by coc_seq desc";
			} else {
				sql = "SELECT coc_seq, coc_vehicle_no, coc_permit_no,group_name,\r\n"
						+ "coc_route_no, coc_court_name, coc_case_no,\r\n"
						+ "coc_court_date, coc_group_code, coc_status, \r\n" + "coc_created_by, coc_created_date, \r\n"
						+ "coc_modified_by, coc_modified_date, coc_remarks,coc_action_taken , coc_inspection_date \r\n"
						+ "FROM public.nt_m_court_cases,nt_r_court_group\r\n" + "where coc_group_code=code \r\n"
						+ "order by coc_seq desc";
			}

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();

				courtCaseDTO.setCourtCaseSeq(rs.getLong("coc_seq"));
				courtCaseDTO.setVehicleNo(rs.getString("coc_vehicle_no"));
				courtCaseDTO.setPermitNo(rs.getString("coc_permit_no"));
				courtCaseDTO.setRouteNo(rs.getString("coc_route_no"));
				courtCaseDTO.setCourtName(rs.getString("coc_court_name"));
				courtCaseDTO.setCaseNo(rs.getString("coc_case_no"));
				courtCaseDTO.setDateOfCourt(rs.getString("coc_court_date"));
				courtCaseDTO.setGroupCode(rs.getString("coc_group_code"));
				courtCaseDTO.setStatus(rs.getString("coc_status"));
				courtCaseDTO.setCreatedBy(rs.getString("coc_created_by"));
				courtCaseDTO.setCreatedDate(rs.getTimestamp("coc_created_date"));
				courtCaseDTO.setModifyBy(rs.getString("coc_modified_by"));
				courtCaseDTO.setModifiedDate(rs.getTimestamp("coc_modified_date"));
				courtCaseDTO.setGroupName(rs.getString("group_name"));

				courtCaseDTO.setRemarks(rs.getString("coc_remarks"));
				courtCaseDTO.setActionByCourt(rs.getString("coc_action_taken"));
				courtCaseDTO.setInspDate(rs.getString("coc_inspection_date"));

				if (courtCaseDTO.getStatus().equalsIgnoreCase("A")) {
					courtCaseDTO.setStatusdes("Active");
				} else if (courtCaseDTO.getStatus().equalsIgnoreCase("I")) {
					courtCaseDTO.setStatusdes("Inactive");
				} else if (courtCaseDTO.getStatus().equalsIgnoreCase("C")) {
					courtCaseDTO.setStatusdes("Closed");
				}

				CourtCaseDTOlist.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return CourtCaseDTOlist;

	}

	public void inserthistorydata(CourtCaseDTO courtCaseDTO, String user) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_h_court_cases\r\n"
					+ "(coc_h_seq, coc_h_vehicle_no, coc_h_permit_no, coc_h_route_no, coc_h_court_name, coc_h_case_no, coc_h_court_date, coc_h_group_code, coc_h_status, coc_h_created_by, coc_h_created_date, coc_h_modified_by, coc_h_modified_date, coc_h_hiscreated_by, coc_h_hiscreated_date, coc_remarks,coc_action_taken , coc_inspection_date )\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, courtCaseDTO.getCourtCaseSeq());
			stmt.setString(2, courtCaseDTO.getVehicleNo());
			stmt.setString(3, courtCaseDTO.getPermitNo());
			stmt.setString(4, courtCaseDTO.getRouteNo());
			stmt.setString(5, courtCaseDTO.getCourtName());
			stmt.setString(6, courtCaseDTO.getCaseNo());

			stmt.setString(7, courtCaseDTO.getDateOfCourt());
			stmt.setString(8, courtCaseDTO.getGroupCode());
			stmt.setString(9, courtCaseDTO.getStatus());
			stmt.setString(10, courtCaseDTO.getCreatedBy());
			stmt.setTimestamp(11, courtCaseDTO.getCreatedDate());
			stmt.setString(12, courtCaseDTO.getModifyBy());
			stmt.setTimestamp(13, courtCaseDTO.getModifiedDate());
			stmt.setString(14, user);
			stmt.setTimestamp(15, timestamp);

			stmt.setString(16, courtCaseDTO.getRemarks());
			stmt.setString(17, courtCaseDTO.getActionByCourt());
			stmt.setString(18, courtCaseDTO.getInspDate());

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

//get data list with parameters 
	public ArrayList<CourtCaseDTO> getcourtcaseListwithparam(String status, String vehicleno, String permitno,
			String courtcaseno, String groupNo, Date startDate, Date endDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String whereSql = null;

		ArrayList<CourtCaseDTO> CourtCaseDTOlist = new ArrayList<CourtCaseDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

		try {

			if (status != null && !status.equalsIgnoreCase("") && !status.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and coc_status =" + "'" + status + "'";

				}
			}

			if (vehicleno != null && !vehicleno.equalsIgnoreCase("") && !vehicleno.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and coc_vehicle_no =" + "'" + vehicleno + "'";

				} else {
					whereSql = whereSql + "and coc_vehicle_no =" + "'" + vehicleno + "'";
				}
			}

			if (permitno != null && !permitno.equalsIgnoreCase("") && !permitno.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and coc_permit_no =" + "'" + permitno + "'";

				} else {
					whereSql = whereSql + "and coc_permit_no =" + "'" + permitno + "'";
				}
			}

			if (groupNo != null && !groupNo.equalsIgnoreCase("") && !groupNo.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and coc_group_code =" + "'" + groupNo + "'";

				} else {
					whereSql = whereSql + "and coc_group_code =" + "'" + groupNo + "'";
				}
			}

			if (courtcaseno != null && !courtcaseno.equalsIgnoreCase("") && !courtcaseno.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and coc_case_no =" + "'" + courtcaseno + "'";

				} else {
					whereSql = whereSql + "and coc_case_no =" + "'" + courtcaseno + "'";
				}
			}

			if (startDate != null) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = " and coc_court_date >=" + "'" + sdf.format(startDate) + "'";

				}

				else {
					whereSql = whereSql + " and coc_court_date >=" + "'" + sdf.format(startDate) + "'";
				}

			}

			if (endDate != null) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "  and coc_court_date <=" + "'" + sdf.format(endDate) + "'";

				}

				else {
					whereSql = whereSql + "  and coc_court_date <=" + "'" + sdf.format(endDate) + "'";
				}

			}

			con = ConnectionManager.getConnection();
			String sql = "SELECT coc_seq, coc_vehicle_no, coc_permit_no,group_name,\r\n"
					+ "coc_route_no, coc_court_name, coc_case_no,\r\n"
					+ "coc_court_date, coc_group_code, coc_status, \r\n" + "coc_created_by, coc_created_date, \r\n"
					+ "coc_modified_by, coc_modified_date , coc_remarks,coc_action_taken , coc_inspection_date\r\n"
					+ "FROM public.nt_m_court_cases,nt_r_court_group\r\n" + "where coc_group_code=code\r\n" + whereSql
					+ "order by coc_seq";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String sDate1 = rs.getString("coc_court_date");
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();

				courtCaseDTO.setCourtCaseSeq(rs.getLong("coc_seq"));
				courtCaseDTO.setVehicleNo(rs.getString("coc_vehicle_no"));
				courtCaseDTO.setPermitNo(rs.getString("coc_permit_no"));
				courtCaseDTO.setRouteNo(rs.getString("coc_route_no"));
				courtCaseDTO.setCourtName(rs.getString("coc_court_name"));
				courtCaseDTO.setCaseNo(rs.getString("coc_case_no"));
				courtCaseDTO.setDateofCourtCase(date1);
				courtCaseDTO.setGroupCode(rs.getString("coc_group_code"));
				courtCaseDTO.setStatus(rs.getString("coc_status"));
				courtCaseDTO.setCreatedBy(rs.getString("coc_created_by"));
				courtCaseDTO.setCreatedDate(rs.getTimestamp("coc_created_date"));
				courtCaseDTO.setModifyBy(rs.getString("coc_modified_by"));
				courtCaseDTO.setModifiedDate(rs.getTimestamp("coc_modified_date"));
				courtCaseDTO.setGroupName(rs.getString("group_name"));
				courtCaseDTO.setRemarks(rs.getString("coc_remarks"));
				courtCaseDTO.setActionByCourt(rs.getString("coc_action_taken"));
				courtCaseDTO.setInspDate(rs.getString("coc_inspection_date"));

				if (courtCaseDTO.getStatus().equalsIgnoreCase("A")) {
					courtCaseDTO.setStatusdes("Active");
				} else if (courtCaseDTO.getStatus().equalsIgnoreCase("C")) {
					courtCaseDTO.setStatusdes("Closed");
				} else {
					courtCaseDTO.setStatusdes("Inactive");
				}

				CourtCaseDTOlist.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return CourtCaseDTOlist;

	}

//court case no list 
	public ArrayList<CourtCaseDTO> courtcaseNoList() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<CourtCaseDTO> CourtCaseDTOlist = new ArrayList<CourtCaseDTO>();

		try {

			con = ConnectionManager.getConnection();
			String sql = "SELECT coc_case_no\r\n" + "FROM public.nt_m_court_cases" + " ORDER BY coc_case_no";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();
				courtCaseDTO.setCaseNo(rs.getString("coc_case_no"));
				CourtCaseDTOlist.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return CourtCaseDTOlist;

	}

	/** For Flying Squad Court Information Status Page **/

	@Override
	public ArrayList<CourtCaseDTO> getCourtCaseVehiNum() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<CourtCaseDTO> vehiNum = new ArrayList<CourtCaseDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "select distinct coc_vehicle_no  from public.nt_m_court_cases where coc_vehicle_no  is not null";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();
				courtCaseDTO.setVehicleNo(rs.getString("coc_vehicle_no"));

				vehiNum.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return vehiNum;

	}

	@Override
	public ArrayList<CourtCaseDTO> getCourtCasePermitNum() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<CourtCaseDTO> permiNum = new ArrayList<CourtCaseDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "select distinct coc_permit_no   from public.nt_m_court_cases where coc_permit_no   is not null";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();
				courtCaseDTO.setPermitNo(rs.getString("coc_permit_no"));

				permiNum.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return permiNum;

	}

	@Override
	public ArrayList<CourtCaseDTO> getCourtCaseServiceNum() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<CourtCaseDTO> refNum = new ArrayList<CourtCaseDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "select distinct inv_det_ref_no  from  public.nt_t_flying_investigation_log_det  where inv_det_ref_no is not null order by inv_det_ref_no ";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();
				courtCaseDTO.setRefrenceNo(rs.getString("inv_det_ref_no"));

				refNum.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return refNum;

	}

	@Override
	public ArrayList<CourtCaseDTO> getDestination() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<CourtCaseDTO> routeDet = new ArrayList<CourtCaseDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "select distinct R.rou_service_destination\r\n" + "from public.nt_m_court_cases\r\n"
					+ "inner join public.nt_r_route R on R.rou_number=public.nt_m_court_cases.coc_route_no\r\n"
					+ "where R.active='A'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();
				courtCaseDTO.setDestination(rs.getString("rou_service_destination"));
				routeDet.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return routeDet;

	}

	@Override
	public ArrayList<CourtCaseDTO> getCaseNum() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<CourtCaseDTO> caseNum = new ArrayList<CourtCaseDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "select distinct coc_case_no    from public.nt_m_court_cases where coc_case_no    is not null order by coc_case_no";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();
				courtCaseDTO.setCaseNo(rs.getString("coc_case_no"));

				caseNum.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return caseNum;

	}

	@Override
	public ArrayList<CourtCaseDTO> getSearchedData(CourtCaseDTO ccDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String Where_Sql = null;
		boolean where_added = false;
		String sql = null;

		ArrayList<CourtCaseDTO> searchedData = new ArrayList<CourtCaseDTO>();
		try {

			con = ConnectionManager.getConnection();

			if (ccDTO.getVehicleNo() != null && !ccDTO.getVehicleNo().trim().isEmpty()
					|| ccDTO.getPermitNo() != null && !ccDTO.getPermitNo().trim().isEmpty()) {
				if (ccDTO.getVehicleNo() != null && !ccDTO.getVehicleNo().trim().isEmpty()
						&& ccDTO.getPermitNo() != null && !ccDTO.getPermitNo().trim().isEmpty()) {

					Where_Sql = "where coc_vehicle_no='" + ccDTO.getVehicleNo() + "' and coc_permit_no='"
							+ ccDTO.getPermitNo() + "'   ";

				}
				if (ccDTO.getVehicleNo() != null && !ccDTO.getVehicleNo().trim().isEmpty()
						&& ccDTO.getPermitNo() == null || ccDTO.getPermitNo().trim().isEmpty()) {

					Where_Sql = "where coc_vehicle_no='" + ccDTO.getVehicleNo() + "'  ";

				}

			} else {
				where_added = true;

			}

			if (ccDTO.getInspectStartDate() != null && ccDTO.getInspectEndDate() != null && where_added) {
				Date inspectDate1 = ccDTO.getInspectStartDate();
				Date inspectDate2 = ccDTO.getInspectEndDate();
				Where_Sql = "where  to_date(coc_inspection_date ,'dd/MM/yyyy')>= '" + inspectDate1 + "' "
						+ "and to_date(coc_inspection_date ,'dd/MM/yyyy')<='" + inspectDate2 + "' ";
			} else if (ccDTO.getInspectStartDate() != null && ccDTO.getInspectEndDate() != null
					&& where_added == false) {

				Date inspectDate1 = ccDTO.getInspectStartDate();
				Date inspectDate2 = ccDTO.getInspectEndDate();
				Where_Sql = "where  coc_vehicle_no='" + ccDTO.getVehicleNo() + "' and coc_permit_no='"
						+ ccDTO.getPermitNo() + "' and coc_case_no='" + ccDTO.getCaseNo() + "'  and coc_court_name ='"
						+ ccDTO.getCourtName() + "'  and  to_date(coc_inspection_date ,'dd/MM/yyyy')>= '" + inspectDate1
						+ "' " + "and to_date(coc_inspection_date ,'dd/MM/yyyy')<='" + inspectDate2 + "'";

			}

			if (ccDTO.getCourtStartDate() != null && ccDTO.getCourtEndDate() != null && where_added) {

				Date courtDate1 = ccDTO.getCourtStartDate();
				Date courtDate2 = ccDTO.getCourtEndDate();
				Where_Sql = "where  to_date(coc_court_date,'dd/MM/yyyy')>='" + courtDate1 + "'"
						+ "and to_date(coc_court_date,'dd/MM/yyyy')<='" + courtDate2 + "'";
			} else if (ccDTO.getCourtStartDate() != null && ccDTO.getCourtEndDate() != null && where_added == false) {

				Date courtDate1 = ccDTO.getCourtStartDate();
				Date courtDate2 = ccDTO.getCourtEndDate();
				Where_Sql = "where  coc_vehicle_no='" + ccDTO.getVehicleNo() + "' and coc_permit_no='"
						+ ccDTO.getPermitNo() + "' and coc_case_no='" + ccDTO.getCaseNo() + "'  and coc_court_name ='"
						+ ccDTO.getCourtName() + "'  and  to_date(coc_court_date,'dd/MM/yyyy')>= '" + courtDate1 + "' "
						+ "and to_date(coc_court_date,'dd/MM/yyyy')<='" + courtDate2 + "' ";

			}
			if (ccDTO.getOrgin() != null && !ccDTO.getOrgin().trim().isEmpty() && where_added) {
				Where_Sql = "where coc_route_orgin ='" + ccDTO.getOrgin() + "' ";

			}

			if (ccDTO.getDestination() != null && !ccDTO.getDestination().trim().isEmpty() && where_added) {
				Where_Sql = "where coc_route_destination ='" + ccDTO.getDestination() + "' ";

			}

			if (ccDTO.getRefrenceNo() != null && !ccDTO.getRefrenceNo().trim().isEmpty() && where_added) {

				Where_Sql = "where I.inv_det_ref_no='" + ccDTO.getRefrenceNo() + "' ";
			}

			if (ccDTO.getCourtName() != null && !ccDTO.getCourtName().trim().isEmpty() && where_added) {

				Where_Sql = "where coc_court_name='" + ccDTO.getCourtName() + "' or X.ccd_court_name='"
						+ ccDTO.getCourtName() + "' ";
			}

			if (ccDTO.getCaseNo() != null && !ccDTO.getCaseNo().trim().isEmpty() && where_added) {

				Where_Sql = "where coc_case_no='" + ccDTO.getCaseNo() + "' ";
			}
			sql = "select distinct coc_vehicle_no,coc_case_no,coc_permit_no,I.inv_det_ref_no,coc_status\r\n"
					+ "from public.nt_m_court_cases C \r\n"
					+ "left outer join public.nt_t_flying_investigation_log_det I on I.inv_det_vehicle_no=C.coc_vehicle_no\r\n"
					+ "left outer join public.nt_t_court_calling_days X on X.ccd_case_no=C.coc_case_no " + Where_Sql
					+ "order by I.inv_det_ref_no";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();
				courtCaseDTO.setVehicleNo(rs.getString("coc_vehicle_no"));
				courtCaseDTO.setCaseNo(rs.getString("coc_case_no"));
				courtCaseDTO.setPermitNo(rs.getString("coc_permit_no"));
				courtCaseDTO.setRefrenceNo(rs.getString("inv_det_ref_no"));

				if (!rs.getString("coc_status").equals(null)) {
					if (rs.getString("coc_status").equals("A")) {

						courtCaseDTO.setStatus("Active");
					}

					if (rs.getString("coc_status").equals("I")) {

						courtCaseDTO.setStatus("Inactive");
					}
					if (rs.getString("coc_status").equals("C")) {

						courtCaseDTO.setStatus("Closed");
					}

				}
				searchedData.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return searchedData;

	}

	@Override
	public CourtCaseDTO getAjaxData(CourtCaseDTO ccDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String Where_Sql = null;

		CourtCaseDTO searchedData = new CourtCaseDTO();
		try {

			con = ConnectionManager.getConnection();

			if (ccDTO.getVehicleNo() != null && !ccDTO.getVehicleNo().trim().isEmpty()) {
				Where_Sql = "where coc_vehicle_no='" + ccDTO.getVehicleNo() + "'";

			} else if (ccDTO.getPermitNo() != null && !ccDTO.getPermitNo().trim().isEmpty()) {
				Where_Sql = "where  coc_permit_no='" + ccDTO.getPermitNo() + "' ";

			} else if (ccDTO.getCaseNo() != null && !ccDTO.getCaseNo().trim().isEmpty()) {
				Where_Sql = "where  coc_case_no='" + ccDTO.getCaseNo() + "'  ";

			} else if (ccDTO.getRefrenceNo() != null && !ccDTO.getRefrenceNo().trim().isEmpty()) {
				Where_Sql = "where I.inv_det_ref_no='" + ccDTO.getRefrenceNo() + "' ";

			}

			String sql = "select distinct coc_vehicle_no,coc_case_no,coc_permit_no,I.inv_det_ref_no,coc_status ,coc_route_no,coc_court_name,R.rou_service_origine,R.rou_service_destination\r\n"
					+ "from public.nt_m_court_cases C\r\n"
					+ "left outer join public.nt_t_flying_investigation_log_det I on I.inv_det_vehicle_no=C.coc_vehicle_no\r\n"
					+ "inner join public.nt_r_route R on R.rou_number=coc_route_no " + Where_Sql;

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				searchedData = new CourtCaseDTO();
				searchedData.setVehicleNo(rs.getString("coc_vehicle_no"));
				searchedData.setPermitNo(rs.getString("coc_permit_no"));
				searchedData.setRefrenceNo(rs.getString("inv_det_ref_no"));
				searchedData.setCourtName(rs.getString("coc_court_name"));
				searchedData.setOrgin(rs.getString("rou_service_origine"));
				searchedData.setDestination(rs.getString("rou_service_destination"));
				searchedData.setCaseNo(rs.getString("coc_case_no"));

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return searchedData;

	}

	@Override
	public ArrayList<CourtCaseDTO> getOrgin() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<CourtCaseDTO> routeDet = new ArrayList<CourtCaseDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "select distinct R.rou_service_origine\r\n" + "from public.nt_m_court_cases\r\n"
					+ "inner join public.nt_r_route R on R.rou_number=public.nt_m_court_cases.coc_route_no\r\n"
					+ "where R.active='A'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();
				courtCaseDTO.setOrgin(rs.getString("rou_service_origine"));
				routeDet.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return routeDet;

	}

	@Override
	public void updateCourtCaseCloseDate(CourtCaseDTO ccDTO, String remarks, String user) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String strDate = dateFormat.format(ccDTO.getCourtCaseCloseDate());

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_court_cases\r\n" + "SET coc_status =?, coc_court_close_date=?,\r\n"
					+ "coc_close_remark=?, coc_case_closed_by=?,\r\n" + "coc_case_closed_date=? \r\n"
					+ "WHERE coc_case_no =?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, "C");
			stmt.setString(2, strDate);
			stmt.setString(3, remarks);
			stmt.setString(4, user);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, ccDTO.getCaseNo());

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
	public void insertNextCallingDate(CourtCaseDTO ccDTO, String user) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_court_calling_days");

			String sql = "INSERT INTO public.nt_t_court_calling_days\r\n"
					+ "(ccd_seq, ccd_case_no, ccd_court_name, ccd_calling_date, ccd_status, ccd_created_by, ccd_created_date, ccd_court_case_seq)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, ccDTO.getCaseNo());
			stmt.setString(3, ccDTO.getCourtNameS());
			stmt.setString(4, dateFormat.format(ccDTO.getNextCallingDate()));
			stmt.setString(5, ccDTO.getStatus());
			stmt.setString(6, user);
			stmt.setTimestamp(7, timestamp);
			stmt.setLong(8, ccDTO.getCourtCaseSeq());

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
	public ArrayList<CourtCaseDTO> getnextCallingDateValues(String courtCaseNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<CourtCaseDTO> dataList = new ArrayList<CourtCaseDTO>();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT ccd_seq, ccd_case_no, ccd_court_name, ccd_calling_date, ccd_status, ccd_created_by, ccd_created_date, ccd_modified_by, ccd_modified_date, ccd_court_case_seq\r\n"
					+ "FROM public.nt_t_court_calling_days where ccd_case_no=? and ccd_status not in ('D') ;\r\n" + "";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, courtCaseNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();
				courtCaseDTO.setCaseNoS(rs.getString("ccd_case_no"));
				courtCaseDTO.setCourtNameS(rs.getString("ccd_court_name"));
				courtCaseDTO.setNextCallingDateS(rs.getString("ccd_calling_date"));
				courtCaseDTO.setCourtCaseSeqS(rs.getLong("ccd_seq"));

				dataList.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return dataList;

	}

	@Override
	public void updateNextCallingDate(CourtCaseDTO ccDTO, String user) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_court_calling_days \r\n" + "SET ccd_court_name =?, ccd_calling_date=?,\r\n"
					+ " ccd_modified_by=?, ccd_modified_date =?\r\n" + "WHERE  ccd_case_no  =? and  ccd_seq=?   ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ccDTO.getCourtNameS());
			stmt.setString(2, dateFormat.format(ccDTO.getNextCallingDate()));
			stmt.setString(3, user);
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, ccDTO.getCaseNo());
			stmt.setLong(6, ccDTO.getCourtCaseSeqS());

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
	public void recordUpdateAsDelete(CourtCaseDTO ccDTO, String user) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_court_calling_days\r\n" + "SET ccd_status =?, ccd_deleted_by=?,\r\n"
					+ "ccd_deleted_date=? \r\n" + "WHERE ccd_case_no =? and   ccd_seq =?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, "D");
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, ccDTO.getCaseNoS());
			stmt.setLong(5, ccDTO.getCourtCaseSeqS());

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
	public CourtCaseDTO getManageCourtcaseViewData(String caseNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		CourtCaseDTO courtCaseDTO = new CourtCaseDTO();

		try {

			con = ConnectionManager.getConnection();
			String sql = "SELECT coc_seq, coc_vehicle_no, coc_permit_no,group_name,\r\n"
					+ "coc_route_no, coc_court_name, coc_case_no,\r\n"
					+ "coc_court_date, coc_group_code, coc_status, \r\n" + "coc_created_by, coc_created_date, \r\n"
					+ "coc_modified_by, coc_modified_date , coc_remarks,coc_action_taken , coc_inspection_date\r\n"
					+ "FROM public.nt_m_court_cases,nt_r_court_group\r\n"
					+ "where coc_group_code=code and coc_case_no=?  ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, caseNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String sDate1 = rs.getString("coc_court_date");
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);

				courtCaseDTO = new CourtCaseDTO();

				courtCaseDTO.setCourtCaseSeq(rs.getLong("coc_seq"));
				courtCaseDTO.setVehicleNo(rs.getString("coc_vehicle_no"));
				courtCaseDTO.setPermitNo(rs.getString("coc_permit_no"));
				courtCaseDTO.setRouteNo(rs.getString("coc_route_no"));
				courtCaseDTO.setCourtName(rs.getString("coc_court_name"));
				courtCaseDTO.setCaseNo(rs.getString("coc_case_no"));
				courtCaseDTO.setDateofCourtCase(date1);

				if (rs.getString("coc_group_code").equals("01")) {

					courtCaseDTO.setGroupCode("Group 01");
				} else {
					courtCaseDTO.setGroupCode("Group 02");
				}
				courtCaseDTO.setStatus(rs.getString("coc_status"));
				courtCaseDTO.setCreatedBy(rs.getString("coc_created_by"));
				courtCaseDTO.setCreatedDate(rs.getTimestamp("coc_created_date"));
				courtCaseDTO.setModifyBy(rs.getString("coc_modified_by"));
				courtCaseDTO.setModifiedDate(rs.getTimestamp("coc_modified_date"));
				courtCaseDTO.setGroupName(rs.getString("group_name"));
				courtCaseDTO.setRemarks(rs.getString("coc_remarks"));
				courtCaseDTO.setActionByCourt(rs.getString("coc_action_taken"));
				courtCaseDTO.setInspDate(rs.getString("coc_inspection_date"));

				if (courtCaseDTO.getStatus().equalsIgnoreCase("A")) {
					courtCaseDTO.setStatusdes("Active");
				} else if (courtCaseDTO.getStatus().equalsIgnoreCase("C")) {
					courtCaseDTO.setStatusdes("Closed");
				} else {
					courtCaseDTO.setStatusdes("Inactive");

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return courtCaseDTO;

	}

	@Override
	public List<Object> getCourtNames() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		String name1 = null;
		String name2 = null;

		List<String> courtName = new ArrayList<String>();
		List<Object> listUnique = null;
		try {
			con = ConnectionManager.getConnection();
			String sql = "select distinct coc_court_name from public.nt_m_court_cases;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				name1 = null;

				name1 = rs.getString("coc_court_name");

				courtName.add(name1);
			}
			String sql2 = "select distinct ccd_court_name from public.nt_t_court_calling_days;";

			stmt2 = con.prepareStatement(sql2);
			rs2 = stmt2.executeQuery();

			while (rs2.next()) {
				name2 = null;

				name2 = rs2.getString("ccd_court_name");

				courtName.add(name2);
			}

			listUnique = courtName.stream().distinct().collect(Collectors.toList());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return listUnique;

	}

// progress report
	@Override
	public void insertTotalHoursForGroup(String startDate, String endDate, String cumalativeDate) {

		Connection con = null;
		PreparedStatement stmt = null, stmt2 = null, stmt3 = null, stmt4 = null, stmt5 = null, stmt6 = null,
				stmt7 = null, stmt8 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		String selectQuery = null;
		String selectQuery2 = null;
		String selectQuery3 = null;
		String selectQuery4 = null;
		String updateQuery = null;
		String updateQuery2 = null;
		String updateQuery3 = null;

		ArrayList<CourtCaseDTO> selectedList = new ArrayList<>();
		ArrayList<CourtCaseDTO> selectedList2 = new ArrayList<>();
		ArrayList<CourtCaseDTO> selectedList3 = new ArrayList<>();
		ArrayList<CourtCaseDTO> selectedList4 = new ArrayList<>();
		CourtCaseDTO dto = new CourtCaseDTO();
		CourtCaseDTO dto2 = new CourtCaseDTO();
		CourtCaseDTO dto3 = new CourtCaseDTO();
		CourtCaseDTO dto4 = new CourtCaseDTO();
		try {

			con = ConnectionManager.getConnection();
			// get group names and related hours
			selectQuery = "select  G.group_name,\r\n"
					+ "sum(extract( hours from ( inv_outtime::time-inv_intime:: time))) as totHours\r\n"
					+ "from public.nt_t_flying_investigation_attendance\r\n"
					+ "inner join public.nt_r_group G on G.code= inv_group_cd\r\n" + "where  inv_attended='true' \r\n"
					+ "and to_char(inv_date ,'yyyy-MM-dd')>=?\r\n" + "and to_char(inv_date ,'yyyy-MM-dd')<=? \r\n"
					+ "group by G.group_name\r\n" + "order by G.group_name";

			stmt = con.prepareStatement(selectQuery);
			stmt.setString(1, startDate);
			stmt.setString(2, endDate);
			rs = stmt.executeQuery();

			while (rs.next()) {
				dto = new CourtCaseDTO();
				dto.setGroupName(rs.getString("group_name"));
				dto.setTotHours(rs.getInt("totHours"));
				selectedList.add(dto);

			}
			// Insert group names and related hours
			if (selectedList != null) {
				for (CourtCaseDTO cc : selectedList) {
					String sql = "INSERT INTO public.nt_m_temp_team_hours\r\n" + "(group_name, tot_hours)\r\n"
							+ "VALUES(?,?);\r\n" + "";
					stmt2 = con.prepareStatement(sql);

					stmt2.setString(1, cc.getGroupName());
					stmt2.setInt(2, (int) cc.getTotHours());
					stmt2.executeUpdate();

				}
				con.commit();
			}
			// get group names and related hours for cumalative values
			selectQuery2 = "select  G.group_name,\r\n"
					+ "sum(extract( hours from ( inv_outtime::time-inv_intime:: time))) as totHoursCum\r\n"
					+ "from public.nt_t_flying_investigation_attendance\r\n"
					+ "inner join public.nt_r_group G on G.code= inv_group_cd\r\n" + "where  inv_attended='true' \r\n"
					+ "and to_char(inv_date ,'yyyy-MM-dd')>=?\r\n" + "and to_char(inv_date ,'yyyy-MM-dd')<=? \r\n"
					+ "group by G.group_name\r\n" + "order by G.group_name";
			;

			stmt3 = con.prepareStatement(selectQuery2);
			stmt3.setString(1, cumalativeDate);
			stmt3.setString(2, endDate);
			rs2 = stmt3.executeQuery();

			while (rs2.next()) {
				dto2 = new CourtCaseDTO();
				dto2.setGroupName(rs2.getString("group_name"));
				dto2.setTotHoursCum(rs2.getInt("totHoursCum"));
				selectedList2.add(dto2);

			}
			// update group names and related hours for cumalative values
			if (selectedList2 != null) {
				for (CourtCaseDTO cc1 : selectedList2) {
					updateQuery = "UPDATE public.nt_m_temp_team_hours \r\n" + "SET tot_hours_cum  =? \r\n"
							+ "WHERE  group_name  =?   ";

					stmt3 = con.prepareStatement(updateQuery);

					stmt3.setInt(1, (int) cc1.getTotHoursCum());
					stmt3.setString(2, cc1.getGroupName());

					int i = stmt3.executeUpdate();

					if (i <= 0) {
						String sql2 = "INSERT INTO public.nt_m_temp_team_hours\r\n"
								+ "(group_name, tot_hours,tot_hours_cum)\r\n" + "VALUES(?,?,?);\r\n" + "";
						stmt8 = con.prepareStatement(sql2);

						stmt8.setString(1, cc1.getGroupName());
						stmt8.setInt(2, 0);
						stmt8.setInt(3, (int) cc1.getTotHoursCum());
						stmt8.executeUpdate();

					}

				}
				con.commit();

			}

			// get inspected vehicle count
			selectQuery3 = "select G.group_name,inv_date,count(a.inv_det_vehicle_no) as inspectedVehiCount\r\n"
					+ "from public.nt_t_flying_investigation_log_master\r\n"
					+ "inner join public.nt_t_flying_investigation_log_det a on a.inv_det_ref_no=inv_referenceno  and a.inv_det_delete is null \r\n"
					+ "and a.inv_service_category ='NT'\r\n"
					+ "inner join public.nt_r_group G on G.code= inv_group_cd\r\n"
					+ "where to_char(inv_date ,'yyyy-MM-dd')>=?\r\n" + "and to_char(inv_date ,'yyyy-MM-dd')<=?\r\n"
					+ "group by G.group_name,inv_date\r\n" + "order by inv_date";
			;

			stmt4 = con.prepareStatement(selectQuery3);
			stmt4.setString(1, startDate);
			stmt4.setString(2, endDate);
			rs3 = stmt4.executeQuery();

			while (rs3.next()) {
				dto3 = new CourtCaseDTO();
				dto3.setGroupName(rs3.getString("group_name"));
				dto3.setInspectedVehiCount(rs3.getInt("inspectedVehiCount"));
				selectedList3.add(dto3);

			}
			if (selectedList3 != null) {
				for (CourtCaseDTO cc2 : selectedList3) {
					updateQuery2 = "UPDATE public.nt_m_temp_team_hours \r\n" + "SET inspected_vehi_count   =? \r\n"
							+ "WHERE  group_name  =?   ";

					stmt5 = con.prepareStatement(updateQuery2);

					stmt5.setInt(1, (int) cc2.getInspectedVehiCount());
					stmt5.setString(2, cc2.getGroupName());

					stmt5.executeUpdate();
				}
				con.commit();

			}

			// get inspected vehicle count for cumalative
			selectQuery4 = "select G.group_name,inv_date,count(a.inv_det_vehicle_no) as inspectedVehiCount\r\n"
					+ "from public.nt_t_flying_investigation_log_master\r\n"
					+ "inner join public.nt_t_flying_investigation_log_det a on a.inv_det_ref_no=inv_referenceno  and a.inv_det_delete is null \r\n"
					+ "and a.inv_service_category ='NT'\r\n"
					+ "inner join public.nt_r_group G on G.code= inv_group_cd\r\n"
					+ "where to_char(inv_date ,'yyyy-MM-dd')>=?\r\n" + "and to_char(inv_date ,'yyyy-MM-dd')<=?\r\n"
					+ "group by G.group_name,inv_date \r\n" + "order by inv_date";
			;

			stmt6 = con.prepareStatement(selectQuery4);
			stmt6.setString(1, cumalativeDate);
			stmt6.setString(2, endDate);
			rs4 = stmt6.executeQuery();

			while (rs4.next()) {
				dto4 = new CourtCaseDTO();
				dto4.setGroupName(rs4.getString("group_name"));
				dto4.setInspectedVehiCount(rs4.getInt("inspectedVehiCount"));
				selectedList4.add(dto4);

			}
			if (selectedList4 != null) {
				for (CourtCaseDTO cc3 : selectedList4) {
					updateQuery3 = "UPDATE public.nt_m_temp_team_hours \r\n" + "SET inspected_vehi_count_cum    =? \r\n"
							+ "WHERE  group_name  =?   ";

					stmt7 = con.prepareStatement(updateQuery3);

					stmt7.setInt(1, (int) cc3.getInspectedVehiCount());
					stmt7.setString(2, cc3.getGroupName());

					stmt7.executeUpdate();

				}
				con.commit();

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(stmt7);
			ConnectionManager.close(stmt8);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs2);
			ConnectionManager.close(rs3);
			ConnectionManager.close(rs4);
			ConnectionManager.close(con);

		}

	}

	@Override
	public void deleteTempTab() {
		Connection con = null;
		PreparedStatement ps = null;

		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "DELETE FROM  public.nt_m_temp_team_hours;";

			ps = con.prepareStatement(query);

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
	public void InsertTempData(String startDate, String endDate) {

		Connection con = null;
		PreparedStatement stmt = null, stmt2 = null, stmt3 = null, stmt4 = null, stmt5 = null, stmt6 = null,
				stmt7 = null, stmt8 = null, stmt9 = null, stmt10 = null;

		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;

		String selectQuery = null;
		String selectQuery2 = null;
		String selectQuery3 = null;
		String selectQuery4 = null;
		String updateQuery = null;
		String updateQuery2 = null;
		String updateQuery3 = null;

		ArrayList<CourtCaseDTO> selectedList = new ArrayList<>();
		ArrayList<CourtCaseDTO> selectedList2 = new ArrayList<>();
		ArrayList<CourtCaseDTO> selectedList3 = new ArrayList<>();
		ArrayList<CourtCaseDTO> selectedList4 = new ArrayList<>();
		CourtCaseDTO dto = new CourtCaseDTO();
		CourtCaseDTO dto2 = new CourtCaseDTO();
		CourtCaseDTO dto3 = new CourtCaseDTO();
		CourtCaseDTO dto4 = new CourtCaseDTO();
		try {

			con = ConnectionManager.getConnection();

			selectQuery = "select  to_char(inv_date,'yyyy-MM-dd') as invesDate ,G.group_name,count(inv_empno)as employeecount,\r\n"
					+ "sum(extract( hours from ( inv_outtime::time-inv_intime:: time))) as totHours\r\n"
					+ "from public.nt_t_flying_investigation_attendance\r\n"
					+ "inner join public.nt_r_group G on G.code= inv_group_cd\r\n" + "where  inv_attended='true'\r\n"
					+ "and to_char(inv_date ,'yyyy-MM-dd')>=?\r\n" + "and to_char(inv_date ,'yyyy-MM-dd')<=? \r\n"
					+ "group by G.group_name,inv_date\r\n" + "order by inv_date;;";

			stmt = con.prepareStatement(selectQuery);
			stmt.setString(1, startDate);
			stmt.setString(2, endDate);
			rs = stmt.executeQuery();

			while (rs.next()) {
				dto = new CourtCaseDTO();
				dto.setInvesDate(rs.getString("invesDate"));
				dto.setGroupName(rs.getString("group_name"));
				dto.setTotHours(rs.getInt("totHours"));
				dto.setEmployeeCount(rs.getInt("employeecount"));
				selectedList.add(dto);

			}
			if (selectedList != null) {
				for (CourtCaseDTO cc : selectedList) {
					String sql = "INSERT INTO public.nt_m_flying_temp_progress\r\n"
							+ "(dates , group_name ,count_of_employees ,count_of_hours )\r\n" + "VALUES(?,?,?,?);\r\n"
							+ "";
					stmt2 = con.prepareStatement(sql);

					stmt2.setString(1, cc.getInvesDate());
					stmt2.setString(2, cc.getGroupName());
					stmt2.setInt(3, (int) cc.getEmployeeCount());
					stmt2.setInt(4, (int) cc.getTotHours());
					stmt2.executeUpdate();

				}
				con.commit();
			}
			selectQuery2 = "select G.group_name,inv_date,count(a.inv_det_vehicle_no) as inspectedVehiCount\r\n"
					+ "from public.nt_t_flying_investigation_log_master \r\n"
					+ "inner join public.nt_t_flying_investigation_log_det a on a.inv_det_ref_no=inv_referenceno\r\n"
					+ "inner join public.nt_r_group G on G.code= inv_group_cd\r\n"
					+ "where to_char(inv_date ,'yyyy-MM-dd')>=?\r\n" + "and to_char(inv_date ,'yyyy-MM-dd')<=? \r\n"
					+ "group by G.group_name,inv_date\r\n" + "order by inv_date;";

			stmt3 = con.prepareStatement(selectQuery2);
			stmt3.setString(1, startDate);
			stmt3.setString(2, endDate);
			rs2 = stmt3.executeQuery();

			while (rs2.next()) {
				dto2 = new CourtCaseDTO();
				dto2.setInvesDate(rs2.getString("inv_date"));
				dto2.setGroupName(rs2.getString("group_name"));
				dto2.setInspectedVehiCount(rs2.getInt("inspectedVehiCount"));

				selectedList2.add(dto2);

			}
			if (selectedList2 != null) {
				for (CourtCaseDTO cc1 : selectedList2) {
					updateQuery = "UPDATE public.nt_m_flying_temp_progress \r\n"
							+ "SET count_of_inspectd_vehi   =? \r\n" + "WHERE dates=? and  group_name  =?   ";

					stmt3 = con.prepareStatement(updateQuery);

					stmt3.setInt(1, (int) cc1.getInspectedVehiCount());
					stmt3.setString(2, cc1.getInvesDate());
					stmt3.setString(3, cc1.getGroupName());

					int i = stmt3.executeUpdate();
					if (i <= 0) {
						String inspecInsertQ = "INSERT INTO public.nt_m_flying_temp_progress\r\n"
								+ "(dates , group_name  ,count_of_inspectd_vehi )\r\n" + "VALUES(?,?,?);\r\n" + "";

						stmt6 = con.prepareStatement(inspecInsertQ);

						stmt6.setString(1, cc1.getInvesDate());
						stmt6.setString(2, cc1.getGroupName());
						stmt6.setInt(3, (int) cc1.getInspectedVehiCount());
						stmt6.executeUpdate();

					}
				}
				con.commit();

			}

			selectQuery3 = "select G.group_name,inv_date, count(distinct a.vio_vehicleno) as vioConditionVehiCount,count(a.vio_condition_cd) as vioConditionCount\r\n"
					+ "from public.nt_t_flying_investigation_log_master \r\n"
					+ "inner join public.nt_t_flying_vio_conditions a on a.vio_refno=inv_referenceno and a.vio_violated_status='true'\r\n"
					+ "inner join public.nt_r_group G on G.code= inv_group_cd\r\n"
					+ "where to_char(inv_date ,'yyyy-MM-dd')>=?\r\n" + "and to_char(inv_date ,'yyyy-MM-dd')<=? \r\n"
					+ "group by G.group_name,inv_date\r\n" + "order by inv_date;;";

			stmt4 = con.prepareStatement(selectQuery3);
			stmt4.setString(1, startDate);
			stmt4.setString(2, endDate);
			rs3 = stmt4.executeQuery();

			while (rs3.next()) {
				dto3 = new CourtCaseDTO();
				dto3.setInvesDate(rs3.getString("inv_date"));
				dto3.setGroupName(rs3.getString("group_name"));
				dto3.setVioVehiConditionCount(rs3.getInt("vioConditionVehiCount"));
				dto3.setVioConditionCount(rs3.getInt("vioConditionCount"));

				selectedList3.add(dto3);

			}
			if (selectedList3 != null) {
				for (CourtCaseDTO cc2 : selectedList3) {
					updateQuery2 = "UPDATE public.nt_m_flying_temp_progress \r\n"
							+ "SET count_of_violated_condi    =?,count_of_violated_condi_vehi =? \r\n"
							+ "WHERE dates=? and  group_name  =?   ";

					stmt5 = con.prepareStatement(updateQuery2);

					stmt5.setInt(1, (int) cc2.getVioConditionCount());
					stmt5.setInt(2, (int) cc2.getVioVehiConditionCount());
					stmt5.setString(3, cc2.getInvesDate());
					stmt5.setString(4, cc2.getGroupName());

					int j = stmt5.executeUpdate();
					if (j <= 0) {
						String inspecInsertQ1 = "INSERT INTO public.nt_m_flying_temp_progress\r\n"
								+ "(dates , group_name ,count_of_violated_condi ,count_of_violated_condi_vehi )\r\n"
								+ "VALUES(?,?,?,?);\r\n" + "";

						stmt7 = con.prepareStatement(inspecInsertQ1);

						stmt7.setString(1, cc2.getInvesDate());
						stmt7.setString(2, cc2.getGroupName());
						stmt7.setInt(3, (int) cc2.getVioConditionCount());
						stmt7.setInt(4, (int) cc2.getVioVehiConditionCount());
						stmt7.executeUpdate();

					}
				}
				con.commit();

			}

			// select update and insert detected buses
			selectQuery4 = "select count(charge_vehicle) as detectedVehiCount, G.group_name,a.charge_inv_date\r\n"
					+ "from nt_t_investigation_charge_master a\r\n"
					+ "inner join nt_t_flying_investigation_log_det b on\r\n"
					+ "b.inv_investigation_no =a.charge_ref_no\r\n"
					+ "inner join public.nt_t_flying_investigation_log_master c on\r\n"
					+ "c.inv_referenceno=b.inv_det_ref_no\r\n" + "inner join public.nt_r_group G on \r\n"
					+ "G.code=c.inv_group_cd\r\n" + "where to_char(a.charge_inv_date ,'yyyy-MM-dd')>=?\r\n"
					+ "and to_char(a.charge_inv_date ,'yyyy-MM-dd')<=? \r\n"
					+ "group by G.group_name,a.charge_inv_date;";

			stmt8 = con.prepareStatement(selectQuery4);
			stmt8.setString(1, startDate);
			stmt8.setString(2, endDate);
			rs4 = stmt8.executeQuery();

			while (rs4.next()) {
				dto4 = new CourtCaseDTO();
				dto4.setInvesDate(rs4.getString("charge_inv_date"));
				dto4.setGroupName(rs4.getString("group_name"));
				dto4.setDetectedVehiCount(rs4.getInt("detectedVehiCount"));

				selectedList4.add(dto4);

			}
			if (selectedList4 != null) {
				for (CourtCaseDTO cc3 : selectedList4) {
					updateQuery3 = "UPDATE public.nt_m_flying_temp_progress \r\n"
							+ "SET count_of_detectd_vehi=?    \r\n" + "WHERE dates=? and  group_name  =?   ";

					stmt9 = con.prepareStatement(updateQuery3);

					stmt9.setInt(1, (int) cc3.getDetectedVehiCount());

					stmt9.setString(2, cc3.getInvesDate());
					stmt9.setString(3, cc3.getGroupName());

					int k = stmt9.executeUpdate();
					if (k <= 0) {
						String inspecInsertQ2 = "INSERT INTO public.nt_m_flying_temp_progress\r\n"
								+ "(dates , group_name ,count_of_detectd_vehi )\r\n" + "VALUES(?,?,?);\r\n" + "";

						stmt10 = con.prepareStatement(inspecInsertQ2);

						stmt10.setString(1, cc3.getInvesDate());
						stmt10.setString(2, cc3.getGroupName());
						stmt10.setInt(3, (int) cc3.getDetectedVehiCount());

						stmt10.executeUpdate();

					}
				}
				con.commit();

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(stmt7);
			ConnectionManager.close(stmt8);
			ConnectionManager.close(stmt9);
			ConnectionManager.close(stmt10);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs2);
			ConnectionManager.close(rs3);
			ConnectionManager.close(rs4);
			ConnectionManager.close(con);

		}

	}

	@Override
	public void deleteFlyingProgressTempTab() {
		Connection con = null;
		PreparedStatement ps = null;

		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "DELETE FROM  public.nt_m_flying_temp_progress;";

			ps = con.prepareStatement(query);

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

	public void insertdataN(CourtCaseDTO courtCaseDTO, String user) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String strDate2 = null;

		if (courtCaseDTO.getInspectStartDate() != null) {
			strDate2 = dateFormat.format(courtCaseDTO.getInspectStartDate());

		}

		try {

			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_court_cases");

			String sql = "INSERT INTO public.nt_m_court_cases\r\n"
					+ "(coc_seq, coc_vehicle_no, coc_permit_no, coc_route_no, coc_court_name,\r\n"
					+ "coc_case_no, coc_court_date, coc_group_code, coc_status, coc_created_by, coc_created_date,coc_inspection_date,coc_remarks,coc_action_taken,coc_route_orgin,coc_route_destination)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, courtCaseDTO.getVehicleNo());
			if (courtCaseDTO.getPermitNo() != null) {
				stmt.setString(3, courtCaseDTO.getPermitNo());
			} else
				stmt.setString(3, null);
			stmt.setString(4, courtCaseDTO.getRouteNo());
			stmt.setString(5, courtCaseDTO.getCourtName());
			stmt.setString(6, courtCaseDTO.getCaseNo());
			stmt.setString(7, dateFormat.format(courtCaseDTO.getDateofCourtCase()));
			stmt.setString(8, courtCaseDTO.getGroupCode());
			stmt.setString(9, courtCaseDTO.getStatus());
			stmt.setString(10, user);
			stmt.setTimestamp(11, timestamp);
			stmt.setString(12, strDate2);
			stmt.setString(13, courtCaseDTO.getRemarks());
			stmt.setString(14, courtCaseDTO.getActionByCourt());
			stmt.setString(15, courtCaseDTO.getOrgin());
			stmt.setString(16, courtCaseDTO.getDestination());

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

	public void updatedataN(CourtCaseDTO courtCaseDTO, String user) {
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		String strDate2 = null;

		String strDate = dateFormat.format(courtCaseDTO.getDateofCourtCase());
		if (courtCaseDTO.getInspectionDate() != null) {
			strDate = dateFormat.format(courtCaseDTO.getInspectionDate());

		}
		if (courtCaseDTO.getInspectStartDate() != null) {
			strDate2 = dateFormat.format(courtCaseDTO.getInspectStartDate());

		}

		try {

			con = ConnectionManager.getConnection();

			String sql1 = "INSERT into public.nt_h_court_cases  "
					+ " (SELECT * FROM public.nt_m_court_cases WHERE coc_seq  = ?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setLong(1, courtCaseDTO.getCourtCaseSeq());
			stmt1.executeUpdate();

			String sql = "UPDATE public.nt_m_court_cases\r\n" + "SET coc_court_name=?, coc_court_date=?,\r\n"
					+ "coc_group_code=?, coc_status=?,\r\n"
					+ "coc_modified_by=?, coc_modified_date=?, coc_remarks=?,coc_action_taken=? , coc_inspection_date=?,\r\n"
					+ " coc_vehicle_no=?, coc_permit_no=?, coc_route_no=?, coc_route_orgin=?, coc_route_destination=?, coc_case_no=? "
					+ "WHERE coc_seq=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, courtCaseDTO.getCourtName());
			stmt.setString(2, strDate);
			stmt.setString(3, courtCaseDTO.getGroupCode());
			stmt.setString(4, courtCaseDTO.getStatus());
			stmt.setString(5, user);
			stmt.setTimestamp(6, timestamp);
			stmt.setString(7, courtCaseDTO.getRemarks());
			stmt.setString(8, courtCaseDTO.getActionByCourt());
			stmt.setString(9, strDate2);
			stmt.setString(10, courtCaseDTO.getVehicleNo());
			stmt.setString(11, courtCaseDTO.getPermitNo());
			stmt.setString(12, courtCaseDTO.getRouteNo());
			stmt.setString(13, courtCaseDTO.getOrgin());
			stmt.setString(14, courtCaseDTO.getDestination());
			stmt.setString(15, courtCaseDTO.getCaseNo());

			stmt.setLong(16, courtCaseDTO.getCourtCaseSeq());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(con);

		}

	}

	public ArrayList<CourtCaseDTO> getcourtcaseListN(String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		ArrayList<CourtCaseDTO> CourtCaseDTOlist = new ArrayList<CourtCaseDTO>();
		try {
			con = ConnectionManager.getConnection();
			if (status.equalsIgnoreCase("C")) {
				sql = "SELECT coc_seq, coc_vehicle_no, coc_permit_no,group_name,\r\n"
						+ "coc_route_no, coc_court_name, coc_case_no,\r\n"
						+ "coc_court_date, coc_group_code, coc_status, \r\n" + "coc_created_by, coc_created_date, \r\n"
						+ "coc_modified_by, coc_modified_date, coc_remarks,coc_action_taken , coc_inspection_date,coc_route_orgin,coc_route_destination \r\n"
						+ "FROM public.nt_m_court_cases,nt_r_court_group\r\n"
						+ "where coc_group_code=code and nt_m_court_cases.coc_status != 'C' \r\n"
						+ "order by coc_seq desc";
			} else {
				sql = "SELECT coc_seq, coc_vehicle_no, coc_permit_no,group_name,\r\n"
						+ "coc_route_no, coc_court_name, coc_case_no,\r\n"
						+ "coc_court_date, coc_group_code, coc_status, \r\n" + "coc_created_by, coc_created_date, \r\n"
						+ "coc_modified_by, coc_modified_date, coc_remarks,coc_action_taken , coc_inspection_date,coc_route_orgin,coc_route_destination \r\n"
						+ "FROM public.nt_m_court_cases,nt_r_court_group\r\n" + "where coc_group_code=code \r\n"
						+ "order by coc_seq desc";
			}

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();

				courtCaseDTO.setCourtCaseSeq(rs.getLong("coc_seq"));
				courtCaseDTO.setVehicleNo(rs.getString("coc_vehicle_no"));
				courtCaseDTO.setPermitNo(rs.getString("coc_permit_no"));
				courtCaseDTO.setRouteNo(rs.getString("coc_route_no"));
				courtCaseDTO.setCourtName(rs.getString("coc_court_name"));
				courtCaseDTO.setCaseNo(rs.getString("coc_case_no"));
				courtCaseDTO.setDateOfCourt(rs.getString("coc_court_date"));
				courtCaseDTO.setGroupCode(rs.getString("coc_group_code"));
				courtCaseDTO.setStatus(rs.getString("coc_status"));
				courtCaseDTO.setCreatedBy(rs.getString("coc_created_by"));
				courtCaseDTO.setCreatedDate(rs.getTimestamp("coc_created_date"));
				courtCaseDTO.setModifyBy(rs.getString("coc_modified_by"));
				courtCaseDTO.setModifiedDate(rs.getTimestamp("coc_modified_date"));
				courtCaseDTO.setGroupName(rs.getString("group_name"));

				courtCaseDTO.setRemarks(rs.getString("coc_remarks"));
				courtCaseDTO.setActionByCourt(rs.getString("coc_action_taken"));
				courtCaseDTO.setInspDate(rs.getString("coc_inspection_date"));
				courtCaseDTO.setOrgin(rs.getString("coc_route_orgin"));
				courtCaseDTO.setDestination(rs.getString("coc_route_destination"));

				if (courtCaseDTO.getStatus().equalsIgnoreCase("A")) {
					courtCaseDTO.setStatusdes("Active");
				} else if (courtCaseDTO.getStatus().equalsIgnoreCase("I")) {
					courtCaseDTO.setStatusdes("Inactive");
				} else if (courtCaseDTO.getStatus().equalsIgnoreCase("C")) {
					courtCaseDTO.setStatusdes("Closed");
				}

				CourtCaseDTOlist.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return CourtCaseDTOlist;

	}

	public ArrayList<CourtCaseDTO> getcourtcaseListwithparamN(String status, String vehicleno, String permitno,
			String courtcaseno, String groupNo, Date startDate, Date endDate) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String whereSql = null;

		ArrayList<CourtCaseDTO> CourtCaseDTOlist = new ArrayList<CourtCaseDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

		try {

			if (status != null && !status.equalsIgnoreCase("") && !status.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and coc_status =" + "'" + status + "'";

				}
			}

			if (vehicleno != null && !vehicleno.equalsIgnoreCase("") && !vehicleno.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and coc_vehicle_no =" + "'" + vehicleno + "'";

				} else {
					whereSql = whereSql + "and coc_vehicle_no =" + "'" + vehicleno + "'";
				}
			}

			if (permitno != null && !permitno.equalsIgnoreCase("") && !permitno.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and coc_permit_no =" + "'" + permitno + "'";

				} else {
					whereSql = whereSql + "and coc_permit_no =" + "'" + permitno + "'";
				}
			}

			if (groupNo != null && !groupNo.equalsIgnoreCase("") && !groupNo.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and coc_group_code =" + "'" + groupNo + "'";

				} else {
					whereSql = whereSql + "and coc_group_code =" + "'" + groupNo + "'";
				}
			}

			if (courtcaseno != null && !courtcaseno.equalsIgnoreCase("") && !courtcaseno.isEmpty()) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "and coc_case_no =" + "'" + courtcaseno + "'";

				} else {
					whereSql = whereSql + "and coc_case_no =" + "'" + courtcaseno + "'";
				}
			}

			if (startDate != null) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = " and coc_court_date >=" + "'" + sdf.format(startDate) + "'";

				}

				else {
					whereSql = whereSql + " and coc_court_date >=" + "'" + sdf.format(startDate) + "'";
				}

			}

			if (endDate != null) {
				if (whereSql == null || whereSql.equalsIgnoreCase("") || whereSql.isEmpty()) {
					whereSql = "  and coc_court_date <=" + "'" + sdf.format(endDate) + "'";

				}

				else {
					whereSql = whereSql + "  and coc_court_date <=" + "'" + sdf.format(endDate) + "'";
				}

			}

			con = ConnectionManager.getConnection();
			String sql = "SELECT coc_seq, coc_vehicle_no, coc_permit_no,group_name,\r\n"
					+ "coc_route_no, coc_court_name, coc_case_no,\r\n"
					+ "coc_court_date, coc_group_code, coc_status, \r\n" + "coc_created_by, coc_created_date, \r\n"
					+ "coc_modified_by, coc_modified_date , coc_remarks,coc_action_taken , coc_inspection_date,coc_route_orgin,coc_route_destination\r\n"
					+ "FROM public.nt_m_court_cases,nt_r_court_group\r\n" + "where coc_group_code=code\r\n" + whereSql
					+ "order by coc_seq";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {

				CourtCaseDTO courtCaseDTO = new CourtCaseDTO();

				courtCaseDTO.setCourtCaseSeq(rs.getLong("coc_seq"));
				courtCaseDTO.setVehicleNo(rs.getString("coc_vehicle_no"));
				courtCaseDTO.setPermitNo(rs.getString("coc_permit_no"));
				courtCaseDTO.setRouteNo(rs.getString("coc_route_no"));
				courtCaseDTO.setCourtName(rs.getString("coc_court_name"));
				courtCaseDTO.setCaseNo(rs.getString("coc_case_no"));
				courtCaseDTO.setDateOfCourt(rs.getString("coc_court_date"));
				courtCaseDTO.setGroupCode(rs.getString("coc_group_code"));
				courtCaseDTO.setStatus(rs.getString("coc_status"));
				courtCaseDTO.setCreatedBy(rs.getString("coc_created_by"));
				courtCaseDTO.setCreatedDate(rs.getTimestamp("coc_created_date"));
				courtCaseDTO.setModifyBy(rs.getString("coc_modified_by"));
				courtCaseDTO.setModifiedDate(rs.getTimestamp("coc_modified_date"));
				courtCaseDTO.setGroupName(rs.getString("group_name"));
				courtCaseDTO.setRemarks(rs.getString("coc_remarks"));
				courtCaseDTO.setActionByCourt(rs.getString("coc_action_taken"));
				courtCaseDTO.setInspDate(rs.getString("coc_inspection_date"));
				courtCaseDTO.setOrgin(rs.getString("coc_route_orgin"));
				courtCaseDTO.setDestination(rs.getString("coc_route_destination"));

				if (courtCaseDTO.getStatus().equalsIgnoreCase("A")) {
					courtCaseDTO.setStatusdes("Active");
				} else if (courtCaseDTO.getStatus().equalsIgnoreCase("C")) {
					courtCaseDTO.setStatusdes("Closed");
				} else {
					courtCaseDTO.setStatusdes("Inactive");
				}

				CourtCaseDTOlist.add(courtCaseDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return CourtCaseDTOlist;

	}

	@Override
	public List<String> getAllVehicleN() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> drpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT coc_vehicle_no  FROM nt_m_court_cases;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				drpdwnDTOList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return drpdwnDTOList;
	}

	@Override
	public List<String> getAllPermitN() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> drpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT coc_permit_no FROM nt_m_court_cases WHERE coc_permit_no is not null;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				drpdwnDTOList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return drpdwnDTOList;
	}

	@Override
	public boolean checkCourtCaseNo(String number) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isFound = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_court_cases where coc_case_no=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, number);

			rs = ps.executeQuery();

			if (rs.next()) {
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

	/** end **/
}
