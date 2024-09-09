package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.EventParticipationDTO;
import lk.informatics.ntc.model.dto.LetterMaintenanceDTO;
import lk.informatics.ntc.model.dto.ProgramDTO;
import lk.informatics.ntc.model.dto.ProjectDTO;
import lk.informatics.ntc.model.dto.TransportDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class AwarenessManagementServiceImpl implements AwarenessManagementService {

	@Override
	public List<ProjectDTO> getProjectList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ProjectDTO> projectDTOList = new ArrayList<ProjectDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_awareness_program ORDER BY awp_reference_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ProjectDTO dto = new ProjectDTO();
				dto.setAwarenessProgram_seq(rs.getLong("awp_seq"));
				dto.setRefNo(rs.getString("awp_reference_no"));
				dto.setProjectName(rs.getString("awp_project_name"));
				dto.setNoOfProject(rs.getInt("awp_no_of_projects"));
				dto.setYear(rs.getString("awp_year"));
				dto.setStatus(rs.getString("awp_status"));
				projectDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return projectDTOList;
	}

	@Override
	public List<ProgramDTO> getProgramList(String projectRefCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<ProgramDTO> programDTOList = new ArrayList<ProgramDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_awareness_program_det WHERE apd_prj_reference_no = ? ORDER BY apd_sub_reference_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, projectRefCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				ProgramDTO dto = new ProgramDTO();
				dto.setDetail_seq(rs.getLong("apd_seq"));
				dto.setSubRefNo(rs.getString("apd_sub_reference_no"));
				dto.setProvince(rs.getString("adp_province_code"));
				dto.setDistrict(rs.getString("adp_district_code"));
				dto.setZone(rs.getString("adp_zone_code"));
				dto.setScheduleDate(rs.getString("adp_schedule_date"));
				dto.setPlace(rs.getString("adp_place"));
				dto.setStatus(rs.getString("adp_status"));
				dto.setYear(rs.getString("adp_year"));
				programDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return programDTOList;
	}

	@Override
	public boolean insertNewProject(ProjectDTO newProjectDTO, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			conn = ConnectionManager.getConnection();
			long seq = Utils.getNextValBySeqName(conn, "seq_nt_m_awareness_program");

			String query = "INSERT INTO public.nt_m_awareness_program (awp_seq, awp_reference_no, awp_project_name, awp_no_of_projects, awp_year, awp_status, awp_created_by, awp_created_date)"
					+ "VALUES(?,?,?,?,?,?,?,?);";

			stmt = conn.prepareStatement(query);
			stmt.setLong(1, seq);
			stmt.setString(2, newProjectDTO.getRefNo());
			stmt.setString(3, newProjectDTO.getProjectName());
			stmt.setInt(4, newProjectDTO.getNoOfProject());
			stmt.setString(5, newProjectDTO.getYear());
			stmt.setString(6, newProjectDTO.getStatus());
			stmt.setString(7, user);
			stmt.setTimestamp(8, timestamp);
			stmt.executeUpdate();

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public boolean updateProject(ProjectDTO newProjectDTO, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			conn = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_awareness_program "
					+ " SET awp_project_name = ?, awp_no_of_projects=?, awp_year=?, awp_status=?, awp_modified_by=?, awp_modified_date=?"
					+ " WHERE awp_reference_no=?;";

			stmt = conn.prepareStatement(query);
			stmt.setString(1, newProjectDTO.getProjectName());
			stmt.setInt(2, newProjectDTO.getNoOfProject());
			stmt.setString(3, newProjectDTO.getYear());
			stmt.setString(4, newProjectDTO.getStatus());
			stmt.setString(5, user);
			stmt.setTimestamp(6, timestamp);
			stmt.setString(7, newProjectDTO.getRefNo());
			stmt.executeUpdate();

			// update status of sub projects

			String query2 = "UPDATE public.nt_t_awareness_program_det SET adp_status=?, adp_modified_by=?, adp_modified_date=?"
					+ " WHERE apd_prj_reference_no=?;";
			stmt2 = conn.prepareStatement(query2);
			stmt2.setString(1, newProjectDTO.getStatus());
			stmt2.setString(2, user);
			stmt2.setTimestamp(3, timestamp);
			stmt2.setString(4, newProjectDTO.getRefNo());
			stmt2.executeUpdate();

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public boolean removeProject(ProjectDTO projectDTO) {
		Connection conn = null;
		PreparedStatement stmt_retrieve = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt_pm = null;
		PreparedStatement stmt_pj = null;
		ResultSet rs_retrieve = null;

		try {
			conn = ConnectionManager.getConnection();

			// delete sub projects data
			String query = "SELECT apd_sub_reference_no FROM public.nt_t_awareness_program_det WHERE apd_prj_reference_no=?;";
			stmt_retrieve = conn.prepareStatement(query);
			stmt_retrieve.setString(1, projectDTO.getRefNo());
			rs_retrieve = stmt_retrieve.executeQuery();
			List<String> subRefNoList = new ArrayList<String>();
			while (rs_retrieve.next()) {
				subRefNoList.add(rs_retrieve.getString(1));
			}

			for (String subRef : subRefNoList) {
				query = "DELETE FROM public.nt_t_project_letter_det WHERE pld_sub_reference_no=?;";
				stmt1 = conn.prepareStatement(query);
				stmt1.setString(1, subRef);
				stmt1.executeUpdate();

				query = "DELETE FROM public.nt_m_event_management WHERE evm_sub_reference_no=?;";
				stmt2 = conn.prepareStatement(query);
				stmt2.setString(1, subRef);
				stmt2.executeUpdate();

				query = "DELETE FROM public.nt_t_event_participation_det WHERE epd_sub_reference_no=?;";
				stmt3 = conn.prepareStatement(query);
				stmt3.setString(1, subRef);
				stmt3.executeUpdate();

				query = "DELETE FROM public.nt_t_event_transport_det WHERE etd_sub_reference_no=?;";
				stmt4 = conn.prepareStatement(query);
				stmt4.setString(1, subRef);
				stmt4.executeUpdate();
			}

			// delete sub projects
			query = "DELETE FROM public.nt_t_awareness_program_det WHERE apd_prj_reference_no=?;";
			stmt_pm = conn.prepareStatement(query);
			stmt_pm.setString(1, projectDTO.getRefNo());
			stmt_pm.executeUpdate();

			// delete main project
			query = "DELETE FROM public.nt_m_awareness_program WHERE awp_reference_no=?;";
			stmt_pj = conn.prepareStatement(query);
			stmt_pj.setString(1, projectDTO.getRefNo());
			stmt_pj.executeUpdate();

			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt_pm);
			ConnectionManager.close(stmt_pj);
			ConnectionManager.close(stmt_retrieve);
			ConnectionManager.close(rs_retrieve);
			ConnectionManager.close(conn);
		}
		return true;
	}

	@Override
	public boolean insertNewProgram(long projectSeq, String projectRefCode, ProgramDTO newProgramDTO, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String scheduledDate = null;

		try {
			SimpleDateFormat newDf = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			scheduledDate = newDf.format(oldDf.parse(newProgramDTO.getScheduleDate()));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			conn = ConnectionManager.getConnection();
			long seq = Utils.getNextValBySeqName(conn, "seq_nt_t_awareness_program_det");

			String query = "INSERT INTO public.nt_t_awareness_program_det "
					+ "(apd_seq, apd_sub_reference_no, apd_prj_reference_no, adp_province_code, adp_district_code, adp_zone_code, adp_schedule_date, adp_place, adp_status, adp_created_by, adp_created_date)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?);";

			stmt = conn.prepareStatement(query);
			stmt.setLong(1, seq);
			stmt.setString(2, newProgramDTO.getSubRefNo());
			stmt.setString(3, projectRefCode);
			stmt.setString(4, newProgramDTO.getProvince());
			stmt.setString(5, newProgramDTO.getDistrict());
			stmt.setString(6, newProgramDTO.getZone());
			stmt.setString(7, scheduledDate);
			stmt.setString(8, newProgramDTO.getPlace());
			stmt.setString(9, newProgramDTO.getStatus());
			stmt.setString(10, user);
			stmt.setTimestamp(11, timestamp);
			stmt.executeUpdate();

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public boolean updateProgram(ProgramDTO newProgramDTO, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String scheduledDate = null;

		try {
			SimpleDateFormat newDf = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			scheduledDate = newDf.format(oldDf.parse(newProgramDTO.getScheduleDate()));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			conn = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_awareness_program_det"
					+ " SET adp_province_code=?, adp_district_code=?, adp_zone_code=?, adp_schedule_date=?, adp_place=?, adp_status=?, adp_modified_by=?, adp_modified_date=?"
					+ " WHERE apd_sub_reference_no=?;";

			stmt = conn.prepareStatement(query);
			stmt.setString(1, newProgramDTO.getProvince());
			stmt.setString(2, newProgramDTO.getDistrict());
			stmt.setString(3, newProgramDTO.getZone());
			stmt.setString(4, scheduledDate);
			stmt.setString(5, newProgramDTO.getPlace());
			stmt.setString(6, newProgramDTO.getStatus());
			stmt.setString(7, user);
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, newProgramDTO.getSubRefNo());
			stmt.executeUpdate();

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public boolean removeProgram(ProgramDTO newProgramDTO) {
		Connection conn = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt_final = null;

		try {
			conn = ConnectionManager.getConnection();

			// delete programs data
			String query = "DELETE FROM public.nt_t_project_letter_det WHERE pld_sub_reference_no=?;";
			stmt1 = conn.prepareStatement(query);
			stmt1.setString(1, newProgramDTO.getSubRefNo());
			stmt1.executeUpdate();

			query = "DELETE FROM public.nt_m_event_management WHERE evm_sub_reference_no=?;";
			stmt2 = conn.prepareStatement(query);
			stmt2.setString(1, newProgramDTO.getSubRefNo());
			stmt2.executeUpdate();

			query = "DELETE FROM public.nt_t_event_participation_det WHERE epd_sub_reference_no=?;";
			stmt3 = conn.prepareStatement(query);
			stmt3.setString(1, newProgramDTO.getSubRefNo());
			stmt3.executeUpdate();

			query = "DELETE FROM public.nt_t_event_transport_det WHERE etd_sub_reference_no=?;";
			stmt4 = conn.prepareStatement(query);
			stmt4.setString(1, newProgramDTO.getSubRefNo());
			stmt4.executeUpdate();

			// delete program
			query = "DELETE FROM public.nt_t_awareness_program_det WHERE apd_sub_reference_no=?;";
			stmt_final = conn.prepareStatement(query);
			stmt_final.setString(1, newProgramDTO.getSubRefNo());
			stmt_final.executeUpdate();
			conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt_final);
			ConnectionManager.close(conn);
		}
		return true;
	}

	@Override
	public List<DropDownDTO> getEduZoneList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_r_education_zone WHERE active = 'A';";

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
	public List<DropDownDTO> getOfficerTypeList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> drpdwnDTOList = new ArrayList<DropDownDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_r_officer_type WHERE active = 'A';";

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
	public LetterMaintenanceDTO getLetterData(String subRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LetterMaintenanceDTO dto = new LetterMaintenanceDTO();
		boolean dataExist = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_awareness_program_det a, public.nt_t_project_letter_det l "
					+ " WHERE a.apd_sub_reference_no = l.pld_sub_reference_no AND l.pld_sub_reference_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, subRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setPld_seq(rs.getLong("pld_seq"));
				dto.setZoneIssueDate(rs.getString("pld_zone_letter_issue_date"));
				dto.setSchoolName(rs.getString("pld_school_name"));
				dto.setSchoolLetterDate(rs.getString("pld_school_letter_date"));
				dto.setPoliceName(rs.getString("pld_police_name"));
				dto.setPoliceLetterDate(rs.getString("pld_police_letter_date"));
				dto.setYear(rs.getString("adp_year"));
				dataExist = true;
			}
			if (!dataExist) {
				dto.setPld_seq(null);
				dto.setYear(null);
				dto.setZoneIssueDate(null);
				dto.setSchoolName(null);
				dto.setSchoolLetterDate(null);
				dto.setPoliceName(null);
				dto.setPoliceLetterDate(null);
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
	public ProgramDTO getEventDetails(ProgramDTO program) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean dataExist = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_event_management WHERE evm_sub_reference_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, program.getSubRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				program.setEvm_seq(rs.getLong("evm_seq"));
				program.setNoOfStudents(rs.getInt("evm_no_of_students"));
				program.setSchoolName(rs.getString("evm_school_name"));
				program.setEstimatedBudjet(rs.getBigDecimal("evm_existing_budget"));
				program.setActualBudjet(rs.getBigDecimal("evm_actual_budget"));
				program.setRemarks(rs.getString("evm_remarks"));
				dataExist = true;
			}
			if (!dataExist) {
				program.setNoOfStudents(0);
				program.setSchoolName(null);
				program.setEstimatedBudjet(null);
				program.setActualBudjet(null);
				program.setRemarks(null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return program;
	}

	@Override
	public List<EventParticipationDTO> getAllEventStaff(String subRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<EventParticipationDTO> dTOList = new ArrayList<EventParticipationDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_event_participation_det WHERE epd_sub_reference_no=? ORDER BY epd_participant_type;";

			ps = con.prepareStatement(query);
			ps.setString(1, subRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				EventParticipationDTO dto = new EventParticipationDTO();
				dto.setEpd_seq(rs.getLong("epd_seq"));
				dto.setTypeOfParticipation(rs.getString("epd_participant_type"));
				dto.setEmpNo(rs.getString("epd_emp_no"));
				dto.setEpfNo(rs.getString("epd_epf_no"));
				dto.setIdNo(rs.getString("epd_id_no"));
				dto.setParticipantName(rs.getString("epd_name"));
				dto.setOfficerType(rs.getString("epd_officer_type_code"));
				dTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dTOList;
	}

	@Override
	public boolean insertNewLetter(String subRefNo, LetterMaintenanceDTO newLetter, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt_check = null;
		PreparedStatement stmt2 = null;
		ResultSet rs_check = null;
		Long pld_seq = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String zoneDate = null;
		String schoolDate = null;
		String policeDate = null;

		try {
			SimpleDateFormat newDf = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			zoneDate = newDf.format(oldDf.parse(newLetter.getZoneIssueDate()));
			schoolDate = newDf.format(oldDf.parse(newLetter.getSchoolLetterDate()));
			policeDate = newDf.format(oldDf.parse(newLetter.getPoliceLetterDate()));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			conn = ConnectionManager.getConnection();

			String query_check = "SELECT pld_seq FROM public.nt_t_project_letter_det WHERE pld_sub_reference_no=?;";

			stmt_check = conn.prepareStatement(query_check);
			stmt_check.setString(1, subRefNo);
			rs_check = stmt_check.executeQuery();

			if (rs_check.next())
				pld_seq = rs_check.getLong(1);

			if (pld_seq == null) {
				long seq = Utils.getNextValBySeqName(conn, "seq_nt_t_project_letter_det");

				String query = "INSERT INTO public.nt_t_project_letter_det "
						+ "(pld_seq, pld_sub_reference_no, pld_zone_letter_issue_date, pld_school_name, pld_school_letter_date, pld_police_name, pld_police_letter_date, pld_created_by, pld_created_date) "
						+ "VALUES(?,?,?,?,?,?,?,?,?);";

				stmt = conn.prepareStatement(query);
				stmt.setLong(1, seq);
				stmt.setString(2, subRefNo);
				stmt.setString(3, zoneDate);
				stmt.setString(4, newLetter.getSchoolName());
				stmt.setString(5, schoolDate);
				stmt.setString(6, newLetter.getPoliceName());
				stmt.setString(7, policeDate);
				stmt.setString(8, user);
				stmt.setTimestamp(9, timestamp);
				stmt.executeUpdate();

			} else {

				String query = "UPDATE public.nt_t_project_letter_det "
						+ "SET pld_zone_letter_issue_date=?, pld_school_name=?, pld_school_letter_date=?, pld_police_name=?, pld_police_letter_date=?, pld_modified_by=?, pld_modified_date=? "
						+ "WHERE pld_seq=?;";

				stmt = conn.prepareStatement(query);
				stmt.setString(1, zoneDate);
				stmt.setString(2, newLetter.getSchoolName());
				stmt.setString(3, schoolDate);
				stmt.setString(4, newLetter.getPoliceName());
				stmt.setString(5, policeDate);
				stmt.setString(6, user);
				stmt.setTimestamp(7, timestamp);
				stmt.setLong(8, newLetter.getPld_seq());
				stmt.executeUpdate();

			}

			// update program year
			String query2 = "UPDATE public.nt_t_awareness_program_det SET adp_year=?, adp_modified_by=?, adp_modified_date =? WHERE apd_sub_reference_no=?;";
			stmt2 = conn.prepareStatement(query2);
			stmt2.setString(1, newLetter.getYear());
			stmt2.setString(2, user);
			stmt2.setTimestamp(3, timestamp);
			stmt2.setString(4, subRefNo);
			stmt2.executeUpdate();

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public boolean updateLetter(LetterMaintenanceDTO selectedLetter, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String zoneDate = null;
		String schoolDate = null;
		String policeDate = null;

		try {
			SimpleDateFormat newDf = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			zoneDate = newDf.format(oldDf.parse(selectedLetter.getZoneIssueDate()));
			schoolDate = newDf.format(oldDf.parse(selectedLetter.getSchoolLetterDate()));
			policeDate = newDf.format(oldDf.parse(selectedLetter.getPoliceLetterDate()));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			conn = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_project_letter_det "
					+ "SET pld_zone_letter_issue_date=?, pld_school_name=?, pld_school_letter_date=?, pld_police_name=?, pld_police_letter_date=?, pld_modified_by=?, pld_modified_date=? "
					+ "WHERE pld_seq=?;";

			stmt = conn.prepareStatement(query);
			stmt.setString(1, zoneDate);
			stmt.setString(2, selectedLetter.getSchoolName());
			stmt.setString(3, schoolDate);
			stmt.setString(4, selectedLetter.getPoliceName());
			stmt.setString(5, policeDate);
			stmt.setString(6, user);
			stmt.setTimestamp(7, timestamp);
			stmt.setLong(8, selectedLetter.getPld_seq());
			stmt.executeUpdate();

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public boolean removeLetter(long seq) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_project_letter_det WHERE pld_seq=?;";

			stmt = conn.prepareStatement(query);
			stmt.setLong(1, seq);
			stmt.executeUpdate();
			conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return true;
	}

	@Override
	public List<EmployeeDTO> getAllEmployee() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<EmployeeDTO> employeeList = new ArrayList<EmployeeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_m_employee WHERE emp_status = 'A' ORDER BY emp_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				EmployeeDTO employeeDTO = new EmployeeDTO();
				employeeDTO.setEmpNo(rs.getString("emp_no"));
				employeeDTO.setEmpEpfNo(rs.getString("emp_epf_no"));
				employeeDTO.setFullName(rs.getString("emp_fullname"));
				employeeDTO.setNicNo(rs.getString("emp_nic_no"));
				employeeDTO.setUserId(rs.getString("emp_user_id"));
				employeeList.add(employeeDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return employeeList;
	}

	@Override
	public boolean insertNewParticipant(String subRefNo, EventParticipationDTO newParticipant, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			conn = ConnectionManager.getConnection();

			long seq = Utils.getNextValBySeqName(conn, "seq_nt_t_event_participation_det");

			String query = "INSERT INTO public.nt_t_event_participation_det "
					+ "(epd_seq, epd_sub_reference_no, epd_participant_type, epd_emp_no, epd_epf_no, epd_id_no, epd_name, epd_officer_type_code, epd_created_by, epd_created_date) "
					+ " VALUES(?,?,?,?,?,?,?,?,?,?);";

			stmt = conn.prepareStatement(query);
			stmt.setLong(1, seq);
			stmt.setString(2, subRefNo);
			stmt.setString(3, newParticipant.getTypeOfParticipation());
			stmt.setString(4, newParticipant.getEmpNo());
			stmt.setString(5, newParticipant.getEpfNo());
			stmt.setString(6, newParticipant.getIdNo());
			stmt.setString(7, newParticipant.getParticipantName());
			stmt.setString(8, newParticipant.getOfficerType());
			stmt.setString(9, user);
			stmt.setTimestamp(10, timestamp);
			stmt.executeUpdate();

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public boolean updateParticipant(EventParticipationDTO participant, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			conn = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_event_participation_det "
					+ " SET epd_participant_type=?, epd_emp_no=?, epd_epf_no=?, epd_id_no=?, epd_name=?, epd_officer_type_code=?, epd_modified_by=?, epd_modified_date=? "
					+ " WHERE epd_seq= ?";

			stmt = conn.prepareStatement(query);
			stmt.setString(1, participant.getTypeOfParticipation());
			stmt.setString(2, participant.getEmpNo());
			stmt.setString(3, participant.getEpfNo());
			stmt.setString(4, participant.getIdNo());
			stmt.setString(5, participant.getParticipantName());
			stmt.setString(6, participant.getOfficerType());
			stmt.setString(7, user);
			stmt.setTimestamp(8, timestamp);
			stmt.setLong(9, participant.getEpd_seq());
			stmt.executeUpdate();

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public boolean removeParticipant(long seq) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = ConnectionManager.getConnection();

			String query = "DELETE FROM  public.nt_t_event_participation_det WHERE epd_seq = ?;";

			stmt = conn.prepareStatement(query);
			stmt.setLong(1, seq);
			stmt.executeUpdate();
			conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return true;
	}

	@Override
	public boolean updateEventDetails(ProgramDTO selectedProgram, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt_check = null;
		ResultSet rs_check = null;
		Long sequence = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			conn = ConnectionManager.getConnection();

			String query_check = "SELECT evm_seq FROM public.nt_m_event_management WHERE evm_sub_reference_no=?;";

			stmt_check = conn.prepareStatement(query_check);
			stmt_check.setString(1, selectedProgram.getSubRefNo());
			rs_check = stmt_check.executeQuery();

			if (rs_check.next())
				sequence = rs_check.getLong(1);

			if (sequence == null) {
				long seq = Utils.getNextValBySeqName(conn, "seq_nt_m_event_management");

				String query = "INSERT INTO public.nt_m_event_management "
						+ " (evm_seq, evm_sub_reference_no, evm_event_date, evm_school_name, evm_no_of_students, evm_existing_budget, evm_actual_budget, evm_remarks, evm_created_by, evm_created_date) "
						+ " VALUES(?,?,?,?,?,?,?,?,?,?)";

				stmt = conn.prepareStatement(query);
				stmt.setLong(1, seq);
				stmt.setString(2, selectedProgram.getSubRefNo());
				stmt.setString(3, selectedProgram.getScheduleDate());
				stmt.setString(4, selectedProgram.getSchoolName());
				stmt.setInt(5, selectedProgram.getNoOfStudents());
				stmt.setBigDecimal(6, selectedProgram.getEstimatedBudjet());
				stmt.setBigDecimal(7, selectedProgram.getActualBudjet());
				stmt.setString(8, selectedProgram.getRemarks());
				stmt.setString(9, user);
				stmt.setTimestamp(10, timestamp);
				stmt.executeUpdate();

			} else {

				String query = "UPDATE public.nt_m_event_management "
						+ " SET evm_event_date=?, evm_school_name=?, evm_no_of_students=?, evm_existing_budget=?, evm_actual_budget=?, evm_remarks=?, evm_modified_by=?, evm_modified_date=? "
						+ " WHERE evm_seq=?;";

				stmt = conn.prepareStatement(query);
				stmt.setString(1, selectedProgram.getScheduleDate());
				stmt.setString(2, selectedProgram.getSchoolName());
				stmt.setInt(3, selectedProgram.getNoOfStudents());
				stmt.setBigDecimal(4, selectedProgram.getEstimatedBudjet());
				stmt.setBigDecimal(5, selectedProgram.getActualBudjet());
				stmt.setString(6, selectedProgram.getRemarks());
				stmt.setString(7, user);
				stmt.setTimestamp(8, timestamp);
				stmt.setLong(9, sequence);
				stmt.executeUpdate();

			}

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public List<TransportDTO> getTransportByProgram(String subRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TransportDTO> dTOList = new ArrayList<TransportDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_event_transport_det WHERE etd_sub_reference_no=? ORDER BY etd_vehicle_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, subRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TransportDTO dto = new TransportDTO();
				dto.setEtd_seq(rs.getLong("etd_seq"));
				dto.setVehicleNo(rs.getString("etd_vehicle_no"));
				dto.setTransportDate(rs.getString("etd_transport_date"));
				dto.setDriverName(rs.getString("etd_driver_name"));
				dto.setKm(rs.getString("etd_km"));
				dTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dTOList;
	}

	@Override
	public boolean insertNewTransport(String subRefNo, TransportDTO newTransport, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String transDate = null;

		try {
			SimpleDateFormat newDf = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			transDate = newDf.format(oldDf.parse(newTransport.getTransportDate()));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			transDate = newTransport.getTransportDate();
		}

		try {
			conn = ConnectionManager.getConnection();

			long seq = Utils.getNextValBySeqName(conn, "seq_nt_t_event_transport_det");

			String query = "INSERT INTO public.nt_t_event_transport_det "
					+ " (etd_seq, etd_sub_reference_no, etd_vehicle_no, etd_driver_name, etd_transport_date, etd_km, etd_created_by, etd_created_date) "
					+ " VALUES(?,?,?,?,?,?,?,?);";

			stmt = conn.prepareStatement(query);
			stmt.setLong(1, seq);
			stmt.setString(2, subRefNo);
			stmt.setString(3, newTransport.getVehicleNo().toUpperCase());
			stmt.setString(4, newTransport.getDriverName());
			stmt.setString(5, transDate);
			stmt.setBigDecimal(6,
					(newTransport.getKm() == null || newTransport.getKm().trim().isEmpty()) ? new BigDecimal(0)
							: new BigDecimal(newTransport.getKm()));
			stmt.setString(7, user);
			stmt.setTimestamp(8, timestamp);
			stmt.executeUpdate();

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public boolean updateTransport(TransportDTO selectedTransport, String user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String transDate = null;

		try {
			SimpleDateFormat newDf = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			transDate = newDf.format(oldDf.parse(selectedTransport.getTransportDate()));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			transDate = selectedTransport.getTransportDate();
		}

		try {
			conn = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_event_transport_det  "
					+ " SET etd_vehicle_no=?, etd_driver_name=?, etd_transport_date=?, etd_km=?, etd_modified_by=?, etd_modified_date=? "
					+ " WHERE etd_seq=?;";

			stmt = conn.prepareStatement(query);
			stmt.setString(1, selectedTransport.getVehicleNo().toUpperCase());
			stmt.setString(2, selectedTransport.getDriverName());
			stmt.setString(3, transDate);
			stmt.setBigDecimal(4,
					(selectedTransport.getKm() == null || selectedTransport.getKm().trim().isEmpty())
							? new BigDecimal(0)
							: new BigDecimal(selectedTransport.getKm()));
			stmt.setString(5, user);
			stmt.setTimestamp(6, timestamp);
			stmt.setLong(7, selectedTransport.getEtd_seq());
			stmt.executeUpdate();

			conn.commit();
			output = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return output;
	}

	@Override
	public boolean removeTransport(Long etd_seq) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = ConnectionManager.getConnection();

			String query = "DELETE FROM  public.nt_t_event_transport_det WHERE etd_seq = ?;";

			stmt = conn.prepareStatement(query);
			stmt.setLong(1, etd_seq);
			stmt.executeUpdate();
			conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return true;
	}

	@Override
	public List<LetterMaintenanceDTO> getLettersByProject(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<LetterMaintenanceDTO> dtoList = new ArrayList<LetterMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_awareness_program_det a, nt_t_project_letter_det l "
					+ "WHERE a.apd_prj_reference_no = ? AND a.apd_sub_reference_no = l.pld_sub_reference_no ORDER BY apd_prj_reference_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				LetterMaintenanceDTO dto = new LetterMaintenanceDTO();
				dto.setPld_seq(rs.getLong("pld_seq"));
				dto.setZoneIssueDate(rs.getString("pld_zone_letter_issue_date"));
				dto.setSchoolName(rs.getString("pld_school_name"));
				dto.setSchoolLetterDate(rs.getString("pld_school_letter_date"));
				dto.setPoliceName(rs.getString("pld_police_name"));
				dto.setPoliceLetterDate(rs.getString("pld_police_letter_date"));
				dto.setSubRefNo(rs.getString("apd_sub_reference_no"));
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dtoList;
	}

	@Override
	public List<TransportDTO> getTransportByProject(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TransportDTO> dtoList = new ArrayList<TransportDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_awareness_program_det a, nt_t_event_transport_det t "
					+ " WHERE t.etd_sub_reference_no IN (SELECT apd_sub_reference_no FROM nt_t_awareness_program_det WHERE apd_prj_reference_no = ?)"
					+ " AND t.etd_sub_reference_no = a.apd_sub_reference_no ORDER BY apd_sub_reference_no";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TransportDTO dto = new TransportDTO();
				dto.setEtd_seq(rs.getLong("etd_seq"));
				dto.setVehicleNo(rs.getString("etd_vehicle_no"));
				dto.setTransportDate(rs.getString("etd_transport_date"));
				dto.setDriverName(rs.getString("etd_driver_name"));
				dto.setKm(rs.getString("etd_km"));

				dto.setSubRefNo(rs.getString("apd_sub_reference_no"));
				dto.setPlace(rs.getString("adp_place"));
				dto.setScheduleDate(rs.getString("adp_schedule_date"));
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dtoList;
	}

}