package lk.informatics.ntc.model.service;

import java.util.HashMap;
import java.util.List;

import lk.informatics.ntc.model.dto.RouteSetUpDTO;
import lk.informatics.ntc.model.dto.TerminalTimeTableDTO;

public interface RouteSetUpForTimeTablePurposeService {

	public List<RouteSetUpDTO> searchRouteDetails(String routeNo,String serviceType);
	public void setTimeTableData(List<RouteSetUpDTO> modifiedDataList);
	
	public List<RouteSetUpDTO> searchRouteDetailsEditView(String routeNo,String serviceType,String groupNo);
	public void editedTimeTableData(List<RouteSetUpDTO> modifiedDataListEdit);
	List<RouteSetUpDTO> getRouteNoEdit();
	boolean searchHistory(String routeNo, String serviceType, String group);
	boolean UpdateStatus(String routeNo, String serviceType, String group);
	List<RouteSetUpDTO> getRouteDetails(String refNo);
	boolean saveDeletedDetails(RouteSetUpDTO modifiedDataList, String reason, String user, String refNo);
	boolean removeTimeTableData(String refNo);
	boolean removeTempTable(RouteSetUpDTO data);
	List<String> getBusNoList();
	List<String> getPermitNoList();
	List<RouteSetUpDTO> getNFCDetails(String permitNo, String busNo);
	boolean removeNFCData(List<RouteSetUpDTO> dataList);
	boolean saveNFCDeletedDetails(List<RouteSetUpDTO> dataList, String reason, String user);
	List<RouteSetUpDTO> getDeletedNFCDetails();
	
	// To get details and time slotes from RouteSchedule function : added by dhananjika.d (26/04/2024)
	public List<String> getRefNoList(String route, String serviceNo);
	boolean checkRefNo(String ref);
	List<String> getTerminalTimeTableStartTime(String refNo);
	List<List<Object>> getTerminalTimeTableDetails(String refNo);
}
