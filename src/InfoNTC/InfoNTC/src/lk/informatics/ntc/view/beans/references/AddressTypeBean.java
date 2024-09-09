package lk.informatics.ntc.view.beans.references;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lk.informatics.ntc.view.beans.references.templates.CodeDescriptionTemplateBean;

 

 


@ManagedBean(name = "addressTypeBean")
@ViewScoped
public class AddressTypeBean extends CodeDescriptionTemplateBean {

	private static final long serialVersionUID = -5195585990103169578L;
	
	private String TABLE_NAME="NT_R_ADDRESS_TYPE";
	private String HEADER_LABLE="address.type.function.name";
	private String CODE_LABLE="code.lable";
	private String DESCRIPTION_ENGLISH_LABLE="eng.description.lable";
	private String DESCRIPTION_SINHALA_LABLE="sin.description.lable";
	private String DESCRIPTION_TAMIL_LABLE="tam.description.lable";
	
	private boolean CODE_MANDATORY=true;
	private boolean DESCRIPTION_MANDATORY=true;
	
	
	@PostConstruct
    public void init() {
        onPageLoad();
    }
	 
	
	
	@Override
	public void isCodeMandatory() {	
        setCodeMandatory(CODE_MANDATORY);
	}

	
	@Override
	public void isDescriptionMandatory() {
		 setDescriptionMandatory(DESCRIPTION_MANDATORY);
	}


	@Override
	public void setTableName() {
		setTABLE_NAME(TABLE_NAME);
	}


	@Override
	public void setFunctionName() {
		setFunctionHeader(HEADER_LABLE);
		 
	}

	@Override
	public void setCodeLable() {
		setCodeLabel(CODE_LABLE);
	}


	@Override
	public void setDescriptionLable() {
		setDescription_english_Label(DESCRIPTION_ENGLISH_LABLE);
		setDescription_sinhala_Label(DESCRIPTION_SINHALA_LABLE);
		setDescription_tamil_Label(DESCRIPTION_TAMIL_LABLE);
		 
	}
	
	
	

}
