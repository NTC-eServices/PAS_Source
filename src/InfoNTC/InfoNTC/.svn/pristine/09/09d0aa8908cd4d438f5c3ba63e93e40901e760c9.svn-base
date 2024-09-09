package lk.informatics.ntc.view.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "activePrvAppBackingBean")
@ViewScoped

public class activePrvAppBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
	private CommonService commonService;
	private String errorMessage, successMessage;
	private String strDetAppNo = null;
	private String strActAppNo = null;
	private Boolean boolAvai = false;
		
	@PostConstruct
	public void init() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		strDetAppNo = null;
		strActAppNo = null;
		boolAvai = false;
	}
	
	public void proceed(){
		if(!strActAppNo.isEmpty() && !strDetAppNo.isEmpty())
		{
			//Validate Delete Application
			if(isDelApplicationValidator())
			{
				//Validate Active Application
				if(isActApplicationValidator())
				{
					//check whether permit number is equal for both application numbers.
					Boolean boolStatus = commonService.IsSamePermitNo(strActAppNo, strDetAppNo);
					if(boolStatus)
					{
						//check whether delete application is payment approved already. (PM301 - C) 
						boolAvai = commonService.IsPaymentProcessOngoing(strDetAppNo);
						if(!boolAvai)
						{
							Boolean result = commonService.updateApplication(strActAppNo, strDetAppNo, sessionBackingBean.getLoginUser());
							if(result)
							{
								setSuccessMessage( strActAppNo + ' ' + "Application No. Activated Successfully.");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								strDetAppNo = null;
								strActAppNo = null;
							}
							else
							{
								setErrorMessage("Cannot update the Application No.");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								strDetAppNo = null;
								strActAppNo = null;
							}
						}
						else
						{
							setErrorMessage("Entered Delete Application no. related Voucher already Approved");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							strDetAppNo = null;
						}
					}	
					else
					{
						setErrorMessage("You have entered different Permit No. related Application Numbers.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						strDetAppNo = null;
						strActAppNo = null;
					}
				}
				else{
					setErrorMessage("Please enter Valid Application Numbers ");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					strActAppNo=null;
				}
			}
			else
			{
				setErrorMessage("Please enter Valid Application Numbers ");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				
				strDetAppNo=null;	
			}			
						
		}
		else
		{
			setErrorMessage("Please enter Application Numbers ");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		
	}
	
	public void clear(){
		strActAppNo = null;
		strDetAppNo = null;
	}

	public boolean isDelApplicationValidator(){
		boolean isDelValidate=true;
		Boolean boolVal = commonService.IsAppAvailableForDel(strDetAppNo);
		if (!boolVal)
		{
			/*setErrorMessage("Please enter Valid Application Numbers ");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");		
			strDetAppNo=null;*/
			isDelValidate=false;
		}
		return isDelValidate;
	}

	public boolean isActApplicationValidator(){
		boolean isActiveValidate=true;
		Boolean boolVal = commonService.IsAppAvailable(strActAppNo);
		if (!boolVal)
		{
			/*setErrorMessage("Please enter Valid Application Numbers ");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			strActAppNo=null;*/
			isActiveValidate=false;
		}
		return isActiveValidate;
	}
	
	
	//getters and setters	
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

	public String getStrDetAppNo() {
		return strDetAppNo;
	}

	public void setStrDetAppNo(String strDetAppNo) {
		this.strDetAppNo = strDetAppNo;
	}

	public String getStrActAppNo() {
		return strActAppNo;
	}

	public void setStrActAppNo(String strActAppNo) {
		this.strActAppNo = strActAppNo;
	}

	public Boolean getBoolAvai() {
		return boolAvai;
	}

	public void setBoolAvai(Boolean boolAvai) {
		this.boolAvai = boolAvai;
	}
	
	
	
}
