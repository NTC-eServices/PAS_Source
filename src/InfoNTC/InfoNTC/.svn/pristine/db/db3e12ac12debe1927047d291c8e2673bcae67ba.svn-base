package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.MaintainTrainingScheduleDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.service.AccessPermissionService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.model.service.GrievanceManagementService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "grievanceVoucherApprovalBackingBean")
@ViewScoped
public class GrievanceVoucherApprovalBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<String> receiptNoList ;

	private String errorMessage, successMessage, rejectReason, advanceSpecialRemark, voucherSpecialRemark, date1,
			alertMSG, InfoMSG;


	private boolean disableClear, disableSave1, disableSave2;
	private BigDecimal totalfee;
	private CommonService commonService;

	// services

	 
	private GrievanceManagementService grievanceManagementService ; 
	private AccessPermissionService  accessPermissionService; 
	// DTO
	
	private ComplaintRequestDTO selectDTO, approveRejectDTO;
	
	private ComplaintRequestDTO voucherApproveDTO = new ComplaintRequestDTO();
	// List
	private List<String> complaintNoList; 
	private List<String> voucherNoList;
	private String selectedComplaintNo,selectedPermitNo,selectedVehiNo,selectedVoucherNo,selectetdReceiptNo; 
	private boolean disablepermitNo,disablevehicleNo; 
	private List<ComplaintRequestDTO> paymentListForGrid;
	
	
	private boolean disableApprovebtn, disableRejectbtn;
	
	


	public GrievanceVoucherApprovalBackingBean() {
		grievanceManagementService = (GrievanceManagementService) SpringApplicationContex.getBean("grievanceManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		accessPermissionService = (AccessPermissionService) SpringApplicationContex.getBean("accessPermissionService");
		complaintNoList = grievanceManagementService.getComplainNoPendingApprove();
		voucherNoList = grievanceManagementService.getVoucherNoList();
		receiptNoList=grievanceManagementService.getReceiptNoList();
		
		
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date1 = dateFormat.format(date);




		disableApprovebtn = true;
		disableRejectbtn = true;
		loadValues();
	}

	public void changeOnComplaintNo() {
		disablepermitNo = false;
		disablevehicleNo = false;
		voucherApproveDTO=grievanceManagementService.getValuesByComplainNo(selectedComplaintNo);
		selectedPermitNo=voucherApproveDTO.getPermitNo();
		selectedVehiNo=voucherApproveDTO.getVehicleNo();
		voucherNoList=grievanceManagementService.getvoucherNoListByComplaintNo(selectedComplaintNo);
	

	}



	public void loadValues() {
	paymentListForGrid = new ArrayList<ComplaintRequestDTO>();
		paymentListForGrid = grievanceManagementService.getPaymentDetailsOnGrid(selectedComplaintNo);
	}

	public void unselectRow() {
		RequestContext.getCurrentInstance().update("dataTable");
		RequestContext.getCurrentInstance().reset("dataTable");
	}

	public void selectRow() {
		disableApprovebtn = false;
		disableRejectbtn = false;
	}

	public void search() {

		if (selectedComplaintNo != null
				&& !selectedComplaintNo.trim().isEmpty()) {
			if (selectedVoucherNo != null
					&& !selectedVoucherNo.trim().isEmpty()) {
			paymentListForGrid = new ArrayList<ComplaintRequestDTO>();
			paymentListForGrid = grievanceManagementService.getPaymentDetailsOnGrid(selectedComplaintNo);
			

			if (paymentListForGrid.isEmpty()) {
				setAlertMSG("No data for searched values.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
			} else {
				disableApprovebtn = false;
				disableRejectbtn = false;
				disableClear = false;
			}
			}
			else {
				setErrorMessage("Please select a Voucher No.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select a Complaint No..");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void reject() {

		String loginUser = sessionBackingBean.getLoginUser();
	
		if (selectDTO != null) {
			approveRejectDTO = grievanceManagementService.getVoucherSatus(selectDTO.getVoucherNo());
			if (approveRejectDTO.getVoucherApprovedStatus() != null && !approveRejectDTO.getVoucherApprovedStatus().isEmpty()) {


				if (approveRejectDTO.getVoucherApprovedStatus().equalsIgnoreCase("R")) {

					setErrorMessage("Selected data is already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else if (approveRejectDTO.getVoucherApprovedStatus().equalsIgnoreCase("A")) {
					setErrorMessage("Selected data is already approved.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} 

			} else {

				if (rejectReason == null || rejectReason.isEmpty()) {
					setErrorMessage("Please enter reject reason.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					grievanceManagementService.updateApproveRejectVoucher(selectDTO.getVoucherNo(), "R",
							rejectReason, loginUser);
					loadValues();
					setInfoMSG("Success");
					setSuccessMessage("Successfully rejected.");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				}
			

			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public void approvePayment() {

		String loginUser = sessionBackingBean.getLoginUser();
		
		if (selectDTO != null) {
			approveRejectDTO = grievanceManagementService.getVoucherSatus(selectDTO.getVoucherNo());
			if (approveRejectDTO.getVoucherApprovedStatus() != null && !approveRejectDTO.getVoucherApprovedStatus().isEmpty()) {

				if (approveRejectDTO.getVoucherApprovedStatus().equalsIgnoreCase("R")) {

					setErrorMessage("Selected data is already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else if (approveRejectDTO.getVoucherApprovedStatus().equalsIgnoreCase("A")) {
					setErrorMessage("Selected data is already approved.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				grievanceManagementService.updateApproveRejectVoucher(selectDTO.getVoucherNo(), "A",
						rejectReason, loginUser);
				//update status type as VA
			
				loadValues();
				setInfoMSG("Success");
				setSuccessMessage("Successfully approved.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");


			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public void clearOne() {
		selectedPermitNo=null;
		selectedVehiNo=null;
		selectedVoucherNo=null;
		selectedComplaintNo=null;
		selectetdReceiptNo=null;

		disableApprovebtn = true;
		disableRejectbtn = true;
		setRejectReason(null);
		selectDTO = new  ComplaintRequestDTO();
		complaintNoList = grievanceManagementService.getComplainNoPendingApprove();
		voucherNoList = grievanceManagementService.getVoucherNoList();
		receiptNoList=grievanceManagementService.getReceiptNoList();
		loadValues();
	}

	public void clearTwo() {

	
		disableApprovebtn = true;
		disableRejectbtn = true;
		setRejectReason(null);
		selectDTO = new  ComplaintRequestDTO();
		complaintNoList = grievanceManagementService.getComplainNoPendingApprove();
		voucherNoList = grievanceManagementService.getVoucherNoList();
		receiptNoList=grievanceManagementService.getReceiptNoList();
		loadValues();
		

	}




	public String getRejectReason() {
		return rejectReason;
	}

	public String getAdvanceSpecialRemark() {
		return advanceSpecialRemark;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public void setAdvanceSpecialRemark(String advanceSpecialRemark) {
		this.advanceSpecialRemark = advanceSpecialRemark;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}



	

	public boolean isDisableClear() {
		return disableClear;
	}

	public void setDisableClear(boolean disableClear) {
		this.disableClear = disableClear;
	}

	public boolean isDisableSave2() {
		return disableSave2;
	}

	public void setDisableSave2(boolean disableSave2) {
		this.disableSave2 = disableSave2;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public String getInfoMSG() {
		return InfoMSG;
	}

	public void setInfoMSG(String infoMSG) {
		InfoMSG = infoMSG;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
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





	public List<String> getReceiptNoList() {
		return receiptNoList;
	}

	public void setReceiptNoList(List<String> receiptNoList) {
		this.receiptNoList = receiptNoList;
	}

	public String getVoucherSpecialRemark() {
		return voucherSpecialRemark;
	}

	public void setVoucherSpecialRemark(String voucherSpecialRemark) {
		this.voucherSpecialRemark = voucherSpecialRemark;
	}

	public BigDecimal getTotalfee() {
		return totalfee;
	}

	public boolean isDisableSave1() {
		return disableSave1;
	}

	public void setDisableSave1(boolean disableSave1) {
		this.disableSave1 = disableSave1;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public void setTotalfee(BigDecimal totalfee) {
		this.totalfee = totalfee;
	}







	public boolean isDisableApprovebtn() {
		return disableApprovebtn;
	}

	public void setDisableApprovebtn(boolean disableApprovebtn) {
		this.disableApprovebtn = disableApprovebtn;
	}

	public boolean isDisableRejectbtn() {
		return disableRejectbtn;
	}

	public void setDisableRejectbtn(boolean disableRejectbtn) {
		this.disableRejectbtn = disableRejectbtn;
	}

/** getters setters **/

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public GrievanceManagementService getGrievanceManagementService() {
		return grievanceManagementService;
	}

	public void setGrievanceManagementService(GrievanceManagementService grievanceManagementService) {
		this.grievanceManagementService = grievanceManagementService;
	}

	public List<String> getComplaintNoList() {
		return complaintNoList;
	}

	public void setComplaintNoList(List<String> complaintNoList) {
		this.complaintNoList = complaintNoList;
	}

	public AccessPermissionService getAccessPermissionService() {
		return accessPermissionService;
	}

	public void setAccessPermissionService(AccessPermissionService accessPermissionService) {
		this.accessPermissionService = accessPermissionService;
	}

	public String getSelectedComplaintNo() {
		return selectedComplaintNo;
	}

	public void setSelectedComplaintNo(String selectedComplaintNo) {
		this.selectedComplaintNo = selectedComplaintNo;
	}

	public String getSelectedPermitNo() {
		return selectedPermitNo;
	}

	public void setSelectedPermitNo(String selectedPermitNo) {
		this.selectedPermitNo = selectedPermitNo;
	}

	public boolean isDisablepermitNo() {
		return disablepermitNo;
	}

	public void setDisablepermitNo(boolean disablepermitNo) {
		this.disablepermitNo = disablepermitNo;
	}

	public String getSelectedVehiNo() {
		return selectedVehiNo;
	}

	public void setSelectedVehiNo(String selectedVehiNo) {
		this.selectedVehiNo = selectedVehiNo;
	}

	public boolean isDisablevehicleNo() {
		return disablevehicleNo;
	}

	public void setDisablevehicleNo(boolean disablevehicleNo) {
		this.disablevehicleNo = disablevehicleNo;
	}

	public List<String> getVoucherNoList() {
		return voucherNoList;
	}

	public void setVoucherNoList(List<String> voucherNoList) {
		this.voucherNoList = voucherNoList;
	}

	public List<ComplaintRequestDTO> getPaymentListForGrid() {
		return paymentListForGrid;
	}

	public void setPaymentListForGrid(List<ComplaintRequestDTO> paymentListForGrid) {
		this.paymentListForGrid = paymentListForGrid;
	}

	public String getSelectedVoucherNo() {
		return selectedVoucherNo;
	}

	public void setSelectedVoucherNo(String selectedVoucherNo) {
		this.selectedVoucherNo = selectedVoucherNo;
	}

	public ComplaintRequestDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(ComplaintRequestDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public String getSelectetdReceiptNo() {
		return selectetdReceiptNo;
	}

	public void setSelectetdReceiptNo(String selectetdReceiptNo) {
		this.selectetdReceiptNo = selectetdReceiptNo;
	}

	
	
	/** finished getters setters **/
}
