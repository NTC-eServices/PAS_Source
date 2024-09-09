package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.DivisionDTO;
import lk.informatics.ntc.model.dto.MainCounterDTO;
import lk.informatics.ntc.model.dto.UserClosedDTO;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "userClosedConsoleBackingBean")
@ViewScoped
public class UserClosedConsoleBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private QueueManagementService queueManagementService;

	private List<UserClosedDTO> displayRecordsList = new ArrayList<UserClosedDTO>(0);
	private List<DivisionDTO> divisionList = new ArrayList<DivisionDTO>(0);
	private List<MainCounterDTO> mainCounterList = new ArrayList<MainCounterDTO>(0);
	private UserClosedDTO searchParam = new UserClosedDTO();
	private UserClosedDTO selectedRecord;
	private boolean disabledCounter = true;
	private String successMessage, errorMessage, loggedUser;
	private boolean completedRecords = false;

	@PostConstruct
	public void init() {
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		loadInitData();
	}

	private void loadInitData() {
		// load ongoing (status!='C') records by default
		displayRecordsList = queueManagementService.getOngoingQueueRecordsForClosedConsole();
		divisionList = queueManagementService.getAllDivisions();
	}

	public void onSelectDivision() {
		if (searchParam.getSectionCode() != null) {
			// load all counters according to the selected division
			mainCounterList = queueManagementService.getCountersOfDivision(searchParam.getSectionCode());
			disabledCounter = false;
		} else {
			disabledCounter = true;
		}
	}

	public void onClickSearchBtn() {
		if ((searchParam.getSectionCode() == null || searchParam.getSectionCode().equals(""))
				&& (searchParam.getCounterId() == null || searchParam.getCounterId().equals(""))
				&& searchParam.getDate() == null
				&& (searchParam.getStatus() == null || searchParam.getStatus().equals(""))) {
			errorMessage = "At least one search criteria should be selected.";
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else {
			displayRecordsList = queueManagementService.getUserClosedSearchResults(searchParam);
			// if status ='C' set completedRecords = true, so that complete button can be
			// disabled in the UI, otherwise set completedRecords = false
			if (searchParam.getStatus().equals("C")) {
				completedRecords = true;
			} else {
				completedRecords = false;
			}
		}
	}

	public void onClickClearhBtn() {
		// load ongoing (status!='C') records by default and initiate the UI
		displayRecordsList = queueManagementService.getOngoingQueueRecordsForClosedConsole();
		searchParam = new UserClosedDTO();
		selectedRecord = null;
		disabledCounter = true;
		completedRecords = false;
	}

	public void onClickCompleteBtn(UserClosedDTO selectedRecord) {
		this.selectedRecord = selectedRecord;
	}

	public void completeAction() {
		selectedRecord.setCompletedBy(sessionBackingBean.getLoginUser());
		boolean success = queueManagementService.completeMainQueue(selectedRecord);
		if (success) {
			successMessage = "Completed successfully.";
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			RequestContext.getCurrentInstance().execute("PF('completeConfirm').hide()");

			// load the displayRecordsList again
			if ((searchParam.getSectionCode() == null || searchParam.getSectionCode().equals(""))
					&& (searchParam.getCounterId() == null || searchParam.getCounterId().equals(""))
					&& searchParam.getDate() == null
					&& (searchParam.getStatus() == null || searchParam.getStatus().equals(""))) {
				// load default ongoing (status!='C') records
				displayRecordsList = queueManagementService.getOngoingQueueRecordsForClosedConsole();
			} else {
				// load data according to the entered search parameters
				displayRecordsList = queueManagementService.getUserClosedSearchResults(searchParam);
			}
			RequestContext.getCurrentInstance().update("dataTable");
		} else {
			errorMessage = "Error occurred. Service could not complete.";
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public List<UserClosedDTO> getDisplayRecordsList() {
		return displayRecordsList;
	}

	public void setDisplayRecordsList(List<UserClosedDTO> displayRecordsList) {
		this.displayRecordsList = displayRecordsList;
	}

	public List<DivisionDTO> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<DivisionDTO> divisionList) {
		this.divisionList = divisionList;
	}

	public List<MainCounterDTO> getMainCounterList() {
		return mainCounterList;
	}

	public void setMainCounterList(List<MainCounterDTO> mainCounterList) {
		this.mainCounterList = mainCounterList;
	}

	public UserClosedDTO getSearchParam() {
		return searchParam;
	}

	public void setSearchParam(UserClosedDTO searchParam) {
		this.searchParam = searchParam;
	}

	public boolean isDisabledCounter() {
		return disabledCounter;
	}

	public void setDisabledCounter(boolean disabledCounter) {
		this.disabledCounter = disabledCounter;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(String loggedUser) {
		this.loggedUser = loggedUser;
	}

	public UserClosedDTO getSelectedRecord() {
		return selectedRecord;
	}

	public void setSelectedRecord(UserClosedDTO selectedRecord) {
		this.selectedRecord = selectedRecord;
	}

	public boolean isCompletedRecords() {
		return completedRecords;
	}

	public void setCompletedRecords(boolean completedRecords) {
		this.completedRecords = completedRecords;
	}

}
