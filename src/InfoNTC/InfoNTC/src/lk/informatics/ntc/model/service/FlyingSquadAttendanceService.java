package lk.informatics.ntc.model.service;

import java.util.ArrayList;
import java.util.Date;

import lk.informatics.ntc.model.dto.FlyingSquadApprovalInvestigationDTO;
import lk.informatics.ntc.model.dto.FlyngSquadAttendanceDTO;

public interface FlyingSquadAttendanceService {

	public ArrayList<FlyingSquadApprovalInvestigationDTO> getReferenceNo();

	public FlyngSquadAttendanceDTO getRefData(String refNo);

	public ArrayList<FlyngSquadAttendanceDTO> getexsistingdetails(String referenceNo, Date invesDate);

	public String isAttended(String referenceNo, Date invesDate);

	public ArrayList<FlyngSquadAttendanceDTO> getnonexsistingdetails(String referenceNo);

	public String getRefNo(Date invesDate, String groupCd);

	public void saveDetailDta(FlyngSquadAttendanceDTO flyngSquadAttendanceDTO, String user, String refNo,
			String groupcd, Date invDate);

	public void updateDetailDta(FlyngSquadAttendanceDTO flyngSquadAttendanceDTO, String user, String refNo,
			String groupcd, Date invDate);

	public void saveHistoryDetailDta(ArrayList<FlyngSquadAttendanceDTO> flyngSquadAttendanceDTOList, String user,
			String refNo, String groupcd, Date invDate);

	public ArrayList<FlyngSquadAttendanceDTO> getgroups();
}
