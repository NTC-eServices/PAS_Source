package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.collections.ListUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.FareOfSemiLuxuryReportDTO;
import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.OffenceManagementDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.service.CombineTimeTableGenerateService;
import lk.informatics.ntc.model.service.OffenceManagementService;
import lk.informatics.ntc.model.service.ReportService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "offenceManagementBackingBean")
@ViewScoped
public class OffenceManagementBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
   //services
	private OffenceManagementService offenceManagementService;
	//DTO
	OffenceManagementDTO offenceMangDTO = new OffenceManagementDTO ();
	OffenceManagementDTO selectDTO = new OffenceManagementDTO();
	OffenceManagementDTO selectDTOEdit = new  OffenceManagementDTO();
	//List
	

	private List<OffenceManagementDTO> offenceCodeList = new ArrayList<OffenceManagementDTO>(0);
	private List<OffenceManagementDTO> offenceCodeDetails;
	private List<OffenceManagementDTO> offenceDetailsshowSecondGrid;
	private List<String> duplicateOffenceCodeList;
	private List<OffenceManagementDTO>attempsList;
	private OffenceManagementDTO ofenceDesDet = new OffenceManagementDTO();
	private StreamedContent files;
	private String errorMsg,sucessMsg;
	private boolean addbuttDisable,showList,showWhenAdd,editMode,addOffenDet,editModeSecond,addbuttDisable1,noOfAttemptDisable;
	private String offenceCodeStr,noOfAttemptForEdit;
	private String mode;
	 String  code = null;
	@PostConstruct
	public void init() {
		offenceManagementService = (OffenceManagementService) SpringApplicationContex.getBean("offenceManagementService");
		offenceMangDTO= new OffenceManagementDTO();
		offenceCodeList=offenceManagementService.getExistingOffenceCode();
		addbuttDisable=false;
		offenceCodeDetails=offenceManagementService.getOffenceCodeDataForGrid();
		attempsList=offenceManagementService.getAttemptsListDropDown();
		showList=true;
		showWhenAdd=false;
		mode="C";
		
	}
//change offence Description	
public void onOffenceCodeChange() {
	ofenceDesDet= offenceManagementService.getDescriptionForCodes(offenceMangDTO.getOffenceCode());
	offenceMangDTO.setOffenceDes(ofenceDesDet.getOffenceDes());
	offenceMangDTO.setOffenceDesSin(ofenceDesDet.getOffenceDesSin());
	offenceMangDTO.setOffenceDesTamil(ofenceDesDet.getOffenceDesTamil());
	addbuttDisable=true;
	addOffenDet=false;
	offenceCodeDetails=offenceManagementService.getOffenceCodeDataForGridForDropDown(offenceMangDTO.getOffenceCode());
	
}


// add button
public void addButAction() {
	String loginUser=sessionBackingBean.getLoginUser();
	if(editMode) {
		// no need to update offenceCode only description and status
		if(!offenceMangDTO.getOffenceDes().trim().isEmpty() || !offenceMangDTO.getOffenceDesSin().trim().isEmpty()
				|| !offenceMangDTO.getOffenceDesTamil().trim().isEmpty())	{
			
		offenceManagementService.updateEditDetails(offenceMangDTO,loginUser)	;
		offenceMangDTO = new OffenceManagementDTO();//new added
		offenceCodeDetails=offenceManagementService.getOffenceCodeDataForGrid();
		offenceCodeList=offenceManagementService.getExistingOffenceCode();
		 setShowList(false);
			setShowWhenAdd(true);
			setSucessMsg("Successfully updated.");
			RequestContext.getCurrentInstance().update("successSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		addbuttDisable=true;
		}
		
		else {
			setErrorMsg("Please enter description for offence code.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			
		}
	}
	else {
if(!offenceMangDTO.getOffenceDes().trim().isEmpty() || !offenceMangDTO.getOffenceDesSin().trim().isEmpty()
		|| !offenceMangDTO.getOffenceDesTamil().trim().isEmpty())	{
	//generate offence code
	/*offenceCodeStr=offenceManagementService.generateOffenceCode();
	offenceMangDTO.setOffenceCode(offenceCodeStr);*/
	if(!offenceMangDTO.getOffenceCode().trim().isEmpty())	{
	
	offenceCodeStr = offenceMangDTO.getOffenceCode();
	String strName = offenceManagementService.checkDuplicate(offenceCodeStr);
	if ((strName == null))
	{		
		//update number generated table Offence Code
		offenceManagementService.updateOffenceCodeInNumberGenTable(offenceCodeStr,loginUser);
		
		offenceManagementService.insertOffenceCodeDesriptionDet(offenceMangDTO,loginUser);
		offenceMangDTO = new OffenceManagementDTO();// new added
		offenceCodeDetails=offenceManagementService.getOffenceCodeDataForGrid();
		offenceCodeList=offenceManagementService.getExistingOffenceCode();
	    setShowList(false);
		setShowWhenAdd(true);
		addbuttDisable=true;
		setSucessMsg("Successfully Saved.");
		RequestContext.getCurrentInstance().update("successSve");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
	}
	else
	{
		setErrorMsg("Duplicate offence code.");
		RequestContext.getCurrentInstance().update("frmrequiredField");
		RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	}

	}
	else
	{
		// error msgs
		setErrorMsg("Please enter offence code.");
		RequestContext.getCurrentInstance().update("frmrequiredField");
		RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	}

}
else {
	// error msgs
	setErrorMsg("Please enter description for offence code.");
	RequestContext.getCurrentInstance().update("frmrequiredField");
	RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	
}
	}	
}

// clear button
public void clearButAction() {
	offenceMangDTO = new OffenceManagementDTO();
	addbuttDisable=false;
	showList=true;
	showWhenAdd=false;
	offenceCodeDetails=offenceManagementService.getOffenceCodeDataForGrid();
	addOffenDet=false;
	mode="C";
	editMode= false;
}
// add button on grid not delete
public void deleteButAction() {
	addOffenDet=false;
/*	offenceManagementService.deleteSelectedOffenceCode(selectDTO.getOffenceCode());
		
	offenceCodeDetails=offenceManagementService.getOffenceCodeDataForGrid();	
	setErrorMsg("Deleted");
	RequestContext.getCurrentInstance().update("frmrequiredField");
	RequestContext.getCurrentInstance().execute("PF('requiredField').show()");*/
	offenceMangDTO.setNoOfAttempts(null);
	offenceMangDTO.setPublicComplain(null);
	offenceMangDTO.setInvestigation(null);
	offenceMangDTO.setAmount(null);
	offenceMangDTO.setAction(null);
	offenceMangDTO.setNoOfDermitPoints(null);
	offenceMangDTO.setChargeAmount(null);
	offenceMangDTO.setActionTxt(null);
	
	if(selectDTO.getOffenceCodeStatus().equalsIgnoreCase("A") || selectDTO.getOffenceCodeStatus().equalsIgnoreCase("Active")) {
	addOffenDet=true;
    code=selectDTO.getOffenceCode();
	offenceMangDTO.setOffenceCode(selectDTO.getOffenceCode()); 
	offenceDetailsshowSecondGrid=offenceManagementService.getOffenceDetailOnSecondGrid(code);
	//
	offenceMangDTO.setOffenceCode(null);
	offenceMangDTO.setOffenceDes(null);
	offenceMangDTO.setOffenceDesSin(null);
	offenceMangDTO.setOffenceDesTamil(null);
	//
	}
	else {
		setErrorMsg("This offence code in inactive mode");
		RequestContext.getCurrentInstance().update("frmrequiredField");
		RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		
	}
	
}	

public void editButAction() {
	editMode= true;
	addbuttDisable=false;
	offenceMangDTO.setOffenceCode(selectDTO.getOffenceCode());
	offenceMangDTO.setOffenceDes(selectDTO.getOffenceDes());
	offenceMangDTO.setOffenceDesSin(selectDTO.getOffenceDesSin());
	offenceMangDTO.setOffenceDesTamil(selectDTO.getOffenceDesTamil());
	if(selectDTO.getOffenceCodeStatus().equalsIgnoreCase("Active")) {
		
		offenceMangDTO.setOffenceCodeStatus("A");
	}
	else if(selectDTO.getOffenceCodeStatus().equalsIgnoreCase("Inactive")) {
	offenceMangDTO.setOffenceCodeStatus("I");
	}
	
	
	  setShowList(false);
		setShowWhenAdd(true);
		addOffenDet=false;
}
		
	
public void addButActionSecond() {
	String loginUser= sessionBackingBean.getLoginUser();
	offenceDetailsshowSecondGrid = new ArrayList<>();
	

	if(editModeSecond) {
		// edit mode add
		if(!offenceMangDTO.getNoOfAttempts().trim().isEmpty() &&
				 !offenceMangDTO.getStatus().trim().isEmpty()) {
			

			
			
			
			offenceManagementService.updateOffenceDetailInDetTable(offenceMangDTO, loginUser,noOfAttemptForEdit,code);
			
			offenceMangDTO.setNoOfAttempts(null);
			offenceMangDTO.setPublicComplain("false");
			offenceMangDTO.setInvestigation("false");
			offenceMangDTO.setAmount("false");
			offenceMangDTO.setAction("false");
			offenceMangDTO.setNoOfDermitPoints(0);
			offenceMangDTO.setChargeAmount(0);
			offenceMangDTO.setActionTxt(null);
			offenceMangDTO.setStatus("A");
			addbuttDisable1=false;
			noOfAttemptDisable=false;
			offenceDetailsshowSecondGrid=offenceManagementService.getOffenceDetailOnSecondGrid(code);
	
			setSucessMsg("Successfully updated.");
			RequestContext.getCurrentInstance().update("successSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			editModeSecond=false;
			
			
		}
		else {
			setErrorMsg("Please enter mandatory feilds.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			offenceDetailsshowSecondGrid=offenceManagementService.getOffenceDetailOnSecondGrid(code);
			
			
		}
		
	}
	else {
	if(!offenceMangDTO.getNoOfAttempts().trim().isEmpty() &&
			 !offenceMangDTO.getStatus().trim().isEmpty()) {
		
		//check duplicate no of attempt foe offence code
		duplicateOffenceCodeList=offenceManagementService.getNoOfAttemptForCheckDuplicates(code);
		if(duplicateOffenceCodeList.contains(offenceMangDTO.getNoOfAttempts())){
			setErrorMsg("This attempt has already added for selected offence code");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			offenceMangDTO.setNoOfAttempts(null);
			offenceMangDTO.setPublicComplain("false");
			offenceMangDTO.setInvestigation("false");
			offenceMangDTO.setAmount("false");
			offenceMangDTO.setAction("false");
			offenceMangDTO.setNoOfDermitPoints(0);
			offenceMangDTO.setChargeAmount(0);
			offenceMangDTO.setActionTxt(null);
			offenceMangDTO.setStatus("A");
			noOfAttemptDisable=false;
			addbuttDisable1=false;
			offenceDetailsshowSecondGrid=offenceManagementService.getOffenceDetailOnSecondGrid(code);
		}
		else {
		
		
		offenceManagementService.insertOffenceDetailInDetTable(offenceMangDTO, loginUser,code);
//		String code=offenceMangDTO.getOffenceCode();
		offenceDetailsshowSecondGrid=offenceManagementService.getOffenceDetailOnSecondGrid(code);
		offenceMangDTO.setNoOfAttempts(null);
		offenceMangDTO.setPublicComplain("false");
		offenceMangDTO.setInvestigation("false");
		offenceMangDTO.setAmount("false");
		offenceMangDTO.setAction("false");
		offenceMangDTO.setNoOfDermitPoints(0);
		offenceMangDTO.setChargeAmount(0);
		offenceMangDTO.setActionTxt(null);
		offenceMangDTO.setStatus("A");
		addbuttDisable1=false;
		noOfAttemptDisable=false;
		setSucessMsg("Successfully saved.");
		RequestContext.getCurrentInstance().update("successSve");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		}
	}
	else {
		setErrorMsg("Please enter mandotory feilds.");
		RequestContext.getCurrentInstance().update("frmrequiredField");
		RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		
		
	}
	
	}	
	
}
public void clearButAction1() {
	//clearButAction();
	offenceMangDTO = new OffenceManagementDTO();
	addbuttDisable=false;
	showList=true;
	showWhenAdd=false;
	offenceCodeDetails=offenceManagementService.getOffenceCodeDataForGrid();
	offenceDetailsshowSecondGrid = new ArrayList<>();
	addbuttDisable1=false;
	noOfAttemptDisable=false;
	offenceDetailsshowSecondGrid=offenceManagementService.getOffenceDetailOnSecondGrid(code);
	editMode= false;
	
}

public boolean isNoOfAttemptDisable() {
	return noOfAttemptDisable;
}
public void setNoOfAttemptDisable(boolean noOfAttemptDisable) {
	this.noOfAttemptDisable = noOfAttemptDisable;
}
public void editButActionSecond() {
	editModeSecond=true;
	noOfAttemptDisable=true;
	noOfAttemptForEdit=selectDTOEdit.getNoOfAttempts();
	offenceMangDTO.setNoOfAttempts(selectDTOEdit.getNoOfAttempts());
	if(selectDTOEdit.getIsAmountAllowed().equals("YES")) {
	offenceMangDTO.setAmount("true");
	}
	else if(selectDTOEdit.getIsAmountAllowed().equals("NO")) {
		offenceMangDTO.setAmount("false");
	}
	else {
		
		offenceMangDTO.setAmount(null);
	}
	
	
	if(selectDTOEdit.getIsActionAllowed().equals("YES")) {
	offenceMangDTO.setAction("true");
	}
	else if(selectDTOEdit.getIsActionAllowed().equals("NO")) {
		offenceMangDTO.setAction("false");
	}
	else {
		
		offenceMangDTO.setAmount(null);
	}
	
	offenceMangDTO.setChargeAmount(selectDTOEdit.getChargeAmount());
	offenceMangDTO.setActionTxt(selectDTOEdit.getActionTxt());
	offenceMangDTO.setNoOfDermitPoints(selectDTOEdit.getNoOfDermitPoints());
	if( selectDTOEdit.getStatus().equalsIgnoreCase("Active")) {
	offenceMangDTO.setStatus("A");
	}
	else if(selectDTOEdit.getStatus().equalsIgnoreCase("Inactive")) {
		offenceMangDTO.setStatus("I");
	    }
	if(selectDTOEdit.getIsPublicComplain().equalsIgnoreCase("YES")) {
	offenceMangDTO.setPublicComplain("true");
	}
	else {
		offenceMangDTO.setPublicComplain("false");
	}
	
	if(selectDTOEdit.getIsInvestigation().equalsIgnoreCase("YES")) {
		offenceMangDTO.setInvestigation("true");
		}
		else {
			offenceMangDTO.setInvestigation("false");
		}
	addbuttDisable1=false;
}

public boolean isShowList() {
	return showList;
}
public void setShowList(boolean showList) {
	this.showList = showList;
}
public boolean isShowWhenAdd() {
	return showWhenAdd;
}
public void setShowWhenAdd(boolean showWhenAdd) {
	this.showWhenAdd = showWhenAdd;
}
	public StreamedContent getFiles() {
		return files;
	}


	public void setFiles(StreamedContent files) {
		this.files = files;
	}




	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public OffenceManagementService getOffenceManagementService() {
		return offenceManagementService;
	}
	public void setOffenceManagementService(OffenceManagementService offenceManagementService) {
		this.offenceManagementService = offenceManagementService;
	}
	public OffenceManagementDTO getOffenceMangDTO() {
		return offenceMangDTO;
	}
	public void setOffenceMangDTO(OffenceManagementDTO offenceMangDTO) {
		this.offenceMangDTO = offenceMangDTO;
	}
	public List<OffenceManagementDTO> getOffenceCodeList() {
		return offenceCodeList;
	}
	public void setOffenceCodeList(List<OffenceManagementDTO> offenceCodeList) {
		this.offenceCodeList = offenceCodeList;
	}
	public OffenceManagementDTO getOfenceDesDet() {
		return ofenceDesDet;
	}
	public void setOfenceDesDet(OffenceManagementDTO ofenceDesDet) {
		this.ofenceDesDet = ofenceDesDet;
	}
	public boolean isAddbuttDisable() {
		return addbuttDisable;
	}
	public void setAddbuttDisable(boolean addbuttDisable) {
		this.addbuttDisable = addbuttDisable;
	}
	public OffenceManagementDTO getSelectDTO() {
		return selectDTO;
	}
	public void setSelectDTO(OffenceManagementDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public List<OffenceManagementDTO> getOffenceCodeDetails() {
		return offenceCodeDetails;
	}
	public void setOffenceCodeDetails(List<OffenceManagementDTO> offenceCodeDetails) {
		this.offenceCodeDetails = offenceCodeDetails;
	}
	public void setOffenceCodeDetails(ArrayList<OffenceManagementDTO> offenceCodeDetails) {
		this.offenceCodeDetails = offenceCodeDetails;
	}
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}
	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}
	public String getOffenceCodeStr() {
		return offenceCodeStr;
	}
	public void setOffenceCodeStr(String offenceCodeStr) {
		this.offenceCodeStr = offenceCodeStr;
	}
	public String getSucessMsg() {
		return sucessMsg;
	}
	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}
	public boolean isEditMode() {
		return editMode;
	}
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
	public boolean isAddOffenDet() {
		return addOffenDet;
	}
	public void setAddOffenDet(boolean addOffenDet) {
		this.addOffenDet = addOffenDet;
	}
	public List<OffenceManagementDTO> getOffenceDetailsshowSecondGrid() {
		return offenceDetailsshowSecondGrid;
	}
	public void setOffenceDetailsshowSecondGrid(List<OffenceManagementDTO> offenceDetailsshowSecondGrid) {
		this.offenceDetailsshowSecondGrid = offenceDetailsshowSecondGrid;
	}
	public OffenceManagementDTO getSelectDTOEdit() {
		return selectDTOEdit;
	}
	public void setSelectDTOEdit(OffenceManagementDTO selectDTOEdit) {
		this.selectDTOEdit = selectDTOEdit;
	}
	public boolean isEditModeSecond() {
		return editModeSecond;
	}
	public void setEditModeSecond(boolean editModeSecond) {
		this.editModeSecond = editModeSecond;
	}
	public boolean isAddbuttDisable1() {
		return addbuttDisable1;
	}
	public void setAddbuttDisable1(boolean addbuttDisable1) {
		this.addbuttDisable1 = addbuttDisable1;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<String> getDuplicateOffenceCodeList() {
		return duplicateOffenceCodeList;
	}
	public void setDuplicateOffenceCodeList(List<String> duplicateOffenceCodeList) {
		this.duplicateOffenceCodeList = duplicateOffenceCodeList;
	}
	public List<OffenceManagementDTO> getAttempsList() {
		return attempsList;
	}
	public void setAttempsList(List<OffenceManagementDTO> attempsList) {
		this.attempsList = attempsList;
	}
	public String getNoOfAttemptForEdit() {
		return noOfAttemptForEdit;
	}
	public void setNoOfAttemptForEdit(String noOfAttemptForEdit) {
		this.noOfAttemptForEdit = noOfAttemptForEdit;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	






}
