package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.MidPointSurveyDTO;
import lk.informatics.ntc.model.dto.TimeBandDTO;

public interface EnterSurveyDataService extends Serializable {

	public List<FormDTO> retrieveFormsList();

	public FormDTO retrieveFormDataFromFormId(String selectedFormID);

	public List<IndicatorsDTO> retrieveIndicatorsForFormId(String formId);

	public void insertDataIntoNt_t_survey_print_tracking(MidPointSurveyDTO midPointSurvey, String loginUser);

	public void insertDataIntoNt_t_indicators(FormDTO formDto, String loginUser,
			List<List<IndicatorsDTO>> indicatorList, boolean editValue);

	public List<List<IndicatorsDTO>> retrieveIndicatorListDataFromFormId(String selectedFormID);

	public void deleteSelectedTableRow(IndicatorsDTO selectedDto);

	String getSIRFromSIPNumber(String sipReqNumber);

	List<TimeBandDTO> retrieveTimeBandList();
}
