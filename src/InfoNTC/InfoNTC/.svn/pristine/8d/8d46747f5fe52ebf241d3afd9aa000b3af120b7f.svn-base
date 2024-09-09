package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.ComplaintActionDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DepartmentDTO;
import lk.informatics.ntc.model.dto.ManageInquiryDTO;
import lk.informatics.ntc.model.service.GrievanceManagementDashboardService;
import lk.informatics.ntc.model.service.ManageInquiryService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "grievanceManagementDashboardBackingBean")
@ViewScoped
public class GrievanceManagementDashboardBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private GrievanceManagementDashboardService grievanceManagementDashboardService;

	private List<DepartmentDTO> actionDepartmentList;

	private ComplaintActionDTO selectedComplaintAction;

	private ComplaintActionDTO selectedComplaintActionRow;

	private ComplaintActionDTO selectedComplaintActionValue;

	private List<ComplaintActionDTO> complaintActionList;

	private String actionDepartmentCode;

	private Date startDate;

	private Date endDate;

	private boolean showDataTable;

	private ComplaintRequestDTO selectedComplaintDTO;

	private ManageInquiryService manageInquiryService;

	private String successMsg;

	private String errorMsg;

	private ManageInquiryDTO manageInquiryDTO;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {
		manageInquiryService = (ManageInquiryService) SpringApplicationContex.getBean("manageInquiryService");
		grievanceManagementDashboardService = (GrievanceManagementDashboardService) SpringApplicationContex
				.getBean("grievanceManagementDashboardService");

		loadSearchForm();
	}

	public void loadSearchForm() {

		actionDepartmentList = new ArrayList<DepartmentDTO>();
		actionDepartmentList = grievanceManagementDashboardService.actionDepartmentList();
		actionDepartmentCode = null;
		startDate = null;
		endDate = null;
		complaintActionList = new ArrayList<ComplaintActionDTO>();
		showDataTable = false;
		RequestContext.getCurrentInstance().update("dashboardForm");

	}

	public void searchButtonAction() {
		selectedComplaintAction = new ComplaintActionDTO();
		selectedComplaintActionRow = new ComplaintActionDTO();
		selectedComplaintActionValue = new ComplaintActionDTO();

		if (actionDepartmentCode != null && !actionDepartmentCode.isEmpty() && !actionDepartmentCode.equals("")) {
		} else {
			sessionBackingBean.setMessage("Please select the department");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			return;
		}

		if (startDate != null) {
			if (endDate == null) {
				sessionBackingBean.setMessage("Please select the Action End Date");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			} else {
				if (startDate.after(endDate)) {
					sessionBackingBean.setMessage("Action Start Date cannot be after Action End Date");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
			}
		}

		if (endDate != null) {
			if (startDate == null) {
				sessionBackingBean.setMessage("Please select the Action Start Date");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				return;
			} else {
				if (startDate.after(endDate)) {
					sessionBackingBean.setMessage("Action Start Date cannot be after Action End Date");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					return;
				}
			}
		}

		complaintActionList = grievanceManagementDashboardService.getComplaintActionData(actionDepartmentCode,
				startDate, endDate);
		showDataTable = true;

		RequestContext.getCurrentInstance().update("dashboardForm");

	}

	public void clearButtonAction() {
		loadSearchForm();
	}

	public void onRowClick() {

		selectedComplaintAction = grievanceManagementDashboardService
				.getComplaintActionDataByComplainNo(selectedComplaintActionValue.getComplainNo());

		selectedComplaintActionRow.setActionDate(selectedComplaintAction.getAction());
		selectedComplaintActionRow.setComplainNo(selectedComplaintAction.getComplainNo());
		selectedComplaintActionRow.setDepartment(selectedComplaintAction.getDepartment());
		selectedComplaintActionRow.setDepartmentCode(selectedComplaintAction.getDepartmentCode());
		selectedComplaintActionRow.setStatus(selectedComplaintAction.getStatus());
		selectedComplaintActionRow.setStatusCode(selectedComplaintAction.getStatusCode());
		selectedComplaintActionRow.setAction(selectedComplaintAction.getAction());
		selectedComplaintActionRow.setActionCompletedDate(selectedComplaintAction.getActionCompletedDate());

		RequestContext.getCurrentInstance().update("complainActionDlg");
		RequestContext.getCurrentInstance().execute("PF('complainActionDlg').show()");
	}

	public void viewComplaint() {
		selectedComplaintDTO = new ComplaintRequestDTO();
		selectedComplaintDTO = manageInquiryService
				.getComplaintDetailsByComplainNo(selectedComplaintAction.getComplainNo());

		RequestContext.getCurrentInstance().update("complaintDlg");
		RequestContext.getCurrentInstance().execute("PF('complaintDlg').show()");
	}

	public void saveAction() {
		String loginUser = sessionBackingBean.loginUser;

		if (selectedComplaintAction.getAction() != null && selectedComplaintAction.getAction() != "") {

			grievanceManagementDashboardService.complainActionHistory(selectedComplaintActionRow, loginUser);

			selectedComplaintAction.setStatus("P");
			grievanceManagementDashboardService.updateComplainAction(selectedComplaintAction, loginUser);

			grievanceManagementDashboardService.actionTaken(loginUser, selectedComplaintAction.getComplainNo(),
					selectedComplaintAction.getDepartmentCode(), selectedComplaintAction.getAction());

			selectedComplaintDTO = new ComplaintRequestDTO();
			manageInquiryDTO = new ManageInquiryDTO();

			selectedComplaintDTO = manageInquiryService
					.getComplaintDetailsByComplainNo(selectedComplaintAction.getComplainNo());

			manageInquiryDTO.setVehicleNum(selectedComplaintDTO.getVehicleNo());
			manageInquiryDTO.setComplainNo(selectedComplaintDTO.getComplaintNo());

			manageInquiryService.insertGrievanceTask(manageInquiryDTO, loginUser, "GM104", "C");

			RequestContext.getCurrentInstance().update("complainActionDlg");
			RequestContext.getCurrentInstance().execute("PF('complainActionDlg').hide()");

			searchButtonAction();

			grievanceManagementDashboardService.beanLinkMethod(selectedComplaintAction, loginUser, "Action Saved", "Grievance Management Dashboard");
			showMsg("SUCCESS", "Action Saved Successfully");

		} else {
			showMsg("ERROR", "Action is Required");

			selectedComplaintAction.setAction(selectedComplaintActionRow.getAction());
			selectedComplaintAction.setActionCompletedDate(selectedComplaintActionRow.getActionCompletedDate());

		}
	}

	public void completeAction() {
		String loginUser = sessionBackingBean.loginUser;

		if (selectedComplaintAction.getActionCompletedDate() != null) {
			grievanceManagementDashboardService.complainActionHistory(selectedComplaintActionRow, loginUser);

			selectedComplaintAction.setStatus("C");
			grievanceManagementDashboardService.updateComplainAction(selectedComplaintAction, loginUser);

			grievanceManagementDashboardService.actionTaken(loginUser, selectedComplaintAction.getComplainNo(),
					selectedComplaintAction.getDepartmentCode(), "Closed");

			selectedComplaintDTO = new ComplaintRequestDTO();
			manageInquiryDTO = new ManageInquiryDTO();

			selectedComplaintDTO = manageInquiryService
					.getComplaintDetailsByComplainNo(selectedComplaintAction.getComplainNo());

			manageInquiryDTO.setVehicleNum(selectedComplaintDTO.getVehicleNo());
			manageInquiryDTO.setComplainNo(selectedComplaintDTO.getComplaintNo());

			manageInquiryService.insertGrievanceTask(manageInquiryDTO, loginUser, "GM105", "C");

			RequestContext.getCurrentInstance().update("complainActionDlg");
			RequestContext.getCurrentInstance().execute("PF('complainActionDlg').hide()");

			searchButtonAction();

			grievanceManagementDashboardService.beanLinkMethod(selectedComplaintAction, loginUser, "Action Completed", "Grievance Management Dashboard");
			showMsg("SUCCESS", "Action Completed Successfully");
		} else {
			showMsg("ERROR", "Action Completed Date is Required");

			selectedComplaintAction.setAction(selectedComplaintActionRow.getAction());
			selectedComplaintAction.setActionCompletedDate(selectedComplaintActionRow.getActionCompletedDate());

		}

	}

	public void clearAction() {
		selectedComplaintAction.setAction(selectedComplaintActionRow.getAction());
		selectedComplaintAction.setActionCompletedDate(selectedComplaintActionRow.getActionCompletedDate());

		RequestContext.getCurrentInstance().update("complainActionDlg");
	}

	public void showMsg(String type, String msg) {
		if (type.equalsIgnoreCase("ERROR")) {
			errorMsg = msg;
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		} else {
			successMsg = msg;
			RequestContext.getCurrentInstance().update("frmSuccess");
			RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
		}
	}

	public GrievanceManagementDashboardService getGrievanceManagementDashboardService() {
		return grievanceManagementDashboardService;
	}

	public void setGrievanceManagementDashboardService(
			GrievanceManagementDashboardService grievanceManagementDashboardService) {
		this.grievanceManagementDashboardService = grievanceManagementDashboardService;
	}

	public List<DepartmentDTO> getActionDepartmentList() {
		return actionDepartmentList;
	}

	public void setActionDepartmentList(List<DepartmentDTO> actionDepartmentList) {
		this.actionDepartmentList = actionDepartmentList;
	}

	public ComplaintActionDTO getSelectedComplaintAction() {
		return selectedComplaintAction;
	}

	public void setSelectedComplaintAction(ComplaintActionDTO selectedComplaintAction) {
		this.selectedComplaintAction = selectedComplaintAction;
	}

	public List<ComplaintActionDTO> getComplaintActionList() {
		return complaintActionList;
	}

	public void setComplaintActionList(List<ComplaintActionDTO> complaintActionList) {
		this.complaintActionList = complaintActionList;
	}

	public String getActionDepartmentCode() {
		return actionDepartmentCode;
	}

	public void setActionDepartmentCode(String actionDepartmentCode) {
		this.actionDepartmentCode = actionDepartmentCode;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isShowDataTable() {
		return showDataTable;
	}

	public void setShowDataTable(boolean showDataTable) {
		this.showDataTable = showDataTable;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ComplaintRequestDTO getSelectedComplaintDTO() {
		return selectedComplaintDTO;
	}

	public void setSelectedComplaintDTO(ComplaintRequestDTO selectedComplaintDTO) {
		this.selectedComplaintDTO = selectedComplaintDTO;
	}

	public ManageInquiryService getManageInquiryService() {
		return manageInquiryService;
	}

	public void setManageInquiryService(ManageInquiryService manageInquiryService) {
		this.manageInquiryService = manageInquiryService;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public ManageInquiryDTO getManageInquiryDTO() {
		return manageInquiryDTO;
	}

	public void setManageInquiryDTO(ManageInquiryDTO manageInquiryDTO) {
		this.manageInquiryDTO = manageInquiryDTO;
	}

	public ComplaintActionDTO getSelectedComplaintActionRow() {
		return selectedComplaintActionRow;
	}

	public void setSelectedComplaintActionRow(ComplaintActionDTO selectedComplaintActionRow) {
		this.selectedComplaintActionRow = selectedComplaintActionRow;
	}

	public ComplaintActionDTO getSelectedComplaintActionValue() {
		return selectedComplaintActionValue;
	}

	public void setSelectedComplaintActionValue(ComplaintActionDTO selectedComplaintActionValue) {
		this.selectedComplaintActionValue = selectedComplaintActionValue;
	}

}
