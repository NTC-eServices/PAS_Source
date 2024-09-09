package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.ManageInquiryDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.GrievanceManagementService;
import lk.informatics.ntc.model.service.ManageInquiryService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "grantPermissionToProceedActiveApplicationBackingBean")
@ViewScoped
public class GrantPermissionToProceedActiveApplicationBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean complaintSelected = false;
	private String complaintNo;
	private String vehicleNo;
	private String permitNo;
	private String specialRemark;
	private List<String> complaintNoList;
	private List<String> vehicleNoList;
	private List<String> permitNoList;

	private ComplaintRequestDTO selectedComplaintDTO;

	private ComplaintRequestDTO selectedComplaint;

	private List<ComplaintRequestDTO> complaintDetailDTOList = new ArrayList<ComplaintRequestDTO>();

	private GrievanceManagementService grievanceManagementService;

	private ManageInquiryService manageInquiryService;

	private CommonService commonService;

	private String successMsg;

	private String errorMsg;

	private boolean complainSelected;

	private ManageInquiryDTO manageInquiryDTO;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {
		manageInquiryService = (ManageInquiryService) SpringApplicationContex.getBean("manageInquiryService");
		grievanceManagementService = (GrievanceManagementService) SpringApplicationContex
				.getBean("grievanceManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		resetSearchFilters();

	}

	public void resetSearchFilters() {
		complaintNoList = grievanceManagementService.getAllComplainNo();
		vehicleNoList = commonService.getAllVehicle();
		permitNoList = commonService.getAllPermit();
		complaintNo = "";
		vehicleNo = "";
		permitNo = "";
		specialRemark = "";
		complainSelected = false;
		searchButtonAction();
	}

	public List<String> completeComplaintNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < complaintNoList.size(); i++) {
			String cm = complaintNoList.get(i);
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public List<String> completeVehicleNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < vehicleNoList.size(); i++) {
			String cm = vehicleNoList.get(i);
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public List<String> completePermitNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < permitNoList.size(); i++) {
			String cm = permitNoList.get(i);
			if (cm != null && cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public void searchButtonAction() {

		selectedComplaint = new ComplaintRequestDTO();

		if (complaintNo != null && !complaintNo.trim().isEmpty()) {
			complaintNo = complaintNo.toUpperCase();
			complaintDetailDTOList = grievanceManagementService
					.getComplaintDetailsByStatusAndGrantPermission(complaintNo, null, null);
		} else if (permitNo != null && !permitNo.trim().isEmpty()) {
			permitNo = permitNo.toUpperCase();
			complaintDetailDTOList = grievanceManagementService.getComplaintDetailsByStatusAndGrantPermission(null,
					null, permitNo);
		} else if (vehicleNo != null && !vehicleNo.trim().isEmpty()) {
			vehicleNo = vehicleNo.toUpperCase();
			complaintDetailDTOList = grievanceManagementService.getComplaintDetailsByStatusAndGrantPermission(null,
					vehicleNo, null);
		} else {
			complaintDetailDTOList = grievanceManagementService.getComplaintDetailsByStatusAndGrantPermission(null,
					null, null);

		}

		if (complaintDetailDTOList.isEmpty()) {
			showMsg("ERROR", "No Active Complains found");
			return;
		}

	}

	public void onRowClick() {
		complaintNo = selectedComplaint.getComplaintNo();
		vehicleNo = selectedComplaint.getVehicleNo();
		permitNo = selectedComplaint.getPermitNo();
		specialRemark = "";
		complainSelected = true;

		RequestContext.getCurrentInstance().update("permissionForm");

	}

	public void proceedButton() {

		if (selectedComplaint != null) {

			if (specialRemark != null && !specialRemark.isEmpty() && !specialRemark.equals("")) {

				String loginUser = sessionBackingBean.loginUser;
				complaintNo = selectedComplaint.getComplaintNo();
				vehicleNo = selectedComplaint.getVehicleNo();
				permitNo = selectedComplaint.getPermitNo();

				manageInquiryDTO = new ManageInquiryDTO();

				manageInquiryDTO.setVehicleNum(selectedComplaint.getVehicleNo());
				manageInquiryDTO.setComplainNo(selectedComplaint.getComplaintNo());

				grievanceManagementService.complainRequestHistory(complaintNo);

				grievanceManagementService.updateGrantPermission(specialRemark, "TA", loginUser, vehicleNo);

				grievanceManagementService.grantPermission(complaintNo, vehicleNo, permitNo, loginUser, specialRemark);

				manageInquiryService.insertGrievanceTask(manageInquiryDTO, loginUser, "GM107", "C");

				grievanceManagementService.beanLinkMethod(selectedComplaintDTO, loginUser,"Save complaint Action", "Grant Permission To Proceed Active Application");
				showMsg("SUCCESS", "Permission granted to proceed");

				resetSearchFilters();
			} else {
				showMsg("ERROR", "Special Remark is required to proceed");
				return;
			}

		} else {
			showMsg("ERROR", "Please select a complain to proceed");
			return;
		}

	}

	public void clearButton() {
		selectedComplaint = new ComplaintRequestDTO();
		specialRemark = "";
		complainSelected = false;

	}

	public void viewComplaint() {
		selectedComplaintDTO = new ComplaintRequestDTO();
		selectedComplaintDTO = manageInquiryService.getComplaintDetailsByComplainNo(selectedComplaint.getComplaintNo());

		RequestContext.getCurrentInstance().update("complaintDlg");
		RequestContext.getCurrentInstance().execute("PF('complaintDlg').show()");
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

	public boolean isComplaintSelected() {
		return complaintSelected;
	}

	public void setComplaintSelected(boolean complaintSelected) {
		this.complaintSelected = complaintSelected;
	}

	public String getComplaintNo() {
		return complaintNo;
	}

	public void setComplaintNo(String complaintNo) {
		this.complaintNo = complaintNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public List<String> getComplaintNoList() {
		return complaintNoList;
	}

	public void setComplaintNoList(List<String> complaintNoList) {
		this.complaintNoList = complaintNoList;
	}

	public List<String> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<String> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public ComplaintRequestDTO getSelectedComplaintDTO() {
		return selectedComplaintDTO;
	}

	public void setSelectedComplaintDTO(ComplaintRequestDTO selectedComplaintDTO) {
		this.selectedComplaintDTO = selectedComplaintDTO;
	}

	public List<ComplaintRequestDTO> getComplaintDetailDTOList() {
		return complaintDetailDTOList;
	}

	public void setComplaintDetailDTOList(List<ComplaintRequestDTO> complaintDetailDTOList) {
		this.complaintDetailDTOList = complaintDetailDTOList;
	}

	public GrievanceManagementService getGrievanceManagementService() {
		return grievanceManagementService;
	}

	public void setGrievanceManagementService(GrievanceManagementService grievanceManagementService) {
		this.grievanceManagementService = grievanceManagementService;
	}

	public ManageInquiryService getManageInquiryService() {
		return manageInquiryService;
	}

	public void setManageInquiryService(ManageInquiryService manageInquiryService) {
		this.manageInquiryService = manageInquiryService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
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

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ComplaintRequestDTO getSelectedComplaint() {
		return selectedComplaint;
	}

	public void setSelectedComplaint(ComplaintRequestDTO selectedComplaint) {
		this.selectedComplaint = selectedComplaint;
	}

	public String getSpecialRemark() {
		return specialRemark;
	}

	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}

	public boolean isComplainSelected() {
		return complainSelected;
	}

	public void setComplainSelected(boolean complainSelected) {
		this.complainSelected = complainSelected;
	}

}
