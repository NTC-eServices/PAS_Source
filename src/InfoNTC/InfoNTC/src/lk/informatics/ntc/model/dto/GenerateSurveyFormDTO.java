package lk.informatics.ntc.model.dto;

import java.util.List;

public class GenerateSurveyFormDTO {

	private Long seqNo;
	private String surveyNo;
	private String surveyType;
	private String surveyMethod;
	private String surveyType_des;
	private String surveyMethod_des;
	private String fieldDefinition;
	private String fieldDefinition_des;
	private String displayAfter;
	private int displayOrder;// value of the field
	private String formID;
	private String copyTemplateID;
	private String formDescription;
	private String fieldName;
	private String fieldName_sinhala;
	private String fieldName_tamil;
	private String fieldType;
	private String fieldType_des;
	private String validationMethod;
	private String validationMethod_des;
	private int fieldLength;
	private Boolean active;
	private Boolean mandatoryField;
	private String headerLabel;
	private String headerLabel_sinhala;
	private String headerLabel_tamil;

	private Long LOV_seqNo;
	private String LOV_code;
	private String LOV_description;
	private String LOV_description_sinhala;
	private String LOV_description_tamil;

	private List<GenerateSurveyFormDTO> indicator_values_list;

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

	public String getFormID() {
		return formID;
	}

	public void setFormID(String formID) {
		this.formID = formID;
	}

	public String getCopyTemplateID() {
		return copyTemplateID;
	}

	public void setCopyTemplateID(String copyTemplateID) {
		this.copyTemplateID = copyTemplateID;
	}

	public String getFormDescription() {
		return formDescription;
	}

	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName_sinhala() {
		return fieldName_sinhala;
	}

	public void setFieldName_sinhala(String fieldName_sinhala) {
		this.fieldName_sinhala = fieldName_sinhala;
	}

	public String getFieldName_tamil() {
		return fieldName_tamil;
	}

	public void setFieldName_tamil(String fieldName_tamil) {
		this.fieldName_tamil = fieldName_tamil;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getValidationMethod() {
		return validationMethod;
	}

	public void setValidationMethod(String validationMethod) {
		this.validationMethod = validationMethod;
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getMandatoryField() {
		return mandatoryField;
	}

	public void setMandatoryField(Boolean mandatoryField) {
		this.mandatoryField = mandatoryField;
	}

	public String getHeaderLabel() {
		return headerLabel;
	}

	public void setHeaderLabel(String headerLabel) {
		this.headerLabel = headerLabel;
	}

	public String getHeaderLabel_sinhala() {
		return headerLabel_sinhala;
	}

	public void setHeaderLabel_sinhala(String headerLabel_sinhala) {
		this.headerLabel_sinhala = headerLabel_sinhala;
	}

	public String getHeaderLabel_tamil() {
		return headerLabel_tamil;
	}

	public void setHeaderLabel_tamil(String headerLabel_tamil) {
		this.headerLabel_tamil = headerLabel_tamil;
	}

	public String getLOV_code() {
		return LOV_code;
	}

	public void setLOV_code(String lOV_code) {
		LOV_code = lOV_code;
	}

	public String getLOV_description() {
		return LOV_description;
	}

	public void setLOV_description(String lOV_description) {
		LOV_description = lOV_description;
	}

	public String getSurveyType_des() {
		return surveyType_des;
	}

	public void setSurveyType_des(String surveyType_des) {
		this.surveyType_des = surveyType_des;
	}

	public String getSurveyMethod_des() {
		return surveyMethod_des;
	}

	public void setSurveyMethod_des(String surveyMethod_des) {
		this.surveyMethod_des = surveyMethod_des;
	}

	public String getFieldType_des() {
		return fieldType_des;
	}

	public void setFieldType_des(String fieldType_des) {
		this.fieldType_des = fieldType_des;
	}

	public String getValidationMethod_des() {
		return validationMethod_des;
	}

	public void setValidationMethod_des(String validationMethod_des) {
		this.validationMethod_des = validationMethod_des;
	}

	public List<GenerateSurveyFormDTO> getIndicator_values_list() {
		return indicator_values_list;
	}

	public void setIndicator_values_list(List<GenerateSurveyFormDTO> indicator_values_list) {
		this.indicator_values_list = indicator_values_list;
	}

	public String getFieldDefinition() {
		return fieldDefinition;
	}

	public void setFieldDefinition(String fieldDefinition) {
		this.fieldDefinition = fieldDefinition;
	}

	public String getFieldDefinition_des() {
		return fieldDefinition_des;
	}

	public void setFieldDefinition_des(String fieldDefinition_des) {
		this.fieldDefinition_des = fieldDefinition_des;
	}

	public String getLOV_description_sinhala() {
		return LOV_description_sinhala;
	}

	public void setLOV_description_sinhala(String lOV_description_sinhala) {
		LOV_description_sinhala = lOV_description_sinhala;
	}

	public String getLOV_description_tamil() {
		return LOV_description_tamil;
	}

	public void setLOV_description_tamil(String lOV_description_tamil) {
		LOV_description_tamil = lOV_description_tamil;
	}

	public String getDisplayAfter() {
		return displayAfter;
	}

	public void setDisplayAfter(String displayAfter) {
		this.displayAfter = displayAfter;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}

	public Long getLOV_seqNo() {
		return LOV_seqNo;
	}

	public void setLOV_seqNo(Long lOV_seqNo) {
		LOV_seqNo = lOV_seqNo;
	}
	
}
