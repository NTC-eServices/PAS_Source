package lk.informatics.ntc.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lk.informatics.ntc.model.dto.CombinePanelGenaratorDTO;
import lk.informatics.ntc.model.dto.FareOfSemiLuxuryReportDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;

public interface ReportService2 {

	public List routeNoDropdown();

	public List serviceTypeDropDown();

	public StationDetailsDTO getstartOrigin(String routeNo, int stage);

	public StationDetailsDTO getExampleFee(String routeNo, int stage);

	public RouteDTO retrieveHighWayBusServiceFareReport(String serviceType, String route);

	/* for semi luxury Report */
	public List<Integer> getStagesAccordingToRouteNo(String routeNo);

	public List<Integer> getStagesAccordingToRouteNoLuxuary(String routeNo);

	public List<StationDetailsDTO> createDynamicTable(List<StationDetailsDTO> stagesList);

	public List<StationDetailsDTO> getsemiFeeAgainstStage(List<Integer> stagesList);

	public StationDetailsDTO getsemiFeeAgainstStageDTO(List<StationDetailsDTO> stagesList);

	public List<StationDetailsDTO> getsemiFeeAgainstStageLuxuary(List<Integer> stagesList);

	public List<StationDetailsDTO> getStagesAccordingToRouteNoDTO(String routeNo);

	public List<StationDetailsDTO> getStagesAccordingToRouteNoDTOLuxuary(String routeNo);

	public void insertDataIntoNt_temp_stages_amount(List<FareOfSemiLuxuryReportDTO> dtoList, List<Integer> stages,
			List<StationDetailsDTO> stagesListDTO);
    List<String>getStagesNameListFromTempTable(String routeNo);
	public void deleteData();

	// for check selected route number in which service type
	public StationDetailsDTO checkServiceTypeForRoute(String route, String serviceType);

	/**
	 * for Controll Sheet
	 */
	public String getOrginByRoute(String routeNo);

	public String getODestinationByRoute(String routeNo);

	public List<String> getRefferenceNo();

	public List<String> getGroupNo(String refNo);

	public Map<String, List> insertTimeSlotsInTemp(List<String> timeSlotList, Map<String, List> busNoList);

	public List<String> getTimeSlots(String tripType, String groupNo, String route, String serviceType, String refNo,boolean ownerSheet,String busAbbriviation);

	public String getServiceCode(String type);

	public Map<String, List> getBusNOList(String refNO, String trip, String group, List<String> timeSlotList);

	public Map<String, List> getBusNOList2(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne,int dayTwo,boolean isHistory);

	public Map<String, List> getBusNOList3(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayTwo,int dayThree,boolean isHistory);

	public void removeTempTable();

	public String getDistanceForRoute(String routeNo, String serviceType);

	public String getTravelTimeForROute(String routeNo, String serviceType);

	public String getSpeedForROute(String routeNo, String serviceType);

	public List<String> getRouteNo(String refNo);

	public List<String> getServiceType(String refNo);

	public void deleteEmptyTimeSLots(String refNo);

	public Map<String, List> insertLeavesInTable(String refNO, String trip, String groupNo,String originBus,String destinationBus,int dayForMonth,String busAbbriviation);

	public Map<String, List> insertLeavesInTable2(String refNO, String trip, String groupNo,String originBus,String destinationBus,int dayForMonth,int dayForMonth2);

	public Map<String, List> insertLeavesInTable3(String refNO, String trip, String groupNo,String originBus,String destinationBus,int dayForMonth,int dayForMonth2);

	/**
	 * end
	 */
	public List<String> serviceTypeDropDownList();

	public TimeTableDTO getTimetableDet(String routeNo, String busCatCode, String tripType, String groupNo);

	public List<String> getStartingTimeList(String genRefNo, String tripType, String groupNo);

	public List<String> getBusNos(String genRefNo, String tripType, String tripId, int noOfDays);

	public boolean checkLeave(String tripType, String groupNo, String route, String service, String refNo);

	public boolean checkDataInBusAbre(String tripType, String groupNo, String route, String service, String refNo);

	public boolean checkBusAssign(String refNo, String tripType, String groupNo);
	public Map<String, List> getBusNOListNew(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne,boolean isHistory);
	public String getAbriviatiosForRoute(String routeNo, String busCat ,String side);
	public List<String> busAbbreviationOrder(String s1, String s2 ,String refNo, String side,String groupNo);
	public List<String> busAbbreviationOrderWithLeave(String s1, String s2 ,String refNo, String side,String groupNo);
	public Map<String, List> getBusNOListNewWithLeave(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne ,boolean isHistory);
	public Map<String, List> getBusNOList2WithLeave(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne,int dayTwo,boolean isHistory);
	public Map<String, List> getBusNOList3WithLeave(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayTwo,int dayThree,boolean isHistory);
    
	public String getStartDateByRefNo(String refNo);
	public String getEndDateByRefNo(String refNo);
	public List serviceTypeDropDownWithOutNormal();
	boolean insertDatesRotations(List<String> list1,List<String> list2,List<String> list3,String month1,String month2,String month3);// will remove 
	public Map<String, List> getBusNOListNewWithLeaveForOwnerSheer(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne,List<String> endTImelist,boolean isHistory,String selectedBusAbbriviation);
	public Map<String, List> getBusNOListNewForOwnerSheet(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne,List<String> endTImelist,boolean isHistory,String selectedBusAbbriviation,boolean busesMoreThanThirty);
	public void truncTableInOwnersheetGenerate();
	public List<String> getEndTimeSlots(String tripType, String groupNo, String route, String serviceType, String refNo,String busAbbriviation);
	
	List<String>getStartMonthList(String refNo,String tripType);
	boolean getselectedDateRangeFromHistory(String refNo,String tripType,String dateRange);
	int getNumberOfBuses(String refNo,String tripType);
	boolean insertDatesRotationsNew(ArrayList<ArrayList<Integer>> arrayOne ,ArrayList<ArrayList<Integer>> arrayTwo ,ArrayList<ArrayList<Integer>> arrayThree,String month1,String month2,String month3);
	boolean insertDatesRotationsForBusesMoreThanThirty(ArrayList<String> arrayOne ,ArrayList<String> arrayTwo ,ArrayList<String> arrayThree,String month1,String month2,String month3,int totalBusCount);
	void removeColumns(int removeColumns);
	List<String>getDates(int busNos);
	int getNumberOfLeaves(String tripType, String groupNo, String route, String service, String refNo);
	List<String>getExpiredPermitNumber(String routeNumber);
//
//	public List<CombinePanelGenaratorDTO> getFixBus(String refNo, String tripType);

	List<String> busNoListWithLeave(String s1, String s2, String refNo, String side, String groupNo);
	
}
