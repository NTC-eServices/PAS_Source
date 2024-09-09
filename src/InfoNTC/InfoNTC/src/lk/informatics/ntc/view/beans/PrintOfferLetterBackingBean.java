package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "printOfferLetterBean")
@ViewScoped
public class PrintOfferLetterBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private TenderDTO tenderDTO, ajaxDTO, selectDTO;
	private List<TenderDTO> getTenderRefNoList = new ArrayList<TenderDTO>(0);
	private List<TenderDTO> getRouteNoList = new ArrayList<TenderDTO>(0);
	private List<TenderDTO> printList;
	private CommonService commonService;
	private TenderService issueTenderService;
	private String errorMessage, successMessage, alertMSG;
	private boolean disableSecondGrid, disableCancel, disableBoardApprove, disablePrintEnvelope, disablePrintCheckList,
			disablePrintOfferLetter, disableSearchData, disableClearTwo;

	public PrintOfferLetterBackingBean() {
		issueTenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		tenderDTO = new TenderDTO();
		ajaxDTO = new TenderDTO();
		selectDTO = new TenderDTO();
		loadValues();
	}

	public void loadValues() {
		disableSecondGrid = true;
		getTenderRefNoList = issueTenderService.getBoardApprovedTenderRefNoList();
		disableBoardApprove = true;
		disableCancel = true;
		disablePrintCheckList = true;
		disablePrintEnvelope = true;
		disablePrintOfferLetter = true;
		disableSearchData = true;
		disableClearTwo = true;
		printList = new ArrayList<>();
		printList = issueTenderService.getDefaultPrintOfferLetterDetails(tenderDTO);

	}

	public void ajaxFillData() {
		ajaxDTO = issueTenderService.getTenderDetails(tenderDTO);
		tenderDTO.setTenderDes(ajaxDTO.getTenderDes());
	}

	public void ajaxFillRouteData() {
		ajaxDTO = issueTenderService.fillRouteDetails(tenderDTO);
		tenderDTO.setArrival(ajaxDTO.getArrival());
		tenderDTO.setDeparture(ajaxDTO.getDeparture());
		tenderDTO.setSlNumber(ajaxDTO.getSlNumber());
	}

	public void searchRoutes() {
		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {

			getRouteNoList = issueTenderService.getrouteNoFillterdByTenderRefNo(tenderDTO);

			if (getRouteNoList.isEmpty()) {
				setErrorMessage("No Data Found For Selected Tender reference no.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				disableSecondGrid = false;
				disableSearchData = false;
				disableClearTwo = false;
			}

		} else {
			setErrorMessage("Please Select the Tender Ref. No.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void serachData() {
		if (tenderDTO.getRouteNo() != null && !tenderDTO.getRouteNo().trim().equalsIgnoreCase("")) {

			printList = new ArrayList<>();
			printList = issueTenderService.getPrintOfferLetterDetails(tenderDTO);

			if (printList.isEmpty()) {
				setErrorMessage("No Data Found For Selected Route No.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				disableCancel = false;
				disablePrintOfferLetter = false;
			}

		} else {
			setErrorMessage("No Route Data Found.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void selectRow() {

		disableCancel = false;
		disablePrintOfferLetter = false;

		commonService.updateTaskStatus(selectDTO.getTenderAppNo(), "TD017", "PD018", "C",
				sessionBackingBean.getLoginUser());
	}

	public void clearAll() {
		printList = new ArrayList<>();
		printList = issueTenderService.getDefaultPrintOfferLetterDetails(tenderDTO);
		tenderDTO = new TenderDTO();
		selectDTO = new TenderDTO();
		disableBoardApprove = true;
		disableCancel = true;
		disablePrintCheckList = true;
		disablePrintEnvelope = true;
		disablePrintOfferLetter = true;
		disableSecondGrid = true;

	}

	public void clearTwo() {

		tenderDTO.setRouteNo(null);
		tenderDTO.setDeparture(null);
		tenderDTO.setArrival(null);
		tenderDTO.setSlNumber(null);
		printList = new ArrayList<>();
		disableBoardApprove = true;
		disableCancel = true;
		disablePrintCheckList = true;
		disablePrintEnvelope = true;
		disablePrintOfferLetter = true;
		disableSecondGrid = false;

	}

	public void clearThree() {
		printList = new ArrayList<>();
		disableBoardApprove = true;
		disableCancel = true;
		disablePrintCheckList = true;
		disablePrintEnvelope = true;
		disablePrintOfferLetter = true;
		disableSecondGrid = true;

	}

	public void printOfferLetter() {

		if (selectDTO != null) {

			boolean isTenderClosed = issueTenderService.isTenderRefNoClosed(selectDTO);

			if (isTenderClosed == false) {

				int count = issueTenderService.getOfferLetterCount(selectDTO);
				count = count + 1;

				boolean isCountUpdated = issueTenderService.updateOfferLetterCountandApplicationNoStatus(selectDTO,
						count);

				boolean isClosed = issueTenderService.checkingForTenderClosing(selectDTO).isEmpty();

				if (isCountUpdated == true && isClosed == false) {

					setSuccessMessage("Successfully print offer letter");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

					commonService.updateTaskStatusCompletedTenderInSurveyTaskTabel(selectDTO.getTenderRefNo(), "TD018");

					disablePrintCheckList = false;
					disablePrintOfferLetter = true;
					printList = new ArrayList<>();
					printList = issueTenderService.getDefaultPrintOfferLetterDetails(tenderDTO);

				} else if (isCountUpdated == true && isClosed == true) {

					setSuccessMessage(
							"All Application No. Are Completed " + selectDTO.getTenderRefNo() + " is Closed.");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

					commonService.updateTaskStatusCompleted(selectDTO.getTenderAppNo(), "TD018",
							sessionBackingBean.getLoginUser());

					issueTenderService.closeTenderReferenceNo(selectDTO);

					disablePrintCheckList = false;
					disablePrintOfferLetter = true;

				} else {
					setErrorMessage("Offer Letter Printing Is Not Success.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Tender Reference No Is Closed");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select A Row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void printCheckList() {

		disablePrintEnvelope = false;
		disablePrintCheckList = true;

	}

	public void printEnvelope() {

		disableBoardApprove = false;
		disablePrintEnvelope = true;

	}

	public String boardApproval() {
		boolean isTaskCompleteInDetails = false;
		boolean isTaskCompleteInHistory = false;

		if (selectDTO != null) {

			isTaskCompleteInDetails = commonService.checkTaskDetails(selectDTO.getTenderAppNo(), "TD017");
			isTaskCompleteInHistory = commonService.checkTaskHistory(selectDTO.getTenderAppNo(), "TD017");

			if (isTaskCompleteInDetails == true || isTaskCompleteInHistory == true) {

				HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
						.getRequest();
				sessionBackingBean.setPageMode("V");
				sessionBackingBean.setApproveURL(request.getRequestURL().toString());
				sessionBackingBean.setSearchURL(null);
				sessionBackingBean.setApproveURLStatus(true);
				sessionBackingBean.setTenderApplicationNo(selectDTO.getTenderAppNo());
				sessionBackingBean.tempOfferLetterDataList = printList;
				sessionBackingBean.setPrintOffterLetterMode(true);
				return "/pages/surveyManagement/viewCommitteeBoardApproval.xhtml#!";

			} else {

				setAlertMSG("Should be completed Board Approval task.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
				return null;
			}

		} else {

			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			return null;
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
	}

	public List<TenderDTO> getGetTenderRefNoList() {
		return getTenderRefNoList;
	}

	public void setGetTenderRefNoList(List<TenderDTO> getTenderRefNoList) {
		this.getTenderRefNoList = getTenderRefNoList;
	}

	public TenderService getIssueTenderService() {
		return issueTenderService;
	}

	public TenderDTO getAjaxDTO() {
		return ajaxDTO;
	}

	public void setAjaxDTO(TenderDTO ajaxDTO) {
		this.ajaxDTO = ajaxDTO;
	}

	public void setIssueTenderService(TenderService issueTenderService) {
		this.issueTenderService = issueTenderService;
	}

	public List<TenderDTO> getGetRouteNoList() {
		return getRouteNoList;
	}

	public void setGetRouteNoList(List<TenderDTO> getRouteNoList) {
		this.getRouteNoList = getRouteNoList;
	}

	public List<TenderDTO> getPrintList() {
		return printList;
	}

	public void setPrintList(List<TenderDTO> printList) {
		this.printList = printList;
	}

	public TenderDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(TenderDTO selectDTO) {
		this.selectDTO = selectDTO;
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

	public String getAlertMSG() {
		return alertMSG;
	}

	public boolean isDisableCancel() {
		return disableCancel;
	}

	public void setDisableCancel(boolean disableCancel) {
		this.disableCancel = disableCancel;
	}

	public boolean isDisableBoardApprove() {
		return disableBoardApprove;
	}

	public void setDisableBoardApprove(boolean disableBoardApprove) {
		this.disableBoardApprove = disableBoardApprove;
	}

	public boolean isDisablePrintEnvelope() {
		return disablePrintEnvelope;
	}

	public void setDisablePrintEnvelope(boolean disablePrintEnvelope) {
		this.disablePrintEnvelope = disablePrintEnvelope;
	}

	public boolean isDisablePrintCheckList() {
		return disablePrintCheckList;
	}

	public void setDisablePrintCheckList(boolean disablePrintCheckList) {
		this.disablePrintCheckList = disablePrintCheckList;
	}

	public boolean isDisablePrintOfferLetter() {
		return disablePrintOfferLetter;
	}

	public void setDisablePrintOfferLetter(boolean disablePrintOfferLetter) {
		this.disablePrintOfferLetter = disablePrintOfferLetter;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public boolean isDisableSecondGrid() {
		return disableSecondGrid;
	}

	public void setDisableSecondGrid(boolean disableSecondGrid) {
		this.disableSecondGrid = disableSecondGrid;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisableSearchData() {
		return disableSearchData;
	}

	public void setDisableSearchData(boolean disableSearchData) {
		this.disableSearchData = disableSearchData;
	}

	public boolean isDisableClearTwo() {
		return disableClearTwo;
	}

	public void setDisableClearTwo(boolean disableClearTwo) {
		this.disableClearTwo = disableClearTwo;
	}

}
