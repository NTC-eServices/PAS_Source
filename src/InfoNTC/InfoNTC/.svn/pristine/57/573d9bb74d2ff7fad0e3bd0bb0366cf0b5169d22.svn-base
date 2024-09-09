package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import lk.informatics.ntc.model.dto.FluingSquadVioConditionDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadDocumentDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.dto.FlyingSquadVioDocumentsDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.FlyingSquadChargeSheetService;
import lk.informatics.ntc.model.service.FlyingSquadInvestigationLogService;
import lk.informatics.ntc.model.service.FlyingSquadRelaseDocumentService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "flyingSquadReleaseDocumentBackingBean")
@ViewScoped
public class FlyingSquadReleaseDocumentsBackingBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	@ManagedProperty(value = "#{flyingSquadChargeSheetBackingBean}")
	private FlyingSquadChargeSheetBackingBean flyingSquadChargeSheetBackingBean;
	private FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO;
	public FlyingSquadDocumentDTO selectedflyingManageInvestigationLogDTO;
	private ArrayList<FlyingSquadVioDocumentsDTO>documentList;
	private String user;
	private String savemsg;
	private String errormsg;
	public ArrayList<FlyingSquadInvestiMasterDTO>refNoList;
	public ArrayList<FlyingManageInvestigationLogDTO>reportNoList;
	public ArrayList<FlyingManageInvestigationLogDTO>invesNoList;
    public FlyingSquadRelaseDocumentService flyingSquadRelaseDocumentService;
    private ArrayList<FlyingSquadDocumentDTO>dataList;
    private FlyingSquadDocumentDTO flyingSquadDocumentDTO;
    private boolean viewMode;
	private CommonService commonService;
	private boolean disabledSaved;
	private boolean createMode;
	private int releasecount;
	
	

	@PostConstruct
	public void init() {
		setFlyingSquadRelaseDocumentService((FlyingSquadRelaseDocumentService) SpringApplicationContex.getBean("flyingSquadRelaseDocumentService"));
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN364", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN364", "V");
		refNoList = flyingSquadRelaseDocumentService.getrefNo(null,null,null,null);
		reportNoList = flyingSquadRelaseDocumentService.getreportNo(null,null,null);
		invesNoList =flyingSquadRelaseDocumentService.getinvesnolist(null,null,null,null);
		selectedflyingManageInvestigationLogDTO = new FlyingSquadDocumentDTO();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		flyingSquadDocumentDTO = new FlyingSquadDocumentDTO();
		user = sessionBackingBean.getLoginUser();
		releasecount = 0;
	
		if(viewMode==true) {
			disabledSaved = true;
		}else {
			disabledSaved = false;
		}
		
		if(createMode==true) {
			disabledSaved = false;
		}else {
			disabledSaved = true;
		}
	}
	
	public void getreportNoList() {
		Date startDate = flyingManageInvestigationLogDTO.getStartDate();
	    Date endDate = flyingManageInvestigationLogDTO.getEndDate();
		String refNo=flyingManageInvestigationLogDTO.getRefNo();
		reportNoList = flyingSquadRelaseDocumentService.getreportNo(flyingManageInvestigationLogDTO.getRefNo(),startDate,endDate);
		invesNoList =flyingSquadRelaseDocumentService.getinvesnolist(flyingManageInvestigationLogDTO.getRefNo(),null,startDate,endDate);
		documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		dataList = new ArrayList<FlyingSquadDocumentDTO>();
		selectedflyingManageInvestigationLogDTO = new FlyingSquadDocumentDTO();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		flyingSquadDocumentDTO = new FlyingSquadDocumentDTO();
		flyingManageInvestigationLogDTO.setRefNo(refNo);
		flyingManageInvestigationLogDTO.setStartDate(startDate);
		flyingManageInvestigationLogDTO.setEndDate(endDate);
	}


	public void invesNoList() {
		Date startDate = flyingManageInvestigationLogDTO.getStartDate();
		Date endDate = flyingManageInvestigationLogDTO.getEndDate();
		String repNo = flyingManageInvestigationLogDTO.getReportNo();
		invesNoList =flyingSquadRelaseDocumentService.getinvesnolist(null, flyingManageInvestigationLogDTO.getReportNo(),startDate,endDate);
		documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		dataList = new ArrayList<FlyingSquadDocumentDTO>();
		selectedflyingManageInvestigationLogDTO = new FlyingSquadDocumentDTO();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		flyingSquadDocumentDTO = new FlyingSquadDocumentDTO();
		refNoList = flyingSquadRelaseDocumentService.getrefNo(repNo,null,startDate,endDate);
		reportNoList = flyingSquadRelaseDocumentService.getreportNo(null,startDate,endDate);
		flyingManageInvestigationLogDTO = flyingSquadRelaseDocumentService.getrefNoNew(repNo, null);
		flyingManageInvestigationLogDTO.setStartDate(startDate);
		flyingManageInvestigationLogDTO.setEndDate(endDate);
	}
	
	public void getinvesdetail() {
		Date startDate = flyingManageInvestigationLogDTO.getStartDate();
		Date endDate = flyingManageInvestigationLogDTO.getEndDate();
		String invesNo = flyingManageInvestigationLogDTO.getInvesNo();
		documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		dataList = new ArrayList<FlyingSquadDocumentDTO>();
		selectedflyingManageInvestigationLogDTO = new FlyingSquadDocumentDTO();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		flyingSquadDocumentDTO = new FlyingSquadDocumentDTO();
		flyingManageInvestigationLogDTO =flyingSquadRelaseDocumentService.getmasterDetails(invesNo); 
		FlyingManageInvestigationLogDTO  test = new FlyingManageInvestigationLogDTO(); 
		test =flyingSquadRelaseDocumentService.getrefNoNew(null, invesNo);
		flyingManageInvestigationLogDTO.setRefNo(test.getRefNo());
		flyingManageInvestigationLogDTO.setReportNo(test.getReportNo());
		flyingManageInvestigationLogDTO.setInvesNo(invesNo);
		refNoList = flyingSquadRelaseDocumentService.getrefNo(null,invesNo,startDate,endDate);
		reportNoList = flyingSquadRelaseDocumentService.getreportNo(test.getRefNo(),startDate,endDate);
		flyingManageInvestigationLogDTO.setStartDate(startDate);
		flyingManageInvestigationLogDTO.setEndDate(endDate);
	
	}
	
	
	public void search() {
		 if(flyingManageInvestigationLogDTO.getStartDate()== null && flyingManageInvestigationLogDTO.getEndDate()== null && (flyingManageInvestigationLogDTO.getRefNo()==null||
				 flyingManageInvestigationLogDTO.getRefNo().equalsIgnoreCase(""))&& (flyingManageInvestigationLogDTO.getReportNo()==null||
				 flyingManageInvestigationLogDTO.getReportNo().equalsIgnoreCase("")) && (flyingManageInvestigationLogDTO.getInvesNo()==null||
				 flyingManageInvestigationLogDTO.getInvesNo().equalsIgnoreCase("")) ) {
			 
			     sessionBackingBean.setMessage("Please Enter At Least One of Search Criteria.");
			     RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			 
		 }else {
			
			 if(flyingManageInvestigationLogDTO.getStartDate()!= null && flyingManageInvestigationLogDTO.getEndDate()== null) {
				  sessionBackingBean.setMessage("Please Enter End Date.");
				  RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				 
			 }else {
				 if(flyingManageInvestigationLogDTO.getStartDate()== null && flyingManageInvestigationLogDTO.getEndDate()!= null) {
					  sessionBackingBean.setMessage("Please Enter Start Date.");
					  RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					 
				 }else {
					 dataList =flyingSquadRelaseDocumentService.getmasterData(flyingManageInvestigationLogDTO.getStartDate(), flyingManageInvestigationLogDTO.getEndDate(), flyingManageInvestigationLogDTO.getRefNo(), flyingManageInvestigationLogDTO.getReportNo(), flyingManageInvestigationLogDTO.getInvesNo());  
					 if(dataList.size()==0)
					 {
						 sessionBackingBean.setMessage("No data found for the search criteria.");
					     RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					     clear();
					 }
				 }
			 }
			 
			 
			 
		 }
	}
	
	public void getdocumentList() {
		documentList = flyingSquadRelaseDocumentService.getdocumentlist(selectedflyingManageInvestigationLogDTO.getInvesNo());
		for(FlyingSquadVioDocumentsDTO flyingSquadVioDocumentsDTO:documentList) {
			if(flyingSquadVioDocumentsDTO.isIsreleased()==true) {
				releasecount++;
			}
		}
		
	}
	
	
	public void releasedData() {
		int count=0;
		for(FlyingSquadVioDocumentsDTO flyingSquadVioDocumentsDTO:documentList) {
			if(flyingSquadVioDocumentsDTO.isIsreleased()==true) {
				count++;
			}
		}
		
		if(releasecount==count) {
			 sessionBackingBean.setMessage("No Changes to Save.");
			 RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}else if(documentList.size()==0) {
			 sessionBackingBean.setMessage("Please Enter Master Data.");
			 RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}
		else {
			flyingSquadRelaseDocumentService.updateData(documentList, user, selectedflyingManageInvestigationLogDTO.getInvesNo());
			documentList = flyingSquadRelaseDocumentService.getdocumentlist(selectedflyingManageInvestigationLogDTO.getInvesNo());
			for(FlyingSquadVioDocumentsDTO flyingSquadVioDocumentsDTO:documentList) {
				if(flyingSquadVioDocumentsDTO.isIsreleased()==true) {
					releasecount++;
				}
			}
			savemsg = "Successfully Saved!";
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
		}
	
	}
	
	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
	}
	

	public void clear() {
		refNoList = flyingSquadRelaseDocumentService.getrefNo(null,null,null,null);
		selectedflyingManageInvestigationLogDTO = new FlyingSquadDocumentDTO();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		flyingSquadDocumentDTO = new FlyingSquadDocumentDTO();
		reportNoList = flyingSquadRelaseDocumentService.getreportNo(null,null,null);
		invesNoList =flyingSquadRelaseDocumentService.getinvesnolist(null,null,null,null);
		documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		dataList = new ArrayList<FlyingSquadDocumentDTO>();
		
	}
	
	public void cancel() {
		refNoList = flyingSquadRelaseDocumentService.getrefNo(null,null,null,null);
		selectedflyingManageInvestigationLogDTO = new FlyingSquadDocumentDTO();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		flyingSquadDocumentDTO = new FlyingSquadDocumentDTO();
		reportNoList = flyingSquadRelaseDocumentService.getreportNo(null,null,null);
		invesNoList =flyingSquadRelaseDocumentService.getinvesnolist(null,null,null,null);
        dataList = new ArrayList<FlyingSquadDocumentDTO>();
        sessionBackingBean.setInvesNo(null);
        documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		
	}
	
	
	  public void viewAction(){
	    	//return "ViewInitiateFlyingSquadInvestigation.xhtml?faces-redirect=true";
		     sessionBackingBean.setInvesNo(selectedflyingManageInvestigationLogDTO.getInvesNo());
		     FacesContext facesContext = FacesContext.getCurrentInstance();
		     FlyingSquadChargeSheetBackingBeann flyingSquadChargeSheetBackingBeann =facesContext.getApplication().evaluateExpressionGet(facesContext, "#{flyingSquadChargeSheetBackingBeann}", FlyingSquadChargeSheetBackingBeann.class);
		     flyingSquadChargeSheetBackingBeann.init();
	    	 RequestContext.getCurrentInstance().update("frmview");
			 RequestContext.getCurrentInstance().execute("PF('showPageDialog').show()");
	    }
	  
	  public void changeDate() {
		  
		    Date startDate = flyingManageInvestigationLogDTO.getStartDate();
		    Date endDate = flyingManageInvestigationLogDTO.getEndDate();
		    selectedflyingManageInvestigationLogDTO = new FlyingSquadDocumentDTO();
			flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
			flyingSquadDocumentDTO = new FlyingSquadDocumentDTO();
			refNoList = flyingSquadRelaseDocumentService.getrefNo(null,null,startDate,endDate);
			reportNoList = flyingSquadRelaseDocumentService.getreportNo(null,startDate,endDate);
			invesNoList =flyingSquadRelaseDocumentService.getinvesnolist(null, null,startDate,endDate);
			documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
			dataList = new ArrayList<FlyingSquadDocumentDTO>();
			flyingManageInvestigationLogDTO.setStartDate(startDate);
			flyingManageInvestigationLogDTO.setEndDate(endDate);
	  }
	  
	public void close() {
		  sessionBackingBean.setInvesNo("");
		  RequestContext.getCurrentInstance().update("frmview");
		  RequestContext.getCurrentInstance().execute("PF('showPageDialog').hide()");
	}
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}


	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}


	public FlyingManageInvestigationLogDTO getFlyingManageInvestigationLogDTO() {
		return flyingManageInvestigationLogDTO;
	}


	public void setFlyingManageInvestigationLogDTO(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO) {
		this.flyingManageInvestigationLogDTO = flyingManageInvestigationLogDTO;
	}


	public ArrayList<FlyingSquadVioDocumentsDTO> getDocumentList() {
		return documentList;
	}


	public void setDocumentList(ArrayList<FlyingSquadVioDocumentsDTO> documentList) {
		this.documentList = documentList;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
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


	public ArrayList<FlyingManageInvestigationLogDTO> getInvesNoList() {
		return invesNoList;
	}


	public void setInvesNoList(ArrayList<FlyingManageInvestigationLogDTO> invesNoList) {
		this.invesNoList = invesNoList;
	}
	
	public FlyingSquadRelaseDocumentService getFlyingSquadRelaseDocumentService() {
		return flyingSquadRelaseDocumentService;
	}


	public void setFlyingSquadRelaseDocumentService(FlyingSquadRelaseDocumentService flyingSquadRelaseDocumentService) {
		this.flyingSquadRelaseDocumentService = flyingSquadRelaseDocumentService;
	}

	public ArrayList<FlyingSquadDocumentDTO> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<FlyingSquadDocumentDTO> dataList) {
		this.dataList = dataList;
	}

	
	
	public FlyingSquadDocumentDTO getFlyingSquadDocumentDTO() {
		return flyingSquadDocumentDTO;
	}

	public void setFlyingSquadDocumentDTO(FlyingSquadDocumentDTO flyingSquadDocumentDTO) {
		this.flyingSquadDocumentDTO = flyingSquadDocumentDTO;
	}

	public FlyingSquadDocumentDTO getSelectedflyingManageInvestigationLogDTO() {
		return selectedflyingManageInvestigationLogDTO;
	}

	public void setSelectedflyingManageInvestigationLogDTO(FlyingSquadDocumentDTO selectedflyingManageInvestigationLogDTO) {
		this.selectedflyingManageInvestigationLogDTO = selectedflyingManageInvestigationLogDTO;
	}

	public FlyingSquadChargeSheetBackingBean getFlyingSquadChargeSheetBackingBean() {
		return flyingSquadChargeSheetBackingBean;
	}

	public void setFlyingSquadChargeSheetBackingBean(FlyingSquadChargeSheetBackingBean flyingSquadChargeSheetBackingBean) {
		this.flyingSquadChargeSheetBackingBean = flyingSquadChargeSheetBackingBean;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisabledSaved() {
		return disabledSaved;
	}

	public void setDisabledSaved(boolean disabledSaved) {
		this.disabledSaved = disabledSaved;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public int getReleasecount() {
		return releasecount;
	}

	public void setReleasecount(int releasecount) {
		this.releasecount = releasecount;
	}

}
