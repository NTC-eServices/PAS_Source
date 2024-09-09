package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.FlyingSquadActionPointsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;

public interface FlyManageGroupService extends Serializable {

	static final long serialVersionUID = 1L;

	public void insertGroups(String groupName, String groupStatus, String user);

	public String genarateGroupcode();

	public FlyingSquadGroupsDTO search(String groupName);

	public void updateGroups(String groupCd, String groupStatus, String user);

	public List<FlyingSquadGroupsDTO> getGroupDetails();

	public String insertGroupsNew(String groupName, String groupStatus, String user);

	public List<FlyingSquadActionPointsDTO> getActionPointDetails();

	public boolean insertActionPoints(FlyingSquadActionPointsDTO flyingSquadActionPointsDTO, String user);

	public boolean updateActionPoints(FlyingSquadActionPointsDTO flyingSquadActionPointsDTO, String user);

	public String checkDuplicate(String actionCode);
}
