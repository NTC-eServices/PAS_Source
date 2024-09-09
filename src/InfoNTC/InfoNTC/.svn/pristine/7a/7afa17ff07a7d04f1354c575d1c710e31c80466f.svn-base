package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lk.informatics.ntc.model.dto.BusDetailsDTO;
import lk.informatics.ntc.model.dto.SetUpMidPointsDTO;

public interface SetUpMidPointsService extends Serializable {
	
	public List<SetUpMidPointsDTO> getRoutesToDropdown();
	
	public List<SetUpMidPointsDTO> getdesCriptionToDropdown();

	public List<SetUpMidPointsDTO> getServiceTypeToDropDown();
	
	public SetUpMidPointsDTO getDetailsbyRouteNo(String routeNo);
	
	public List<SetUpMidPointsDTO> getAllMidPointsOrgToDes(String routeNo, String serviceType);
	
	public List<SetUpMidPointsDTO> getAllMidPointsDesToOrg(String routeNo, String serviceType);

	public List<BusDetailsDTO> getDetailsByRouteAndServiceType(String routeNo, String serviceType, List<SetUpMidPointsDTO> selectedOrgToDesMidPointList, List<SetUpMidPointsDTO> orgToDesMidPointList,String group);
	
	public List<BusDetailsDTO> getDetailsByRouteAndServiceTypeDesToOrg(String routeNo, String serviceType, List<SetUpMidPointsDTO> selectedDesToOrgMidPointList, List<SetUpMidPointsDTO> desToOrgMidPointList,String group);
	
	public List<SetUpMidPointsDTO> getAbbriviation();
	
	public List<SetUpMidPointsDTO> getAbbriviationDes();
	
	public List<SetUpMidPointsDTO> getPermitNoToDropDown();
	
	public List<SetUpMidPointsDTO> getBusNoToDropDown();
	
	public void saveSelectedData(SetUpMidPointsDTO data);

	public List<SetUpMidPointsDTO> getStartTime(String routeNo, String serviceType);
	
	public List<SetUpMidPointsDTO> getMidPointName(String routeNo, String serviceType);
	
	public List<SetUpMidPointsDTO> getMidPointsTime(String routeNo, String serviceType);
	
	public List<SetUpMidPointsDTO> getMidPointNameDes(String routeNo, String serviceType);
	
	public List<SetUpMidPointsDTO> getMidPointsTimeDes(String routeNo, String serviceType);
	
	public List<BusDetailsDTO> getpermitNo(String routeNo);

	public void updateTakenTime(List<SetUpMidPointsDTO> selectedOrgToDesMidPointList, List<SetUpMidPointsDTO> selectedDesToOrgMidPointList);

	public void setMidPointDataOrgToDes(List<BusDetailsDTO> selectedOrgToDesBusDetailsList,String group);

	public boolean setMidPointDataDesToOrg(List<BusDetailsDTO> selectedOrgToDesBusDetailsList,String group);

	List<String> calculateArrivalTimesOrgList(String startTime, List<SetUpMidPointsDTO> selectedMidPointList,
			List<SetUpMidPointsDTO> desToOrgMidPointList);

	List<String> getPermitNoToDropDown(String routeNo);

	List<String> getBusNoToDropDown(String routeNo);

	SetUpMidPointsDTO getDetails(String routeNo, String serviceType);
	
	void updateTimeOrigin(List<BusDetailsDTO> selectedOrgToDesBusDetailsList);

	void updateTimeDestination(List<BusDetailsDTO> selectedOrgToDesBusDetailsList);


}
