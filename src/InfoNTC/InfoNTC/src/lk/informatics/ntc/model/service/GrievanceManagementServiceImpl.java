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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CommittedOffencesDTO;
import lk.informatics.ntc.model.dto.ComplaintActionDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class GrievanceManagementServiceImpl implements GrievanceManagementService {

	@Override
	public synchronized String generateCIFNo(String complainType) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps_count = null;
		ResultSet rs = null;
		ResultSet rs_count = null;
		String format = null;
		long number = 0;
		String generatedCode = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT string_value, type FROM nt_r_parameters WHERE param_name = ?;";
			String param = "COMPLAINT_NO";

			if (complainType.equals("C"))
				param = "COMPLAINT_NO";
			else if (complainType.equals("I"))
				param = "INQUIRY_NO";
			else if (complainType.equals("F"))
				param = "FA_NO";

			ps = con.prepareStatement(query);
			ps.setString(1, param);
			rs = ps.executeQuery();

			while (rs.next()) {
				format = rs.getString("string_value");
				number = rs.getLong("type");
			}

			// NO FORMAT -> CN:190100001 (CN-year-month-complaint Number)
			// complaint number should be start with 00001 in every new month

			DateFormat dfYear = new SimpleDateFormat("yy");
			String currntYear = dfYear.format(Calendar.getInstance().getTime());

			DateFormat dfMonth = new SimpleDateFormat("MM");
			String currntYearMonth = dfMonth.format(Calendar.getInstance().getTime());

			// check if it is the first complaint for the month
			query = "SELECT COUNT(*) FROM nt_m_complain_request WHERE complain_no LIKE ? ;";
			ps_count = con.prepareStatement(query);
			ps_count.setString(1, format + currntYear + currntYearMonth + "%");
			rs_count = ps_count.executeQuery();
			int recCount = 0;
			while (rs_count.next()) {
				recCount = rs_count.getInt(1);
			}

			// Generate number
			if (format != null) {
				if (recCount > 0)
					number += 1;
				else
					number = 00001;

				generatedCode = format + currntYear + currntYearMonth + String.format("%05d", number);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps_count);
			ConnectionManager.close(rs_count);
			ConnectionManager.close(con);
		}
		return generatedCode;
	}

	@Override
	public boolean updateParamSequence(String paramName) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		String format = null;
		long cnt = 1;
		boolean success = false;


		
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT string_value, type FROM nt_r_parameters WHERE param_name = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, paramName);
			rs = ps.executeQuery();

			while (rs.next()) {
				format = rs.getString("string_value");
				cnt = rs.getLong("type");
			}

			if (format != null) {
				cnt += 1;

				query = "UPDATE nt_r_parameters SET type = ? WHERE param_name = ?; ";
				ps2 = con.prepareStatement(query);
				ps2.setString(1, String.valueOf(cnt));
				ps2.setString(2, paramName);
				ps2.executeUpdate();

				con.commit();
				
				
			}
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public List<DropDownDTO> getPriorityOrderList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_r_priority_order WHERE active = 'A' ORDER BY code;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getSeverityList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_r_severity WHERE active = 'A';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getComplaintMediaList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_r_complaint_media WHERE active = 'A';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));
				drpdwnDTOList.add(dto);
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
	public List<CommittedOffencesDTO> getCommittedOffenceList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommittedOffencesDTO> drpdwnDTOList = new ArrayList<CommittedOffencesDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_offence_management WHERE status = 'A' ORDER BY ofm_seq;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommittedOffencesDTO dto = new CommittedOffencesDTO();
				dto.setCode(rs.getString("ofm_offence_code"));
				dto.setDescription(rs.getString("ofm_offence_desc"));
				drpdwnDTOList.add(dto);
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
	public long insertComplaintRequest(String permitAuth, String permitNo, String vehicleNo, String complainType,
			String complainNo, String priority, String severity, String route, String origin, String dest, String depot,
			String province, String eplace, String edatetime, String complaintMedia, String lang, String name,
			String name_si, String name_ta, String address1, String address1_si, String address1_ta, String address2,
			String address2_si, String address2_ta, String city, String city_si, String city_ta, String contact1,
			String contact2, boolean participation, boolean proof, String other, String detail, String user,
			String serviceTypeDes) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		Timestamp eventTimestamp = null;
		long seq;

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parsedDate = df.parse(edatetime);
			eventTimestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			con = ConnectionManager.getConnection();
			seq = Utils.getNextValBySeqName(con, "seq_nt_m_complain_request");

			String sql = "INSERT INTO public.nt_m_complain_request "
					+ "(seq, permit_authority, permit_no, vehicle_no, type_of_contact, complain_no, priority_order, severity_no, "
					+ "route_no, origin, destination, depot, province, event_place, event_datetime, complainer_name, complainer_name_si, complainer_name_ta, "
					+ "address1, address1_si, address1_ta, address2, address2_si, address2_ta, city, city_si, city_ta, contact_no1, contact_no2, "
					+ "is_participation_on_inquiry, is_witten_proof_available, other_commited_offences, details_of_complain, created_by, created_date, complaint_media, process_status,service_type) "
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seq);
			stmt.setString(2, permitAuth);
			stmt.setString(3, permitNo);
			stmt.setString(4, vehicleNo);
			stmt.setString(5, complainType);
			stmt.setString(6, complainNo);
			stmt.setString(7, priority);
			stmt.setString(8, severity);
			stmt.setString(9, route);
			stmt.setString(10, origin);
			stmt.setString(11, dest);
			stmt.setString(12, depot);
			stmt.setString(13, province);
			stmt.setString(14, eplace);
			stmt.setTimestamp(15, eventTimestamp);

			stmt.setString(16, name);
			stmt.setString(17, name_si);
			stmt.setString(18, name_ta);
			stmt.setString(19, address1);
			stmt.setString(20, address1_si);
			stmt.setString(21, address1_ta);
			stmt.setString(22, address2);
			stmt.setString(23, address2_si);
			stmt.setString(24, address2_ta);
			stmt.setString(25, city);
			stmt.setString(26, city_si);
			stmt.setString(27, city_ta);

			stmt.setString(28, contact1);
			stmt.setString(29, contact2);
			stmt.setString(30, participation ? "Y" : "N");
			stmt.setString(31, proof ? "Y" : "N");
			stmt.setString(32, other);
			stmt.setString(33, detail);
			stmt.setString(34, user);
			stmt.setTimestamp(35, timestamp);
			stmt.setString(36, complaintMedia);
			stmt.setString(37, "P");
			stmt.setString(38, serviceTypeDes);
			stmt.executeUpdate();

			con.commit();
		
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return seq;
	}

	@Override
	public void insertCommitedOffences(long complaintSeq, List<CommittedOffencesDTO> committedOffencesList,
			String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_t_grievance_commited_offence "
					+ "(seq, complain_seq, is_applicable, offence_type_code, remarks, created_by, created_date) "
					+ "VALUES(?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			for (CommittedOffencesDTO offence : committedOffencesList) {
				long seq = Utils.getNextValBySeqName(con, "seq_nt_t_grievance_commited_offence");

				stmt.setLong(1, seq);
				stmt.setLong(2, complaintSeq);
				stmt.setString(3, offence.isApplicable() ? "Y" : "N");
				stmt.setString(4, offence.getCode());
				stmt.setString(5, offence.isApplicable() ? offence.getRemark() : "");
				stmt.setString(6, user);
				stmt.setTimestamp(7, timestamp);
				stmt.executeUpdate();
			}

			con.commit();
			

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	@Override
	public void updateCommitedOffences(ComplaintRequestDTO selectedComplaintDTO, long complaintSeq, List<CommittedOffencesDTO> committedOffencesList,
			String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_grievance_commited_offence "
					+ "SET is_applicable=?, remarks=?, modified_by=?, modified_date=? "
					+ "WHERE complain_seq=? AND offence_type_code=?; ";

			for (CommittedOffencesDTO offence : committedOffencesList) {
				stmt = con.prepareStatement(sql);

				stmt.setString(1, offence.isApplicable() ? "Y" : "N");
				stmt.setString(2, offence.isApplicable() ? offence.getRemark() : "");
				stmt.setString(3, user);
				stmt.setTimestamp(4, timestamp);
				stmt.setLong(5, complaintSeq);
				stmt.setString(6, offence.getCode());

				stmt.executeUpdate();
			}

			con.commit();
			

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	@Override
	public List<ComplaintRequestDTO> getComplaintDetails(String complaintNo, String vehicleNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> complaintDTOList = new ArrayList<ComplaintRequestDTO>();

		try {
			con = ConnectionManager.getConnection();

			if (complaintNo != null && !complaintNo.trim().isEmpty()) {
				String query = "SELECT * FROM nt_m_complain_request WHERE complain_no = ?;";

				ps = con.prepareStatement(query);
				ps.setString(1, complaintNo);
				rs = ps.executeQuery();

			} else if (vehicleNo != null && !vehicleNo.trim().isEmpty()) {
				String query = "SELECT * FROM nt_m_complain_request WHERE vehicle_no = ?;";

				ps = con.prepareStatement(query);
				ps.setString(1, vehicleNo);
				rs = ps.executeQuery();

			} else if (permitNo != null && !permitNo.trim().isEmpty()) {
				String query = "SELECT * FROM nt_m_complain_request WHERE permit_no = ?;";

				ps = con.prepareStatement(query);
				ps.setString(1, permitNo);
				rs = ps.executeQuery();
			}

			while (rs.next()) {
				ComplaintRequestDTO dto = new ComplaintRequestDTO();
				dto.setComplainSeq(rs.getLong("seq"));
				dto.setPermitAuthority(rs.getString("permit_authority"));
				dto.setPermitNo(rs.getString("permit_no"));
				dto.setVehicleNo(rs.getString("vehicle_no"));
				dto.setComplainTypeCode(rs.getString("type_of_contact"));
				dto.setComplainMedia(rs.getString("complaint_media"));
				dto.setComplaintNo(rs.getString("complain_no"));
				dto.setPriorityOrder(rs.getString("priority_order"));
				dto.setSeverityNo(rs.getString("severity_no"));
				dto.setRouteNo(rs.getString("route_no"));
				dto.setOrigin(rs.getString("origin"));
				dto.setDestination(rs.getString("destination"));
				dto.setDepot(rs.getString("depot"));
				dto.setProvince(rs.getString("province"));
				dto.setEventPlace(rs.getString("event_place"));
				dto.setEventDateTime(rs.getString("event_datetime"));
				dto.setComplainerName(rs.getString("complainer_name"));
				dto.setComplainerName_si(rs.getString("complainer_name_si"));
				dto.setComplainerName_ta(rs.getString("complainer_name_ta"));
				dto.setAddress1(rs.getString("address1"));
				dto.setAddress1_si(rs.getString("address1_si"));
				dto.setAddress1_ta(rs.getString("address1_ta"));
				dto.setAddress2(rs.getString("address2"));
				dto.setAddress2_si(rs.getString("address2_si"));
				dto.setAddress2_ta(rs.getString("address2_ta"));
				dto.setCity(rs.getString("city"));
				dto.setCity_si(rs.getString("city_si"));
				dto.setCity_ta(rs.getString("city_ta"));
				dto.setContact1(rs.getString("contact_no1"));
				dto.setContact2(rs.getString("contact_no2"));
				String participation = rs.getString("is_participation_on_inquiry");
				if (participation != null && participation.equalsIgnoreCase("Y"))
					dto.setComplainerParticipation(true);

				String proof = rs.getString("is_witten_proof_available");
				if (proof != null && proof.equalsIgnoreCase("Y"))
					dto.setHasWrittenProof(true);

				dto.setOtherOffence(rs.getString("other_commited_offences"));
				dto.setDetailOfComplain(rs.getString("details_of_complain"));
				dto.setCommittedOffences(getCommittedOffencesByComplaint(dto.getComplainSeq(), false));
				dto.setInvestigationDate(rs.getDate("investigation_date"));
				dto.setProcessStatus(rs.getString("process_status"));

				if (rs.getString("process_status") != null && !rs.getString("process_status").trim().isEmpty()) {

					if (rs.getString("process_status").equalsIgnoreCase("O")) {

						dto.setProcess_status_des("Ongoing");
					} else if (rs.getString("process_status").equalsIgnoreCase("C")) {

						dto.setProcess_status_des("Closed");
					} else if (rs.getString("process_status").equalsIgnoreCase("P")) {

						dto.setProcess_status_des("Pending");
					}
				}

				dto.setServiceTypeDescription(rs.getString("service_type"));

				dto.setRecommendRemark(rs.getString("sps_recommend_remark"));
				dto.setRecommended(rs.getString("sps_is_recommend"));

				dto.setApprovedRemark(rs.getString("sps_approved_remark"));
				dto.setApproved(rs.getString("sps_is_approved"));

				complaintDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return complaintDTOList;
	}

	@Override
	public List<ComplaintRequestDTO> getComplaintDetailsByStatusAndGrantPermission(String complaintNo, String vehicleNo,
			String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> complaintDTOList = new ArrayList<ComplaintRequestDTO>();

		try {
			con = ConnectionManager.getConnection();

			if (complaintNo != null && !complaintNo.trim().isEmpty()) {
				String query = "SELECT permit_no,vehicle_no,complain_no,process_status,sps_recommend_remark,sps_is_recommend,sps_approved_remark,sps_is_approved FROM nt_m_complain_request WHERE process_status IN ('O','P') AND grant_permission_status IS NULL AND complain_no = ?;";

				ps = con.prepareStatement(query);
				ps.setString(1, complaintNo);
				rs = ps.executeQuery();

			} else if (vehicleNo != null && !vehicleNo.trim().isEmpty()) {
				String query = "SELECT permit_no,vehicle_no,complain_no,process_status,sps_recommend_remark,sps_is_recommend,sps_approved_remark,sps_is_approved FROM nt_m_complain_request WHERE process_status IN ('O','P') AND grant_permission_status IS NULL AND vehicle_no = ?;";

				ps = con.prepareStatement(query);
				ps.setString(1, vehicleNo);
				rs = ps.executeQuery();

			} else if (permitNo != null && !permitNo.trim().isEmpty()) {
				String query = "SELECT permit_no,vehicle_no,complain_no,process_status,sps_recommend_remark,sps_is_recommend,sps_approved_remark,sps_is_approved FROM nt_m_complain_request WHERE process_status IN ('O','P') AND grant_permission_status IS NULL AND permit_no = ?;";

				ps = con.prepareStatement(query);
				ps.setString(1, permitNo);
				rs = ps.executeQuery();
			} else {
				String query = "SELECT permit_no,vehicle_no,complain_no,process_status,sps_recommend_remark,sps_is_recommend,sps_approved_remark,sps_is_approved FROM nt_m_complain_request WHERE process_status IN ('O','P') AND grant_permission_status IS NULL";

				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
			}

			while (rs.next()) {
				ComplaintRequestDTO dto = new ComplaintRequestDTO();
				dto.setPermitNo(rs.getString("permit_no"));
				dto.setVehicleNo(rs.getString("vehicle_no"));
				dto.setComplaintNo(rs.getString("complain_no"));
				dto.setProcessStatus(rs.getString("process_status"));

				if (rs.getString("process_status") != null && !rs.getString("process_status").trim().isEmpty()) {

					if (rs.getString("process_status").equalsIgnoreCase("O")) {

						dto.setProcess_status_des("Ongoing");
					} else if (rs.getString("process_status").equalsIgnoreCase("C")) {

						dto.setProcess_status_des("Closed");
					} else if (rs.getString("process_status").equalsIgnoreCase("P")) {

						dto.setProcess_status_des("Pending");
					}
				}

				dto.setRecommendRemark(rs.getString("sps_recommend_remark"));
				dto.setRecommended(rs.getString("sps_is_recommend"));

				dto.setApprovedRemark(rs.getString("sps_approved_remark"));
				dto.setApproved(rs.getString("sps_is_approved"));

				complaintDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return complaintDTOList;
	}

	@Override
	public List<CommittedOffencesDTO> getCommittedOffencesByComplaint(Long complainSeq, boolean applicableOnly) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommittedOffencesDTO> commitedDTOList = new ArrayList<CommittedOffencesDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_grievance_commited_offence a, nt_m_offence_management b"
					+ " LEFT JOIN nt_t_offence_charge_sheet c ON c.chr_complaint_no IN (SELECT complain_no FROM nt_m_complain_request WHERE seq = ?) "
					+ " AND c.chr_offence_code = b.ofm_offence_code "
					+ " WHERE a.offence_type_code = b.ofm_offence_code " + " AND a.complain_seq = ? ";

			if (applicableOnly)
				query += " AND a.is_applicable = 'Y' ";

			query += " ORDER BY b.ofm_seq;";

			ps = con.prepareStatement(query);
			ps.setLong(1, complainSeq);
			ps.setLong(2, complainSeq);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommittedOffencesDTO dto = new CommittedOffencesDTO();
				dto.setSeq(rs.getLong("seq"));
				dto.setCode(rs.getString("ofm_offence_code"));
				dto.setDescription(rs.getString("ofm_offence_desc"));
				dto.setRemark(rs.getString("remarks"));
				dto.setApplicable(
						rs.getString("is_applicable") == null || rs.getString("is_applicable").equals("N") ? false
								: true);
				dto.setChr_seq(rs.getLong("chr_seq"));
				dto.setCharge(rs.getBigDecimal("chr_charge_amount"));
				commitedDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return commitedDTOList;
	}

	public List<CommittedOffencesDTO> getCommittedOffencesById(Long complainSeq, String driverId, String conductorId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommittedOffencesDTO> commitedDTOList = new ArrayList<CommittedOffencesDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_driver_conductor_points_det a, nt_t_offence_charge_sheet b, nt_t_grievance_commited_offence c "
					+ "WHERE a.p_complain_no = b.chr_complaint_no " + "AND a.p_offence_code = b.chr_offence_code "
					+ "AND a.p_complain_no IN (SELECT complain_no FROM nt_m_complain_request WHERE seq = c.complain_seq) "
					+ "AND a.p_offence_code = c.offence_type_code "
					+ "AND a.p_complain_no IN (SELECT complain_no FROM nt_m_complain_request WHERE seq = ?) ";

			if (driverId != null)
				query += " AND a.p_driver_id = ?;";
			else
				query += " AND a.p_conductor_id = ?;";

			ps = con.prepareStatement(query);
			ps.setLong(1, complainSeq);
			if (driverId != null)
				ps.setString(2, driverId);
			else
				ps.setString(2, conductorId);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommittedOffencesDTO dto = new CommittedOffencesDTO();
				dto.setCode(rs.getString("p_offence_code"));
				dto.setDescription(rs.getString("chr_offence_desc"));
				dto.setRemark(rs.getString("remarks"));
				dto.setChr_seq(rs.getLong("chr_seq"));
				dto.setCharge(rs.getBigDecimal("chr_charge_amount"));
				commitedDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return commitedDTOList;
	}

	@Override
	public boolean updateComplaintRequest(ComplaintRequestDTO complaintDetailDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		Timestamp eventTimestamp = null;

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parsedDate = df.parse(complaintDetailDTO.getEventDateTime());
			eventTimestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			con = ConnectionManager.getConnection();

			/*
			 * Added By Dinushi Ranasinghe Added Date 01/10/2020
			 */
			String sql1 = "INSERT into public.nt_h_complain_request  "
					+ " (SELECT * FROM public.nt_m_complain_request WHERE complain_no = ?) ";

			stmt1 = con.prepareStatement(sql1);
			stmt1.setString(1, complaintDetailDTO.getComplaintNo());
			stmt1.executeUpdate();

			/**/

			String sql = "UPDATE public.nt_m_complain_request "
					+ "SET permit_authority = ?, permit_no = ?, vehicle_no = ?, type_of_contact = ?, priority_order = ?, severity_no = ?, "
					+ "route_no = ?, origin = ?, destination = ?, depot = ?, province = ?, event_place = ?, event_datetime = ?, complaint_media = ?, "
					+ "complainer_name = ?, complainer_name_si = ?, complainer_name_ta = ?, address1 = ?, address1_si = ?, address1_ta = ?, address2 = ?, address2_si = ?, address2_ta = ?, city = ?, city_si = ?, city_ta = ?, "
					+ "contact_no1 = ?, contact_no2 = ?, is_participation_on_inquiry = ?, is_witten_proof_available = ?, other_commited_offences = ?, details_of_complain = ?,  "
					+ "modified_by = ?, modified_date = ? " + "WHERE seq = ?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, complaintDetailDTO.getPermitAuthority());
			stmt.setString(2, complaintDetailDTO.getPermitNo().toUpperCase());
			stmt.setString(3, complaintDetailDTO.getVehicleNo());
			stmt.setString(4, complaintDetailDTO.getComplainTypeCode());
			stmt.setString(5, complaintDetailDTO.getPriorityOrder());
			stmt.setString(6, complaintDetailDTO.getSeverityNo());
			stmt.setString(7, complaintDetailDTO.getRouteNo());
			stmt.setString(8, complaintDetailDTO.getOrigin());
			stmt.setString(9, complaintDetailDTO.getDestination());
			stmt.setString(10, complaintDetailDTO.getDepot());
			stmt.setString(11, complaintDetailDTO.getProvince());
			stmt.setString(12, complaintDetailDTO.getEventPlace());
			stmt.setTimestamp(13, eventTimestamp);
			stmt.setString(14, complaintDetailDTO.getComplainMedia());
			stmt.setString(15, complaintDetailDTO.getComplainerName());
			stmt.setString(16, complaintDetailDTO.getComplainerName_si());
			stmt.setString(17, complaintDetailDTO.getComplainerName_ta());
			stmt.setString(18, complaintDetailDTO.getAddress1());
			stmt.setString(19, complaintDetailDTO.getAddress1_si());
			stmt.setString(20, complaintDetailDTO.getAddress1_ta());
			stmt.setString(21, complaintDetailDTO.getAddress2());
			stmt.setString(22, complaintDetailDTO.getAddress2_si());
			stmt.setString(23, complaintDetailDTO.getAddress2_ta());
			stmt.setString(24, complaintDetailDTO.getCity());
			stmt.setString(25, complaintDetailDTO.getCity_si());
			stmt.setString(26, complaintDetailDTO.getCity_ta());
			stmt.setString(27, complaintDetailDTO.getContact1());
			stmt.setString(28, complaintDetailDTO.getContact2());
			stmt.setString(29, complaintDetailDTO.isComplainerParticipation() ? "Y" : "N");
			stmt.setString(30, complaintDetailDTO.isHasWrittenProof() ? "Y" : "N");
			stmt.setString(31, complaintDetailDTO.getOtherOffence());
			stmt.setString(32, complaintDetailDTO.getDetailOfComplain());
			stmt.setString(33, user);
			stmt.setTimestamp(34, timestamp);
			stmt.setLong(35, complaintDetailDTO.getComplainSeq());
			stmt.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return true;
	}

	@Override
	public List<String> getAllComplainNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> drpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_complain_request ORDER BY complain_no desc;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				drpdwnDTOList.add(rs.getString("complain_no"));
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
	public List<ComplaintRequestDTO> getComplaintListByInvestigationDate(Date investigationFrom, Date investigationTo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> complaintList = new ArrayList<ComplaintRequestDTO>();
		String query;
		String fromDate = null;
		String toDate = null;

		try {
			con = ConnectionManager.getConnection();

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			if (investigationFrom != null)
				fromDate = df.format(investigationFrom);
			if (investigationTo != null)
				toDate = df.format(investigationTo);

			if (fromDate == null) {
				toDate = df.format(investigationTo);
				query = "SELECT * FROM public.nt_m_complain_request WHERE investigation_date <= DATE(?);";
				ps = con.prepareStatement(query);
				ps.setString(1, toDate);
			} else if (toDate == null) {
				query = "SELECT * FROM public.nt_m_complain_request WHERE investigation_date >= DATE(?);";
				ps = con.prepareStatement(query);
				ps.setString(1, fromDate);
			} else {
				query = "SELECT * FROM nt_m_complain_request WHERE investigation_date >= DATE(?) AND investigation_date <= DATE(?);";
				ps = con.prepareStatement(query);
				ps.setString(1, fromDate);
				ps.setString(2, toDate);
			}
			rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintRequestDTO dto = new ComplaintRequestDTO();
				dto.setComplainSeq(rs.getLong("seq"));
				dto.setPermitAuthority(rs.getString("permit_authority"));
				dto.setPermitNo(rs.getString("permit_no"));
				dto.setVehicleNo(rs.getString("vehicle_no"));
				dto.setComplainTypeCode(rs.getString("type_of_contact"));
				dto.setComplainMedia(rs.getString("complaint_media"));
				dto.setComplaintNo(rs.getString("complain_no"));
				dto.setPriorityOrder(rs.getString("priority_order"));
				dto.setSeverityNo(rs.getString("severity_no"));
				dto.setRouteNo(rs.getString("route_no"));
				dto.setOrigin(rs.getString("origin"));
				dto.setDestination(rs.getString("destination"));
				dto.setDepot(rs.getString("depot"));
				dto.setProvince(rs.getString("province"));
				dto.setEventPlace(rs.getString("event_place"));
				dto.setEventDateTime(rs.getString("event_datetime"));
				dto.setComplainerName(rs.getString("complainer_name"));
				dto.setComplainerName_si(rs.getString("complainer_name_si"));
				dto.setComplainerName_ta(rs.getString("complainer_name_ta"));
				dto.setAddress1(rs.getString("address1"));
				dto.setAddress1_si(rs.getString("address1_si"));
				dto.setAddress1_ta(rs.getString("address1_ta"));
				dto.setAddress2(rs.getString("address2"));
				dto.setAddress2_si(rs.getString("address2_si"));
				dto.setAddress2_ta(rs.getString("address2_ta"));
				dto.setCity(rs.getString("city"));
				dto.setCity_si(rs.getString("city_si"));
				dto.setCity_ta(rs.getString("city_ta"));
				dto.setContact1(rs.getString("contact_no1"));
				dto.setContact2(rs.getString("contact_no2"));
				String participation = rs.getString("is_participation_on_inquiry");
				if (participation != null && participation.equalsIgnoreCase("Y"))
					dto.setComplainerParticipation(true);

				String proof = rs.getString("is_participation_on_inquiry");
				if (proof != null && proof.equalsIgnoreCase("Y"))
					dto.setHasWrittenProof(true);

				dto.setOtherOffence(rs.getString("other_commited_offences"));
				dto.setDetailOfComplain(rs.getString("details_of_complain"));
				dto.setCommittedOffences(getCommittedOffencesByComplaint(dto.getComplainSeq(), false));
				dto.setInvestigationDate(rs.getDate("investigation_date"));
				dto.setProcessStatus(rs.getString("process_status"));

				if (rs.getString("process_status") != null && !rs.getString("process_status").trim().isEmpty()) {

					if (rs.getString("process_status").equalsIgnoreCase("O")) {

						dto.setProcess_status_des("Ongoing");
					} else if (rs.getString("process_status").equalsIgnoreCase("C")) {

						dto.setProcess_status_des("Closed");
					} else if (rs.getString("process_status").equalsIgnoreCase("P")) {

						dto.setProcess_status_des("Pending");
					}
				}

				dto.setServiceTypeDescription(rs.getString("service_type"));

				dto.setRecommendRemark(rs.getString("sps_recommend_remark"));
				dto.setRecommended(rs.getString("sps_is_recommend"));

				dto.setApprovedRemark(rs.getString("sps_approved_remark"));
				dto.setApproved(rs.getString("sps_is_approved"));

				complaintList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return complaintList;
	}

	@Override
	public List<DropDownDTO> getDrivers() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration a"
					+ " WHERE a.dcr_training_type in ('FD','ND','DD','RD','RRD','RRRD') AND a.dcr_status in ('TA','A') ORDER BY dcr_driver_conductor_id;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_driver_conductor_id"));
				dto.setDescription(rs.getString("dcr_driver_conductor_id"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getConductors() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration a"
					+ " WHERE a.dcr_training_type in ('FC','NC','DC','RC','RRC','RRRC') AND a.dcr_status in ('TA','A') ORDER BY dcr_driver_conductor_id;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_driver_conductor_id"));
				dto.setDescription(rs.getString("dcr_driver_conductor_id"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getDriversByTrainingType(String type) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration a"
					+ " WHERE a.dcr_training_type in ('FD','ND','DD','RD','RRD','RRRD') AND a.dcr_status in ('TA','A') AND dcr_training_type=? ORDER BY dcr_driver_conductor_id;";
			ps = con.prepareStatement(query);
			ps.setString(1, type);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_driver_conductor_id"));
				dto.setDescription(rs.getString("dcr_driver_conductor_id"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getConductorsByTrainingType(String type) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_driver_conductor_id FROM nt_t_driver_conductor_registration a"
					+ " WHERE 	a.dcr_training_type in ('FC','NC','DC','RC','RRC','RRRC') AND a.dcr_status in ('TA','A') AND dcr_training_type=? ORDER BY dcr_driver_conductor_id;";
			ps = con.prepareStatement(query);
			ps.setString(1, type);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_driver_conductor_id"));
				dto.setDescription(rs.getString("dcr_driver_conductor_id"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getApplicationNos() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT dcr_app_no FROM nt_t_driver_conductor_registration a"
					+ " WHERE a.dcr_status in ('TA','A') ORDER BY dcr_app_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_app_no"));
				dto.setDescription(rs.getString("dcr_app_no"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getApplicationNoByTrainingType(String trainingType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();
		String query = null;
		try {
			con = ConnectionManager.getConnection();
			if (trainingType.equals("DD") || trainingType.equals("DC")) {
				query = "SELECT dcr_app_no FROM nt_t_driver_conductor_registration WHERE dcr_training_type = ? AND "
						+ "and dcr_status in ('TA','A') and dcr_status_type in('R','CP') ORDER BY dcr_app_no;";
			} else {
				query = "SELECT dcr_app_no FROM nt_t_driver_conductor_registration WHERE dcr_training_type = ? AND "
						+ "dcr_status in ('TA','A') ORDER BY dcr_app_no;"; //--Requested by Pramitha on 29/03/2022
				//+ "dcr_status = 'A' ORDER BY dcr_app_no;";  --Requested by Pramitha on 29/03/2022
			}
			ps = con.prepareStatement(query);
			ps.setString(1, trainingType);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_app_no"));
				dto.setDescription(rs.getString("dcr_app_no"));
				drpdwnDTOList.add(dto);
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
	public DriverConductorRegistrationDTO getDriverConductorData(DriverConductorRegistrationDTO dcData) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_driver_conductor_registration WHERE dcr_driver_conductor_id = ? ORDER BY dcr_driver_conductor_id; ";

			ps = con.prepareStatement(query);
			ps.setString(1, dcData.getDriverConductorId());
			rs = ps.executeQuery();

			dcData.setDriverConductorId(null);
			dcData.setFullNameEng(null);
			dcData.setAdd1Eng(null);
			dcData.setAdd2Eng(null);
			dcData.setCityEng(null);

			while (rs.next()) {
				dcData.setDriverConductorId(rs.getString("dcr_driver_conductor_id"));
				dcData.setNic(rs.getString("dcr_nic"));
				dcData.setFullNameEng(rs.getString("dcr_full_name_eng"));
				dcData.setAdd1Eng(rs.getString("dcr_add_1_eng"));
				dcData.setAdd2Eng(rs.getString("dcr_add_2_eng"));
				dcData.setCityEng(rs.getString("dcr_city_eng"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dcData;
	}

	@Override
	public List<DropDownDTO> getActionTypes() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_r_action_types;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));
				drpdwnDTOList.add(dto);
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
	public List<ComplaintRequestDTO> getComplaintHistoryByOwner(String vehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> complaintDTOList = new ArrayList<ComplaintRequestDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, NULL, NULL  "
					+ " FROM public.nt_m_complain_request c "
					+ " WHERE c.vehicle_no = ? AND c.complain_no NOT IN (SELECT d.complain_ref_no FROM nt_t_action_owner_det d) "
					+ " UNION "
					+ " SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, d.action_type_code, d.action_desc  "
					+ " FROM nt_m_complain_request c, nt_t_action_owner_det d " + " WHERE c.vehicle_no = ? "
					+ " AND c.complain_no = d.complain_ref_no " + " ORDER BY complain_no";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNo);
			ps.setString(2, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintRequestDTO dto = new ComplaintRequestDTO();
				dto.setComplainSeq(rs.getLong("seq"));
				dto.setComplaintNo(rs.getString("complain_no"));
				dto.setSeverityNo(rs.getString("severity_no"));
				dto.setEventDateTime(rs.getString("event_datetime"));
				dto.setOtherOffence(rs.getString("other_commited_offences"));
				dto.setDetailOfComplain(rs.getString("details_of_complain"));

				dto.setActionCode(rs.getString(7));
				dto.setActionDesc(rs.getString(8));
				complaintDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return complaintDTOList;
	}

	@Override
	public List<ComplaintRequestDTO> getComplaintHistoryByDriver(String driverId, String driverNic) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> complaintDTOList = new ArrayList<ComplaintRequestDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			if (driverId != null) {
				query = "SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, NULL action_type_code, NULL action_desc "
						+ "FROM nt_m_complain_request c, nt_t_complain_driver_det com "
						+ "WHERE c.complain_no = com.complain_ref_no " + "AND com.driver_id = ? "
						+ "AND c.complain_no NOT IN (SELECT complain_ref_no FROM nt_t_action_driver_det) " + "UNION "
						+ "SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, act.action_type_code, act.action_desc "
						+ "FROM nt_m_complain_request c, nt_t_complain_driver_det com, nt_t_action_driver_det act "
						+ "WHERE c.complain_no = com.complain_ref_no " + "AND c.complain_no = act.complain_ref_no "
						+ "AND com.driver_id = ? " + "ORDER BY complain_no;";
			} else {
				query = "SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, NULL action_type_code, NULL action_desc "
						+ "FROM nt_m_complain_request c, nt_t_complain_driver_det com "
						+ "WHERE c.complain_no = com.complain_ref_no " + "AND com.driver_nic_no = ? "
						+ "AND c.complain_no NOT IN (SELECT complain_ref_no FROM nt_t_action_driver_det) " + "UNION "
						+ "SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, act.action_type_code, act.action_desc "
						+ "FROM nt_m_complain_request c, nt_t_complain_driver_det com, nt_t_action_driver_det act "
						+ "WHERE c.complain_no = com.complain_ref_no " + "AND c.complain_no = act.complain_ref_no "
						+ "AND com.driver_nic_no = ? " + "ORDER BY complain_no;";
			}

			ps = con.prepareStatement(query);
			if (driverId != null) {
				ps.setString(1, driverId);
				ps.setString(2, driverId);
			} else {
				ps.setString(1, driverNic);
				ps.setString(2, driverNic);
			}
			rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintRequestDTO dto = new ComplaintRequestDTO();
				dto.setComplainSeq(rs.getLong("seq"));
				dto.setComplaintNo(rs.getString("complain_no"));
				dto.setSeverityNo(rs.getString("severity_no"));
				dto.setEventDateTime(rs.getString("event_datetime"));
				dto.setOtherOffence(rs.getString("other_commited_offences"));
				dto.setDetailOfComplain(rs.getString("details_of_complain"));

				dto.setActionCode(rs.getString("action_type_code"));
				dto.setActionDesc(rs.getString("action_desc"));
				complaintDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return complaintDTOList;
	}

	@Override
	public List<ComplaintRequestDTO> getComplaintHistoryByConductor(String conductorId, String conductorNic) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> complaintDTOList = new ArrayList<ComplaintRequestDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			if (conductorId != null) {
				query = "SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, NULL action_type_code, NULL action_desc "
						+ "FROM nt_m_complain_request c, nt_t_complain_conductor_det com "
						+ "WHERE c.complain_no = com.complain_ref_no " + "AND com.cond_id = ? "
						+ "AND c.complain_no NOT IN (SELECT complain_ref_no FROM nt_t_action_conductor_det) " + "UNION "
						+ "SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, act.action_type_code, act.action_desc "
						+ "FROM nt_m_complain_request c, nt_t_complain_conductor_det com, nt_t_action_conductor_det act "
						+ "WHERE c.complain_no = com.complain_ref_no " + "AND c.complain_no = act.complain_ref_no "
						+ "AND com.cond_id = ? " + "ORDER BY complain_no;";
			} else {
				query = "SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, NULL action_type_code, NULL action_desc "
						+ "FROM nt_m_complain_request c, nt_t_complain_conductor_det com "
						+ "WHERE c.complain_no = com.complain_ref_no " + "AND com.cond_nic_no= ? "
						+ "AND c.complain_no NOT IN (SELECT complain_ref_no FROM nt_t_action_conductor_det) " + "UNION "
						+ "SELECT c.seq, c.complain_no, c.severity_no, c.event_datetime, c.other_commited_offences, c.details_of_complain, act.action_type_code, act.action_desc "
						+ "FROM nt_m_complain_request c, nt_t_complain_conductor_det com, nt_t_action_conductor_det act "
						+ "WHERE c.complain_no = com.complain_ref_no " + "AND c.complain_no = act.complain_ref_no "
						+ "AND com.cond_nic_no = ? " + "ORDER BY complain_no;";
			}

			ps = con.prepareStatement(query);
			if (conductorId != null) {
				ps.setString(1, conductorId);
				ps.setString(2, conductorId);
			} else {
				ps.setString(1, conductorNic);
				ps.setString(2, conductorNic);
			}
			rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintRequestDTO dto = new ComplaintRequestDTO();
				dto.setComplainSeq(rs.getLong("seq"));
				dto.setComplaintNo(rs.getString("complain_no"));
				dto.setSeverityNo(rs.getString("severity_no"));
				dto.setEventDateTime(rs.getString("event_datetime"));
				dto.setOtherOffence(rs.getString("other_commited_offences"));
				dto.setDetailOfComplain(rs.getString("details_of_complain"));

				dto.setActionCode(rs.getString("action_type_code"));
				dto.setActionDesc(rs.getString("action_desc"));
				complaintDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return complaintDTOList;
	}

	@Override
	public ComplaintRequestDTO getActionData(String viewType, ComplaintRequestDTO selectedComplaintDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			if (viewType.equalsIgnoreCase("O"))
				query = "SELECT * FROM public.nt_t_action_owner_det WHERE complain_ref_no = ?";
			else if (viewType.equalsIgnoreCase("D"))
				query = "SELECT * FROM public.nt_t_action_driver_det WHERE complain_ref_no = ?";
			else if (viewType.equalsIgnoreCase("C"))
				query = "SELECT * FROM public.nt_t_action_conductor_det WHERE complain_ref_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedComplaintDTO.getComplaintNo());
			rs = ps.executeQuery();

			selectedComplaintDTO.setActionSeq(null);
			selectedComplaintDTO.setActionCode(null);
			selectedComplaintDTO.setActionDesc(null);
			selectedComplaintDTO.setDepartmentType(null);

			while (rs.next()) {
				selectedComplaintDTO.setActionSeq(rs.getLong("seq"));
				selectedComplaintDTO.setActionCode(rs.getString("action_type_code"));
				selectedComplaintDTO.setActionDesc(rs.getString("action_desc"));
				selectedComplaintDTO.setDepartmentType(rs.getString("department_type_code"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return selectedComplaintDTO;
	}

	@Override
	public boolean saveActionData(String viewType, ComplaintRequestDTO selectedComplaintDTO, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";
			long seq = 0;

			if (selectedComplaintDTO.getActionSeq() == null) {
				if (viewType.equalsIgnoreCase("O")) {
					query = "INSERT INTO public.nt_t_action_owner_det (seq, complain_ref_no, department_type_code, action_type_code, action_desc, created_by, created_date) "
							+ "VALUES(?,?,?,?,?,?,?);";
					seq = Utils.getNextValBySeqName(con, "seq_nt_t_action_owner_det");

				} else if (viewType.equalsIgnoreCase("D")) {
					query = "INSERT INTO public.nt_t_action_driver_det (seq, complain_ref_no, department_type_code, action_type_code, action_desc, created_by, created_date) "
							+ "VALUES(?,?,?,?,?,?,?);";
					seq = Utils.getNextValBySeqName(con, "seq_nt_t_action_driver_det");

				} else if (viewType.equalsIgnoreCase("C")) {
					query = "INSERT INTO public.nt_t_action_conductor_det (seq, complain_ref_no, department_type_code, action_type_code, action_desc, created_by, created_date) "
							+ "VALUES(?,?,?,?,?,?,?);";
					seq = Utils.getNextValBySeqName(con, "seq_nt_t_action_conductor_det");
				}
				ps = con.prepareStatement(query);
				ps.setLong(1, seq);
				ps.setString(2, selectedComplaintDTO.getComplaintNo());
				ps.setString(3, selectedComplaintDTO.getDepartmentType());
				ps.setString(4, selectedComplaintDTO.getActionCode());
				ps.setString(5, selectedComplaintDTO.getActionDesc());
				ps.setString(6, user);
				ps.setTimestamp(7, timestamp);
				ps.executeUpdate();
			

			} else {
				if (viewType.equalsIgnoreCase("O")) {
					query = "UPDATE public.nt_t_action_owner_det SET complain_ref_no=?, department_type_code=?, action_type_code=?, action_desc=?, modified_by=?, modified_date=?"
							+ " WHERE seq=?;";
				} else if (viewType.equalsIgnoreCase("D")) {
					query = "UPDATE public.nt_t_action_driver_det SET complain_ref_no=?, department_type_code=?, action_type_code=?, action_desc=?, modified_by=?, modified_date=?"
							+ " WHERE seq=?;";
				} else if (viewType.equalsIgnoreCase("C")) {
					query = "UPDATE public.nt_t_action_conductor_det SET complain_ref_no=?, department_type_code=?, action_type_code=?, action_desc=?, modified_by=?, modified_date=?"
							+ " WHERE seq=?;";
				}
				ps = con.prepareStatement(query);
				ps.setString(1, selectedComplaintDTO.getComplaintNo());
				ps.setString(2, selectedComplaintDTO.getDepartmentType());
				ps.setString(3, selectedComplaintDTO.getActionCode());
				ps.setString(4, selectedComplaintDTO.getActionDesc());
				ps.setString(5, user);
				ps.setTimestamp(6, timestamp);
				ps.setLong(7, selectedComplaintDTO.getActionSeq());
				ps.executeUpdate();
				
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
	public boolean saveUpdateInquiryProcess(ComplaintRequestDTO selectedComplaintDTO,
			DriverConductorRegistrationDTO driverData, DriverConductorRegistrationDTO conductorData, String user) {
		Connection con = null;
		PreparedStatement ps_r = null;
		PreparedStatement ps_r2 = null;
		PreparedStatement ps_r3 = null;
		PreparedStatement ps_o = null;
		PreparedStatement ps_d = null;
		PreparedStatement ps_c = null;
		ResultSet rs_r = null;
		ResultSet rs_r2 = null;
		ResultSet rs_r3 = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";
			long seq = 0;
			int count = 0;

			// update owner complaint log
			String sql_retrieve = "SELECT COUNT(*) FROM nt_t_complain_owner_det WHERE complain_ref_no = '"
					+ selectedComplaintDTO.getComplaintNo() + "';";
			ps_r = con.prepareStatement(sql_retrieve);
			rs_r = ps_r.executeQuery();

			while (rs_r.next()) {
				count = rs_r.getInt(1);
			}

			if (count == 0) {
				query = "INSERT INTO public.nt_t_complain_owner_det (owner_seq, complain_ref_no, permit_no, vehicleno, owner_name, created_by, created_date) "
						+ " VALUES(?,?,?,?,?,?,?);";
				seq = Utils.getNextValBySeqName(con, "seq_nt_t_complain_owner_det");

				ps_o = con.prepareStatement(query);
				ps_o.setLong(1, seq);
				ps_o.setString(2, selectedComplaintDTO.getComplaintNo());
				ps_o.setString(3, selectedComplaintDTO.getPermitNo());
				ps_o.setString(4, selectedComplaintDTO.getVehicleNo());
				ps_o.setString(5, selectedComplaintDTO.getOwnerName());
				ps_o.setString(6, user);
				ps_o.setTimestamp(7, timestamp);
				ps_o.executeUpdate();
				
				
			} else {
				query = "UPDATE public.nt_t_complain_owner_det SET owner_name=?, modified_by=?, modified_date=? "
						+ "WHERE complain_ref_no=?;";

				ps_o = con.prepareStatement(query);
				ps_o.setString(1, selectedComplaintDTO.getOwnerName());
				ps_o.setString(2, user);
				ps_o.setTimestamp(3, timestamp);
				ps_o.setString(4, selectedComplaintDTO.getComplaintNo());
				ps_o.executeUpdate();
			}

			// update driver complaint log
			sql_retrieve = "SELECT COUNT(*) FROM nt_t_complain_driver_det WHERE complain_ref_no = '"
					+ selectedComplaintDTO.getComplaintNo() + "';";
			ps_r2 = con.prepareStatement(sql_retrieve);
			rs_r2 = ps_r2.executeQuery();

			while (rs_r2.next()) {
				count = rs_r2.getInt(1);
			}

			if (count == 0) {
				query = "INSERT INTO public.nt_t_complain_driver_det (driver_seq, complain_ref_no, driver_id, driver_nic_no, driver_name, driver_address_1, driver_address_2, driver_address_3, created_by, created_date)"
						+ " VALUES(?,?,?,?,?,?,?,?,?,?);";
				seq = Utils.getNextValBySeqName(con, "seq_nt_t_complain_driver_det");

				ps_d = con.prepareStatement(query);
				ps_d.setLong(1, seq);
				ps_d.setString(2, selectedComplaintDTO.getComplaintNo());
				ps_d.setString(3, driverData.getDriverConductorId());
				ps_d.setString(4, driverData.getNic());
				ps_d.setString(5, driverData.getFullNameEng());
				ps_d.setString(6, driverData.getAdd1Eng());
				ps_d.setString(7, driverData.getAdd2Eng());
				ps_d.setString(8, driverData.getCityEng());
				ps_d.setString(9, user);
				ps_d.setTimestamp(10, timestamp);
				ps_d.executeUpdate();
			} else {
				query = "UPDATE public.nt_t_complain_driver_det "
						+ " SET driver_id=?, driver_nic_no=?, driver_name=?, driver_address_1=?, driver_address_2=?, driver_address_3=?, modified_by=?, modified_date=?"
						+ " WHERE complain_ref_no=?";

				ps_d = con.prepareStatement(query);
				ps_d.setString(1, driverData.getDriverConductorId());
				ps_d.setString(2, driverData.getNic());
				ps_d.setString(3, driverData.getFullNameEng());
				ps_d.setString(4, driverData.getAdd1Eng());
				ps_d.setString(5, driverData.getAdd2Eng());
				ps_d.setString(6, driverData.getCityEng());
				ps_d.setString(7, user);
				ps_d.setTimestamp(8, timestamp);
				ps_d.setString(9, selectedComplaintDTO.getComplaintNo());
				ps_d.executeUpdate();
			}

			// update conductor complaint log
			sql_retrieve = "SELECT COUNT(*) FROM nt_t_complain_conductor_det WHERE complain_ref_no = '"
					+ selectedComplaintDTO.getComplaintNo() + "';";
			ps_r3 = con.prepareStatement(sql_retrieve);
			rs_r3 = ps_r3.executeQuery();

			while (rs_r3.next()) {
				count = rs_r3.getInt(1);
			}

			if (count == 0) {
				query = "INSERT INTO public.nt_t_complain_conductor_det (cond_seq, complain_ref_no, cond_id, cond_nic_no, cond_name, cond_address_1,cond_address_2,cond_address_3, created_by, created_date)"
						+ " VALUES(?,?,?,?,?,?,?,?,?,?);";
				seq = Utils.getNextValBySeqName(con, "seq_nt_t_complain_conductor_det");

				ps_c = con.prepareStatement(query);
				ps_c.setLong(1, seq);
				ps_c.setString(2, selectedComplaintDTO.getComplaintNo());
				ps_c.setString(3, conductorData.getDriverConductorId());
				ps_c.setString(4, conductorData.getNic());
				ps_c.setString(5, conductorData.getFullNameEng());
				ps_c.setString(6, conductorData.getAdd1Eng());
				ps_c.setString(7, conductorData.getAdd2Eng());
				ps_c.setString(8, conductorData.getCityEng());
				ps_c.setString(9, user);
				ps_c.setTimestamp(10, timestamp);
				ps_c.executeUpdate();
			} else {
				query = "UPDATE public.nt_t_complain_conductor_det "
						+ " SET cond_id=?, cond_nic_no=?, cond_name=?, cond_address_1=?, cond_address_2=?, cond_address_3=?, modified_by=?, modified_date=?"
						+ " WHERE complain_ref_no=?;";

				ps_c = con.prepareStatement(query);
				ps_c.setString(1, conductorData.getDriverConductorId());
				ps_c.setString(2, conductorData.getNic());
				ps_c.setString(3, conductorData.getFullNameEng());
				ps_c.setString(4, conductorData.getAdd1Eng());
				ps_c.setString(5, conductorData.getAdd2Eng());
				ps_c.setString(6, conductorData.getCityEng());
				ps_c.setString(7, user);
				ps_c.setTimestamp(8, timestamp);
				ps_c.setString(9, selectedComplaintDTO.getComplaintNo());
				ps_c.executeUpdate();
			}

			con.commit();
			
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps_o);
			ConnectionManager.close(ps_d);
			ConnectionManager.close(ps_c);
			ConnectionManager.close(ps_r);
			ConnectionManager.close(ps_r2);
			ConnectionManager.close(ps_r3);
			ConnectionManager.close(rs_r);
			ConnectionManager.close(rs_r2);
			ConnectionManager.close(rs_r3);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public DriverConductorRegistrationDTO getComplaintDriver(String complaintNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		DriverConductorRegistrationDTO dto = new DriverConductorRegistrationDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_complain_driver_det WHERE complain_ref_no=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setSeq(rs.getLong("driver_seq"));
				dto.setDriverConductorId(rs.getString("driver_id"));
				dto.setNic(rs.getString("driver_nic_no"));
				dto.setFullNameEng(rs.getString("driver_name"));
				dto.setAdd1Eng(rs.getString("driver_address_1"));
				dto.setAdd2Eng(rs.getString("driver_address_2"));
				dto.setCityEng(rs.getString("driver_address_3"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public DriverConductorRegistrationDTO getComplaintConductor(String complaintNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		DriverConductorRegistrationDTO dto = new DriverConductorRegistrationDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_complain_conductor_det WHERE complain_ref_no=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setSeq(rs.getLong("cond_seq"));
				dto.setDriverConductorId(rs.getString("cond_id"));
				dto.setNic(rs.getString("cond_nic_no"));
				dto.setFullNameEng(rs.getString("cond_name"));
				dto.setAdd1Eng(rs.getString("cond_address_1"));
				dto.setAdd2Eng(rs.getString("cond_address_2"));
				dto.setCityEng(rs.getString("cond_address_3"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public BigDecimal getOffenceCharge(String offenceCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal amount = new BigDecimal(0);
		int attempt_count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_offence_management_det  "
					+ "WHERE omd_is_public_complain = 'Y' AND  omd_is_amount = 'Y' AND  omd_offence_code = ? ";

			// TODO

			if (attempt_count >= 3) {
				query += "AND omd_attempt_code = 'TT';";
			} else if (attempt_count == 2) {
				query += "AND omd_attempt_code = 'ST';";
			} else {
				query += "AND omd_attempt_code = 'FT';";
			}

			ps = con.prepareStatement(query);
			ps.setString(1, offenceCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				amount = rs.getBigDecimal("omd_charge_amount");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return amount;
	}

	@Override
	public boolean saveOffenceCharge(String driID, String driNic, String condID, String condNic, String complaintNo,
			CommittedOffencesDTO offence, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps_point = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";
			long seq = 0;

			if (offence.getChr_seq() == null || offence.getChr_seq() == 0) {

				query = "INSERT INTO public.nt_t_offence_charge_sheet  (chr_seq, chr_complaint_no, chr_offence_code, chr_offence_desc, chr_charge_amount, chr_created_by, chr_created_date)"
						+ " VALUES(?,?,?,?,?,?,?);";
				seq = Utils.getNextValBySeqName(con, "seq_nt_t_offence_charge_sheet");

				ps = con.prepareStatement(query);
				ps.setLong(1, seq);
				ps.setString(2, complaintNo);
				ps.setString(3, offence.getCode());
				ps.setString(4, offence.getDescription());
				ps.setBigDecimal(5, offence.getCharge());
				ps.setString(6, user);
				ps.setTimestamp(7, timestamp);
				ps.executeUpdate();

			} else {
				query = "UPDATE public.nt_t_offence_charge_sheet SET chr_charge_amount=?, chr_modified_by=?, chr_modified_date=? "
						+ " WHERE chr_seq=?;";

				ps = con.prepareStatement(query);
				ps.setBigDecimal(1, offence.getCharge());
				ps.setString(2, user);
				ps.setTimestamp(3, timestamp);
				ps.setLong(4, offence.getChr_seq());
				ps.executeUpdate();
			}

			// Save Driver Conductor Points
			long p_seq = 0;

			if (offence.getP_seq() == null || offence.getP_seq() == 0) {

				query = "INSERT INTO public.nt_t_driver_conductor_points_det "
						+ "(p_seq, p_driver_id, p_driver_nic, p_driver_points, p_conductor_id, p_conductor_nic, p_conductor_points, p_complain_no, p_offence_code, p_created_by, p_created_date) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?);";
				p_seq = Utils.getNextValBySeqName(con, "seq_nt_t_driver_conductor_points_det");

				ps_point = con.prepareStatement(query);
				ps_point.setLong(1, p_seq);
				ps_point.setString(2, driID);
				ps_point.setString(3, driNic);
				ps_point.setBigDecimal(4, offence.getDriverPoints());
				ps_point.setString(5, condID);
				ps_point.setString(6, condNic);
				ps_point.setBigDecimal(7, offence.getConductorPoints());
				ps_point.setString(8, complaintNo);
				ps_point.setString(9, offence.getCode());
				ps_point.setString(10, user);
				ps_point.setTimestamp(11, timestamp);
				ps_point.executeUpdate();

			} else {
				query = "UPDATE public.nt_t_driver_conductor_points_det SET p_driver_id=?, p_driver_nic=?,p_driver_points=?, p_conductor_id=?, p_conductor_nic=?,p_conductor_points=?, p_modified_by=?, p_modified_date=? "
						+ " WHERE p_complain_no=? AND p_offence_code=?;";

				ps_point = con.prepareStatement(query);
				ps_point.setString(1, driID);
				ps_point.setString(2, driNic);
				ps_point.setBigDecimal(3, offence.getDriverPoints());
				ps_point.setString(4, condID);
				ps_point.setString(5, condNic);
				ps_point.setBigDecimal(6, offence.getConductorPoints());
				ps_point.setString(7, user);
				ps_point.setTimestamp(8, timestamp);
				ps_point.setString(9, complaintNo);
				ps_point.setString(10, offence.getCode());
				ps_point.executeUpdate();
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
	public CommittedOffencesDTO getDriverConductorPoints(String complaintNo, CommittedOffencesDTO c) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT * FROM nt_t_driver_conductor_points_det WHERE p_complain_no= ? AND p_offence_code = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			ps.setString(2, c.getCode());
			rs = ps.executeQuery();

			c.setP_seq(null);
			c.setDriverApplicable(false);
			c.setDriverPoints(null);
			c.setConductorApplicable(false);
			c.setConductorPoints(null);

			while (rs.next()) {
				c.setP_seq(rs.getLong("p_seq"));
				if (rs.getString("p_driver_nic") != null)
					c.setDriverApplicable(true);
				c.setDriverPoints(rs.getBigDecimal("p_driver_points"));
				if (rs.getString("p_conductor_nic") != null)
					c.setConductorApplicable(false);
				c.setConductorPoints(rs.getBigDecimal("p_conductor_points"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return c;
	}

	@Override
	public BigDecimal getDriverPointsByComplaint(String complaintNo, String driverId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal points = new BigDecimal(0);

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT sum(p_driver_points) FROM nt_t_driver_conductor_points_det WHERE p_complain_no = ? AND p_driver_nic = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			ps.setString(2, driverId);
			rs = ps.executeQuery();

			while (rs.next()) {
				points = rs.getBigDecimal(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return points;
	}

	@Override
	public BigDecimal getConductorPointsByComplaint(String complaintNo, String conductorId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal points = new BigDecimal(0);

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT sum(p_conductor_points) FROM nt_t_driver_conductor_points_det WHERE p_complain_no = ? AND p_conductor_nic = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			ps.setString(2, conductorId);
			rs = ps.executeQuery();

			while (rs.next()) {
				points = rs.getBigDecimal(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return points;
	}

	@Override
	public List<DropDownDTO> getInquiryDrivers() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcr_driver_conductor_id FROM nt_t_driver_conductor_registration a\r\n"
					+ "WHERE a.dcr_training_type in ('FD','ND','DD','RD','RRD','RRRD') AND a.dcr_status IN ('A','TA') ORDER BY dcr_driver_conductor_id;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_driver_conductor_id"));
				dto.setDescription(rs.getString("dcr_driver_conductor_id"));
				drpdwnDTOList.add(dto);
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
	public List<DropDownDTO> getInquiryConductors() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select dcr_driver_conductor_id FROM nt_t_driver_conductor_registration a\r\n"
					+ "WHERE a.dcr_training_type in ('FC','NC','DC','RC','RRC','RRRC') AND a.dcr_status IN ('A','TA') ORDER BY dcr_driver_conductor_id;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				dto.setCode(rs.getString("dcr_driver_conductor_id"));
				dto.setDescription(rs.getString("dcr_driver_conductor_id"));
				drpdwnDTOList.add(dto);
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
	public boolean updateComplaintStatus(ComplaintRequestDTO selectedComplaintDTO, String status, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean success = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_complain_request "
					+ "SET process_status = ?, modified_by = ?, modified_date = ? " + "WHERE seq = ?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, status);
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setLong(4, selectedComplaintDTO.getComplainSeq());
			stmt.executeUpdate();

			con.commit();
			
			
			success = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public void sendComplaintRefSMS(String contactNo, String message, String subject) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pending_alerts");

			String query = "INSERT INTO nt_t_pending_alerts(seq,alert_type,sms_message,receipient_mobileno,status,message_subject) VALUES (?,?,?,?,?,?)";

			stmt = con.prepareStatement(query);

			stmt.setLong(1, seqNo);
			stmt.setString(2, "sms");
			stmt.setString(3, message);
			stmt.setString(4, contactNo);
			stmt.setString(5, "P");
			stmt.setString(6, subject);
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
	public String generateVoucherNOForChrgeSheet() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strVouNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "select chv_voucher_no  from public.nt_t_offence_charge_sheet_voucher order by chv_created_date   desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("chv_voucher_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strVouNo = "CHV" + currYear + ApprecordcountN;
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
					strVouNo = "CHV" + currYear + ApprecordcountN;
				}
			} else
				strVouNo = "CHV" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return strVouNo;
	}

	@Override
	public void insertVoucherDetails(String voucher, ComplaintRequestDTO selectedComplaintDTO, String user,
			BigDecimal totalAmt) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String accountNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "";
			String query1 = "";
			query1 = "select string_value\r\n"
					+ "from public.nt_r_parameters where param_name='CHARGE_SHEET_VOUCHER_ACCOUNT_NUMBER';";

			ps1 = con.prepareStatement(query1);
			rs = ps1.executeQuery();

			while (rs.next()) {
				accountNo = rs.getString("string_value");
			}

			long seq = 0;

			query = "INSERT INTO public.nt_t_offence_charge_sheet_voucher\r\n"
					+ "(chv_seq, chv_complaint_no, chv_voucher_no, chv_account_no, chv_charge_amount_tot, chv_created_by, chv_created_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
			seq = Utils.getNextValBySeqName(con, "seq_nt_t_offence_charge_sheet_voucher");

			ps = con.prepareStatement(query);
			ps.setLong(1, seq);
			ps.setString(2, selectedComplaintDTO.getComplaintNo());
			ps.setString(3, voucher);
			ps.setString(4, accountNo);
			ps.setBigDecimal(5, totalAmt);
			ps.setString(6, user);
			ps.setTimestamp(7, timestamp);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

	}

	@Override
	public String getExisteingVoucherNo(String complainNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String voucherNoExisting = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select chv_voucher_no from  public.nt_t_offence_charge_sheet_voucher where chv_complaint_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, complainNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				voucherNoExisting = rs.getString("chv_voucher_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return voucherNoExisting;
	}

	@Override
	public List<String> getComplainNoPendingApprove() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> compliantDrpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select chv_complaint_no  from public.nt_t_offence_charge_sheet_voucher where chv_voucher_approved is null order by chv_complaint_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				compliantDrpdwnDTOList.add(rs.getString("chv_complaint_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return compliantDrpdwnDTOList;
	}

	@Override
	public List<String> getVoucherNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> voucherDrpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select chv_voucher_no   from public.nt_t_offence_charge_sheet_voucher where chv_voucher_approved  is  null order by chv_voucher_no ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				voucherDrpdwnDTOList.add(rs.getString("chv_voucher_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return voucherDrpdwnDTOList;
	}

	@Override
	public ComplaintRequestDTO getValuesByComplainNo(String compliantNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ComplaintRequestDTO dto = new ComplaintRequestDTO();
		try {
			con = ConnectionManager.getConnection();
			String query = "select permit_no ,vehicle_no  from public.nt_m_complain_request where complain_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, compliantNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new ComplaintRequestDTO();
				dto.setPermitNo(rs.getString("permit_no"));
				dto.setVehicleNo(rs.getString("vehicle_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public List<String> getvoucherNoListByComplaintNo(String complaintNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> voucherDrpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select chv_voucher_no  from public.nt_t_offence_charge_sheet_voucher where chv_voucher_approved  is  null and chv_complaint_no =? order by chv_voucher_no ;";

			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				voucherDrpdwnDTOList.add(rs.getString("chv_voucher_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return voucherDrpdwnDTOList;
	}

	@Override
	public List<ComplaintRequestDTO> getPaymentDetailsOnGrid(String compliantNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> gridDataDTOList = new ArrayList<ComplaintRequestDTO>();
		String query = null;
		try {
			con = ConnectionManager.getConnection();
			if (compliantNo != null && !compliantNo.trim().isEmpty()) {
				query = "select a.chv_voucher_no,a.chv_charge_amount_tot,b.permit_no,b.vehicle_no,chv_complaint_no,chv_voucher_approved,c.rec_receipt_no \r\n"
						+ "from public.nt_t_offence_charge_sheet_voucher a\r\n"
						+ "inner join public.nt_m_complain_request b on b.complain_no=a.chv_complaint_no\r\n"
						+ "left outer join public.nt_t_grievance_chargesheet_receipt c on a.chv_complaint_no = c.rec_complaint_no"
						+ " where a.chv_complaint_no=?" + " order by a.chv_voucher_approved desc, a.chv_voucher_no ;";
				ps = con.prepareStatement(query);
				ps.setString(1, compliantNo);
			} else {
				query = "select a.chv_voucher_no,a.chv_charge_amount_tot,b.permit_no,b.vehicle_no,chv_complaint_no,chv_voucher_approved,c.rec_receipt_no  \r\n"
						+ "from public.nt_t_offence_charge_sheet_voucher a\r\n"
						+ "inner join public.nt_m_complain_request b on b.complain_no=a.chv_complaint_no "
						+ " left outer join public.nt_t_grievance_chargesheet_receipt c on a.chv_complaint_no = c.rec_complaint_no"
						+ " order by a.chv_voucher_approved desc, a.chv_voucher_no;";

				ps = con.prepareStatement(query);

			}

			rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintRequestDTO dt = new ComplaintRequestDTO();
				dt.setPermitNo(rs.getString("permit_no"));
				dt.setVehicleNo(rs.getString("vehicle_no"));
				dt.setVoucherNo(rs.getString("chv_voucher_no"));
				dt.setTotalCharge(rs.getBigDecimal("chv_charge_amount_tot"));
				dt.setComplaintNo(rs.getString("chv_complaint_no"));
				if (rs.getString("chv_voucher_approved") != null) {
					if (rs.getString("chv_voucher_approved").equals("A")) {
						dt.setVoucherApprovedStatus("Approved");
					}
					if (rs.getString("chv_voucher_approved").equals("R")) {
						dt.setVoucherApprovedStatus("Reject");
					}
				} else {
					dt.setVoucherApprovedStatus("Pending");
				}
				dt.setReceiptNoForGr(rs.getString("rec_receipt_no"));
				gridDataDTOList.add(dt);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return gridDataDTOList;
	}

	@Override
	public ComplaintRequestDTO getVoucherSatus(String voucherNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ComplaintRequestDTO voucherApprovedDTO = new ComplaintRequestDTO();
		String query = null;
		try {
			con = ConnectionManager.getConnection();

			query = "select chv_voucher_approved  from  public.nt_t_offence_charge_sheet_voucher  where chv_voucher_no=?;";

			ps = con.prepareStatement(query);

			ps.setString(1, voucherNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				voucherApprovedDTO = new ComplaintRequestDTO();
				voucherApprovedDTO.setVoucherApprovedStatus(rs.getString("chv_voucher_approved"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return voucherApprovedDTO;
	}

	@Override
	public boolean updateApproveRejectVoucher(String voucheNo, String status, String reason, String approvedBy) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean success = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_offence_charge_sheet_voucher "
					+ "SET  chv_voucher_approved  = ?,  chv_approved_reject_by   = ?, chv_approved_reject_date = ? ,chv_reject_reason =?"
					+ "WHERE chv_voucher_no  = ?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, status);
			stmt.setString(2, approvedBy);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, reason);
			stmt.setString(5, voucheNo);
			stmt.executeUpdate();

			con.commit();
			success = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public List<ComplaintRequestDTO> showUser() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ComplaintRequestDTO> returnUserList = new ArrayList<ComplaintRequestDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  emp_user_id FROM public.nt_m_employee where emp_user_id is not null and  emp_user_status='A';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintRequestDTO employeeDTO = new ComplaintRequestDTO();
				employeeDTO.setUserId(rs.getString("emp_user_id"));

				returnUserList.add(employeeDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnUserList;
	}

	@Override
	public List<String> getApprovedVoucherNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> voucherDrpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select chv_voucher_no from public.nt_t_offence_charge_sheet_voucher where chv_voucher_approved='A' order by chv_approved_reject_date desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				voucherDrpdwnDTOList.add(rs.getString("chv_voucher_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return voucherDrpdwnDTOList;
	}

	@Override
	public List<String> complaintNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> compliantDrpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select chv_complaint_no  from public.nt_t_offence_charge_sheet_voucher where chv_voucher_approved='A' order by chv_complaint_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				compliantDrpdwnDTOList.add(rs.getString("chv_complaint_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return compliantDrpdwnDTOList;
	}

	@Override
	public ComplaintRequestDTO showDataByVoucherOrComplaintNo(String string1, String string2) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		ComplaintRequestDTO dto = new ComplaintRequestDTO();

		try {
			con = ConnectionManager.getConnection();
			if (string1 != null && !string1.trim().isEmpty() && (string2 == null || string2.trim().isEmpty())) {
				String query = "select b.vehicle_no,b.complain_no\r\n" + "from public.nt_m_complain_request b\r\n"
						+ "inner join public.nt_t_offence_charge_sheet_voucher a on a.chv_complaint_no=b.complain_no\r\n"
						+ "where a.chv_voucher_no=?;";

				ps = con.prepareStatement(query);
				ps.setString(1, string1);
				rs = ps.executeQuery();

				while (rs.next()) {
					dto = new ComplaintRequestDTO();
					dto.setComplaintNo(rs.getString("complain_no"));
					dto.setVehicleNo(rs.getString("vehicle_no"));
				}
			}
			if (string2 != null && !string2.trim().isEmpty() && (string1 == null || string1.trim().isEmpty())) {
				String query = "select a.chv_voucher_no,b.vehicle_no\r\n"
						+ "from public.nt_t_offence_charge_sheet_voucher a\r\n"
						+ "inner join public.nt_m_complain_request b on b.complain_no=a.chv_complaint_no\r\n"
						+ "where a.chv_complaint_no=?;";

				ps1 = con.prepareStatement(query);
				ps1.setString(1, string2);
				rs1 = ps1.executeQuery();

				while (rs.next()) {
					dto = new ComplaintRequestDTO();
					dto.setVoucherNo(rs.getString("chv_voucher_no"));
					dto.setVehicleNo(rs.getString("vehicle_no"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs1);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public List<ComplaintRequestDTO> getGenerateReceiptListForGrievance(ComplaintRequestDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> gridDataDTOList = new ArrayList<ComplaintRequestDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select chv_complaint_no ,chv_voucher_no ,chv_voucher_approved,b.vehicle_no  \r\n"
					+ "from  public.nt_t_offence_charge_sheet_voucher a\r\n"
					+ "inner join public.nt_m_complain_request b on b.complain_no=a.chv_complaint_no\r\n"
					+ "where a.chv_complaint_no=? and a.chv_voucher_no=? order by chv_approved_reject_date desc;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getComplaintNo());
			ps.setString(2, dto.getVoucherNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintRequestDTO gridDataDTO = new ComplaintRequestDTO();
				gridDataDTO.setComplaintNo(rs.getString("chv_complaint_no"));
				gridDataDTO.setVoucherNo(rs.getString("chv_voucher_no"));
				if (rs.getString("chv_voucher_approved") != null
						&& !rs.getString("chv_voucher_approved").trim().isEmpty()) {
					if (rs.getString("chv_voucher_approved").equalsIgnoreCase("A")) {
						gridDataDTO.setVoucherApprovedStatus("Approved");
					} else if (rs.getString("chv_voucher_approved").equalsIgnoreCase("R")) {
						gridDataDTO.setVoucherApprovedStatus("Rejected");
					}
				} else {
					gridDataDTO.setVoucherApprovedStatus("Pending");
				}
				gridDataDTO.setVehicleNo(dto.getVehicleNo());
				gridDataDTOList.add(gridDataDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return gridDataDTOList;
	}

	@Override
	public int getTotalAmount(String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int data = 0;
		try {

			con = ConnectionManager.getConnection();

			String query = "select chv_charge_amount_tot  from public.nt_t_offence_charge_sheet_voucher where chv_voucher_no =?;";
			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getInt("chv_charge_amount_tot");
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
	public boolean isReceiptgeneratedForGrievance(String voucherNo) {
		boolean isReceiptgenerated = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  rec_is_receipt_generated FROM public.nt_t_grievance_chargesheet_receipt "
					+ "WHERE rec_voucher_no=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next() && rs.getString("rec_is_receipt_generated").equals("Y")) {
				isReceiptgenerated = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isReceiptgenerated;
	}

	@Override
	public ComplaintRequestDTO getReceiptDetailsForGrievance(String voucherNo) {
		ComplaintRequestDTO receiptDetails = new ComplaintRequestDTO();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rec_payment_mode, rec_back_code, rec_branch_code, rec_is_diposit_to_branch,\r\n"
					+ "rec_cheque_no, rec_remarks \r\n"
					+ "FROM public.nt_t_grievance_chargesheet_receipt WHERE rec_voucher_no=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				receiptDetails.setPaymentModeCodeForGr(rs.getString("rec_payment_mode"));
				if (rs.getString("rec_back_code") != null) {
					receiptDetails.setBankCodeForGr(rs.getString("rec_back_code"));
					receiptDetails.setBranchCodeForGr(rs.getString("rec_branch_code"));

					// get bank and branch description
					String query2 = "SELECT b.description as bankDes, c.description as branchDes \r\n"
							+ "FROM public.nt_t_grievance_chargesheet_receipt r\r\n"
							+ "INNER JOIN nt_r_bank b on b.code= r.rec_back_code \r\n"
							+ "INNER JOIN nt_r_branch c on c.code= r.rec_branch_code \r\n"
							+ "WHERE rec_voucher_no=? \r\n" + "LIMIT 1;";
					ps2 = con.prepareStatement(query2);
					ps2.setString(1, voucherNo);
					rs2 = ps2.executeQuery();
					if (rs2.next()) {
						receiptDetails.setBankDesForGr(rs2.getString("bankDes"));
						receiptDetails.setBranchDesForGr(rs2.getString("branchDes"));
					}
				}
				if (rs.getString("rec_is_diposit_to_branch").equals("Y")) {
					receiptDetails.setDepositToBankForGr(true);
				} else if (rs.getString("rec_is_diposit_to_branch").equals("N")) {
					receiptDetails.setDepositToBankForGr(false);
				}
				receiptDetails.setChequeOrBankReceipt(rs.getString("rec_cheque_no"));
				receiptDetails.setReceiptRemarks(rs.getString("rec_remarks"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return receiptDetails;
	}

	@Override
	public boolean isPrintCompletedForGrievance(String voucherNo) {
		boolean isPrintCompleted = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  chv_isprint " + "FROM public.nt_t_offence_charge_sheet_voucher "
					+ "WHERE chv_voucher_no    =? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				String isPrint = rs.getString("chv_isprint");
				if (isPrint != null) {
					if (isPrint.equals("Y")) {
						isPrintCompleted = true;
					} else {
						isPrintCompleted = false;
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
		return isPrintCompleted;
	}

	@Override
	public List<ComplaintRequestDTO> getVoucherDetailsListForGrievance(String complaintNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> data = new ArrayList<ComplaintRequestDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select a.chr_offence_code,a.chr_offence_desc,a.chr_charge_amount,b.string_value from \r\n"
					+ "(select  chr_offence_code,chr_offence_desc,chr_charge_amount\r\n"
					+ "from public.nt_t_offence_charge_sheet\r\n" + "where chr_complaint_no=? )a,\r\n"
					+ "(select string_value\r\n"
					+ "from public.nt_r_parameters where param_name='CHARGE_SHEET_VOUCHER_ACCOUNT_NUMBER')b";

			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			rs = ps.executeQuery();

			ComplaintRequestDTO p;

			while (rs.next()) {
				p = new ComplaintRequestDTO();
				p.setOffenceCode(rs.getString("chr_offence_code"));
				p.setOffenceDes(rs.getString("chr_offence_desc"));
				p.setAmmount(rs.getBigDecimal("chr_charge_amount"));
				p.setAccountNO(rs.getString("string_value"));

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
	public void saveReceiptForGrievance(ComplaintRequestDTO selectedRow, String loginUser) {

		String receiptNo = generateReceiptNoForGrievance();
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement ps = null, stmt1 = null, stmt2 = null;
		try {

			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_grievance_chargesheet_receipt");
			String query = "INSERT INTO public.nt_t_grievance_chargesheet_receipt\r\n"
					+ "(rec_seq_no, rec_complaint_no,\r\n"
					+ "rec_voucher_no, rec_payment_mode, rec_is_diposit_to_branch, rec_receipt_no, rec_back_code,\r\n"
					+ "rec_branch_code, rec_cheque_no, rec_total_fee, rec_remarks, rec_is_receipt_generated, \r\n"
					+ "rep_created_by, rep_created_date)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(query);

			ps.setLong(1, seqNo);
			ps.setString(2, selectedRow.getComplaintNo());

			ps.setString(3, selectedRow.getVoucherNo());
			ps.setString(4, selectedRow.getPaymentModeCodeForGr());
			String depositToBank = selectedRow.getDepositToBankForGr() ? "Y" : "N";

			ps.setString(5, depositToBank);
			ps.setString(6, receiptNo);
			ps.setString(7, selectedRow.getBankCodeForGr());
			ps.setString(8, selectedRow.getBranchCodeForGr());
			ps.setString(9, selectedRow.getChequeOrBankReceipt());
			ps.setBigDecimal(10, selectedRow.getTotalAmount());
			ps.setString(11, selectedRow.getReceiptRemarks());
			ps.setString(12, "Y");

			ps.setString(13, loginUser);
			ps.setTimestamp(14, timestamp);

			ps.executeUpdate();

		

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}

	}

	public String generateReceiptNoForGrievance() {

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

			String sql = "SELECT rec_receipt_no FROM public.nt_t_grievance_chargesheet_receipt ORDER BY rep_created_date desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("rec_receipt_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strRecNo = "RGM" + currYear + ApprecordcountN;
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
					strRecNo = "RGM" + currYear + ApprecordcountN;
				}
			} else {
				strRecNo = "RGM" + currYear + "00001";
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
	public String getReceiptNoForPrintForGrievance(String voucherNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String receiptNoForPrint = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select rec_receipt_no  from  public.nt_t_grievance_chargesheet_receipt where rec_voucher_no =?;";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				receiptNoForPrint = rs.getString("rec_receipt_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return receiptNoForPrint;
	}

	@Override
	public String checkDataIntable(String complainNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String voucherNoExisting = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select chr_offence_code   from public.nt_t_offence_charge_sheet where chr_complaint_no =?;";

			ps = con.prepareStatement(query);
			ps.setString(1, complainNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				voucherNoExisting = rs.getString("chr_offence_code");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return voucherNoExisting;
	}

	@Override
	public List<String> getReceiptNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> voucherDrpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select  rec_receipt_no    from public.nt_t_grievance_chargesheet_receipt  where  rec_receipt_no   is not null order by  rec_receipt_no  ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				voucherDrpdwnDTOList.add(rs.getString("rec_receipt_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return voucherDrpdwnDTOList;
	}

	@Override
	public String getReceiptNoForPrintForGrievanceByComplaintno(String complaintNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String receiptNoForPrint = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select rec_receipt_no  from  public.nt_t_grievance_chargesheet_receipt where rec_complaint_no =?;";

			ps = con.prepareStatement(query);
			ps.setString(1, complaintNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				receiptNoForPrint = rs.getString("rec_receipt_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return receiptNoForPrint;
	}

	@Override
	public boolean updateParamSequenceN(String paramName) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		boolean success = false;
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE nt_r_parameters SET type = ? WHERE param_name = ?; ";
			ps2 = con.prepareStatement(query);
			ps2.setString(1, "00001");
			ps2.setString(2, paramName);
			ps2.executeUpdate();
			con.commit();
			
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean checkDataInChargeSheetTable(String compliantNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		boolean success = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_offence_charge_sheet \r\n" + "WHERE chr_complaint_no =?";
			ps2 = con.prepareStatement(query);
			ps2.setString(1, compliantNo);

			rs = ps2.executeQuery();
			while (rs.next()) {
				success = true;

			}

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public List<ComplaintRequestDTO> getPendingGenerateReceiptListForGrievance() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ComplaintRequestDTO> gridDataDTOList = new ArrayList<ComplaintRequestDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select chv_complaint_no ,chv_voucher_no ,chv_voucher_approved,b.vehicle_no  \r\n"
					+ "from  public.nt_t_offence_charge_sheet_voucher a\r\n"
					+ "inner join public.nt_m_complain_request b on b.complain_no=a.chv_complaint_no\r\n"
					+ "left outer join public.nt_t_grievance_chargesheet_receipt c on c.rec_complaint_no=a.chv_complaint_no\r\n"
					+ "where c.rec_receipt_no is null and a.chv_voucher_approved='A' order by chv_voucher_no desc";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintRequestDTO gridDataDTO = new ComplaintRequestDTO();
				gridDataDTO.setComplaintNo(rs.getString("chv_complaint_no"));
				gridDataDTO.setVoucherNo(rs.getString("chv_voucher_no"));
				if (rs.getString("chv_voucher_approved") != null
						&& !rs.getString("chv_voucher_approved").trim().isEmpty()) {
					if (rs.getString("chv_voucher_approved").equalsIgnoreCase("A")) {
						gridDataDTO.setVoucherApprovedStatus("Approved");
					} else if (rs.getString("chv_voucher_approved").equalsIgnoreCase("R")) {
						gridDataDTO.setVoucherApprovedStatus("Rejected");
					}
				} else {
					gridDataDTO.setVoucherApprovedStatus("Pending");
				}
				gridDataDTO.setVehicleNo(rs.getString("vehicle_no"));
				gridDataDTOList.add(gridDataDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return gridDataDTOList;
	}

	@Override
	public boolean updateChargeSheetVoucher(ComplaintRequestDTO selectedComplaintDTO, String user,
			BigDecimal totalAmt) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		String data = null;
		boolean success = false;
		
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "select chv_voucher_no from nt_t_offence_charge_sheet_voucher where chv_complaint_no = ?  and chv_voucher_approved is null";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedComplaintDTO.getComplaintNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				data = rs.getString("chv_voucher_no");
			}

			if (data != null) {
				String query2 = "UPDATE nt_t_offence_charge_sheet_voucher SET chv_charge_amount_tot = ? WHERE chv_complaint_no = ? AND chv_voucher_no = ?; ";
				ps2 = con.prepareStatement(query2);
				ps2.setBigDecimal(1, totalAmt);
				ps2.setString(2, selectedComplaintDTO.getComplaintNo());
				ps2.setString(3, data);
				ps2.executeUpdate();
				con.commit();

				success = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return success;
	}

	@SuppressWarnings({ "null", "resource", "unused" })
	@Override
	public List<ComplaintRequestDTO> getCommittedOffenceListByComplainSeqList(String vehicleNo, String permitNo,
			String complainNumber) {
		// get Complain sequence number list

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		List<String> data = new ArrayList<String>();
		String seq = null;
		String query1 = null;
		List<ComplaintRequestDTO> resultDTOList = new ArrayList<ComplaintRequestDTO>();

		try {

			con = ConnectionManager.getConnection();
			if (vehicleNo != null && !vehicleNo.trim().isEmpty()) {
				query1 = "SELECT seq FROM public.nt_m_complain_request WHERE vehicle_no =?";

				ps = con.prepareStatement(query1);
				ps.setString(1, vehicleNo);
				rs = ps.executeQuery();
			}
			if (permitNo != null && !permitNo.trim().isEmpty()) {
				query1 = "SELECT seq FROM public.nt_m_complain_request WHERE permit_no =?";

				ps = con.prepareStatement(query1);
				ps.setString(1, permitNo);
				rs = ps.executeQuery();
			}
			if (complainNumber != null && !complainNumber.trim().isEmpty()) {
				query1 = "SELECT seq FROM public.nt_m_complain_request WHERE complain_no =?";

				ps = con.prepareStatement(query1);
				ps.setString(1, complainNumber);
				rs = ps.executeQuery();
			}
			while (rs.next()) {
				ComplaintRequestDTO resultDTO = new ComplaintRequestDTO();
				resultDTO.setComplainSeq(rs.getLong("seq"));
				data.add(resultDTO.getComplainSeq().toString());
			}

			if (data != null) {
				for (String s : data) {
					String query2 = "SELECT is_applicable , remarks , offence_type_code,o.ofm_offence_desc FROM PUBLIC.NT_T_GRIEVANCE_COMMITED_OFFENCE \r\n"
							+ "inner join public.nt_m_offence_management o on o.ofm_offence_code  =NT_T_GRIEVANCE_COMMITED_OFFENCE.offence_type_code \r\n"
							+ "WHERE IS_APPLICABLE='Y' AND COMPLAIN_SEQ in ('" + s + "')";
					ps2 = con.prepareStatement(query2);
					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						ComplaintRequestDTO resultDTO = new ComplaintRequestDTO();
						if (rs2.getString(1) != null) {
							if (rs2.getString(1).equals("Y"))
								resultDTO.setApplicableYes(true);
						} else {
							resultDTO.setApplicableYes(false);
						}

						resultDTO.setRemarks(rs2.getString("remarks"));
						resultDTO.setOffence(rs2.getString("ofm_offence_desc"));
						resultDTOList.add(resultDTO);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return resultDTOList;

	}


	
	@Override
	public void updateInquiryRecommendation(String remark, String status, String user, String complainNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_complain_request SET sps_recommend_by=?,sps_is_recommend=?,sps_recommend_date=?,sps_recommend_remark=? WHERE complain_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, user);
			stmt.setString(2, status);
			if (status != null) {
				stmt.setString(3, currentDate);
			} else {
				stmt.setString(3, null);
			}
			stmt.setString(4, remark);
			stmt.setString(5, complainNo);
			stmt.executeUpdate();

			con.commit();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}


	@Override
	public void updateInquiryApproval(String remark, String status, String user, String complainNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_complain_request SET sps_approved_by=?,sps_is_approved=?,sps_approved_date=?,sps_approved_remark=? WHERE complain_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, user);
			stmt.setString(2, status);
			if (status != null) {
				stmt.setString(3, currentDate);
			} else {
				stmt.setString(3, null);
			}
			stmt.setString(4, remark);
			stmt.setString(5, complainNo);
			stmt.executeUpdate();

			con.commit();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}
	
	

	@Override
	public void complainRequestHistory(ComplaintRequestDTO selectedComplaintDTO, String complainNo, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "INSERT into public.nt_h_complain_request  "
					+ " (SELECT * FROM public.nt_m_complain_request WHERE complain_no = ?) ";

			ps = con.prepareStatement(query);
			ps.setString(1, complainNo);
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
	public void complainRequestHistory(String complainNo) {
		Connection con = null;
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query = "INSERT into public.nt_h_complain_request  "
					+ " (SELECT * FROM public.nt_m_complain_request WHERE complain_no = ?) ";

			ps = con.prepareStatement(query);
			ps.setString(1, complainNo);
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
	public void updateGrantPermission(String remark, String status, String user, String vehicleNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_complain_request SET grant_permission_given_by=?,grant_permission_status=?,grant_permission_given_date=?,grant_permission_remark=? WHERE vehicle_no=? AND grant_permission_status IS NULL";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, user);
			stmt.setString(2, status);
			stmt.setString(3, currentDate);
			stmt.setString(4, remark);
			stmt.setString(5, vehicleNo);
			stmt.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	@Override
	public void grantPermission(String complainNo, String vehicleNo, String permitNo, String loginUser,
			String specialRemark) {
		Connection con = null;
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = dateFormat.format(timestamp);

		try {

			con = ConnectionManager.getConnection();

			String query = "INSERT INTO public.nt_m_grant_permission (seq,complain_no,vehicle_no,permit_no,grant_permission_given_by,grant_permission_given_date,remark)  VALUES(?,?,?,?,?,?,?)";

			ps = con.prepareStatement(query);
			ps.setLong(1, Utils.getNextValBySeqName(con, "public.seq_nt_m_grant_permission"));
			ps.setString(2, complainNo);
			ps.setString(3, vehicleNo);
			ps.setString(4, permitNo);
			ps.setString(5, loginUser);
			ps.setString(6, currentDate);
			ps.setString(7, specialRemark);
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
	public void reduceDemeritPointsFromTotalPoints(String driverId, String conductorId, BigDecimal driverDemeritPoints,
			BigDecimal conductorDemeritPoints,ComplaintRequestDTO selectedComplaintDTO,String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = dateFormat.format(timestamp);
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		
		BigDecimal driverSeq = new BigDecimal(0);
		BigDecimal conductorSeq = new BigDecimal(0);
		
		BigDecimal driverPoints = new BigDecimal(0);
		BigDecimal conductorPoints = new BigDecimal(0);
		BigDecimal updatedDriverPoints = new BigDecimal(0);
		BigDecimal updatedConductorPoints = new BigDecimal(0);
		List<BigDecimal> pointsList = new ArrayList<>();

		if(driverDemeritPoints == null) {
			driverDemeritPoints = new BigDecimal(0);
		}
		if(conductorDemeritPoints ==null) {
			conductorDemeritPoints = new BigDecimal(0);
		}
		try {
			con = ConnectionManager.getConnection();

			String sql = "select dcr_points,dcr_seq from public.nt_t_driver_conductor_registration\r\n" + 
					"where dcr_driver_conductor_id =? order by dcr_created_date  desc limit 1 ;";

			
				ps = con.prepareStatement(sql);
			ps.setString(1, driverId);
			rs = ps.executeQuery();

			while (rs.next()) {
				driverPoints = rs.getBigDecimal("dcr_points");
				driverSeq = rs.getBigDecimal("dcr_seq");
			}
			updatedDriverPoints =driverPoints.subtract(driverDemeritPoints);
			pointsList.add(updatedDriverPoints);
			pointsList.add(driverSeq);
			
			String sql2 = "select dcr_points,dcr_seq from public.nt_t_driver_conductor_registration\r\n" + 
					"where dcr_driver_conductor_id =? order by dcr_created_date  desc limit 1 ;";

			
				ps2 = con.prepareStatement(sql2);
			ps2.setString(1, conductorId);
			rs2 = ps2.executeQuery();

			while (rs2.next()) {
				conductorPoints = rs2.getBigDecimal("dcr_points");
				conductorSeq = rs2.getBigDecimal("dcr_seq");
			}
			updatedConductorPoints = conductorPoints.subtract(conductorDemeritPoints);
			pointsList.add(updatedConductorPoints);
			pointsList.add(conductorSeq);
			
			
			
			
			
			for(int i =0 ; i<2;i++ ) {
				
				
				
				//insert into history
				String historySql = "insert into public.nt_h_driver_conductor_registration (\r\n" + 
						"select * from public.nt_t_driver_conductor_registration\r\n" + 
						"where dcr_driver_conductor_id =?  order by dcr_created_date  desc limit 1 \r\n" + 
						");";

				stmt2 = con.prepareStatement(historySql);
				if (i == 0) {
					stmt2.setString(1, driverId);
				} else {
					stmt2.setString(1, conductorId);
				}
				stmt2.executeUpdate();	
			String updateSql = "UPDATE public.nt_t_driver_conductor_registration SET dcr_points=?,dcr_modified_by=? WHERE dcr_driver_conductor_id=? and dcr_seq=? ";

			stmt = con.prepareStatement(updateSql);
			
			
			if (i==0) {
				stmt.setBigDecimal(1,pointsList.get(i) );
				stmt.setString(2,"UPDATED BY SYSTEM" );
				stmt.setString(3, driverId);
				stmt.setInt(4, driverSeq.intValue());
			} else {
				stmt.setBigDecimal(1,pointsList.get(i+1) );
				stmt.setString(2,"UPDATED BY SYSTEM" );
				stmt.setString(3, conductorId);
				stmt.setInt(4, conductorSeq.intValue());
			}
			
			stmt.executeUpdate();
			}
			con.commit();
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs2);
			ConnectionManager.close(con);
		}
	}
	
	@Override
	public List<CommittedOffencesDTO> getCommittedOffenceListForPublicComplaint() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommittedOffencesDTO> drpdwnDTOList = new ArrayList<CommittedOffencesDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct ofm_seq,ofm_offence_code,ofm_offence_desc FROM public.nt_m_offence_management master " 
					+ "inner join public.nt_t_offence_management_det det on det.omd_offence_code =master.ofm_offence_code " 
					+ "WHERE master.status = 'A' and det.omd_is_public_complain = 'Y' " 
					+ "ORDER BY ofm_seq;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommittedOffencesDTO dto = new CommittedOffencesDTO();
				dto.setCode(rs.getString("ofm_offence_code"));
				dto.setDescription(rs.getString("ofm_offence_desc"));
				drpdwnDTOList.add(dto);
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
	
	private void insertTaskInquiryRecord(Connection con, String user,
			Timestamp timestamp, String status,String busNo,String complaintNo, String permitNo, String function, String funDes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, complaint_no, permit_no,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, busNo);
			psInsert.setString(5, complaintNo);
			psInsert.setString(6,permitNo);
			psInsert.setString(7, function);
			psInsert.setString(8, funDes);
			
			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void insertTaskInquiryRecord(Connection con, ComplaintRequestDTO selectedComplaintDTO,
			Timestamp timestamp, String status, String function,String user, String funDes){
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, complaint_no, permit_no,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, selectedComplaintDTO.getVehicleNo());
			psInsert.setString(5, selectedComplaintDTO.getComplaintNo());
			psInsert.setString(6,selectedComplaintDTO.getPermitNo());
			psInsert.setString(7, function);
			psInsert.setString(8, funDes);
			
			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void beanLinkMethod(String user, String busNo,String complaintNo, String permitNo, String funDes,String status) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, user, timestamp, status, busNo, complaintNo, permitNo, "Grievances Management", funDes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void beanLinkMethod(ComplaintRequestDTO selectedComplaintDTO,String user,String des,String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, selectedComplaintDTO, timestamp, des, "Grievances Management",user,funDes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}


}