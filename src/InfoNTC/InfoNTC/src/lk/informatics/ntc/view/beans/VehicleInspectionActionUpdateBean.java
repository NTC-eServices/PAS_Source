package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.ActionPointsManagementDTO;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "vehicleInspectionActionUpdateBean")
@ViewScoped
public class VehicleInspectionActionUpdateBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private String serachedSeqCode;
	private String searchedSection;
	private String searchedStatus;
	private String selectedSeqCode;
	private String selectedSection;
	private String enteredDescription;
	private String selectedStatus;
	private String errorMsg;
	private String sucessMsg;

	private boolean disableSeqCodeField = true;
	private boolean disableSectionField = true;
	private boolean disabledDesField = true;
	private boolean disabledStatusField = true;
	private boolean disableSaveBtn = true;
	private boolean disableClearBtn = true;
	private boolean pressedSearchBtn = false;

	private ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
	private ActionPointsManagementDTO selectedsectionDes;
	private ActionPointsManagementDTO selectedstatusDes;
	private ActionPointsManagementDTO selectedEditRow;
	private ActionPointsManagementDTO selectedDeleteRow;
	private ActionPointsManagementDTO searchedSectionAndStatus;

	public List<ActionPointsManagementDTO> sequenceCodeList = new ArrayList<ActionPointsManagementDTO>(0);
	public List<ActionPointsManagementDTO> sectionList = new ArrayList<ActionPointsManagementDTO>(0);
	public List<ActionPointsManagementDTO> statusList = new ArrayList<ActionPointsManagementDTO>(0);
	public List<ActionPointsManagementDTO> dataList = new ArrayList<ActionPointsManagementDTO>(0);
	public List<ActionPointsManagementDTO> sectionDescriptionsList = new ArrayList<ActionPointsManagementDTO>(0);

	private InspectionActionPointService inspectionActionPointService;

	public VehicleInspectionActionUpdateBean() {
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		sequenceCodeList = inspectionActionPointService.getAllSequenceCodeList();
		sectionList = inspectionActionPointService.getSectionList();
		statusList = inspectionActionPointService.getStatusList();
		dataList = inspectionActionPointService.viewAllRecords();

	}

	public void onSeqCodeChange() {
		actionPointsManagementDTO.setSequence(serachedSeqCode);

		searchedSectionAndStatus = inspectionActionPointService.getCurrentSectionAndStatus(serachedSeqCode);
		searchedSection = searchedSectionAndStatus.getSectionCode();
		searchedStatus = searchedSectionAndStatus.getStatusCode();
	}

	public void onSectionChange() {
		actionPointsManagementDTO.setSectionCode(searchedSection);
		selectedsectionDes = inspectionActionPointService.getDescriptionForSectionCode(searchedSection);
		actionPointsManagementDTO.setSectionDescription(selectedsectionDes.getSectionDescription());
	}

	public void onstatusChange() {
		actionPointsManagementDTO.setStatusCode(searchedStatus);
		selectedstatusDes = inspectionActionPointService.getDescriptionForStatusCode(searchedStatus);
		actionPointsManagementDTO.setStatusDescription(selectedstatusDes.getStatusDescription());
	}

	public void oneditstatusChange() {
		actionPointsManagementDTO.setStatusCode(selectedStatus);
		selectedstatusDes = inspectionActionPointService.getDescriptionForStatusCode(selectedStatus);
		actionPointsManagementDTO.setStatusDescription(selectedstatusDes.getStatusDescription());
	}

	public void serachRecords() {

		if (!serachedSeqCode.isEmpty() && serachedSeqCode != null && !serachedSeqCode.equals("")
				&& searchedSection.equals("") && searchedStatus.equals("")) {

			setPressedSearchBtn(true);
			dataList = new ArrayList<ActionPointsManagementDTO>(0);
			dataList = inspectionActionPointService.getSearchedRecords(serachedSeqCode, searchedSection,
					searchedStatus);
		} else if (!searchedSection.isEmpty() && searchedSection != null && !searchedSection.equals("")
				&& serachedSeqCode.equals("") && searchedStatus.equals("")) {

			setPressedSearchBtn(true);
			dataList = new ArrayList<ActionPointsManagementDTO>(0);
			dataList = inspectionActionPointService.getSearchedRecords(serachedSeqCode, searchedSection,
					searchedStatus);
		} else if (!searchedStatus.isEmpty() && searchedStatus != null && !searchedStatus.equals("")
				&& serachedSeqCode.equals("") && searchedSection.equals("")) {

			setPressedSearchBtn(true);
			dataList = new ArrayList<ActionPointsManagementDTO>(0);
			dataList = inspectionActionPointService.getSearchedRecords(serachedSeqCode, searchedSection,
					searchedStatus);
		} else if (!serachedSeqCode.isEmpty() && serachedSeqCode != null && !serachedSeqCode.equals("")
				&& !searchedSection.isEmpty() && searchedSection != null && !searchedSection.equals("")
				&& searchedStatus.equals("")) {

			setPressedSearchBtn(true);
			dataList = new ArrayList<ActionPointsManagementDTO>(0);
			dataList = inspectionActionPointService.getSearchedRecordsOne(serachedSeqCode, searchedSection);
		} else if (!serachedSeqCode.isEmpty() && serachedSeqCode != null && !serachedSeqCode.equals("")
				&& !searchedStatus.isEmpty() && searchedStatus != null && !searchedStatus.equals("")
				&& searchedSection.equals("")) {

			setPressedSearchBtn(true);
			dataList = new ArrayList<ActionPointsManagementDTO>(0);
			dataList = inspectionActionPointService.getSearchedRecordsTwo(serachedSeqCode, searchedStatus);
		} else if (!searchedSection.isEmpty() && searchedSection != null && !searchedSection.equals("")
				&& !searchedStatus.isEmpty() && searchedStatus != null && !searchedStatus.equals("")
				&& serachedSeqCode.equals("")) {

			setPressedSearchBtn(true);
			dataList = new ArrayList<ActionPointsManagementDTO>(0);
			dataList = inspectionActionPointService.getSearchedRecordsThree(searchedSection, searchedStatus);
		} else if (!serachedSeqCode.isEmpty() && serachedSeqCode != null && !serachedSeqCode.equals("")
				&& !searchedSection.isEmpty() && searchedSection != null && !searchedSection.equals("")
				&& !searchedStatus.isEmpty() && searchedStatus != null && !searchedStatus.equals("")) {

			setPressedSearchBtn(true);
			dataList = new ArrayList<ActionPointsManagementDTO>(0);
			dataList = inspectionActionPointService.getSearchedRecordsFour(serachedSeqCode, searchedSection,
					searchedStatus);
		} else if (serachedSeqCode.equals("") && searchedSection.equals("") && searchedStatus.equals("")) {

			errorMsg = "Fields should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clearPage() {

		setSearchedSection(null);
		setSearchedStatus(null);
		setSerachedSeqCode(null);
		dataList = inspectionActionPointService.viewAllRecords();
	}

	public void updateRecord() {

		if (!(selectedSeqCode.isEmpty() || selectedSection == null || selectedSection.equals("")
				|| enteredDescription.isEmpty() || selectedStatus == null || enteredDescription.equals(""))) {

			String beforeSectionDescription = selectedEditRow.getDescription();

			boolean descriptionDuplicatedForSection = false;
			sectionDescriptionsList = inspectionActionPointService
					.getAllDesccritionsForCurrentSectionCode(selectedSection);
			for (int i = 0; i < sectionDescriptionsList.size(); i++) {

				if (enteredDescription.equals(sectionDescriptionsList.get(i).getCheckingSectionDescription())) {

					descriptionDuplicatedForSection = true;
					break;
				} else {
					descriptionDuplicatedForSection = false;

				}
			}
			if (sectionDescriptionsList.isEmpty()) {
				descriptionDuplicatedForSection = false;
			} else {

			}
			if (descriptionDuplicatedForSection == false || beforeSectionDescription.equals(enteredDescription)) {
				String modifyBy = sessionBackingBean.loginUser;
				actionPointsManagementDTO.setModifyBy(modifyBy);
				actionPointsManagementDTO.setSequence(actionPointsManagementDTO.getSequence());
				actionPointsManagementDTO.setSectionCode(actionPointsManagementDTO.getSectionCode());
				selectedsectionDes = inspectionActionPointService
						.getDescriptionForSectionCode(actionPointsManagementDTO.getSectionCode());
				actionPointsManagementDTO.setSectionDescription(selectedsectionDes.getSectionDescription());
				actionPointsManagementDTO.setDescription(enteredDescription);
				actionPointsManagementDTO.setStatusCode(selectedStatus);
				selectedstatusDes = inspectionActionPointService.getDescriptionForStatusCode(selectedStatus);
				actionPointsManagementDTO.setStatusDescription(selectedstatusDes.getStatusDescription());
				actionPointsManagementDTO.setSeq(actionPointsManagementDTO.getSeq());

				int result = inspectionActionPointService.updateSeqRecord(actionPointsManagementDTO);
				if (result == 0) {

					RequestContext.getCurrentInstance().update("frmsuccessSve");
					setSucessMsg("Successfully Saved.");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					selectedEditRow.setSeq(actionPointsManagementDTO.getSeq());
					selectedEditRow.setSequence(actionPointsManagementDTO.getSequence());
					selectedEditRow.setSectionCode(actionPointsManagementDTO.getSectionCode());
					selectedEditRow.setSectionDescription(actionPointsManagementDTO.getSectionDescription());
					selectedEditRow.setDescription(actionPointsManagementDTO.getDescription());
					selectedEditRow.setStatusCode(actionPointsManagementDTO.getStatusCode());
					selectedEditRow.setStatusDescription(actionPointsManagementDTO.getStatusDescription());
					if (pressedSearchBtn == false) {
						dataList = inspectionActionPointService.viewAllRecords();

					}
					clearAddForm();

				}
			} else {

				errorMsg = "Section Description should be unique.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else if (selectedSeqCode.isEmpty() || selectedSection == null || selectedSection.equals("")
				|| enteredDescription.isEmpty() || selectedStatus == null || enteredDescription.equals("")) {

			if (selectedSeqCode.isEmpty()) {

				errorMsg = "Sequence should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (selectedSection.isEmpty() || selectedSection == null) {

				errorMsg = "Section should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (enteredDescription.isEmpty() || enteredDescription == null) {

				errorMsg = "Description should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (selectedStatus.isEmpty() || selectedStatus == null) {

				errorMsg = "Status should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
	}

	public void clearAddForm() {
		setSelectedSeqCode(null);
		setSelectedSection(null);
		setEnteredDescription(null);
		setSelectedStatus(null);
		setDisabledDesField(true);
		setDisabledStatusField(true);
		setDisableClearBtn(true);
		setDisableSaveBtn(true);
	}

	public void editDetails() {
		actionPointsManagementDTO.setSequence(selectedEditRow.getSequence());
		actionPointsManagementDTO.setSeq(selectedEditRow.getSeq());
		boolean resultValue = inspectionActionPointService.checkAsigned(actionPointsManagementDTO.getSequence(),
				actionPointsManagementDTO.getSeq());
		if (resultValue == false) {
			actionPointsManagementDTO.setSequence(selectedEditRow.getSequence());
			actionPointsManagementDTO.setSeq(selectedEditRow.getSeq());
			actionPointsManagementDTO.setSectionCode(selectedEditRow.getSectionCode());
			actionPointsManagementDTO.setSectionDescription(selectedEditRow.getSectionDescription());
			actionPointsManagementDTO.setDescription(selectedEditRow.getDescription());
			actionPointsManagementDTO.setStatusCode(selectedEditRow.getStatusCode());
			actionPointsManagementDTO.setStatusDescription(selectedEditRow.getStatusDescription());
			setSelectedSeqCode(actionPointsManagementDTO.getSequence());
			setSelectedSection(actionPointsManagementDTO.getSectionCode());
			setEnteredDescription(actionPointsManagementDTO.getDescription());
			setSelectedStatus(actionPointsManagementDTO.getStatusCode());
			setDisabledDesField(false);
			setDisabledStatusField(false);
			setDisableClearBtn(false);
			setDisableSaveBtn(false);
		} else {
			errorMsg = "You can not edit this record.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void deleteActButtonAction() {
		RequestContext.getCurrentInstance().update("frmseqRecoredEdit");
		actionPointsManagementDTO.setSeq(selectedDeleteRow.getSeq());
		actionPointsManagementDTO.setSequence(selectedDeleteRow.getSequence());
		boolean resultValue = inspectionActionPointService.checkAsigned(actionPointsManagementDTO.getSequence(),
				actionPointsManagementDTO.getSeq());
		if (resultValue == false) {
			RequestContext.getCurrentInstance().execute("PF('deleteconfirmationsequence').show()");
			clearAddForm();
		} else {

			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			clearAddForm();
		}
	}

	public void deleteRole() {

		RequestContext.getCurrentInstance().update("@form");
		actionPointsManagementDTO.setSeq(selectedDeleteRow.getSeq());
		actionPointsManagementDTO.setSequence(selectedDeleteRow.getSequence());
		int result = inspectionActionPointService.deleteSequenceRecord(actionPointsManagementDTO.getSeq());
		if (result == 0) {
			RequestContext.getCurrentInstance().update("frmsuccessSve");

			sucessMsg = "Successfully Deleted.";
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			dataList.remove(selectedDeleteRow);
			RequestContext.getCurrentInstance().update("frmFunActTable");

		} else {
			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public boolean isPressedSearchBtn() {
		return pressedSearchBtn;
	}

	public void setPressedSearchBtn(boolean pressedSearchBtn) {
		this.pressedSearchBtn = pressedSearchBtn;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public ActionPointsManagementDTO getSelectedEditRow() {
		return selectedEditRow;
	}

	public void setSelectedEditRow(ActionPointsManagementDTO selectedEditRow) {
		this.selectedEditRow = selectedEditRow;
	}

	public ActionPointsManagementDTO getSelectedDeleteRow() {
		return selectedDeleteRow;
	}

	public void setSelectedDeleteRow(ActionPointsManagementDTO selectedDeleteRow) {
		this.selectedDeleteRow = selectedDeleteRow;
	}

	public String getSelectedSection() {
		return selectedSection;
	}

	public void setSelectedSection(String selectedSection) {
		this.selectedSection = selectedSection;
	}

	public String getEnteredDescription() {
		return enteredDescription;
	}

	public void setEnteredDescription(String enteredDescription) {
		this.enteredDescription = enteredDescription;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public boolean isDisableSeqCodeField() {
		return disableSeqCodeField;
	}

	public void setDisableSeqCodeField(boolean disableSeqCodeField) {
		this.disableSeqCodeField = disableSeqCodeField;
	}

	public boolean isDisableSectionField() {
		return disableSectionField;
	}

	public void setDisableSectionField(boolean disableSectionField) {
		this.disableSectionField = disableSectionField;
	}

	public boolean isDisabledDesField() {
		return disabledDesField;
	}

	public void setDisabledDesField(boolean disabledDesField) {
		this.disabledDesField = disabledDesField;
	}

	public boolean isDisabledStatusField() {
		return disabledStatusField;
	}

	public void setDisabledStatusField(boolean disabledStatusField) {
		this.disabledStatusField = disabledStatusField;
	}

	public boolean isDisableSaveBtn() {
		return disableSaveBtn;
	}

	public void setDisableSaveBtn(boolean disableSaveBtn) {
		this.disableSaveBtn = disableSaveBtn;
	}

	public boolean isDisableClearBtn() {
		return disableClearBtn;
	}

	public void setDisableClearBtn(boolean disableClearBtn) {
		this.disableClearBtn = disableClearBtn;
	}

	public String getSelectedSeqCode() {
		return selectedSeqCode;
	}

	public void setSelectedSeqCode(String selectedSeqCode) {
		this.selectedSeqCode = selectedSeqCode;
	}

	public List<ActionPointsManagementDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<ActionPointsManagementDTO> dataList) {
		this.dataList = dataList;
	}

	public ActionPointsManagementDTO getSelectedstatusDes() {
		return selectedstatusDes;
	}

	public void setSelectedstatusDes(ActionPointsManagementDTO selectedstatusDes) {
		this.selectedstatusDes = selectedstatusDes;
	}

	public List<ActionPointsManagementDTO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<ActionPointsManagementDTO> statusList) {
		this.statusList = statusList;
	}

	public String getSearchedStatus() {
		return searchedStatus;
	}

	public void setSearchedStatus(String searchedStatus) {
		this.searchedStatus = searchedStatus;
	}

	public ActionPointsManagementDTO getSelectedsectionDes() {
		return selectedsectionDes;
	}

	public void setSelectedsectionDes(ActionPointsManagementDTO selectedsectionDes) {
		this.selectedsectionDes = selectedsectionDes;
	}

	public String getSearchedSection() {
		return searchedSection;
	}

	public void setSearchedSection(String searchedSection) {
		this.searchedSection = searchedSection;
	}

	public List<ActionPointsManagementDTO> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<ActionPointsManagementDTO> sectionList) {
		this.sectionList = sectionList;
	}

	public List<ActionPointsManagementDTO> getSequenceCodeList() {
		return sequenceCodeList;
	}

	public void setSequenceCodeList(List<ActionPointsManagementDTO> sequenceCodeList) {
		this.sequenceCodeList = sequenceCodeList;
	}

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getSerachedSeqCode() {
		return serachedSeqCode;
	}

	public void setSerachedSeqCode(String serachedSeqCode) {
		this.serachedSeqCode = serachedSeqCode;
	}

	public ActionPointsManagementDTO getActionPointsManagementDTO() {
		return actionPointsManagementDTO;
	}

	public void setActionPointsManagementDTO(ActionPointsManagementDTO actionPointsManagementDTO) {
		this.actionPointsManagementDTO = actionPointsManagementDTO;
	}

	public ActionPointsManagementDTO getSearchedSectionAndStatus() {
		return searchedSectionAndStatus;
	}

	public void setSearchedSectionAndStatus(ActionPointsManagementDTO searchedSectionAndStatus) {
		this.searchedSectionAndStatus = searchedSectionAndStatus;
	}

	public List<ActionPointsManagementDTO> getSectionDescriptionsList() {
		return sectionDescriptionsList;
	}

	public void setSectionDescriptionsList(List<ActionPointsManagementDTO> sectionDescriptionsList) {
		this.sectionDescriptionsList = sectionDescriptionsList;
	}

}
