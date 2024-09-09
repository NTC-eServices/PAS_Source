package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.dto.FlyingSquadSteupDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.FlyingSquadInvestigationLogService;
import lk.informatics.ntc.model.service.FlyingSquadInvestigationService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name ="flyingSquadInvestigationLogBackingBean")
@ViewScoped
public class FlyingSquadInvestigationLogBackingBean {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	public FlyingSquadInvestigationLogService flyingSquadInvestigationLogService;
	public ArrayList<FlyingSquadInvestiMasterDTO>refNoList;
	public ArrayList<FlyingManageInvestigationLogDTO>reportNoList;
	public List<FlyingManageInvestigationLogDTO> vehicleNoList;
	public FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO;
	public FlyingManageInvestigationLogDTO flyingManageInvestigationLogDetailDTO;
	public List<FlyingManageInvestigationLogDTO> routeList;
	private AdminService adminService;
	public List<FlyingManageInvestigationLogDTO> serviceTypeList;
	public ArrayList<FlyingManageInvestigationLogDTO>detailList;
	public FlyingManageInvestigationLogDTO selectedflyingManageInvestigationLogDTO;
	public FlyingManageInvestigationLogDTO deletededflyingManageInvestigationLogDTO;
   
	private String user;
	private String savemsg;
	private String no;
    private boolean saved;
    private int bustot;
    private boolean change;
    public boolean disableVal;
    public boolean renderVal;
    public boolean displayVal;
    private String strServiceCategoryGlobal;
    
	@PostConstruct
	public void init() {
		setFlyingSquadInvestigationLogService((FlyingSquadInvestigationLogService) SpringApplicationContex
				.getBean("flyingSquadInvestigationLogService"));
		setUser(sessionBackingBean.getLoginUser());	
		refNoList = flyingSquadInvestigationLogService.getrefNo();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		flyingManageInvestigationLogDetailDTO = new FlyingManageInvestigationLogDTO();
		vehicleNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
		vehicleNoList = flyingSquadInvestigationLogService.getVehicleDetail();
		routeList = flyingSquadInvestigationLogService.routeNodropdown();
		serviceTypeList = flyingSquadInvestigationLogService.getServiceTypeToDropdown();
		selectedflyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		deletededflyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		saved = true;
		bustot = 0;
		disableVal = false;
		renderVal=true;
		displayVal=false;
		flyingManageInvestigationLogDetailDTO.setServiceCategory("NT");
		strServiceCategoryGlobal="NT";
	}
	
	public boolean isRenderVal() {
		return renderVal;
	}

	public void setRenderVal(boolean renderVal) {
		this.renderVal = renderVal;
	}

	public void getreportNo() {
		reportNoList =flyingSquadInvestigationLogService.getreportNo(flyingManageInvestigationLogDTO.getRefNo());
		flyingManageInvestigationLogDTO = flyingSquadInvestigationLogService.getmasterData(flyingManageInvestigationLogDTO.getRefNo(), null);
	}

	public void search() {
		if(flyingManageInvestigationLogDTO.getRefNo()!=null&&!flyingManageInvestigationLogDTO.getRefNo().equalsIgnoreCase("")) {
			if(flyingManageInvestigationLogDTO.getReportNo()!=null &&!flyingManageInvestigationLogDTO.getReportNo().equalsIgnoreCase("")&&!flyingManageInvestigationLogDTO.getReportNo().isEmpty()) {
			  flyingManageInvestigationLogDTO = flyingSquadInvestigationLogService.getmasterData(flyingManageInvestigationLogDTO.getRefNo(), flyingManageInvestigationLogDTO.getReportNo());
			  detailList = flyingSquadInvestigationLogService.getdetails(flyingManageInvestigationLogDTO.getRefNo(),flyingManageInvestigationLogDTO.getReportNo());
			  bustot = detailList.size();
			  saved = false;
			  /**CR From Parmaith on 08/11/2022 ---> Show investigation date and time which entered in Initiate Flying Squad Investigation Function as Time Field and User should be able to Edit it**/
			  Date investigationDateTime = flyingSquadInvestigationLogService.getInvestigationDateAndTime(flyingManageInvestigationLogDTO.getRefNo());
			  flyingManageInvestigationLogDetailDTO.setCurrentTime(investigationDateTime);
			  
			}else {
		      //flyingManageInvestigationLogDTO = flyingSquadInvestigationLogService.getmasterData(flyingManageInvestigationLogDTO.getRefNo(), null);
		    	 sessionBackingBean.setMessage("Please Enter Report No. or Genarate Report No.");
				 RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		    }
		}
		else {
			  sessionBackingBean.setMessage("Please Enter Reference No.");
			  RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}
	}
	
	
	public void clear() {
		refNoList = flyingSquadInvestigationLogService.getrefNo();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		flyingManageInvestigationLogDetailDTO = new FlyingManageInvestigationLogDTO();
		vehicleNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
		vehicleNoList = flyingSquadInvestigationLogService.getVehicleDetail();
		routeList = flyingSquadInvestigationLogService.routeNodropdown();
		serviceTypeList = flyingSquadInvestigationLogService.getServiceTypeToDropdown();
		reportNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
		saved = true;
		disableVal = false;		
		renderVal=true;
		displayVal=false;
		flyingManageInvestigationLogDetailDTO.setServiceCategory("NT");
	}
	
	public void cancel() {
		refNoList = flyingSquadInvestigationLogService.getrefNo();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		flyingManageInvestigationLogDetailDTO = new FlyingManageInvestigationLogDTO();
		vehicleNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
		vehicleNoList = flyingSquadInvestigationLogService.getVehicleDetail();
		routeList = flyingSquadInvestigationLogService.routeNodropdown();
		serviceTypeList = flyingSquadInvestigationLogService.getServiceTypeToDropdown();
		detailList = new ArrayList<FlyingManageInvestigationLogDTO>();
		reportNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
		saved = true;
		bustot = 0;
		disableVal = false;
		renderVal=true;
		displayVal=false;
		flyingManageInvestigationLogDetailDTO.setServiceCategory("NT");
	}
	
	public void addDetails() {
		if(flyingManageInvestigationLogDTO.getRefNo()!=null && !flyingManageInvestigationLogDTO.getRefNo().equalsIgnoreCase("")) {
			if(flyingManageInvestigationLogDTO.getReportNo() == null || (flyingManageInvestigationLogDTO.getReportNo().isEmpty())){
			String reportno =null;
			reportno = flyingSquadInvestigationLogService.savemasterdata(flyingManageInvestigationLogDTO, user);
			reportNoList =flyingSquadInvestigationLogService.getreportNo(flyingManageInvestigationLogDTO.getRefNo());
			flyingManageInvestigationLogDTO.setReportNo(reportno);
		    flyingManageInvestigationLogDTO = flyingSquadInvestigationLogService.getmasterData(flyingManageInvestigationLogDTO.getRefNo(), flyingManageInvestigationLogDTO.getReportNo());
			saved = false;
			savemsg = "Successfully Saved!";
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
			disableVal = false;
			}
			else{
				  sessionBackingBean.setMessage("Already generated Report No. should not allow to save it again.");
				  RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}
		}else {
			 sessionBackingBean.setMessage("Please Enter Reference No.");
			  RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}
		
		
		
	}
	
	public void saveDetails() {
	if(flyingManageInvestigationLogDTO.getRefNo()!=null && !flyingManageInvestigationLogDTO.getRefNo().equalsIgnoreCase("")) {
	 if(flyingManageInvestigationLogDTO.getReportNo()!=null && !flyingManageInvestigationLogDTO.getReportNo().equalsIgnoreCase("")) {
	   if(flyingManageInvestigationLogDetailDTO.getBusNo()!=null &&!flyingManageInvestigationLogDetailDTO.getBusNo().equalsIgnoreCase("")) {
		  if(flyingManageInvestigationLogDetailDTO.getInvesNo()==null ||flyingManageInvestigationLogDetailDTO.getInvesNo().equalsIgnoreCase("")) {
			flyingSquadInvestigationLogService.savedetaildata(flyingManageInvestigationLogDetailDTO, user,flyingManageInvestigationLogDTO.getReportNo(),flyingManageInvestigationLogDTO.getRefNo());
			detailList = flyingSquadInvestigationLogService.getdetails(flyingManageInvestigationLogDTO.getRefNo(),flyingManageInvestigationLogDTO.getReportNo());
			bustot = detailList.size();
			flyingManageInvestigationLogDetailDTO = new FlyingManageInvestigationLogDTO();
			change=false;
			savemsg = "Successfully Saved!";
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
		}
		else {
			flyingSquadInvestigationLogService.updatedetaildata(flyingManageInvestigationLogDetailDTO, user,flyingManageInvestigationLogDTO.getReportNo(),flyingManageInvestigationLogDTO.getRefNo());
			detailList = flyingSquadInvestigationLogService.getdetails(flyingManageInvestigationLogDTO.getRefNo(),flyingManageInvestigationLogDTO.getReportNo());
			flyingManageInvestigationLogDetailDTO = new FlyingManageInvestigationLogDTO();
			savemsg = "Successfully Updated!";
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
		}
	    }else {
	    	  sessionBackingBean.setMessage("Please Enter Vehicle No.");
			  RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
	    }
	     
	    }else {
	    	   sessionBackingBean.setMessage("Please Save Master Data Before Adding Bus Details.");
			  RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
	    }
		}else {
			  sessionBackingBean.setMessage("Please Enter Reference No.");
			  RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}
	}
	
    public void cancelDetails() {
    	flyingManageInvestigationLogDetailDTO = new FlyingManageInvestigationLogDTO();
		flyingManageInvestigationLogDetailDTO.setServiceCategory("NT");
		vehicleNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
		vehicleNoList = flyingSquadInvestigationLogService.getVehicleDetail();
		disableVal = false;
		renderVal=true;
		displayVal=false;
    }
	public void getvehicledetails() {
		serviceTypeList = flyingSquadInvestigationLogService.getServiceTypeToDropdown();
		//flyingManageInvestigationLogDetailDTO = flyingSquadInvestigationLogService.getVehicle(flyingManageInvestigationLogDetailDTO.getBusNo());
		//String strServiceCategory = flyingManageInvestigationLogDetailDTO.getServiceCategory();strServiceCategoryGlobal
		flyingManageInvestigationLogDetailDTO = flyingSquadInvestigationLogService.getVehicle(flyingManageInvestigationLogDetailDTO.getBusNo(),strServiceCategoryGlobal);
		flyingManageInvestigationLogDetailDTO.setServiceCategory(strServiceCategoryGlobal);
		
		  /**CR From Parmaith on 08/11/2022 --->No need to show current date and time  Show investigation date and time which entered in Initiate Flying Squad Investigation Function as Time Field and User should be able to Edit it**/
		  Date investigationDateTime = flyingSquadInvestigationLogService.getInvestigationDateAndTime(flyingManageInvestigationLogDTO.getRefNo());
		  flyingManageInvestigationLogDetailDTO.setCurrentTime(investigationDateTime);
	}
	
	public void editDetails() {
		if(selectedflyingManageInvestigationLogDTO.getServiceCategory().equals("T") || selectedflyingManageInvestigationLogDTO.getServiceCategory().equals("O"))
		{
			renderVal=false;
			displayVal=true;
		}
		else
		{
			vehicleNoList = flyingSquadInvestigationLogService.getPermitNoByService(selectedflyingManageInvestigationLogDTO.getServiceCategory());
			renderVal=true;
			displayVal=false;
		}
		flyingManageInvestigationLogDetailDTO.setInvesNo(selectedflyingManageInvestigationLogDTO.getInvesNo());
		flyingManageInvestigationLogDetailDTO.setBusNo(selectedflyingManageInvestigationLogDTO.getBusNo());
		flyingManageInvestigationLogDetailDTO.setRouteNo(selectedflyingManageInvestigationLogDTO.getRouteNo());
		flyingManageInvestigationLogDetailDTO.setRouteFrom(selectedflyingManageInvestigationLogDTO.getRouteFrom());
		flyingManageInvestigationLogDetailDTO.setRouteTo(selectedflyingManageInvestigationLogDTO.getRouteTo());
		flyingManageInvestigationLogDetailDTO.setServiceTypeCd(selectedflyingManageInvestigationLogDTO.getServiceTypeCd());
		flyingManageInvestigationLogDetailDTO.setServiceTypeDes(selectedflyingManageInvestigationLogDTO.getServiceTypeDes());
		flyingManageInvestigationLogDetailDTO.setCurrentTime(selectedflyingManageInvestigationLogDTO.getCurrentTime());
		flyingManageInvestigationLogDetailDTO.setFault(selectedflyingManageInvestigationLogDTO.getFault());
		flyingManageInvestigationLogDetailDTO.setDocuments(selectedflyingManageInvestigationLogDTO.getDocuments());
		flyingManageInvestigationLogDetailDTO.setInvestigationPlace(selectedflyingManageInvestigationLogDTO.getInvestigationPlace());
		flyingManageInvestigationLogDetailDTO.setRemarks(selectedflyingManageInvestigationLogDTO.getRemarks());
		flyingManageInvestigationLogDetailDTO.setServiceTypeDes(selectedflyingManageInvestigationLogDTO.getServiceTypeDes());
		flyingManageInvestigationLogDetailDTO.setServiceCategory(selectedflyingManageInvestigationLogDTO.getServiceCategory());
		flyingManageInvestigationLogDetailDTO.setPermitNo(selectedflyingManageInvestigationLogDTO.getPermitNo());
		
		
	}
	
	public void delete() {
		no =selectedflyingManageInvestigationLogDTO.getInvesNo();
		deletededflyingManageInvestigationLogDTO =selectedflyingManageInvestigationLogDTO;
		deletededflyingManageInvestigationLogDTO.setRefNo(flyingManageInvestigationLogDTO.getRefNo());
		deletededflyingManageInvestigationLogDTO.setReportNo(flyingManageInvestigationLogDTO.getReportNo());
		RequestContext.getCurrentInstance().execute("PF('deleteconfirmation').show()");
		
		
	}
	
	public void deleteRecord() {
		
		flyingSquadInvestigationLogService.updateStatus(no,deletededflyingManageInvestigationLogDTO,user); 
		flyingSquadInvestigationLogService.delete(no);
		savemsg = "Successfully Deleted!";
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
		getdetails();
		bustot = detailList.size();
	}
	
	public void getdetails() {
		detailList = flyingSquadInvestigationLogService.getdetails(flyingManageInvestigationLogDTO.getRefNo(),flyingManageInvestigationLogDTO.getReportNo());
		RequestContext.getCurrentInstance().update("frmInvestigationLog");
	}
	public void save() {
		if(flyingManageInvestigationLogDTO.getReportNo()!=null && !flyingManageInvestigationLogDTO.getReportNo().equalsIgnoreCase("")) {
		flyingSquadInvestigationLogService.updateendDate(flyingManageInvestigationLogDTO.getReportNo(), user);
		savemsg = "Successfully Saved!";
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
		
		}else {
			 sessionBackingBean.setMessage("No Changes to Save");
			  RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}
	}
	
	public void changeroute() {
		String temp = null;
		temp = flyingManageInvestigationLogDetailDTO.getRouteFrom();
		flyingManageInvestigationLogDetailDTO.setRouteFrom(flyingManageInvestigationLogDetailDTO.getRouteTo());
		flyingManageInvestigationLogDetailDTO.setRouteTo(temp);
	}
	public void getroutedata() {
		//flyingManageInvestigationLogDetailDTO = flyingSquadInvestigationLogService.getroutedata(flyingManageInvestigationLogDetailDTO.getRouteNo());
	/***Issue occured due to flyingManageInvestigationLogDetailDTO initilize.  **/
		List<String> routeData = new ArrayList<>();
		routeData =flyingSquadInvestigationLogService.getRouteDataNew(flyingManageInvestigationLogDetailDTO.getRouteNo());
		flyingManageInvestigationLogDetailDTO.setRouteFrom(routeData.get(0));
		flyingManageInvestigationLogDetailDTO.setRouteTo(routeData.get(1));
	
	}
	
	public void onServiceCategoryChange() {
		vehicleNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
		
		clearDet();
		strServiceCategoryGlobal= flyingManageInvestigationLogDetailDTO.getServiceCategory();	
		if (flyingManageInvestigationLogDetailDTO.getServiceCategory().equals("O") || flyingManageInvestigationLogDetailDTO.getServiceCategory().equals("T")) {
			renderVal=false;
			displayVal=true;
			
		}
		else {
			vehicleNoList = flyingSquadInvestigationLogService.getPermitNoByService(flyingManageInvestigationLogDetailDTO.getServiceCategory());
			if(flyingManageInvestigationLogDetailDTO.getServiceCategory().equals("sisu") || flyingManageInvestigationLogDetailDTO.getServiceCategory().equals("nisi") || flyingManageInvestigationLogDetailDTO.getServiceCategory().equals("gami")){
				disableVal = true;
				
			}
			else
			{disableVal = false;}
			renderVal=true;
			displayVal=false;
		}
		 /**CR From Parmaith on 08/11/2022 --->No need to show current date and time  Show investigation date and time which entered in Initiate Flying Squad Investigation Function as Time Field and User should be able to Edit it**/
		  Date investigationDateTime = flyingSquadInvestigationLogService.getInvestigationDateAndTime(flyingManageInvestigationLogDTO.getRefNo());
		  flyingManageInvestigationLogDetailDTO.setCurrentTime(investigationDateTime);
	}
	
	private void clearDet()
	{
		flyingManageInvestigationLogDetailDTO.setBusNo(null);
		flyingManageInvestigationLogDetailDTO.setPermitNo(null);
		flyingManageInvestigationLogDetailDTO.setRouteNo(null);
		flyingManageInvestigationLogDetailDTO.setRouteFrom(null);
		flyingManageInvestigationLogDetailDTO.setRouteTo(null);
		flyingManageInvestigationLogDetailDTO.setServiceTypeCd(null);
		flyingManageInvestigationLogDetailDTO.setServiceTypeDes(null);
		flyingManageInvestigationLogDetailDTO.setCurrentTime(null);
		flyingManageInvestigationLogDetailDTO.setInvestigationPlace(null);
		flyingManageInvestigationLogDetailDTO.setRemarks(null);
		flyingManageInvestigationLogDetailDTO.setFault(null);
		flyingManageInvestigationLogDetailDTO.setDocuments(null);
		
	}
	
	
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public FlyingSquadInvestigationLogService getFlyingSquadInvestigationLogService() {
		return flyingSquadInvestigationLogService;
	}


	public void setFlyingSquadInvestigationLogService(
			FlyingSquadInvestigationLogService flyingSquadInvestigationLogService) {
		this.flyingSquadInvestigationLogService = flyingSquadInvestigationLogService;
	}
	
	public ArrayList<FlyingSquadInvestiMasterDTO> getRefNoList() {
		return refNoList;
	}


	public void setRefNoList(ArrayList<FlyingSquadInvestiMasterDTO> refNoList) {
		this.refNoList = refNoList;
	}
    
	public ArrayList<FlyingManageInvestigationLogDTO> getReportNoList() {
		return reportNoList;
	}


	public void setReportNoList(ArrayList<FlyingManageInvestigationLogDTO> reportNoList) {
		this.reportNoList = reportNoList;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public FlyingManageInvestigationLogDTO getFlyingManageInvestigationLogDTO() {
		return flyingManageInvestigationLogDTO;
	}

	public void setFlyingManageInvestigationLogDTO(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO) {
		this.flyingManageInvestigationLogDTO = flyingManageInvestigationLogDTO;
	}

	public String getSavemsg() {
		return savemsg;
	}

	public void setSavemsg(String savemsg) {
		this.savemsg = savemsg;
	}
	
	public FlyingManageInvestigationLogDTO getFlyingManageInvestigationLogDetailDTO() {
		return flyingManageInvestigationLogDetailDTO;
	}

	public void setFlyingManageInvestigationLogDetailDTO(
			FlyingManageInvestigationLogDTO flyingManageInvestigationLogDetailDTO) {
		this.flyingManageInvestigationLogDetailDTO = flyingManageInvestigationLogDetailDTO;
	}

	public List<FlyingManageInvestigationLogDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<FlyingManageInvestigationLogDTO> routeList) {
		this.routeList = routeList;
	}

	public FlyingManageInvestigationLogDTO routedetailDTO;
	public FlyingManageInvestigationLogDTO getRoutedetailDTO() {
		return routedetailDTO;
	}

	public void setRoutedetailDTO(FlyingManageInvestigationLogDTO routedetailDTO) {
		this.routedetailDTO = routedetailDTO;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	
	public List<FlyingManageInvestigationLogDTO> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<FlyingManageInvestigationLogDTO> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
	}
	
	public List<FlyingManageInvestigationLogDTO> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<FlyingManageInvestigationLogDTO> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}


	public ArrayList<FlyingManageInvestigationLogDTO> getDetailList() {
		return detailList;
	}

	public void setDetailList(ArrayList<FlyingManageInvestigationLogDTO> detailList) {
		this.detailList = detailList;
	}

	public FlyingManageInvestigationLogDTO getSelectedflyingManageInvestigationLogDTO() {
		return selectedflyingManageInvestigationLogDTO;
	}

	public void setSelectedflyingManageInvestigationLogDTO(
			FlyingManageInvestigationLogDTO selectedflyingManageInvestigationLogDTO) {
		this.selectedflyingManageInvestigationLogDTO = selectedflyingManageInvestigationLogDTO;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public int getBustot() {
		return bustot;
	}

	public void setBustot(int bustot) {
		this.bustot = bustot;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public FlyingManageInvestigationLogDTO getDeletededflyingManageInvestigationLogDTO() {
			return deletededflyingManageInvestigationLogDTO;
		}

	public void setDeletededflyingManageInvestigationLogDTO(
				FlyingManageInvestigationLogDTO deletededflyingManageInvestigationLogDTO) {
			this.deletededflyingManageInvestigationLogDTO = deletededflyingManageInvestigationLogDTO;
		}

	public boolean isDisableVal() {
		return disableVal;
	}

	public void setDisableVal(boolean disableVal) {
		this.disableVal = disableVal;
	}

	public boolean isDisplayVal() {
		return displayVal;
	}

	public void setDisplayVal(boolean displayVal) {
		this.displayVal = displayVal;
	}



	
}
