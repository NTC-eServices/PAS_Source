package lk.informatics.ntc.view.beans;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DriverConductorTrainingDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.MaintainTrainingScheduleDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.model.service.GrievanceManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

/**
 * 
 * @author dilakshi.h
 *
 */
@ViewScoped
@ManagedBean(name="trainingAttendanceBackingBean")
public class TrainingAttendanceBackingBean {
	
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
	private DriverConductorTrainingService driverConductorTrainingService;
	private GrievanceManagementService grievanceManagementService;
	private CommonService commonService;
	
	private MaintainTrainingScheduleDTO maintainTrainingScheduleDTO = new MaintainTrainingScheduleDTO();
	
	private String sucessMsg;
	private String errorMsg;
	
	private String dateFormatStr = "dd/MM/yyyy";
	private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
	
	private List<DriverConductorTrainingDTO> attendanceList;
	private List<CommonDTO> trainingTypeList;
	private String selectedTrainingType;
	private Date trainingDate;
	private Date startTime;
	private Date endTime;
	private String location;
	private boolean dataSaved;
	
	public TrainingAttendanceBackingBean() {
		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex.getBean("driverConductorTrainingService");
		grievanceManagementService = (GrievanceManagementService) SpringApplicationContex.getBean("grievanceManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		
		trainingTypeList = driverConductorTrainingService.GetAllTrainingTypesWithoutDuplicate();
		clearSearchView();
	}
	
	
	public void loadAttendanceList() {
		attendanceList = driverConductorTrainingService.getAttendanceList(trainingDate, selectedTrainingType, startTime, endTime, location);
	}
	
	public void clearSearchView() {
		selectedTrainingType = "";
		trainingDate = null;
		startTime = null;
		endTime = null;
		location = null;
		dataSaved = false;
		attendanceList = new ArrayList<DriverConductorTrainingDTO>();
	}
	
	public void searchAttendance() {
		if (selectedTrainingType == null || selectedTrainingType.isEmpty() || trainingDate == null) {
			showMsg("ERROR", "Mandatory fields cannot be empty.");
			return;
		}
		
		loadAttendanceList();
		
		if (attendanceList == null || attendanceList.isEmpty())
			showMsg("ERROR", "No Data found.");
	}
	
	
	public void saveForm() {
		String user = sessionBackingBean.loginUser;
		maintainTrainingScheduleDTO.setTrainingDate(trainingDate);
		
		if (!driverConductorTrainingService.saveAttendance(attendanceList, user)) {
			showMsg("ERROR", "Error saving attendance data.");
			return;
		}
		
		driverConductorTrainingService.beanLinkMethod(maintainTrainingScheduleDTO, user, "Save Attendence", "Maintain Attendance");
		showMsg("SUCCESS", "Saved Successfully");
		loadAttendanceList();
		dataSaved = true;
	}
	
	
	/*
	 * public void cancelForm() { attendanceList = new
	 * ArrayList<DriverConductorTrainingDTO>(); clearSearchView(); }
	 */
	
	
	public void printAttendanceSheet() {
		String user = sessionBackingBean.loginUser;
		maintainTrainingScheduleDTO.setTrainingDate(trainingDate);
		/** 2022/06/13 change code -----> set status as A only for print IDs**/
		for(DriverConductorTrainingDTO a: nullSafe(attendanceList))
			/*if(!driverConductorTrainingService.updateStatus(null, "A", a.getStatusTypeCode(), a.getStatusCode(), a.getAppNo(), user)) {
				showMsg("ERROR", "Error while updating status.");
				return;
			}*/
			if(a.isAttPrint()) {
			if(!driverConductorTrainingService.updateStatusAndInactivePrevious(null, "A", a.getStatusTypeCode(), a.getStatusCode(), a.getAppNo(), user,a.getDriverConductorId())) {
				showMsg("ERROR", "Error while updating status.");
				return;
			}
			}
		driverConductorTrainingService.beanLinkMethod(maintainTrainingScheduleDTO, user, "Print Attendance Sheet", "Maintain Attendance");
		showMsg("SUCCESS", "Updated Successfully.");
		
	}
	
	
	
	
	
	/*
	 * Common methodss
	 */
	
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
	
	public GrievanceManagementService getGrievanceManagementService() {
		return grievanceManagementService;
	}
	
	public void setGrievanceManagementService(GrievanceManagementService grievanceManagementService) {
		this.grievanceManagementService = grievanceManagementService;
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
	
	public String getDateFormatStr() {
		return dateFormatStr;
	}
	
	public void setDateFormatStr(String dateFormatStr) {
		this.dateFormatStr = dateFormatStr;
	}
	
	public DateTimeFormatter getTimeFormat() {
		return timeFormat;
	}
	
	public void setTimeFormat(DateTimeFormatter timeFormat) {
		this.timeFormat = timeFormat;
	}
	
	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public String getSelectedTrainingType() {
		return selectedTrainingType;
	}

	public void setSelectedTrainingType(String selectedTrainingType) {
		this.selectedTrainingType = selectedTrainingType;
	}

	public DriverConductorTrainingService getDriverConductorTrainingService() {
		return driverConductorTrainingService;
	}

	public void setDriverConductorTrainingService(DriverConductorTrainingService driverConductorTrainingService) {
		this.driverConductorTrainingService = driverConductorTrainingService;
	}

	public List<CommonDTO> getTrainingTypeList() {
		return trainingTypeList;
	}

	public void setTrainingTypeList(List<CommonDTO> trainingTypeList) {
		this.trainingTypeList = trainingTypeList;
	}

	public boolean isDataSaved() {
		return dataSaved;
	}

	public void setDataSaved(boolean dataSaved) {
		this.dataSaved = dataSaved;
	}

	public List<DriverConductorTrainingDTO> getAttendanceList() {
		return attendanceList;
	}

	public void setAttendanceList(List<DriverConductorTrainingDTO> attendanceList) {
		this.attendanceList = attendanceList;
	}

	public Date getTrainingDate() {
		return trainingDate;
	}

	public void setTrainingDate(Date trainingDate) {
		this.trainingDate = trainingDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	public MaintainTrainingScheduleDTO getMaintainTrainingScheduleDTO() {
		return maintainTrainingScheduleDTO;
	}


	public void setMaintainTrainingScheduleDTO(MaintainTrainingScheduleDTO maintainTrainingScheduleDTO) {
		this.maintainTrainingScheduleDTO = maintainTrainingScheduleDTO;
	}
	
}
