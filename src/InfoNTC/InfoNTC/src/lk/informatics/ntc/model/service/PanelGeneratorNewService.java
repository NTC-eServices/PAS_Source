package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PanelGeneratorNewDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.view.beans.PanelGeneratorNewBean;

public interface PanelGeneratorNewService extends Serializable {

	public List<CommonDTO> getRoutesToDropdown();

	public List<PanelGeneratorNewDTO> getTripGeneratorList();

	public int insertPanel1(PanelGeneratorNewDTO panelGeneratorNewDTO, String user, String refNo);

	public String generateRefNo();

	public int group01_details(PanelGeneratorNewDTO panelGeneratorNewDTO, String user, List<PanelGeneratorNewDTO> group1_OD_List,
			List<PanelGeneratorNewDTO> group1_DO_List, List<String> dateList_Group1);

	public int group02_details(PanelGeneratorNewDTO panelGeneratoNewrDTO, String user, List<PanelGeneratorNewDTO> group2_OD_List,
			List<PanelGeneratorNewDTO> group2_DO_List, List<String> dateList_Group2);

	public int group03_details(PanelGeneratorNewDTO panelGeneratorNewDTO, String user, List<PanelGeneratorNewDTO> group3_OD_List,
			List<PanelGeneratorNewDTO> group3_DO_List, List<String> dateList_Group3);

	public List<PanelGeneratorNewDTO> getRefNoList();

	public PanelGeneratorNewDTO getDetails(String refNo, PanelGeneratorNewDTO panelGeneratorDTO);

	public List<String> getDateList_Group1(Long seqNo);

	public List<String> getDateList_Group2(Long seqNo);

	public List<String> getDateList_Group3(Long seqNo);

	public List<PanelGeneratorNewDTO> getODList_Group1(Long seqNo);

	public List<PanelGeneratorNewDTO> getODList_Group2(Long seqNo);

	public List<PanelGeneratorNewDTO> getODList_Group3(Long seqNo);

	public List<PanelGeneratorNewDTO> getDOList_Group1(Long seqNo);

	public List<PanelGeneratorNewDTO> getDOList_Group2(Long seqNo);

	public List<PanelGeneratorNewDTO> getDOList_Group3(Long seqNo);

	public int updateGroup1_OriginToDestination(Long seqNo, String user, List<PanelGeneratorNewDTO> group1_OD_List);

	public int updateGroup2_OriginToDestination(Long seqNo, String user, List<PanelGeneratorNewDTO> group2_OD_List);

	public int updateGroup3_OriginToDestination(Long seqNo, String user, List<PanelGeneratorNewDTO> group3_OD_List);

	public int updateGroup1_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorNewDTO> group1_DO_List);

	public int updateGroup2_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorNewDTO> group2_DO_List);

	public int updateGroup3_DestinationToOrigin(Long seqNo, String user, List<PanelGeneratorNewDTO> group3_DO_List);

	public int updateDateList_Group01(Long seqNo, String user, List<String> dateList_Group1);

	public int updateDateList_Group02(Long seqNo, String user, List<String> dateList_Group2);

	public int updateDateList_Group03(Long seqNo, String user, List<String> dateList_Group3);

	public int inActiveGroup02(Long seqNo, String user);

	public int inActiveGroup03(Long seqNo, String user);

	public int updatePanelGenerator(String timeTables, String user, String refNo);

	public int updateStatus(String status, String user, String refNo);

	public List<CommonDTO> getSelectedRoutes();
	boolean getActiveDataRecord(String routeNo, String serviceType);
	
	public void saveOriginDestinationData(PanelGeneratorNewBean panelGeneratorNewBean);
	
	public List<TimeTableDTO> getTrips();

}
