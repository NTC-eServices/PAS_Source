package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CombineControlSheetDetailsDTO;
import lk.informatics.ntc.model.dto.CombineControlSheetMasterDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.RouteScheduleDetailsDTO;
import lk.informatics.ntc.model.dto.RouteScheduleMasterDTO;
import lk.informatics.ntc.model.dto.RouteScheduleMidPointDTO;
import lk.informatics.ntc.model.dto.ServiceTypeDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class CombineControlSheetServiceImpl implements CombineControlSheetService {

	@Override
	public List<RouteCreationDTO> getAllActiveRoutes() {

		List<RouteCreationDTO> data = new ArrayList<RouteCreationDTO>();

		String query = "SELECT seqno, rou_description, rou_number, rou_service_origine, rou_service_destination, "
				+ "rou_via, rou_distance, rou_description FROM public.nt_r_route WHERE active = 'A'order by rou_description asc ";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			ResultSet rs = preparedStatement.executeQuery();
			connection.commit();

			while (rs.next()) {
				RouteCreationDTO p = new RouteCreationDTO();
				p.setRouteNo(rs.getString("rou_number"));
				p.setStartFrom(rs.getString("rou_service_origine"));
				p.setEndAt(rs.getString("rou_service_destination"));
				p.setLength(rs.getBigDecimal("rou_distance"));
				p.setRouteVia(rs.getString("rou_via"));
				p.setRouteDesc(rs.getString("rou_number") + " - " +rs.getString("rou_description"));
				data.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public List<ServiceTypeDTO> getAllServiceStypes() {
		List<ServiceTypeDTO> data = new ArrayList<ServiceTypeDTO>();

		String query = "SELECT code, description, description_si, description_ta FROM nt_r_service_types  WHERE active ='A'order by description asc ";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			ResultSet rs = preparedStatement.executeQuery();
			connection.commit();

			while (rs.next()) {
				ServiceTypeDTO p = new ServiceTypeDTO();
				p.setCode(rs.getString("code"));
				p.setDescription(rs.getString("description"));
				p.setDescriptionSi(rs.getString("description_si"));
				p.setDescriptionTa(rs.getString("description_ta"));
				data.add(p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public List<StationDetailsDTO> getStationsByRouteAndServiceType(String routeNo, String serviceType) {

		List<StationDetailsDTO> data = new ArrayList<StationDetailsDTO>();

		String query = "select s.midpoint_name, s.code  from nt_m_route_midpoint rmp "
				+ "inner join nt_r_station s on rmp.mp_midpoint_code = s.code "
				+ "where mp_route_no=? and mp_bus_type=?";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {

			preparedStatement.setString(1, routeNo);
			preparedStatement.setString(2, serviceType);

			ResultSet rs = preparedStatement.executeQuery();
			connection.commit();

			while (rs.next()) {
				StationDetailsDTO s = new StationDetailsDTO();
				s.setStationCode(rs.getString("code"));
				s.setStationNameEn(rs.getString("midpoint_name"));
				s.setStationType("M");
				data.add(s);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public RouteScheduleMasterDTO getRouteScheduleInfo(String routeNo, String serviceType, String groupNo, String side,
			LocalDate startDate, LocalDate endDate) {

		String selectQuery = "select rs_route_ref_no, rs_generator_ref_no, rs_start_date,  rs_end_date, rs_no_of_dates, rs_is_swap, rs_rotation_type from nt_m_route_schedule_generator "
				+ "where rs_route_no =? and rs_group_no =? and rs_trip_type =? and rs_bus_category_code =? and "
				+ "(TO_DATE(rs_start_date , 'DD/MM/YYYY') , TO_DATE(rs_end_date, 'DD/MM/YYYY')) " + 
				"OVERLAPS (?, ?) order by created_date  desc limit 1";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {

			preparedStatement.setString(1, routeNo);
			preparedStatement.setString(2, groupNo);
			preparedStatement.setString(3, side);
			preparedStatement.setString(4, serviceType);

			if (startDate != null) {
				preparedStatement.setDate(5, java.sql.Date.valueOf(startDate));
			} else {
				preparedStatement.setNull(5, java.sql.Types.DATE);
			}
			if (endDate != null) {
				preparedStatement.setDate(6, java.sql.Date.valueOf(endDate));
			} else {
				preparedStatement.setNull(6, java.sql.Types.DATE);
			}

			ResultSet rs = preparedStatement.executeQuery();

			RouteScheduleMasterDTO masterDTO = null;
			while (rs.next()) {

				masterDTO = new RouteScheduleMasterDTO();
				masterDTO.setRsRouteRefNo(rs.getString("rs_route_ref_no"));
				masterDTO.setRsGeneratorRefNo(rs.getString("rs_generator_ref_no"));
				masterDTO.setRsStartDate(rs.getString("rs_start_date"));
				masterDTO.setRsEndDate(rs.getString("rs_end_date"));
				masterDTO.setRsNoOfDates(rs.getString("rs_no_of_dates"));
				masterDTO.setRsIsSwap(rs.getString("rs_is_swap"));
				masterDTO.setRsRotationType(rs.getString("rs_rotation_type"));

				masterDTO.setRsRouteNo(routeNo);
				masterDTO.setRsServiceType(serviceType);
				masterDTO.setRsGroupNO(groupNo);
				masterDTO.setRsSide(side);
			}

			return masterDTO;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<RouteScheduleDetailsDTO> getRouteScheduleTripDetailsByRef(String ref) {

		List<RouteScheduleDetailsDTO> list = new ArrayList<RouteScheduleDetailsDTO>();

		String selectQuery = "select trip_id::numeric,bus_no, day_no  from nt_t_route_schedule_generator_det01 "
				+ "where route_ref_no =? and leave_bus is null group by trip_id, bus_no, day_no order by day_no, trip_id, bus_no;";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {

			preparedStatement.setString(1, ref);

			ResultSet rs = preparedStatement.executeQuery();

			RouteScheduleDetailsDTO detailsDTO = null;
			while (rs.next()) {

				detailsDTO = new RouteScheduleDetailsDTO();
				detailsDTO.setTripId(String.valueOf(rs.getInt("trip_id")));
				detailsDTO.setBusNo(rs.getString("bus_no"));
				detailsDTO.setDayNo(rs.getString("day_no"));

				list.add(detailsDTO);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

	@Override
	public List<RouteScheduleDetailsDTO> getRouteScheduleLeaveTripDetailsByRef(String ref) {
		List<RouteScheduleDetailsDTO> list = new ArrayList<RouteScheduleDetailsDTO>();

		String selectQuery = "select leave_trip_id::numeric, leave_bus , leave_trip_day::numeric from nt_t_route_schedule_generator_det01 " + 
				"where route_ref_no =? and bus_no is null group by leave_trip_id, leave_bus, leave_trip_day order by leave_trip_day, leave_trip_id, leave_bus;";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {

			preparedStatement.setString(1, ref);

			ResultSet rs = preparedStatement.executeQuery();

			RouteScheduleDetailsDTO detailsDTO = null;
			while (rs.next()) {

				detailsDTO = new RouteScheduleDetailsDTO();
				detailsDTO.setTripId(String.valueOf(rs.getInt("leave_trip_id")));
				detailsDTO.setBusNo(rs.getString("leave_bus"));
				detailsDTO.setDayNo(rs.getString("leave_trip_day"));

				list.add(detailsDTO);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<RouteScheduleMidPointDTO> getRouteMidPointsForNoneSLTB(String routeNo, String serviceType, String groupNo, String side, StationDetailsDTO selectedStation) {
		List<RouteScheduleMidPointDTO> list = new ArrayList<RouteScheduleMidPointDTO>();

		String selectQuery = null;
		String andClause = null;
		
		if(selectedStation.getStationType().equals("O")) {
			andClause = "and mp_origin = '"+ selectedStation.getStationNameEn() + "'";
		}else if(selectedStation.getStationType().equals("D")) {
			andClause = "and mp_destination = '"+ selectedStation.getStationNameEn() + "'";
		}else {
			andClause = "and mp_midpoint = '"+ selectedStation.getStationNameEn() + "'";
		}

		if (side.equals("O")) {
			selectQuery = "select mp_bus_no, mp_origin, mp_start_time, mp_midpoint , mp_midpoint_time, mp_destination , mp_endtime  from nt_m_midpoint_o_to_d "
					+ "where mp_route_no=? and mp_service_type =? and group_no = ? AND mp_bus_no NOT LIKE 'SLTB%'  "+andClause+" order by mp_start_time";
		} else {
			selectQuery = "select mp_bus_no, mp_origin, mp_start_time, mp_midpoint , mp_midpoint_time, mp_destination , mp_endtime  from nt_m_midpoint_d_to_o "
					+ "where mp_route_no=? and mp_service_type =? and group_no = ? AND mp_bus_no NOT LIKE 'SLTB%'  "+andClause+" order by mp_start_time";
		}

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {

			preparedStatement.setString(1, routeNo);
			preparedStatement.setString(2, serviceType);
			preparedStatement.setString(3, groupNo);

			ResultSet rs = preparedStatement.executeQuery();

			RouteScheduleMidPointDTO midPointDTO = null;
			while (rs.next()) {

				midPointDTO = new RouteScheduleMidPointDTO();
				midPointDTO.setMpOrigin(rs.getString("mp_origin"));
				midPointDTO.setMpStartTime(rs.getString("mp_start_time"));
				midPointDTO.setMpMidpoint(rs.getString("mp_midpoint"));
				midPointDTO.setMpMidpointTime(rs.getString("mp_midpoint_time"));
				midPointDTO.setMpDestination(rs.getString("mp_destination"));
				midPointDTO.setMpEndTime(rs.getString("mp_endtime"));				
				midPointDTO.setMpBusNo(rs.getString("mp_bus_no"));	

				list.add(midPointDTO);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<RouteScheduleMidPointDTO> getRouteMidPointsForSLTB(String routeNo, String serviceType, String groupNo,
			String side, StationDetailsDTO selectedStation) {
		List<RouteScheduleMidPointDTO> list = new ArrayList<RouteScheduleMidPointDTO>();
		String selectQuery = null;
		String andClause = null;
		
		if(selectedStation.getStationType().equals("O")) {
			andClause = "and mp_origin = '"+ selectedStation.getStationNameEn() + "'";
		}else if(selectedStation.getStationType().equals("D")) {
			andClause = "and mp_destination = '"+ selectedStation.getStationNameEn() + "'";
		}else {
			andClause = "and mp_midpoint = '"+ selectedStation.getStationNameEn() + "'";
		}

		if (side.equals("O")) {
			selectQuery = "select mp_bus_no, mp_origin, mp_start_time, mp_midpoint , mp_midpoint_time, mp_destination , mp_endtime  from nt_m_midpoint_o_to_d "
					+ "where mp_route_no=? and mp_service_type =? and group_no = ? AND mp_bus_no  LIKE 'SLTB%'  "+andClause+" order by mp_start_time";
		} else {
			selectQuery = "select mp_bus_no, mp_origin, mp_start_time, mp_midpoint , mp_midpoint_time, mp_destination , mp_endtime  from nt_m_midpoint_d_to_o "
					+ "where mp_route_no=? and mp_service_type =? and group_no = ? AND mp_bus_no  LIKE 'SLTB%'  "+andClause+" order by mp_start_time";
		}

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {

			preparedStatement.setString(1, routeNo);
			preparedStatement.setString(2, serviceType);
			preparedStatement.setString(3, groupNo);

			ResultSet rs = preparedStatement.executeQuery();

			RouteScheduleMidPointDTO midPointDTO = null;
			while (rs.next()) {

				midPointDTO = new RouteScheduleMidPointDTO();
				midPointDTO.setMpOrigin(rs.getString("mp_origin"));
				midPointDTO.setMpStartTime(rs.getString("mp_start_time"));
				midPointDTO.setMpMidpoint(rs.getString("mp_midpoint"));
				midPointDTO.setMpMidpointTime(rs.getString("mp_midpoint_time"));
				midPointDTO.setMpDestination(rs.getString("mp_destination"));
				midPointDTO.setMpEndTime(rs.getString("mp_endtime"));				
				midPointDTO.setMpBusNo(rs.getString("mp_bus_no"));	

				list.add(midPointDTO);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
