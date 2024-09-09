package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.EventParticipationDTO;
import lk.informatics.ntc.model.dto.LetterMaintenanceDTO;
import lk.informatics.ntc.model.dto.ProgramDTO;
import lk.informatics.ntc.model.dto.ProjectDTO;
import lk.informatics.ntc.model.dto.TransportDTO;

public interface AwarenessManagementService {

	public List<ProjectDTO> getProjectList();

	public List<ProgramDTO> getProgramList(String projectRefCode);

	public List<DropDownDTO> getEduZoneList();

	public List<DropDownDTO> getOfficerTypeList();

	public boolean insertNewProject(ProjectDTO newProjectDTO, String user);

	public boolean updateProject(ProjectDTO newProjectDTO, String user);

	public boolean removeProject(ProjectDTO newProjectDTO);

	public boolean insertNewProgram(long projectSeq, String projectRefCode, ProgramDTO newProgramDTO, String user);

	public boolean updateProgram(ProgramDTO newProgramDTO, String user);

	public boolean removeProgram(ProgramDTO newProgramDTO);

	public LetterMaintenanceDTO getLetterData(String subRefNo);

	public ProgramDTO getEventDetails(ProgramDTO program);

	public List<EventParticipationDTO> getAllEventStaff(String subRefNo);

	public boolean insertNewLetter(String subRefNo, LetterMaintenanceDTO newLetter, String user);

	public boolean updateLetter(LetterMaintenanceDTO selectedLetter, String user);

	public boolean removeLetter(long l);

	public List<EmployeeDTO> getAllEmployee();

	public boolean insertNewParticipant(String subRefNo, EventParticipationDTO newParticipant, String user);

	public boolean updateParticipant(EventParticipationDTO participant, String user);

	public boolean removeParticipant(long seq);

	public boolean updateEventDetails(ProgramDTO selectedProgram, String user);

	public List<LetterMaintenanceDTO> getLettersByProject(String refNo);

	public List<TransportDTO> getTransportByProject(String refNo);

	public List<TransportDTO> getTransportByProgram(String subRefNo);

	public boolean insertNewTransport(String subRefNo, TransportDTO newTransport, String user);

	public boolean updateTransport(TransportDTO selectedTransport, String user);

	public boolean removeTransport(Long etd_seq);

}
