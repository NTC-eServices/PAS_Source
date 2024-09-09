package lk.informatics.ntc.view.beans;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import lk.informatics.ntc.model.dto.CommittedOffencesDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
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
@ManagedBean(name="complaintInitiationBackingBean")
public class ComplaintInitiationBackingBean {
	
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
	
	private boolean vehicleSelected = false;
	private String complainType = "C";
	private String complaintNo;
	private String permitAuthority = "NTC";
	private String vehicleNo;
	private String permitNo;
	private String routeNo;
	private String province;
	private String originDesc;
	private String destinationDesc;
	private String depot;
	private String priorityOrder;
	private String severity;
	private String eventPlace;
	private String eventDateTime;
	private String complaintMedia;
	private boolean writtenProof;
	private boolean complainerParticipation;
	private String complaintDetail;
	private String commitedOffenceOther;
	private String selectedLangTab = "en";
	private String name;
	private String contact1;
	private String contact2;
	private String address1;
	private String address2;
	private String city;
	private String name_si;
	private String address1_si;
	private String address2_si;
	private String city_si;
	private String name_ta;
	private String address1_ta;
	private String address2_ta;
	private String city_ta;
	private String serviceDescription;
	private String vehicleNumber;
	private String permitNumber;
	private List<String> vehicleNoList;
	private List<String> permitNoList;
	private List<DropDownDTO> priorityOrderList = new ArrayList<DropDownDTO>();
	private List<DropDownDTO> severityList = new ArrayList<DropDownDTO>();
	private List<DropDownDTO> complainMediaList = new ArrayList<DropDownDTO>();
	private List<RouteCreationDTO> routeList;
	private List<CommonDTO> provincelList;
	private List<CommittedOffencesDTO> committedOffencesList = new ArrayList<CommittedOffencesDTO>();
	private List<String> langList = Arrays.asList("en", "si", "ta");
	private boolean disDocument;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	
	public ComplaintInitiationBackingBean() {
		grievanceManagementService = (GrievanceManagementService) SpringApplicationContex.getBean("grievanceManagementService");
		terminalManagementService = (TerminalManagementService) SpringApplicationContex.getBean("terminalManagementService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		routeCreatorService = (RouteCreatorService)SpringApplicationContex.getBean("routeCreatorService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex.getBean("documentManagementService");
		
		provincelList = adminService.getProvinceToDropdown();
		routeList = routeCreatorService.getAllRoutes();
		priorityOrderList = grievanceManagementService.getPriorityOrderList();
		severityList = grievanceManagementService.getSeverityList();
		complainMediaList = grievanceManagementService.getComplaintMediaList();
		/**committedOffencesList -->only show public complaint =Y in detail table request for NTC 2022/09/09 tharushi.e***/
		//committedOffencesList = grievanceManagementService.getCommittedOffenceList();
		committedOffencesList = grievanceManagementService.getCommittedOffenceListForPublicComplaint();
		complaintNo = grievanceManagementService.generateCIFNo("C");
		vehicleNoList = commonService.getAllVehicle();
		permitNoList = commonService.getAllPermit();
		disDocument = true;
		
		resetDropDowns();
	}

	private void resetDropDowns() {

		if(complainMediaList != null && !complainMediaList.isEmpty())
			complaintMedia = complainMediaList.get(0).getCode();
	}
	
	public void onPermitAuthorityChange(ValueChangeEvent e) {
		clearView();	
		if (e.getNewValue() != null) {
			permitAuthority = e.getNewValue().toString();
	
		}
		if(permitAuthority.equals("TP")) {
			vehicleNoList = commonService.getAllTemporaryVehicle();
			permitNoList = commonService.getAllTemporaryPermit();	
		}
		else {
			vehicleNoList = commonService.getAllVehicle();
			permitNoList = commonService.getAllPermit();
		}
		
		
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
            if(cm.toUpperCase().contains(query)) {
            	filteredNo.add(cm);
            }
        }
        return filteredNo;
    }
	
	public void onComplaintTypeChange(ValueChangeEvent e) {
	
		if (e.getNewValue() != null) {
			complainType = e.getNewValue().toString();
	
		}
	}
	
	public synchronized void generateComplaintNo() {
		complaintNo = grievanceManagementService.generateCIFNo(complainType);
	}
	
	public void onPermitNoChange() {
		PermitDTO permitDetailDTO = new PermitDTO();
		if(permitAuthority.equals("TP")) {
			 permitDetailDTO = terminalManagementService.getTemporaryPermitInfoByBusNoPermitNo(permitNo, null);	
		}else {
			
			if (permitNo != null && !permitNo.trim().isEmpty() && !terminalManagementService.validateVehicleOrPermitNo("PERMIT", permitNo.trim().toUpperCase())) {
				errorMsg="Invalid Permit Number";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				return;
			}
			permitDetailDTO = terminalManagementService.getPermitInfoByBusNoPermitNo(permitNo, null);
		}
		
		
		permitNo = permitNo.trim().toUpperCase();
		 
		vehicleNo = permitDetailDTO.getBusRegNo();
		routeNo = permitDetailDTO.getRouteNo();
		originDesc = permitDetailDTO.getOrigin();
		destinationDesc = permitDetailDTO.getDestination();
		province = permitDetailDTO.getProvince();
		serviceDescription = permitDetailDTO.getServiceTypeDesc();
		permitNumber = permitNo;
		vehicleNumber =  permitDetailDTO.getBusRegNo();
		 
	}
	
	public void onVehicleNoChange() {

		vehicleNo = vehicleNo.trim().toUpperCase();
		PermitDTO permitDetailDTO = new PermitDTO();
		if(permitAuthority.equals("TP")) {
			 permitDetailDTO = terminalManagementService.getTemporaryPermitInfoByBusNoPermitNo(null, vehicleNo);	
		}else {
		 permitDetailDTO = terminalManagementService.getPermitInfoByBusNoPermitNo(null, vehicleNo);
		}
		permitNo = permitDetailDTO.getPermitNo();
		routeNo = permitDetailDTO.getRouteNo();
		originDesc = permitDetailDTO.getOrigin();
		destinationDesc = permitDetailDTO.getDestination();
		if(permitDetailDTO.getProvince()!=null) {
		province = permitDetailDTO.getProvince();
		}
	    serviceDescription = permitDetailDTO.getServiceTypeDesc();
	    permitNumber = permitDetailDTO.getPermitNo();
	    vehicleNumber = vehicleNo;
		
	}
	
	public void eventDateTimeSelect() {
		try {
			SimpleDateFormat newDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(newDf != null && !newDf.toString().trim().equals("") ) {
			eventDateTime = newDf.format(oldDf.parse(eventDateTime));
			}
		} catch(NullPointerException e) { 
			e.printStackTrace();
		} catch(ParseException e) { 
			e.printStackTrace();
		} catch(Exception e) { 
			e.printStackTrace();
		}
	}
	
	public void clearCommitedOffence() {
		for(CommittedOffencesDTO offence : committedOffencesList) {
			offence.setApplicable(false);
			offence.setRemark(null);
		}
		commitedOffenceOther = null;
	}
	
	public synchronized void submitComplaint() {
		eventDateTimeSelect();
		
		if (complainType == null || complainType.isEmpty() || priorityOrder == null || priorityOrder.isEmpty() || severity == null || severity.isEmpty() || complaintMedia == null || complaintMedia.isEmpty() ||
				permitAuthority == null || permitAuthority.isEmpty() ||
				(!complainType.equalsIgnoreCase("I") && (vehicleNo == null || vehicleNo.trim().isEmpty())) ||
				(permitAuthority.equalsIgnoreCase("NTC") && (vehicleNo == null || vehicleNo.trim().isEmpty())) ||
				eventPlace == null || eventPlace.trim().isEmpty() ||
				eventDateTime == null || eventDateTime.trim().isEmpty() ||
				( (name == null || name.trim().isEmpty()) && (name_si == null || name_si.trim().isEmpty()) && (name_ta == null || name_ta.trim().isEmpty()) )) {
			errorMsg="Mandatory fields cannot be empty.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}
		/** added by tharushi.e for validate province **/
		     if(!permitAuthority.equalsIgnoreCase("NTC")) {
		    	if(province.equals(null) || province.trim().isEmpty()) {
		    		errorMsg="Mandatory fields cannot be empty.";
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
					return;
		    		
		    	} 
		    	 
		     }
		
		/** end **/
		
	
		String user = sessionBackingBean.loginUser;
		generateComplaintNo();
	
		
		long returnSeq = grievanceManagementService.insertComplaintRequest(permitAuthority, permitNo, vehicleNo, complainType, complaintNo, priorityOrder, severity, routeNo, originDesc, destinationDesc, depot, province, 
				eventPlace, eventDateTime, complaintMedia, selectedLangTab, name,name_si,name_ta, address1,address1_si,address1_ta, address2,address2_si,address2_ta, city,city_si,city_ta, contact1, contact2, complainerParticipation, writtenProof, commitedOffenceOther, complaintDetail, user,serviceDescription);
		
		if (returnSeq == 0) {
			errorMsg="Error Saving data";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}
		
		grievanceManagementService.insertCommitedOffences(returnSeq, committedOffencesList, user);
		
		//update sequence
		String newcomplaintNo = complaintNo.substring(6);
		if (newcomplaintNo.equals("00001") || (newcomplaintNo == "00001" ))
		{
			if (complainType.equalsIgnoreCase("C"))
				grievanceManagementService.updateParamSequenceN("COMPLAINT_NO");
			else if (complainType.equalsIgnoreCase("I"))
				grievanceManagementService.updateParamSequenceN("INQUIRY_NO");
			else if (complainType.equalsIgnoreCase("F"))
				grievanceManagementService.updateParamSequenceN("FA_NO");
		}
		else
		{
			if  (complainType.equalsIgnoreCase("C"))
				grievanceManagementService.updateParamSequence("COMPLAINT_NO");
			else if (complainType.equalsIgnoreCase("I"))
				grievanceManagementService.updateParamSequence("INQUIRY_NO");
			else if (complainType.equalsIgnoreCase("F"))
				grievanceManagementService.updateParamSequence("FA_NO");			
		}
			
		
		
		//send SMS
		if (contact1 != null && !contact1.isEmpty()) {
			String message = "Your complaint is recorded successfully. Reference no is " + complaintNo;
			String subject = "Complaint is recorded Succesfully";
			grievanceManagementService.sendComplaintRefSMS(contact1, message, subject);
			grievanceManagementService.beanLinkMethod(user, vehicleNo, complaintNo, permitNo,"Initiate Complaint Request","Complaint Reference No Sent");
		} else
			System.err.println("SENDING SMS FAILED. CONTACT NO IS NOT PROVIDED.");
		
		grievanceManagementService.beanLinkMethod(user, vehicleNo, complaintNo, permitNo,"Initiate Complaint Request","Complaint Submit");
		RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
		disDocument= false;
		clearView();
	}
	
	
	public void clearView() {
		vehicleSelected = false;
		complainType = "C";
		permitAuthority = "NTC";
		vehicleNo = null;
		permitNo = null;
		routeNo = null;
		province = null;
		originDesc = null;
		destinationDesc = null;
		depot = null;
		priorityOrder = null;
		severity = null;
		eventPlace = null;
		eventDateTime = null;
		complaintMedia = null;
		writtenProof = false;
		complainerParticipation = false;
		complaintDetail = null;
		commitedOffenceOther = null;
		selectedLangTab = "en";
		name = null;
		contact1 = null;
		contact2 = null;
		address1 = null;
		address2 = null;
		name_si = null;
		name_ta = null;
		address1_si = null;
		address2_si = null;
		city_si = null;
		address1_ta = null;
		address2_ta = null;
		city_ta = null;
		city = null;
		resetDropDowns();
		routeList = new ArrayList<RouteCreationDTO>();
		clearCommitedOffence();
		disDocument=true;
		vehicleNumber =  null;
		permitNumber = null;
		serviceDescription =  null;
	}
	
	public void documentManagement()
	{
		try {
			
			if(complainType.equals("C"))
				{sessionBackingBean.setTransactionType("Complaint");}
			else if(complainType.equals("I"))
			{sessionBackingBean.setTransactionType("Inquiry");}
			else {sessionBackingBean.setTransactionType("Fatal Accident");}
			
			sessionBackingBean.setComplainNo(complaintNo);
		

			String strTransactionType = complainType;
			
			setMandatoryList(documentManagementService.mandatoryDocsForGrievance(strTransactionType,complaintNo));
			setOptionalList(documentManagementService.optionalDocsForGrievance(strTransactionType,complaintNo));

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService.grievanceMandatoryListM(complaintNo,strTransactionType);
			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService.grievanceOptionalListM(complaintNo,strTransactionType);
			 
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

	public boolean isVehicleSelected() {
		return vehicleSelected;
	}

	public void setVehicleSelected(boolean vehicleSelected) {
		this.vehicleSelected = vehicleSelected;
	}

	public String getComplainType() {
		return complainType;
	}

	public void setComplainType(String complainType) {
		this.complainType = complainType;
	}

	public String getComplaintNo() {
		return complaintNo;
	}

	public void setComplaintNo(String complaintNo) {
		this.complaintNo = complaintNo;
	}

	public String getPermitAuthority() {
		return permitAuthority;
	}

	public void setPermitAuthority(String permitAuthority) {
		this.permitAuthority = permitAuthority;
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

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getOriginDesc() {
		return originDesc;
	}

	public void setOriginDesc(String originDesc) {
		this.originDesc = originDesc;
	}

	public String getDestinationDesc() {
		return destinationDesc;
	}

	public void setDestinationDesc(String destinationDesc) {
		this.destinationDesc = destinationDesc;
	}

	public String getDepot() {
		return depot;
	}

	public void setDepot(String depot) {
		this.depot = depot;
	}

	public String getPriorityOrder() {
		return priorityOrder;
	}

	public void setPriorityOrder(String priorityOrder) {
		this.priorityOrder = priorityOrder;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getEventPlace() {
		return eventPlace;
	}

	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}

	public String getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(String eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public String getComplaintMedia() {
		return complaintMedia;
	}

	public void setComplaintMedia(String complaintMedia) {
		this.complaintMedia = complaintMedia;
	}

	public boolean isWrittenProof() {
		return writtenProof;
	}

	public void setWrittenProof(boolean writtenProof) {
		this.writtenProof = writtenProof;
	}

	public boolean isComplainerParticipation() {
		return complainerParticipation;
	}

	public void setComplainerParticipation(boolean complainerParticipation) {
		this.complainerParticipation = complainerParticipation;
	}

	public String getComplaintDetail() {
		return complaintDetail;
	}

	public void setComplaintDetail(String complaintDetail) {
		this.complaintDetail = complaintDetail;
	}

	public String getCommitedOffenceOther() {
		return commitedOffenceOther;
	}

	public void setCommitedOffenceOther(String commitedOffenceOther) {
		this.commitedOffenceOther = commitedOffenceOther;
	}

	public List<DropDownDTO> getPriorityOrderList() {
		return priorityOrderList;
	}

	public void setPriorityOrderList(List<DropDownDTO> priorityOrderList) {
		this.priorityOrderList = priorityOrderList;
	}

	public List<DropDownDTO> getSeverityList() {
		return severityList;
	}

	public void setSeverityList(List<DropDownDTO> severityList) {
		this.severityList = severityList;
	}

	public List<DropDownDTO> getComplainMediaList() {
		return complainMediaList;
	}

	public void setComplainMediaList(List<DropDownDTO> complainMediaList) {
		this.complainMediaList = complainMediaList;
	}

	public List<RouteCreationDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteCreationDTO> routeList) {
		this.routeList = routeList;
	}

	public List<CommittedOffencesDTO> getCommittedOffencesList() {
		return committedOffencesList;
	}

	public void setCommittedOffencesList(List<CommittedOffencesDTO> committedOffencesList) {
		this.committedOffencesList = committedOffencesList;
	}

	public String getSelectedLangTab() {
		return selectedLangTab;
	}

	public void setSelectedLangTab(String selectedLangTab) {
		this.selectedLangTab = selectedLangTab;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact1() {
		return contact1;
	}

	public void setContact1(String contact1) {
		this.contact1 = contact1;
	}

	public String getContact2() {
		return contact2;
	}
	
	public void setContact2(String contact2) {
		this.contact2 = contact2;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<CommonDTO> getProvincelList() {
		return provincelList;
	}

	public void setProvincelList(List<CommonDTO> provincelList) {
		this.provincelList = provincelList;
	}

	public String getName_si() {
		return name_si;
	}

	public void setName_si(String name_si) {
		this.name_si = name_si;
	}

	public String getAddress1_si() {
		return address1_si;
	}

	public void setAddress1_si(String address1_si) {
		this.address1_si = address1_si;
	}

	public String getAddress2_si() {
		return address2_si;
	}

	public void setAddress2_si(String address2_si) {
		this.address2_si = address2_si;
	}


	public String getCity_si() {
		return city_si;
	}

	public void setCity_si(String city_si) {
		this.city_si = city_si;
	}

	public String getName_ta() {
		return name_ta;
	}

	public void setName_ta(String name_ta) {
		this.name_ta = name_ta;
	}

	public String getAddress1_ta() {
		return address1_ta;
	}

	public void setAddress1_ta(String address1_ta) {
		this.address1_ta = address1_ta;
	}

	public String getAddress2_ta() {
		return address2_ta;
	}

	public void setAddress2_ta(String address2_ta) {
		this.address2_ta = address2_ta;
	}

	public String getCity_ta() {
		return city_ta;
	}

	public void setCity_ta(String city_ta) {
		this.city_ta = city_ta;
	}

	public List<String> getLangList() {
		return langList;
	}

	public void setLangList(List<String> langList) {
		this.langList = langList;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
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

	public boolean isDisDocument() {
		return disDocument;
	}

	public void setDisDocument(boolean disDocument) {
		this.disDocument = disDocument;
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

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getPermitNumber() {
		return permitNumber;
	}

	public void setPermitNumber(String permitNumber) {
		this.permitNumber = permitNumber;
	}
	

}
