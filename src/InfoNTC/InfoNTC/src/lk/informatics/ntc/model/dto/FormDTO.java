package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class FormDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String formId;
	private String formDescription;
	private String surveyNo; 
	private String surveyType;
	private String surveyMethod;

	
	private String surveyFormNo;
	private String formulaName;
	private String formulaNameCode;
	private String formulaNameDescription;
	private String fieldName;
	private String operator;
	private String value;
	private String formula;
	private String formulaTemplateId;
	private String formulaId;
	private int formulaCount;
	
	
	private MidPointSurveyDTO midpointSurvey;
	
	//pathum
	
	private String displayFormulaName;
	private String surveyReqNo;
	
	//sasini
	private String surveyTypeDes;
	private String surveyMethodDes;
	
	
	//Getters and setters
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormDescription() {
		return formDescription;
	}
	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}
	public String getSurveyNo() {
		return surveyNo;
	}
	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}
	public String getSurveyType() {
		return surveyType;
	}
	public void setSurveyType(String surveyType) {
		this.surveyType = surveyType;
	}
	public String getSurveyMethod() {
		return surveyMethod;
	}
	public void setSurveyMethod(String surveyMethod) {
		this.surveyMethod = surveyMethod;
	}
	public String getSurveyFormNo() {
		return surveyFormNo;
	}
	public void setSurveyFormNo(String surveyFormNo) {
		this.surveyFormNo = surveyFormNo;
	}
	public String getFormulaName() {
		return formulaName;
	}
	public void setFormulaName(String formulaName) {
		this.formulaName = formulaName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getFormulaNameCode() {
		return formulaNameCode;
	}
	public void setFormulaNameCode(String formulaNameCode) {
		this.formulaNameCode = formulaNameCode;
	}
	public String getFormulaNameDescription() {
		return formulaNameDescription;
	}
	public void setFormulaNameDescription(String formulaNameDescription) {
		this.formulaNameDescription = formulaNameDescription;
	}
	public String getFormulaId() {
		return formulaId;
	}
	public void setFormulaId(String formulaId) {
		this.formulaId = formulaId;
	}
	public String getFormulaTemplateId() {
		return formulaTemplateId;
	}
	public void setFormulaTemplateId(String formulaTemplateId) {
		this.formulaTemplateId = formulaTemplateId;
	}
	public MidPointSurveyDTO getMidpointSurvey() {
		return midpointSurvey;
	}
	public void setMidpointSurvey(MidPointSurveyDTO midpointSurvey) {
		this.midpointSurvey = midpointSurvey;
	}
	public String getDisplayFormulaName() {
		return displayFormulaName;
	}
	public void setDisplayFormulaName(String displayFormulaName) {
		this.displayFormulaName = displayFormulaName;
	}
	public String getSurveyReqNo() {
		return surveyReqNo;
	}
	public void setSurveyReqNo(String surveyReqNo) {
		this.surveyReqNo = surveyReqNo;
	}
	public int getFormulaCount() {
		return formulaCount;
	}
	public void setFormulaCount(int formulaCount) {
		this.formulaCount = formulaCount;
	}
	public String getSurveyTypeDes() {
		return surveyTypeDes;
	}
	public void setSurveyTypeDes(String surveyTypeDes) {
		this.surveyTypeDes = surveyTypeDes;
	}
	public String getSurveyMethodDes() {
		return surveyMethodDes;
	}
	public void setSurveyMethodDes(String surveyMethodDes) {
		this.surveyMethodDes = surveyMethodDes;
	}
	
	
	
	
	
	

	
	
	
	

}
