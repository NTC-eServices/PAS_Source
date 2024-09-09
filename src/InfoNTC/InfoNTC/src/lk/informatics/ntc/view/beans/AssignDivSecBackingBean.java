package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AssignDivSecDTO;
import lk.informatics.ntc.model.service.AssignDivSecService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "assignDivSecBackingBean")
@ViewScoped
public class AssignDivSecBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	AssignDivSecDTO assignDivSecDTO;
	AssignDivSecDTO deleteAssignDivSecDTO;
	AssignDivSecService assignDivSecService;
	private List<AssignDivSecDTO> getDistrictList = new ArrayList<AssignDivSecDTO>(0);
	private List<AssignDivSecDTO> assignedDivSec = new ArrayList<AssignDivSecDTO>(0);

	Boolean searchedRecord = false, disableBranchCode = false;
	String errorMessage, successMessage, infoMessage;
	String username, districtCodeStr;

	@PostConstruct
	public void init() {
		assignDivSecDTO = new AssignDivSecDTO();
		assignDivSecService = (AssignDivSecService) SpringApplicationContex.getBean("assignDivSecService");
		getDistrictList = assignDivSecService.getDistrictCode();
	}

	public void onDistrictCodeSelect() {
		searchedRecord = false;
		districtCodeStr = assignDivSecDTO.getDistrictCode();
		clearAddForm();
		assignedDivSec.clear();
		assignDivSecDTO.setDistrictDes(assignDivSecService.getDivSecName(assignDivSecDTO.getDistrictCode()));
	}

	@SuppressWarnings("deprecation")
	public void search() {
		if (assignDivSecDTO.getDistrictCode() != null && !assignDivSecDTO.getDistrictCode().trim().isEmpty()) {
			assignedDivSec = assignDivSecService.getAssignedDivSecList(assignDivSecDTO.getDistrictCode());
			searchedRecord = true;
		} else {
			setErrorMessage("Please select District Code.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearPage() {
		assignDivSecDTO.setDistrictCode(null);
		assignDivSecDTO.setDistrictDes(null);
		assignDivSecDTO.setDivSec_description_english(null);
		assignDivSecDTO.setDivSec_description_sinhala(null);
		assignDivSecDTO.setDivSec_description_tamil(null);
		assignDivSecDTO.setDivSecCode(null);
		assignedDivSec.clear();
		searchedRecord = false;
		disableBranchCode = false;
	}

	@SuppressWarnings("deprecation")
	public void saveDivSecFunction() {
		if (assignDivSecDTO.getDivSecCode() == null || assignDivSecDTO.getDivSecCode().trim().isEmpty()) {
			setErrorMessage("Please insert D.S. Division Code.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (assignDivSecDTO.getDivSec_description_english() == null
				|| assignDivSecDTO.getDivSec_description_english().trim().isEmpty()) {
			setErrorMessage("Please insert D.S. Division Name (English).");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (assignDivSecDTO.getDivSec_description_sinhala() == null
				|| assignDivSecDTO.getDivSec_description_sinhala().trim().isEmpty()) {
			setErrorMessage("Please insert D.S. Division Name (Sinhala).");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (assignDivSecDTO.getDivSec_description_tamil() == null
				|| assignDivSecDTO.getDivSec_description_tamil().trim().isEmpty()) {
			setErrorMessage("Please insert D.S. Division Name (Tamil).");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else {
			username = sessionBackingBean.getLoginUser();
			if (assignDivSecDTO.isEditRecord()) {
				assignDivSecDTO.setEditRecord(false);
				assignDivSecDTO
						.setDivSecCode(assignDivSecDTO.getDistrictCode() + "-" + assignDivSecDTO.getDivSecCode());
				assignDivSecService.editRecord(assignDivSecDTO, username);
				disableBranchCode = false;
				setSuccessMessage("Updated successsfully.");
				assignedDivSec = assignDivSecService.getAssignedDivSecList(assignDivSecDTO.getDistrictCode());
				clearAddForm();
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				if (assignDivSecService.isCodeDuplicate(assignDivSecDTO.getDistrictCode(),
						assignDivSecDTO.getDivSecCode())) {
					assignDivSecDTO.setDivSecCode(null);
					setErrorMessage("Duplicate D.S. Division Code.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					assignDivSecDTO
							.setDivSecCode(assignDivSecDTO.getDistrictCode() + "-" + assignDivSecDTO.getDivSecCode());
					assignDivSecService.saveRecord(assignDivSecDTO, username);
					setSuccessMessage("Added successsfully.");
					assignedDivSec = assignDivSecService.getAssignedDivSecList(assignDivSecDTO.getDistrictCode());
					clearAddForm();
					RequestContext.getCurrentInstance().update("successMSG");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				}
			}
		}

	}

	public void clearAddForm() {
		if (assignDivSecDTO.isEditRecord()) {
			assignDivSecDTO.setEditRecord(false);
		}
		assignDivSecDTO.setDivSec_description_english(null);
		assignDivSecDTO.setDivSec_description_sinhala(null);
		assignDivSecDTO.setDivSec_description_tamil(null);
		assignDivSecDTO.setDivSecCode(null);
		disableBranchCode = false;
	}

	@SuppressWarnings("deprecation")
	public void deleteAction() {

		clearAddForm();
		if (deleteAssignDivSecDTO != null) {

			int result = assignDivSecService.deleteRecord(deleteAssignDivSecDTO, districtCodeStr);
			if (result < 0) {

				setErrorMessage("Delete unsuccesssful.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				deleteAssignDivSecDTO = null;

				return;
			} else {
				assignedDivSec = assignDivSecService.getAssignedDivSecList(districtCodeStr);

				setSuccessMessage("Deleted successsfully.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				deleteAssignDivSecDTO = null;

				return;
			}
		} else {

			setErrorMessage("Delete unsuccesssful.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			deleteAssignDivSecDTO = null;
			return;
		}
	}

	public void editAction(AssignDivSecDTO item) {
		disableBranchCode = true;
		int districtCodeLength = item.getDistrictCode().length();
		int divSecCodeLength = item.getDivSecCode().length();
		String divSecCodeWithoutDistrictCode = item.getDivSecCode().substring(districtCodeLength + 1, divSecCodeLength);

		assignDivSecDTO.setDivSecCode(divSecCodeWithoutDistrictCode);
		assignDivSecDTO.setDivSec_description_english(item.getDivSec_description_english());
		assignDivSecDTO.setDivSec_description_sinhala(item.getDivSec_description_sinhala());
		assignDivSecDTO.setDivSec_description_tamil(item.getDivSec_description_tamil());
		assignDivSecDTO.setEditRecord(true);
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public AssignDivSecDTO getAssignDivSecDTO() {
		return assignDivSecDTO;
	}

	public void setAssignDivSecDTO(AssignDivSecDTO assignDivSecDTO) {
		this.assignDivSecDTO = assignDivSecDTO;
	}

	public AssignDivSecDTO getDeleteAssignDivSecDTO() {
		return deleteAssignDivSecDTO;
	}

	public void setDeleteAssignDivSecDTO(AssignDivSecDTO deleteAssignDivSecDTO) {
		this.deleteAssignDivSecDTO = deleteAssignDivSecDTO;
	}

	public AssignDivSecService getAssignDivSecService() {
		return assignDivSecService;
	}

	public void setAssignDivSecService(AssignDivSecService assignDivSecService) {
		this.assignDivSecService = assignDivSecService;
	}

	public List<AssignDivSecDTO> getGetDistrictList() {
		return getDistrictList;
	}

	public void setGetDistrictList(List<AssignDivSecDTO> getDistrictList) {
		this.getDistrictList = getDistrictList;
	}

	public List<AssignDivSecDTO> getAssignedDivSec() {
		return assignedDivSec;
	}

	public void setAssignedDivSec(List<AssignDivSecDTO> assignedDivSec) {
		this.assignedDivSec = assignedDivSec;
	}

	public Boolean getSearchedRecord() {
		return searchedRecord;
	}

	public void setSearchedRecord(Boolean searchedRecord) {
		this.searchedRecord = searchedRecord;
	}

	public Boolean getDisableBranchCode() {
		return disableBranchCode;
	}

	public void setDisableBranchCode(Boolean disableBranchCode) {
		this.disableBranchCode = disableBranchCode;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDistrictCodeStr() {
		return districtCodeStr;
	}

	public void setDistrictCodeStr(String districtCodeStr) {
		this.districtCodeStr = districtCodeStr;
	}

}
