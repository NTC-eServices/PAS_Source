package lk.informatics.ntc.model.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lk.informatics.ntc.model.dto.CombinePanelGenaratorDTO;
import lk.informatics.ntc.model.dto.FareOfSemiLuxuryReportDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;

public interface ReportService {

	public List routeNoDropdown();

	public List serviceTypeDropDown();

	public StationDetailsDTO getstartOrigin(String routeNo, int stage);

	public StationDetailsDTO getExampleFee(String routeNo, int stage);

	public RouteDTO retrieveFareforExpresswayAndSuperLuxuryReport(String serviceType, String route);
	

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

	public Map<String, List> getBusNOList2(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne,int dayTwo,boolean isHistory, List<String> fixTimeList);

	public Map<String, List> getBusNOList3(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayTwo,int dayThree,boolean isHistory, List<String> fixTimeList);

	public void removeTempTable(String refNo);

	public String getDistanceForRoute(String routeNo, String serviceType);

	public String getTravelTimeForROute(String routeNo, String serviceType);

	public String getSpeedForROute(String routeNo, String serviceType);

	public List<String> getRouteNo(String refNo);

	public List<String> getServiceType(String refNo);

	public void deleteEmptyTimeSLots(String refNo);

	public Map<String, List> insertLeavesInTable(String refNO, String trip, String groupNo,String originBus,String destinationBus,int dayForMonth,String busAbbriviation, int stopPoint, String reportRefNo);

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
//	public Map<String, List> getBusNOListNew(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne,boolean isHistory, List<String> timeSlotListFix, int startPoint);
	public Map<String, List> getBusNOListNew(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne ,boolean isHistory,List<String> fixTimeList,List<String> originBusList, List<String> destinationBusList, int startPoint, String reportRefNo);

	public String getAbriviatiosForRoute(String routeNo, String busCat ,String side);
	public List<String> busAbbreviationOrder(String s1, String s2 ,String refNo, String side,String groupNo);
	public List<String> busAbbreviationOrderWithLeave(String s1, String s2 ,String refNo, String side,String groupNo);
	public Map<String, List> getBusNOListNewWithLeave(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne ,boolean isHistory,List<String> fixTimeList,List<String> originBusList, List<String> destinationBusList, int startPoint, String reportRefNo);
	public Map<String, List> getBusNOList2WithLeave(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne,int dayTwo,boolean isHistory,List<String> fixTimeList,List<String> originBusList, List<String> destinationBusList);
	public Map<String, List> getBusNOList3WithLeave(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayTwo,int dayThree,boolean isHistory,List<String> fixTimeList,List<String> originBusList, List<String> destinationBusList);
    
	public String getStartDateByRefNo(String refNo);
	public String getEndDateByRefNo(String refNo);
	public List serviceTypeDropDownWithOutNormal();
	boolean insertDatesRotations(List<String> list1,List<String> list2,List<String> list3,String month1,String month2,String month3);// will remove 
	public Map<String, List> getBusNOListNewWithLeaveForOwnerSheer(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne,List<String> endTImelist,boolean isHistory,String selectedBusAbbriviation, int numberOfBuses, String reportRefNo);
	public Map<String, List> getBusNOListNewForOwnerSheet(String refNO, String trip, String group, List<String> timeSlotList,List<String> busOrderListForOrigin,List<String> busOrderListForDestination,String originBus,String destinationBus,int dayOne,List<String> endTImelist,boolean isHistory,String selectedBusAbbriviation,boolean busesMoreThanThirty, int numberOfBuses,String reportRefNo);
	public void truncTableInOwnersheetGenerate(String refNo);
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

	List<String> busAbbreviationOrderWithLeaveForOwner(String s1, String s2, String refNo, String side, String groupNo);

	public boolean insertDatesRotationsForBusesMoreThanThirtyNormal(ArrayList<ArrayList<Integer>> arrayOne,
			ArrayList<ArrayList<Integer>> arrayTwo, ArrayList<ArrayList<Integer>> arrayThree, String month1,
			String month2, String month3, int totalBusCount);

	List<String> getFixTimeSlots(String tripType, String groupNo, String route, String serviceType, String refNo,
			boolean ownerSheet, String busAbbriviation);

	public boolean insertDatesRotationsForBusesLessThanThirty(ArrayList<String> arrayOne, ArrayList<String> arrayTwo,
			ArrayList<String> arrayThree, String month1, String month2, String month3, int totalBusCount, String refNo,
			LinkedHashMap<String, List<String>> fullMonths);

	List<String> getOriginBusListWithLeave(String refNo, String side);
	boolean insertDatesForControlSheet(List<String> dateList,String refNo, String reportRfNo);
	public List<String[]> getMonthListWithRidingDates();
	///

	boolean getTwoDayRotation(String refNo, String tripType);

	Map<String, List> getBusNOListNewForTwoDay(String refNO, String trip, String group, List<String> timeSlotList,
			List<String> busOrderListForOrigin, List<String> busOrderListForDestination, String originBus,
			String destinationBus, int dayOne, boolean isHistory, List<String> fixTimeList, int startPoint, String reportRefNo);

	Map<String, List> insertLeavesInTableForTwoDay(String refNO, String trip, String groupNo, String originBus,
			String destinationBus, int dayForMonth, String selectedBusAbbriviation, int startPoint, String reportRefNo);

	Map<String, List> getBusNOListNewWithLeaveTwoDay(String refNO, String trip, String group, List<String> timeSlotList,
			List<String> busOrderListForOrigin, List<String> busOrderListForDestination, String originBus,
			String destinationBus, int dayOne, boolean isHistory, List<String> fixTimeList, List<String> originBusList,
			List<String> destinationBusList, int startPoint, String reportRefNo);

	boolean checkLeaveTwoDay(String tripType, String groupNo, String route, String service, String refNo);

	Map<String, List> getBusNOListNewWithLeaveForOwnerSheerTwoDay(String refNO, String trip, String group,
			List<String> timeSlotList, List<String> busOrderListForOrigin, List<String> busOrderListForDestination,
			String originBus, String destinationBus, int dayOne, List<String> endTImelist, boolean isHistory,
			String selectedBusAbbriviation, int numberOfBuses,String reportRefNo);

	Map<String, List> getBusNOListNewForOwnerSheetTwoDay(String refNO, String trip, String group,
			List<String> timeSlotList, List<String> busOrderListForOrigin, List<String> busOrderListForDestination,
			String originBus, String destinationBus, int dayOne, List<String> endTImelist, boolean isHistory,
			String selectedBusAbbriviation, boolean busesMoreThanThirty, int numberOfBuses, String reportRefNo);

	List<String> getTimeSlotsTwoDay(String tripType, String groupNo, String route, String serviceType, String refNo,
			boolean ownerSheet, String busAbbriviation);

	Map<String, List> insertTerminalData(String refNO, String trip, String service, List<String> timeSlotList,
			List<String> busOrderListForOrigin, List<String> busOrderListForDestination, String terminalCode,
			String route, int dayOne, boolean isHistory, List<String> fixTimeList, List<String> originBusList,
			List<String> destinationBusList, int startPoint, List<Integer> dayList);

	Map<String, List> insertTerminalDataTwoDay(String refNO, String trip, String service, List<String> timeSlotList,
			List<String> busOrderListForOrigin, List<String> busOrderListForDestination, String terminalCode,
			String route, int dayOne, boolean isHistory, List<String> fixTimeList, List<String> originBusList,
			List<String> destinationBusList, int startPoint, List<Integer> dayList);

		public boolean alreadySaved(String refNo);

		Map<String, List> insertTerminalDataWithoutLeave(String refNO, String trip, String service,
				List<String> timeSlotList, List<String> busOrderListForOrigin, List<String> busOrderListForDestination,
				String terminalCode, String route, int dayOne, boolean isHistory, List<String> fixTimeList,
				List<String> originBusList, List<String> destinationBusList, int startPoint, List<Integer> dayList);

		Map<String, List> insertTerminalDataTwoDayWithoutLeave(String refNO, String trip, String service,
				List<String> timeSlotList, List<String> busOrderListForOrigin, List<String> busOrderListForDestination,
				String terminalCode, String route, int dayOne, boolean isHistory, List<String> fixTimeList,
				List<String> originBusList, List<String> destinationBusList, int startPoint, List<Integer> dayList);

	
	
}
