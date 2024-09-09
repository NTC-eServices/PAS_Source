package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.BusDetailsDTO;
import lk.informatics.ntc.model.dto.RouteSetUpDTO;
import lk.informatics.ntc.model.dto.SetUpMidPointsDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.beans.SetUpMidPointsBackingBean;

public class SetUpMidPointsServiceImpl implements SetUpMidPointsService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	private static String origin;
	private static String destination;
	
	public SetUpMidPointsBackingBean setUpMidPointsBackingBean;

	//Get Routes To drop Down
	@Override
	public List<SetUpMidPointsDTO> getRoutesToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SetUpMidPointsDTO> routeList = new ArrayList<SetUpMidPointsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rc_route_no FROM public.nt_t_route_creator";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO setUpMidPointsDTO = new SetUpMidPointsDTO();
				setUpMidPointsDTO.setRouteNo(rs.getString("rc_route_no"));

				routeList.add(setUpMidPointsDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeList;

	}
	
	@Override
    public List<SetUpMidPointsDTO> getdesCriptionToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SetUpMidPointsDTO> descript = new ArrayList<SetUpMidPointsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT rc_route_no,rc_strat_from,rc_end_at FROM public.nt_t_route_creator"; 
					    
			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO setUpMidPointsDTO = new SetUpMidPointsDTO();
				setUpMidPointsDTO.setRouteNo(rs.getString("rc_route_no"));
				setUpMidPointsDTO.setOrigin(rs.getString("rc_strat_from"));
				setUpMidPointsDTO.setDestination(rs.getString("rc_end_at"));
				descript.add(setUpMidPointsDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return descript;

	}
	//Get Service Type To Drop Down
	@Override
	public List<SetUpMidPointsDTO> getServiceTypeToDropDown(){
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SetUpMidPointsDTO> serviceTypeList = new ArrayList<SetUpMidPointsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT description,code FROM public.nt_r_service_types WHERE active= 'A' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO setUpMidPointsDTO = new SetUpMidPointsDTO();
				setUpMidPointsDTO.setServiceType(rs.getString("description"));
				setUpMidPointsDTO.setCode(rs.getString("code"));

				serviceTypeList.add(setUpMidPointsDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return serviceTypeList;
		
	}
	
	// Get Origin And Destination By Route No
	@Override
	public SetUpMidPointsDTO getDetailsbyRouteNo(String routeNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		SetUpMidPointsDTO details = new SetUpMidPointsDTO();

		try {
			con = ConnectionManager.getConnection();

//			String query = "SELECT b.rou_service_origine,b.rou_service_destination,b.rou_number,a.rs_route_no\r\n"
//					+ " FROM public.nt_m_route_station a\r\n " + "inner join public.nt_r_route b on b.rou_number=a.rs_route_no\r\n"
//					+ " WHERE a.rs_route_no =  ? ";
			
			String query = "SELECT rou_service_origine,rou_service_destination FROM public.nt_r_route WHERE rou_number =?";
					
			

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				details.setOrigin(rs.getString("rou_service_origine"));
				details.setDestination(rs.getString("rou_service_destination"));
				
			}
			
			origin = details.getOrigin();
			destination = details.getDestination();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
        
		return details;

	}
	
	
	//Get Mid-Points Origin To Destination
	@Override
	public List<SetUpMidPointsDTO> getAllMidPointsOrgToDes(String routeNo, String serviceType){
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<SetUpMidPointsDTO> midPointList = new ArrayList<SetUpMidPointsDTO>();
		
	
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT a.mp_time_taken,a.mp_midpoint_code,a.seq,b.code,b.midpoint_name "
					+ "FROM public.nt_m_route_midpoint a "
					+ "INNER JOIN public.nt_r_station b on a.mp_midpoint_code=b.code "
					+ "WHERE a.mp_route_no =? AND a.mp_bus_type  =? ORDER BY a.seq";
			
			ps = con.prepareStatement(query);
	
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO midData = new SetUpMidPointsDTO();

				midData.setSeq(rs.getLong("seq"));
				midData.setMidPointName(rs.getString("midpoint_name"));
				midData.setTimeTaken(rs.getString("mp_time_taken"));
				midData.setTickingOption(false);
				midPointList.add(midData);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		return midPointList;
		
	}
	
	
	//Get Mid-Points Destination To Origin
	@Override
	public List<SetUpMidPointsDTO> getAllMidPointsDesToOrg(String routeNo, String serviceType){
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<SetUpMidPointsDTO> midPointList2 = new ArrayList<SetUpMidPointsDTO>();
		

	
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT a.mp_time_taken,a.mp_midpoint_code,a.seq,b.code,b.midpoint_name "
					+ "FROM public.nt_m_route_midpoint_reverse a "
					+ "INNER JOIN public.nt_r_station b on a.mp_midpoint_code=b.code "
					+ "WHERE a.mp_route_no =? AND a.mp_bus_type = ? "
					+ "ORDER BY a.seq";
			
			
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO midData = new SetUpMidPointsDTO();
				midData.setSeq(rs.getLong("seq"));
				midData.setMidPointName(rs.getString("midpoint_name"));
				midData.setTimeTaken(rs.getString("mp_time_taken"));
				midData.setTickingOption(false);
				midPointList2.add(midData);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		return midPointList2;
		
	}
	

	//Get Abbreviation,Bus No,Permit No,Start Time,End Time By Route Number And Service Type Origin To Destination

	public List<BusDetailsDTO> getDetailsByRouteAndServiceType(String routeNo, String serviceType, 
			List<SetUpMidPointsDTO> selectedOrgToDesMidPointList, List<SetUpMidPointsDTO> orgToDesMidPointList,String group) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;	
		
		List<BusDetailsDTO> busDetailsList = new ArrayList<>();
		List<String> midList = new ArrayList<String>();
        int time = 0;
		
        try {
        	con = ConnectionManager.getConnection();
        	String sql = null;
        	   
        	//Only submit data
        	
            sql =  "select DISTINCT on(panel_generator_origin_trip_details.id) panel_generator_origin_trip_details.abbreviation,panel_generator_origin_trip_details.permit_no,"
            		+ "panel_generator_origin_trip_details.bus_no,panel_generator_origin_trip_details.start_time,"
            		+ "panel_generator_origin_trip_details.end_time,panel_generator_origin_trip_details.id,panel_generator_origin_trip_details.is_fixed "
            		+ "from public.panel_generator_origin_trip_details "
            		+ "inner join public.panel_generator_general_data on panel_generator_general_data.refno = panel_generator_origin_trip_details.ref_no "
            		+ "inner join public.nt_m_panelgenerator "
            		+ "on panel_generator_general_data.refno = nt_m_panelgenerator.ref_no  "
            		+ "where panel_generator_origin_trip_details.route_no  = ? and panel_generator_general_data.buscategory = ? and panel_generator_general_data.draftdata = 'S' "
            		+ "and  panel_generator_general_data.groupcount = ? and nt_m_panelgenerator.status = 'A' order by panel_generator_origin_trip_details.id";
        	
            
            //Save & Submit data
        	
//        	sql =  "select DISTINCT on(panel_generator_origin_trip_details.start_time) panel_generator_origin_trip_details.abbreviation,panel_generator_origin_trip_details.permit_no,"
//            		+ "panel_generator_origin_trip_details.bus_no,panel_generator_origin_trip_details.start_time,panel_generator_origin_trip_details.end_time,panel_generator_origin_trip_details.id "
//            		+ "from public.panel_generator_origin_trip_details "
//            		+ "inner join public.panel_generator_general_data on panel_generator_general_data.refno = panel_generator_origin_trip_details.ref_no "
//            		+ "where route_no  = ? and panel_generator_general_data.buscategory = ? order by panel_generator_origin_trip_details.start_time";

            ps = con.prepareStatement(sql);
            ps.setString(1, routeNo);
            ps.setString(2, serviceType);
            ps.setString(3, group);

            rs = ps.executeQuery();
            
            for(SetUpMidPointsDTO data:selectedOrgToDesMidPointList) {
        		midList.add(data.getMidPointName());
            }
            
                while (rs.next()) {
                	String startTime = rs.getString("start_time");
                	List<String> times = calculateArrivalTimesOrg(startTime, selectedOrgToDesMidPointList,orgToDesMidPointList);
                	
                    BusDetailsDTO busDetails = new BusDetailsDTO();
                    busDetails.setSeq(rs.getLong("id"));
                    busDetails.setBusNo(rs.getString("bus_no"));
                    busDetails.setPermitNo(rs.getString("permit_no"));
                    busDetails.setAbbreviation(rs.getString("abbreviation"));
                    busDetails.setStartTime(rs.getString("start_time"));
                    busDetails.setEndTime(rs.getString("end_time"));
                    busDetails.setFixedBus(rs.getBoolean("is_fixed"));
                    busDetails.setRouteNo(routeNo);
                    busDetails.setServiceType(serviceType);
                    busDetails.setMidList(midList);
                    busDetails.setTakenTime(times);
                    busDetailsList.add(busDetails);

                    time = time + 1;
                }
        } catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

        return busDetailsList;
         
    }
	
	@Override
	public List<String> calculateArrivalTimesOrgList(String startTime, List<SetUpMidPointsDTO> selectedMidPointList,
			List<SetUpMidPointsDTO> desToOrgMidPointList){
		 List<String> arrivalTimes = new ArrayList<>();
		    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		    try {
		        java.util.Date currentArrivalTime = sdf.parse(startTime);

		        if (desToOrgMidPointList.size() != selectedMidPointList.size()) {

		            for (SetUpMidPointsDTO midpoint : desToOrgMidPointList) {
		            	if(selectedMidPointList.contains(midpoint)) {
		            		String[] timeParts = midpoint.getTimeTaken().split(":");
	    	                int hours = Integer.parseInt(timeParts[0]);
	    	                int minutes = Integer.parseInt(timeParts[1]);

	    	                int minutesToAdd = (hours * 60) + minutes;
	        				currentArrivalTime = new Date(currentArrivalTime.getTime() + (minutesToAdd * 60000));
	        				arrivalTimes.add(sdf.format(currentArrivalTime));
		            	}else {
		            		String[] timeParts = midpoint.getTimeTaken().split(":");
			                int hours = Integer.parseInt(timeParts[0]);
			                int minutes = Integer.parseInt(timeParts[1]);

			                int minutesToAdd = (hours * 60) + minutes;
		    				currentArrivalTime = new Date(currentArrivalTime.getTime() + (minutesToAdd * 60000));
		            	}
		            }
		        }else {

		            for (SetUpMidPointsDTO midpoint : selectedMidPointList) {
		                String[] timeParts = midpoint.getTimeTaken().split(":");
		                int hours = Integer.parseInt(timeParts[0]);
		                int minutes = Integer.parseInt(timeParts[1]);

		                int minutesToAdd = (hours * 60) + minutes;

		                currentArrivalTime = new Date(currentArrivalTime.getTime() + (minutesToAdd * 60000));
		                arrivalTimes.add(sdf.format(currentArrivalTime));
		            }
		        } 
		    }catch (ParseException e) {
		        e.printStackTrace(); // Handle the parse exception as needed
		    }

		    return arrivalTimes;
	}

	
	public static List<String> calculateArrivalTimesOrg(String startTime, List<SetUpMidPointsDTO> selectedMidPointList, List<SetUpMidPointsDTO> desToOrgMidPointList) {
	    List<String> arrivalTimes = new ArrayList<>();
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	    try {
	        java.util.Date currentArrivalTime = sdf.parse(startTime);

	        if (desToOrgMidPointList.size() != selectedMidPointList.size()) {

	            for (SetUpMidPointsDTO midpoint : desToOrgMidPointList) {
	            	if(selectedMidPointList.contains(midpoint)) {
	            		String[] timeParts = midpoint.getTimeTaken().split(":");
    	                int hours = Integer.parseInt(timeParts[0]);
    	                int minutes = Integer.parseInt(timeParts[1]);

    	                int minutesToAdd = (hours * 60) + minutes;
        				currentArrivalTime = new Date(currentArrivalTime.getTime() + (minutesToAdd * 60000));
        				arrivalTimes.add(sdf.format(currentArrivalTime));
	            	}else {
	            		String[] timeParts = midpoint.getTimeTaken().split(":");
		                int hours = Integer.parseInt(timeParts[0]);
		                int minutes = Integer.parseInt(timeParts[1]);

		                int minutesToAdd = (hours * 60) + minutes;
	    				currentArrivalTime = new Date(currentArrivalTime.getTime() + (minutesToAdd * 60000));
	            	}
	            }
	        }else {

	            for (SetUpMidPointsDTO midpoint : selectedMidPointList) {
	                String[] timeParts = midpoint.getTimeTaken().split(":");
	                int hours = Integer.parseInt(timeParts[0]);
	                int minutes = Integer.parseInt(timeParts[1]);

	                int minutesToAdd = (hours * 60) + minutes;

	                currentArrivalTime = new Date(currentArrivalTime.getTime() + (minutesToAdd * 60000));
	                arrivalTimes.add(sdf.format(currentArrivalTime));
	            }
	        } 
	    }catch (ParseException e) {
	        e.printStackTrace(); // Handle the parse exception as needed
	    }

	    return arrivalTimes;
	}

	
	//Get Abbreviation To Drop Down
	@Override
	public List<SetUpMidPointsDTO> getAbbriviation() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SetUpMidPointsDTO> abbriviationList = new ArrayList<SetUpMidPointsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select abbreviation from public.panel_generator_origin_trip_details";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO setUpMidPointsDTO = new SetUpMidPointsDTO();
				setUpMidPointsDTO.setAbbreviation(rs.getString("abbreviation"));

				abbriviationList.add(setUpMidPointsDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return abbriviationList;

	}
	
	@Override
	public List<SetUpMidPointsDTO> getAbbriviationDes() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SetUpMidPointsDTO> abbriviationList = new ArrayList<SetUpMidPointsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select abbreviation from public.panel_generator_destination_trip_details";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO setUpMidPointsDTO = new SetUpMidPointsDTO();
				setUpMidPointsDTO.setAbbreviation(rs.getString("abbreviation"));

				abbriviationList.add(setUpMidPointsDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return abbriviationList;

	}
	
	//Get Permit No To Drop Down
	@Override
	public List<SetUpMidPointsDTO> getPermitNoToDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SetUpMidPointsDTO> permitNoList = new ArrayList<SetUpMidPointsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_permit_no FROM public.nt_t_pm_application";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO setUpMidPointsDTO = new SetUpMidPointsDTO();
				setUpMidPointsDTO.setPermitNo(rs.getString("pm_permit_no"));

				permitNoList.add(setUpMidPointsDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNoList;

	}
	
	//Get Bus No TO Drop Down
	@Override
	public List<SetUpMidPointsDTO> getBusNoToDropDown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SetUpMidPointsDTO> busNoList = new ArrayList<SetUpMidPointsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_vehicle_regno FROM public.nt_t_pm_application";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO setUpMidPointsDTO = new SetUpMidPointsDTO();
				setUpMidPointsDTO.setBusNo(rs.getString("pm_vehicle_regno"));

				busNoList.add(setUpMidPointsDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busNoList;

	}

	@Override
	public List<String> getPermitNoToDropDown(String routeNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> permitNoList = new ArrayList<String>();
		String permitNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT x.pm_permit_no FROM public.nt_t_pm_application x where x.pm_route_no = ? and pm_status = 'A'";

			ps = con.prepareStatement(query);
			ps.setString(1,routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitNo = rs.getString("pm_permit_no");

				permitNoList.add(permitNo);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return permitNoList;

	}
	
	//Get Bus No TO Drop Down
	@Override
	public List<String> getBusNoToDropDown(String routeNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> busNoList = new ArrayList<>();
		String busNo = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT x.pm_vehicle_regno  FROM public.nt_t_pm_application x where x.pm_route_no = ? and pm_status = 'A'";

			ps = con.prepareStatement(query);
			ps.setString(1,routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				busNo = rs.getString("pm_vehicle_regno");

				busNoList.add(busNo);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busNoList;

	}

//Save Selected data In The DataBase
@Override	
public void saveSelectedData(SetUpMidPointsDTO data){
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = ConnectionManager.getConnection();


			String query = "INSERT INTO public.nt_d_test (mid_point,time_taken) VALUES (?,?)";
			
			System.out.println(query);

			ps = con.prepareStatement(query);
			ps.setString(1, data.getMidPointName());
			ps.setString(2, data.getTimeTaken());
		    ps.executeUpdate();

			

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
	}

//Get Start Time
@Override
public List<SetUpMidPointsDTO> getStartTime(String routeNo, String serviceType) {
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<SetUpMidPointsDTO> startTimeList = new ArrayList<SetUpMidPointsDTO>();

	try {
		con = ConnectionManager.getConnection();

		String  sql = "SELECT DISTINCT a.rc_route_no,a.seq,c.trip_id,c.assigned_bus_no,c.start_time_slot,c.end_time_slot,c.bus_num,c.seq_no,b.pm_permit_no,d.code \r\n"
			         + " FROM public.nt_t_pm_application b \r\n" 
				     + "INNER JOIN public.nt_t_route_creator a on b.pm_route_no=a.rc_route_no \r\n"
				     +"INNER JOIN public.nt_m_timetable_generator_det c on a.seq = c.trip_id \r\n"
				     + "INNER JOIN public.nt_r_service_types d on b.pm_service_type=d.code \r\n" 
			         +"WHERE b.pm_route_no =? AND d.description = ?"+"ORDER BY c.trip_id";

      
		
		ps = con.prepareStatement(sql);
		ps.setString(1, routeNo);
		ps.setString(2, serviceType);
		rs = ps.executeQuery();
		

		while (rs.next()) {
			SetUpMidPointsDTO timeDetails = new SetUpMidPointsDTO();
			
			timeDetails.setStartTime(rs.getString("start_time_slot"));
			
			startTimeList.add(timeDetails);

		}

	} catch (Exception e) {
		e.printStackTrace();

	
	} finally {
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);
		ConnectionManager.close(con);
	}
	
	return startTimeList;
}


@Override
public SetUpMidPointsDTO getDetails(String routeNo, String serviceType) {
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<SetUpMidPointsDTO> startTimeList = new ArrayList<SetUpMidPointsDTO>();
	SetUpMidPointsDTO timeDetails = new SetUpMidPointsDTO();
	
	

	try {
		con = ConnectionManager.getConnection();

		String  sql = "SELECT DISTINCT rc_travel_time, rc_bus_speed, rc_length "
				+ "FROM public.nt_t_route_creator "
				+ "INNER JOIN public.nt_r_service_types ON nt_r_service_types.code = nt_t_route_creator.rc_bus_type "
				+ "WHERE rc_route_no =? AND nt_r_service_types.description = ?";

      
		
		ps = con.prepareStatement(sql);
		ps.setString(1, routeNo);
		ps.setString(2, serviceType);
		rs = ps.executeQuery();
		

		while (rs.next()) {
			
			timeDetails.setTime(rs.getString("rc_travel_time"));
			timeDetails.setSpeed(rs.getString("rc_bus_speed"));
			timeDetails.setLength(rs.getString("rc_length"));

		}

	} catch (Exception e) {
		e.printStackTrace();

	
	} finally {
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);
		ConnectionManager.close(con);
	}
	return timeDetails;

}

//get Mid Point Name Origin To Destination
@Override
public List<SetUpMidPointsDTO> getMidPointName(String routeNo, String serviceType){
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	List<SetUpMidPointsDTO> midPointNameList = new ArrayList<SetUpMidPointsDTO>();
	


	try {
		con = ConnectionManager.getConnection();

		String query = "SELECT DISTINCT a.mp_midpoint_code,a.seq,b.code,b.midpoint_name,c.code\r\n" 
	                   + "FROM public.nt_m_route_midpoint a\r\n" 
			           + "INNER JOIN public.nt_r_station b on a.mp_midpoint_code=b.code\r\n" 
	                   +"INNER JOIN public.nt_r_service_types c on a.mp_bus_type =c.code \r\n"
	                   + " WHERE a.mp_route_no =? AND c.description =?"+"ORDER BY a.seq";
		
		

		ps = con.prepareStatement(query);
		ps.setString(1, routeNo);
		ps.setString(2, serviceType);
		rs = ps.executeQuery();

		while (rs.next()) {
			SetUpMidPointsDTO midData = new SetUpMidPointsDTO();
			midData.setMidPointName(rs.getString("midpoint_name"));
			midPointNameList.add(midData);
			

		}

	} catch (Exception e) {

		e.printStackTrace();
	} finally {
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);
		ConnectionManager.close(con);
	}
	
	
	return midPointNameList;
	
}

//get Mid Point Time Taken Origin To Destination

public List<SetUpMidPointsDTO> getMidPointsTime(String routeNo, String serviceType){
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	List<SetUpMidPointsDTO> midPointList = new ArrayList<SetUpMidPointsDTO>();
	

	try {
		con = ConnectionManager.getConnection();


		String query = "SELECT DISTINCT a.mp_time_taken,a.mp_midpoint_code,a.seq,b.code,b.midpoint_name,c.code\r\n" 
		              + "FROM public.nt_m_route_midpoint a\r\n" 
				      + "INNER JOIN public.nt_r_station b on a.mp_midpoint_code=b.code\r\n" 
		              +"INNER JOIN public.nt_r_service_types c on a.mp_bus_type =c.code \r\n"
		              + " WHERE a.mp_route_no =? AND c.description =?"+"ORDER BY a.seq";
		
		

		ps = con.prepareStatement(query);

		ps.setString(1, routeNo);
		ps.setString(2, serviceType);
		rs = ps.executeQuery();

		while (rs.next()) {
			SetUpMidPointsDTO midData = new SetUpMidPointsDTO();

			midData.setTimeTaken(rs.getString("mp_time_taken"));
		
			midPointList.add(midData);

		}

	} catch (Exception e) {

		e.printStackTrace();
	} finally {
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);
		ConnectionManager.close(con);
	}
	
	return midPointList;
	
}


//Get MidPoint Total Travel Time Origin To Destination

@Override
public List<SetUpMidPointsDTO> getMidPointNameDes(String routeNo, String serviceType){
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	List<SetUpMidPointsDTO> midPointNameList = new ArrayList<SetUpMidPointsDTO>();
	


	try {
		con = ConnectionManager.getConnection();

		String query = "SELECT DISTINCT a.mp_time_taken,a.mp_midpoint_code,a.seq,b.code,b.midpoint_name,c.code\r\n" 
	                   + "FROM public.nt_m_route_midpoint_reverse a\r\n" 
		               + "INNER JOIN public.nt_r_station b on a.mp_midpoint_code=b.code\r\n"
		               + "INNER JOIN public.nt_r_service_types c on a.mp_bus_type =c.code \r\n"
	                   + " WHERE a.mp_route_no =? AND c.description =?"+"ORDER BY a.seq";

		

		ps = con.prepareStatement(query);
		ps.setString(1, routeNo);
		ps.setString(2, serviceType);
		rs = ps.executeQuery();

		while (rs.next()) {
			SetUpMidPointsDTO midData = new SetUpMidPointsDTO();
			midData.setMidPointName(rs.getString("midpoint_name"));
			midPointNameList.add(midData);
			

		}

	} catch (Exception e) {

		e.printStackTrace();
	} finally {
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);
		ConnectionManager.close(con);
	}
	
	
	return midPointNameList;
	
}

//get Mid Point Time Taken Destination To Origin
@Override
public List<SetUpMidPointsDTO> getMidPointsTimeDes(String routeNo, String serviceType){
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	List<SetUpMidPointsDTO> midPointTimeList = new ArrayList<SetUpMidPointsDTO>();
	


	try {
		con = ConnectionManager.getConnection();

		String query =  "SELECT DISTINCT a.mp_time_taken,a.mp_midpoint_code,a.seq,b.code,b.midpoint_name,c.code\r\n" 
		               + "FROM public.nt_m_route_midpoint_reverse a\r\n" 
			           + "INNER JOIN public.nt_r_station b on a.mp_midpoint_code=b.code\r\n"
			           + "INNER JOIN public.nt_r_service_types c on a.mp_bus_type =c.code \r\n"
		               + " WHERE a.mp_route_no =? AND c.description =?"+"ORDER BY a.seq";


		ps = con.prepareStatement(query);
		ps.setString(1, routeNo);
		ps.setString(2, serviceType);
		rs = ps.executeQuery();

		while (rs.next()) {
			SetUpMidPointsDTO midData = new SetUpMidPointsDTO();
			
			midData.setTimeTaken(rs.getString("mp_time_taken"));
			midPointTimeList.add(midData);

		}

	} catch (Exception e) {

		e.printStackTrace();
	} finally {
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);
		ConnectionManager.close(con);
	}
	

	  return midPointTimeList;

	
}


public List<BusDetailsDTO> getpermitNo(String routeNo){
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	List<BusDetailsDTO> startTimeList = new ArrayList<BusDetailsDTO>();

	try {
		con = ConnectionManager.getConnection();

		String query = "SELECT pm_permit_no \r\n"
		             + " FROM public.nt_t_pm_application  \r\n" 
			         +"WHERE pm_route_no = ? "
		             +"ORDER BY seq ";
      
		
		ps = con.prepareStatement(query);
		ps.setString(1, routeNo);
		
		rs = ps.executeQuery();
		

		while (rs.next()) {
			BusDetailsDTO timeDetails = new BusDetailsDTO();
			
			timeDetails.setStartTime(rs.getString("pm_permit_no"));
			
			startTimeList.add(timeDetails);

		}

	} catch (Exception e) {
		e.printStackTrace();

		return null;
	} finally {
		ConnectionManager.close(rs);
		ConnectionManager.close(ps);
		ConnectionManager.close(con);
	}
	
	return startTimeList;
	
}

//new codes	
	@Override
	public List<BusDetailsDTO> getDetailsByRouteAndServiceTypeDesToOrg(String routeNo, String serviceType, 
			List<SetUpMidPointsDTO> selectedDesToOrgMidPointList, List<SetUpMidPointsDTO> desToOrgMidPointList,String group ) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;	
		
		List<BusDetailsDTO> busDetailsList = new ArrayList<>();
		List<String> midList = new ArrayList<String>();
        int time = 0;
		
        try {
        	con = ConnectionManager.getConnection();
        	String sql = null;
        	  
        	// Only submit data
        	
            sql = "select DISTINCT on(panel_generator_destination_trip_details.id) "
            		+ "panel_generator_destination_trip_details.abbreviation,panel_generator_destination_trip_details.permit_no,"
            		+ "panel_generator_destination_trip_details.bus_no,panel_generator_destination_trip_details.start_time,"
            		+ "panel_generator_destination_trip_details.end_time,panel_generator_destination_trip_details.id,panel_generator_destination_trip_details.is_fixed  "
            		+ "from public.panel_generator_destination_trip_details "
            		+ "inner join public.panel_generator_general_data on panel_generator_general_data.refno = panel_generator_destination_trip_details.ref_no "
            		+ "inner join public.nt_m_panelgenerator "
            		+ "on panel_generator_general_data.refno = nt_m_panelgenerator.ref_no  "
            		+ "where panel_generator_destination_trip_details.route_no  = ? and  panel_generator_general_data.buscategory = ? "
            		+ "and panel_generator_general_data.draftdata = 'S' and  panel_generator_general_data.groupcount = ? "
            		+ "and nt_m_panelgenerator.status = 'A' "
            		+ "order by panel_generator_destination_trip_details.id;";
            

        	// both submit & saved data
        	
//        	sql = "select DISTINCT on(panel_generator_destination_trip_details.start_time) "
//            		+ "panel_generator_destination_trip_details.abbreviation,panel_generator_destination_trip_details.permit_no,"
//            		+ "panel_generator_destination_trip_details.bus_no,panel_generator_destination_trip_details.start_time,"
//            		+ "panel_generator_destination_trip_details.end_time,panel_generator_destination_trip_details.id "
//            		+ "from public.panel_generator_destination_trip_details "
//            		+ "inner join public.panel_generator_general_data on panel_generator_general_data.refno = panel_generator_destination_trip_details.ref_no "
//            		+ "where panel_generator_destination_trip_details.route_no  = ? and  panel_generator_general_data.buscategory = ? "
//            		+ "order by panel_generator_destination_trip_details.start_time;";

            ps = con.prepareStatement(sql);
            ps.setString(1, routeNo);
            ps.setString(2, serviceType);
            ps.setString(3, group);
            
        	for(SetUpMidPointsDTO data:selectedDesToOrgMidPointList) {
        		midList.add(data.getMidPointName());
        	}

            rs = ps.executeQuery();

                while (rs.next()) {
                	String startTime = rs.getString("start_time");
                	List<String> times = calculateArrivalTimesOrg(startTime, selectedDesToOrgMidPointList,desToOrgMidPointList);
                	
                    BusDetailsDTO busDetails = new BusDetailsDTO();
                    busDetails.setSeq(rs.getLong("id"));
                    busDetails.setBusNo(rs.getString("bus_no"));
                    busDetails.setPermitNo(rs.getString("permit_no"));
                    busDetails.setAbbreviation(rs.getString("abbreviation"));
                    busDetails.setStartTime(rs.getString("start_time"));
                    busDetails.setEndTime(rs.getString("end_time"));
                    busDetails.setFixedBusDes(rs.getBoolean("is_fixed"));
                    busDetails.setRouteNo(routeNo);
                    busDetails.setServiceType(serviceType);
                    busDetails.setMidList(midList);
                    busDetails.setTakenTime(times);
                    busDetailsList.add(busDetails);

                    time = time + 1;
                }
        } catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

        return busDetailsList;
         
    }
    
    public static List<String> calculateArrivalTimes(String startTime, List<SetUpMidPointsDTO> selectedMidPointList, List<SetUpMidPointsDTO> desToOrgMidPointList) {
        List<String> arrivalTimes = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try {
            java.util.Date currentArrivalTime = sdf.parse(startTime);
            
            int desToOrgMidPointListSize = desToOrgMidPointList.size();
            int selectedMidPointListSize = selectedMidPointList.size();
            
            if(!(desToOrgMidPointListSize == selectedMidPointListSize)) {
            	for (SetUpMidPointsDTO midpoint : desToOrgMidPointList) {
                	for (SetUpMidPointsDTO midpointCheck : selectedMidPointList) {
                		if(!midpoint.getMidPointName().equals(midpointCheck.getMidPointName())) {
                			if(midpoint.getSeq()< midpointCheck.getSeq()) {
                				String[] timeParts = midpoint.getTimeTaken().split(":");
                                int hours = Integer.parseInt(timeParts[0]);
                                int minutes = Integer.parseInt(timeParts[1]);

                                int minutesToAdd = (hours * 60) + minutes;

                                currentArrivalTime = new Date(currentArrivalTime.getTime() + (minutesToAdd * 60000));
                               
                			}
                			
                		}
                		
                	}
                }
            }
            
            for (SetUpMidPointsDTO midpoint : selectedMidPointList) {
            	String[] timeParts = midpoint.getTimeTaken().split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);

                int minutesToAdd = (hours * 60) + minutes;

                // Add minutesToAdd to currentArrivalTime
                currentArrivalTime = new Date(currentArrivalTime.getTime() + (minutesToAdd * 60000));
                arrivalTimes.add(sdf.format(currentArrivalTime));
                
                currentArrivalTime = new Date(currentArrivalTime.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the parse exception as needed
        }

        return arrivalTimes;
    }
	
    @Override
    public void updateTakenTime(List<SetUpMidPointsDTO> selectedOrgToDesMidPointList, List<SetUpMidPointsDTO> selectedDesToOrgMidPointList) {
    	Connection con = null;
		PreparedStatement psUpdateOriToDes = null;
		PreparedStatement psUpdateDesToOri = null;

		try {
			String updateSqlOriToDes = null;
			String updateSqlDesToOri = null;
			con = ConnectionManager.getConnection();

			updateSqlOriToDes = "UPDATE public.nt_m_route_midpoint SET mp_time_taken = ? WHERE seq = ?";
			updateSqlDesToOri = "UPDATE public.nt_m_route_midpoint_reverse SET mp_time_taken = ? WHERE seq = ?";
			
			psUpdateOriToDes = con.prepareStatement(updateSqlOriToDes);
			psUpdateDesToOri = con.prepareStatement(updateSqlDesToOri);
			
			for (SetUpMidPointsDTO data : selectedOrgToDesMidPointList) {
				psUpdateOriToDes.setString(1, data.getTimeTaken());
				psUpdateOriToDes.setLong(2, data.getSeq());

				psUpdateOriToDes.executeUpdate();
				con.commit();
			}
			
			for (SetUpMidPointsDTO data : selectedDesToOrgMidPointList) {
				psUpdateDesToOri.setString(1, data.getTimeTaken());
				psUpdateDesToOri.setLong(2, data.getSeq());

				psUpdateDesToOri.executeUpdate();
				con.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(psUpdateDesToOri);
			ConnectionManager.close(psUpdateOriToDes);
			ConnectionManager.close(con);
		}
    }
    
	public void setMidPointDataOrgToDes(List<BusDetailsDTO> selectedOrgToDesBusDetailsList, String group) {
		Connection con = null;
		PreparedStatement psInsert = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;

		try {
			String updateSql = null;
			String insertSql = null;

			con = ConnectionManager.getConnection();

			insertSql = "INSERT INTO public.nt_m_midpoint_o_to_d "
					+ "(mp_abbreviation, mp_permit_no, mp_bus_no, mp_origin, mp_start_time, mp_midpoint_time, "
					+ "mp_destination, mp_endtime, seq, mp_midpoint, mp_route_no, mp_service_type, isfixed,group_no,running_no) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			updateSql = "UPDATE public.nt_m_midpoint_o_to_d SET mp_abbreviation = ?, mp_permit_no = ?, seq = ?, mp_origin = ?,"
					+ " mp_start_time = ?, mp_destination = ?, mp_endtime = ?, mp_midpoint = ?, mp_route_no = ?, mp_service_type = ?,isfixed = ?, running_no = ? "
					+ "WHERE mp_bus_no = ? AND mp_midpoint_time = ? and group_no = ?";

			psInsert = con.prepareStatement(insertSql);
			psUpdate = con.prepareStatement(updateSql);

			for (BusDetailsDTO data : selectedOrgToDesBusDetailsList) {
				try {
					List<String> takenTimeList = data.getTakenTime();
					List<String> midPointList = data.getMidList();

					int minSize = Math.min(takenTimeList.size(), midPointList.size());

					for (int i = 0; i < minSize; i++) {
						String takenTime = takenTimeList.get(i);
						String midPoint = midPointList.get(i);

						psUpdate.setString(1, data.getAbbreviation());
						psUpdate.setString(2, data.getPermitNo());
						psUpdate.setLong(3, data.getSeq());
						psUpdate.setString(4, origin);
						psUpdate.setString(5, data.getStartTime());
						psUpdate.setString(6, destination);
						psUpdate.setString(7, data.getEndTime());
						psUpdate.setString(8, midPoint);
						psUpdate.setString(9, data.getRouteNo());
						psUpdate.setString(10, data.getServiceType());
						psUpdate.setString(11, data.getFix());
						psUpdate.setString(12, data.getAbbreviation());
						psUpdate.setString(13, data.getBusNo());
						psUpdate.setString(14, takenTime);
						psUpdate.setString(15, group);

						int updateCount = psUpdate.executeUpdate();
						con.commit();

						if (updateCount == 0) {
							psInsert.setString(1, data.getAbbreviation());
							psInsert.setString(2, data.getPermitNo());
							psInsert.setString(3, data.getBusNo());
							psInsert.setString(4, origin);
							psInsert.setString(5, data.getStartTime());
							psInsert.setString(6, takenTime);
							psInsert.setString(7, destination);
							psInsert.setString(8, data.getEndTime());
							psInsert.setLong(9, data.getSeq());
							psInsert.setString(10, midPoint);
							psInsert.setString(11, data.getRouteNo());
							psInsert.setString(12, data.getServiceType());
							psInsert.setString(13, data.getFix());
							psInsert.setString(14, group);
							psInsert.setString(15, data.getAbbreviation());
							psInsert.executeUpdate();
							con.commit();
						}
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
    
    public boolean setMidPointDataDesToOrg(List<BusDetailsDTO> selectedDesToOrgBusDetailsList,String group) {
    	Connection con = null;
		PreparedStatement psInsert = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		
		boolean notEmpty = false;
		
		try {
			String updateSql = null;
			String insertSql = null;

			con = ConnectionManager.getConnection();
			
			insertSql = "INSERT INTO public.nt_m_midpoint_d_to_o "
					+ "(mp_abbreviation, mp_permit_no, mp_bus_no, mp_origin, mp_start_time, mp_midpoint_time, "
					+ "mp_destination, mp_endtime, seq, mp_midpoint, mp_route_no, mp_service_type, isfixed,group_no,running_no) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			updateSql = "UPDATE public.nt_m_midpoint_d_to_o SET mp_abbreviation = ?, mp_permit_no = ?, seq = ?, mp_origin = ?,"
					+ " mp_start_time = ?, mp_destination = ?, mp_endtime = ?, mp_midpoint = ?, mp_route_no = ?, mp_service_type = ?,isfixed = ?, running_no = ? "
					+ "WHERE mp_bus_no = ? AND mp_midpoint_time = ? and group_no = ?";

			
			psInsert = con.prepareStatement(insertSql);
			psUpdate = con.prepareStatement(updateSql);
			
			for (BusDetailsDTO data : selectedDesToOrgBusDetailsList) {
			    List<String> takenTimeList = data.getTakenTime();
			    List<String> midPointList = data.getMidList();

			    int minSize = Math.min(takenTimeList.size(), midPointList.size());

			    try {
			    	for (int i = 0; i < minSize; i++) {
				        String takenTime = takenTimeList.get(i);
				        String midPoint = midPointList.get(i);

				        psUpdate.setString(1, data.getAbbreviation());
				        psUpdate.setString(2, data.getPermitNo());
				        psUpdate.setLong(3, data.getSeq());
				        psUpdate.setString(4, origin);
				        psUpdate.setString(5, data.getStartTime());
				        psUpdate.setString(6, destination);
				        psUpdate.setString(7, data.getEndTime());
				        psUpdate.setString(8, midPoint);
				        psUpdate.setString(9, data.getRouteNo());
				        psUpdate.setString(10, data.getServiceType());
				        psUpdate.setString(11, data.getFix());
				        psUpdate.setString(12, data.getAbbreviation());
				        psUpdate.setString(13, data.getBusNo());
				        psUpdate.setString(14, takenTime);
				        psUpdate.setString(15, group);
				        int updateCount = psUpdate.executeUpdate();
				        con.commit();

				        if (updateCount == 0) {
				            psInsert.setString(1, data.getAbbreviation());
				            psInsert.setString(2, data.getPermitNo());
				            psInsert.setString(3, data.getBusNo());
				            psInsert.setString(4, origin);
				            psInsert.setString(5, data.getStartTime());
				            psInsert.setString(6, takenTime);
				            psInsert.setString(7, destination);
				            psInsert.setString(8, data.getEndTime());
				            psInsert.setLong(9, data.getSeq());
				            psInsert.setString(10, midPoint);
				            psInsert.setString(11, data.getRouteNo());
				            psInsert.setString(12, data.getServiceType());
				            psInsert.setString(13, data.getFix());
				            psInsert.setString(14, group);
				            psInsert.setString(15, data.getAbbreviation());
				            psInsert.executeUpdate();
				            con.commit();
				        }
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
		return notEmpty;
    }
    
    public void updateTimeOrigin(List<BusDetailsDTO> selectedOrgToDesBusDetailsList) {
    	Connection con = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		
		try {
			String updateSql = null;

			con = ConnectionManager.getConnection();
	
			updateSql = "UPDATE public.panel_generator_origin_trip_details "
					+ "SET abbreviation = ?,permit_no = ?,bus_no = ?,start_time = ?,end_time = ? "
					+ "WHERE id = ?";
			
			psUpdate = con.prepareStatement(updateSql);
			
			for (BusDetailsDTO data : selectedOrgToDesBusDetailsList) {
				try {
					psUpdate.setString(1, data.getAbbreviation());
			        psUpdate.setString(2, data.getPermitNo());
			        psUpdate.setString(3, data.getBusNo());
			        psUpdate.setString(4, data.getStartTime());
			        psUpdate.setString(5, data.getEndTime());
			        psUpdate.setLong(6, data.getSeq());
			       
			        psUpdate.executeUpdate();
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
	        ConnectionManager.close(con);
		}

    }
    
    public void updateTimeDestination(List<BusDetailsDTO> selectedOrgToDesBusDetailsList) {
    	Connection con = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		
		try {
			String updateSql = null;

			con = ConnectionManager.getConnection();
	
			updateSql = "UPDATE public.panel_generator_destination_trip_details "
					+ "SET abbreviation = ?,permit_no = ?,bus_no = ?,start_time = ?,end_time = ? "
					+ "WHERE id = ?";

			psUpdate = con.prepareStatement(updateSql);

			for (BusDetailsDTO data : selectedOrgToDesBusDetailsList) {
				try {
					psUpdate.setString(1, data.getAbbreviation());
					psUpdate.setString(2, data.getPermitNo());
					psUpdate.setString(3, data.getBusNo());
					psUpdate.setString(4, data.getStartTime());
					psUpdate.setString(5, data.getEndTime());
					psUpdate.setLong(6, data.getSeq());

					psUpdate.executeUpdate();
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
	        ConnectionManager.close(con);
		}

    }
    
    public static String getOrigin() {
		return origin;
	}

	public static void setOrigin(String origin) {
		SetUpMidPointsServiceImpl.origin = origin;
	}

	public static String getDestination() {
		return destination;
	}

	public static void setDestination(String destination) {
		SetUpMidPointsServiceImpl.destination = destination;
	}

	
}


