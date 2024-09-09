package lk.informatics.ntc.model.service;

import java.util.ArrayList;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadSheduleDTO;
import lk.informatics.ntc.model.dto.RosterDTO;

public interface FlyingSquadScheduleService {

	public ArrayList<RosterDTO> getRosterCd();

	public ArrayList<FlyingSquadSheduleDTO> genarateDetails(int year, int month);

	public ArrayList<FlyingSquadSheduleDTO> getGroupcode();

	public Integer getcount(int year, int month);

	public void insertDetails(ArrayList<FlyingSquadSheduleDTO> flyingSquadSheduleDTOList, String user, int year,
			int month);

	public void updateShedule(ArrayList<FlyingSquadSheduleDTO> flyingSquadSheduleDTOList, String user, int year,
			int month);

	public ArrayList<FlyingSquadGroupsDTO> getServiceDetails();
}
