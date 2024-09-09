package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PanelGeneratorDTO;

public interface PanelGeneratorService extends Serializable {

	public List<CommonDTO> getRoutesToDropdown();

	public List<PanelGeneratorDTO> getTripGeneratorList();

	public int insertPanel1(PanelGeneratorDTO panelGeneratorDTO, String user, String refNo);

	public String generateRefNo();

	public int group01_details(PanelGeneratorDTO panelGeneratorDTO, String user, List<PanelGeneratorDTO> group1_OD_List,
			List<PanelGeneratorDTO> group1_DO_List, List<String> dateList_Group1);

	public int group02_details(PanelGeneratorDTO panelGeneratorDTO, String user, List<PanelGeneratorDTO> group2_OD_List,
			List<PanelGeneratorDTO> group2_DO_List, List<String> dateList_Group2);

	public int group03_details(PanelGeneratorDTO panelGeneratorDTO, String user, List<PanelGeneratorDTO> group3_OD_List,
			List<PanelGeneratorDTO> group3_DO_List, List<String> dateList_Group3);

	public List<PanelGeneratorDTO> getRefNoList();

	public PanelGeneratorDTO getDetails(String refNo, PanelGeneratorDTO panelGeneratorDTO);

	public List<String> getDateList_Group1(Long seqNo,String refNo);

	public List<String> getDateList_Group2(Long seqNo,String refNo);

	public List<String> getDateList_Group3(Long seqNo,String refNo);

	public List<PanelGeneratorDTO> getODList_Group1(Long seqNo);

	public List<PanelGeneratorDTO> getODList_Group2(Long seqNo);

	public List<PanelGeneratorDTO> getODList_Group3(Long seqNo);

	public List<PanelGeneratorDTO> getDOList_Group1(Long seqNo);

	public List<PanelGeneratorDTO> getDOList_Group2(Long seqNo);

	public List<PanelGeneratorDTO> getDOList_Group3(Long seqNo);

	public int updateGroup1_OriginToDestination(Long seqNo, String user, List<PanelGeneratorDTO> group1_OD_List);

	public int updateGroup2_OriginToDestination(Long seqNo, String user, List<PanelGeneratorDTO> group2_OD_List);

	public int updateGroup3_OriginToDestination(Long seqNo, String user, List<PanelGeneratorDTO> group3_OD_List);

	public int updateGroup1_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorDTO> group1_DO_List);

	public int updateGroup2_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorDTO> group2_DO_List);

	public int updateGroup3_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorDTO> group3_DO_List);

	public int updateDateList_Group01(Long seqNo, String user, List<String> dateList_Group1);

	public int updateDateList_Group02(Long seqNo, String user, List<String> dateList_Group2);

	public int updateDateList_Group03(Long seqNo, String user, List<String> dateList_Group3);

	public int inActiveGroup02(Long seqNo, String user);

	public int inActiveGroup03(Long seqNo, String user);

	public int updatePanelGenerator(String timeTables, String user, String refNo);

	public int updateStatus(String status, String user, String refNo, String route, String service);

	public List<CommonDTO> getSelectedRoutes();
	boolean getActiveDataRecord(String routeNo, String serviceType, String group, String noOfTimeTable);

	List<String> getRefNoListForDelete();

	String getActiveRefNo(String routeNo, String serviceType, String group, String noOfTimeTable);

	boolean UpdateStatus(String refNo);

	boolean UpdateStatusByRoute(String routeNo, String serviceType, String group);

}
