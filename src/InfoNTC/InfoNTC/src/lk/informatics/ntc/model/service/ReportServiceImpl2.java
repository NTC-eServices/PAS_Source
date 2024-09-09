package lk.informatics.ntc.model.service;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.util.ArrayUtil;

import lk.informatics.ntc.model.dto.CombinePanelGenaratorDTO;
import lk.informatics.ntc.model.dto.FareOfSemiLuxuryReportDTO;
import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class ReportServiceImpl2 implements ReportService2 {

	public List routeNoDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct rs_route_no " + "from nt_m_route_station "
					+ "where rs_route_no is not null ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				value = rs.getString("rs_route_no");
				returnList.add(value);

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
	public List serviceTypeDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetMaintenanceDTO> returnList = new ArrayList<LogSheetMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select description,code from nt_r_service_types where active='A'\r\n"
					+ "order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				LogSheetMaintenanceDTO logDTO = new LogSheetMaintenanceDTO();
				logDTO.setServiceCode(rs.getString("code"));
				logDTO.setServiceDescription(rs.getString("description"));

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
	public StationDetailsDTO getstartOrigin(String routeNo, int stage) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StationDetailsDTO dto = new StationDetailsDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "select  a.rs_stage,a.rs_station ,b.midpoint_name,b.midpoint_name_sin,b.midpoint_name_tamil,a.rs_route_no\r\n"
					+ "from public.nt_m_route_station a\r\n"
					+ "inner join public.nt_r_station b on a.rs_station=b.code\r\n" + "where a.rs_route_no=? \r\n"
					+ "and a.rs_stage =?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setInt(2, stage);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setStationNameSin(rs.getString("midpoint_name_sin"));
				dto.setStationNameEn(rs.getString("midpoint_name"));
				dto.setStationNameTam(rs.getString("midpoint_name_tamil"));
				dto.setStage(rs.getString("rs_stage"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public StationDetailsDTO getExampleFee(String routeNo, int stage) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StationDetailsDTO dto2 = new StationDetailsDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "select a.rs_stage , a.rs_station ,a.rs_route_no,b.midpoint_name,b.midpoint_name_sin,b.midpoint_name_tamil,c.tfc_normal_round_fee as fee\r\n"
					+ "from public.nt_m_route_station a\r\n"
					+ "inner join public.nt_r_station b on a.rs_station=b.code\r\n"
					+ "inner join public.nt_t_fee_circle c on a.rs_stage=c.tfc_stage\r\n" + "where a.rs_route_no=? \r\n"
					+ "and a.rs_stage=?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setInt(2, stage);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto2.setExampleFee(rs.getString("fee"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto2;
	}

	@Override
	public RouteDTO retrieveHighWayBusServiceFareReport(String serviceType, String route) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RouteDTO dto = new RouteDTO();

		try {
			con = ConnectionManager.getConnection();

			/** get bus fare start **/
			String query = "select tfc_highway_round_fee " + "from  public.nt_t_fee_circle "
					+ "where nt_t_fee_circle.tfc_stage =  (select max(rs_stage) "
					+ "from nt_m_route_station inner join nt_t_fee_circle "
					+ "on nt_m_route_station.rs_stage = nt_t_fee_circle.tfc_stage "
					+ "where nt_m_route_station.rs_route_no =  '" + route + "' "
					+ "and nt_m_route_station.rs_is_expressway =  'Y')";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setBusFare(rs.getBigDecimal("tfc_highway_round_fee"));
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			/** get bus fare end **/

			/** get destination & origin start **/
			String query2 = "select distinct nt_r_route.rou_number,nt_r_route.rou_service_origine,nt_r_route.rou_service_destination, "
					+ "nt_r_route.rou_service_origine_sin,nt_r_route.rou_service_destination_sin,nt_r_route.rou_tot_busfare "
					+ "from nt_r_route inner join nt_m_route_station "
					+ "on nt_m_route_station.rs_route_no = nt_r_route.rou_number " + "where nt_r_route.rou_number= '"
					+ route + "' " + "and nt_m_route_station.rs_is_expressway = 'Y'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setOrigin(rs.getString("rou_service_origine"));
				dto.setDestination(rs.getString("rou_service_destination"));
				dto.setRouteNo(rs.getString("rou_number"));
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			/** get destination & origin end **/

			/** get fare change date start **/
			String query3 = "select mfc_re_ammendment_date from nt_m_fee_circle_master " + "where mfc_status = 'A'";

			ps = con.prepareStatement(query3);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setFareChangedDate(rs.getString("mfc_re_ammendment_date"));
			}
			/** get fare change date end **/

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public List<Integer> getStagesAccordingToRouteNo(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String stages = null;

		List<Integer> stagesList = new ArrayList<Integer>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select rs_stage from public.nt_m_route_station where rs_route_no=?  and rs_is_semi_luxuary='Y' order by rs_stage LIMIT 41;";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				stages = rs.getString("rs_stage");
				stagesList.add(Integer.parseInt(stages));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return stagesList;
	}

	@Override
	public List<StationDetailsDTO> createDynamicTable(List<StationDetailsDTO> stagesList) {
		Connection con = null;
		PreparedStatement ps = null, ps2 = null;
		ResultSet rs = null;
		int numberOFColumn;
		String colname;
		try {
			con = ConnectionManager.getConnection();

			String query = "CREATE TABLE IF NOT EXISTS public.nt_temp_stages_amount();";
			ps = con.prepareStatement(query);
			ps.executeUpdate();
			con.commit();

			ConnectionManager.close(ps);

			numberOFColumn = stagesList.size();
			for (int i = 0; i < numberOFColumn; i++) {
				colname = "Column" + i;
				String sql = "ALTER TABLE public.nt_temp_stages_amount ADD " + colname + " VARCHAR(30)";
				ps2 = con.prepareStatement(sql);
				ps2.executeUpdate();
				con.commit();
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return stagesList;
	}

	@Override
	public List<StationDetailsDTO> getsemiFeeAgainstStage(List<Integer> stagesListTemp) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<StationDetailsDTO> fareList = new ArrayList<StationDetailsDTO>();
		try {
			con = ConnectionManager.getConnection();

			int min = Collections.min(stagesListTemp, null);
			int max = Collections.max(stagesListTemp, null);

			String query = "select tfc_semi_luxury_round_fee,tfc_stage from public.nt_t_fee_circle where tfc_stage between "
					+ min + " and " + max;
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO stageDTO = new StationDetailsDTO();
				stageDTO.setSemifare(rs.getString("tfc_semi_luxury_round_fee"));
				stageDTO.setStage(rs.getString("tfc_stage"));
				fareList.add(stageDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return fareList;
	}

	@Override
	public StationDetailsDTO getsemiFeeAgainstStageDTO(List<StationDetailsDTO> stagesListTemp) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StationDetailsDTO stageDTO = new StationDetailsDTO();
		try {
			con = ConnectionManager.getConnection();
			String stagesList = null;
			for (StationDetailsDTO i : stagesListTemp) {
				if (stagesList != null && stagesList.length() != 0) {
					stagesList = stagesList + "," + i.getStage();
				} else {
					stagesList = i.getStage();
				}
			}

			String query = "select tfc_semi_luxury_round_fee,tfc_stage from public.nt_t_fee_circle where tfc_stage in ("
					+ stagesList + ")";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				stageDTO.setSemifare(rs.getString("tfc_semi_luxury_round_fee"));
				stageDTO.setStage(rs.getString("tfc_stage"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return stageDTO;

	}

	@Override
	public List<StationDetailsDTO> getStagesAccordingToRouteNoDTO(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<StationDetailsDTO> stagesList = new ArrayList<StationDetailsDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select a.rs_stage, b.midpoint_name_sin from public.nt_m_route_station a,  public.nt_r_station b where a.rs_route_no=? and a.rs_is_semi_luxuary='Y' and a.rs_station=b.code order by a.rs_stage LIMIT 41";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO stageDTO = new StationDetailsDTO();
				stageDTO.setStage(rs.getString("rs_stage"));
				stageDTO.setStationNameSin(rs.getString("midpoint_name_sin"));
				stagesList.add(stageDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return stagesList;
	}

	@Override
	public void insertDataIntoNt_temp_stages_amount(List<FareOfSemiLuxuryReportDTO> dtoList, List<Integer> stages,
			List<StationDetailsDTO> stagesListDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "INSERT INTO public.nt_temp_stages_amount "
					+ "(stage0, stage1, stage2, stage3, stage4, stage5, stage6, stage7, stage8, stage9, stage10, stage11, stage12, stage13, stage14, stage15, stage16, stage17, stage18, stage19, stage20, stage21, stage22, stage23, stage24, stage25, stage26, stage27, stage28, stage29, stage30, stage31, stage32, stage33, stage34, stage35, stage36, stage37, stage38, stage39, stage40, main_stage) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
					+ "";

			stmt = con.prepareStatement(sql);

			int tempCount = 0;
			/** insert stage names start **/
			if (tempCount < stages.size()) {
				stmt.setString(1, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(1, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(2, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(2, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(3, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(3, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(4, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(4, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(5, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(5, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(6, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(6, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(7, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(7, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(8, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(8, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(9, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(9, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(10, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(10, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(11, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(11, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(12, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(12, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(13, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(13, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(14, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(14, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(15, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(15, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(16, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(16, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(17, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(17, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(18, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(18, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(19, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(19, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(20, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(20, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(21, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(21, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(22, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(22, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(23, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(23, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(24, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(24, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(25, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(25, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(26, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(26, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(27, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(27, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(28, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(28, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(29, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(29, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(30, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(30, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(31, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(31, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(32, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(32, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(33, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(33, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(34, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(34, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(35, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(35, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(36, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(36, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(37, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(37, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(38, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(38, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(39, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(39, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(40, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(40, java.sql.Types.VARCHAR);
			}
			if (tempCount < stages.size()) {
				stmt.setString(41, stagesListDTO.get(tempCount).getStationNameSin());
				tempCount = tempCount + 1;
			} else {
				stmt.setNull(41, java.sql.Types.VARCHAR);
			}

			stmt.setNull(42, java.sql.Types.INTEGER);

			stmt.executeUpdate();
			/** insert stage names end **/

			int count = 0;
			int stageCount = 0;
			for (int i = 0; i < stages.size(); i++) {

				if (stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(1, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(1, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(2, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(2, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(3, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(3, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(4, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(4, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(5, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(5, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(6, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(6, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(7, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(7, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(8, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(8, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(9, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(9, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(10, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(10, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(11, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(11, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(12, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(12, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(13, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(13, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(14, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(14, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(15, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(15, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(16, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(16, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(17, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(17, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(18, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(18, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(19, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(19, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(20, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(20, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(21, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(21, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(22, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(22, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(23, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(23, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(24, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(24, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(25, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(25, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(26, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(26, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(27, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(27, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(28, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(28, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(29, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(29, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(30, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(30, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(31, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(31, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(32, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(32, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(33, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(33, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(34, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(34, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(35, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(35, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(36, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(36, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(37, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(37, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(38, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(38, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(39, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(39, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(40, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(40, java.sql.Types.VARCHAR);
				}
				if (count < dtoList.size() && stageCount <= stages.size()
						&& dtoList.get(count).getMainStage().equalsIgnoreCase(Integer.toString(stages.get(i)))) {
					stmt.setString(41, dtoList.get(count).getFee());
					count = count + 1;
					stageCount = stageCount + 1;
				} else {
					stmt.setNull(41, java.sql.Types.VARCHAR);
				}
				stmt.setInt(42, Integer.valueOf(stages.get(i)));

				stageCount = 0;

				stmt.executeUpdate();

			}

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
	public void deleteData() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM  public.nt_temp_stages_amount ;";

			stmt = con.prepareStatement(sql);
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
	public List<Integer> getStagesAccordingToRouteNoLuxuary(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String stages = null;

		List<Integer> stagesList = new ArrayList<Integer>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select rs_stage from public.nt_m_route_station where rs_route_no=?  and rs_is_luxuary='Y' order by rs_stage LIMIT 41;";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				stages = rs.getString("rs_stage");
				stagesList.add(Integer.parseInt(stages));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return stagesList;
	}

	@Override
	public List<StationDetailsDTO> getStagesAccordingToRouteNoDTOLuxuary(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<StationDetailsDTO> stagesList = new ArrayList<StationDetailsDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select a.rs_stage, b.midpoint_name_sin from public.nt_m_route_station a,  public.nt_r_station b where a.rs_route_no=? and a.rs_is_luxuary='Y' and a.rs_station=b.code order by a.rs_stage LIMIT 41";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO stageDTO = new StationDetailsDTO();
				stageDTO.setStage(rs.getString("rs_stage"));
				stageDTO.setStationNameSin(rs.getString("midpoint_name_sin"));
				stagesList.add(stageDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return stagesList;
	}

	@Override
	public List<StationDetailsDTO> getsemiFeeAgainstStageLuxuary(List<Integer> stagesListTemp) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<StationDetailsDTO> fareList = new ArrayList<StationDetailsDTO>();
		try {
			con = ConnectionManager.getConnection();

			int min = Collections.min(stagesListTemp, null);
			int max = Collections.max(stagesListTemp, null);

			String query = "select tfc_luxury_round_fee,tfc_stage from public.nt_t_fee_circle where tfc_stage between "
					+ min + " and " + max;
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO stageDTO = new StationDetailsDTO();
				stageDTO.setSemifare(rs.getString("tfc_luxury_round_fee"));
				stageDTO.setStage(rs.getString("tfc_stage"));

				fareList.add(stageDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return fareList;
	}

	@Override
	public StationDetailsDTO checkServiceTypeForRoute(String route, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StationDetailsDTO dto = new StationDetailsDTO();
		String andPart = null;
		if (serviceType.equals("EB")) {
			andPart = "and rs_is_expressway='Y'";

		}
		if (serviceType.equals("001")) {
			andPart = "and rs_is_normal=null";

		}
		if (serviceType.equals("002")) {
			andPart = "and rs_is_luxuary='Y'";

		}
		if (serviceType.equals("003")) {
			andPart = "and rs_is_super_luxuary='Y'";

		}
		if (serviceType.equals("004")) {
			andPart = "and rs_is_semi_luxuary='Y'";

		}

		try {

			con = ConnectionManager.getConnection();

			String query = "select rs_is_normal,rs_is_luxuary,rs_is_semi_luxuary,rs_is_super_luxuary,rs_is_expressway from public.nt_m_route_station\r\n"
					+ "where rs_route_no=?" + andPart;

			ps = con.prepareStatement(query);
			ps.setString(1, route);

			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setNormalRep(rs.getString("rs_is_normal"));
				dto.setLuxuryRep(rs.getString("rs_is_luxuary"));
				dto.setSemiLuxuryRep(rs.getString("rs_is_semi_luxuary"));
				dto.setSuperLuxuryRep(rs.getString("rs_is_super_luxuary"));
				dto.setExpressrep(rs.getString("rs_is_expressway"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public String getOrginByRoute(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String origin = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select rou_service_origine,rou_service_destination\r\n"
					+ "from public.nt_r_route where rou_number=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				origin = rs.getString("rou_service_origine");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return origin;
	}

	@Override
	public String getODestinationByRoute(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String origin = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select rou_service_origine,rou_service_destination\r\n"
					+ "from public.nt_r_route where rou_number=?";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				origin = rs.getString("rou_service_destination");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return origin;
	}

	@Override
	public Map<String, List> insertTimeSlotsInTemp(List<String> timeSlotList, Map<String, List> busNoList) {
		Map<String, List> ret = null;
		List<String> list1 = new ArrayList<>();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();
			for (final java.util.Map.Entry<String, List> entry : busNoList.entrySet()) {
				final List<String> value = entry.getValue();
				for (final String val : value) {
					list1.add(val);
				}
			}

			String sql = "INSERT INTO public.nt_m_control_sheet\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql);

			Iterator<String> it1 = timeSlotList.iterator();

			stmt.addBatch();
			stmt.executeUpdate();
			con.commit();

			ConnectionManager.close(stmt);

			/** table 02 **/
			String sql2 = "INSERT INTO public.nt_m_control_sheet2\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql2);
			for (String timeS : timeSlotList) {
				stmt.setString(1, null);
				stmt.setString(2, null);
				stmt.setString(3, null);
				stmt.setString(4, null);

				stmt.setString(5, null);
				stmt.setString(6, null);
				stmt.setString(7, timeS);
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}

			ConnectionManager.close(stmt);

			/** end **/
			/** table 03 **/
			String sql3 = "INSERT INTO public.nt_m_control_sheet3\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql3);
			for (String timeS : timeSlotList) {
				stmt.setString(1, null);
				stmt.setString(2, null);
				stmt.setString(3, null);
				stmt.setString(4, null);

				stmt.setString(5, null);
				stmt.setString(6, null);
				stmt.setString(7, timeS);
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}
			ConnectionManager.close(stmt);

			/** end **/
			/** table 04 **/
			String sql4 = "INSERT INTO public.nt_m_control_sheet4\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql4);
			for (String timeS : timeSlotList) {
				stmt.setString(1, null);
				stmt.setString(2, null);
				stmt.setString(3, null);
				stmt.setString(4, null);

				stmt.setString(5, null);
				stmt.setString(6, null);
				stmt.setString(7, timeS);
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}
			ConnectionManager.close(stmt);

			/** end **/
			/** table 05 **/
			String sql5 = "INSERT INTO public.nt_m_control_sheet5\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot,col7)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?,?);;";

			stmt = con.prepareStatement(sql5);
			for (String timeS : timeSlotList) {
				stmt.setString(1, null);
				stmt.setString(2, null);
				stmt.setString(3, null);
				stmt.setString(4, null);

				stmt.setString(5, null);
				stmt.setString(6, null);
				stmt.setString(7, timeS);
				stmt.setString(8, null);
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}
			ConnectionManager.close(stmt);

			/** end **/

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return ret;
	}

	@Override
	public List<String> getTimeSlots(String tripType, String groupNo, String route, String serviceType, String refNo,
			boolean ownerSheet, String busAbbriviation) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();
		String query = null;
		String busAbbriviationNew = null;
		if (busAbbriviation != null) {
			busAbbriviationNew = busAbbriviation + "%";
		}

		try {
			con = ConnectionManager.getConnection();
//	Without SLTB & ETC
			if (ownerSheet) {
				if (tripType.equalsIgnoreCase("O")) {
					query = " select a.start_time from public.panel_generator_origin_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? and b.routeno = ? and b.buscategory = ? order by a.start_time  asc";
				} else if (tripType.equalsIgnoreCase("D")) {
					query = " select a.start_time from public.panel_generator_destination_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? and b.routeno = ? and b.buscategory = ? order by a.start_time  asc";
				}
			} else {
				if (tripType.equalsIgnoreCase("O")) {
					query = " select a.start_time from public.panel_generator_origin_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? and b.routeno = ? and b.buscategory = ? order by a.start_time  asc";
				} else if (tripType.equalsIgnoreCase("D")) {
					query = " select a.start_time from public.panel_generator_destination_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? and b.routeno = ? and b.buscategory = ? order by a.start_time  asc";
				}
			}
			
//	With SLTB & ETC
//			if (ownerSheet) {
//				if (tripType.equalsIgnoreCase("O")) {
//					query = " select a.start_time from public.panel_generator_origin_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and b.refno = ?  and b.groupcount = ? and b.routeno = ? and b.buscategory = ? order by a.start_time  asc";
//				} else if (tripType.equalsIgnoreCase("D")) {
//					query = " select a.start_time from public.panel_generator_destination_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and b.refno = ?  and b.groupcount = ? and b.routeno = ? and b.buscategory = ? order by a.start_time  asc";
//				}
//			} else {
//				if (tripType.equalsIgnoreCase("O")) {
//					query = " select a.start_time from public.panel_generator_origin_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and b.refno = ?  and b.groupcount = ? and b.routeno = ? and b.buscategory = ? order by a.start_time  asc";
//				} else if (tripType.equalsIgnoreCase("D")) {
//					query = " select a.start_time from public.panel_generator_destination_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and b.refno = ?  and b.groupcount = ? and b.routeno = ? and b.buscategory = ? order by a.start_time  asc";
//				}
//			}

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, route);
			ps.setString(4, serviceType);

			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("start_time");
				returnList.add(value);
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
	public List<String> getGroupNo(String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select no_of_bus_perweek,route_no,bus_category\r\n"
					+ "from  public.nt_m_panelgenerator \r\n"
					+ "inner join public.nt_r_service_types a on a.code=public.nt_m_panelgenerator.bus_category\r\n"
					+ "where a.active='A'\r\n" + "and ref_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				value = rs.getString("no_of_bus_perweek");
				returnList.add(value);

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
	public List<String> getRefferenceNo() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct rs_generator_ref_no  from public.nt_m_route_schedule_generator  where rs_route_ref_no is not null order by  rs_generator_ref_no ASC";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				value = rs.getString("rs_generator_ref_no");
				returnList.add(value);

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
	public String getServiceCode(String type) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String code = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT * FROM public.nt_r_service_types where description =?";

			ps = con.prepareStatement(query2);
			ps.setString(1, type);
			rs = ps.executeQuery();

			while (rs.next()) {
				code = rs.getString("code");

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return code;
	}

	@Override
	public Map<String, List> getBusNOList(String refNO, String trip, String group, List<String> timeSlotList) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;

		PreparedStatement ps3 = null;

		ResultSet rs3 = null;

		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String, List> map = new HashMap();

		String value = "";
		String value2 = " ";
		List<Object> returnList = new ArrayList<Object>();
		List<Object> returnList2 = new ArrayList<Object>();
		List<Object> returnList3 = new ArrayList<Object>();
		List<Object> returnList4 = new ArrayList<Object>();
		List<Object> returnList5 = new ArrayList<Object>();
		List<Object> returnList6 = new ArrayList<Object>();
		List<Object> returnList7 = new ArrayList<Object>();
		List<Object> returnList8 = new ArrayList<Object>();
		List<Object> returnList9 = new ArrayList<Object>();
		List<Object> returnList10 = new ArrayList<Object>();
		List<Object> returnList11 = new ArrayList<Object>();
		List<Object> returnList12 = new ArrayList<Object>();
		List<Object> returnList13 = new ArrayList<Object>();
		List<Object> returnList14 = new ArrayList<Object>();
		List<Object> returnList15 = new ArrayList<Object>();
		List<Object> returnList16 = new ArrayList<Object>();
		List<Object> returnList17 = new ArrayList<Object>();
		List<Object> returnList18 = new ArrayList<Object>();
		List<Object> returnList19 = new ArrayList<Object>();
		List<Object> returnList20 = new ArrayList<Object>();
		List<Object> returnList21 = new ArrayList<Object>();
		List<Object> returnList22 = new ArrayList<Object>();
		List<Object> returnList23 = new ArrayList<Object>();
		List<Object> returnList24 = new ArrayList<Object>();
		List<Object> returnList25 = new ArrayList<Object>();
		List<Object> returnList26 = new ArrayList<Object>();
		List<Object> returnList27 = new ArrayList<Object>();
		List<Object> returnList28 = new ArrayList<Object>();
		List<Object> returnList29 = new ArrayList<Object>();
		List<Object> returnList30 = new ArrayList<Object>();
		List<Object> returnList31 = new ArrayList<Object>();

		List<String> busNumList = new ArrayList<String>();
		List<String> timeList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			boolean withLeave = false;
			String leaveQuery = "select * from  public.nt_t_route_schedule_generator_det02 where generator_ref_no=? and trip_type=?;";

			ps3 = con.prepareStatement(leaveQuery);
			ps3.setString(1, refNO);
			ps3.setString(2, trip);

			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				withLeave = true;
			}

			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);

			for (int i = 1; i < 32; i++) {
				if (withLeave) {
					String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
							+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
							+ "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
							+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
							+ "where b.rs_generator_ref_no=?\r\n" + "and b.rs_trip_type=?\r\n"
							+ "and b.rs_group_no=?\r\n" + "and a.day_no=?\r\n" + "and c.trip_type=?\r\n"
							+ "and a.trip_id\r\n" + "not in \r\n"
							+ "(select distinct c.leave_position from  public.nt_t_route_schedule_generator_det02 c\r\n"
							+ "inner join public.nt_t_route_schedule_generator_det01 x on x.generator_ref_no=?\r\n"
							+ "where c.generator_ref_no=? and c.trip_type=a.trip_type)\r\n" + "order by a.seq;";

					ps = con.prepareStatement(queryBus);
					ps.setString(1, refNO);
					ps.setString(2, trip);
					ps.setString(3, group);
					ps.setInt(4, i);
					ps.setString(5, trip);
					ps.setString(6, refNO);
					ps.setString(7, refNO);

				}

				else {
					/*
					 * String queryBus = " select distinct seq,bus_no,day_no,trip_type \r\n" +
					 * "from public.nt_t_route_schedule_generator_det01 a\r\n" +
					 * "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
					 * + "where b.rs_generator_ref_no=?\r\n" + "and b.rs_trip_type=?\r\n" +
					 * "and b.rs_group_no=?\r\n" + "and a.day_no=? \r\n" + "order by seq;";
					 */

					String queryBus = " select distinct bus_no \r\n"
							+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
							+ "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
							+ "where b.rs_generator_ref_no=?\r\n" + "and b.rs_trip_type=?\r\n"
							+ "and b.rs_group_no=?\r\n" + "and a.day_no=? ;";

					ps = con.prepareStatement(queryBus);
					ps.setString(1, refNO);
					ps.setString(2, trip);
					ps.setString(3, group);
					ps.setInt(4, i);
				}

				rs = ps.executeQuery();
				/** **/
				while (rs.next()) {
					if (i == 1) {

						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {

							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							busNumList.add(value);
							timeList.add(value2);
							returnList.add(value);

						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}

					if (i == 2) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList2.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 3) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList3.add(value);
							busNumList.add(value);
							timeList.add(value2);
							;
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 4) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList4.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 5) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList5.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 6) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList6.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 7) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=?";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList7.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 8) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList8.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 9) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList9.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 10) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList10.add(value);
							busNumList.add(value);
							timeList.add(value2);
							;
						}
					}
					if (i == 11) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList11.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 12) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList12.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 13) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=?";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList13.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
						ConnectionManager.close(rs1);
						ConnectionManager.close(ps1);
					}
					if (i == 14) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList14.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 15) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList15.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 16) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList16.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 17) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList17.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 18) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList18.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 19) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList19.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 20) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList20.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 21) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList21.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 22) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList22.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 23) {
						String query1 = "select  distinct  assigned_bus_no,start_time_slot,trip_type  from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList23.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 24) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type   from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList24.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 25) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList25.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 26) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList26.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 27) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList27.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 28) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList28.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 29) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList29.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 30) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList30.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
					if (i == 31) {
						String query1 = "select  distinct assigned_bus_no,start_time_slot,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
								+ "and  generator_ref_no =? and group_no=? ";
						ps1 = con.prepareStatement(query1);
						ps1.setString(1, rs.getString("bus_no"));
						ps1.setString(2, refNO);
						ps1.setString(3, group);

						rs1 = ps1.executeQuery();
						while (rs1.next()) {
							value = rs1.getString("assigned_bus_no") + rs1.getString("trip_type");
							value2 = rs1.getString("start_time_slot");
							returnList31.add(value);
							busNumList.add(value);
							timeList.add(value2);
						}
					}
				}

			}

			String sql = "INSERT INTO public.nt_m_control_sheet\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql);

			Iterator<String> it1 = timeSlotList.iterator();
			Iterator<Object> it2 = returnList.iterator();
			Iterator<Object> it3 = returnList2.iterator();
			Iterator<Object> it4 = returnList3.iterator();
			Iterator<Object> it5 = returnList4.iterator();
			Iterator<Object> it6 = returnList5.iterator();
			Iterator<Object> it7 = returnList6.iterator();
			for (; it1.hasNext();) {
				stmt.setString(1, (String) it2.next());
				stmt.setString(2, (String) it3.next());
				stmt.setString(3, (String) it4.next());
				stmt.setString(4, (String) it5.next());
				stmt.setString(5, (String) it6.next());
				stmt.setString(6, (String) it7.next());
				stmt.setString(7, it1.next());
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}
			/*** update bus numbers according to timeslots **/

			/** end **/
			String sql2 = "INSERT INTO public.nt_m_control_sheet2\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt2 = con.prepareStatement(sql2);

			Iterator<String> it8 = timeSlotList.iterator();
			Iterator<Object> it9 = returnList7.iterator();
			Iterator<Object> it10 = returnList8.iterator();
			Iterator<Object> it11 = returnList9.iterator();
			Iterator<Object> it12 = returnList10.iterator();
			Iterator<Object> it13 = returnList11.iterator();
			Iterator<Object> it14 = returnList12.iterator();
			for (; it8.hasNext() && it9.hasNext();) {
				stmt2.setString(1, (String) it9.next());
				stmt2.setString(2, (String) it10.next());
				stmt2.setString(3, (String) it11.next());
				stmt2.setString(4, (String) it12.next());
				stmt2.setString(5, (String) it13.next());
				stmt2.setString(6, (String) it14.next());
				stmt2.setString(7, it8.next());
				stmt2.addBatch();
				stmt2.executeUpdate();
				con.commit();
			}

			String sql3 = "INSERT INTO public.nt_m_control_sheet3\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt3 = con.prepareStatement(sql3);

			Iterator<String> it15 = timeSlotList.iterator();
			Iterator<Object> it16 = returnList13.iterator();
			Iterator<Object> it17 = returnList14.iterator();
			Iterator<Object> it18 = returnList15.iterator();
			Iterator<Object> it19 = returnList16.iterator();
			Iterator<Object> it20 = returnList17.iterator();
			Iterator<Object> it21 = returnList18.iterator();
			for (; it15.hasNext() && it16.hasNext();) {
				stmt3.setString(1, (String) it16.next());
				stmt3.setString(2, (String) it17.next());
				stmt3.setString(3, (String) it18.next());
				stmt3.setString(4, (String) it19.next());
				stmt3.setString(5, (String) it20.next());
				stmt3.setString(6, (String) it21.next());
				stmt3.setString(7, it15.next());
				stmt3.addBatch();
				stmt3.executeUpdate();
				con.commit();
			}

			String sql4 = "INSERT INTO public.nt_m_control_sheet4\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt4 = con.prepareStatement(sql4);

			Iterator<String> it22 = timeSlotList.iterator();
			Iterator<Object> it23 = returnList19.iterator();
			Iterator<Object> it24 = returnList20.iterator();
			Iterator<Object> it25 = returnList21.iterator();
			Iterator<Object> it26 = returnList22.iterator();
			Iterator<Object> it27 = returnList23.iterator();
			Iterator<Object> it28 = returnList24.iterator();
			for (; it22.hasNext() && it23.hasNext();) {
				stmt4.setString(1, (String) it23.next());
				stmt4.setString(2, (String) it24.next());
				stmt4.setString(3, (String) it25.next());
				stmt4.setString(4, (String) it26.next());
				stmt4.setString(5, (String) it27.next());
				stmt4.setString(6, (String) it28.next());
				stmt4.setString(7, it22.next());
				stmt4.addBatch();
				stmt4.executeUpdate();
				con.commit();
			}

			String sql5 = "INSERT INTO public.nt_m_control_sheet5\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot, col7)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?,?);;";

			stmt5 = con.prepareStatement(sql5);

			Iterator<String> it29 = timeSlotList.iterator();
			Iterator<Object> it30 = returnList25.iterator();
			Iterator<Object> it31 = returnList26.iterator();
			Iterator<Object> it32 = returnList27.iterator();
			Iterator<Object> it33 = returnList28.iterator();
			Iterator<Object> it34 = returnList29.iterator();
			Iterator<Object> it35 = returnList30.iterator();
			Iterator<Object> it36 = returnList31.iterator();

			for (; it29.hasNext();) {
				if (returnList29.isEmpty() && returnList30.isEmpty() && returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, null);
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());
				}

				else if (returnList30.isEmpty() && returnList31.isEmpty()) {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else if (returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, (String) it36.next());
					stmt5.setString(8, it29.next());
				}

				stmt5.addBatch();
				stmt5.executeUpdate();
				con.commit();
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs3);
			ConnectionManager.close(con);
		}

		return map;

	}

	@Override
	public Map<String, List> getBusNOList2(String refNO, String trip, String group, List<String> timeSlotList,
			List<String> busOrderListForOrigin, List<String> busOrderListForDestination, String originBus,
			String destinationBus, int dayOne, int dayTwo, boolean isHistory) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement ps3 = null;

		ResultSet rs3 = null;

		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String, List> map = new HashMap();

		String value = "";

		List<String> returnList = new ArrayList<String>();
		List<String> returnList2 = new ArrayList<String>();
		List<String> returnList3 = new ArrayList<String>();
		List<String> returnList4 = new ArrayList<String>();
		List<String> returnList5 = new ArrayList<String>();
		List<String> returnList6 = new ArrayList<String>();
		List<String> returnList7 = new ArrayList<String>();
		List<String> returnList8 = new ArrayList<String>();
		List<String> returnList9 = new ArrayList<String>();
		List<String> returnList10 = new ArrayList<String>();
		List<String> returnList11 = new ArrayList<String>();
		List<String> returnList12 = new ArrayList<String>();
		List<String> returnList13 = new ArrayList<String>();
		List<String> returnList14 = new ArrayList<String>();
		List<String> returnList15 = new ArrayList<String>();
		List<String> returnList16 = new ArrayList<String>();
		List<String> returnList17 = new ArrayList<String>();
		List<String> returnList18 = new ArrayList<String>();
		List<String> returnList19 = new ArrayList<String>();
		List<String> returnList20 = new ArrayList<String>();
		List<String> returnList21 = new ArrayList<String>();
		List<String> returnList22 = new ArrayList<String>();
		List<String> returnList23 = new ArrayList<String>();
		List<String> returnList24 = new ArrayList<String>();
		List<String> returnList25 = new ArrayList<String>();
		List<String> returnList26 = new ArrayList<String>();
		List<String> returnList27 = new ArrayList<String>();
		List<String> returnList28 = new ArrayList<String>();
		List<String> returnList29 = new ArrayList<String>();
		List<String> returnList30 = new ArrayList<String>();
		List<String> returnList31 = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			boolean withLeave = false;
			String leaveQuery = "select * from  public.nt_t_route_schedule_generator_det02 where generator_ref_no=? and trip_type=?;";

			ps3 = con.prepareStatement(leaveQuery);
			ps3.setString(1, refNO);
			ps3.setString(2, trip);

			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				withLeave = true;
			}

			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);

			for (int i = dayOne + 1; i < dayTwo + 1; i++) {
				List<String> rotateBusListForOrgin = new ArrayList<>();
				List<String> rotateBusListForDestination = new ArrayList<>();
				/*
				 * if (withLeave) { String queryBus =
				 * "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n" +
				 * "from public.nt_t_route_schedule_generator_det01 a\r\n" +
				 * "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
				 * +
				 * "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
				 * + "where b.rs_generator_ref_no=?\r\n" + "and b.rs_trip_type=?\r\n" +
				 * "and b.rs_group_no=?\r\n" + "and a.day_no=?\r\n" + "and c.trip_type=?\r\n" +
				 * "and a.trip_id\r\n" + "not in \r\n" +
				 * "(select distinct c.leave_position from  public.nt_t_route_schedule_generator_det02 c\r\n"
				 * +
				 * "inner join public.nt_t_route_schedule_generator_det01 x on x.generator_ref_no=?\r\n"
				 * + "where c.generator_ref_no=? and c.trip_type=a.trip_type)\r\n" +
				 * "order by a.seq;";
				 * 
				 * ps = con.prepareStatement(queryBus); ps.setString(1, refNO); ps.setString(2,
				 * trip); ps.setString(3, group); ps.setInt(4, i); ps.setString(5, trip);
				 * ps.setString(6, refNO); ps.setString(7, refNO);
				 * 
				 * }
				 * 
				 * else {
				 */
				if (isHistory) {
					// and b.rs_trip_type=?
					String queryBus = "select  bus_no from public.nt_h_route_schedule_generator_det01  a\r\n"
							+ "inner join public.nt_h_route_schedule_generator b \r\n"
							+ "on b.rs_generator_ref_no=a.generator_ref_no and b.rs_seq =a.mas_seq where b.rs_generator_ref_no=?\r\n"
							+ "and b.rs_group_no=? and a.day_no=? ;";

					ps = con.prepareStatement(queryBus);
					ps.setString(1, refNO);
					// ps.setString(2, trip);
					ps.setString(2, group);
					ps.setInt(3, i);
				} else {
					String queryBus = "select  bus_no \r\n" + "from public.nt_t_route_schedule_generator_det01 a\r\n"
							+ "inner join public.nt_m_route_schedule_generator b \r\n"
							+ "on b.rs_generator_ref_no=a.generator_ref_no\r\n" + "where b.rs_generator_ref_no=?\r\n"
							+ "and b.rs_trip_type=?\r\n" + "and b.rs_group_no=? and a.day_no=? ;";

					ps = con.prepareStatement(queryBus);
					ps.setString(1, refNO);
					ps.setString(2, trip);
					ps.setString(3, group);
					ps.setInt(4, i);

				}

				rs = ps.executeQuery();

				while (rs.next()) {
					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
						rotateBusListForOrgin.add(rs.getString("bus_no"));
					}
					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(destinationBus)) {
						rotateBusListForDestination.add(rs.getString("bus_no"));
					}
				}

				List<String> orderdPerDayBusList = new ArrayList<>();
				List<String> list = new ArrayList<>();
				if (trip.equals("O")) {
					list = busOrderListForOrigin;
				} else if (trip.equals("D")) {
					list = busOrderListForDestination;
				}

				int oi = 0;

				int di = 0;

				for (String bo : list) {

					if (bo.equals("O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						for (int k = oi; k <= rotateBusListForOrgin.size(); k++) {

							orderdPerDayBusList.add(rotateBusListForOrgin.get(oi));
							oi++;

							break;

						}

					}

					else if (bo.equals("SLTB-O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("SLTB-O");
					}

					else if (bo.equals("SLTB-D") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("SLTB-D");
					}

					else if (bo.equals("ETC-O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("ETC-O");
					}

					else if (bo.equals("ETC-D") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("ETC-D");
					}

					/** for fixed pvt bus **/

					else if (bo.contains("O-Y") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {
						orderdPerDayBusList.add(bo);

					}

					else if (bo.contains("D-Y") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {
						orderdPerDayBusList.add(bo);

					}
					/** for fixed pvt bus end **/

					else if (bo.equals("D") && rotateBusListForDestination != null
							&& !rotateBusListForDestination.isEmpty()) {

						for (int k = di; k <= rotateBusListForDestination.size(); k++) {

							orderdPerDayBusList.add(rotateBusListForDestination.get(di));
							di++;

							break;

						}

					}

				}

				for (String op : orderdPerDayBusList) {
					String abbr = null;

					if (dayOne == 30) {
						if (i == 31) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {

								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}

						if (i == 32) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList2.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 33) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList3.add(value);

									;
								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 34) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList4.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 35) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList5.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 36) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList6.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 37) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList7.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 38) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList8.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 39) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList9.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 40) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList10.add(value);

								}
							}
						}
						if (i == 41) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList11.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 42) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList12.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 43) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList13.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 44) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList14.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 45) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList15.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 46) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList16.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 47) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList17.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 48) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList18.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 49) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList19.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 50) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList20.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 51) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList21.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 52) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList22.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 53) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList23.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 54) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList24.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 55) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList25.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 56) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList26.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 57) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList27.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 58) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList28.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 59) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList29.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 60) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList30.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 61) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList31.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
					} else if (dayOne == 31) {
						if (i == 32) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}

						if (i == 33) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList2.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 34) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList3.add(value);

									;
								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 35) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList4.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 36) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList5.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 37) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList6.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 38) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList7.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 39) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList8.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 40) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList9.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 41) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList10.add(value);

								}
							}
						}
						if (i == 42) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList11.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 43) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList12.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 44) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList13.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 45) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList14.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 46) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList15.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 47) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList16.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 48) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList17.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 49) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList18.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 50) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList19.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 51) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList20.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 52) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList21.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 53) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList22.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 54) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList23.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 55) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList24.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 56) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList25.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 57) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList26.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 58) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList27.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 59) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList28.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 60) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList29.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 61) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList30.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 62) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList31.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}

					}

				}
			}
			String sql = "INSERT INTO public.nt_m_control_sheet\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql);

			Iterator<String> it1 = timeSlotList.iterator();
			Iterator<String> it2 = returnList.iterator();
			Iterator<String> it3 = returnList2.iterator();
			Iterator<String> it4 = returnList3.iterator();
			Iterator<String> it5 = returnList4.iterator();
			Iterator<String> it6 = returnList5.iterator();
			Iterator<String> it7 = returnList6.iterator();
			for (; it1.hasNext();) {
				stmt.setString(1, (String) it2.next());
				stmt.setString(2, (String) it3.next());
				stmt.setString(3, (String) it4.next());
				stmt.setString(4, (String) it5.next());
				stmt.setString(5, (String) it6.next());
				stmt.setString(6, (String) it7.next());
				stmt.setString(7, it1.next());
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}
			/*** update bus numbers according to timeslots **/

			/** end **/
			String sql2 = "INSERT INTO public.nt_m_control_sheet2\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt2 = con.prepareStatement(sql2);

			Iterator<String> it8 = timeSlotList.iterator();
			Iterator<String> it9 = returnList7.iterator();
			Iterator<String> it10 = returnList8.iterator();
			Iterator<String> it11 = returnList9.iterator();
			Iterator<String> it12 = returnList10.iterator();
			Iterator<String> it13 = returnList11.iterator();
			Iterator<String> it14 = returnList12.iterator();
			for (; it8.hasNext() && it9.hasNext();) {
				stmt2.setString(1, (String) it9.next());
				stmt2.setString(2, (String) it10.next());
				stmt2.setString(3, (String) it11.next());
				stmt2.setString(4, (String) it12.next());
				stmt2.setString(5, (String) it13.next());
				stmt2.setString(6, (String) it14.next());
				stmt2.setString(7, it8.next());
				stmt2.addBatch();
				stmt2.executeUpdate();
				con.commit();
			}

			String sql3 = "INSERT INTO public.nt_m_control_sheet3\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt3 = con.prepareStatement(sql3);

			Iterator<String> it15 = timeSlotList.iterator();
			Iterator<String> it16 = returnList13.iterator();
			Iterator<String> it17 = returnList14.iterator();
			Iterator<String> it18 = returnList15.iterator();
			Iterator<String> it19 = returnList16.iterator();
			Iterator<String> it20 = returnList17.iterator();
			Iterator<String> it21 = returnList18.iterator();
			for (; it15.hasNext() && it16.hasNext();) {
				stmt3.setString(1, (String) it16.next());
				stmt3.setString(2, (String) it17.next());
				stmt3.setString(3, (String) it18.next());
				stmt3.setString(4, (String) it19.next());
				stmt3.setString(5, (String) it20.next());
				stmt3.setString(6, (String) it21.next());
				stmt3.setString(7, it15.next());
				stmt3.addBatch();
				stmt3.executeUpdate();
				con.commit();
			}

			String sql4 = "INSERT INTO public.nt_m_control_sheet4\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt4 = con.prepareStatement(sql4);

			Iterator<String> it22 = timeSlotList.iterator();
			Iterator<String> it23 = returnList19.iterator();
			Iterator<String> it24 = returnList20.iterator();
			Iterator<String> it25 = returnList21.iterator();
			Iterator<String> it26 = returnList22.iterator();
			Iterator<String> it27 = returnList23.iterator();
			Iterator<String> it28 = returnList24.iterator();
			for (; it22.hasNext() && it23.hasNext();) {
				stmt4.setString(1, (String) it23.next());
				stmt4.setString(2, (String) it24.next());
				stmt4.setString(3, (String) it25.next());
				stmt4.setString(4, (String) it26.next());
				stmt4.setString(5, (String) it27.next());
				stmt4.setString(6, (String) it28.next());
				stmt4.setString(7, it22.next());
				stmt4.addBatch();
				stmt4.executeUpdate();
				con.commit();
			}

			String sql5 = "INSERT INTO public.nt_m_control_sheet5\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot, col7)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?,?);;";

			stmt5 = con.prepareStatement(sql5);

			Iterator<String> it29 = timeSlotList.iterator();
			Iterator<String> it30 = returnList25.iterator();
			Iterator<String> it31 = returnList26.iterator();
			Iterator<String> it32 = returnList27.iterator();
			Iterator<String> it33 = returnList28.iterator();
			Iterator<String> it34 = returnList29.iterator();
			Iterator<String> it35 = returnList30.iterator();
			Iterator<String> it36 = returnList31.iterator();

			for (; it29.hasNext();) {
				if (returnList29.isEmpty() && returnList30.isEmpty() && returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, null);
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());
				}

				else if (returnList30.isEmpty() && returnList31.isEmpty()) {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else if (returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, (String) it36.next());
					stmt5.setString(8, it29.next());
				}

				stmt5.addBatch();
				stmt5.executeUpdate();
				con.commit();
			}

			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);

		} catch (

		Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs3);
			ConnectionManager.close(con);
		}

		return map;

	}

	@Override
	public Map<String, List> getBusNOList3(String refNO, String trip, String group, List<String> timeSlotList,
			List<String> busOrderListForOrigin, List<String> busOrderListForDestination, String originBus,
			String destinationBus, int dayTwo, int dayThree, boolean isHistory) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement ps3 = null;

		ResultSet rs3 = null;

		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String, List> map = new HashMap();

		String value = "";

		List<String> returnList = new ArrayList<String>();
		List<String> returnList2 = new ArrayList<String>();
		List<String> returnList3 = new ArrayList<String>();
		List<String> returnList4 = new ArrayList<String>();
		List<String> returnList5 = new ArrayList<String>();
		List<String> returnList6 = new ArrayList<String>();
		List<String> returnList7 = new ArrayList<String>();
		List<String> returnList8 = new ArrayList<String>();
		List<String> returnList9 = new ArrayList<String>();
		List<String> returnList10 = new ArrayList<String>();
		List<String> returnList11 = new ArrayList<String>();
		List<String> returnList12 = new ArrayList<String>();
		List<String> returnList13 = new ArrayList<String>();
		List<String> returnList14 = new ArrayList<String>();
		List<String> returnList15 = new ArrayList<String>();
		List<String> returnList16 = new ArrayList<String>();
		List<String> returnList17 = new ArrayList<String>();
		List<String> returnList18 = new ArrayList<String>();
		List<String> returnList19 = new ArrayList<String>();
		List<String> returnList20 = new ArrayList<String>();
		List<String> returnList21 = new ArrayList<String>();
		List<String> returnList22 = new ArrayList<String>();
		List<String> returnList23 = new ArrayList<String>();
		List<String> returnList24 = new ArrayList<String>();
		List<String> returnList25 = new ArrayList<String>();
		List<String> returnList26 = new ArrayList<String>();
		List<String> returnList27 = new ArrayList<String>();
		List<String> returnList28 = new ArrayList<String>();
		List<String> returnList29 = new ArrayList<String>();
		List<String> returnList30 = new ArrayList<String>();
		List<String> returnList31 = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			boolean withLeave = false;
			String leaveQuery = "select * from  public.nt_t_route_schedule_generator_det02 where generator_ref_no=? and trip_type=?;";

			ps3 = con.prepareStatement(leaveQuery);
			ps3.setString(1, refNO);
			ps3.setString(2, trip);

			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				withLeave = true;
			}

			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);

			for (int i = dayTwo + 1; i < dayThree + 1; i++) {
				List<String> rotateBusListForOrgin = new ArrayList<>();
				List<String> rotateBusListForDestination = new ArrayList<>();

				if (isHistory) {
					// and b.rs_trip_type=?
					String queryBus = "select  bus_no from public.nt_h_route_schedule_generator_det01  a\r\n"
							+ "inner join public.nt_h_route_schedule_generator b \r\n"
							+ "on b.rs_generator_ref_no=a.generator_ref_no and b.rs_seq =a.mas_seq where b.rs_generator_ref_no=?\r\n"
							+ "and b.rs_group_no=? and a.day_no=? ;";

					ps = con.prepareStatement(queryBus);
					ps.setString(1, refNO);
					// ps.setString(2, trip);
					ps.setString(2, group);
					ps.setInt(3, i);
				} else {
					String queryBus = "select  bus_no \r\n" + "from public.nt_t_route_schedule_generator_det01 a\r\n"
							+ "inner join public.nt_m_route_schedule_generator b \r\n"
							+ "on b.rs_generator_ref_no=a.generator_ref_no\r\n" + "where b.rs_generator_ref_no=?\r\n"
							+ "and b.rs_trip_type=?\r\n" + "and b.rs_group_no=? and a.day_no=? ;";

					ps = con.prepareStatement(queryBus);
					ps.setString(1, refNO);
					ps.setString(2, trip);
					ps.setString(3, group);
					ps.setInt(4, i);

				}

				rs = ps.executeQuery();

				while (rs.next()) {
					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
						rotateBusListForOrgin.add(rs.getString("bus_no"));
					}
					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(destinationBus)) {
						rotateBusListForDestination.add(rs.getString("bus_no"));
					}
				}

				List<String> orderdPerDayBusList = new ArrayList<>();
				List<String> list = new ArrayList<>();
				if (trip.equals("O")) {
					list = busOrderListForOrigin;
				} else if (trip.equals("D")) {
					list = busOrderListForDestination;
				}

				int oi = 0;

				int di = 0;

				for (String bo : list) {

					if (bo.equals("O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						for (int k = oi; k <= rotateBusListForOrgin.size(); k++) {

							orderdPerDayBusList.add(rotateBusListForOrgin.get(oi));
							oi++;

							break;

						}

					}

					else if (bo.equals("SLTB-O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("SLTB-O");
					}

					else if (bo.equals("SLTB-D") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("SLTB-D");
					}

					else if (bo.equals("ETC-O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("ETC-O");
					}

					else if (bo.equals("ETC-D") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("ETC-D");
					}

					/** for fixed pvt bus **/

					else if (bo.contains("O-Y") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {
						orderdPerDayBusList.add(bo);

					}

					else if (bo.contains("D-Y") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {
						orderdPerDayBusList.add(bo);

					}
					/** for fixed pvt bus end **/

					else if (bo.equals("D") && rotateBusListForDestination != null
							&& !rotateBusListForDestination.isEmpty()) {

						for (int k = di; k <= rotateBusListForDestination.size(); k++) {

							orderdPerDayBusList.add(rotateBusListForDestination.get(di));
							di++;

							break;

						}

					}

				}

				for (String op : orderdPerDayBusList) {
					String abbr = null;
					if (dayTwo == 61) {
						if (i == 62) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {

								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}

						if (i == 63) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList2.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 64) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList3.add(value);

									;
								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 65) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList4.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 66) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList5.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 67) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList6.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 68) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList7.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 69) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList8.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 70) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList9.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 71) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList10.add(value);

								}
							}
						}
						if (i == 72) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList11.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 73) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList12.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 74) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList13.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 75) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList14.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 76) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList15.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 77) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList16.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 78) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList17.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 79) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList18.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 80) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList19.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 81) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList20.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 82) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList21.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 83) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList22.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 84) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList23.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 85) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList24.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 86) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList25.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 87) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList26.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 88) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList27.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 89) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList28.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 90) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList29.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 91) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList30.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 92) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList31.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}

					} else if (dayTwo == 62) {
						if (i == 63) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {

								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 64) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList2.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 65) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList3.add(value);

									;
								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 66) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList4.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 67) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList5.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 68) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList6.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 69) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList7.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 70) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList8.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 71) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList9.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 72) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList10.add(value);

								}
							}
						}
						if (i == 73) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList11.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 74) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList12.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 75) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList13.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 76) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList14.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 77) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList15.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 78) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList16.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 79) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList17.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 80) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList18.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 81) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList19.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 82) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList20.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 83) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList21.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 84) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList22.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 85) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList23.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 86) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList24.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 87) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList25.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 88) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList26.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 89) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList27.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 90) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList28.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 91) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList29.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 92) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList30.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 93) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no,trip_type from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? and trip_type =? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList31.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
					}
				}
			}
			String sql = "INSERT INTO public.nt_m_control_sheet\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql);

			Iterator<String> it1 = timeSlotList.iterator();
			Iterator<String> it2 = returnList.iterator();
			Iterator<String> it3 = returnList2.iterator();
			Iterator<String> it4 = returnList3.iterator();
			Iterator<String> it5 = returnList4.iterator();
			Iterator<String> it6 = returnList5.iterator();
			Iterator<String> it7 = returnList6.iterator();
			for (; it1.hasNext();) {
				stmt.setString(1, (String) it2.next());
				stmt.setString(2, (String) it3.next());
				stmt.setString(3, (String) it4.next());
				stmt.setString(4, (String) it5.next());
				stmt.setString(5, (String) it6.next());
				stmt.setString(6, (String) it7.next());
				stmt.setString(7, it1.next());
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}
			/*** update bus numbers according to timeslots **/

			/** end **/
			String sql2 = "INSERT INTO public.nt_m_control_sheet2\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt2 = con.prepareStatement(sql2);

			Iterator<String> it8 = timeSlotList.iterator();
			Iterator<String> it9 = returnList7.iterator();
			Iterator<String> it10 = returnList8.iterator();
			Iterator<String> it11 = returnList9.iterator();
			Iterator<String> it12 = returnList10.iterator();
			Iterator<String> it13 = returnList11.iterator();
			Iterator<String> it14 = returnList12.iterator();
			for (; it8.hasNext() && it9.hasNext();) {
				stmt2.setString(1, (String) it9.next());
				stmt2.setString(2, (String) it10.next());
				stmt2.setString(3, (String) it11.next());
				stmt2.setString(4, (String) it12.next());
				stmt2.setString(5, (String) it13.next());
				stmt2.setString(6, (String) it14.next());
				stmt2.setString(7, it8.next());
				stmt2.addBatch();
				stmt2.executeUpdate();
				con.commit();
			}

			String sql3 = "INSERT INTO public.nt_m_control_sheet3\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt3 = con.prepareStatement(sql3);

			Iterator<String> it15 = timeSlotList.iterator();
			Iterator<String> it16 = returnList13.iterator();
			Iterator<String> it17 = returnList14.iterator();
			Iterator<String> it18 = returnList15.iterator();
			Iterator<String> it19 = returnList16.iterator();
			Iterator<String> it20 = returnList17.iterator();
			Iterator<String> it21 = returnList18.iterator();
			for (; it15.hasNext() && it16.hasNext();) {
				stmt3.setString(1, (String) it16.next());
				stmt3.setString(2, (String) it17.next());
				stmt3.setString(3, (String) it18.next());
				stmt3.setString(4, (String) it19.next());
				stmt3.setString(5, (String) it20.next());
				stmt3.setString(6, (String) it21.next());
				stmt3.setString(7, it15.next());
				stmt3.addBatch();
				stmt3.executeUpdate();
				con.commit();
			}

			String sql4 = "INSERT INTO public.nt_m_control_sheet4\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt4 = con.prepareStatement(sql4);

			Iterator<String> it22 = timeSlotList.iterator();
			Iterator<String> it23 = returnList19.iterator();
			Iterator<String> it24 = returnList20.iterator();
			Iterator<String> it25 = returnList21.iterator();
			Iterator<String> it26 = returnList22.iterator();
			Iterator<String> it27 = returnList23.iterator();
			Iterator<String> it28 = returnList24.iterator();
			for (; it22.hasNext() && it23.hasNext();) {
				stmt4.setString(1, (String) it23.next());
				stmt4.setString(2, (String) it24.next());
				stmt4.setString(3, (String) it25.next());
				stmt4.setString(4, (String) it26.next());
				stmt4.setString(5, (String) it27.next());
				stmt4.setString(6, (String) it28.next());
				stmt4.setString(7, it22.next());
				stmt4.addBatch();
				stmt4.executeUpdate();
				con.commit();
			}

			String sql5 = "INSERT INTO public.nt_m_control_sheet5\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot, col7)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?,?);;";

			stmt5 = con.prepareStatement(sql5);

			Iterator<String> it29 = timeSlotList.iterator();
			Iterator<String> it30 = returnList25.iterator();
			Iterator<String> it31 = returnList26.iterator();
			Iterator<String> it32 = returnList27.iterator();
			Iterator<String> it33 = returnList28.iterator();
			Iterator<String> it34 = returnList29.iterator();
			Iterator<String> it35 = returnList30.iterator();
			Iterator<String> it36 = returnList31.iterator();

			for (; it29.hasNext();) {
				if (returnList29.isEmpty() && returnList30.isEmpty() && returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, null);
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());
				}

				else if (returnList30.isEmpty() && returnList31.isEmpty()) {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else if (returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, (String) it36.next());
					stmt5.setString(8, it29.next());
				}

				stmt5.addBatch();
				stmt5.executeUpdate();
				con.commit();
			}

			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);

		} catch (

		Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs3);
			ConnectionManager.close(con);
		}

		return map;

	}

	public Map<String, List> insertLeavesInTable(String refNO, String trip, String groupNo, String originBus,
			String destinationBus, int dayForMonth, String selectedBusAbbriviation) {
		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt5 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String leave_query1 = null;
		List<String> leaveBusList1 = new ArrayList<>();
		List<String> leaveBusList2 = new ArrayList<>();
		List<String> leaveBusList3 = new ArrayList<>();
		List<String> leaveBusList4 = new ArrayList<>();
		List<String> leaveBusList5 = new ArrayList<>();
		List<String> leaveBusList6 = new ArrayList<>();
		List<String> leaveBusList7 = new ArrayList<>();
		List<String> leaveBusList8 = new ArrayList<>();
		List<String> leaveBusList9 = new ArrayList<>();
		List<String> leaveBusList10 = new ArrayList<>();
		List<String> leaveBusList11 = new ArrayList<>();
		List<String> leaveBusList12 = new ArrayList<>();
		List<String> leaveBusList13 = new ArrayList<>();
		List<String> leaveBusList14 = new ArrayList<>();
		List<String> leaveBusList15 = new ArrayList<>();
		List<String> leaveBusList16 = new ArrayList<>();
		List<String> leaveBusList17 = new ArrayList<>();
		List<String> leaveBusList18 = new ArrayList<>();
		List<String> leaveBusList19 = new ArrayList<>();
		List<String> leaveBusList20 = new ArrayList<>();
		List<String> leaveBusList21 = new ArrayList<>();
		List<String> leaveBusList22 = new ArrayList<>();
		List<String> leaveBusList23 = new ArrayList<>();
		List<String> leaveBusList24 = new ArrayList<>();
		List<String> leaveBusList25 = new ArrayList<>();
		List<String> leaveBusList26 = new ArrayList<>();
		List<String> leaveBusList27 = new ArrayList<>();
		List<String> leaveBusList28 = new ArrayList<>();
		List<String> leaveBusList29 = new ArrayList<>();
		List<String> leaveBusList30 = new ArrayList<>();
		List<String> leaveBusList31 = new ArrayList<>();

		Map<String, List> map = new HashMap();
		String tripType = null;
		/** leave bus abbreviation selection **/
		try {
			con = ConnectionManager.getConnection();
//			if (selectedBusAbbriviation != null) {
//				selectedBusAbbriviation = selectedBusAbbriviation + '%';
//
//				leave_query1 = "select a.bus_no,a.day_no,b.leave_position,a.trip_id\r\n"
//						+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
//						+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
//						+ "where a.generator_ref_no=? and bus_no like " + "'" + selectedBusAbbriviation + "' "
//						+ "and b.leave_position=a.trip_id  group by a.day_no, a.bus_no,b.leave_position,a.trip_id, a.trip_type order by  a.day_no,b.leave_position,a.trip_type ";
//
//			} else {
//				leave_query1 = "select a.bus_no,a.day_no,b.leave_position,a.trip_id\r\n"
//						+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
//						+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
//						+ "where a.generator_ref_no=? \r\n"
//						+ "and b.leave_position=a.trip_id  group by a.day_no, a.bus_no,b.leave_position,a.trip_id, a.trip_type order by  a.day_no,b.leave_position,a.trip_type ";
//			}
			
			if (trip.equals("O")) {
				
				leave_query1 = "SELECT  distinct on(leave_trip_day) leave_bus, leave_trip_id, leave_trip_day "
						+ "FROM public.nt_t_route_schedule_generator_det01 "
						+ "WHERE generator_ref_no = ? and trip_type = 'O' AND leave_bus IS NOT null "
						+ "AND leave_trip_id IS NOT null AND leave_trip_day IS NOT null order by leave_trip_day";
				
				tripType = "O";

			} else {
				leave_query1 = "SELECT  distinct on(leave_trip_day) leave_bus, leave_trip_id, leave_trip_day "
						+ "FROM public.nt_t_route_schedule_generator_det01 "
						+ "WHERE generator_ref_no = ? and trip_type = 'D' AND leave_bus IS NOT null "
						+ "AND leave_trip_id IS NOT null AND leave_trip_day IS NOT null order by leave_trip_day";
				
				tripType = "D";

			}
			ps1 = con.prepareStatement(leave_query1);

			ps1.setString(1, refNO);
			// ps1.setString(2, trip);

			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				String abbr = null;
				if (rs1.getString("leave_trip_day").equals("1")) {

					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList1.add(bus + tripType);
					}
					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("2")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList2.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("3")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList3.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("4")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList4.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("5")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList5.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("6")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList6.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("7")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList7.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("8")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList8.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("9")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList9.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("10")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList10.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("11")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList11.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("12")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList12.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("13")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList13.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("14")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList14.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("15")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList15.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("16")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList16.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("17")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList17.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("18")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList18.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("19")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList19.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("20")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList20.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("21")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList21.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("22")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList22.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("23")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList23.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("24")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList24.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("25")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList25.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("26")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList26.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("27")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList27.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("28")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList28.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("29")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList29.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}
				if (rs1.getString("leave_trip_day").equals("30")) {
					
					String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
							+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

					ps2 = con.prepareStatement(leave_query2);

					ps2.setString(1, refNO);
					ps2.setString(2, rs1.getString("leave_trip_day"));

					rs2 = ps2.executeQuery();
					while (rs2.next()) {
						String bus = rs2.getString("leave_bus");
						leaveBusList30.add(bus + tripType);
					}

					ConnectionManager.close(rs2);
					ConnectionManager.close(ps2);

				}

				if (dayForMonth == 31) {

					if (rs1.getString("leave_trip_day").equals("30")) {
						
						String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
								+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, rs1.getString("leave_trip_day"));

						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							String bus = rs2.getString("leave_bus");
							leaveBusList30.add(bus + tripType);
						}

						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);

					}
					if (rs1.getString("leave_trip_day").equals("31")) {
						
						String leave_query2 = "select distinct leave_bus from public.nt_t_route_schedule_generator_det01 "
								+ "where generator_ref_no=? and leave_trip_day = ? and leave_bus IS NOT null";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, rs1.getString("leave_trip_day"));

						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							String bus = rs2.getString("leave_bus");
							leaveBusList31.add(bus + tripType);
						}

						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);

					}
				}

			}
			/** end leave bus abbreviation selection **/
			String sql5 = "INSERT INTO public.nt_m_control_sheet_leavebus\r\n"
					+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, "
					+ "col17, col18, col19, col20, col21, col22, col23, col24, col25, "
					+ "col26, col27, col28, col29, col30, col31)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?);";

			stmt5 = con.prepareStatement(sql5);

			Iterator<String> it1 = leaveBusList1.iterator();
			Iterator<String> it2 = leaveBusList2.iterator();
			Iterator<String> it3 = leaveBusList3.iterator();
			Iterator<String> it4 = leaveBusList4.iterator();
			Iterator<String> it5 = leaveBusList5.iterator();
			Iterator<String> it6 = leaveBusList6.iterator();
			Iterator<String> it7 = leaveBusList7.iterator();
			Iterator<String> it8 = leaveBusList8.iterator();
			Iterator<String> it9 = leaveBusList9.iterator();
			Iterator<String> it10 = leaveBusList10.iterator();

			Iterator<String> it11 = leaveBusList11.iterator();
			Iterator<String> it12 = leaveBusList12.iterator();
			Iterator<String> it13 = leaveBusList13.iterator();
			Iterator<String> it14 = leaveBusList14.iterator();
			Iterator<String> it15 = leaveBusList15.iterator();
			Iterator<String> it16 = leaveBusList16.iterator();
			Iterator<String> it17 = leaveBusList17.iterator();
			Iterator<String> it18 = leaveBusList18.iterator();
			Iterator<String> it19 = leaveBusList19.iterator();
			Iterator<String> it20 = leaveBusList20.iterator();

			Iterator<String> it21 = leaveBusList21.iterator();
			Iterator<String> it22 = leaveBusList22.iterator();
			Iterator<String> it23 = leaveBusList23.iterator();
			Iterator<String> it24 = leaveBusList24.iterator();
			Iterator<String> it25 = leaveBusList25.iterator();
			Iterator<String> it26 = leaveBusList26.iterator();
			Iterator<String> it27 = leaveBusList27.iterator();
			Iterator<String> it28 = leaveBusList28.iterator();
			Iterator<String> it29 = leaveBusList29.iterator();
			Iterator<String> it30 = leaveBusList30.iterator();
			Iterator<String> it31 = leaveBusList31.iterator();

			for (; it1.hasNext();) {
				if (leaveBusList29.isEmpty() && leaveBusList29.isEmpty() && leaveBusList29.isEmpty()) {
					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, null);
					stmt5.setString(30, null);
					stmt5.setString(31, null);

				}

				else if (leaveBusList30.isEmpty() && leaveBusList31.isEmpty()) {

					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, it29.next());
					stmt5.setString(30, null);
					stmt5.setString(31, null);

				}

				else if (leaveBusList31.isEmpty()) {

					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, it29.next());
					stmt5.setString(30, it30.next());
					stmt5.setString(31, null);

				}

				else {

					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, it29.next());
					stmt5.setString(30, it30.next());
					stmt5.setString(31, it31.next());

				}

				stmt5.addBatch();
				stmt5.executeUpdate();
				con.commit();
			}

			/** start insert leave bus **/

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			ConnectionManager.close(ps1);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(rs1);
			ConnectionManager.close(con);
		}
		return map;

	}


	@Override
	public void removeTempTable() {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "DELETE FROM  public.nt_m_control_sheet;";

			ps = con.prepareStatement(query);

			ps.executeUpdate();

			ConnectionManager.close(ps);

			String query2 = "DELETE FROM  public.nt_m_control_sheet2;";

			ps2 = con.prepareStatement(query2);

			ps2.executeUpdate();

			ConnectionManager.close(ps2);

			String query3 = "DELETE FROM  public.nt_m_control_sheet3;";

			ps3 = con.prepareStatement(query3);

			ps3.executeUpdate();

			ConnectionManager.close(ps3);

			String query4 = "DELETE FROM  public.nt_m_control_sheet4;";

			ps4 = con.prepareStatement(query4);

			ps4.executeUpdate();

			ConnectionManager.close(ps4);

			String query5 = "DELETE FROM  public.nt_m_control_sheet5;";

			ps5 = con.prepareStatement(query5);

			ps5.executeUpdate();

			ConnectionManager.close(ps5);

			String query6 = "DELETE FROM  nt_m_control_sheet_leavebus;";

			ps6 = con.prepareStatement(query6);

			ps6.executeUpdate();

			ConnectionManager.close(ps6);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(ps5);
			ConnectionManager.close(ps5);
			ConnectionManager.close(ps6);
			ConnectionManager.close(con);
		}

	}

	@Override
	public String getDistanceForRoute(String routeNo, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int distance = 0;

		try {
			con = ConnectionManager.getConnection();

			String query = "select rc_length,rc_travel_time,rc_bus_speed\r\n"
					+ "from public.nt_t_route_creator where rc_route_no=? and rc_bus_type=?";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				distance = rs.getInt("rc_length");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return Integer.toString(distance);
	}

	@Override
	public String getTravelTimeForROute(String routeNo, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String time = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select rc_length,rc_travel_time,rc_bus_speed\r\n"
					+ "from public.nt_t_route_creator where rc_route_no=? and rc_bus_type=?";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				time = rs.getString("rc_travel_time");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return time;
	}

	@Override
	public String getSpeedForROute(String routeNo, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String speed = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select rc_length,rc_travel_time,rc_bus_speed\r\n"
					+ "from public.nt_t_route_creator where rc_route_no=? and rc_bus_type=?";
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				speed = rs.getString("rc_bus_speed");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return speed;
	}

	@Override
	public List<String> serviceTypeDropDownList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String des = null;
		List<String> returnList = new ArrayList<String>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select description,code from nt_r_service_types where active='A'\r\n"
					+ "order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				des = (rs.getString("description"));

				returnList.add(des);

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
	public TimeTableDTO getTimetableDet(String routeNo, String busCatCode, String tripType, String groupNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		TimeTableDTO timeTableDTO = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rs_generator_ref_no,rs_start_date,rs_end_date "
					+ "FROM public.nt_m_route_schedule_generator " + "WHERE rs_route_no=? "
					+ "AND rs_bus_category_code=? " + "AND rs_trip_type=? " + "AND rs_group_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, busCatCode);
			ps.setString(3, tripType);
			ps.setString(4, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				timeTableDTO = new TimeTableDTO();
				timeTableDTO.setGenereatedRefNo(rs.getString("rs_generator_ref_no"));
				timeTableDTO.setStartTime(rs.getString("rs_start_date"));
				timeTableDTO.setEndTime(rs.getString("rs_end_date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return timeTableDTO;

	}

	@Override
	public List<String> getStartingTimeList(String genRefNo, String tripType, String groupNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT start_time_slot,end_time_slot " + "FROM public.nt_m_timetable_generator_det "
					+ "WHERE generator_ref_no=? " + "AND trip_type=? " + "AND group_no=? " + "ORDER BY seq_no";

			ps = con.prepareStatement(query);
			ps.setString(1, genRefNo);
			ps.setString(2, tripType);
			ps.setString(3, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				if (tripType.equalsIgnoreCase("O")) {
					value = rs.getString("start_time_slot");
					returnList.add(value);
				} else if (tripType.equalsIgnoreCase("D")) {
					value = rs.getString("end_time_slot");
					returnList.add(value);
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
	public List<String> getBusNos(String genRefNo, String tripType, String tripId, int noOfDays) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT bus_no \r\n" + "FROM nt_t_route_schedule_generator_det01 \r\n"
					+ "WHERE generator_ref_no=? \r\n" + "AND trip_type=? \r\n" + "AND trip_id=? \r\n"
					+ "AND day_no <= ? \r\n" + "ORDER BY seq";

			ps = con.prepareStatement(query);
			ps.setString(1, genRefNo);
			ps.setString(2, tripType);
			ps.setString(3, tripId);
			ps.setInt(4, noOfDays);

			rs = ps.executeQuery();

			while (rs.next()) {
				returnList.add(rs.getString("bus_no"));
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
	public List<String> getRouteNo(String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select no_of_bus_perweek,route_no,bus_category\r\n"
					+ "from  public.nt_m_panelgenerator \r\n"
					+ "inner join public.nt_r_service_types a on a.code=public.nt_m_panelgenerator.bus_category\r\n"
					+ "where a.active='A'\r\n" + "and ref_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				value = rs.getString("route_no");
				returnList.add(value);

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
	public List<String> getServiceType(String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select no_of_bus_perweek,route_no,bus_category,a.description\r\n"
					+ "from  public.nt_m_panelgenerator \r\n"
					+ "inner join public.nt_r_service_types a on a.code=public.nt_m_panelgenerator.bus_category\r\n"
					+ "where a.active='A'\r\n" + "and ref_no =?";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				value = rs.getString("description");
				returnList.add(value);

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
	public void deleteEmptyTimeSLots(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;

		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query1 = "delete  from public.nt_m_timetable_generator_det \r\n" + "where generator_ref_no=?\r\n"
					+ "and start_time_slot is null and end_time_slot is null;";

			ps = con.prepareStatement(query1);
			ps.setString(1, refNo);
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
	public boolean checkLeave(String tripType, String groupNo, String route, String service, String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean haveLeave = false;

		try {
			con = ConnectionManager.getConnection();

			String leaveQuery = "select * from  public.nt_t_route_schedule_generator_det02 where generator_ref_no=? and trip_type=?;";

			ps = con.prepareStatement(leaveQuery);
			ps.setString(1, refNo);
			ps.setString(2, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				haveLeave = true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return haveLeave;
	}

	@Override
	public boolean checkDataInBusAbre(String tripType, String groupNo, String route, String service, String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean haveLeave = false;

		try {
			con = ConnectionManager.getConnection();

			String leaveQuery = "select * from public.nt_h_bus_assign_for_abbreviation where generator_ref_no=? and group_no=? and trip_type=?;";

			ps = con.prepareStatement(leaveQuery);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				haveLeave = true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return haveLeave;
	}

	@Override
	public boolean checkBusAssign(String refNo, String tripType, String groupNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean busHave = false;

		try {
			con = ConnectionManager.getConnection();

			String leaveQuery = "select * from public.nt_m_timetable_generator_det  where assigned_bus_no is not null and generator_ref_no=? and trip_type=? and group_no=?;";

			ps = con.prepareStatement(leaveQuery);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				busHave = true;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busHave;
	}

	@Override
	public Map<String, List> getBusNOListNew(String refNO, String trip, String group, List<String> timeSlotList,
			List<String> busOrderListForOrigin, List<String> busOrderListForDestination, String originBus,
			String destinationBus, int dayOne, boolean isHistory) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement ps3 = null;

		ResultSet rs3 = null;

		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String, List> map = new HashMap();

		String value = "";

		List<String> returnList1 = new ArrayList<String>();
		List<String> returnList2 = new ArrayList<String>();
		List<String> returnList3 = new ArrayList<String>();
		List<String> returnList4 = new ArrayList<String>();
		List<String> returnList5 = new ArrayList<String>();
		List<String> returnList6 = new ArrayList<String>();
		List<String> returnList7 = new ArrayList<String>();
		List<String> returnList8 = new ArrayList<String>();
		List<String> returnList9 = new ArrayList<String>();
		List<String> returnList10 = new ArrayList<String>();
		List<String> returnList11 = new ArrayList<String>();
		List<String> returnList12 = new ArrayList<String>();
		List<String> returnList13 = new ArrayList<String>();
		List<String> returnList14 = new ArrayList<String>();
		List<String> returnList15 = new ArrayList<String>();
		List<String> returnList16 = new ArrayList<String>();
		List<String> returnList17 = new ArrayList<String>();
		List<String> returnList18 = new ArrayList<String>();
		List<String> returnList19 = new ArrayList<String>();
		List<String> returnList20 = new ArrayList<String>();
		List<String> returnList21 = new ArrayList<String>();
		List<String> returnList22 = new ArrayList<String>();
		List<String> returnList23 = new ArrayList<String>();
		List<String> returnList24 = new ArrayList<String>();
		List<String> returnList25 = new ArrayList<String>();
		List<String> returnList26 = new ArrayList<String>();
		List<String> returnList27 = new ArrayList<String>();
		List<String> returnList28 = new ArrayList<String>();
		List<String> returnList29 = new ArrayList<String>();
		List<String> returnList30 = new ArrayList<String>();
		List<String> returnList31 = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			boolean withLeave = false;
			String leaveQuery = "select * from  public.nt_t_route_schedule_generator_det02 where generator_ref_no=? and trip_type=?;";

			ps3 = con.prepareStatement(leaveQuery);
			ps3.setString(1, refNO);
			ps3.setString(2, trip);

			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				withLeave = true;
			}

			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);

			for (int i = 1; i <= dayOne; i++) {
				List<String> rotateBusListForOrgin = new ArrayList<>();
				List<String> rotateBusListForDestination = new ArrayList<>();

				if (isHistory) {
					// and b.rs_trip_type=?
					String queryBus = "select  a.bus_no,a.trip_type  from public.nt_h_route_schedule_generator_det01  a\r\n"
							+ "inner join public.nt_h_route_schedule_generator b \r\n"
							+ "on b.rs_generator_ref_no=a.generator_ref_no and b.rs_seq =a.mas_seq where b.rs_generator_ref_no=?\r\n"
							+ " and b.rs_group_no=? and a.day_no=? ;";

					ps = con.prepareStatement(queryBus);
					ps.setString(1, refNO);
					// ps.setString(2, trip);
					ps.setString(2, group);
					ps.setInt(3, i);
				} else {
					String queryBus = "SELECT a.bus_no, a.trip_type " +
			                "FROM public.nt_t_route_schedule_generator_det01 a " +
			                "INNER JOIN public.nt_m_route_schedule_generator b " +
			                "ON b.rs_generator_ref_no = a.generator_ref_no " +
			                "WHERE b.rs_generator_ref_no = ? " +
			                "AND b.rs_trip_type = ? " +
			                "AND b.rs_group_no = ? " +
			                "AND a.day_no = ? " +
			                "ORDER BY CASE WHEN a.trip_type = ? THEN 0 ELSE 1 END";

					ps = con.prepareStatement(queryBus);
					ps.setString(1, refNO);
					ps.setString(2, trip);
					ps.setString(3, group);
					ps.setInt(4, i);
					ps.setString(5, trip);

				}
				rs = ps.executeQuery();

				while (rs.next()) {

					if (trip.equals("O")) {
						rotateBusListForOrgin.add(rs.getString("bus_no")+rs.getString("trip_type"));
					}
					if (trip.equals("D")) {
						rotateBusListForDestination.add(rs.getString("bus_no")+rs.getString("trip_type"));
					}
				}

				List<String> orderdPerDayBusList = new ArrayList<>();
				List<String> list = new ArrayList<>();
				if (trip.equals("O")) {
					list = rotateBusListForOrgin;
				} else if (trip.equals("D")) {
					list = rotateBusListForDestination;
				}


				for (String bo : list) {
					orderdPerDayBusList.add(bo);
				}

				for (String op : orderdPerDayBusList) {
					switch (i) {
				    case 1:
				        returnList1.add(op);
				        break;
				    case 2:
				        returnList2.add(op);
				        break;
				    case 3:
				        returnList3.add(op);
				        break;
				    case 4:
				        returnList4.add(op);
				        break;
				    case 5:
				        returnList5.add(op);
				        break;
				    case 6:
				        returnList6.add(op);
				        break;
				    case 7:
				        returnList7.add(op);
				        break;
				    case 8:
				        returnList8.add(op);
				        break;
				    case 9:
				        returnList9.add(op);
				        break;
				    case 10:
				        returnList10.add(op);
				        break;
				    case 11:
				        returnList11.add(op);
				        break;
				    case 12:
				        returnList12.add(op);
				        break;
				    case 13:
				        returnList13.add(op);
				        break;
				    case 14:
				        returnList14.add(op);
				        break;
				    case 15:
				        returnList15.add(op);
				        break;
				    case 16:
				        returnList16.add(op);
				        break;
				    case 17:
				        returnList17.add(op);
				        break;
				    case 18:
				        returnList18.add(op);
				        break;
				    case 19:
				        returnList19.add(op);
				        break;
				    case 20:
				        returnList20.add(op);
				        break;
				    case 21:
				        returnList21.add(op);
				        break;
				    case 22:
				        returnList22.add(op);
				        break;
				    case 23:
				        returnList23.add(op);
				        break;
				    case 24:
				        returnList24.add(op);
				        break;
				    case 25:
				        returnList25.add(op);
				        break;
				    case 26:
				        returnList26.add(op);
				        break;
				    case 27:
				        returnList27.add(op);
				        break;
				    case 28:
				        returnList28.add(op);
				        break;
				    case 29:
				        returnList29.add(op);
				        break;
				    case 30:
				        returnList30.add(op);
				        break;
				    case 31:
				        returnList31.add(op);
				        break;
				    default:
				       
				        break;
				}

				}
			}

			String sql = "INSERT INTO public.nt_m_control_sheet\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql);

			Iterator<String> it1 = timeSlotList.iterator();
			Iterator<String> it2 = returnList1.iterator();
			Iterator<String> it3 = returnList2.iterator();
			Iterator<String> it4 = returnList3.iterator();
			Iterator<String> it5 = returnList4.iterator();
			Iterator<String> it6 = returnList5.iterator();
			Iterator<String> it7 = returnList6.iterator();
			for (; it1.hasNext();) {
				stmt.setString(1, (String) it2.next());
				stmt.setString(2, (String) it3.next());
				stmt.setString(3, (String) it4.next());
				stmt.setString(4, (String) it5.next());
				stmt.setString(5, (String) it6.next());
				stmt.setString(6, (String) it7.next());
				stmt.setString(7, it1.next());
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}
			/*** update bus numbers according to timeslots **/

			/** end **/
			String sql2 = "INSERT INTO public.nt_m_control_sheet2\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt2 = con.prepareStatement(sql2);

			Iterator<String> it8 = timeSlotList.iterator();
			Iterator<String> it9 = returnList7.iterator();
			Iterator<String> it10 = returnList8.iterator();
			Iterator<String> it11 = returnList9.iterator();
			Iterator<String> it12 = returnList10.iterator();
			Iterator<String> it13 = returnList11.iterator();
			Iterator<String> it14 = returnList12.iterator();
			for (; it8.hasNext() && it9.hasNext();) {
				stmt2.setString(1, (String) it9.next());
				stmt2.setString(2, (String) it10.next());
				stmt2.setString(3, (String) it11.next());
				stmt2.setString(4, (String) it12.next());
				stmt2.setString(5, (String) it13.next());
				stmt2.setString(6, (String) it14.next());
				stmt2.setString(7, it8.next());
				stmt2.addBatch();
				stmt2.executeUpdate();
				con.commit();
			}

			String sql3 = "INSERT INTO public.nt_m_control_sheet3\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt3 = con.prepareStatement(sql3);

			Iterator<String> it15 = timeSlotList.iterator();
			Iterator<String> it16 = returnList13.iterator();
			Iterator<String> it17 = returnList14.iterator();
			Iterator<String> it18 = returnList15.iterator();
			Iterator<String> it19 = returnList16.iterator();
			Iterator<String> it20 = returnList17.iterator();
			Iterator<String> it21 = returnList18.iterator();
			for (; it15.hasNext() && it16.hasNext();) {
				stmt3.setString(1, (String) it16.next());
				stmt3.setString(2, (String) it17.next());
				stmt3.setString(3, (String) it18.next());
				stmt3.setString(4, (String) it19.next());
				stmt3.setString(5, (String) it20.next());
				stmt3.setString(6, (String) it21.next());
				stmt3.setString(7, it15.next());
				stmt3.addBatch();
				stmt3.executeUpdate();
				con.commit();
			}

			String sql4 = "INSERT INTO public.nt_m_control_sheet4\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt4 = con.prepareStatement(sql4);

			Iterator<String> it22 = timeSlotList.iterator();
			Iterator<String> it23 = returnList19.iterator();
			Iterator<String> it24 = returnList20.iterator();
			Iterator<String> it25 = returnList21.iterator();
			Iterator<String> it26 = returnList22.iterator();
			Iterator<String> it27 = returnList23.iterator();
			Iterator<String> it28 = returnList24.iterator();
			for (; it22.hasNext() && it23.hasNext();) {
				stmt4.setString(1, (String) it23.next());
				stmt4.setString(2, (String) it24.next());
				stmt4.setString(3, (String) it25.next());
				stmt4.setString(4, (String) it26.next());
				stmt4.setString(5, (String) it27.next());
				stmt4.setString(6, (String) it28.next());
				stmt4.setString(7, it22.next());
				stmt4.addBatch();
				stmt4.executeUpdate();
				con.commit();
			}

			String sql5 = "INSERT INTO public.nt_m_control_sheet5\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot, col7)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?,?);;";

			stmt5 = con.prepareStatement(sql5);

			Iterator<String> it29 = timeSlotList.iterator();
			Iterator<String> it30 = returnList25.iterator();
			Iterator<String> it31 = returnList26.iterator();
			Iterator<String> it32 = returnList27.iterator();
			Iterator<String> it33 = returnList28.iterator();
			Iterator<String> it34 = returnList29.iterator();
			Iterator<String> it35 = returnList30.iterator();
			Iterator<String> it36 = returnList31.iterator();

			for (; it29.hasNext();) {
				if (returnList29.isEmpty() && returnList30.isEmpty() && returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, null);
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());
				}

				else if (returnList30.isEmpty() && returnList31.isEmpty()) {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else if (returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, (String) it36.next());
					stmt5.setString(8, it29.next());
				}

				stmt5.addBatch();
				stmt5.executeUpdate();
				con.commit();
			}

			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);

		} catch (

		Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs3);
			ConnectionManager.close(con);
		}

		return map;

	}

	@Override
	public String getAbriviatiosForRoute(String routeNo, String busCat, String side) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String abbreviation = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select rc_abbriviation_ltr_start , rc_abbriviation_ltr_end  from  public.nt_t_route_creator where rc_route_no =? and  rc_bus_type =? ";

			ps = con.prepareStatement(query2);

			ps.setString(1, routeNo);
			ps.setString(2, busCat);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (side.equalsIgnoreCase("O")) {
					abbreviation = rs.getString("rc_abbriviation_ltr_start");
				} else if (side.equalsIgnoreCase("D")) {
					abbreviation = rs.getString("rc_abbriviation_ltr_end");
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

		return abbreviation;
	}

	@Override
	public List<String> busAbbreviationOrder(String s1, String s2, String refNo, String side, String groupNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "";
//	Without SLTB & ETC
			if (side.equalsIgnoreCase("O")) {
				query = " select a.abbreviation,a.is_fixed,a.bus_no from public.panel_generator_origin_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? order by a.start_time  asc";
			} else if (side.equalsIgnoreCase("D")) {
				query = " select a.abbreviation,a.is_fixed,a.bus_no from public.panel_generator_destination_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? order by a.start_time  asc";
			}
			
//	With SLTB & ETC			
//			if (side.equalsIgnoreCase("O")) {
//				query = " select a.abbreviation,a.is_fixed,a.bus_no from public.panel_generator_origin_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and b.refno = ?  and b.groupcount = ? order by a.start_time  asc";
//			} else if (side.equalsIgnoreCase("D")) {
//				query = " select a.abbreviation,a.is_fixed,a.bus_no from public.panel_generator_destination_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and b.refno = ?  and b.groupcount = ? order by a.start_time  asc";
//			}

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				returnList.add(rs.getString("bus_no"));
				/*
				 * value = rs.getString("abbreviation").replaceAll("[0123456789]", ""); if
				 * (value.equalsIgnoreCase(s1) || value.equalsIgnoreCase("SLTB-O") ||
				 * value.equalsIgnoreCase("ETC-O")) { if (value.equalsIgnoreCase(s1) &&
				 * rs.getBoolean("is_fixed")) { returnList.add("O-Y" +
				 * rs.getString("abbreviation")); } else if (value.equalsIgnoreCase(s1) &&
				 * !rs.getBoolean("is_fixed")) { returnList.add("O"); } else if
				 * (value.equalsIgnoreCase("ETC-O")) { returnList.add("ETC-O"); }
				 * 
				 * else { returnList.add("SLTB-O"); } } else if (value.equalsIgnoreCase(s2) ||
				 * value.equalsIgnoreCase("SLTB-D") || value.equalsIgnoreCase("ETC-D")) { if
				 * (value.equalsIgnoreCase(s2) && rs.getBoolean("is_fixed")) {
				 * returnList.add("D-Y" + rs.getString("abbreviation")); } else if
				 * (value.equalsIgnoreCase(s2) && !rs.getBoolean("is_fixed")) {
				 * returnList.add("D"); } else if (value.equalsIgnoreCase("ETC-D")) {
				 * returnList.add("ETC-D"); }
				 * 
				 * else { returnList.add("SLTB-D"); }
				 * 
				 * }
				 */
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
	public List<String> busAbbreviationOrderWithLeave(String s1, String s2, String refNo, String side, String groupNo) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "";
			String sql = null;

			if (side.equalsIgnoreCase("O")) {
				query = " select a.abbreviation,a.is_fixed,a.bus_no from public.panel_generator_origin_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? order by a.start_time  asc";
				
				sql = "SELECT route_no, origin, abbreviation, permit_no, bus_no, id "
						+ "FROM panel_generator_leave_bus_details "
						+ "WHERE ref_no = ? "
						+ "AND ("
						+ "(abbreviation IS NOT NULL AND abbreviation <> '') OR "
						+ "(permit_no IS NOT NULL AND permit_no <> '') OR "
						+ "(bus_no IS NOT NULL AND bus_no <> ''))";
				
			} else if (side.equalsIgnoreCase("D")) {
				query = " select a.abbreviation,a.is_fixed,a.bus_no from public.panel_generator_destination_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? order by a.start_time  asc";
				
				sql = "SELECT route_no, origin, abbrivationdes, permit_no_des, bus_no_des, id "
						+ "FROM panel_generator_leave_bus_details "
						+ "WHERE ref_no = ? "
						+ "AND ("
						+ "(abbrivationdes IS NOT NULL AND abbrivationdes <> '') OR "
						+ "(permit_no_des IS NOT NULL AND permit_no_des <> '') OR "
						+ "(bus_no_des IS NOT NULL AND bus_no_des <> ''))";
			}

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				returnList.add(rs.getString("bus_no") + "O");
			}
			
			ps1 = con.prepareStatement(sql);
			ps1.setString(1, refNo);
			
			rs1 = ps1.executeQuery();
			while(rs1.next()) {
				if (side.equalsIgnoreCase("O")) {
					returnList.add(rs1.getString("bus_no") + "L");
				}
				else {
					returnList.add(rs1.getString("bus_no_des") + "L");
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1); 
			ConnectionManager.close(con);
		}

		return returnList;

	}

	@Override
	public List<String> busNoListWithLeave(String s1, String s2, String refNo, String side, String groupNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "";

			if (side.equalsIgnoreCase("O")) {
				query = " select a.abbreviation,a.is_fixed,a.bus_no from public.panel_generator_origin_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? order by a.start_time  asc";
			} else if (side.equalsIgnoreCase("D")) {
				query = " select a.abbreviation,a.is_fixed,a.bus_no from public.panel_generator_destination_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? order by a.start_time  asc";
			}

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				if(side.equalsIgnoreCase("O") && rs.getString("abbreviation").contains(s1)) {
					returnList.add(rs.getString("bus_no"));
				}else if(side.equalsIgnoreCase("D") && rs.getString("abbreviation").contains(s2)) {
					returnList.add(rs.getString("bus_no"));
				}
				
				/*
				 * value = rs.getString("abbreviation").replaceAll("[0123456789]", ""); if
				 * (value.equalsIgnoreCase(s1) || value.equalsIgnoreCase("SLTB-O") ||
				 * value.equalsIgnoreCase("ETC-O")) { if (value.equalsIgnoreCase(s1) &&
				 * rs.getBoolean("is_fixed")) { returnList.add("O-Y" +
				 * rs.getString("abbreviation")); } else if (value.equalsIgnoreCase(s1) &&
				 * !rs.getBoolean("is_fixed")) { returnList.add("O"); } else if
				 * (value.equalsIgnoreCase("ETC-O")) { returnList.add("ETC-O"); }
				 * 
				 * else { returnList.add("SLTB-O"); } } else if (value.equalsIgnoreCase(s2) ||
				 * value.equalsIgnoreCase("SLTB-D") || value.equalsIgnoreCase("ETC-D")) { if
				 * (value.equalsIgnoreCase(s2) && rs.getBoolean("is_fixed")) {
				 * returnList.add("D-Y" + rs.getString("abbreviation")); } else if
				 * (value.equalsIgnoreCase(s2) && !rs.getBoolean("is_fixed")) {
				 * returnList.add("D"); } else if (value.equalsIgnoreCase("ETC-D")) {
				 * returnList.add("ETC-D"); }
				 * 
				 * else { returnList.add("SLTB-D"); }
				 * 
				 * }
				 */
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
	public Map<String, List> getBusNOListNewWithLeave(String refNO, String trip, String group,
			List<String> timeSlotList, List<String> busOrderListForOrigin, List<String> busOrderListForDestination,
			String originBus, String destinationBus, int dayOne, boolean isHistory) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement ps3 = null;

		ResultSet rs3 = null;

		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String, List> map = new HashMap();

		String value = "";

		List<String> returnList = new ArrayList<String>();
		List<String> returnList2 = new ArrayList<String>();
		List<String> returnList3 = new ArrayList<String>();
		List<String> returnList4 = new ArrayList<String>();
		List<String> returnList5 = new ArrayList<String>();
		List<String> returnList6 = new ArrayList<String>();
		List<String> returnList7 = new ArrayList<String>();
		List<String> returnList8 = new ArrayList<String>();
		List<String> returnList9 = new ArrayList<String>();
		List<String> returnList10 = new ArrayList<String>();
		List<String> returnList11 = new ArrayList<String>();
		List<String> returnList12 = new ArrayList<String>();
		List<String> returnList13 = new ArrayList<String>();
		List<String> returnList14 = new ArrayList<String>();
		List<String> returnList15 = new ArrayList<String>();
		List<String> returnList16 = new ArrayList<String>();
		List<String> returnList17 = new ArrayList<String>();
		List<String> returnList18 = new ArrayList<String>();
		List<String> returnList19 = new ArrayList<String>();
		List<String> returnList20 = new ArrayList<String>();
		List<String> returnList21 = new ArrayList<String>();
		List<String> returnList22 = new ArrayList<String>();
		List<String> returnList23 = new ArrayList<String>();
		List<String> returnList24 = new ArrayList<String>();
		List<String> returnList25 = new ArrayList<String>();
		List<String> returnList26 = new ArrayList<String>();
		List<String> returnList27 = new ArrayList<String>();
		List<String> returnList28 = new ArrayList<String>();
		List<String> returnList29 = new ArrayList<String>();
		List<String> returnList30 = new ArrayList<String>();
		List<String> returnList31 = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			boolean multiCouple = false;
			String coupleQuery = "\r\n" + "SELECT no_of_couples_per_one_bus FROM public.nt_m_timetable_generator \r\n"
					+ "WHERE generator_ref_no =?  and  trip_type =? and group_no =?;";

			ps3 = con.prepareStatement(coupleQuery);
			ps3.setString(1, refNO);
			ps3.setString(2, trip);
			ps3.setString(3, group);
			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				multiCouple = true;
			}

			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);

			for (int i = 1; i <= dayOne; i++) {
				List<String> rotateBusListForOrgin = new ArrayList<>();
				List<String> rotateBusListForDestination = new ArrayList<>();

				if (multiCouple) {
					if (isHistory) {
						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type  \r\n"
								+ "from public.nt_h_route_schedule_generator_det01 a \r\n"
								+ "inner join public.nt_h_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no  and a.mas_seq =b.rs_seq \r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no \r\n"
								+ "where b.rs_generator_ref_no=? and b.rs_trip_type='O' \r\n"
								+ "and b.rs_group_no=? and a.day_no=? and c.trip_type='O' \r\n"
								+ "and a.bus_no not in \r\n" + "(select a.bus_no \r\n"
								+ "from public.nt_h_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
								+ "where a.generator_ref_no= ?\r\n" + "and b.leave_position=a.trip_id  \r\n"
								+ "and a.day_no =?) order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setInt(7, i);
					}

					else {
						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "where b.rs_generator_ref_no=? and b.rs_trip_type=?\r\n"
								+ "and b.rs_group_no=? and a.day_no=? and c.trip_type=?\r\n"
								+ "and a.bus_no not in \r\n" + "(select a.bus_no\r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
								+ "where a.generator_ref_no= ?\r\n" + "and b.leave_position=a.trip_id  \r\n"
								+ "and a.day_no =?) order by a.seq;;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setInt(7, i);
					}

				}

				else {

					if (isHistory) {

						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "from public.nt_h_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_h_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no and a.mas_seq =b.rs_seq \r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "where b.rs_generator_ref_no='' and b.rs_trip_type=?\r\n"
								+ "and b.rs_group_no=? and a.day_no=? and c.trip_type=?\r\n"
								+ "and a.trip_id not in \r\n"
								+ "(select distinct c.leave_position from  public.nt_t_route_schedule_generator_det02 c\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det01 x on x.generator_ref_no=?\r\n"
								+ "where c.generator_ref_no=? and c.trip_type=a.trip_type) order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setString(7, refNO);

					}

					else {

						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "where b.rs_generator_ref_no=?\r\n" + "and b.rs_trip_type=?\r\n"
								+ "and b.rs_group_no=?\r\n" + "and a.day_no=?\r\n" + "and c.trip_type=?\r\n"
								+ "and a.trip_id\r\n" + "not in \r\n"
								+ "(select distinct c.leave_position from  public.nt_t_route_schedule_generator_det02 c\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det01 x on x.generator_ref_no=?\r\n"
								+ "where c.generator_ref_no=? and c.trip_type=a.trip_type)\r\n" + "order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setString(7, refNO);
					}

				}
				rs = ps.executeQuery();

//				while (rs.next()) {
//					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
//						rotateBusListForOrgin.add(rs.getString("bus_no"));
//					}
//					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(destinationBus)) {
//						rotateBusListForDestination.add(rs.getString("bus_no"));
//					}
//				}
				
				while (rs.next()) {
					if (rs.getString("trip_type").equals("O")) {
						rotateBusListForOrgin.add(rs.getString("bus_no"));
					}
					if (rs.getString("trip_type").equals("D")) {
						rotateBusListForDestination.add(rs.getString("bus_no"));
					}
				}

				List<String> orderdPerDayBusList = new ArrayList<>();
				List<String> list = new ArrayList<>();
				if (trip.equals("O")) {
					list = busOrderListForOrigin;
				} else if (trip.equals("D")) {
					list = busOrderListForDestination;
				}


				for (String bo : list) {
					if (bo.contains("O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()  ) {
							orderdPerDayBusList.add(bo);					
					}
					else if(bo.contains("L") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()  ) {
						orderdPerDayBusList.add(bo);
					}
				}

				for (String op : orderdPerDayBusList) {
					switch (i) {
				    case 1:
				    	String query1 = "select distinct nt_t_route_schedule_generator_det01.bus_no,nt_t_route_schedule_generator_det01.trip_id\r\n"
				    			+ "from public.nt_t_route_schedule_generator_det01 \r\n"
				    			+ "inner join nt_m_route_schedule_generator \r\n"
				    			+ "on nt_m_route_schedule_generator.rs_generator_ref_no = nt_t_route_schedule_generator_det01.generator_ref_no \r\n"
				    			+ "where nt_t_route_schedule_generator_det01.generator_ref_no= ? and nt_t_route_schedule_generator_det01.day_no = ? \r\n"
				    			+ "and nt_m_route_schedule_generator.rs_group_no = ? and nt_t_route_schedule_generator_det01.trip_type = ?\r\n"
				    			+ "and nt_t_route_schedule_generator_det01.bus_no IS NOT null order by nt_t_route_schedule_generator_det01.trip_id ";
  						ps1 = con.prepareStatement(query1);
  						ps1.setString(1, refNO);
  						ps1.setString(2, group);
  						//ps1.setString(4, trip);

  						rs1 = ps1.executeQuery();
  						while (rs1.next()) {
  							
  							value = rs1.getString("assigned_bus_no") + "F";
  					         returnList.add(value);

  						}
  						ConnectionManager.close(rs1);
  						ConnectionManager.close(ps1);
				        returnList.add(op);
				        break;
				    case 2:
				        returnList2.add(op);
				        break;
				    case 3:
				        returnList3.add(op);
				        break;
				    case 4:
				        returnList4.add(op);
				        break;
				    case 5:
				        returnList5.add(op);
				        break;
				    case 6:
				        returnList6.add(op);
				        break;
				    case 7:
				        returnList7.add(op);
				        break;
				    case 8:
				        returnList8.add(op);
				        break;
				    case 9:
				        returnList9.add(op);
				        break;
				    case 10:
				        returnList10.add(op);
				        break;
				    case 11:
				        returnList11.add(op);
				        break;
				    case 12:
				        returnList12.add(op);
				        break;
				    case 13:
				        returnList13.add(op);
				        break;
				    case 14:
				        returnList14.add(op);
				        break;
				    case 15:
				        returnList15.add(op);
				        break;
				    case 16:
				        returnList16.add(op);
				        break;
				    case 17:
				        returnList17.add(op);
				        break;
				    case 18:
				        returnList18.add(op);
				        break;
				    case 19:
				        returnList19.add(op);
				        break;
				    case 20:
				        returnList20.add(op);
				        break;
				    case 21:
				        returnList21.add(op);
				        break;
				    case 22:
				        returnList22.add(op);
				        break;
				    case 23:
				        returnList23.add(op);
				        break;
				    case 24:
				        returnList24.add(op);
				        break;
				    case 25:
				        returnList25.add(op);
				        break;
				    case 26:
				        returnList26.add(op);
				        break;
				    case 27:
				        returnList27.add(op);
				        break;
				    case 28:
				        returnList28.add(op);
				        break;
				    case 29:
				        returnList29.add(op);
				        break;
				    case 30:
				        returnList30.add(op);
				        break;
				    case 31:
				        returnList31.add(op);
				        break;
				    default:
				       
				        break;
				}

				}
			}
			String sql = "INSERT INTO public.nt_m_control_sheet\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql);

			Iterator<String> it1 = timeSlotList.iterator();
			Iterator<String> it2 = returnList.iterator();
			Iterator<String> it3 = returnList2.iterator();
			Iterator<String> it4 = returnList3.iterator();
			Iterator<String> it5 = returnList4.iterator();
			Iterator<String> it6 = returnList5.iterator();
			Iterator<String> it7 = returnList6.iterator();
			for (; it1.hasNext();) {
				stmt.setString(1, (String) it2.next());
				stmt.setString(2, (String) it3.next());
				stmt.setString(3, (String) it4.next());
				stmt.setString(4, (String) it5.next());
				stmt.setString(5, (String) it6.next());
				stmt.setString(6, (String) it7.next());
				stmt.setString(7, it1.next());
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}
			/*** update bus numbers according to timeslots **/

			/** end **/
			String sql2 = "INSERT INTO public.nt_m_control_sheet2\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt2 = con.prepareStatement(sql2);

			Iterator<String> it8 = timeSlotList.iterator();
			Iterator<String> it9 = returnList7.iterator();
			Iterator<String> it10 = returnList8.iterator();
			Iterator<String> it11 = returnList9.iterator();
			Iterator<String> it12 = returnList10.iterator();
			Iterator<String> it13 = returnList11.iterator();
			Iterator<String> it14 = returnList12.iterator();
			for (; it8.hasNext() && it9.hasNext();) {
				stmt2.setString(1, (String) it9.next());
				stmt2.setString(2, (String) it10.next());
				stmt2.setString(3, (String) it11.next());
				stmt2.setString(4, (String) it12.next());
				stmt2.setString(5, (String) it13.next());
				stmt2.setString(6, (String) it14.next());
				stmt2.setString(7, it8.next());
				stmt2.addBatch();
				stmt2.executeUpdate();
				con.commit();
			}

			String sql3 = "INSERT INTO public.nt_m_control_sheet3\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt3 = con.prepareStatement(sql3);

			Iterator<String> it15 = timeSlotList.iterator();
			Iterator<String> it16 = returnList13.iterator();
			Iterator<String> it17 = returnList14.iterator();
			Iterator<String> it18 = returnList15.iterator();
			Iterator<String> it19 = returnList16.iterator();
			Iterator<String> it20 = returnList17.iterator();
			Iterator<String> it21 = returnList18.iterator();
			for (; it15.hasNext() && it16.hasNext();) {
				stmt3.setString(1, (String) it16.next());
				stmt3.setString(2, (String) it17.next());
				stmt3.setString(3, (String) it18.next());
				stmt3.setString(4, (String) it19.next());
				stmt3.setString(5, (String) it20.next());
				stmt3.setString(6, (String) it21.next());
				stmt3.setString(7, it15.next());
				stmt3.addBatch();
				stmt3.executeUpdate();
				con.commit();
			}

			String sql4 = "INSERT INTO public.nt_m_control_sheet4\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt4 = con.prepareStatement(sql4);

			Iterator<String> it22 = timeSlotList.iterator();
			Iterator<String> it23 = returnList19.iterator();
			Iterator<String> it24 = returnList20.iterator();
			Iterator<String> it25 = returnList21.iterator();
			Iterator<String> it26 = returnList22.iterator();
			Iterator<String> it27 = returnList23.iterator();
			Iterator<String> it28 = returnList24.iterator();
			for (; it22.hasNext() && it23.hasNext();) {
				stmt4.setString(1, (String) it23.next());
				stmt4.setString(2, (String) it24.next());
				stmt4.setString(3, (String) it25.next());
				stmt4.setString(4, (String) it26.next());
				stmt4.setString(5, (String) it27.next());
				stmt4.setString(6, (String) it28.next());
				stmt4.setString(7, it22.next());
				stmt4.addBatch();
				stmt4.executeUpdate();
				con.commit();
			}

			String sql5 = "INSERT INTO public.nt_m_control_sheet5\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot, col7)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?,?);;";

			stmt5 = con.prepareStatement(sql5);

			Iterator<String> it29 = timeSlotList.iterator();
			Iterator<String> it30 = returnList25.iterator();
			Iterator<String> it31 = returnList26.iterator();
			Iterator<String> it32 = returnList27.iterator();
			Iterator<String> it33 = returnList28.iterator();
			Iterator<String> it34 = returnList29.iterator();
			Iterator<String> it35 = returnList30.iterator();
			Iterator<String> it36 = returnList31.iterator();

			for (; it29.hasNext();) {
				if (returnList29.isEmpty() && returnList30.isEmpty() && returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, null);
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());
				}

				else if (returnList30.isEmpty() && returnList31.isEmpty()) {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else if (returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, (String) it36.next());
					stmt5.setString(8, it29.next());
				}

				stmt5.addBatch();
				stmt5.executeUpdate();
				con.commit();
			}

			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);

		} catch (

		Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs3);
			ConnectionManager.close(con);
		}

		return map;

	}

	@Override
	public Map<String, List> getBusNOList2WithLeave(String refNO, String trip, String group, List<String> timeSlotList,
			List<String> busOrderListForOrigin, List<String> busOrderListForDestination, String originBus,
			String destinationBus, int dayOne, int dayTwo, boolean isHistory) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement ps3 = null;

		ResultSet rs3 = null;

		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String, List> map = new HashMap();

		String value = "";

		List<String> returnList = new ArrayList<String>();
		List<String> returnList2 = new ArrayList<String>();
		List<String> returnList3 = new ArrayList<String>();
		List<String> returnList4 = new ArrayList<String>();
		List<String> returnList5 = new ArrayList<String>();
		List<String> returnList6 = new ArrayList<String>();
		List<String> returnList7 = new ArrayList<String>();
		List<String> returnList8 = new ArrayList<String>();
		List<String> returnList9 = new ArrayList<String>();
		List<String> returnList10 = new ArrayList<String>();
		List<String> returnList11 = new ArrayList<String>();
		List<String> returnList12 = new ArrayList<String>();
		List<String> returnList13 = new ArrayList<String>();
		List<String> returnList14 = new ArrayList<String>();
		List<String> returnList15 = new ArrayList<String>();
		List<String> returnList16 = new ArrayList<String>();
		List<String> returnList17 = new ArrayList<String>();
		List<String> returnList18 = new ArrayList<String>();
		List<String> returnList19 = new ArrayList<String>();
		List<String> returnList20 = new ArrayList<String>();
		List<String> returnList21 = new ArrayList<String>();
		List<String> returnList22 = new ArrayList<String>();
		List<String> returnList23 = new ArrayList<String>();
		List<String> returnList24 = new ArrayList<String>();
		List<String> returnList25 = new ArrayList<String>();
		List<String> returnList26 = new ArrayList<String>();
		List<String> returnList27 = new ArrayList<String>();
		List<String> returnList28 = new ArrayList<String>();
		List<String> returnList29 = new ArrayList<String>();
		List<String> returnList30 = new ArrayList<String>();
		List<String> returnList31 = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			boolean multiCouple = false;
			String coupleQuery = "\r\n" + "SELECT no_of_couples_per_one_bus FROM public.nt_m_timetable_generator \r\n"
					+ "WHERE generator_ref_no =?  and  trip_type =? and group_no =?;";

			ps3 = con.prepareStatement(coupleQuery);
			ps3.setString(1, refNO);
			ps3.setString(2, trip);
			ps3.setString(3, group);
			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				multiCouple = true;
			}

			for (int i = dayOne + 1; i <= dayTwo + 1; i++) {
				List<String> rotateBusListForOrgin = new ArrayList<>();
				List<String> rotateBusListForDestination = new ArrayList<>();

				if (multiCouple) {
					if (isHistory) {
						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type  \r\n"
								+ "from public.nt_h_route_schedule_generator_det01 a \r\n"
								+ "inner join public.nt_h_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no  and a.mas_seq =b.rs_seq \r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no \r\n"
								+ "where b.rs_generator_ref_no=? and b.rs_trip_type='O' \r\n"
								+ "and b.rs_group_no=? and a.day_no=? and c.trip_type='O' \r\n"
								+ "and a.bus_no not in \r\n" + "(select a.bus_no \r\n"
								+ "from public.nt_h_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
								+ "where a.generator_ref_no= ?\r\n" + "and b.leave_position=a.trip_id  \r\n"
								+ "and a.day_no =?) order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setInt(7, i);
					}

					else {
						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "where b.rs_generator_ref_no=? and b.rs_trip_type=?\r\n"
								+ "and b.rs_group_no=? and a.day_no=? and c.trip_type=?\r\n"
								+ "and a.bus_no not in \r\n" + "(select a.bus_no\r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
								+ "where a.generator_ref_no= ?\r\n" + "and b.leave_position=a.trip_id  \r\n"
								+ "and a.day_no =?) order by a.seq;;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setInt(7, i);
					}

				}

				else {

					if (isHistory) {

						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "						from public.nt_h_route_schedule_generator_det01 a\r\n"
								+ "						inner join public.nt_h_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no and a.mas_seq =b.rs_seq \r\n"
								+ "						inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "						where b.rs_generator_ref_no='' and b.rs_trip_type=?\r\n"
								+ "						and b.rs_group_no=? and a.day_no=? and c.trip_type=?\r\n"
								+ "						and a.trip_id not in \r\n"
								+ "						(select distinct c.leave_position from  public.nt_t_route_schedule_generator_det02 c\r\n"
								+ "						inner join public.nt_t_route_schedule_generator_det01 x on x.generator_ref_no=?\r\n"
								+ "						where c.generator_ref_no=? and c.trip_type=a.trip_type) order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setString(7, refNO);

					}

					else {

						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "where b.rs_generator_ref_no=?\r\n" + "and b.rs_trip_type=?\r\n"
								+ "and b.rs_group_no=?\r\n" + "and a.day_no=?\r\n" + "and c.trip_type=?\r\n"
								+ "and a.trip_id\r\n" + "not in \r\n"
								+ "(select distinct c.leave_position from  public.nt_t_route_schedule_generator_det02 c\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det01 x on x.generator_ref_no=?\r\n"
								+ "where c.generator_ref_no=? and c.trip_type=a.trip_type)\r\n" + "order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setString(7, refNO);
					}

				}
				rs = ps.executeQuery();

				while (rs.next()) {
					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
						rotateBusListForOrgin.add(rs.getString("bus_no"));
					}
					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(destinationBus)) {
						rotateBusListForDestination.add(rs.getString("bus_no"));
					}
				}

				List<String> orderdPerDayBusList = new ArrayList<>();
				List<String> list = new ArrayList<>();
				if (trip.equals("O")) {
					list = busOrderListForOrigin;
				} else if (trip.equals("D")) {
					list = busOrderListForDestination;
				}

				int oi = 0;

				int di = 0;

				for (String bo : list) {

					if (bo.equals("O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						for (int k = oi; k <= rotateBusListForOrgin.size(); k++) {

							orderdPerDayBusList.add(rotateBusListForOrgin.get(oi));
							oi++;

							break;

						}

					} else if (bo.equals("D") && rotateBusListForDestination != null
							&& !rotateBusListForDestination.isEmpty()) {

						for (int k = di; k <= rotateBusListForDestination.size(); k++) {

							orderdPerDayBusList.add(rotateBusListForDestination.get(di));
							di++;

							break;

						}

					}

					/** for fixed pvt bus **/

					else if (bo.contains("O-Y") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {
						orderdPerDayBusList.add(bo);

					}

					else if (bo.contains("D-Y") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {
						orderdPerDayBusList.add(bo);

					}
					/** for fixed pvt bus end **/

					else if (bo.equals("SLTB-O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("SLTB-O");
					}

					else if (bo.equals("SLTB-D") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("SLTB-D");
					}

				}

				for (String op : orderdPerDayBusList) {
					String abbr = null;

					if (dayOne == 30) {
						if (i == 31) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}

						if (i == 32) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList2.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 33) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList3.add(value);

									;
								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 34) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList4.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 35) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList5.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 36) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList6.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 37) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList7.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 38) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList8.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 39) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList9.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 40) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList10.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 41) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList11.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 42) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList12.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 43) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList13.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 44) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList14.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 45) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList15.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 46) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList16.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 47) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList17.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 48) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList18.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 49) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList19.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 50) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList20.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 51) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList21.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 52) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList22.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 53) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList23.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 54) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList24.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 55) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList25.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 56) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList26.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 57) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList27.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 58) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList28.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 59) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList29.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 60) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList30.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 61) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList31.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
					} else if (dayOne == 31) {
						if (i == 32) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}

						if (i == 33) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList2.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 34) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList3.add(value);

									;
								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 35) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList4.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 36) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList5.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 37) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList6.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 38) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList7.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 39) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList8.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 40) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList9.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 41) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList10.add(value);

								}
							}
						}
						if (i == 42) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList11.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 43) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList12.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 44) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList13.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 45) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList14.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 46) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList15.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 47) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList16.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 48) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList17.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 49) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList18.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 50) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList19.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 51) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList20.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 52) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList21.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 53) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList22.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 54) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList23.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 55) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList24.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 56) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList25.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 57) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList26.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 58) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList27.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 59) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList28.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 60) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList29.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 61) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList30.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 62) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList31.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
					}
				}
			}
			String sql = "INSERT INTO public.nt_m_control_sheet\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql);

			Iterator<String> it1 = timeSlotList.iterator();
			Iterator<String> it2 = returnList.iterator();
			Iterator<String> it3 = returnList2.iterator();
			Iterator<String> it4 = returnList3.iterator();
			Iterator<String> it5 = returnList4.iterator();
			Iterator<String> it6 = returnList5.iterator();
			Iterator<String> it7 = returnList6.iterator();
			for (; it1.hasNext();) {
				stmt.setString(1, (String) it2.next());
				stmt.setString(2, (String) it3.next());
				stmt.setString(3, (String) it4.next());
				stmt.setString(4, (String) it5.next());
				stmt.setString(5, (String) it6.next());
				stmt.setString(6, (String) it7.next());
				stmt.setString(7, it1.next());
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}
			/*** update bus numbers according to timeslots **/

			/** end **/
			String sql2 = "INSERT INTO public.nt_m_control_sheet2\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt2 = con.prepareStatement(sql2);

			Iterator<String> it8 = timeSlotList.iterator();
			Iterator<String> it9 = returnList7.iterator();
			Iterator<String> it10 = returnList8.iterator();
			Iterator<String> it11 = returnList9.iterator();
			Iterator<String> it12 = returnList10.iterator();
			Iterator<String> it13 = returnList11.iterator();
			Iterator<String> it14 = returnList12.iterator();
			for (; it8.hasNext() && it9.hasNext();) {
				stmt2.setString(1, (String) it9.next());
				stmt2.setString(2, (String) it10.next());
				stmt2.setString(3, (String) it11.next());
				stmt2.setString(4, (String) it12.next());
				stmt2.setString(5, (String) it13.next());
				stmt2.setString(6, (String) it14.next());
				stmt2.setString(7, it8.next());
				stmt2.addBatch();
				stmt2.executeUpdate();
				con.commit();
			}

			String sql3 = "INSERT INTO public.nt_m_control_sheet3\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt3 = con.prepareStatement(sql3);

			Iterator<String> it15 = timeSlotList.iterator();
			Iterator<String> it16 = returnList13.iterator();
			Iterator<String> it17 = returnList14.iterator();
			Iterator<String> it18 = returnList15.iterator();
			Iterator<String> it19 = returnList16.iterator();
			Iterator<String> it20 = returnList17.iterator();
			Iterator<String> it21 = returnList18.iterator();
			for (; it15.hasNext() && it16.hasNext();) {
				stmt3.setString(1, (String) it16.next());
				stmt3.setString(2, (String) it17.next());
				stmt3.setString(3, (String) it18.next());
				stmt3.setString(4, (String) it19.next());
				stmt3.setString(5, (String) it20.next());
				stmt3.setString(6, (String) it21.next());
				stmt3.setString(7, it15.next());
				stmt3.addBatch();
				stmt3.executeUpdate();
				con.commit();
			}

			String sql4 = "INSERT INTO public.nt_m_control_sheet4\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt4 = con.prepareStatement(sql4);

			Iterator<String> it22 = timeSlotList.iterator();
			Iterator<String> it23 = returnList19.iterator();
			Iterator<String> it24 = returnList20.iterator();
			Iterator<String> it25 = returnList21.iterator();
			Iterator<String> it26 = returnList22.iterator();
			Iterator<String> it27 = returnList23.iterator();
			Iterator<String> it28 = returnList24.iterator();
			for (; it22.hasNext() && it23.hasNext();) {
				stmt4.setString(1, (String) it23.next());
				stmt4.setString(2, (String) it24.next());
				stmt4.setString(3, (String) it25.next());
				stmt4.setString(4, (String) it26.next());
				stmt4.setString(5, (String) it27.next());
				stmt4.setString(6, (String) it28.next());
				stmt4.setString(7, it22.next());
				stmt4.addBatch();
				stmt4.executeUpdate();
				con.commit();
			}

			String sql5 = "INSERT INTO public.nt_m_control_sheet5\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot, col7)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?,?);;";

			stmt5 = con.prepareStatement(sql5);

			Iterator<String> it29 = timeSlotList.iterator();
			Iterator<String> it30 = returnList25.iterator();
			Iterator<String> it31 = returnList26.iterator();
			Iterator<String> it32 = returnList27.iterator();
			Iterator<String> it33 = returnList28.iterator();
			Iterator<String> it34 = returnList29.iterator();
			Iterator<String> it35 = returnList30.iterator();
			Iterator<String> it36 = returnList31.iterator();

			for (; it29.hasNext();) {
				if (returnList29.isEmpty() && returnList30.isEmpty() && returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, null);
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());
				}

				else if (returnList30.isEmpty() && returnList31.isEmpty()) {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else if (returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, (String) it36.next());
					stmt5.setString(8, it29.next());
				}

				stmt5.addBatch();
				stmt5.executeUpdate();
				con.commit();
			}

			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);

		} catch (

		Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs3);
			ConnectionManager.close(con);
		}

		return map;

	}

	@Override
	public Map<String, List> getBusNOList3WithLeave(String refNO, String trip, String group, List<String> timeSlotList,
			List<String> busOrderListForOrigin, List<String> busOrderListForDestination, String originBus,
			String destinationBus, int dayTwo, int dayThree, boolean isHistory) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement ps3 = null;

		ResultSet rs3 = null;

		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String, List> map = new HashMap();

		String value = "";

		List<String> returnList = new ArrayList<String>();
		List<String> returnList2 = new ArrayList<String>();
		List<String> returnList3 = new ArrayList<String>();
		List<String> returnList4 = new ArrayList<String>();
		List<String> returnList5 = new ArrayList<String>();
		List<String> returnList6 = new ArrayList<String>();
		List<String> returnList7 = new ArrayList<String>();
		List<String> returnList8 = new ArrayList<String>();
		List<String> returnList9 = new ArrayList<String>();
		List<String> returnList10 = new ArrayList<String>();
		List<String> returnList11 = new ArrayList<String>();
		List<String> returnList12 = new ArrayList<String>();
		List<String> returnList13 = new ArrayList<String>();
		List<String> returnList14 = new ArrayList<String>();
		List<String> returnList15 = new ArrayList<String>();
		List<String> returnList16 = new ArrayList<String>();
		List<String> returnList17 = new ArrayList<String>();
		List<String> returnList18 = new ArrayList<String>();
		List<String> returnList19 = new ArrayList<String>();
		List<String> returnList20 = new ArrayList<String>();
		List<String> returnList21 = new ArrayList<String>();
		List<String> returnList22 = new ArrayList<String>();
		List<String> returnList23 = new ArrayList<String>();
		List<String> returnList24 = new ArrayList<String>();
		List<String> returnList25 = new ArrayList<String>();
		List<String> returnList26 = new ArrayList<String>();
		List<String> returnList27 = new ArrayList<String>();
		List<String> returnList28 = new ArrayList<String>();
		List<String> returnList29 = new ArrayList<String>();
		List<String> returnList30 = new ArrayList<String>();
		List<String> returnList31 = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			boolean multiCouple = false;
			String coupleQuery = "\r\n" + "SELECT no_of_couples_per_one_bus FROM public.nt_m_timetable_generator \r\n"
					+ "WHERE generator_ref_no =?  and  trip_type =? and group_no =?;";

			ps3 = con.prepareStatement(coupleQuery);
			ps3.setString(1, refNO);
			ps3.setString(2, trip);
			ps3.setString(3, group);
			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				multiCouple = true;
			}

			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);

			for (int i = dayTwo + 1; i <= dayThree + 1; i++) {
				List<String> rotateBusListForOrgin = new ArrayList<>();
				List<String> rotateBusListForDestination = new ArrayList<>();

				if (multiCouple) {
					if (isHistory) {
						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type  \r\n"
								+ "from public.nt_h_route_schedule_generator_det01 a \r\n"
								+ "inner join public.nt_h_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no  and a.mas_seq =b.rs_seq \r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no \r\n"
								+ "where b.rs_generator_ref_no=? and b.rs_trip_type='O' \r\n"
								+ "and b.rs_group_no=? and a.day_no=? and c.trip_type='O' \r\n"
								+ "and a.bus_no not in \r\n" + "(select a.bus_no \r\n"
								+ "from public.nt_h_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
								+ "where a.generator_ref_no= ?\r\n" + "and b.leave_position=a.trip_id  \r\n"
								+ "and a.day_no =?) order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setInt(7, i);
					}

					else {
						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "where b.rs_generator_ref_no=? and b.rs_trip_type=?\r\n"
								+ "and b.rs_group_no=? and a.day_no=? and c.trip_type=?\r\n"
								+ "and a.bus_no not in \r\n" + "(select a.bus_no\r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
								+ "where a.generator_ref_no= ?\r\n" + "and b.leave_position=a.trip_id  \r\n"
								+ "and a.day_no =?) order by a.seq;;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setInt(7, i);
					}

				}

				else {

					if (isHistory) {

						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "						from public.nt_h_route_schedule_generator_det01 a\r\n"
								+ "						inner join public.nt_h_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no and a.mas_seq =b.rs_seq \r\n"
								+ "						inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "						where b.rs_generator_ref_no='' and b.rs_trip_type=?\r\n"
								+ "						and b.rs_group_no=? and a.day_no=? and c.trip_type=?\r\n"
								+ "						and a.trip_id not in \r\n"
								+ "						(select distinct c.leave_position from  public.nt_t_route_schedule_generator_det02 c\r\n"
								+ "						inner join public.nt_t_route_schedule_generator_det01 x on x.generator_ref_no=?\r\n"
								+ "						where c.generator_ref_no=? and c.trip_type=a.trip_type) order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setString(7, refNO);

					}

					else {

						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "where b.rs_generator_ref_no=?\r\n" + "and b.rs_trip_type=?\r\n"
								+ "and b.rs_group_no=?\r\n" + "and a.day_no=?\r\n" + "and c.trip_type=?\r\n"
								+ "and a.trip_id\r\n" + "not in \r\n"
								+ "(select distinct c.leave_position from  public.nt_t_route_schedule_generator_det02 c\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det01 x on x.generator_ref_no=?\r\n"
								+ "where c.generator_ref_no=? and c.trip_type=a.trip_type)\r\n" + "order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setString(7, refNO);
					}

				}
				rs = ps.executeQuery();

				while (rs.next()) {
					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
						rotateBusListForOrgin.add(rs.getString("bus_no"));
					}
					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(destinationBus)) {
						rotateBusListForDestination.add(rs.getString("bus_no"));
					}
				}

				List<String> orderdPerDayBusList = new ArrayList<>();
				List<String> list = new ArrayList<>();
				if (trip.equals("O")) {
					list = busOrderListForOrigin;
				} else if (trip.equals("D")) {
					list = busOrderListForDestination;
				}

				int oi = 0;

				int di = 0;

				for (String bo : list) {

					if (bo.equals("O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						for (int k = oi; k <= rotateBusListForOrgin.size(); k++) {

							orderdPerDayBusList.add(rotateBusListForOrgin.get(oi));
							oi++;

							break;

						}

					}

					else if (bo.equals("SLTB-O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("SLTB-O");
					}

					else if (bo.equals("SLTB-D") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						orderdPerDayBusList.add("SLTB-D");
					}

					/** for fixed pvt bus **/

					else if (bo.contains("O-Y") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {
						orderdPerDayBusList.add(bo);

					}

					else if (bo.contains("D-Y") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {
						orderdPerDayBusList.add(bo);

					}
					/** for fixed pvt bus end **/

					else if (bo.equals("D") && rotateBusListForDestination != null
							&& !rotateBusListForDestination.isEmpty()) {

						for (int k = di; k <= rotateBusListForDestination.size(); k++) {

							orderdPerDayBusList.add(rotateBusListForDestination.get(di));
							di++;

							break;

						}

					}

				}

				for (String op : orderdPerDayBusList) {
					String abbr = null;

					if (dayTwo == 61) {

						if (i == 62) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}

						if (i == 63) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList2.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 64) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList3.add(value);

									;
								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 65) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList4.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 66) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList5.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 67) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList6.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 68) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList7.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 69) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList8.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 70) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList9.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 71) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList10.add(value);

								}
							}
						}
						if (i == 72) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList11.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 73) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList12.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 74) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList13.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 75) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList14.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 76) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList15.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 77) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList16.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 78) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList17.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 79) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList18.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 80) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList19.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 81) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList20.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 82) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList21.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 83) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList22.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 84) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList23.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 85) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList24.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 86) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList25.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 87) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList26.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 88) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList27.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 89) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList28.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 90) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList29.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 91) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList30.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 92) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList31.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
					} else if (dayTwo == 62) {
						if (i == 63) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}

						if (i == 64) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList2.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 65) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList3.add(value);

									;
								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 66) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList4.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 67) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList5.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 68) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList6.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 69) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList7.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 70) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList8.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 71) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList9.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 72) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList10.add(value);

								}
							}
						}
						if (i == 73) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList11.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 74) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList12.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 75) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList13.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);
							}
						}
						if (i == 76) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList14.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 77) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList15.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 78) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList16.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 79) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList17.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 80) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList18.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 81) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList19.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 82) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList20.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 83) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList21.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 84) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList22.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 85) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList23.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 86) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList24.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 87) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList25.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 88) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=? ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList26.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 89) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList27.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 90) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList28.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 91) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList29.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 92) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList30.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}
						if (i == 93) {
							if (op.contains("O-Y") || op.contains("D-Y")) {

								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, (op.trim().substring(3)).trim());
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {

									value = rs1.getString("assigned_bus_no") + "F";
									returnList.add(value);

								}
								ConnectionManager.close(rs1);
								ConnectionManager.close(ps1);

							} else {
								String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
										+ "and  generator_ref_no =? and group_no=?  ";
								ps1 = con.prepareStatement(query1);
								ps1.setString(1, op);
								ps1.setString(2, refNO);
								ps1.setString(3, group);
								// ps1.setString(4, trip);

								rs1 = ps1.executeQuery();
								while (rs1.next()) {
									if (op.replaceAll("[0123456789]", "").equals(originBus)) {
										abbr = "O";
									} else {

										abbr = "D";
									}
									value = rs1.getString("assigned_bus_no") + abbr;

									returnList31.add(value);

								}
								ConnectionManager.close(ps1);
								ConnectionManager.close(rs1);
							}
						}

					}
				}
			}
			String sql = "INSERT INTO public.nt_m_control_sheet\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt = con.prepareStatement(sql);

			Iterator<String> it1 = timeSlotList.iterator();
			Iterator<String> it2 = returnList.iterator();
			Iterator<String> it3 = returnList2.iterator();
			Iterator<String> it4 = returnList3.iterator();
			Iterator<String> it5 = returnList4.iterator();
			Iterator<String> it6 = returnList5.iterator();
			Iterator<String> it7 = returnList6.iterator();
			for (; it1.hasNext();) {
				stmt.setString(1, (String) it2.next());
				stmt.setString(2, (String) it3.next());
				stmt.setString(3, (String) it4.next());
				stmt.setString(4, (String) it5.next());
				stmt.setString(5, (String) it6.next());
				stmt.setString(6, (String) it7.next());
				stmt.setString(7, it1.next());
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}
			/*** update bus numbers according to timeslots **/

			/** end **/
			String sql2 = "INSERT INTO public.nt_m_control_sheet2\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt2 = con.prepareStatement(sql2);

			Iterator<String> it8 = timeSlotList.iterator();
			Iterator<String> it9 = returnList7.iterator();
			Iterator<String> it10 = returnList8.iterator();
			Iterator<String> it11 = returnList9.iterator();
			Iterator<String> it12 = returnList10.iterator();
			Iterator<String> it13 = returnList11.iterator();
			Iterator<String> it14 = returnList12.iterator();
			for (; it8.hasNext() && it9.hasNext();) {
				stmt2.setString(1, (String) it9.next());
				stmt2.setString(2, (String) it10.next());
				stmt2.setString(3, (String) it11.next());
				stmt2.setString(4, (String) it12.next());
				stmt2.setString(5, (String) it13.next());
				stmt2.setString(6, (String) it14.next());
				stmt2.setString(7, it8.next());
				stmt2.addBatch();
				stmt2.executeUpdate();
				con.commit();
			}

			String sql3 = "INSERT INTO public.nt_m_control_sheet3\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt3 = con.prepareStatement(sql3);

			Iterator<String> it15 = timeSlotList.iterator();
			Iterator<String> it16 = returnList13.iterator();
			Iterator<String> it17 = returnList14.iterator();
			Iterator<String> it18 = returnList15.iterator();
			Iterator<String> it19 = returnList16.iterator();
			Iterator<String> it20 = returnList17.iterator();
			Iterator<String> it21 = returnList18.iterator();
			for (; it15.hasNext() && it16.hasNext();) {
				stmt3.setString(1, (String) it16.next());
				stmt3.setString(2, (String) it17.next());
				stmt3.setString(3, (String) it18.next());
				stmt3.setString(4, (String) it19.next());
				stmt3.setString(5, (String) it20.next());
				stmt3.setString(6, (String) it21.next());
				stmt3.setString(7, it15.next());
				stmt3.addBatch();
				stmt3.executeUpdate();
				con.commit();
			}

			String sql4 = "INSERT INTO public.nt_m_control_sheet4\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?);;";

			stmt4 = con.prepareStatement(sql4);

			Iterator<String> it22 = timeSlotList.iterator();
			Iterator<String> it23 = returnList19.iterator();
			Iterator<String> it24 = returnList20.iterator();
			Iterator<String> it25 = returnList21.iterator();
			Iterator<String> it26 = returnList22.iterator();
			Iterator<String> it27 = returnList23.iterator();
			Iterator<String> it28 = returnList24.iterator();
			for (; it22.hasNext() && it23.hasNext();) {
				stmt4.setString(1, (String) it23.next());
				stmt4.setString(2, (String) it24.next());
				stmt4.setString(3, (String) it25.next());
				stmt4.setString(4, (String) it26.next());
				stmt4.setString(5, (String) it27.next());
				stmt4.setString(6, (String) it28.next());
				stmt4.setString(7, it22.next());
				stmt4.addBatch();
				stmt4.executeUpdate();
				con.commit();
			}

			String sql5 = "INSERT INTO public.nt_m_control_sheet5\r\n"
					+ "(col1, col2, col3, col4, col5, col6, time_slot, col7)\r\n" + "VALUES(?, ?, ?, ?, ?, ?, ?,?);;";

			stmt5 = con.prepareStatement(sql5);

			Iterator<String> it29 = timeSlotList.iterator();
			Iterator<String> it30 = returnList25.iterator();
			Iterator<String> it31 = returnList26.iterator();
			Iterator<String> it32 = returnList27.iterator();
			Iterator<String> it33 = returnList28.iterator();
			Iterator<String> it34 = returnList29.iterator();
			Iterator<String> it35 = returnList30.iterator();
			Iterator<String> it36 = returnList31.iterator();

			for (; it29.hasNext();) {
				if (returnList29.isEmpty() && returnList30.isEmpty() && returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, null);
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());
				}

				else if (returnList30.isEmpty() && returnList31.isEmpty()) {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, null);
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else if (returnList31.isEmpty()) {
					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, null);
					stmt5.setString(8, it29.next());

				}

				else {

					stmt5.setString(1, (String) it30.next());
					stmt5.setString(2, (String) it31.next());
					stmt5.setString(3, (String) it32.next());
					stmt5.setString(4, (String) it33.next());
					stmt5.setString(5, (String) it34.next());
					stmt5.setString(6, (String) it35.next());
					stmt5.setString(7, (String) it36.next());
					stmt5.setString(8, it29.next());
				}

				stmt5.addBatch();
				stmt5.executeUpdate();
				con.commit();
			}

			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);

		} catch (

		Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs3);
			ConnectionManager.close(con);
		}

		return map;

	}

	@Override
	public String getStartDateByRefNo(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String time = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  distinct  to_char(TO_DATE (rs_start_date,'dd/MM/yyyy'),'yyyy-MM-dd' ) as rs_start_date FROM public.nt_m_route_schedule_generator \r\n"
					+ "WHERE rs_generator_ref_no =?;";
			ps = con.prepareStatement(query);
			ps.setString(1, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				time = rs.getString("rs_start_date");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return time;
	}

	@Override
	public String getEndDateByRefNo(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String time = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  distinct  to_char(TO_DATE (rs_end_date,'dd/MM/yyyy'),'yyyy-MM-dd' ) as rs_end_date FROM public.nt_m_route_schedule_generator \r\n"
					+ "WHERE rs_generator_ref_no =?;";
			ps = con.prepareStatement(query);
			ps.setString(1, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				time = rs.getString("rs_end_date");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return time;
	}

	@Override
	public Map<String, List> insertLeavesInTable2(String refNO, String trip, String groupNo, String originBus,
			String destinationBus, int dayForMonth, int leaveDate) {
		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt5 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		List<String> leaveBusList1 = new ArrayList<>();
		List<String> leaveBusList2 = new ArrayList<>();
		List<String> leaveBusList3 = new ArrayList<>();
		List<String> leaveBusList4 = new ArrayList<>();
		List<String> leaveBusList5 = new ArrayList<>();
		List<String> leaveBusList6 = new ArrayList<>();
		List<String> leaveBusList7 = new ArrayList<>();
		List<String> leaveBusList8 = new ArrayList<>();
		List<String> leaveBusList9 = new ArrayList<>();
		List<String> leaveBusList10 = new ArrayList<>();
		List<String> leaveBusList11 = new ArrayList<>();
		List<String> leaveBusList12 = new ArrayList<>();
		List<String> leaveBusList13 = new ArrayList<>();
		List<String> leaveBusList14 = new ArrayList<>();
		List<String> leaveBusList15 = new ArrayList<>();
		List<String> leaveBusList16 = new ArrayList<>();
		List<String> leaveBusList17 = new ArrayList<>();
		List<String> leaveBusList18 = new ArrayList<>();
		List<String> leaveBusList19 = new ArrayList<>();
		List<String> leaveBusList20 = new ArrayList<>();
		List<String> leaveBusList21 = new ArrayList<>();
		List<String> leaveBusList22 = new ArrayList<>();
		List<String> leaveBusList23 = new ArrayList<>();
		List<String> leaveBusList24 = new ArrayList<>();
		List<String> leaveBusList25 = new ArrayList<>();
		List<String> leaveBusList26 = new ArrayList<>();
		List<String> leaveBusList27 = new ArrayList<>();
		List<String> leaveBusList28 = new ArrayList<>();
		List<String> leaveBusList29 = new ArrayList<>();
		List<String> leaveBusList30 = new ArrayList<>();
		List<String> leaveBusList31 = new ArrayList<>();

		Map<String, List> map = new HashMap();
		/** leave bus abbreviation selection **/
		try {
			con = ConnectionManager.getConnection();
			String leave_query1 = "select a.bus_no,a.day_no,b.leave_position,a.trip_id\r\n"
					+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
					+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
					+ "where a.generator_ref_no=? \r\n"
					+ "and b.leave_position=a.trip_id  group by a.day_no, a.bus_no,b.leave_position,a.trip_id, a.trip_type order by  a.day_no,b.leave_position,a.trip_type ";

			ps1 = con.prepareStatement(leave_query1);

			ps1.setString(1, refNO);
			// ps1.setString(2, trip);

			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				String abbr = null;

				if (dayForMonth == 31) {

					if (rs1.getString("day_no").equals("32")) {

						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));

						rs2 = ps2.executeQuery();
						while (rs2.next()) {

							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList1.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);

					}
					if (rs1.getString("day_no").equals("33")) {

						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList2.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);

					}
					if (rs1.getString("day_no").equals("34")) {
						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList3.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("35")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList4.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("36")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList5.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("37")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList6.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("38")) {
						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList7.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("39")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList8.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("40")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList9.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("41")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList10.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("42")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList11.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("43")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList12.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("44")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList13.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("45")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList14.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("46")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList15.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("47")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList16.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("48")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList17.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("49")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList18.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("50")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList19.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("51")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList20.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("52")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList21.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("53")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList22.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("54")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList23.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("55")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList24.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("56")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList25.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("57")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList26.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("58")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList27.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("59")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList28.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("60")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList29.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("61")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList30.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (leaveDate == 30) {
						if (rs1.getString("day_no").equals("62")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}
					}

					else if (leaveDate == 31) {
						if (rs1.getString("day_no").equals("62")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}
						if (rs1.getString("day_no").equals("63")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}

					}
				} else if (dayForMonth == 30) {
					if (rs1.getString("day_no").equals("31")) {

						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));

						rs2 = ps2.executeQuery();
						while (rs2.next()) {

							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList1.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);

					}
					if (rs1.getString("day_no").equals("32")) {

						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList2.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);

					}
					if (rs1.getString("day_no").equals("33")) {
						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList3.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("34")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList4.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("35")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList5.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("36")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList6.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("37")) {
						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList7.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("38")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList8.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("39")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList9.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("40")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList10.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("41")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList11.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("42")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList12.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("43")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList13.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("44")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList14.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("45")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList15.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("46")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList16.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("47")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList17.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("48")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList18.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("49")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList19.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("50")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList20.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("51")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList21.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("52")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList22.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("53")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList23.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("54")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList24.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("55")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList25.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("56")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList26.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("57")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList27.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("58")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList28.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("59")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList29.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("60")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList30.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (leaveDate == 30) {
						if (rs1.getString("day_no").equals("61")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}
					}

					else if (leaveDate == 31) {
						if (rs1.getString("day_no").equals("61")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}
						if (rs1.getString("day_no").equals("62")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}

					}

				}
			}
			/** end leave bus abbreviation selection **/
			String sql5 = "INSERT INTO public.nt_m_control_sheet_leavebus\r\n"
					+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, "
					+ "col17, col18, col19, col20, col21, col22, col23, col24, col25, "
					+ "col26, col27, col28, col29, col30, col31)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?);";

			stmt5 = con.prepareStatement(sql5);

			Iterator<String> it1 = leaveBusList1.iterator();
			Iterator<String> it2 = leaveBusList2.iterator();
			Iterator<String> it3 = leaveBusList3.iterator();
			Iterator<String> it4 = leaveBusList4.iterator();
			Iterator<String> it5 = leaveBusList5.iterator();
			Iterator<String> it6 = leaveBusList6.iterator();
			Iterator<String> it7 = leaveBusList7.iterator();
			Iterator<String> it8 = leaveBusList8.iterator();
			Iterator<String> it9 = leaveBusList9.iterator();
			Iterator<String> it10 = leaveBusList10.iterator();

			Iterator<String> it11 = leaveBusList11.iterator();
			Iterator<String> it12 = leaveBusList12.iterator();
			Iterator<String> it13 = leaveBusList13.iterator();
			Iterator<String> it14 = leaveBusList14.iterator();
			Iterator<String> it15 = leaveBusList15.iterator();
			Iterator<String> it16 = leaveBusList16.iterator();
			Iterator<String> it17 = leaveBusList17.iterator();
			Iterator<String> it18 = leaveBusList18.iterator();
			Iterator<String> it19 = leaveBusList19.iterator();
			Iterator<String> it20 = leaveBusList20.iterator();

			Iterator<String> it21 = leaveBusList21.iterator();
			Iterator<String> it22 = leaveBusList22.iterator();
			Iterator<String> it23 = leaveBusList23.iterator();
			Iterator<String> it24 = leaveBusList24.iterator();
			Iterator<String> it25 = leaveBusList25.iterator();
			Iterator<String> it26 = leaveBusList26.iterator();
			Iterator<String> it27 = leaveBusList27.iterator();
			Iterator<String> it28 = leaveBusList28.iterator();
			Iterator<String> it29 = leaveBusList29.iterator();
			Iterator<String> it30 = leaveBusList30.iterator();
			Iterator<String> it31 = leaveBusList31.iterator();

			for (; it1.hasNext();) {
				if (leaveBusList29.isEmpty() && leaveBusList29.isEmpty() && leaveBusList29.isEmpty()) {
					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, null);
					stmt5.setString(30, null);
					stmt5.setString(31, null);

				}

				else if (leaveBusList30.isEmpty() && leaveBusList31.isEmpty()) {

					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, it29.next());
					stmt5.setString(30, null);
					stmt5.setString(31, null);

				}

				else if (leaveBusList31.isEmpty()) {

					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, it29.next());
					stmt5.setString(30, it30.next());
					stmt5.setString(31, null);

				}

				else {

					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, it29.next());
					stmt5.setString(30, it30.next());
					stmt5.setString(31, it31.next());

				}

				stmt5.addBatch();
				stmt5.executeUpdate();
				con.commit();
			}

			/** start insert leave bus **/

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			ConnectionManager.close(ps1);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(rs1);
			ConnectionManager.close(con);
		}
		return map;

	}

	@Override
	public Map<String, List> insertLeavesInTable3(String refNO, String trip, String groupNo, String originBus,
			String destinationBus, int dayForMonth, int leaveDate) {
		Connection con = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt5 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		List<String> leaveBusList1 = new ArrayList<>();
		List<String> leaveBusList2 = new ArrayList<>();
		List<String> leaveBusList3 = new ArrayList<>();
		List<String> leaveBusList4 = new ArrayList<>();
		List<String> leaveBusList5 = new ArrayList<>();
		List<String> leaveBusList6 = new ArrayList<>();
		List<String> leaveBusList7 = new ArrayList<>();
		List<String> leaveBusList8 = new ArrayList<>();
		List<String> leaveBusList9 = new ArrayList<>();
		List<String> leaveBusList10 = new ArrayList<>();
		List<String> leaveBusList11 = new ArrayList<>();
		List<String> leaveBusList12 = new ArrayList<>();
		List<String> leaveBusList13 = new ArrayList<>();
		List<String> leaveBusList14 = new ArrayList<>();
		List<String> leaveBusList15 = new ArrayList<>();
		List<String> leaveBusList16 = new ArrayList<>();
		List<String> leaveBusList17 = new ArrayList<>();
		List<String> leaveBusList18 = new ArrayList<>();
		List<String> leaveBusList19 = new ArrayList<>();
		List<String> leaveBusList20 = new ArrayList<>();
		List<String> leaveBusList21 = new ArrayList<>();
		List<String> leaveBusList22 = new ArrayList<>();
		List<String> leaveBusList23 = new ArrayList<>();
		List<String> leaveBusList24 = new ArrayList<>();
		List<String> leaveBusList25 = new ArrayList<>();
		List<String> leaveBusList26 = new ArrayList<>();
		List<String> leaveBusList27 = new ArrayList<>();
		List<String> leaveBusList28 = new ArrayList<>();
		List<String> leaveBusList29 = new ArrayList<>();
		List<String> leaveBusList30 = new ArrayList<>();
		List<String> leaveBusList31 = new ArrayList<>();

		Map<String, List> map = new HashMap();
		/** leave bus abbreviation selection **/
		try {
			con = ConnectionManager.getConnection();
			String leave_query1 = "select a.bus_no,a.day_no,b.leave_position,a.trip_id\r\n"
					+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
					+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
					+ "where a.generator_ref_no=? \r\n"
					+ "and b.leave_position=a.trip_id  group by a.day_no, a.bus_no,b.leave_position,a.trip_id, a.trip_type order by  a.day_no,b.leave_position,a.trip_type ";

			ps1 = con.prepareStatement(leave_query1);

			ps1.setString(1, refNO);
			// ps1.setString(2, trip);

			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				String abbr = null;

				if (dayForMonth == 61) {

					if (rs1.getString("day_no").equals("62")) {

						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));

						rs2 = ps2.executeQuery();
						while (rs2.next()) {

							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList1.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);

					}
					if (rs1.getString("day_no").equals("63")) {

						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList2.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);

					}
					if (rs1.getString("day_no").equals("64")) {
						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList3.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("65")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList4.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("66")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList5.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("67")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList6.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("68")) {
						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList7.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("69")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList8.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("70")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList9.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("71")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList10.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("72")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList11.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("73")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList12.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("74")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList13.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("75")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList14.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("76")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList15.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("77")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList16.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("78")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList17.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("79")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList18.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("80")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList19.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("81")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList20.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("82")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList21.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("83")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList22.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("84")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList23.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("85")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList24.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("86")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList25.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("87")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList26.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("88")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList27.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("89")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList28.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("90")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList29.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("91")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList30.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (leaveDate == 30) {
						if (rs1.getString("day_no").equals("92")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}
					}

					else if (leaveDate == 31) {
						if (rs1.getString("day_no").equals("92")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}
						if (rs1.getString("day_no").equals("93")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}

					}
				} else if (dayForMonth == 60) {
					if (rs1.getString("day_no").equals("31")) {

						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));

						rs2 = ps2.executeQuery();
						while (rs2.next()) {

							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList1.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);

					}
					if (rs1.getString("day_no").equals("32")) {

						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList2.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);

					}
					if (rs1.getString("day_no").equals("33")) {
						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList3.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("34")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList4.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("35")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList5.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("36")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList6.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("37")) {
						String leave_query2 = "select  distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList7.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("38")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=? and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList8.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("39")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList9.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("40")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList10.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("41")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList11.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("42")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList12.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("43")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList13.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("44")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList14.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("45")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList15.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("46")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList16.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("47")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList17.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("48")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList18.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("49")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList19.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("50")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList20.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("51")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList21.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("52")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList22.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("53")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList23.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("54")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList24.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("55")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();
						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList25.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("56")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList26.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("57")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList27.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("58")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList28.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("59")) {
						String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList29.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (rs1.getString("day_no").equals("60")) {
						String leave_query2 = "select distinct  assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
								+ "where generator_ref_no=? and group_no=?  and bus_num=?";

						ps2 = con.prepareStatement(leave_query2);

						ps2.setString(1, refNO);
						ps2.setString(2, groupNo);
						// ps2.setString(3, trip);
						ps2.setString(3, rs1.getString("bus_no"));
						rs2 = ps2.executeQuery();

						while (rs2.next()) {
							if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
								abbr = "O";
							} else {

								abbr = "D";
							}
							leaveBusList30.add(rs2.getString("assigned_bus_no") + abbr);
						}
						ConnectionManager.close(rs2);
						ConnectionManager.close(ps2);
					}
					if (leaveDate == 30) {
						if (rs1.getString("day_no").equals("61")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}
					}

					else if (leaveDate == 31) {
						if (rs1.getString("day_no").equals("61")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}
						if (rs1.getString("day_no").equals("62")) {
							String leave_query2 = "select distinct assigned_bus_no from public.nt_m_timetable_generator_det \r\n"
									+ "where generator_ref_no=? and group_no=?  and bus_num=?";

							ps2 = con.prepareStatement(leave_query2);

							ps2.setString(1, refNO);
							ps2.setString(2, groupNo);
							// ps2.setString(3, trip);
							ps2.setString(3, rs1.getString("bus_no"));
							rs2 = ps2.executeQuery();

							while (rs2.next()) {
								if (rs1.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								leaveBusList31.add(rs2.getString("assigned_bus_no") + abbr);
							}
							ConnectionManager.close(rs2);
							ConnectionManager.close(ps2);
						}

					}

				}
			}
			/** end leave bus abbreviation selection **/
			String sql5 = "INSERT INTO public.nt_m_control_sheet_leavebus\r\n"
					+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, "
					+ "col17, col18, col19, col20, col21, col22, col23, col24, col25, "
					+ "col26, col27, col28, col29, col30, col31)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?);";

			stmt5 = con.prepareStatement(sql5);

			Iterator<String> it1 = leaveBusList1.iterator();
			Iterator<String> it2 = leaveBusList2.iterator();
			Iterator<String> it3 = leaveBusList3.iterator();
			Iterator<String> it4 = leaveBusList4.iterator();
			Iterator<String> it5 = leaveBusList5.iterator();
			Iterator<String> it6 = leaveBusList6.iterator();
			Iterator<String> it7 = leaveBusList7.iterator();
			Iterator<String> it8 = leaveBusList8.iterator();
			Iterator<String> it9 = leaveBusList9.iterator();
			Iterator<String> it10 = leaveBusList10.iterator();

			Iterator<String> it11 = leaveBusList11.iterator();
			Iterator<String> it12 = leaveBusList12.iterator();
			Iterator<String> it13 = leaveBusList13.iterator();
			Iterator<String> it14 = leaveBusList14.iterator();
			Iterator<String> it15 = leaveBusList15.iterator();
			Iterator<String> it16 = leaveBusList16.iterator();
			Iterator<String> it17 = leaveBusList17.iterator();
			Iterator<String> it18 = leaveBusList18.iterator();
			Iterator<String> it19 = leaveBusList19.iterator();
			Iterator<String> it20 = leaveBusList20.iterator();

			Iterator<String> it21 = leaveBusList21.iterator();
			Iterator<String> it22 = leaveBusList22.iterator();
			Iterator<String> it23 = leaveBusList23.iterator();
			Iterator<String> it24 = leaveBusList24.iterator();
			Iterator<String> it25 = leaveBusList25.iterator();
			Iterator<String> it26 = leaveBusList26.iterator();
			Iterator<String> it27 = leaveBusList27.iterator();
			Iterator<String> it28 = leaveBusList28.iterator();
			Iterator<String> it29 = leaveBusList29.iterator();
			Iterator<String> it30 = leaveBusList30.iterator();
			Iterator<String> it31 = leaveBusList31.iterator();

			for (; it1.hasNext();) {
				if (leaveBusList29.isEmpty() && leaveBusList29.isEmpty() && leaveBusList29.isEmpty()) {
					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, null);
					stmt5.setString(30, null);
					stmt5.setString(31, null);

				}

				else if (leaveBusList30.isEmpty() && leaveBusList31.isEmpty()) {

					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, it29.next());
					stmt5.setString(30, null);
					stmt5.setString(31, null);

				}

				else if (leaveBusList31.isEmpty()) {

					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, it29.next());
					stmt5.setString(30, it30.next());
					stmt5.setString(31, null);

				}

				else {

					stmt5.setString(1, it1.next());
					stmt5.setString(2, it2.next());
					stmt5.setString(3, it3.next());
					stmt5.setString(4, it4.next());
					stmt5.setString(5, it5.next());
					stmt5.setString(6, it6.next());
					stmt5.setString(7, it7.next());
					stmt5.setString(8, it8.next());
					stmt5.setString(9, it9.next());
					stmt5.setString(10, it10.next());

					stmt5.setString(11, it11.next());
					stmt5.setString(12, it12.next());
					stmt5.setString(13, it13.next());
					stmt5.setString(14, it14.next());
					stmt5.setString(15, it15.next());
					stmt5.setString(16, it16.next());
					stmt5.setString(17, it17.next());
					stmt5.setString(18, it18.next());
					stmt5.setString(19, it19.next());
					stmt5.setString(20, it20.next());

					stmt5.setString(21, it21.next());
					stmt5.setString(22, it22.next());
					stmt5.setString(23, it23.next());
					stmt5.setString(24, it24.next());
					stmt5.setString(25, it25.next());
					stmt5.setString(26, it26.next());
					stmt5.setString(27, it27.next());
					stmt5.setString(28, it28.next());
					stmt5.setString(29, it29.next());
					stmt5.setString(30, it30.next());
					stmt5.setString(31, it31.next());

				}

				stmt5.addBatch();
				stmt5.executeUpdate();
				con.commit();
			}

			/** start insert leave bus **/

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			ConnectionManager.close(ps1);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(rs1);
			ConnectionManager.close(con);
		}
		return map;

	}

	@Override
	public List serviceTypeDropDownWithOutNormal() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<LogSheetMaintenanceDTO> returnList = new ArrayList<LogSheetMaintenanceDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select description,code from nt_r_service_types where active='A' and code not in('001')\r\n"
					+ "order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				LogSheetMaintenanceDTO logDTO = new LogSheetMaintenanceDTO();
				logDTO.setServiceCode(rs.getString("code"));
				logDTO.setServiceDescription(rs.getString("description"));

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
	public boolean insertDatesRotations(List<String> list1, List<String> list2, List<String> list3, String month1,
			String month2, String month3) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		boolean insertSuccess = false;

		/** leave bus abbreviation selection **/
		try {
			con = ConnectionManager.getConnection();

			if (!list3.isEmpty()) {
				if (list3.size() < 31) {
					for (int i = 0; i < 31 - list3.size(); i++) {
						list3.add(null);
					}
				}

				String sql3 = "INSERT INTO public.nt_t_owner_sheet_dates\r\n"
						+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, "
						+ "col17, col18, col19, col20, col21, col22, col23, col24, col25, "
						+ "col26, col27, col28, col29, col30, col31,month)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?,?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?);";

				ps3 = con.prepareStatement(sql3);
				for (int i = 0; i < list3.size(); i++) {
					ps3.setString(i + 1, list3.get(i));
				}
				ps3.setString(32, month3);

				int i1 = ps3.executeUpdate();
				if (i1 > 0) {
					insertSuccess = true;
				}
			}

			if (!list2.isEmpty()) {
				if (list2.size() < 31) {
					for (int i = 0; i < 31 - list2.size(); i++) {
						list2.add(null);
					}
				}

				String sql2 = "INSERT INTO public.nt_t_owner_sheet_dates\r\n"
						+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, "
						+ "col17, col18, col19, col20, col21, col22, col23, col24, col25, "
						+ "col26, col27, col28, col29, col30, col31,month)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?,?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?);";

				ps2 = con.prepareStatement(sql2);
				for (int i = 0; i < list2.size(); i++) {
					ps2.setString(i + 1, list2.get(i));
				}
				ps2.setString(32, month2);

				int i2 = ps2.executeUpdate();
				if (i2 > 0) {
					insertSuccess = true;
				}
			}

			if (!list1.isEmpty()) {
				if (list1.size() < 31) {
					for (int i = 0; i < 31 - list1.size(); i++) {
						list1.add(null);
					}

				}

				String sql = "INSERT INTO public.nt_t_owner_sheet_dates\r\n"
						+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, "
						+ "col17, col18, col19, col20, col21, col22, col23, col24, col25, "
						+ "col26, col27, col28, col29, col30, col31,month)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?,?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?);";

				ps = con.prepareStatement(sql);
				for (int i = 0; i < list1.size(); i++) {
					ps.setString(i + 1, list1.get(i));
				}
				ps.setString(32, month1);

				int i3 = ps.executeUpdate();
				if (i3 > 0) {
					insertSuccess = true;
				}
			}

			con.commit();

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(rs1);
			ConnectionManager.close(con);
		}
		return insertSuccess;

	}

	@Override
	public Map<String, List> getBusNOListNewWithLeaveForOwnerSheer(String refNO, String trip, String group,
			List<String> timeSlotList, List<String> busOrderListForOrigin, List<String> busOrderListForDestination,
			String originBus, String destinationBus, int dayOne, List<String> endTImelist, boolean isHistory,
			String selectedBusAbbriviation) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement ps3 = null;

		ResultSet rs3 = null;

		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String, List> map = new HashMap();

		String value = "";
		String selectedBusAbbriviationNew = selectedBusAbbriviation + '%';
		List<String> returnList = new ArrayList<String>();
		List<String> returnList2 = new ArrayList<String>();
		List<String> returnList3 = new ArrayList<String>();
		List<String> returnList4 = new ArrayList<String>();
		List<String> returnList5 = new ArrayList<String>();
		List<String> returnList6 = new ArrayList<String>();
		List<String> returnList7 = new ArrayList<String>();
		List<String> returnList8 = new ArrayList<String>();
		List<String> returnList9 = new ArrayList<String>();
		List<String> returnList10 = new ArrayList<String>();
		List<String> returnList11 = new ArrayList<String>();
		List<String> returnList12 = new ArrayList<String>();
		List<String> returnList13 = new ArrayList<String>();
		List<String> returnList14 = new ArrayList<String>();
		List<String> returnList15 = new ArrayList<String>();
		List<String> returnList16 = new ArrayList<String>();
		List<String> returnList17 = new ArrayList<String>();
		List<String> returnList18 = new ArrayList<String>();
		List<String> returnList19 = new ArrayList<String>();
		List<String> returnList20 = new ArrayList<String>();
		List<String> returnList21 = new ArrayList<String>();
		List<String> returnList22 = new ArrayList<String>();
		List<String> returnList23 = new ArrayList<String>();
		List<String> returnList24 = new ArrayList<String>();
		List<String> returnList25 = new ArrayList<String>();
		List<String> returnList26 = new ArrayList<String>();
		List<String> returnList27 = new ArrayList<String>();
		List<String> returnList28 = new ArrayList<String>();
		List<String> returnList29 = new ArrayList<String>();
		List<String> returnList30 = new ArrayList<String>();
		List<String> returnList31 = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			boolean multiCouple = false;
			String coupleQuery = "\r\n" + "SELECT no_of_couples_per_one_bus FROM public.nt_m_timetable_generator \r\n"
					+ "WHERE generator_ref_no =?  and  trip_type =? and group_no =?;";

			ps3 = con.prepareStatement(coupleQuery);
			ps3.setString(1, refNO);
			ps3.setString(2, trip);
			ps3.setString(3, group);
			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				multiCouple = true;
			}

			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);

			for (int i = 1; i <= dayOne; i++) {
				List<String> rotateBusListForOrgin = new ArrayList<>();
				List<String> rotateBusListForDestination = new ArrayList<>();

				if (multiCouple) {
					if (isHistory) {
						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type  \r\n"
								+ "from public.nt_h_route_schedule_generator_det01 a \r\n"
								+ "inner join public.nt_h_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no  and a.mas_seq =b.rs_seq \r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no \r\n"
								+ "where b.rs_generator_ref_no=?  and b.rs_trip_type='O' \r\n"
								+ "and b.rs_group_no=? and bus_no like" + "'" + selectedBusAbbriviationNew + "' "
								+ " and a.day_no=? and c.trip_type='O' \r\n" + "and a.bus_no not in \r\n"
								+ "(select a.bus_no \r\n" + "from public.nt_h_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
								+ "where a.generator_ref_no= ?\r\n" + "and b.leave_position=a.trip_id  \r\n"
								+ "and a.day_no =?) order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setInt(7, i);
					}

					else {
						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "where b.rs_generator_ref_no=? and b.rs_trip_type=?\r\n"
								+ "and b.rs_group_no=? and a.day_no=? and bus_no like" + "'"
								+ selectedBusAbbriviationNew + "' " + " and c.trip_type=?\r\n"
								+ "and a.bus_no not in \r\n" + "(select a.bus_no\r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 b on (b.generator_ref_no=a.generator_ref_no and b.trip_type=a.trip_type)\r\n"
								+ "where a.generator_ref_no= ?\r\n" + "and b.leave_position=a.trip_id  \r\n"
								+ "and a.day_no =?) order by a.seq;;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setInt(7, i);
					}

				}

				else {

					if (isHistory) {

						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "from public.nt_h_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_h_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no and a.mas_seq =b.rs_seq \r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "where b.rs_generator_ref_no='' and b.rs_trip_type=?\r\n"
								+ "and b.rs_group_no=? and a.day_no=? and c.trip_type=?\r\n"
								+ "and a.trip_id not in \r\n"
								+ "(select distinct c.leave_position from  public.nt_t_route_schedule_generator_det02 c\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det01 x on x.generator_ref_no=?\r\n"
								+ "where c.generator_ref_no=? and c.trip_type=a.trip_type) and bus_no like" + "'"
								+ selectedBusAbbriviationNew + "' " + "order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setString(7, refNO);

					}

					else {

						String queryBus = "select distinct a.seq,bus_no,day_no,a.trip_id,a.trip_type \r\n"
								+ "from public.nt_t_route_schedule_generator_det01 a\r\n"
								+ "inner join public.nt_m_route_schedule_generator b on b.rs_generator_ref_no=a.generator_ref_no\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det02 c on c.generator_ref_no=a.generator_ref_no\r\n"
								+ "where b.rs_generator_ref_no=?\r\n" + "and b.rs_trip_type=?\r\n"
								+ "and b.rs_group_no=?\r\n" + "and a.day_no=?\r\n" + "and c.trip_type=?\r\n"
								+ "and a.trip_id\r\n" + "not in \r\n"
								+ "(select distinct c.leave_position from  public.nt_t_route_schedule_generator_det02 c\r\n"
								+ "inner join public.nt_t_route_schedule_generator_det01 x on x.generator_ref_no=?\r\n"
								+ "where c.generator_ref_no=? and c.trip_type=a.trip_type) and bus_no like" + "'"
								+ selectedBusAbbriviationNew + "' " + "order by a.seq;";

						ps = con.prepareStatement(queryBus);
						ps.setString(1, refNO);
						ps.setString(2, trip);
						ps.setString(3, group);
						ps.setInt(4, i);
						ps.setString(5, trip);
						ps.setString(6, refNO);
						ps.setString(7, refNO);
					}

				}
				rs = ps.executeQuery();

				while (rs.next()) {
					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(originBus)) {
						rotateBusListForOrgin.add(rs.getString("bus_no"));
					}
					if (rs.getString("bus_no").replaceAll("[0123456789]", "").equals(destinationBus)) {
						rotateBusListForDestination.add(rs.getString("bus_no"));
					}
				}

				List<String> orderdPerDayBusList = new ArrayList<>();
				List<String> list = new ArrayList<>();
				if (trip.equals("O")) {
					list = busOrderListForOrigin;
				} else if (trip.equals("D")) {
					list = busOrderListForDestination;
				}

				int oi = 0;

				int di = 0;

				for (String bo : list) {

					if (bo.equals("O") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {

						for (int k = oi; k <= rotateBusListForOrgin.size(); k++) {

							orderdPerDayBusList.add(rotateBusListForOrgin.get(oi));
							oi++;

							break;

						}

					}

					else if (bo.equals("D") && rotateBusListForDestination != null
							&& !rotateBusListForDestination.isEmpty()) {

						for (int k = di; k <= rotateBusListForDestination.size(); k++) {

							orderdPerDayBusList.add(rotateBusListForDestination.get(di));
							di++;

							break;

						}

					}

					/** for fixed pvt bus **/

					else if (bo.contains("O-Y") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {
						orderdPerDayBusList.add(bo);

					}

					else if (bo.contains("D-Y") && rotateBusListForOrgin != null && !rotateBusListForOrgin.isEmpty()) {
						orderdPerDayBusList.add(bo);

					}
					/** for fixed pvt bus end **/

				}

				for (String op : orderdPerDayBusList) {
					String abbr = null;
					if (i == 1) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;
								returnList.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}

					if (i == 2) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList2.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList2.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 3) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList3.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList3.add(value);

								;
							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 4) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList4.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList4.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 5) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList5.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList5.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 6) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList6.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList6.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 7) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList7.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList7.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 8) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList8.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList8.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 9) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList9.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList9.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 10) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList10.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList10.add(value);

							}
						}
					}
					if (i == 11) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList11.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList11.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 12) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList12.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList12.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 13) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList13.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList13.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);
						}
					}
					if (i == 14) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList14.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList14.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 15) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList15.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList15.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 16) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList16.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList16.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 17) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList17.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList17.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 18) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList18.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList18.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 19) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList19.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList19.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 20) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList20.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList20.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 21) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList21.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList21.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 22) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList22.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList22.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 23) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList23.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList23.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 24) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList24.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList24.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 25) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList25.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList25.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 26) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList26.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=? ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList26.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 27) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList27.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList27.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 28) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList28.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList28.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 29) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList29.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList29.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 30) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList30.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList30.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
					if (i == 31) {
						if (op.contains("O-Y") || op.contains("D-Y")) {

							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, (op.trim().substring(3)).trim());
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {

								value = rs1.getString("assigned_bus_no") + "F";
								returnList31.add(value);

							}
							ConnectionManager.close(rs1);
							ConnectionManager.close(ps1);

						} else {
							String query1 = "select  distinct assigned_bus_no from   public.nt_m_timetable_generator_det where bus_num=? \r\n"
									+ "and  generator_ref_no =? and group_no=?  ";
							ps1 = con.prepareStatement(query1);
							ps1.setString(1, op);
							ps1.setString(2, refNO);
							ps1.setString(3, group);
							// ps1.setString(4, trip);

							rs1 = ps1.executeQuery();
							while (rs1.next()) {
								if (op.replaceAll("[0123456789]", "").equals(originBus)
										|| op.equalsIgnoreCase("SLTB-O")) {
									abbr = "O";
								} else {

									abbr = "D";
								}
								value = rs1.getString("assigned_bus_no") + abbr;

								returnList31.add(value);

							}
							ConnectionManager.close(ps1);
							ConnectionManager.close(rs1);
						}
					}
				}
			}
			String sql = "INSERT INTO public.nt_t_owner_sheet_buses\r\n"
					+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, col17, col18, col19, col20, col21, col22, col23, col24, col25, col26, col27, col28, col29, col30, col31, time_slot, time_slot_depart)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\r\n"
					+ "";

			stmt = con.prepareStatement(sql);

			Iterator<String> it1 = timeSlotList.iterator();
			Iterator<String> it2 = returnList.iterator();
			Iterator<String> it3 = returnList2.iterator();
			Iterator<String> it4 = returnList3.iterator();
			Iterator<String> it5 = returnList4.iterator();
			Iterator<String> it6 = returnList5.iterator();
			Iterator<String> it7 = returnList6.iterator();

			// Iterator<String> it8 = timeSlotList.iterator();
			Iterator<String> it9 = returnList7.iterator();
			Iterator<String> it10 = returnList8.iterator();
			Iterator<String> it11 = returnList9.iterator();
			Iterator<String> it12 = returnList10.iterator();
			Iterator<String> it13 = returnList11.iterator();
			Iterator<String> it14 = returnList12.iterator();

			// Iterator<String> it15 = timeSlotList.iterator();
			Iterator<String> it16 = returnList13.iterator();
			Iterator<String> it17 = returnList14.iterator();
			Iterator<String> it18 = returnList15.iterator();
			Iterator<String> it19 = returnList16.iterator();
			Iterator<String> it20 = returnList17.iterator();
			Iterator<String> it21 = returnList18.iterator();

			// Iterator<String> it22 = timeSlotList.iterator();
			Iterator<String> it23 = returnList19.iterator();
			Iterator<String> it24 = returnList20.iterator();
			Iterator<String> it25 = returnList21.iterator();
			Iterator<String> it26 = returnList22.iterator();
			Iterator<String> it27 = returnList23.iterator();
			Iterator<String> it28 = returnList24.iterator();

			// Iterator<String> it29 = timeSlotList.iterator();
			Iterator<String> it30 = returnList25.iterator();
			Iterator<String> it31 = returnList26.iterator();
			Iterator<String> it32 = returnList27.iterator();
			Iterator<String> it33 = returnList28.iterator();
			Iterator<String> it34 = returnList29.iterator();
			Iterator<String> it35 = returnList30.iterator();
			Iterator<String> it36 = returnList31.iterator();
			Iterator<String> it37 = endTImelist.iterator();

			for (; it1.hasNext();) {
				stmt.setString(1, (String) it2.next());
				stmt.setString(2, (String) it3.next());
				stmt.setString(3, (String) it4.next());
				stmt.setString(4, (String) it5.next());
				stmt.setString(5, (String) it6.next());
				stmt.setString(6, (String) it7.next());
				// stmt.setString(7, it1.next());

				stmt.setString(7, (String) it9.next());
				stmt.setString(8, (String) it10.next());
				stmt.setString(9, (String) it11.next());
				stmt.setString(10, (String) it12.next());
				stmt.setString(11, (String) it13.next());
				stmt.setString(12, (String) it14.next());
				// stmt.setString(7, it8.next());

				stmt.setString(13, (String) it16.next());
				stmt.setString(14, (String) it17.next());
				stmt.setString(15, (String) it18.next());
				stmt.setString(16, (String) it19.next());
				stmt.setString(17, (String) it20.next());
				stmt.setString(18, (String) it21.next());
				// stmt3.setString(7, it15.next());

				stmt.setString(19, (String) it23.next());
				stmt.setString(20, (String) it24.next());
				stmt.setString(21, (String) it25.next());
				stmt.setString(22, (String) it26.next());
				stmt.setString(23, (String) it27.next());
				stmt.setString(24, (String) it28.next());
				// stmt4.setString(7, it22.next());

				// for (; it1.hasNext();) {
				if (returnList29.isEmpty() && returnList30.isEmpty() && returnList31.isEmpty()) {
					stmt.setString(25, (String) it30.next());
					stmt.setString(26, (String) it31.next());
					stmt.setString(27, (String) it32.next());
					stmt.setString(28, (String) it33.next());
					stmt.setString(29, null);
					stmt.setString(30, null);
					stmt.setString(31, null);
					stmt.setString(32, it1.next());
					stmt.setString(33, it37.next());
				}

				else if (returnList30.isEmpty() && returnList31.isEmpty()) {

					stmt.setString(25, (String) it30.next());
					stmt.setString(26, (String) it31.next());
					stmt.setString(27, (String) it32.next());
					stmt.setString(28, (String) it33.next());
					stmt.setString(29, (String) it34.next());
					stmt.setString(30, null);
					stmt.setString(31, null);
					stmt.setString(32, it1.next());
					stmt.setString(33, it37.next());

				}

				else if (returnList31.isEmpty()) {
					stmt.setString(25, (String) it30.next());
					stmt.setString(26, (String) it31.next());
					stmt.setString(27, (String) it32.next());
					stmt.setString(28, (String) it33.next());
					stmt.setString(29, (String) it34.next());
					stmt.setString(30, (String) it35.next());
					stmt.setString(31, null);
					stmt.setString(32, it1.next());
					stmt.setString(33, it37.next());

				}

				else {

					stmt.setString(25, (String) it30.next());
					stmt.setString(26, (String) it31.next());
					stmt.setString(27, (String) it32.next());
					stmt.setString(28, (String) it33.next());
					stmt.setString(29, (String) it34.next());
					stmt.setString(30, (String) it35.next());
					stmt.setString(31, (String) it36.next());
					stmt.setString(32, it1.next());
					stmt.setString(33, it37.next());
				}
				// }
				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}

			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);

		} catch (

		Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs3);
			ConnectionManager.close(con);
		}

		return map;

	}

	@Override
	public Map<String, List> getBusNOListNewForOwnerSheet(String refNO, String trip, String group,
			List<String> timeSlotList, List<String> busOrderListForOrigin, List<String> busOrderListForDestination,
			String originBus, String destinationBus, int dayOne, List<String> endTImelist, boolean isHistory,
			String selectedBusAbbriviation, boolean busesMoreThanThirty) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;
		PreparedStatement stmt6 = null;
		PreparedStatement ps3 = null;

		ResultSet rs3 = null;

		ResultSet rs = null;
		ResultSet rs1 = null;
		Map<String, List> map = new HashMap();

		String value = "";
		String selectedBusAbbriviationNew = selectedBusAbbriviation + '%';
		List<String> returnList1 = new ArrayList<String>();
		List<String> returnList2 = new ArrayList<String>();
		List<String> returnList3 = new ArrayList<String>();
		List<String> returnList4 = new ArrayList<String>();
		List<String> returnList5 = new ArrayList<String>();
		List<String> returnList6 = new ArrayList<String>();
		List<String> returnList7 = new ArrayList<String>();
		List<String> returnList8 = new ArrayList<String>();
		List<String> returnList9 = new ArrayList<String>();
		List<String> returnList10 = new ArrayList<String>();
		List<String> returnList11 = new ArrayList<String>();
		List<String> returnList12 = new ArrayList<String>();
		List<String> returnList13 = new ArrayList<String>();
		List<String> returnList14 = new ArrayList<String>();
		List<String> returnList15 = new ArrayList<String>();
		List<String> returnList16 = new ArrayList<String>();
		List<String> returnList17 = new ArrayList<String>();
		List<String> returnList18 = new ArrayList<String>();
		List<String> returnList19 = new ArrayList<String>();
		List<String> returnList20 = new ArrayList<String>();
		List<String> returnList21 = new ArrayList<String>();
		List<String> returnList22 = new ArrayList<String>();
		List<String> returnList23 = new ArrayList<String>();
		List<String> returnList24 = new ArrayList<String>();
		List<String> returnList25 = new ArrayList<String>();
		List<String> returnList26 = new ArrayList<String>();
		List<String> returnList27 = new ArrayList<String>();
		List<String> returnList28 = new ArrayList<String>();
		List<String> returnList29 = new ArrayList<String>();
		List<String> returnList30 = new ArrayList<String>();
		List<String> returnList31 = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			boolean withLeave = false;
			String leaveQuery = "select * from  public.nt_t_route_schedule_generator_det02 where generator_ref_no=? and trip_type=?;";

			ps3 = con.prepareStatement(leaveQuery);
			ps3.setString(1, refNO);
			ps3.setString(2, trip);

			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				withLeave = true;
			}

			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);

			for (int i = 1; i <= dayOne; i++) {
				List<String> rotateBusListForOrgin = new ArrayList<>();
				List<String> rotateBusListForDestination = new ArrayList<>();

				if (isHistory) {
					// and b.rs_trip_type=?
					String queryBus = "select  a.bus_no,a.trip_type  from public.nt_h_route_schedule_generator_det01  a "
							+ "inner join public.nt_h_route_schedule_generator b "
							+ "on b.rs_generator_ref_no=a.generator_ref_no and b.rs_seq =a.mas_seq where b.rs_generator_ref_no=? "
							+ "and b.rs_group_no=? and a.day_no=?";

					ps = con.prepareStatement(queryBus);
					ps.setString(1, refNO);
					// ps.setString(2, trip);
					ps.setString(2, group);
					ps.setInt(3, i);
				} else {
					String queryBus = "select  a.bus_no,a.trip_type "
							+ "from public.nt_t_route_schedule_generator_det01 a "
							+ "inner join public.nt_m_route_schedule_generator b "
							+ "on b.rs_generator_ref_no=a.generator_ref_no "
							+ "where b.rs_generator_ref_no=? "
							+ "and b.rs_trip_type=? "
							+ "and b.rs_group_no=? and a.day_no=? ORDER BY CASE WHEN a.trip_type = ? THEN 0 ELSE 1 end";

					ps = con.prepareStatement(queryBus);
					ps.setString(1, refNO);
					ps.setString(2, trip);
					ps.setString(3, group);
					ps.setInt(4, i);
					ps.setString(5, trip);

				}
				rs = ps.executeQuery();

				while (rs.next()) {

					if (trip.equals("O")) {
						rotateBusListForOrgin.add(rs.getString("bus_no")+rs.getString("trip_type"));
					}
					if (trip.equals("D")) {
						rotateBusListForDestination.add(rs.getString("bus_no")+rs.getString("trip_type"));
					}
				}

				List<String> orderdPerDayBusList = new ArrayList<>();
				List<String> list = new ArrayList<>();
				if (trip.equals("O")) {
					list = rotateBusListForOrgin;
				} else if (trip.equals("D")) {
					list = rotateBusListForDestination;
				}


				for (String bo : list) {
					orderdPerDayBusList.add(bo);
				}

				for (String op : orderdPerDayBusList) {
					switch (i) {
				    case 1:
				        returnList1.add(op);
				        break;
				    case 2:
				        returnList2.add(op);
				        break;
				    case 3:
				        returnList3.add(op);
				        break;
				    case 4:
				        returnList4.add(op);
				        break;
				    case 5:
				        returnList5.add(op);
				        break;
				    case 6:
				        returnList6.add(op);
				        break;
				    case 7:
				        returnList7.add(op);
				        break;
				    case 8:
				        returnList8.add(op);
				        break;
				    case 9:
				        returnList9.add(op);
				        break;
				    case 10:
				        returnList10.add(op);
				        break;
				    case 11:
				        returnList11.add(op);
				        break;
				    case 12:
				        returnList12.add(op);
				        break;
				    case 13:
				        returnList13.add(op);
				        break;
				    case 14:
				        returnList14.add(op);
				        break;
				    case 15:
				        returnList15.add(op);
				        break;
				    case 16:
				        returnList16.add(op);
				        break;
				    case 17:
				        returnList17.add(op);
				        break;
				    case 18:
				        returnList18.add(op);
				        break;
				    case 19:
				        returnList19.add(op);
				        break;
				    case 20:
				        returnList20.add(op);
				        break;
				    case 21:
				        returnList21.add(op);
				        break;
				    case 22:
				        returnList22.add(op);
				        break;
				    case 23:
				        returnList23.add(op);
				        break;
				    case 24:
				        returnList24.add(op);
				        break;
				    case 25:
				        returnList25.add(op);
				        break;
				    case 26:
				        returnList26.add(op);
				        break;
				    case 27:
				        returnList27.add(op);
				        break;
				    case 28:
				        returnList28.add(op);
				        break;
				    case 29:
				        returnList29.add(op);
				        break;
				    case 30:
				        returnList30.add(op);
				        break;
				    case 31:
				        returnList31.add(op);
				        break;
				    default:
				       
				        break;
				}

				}
			}

			String sql = "INSERT INTO public.nt_t_owner_sheet_buses\r\n"
					+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, col17, col18, col19, col20, col21, col22, col23, col24, col25, col26, col27, col28, col29, col30, col31, time_slot, time_slot_depart)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\r\n"
					+ "";

			stmt = con.prepareStatement(sql);

			Iterator<String> it1 = timeSlotList.iterator();
			Iterator<String> it2 = returnList1.iterator();
			Iterator<String> it3 = returnList2.iterator();
			Iterator<String> it4 = returnList3.iterator();
			Iterator<String> it5 = returnList4.iterator();
			Iterator<String> it6 = returnList5.iterator();
			Iterator<String> it7 = returnList6.iterator();

			Iterator<String> it9 = returnList7.iterator();
			Iterator<String> it10 = returnList8.iterator();
			Iterator<String> it11 = returnList9.iterator();
			Iterator<String> it12 = returnList10.iterator();
			Iterator<String> it13 = returnList11.iterator();
			Iterator<String> it14 = returnList12.iterator();

			Iterator<String> it16 = returnList13.iterator();
			Iterator<String> it17 = returnList14.iterator();
			Iterator<String> it18 = returnList15.iterator();
			Iterator<String> it19 = returnList16.iterator();
			Iterator<String> it20 = returnList17.iterator();
			Iterator<String> it21 = returnList18.iterator();

			Iterator<String> it23 = returnList19.iterator();
			Iterator<String> it24 = returnList20.iterator();
			Iterator<String> it25 = returnList21.iterator();
			Iterator<String> it26 = returnList22.iterator();
			Iterator<String> it27 = returnList23.iterator();
			Iterator<String> it28 = returnList24.iterator();

			Iterator<String> it30 = returnList25.iterator();
			Iterator<String> it31 = returnList26.iterator();
			Iterator<String> it32 = returnList27.iterator();
			Iterator<String> it33 = returnList28.iterator();
			Iterator<String> it34 = returnList29.iterator();
			Iterator<String> it35 = returnList30.iterator();
			Iterator<String> it36 = returnList31.iterator();
			Iterator<String> it37 = endTImelist.iterator();

			for (; it1.hasNext();) {
				stmt.setString(1, (String) it2.next());
				stmt.setString(2, (String) it3.next());
				stmt.setString(3, (String) it4.next());
				stmt.setString(4, (String) it5.next());
				stmt.setString(5, (String) it6.next());
				stmt.setString(6, (String) it7.next());
			

				stmt.setString(7, (String) it9.next());
				stmt.setString(8, (String) it10.next());
				stmt.setString(9, (String) it11.next());
				stmt.setString(10, (String) it12.next());
				stmt.setString(11, (String) it13.next());
				stmt.setString(12, (String) it14.next());
			

				stmt.setString(13, (String) it16.next());
				stmt.setString(14, (String) it17.next());
				stmt.setString(15, (String) it18.next());
				stmt.setString(16, (String) it19.next());
				stmt.setString(17, (String) it20.next());
				stmt.setString(18, (String) it21.next());
			

				stmt.setString(19, (String) it23.next());
				stmt.setString(20, (String) it24.next());
				stmt.setString(21, (String) it25.next());
				stmt.setString(22, (String) it26.next());
				stmt.setString(23, (String) it27.next());
				stmt.setString(24, (String) it28.next());
				

				if (returnList29.isEmpty() && returnList30.isEmpty() && returnList31.isEmpty()) {
					stmt.setString(25, (String) it30.next());
					stmt.setString(26, (String) it31.next());
					stmt.setString(27, (String) it32.next());
					stmt.setString(28, (String) it33.next());
					stmt.setString(29, null);
					stmt.setString(30, null);
					stmt.setString(31, null);
					stmt.setString(32, it1.next());
					stmt.setString(33, it37.next());
				}

				else if (returnList30.isEmpty() && returnList31.isEmpty()) {

					stmt.setString(25, (String) it30.next());
					stmt.setString(26, (String) it31.next());
					stmt.setString(27, (String) it32.next());
					stmt.setString(28, (String) it33.next());
					stmt.setString(29, (String) it34.next());
					stmt.setString(30, null);
					stmt.setString(31, null);
					stmt.setString(32, it1.next());
					stmt.setString(33, it37.next());

				}

				else if (returnList31.isEmpty()) {
					stmt.setString(25, (String) it30.next());
					stmt.setString(26, (String) it31.next());
					stmt.setString(27, (String) it32.next());
					stmt.setString(28, (String) it33.next());
					stmt.setString(29, (String) it34.next());
					stmt.setString(30, (String) it35.next());
					stmt.setString(31, null);
					stmt.setString(32, it1.next());
					stmt.setString(33, it37.next());

				}

				else {

					stmt.setString(25, (String) it30.next());
					stmt.setString(26, (String) it31.next());
					stmt.setString(27, (String) it32.next());
					stmt.setString(28, (String) it33.next());
					stmt.setString(29, (String) it34.next());
					stmt.setString(30, (String) it35.next());
					stmt.setString(31, (String) it36.next());
					stmt.setString(32, it1.next());
					stmt.setString(33, it37.next());
				}

				stmt.addBatch();
				stmt.executeUpdate();
				con.commit();
			}

			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
		
		} catch (

		Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt5);
			ConnectionManager.close(stmt6);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs1);
			ConnectionManager.close(rs3);
			ConnectionManager.close(con);
		}

		return map;

	}

	@Override
	public void truncTableInOwnersheetGenerate() {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		PreparedStatement ps7 = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "truncate table  public.nt_t_owner_sheet_dates;";

			ps = con.prepareStatement(query);

			ps.executeUpdate();

			ConnectionManager.close(ps);

			String query2 = "truncate table  public.nt_t_owner_sheet_buses;";

			ps2 = con.prepareStatement(query2);

			ps2.executeUpdate();

			ConnectionManager.close(ps2);

			String query6 = "DELETE FROM  nt_m_control_sheet_leavebus;";

			ps6 = con.prepareStatement(query6);

			ps6.executeUpdate();

			String query7 = "DELETE FROM  public.nt_t_owner_sheet_dates2;";

			ps7 = con.prepareStatement(query7);

			ps7.executeUpdate();

			ConnectionManager.close(ps7);

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(ps5);
			ConnectionManager.close(ps5);
			ConnectionManager.close(ps6);
			ConnectionManager.close(ps7);
			ConnectionManager.close(con);
		}

	}

	@Override
	public List<String> getEndTimeSlots(String tripType, String groupNo, String route, String serviceType, String refNo,
			String busAbbriviation) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();
		String query = null;
		String busAbbriviationNew = null;
		if (busAbbriviation != null) {
			busAbbriviationNew = busAbbriviation + "%";
		}

		try {
			con = ConnectionManager.getConnection();
			
				if (tripType.equalsIgnoreCase("O")) {
					query = "select a.end_time from public.panel_generator_origin_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? and b.routeno = ? and b.buscategory = ? order by a.start_time  asc";
				} else if (tripType.equalsIgnoreCase("D")) {
					query = "select a.end_time from public.panel_generator_destination_trip_details a inner join public.panel_generator_general_data b on b.refno = a.ref_no where a.draftdata = 'S' and (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%') and b.refno = ?  and b.groupcount = ? and b.routeno = ? and b.buscategory = ? order by a.start_time  asc";
				}
			

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, route);
			ps.setString(4, serviceType);

			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("end_time");
				returnList.add(value);
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
	public List<String> getStartMonthList(String refNo, String tripType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select rs_start_date , rs_end_date from public.nt_m_route_schedule_generator "
					+ "where rs_generator_ref_no =? and rs_trip_type =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {

				value = rs.getString("rs_start_date") + " - " + rs.getString("rs_end_date");
				returnList.add(value);

			}

			String query2 = "select rs_start_date , rs_end_date from public.nt_h_route_schedule_generator "
					+ "where rs_generator_ref_no =? and rs_trip_type =? ";

			ps2 = con.prepareStatement(query2);
			ps2.setString(1, refNo);
			ps2.setString(2, tripType);
			rs2 = ps2.executeQuery();

			while (rs2.next()) {

				value = rs2.getString("rs_start_date") + " - " + rs2.getString("rs_end_date");
				returnList.add(value);

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

		return returnList;

	}

	@Override
	public boolean getselectedDateRangeFromHistory(String refNo, String tripType, String dateRange) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		boolean historyOne = false;
		String startDate = dateRange.trim().substring(0, 10);
		// String endDate = dateRange.trim().substring(11,21) ;

		String endDate = dateRange.trim().substring(dateRange.lastIndexOf("-") + 1);

		try {
			con = ConnectionManager.getConnection();

			String query = "select rs_start_date , rs_end_date from public.nt_m_route_schedule_generator "
					+ "where rs_generator_ref_no =? and rs_trip_type =? ";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getString("rs_start_date").equals(startDate.trim())
						&& rs.getString("rs_end_date").equals(endDate.trim())) {

					historyOne = false;
				}

			}

			String query2 = "select rs_start_date , rs_end_date from public.nt_h_route_schedule_generator "
					+ "where rs_generator_ref_no =? and rs_trip_type =? ";

			ps2 = con.prepareStatement(query2);
			ps2.setString(1, refNo);
			ps2.setString(2, tripType);
			rs2 = ps2.executeQuery();

			while (rs2.next()) {

				if (rs2.getString("rs_start_date").equals(startDate.trim())
						&& rs2.getString("rs_end_date").equals(endDate.trim())) {

					historyOne = true;
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

		return historyOne;

	}

	@Override
	public List<String> getStagesNameListFromTempTable(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> stagesList = new ArrayList<>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT stage0, stage1, stage2, stage3, stage4, stage5, stage6, stage7, stage8, stage9, stage10, stage11, stage12, stage13,\r\n"
					+ "stage14, stage15, stage16, stage17, stage18, stage19, stage20, stage21, stage22, stage23, stage24, stage25, stage26, stage27,\r\n"
					+ "stage28, stage29, stage30, stage31, stage32, stage33, stage34, stage35, stage36, stage37, stage38, stage39, stage40\r\n"
					+ "FROM public.nt_temp_stages_amount limit 1;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				stagesList = new ArrayList<>();

				for (int i = 1; i < 42; i++) {
					stagesList.add(rs.getString(i));

				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return stagesList;
	}

	@Override
	public int getNumberOfBuses(String refNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query = "select count(assigned_bus_no) as busCount from public.nt_m_timetable_generator_det\r\n"
					+ "where generator_ref_no =? and  trip_type =? and assigned_bus_no  is not null and start_time_slot  is not null and assigned_bus_no not in('SLTB-O','SLTB-D') and is_fixed_time ='N';";
			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("busCount");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return count;
	}

	@Override
	public boolean insertDatesRotationsNew(ArrayList<ArrayList<Integer>> arrayOne,
			ArrayList<ArrayList<Integer>> arrayTwo, ArrayList<ArrayList<Integer>> arrayThree, String month1,
			String month2, String month3) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		boolean insertSuccess = false;

		/** leave bus abbreviation selection **/
		try {
			con = ConnectionManager.getConnection();
			if (!arrayThree.isEmpty()) {

				int remainSLotsStratAt = arrayThree.size() + 1;

				String sql = "INSERT INTO public.nt_t_owner_sheet_dates\r\n"
						+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, "
						+ "col17, col18, col19, col20, col21, col22, col23, col24, col25, "
						+ "col26, col27, col28, col29, col30, col31,month)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?,?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?);";

				ps = con.prepareStatement(sql);
				for (int i = 0; i < arrayThree.size(); i++) {

					ps.setString(i + 1, arrayThree.get(i).toString());

				}

				for (int j = remainSLotsStratAt; j < 32; j++) {
					ps.setString(j, null);
				}
				ps.setString(32, month3);

				int i3 = ps.executeUpdate();
				if (i3 > 0) {
					insertSuccess = true;
				}
			}

			if (!arrayTwo.isEmpty()) {
				int remainSLotsStratAt = arrayTwo.size() + 1;

				String sql2 = "INSERT INTO public.nt_t_owner_sheet_dates\r\n"
						+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, "
						+ "col17, col18, col19, col20, col21, col22, col23, col24, col25, "
						+ "col26, col27, col28, col29, col30, col31,month)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?,?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?);";

				ps2 = con.prepareStatement(sql2);
				for (int i = 0; i < arrayTwo.size(); i++) {
					ps2.setString(i + 1, arrayTwo.get(i).toString());
				}
				for (int j = remainSLotsStratAt; j < 32; j++) {
					ps2.setString(j, null);
				}

				ps2.setString(32, month2);

				int i2 = ps2.executeUpdate();
				if (i2 > 0) {
					insertSuccess = true;
				}

			}

			if (!arrayOne.isEmpty()) {

				int remainSLotsStratAt = arrayOne.size() + 1;

				String sql3 = "INSERT INTO public.nt_t_owner_sheet_dates\r\n"
						+ "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, "
						+ "col17, col18, col19, col20, col21, col22, col23, col24, col25, "
						+ "col26, col27, col28, col29, col30, col31,month)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?, ?, ?, ?,?, ?, ?,?,?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?);";

				ps3 = con.prepareStatement(sql3);
				for (int i = 0; i < arrayOne.size(); i++) {
					ps3.setString(i + 1, arrayOne.get(i).toString());

				}

				for (int j = remainSLotsStratAt; j < 32; j++) {
					ps3.setString(j, null);
				}
				ps3.setString(32, month1);

				int i1 = ps3.executeUpdate();
				if (i1 > 0) {
					insertSuccess = true;
				}
			}

			con.commit();

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(rs1);
			ConnectionManager.close(con);
		}
		return insertSuccess;

	}

	@Override
	public boolean insertDatesRotationsForBusesMoreThanThirty(ArrayList<String> arrayOne, ArrayList<String> arrayTwo,
			ArrayList<String> arrayThree, String month1, String month2, String month3, int totalBusCount) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		boolean insertSuccess = false;
		ArrayList<String> columnList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();
			/*
			 * if(totalBusCount>32) {
			 *//*** dynamically create columns for remaining buses start ***/
			/*
			 * 
			 * for(int i=33;i<= totalBusCount ;i++) { String val=Integer.toString(i); String
			 * col ="col"; col =col + val; String
			 * createColumnSql="ALTER TABLE public.nt_t_owner_sheet_dates2 ADD " + "" + col
			 * + " " + " varchar(100) NULL";
			 * 
			 * ps4 = con.prepareStatement(createColumnSql); ps4.executeUpdate();
			 * 
			 * } con.commit();
			 * 
			 *//*** dynamically create columns for remaining buses end ***//*
																			 * }
																			 */
			String colCount = "?";
			for (int i = 1; i <= 121; i++) {
				columnList.add(colCount);// ?,?,
			}
			String values = columnList.toString();
			/*** remove [] from ***/
			// Creating a StringBuilder object
			StringBuilder sb = new StringBuilder(values);

			// Removing the last character
			// of a string
			sb.deleteCharAt(values.length() - 1);

			// Removing the first character
			// of a string
			sb.deleteCharAt(0);

			// Converting StringBuilder into a string
			// and return the modified string
			values = sb.toString();

			/**** end ***/
			if (!arrayThree.isEmpty()) {

				String sql = "INSERT INTO public.nt_t_owner_sheet_dates2\r\n" + "VALUES(" + "" + values + " " + " )";

				ps = con.prepareStatement(sql);
				ps.setString(1, month3);
				int j = 2;
				for (int i = 0; i <= arrayThree.size(); i++) {
					if (j <= totalBusCount + 1) {
						ps.setString(j, arrayThree.get(i).toString());
						j++;
					}
				}

				for (int i = arrayThree.size() + 1; i < 122; i++) {
					ps.setString(i, null);

				}

				int i3 = ps.executeUpdate();
				if (i3 > 0) {
					insertSuccess = true;
				}

			}
			if (!arrayTwo.isEmpty()) {
				String sql2 = "INSERT INTO public.nt_t_owner_sheet_dates2\r\n" + "VALUES(" + "" + values + " " + " )";

				ps2 = con.prepareStatement(sql2);
				ps2.setString(1, month2);
				int j = 2;
				for (int i = 0; i <= arrayTwo.size(); i++) {
					if (j <= totalBusCount + 1) {
						ps2.setString(j, arrayTwo.get(i).toString());
						j++;
					}
				}

				for (int i = arrayTwo.size() + 1; i < 122; i++) {
					ps2.setString(i, null);

				}
				int i2 = ps2.executeUpdate();
				if (i2 > 0) {
					insertSuccess = true;
				}

			}

			if (!arrayOne.isEmpty()) {

				String sql3 = "INSERT INTO public.nt_t_owner_sheet_dates2\r\n" + "VALUES(" + "" + values + " " + " )";

				ps3 = con.prepareStatement(sql3);
				ps3.setString(1, month1);
				int j = 2;

				for (int i = 0; i <= arrayOne.size(); i++) {
					if (j <= totalBusCount + 1) {
						ps3.setString(j, arrayOne.get(i).toString());
						j++;
					}
				}

				for (int i = arrayOne.size() + 1; i < 122; i++) {
					ps3.setString(i, null);

				}
				int i1 = ps3.executeUpdate();
				if (i1 > 0) {
					insertSuccess = true;
				}
			}

			con.commit();

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(ps4);
			ConnectionManager.close(rs1);
			ConnectionManager.close(con);
		}
		return insertSuccess;

	}

	@Override
	public void removeColumns(int removeColumns) {
		Connection con = null;
		PreparedStatement ps = null;

		try {

			con = ConnectionManager.getConnection();

			for (int i = 33; i <= removeColumns; i++) {
				String val = Integer.toString(i);
				String col = "col";
				col = col + val;
				String createColumnSql = "ALTER TABLE public.nt_t_owner_sheet_dates2 DROP COLUMN " + "" + col + " "
						+ " ";

				ps = con.prepareStatement(createColumnSql);
				ps.executeUpdate();

			}
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public List<String> getDates(int busNos) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String column = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			for (int i = 1; i <= busNos; i++) {
				String val = Integer.toString(i);
				String col = "col";
				col = col + val;

				String query = "SELECT " + "" + col + " " + " FROM public.nt_t_owner_sheet_dates2;";
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();

				while (rs.next()) {

					column = rs.getString(col);
					returnList.add(column);

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
	public int getNumberOfLeaves(String tripType, String groupNo, String route, String service, String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int value = 0;

		try {
			con = ConnectionManager.getConnection();

			String leaveQuery = "SELECT no_of_buses_on_leave  FROM public.nt_m_timetable_generator x\r\n"
					+ "WHERE  generator_ref_no =? and trip_type =? and status ='A' and group_no =?;";

			ps = con.prepareStatement(leaveQuery);
			ps.setString(1, refNo);
			ps.setString(2, tripType);
			ps.setString(3, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getInt("no_of_buses_on_leave");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return value;
	}

	@Override
	public List<String> getExpiredPermitNumber(String routeNumber) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> valueList = new ArrayList<>();
		String expiredDate = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String permitBusNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select pm_permit_expire_date,pm_permit_no,pm_vehicle_regno  from public.nt_t_pm_application where pm_route_no =?;";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				expiredDate = rs.getString("pm_permit_expire_date");
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(expiredDate);
				if (date1.before(date)) {
					permitBusNo = rs.getString("pm_vehicle_regno") + "(" + rs.getString("pm_permit_no") + ")";
					valueList.add(permitBusNo);
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return valueList;
	}

//	@Override
//	public List<CombinePanelGenaratorDTO> getFixBus(String tripType, String route, String service, String groupNo){
//		List<CombinePanelGenaratorDTO> fixBus = new ArrayList<CombinePanelGenaratorDTO>();
//		Connection con = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		try {
//			if(tripType.equals("O")) {
//				String sql = "select a.abbreviation,a.start_time from public.panel_generator_origin_trip_details a \r\n"
//						+ "inner join public.panel_generator_general_data b on a.ref_no = b.refno \r\n"
//						+ "where a.route_no =? and b.buscategory =? \r\n"
//						+ "and a.group_name =? and a.is_fixed=true";
//				
//				ps = con.prepareStatement(sql);
//				ps.setString(1, route);
//			}
//			
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		} finally {
//			ConnectionManager.close(rs);
//			ConnectionManager.close(ps);
//			ConnectionManager.close(con);
//		}
//		
//		return fixBus;
//	}


}
