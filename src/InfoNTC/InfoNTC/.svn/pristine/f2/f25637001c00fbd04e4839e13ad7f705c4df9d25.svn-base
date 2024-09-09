package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AssignModelDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "assignModel")
@ViewScoped
public class AssignModelsBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private AdminService adminService;
	private List<AssignModelDTO> makeList = new ArrayList<AssignModelDTO>(0);
	private AssignModelDTO assignModelDTO;
	private String makeDeiscription, errorMessage, successMessage;
	public List<AssignModelDTO> modelList;
	private boolean disabled;

	public AssignModelsBackingBean() {
		assignModelDTO = new AssignModelDTO();
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		makeList = adminService.getMakes();
		loadValues();
		disabled = true;
	}

	public void completeMakeDescription() {

		makeDeiscription = adminService.getMakesDescription(assignModelDTO);
		assignModelDTO.setMakeDescription(makeDeiscription);

	}

	@SuppressWarnings("deprecation")
	public void loadValues() {
		modelList = new ArrayList<AssignModelDTO>();
		modelList = adminService.getModelDetails(assignModelDTO);
		RequestContext.getCurrentInstance().update("modelDataTable");
	}

	@SuppressWarnings("deprecation")
	public void searchMake() {

		if ((assignModelDTO.getMakeCode() == null || assignModelDTO.getMakeCode().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please select make code");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {
			disabled = false;
			modelList = new ArrayList<AssignModelDTO>();
			modelList = adminService.getModelDetails(assignModelDTO);

		}

	}

	@SuppressWarnings("deprecation")
	public void addModel() {
		String loginUser = sessionBackingBean.getLoginUser();

		if ((assignModelDTO.getModelCode() != null && !assignModelDTO.getModelCode().trim().equalsIgnoreCase(""))) {

			if ((assignModelDTO.getModelDescription() != null
					&& !assignModelDTO.getModelDescription().trim().equalsIgnoreCase(""))) {

				boolean isModelFound = adminService.checkModel(assignModelDTO);

				if (isModelFound == false) {

					boolean isModelAdded = adminService.insertModel(assignModelDTO, loginUser);

					if (isModelAdded == true) {

						setSuccessMessage("Saved successfully");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						modelList = new ArrayList<AssignModelDTO>();
						modelList = adminService.getModelDetails(assignModelDTO);
						clearTwo();

					} else {
						setErrorMessage("Data saving fail");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Model Code already found");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {

				setErrorMessage("Please enter Model Description");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {

			setErrorMessage("Please enter Model Code");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
	}

	@SuppressWarnings("deprecation")
	public void delete(String modelCode) {

		boolean isDeleted = adminService.deleteModel(modelCode);

		if (isDeleted == true) {
			setSuccessMessage("Deleted successfully");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			modelList = new ArrayList<AssignModelDTO>();
			modelList = adminService.getModelDetails(assignModelDTO);

		} else {
			setErrorMessage("Delete unsuccessfully");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearOne() {
		assignModelDTO.setMakeCode(null);
		assignModelDTO.setMakeDescription(null);
		assignModelDTO.setModelCode(null);
		assignModelDTO.setModelDescription(null);
		disabled = true;
		modelList = new ArrayList<AssignModelDTO>();
		modelList = adminService.getModelDetails(assignModelDTO);
	}

	public void clearTwo() {

		assignModelDTO.setModelCode(null);
		assignModelDTO.setModelDescription(null);
	}

	public void goBack() {
		RequestContext.getCurrentInstance().execute("PF('viewCreateModels').hide()");

		clearOne();
		RequestContext.getCurrentInstance().update("frmcreateModels");
		BacklogPermitBackingBean backingBean = new BacklogPermitBackingBean();
		backingBean.updateModelsMenu();
	}

	public AssignModelDTO getAssignModelDTO() {
		return assignModelDTO;
	}

	public void setAssignModelDTO(AssignModelDTO assignModelDTO) {
		this.assignModelDTO = assignModelDTO;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<AssignModelDTO> getMakeList() {
		return makeList;
	}

	public void setMakeList(List<AssignModelDTO> makeList) {
		this.makeList = makeList;
	}

	public String getMakeDeiscription() {
		return makeDeiscription;
	}

	public void setMakeDeiscription(String makeDeiscription) {
		this.makeDeiscription = makeDeiscription;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<AssignModelDTO> getModelList() {
		return modelList;
	}

	public void setModelList(List<AssignModelDTO> modelList) {
		this.modelList = modelList;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

}
