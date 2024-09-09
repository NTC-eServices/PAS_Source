package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import lk.informatics.ntc.model.dto.ActionPointsManagementDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PrintInspectionDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.view.beans.SessionBackingBean;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class InspectionActionPointServiceImpl implements InspectionActionPointService {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@Override
	public boolean checkInspection(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isAvailable = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "select DISTINCT tsd_app_no FROM public.nt_t_task_det "
					+ "where (tsd_task_code='PM101' or tsd_task_code='PM100') and tsd_status='C' and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isAvailable = true;
			} else {
				isAvailable = false;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isAvailable;
	}

	@Override
	public List<ActionPointsManagementDTO> getSectionList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActionPointsManagementDTO> returnList = new ArrayList<ActionPointsManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_sections WHERE active = 'A' order by description;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();

				actionPointsManagementDTO.setSectionCode(rs.getString("code"));
				actionPointsManagementDTO.setSectionDescription(rs.getString("description"));
				returnList.add(actionPointsManagementDTO);

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
	public ActionPointsManagementDTO getDescriptionForSectionCode(String selectedSection) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_sections where code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedSection);
			rs = ps.executeQuery();

			while (rs.next()) {
				actionPointsManagementDTO.setSectionDescription(rs.getString("description"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return actionPointsManagementDTO;
	}

	@Override
	public List<ActionPointsManagementDTO> getStatusList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActionPointsManagementDTO> returnList = new ArrayList<ActionPointsManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_rec_status WHERE active = 'A' order by description;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();

				actionPointsManagementDTO.setStatusCode(rs.getString("code"));
				actionPointsManagementDTO.setStatusDescription(rs.getString("description"));
				returnList.add(actionPointsManagementDTO);

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
	public ActionPointsManagementDTO getDescriptionForStatusCode(String selectedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  description FROM public.nt_r_rec_status where code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedStatus);
			rs = ps.executeQuery();

			while (rs.next()) {
				actionPointsManagementDTO.setStatusDescription(rs.getString("description"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return actionPointsManagementDTO;
	}

	@Override
	public int insertNewSeqNo(ActionPointsManagementDTO dto) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_r_inspec_act_points");
			String sql = "INSERT INTO public.nt_r_inspec_act_points(iap_seq, rou_code, rou_description, rou_section_code, active, created_by, created_date) VALUES(?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, dto.getSequence());
			stmt.setString(3, dto.getDescription());
			stmt.setString(4, dto.getSectionCode());
			stmt.setString(5, dto.getStatusCode());
			stmt.setString(6, dto.getCreatedBy());
			stmt.setTimestamp(7, timestamp);

			stmt.executeUpdate();
			dto.setSeq(seqNo);

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
	public int updateSeqRecord(ActionPointsManagementDTO actionPointsManagementDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_r_inspec_act_points SET  rou_description=?,active=?, modified_by=?, modified_date=? WHERE iap_seq=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, actionPointsManagementDTO.getDescription());
			stmt.setString(2, actionPointsManagementDTO.getStatusCode());
			stmt.setString(3, actionPointsManagementDTO.getModifyBy());
			stmt.setTimestamp(4, timestamp);
			stmt.setLong(5, actionPointsManagementDTO.getSeq());

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
	public ActionPointsManagementDTO getGeneratedSeqNo(String sequence) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT iap_seq FROM public.nt_r_inspec_act_points where rou_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, sequence);
			rs = ps.executeQuery();

			while (rs.next()) {
				actionPointsManagementDTO.setSeq(rs.getLong("iap_seq"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return actionPointsManagementDTO;
	}

	@Override
	public List<ActionPointsManagementDTO> getAllSequenceCodeList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActionPointsManagementDTO> returnList = new ArrayList<ActionPointsManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  rou_code FROM public.nt_r_inspec_act_points order by rou_code;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
				actionPointsManagementDTO.setGeneratedSeqCode(rs.getString("rou_code"));
				returnList.add(actionPointsManagementDTO);

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
	public boolean checkAsigned(String sequence, Long seq) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isSeqCodeAssigned = false;
		ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT   vid_sec_seq FROM public.nt_t_vehicle_inspec_det where vid_sec_seq=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, sequence);
			rs = stmt.executeQuery();

			while (rs.next()) {
				actionPointsManagementDTO.setVidSecSequence(rs.getString("vid_sec_seq"));
			}

			if (actionPointsManagementDTO.getVidSecSequence().isEmpty()) {
				isSeqCodeAssigned = false;
			} else {
				isSeqCodeAssigned = true;

			}

		} catch (Exception e) {

			isSeqCodeAssigned = false;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isSeqCodeAssigned;
	}

	@Override
	public int deleteSequenceRecord(Long seq) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM public.nt_r_inspec_act_points WHERE iap_seq=?;";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, seq);

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
	public List<ActionPointsManagementDTO> viewAllRecords() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActionPointsManagementDTO> returnList = new ArrayList<ActionPointsManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.iap_seq as ipseq, a.rou_code as seqcode, a.rou_description as seqdescription, a.rou_section_code as sectioncode, a.active as statuscode, b.description as sectionDes, c.description as statusDes  FROM public.nt_r_inspec_act_points a,public.nt_r_sections b,nt_r_rec_status c  where a.active='A' and a.rou_section_code=b.code and a.active=c.code order by a.rou_section_code,a.rou_code ;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
				actionPointsManagementDTO.setSeq(rs.getLong("ipseq"));
				actionPointsManagementDTO.setSequence(rs.getString("seqcode"));
				actionPointsManagementDTO.setDescription(rs.getString("seqdescription"));
				actionPointsManagementDTO.setSectionCode(rs.getString("sectioncode"));
				actionPointsManagementDTO.setStatusCode(rs.getString("statuscode"));
				actionPointsManagementDTO.setSectionDescription(rs.getString("sectionDes"));
				actionPointsManagementDTO.setStatusDescription(rs.getString("statusDes"));
				returnList.add(actionPointsManagementDTO);

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
	public List<ActionPointsManagementDTO> getSearchedRecords(String serachedSeqCode, String searchedSection,
			String searchedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActionPointsManagementDTO> returnList = new ArrayList<ActionPointsManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.iap_seq as ipseq, a.rou_code as seqcode, a.rou_description as seqdescription, a.rou_section_code as sectioncode, a.active as statuscode, b.description as sectionDes, c.description as statusDes FROM public.nt_r_inspec_act_points a,public.nt_r_sections b,nt_r_rec_status c where a.rou_section_code=b.code and a.active=c.code and (a.active=? or a.rou_code=? or a.rou_section_code=?) order by a.rou_section_code,a.rou_code;";

			ps = con.prepareStatement(query);

			ps.setString(1, searchedStatus);
			ps.setString(2, serachedSeqCode);
			ps.setString(3, searchedSection);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
				actionPointsManagementDTO.setSeq(rs.getLong("ipseq"));
				actionPointsManagementDTO.setSequence(rs.getString("seqcode"));
				actionPointsManagementDTO.setDescription(rs.getString("seqdescription"));
				actionPointsManagementDTO.setSectionCode(rs.getString("sectioncode"));
				actionPointsManagementDTO.setStatusCode(rs.getString("statuscode"));
				actionPointsManagementDTO.setSectionDescription(rs.getString("sectionDes"));
				actionPointsManagementDTO.setStatusDescription(rs.getString("statusDes"));
				returnList.add(actionPointsManagementDTO);

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
	public List<ActionPointsManagementDTO> getSearchedRecordsOne(String serachedSeqCode, String searchedSection) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActionPointsManagementDTO> returnList = new ArrayList<ActionPointsManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.iap_seq as ipseq, a.rou_code as seqcode, a.rou_description as seqdescription, a.rou_section_code as sectioncode, a.active as statuscode, b.description as sectionDes, c.description as statusDes FROM public.nt_r_inspec_act_points a,public.nt_r_sections b,nt_r_rec_status c where a.rou_section_code=b.code and a.active=c.code and (a.rou_code=? and a.rou_section_code=?) order by a.rou_section_code,a.rou_code;";

			ps = con.prepareStatement(query);

			ps.setString(1, serachedSeqCode);
			ps.setString(2, searchedSection);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
				actionPointsManagementDTO.setSeq(rs.getLong("ipseq"));
				actionPointsManagementDTO.setSequence(rs.getString("seqcode"));
				actionPointsManagementDTO.setDescription(rs.getString("seqdescription"));
				actionPointsManagementDTO.setSectionCode(rs.getString("sectioncode"));
				actionPointsManagementDTO.setStatusCode(rs.getString("statuscode"));
				actionPointsManagementDTO.setSectionDescription(rs.getString("sectionDes"));
				actionPointsManagementDTO.setStatusDescription(rs.getString("statusDes"));
				returnList.add(actionPointsManagementDTO);

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
	public List<ActionPointsManagementDTO> getSearchedRecordsTwo(String serachedSeqCode, String searchedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActionPointsManagementDTO> returnList = new ArrayList<ActionPointsManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.iap_seq as ipseq, a.rou_code as seqcode, a.rou_description as seqdescription, a.rou_section_code as sectioncode, a.active as statuscode, b.description as sectionDes, c.description as statusDes FROM public.nt_r_inspec_act_points a,public.nt_r_sections b,nt_r_rec_status c where a.rou_section_code=b.code and a.active=c.code and (a.rou_code=? and a.active=?) order by a.rou_section_code,a.rou_code;";

			ps = con.prepareStatement(query);

			ps.setString(1, serachedSeqCode);
			ps.setString(2, searchedStatus);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
				actionPointsManagementDTO.setSeq(rs.getLong("ipseq"));
				actionPointsManagementDTO.setSequence(rs.getString("seqcode"));
				actionPointsManagementDTO.setDescription(rs.getString("seqdescription"));
				actionPointsManagementDTO.setSectionCode(rs.getString("sectioncode"));
				actionPointsManagementDTO.setStatusCode(rs.getString("statuscode"));
				actionPointsManagementDTO.setSectionDescription(rs.getString("sectionDes"));
				actionPointsManagementDTO.setStatusDescription(rs.getString("statusDes"));
				returnList.add(actionPointsManagementDTO);
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
	public List<ActionPointsManagementDTO> getSearchedRecordsThree(String searchedSection, String searchedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActionPointsManagementDTO> returnList = new ArrayList<ActionPointsManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.iap_seq as ipseq, a.rou_code as seqcode, a.rou_description as seqdescription, a.rou_section_code as sectioncode, a.active as statuscode, b.description as sectionDes, c.description as statusDes FROM public.nt_r_inspec_act_points a,public.nt_r_sections b,nt_r_rec_status c where a.rou_section_code=b.code and a.active=c.code and (a.rou_section_code=? and a.active=?) order by a.rou_section_code,a.rou_code;";

			ps = con.prepareStatement(query);

			ps.setString(1, searchedSection);
			ps.setString(2, searchedStatus);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
				actionPointsManagementDTO.setSeq(rs.getLong("ipseq"));
				actionPointsManagementDTO.setSequence(rs.getString("seqcode"));
				actionPointsManagementDTO.setDescription(rs.getString("seqdescription"));
				actionPointsManagementDTO.setSectionCode(rs.getString("sectioncode"));
				actionPointsManagementDTO.setStatusCode(rs.getString("statuscode"));
				actionPointsManagementDTO.setSectionDescription(rs.getString("sectionDes"));
				actionPointsManagementDTO.setStatusDescription(rs.getString("statusDes"));
				returnList.add(actionPointsManagementDTO);

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
	public List<ActionPointsManagementDTO> getSearchedRecordsFour(String serachedSeqCode, String searchedSection,
			String searchedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActionPointsManagementDTO> returnList = new ArrayList<ActionPointsManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.iap_seq as ipseq, a.rou_code as seqcode, a.rou_description as seqdescription, a.rou_section_code as sectioncode, a.active as statuscode, b.description as sectionDes, c.description as statusDes FROM public.nt_r_inspec_act_points a,public.nt_r_sections b,nt_r_rec_status c where a.rou_section_code=b.code and a.active=c.code and (a.rou_code=? and a.rou_section_code=? and a.active=?) order by a.rou_section_code,a.rou_code;";

			ps = con.prepareStatement(query);
			ps.setString(1, serachedSeqCode);
			ps.setString(2, searchedSection);
			ps.setString(3, searchedStatus);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
				actionPointsManagementDTO.setSeq(rs.getLong("ipseq"));
				actionPointsManagementDTO.setSequence(rs.getString("seqcode"));
				actionPointsManagementDTO.setDescription(rs.getString("seqdescription"));
				actionPointsManagementDTO.setSectionCode(rs.getString("sectioncode"));
				actionPointsManagementDTO.setStatusCode(rs.getString("statuscode"));
				actionPointsManagementDTO.setSectionDescription(rs.getString("sectionDes"));
				actionPointsManagementDTO.setStatusDescription(rs.getString("statusDes"));
				returnList.add(actionPointsManagementDTO);
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
	public List<PrintInspectionDTO> getAllApplicationNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT tsd_app_no  FROM public.nt_t_task_det  where (tsd_task_code not in ('PM100','PM101') ) or (tsd_task_code = 'PM101' and tsd_status='C') order by tsd_app_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setApplicationNo(rs.getString("tsd_app_no"));
				returnList.add(printInspectionDTO);

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
	public List<PrintInspectionDTO> getAllRecords(String currentApplicationNo, String currentVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct a.tsd_app_no as taskapplicationno, a.tsd_vehicle_no as taskvehicleno, a.tsd_status as taskstatus, b.pmo_permit_no as permitno, b.pmo_full_name as ownername FROM public.nt_t_task_det a,public.nt_t_pm_vehi_owner b where (a.tsd_app_no=? or a.tsd_vehicle_no=? ) and (a.tsd_task_code not in ('PM100') or a.tsd_task_code ='PM101' and a.tsd_status='O') and a.tsd_app_no=b.pmo_application_no and tsd_vehicle_no is not null and tsd_vehicle_no !='';";
			ps = con.prepareStatement(query);
			ps.setString(1, currentApplicationNo);
			ps.setString(2, currentVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setOwnerApplicationNo(rs.getString("taskapplicationno"));
				printInspectionDTO.setOwnerTaskStatus(rs.getString("taskstatus"));
				printInspectionDTO.setOwnerPermitNo(rs.getString("permitno"));
				printInspectionDTO.setOwner(rs.getString("ownername"));
				printInspectionDTO.setOwnerVehicleNo(rs.getString("taskvehicleno"));
				returnList.add(printInspectionDTO);

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
	public List<PrintInspectionDTO> getAllRecordsForBoth(String selectedApplicationNo, String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.tsd_seq as taskseq, a.tsd_vehicle_no as taskvehicleno, a.tsd_app_no as taskapplicationno, a.tsd_task_code as taskcode, a.tsd_status as taskstatus,b.pmo_permit_no as permitno,b.pmo_full_name as ownername FROM public.nt_t_task_det a,public.nt_t_pm_vehi_owner b where (a.tsd_app_no=? and a.tsd_vehicle_no=?) and a.tsd_task_code='PM101' and a.tsd_status='C' and a.tsd_app_no=b.pmo_application_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			ps.setString(2, selectedVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setOwnerSeq(rs.getString("taskseq"));
				printInspectionDTO.setOwnerApplicationNo(rs.getString("taskapplicationno"));
				printInspectionDTO.setOwnerTaskCode(rs.getString("taskcode"));
				printInspectionDTO.setOwnerTaskStatus(rs.getString("taskstatus"));
				printInspectionDTO.setOwnerPermitNo(rs.getString("permitno"));
				printInspectionDTO.setOwner(rs.getString("ownername"));
				printInspectionDTO.setOwnerVehicleNo(rs.getString("taskvehicleno"));
				returnList.add(printInspectionDTO);

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
	public PrintInspectionDTO getDetails(String selectedApplicationNo, String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tsd_vehicle_no, tsd_app_no FROM public.nt_t_task_det where tsd_task_code='PM101' and tsd_status='C' and (tsd_vehicle_no=? or tsd_app_no=?);";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			ps.setString(2, selectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				printInspectionDTO.setApplicationNo(rs.getString("tsd_app_no"));
				printInspectionDTO.setVehicleNo(rs.getString("tsd_vehicle_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return printInspectionDTO;
	}

	@Override
	public List<PrintInspectionDTO> getAllApplicationNoListForViewInspection() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			// ----Update New Query bcz remove inactive application no. in drop down menu
			String query = "SELECT DISTINCT a.pm_application_no as app_no FROM public.nt_t_pm_application a, public.nt_t_task_det  b where (b.tsd_task_code='PM101'and b.tsd_status='O'  or b.tsd_task_code='PM100' and b.tsd_status='C' or b.tsd_task_code='PM101'and b.tsd_status='C'  or b.tsd_task_code='PR200' and b.tsd_status='O') and a.pm_application_no=b.tsd_app_no and a.pm_status in('A','P','O') order by a.pm_application_no;";
			ps = con.prepareStatement(query);
			// ps.setString(1, LangId);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setApplicationNo(rs.getString("app_no"));
				returnList.add(printInspectionDTO);

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
	public List<PrintInspectionDTO> getAllRecordsForViewInspections(String currentApplicationNo,
			String currentVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select c.pm_application_no as app_no, c.pm_vehicle_regno as bus_reg_no,a.tsd_status as taskstatus,b.pmo_permit_no as permitno,b.pmo_full_name as ownername FROM public.nt_t_task_det a,public.nt_t_pm_vehi_owner b ,  public.nt_t_pm_application c where (c.pm_application_no=? or c.pm_vehicle_regno=?) and ((a.tsd_task_code in ('PM101','PM100') and a.tsd_status in('C','O') ) or (tsd_task_code='PR200' and tsd_status='O')) and a.tsd_app_no=b.pmo_application_no  and a.tsd_app_no=c.pm_application_no ;";

			ps = con.prepareStatement(query);
			ps.setString(1, currentApplicationNo);
			ps.setString(2, currentVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setOwnerApplicationNo(rs.getString("taskapplicationno"));
				printInspectionDTO.setOwnerVehicleNo(rs.getString("taskvehicleno"));
				printInspectionDTO.setOwnerTaskStatus(rs.getString("taskstatus"));
				printInspectionDTO.setOwnerPermitNo(rs.getString("permitno"));
				printInspectionDTO.setOwner(rs.getString("ownername"));
				returnList.add(printInspectionDTO);
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
	public PrintInspectionDTO getDetailsForViewInspectionDetails(String selectedApplicationNo,
			String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tsd_vehicle_no, tsd_app_no FROM public.nt_t_task_det where (tsd_task_code='PM101' and tsd_status='O' or tsd_task_code='PM100' and tsd_status='C' or tsd_task_code='PM101'and tsd_status='C') and (tsd_vehicle_no=? or tsd_app_no=?);";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			ps.setString(2, selectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				printInspectionDTO.setApplicationNo(rs.getString("tsd_app_no"));
				printInspectionDTO.setVehicleNo(rs.getString("tsd_vehicle_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return printInspectionDTO;
	}

	@Override
	public List<PrintInspectionDTO> getAllRecordsForBothViewInspection(String selectedApplicationNo,
			String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select c.pm_application_no as app_no, c.pm_vehicle_regno as bus_reg_no,a.tsd_status as taskstatus,b.pmo_permit_no as permitno,b.pmo_full_name as ownername FROM public.nt_t_task_det a,public.nt_t_pm_vehi_owner b ,  public.nt_t_pm_application c where (c.pm_application_no=? and c.pm_vehicle_regno=?) and ((a.tsd_task_code in ('PM101','PM100') and a.tsd_status in('C','O') ) or (tsd_task_code='PR200' and tsd_status='O')) and a.tsd_app_no=b.pmo_application_no and a.tsd_app_no=c.pm_application_no ;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			ps.setString(2, selectedVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setOwnerApplicationNo(rs.getString("app_no"));
				printInspectionDTO.setOwnerVehicleNo(rs.getString("bus_reg_no"));
				printInspectionDTO.setOwnerTaskStatus(rs.getString("taskstatus"));
				printInspectionDTO.setOwnerPermitNo(rs.getString("permitno"));
				printInspectionDTO.setOwner(rs.getString("ownername"));
				returnList.add(printInspectionDTO);
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
	public VehicleInspectionDTO getRecordForCurrentApplicationNo(String selectedApplicationNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		VehicleInspectionDTO vehicleInspectionDTO = new VehicleInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pm_inspection_status , a.inspection_type ,a.inspection_location ,a.pm_route_flag as pm_route_flag,a.seq as permitseq, a.pm_application_no as applicationno, a.pm_queue_no as queueno, a.pm_permit_no as permitno, a.pm_vehicle_regno as vehicleno, a.pm_service_type as servicetypecode, a.pm_route_no as routecode,"
					+ "b.rou_description as routedescription, c.description as servicetypedescription,d.pmo_full_name as permiownername,"
					+ "d.pmo_nic as ownernic,e.pmb_chassis_no as chassisno, e.pmb_make as makecode, e.pmb_model as modelcode, e.pmb_production_date as manufacterdate,f.description as makedescription,g.mod_description as modeldescription, a.pm_next_ins_date_sec1_2 as dateone, a.pm_next_ins_date_sec3 as datetwo,a.pm_inspect_remark as finalremarkdes, a.pm_inspect_date as inspecteddate, a.pm_created_by as inspectedby FROM public.nt_t_pm_application a,public.nt_r_route b,public.nt_r_service_types c,public.nt_t_pm_vehi_owner d,public.nt_t_pm_omini_bus1 e,public.nt_r_make f,public.nt_r_model g where a.pm_route_no=b.rou_number and a.pm_service_type=c.code and a.pm_application_no=d.pmo_application_no and a.pm_application_no=e.pmb_application_no and e.pmb_make=f.code  and g.mod_code=e.pmb_model and a.pm_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNumber);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleInspectionDTO.setRouteFlag(rs.getString("pm_route_flag"));
				vehicleInspectionDTO.setPermitSeqNo(rs.getLong("permitseq"));
				vehicleInspectionDTO.setApplicationNo(rs.getString("applicationno"));
				vehicleInspectionDTO.setQueueNo(rs.getString("queueno"));
				vehicleInspectionDTO.setPermitNo(rs.getString("permitno"));
				vehicleInspectionDTO.setVehicleNo(rs.getString("vehicleno"));
				vehicleInspectionDTO.setServiceTypeCode(rs.getString("servicetypecode"));
				vehicleInspectionDTO.setRouteDetails(rs.getString("routedescription"));
				vehicleInspectionDTO.setRouteNo(rs.getString("routecode"));
				vehicleInspectionDTO.setServiceType(rs.getString("servicetypedescription"));
				vehicleInspectionDTO.setPermitOwner(rs.getString("permiownername"));
				vehicleInspectionDTO.setNicreg(rs.getString("ownernic"));
				vehicleInspectionDTO.setChassisNo(rs.getString("chassisno"));
				vehicleInspectionDTO.setMakeTypeCode(rs.getString("makecode"));
				vehicleInspectionDTO.setModelTypeCode(rs.getString("modelcode"));
				vehicleInspectionDTO.setProductDate(rs.getString("manufacterdate"));
				vehicleInspectionDTO.setMake(rs.getString("makedescription"));
				vehicleInspectionDTO.setModel(rs.getString("modeldescription"));
				vehicleInspectionDTO.setDate1(rs.getString("dateone"));
				vehicleInspectionDTO.setDate2(rs.getString("datetwo"));
				vehicleInspectionDTO.setFinalremarkDescription(rs.getString("finalremarkdes"));

				vehicleInspectionDTO.setInspectionTypeCode(rs.getString("inspection_type"));
				String type = rs.getString("inspection_type");

				if (type.equals("AI")) {
					vehicleInspectionDTO.setInspectionTypeDes("AMENDMENT");
				} else if (type.equals("PI")) {
					vehicleInspectionDTO.setInspectionTypeDes("RENEWAL");
				}

				String inspectionStatus = rs.getString("pm_inspection_status");
				if (inspectionStatus != null) {
					vehicleInspectionDTO.setInspectionStatusCode(inspectionStatus);
				} else {
					vehicleInspectionDTO.setInspectionStatusCode("I");
				}

				String inspectionLocation = rs.getString("inspection_location");
				if (inspectionLocation != null) {
					vehicleInspectionDTO.setInspecLocationCode(inspectionLocation);
				} else {
					vehicleInspectionDTO.setInspecLocationCode("NTC");
				}
				
				vehicleInspectionDTO.setInspectedDate(rs.getString("inspecteddate") == null? "N/A": rs.getString("inspecteddate"));
				vehicleInspectionDTO.setInspectedBy(rs.getString("inspectedby") == null? "N/A": rs.getString("inspectedby"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return vehicleInspectionDTO;
	}

	@Override
	public List<VehicleInspectionDTO> getAllInspectionRecordsDetails(String selectedApplicationNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select c.vid_sec_seq as vid_sec_seq, b.iap_seq as actionpointseq ,a.description as sectiondescription,b.rou_code as actionpointrouseq, b.rou_description as actionpointdescription,  c.vid_remark as inspectionremark, c.vid_exist as existvalue from public.nt_r_sections a, public.nt_r_inspec_act_points b ,public.nt_t_vehicle_inspec_det c where a.code = b.rou_section_code and c.vid_sec_seq=b.iap_seq  and c.vid_app_no=? order by(a.code,b.rou_code);";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNumber);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleInspectionDTO = new VehicleInspectionDTO();
				vehicleInspectionDTO.setGridno(rs.getString("actionpointseq"));
				vehicleInspectionDTO.setNewGridNo(rs.getInt("vid_sec_seq"));
				vehicleInspectionDTO.setActionPointSeqNo(rs.getString("actionpointrouseq"));
				vehicleInspectionDTO.setSection(rs.getString("sectiondescription"));
				vehicleInspectionDTO.setDescription(rs.getString("actionpointdescription"));
				vehicleInspectionDTO.setRemark(rs.getString("inspectionremark"));
				vehicleInspectionDTO.setExists(rs.getBoolean("existvalue"));
				returnList.add(vehicleInspectionDTO);
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
	public List<ActionPointsManagementDTO> getAllDesccritionsForCurrentSectionCode(String selectedSection) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ActionPointsManagementDTO> returnList = new ArrayList<ActionPointsManagementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rou_description FROM public.nt_r_inspec_act_points where rou_section_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedSection);
			rs = ps.executeQuery();

			while (rs.next()) {
				ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
				actionPointsManagementDTO.setCheckingSectionDescription(rs.getString("rou_description"));
				returnList.add(actionPointsManagementDTO);
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
	public ActionPointsManagementDTO getCurrentSectionAndStatus(String serachedSeqCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT   rou_section_code, active FROM public.nt_r_inspec_act_points where rou_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, serachedSeqCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				actionPointsManagementDTO.setSectionCode(rs.getString("rou_section_code"));
				actionPointsManagementDTO.setStatusCode(rs.getString("active"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return actionPointsManagementDTO;
	}

	@Override
	public List<PrintInspectionDTO> getAllVehicleNoListForViewInspection() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			// ----Update New Query bcz remove inactive application no in drop down menu
			String query = "SELECT a.pm_vehicle_regno as vehicle_no FROM public.nt_t_pm_application a, public.nt_t_task_det  b where (b.tsd_task_code='PM101'and b.tsd_status='O'  or b.tsd_task_code='PM100' and b.tsd_status='C' or b.tsd_task_code='PM101'and b.tsd_status='C'  or b.tsd_task_code='PR200' and b.tsd_status='O') and a.pm_application_no=b.tsd_app_no and a.pm_status in('A','P','O') order by a.pm_vehicle_regno;";
			ps = con.prepareStatement(query);
			// ps.setString(1, LangId);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setVehicleNo(rs.getString("vehicle_no"));
				returnList.add(printInspectionDTO);

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
	public List<PrintInspectionDTO> getAllRecordsForViewInspectionsDefault() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			// ----Update New Query bcz remove inactive application no. in drop down menu
			String query = "SELECT  c.pm_application_no as app_no, c.pm_vehicle_regno as bus_reg_no, a.tsd_status as taskstatus, b.pmo_permit_no as permitno,b.pmo_full_name as ownername  FROM public.nt_t_pm_application c, public.nt_t_pm_vehi_owner b , public.nt_t_task_det a where  ((a.tsd_task_code in ('PM101','PM100') and a.tsd_status in('C','O') ) or (tsd_task_code='PR200' and tsd_status='O'))  and a.tsd_app_no=b.pmo_application_no  and a.tsd_app_no=c.pm_application_no and pm_status in ('A','P','O') order by c.pm_application_no ;";

			ps = con.prepareStatement(query);
//			ps.setString(1, currentApplicationNo);
			// ps.setString(2, currentVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setOwnerApplicationNo(rs.getString("app_no"));
				printInspectionDTO.setOwnerVehicleNo(rs.getString("bus_reg_no"));
				printInspectionDTO.setOwnerTaskStatus(rs.getString("taskstatus"));
				printInspectionDTO.setOwnerPermitNo(rs.getString("permitno"));
				printInspectionDTO.setOwner(rs.getString("ownername"));

				returnList.add(printInspectionDTO);

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
	public PrintInspectionDTO getCurrentVehicleNo(String selectedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			// ---combine with application table and get vehicle no. for selected app no
			// with checking task codes
			String query = "SELECT  a.pm_vehicle_regno  as vehi_num  FROM public.nt_t_task_det b  , public.nt_t_pm_application a where (b.tsd_task_code='PM101' and  b.tsd_status='O' or b.tsd_task_code='PM100' and b.tsd_status='C' or b.tsd_task_code='PM101'and b.tsd_status='C'  or b.tsd_task_code='PR200' and b.tsd_status='O') and  a.pm_application_no=? and a.pm_application_no=b.tsd_app_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				printInspectionDTO.setVehicleNo(rs.getString("vehi_num"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return printInspectionDTO;
	}

	@Override
	public PrintInspectionDTO getCurrentApplicationNo(String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

//			String query = "SELECT   tsd_app_no FROM public.nt_t_task_det where (tsd_task_code='PM101' or tsd_task_code='PM100') and tsd_status='C' and (tsd_vehicle_no=? );";
			String query = "SELECT   tsd_app_no FROM public.nt_t_task_det  where (tsd_task_code='PM101'and tsd_status='O'  or tsd_task_code='PM100' and tsd_status='C' or tsd_task_code='PM101'and tsd_status='C'  or tsd_task_code='PR200' and tsd_status='O') and (tsd_vehicle_no=? );";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				printInspectionDTO.setApplicationNo(rs.getString("tsd_app_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return printInspectionDTO;
	}

	@Override
	public List<PrintInspectionDTO> getAllVehicleNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT tsd_vehicle_no  FROM public.nt_t_task_det  where (tsd_task_code not in ('PM100','PM101') ) or (tsd_task_code = 'PM101' and tsd_status='C')  and tsd_vehicle_no is not null and tsd_vehicle_no != '' order by tsd_vehicle_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setVehicleNo(rs.getString("tsd_vehicle_no"));
				returnList.add(printInspectionDTO);

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
	public PrintInspectionDTO getCurrentApplicationNoPrint(String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT   tsd_app_no FROM public.nt_t_task_det where (tsd_task_code='PM101') and tsd_status='C' and (tsd_vehicle_no=? );";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				printInspectionDTO.setApplicationNo(rs.getString("tsd_app_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return printInspectionDTO;
	}

	@Override
	public PrintInspectionDTO getCurrentVehicleNoPrint(String selectedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tsd_vehicle_no FROM public.nt_t_task_det where (tsd_task_code='PM101' ) and tsd_status='C' and ( tsd_app_no=?);";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				printInspectionDTO.setVehicleNo(rs.getString("tsd_vehicle_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return printInspectionDTO;
	}

	@Override
	public List<VehicleInspectionDTO> getRouteNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  rou_number FROM public.nt_r_route  order by rou_number;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleInspectionDTO = new VehicleInspectionDTO();
				vehicleInspectionDTO.setRouteNo(rs.getString("rou_number"));
				returnList.add(vehicleInspectionDTO);

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
	public List<VehicleInspectionDTO> getServiceTypesList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_service_types where active='A' order by description;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleInspectionDTO = new VehicleInspectionDTO();
				vehicleInspectionDTO.setServiceTypeCode(rs.getString("code"));
				vehicleInspectionDTO.setServiceTypeDescription(rs.getString("description"));
				returnList.add(vehicleInspectionDTO);

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
	public String getServiceTypeDes(String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String code = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code FROM public.nt_r_service_types where description=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				code = rs.getString("code");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return code;
	}

	@Override
	public int updateInspectionRecord(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO, String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String date3 = null;
		String date1_2 = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_application SET pm_service_type=?, pm_route_no=?, pm_next_ins_date_sec1_2=?, pm_next_ins_date_sec3=?, pm_inspect_remark=?, pm_modified_by=?, pm_modified_date=? WHERE pm_application_no=? and pm_permit_no=? and pm_vehicle_regno=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, vehicleDTO.getServiceTypeCode());
			stmt.setString(2, vehicleDTO.getRouteNo());

			if (vehicleDTO.getCalender1() != null) {

				date1_2 = sdf.format(vehicleDTO.getCalender1());
			}

			if (vehicleDTO.getCalender2() != null) {

				date3 = sdf.format(vehicleDTO.getCalender2());
			}
			if (date1_2 != null && !date1_2.trim().equalsIgnoreCase("") && !date1_2.isEmpty()) {
				stmt.setString(3, date1_2);
			} else {
				stmt.setNull(3, java.sql.Types.VARCHAR);
			}
			if (date3 != null && !date3.trim().equalsIgnoreCase("") && !date3.isEmpty()) {
				stmt.setString(4, date3);
			} else {
				stmt.setNull(4, java.sql.Types.VARCHAR);
			}

			stmt.setString(5, vehicleDTO.getFinalremarkDescription());
			stmt.setString(6, loginUser);
			stmt.setTimestamp(7, timestamp);
			stmt.setString(8, vehicleDTO.getApplicationNo());
			stmt.setString(9, vehicleDTO.getPermitNo());
			stmt.setString(10, vehicleDTO.getVehicleNo());

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
	public int updatePermitOwnerInspectionRecord(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO,
			String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_vehi_owner SET  pmo_full_name=?, pmo_nic=?, pm_modified_by=?, pm_modified_date=? WHERE pmo_application_no=? and pmo_permit_no=? and pmo_vehicle_regno=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, vehicleDTO.getPermitOwner());
			stmt.setString(2, vehicleDTO.getNicreg());
			stmt.setString(3, loginUser);
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, vehicleDTO.getApplicationNo());
			stmt.setString(6, vehicleDTO.getPermitNo());
			stmt.setString(7, vehicleDTO.getVehicleNo());

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
	public int updateOminiBusDetInspectionRecord(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO,
			String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_omini_bus1 SET  pmb_chassis_no=?, pmb_production_date=?,  pmb_modified_by=?, pmb_modified_date=? WHERE pmb_application_no=? and pmb_permit_no=? and pmb_vehicle_regno=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, vehicleDTO.getChassisNo());
			stmt.setString(2, vehicleDTO.getProductDate());
			stmt.setString(3, loginUser);
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, vehicleDTO.getApplicationNo());
			stmt.setString(6, vehicleDTO.getPermitNo());
			stmt.setString(7, vehicleDTO.getVehicleNo());

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
	public boolean saveDataVehicleInspecDetails(VehicleInspectionDTO vehicleDTO, List<VehicleInspectionDTO> dataList,
			String applicationNo, String loginUser) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDataSave = false;
		String exitTest = null;

		try {
			con = ConnectionManager.getConnection();

			for (int i = 0; i < dataList.size(); i++) {

				String sql = "UPDATE public.nt_t_vehicle_inspec_det SET   vid_remark=?, vid_exist=?, vid_modified_by=?, vid_modified_date=? WHERE vid_sec_seq=? and vid_app_no=?;";
				stmt = con.prepareStatement(sql);

				if (dataList.get(i).getRemark() != null) {
					stmt.setString(1, dataList.get(i).getRemark().trim());
				} else {

					stmt.setNull(1, java.sql.Types.VARCHAR);
				}

				if (dataList.get(i).isExists() == true) {
					exitTest = "YES";
				} else {
					exitTest = "NO";
				}
				stmt.setString(2, exitTest);
				stmt.setString(3, loginUser);
				stmt.setTimestamp(4, timestamp);

				int gridVal = Integer.parseInt(dataList.get(i).getGridno());
				stmt.setInt(5, gridVal);
				stmt.setString(6, applicationNo);

				int c = stmt.executeUpdate();

				if (c > 0) {
					isDataSave = true;

				} else {
					isDataSave = false;

				}

			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return isDataSave;
	}

	@Override
	public VehicleInspectionDTO getAssignedFunction(String logedUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		VehicleInspectionDTO vehicleInspectionDTO = new VehicleInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select nt_r_fun_role_activity.fra_function_code, nt_r_fun_role_activity.fra_activity_code "
					+ "from nt_t_granted_user_role inner join nt_r_fun_role_activity on "
					+ "nt_r_fun_role_activity.fra_role_code = nt_t_granted_user_role.gur_role_code "
					+ "where  fra_function_code='FN2_5' and gur_user_id =? and nt_t_granted_user_role.gur_status='A' ;";

			ps = con.prepareStatement(query);
			ps.setString(1, logedUser);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleInspectionDTO.setActivityCode(rs.getString("fra_activity_code"));
				vehicleInspectionDTO.setFunctionCode(rs.getString("fra_function_code"));

				// check edit btn need to disable or not
				if (vehicleInspectionDTO.getActivityCode().equals("E")) {
					vehicleInspectionDTO.setDisabledEditBtnInGrid(false);
					break;
				} else {
					vehicleInspectionDTO.setDisabledEditBtnInGrid(true);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return vehicleInspectionDTO;
	}

	@Override
	public VehicleInspectionDTO getDetailsWithModiFyDate(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		VehicleInspectionDTO vehicleInspectionDTO = new VehicleInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_permit_no, pm_vehicle_regno FROM public.nt_t_pm_application where pm_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleInspectionDTO.setPermitNo(rs.getString("pm_permit_no"));
				vehicleInspectionDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return vehicleInspectionDTO;
	}

	@Override
	public int insertNewTaskDetWithAppDet(VehicleInspectionDTO taskDetWithAppDetDTO, String loginUser,
			String applicationNo, String taskCode, String taskStatus) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det_with_application_det");

			String sql = "INSERT INTO public.nt_t_task_det_with_application_det (tsd_appd_seq, tsd_appd_application_no, tsd_appd_vehicle_no, tsd_appd_permit_no, tsd_appd_task_code, tsd_appd_task_status, tsd_appd_modified_by, tsd_appd_modified_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?);	";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, seqNo);
			stmt.setString(2, applicationNo);
			stmt.setString(3, taskDetWithAppDetDTO.getVehicleNo());
			stmt.setString(4, taskDetWithAppDetDTO.getPermitNo());
			stmt.setString(5, taskCode);
			stmt.setString(6, taskStatus);
			stmt.setString(7, loginUser);
			stmt.setTimestamp(8, timestamp);

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
	public boolean isCheckedSameTaskCodeForSelectedApp(String applicationNo, String taskCode, String taskStatus) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isAvailable = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tsd_appd_application_no,tsd_appd_task_code, tsd_appd_task_status FROM public.nt_t_task_det_with_application_det where tsd_appd_application_no=? and tsd_appd_task_code=? and tsd_appd_task_status=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			ps.setString(2, taskCode);
			ps.setString(3, taskStatus);
			rs = ps.executeQuery();

			if (rs.next()) {
				isAvailable = true;
			} else {
				isAvailable = false;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isAvailable;
	}

	@Override
	public int updateNewTaskDetWithAppDet(VehicleInspectionDTO taskDetWithAppDetDTO, String loginUser,
			String applicationNo, String taskCode, String taskStatus) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_task_det_with_application_det SET   tsd_appd_vehicle_no=?, tsd_appd_permit_no=?,  tsd_appd_modified_by=?, tsd_appd_modified_date=? where tsd_appd_application_no=? and tsd_appd_task_code=? and tsd_appd_task_status=?; ";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, taskDetWithAppDetDTO.getVehicleNo());
			stmt.setString(2, taskDetWithAppDetDTO.getPermitNo());
			stmt.setString(3, loginUser);
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, applicationNo);
			stmt.setString(6, taskCode);
			stmt.setString(7, taskStatus);
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
	public boolean isCheckedTaskCodeInTaskDetCompleted(String applicationNo, String taskCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isAvailable = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tsd_task_code, tsd_status FROM public.nt_t_task_det where tsd_app_no=? and tsd_task_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			ps.setString(2, taskCode);

			rs = ps.executeQuery();

			while (rs.next()) {
				String currentTaskCode = rs.getString("tsd_task_code");
				String currentTaskStatus = rs.getString("tsd_status");
				if (currentTaskCode.equals(taskCode)) {
					if (currentTaskStatus.equals("O")) {
						isAvailable = false;
					} else if (currentTaskStatus.equals("C")) {
						isAvailable = true;
					}
				} else {

				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isAvailable;
	}

	@Override
	public int updateOminiBusDetInspectionRecordWithApplicationNo(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO,
			String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_omini_bus1 SET  pmb_chassis_no=?, pmb_production_date=?,  pmb_modified_by=?, pmb_modified_date=?, pmb_make=?, pmb_model=?  WHERE pmb_application_no=? and pmb_permit_no=? and pmb_vehicle_regno=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, vehicleDTO.getChassisNo());
			stmt.setString(2, vehicleDTO.getProductDate());
			stmt.setString(3, loginUser);
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, vehicleDTO.getMakeTypeCode());
			stmt.setString(6, vehicleDTO.getModelTypeCode());
			stmt.setString(7, vehicleDTO.getApplicationNo());
			stmt.setString(8, vehicleDTO.getPermitNo());
			stmt.setString(9, vehicleDTO.getVehicleNo());

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
	public List<PrintInspectionDTO> getAllApplicationNoListForViewInspectionNormal() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT a.pm_application_no as app_no FROM public.nt_t_pm_application a, public.nt_t_task_det  b "
					+ " where (b.tsd_task_code='PM101'and b.tsd_status='O' "
					+ "or b.tsd_task_code='PM100' and b.tsd_status='C'"
					+ "or b.tsd_task_code='PM101'and b.tsd_status='C' "
					+ "or b.tsd_task_code='PR200' and b.tsd_status='O')"
					+ "and a.pm_application_no=b.tsd_app_no and a.pm_status in('A','P','O') "
					+ "and a.inspection_type in ('AI','PI','NI')" + "order by a.pm_application_no;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setApplicationNo(rs.getString("app_no"));

				returnList.add(printInspectionDTO);

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
	public List<PrintInspectionDTO> getAllVehicleNoListForViewInspectionNew() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pm_vehicle_regno as vehicle_no FROM public.nt_t_pm_application a, public.nt_t_task_det  b where (b.tsd_task_code='PM101'and b.tsd_status='O'  or b.tsd_task_code='PM100' and b.tsd_status='C' or b.tsd_task_code='PM101'and b.tsd_status='C'  or b.tsd_task_code='PR200' and b.tsd_status='O') and a.pm_application_no=b.tsd_app_no and a.pm_status in('A','P','O') and a.inspection_type in ('AI','PI','NI') order by a.pm_vehicle_regno;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setVehicleNo(rs.getString("vehicle_no"));

				returnList.add(printInspectionDTO);

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
	public List<PrintInspectionDTO> getAllRecordsForViewInspectionsNew(String currentApplicationNo,
			String currentVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select c.pm_application_no as app_no, c.pm_vehicle_regno as bus_reg_no,a.tsd_status as taskstatus,b.pmo_permit_no as permitno,b.pmo_full_name as ownername FROM public.nt_t_task_det a,public.nt_t_pm_vehi_owner b ,  public.nt_t_pm_application c where (c.pm_application_no=? or c.pm_vehicle_regno=?) and ((a.tsd_task_code in ('PM101','PM100') and a.tsd_status in('C','O') ) or (tsd_task_code='PR200' and tsd_status='O')) and a.tsd_app_no=b.pmo_application_no  and a.tsd_app_no=c.pm_application_no and a.inspection_type in ('AI','PI') ;";

			ps = con.prepareStatement(query);
			ps.setString(1, currentApplicationNo);
			ps.setString(2, currentVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();

				printInspectionDTO.setOwnerApplicationNo(rs.getString("taskapplicationno"));
				printInspectionDTO.setOwnerVehicleNo(rs.getString("taskvehicleno"));

				printInspectionDTO.setOwnerTaskStatus(rs.getString("taskstatus"));
				printInspectionDTO.setOwnerPermitNo(rs.getString("permitno"));
				printInspectionDTO.setOwner(rs.getString("ownername"));
				returnList.add(printInspectionDTO);
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
	public PrintInspectionDTO getDetailsForViewInspectionDetailsNew(String selectedApplicationNo,
			String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tsd_vehicle_no, tsd_app_no FROM public.nt_t_task_det where (tsd_task_code='PM101' and tsd_status='O' or tsd_task_code='PM100' and tsd_status='C' or tsd_task_code='PM101'and tsd_status='C') and (tsd_vehicle_no=? or tsd_app_no=?);";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			ps.setString(2, selectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				printInspectionDTO.setApplicationNo(rs.getString("tsd_app_no"));
				printInspectionDTO.setVehicleNo(rs.getString("tsd_vehicle_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return printInspectionDTO;
	}

	@Override
	public List<PrintInspectionDTO> getAllRecordsForBothNew(String selectedApplicationNo, String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select c.pm_application_no as app_no, c.pm_vehicle_regno as bus_reg_no,a.tsd_status as taskstatus,b.pmo_permit_no as permitno,b.pmo_full_name as ownername FROM public.nt_t_task_det a,public.nt_t_pm_vehi_owner b ,  public.nt_t_pm_application c where (c.pm_application_no=? and c.pm_vehicle_regno=?) and ((a.tsd_task_code in ('PM101','PM100') and a.tsd_status in('C','O') ) or (tsd_task_code='PR200' and tsd_status='O')) and a.tsd_app_no=b.pmo_application_no and a.tsd_app_no=c.pm_application_no and c.inspection_type in ('AI','PI','NI') ;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			ps.setString(2, selectedVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setOwnerApplicationNo(rs.getString("app_no"));
				printInspectionDTO.setOwnerVehicleNo(rs.getString("bus_reg_no"));
				printInspectionDTO.setOwnerTaskStatus(rs.getString("taskstatus"));
				printInspectionDTO.setOwnerPermitNo(rs.getString("permitno"));
				printInspectionDTO.setOwner(rs.getString("ownername"));
				returnList.add(printInspectionDTO);
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
	public List<PrintInspectionDTO> getAllRecordsForViewInspectionsDefaultNew() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  c.pm_application_no as app_no, c.pm_vehicle_regno as bus_reg_no, a.tsd_status as taskstatus, b.pmo_permit_no as permitno,b.pmo_full_name as ownername  FROM public.nt_t_pm_application c, public.nt_t_pm_vehi_owner b , public.nt_t_task_det a where  ((a.tsd_task_code in ('PM101','PM100') and a.tsd_status in('C','O') ) or (tsd_task_code='PR200' and tsd_status='O'))  and a.tsd_app_no=b.pmo_application_no  and a.tsd_app_no=c.pm_application_no and pm_status in ('A','P','O') and c.inspection_type in ('AI','PI') order by c.pm_application_no ;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setOwnerApplicationNo(rs.getString("app_no"));
				printInspectionDTO.setOwnerVehicleNo(rs.getString("bus_reg_no"));
				printInspectionDTO.setOwnerTaskStatus(rs.getString("taskstatus"));
				printInspectionDTO.setOwnerPermitNo(rs.getString("permitno"));
				printInspectionDTO.setOwner(rs.getString("ownername"));

				returnList.add(printInspectionDTO);

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
	public VehicleInspectionDTO getRecordForCurrentApplicationNoNew(String selectedApplicationNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		VehicleInspectionDTO vehicleInspectionDTO = new VehicleInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pm_inspection_status , a.inspection_type ,a.inspection_location , a.pm_route_flag as pm_route_flag,a.seq as permitseq, a.pm_application_no as applicationno, a.pm_queue_no as queueno, a.pm_permit_no as permitno, a.pm_vehicle_regno as vehicleno, a.pm_service_type as servicetypecode, a.pm_route_no as routecode,"
					+ "b.rou_description as routedescription, l.loc_description , c.description as servicetypedescription,d.pmo_full_name as permiownername,"
					+ "d.pmo_nic as ownernic,e.pmb_chassis_no as chassisno,e.pmb_engine_no as engineno, e.pmb_make as makecode, e.pmb_model as modelcode, e.pmb_production_date as manufacterdate,f.description as makedescription,g.mod_description as modeldescription, a.pm_next_ins_date_sec1_2 as dateone, a.pm_next_ins_date_sec3 as datetwo,a.pm_inspect_remark as finalremarkdes, a.proceed_remark as proceedremark,a.pm_tender_ref_no as tenderRef FROM public.nt_t_pm_application a,public.nt_r_route b,public.nt_r_service_types c,public.nt_t_pm_vehi_owner d,public.nt_t_pm_omini_bus1 e,public.nt_r_make f,public.nt_r_model g , nt_r_inspection_location l where a.inspection_location =l.loc_code  and a.pm_route_no=b.rou_number and a.pm_service_type=c.code and a.pm_application_no=d.pmo_application_no and a.pm_application_no=e.pmb_application_no and e.pmb_make=f.code  and g.mod_code=e.pmb_model and a.pm_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNumber);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleInspectionDTO.setInspectioncomplIncomplStatus(rs.getString("pm_inspection_status"));
				vehicleInspectionDTO.setInspectionTypeCode(rs.getString("inspection_type"));
				vehicleInspectionDTO.setInspecLocationCode(rs.getString("inspection_location"));
				vehicleInspectionDTO.setInspecLocationDes(rs.getString("loc_description"));
				vehicleInspectionDTO.setRouteFlag(rs.getString("pm_route_flag"));
				vehicleInspectionDTO.setPermitSeqNo(rs.getLong("permitseq"));
				vehicleInspectionDTO.setApplicationNo(rs.getString("applicationno"));
				vehicleInspectionDTO.setQueueNo(rs.getString("queueno"));
				vehicleInspectionDTO.setPermitNo(rs.getString("permitno"));
				vehicleInspectionDTO.setVehicleNo(rs.getString("vehicleno"));
				vehicleInspectionDTO.setServiceTypeCode(rs.getString("servicetypecode"));
				vehicleInspectionDTO.setRouteDetails(rs.getString("routedescription"));
				vehicleInspectionDTO.setRouteNo(rs.getString("routecode"));
				vehicleInspectionDTO.setServiceType(rs.getString("servicetypedescription"));
				vehicleInspectionDTO.setPermitOwner(rs.getString("permiownername"));
				vehicleInspectionDTO.setNicreg(rs.getString("ownernic"));
				vehicleInspectionDTO.setChassisNo(rs.getString("chassisno"));
				vehicleInspectionDTO.setEngineNo(rs.getString("engineno"));
				vehicleInspectionDTO.setMakeTypeCode(rs.getString("makecode"));
				vehicleInspectionDTO.setModelTypeCode(rs.getString("modelcode"));
				vehicleInspectionDTO.setProductDate(rs.getString("manufacterdate"));
				vehicleInspectionDTO.setMake(rs.getString("makedescription"));
				vehicleInspectionDTO.setModel(rs.getString("modeldescription"));
				vehicleInspectionDTO.setDate1(rs.getString("dateone"));
				vehicleInspectionDTO.setDate2(rs.getString("datetwo"));
				vehicleInspectionDTO.setFinalremarkDescription(rs.getString("finalremarkdes"));
				vehicleInspectionDTO.setProceedRemark(rs.getString("proceedremark"));
				vehicleInspectionDTO.setTenderRefNo(rs.getString("tenderRef"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return vehicleInspectionDTO;
	}

	@Override
	public int updateInspectionRecordNew(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO, String loginUser,
			String inspectionStatus, String inspectionType) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String date3 = null;
		String date1_2 = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_application SET proceed_status = ? , pm_inspection_status =?  ,inspection_location =? ,pm_service_type=?, pm_route_no=?, pm_next_ins_date_sec1_2=?, pm_next_ins_date_sec3=?, pm_inspect_remark=?, pm_modified_by=?, pm_modified_date=? WHERE pm_application_no=? and pm_permit_no=? and pm_vehicle_regno=?;";

			stmt = con.prepareStatement(sql);
			if (inspectionStatus.equals("C")) {
				stmt.setString(1, "Y");
			} else if (inspectionStatus.equals("I")) {
				stmt.setString(1, "N");
			}
			stmt.setString(2, inspectionStatus);
			stmt.setString(3, vehicleDTO.getInspecLocationCode());
			stmt.setString(4, vehicleDTO.getServiceTypeCode());
			stmt.setString(5, vehicleDTO.getRouteNo());

			if (vehicleDTO.getCalender1() != null) {

				date1_2 = sdf.format(vehicleDTO.getCalender1());
			}

			if (vehicleDTO.getCalender2() != null) {
				date3 = sdf.format(vehicleDTO.getCalender2());
			}
			if (date1_2 != null && !date1_2.trim().equalsIgnoreCase("") && !date1_2.isEmpty()) {
				stmt.setString(6, date1_2);
			} else {
				stmt.setNull(6, java.sql.Types.VARCHAR);
			}
			if (date3 != null && !date3.trim().equalsIgnoreCase("") && !date3.isEmpty()) {
				stmt.setString(7, date3);
			} else {
				stmt.setNull(7, java.sql.Types.VARCHAR);
			}

			stmt.setString(8, vehicleDTO.getFinalremarkDescription());
			stmt.setString(9, loginUser);
			stmt.setTimestamp(10, timestamp);
			stmt.setString(11, vehicleDTO.getApplicationNo());
			stmt.setString(12, vehicleDTO.getPermitNo());
			stmt.setString(13, vehicleDTO.getVehicleNo());

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
	public int updateOminiBusDetAmendmentInspectionRecord(VehicleInspectionDTO vehicleDTO, PermitDTO permitDTO,
			String loginUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_omini_bus1 SET pmb_chassis_no=?, pmb_engine_no=?, pmb_production_date=?, "
					+ "pmb_make=?, pmb_model=?, pmb_modified_by=?, pmb_modified_date=? WHERE pmb_application_no=? and pmb_permit_no=? and pmb_vehicle_regno=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, vehicleDTO.getChassisNo());
			stmt.setString(2, vehicleDTO.getEngineNo());
			stmt.setString(3, vehicleDTO.getProductDate());
			stmt.setString(4, vehicleDTO.getMakeTypeCode());
			stmt.setString(5, vehicleDTO.getModelTypeCode());
			stmt.setString(6, loginUser);
			stmt.setTimestamp(7, timestamp);
			stmt.setString(8, vehicleDTO.getApplicationNo());
			stmt.setString(9, vehicleDTO.getPermitNo());
			stmt.setString(10, vehicleDTO.getVehicleNo());

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

}
