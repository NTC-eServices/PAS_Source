package lk.informatics.ntc.view.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.view.util.AES;
import lk.informatics.ntc.view.util.PropertyReader;

@ManagedBean(name = "setupDbCredentionalBackingBean")
@ViewScoped
public class SetupDbCredentionalBackingBean {
	private String errorMessage;
	private String strUserName = null;
	private String strPassword = null;
	private String strEnUserName = null;
	private String strEnPassword = null;
	private String strSecrectKey = null;
	
	@PostConstruct
	public void init() {
		try {
			strSecrectKey = PropertyReader.getPropertyValue("password.secretKey");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	
	public void proceed(){
		if(!strUserName.isEmpty()){
			if(!strPassword.isEmpty()){
					strEnUserName = AES.encrypt(strUserName, strSecrectKey);
					strEnPassword = AES.encrypt(strPassword, strSecrectKey);			
			}
			else
			{
				setErrorMessage("Please enter Password ");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
			
		}
		else
		{
			setErrorMessage("Please enter Username ");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		
	}

	public void clear(){
		errorMessage = null; 
		strUserName = null;
		strPassword = null;
		strEnUserName = null;
		strEnPassword = null;
	}

	
	//getters and setters
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getStrUserName() {
		return strUserName;
	}

	public void setStrUserName(String strUserName) {
		this.strUserName = strUserName;
	}

	public String getStrPassword() {
		return strPassword;
	}

	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}

	public String getStrEnUserName() {
		return strEnUserName;
	}

	public void setStrEnUserName(String strEnUserName) {
		this.strEnUserName = strEnUserName;
	}

	public String getStrEnPassword() {
		return strEnPassword;
	}

	public void setStrEnPassword(String strEnPassword) {
		this.strEnPassword = strEnPassword;
	}

	public String getStrSecrectKey() {
		return strSecrectKey;
	}

	public void setStrSecrectKey(String strSecrectKey) {
		this.strSecrectKey = strSecrectKey;
	}
	

}
