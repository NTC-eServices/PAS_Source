package lk.informatics.ntc.view.beans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.EventParticipationDTO;
import lk.informatics.ntc.model.dto.LetterMaintenanceDTO;
import lk.informatics.ntc.model.dto.ProgramDTO;
import lk.informatics.ntc.model.dto.ProjectDTO;
import lk.informatics.ntc.model.dto.TransportDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.AwarenessManagementService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

/**
 * 
 * @author dilakshi.h
 *
 */
@ViewScoped
@ManagedBean(name = "awarenessManagementBackingBean")
public class AwarenessManagementBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private AwarenessManagementService awarenessManagementService;
	private CommonService commonService;
	private AdminService adminService;

	private String sucessMsg;
	private String errorMsg;

	String dateFormatStr = "dd/MM/yyyy";
	DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
	DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	private Date currentDate = new Date();

	private List<ProjectDTO> projectList = new ArrayList<ProjectDTO>();
	private List<ProjectDTO> filteredProjectList;
	private List<ProgramDTO> filteredProgramList;
	private ProjectDTO selectedProject = new ProjectDTO();
	private ProgramDTO selectedProgram = new ProgramDTO();
	private ProjectDTO newProject = new ProjectDTO();
	private ProgramDTO newProgram = new ProgramDTO();
	private boolean showDetail = false;

	private List<CommonDTO> provinceList;
	private List<CommonDTO> districtList;
	private List<CommonDTO> districtListAll;
	private List<DropDownDTO> eduZoneList;
	private List<DropDownDTO> officerTypeList;

	// Letter Management
	private List<LetterMaintenanceDTO> letterList = new ArrayList<LetterMaintenanceDTO>();
	private LetterMaintenanceDTO selectedLetter = new LetterMaintenanceDTO();

	// Event Management
	private List<EventParticipationDTO> participationList = new ArrayList<EventParticipationDTO>();
	private EventParticipationDTO newParticipant = new EventParticipationDTO();
	private EventParticipationDTO selectedParticipant = new EventParticipationDTO();
	private List<EmployeeDTO> employeeList = new ArrayList<EmployeeDTO>();

	// Transport Management
	private List<TransportDTO> transportList = new ArrayList<TransportDTO>();
	private TransportDTO newTransport = new TransportDTO();
	private TransportDTO selectedTransport = new TransportDTO();

	public AwarenessManagementBackingBean() {
		awarenessManagementService = (AwarenessManagementService) SpringApplicationContex
				.getBean("awarenessManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");

		provinceList = adminService.getProvinceToDropdown();
		eduZoneList = awarenessManagementService.getEduZoneList();
		officerTypeList = awarenessManagementService.getOfficerTypeList();
		employeeList = awarenessManagementService.getAllEmployee();
		districtListAll = adminService.getDistrictToDropdown();
		refreshProjectList();
	}

	public void refreshProjectList() {
		projectList = awarenessManagementService.getProjectList();
		selectedProject = new ProjectDTO();
		selectedProject.setSubProgramList(new ArrayList<ProgramDTO>());
		showDetail = false;
		selectedProgram = new ProgramDTO();
	}

	public void updateProvince(String status, ProgramDTO program) {
		String province = null;
		if (status.equals("NEW")) {
			province = newProgram.getProvince();
		} else {
			selectedProgram = program;
			province = selectedProgram.getProvince();
		}
		districtList = adminService.getDistrictByProvinceToDropdown(province);
	}

	public void showNewProjectDialog() {
		newProject = new ProjectDTO();
		newProject.setRefNo(commonService.generateNewRefNo("AM-PRJ"));
		RequestContext.getCurrentInstance().execute("PF('newProjectDialog').show();");
	}

	public void saveNewProject() {
		String user = sessionBackingBean.getLoginUser();

		if (newProject.getRefNo() == null || newProject.getRefNo().isEmpty() || newProject.getProjectName() == null
				|| newProject.getProjectName().isEmpty()) {
			showMsg("ERROR", "Mandatory fields cannot be empty");
			return;
		}
		if (awarenessManagementService.insertNewProject(newProject, user)) {
			projectList = awarenessManagementService.getProjectList();
			commonService.updateRefNoSeq("AM-PRJ", newProject.getRefNo(), user);
			showMsg("SUCCESS", "Saved Successfully");
			RequestContext.getCurrentInstance().execute("PF('newProjectDialog').hide();");
		} else {
			showMsg("ERROR", "Error while saving data");
		}

	}

	public void clearNewProjectDialog() {
		newProject.setAwarenessProgram_seq(null);
		newProject.setProjectName(null);
		newProject.setNoOfProject(0);
		newProject.setStatus(null);
	}

	public void updateProject(RowEditEvent event) {
		String user = sessionBackingBean.getLoginUser();
		ProjectDTO updateProject = (ProjectDTO) event.getObject();
		updateProject.setSubProgramList(awarenessManagementService.getProgramList(updateProject.getRefNo()));

		if (updateProject.getRefNo() == null || updateProject.getRefNo().isEmpty()
				|| updateProject.getProjectName() == null || updateProject.getProjectName().isEmpty()) {
			showMsg("ERROR", "Mandatory fields cannot be empty");
			projectList = awarenessManagementService.getProjectList();
			return;
		}
		if (updateProject.getNoOfProject() < activeProgramCount(updateProject.getSubProgramList())) {
			showMsg("ERROR", "No. of Programs cannot be lesser than Active Program Count.");
			projectList = awarenessManagementService.getProjectList();
			return;
		}
		if (awarenessManagementService.updateProject(updateProject, user)) {
			projectList = awarenessManagementService.getProjectList();
			showMsg("SUCCESS", "Updated Successfully");
		} else {
			showMsg("ERROR", "Error while saving data");
			projectList = awarenessManagementService.getProjectList();
		}
	}

	public void deleteProject() {
		if (awarenessManagementService.removeProject(selectedProject)) {
			refreshProjectList();
			showMsg("SUCCESS", "Deleted Successfully");
		} else {
			showMsg("ERROR", "Error while deleting data");
		}
	}

	public void viewProjectDetails() {
		selectedProject.setSubProgramList(awarenessManagementService.getProgramList(selectedProject.getRefNo()));
		showDetail = true;
	}

	// Program Related
	public void showNewProgramDialog() {
		if (activeProgramCount(selectedProject.getSubProgramList()) >= selectedProject.getNoOfProject()) {
			showMsg("ERROR", "No. of Active Programs can not be exceeded.");
			return;
		}
		newProgram = new ProgramDTO();
		newProgram.setStatus(selectedProject.getStatus());
		newProgram.setType("S");
		newProgram.setSubRefNo(commonService.generateNewRefNo("AM-MS"));

		RequestContext.getCurrentInstance().execute("PF('newProgramDialog').show();");
	}

	public void changeProgramType() {
		if (newProgram.getType().equalsIgnoreCase("O"))
			newProgram.setSubRefNo(commonService.generateNewRefNo("AM-MO"));
		else
			newProgram.setSubRefNo(commonService.generateNewRefNo("AM-MS"));
	}

	private int activeProgramCount(List<ProgramDTO> progrms) {
		int count = 0;
		for (ProgramDTO programDTO : nullSafe(progrms)) {
			if (programDTO.getStatus().equalsIgnoreCase("A"))
				count++;
		}
		return count;
	}

	public void saveNewProgram() {
		if (newProgram.getSubRefNo() == null || newProgram.getSubRefNo().isEmpty() || newProgram.getProvince() == null
				|| newProgram.getProvince().isEmpty() || newProgram.getDistrict() == null
				|| newProgram.getDistrict().isEmpty() || newProgram.getScheduleDate() == null
				|| newProgram.getScheduleDate().isEmpty() || newProgram.getPlace() == null
				|| newProgram.getPlace().isEmpty()) {
			showMsg("ERROR", "Mandatory fields cannot be empty");
			return;
		}

		String user = sessionBackingBean.getLoginUser();

		if (awarenessManagementService.insertNewProgram(selectedProject.getAwarenessProgram_seq(),
				selectedProject.getRefNo(), newProgram, user)) {
			showMsg("SUCCESS", "Saved Successfully");
			RequestContext.getCurrentInstance().execute("PF('newProgramDialog').hide();");
			selectedProject.setSubProgramList(awarenessManagementService.getProgramList(selectedProject.getRefNo()));
			if (newProgram.getType().equalsIgnoreCase("O"))
				commonService.updateRefNoSeq("AM-MO", newProgram.getSubRefNo(), user);
			else
				commonService.updateRefNoSeq("AM-MS", newProgram.getSubRefNo(), user);

		}

	}

	public void clearNewProgramDialog() {
		newProgram.setDetail_seq(null);
		newProgram.setProvince(null);
		newProgram.setDistrict(null);
		newProgram.setZone(null);
		newProgram.setScheduleDate(null);
		newProgram.setPlace(null);
	}

	public void deleteProgram() {
		if (awarenessManagementService.removeProgram(selectedProgram)) {
			showMsg("SUCCESS", "Deleted Successfully");
			resetPrograms();
		}
	}

	public void updateProgram(RowEditEvent event) {
		String user = sessionBackingBean.getLoginUser();
		selectedProgram = (ProgramDTO) event.getObject();

		if (selectedProgram.getSubRefNo() == null || selectedProgram.getSubRefNo().isEmpty()
				|| selectedProgram.getProvince() == null || selectedProgram.getProvince().isEmpty()
				|| selectedProgram.getDistrict() == null || selectedProgram.getDistrict().isEmpty()
				|| selectedProgram.getScheduleDate() == null || selectedProgram.getScheduleDate().isEmpty()
				|| selectedProgram.getPlace() == null || selectedProgram.getPlace().isEmpty()) {
			showMsg("ERROR", "Mandatory fields cannot be empty");
			resetPrograms();
			return;
		}

		if (!selectedProject.getStatus().equals("A") && newProgram.getStatus().equals("A")) {
			showMsg("ERROR", "Not Allowed. Project is not in Active Status.");
			resetPrograms();
			return;
		}

		if (activeProgramCount(selectedProject.getSubProgramList()) > selectedProject.getNoOfProject()) {
			showMsg("ERROR", "No. of Active Programs can not be exceeded.");
			resetPrograms();
			return;
		}

		if (awarenessManagementService.updateProgram(selectedProgram, user)) {
			showMsg("SUCCESS", "Updated Successfully");
			resetPrograms();
		}
	}

	private void resetPrograms() {
		selectedProject.setSubProgramList(awarenessManagementService.getProgramList(selectedProject.getRefNo()));
		selectedProgram = new ProgramDTO();
	}

	// Program Letter Management
	public void loadLetterManagement() {
		resetLetterData();
		RequestContext.getCurrentInstance().execute("PF('letterDialog').show()");
	}

	public void resetLetterData() {
		selectedLetter = awarenessManagementService.getLetterData(selectedProgram.getSubRefNo());
		// set current year
		if (selectedLetter.getPld_seq() == null) {
			selectedLetter.setYear(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
		}
	}

	public void saveLetterData() {
		if (selectedLetter.getSchoolName() == null || selectedLetter.getSchoolName().isEmpty()) {
			showMsg("ERROR", "Mandatory fields cannot be empty");

			try {
				SimpleDateFormat newDf = new SimpleDateFormat("dd/MM/yyyy");
				DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
				selectedLetter.setZoneIssueDate(newDf.format(oldDf.parse(selectedLetter.getZoneIssueDate())));
				selectedLetter.setSchoolLetterDate(newDf.format(oldDf.parse(selectedLetter.getSchoolLetterDate())));
				selectedLetter.setPoliceLetterDate(newDf.format(oldDf.parse(selectedLetter.getPoliceLetterDate())));
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return;
		}

		String user = sessionBackingBean.getLoginUser();

		if (awarenessManagementService.insertNewLetter(selectedProgram.getSubRefNo(), selectedLetter, user)) {
			showMsg("SUCCESS", "Saved Successfully");
			RequestContext.getCurrentInstance().execute("PF('letterDialog').hide();");

		} else {
			showMsg("ERROR", "Error while saving data");
		}
	}

	public void previewLetterManagement() {
		letterList = awarenessManagementService.getLettersByProject(selectedProject.getRefNo());
		RequestContext.getCurrentInstance().execute("PF('lettersPreviewDialog').show()");
	}

	// Program Event Management
	public void loadEventManagement() {
		selectedProgram = awarenessManagementService.getEventDetails(selectedProgram);
		participationList = awarenessManagementService.getAllEventStaff(selectedProgram.getSubRefNo());
		RequestContext.getCurrentInstance().execute("PF('eventDialog').show()");
	}

	public void saveEventDetail() {
		// mandatory check
		String user = sessionBackingBean.getLoginUser();

		if (awarenessManagementService.updateEventDetails(selectedProgram, user)) {
			showMsg("SUCCESS", "Updated Successfully");
		} else {
			showMsg("ERROR", "Error");
		}
	}

	public void resetEventDetail() {
		selectedProgram = awarenessManagementService.getEventDetails(selectedProgram);
	}

	public void showNewParticipantDialog() {
		newParticipant = new EventParticipationDTO();
		RequestContext.getCurrentInstance().execute("PF('newParticipantDialog').show()");
	}

	public void saveNewParticipant() {
		if ((newParticipant.getTypeOfParticipation() == null && newParticipant.getTypeOfParticipation().equals("E")
				&& (newParticipant.getEmpNo() == null || newParticipant.getEmpNo().trim().isEmpty()))
				|| newParticipant.getParticipantName() == null
				|| newParticipant.getParticipantName().trim().isEmpty()) {
			showMsg("ERROR", "Mandatory fields cannot be empty");
			return;
		}

		for (EventParticipationDTO p : participationList) {
			if (p.getEmpNo() != null && p.getEmpNo().equalsIgnoreCase(newParticipant.getEmpNo())) {
				showMsg("ERROR", "This participant is added already.");
				participationList = awarenessManagementService.getAllEventStaff(selectedProgram.getSubRefNo());
				return;
			}
		}

		String user = sessionBackingBean.getLoginUser();

		if (awarenessManagementService.insertNewParticipant(selectedProgram.getSubRefNo(), newParticipant, user)) {
			RequestContext.getCurrentInstance().execute("PF('newParticipantDialog').hide();");
			RequestContext.getCurrentInstance().execute("PF('eventDialog').show()");
			showMsg("SUCCESS", "Saved Successfully");
			participationList = awarenessManagementService.getAllEventStaff(selectedProgram.getSubRefNo());
		} else {
			RequestContext.getCurrentInstance().execute("PF('eventDialog').show()");
			showMsg("ERROR", "Error while saving data");
		}
	}

	public void clearNewParticipantDialog() {
		newParticipant = new EventParticipationDTO();
	}

	public List<String> completeEmpNo(String query) {
		query = query.toUpperCase();
		List<String> filteredEmp = new ArrayList<String>();

		for (int i = 0; i < employeeList.size(); i++) {
			EmployeeDTO dto = employeeList.get(i);
			if (dto.getEmpNo().toUpperCase().contains(query)) {
				filteredEmp.add(dto.getEmpNo());
			}
		}
		return filteredEmp;
	}

	public List<String> completeEpfNo(String query) {
		query = query.toUpperCase();
		List<String> filteredEpf = new ArrayList<String>();

		for (int i = 0; i < employeeList.size(); i++) {
			EmployeeDTO dto = employeeList.get(i);
			if (dto.getEmpEpfNo().toUpperCase().contains(query)) {
				filteredEpf.add(dto.getEmpEpfNo());
			}
		}
		return filteredEpf;
	}

	public void changeEmpNo(String status, EventParticipationDTO participant) {
		if (status.equals("NEW")) {
			for (EmployeeDTO e : nullSafe(employeeList)) {
				if (e.getEmpNo().equalsIgnoreCase(newParticipant.getEmpNo())) {
					newParticipant.setEpfNo(e.getEmpEpfNo());
					newParticipant.setParticipantName(e.getFullName());
					newParticipant.setIdNo(e.getNicNo());
					return;
				}
			}
		} else {
			for (EmployeeDTO e : nullSafe(employeeList)) {
				if (e.getEmpNo().equalsIgnoreCase(participant.getEmpNo())) {
					selectedParticipant.setEmpNo(e.getEmpNo());
					selectedParticipant.setEpfNo(e.getEmpEpfNo());
					selectedParticipant.setParticipantName(e.getFullName());
					selectedParticipant.setIdNo(e.getNicNo());
					return;
				}
			}
		}
	}

	public void changeEpfNo(String status, EventParticipationDTO participant) {
		if (status.equals("NEW")) {
			for (EmployeeDTO e : nullSafe(employeeList)) {
				if (e.getEmpEpfNo().equalsIgnoreCase(newParticipant.getEpfNo())) {
					newParticipant.setEmpNo(e.getEmpNo());
					newParticipant.setParticipantName(e.getFullName());
					newParticipant.setIdNo(e.getNicNo());
					return;
				}
			}
		} else {
			for (EmployeeDTO e : nullSafe(employeeList)) {
				if (e.getEmpEpfNo().equalsIgnoreCase(participant.getEpfNo())) {
					selectedParticipant.setEmpNo(e.getEmpNo());
					selectedParticipant.setEpfNo(e.getEmpEpfNo());
					selectedParticipant.setParticipantName(e.getFullName());
					selectedParticipant.setIdNo(e.getNicNo());
					return;
				}
			}
		}
	}

	public void changeParticipationType(String status) {
		if (status.equals("NEW")) {
			newParticipant.setEmpNo(null);
			newParticipant.setEpfNo(null);
			newParticipant.setOfficerType(null);
		} else {
			selectedParticipant.setEmpNo(null);
			selectedParticipant.setEpfNo(null);
			selectedParticipant.setOfficerType(null);
		}
	}

	public void updateParticipant(RowEditEvent event) {
		String user = sessionBackingBean.getLoginUser();
		EventParticipationDTO updatedParticipant = (EventParticipationDTO) event.getObject();
		if ((selectedParticipant.getEmpNo() != null || selectedParticipant.getEpfNo() != null)
				&& updatedParticipant.getTypeOfParticipation().equalsIgnoreCase("E")) {
			String updatedEPF = selectedParticipant.getEpfNo();
			String updatedEmpNo = selectedParticipant.getEmpNo();
			String updatedName = selectedParticipant.getParticipantName();
			updatedParticipant.setEpfNo(updatedEPF);
			updatedParticipant.setEmpNo(updatedEmpNo);
			updatedParticipant.setParticipantName(updatedName);
		}

		if ((updatedParticipant.getTypeOfParticipation() == null
				&& updatedParticipant.getTypeOfParticipation().equals("E")
				&& (updatedParticipant.getEmpNo() == null || updatedParticipant.getEmpNo().trim().isEmpty()))
				|| updatedParticipant.getParticipantName() == null
				|| updatedParticipant.getParticipantName().trim().isEmpty()) {
			showMsg("ERROR", "Mandatory fields cannot be empty");
			participationList = awarenessManagementService.getAllEventStaff(selectedProgram.getSubRefNo());
			return;
		}

		for (EventParticipationDTO p : awarenessManagementService.getAllEventStaff(selectedProgram.getSubRefNo())) {
			if (p.getEmpNo() != null && p.getEmpNo().equalsIgnoreCase(updatedParticipant.getEmpNo())) {
				showMsg("ERROR", "This participant is added already.");
				participationList = awarenessManagementService.getAllEventStaff(selectedProgram.getSubRefNo());
				return;
			}
		}
		if (awarenessManagementService.updateParticipant(updatedParticipant, user)) {
			showMsg("SUCCESS", "Updated Successfully");
		} else {
			showMsg("ERROR", "Error while saving data");
		}
		participationList = awarenessManagementService.getAllEventStaff(selectedProgram.getSubRefNo());
		selectedParticipant = new EventParticipationDTO();
	}

	public void removeParticipant() {
		if (awarenessManagementService.removeParticipant(selectedParticipant.getEpd_seq())) {
			showMsg("SUCCESS", "Deleted Successfully");
			participationList = awarenessManagementService.getAllEventStaff(selectedProgram.getSubRefNo());
			selectedParticipant = new EventParticipationDTO();
		}
	}

	// Program Transport Management
	public void loadTransportManagement() {
		transportList = awarenessManagementService.getTransportByProgram(selectedProgram.getSubRefNo());
		RequestContext.getCurrentInstance().execute("PF('transportDialog').show()");
	}

	public void showNewTransportDialog() {
		newTransport = new TransportDTO();
		RequestContext.getCurrentInstance().execute("PF('newTransportDialog').show();");
	}

	public void clearNewTransportDialog() {
		newTransport = new TransportDTO();
	}

	public void onChangeVehicleNo() {
		for (TransportDTO trans : transportList) {
			if (trans.getVehicleNo().equalsIgnoreCase(newTransport.getVehicleNo())) {
				showMsg("ERROR", "This Vehicle is added already.");
				return;
			}
		}
	}

	public void saveNewTransport() {
		// validate vehicle no
		if (newTransport.getVehicleNo() == null || newTransport.getVehicleNo().isEmpty()
				|| newTransport.getDriverName() == null || newTransport.getDriverName().isEmpty()
				|| newTransport.getKm() == null || newTransport.getTransportDate() == null
				|| newTransport.getTransportDate().isEmpty()) {
			showMsg("ERROR", "Mandatory fields cannot be empty");
			return;
		}

		for (TransportDTO trans : transportList) {

			try {
				newTransport.setTransportDate(dateFormat.format(oldDf.parse(newTransport.getTransportDate())));
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (ParseException e) {
			}

			if (trans.getVehicleNo().equalsIgnoreCase(newTransport.getVehicleNo()) && trans.getTransportDate() != null
					&& trans.getTransportDate().equals(newTransport.getTransportDate())) {
				showMsg("ERROR", "This Vehicle is already assigned for the selected date.");
				return;
			}
		}

		String user = sessionBackingBean.getLoginUser();

		if (awarenessManagementService.insertNewTransport(selectedProgram.getSubRefNo(), newTransport, user)) {
			showMsg("SUCCESS", "Saved Successfully");
			RequestContext.getCurrentInstance().execute("PF('newTransportDialog').hide();");
			RequestContext.getCurrentInstance().execute("PF('transportDialog').show()");
			transportList = awarenessManagementService.getTransportByProgram(selectedProgram.getSubRefNo());
		} else {
			showMsg("ERROR", "Error while saving data");
		}
	}

	public void updateTransport(RowEditEvent event) {
		String user = sessionBackingBean.getLoginUser();
		TransportDTO updatedTransport = (TransportDTO) event.getObject();

		if (updatedTransport.getVehicleNo() == null || updatedTransport.getVehicleNo().isEmpty()
				|| updatedTransport.getDriverName() == null || updatedTransport.getDriverName().isEmpty()
				|| updatedTransport.getKm() == null || updatedTransport.getTransportDate() == null
				|| updatedTransport.getTransportDate().isEmpty()) {
			showMsg("ERROR", "Mandatory fields cannot be empty");
			transportList = awarenessManagementService.getTransportByProgram(selectedProgram.getSubRefNo());
			return;
		}

		TransportDTO oldTransDTO = new TransportDTO();

		for (TransportDTO trans : awarenessManagementService.getTransportByProgram(selectedProgram.getSubRefNo())) {
			if (trans.getEtd_seq() == updatedTransport.getEtd_seq()) {
				oldTransDTO = trans;
				break;
			}

		}
		for (TransportDTO trans : awarenessManagementService.getTransportByProgram(selectedProgram.getSubRefNo())) {
			try {
				updatedTransport.setTransportDate(dateFormat.format(oldDf.parse(updatedTransport.getTransportDate())));
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (ParseException e) {
			}

			// when vehicle no / transport date is been changed validate vehicle
			// no and date
			if (!(oldTransDTO.getVehicleNo().equalsIgnoreCase(updatedTransport.getVehicleNo())
					&& oldTransDTO.getTransportDate().equalsIgnoreCase(updatedTransport.getTransportDate()))) {
				if (trans.getVehicleNo().equalsIgnoreCase(updatedTransport.getVehicleNo())
						&& trans.getTransportDate() != null
						&& trans.getTransportDate().equals(updatedTransport.getTransportDate())) {
					showMsg("ERROR", "This Vehicle is already assigned for the selected date.");
					transportList = awarenessManagementService.getTransportByProgram(selectedProgram.getSubRefNo());
					return;
				}
			}
		}
		if (awarenessManagementService.updateTransport(updatedTransport, user)) {
			showMsg("SUCCESS", "Updated Successfully");
		} else {
			showMsg("ERROR", "Error while saving data");
		}
		transportList = awarenessManagementService.getTransportByProgram(selectedProgram.getSubRefNo());
		RequestContext.getCurrentInstance().update("frmTransportMgmnt:dtTransport");
	}

	public void removeTransport() {
		if (awarenessManagementService.removeTransport(selectedTransport.getEtd_seq())) {
			showMsg("SUCCESS", "Deleted Successfully");
			transportList = awarenessManagementService.getTransportByProgram(selectedProgram.getSubRefNo());
			selectedTransport = new TransportDTO();
		}
	}

	public void previewTransportManagement() {
		transportList = awarenessManagementService.getTransportByProject(selectedProject.getRefNo());
		RequestContext.getCurrentInstance().execute("PF('transportPreviewDialog').show();");
	}

	// Common methods

	public void showMsg(String type, String msg) {
		if (type.equalsIgnoreCase("ERROR")) {
			errorMsg = msg;
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		} else {
			sucessMsg = msg;
			RequestContext.getCurrentInstance().update("frmSuccess");
			RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
		}
	}

	private static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	//////////////// getter & setters /////////////////////

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public AwarenessManagementService getAwarenessManagementService() {
		return awarenessManagementService;
	}

	public void setAwarenessManagementService(AwarenessManagementService awarenessManagementService) {
		this.awarenessManagementService = awarenessManagementService;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public List<ProjectDTO> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<ProjectDTO> projectList) {
		this.projectList = projectList;
	}

	public ProjectDTO getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(ProjectDTO selectedProject) {
		this.selectedProject = selectedProject;
	}

	public ProgramDTO getSelectedProgram() {
		return selectedProgram;
	}

	public void setSelectedProgram(ProgramDTO selectedProgram) {
		this.selectedProgram = selectedProgram;
	}

	public ProjectDTO getNewProject() {
		return newProject;
	}

	public void setNewProject(ProjectDTO newProject) {
		this.newProject = newProject;
	}

	public ProgramDTO getNewProgram() {
		return newProgram;
	}

	public void setNewProgram(ProgramDTO newProgram) {
		this.newProgram = newProgram;
	}

	public boolean isShowDetail() {
		return showDetail;
	}

	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<CommonDTO> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<CommonDTO> provinceList) {
		this.provinceList = provinceList;
	}

	public List<CommonDTO> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<CommonDTO> districtList) {
		this.districtList = districtList;
	}

	public List<DropDownDTO> getEduZoneList() {
		return eduZoneList;
	}

	public void setEduZoneList(List<DropDownDTO> eduZoneList) {
		this.eduZoneList = eduZoneList;
	}

	public List<DropDownDTO> getOfficerTypeList() {
		return officerTypeList;
	}

	public void setOfficerTypeList(List<DropDownDTO> officerTypeList) {
		this.officerTypeList = officerTypeList;
	}

	public List<ProjectDTO> getFilteredProjectList() {
		return filteredProjectList;
	}

	public void setFilteredProjectList(List<ProjectDTO> filteredProjectList) {
		this.filteredProjectList = filteredProjectList;
	}

	public List<ProgramDTO> getFilteredProgramList() {
		return filteredProgramList;
	}

	public void setFilteredProgramList(List<ProgramDTO> filteredProgramList) {
		this.filteredProgramList = filteredProgramList;
	}

	public List<LetterMaintenanceDTO> getLetterList() {
		return letterList;
	}

	public void setLetterList(List<LetterMaintenanceDTO> letterList) {
		this.letterList = letterList;
	}

	public LetterMaintenanceDTO getSelectedLetter() {
		return selectedLetter;
	}

	public void setSelectedLetter(LetterMaintenanceDTO selectedLetter) {
		this.selectedLetter = selectedLetter;
	}

	public List<EventParticipationDTO> getParticipationList() {
		return participationList;
	}

	public void setParticipationList(List<EventParticipationDTO> participationList) {
		this.participationList = participationList;
	}

	public EventParticipationDTO getNewParticipant() {
		return newParticipant;
	}

	public void setNewParticipant(EventParticipationDTO newParticipant) {
		this.newParticipant = newParticipant;
	}

	public EventParticipationDTO getSelectedParticipant() {
		return selectedParticipant;
	}

	public void setSelectedParticipant(EventParticipationDTO selectedParticipant) {
		this.selectedParticipant = selectedParticipant;
	}

	public List<TransportDTO> getTransportList() {
		return transportList;
	}

	public void setTransportList(List<TransportDTO> transportList) {
		this.transportList = transportList;
	}

	public TransportDTO getNewTransport() {
		return newTransport;
	}

	public void setNewTransport(TransportDTO newTransport) {
		this.newTransport = newTransport;
	}

	public TransportDTO getSelectedTransport() {
		return selectedTransport;
	}

	public void setSelectedTransport(TransportDTO selectedTransport) {
		this.selectedTransport = selectedTransport;
	}

	public List<CommonDTO> getDistrictListAll() {
		return districtListAll;
	}

	public void setDistrictListAll(List<CommonDTO> districtListAll) {
		this.districtListAll = districtListAll;
	}

	public List<EmployeeDTO> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<EmployeeDTO> employeeList) {
		this.employeeList = employeeList;
	}

}
