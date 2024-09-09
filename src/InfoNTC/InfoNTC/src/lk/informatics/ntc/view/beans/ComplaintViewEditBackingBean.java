package lk.informatics.ntc.view.beans;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.commons.collections.ListUtils;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.GrievanceManagementService;
import lk.informatics.ntc.model.service.RouteCreatorService;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

/**
 * 
 * @author dilakshi.h
 *
 */
@ViewScoped
@ManagedBean(name="complaintViewEditBackingBean")
public class ComplaintViewEditBackingBean {
	
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
	private GrievanceManagementService grievanceManagementService;
	private CommonService commonService;
	private TerminalManagementService terminalManagementService;
	private RouteCreatorService routeCreatorService;
	private AdminService adminService;
	private DocumentManagementService documentManagementService;
	
	private String sucessMsg;
	private String errorMsg;
	
	private String dateFormatStr = "dd/MM/yyyy";
	private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
	
	private boolean complaintSelected = false;
	private String complaintNo;
	private String vehicleNo;
	private String permitNo;
	private List<String> complaintNoList;
	private List<String> vehicleNoList;
	private List<String> permitNoList;
	private List<String> vehicleNoListForTP;
	private List<String> permitNoListForTP;
	private List<String> allVehicleNoList;
	private List<String> allPermitNoList;
	private ComplaintRequestDTO selectedComplaintDTO = new ComplaintRequestDTO();
	private List<ComplaintRequestDTO> complaintDetailDTOList = new ArrayList<ComplaintRequestDTO>();
	private List<ComplaintRequestDTO> oldComplaintDetailDTOList = new ArrayList<ComplaintRequestDTO>();
	private boolean editMode ;
	private boolean disableEditBtn = true;
	private boolean disableMode = true;
	private String mode;
	private String complainType;
	String tempPermit =null;
	String tempVehicleNo =null;
	String tempComplaintNo =null;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	private List<ComplaintRequestDTO>commitedOffence = new ArrayList<ComplaintRequestDTO>();
	@PostConstruct
	public void init() {
		grievanceManagementService = (GrievanceManagementService) SpringApplicationContex.getBean("grievanceManagementService");
		terminalManagementService = (TerminalManagementService) SpringApplicationContex.getBean("terminalManagementService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex.getBean("documentManagementService");
		
		complaintNoList = grievanceManagementService.getAllComplainNo();
		vehicleNoList = commonService.getAllVehicle();
		permitNoList = commonService.getAllPermit();
		vehicleNoListForTP = commonService.getAllTemporaryVehicle();
		permitNoListForTP = commonService.getAllTemporaryPermit();
		complainType=null;
		disableMode = true;
		allVehicleNoList=ListUtils.union(vehicleNoList, vehicleNoListForTP);
		allPermitNoList = ListUtils.union(permitNoList, permitNoListForTP);
		//Check edit permission
		String result = adminService.getUserActivity(sessionBackingBean.loginUser,"FN451");
	   	if (result != null)
	   	{
	   		setMode("E");
	   		disableEditBtn= false; // added by tharushi.e
	   	}
	   	else
	   	{
	   		setMode("V");
	   	}
	}
	

	public List<String> completeComplaintNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();
        
        for (int i = 0; i < complaintNoList.size(); i++) {
            String cm = complaintNoList.get(i);
            if(cm.toUpperCase().contains(query)) {
            	filteredNo.add(cm);
            }
        }
        return filteredNo;
    }
	
	public List<String> completeVehicleNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();
        
        for (int i = 0; i < vehicleNoList.size(); i++) {
            String cm = vehicleNoList.get(i);
            if(cm.toUpperCase().contains(query)) {
            	filteredNo.add(cm);
            }
        }
        return filteredNo;
    }
	
	public List<String> completePermitNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();
        
        for (int i = 0; i < permitNoList.size(); i++) {
            String cm = permitNoList.get(i);
            if(cm != null && cm.toUpperCase().contains(query)) {
            	filteredNo.add(cm);
            }
        }
        return filteredNo;
    }
	
	public List<String> completeVehicleNoForSearch(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();
        
        for (int i = 0; i < allVehicleNoList.size(); i++) {
            String cm = allVehicleNoList.get(i);
            if(cm.toUpperCase().contains(query)) {
            	filteredNo.add(cm);
            }
        }
        return filteredNo;
    }
	
	public List<String> completePermitNoForSearch(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();
        
        for (int i = 0; i < allPermitNoList.size(); i++) {
            String cm = allPermitNoList.get(i);
            if(cm != null && cm.toUpperCase().contains(query)) {
            	filteredNo.add(cm);
            }
        }
        return filteredNo;
    }
	
	
	public void onVehicleNoChange() {

		vehicleNo = selectedComplaintDTO.getVehicleNo().trim().toUpperCase();
		PermitDTO permitDetailDTO = new PermitDTO();
		if(selectedComplaintDTO.getPermitAuthority().equals("TP")) {
			 permitDetailDTO = terminalManagementService.getTemporaryPermitInfoByBusNoPermitNo(null, vehicleNo);
			 vehicleNoList = commonService.getAllTemporaryVehicle();
		}else {
		 permitDetailDTO = terminalManagementService.getPermitInfoByBusNoPermitNo(null, vehicleNo);
		}
		permitNo = permitDetailDTO.getPermitNo();
		selectedComplaintDTO.setPermitNo(permitDetailDTO.getPermitNo());
		selectedComplaintDTO.setRouteNo(permitDetailDTO.getRouteNo());
		selectedComplaintDTO.setOrigin( permitDetailDTO.getOrigin());
		selectedComplaintDTO.setDestination(permitDetailDTO.getDestination());
		if(permitDetailDTO.getProvince()!=null) {
		selectedComplaintDTO.setRouteNo(permitDetailDTO.getProvince());
		}
	 
	    selectedComplaintDTO.setRouteNo(permitDetailDTO.getServiceTypeDesc());
		
	}
	
	
	public void eventDateTimeSelect() {
		try {
			SimpleDateFormat newDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			selectedComplaintDTO.setEventDateTime(newDf.format(oldDf.parse(selectedComplaintDTO.getEventDateTime())));
		} catch(NullPointerException e) { 
			e.printStackTrace();
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	/********************************************************************************************************************
	 ****************************** View/ Edit Complaints ***************************************************************
	 ********************************************************************************************************************/
	public void searchComplaint() {
		editMode = false;
		disableEditBtn= false;//added by dinushi on 02-04-2021
		
		if ((complaintNo == null || complaintNo.trim().isEmpty()) &&
				(vehicleNo == null || vehicleNo.trim().isEmpty()) &&
				(permitNo == null || permitNo.trim().isEmpty())) {
			errorMsg="Please enter a value first.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}

		if (complaintNo != null && !complaintNo.trim().isEmpty()) {
			complaintNo = complaintNo.toUpperCase();
			complaintDetailDTOList = grievanceManagementService.getComplaintDetails(complaintNo, null, null);
			commitedOffence = grievanceManagementService.getCommittedOffenceListByComplainSeqList(null,null,complaintNo);
		}
		if (complaintDetailDTOList.isEmpty() && permitNo != null && !permitNo.trim().isEmpty()) {
			permitNo = permitNo.toUpperCase();
			complaintDetailDTOList = grievanceManagementService.getComplaintDetails(null, null, permitNo);
			commitedOffence = grievanceManagementService.getCommittedOffenceListByComplainSeqList(null, permitNo,null);
		}
		if (complaintDetailDTOList.isEmpty() && vehicleNo != null && !vehicleNo.trim().isEmpty()) {
			vehicleNo = vehicleNo.toUpperCase();
			complaintDetailDTOList = grievanceManagementService.getComplaintDetails(null, vehicleNo, null);
			commitedOffence = grievanceManagementService.getCommittedOffenceListByComplainSeqList(vehicleNo, null,null);
		}
		if (complaintDetailDTOList.isEmpty()){
			errorMsg="No Data found.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show();");
			return;
		}
		
		if (complaintDetailDTOList.size() > 1) {
			RequestContext.getCurrentInstance().execute("PF('complaintsDialog').show();");
		} else {
			selectedComplaintDTO = complaintDetailDTOList.get(0);
			complaintSelected = true;
			disableSave();
		}
		if(selectedComplaintDTO.getPermitAuthority() != null) {
		if(selectedComplaintDTO.getPermitAuthority().equals("TP")) {
		vehicleNoList = commonService.getAllTemporaryVehicle();	
		}
		}
	}
	
	public void selectOneComplaint() {
		complaintSelected = true;
		RequestContext.getCurrentInstance().execute("PF('complaintsDialog').hide();");
		
		disableSave();
	}
	
	
	public void editComplaint() {
		oldComplaintDetailDTOList = complaintDetailDTOList;
		editMode = true;
		disableEditBtn= true;
		
	}

	public void disableSave()
	{
		
		tempPermit = selectedComplaintDTO.getPermitNo();
		tempVehicleNo =selectedComplaintDTO.getVehicleNo();
		 tempComplaintNo = selectedComplaintDTO.getComplaintNo();
		if(selectedComplaintDTO.getProcessStatus() != null) {
		if(selectedComplaintDTO.getProcessStatus().equalsIgnoreCase("C"))
		{
			disableMode=true;
		}
		else {
			disableMode=false;
		}
		}
		else
			disableMode=false;
		if(selectedComplaintDTO.getComplaintNo() != null) {
			commitedOffence = grievanceManagementService.getCommittedOffenceListByComplainSeqList(null,null,selectedComplaintDTO.getComplaintNo());
		}
		
	}
	
	public void exitEditMode() {
		complaintDetailDTOList = oldComplaintDetailDTOList;
		editMode = false;
		disableEditBtn= false; // added by tharushi.e
		eventDateTimeSelect();
		searchComplaintAfterExitEdit();
		//Check edit permission
				String result = adminService.getUserActivity(sessionBackingBean.loginUser,"FN451");
			   	if (result != null)
			   	{
			   		setMode("E");
			   		disableEditBtn= false; // added by tharushi.e
			   	}
			   	else
			   	{
			   		setMode("V");
			   	}
		
	}
	
	public void searchComplaintAfterExitEdit() {
	
		
		if ((tempComplaintNo == null || tempComplaintNo.trim().isEmpty()) &&
				(tempPermit == null || tempPermit.trim().isEmpty()) &&
				(tempVehicleNo == null || tempVehicleNo.trim().isEmpty())) {
			errorMsg="Please enter a value first.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}

		if (tempComplaintNo != null && !tempComplaintNo.trim().isEmpty()) {
			tempComplaintNo = tempComplaintNo.toUpperCase();
			complaintDetailDTOList = grievanceManagementService.getComplaintDetails(tempComplaintNo, null, null);
			commitedOffence = grievanceManagementService.getCommittedOffenceListByComplainSeqList(null,null,tempComplaintNo);
		}
		if (complaintDetailDTOList.isEmpty() && tempPermit != null && !tempPermit.trim().isEmpty()) {
			tempPermit = tempPermit.toUpperCase();
			complaintDetailDTOList = grievanceManagementService.getComplaintDetails(null, null, tempPermit);
			commitedOffence = grievanceManagementService.getCommittedOffenceListByComplainSeqList(null, tempPermit,null);
		}
		if (complaintDetailDTOList.isEmpty() && tempVehicleNo != null && !tempVehicleNo.trim().isEmpty()) {
			tempVehicleNo = tempVehicleNo.toUpperCase();
			complaintDetailDTOList = grievanceManagementService.getComplaintDetails(null, tempVehicleNo, null);
			commitedOffence = grievanceManagementService.getCommittedOffenceListByComplainSeqList(tempVehicleNo, null,null);
		}
		if (complaintDetailDTOList.isEmpty()){
			errorMsg="No Data found.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show();");
			return;
		}
		if (complaintDetailDTOList.size() > 1) {
			RequestContext.getCurrentInstance().execute("PF('complaintsDialog').show();");
		} else {
			selectedComplaintDTO = complaintDetailDTOList.get(0);
			complaintSelected = true;
			disableSave();
		}
		
		
		
	}
	
	
	public void saveEditedComplaint() {
		eventDateTimeSelect();
		if (selectedComplaintDTO.getComplainTypeCode() == null || selectedComplaintDTO.getComplainTypeCode().isEmpty() || selectedComplaintDTO.getPriorityOrder() == null || selectedComplaintDTO.getPriorityOrder().isEmpty() || selectedComplaintDTO.getSeverityNo() == null || selectedComplaintDTO.getSeverityNo().isEmpty() || selectedComplaintDTO.getComplainMedia() == null || selectedComplaintDTO.getComplainMedia().isEmpty() ||
				selectedComplaintDTO.getPermitAuthority() == null || selectedComplaintDTO.getPermitAuthority().isEmpty() ||
				selectedComplaintDTO.getEventPlace() == null || selectedComplaintDTO.getEventPlace().trim().isEmpty() ||
				selectedComplaintDTO.getEventDateTime() == null || selectedComplaintDTO.getEventDateTime().trim().isEmpty() ) {
			errorMsg="Mandatory fields cannot be empty.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}
		
		/** added by tharushi.e for validate province **/
	     if(!selectedComplaintDTO.getPermitAuthority() .equalsIgnoreCase("NTC")) {
	    	if(selectedComplaintDTO.getProvince().equals(null) || selectedComplaintDTO.getProvince().trim().isEmpty()) {
	    		errorMsg="Mandatory fields cannot be empty.";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				return;
	    		
	    	} 
	    	 
	     }
	
	/** end **/
		if ((selectedComplaintDTO.getComplainerName() == null || selectedComplaintDTO.getComplainerName().trim().isEmpty()) && (selectedComplaintDTO.getComplainerName_ta() == null || selectedComplaintDTO.getComplainerName_ta().trim().isEmpty()) && (selectedComplaintDTO.getComplainerName_si() == null || selectedComplaintDTO.getComplainerName_si().trim().isEmpty()) ) {
			errorMsg="Complainer�s name is required.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}
		if (selectedComplaintDTO.isComplainerParticipation() && 
				( ( (selectedComplaintDTO.getAddress1() == null || selectedComplaintDTO.getAddress1().trim().isEmpty()) && 
						  (selectedComplaintDTO.getAddress1_ta() == null || selectedComplaintDTO.getAddress1_ta().trim().isEmpty()) &&
						  (selectedComplaintDTO.getAddress1_si() == null || selectedComplaintDTO.getAddress1_si().trim().isEmpty()) )
				
				  )
				) {
			errorMsg="Complainer�s details are required.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}
		
		String user = sessionBackingBean.loginUser;
		
		if (selectedComplaintDTO != null && !grievanceManagementService.updateComplaintRequest(selectedComplaintDTO, user)) {
			errorMsg="Error updating data";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		
		}
		grievanceManagementService.updateCommitedOffences(selectedComplaintDTO, selectedComplaintDTO.getComplainSeq(), selectedComplaintDTO.getCommittedOffences(), user);
		selectedComplaintDTO.setCommittedOffences(grievanceManagementService.getCommittedOffencesByComplaint(selectedComplaintDTO.getComplainSeq(), false));
		grievanceManagementService.beanLinkMethod(selectedComplaintDTO, user, "Edit Complaint Data"," Initiate Complaint Request");
		sucessMsg = "Data updated successfully.";
		RequestContext.getCurrentInstance().update("frmSuccess");
		RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
		
		editMode = false;
		disableEditBtn=false; // added by tharushi.e
	}
	
	
	public void clearSearchView() {
		complaintNo = null;
		vehicleNo = null;
		permitNo = null;
		complaintSelected = false;
		editMode = false;
		selectedComplaintDTO = new ComplaintRequestDTO();
		complaintDetailDTOList = new ArrayList<ComplaintRequestDTO>();
		disableEditBtn= false;//added by dinushi on 02-04-2021
	}
	
	public void clearDetailView() {
		complaintSelected = false;
		editMode = false;
		selectedComplaintDTO = new ComplaintRequestDTO();
		complaintDetailDTOList = new ArrayList<ComplaintRequestDTO>();
	}
	
	public void documentManagement()
	{
		try {
		
			sessionBackingBean.setComplainNo(complaintNo);
			
			String strTransactionType = "24";
			sessionBackingBean.setTransactionType("COMPLAIN");
			
			setMandatoryList(documentManagementService.mandatoryDocsForGrievance(strTransactionType,complaintNo));
			setOptionalList(documentManagementService.optionalDocsForGrievance(strTransactionType,complaintNo));

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService.grievanceMandatoryListM(complaintNo,strTransactionType);
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService.grievanceOptionalListM(complaintNo,strTransactionType);
			
			grievanceManagementService.beanLinkMethod(sessionBackingBean.getLoginUser(), vehicleNo, complaintNo, permitNo,"Initiate Complaint Request","Document Submit");
			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	
	
	
	/*
	 * Common methods
	 */
	public static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
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

	public TerminalManagementService getTerminalManagementService() {
		return terminalManagementService;
	}

	public void setTerminalManagementService(TerminalManagementService terminalManagementService) {
		this.terminalManagementService = terminalManagementService;
	}

	public RouteCreatorService getRouteCreatorService() {
		return routeCreatorService;
	}

	public void setRouteCreatorService(RouteCreatorService routeCreatorService) {
		this.routeCreatorService = routeCreatorService;
	}

	public boolean isComplaintSelected() {
		return complaintSelected;
	}

	public void setComplaintSelected(boolean complaintSelected) {
		this.complaintSelected = complaintSelected;
	}

	public String getComplaintNo() {
		return complaintNo;
	}

	public void setComplaintNo(String complaintNo) {
		this.complaintNo = complaintNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public ComplaintRequestDTO getSelectedComplaintDTO() {
		return selectedComplaintDTO;
	}

	public void setSelectedComplaintDTO(ComplaintRequestDTO selectedComplaintDTO) {
		this.selectedComplaintDTO = selectedComplaintDTO;
	}

	public List<ComplaintRequestDTO> getComplaintDetailDTOList() {
		return complaintDetailDTOList;
	}

	public void setComplaintDetailDTOList(List<ComplaintRequestDTO> complaintDetailDTOList) {
		this.complaintDetailDTOList = complaintDetailDTOList;
	}

	public List<String> getComplaintNoList() {
		return complaintNoList;
	}

	public void setComplaintNoList(List<String> complaintNoList) {
		this.complaintNoList = complaintNoList;
	}

	public List<String> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<String> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean isDisableEditBtn() {
		return disableEditBtn;
	}

	public void setDisableEditBtn(boolean disableEditBtn) {
		this.disableEditBtn = disableEditBtn;
	}

	public List<DocumentManagementDTO> getMandatoryList() {
		return mandatoryList;
	}

	public void setMandatoryList(List<DocumentManagementDTO> mandatoryList) {
		this.mandatoryList = mandatoryList;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public boolean isDisableMode() {
		return disableMode;
	}

	public void setDisableMode(boolean disableMode) {
		this.disableMode = disableMode;
	}


	public List<ComplaintRequestDTO> getCommitedOffence() {
		return commitedOffence;
	}


	public void setCommitedOffence(List<ComplaintRequestDTO> commitedOffence) {
		this.commitedOffence = commitedOffence;
	}


	

}
