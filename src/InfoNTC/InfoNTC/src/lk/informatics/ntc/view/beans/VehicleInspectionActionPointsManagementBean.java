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

@ManagedBean(name = "vehicleInspectionActionPointsManagementBean")
@ViewScoped
public class VehicleInspectionActionPointsManagementBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private ActionPointsManagementDTO actionPointsManagementDTO = new ActionPointsManagementDTO();
	private ActionPointsManagementDTO selectedsectionDes;
	private ActionPointsManagementDTO selectedstatusDes;
	private ActionPointsManagementDTO selectedSeqNo;
	private ActionPointsManagementDTO selectedEditRow;
	private ActionPointsManagementDTO selectedDeleteRow;
	private String selectedSection;
	private String enteredDescription;
	private String selectedStatus;
	private String sucessMsg;
	private String errorMsg;

	private boolean disableSequneceField = false;

	private boolean showbtnSave = true;
	private boolean showbtnUpdate = false;

	public List<ActionPointsManagementDTO> sectionList = new ArrayList<ActionPointsManagementDTO>(0);
	public List<ActionPointsManagementDTO> statusList = new ArrayList<ActionPointsManagementDTO>(0);
	public List<ActionPointsManagementDTO> dataList = new ArrayList<ActionPointsManagementDTO>(0);
	public List<ActionPointsManagementDTO> sequenceCodeList = new ArrayList<ActionPointsManagementDTO>(0);
	public List<ActionPointsManagementDTO> sectionDescriptionsList = new ArrayList<ActionPointsManagementDTO>(0);

	private InspectionActionPointService inspectionActionPointService;

	public VehicleInspectionActionPointsManagementBean() {
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		sectionList = inspectionActionPointService.getSectionList();
		statusList = inspectionActionPointService.getStatusList();

	}

	public void onSectionChange() {
		actionPointsManagementDTO.setSectionCode(selectedSection);
		selectedsectionDes = inspectionActionPointService.getDescriptionForSectionCode(selectedSection);
		actionPointsManagementDTO.setSectionDescription(selectedsectionDes.getSectionDescription());

	}

	public void onstatusChange() {
		actionPointsManagementDTO.setStatusCode(selectedStatus);
		selectedstatusDes = inspectionActionPointService.getDescriptionForStatusCode(selectedStatus);
		actionPointsManagementDTO.setStatusDescription(selectedstatusDes.getStatusDescription());
	}

	public void saveRecord() {
		if (!(actionPointsManagementDTO.getSequence().isEmpty() || selectedSection.isEmpty() || selectedSection == null
				|| enteredDescription.isEmpty() || enteredDescription == null || selectedStatus.isEmpty()
				|| selectedStatus == null)) {
			actionPointsManagementDTO.setDescription(enteredDescription);
			String createdBy = sessionBackingBean.loginUser;
			actionPointsManagementDTO.setCreatedBy(createdBy);

			sequenceCodeList = inspectionActionPointService.getAllSequenceCodeList();
			int size = sequenceCodeList.size();
			boolean isDuplicated = true;
			boolean descriptionDuplicatedForSection = false;
			for (int i = 0; i < size; i++) {

				if (actionPointsManagementDTO.getSequence().equals(sequenceCodeList.get(i).getGeneratedSeqCode())) {

					isDuplicated = true;
					break;
				} else {
					isDuplicated = false;

				}
			}
			sectionDescriptionsList = inspectionActionPointService
					.getAllDesccritionsForCurrentSectionCode(selectedSection);
			for (int i = 0; i < sectionDescriptionsList.size(); i++) {

				if (actionPointsManagementDTO.getDescription()
						.equals(sectionDescriptionsList.get(i).getCheckingSectionDescription())) {

					descriptionDuplicatedForSection = true;
					break;
				} else {
					descriptionDuplicatedForSection = false;

				}
			}
			if (sequenceCodeList.isEmpty()) {
				isDuplicated = false;
			} else {

			}

			if (sectionDescriptionsList.isEmpty()) {
				descriptionDuplicatedForSection = false;
			}

			if (isDuplicated == false) {
				if (descriptionDuplicatedForSection == false) {
					int result = inspectionActionPointService.insertNewSeqNo(actionPointsManagementDTO);
					if (result == 0) {

						RequestContext.getCurrentInstance().update("frmsuccessSve");
						setSucessMsg("Successfully Saved.");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						selectedSeqNo = inspectionActionPointService
								.getGeneratedSeqNo(actionPointsManagementDTO.getSequence());
						actionPointsManagementDTO.setSeq(selectedSeqNo.getSeq());
						ActionPointsManagementDTO actionPointsManagementDTO1 = new ActionPointsManagementDTO();
						actionPointsManagementDTO1.setSequence(actionPointsManagementDTO.getSequence());
						actionPointsManagementDTO1
								.setSectionDescription(actionPointsManagementDTO.getSectionDescription());
						actionPointsManagementDTO1.setDescription(actionPointsManagementDTO.getDescription());
						actionPointsManagementDTO1
								.setStatusDescription(actionPointsManagementDTO.getStatusDescription());
						actionPointsManagementDTO1.setSectionCode(actionPointsManagementDTO.getSectionCode());
						actionPointsManagementDTO1.setStatusCode(actionPointsManagementDTO.getStatusCode());
						actionPointsManagementDTO1.setSeq(actionPointsManagementDTO.getSeq());
						dataList.add(actionPointsManagementDTO1);
						actionPointsManagementDTO.setSequence(null);
						setSelectedSection(null);
						setEnteredDescription(null);
						setSelectedStatus(null);

					} else {

						errorMsg = "Sequence should be unique.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Section Description should be unique.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Sequence should be unique.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else if (actionPointsManagementDTO.getSequence().isEmpty() || selectedSection.isEmpty()
				|| selectedSection == null || enteredDescription.isEmpty() || enteredDescription == null
				|| selectedStatus.isEmpty() || selectedStatus == null) {

			if (actionPointsManagementDTO.getSequence().isEmpty()) {
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

	public void clearFields() {
		actionPointsManagementDTO.setSequence(null);
		setSelectedSection(null);
		setEnteredDescription(null);
		setSelectedStatus(null);
		setDisableSequneceField(false);
		setShowbtnSave(true);
		setShowbtnUpdate(false);
	}

	public void editActButtonAction() {

		actionPointsManagementDTO.setSequence(selectedEditRow.getSequence());
		boolean resultValue = inspectionActionPointService.checkAsigned(actionPointsManagementDTO.getSequence(),
				actionPointsManagementDTO.getSeq());
		if (resultValue == false) {
			actionPointsManagementDTO.setSequence(selectedEditRow.getSequence());
			setSelectedSection(selectedEditRow.getSectionCode());
			setEnteredDescription(selectedEditRow.getDescription());
			setSelectedStatus(selectedEditRow.getStatusCode());
			actionPointsManagementDTO.setSeq(selectedEditRow.getSeq());
			setDisableSequneceField(true);
			setShowbtnSave(false);
			setShowbtnUpdate(true);

		} else {
			errorMsg = "You can not edit this record.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void updateRecord() {
		if (!(actionPointsManagementDTO.getSequence().isEmpty() || selectedSection.isEmpty() || selectedSection == null
				|| enteredDescription.isEmpty() || enteredDescription == null || selectedStatus.isEmpty()
				|| selectedStatus == null)) {
			String beforeSectionDescription = selectedEditRow.getDescription();

			boolean descriptionDuplicatedForSection = false;
			sectionDescriptionsList = inspectionActionPointService
					.getAllDesccritionsForCurrentSectionCode(selectedSection);
			for (int i = 0; i < sectionDescriptionsList.size(); i++) {

				if (actionPointsManagementDTO.getDescription()
						.equals(sectionDescriptionsList.get(i).getCheckingSectionDescription())) {

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
				actionPointsManagementDTO.setSequence(actionPointsManagementDTO.getSequence());
				actionPointsManagementDTO.setSectionCode(selectedSection);
				selectedsectionDes = inspectionActionPointService.getDescriptionForSectionCode(selectedSection);
				actionPointsManagementDTO.setSectionDescription(selectedsectionDes.getSectionDescription());
				actionPointsManagementDTO.setDescription(enteredDescription);
				actionPointsManagementDTO.setStatusCode(selectedStatus);
				selectedstatusDes = inspectionActionPointService.getDescriptionForStatusCode(selectedStatus);
				actionPointsManagementDTO.setStatusDescription(selectedstatusDes.getStatusDescription());
				actionPointsManagementDTO.setSeq(actionPointsManagementDTO.getSeq());
				String modifyBy = sessionBackingBean.loginUser;
				actionPointsManagementDTO.setModifyBy(modifyBy);

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
					setDisableSequneceField(false);
					setShowbtnSave(true);
					setShowbtnUpdate(false);
					actionPointsManagementDTO.setSequence(null);
					setSelectedSection(null);
					setEnteredDescription(null);
					setSelectedStatus(null);

				}
			} else {
				errorMsg = "Section Description should be unique.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else if (actionPointsManagementDTO.getSequence().isEmpty() || selectedSection.isEmpty()
				|| selectedSection == null || enteredDescription.isEmpty() || enteredDescription == null
				|| selectedStatus.isEmpty() || selectedStatus == null) {

			if (actionPointsManagementDTO.getSequence().isEmpty()) {
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

	public void deleteActButtonAction() {
		RequestContext.getCurrentInstance().update("frmAddVIAPM");
		actionPointsManagementDTO.setSeq(selectedDeleteRow.getSeq());
		actionPointsManagementDTO.setSequence(selectedDeleteRow.getSequence());
		boolean resultValue = inspectionActionPointService.checkAsigned(actionPointsManagementDTO.getSequence(),
				actionPointsManagementDTO.getSeq());
		if (resultValue == false) {
			RequestContext.getCurrentInstance().execute("PF('deleteconfirmationsequence').show()");
			clearFields();
		} else {
			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			clearFields();
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
			RequestContext.getCurrentInstance().update("frmVehicleInspectionTable");
		} else {
			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public List<ActionPointsManagementDTO> getSequenceCodeList() {
		return sequenceCodeList;
	}

	public void setSequenceCodeList(List<ActionPointsManagementDTO> sequenceCodeList) {
		this.sequenceCodeList = sequenceCodeList;
	}

	public ActionPointsManagementDTO getSelectedDeleteRow() {
		return selectedDeleteRow;
	}

	public void setSelectedDeleteRow(ActionPointsManagementDTO selectedDeleteRow) {
		this.selectedDeleteRow = selectedDeleteRow;
	}

	public ActionPointsManagementDTO getSelectedSeqNo() {
		return selectedSeqNo;
	}

	public void setSelectedSeqNo(ActionPointsManagementDTO selectedSeqNo) {
		this.selectedSeqNo = selectedSeqNo;
	}

	public boolean isShowbtnSave() {
		return showbtnSave;
	}

	public void setShowbtnSave(boolean showbtnSave) {
		this.showbtnSave = showbtnSave;
	}

	public boolean isShowbtnUpdate() {
		return showbtnUpdate;
	}

	public void setShowbtnUpdate(boolean showbtnUpdate) {
		this.showbtnUpdate = showbtnUpdate;
	}

	public boolean isDisableSequneceField() {
		return disableSequneceField;
	}

	public void setDisableSequneceField(boolean disableSequneceField) {
		this.disableSequneceField = disableSequneceField;
	}

	public ActionPointsManagementDTO getSelectedEditRow() {
		return selectedEditRow;
	}

	public void setSelectedEditRow(ActionPointsManagementDTO selectedEditRow) {
		this.selectedEditRow = selectedEditRow;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public ActionPointsManagementDTO getSelectedsectionDes() {
		return selectedsectionDes;
	}

	public void setSelectedsectionDes(ActionPointsManagementDTO selectedsectionDes) {
		this.selectedsectionDes = selectedsectionDes;
	}

	public ActionPointsManagementDTO getSelectedstatusDes() {
		return selectedstatusDes;
	}

	public void setSelectedstatusDes(ActionPointsManagementDTO selectedstatusDes) {
		this.selectedstatusDes = selectedstatusDes;
	}

	public List<ActionPointsManagementDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<ActionPointsManagementDTO> dataList) {
		this.dataList = dataList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public List<ActionPointsManagementDTO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<ActionPointsManagementDTO> statusList) {
		this.statusList = statusList;
	}

	public List<ActionPointsManagementDTO> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<ActionPointsManagementDTO> sectionList) {
		this.sectionList = sectionList;
	}

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public String getEnteredDescription() {
		return enteredDescription;
	}

	public void setEnteredDescription(String enteredDescription) {
		this.enteredDescription = enteredDescription;
	}

	public String getSelectedSection() {
		return selectedSection;
	}

	public void setSelectedSection(String selectedSection) {
		this.selectedSection = selectedSection;
	}

	public ActionPointsManagementDTO getActionPointsManagementDTO() {
		return actionPointsManagementDTO;
	}

	public void setActionPointsManagementDTO(ActionPointsManagementDTO actionPointsManagementDTO) {
		this.actionPointsManagementDTO = actionPointsManagementDTO;
	}

	public List<ActionPointsManagementDTO> getSectionDescriptionsList() {
		return sectionDescriptionsList;
	}

	public void setSectionDescriptionsList(List<ActionPointsManagementDTO> sectionDescriptionsList) {
		this.sectionDescriptionsList = sectionDescriptionsList;
	}

}
