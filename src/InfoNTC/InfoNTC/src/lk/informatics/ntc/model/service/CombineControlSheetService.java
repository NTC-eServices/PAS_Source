package lk.informatics.ntc.model.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CombineControlSheetDetailsDTO;
import lk.informatics.ntc.model.dto.CombineControlSheetMasterDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.RouteScheduleDetailsDTO;
import lk.informatics.ntc.model.dto.RouteScheduleMasterDTO;
import lk.informatics.ntc.model.dto.RouteScheduleMidPointDTO;
import lk.informatics.ntc.model.dto.ServiceTypeDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;

public interface CombineControlSheetService {

	public List<RouteCreationDTO> getAllActiveRoutes();

	public List<ServiceTypeDTO> getAllServiceStypes();

	public List<StationDetailsDTO> getStationsByRouteAndServiceType(String routeNo, String serviceType);

	public RouteScheduleMasterDTO getRouteScheduleInfo(String routeNo, String serviceType, String groupNo, String side, LocalDate startDate,
			LocalDate endDate);
	
	public  List<RouteScheduleDetailsDTO> getRouteScheduleTripDetailsByRef(String ref);
	
	public  List<RouteScheduleDetailsDTO> getRouteScheduleLeaveTripDetailsByRef(String ref);
	
	public  List<RouteScheduleMidPointDTO> getRouteMidPointsForNoneSLTB(String routeNo, String serviceType, String groupNo, String side, StationDetailsDTO selectedStation);
	
	public  List<RouteScheduleMidPointDTO> getRouteMidPointsForSLTB(String routeNo, String serviceType, String groupNo, String side, StationDetailsDTO selectedStation);
	


}
