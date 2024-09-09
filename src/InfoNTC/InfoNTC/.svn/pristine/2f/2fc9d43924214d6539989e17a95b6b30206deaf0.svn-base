package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.RouteScheduleDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;

public interface BusesAssignedForAbbreviationService {

	public RouteScheduleDTO retrieveStartEndDateOfTimeTableDateRange(String generatedRefNo, String groupNo,
			String tripType);

	public List<RouteScheduleDTO> retrieveLeavePositionDetails(String generatedRefNo, String tripType);

	public void updateBusNoInTimetableGeneratorDet(List<RouteScheduleDTO> busNoList, String loginUser,
			String generatedRefNum, String groupNum, String tripType);

	public List<RouteScheduleDTO> getBusNoList(String routeNo, String refNo, String groupNo, String tripType);

	public String retrieveOriginDestinationSwap(String routeNo, String generatedRefNo);

	public List<RouteScheduleDTO> getBusNoListWithSelectedBuses(Object object, String generatedRefNo, String groupNo,
			String tripType, String routeNum, String busCategory);

	public RouteScheduleDTO retrieveStartEndDateOfLastPanelDateRange(String generatedRefNo, String groupNo,
			String routeNo, String trpType);

	public List<RouteScheduleDTO> getBusNoListWithoutCTBBuses(String routeNo, String refNo, String groupNo,
			String tripType, String busCategory);

	List<RouteScheduleDTO> getBusNoListWithSelectedBusesDestination(Object object, String generatedRefNo,
			String groupNo, String tripType, String routeNum, String busCategory);

	public List<RouteScheduleDTO> getAssignedBuses(Object object, String generatedRefNo, String groupNo,
			String tripType, String routeNum, String busCategory);

	public boolean updateBusNoInTimetableGeneratorDetNew(List<RouteScheduleDTO> modifyNoList, String loginUser,
			String generatedRefNum, String groupNum, String tripType);// new for bus abbreviation

	public List<String> getAllAssignedBusNoForNTCFixedBuses(String generatedRefNo, String groupNo);

	List<TimeTableDTO> getAllBusNoForFixedBusesWithoutAssignedNTCBuses(String busRoute, String serviceType,
			String originDestinationFlag, String generatedRefNo, String groupNo);

}
