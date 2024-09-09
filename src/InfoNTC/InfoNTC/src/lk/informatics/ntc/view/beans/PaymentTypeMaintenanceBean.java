package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.PaymentTypeMaintenanceDTO;
import lk.informatics.ntc.model.service.PaymentTypeMaintenanceService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "paymentTypeMaintenanceBean")
@ViewScoped
public class PaymentTypeMaintenanceBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private String selectedTranstractionType;
	private String selectedChargeType;
	private String selectedAccountNo;
	private Double displayAmount;
	private String selectedStatus;
	private String inputAccountNo;
	private String sucessMsg;
	private String errorMsg;
	private String previousAccountNo;
	private Double previousAmount;
	private String previousSelectedStatus;

	private boolean readOnlyAmountField = true;
	private boolean disableTranstractionTypeField = false;
	private boolean disableChargeTypeField = false;
	private boolean showInputAccountNoField = false;
	private boolean showSearchAccountNoMenu = true;
	private boolean showSearchBtn = true;
	private boolean showSaveBtn = false;
	private boolean pressedEditBtn = false;

	private PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
	private PaymentTypeMaintenanceDTO selectedTranstractionTypeDes;
	private PaymentTypeMaintenanceDTO selectedPaymentTypeDes;
	private PaymentTypeMaintenanceDTO selectedAmount;
	private PaymentTypeMaintenanceDTO selectedStatusDes;
	private PaymentTypeMaintenanceDTO selectedEditRow;
	private PaymentTypeMaintenanceDTO selectedDeleteRow;

	PaymentTypeMaintenanceService paymentTypeMaintenanceService;

	public List<PaymentTypeMaintenanceDTO> transtractionTypeList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> paymentTypeList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> accountNoList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> statusList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> dataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);

	public PaymentTypeMaintenanceBean() {
		paymentTypeMaintenanceService = (PaymentTypeMaintenanceService) SpringApplicationContex
				.getBean("paymentTypeMaintenanceService");
		transtractionTypeList = paymentTypeMaintenanceService.getAllTranstractionTypeList();
		paymentTypeList = paymentTypeMaintenanceService.getAllPaymentTypeList();
		statusList = paymentTypeMaintenanceService.getAllStatusList();
	}

	public void onTranstractionTypeChange() {

		paymentTypeMaintenanceDTO.setTranstractionTypeCode(selectedTranstractionType);
		selectedTranstractionTypeDes = paymentTypeMaintenanceService
				.getTranstractionDescription(selectedTranstractionType);
		paymentTypeMaintenanceDTO
				.setTranstractionDescription(selectedTranstractionTypeDes.getTranstractionDescription());

	}

	public void onChargeTypeChange() {

		paymentTypeMaintenanceDTO.setPaymentTypeCode(selectedChargeType);
		selectedPaymentTypeDes = paymentTypeMaintenanceService.getPaymentTypeDescription(selectedChargeType);
		paymentTypeMaintenanceDTO.setPaymentTypeDescription(selectedPaymentTypeDes.getPaymentTypeDescription());

		if (selectedChargeType != null && !selectedChargeType.equals("")) {
			accountNoList = paymentTypeMaintenanceService
					.getAllAccountNoList(paymentTypeMaintenanceDTO.getTranstractionTypeCode(), selectedChargeType);
		} else if (selectedTranstractionType.equals("") || selectedChargeType.equals("")) {
			accountNoList.clear();
			setDisplayAmount(null);
			RequestContext.getCurrentInstance().update("amountId");
		}

	}

	public void onACcountNoChange() {

		paymentTypeMaintenanceDTO.setAccountNo(selectedAccountNo);
		if (selectedAccountNo != null && !selectedAccountNo.equals("")) {
			selectedAmount = paymentTypeMaintenanceService.getCurrentAmount(
					paymentTypeMaintenanceDTO.getTranstractionTypeCode(),
					paymentTypeMaintenanceDTO.getPaymentTypeCode(), paymentTypeMaintenanceDTO.getAccountNo());

			paymentTypeMaintenanceDTO.setAmount(selectedAmount.getAmount());
			setDisplayAmount(paymentTypeMaintenanceDTO.getAmount());
		} else {
			setDisplayAmount(null);
		}
	}

	public void onstatusChange() {

		paymentTypeMaintenanceDTO.setStatusCode(selectedStatus);
		selectedStatusDes = paymentTypeMaintenanceService.getStatusDescription(selectedStatus);
		paymentTypeMaintenanceDTO.setStatusDescription(selectedStatusDes.getStatusDescription());

	}

	public void searchDetails() {

		if (!selectedTranstractionType.isEmpty() && selectedTranstractionType != null
				&& !selectedTranstractionType.equals("") && selectedChargeType.equals("")
				&& selectedAccountNo.equals("") && displayAmount == null && selectedStatus.equals("")) {

			dataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
			dataList = paymentTypeMaintenanceService
					.getDisplaySearchDetailsWithTranstractionType(selectedTranstractionType);

		} else if (!selectedTranstractionType.isEmpty() && selectedTranstractionType != null
				&& !selectedTranstractionType.equals("") && !selectedChargeType.isEmpty() && selectedChargeType != null
				&& !selectedChargeType.equals("") && selectedAccountNo.equals("") && displayAmount == null
				&& selectedStatus.equals("")) {

			dataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
			dataList = paymentTypeMaintenanceService.getDisplayRecordsTransAndCharge(selectedTranstractionType,
					selectedChargeType);

		} else if (!selectedTranstractionType.isEmpty() && selectedTranstractionType != null
				&& !selectedTranstractionType.equals("") && !selectedChargeType.isEmpty() && selectedChargeType != null
				&& !selectedChargeType.equals("") && !selectedAccountNo.isEmpty() && selectedAccountNo != null
				&& !selectedAccountNo.equals("") && displayAmount == null && selectedStatus.equals("")) {

			dataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
			errorMsg = "Amount should be updated.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (selectedTranstractionType.equals("") && selectedChargeType.equals("") && selectedAccountNo.equals("")
				&& displayAmount == null && !selectedStatus.isEmpty() && selectedStatus != null
				&& !selectedStatus.equals("")) {

			dataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
			dataList = paymentTypeMaintenanceService.getSearchRecordsWithStatus(selectedStatus);

		} else if (selectedTranstractionType != null && !selectedTranstractionType.equals("")
				&& selectedChargeType != null && !selectedChargeType.equals("") && selectedAccountNo != null
				&& !selectedAccountNo.equals("") && displayAmount != null && !displayAmount.equals("")
				&& ((selectedStatus != null && !selectedStatus.equals("") || selectedStatus.equals("")))) {

			dataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
			dataList = paymentTypeMaintenanceService.getSearchRecordsWithAll(selectedTranstractionType,
					selectedChargeType, selectedAccountNo, displayAmount, selectedStatus);

		} else if (selectedTranstractionType.equals("") && selectedChargeType.equals("") && selectedAccountNo.equals("")
				&& displayAmount == null && selectedStatus.equals("")) {

			dataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
			errorMsg = "Field/s should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (selectedTranstractionType.equals("") && !selectedChargeType.isEmpty() && selectedChargeType != null
				&& !selectedChargeType.equals("") && selectedAccountNo.equals("") && displayAmount == null
				&& selectedStatus.equals("")) {

			dataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
			errorMsg = "You can not searched with only charge type.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (!selectedTranstractionType.isEmpty() && selectedTranstractionType != null
				&& !selectedTranstractionType.equals("") && selectedChargeType.equals("")
				&& selectedAccountNo.equals("") && displayAmount == null && !selectedStatus.isEmpty()
				&& selectedStatus != null && !selectedStatus.equals("")) {

			dataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
			dataList = paymentTypeMaintenanceService.getSearchRecordsWithTransAndStatus(selectedTranstractionType,
					selectedStatus);

		}
	}

	public void editActButtonAction() {

		boolean resultValue = paymentTypeMaintenanceService.checkHaveFunction(selectedEditRow.getPaymentTypeCode(),
				selectedEditRow.getAccountNo());
		if (resultValue == false) {
			setPressedEditBtn(true);
			setShowInputAccountNoField(true);
			setShowSearchAccountNoMenu(false);
			setReadOnlyAmountField(false);
			setShowSearchBtn(false);
			setShowSaveBtn(true);
			setDisableTranstractionTypeField(true);
			setDisableChargeTypeField(true);
			setSelectedTranstractionType(selectedEditRow.getTranstractionTypeCode());
			setSelectedChargeType(selectedEditRow.getPaymentTypeCode());
			setInputAccountNo(selectedEditRow.getAccountNo());
			setDisplayAmount(selectedEditRow.getAmount());
			setSelectedStatus(selectedEditRow.getStatusCode());
			previousAccountNo = selectedEditRow.getAccountNo();
			previousAmount = selectedEditRow.getAmount();
			previousSelectedStatus = selectedEditRow.getStatusCode();
			paymentTypeMaintenanceDTO.setTectCode(selectedEditRow.getTectCode());
			paymentTypeMaintenanceDTO.setTranstractionTypeCode(selectedEditRow.getTranstractionTypeCode());
			paymentTypeMaintenanceDTO.setTranstractionDescription(selectedEditRow.getTranstractionDescription());
			paymentTypeMaintenanceDTO.setPaymentTypeCode(selectedEditRow.getPaymentTypeCode());
			paymentTypeMaintenanceDTO.setPaymentTypeDescription(selectedEditRow.getPaymentTypeDescription());
			paymentTypeMaintenanceDTO.setAccountNo(inputAccountNo);
			paymentTypeMaintenanceDTO.setAmount(displayAmount);
			paymentTypeMaintenanceDTO.setStatusCode(selectedStatus);
			selectedStatusDes = paymentTypeMaintenanceService.getStatusDescription(selectedStatus);
			paymentTypeMaintenanceDTO.setStatusDescription(selectedStatusDes.getStatusDescription());

		} else {

			errorMsg = "Record is already assigned to create a payment voucher.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void updateRecord() {

		if (!(selectedTranstractionType.isEmpty() || selectedTranstractionType == null
				|| selectedTranstractionType.equals("") || selectedChargeType.isEmpty() || selectedChargeType == null
				|| selectedChargeType.equals("") || inputAccountNo.isEmpty() || inputAccountNo == null
				|| inputAccountNo.equals("") || displayAmount == null || displayAmount.equals("")
				|| selectedStatus.isEmpty() || selectedStatus == null || selectedStatus.equals(""))) {
			paymentTypeMaintenanceDTO.setTectCode(paymentTypeMaintenanceDTO.getTectCode());
			paymentTypeMaintenanceDTO.setTranstractionTypeCode(paymentTypeMaintenanceDTO.getTranstractionTypeCode());
			paymentTypeMaintenanceDTO
					.setTranstractionDescription(paymentTypeMaintenanceDTO.getTranstractionDescription());
			paymentTypeMaintenanceDTO.setPaymentTypeCode(paymentTypeMaintenanceDTO.getPaymentTypeCode());
			paymentTypeMaintenanceDTO.setPaymentTypeDescription(paymentTypeMaintenanceDTO.getPaymentTypeDescription());
			paymentTypeMaintenanceDTO.setAccountNo(inputAccountNo);
			paymentTypeMaintenanceDTO.setAmount(displayAmount);
			paymentTypeMaintenanceDTO.setStatusCode(selectedStatus);
			selectedStatusDes = paymentTypeMaintenanceService.getStatusDescription(selectedStatus);
			paymentTypeMaintenanceDTO.setStatusDescription(selectedStatusDes.getStatusDescription());

			String modifyBy = sessionBackingBean.loginUser;
			paymentTypeMaintenanceDTO.setModifyBy(modifyBy);

			if (!(paymentTypeMaintenanceDTO.getAccountNo().equals(previousAccountNo)
					&& paymentTypeMaintenanceDTO.getAmount().equals(previousAmount)
					&& paymentTypeMaintenanceDTO.getStatusCode().equals(previousSelectedStatus))) {

				int result = paymentTypeMaintenanceService.updateTable(paymentTypeMaintenanceDTO);
				if (result == 0) {

					RequestContext.getCurrentInstance().update("frmsuccessSve");
					setSucessMsg("Successfully Saved.");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					selectedEditRow.setTectCode(paymentTypeMaintenanceDTO.getTectCode());
					selectedEditRow.setTranstractionTypeCode(paymentTypeMaintenanceDTO.getTranstractionTypeCode());
					selectedEditRow
							.setTranstractionDescription(paymentTypeMaintenanceDTO.getTranstractionDescription());
					selectedEditRow.setPaymentTypeCode(paymentTypeMaintenanceDTO.getPaymentTypeCode());
					selectedEditRow.setPaymentTypeDescription(paymentTypeMaintenanceDTO.getPaymentTypeDescription());
					selectedEditRow.setAccountNo(paymentTypeMaintenanceDTO.getAccountNo());
					selectedEditRow.setAmount(paymentTypeMaintenanceDTO.getAmount());
					selectedEditRow.setStatusCode(paymentTypeMaintenanceDTO.getStatusCode());
					selectedEditRow.setStatusDescription(paymentTypeMaintenanceDTO.getStatusDescription());
					setSelectedTranstractionType(null);
					setSelectedChargeType(null);
					setInputAccountNo(null);
					setDisplayAmount(null);
					setSelectedStatus(null);
					setReadOnlyAmountField(true);
					setShowInputAccountNoField(false);
					setShowSearchAccountNoMenu(true);
					setShowSearchBtn(true);
					setShowSaveBtn(false);
					setDisableTranstractionTypeField(false);
					setDisableChargeTypeField(false);
				}
			} else {

				errorMsg = "Please update record before save.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else if (selectedTranstractionType.isEmpty() || selectedTranstractionType == null
				|| selectedTranstractionType.equals("") || selectedChargeType.isEmpty() || selectedChargeType == null
				|| selectedChargeType.equals("") || inputAccountNo.isEmpty() || inputAccountNo == null
				|| inputAccountNo.equals("") || displayAmount == null || displayAmount.equals("")
				|| selectedStatus.isEmpty() || selectedStatus == null || selectedStatus.equals("")) {
			if (selectedTranstractionType.isEmpty() || selectedTranstractionType == null) {

				errorMsg = "Transtraction Type should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (selectedChargeType.isEmpty() || selectedChargeType == null) {

				errorMsg = "Charge Type should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (inputAccountNo.isEmpty() || inputAccountNo == null) {

				errorMsg = "Account No should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (displayAmount == null) {

				errorMsg = "Amount should be entered.";
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

		boolean resultValue = paymentTypeMaintenanceService.checkHaveFunction(selectedDeleteRow.getPaymentTypeCode(),
				selectedDeleteRow.getAccountNo());
		if (resultValue == false) {
			RequestContext.getCurrentInstance().update("frmAddVIAPM");
			paymentTypeMaintenanceDTO.setTectCode(selectedDeleteRow.getTectCode());
			RequestContext.getCurrentInstance().execute("PF('deleteconfirmationsequence').show()");

		} else {

			errorMsg = "Record is already assigned to create a payment voucher.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		setSelectedTranstractionType(null);
		setSelectedChargeType(null);
		setSelectedAccountNo(null);
		setDisplayAmount(null);
		setSelectedStatus(null);
		setInputAccountNo(null);
		setReadOnlyAmountField(true);
		setShowInputAccountNoField(false);
		setShowSearchAccountNoMenu(true);
		setShowSearchBtn(true);
		setShowSaveBtn(false);
		setDisableChargeTypeField(false);
		setDisableTranstractionTypeField(false);
	}

	public void deleteRole() {

		RequestContext.getCurrentInstance().update("@form");
		paymentTypeMaintenanceDTO.setTectCode(selectedDeleteRow.getTectCode());
		int result = paymentTypeMaintenanceService.deleteRecord(paymentTypeMaintenanceDTO.getTectCode());
		if (result == 0) {

			RequestContext.getCurrentInstance().update("frmsuccessSve");
			sucessMsg = "Successfully Deleted.";
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			dataList.remove(selectedDeleteRow);
			RequestContext.getCurrentInstance().update("frmPayemntTypeTable");

		} else {
			errorMsg = "Record is already assigned to create a payment voucher.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clearFields() {

		setSelectedTranstractionType(null);
		setSelectedChargeType(null);
		setSelectedAccountNo(null);
		setDisplayAmount(null);
		setSelectedStatus(null);
		setReadOnlyAmountField(true);
		setShowInputAccountNoField(false);
		setShowSearchAccountNoMenu(true);
		setShowSearchBtn(true);
		setShowSaveBtn(false);
		if (pressedEditBtn == true) {
			setInputAccountNo(null);
			dataList.clear();
			setDisableTranstractionTypeField(false);
			setDisableChargeTypeField(false);
		} else {
			dataList.clear();
		}

	}

	public List<PaymentTypeMaintenanceDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PaymentTypeMaintenanceDTO> dataList) {
		this.dataList = dataList;
	}

	public List<PaymentTypeMaintenanceDTO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<PaymentTypeMaintenanceDTO> statusList) {
		this.statusList = statusList;
	}

	public PaymentTypeMaintenanceDTO getSelectedStatusDes() {
		return selectedStatusDes;
	}

	public void setSelectedStatusDes(PaymentTypeMaintenanceDTO selectedStatusDes) {
		this.selectedStatusDes = selectedStatusDes;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public PaymentTypeMaintenanceDTO getSelectedAmount() {
		return selectedAmount;
	}

	public void setSelectedAmount(PaymentTypeMaintenanceDTO selectedAmount) {
		this.selectedAmount = selectedAmount;
	}

	public String getSelectedAccountNo() {
		return selectedAccountNo;
	}

	public Double getDisplayAmount() {
		return displayAmount;
	}

	public void setDisplayAmount(Double displayAmount) {
		this.displayAmount = displayAmount;
	}

	public void setSelectedAccountNo(String selectedAccountNo) {
		this.selectedAccountNo = selectedAccountNo;
	}

	public List<PaymentTypeMaintenanceDTO> getAccountNoList() {
		return accountNoList;
	}

	public void setAccountNoList(List<PaymentTypeMaintenanceDTO> accountNoList) {
		this.accountNoList = accountNoList;
	}

	public PaymentTypeMaintenanceDTO getSelectedPaymentTypeDes() {
		return selectedPaymentTypeDes;
	}

	public void setSelectedPaymentTypeDes(PaymentTypeMaintenanceDTO selectedPaymentTypeDes) {
		this.selectedPaymentTypeDes = selectedPaymentTypeDes;
	}

	public String getSelectedChargeType() {
		return selectedChargeType;
	}

	public void setSelectedChargeType(String selectedChargeType) {
		this.selectedChargeType = selectedChargeType;
	}

	public List<PaymentTypeMaintenanceDTO> getPaymentTypeList() {
		return paymentTypeList;
	}

	public void setPaymentTypeList(List<PaymentTypeMaintenanceDTO> paymentTypeList) {
		this.paymentTypeList = paymentTypeList;
	}

	public PaymentTypeMaintenanceDTO getSelectedTranstractionTypeDes() {
		return selectedTranstractionTypeDes;
	}

	public void setSelectedTranstractionTypeDes(PaymentTypeMaintenanceDTO selectedTranstractionTypeDes) {
		this.selectedTranstractionTypeDes = selectedTranstractionTypeDes;
	}

	public String getSelectedTranstractionType() {
		return selectedTranstractionType;
	}

	public void setSelectedTranstractionType(String selectedTranstractionType) {
		this.selectedTranstractionType = selectedTranstractionType;
	}

	public List<PaymentTypeMaintenanceDTO> getTranstractionTypeList() {
		return transtractionTypeList;
	}

	public void setTranstractionTypeList(List<PaymentTypeMaintenanceDTO> transtractionTypeList) {
		this.transtractionTypeList = transtractionTypeList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public PaymentTypeMaintenanceDTO getPaymentTypeMaintenanceDTO() {
		return paymentTypeMaintenanceDTO;
	}

	public void setPaymentTypeMaintenanceDTO(PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO) {
		this.paymentTypeMaintenanceDTO = paymentTypeMaintenanceDTO;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public PaymentTypeMaintenanceService getPaymentTypeMaintenanceService() {
		return paymentTypeMaintenanceService;
	}

	public void setPaymentTypeMaintenanceService(PaymentTypeMaintenanceService paymentTypeMaintenanceService) {
		this.paymentTypeMaintenanceService = paymentTypeMaintenanceService;
	}

	public boolean isReadOnlyAmountField() {
		return readOnlyAmountField;
	}

	public void setReadOnlyAmountField(boolean readOnlyAmountField) {
		this.readOnlyAmountField = readOnlyAmountField;
	}

	public PaymentTypeMaintenanceDTO getSelectedEditRow() {
		return selectedEditRow;
	}

	public void setSelectedEditRow(PaymentTypeMaintenanceDTO selectedEditRow) {
		this.selectedEditRow = selectedEditRow;
	}

	public boolean isDisableTranstractionTypeField() {
		return disableTranstractionTypeField;
	}

	public void setDisableTranstractionTypeField(boolean disableTranstractionTypeField) {
		this.disableTranstractionTypeField = disableTranstractionTypeField;
	}

	public boolean isDisableChargeTypeField() {
		return disableChargeTypeField;
	}

	public void setDisableChargeTypeField(boolean disableChargeTypeField) {
		this.disableChargeTypeField = disableChargeTypeField;
	}

	public String getInputAccountNo() {
		return inputAccountNo;
	}

	public void setInputAccountNo(String inputAccountNo) {
		this.inputAccountNo = inputAccountNo;
	}

	public boolean isShowInputAccountNoField() {
		return showInputAccountNoField;
	}

	public void setShowInputAccountNoField(boolean showInputAccountNoField) {
		this.showInputAccountNoField = showInputAccountNoField;
	}

	public boolean isShowSearchAccountNoMenu() {
		return showSearchAccountNoMenu;
	}

	public void setShowSearchAccountNoMenu(boolean showSearchAccountNoMenu) {
		this.showSearchAccountNoMenu = showSearchAccountNoMenu;
	}

	public boolean isShowSearchBtn() {
		return showSearchBtn;
	}

	public void setShowSearchBtn(boolean showSearchBtn) {
		this.showSearchBtn = showSearchBtn;
	}

	public boolean isShowSaveBtn() {
		return showSaveBtn;
	}

	public void setShowSaveBtn(boolean showSaveBtn) {
		this.showSaveBtn = showSaveBtn;
	}

	public boolean isPressedEditBtn() {
		return pressedEditBtn;
	}

	public void setPressedEditBtn(boolean pressedEditBtn) {
		this.pressedEditBtn = pressedEditBtn;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public PaymentTypeMaintenanceDTO getSelectedDeleteRow() {
		return selectedDeleteRow;
	}

	public void setSelectedDeleteRow(PaymentTypeMaintenanceDTO selectedDeleteRow) {
		this.selectedDeleteRow = selectedDeleteRow;
	}

}
