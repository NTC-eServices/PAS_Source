package lk.informatics.ntc.model.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.UploadImageDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.roc.utils.common.Utils;

public class VehicleInspectionServiceImpl implements VehicleInspectionService {

	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger("VehicleInspectionServiceImpl");

	private CommonService commonService;
	private MigratedService migratedService;

	@Override
	public boolean CopyTaskDetailsANDinsertTaskHistory(VehicleInspectionDTO dto, String loginUSer, String taskCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		VehicleInspectionDTO inspectionDTO = null;

		boolean isUpdate = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date "
					+ "FROM public.nt_t_task_det " + "WHERE tsd_app_no=? AND tsd_task_code=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, dto.getApplicationNo());
			stmt.setString(2, taskCode);

			rs = stmt.executeQuery();

			if (rs.next()) {

				isUpdate = true;

				inspectionDTO = new VehicleInspectionDTO();
				inspectionDTO.setTaskSeq(rs.getLong("tsd_seq"));
				inspectionDTO.setVehicleNo(rs.getString("tsd_vehicle_no"));
				inspectionDTO.setApplicationNo(rs.getString("tsd_app_no"));
				inspectionDTO.setTaskCode(rs.getString("tsd_task_code"));
				inspectionDTO.setTaskStatus(rs.getString("tsd_status"));
				inspectionDTO.setCreateBy(rs.getString("created_by"));
				inspectionDTO.setCreateDate(rs.getTimestamp("created_date"));

				String sql2 = "INSERT INTO public.nt_h_task_his "
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(? , ?, ?, ?, ?, ?, ?); " + "";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, inspectionDTO.getTaskSeq());
				stmt2.setString(2, inspectionDTO.getVehicleNo());
				stmt2.setString(3, inspectionDTO.getApplicationNo());
				stmt2.setString(4, inspectionDTO.getTaskCode());
				stmt2.setString(5, inspectionDTO.getTaskStatus());
				stmt2.setString(6, inspectionDTO.getCreateBy());
				stmt2.setTimestamp(7, inspectionDTO.getCreateDate());

				stmt2.executeUpdate();

			} else {
				isUpdate = false;
			}
			migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
			String queueNumber = migratedService.findQueueNumberFromApplicationNo(inspectionDTO.getApplicationNo());
			migratedService.updateQueueNumberTaskInQueueMaster(queueNumber, inspectionDTO.getApplicationNo(),
					inspectionDTO.getTaskCode(), inspectionDTO.getTaskStatus());

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return isUpdate;
	}

	@Override
	public boolean updateTaskDetails(VehicleInspectionDTO dto, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_task_det " + "SET tsd_status=? "
					+ "WHERE tsd_app_no=? AND tsd_task_code=?; ";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, dto.getApplicationNo());
			ps.setString(3, taskCode);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public boolean deleteTaskDetails(VehicleInspectionDTO dto, String taskCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isTaskPM300Delete = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_task_det WHERE tsd_app_no=? AND tsd_task_code=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getApplicationNo());
			ps.setString(2, taskCode);
			int i = ps.executeUpdate();

			if (i > 0) {
				isTaskPM300Delete = true;
			} else {
				isTaskPM300Delete = false;
			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskPM300Delete;
	}

	@Override
	public boolean insertTaskDetails(VehicleInspectionDTO dto, String loginUSer, String taskCode, String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_t_task_det "
					+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, dto.getVehicleNo());
			stmt.setString(3, dto.getApplicationNo());
			stmt.setString(4, taskCode);
			stmt.setString(5, status);
			stmt.setString(6, loginUSer);
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
		return false;
	}

	@Override
	public boolean checkTaskDetails(VehicleInspectionDTO dto, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_t_task_det "
					+ "where tsd_task_code=? and tsd_status=? and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, status);
			ps.setString(3, dto.getApplicationNo());
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
			
		}
		return isTaskAvailable;
	}

	@Override
	public boolean checkQueueNo(String queueNo) {
		String query;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			query = "select pm_application_no " + "from nt_t_pm_application " + "where pm_queue_no='" + queueNo + "'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				return true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return false;

	}

	@Override
	public List<VehicleInspectionDTO> getActiveActionPointData() {
		List<VehicleInspectionDTO> dataList = new ArrayList<VehicleInspectionDTO>();
		Connection con = null;
		String query;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			query = "select  b.iap_seq,b.rou_code,  code, description, a.active, b.rou_description "
					+ "FROM public.nt_r_sections a "
					+ "inner join nt_r_inspec_act_points b on b.rou_section_code= a.code WHERE a.active='A' "
					+ "and  b.active ='A' order by (a.code,b.rou_code); ";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();
				VehicleDTO.setNewGridNo(rs.getInt("iap_seq"));
				VehicleDTO.setActionPointSeqNo(rs.getString("rou_code"));
				VehicleDTO.setSection((rs.getString("description")));
				VehicleDTO.setDescription((rs.getString("rou_description")));
				dataList.add(VehicleDTO);
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
	public String getVehicleNo(String queueNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		String vehicleNO = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select que_vehicle_no from nt_m_queue_master where que_number=? and que_date like '" + today
					+ "%'";

			ps = con.prepareStatement(query);
			ps.setString(1, queueNO);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNO = (rs.getString("que_vehicle_no"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNO;
	}

	@Override
	public VehicleInspectionDTO checkVehicleNo(String queueNo) {
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		VehicleInspectionDTO inspectionDTO = new VehicleInspectionDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "select pm_vehicle_regno from public.nt_t_pm_application where pm_vehicle_regno "
					+ "in (select que_vehicle_no from nt_m_queue_master where que_number=? and que_date like '" + today
					+ "%' )";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, queueNo);
			rs = stmt.executeQuery();

			while (rs.next()) {

				inspectionDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return inspectionDTO;
	}

	@Override
	public boolean applicationType(String queueNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select pm_queue_no, pm_is_backlog_app " + "from nt_t_pm_application "
					+ "where pm_queue_no='" + queueNo + "' and pm_is_backlog_app='N'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				return true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;

	}

	@Override
	public VehicleInspectionDTO getDatafromnext(String queryNo, boolean inspectionForAmendment) {
		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String today = df.format(date);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query;

			String backlogStatus = isbacklog(queryNo);

			if (inspectionForAmendment) {
				backlogStatus = "N";
			}

			if (backlogStatus != null && !backlogStatus.isEmpty() && !backlogStatus.trim().equalsIgnoreCase("")
					&& backlogStatus.equalsIgnoreCase("Y")) {
				query = "select distinct a.pm_isnew_permit,a.pm_inspect_remark,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type,"
						+ " b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
						+ " g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_valid_to,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit"
						+ " from nt_t_pm_application a , nt_m_queue_master d,nt_r_route b ,nt_r_service_types c, nt_t_pm_omini_bus1 e,nt_r_make f,nt_r_model g, nt_t_pm_vehi_owner h"
						+ " where a.pm_vehicle_regno = d.que_vehicle_no" + " and a.pm_route_no = b.rou_number "
						+ " and a.pm_service_type= c.code " + " and a.pm_application_no = e.pmb_application_no "
						+ " and e.pmb_make=f.code " + " and  e.pmb_model = g.mod_code "
						+ " and a.pm_application_no = h.pmo_application_no " + " and a.pm_status='A'"
						+ " and d.que_number= '" + queryNo + "'" + " and to_char(d.created_date,'dd-MM-yyyy')='" + today
						+ "'";
			} else {
				query = "select distinct  a.pm_isnew_permit,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_valid_to,a.pm_inspect_remark,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type, "
						+ " b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
						+ " g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit "
						+ " from public.nt_t_pm_application a left outer join public.nt_r_route b on a.pm_route_no = b.rou_number "
						+ " left outer join public.nt_r_service_types c on a.pm_service_type= c.code "
						+ " left outer join public.nt_m_queue_master d on a.pm_vehicle_regno = d.que_vehicle_no "
						+ " left outer join nt_t_pm_omini_bus1 e on a.pm_application_no = e.pmb_application_no "
						+ " left outer join nt_r_make f on e.pmb_make=f.code "
						+ " left outer join nt_r_model g on e.pmb_model = g.mod_code "
						+ " left outer join nt_t_pm_vehi_owner h on a.pm_application_no = h.pmo_application_no "
						+ " where d.que_number='" + queryNo + "' and to_char(d.created_date,'dd-MM-yyyy')= '" + today
						+ "'";

			}

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				VehicleDTO.setIsNewPermit(rs.getString("pm_isnew_permit"));
				VehicleDTO.setFinalRemark(rs.getString("pm_inspect_remark"));
				VehicleDTO.setInspectionStatus(rs.getString("pm_reinspec_status"));
				VehicleDTO.setDate1(rs.getString("pm_next_ins_date_sec1_2"));
				VehicleDTO.setDate2(rs.getString("pm_next_ins_date_sec3"));

				VehicleDTO.setRouteFlag(rs.getString("pm_route_flag"));

				VehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setOldApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setPermitSeqNo(rs.getLong("seq"));
				VehicleDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				VehicleDTO.setManyear(rs.getString("pmb_production_date"));
				VehicleDTO.setMakeDescription(rs.getString("make"));
				VehicleDTO.setMakeTypeCode(rs.getString("pmb_make"));
				VehicleDTO.setModelTypeCode(rs.getString("pmb_model"));
				VehicleDTO.setModelDescription(rs.getString("mod_description"));
				VehicleDTO.setNicreg(rs.getString("pmo_nic"));
				VehicleDTO.setPermitNo(rs.getString("pm_permit_no"));
				VehicleDTO.setPermitOwner(rs.getString("pmo_full_name"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setRouteDetails(rs.getString("rou_description"));
				VehicleDTO.setRouteNo(rs.getString("pm_route_no"));
				VehicleDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				VehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				VehicleDTO.setOrigine(rs.getString("rou_service_origine"));
				VehicleDTO.setDestination(rs.getString("rou_service_destination"));
				VehicleDTO.setRouteVia(rs.getString("rou_via"));
				VehicleDTO.setBacklogApp(true);
				VehicleDTO.setValidTo(rs.getString("pm_valid_to"));
				VehicleDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				VehicleDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				VehicleDTO.setTenderPermit(rs.getString("pm_is_tender_permit"));
				VehicleDTO.setBacklogStatus(backlogStatus);
				VehicleDTO.setPermitIssueDate(rs.getString("pm_permit_issue_date"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;

	}

	@Override
	public boolean saveDataVehicleOwnerWithApplicatioNo(VehicleInspectionDTO VehicleDTO,
			String generatedapplicationNO) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO nt_t_pm_vehi_owner(seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, pm_created_by, pm_created_date"
					+ "					)SELECT nextval('seq_nt_t_pm_vehi_owner'), "
					+ "				   ?,pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, ?, ?"
					+ "				FROM nt_t_pm_vehi_owner WHERE pmo_application_no=?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, generatedapplicationNO);
			stmt.setString(2, VehicleDTO.getCreateBy());
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, VehicleDTO.getApplicationNo());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isDataSave = true;
			} else {
				isDataSave = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isDataSave;
	}

	@Override
	public boolean saveDataVehicleOwnerWithOutApplicationNo(VehicleInspectionDTO VehicleDTO,
			String generatedapplicationNO) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "insert into nt_t_pm_vehi_owner"
					+ "(seq,pmo_application_no,pmo_permit_no,pmo_vehicle_regno,pmo_full_name,pmo_nic,pm_created_by,pm_created_date,pmo_is_backlog_app)"
					+ "values (?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_vehi_owner");

			stmt.setLong(1, seqNo);
			stmt.setString(2, generatedapplicationNO);
			stmt.setString(3, VehicleDTO.getPermitNo());
			stmt.setString(4, VehicleDTO.getVehicleNo());
			stmt.setString(5, VehicleDTO.getPermitOwner());
			stmt.setString(6, VehicleDTO.getNicreg());
			stmt.setString(7, VehicleDTO.getLoginUser());
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, "N");

			int i = stmt.executeUpdate();

			if (i > 0) {
				isDataSave = true;
			} else {
				isDataSave = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isDataSave;

	}

	@Override
	public boolean saveDataApplication(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO,
			String loginUser) {
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		String date3 = null;
		String date1_2 = null;
		String expireDate = null;

		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (VehicleDTO.getCalender1() != null) {
			date1_2 = sdf.format(VehicleDTO.getCalender1());
		}

		if (VehicleDTO.getCalender2() != null) {
			date3 = sdf.format(VehicleDTO.getCalender2());
		}

		if (VehicleDTO.getExpireDate() != null) {
			expireDate = VehicleDTO.getExpireDate();
		}

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSave = false;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_application");
			String sql = "insert into nt_t_pm_application "
					+ "(seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno,"
					+ "pm_status, pm_service_type, pm_route_no, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_created_by, pm_created_date,"
					+ "pm_is_backlog_app,pm_valid_to,pm_tot_bus_fare,pm_permit_expire_date,pm_route_flag,pm_is_tender_permit, pm_permit_issue_date,pm_reinspec_status,pm_isnew_permit, pm_tran_type) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, generatedapplicationNO);
			stmt.setString(3, VehicleDTO.getQueueNo());
			stmt.setString(4, VehicleDTO.getPermitNo());
			stmt.setString(5, VehicleDTO.getVehicleNo());
			stmt.setString(6, "O");
			stmt.setString(7, VehicleDTO.getServiceTypeCode());
			stmt.setString(8, VehicleDTO.getRouteNo());
			if (date1_2 != null && !date1_2.trim().equalsIgnoreCase("") && !date1_2.isEmpty()) {
				stmt.setString(9, date1_2);
			} else {
				stmt.setNull(9, java.sql.Types.VARCHAR);
			}
			if (date3 != null && !date3.trim().equalsIgnoreCase("") && !date3.isEmpty()) {
				stmt.setString(10, date3);
			} else {
				stmt.setNull(10, java.sql.Types.VARCHAR);
			}
			stmt.setString(11, VehicleDTO.getFinalRemark());
			stmt.setString(12, VehicleDTO.getLoginUser());
			stmt.setTimestamp(13, timestamp);
			stmt.setString(14, VehicleDTO.getBacklogStatus());
			stmt.setString(15, VehicleDTO.getValidTo());
			stmt.setBigDecimal(16, VehicleDTO.getBusFare());
			if (expireDate != null && !expireDate.trim().equalsIgnoreCase("") && !expireDate.isEmpty()) {
				stmt.setString(17, expireDate);
			} else {
				stmt.setNull(17, java.sql.Types.VARCHAR);
			}
			stmt.setString(18, VehicleDTO.getRouteFlag());
			stmt.setString(19, VehicleDTO.getTenderPermit());
			stmt.setString(20, VehicleDTO.getPermitIssueDate());
			stmt.setString(21, VehicleDTO.getInspectionStatus());
			stmt.setString(22, VehicleDTO.getIsNewPermit());
			stmt.setString(23, VehicleDTO.getTranstractionTypeCode());

			int i = stmt.executeUpdate();

			if (i > 0) {
				isDataSave = true;

				String query2 = "UPDATE public.nt_t_pm_application "
						+ "SET pm_status='I' , pm_modified_by=?, pm_modified_date=?" + "WHERE pm_application_no=?; ";

				ps = con.prepareStatement(query2);
				ps.setString(1, loginUser);
				ps.setTimestamp(2, timestamp);
				ps.setString(3, VehicleDTO.getOldApplicationNo());

				ps.executeUpdate();

			} else {
				isDataSave = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return isDataSave;

	}

	@Override
	public boolean saveDataVehicleInspecDetails(VehicleInspectionDTO VehicleDTO, List<VehicleInspectionDTO> dataList,
			String generatedapplicationNO) {
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

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_vehicle_inspec_det");

				String sql = "INSERT INTO public.nt_t_vehicle_inspec_det "
						+ "(vid_seq_no, vid_app_no, vid_sec_seq, vid_remark, vid_exist, vid_created_by, vid_created_date) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, generatedapplicationNO);
				stmt.setInt(3, dataList.get(i).getNewGridNo());

				if (dataList.get(i).getRemark() != null) {
					stmt.setString(4, dataList.get(i).getRemark().trim());
				} else {
					logger.info("UPDATING NULL VALUES");
					stmt.setNull(4, java.sql.Types.VARCHAR);
				}

				if (dataList.get(i).isExists() == true) {
					exitTest = "YES";
				} else {
					exitTest = "NO";
				}
				stmt.setString(5, exitTest);
				stmt.setString(6, VehicleDTO.getLoginUser());
				stmt.setTimestamp(7, timestamp);

				int c = stmt.executeUpdate();

				if (c > 0) {
					isDataSave = true;
					logger.info("Inserting vehicle inspection........");
				} else {
					isDataSave = false;
					logger.error("ERROR........");
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
	public List<VehicleInspectionDTO> Gridview(VehicleInspectionDTO vehicleDTO) {

		List<VehicleInspectionDTO> dataList = new ArrayList<VehicleInspectionDTO>();
		Connection con = null;
		String query;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String applicationNo = vehicleDTO.getApplicationNo();

		try {
			con = ConnectionManager.getConnection();

			query = "select b.iap_seq ,b.rou_code, b.rou_section_code ,vid_remark,vid_exist,vid_seq_no,  b.rou_description , c.description as sectiondes from nt_t_vehicle_inspec_det a , nt_r_inspec_act_points b, public.nt_r_sections c  where vid_app_no='"
					+ applicationNo
					+ "' and b.active='A' and  b.iap_seq = a.vid_sec_seq and b.rou_section_code=c.code order by vid_seq_no;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();
				VehicleDTO.setNewGridNo((rs.getInt("iap_seq")));
				VehicleDTO.setActionPointSeqNo(rs.getString("rou_code"));
				VehicleDTO.setRemark((rs.getString("vid_remark")));
				VehicleDTO.setDescription(rs.getString("rou_description"));
				VehicleDTO.setSection(rs.getString("sectiondes"));
				VehicleDTO.setExistsText(rs.getString("vid_exist"));
				VehicleDTO.setVehicleInspectionSeq(rs.getLong("vid_seq_no"));
				if (VehicleDTO.getExistsText().equalsIgnoreCase("yes")) {
					VehicleDTO.setExists(true);
				} else {
					VehicleDTO.setExists(false);
				}

				dataList.add(VehicleDTO);
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

	public void searchQueueTable(VehicleInspectionDTO vehicleDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String query = "select que_number,que_seq,que_vehicle_no,que_application_no " + "from nt_m_queue_master "
					+ "where que_number='" + vehicleDTO.getQueueNo() + "' and to_char(created_date,'dd-MM-yyyy')= '"
					+ today + "'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleDTO.setApplicationNo(rs.getString("que_application_no"));
				vehicleDTO.setQueueNo(rs.getString("que_number"));
				vehicleDTO.setPermitSeqNo(Long.valueOf(rs.getInt("que_seq")));
				vehicleDTO.setVehicleNo(rs.getString("que_vehicle_no"));

				if (vehicleDTO.getApplicationNo() == null) {
					vehicleDTO.setApplicationNo(vehicleDTO.getOldApplicationNo());
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public VehicleInspectionDTO search(String queueNo, boolean inspectionForAmendment) {

		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();
			String query;

			String backlogStatus = isbacklog(queueNo);

			if (inspectionForAmendment) {
				backlogStatus = "N";
			}

			if (backlogStatus != null && !backlogStatus.isEmpty() && !backlogStatus.trim().equalsIgnoreCase("")
					&& backlogStatus.equalsIgnoreCase("Y")) {
				query = "select distinct a.pm_isnew_permit,a.pm_inspect_remark,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type,"
						+ " b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
						+ " g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_valid_to,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit"
						+ " from nt_t_pm_application a , nt_m_queue_master d,nt_r_route b ,nt_r_service_types c, nt_t_pm_omini_bus1 e,nt_r_make f,nt_r_model g, nt_t_pm_vehi_owner h"
						+ " where a.pm_vehicle_regno = d.que_vehicle_no" + " and a.pm_route_no = b.rou_number "
						+ " and a.pm_service_type= c.code " + " and a.pm_application_no = e.pmb_application_no "
						+ " and e.pmb_make=f.code " + " and  e.pmb_model = g.mod_code "
						+ " and a.pm_application_no = h.pmo_application_no " + " and a.pm_status='A'"
						+ " and d.que_number= '" + queueNo + "'" + " and to_char(d.created_date,'dd-MM-yyyy')='" + today
						+ "'";
			} else {

				query = "select distinct a.pm_isnew_permit,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_valid_to,a.pm_inspect_remark,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type, "
						+ " b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
						+ " g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit "
						+ " from public.nt_t_pm_application a left outer join public.nt_r_route b on a.pm_route_no = b.rou_number "
						+ " left outer join public.nt_r_service_types c on a.pm_service_type= c.code "
						+ " left outer join public.nt_m_queue_master d on a.pm_application_no = d.que_application_no "
						+ " left outer join nt_t_pm_omini_bus1 e on a.pm_application_no = e.pmb_application_no "
						+ " left outer join nt_r_make f on e.pmb_make=f.code "
						+ " left outer join nt_r_model g on e.pmb_model = g.mod_code "
						+ " left outer join nt_t_pm_vehi_owner h on a.pm_application_no = h.pmo_application_no "
						+ " where d.que_number='" + queueNo + "' and to_char(d.created_date,'dd-MM-yyyy')= '" + today
						+ "'";
			}

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				VehicleDTO.setIsNewPermit(rs.getString("pm_isnew_permit"));
				VehicleDTO.setFinalRemark(rs.getString("pm_inspect_remark"));
				VehicleDTO.setInspectionStatus(rs.getString("pm_reinspec_status"));
				VehicleDTO.setDate1(rs.getString("pm_next_ins_date_sec1_2"));
				VehicleDTO.setDate2(rs.getString("pm_next_ins_date_sec3"));
				VehicleDTO.setRouteFlag(rs.getString("pm_route_flag"));
				VehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setOldApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setPermitSeqNo(rs.getLong("seq"));
				VehicleDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				VehicleDTO.setManyear(rs.getString("pmb_production_date"));
				VehicleDTO.setMakeDescription(rs.getString("make"));
				VehicleDTO.setMakeTypeCode(rs.getString("pmb_make"));
				VehicleDTO.setModelTypeCode(rs.getString("pmb_model"));
				VehicleDTO.setModelDescription(rs.getString("mod_description"));
				VehicleDTO.setNicreg(rs.getString("pmo_nic"));
				VehicleDTO.setPermitNo(rs.getString("pm_permit_no"));
				VehicleDTO.setPermitOwner(rs.getString("pmo_full_name"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setRouteDetails(rs.getString("rou_description"));
				VehicleDTO.setRouteNo(rs.getString("pm_route_no"));
				VehicleDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				VehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				VehicleDTO.setOrigine(rs.getString("rou_service_origine"));
				VehicleDTO.setDestination(rs.getString("rou_service_destination"));
				VehicleDTO.setRouteVia(rs.getString("rou_via"));
				VehicleDTO.setBacklogApp(true);
				VehicleDTO.setValidTo(rs.getString("pm_valid_to"));
				VehicleDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				VehicleDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				VehicleDTO.setTenderPermit(rs.getString("pm_is_tender_permit"));
				VehicleDTO.setBacklogStatus(backlogStatus);
				VehicleDTO.setPermitIssueDate(rs.getString("pm_permit_issue_date"));

			}

			if (!rs.isBeforeFirst()) {
				VehicleDTO.setQueueNo(queueNo);
				searchQueueTable(VehicleDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;

	}

	public String isbacklog(String queueNo) {
		String backlogStatus = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String query = "select d.que_vehicle_no, a.pm_is_backlog_app "
					+ " from nt_m_queue_master d, nt_t_pm_application a "
					+ " where a.pm_vehicle_regno = d.que_vehicle_no " + " and d.que_number='" + queueNo + "'"
					+ " and a.pm_status = 'A' and to_char(d.created_date,'dd-MM-yyyy')= '" + today + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				backlogStatus = rs.getString("pm_is_backlog_app");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		if (backlogStatus == null || backlogStatus.equalsIgnoreCase("N") || backlogStatus.trim().equalsIgnoreCase("")) {
			backlogStatus = "N";
		} else {
			backlogStatus = "Y";
		}
		return backlogStatus;

	}

	@Override
	public boolean routeDetails(VehicleInspectionDTO vehicleDTO, String routeno) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select rou_description " + "from nt_r_route " + "where rou_number='" + routeno + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleDTO.setRouteDetails(rs.getString("rou_description"));

			}
			return true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public List<VehicleInspectionDTO> routeNodropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rou_number,rou_description" + " FROM nt_r_route" + " where active='A'"
					+ " order by rou_number";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setRouteNo(rs.getString("rou_number"));
				vehicleDTO.setRouteDetails(rs.getString("rou_description"));

				returnList.add(vehicleDTO);

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
	public List<VehicleInspectionDTO> makedropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM nt_r_make WHERE active = 'A' and code != '' and description != '' "
					+ "order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setMakeTypeCode(rs.getString("code"));
				vehicleDTO.setMakeDescription(rs.getString("description"));
				// make dto for code
				returnList.add(vehicleDTO);

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
	public void counterStatus(String counterId, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "update nt_r_counter "
					+ "set cou_status='A',cou_serving_queueno=NULL , cou_assigned_userid='" + user
					+ "' where cou_counter_id = '" + counterId + "'";
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
	public List<CommonDTO> counterdropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select cou_counter_name,cou_counter_id " + "from nt_r_counter "
					+ "where cou_status='I' and cou_trn_type='02'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();

				commonDTO.setCounter(rs.getString("cou_counter_name"));
				commonDTO.setCounterId(rs.getString("cou_counter_id"));
				// make dto for code
				returnList.add(commonDTO);

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
	public List<VehicleInspectionDTO> servicetypedropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description" + " FROM nt_r_service_types" + " WHERE active = 'A'"
					+ " order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setServiceTypeCode(rs.getString("code"));
				vehicleDTO.setServiceTypeDescription(rs.getString("description"));
				// make dto for code
				returnList.add(vehicleDTO);

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
	public List<VehicleInspectionDTO> modeldropdown(String code) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query;
			if (code.equals("") || code.equals(null)) {
				query = "SELECT mod_code, mod_description" + " FROM nt_r_model" + " WHERE active = 'A'"
						+ " order by mod_description";
			} else {

				query = "SELECT mod_code, mod_description" + " FROM nt_r_model"
						+ " WHERE active = 'A' AND mod_make_code = '" + code + "'" + " order by mod_description";
			}
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setModelTypeCode(rs.getString("mod_code"));
				vehicleDTO.setModelDescription(rs.getString("mod_description"));
				// make dto for code
				returnList.add(vehicleDTO);

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
	public String generateApplicationNo() {
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

			String sql = "SELECT pm_application_no "
					+ " FROM public.nt_t_pm_application WHERE pm_application_no LIKE 'PAP%'"
					+ " ORDER BY pm_created_date desc " + " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pm_application_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "PAP" + currYear + ApprecordcountN;
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
					strAppNo = "PAP" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "PAP" + currYear + "00001";

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
	public void insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(UploadImageDTO uploadImageDTO, String loginUser,
			String status) {
		logger.info("insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean appNoAvailable = false;
		try {
			con = ConnectionManager.getConnection();

			/** Check if data available for application no start **/
			String selectQuery = "SELECT * FROM public.nt_t_owner_vehi_images WHERE ovi_app_no=?";

			ps = con.prepareStatement(selectQuery);
			ps.setString(1, uploadImageDTO.getApplicationNo());

			rs = ps.executeQuery();

			while (rs.next()) {
				appNoAvailable = true;
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			
			/** Check if data available for application no end **/

			if (appNoAvailable) {
				// data available -> update image paths

				String sql = "UPDATE public.nt_t_owner_vehi_images "
						+ "SET ovi_owner_image=?, ovi_image1=?, ovi_image2=?, ovi_image3=?, ovi_image4=?, ovi_image5=?, ovi_image6=?, ovi_modified_by=?, ovi_modified_date=?, ovi_status=? "
						+ "WHERE ovi_app_no=?";

				ps = con.prepareStatement(sql);
				ps.setString(1, uploadImageDTO.getVehicleOwnerPhotoPath());
				ps.setString(2, uploadImageDTO.getFirstVehiImagePath());
				ps.setString(3, uploadImageDTO.getSecondVehiImagePath());
				ps.setString(4, uploadImageDTO.getThirdVehiImagePath());
				ps.setString(5, uploadImageDTO.getForthVehiImagePath());
				ps.setString(6, uploadImageDTO.getFifthVehiImagePath());
				ps.setString(7, uploadImageDTO.getSixthVehiImagePath());
				ps.setString(8, loginUser);
				ps.setTimestamp(9, timestamp);
				ps.setString(10, status);
				ps.setString(11, uploadImageDTO.getApplicationNo());

				ps.executeUpdate();

			} else {
				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_owner_vehi_images");

				String sql = "INSERT INTO public.nt_t_owner_vehi_images "
						+ "(ovi_seq_no, ovi_app_no, ovi_owner_image, ovi_image1, ovi_image2, ovi_image3, ovi_image4, ovi_image5, ovi_image6, ovi_created_by, ovi_created_date) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, uploadImageDTO.getApplicationNo());
				ps.setString(3, uploadImageDTO.getVehicleOwnerPhotoPath());
				ps.setString(4, uploadImageDTO.getFirstVehiImagePath());
				ps.setString(5, uploadImageDTO.getSecondVehiImagePath());
				ps.setString(6, uploadImageDTO.getThirdVehiImagePath());
				ps.setString(7, uploadImageDTO.getForthVehiImagePath());
				ps.setString(8, uploadImageDTO.getFifthVehiImagePath());
				ps.setString(9, uploadImageDTO.getSixthVehiImagePath());
				ps.setString(10, loginUser);
				ps.setTimestamp(11, timestamp);

				ps.executeUpdate();
			}

			con.commit();

		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES end");
	}

	@Override
	public void updateCounter(String username) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "update nt_r_counter " + "set cou_status='I', cou_serving_queueno=NULL "
					+ "where cou_assigned_userid ='" + username + "'";

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
	public UploadImageDTO findUploadImageDetailsByVehicleNo(String vehicleNo, String applicationNo, String loginUser) {
		logger.info("findUploadImageDetailsByVehicleNo start");

		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		String taskCode = "PM101";
		boolean taskOngoing = false;

		UploadImageDTO uploadImageDTO = new UploadImageDTO();

		try {
			con = ConnectionManager.getConnection();

			// 1. for vehicle no and application no check task PM101 is there in
			// nt_t_task_det

			/** check application no and vehicle no is in nt_t_task_det table start **/
			String sql2 = "SELECT * FROM public.nt_t_task_det WHERE tsd_vehicle_no = ? and tsd_app_no =? and tsd_task_code = ?";

			ps = con.prepareStatement(sql2);
			ps.setString(1, vehicleNo);
			ps.setString(2, applicationNo);
			ps.setString(3, taskCode);

			rs = ps.executeQuery();

			while (rs.next()) {
				taskOngoing = true;
			}

			// 2. if it is not there save process with PM101 and status as "O"
			if (taskOngoing) {
				// get the data/images of relevant vehicle no and display in form
				uploadImageDTO = retrieveVehicleImageDataForVehicleNo(applicationNo);// in here also there can be
																						// cases where applicationNo
																						// is null but photos are
																						// uploaded.

			} else {
				// save with task code and status

				commonService.updateTaskStatus(applicationNo, "PM100", "PM101", "C", loginUser);

				ConnectionManager.close(rs);
				ConnectionManager.close(ps);

				/** check application no and vehicle no is in nt_t_task_det table end **/

				// 3. delete vehicle inspection related task PM100 and insert record to
				// nt_h_task_his table
				/**
				 * delete vehicle inspection related task PM100 and insert record to
				 * nt_h_task_his table start
				 **/

				String sql = "SELECT tsd_seq FROM public.nt_t_task_det WHERE tsd_app_no=? and tsd_vehicle_no=? and tsd_task_code=?";
				ps = con.prepareStatement(sql);
				ps.setString(1, applicationNo);
				ps.setString(2, vehicleNo);
				ps.setString(3, taskCode);

				rs = ps.executeQuery();

				while (rs.next()) {
				}

				/**
				 * delete vehicle inspection related task PM100 and insert record to
				 * nt_h_task_his table end
				 **/

			}
		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("findUploadImageDetailsByVehicleNo end");

		return uploadImageDTO;
	}

	@Override
	public UploadImageDTO retrieveVehicleImageDataForVehicleNo(String applicationNo) {
		logger.info("retrieveVehicleImageDataForVehicleNo(applicationNo-" + applicationNo + ") start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		UploadImageDTO dto = null;
		try {
			String sql = "SELECT ovi_owner_image, ovi_image1, ovi_image2, ovi_image3, ovi_image4, ovi_image5, ovi_image6 "
					+ "FROM public.nt_t_owner_vehi_images WHERE ovi_app_no=?";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new UploadImageDTO();
				dto.setVehicleOwnerPhotoPath(rs.getString("ovi_owner_image"));
				dto.setFirstVehiImagePath(rs.getString("ovi_image1"));
				dto.setSecondVehiImagePath(rs.getString("ovi_image2"));
				dto.setThirdVehiImagePath(rs.getString("ovi_image3"));
				dto.setForthVehiImagePath(rs.getString("ovi_image4"));
				dto.setFifthVehiImagePath(rs.getString("ovi_image5"));
				dto.setSixthVehiImagePath(rs.getString("ovi_image6"));
				dto.setApplicationNo(applicationNo);
				break;
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("retrieveVehicleImageDataForVehicleNo end");

		return dto;
	}

	@Override
	public String checkStatusFromApplicationNumber(String applicationNo) {

		logger.info("retrieveVehicleImageDataForVehicleNo(applicationNo-" + applicationNo + ") start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT ovi_status " + "FROM public.nt_t_owner_vehi_images WHERE ovi_app_no=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				status = rs.getString("ovi_status");
				break;
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("retrieveVehicleImageDataForVehicleNo end");

		return status;

	}

	@Override
	public VehicleInspectionDTO searchOnBackBtnAction(String queueNo) {

		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type,"
					+ " b.rou_description,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
					+ " g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic"
					+ " from nt_t_pm_application a , nt_m_queue_master d,nt_r_route b ,nt_r_service_types c, nt_t_pm_omini_bus1 e,nt_r_make f,nt_r_model g, nt_t_pm_vehi_owner h"
					+ " where a.pm_application_no = d.que_application_no " + " and a.pm_route_no = b.rou_number "
					+ " and a.pm_service_type= c.code " + " and a.pm_application_no = e.pmb_application_no "
					+ " and e.pmb_make=f.code " + " and  e.pmb_model = g.mod_code "
					+ " and a.pm_application_no = h.pmo_application_no " +

					" and d.que_number= '" + queueNo + "'" + " and to_char(d.created_date,'dd-MM-yyyy')='" + today
					+ "'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleDTO.setRouteFlag(rs.getString("pm_route_flag"));
				VehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setOldApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setPermitSeqNo(rs.getLong("seq"));
				VehicleDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				VehicleDTO.setManyear(rs.getString("pmb_production_date"));
				VehicleDTO.setMakeDescription(rs.getString("make"));
				VehicleDTO.setMakeTypeCode(rs.getString("pmb_make"));
				VehicleDTO.setModelTypeCode(rs.getString("pmb_model"));
				VehicleDTO.setModelDescription(rs.getString("mod_description"));
				VehicleDTO.setNicreg(rs.getString("pmo_nic"));
				VehicleDTO.setPermitNo(rs.getString("pm_permit_no"));
				VehicleDTO.setPermitOwner(rs.getString("pmo_full_name"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setRouteDetails(rs.getString("rou_description"));
				VehicleDTO.setRouteNo(rs.getString("pm_route_no"));
				VehicleDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				VehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				VehicleDTO.setBacklogApp(true);

			}

			if (!rs.isBeforeFirst()) {
				VehicleDTO.setQueueNo(queueNo);
				searchQueueTable(VehicleDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;

	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	@Override
	public VehicleInspectionDTO getDataForReinspection(String queueNo) {

		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();
			String query;

			query = "select  distinct a.pm_isnew_permit,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_valid_to,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type,  b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make,  g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit  "
					+ "from public.nt_t_pm_application a left outer join public.nt_r_route b on a.pm_route_no = b.rou_number  "
					+ "left outer join public.nt_r_service_types c on a.pm_service_type= c.code  "
					+ "left outer join public.nt_m_queue_master d on a.pm_vehicle_regno = d.que_vehicle_no  "
					+ "left outer join nt_t_pm_omini_bus1 e on a.pm_application_no = e.pmb_application_no  "
					+ "left outer join nt_r_make f on e.pmb_make=f.code  "
					+ "left outer join nt_r_model g on e.pmb_model = g.mod_code  "
					+ "left outer join nt_t_pm_vehi_owner h on a.pm_application_no = h.pmo_application_no  "
					+ "where d.que_number='" + queueNo + "'" + "and to_char(d.created_date,'dd-MM-yyyy')= '" + today
					+ "'" + "and a.pm_status != 'I'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				VehicleDTO.setIsNewPermit(rs.getString("pm_isnew_permit"));
				VehicleDTO.setInspectionStatus(rs.getString("pm_reinspec_status"));
				VehicleDTO.setDate1(rs.getString("pm_next_ins_date_sec1_2"));
				VehicleDTO.setDate2(rs.getString("pm_next_ins_date_sec3"));
				VehicleDTO.setRouteFlag(rs.getString("pm_route_flag"));
				VehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setOldApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setPermitSeqNo(rs.getLong("seq"));
				VehicleDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				VehicleDTO.setManyear(rs.getString("pmb_production_date"));
				VehicleDTO.setMakeDescription(rs.getString("make"));
				VehicleDTO.setMakeTypeCode(rs.getString("pmb_make"));
				VehicleDTO.setModelTypeCode(rs.getString("pmb_model"));
				VehicleDTO.setModelDescription(rs.getString("mod_description"));
				VehicleDTO.setNicreg(rs.getString("pmo_nic"));
				VehicleDTO.setPermitNo(rs.getString("pm_permit_no"));
				VehicleDTO.setPermitOwner(rs.getString("pmo_full_name"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setRouteDetails(rs.getString("rou_description"));
				VehicleDTO.setRouteNo(rs.getString("pm_route_no"));
				VehicleDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				VehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				VehicleDTO.setOrigine(rs.getString("rou_service_origine"));
				VehicleDTO.setDestination(rs.getString("rou_service_destination"));
				VehicleDTO.setRouteVia(rs.getString("rou_via"));
				VehicleDTO.setBacklogApp(true);
				VehicleDTO.setValidTo(rs.getString("pm_valid_to"));
				VehicleDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				VehicleDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				VehicleDTO.setTenderPermit(rs.getString("pm_is_tender_permit"));
				VehicleDTO.setBacklogStatus("N");
				VehicleDTO.setPermitIssueDate(rs.getString("pm_permit_issue_date"));

			}

			if (!rs.isBeforeFirst()) {
				VehicleDTO.setQueueNo(queueNo);
				searchQueueTable(VehicleDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;
	}

	@Override
	public String applicationTaskCodeStatusPM100(String applicationNo) {
		String taskStatus = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String query = "select a.pm_application_no as pm_application_no"
					+ " from nt_t_pm_application a inner join nt_t_task_det b"
					+ " on a.pm_application_no = b.tsd_app_no inner join nt_m_queue_master d on d.que_number=a.pm_queue_no"
					+ " where a.pm_application_no ='" + applicationNo + "'"
					+ " and b.tsd_task_code = 'PM100'and b.tsd_status = 'C'"
					+ "and to_char(d.created_date,'dd-MM-yyyy')= '" + today + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				taskStatus = rs.getString("pm_application_no");

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
	public String applicationTaskCodeStatusPM101(String applicationNo) {
		String taskStatus = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String query = "select a.pm_application_no as pm_application_no"
					+ " from nt_t_pm_application a inner join nt_h_task_his b"
					+ " on a.pm_application_no = b.tsd_app_no inner join nt_m_queue_master d on d.que_number=a.pm_queue_no"
					+ " where a.pm_application_no ='" + applicationNo + "'"
					+ " and b.tsd_task_code = 'PM101'and b.tsd_status = 'O'"
					+ "and to_char(b.created_date,'dd-MM-yyyy')= '" + today + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				taskStatus = rs.getString("pm_application_no");

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
	public void permanentSkip(String queueNo) {
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);
		String skipCount = null;
		String query;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			query = "select number_value " + "from nt_r_parameters " + "where param_name='QUEUE_MASTER_SKIP_COUNT'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				skipCount = rs.getString("number_value");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		try {
			con = ConnectionManager.getConnection();

			query = "UPDATE public.nt_m_queue_master " + "SET que_skip_count=? "
					+ "WHERE que_number=? AND que_date LIKE '" + today + "%'";

			ps = con.prepareStatement(query);

			ps.setString(1, skipCount);
			ps.setString(2, queueNo);
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
	public UploadImageDTO findVehicleOwnerByFormerApplicationNo(String vehicleNo) {
		logger.info("findVehicleOwnerByFormerApplicationNo start");

		String query;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UploadImageDTO dto = new UploadImageDTO();
		try {
			con = ConnectionManager.getConnection();

			int count = 0;
			String query1 = "select count(pm_application_no) as numCount from public.nt_t_pm_application where pm_vehicle_regno ='"
					+ vehicleNo + "'";
			ps = con.prepareStatement(query1);
			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("numCount");
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			if (count >= 2) {

				query = "select * from public.nt_t_owner_vehi_images where ovi_app_no = (select a.pm_application_no "
						+ "from public.nt_t_pm_application a,public.nt_t_owner_vehi_images b where a.pm_vehicle_regno ='"
						+ vehicleNo + "' "
						+ "and b.ovi_app_no=a.pm_application_no and b.ovi_owner_image is not null order by seq DESC limit 1)";

				ps = con.prepareStatement(query);

				rs = ps.executeQuery();

				while (rs.next()) {
					dto = new UploadImageDTO();
					dto.setVehicleOwnerPhotoPath(rs.getString("ovi_owner_image"));

					dto.setFirstVehiImagePath(rs.getString("ovi_image1"));
					dto.setSecondVehiImagePath(rs.getString("ovi_image2"));
					dto.setThirdVehiImagePath(rs.getString("ovi_image3"));
					dto.setForthVehiImagePath(rs.getString("ovi_image4"));
					dto.setFifthVehiImagePath(rs.getString("ovi_image5"));
					dto.setSixthVehiImagePath(rs.getString("ovi_image6"));

					dto.setApplicationNo(rs.getString("ovi_app_no"));
				}

			}

		} catch (Exception e) {
			logger.info("findVehicleOwnerByFormerApplicationNo error: " + e.toString());
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("findVehicleOwnerByFormerApplicationNo end");
		return dto;
	}

	@Override
	public VehicleInspectionDTO getApplicationForAmendment(String generatedApplicationNo) {

		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();
			String query;

			query = "select  distinct a.pm_isnew_permit,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_valid_to,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type,  b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make,  g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit  "
					+ "from public.nt_t_pm_application a left outer join public.nt_r_route b on a.pm_route_no = b.rou_number  "
					+ "left outer join public.nt_r_service_types c on a.pm_service_type= c.code  "
					+ "left outer join public.nt_m_queue_master d on a.pm_vehicle_regno = d.que_vehicle_no  "
					+ "left outer join nt_t_pm_omini_bus1 e on a.pm_application_no = e.pmb_application_no  "
					+ "left outer join nt_r_make f on e.pmb_make=f.code  "
					+ "left outer join nt_r_model g on e.pmb_model = g.mod_code  "
					+ "left outer join nt_t_pm_vehi_owner h on a.pm_application_no = h.pmo_application_no  "
					+ "where a.pm_application_no='" + generatedApplicationNo + "'" + "and a.pm_status = 'P'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				VehicleDTO.setIsNewPermit(rs.getString("pm_isnew_permit"));
				VehicleDTO.setInspectionStatus(rs.getString("pm_reinspec_status"));
				VehicleDTO.setDate1(rs.getString("pm_next_ins_date_sec1_2"));
				VehicleDTO.setDate2(rs.getString("pm_next_ins_date_sec3"));

				VehicleDTO.setRouteFlag(rs.getString("pm_route_flag"));

				VehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setOldApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setPermitSeqNo(rs.getLong("seq"));
				VehicleDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				VehicleDTO.setManyear(rs.getString("pmb_production_date"));
				VehicleDTO.setMakeDescription(rs.getString("make"));
				VehicleDTO.setMakeTypeCode(rs.getString("pmb_make"));
				VehicleDTO.setModelTypeCode(rs.getString("pmb_model"));
				VehicleDTO.setModelDescription(rs.getString("mod_description"));
				VehicleDTO.setNicreg(rs.getString("pmo_nic"));
				VehicleDTO.setPermitNo(rs.getString("pm_permit_no"));
				VehicleDTO.setPermitOwner(rs.getString("pmo_full_name"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setRouteDetails(rs.getString("rou_description"));
				VehicleDTO.setRouteNo(rs.getString("pm_route_no"));
				VehicleDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				VehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				VehicleDTO.setOrigine(rs.getString("rou_service_origine"));
				VehicleDTO.setDestination(rs.getString("rou_service_destination"));
				VehicleDTO.setRouteVia(rs.getString("rou_via"));
				VehicleDTO.setBacklogApp(true);
				VehicleDTO.setValidTo(rs.getString("pm_valid_to"));
				VehicleDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));

				VehicleDTO.setExpireDate(rs.getString("pm_permit_expire_date"));

				VehicleDTO.setTenderPermit(rs.getString("pm_is_tender_permit"));

				VehicleDTO.setBacklogStatus("N");
				VehicleDTO.setPermitIssueDate(rs.getString("pm_permit_issue_date"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;

	}

	@Override
	public void saveInspectionForAmendment(VehicleInspectionDTO VehicleDTO) {
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		String date3 = null;
		String date1_2 = null;

		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (VehicleDTO.getCalender1() != null) {
			date1_2 = sdf.format(VehicleDTO.getCalender1());
		}

		if (VehicleDTO.getCalender2() != null) {
			date3 = sdf.format(VehicleDTO.getCalender2());
		}

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_t_pm_application SET pm_next_ins_date_sec1_2 = ?, pm_next_ins_date_sec3 = ?, pm_inspect_remark= ?,pm_reinspec_status = ?,pm_modified_by=?,pm_modified_date=? WHERE pm_application_no = ?";

			stmt = con.prepareStatement(sql);

			if (date1_2 != null && !date1_2.trim().equalsIgnoreCase("") && !date1_2.isEmpty()) {
				stmt.setString(1, date1_2);
			} else {
				stmt.setNull(1, java.sql.Types.VARCHAR);
			}

			if (date3 != null && !date3.trim().equalsIgnoreCase("") && !date3.isEmpty()) {
				stmt.setString(2, date3);
			} else {
				stmt.setNull(2, java.sql.Types.VARCHAR);
			}

			stmt.setString(3, VehicleDTO.getFinalRemark());

			stmt.setString(4, VehicleDTO.getInspectionStatus());

			stmt.setString(5, VehicleDTO.getCreateBy());

			stmt.setTimestamp(6, timestamp);

			stmt.setString(7, VehicleDTO.getApplicationNo());

			stmt.executeUpdate();

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
	public void updateOminiBusForInspection(OminiBusDTO ominiBusDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_omini_bus1 SET pmb_chassis_no=?, pmb_make=?, pmb_model=?, pmb_modified_by=?,pmb_modified_date=? WHERE pmb_application_no= ?";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ominiBusDTO.getChassisNo());
			stmt.setString(2, ominiBusDTO.getMake());
			stmt.setString(3, ominiBusDTO.getModel());
			stmt.setString(4, ominiBusDTO.getModifiedBy());
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, ominiBusDTO.getApplicationNo());

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
	public boolean saveDataVehicleInspecDetailsHistory(VehicleInspectionDTO vehicleDTO,
			List<VehicleInspectionDTO> dataList, String generatedApplicationNO) {

		int versionNo = getVersionNumberVehicleInspecDetailsHistory(generatedApplicationNO);

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

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_vehicle_inspec_det");

				String sql = "INSERT INTO public.nt_h_vehicle_inspec_det "
						+ "(vid_seq_no, vid_app_no, vid_sec_seq, vid_remark, vid_exist, vid_created_by, vid_created_date,vid_version) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, generatedApplicationNO);
				stmt.setInt(3, dataList.get(i).getNewGridNo());

				if (dataList.get(i).getRemark() != null) {
					stmt.setString(4, dataList.get(i).getRemark().trim());
				} else {
					logger.info("UPDATING NULL VALUES");
					stmt.setNull(4, java.sql.Types.VARCHAR);
				}

				if (dataList.get(i).isExists() == true) {
					exitTest = "YES";
				} else {
					exitTest = "NO";
				}
				stmt.setString(5, exitTest);
				stmt.setString(6, vehicleDTO.getLoginUser());
				stmt.setTimestamp(7, timestamp);
				stmt.setInt(8, versionNo);

				int c = stmt.executeUpdate();

				if (c > 0) {
					isDataSave = true;
					logger.info("Inserting vehicle inspection........");
				} else {
					isDataSave = false;
					logger.error("ERROR........");
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

	public int getVersionNumberVehicleInspecDetailsHistory(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT vid_version " + "FROM nt_h_vehicle_inspec_det "
					+ "WHERE vid_app_no=? order by vid_version desc limit 1";

			ps = con.prepareStatement(sql);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				count = rs.getInt("vid_version");
			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return output;
	}

	@Override
	public void saveDataVehicleImagesWithApplicatioNo(VehicleInspectionDTO vehicleDTO, String generatedApplicationNO,
			String loginUser) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UploadImageDTO dto = null;
		try {
			con = ConnectionManager.getConnection();

			/** select vehi images for old app number start **/
			String select = "SELECT ovi_app_no, ovi_owner_image, ovi_image1, ovi_image2, ovi_image3, ovi_image4, ovi_image5, ovi_image6, "
					+ " ovi_status " + "FROM public.nt_t_owner_vehi_images where ovi_app_no='"
					+ vehicleDTO.getApplicationNo() + "'";

			stmt = con.prepareStatement(select);
			rs = stmt.executeQuery();

			while (rs.next()) {
				dto = new UploadImageDTO();
				dto.setApplicationNo(rs.getString("ovi_app_no"));
				dto.setFirstVehiImagePath(rs.getString("ovi_image1"));
				dto.setSecondVehiImagePath(rs.getString("ovi_image2"));
				dto.setThirdVehiImagePath(rs.getString("ovi_image3"));
				dto.setForthVehiImagePath(rs.getString("ovi_image4"));
				dto.setFifthVehiImagePath(rs.getString("ovi_image5"));
				dto.setSixthVehiImagePath(rs.getString("ovi_image6"));
				dto.setVehicleOwnerPhotoPath(rs.getString("ovi_owner_image"));

			}

			ConnectionManager.close(stmt);
			/** select vehi images for old app number end **/

			/** insert vehi images start **/
			if (dto != null) {
				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_owner_vehi_images");

				/** save images in physical path start **/
				UploadImageDTO newPhotosDTO = saveImagesToPhysicalPath(dto, generatedApplicationNO);
				/** save images in physical path end **/

				String sql = "INSERT INTO public.nt_t_owner_vehi_images "
						+ "(ovi_seq_no, ovi_app_no, ovi_owner_image, ovi_image1, ovi_image2, ovi_image3, ovi_image4, ovi_image5, ovi_image6, "
						+ "ovi_modified_by, ovi_modified_date, ovi_status) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, generatedApplicationNO);
				stmt.setString(3, newPhotosDTO.getVehicleOwnerPhotoPath());
				stmt.setString(4, newPhotosDTO.getFirstVehiImagePath());
				stmt.setString(5, newPhotosDTO.getSecondVehiImagePath());
				stmt.setString(6, newPhotosDTO.getThirdVehiImagePath());
				stmt.setString(7, newPhotosDTO.getForthVehiImagePath());
				stmt.setString(8, newPhotosDTO.getFifthVehiImagePath());
				stmt.setString(9, newPhotosDTO.getSixthVehiImagePath());
				stmt.setString(10, loginUser);
				stmt.setTimestamp(11, timestamp);
				stmt.setString(12, "A");

				stmt.executeUpdate();

				con.commit();
			}
			/** insert vehi images end **/

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	private UploadImageDTO saveImagesToPhysicalPath(UploadImageDTO dto, String generatesAppNum) {
		InputStream is = null;
		OutputStream os = null;
		String extension = "png";
		UploadImageDTO returnDTO = new UploadImageDTO();
		try {
			if (dto != null) {

				Properties props = PropertyReader.loadPropertyFile();
				String imagePath = props.getProperty("vehicle.inspection.upload.photo.path");
				String tempPath = imagePath + generatesAppNum + File.separator;

				File theDir = new File(tempPath);

				if (!theDir.mkdirs()) {
					theDir.mkdir();
				} else {
					theDir.mkdir();
				}

				/** owner image start */
				if (dto.getVehicleOwnerPhotoPath() != null && !dto.getVehicleOwnerPhotoPath().isEmpty()
						&& !dto.getVehicleOwnerPhotoPath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehicleOwner." + extension;
					is = new FileInputStream(dto.getVehicleOwnerPhotoPath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setVehicleOwnerPhotoPath(destPath);
				}
				/** owner image end */

				/** vehicle image one start **/
				if (dto.getFirstVehiImagePath() != null && !dto.getFirstVehiImagePath().isEmpty()
						&& !dto.getFirstVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto1." + extension;
					is = new FileInputStream(dto.getFirstVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setFirstVehiImagePath(destPath);
				}
				/** vehicle image one end **/

				/** vehicle image two start **/
				if (dto.getSecondVehiImagePath() != null && !dto.getSecondVehiImagePath().isEmpty()
						&& !dto.getSecondVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto2." + extension;
					is = new FileInputStream(dto.getSecondVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setSecondVehiImagePath(destPath);
				}
				/** vehicle image two end **/

				/** vehicle image three start **/
				if (dto.getThirdVehiImagePath() != null && !dto.getThirdVehiImagePath().isEmpty()
						&& !dto.getThirdVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto3." + extension;
					is = new FileInputStream(dto.getThirdVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setThirdVehiImagePath(destPath);
				}
				/** vehicle image three end **/

				/** vehicle image four start **/
				if (dto.getForthVehiImagePath() != null && !dto.getForthVehiImagePath().isEmpty()
						&& !dto.getForthVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto4." + extension;
					is = new FileInputStream(dto.getForthVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setForthVehiImagePath(destPath);
				}
				/** vehicle image four end **/

				/** vehicle image five start **/
				if (dto.getFifthVehiImagePath() != null && !dto.getFifthVehiImagePath().isEmpty()
						&& !dto.getFifthVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto5." + extension;
					is = new FileInputStream(dto.getFifthVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setFifthVehiImagePath(destPath);
				}
				/** vehicle image five end **/

				/** vehicle image six start **/
				if (dto.getSixthVehiImagePath() != null && !dto.getSixthVehiImagePath().isEmpty()
						&& !dto.getSixthVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto6." + extension;
					is = new FileInputStream(dto.getSixthVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setSixthVehiImagePath(destPath);
				}
				/** vehicle image six end **/

			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return returnDTO;
	}

	@Override
	public VehicleInspectionDTO getCheckAmmendments(String queueNo) {
		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();
			String query;

			query = "SELECT que_number,  que_vehicle_no, que_application_no , que_permit_no FROM public.nt_m_queue_master where que_number= '"
					+ queueNo + "'" + " and to_char(created_date,'dd-MM-yyyy')='" + today + "'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				VehicleDTO.setApplicationNo(rs.getString("que_application_no"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setVehicleNo(rs.getString("que_vehicle_no"));
				VehicleDTO.setPermitNo(rs.getString("que_permit_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;
	}

	@Override
	public VehicleInspectionDTO getTaskDet(String applicationNo, String vehicleNo) {
		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();
			String query;

			query = "SELECT  tsd_task_code, tsd_status FROM public.nt_t_task_det where tsd_app_no=? or tsd_vehicle_no=? order by created_date desc limit 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			ps.setString(2, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleDTO.setTaskDetCode(rs.getString("tsd_task_code"));
				VehicleDTO.setTaskDetStatus(rs.getString("tsd_status"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;
	}

	@Override
	public int updateTranstractionTypeForAmmendments(String transtractionTypeCode, String currentAppNo,
			String loginUser) {
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int saved = -1;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_application SET pm_tran_type=?, pm_modified_by=? , pm_modified_date=?  WHERE pm_application_no=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, transtractionTypeCode);
			stmt.setString(2, loginUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, currentAppNo);
			int i = stmt.executeUpdate();

			if (i > 0) {
				saved = 0;
			} else {
				saved = -1;
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
	public boolean updateDataApplicationTable(VehicleInspectionDTO VehicleDTO, String neededApplicationNo,
			String loginUser) {
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		String date3 = null;
		String date1_2 = null;
		String expireDate = null;

		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (VehicleDTO.getCalender1() != null) {
			date1_2 = sdf.format(VehicleDTO.getCalender1());
		}

		if (VehicleDTO.getCalender2() != null) {
			date3 = sdf.format(VehicleDTO.getCalender2());
		}

		if (VehicleDTO.getExpireDate() != null) {
			expireDate = VehicleDTO.getExpireDate();
		}

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDataSave = false;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_application SET pm_queue_no=?, pm_permit_no=?, pm_vehicle_regno=?, pm_status=?, pm_service_type=?, "
					+ "pm_route_no=?, pm_next_ins_date_sec1_2=?, pm_next_ins_date_sec3=?, pm_inspect_remark=?, pm_modified_by=?, pm_modified_date=?,  "
					+ "pm_tot_bus_fare=?, pm_route_flag=?, pm_is_tender_permit=?, pm_permit_issue_date=?, pm_reinspec_status=?, pm_isnew_permit=?, pm_tran_type=?, "
					+ "pm_valid_to=?, pm_permit_expire_date=?, pm_is_backlog_app=? where pm_application_no=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, VehicleDTO.getQueueNo());
			stmt.setString(2, VehicleDTO.getPermitNo());
			stmt.setString(3, VehicleDTO.getVehicleNo());
			stmt.setString(4, "O");
			stmt.setString(5, VehicleDTO.getServiceTypeCode());
			stmt.setString(6, VehicleDTO.getRouteNo());
			if (date1_2 != null && !date1_2.trim().equalsIgnoreCase("") && !date1_2.isEmpty()) {
				stmt.setString(7, date1_2);
			} else {
				stmt.setNull(7, java.sql.Types.VARCHAR);
			}
			if (date3 != null && !date3.trim().equalsIgnoreCase("") && !date3.isEmpty()) {
				stmt.setString(8, date3);
			} else {
				stmt.setNull(8, java.sql.Types.VARCHAR);
			}
			stmt.setString(9, VehicleDTO.getFinalRemark());
			stmt.setString(10, VehicleDTO.getLoginUser());
			stmt.setTimestamp(11, timestamp);

			stmt.setBigDecimal(12, VehicleDTO.getBusFare());

			stmt.setString(13, VehicleDTO.getRouteFlag());
			stmt.setString(14, VehicleDTO.getTenderPermit());
			stmt.setString(15, VehicleDTO.getPermitIssueDate());
			stmt.setString(16, VehicleDTO.getInspectionStatus());
			stmt.setString(17, VehicleDTO.getIsNewPermit());
			stmt.setString(18, VehicleDTO.getTranstractionTypeCode());
			stmt.setString(19, VehicleDTO.getValidTo());
			if (expireDate != null && !expireDate.trim().equalsIgnoreCase("") && !expireDate.isEmpty()) {
				stmt.setString(20, expireDate);
			} else {
				stmt.setNull(20, java.sql.Types.VARCHAR);
			}
			stmt.setString(21, VehicleDTO.getBacklogStatus());
			stmt.setString(22, neededApplicationNo);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isDataSave = true;

			} else {
				isDataSave = false;
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
	public boolean updateDataVehicleOwnerWithApplicatioNo(VehicleInspectionDTO vehicleDTO, String neededApplicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isDataSave = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_vehi_owner SET  pmo_full_name=?, pmo_nic=?, pm_modified_by=?, pm_modified_date=? WHERE pmo_application_no=? and pmo_permit_no=? and pmo_vehicle_regno=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, vehicleDTO.getPermitOwner());
			stmt.setString(2, vehicleDTO.getNicreg());
			stmt.setString(3, vehicleDTO.getLoginUser());
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, vehicleDTO.getApplicationNo());
			stmt.setString(6, vehicleDTO.getPermitNo());
			stmt.setString(7, vehicleDTO.getVehicleNo());

			stmt.executeUpdate();

			int i = stmt.executeUpdate();

			if (i > 0) {
				isDataSave = true;
			} else {
				isDataSave = false;

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
	public boolean checkTaskDetDataExist(String vehicleNo, String applicationNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exist = false;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_t_task_det WHERE tsd_vehicle_no='" + vehicleNo
					+ "' AND tsd_app_no='" + applicationNo + "' AND tsd_task_code='PM100' AND tsd_status='C'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				exist = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return exist;
	}

	@Override
	public String applicationTaskCodeStatusPM400(String applicationNo) {
		String taskStatus = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String query = "select a.pm_application_no as pm_application_no"
					+ " from nt_t_pm_application a inner join nt_t_task_det b"
					+ " on a.pm_application_no = b.tsd_app_no inner join nt_m_queue_master d on d.que_number=a.pm_queue_no"
					+ " where a.pm_application_no ='" + applicationNo + "'"
					+ " and b.tsd_task_code = 'PM400'and b.tsd_status = 'C'"
					+ "and to_char(b.created_date,'dd-MM-yyyy')= '" + today + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				taskStatus = rs.getString("pm_application_no");

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
	public VehicleInspectionDTO getTaskDetForToday(String applicationNo, String vehicleNo) {
		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();
			String query;

			query = "SELECT  tsd_task_code, tsd_status\r\n"
					+ "FROM public.nt_t_task_det where tsd_app_no=? or tsd_vehicle_no=? \r\n"
					+ "and to_char(created_date,'dd-MM-yyyy')= '" + today + "'";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			ps.setString(2, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleDTO.setTaskDetCode(rs.getString("tsd_task_code"));
				VehicleDTO.setTaskDetStatus(rs.getString("tsd_status"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;
	}

	@Override
	public boolean saveAllDataWithApp(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO, String loginUser,
			OminiBusDTO ominiBusDTO, List<VehicleInspectionDTO> dataList, String taskoCode, String task) {
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		String date3 = null;
		String date1_2 = null;
		String expireDate = null;

		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (VehicleDTO.getCalender1() != null) {
			date1_2 = sdf.format(VehicleDTO.getCalender1());
		}

		if (VehicleDTO.getCalender2() != null) {
			date3 = sdf.format(VehicleDTO.getCalender2());
		}

		if (VehicleDTO.getExpireDate() != null) {
			expireDate = VehicleDTO.getExpireDate();
		}

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSaveInApp = false;
		boolean isDataSaveInVehi = false;
		boolean isDataSaveInOmni = false;
		boolean isDataSave = false;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_application");
			String sql = "insert into nt_t_pm_application "
					+ "(seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno,"
					+ "pm_status, pm_service_type, pm_route_no, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_created_by, pm_created_date,"
					+ "pm_is_backlog_app,pm_valid_to,pm_tot_bus_fare,pm_permit_expire_date,pm_route_flag,pm_is_tender_permit, pm_permit_issue_date,pm_reinspec_status,pm_isnew_permit, pm_tran_type) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, generatedapplicationNO);
			stmt.setString(3, VehicleDTO.getQueueNo());
			stmt.setString(4, VehicleDTO.getPermitNo());
			stmt.setString(5, VehicleDTO.getVehicleNo());
			stmt.setString(6, "O");
			stmt.setString(7, VehicleDTO.getServiceTypeCode());
			stmt.setString(8, VehicleDTO.getRouteNo());
			if (date1_2 != null && !date1_2.trim().equalsIgnoreCase("") && !date1_2.isEmpty()) {
				stmt.setString(9, date1_2);
			} else {
				stmt.setNull(9, java.sql.Types.VARCHAR);
			}
			if (date3 != null && !date3.trim().equalsIgnoreCase("") && !date3.isEmpty()) {
				stmt.setString(10, date3);
			} else {
				stmt.setNull(10, java.sql.Types.VARCHAR);
			}
			stmt.setString(11, VehicleDTO.getFinalRemark());
			stmt.setString(12, VehicleDTO.getLoginUser());
			stmt.setTimestamp(13, timestamp);
			stmt.setString(14, VehicleDTO.getBacklogStatus());
			stmt.setString(15, VehicleDTO.getValidTo());
			stmt.setBigDecimal(16, VehicleDTO.getBusFare());
			if (expireDate != null && !expireDate.trim().equalsIgnoreCase("") && !expireDate.isEmpty()) {
				stmt.setString(17, expireDate);
			} else {
				stmt.setNull(17, java.sql.Types.VARCHAR);
			}
			stmt.setString(18, VehicleDTO.getRouteFlag());
			stmt.setString(19, VehicleDTO.getTenderPermit());
			stmt.setString(20, VehicleDTO.getPermitIssueDate());
			stmt.setString(21, VehicleDTO.getInspectionStatus());
			stmt.setString(22, VehicleDTO.getIsNewPermit());
			stmt.setString(23, VehicleDTO.getTranstractionTypeCode());

			int i = stmt.executeUpdate();

			ConnectionManager.close(stmt);
			if (i > 0) {
				isDataSaveInApp = true;

				String query2 = "UPDATE public.nt_t_pm_application "
						+ "SET pm_status='I' , pm_modified_by=?, pm_modified_date=?" + "WHERE pm_application_no=?; ";

				ps = con.prepareStatement(query2);
				ps.setString(1, loginUser);
				ps.setTimestamp(2, timestamp);
				ps.setString(3, VehicleDTO.getOldApplicationNo());

				ps.executeUpdate();

				ConnectionManager.close(ps);

			} else {
				isDataSaveInApp = false;
			}
			if (isDataSaveInApp) {

				String sql1 = "INSERT INTO nt_t_pm_vehi_owner(seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, pm_created_by, pm_created_date"
						+ "					)SELECT nextval('seq_nt_t_pm_vehi_owner'), "
						+ "				   ?,pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, ?, ?"
						+ "				FROM nt_t_pm_vehi_owner WHERE pmo_application_no=?";

				stmt = con.prepareStatement(sql1);

				stmt.setString(1, generatedapplicationNO);
				stmt.setString(2, VehicleDTO.getCreateBy());
				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, VehicleDTO.getApplicationNo());

				int i2 = stmt.executeUpdate();
				ConnectionManager.close(stmt);
				if (i2 > 0) {
					isDataSaveInVehi = true;
				} else {
					isDataSaveInVehi = false;
				}

				if (isDataSaveInVehi) {
					String sql2 = "INSERT INTO nt_t_pm_omini_bus1(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name"
							+ "					)SELECT nextval('seq_nt_t_pm_omini_bus1'), "
							+ "				   ?, pmb_vehicle_regno, pmb_engine_no, ?, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, ?, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name "
							+ "				FROM nt_t_pm_omini_bus1 WHERE pmb_application_no=?";

					stmt = con.prepareStatement(sql2);

					stmt.setString(1, generatedapplicationNO);
					stmt.setString(2, ominiBusDTO.getChassisNo());
					stmt.setString(3, ominiBusDTO.getManufactureDate());
					stmt.setString(4, ominiBusDTO.getApplicationNo());

					int i3 = stmt.executeUpdate();
					ConnectionManager.close(stmt);
					if (i3 > 0) {
						isDataSaveInOmni = true;
					}
					if (isDataSaveInOmni) {
						boolean queueUpdate1 = false;
						boolean queueUpdate2 = false;
						boolean queueUpdate3 = false;
						boolean vehiImageSave = saveDataVehicleImagesWithApplicatioNoNew(VehicleDTO,
								generatedapplicationNO, loginUser, con);

						boolean saveInspectDet = saveDataVehicleInspecDetailsNew(VehicleDTO, dataList,
								generatedapplicationNO, con);
						boolean saveInspectDetHis = saveDataVehicleInspecDetailsHistoryNew(VehicleDTO, dataList,
								generatedapplicationNO, con);
						if (vehiImageSave && saveInspectDet && saveInspectDetHis) {

							// update queMaster Data
							queueUpdate1 = updateStatusOfQueApp(con, VehicleDTO.getQueueNo(), generatedapplicationNO);
							queueUpdate2 = updateTransactionTypeCodeForQueueNo(con, VehicleDTO.getQueueNo(), "02");
							queueUpdate3 = updateQueueNumberTaskInQueueMaster(con, VehicleDTO.getQueueNo(),
									generatedapplicationNO, "PM100", "C");
							//update number generation 
							updateNumberGeneration(con, generatedapplicationNO, loginUser,"PAP");
						}
						if (queueUpdate1 && queueUpdate2 && queueUpdate3) {
							con.commit();
							isDataSave = true;
						}

					}

				}

			}

		} catch (Exception ex) {

			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return isDataSave;

	}

	@Override
	public boolean saveAllDataWithoutAppNo(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO,
			String loginUser, OminiBusDTO ominiBusDTO, List<VehicleInspectionDTO> dataList, String taskoCode,
			String task) {
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		String date3 = null;
		String date1_2 = null;
		String expireDate = null;

		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (VehicleDTO.getCalender1() != null) {
			date1_2 = sdf.format(VehicleDTO.getCalender1());
		}

		if (VehicleDTO.getCalender2() != null) {
			date3 = sdf.format(VehicleDTO.getCalender2());
		}

		if (VehicleDTO.getExpireDate() != null) {
			expireDate = VehicleDTO.getExpireDate();
		}

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSaveInApp = false;
		boolean isDataSaveInVehi = false;
		boolean isDataSaveInOmni = false;
		boolean isDataSave = false;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_application");
			String sql = "insert into nt_t_pm_application "
					+ "(seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno,"
					+ "pm_status, pm_service_type, pm_route_no, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_created_by, pm_created_date,"
					+ "pm_is_backlog_app,pm_valid_to,pm_tot_bus_fare,pm_permit_expire_date,pm_route_flag,pm_is_tender_permit, pm_permit_issue_date,pm_reinspec_status,pm_isnew_permit, pm_tran_type) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, generatedapplicationNO);
			stmt.setString(3, VehicleDTO.getQueueNo());
			stmt.setString(4, VehicleDTO.getPermitNo());
			stmt.setString(5, VehicleDTO.getVehicleNo());
			stmt.setString(6, "O");
			stmt.setString(7, VehicleDTO.getServiceTypeCode());
			stmt.setString(8, VehicleDTO.getRouteNo());
			if (date1_2 != null && !date1_2.trim().equalsIgnoreCase("") && !date1_2.isEmpty()) {
				stmt.setString(9, date1_2);
			} else {
				stmt.setNull(9, java.sql.Types.VARCHAR);
			}
			if (date3 != null && !date3.trim().equalsIgnoreCase("") && !date3.isEmpty()) {
				stmt.setString(10, date3);
			} else {
				stmt.setNull(10, java.sql.Types.VARCHAR);
			}
			stmt.setString(11, VehicleDTO.getFinalRemark());
			stmt.setString(12, VehicleDTO.getLoginUser());
			stmt.setTimestamp(13, timestamp);
			stmt.setString(14, VehicleDTO.getBacklogStatus());
			stmt.setString(15, VehicleDTO.getValidTo());
			stmt.setBigDecimal(16, VehicleDTO.getBusFare());
			if (expireDate != null && !expireDate.trim().equalsIgnoreCase("") && !expireDate.isEmpty()) {
				stmt.setString(17, expireDate);
			} else {
				stmt.setNull(17, java.sql.Types.VARCHAR);
			}
			stmt.setString(18, VehicleDTO.getRouteFlag());
			stmt.setString(19, VehicleDTO.getTenderPermit());
			stmt.setString(20, VehicleDTO.getPermitIssueDate());
			stmt.setString(21, VehicleDTO.getInspectionStatus());
			stmt.setString(22, VehicleDTO.getIsNewPermit());
			stmt.setString(23, VehicleDTO.getTranstractionTypeCode());

			int i = stmt.executeUpdate();

			ConnectionManager.close(stmt);
			if (i > 0) {
				isDataSaveInApp = true;

				String query2 = "UPDATE public.nt_t_pm_application "
						+ "SET pm_status='I' , pm_modified_by=?, pm_modified_date=?" + "WHERE pm_application_no=?; ";

				ps = con.prepareStatement(query2);
				ps.setString(1, loginUser);
				ps.setTimestamp(2, timestamp);
				ps.setString(3, VehicleDTO.getOldApplicationNo());

				ps.executeUpdate();

				ConnectionManager.close(ps);

			} else {
				isDataSaveInApp = false;
			}
			if (isDataSaveInApp) {

				String sql2 = "insert into nt_t_pm_vehi_owner"
						+ "(seq,pmo_application_no,pmo_permit_no,pmo_vehicle_regno,pmo_full_name,pmo_nic,pm_created_by,pm_created_date,pmo_is_backlog_app)"
						+ "values (?,?,?,?,?,?,?,?,?)";

				stmt = con.prepareStatement(sql2);
				long seqNo2 = Utils.getNextValBySeqName(con, "seq_nt_t_pm_vehi_owner");

				stmt.setLong(1, seqNo2);
				stmt.setString(2, generatedapplicationNO);
				stmt.setString(3, VehicleDTO.getPermitNo());
				stmt.setString(4, VehicleDTO.getVehicleNo());
				stmt.setString(5, VehicleDTO.getPermitOwner());
				stmt.setString(6, VehicleDTO.getNicreg());
				stmt.setString(7, VehicleDTO.getLoginUser());
				stmt.setTimestamp(8, timestamp);
				stmt.setString(9, "N");

				int i2 = stmt.executeUpdate();
				ConnectionManager.close(stmt);
				if (i2 > 0) {
					isDataSaveInVehi = true;
				} else {
					isDataSaveInVehi = false;
				}

				if (isDataSaveInVehi) {

					long seqNo3 = Utils.getNextValBySeqName(con, "seq_nt_t_pm_omini_bus1");

					String sql3 = "INSERT INTO public.nt_t_pm_omini_bus1"
							+ "(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no,"
							+ " pmb_chassis_no, pmb_make, pmb_model,pmb_production_date, "
							+ " pmb_first_reg_date, pmb_seating_capacity, pmb_no_of_doors,"
							+ " pmb_weight,pmb_is_backlog_app,pmb_permit_no,pmb_created_by, pmb_created_date)"
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

					stmt = con.prepareStatement(sql3);

					stmt.setLong(1, seqNo3);
					stmt.setString(2, ominiBusDTO.getApplicationNo());
					stmt.setString(3, ominiBusDTO.getVehicleRegNo());
					stmt.setString(4, ominiBusDTO.getEngineNo());
					stmt.setString(5, ominiBusDTO.getChassisNo());
					stmt.setString(6, ominiBusDTO.getMake());
					stmt.setString(7, ominiBusDTO.getModel());
					stmt.setString(8, ominiBusDTO.getManufactureDate());

					if (ominiBusDTO.getRegistrationDate() != null) {
						String registrationDate = (dateFormat.format(ominiBusDTO.getRegistrationDate()));
						stmt.setString(9, registrationDate);
					} else {
						stmt.setString(9, null);
					}
					stmt.setString(10, ominiBusDTO.getSeating());
					stmt.setString(11, ominiBusDTO.getNoofDoors());
					stmt.setString(12, ominiBusDTO.getWeight());
					stmt.setString(13, ominiBusDTO.getIsBacklogApp());
					stmt.setString(14, ominiBusDTO.getPermitNo());
					stmt.setString(15, ominiBusDTO.getCreatedBy());
					stmt.setTimestamp(16, timestamp);

					int i3 = stmt.executeUpdate();
					ConnectionManager.close(stmt);
					if (i3 > 0) {
						isDataSaveInOmni = true;
					}
					if (isDataSaveInOmni) {
						boolean queueUpdate1 = false;
						boolean queueUpdate2 = false;
						boolean queueUpdate3 = false;
						boolean saveInspectDet = saveDataVehicleInspecDetailsNew(VehicleDTO, dataList,
								generatedapplicationNO, con);
						boolean saveDetHistory = saveDataVehicleInspecDetailsHistoryNew(VehicleDTO, dataList,
								generatedapplicationNO, con);
						if (saveInspectDet && saveDetHistory) {

							// update queMaster Data
							queueUpdate1 = updateStatusOfQueApp(con, VehicleDTO.getQueueNo(), generatedapplicationNO);
							queueUpdate2 = updateTransactionTypeCodeForQueueNo(con, VehicleDTO.getQueueNo(), "02");
							queueUpdate3 = updateQueueNumberTaskInQueueMaster(con, VehicleDTO.getQueueNo(),
									generatedapplicationNO, "PM100", "C");
							//update numbr generaion
							updateNumberGeneration(con, generatedapplicationNO, loginUser,"PAP");
						}
						if (queueUpdate1 && queueUpdate2 && queueUpdate3) {
							con.commit();
							isDataSave = true;
						}
					}

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isDataSave;

	}

	public boolean saveDataVehicleImagesWithApplicatioNoNew(VehicleInspectionDTO vehicleDTO,
			String generatedApplicationNO, String loginUser, Connection con) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		PreparedStatement stmt = null;
		ResultSet rs = null;
		UploadImageDTO dto = null;
		boolean save = false;
		try {

			/** select vehi images for old app number start **/
			String select = "SELECT ovi_app_no, ovi_owner_image, ovi_image1, ovi_image2, ovi_image3, ovi_image4, ovi_image5, ovi_image6, "
					+ " ovi_status " + "FROM public.nt_t_owner_vehi_images where ovi_app_no='"
					+ vehicleDTO.getApplicationNo() + "'";

			stmt = con.prepareStatement(select);
			rs = stmt.executeQuery();

			while (rs.next()) {
				dto = new UploadImageDTO();
				dto.setApplicationNo(rs.getString("ovi_app_no"));
				dto.setFirstVehiImagePath(rs.getString("ovi_image1"));
				dto.setSecondVehiImagePath(rs.getString("ovi_image2"));
				dto.setThirdVehiImagePath(rs.getString("ovi_image3"));
				dto.setForthVehiImagePath(rs.getString("ovi_image4"));
				dto.setFifthVehiImagePath(rs.getString("ovi_image5"));
				dto.setSixthVehiImagePath(rs.getString("ovi_image6"));
				dto.setVehicleOwnerPhotoPath(rs.getString("ovi_owner_image"));

			}

			ConnectionManager.close(stmt);
			/** select vehi images for old app number end **/

			/** insert vehi images start **/
			if (dto != null) {
				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_owner_vehi_images");

				/** save images in physical path start **/
				UploadImageDTO newPhotosDTO = saveImagesToPhysicalPath(dto, generatedApplicationNO);
				/** save images in physical path end **/

				String sql = "INSERT INTO public.nt_t_owner_vehi_images "
						+ "(ovi_seq_no, ovi_app_no, ovi_owner_image, ovi_image1, ovi_image2, ovi_image3, ovi_image4, ovi_image5, ovi_image6, "
						+ "ovi_modified_by, ovi_modified_date, ovi_status) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, generatedApplicationNO);
				stmt.setString(3, newPhotosDTO.getVehicleOwnerPhotoPath());
				stmt.setString(4, newPhotosDTO.getFirstVehiImagePath());
				stmt.setString(5, newPhotosDTO.getSecondVehiImagePath());
				stmt.setString(6, newPhotosDTO.getThirdVehiImagePath());
				stmt.setString(7, newPhotosDTO.getForthVehiImagePath());
				stmt.setString(8, newPhotosDTO.getFifthVehiImagePath());
				stmt.setString(9, newPhotosDTO.getSixthVehiImagePath());
				stmt.setString(10, loginUser);
				stmt.setTimestamp(11, timestamp);
				stmt.setString(12, "A");

				int i = stmt.executeUpdate();

				if (i > 0) {
					save = true;
				}

			}
			/** insert vehi images end **/

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);

		}
		return save;

	}

	public boolean saveDataVehicleInspecDetailsNew(VehicleInspectionDTO VehicleDTO, List<VehicleInspectionDTO> dataList,
			String generatedapplicationNO, Connection con) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDataSave = false;
		String exitTest = null;

		try {

			for (int i = 0; i < dataList.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_vehicle_inspec_det");

				String sql = "INSERT INTO public.nt_t_vehicle_inspec_det "
						+ "(vid_seq_no, vid_app_no, vid_sec_seq, vid_remark, vid_exist, vid_created_by, vid_created_date) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, generatedapplicationNO);
				stmt.setInt(3, dataList.get(i).getNewGridNo());

				if (dataList.get(i).getRemark() != null) {
					stmt.setString(4, dataList.get(i).getRemark().trim());
				} else {
					logger.info("UPDATING NULL VALUES");
					stmt.setNull(4, java.sql.Types.VARCHAR);
				}

				if (dataList.get(i).isExists() == true) {
					exitTest = "YES";
				} else {
					exitTest = "NO";
				}
				stmt.setString(5, exitTest);
				stmt.setString(6, VehicleDTO.getLoginUser());
				stmt.setTimestamp(7, timestamp);

				int c = stmt.executeUpdate();

				if (c > 0) {
					isDataSave = true;
					logger.info("Inserting vehicle inspection........");
				} else {
					isDataSave = false;
					logger.error("ERROR........");
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);

		}
		return isDataSave;
	}

	public boolean saveDataVehicleInspecDetailsHistoryNew(VehicleInspectionDTO vehicleDTO,
			List<VehicleInspectionDTO> dataList, String generatedApplicationNO, Connection con) {

//		int versionNo = getVersionNumberVehicleInspecDetailsHistory(generatedApplicationNO);
		int versionNo = getVersionNumberVehicleInspecDetailsHistoryNew(generatedApplicationNO,con);
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDataSave = false;
		String exitTest = null;

		try {

			for (int i = 0; i < dataList.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_vehicle_inspec_det");

				String sql = "INSERT INTO public.nt_h_vehicle_inspec_det "
						+ "(vid_seq_no, vid_app_no, vid_sec_seq, vid_remark, vid_exist, vid_created_by, vid_created_date,vid_version) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, generatedApplicationNO);
				stmt.setInt(3, dataList.get(i).getNewGridNo());

				if (dataList.get(i).getRemark() != null) {
					stmt.setString(4, dataList.get(i).getRemark().trim());
				} else {
					logger.info("UPDATING NULL VALUES");
					stmt.setNull(4, java.sql.Types.VARCHAR);
				}

				if (dataList.get(i).isExists() == true) {
					exitTest = "YES";
				} else {
					exitTest = "NO";
				}
				stmt.setString(5, exitTest);
				stmt.setString(6, vehicleDTO.getLoginUser());
				stmt.setTimestamp(7, timestamp);
				stmt.setInt(8, versionNo);

				int c = stmt.executeUpdate();

				if (c > 0) {
					isDataSave = true;
					logger.info("Inserting vehicle inspection........");
				} else {
					isDataSave = false;
					logger.error("ERROR........");
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);

		}
		return isDataSave;

	}

	@Override
	public String getTokenType(String queueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		String tokenType = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select que_inspection_type  from nt_m_queue_master where que_number=? and que_date like '"
					+ today + "%'";

			ps = con.prepareStatement(query);
			ps.setString(1, queueNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				tokenType = (rs.getString("que_inspection_type"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tokenType;
	}

	@Override
	public VehicleInspectionDTO searchForOtherInspection(String queueNo) {

		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();
			String query;

			String backlogStatus = isbacklog(queueNo);

			if (backlogStatus != null && !backlogStatus.isEmpty() && !backlogStatus.trim().equalsIgnoreCase("")
					&& backlogStatus.equalsIgnoreCase("Y")) {
				query = "select distinct a.pm_isnew_permit,a.pm_inspect_remark,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type,"
						+ " b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
						+ " g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_valid_to,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit"
						+ " from nt_t_pm_application a , nt_m_queue_master d,nt_r_route b ,nt_r_service_types c, nt_t_pm_omini_bus1 e,nt_r_make f,nt_r_model g, nt_t_pm_vehi_owner h"
						+ " where a.pm_vehicle_regno = d.que_vehicle_no" + " and a.pm_route_no = b.rou_number "
						+ " and a.pm_service_type= c.code " + " and a.pm_application_no = e.pmb_application_no "
						+ " and e.pmb_make=f.code " + " and  e.pmb_model = g.mod_code "
						+ " and a.pm_application_no = h.pmo_application_no " + " and a.pm_status='A'"
						+ " and d.que_number= '" + queueNo + "'" + " and to_char(d.created_date,'dd-MM-yyyy')='" + today
						+ "'";
			} else {

				query = "select distinct a.pm_isnew_permit,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_valid_to,a.pm_inspect_remark,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type, "
						+ " b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
						+ " g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit "
						+ " from public.nt_t_pm_application a left outer join public.nt_r_route b on a.pm_route_no = b.rou_number "
						+ " left outer join public.nt_r_service_types c on a.pm_service_type= c.code "
						+ " left outer join public.nt_m_queue_master d on a.pm_application_no = d.que_application_no "
						+ " left outer join nt_t_pm_omini_bus1 e on a.pm_application_no = e.pmb_application_no "
						+ " left outer join nt_r_make f on e.pmb_make=f.code "
						+ " left outer join nt_r_model g on e.pmb_model = g.mod_code "
						+ " left outer join nt_t_pm_vehi_owner h on a.pm_application_no = h.pmo_application_no "
						+ " where d.que_number='" + queueNo + "' and to_char(d.created_date,'dd-MM-yyyy')= '" + today
						+ "'";

			}

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				VehicleDTO.setIsNewPermit(rs.getString("pm_isnew_permit"));
				VehicleDTO.setFinalRemark(rs.getString("pm_inspect_remark"));
				VehicleDTO.setInspectionStatus(rs.getString("pm_reinspec_status"));
				VehicleDTO.setDate1(rs.getString("pm_next_ins_date_sec1_2"));
				VehicleDTO.setDate2(rs.getString("pm_next_ins_date_sec3"));
				VehicleDTO.setRouteFlag(rs.getString("pm_route_flag"));
				VehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setOldApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setPermitSeqNo(rs.getLong("seq"));
				VehicleDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				VehicleDTO.setManyear(rs.getString("pmb_production_date"));
				VehicleDTO.setMakeDescription(rs.getString("make"));
				VehicleDTO.setMakeTypeCode(rs.getString("pmb_make"));
				VehicleDTO.setModelTypeCode(rs.getString("pmb_model"));
				VehicleDTO.setModelDescription(rs.getString("mod_description"));
				VehicleDTO.setNicreg(rs.getString("pmo_nic"));
				VehicleDTO.setPermitNo(rs.getString("pm_permit_no"));
				VehicleDTO.setPermitOwner(rs.getString("pmo_full_name"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setRouteDetails(rs.getString("rou_description"));
				VehicleDTO.setRouteNo(rs.getString("pm_route_no"));
				VehicleDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				VehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				VehicleDTO.setOrigine(rs.getString("rou_service_origine"));
				VehicleDTO.setDestination(rs.getString("rou_service_destination"));
				VehicleDTO.setRouteVia(rs.getString("rou_via"));
				VehicleDTO.setBacklogApp(true);
				VehicleDTO.setValidTo(rs.getString("pm_valid_to"));
				VehicleDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				VehicleDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				VehicleDTO.setTenderPermit(rs.getString("pm_is_tender_permit"));
				VehicleDTO.setBacklogStatus(backlogStatus);
				VehicleDTO.setPermitIssueDate(rs.getString("pm_permit_issue_date"));

			}

			if (!rs.isBeforeFirst()) {
				VehicleDTO.setQueueNo(queueNo);
				searchQueueTable(VehicleDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;

	}

	@Override
	public List<VehicleInspectionDTO> getInspectionLocationList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<VehicleInspectionDTO> returnList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT loc_code, loc_description,loc_order" + " FROM public.nt_r_inspection_location"
					+ " where  loc_status ='A'" + " order by loc_order ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setInspecLocationCode(rs.getString("loc_code"));
				vehicleDTO.setInspecLocationDes(rs.getString("loc_description"));
				vehicleDTO.setInspectionLocationorder(rs.getInt("loc_order"));

				returnList.add(vehicleDTO);

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

	/* Other inspection CR start here */

	@Override
	public VehicleInspectionDTO getVehicleInformation(String queueNo, String applicationNo, boolean isViewEditMode) {

		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {

			con = ConnectionManager.getConnection();

			if (isViewEditMode) {

				String query = "select a.pm_inspection_status, a.inspection_location, a.pm_vehicle_regno, a.pm_isnew_permit,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_valid_to,a.pm_inspect_remark,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type,a.proceed_remark as proceedremark, "
						+ " b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
						+ " g.mod_description, d.que_inspection_type, d.task_status, d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit "
						+ " from public.nt_t_pm_application a left outer join public.nt_r_route b on a.pm_route_no = b.rou_number "
						+ " left outer join public.nt_r_service_types c on a.pm_service_type= c.code "
						+ " left outer join public.nt_m_queue_master d on a.pm_application_no = d.que_application_no  "
						+ " left outer join nt_t_pm_omini_bus1 e on a.pm_application_no = e.pmb_application_no "
						+ " left outer join nt_r_make f on e.pmb_make=f.code "
						+ " left outer join nt_r_model g on e.pmb_model = g.mod_code "
						+ " left outer join nt_t_pm_vehi_owner h on a.pm_application_no = h.pmo_application_no "
						+ " where a.pm_application_no =? order by d.created_date desc limit 1";

				ps = con.prepareStatement(query);
				ps.setString(1, applicationNo);

			} else {
				String query = "select a.pm_inspection_status, a.inspection_location, a.pm_vehicle_regno, a.pm_isnew_permit,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_valid_to,a.pm_inspect_remark,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type, a.proceed_remark as proceedremark, "
						+ " b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
						+ " g.mod_description, d.que_inspection_type, d.task_status, d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit "
						+ " from public.nt_t_pm_application a left outer join public.nt_r_route b on a.pm_route_no = b.rou_number "
						+ " left outer join public.nt_r_service_types c on a.pm_service_type= c.code "
						+ " left outer join public.nt_m_queue_master d on a.pm_permit_no = d.que_permit_no "
						+ " left outer join nt_t_pm_omini_bus1 e on a.pm_application_no = e.pmb_application_no "
						+ " left outer join nt_r_make f on e.pmb_make=f.code "
						+ " left outer join nt_r_model g on e.pmb_model = g.mod_code "
						+ " left outer join nt_t_pm_vehi_owner h on a.pm_application_no = h.pmo_application_no "
						+ " where d.que_number='" + queueNo + "' and to_char(d.created_date,'dd-MM-yyyy')= '" + today
						+ "' and a.pm_application_no =? order by d.created_date desc limit 1";

				ps = con.prepareStatement(query);
				ps.setString(1, applicationNo);
			}

			rs = ps.executeQuery();

			if (rs.next()) {
				VehicleDTO.setDataFound(true);
				VehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				VehicleDTO.setIsNewPermit(rs.getString("pm_isnew_permit"));
				VehicleDTO.setFinalRemark(rs.getString("pm_inspect_remark"));
				VehicleDTO.setInspectionStatus(rs.getString("pm_reinspec_status"));
				VehicleDTO.setDate1(rs.getString("pm_next_ins_date_sec1_2"));
				VehicleDTO.setDate2(rs.getString("pm_next_ins_date_sec3"));
				VehicleDTO.setRouteFlag(rs.getString("pm_route_flag"));
				VehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setOldApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setPermitSeqNo(rs.getLong("seq"));
				VehicleDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				VehicleDTO.setManyear(rs.getString("pmb_production_date"));
				VehicleDTO.setMakeDescription(rs.getString("make"));
				VehicleDTO.setMakeTypeCode(rs.getString("pmb_make"));
				VehicleDTO.setModelTypeCode(rs.getString("pmb_model"));
				VehicleDTO.setModelDescription(rs.getString("mod_description"));
				VehicleDTO.setNicreg(rs.getString("pmo_nic"));
				VehicleDTO.setPermitNo(rs.getString("pm_permit_no"));
				VehicleDTO.setPermitOwner(rs.getString("pmo_full_name"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setRouteDetails(rs.getString("rou_description"));
				VehicleDTO.setRouteNo(rs.getString("pm_route_no"));
				VehicleDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				VehicleDTO.setOrigine(rs.getString("rou_service_origine"));
				VehicleDTO.setDestination(rs.getString("rou_service_destination"));
				VehicleDTO.setRouteVia(rs.getString("rou_via"));
				VehicleDTO.setValidTo(rs.getString("pm_valid_to"));
				VehicleDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				VehicleDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				VehicleDTO.setTenderPermit(rs.getString("pm_is_tender_permit"));
				VehicleDTO.setPermitIssueDate(rs.getString("pm_permit_issue_date"));
				VehicleDTO.setProceedRemark(rs.getString("proceedremark"));
				VehicleDTO.setInspectionTypeCode(rs.getString("que_inspection_type"));
				String type = rs.getString("que_inspection_type");

				if (type.equals("CI")) {
					VehicleDTO.setInspectionTypeDes("COMPLAIN");
				} else if (type.equals("II")) {
					VehicleDTO.setInspectionTypeDes("INQUIRY");
				} else if (type.equals("SI")) {
					VehicleDTO.setInspectionTypeDes("SITE VISITS");
				}

				String inspectionStatus = rs.getString("pm_inspection_status");
				if (inspectionStatus != null) {
					VehicleDTO.setInspectionStatusCode(inspectionStatus);
				} else {
					VehicleDTO.setInspectionStatusCode("I");
				}

				String inspectionLocation = rs.getString("inspection_location");
				if (inspectionLocation != null) {
					VehicleDTO.setInspecLocationCode(inspectionLocation);
				} else {
					VehicleDTO.setInspecLocationCode("NTC");
				}

			} else {
				VehicleDTO.setDataFound(false);
			}

			if (!rs.isBeforeFirst()) {
				VehicleDTO.setQueueNo(queueNo);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;
	}

	@Override
	public VehicleInspectionDTO getLatestApplicationNo(String permitNo) {
		logger.info("other inspection find application no for call next start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		VehicleInspectionDTO dto = new VehicleInspectionDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = "select pm_application_no from nt_t_pm_application "
					+ "where pm_permit_no = ? order by pm_created_date desc limit 1;";

			ps = con.prepareStatement(sql);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			if (rs.next()) {

				dto.setApplicationNoFound(true);
				dto.setApplicationNo(rs.getString("pm_application_no"));

			} else {
				dto.setApplicationNoFound(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("other inspection find application no for call next end");
		return dto;
	}

	@Override
	public String getPermitNo(String queueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);
		logger.info("other inspection find permit no for search end");

		String permitNo = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select que_permit_no  from nt_m_queue_master where que_number=? and que_date like '" + today
					+ "%'";

			ps = con.prepareStatement(query);
			ps.setString(1, queueNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitNo = (rs.getString("que_permit_no"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("other inspection find permit no for search end");
		return permitNo;
	}

	@Override
	public boolean saveOtherVehicleInspection(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO,
			String loginUser, OminiBusDTO ominiBusDTO, List<VehicleInspectionDTO> dataList) {

		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		String date3 = null;
		String date1_2 = null;
		String expireDate = null;

		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		if (VehicleDTO.getCalender1() != null) {
			date1_2 = sdf.format(VehicleDTO.getCalender1());
		}

		if (VehicleDTO.getCalender2() != null) {
			date3 = sdf.format(VehicleDTO.getCalender2());
		}

		if (VehicleDTO.getExpireDate() != null) {
			expireDate = VehicleDTO.getExpireDate();
		}

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDataSaveInVehi = false;
		boolean isDataSaveInOmni = false;
		boolean isDataSave = false;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_application");
			String sql = "insert into nt_t_pm_application "
					+ "(seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno,"
					+ "pm_status, pm_service_type, pm_route_no, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, "
					+ "pm_created_by, pm_created_date,pm_is_backlog_app,pm_valid_to,pm_tot_bus_fare,pm_permit_expire_date, "
					+ "pm_route_flag,pm_is_tender_permit, pm_permit_issue_date,pm_reinspec_status,pm_isnew_permit, pm_tran_type, "
					+ "pm_inspection_status, inspection_type, inspection_location, proceed_status) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, generatedapplicationNO);
			stmt.setString(3, VehicleDTO.getQueueNo());
			stmt.setString(4, VehicleDTO.getPermitNo());
			stmt.setString(5, VehicleDTO.getVehicleNo());
			stmt.setString(6, "G");
			stmt.setString(7, VehicleDTO.getServiceTypeCode());
			stmt.setString(8, VehicleDTO.getRouteNo());
			if (date1_2 != null && !date1_2.trim().equalsIgnoreCase("") && !date1_2.isEmpty()) {
				stmt.setString(9, date1_2);
			} else {
				stmt.setNull(9, java.sql.Types.VARCHAR);
			}
			if (date3 != null && !date3.trim().equalsIgnoreCase("") && !date3.isEmpty()) {
				stmt.setString(10, date3);
			} else {
				stmt.setNull(10, java.sql.Types.VARCHAR);
			}
			stmt.setString(11, VehicleDTO.getFinalRemark());
			stmt.setString(12, VehicleDTO.getLoginUser());
			stmt.setTimestamp(13, timestamp);
			stmt.setString(14, "N");
			stmt.setString(15, VehicleDTO.getValidTo());
			stmt.setBigDecimal(16, VehicleDTO.getBusFare());
			if (expireDate != null && !expireDate.trim().equalsIgnoreCase("") && !expireDate.isEmpty()) {
				stmt.setString(17, expireDate);
			} else {
				stmt.setNull(17, java.sql.Types.VARCHAR);
			}
			stmt.setString(18, VehicleDTO.getRouteFlag());
			stmt.setString(19, VehicleDTO.getTenderPermit());
			stmt.setString(20, VehicleDTO.getPermitIssueDate());
			stmt.setString(21, VehicleDTO.getInspectionStatus());
			stmt.setString(22, VehicleDTO.getIsNewPermit());
			stmt.setString(23, VehicleDTO.getTranstractionTypeCode());

			stmt.setString(24, VehicleDTO.getInspectionStatusCode());
			stmt.setString(25, VehicleDTO.getInspectionTypeCode());
			stmt.setString(26, VehicleDTO.getInspecLocationCode());

			if (VehicleDTO.getInspectionStatusCode().equals("I")) {
				stmt.setString(27, "N");
			} else {
				stmt.setString(27, null);
			}

			int i = stmt.executeUpdate();

			ConnectionManager.close(stmt);

			if (i > 0) {

				String query3 = "INSERT INTO nt_t_pm_vehi_owner(seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, pm_created_by, pm_created_date"
						+ "					)SELECT nextval('seq_nt_t_pm_vehi_owner'), "
						+ "				   ?,pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, ?, ?"
						+ "				FROM nt_t_pm_vehi_owner WHERE pmo_application_no=?";

				stmt = con.prepareStatement(query3);

				stmt.setString(1, generatedapplicationNO);
				stmt.setString(2, VehicleDTO.getCreateBy());
				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, VehicleDTO.getOldApplicationNo());

				int i2 = stmt.executeUpdate();
				ConnectionManager.close(stmt);
				if (i2 > 0) {
					isDataSaveInVehi = true;
				} else {
					isDataSaveInVehi = false;
				}

				if (isDataSaveInVehi) {			
					/*
					 * Removed extra pmb_emission_test_exp_date, pmb_garage_name from query4 select part
					 * dhananjika.d on (13/06/2024)
					 */
					
					String query4 = "INSERT INTO nt_t_pm_omini_bus1(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name"
							+ "					)SELECT nextval('seq_nt_t_pm_omini_bus1'), "
							+ "				   ?, pmb_vehicle_regno, pmb_engine_no, ?, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, ?, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name "
							+ "				FROM nt_t_pm_omini_bus1 WHERE pmb_application_no=?";

					stmt = con.prepareStatement(query4);
					stmt.setString(1, generatedapplicationNO);
					stmt.setString(2, ominiBusDTO.getChassisNo());
					stmt.setString(3, ominiBusDTO.getManufactureDate());
					stmt.setString(4, ominiBusDTO.getOldApplicationNo());

					int i3 = stmt.executeUpdate();
					ConnectionManager.close(stmt);
					if (i3 > 0) {
						isDataSaveInOmni = true;
					}
					if (isDataSaveInOmni) {

						saveDataVehicleImagesWithApplicatioNoNew(VehicleDTO, generatedapplicationNO, loginUser, con);
						saveDataVehicleInspecDetailsNew(VehicleDTO, dataList, generatedapplicationNO, con);
						saveDataVehicleInspecDetailsHistoryNew(VehicleDTO, dataList, generatedapplicationNO, con);
						updateNumberGeneration(con, generatedapplicationNO, loginUser,"OAP");
						con.commit();
						isDataSave = true;

					}
				}

			}

		} catch (Exception ex) {

			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return isDataSave;

	}

	@Override
	public void updateQueueMasterStatus(String queueNo, VehicleInspectionDTO dto, String status, String taskCode,
			String taskStatus, String loginUser, boolean isApplicationUpdate) {

		logger.info("other inspection update queueNo status start");

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String today = df.format(date);
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			if (isApplicationUpdate) {

				String sql = "UPDATE public.nt_m_queue_master SET  task_status=?, que_application_no = ?, que_task_code =?, "
						+ "que_task_status =?, modified_by=? , modified_date=? "
						+ "WHERE que_number=? and que_permit_no =? and to_char(created_date,'dd-MM-yyyy')=? ;";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, status);
				stmt.setString(2, dto.getApplicationNo());
				stmt.setString(3, taskCode);
				stmt.setString(4, taskStatus);
				stmt.setString(5, loginUser);
				stmt.setTimestamp(6, timestamp);
				stmt.setString(7, queueNo);
				stmt.setString(8, dto.getPermitNo());
				stmt.setString(9, today);

			} else if (isApplicationUpdate == false) {

				String sql = "UPDATE public.nt_m_queue_master SET  task_status=?, que_task_code =?, "
						+ "que_task_status =?, modified_by=? , modified_date=? "
						+ "WHERE que_number=? and que_permit_no =? and to_char(created_date,'dd-MM-yyyy')=? ;";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, status);
				stmt.setString(2, taskCode);
				stmt.setString(3, taskStatus);
				stmt.setString(4, loginUser);
				stmt.setTimestamp(5, timestamp);
				stmt.setString(6, queueNo);
				stmt.setString(7, dto.getPermitNo());
				stmt.setString(8, today);

			}

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("other inspection update queueNo status error - > " + ex);

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		logger.info("other inspection update queueNo status end");
	}

	@Override
	public String generateOtherInspectionApplicationNo() {
		logger.info("other inspection generate new application no start");
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

			String sql = "SELECT app_no FROM public.nt_r_number_generation where code='OAP' ;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("app_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "OAP" + currYear + ApprecordcountN;
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
					strAppNo = "OAP" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "OAP" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		logger.info("other inspection generate new application no end");
		return strAppNo;
	}

	@Override
	public List<VehicleInspectionDTO> getApplicationNoList() {
		logger.info("edit/view other inspection get apllication no. list satrt");
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VehicleInspectionDTO> applicationNoList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct c.pm_application_no  FROM public.nt_t_task_det a, public.nt_t_pm_application c "
					+ "where ((a.tsd_task_code in ('CI100','CI101', 'II100','II101', 'SI100','SI101','CI102','CI103', 'II102','II103', 'SI102','SI103') and "
					+ "a.tsd_status in('C','O', 'I') ) ) and  a.tsd_app_no=c.pm_application_no";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				applicationNoList.add(vehicleDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("edit/view other inspection get apllication no. list end");
		return applicationNoList;
	}

	@Override
	public List<VehicleInspectionDTO> getVehicleNoList() {
		logger.info("edit/view other inspection get vehicle no. list satrt");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VehicleInspectionDTO> vehicleNoList = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct c.pm_vehicle_regno FROM public.nt_t_task_det a, public.nt_t_pm_application c "
					+ "where ((a.tsd_task_code in ('CI100','CI101', 'II100','II101', 'SI100','SI101','CI102','CI103', 'II102','II103', 'SI102','SI103') and "
					+ "a.tsd_status in('C','O', 'I') ) ) and  a.tsd_app_no=c.pm_application_no";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				vehicleNoList.add(vehicleDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("edit/view other inspection get vehicle no. list end");
		return vehicleNoList;
	}

	@Override
	public List<VehicleInspectionDTO> getOtherInspectionRecords(VehicleInspectionDTO dto, boolean isLoadDefaultData) {
		logger.info("edit/view other inspection get inspection data list satrt");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VehicleInspectionDTO> list = new ArrayList<VehicleInspectionDTO>();

		try {
			con = ConnectionManager.getConnection();

			if (isLoadDefaultData) {

				String query = "select  c.pm_queue_no, c.inspection_type, c.pm_application_no, c.pm_vehicle_regno, a.tsd_status ,b.pmo_permit_no ,b.pmo_full_name  "
						+ "FROM public.nt_t_task_det a,public.nt_t_pm_vehi_owner b ,  public.nt_t_pm_application c "
						+ "where ((a.tsd_task_code in ('CI100','CI101', 'II100','II101', 'SI100','SI101','CI102','CI103', 'II102','II103', 'SI102','SI103') and a.tsd_status in('C','O', 'I') ) ) and "
						+ "a.tsd_app_no=b.pmo_application_no and a.tsd_app_no=c.pm_application_no order  by pm_application_no desc ";

				ps = con.prepareStatement(query);
			} else {

				String query = "select  c.pm_queue_no, c.inspection_type, c.pm_application_no, c.pm_vehicle_regno, a.tsd_status ,b.pmo_permit_no ,b.pmo_full_name  "
						+ "FROM public.nt_t_task_det a,public.nt_t_pm_vehi_owner b ,  public.nt_t_pm_application c "
						+ "where (c.pm_application_no=? or c.pm_vehicle_regno=?) and "
						+ "((a.tsd_task_code in ('CI100','CI101', 'II100','II101', 'SI100','SI101','CI102','CI103', 'II102','II103', 'SI102','SI103') and a.tsd_status in('C','O', 'I') ) ) and "
						+ "a.tsd_app_no=b.pmo_application_no and a.tsd_app_no=c.pm_application_no order  by pm_application_no desc ";

				ps = con.prepareStatement(query);
				ps.setString(1, dto.getApplicationNo());
				ps.setString(2, dto.getVehicleNo());
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				vehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				vehicleDTO.setPermitNo(rs.getString("pmo_permit_no"));
				vehicleDTO.setOwnerName(rs.getString("pmo_full_name"));
				vehicleDTO.setQueueNo(rs.getString("pm_queue_no"));
				vehicleDTO.setInspectionTypeCode(rs.getString("inspection_type"));
				list.add(vehicleDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("edit/view other inspection get inspection data list end");
		return list;
	}

	@Override
	public String getApplicationNumberOrPermitNumber(String number, boolean isApplicationNoChange) {
		logger.info("edit/view other inspection get application no or permit no data start");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String foundValue = null;

		try {

			con = ConnectionManager.getConnection();

			if (isApplicationNoChange) {

				String query = "SELECT  tsd_app_no, tsd_vehicle_no FROM public.nt_t_task_det "
						+ "where (tsd_task_code in ('CI100','CI101', 'II100','II101', 'SI100','SI101','CI102','CI103', 'II102','II103', 'SI102','SI103') and tsd_status in('C','O', 'I') ) "
						+ "and (tsd_app_no=? )  order by created_date desc;";

				ps = con.prepareStatement(query);
				ps.setString(1, number);
				rs = ps.executeQuery();

				if (rs.next()) {
					foundValue = (rs.getString("tsd_vehicle_no"));
				}

			} else {

				String query = "SELECT  tsd_app_no, tsd_vehicle_no FROM public.nt_t_task_det "
						+ "where (tsd_task_code in ('CI100','CI101', 'II100','II101', 'SI100','SI101','CI102','CI103', 'II102','II103', 'SI102','SI103') and tsd_status in('C','O', 'I') ) "
						+ "and (tsd_vehicle_no=? )  order by created_date desc;";

				ps = con.prepareStatement(query);
				ps.setString(1, number);
				rs = ps.executeQuery();

				if (rs.next()) {
					foundValue = (rs.getString("tsd_app_no"));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("edit/view other inspection get application no or permit no data end");
		return foundValue;
	}

	@Override
	public boolean updateStatusOfQueApp(Connection con, String queueNo, String appNo) {

		// task status - once call queue number called = 'O' after save queue number
		// ='C'

		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);
		boolean save = false;
		try {

			String sql3 = "update nt_m_queue_master " + "set que_application_no=? "
					+ " WHERE que_number=? and que_date like '" + today + "%'";

			ps = con.prepareStatement(sql3);

			ps.setString(1, appNo);
			ps.setString(2, queueNo);

			int i = ps.executeUpdate();
			if (i > 0) {
				save = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateQueueNumberAfterCallNext error: " + ex.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}
		return save;

	}

	@Override
	public boolean updateTransactionTypeCodeForQueueNo(Connection con, String queueNumber, String currentQueueType) {
		logger.info("updateTransactionTypeCodeForQueueNo start");

		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		String trnTypeCode = null;
		boolean update = false;
		boolean okey = false;
		try {

			String sql = "select que_trn_type_code from public.nt_m_queue_master where que_number=? and que_date like '"
					+ today + "%'";

			ps = con.prepareStatement(sql);
			ps.setString(1, queueNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				trnTypeCode = rs.getString("que_trn_type_code");
			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (trnTypeCode != null && !trnTypeCode.isEmpty() && !trnTypeCode.trim().equalsIgnoreCase("")
					&& trnTypeCode.trim().equalsIgnoreCase(currentQueueType)) {
				// no need to update
				okey = true;
			} else {
				// update queue TRN type code
				update = true;
			}

			if (update) {
				String sql2 = "UPDATE public.nt_m_queue_master SET que_trn_type_code=? where que_number=? and que_date like '"
						+ today + "%'";
				ps = con.prepareStatement(sql2);

				ps.setString(1, currentQueueType);
				ps.setString(2, queueNumber);

				int i = ps.executeUpdate();

				if (i > 0) {
					okey = true;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateTransactionTypeCodeForQueueNo error: " + ex.toString());

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		logger.error("updateTransactionTypeCodeForQueueNo end");

		return okey;
	}

	@Override
	public boolean updateQueueNumberTaskInQueueMaster(Connection con, String queueNo, String appNo, String taskCode,
			String taskStatus) {
		logger.info("updateQueueNumberTaskInQueueMaster start");

		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean save = false;

		try {

			String query = "UPDATE public.nt_m_queue_master SET que_task_code=?, que_task_status=? WHERE que_application_no=?  AND que_number=?";
			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, taskStatus);
			ps.setString(3, appNo);
			ps.setString(4, queueNo);

			int i = ps.executeUpdate();
			if (i > 0) {
				save = true;
			}

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			/** for queue counter display start **/
			if (taskStatus != null && !taskStatus.isEmpty() && !taskStatus.trim().equalsIgnoreCase("")
					&& taskStatus.equalsIgnoreCase("C")) {
				String query2 = "UPDATE public.nt_m_queue_master SET que_skip_count=null where que_number=? and que_application_no=?";

				ps = con.prepareStatement(query2);
				ps.setString(1, queueNo);
				ps.setString(2, appNo);

				ps.executeUpdate();
			}

			/** for queue counter display end **/

		} catch (Exception ex) {
			logger.error("updateQueueNumberTaskInQueueMaster error: " + ex.toString());
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		logger.info("updateQueueNumberTaskInQueueMaster end");
		return save;
	}

	@Override
	public VehicleInspectionDTO searchForNormalInspection(String queueNo, boolean inspectionForAmendment,
			String activeApplicationNUmber) {
		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);
		boolean backlog = false;
		try {

			con = ConnectionManager.getConnection();
			String query;

			String backlogStatus = isbacklog(queueNo);

			if (inspectionForAmendment) {
				backlogStatus = "N";
			}

			if (backlogStatus != null && !backlogStatus.isEmpty() && !backlogStatus.trim().equalsIgnoreCase("")
					&& backlogStatus.equalsIgnoreCase("Y")) {
				backlog = true;
				query = "select distinct a.inspection_type ,a.inspection_location ,a.pm_inspection_status , a.pm_isnew_permit,a.pm_inspect_remark,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type,"
						+ " b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_engine_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
						+ " g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_valid_to,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit"
						+ " from nt_t_pm_application a , nt_m_queue_master d,nt_r_route b ,nt_r_service_types c, nt_t_pm_omini_bus1 e,nt_r_make f,nt_r_model g, nt_t_pm_vehi_owner h "
						+ " where a.pm_vehicle_regno = d.que_vehicle_no" + " and a.pm_route_no = b.rou_number "
						+ " and a.pm_service_type= c.code " + " and a.pm_application_no = e.pmb_application_no "
						+ " and e.pmb_make=f.code " + " and  e.pmb_model = g.mod_code "
						+ " and a.pm_application_no = h.pmo_application_no " + " and a.pm_status='A'"
						+ " and d.que_number= '" + queueNo + "'" + " and to_char(d.created_date,'dd-MM-yyyy')='" + today
						+ "'";

			} else {
				query = "select distinct a.inspection_type ,a.inspection_location ,a.pm_inspection_status ,l.loc_description , a.pm_isnew_permit,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_valid_to,a.pm_inspect_remark,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type, "
						+ " b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_engine_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
						+ " g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit "
						+ " from public.nt_t_pm_application a  inner join nt_m_queue_master d on d.que_permit_no =a.pm_permit_no    left outer join public.nt_r_route b on a.pm_route_no = b.rou_number "
						+ " left outer join public.nt_r_service_types c on a.pm_service_type= c.code "
						+ " left outer join nt_t_pm_omini_bus1 e on a.pm_application_no = e.pmb_application_no "
						+ " left outer join nt_r_make f on e.pmb_make=f.code "
						+ " left outer join nt_r_model g on e.pmb_model = g.mod_code "
						+ " left outer join nt_t_pm_vehi_owner h on a.pm_application_no = h.pmo_application_no "
						+ " left outer join nt_r_inspection_location l on a.inspection_location = l.loc_code  "
						+ " where a.pm_application_no ='" + activeApplicationNUmber + "' and  d.que_number='" + queueNo
						+ "' and to_char(d.created_date,'dd-MM-yyyy')= '" + today + "'";

			}

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				if (!backlog) {
					VehicleDTO.setInspecLocationCode(rs.getString("inspection_location"));
					VehicleDTO.setInspecLocationDes(rs.getString("loc_description"));
					VehicleDTO.setInspectioncomplIncomplStatus(rs.getString("pm_inspection_status"));
				}

				VehicleDTO.setIsNewPermit(rs.getString("pm_isnew_permit"));
				VehicleDTO.setFinalRemark(rs.getString("pm_inspect_remark"));
				VehicleDTO.setInspectionStatus(rs.getString("pm_reinspec_status"));
				VehicleDTO.setDate1(rs.getString("pm_next_ins_date_sec1_2"));
				VehicleDTO.setDate2(rs.getString("pm_next_ins_date_sec3"));
				VehicleDTO.setRouteFlag(rs.getString("pm_route_flag"));
				VehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setOldApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setPermitSeqNo(rs.getLong("seq"));
				VehicleDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				VehicleDTO.setEngineNo(rs.getString("pmb_engine_no"));
				VehicleDTO.setManyear(rs.getString("pmb_production_date"));
				VehicleDTO.setMakeDescription(rs.getString("make"));
				VehicleDTO.setMakeTypeCode(rs.getString("pmb_make"));
				VehicleDTO.setModelTypeCode(rs.getString("pmb_model"));
				VehicleDTO.setModelDescription(rs.getString("mod_description"));
				VehicleDTO.setNicreg(rs.getString("pmo_nic"));
				VehicleDTO.setPermitNo(rs.getString("pm_permit_no"));
				VehicleDTO.setPermitOwner(rs.getString("pmo_full_name"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setRouteDetails(rs.getString("rou_description"));
				VehicleDTO.setRouteNo(rs.getString("pm_route_no"));
				VehicleDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				VehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				VehicleDTO.setOrigine(rs.getString("rou_service_origine"));
				VehicleDTO.setDestination(rs.getString("rou_service_destination"));
				VehicleDTO.setRouteVia(rs.getString("rou_via"));
				VehicleDTO.setBacklogApp(true);
				VehicleDTO.setValidTo(rs.getString("pm_valid_to"));
				VehicleDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				VehicleDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				VehicleDTO.setTenderPermit(rs.getString("pm_is_tender_permit"));
				VehicleDTO.setBacklogStatus(backlogStatus);
				VehicleDTO.setPermitIssueDate(rs.getString("pm_permit_issue_date"));
			}

			if (!rs.isBeforeFirst()) {
				VehicleDTO.setQueueNo(queueNo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;

	}

	@Override
	public String getLatestApplicationNumber(String vehicleNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String vehicleNO = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select pm_application_no from public.nt_t_pm_application  where pm_vehicle_regno =? \r\n"
					+ "order by pm_created_date desc limit 1 ";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNumber);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNO = (rs.getString("pm_application_no"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNO;
	}

	@Override
	public List<VehicleInspectionDTO> GridviewNew(String latestApplicationNumber) {
		List<VehicleInspectionDTO> dataList = new ArrayList<VehicleInspectionDTO>();
		Connection con = null;
		String query;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			query = "select b.iap_seq ,b.rou_code, b.rou_section_code ,vid_remark,vid_exist,  b.rou_description , c.description as sectiondes from nt_t_vehicle_inspec_det a , nt_r_inspec_act_points b, public.nt_r_sections c  where vid_app_no='"
					+ latestApplicationNumber
					+ "' and b.active='A' and  b.iap_seq = a.vid_sec_seq and b.rou_section_code=c.code order by vid_seq_no;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();
				VehicleDTO.setNewGridNo((rs.getInt("iap_seq")));
				VehicleDTO.setActionPointSeqNo(rs.getString("rou_code"));
				VehicleDTO.setRemark((rs.getString("vid_remark")));
				VehicleDTO.setDescription(rs.getString("rou_description"));
				VehicleDTO.setSection(rs.getString("sectiondes"));
				VehicleDTO.setExistsText(rs.getString("vid_exist"));

				if (VehicleDTO.getExistsText().equalsIgnoreCase("yes")) {
					VehicleDTO.setExists(true);
				} else {
					VehicleDTO.setExists(false);
				}

				dataList.add(VehicleDTO);
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
	public boolean saveAllDataWithoutAppNoNew(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO,
			String loginUser, OminiBusDTO ominiBusDTO, List<VehicleInspectionDTO> dataList, String inspectionStatus,
			String inspectionType) {
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		String date3 = null;
		String date1_2 = null;
		String expireDate = null;

		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (VehicleDTO.getCalender1() != null) {
			date1_2 = sdf.format(VehicleDTO.getCalender1());
		}

		if (VehicleDTO.getCalender2() != null) {
			date3 = sdf.format(VehicleDTO.getCalender2());
		}

		if (VehicleDTO.getExpireDate() != null) {
			expireDate = VehicleDTO.getExpireDate();
		}

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSaveInApp = false;
		boolean isDataSaveInVehi = false;
		boolean isDataSaveInOmni = false;
		boolean isDataSave = false;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_application");
			String sql = "insert into nt_t_pm_application "
					+ "(seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno,"
					+ "pm_status, pm_service_type, pm_route_no, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_created_by, pm_created_date,"
					+ "pm_is_backlog_app,pm_valid_to,pm_tot_bus_fare,pm_permit_expire_date,pm_route_flag,pm_is_tender_permit, pm_permit_issue_date,pm_reinspec_status,pm_isnew_permit, pm_tran_type,"
					+ "inspection_location , inspection_type , proceed_status,  pm_inspection_status,pm_tender_ref_no ) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, generatedapplicationNO);
			stmt.setString(3, VehicleDTO.getQueueNo());
			stmt.setString(4, VehicleDTO.getPermitNo());
			stmt.setString(5, VehicleDTO.getVehicleNo());
			stmt.setString(6, "O");
			stmt.setString(7, VehicleDTO.getServiceTypeCode());
			stmt.setString(8, VehicleDTO.getRouteNo());
			if (date1_2 != null && !date1_2.trim().equalsIgnoreCase("") && !date1_2.isEmpty()) {
				stmt.setString(9, date1_2);
			} else {
				stmt.setNull(9, java.sql.Types.VARCHAR);
			}
			if (date3 != null && !date3.trim().equalsIgnoreCase("") && !date3.isEmpty()) {
				stmt.setString(10, date3);
			} else {
				stmt.setNull(10, java.sql.Types.VARCHAR);
			}
			stmt.setString(11, VehicleDTO.getFinalRemark());
			stmt.setString(12, VehicleDTO.getLoginUser());
			stmt.setTimestamp(13, timestamp);
			stmt.setString(14, VehicleDTO.getBacklogStatus());
			stmt.setString(15, VehicleDTO.getValidTo());
			stmt.setBigDecimal(16, VehicleDTO.getBusFare());
			if (expireDate != null && !expireDate.trim().equalsIgnoreCase("") && !expireDate.isEmpty()) {
				stmt.setString(17, expireDate);
			} else {
				stmt.setNull(17, java.sql.Types.VARCHAR);
			}
			stmt.setString(18, VehicleDTO.getRouteFlag());
			stmt.setString(19, VehicleDTO.getTenderPermit());
			stmt.setString(20, VehicleDTO.getPermitIssueDate());
			stmt.setString(21, VehicleDTO.getInspectionStatus());
			stmt.setString(22, VehicleDTO.getIsNewPermit());
			stmt.setString(23, VehicleDTO.getTranstractionTypeCode());
			stmt.setString(24, VehicleDTO.getInspecLocationCode());
			stmt.setString(25, inspectionType);
			if (inspectionStatus.equals("I")) {
				stmt.setString(26, "N");
			} else {
				stmt.setString(26, null);
			}
			stmt.setString(27, inspectionStatus);
			stmt.setString(28, VehicleDTO.getTenderRefNo());

			int i = stmt.executeUpdate();

			ConnectionManager.close(stmt);
			if (i > 0) {
				isDataSaveInApp = true;

				String query2 = "UPDATE public.nt_t_pm_application "
						+ "SET pm_status='I' , pm_modified_by=?, pm_modified_date=?" + "WHERE pm_application_no=?; ";

				ps = con.prepareStatement(query2);
				ps.setString(1, loginUser);
				ps.setTimestamp(2, timestamp);
				ps.setString(3, VehicleDTO.getOldApplicationNo());

				ps.executeUpdate();

				ConnectionManager.close(ps);

			} else {
				isDataSaveInApp = false;
			}
			if (isDataSaveInApp) {

				String sql2 = "insert into nt_t_pm_vehi_owner"
						+ "(seq,pmo_application_no,pmo_permit_no,pmo_vehicle_regno,pmo_full_name,pmo_nic,pm_created_by,pm_created_date,pmo_is_backlog_app)"
						+ "values (?,?,?,?,?,?,?,?,?)";

				stmt = con.prepareStatement(sql2);
				long seqNo2 = Utils.getNextValBySeqName(con, "seq_nt_t_pm_vehi_owner");

				stmt.setLong(1, seqNo2);
				stmt.setString(2, generatedapplicationNO);
				stmt.setString(3, VehicleDTO.getPermitNo());
				stmt.setString(4, VehicleDTO.getVehicleNo());
				stmt.setString(5, VehicleDTO.getPermitOwner());
				stmt.setString(6, VehicleDTO.getNicreg());
				stmt.setString(7, VehicleDTO.getLoginUser());
				stmt.setTimestamp(8, timestamp);
				stmt.setString(9, "N");

				int i2 = stmt.executeUpdate();
				ConnectionManager.close(stmt);
				if (i2 > 0) {
					isDataSaveInVehi = true;
				} else {
					isDataSaveInVehi = false;
				}

				if (isDataSaveInVehi) {

					long seqNo3 = Utils.getNextValBySeqName(con, "seq_nt_t_pm_omini_bus1");

					String sql3 = "INSERT INTO public.nt_t_pm_omini_bus1"
							+ "(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no,"
							+ " pmb_chassis_no, pmb_make, pmb_model,pmb_production_date, "
							+ " pmb_first_reg_date, pmb_seating_capacity, pmb_no_of_doors,"
							+ " pmb_weight,pmb_is_backlog_app,pmb_permit_no,pmb_created_by, pmb_created_date)"
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

					stmt = con.prepareStatement(sql3);

					stmt.setLong(1, seqNo3);
					stmt.setString(2, ominiBusDTO.getApplicationNo());
					stmt.setString(3, ominiBusDTO.getVehicleRegNo());
					stmt.setString(4, ominiBusDTO.getEngineNo());
					stmt.setString(5, ominiBusDTO.getChassisNo());
					stmt.setString(6, ominiBusDTO.getMake());
					stmt.setString(7, ominiBusDTO.getModel());
					stmt.setString(8, ominiBusDTO.getManufactureDate());

					if (ominiBusDTO.getRegistrationDate() != null) {
						String registrationDate = (dateFormat.format(ominiBusDTO.getRegistrationDate()));
						stmt.setString(9, registrationDate);
					} else {
						stmt.setString(9, null);
					}
					stmt.setString(10, ominiBusDTO.getSeating());
					stmt.setString(11, ominiBusDTO.getNoofDoors());
					stmt.setString(12, ominiBusDTO.getWeight());
					stmt.setString(13, ominiBusDTO.getIsBacklogApp());
					stmt.setString(14, ominiBusDTO.getPermitNo());
					stmt.setString(15, ominiBusDTO.getCreatedBy());
					stmt.setTimestamp(16, timestamp);

					int i3 = stmt.executeUpdate();
					ConnectionManager.close(stmt);
					if (i3 > 0) {
						isDataSaveInOmni = true;
					}
					if (isDataSaveInOmni) {
						boolean queueUpdate1 = false;
						boolean queueUpdate2 = false;
						boolean queueUpdate3 = false;
						boolean taskUpdate = false;
						boolean taskDetUpdate = false;
						boolean skipInspectionUpdate = false;
						boolean saveInspectDet = saveDataVehicleInspecDetailsNew(VehicleDTO, dataList,
								generatedapplicationNO, con);
						boolean saveDetHistory = saveDataVehicleInspecDetailsHistoryNew(VehicleDTO, dataList,
								generatedapplicationNO, con);
						if (saveInspectDet && saveDetHistory) {

							// update queMaster Data
							queueUpdate1 = updateStatusOfQueApp(con, VehicleDTO.getQueueNo(), generatedapplicationNO);
							queueUpdate2 = updateTransactionTypeCodeForQueueNo(con, VehicleDTO.getQueueNo(), "02");
							queueUpdate3 = updateQueueNumberTaskInQueueMaster(con, VehicleDTO.getQueueNo(),
									generatedapplicationNO, "PM100", "C");
							skipInspectionUpdate = updatePhotoUploadStatus(con, generatedapplicationNO, "N");
							//update number generation
							updateNumberGeneration(con, generatedapplicationNO, loginUser,"PAP");
							// update task table Data
							VehicleDTO.setApplicationNo(generatedapplicationNO);
							boolean isTasKPM100Available = checkTaskDetails(VehicleDTO, "PM100", "C");

							if (isTasKPM100Available == false && queueUpdate1 && queueUpdate2 && queueUpdate3
									&& skipInspectionUpdate) {

								taskDetUpdate = insertTaskDetailsNew(con, VehicleDTO, loginUser, "PM100", "C");
								if (inspectionStatus.equals("I") && inspectionType.equals("PI") && taskDetUpdate) {
									taskUpdate = insertIncomplteTaskHistroy(VehicleDTO, loginUser, "PI100", "I");
								} else if (inspectionStatus.equals("I") && inspectionType.equals("AI")
										&& taskDetUpdate) {
									taskUpdate = insertIncomplteTaskHistroy(VehicleDTO, loginUser, "AI100", "I");
								}

							}

						}

						if (taskUpdate || taskDetUpdate) {
							con.commit();
							isDataSave = true;
						}
					}

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isDataSave;

	}

	@Override
	public boolean saveAllDataWithAppNew(VehicleInspectionDTO VehicleDTO, String generatedapplicationNO,
			String loginUser, OminiBusDTO ominiBusDTO, List<VehicleInspectionDTO> dataList, String inspectionStatus,
			String inspectionType) {

		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		String date3 = null;
		String date1_2 = null;
		String expireDate = null;

		String format = "dd/MM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (VehicleDTO.getCalender1() != null) {
			date1_2 = sdf.format(VehicleDTO.getCalender1());
		}

		if (VehicleDTO.getCalender2() != null) {
			date3 = sdf.format(VehicleDTO.getCalender2());
		}

		if (VehicleDTO.getExpireDate() != null) {
			expireDate = VehicleDTO.getExpireDate();
		}

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSaveInApp = false;
		boolean isDataSaveInVehi = false;
		boolean isDataSaveInOmni = false;
		boolean isDataSave = false;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_application");
			String sql = "insert into nt_t_pm_application "
					+ "(seq, pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno,"
					+ "pm_status, pm_service_type, pm_route_no, pm_next_ins_date_sec1_2, pm_next_ins_date_sec3, pm_inspect_remark, pm_created_by, pm_created_date,"
					+ "pm_is_backlog_app,pm_valid_to,pm_tot_bus_fare,pm_permit_expire_date,pm_route_flag,pm_is_tender_permit, pm_permit_issue_date,pm_reinspec_status,pm_isnew_permit, pm_tran_type,"
					+ "inspection_location , inspection_type , proceed_status,  pm_inspection_status,pm_tender_ref_no ) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, generatedapplicationNO);
			stmt.setString(3, VehicleDTO.getQueueNo());
			stmt.setString(4, VehicleDTO.getPermitNo());
			stmt.setString(5, VehicleDTO.getVehicleNo());
			stmt.setString(6, "O");
			stmt.setString(7, VehicleDTO.getServiceTypeCode());
			stmt.setString(8, VehicleDTO.getRouteNo());
			if (date1_2 != null && !date1_2.trim().equalsIgnoreCase("") && !date1_2.isEmpty()) {
				stmt.setString(9, date1_2);
			} else {
				stmt.setNull(9, java.sql.Types.VARCHAR);
			}
			if (date3 != null && !date3.trim().equalsIgnoreCase("") && !date3.isEmpty()) {
				stmt.setString(10, date3);
			} else {
				stmt.setNull(10, java.sql.Types.VARCHAR);
			}
			stmt.setString(11, VehicleDTO.getFinalRemark());
			stmt.setString(12, VehicleDTO.getLoginUser());
			stmt.setTimestamp(13, timestamp);
			stmt.setString(14, VehicleDTO.getBacklogStatus());
			stmt.setString(15, VehicleDTO.getValidTo());
			stmt.setBigDecimal(16, VehicleDTO.getBusFare());
			if (expireDate != null && !expireDate.trim().equalsIgnoreCase("") && !expireDate.isEmpty()) {
				stmt.setString(17, expireDate);
			} else {
				stmt.setNull(17, java.sql.Types.VARCHAR);
			}
			stmt.setString(18, VehicleDTO.getRouteFlag());
			stmt.setString(19, VehicleDTO.getTenderPermit());
			stmt.setString(20, VehicleDTO.getPermitIssueDate());
			stmt.setString(21, VehicleDTO.getInspectionStatus());
			stmt.setString(22, VehicleDTO.getIsNewPermit());
			stmt.setString(23, VehicleDTO.getTranstractionTypeCode());

			stmt.setString(24, VehicleDTO.getInspecLocationCode());
			stmt.setString(25, inspectionType);
			if (inspectionStatus.equals("I")) {
				stmt.setString(26, "N");
			} else {
				stmt.setString(26, null);
			}
			stmt.setString(27, inspectionStatus);
			stmt.setString(28, VehicleDTO.getTenderRefNo());

			int i = stmt.executeUpdate();

			ConnectionManager.close(stmt);
			if (i > 0) {
				isDataSaveInApp = true;

				String query2 = "UPDATE public.nt_t_pm_application "
						+ "SET pm_status='I' , pm_modified_by=?, pm_modified_date=?" + "WHERE pm_application_no=?; ";

				ps = con.prepareStatement(query2);
				ps.setString(1, loginUser);
				ps.setTimestamp(2, timestamp);
				ps.setString(3, VehicleDTO.getOldApplicationNo());

				ps.executeUpdate();

				ConnectionManager.close(ps);

			} else {
				isDataSaveInApp = false;
			}
			if (isDataSaveInApp) {

				String sql1 = "INSERT INTO nt_t_pm_vehi_owner(seq, pmo_application_no, pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, pm_created_by, pm_created_date"
						+ "					)SELECT nextval('seq_nt_t_pm_vehi_owner'), "
						+ "				   ?,pmo_permit_no, pmo_vehicle_regno, pmo_preferred_language, pmo_title, pmo_full_name, pmo_full_name_sinhala, pmo_full_name_tamil, pmo_name_with_initial, pmo_nic, pmo_gender, pmo_dob, pmo_marital_status, pmo_telephone_no, pmo_mobile_no, pmo_province, pmo_district, pmo_div_sec, pmo_address1, pmo_address1_sinhala, pmo_address1_tamil, pmo_address2, pmo_address2_sinhala, pmo_address2_tamil, pmo_address3, pmo_address3_sinhala, pmo_address3_tamil, pmo_city, pmo_city_sinhala, pmo_city_tamil, pmo_is_backlog_app, ?, ?"
						+ "				FROM nt_t_pm_vehi_owner WHERE pmo_application_no=?";

				stmt = con.prepareStatement(sql1);

				stmt.setString(1, generatedapplicationNO);
				stmt.setString(2, VehicleDTO.getCreateBy());
				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, VehicleDTO.getApplicationNo());

				int i2 = stmt.executeUpdate();
				ConnectionManager.close(stmt);
				if (i2 > 0) {
					isDataSaveInVehi = true;
				} else {
					isDataSaveInVehi = false;
				}

				if (isDataSaveInVehi) {
					String sql2 = "INSERT INTO nt_t_pm_omini_bus1(seq, pmb_application_no, pmb_vehicle_regno, pmb_engine_no, pmb_chassis_no, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no, pmb_emission_test_exp_date, pmb_garage_name"
							+ "					)SELECT nextval('seq_nt_t_pm_omini_bus1'), "
							+ "				   ?, pmb_vehicle_regno, pmb_engine_no, ?, pmb_make, pmb_model, pmb_seating_capacity, pmb_no_of_doors, pmb_height, ?, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date, pmb_bus_price, pmb_is_loan_obtain, pmb_finance_company, pmb_bank_loan_amt, pmb_due_amt, pmb_date_obtain, pmb_lapsed_installment, pmb_revenue_license_exp_date, pmb_first_reg_date, pmb_is_backlog_app, pmb_created_by, pmb_created_date, pmb_permit_no, pmb_policy_no , pmb_emission_test_exp_date, pmb_garage_name "
							+ "				FROM nt_t_pm_omini_bus1 WHERE pmb_application_no=?";

					stmt = con.prepareStatement(sql2);

					stmt.setString(1, generatedapplicationNO);
					stmt.setString(2, ominiBusDTO.getChassisNo());
					stmt.setString(3, ominiBusDTO.getManufactureDate());
					stmt.setString(4, ominiBusDTO.getApplicationNo());

					int i3 = stmt.executeUpdate();
					ConnectionManager.close(stmt);
					if (i3 > 0) {
						isDataSaveInOmni = true;
					}
					if (isDataSaveInOmni) {
						boolean queueUpdate1 = false;
						boolean queueUpdate2 = false;
						boolean queueUpdate3 = false;
						boolean taskUpdate = false;
						boolean taskDetUpdate = false;
						boolean skipInspectionUpdate = false;
						boolean vehiImageSave = saveDataVehicleImagesWithApplicatioNoNew(VehicleDTO,
								generatedapplicationNO, loginUser, con);
						boolean saveInspectDet = saveDataVehicleInspecDetailsNew(VehicleDTO, dataList,
								generatedapplicationNO, con);
						boolean saveInspectDetHis = saveDataVehicleInspecDetailsHistoryNew(VehicleDTO, dataList,
								generatedapplicationNO, con);
						if (vehiImageSave || (saveInspectDet && saveInspectDetHis)) {

							// update queMaster Data
							queueUpdate1 = updateStatusOfQueApp(con, VehicleDTO.getQueueNo(), generatedapplicationNO);
							queueUpdate2 = updateTransactionTypeCodeForQueueNo(con, VehicleDTO.getQueueNo(), "02");
							queueUpdate3 = updateQueueNumberTaskInQueueMaster(con, VehicleDTO.getQueueNo(),
									generatedapplicationNO, "PM100", "C");
							skipInspectionUpdate = updatePhotoUploadStatus(con, generatedapplicationNO, "N");
							//update number generation
							updateNumberGeneration(con, generatedapplicationNO, loginUser,"PAP");
							// update task table Data
							VehicleDTO.setApplicationNo(generatedapplicationNO);
							boolean isTasKPM100Available = checkTaskDetails(VehicleDTO, "PM100", "C");

							if (isTasKPM100Available == false && queueUpdate1 && queueUpdate2 && queueUpdate3
									&& skipInspectionUpdate) {

								taskDetUpdate = insertTaskDetailsNew(con, VehicleDTO, loginUser, "PM100", "C");
								if (inspectionStatus.equals("I") && inspectionType.equals("PI") && taskDetUpdate) {
									taskUpdate = insertIncomplteTaskHistroy(VehicleDTO, loginUser, "PI100", "I");
								} else if (inspectionStatus.equals("I") && inspectionType.equals("AI")
										&& taskDetUpdate) {
									taskUpdate = insertIncomplteTaskHistroy(VehicleDTO, loginUser, "AI100", "I");
								}

							}

						}
						if (taskUpdate || taskDetUpdate) {
							con.commit();
							isDataSave = true;
						}

					}

				}

			}

		} catch (Exception ex) {

			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isDataSave;

	}

	@Override
	public boolean insertIncomplteTaskHistroy(VehicleInspectionDTO dto, String loginUSer, String taskCode,
			String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean update = false;

		try {

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_h_task_his "
					+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, dto.getVehicleNo());
			stmt.setString(3, dto.getApplicationNo());
			stmt.setString(4, taskCode);
			stmt.setString(5, status);
			stmt.setString(6, loginUSer);
			stmt.setTimestamp(7, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				update = true;
				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);

		}
		return update;
	}

	@Override
	public VehicleInspectionDTO getDataForNormalReinspection(String queueNo) {
		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {

			con = ConnectionManager.getConnection();
			String query;

			query = "select  distinct a.pm_isnew_permit,a.pm_tot_bus_fare,a.pm_permit_expire_date,a.pm_valid_to,a.pm_reinspec_status,a.pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3,a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type,  b.rou_description,b.rou_service_origine, b.rou_service_destination, b.rou_via,c.description,e.pmb_chassis_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make,  g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic,a.pm_is_backlog_app, a.pm_permit_issue_date,a.pm_is_tender_permit  "
					+ "from public.nt_t_pm_application a left outer join public.nt_r_route b on a.pm_route_no = b.rou_number  "
					+ "left outer join public.nt_r_service_types c on a.pm_service_type= c.code  "
					+ "left outer join public.nt_m_queue_master d on a.pm_vehicle_regno = d.que_vehicle_no  "
					+ "left outer join nt_t_pm_omini_bus1 e on a.pm_application_no = e.pmb_application_no  "
					+ "left outer join nt_r_make f on e.pmb_make=f.code  "
					+ "left outer join nt_r_model g on e.pmb_model = g.mod_code  "
					+ "left outer join nt_t_pm_vehi_owner h on a.pm_application_no = h.pmo_application_no  "
					+ "where d.que_number='" + queueNo + "'" + "and to_char(d.created_date,'dd-MM-yyyy')= '" + today
					+ "'" + "and a.pm_status != 'I'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				VehicleDTO.setIsNewPermit(rs.getString("pm_isnew_permit"));
				VehicleDTO.setInspectionStatus(rs.getString("pm_reinspec_status"));
				VehicleDTO.setDate1(rs.getString("pm_next_ins_date_sec1_2"));
				VehicleDTO.setDate2(rs.getString("pm_next_ins_date_sec3"));

				VehicleDTO.setRouteFlag(rs.getString("pm_route_flag"));

				VehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setOldApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setPermitSeqNo(rs.getLong("seq"));
				VehicleDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				VehicleDTO.setManyear(rs.getString("pmb_production_date"));
				VehicleDTO.setMakeDescription(rs.getString("make"));
				VehicleDTO.setMakeTypeCode(rs.getString("pmb_make"));
				VehicleDTO.setModelTypeCode(rs.getString("pmb_model"));
				VehicleDTO.setModelDescription(rs.getString("mod_description"));
				VehicleDTO.setNicreg(rs.getString("pmo_nic"));
				VehicleDTO.setPermitNo(rs.getString("pm_permit_no"));
				VehicleDTO.setPermitOwner(rs.getString("pmo_full_name"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setRouteDetails(rs.getString("rou_description"));
				VehicleDTO.setRouteNo(rs.getString("pm_route_no"));
				VehicleDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				VehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				VehicleDTO.setOrigine(rs.getString("rou_service_origine"));
				VehicleDTO.setDestination(rs.getString("rou_service_destination"));
				VehicleDTO.setRouteVia(rs.getString("rou_via"));
				VehicleDTO.setBacklogApp(true);
				VehicleDTO.setValidTo(rs.getString("pm_valid_to"));
				VehicleDTO.setBusFare(rs.getBigDecimal("pm_tot_bus_fare"));
				VehicleDTO.setExpireDate(rs.getString("pm_permit_expire_date"));
				VehicleDTO.setTenderPermit(rs.getString("pm_is_tender_permit"));
				VehicleDTO.setBacklogStatus("N");
				VehicleDTO.setPermitIssueDate(rs.getString("pm_permit_issue_date"));

			}

			if (!rs.isBeforeFirst()) {
				VehicleDTO.setQueueNo(queueNo);
				searchQueueTable(VehicleDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;
	}

	@Override
	public boolean updatePhotoUploadStatus(Connection con, String generatedApplicationNo, String photoUpload) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean updated = false;

		try {

			String sql = "UPDATE nt_t_pm_application SET pm_skipped_inspection = ? WHERE pm_application_no = ?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, photoUpload);
			stmt.setString(2, generatedApplicationNo);

			int i = stmt.executeUpdate();
			if (i > 0) {
				updated = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
		}
		return updated;
	}
	/* Other inspection CR end here */

	@Override
	public boolean insertTaskDetailsNew(Connection con, VehicleInspectionDTO dto, String loginUSer, String taskCode,
			String status) {

		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean taskUpdated = false;

		try {

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_t_task_det "
					+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, dto.getVehicleNo());
			stmt.setString(3, dto.getApplicationNo());
			stmt.setString(4, taskCode);
			stmt.setString(5, status);
			stmt.setString(6, loginUSer);
			stmt.setTimestamp(7, timestamp);

			int i = stmt.executeUpdate();

			if (i > 0) {
				taskUpdated = true;
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			
		}
		return taskUpdated;
	}

	@Override
	public boolean updateAmendmentInspectionDeatils(OminiBusDTO ominiBusDTO, VehicleInspectionDTO VehicleDTO,
			List<VehicleInspectionDTO> dataList, String getApplication, String generatedApplicationNO,
			String getTranstractionTypeCode, String currentAppNo, String loginUser,
			PermitRenewalsDTO applicationHistoryDTO, String inspectionStatus, String inspectionType) {

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDataSave = false;
		boolean isHistoryDataSave = false;
		boolean isOmniSave = false;
		boolean isAppSave = false;
		boolean taskUpdate = false;
		String exitTest = null;

		try {
			con = ConnectionManager.getConnection();

			for (int i = 0; i < dataList.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_vehicle_inspec_det");

				String sql = "INSERT INTO public.nt_t_vehicle_inspec_det "
						+ "(vid_seq_no, vid_app_no, vid_sec_seq, vid_remark, vid_exist, vid_created_by, vid_created_date) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?);";
				stmt = con.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, generatedApplicationNO);
				stmt.setInt(3, dataList.get(i).getNewGridNo());

				if (dataList.get(i).getRemark() != null) {
					stmt.setString(4, dataList.get(i).getRemark().trim());
				} else {
					logger.info("UPDATING NULL VALUES");
					stmt.setNull(4, java.sql.Types.VARCHAR);
				}

				if (dataList.get(i).isExists() == true) {
					exitTest = "YES";
				} else {
					exitTest = "NO";
				}
				stmt.setString(5, exitTest);
				stmt.setString(6, VehicleDTO.getLoginUser());
				stmt.setTimestamp(7, timestamp);

				int c = stmt.executeUpdate();
				ConnectionManager.close(stmt);
				if (c > 0) {
					isDataSave = true;
					logger.info("Inserting vehicle inspection........");
				} else {
					isDataSave = false;
					logger.error("ERROR........");
				}

			}

			if (stmt != null) {
				stmt.close();
			}
			if (isDataSave) {
				// history data save

				int versionNo = getVersionNumberVehicleInspecDetailsHistory(generatedApplicationNO);

				String exitTest2 = null;

				for (int i = 0; i < dataList.size(); i++) {

					long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_vehicle_inspec_det");

					String sql = "INSERT INTO public.nt_h_vehicle_inspec_det "
							+ "(vid_seq_no, vid_app_no, vid_sec_seq, vid_remark, vid_exist, vid_created_by, vid_created_date,vid_version) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
					stmt = con.prepareStatement(sql);

					stmt.setLong(1, seqNo);
					stmt.setString(2, generatedApplicationNO);
					stmt.setInt(3, dataList.get(i).getNewGridNo());

					if (dataList.get(i).getRemark() != null) {
						stmt.setString(4, dataList.get(i).getRemark().trim());
					} else {
						logger.info("UPDATING NULL VALUES");
						stmt.setNull(4, java.sql.Types.VARCHAR);
					}

					if (dataList.get(i).isExists() == true) {
						exitTest2 = "YES";
					} else {
						exitTest2 = "NO";
					}
					stmt.setString(5, exitTest2);
					stmt.setString(6, VehicleDTO.getLoginUser());
					stmt.setTimestamp(7, timestamp);
					stmt.setInt(8, versionNo);

					int c = stmt.executeUpdate();
					ConnectionManager.close(stmt);
					if (c > 0) {
						isHistoryDataSave = true;
						logger.info("Inserting vehicle inspection........");
					} else {
						isHistoryDataSave = false;
						logger.error("ERROR........");
					}

				}

			}

			if (isHistoryDataSave) {

				String sql = "UPDATE public.nt_t_pm_omini_bus1 SET pmb_chassis_no=?, pmb_engine_no=?, pmb_make=?, pmb_model=?, pmb_production_date=?, "
						+ "pmb_modified_by=?, pmb_modified_date=? WHERE pmb_application_no= ?";

				stmt = con.prepareStatement(sql);

				stmt.setString(1, ominiBusDTO.getChassisNo());
				stmt.setString(2, ominiBusDTO.getEngineNo());
				stmt.setString(3, ominiBusDTO.getMake());
				stmt.setString(4, ominiBusDTO.getModel());
				stmt.setString(5, ominiBusDTO.getManufactureDate());
				stmt.setString(6, loginUser);
				stmt.setTimestamp(7, timestamp);
				stmt.setString(8, ominiBusDTO.getApplicationNo());

				int i = stmt.executeUpdate();
				if (i > 0) {
					isOmniSave = true;
				}

			}

			if (isOmniSave) {

				// long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_pm_application");
				String sql = "UPDATE public.nt_t_pm_application SET pm_tran_type=?, pm_modified_by=? , pm_modified_date=?, inspection_location =? , inspection_type =? , proceed_status =?,  pm_inspection_status =?  WHERE pm_application_no=?;";

				stmt = con.prepareStatement(sql);

				stmt.setString(1, getTranstractionTypeCode);
				stmt.setString(2, loginUser);
				stmt.setTimestamp(3, timestamp);

				stmt.setString(4, VehicleDTO.getInspecLocationCode());
				stmt.setString(5, inspectionType);
				if (inspectionStatus.equals("I")) {
					stmt.setString(6, "N");
				} else if (inspectionStatus.equals("C")) {
					stmt.setString(6, "Y");
				}

				stmt.setString(7, inspectionStatus);
				stmt.setString(8, currentAppNo);
				int i = stmt.executeUpdate();

				if (i > 0) {
					isAppSave = true;
				} else {
					isAppSave = false;
				}
			}

			if (isAppSave) {

				if (inspectionStatus.equals("I") && inspectionType.equals("AI")) {
					taskUpdate = insertIncomplteTaskHistroy(VehicleDTO, loginUser, "AI100", "I");
				}
			}

			if (taskUpdate || isAppSave) {
				con.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return isDataSave;
	}

	public void updateNumberGeneration(Connection con, String generateAppNo, String modifiedBy,String type) {

		logger.info("other inspection update number generation app no. start");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {

			String sql = "UPDATE public.nt_r_number_generation "
					+ "SET  app_no=?, modified_by=?, modified_date=? WHERE code=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, generateAppNo);
			stmt.setString(2, modifiedBy);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, type);
			stmt.executeUpdate();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
		}

		logger.info("other inspection  update number generation app no. end");

	}

	/** Other inspection start **/

	@Override
	public boolean updateOtherInspection(VehicleInspectionDTO inspectionDTO, String loginUser,
			List<VehicleInspectionDTO> dataList) {

		logger.info("other inspection update inspection location and status start");
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isUpdate = false;
		String exitTest = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_application SET  inspection_location =?,  pm_inspection_status =?, "
					+ " pm_modified_by =?, pm_modified_date =?, pm_inspect_remark=?, proceed_status=?  where pm_application_no =?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, inspectionDTO.getInspecLocationCode());
			stmt.setString(2, inspectionDTO.getInspectionStatusCode());
			stmt.setString(3, loginUser);
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, inspectionDTO.getFinalRemark());

			if (inspectionDTO.getInspectionStatusCode().equals("I")) {
				stmt.setString(6, "N");
			} else {
				stmt.setString(6, null);
			}

			stmt.setString(7, inspectionDTO.getApplicationNo());

			int c = stmt.executeUpdate();

			if (c > 0) {

				for (int i = 0; i < dataList.size(); i++) {

					String query = "UPDATE public.nt_t_vehicle_inspec_det SET   vid_remark=?, vid_exist=?, vid_modified_by=?, "
							+ "vid_modified_date=? WHERE vid_seq_no=? and vid_app_no=?;";

					stmt = con.prepareStatement(query);

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
					stmt.setLong(5, dataList.get(i).getVehicleInspectionSeq());
					stmt.setString(6, inspectionDTO.getApplicationNo());

					int d = stmt.executeUpdate();

					if (d > 0) {

						isUpdate = true;
						int versionNo = getVersionNumberVehicleInspecDetailsHistory(inspectionDTO.getApplicationNo());

						for (int e = 0; e < dataList.size(); e++) {

							long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_vehicle_inspec_det");

							String historyQuery = "INSERT INTO public.nt_h_vehicle_inspec_det "
									+ "(vid_seq_no, vid_app_no, vid_sec_seq, vid_remark, vid_exist, vid_created_by, vid_created_date,vid_version) "
									+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

							stmt = con.prepareStatement(historyQuery);

							stmt.setLong(1, seqNo);
							stmt.setString(2, inspectionDTO.getApplicationNo());
							stmt.setInt(3, dataList.get(e).getNewGridNo());

							if (dataList.get(e).getRemark() != null) {
								stmt.setString(4, dataList.get(e).getRemark().trim());
							} else {
								stmt.setNull(4, java.sql.Types.VARCHAR);
							}

							if (dataList.get(e).isExists() == true) {
								exitTest = "YES";
							} else {
								exitTest = "NO";
							}
							stmt.setString(5, exitTest);
							stmt.setString(6, inspectionDTO.getLoginUser());
							stmt.setTimestamp(7, timestamp);
							stmt.setInt(8, versionNo);
							stmt.executeUpdate();

						}

					} else {
						isUpdate = false;
					}

				}

			} else {
				isUpdate = false;
			}
			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		logger.info("other inspection  update inspection location and status end");

		return isUpdate;
	}

	@Override
	public VehicleInspectionDTO getQueueMasterTableData(VehicleInspectionDTO dto, String queueNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);
		logger.info("other inspection get queue status start");

		VehicleInspectionDTO inspectionDTO = new VehicleInspectionDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "select task_status  from nt_m_queue_master " + "where que_number=? and que_date like '"
					+ today + "%' and que_permit_no=? and que_vehicle_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, queueNo);
			ps.setString(2, dto.getPermitNo());
			ps.setString(3, dto.getVehicleNo());

			rs = ps.executeQuery();

			if (rs.next()) {
				inspectionDTO.setQueueDataFound(true);
				inspectionDTO.setQueueStatus(rs.getString("task_status"));
			} else {
				inspectionDTO.setQueueDataFound(false);
			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("other inspection get queue status end");
		return inspectionDTO;
	}

	@Override
	public void updateQueueMasterStatusUsingAppNo(String queueNo, VehicleInspectionDTO dto, String status,
			String taskCode, String taskStatus, String loginUser) {

		logger.info("other inspection update queueNo status start");

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String today = df.format(date);
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_queue_master SET  task_status=?, que_task_code =?, "
					+ "que_task_status =?, modified_by=? , modified_date=? "
					+ "WHERE que_number=? and que_application_no =? and to_char(created_date,'dd-MM-yyyy')=? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, status);
			stmt.setString(2, taskCode);
			stmt.setString(3, taskStatus);
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, queueNo);
			stmt.setString(7, dto.getApplicationNo());
			stmt.setString(8, today);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("other inspection update queueNo status error - > " + ex);

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		logger.info("other inspection update queueNo status end");

	}

	/** Other inspection end **/

	@Override
	public boolean taskCodeStatusINQueuePM100(String tokenNumber) {
		String taskStatus = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);
		boolean canSearch = true;
		try {
			con = ConnectionManager.getConnection();

			String query = "select d.que_task_status from public.nt_m_queue_master d"
					+ " where d.que_number =? and d.que_task_code ='PM101'"
					+ "and to_char(d.created_date,'dd-MM-yyyy')= '" + today + "'";

			ps = con.prepareStatement(query);
			ps.setString(1, tokenNumber);
			rs = ps.executeQuery();

			while (rs.next()) {
				taskStatus = rs.getString("que_task_status");

			}
			if (taskStatus != null && taskStatus.equalsIgnoreCase("C")) {
				canSearch = false;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return canSearch;

	}

	@Override
	public boolean validTokenInQueue(String tokenNumber) {
		String queueNum = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);
		boolean validToken = true;
		try {
			con = ConnectionManager.getConnection();

			String query = "select d.que_number from public.nt_m_queue_master d"
					+ " where d.que_number =? and que_inspection_type in ('AI','PI')"
					+ "and to_char(d.created_date,'dd-MM-yyyy')= '" + today + "'";

			ps = con.prepareStatement(query);
			ps.setString(1, tokenNumber);
			rs = ps.executeQuery();

			while (rs.next()) {
				queueNum = rs.getString("que_number");

			}
			if (queueNum == null || queueNum.isEmpty()) {
				validToken = false;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return validToken;

	}

	@Override
	public VehicleInspectionDTO getTaskDetForTodayNew(String vehicleNo) {
		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();
			String query;

			query = "SELECT  tsd_task_code, tsd_status\r\n" + "FROM public.nt_t_task_det where  tsd_vehicle_no=? \r\n"
					+ "and to_char(created_date,'dd-MM-yyyy')= '" + today + "'";

			ps = con.prepareStatement(query);

			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleDTO.setTaskDetCode(rs.getString("tsd_task_code"));
				VehicleDTO.setTaskDetStatus(rs.getString("tsd_status"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;
	}

	@Override
	public String getActiveApplicationNumber(String vehicleNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String vehicleNO = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select pm_application_no from public.nt_t_pm_application  where pm_vehicle_regno =? and pm_status ='A' \r\n"
					+ "order by pm_created_date desc limit 1 ";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNumber);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNO = (rs.getString("pm_application_no"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNO;
	}

	@Override
	public String getAmendmentType(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String vehicleNO = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT amd_trn_type  FROM public.nt_m_amendments \r\n" + "WHERE amd_application_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNO = (rs.getString("amd_trn_type"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNO;
	}

	@Override
	public String getLatestApplicationNumberByPermitNo(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String vehicleNO = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select pm_application_no from public.nt_t_pm_application  where pm_permit_no =? \r\n"
					+ "order by pm_created_date desc limit 1 ";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNO = (rs.getString("pm_application_no"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNO;
	}

	@Override
	public String getActiveApplicationNumberByPermitNo(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String vehicleNO = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select pm_application_no from public.nt_t_pm_application  where pm_permit_no =? and pm_status ='A' \r\n"
					+ "order by pm_created_date desc limit 1 ";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNO = (rs.getString("pm_application_no"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNO;
	}

	@Override
	public List<String> getLatestApplicationNumberList(String vehicleNumber) {

		List<String> dataList = new ArrayList<String>();
		Connection con = null;
		String query;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String appNo = null;

		try {
			con = ConnectionManager.getConnection();

			query = "select pm_application_no from public.nt_t_pm_application  where pm_vehicle_regno =? \r\n"
					+ "order by pm_created_date desc";
			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNumber);
			rs = ps.executeQuery();

			while (rs.next()) {

				appNo = rs.getString("pm_application_no");

				dataList.add(appNo);
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
	public List<String> getLatestApplicationNumberListByPermitNo(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> dataList = new ArrayList<String>();

		String appNo = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select pm_application_no from public.nt_t_pm_application  where pm_permit_no =? \r\n"
					+ "order by pm_created_date desc  ";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				appNo = (rs.getString("pm_application_no"));
				dataList.add(appNo);
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
	public boolean updateActionPoints(List<VehicleInspectionDTO> dataList, VehicleInspectionDTO inspectionDTO,
			String loginUser) {

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

				String sql = "UPDATE public.nt_t_vehicle_inspec_det SET   vid_remark=?, vid_exist=?, vid_modified_by=?, vid_modified_date=? WHERE vid_seq_no=? and vid_app_no=?;";

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
				stmt.setLong(5, dataList.get(i).getVehicleInspectionSeq());
				stmt.setString(6, inspectionDTO.getApplicationNo());

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
	public VehicleInspectionDTO searchOnBackBtnActionNew(String queueNo) {

		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct a.inspection_type ,a.inspection_location, l.loc_description,a.pm_inspection_status, a.pm_route_flag,a.pm_application_no, a.pm_queue_no,a.pm_permit_no,a.pm_vehicle_regno,a.seq,a.pm_route_no,a.pm_service_type,"
					+ " b.rou_description,c.description,e.pmb_chassis_no,e.pmb_engine_no,e.pmb_make,e.pmb_model,e.pmb_production_date,f.description as make, "
					+ " g.mod_description,d.que_number,h.pmo_full_name,h.pmo_nic"
					+ " from nt_t_pm_application a , nt_m_queue_master d,nt_r_route b ,nt_r_service_types c, nt_t_pm_omini_bus1 e,nt_r_make f,nt_r_model g, nt_t_pm_vehi_owner h ,nt_r_inspection_location l"
					+ " where a.pm_application_no = d.que_application_no " + " and a.pm_route_no = b.rou_number "
					+ " and a.pm_service_type= c.code " + " and a.pm_application_no = e.pmb_application_no "
					+ " and e.pmb_make=f.code "
					+ " and  e.pmb_model = g.mod_code and a.inspection_location = l.loc_code "
					+ " and a.pm_application_no = h.pmo_application_no " +

					" and d.que_number= '" + queueNo + "'" + " and to_char(d.created_date,'dd-MM-yyyy')='" + today
					+ "'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleDTO.setInspectionTypeCode(rs.getString("inspection_type"));
				VehicleDTO.setInspecLocationCode(rs.getString("inspection_location"));
				VehicleDTO.setInspecLocationDes(rs.getString("loc_description"));
				VehicleDTO.setInspectioncomplIncomplStatus(rs.getString("pm_inspection_status"));
				VehicleDTO.setRouteFlag(rs.getString("pm_route_flag"));
				VehicleDTO.setApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setOldApplicationNo(rs.getString("pm_application_no"));
				VehicleDTO.setPermitSeqNo(rs.getLong("seq"));
				VehicleDTO.setChassisNo(rs.getString("pmb_chassis_no"));
				VehicleDTO.setEngineNo(rs.getString("pmb_engine_no"));
				VehicleDTO.setManyear(rs.getString("pmb_production_date"));
				VehicleDTO.setMakeDescription(rs.getString("make"));
				VehicleDTO.setMakeTypeCode(rs.getString("pmb_make"));
				VehicleDTO.setModelTypeCode(rs.getString("pmb_model"));
				VehicleDTO.setModelDescription(rs.getString("mod_description"));
				VehicleDTO.setNicreg(rs.getString("pmo_nic"));
				VehicleDTO.setPermitNo(rs.getString("pm_permit_no"));
				VehicleDTO.setPermitOwner(rs.getString("pmo_full_name"));
				VehicleDTO.setQueueNo(rs.getString("que_number"));
				VehicleDTO.setRouteDetails(rs.getString("rou_description"));
				VehicleDTO.setRouteNo(rs.getString("pm_route_no"));
				VehicleDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				VehicleDTO.setVehicleNo(rs.getString("pm_vehicle_regno"));
				VehicleDTO.setBacklogApp(true);
			}

			if (!rs.isBeforeFirst()) {
				VehicleDTO.setQueueNo(queueNo);
				searchQueueTable(VehicleDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;

	}

	@Override
	public String getLatestInspectionStatus(String latestAppNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String vehicleNO = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pm_inspection_status   FROM public.nt_t_pm_application \r\n"
					+ "WHERE pm_application_no  =?";

			ps = con.prepareStatement(query);
			ps.setString(1, latestAppNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNO = (rs.getString("pm_inspection_status"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNO;
	}

	@Override
	public boolean checkStausForAppNoList(String busNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean dataHave = false;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_inspection_status FROM public.nt_t_pm_application x\r\n"
					+ "WHERE  pm_vehicle_regno  =? and pm_inspection_status is not null ";

			ps = con.prepareStatement(query);
			ps.setString(1, busNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				if (rs.getString("pm_inspection_status") != null && !rs.getString("pm_inspection_status").isEmpty()) {
					dataHave = true;
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataHave;

	}

	@Override
	public boolean checkInspectionDataForVehiNo(String busNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean dataHave = false;
		try {
			con = ConnectionManager.getConnection();

			String query = "select i.vid_app_no from public.nt_t_vehicle_inspec_det i\r\n"
					+ "inner join public.nt_t_pm_application a on a.pm_application_no  = i.vid_app_no \r\n"
					+ "where a.pm_vehicle_regno  =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, busNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getString("vid_app_no") != null && !rs.getString("vid_app_no").isEmpty()) {
					dataHave = true;
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dataHave;

	}

	@Override
	public String getLatestApplicationNumberForAmendmend(String vehicleNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String vehicleNO = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select pm_application_no from public.nt_t_pm_application  where  pm_application_no like 'AAP%' and pm_vehicle_regno =? \r\n"
					+ "order by pm_created_date desc limit 1 ";

			ps = con.prepareStatement(query);
			ps.setString(1, vehicleNumber);
			rs = ps.executeQuery();

			while (rs.next()) {
				vehicleNO = (rs.getString("pm_application_no"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return vehicleNO;
	}

	@Override
	public VehicleInspectionDTO getTaskDetForTodayNewForAmendmed(String vehicleNo, String appNo) {
		VehicleInspectionDTO VehicleDTO = new VehicleInspectionDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();
			String query;

			query = "SELECT  tsd_task_code, tsd_status\r\n"
					+ "FROM public.nt_t_task_det where  tsd_vehicle_no=? and tsd_app_no =? \r\n"
					+ "and to_char(created_date,'dd-MM-yyyy')= '" + today + "'";

			ps = con.prepareStatement(query);

			ps.setString(1, vehicleNo);
			ps.setString(2, appNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleDTO.setTaskDetCode(rs.getString("tsd_task_code"));
				VehicleDTO.setTaskDetStatus(rs.getString("tsd_status"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return VehicleDTO;
	}

	
	public int getVersionNumberVehicleInspecDetailsHistoryNew(String appNo, Connection con) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		int output = 0;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT vid_version " + "FROM nt_h_vehicle_inspec_det "
					+ "WHERE vid_app_no=? order by vid_version desc limit 1";

			ps = con.prepareStatement(sql);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			int count = 0;
			while (rs.next()) {
				count = rs.getInt("vid_version");
			}

			if (count == 0) {
				count = 1;
				output = count;

			} else {
				count++;
				output = count;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			
		}
		return output;
	}
	
	@Override
	public boolean isNewPermit(String queueNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean retunval=false;

        java.util.Date date = new java.util.Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(date);

		try {
			con = ConnectionManager.getConnection();

			String query = "select que_permit_no from nt_m_queue_master "
					+ "where que_number = ? and que_permit_no is null and que_date like '"
                    + today + "%'";

			ps = con.prepareStatement(query);
			ps.setString(1, queueNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				if(rs.getString("que_permit_no") == null)
				{
					retunval = true;
				}
				else retunval = false;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		return retunval;
	}
	

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		VehicleInspectionServiceImpl.logger = logger;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public boolean isOngoingRenewalsExists(String activeApplication) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean retunval = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "select a.pm_application_no, a.pm_status, t.tsd_task_code, t.tsd_status from nt_t_pm_application a "
					+ "inner join nt_t_task_det t on t.tsd_app_no = a.pm_application_no "
					+ "where a.pm_application_no = ? and a.pm_status = 'A' "
					+ "and not(t.tsd_task_code IN ('PM400','SI101') and t.tsd_status ='C')";

			ps = con.prepareStatement(query);
			ps.setString(1, activeApplication);
			rs = ps.executeQuery();

			while (rs.next()) {
				retunval = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return retunval;
	}

	@Override
	public String generateNormalInspectionApplicationNo() {
		logger.info("normal inspection generate new application no start");
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

			String sql = "SELECT app_no FROM public.nt_r_number_generation where code='PAP' ;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("app_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "PAP" + currYear + ApprecordcountN;
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
					strAppNo = "PAP" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "PAP" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		logger.info("normal inspection generate new application no end");
		return strAppNo;
	}

}