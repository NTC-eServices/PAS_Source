package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.BusFareDTO;
import lk.informatics.ntc.model.dto.MidPointTimesDTO;
import lk.informatics.ntc.model.dto.MidpointUIDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;

public interface CombineTimeTableGenerateService extends Serializable {

	public List<RouteDTO> getRouteNoList();

	public TimeTableDTO getRouteData(String routeNO);

	public List<TimeTableDTO> retireveStartEndTimes(String string, String selectedRouteNo, String selectedGroup);

	public List<MidpointUIDTO> retrieveMidPointTimeTakenForRoute(String selectedRouteNo, String busType);

	public List<String> retrieveMidPointNamesForRoute(String selectedRouteNo, String busType2);

	public String retrieveBusTypeDesc(String busType);

	public List<MidpointUIDTO> retrieveMidPointTimeTakenForRouteDtoO(String selectedRouteNo, String busType);

	public List<String> retrieveMidPointNamesForRouteDtoO(String selectedRouteNo, String string);

	public List<RouteDTO> retrieveSimilarRoutes(String selectedRouteNo);

	public void insertMidpointDataToNt_t_midpoint_timetable(List<MidPointTimesDTO> midPointsExpress, String routeNo,
			String category, String tripType, String group, String loginUser);

	public boolean checkDataAvailableInNt_t_midpoint_timetable(String selectedRouteNo, String string, String string2,
			String selectedGroup);

	public List<MidPointTimesDTO> selectTimeTableData(String selectedRouteNo, String string, String string2,
			String selectedGroup);

	public boolean updateNt_t_midpoint_timeTable(String routeOnEdit, MidPointTimesDTO editMidpointDTO, String category,
			String tripType, String selectedGroup, String loginUser);

	public List<BusFareDTO> retrieveCategoryList();

	public List<RouteDTO> retrieveViaList(String routeNum);

	public List<MidpointUIDTO> retrieveMidPointNameListForRoute(String routeNo, String busType);

	public List<MidpointUIDTO> retrieveMidPointNameListForRouteDtoO(String routeNo, String busType);

	public List<String> retrieveDataOfMidPoint(String midPointCode, String tripType, String group, String roouteNum,
			String busCategory);
}
