package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiDetailDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.service.FlyingSquadInvestigationService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name ="flyingSquadInvestigationBackingBean")
@ViewScoped
public class FlyingSquadInvestigationBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	

	private FlyingSquadInvestiDetailDTO flyingSquadInvestiDetailDTO;
	private FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO;
	private String user;
	private FlyingSquadInvestigationService flyingSquadInvestigationService;
	private ArrayList<FlyingSquadGroupsDTO> flyingSquadGroupsList;
	private ArrayList<FlyingSquadInvestiDetailDTO>flyingSquadTeamList;
	private ArrayList<FlyingSquadInvestiMasterDTO>refNoList;
	private String refNo;
	private String tempvehicleNo;
	private String savemsg;
	private String errormsg;
	private boolean disableSave;
	private String flyingRefNo;
	private boolean change;
	@PostConstruct
	public void init() {
		setFlyingSquadInvestigationService((FlyingSquadInvestigationService) SpringApplicationContex
				.getBean("flyingSquadInvestigationService"));
		setUser(sessionBackingBean.getLoginUser());	
		
		flyingSquadInvestiDetailDTO = new FlyingSquadInvestiDetailDTO();
		flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
	    flyingSquadGroupsList = new ArrayList<FlyingSquadGroupsDTO>();
		flyingSquadTeamList = new ArrayList<FlyingSquadInvestiDetailDTO>();
		
		/*Modified By: dinushi.r on 22-07-2020*/
		//refNoList = flyingSquadInvestigationService.getrefNo();
		refNoList = flyingSquadInvestigationService.getrefNoforInvestigation();
		/**/
		
	    setDisableSave(true);
	   /* flyingRefNo = null;
	    flyingRefNo = sessionBackingBean.getFlyrefNo();
	    
	    if(flyingRefNo!=null) {
	    	searchnew();
	    }*/
	}
	
	public void getgroups() {
		
		   java.util.Date utilDate = new java.util.Date(flyingSquadInvestiMasterDTO.getInvestigationDate().getTime());
		
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(utilDate); 
		    int month = cal.get(Calendar.MONTH); // 0 being January
		    int year = cal.get(Calendar.YEAR);
		    flyingSquadGroupsList = flyingSquadInvestigationService.getGroupcode(year, month);
		     
		    
		     
		    
	}
	
	public void getTeam() {
		
		flyingSquadTeamList = flyingSquadInvestigationService.groupDetail(flyingSquadInvestiMasterDTO.getGroupCd());
		
	}
	
	
	public void save() {
		String refNo = null;
		
		refNo = flyingSquadInvestiMasterDTO.getRefNo();
		if( refNo != null) {
			update();
		} else {
			if (flyingSquadInvestiMasterDTO.getInvestigationDate() == null) {
				errormsg = "Please select the Date.";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
			} else {
				if (flyingSquadInvestiMasterDTO.getGroupCd().isEmpty()
						|| flyingSquadInvestiMasterDTO.getGroupCd().equalsIgnoreCase("")
						|| flyingSquadInvestiMasterDTO.getGroupCd() == null) {
					errormsg = "Please Select a Group.";
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");

				} else {

					if (flyingSquadInvestiMasterDTO.getStartTime() == null) {
						errormsg = "Please Select StartTime.";
						RequestContext.getCurrentInstance().update("frmError");
						RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");

					} else {

						if (flyingSquadInvestiMasterDTO.getEndtime() == null) {
							errormsg = "Please Select End Date & Time.";
							RequestContext.getCurrentInstance().update("frmError");
							RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
						} else {
							if (flyingSquadInvestiMasterDTO.getVehicleNo().isEmpty()
									|| flyingSquadInvestiMasterDTO.getVehicleNo().equalsIgnoreCase("")
									|| flyingSquadInvestiMasterDTO.getVehicleNo() == null) {
								errormsg = "Please Enter Vehicle No.";
								RequestContext.getCurrentInstance().update("frmError");
								RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
							} else {
								if (flyingSquadInvestiMasterDTO.getDriverName().isEmpty()
										|| flyingSquadInvestiMasterDTO.getDriverName().equalsIgnoreCase("")
										|| flyingSquadInvestiMasterDTO.getDriverName() == null) {
									errormsg = "Please Enter Driver Name.";
									RequestContext.getCurrentInstance().update("frmError");
									RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
								} else {
									if (flyingSquadInvestiMasterDTO.getStartPlace().isEmpty()
											|| flyingSquadInvestiMasterDTO.getStartPlace().equalsIgnoreCase("")
											|| flyingSquadInvestiMasterDTO.getStartPlace() == null) {
										errormsg = "Please Enter Start Place.";
										RequestContext.getCurrentInstance().update("frmError");
										RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
									} else {
										if (flyingSquadInvestiMasterDTO.getInvestigationDetails().isEmpty()
												|| flyingSquadInvestiMasterDTO.getInvestigationDetails()
														.equalsIgnoreCase("")
												|| flyingSquadInvestiMasterDTO.getInvestigationDetails() == null) {
											errormsg = "Please Enter Details of Investigation Places, Routes,Services and Investigation Places.";
											RequestContext.getCurrentInstance().update("frmError");
											RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
										} else {
											if (flyingSquadInvestiMasterDTO.getEndPlace().isEmpty()
													|| flyingSquadInvestiMasterDTO.getEndPlace().equalsIgnoreCase("")
													|| flyingSquadInvestiMasterDTO.getEndPlace() == null) {
												errormsg = "Please Enter Place of Job End.";
												RequestContext.getCurrentInstance().update("frmError");
												RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
											} else {
												java.util.Date calDate = new java.util.Date(
														flyingSquadInvestiMasterDTO.getInvestigationDate().getTime());
												Calendar cal = Calendar.getInstance();
												cal.setTime(calDate);
												int year = cal.get(Calendar.YEAR);
												refNo = flyingSquadInvestigationService.getSeqNo(year);
												flyingSquadInvestiMasterDTO.setRefNo(refNo);
												flyingSquadInvestigationService.saveMasterDta(flyingSquadInvestiMasterDTO,
														user);
												boolean success = flyingSquadInvestigationService.saveDetailDta(flyingSquadTeamList, user,
														refNo);
												
												if (success) {
													setSavemsg("Successfully Saved!");
													setDisableSave(false);
													RequestContext.getCurrentInstance().update("frmsuccessSve");
													RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
												}else {
													errormsg = "Unsucessfull!.";
													RequestContext.getCurrentInstance().update("frmError");
													RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
												}
												

											}
										}
									}
								}
							}
						}
					}
				}
			}
			
		}
		
		
		
		

			
		}
		
		
	
	
	public void cancel() {
		flyingSquadInvestiDetailDTO = new FlyingSquadInvestiDetailDTO();
		flyingSquadInvestiMasterDTO = new FlyingSquadInvestiMasterDTO();
		flyingSquadGroupsList = new ArrayList<FlyingSquadGroupsDTO>();
		flyingSquadTeamList = new ArrayList<FlyingSquadInvestiDetailDTO>();
		setDisableSave(true);
	
	}
	
	public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
    }
	
	public void search(){
		String refno = flyingSquadInvestiMasterDTO.getRefNo();
		  if(flyingSquadInvestiMasterDTO.getRefNo().isEmpty()||flyingSquadInvestiMasterDTO.getRefNo().equalsIgnoreCase("")||flyingSquadInvestiMasterDTO.getRefNo()==null) {
			  errormsg = "Please  Enter Reference No.";
			  RequestContext.getCurrentInstance().update("frmError");
			  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
			  
		  }else {
			  
			  flyingSquadInvestiMasterDTO = flyingSquadInvestigationService.getmasterDetails(refno);
			  flyingSquadTeamList = flyingSquadInvestigationService.getmemberDetails(refno);
			  flyingSquadInvestiMasterDTO.setRefNo(refno);
			  RequestContext.getCurrentInstance().update("frmeditinv");
		
			  
		  }
		
	}
	
	public void searchnew(){
	
			  
			  flyingSquadInvestiMasterDTO = flyingSquadInvestigationService.getmasterDetails(flyingRefNo);
			  flyingSquadTeamList = flyingSquadInvestigationService.getmemberDetails(flyingRefNo);
			  flyingSquadInvestiMasterDTO.setRefNo(flyingRefNo);
			  RequestContext.getCurrentInstance().update("frmeditinvl");
		
		
		
	}
	
	public void update() {
		
		if(flyingSquadInvestiMasterDTO.getRefNo().isEmpty()||flyingSquadInvestiMasterDTO.getRefNo().equalsIgnoreCase("")||flyingSquadInvestiMasterDTO.getRefNo()==null) {
			   errormsg = "Please Select the Reference No.";
			  RequestContext.getCurrentInstance().update("frmError");
			  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
		}else {
			if(flyingSquadInvestiMasterDTO.getInvestigationDate()==null) {
				  errormsg = "Please Select the Investigation  Date.";
				  RequestContext.getCurrentInstance().update("frmError");
				  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
			  }else {
				  if(flyingSquadInvestiMasterDTO.getGroupCd().isEmpty()||flyingSquadInvestiMasterDTO.getGroupCd().equalsIgnoreCase("")||flyingSquadInvestiMasterDTO.getGroupCd()==null) {
					  errormsg = "Please Select a Group.";
					  RequestContext.getCurrentInstance().update("frmError");
					  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
					  
				  }else {
					  /*Modified by : dinushi.r on 22-07-2020*/
					  String validateRef = flyingSquadInvestigationService.validateRefNo(flyingSquadInvestiMasterDTO.getRefNo());
					  if(validateRef== null){
						  errormsg = "Cannot update data. End Date of Investigation is exceed.";
						  RequestContext.getCurrentInstance().update("frmError");
						  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
					  }
					  else{
						  if(flyingSquadInvestiMasterDTO.getStartTime()==null) {
							  errormsg = "Please Select StartTime.";
							  RequestContext.getCurrentInstance().update("frmError");
							  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
							  
						  }else {
							  
							  if(flyingSquadInvestiMasterDTO.getEndtime()==null) {
								  errormsg = "Please Select End Date & Time.";
								  RequestContext.getCurrentInstance().update("frmError");
								  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
							  }
							  else {
								  if(flyingSquadInvestiMasterDTO.getVehicleNo().isEmpty()||flyingSquadInvestiMasterDTO.getVehicleNo().equalsIgnoreCase("")||flyingSquadInvestiMasterDTO.getVehicleNo() ==null) {
									  errormsg = "Please Enter Vehicle No.";
									  RequestContext.getCurrentInstance().update("frmError");
									  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
								  }else {
									  if(flyingSquadInvestiMasterDTO.getDriverName().isEmpty()||flyingSquadInvestiMasterDTO.getDriverName().equalsIgnoreCase("")||flyingSquadInvestiMasterDTO.getDriverName() ==null) {
										  errormsg = "Please Enter Driver Name.";
										  RequestContext.getCurrentInstance().update("frmError");
										  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
									  }
									  else {
										  if(flyingSquadInvestiMasterDTO.getStartPlace().isEmpty()||flyingSquadInvestiMasterDTO.getStartPlace().equalsIgnoreCase("")||flyingSquadInvestiMasterDTO.getStartPlace() ==null) {
											  errormsg = "Please Enter Start Place.";
											  RequestContext.getCurrentInstance().update("frmError");
											  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
										  }
										  else {
											  if(flyingSquadInvestiMasterDTO.getInvestigationDetails().isEmpty()||flyingSquadInvestiMasterDTO.getInvestigationDetails().equalsIgnoreCase("")||flyingSquadInvestiMasterDTO.getInvestigationDetails() ==null) {
												  errormsg = "Please Enter Details of Investigation Places, Routes,Services and Investigation Places.";
												  RequestContext.getCurrentInstance().update("frmError");
												  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
											  }else {
												  if(flyingSquadInvestiMasterDTO.getEndPlace().isEmpty()||flyingSquadInvestiMasterDTO.getEndPlace().equalsIgnoreCase("")||flyingSquadInvestiMasterDTO.getEndPlace()==null) {
													  errormsg = "Please Enter Place of Job End.";
													  RequestContext.getCurrentInstance().update("frmError");
													  RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
												  }else {
														flyingSquadInvestigationService.updatemasterData(flyingSquadInvestiMasterDTO, user);
														flyingSquadInvestigationService.updatedetail(flyingSquadTeamList, user, flyingSquadInvestiMasterDTO.getRefNo());
														RequestContext.getCurrentInstance().update("frmeditinv");
														setSavemsg("Successfully Updated!");
													    RequestContext.getCurrentInstance().update("frmsuccessSve");
													    RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
												  }
												  }
												  
												
											  }
											   
										  }
										   
									  }
									  
									
								  }
								   
							  }
							  
							  
						  }
					  }
					  /**/
				  }
		}
  
		
			  	  
 }	
	
	public void refNo() {
		/*Modified By: dinushi.r on 22-07-2020*/
		//refNoList = flyingSquadInvestigationService.getrefNo();
		refNoList = flyingSquadInvestigationService.getrefNoforInvestigation();
		/**/
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public FlyingSquadInvestiDetailDTO getFlyingSquadInvestiDetailDTO() {
		return flyingSquadInvestiDetailDTO;
	}

	public void setFlyingSquadInvestiDetailDTO(FlyingSquadInvestiDetailDTO flyingSquadInvestiDetailDTO) {
		this.flyingSquadInvestiDetailDTO = flyingSquadInvestiDetailDTO;
	}

	public FlyingSquadInvestiMasterDTO getFlyingSquadInvestiMasterDTO() {
		return flyingSquadInvestiMasterDTO;
	}

	public void setFlyingSquadInvestiMasterDTO(FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO) {
		this.flyingSquadInvestiMasterDTO = flyingSquadInvestiMasterDTO;
	}

	public FlyingSquadInvestigationService getFlyingSquadInvestigationService() {
		return flyingSquadInvestigationService;
	}

	public void setFlyingSquadInvestigationService(FlyingSquadInvestigationService flyingSquadInvestigationService) {
		this.flyingSquadInvestigationService = flyingSquadInvestigationService;
	}
	
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public ArrayList<FlyingSquadGroupsDTO> getFlyingSquadGroupsList() {
		return flyingSquadGroupsList;
	}

	public void setFlyingSquadGroupsList(ArrayList<FlyingSquadGroupsDTO> flyingSquadGroupsList) {
		flyingSquadGroupsList = flyingSquadGroupsList;
	}

	public ArrayList<FlyingSquadInvestiDetailDTO> getFlyingSquadTeamList() {
		return flyingSquadTeamList;
	}

	public void setFlyingSquadTeamList(ArrayList<FlyingSquadInvestiDetailDTO> flyingSquadTeamList) {
		this.flyingSquadTeamList = flyingSquadTeamList;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getSavemsg() {
		return savemsg;
	}

	public void setSavemsg(String savemsg) {
		this.savemsg = savemsg;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public boolean isDisableSave() {
		return disableSave;
	}

	public void setDisableSave(boolean disableSave) {
		this.disableSave = disableSave;
	}

	public ArrayList<FlyingSquadInvestiMasterDTO> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(ArrayList<FlyingSquadInvestiMasterDTO> refNoList) {
		this.refNoList = refNoList;
	}

	public String getFlyingRefNo() {
		return flyingRefNo;
	}

	public void setFlyingRefNo(String flyingRefNo) {
		this.flyingRefNo = flyingRefNo;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

}
