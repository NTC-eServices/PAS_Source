package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.TenderApplicantDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.ApproveElectedBidderService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "approveElectedBidderBackingBean")
@ViewScoped

public class ApproveElectedBidderBackingBean implements Serializable {
	private static final long serialVersionUID = -6770167572536760677L;

	private CommonService commonService;
	private ApproveElectedBidderService approveElectedBidderService;

	private TenderApplicantDTO tenderApplicantDTO;
	private TenderDTO tenderDTO;

	private List<String> referenceNoList;
	private List<TenderApplicantDTO> routeNoList;
	private List<TenderApplicantDTO> selectingApplicationList;
	private List<TenderApplicantDTO> selectedApplicationList;
	private List<TenderApplicantDTO> temptSelectedApplicationList;

	private String errorMessage, successMessage, infoMessage;
	private boolean disabledBtnsAfterApproved = false;

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

		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object objCallerbackBtn = fcontext.getExternalContext().getSessionMap().get("TENDER_DETAILS_BACK");
		Object objCallerTenderRefNo = fcontext.getExternalContext().getSessionMap().get("TENDER_REF_NO");

		if (objCallerbackBtn != null) {
			String backBtn = (String) objCallerbackBtn;
			if (backBtn != null && !backBtn.isEmpty() && backBtn.equalsIgnoreCase("true")) {
				String strTenderRefNo = (String) objCallerTenderRefNo;
				tenderDTO.setTenderRefNo(strTenderRefNo);
				onReferenceNoChange();
				fcontext.getExternalContext().getSessionMap().put("TENDER_REF_NO", null);
				fcontext.getExternalContext().getSessionMap().put("TRAFFIC_PROPOSAL_NO", null);
				fcontext.getExternalContext().getSessionMap().put("TENDER_DETAILS_BACK", "false");
			}
		}

	}

	private void loadValues() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		approveElectedBidderService = (ApproveElectedBidderService) SpringApplicationContex
				.getBean("approveElectedBidderService");
		referenceNoList = approveElectedBidderService.getReferenceNoList();
	}

	public void onReferenceNoChange() {
		tenderApplicantDTO = new TenderApplicantDTO();
		setDisabledBtnsAfterApproved(false);
		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().trim().isEmpty()) {
			tenderDTO = approveElectedBidderService.getDetailsByReferenceNo(tenderDTO);
			routeNoList = approveElectedBidderService.getRouteNoList(tenderDTO.getTenderRefNo());
			clearSelectingApplications();
			getSelectedApplcaitions();
		} else {
			clear();
		}

	}

	public void clear() {
		tenderDTO = new TenderDTO();
		tenderApplicantDTO = new TenderApplicantDTO();
		routeNoList = new ArrayList<TenderApplicantDTO>();
		selectingApplicationList = new ArrayList<TenderApplicantDTO>();
		selectedApplicationList = new ArrayList<TenderApplicantDTO>();
		setDisabledBtnsAfterApproved(false);
		referenceNoList = new ArrayList<String>();
		referenceNoList = approveElectedBidderService.getReferenceNoList();
	}

	@SuppressWarnings("deprecation")
	public void viewTenderDetails() {
		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {
			String strTrafficProposalNo = approveElectedBidderService
					.getTrafficProposalNoFromTenderRefNo(tenderDTO.getTenderRefNo());
			try {
				FacesContext fcontext = FacesContext.getCurrentInstance();
				fcontext.getExternalContext().getSessionMap().put("TENDER_DETAILS", "true");
				fcontext.getExternalContext().getSessionMap().put("TENDER_REF_NO", tenderDTO.getTenderRefNo());
				fcontext.getExternalContext().getSessionMap().put("TRAFFIC_PROPOSAL_NO", strTrafficProposalNo);

				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/tenderManagement/createTender.xhtml");

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			setErrorMessage("Referance No. should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	@SuppressWarnings("deprecation")
	public void onRouteNoChange() {
		if (tenderApplicantDTO.getRouteSeqNo() > 0) {

			tenderApplicantDTO = approveElectedBidderService.getDetailsByRouteNo(tenderApplicantDTO);

			if (!tenderDTO.getTenderRefNo().isEmpty() && !tenderDTO.getTenderRefNo().equalsIgnoreCase("")) {
				getSelectedApplcaitions();
				if (tenderApplicantDTO.getSerialNo() != null && !tenderApplicantDTO.getSerialNo().isEmpty()
						&& !tenderApplicantDTO.getSerialNo().equalsIgnoreCase("")) {

					selectingApplicationList = approveElectedBidderService
							.getSelectingApplications(tenderApplicantDTO.getSerialNo(), tenderDTO.getTenderRefNo());

					if (!(selectingApplicationList.size() > 0)) {
						clearRouteDetails();
						setInfoMessage("No applications for the selected Route No.");
						RequestContext.getCurrentInstance().update("infoMSG");
						RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
					}

				} else {
					setErrorMessage("Serial No. not found.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Referance No. should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			tenderApplicantDTO = new TenderApplicantDTO();
			selectingApplicationList = new ArrayList<TenderApplicantDTO>();
		}

	}

	public void clearRouteDetails() {
		clearSelectingApplications();
	}

	public void clearSelectingApplications() {
		tenderApplicantDTO = new TenderApplicantDTO();
		selectingApplicationList = new ArrayList<TenderApplicantDTO>();
	}

	// Add applications button
	@SuppressWarnings("deprecation")
	public void addApplcations() {

		boolean applicationAdded = false;

		if (getSelectedApplcaitions() && temptSelectedApplicationList.size() > 0) {

			applicationAdded = approveElectedBidderService.addApplications(temptSelectedApplicationList);

			if (applicationAdded) {
				commonService.updateTenderTaskDetails(tenderDTO.getTenderRefNo(), "TD017", "O");
				selectingApplicationList = approveElectedBidderService
						.getSelectingApplications(tenderApplicantDTO.getSerialNo(), tenderDTO.getTenderRefNo());
				getSelectedApplcaitions();

				setSuccessMessage("Successfully Added.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			} else {
				setErrorMessage("Adding unsuccessful.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setInfoMessage("Applications should be selected.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		}

	}

	// @SuppressWarnings("deprecation")
	public boolean getSelectedApplcaitions() {

		boolean validate = true;
		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().isEmpty()
				&& !tenderDTO.getTenderRefNo().equalsIgnoreCase("")) {
			selectedApplicationList = approveElectedBidderService.getSelectedApplications(tenderDTO.getTenderRefNo());
		} else {
			validate = false;
		}

		return validate;
	}

	@SuppressWarnings("deprecation")
	public void approveElectedBidder() {

		boolean approved = false;
		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().isEmpty()
				&& !tenderDTO.getTenderRefNo().equalsIgnoreCase("")) {
			if (selectedApplicationList.size() > 0) {
				approved = approveElectedBidderService.approveElectedBidder(tenderDTO.getTenderRefNo(),
						sessionBackingBean.getLoginUser());
				if (approved) {
					commonService.updateTenderTaskDetails(tenderDTO.getTenderRefNo(), "TD017", "C");
					setSuccessMessage("Successfully Approved.");
					RequestContext.getCurrentInstance().update("successMSG");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					setDisabledBtnsAfterApproved(true);
				} else {
					setErrorMessage("Approval unsuccessful.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setInfoMessage("At least one Application should be added.");
				RequestContext.getCurrentInstance().update("infoMSG");
				RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
			}
		} else {
			setInfoMessage("Tender Reference no. should be selected.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		}

	}

	@SuppressWarnings("deprecation")
	public void removeSelectedApplication(String applicationNo, String serialNo) {
		boolean removed = false;
		if (applicationNo != null && !applicationNo.isEmpty() && !applicationNo.equalsIgnoreCase("")) {

			removed = approveElectedBidderService.removeSelectedApplication(applicationNo);

			if (removed) {
				getSelectedApplcaitions();
				if (tenderApplicantDTO.getSerialNo() != null && tenderApplicantDTO.getSerialNo().equals(serialNo)) {
					onRouteNoChange();
				}
				setSuccessMessage("Successfully removed.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				setErrorMessage("Remove unsuccessful");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Unable to find the Application No.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	// Getters and setters

	public List<String> getReferenceNoList() {
		return referenceNoList;
	}

	public ApproveElectedBidderService getApproveElectedBidderService() {
		return approveElectedBidderService;
	}

	public void setApproveElectedBidderService(ApproveElectedBidderService approveElectedBidderService) {
		this.approveElectedBidderService = approveElectedBidderService;
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

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisabledBtnsAfterApproved() {
		return disabledBtnsAfterApproved;
	}

	public void setDisabledBtnsAfterApproved(boolean disabledBtnsAfterApproved) {
		this.disabledBtnsAfterApproved = disabledBtnsAfterApproved;
	}

}
