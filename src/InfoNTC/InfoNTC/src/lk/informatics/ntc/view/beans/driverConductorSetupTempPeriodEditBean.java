package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "driverConductorSetupTempPeriodEditBean")
@ViewScoped

public class driverConductorSetupTempPeriodEditBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
	private CommonService commonService;
	private DriverConductorTrainingService driverConductorTrainingService;
	private AdminService adminService;

	private DriverConductorRegistrationDTO driverconductorDto, updateDTO;
	private DriverConductorRegistrationDTO saveData;
	private List<DriverConductorRegistrationDTO> showSearchData,updatedList;
	private List<CommonDTO> trainingTypeList;
	private List<CommonDTO> idNoList;
	private List<CommonDTO> driverConList;
	private String successMessage, errorMessage, alertMSG, loginUser, strSelectedType, strSelectedNic, strSelectedID, strName;
	private boolean disableMode,disableNic,disableDCId,renderUpdateButton,renderSaveButton;
	
	private DriverConductorRegistrationDTO selectDTO;
	
	@PostConstruct
	public void init() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		loginUser = sessionBackingBean.getLoginUser();

		setDriverconductorDto(new DriverConductorRegistrationDTO());
		trainingTypeList = driverConductorTrainingService.GetAllTrainingTypesWithoutDuplicate();
		idNoList = driverConductorTrainingService.getTempANICList();
		driverConList = driverConductorTrainingService.getPendingDCIDList();
		selectDTO = new DriverConductorRegistrationDTO();
		showSearchData = driverConductorTrainingService.getPaymentCompletedDC();
		disableMode=false;
		disableNic= true;
		disableDCId=true;
		successMessage = null;
		errorMessage=null;
		alertMSG=null;
		renderUpdateButton=false;
		renderSaveButton=true;
	}
	public void onTrainingTypeChange()
	{		
		if(strSelectedType != "")
		{
			disableNic= false;
			idNoList = driverConductorTrainingService.getTempANICListByTrainingType(strSelectedType);
			driverConList = driverConductorTrainingService.getPendingDCIDListByTrainingType(strSelectedType);	
			strName=null;
		}
	}
	public void onIDNoChange()
	{
		
			if(strSelectedNic != "")
			{
				disableDCId=false;
				driverConList = driverConductorTrainingService.getTempDCIDListByTrainingandID(strSelectedType, strSelectedNic);
				strName = driverConductorTrainingService.getNamebyNICOrDCId(strSelectedNic, strSelectedID);
			}
			else
			{
				setErrorMessage("ID Number required.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		
		
	}
	public void onDCIDNoChange()
	{
		setDriverconductorDto(new DriverConductorRegistrationDTO());
		strName = driverConductorTrainingService.getNamebyNICOrDCId(strSelectedNic, strSelectedID);
	}
	public void clear()
	{
		driverconductorDto = new DriverConductorRegistrationDTO();
		trainingTypeList = driverConductorTrainingService.GetAllTrainingTypesWithoutDuplicate();
		idNoList = driverConductorTrainingService.getTempANICList();
		driverConList = driverConductorTrainingService.getPendingDCIDList();
		strSelectedID=null;
		strSelectedNic=null;
		strSelectedType=null;
		disableNic= true;
		disableDCId=true;
		strName=null;
		showSearchData = new ArrayList<>();
		showSearchData = driverConductorTrainingService.getPaymentCompletedDC();
		selectDTO = new DriverConductorRegistrationDTO();
		renderUpdateButton=false;
		renderSaveButton=true;
	}
	public void searchData()
	{
		if(strSelectedNic!=null && !strSelectedNic.trim().isEmpty()) {
			showSearchData = new ArrayList<>();
			showSearchData=driverConductorTrainingService.getsearchDataByEnterdVal(strSelectedType,strSelectedNic,strSelectedID,strName);
			renderUpdateButton=true;
			renderSaveButton=false;
		}
		else {
			
			setErrorMessage("Please select mandatory field.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("formOne");
		}
	}
	public void clearGrid()
	{
		clear();
		showSearchData = new ArrayList<>();
		showSearchData = driverConductorTrainingService.getPaymentCompletedDC();
		selectDTO = new DriverConductorRegistrationDTO();
	}
	public void saveTempPeriodData() {

		if (!showSearchData.isEmpty()) {

			if (dataCheckingMethod(showSearchData) == 0) {

				setErrorMessage("No Value Found To Update");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				RequestContext.getCurrentInstance().update("formOne");

			} else if (dataCheckingMethod(showSearchData) == 1) {

				setErrorMessage("All Selected Row's Data Need To Be Entered");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				RequestContext.getCurrentInstance().update("formOne");

			} else if (dataCheckingMethod(showSearchData) == 2) {

				RequestContext.getCurrentInstance().execute("PF('comfirmMSG').show()");

			} else if (dataCheckingMethod(showSearchData) == 3) {

				setErrorMessage("To Date should be greater than From Date");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				RequestContext.getCurrentInstance().update("formOne");
			}
			
			for(DriverConductorRegistrationDTO data : showSearchData) {
				saveData.setAppNo(data.getAppNo());
				saveData.setNic(data.getNic());
				saveData.setDriverConductorId(data.getDriverConductorId());
				
				driverConductorTrainingService.beanLinkMethod(saveData, sessionBackingBean.getLoginUser(), "Save Temp Period Data", "Setup Temporary Period Edit/View - Driver/Conductor");
			}

		} else {
			setErrorMessage("No Data Found For Save");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("formOne");
		}

	}
	public int dataCheckingMethod(List<DriverConductorRegistrationDTO> myList) {

		int dataEmpty = 0;
		updatedList = new ArrayList<>();

		for (DriverConductorRegistrationDTO dto : myList) {

			if (dto.getLogRefCheck() == true) {

				if (dto.getFromDate() != null && dto.getToDate() != null) {
					if (!(dto.getToDate().before(dto.getFromDate()))){
					updateDTO = new DriverConductorRegistrationDTO(dto.getFromDate(), dto.getToDate(), dto.getAppNo(),dto.getDriverConductorId(), dto.getNic());
					updatedList.add(updateDTO);

					dataEmpty = 2;
					
					}
					else
					{
						dataEmpty = 3;
					}

				} else {
					return dataEmpty = 1;
				}

			}

		}

		return dataEmpty;
	}
	public void saveDate() {

		RequestContext.getCurrentInstance().execute("PF('comfirmMSG').hide()");

		if (driverConductorTrainingService.InsertDCTempPeriod(selectDTO, sessionBackingBean.getLoginUser(), this.updatedList)) {

			setSuccessMessage("Successfully Updated.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			RequestContext.getCurrentInstance().update("frmsuccess");
			clear();

		} else {
			setErrorMessage("Update Fail");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("formOne");
		}
	}
	public void updateDates() {
		if(selectDTO!=null) {
		if (!(selectDTO.getToDate().before(selectDTO.getFromDate()))){
		if (driverConductorTrainingService.updateDCTempPeriod(selectDTO, sessionBackingBean.getLoginUser())) {
			selectDTO.setNic(strSelectedNic);
			selectDTO.setDriverConductorId(strSelectedID);
			driverConductorTrainingService.beanLinkMethod(selectDTO, sessionBackingBean.getLoginUser(), "Update Temp Period", "Setup Temporary Period Edit/View - Driver/Conductor");
			setSuccessMessage("Successfully Updated.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			RequestContext.getCurrentInstance().update("frmsuccess");
			//clear();

		} else {
			setErrorMessage("Update Fail");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("formOne");
		}
		}
		else {
			setErrorMessage("Please enter valid from date and to date.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("formOne");
		}
		}
		else {
			setErrorMessage("Please select a data row.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("formOne");
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
	public List<CommonDTO> getIdNoList() {
		return idNoList;
	}
	public void setIdNoList(List<CommonDTO> idNoList) {
		this.idNoList = idNoList;
	}
	public List<CommonDTO> getDriverConList() {
		return driverConList;
	}
	public void setDriverConList(List<CommonDTO> driverConList) {
		this.driverConList = driverConList;
	}
	public String getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}
	public String getStrSelectedType() {
		return strSelectedType;
	}
	public void setStrSelectedType(String strSelectedType) {
		this.strSelectedType = strSelectedType;
	}
	public String getStrSelectedNic() {
		return strSelectedNic;
	}
	public void setStrSelectedNic(String strSelectedNic) {
		this.strSelectedNic = strSelectedNic;
	}
	public String getStrSelectedID() {
		return strSelectedID;
	}
	public void setStrSelectedID(String strSelectedID) {
		this.strSelectedID = strSelectedID;
	}
	public boolean isDisableMode() {
		return disableMode;
	}
	public void setDisableMode(boolean disableMode) {
		this.disableMode = disableMode;
	}
	public boolean isDisableNic() {
		return disableNic;
	}
	public void setDisableNic(boolean disableNic) {
		this.disableNic = disableNic;
	}
	public boolean isDisableDCId() {
		return disableDCId;
	}
	public void setDisableDCId(boolean disableDCId) {
		this.disableDCId = disableDCId;
	}
	public DriverConductorRegistrationDTO getDriverconductorDto() {
		return driverconductorDto;
	}
	public void setDriverconductorDto(DriverConductorRegistrationDTO driverconductorDto) {
		this.driverconductorDto = driverconductorDto;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}

	public DriverConductorRegistrationDTO getSelectDTO() {
		return selectDTO;
	}
	public void setSelectDTO(DriverConductorRegistrationDTO selectDTO) {
		this.selectDTO = selectDTO;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getAlertMSG() {
		return alertMSG;
	}
	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}
	public List<DriverConductorRegistrationDTO> getShowSearchData() {
		return showSearchData;
	}
	public void setShowSearchData(List<DriverConductorRegistrationDTO> showSearchData) {
		this.showSearchData = showSearchData;
	}
	public boolean isRenderUpdateButton() {
		return renderUpdateButton;
	}
	public void setRenderUpdateButton(boolean renderUpdateButton) {
		this.renderUpdateButton = renderUpdateButton;
	}
	
	
	public boolean isRenderSaveButton() {
		return renderSaveButton;
	}
	public void setRenderSaveButton(boolean renderSaveButton) {
		this.renderSaveButton = renderSaveButton;
	}
	public DriverConductorRegistrationDTO getSaveData() {
		return saveData;
	}
	public void setSaveData(DriverConductorRegistrationDTO saveData) {
		this.saveData = saveData;
	}
	
	
}
