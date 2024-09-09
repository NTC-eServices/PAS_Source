package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lk.informatics.ntc.model.dto.BusDetailsDTO;
import lk.informatics.ntc.model.dto.ChakreeyaPalaliDTO;
import lk.informatics.ntc.model.dto.ChakreeyaPalaliDestiDTO;
import lk.informatics.ntc.model.dto.RouteScheduleHelperDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.view.beans.OwnerSheetListMonthWrapper;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;
import org.apache.log4j.Logger;

public class TimeTableServiceImpl implements TimeTableService {

	public static Logger logger = Logger.getLogger(TimeTableServiceImpl.class);
	private static String abbOri = null;
	private static String abbDes = null;

	@Override
	public List<TimeTableDTO> getRouteNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> routeNoList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT distinct  route_no, status FROM public.nt_m_panelgenerator where status='A'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setRouteNo(rs.getString("route_no"));
				routeNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeNoList;
	}

	@Override
	public TimeTableDTO getBusCategory(String routeNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  route_no,bus_category, status, nt_r_service_types.description "
					+ "FROM public.nt_m_panelgenerator "
					+ "inner join public.nt_r_service_types on nt_r_service_types.code=nt_m_panelgenerator.bus_category "
					+ "where status='A' and route_no=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setBusCategory(rs.getString("bus_category"));
				dto.setBusCategoryDes(rs.getString("description"));

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public int getGroupCount(String routeNO, String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int groupCount = 0;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select count(seq_panelgenerator) as no_of_groups from nt_t_panelgenerator_det "
					+ "inner join nt_m_panelgenerator on nt_m_panelgenerator.seq=nt_t_panelgenerator_det.seq_panelgenerator "
					+ "where nt_m_panelgenerator.route_no=? and nt_t_panelgenerator_det.status='A'  and nt_m_panelgenerator.ref_no=?  and nt_m_panelgenerator.status='A'";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			ps.setString(2, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				groupCount = rs.getInt("no_of_groups");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return groupCount;
	}

	@Override
	public List<TimeTableDTO> getNewRouteDetails(String routeNO, String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> routeDetailsList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select  origin_start_time, origin_end_time, origin_divide_range,destination_start_time,destination_end_time,destination_divide_range from nt_t_panelgenerator_det "
					+ "inner join nt_m_panelgenerator on nt_m_panelgenerator.seq=nt_t_panelgenerator_det.seq_panelgenerator "
					+ "where nt_m_panelgenerator.route_no=? and nt_t_panelgenerator_det.status='A'  and nt_m_panelgenerator.ref_no=? and nt_m_panelgenerator.status='A' order by group_no ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			ps.setString(2, refNo);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setOriginStartTimeString(rs.getString("origin_start_time"));
				e.setOriginEndTimeString(rs.getString("origin_end_time"));
				e.setOriginDivideRangeString(rs.getString("origin_divide_range"));

				e.setDestinationStartTimeString(rs.getString("destination_start_time"));
				e.setDestinationEndTimeString(rs.getString("destination_end_time"));
				e.setDestinationDivideRangeString(rs.getString("destination_divide_range"));
				routeDetailsList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeDetailsList;
	}

	@Override
//	public List<TimeTableDTO> getStartEndTimeForPanelGenerator(String routeNO, String refNo) {
//		Connection con = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		List<TimeTableDTO> routeDetailsList = new ArrayList<TimeTableDTO>();
//
//		try {
//			con = ConnectionManager.getConnection();
//
//			String query2 = "SELECT\r\n" + "    origin_start_time, origin_end_time, origin_divide_range,\r\n"
//					+ "    destination_start_time, destination_end_time, destination_divide_range\r\n" + "FROM\r\n"
//					+ "    nt_t_panelgenerator_det\r\n" + "CROSS JOIN\r\n" + "    nt_m_panelgenerator\r\n" + "WHERE\r\n"
//					+ "    nt_m_panelgenerator.route_no = ?\r\n" + "    AND nt_t_panelgenerator_det.status = 'A'\r\n"
//					+ "    AND nt_m_panelgenerator.ref_no = ?\r\n" + "    AND nt_m_panelgenerator.status = 'A'\r\n"
//					+ "ORDER BY\r\n" + "    group_no;\r\n" + "";
//
//			ps = con.prepareStatement(query2);
//			ps.setString(1, routeNO);
//			ps.setString(2, refNo);
//			rs = ps.executeQuery();
//			TimeTableDTO e;
//
//			while (rs.next()) {
//				e = new TimeTableDTO();
//	            String oriStartTime = extractTime(rs.getString("origin_start_time"));
//	            String oriEndTime = extractTime(rs.getString("origin_end_time"));
//	           
//
//	            e.setOriginStartTimeString(oriStartTime);
//	            e.setOriginEndTimeString(oriEndTime);
//	            e.setOriginDivideRangeString(rs.getString("origin_divide_range"));
//
//	            String destStartTime = extractTime(rs.getString("destination_start_time"));
//	            String destEndTime = extractTime(rs.getString("destination_end_time"));
//
//	            e.setDestinationStartTimeString(destStartTime);
//	            e.setDestinationEndTimeString(destEndTime);
//	            e.setDestinationDivideRangeString(rs.getString("destination_divide_range"));
//	            routeDetailsList.add(e);
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//			return null;
//		} finally {
//			ConnectionManager.close(rs);
//			ConnectionManager.close(ps);
//			ConnectionManager.close(con);
//		}
//
//		return routeDetailsList;
//	}

	
	public List<TimeTableDTO> getStartEndTimeForPanelGenerator(String routeNO, String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> routeDetailsList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT\r\n"
					+ "    nt_t_panelgenerator_det.origin_start_time,\r\n"
					+ "    nt_t_panelgenerator_det.origin_end_time,\r\n"
					+ "    nt_t_panelgenerator_det.origin_divide_range,\r\n"
					+ "    nt_t_panelgenerator_det.destination_start_time,\r\n"
					+ "    nt_t_panelgenerator_det.destination_end_time,\r\n"
					+ "    nt_t_panelgenerator_det.destination_divide_range\r\n"
					+ "FROM\r\n"
					+ "    nt_t_panelgenerator_det\r\n"
					+ "JOIN\r\n"
					+ "    nt_m_panelgenerator\r\n"
					+ "ON\r\n"
					+ "    nt_t_panelgenerator_det.seq_panelgenerator = nt_m_panelgenerator.seq\r\n"
					+ "WHERE\r\n"
					+ "    nt_m_panelgenerator.route_no = ?\r\n"
					+ "    AND nt_m_panelgenerator.ref_no = ?\r\n"
					+ "    AND nt_t_panelgenerator_det.status = 'A'\r\n"
					+ "    AND nt_m_panelgenerator.status = 'A'\r\n"
					+ "ORDER BY\r\n"
					+ "    nt_t_panelgenerator_det.group_no;\r\n"
					+ "";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			ps.setString(2, refNo);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
	            String oriStartTime = extractTime(rs.getString("origin_start_time"));
	            String oriEndTime = extractTime(rs.getString("origin_end_time"));
	            
	            String startDatePart = extractDatePart(rs.getString("origin_start_time"));
				String endDatePart = extractDatePart(rs.getString("origin_end_time"));
	            
	            long intrvalDate = intervalDatePart(startDatePart, endDatePart);
	            long intrvalTime = intervalTimePart(oriStartTime, oriEndTime);
	            
	            long checkTime = (intrvalDate * 24) + intrvalTime;

	            e.setOriginDivideRangeString(rs.getString("origin_divide_range"));

	            String destStartTime = extractTime(rs.getString("destination_start_time"));
	            String destEndTime = extractTime(rs.getString("destination_end_time"));
	            
	            String startDatePartDes = extractDatePart(rs.getString("destination_start_time"));
				String endDatePartDes = extractDatePart(rs.getString("destination_end_time"));
	            
	            long intrvalDateDes = intervalDatePart(startDatePartDes, endDatePartDes);
	            long intrvalTimeDes = intervalTimePart(destStartTime, destEndTime);
	            
	            long checkTimeDes = (intrvalDateDes * 24) + intrvalTimeDes;
	        
//	            if(checkTimeDes > 24 || checkTime > 24) {
	            	e.setOriginStartTimeString(rs.getString("origin_start_time"));
	            	e.setOriginEndTimeString(rs.getString("origin_end_time"));
	            	e.setDestinationStartTimeString(rs.getString("destination_start_time"));
		            e.setDestinationEndTimeString(rs.getString("destination_end_time"));
		            e.setCheck(true);
//	            }else {
//	            	e.setOriginStartTimeString(oriStartTime);
//	 	            e.setOriginEndTimeString(oriEndTime);
//		            e.setDestinationStartTimeString(destStartTime);
//		            e.setDestinationEndTimeString(destEndTime);
//		            e.setCheck(false);
//	            }
	            e.setDestinationDivideRangeString(rs.getString("destination_divide_range"));
	            
	            routeDetailsList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeDetailsList;
	}
	
	private String extractDatePart(String string) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	    try {
	        Date date = dateFormat.parse(string);
	        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
	        return timeFormat.format(date);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null; // Handle the error as needed in your application
	    }
       
    }
	
	private String extractTime(String dateTimeString) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	    try {
	        Date date = dateFormat.parse(dateTimeString);
	        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	        return timeFormat.format(date);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null; // Handle the error as needed in your application
	    }
	}
	
	public String calculateTime(String startTime, String travelTime) {
		String endTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		try {
			java.util.Date currentArrivalTime = sdf.parse(startTime);

			String[] timeParts = travelTime.split(":");
			int hours = Integer.parseInt(timeParts[0]);
			int minutes = Integer.parseInt(timeParts[1]);

			int minutesToAdd = (hours * 60) + minutes;

			currentArrivalTime = new Date(currentArrivalTime.getTime() + (minutesToAdd * 60000));
			endTime = sdf.format(currentArrivalTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return endTime;

	}
	
	public long intervalDatePart(String startDatePart,String endDatePart) {
		 try {
			 	long inervalDay = 0;
			 
		        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		        Date startDate = dateFormat.parse(startDatePart);
		        Date endDate = dateFormat.parse(endDatePart);

		        // Extract the day part of the dates
		        Calendar startCal = Calendar.getInstance();
		        startCal.setTime(startDate);
		        int startYear = startCal.get(Calendar.YEAR);
		        int startMonth = startCal.get(Calendar.MONTH);
		        int startDay = startCal.get(Calendar.DAY_OF_MONTH);

		        Calendar endCal = Calendar.getInstance();
		        endCal.setTime(endDate);
		        int endYear = endCal.get(Calendar.YEAR);
		        int endMonth = endCal.get(Calendar.MONTH);
		        int endDay = endCal.get(Calendar.DAY_OF_MONTH);
		        
		        int inervalYear = endYear - startYear;
		        int inervalMonth = endMonth - startMonth;
		        
		        if(inervalYear == 0 && inervalMonth == 0) {
		        	inervalDay = endDay - startDay;
		        }

		        
				// Calculate the interval in days
		        return inervalDay;
		    } catch (ParseException e) {
		        // Handle parsing exception
		        e.printStackTrace();
		        return -1; // Return -1 to indicate an error
		    }
      
   }
	
	public long intervalTimePart(String startTimePart,String endTimePart) {
		 try {
			 	long inervalHour = 0;
			 
			 	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		        Date startTime = dateFormat.parse(startTimePart);
		        Date endTime = dateFormat.parse(endTimePart);

		        // Extract the day part of the dates
		        Calendar startCal = Calendar.getInstance();
		        startCal.setTime(startTime);
		        int startHour = startCal.get(Calendar.HOUR_OF_DAY);
		        int startMin = startCal.get(Calendar.MINUTE);

		        Calendar endCal = Calendar.getInstance();
		        endCal.setTime(endTime);
		        int endHour = endCal.get(Calendar.HOUR_OF_DAY);
		        int endMin = endCal.get(Calendar.MINUTE);
		 
		        inervalHour = endHour - startHour;
		        
		        if(inervalHour == 0) {
		        	inervalHour = endMin - startMin;
		        }
		        
				// Calculate the interval in days
		        return inervalHour;
		    } catch (ParseException e) {
		        // Handle parsing exception
		        e.printStackTrace();
		        return -1; // Return -1 to indicate an error
		    }
     
  }

	
	@Override
	public int getPVTbusCount(String routeNo, boolean type, String serviceType, String group) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int pvtCount = 0;
		
		if(serviceType.equalsIgnoreCase("NORMAL")) {
			serviceType = "001";
		}else if(serviceType.equalsIgnoreCase("LUXURY")) {
			serviceType = "002";
		}else if(serviceType.equalsIgnoreCase("SUPER LUXURY")) {
			serviceType = "003";
		}else if(serviceType.equalsIgnoreCase("EXPRESSWAY BUS")) {
			serviceType = "EB";
		}
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select count(rs_vehicle_regno) as pvtCount from public.nt_t_route_setup_timetable "
					+ "where rs_route_no= ? and rs_status in ('A','O') and  rs_route_flag= ? and rs_service_type = ? "
					+ "and rs_group_no = ? and rs_use_time_table=true ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setBoolean(2, type);
			ps.setString(3, serviceType);
			ps.setString(4, group);
			rs = ps.executeQuery();

			while (rs.next()) {
				pvtCount = rs.getInt("pvtCount");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return pvtCount;
	}

	@Override
	public List<VehicleInspectionDTO> getPVTbuses(String routeNo, String type) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<VehicleInspectionDTO> dtoList = new ArrayList<VehicleInspectionDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select pm_vehicle_regno, pm_permit_no from public.nt_t_pm_application where pm_route_no=? and pm_status in ('A','O')"
					+ " and  pm_route_flag=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, type);
			rs = ps.executeQuery();

			while (rs.next()) {
				VehicleInspectionDTO vehicleDto = new VehicleInspectionDTO();
				vehicleDto.setVehicleNo(rs.getString("pm_vehicle_regno"));
				vehicleDto.setPermitNo(rs.getString("pm_permit_no"));
				dtoList.add(vehicleDto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dtoList;
	}

	@Override
	public String getRestTime(String routeNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String restTime = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select rc_driver_rest_time  FROM public.nt_t_route_creator where rc_route_no=? and rc_status='A'";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			rs = ps.executeQuery();

			while (rs.next()) {
				restTime = rs.getString("rc_driver_rest_time");

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return restTime;
	}

	@Override
	public boolean insertTripsGenerateMasterData(TimeTableDTO timeTableDTO, String value, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSave = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_trips_generator");

			String query = "INSERT INTO public.nt_m_trips_generator"
					+ "(seq, tg_route_no, tg_generator_ref_no, tg_trips_ref_code, tg_bus_category, tg_route_start, tg_route_end, "
					+ "tg_created_by, tg_created_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(query);

			try {
				ps.setLong(1, seqNo);
				ps.setString(2, timeTableDTO.getRouteNo());
				ps.setString(3, timeTableDTO.getGenereatedRefNo());
				ps.setString(4, value);
				ps.setString(5, timeTableDTO.getBusCategory());
				ps.setString(6, timeTableDTO.getOrigin());
				ps.setString(7, timeTableDTO.getDestination());

				ps.setString(8, user);
				ps.setTimestamp(9, timestamp);

				int insert = ps.executeUpdate();

				if (insert > 0) {
					isDataSave = true;
				} else {
					isDataSave = false;
				}

				con.commit();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isDataSave;
	}

	@Override
	public void insertTripsGenerateDetailsOneData(List<TimeTableDTO> list, String value, String user, String groupNo,
			String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			for (int i = 0; i < list.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_trips_generator_det");

				String query = "INSERT INTO public.nt_t_trips_generator_det "
						+ "(seq, trips_ref_code, time_range_from, time_range_to, time_range_frequency,group_no,"
						+ "trip_type, no_of_bus, created_by, created_date)" + "VALUES(?, ?, ?, ?, ?,?,?, ?, ?, ?); ";

				ps = con.prepareStatement(query);

				ps.setLong(1, seqNo);
				ps.setString(2, value);
				ps.setString(3, list.get(i).getFirstValue());
				ps.setString(4, list.get(i).getSecondValue());
				ps.setInt(5, list.get(i).getFrequency());
				ps.setString(6, groupNo);
				ps.setString(7, tripType);
				ps.setInt(8, list.get(i).getNoOfBuses());
				ps.setString(9, user);
				ps.setTimestamp(10, timestamp);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
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

	}

	@Override
	public void insertTripsGenerateDetailsTwoData(List<TimeTableDTO> list, String value, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			for (int i = 0; i < list.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_trips_generator_det02");

				String query = "INSERT INTO public.nt_t_trips_generator_det02 "
						+ "(seq, trips_ref_code, group_no, trip_type, no_of_trips, no_of_ctb_bus, no_of_pvt_bus,"
						+ " no_of_other_bus, rest_time, total_bus, created_by, created_date) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

				ps = con.prepareStatement(query);

				ps.setLong(1, seqNo);
				ps.setString(2, value);
				ps.setString(3, list.get(i).getGroup());
				ps.setString(4, list.get(i).getType());

				ps.setInt(5, list.get(i).getTotalTrips());
				ps.setInt(6, list.get(i).getNoOfCTBbueses());
				ps.setInt(7, list.get(i).getNoOfPVTbueses());
				ps.setInt(8, list.get(i).getNoOfOtherbuses());
				ps.setString(9, list.get(i).getRestTime());
				ps.setInt(10, list.get(i).getTotalBuses());
				ps.setString(11, user);
				ps.setTimestamp(12, timestamp);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
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
	}

	@Override
	public String generateReferenceNo() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strAppNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tg_trips_ref_code " + " FROM public.nt_m_trips_generator "
					+ " ORDER BY tg_created_date desc " + " LIMIT 1";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				result = rs.getString("tg_trips_ref_code");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "TTR" + currYear + ApprecordcountN;
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
					strAppNo = "TTR" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "TTR" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	@Override
	public TimeTableDTO getRouteData(String routeNO, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  ref_no,route_no, nt_r_route.rou_service_origine,nt_r_route.rou_service_destination "
					+ "FROM public.nt_m_panelgenerator "
					+ "inner join public.nt_r_route on nt_r_route.rou_number=nt_m_panelgenerator.route_no "
					+ " where status='A' and route_no=? and bus_category=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setDestination(rs.getString("rou_service_destination"));
				dto.setGenereatedRefNo(rs.getString("ref_no"));
				dto.setOrigin(rs.getString("rou_service_origine"));
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public TimeTableDTO getRouteData(String routeNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  ref_no,route_no, nt_r_route.rou_service_origine,nt_r_route.rou_service_destination "
					+ "FROM public.nt_m_panelgenerator "
					+ "inner join public.nt_r_route on nt_r_route.rou_number=nt_m_panelgenerator.route_no "
					+ " where status='A' and route_no=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setDestination(rs.getString("rou_service_destination"));
				dto.setGenereatedRefNo(rs.getString("ref_no"));
				dto.setOrigin(rs.getString("rou_service_origine"));
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public boolean isRecordFound(TimeTableDTO timeTableDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isFound = false;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT tg_route_no, tg_generator_ref_no, tg_bus_category FROM public.nt_m_trips_generator "
					+ "WHERE tg_route_no=? and  tg_generator_ref_no=? and tg_bus_category=?;";

			ps = con.prepareStatement(query2);
			ps.setString(1, timeTableDTO.getRouteNo());
			ps.setString(2, timeTableDTO.getGenereatedRefNo());
			ps.setString(3, timeTableDTO.getBusCategory());
			rs = ps.executeQuery();

			if (rs.next()) {
				isFound = true;
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

	@Override
	public List<TimeTableDTO> getRouteNoListForFixedBuses() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> routeNoList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct route_no from public.nt_m_panelgenerator "
					+ "inner join public.nt_m_timetable_generator on nt_m_timetable_generator.generator_ref_no=nt_m_panelgenerator.ref_no "
					+ "inner join public.nt_m_timetable_generator_det on nt_m_timetable_generator_det.seq_master=nt_m_timetable_generator.seq_no "
					+ "where nt_m_panelgenerator.status='A' and nt_m_timetable_generator.status='A'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setRouteNo(rs.getString("route_no"));
				routeNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeNoList;
	}

	@Override
	public List<TimeTableDTO> getDetailsOfFixedBuses(TimeTableDTO dto, String groupNo, String type) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> fixedBusesList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select b.seq_no, b.bus_num, b.start_time_slot, b.end_time_slot, b.assigned_bus_no, b.is_fixed_time, b.trip_type, b.trip_id  from nt_m_timetable_generator a "
					+ "inner join public.nt_m_timetable_generator_det b on b.seq_master = a.seq_no "
					+ "where a.generator_ref_no=? and b.group_no=? and b.trip_type=? order by b.start_time_slot";

			ps = con.prepareStatement(query2);

			ps.setString(1, dto.getGenereatedRefNo());
			ps.setString(2, groupNo);
			ps.setString(3, type);

			rs = ps.executeQuery();
			TimeTableDTO e;

			for (int i = 0; rs.next(); i++) {
				e = new TimeTableDTO();

				String id = String.valueOf(i + 1);
				e.setId(id);
				e.setTimeTableDetSeq(rs.getLong("seq_no"));
				e.setBusNo(rs.getString("bus_num"));
				e.setStartTime(rs.getString("start_time_slot"));
				e.setEndTime(rs.getString("end_time_slot"));
				e.setAssigneBusNo(rs.getString("assigned_bus_no"));
				e.setTripType(rs.getString("trip_type"));
				e.setDuplicateBusNum(rs.getString("bus_num"));
				e.setTempStartTime(rs.getString("start_time_slot"));
				e.setTempEndTime(rs.getString("end_time_slot"));
				e.setTripId(Integer.toString(rs.getInt("trip_id")));

				String isCTBCode = rs.getString("is_fixed_time");
				boolean isCTB = false;

				if (isCTBCode != null) {
					if (isCTBCode.equals("Y")) {
						isCTB = true;
						e.setFixedTime(true);
					} else {
						isCTB = false;
						e.setFixedTime(false);
					}
				} else {
					isCTB = false;
					e.setFixedTime(false);
				}

				e.setCtbBus(isCTB);
				fixedBusesList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return fixedBusesList;
	}

	@Override
	public List<TimeTableDTO> getAllBusNoForFixedBuses() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> busNoList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select pm_vehicle_regno from public.nt_t_pm_application where pm_status='A'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setAssigneBusNo(rs.getString("pm_vehicle_regno"));
				busNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public List<TimeTableDTO> getTimeTableGeneratorDetDataForHistorySave(String generatedRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> busNoList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT seq_no, seq_master, generator_ref_no, bus_num, start_time_slot, end_time_slot, assigned_bus_no, "
					+ "	trip_id, is_fixed_time, group_no, trip_type, created_by, created_date "
					+ " FROM public.nt_m_timetable_generator_det " + " where generator_ref_no=?;";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setMasterSeq(rs.getLong("seq_master"));
				e.setGenereatedRefNo(rs.getString("generator_ref_no"));
				e.setBusNo(rs.getString("bus_num"));
				e.setStartTime(rs.getString("start_time_slot"));
				e.setEndTime(rs.getString("end_time_slot"));
				e.setAssigneBusNo(rs.getString("assigned_bus_no"));

				e.setTripIdInt(rs.getInt("trip_id"));
				e.setWithFixedTimeCode(rs.getString("is_fixed_time"));
				e.setGroup(rs.getString("group_no"));
				e.setType(rs.getString("trip_type"));
				e.setTripType(rs.getString("trip_type"));
				e.setCreatedBy(rs.getString("created_by"));
				e.setCreatedDate(rs.getTimestamp("created_date"));
				busNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public boolean checkRelatedDataInTripGeneratorTable(String routeNo, String generatedRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isFound = false;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT tg_route_no, tg_generator_ref_no "
					+ "FROM public.nt_m_trips_generator WHERE tg_generator_ref_no=? and tg_route_no=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isFound = true;
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

	@Override
	public List<TimeTableDTO> getSavedTripsTimeDetails(String routeNO, String refNo, String type, String groupNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> list = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select a.tg_route_no, a.tg_generator_ref_no, a.tg_trips_ref_code, "
					+ "b.time_range_from, b.time_range_to, b.no_of_bus, b.time_range_frequency, b.group_no, b.trip_type "
					+ "from public.nt_m_trips_generator a "
					+ "inner join public.nt_t_trips_generator_det b on b.trips_ref_code=a.tg_trips_ref_code "
					+ "where tg_route_no=? and tg_generator_ref_no=? and b.group_no=? and b.trip_type=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			ps.setString(2, refNo);
			ps.setString(3, groupNo);
			ps.setString(4, type);

			rs = ps.executeQuery();
			TimeTableDTO e;

			for (int i = 0; rs.next(); i++) {

				e = new TimeTableDTO();
				String id = String.valueOf(i + 1);

				e.setId(id);
				e.setFirstValue(rs.getString("time_range_from"));
				e.setSecondValue(rs.getString("time_range_to"));
				e.setNoOfBuses(rs.getInt("no_of_bus"));
				e.setFrequency(rs.getInt("time_range_frequency"));

				list.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return list;
	}

	@Override
	public List<TimeTableDTO> showTimeSlotDetForGroups(String tripRefNo, String groupNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> timeSlotDet = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select trip_type,time_range_from,time_range_to,time_range_frequency,no_of_bus from  public.nt_t_trips_generator_det where trips_ref_code=? and group_no=? and trip_type=? and time_range_frequency!=0";

			ps = con.prepareStatement(query2);
			ps.setString(1, tripRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);

			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setTimeRangeStart(rs.getString("time_range_from"));
				e.setTimeRangeEnd(rs.getString("time_range_to"));
				e.setFrequency(rs.getInt("time_range_frequency"));
				e.setNoOfBuses(rs.getInt("no_of_bus"));
				e.setTripType(rs.getString("trip_type"));
				timeSlotDet.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return timeSlotDet;
	}

	@Override
	public TimeTableDTO getTravleTimeForRouteBusCate(String RouteNo, String busCat) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select rc_travel_time from public.nt_t_route_creator where rc_route_no=? and rc_bus_type =?;";

			ps = con.prepareStatement(query2);
			ps.setString(1, RouteNo);
			ps.setString(2, busCat);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setTimeRangeEnd(rs.getString("rc_travel_time"));

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public List<TimeTableDTO> generateBusType(String tripRefNo, String groupNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> busTypeList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT Count(time_range_from) as busCount FROM public.nt_t_trips_generator_det where trips_ref_code =? and group_no =? and trip_type =?";

			ps = con.prepareStatement(query2);
			ps.setString(1, tripRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			TimeTableDTO e;

			int count;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setBusCount(rs.getInt("Count(time_range_from)"));
				count = e.getBusCount();
				for (int i = 0; i < count; i++) {
					TimeTableDTO typeDTO = new TimeTableDTO();
					int j = 0;
					j = +1;
					typeDTO.setBusTypeName('M' + j);
					busTypeList.add(typeDTO);

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

		return busTypeList;
	}

	@Override
	public TimeTableDTO getReffrenceNo(String routeNo, String busCat) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select tg_generator_ref_no,tg_trips_ref_code FROM public.nt_m_trips_generator where tg_route_no =? and tg_bus_category =?";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, busCat);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setPanelGenNo(rs.getString("tg_generator_ref_no"));
				dto.setTripRefNo(rs.getString("tg_trips_ref_code"));
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	public int getGroupCountForEdit(String routeNO, String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String groupCount = null;
		int count = 0;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select c.group_no from public.nt_m_trips_generator a "
					+ "inner join public.nt_t_trips_generator_det02 c on c.trips_ref_code=a.tg_trips_ref_code "
					+ "where tg_route_no=? and tg_generator_ref_no=? order by c.group_no desc limit 1";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			ps.setString(2, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				groupCount = rs.getString("group_no");
			}

			if (groupCount == null) {
				count = 0;
			} else {
				count = Integer.parseInt(groupCount);
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
	public TimeTableDTO getSavedTripsRouteAndBusDetails(String routeNO, String refNo, String type, String groupNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select  a.tg_route_no, a.tg_generator_ref_no, a.tg_trips_ref_code, "
					+ "c.no_of_trips, c.no_of_ctb_bus, c.no_of_pvt_bus, c.no_of_other_bus, c.rest_time, c.total_bus "
					+ "from public.nt_m_trips_generator a "
					+ "inner join public.nt_t_trips_generator_det02 c on c.trips_ref_code=a.tg_trips_ref_code "
					+ "where tg_route_no=? and tg_generator_ref_no=? and c.group_no=? and c.trip_type=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			ps.setString(2, refNo);
			ps.setString(3, groupNo);
			ps.setString(4, type);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setTotalTrips(rs.getInt("no_of_trips"));
				dto.setNoOfPVTbueses(rs.getInt("no_of_pvt_bus"));
				dto.setNoOfCTBbueses(rs.getInt("no_of_ctb_bus"));
				dto.setNoOfOtherbuses(rs.getInt("no_of_other_bus"));

				String restTimeCode = rs.getString("rest_time");
				int total_minute = 0;

				String[] parts = restTimeCode.split(":");

				int start_hours = Integer.parseInt(parts[0]);
				int start_minute = Integer.parseInt(parts[1]);
				total_minute = start_minute + (start_hours * 60);

				dto.setRestTimeInt(total_minute);
				dto.setTotalBuses(rs.getInt("total_bus"));
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public List<TimeTableDTO> getTripsTableDetOneDataForHistorySave(String routeNo, String generatedRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> list = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select b.seq, b.trips_ref_code, b.time_range_from, b.time_range_to, b.time_range_frequency, b.group_no, "
					+ "b.trip_type, b.no_of_bus, b.created_by, b.created_date " + "from public.nt_m_trips_generator a "
					+ "inner join public.nt_t_trips_generator_det b on b.trips_ref_code=a.tg_trips_ref_code "
					+ "where tg_route_no=? and tg_generator_ref_no=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, generatedRefNo);

			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setTripRefNo(rs.getString("trips_ref_code"));
				e.setStartTime(rs.getString("time_range_from"));
				e.setEndTime(rs.getString("time_range_to"));
				e.setFrequency(rs.getInt("time_range_frequency"));
				e.setGroup(rs.getString("group_no"));
				e.setType(rs.getString("trip_type"));
				e.setNoOfBuses(rs.getInt("no_of_bus"));
				e.setCreatedBy(rs.getString("created_by"));
				e.setCreatedDate(rs.getTimestamp("created_date"));
				list.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return list;
	}

	@Override
	public List<TimeTableDTO> getTripsTableDetTwoDataForHistorySave(String routeNo, String generatedRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> list = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select  c.seq, c.trips_ref_code, c.group_no, c.trip_type, c.no_of_trips, c.no_of_ctb_bus, "
					+ "c.no_of_pvt_bus, c.no_of_other_bus, c.rest_time, c.total_bus, c.created_by, c.created_date "
					+ "from public.nt_m_trips_generator a "
					+ "inner join public.nt_t_trips_generator_det02 c on c.trips_ref_code=a.tg_trips_ref_code "
					+ "where tg_route_no=? and tg_generator_ref_no=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, generatedRefNo);

			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setTripRefNo(rs.getString("trips_ref_code"));
				e.setGroup(rs.getString("group_no"));
				e.setType(rs.getString("trip_type"));
				e.setTotalTrips(rs.getInt("no_of_trips"));
				e.setNoOfCTBbueses(rs.getInt("no_of_ctb_bus"));
				e.setNoOfPVTbueses(rs.getInt("no_of_pvt_bus"));
				e.setNoOfOtherbuses(rs.getInt("no_of_other_bus"));
				e.setRestTime(rs.getString("rest_time"));
				e.setTotalBuses(rs.getInt("total_bus"));
				e.setCreatedBy(rs.getString("created_by"));
				e.setCreatedDate(rs.getTimestamp("created_date"));

				list.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return list;
	}

	@Override
	public boolean insertTripsGenerateHistoryDetailsOneData(List<TimeTableDTO> list, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isDataEntered = false;

		try {

			con = ConnectionManager.getConnection();

			for (int i = 0; i < list.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_trips_generator_det");

				String query = "INSERT INTO public.nt_h_trips_generator_det "
						+ "(seq, trips_ref_code, time_range_from, time_range_to, time_range_frequency,group_no,"
						+ "trip_type, no_of_bus, created_by, created_date, modified_by, modified_date)"
						+ "VALUES(?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?); ";

				ps = con.prepareStatement(query);

				ps.setLong(1, seqNo);
				ps.setString(2, list.get(i).getTripRefNo());
				ps.setString(3, list.get(i).getStartTime());
				ps.setString(4, list.get(i).getEndTime());
				ps.setInt(5, list.get(i).getFrequency());
				ps.setString(6, list.get(i).getGroup());
				ps.setString(7, list.get(i).getType());
				ps.setInt(8, list.get(i).getNoOfBuses());
				ps.setString(9, list.get(i).getCreatedBy());
				ps.setTimestamp(10, list.get(i).getCreatedDate());
				ps.setString(11, user);
				ps.setTimestamp(12, timestamp);

				int c = ps.executeUpdate();

				if (c > 0) {
					isDataEntered = true;
				}

			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isDataEntered;

	}

	@Override
	public boolean insertTripsGenerateHistoryDetailsTwoData(List<TimeTableDTO> list, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isDataEntered = false;

		try {

			con = ConnectionManager.getConnection();

			for (int i = 0; i < list.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_trips_generator_det02");

				String query = "INSERT INTO public.nt_h_trips_generator_det02 "
						+ "(seq, trips_ref_code, group_no, trip_type, no_of_trips, no_of_ctb_bus, no_of_pvt_bus,"
						+ " no_of_other_bus, rest_time, total_bus, created_by, created_date, modified_by, modified_date) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?);";

				ps = con.prepareStatement(query);

				ps.setLong(1, seqNo);
				ps.setString(2, list.get(i).getTripRefNo());
				ps.setString(3, list.get(i).getGroup());
				ps.setString(4, list.get(i).getType());

				ps.setInt(5, list.get(i).getTotalTrips());
				ps.setInt(6, list.get(i).getNoOfCTBbueses());
				ps.setInt(7, list.get(i).getNoOfPVTbueses());
				ps.setInt(8, list.get(i).getNoOfOtherbuses());
				ps.setString(9, list.get(i).getRestTime());
				ps.setInt(10, list.get(i).getTotalBuses());
				ps.setString(11, list.get(i).getCreatedBy());
				ps.setTimestamp(12, list.get(i).getCreatedDate());
				ps.setString(13, user);
				ps.setTimestamp(14, timestamp);

				int c = ps.executeUpdate();

				if (c > 0) {
					isDataEntered = true;
				}

			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
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

		return isDataEntered;

	}

	@Override
	public String getTripReferenceNo(String generateRefNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String refNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT tg_trips_ref_code FROM public.nt_m_trips_generator where tg_generator_ref_no=? ;";

			ps = con.prepareStatement(query2);
			ps.setString(1, generateRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				refNo = rs.getString("tg_trips_ref_code");

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return refNo;
	}

	@Override
	public void deleteTripsGenerateDetailsOneData(String generateRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_trips_generator_det WHERE trips_ref_code=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, generateRefNo);
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
	public void deleteTripsGenerateDetailsTwoData(String generateRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_trips_generator_det02 WHERE trips_ref_code=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, generateRefNo);
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
	public TimeTableDTO getPanelStageStatus(String generateRefNo, String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  tg_route_no, tg_generator_ref_no, tg_with_fixed_time, tg_without_fixed_time,"
					+ " tg_is_fixed_buses "
					+ "FROM public.nt_m_trips_generator where tg_generator_ref_no=? and tg_route_no=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, generateRefNo);
			ps.setString(2, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setWithFixedBusesCode(rs.getString("tg_is_fixed_buses"));
				dto.setWithOutFixedTimeCode(rs.getString("tg_without_fixed_time"));
				dto.setWithFixedTimeCode(rs.getString("tg_with_fixed_time"));
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public boolean checkProcessPath(String generateRefNo, String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isFound = false;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT * FROM public.nt_m_timetable_generator WHERE generator_ref_no=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, generateRefNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isFound = true;
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

	@Override
	public boolean insertTimeTableGeneratorDetDataForHistorySave(List<TimeTableDTO> list, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataEntered = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			for (int i = 0; i < list.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_timetable_generator");

				String query = "INSERT INTO public.nt_h_timetable_generator "
						+ "(seq_no, seq_master, generator_ref_no, bus_num, start_time_slot, end_time_slot, assigned_bus_no, "
						+ "trip_id, is_fixed_time, group_no, trip_type, created_by, created_date,"
						+ " modified_by_without_fixed_time, modified_date_without_fixed_time) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ); ";

				ps = con.prepareStatement(query);

				ps.setLong(1, seqNo);
				ps.setLong(2, list.get(i).getMasterSeq());
				ps.setString(3, list.get(i).getGenereatedRefNo());
				ps.setString(4, list.get(i).getBusNo());
				ps.setString(5, list.get(i).getStartTime());
				ps.setString(6, list.get(i).getEndTime());
				ps.setString(7, list.get(i).getAssigneBusNo());
				ps.setInt(8, list.get(i).getTripIdInt());
				ps.setString(9, list.get(i).getWithFixedTimeCode());
				ps.setString(10, list.get(i).getGroup());
				ps.setString(11, list.get(i).getTripType());
				ps.setString(12, list.get(i).getCreatedBy());
				ps.setTimestamp(13, list.get(i).getCreatedDate());

				ps.setString(14, user);
				ps.setTimestamp(15, timestamp);

				int c = ps.executeUpdate();

				if (c > 0) {
					isDataEntered = true;
				}
			}

			con.commit();

		}catch (Exception e) {
			e.printStackTrace();
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

		return isDataEntered;
	}

	private boolean updateTimeTableGeneratorDetData(List<TimeTableDTO> list, String user, String type, String groupNo,
			String generatedRefNo, Connection con) throws Exception {

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataEntered = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String query = null;

		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getAssigneBusNo().equals("SLTB-O") || list.get(i).getAssigneBusNo().equals("SLTB-D")
					|| list.get(i).getAssigneBusNo().equals("ETC-O") || list.get(i).getAssigneBusNo().equals("ETC-D")) {

				query = "UPDATE public.nt_m_timetable_generator_det "
						+ "SET assigned_bus_no=?, created_by_assigned_bus=?, created_date_assigned_bus=? ,is_fixed_time =? "
						+ "WHERE generator_ref_no=? and group_no=? and trip_type=? and seq_no=?;";

				ps = con.prepareStatement(query);

				ps.setString(1, list.get(i).getAssigneBusNo());
				ps.setString(2, user);
				ps.setTimestamp(3, timestamp);
				ps.setString(4, "Y");
				ps.setString(5, generatedRefNo);
				ps.setString(6, groupNo);
				ps.setString(7, type);
				ps.setLong(8, list.get(i).getTimeTableDetSeq());

				int c = ps.executeUpdate();

				if (c > 0) {
					isDataEntered = true;
				}

			} else {

				query = "UPDATE public.nt_m_timetable_generator_det "
						+ "SET assigned_bus_no=?, created_by_assigned_bus=?, created_date_assigned_bus=? "
						+ "WHERE generator_ref_no=? and group_no=? and trip_type=? and seq_no=?;";

				ps = con.prepareStatement(query);

				ps.setString(1, list.get(i).getAssigneBusNo());
				ps.setString(2, user);
				ps.setTimestamp(3, timestamp);
				ps.setString(4, generatedRefNo);
				ps.setString(5, groupNo);
				ps.setString(6, type);
				ps.setLong(7, list.get(i).getTimeTableDetSeq());

				int c = ps.executeUpdate();

				if (c > 0) {
					isDataEntered = true;
				}

			}

		}

		ConnectionManager.close(rs);
		ConnectionManager.close(ps);

		return isDataEntered;
	}

	@Override
	public List<TimeTableDTO> getRouteNoListFromTripsGen() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> routeNoList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select  distinct tg_route_no\r\n" + "from public.nt_m_trips_generator ";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setRouteNo(rs.getString("tg_route_no"));
				routeNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeNoList;
	}

	@Override
	public ArrayList<String> isGroup(String refNo, String tripSide) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String group = null;
		ArrayList<String> dtoList = new ArrayList<>();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct group_no  from public.nt_t_trips_generator_det where trips_ref_code =? and trip_type=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, tripSide);
			rs = ps.executeQuery();

			while (rs.next()) {
				group = rs.getString("group_no");
				dtoList.add(group);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dtoList;
	}

	private boolean insertTimeSlots(List<TimeTableDTO> timeStartList, String refNo, String tripType, String groupNo,
			String user, Connection con) throws Exception {

		PreparedStatement ps, ps1 = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		int i = 0;
		long masterSeq = 0;
		boolean returnVal = false;

		String query1 = "select seq_no from public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and trip_type=?";

		ps1 = con.prepareStatement(query1);
		ps1.setString(1, refNo);
		ps1.setString(2, groupNo);
		ps1.setString(3, tripType);
		rs = ps1.executeQuery();

		while (rs.next()) {
			masterSeq = rs.getLong("seq_no");
		}

		String query = "INSERT INTO public.nt_m_timetable_generator_det\r\n"
				+ "(seq_no, seq_master, generator_ref_no, bus_num, start_time_slot, end_time_slot, trip_id, is_fixed_time, group_no, trip_type, created_by, created_date,is_pvt_bus)\r\n"
				+ "VALUES(?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";

		ps = con.prepareStatement(query);
		for (TimeTableDTO tTable : timeStartList) {
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_timetable_generator_det");
			i = i + 1;
			ps.setLong(1, seqNo);
			ps.setLong(2, masterSeq);
			ps.setString(3, refNo);

			if (tTable.getBusType() != null) {
				if (tTable.getBusType().equals("null")) {
					ps.setString(4, null);
				} else {
					ps.setString(4, tTable.getBusType());
				}
			} else {
				ps.setString(4, tTable.getBusType());
			}

			ps.setString(5, String.valueOf(tTable.getTimeStartVal()));
			ps.setString(6, String.valueOf(tTable.getTimeEndVal()));
			ps.setInt(7, i);

			if (tTable.getBusType() != null) {

				if (tTable.getBusType().equals("null")) {

					ps.setString(8, "N");
				} else {
					ps.setString(8, "Y");
				}
			} else {
				ps.setString(8, "N");
			}
			ps.setString(9, groupNo);
			ps.setString(10, tripType);
			ps.setString(11, user);
			ps.setTimestamp(12, timestamp);
			if (tTable.getBusType() == null || tTable.getBusType().equalsIgnoreCase("SLTB-O")
					|| tTable.getBusType().equalsIgnoreCase("SLTB-D") || tTable.getBusType().equalsIgnoreCase("ETC-O")
					|| tTable.getBusType().equalsIgnoreCase("ETC-D") || tTable.getBusType().equals("null")) {
				ps.setString(13, "N");
			} else {
				ps.setString(13, "Y");
			}

			ps.addBatch();
			int update = ps.executeUpdate();

			if (update > 0) {
				returnVal = true;
			}

		}

		ConnectionManager.close(rs);
		ConnectionManager.close(ps1);

		return returnVal;

	}

	@Override
	public TimeTableDTO getAbriviatiosForRoute(String routeNo, String busCat) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select rc_abbriviation_ltr_start , rc_abbriviation_ltr_end  from  public.nt_t_route_creator where rc_route_no =? and  rc_bus_type =? ";

			ps = con.prepareStatement(query2);

			ps.setString(1, routeNo);
			ps.setString(2, busCat);
			rs = ps.executeQuery();

			while (rs.next()) {
				abbDes = rs.getString("rc_abbriviation_ltr_end");
				abbOri = rs.getString("rc_abbriviation_ltr_start");
				dto.setAbbriAtOrigin(rs.getString("rc_abbriviation_ltr_start"));
				dto.setAbbriAtDestination(rs.getString("rc_abbriviation_ltr_end"));
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

//	@Override
//	public String getRealtedAbbreviation(String refNo, String routeNo) {
//		Connection con = null;
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		String abbreviation;
//		try {
//			con = ConnectionManager.getConnection();
//
//			String query2 = "select rc_abbriviation_ltr_start , rc_abbriviation_ltr_end  from  public.nt_t_route_creator where rc_route_no =? and  rc_bus_type =? ";
//
//			ps = con.prepareStatement(query2);
//
//			ps.setString(1, routeNo);
//			ps.setString(2, busCat);
//			rs = ps.executeQuery();
//
//			while (rs.next()) {
//
//				dto.setAbbriAtOrigin(rs.getString("rc_abbriviation_ltr_start"));
//				dto.setAbbriAtDestination(rs.getString("rc_abbriviation_ltr_end"));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//			return null;
//		} finally {
//			ConnectionManager.close(rs);
//			ConnectionManager.close(ps);
//			ConnectionManager.close(con);
//		}
//
//		return abbreviation;
//	}

	private boolean saveNoOfTrips(int noOFTripsValGOne, String refNo, String groupNo, String tripType, String loginUSer,
			String tTRefNo, Connection con) throws Exception {

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSave = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_timetable_generator");

		String query = "INSERT INTO public.nt_m_timetable_generator\r\n"
				+ "(seq_no, generator_ref_no, no_of_trips_available, created_by, created_date, group_no, trip_type,timetable_ref_no,status )\r\n"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?,?,?);";

		ps = con.prepareStatement(query);

		ps.setLong(1, seqNo);
		ps.setString(2, refNo);
		ps.setInt(3, noOFTripsValGOne);
		ps.setString(4, loginUSer);
		ps.setTimestamp(5, timestamp);
		ps.setString(6, groupNo);
		ps.setString(7, tripType);
		ps.setString(8, tTRefNo);
		ps.setString(9, "A");

		int insert = ps.executeUpdate();

		if (insert > 0) {
			isDataSave = true;
		}

		ConnectionManager.close(rs);
		ConnectionManager.close(ps);

		return isDataSave;
	}

	@Override
	public List<TimeTableDTO> getBusCategoryList(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> routeNoList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select distinct bus_category,  nt_r_service_types.description FROM public.nt_m_panelgenerator "
					+ "inner join public.nt_r_service_types on nt_r_service_types.code=nt_m_panelgenerator.bus_category "
					+ "where status='A' and  route_no=? order by description";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setBusCategory(rs.getString("bus_category"));
				e.setBusCategoryDes(rs.getString("description"));
				routeNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeNoList;
	}

	@Override
	public String getPanelGenNo(String routeNo, String category) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strRefNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select ref_no from nt_m_panelgenerator "
					+ " where route_no =? and bus_category =? and status ='A' ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, category);
			rs = ps.executeQuery();

			while (rs.next()) {
				strRefNo = (rs.getString("ref_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return strRefNo;
	}

	@Override
	public String generateTimeTableRefNo(String group, String tripNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strTimeTableRef = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT timetable_ref_no  FROM public.nt_m_timetable_generator WHERE timetable_ref_no IS NOT NULL ORDER BY timetable_ref_no desc LIMIT 1 ";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				result = rs.getString("timetable_ref_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strTimeTableRef = "TT" + group + tripNo + currYear + ApprecordcountN;
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
					strTimeTableRef = "TT" + group + tripNo + currYear + ApprecordcountN;
				}
			} else
				strTimeTableRef = "TT" + group + tripNo + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return strTimeTableRef;

	}

	@Override
	public boolean dataAvailableForShow(String panelRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isHave = false;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select * from public.nt_m_timetable_generator where create_by_without_fixed_time  is null and created_date_without_fixed_time  is null and generator_ref_no=? and  status='A'";

			ps = con.prepareStatement(query2);
			ps.setString(1, panelRefNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isHave = true;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isHave;
	}

	@Override
	public List<TimeTableDTO> getInsertedData(String groupNo, String tripType, String panelRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> dataList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select * from public.nt_m_timetable_generator_det where generator_ref_no=? and group_no=? and trip_type=? order by start_time_slot ";

			ps = con.prepareStatement(query2);
			ps.setString(1, panelRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setTimeStartVal(rs.getString("start_time_slot"));
				e.setTimeEndVal(rs.getString("end_time_slot"));
				e.setBusType(rs.getString("bus_num"));
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

	private boolean updateTimeSlots(List<TimeTableDTO> timeStartList, String refNo, String tripType, String groupNo,
			String user, Connection con) throws Exception {

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDataSave = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String query = "UPDATE public.nt_m_timetable_generator_det\r\n"
				+ "SET  bus_num=? ,is_fixed_time=?,modify_by=?,modify_date=? where  start_time_slot=? and  end_time_slot=? and    group_no=? and  trip_type=? and\r\n"
				+ "generator_ref_no=?;";

		ps = con.prepareStatement(query);
		for (TimeTableDTO tTable : timeStartList) {
			if (tTable.getBusType() == null || tTable.getBusType().trim().isEmpty()
					|| tTable.getBusType().equals("null")) {
				ps.setString(1, null);
			} else {
				ps.setString(1, tTable.getBusType());
			}

			if (tTable.getBusType() != null) {

				if (tTable.getBusType().equals("null")) {

					ps.setString(2, "N");
				} else {
					ps.setString(2, "Y");
				}
			} else {
				ps.setString(2, "N");
			}
			ps.setString(3, user);

			ps.setTimestamp(4, timestamp);

			ps.setString(5, String.valueOf(tTable.getTimeStartVal()));
			ps.setString(6, String.valueOf(tTable.getTimeEndVal()));
			ps.setString(7, groupNo);
			ps.setString(8, tripType);
			ps.setString(9, refNo);

			ps.addBatch();
			int update = ps.executeUpdate();

			if (update > 0) {
				isDataSave = true;
			}

		}

		ConnectionManager.close(rs);
		ConnectionManager.close(ps);

		return isDataSave;
	}

	@Override
	public int getNoOfTripsavail(String pRefNo, String groupNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String tripCount = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select count(*) as countTrip from public.nt_m_timetable_generator_det where generator_ref_no=? and group_no=? and trip_type=? and bus_num is null ";

			ps = con.prepareStatement(query2);
			ps.setString(1, pRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				tripCount = rs.getString("countTrip");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return Integer.parseInt(tripCount);
	}

	private boolean updateNoOFtrips(String panelRef, String groupNo, String tripType, int trips, Connection con)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean returnVal = false;

		String query = "UPDATE public.nt_m_timetable_generator\r\n" + "SET  no_of_trips_available=? \r\n"
				+ "WHERE generator_ref_no=? and group_no=? and  trip_type=? ;";

		ps = con.prepareStatement(query);

		ps.setInt(1, trips);
		ps.setString(2, panelRef);
		ps.setString(3, groupNo);
		ps.setString(4, tripType);

		int update = ps.executeUpdate();

		if (update > 0) {
			returnVal = true;
		}

		ConnectionManager.close(rs);
		ConnectionManager.close(ps);

		return returnVal;
	}

	@Override
	public boolean availableForEdit(String panelRef) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isHave = false;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select * from public.nt_m_timetable_generator where create_by_without_fixed_time is  null and generator_ref_no=? and  status='A'";

			ps = con.prepareStatement(query2);
			ps.setString(1, panelRef);
			rs = ps.executeQuery();

			if (rs.next()) {
				isHave = true;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isHave;
	}

	@Override
	public TimeTableDTO getNoOfNonPrivateBusses(String tripRef, String group, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select no_of_ctb_bus ,no_of_other_bus,no_of_pvt_bus\r\n"
					+ "from public.nt_t_trips_generator_det02 where \r\n"
					+ "trips_ref_code =? and group_no =? and trip_type =?";

			ps = con.prepareStatement(query2);
			ps.setString(1, tripRef);
			ps.setString(2, group);
			ps.setString(3, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setNoOfCTBbueses(rs.getInt("no_of_ctb_bus"));
				dto.setNoOfOtherbuses(rs.getInt("no_of_other_bus"));
				dto.setNoOfPVTbueses(rs.getInt("no_of_pvt_bus"));

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public TimeTableDTO getLastEndTimeOfTimeRange(String tripRefNo, String groupNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO timeSlotDet = new TimeTableDTO();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select time_range_to from  public.nt_t_trips_generator_det "
					+ "where trips_ref_code=? and group_no=? and trip_type=? order by seq DESC limit 1";

			ps = con.prepareStatement(query2);
			ps.setString(1, tripRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);

			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				timeSlotDet.setEndTime(rs.getString("time_range_to"));

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return timeSlotDet;
	}

	@Override
	public List<TimeTableDTO> getAllBusNoForFixedBuses(String busRoute, String serviceType,
			String originDestinationFlag) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> busNoList = new ArrayList<TimeTableDTO>();
		String routeFlag = null;
		if (originDestinationFlag.equalsIgnoreCase("O")) {
			routeFlag = "N";

		} else if (originDestinationFlag.equalsIgnoreCase("D")) {
			routeFlag = "Y";
		}
		try {
			con = ConnectionManager.getConnection();

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -2);
			Date pyear = cal.getTime();

			String today = dateFormat.format(new Date());
			String yearBeforeToday = dateFormat.format(pyear);

			String query2 = "select pm_vehicle_regno, pm_permit_expire_date,pm_status,pm_route_no,pm_permit_no from public.nt_t_pm_application "
					+ "where pm_status in('A') and pm_route_flag ='" + routeFlag + "' and pm_route_no='" + busRoute
					+ "'  and pm_service_type='" + serviceType + "' " + "or (pm_status in('A') and pm_route_no='"
					+ busRoute + "' and pm_service_type='" + serviceType + "' " + " and pm_route_flag ='" + routeFlag
					+ "' and to_date(pm_permit_expire_date, 'DD/MM/YYYY') " + "BETWEEN to_date('" + yearBeforeToday
					+ "', 'DD/MM/YYYY') AND to_date('" + today + "', 'DD/MM/YYYY')) order by pm_permit_no";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setAssigneBusNo(rs.getString("pm_vehicle_regno") + "-" + rs.getString("pm_permit_expire_date"));
				busNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public String getAssignedBusnumber(String busNo, String generatedRefNo, String groupNum, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String assignedBus = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select assigned_bus_no from nt_m_timetable_generator_det where bus_num='" + busNo
					+ "' and bus_num is not null and " + " generator_ref_no='" + generatedRefNo + "' and group_no='"
					+ groupNum + "' and trip_type='" + tripType + "'";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				assignedBus = rs.getString("assigned_bus_no");
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return assignedBus;

	}

	@Override
	public boolean saveNightShiftBuses(LinkedList<TimeTableDTO> midnightShiftBusesOrigin, String routeNo,
			String genereatedRefNo, String group, String tripType, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean found = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();

			for (TimeTableDTO dto : midnightShiftBusesOrigin) {

				/** check data is available in database table start **/
				String select = "SELECT * FROM public.nt_m_nightshift_timetable_det where rotue_no=? and panel_generator_ref_no=? "
						+ " and trip_type=? and group_no=? and bus_trip_id=?";
				ps = con.prepareStatement(select);
				ps.setString(1, routeNo);
				ps.setString(2, genereatedRefNo);
				ps.setString(3, tripType);
				ps.setString(4, group);
				ps.setString(5, dto.getTripId());
				rs = ps.executeQuery();

				while (rs.next()) {
					found = true;
				}

				ConnectionManager.close(ps);
				ConnectionManager.close(rs);
				/** check data is available in database table end **/

				if (found) {
					// update data
					String update = "UPDATE public.nt_m_nightshift_timetable_det SET  bus_no=?, start_time=?, end_time=?, modified_by=?,modified_date=? "
							+ "WHERE rotue_no=? and  panel_generator_ref_no=? and trip_type=? and group_no=? and bus_trip_id=?";
					ps = con.prepareStatement(update);

					ps.setString(1, dto.getBusNo());
					ps.setString(2, dto.getStartTime());
					ps.setString(3, dto.getEndTime());
					ps.setString(4, loginUser);
					ps.setTimestamp(5, timestamp);
					ps.setString(6, routeNo);
					ps.setString(7, genereatedRefNo);
					ps.setString(8, tripType);
					ps.setString(9, group);
					ps.setString(10, dto.getTripId());

					ps.executeUpdate();
				} else {
					// insert data
					long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_nightshift_timetable_det");

					String insert = "INSERT INTO public.nt_m_nightshift_timetable_det (seq_no, rotue_no, panel_generator_ref_no, bus_no, "
							+ "start_time, end_time, trip_type, group_no, bus_trip_id, created_by, created_date) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					ps = con.prepareStatement(insert);

					ps.setLong(1, seqNo);
					ps.setString(2, routeNo);
					ps.setString(3, genereatedRefNo);
					ps.setString(4, dto.getBusNo());
					ps.setString(5, dto.getStartTime());
					ps.setString(6, dto.getEndTime());
					ps.setString(7, tripType);
					ps.setString(8, group);
					ps.setString(9, dto.getTripId());
					ps.setString(10, loginUser);
					ps.setTimestamp(11, timestamp);

					ps.executeUpdate();
				}
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return true;
	}

	@Override
	public void deleteNightShiftBuses(TimeTableDTO deleteDTO, String routeNo, String genereatedRefNo, String group,
			String tripType, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_m_nightshift_timetable_det "
					+ "WHERE  rotue_no=? and  panel_generator_ref_no=? and trip_type=? and group_no=? and bus_trip_id=?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, genereatedRefNo);
			ps.setString(3, tripType);
			ps.setString(4, group);
			ps.setString(5, deleteDTO.getTripId());
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

	}

	@Override
	public LinkedList<TimeTableDTO> getNightShiftBuses(String routeNo, String genereatedRefNo, String group,
			String tripType) {

		LinkedList<TimeTableDTO> busList = new LinkedList<TimeTableDTO>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			String select = "SELECT * FROM public.nt_m_nightshift_timetable_det where rotue_no=? and panel_generator_ref_no=? "
					+ " and trip_type=? and group_no=? ";
			ps = con.prepareStatement(select);
			ps.setString(1, routeNo);
			ps.setString(2, genereatedRefNo);
			ps.setString(3, tripType);
			ps.setString(4, group);
			rs = ps.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setBusNo(rs.getString("bus_no"));
				dto.setStartTime(rs.getString("start_time"));
				dto.setEndTime(rs.getString("end_time"));
				dto.setTripId(rs.getString("bus_trip_id"));
				busList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return busList;
	}

	@Override
	public List<TimeTableDTO> getBusNoByRouteAndCatForFixedBuses(String routeNo, String categoryCode, String order) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> busNoList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select pm_vehicle_regno from public.nt_t_pm_application  where pm_status in ('A','O') and pm_route_no=? and pm_service_type=?  order by pm_route_flag "
					+ order;

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, categoryCode);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setAssigneBusNo(rs.getString("pm_vehicle_regno"));
				busNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public List<TimeTableDTO> getTimeSlotDetForGroups(String ttRefNo, String groupNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> timeSlotDet = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select a.trip_type, a.time_range_from, a.time_range_to, a.time_range_frequency, a.no_of_bus "
					+ "from  public.nt_t_trips_generator_det a , public.nt_m_trips_generator b "
					+ "where a.trips_ref_code = b.tg_trips_ref_code and b.tg_generator_ref_no=? and a.group_no=? and a.trip_type=? and a.time_range_frequency!=0";
			ps = con.prepareStatement(query2);
			ps.setString(1, ttRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);

			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setTimeRangeStart(rs.getString("time_range_from"));
				e.setTimeRangeEnd(rs.getString("time_range_to"));
				e.setFrequency(rs.getInt("time_range_frequency"));
				e.setNoOfBuses(rs.getInt("no_of_bus"));
				e.setTripType(rs.getString("trip_type"));
				timeSlotDet.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return timeSlotDet;
	}

	@Override
	public TimeTableDTO getNoOfSltbEtcBusses(String tripRef, String group) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select sum(no_of_ctb_bus)as no_of_ctb_bus  ,sum(no_of_other_bus) as no_of_other_bus,sum(no_of_pvt_bus) as no_of_pvt_bus\r\n"
					+ "from public.nt_t_trips_generator_det02 where " + "trips_ref_code =? and group_no =? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, tripRef);
			ps.setString(2, group);

			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setNoOfCTBbueses(rs.getInt("no_of_ctb_bus"));
				dto.setNoOfOtherbuses(rs.getInt("no_of_other_bus"));

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public boolean checkFixedBuses(String refNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isHave = false;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select * from public.nt_m_timetable_generator_det where is_fixed_time ='Y' and generator_ref_no =? and trip_type =?";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, tripType);
			rs = ps.executeQuery();

			if (rs.next()) {
				isHave = true;
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isHave;
	}

	@Override
	public List<ChakreeyaPalaliDTO> getDataForReport(String refNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ChakreeyaPalaliDTO dto = new ChakreeyaPalaliDTO();

		List<ChakreeyaPalaliDTO> dtoList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select generator_ref_no, bus_num ,start_time_slot ,end_time_slot,trip_id as tripido,trip_type as triptypeo\r\n"
					+ "FROM public.nt_m_timetable_generator_det\r\n"
					+ "where generator_ref_no=?  and start_time_slot is not null \r\n"
					+ "and trip_type=? order by trip_id  ";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new ChakreeyaPalaliDTO();
				dto.setBusNoOrigin(rs.getString("bus_num"));
				dto.setStartTimeOrigin(rs.getString("start_time_slot"));
				dto.setEndTImeOrigin(rs.getString("end_time_slot"));
				dtoList.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			ConnectionManager.close(con);
		}

		return dtoList;

	}

	@Override
	public List<ChakreeyaPalaliDestiDTO> getDataForReportDestination(String refNo, String tripSide) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ChakreeyaPalaliDestiDTO dto1 = new ChakreeyaPalaliDestiDTO();
		List<ChakreeyaPalaliDestiDTO> dtoList2 = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select generator_ref_no, bus_num ,start_time_slot ,end_time_slot,trip_id as tripido,trip_type as triptypeo\r\n"
					+ "FROM public.nt_m_timetable_generator_det\r\n"
					+ "where generator_ref_no=?  and start_time_slot is not null\r\n"
					+ "and trip_type=? order by trip_id  ";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, tripSide);

			rs = ps.executeQuery();

			while (rs.next()) {

				dto1 = new ChakreeyaPalaliDestiDTO();
				dto1.setBusNoDestination(rs.getString("bus_num"));
				dto1.setStartTimeDestination(rs.getString("start_time_slot"));
				dto1.setEndTImeDestination(rs.getString("end_time_slot"));

				dtoList2.add(dto1);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			ConnectionManager.close(con);
		}

		return dtoList2;

	}

	@Override
	public String getBusCategoryDescription(String busCategoryCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String description = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_service_types where code=? and active='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, busCategoryCode);
			rs = ps.executeQuery();

			if (rs.next()) {
				description = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return description;
	}

	@Override
	public void fillRemainingBusNumbers(String groupNo, String genereatedRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct bus_num, assigned_bus_no FROM public.nt_m_timetable_generator_det "
					+ "WHERE generator_ref_no=? and group_no=? and assigned_bus_no is not null and assigned_bus_no !='' ";

//			 and assigned_bus_no not in ('SLTB-O','ETC-O','SLTB-D','ETC-D')
			ps = con.prepareStatement(query);
			ps.setString(1, genereatedRefNo);
			ps.setString(2, groupNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				String query2 = "UPDATE public.nt_m_timetable_generator_det "
						+ "SET assigned_bus_no=? WHERE generator_ref_no=? and group_no=? and bus_num =? and (assigned_bus_no is null or assigned_bus_no ='')";

				ps2 = con.prepareStatement(query2);
				ps2.setString(1, rs.getString("assigned_bus_no"));
				ps2.setString(2, genereatedRefNo);
				ps2.setString(3, groupNo);
				ps2.setString(4, rs.getString("bus_num"));
				ps2.executeUpdate();
				ConnectionManager.close(ps2);
			}

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
	}

	@Override
	public boolean updateTimeTableGeneratorDetData(List<TimeTableDTO> originList, List<TimeTableDTO> desList,
			String user, String groupNo, String generatedRefNo) {
		Connection con = null;
		boolean returnVal = false;
		try {
			con = ConnectionManager.getConnection();

			updateTimeTableGeneratorDetData(originList, user, "O", groupNo, generatedRefNo, con);
			updateTimeTableGeneratorDetData(desList, user, "D", groupNo, generatedRefNo, con);
			returnVal = true;

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		} finally {
			ConnectionManager.close(con);
		}
		return returnVal;
	}

	@Override
	public boolean savePanelGeneratorWithFixedTimeAllInOne(int noOFTripsValGOne, String panelGenNo, String groupNo,
			String loginUser, String timeTableRefNoOrigin, String timeTableRefNoDes,
			List<TimeTableDTO> timeStartListOrigin, List<TimeTableDTO> timeStartListDes) {
		Connection con = null;
		boolean returnVal = false;
		try {
			con = ConnectionManager.getConnection();

			// saveNoOfTrips
			boolean save1 = saveNoOfTrips(noOFTripsValGOne, panelGenNo, groupNo, "O", loginUser, timeTableRefNoOrigin,
					con);
			boolean save2 = saveNoOfTrips(noOFTripsValGOne, panelGenNo, groupNo, "D", loginUser, timeTableRefNoDes,
					con);

			// insertTimeSlots
			boolean save3 = insertTimeSlots(timeStartListOrigin, panelGenNo, "O", groupNo, loginUser, con);
			boolean save4 = insertTimeSlots(timeStartListDes, panelGenNo, "D", groupNo, loginUser, con);

			// updateNoOFtrips
			int noOfTripsVal = getNoOfTripsAvailable(panelGenNo, groupNo, "O", con);
			int noOfTripsValD = getNoOfTripsAvailable(panelGenNo, groupNo, "D", con);

			boolean save5 = updateNoOFtrips(panelGenNo, groupNo, "O", noOfTripsVal, con);
			boolean save6 = updateNoOFtrips(panelGenNo, groupNo, "D", noOfTripsValD, con);

			returnVal = true;

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		} finally {
			ConnectionManager.close(con);
		}
		return returnVal;
	}

	private int getNoOfTripsAvailable(String pRefNo, String groupNo, String tripType, Connection con) throws Exception {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String tripCount = null;

		String query2 = "select count(*) as countTrip from public.nt_m_timetable_generator_det where generator_ref_no=? and group_no=? and trip_type=? and bus_num is null ";

		ps = con.prepareStatement(query2);
		ps.setString(1, pRefNo);
		ps.setString(2, groupNo);
		ps.setString(3, tripType);
		rs = ps.executeQuery();

		while (rs.next()) {
			tripCount = rs.getString("countTrip");
		}

		ConnectionManager.close(rs);
		ConnectionManager.close(ps);

		return Integer.parseInt(tripCount);
	}

	@Override
	public boolean updatePanelGeneratorWithFixedTimeAllInOne(String panelGenNo, String groupNo, String loginUser,
			List<TimeTableDTO> timeStartListOrigin, List<TimeTableDTO> timeStartListDes) {
		Connection con = null;
		boolean returnVal = false;
		try {
			con = ConnectionManager.getConnection();

			// updateTimeSlots
			boolean save1 = updateTimeSlots(timeStartListOrigin, panelGenNo, "O", groupNo, loginUser, con);
			boolean save2 = updateTimeSlots(timeStartListDes, panelGenNo, "D", groupNo, loginUser, con);

			// updateNoOFtrips
			int noOfTripsVal = getNoOfTripsAvailable(panelGenNo, groupNo, "O", con);
			int noOfTripsValD = getNoOfTripsAvailable(panelGenNo, groupNo, "D", con);

			boolean save5 = updateNoOFtrips(panelGenNo, groupNo, "O", noOfTripsVal, con);
			boolean save6 = updateNoOFtrips(panelGenNo, groupNo, "D", noOfTripsValD, con);

			returnVal = true;

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			returnVal = false;
		} finally {
			ConnectionManager.close(con);
		}
		return returnVal;
	}

	@Override
	public void saveOriginTimeSlotTable(List<TimeTableDTO> originRouteList, String draft) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		int i = 0;
		try {

			con = ConnectionManager.getConnection();

			String query = "INSERT INTO panel_generator_origin_trip_details ("
					+ "ref_no, route_no, origin, abbreviation, permit_no, "
					+ "bus_no, start_time, end_time, rest_time, is_fixed, " 
					+ "createdBy, createddate,status,group_name,draftdata) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
			
			String queryUpdate = "UPDATE panel_generator_origin_trip_details SET draftdata = ? WHERE ref_no=? AND start_time=?";

			ps = con.prepareStatement(query);
			psUpdate = con.prepareStatement(queryUpdate);

			for (TimeTableDTO trip : originRouteList) {
				try {
					psUpdate.setString(1, draft);
					psUpdate.setString(2, trip.getGenereatedRefNo());
					psUpdate.setString(3, trip.getStartTimeOrigin());
					
					if(draft.equalsIgnoreCase("S")) {
						i = psUpdate.executeUpdate();
					}
					
					if(i == 0) {
						ps.setString(1, trip.getGenereatedRefNo());
						ps.setString(2, trip.getRouteNo());
						ps.setString(3, trip.getOrigin());
						ps.setString(4, trip.getAbbreviationOrigin());
						ps.setString(5, trip.getPermitNoOrigin());
						ps.setString(6, trip.getBusNoOrigin());
						ps.setString(7, trip.getStartTimeOrigin());

						ps.setString(8, trip.getEndTimeOrigin());
						ps.setString(9, trip.getRestTimeOrigin());
						ps.setBoolean(10, trip.isFixBusOrigin());
						
						ps.setString(11, trip.getUser());
						ps.setTimestamp(12, timestamp);
						ps.setBoolean(13, trip.isForSubmit()); //true for submit, false for save as draft
						ps.setString(14, trip.getGroupNo());
						ps.setString(15, draft);
						ps.executeUpdate();
					}			
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}
			}
			con.commit(); 
			System.out.println("trip list origin data saved Succesfully");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}
	
	@Override
	public void editOriginTimeSlotTable(TimeTableDTO panelGeneratorFormData, List<TimeTableDTO> originRouteList,
			String user,boolean isForSubmit,String refNo,String draft) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String updateQuery = "UPDATE panel_generator_origin_trip_details "
			        + "SET abbreviation = ?, permit_no = ?, "
			        + "bus_no = ?, start_time = ?, end_time = ?, rest_time = ?, is_fixed = ?, "
			        + "modifiedby = ?, modifieddate = ?, status = ?, group_name = ?, draftdata = ? "
			        + "WHERE ref_no=? AND id=?";

			ps = con.prepareStatement(updateQuery);

			for (TimeTableDTO trip : originRouteList) {
				try {
				
					ps.setString(1, trip.getAbbreviationOrigin());
					ps.setString(2, trip.getPermitNoOrigin());
					ps.setString(3, trip.getBusNoOrigin());
					ps.setString(4, trip.getStartTimeOrigin());

					ps.setString(5, trip.getEndTimeOrigin());
					ps.setString(6, trip.getRestTimeOrigin());
					ps.setBoolean(7, trip.isFixBusOrigin());
					
					ps.setString(8, user);
					ps.setTimestamp(9, timestamp);
					ps.setBoolean(10, isForSubmit); 
					ps.setString(11, panelGeneratorFormData.getGroupNo());
					ps.setString(12, draft);
					ps.setString(13, refNo);
					ps.setLong(14, trip.getSeq());
					ps.executeUpdate();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}
				
				
			}
			con.commit();
			System.out.println("trip list origin data updated Succesfully");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}
	
	@Override
	public void saveDestinationTimeSlotTable(List<TimeTableDTO> destinationRouteList, String draft) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		int i = 0;
		
		try {

			con = ConnectionManager.getConnection();

			String query = "INSERT INTO panel_generator_destination_trip_details ("
					+ "ref_no, route_no, origin, abbreviation, permit_no, "
					+ "bus_no, start_time, end_time, rest_time, is_fixed, " + "createdBy, createddate,status,group_name,draftdata) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
			
			String queryUpdate = "UPDATE panel_generator_destination_trip_details SET draftdata = ? WHERE ref_no=? AND start_time=?";

			ps = con.prepareStatement(query);
			psUpdate = con.prepareStatement(queryUpdate);

			for (TimeTableDTO trip : destinationRouteList) {
				try {
					psUpdate.setString(1, draft);
					psUpdate.setString(2, trip.getGenereatedRefNo());
					psUpdate.setString(3, trip.getStartTimeDestination());
					
					if(draft.equalsIgnoreCase("S")) {
						i = psUpdate.executeUpdate();
					}
					
					if(i==0) {
						ps.setString(1, trip.getGenereatedRefNo());
						ps.setString(2, trip.getRouteNo());
						ps.setString(3, trip.getOrigin());
						ps.setString(4, trip.getAbbreviationDestination());
						ps.setString(5, trip.getPermitNoDestination());
						ps.setString(6, trip.getBusNoDestination());
						ps.setString(7, trip.getStartTimeDestination());

						ps.setString(8, trip.getEndTimeDestination());
						ps.setString(9, trip.getRestTimeDestination());
						ps.setBoolean(10, trip.isFixBusDestination());
						
						ps.setString(11, trip.getUser());
						ps.setTimestamp(12, timestamp);
						ps.setBoolean(13, trip.isForSubmit()); //true for submit, false for save as draft
						ps.setString(14, trip.getGroupNo());
						ps.setString(15, draft);
						ps.executeUpdate();				
					}
					
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}
			}
			con.commit();
			System.out.println("trip list destination data saved Succesfully");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}
	
	@Override
	public void editDestinationTimeSlotTable(TimeTableDTO panelGeneratorFormData, List<TimeTableDTO> destinationRouteList,
			String user, boolean isForSubmit, String refNo,String draft) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String updateQuery = "UPDATE panel_generator_destination_trip_details "
			        + "SET abbreviation = ?, permit_no = ?, "
			        + "bus_no = ?, start_time = ?, end_time = ?, rest_time = ?, is_fixed = ?, "
			        + "modifiedby = ?, modifieddate = ?, status = ?, group_name = ?,draftdata = ? "
			        + "WHERE ref_no=? AND id=?";

			ps = con.prepareStatement(updateQuery);

			for (TimeTableDTO trip : destinationRouteList) {
				try {
				
					ps.setString(1, trip.getAbbreviationDestination());
					ps.setString(2, trip.getPermitNoDestination());
					ps.setString(3, trip.getBusNoDestination());
					ps.setString(4, trip.getStartTimeDestination());

					ps.setString(5, trip.getEndTimeDestination());
					ps.setString(6, trip.getRestTimeDestination());
					ps.setBoolean(7, trip.isFixBusDestination());
					
					ps.setString(8, user);
					ps.setTimestamp(9, timestamp);
					ps.setBoolean(10, isForSubmit); 
					ps.setString(11, panelGeneratorFormData.getGroupNo());
					ps.setString(12, draft);
					ps.setString(13, refNo);
					ps.setLong(14, trip.getSeq());
					ps.executeUpdate();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}
			}
			
			con.commit();

			System.out.println("trip list destination data updated Succesfully");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}
	@Override
	public void saveLeaveBusesTimeSlotTable(List<TimeTableDTO> leaveBusList, List<TimeTableDTO> leaveBusListDes,String draft) {
	    Connection con = null;
	    PreparedStatement ps = null;
	    PreparedStatement psUpdate = null;
	    PreparedStatement psUpdateDes = null;
	    ResultSet rs = null;
	    java.util.Date date = new java.util.Date();
	    Timestamp timestamp = new Timestamp(date.getTime());

	    try {
	        con = ConnectionManager.getConnection();

	        String query = "INSERT INTO panel_generator_leave_bus_details ("
	            + "ref_no, route_no, origin, abbreviation, permit_no, "
	            + "bus_no, createdBy, createddate, status, abbrivationdes, permit_no_des, bus_no_des,draftdata) "
	            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        
	        String queryUpdate = "UPDATE panel_generator_leave_bus_details SET draftdata = ? WHERE ref_no=? AND permit_no=? ";
	        String queryUpdateDes = "UPDATE panel_generator_leave_bus_details SET draftdata = ? WHERE ref_no=? AND permit_no_des=?";
	        
	        ps = con.prepareStatement(query);
	        psUpdate = con.prepareStatement(queryUpdate);
	        psUpdateDes = con.prepareStatement(queryUpdateDes);

	        int listSize =leaveBusList.size() + leaveBusListDes.size();
	        
	        for (int i = 0; i < listSize; i++) {	            
	            if(i < leaveBusList.size()) {
	            	try {
	            		TimeTableDTO trip =leaveBusList.get(i);
	            		psUpdate.setString(1, draft);
						psUpdate.setString(2, trip.getGenereatedRefNo());
						psUpdate.setString(3, trip.getPermitNoLeave());
						
						int updateCount = psUpdate.executeUpdate();
						
						if(updateCount == 0) {
							ps.setString(1, trip.getGenereatedRefNo());
		                    ps.setString(2, trip.getRouteNo());
		                    ps.setString(3, trip.getOrigin());
		                    ps.setString(4, trip.getAbbreviationLeave());
		                    ps.setString(5, trip.getPermitNoLeave());
		                    ps.setString(6, trip.getBusNoLeave());
		                    ps.setString(7, trip.getUser());
		                    ps.setTimestamp(8, timestamp);
		                    ps.setBoolean(9, trip.isForSubmit());
		                    ps.setNull(10, Types.VARCHAR);
		                    ps.setNull(11, Types.VARCHAR);
		                    ps.setNull(12, Types.VARCHAR);
		                    ps.setString(13, draft);
		                    
		                    ps.executeUpdate();
						}	
	
	            	} catch (SQLException e) {
		                con.rollback();
		                e.printStackTrace();
		            }
	            }else {
	            	TimeTableDTO desTrip = leaveBusListDes.get(i - leaveBusList.size());
	            	try {
	            		psUpdateDes.setString(1, draft);
						psUpdateDes.setString(2, desTrip.getGenereatedRefNo());
						psUpdateDes.setString(3, desTrip.getPermitNoLeaveDes());
						
						int updateCount = psUpdateDes.executeUpdate();
						
						if(updateCount == 0) {
					 		ps.setString(1, desTrip.getGenereatedRefNo());
		                    ps.setString(2, desTrip.getRouteNo());
		                    ps.setString(3, desTrip.getOrigin());
		                    ps.setNull(4, Types.VARCHAR);
		                    ps.setNull(5, Types.VARCHAR);
		                    ps.setNull(6, Types.VARCHAR);
		                    ps.setString(7, desTrip.getUser());
		                    ps.setTimestamp(8, timestamp);
		                    ps.setBoolean(9, desTrip.isForSubmit());
		                    ps.setString(10, desTrip.getAbbreviationLeaveDes());
			                ps.setString(11, desTrip.getPermitNoLeaveDes());
			                ps.setString(12, desTrip.getBusNoLeaveDes());
			                ps.setString(13, draft);
			                
			                ps.executeUpdate();
						}
	           
	            	} catch (SQLException e) {
		                con.rollback();
		                e.printStackTrace();
		            }
	            }
	        }
	        
	        con.commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // Close the resources in the reverse order they were opened
	        ConnectionManager.close(ps);
	        ConnectionManager.close(con);
	    }
	}

	
	@Override
	public void editLeaveBusesTimeSlotTable(TimeTableDTO panelGeneratorFormData, List<TimeTableDTO> leaveBusList,List<TimeTableDTO> leaveBusListDes,
			String user, boolean isForSubmit, String refNo,String draft) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String updateQuery = "UPDATE panel_generator_leave_bus_details "
			        + "SET abbreviation = ?, permit_no = ?, "
			        + "bus_no = ?, "
			        + "modifiedby = ?, modifieddate = ?, status = ?, "
			        + "abbrivationdes = ?, permit_no_des = ?, bus_no_des = ?,draftdata = ?"
			        + "WHERE ref_no = ? AND id = ?";

			ps = con.prepareStatement(updateQuery);
			
			int listSize =leaveBusList.size() + leaveBusListDes.size();

		   for (int i = 0; i < listSize; i++) {
			   if(i < leaveBusList.size()) {
	            	try {
	            		TimeTableDTO trip =leaveBusList.get(i);
	            		
	            		ps.setString(1, trip.getAbbreviationLeave());
						ps.setString(2, trip.getPermitNoLeave());
						ps.setString(3, trip.getBusNoLeave());					
						ps.setString(4, user);
						ps.setTimestamp(5, timestamp);
						ps.setBoolean(6, isForSubmit); 
						ps.setNull(7, Types.VARCHAR);
		                ps.setNull(8, Types.VARCHAR);
		                ps.setNull(9, Types.VARCHAR);
		                ps.setString(10, draft);
		                ps.setString(11, refNo);
		                ps.setLong(12, trip.getSeq());
						
		                ps.executeUpdate();		                
	            	} catch (SQLException e) {
		                con.rollback();
		                e.printStackTrace();
		            }
			   }else {
				   try {
					   TimeTableDTO desTrip = leaveBusListDes.get(i - leaveBusList.size());
					   
					   ps.setNull(1, Types.VARCHAR);
		               ps.setNull(2, Types.VARCHAR);
		               ps.setNull(3, Types.VARCHAR);					
		               ps.setString(4, user);
		               ps.setTimestamp(5, timestamp);
		               ps.setBoolean(6, isForSubmit);
		               ps.setString(7, desTrip.getAbbreviationLeaveDes());
		               ps.setString(8, desTrip.getPermitNoLeaveDes());
		               ps.setString(9, desTrip.getBusNoLeaveDes());
		               ps.setString(10, draft);
		               ps.setString(11, refNo);
		               ps.setLong(12, desTrip.getSeq());
						
		               ps.executeUpdate();					   
				   }catch (SQLException e) {
		                con.rollback();
		                e.printStackTrace();
		            }	   
			   }
		   }
		   con.commit();
		 } catch (Exception e) {
		        e.printStackTrace();
		 } finally {
		        // Close the resources in the reverse order they were opened
		        ConnectionManager.close(ps);
		        ConnectionManager.close(con);
		 }
	}

	@Override
	public List<String> getPermitNoList(String routeNo,boolean side,String group,String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<>();
		String permitNo;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rs_permit_no FROM public.nt_t_route_setup_timetable where rs_route_no= ? and "
					+ "rs_route_flag= ? and rs_status in ('A','O') and rs_group_no = ? and rs_use_time_table = true and rs_service_type = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setBoolean(2, side);
			ps.setString(3, group);
			ps.setString(4, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitNo = rs.getString("rs_permit_no");
				list.add(permitNo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public List<String> getBusNoList(String routeNo,boolean side,String group,String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<>();
		String busNo;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rs_vehicle_regno FROM public.nt_t_route_setup_timetable where rs_route_no= ? "
					+ "and rs_route_flag= ? and rs_status in ('A','O') and rs_group_no = ? and rs_use_time_table = true and rs_service_type = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setBoolean(2, side);
			ps.setString(3, group);
			ps.setString(4, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				busNo = rs.getString("rs_vehicle_regno");
				list.add(busNo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public String updateBusNo(String permitNo, String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String busNo = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rs_vehicle_regno FROM public.nt_t_route_setup_timetable where rs_permit_no= ? and rs_status in ('A','O') and rs_route_no= ?";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			ps.setString(2, routeNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				busNo = rs.getString("rs_vehicle_regno");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busNo;
	}

	@Override
	public String updatePermitNo(String busNo, String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String permitNo = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rs_permit_no FROM public.nt_t_route_setup_timetable where rs_vehicle_regno= ? and rs_status in ('A','O') and rs_route_no= ?";

			ps = con.prepareStatement(query);
			ps.setString(1, busNo);
			ps.setString(2, routeNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				permitNo = rs.getString("rs_permit_no");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNo;
	}

	@Override
	public void saveGeneralDetailsPanelGenerator(TimeTableDTO panelGeneratorFormData, String draft) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String queryInsert = "INSERT INTO panel_generator_general_data ("
					+ "refno, routeno, groupcount, buscategory, origin, destination, isdailyrotation, "
					+ "istwodayrotation, nooftripsorigin, noofprivatebusesorigin, nooftemporarybusesorigin, "
					+ "noofctborigin, noofetcorigin, noofprivateleavebusesorigin, noofdummybusesorigin, "
					+ "resttimeorigin, totalbusesorigin, nooftripsdestination, noofprivatebusesdestination, "
					+ "nooftemporarybusesdestination, noofctbdestination, noofetcdestination, "
					+ "noofprivateleavebusesdestination, noofdummybusesdestination, resttimedestination, "
					+ "totalbusesdestination, createdby, createddate, state, time_tables_per_week,draftdata,coupletwo) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? )";
			
			String queryUpdate = "UPDATE panel_generator_general_data SET draftdata = ? WHERE refno = ?";

			ps = con.prepareStatement(queryInsert);
			psUpdate = con.prepareStatement(queryUpdate);
			try {
				psUpdate.setString(1, draft);
				psUpdate.setString(2, panelGeneratorFormData.getGenereatedRefNo());
				
				int i = psUpdate.executeUpdate();
				
				if(i == 0) {
					ps.setString(1, panelGeneratorFormData.getGenereatedRefNo());
					ps.setString(2, panelGeneratorFormData.getRouteNo());
					ps.setString(3, panelGeneratorFormData.getGroupNo());
					ps.setString(4, panelGeneratorFormData.getBusCategory());
					ps.setString(5, panelGeneratorFormData.getOrigin());
					ps.setString(6, panelGeneratorFormData.getDestination());
					ps.setBoolean(7, panelGeneratorFormData.getIsDailyRotation());
					ps.setBoolean(8, panelGeneratorFormData.getIsTwoDayRotation());

					ps.setInt(9, panelGeneratorFormData.getNoOfTripsOrigin());
					ps.setInt(10, panelGeneratorFormData.getNoOfPvtBusesOrigin());
					ps.setInt(11, panelGeneratorFormData.getNoOfTemporaryBusesOrigin());
					ps.setInt(12, panelGeneratorFormData.getNoOfCtbOrigin());
					ps.setInt(13, panelGeneratorFormData.getNoOfEtcOrigin());
					ps.setInt(14, panelGeneratorFormData.getNoOfPvtLeaveBusesOrigin());
					ps.setInt(15, panelGeneratorFormData.getNoOfDummyBusesOrigin());
					ps.setString(16, panelGeneratorFormData.getRestTimeOrigin());
					ps.setInt(17, panelGeneratorFormData.getTotalBusesOrigin());

					ps.setInt(18, panelGeneratorFormData.getNoOfTripsDestination());
					ps.setInt(19, panelGeneratorFormData.getNoOfPvtBusesDestination());
					ps.setInt(20, panelGeneratorFormData.getNoOfTemporaryBusesDestination());
					ps.setInt(21, panelGeneratorFormData.getNoOfCtbDestination());
					ps.setInt(22, panelGeneratorFormData.getNoOfEtcDestination());
					ps.setInt(23, panelGeneratorFormData.getNoOfPvtLeaveBusesDestination());
					ps.setInt(24, panelGeneratorFormData.getNoOfDummyBusesDestination());
					ps.setString(25, panelGeneratorFormData.getRestTimeDestination());
					ps.setInt(26, panelGeneratorFormData.getTotalBusesDestination());

					ps.setString(27, panelGeneratorFormData.getUser());
					ps.setTimestamp(28, timestamp);
					ps.setBoolean(29, panelGeneratorFormData.isForSubmit()); // true=for submit, false= for save 
					ps.setString(30, panelGeneratorFormData.getNoOfTimeTablesPerWeek());
					ps.setString(31, draft);
					ps.setBoolean(32, panelGeneratorFormData.isCoupleTwo());

					ps.executeUpdate();
				}
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
			
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
	public void saveGeneralDetailsEditPanelGenerator(TimeTableDTO panelGeneratorFormData, String user, boolean isForSubmit, String refNo,String draft) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String updateQuery = "UPDATE panel_generator_general_data "
			        + "SET isdailyrotation = ?, istwodayrotation = ?, nooftripsorigin = ?, noofprivatebusesorigin = ?,"
			        + "nooftemporarybusesorigin = ?, noofctborigin = ?, noofetcorigin = ?, noofprivateleavebusesorigin = ?, "
			        + "noofdummybusesorigin = ?, resttimeorigin = ?, totalbusesorigin = ?, nooftripsdestination = ?, "
			        + "noofprivatebusesdestination = ?, nooftemporarybusesdestination = ?, noofctbdestination = ?, "
			        + "noofetcdestination = ?, noofprivateleavebusesdestination = ?, noofdummybusesdestination = ?, "
			        + "resttimedestination = ?, totalbusesdestination = ?, modifiedby = ?, modifieddate = ?, state = ?,draftdata = ?, coupletwo = ? "
			        + "WHERE refno = ?"; 

			ps = con.prepareStatement(updateQuery);

			try {
				ps.setBoolean(1, panelGeneratorFormData.getIsDailyRotation());
				ps.setBoolean(2, panelGeneratorFormData.getIsTwoDayRotation());

				ps.setInt(3, panelGeneratorFormData.getNoOfTripsOrigin());
				ps.setInt(4, panelGeneratorFormData.getNoOfPvtBusesOrigin());
				ps.setInt(5, panelGeneratorFormData.getNoOfTemporaryBusesOrigin());
				ps.setInt(6, panelGeneratorFormData.getNoOfCtbOrigin());
				ps.setInt(7, panelGeneratorFormData.getNoOfEtcOrigin());
				ps.setInt(8, panelGeneratorFormData.getNoOfPvtLeaveBusesOrigin());
				ps.setInt(9, panelGeneratorFormData.getNoOfDummyBusesOrigin());
				ps.setString(10, panelGeneratorFormData.getRestTimeOrigin());
				ps.setInt(11, panelGeneratorFormData.getTotalBusesOrigin());

				ps.setInt(12, panelGeneratorFormData.getNoOfTripsDestination());
				ps.setInt(13, panelGeneratorFormData.getNoOfPvtBusesDestination());
				ps.setInt(14, panelGeneratorFormData.getNoOfTemporaryBusesDestination());
				ps.setInt(15, panelGeneratorFormData.getNoOfCtbDestination());
				ps.setInt(16, panelGeneratorFormData.getNoOfEtcDestination());
				ps.setInt(17, panelGeneratorFormData.getNoOfPvtLeaveBusesDestination());
				ps.setInt(18, panelGeneratorFormData.getNoOfDummyBusesDestination());
				ps.setString(19, panelGeneratorFormData.getRestTimeDestination());
				ps.setInt(20, panelGeneratorFormData.getTotalBusesDestination());

				ps.setString(21, user);
				ps.setTimestamp(22, timestamp);
				ps.setBoolean(23, isForSubmit); // true=for submit, false= for save 
				ps.setString(24, draft);
				ps.setBoolean(25, panelGeneratorFormData.isCoupleTwo());
				ps.setString(26, refNo);
				ps.executeUpdate();
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
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
	public String getTravelTime(String routeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String travelTime = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT rc_travel_time FROM public.nt_t_route_creator where rc_route_no=? and rc_status='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				travelTime = rs.getString("rc_travel_time");
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return travelTime;
	}
	
	@Override
	public List<String> getReferenceNumbers(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String refNo = null;
		List<String> refNoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT ref_no FROM public.nt_m_panelgenerator order by ref_no desc";

			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();

			while (rs.next()) {
				refNo = rs.getString("ref_no");
				refNoList.add(refNo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return refNoList;
	}
	
	@Override
	public TimeTableDTO getRouteDataForPanelGenerator(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  nt_m_panelgenerator.route_no,nt_m_panelgenerator.bus_category,"
					+ "nt_m_panelgenerator.no_of_bus_perweek,nt_r_route.rou_service_origine,"
					+ "nt_r_route.rou_service_destination,nt_t_panelgenerator_det.group_no, "
					+ "nt_m_panelgenerator.seq "
					+ "FROM nt_m_panelgenerator "
					+ "inner join nt_r_route on nt_r_route.rou_number=nt_m_panelgenerator.route_no "
					+ "inner join nt_t_panelgenerator_det on nt_t_panelgenerator_det.seq_panelgenerator=nt_m_panelgenerator.seq "
					+ "where nt_m_panelgenerator.status='A' and nt_m_panelgenerator.ref_no=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setSeq(rs.getLong("seq"));
				dto.setDestination(rs.getString("rou_service_destination"));
				dto.setOrigin(rs.getString("rou_service_origine"));
				dto.setRouteNo(rs.getString("route_no"));
				dto.setBusCategory(rs.getString("bus_category"));
				dto.setNoOfTimeTablesPerWeek(rs.getString("no_of_bus_perweek"));
				dto.setRouteStatus("Active");
				dto.setGenereatedRefNo(refNo);
				dto.setGroupNo(rs.getString("group_no"));
				dto.setStatus("Active");
				if (rs.getString("bus_category").equals("001")) {

					dto.setBusCategoryDes("NORMAL");
				}

				else if (rs.getString("bus_category").equals("002")) {
					dto.setBusCategoryDes("LUXURY");

				}

				else if (rs.getString("bus_category").equals("003")) {
					dto.setBusCategoryDes("SUPER LUXURY");

				} else if (rs.getString("bus_category").equals("004")) {
					dto.setBusCategoryDes("SEMI-LUXURY");

				} else if (rs.getString("bus_category").equals("EB")) {
					dto.setBusCategoryDes("EXPRESSWAY BUS");

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

		return dto;
	}
	
	public boolean validateRefNo(String refNo) {
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String tempRefNo = null;
	    String draftdata = null;
	    List<String> refNoList = new ArrayList<>();
	    try {

	        con = ConnectionManager.getConnection();

	        String query = "SELECT refno,draftdata FROM panel_generator_general_data";

	        ps = con.prepareStatement(query);

	        rs = ps.executeQuery();

	        while (rs.next()) {
	            tempRefNo = rs.getString("refno");
	            if (tempRefNo.equalsIgnoreCase(refNo)) {
	                System.out.println("ref number already assigned");
	                return false; // If refNo is found, it means it's not valid
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false; // Return false if an exception occurs
	    } finally {
	        ConnectionManager.close(rs);
	        ConnectionManager.close(ps);
	        ConnectionManager.close(con);
	    }

	    // If the loop completes without finding a matching refNo, it is valid
	    return true;
	}
	
	public boolean validateRefNoForSave(String refNo) {
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String tempRefNo = null;
	    String draftdata = null;
	    List<String> refNoList = new ArrayList<>();
	    try {

	        con = ConnectionManager.getConnection();

	        String query = "SELECT refno,draftdata FROM panel_generator_general_data";

	        ps = con.prepareStatement(query);

	        rs = ps.executeQuery();

	        while (rs.next()) {
	            tempRefNo = rs.getString("refno");
	            draftdata = rs.getString("draftdata");
	            if (tempRefNo.equalsIgnoreCase(refNo) && draftdata.equals("S")) {
	                System.out.println("ref number already assigned");
	                return false; // If refNo is found, it means it's not valid
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false; // Return false if an exception occurs
	    } finally {
	        ConnectionManager.close(rs);
	        ConnectionManager.close(ps);
	        ConnectionManager.close(con);
	    }

	    // If the loop completes without finding a matching refNo, it is valid
	    return true;
	}

	@Override
	public TimeTableDTO getDetailsForReport(String routeNo,String busCategory) {
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String originSinhala,destinationSinhala,distance,travelTime,averageSpeed,tripCategorySinhala,des = null;
	    TimeTableDTO dto = new TimeTableDTO();
	    try {

	        con = ConnectionManager.getConnection();

	        String query = "SELECT rou_description,rou_distance,nt_m_panelgenerator.bus_category "
	        		+ "FROM public.nt_r_route inner join public.nt_m_panelgenerator on nt_m_panelgenerator.route_no  = rou_number "
	        		+ "where rou_number =? and bus_category = ?";

	        ps = con.prepareStatement(query);
	        ps.setString(1, routeNo);
	        ps.setString(2, busCategory);
	        rs = ps.executeQuery();

	        if (rs.next()) {
	        	distance = rs.getString("rou_distance");
	        	des = rs.getString("rou_description");

	            dto.setDistanceString(distance);
	            dto.setDescription(des);
	        
	            dto.setBusCategory(rs.getString("bus_category"));
	            
	        	if (rs.getString("bus_category").equals("001")) {

					dto.setBusCategoryDes("NORMAL");
				}

				else if (rs.getString("bus_category").equals("002")) {
					dto.setBusCategoryDes("LUXURY");

				}

				else if (rs.getString("bus_category").equals("003")) {
					dto.setBusCategoryDes("SUPER LUXURY");

				} else if (rs.getString("bus_category").equals("004")) {
					dto.setBusCategoryDes("SEMI-LUXURY");

				} else if (rs.getString("bus_category").equals("EB")) {
					dto.setBusCategoryDes("EXPRESSWAY BUS");

				}
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
	public List<String> getReferenceNumbersForEdit(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String refNo = null;
		List<String> refNoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT refno FROM panel_generator_general_data order by refno desc";

			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();

			while (rs.next()) {
				refNo = rs.getString("refno");
				refNoList.add(refNo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return refNoList;
	}
	
	@Override
	public List<String> getLeavePositionList(String refNo,String tripType){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> refNoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT leave_position, trip_type FROM public.nt_t_route_schedule_generator_det02 where generator_ref_no = ? and trip_type = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				refNoList.add(rs.getString("leave_position"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return refNoList;
	}
	
	@Override
	public TimeTableDTO getRouteDataForEditPanelGenerator(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();
		boolean state=true;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT  routeno, groupcount, buscategory, origin, destination, isdailyrotation, istwodayrotation, nooftripsorigin, noofprivatebusesorigin, "
	                + "nooftemporarybusesorigin, noofctborigin, noofetcorigin, noofprivateleavebusesorigin, noofdummybusesorigin, resttimeorigin, totalbusesorigin, "
	                + "nooftripsdestination, noofprivatebusesdestination, nooftemporarybusesdestination, noofctbdestination, noofetcdestination, noofprivateleavebusesdestination, "
	                + "noofdummybusesdestination, resttimedestination, totalbusesdestination, time_tables_per_week, state,coupletwo "
	                + "FROM panel_generator_general_data "
	                + "WHERE refno=?";
			
			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setRouteNo(rs.getString("routeno"));
				dto.setGroupNo(rs.getString("groupcount"));
				dto.setBusCategory(rs.getString("buscategory"));
				dto.setOrigin(rs.getString("origin"));
				dto.setDestination(rs.getString("destination"));
				dto.setDailyRotation(rs.getBoolean("isdailyrotation"));
				dto.setTwoDayRotation(rs.getBoolean("istwodayrotation"));
				state = rs.getBoolean("state");
				if(state == true) {
					dto.setStatus("Active");
				}else {
					dto.setStatus("Inactive");
				}
				dto.setNoOfTimeTablesPerWeek(rs.getString("time_tables_per_week"));
				dto.setNoOfTripsOrigin(rs.getInt("nooftripsorigin"));
				dto.setNoOfPvtBusesOrigin(rs.getInt("noofprivatebusesorigin"));
				dto.setNoOfTemporaryBusesOrigin(rs.getInt("nooftemporarybusesorigin"));
				dto.setNoOfCtbOrigin(rs.getInt("noofctborigin"));
				dto.setNoOfEtcOrigin(rs.getInt("noofetcorigin"));
				dto.setNoOfPvtLeaveBusesOrigin(rs.getInt("noofprivateleavebusesorigin"));
				dto.setNoOfDummyBusesOrigin(rs.getInt("noofdummybusesorigin"));
				dto.setRestTimeOrigin(rs.getString("resttimeorigin"));
				dto.setTotalBusesOrigin(rs.getInt("totalbusesorigin"));
				
				dto.setNoOfTripsDestination(rs.getInt("nooftripsdestination"));
				dto.setNoOfPvtBusesDestination(rs.getInt("noofprivatebusesdestination"));
				dto.setNoOfTemporaryBusesDestination(rs.getInt("nooftemporarybusesdestination"));
				dto.setNoOfCtbDestination(rs.getInt("noofctbdestination"));
				dto.setNoOfEtcDestination(rs.getInt("noofetcdestination"));
				dto.setNoOfPvtLeaveBusesDestination(rs.getInt("noofprivateleavebusesdestination"));
				dto.setNoOfDummyBusesDestination(rs.getInt("noofdummybusesdestination"));
				dto.setRestTimeDestination(rs.getString("resttimedestination"));
				dto.setTotalBusesDestination(rs.getInt("totalbusesdestination"));
				
				dto.setGenereatedRefNo(refNo);
				dto.setCoupleTwo(rs.getBoolean("coupletwo"));				

				if (rs.getString("buscategory").equals("001")) {

					dto.setBusCategoryDes("NORMAL");
				}

				else if (rs.getString("buscategory").equals("002")) {

					dto.setBusCategoryDes("LUXURY");
				}
				else if (rs.getString("buscategory").equals("003")) {

					dto.setBusCategoryDes("SUPER LUXURY");
				}
				else if (rs.getString("buscategory").equals("004")) {

					dto.setBusCategoryDes("SEMI-LUXURY");
				}
				else if (rs.getString("buscategory").equals("EB")) {

					dto.setBusCategoryDes("EXPRESSWAY BUS");
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

		return dto;
	}
	
	@Override
	public HashMap<List<TimeTableDTO>, List<TimeTableDTO>> getPreviousData(String refNo,String tripType, String group) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		PreparedStatement ps3 = null;
		ResultSet rs3 = null;
		HashMap<List<TimeTableDTO>, List<TimeTableDTO>> dataMap = new HashMap<>();
		List<TimeTableDTO> dataList = new ArrayList<>();
		List<TimeTableDTO> dataListLeave = new ArrayList<>();
		String day = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT rs_no_of_dates,rs_trip_type FROM public.nt_m_route_schedule_generator "
					+ "where rs_generator_ref_no = ? and rs_trip_type = ? and rs_group_no = ?";
			
			ps1 = con.prepareStatement(query2);
			ps1.setString(1, refNo);
			ps1.setString(2, tripType);
			ps1.setString(3, group);
			rs1 = ps1.executeQuery();
			
			while (rs1.next()) {
				day = rs1.getString("rs_no_of_dates");
			}
			
			String sql = "SELECT a.bus_no, a.trip_type "
					+ "FROM public.nt_t_route_schedule_generator_det01 a "
					+ "INNER JOIN public.nt_m_route_schedule_generator b "
					+ "ON b.rs_generator_ref_no = a.generator_ref_no "
					+ "WHERE b.rs_generator_ref_no = ? "
					+ "AND b.rs_trip_type = ? "
					+ "AND a.trip_type  = ? "
					+ "AND b.rs_group_no = ? "
					+ "AND a.day_no = ? order by a.seq";
			
			ps = con.prepareStatement(sql);
			ps.setString(1, refNo);
			ps.setString(2, tripType);
			ps.setString(3, tripType);
			ps.setString(4, group);
			ps.setLong(5, Long.valueOf(day));
			rs = ps.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();	
				if(tripType.equalsIgnoreCase("O")) {
					dto.setBusNoOrigin(rs.getString("bus_no"));
				}else {
					dto.setBusNoDestination(rs.getString("bus_no"));
				}
				
				dataList.add(dto);				
			}
			
			String sql2 = "SELECT a.trip_type, a.leave_bus "
					+ "FROM public.nt_t_route_schedule_generator_det01 a "
					+ "INNER JOIN public.nt_m_route_schedule_generator b "
					+ "ON b.rs_generator_ref_no = a.generator_ref_no "
					+ "WHERE b.rs_generator_ref_no = ? "
					+ "AND b.rs_trip_type = ? "
					+ "AND a.trip_type  = ? "
					+ "AND b.rs_group_no = ? "
					+ "AND a.leave_trip_day  = ?";
			
			ps3 = con.prepareStatement(sql2);
			ps3.setString(1, refNo);
			ps3.setString(2, tripType);
			ps3.setString(3, tripType);
			ps3.setString(4, group);
			ps3.setString(5, day);
			rs3 = ps3.executeQuery();

			while (rs3.next()) {
				TimeTableDTO dto = new TimeTableDTO();	
				if(tripType.equalsIgnoreCase("O")) {
					dto.setBusNoOrigin(rs3.getString("leave_bus"));
				}else {
					dto.setBusNoDestination(rs3.getString("leave_bus"));
				}
				
				dataListLeave.add(dto);				
			}

			dataMap.put(dataList, dataListLeave);
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dataMap;
	}
	
	@Override
	public int getCountForCoupleTwo(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TimeTableDTO dto = new TimeTableDTO();
		int count = 0;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT COUNT(*) AS abbreviation_count "
					+ "FROM (SELECT DISTINCT a.abbreviation "
					+ "FROM public.panel_generator_origin_trip_details a "
					+ "INNER JOIN public.panel_generator_general_data b ON b.refno = a.ref_no "
					+ "INNER JOIN public.nt_t_route_creator c "
					+ "ON c.rc_route_no = b.routeno AND c.rc_bus_type = b.buscategory "
					+ "WHERE a.draftdata = 'S' "
					+ "AND (a.abbreviation NOT LIKE 'SLTB%' AND a.abbreviation NOT LIKE 'ETC%' "
					+ "			AND a.abbreviation NOT LIKE CONCAT(rc_abbriviation_ltr_end, '%')) "
					+ "AND b.refno = ?) AS distinct_abbreviations_count";
			
			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getInt("abbreviation_count");
			}

		} catch (Exception e) {
			e.printStackTrace();

			return count;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return count;
	}
	
	@Override
	public TimeTableDTO getDtoWithGeneralData(String routeNo,String serviceType) {
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    String busType = null;
	    TimeTableDTO dto = new TimeTableDTO();
	    try {

	        con = ConnectionManager.getConnection();

	        String query = "SELECT seq,rc_bus_type, rc_travel_time, rc_driver_rest_time, rc_bus_speed, rc_strat_from, rc_end_at, "
	        		+ "rc_abbriviation_ltr_start, rc_abbriviation_ltr_end,nt_r_service_types.code  FROM public.nt_t_route_creator "
	        		+ "inner join public.nt_r_service_types on nt_r_service_types.code = rc_bus_type "
	        		+ "where rc_route_no=? and nt_r_service_types.code = ?";

	        ps = con.prepareStatement(query);
	        ps.setString(1, routeNo);
	        ps.setString(2, serviceType);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	        	busType = rs.getString("rc_bus_type");
	        	
	        	dto.setBusCategory(rs.getString("rc_bus_type"));
	        	
	        	if (busType.equals("001")) {

					dto.setBusCategoryDes("NORMAL");
				}

				else if (busType.equals("002")) {
					dto.setBusCategoryDes("LUXURY");

				}

				else if (busType.equals("003")) {
					dto.setBusCategoryDes("SUPER LUXURY");

				} else if (busType.equals("004")) {
					dto.setBusCategoryDes("SEMI-LUXURY");

				} else if (busType.equals("EB")) {
					dto.setBusCategoryDes("EXPRESSWAY BUS");

				}
	        	
	        	dto.setTraveltime(rs.getString("rc_travel_time"));
	        	dto.setRestTime(rs.getString("rc_driver_rest_time"));
	        	dto.setBusSpeed(rs.getString("rc_bus_speed"));
	        	dto.setOrigin(rs.getString("rc_strat_from"));
	        	dto.setDestination(rs.getString("rc_end_at"));
	        	dto.setAbbreviationOrigin(rs.getString("rc_abbriviation_ltr_start"));
	        	dto.setAbbreviationDestination(rs.getString("rc_abbriviation_ltr_end"));
	        	dto.setBusCategory(rs.getString("code"));
	        	dto.setId(rs.getString("seq"));
	            
	        }
	        
	        
	        

	    } catch (Exception e) {
	        e.printStackTrace();
	        
	    } finally {
	        ConnectionManager.close(rs);
	        ConnectionManager.close(ps);
	        ConnectionManager.close(con);
	    }

	    // If the loop completes without finding a matching refNo, it is valid
	    return dto;
	}
	
	@Override
	public List<TimeTableDTO> getOriginDtoListForEdit(String refNo,String routeno, String service) {
		Connection con = null;
	    PreparedStatement ps = null;
	    PreparedStatement ps1 = null;
	    ResultSet rs = null;
	    ResultSet rs1 = null;
	    
		List<TimeTableDTO> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT panel_generator_origin_trip_details.route_no, panel_generator_origin_trip_details.origin, "
					+ "panel_generator_origin_trip_details.abbreviation, panel_generator_origin_trip_details.permit_no, "
					+ "panel_generator_origin_trip_details.bus_no, panel_generator_origin_trip_details.start_time, "
					+ "panel_generator_origin_trip_details.end_time, panel_generator_origin_trip_details.rest_time, "
					+ "panel_generator_origin_trip_details.is_fixed, panel_generator_origin_trip_details.group_name, "
					+ "panel_generator_origin_trip_details.id "
					+ "FROM panel_generator_origin_trip_details "
					+ "where ref_no=? ORDER BY id";
			
			String sql = "SELECT seq "
					+ "FROM nt_t_control_panel_bus_details "
					+ "where routeno=? AND service_type = ? ORDER BY depature";

			ps = con.prepareStatement(query);
	        ps.setString(1, refNo);
	        rs = ps.executeQuery();

	        ps1 = con.prepareStatement(sql);
	        ps1.setString(1, routeno);
	        ps1.setString(2, service);
	        rs1 = ps1.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setRouteNo(rs.getString("route_no"));
				dto.setOrigin(rs.getString("origin"));
				dto.setAbbreviationOrigin(rs.getString("abbreviation"));
				dto.setPermitNoOrigin(rs.getString("permit_no"));
				dto.setBusNoOrigin(rs.getString("bus_no"));
				dto.setStartTimeOrigin(rs.getString("start_time"));
				dto.setEndTimeOrigin(rs.getString("end_time"));
				dto.setRestTimeOrigin(rs.getString("rest_time"));
				dto.setFixBusOrigin(rs.getBoolean("is_fixed"));
				dto.setGroupNo(rs.getString("group_name"));
				dto.setSeq(rs.getLong("id"));
				if (rs1.next()) {
	                dto.setAbbno(rs1.getLong("seq"));
	            }
				
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
	        ConnectionManager.close(ps);
	        ConnectionManager.close(rs1);
	        ConnectionManager.close(ps1);
	        ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	
	
	@Override
	public List<TimeTableDTO> getDestinationDtoListForEdit(String refNo,String routeno, String service) {
		
		Connection con = null;
	    PreparedStatement ps = null;
	    PreparedStatement ps1 = null;
	    ResultSet rs = null;
	    ResultSet rs1 = null;
	    
		List<TimeTableDTO> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT panel_generator_destination_trip_details.route_no, panel_generator_destination_trip_details.origin, "
					+ "panel_generator_destination_trip_details.abbreviation, panel_generator_destination_trip_details.permit_no, "
					+ "panel_generator_destination_trip_details.bus_no, panel_generator_destination_trip_details.start_time, "
					+ "panel_generator_destination_trip_details.end_time, panel_generator_destination_trip_details.rest_time, "
					+ "panel_generator_destination_trip_details.is_fixed, panel_generator_destination_trip_details.group_name, "
					+ "panel_generator_destination_trip_details.id "
					+ "FROM panel_generator_destination_trip_details "
					+ "where ref_no=? ORDER BY id";
			
			String sql = "SELECT seq "
					+ "FROM nt_t_control_panel_bus_details "
					+ "where routeno=? AND service_type = ? ORDER BY depature";

			ps = con.prepareStatement(query);
	        ps.setString(1, refNo);
	        rs = ps.executeQuery();

	        ps1 = con.prepareStatement(sql);
	        ps1.setString(1, routeno);
	        ps1.setString(2, service);
	        rs1 = ps1.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setRouteNo(rs.getString("route_no"));
				dto.setDestination(rs.getString("origin"));
				dto.setAbbreviationDestination(rs.getString("abbreviation"));
				dto.setPermitNoDestination(rs.getString("permit_no"));
				dto.setBusNoDestination(rs.getString("bus_no"));
				dto.setStartTimeDestination(rs.getString("start_time"));
				dto.setEndTimeDestination(rs.getString("end_time"));
				dto.setRestTimeDestination(rs.getString("rest_time"));
				dto.setFixBusDestination(rs.getBoolean("is_fixed"));
				dto.setGroupNo(rs.getString("group_name"));
				dto.setSeq(rs.getLong("id"));
				if (rs1.next()) {
	                dto.setAbbno(rs1.getLong("seq"));
	            }
				
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
	        ConnectionManager.close(ps);
	        ConnectionManager.close(rs1);
	        ConnectionManager.close(ps1);
	        ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	
	@Override
	public List<TimeTableDTO> getOriginDtoListForSchedule(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT route_no, origin, abbreviation, permit_no, bus_no, start_time, end_time, rest_time, is_fixed, "
					+ "group_name, id FROM panel_generator_origin_trip_details "
					+ "where ref_no=? "
					+ "ORDER BY id";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setRouteNo(rs.getString("route_no"));
				dto.setOrigin(rs.getString("origin"));
				dto.setAbbreviationOrigin(rs.getString("abbreviation"));
				dto.setPermitNoOrigin(rs.getString("permit_no"));
				dto.setBusNoOrigin(rs.getString("bus_no"));
				dto.setStartTimeOrigin(rs.getString("start_time"));
				dto.setEndTimeOrigin(rs.getString("end_time"));
				dto.setRestTimeOrigin(rs.getString("rest_time"));
				dto.setFixBusOrigin(rs.getBoolean("is_fixed"));
				dto.setGroupNo(rs.getString("group_name"));
				dto.setSeq(rs.getLong("id"));
				
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	@Override
	public List<String> getOriginDtoListForScheduleFix(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT bus_no FROM panel_generator_origin_trip_details "
					+ "where ref_no= ? and is_fixed = true "
					+ "ORDER BY start_time";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dtoList.add(rs.getString("bus_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	@Override
	public List<String> getDestinationDtoListForScheduleFix(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT bus_no FROM panel_generator_destination_trip_details "
					+ "where ref_no= ? and is_fixed = true "
					+ "ORDER BY start_time";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dtoList.add(rs.getString("bus_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	@Override
	public List<TimeTableDTO> getOriginDtoListForScheduleWithoutFix(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct bus_no "
					+ "FROM panel_generator_origin_trip_details  "
					+ "INNER JOIN nt_t_route_creator rc ON route_no = rc.rc_route_no "
					+ "where ref_no=? and is_fixed = false and  abbreviation NOT LIKE CONCAT(rc_abbriviation_ltr_end, '%') ";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setBusNoOrigin(rs.getString("bus_no"));
				
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}


	@Override
	public List<TimeTableDTO> getDetailsForControlSheetReport(String refNo, String tripDirection) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> dtoList = new ArrayList<>();
		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT * FROM nt_t_route_schedule_generator_det01 " +
					"WHERE generator_ref_no=? AND trip_type=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, tripDirection);
			rs = ps.executeQuery();
			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setSeq(rs.getLong("seq"));
				dto.setRouteNo(rs.getString("route_ref_no"));
				dto.setGenereatedRefNo(rs.getString("generator_ref_no"));
				String dayNo = rs.getString("day_no");
				dto.setNumOfLeaves(0);
				dto.setTripId(rs.getString("trip_id"));
				dto.setBusNo(rs.getString("bus_no"));
				if (dayNo == null) {
					dayNo = rs.getString("leave_trip_day");
					dto.setTripId(rs.getString("leave_trip_id"));
					dto.setBusNo(rs.getString("leave_bus"));
					dto.setNumOfLeaves(1);
				}
				try {
					dto.setNoOfDays(Integer.parseInt(dayNo));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				dto.setTripType(rs.getString("trip_type"));

				dtoList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	@Override
	public List<TimeTableDTO> getOriginDtoLeaveListForSchedule(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT bus_no,id FROM panel_generator_leave_bus_details where ref_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setBusNoOrigin(rs.getString("bus_no"));
				dto.setSeq(rs.getLong("id"));
				
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	@Override
	public List<TimeTableDTO> getDestinationDtoListForSchedule(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT route_no, origin, abbreviation, permit_no, bus_no, start_time, end_time, "
					+ "rest_time, is_fixed, group_name, id "
					+ "FROM panel_generator_destination_trip_details "
					+ "where ref_no=? "
					+ "ORDER BY id";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setRouteNo(rs.getString("route_no"));
				dto.setDestination(rs.getString("origin"));
				dto.setAbbreviationDestination(rs.getString("abbreviation"));
				dto.setPermitNoDestination(rs.getString("permit_no"));
				dto.setBusNoDestination(rs.getString("bus_no"));
				dto.setStartTimeDestination(rs.getString("start_time"));
				dto.setEndTimeDestination(rs.getString("end_time"));
				dto.setRestTimeDestination(rs.getString("rest_time"));
				dto.setFixBusDestination(rs.getBoolean("is_fixed"));
				dto.setGroupNo(rs.getString("group_name"));
				dto.setSeq(rs.getLong("id"));
					
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	@Override
	public List<TimeTableDTO> getDestinationDtoListForScheduleWithoutFix(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT bus_no "
					+ "FROM panel_generator_destination_trip_details "
					+ "INNER JOIN nt_t_route_creator rc ON route_no = rc.rc_route_no "
					+ "where ref_no = ? "
					+ "AND  is_fixed = false "
					+ "and  abbreviation NOT LIKE CONCAT(rc_abbriviation_ltr_start, '%') ";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setBusNoDestination(rs.getString("bus_no"));
					
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	
	@Override
	public List<TimeTableDTO> getLeaveBusesDtoListForEdit(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT route_no, origin, abbreviation, permit_no, bus_no, id "
					+ "FROM panel_generator_leave_bus_details "
					+ "WHERE ref_no = ? "
					+ "AND ("
					+ "(abbreviation IS NOT NULL AND abbreviation <> '') OR "
					+ "(permit_no IS NOT NULL AND permit_no <> '') OR "
					+ "(bus_no IS NOT NULL AND bus_no <> ''))";
			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setRouteNo(rs.getString("route_no"));
				dto.setDestination(rs.getString("origin"));
				dto.setAbbreviationLeave(rs.getString("abbreviation"));
				dto.setPermitNoLeave(rs.getString("permit_no"));
				dto.setBusNoLeave(rs.getString("bus_no"));	
				dto.setSeq(rs.getLong("id"));
					
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	@Override
	public List<TimeTableDTO> getLeaveBusesDtoListForEditDes(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT route_no, origin, abbrivationdes, permit_no_des, bus_no_des, id "
					+ "FROM panel_generator_leave_bus_details "
					+ "WHERE ref_no = ? "
					+ "AND ("
					+ "(abbrivationdes IS NOT NULL AND abbrivationdes <> '') OR "
					+ "(permit_no_des IS NOT NULL AND permit_no_des <> '') OR "
					+ "(bus_no_des IS NOT NULL AND bus_no_des <> ''))";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TimeTableDTO dto = new TimeTableDTO();
				dto.setRouteNo(rs.getString("route_no"));
				dto.setDestination(rs.getString("origin"));
				dto.setAbbreviationLeaveDes(rs.getString("abbrivationdes"));
				dto.setPermitNoLeaveDes(rs.getString("permit_no_des"));
				dto.setBusNoLeaveDes(rs.getString("bus_no_des"));
				dto.setSeq(rs.getLong("id"));
					
				dtoList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}

	@Override
	public void searchGeneralDetailsPanelGenerator(List<TimeTableDTO> list, String refNo) {
		Connection con = null;
		PreparedStatement psInsert = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;

		try {
			String insertSql = null;

			con = ConnectionManager.getConnection();

			insertSql = "INSERT INTO public.nt_t_control_panel_bus_details "
					+ "(abbreviation,bus_no,origin,destination,routeno,service_type,arrival,depature,tripno,end_time,service_time,stering_hours,"
					+ "rest_time,break_1,break_2,break_3,arrival1,depature1,arrival2,depature2,arrival3,depature3,arrival4,depature4,"
					+ "arrival5,depature5,refno) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			psInsert = con.prepareStatement(insertSql);

			for (TimeTableDTO data : list) {
				try {
					psInsert.setString(1, data.getAbbreviation());
					psInsert.setString(2, data.getBusNo());
					psInsert.setString(3, data.getOrigin());
					psInsert.setString(4, data.getDestination());
					psInsert.setString(5, data.getRouteNo());
					psInsert.setString(6, data.getServiceType());
					psInsert.setString(7, data.getArrival());
					psInsert.setString(8, data.getDepature());
					psInsert.setLong(9, data.getTripno());
					psInsert.setString(10, data.getEndTime());
					psInsert.setString(11, data.getServiceTime());
					psInsert.setString(12, data.getSteringHours());
					psInsert.setString(13, data.getRestTime());
					psInsert.setString(14, data.getBreak1());
					psInsert.setString(15, data.getBreak2());
					psInsert.setString(16, data.getBreak3());
					psInsert.setString(17, data.getArrivalOne());
					psInsert.setString(18, data.getDepatureOne());
					psInsert.setString(19, data.getArrival2());
					psInsert.setString(20, data.getDepature2());
					psInsert.setString(21, data.getArrival3());
					psInsert.setString(22, data.getDepature3());
					psInsert.setString(23, data.getArrival4());
					psInsert.setString(24, data.getDepature4());
					psInsert.setString(25, data.getArrival5());
					psInsert.setString(26, data.getDepature5());
					psInsert.setString(27, refNo);

					psInsert.executeUpdate();
					con.commit();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(psUpdate);
			ConnectionManager.close(psInsert);
			ConnectionManager.close(con);
		}

	}
	
	@Override
	public boolean checkDataHave(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean dataHave = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * FROM public.nt_t_control_panel_bus_details where refno = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				dataHave = true;
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
	public void updateGeneralDetailsPanelGenerator(List<TimeTableDTO> list,String refNo) {
		Connection con = null;
		PreparedStatement psInsert = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;

		try {
			String insertSql = null;
			String updateSql = null;

			con = ConnectionManager.getConnection();

			updateSql = "UPDATE public.nt_t_control_panel_bus_details SET origin = ?, destination = ?, service_type = ?, arrival = ?, "
					+ "depature = ?, tripno = ?, end_time = ?, service_time = ?, stering_hours = ?, rest_time = ?, break_1 = ?, "
					+ "break_2 = ?, break_3 = ?, arrival1 = ?, depature1 = ?, arrival2 = ?, depature2 = ?, arrival3 = ?, "
					+ "depature3 = ?, arrival4 = ?, depature4 = ?, arrival5 = ?, depature5 = ?, "
					+ "routeno = ?,bus_no =? WHERE abbreviation = ? and refno = ?";
			
			insertSql = "INSERT INTO public.nt_t_control_panel_bus_details "
					+ "(abbreviation,bus_no,origin,destination,routeno,service_type,arrival,depature,tripno,end_time,service_time,stering_hours,"
					+ "rest_time,break_1,break_2,break_3,arrival1,depature1,arrival2,depature2,arrival3,depature3,arrival4,depature4,"
					+ "arrival5,depature5,refno) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			psInsert = con.prepareStatement(insertSql);

			psUpdate = con.prepareStatement(updateSql);

			for (TimeTableDTO data : list) {

				try {
					psUpdate.setString(1, data.getOrigin());
				    psUpdate.setString(2, data.getDestination());
				    psUpdate.setString(3, data.getServiceType());
				    psUpdate.setString(4, data.getArrival());
				    psUpdate.setString(5, data.getDepature());
				    psUpdate.setLong(6, data.getTripno());
				    psUpdate.setString(7, data.getEndTime());
				    psUpdate.setString(8, data.getServiceTime());
				    psUpdate.setString(9, data.getSteringHours());
				    psUpdate.setString(10, data.getRestTime());
				    psUpdate.setString(11, data.getBreak1());
				    psUpdate.setString(12, data.getBreak2());
				    psUpdate.setString(13, data.getBreak3());
				    psUpdate.setString(14, data.getArrivalOne());
				    psUpdate.setString(15, data.getDepatureOne());
				    psUpdate.setString(16, data.getArrival2());
				    psUpdate.setString(17, data.getDepature2());
				    psUpdate.setString(18, data.getArrival3());
				    psUpdate.setString(19, data.getDepature3());
				    psUpdate.setString(20, data.getArrival4());
				    psUpdate.setString(21, data.getDepature4());
				    psUpdate.setString(22, data.getArrival5());
				    psUpdate.setString(23, data.getDepature5());
				    psUpdate.setString(24, data.getRouteNo());
				    psUpdate.setString(25, data.getBusNo());
				    psUpdate.setString(26, data.getAbbreviation());
				    psUpdate.setString(27, refNo);

				    int updateCount = psUpdate.executeUpdate();
					con.commit();
					
					if(updateCount == 0) {
						psInsert.setString(1, data.getAbbreviation());
						psInsert.setString(2, data.getBusNo());
						psInsert.setString(3, data.getOrigin());
						psInsert.setString(4, data.getDestination());
						psInsert.setString(5, data.getRouteNo());
						psInsert.setString(6, data.getServiceType());
						psInsert.setString(7, data.getArrival());
						psInsert.setString(8, data.getDepature());
						psInsert.setLong(9, data.getTripno());
						psInsert.setString(10, data.getEndTime());
						psInsert.setString(11, data.getServiceTime());
						psInsert.setString(12, data.getSteringHours());
						psInsert.setString(13, data.getRestTime());
						psInsert.setString(14, data.getBreak1());
						psInsert.setString(15, data.getBreak2());
						psInsert.setString(16, data.getBreak3());
						psInsert.setString(17, data.getArrivalOne());
						psInsert.setString(18, data.getDepatureOne());
						psInsert.setString(19, data.getArrival2());
						psInsert.setString(20, data.getDepature2());
						psInsert.setString(21, data.getArrival3());
						psInsert.setString(22, data.getDepature3());
						psInsert.setString(23, data.getArrival4());
						psInsert.setString(24, data.getDepature4());
						psInsert.setString(25, data.getArrival5());
						psInsert.setString(26, data.getDepature5());
						psInsert.setString(27, refNo);

						psInsert.executeUpdate();
						con.commit();
					}
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(psUpdate);
			ConnectionManager.close(psInsert);
			ConnectionManager.close(con);
		}

	}
	
	@Override
	public List<String> getDaysOfGroup(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> dtoList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select d_sunday,d_monday,d_tuesday,d_wednesday,d_thursday,d_friday,d_saturday "
					+ "from public.nt_t_panelgenerator_det "
					+ "inner join public.nt_m_panelgenerator "
					+ "on nt_t_panelgenerator_det.seq_panelgenerator = nt_m_panelgenerator.seq "
					+ "where nt_m_panelgenerator.ref_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				if(rs.getString("d_sunday").equalsIgnoreCase("Y")) {
					dtoList.add("sunday");
				}if(rs.getString("d_monday").equalsIgnoreCase("Y")) {
					dtoList.add("monday");
				}if(rs.getString("d_tuesday").equalsIgnoreCase("Y")) {
					dtoList.add("tuesday");
				}if(rs.getString("d_wednesday").equalsIgnoreCase("Y")) {
					dtoList.add("wednesday");
				}if(rs.getString("d_thursday").equalsIgnoreCase("Y")) {
					dtoList.add("thursday");
				}if(rs.getString("d_friday").equalsIgnoreCase("Y")) {
					dtoList.add("friday");
				}if(rs.getString("d_saturday").equalsIgnoreCase("Y")) {
					dtoList.add("saturday");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dtoList;
	}
	
	@Override
	public Date getStartDaysOfGroup(String refNo,String tripSide) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> dtoList = new ArrayList<>();
		Date date = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "select rs_end_date from public.nt_m_route_schedule_generator "
					+ "where rs_generator_ref_no = ? and rs_trip_type = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				String time = rs.getString("rs_end_date");
				date =new SimpleDateFormat("dd/MM/yyyy").parse(time); 
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return date;
	}
	
	// added by danilka.j
	// currently support for only 31 columns(31 buses)
	@Override
	public void insertRidingDaysForOwnershipReport(OwnerSheetListMonthWrapper wrapper, String refNo) {
	    Connection con = null;  

	    try {
	        con = ConnectionManager.getConnection();
	        try (PreparedStatement psInsert = con.prepareStatement(
	                "INSERT INTO public.nt_t_owner_sheet_dates "
	                        + "(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, col12, col13, col14, col15, col16, "
	                        + "col17, col18, col19, col20, col21, col22, col23, col24, col25, "
	                        + "col26, col27, col28, col29, col30, col31, month, ref_no) "
	                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
	        ) {
	            for (int i = 0; i < 31; i++) {
	                if (i < wrapper.getList().length) {
	                    psInsert.setString(i + 1, wrapper.getList()[i]);
	                } else {
	                    // If no record is available, insert null
	                    psInsert.setNull(i + 1, Types.VARCHAR);
	                }
	            }
	            psInsert.setString(32, wrapper.getMonth());
	            psInsert.setString(33, refNo);

	            psInsert.executeUpdate();
	            con.commit();

	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Roll back the transaction in case of an exception
	            try {
	                if (con != null) {
	                    con.rollback();
	                }
	            } catch (SQLException rollbackException) {
	                rollbackException.printStackTrace();
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // Close the connection in the finally block to ensure proper cleanup
	        try {
	            if (con != null) {
	                con.close();
	            }
	        } catch (SQLException closeException) {
	            closeException.printStackTrace();
	        }
	    }
	}


	@Override
	public int getDayOneBusCount(String refNo, String side) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		HashMap<String, List<String>> hashMap = new HashMap<>();
		int count = 0;
		
		try {
			String query = null;
			String sql = null;
			con = ConnectionManager.getConnection();

			query = "select abbreviation, bus_no, start_time, end_time  FROM public.panel_generator_origin_trip_details "
					+ "WHERE ref_no = ? order by start_time ";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				String bus = rs.getString("abbreviation");
				if (!hashMap.containsKey(bus)) {
					List<String> startTimeList = new ArrayList<String>();
					startTimeList.add(rs.getString("start_time"));
					hashMap.put(bus, startTimeList);
				} else {
					List<String> startTimeList = hashMap.get(bus);
					startTimeList.add(rs.getString("start_time"));
				}
			}
			
			sql = "select abbreviation, bus_no, start_time, end_time  FROM public.panel_generator_destination_trip_details "
					+ "WHERE ref_no = ? order by start_time ";

			ps1 = con.prepareStatement(sql);
			ps1.setString(1, refNo);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				String bus = rs1.getString("abbreviation");
				if (!hashMap.containsKey(bus)) {
					List<String> startTimeList = new ArrayList<String>();
					startTimeList.add(rs1.getString("start_time"));
					hashMap.put(bus, startTimeList);
				} else {
					List<String> startTimeList = hashMap.get(bus);
					startTimeList.add(rs1.getString("start_time"));
				}
			}

			for(Map.Entry<String, List<String>> entry : hashMap.entrySet()) {
				List<String> times = entry.getValue();
				String time1 = times.get(0);
				String time2 = times.get(1);
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				LocalTime localTime1 = LocalTime.parse(time1, formatter);
			    LocalTime localTime2 = LocalTime.parse(time2, formatter);
				
			    if(side.equalsIgnoreCase("O") && !(entry.getKey().contains("SLTB") || entry.getKey().contains("ETC") || (entry.getKey().contains(abbDes)))) {
			    	if (localTime1.isBefore(localTime2)){
			            count++;
			        }
				}else if(side.equalsIgnoreCase("D") && !(entry.getKey().contains("SLTB") || entry.getKey().contains("ETC") || (entry.getKey().contains(abbOri)))){
			    	if (localTime2.isBefore(localTime1)) {
			            count++;
			        }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return count;
	}
	
	@Override
	public boolean getTwoDayRotation(String refNo, String tripType, String dateRange) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;
		boolean historyOne = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "select istwodayrotation from public.panel_generator_general_data where refno = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getBoolean("istwodayrotation")) {
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
	
	/* Created Dhananajika.d (21/08/2024) */
	@Override
	public LinkedHashMap<LinkedHashSet<String>, RouteScheduleHelperDTO> getBusFixAndNotFixed(String refNo, String abbreviation, String side) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		LinkedHashSet<String> fixedList = new LinkedHashSet<>();
		LinkedHashSet<String> notFixedList = new LinkedHashSet<>();
		List<String> fullBusList = new ArrayList<>();
		LinkedHashMap<LinkedHashSet<String>, RouteScheduleHelperDTO> busMap = new LinkedHashMap<>();
		RouteScheduleHelperDTO dto = new RouteScheduleHelperDTO();
		try {

			con = ConnectionManager.getConnection();

			if(side.equalsIgnoreCase("O")) {
				query = "select route_no, origin, abbreviation, permit_no, bus_no, start_time, end_time, rest_time, is_fixed, group_name, id "
						+ "from panel_generator_origin_trip_details "
						+ "where ref_no =? and (abbreviation NOT LIKE 'SLTB%' AND abbreviation NOT LIKE 'ETC%' AND abbreviation LIKE ?) "
						+ "order by id";
			}else if(side.equalsIgnoreCase("D")) {
				query = "select route_no, origin, abbreviation, permit_no, bus_no, start_time, end_time, rest_time, is_fixed, group_name, id "
						+ "from panel_generator_destination_trip_details "
						+ "where ref_no =? and (abbreviation NOT LIKE 'SLTB%' AND abbreviation NOT LIKE 'ETC%' AND abbreviation LIKE ?) "
						+ "order by id";
			}
			

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, abbreviation + "%");
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getBoolean("is_fixed")) {
					String bus = rs.getString("bus_no") + "F";
					fixedList.add(bus);
					fullBusList.add(bus);
				} else {
					String bus = rs.getString("bus_no");
					notFixedList.add(bus);
					fullBusList.add(bus);
				}
				
				
			}
			
			dto.setBusList(fullBusList);
			dto.setBusSet(notFixedList);
			busMap.put(fixedList, dto);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busMap;
	}
	
	@Override
	public RouteScheduleHelperDTO getLeaveBusesList(String refNo, String abbreviation, String side) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		LinkedHashSet<String> leaveBusList = new LinkedHashSet<>();
		List<String> leaveBusListString = new ArrayList<>();
		RouteScheduleHelperDTO dto = new RouteScheduleHelperDTO();
		try {

			con = ConnectionManager.getConnection();

			if(side.equalsIgnoreCase("O")) {
				query = "SELECT route_no, origin, abbreviation, permit_no, bus_no, id "
						+ "FROM panel_generator_leave_bus_details "
						+ "WHERE ref_no = ? "
						+ "AND abbreviation LIKE ?"
						+ "AND ("
						+ "(abbreviation IS NOT NULL AND abbreviation <> '') OR "
						+ "(permit_no IS NOT NULL AND permit_no <> '') OR "
						+ "(bus_no IS NOT NULL AND bus_no <> ''))";
			}else if(side.equalsIgnoreCase("D")) {
				query = "SELECT route_no, origin, abbrivationdes, permit_no_des, bus_no_des, id "
						+ "FROM panel_generator_leave_bus_details "
						+ "WHERE ref_no = ? "
						+ "AND abbrivationdes LIKE ?"
						+ "AND ("
						+ "(abbrivationdes IS NOT NULL AND abbrivationdes <> '') OR "
						+ "(permit_no_des IS NOT NULL AND permit_no_des <> '') OR "
						+ "(bus_no_des IS NOT NULL AND bus_no_des <> ''))";
			}

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setString(2, abbreviation + "%");
			rs = ps.executeQuery();

			while (rs.next()) {
				if(side.equalsIgnoreCase("O")) {
					String bus = rs.getString("bus_no");
					leaveBusList.add(bus);
					leaveBusListString.add(bus);
				}else if(side.equalsIgnoreCase("D")) {
					String bus = rs.getString("bus_no_des");
					leaveBusList.add(bus);
					leaveBusListString.add(bus);
				}				
			}
			
			dto.setBusList(leaveBusListString);
			dto.setBusSet(leaveBusList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto;
	}
	

	public static String getAbbOri() {
		return abbOri;
	}

	public static void setAbbOri(String abbOri) {
		TimeTableServiceImpl.abbOri = abbOri;
	}

	public static String getAbbDes() {
		return abbDes;
	}

	public static void setAbbDes(String abbDes) {
		TimeTableServiceImpl.abbDes = abbDes;
	}
	
}
