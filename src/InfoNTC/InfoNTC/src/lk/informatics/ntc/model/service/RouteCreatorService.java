package lk.informatics.ntc.model.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lk.informatics.ntc.model.dto.MidpointUIDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.RouteTypeDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;

public interface RouteCreatorService {
	public List<StationDetailsDTO> getAllStations();

	public List<RouteCreationDTO> getAllStationsForRouteCreaterEdit(String routeNo, String busType);// added by
																									// tharushi.e

	public List<RouteCreationDTO> getAllStationsForRouteCreaterEdit2(String routeNo, String busType);// added by
																										// tharushi.e

	public List<StationDetailsDTO> getAllStationsOnRoutes(String routeNo, String serviceType);// added by tharushi.e

	public List<RouteCreationDTO> getAllRoutes();

	/* Assign Station to routes */
	public List<RouteCreationDTO> getRouteDetails(RouteCreationDTO stationToRoute);

	public StationDetailsDTO getStationsInSinAndTamil(String string);

	public StationDetailsDTO getStaInSinAndTamilSec(String string);

	public List<RouteTypeDTO> getAllRouteTypes();

	public List<VehicleInspectionDTO> getAllServiceTypes();

	public boolean saveRouteDetails(RouteCreationDTO routeCreationDTO, List<MidpointUIDTO> midPoints,
			List<MidpointUIDTO> midPointsSwap, String user);

	public int generateStageValue(String routeNoEnterd);

	public List<StationDetailsDTO> getAllServiceType();

	public List<StationDetailsDTO> addAssignStationToRoute(StationDetailsDTO station, String routeNoEnterd,
			String loginUser, String string);

	public List<StationDetailsDTO> showDataonGrid(String routeNo);

	public List<RouteCreationDTO> getRouteDetailsFromRouteNo(String routeNo, String status);

	public List<RouteCreationDTO> getRouteDetailsFromRouteNoForEdit(String route, String status, String busType);

	public boolean updateRouteDetails(RouteCreationDTO routeCreationDTO, List<MidpointUIDTO> midPoints,
			List<MidpointUIDTO> midPointsSwap, String user);

	public boolean updateRouteDetailsForEdit(RouteCreationDTO routeCreationDTO, List<MidpointUIDTO> midPoints,
			List<MidpointUIDTO> midPointsSwap, String user);

	public LinkedList<MidpointUIDTO> getMidPointsFromRouteNo(String routeNo);

	public LinkedList<MidpointUIDTO> getMidPointsFromRouteNoAndBusType(String routeNo, String busType);// added by
																										// tharushi.e

	public LinkedList<MidpointUIDTO> getMidPointsReverseFromRouteNo(String routeNo);

	public LinkedList<MidpointUIDTO> getMidPointsReverseFromRouteNoAndBusType(String routeNo, String busType);// added
																												// by
																												// tharushi.e

	public List<RouteCreationDTO> getCreatedRouteData();

	public List<String> getStagesList(String selectedRoute);

	public List<RouteCreationDTO> getLocationList(String selectedRoute, String selectedStage, String selectedStatus);

	public void updatedEditedData(String selectedstationCode, String selectedStage, String loginUser,
			String selectedServiceType, String selectedStatus, String routeNo, String newStage,
			StationDetailsDTO stationToRouteSecondDTO);

	public void updateStageSeq(String RouteNo, String Stage, String Station, StationDetailsDTO station,
			String loginUser);

	public boolean checkDuplicateStation(String routeNo, String stationCode);

	public StationDetailsDTO retrieveRouteDataForRouteNum(String routeNo);

	public String getStationDescFromStationCode(String stationCode);

	public List<StationDetailsDTO> insertAssignStationToRoute(List<StationDetailsDTO> stationList, String loginUser,
			String routeNum);

	public boolean checkSelectedStageExist(String routeNo, String station, String newStage);

	public List<RouteCreationDTO> getRouteDetailsByRouteandType(String routeNo, String serviceType);
	Map<String ,String>getDataForCheckDuplicate(String routeNo);

}
