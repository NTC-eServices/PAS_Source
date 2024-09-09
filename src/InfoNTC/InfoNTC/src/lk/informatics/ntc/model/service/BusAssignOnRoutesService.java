package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.BusAssignOnRoutesDTO;

public interface BusAssignOnRoutesService {

	public List routeNoDropDown();

	public List busNoDropDown(String value);

	public BusAssignOnRoutesDTO busCategoryDropDown(BusAssignOnRoutesDTO busDTO);

	public BusAssignOnRoutesDTO originDestination(String busNo, String route);

	public boolean checkSwap(String vehNo);

	public void add(BusAssignOnRoutesDTO busDTO, String user);

	public boolean addCheck(BusAssignOnRoutesDTO busDTO);

	public List table();

	public boolean editTable(String user, String busNo, String status);
}
