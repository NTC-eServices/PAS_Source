package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.TenderApplicantDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "tenderEvaluation")
@ViewScoped
public class TenderEvaluationBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private TenderService tenderService;
	private CommonService commonService;

	private TenderApplicantDTO tenderApplicantDTO;
	private TenderDTO tenderDTO;

	private List<String> referenceNoList;
	private List<TenderApplicantDTO> routeNoList;
	private List<TenderApplicantDTO> selectingApplicationList;
	private List<TenderApplicantDTO> selectedApplicationList;
	private List<TenderApplicantDTO> temptSelectedApplicationList;

	private String errorMsg;
	private String successMsg = "Successfully Saved.";
	private boolean disableAddApplications;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {

		tenderDTO = new TenderDTO();
		tenderApplicantDTO = new TenderApplicantDTO();
		referenceNoList = new ArrayList<String>();
		routeNoList = new ArrayList<TenderApplicantDTO>();
		selectingApplicationList = new ArrayList<TenderApplicantDTO>();
		selectedApplicationList = new ArrayList<TenderApplicantDTO>();
		temptSelectedApplicationList = new ArrayList<TenderApplicantDTO>();
		loadValues();

	}

	private void loadValues() {
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		referenceNoList = tenderService.getOnGoingReferenceNoList();

	}

	public void onReferenceNoChange() {
		tenderApplicantDTO = new TenderApplicantDTO();
		tenderDTO = tenderService.getDetailsByReferenceNo(tenderDTO);

		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().isEmpty()
				&& !tenderDTO.getTenderRefNo().equalsIgnoreCase("")) {

			routeNoList = tenderService.getRouteNoList(tenderDTO.getTenderRefNo());
			getSelectedApplcaitions();

			disableAddApplications = tenderService.checkTenderEvalStatus(tenderDTO.getTenderRefNo(), "F");

		} else {
			errorMsg = "Tender Reference No. should be selected.";
			RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	// Search button
	public void getDetailsByReferenceNo() {

		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().isEmpty()
				&& !tenderDTO.getTenderRefNo().equalsIgnoreCase("")) {

			routeNoList = tenderService.getRouteNoList(tenderDTO.getTenderRefNo());
			getSelectedApplcaitions();
		} else {
			errorMsg = "Tender Reference No. should be selected.";
			RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void clear() {
		tenderDTO = new TenderDTO();
		tenderApplicantDTO = new TenderApplicantDTO();
		routeNoList = new ArrayList<TenderApplicantDTO>();
		selectingApplicationList = new ArrayList<TenderApplicantDTO>();
		selectedApplicationList = new ArrayList<TenderApplicantDTO>();
		disableAddApplications = false;
	}

	public void onRouteNoChange() {
		tenderApplicantDTO = tenderService.getDetailsByRouteNo(tenderApplicantDTO);

		if (!tenderDTO.getTenderRefNo().isEmpty() && !tenderDTO.getTenderRefNo().equalsIgnoreCase("")) {

			if (tenderApplicantDTO.getSerialNo() != null && !tenderApplicantDTO.getSerialNo().isEmpty()
					&& !tenderApplicantDTO.getSerialNo().equalsIgnoreCase("")) {

				selectingApplicationList = tenderService.getSelectingApplications(tenderApplicantDTO.getSerialNo(),
						tenderDTO.getTenderRefNo());

				if (selectingApplicationList == null) {
					errorMsg = "There are no applications related to current Route Number.";
					RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Serial No. not found.";
				RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Referance No. not found.";
			RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		getSelectedApplcaitions();

	}

	// Search applications
	public void searchByRouteNo() {

		if (!tenderDTO.getTenderRefNo().isEmpty() && !tenderDTO.getTenderRefNo().equalsIgnoreCase("")) {

			if (tenderApplicantDTO.getSerialNo() != null && !tenderApplicantDTO.getSerialNo().isEmpty()
					&& !tenderApplicantDTO.getSerialNo().equalsIgnoreCase("")) {

				selectingApplicationList = tenderService.getSelectingApplications(tenderApplicantDTO.getSerialNo(),
						tenderDTO.getTenderRefNo());

				if (selectingApplicationList == null) {
					errorMsg = "There are no applications related to current Route Number.";
					RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Serial No. not found.";
				RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Referance No. not found.";
			RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void clearRouteDetails() {
		tenderApplicantDTO = new TenderApplicantDTO();
		clearSelectingApplications();
	}

	public void clearSelectingApplications() {
		tenderApplicantDTO = new TenderApplicantDTO();
		selectingApplicationList = new ArrayList<TenderApplicantDTO>();
	}

	// Add applications button
	public void addApplcations() {

		boolean applicationAdded = false;

		if (getSelectedApplcaitions() && temptSelectedApplicationList.size() > 0) {

			applicationAdded = tenderService.addApplications(temptSelectedApplicationList);

			commonService.updateTenderTaskDetails(tenderDTO.getTenderRefNo(), "TD016", "O");

			if (applicationAdded) {

				RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				getSelectedApplcaitions();

			} else {
				errorMsg = "Applications didn't insert.";
				RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Applications should be selected.";
			RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		getSelectedApplcaitions();
	}

	public boolean getSelectedApplcaitions() {

		boolean validate = true;
		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().isEmpty()
				&& !tenderDTO.getTenderRefNo().equalsIgnoreCase("")) {
			selectedApplicationList = tenderService.getSelectedApplications(tenderDTO.getTenderRefNo());
		} else {
			validate = false;
			errorMsg = "Referance No. not found.";
			RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		return validate;
	}

	public void clearAddedApplications() {
		selectedApplicationList = new ArrayList<TenderApplicantDTO>();
	}

	public void completeTender() {

		boolean completeTrue = false;
		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().isEmpty()
				&& !tenderDTO.getTenderRefNo().equalsIgnoreCase("")) {
			completeTrue = tenderService.completeTender(tenderDTO.getTenderRefNo());
			commonService.updateTenderTaskDetails(tenderDTO.getTenderRefNo(), "TD016", "C");

			disableAddApplications = tenderService.checkTenderEvalStatus(tenderDTO.getTenderRefNo(), "F");

		} else {
			errorMsg = "Tender Reference didn't found.";
			RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		if (completeTrue) {
			RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {
			errorMsg = "Data didn't save.";
			RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void removeSelectedApplication(String applicationNo) {
		boolean removed = false;
		if (applicationNo != null && !applicationNo.isEmpty() && !applicationNo.equalsIgnoreCase("")) {

			removed = tenderService.removeSelectedApplication(applicationNo);

			if (removed) {
				getSelectedApplcaitions();
				successMsg = "Successfully Removed.";
				RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
				RequestContext.getCurrentInstance().update("frmsuccess");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			} else {
				errorMsg = "Data didn't remove.";
				RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Application No. couldn't found.";
			RequestContext.getCurrentInstance().update("frm_TenderEvaluation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	// Getters and setters
	public TenderService getTenderService() {
		return tenderService;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
	}

	public List<String> getReferenceNoList() {
		return referenceNoList;
	}

	public void setReferenceNoList(List<String> referenceNoList) {
		this.referenceNoList = referenceNoList;
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

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
	}

	public TenderApplicantDTO getTenderApplicantDTO() {
		return tenderApplicantDTO;
	}

	public void setTenderApplicantDTO(TenderApplicantDTO tenderApplicantDTO) {
		this.tenderApplicantDTO = tenderApplicantDTO;
	}

	public List<TenderApplicantDTO> getRouteNoList() {
		return routeNoList;
	}

	public void setRouteNoList(List<TenderApplicantDTO> routeNoList) {
		this.routeNoList = routeNoList;
	}

	public List<TenderApplicantDTO> getSelectingApplicationList() {
		return selectingApplicationList;
	}

	public void setSelectingApplicationList(List<TenderApplicantDTO> selectingApplicationList) {
		this.selectingApplicationList = selectingApplicationList;
	}

	public List<TenderApplicantDTO> getSelectedApplicationList() {
		return selectedApplicationList;
	}

	public void setSelectedApplicationList(List<TenderApplicantDTO> selectedApplicationList) {
		this.selectedApplicationList = selectedApplicationList;
	}

	public List<TenderApplicantDTO> getTemptSelectedApplicationList() {
		return temptSelectedApplicationList;
	}

	public void setTemptSelectedApplicationList(List<TenderApplicantDTO> temptSelectedApplicationList) {
		this.temptSelectedApplicationList = temptSelectedApplicationList;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisableAddApplications() {
		return disableAddApplications;
	}

	public void setDisableAddApplications(boolean disableAddApplications) {
		this.disableAddApplications = disableAddApplications;
	}

}
