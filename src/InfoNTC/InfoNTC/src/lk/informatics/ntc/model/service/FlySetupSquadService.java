package lk.informatics.ntc.model.service;

import java.sql.Timestamp;
import java.util.ArrayList;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadSteupDTO;
import lk.informatics.ntc.model.dto.UserDTO;

public interface FlySetupSquadService {

	public ArrayList<FlyingSquadGroupsDTO> getGroupList();

	public ArrayList<UserDTO> getUserList();

	public ArrayList<UserDTO> getEmpNoList();

	public FlyingSquadSteupDTO getOfficerDetails(String userName);

	public void insertFlyingSquad(String groupCd, String UserId, String empId, String empName, String status,
			String user);

	public ArrayList<FlyingSquadSteupDTO> getAllDetails();

	public void insertFlyingSquadHistory(long seqNo, String groupCd, String UserId, String empId, String empName,
			String status, String createdBy, Timestamp createdDate, String modifiedby, Timestamp modififedDate);

	public void updteDetails(String groupCd, String userId, String status, String user);

	public FlyingSquadSteupDTO getOfficerDetailsFromEmpId(String empNo);

	public boolean checkduplicateValues(String groupCd, String userId);

}
