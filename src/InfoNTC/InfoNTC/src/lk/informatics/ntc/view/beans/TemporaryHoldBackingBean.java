package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.TemporaryHoldService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "temporaryHoldBackingBean")
@ViewScoped
public class TemporaryHoldBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<VehicleInspectionDTO> permitNoList = new ArrayList<VehicleInspectionDTO>(0);
	private List<VehicleInspectionDTO> applicationNoList = new ArrayList<VehicleInspectionDTO>(0);

	private String selectedPermitNo;
	private String selectedApplicationNo;
	private String selectedApplicationStatus;
	private String errorMessage, successMessage;

	private boolean disableApplicationNoDropdown = true;
	private boolean disableHoldBtn = true;
	private boolean disableRemoveHoldBtn = true;

	private TemporaryHoldService temporaryHoldService;

	@PostConstruct
	public void init() {
		temporaryHoldService = (TemporaryHoldService) SpringApplicationContex.getBean("temporaryHoldService");
		loadInitData();
	}

	private void loadInitData() {
		permitNoList = temporaryHoldService.getPermitNoList();
	}

	public void onPermitNoChange() {
		if (selectedPermitNo != null && !selectedPermitNo.isEmpty() && !selectedPermitNo.trim().equals("")) {
			applicationNoList = temporaryHoldService.getApplicationNoList(selectedPermitNo);
			disableApplicationNoDropdown = false;
		} else {
			disableApplicationNoDropdown = true;
		}
	}

	public void onApplicationNoChange() {
		if (selectedApplicationNo != null && !selectedApplicationNo.isEmpty() && !selectedApplicationNo.trim().equals("")) {
			selectedApplicationStatus = temporaryHoldService.getApplicationStatus(selectedApplicationNo);
			if (selectedApplicationStatus != null
					&& selectedApplicationStatus.equals("H")) {
				disableHoldBtn = true;
				disableRemoveHoldBtn = false;
			} else if (selectedApplicationStatus != null
					&& (selectedApplicationStatus.equals("O")
							|| selectedApplicationStatus.equals("P"))) {
				disableHoldBtn = false;
				disableRemoveHoldBtn = true;
			} else {
				disableHoldBtn = true;
				disableRemoveHoldBtn = true;
			}
		} else {
			disableHoldBtn = true;
			disableRemoveHoldBtn = true;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void temporaryHoldBtnAction() {
		boolean success = temporaryHoldService.temporaryHold(selectedApplicationNo,sessionBackingBean.getLoginUser());
		if (success) {
			disableHoldBtn = true;
			disableRemoveHoldBtn = false;
			successMessage = "Temporary hold added successfully.";
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {
			errorMessage = "Error - Could not add temporary hold to the selected application.";
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removeTemporaryHoldBtnAction() {
		boolean success = temporaryHoldService.removeTemporaryHold(selectedApplicationNo,sessionBackingBean.getLoginUser());
		if (success) {
			disableHoldBtn = false;
			disableRemoveHoldBtn = true;
			successMessage = "Temporary hold removed successfully.";
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {
			errorMessage = "Error - Could not remove temporary hold of the selected application.";
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}
	
	public void clearBtnAction() {
		selectedPermitNo = null;
		selectedApplicationNo = null;
		selectedApplicationStatus = null;

		disableApplicationNoDropdown = true;
		disableHoldBtn = true;
		disableRemoveHoldBtn = true;
		permitNoList = temporaryHoldService.getPermitNoList();
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<VehicleInspectionDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<VehicleInspectionDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public List<VehicleInspectionDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<VehicleInspectionDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public String getSelectedPermitNo() {
		return selectedPermitNo;
	}

	public void setSelectedPermitNo(String selectedPermitNo) {
		this.selectedPermitNo = selectedPermitNo;
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

	public boolean isDisableApplicationNoDropdown() {
		return disableApplicationNoDropdown;
	}

	public void setDisableApplicationNoDropdown(boolean disableApplicationNoDropdown) {
		this.disableApplicationNoDropdown = disableApplicationNoDropdown;
	}

	public TemporaryHoldService getTemporaryHoldService() {
		return temporaryHoldService;
	}

	public void setTemporaryHoldService(TemporaryHoldService temporaryHoldService) {
		this.temporaryHoldService = temporaryHoldService;
	}

	public boolean isDisableHoldBtn() {
		return disableHoldBtn;
	}

	public void setDisableHoldBtn(boolean disableHoldBtn) {
		this.disableHoldBtn = disableHoldBtn;
	}

	public boolean isDisableRemoveHoldBtn() {
		return disableRemoveHoldBtn;
	}

	public void setDisableRemoveHoldBtn(boolean disableRemoveHoldBtn) {
		this.disableRemoveHoldBtn = disableRemoveHoldBtn;
	}

	public String getSelectedApplicationNo() {
		return selectedApplicationNo;
	}

	public void setSelectedApplicationNo(String selectedApplicationNo) {
		this.selectedApplicationNo = selectedApplicationNo;
	}

	public String getSelectedApplicationStatus() {
		return selectedApplicationStatus;
	}

	public void setSelectedApplicationStatus(String selectedApplicationStatus) {
		this.selectedApplicationStatus = selectedApplicationStatus;
	}

}
