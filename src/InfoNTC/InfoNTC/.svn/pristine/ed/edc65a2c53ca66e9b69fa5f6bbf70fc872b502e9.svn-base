package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.ManageInvestigationDTO;
import lk.informatics.ntc.model.service.ApproveActionChargesService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "approveActionChargesBean")
@ViewScoped
public class ApproveActionChargesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private ApproveActionChargesService approveActionChargesService;

	private ManageInvestigationDTO actionChargesDTO;
	private ManageInvestigationDTO selectedActionCharge;
	private ManageInvestigationDTO chargeFinalization;

	private List<DropDownDTO> actionList;
	private List<DropDownDTO> attemptList;
	private List<DropDownDTO> driverList;
	private List<DropDownDTO> conductorList;

	private List<ManageInvestigationDTO> chargeRefList;
	private List<ManageInvestigationDTO> investigationsList;
	private List<ManageInvestigationDTO> chargesList;
	private DriverConductorRegistrationDTO driverData;
	private DriverConductorRegistrationDTO conductorData;

	private boolean itemSelected;

	private BigDecimal totalChargeAmount;
	private BigDecimal totalDriverPoints;
	private BigDecimal totalConductorPoints;

	private String successMsg;
	private String errorMsg;

	private boolean enableApprove;
	private boolean enableReject;
	private BigDecimal latePaymentFee ;
	@PostConstruct
	public void init() {
		approveActionChargesService = (ApproveActionChargesService) SpringApplicationContex
				.getBean("approveActionChargesService");

		loadData();
	}

	public void loadData() {
		actionChargesDTO = new ManageInvestigationDTO();
		selectedActionCharge = new ManageInvestigationDTO();
		chargeFinalization = new ManageInvestigationDTO();
		driverData = new DriverConductorRegistrationDTO();
		conductorData = new DriverConductorRegistrationDTO();

		investigationsList = new ArrayList<ManageInvestigationDTO>();
		chargeRefList = new ArrayList<ManageInvestigationDTO>();
		chargesList = new ArrayList<ManageInvestigationDTO>();

		actionList = new ArrayList<DropDownDTO>();
		attemptList = new ArrayList<DropDownDTO>();
		driverList = new ArrayList<DropDownDTO>();
		conductorList = new ArrayList<DropDownDTO>();

		itemSelected = false;
		enableApprove = false;
		enableReject = false;

		// Load LOV data
		chargeRefList = approveActionChargesService.getChargeRefNoList();

		investigationsList = approveActionChargesService.searchSavedSuspensionsDetails();
		BigDecimal latePaymentFee = new BigDecimal(0);
	}

	public void searchButtonAction() {
		investigationsList = new ArrayList<ManageInvestigationDTO>();
		chargeFinalization = new ManageInvestigationDTO();
		selectedActionCharge = new ManageInvestigationDTO();
		itemSelected = false;

		if (actionChargesDTO.getStartInvestigationDate() == null
				&& actionChargesDTO.getEndInvestigationDate() != null) {

			return;
		} else if (actionChargesDTO.getStartInvestigationDate() != null
				&& actionChargesDTO.getEndInvestigationDate() == null) {

			return;
		} else if (actionChargesDTO.getStartInvestigationDate() != null
				&& actionChargesDTO.getEndInvestigationDate() != null) {
			investigationsList = approveActionChargesService.searchInvestigationDetailsByDate(
					actionChargesDTO.getStartInvestigationDate(), actionChargesDTO.getEndInvestigationDate());
		} else {
			investigationsList = approveActionChargesService.searchInvestigationDetails(actionChargesDTO);
		}

		if (investigationsList == null || investigationsList.size() == 0) {
			return;
		}

		if (investigationsList.size() == 1) {

			for (ManageInvestigationDTO dto : investigationsList) {
				selectedActionCharge = dto;
			}

			chargeFinalization = selectedActionCharge;

			if (selectedActionCharge.getDriverId() == null) {
				chargeFinalization.setDriverId(selectedActionCharge.getDriverNIC());
			}

			if (selectedActionCharge.getConductorId() == null) {
				chargeFinalization.setConductorId(selectedActionCharge.getConductorNIC());
			}

			itemSelected = true;

			if (selectedActionCharge.getCurrentStatus().equalsIgnoreCase("CS")) {
				enableApprove = false;
				enableReject = true;
			} else if (selectedActionCharge.getCurrentStatus().equalsIgnoreCase("CA")) {
				enableApprove = false;
				enableReject = true;
			} else if (selectedActionCharge.getCurrentStatus().equalsIgnoreCase("CR")) {
				enableApprove = false;
				enableReject = false;
			}

		}
		
		
		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void viewBtnAction() {
		if (chargeFinalization.getChargeRefCode() != null) {
			chargesList = approveActionChargesService.getChargeSheetByChargeRef(chargeFinalization.getChargeRefCode());

			calculateTotalAmount();
			calculatePointsDriver();
			calculatePointsConductor();
			latePaymentFee=approveActionChargesService.getLatePaymentFee(selectedActionCharge.getChargeRefCode());
			RequestContext.getCurrentInstance().update("frmChargeFinalize");
			RequestContext.getCurrentInstance().execute("PF('dlgChargeFinalization').show()");
		} else {
			errorMsg = "Please select a row first.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		}
	}

	public void onRowSelect(SelectEvent event) {

		chargeFinalization = selectedActionCharge;

		itemSelected = true;

		if (selectedActionCharge.getCurrentStatus().equalsIgnoreCase("CS")) {
			enableApprove = true;
			enableReject = false;
		} else if (selectedActionCharge.getCurrentStatus().equalsIgnoreCase("CA")) {
			enableApprove = false;
			enableReject = true;
		} else if (selectedActionCharge.getCurrentStatus().equalsIgnoreCase("CR")) {
			enableApprove = false;
			enableReject = false;
		}

		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void onRowUnselect(UnselectEvent event) {
		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void calculateTotalAmount() {

		totalChargeAmount = new BigDecimal(0);
		totalChargeAmount = approveActionChargesService.getTotalAmount(selectedActionCharge.getChargeRefCode());

//		for (ManageInvestigationDTO dto : chargesList) {
//			if (dto.getAmount() != null)
//				totalChargeAmount = totalChargeAmount.add(dto.getAmount());
//		}

		chargeFinalization.setFinalAmount(totalChargeAmount);
	}

	public void calculatePointsDriver() {

		totalDriverPoints = new BigDecimal(0);

		for (ManageInvestigationDTO dto : chargesList) {
			if (dto.getDriverPoints() != null)
				totalDriverPoints = totalDriverPoints.add(dto.getDriverPoints());
		}

	}

	public void calculatePointsConductor() {

		totalConductorPoints = new BigDecimal(0);

		for (ManageInvestigationDTO dto : chargesList) {
			if (dto.getConductorPoints() != null)
				totalConductorPoints = totalConductorPoints.add(dto.getConductorPoints());
		}

	}

	public void clearButtonAction() {
		loadData();
	}

	public void approveBtnAction() {
		actionChargesDTO.setLoginUser(sessionBackingBean.getLoginUser());
		boolean approve = approveActionChargesService.approveCharges(actionChargesDTO, selectedActionCharge.getChargeRefCode(),
				sessionBackingBean.getLoginUser());

		if (approve) {
			approveActionChargesService.beanLinkMethod(actionChargesDTO, "Action/Charge Approved");
			showMsg("SUCCESS", "Action/Charge Approved");
		}

		loadData();

		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void rejectBtnAction() {
		actionChargesDTO.setLoginUser(sessionBackingBean.getLoginUser());
		boolean reject = approveActionChargesService.rejectCharges(actionChargesDTO, selectedActionCharge.getChargeRefCode(),
				sessionBackingBean.getLoginUser());

		if (reject) {
			approveActionChargesService.beanLinkMethod(actionChargesDTO, "Action/Charge Rejected");
			showMsg("SUCCESS", "Action/Charge Rejected");
		}

		loadData();

		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void cancelBtnAction() {
		loadData();

		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void offenceHistoryBtnAction() {
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

	public ApproveActionChargesService getApproveActionChargesService() {
		return approveActionChargesService;
	}

	public void setApproveActionChargesService(ApproveActionChargesService approveActionChargesService) {
		this.approveActionChargesService = approveActionChargesService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public ManageInvestigationDTO getActionChargesDTO() {
		return actionChargesDTO;
	}

	public void setActionChargesDTO(ManageInvestigationDTO actionChargesDTO) {
		this.actionChargesDTO = actionChargesDTO;
	}

	public ManageInvestigationDTO getSelectedActionCharge() {
		return selectedActionCharge;
	}

	public void setSelectedActionCharge(ManageInvestigationDTO selectedActionCharge) {
		this.selectedActionCharge = selectedActionCharge;
	}

	public ManageInvestigationDTO getChargeFinalization() {
		return chargeFinalization;
	}

	public void setChargeFinalization(ManageInvestigationDTO chargeFinalization) {
		this.chargeFinalization = chargeFinalization;
	}

	public List<DropDownDTO> getActionList() {
		return actionList;
	}

	public void setActionList(List<DropDownDTO> actionList) {
		this.actionList = actionList;
	}

	public List<DropDownDTO> getAttemptList() {
		return attemptList;
	}

	public void setAttemptList(List<DropDownDTO> attemptList) {
		this.attemptList = attemptList;
	}

	public List<DropDownDTO> getDriverList() {
		return driverList;
	}

	public void setDriverList(List<DropDownDTO> driverList) {
		this.driverList = driverList;
	}

	public List<DropDownDTO> getConductorList() {
		return conductorList;
	}

	public void setConductorList(List<DropDownDTO> conductorList) {
		this.conductorList = conductorList;
	}

	public List<ManageInvestigationDTO> getChargeRefList() {
		return chargeRefList;
	}

	public void setChargeRefList(List<ManageInvestigationDTO> chargeRefList) {
		this.chargeRefList = chargeRefList;
	}

	public List<ManageInvestigationDTO> getInvestigationsList() {
		return investigationsList;
	}

	public void setInvestigationsList(List<ManageInvestigationDTO> investigationsList) {
		this.investigationsList = investigationsList;
	}

	public List<ManageInvestigationDTO> getChargesList() {
		return chargesList;
	}

	public void setChargesList(List<ManageInvestigationDTO> chargesList) {
		this.chargesList = chargesList;
	}

	public DriverConductorRegistrationDTO getDriverData() {
		return driverData;
	}

	public void setDriverData(DriverConductorRegistrationDTO driverData) {
		this.driverData = driverData;
	}

	public DriverConductorRegistrationDTO getConductorData() {
		return conductorData;
	}

	public void setConductorData(DriverConductorRegistrationDTO conductorData) {
		this.conductorData = conductorData;
	}

	public boolean isItemSelected() {
		return itemSelected;
	}

	public void setItemSelected(boolean itemSelected) {
		this.itemSelected = itemSelected;
	}

	public BigDecimal getTotalChargeAmount() {
		return totalChargeAmount;
	}

	public void setTotalChargeAmount(BigDecimal totalChargeAmount) {
		this.totalChargeAmount = totalChargeAmount;
	}

	public BigDecimal getTotalDriverPoints() {
		return totalDriverPoints;
	}

	public void setTotalDriverPoints(BigDecimal totalDriverPoints) {
		this.totalDriverPoints = totalDriverPoints;
	}

	public BigDecimal getTotalConductorPoints() {
		return totalConductorPoints;
	}

	public void setTotalConductorPoints(BigDecimal totalConductorPoints) {
		this.totalConductorPoints = totalConductorPoints;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public boolean isEnableApprove() {
		return enableApprove;
	}

	public void setEnableApprove(boolean enableApprove) {
		this.enableApprove = enableApprove;
	}

	public boolean isEnableReject() {
		return enableReject;
	}

	public void setEnableReject(boolean enableReject) {
		this.enableReject = enableReject;
	}

	public BigDecimal getLatePaymentFee() {
		return latePaymentFee;
	}

	public void setLatePaymentFee(BigDecimal latePaymentFee) {
		this.latePaymentFee = latePaymentFee;
	}

}
