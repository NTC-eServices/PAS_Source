package lk.informatics.ntc.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import lk.informatics.ntc.model.dto.ChakreeyaPalaliDTO;
import lk.informatics.ntc.model.dto.ChakreeyaPalaliDestiDTO;
import lk.informatics.ntc.model.dto.RouteScheduleHelperDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.view.beans.OwnerSheetListMonthWrapper;

public interface TimeTableService {

	public List<TimeTableDTO> getRouteNoList();

	public TimeTableDTO getBusCategory(String routeNO);

	public int getGroupCount(String routeNO, String refNo);

	public List<TimeTableDTO> getNewRouteDetails(String routeNO, String refNo);

	public int getPVTbusCount(String routeNo, boolean type, String serviceType, String group);

	public String getRestTime(String routeNO);

	public boolean insertTripsGenerateMasterData(TimeTableDTO timeTableDTO, String value, String user);

	public void insertTripsGenerateDetailsOneData(List<TimeTableDTO> list, String value, String user, String groupNo,
			String tripType);

	// for panel generator with fixedtime
	public List<TimeTableDTO> showTimeSlotDetForGroups(String tripRefNo, String groupNo, String tripType);

	public void insertTripsGenerateDetailsTwoData(List<TimeTableDTO> list, String value, String user);

	public String generateReferenceNo();

	/* This method changed by GAYATHRA */
	public TimeTableDTO getRouteData(String routeNO, String busCategory);

	public TimeTableDTO getRouteData(String routeNO);

	public String getPanelGenNo(String routeNo, String category);

	public TimeTableDTO getTravleTimeForRouteBusCate(String RouteNo, String busCat);

	public boolean isRecordFound(TimeTableDTO timeTableDTO);

	public List<TimeTableDTO> generateBusType(String tripRefNo, String groupNo, String tripType);

	public TimeTableDTO getReffrenceNo(String routeNo, String busCat);

	public List<TimeTableDTO> getRouteNoListForFixedBuses();

	public List<TimeTableDTO> getDetailsOfFixedBuses(TimeTableDTO dto, String groupNo, String type);

	public List<TimeTableDTO> getAllBusNoForFixedBuses();

	public List<TimeTableDTO> getTimeTableGeneratorDetDataForHistorySave(String generatedRefNo);

	public boolean checkRelatedDataInTripGeneratorTable(String routeNo, String generatedRefNo);

	public List<TimeTableDTO> getSavedTripsTimeDetails(String routeNO, String refNo, String type, String groupNo);

	public List<TimeTableDTO> getRouteNoListFromTripsGen();

	public ArrayList<String> isGroup(String refNo, String tripSide);

//	public boolean insertTimeSlots(List<TimeTableDTO> timeStartListOrigin, List<TimeTableDTO> timeStartListDes, String refNo, String groupNo,
//			 String user);

//	public boolean saveNoOfTrips(int noOFTripsValGOne, String refNo, String groupNo, String loginUSer,
//			String TimeTableRefNoOrigin, String TimeTableRefNoDes);

	public int getGroupCountForEdit(String routeNO, String refNo);

	public TimeTableDTO getSavedTripsRouteAndBusDetails(String routeNO, String refNo, String type, String groupNo);

	public List<TimeTableDTO> getTripsTableDetOneDataForHistorySave(String routeNo, String generatedRefNo);

	public List<TimeTableDTO> getTripsTableDetTwoDataForHistorySave(String routeNo, String generatedRefNo);

	public boolean insertTripsGenerateHistoryDetailsOneData(List<TimeTableDTO> list, String user);

	public boolean insertTripsGenerateHistoryDetailsTwoData(List<TimeTableDTO> list, String user);

	public String getTripReferenceNo(String generateRefNo);

	public void deleteTripsGenerateDetailsOneData(String generateRefNo);

	public void deleteTripsGenerateDetailsTwoData(String generateRefNo);

	public TimeTableDTO getPanelStageStatus(String generateRefNo, String routeNo);

	public boolean checkProcessPath(String generateRefNo, String routeNo);

	public boolean insertTimeTableGeneratorDetDataForHistorySave(List<TimeTableDTO> list, String user);

	public boolean updateTimeTableGeneratorDetData(List<TimeTableDTO> originList, List<TimeTableDTO> desList,
			String user, String groupNo, String generatedRefNo);

	public TimeTableDTO getAbriviatiosForRoute(String routeNo, String busCat);

	public List<TimeTableDTO> getBusCategoryList(String routeNo);

	String generateTimeTableRefNo(String group, String tripNo);

	public boolean dataAvailableForShow(String panelRefNo);

	public List<TimeTableDTO> getInsertedData(String groupNo, String tripType, String panelRefNo);

//	public ArrayList<TimeTableDTO> updateTimeSlots(List<TimeTableDTO> timeStartList, String refNo, String tripType,
//			String groupNo, String user);

	public int getNoOfTripsavail(String pRefNo, String groupNo, String tripType);

//	public void updateNoOFtrips(String panelRef, String groupNo, int tripsOrigin, int tripsDes);

	public boolean availableForEdit(String panelRef);

	public TimeTableDTO getNoOfNonPrivateBusses(String tripRef, String group, String tripType);

	public TimeTableDTO getLastEndTimeOfTimeRange(String tripRefNo, String string, String string2);

	public List<TimeTableDTO> getAllBusNoForFixedBuses(String busRoute, String serviceType,
			String originDestinationFlag);

	public String getAssignedBusnumber(String busNo, String generatedRefNo, String groupNum, String tripType);

	public boolean saveNightShiftBuses(LinkedList<TimeTableDTO> midnightShiftBusesOrigin, String routeNo,
			String genereatedRefNo, String string, String string2, String loginUser);

	public void deleteNightShiftBuses(TimeTableDTO deleteDTO, String routeNo, String genereatedRefNo, String string,
			String string2, String loginUser);

	public LinkedList<TimeTableDTO> getNightShiftBuses(String routeNo, String genereatedRefNo, String string,
			String string2);

	public List<VehicleInspectionDTO> getPVTbuses(String routeNo, String type);

	public List<TimeTableDTO> getBusNoByRouteAndCatForFixedBuses(String routeNo, String categoryCode, String order);

	public List<TimeTableDTO> getTimeSlotDetForGroups(String ttRefNo, String groupNo, String tripType);

	public TimeTableDTO getNoOfSltbEtcBusses(String tripRef, String group);

	public boolean checkFixedBuses(String refNo, String tripType);

	public List<ChakreeyaPalaliDTO> getDataForReport(String refNo, String tripSide);

	public List<ChakreeyaPalaliDestiDTO> getDataForReportDestination(String refNo, String tripSide);

	public String getBusCategoryDescription(String busCategoryCode);

	public void fillRemainingBusNumbers(String groupNo, String genereatedRefNo);

	public boolean savePanelGeneratorWithFixedTimeAllInOne(int noOFTripsValGOne, String panelGenNo, String string,
			String loginUser, String timeTableRefNoOrigin, String timeTableRefNoDes,
			List<TimeTableDTO> timeStartListOrigin, List<TimeTableDTO> timeStartListDes);

	public boolean updatePanelGeneratorWithFixedTimeAllInOne(String panelGenNo, String groupNo, String loginUser,
			List<TimeTableDTO> timeStartListOrigin, List<TimeTableDTO> timeStartListDes);

	public void saveOriginTimeSlotTable(List<TimeTableDTO> originRouteList,String draft);

	public void saveDestinationTimeSlotTable(List<TimeTableDTO> destinationRouteList, String draft);

	public List<TimeTableDTO> getStartEndTimeForPanelGenerator(String routeNO, String refNo);

//	public String getRealtedAbbreviation(String refNo, String routeNo);
	public List<String> getPermitNoList(String routeNo,boolean side,String group,String serviceType);

	public List<String> getBusNoList(String routeNo,boolean side,String group,String serviceType);

	public String updateBusNo(String permitNo, String routeNo);

	public String updatePermitNo(String busNo, String routeNo);

	public void saveGeneralDetailsPanelGenerator(TimeTableDTO panelGeneratorFormData, String draft);

	public String getTravelTime(String routeNo);

	public List<String> getReferenceNumbers();

	public TimeTableDTO getRouteDataForPanelGenerator(String refNo);

	public void saveLeaveBusesTimeSlotTable(List<TimeTableDTO> leaveBusList,List<TimeTableDTO> leaveBusListDes,String draft);

	public boolean validateRefNo(String refNo);
	
	 public boolean validateRefNoForSave(String refNo);

	public TimeTableDTO getDetailsForReport(String routeNo,String busCategory);

	public List<String> getReferenceNumbersForEdit();

	public TimeTableDTO getRouteDataForEditPanelGenerator(String refNo);

	public TimeTableDTO getDtoWithGeneralData(String routeNo,String serviceType);

	public List<TimeTableDTO> getOriginDtoListForEdit(String refNo,String routeno, String service);

	public List<TimeTableDTO> getDestinationDtoListForEdit(String refNo,String routeno, String service);

	public List<TimeTableDTO> getLeaveBusesDtoListForEdit(String refNo);

	public void saveGeneralDetailsEditPanelGenerator(TimeTableDTO panelGeneratorFormData, String user,
			boolean isForSubmit, String refNo,String draft);

	public void editOriginTimeSlotTable(TimeTableDTO panelGeneratorFormData, List<TimeTableDTO> originRouteList,
			String user, boolean isForSubmit, String refNo,String draft);
	public void editDestinationTimeSlotTable(TimeTableDTO panelGeneratorFormData, List<TimeTableDTO> destinationRouteList,
			String user, boolean isForSubmit, String refNo,String draft);
	public void editLeaveBusesTimeSlotTable(TimeTableDTO panelGeneratorFormData, List<TimeTableDTO> leaveBusList,List<TimeTableDTO> leaveBusListDes,
			String user, boolean isForSubmit, String refNo,String draft);

	List<TimeTableDTO> getLeaveBusesDtoListForEditDes(String refNo);

	void searchGeneralDetailsPanelGenerator(List<TimeTableDTO> list, String refNo);

	void updateGeneralDetailsPanelGenerator(List<TimeTableDTO> list,String refNo);

	List<TimeTableDTO> getDestinationDtoListForSchedule(String refNo);

	List<TimeTableDTO> getOriginDtoListForSchedule(String refNo);

	List<TimeTableDTO> getDetailsForControlSheetReport(String refNo, String tripDirection);

	List<TimeTableDTO> getOriginDtoLeaveListForSchedule(String refNo);

	int getCountForCoupleTwo(String refNo);

	boolean checkDataHave(String refNo);

	HashMap<List<TimeTableDTO>, List<TimeTableDTO>> getPreviousData(String refNo,String tripType, String group);

	List<String> getLeavePositionList(String refNo,String tripType);

	List<TimeTableDTO> getOriginDtoListForScheduleWithoutFix(String refNo);

	List<TimeTableDTO> getDestinationDtoListForScheduleWithoutFix(String refNo);

	List<String> getOriginDtoListForScheduleFix(String refNo);

	List<String> getDestinationDtoListForScheduleFix(String refNo);

	List<String> getDaysOfGroup(String refNo);

	Date getStartDaysOfGroup(String refNo,String tripSide);

	//public void insertRidingDaysForOwnershipReport(String[] columnData, String refNo);
	public void insertRidingDaysForOwnershipReport(OwnerSheetListMonthWrapper wrapper, String refNo);

	int getDayOneBusCount(String refNo, String side);

	boolean getTwoDayRotation(String refNo, String tripType, String dateRange);

	LinkedHashMap<LinkedHashSet<String>, RouteScheduleHelperDTO> getBusFixAndNotFixed(String refNo, String abbreviation, String side);

	RouteScheduleHelperDTO getLeaveBusesList(String refNo, String abbreviation, String side);





}
