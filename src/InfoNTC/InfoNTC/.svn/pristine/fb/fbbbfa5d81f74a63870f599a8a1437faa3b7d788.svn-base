package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "paymentVoucherApprove")
@ViewScoped
public class PaymentVoucherApprovalBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private PaymentVoucherDTO paymentVoucherDTO;
	public PaymentVoucherService paymentVoucherService;
	private List<PaymentVoucherDTO> transactionTypeList = new ArrayList<PaymentVoucherDTO>(0);
	private List<PaymentVoucherDTO> departmentList = new ArrayList<PaymentVoucherDTO>(0);
	private List<PaymentVoucherDTO> unitList = new ArrayList<PaymentVoucherDTO>(0);
	private List<PaymentVoucherDTO> receiptNoList = new ArrayList<PaymentVoucherDTO>(0);
	private List<String> voucherNoList = new ArrayList<String>(0);
	private List<PaymentVoucherDTO> applicationNo = new ArrayList<PaymentVoucherDTO>(0);
	private String errorMessage, successMessage, rejectReason, advanceSpecialRemark, voucherSpecialRemark, date1,
			alertMSG, InfoMSG;
	private List<PaymentVoucherDTO> paymentList;
	private List<PaymentVoucherDTO> advancePaymentList, voucherPaymentList;
	private PaymentVoucherDTO selectDTO, viewSelect;
	private boolean disableApprove, disableReject, disableClear, disableSave1, disableSave2;
	private BigDecimal totalfee;
	private CommonService commonService;
	String sisuRequestNo = null;
	String sisuRefNo = null;

	public PaymentVoucherApprovalBackingBean() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		paymentVoucherDTO = new PaymentVoucherDTO();
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		transactionTypeList = paymentVoucherService.getTranactionType();
		departmentList = paymentVoucherService.getDepartment();
		unitList = paymentVoucherService.getUnit();
		receiptNoList = paymentVoucherService.getReceiptNo();
		voucherNoList = paymentVoucherService.getVoucherNo();
		applicationNo = paymentVoucherService.getApplicationNo();
		disableApprove = true;
		disableReject = true;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date1 = dateFormat.format(date);
		loadValues();
	}

	public void loadValues() {
		paymentList = new ArrayList<PaymentVoucherDTO>();
		paymentList = paymentVoucherService.getApprovedPaymentDetails(paymentVoucherDTO);
	}

	public void unselectRow() {

		RequestContext.getCurrentInstance().update("dataTable");
		RequestContext.getCurrentInstance().reset("dataTable");
	}

	public void selectRow() {

		String loginUser = sessionBackingBean.getLoginUser();

		disableApprove = false;
		disableReject = false;

		if (selectDTO.getApproveStatus().equals("PENDING")) {

			if (selectDTO.getApplicationNo().substring(0, 3).equals("TAP")) {
				commonService.updateTaskStatus(selectDTO.getApplicationNo(), "TD007", "TD008", "C",
						sessionBackingBean.getLoginUser());

			} else if (selectDTO.getApplicationNo().substring(0, 3).equals("SER")) {

				sisuRequestNo = commonService.getSisuRequstNoForServiceNoInTaskTable(selectDTO.getApplicationNo());
				sisuRefNo = commonService.getSisuRefNoForServiceNoInTaskTable(selectDTO.getApplicationNo());

				commonService.updateTaskStatusSubsidyTaskTabel(sisuRequestNo, selectDTO.getApplicationNo(), sisuRefNo,
						"SM001", "SM002", "C", sessionBackingBean.getLoginUser());
			} else {
				commonService.updateTaskStatus(selectDTO.getApplicationNo(), "PM300", "PM301", "C",
						sessionBackingBean.getLoginUser());
			}

		}

	}

	public void search() {

		if (paymentVoucherDTO.getTransactionCode() != null
				&& !paymentVoucherDTO.getTransactionCode().trim().isEmpty()) {

			boolean isAvailableTransaction = paymentVoucherService.checkTransaction(paymentVoucherDTO);

			if (isAvailableTransaction == true) {

				paymentList = new ArrayList<PaymentVoucherDTO>();
				paymentList = paymentVoucherService.getPaymentDetails(paymentVoucherDTO);

				disableApprove = false;
				disableReject = false;
				disableClear = false;

				if (paymentList.isEmpty()) {
					setAlertMSG("No data for searched values.");
					RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
				}

			} else {
				setAlertMSG("No data for searched values.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
			}

		} else {
			setErrorMessage("Please select a Transaction Type.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void reject() {

		String loginUser = sessionBackingBean.getLoginUser();
		if (selectDTO != null) {

			if (paymentVoucherService.isReceiptGenerated(selectDTO) == false) {

				boolean isPermitApprove = paymentVoucherService.isPaymentApprove(selectDTO);

				boolean isPermitReject = false;
				isPermitReject = paymentVoucherService.isPaymentReject(selectDTO);

				if (isPermitReject == false) {

					if (rejectReason != null && !rejectReason.trim().equalsIgnoreCase("")) {

						paymentVoucherDTO.setRejectReason(rejectReason);

						boolean isReject = paymentVoucherService.rejectPayment(selectDTO);

						if (isReject == true) {
							setInfoMSG("Success");
							setSuccessMessage("Successfully rejected.");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							setRejectReason(null);
							paymentList = new ArrayList<PaymentVoucherDTO>();
							paymentList = paymentVoucherService.getApprovedPaymentDetails(paymentVoucherDTO);

							if (selectDTO.getApplicationNo().substring(0, 3).equals("TAP")) {

							} else if (selectDTO.getApplicationNo().substring(0, 3).equals("SER")) {

								/* Send ongoing task to history */
								paymentVoucherService.CopyTaskDetailsANDinsertTaskHistorySubsidy(selectDTO,
										sessionBackingBean.getLoginUser(), "SM002");
								/*
								 * user need to generate new voucher so updated
								 * pm300 as ongoing
								 */
								paymentVoucherService.changeTaskDetailsSubsidy(selectDTO, "SM002", "SM001", "O");

							} else if (selectDTO.getApplicationNo().substring(0, 3).equals("PAP")
									|| selectDTO.getApplicationNo().substring(0, 3).equals("AAP")) {

								if (isPermitApprove == true) {

									/* Send ongoing task to history */
									paymentVoucherService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, loginUser,
											"PM301");
									/*
									 * user need to generate new voucher so
									 * updated pm300 as ongoing
									 */
									paymentVoucherService.changeTaskDetails(selectDTO, "PM301", "PM300", "O", "C");

								} else {

									/* Send ongoing task to history */
									paymentVoucherService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, loginUser,
											"PM301");
									/*
									 * user need to generate new voucher so
									 * updated pm300 as ongoing
									 */
									paymentVoucherService.changeTaskDetails(selectDTO, "PM301", "PM300", "O", "O");
								}

							}

						} else {
							setErrorMessage("Rejection failed");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Please add a reject reason.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

					}

				} else {
					setErrorMessage("Selected data is already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Receipt Already Generated.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void approvePayment() {
		String loginUser = sessionBackingBean.getLoginUser();
		String serviceNo = null;
		if (selectDTO != null) {

			boolean isPermitApprove = false;
			isPermitApprove = paymentVoucherService.isPaymentApprove(selectDTO);

			if (isPermitApprove == false) {

				boolean isPermitReject = false;
				isPermitReject = paymentVoucherService.isPaymentReject(selectDTO);

				if (isPermitReject == false) {
					boolean isApprove = false;
					isApprove = paymentVoucherService.approvePayment(selectDTO);

					if (isApprove == true) {

						paymentList = new ArrayList<PaymentVoucherDTO>();
						paymentList = paymentVoucherService.getApprovedPaymentDetails(paymentVoucherDTO);

						setInfoMSG("Success");
						setSuccessMessage("Payment approval successful.");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						if (selectDTO.getApplicationNo().substring(0, 3).equals("TAP")) {

							commonService.updateTaskStatusCompleted(selectDTO.getApplicationNo(), "TD008",
									sessionBackingBean.getLoginUser());

						} else if (selectDTO.getApplicationNo().substring(0, 3).equals("REF")) {// remove "SER"
							serviceNo =commonService.getSisurServiceNoInTaskTable(selectDTO.getApplicationNo());
							sisuRequestNo = commonService
									.getSisuRequstNoForServiceNoInTaskTable(serviceNo);
						//	sisuRefNo = commonService.getSisuRefNoForServiceNoInTaskTable(selectDTO.getApplicationNo());
							
							commonService.updateTaskStatusCompletedSubsidyTaskTabel(sisuRequestNo,serviceNo,
									selectDTO.getApplicationNo(), "SM002", "C");

						} else {

							commonService.updateTaskStatusCompleted(selectDTO.getApplicationNo(), "PM301",
									sessionBackingBean.getLoginUser());
						}

					} else {
						setErrorMessage("Payment approval fail.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Selected data is already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected data is already approved.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void editAction() {

		boolean isVoucherPayment = paymentVoucherService.isVoucherPayment(viewSelect);

		if (isVoucherPayment == true) {

			voucherPaymentList = new ArrayList<PaymentVoucherDTO>();
			voucherPaymentList = paymentVoucherService.getVoucherPaymentDetails(viewSelect);
			totalfee = new BigDecimal(0);
			for (int i = 0; i < voucherPaymentList.size(); i++) {

				try {

					BigDecimal amt = null;
					amt = voucherPaymentList.get(i).getAmount();
					totalfee = totalfee.add(amt);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			RequestContext.getCurrentInstance().execute("PF('voucherPayment').show()");
		} else {

			RequestContext.getCurrentInstance().execute("PF('advancedPayment').show()");

			advancePaymentList = new ArrayList<PaymentVoucherDTO>();
			advancePaymentList = paymentVoucherService.getAdvancePaymentDetails(viewSelect);

			for (int i = 0; i < advancePaymentList.size(); i++) {

				try {

					totalfee = new BigDecimal(0);
					BigDecimal amt = null;
					amt = advancePaymentList.get(i).getAmount();
					totalfee = totalfee.add(amt);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void saveSpecialRemark() {
		if (advanceSpecialRemark != null && !advanceSpecialRemark.trim().equalsIgnoreCase("")) {

			viewSelect.setSpeacialRemark(advanceSpecialRemark);

			boolean isSaveAdvance = paymentVoucherService.saveAdvanceRemark(viewSelect);

			if (isSaveAdvance == true) {
				setInfoMSG("Success");
				setSuccessMessage("Successfully saved.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				RequestContext.getCurrentInstance().execute("PF('advancedPayment').hide()");
				disableSave1 = true;
				disableSave2 = true;
			} else {
				setErrorMessage("Saving is not successful");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please enter the sepecial remark");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearOne() {
		paymentVoucherDTO = new PaymentVoucherDTO();
	}

	public void clearTwo() {
		paymentVoucherDTO = new PaymentVoucherDTO();
		paymentList = new ArrayList<PaymentVoucherDTO>();
		disableApprove = true;
		disableReject = true;
		setRejectReason(null);
		loadValues();

	}

	public List<PaymentVoucherDTO> getPaymentList() {
		return paymentList;
	}

	public List<PaymentVoucherDTO> getVoucherPaymentList() {
		return voucherPaymentList;
	}

	public void setVoucherPaymentList(List<PaymentVoucherDTO> voucherPaymentList) {
		this.voucherPaymentList = voucherPaymentList;
	}

	public List<PaymentVoucherDTO> getAdvancePaymentList() {
		return advancePaymentList;
	}

	public void setAdvancePaymentList(List<PaymentVoucherDTO> advancePaymentList) {
		this.advancePaymentList = advancePaymentList;
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

	public void setPaymentList(List<PaymentVoucherDTO> paymentList) {
		this.paymentList = paymentList;
	}

	public boolean isDisableApprove() {
		return disableApprove;
	}

	public PaymentVoucherDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(PaymentVoucherDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public void setDisableApprove(boolean disableApprove) {
		this.disableApprove = disableApprove;
	}

	public boolean isDisableReject() {
		return disableReject;
	}

	public void setDisableReject(boolean disableReject) {
		this.disableReject = disableReject;
	}

	public boolean isDisableClear() {
		return disableClear;
	}

	public void setDisableClear(boolean disableClear) {
		this.disableClear = disableClear;
	}

	public PaymentVoucherDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(PaymentVoucherDTO selectDTO) {
		this.selectDTO = selectDTO;
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

	public List<PaymentVoucherDTO> getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(List<PaymentVoucherDTO> applicationNo) {
		this.applicationNo = applicationNo;
	}

	public List<String> getVoucherNoList() {
		return voucherNoList;
	}

	public void setVoucherNoList(List<String> voucherNoList) {
		this.voucherNoList = voucherNoList;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public List<PaymentVoucherDTO> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<PaymentVoucherDTO> unitList) {
		this.unitList = unitList;
	}

	public List<PaymentVoucherDTO> getTransactionTypeList() {
		return transactionTypeList;
	}

	public void setTransactionTypeList(List<PaymentVoucherDTO> transactionTypeList) {
		this.transactionTypeList = transactionTypeList;
	}

	public List<PaymentVoucherDTO> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<PaymentVoucherDTO> departmentList) {
		this.departmentList = departmentList;
	}

	public List<PaymentVoucherDTO> getReceiptNoList() {
		return receiptNoList;
	}

	public void setReceiptNoList(List<PaymentVoucherDTO> receiptNoList) {
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

}
