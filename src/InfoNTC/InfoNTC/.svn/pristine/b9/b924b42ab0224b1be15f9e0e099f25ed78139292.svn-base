package lk.informatics.ntc.view.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.DivisionDTO;
import lk.informatics.ntc.model.dto.MainCounterDTO;
import lk.informatics.ntc.model.dto.QueueNumberDTO;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "queueUserConsoleBean")
@ViewScoped
public class QueueManagementUserConsoleBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private QueueManagementService queueManagementService;
	private String alertMessage, errorMessage, successMessage;
	private QueueNumberDTO queueNumberDTO;
	private List<DivisionDTO> divisionList;
	private List<MainCounterDTO> counterList;
	private boolean disabledCounter;

	public QueueManagementUserConsoleBackingBean() {
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		queueNumberDTO = new QueueNumberDTO();
		divisionList = queueManagementService.getAllDivisions();
		disabledCounter = true;
	}

	public void onDivisionSelect() {
		counterList = queueManagementService.getCountersOfDivision(queueNumberDTO.getTransTypeCode());
		disabledCounter = false;
	}

	public void callQueueNo() {

		if (queueNumberDTO.getTransTypeCode() != null
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("")) {

			if (queueNumberDTO.getQueueNumber() != null
					&& !queueNumberDTO.getQueueNumber().trim().equalsIgnoreCase("")) {

				if (queueNumberDTO.getCounterNo() != null
						&& !queueNumberDTO.getCounterNo().trim().equalsIgnoreCase("")) {

					int queueNoValid = queueManagementService.isValidQueueNo(queueNumberDTO.getQueueNumber());

					if (queueNoValid == 1) {

						if (queueNumberDTO.getTransTypeCode() == "OP") {

							if (queueManagementService.isQueueNoProceed(queueNumberDTO.getQueueNumber()) == false) {

								if (queueManagementService.callTokenNo(queueNumberDTO.getTransTypeCode(),
										queueNumberDTO.getCounterNo(), queueNumberDTO.getQueueNumber())) {

									setSuccessMessage("Token number " + queueNumberDTO.getQueueNumber()
											+ " is called successfully.");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

								} else {
									setAlertMessage(
											"Cannot call " + queueNumberDTO.getQueueNumber() + " token number.");
									RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
								}

							} else {
								setAlertMessage("Token number is already proceeded.");
								RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
							}

						} else {

							if (queueManagementService.callTokenNo(queueNumberDTO.getTransTypeCode(),
									queueNumberDTO.getCounterNo(), queueNumberDTO.getQueueNumber())) {

								setSuccessMessage(
										"Token number " + queueNumberDTO.getQueueNumber() + " is called successfully.");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

							} else {
								setAlertMessage("Cannot call " + queueNumberDTO.getQueueNumber() + " token number.");
								RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
							}
						}

					} else if (queueNoValid == 2) {

						setAlertMessage("Token number already assigned for a counter.");
						RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");

					} else {
						setErrorMessage("Invalid token number.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select a counter.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please enter a token number.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please select a division.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void callNextQueueNo() {

		if (queueNumberDTO.getTransTypeCode() != null
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("")) {

			if (queueNumberDTO.getTransTypeCode() == "OP") {

				if (queueManagementService.isQueueNoProceed(queueNumberDTO.getQueueNumber()) == false) {

					String callNextNumber = queueManagementService.getNextToken(queueNumberDTO.getTransTypeCode());

					if (callNextNumber != null) {

						queueNumberDTO.setQueueNumber(callNextNumber);
						disabledCounter = false;
					} else {
						setAlertMessage("Cannot find token number for call next.");
						RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
					}

				} else {
					setAlertMessage("Token number is already proceeded.");
					RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
				}

			} else {
				String callNextNumber = queueManagementService.getNextToken(queueNumberDTO.getTransTypeCode());

				if (callNextNumber != null) {

					queueNumberDTO.setQueueNumber(callNextNumber);
					disabledCounter = false;
				} else {
					setAlertMessage("Cannot find token number for call next.");
					RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
				}
			}

		} else {
			setErrorMessage("Please select a division.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearData() {
		String divisionCode = queueNumberDTO.getTransTypeCode();
		queueNumberDTO = new QueueNumberDTO();
		queueNumberDTO.setTransTypeCode(divisionCode);

		disabledCounter = false;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
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

	public QueueNumberDTO getQueueNumberDTO() {
		return queueNumberDTO;
	}

	public void setQueueNumberDTO(QueueNumberDTO queueNumberDTO) {
		this.queueNumberDTO = queueNumberDTO;
	}

	public List<DivisionDTO> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<DivisionDTO> divisionList) {
		this.divisionList = divisionList;
	}

	public boolean isDisabledCounter() {
		return disabledCounter;
	}

	public void setDisabledCounter(boolean disabledCounter) {
		this.disabledCounter = disabledCounter;
	}

	public List<MainCounterDTO> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<MainCounterDTO> counterList) {
		this.counterList = counterList;
	}

}
