package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import lk.informatics.ntc.model.dto.PrintInspectionDTO;
import lk.informatics.ntc.model.dto.ProceedIncompleteApplicationDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class IncompleteApprovalServiceImpl implements IncompleteApprovalService {
	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger("IncompleteApprovalServiceImpl");

	@Override
	public List<PrintInspectionDTO> getAllApplicationNoListForIncompleteApproval() {
		logger.info("getAllApplicationNoListForIncompleteApproval start");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT DISTINCT a.pm_application_no FROM nt_t_pm_application a WHERE a.pm_status NOT IN ('I', 'H','G') ORDER BY a.pm_application_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setApplicationNo(rs.getString("pm_application_no"));
				returnList.add(printInspectionDTO);
			}

		} catch (Exception e) {
			logger.info("getAllApplicationNoListForIncompleteApproval error");
			logger.info(e);
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getAllApplicationNoListForIncompleteApproval end");
		return returnList;
	}

	@Override
	public List<PrintInspectionDTO> getAllVehicleNoListForIncompleteApproval() {
		logger.info("getAllVehicleNoListForIncompleteApproval start");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct a.pm_vehicle_regno from nt_t_pm_application a where a.pm_status not in ('I', 'H','G') ORDER BY a.pm_vehicle_regno;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				returnList.add(printInspectionDTO);
			}

		} catch (Exception e) {
			logger.info("getAllVehicleNoListForIncompleteApproval error");
			logger.info(e);
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getAllVehicleNoListForIncompleteApproval end");
		return returnList;
	}

	@Override
	public List<PrintInspectionDTO> searchApplications(String currentApplicationNo, String currentVehicleNo,
			String selectedInspectionStatus) {
		logger.info("searchApplications start");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PrintInspectionDTO> returnList = new ArrayList<PrintInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			if ((currentApplicationNo == null || currentApplicationNo.trim().equals(""))
					&& (currentVehicleNo == null || currentVehicleNo.trim().equals(""))) {
				String query = "select distinct c.pm_application_no as app_no,c.pm_vehicle_regno as bus_reg_no,b.pmo_permit_no as permitno,b.pmo_full_name as ownername,c.inspection_type as inspection_type "
						+ "from public.nt_t_pm_application c inner join public.nt_t_pm_vehi_owner b on b.pmo_application_no = c.pm_application_no where c.pm_status NOT IN ('I', 'H') and c.pm_inspection_status =? order by app_no ;";

				ps = con.prepareStatement(query);
				ps.setString(1, selectedInspectionStatus);
			} else if (currentApplicationNo == null || currentApplicationNo.trim().equals("")) {
				String query = "select distinct c.pm_application_no as app_no,c.pm_vehicle_regno as bus_reg_no,b.pmo_permit_no as permitno,b.pmo_full_name as ownername,c.inspection_type as inspection_type "
						+ "from public.nt_t_pm_application c inner join public.nt_t_pm_vehi_owner b on b.pmo_application_no = c.pm_application_no where c.pm_vehicle_regno =? and c.pm_inspection_status =? order by app_no ;";

				ps = con.prepareStatement(query);
				ps.setString(1, currentVehicleNo);
				ps.setString(2, selectedInspectionStatus);
			} else {
				String query = "select distinct c.pm_application_no as app_no,c.pm_vehicle_regno as bus_reg_no,b.pmo_permit_no as permitno,b.pmo_full_name as ownername,c.inspection_type as inspection_type "
						+ "from public.nt_t_pm_application c inner join public.nt_t_pm_vehi_owner b on b.pmo_application_no = c.pm_application_no where c.pm_application_no =? and c.pm_vehicle_regno =? and c.pm_inspection_status =? order by app_no ;";

				ps = con.prepareStatement(query);
				ps.setString(1, currentApplicationNo);
				ps.setString(2, currentVehicleNo);
				ps.setString(3, selectedInspectionStatus);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
				printInspectionDTO.setOwnerApplicationNo(rs.getString("app_no"));
				printInspectionDTO.setOwnerVehicleNo(rs.getString("bus_reg_no"));
				printInspectionDTO.setOwnerPermitNo(rs.getString("permitno"));
				printInspectionDTO.setOwner(rs.getString("ownername"));
				printInspectionDTO.setInspectionType(rs.getString("inspection_type"));
				returnList.add(printInspectionDTO);
			}

		} catch (Exception e) {
			logger.info("searchApplications error");
			logger.info(e);
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("searchApplications end");
		return returnList;
	}

	@Override
	public PrintInspectionDTO getCurrentVehicleNo(String selectedApplicationNo) {
		logger.info("getCurrentVehicleNo start");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
		try {
			con = ConnectionManager.getConnection();
			String query = "select a.pm_vehicle_regno as vehi_num from public.nt_t_pm_application a where a.pm_application_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			rs = ps.executeQuery();
			while (rs.next()) {
				printInspectionDTO.setVehicleNo(rs.getString("vehi_num"));
			}
		} catch (Exception e) {
			logger.info("getCurrentVehicleNo error");
			logger.info(e);
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getCurrentVehicleNo end");
		return printInspectionDTO;
	}

	@Override
	public boolean proceedIncompleteApplication(ProceedIncompleteApplicationDTO dto) {
		logger.info("proceedIncompleteApplication start");
		boolean returnVal = false;

		Connection con = null;
		try {
			con = ConnectionManager.getConnection();

			insertCurrentRecordToHistory(dto, con);
			updateCurrentApplication(dto, con);
			insertTaskHistory(dto, con);

			con.commit();
			returnVal = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("proceedIncompleteApplication error");
			logger.error(e);
		} finally {
			ConnectionManager.close(con);
		}
		logger.info("proceedIncompleteApplication end");
		return returnVal;
	}

	private boolean insertCurrentRecordToHistory(ProceedIncompleteApplicationDTO dto, Connection con) throws Exception {
		logger.info("insertCurrentRecordToHistory start");
		boolean returnVal = false;

		PreparedStatement ps = null;
		String sql = "INSERT into public.nt_h_pm_application_new (SELECT * FROM public.nt_t_pm_application WHERE pm_application_no =?)";

		ps = con.prepareStatement(sql);
		ps.setString(1, dto.getApplicationNo());
		int success = ps.executeUpdate();

		if (success > 0) {
			returnVal = true;
		}
		ConnectionManager.close(ps);

		logger.info("insertCurrentRecordToHistory end");
		return returnVal;
	}

	private boolean updateCurrentApplication(ProceedIncompleteApplicationDTO dto, Connection con) throws Exception {
		logger.info("updateCurrentApplication start");
		boolean returnVal = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		PreparedStatement ps = null;
		String sql = "UPDATE nt_t_pm_application SET proceed_status ='Y', proceed_remark =?, proceed_given_by =?, proceed_given_date =? WHERE pm_application_no =?;";

		ps = con.prepareStatement(sql);
		ps.setString(1, dto.getProceedRemark());
		ps.setString(2, dto.getLoginUser());
		ps.setTimestamp(3, timestamp);
		ps.setString(4, dto.getApplicationNo());
		int success = ps.executeUpdate();

		if (success > 0) {
			returnVal = true;
		}

		ConnectionManager.close(ps);

		logger.info("updateCurrentApplication end");
		return returnVal;
	}

	private boolean insertTaskHistory(ProceedIncompleteApplicationDTO dto, Connection con) throws Exception {
		logger.info("insertTaskHistory start");
		boolean returnVal = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		// find the sequence number of the application from tasks table
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		Long seq = null;

		String sql1 = "SELECT tsd_seq FROM public.nt_t_task_det WHERE tsd_app_no=?;";
		ps1 = con.prepareStatement(sql1);
		ps1.setString(1, dto.getApplicationNo());
		rs = ps1.executeQuery();

		while (rs.next()) {
			seq = rs.getLong("tsd_seq");
		}

		ConnectionManager.close(rs);
		ConnectionManager.close(ps1);
		
		String taskCode = null;
		if (dto.getInspectionType().equals("PI") || dto.getInspectionType().equals("AI")
				|| dto.getInspectionType().equals("CI") || dto.getInspectionType().equals("II")
				|| dto.getInspectionType().equals("SI")) {
			taskCode = dto.getInspectionType() + "100"; // PI100 | AI100 | CI100 | II100 | SI100
		}

		// insert in to history table
		PreparedStatement ps2 = null;

		String sql2 = "INSERT INTO public.nt_h_task_his (tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
				+ "VALUES (?,?,?,?,?,?,?);";

		ps2 = con.prepareStatement(sql2);
		ps2.setLong(1, seq);
		ps2.setString(2, dto.getVehicleNo());
		ps2.setString(3, dto.getApplicationNo());
		ps2.setString(4, taskCode);
		ps2.setString(5, "C");
		ps2.setString(6, dto.getLoginUser());
		ps2.setTimestamp(7, timestamp);
		int success = ps2.executeUpdate();

		if (success > 0) {
			returnVal = true;
		}

		ConnectionManager.close(ps2);

		logger.info("insertTaskHistory end");
		return returnVal;
	}

	@Override
	public boolean enableProceedBtn(String selectedApplicationNo) {
		logger.info("enableProceedBtn start");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean returnVal = false;

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT seq, pm_permit_no, pm_vehicle_regno FROM public.nt_t_pm_application "
					+ "WHERE pm_application_no=? and pm_inspection_status='I' and proceed_status='N';";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				returnVal = true;
			}

		} catch (Exception e) {
			logger.info("enableProceedBtn error");
			logger.info(e);
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("enableProceedBtn end");
		return returnVal;
	}

}
