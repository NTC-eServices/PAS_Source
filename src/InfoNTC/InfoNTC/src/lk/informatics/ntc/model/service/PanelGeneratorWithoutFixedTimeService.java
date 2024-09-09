package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import lk.informatics.ntc.model.dto.TimeTableDTO;

public interface PanelGeneratorWithoutFixedTimeService extends Serializable {

	public String retrieveDaysForGroup(String routeNo, String RefNo, String groupNo);

	public List<String> retrieveStartEndTimes(String routeNo, String refNum, String groupNo);

	public String retrieveLastPrivateBusNumber(String genereatedRefNo, String groupNo, String type,
			String abbreviation);

	public String retrieveDriverRestTime(String routeNo);

	public void addDataIntoTimetableGenerator(List<TimeTableDTO> originList, List<TimeTableDTO> desList,
			String routeNum, String groupNo, String referenceNum, String loginUser, boolean coupling);

	public void updateIntoTimetableGenerator(Connection conn, TimeTableDTO dTO, String groupNo, String tripType,
			String referenceNum, Long masterSeq, String loginUser, String duplicateBusNum);

	public String getBusRideTime(String routeNO, String busType);

	public boolean checkDataAlreadySaved(String genereatedRefNo, String groupNum, String tripType);

	public String retrieveCouplesForRoute(String genereatedRefNo, String groupNo);

	public int retrievePrivateBusNumbers(String genereatedRefNo, String groupNum, String tripType);

	public List<String> retreivePrivateBusNumbersInFixedBuses(TimeTableDTO timeTableDTO, String groupNum,
			String tripType, String abbreviation);

	public int retrieveFixedBusNumbers(String genereatedRefNo, String groupNum, String tripType, String pvtBus);

	public List<TimeTableDTO> selectDataForGroups(String genereatedRefNo, String groupNum, String TripType);

	public int getNumOfLeaves(String genereatedRefNo, String groupNum, String TripType);

	public boolean updateLeaves(String genereatedRefNo, String groupNum, int originLeaves, int desLeaves,
			String loginUser);

	public int retrieveNumOfLeaves(String genereatedRefNo, String groupNum, String tripType);

	public void updateTimetableGenerator(Connection conn, TimeTableDTO dTO, String groupNo, String tripType,
			String referenceNum, Long masterSeq, String loginUser, String duplicateBusNum);

	public void updateTimetableGeneratorEdit(Connection conn, TimeTableDTO dTO, String groupNo, String tripType,
			String referenceNum, Long masterSeq, String loginUser, String duplicateBusNum, int tripId); // new

	public boolean checkLeavesOnPanelRefNo(String refNo, String groupNo);

	public void updateCouplesInMasterTable(String genereatedRefNo, String groupNum, String coupleNoOrigin,
			String coupleNoDes);

	public void updateTimetableGeneratorNew(TimeTableDTO timeTableDTO, String groupNo, String tripType,
			String referenceNum, String loginUser, String duplicateBusNum); // new for connection close

	public void updateTimetableGeneratorEditNew(String groupNo, String referenceNum, String loginUser,
			List<TimeTableDTO> originBusList, List<TimeTableDTO> desBusList) throws Exception;// to save both in one connection

	public void updateTimetableGeneratorNew(String groupNo, String referenceNum, String loginUser,
			List<TimeTableDTO> originBusList, List<TimeTableDTO> desBusList); // to save both in one connection
}
