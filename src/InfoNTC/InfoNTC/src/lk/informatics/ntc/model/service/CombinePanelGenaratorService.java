package lk.informatics.ntc.model.service;

import java.util.LinkedHashMap;
import java.util.List;

import lk.informatics.ntc.model.dto.BusDetailsDTO;
import lk.informatics.ntc.model.dto.CombinePanelGenaratorDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.RouteSetUpDTO;
import lk.informatics.ntc.model.dto.SetUpMidPointsDTO;

public interface CombinePanelGenaratorService {

	public List<CombinePanelGenaratorDTO> getRouteNoList();
	public CombinePanelGenaratorDTO  getOriginDestination(String routeNo);
	public List<CombinePanelGenaratorDTO> getDetails(String routeNo,String serviceType,String groupNo,String defineSide);
	public List<SetUpMidPointsDTO> getAllMidPointsOrgToDes(String routeNo, String serviceType, String groupNo);
	public List<SetUpMidPointsDTO> getAllMidPointsDesToOri(String routeNo, String serviceType, String groupNo);
	public SetUpMidPointsDTO getAllDetailsOrgToDes(String routeNo, String serviceType, String code);
	public BusDetailsDTO getBusDetails(String routeNo, String serviceType);
	public CombinePanelGenaratorDTO getBusData(String routeNo, String serviceType);
	List<String> getAllMidPointsTimeOrgToDes(List<SetUpMidPointsDTO> originDataList);
	public long getDeviceNo();
	boolean getAllTimes(String routeNo, String serviceType,String group,String side, long deviceNo);
	public void setMidPointData(List<SetUpMidPointsDTO> selectedDetailsList);
	void updateMidPointData(List<SetUpMidPointsDTO> selectedDetailsList);
	LinkedHashMap<String, LinkedHashMap<String, List<String>>> getAllTimesForMidPoint(String midPoint, String rangeStart, String rangeEnd, List<String> routes);
	List<SetUpMidPointsDTO> getAllTimeDataForTable(List<String> routes,long deviceNo);
	public void clearPreviousData(long deviceNo);
	void updateTables(SetUpMidPointsDTO data, String side, long deviceNo);
	List<String> getAllRunningNo();
	String getAllRunningDetailsNo(String route, String serviceType);
	
	public List<RouteCreationDTO> getRouteInfo(List<RouteCreationDTO> selectedRouteList);
	
}
