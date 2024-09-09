package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.TerminalPayCancellationDTO;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "acceptPaymentCancellationBackingBean")
@ViewScoped

public class AcceptPaymentCancellationBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Services
	private TerminalManagementService terminalManagementService;

	// DTOs
	private List<TerminalPayCancellationDTO> paymentCancellationList = new ArrayList<TerminalPayCancellationDTO>();
	private TerminalPayCancellationDTO terminalPayCancellationDTO;
	private TerminalPayCancellationDTO strSelectedVoucherSeq;
	// SelectedValues
	private String errorMsg;
	private String successMessage;
	private String reqVoucherNo;

	@PostConstruct
	public void init() {
		setTerminalPayCancellationDTO(new TerminalPayCancellationDTO());
		terminalManagementService = (TerminalManagementService) SpringApplicationContex
				.getBean("terminalManagementService");
		setReqVoucherNo(null);
		terminalPayCancellationDTO = new TerminalPayCancellationDTO();
	}

	// Methods
	public void searchVoucher() {
		paymentCancellationList = terminalManagementService.getDetailsByVoucherNo(reqVoucherNo.trim());
		if (paymentCancellationList.isEmpty()) {
			setErrorMsg("No Data Found.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearVoucher() {
		init();
		setReqVoucherNo(null);
		terminalPayCancellationDTO = new TerminalPayCancellationDTO();
		paymentCancellationList.clear();
	}

	public void cancelAction() {
		boolean isCancelled = terminalManagementService.cancelledPayment((strSelectedVoucherSeq.getSeq()),
				sessionBackingBean.getLoginUser());

		if (isCancelled == true) {
			setSuccessMessage("Successfully Cancelled");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			terminalManagementService.beanLinkMethod("Cancel Voucher", sessionBackingBean.getLoginUser(), "Accept Payment Cancellation", reqVoucherNo);
			clearVoucher();

		} else {
			setErrorMsg("Cancelled unsuccessfully");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public TerminalManagementService getTerminalManagementService() {
		return terminalManagementService;
	}

	public void setTerminalManagementService(TerminalManagementService terminalManagementService) {
		this.terminalManagementService = terminalManagementService;
	}

	public List<TerminalPayCancellationDTO> getPaymentCancellationList() {
		return paymentCancellationList;
	}

	public void setPaymentCancellationList(List<TerminalPayCancellationDTO> paymentCancellationList) {
		this.paymentCancellationList = paymentCancellationList;
	}

	public TerminalPayCancellationDTO getTerminalPayCancellationDTO() {
		return terminalPayCancellationDTO;
	}

	public void setTerminalPayCancellationDTO(TerminalPayCancellationDTO terminalPayCancellationDTO) {
		this.terminalPayCancellationDTO = terminalPayCancellationDTO;
	}

	public TerminalPayCancellationDTO getStrSelectedVoucherSeq() {
		return strSelectedVoucherSeq;
	}

	public void setStrSelectedVoucherSeq(TerminalPayCancellationDTO strSelectedVoucherSeq) {
		this.strSelectedVoucherSeq = strSelectedVoucherSeq;
	}

	public String getReqVoucherNo() {
		return reqVoucherNo;
	}

	public void setReqVoucherNo(String reqVoucherNo) {
		this.reqVoucherNo = reqVoucherNo;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
