package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.SuspendDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class SuspendServiceImpl implements SuspendService {

	@Override
	public List<SuspendDTO> getChargeRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SuspendDTO> chargeRefNoList = new ArrayList<SuspendDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT charge_ref_no FROM nt_t_investigation_charge_master WHERE charge_app_status IN ('O', 'A') ORDER BY charge_ref_no asc ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SuspendDTO dto = new SuspendDTO();

				dto.setChargeRefNo(rs.getString("charge_ref_no"));

				chargeRefNoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return chargeRefNoList;
	}

	@Override
	public SuspendDTO searchInvestigationDetails(SuspendDTO searchDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SuspendDTO investigationDto = new SuspendDTO();

		List<DropDownDTO> chargeRefList = new ArrayList<DropDownDTO>();
		List<DropDownDTO> permitList = new ArrayList<DropDownDTO>();
		List<DropDownDTO> driverList = new ArrayList<DropDownDTO>();
		List<DropDownDTO> conductorList = new ArrayList<DropDownDTO>();

		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT A.charge_ref_no, A.charge_permit, A.charge_permit_owner, A.charge_vehicle, B.driver_id, B.conductor_id, A.app_prev_status,A.application_no "
					+ "FROM public.nt_t_investigation_charge_master A, nt_t_investigation_driver_conductor_det B "
					+ "WHERE A.charge_ref_no = B.charge_ref_no " + "AND A.charge_app_status IN ('O', 'A') ";

			String chargeRef = null;
			String vehicleNo = null;
			String permitNo = null;
			String permitOwner = null;

			if (searchDTO.getChargeRefNo() != null && !searchDTO.getChargeRefNo().trim().isEmpty()) {
				chargeRef = searchDTO.getChargeRefNo();
				query = query + " and A.charge_ref_no = ? order by A.created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, chargeRef);

			} else if (searchDTO.getVehicleNo() != null && !searchDTO.getVehicleNo().trim().isEmpty()) {
				vehicleNo = searchDTO.getVehicleNo();
				query = query + " and A.charge_vehicle = ? order by A.created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, vehicleNo);

			} else if (searchDTO.getPermitNo() != null && !searchDTO.getPermitNo().trim().isEmpty()) {
				permitNo = searchDTO.getPermitNo();
				query = query + " and A.charge_permit = ? order by A.created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, permitNo);

			} else if (searchDTO.getPermitOwnerName() != null && !searchDTO.getPermitOwnerName().trim().isEmpty()) {
				permitOwner = searchDTO.getPermitOwnerName();
				query = query + " and A.charge_permit_owner = ? order by A.created_date ";
				ps = con.prepareStatement(query);
				ps.setString(1, permitOwner);

			}

			if (chargeRef != null || vehicleNo != null || permitNo != null || permitOwner != null) {
				rs = ps.executeQuery();

				while (rs.next()) {
					DropDownDTO chargeRefDto = new DropDownDTO();
					DropDownDTO permitDto = new DropDownDTO();
					DropDownDTO driverDto = new DropDownDTO();
					DropDownDTO conductorDto = new DropDownDTO();

					investigationDto.setChargeRefNo(rs.getString("charge_ref_no"));
					investigationDto.setPermitNo(rs.getString("charge_permit"));
					investigationDto.setPermitOwnerName(rs.getString("charge_permit_owner"));
					investigationDto.setVehicleNo(rs.getString("charge_vehicle"));
					investigationDto.setDriverID(rs.getString("driver_id"));
					investigationDto.setConductorID(rs.getString("conductor_id"));
					investigationDto.setAppPrvStatus(rs.getString("app_prev_status"));
					investigationDto.setApplicationNo(rs.getString("application_no"));

					chargeRefDto.setCode(investigationDto.getChargeRefNo());
					chargeRefList.add(chargeRefDto);

					permitDto.setCode(investigationDto.getPermitNo());
					permitList.add(permitDto);

					driverDto.setCode(investigationDto.getDriverID());
					driverList.add(driverDto);

					conductorDto.setCode(investigationDto.getConductorID());
					conductorList.add(conductorDto);

					investigationDto.setChargeRefList(chargeRefList);
					investigationDto.setPermitList(permitList);
					investigationDto.setDriverList(driverList);
					investigationDto.setConductorList(conductorList);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return investigationDto;
	}

	@Override
	public List<DropDownDTO> getSuspendTypeList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DropDownDTO> suspendTypeList = new ArrayList<DropDownDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT * FROM nt_r_suspend_types WHERE status IN ('A') ORDER BY code asc ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();

				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));

				suspendTypeList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return suspendTypeList;
	}

	@Override
	public boolean saveSuspendMaster(SuspendDTO suspendDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			query = "INSERT INTO public.nt_t_suspend_master " + "(suspend_ref_no, " + "suspend_charge_ref_no, "
					+ "suspend_category, " + "suspend_permit, " + "suspend_permit_owner, " + "suspend_vehicle, "
					+ "suspend_driver_id, " + "suspend_conductor_id, " + "suspend_type, " + "suspend_reason, "
					+ "suspend_start_date, " + "suspend_end_date, " + "suspend_status, " + "suspend_current_status, "
					+ "created_by, " + "created_date)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			ps = con.prepareStatement(query);

			DateFormat dfYear = new SimpleDateFormat("yy");
			String currentYear = dfYear.format(Calendar.getInstance().getTime());

			DateFormat dfMonth = new SimpleDateFormat("MM");
			String currentMonth = dfMonth.format(Calendar.getInstance().getTime());

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_suspend_master");
			String suspendRef = "S" + currentYear + currentMonth + String.format("%04d", seqNo);

			ps.setString(1, suspendRef);
			ps.setString(2, suspendDTO.getChargeRefNo());
			ps.setString(3, suspendDTO.getSuspendCategory());
			ps.setString(4, suspendDTO.getPermitNo());
			ps.setString(5, suspendDTO.getPermitOwnerName());
			ps.setString(6, suspendDTO.getVehicleNo());
			ps.setString(7, suspendDTO.getDriverID());
			ps.setString(8, suspendDTO.getConductorID());
			ps.setString(9, suspendDTO.getSuspendTypeCode());
			ps.setString(10, suspendDTO.getSuspendReason());

			java.sql.Date sqlStartDate = new java.sql.Date(suspendDTO.getSuspendStartDate().getTime());
			ps.setDate(11, sqlStartDate);

			java.sql.Date sqlEndDate = new java.sql.Date(suspendDTO.getSuspendEndDate().getTime());
			ps.setDate(12, sqlEndDate);

			ps.setString(13, suspendDTO.getSuspendStatus());
			ps.setString(14, "SS");
			ps.setString(15, suspendDTO.getCreatedUser());
			ps.setTimestamp(16, timestamp);

			ps.executeUpdate();

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
	public List<SuspendDTO> getSavedSuspendRecords() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SuspendDTO> suspendList = new ArrayList<SuspendDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT suspend_ref_no, suspend_charge_ref_no, "
					+ "suspend_category, suspend_permit, suspend_permit_owner, "
					+ "suspend_vehicle, suspend_driver_id, suspend_conductor_id, "
					+ "suspend_type, suspend_reason, suspend_start_date, suspend_end_date, "
					+ "suspend_status, suspend_current_status, B.description ,A.application_no, A.app_prev_status "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B. code AND suspend_status not in ('RS', 'I')"
					+ "ORDER BY suspend_ref_no DESC";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SuspendDTO dto = new SuspendDTO();

				dto.setSuspendRefNo(rs.getString("suspend_ref_no"));
				dto.setChargeRefNo(rs.getString("suspend_charge_ref_no"));
				dto.setSuspendCategory(rs.getString("suspend_category"));
				dto.setPermitNo(rs.getString("suspend_permit"));
				dto.setPermitOwnerName(rs.getString("suspend_permit_owner"));
				dto.setVehicleNo(rs.getString("suspend_vehicle"));
				dto.setDriverID(rs.getString("suspend_driver_id"));
				dto.setConductorID(rs.getString("suspend_conductor_id"));
				dto.setSuspendTypeCode(rs.getString("suspend_type"));
				dto.setSuspendTypeDesc(rs.getString("description"));
				dto.setSuspendReason(rs.getString("suspend_reason"));
				dto.setSuspendStartDate(rs.getDate("suspend_start_date"));
				dto.setSuspendEndDate(rs.getDate("suspend_end_date"));

				dto.setSuspendStatus(rs.getString("suspend_status"));

				if (dto.getSuspendStatus().equalsIgnoreCase("A")) {
					dto.setSuspendStatusDesc("Initiate");
				} else {
					dto.setSuspendStatusDesc("Re-Active");
				}

				dto.setCurrentStatus(rs.getString("suspend_current_status"));

				String currStatusDesc = getStatusDesc(dto.getCurrentStatus());
				dto.setCurrentStatusDesc(currStatusDesc);

				dto.setApplicationNo(rs.getString("application_no"));
				dto.setAppPrvStatus(rs.getString("app_prev_status"));

				suspendList.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return suspendList;
	}

	@Override
	public List<SuspendDTO> getSavedSuspendRecordsByChargeRef(String chargeRef) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SuspendDTO> suspendList = new ArrayList<SuspendDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT suspend_ref_no, suspend_charge_ref_no, "
					+ "suspend_category, suspend_permit, suspend_permit_owner, "
					+ "suspend_vehicle, suspend_driver_id, suspend_conductor_id, "
					+ "suspend_type, suspend_reason, suspend_start_date, suspend_end_date, "
					+ "suspend_status, suspend_current_status, B.description, A.application_no, A.app_prev_status "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B. code  AND A.suspend_status not in ('RS', 'I') "
					+ "AND A.suspend_charge_ref_no = ? " + "ORDER BY suspend_ref_no ASC";

			ps = con.prepareStatement(query);
			ps.setString(1, chargeRef);
			rs = ps.executeQuery();

			while (rs.next()) {
				SuspendDTO dto = new SuspendDTO();

				dto.setSuspendRefNo(rs.getString("suspend_ref_no"));
				dto.setChargeRefNo(rs.getString("suspend_charge_ref_no"));
				dto.setSuspendCategory(rs.getString("suspend_category"));
				dto.setPermitNo(rs.getString("suspend_permit"));
				dto.setPermitOwnerName(rs.getString("suspend_permit_owner"));
				dto.setVehicleNo(rs.getString("suspend_vehicle"));
				dto.setDriverID(rs.getString("suspend_driver_id"));
				dto.setConductorID(rs.getString("suspend_conductor_id"));
				dto.setSuspendTypeCode(rs.getString("suspend_type"));
				dto.setSuspendTypeDesc(rs.getString("description"));
				dto.setSuspendReason(rs.getString("suspend_reason"));
				dto.setSuspendStartDate(rs.getDate("suspend_start_date"));
				dto.setSuspendEndDate(rs.getDate("suspend_end_date"));

				dto.setSuspendStatus(rs.getString("suspend_status"));

				if (dto.getSuspendStatus().equalsIgnoreCase("A")) {
					dto.setSuspendStatusDesc("Initiate");
				} else {
					dto.setSuspendStatusDesc("Re-Active");
				}

				dto.setCurrentStatus(rs.getString("suspend_current_status"));

				dto.setApplicationNo(rs.getString("application_no"));
				dto.setAppPrvStatus(rs.getString("app_prev_status"));

				suspendList.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return suspendList;
	}

	@Override
	public boolean updateSuspendRecord(SuspendDTO suspendDTO) {
		Connection con = null;
		PreparedStatement ps = null, ps1 = null, ps2 = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String query = "";

			query = "UPDATE public.nt_t_suspend_master " + "SET suspend_type = ?, " + "suspend_reason = ?, "
					+ "suspend_start_date = ?, " + "suspend_end_date = ?, " + "suspend_status = ?,"
					+ "suspend_current_status = ?, " + "modified_by = ?, " + "modified_date = ? "
					+ "WHERE suspend_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, suspendDTO.getSuspendTypeCode());
			ps.setString(2, suspendDTO.getSuspendReason());

			java.sql.Date sqlStartDate = new java.sql.Date(suspendDTO.getSuspendStartDate().getTime());
			ps.setDate(3, sqlStartDate);

			java.sql.Date sqlEndDate = new java.sql.Date(suspendDTO.getSuspendEndDate().getTime());
			ps.setDate(4, sqlEndDate);

			if (suspendDTO.getSuspendStatus().equals("I")) {
				ps.setString(5, "RS");
			} else {
				ps.setString(5, suspendDTO.getSuspendStatus());
			}
			if (suspendDTO.getSuspendStatus().equals("I")) {
				ps.setString(6, "RS");
			} else {
				ps.setString(6, suspendDTO.getCurrentStatus());
			}

			ps.setString(7, suspendDTO.getCreatedUser());
			ps.setTimestamp(8, timestamp);
			ps.setString(9, suspendDTO.getSuspendRefNo());

			if (suspendDTO.getApplicationNo() != null) {
				if (suspendDTO.getSuspendStatus().equals("I") && suspendDTO.getPermitNo() != null) {
					String queryN = "UPDATE public.nt_t_pm_application " + "SET pm_status = ? "
							+ "WHERE pm_permit_no = ? " + "AND pm_application_no = ? ";

					ps1 = con.prepareStatement(queryN);
					ps1.setString(1, suspendDTO.getAppPrvStatus());
					ps1.setString(2, suspendDTO.getPermitNo());
					ps1.setString(3, suspendDTO.getApplicationNo());
					ps1.executeUpdate();

				}
			}

			if (suspendDTO.getApplicationNo() != null) {
				if (suspendDTO.getSuspendStatus() == "I" && suspendDTO.getDriverID() != null
						|| suspendDTO.getConductorID() != null) {
					String queryND = "UPDATE public.nt_t_driver_conductor_registration " + "SET dcr_status = ? "
							+ "AND dcr_app_no = ?  ";

					ps2 = con.prepareStatement(queryND);
					ps2.setString(1, suspendDTO.getAppPrvStatus());
					ps2.setString(2, suspendDTO.getApplicationNo());
					ps2.executeUpdate();
				}
			}

			ps.executeUpdate();

			con.commit();
		
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public int checkDuplicate(SuspendDTO checkDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int checkDuplicate = 0;
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT COUNT(*) AS dup_check FROM public.nt_t_suspend_master " + "WHERE suspend_charge_ref_no = ? "
					+ "AND suspend_category = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, checkDTO.getChargeRefNo());
			ps.setString(2, checkDTO.getSuspendCategory());

			rs = ps.executeQuery();

			while (rs.next()) {
				checkDuplicate = rs.getInt("dup_check");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return checkDuplicate;
	}

	@Override
	public List<String> filterPermitsForApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> filterList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct(suspend_permit) FROM nt_t_suspend_master WHERE suspend_status = 'A' AND suspend_current_status IN ('SS', 'SA') ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				filterList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return filterList;
	}

	@Override
	public List<String> filterVehicleForApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> filterList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct(suspend_vehicle) FROM nt_t_suspend_master WHERE suspend_status = 'A' AND suspend_current_status IN ('SS', 'SA') ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				filterList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return filterList;
	}

	@Override
	public List<String> filterDriverForApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> filterList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct(suspend_driver_id) FROM nt_t_suspend_master WHERE suspend_status = 'A' AND suspend_current_status IN ('SS', 'SA') ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				filterList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return filterList;
	}

	@Override
	public List<String> filterConductorForApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> filterList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct(suspend_conductor_id) FROM nt_t_suspend_master WHERE suspend_status = 'A' AND suspend_current_status in ('SS', 'SA') ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				filterList.add(rs.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return filterList;
	}

	@Override
	public List<SuspendDTO> searchSuspendDetailsForApprove(SuspendDTO searchDTO, String isDirector) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SuspendDTO> suspendList = new ArrayList<SuspendDTO>();

		List<SuspendDTO> permitList = new ArrayList<SuspendDTO>();
		List<SuspendDTO> driverList = new ArrayList<SuspendDTO>();
		List<SuspendDTO> conductorList = new ArrayList<SuspendDTO>();

		List<SuspendDTO> finalList = new ArrayList<SuspendDTO>();

		try {
			con = ConnectionManager.getConnection();

			String queryA = "SELECT A.*, B.description "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B.code " + "AND suspend_status IN ('A') "
					+ "AND suspend_current_status IN ('SS') " + "AND suspend_category LIKE COALESCE(?, '%') "
					+ "AND suspend_type LIKE COALESCE(?, '%') " + "AND suspend_vehicle LIKE COALESCE(?, '%') ";

			String queryB = "SELECT A.*, B.description "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B.code " + "AND suspend_status IN ('A') "
					+ "AND suspend_current_status IN ('SA') " + "AND suspend_category LIKE COALESCE(?, '%') "
					+ "AND suspend_type LIKE COALESCE(?, '%') " + "AND suspend_vehicle LIKE COALESCE(?, '%') ";

			String queryC = "SELECT A.*, B.description "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B.code " + "AND suspend_status IN ('A') "
					+ "AND suspend_current_status IN ('SS', 'SA', 'DA') "
					+ "AND suspend_category LIKE COALESCE(?, '%') " + "AND suspend_type LIKE COALESCE(?, '%') "
					+ "AND suspend_vehicle LIKE COALESCE(?, '%') ";

			String query2 = "SELECT A.*, B.description "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B.code " + "AND suspend_permit = ? ";

			String query3 = "SELECT A.*, B.description "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B.code " + "AND suspend_driver_id = ? ";

			String query4 = "SELECT A.*, B.description "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B.code " + "AND suspend_conductor_id = ? ";

			String suspendCategory = null;
			String suspendType = null;
			String suspendVehicle = null;
			String suspendPermit = null;
			String suspendDriver = null;
			String suspendConductor = null;

			if (searchDTO.getSuspendCategory() != null && !searchDTO.getSuspendCategory().trim().isEmpty()) {
				suspendCategory = searchDTO.getSuspendCategory().trim();
			}

			if (searchDTO.getSuspendTypeCode() != null && !searchDTO.getSuspendTypeCode().trim().isEmpty()) {
				suspendType = searchDTO.getSuspendTypeCode().trim();
			}

			if (searchDTO.getVehicleNo() != null && !searchDTO.getVehicleNo().trim().isEmpty()) {
				suspendVehicle = searchDTO.getVehicleNo().trim();
			}

			if (searchDTO.getPermitNo() != null && !searchDTO.getPermitNo().trim().isEmpty()) {
				suspendPermit = searchDTO.getPermitNo().trim();
			}

			if (searchDTO.getDriverID() != null && !searchDTO.getDriverID().trim().isEmpty()) {
				suspendDriver = searchDTO.getDriverID().trim();
			}

			if (searchDTO.getConductorID() != null && !searchDTO.getConductorID().trim().isEmpty()) {
				suspendConductor = searchDTO.getConductorID().trim();
			}

			if (isDirector.equalsIgnoreCase("Y")) {
				ps = con.prepareStatement(queryB);
			} else if (isDirector.equalsIgnoreCase("D")) {
				ps = con.prepareStatement(queryA);
			} else {
				ps = con.prepareStatement(queryC);
			}

			ps.setString(1, suspendCategory);
			ps.setString(2, suspendType);
			ps.setString(3, suspendVehicle);

			rs = ps.executeQuery();

			while (rs.next()) {
				SuspendDTO dto = new SuspendDTO();

				dto.setSuspendRefNo(rs.getString("suspend_ref_no"));
				dto.setChargeRefNo(rs.getString("suspend_charge_ref_no"));
				dto.setSuspendCategory(rs.getString("suspend_category"));
				dto.setPermitNo(rs.getString("suspend_permit"));
				dto.setPermitOwnerName(rs.getString("suspend_permit_owner"));
				dto.setVehicleNo(rs.getString("suspend_vehicle"));
				dto.setDriverID(rs.getString("suspend_driver_id"));
				dto.setConductorID(rs.getString("suspend_conductor_id"));
				dto.setSuspendTypeCode(rs.getString("suspend_type"));
				dto.setSuspendTypeDesc(rs.getString("description"));
				dto.setSuspendReason(rs.getString("suspend_reason"));
				dto.setSuspendStartDate(rs.getDate("suspend_start_date"));
				dto.setSuspendEndDate(rs.getDate("suspend_end_date"));
				dto.setSuspendStatus(rs.getString("suspend_status"));

				dto.setCurrentStatus(rs.getString("suspend_current_status"));
				if (dto.getCurrentStatus() != null) {
					if (dto.getCurrentStatus().equalsIgnoreCase("SS")) {
						dto.setSuspendStatusDesc("SUSPEND INITIATED");

					}

					else if (dto.getCurrentStatus().equalsIgnoreCase("SA")) {

						dto.setSuspendStatusDesc("SUSPEND APPROVED");
					}

					else if (dto.getCurrentStatus().equalsIgnoreCase("DA")) {

						dto.setSuspendStatusDesc("DIRECTOR APPROVED");
					}

					else if (dto.getCurrentStatus().equalsIgnoreCase("DR")) {

						dto.setSuspendStatusDesc("DIRECTOR REJECTED");
					}
				}
				suspendList.add(dto);

			}
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);

			if (suspendPermit != null) {
				ps = con.prepareStatement(query2);
				ps.setString(1, suspendPermit);
				rs = ps.executeQuery();

				while (rs.next()) {
					SuspendDTO dto = new SuspendDTO();
					dto.setSuspendRefNo(rs.getString("suspend_ref_no"));
					permitList.add(dto);
				}
			}
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);

			if (suspendDriver != null) {
				ps = con.prepareStatement(query3);
				ps.setString(1, suspendDriver);
				rs = ps.executeQuery();

				while (rs.next()) {
					SuspendDTO dto = new SuspendDTO();
					dto.setSuspendRefNo(rs.getString("suspend_ref_no"));
					driverList.add(dto);
				}
			}
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);

			if (suspendConductor != null) {
				ps = con.prepareStatement(query4);
				ps.setString(1, suspendConductor);
				rs = ps.executeQuery();

				while (rs.next()) {
					SuspendDTO dto = new SuspendDTO();
					dto.setSuspendRefNo(rs.getString("suspend_ref_no"));
					conductorList.add(dto);
				}
			}

			if (suspendList.size() != 0) {
				if (permitList.size() == 0 && driverList.size() == 0 && conductorList.size() == 0) {
					finalList = suspendList;
				}

				if (permitList.size() != 0) {
					for (SuspendDTO dto : permitList) {
						String susRef = dto.getSuspendRefNo();
						for (SuspendDTO dto1 : suspendList) {
							if (dto1.getSuspendRefNo().equalsIgnoreCase(susRef)) {
								finalList.add(dto1);
							}
						}
					}
				}

				if (driverList.size() != 0) {
					for (SuspendDTO dto : driverList) {
						String susRef = dto.getSuspendRefNo();
						for (SuspendDTO dto1 : suspendList) {
							if (dto1.getSuspendRefNo().equalsIgnoreCase(susRef)) {
								finalList.add(dto1);
							}
						}
					}
				}

				if (conductorList.size() != 0) {
					for (SuspendDTO dto : conductorList) {
						String susRef = dto.getSuspendRefNo();
						for (SuspendDTO dto1 : suspendList) {
							if (dto1.getSuspendRefNo().equalsIgnoreCase(susRef)) {
								finalList.add(dto1);
							}
						}
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return finalList;
	}

	@Override
	public boolean updateApproveStatus(SuspendDTO approveDto, String remark, String des) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_suspend_master " + "SET suspend_current_status = ?, "
					+ "approved_by = ?, " + "approved_date = ? ,suspend_reject_reason =? "
					+ "WHERE suspend_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, approveDto.getCurrentStatus());
			ps.setString(2, approveDto.getCreatedUser());
			ps.setTimestamp(3, timestamp);
			ps.setString(4, remark);
			ps.setString(5, approveDto.getSuspendRefNo());

			ps.executeUpdate();

			con.commit();
			
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean updateDirectorApproveStatus(SuspendDTO dirApproveDto, String prevApp, String preAppStatus, String des) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_suspend_master " + "SET suspend_current_status = ?, "
					+ "director_approved_by = ?, " + "director_approved_date = ?, " + "application_no  = ?, "
					+ "app_prev_status  = ? " + "WHERE suspend_ref_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, dirApproveDto.getCurrentStatus());
			ps.setString(2, dirApproveDto.getCreatedUser());
			ps.setTimestamp(3, timestamp);
			ps.setString(4, prevApp);
			ps.setString(5, preAppStatus);
			ps.setString(6, dirApproveDto.getSuspendRefNo());

			ps.executeUpdate();

			con.commit();
		
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean updatePermitStatus(SuspendDTO selectedDTO, String permitNo, String status, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			/** insert into history table nt_h_application by tharushi.e **/

			String query1 = "INSERT into public.nt_h_pm_application_new  "
					+ " (SELECT * FROM public.nt_t_pm_application WHERE pm_permit_no  = ? and pm_status not in ('I','G')) ";
			ps1 = con.prepareStatement(query1);
			ps1.setString(1, permitNo);
			ps1.executeUpdate();

			String query = "UPDATE public.nt_t_pm_application " + "SET pm_status = ?, " + "pm_modified_by = ?, "
					+ "pm_modified_date = ? " + "WHERE pm_permit_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, status);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, permitNo);

			ps.executeUpdate();

			con.commit();
			
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean saveTaskHis(String vehicleNo, String chargeRef, String taskCode, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "INSERT INTO public.nt_h_task_his " + "(tsd_seq, " + "tsd_vehicle_no, " + "tsd_app_no, "
					+ "tsd_task_code, " + "tsd_status, " + "created_by, " + "created_date)" + " VALUES(?,?,?,?,?,?,?)";

			ps = con.prepareStatement(query);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_task_his");

			ps.setLong(1, seqNo);
			ps.setString(2, vehicleNo);
			ps.setString(3, chargeRef);
			ps.setString(4, taskCode);
			ps.setString(5, user);
			ps.setTimestamp(16, timestamp);

			ps.executeUpdate();

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
	public List<SuspendDTO> getSuspendListByPermitNo(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SuspendDTO> suspendList = new ArrayList<SuspendDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT suspend_ref_no, suspend_charge_ref_no, "
					+ "case when suspend_category = 'P' then 'Permit'when suspend_category = 'C' then 'Conductor Id'when suspend_category = 'D' then 'Driver Id' else 'N/A' end as suspend_category, "
					+ "suspend_permit_owner,suspend_permit, "
					+ "suspend_vehicle, suspend_driver_id, suspend_conductor_id, "
					+ "suspend_type, suspend_reason, suspend_start_date, suspend_end_date, "
					+ "suspend_status, suspend_current_status, B.description "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B. code and suspend_permit=? " + "ORDER BY suspend_ref_no DESC";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SuspendDTO dto = new SuspendDTO();

				dto.setSuspendRefNo(rs.getString("suspend_ref_no"));
				dto.setChargeRefNo(rs.getString("suspend_charge_ref_no"));
				dto.setSuspendCategory(rs.getString("suspend_category"));
				dto.setPermitNo(rs.getString("suspend_permit"));
				dto.setPermitOwnerName(rs.getString("suspend_permit_owner"));
				dto.setVehicleNo(rs.getString("suspend_vehicle"));
				dto.setDriverID(rs.getString("suspend_driver_id"));
				dto.setConductorID(rs.getString("suspend_conductor_id"));
				dto.setSuspendTypeCode(rs.getString("suspend_type"));
				dto.setSuspendTypeDesc(rs.getString("description"));
				dto.setSuspendReason(rs.getString("suspend_reason"));
				dto.setSuspendStartDate(rs.getDate("suspend_start_date"));
				dto.setSuspendEndDate(rs.getDate("suspend_end_date"));

				dto.setSuspendStatus(rs.getString("suspend_status"));

				if (dto.getSuspendStatus().equalsIgnoreCase("A")) {
					dto.setSuspendStatusDesc("Initiate");
				} else {
					dto.setSuspendStatusDesc("Re-Active");
				}

				dto.setCurrentStatus(rs.getString("suspend_current_status"));

				suspendList.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return suspendList;
	}

	@Override
	public SuspendDTO getDetasilsDTOByPermitNO(String permitNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SuspendDTO dto = new SuspendDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT suspend_ref_no, suspend_charge_ref_no, "
					+ "case when suspend_category = 'P' then 'Permit'when suspend_category = 'C' then 'Conductor Id'when suspend_category = 'D' then 'Driver Id' else 'N/A' end as suspend_category, "
					+ "suspend_permit_owner,suspend_permit, "
					+ "suspend_vehicle, suspend_driver_id, suspend_conductor_id, "
					+ "suspend_type, suspend_reason, suspend_start_date, suspend_end_date, "
					+ "suspend_status, suspend_current_status, B.description "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B. code and suspend_permit=? " + "ORDER BY suspend_ref_no DESC";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNO);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new SuspendDTO();

				dto.setSuspendRefNo(rs.getString("suspend_ref_no"));
				dto.setChargeRefNo(rs.getString("suspend_charge_ref_no"));
				dto.setSuspendCategory(rs.getString("suspend_category"));
				dto.setPermitNo(rs.getString("suspend_permit"));
				dto.setPermitOwnerName(rs.getString("suspend_permit_owner"));
				dto.setVehicleNo(rs.getString("suspend_vehicle"));
				dto.setDriverID(rs.getString("suspend_driver_id"));
				dto.setConductorID(rs.getString("suspend_conductor_id"));
				dto.setSuspendTypeCode(rs.getString("suspend_type"));
				dto.setSuspendTypeDesc(rs.getString("description"));
				dto.setSuspendReason(rs.getString("suspend_reason"));
				dto.setSuspendStartDate(rs.getDate("suspend_start_date"));
				dto.setSuspendEndDate(rs.getDate("suspend_end_date"));

				dto.setSuspendStatus(rs.getString("suspend_status"));

				if (dto.getSuspendStatus().equalsIgnoreCase("A")) {
					dto.setSuspendStatusDesc("Initiate");
				} else {
					dto.setSuspendStatusDesc("Re-Active");
				}

				dto.setCurrentStatus(rs.getString("suspend_current_status"));

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
	public String getNameByDCId(String id) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strName = null;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT dcr_full_name_sin ,dcr_name_with_initials  "
					+ " FROM nt_t_driver_conductor_registration " + " WHERE  dcr_driver_conductor_id  = ? ";
			ps = con.prepareStatement(sql1);

			ps.setString(1, id);

			rs = ps.executeQuery();

			while (rs.next()) {
				strName = rs.getString("dcr_name_with_initials");

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return strName;
	}

	@Override
	public String getStatusDesc(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT inv_status_desc FROM public.nt_r_investigation_status "
					+ "WHERE inv_status_code = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, code);

			rs = ps.executeQuery();

			while (rs.next()) {
				status = rs.getString("inv_status_desc");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return status;
	}

	@Override
	public boolean updateDriverConductorStatus(SuspendDTO selectedDTO, String DCid, String status, String user, String des) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();

			String query1 = "INSERT into public.nt_h_driver_conductor_registration  "
					+ " (SELECT * FROM public.nt_t_driver_conductor_registration WHERE dcr_driver_conductor_id   = ? and dcr_status not in ('I','B')) ";

			ps1 = con.prepareStatement(query1);
			ps1.setString(1, DCid);
			ps1.executeUpdate();

			String query = "UPDATE public.nt_t_driver_conductor_registration " + "SET dcr_status = ?, "
					+ "dcr_modified_by  = ?, " + "dcr_modified_date  = ? ," + "dcr_status_type =? "
					+ "WHERE dcr_driver_conductor_id = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, status);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, "S");
			ps.setString(5, DCid);

			ps.executeUpdate();

			con.commit();
			
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public SuspendDTO getPreviousAppDet(String permitNo, String driverId, String conductorId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		SuspendDTO dto = new SuspendDTO();

		try {
			con = ConnectionManager.getConnection();
			if (permitNo != null && !permitNo.isEmpty()) {
				String query = "select pm_application_no,pm_status from public.nt_t_pm_application\r\n"
						+ "where pm_permit_no=?   and pm_status not in('I','G')  order by pm_created_date  desc limit 1";

				ps = con.prepareStatement(query);
				ps.setString(1, permitNo);
				rs = ps.executeQuery();

				while (rs.next()) {
					dto = new SuspendDTO();

					dto.setPreAppNo(rs.getString("pm_application_no"));
					dto.setPreAppStatus(rs.getString("pm_status"));

				}

			} else if (driverId != null || conductorId != null) {
				String query = "select dcr_app_no,dcr_status from public.nt_t_driver_conductor_registration\r\n"
						+ "where dcr_driver_conductor_id=? and dcr_status not in('I','B') order by dcr_created_date desc limit 1";

				ps = con.prepareStatement(query);
				if (driverId != null && !driverId.isEmpty()) {
					ps.setString(1, driverId);
				} else if (conductorId != null && !conductorId.isEmpty()) {
					ps.setString(1, conductorId);
				}
				rs = ps.executeQuery();

				while (rs.next()) {
					dto = new SuspendDTO();

					dto.setPreAppNo(rs.getString("dcr_app_no"));
					dto.setPreAppStatus(rs.getString("dcr_status"));

				}

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
	public List<SuspendDTO> getChargeRefNoListforSearch() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SuspendDTO> chargeRefNoList = new ArrayList<SuspendDTO>();
		String query;

		try {
			con = ConnectionManager.getConnection();

			query = "SELECT distinct suspend_charge_ref_no FROM nt_t_suspend_master WHERE suspend_status not in ('RS', 'I') ORDER by suspend_charge_ref_no; ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				SuspendDTO dto = new SuspendDTO();

				dto.setChargeRefNo(rs.getString("suspend_charge_ref_no"));

				chargeRefNoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return chargeRefNoList;
	}

	@Override
	public List<String> getAllVehicle() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> drpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			// String query = "SELECT pm_vehicle_regno FROM nt_t_pm_application WHERE
			// pm_status in ('A','O');";
			String query = "SELECT distinct suspend_vehicle FROM nt_t_suspend_master WHERE suspend_status not in ('RS', 'I');";

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
	public List<String> getAllPermit() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> drpdwnDTOList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			// String query = "SELECT pm_permit_no FROM nt_t_pm_application WHERE pm_status
			// in ('A','O');";
			String query = "SELECT distinct suspend_permit FROM nt_t_suspend_master WHERE suspend_status not in ('RS', 'I');";

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
	public List<SuspendDTO> getSavedSuspendRecordsSearch(SuspendDTO searchDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<SuspendDTO> suspendList = new ArrayList<SuspendDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT suspend_ref_no, suspend_charge_ref_no, "
					+ "suspend_category, suspend_permit, suspend_permit_owner, "
					+ "suspend_vehicle, suspend_driver_id, suspend_conductor_id, "
					+ "suspend_type, suspend_reason, suspend_start_date, suspend_end_date, "
					+ "suspend_status, suspend_current_status, B.description ,A.application_no, A.app_prev_status "
					+ "FROM public.nt_t_suspend_master A, public.nt_r_suspend_types B "
					+ "WHERE A.suspend_type = B. code AND A.suspend_status not in ('RS', 'I') ";

			String chargeRef = null;
			String vehicleNo = null;
			String permitNo = null;
			String permitOwner = null;

			if (searchDTO.getChargeRefNo() != null && !searchDTO.getChargeRefNo().trim().isEmpty()) {
				chargeRef = searchDTO.getChargeRefNo();
				query = query + " and A.suspend_charge_ref_no = ? order by A.suspend_charge_ref_no ";
				ps = con.prepareStatement(query);
				ps.setString(1, chargeRef);

			} else if (searchDTO.getVehicleNo() != null && !searchDTO.getVehicleNo().trim().isEmpty()) {
				vehicleNo = searchDTO.getVehicleNo();
				query = query + " and A.suspend_vehicle = ? order by A.suspend_charge_ref_no ";
				ps = con.prepareStatement(query);
				ps.setString(1, vehicleNo);

			} else if (searchDTO.getPermitNo() != null && !searchDTO.getPermitNo().trim().isEmpty()) {
				permitNo = searchDTO.getPermitNo();
				query = query + " and A.suspend_permit = ? order by A.suspend_charge_ref_no ";
				ps = con.prepareStatement(query);
				ps.setString(1, permitNo);

			} else if (searchDTO.getPermitOwnerName() != null && !searchDTO.getPermitOwnerName().trim().isEmpty()) {
				permitOwner = searchDTO.getPermitOwnerName();
				query = query + " and A.suspend_permit_owner = ? order by A.suspend_charge_ref_no ";
				ps = con.prepareStatement(query);
				ps.setString(1, permitOwner);

			}

			if (chargeRef != null || vehicleNo != null || permitNo != null || permitOwner != null) {
				rs = ps.executeQuery();

				while (rs.next()) {
					SuspendDTO dto = new SuspendDTO();

					dto.setSuspendRefNo(rs.getString("suspend_ref_no"));
					dto.setChargeRefNo(rs.getString("suspend_charge_ref_no"));
					dto.setSuspendCategory(rs.getString("suspend_category"));
					dto.setPermitNo(rs.getString("suspend_permit"));
					dto.setPermitOwnerName(rs.getString("suspend_permit_owner"));
					dto.setVehicleNo(rs.getString("suspend_vehicle"));
					dto.setDriverID(rs.getString("suspend_driver_id"));
					dto.setConductorID(rs.getString("suspend_conductor_id"));
					dto.setSuspendTypeCode(rs.getString("suspend_type"));
					dto.setSuspendTypeDesc(rs.getString("description"));
					dto.setSuspendReason(rs.getString("suspend_reason"));
					dto.setSuspendStartDate(rs.getDate("suspend_start_date"));
					dto.setSuspendEndDate(rs.getDate("suspend_end_date"));

					dto.setSuspendStatus(rs.getString("suspend_status"));

					if (dto.getSuspendStatus().equalsIgnoreCase("A")) {
						dto.setSuspendStatusDesc("Initiate");
					} else {
						dto.setSuspendStatusDesc("Re-Active");
					}

					dto.setCurrentStatus(rs.getString("suspend_current_status"));

					String currStatusDesc = getStatusDesc(dto.getCurrentStatus());
					dto.setCurrentStatusDesc(currStatusDesc);

					dto.setApplicationNo(rs.getString("application_no"));
					dto.setAppPrvStatus(rs.getString("app_prev_status"));

					suspendList.add(dto);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return suspendList;
	}
	
	
	private void insertTaskInquiryRecord(Connection con, SuspendDTO searchDTO,
			Timestamp timestamp, String status, String function,String funDes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, ref_no, permit_no,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, searchDTO.getCreatedUser());
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, searchDTO.getVehicleNo());
			psInsert.setString(5, searchDTO.getChargeRefNo());
			psInsert.setString(6, searchDTO.getPermitNo());
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
	
	public void beanLinkMethod(SuspendDTO selectedDTO, String des, String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, selectedDTO, timestamp, des, "Investigation Management",funDes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
