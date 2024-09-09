package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.MidPointSurveyDTO;

public interface GenerateDraftSurveyService extends Serializable {

	public List<FormDTO> retrieveFormsList();

	public FormDTO retrieveFormDataFromFormId(String selectedFormID);

	public List<IndicatorsDTO> retrieveIndicatorsForFormId(String formId);

	public void insertDataIntoNt_t_survey_print_tracking(MidPointSurveyDTO midPointSurvey, String loginUser);

	// Draft survey form report
	public List<Integer> insertEnteredRows(String selectedFormID, List<Integer> newInsert);

	public void deleteEnterdRows(String selectedFormID);

}
