package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lk.informatics.ntc.model.dto.BusDetailsDTO;
import lk.informatics.ntc.model.dto.CombinePanelGenaratorDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.RouteScheduleMasterDTO;
import lk.informatics.ntc.model.dto.RouteSetUpDTO;
import lk.informatics.ntc.model.dto.SetUpMidPointsDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class CombinePanelGenaratorServiceImpl implements CombinePanelGenaratorService {

	@Override
	public List<CombinePanelGenaratorDTO> getRouteNoList() {
		List<CombinePanelGenaratorDTO> routeNoList = new ArrayList<CombinePanelGenaratorDTO>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT DISTINCT routeno,origin,destination FROM public.panel_generator_general_data";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				CombinePanelGenaratorDTO combinePanelDTO = new CombinePanelGenaratorDTO();
				String des = rs.getString("origin") + " - " +rs.getString("destination");
				combinePanelDTO.setRouteNo(rs.getString("routeno"));
				combinePanelDTO.setRouteDes(des);
				routeNoList.add(combinePanelDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeNoList;
	}

	@Override
	public CombinePanelGenaratorDTO getOriginDestination(String routeNo) {
		CombinePanelGenaratorDTO combinePanelDTO = new CombinePanelGenaratorDTO();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT rou_service_origine,rou_service_destination FROM public.nt_r_route WHERE rou_number = ? AND active ='A'";

			ps = con.prepareStatement(sql);
			ps.setString(1, routeNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				combinePanelDTO.setOrigin(rs.getString("rou_service_origine"));
				combinePanelDTO.setDestination(rs.getString("rou_service_destination"));
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
		return combinePanelDTO;
	}

	@Override
	public List<CombinePanelGenaratorDTO> getDetails(String routeNo, String serviceType, String groupNo,
			String defineSide) {
		List<CombinePanelGenaratorDTO> detailsList = new ArrayList<CombinePanelGenaratorDTO>();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT origin,destination FROM public.test_dhananjika WHERE routeno = ?";

			ps = con.prepareStatement(sql);
			ps.setString(1, routeNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				CombinePanelGenaratorDTO combinePanelDTO = new CombinePanelGenaratorDTO();
				combinePanelDTO.setOrigin(rs.getString("origin"));
				combinePanelDTO.setDestination(rs.getString("destination"));
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
		return detailsList;
	}

	@Override
	public List<SetUpMidPointsDTO> getAllMidPointsOrgToDes(String routeNo, String serviceType, String groupNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SetUpMidPointsDTO> midPointList = new ArrayList<SetUpMidPointsDTO>();

		try {
			con = ConnectionManager.getConnection();

//			Error in sql code

			String query ="SELECT DISTINCT  on(mp_midpoint) mp_midpoint,id FROM public.nt_m_midpoint_o_to_d  WHERE mp_route_no = ? and mp_service_type = ? and group_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			ps.setString(3, groupNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO midData = new SetUpMidPointsDTO();

				midData.setMidPointName(rs.getString("mp_midpoint"));
				midData.setSeq(rs.getLong("id"));
			
				midPointList.add(midData);

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

		return midPointList;
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
	
	@Override
	public List<String> getAllMidPointsTimeOrgToDes(List<SetUpMidPointsDTO> originDataList) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> midPointList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

//			Error in sql code

			String query = "SELECT details.* " + "FROM public.nt_d_setup_midpoint details "
					+ "WHERE route_number = ? AND service_type = ?";

			ps = con.prepareStatement(query);
			
			rs = ps.executeQuery();

			while (rs.next()) {
			

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

		return midPointList;
	}
	
	public List<SetUpMidPointsDTO> getAllMidPointsDesToOri(String routeNo, String serviceType, String groupNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<SetUpMidPointsDTO> midPointList = new ArrayList<SetUpMidPointsDTO>();

		try {
			con = ConnectionManager.getConnection();

//			Error in sql code

			String query = "SELECT DISTINCT on(mp_midpoint) mp_midpoint,id FROM public.nt_m_midpoint_d_to_o  WHERE mp_route_no = ? and mp_service_type = ? and group_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			ps.setString(3, groupNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				SetUpMidPointsDTO midData = new SetUpMidPointsDTO();

				midData.setMidPointName(rs.getString("mp_midpoint"));
				midData.setSeq(rs.getLong("id"));
			
				midPointList.add(midData);

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

		return midPointList;
	}


	@Override
	public SetUpMidPointsDTO getAllDetailsOrgToDes(String routeNo, String serviceType, String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		SetUpMidPointsDTO midData = new SetUpMidPointsDTO();
		
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT details.origin,details.destination,details.routeno,details.id, service.description "
					+ "FROM public.panel_generator_general_data details "
					+ "INNER JOIN public.nt_r_service_types service on details.buscategory = service.code "
					+ "WHERE details.routeno = ? AND details.buscategory =?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				midData.setRouteNo(rs.getString("routeno"));
				midData.setOrigin(rs.getString("origin"));
				midData.setDestination(rs.getString("destination"));
				midData.setSeq(rs.getLong("id"));
				midData.setServiceType(rs.getString("description"));
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
		return midData;	
	}
	
	@Override
	public CombinePanelGenaratorDTO getBusData(String routeNo, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		CombinePanelGenaratorDTO midData = new CombinePanelGenaratorDTO();
		
		try {
			con = ConnectionManager.getConnection();

//			Error in sql code

			String query = "SELECT DISTINCT details.* FROM public.test_dhana details "
					+ "WHERE routeno = ? AND servicetype = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				midData.setBusNo(rs.getString("busno"));
				midData.setPermitNo(rs.getString("permitno"));
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
		return midData;

		
	}

	@Override
	public BusDetailsDTO getBusDetails(String routeNo, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		BusDetailsDTO busData = new BusDetailsDTO();
		
		try {
			con = ConnectionManager.getConnection();

//			Error in sql code

			String query = "SELECT DISTINCT details.*, service.description FROM public.nt_d_setup_midpoint details "
					+ "INNER JOIN public.nt_r_service_types service on details.service_type = service.code "
					+ "WHERE route_number = ? AND service_type = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				busData.setPermitNo(rs.getString("pm_permit_no"));
				busData.setBusNo(rs.getString("pm_vehicle_regno"));
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
		return busData;
		
	}
	
	@Override
	public void setMidPointData(List<SetUpMidPointsDTO> selectedDetailsList) {
		Connection con = null;
		PreparedStatement psInsert = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		
		try {
			String updateSql = null;
			String insertSql = null;

			con = ConnectionManager.getConnection();
			
			insertSql = "INSERT INTO public.nt_t_combine_panel_generator "
					+ "(service_type, busno, permitno, origin, destination, midpoint, start_time, end_time, mid_time, route_no) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			
			updateSql = "UPDATE public.nt_t_combine_panel_generator "
					+ "SET service_type = ?, "
					+ "busno = ?, "
					+ "permitno = ?, "
					+ "origin = ?, "
					+ "destination = ?, "
					+ "midpoint = ?, "
					+ "start_time = ?, "
					+ "end_time = ? "
					+ "WHERE route_no = ? AND mid_time = ?";
			
			psInsert = con.prepareStatement(insertSql);
			psUpdate = con.prepareStatement(updateSql);
			
			for (SetUpMidPointsDTO data : selectedDetailsList) {
				Map.Entry<String, String> midpoints = data.getMidpoints().entrySet().iterator().next();
				Map.Entry<String, String> startTimes = data.getStartTimes().entrySet().iterator().next();
				Map.Entry<String, String> endTimes = data.getEndTimes().entrySet().iterator().next();
				psUpdate.setString(1, data.getCode());
		        psUpdate.setString(2, data.getBusNo());
		        psUpdate.setString(3, data.getPermitNo());
		        psUpdate.setString(4, data.getOrigin());
		        psUpdate.setString(5, data.getDestination());
		        psUpdate.setString(6, midpoints.getKey());
		        psUpdate.setString(7, startTimes.getValue());
		        psUpdate.setString(8, endTimes.getValue());
		        psUpdate.setString(9, data.getRouteNo());
		        psUpdate.setString(10, midpoints.getValue());
		        
		        int updateCount = psUpdate.executeUpdate();
		        con.commit();
		        
		        if (updateCount == 0) {
		        	psInsert.setString(1, data.getCode());
			        psInsert.setString(2, data.getBusNo());
			        psInsert.setString(3, data.getPermitNo());
			        psInsert.setString(4, data.getOrigin());
			        psInsert.setString(5, data.getDestination());
			        psInsert.setString(6, midpoints.getKey());
			        psInsert.setString(7, startTimes.getValue());
			        psInsert.setString(8, endTimes.getValue());
			        psInsert.setString(9, midpoints.getValue());
			        psInsert.setString(10, data.getRouteNo());
			        
			        psInsert.executeUpdate();
		            con.commit();
		        }
		        
			}
			
		} catch (SQLException e) {
	        try {
	            if (con != null) {
	                con.rollback();
	            }
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
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
	public void updateMidPointData(List<SetUpMidPointsDTO> selectedDetailsList) {
		Connection con = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;
		
		try {
			String updateSql = null;

			con = ConnectionManager.getConnection();
					 
			updateSql = "UPDATE public.nt_t_combine_panel_generator "
					+ "SET mid_time = ? "
					+ "WHERE route_no = ? AND midpoint = ?";
			
			psUpdate = con.prepareStatement(updateSql);
			
			for (SetUpMidPointsDTO data : selectedDetailsList) {
				Map<String, String> takenTimeMap = data.getMidpoints();
			    List<String> midPointList = data.getSelectedMidName();
			    
			    int minSize = Math.min(takenTimeMap.size(), midPointList.size());

			    for (int i = 0; i < minSize; i++) {
			        String midPoint = midPointList.get(i);
			        String takenTime = takenTimeMap.get(midPoint);
			        
			        psUpdate.setString(1, takenTime);
					psUpdate.setString(2, data.getRouteNo());
					psUpdate.setString(3, midPoint);
					       
				    psUpdate.executeUpdate();
				    con.commit();
			    }
			}
			
		}catch (SQLException e) {
	        try {
	            if (con != null) {
	                con.rollback();
	            }
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
	        e.printStackTrace();

	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
	        ConnectionManager.close(psUpdate);
	        ConnectionManager.close(con);
		}
	}
	
	@Override
	public long getDeviceNo() {
		long deviceNo = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String sql = null;
			con = ConnectionManager.getConnection();

			sql = "SELECT MAX(userid) AS max_value FROM nt_m_combinePanal_temp;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				deviceNo = rs.getLong("max_value");
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
		
		return deviceNo;		
	}

	
	
	@Override
	public LinkedHashMap<String, LinkedHashMap<String, List<String>>> getAllTimesForMidPoint(String midPoint, String rangeStart,String rangeEnd, List<String> routes) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;

		LinkedHashMap<String, LinkedHashMap<String, List<String>>> midPointList = new LinkedHashMap<>();
		LinkedHashMap<String, List<String>> hashmap = new LinkedHashMap<>();

		try {
			con = ConnectionManager.getConnection();
			String query = null;
			String sql = null;
			String side = null;
			
				query = "select mp_route_no,mp_start_time,group_no from nt_m_midpoint_o_to_d where mp_midpoint = ? "
						+ "and  mp_start_time between ? and ?";

				sql = "select mp_route_no,mp_start_time,group_no from nt_m_midpoint_d_to_o where mp_midpoint = ? "
						+ "and  mp_start_time between ? and ?";


		
			ps = con.prepareStatement(query);
			ps.setString(1, midPoint);
			ps.setString(2, rangeStart);
			ps.setString(3, rangeEnd);
			rs = ps.executeQuery();

			while (rs.next()) {
				String route = rs.getString("mp_route_no") + " - " + rs.getString("group_no");

				if (routes.contains(route)) {
					side = rs.getString("mp_route_no") + " - Origin to Destination";
					if (!hashmap.containsKey(side)) {
						List<String> startTimeList = new ArrayList<String>();
						startTimeList.add(rs.getString("mp_start_time"));
						hashmap.put(side, startTimeList);
					} else {
						List<String> startTimeList = hashmap.get(side);
						startTimeList.add(rs.getString("mp_start_time"));
					}
				}
			}
			
			
			ps1 = con.prepareStatement(sql);
			ps1.setString(1, midPoint);
			ps1.setString(2, rangeStart);
			ps1.setString(3, rangeEnd);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				String route = rs1.getString("mp_route_no") + " - " + rs1.getString("group_no");

				if (routes.contains(route)) {
					side = rs1.getString("mp_route_no") + " - Destination to Origin";
					if (!hashmap.containsKey(side)) {
						List<String> startTimeList = new ArrayList<String>();
						startTimeList.add(rs1.getString("mp_start_time"));
						hashmap.put(side, startTimeList);
					} else {
						List<String> startTimeList = hashmap.get(side);
						startTimeList.add(rs1.getString("mp_start_time"));
					}
				}
			}
			
			for(Map.Entry<String, List<String>> entry : hashmap.entrySet()) {
				String sideWithRoute = entry.getKey();
				List<String> times = entry.getValue();
				
				if (midPointList.containsKey(sideWithRoute)) {
					LinkedHashMap<String, List<String>> innerMap1 = midPointList.get(sideWithRoute);
					innerMap1.put(midPoint, times);
				} else {
					LinkedHashMap<String, List<String>> innerMap1 = new LinkedHashMap<>();
					innerMap1.put(midPoint, times);
					midPointList.put(sideWithRoute, innerMap1);
				}
				
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

		return midPointList;
	
	}
	
	@Override
	public boolean getAllTimes(String routeNo, String serviceType,String group,String side, long deviceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement insertPs = null;
		ResultSet rs = null;

		boolean sucess = false;

		try {
			con = ConnectionManager.getConnection();
			String query = null;
			//modified by danilka.j - to exclude midpoints equal to the destination
			if(side.equals("Origin to Destination")) {
				query = "select distinct * from public.nt_m_midpoint_o_to_d where mp_route_no = ? and mp_service_type = ? and group_no = ? "
						+ "AND mp_destination <> mp_midpoint order by id";
			}else {
				query = "select distinct * from public.nt_m_midpoint_d_to_o where mp_route_no = ? and mp_service_type = ? and group_no = ? "
						+ "AND mp_destination <> mp_midpoint order by id";
			}
			
			ps = con.prepareStatement(query);
			ps.setString(1, routeNo);
			ps.setString(2, serviceType);
			ps.setString(3, group);
			rs = ps.executeQuery();

			while (rs.next()) {
				String insertSql = "INSERT INTO public.nt_m_combinePanal_temp "
						+ "(mp_permit_no, mp_bus_no, mp_origin, mp_start_time, mp_midpoint_time, "
						+ "mp_destination, mp_endtime, mp_midpoint, mp_route_no, mp_service_type,group_no,side,userid,running_no) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				insertPs = con.prepareStatement(insertSql);
				
				insertPs.setString(1, rs.getString("mp_permit_no"));
				insertPs.setString(2, rs.getString("mp_bus_no"));
				insertPs.setString(3, rs.getString("mp_origin"));
				insertPs.setString(4, rs.getString("mp_start_time"));
				insertPs.setString(5, rs.getString("mp_midpoint_time"));
				insertPs.setString(6, rs.getString("mp_destination"));
				insertPs.setString(7, rs.getString("mp_endtime"));
				insertPs.setString(8, rs.getString("mp_midpoint"));
				insertPs.setString(9, rs.getString("mp_route_no"));
				insertPs.setString(10, rs.getString("mp_service_type"));
				insertPs.setString(11, group);
				insertPs.setString(12, side);
				insertPs.setLong(13, deviceNo);
				if(rs.getString("isfixed").equals("Y")) {
					insertPs.setString(14, rs.getString("mp_bus_no"));
				}else {
					insertPs.setString(14, rs.getString("running_no"));
				}
				
				
				int insertCount = insertPs.executeUpdate();
	            if (insertCount > 0) {
	            	sucess = true;
	            }
	        }

	        con.commit();

		} catch (SQLException e) {
	        try {
	            if (con != null) {
	                con.rollback();
	            }
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
	        e.printStackTrace();

	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(insertPs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return sucess;
	
	}
	
	@Override
	public List<SetUpMidPointsDTO> getAllTimeDataForTable(List<String> routes,long deviceNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		String prevStartTime = null;
		String prevEndTime = null;
		String prevBusNo = null;
		int listID = 0;
		List<SetUpMidPointsDTO> midPointList = new ArrayList<SetUpMidPointsDTO>();
		List<String> destinationList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			
			String sql = "SELECT DISTINCT mp_destination FROM public.nt_m_combinepanal_temp WHERE userid = ?";
			ps1 = con.prepareStatement(sql);
			ps1.setLong(1, deviceNo);
			rs1 = ps1.executeQuery();
			
			while (rs1.next()) {
				destinationList.add(rs1.getString("mp_destination"));
			}

			String query = "select * from nt_m_combinepanal_temp where userid = ? order by mp_start_time ";

			ps = con.prepareStatement(query);
			ps.setLong(1, deviceNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				
				String route = rs.getString("mp_route_no") + "-" + rs.getString("mp_service_type") + "-" + rs.getString("group_no") + "-" + rs.getString("side");
				if(routes.contains(route)) {
					Map<String, String> midpoints = new LinkedHashMap<>();
					Map<String, String> startTimes = new LinkedHashMap<>();
					Map<String, String> endTimes = new LinkedHashMap<>();
					if (Objects.equals(rs.getString("mp_start_time"), prevStartTime) &&
				            Objects.equals(rs.getString("mp_endtime"), prevEndTime) &&
				            Objects.equals(rs.getString("mp_bus_no"), prevBusNo) && !destinationList.contains(rs.getString("mp_midpoint"))) {						
						midpoints.put(rs.getString("mp_midpoint"), rs.getString("mp_midpoint_time"));
						midPointList.get(listID-1).getMidpoints().put(rs.getString("mp_midpoint"), rs.getString("mp_midpoint_time"));
					}
					else {
						
						
						if(!(Objects.equals(rs.getString("mp_start_time"), prevStartTime) &&
					            Objects.equals(rs.getString("mp_endtime"), prevEndTime) &&
					            Objects.equals(rs.getString("mp_bus_no"), prevBusNo)) && destinationList.contains(rs.getString("mp_midpoint"))){
							endTimes.put(rs.getString("mp_midpoint"), rs.getString("mp_midpoint_time"));
						}
						else if (Objects.equals(rs.getString("mp_start_time"), prevStartTime) &&
					            Objects.equals(rs.getString("mp_endtime"), prevEndTime) &&
					            Objects.equals(rs.getString("mp_bus_no"), prevBusNo) && destinationList.contains(rs.getString("mp_midpoint"))) {
							
							endTimes.put(rs.getString("mp_midpoint"), rs.getString("mp_midpoint_time"));
							midPointList.get(listID-1).getEndTimes().put(rs.getString("mp_midpoint"), rs.getString("mp_midpoint_time"));

						}else if (Objects.equals(rs.getString("mp_start_time"), prevStartTime) &&
					            Objects.equals(rs.getString("mp_endtime"), prevEndTime) &&
					            Objects.equals(rs.getString("mp_bus_no"), prevBusNo)) {
							
							midpoints.put(rs.getString("mp_midpoint"), rs.getString("mp_midpoint_time"));
							midPointList.get(listID-1).getMidpoints().put(rs.getString("mp_midpoint"), rs.getString("mp_midpoint_time"));
						}else {
							SetUpMidPointsDTO midData = new SetUpMidPointsDTO();
							midData.setPermitNo(rs.getString("mp_permit_no"));
							midData.setBusNo(rs.getString("mp_bus_no"));
							midData.setOrigin(rs.getString("mp_origin"));
							midData.setDestination(rs.getString("mp_destination"));
							midData.setRouteNo(rs.getString("mp_route_no"));
							midData.setCode(rs.getString("mp_service_type"));
							if (rs.getString("mp_service_type").equalsIgnoreCase("001")) {
								midData.setServiceType("NORMAL");
							}
							else if (rs.getString("mp_service_type").equalsIgnoreCase("002")) {
								midData.setServiceType("LUXURY");
							}
							else if (rs.getString("mp_service_type").equalsIgnoreCase("003")) {
								midData.setServiceType("SUPER LUXURY");
							}
							else if (rs.getString("mp_service_type").equalsIgnoreCase("004")) {
								midData.setServiceType("SEMI-LUXURY");
							}
							else if (rs.getString("mp_service_type").equalsIgnoreCase("EB")) {
								midData.setServiceType("EXPRESSWAY BUS");
							}
							midData.setGroup(rs.getString("group_no"));
							midData.setTripType(rs.getString("side"));
							
							startTimes.put(rs.getString("mp_origin"), rs.getString("mp_start_time"));
							midpoints.put(rs.getString("mp_midpoint"), rs.getString("mp_midpoint_time"));
							endTimes.put(rs.getString("mp_destination"), rs.getString("mp_endtime"));
							
							midData.setStartTimes(startTimes);
							midData.setEndTimes(endTimes);
							midData.setMidpoints(midpoints);
							listID++;
							

							midData.setAbbreviation(rs.getString("running_no"));
							midPointList.add(midData);
						}

						 prevStartTime = rs.getString("mp_start_time");
					     prevEndTime = rs.getString("mp_endtime");
					     prevBusNo = rs.getString("mp_bus_no");
					}
					
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return midPointList;
	}
	
	@Override
	public void clearPreviousData(long deviceNo) {
		Connection con = null;
		PreparedStatement ps = null;
		
		try {

			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_m_combinePanal_temp WHERE userid = ?";

			ps = con.prepareStatement(query);
			ps.setLong(1, deviceNo);
			ps.executeUpdate();
			con.commit();

		}catch (SQLException e) {
	        try {
	            if (con != null) {
	                con.rollback();
	            }
	        } catch (SQLException rollbackException) {
	            rollbackException.printStackTrace();
	        }
	        e.printStackTrace();

	    } catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
	}
	
	@Override
	public void updateTables(SetUpMidPointsDTO data, String table, long deviceNo) {
		Connection con = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement ps5 = null;
		PreparedStatement ps6 = null;
		ResultSet rs = null;
		String ref = null;
		
		try {
			String query = null;
			String sql1 = null;
			String sql2 = null;
			String sql3 = null;
			String sql4 = null;
			String sql5 = null;
			String sql6 = null;

			con = ConnectionManager.getConnection();
			
			query = "SELECT nt_m_panelgenerator.ref_no  FROM public.nt_m_panelgenerator "
					+ "inner join public.nt_t_panelgenerator_det on nt_m_panelgenerator.seq = nt_t_panelgenerator_det.seq_panelgenerator "
					+ "where nt_m_panelgenerator.route_no  = ? and nt_t_panelgenerator_det.bus_category  = ? "
					+ "and nt_t_panelgenerator_det.group_no  = ?";
			
			ps1 = con.prepareStatement(query);
			ps1.setString(1, data.getRouteNo());
			ps1.setString(2, data.getCode());
			ps1.setString(3, data.getGroup());
			rs = ps1.executeQuery();
			
			while (rs.next()) {
				ref = rs.getString("ref_no");
			}
			
			if(data.getTripType().equalsIgnoreCase("Origin to Destination") && table.equalsIgnoreCase("s")) {
				sql1 = "update public.panel_generator_origin_trip_details set start_time = ? where ref_no = ? and bus_no = ? and permit_no = ? and end_time = ?";
				sql2 = "update public.nt_m_midpoint_o_to_d set mp_start_time = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_endtime = ?";
				sql6 = "update public.nt_m_combinepanal_temp set mp_start_time = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_endtime = ? and side = ? and userid = ?";
				
				ps2 = con.prepareStatement(sql1);
				ps3 = con.prepareStatement(sql2);
				ps4 = con.prepareStatement(sql6);
				
				Map.Entry<String, String> map = data.getStartTimes().entrySet().iterator().next();
				Map.Entry<String, String> mapEnd = data.getEndTimes().entrySet().iterator().next();
				
				try {
					ps2.setString(1, map.getValue());
					ps2.setString(2, ref);
					ps2.setString(3, data.getBusNo());
					ps2.setString(4, data.getPermitNo());
					ps2.setString(5, mapEnd.getValue());
			        
					ps2.executeUpdate();
			        con.commit();
			        
			        ps3.setString(1, map.getValue());
					ps3.setString(2, data.getRouteNo());
					ps3.setString(3, data.getCode());
					ps3.setString(4, data.getGroup());
					ps3.setString(5, mapEnd.getValue());
					
					ps3.executeUpdate();
			        con.commit();
			        
			        ps4.setString(1, map.getValue());
					ps4.setString(2, data.getRouteNo());
					ps4.setString(3, data.getCode());
					ps4.setString(4, data.getGroup());
					ps4.setString(5, mapEnd.getValue());
					ps4.setString(6, data.getTripType());
					ps4.setLong(7, deviceNo);
					
					ps4.executeUpdate();
			        con.commit();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}
				
			}
			else if(data.getTripType().equalsIgnoreCase("Destination to Origin") && table.equalsIgnoreCase("s")) {
				sql1 = "update public.panel_generator_destination_trip_details set start_time = ? where ref_no = ? and bus_no = ? and permit_no = ? and end_time = ?";
				sql2 = "update public.nt_m_midpoint_d_to_o set mp_start_time = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_endtime = ?";
				sql6 = "update public.nt_m_combinepanal_temp set mp_start_time = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_endtime = ? and side = ? and userid = ?";
				
				ps2 = con.prepareStatement(sql1);
				ps3 = con.prepareStatement(sql2);
				ps4 = con.prepareStatement(sql6);
				
				Map.Entry<String, String> map = data.getStartTimes().entrySet().iterator().next();
				Map.Entry<String, String> mapEnd = data.getEndTimes().entrySet().iterator().next();
				
				try {
					ps2.setString(1, map.getValue());
					ps2.setString(2, ref);
					ps2.setString(3, data.getBusNo());
					ps2.setString(4, data.getPermitNo());
					ps2.setString(5, mapEnd.getValue());
			        
					ps2.executeUpdate();
			        con.commit();
			        
			        ps3.setString(1, map.getValue());
					ps3.setString(2, data.getRouteNo());
					ps3.setString(3, data.getCode());
					ps3.setString(4, data.getGroup());
					ps3.setString(5, mapEnd.getValue());
					
					ps3.executeUpdate();
			        con.commit();
			        
			        ps4.setString(1, map.getValue());
					ps4.setString(2, data.getRouteNo());
					ps4.setString(3, data.getCode());
					ps4.setString(4, data.getGroup());
					ps4.setString(5, mapEnd.getValue());
					ps4.setString(6, data.getTripType());
					ps4.setLong(7, deviceNo);
					
					ps4.executeUpdate();
			        con.commit();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}
				
				
			}
			else if(data.getTripType().equalsIgnoreCase("Origin to Destination") && table.equalsIgnoreCase("m")) {
				sql3 = "update public.nt_m_midpoint_o_to_d set mp_midpoint_time = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_bus_no = ? and mp_permit_no = ? and (mp_start_time = ? or mp_endtime = ?)";
				sql6 = "update public.nt_m_combinepanal_temp set mp_midpoint_time = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_bus_no = ? and mp_permit_no = ? and (mp_start_time = ? or mp_endtime = ?) and side = ? and userid = ?";
				
				ps2 = con.prepareStatement(sql3);
				ps4 = con.prepareStatement(sql6);
				
				Map.Entry<String, String> map = data.getMidpoints().entrySet().iterator().next();
				Map.Entry<String, String> mapStart = data.getStartTimes().entrySet().iterator().next();
				Map.Entry<String, String> mapEnd = data.getEndTimes().entrySet().iterator().next();
				
				try {
					ps2.setString(1, map.getValue());
					ps2.setString(2, data.getRouteNo());
					ps2.setString(3, data.getCode());
					ps2.setString(4, data.getGroup());
					ps2.setString(5, data.getBusNo());
					ps2.setString(6, data.getPermitNo());
					ps2.setString(7, mapStart.getValue());
					ps2.setString(8, mapEnd.getValue());
					
					ps2.executeUpdate();
			        con.commit();
			        
			        ps4.setString(1, map.getValue());
					ps4.setString(2, data.getRouteNo());
					ps4.setString(3, data.getCode());
					ps4.setString(4, data.getGroup());
					ps4.setString(5, data.getBusNo());
					ps4.setString(6, data.getPermitNo());
					ps4.setString(7, mapStart.getValue());
					ps4.setString(8, mapEnd.getValue());
					ps4.setString(9, data.getTripType());
					ps4.setLong(10, deviceNo);
					
					ps4.executeUpdate();
			        con.commit();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}
	
			}
			else if(data.getTripType().equalsIgnoreCase("Destination to Origin") && table.equalsIgnoreCase("m")) {
				sql3 = "update public.nt_m_midpoint_d_to_o set mp_midpoint_time = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_bus_no = ? and mp_permit_no = ? and (mp_start_time = ? or mp_endtime = ?)";
				sql6 = "update public.nt_m_combinepanal_temp set mp_midpoint_time = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_bus_no = ? and mp_permit_no = ? and (mp_start_time = ? or mp_endtime = ?) and side = ? and userid = ?";
				
				ps2 = con.prepareStatement(sql3);
				ps4 = con.prepareStatement(sql6);
				
				Map.Entry<String, String> map = data.getMidpoints().entrySet().iterator().next();
				Map.Entry<String, String> mapStart = data.getStartTimes().entrySet().iterator().next();
				Map.Entry<String, String> mapEnd = data.getEndTimes().entrySet().iterator().next();
				
				try {
					ps2.setString(1, map.getValue());
					ps2.setString(2, data.getRouteNo());
					ps2.setString(3, data.getCode());
					ps2.setString(4, data.getGroup());
					ps2.setString(5, data.getBusNo());
					ps2.setString(6, data.getPermitNo());
					ps2.setString(7, mapStart.getValue());
					ps2.setString(8, mapEnd.getValue());
					
					ps2.executeUpdate();
			        con.commit();
			        
			        ps4.setString(1, map.getValue());
					ps4.setString(2, data.getRouteNo());
					ps4.setString(3, data.getCode());
					ps4.setString(4, data.getGroup());
					ps4.setString(5, data.getBusNo());
					ps4.setString(6, data.getPermitNo());
					ps4.setString(7, mapStart.getValue());
					ps4.setString(8, mapEnd.getValue());
					ps4.setString(9, data.getTripType());
					ps4.setLong(10, deviceNo);
					
					ps4.executeUpdate();
			        con.commit();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}
			}
			else if(data.getTripType().equalsIgnoreCase("Origin to Destination") && table.equalsIgnoreCase("e")) {
				sql4 = "update public.panel_generator_origin_trip_details set end_time = ? where ref_no = ? and bus_no = ? and permit_no = ? and start_time = ?";
				sql5 = "update public.nt_m_midpoint_o_to_d set mp_endtime = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_start_time = ?";
				sql6 = "update public.nt_m_combinepanal_temp set mp_endtime = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_start_time = ? and side = ? and userid = ?";
				
				ps2 = con.prepareStatement(sql4);
				ps3 = con.prepareStatement(sql5);
				ps4 = con.prepareStatement(sql6);
				
				Map.Entry<String, String> map = data.getEndTimes().entrySet().iterator().next();
				Map.Entry<String, String> mapStart = data.getStartTimes().entrySet().iterator().next();
				
				try {
					ps2.setString(1, map.getValue());
					ps2.setString(2, ref);
					ps2.setString(3, data.getBusNo());
					ps2.setString(4, data.getPermitNo());
					ps2.setString(5, mapStart.getValue());
			        
					ps2.executeUpdate();
			        con.commit();
			        
			        ps3.setString(1, map.getValue());
					ps3.setString(2, data.getRouteNo());
					ps3.setString(3, data.getCode());
					ps3.setString(4, data.getGroup());
					ps3.setString(5, mapStart.getValue());
					
					ps3.executeUpdate();
			        con.commit();
			        
			        ps4.setString(1, map.getValue());
					ps4.setString(2, data.getRouteNo());
					ps4.setString(3, data.getCode());
					ps4.setString(4, data.getGroup());
					ps4.setString(5, mapStart.getValue());
					ps4.setString(6, data.getTripType());
					ps4.setLong(7, deviceNo);
					
					ps4.executeUpdate();
			        con.commit();
				} catch (SQLException e) {
					con.rollback();
					e.printStackTrace();
				}

			}
			else if(data.getTripType().equalsIgnoreCase("Destination to Origin") && table.equalsIgnoreCase("e")) {
				sql4 = "update public.panel_generator_destination_trip_details set end_time = ? where ref_no = ? and bus_no = ? and permit_no = ? and start_time = ?";
				sql5 = "update public.nt_m_midpoint_d_to_o set mp_endtime = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_start_time = ?";
				sql6 = "update public.nt_m_combinepanal_temp set mp_endtime = ? where mp_route_no = ? and mp_service_type = ? and group_no  = ? and mp_start_time = ? and side = ? and userid = ?";
				
				ps2 = con.prepareStatement(sql4);
				ps3 = con.prepareStatement(sql5);
				ps4 = con.prepareStatement(sql6);
				
				Map.Entry<String, String> map = data.getEndTimes().entrySet().iterator().next();
				Map.Entry<String, String> mapStart = data.getStartTimes().entrySet().iterator().next();
				
				try {
					ps2.setString(1, map.getValue());
					ps2.setString(2, ref);
					ps2.setString(3, data.getBusNo());
					ps2.setString(4, data.getPermitNo());
					ps2.setString(5, mapStart.getValue());
			        
					ps2.executeUpdate();
			        con.commit();
			        
			        ps3.setString(1, map.getValue());
					ps3.setString(2, data.getRouteNo());
					ps3.setString(3, data.getCode());
					ps3.setString(4, data.getGroup());
					ps3.setString(5, mapStart.getValue());
					
					ps3.executeUpdate();
			        con.commit();
			        
			        ps4.setString(1, map.getValue());
					ps4.setString(2, data.getRouteNo());
					ps4.setString(3, data.getCode());
					ps4.setString(4, data.getGroup());
					ps4.setString(5, mapStart.getValue());
					ps4.setString(6, data.getTripType());
					ps4.setLong(7, deviceNo);
					
					ps4.executeUpdate();
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
	        ConnectionManager.close(ps6);
	        ConnectionManager.close(ps5);
	        ConnectionManager.close(ps4);
	        ConnectionManager.close(ps3);
	        ConnectionManager.close(ps2);
	        ConnectionManager.close(ps1);
	        ConnectionManager.close(con);
		}
	}
	
	@Override
	public List<String> getAllRunningNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> midPointList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select running_no from nt_m_combinepanal_temp ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				midPointList.add(rs.getString("running_no"));
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

		return midPointList;
	}
	
	@Override
	public String getAllRunningDetailsNo(String route, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String routeDetail = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT rc_strat_from, rc_end_at, rc_abbriviation_ltr_start, rc_abbriviation_ltr_end "
					+ "FROM public.nt_t_route_creator where rc_route_no = ? and rc_bus_type = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, route);
			ps.setString(2, serviceType);
			rs = ps.executeQuery();

			while (rs.next()) {
				routeDetail = route + " : " + rs.getString("rc_abbriviation_ltr_start") + "/ SLTB-O/ ETC-O - "
						+ rs.getString("rc_strat_from") + " , " + rs.getString("rc_abbriviation_ltr_end")
						+ "/ SLTB-D/ ETC-D - " + rs.getString("rc_end_at");
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

		return routeDetail;
	}

	@Override
	public List<RouteCreationDTO> getRouteInfo(List<RouteCreationDTO> selectedRouteList) {

		for (RouteCreationDTO routeCreationDTO : selectedRouteList) {
			
			String selectQuery = "select rc.rc_travel_time, r.rou_distance from nt_t_route_creator rc " + 
					"inner join nt_r_route r on rc.rc_route_no = r.rou_number " + 
					"where rc.rc_route_no=? and rc.rc_bus_type=?";

			try (Connection connection = ConnectionManager.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);) {

				preparedStatement.setString(1, routeCreationDTO.getRouteNo());
				preparedStatement.setString(2, routeCreationDTO.getServiceTypeCode());

				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					routeCreationDTO.setDistance(Double.valueOf(rs.getDouble("rou_distance")));
					routeCreationDTO.setTravelTimeStr(rs.getString("rc_travel_time"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return selectedRouteList;
	}
	
	
}