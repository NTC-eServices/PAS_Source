package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lk.informatics.ntc.model.dto.ExpiredPermitDTO;
import lk.informatics.ntc.model.service.ExpiredPermitsApprovalService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "expiredPermitsApprovalBackingBean")
@ViewScoped
public class ExpiredPermitsApprovalBackingBean implements Serializable {

	private static final long serialVersionUID = 5145554514856550681L;

	private String errorMessage, successMessage, infoMessage;
	boolean disabledApprove = true, disabledReject = true, disabledClear = true;
	private ExpiredPermitDTO expiredPermitDTO;
	private ExpiredPermitDTO selectedRowDTO;
	private ExpiredPermitsApprovalService expiredPermitsApprovalService;
	private List<ExpiredPermitDTO> permitNoList = new ArrayList<ExpiredPermitDTO>(0);
	private List<ExpiredPermitDTO> applicationNoList = new ArrayList<ExpiredPermitDTO>(0);
	private List<ExpiredPermitDTO> expiredPermitsApprovalList = new ArrayList<ExpiredPermitDTO>(0);

	@PostConstruct
	public void init() {
		expiredPermitDTO = new ExpiredPermitDTO();
		expiredPermitsApprovalService = (ExpiredPermitsApprovalService) SpringApplicationContex
				.getBean("expiredPermitsApprovalService");
		permitNoList = expiredPermitsApprovalService.getPermitNo();
		applicationNoList = expiredPermitsApprovalService.getApplicationNo();
	}

	public void clear() {
		expiredPermitDTO.setPermitNo("");
		expiredPermitDTO.setRenewalApplicationNo("");
		expiredPermitDTO.setRegNoOfBus("");
		expiredPermitDTO.setSearchDate(null);
		expiredPermitDTO.setQueueNo("");
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

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public ExpiredPermitDTO getExpiredPermitDTO() {
		return expiredPermitDTO;
	}

	public void setExpiredPermitDTO(ExpiredPermitDTO expiredPermitDTO) {
		this.expiredPermitDTO = expiredPermitDTO;
	}

	public List<ExpiredPermitDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<ExpiredPermitDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<ExpiredPermitDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<ExpiredPermitDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public List<ExpiredPermitDTO> getExpiredPermitsApprovalList() {
		return expiredPermitsApprovalList;
	}

	public void setExpiredPermitsApprovalList(List<ExpiredPermitDTO> expiredPermitsApprovalList) {
		this.expiredPermitsApprovalList = expiredPermitsApprovalList;
	}

	public ExpiredPermitDTO getSelectedRowDTO() {
		return selectedRowDTO;
	}

	public void setSelectedRowDTO(ExpiredPermitDTO selectedRowDTO) {
		this.selectedRowDTO = selectedRowDTO;
	}

	public boolean isDisabledApprove() {
		return disabledApprove;
	}

	public void setDisabledApprove(boolean disabledApprove) {
		this.disabledApprove = disabledApprove;
	}

	public boolean isDisabledReject() {
		return disabledReject;
	}

	public void setDisabledReject(boolean disabledReject) {
		this.disabledReject = disabledReject;
	}

	public boolean isDisabledClear() {
		return disabledClear;
	}

	public void setDisabledClear(boolean disabledClear) {
		this.disabledClear = disabledClear;
	}

}
