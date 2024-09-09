package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.TimeBandDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.EnterSurveyDataService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "enterSurveyDataBackingBean")
@ViewScoped
public class EnterServeyDataBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<FormDTO> formList;
	private String selectedFormID;
	private FormDTO formDTO;
	private List<IndicatorsDTO> tableIndicatorList;
	private List<IndicatorsDTO> inputDataIndicatorList;
	private String noOfRows;
	private String noOfCopies;
	private boolean printCopies;
	private int rowCount;
	private int stepVal;
	private List<Integer> integerIndicatorList;
	private List<List<IndicatorsDTO>> indicatorList;

	private EnterSurveyDataService enterSurveyDataService;
	private CommonService commonService;
	private String errMessage;
	private boolean viewIndicatorList;
	private boolean editValues;

	private List<TimeBandDTO> startTimeBandList;
	private boolean startTimeView;
	private boolean endTimeView;
	private boolean timebandView;
	private String startTimeBand;
	private String endTimeBand;
	private String timeBand;
	private List<TimeBandDTO> endTimeBandList;
	private List<TimeBandDTO> timeBandList;
	private boolean timeBandHeader;
	private Date autoGenDate;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {
		enterSurveyDataService = (EnterSurveyDataService) SpringApplicationContex.getBean("enterSurveyDataService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		formDTO = new FormDTO();
		formList = new ArrayList<FormDTO>();
		formList = enterSurveyDataService.retrieveFormsList();
		tableIndicatorList = new ArrayList<IndicatorsDTO>();
		inputDataIndicatorList = new ArrayList<IndicatorsDTO>();
		integerIndicatorList = new ArrayList<Integer>();
		indicatorList = new ArrayList<List<IndicatorsDTO>>();
		printCopies = false;
		rowCount = 0;
		stepVal = 0;
		errMessage = null;
		viewIndicatorList = false;
		editValues = false;

		startTimeBandList = new ArrayList<TimeBandDTO>();
		startTimeView = false;
		endTimeView = false;
		timebandView = false;
		timeBandHeader = false;
	}

	public void onFormIdClick() {

		startTimeBandList = enterSurveyDataService.retrieveTimeBandList();
		formDTO = enterSurveyDataService.retrieveFormDataFromFormId(selectedFormID);

		tableIndicatorList = retrieveIndicatorList(selectedFormID);

		for (IndicatorsDTO dto : tableIndicatorList) {
			IndicatorsDTO addDto = new IndicatorsDTO();
			inputDataIndicatorList.add(addDto);
		}

		printCopies = true;

		List<List<IndicatorsDTO>> indDtoList = enterSurveyDataService
				.retrieveIndicatorListDataFromFormId(selectedFormID);

		if (indDtoList != null && indDtoList.size() != 0) {
			startTimeView = true;
			List<IndicatorsDTO> tempList = indDtoList.get(0);

			int countDto = 0;
			int countList = 0;
			List<List<IndicatorsDTO>> tempindicatorList = new ArrayList<List<IndicatorsDTO>>();
			List<IndicatorsDTO> tempLst = new ArrayList<IndicatorsDTO>();
			for (IndicatorsDTO dto : tempList) {
				for (List<IndicatorsDTO> list : indDtoList) {
					if (list != null && !list.isEmpty() && list.size() != 0) {
						IndicatorsDTO tempDto = indDtoList.get(countList).get(countDto);
						tempLst.add(tempDto);
					}
					countList++;
				}
				tempindicatorList.add(tempLst);
				countDto++;
				countList = 0;
				tempLst = new ArrayList<IndicatorsDTO>();
			}

			indicatorList = tempindicatorList;

			if (indicatorList != null && indicatorList.size() != 0) {
				editValues = true;
			}

			viewIndicatorList = true;
			addRowsAction();
		}

		RequestContext.getCurrentInstance().update("frmGenSurvey");
	}

	public List<IndicatorsDTO> retrieveIndicatorList(String selectedFormId) {

		List<IndicatorsDTO> indicatorLst = new ArrayList<IndicatorsDTO>();

		indicatorLst = enterSurveyDataService.retrieveIndicatorsForFormId(selectedFormId);

		return indicatorLst;
	}

	public void addDynamicData() {

	}

	public void addRowsAction() {
		timeBandHeader = true;

		rowCount = 1;
		integerIndicatorList = new ArrayList<Integer>();
		for (int i = 1; i <= (rowCount + 1); i++) {
			integerIndicatorList.add(i);
		}

		if (timeBand != null && !timeBand.isEmpty() && !timeBand.trim().equalsIgnoreCase("")) {

			List<TimeBandDTO> tempTimeBandList = new ArrayList<TimeBandDTO>();
			List<IndicatorsDTO> tempIndicatorList = new ArrayList<IndicatorsDTO>();

			tempTimeBandList = enterSurveyDataService.retrieveTimeBandList();
			IndicatorsDTO indDtoEnd = new IndicatorsDTO();
			if (tempTimeBandList != null && tempTimeBandList.size() != 0) {
				indDtoEnd.setFieldNameEn("Time Band");
				indDtoEnd.setFieldType("String");
				indDtoEnd.setMandatoryField("Y");
				indDtoEnd.setLovField("Y");

				List<IndicatorsDTO> indicatorLOVList = new ArrayList<IndicatorsDTO>();

				for (TimeBandDTO timeDto : tempTimeBandList) {
					IndicatorsDTO lovDto = new IndicatorsDTO();
					lovDto.setLovCode(Integer.toString(timeDto.getTimeBand()));
					lovDto.setFieldNameEn(timeDto.getStartTime() + " - " + timeDto.getEndTime());

					indicatorLOVList.add(lovDto);
				}

				indDtoEnd.setIndicatorLOV(indicatorLOVList);
				indDtoEnd.setLovFieldEnable(true);
				indDtoEnd.setFieldInputVal(timeBand);
			}
			tableIndicatorList.add(indDtoEnd);

		}

		inputDataIndicatorList = tableIndicatorList;
		IndicatorsDTO tempIndDto = new IndicatorsDTO();
		if (viewIndicatorList == false) {

			List<IndicatorsDTO> tempIndicatorList = new ArrayList<IndicatorsDTO>();
			for (IndicatorsDTO dto : inputDataIndicatorList) {
				IndicatorsDTO indDto = new IndicatorsDTO();
				indDto.setFieldNameEn(dto.getFieldNameEn());
				indDto.setFieldNameSin(dto.getFieldNameSin());
				indDto.setFieldNameTam(dto.getFieldNameTam());
				indDto.setFieldType(dto.getFieldType());
				indDto.setFieldLength(dto.getFieldLength());
				indDto.setMandatoryField(dto.getMandatoryField());
				indDto.setActive(dto.getActive());
				indDto.setLovField(dto.getLovField());
				indDto.setDisplayAfter(dto.getDisplayAfter());
				indDto.setDisplayOrder(dto.getDisplayOrder());
				indDto.setFieldIndicatorSeq(dto.getFieldIndicatorSeq());

				if (dto.getLovField() != null && !dto.getLovField().isEmpty()
						&& dto.getLovField().equalsIgnoreCase("Y")) {
					for (IndicatorsDTO obj : dto.getIndicatorLOV()) {
						if (dto.getFieldInputVal() != null && !dto.getFieldInputVal().isEmpty()
								&& dto.getFieldInputVal().equalsIgnoreCase(obj.getLovCode())) {
							dto.setFieldInputVal(obj.getFieldNameEn());
							indDto.setLovSelectedVal(obj.getLovCode());
						}
					}
				}

				if (dto.getFieldType().equalsIgnoreCase("FT03")) {
					if (autoGenDate != null) {
						SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
						try {
							String dateString = sdfr.format(autoGenDate);
							dto.setFieldInputVal(dateString);
						} catch (Exception ex) {
							throw ex;
						}
					}
				}

				indDto.setFieldInputVal(dto.getFieldInputVal());

				tempIndicatorList.add(indDto);
				tempIndDto = indDto;
			}

			indicatorList.add(tempIndicatorList);

		}

		tableIndicatorList = retrieveIndicatorList(selectedFormID);
		viewIndicatorList = false;
		RequestContext.getCurrentInstance().update("frmGenSurvey");
	}

	public void deleteRowAction(List<List<IndicatorsDTO>> lst) {
		if (editValues) {

			IndicatorsDTO dto = (IndicatorsDTO) lst.get(0);
			enterSurveyDataService.deleteSelectedTableRow(dto);
		}

		indicatorList.remove(lst);

		RequestContext.getCurrentInstance().update("frmGenSurvey");
	}

	public void saveAction() {

		formDTO.setFormId(selectedFormID);
		enterSurveyDataService.insertDataIntoNt_t_indicators(formDTO, sessionBackingBean.getLoginUser(), indicatorList,
				editValues);
		String sirRequestNo = enterSurveyDataService.getSIRFromSIPNumber(formDTO.getSurveyNo());

		commonService.updateSurveyTaskDetails(sirRequestNo, formDTO.getSurveyNo(), "SU006", "O",
				sessionBackingBean.getLoginUser());

		cleaForm();
		RequestContext.getCurrentInstance().update("frmGenSurvey");

		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
	}

	public void cleaForm() {
		formDTO = new FormDTO();
		formList = new ArrayList<FormDTO>();
		formList = enterSurveyDataService.retrieveFormsList();
		tableIndicatorList = new ArrayList<IndicatorsDTO>();
		inputDataIndicatorList = new ArrayList<IndicatorsDTO>();
		integerIndicatorList = new ArrayList<Integer>();
		indicatorList = new ArrayList<List<IndicatorsDTO>>();
		printCopies = false;
		rowCount = 0;
		stepVal = 0;
		errMessage = null;
		selectedFormID = "";
		viewIndicatorList = false;
		editValues = false;
		startTimeBandList = new ArrayList<TimeBandDTO>();
		startTimeView = false;
		endTimeView = false;
		timebandView = false;
		timeBandHeader = false;
		RequestContext.getCurrentInstance().update("frmGenSurvey");
	}

	public String validateinputValues(List<IndicatorsDTO> inputDataIndicatorList) {
		String message = null;
		for (IndicatorsDTO dto : tableIndicatorList) {
			for (IndicatorsDTO dto1 : inputDataIndicatorList) {
				if (dto.getMandatoryField() != null && !dto.getMandatoryField().isEmpty()
						&& dto.getMandatoryField().equalsIgnoreCase("Y")) {

					if (dto.getFieldInputVal() == null || dto.getFieldInputVal().isEmpty()
							|| dto.getFieldInputVal().trim().equalsIgnoreCase("")) {
						message = "Please input " + dto.getFieldNameEn();
						return message;
					}
				}
			}
		}

		return null;
	}

	public void onStartDateClick() {
		endTimeView = true;
		endTimeBandList = enterSurveyDataService.retrieveTimeBandList();
		for (int i = 1; i <= Integer.parseInt(startTimeBand); i++) {
			endTimeBandList.remove(0);
		}

		RequestContext.getCurrentInstance().update("frmGenSurvey");
	}

	@SuppressWarnings("unlikely-arg-type")
	public void onEndDateClick() {

		String tempTimeBand = null;
		timebandView = true;
		int startTime = Integer.parseInt(startTimeBand);
		startTime = startTime - 1;
		tempTimeBand = Integer.toString(startTime);
		List<TimeBandDTO> tempTimeBandList = new ArrayList<TimeBandDTO>();

		timeBandList = enterSurveyDataService.retrieveTimeBandList();

		for (int i = 1; i <= Integer.parseInt(tempTimeBand); i++) {
			timeBandList.remove(0);
		}

		int endTimeBnd = Integer.parseInt(endTimeBand);

		for (TimeBandDTO dto : timeBandList) {
			if (dto.getTimeBand() == endTimeBnd) {
				tempTimeBandList.add(dto);
				break;
			} else {
				tempTimeBandList.add(dto);
			}
		}
		timeBandList = tempTimeBandList;

		RequestContext.getCurrentInstance().update("frmGenSurvey");

	}

	public List<FormDTO> getFormList() {
		return formList;
	}

	public void setFormList(List<FormDTO> formList) {
		this.formList = formList;
	}

	public String getSelectedFormID() {
		return selectedFormID;
	}

	public void setSelectedFormID(String selectedFormID) {
		this.selectedFormID = selectedFormID;
	}

	public FormDTO getFormDTO() {
		return formDTO;
	}

	public void setFormDTO(FormDTO formDTO) {
		this.formDTO = formDTO;
	}

	public List<IndicatorsDTO> getTableIndicatorList() {
		return tableIndicatorList;
	}

	public void setTableIndicatorList(List<IndicatorsDTO> tableIndicatorList) {
		this.tableIndicatorList = tableIndicatorList;
	}

	public String getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(String noOfRows) {
		this.noOfRows = noOfRows;
	}

	public String getNoOfCopies() {
		return noOfCopies;
	}

	public void setNoOfCopies(String noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isPrintCopies() {
		return printCopies;
	}

	public void setPrintCopies(boolean printCopies) {
		this.printCopies = printCopies;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public List<IndicatorsDTO> getInputDataIndicatorList() {
		return inputDataIndicatorList;
	}

	public void setInputDataIndicatorList(List<IndicatorsDTO> inputDataIndicatorList) {
		this.inputDataIndicatorList = inputDataIndicatorList;
	}

	public int getStepVal() {
		return stepVal;
	}

	public void setStepVal(int stepVal) {
		this.stepVal = stepVal;
	}

	public List<Integer> getIntegerIndicatorList() {
		return integerIndicatorList;
	}

	public void setIntegerIndicatorList(List<Integer> integerIndicatorList) {
		this.integerIndicatorList = integerIndicatorList;
	}

	public EnterSurveyDataService getEnterSurveyDataService() {
		return enterSurveyDataService;
	}

	public void setEnterSurveyDataService(EnterSurveyDataService enterSurveyDataService) {
		this.enterSurveyDataService = enterSurveyDataService;
	}

	public List<List<IndicatorsDTO>> getIndicatorList() {
		return indicatorList;
	}

	public void setIndicatorList(List<List<IndicatorsDTO>> indicatorList) {
		this.indicatorList = indicatorList;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public boolean isViewIndicatorList() {
		return viewIndicatorList;
	}

	public void setViewIndicatorList(boolean viewIndicatorList) {
		this.viewIndicatorList = viewIndicatorList;
	}

	public boolean isEditValues() {
		return editValues;
	}

	public void setEditValues(boolean editValues) {
		this.editValues = editValues;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isStartTimeView() {
		return startTimeView;
	}

	public void setStartTimeView(boolean startTimeView) {
		this.startTimeView = startTimeView;
	}

	public boolean isEndTimeView() {
		return endTimeView;
	}

	public void setEndTimeView(boolean endTimeView) {
		this.endTimeView = endTimeView;
	}

	public String getStartTimeBand() {
		return startTimeBand;
	}

	public void setStartTimeBand(String startTimeBand) {
		this.startTimeBand = startTimeBand;
	}

	public String getEndTimeBand() {
		return endTimeBand;
	}

	public void setEndTimeBand(String endTimeBand) {
		this.endTimeBand = endTimeBand;
	}

	public List<TimeBandDTO> getEndTimeBandList() {
		return endTimeBandList;
	}

	public void setEndTimeBandList(List<TimeBandDTO> endTimeBandList) {
		this.endTimeBandList = endTimeBandList;
	}

	public boolean isTimebandView() {
		return timebandView;
	}

	public void setTimebandView(boolean timebandView) {
		this.timebandView = timebandView;
	}

	public String getTimeBand() {
		return timeBand;
	}

	public void setTimeBand(String timeBand) {
		this.timeBand = timeBand;
	}

	public List<TimeBandDTO> getStartTimeBandList() {
		return startTimeBandList;
	}

	public void setStartTimeBandList(List<TimeBandDTO> startTimeBandList) {
		this.startTimeBandList = startTimeBandList;
	}

	public List<TimeBandDTO> getTimeBandList() {
		return timeBandList;
	}

	public void setTimeBandList(List<TimeBandDTO> timeBandList) {
		this.timeBandList = timeBandList;
	}

	public boolean isTimeBandHeader() {
		return timeBandHeader;
	}

	public void setTimeBandHeader(boolean timeBandHeader) {
		this.timeBandHeader = timeBandHeader;
	}

	public Date getAutoGenDate() {
		return autoGenDate;
	}

	public void setAutoGenDate(Date autoGenDate) {
		this.autoGenDate = autoGenDate;
	}

}
