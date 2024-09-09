package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.GenerateSurveyFormDTO;

public interface GenerateSurveyFormService {
	public List<GenerateSurveyFormDTO> getApprovedSurveyNoList();

	public List<GenerateSurveyFormDTO> getFormIDList();

	public List<GenerateSurveyFormDTO> getFieldTypeList();

	public List<GenerateSurveyFormDTO> getValidationMethodList();

	public List<GenerateSurveyFormDTO> getFieldDefinitionList();

	public GenerateSurveyFormDTO getFormDetails(GenerateSurveyFormDTO generateSurveyFormDTO);

	public List<GenerateSurveyFormDTO> getIndicatorsListFromTempID(String copyTemplateID);

	public List<GenerateSurveyFormDTO> getIndicatorsList(GenerateSurveyFormDTO generateSurveyFormDTO);

	public List<GenerateSurveyFormDTO> getIndicatorsValueList(GenerateSurveyFormDTO generateSurveyFormDTO);

	public boolean check_FormID_duplicate(String formID);

	public boolean check_LOV_code_duplicate(String LOV_code);

	public boolean saveForm(GenerateSurveyFormDTO generateSurveyFormDTO, List<GenerateSurveyFormDTO> indicators_list,
			String user);

	public boolean updateForm(GenerateSurveyFormDTO generateSurveyFormDTO, List<GenerateSurveyFormDTO> indicators_list,
			String user);

	public void updateSurveyTaskDetails(String surveyNo, String taskCode, String taskStatus);

	public String getTaskCode(String surveyNo);
}
