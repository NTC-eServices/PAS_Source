package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AssignBranchesDTO;
import lk.informatics.ntc.model.service.AssignBranchesService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "assignBranchesBackingBean")
@ViewScoped
public class AssignBranchesBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	AssignBranchesDTO assignBranchesDTO;
	AssignBranchesDTO deleteAssignBranchesDTO;
	AssignBranchesService assignBranchesService;
	private List<AssignBranchesDTO> getBankList = new ArrayList<AssignBranchesDTO>(0);
	private List<AssignBranchesDTO> assignedBranches = new ArrayList<AssignBranchesDTO>(0);

	Boolean searchedRecord = false, disableBranchCode = false;
	String errorMessage, successMessage, infoMessage;
	String username, bankCodeStr;

	@PostConstruct
	public void init() {
		assignBranchesDTO = new AssignBranchesDTO();
		assignBranchesService = (AssignBranchesService) SpringApplicationContex.getBean("assignBranchesService");
		getBankList = assignBranchesService.getBankCode();
	}

	public void onBankCodeSelect() {
		searchedRecord = false;
		bankCodeStr = assignBranchesDTO.getBankCode();
		clearAddForm();
		assignedBranches.clear();
		assignBranchesDTO.setBankDes(assignBranchesService.getBranchName(assignBranchesDTO.getBankCode()));
	}

	@SuppressWarnings("deprecation")
	public void search() {
		if (assignBranchesDTO.getBankCode() != null && !assignBranchesDTO.getBankCode().trim().isEmpty()) {
			assignedBranches = assignBranchesService.getAssignedBranchesList(assignBranchesDTO.getBankCode());
			searchedRecord = true;
		} else {
			setErrorMessage("Please select bank code.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearPage() {
		assignBranchesDTO.setBankCode(null);
		assignBranchesDTO.setBankDes(null);
		assignBranchesDTO.setBranch_description_english(null);
		assignBranchesDTO.setBranch_description_sinhala(null);
		assignBranchesDTO.setBranch_description_tamil(null);
		assignBranchesDTO.setBranchCode(null);
		assignedBranches.clear();
		searchedRecord = false;
		disableBranchCode = false;
	}

	@SuppressWarnings("deprecation")
	public void saveBranchesFunction() {
		if (assignBranchesDTO.getBranchCode() == null || assignBranchesDTO.getBranchCode().trim().isEmpty()) {
			setErrorMessage("Please insert branch code.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (assignBranchesDTO.getBranch_description_english() == null
				|| assignBranchesDTO.getBranch_description_english().trim().isEmpty()) {
			setErrorMessage("Please insert branch name(English).");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (assignBranchesDTO.getBranch_description_sinhala() == null
				|| assignBranchesDTO.getBranch_description_sinhala().trim().isEmpty()) {
			setErrorMessage("Please insert branch name(Sinhala).");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (assignBranchesDTO.getBranch_description_tamil() == null
				|| assignBranchesDTO.getBranch_description_tamil().trim().isEmpty()) {
			setErrorMessage("Please insert branch name(Tamil).");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else {
			username = sessionBackingBean.getLoginUser();
			if (assignBranchesDTO.isEditRecord()) {
				assignBranchesDTO.setEditRecord(false);
				assignBranchesService.editRecord(assignBranchesDTO, username);
				disableBranchCode = false;
				setSuccessMessage("Updated successsfully.");
				assignedBranches = assignBranchesService.getAssignedBranchesList(assignBranchesDTO.getBankCode());
				clearAddForm();
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				if (assignBranchesService.isCodeDuplicate(assignBranchesDTO.getBankCode(),
						assignBranchesDTO.getBranchCode())) {
					setErrorMessage("Duplicate branch code.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					assignBranchesService.saveRecord(assignBranchesDTO, username);
					setSuccessMessage("Added successsfully.");
					assignedBranches = assignBranchesService.getAssignedBranchesList(assignBranchesDTO.getBankCode());
					clearAddForm();
					RequestContext.getCurrentInstance().update("successMSG");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				}
			}
		}

	}

	public void clearAddForm() {
		if (assignBranchesDTO.isEditRecord()) {
			assignBranchesDTO.setEditRecord(false);
		}
		assignBranchesDTO.setBranch_description_english(null);
		assignBranchesDTO.setBranch_description_sinhala(null);
		assignBranchesDTO.setBranch_description_tamil(null);
		assignBranchesDTO.setBranchCode(null);
		disableBranchCode = false;
	}

	@SuppressWarnings("deprecation")
	public void deleteAction() {
		clearAddForm();
		if (deleteAssignBranchesDTO != null) {

			int result = assignBranchesService.deleteRecord(deleteAssignBranchesDTO, bankCodeStr);
			if (result < 0) {

				setErrorMessage("Delete unsuccesssful.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				deleteAssignBranchesDTO = null;

				return;
			} else {
				assignedBranches = assignBranchesService.getAssignedBranchesList(bankCodeStr);

				setSuccessMessage("Deleted successsfully.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				deleteAssignBranchesDTO = null;

				return;
			}
		} else {

			setErrorMessage("Delete unsuccesssful.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			deleteAssignBranchesDTO = null;
			return;
		}
	}

	public void editAction(AssignBranchesDTO item) {
		disableBranchCode = true;
		assignBranchesDTO.setBranchCode(item.getBranchCode());
		assignBranchesDTO.setBranch_description_english(item.getBranch_description_english());
		assignBranchesDTO.setBranch_description_sinhala(item.getBranch_description_sinhala());
		assignBranchesDTO.setBranch_description_tamil(item.getBranch_description_tamil());
		assignBranchesDTO.setEditRecord(true);
	}

	public AssignBranchesDTO getAssignBranchesDTO() {
		return assignBranchesDTO;
	}

	public void setAssignBranchesDTO(AssignBranchesDTO assignBranchesDTO) {
		this.assignBranchesDTO = assignBranchesDTO;
	}

	public AssignBranchesService getAssignBranchesService() {
		return assignBranchesService;
	}

	public void setAssignBranchesService(AssignBranchesService assignBranchesService) {
		this.assignBranchesService = assignBranchesService;
	}

	public List<AssignBranchesDTO> getGetBankList() {
		return getBankList;
	}

	public void setGetBankList(List<AssignBranchesDTO> getBankList) {
		this.getBankList = getBankList;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public Boolean getSearchedRecord() {
		return searchedRecord;
	}

	public void setSearchedRecord(Boolean searchedRecord) {
		this.searchedRecord = searchedRecord;
	}

	public List<AssignBranchesDTO> getAssignedBranches() {
		return assignedBranches;
	}

	public void setAssignedBranches(List<AssignBranchesDTO> assignedBranches) {
		this.assignedBranches = assignedBranches;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public AssignBranchesDTO getDeleteAssignBranchesDTO() {
		return deleteAssignBranchesDTO;
	}

	public void setDeleteAssignBranchesDTO(AssignBranchesDTO deleteAssignBranchesDTO) {
		this.deleteAssignBranchesDTO = deleteAssignBranchesDTO;
	}

	public Boolean getDisableBranchCode() {
		return disableBranchCode;
	}

	public void setDisableBranchCode(Boolean disableBranchCode) {
		this.disableBranchCode = disableBranchCode;
	}
}
