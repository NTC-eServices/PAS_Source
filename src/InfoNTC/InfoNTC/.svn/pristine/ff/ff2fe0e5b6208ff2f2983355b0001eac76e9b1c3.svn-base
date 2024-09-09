package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import lk.informatics.ntc.model.dto.AnalyzeSurveryDataDTO;
import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.TimeBandHoursDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.EnterSurveyDataService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "analyzeSurveyDataBean")
@ViewScoped
public class AnalyzeSurveyDataBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private SurveyService surveyService;
	private EnterSurveyDataService enterSurveyDataService;
	public CommonService commonService;
	private FormDTO formDTO; // store form fields

	private List<String> formIdList; // drpd-FormId
	private List<FormDTO> formulaNameList; // drpd-FormName
	private List<IndicatorsDTO> fieldNameList; // drpd-FieldName
	private List<String> operatorList; // drpd-Operator
	private List<String> formulaIdList; // drpd-CopyFormId
	private List<FormDTO> tbl_FormulaList; // tbl-Formula

	private String errorMsg;
	private String alertMsg;
	private String successMsg;
	private int activeTabIndex;
	private boolean renderTabView;

	private boolean disableOperetor;
	private boolean disableFieldName;
	private boolean disableValue;

	private boolean isSumStart;
	private boolean isCountStart;
	private boolean disableAdd;
	private boolean disableSurveyCompleted;
	private boolean disableAnalyzedData;
	private boolean disableTabAnalyadzedData;
	private boolean disableTabDisplayGraph;

	private String temptSelectedOperator = "invalid";
	private String surveyNo;
	private String surveyReqNo;
	private HashMap mMap;
	private List<HashMap> hashMapList;
	private List<HashMap> tempHashMapList;
	private List<String> answersList;
	private List<String> operationsList;

	private List<AnalyzeSurveryDataDTO> analyzeSurveryDataDTOList;
	private List<List<AnalyzeSurveryDataDTO>> analyzeSurveryDataList;
	private int columnCount;
	private List<Integer> integerIndicatorList;
	private List<AnalyzeSurveryDataDTO> formulaList;

	private TimeBandHoursDTO timeBandHoursDTO;

	private LineChartModel lineModel1;
	private LineChartModel lineModel2;

	private LineChartModel graphAnalyzedData;

	@PostConstruct
	public void init() {
		formDTO = new FormDTO();
		formIdList = new ArrayList<String>();
		formulaNameList = new ArrayList<FormDTO>();
		fieldNameList = new ArrayList<IndicatorsDTO>();
		operatorList = new ArrayList<String>();
		formulaIdList = new ArrayList<String>();
		tbl_FormulaList = new ArrayList<FormDTO>();
		formulaIdList = new ArrayList<String>();
		answersList = new ArrayList<String>();
		operationsList = new ArrayList<String>();

		mMap = new HashMap<String, String>();
		hashMapList = new ArrayList<HashMap>();
		tempHashMapList = new ArrayList<HashMap>();
		timeBandHoursDTO = new TimeBandHoursDTO();

		analyzeSurveryDataDTOList = new ArrayList<AnalyzeSurveryDataDTO>();
		analyzeSurveryDataList = new ArrayList<List<AnalyzeSurveryDataDTO>>();
		columnCount = 0;
		integerIndicatorList = new ArrayList<Integer>();
		formulaList = new ArrayList<AnalyzeSurveryDataDTO>();

		loadValues();
		graphAnalyzedData = new LineChartModel();
		disableAdd = true;
		disableSurveyCompleted = false;
		disableAnalyzedData = true;

		disableTabAnalyadzedData = true;
		disableTabDisplayGraph = true;
		activeTabIndex = 0;

		if (commonService.checkTaskOnSurveyHisDetails(surveyNo, "SU006", "C")) {
			disableSurveyCompleted = true;
		}

		if (commonService.checkTaskOnSurveyHisDetails(surveyNo, "SU007", "O")) {
			disableAnalyzedData = false;
		}

	}

	private void loadValues() {
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		enterSurveyDataService = (EnterSurveyDataService) SpringApplicationContex.getBean("enterSurveyDataService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		formIdList = surveyService.get_Drpd_FormIdList();
		formulaNameList = surveyService.get_drpd_FormulaNameList();
		temptHardCode();

	}

	private void createLineModels() {

		graphAnalyzedData = initCategoryModel(analyzeSurveryDataList);
		graphAnalyzedData.setTitle("Category Chart");
		graphAnalyzedData.setLegendPosition("e");
		graphAnalyzedData.setShowPointLabels(true);
		graphAnalyzedData.getAxes().put(AxisType.X, new CategoryAxis("Time Bands"));
		Axis yAxis = graphAnalyzedData.getAxis(AxisType.Y);
		yAxis.setLabel("Formula Answers");

	}

	private LineChartModel initCategoryModel(List<List<AnalyzeSurveryDataDTO>> analyzeSurveryDataList) {

		LineChartModel model = new LineChartModel();

		for (int j = 0; j < columnCount; j++) {

			ChartSeries forOneFormula = new ChartSeries();
			int count = 0;
			boolean formula = false;
			for (List<AnalyzeSurveryDataDTO> timeband : analyzeSurveryDataList) {

				if (count == 0) {
					forOneFormula.setLabel(timeband.get(j).getFormulaName());
				}

				if (timeband.get(j).getAnswer() != null && !timeband.get(j).getAnswer().isEmpty()
						&& !timeband.get(j).getAnswer().contains(",") && timeband.get(j).getFormula().contains("SUM")) {

					forOneFormula.set(timeband.get(j).getTimeband(), Double.parseDouble(timeband.get(j).getAnswer()));
					formula = true;
				}
				count++;
			}

			if (formula) {
				model.addSeries(forOneFormula);
			}

		}

		return model;
	}

	public void temptHardCode() {
		operatorList.add("+");
		operatorList.add("-");
		operatorList.add("*");
		operatorList.add("/");
		operatorList.add(">");
		operatorList.add("<");
		operatorList.add("=");
		operatorList.add("SUM (");
		operatorList.add("COUNT (");

	}

	// 01
	public void onFormIdChange() {

		getDetailsByFormId();

		// load drpd-FieldNames
		fieldNameList = surveyService.get_drp_FieldNamesList(formDTO.getFormId());
		// load tbl-Formula list
		tbl_FormulaList = surveyService.get_tbl_formulaList(formDTO);

		disableOperetor = true;
		disableFieldName = true;
		disableValue = true;

		getADynamicRaw();
		graphAnalyzedData = new LineChartModel();
		disableSurveyCompleted = false;
		disableAnalyzedData = true;

		if (!commonService.checkTaskOnSurveyHisDetails(surveyNo, "SU007", "O")) {
			disableAnalyzedData = true;
		}

		if (commonService.checkTaskOnSurveyHisDetails(surveyNo, "SU006", "C")) {
			disableSurveyCompleted = true;
			disableAnalyzedData = false;
		}

	}

	// 02
	public void getDetailsByFormId() {

		formDTO = surveyService.getDetailsByFormId(formDTO);
		surveyReqNo = formDTO.getSurveyReqNo();
		surveyNo = formDTO.getSurveyNo();

		// get surveyTypeDes and surveyType method des
		formDTO.setSurveyTypeDes(surveyService.returnSurveyTypeDes(formDTO.getSurveyType()));
		formDTO.setSurveyMethodDes(surveyService.returnSurveyMethodDes(formDTO.getSurveyMethod()));

	}

	// 03
	public List<IndicatorsDTO> getADynamicRaw() {

		hashMapList = new ArrayList<HashMap>();

		// list of [list of values-of a field]
		List<List<IndicatorsDTO>> indDtoList = enterSurveyDataService
				.retrieveIndicatorListDataFromFormId(formDTO.getFormId());

		List<List<IndicatorsDTO>> raw = new ArrayList<List<IndicatorsDTO>>();

		for (int i = 0; i < indDtoList.get(1).size(); i++) {

			// iterate through fields
			for (List<IndicatorsDTO> mainListIndex : indDtoList) {

				List<IndicatorsDTO> rawIndex = new ArrayList<IndicatorsDTO>();
				IndicatorsDTO indexDTO = new IndicatorsDTO();
				int j = 0;
				// iterate through values
				for (IndicatorsDTO indexList : mainListIndex) {

					if (i == j) {
						mMap.put(indexList.getFieldNameEn(), indexList.getFieldInputVal());

						indexDTO.setFieldNameEn(indexList.getFieldNameEn());
						indexDTO.setFieldInputVal(indexList.getFieldInputVal());
						rawIndex.add(indexDTO);
						break;
					}

					j++;
				}

				raw.add(rawIndex);
			}
			if (mMap != null && !mMap.isEmpty()) {
				hashMapList.add(mMap);
			}
			mMap = new HashMap();

		}

		return null;

	}

	// 04

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void analyzeData() {

		timeBandHoursDTO = new TimeBandHoursDTO();
		formulaList = new ArrayList<AnalyzeSurveryDataDTO>();
		tbl_FormulaList = new ArrayList<FormDTO>();
		tempHashMapList = new ArrayList<HashMap>();
		analyzeSurveryDataDTOList = new ArrayList<AnalyzeSurveryDataDTO>();
		analyzeSurveryDataList = new ArrayList<List<AnalyzeSurveryDataDTO>>();

		tbl_FormulaList = surveyService.get_tbl_formulaList(formDTO);

		if (tbl_FormulaList.size() == 0) {
			alertMsg = "Insert formula to analyze data.";
			RequestContext.getCurrentInstance().update("frm_AnalyzeSurveyData");
			RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
		} else {
			columnCount = 0;
			columnCount = surveyService.getFormulaCount(formDTO);

			HashMap<String, String> hm = new HashMap<String, String>();

			int count = 0;

			List<HashMap> hashMapList1To2 = new ArrayList<HashMap>();
			List<HashMap> hashMapList2To3 = new ArrayList<HashMap>();
			List<HashMap> hashMapList3To4 = new ArrayList<HashMap>();
			List<HashMap> hashMapList4To5 = new ArrayList<HashMap>();
			List<HashMap> hashMapList5To6 = new ArrayList<HashMap>();
			List<HashMap> hashMapList6To7 = new ArrayList<HashMap>();
			List<HashMap> hashMapList7To8 = new ArrayList<HashMap>();
			List<HashMap> hashMapList8To9 = new ArrayList<HashMap>();
			List<HashMap> hashMapList9To10 = new ArrayList<HashMap>();
			List<HashMap> hashMapList10To11 = new ArrayList<HashMap>();
			List<HashMap> hashMapList11To12 = new ArrayList<HashMap>();
			List<HashMap> hashMapList12To13 = new ArrayList<HashMap>();
			List<HashMap> hashMapList13To14 = new ArrayList<HashMap>();
			List<HashMap> hashMapList14To15 = new ArrayList<HashMap>();
			List<HashMap> hashMapList15To16 = new ArrayList<HashMap>();
			List<HashMap> hashMapList16To17 = new ArrayList<HashMap>();
			List<HashMap> hashMapList17To18 = new ArrayList<HashMap>();
			List<HashMap> hashMapList18To19 = new ArrayList<HashMap>();
			List<HashMap> hashMapList19To20 = new ArrayList<HashMap>();
			List<HashMap> hashMapList20To21 = new ArrayList<HashMap>();
			List<HashMap> hashMapList21To22 = new ArrayList<HashMap>();
			List<HashMap> hashMapList22To23 = new ArrayList<HashMap>();
			List<HashMap> hashMapList23To24 = new ArrayList<HashMap>();
			List<HashMap> hashMapList24To1 = new ArrayList<HashMap>();

			// iterate through hash map
			for (Map<String, String> entry : hashMapList) {

				Iterator it = entry.entrySet().iterator();

				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();

					String key = (String) pair.getKey();
					String value = (String) pair.getValue();

					if (key != null && !key.isEmpty() && !key.trim().equalsIgnoreCase("")
							&& key.equalsIgnoreCase("Time Band")) {

						if (value != null && !value.isEmpty() && !value.trim().equalsIgnoreCase("")) {

							if (value.equalsIgnoreCase("01.00 - 02.00")) {
								hashMapList1To2.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList1To2(hashMapList1To2);
								break;
							}
							if (value.equalsIgnoreCase("02.00 - 03.00")) {
								hashMapList2To3.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList2To3(hashMapList2To3);
								break;
							}
							if (value.equalsIgnoreCase("03.00 - 04.00")) {
								hashMapList3To4.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList3To4(hashMapList3To4);
								break;
							}
							if (value.equalsIgnoreCase("04.00 - 05.00")) {
								hashMapList4To5.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList4To5(hashMapList4To5);
								break;
							}
							if (value.equalsIgnoreCase("05.00 - 06.00")) {
								hashMapList5To6.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList5To6(hashMapList5To6);
								break;
							}
							if (value.equalsIgnoreCase("06.00 - 07.00")) {
								hashMapList6To7.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList6To7(hashMapList6To7);
								break;
							}
							if (value.equalsIgnoreCase("07.00 - 08.00")) {
								hashMapList7To8.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList7To8(hashMapList7To8);
								break;
							}
							if (value.equalsIgnoreCase("08.00 - 09.00")) {
								hashMapList8To9.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList8To9(hashMapList8To9);
								break;
							}
							if (value.equalsIgnoreCase("09.00 - 10.00")) {
								hashMapList9To10.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList9To10(hashMapList9To10);
								break;
							}
							if (value.equalsIgnoreCase("10.00 - 11.00")) {
								hashMapList10To11.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList10To11(hashMapList10To11);
								break;
							}
							if (value.equalsIgnoreCase("11.00 - 12.00")) {
								hashMapList11To12.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList11To12(hashMapList11To12);
								break;
							}
							if (value.equalsIgnoreCase("12.00 - 13.00")) {
								hashMapList12To13.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList12To13(hashMapList12To13);
								break;
							}
							if (value.equalsIgnoreCase("13.00 - 14.00")) {
								hashMapList13To14.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList13To14(hashMapList13To14);
								break;
							}
							if (value.equalsIgnoreCase("14.00 - 15.00")) {
								hashMapList14To15.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList14To15(hashMapList14To15);
								break;
							}
							if (value.equalsIgnoreCase("15.00 - 16.00")) {
								hashMapList15To16.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList15To16(hashMapList15To16);
								break;
							}
							if (value.equalsIgnoreCase("16.00 - 17.00")) {
								hashMapList16To17.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList16To17(hashMapList16To17);
								break;
							}
							if (value.equalsIgnoreCase("17.00 - 18.00")) {
								hashMapList17To18.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList17To18(hashMapList17To18);
								break;
							}
							if (value.equalsIgnoreCase("18.00 - 19.00")) {
								hashMapList18To19.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList18To19(hashMapList18To19);
								break;
							}
							if (value.equalsIgnoreCase("19.00 - 20.00")) {
								hashMapList19To20.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList19To20(hashMapList19To20);
								break;
							}
							if (value.equalsIgnoreCase("20.00 - 21.00")) {
								hashMapList20To21.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList20To21(hashMapList20To21);
								break;
							}
							if (value.equalsIgnoreCase("21.00 - 22.00")) {
								hashMapList21To22.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList21To22(hashMapList21To22);
								break;
							}
							if (value.equalsIgnoreCase("22.00 - 23.00")) {
								hashMapList22To23.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList22To23(hashMapList22To23);
								break;
							}
							if (value.equalsIgnoreCase("23.00 - 24.00")) {
								hashMapList23To24.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList23To24(hashMapList23To24);
								break;
							}
							if (value.equalsIgnoreCase("24.00 - 1.00")) {
								hashMapList24To1.add(hashMapList.get(count));
								timeBandHoursDTO.setHashMapList24To1(hashMapList24To1);
								break;
							}
						}
					}
				}
				count++;
			}

			if (timeBandHoursDTO != null) {

				// get time band dto's lists one by one
				if (timeBandHoursDTO.getHashMapList1To2() != null && !timeBandHoursDTO.getHashMapList1To2().isEmpty()
						&& timeBandHoursDTO.getHashMapList1To2().size() != 0) {

					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList1To2();

					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();

						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("01.00 - 2.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}

				if (timeBandHoursDTO.getHashMapList2To3() != null && !timeBandHoursDTO.getHashMapList2To3().isEmpty()
						&& timeBandHoursDTO.getHashMapList2To3().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList2To3();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("02.00 - 3.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList3To4() != null && !timeBandHoursDTO.getHashMapList3To4().isEmpty()
						&& timeBandHoursDTO.getHashMapList3To4().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList3To4();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("03.00 - 4.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList4To5() != null && !timeBandHoursDTO.getHashMapList4To5().isEmpty()
						&& timeBandHoursDTO.getHashMapList4To5().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList4To5();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("04.00 - 5.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList5To6() != null && !timeBandHoursDTO.getHashMapList5To6().isEmpty()
						&& timeBandHoursDTO.getHashMapList5To6().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList5To6();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("05.00 - 6.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList6To7() != null && !timeBandHoursDTO.getHashMapList6To7().isEmpty()
						&& timeBandHoursDTO.getHashMapList6To7().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList6To7();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("06.00 - 7.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList7To8() != null && !timeBandHoursDTO.getHashMapList7To8().isEmpty()
						&& timeBandHoursDTO.getHashMapList7To8().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList7To8();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("07.00 - 8.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList8To9() != null && !timeBandHoursDTO.getHashMapList8To9().isEmpty()
						&& timeBandHoursDTO.getHashMapList8To9().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList8To9();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("08.00 - 9.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList9To10() != null && !timeBandHoursDTO.getHashMapList9To10().isEmpty()
						&& timeBandHoursDTO.getHashMapList9To10().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList9To10();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("09.00 - 10.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList10To11() != null
						&& !timeBandHoursDTO.getHashMapList10To11().isEmpty()
						&& timeBandHoursDTO.getHashMapList10To11().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList10To11();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("10.00 - 11.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList11To12() != null
						&& !timeBandHoursDTO.getHashMapList11To12().isEmpty()
						&& timeBandHoursDTO.getHashMapList11To12().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList11To12();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("11.00 - 12.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList12To13() != null
						&& !timeBandHoursDTO.getHashMapList12To13().isEmpty()
						&& timeBandHoursDTO.getHashMapList12To13().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList12To13();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("12.00 - 13.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList13To14() != null
						&& !timeBandHoursDTO.getHashMapList13To14().isEmpty()
						&& timeBandHoursDTO.getHashMapList13To14().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList13To14();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("13.00 - 14.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList14To15() != null
						&& !timeBandHoursDTO.getHashMapList14To15().isEmpty()
						&& timeBandHoursDTO.getHashMapList14To15().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList14To15();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("14.00 - 15.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList15To16() != null
						&& !timeBandHoursDTO.getHashMapList15To16().isEmpty()
						&& timeBandHoursDTO.getHashMapList15To16().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList15To16();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("15.00 - 16.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList16To17() != null
						&& !timeBandHoursDTO.getHashMapList16To17().isEmpty()
						&& timeBandHoursDTO.getHashMapList16To17().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList16To17();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("16.00 - 17.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList17To18() != null
						&& !timeBandHoursDTO.getHashMapList17To18().isEmpty()
						&& timeBandHoursDTO.getHashMapList17To18().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList17To18();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("17.00 - 18.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList18To19() != null
						&& !timeBandHoursDTO.getHashMapList18To19().isEmpty()
						&& timeBandHoursDTO.getHashMapList18To19().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList18To19();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("18.00 - 19.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList19To20() != null
						&& !timeBandHoursDTO.getHashMapList19To20().isEmpty()
						&& timeBandHoursDTO.getHashMapList19To20().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList19To20();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("19.00 - 20.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList20To21() != null
						&& !timeBandHoursDTO.getHashMapList20To21().isEmpty()
						&& timeBandHoursDTO.getHashMapList20To21().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList20To21();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("20.00 - 21.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList21To22() != null
						&& !timeBandHoursDTO.getHashMapList21To22().isEmpty()
						&& timeBandHoursDTO.getHashMapList21To22().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList21To22();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("21.00 - 22.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList22To23() != null
						&& !timeBandHoursDTO.getHashMapList22To23().isEmpty()
						&& timeBandHoursDTO.getHashMapList22To23().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList22To23();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("22.00 - 23.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}
				if (timeBandHoursDTO.getHashMapList23To24() != null
						&& !timeBandHoursDTO.getHashMapList23To24().isEmpty()
						&& timeBandHoursDTO.getHashMapList23To24().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList23To24();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("23.00 - 24.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}

				if (timeBandHoursDTO.getHashMapList24To1() != null && !timeBandHoursDTO.getHashMapList24To1().isEmpty()
						&& timeBandHoursDTO.getHashMapList24To1().size() != 0) {
					AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

					tempHashMapList = timeBandHoursDTO.getHashMapList24To1();
					for (FormDTO dto : tbl_FormulaList) {
						surverDataDto = new AnalyzeSurveryDataDTO();
						answersList = new ArrayList<String>();
						operationsList = new ArrayList<String>();
						String formula = dto.getFormula();
						String finalAnswer = solveCalculation(formula);

						String formulaName = dto.getFormulaName();
						surverDataDto.setFormulaName(formulaName);
						surverDataDto.setFormula(formula);
						surverDataDto.setAnswer(finalAnswer);
						surverDataDto.setTimeband("24.00 - 1.00");

						analyzeSurveryDataDTOList.add(surverDataDto);
					}
				}

			}

			int i = 0;
			List<AnalyzeSurveryDataDTO> temp = new ArrayList<AnalyzeSurveryDataDTO>();

			for (AnalyzeSurveryDataDTO dto : analyzeSurveryDataDTOList) {

				AnalyzeSurveryDataDTO d1 = new AnalyzeSurveryDataDTO();

				d1.setFormulaName(dto.getFormulaName());

				if (formulaList.size() < columnCount) {
					formulaList.add(d1);
				}

				if (i < columnCount) {
					temp.add(dto);
				} else {
					i = 0;
					analyzeSurveryDataList.add(temp);
					temp = new ArrayList<AnalyzeSurveryDataDTO>();
					temp.add(dto);
				}

				i++;

			}

			AnalyzeSurveryDataDTO timeBrand = new AnalyzeSurveryDataDTO();
			timeBrand.setFormulaName("Time Brand");
			formulaList.add(timeBrand);

			analyzeSurveryDataList.add(temp);

			for (List<AnalyzeSurveryDataDTO> analyzeSurveryDataDTO : analyzeSurveryDataList) {

				for (AnalyzeSurveryDataDTO dto : analyzeSurveryDataDTO) {

				}

			}

			createDynamicColumns();

			createLineModels();
			if (!commonService.checkTaskOnSurveyHisDetails(surveyNo, "SU007", "O")) {
				commonService.updateSurveyTaskDetails(surveyReqNo, surveyNo, "SU007", "C",
						sessionBackingBean.loginUser);
			}

			successMsg = "Data successfully analyzed.";
			RequestContext.getCurrentInstance().update("frm_AnalyzeSurveyData");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			disableTabAnalyadzedData = false;
			disableTabDisplayGraph = false;
			activeTabIndex = 1;

			RequestContext.getCurrentInstance().update("frm_AnalyzeSurveyData:tbl-AnalyzedDat");

		}

	}

	private UIComponent getUIComponent(String id) {
		return FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
	}

	public UIComponent findComponent(final String id) {

		UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
		UIComponent component = view.findComponent("uf:name");

		return null;

	}

	// notDone
	public void removeSelectedFormula(String formulaId) {
		activeTabIndex = 0;

		boolean deleted = surveyService.removeSelectedFormula(formulaId);
		tbl_FormulaList = surveyService.get_tbl_formulaList(formDTO);

		if (deleted) {
			successMsg = "Successfully deleted.";
			RequestContext.getCurrentInstance().update("frm_AnalyzeSurveyData");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {
			errorMsg = "Data didn't save.";
			RequestContext.getCurrentInstance().update("frm_AnalyzeSurveyData");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void onFormulaNameChange() {

		formDTO.setFieldName(null);
		formDTO.setOperator(null);
		formDTO.setValue(null);

		setDisableOperetor(false);
		setDisableFieldName(false);
		setDisableValue(false);
		setDisableAdd(true);

		if (formDTO.getDisplayFormulaName().equals(null) || formDTO.getDisplayFormulaName().equals("")
				|| formDTO.getDisplayFormulaName().isEmpty()) {
			operatorList = new ArrayList<String>();
			operatorList.add("SUM (");
			operatorList.add("COUNT (");
		} else {
			operatorList = new ArrayList<String>();
			if (isCountStart) {
				operatorList.add(">");
				operatorList.add("<");
				operatorList.add("=");
			} else {
				operatorList.add("+");
				operatorList.add("-");
				operatorList.add("*");
				operatorList.add("/");
				operatorList.add(">");
				operatorList.add("<");
				operatorList.add("=");
			}

		}
		formDTO.setFormulaNameDescription(surveyService.getSelectedFormulaName(formDTO.getFormulaNameCode()));

		formDTO.setDisplayFormulaName(formDTO.getFormulaNameDescription() + " = ");

	}

	public void onFieldNameChange() {
		setDisableValue(true);

		if (formDTO.getDisplayFormulaName().equals(null) || formDTO.getDisplayFormulaName().equals("")
				|| formDTO.getDisplayFormulaName().isEmpty()) {
			operatorList = new ArrayList<String>();

			if (!isSumStart) {
				operatorList.add("SUM (");
			}

			if (!isCountStart) {
				operatorList.add("COUNT (");
			}

		} else {

			if (isSumStart || isCountStart) {

				operatorList = new ArrayList<String>();
				if (isCountStart) {
					operatorList.add(">");
					operatorList.add("<");
					operatorList.add("=");
				} else {
					operatorList.add("+");
					operatorList.add("-");
					operatorList.add("*");
					operatorList.add("/");
					operatorList.add(">");
					operatorList.add("<");
					operatorList.add("=");
				}

				if (isSumStart && !isCountStart) {
					operatorList.add("END SUM()");
				}

				if (isCountStart) {
					operatorList.add("END COUNT()");
				}

			} else {

				operatorList = new ArrayList<String>();
				if (isCountStart) {
					operatorList.add(">");
					operatorList.add("<");
					operatorList.add("=");
				} else {
					operatorList.add("+");
					operatorList.add("-");
					operatorList.add("*");
					operatorList.add("/");
					operatorList.add(">");
					operatorList.add("<");
					operatorList.add("=");
				}

			}

		}

		setDisableOperetor(false);
		formDTO.setOperator(null);
		formDTO.setValue(null);
		getCreatedFormula(formDTO.getDisplayFormulaName(), formDTO.getFieldName());
		setDisableFieldName(true);

		if (isSumStart) {
			setDisableAdd(true);
		} else {
			if (isCountStart) {
				setDisableAdd(true);
			} else {
				setDisableAdd(false);
			}
		}

	}

	public void onOperaterChange() {
		setTemptSelectedOperator("invalid");
		if (formDTO.getOperator().equals("SUM (") && !isSumStart) {
			isSumStart = true;
		} else if (formDTO.getOperator().equals("COUNT (") && !isCountStart) {
			isCountStart = true;
		}

		if (formDTO.getOperator().equals("END SUM()") && isSumStart) {
			temptSelectedOperator = formDTO.getOperator();
			getCreatedFormula(formDTO.getDisplayFormulaName(), "");
			isSumStart = false;

			formDTO.setOperator("");
			operatorList = new ArrayList<String>();
			if (isCountStart) {
				operatorList.add(">");
				operatorList.add("<");
				operatorList.add("=");
			} else {
				operatorList.add("+");
				operatorList.add("-");
				operatorList.add("*");
				operatorList.add("/");
				operatorList.add(">");
				operatorList.add("<");
				operatorList.add("=");
			}
			setDisableFieldName(true);
			setDisableValue(true);

		} else if (formDTO.getOperator().equals("END COUNT()") && isCountStart) {
			temptSelectedOperator = formDTO.getOperator();
			getCreatedFormula(formDTO.getDisplayFormulaName(), "");
			isCountStart = false;

			formDTO.setOperator("");
			operatorList = new ArrayList<String>();
			if (isCountStart) {
				operatorList.add(">");
				operatorList.add("<");
				operatorList.add("=");
			} else {
				operatorList.add("+");
				operatorList.add("-");
				operatorList.add("*");
				operatorList.add("/");
				operatorList.add(">");
				operatorList.add("<");
				operatorList.add("=");
			}

			if (isSumStart) {
				operatorList.add("END SUM()");
			}
			setDisableFieldName(true);
			setDisableValue(true);
		} else {

			setDisableFieldName(false);

			setDisableValue(false);

			setDisableAdd(true);
			formDTO.setFieldName(null);
			formDTO.setValue(null);

			getCreatedFormula(formDTO.getDisplayFormulaName(), formDTO.getOperator());

			if (!operatorList.get(0).equals("+")) {
				// when have SUM and COUNT
				setDisableOperetor(true);
			} else {
				operatorList = new ArrayList<String>();

				if (!isSumStart) {
					operatorList.add("SUM (");
				}

				if (!isCountStart) {
					operatorList.add("COUNT (");
				}

			}
		}
		if (temptSelectedOperator.equals("END SUM()") || temptSelectedOperator.equals("END COUNT()")) {

			if (isSumStart) {
				setDisableAdd(true);
			} else {
				setDisableAdd(false);
			}

			setDisableValue(false);

		} else {
			if (isCountStart) {
				setDisableAdd(true);

			}

		}

	}

	public void onFormulaChange() {

		if (formDTO.getDisplayFormulaName().equals(null) || formDTO.getDisplayFormulaName().equals("")
				|| formDTO.getDisplayFormulaName().isEmpty()) {
			operatorList = new ArrayList<String>();
			if (!isSumStart) {
				operatorList.add("SUM (");
			}

			if (!isCountStart) {
				operatorList.add("COUNT (");
			}

		} else {
			operatorList = new ArrayList<String>();
			if (isCountStart) {
				operatorList.add(">");
				operatorList.add("<");
				operatorList.add("=");
			} else {
				operatorList.add("+");
				operatorList.add("-");
				operatorList.add("*");
				operatorList.add("/");
				operatorList.add(">");
				operatorList.add("<");
				operatorList.add("=");
			}

		}
	}

	public void onValueChange() {

		setDisableFieldName(true);

		if (formDTO.getDisplayFormulaName().equals(null) || formDTO.getDisplayFormulaName().equals("")
				|| formDTO.getDisplayFormulaName().isEmpty()) {
			operatorList = new ArrayList<String>();

			if (!isSumStart) {
				operatorList.add("SUM (");
			}

			if (!isCountStart) {
				operatorList.add("COUNT (");
			}

		} else {

			if (isSumStart || isCountStart) {

				operatorList = new ArrayList<String>();
				if (isCountStart) {
					operatorList.add(">");
					operatorList.add("<");
					operatorList.add("=");
				} else {
					operatorList.add("+");
					operatorList.add("-");
					operatorList.add("*");
					operatorList.add("/");
					operatorList.add(">");
					operatorList.add("<");
					operatorList.add("=");
				}

				if (isSumStart) {
					operatorList.add("END SUM()");
				}

				if (isCountStart) {
					operatorList.add("END COUNT()");
				}

			} else {

				operatorList = new ArrayList<String>();
				if (isCountStart) {
					operatorList.add(">");
					operatorList.add("<");
					operatorList.add("=");
				} else {
					operatorList.add("+");
					operatorList.add("-");
					operatorList.add("*");
					operatorList.add("/");
					operatorList.add(">");
					operatorList.add("<");
					operatorList.add("=");
				}

			}

		}

		setDisableOperetor(false);
		formDTO.setOperator(null);
		getCreatedFormula(formDTO.getDisplayFormulaName(), formDTO.getValue());
		setDisableValue(true);

		setDisableAdd(false);
		formDTO.setValue(null);

	}

	public void getCreatedFormula(String oldFormula, String newField) {

		if (!oldFormula.contains("=")) {
			String tempt = oldFormula.substring(0, oldFormula.length() - 1);
			formDTO.setDisplayFormulaName(tempt + " " + newField + " ");
		} else {

			if (!isSumStart && !isCountStart) {
				String tempt = oldFormula;
				formDTO.setDisplayFormulaName(tempt + " " + newField + " ");

			} else if (isCountStart) {

				if (temptSelectedOperator.equals("END COUNT()")) {
					String tempt = oldFormula.substring(0, oldFormula.length() - 1);
					formDTO.setDisplayFormulaName(tempt + " " + newField + ") ");

				} else {
					String tempt = oldFormula.substring(0, oldFormula.length() - 1);
					formDTO.setDisplayFormulaName(tempt + " " + newField + " ");

				}

			} else if (isSumStart) {
				if (temptSelectedOperator.equals("END SUM()")) {
					String tempt = oldFormula.substring(0, oldFormula.length() - 1);
					formDTO.setDisplayFormulaName(tempt + " " + newField + ") ");

				} else {
					String tempt = oldFormula.substring(0, oldFormula.length() - 1);
					formDTO.setDisplayFormulaName(tempt + " " + newField + " ");

				}

			} else {
				String tempt = oldFormula;
				formDTO.setDisplayFormulaName(tempt + " " + newField + ") ");

			}

		}

	}

	public void addFormulaToGrid() {

		disableOperetor = true;
		disableFieldName = true;

		String formula = formDTO.getDisplayFormulaName();
		formDTO.setDisplayFormulaName(formula.replaceAll("( )+", " "));

		if (formDTO.getDisplayFormulaName() != null && !formDTO.getDisplayFormulaName().isEmpty()
				&& !formDTO.getDisplayFormulaName().equalsIgnoreCase("")) {
			String inserted = surveyService.addFormulaToGrid(formDTO);
			if (inserted.equals("S")) {

				successMsg = "Formula successfully added.";
				RequestContext.getCurrentInstance().update("frm_AnalyzeSurveyData");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else if (inserted.equals("D")) {

				alertMsg = "Duplicate formula";
				RequestContext.getCurrentInstance().update("frm_AnalyzeSurveyData");
				RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
			} else {

				errorMsg = "Invalid formula";
				RequestContext.getCurrentInstance().update("frm_AnalyzeSurveyData");
				RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
			}
		} else {
			errorMsg = "Formula name not found.";
			RequestContext.getCurrentInstance().update("frm_AnalyzeSurveyData");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

		tbl_FormulaList = surveyService.get_tbl_formulaList(formDTO);

		formDTO.setDisplayFormulaName(null);
		formDTO.setFormulaNameCode(null);
		formDTO.setFormulaNameDescription(null);
		formDTO.setFieldName(null);
		formDTO.setValue(null);
		disableValue = true;
		disableAdd = true;
	}

	public void clearFormula() {
		formDTO.setFormulaTemplateId(null);
		formDTO.setFormulaNameCode(null);
		formDTO.setFieldName(null);
		formDTO.setOperator(null);
		formDTO.setValue(null);
		formDTO.setDisplayFormulaName(null);

		disableOperetor = true;
		disableFieldName = true;
		disableValue = true;
		isCountStart = false;
		isSumStart = false;

	}

	public void name(List<HashMap> map, String timeBand) {

		if (map != null && !map.isEmpty() && map.size() != 0) {
			AnalyzeSurveryDataDTO surverDataDto = new AnalyzeSurveryDataDTO();

			tempHashMapList = map;
			for (FormDTO dto : tbl_FormulaList) {
				surverDataDto = new AnalyzeSurveryDataDTO();
				answersList = new ArrayList<String>();
				operationsList = new ArrayList<String>();
				String formula = dto.getFormula();
				String finalAnswer = solveCalculation(formula);

				String formulaName = dto.getFormulaName();
				surverDataDto.setFormulaName(formulaName);
				surverDataDto.setFormula(formula);
				surverDataDto.setAnswer(finalAnswer);
				surverDataDto.setTimeband(timeBand);

				analyzeSurveryDataDTOList.add(surverDataDto);
			}
		}

	}

	public String solveCalculation(String formula) {

		String firstOperand = null;
		String valuesToSum = "";
		String finalAnswer = null;
		String sumAnswer = "";
		String finalCount = "";
		String subFormula = "";
		int countFinal = 0;

		String getfirst = formula.substring(formula.lastIndexOf("=") + 1);
		List<IndicatorsDTO> dtoList = new ArrayList<>();

		dtoList = getADynamicRaw();

		String sum = getfirst.substring(0, Math.min(getfirst.length(), 4));

		String count = getfirst.substring(0, Math.min(getfirst.length(), 6));

		// first start
		if (sum != null && !sum.isEmpty() && sum.trim().equalsIgnoreCase("SUM")) {

			List<String> tempAnswersList = new ArrayList<String>();

			String remove = "SUM";

			/** first operand SUM solve start **/
			String subStr = getfirst.substring(getfirst.lastIndexOf(remove.trim()) + 6);

			String[] parts = subStr.split("\\)");
			String beforeFirst = parts[0];

			String tempBefore = beforeFirst;

			for (Map<String, String> entry : tempHashMapList) {
				Iterator it = entry.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();

					String key = (String) pair.getKey();
					String value = (String) pair.getValue();
					boolean isFound = beforeFirst.toLowerCase().contains(key.toLowerCase());
					if (isFound) {
						tempBefore = tempBefore.toLowerCase().replace(key.toLowerCase(), value.toLowerCase());
					}
				}

				String answer = evaluate(tempBefore);

				tempAnswersList.add(answer);
				tempBefore = beforeFirst;
			}

			// SUM OF ALL start

			for (String answer : tempAnswersList) {
				valuesToSum = valuesToSum + answer + " + ";
			}

			valuesToSum += "0";

			sumAnswer = evaluate(valuesToSum);

			answersList.add(sumAnswer);
			// SUM OF ALL end

			firstOperand = remove + " ( " + beforeFirst + " )";

			String secondOperand = getfirst.substring(getfirst.lastIndexOf(firstOperand) + (firstOperand.length() + 1));

			String operator = secondOperand.substring(0, Math.min(secondOperand.length(), 2));

			if (operator != null && !operator.isEmpty() && !operator.trim().equalsIgnoreCase("")) {
				operationsList.add(operator);
			}

			String secondSolve = secondOperand.substring(secondOperand.lastIndexOf(operator) + (operator.length()));

			if (secondSolve != null && !secondSolve.isEmpty() && !secondSolve.trim().equalsIgnoreCase("")) {
				solveCalculation(secondSolve);
			}
			/** first operand SUM solve end **/

		} else if (count != null && !count.isEmpty() && count.trim().equalsIgnoreCase("COUNT")) {

			/** first operand COUNT solve start **/

			String remove = "COUNT";

			String subStr1 = getfirst.substring(getfirst.lastIndexOf(getfirst.trim()) + 7);

			String[] parts1 = subStr1.split("\\)");
			String beforeFirst1 = parts1[0];

			String tempBefore = beforeFirst1;
			for (Map<String, String> entry : tempHashMapList) {
				Iterator it = entry.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();

					String key = (String) pair.getKey();
					String value = (String) pair.getValue();
					boolean isFound = beforeFirst1.contains(key);
					if (isFound) {
						if (value != null && !value.isEmpty() && !value.trim().equalsIgnoreCase("")
								&& !value.equalsIgnoreCase("0")) {
							countFinal++;
						}
					}
				}
			}

			finalCount = Integer.toString(countFinal);

			answersList.add(finalCount);

			String firstOperand1 = remove + "( " + beforeFirst1 + " )";

			String secondOperand = getfirst
					.substring(getfirst.lastIndexOf(firstOperand1) + (firstOperand1.length() + 1));

			String operator = secondOperand.substring(0, Math.min(secondOperand.length(), 2));

			if (operator != null && !operator.isEmpty() && !operator.trim().equalsIgnoreCase("")) {
				operationsList.add(operator);
			}

			String secondSolve = secondOperand.substring(secondOperand.lastIndexOf(operator) + (operator.length()));

			if (secondSolve != null && !secondSolve.isEmpty() && !secondSolve.trim().equalsIgnoreCase("")) {
				solveCalculation(secondSolve);
			}

			/** first operand COUNT solve end **/
		} else {

			List<String> tempAnswersList = new ArrayList<String>();
			String tempBefore = getfirst.trim();
			for (Map<String, String> entry : tempHashMapList) {
				Iterator it = entry.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();

					String key = (String) pair.getKey();
					String value = (String) pair.getValue();
					boolean isFound = getfirst.trim().toLowerCase().contains(key.toLowerCase());
					if (isFound) {
						tempBefore = tempBefore.toLowerCase().replace(key.toLowerCase(), value.toLowerCase());
					}
				}

				String answer = evaluate(tempBefore);

				tempAnswersList.add(answer);
				tempBefore = getfirst.trim();
			}

			for (String answer : tempAnswersList) {
				valuesToSum = valuesToSum + answer + " , ";
			}
			valuesToSum = valuesToSum.substring(0, valuesToSum.length() - 2);
			answersList.add(valuesToSum);

			finalAnswer = valuesToSum;
		}
		// first end

		for (String answer : answersList) {
			for (String operand : operationsList) {
				subFormula += answer + operand;
			}
		}

		String answer = "";
		if (subFormula != null && !subFormula.isEmpty() && !subFormula.trim().equalsIgnoreCase("")) {
			subFormula = subFormula.substring(0, subFormula.length() - 1);
			answer = evaluate(subFormula);
		}

		if (answer != null && !answer.isEmpty() && !answer.trim().equalsIgnoreCase("")) {
			finalAnswer = answer;
		} else if (sumAnswer != null && !sumAnswer.isEmpty() && !sumAnswer.trim().equalsIgnoreCase("")) {
			finalAnswer = sumAnswer;
		} else if (finalCount != null && !finalCount.isEmpty() && !finalCount.trim().equalsIgnoreCase("")) {
			finalAnswer = finalCount;
		}

		return finalAnswer;
	}

	public void displayGraph() {

	}

	public void downloadData() {

	}

	public void printDataTable() {

	}

	private void createDynamicColumns() {

	}

	public void btn_CompleteSurvey() {

		commonService.updateSurveyTaskDetails(surveyReqNo, surveyNo, "SU006", "C", sessionBackingBean.loginUser);
		commonService.updateSurveyTaskDetails(surveyReqNo, formDTO.getSurveyNo(), "SU007", "O",
				sessionBackingBean.loginUser);

		disableAnalyzedData = false;
		disableSurveyCompleted = true;

		successMsg = "Survey Completed.";
		RequestContext.getCurrentInstance().update("frm_AnalyzeSurveyData");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

	}

	public void btn_AnalyzeData() {
		commonService.updateSurveyTaskDetails(surveyReqNo, surveyNo, "SU007", "C", sessionBackingBean.loginUser);
	}

	// Getters and setters
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public FormDTO getFormDTO() {
		return formDTO;
	}

	public void setFormDTO(FormDTO formDTO) {
		this.formDTO = formDTO;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public List<String> getFormIdList() {
		return formIdList;
	}

	public void setFormIdList(List<String> formIdList) {
		this.formIdList = formIdList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public List<FormDTO> getFormulaNameList() {
		return formulaNameList;
	}

	public void setFormulaNameList(List<FormDTO> formulaNameList) {
		this.formulaNameList = formulaNameList;
	}

	public List<IndicatorsDTO> getFieldNameList() {
		return fieldNameList;
	}

	public void setFieldNameList(List<IndicatorsDTO> fieldNameList) {
		this.fieldNameList = fieldNameList;
	}

	public List<String> getOperatorList() {
		return operatorList;
	}

	public void setOperatorList(List<String> operatorList) {
		this.operatorList = operatorList;
	}

	public List<String> getFormulaIdList() {
		return formulaIdList;
	}

	public void setFormulaIdList(List<String> formulaIdList) {
		this.formulaIdList = formulaIdList;
	}

	public boolean isDisableFieldName() {
		return disableFieldName;
	}

	public void setDisableFieldName(boolean disableFieldName) {
		this.disableFieldName = disableFieldName;
	}

	public boolean isDisableValue() {
		return disableValue;
	}

	public void setDisableValue(boolean disableValue) {
		this.disableValue = disableValue;
	}

	public boolean isDisableOperetor() {
		return disableOperetor;
	}

	public void setDisableOperetor(boolean disableOperetor) {
		this.disableOperetor = disableOperetor;
	}

	public List<FormDTO> getTbl_FormulaList() {
		return tbl_FormulaList;
	}

	public void setTbl_FormulaList(List<FormDTO> tbl_FormulaList) {
		this.tbl_FormulaList = tbl_FormulaList;
	}

	public boolean isSumStart() {
		return isSumStart;
	}

	public void setSumStart(boolean isSumStart) {
		this.isSumStart = isSumStart;
	}

	public boolean isCountStart() {
		return isCountStart;
	}

	public void setCountStart(boolean isCountStart) {
		this.isCountStart = isCountStart;
	}

	public boolean isDisableAdd() {
		return disableAdd;
	}

	public void setDisableAdd(boolean disableAdd) {
		this.disableAdd = disableAdd;
	}

	public String getTemptSelectedOperator() {
		return temptSelectedOperator;
	}

	public void setTemptSelectedOperator(String temptSelectedOperator) {
		this.temptSelectedOperator = temptSelectedOperator;
	}

	public EnterSurveyDataService getEnterSurveyDataService() {
		return enterSurveyDataService;
	}

	public void setEnterSurveyDataService(EnterSurveyDataService enterSurveyDataService) {
		this.enterSurveyDataService = enterSurveyDataService;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public boolean isRenderTabView() {
		return renderTabView;
	}

	public void setRenderTabView(boolean renderTabView) {
		this.renderTabView = renderTabView;
	}

	public HashMap getmMap() {
		return mMap;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public String getSurveyNo() {
		return surveyNo;
	}

	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}

	public String getSurveyReqNo() {
		return surveyReqNo;
	}

	public void setSurveyReqNo(String surveyReqNo) {
		this.surveyReqNo = surveyReqNo;
	}

	public void setmMap(HashMap mMap) {
		this.mMap = mMap;
	}

	public List<HashMap> getHashMapList() {
		return hashMapList;
	}

	public void setHashMapList(List<HashMap> hashMapList) {
		this.hashMapList = hashMapList;
	}

	public static String evaluate(String expression) {
		String val = "";
		try {

			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			String calculate = expression;
			val = engine.eval(calculate).toString();

		} catch (ScriptException e) {
			e.printStackTrace();
		}

		return val;
		/*
		 * char[] tokens = expression.toCharArray();
		 * 
		 * // Stack for numbers: 'values' Stack<Integer> values = new
		 * Stack<Integer>();
		 * 
		 * // Stack for Operators: 'ops' Stack<Character> ops = new
		 * Stack<Character>();
		 * 
		 * for (int i = 0; i < tokens.length; i++) { // Current token is a
		 * whitespace, skip it if (tokens[i] == ' ') continue;
		 * 
		 * // Current token is a number, push it to stack for numbers if
		 * (tokens[i] >= '0' && tokens[i] <= '9') { StringBuffer sbuf = new
		 * StringBuffer(); // There may be more than one digits in number while
		 * (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
		 * sbuf.append(tokens[i++]);
		 * values.push(Integer.parseInt(sbuf.toString())); }
		 * 
		 * // Current token is an opening brace, push it to 'ops' else if
		 * (tokens[i] == '(') ops.push(tokens[i]);
		 * 
		 * // Closing brace encountered, solve entire brace else if (tokens[i]
		 * == ')') { while (ops.peek() != '(') values.push(applyOp(ops.pop(),
		 * values.pop(), values.pop())); ops.pop(); }
		 * 
		 * // Current token is an operator. else if (tokens[i] == '+' ||
		 * tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') { // While
		 * top of 'ops' has same or greater precedence to current // token,
		 * which is an operator. Apply operator on top of 'ops' // to top two
		 * elements in values stack while (!ops.empty() &&
		 * hasPrecedence(tokens[i], ops.peek())) values.push(applyOp(ops.pop(),
		 * values.pop(), values.pop()));
		 * 
		 * // Push current token to 'ops'. ops.push(tokens[i]); } }
		 * 
		 * // Entire expression has been parsed at this point, apply remaining
		 * // ops to remaining values while (!ops.empty())
		 * values.push(applyOp(ops.pop(), values.pop(), values.pop()));
		 * 
		 * // Top of 'values' contains result, return it return values.pop();
		 */}

	public static boolean hasPrecedence(char op1, char op2) {
		if (op2 == '(' || op2 == ')')
			return false;
		if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
			return false;
		else
			return true;
	}

	public static int applyOp(char op, int b, int a) {
		switch (op) {
		case '+':
			return a + b;
		case '-':
			return a - b;
		case '*':
			return a * b;
		case '/':
			if (b == 0)
				throw new UnsupportedOperationException("Cannot divide by zero");
			return a / b;
		}
		return 0;
	}

	public List<String> getAnswersList() {
		return answersList;
	}

	public void setAnswersList(List<String> answersList) {
		this.answersList = answersList;
	}

	public List<String> getOperationsList() {
		return operationsList;
	}

	public void setOperationsList(List<String> operationsList) {
		this.operationsList = operationsList;
	}

	public List<HashMap> getTempHashMapList() {
		return tempHashMapList;
	}

	public void setTempHashMapList(List<HashMap> tempHashMapList) {
		this.tempHashMapList = tempHashMapList;
	}

	public TimeBandHoursDTO getTimeBandHoursDTO() {
		return timeBandHoursDTO;
	}

	public void setTimeBandHoursDTO(TimeBandHoursDTO timeBandHoursDTO) {
		this.timeBandHoursDTO = timeBandHoursDTO;
	}

	public List<AnalyzeSurveryDataDTO> getAnalyzeSurveryDataDTOList() {
		return analyzeSurveryDataDTOList;
	}

	public void setAnalyzeSurveryDataDTOList(List<AnalyzeSurveryDataDTO> analyzeSurveryDataDTOList) {
		this.analyzeSurveryDataDTOList = analyzeSurveryDataDTOList;
	}

	public LineChartModel getLineModel1() {
		return lineModel1;
	}

	public void setLineModel1(LineChartModel lineModel1) {
		this.lineModel1 = lineModel1;
	}

	public LineChartModel getLineModel2() {
		return lineModel2;
	}

	public void setLineModel2(LineChartModel lineModel2) {
		this.lineModel2 = lineModel2;
	}

	/*
	 * public List<ColumnModel> getColumns() { return columns; }
	 * 
	 * public void setColumns(List<ColumnModel> columns) { this.columns =
	 * columns; }
	 */

	public List<List<AnalyzeSurveryDataDTO>> getAnalyzeSurveryDataList() {
		return analyzeSurveryDataList;
	}

	public void setAnalyzeSurveryDataList(List<List<AnalyzeSurveryDataDTO>> analyzeSurveryDataList) {
		this.analyzeSurveryDataList = analyzeSurveryDataList;
	}

	public List<Integer> getIntegerIndicatorList() {
		return integerIndicatorList;
	}

	public void setIntegerIndicatorList(List<Integer> integerIndicatorList) {
		this.integerIndicatorList = integerIndicatorList;
	}

	public List<AnalyzeSurveryDataDTO> getFormulaList() {
		return formulaList;
	}

	public void setFormulaList(List<AnalyzeSurveryDataDTO> formulaList) {
		this.formulaList = formulaList;
	}

	// class for columns model

	public LineChartModel getGraphAnalyzedData() {
		return graphAnalyzedData;
	}

	public void setGraphAnalyzedData(LineChartModel graphAnalyzedData) {
		this.graphAnalyzedData = graphAnalyzedData;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public boolean isDisableSurveyCompleted() {
		return disableSurveyCompleted;
	}

	public void setDisableSurveyCompleted(boolean disableSurveyCompleted) {
		this.disableSurveyCompleted = disableSurveyCompleted;
	}

	public boolean isDisableAnalyzedData() {
		return disableAnalyzedData;
	}

	public void setDisableAnalyzedData(boolean disableAnalyzedData) {
		this.disableAnalyzedData = disableAnalyzedData;
	}

	public boolean isDisableTabAnalyadzedData() {
		return disableTabAnalyadzedData;
	}

	public void setDisableTabAnalyadzedData(boolean disableTabAnalyadzedData) {
		this.disableTabAnalyadzedData = disableTabAnalyadzedData;
	}

	public boolean isDisableTabDisplayGraph() {
		return disableTabDisplayGraph;
	}

	public void setDisableTabDisplayGraph(boolean disableTabDisplayGraph) {
		this.disableTabDisplayGraph = disableTabDisplayGraph;
	}

}
