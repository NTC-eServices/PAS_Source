package lk.informatics.ntc.view.beans;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.PaymentTypeMaintenanceDTO;
import lk.informatics.ntc.model.service.PaymentTypeMaintenanceService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "createPaymentMaintenanceBackingBean")
@ViewScoped
public class CreatePaymentMaintenanceBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private String selectedTranstractionType;
	private String selectedChargeType;
	private String inputAccountNo;
	private Double displayAmount;
	private String selectedStatus;
	private String inputCode;
	private String errorMsg;
	private String sucessMsg;
	private String inputChargeTypeCode;
	private String inputChargeTypeDes;
	private String beforeEditingChargeTypeDes;
	private String beforeChargeTypeCode;
	private boolean disableTranstractionTypeField = false;
	private boolean disableChargeTypeField = false;
	private boolean disableInputCodeField = false;
	private boolean showSaveBtn = true;
	private boolean showUpdateBtn = false;
	private boolean disableChargeTypeCodeField = false;
	private boolean disableChargeTypeDesField = false;
	private boolean showChargeTypeSaveBtn = true;
	private boolean showChargeTypeUpdateBtn = false;

	private PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO = new PaymentTypeMaintenanceDTO();
	private PaymentTypeMaintenanceDTO selectedTranstractionTypeDes;
	private PaymentTypeMaintenanceDTO selectedPaymentTypeDes;
	private PaymentTypeMaintenanceDTO selectedStatusDes;
	private PaymentTypeMaintenanceDTO selectedEditRow;
	private PaymentTypeMaintenanceDTO selectedDeleteRow;

	PaymentTypeMaintenanceService paymentTypeMaintenanceService;

	public List<PaymentTypeMaintenanceDTO> transtractionTypeList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> paymentTypeList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> statusList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> tctCodeList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> dataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> popupdataList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> chargeTypeCodeList = new ArrayList<PaymentTypeMaintenanceDTO>(0);
	public List<PaymentTypeMaintenanceDTO> chargeTypeCodeListForCurrentTranstractionType = new ArrayList<PaymentTypeMaintenanceDTO>(
			0);

	public CreatePaymentMaintenanceBackingBean() {
		paymentTypeMaintenanceService = (PaymentTypeMaintenanceService) SpringApplicationContex
				.getBean("paymentTypeMaintenanceService");
		transtractionTypeList = paymentTypeMaintenanceService.getAllTranstractionTypeList();
		paymentTypeList = paymentTypeMaintenanceService.getAllPaymentTypeList();
		statusList = paymentTypeMaintenanceService.getAllStatusList();
		popupdataList = paymentTypeMaintenanceService.getAllChargeTypesList();
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

	}

	public void onstatusChange() {

		paymentTypeMaintenanceDTO.setStatusCode(selectedStatus);
		selectedStatusDes = paymentTypeMaintenanceService.getStatusDescription(selectedStatus);
		paymentTypeMaintenanceDTO.setStatusDescription(selectedStatusDes.getStatusDescription());

	}

	public void saveRecord() {

		if (!(inputCode.isEmpty() || inputCode == null || inputCode.equals("") || selectedTranstractionType.isEmpty()
				|| selectedTranstractionType == null || selectedTranstractionType.equals("")
				|| selectedChargeType.isEmpty() || selectedChargeType == null || selectedChargeType.equals("")
				|| inputAccountNo.isEmpty() || inputAccountNo == null || inputAccountNo.equals("")
				|| displayAmount == null || displayAmount.equals("") || selectedStatus.isEmpty()
				|| selectedStatus == null || selectedStatus.equals(""))) {

			String newTechCode = inputCode;
			String checkingChargeTypeCode = selectedChargeType;
			tctCodeList = paymentTypeMaintenanceService.getAllTctCodeList();
			chargeTypeCodeListForCurrentTranstractionType = paymentTypeMaintenanceService
					.getAllChargeTypeCodesForCurrentTranstractionType(selectedTranstractionType);
			int size = tctCodeList.size();
			boolean isDuplicated = true;
			boolean chargeTypeCodeDuplicated = true;
			for (int i = 0; i < size; i++) {

				if (newTechCode.equals(tctCodeList.get(i).getGeneratedTctCodes())) {

					isDuplicated = true;
					break;
				} else {
					isDuplicated = false;
				}
			}
			if (tctCodeList.isEmpty()) {
				isDuplicated = false;
			} else {

			}
			for (int i = 0; i < chargeTypeCodeListForCurrentTranstractionType.size(); i++) {

				if (checkingChargeTypeCode.equals(
						chargeTypeCodeListForCurrentTranstractionType.get(i).getCheckingChargeTypeCodeListValue())) {

					chargeTypeCodeDuplicated = true;
					break;
				} else {
					chargeTypeCodeDuplicated = false;
				}
			}
			if (chargeTypeCodeListForCurrentTranstractionType.isEmpty()) {
				chargeTypeCodeDuplicated = false;

			}

			if (isDuplicated == false) {
				if (chargeTypeCodeDuplicated == false) {
					String createdBy = sessionBackingBean.loginUser;
					paymentTypeMaintenanceDTO.setCreatedBy(createdBy);
					paymentTypeMaintenanceDTO.setTectCode(inputCode);
					paymentTypeMaintenanceDTO.setTranstractionTypeCode(selectedTranstractionType);
					selectedTranstractionTypeDes = paymentTypeMaintenanceService
							.getTranstractionDescription(selectedTranstractionType);
					paymentTypeMaintenanceDTO
							.setTranstractionDescription(selectedTranstractionTypeDes.getTranstractionDescription());
					paymentTypeMaintenanceDTO.setPaymentTypeCode(selectedChargeType);
					selectedPaymentTypeDes = paymentTypeMaintenanceService
							.getPaymentTypeDescription(selectedChargeType);
					paymentTypeMaintenanceDTO
							.setPaymentTypeDescription(selectedPaymentTypeDes.getPaymentTypeDescription());
					paymentTypeMaintenanceDTO.setAccountNo(inputAccountNo);
					paymentTypeMaintenanceDTO.setAmount(displayAmount);
					paymentTypeMaintenanceDTO.setStatusCode(selectedStatus);
					selectedStatusDes = paymentTypeMaintenanceService.getStatusDescription(selectedStatus);
					paymentTypeMaintenanceDTO.setStatusDescription(selectedStatusDes.getStatusDescription());

					int result = paymentTypeMaintenanceService.insertNewRecordWithTechCode(paymentTypeMaintenanceDTO);
					if (result == 0) {

						RequestContext.getCurrentInstance().update("frmsuccessSve");
						setSucessMsg("Successfully saved.");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						PaymentTypeMaintenanceDTO dto = new PaymentTypeMaintenanceDTO();
						dto.setTectCode(paymentTypeMaintenanceDTO.getTectCode());
						dto.setTranstractionTypeCode(paymentTypeMaintenanceDTO.getTranstractionTypeCode());
						dto.setTranstractionDescription(paymentTypeMaintenanceDTO.getTranstractionDescription());
						dto.setPaymentTypeCode(paymentTypeMaintenanceDTO.getPaymentTypeCode());
						dto.setPaymentTypeDescription(paymentTypeMaintenanceDTO.getPaymentTypeDescription());
						dto.setAccountNo(paymentTypeMaintenanceDTO.getAccountNo());

						NumberFormat formatter = new DecimalFormat("#0.00");
						String displayAmount = formatter.format(paymentTypeMaintenanceDTO.getAmount());

						dto.setDisplaySettingAmount(displayAmount);
						dto.setStatusCode(paymentTypeMaintenanceDTO.getStatusCode());
						dto.setStatusDescription(paymentTypeMaintenanceDTO.getStatusDescription());
						dataList.add(dto);

						clearFields();
					} else {
						errorMsg = "You can not save.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "A different Charge Type should be selected.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Code should be unique.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else if (inputCode.isEmpty() || inputCode == null || inputCode.equals("")
				|| selectedTranstractionType.isEmpty() || selectedTranstractionType == null
				|| selectedTranstractionType.equals("") || selectedChargeType.isEmpty() || selectedChargeType == null
				|| selectedChargeType.equals("") || inputAccountNo.isEmpty() || inputAccountNo == null
				|| inputAccountNo.equals("") || displayAmount == null || displayAmount.equals("")
				|| selectedStatus.isEmpty() || selectedStatus == null || selectedStatus.equals("")) {
			if (inputCode.isEmpty() || inputCode == null) {

				errorMsg = "Code should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (selectedTranstractionType.isEmpty() || selectedTranstractionType == null) {

				errorMsg = "Transaction Type should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (selectedChargeType.isEmpty() || selectedChargeType == null) {

				errorMsg = "Charge Type should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (inputAccountNo.isEmpty() || inputAccountNo == null) {

				errorMsg = "Account No. should be entered.";
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

	public void updateRecord() {

		String checkingChargeTypeCode = selectedChargeType;
		chargeTypeCodeListForCurrentTranstractionType = paymentTypeMaintenanceService
				.getAllChargeTypeCodesForCurrentTranstractionType(selectedTranstractionType);
		boolean chargeTypeCodeDuplicated = true;
		for (int i = 0; i < chargeTypeCodeListForCurrentTranstractionType.size(); i++) {

			if (checkingChargeTypeCode.equals(
					chargeTypeCodeListForCurrentTranstractionType.get(i).getCheckingChargeTypeCodeListValue())) {

				chargeTypeCodeDuplicated = true;
				break;
			} else {
				chargeTypeCodeDuplicated = false;

			}
		}
		if (chargeTypeCodeListForCurrentTranstractionType.isEmpty()) {
			chargeTypeCodeDuplicated = false;

		}
		if (!(inputCode.isEmpty() || inputCode == null || inputCode.equals("") || selectedTranstractionType.isEmpty()
				|| selectedTranstractionType == null || selectedTranstractionType.equals("")
				|| selectedChargeType.isEmpty() || selectedChargeType == null || selectedChargeType.equals("")
				|| inputAccountNo.isEmpty() || inputAccountNo == null || inputAccountNo.equals("")
				|| displayAmount == null || displayAmount.equals("") || selectedStatus.isEmpty()
				|| selectedStatus == null || selectedStatus.equals(""))) {
			if (chargeTypeCodeDuplicated == false || selectedChargeType.equals(beforeChargeTypeCode)) {

				String modifyBy = sessionBackingBean.loginUser;
				paymentTypeMaintenanceDTO.setModifyBy(modifyBy);
				paymentTypeMaintenanceDTO.setTectCode(paymentTypeMaintenanceDTO.getTectCode());
				paymentTypeMaintenanceDTO.setTranstractionTypeCode(selectedTranstractionType);
				selectedTranstractionTypeDes = paymentTypeMaintenanceService
						.getTranstractionDescription(selectedTranstractionType);
				paymentTypeMaintenanceDTO
						.setTranstractionDescription(selectedTranstractionTypeDes.getTranstractionDescription());
				paymentTypeMaintenanceDTO.setPaymentTypeCode(selectedChargeType);
				selectedPaymentTypeDes = paymentTypeMaintenanceService.getPaymentTypeDescription(selectedChargeType);
				paymentTypeMaintenanceDTO.setPaymentTypeDescription(selectedPaymentTypeDes.getPaymentTypeDescription());
				paymentTypeMaintenanceDTO.setAccountNo(inputAccountNo);
				paymentTypeMaintenanceDTO.setAmount(displayAmount);
				paymentTypeMaintenanceDTO.setStatusCode(selectedStatus);
				selectedStatusDes = paymentTypeMaintenanceService.getStatusDescription(selectedStatus);
				paymentTypeMaintenanceDTO.setStatusDescription(selectedStatusDes.getStatusDescription());
				int result = paymentTypeMaintenanceService.updateTable(paymentTypeMaintenanceDTO);
				if (result == 0) {

					RequestContext.getCurrentInstance().update("frmsuccessSve");
					setSucessMsg("Successfully saved.");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					selectedEditRow.setTectCode(paymentTypeMaintenanceDTO.getTectCode());
					selectedEditRow.setTranstractionTypeCode(paymentTypeMaintenanceDTO.getTranstractionTypeCode());
					selectedEditRow
							.setTranstractionDescription(paymentTypeMaintenanceDTO.getTranstractionDescription());
					selectedEditRow.setPaymentTypeCode(paymentTypeMaintenanceDTO.getPaymentTypeCode());
					selectedEditRow.setPaymentTypeDescription(paymentTypeMaintenanceDTO.getPaymentTypeDescription());
					selectedEditRow.setAccountNo(paymentTypeMaintenanceDTO.getAccountNo());

					NumberFormat formatter = new DecimalFormat("#0.00");
					String displayEditingAmount = formatter.format(paymentTypeMaintenanceDTO.getAmount());

					selectedEditRow.setDisplaySettingAmount(displayEditingAmount);
					selectedEditRow.setStatusCode(paymentTypeMaintenanceDTO.getStatusCode());
					selectedEditRow.setStatusDescription(paymentTypeMaintenanceDTO.getStatusDescription());
					clearFields();
				} else {

				}
			} else {
				errorMsg = "Other Charge Type Code should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else if (inputCode.isEmpty() || inputCode == null || inputCode.equals("")
				|| selectedTranstractionType.isEmpty() || selectedTranstractionType == null
				|| selectedTranstractionType.equals("") || selectedChargeType.isEmpty() || selectedChargeType == null
				|| selectedChargeType.equals("") || inputAccountNo.isEmpty() || inputAccountNo == null
				|| inputAccountNo.equals("") || displayAmount == null || displayAmount.equals("")
				|| selectedStatus.isEmpty() || selectedStatus == null || selectedStatus.equals("")) {

			if (inputCode.isEmpty() || inputCode == null) {

				errorMsg = "Code should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (selectedTranstractionType.isEmpty() || selectedTranstractionType == null) {

				errorMsg = "Transaction Type should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (selectedChargeType.isEmpty() || selectedChargeType == null) {

				errorMsg = "Charge Type should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (inputAccountNo.isEmpty() || inputAccountNo == null) {

				errorMsg = "Account No. should be entered.";
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

	public void clearFields() {

		setInputCode(null);
		setSelectedTranstractionType(null);
		setSelectedChargeType(null);
		setInputAccountNo(null);
		setDisplayAmount(null);
		setSelectedStatus(null);
		setDisableInputCodeField(false);
		setDisableTranstractionTypeField(false);
		setDisableChargeTypeField(false);
		setShowSaveBtn(true);
		setShowUpdateBtn(false);
	}

	public void editActButtonAction() {

		boolean resultValue = paymentTypeMaintenanceService.checkHaveFunction(selectedEditRow.getPaymentTypeCode(),
				selectedEditRow.getAccountNo());
		if (resultValue == false) {

			paymentTypeMaintenanceDTO.setTectCode(selectedEditRow.getTectCode());
			paymentTypeMaintenanceDTO.setTranstractionTypeCode(selectedEditRow.getTranstractionTypeCode());
			paymentTypeMaintenanceDTO.setTranstractionDescription(selectedEditRow.getTranstractionDescription());
			paymentTypeMaintenanceDTO.setPaymentTypeCode(selectedEditRow.getPaymentTypeCode());
			paymentTypeMaintenanceDTO.setPaymentTypeDescription(selectedEditRow.getPaymentTypeDescription());
			paymentTypeMaintenanceDTO.setAccountNo(selectedEditRow.getAccountNo());
			paymentTypeMaintenanceDTO.setDisplaySettingAmount(selectedEditRow.getDisplaySettingAmount());
			paymentTypeMaintenanceDTO.setStatusCode(selectedEditRow.getStatusCode());
			paymentTypeMaintenanceDTO.setStatusDescription(selectedEditRow.getStatusDescription());
			beforeChargeTypeCode = selectedEditRow.getPaymentTypeCode();
			setInputCode(selectedEditRow.getTectCode());
			setSelectedTranstractionType(selectedEditRow.getTranstractionTypeCode());
			setSelectedChargeType(selectedEditRow.getPaymentTypeCode());
			setInputAccountNo(selectedEditRow.getAccountNo());
			setDisplayAmount(Double.parseDouble(selectedEditRow.getDisplaySettingAmount()));
			setSelectedStatus(selectedEditRow.getStatusCode());
			setDisableInputCodeField(true);
			setDisableTranstractionTypeField(true);
			setShowSaveBtn(false);
			setShowUpdateBtn(true);
		} else {

			errorMsg = "Record is already assigned to create a payment.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
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

			errorMsg = "Record is already assigned to create a payment.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		clearFields();
	}

	public void deleteRole() {

		RequestContext.getCurrentInstance().update("@form");
		paymentTypeMaintenanceDTO.setTectCode(selectedDeleteRow.getTectCode());
		int result = paymentTypeMaintenanceService.deleteRecord(paymentTypeMaintenanceDTO.getTectCode());
		if (result == 0) {

			RequestContext.getCurrentInstance().update("frmsuccessSve");
			setSucessMsg("Successfully deleted.");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			dataList.remove(selectedDeleteRow);
			RequestContext.getCurrentInstance().update("frmPayemntTypeTable");

		} else {
			errorMsg = "Record is already assigned to create a payment.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void viewChargeTypePopUp() {
		RequestContext.getCurrentInstance().execute("PF('viewCreateChargeType').show()");
	}

	public void saveChargeType() {

		RequestContext.getCurrentInstance().execute("PF('viewCreateChargeType').show()");
		if (!(inputChargeTypeCode.isEmpty() || inputChargeTypeCode == null || inputChargeTypeCode.equals("")
				|| inputChargeTypeDes.isEmpty() || inputChargeTypeDes == null || inputChargeTypeDes.equals(""))) {

			String newChargeTypeCode = inputChargeTypeCode;
			String newChargeTypeDes = inputChargeTypeDes;
			chargeTypeCodeList = paymentTypeMaintenanceService.getAllChargeTypeCodeList();
			int size = chargeTypeCodeList.size();
			boolean isDuplicated = true;
			for (int i = 0; i < size; i++) {
				if (newChargeTypeDes.equals(chargeTypeCodeList.get(i).getGeneratedChargeTypeCodeDes())) {

					isDuplicated = true;
					break;
				} else {
					isDuplicated = false;

				}
			}
			if (chargeTypeCodeList.isEmpty()) {
				isDuplicated = false;
			} else {

			}

			if (isDuplicated == false) {
				paymentTypeMaintenanceDTO.setPopUpChargeTypeCode(inputChargeTypeCode);
				paymentTypeMaintenanceDTO.setPopUpChargeTypeDescription(inputChargeTypeDes);
				String createdBy = sessionBackingBean.loginUser;
				paymentTypeMaintenanceDTO.setCreatedBy(createdBy);
				paymentTypeMaintenanceDTO.setChargeTypeStatusCode("A");
				paymentTypeMaintenanceDTO.setChargeTypeStatusDes("Active");
				int result = paymentTypeMaintenanceService.insertChargeType(paymentTypeMaintenanceDTO);
				if (result == 0) {

					RequestContext.getCurrentInstance().update("frmsuccessSve");
					setSucessMsg("Successfully saved.");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					PaymentTypeMaintenanceDTO dto = new PaymentTypeMaintenanceDTO();
					dto.setPopUpChargeTypeCode(paymentTypeMaintenanceDTO.getPopUpChargeTypeCode());
					dto.setPopUpChargeTypeDescription(paymentTypeMaintenanceDTO.getPopUpChargeTypeDescription());
					dto.setChargeTypeStatusCode(paymentTypeMaintenanceDTO.getChargeTypeStatusCode());
					dto.setChargeTypeStatusDes(paymentTypeMaintenanceDTO.getChargeTypeStatusDes());
					popupdataList.add(dto);
					setInputChargeTypeCode(null);
					setInputChargeTypeDes(null);
				} else {
					errorMsg = "Charge Type Code should be unique.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Charge Type Description should be unique.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else if (inputChargeTypeCode.isEmpty() || inputChargeTypeCode == null || inputChargeTypeCode.equals("")
				|| inputChargeTypeDes.isEmpty() || inputChargeTypeDes == null || inputChargeTypeDes.equals("")) {

			if (inputChargeTypeCode.isEmpty() || inputChargeTypeCode == null || inputChargeTypeCode.equals("")) {

				errorMsg = "Charge Type Code should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (inputChargeTypeDes.isEmpty() || inputChargeTypeDes == null || inputChargeTypeDes.equals("")) {

				errorMsg = "Charge Type Description should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
	}

	public void updateChargeType() {

		RequestContext.getCurrentInstance().execute("PF('viewCreateChargeType').show()");
		if (!(inputChargeTypeCode.isEmpty() || inputChargeTypeCode == null || inputChargeTypeCode.equals("")
				|| inputChargeTypeDes.isEmpty() || inputChargeTypeDes == null || inputChargeTypeDes.equals(""))) {

			String editChargeTypeCode = inputChargeTypeCode;
			String editChargeTypeDes = inputChargeTypeDes;
			chargeTypeCodeList = paymentTypeMaintenanceService.getAllChargeTypeCodeList();
			int size = chargeTypeCodeList.size();
			boolean isDuplicated = true;
			for (int i = 0; i < size; i++) {
				if (editChargeTypeDes.equals(chargeTypeCodeList.get(i).getGeneratedChargeTypeCodeDes())) {

					isDuplicated = true;
					break;
				} else {
					isDuplicated = false;
				}
			}
			if (chargeTypeCodeList.isEmpty()) {
				isDuplicated = false;
			} else {

			}

			if (!inputChargeTypeDes.equals(beforeEditingChargeTypeDes)) {
				if (isDuplicated == false || beforeEditingChargeTypeDes.equals(editChargeTypeDes)) {
					paymentTypeMaintenanceDTO
							.setPopUpChargeTypeCode(paymentTypeMaintenanceDTO.getPopUpChargeTypeCode());
					paymentTypeMaintenanceDTO.setPopUpChargeTypeDescription(inputChargeTypeDes);
					String modifyBy = sessionBackingBean.loginUser;
					paymentTypeMaintenanceDTO.setModifyBy(modifyBy);

					int result = paymentTypeMaintenanceService.updateChargeType(paymentTypeMaintenanceDTO);
					if (result == 0) {

						RequestContext.getCurrentInstance().update("frmsuccessSve");
						setSucessMsg("Successfully saved.");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						selectedEditRow.setPopUpChargeTypeCode(paymentTypeMaintenanceDTO.getPopUpChargeTypeCode());
						selectedEditRow.setPopUpChargeTypeDescription(
								paymentTypeMaintenanceDTO.getPopUpChargeTypeDescription());
						selectedEditRow.setChargeTypeStatusCode(paymentTypeMaintenanceDTO.getChargeTypeStatusCode());
						selectedEditRow.setChargeTypeStatusDes(paymentTypeMaintenanceDTO.getChargeTypeStatusDes());
						RequestContext.getCurrentInstance().update("frmcreateChargeTypes");
						setInputChargeTypeCode(null);
						setInputChargeTypeDes(null);
						setDisableChargeTypeCodeField(false);
						clearFields();
					} else {
						errorMsg = "Charge Type Code should be unique.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Charge Type Description should be unique.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				errorMsg = "Please update record before save.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else if (inputChargeTypeCode.isEmpty() || inputChargeTypeCode == null || inputChargeTypeCode.equals("")
				|| inputChargeTypeDes.isEmpty() || inputChargeTypeDes == null || inputChargeTypeDes.equals("")) {

			if (inputChargeTypeCode.isEmpty() || inputChargeTypeCode == null || inputChargeTypeCode.equals("")) {

				errorMsg = "Charge Type Code should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (inputChargeTypeDes.isEmpty() || inputChargeTypeDes == null || inputChargeTypeDes.equals("")) {

				errorMsg = "Charge Type Description should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}

	}

	public void clearPopup() {

		RequestContext.getCurrentInstance().execute("PF('viewCreateChargeType').show()");
		setInputChargeTypeCode(null);
		setInputChargeTypeDes(null);
		setDisableChargeTypeCodeField(false);
		setDisableChargeTypeDesField(false);
		setShowChargeTypeSaveBtn(true);
		setShowChargeTypeUpdateBtn(false);
	}

	public void popUpeditActButtonAction() {

		RequestContext.getCurrentInstance().execute("PF('viewCreateChargeType').show()");
		paymentTypeMaintenanceDTO.setPopUpChargeTypeCode(selectedEditRow.getPopUpChargeTypeCode());
		boolean resultValueWithTrn = paymentTypeMaintenanceService
				.checkHaveFunctionWithChargeTypeCode(selectedEditRow.getPopUpChargeTypeCode());
		boolean resultValueWithVoucher = paymentTypeMaintenanceService
				.checkHaveFunctionWithChargeTypeCodeOne(selectedEditRow.getPopUpChargeTypeCode());
		if (resultValueWithTrn == false || resultValueWithVoucher == false) {
			beforeEditingChargeTypeDes = selectedEditRow.getPopUpChargeTypeDescription();
			setInputChargeTypeCode(selectedEditRow.getPopUpChargeTypeCode());
			setInputChargeTypeDes(selectedEditRow.getPopUpChargeTypeDescription());
			paymentTypeMaintenanceDTO.setPopUpChargeTypeCode(selectedEditRow.getPopUpChargeTypeCode());
			paymentTypeMaintenanceDTO.setChargeTypeStatusCode(selectedEditRow.getChargeTypeStatusCode());
			paymentTypeMaintenanceDTO.setChargeTypeStatusDes(selectedEditRow.getChargeTypeStatusDes());
			setShowChargeTypeSaveBtn(false);
			setShowChargeTypeUpdateBtn(true);
			setDisableChargeTypeCodeField(true);
		} else {

			errorMsg = "Record is already assigned to create a payment.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void popUpDeleteActButtonAction() {

		RequestContext.getCurrentInstance().execute("PF('viewCreateChargeType').show()");
		boolean resultValueWithTrn = paymentTypeMaintenanceService
				.checkHaveFunctionWithChargeTypeCode(selectedDeleteRow.getPopUpChargeTypeCode());
		boolean resultValueWithVoucher = paymentTypeMaintenanceService
				.checkHaveFunctionWithChargeTypeCodeOne(selectedDeleteRow.getPopUpChargeTypeCode());
		if (resultValueWithTrn == false || resultValueWithVoucher == false) {
			RequestContext.getCurrentInstance().update("frmcreateChargeTypes");
			paymentTypeMaintenanceDTO.setPopUpChargeTypeCode(selectedDeleteRow.getPopUpChargeTypeCode());
			RequestContext.getCurrentInstance().execute("PF('deleteconfirmationPopUp').show()");
		} else {
			RequestContext.getCurrentInstance().update("frmcreateChargeTypes");
			errorMsg = "Record is already assigned to create a payment.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		clearPopup();
	}

	public void deleteChargeType() {

		RequestContext.getCurrentInstance().update("@form");
		paymentTypeMaintenanceDTO.setPopUpChargeTypeCode(selectedDeleteRow.getPopUpChargeTypeCode());
		int result = paymentTypeMaintenanceService
				.deleteChargeTypeRecord(paymentTypeMaintenanceDTO.getPopUpChargeTypeCode());
		if (result == 0) {

			RequestContext.getCurrentInstance().update("frmsuccessSve");
			setSucessMsg("Successfully deleted.");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			popupdataList.remove(selectedDeleteRow);
			RequestContext.getCurrentInstance().update("frmFunActTable1");

		} else {
			errorMsg = "Record is already assigned to create a payment.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void goBack() {

		clearPopup();
		RequestContext.getCurrentInstance().execute("PF('viewCreateChargeType').hide()");
		paymentTypeList = paymentTypeMaintenanceService.getAllPaymentTypeList();
		RequestContext.getCurrentInstance().update("frmAddVIAPM:chargeTypeId");
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getSelectedTranstractionType() {
		return selectedTranstractionType;
	}

	public void setSelectedTranstractionType(String selectedTranstractionType) {
		this.selectedTranstractionType = selectedTranstractionType;
	}

	public String getSelectedChargeType() {
		return selectedChargeType;
	}

	public void setSelectedChargeType(String selectedChargeType) {
		this.selectedChargeType = selectedChargeType;
	}

	public String getInputAccountNo() {
		return inputAccountNo;
	}

	public void setInputAccountNo(String inputAccountNo) {
		this.inputAccountNo = inputAccountNo;
	}

	public Double getDisplayAmount() {
		return displayAmount;
	}

	public void setDisplayAmount(Double displayAmount) {
		this.displayAmount = displayAmount;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
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

	public boolean isShowSaveBtn() {
		return showSaveBtn;
	}

	public void setShowSaveBtn(boolean showSaveBtn) {
		this.showSaveBtn = showSaveBtn;
	}

	public boolean isShowUpdateBtn() {
		return showUpdateBtn;
	}

	public void setShowUpdateBtn(boolean showUpdateBtn) {
		this.showUpdateBtn = showUpdateBtn;
	}

	public PaymentTypeMaintenanceDTO getPaymentTypeMaintenanceDTO() {
		return paymentTypeMaintenanceDTO;
	}

	public void setPaymentTypeMaintenanceDTO(PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO) {
		this.paymentTypeMaintenanceDTO = paymentTypeMaintenanceDTO;
	}

	public PaymentTypeMaintenanceDTO getSelectedTranstractionTypeDes() {
		return selectedTranstractionTypeDes;
	}

	public void setSelectedTranstractionTypeDes(PaymentTypeMaintenanceDTO selectedTranstractionTypeDes) {
		this.selectedTranstractionTypeDes = selectedTranstractionTypeDes;
	}

	public PaymentTypeMaintenanceDTO getSelectedPaymentTypeDes() {
		return selectedPaymentTypeDes;
	}

	public void setSelectedPaymentTypeDes(PaymentTypeMaintenanceDTO selectedPaymentTypeDes) {
		this.selectedPaymentTypeDes = selectedPaymentTypeDes;
	}

	public PaymentTypeMaintenanceDTO getSelectedStatusDes() {
		return selectedStatusDes;
	}

	public void setSelectedStatusDes(PaymentTypeMaintenanceDTO selectedStatusDes) {
		this.selectedStatusDes = selectedStatusDes;
	}

	public PaymentTypeMaintenanceDTO getSelectedEditRow() {
		return selectedEditRow;
	}

	public void setSelectedEditRow(PaymentTypeMaintenanceDTO selectedEditRow) {
		this.selectedEditRow = selectedEditRow;
	}

	public PaymentTypeMaintenanceDTO getSelectedDeleteRow() {
		return selectedDeleteRow;
	}

	public void setSelectedDeleteRow(PaymentTypeMaintenanceDTO selectedDeleteRow) {
		this.selectedDeleteRow = selectedDeleteRow;
	}

	public PaymentTypeMaintenanceService getPaymentTypeMaintenanceService() {
		return paymentTypeMaintenanceService;
	}

	public void setPaymentTypeMaintenanceService(PaymentTypeMaintenanceService paymentTypeMaintenanceService) {
		this.paymentTypeMaintenanceService = paymentTypeMaintenanceService;
	}

	public List<PaymentTypeMaintenanceDTO> getTranstractionTypeList() {
		return transtractionTypeList;
	}

	public void setTranstractionTypeList(List<PaymentTypeMaintenanceDTO> transtractionTypeList) {
		this.transtractionTypeList = transtractionTypeList;
	}

	public List<PaymentTypeMaintenanceDTO> getPaymentTypeList() {
		return paymentTypeList;
	}

	public void setPaymentTypeList(List<PaymentTypeMaintenanceDTO> paymentTypeList) {
		this.paymentTypeList = paymentTypeList;
	}

	public List<PaymentTypeMaintenanceDTO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<PaymentTypeMaintenanceDTO> statusList) {
		this.statusList = statusList;
	}

	public List<PaymentTypeMaintenanceDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PaymentTypeMaintenanceDTO> dataList) {
		this.dataList = dataList;
	}

	public String getInputCode() {
		return inputCode;
	}

	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

	public boolean isDisableInputCodeField() {
		return disableInputCodeField;
	}

	public void setDisableInputCodeField(boolean disableInputCodeField) {
		this.disableInputCodeField = disableInputCodeField;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<PaymentTypeMaintenanceDTO> getTctCodeList() {
		return tctCodeList;
	}

	public void setTctCodeList(List<PaymentTypeMaintenanceDTO> tctCodeList) {
		this.tctCodeList = tctCodeList;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getInputChargeTypeCode() {
		return inputChargeTypeCode;
	}

	public void setInputChargeTypeCode(String inputChargeTypeCode) {
		this.inputChargeTypeCode = inputChargeTypeCode;
	}

	public String getInputChargeTypeDes() {
		return inputChargeTypeDes;
	}

	public void setInputChargeTypeDes(String inputChargeTypeDes) {
		this.inputChargeTypeDes = inputChargeTypeDes;
	}

	public boolean isDisableChargeTypeCodeField() {
		return disableChargeTypeCodeField;
	}

	public void setDisableChargeTypeCodeField(boolean disableChargeTypeCodeField) {
		this.disableChargeTypeCodeField = disableChargeTypeCodeField;
	}

	public boolean isDisableChargeTypeDesField() {
		return disableChargeTypeDesField;
	}

	public void setDisableChargeTypeDesField(boolean disableChargeTypeDesField) {
		this.disableChargeTypeDesField = disableChargeTypeDesField;
	}

	public boolean isShowChargeTypeSaveBtn() {
		return showChargeTypeSaveBtn;
	}

	public void setShowChargeTypeSaveBtn(boolean showChargeTypeSaveBtn) {
		this.showChargeTypeSaveBtn = showChargeTypeSaveBtn;
	}

	public boolean isShowChargeTypeUpdateBtn() {
		return showChargeTypeUpdateBtn;
	}

	public void setShowChargeTypeUpdateBtn(boolean showChargeTypeUpdateBtn) {
		this.showChargeTypeUpdateBtn = showChargeTypeUpdateBtn;
	}

	public List<PaymentTypeMaintenanceDTO> getPopupdataList() {
		return popupdataList;
	}

	public void setPopupdataList(List<PaymentTypeMaintenanceDTO> popupdataList) {
		this.popupdataList = popupdataList;
	}

	public List<PaymentTypeMaintenanceDTO> getChargeTypeCodeList() {
		return chargeTypeCodeList;
	}

	public void setChargeTypeCodeList(List<PaymentTypeMaintenanceDTO> chargeTypeCodeList) {
		this.chargeTypeCodeList = chargeTypeCodeList;
	}

	public List<PaymentTypeMaintenanceDTO> getChargeTypeCodeListForCurrentTranstractionType() {
		return chargeTypeCodeListForCurrentTranstractionType;
	}

	public void setChargeTypeCodeListForCurrentTranstractionType(
			List<PaymentTypeMaintenanceDTO> chargeTypeCodeListForCurrentTranstractionType) {
		this.chargeTypeCodeListForCurrentTranstractionType = chargeTypeCodeListForCurrentTranstractionType;
	}

	public String getBeforeChargeTypeCode() {
		return beforeChargeTypeCode;
	}

	public void setBeforeChargeTypeCode(String beforeChargeTypeCode) {
		this.beforeChargeTypeCode = beforeChargeTypeCode;
	}

}
