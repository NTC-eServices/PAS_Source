package lk.informatics.ntc.view.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.MaintainTrainingScheduleDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "maintainTrainingScheduleBean")
@ViewScoped
public class MaintainTrainingScheduleBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private CommonService commonService;
	private DriverConductorTrainingService driverConductorTrainingService;
	private AdminService adminService;
	private List<CommonDTO> trainingTypeList;
	private List<CommonDTO> monthList;
	private List<MaintainTrainingScheduleDTO> scheduleList = new ArrayList<MaintainTrainingScheduleDTO>();

	private MaintainTrainingScheduleDTO trainingScheduleDTO;
	private MaintainTrainingScheduleDTO strSelectedScheduleCode;

	private String strSelectedType, loginUser, errormsg;
	private String strSelectedMonth;
	private boolean disableUpdateMode, disableSaveMode;
	Date trainingDate;

	@PostConstruct
	public void init() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		loginUser = sessionBackingBean.getLoginUser();

		setTrainingScheduleDTO(new MaintainTrainingScheduleDTO());
		trainingTypeList = driverConductorTrainingService.GetAllTrainingTypesWithoutDuplicate();
		monthList = driverConductorTrainingService.GetAllMonths();
		disableUpdateMode = false;
		disableSaveMode = true;
		loadValues();
	}

	public void loadValues() {
		scheduleList = driverConductorTrainingService.getScheduleDetails();		
	}

	public void add() {
		if (validateForm()) {			
			String ssDate=new SimpleDateFormat("HH:mm").format(trainingScheduleDTO.getStartTimeN());
			String eeDate=new SimpleDateFormat("HH:mm").format(trainingScheduleDTO.getEndTimeN());												
			trainingScheduleDTO.setStartTime(ssDate);
			trainingScheduleDTO.setEndTime(eeDate);
			
			trainingScheduleDTO.setTrainingTypeCode(strSelectedType);
			trainingScheduleDTO.setMonthCode(strSelectedMonth);
			trainingScheduleDTO.setStaus("A");

			String scheduleCode = driverConductorTrainingService.insertTrainingSchedule(trainingScheduleDTO, loginUser);
			if (scheduleCode != null) {
				trainingScheduleDTO.setScheduleCode(scheduleCode);
				driverConductorTrainingService.beanLinkMethod(trainingScheduleDTO, sessionBackingBean.getLoginUser(), "Add Training Schedule", "Maintain Training Schedule");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
				clearAction();
				loadValues();
			} else {
				setErrormsg("Data not Saved.");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('Error').show()");
			}
		}

	}

	public void editAction() throws ParseException {

		String sdate =strSelectedScheduleCode.getStartTime();
		Date snDate = new SimpleDateFormat("HH:mm").parse(sdate);  
		String edate =strSelectedScheduleCode.getEndTime();
		Date enDate = new SimpleDateFormat("HH:mm").parse(edate);  
		
		trainingScheduleDTO.setStartTimeN(snDate);
		trainingScheduleDTO.setEndTimeN(enDate);
		trainingScheduleDTO.setYearN(strSelectedScheduleCode.getYearN());
		strSelectedMonth = strSelectedScheduleCode.getMonthCode();
		strSelectedType = strSelectedScheduleCode.getTrainingTypeCode();
		trainingScheduleDTO.setLocation(strSelectedScheduleCode.getLocation());
		trainingScheduleDTO.setNoofTrainees(strSelectedScheduleCode.getNoofTrainees());
		trainingScheduleDTO.setTrainingDate(strSelectedScheduleCode.getTrainingDate());
		trainingScheduleDTO.setScheduleCode(strSelectedScheduleCode.getScheduleCode());
		trainingScheduleDTO.setStaus(strSelectedScheduleCode.getStaus());
		
		disableUpdateMode = true;
		disableSaveMode = false;
	}
	
	public void update()
	{
		String ssDate=new SimpleDateFormat("HH:mm").format(trainingScheduleDTO.getStartTimeN());
		String eeDate=new SimpleDateFormat("HH:mm").format(trainingScheduleDTO.getEndTimeN());												
		trainingScheduleDTO.setStartTime(ssDate);
		trainingScheduleDTO.setEndTime(eeDate);
		
		trainingScheduleDTO.setTrainingTypeCode(strSelectedType);
		trainingScheduleDTO.setMonthCode(strSelectedMonth);
		//trainingScheduleDTO.setStaus("A");
		
		Boolean isUpdated = driverConductorTrainingService.updateTrainingSchedule(trainingScheduleDTO, loginUser);
		if (isUpdated) {
			driverConductorTrainingService.beanLinkMethod(trainingScheduleDTO, sessionBackingBean.getLoginUser(), "Edit Training Schedule", "Maintain Training Schedule");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
			clearAction();
			loadValues();
			disableUpdateMode = false;
			disableSaveMode = true;
		} else {
			setErrormsg("Data not Saved.");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
		}
		
	}

	public void clearAction() {
		disableUpdateMode = false;
		disableSaveMode = true;
		strSelectedType = null;
		strSelectedMonth = null;
		trainingScheduleDTO = new MaintainTrainingScheduleDTO();
		loadValues();
		strSelectedScheduleCode = new MaintainTrainingScheduleDTO();;
	}

	private boolean validateForm() {
		boolean ret = true;
		// Date today = new Date();

		if (trainingScheduleDTO.getYearN() != null && trainingScheduleDTO.getYearN() != ""
				&& !(trainingScheduleDTO.getYearN().equals(null)) && !trainingScheduleDTO.getYearN().isEmpty()) {
			if (strSelectedMonth != null && strSelectedMonth != "") {
				if (trainingScheduleDTO.getTrainingDate() != null) {
					if (trainingScheduleDTO.getStartTimeN() != null && !(trainingScheduleDTO.getStartTimeN().equals(null))) {
						if (trainingScheduleDTO.getEndTimeN() != null && !(trainingScheduleDTO.getEndTimeN().equals(null))) {
							if (strSelectedType != null && strSelectedType != "" ) {
								if (trainingScheduleDTO.getLocation() != null && trainingScheduleDTO.getLocation() != ""
										&& !(trainingScheduleDTO.getLocation().equals(null))
										&& !trainingScheduleDTO.getLocation().isEmpty()) {
									if (trainingScheduleDTO.getNoofTrainees() != null
											&& !(trainingScheduleDTO.getNoofTrainees().equals(null))) {
										return ret;
									} else {
										setErrormsg("No. of Trainees required.");
										RequestContext.getCurrentInstance().update("frmError");
										RequestContext.getCurrentInstance().execute("PF('Error').show()");
										return false;
									}
								} else {
									setErrormsg("Location required.");
									RequestContext.getCurrentInstance().update("frmError");
									RequestContext.getCurrentInstance().execute("PF('Error').show()");
									return false;
								}
							} else {
								setErrormsg("Type of Training required.");
								RequestContext.getCurrentInstance().update("frmError");
								RequestContext.getCurrentInstance().execute("PF('Error').show()");
								return false;
							}
						} else {
							setErrormsg("End Time required.");
							RequestContext.getCurrentInstance().update("frmError");
							RequestContext.getCurrentInstance().execute("PF('Error').show()");
							return false;
						}
					} else {
						setErrormsg("Start Time required.");
						RequestContext.getCurrentInstance().update("frmError");
						RequestContext.getCurrentInstance().execute("PF('Error').show()");
						return false;
					}
				} else {
					setErrormsg("Schedule Date required.");
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('Error').show()");
					return false;
				}
			} else {
				setErrormsg("Month required.");
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('Error').show()");
				return false;
			}
		} else {
			setErrormsg("Year required.");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('Error').show()");
			return false;
		}

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public DriverConductorTrainingService getDriverConductorTrainingService() {
		return driverConductorTrainingService;
	}

	public void setDriverConductorTrainingService(DriverConductorTrainingService driverConductorTrainingService) {
		this.driverConductorTrainingService = driverConductorTrainingService;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<CommonDTO> getTrainingTypeList() {
		return trainingTypeList;
	}

	public void setTrainingTypeList(List<CommonDTO> trainingTypeList) {
		this.trainingTypeList = trainingTypeList;
	}

	public List<CommonDTO> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<CommonDTO> monthList) {
		this.monthList = monthList;
	}

	public String getStrSelectedType() {
		return strSelectedType;
	}

	public void setStrSelectedType(String strSelectedType) {
		this.strSelectedType = strSelectedType;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public String getStrSelectedMonth() {
		return strSelectedMonth;
	}

	public void setStrSelectedMonth(String strSelectedMonth) {
		this.strSelectedMonth = strSelectedMonth;
	}

	public boolean isDisableUpdateMode() {
		return disableUpdateMode;
	}

	public void setDisableUpdateMode(boolean disableUpdateMode) {
		this.disableUpdateMode = disableUpdateMode;
	}

	public boolean isDisableSaveMode() {
		return disableSaveMode;
	}

	public void setDisableSaveMode(boolean disableSaveMode) {
		this.disableSaveMode = disableSaveMode;
	}

	public MaintainTrainingScheduleDTO getTrainingScheduleDTO() {
		return trainingScheduleDTO;
	}

	public void setTrainingScheduleDTO(MaintainTrainingScheduleDTO trainingScheduleDTO) {
		this.trainingScheduleDTO = trainingScheduleDTO;
	}

	public MaintainTrainingScheduleDTO getStrSelectedScheduleCode() {
		return strSelectedScheduleCode;
	}

	public void setStrSelectedScheduleCode(MaintainTrainingScheduleDTO strSelectedScheduleCode) {
		this.strSelectedScheduleCode = strSelectedScheduleCode;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public List<MaintainTrainingScheduleDTO> getScheduleList() {
		return scheduleList;
	}

	public void setScheduleList(List<MaintainTrainingScheduleDTO> scheduleList) {
		this.scheduleList = scheduleList;
	}

	public Date getTrainingDate() {
		return trainingDate;
	}

	public void setTrainingDate(Date trainingDate) {
		this.trainingDate = trainingDate;
	}

}
