package lk.informatics.ntc.view.beans.references.templates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.ValueTypeDTO;
import lk.informatics.ntc.model.service.ReferenceDataService;
import lk.informatics.ntc.view.beans.SessionBackingBean;
import lk.informatics.ntc.view.util.SpringApplicationContex;

 

 

@ManagedBean(name = "valueTypeTemplate")
@ViewScoped
public abstract class ValueTypeTemplateBean implements Serializable {

	private static final long serialVersionUID = -2983817229956621158L;
	
	private ValueTypeDTO valueTypeDTO;
	public  boolean valueMandatory;
	public  boolean typeMandatory;
	private ReferenceDataService referenceDataService;
	private ValueTypeDTO deleteValueTypeDTO;
	
	public String functionHeader=null;
	public String TABLE_NAME=null;
	public String valueLabel=null;
	public String typeLabel=null;
	public String saveButtonLabel=null;
	public String clearButtonLabel=null;
	public String searchButtonLabel=null;
	
	public List<ValueTypeDTO> searchedData= new ArrayList<ValueTypeDTO>();
	
	
	
	@ManagedProperty("#{msg}")
	public ResourceBundle bundle; 
	
	
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
	
	
	
	
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}



	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}



	public ResourceBundle getBundle() {
		return bundle;
	}



	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}



	public void onPageLoad(){
		 
		 isValueMandatory();
		 isTypeMandatory();
	     setTableName();
	     setFunctionName();
	     setValueLable();
	     setTypeLable();
	  
	     
	     this.saveButtonLabel=bundle.getString("reference.page.save.button.label");
	     this.clearButtonLabel=bundle.getString("reference.page.clear.button.label");
	     this.searchButtonLabel=bundle.getString("reference.page.clear.button.search");
	      
	}
	
	
	
	@javax.annotation.PostConstruct
	public void init() {
		referenceDataService = (ReferenceDataService) SpringApplicationContex.getBean("referenceDataService");
	}
	
	public ValueTypeTemplateBean(){
		valueTypeDTO=new ValueTypeDTO();
	}
	
	
	public void checkDuplicate(){
		RequestContext context = RequestContext.getCurrentInstance();
		try{
			boolean duplicate= referenceDataService.isCodeDuplicate(valueTypeDTO.getValue(), TABLE_NAME);
			if(duplicate){
				context.execute("PF('duplicateCode').show();");
				return;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}


    


	public abstract void isValueMandatory();
	public abstract void isTypeMandatory();
    public abstract void setTableName();
    public abstract void setFunctionName();
    public abstract void setValueLable();
    public abstract void setTypeLable();
     

	
	public void saveRecord(){
		 RequestContext context = RequestContext.getCurrentInstance();
		
		 boolean valid= checkEmptyFields();
		 if(!valid){
			 return;
		 }
		 
		 int response = referenceDataService.saveValueTypeRecord(valueTypeDTO, TABLE_NAME, sessionBackingBean.getLoginUser());
		 if(response < 0){
			 context.execute("PF('saveError').show();");
			 return;
		 }else{
			 if(response==1){
				 context.execute("PF('duplicateCode').show();");
				 return;
			 }else{
				 if(response==2){
					 context.execute("PF('duplicateDesc').show();");
					 return;
				 }else{
					 context.execute("PF('saveSuccess').show();");
					 if(valueTypeDTO.isSearchedRecord() && !valueTypeDTO.isFreshRecord()){ // edit mode fro query record
						 valueTypeDTO= new ValueTypeDTO();
						 searchAction();
					 }else{
						 if(valueTypeDTO.isFreshRecord()){       // edit mode for new record
							
							 for(ValueTypeDTO obj:searchedData){
								 if(obj.getValue().equalsIgnoreCase(valueTypeDTO.getValue())){
									 obj.setType(valueTypeDTO.getType());
								 }
							 }
							 valueTypeDTO= new ValueTypeDTO();
						 }else{
							 valueTypeDTO.setSearchedRecord(true); // new insert
							 valueTypeDTO.setFreshRecord(true);
							 searchedData.add(valueTypeDTO);
							 valueTypeDTO= new ValueTypeDTO();
						 }
						 
						
					 }
					 
				 }
			 }
		 }
	}
	
	
	
	public void searchAction(){
		searchedData= referenceDataService.searchValueTypeRecords(valueTypeDTO, TABLE_NAME);
	}
	
	
	public void deleteAction(){
		valueTypeDTO=new ValueTypeDTO();
		RequestContext context = RequestContext.getCurrentInstance();
		if(deleteValueTypeDTO!=null){
			
			int result= referenceDataService.deleteValueTypeRecord(deleteValueTypeDTO, TABLE_NAME);
			 if(result < 0){
				 context.execute("PF('deleteError').show();");
				 deleteValueTypeDTO=null;
				 return;
			 }else{
				 context.execute("PF('deletesuccess').show();");
				 
			 	 searchAction();
				 deleteValueTypeDTO=null;
				 return;
			 }
		}else{
			 context.execute("PF('deleteError').show();");
			 deleteValueTypeDTO=null;
			 return;
		}
	}
	
	
	public void editAction(ValueTypeDTO editedcodeDescriptionDTO){
		this.valueTypeDTO=editedcodeDescriptionDTO;
	}
	
	
	public boolean checkEmptyFields(){
		boolean nullfound=false;
		if(valueMandatory){
			if(valueTypeDTO.getValue()==null || valueTypeDTO.getValue().trim().equalsIgnoreCase("")){
				nullfound=true;
			}
		}
		if(typeMandatory){
			if(valueTypeDTO.getType()==null || valueTypeDTO.getType().trim().equalsIgnoreCase("")){
				nullfound=true;
			}
		}
		
		if(nullfound){
			 RequestContext context = RequestContext.getCurrentInstance();
			 context.execute("PF('emptyFields').show();");
			 return false;
		}
		return true;
	}
	
	
	
	
	 public void clearForm(){
		 valueTypeDTO=new ValueTypeDTO();
		 searchedData= new ArrayList<ValueTypeDTO>();
		 deleteValueTypeDTO=null;
	 }

	public ReferenceDataService getReferenceDataService() {
		return referenceDataService;
	}

	public void setReferenceDataService(ReferenceDataService referenceDataService) {
		this.referenceDataService = referenceDataService;
	}



	public String getFunctionHeader() {
		return functionHeader;
	}

	public void setFunctionHeader(String functionHeader) {
		this.functionHeader = bundle.getString(functionHeader);
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public void setTABLE_NAME(String tABLE_NAME) {
		TABLE_NAME = tABLE_NAME;
	}

	public String getValueLabel() {
		return valueLabel;
	}

	public void setValueLabel(String valueLabel) {
		this.valueLabel = bundle.getString(valueLabel);
		 
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = bundle.getString(typeLabel);
		 
	}



	public String getSaveButtonLabel() {
		return saveButtonLabel;
	}



	public void setSaveButtonLabel(String saveButtonLabel) {
		this.saveButtonLabel = saveButtonLabel;
	}



	public String getClearButtonLabel() {
		return clearButtonLabel;
	}



	public void setClearButtonLabel(String clearButtonLabel) {
		this.clearButtonLabel = clearButtonLabel;
	}



	public String getSearchButtonLabel() {
		return searchButtonLabel;
	}



	public void setSearchButtonLabel(String searchButtonLabel) {
		this.searchButtonLabel = searchButtonLabel;
	}



	public List<ValueTypeDTO> getSearchedData() {
		return searchedData;
	}



	public void setSearchedData(List<ValueTypeDTO> searchedData) {
		this.searchedData = searchedData;
	}


	public void setValueMandatory(boolean valueMandatory) {
		this.valueMandatory = valueMandatory;
	}


	public void setTypeMandatory(boolean typeMandatory) {
		this.typeMandatory = typeMandatory;
	}



	public ValueTypeDTO getDeleteValueTypeDTO() {
		return deleteValueTypeDTO;
	}



	public void setDeleteValueTypeDTO(ValueTypeDTO deleteValueTypeDTO) {
		this.deleteValueTypeDTO = deleteValueTypeDTO;
	}



	public ValueTypeDTO getValueTypeDTO() {
		return valueTypeDTO;
	}



	public void setValueTypeDTO(ValueTypeDTO valueTypeDTO) {
		this.valueTypeDTO = valueTypeDTO;
	}
	 
	
	
	
	
	
	
	
	
	
	
	
}
