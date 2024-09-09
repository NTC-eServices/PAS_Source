package lk.informatics.ntc.model.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.RouteScheduleDTO;

public interface RouteScheduleService {

	public List<RouteScheduleDTO> getRouteNo();

	public RouteScheduleDTO getRouteDetails(String routeNo, String busCategory);

	public List<RouteScheduleDTO> getGroupNoList(String routeNo, String generatedRefNo);

	public List<RouteScheduleDTO> getBusCategoryList(String routeNo);

	public RouteScheduleDTO getNoOfBusesAndTripsForRoute(String routeNo, String refNo, String groupNo, String tripType);

	public int getFixedOrWithoutFixedTripsCount(String refNo, String groupNo, String tripType, String fixType);

	public int getFixedOrWithoutFixedTripsCountNew(String refNo, String groupNo, String tripType, String fixType,
			String routeNo, String busCat);// new

	public int getFixedOrWithoutFixedTripsCountNewWithCoupleTwo(String refNo, String groupNo, String tripType,
			String fixType, String routeNo, String busCat);// new for couple 2 scenario

	public List<RouteScheduleDTO> getBusNoList(String routeNo, String refNo, String groupNo, String tripType);

	/* Get bus data related to route */

	public int getTotalBusesForRoute(String routeNo, String refNo);

	public int getTotalTripsForRoute(String refNo, String tripType, String groupNo);

	/* Get bus data related to group and type */

	public int getTotalWithoutFixedBusForGroupAndType(String routeNo, String ref, String tripType, String group);

	public int getTotalLavesForGroupAndType(String ref, String tripType, String group);

	/* Get bus data related to group */

	public int getTotalBusForGroup(String routeNo, String refNo, String groupNo);

	public int getTotalTripsForGroup(String routeNo, String refNo, String groupNo);

	public boolean insertRouteScheduleMasterTableData(RouteScheduleDTO dto, String rotationType, String trip_type,
			String groupNo, String generatedRef, int daysDiff, String user, String routeRefNo);

	public boolean insertRouteScheduleDetOneTableData(List<RouteScheduleDTO> busNoList, String generatedRef, String user, String routeRefNo, boolean swap, int days);

	public boolean insertRouteScheduleDetTwoTableData(RouteScheduleDTO dto, List<String> leavePostionList,
			String generatedRef, String user, String routeRefNo, String tripType);

	public String generateReferenceNo();

	public RouteScheduleDTO getLastRouteScheduleData(String routeNo, String busCategory, String generatedRefNo,
			String group, String tripType);

	public boolean isRelatedDataFound(String routeNo, String busCategory, String generatedRefNo, String group,
			String tripType);

	public int getNoOfDays(String routeNo, String busCategory, String generatedRefNo, String group, String tripType);

	public List<String> selectEditDate(String routeNo, String busCategory, String generatedRefNo, String group,
			String tripType);

	public void updateEditedBusNumbersInRoute_schedule_generator_det01(String originalBusNum,
			String editedBusNum, String seqNum, String loginUser, String tripType);

	public List<RouteScheduleDTO> getBusNoListWithoutCTBBus(String routeNo, String refNo, String groupNo,
			String tripType, String busCategory);

	public List<RouteScheduleDTO> getBusNoListWithoutCTBBusWithCoupleTwo(String routeNo, String refNo, String groupNo,
			String tripType, String busCategory);// for couple 2 scenarios

	public boolean checkBusAssignerDone(String busCategory, String generatedRefNo, String groupNo, String tripType);

	public boolean checkBusAssignerDoneLetEdit(String busCategory, String generatedRefNo, String groupNo,
			String tripType);

	public List<String> selectEditDateForAssignedBuses(String routeNo, String busCategory, String generatedRefNo,
			String group, String tripType);

	public List<RouteScheduleDTO> retrieveInsertedDataForEdit(String generatedRefNo, String triptype);

	public List<RouteScheduleDTO> retrieveInsertedDataForEditTimeTable(String generatedRefNo, String busCategory,
			String groupNo, String tripType, String route);

	public void insertRouteGeneratorDetDataForHistory(String generatedRefNo, String string,
			String originalBusNum, String editedBusNum, String seqNum, String loginUser, String tripId);

	public String retrieveLastBusNumOfRouteSchedule(String routeNo, String generatedRefNo, String groupNo,
			String tripType, String busCategory);

	public List<RouteScheduleDTO> getBusNoListSecondTime(String routeNo, String refNo, String groupNo, String tripType,
			String busCategory);

	public String retrieveReferenceNo(String routeNo, String busCategory, String generatedRefNo, String group,
			String tripType);

	public void updateRouteScheduleGeneratorDates(Date lastPanelEndDate, String routeNo, String busCategory,
			String generatedRefNo, String group, String tripType, int dateDifference,Date lastPanelStartDateNew);

	public List<String> getDateRangesForRouteSchedule(String routeNo, String busCategory, String generatedRefNo,
			String groupNo, String tripType);

	public List<String> selectEditDataForRouteSchedule(String routeNo, String busCategory, String generatedRefNo,
			String groupNo, String tripType, int dateCount);

	public List<String> selectEditDataForRouteScheduleNew(String routeNo, String busCategory, String generatedRefNo,
			String groupNo, String tripType, int dateCount);

	public List<RouteScheduleDTO> selectEditDataForRouteScheduleDTO(String routeNo, String busCategory,
			String generatedRefNo, String groupNo, String tripType, int dateCount);

	public List<RouteScheduleDTO> getBusNoListForRotation(String routeNo, String refNo, String groupNo, String tripType,
			String busCategory);

	public List<RouteScheduleDTO> getBusNoListForRotationWithCoupleTwo(String routeNo, String refNo, String groupNo,
			String tripType, String busCategory); // for couple 2 scenarios

	public int getNoOfTripsPerSide(String routeNo, String refNo, String groupNo, String tripType, String busCat);

	public int getNoOfTripsPerSideWithSltb(String routeNo, String refNo, String groupNo, String tripType,
			String busCat);

	public int getNoOfTripsPerSideForEdit(String routeNo, String refNo, String groupNo, String tripType, String busCat);

	public boolean updateEditedBusNumbersAcordinTrip(String refNo, String tripType, String dayOne, String tripOne,
			String busOne);

	public String getCoupleForRef(String refNo, String tripType, String groupNo);// new

	public boolean checkLeaveInTimeTableDet(String refNo, String groupNo, String tripType);
	
	public List<RouteScheduleDTO> getBusNoListForRotationWithCoupleTwoNew(String routeNo, String refNo, String groupNo,
			String tripType, String busCategory); // for couple 2 scenarios for second panal
	
	public List<String> getLeaveListPerSide (String refNo, String tripType);
	
	public int getMasterSeq(String routeNo, String busCategory,
			String generatedRefNo, String group, String tripType);

	public boolean updateRouteScheduleDetOneTableData(List<RouteScheduleDTO> leaveForRouteList, String loginUser, String referenceNo, String generatedRef, String tripType, int days);

	RouteScheduleDTO getRouteDetailsGroup(String routeNo, String busCategory, String group);

	boolean insertRouteScheduleDetOneTableDataTwoDay(List<RouteScheduleDTO> busNoList, String generatedRef, String user, String routeRefNo, boolean swap, int days);

	boolean insertRouteScheduleDetTwoTableDataTwoDay(RouteScheduleDTO dto, List<String> leavePostionList,
			String generatedRef, String user, String routeRefNo, String tripType);

	boolean updateRouteScheduleDetOneTableDataTwoDay(List<RouteScheduleDTO> leaveForRouteList, String loginUser, String referenceNo, String generatedRef, String tripType, int days);

	int getNoOfTrips(String refNo, String tripType, String table);

	List<String> getBusNoForEdit(String generatedRefNo, String tripId, String tripType, String table);

	List<String> getNoOfTripsLeave(String refNo, String tripType, String table);

	List<String> getBusNoForEditLeave(String generatedRefNo, String tripId, String tripType,String table);

	int getNoOfTripsForTwoDay(String refNo, String tripType);

	List<String> getBusNoForEditTwoDay(String generatedRefNo, String tripId, String tripType);

	void updateEditedBusNumbersInRoute_schedule_generator_det01ForEdit(String refNo, String tripType, String userName,
			List<RouteScheduleDTO> routeList,int loop,String table);

	void updateEditedBusNumbersInRoute_schedule_generator_det01ForEditLeaves(String refNo, String tripType,
			String userName, List<RouteScheduleDTO> routeList,String table, int days);
	

	// To add time table data to terminal use : added by dhananjika.d (30/04/2024)
	
	void insertTerminalData(List<RouteScheduleDTO> busNoList, List<RouteScheduleDTO> twoDayBusNoList, List<String> startTime,String route, String service, String terminal)
			throws SQLException;

	String getTerminalCode(String route, String service);

	List<String> getTimes(String refNo, String trip);

	List<String> getEndTimes(String refNo, String trip);
}
