package lk.informatics.ntc.view.beans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
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

@ManagedBean(name = "publishTenderManagmentBackingBean")
@ViewScoped
public class PublishTenderManagmentBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	// services
	private TenderService tenderService;
	private CommonService commonService;

	// DTO

	private TenderDTO tenderDTO;
	private TenderDTO showTenderValues;
	private TenderDTO editValues;

	private List<TenderDTO> tenderRefNoList = new ArrayList<TenderDTO>();
	private List<TenderDTO> tenderDetailList;
	private List<TenderDTO> addGridData;
	private List<TenderDTO> saveDataDB;
	private List<TenderDTO> duplicateRaw;
	private TenderDTO selectList;
	private List<TenderDTO> updateApprove;

	private String errorMessage;
	private String successMessage;
	private String currentDate;

	private String myDate;
	private String loginUser;
	public String selectTenderDate, approveStatus, status;
	private boolean editFlag;

	public boolean isEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}

	private String viewCreateTenderURL;

	public PublishTenderManagmentBackingBean() {
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");

	}

	@PostConstruct
	public void init() {
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		tenderDTO = new TenderDTO();
		tenderRefNoList = tenderService.getTenderRefNoList();

		LocalDate localDate = LocalDate.now();// For reference
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		currentDate = localDate.format(formatter);
		loginUser = sessionBackingBean.getLoginUser();
		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object objCallerTenderRefNo = fcontext.getExternalContext().getSessionMap().get("TENDER_REF_NO");

		if (objCallerTenderRefNo != null) {
			String strTenderRefNo = (String) objCallerTenderRefNo;
			tenderDTO.setTenderRefNo(strTenderRefNo);

			fcontext.getExternalContext().getSessionMap().put("TENDER_REF_NO", null);

		}

	}

	// fill details when enter the tender ref no
	public void fillTenderValues() {

		showTenderValues = tenderService.getTenderDetails(tenderDTO);
		tenderDTO.setTenderDes(showTenderValues.getTenderDes());

		Date pubdate = showTenderValues.getPublishDate();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		tenderDTO.setStringPublishDate(dateFormat.format(pubdate));

		tenderDTO.setStringCloseDate(showTenderValues.getStringCloseDate());

		tenderDTO.setTime(showTenderValues.getTime());

		tenderDTO.setIniCloseDate(showTenderValues.getIniCloseDate());

	}

	public void showSavedData() {

		addGridData = tenderService.showDataToGrid(tenderDTO);
	}

	public void addAction() throws ParseException {
		if (editFlag) {

			tenderService.updateEditData(selectList.getNewTenderDateString(), selectList.getPostPoneReason(), tenderDTO,
					loginUser);
			selectList = new TenderDTO();
			showData();
			tenderDTO.setPostPoneReason("");
			Date dateClear = tenderDTO.getNewTenderDate();
			dateClear = null;

		} else {
			status = tenderService.getApproveAgainstTenderRefNo(tenderDTO.getTenderRefNo());

			if (status == null || !status.equals("A")) {
				if (status == null || !status.equals("R")) {
					LocalDate localDate = LocalDate.now();// For reference
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					myDate = localDate.format(formatter);

					String closeDate = null;

					closeDate = tenderDTO.getIniCloseDate();

					if (tenderDTO.getPostPoneReason() != null && !tenderDTO.getPostPoneReason().trim().isEmpty()) {

						if (tenderDTO.getNewTenderDate() != null) {
							Date newTenderDate = tenderDTO.getNewTenderDate();
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							String strDate = dateFormat.format(newTenderDate);

							if (strDate.compareTo(tenderDTO.getStringPublishDate()) > 0) {
								if (strDate.compareTo(closeDate) < 0) {

									addGridData = new ArrayList<TenderDTO>();
									saveDataDB = tenderService.updatePostponeReason(strDate, tenderDTO, loginUser);
									tenderService.updateDates(tenderDTO, loginUser);
									showData();

									commonService.updateTaskStatusTenderInSurveyTaskTabel(tenderDTO.getTenderRefNo(),
											"TD003", "TD004", "C", sessionBackingBean.getLoginUser());
									setSuccessMessage("Successfully saved.");
									RequestContext.getCurrentInstance().update("successMSG");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								} else {

									setErrorMessage("Please select  valid New Tender Date.");
									RequestContext.getCurrentInstance().update("errorMSG");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
							} else {

								setErrorMessage("Please select  valid New Tender Date.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							setErrorMessage("Please select New Tender Date.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {

						setErrorMessage("Please Enter Postpone Reason.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("This Tender Reference No. can not postpone");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else if (status == null || !status.equals("R")) {
				if (status == null || !status.equals("A")) {
					LocalDate localDate = LocalDate.now();// For reference
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					myDate = localDate.format(formatter);

					String closeDate = null;

					closeDate = tenderDTO.getIniCloseDate();

					if (tenderDTO.getPostPoneReason() != null && !tenderDTO.getPostPoneReason().trim().isEmpty()) {

						if (tenderDTO.getNewTenderDate() != null) {
							Date newTenderDate = tenderDTO.getNewTenderDate();
							DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							String strDate = dateFormat.format(newTenderDate);

							if (strDate.compareTo(tenderDTO.getStringPublishDate()) > 0
									&& strDate.compareTo(closeDate) < 0) {

								addGridData = new ArrayList<TenderDTO>();
								saveDataDB = tenderService.updatePostponeReason(strDate, tenderDTO, loginUser);
								tenderService.updateDates(tenderDTO, loginUser);
								showData();

								commonService.updateTaskStatusTenderInSurveyTaskTabel(tenderDTO.getTenderRefNo(),
										"TD003", "TD004", "C", sessionBackingBean.getLoginUser());

								setSuccessMessage("Successfully saved.");
								RequestContext.getCurrentInstance().update("successMSG");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							} else {

								setErrorMessage("Please select  valid New Tender Date.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							setErrorMessage("Please select New Tender Date.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {

						setErrorMessage("Please Enter Postpone Reason.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("This Tender Reference No. can not postpone");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else if (status.equals("A")) {
				setErrorMessage("This Tender Reference No. can not postpone");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

			else if (status.equals("R")) {
				setErrorMessage("This Tender Reference No. can not postpone");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}
		}

	}

	public void showData() {
		addGridData = tenderService.showDataToGrid(tenderDTO);
	}

	public void clearAddDetails() {

		tenderDTO = new TenderDTO();
		selectTenderDate = null;

	}

	public void viewPage() {

		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {

			tenderDetailList = new ArrayList<>();
			tenderDetailList = tenderService.getDetails_tender_details(tenderDTO.getTenderRefNo());

			RequestContext.getCurrentInstance().execute("PF('tenderDetails').show()");

		} else {
			setErrorMessage("No Data Found For Selected Tender");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearSearch() {

		tenderDTO = new TenderDTO();
		selectTenderDate = null;

	}

	public void clearPage() {

		tenderDTO = new TenderDTO();
		addGridData = new ArrayList<TenderDTO>();
	}

	public void fillRouteDet(String routeNo)

	{

	}

	public void searchDetails() {

	}

	public void addButton() {
	}

	public void saveButton() {
	}

	public void clearSurDet() {

	}

	public void editButton() throws ParseException {

		status = tenderService.getApproveAgainstTenderRefNo(tenderDTO.getTenderRefNo());
		if (status == null || !status.equals("A")) {
			if (status == null || !status.equals("R")) {

				tenderDTO.setPostPoneReason(selectList.getPostPoneReason());

				String newTenDate = selectList.getNewTenderDateString();
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(newTenDate);
				tenderDTO.setNewTenderDate(date1);

				editFlag = true;
			} else {
				setErrorMessage("This Reference No. can not edit.(already rejected)");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else if (status == null || !status.equals("R")) {
			if (status == null || !status.equals("A")) {

				tenderDTO.setPostPoneReason(selectList.getPostPoneReason());

				String newTenDate = selectList.getNewTenderDateString();
				Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(newTenDate);
				tenderDTO.setNewTenderDate(date1);

				editFlag = true;
			} else {
				setErrorMessage("This Reference No. can not edit.(already approved)");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}

	}

	public String editAdButton() {

		String Url = null;
		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {

			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("TENDER_REF_NO", tenderDTO.getTenderRefNo());

			String viewedTenderRefNo = tenderDTO.getTenderRefNo();

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			viewCreateTenderURL = request.getRequestURL().toString();
			sessionBackingBean.setViewCreateTenderURL(viewCreateTenderURL);
			sessionBackingBean.setViewCreateTenderURLStatus(true);
			sessionBackingBean.setViewedTenderRefNo(viewedTenderRefNo);

			Url = "/pages/tenderManagement/tenderAdvertisement.xhtml";
		}
		return Url;

	}

	public String finalAdForPubButton() {

		String Url = null;
		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {

			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("TENDER_REF_NO", tenderDTO.getTenderRefNo());

			String viewedTenderRefNo = tenderDTO.getTenderRefNo();

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			viewCreateTenderURL = request.getRequestURL().toString();
			sessionBackingBean.setViewCreateTenderURL(viewCreateTenderURL);
			sessionBackingBean.setViewCreateTenderURLStatus(true);
			sessionBackingBean.setViewedTenderRefNo(viewedTenderRefNo);

			Url = "/pages/tenderManagement/approveAdvertisement.xhtml";
		}
		return Url;

	}

	public void approveButton() {

		status = tenderService.getApproveAgainstTenderRefNo(tenderDTO.getTenderRefNo());
		approveStatus = tenderService.getApproveRejectStatus(tenderDTO.getTenderRefNo(),
				selectList.getNewTenderDateString());
		if (status == null || status.equals("P")) {
			updateApprove = tenderService.updateApprovedData(tenderDTO, "A", selectList.getNewTenderDateString(),
					loginUser);

			addGridData = tenderService.showDataToGrid(tenderDTO);

			commonService.updateTaskStatusCompletedTenderInSurveyTaskTabel(tenderDTO.getTenderRefNo(), "TD004");
			setSuccessMessage("Successfully approved.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else if (status.equals("A")) {
			setErrorMessage("Already approved");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (status.equals("R")) {
			setErrorMessage("Already rejected");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (approveStatus.equals("A")) {

			setErrorMessage("Already approved");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else if (approveStatus.equals("R")) {
			setErrorMessage("Already rejected");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
	}

	public void rejectButton() {

		status = tenderService.getApproveAgainstTenderRefNo(tenderDTO.getTenderRefNo());
		approveStatus = tenderService.getApproveRejectStatus(tenderDTO.getTenderRefNo(),
				selectList.getNewTenderDateString());
		if (status == null || status.equals("P")) {
			updateApprove = tenderService.updateApprovedData(tenderDTO, "R", selectList.getNewTenderDateString(),
					loginUser);
			addGridData = tenderService.showDataToGrid(tenderDTO);

			setSuccessMessage("Successfully rejected.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else if (status.equals("A")) {
			setErrorMessage("Already approved");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (status.equals("R")) {
			setErrorMessage("Already rejected");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (approveStatus.equals("A")) {

			setErrorMessage("Already approved");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else if (approveStatus.equals("R")) {
			setErrorMessage("Already rejected");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
	}

	public TenderService getTenderService() {
		return tenderService;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
	}

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
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

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<TenderDTO> getTenderRefNoList() {
		return tenderRefNoList;
	}

	public void setTenderRefNoList(List<TenderDTO> tenderRefNoList) {
		this.tenderRefNoList = tenderRefNoList;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public TenderDTO getShowTenderValues() {
		return showTenderValues;
	}

	public void setShowTenderValues(TenderDTO showTenderValues) {
		this.showTenderValues = showTenderValues;
	}

	public List<TenderDTO> getAddGridData() {
		return addGridData;
	}

	public void setAddGridData(List<TenderDTO> addGridData) {
		this.addGridData = addGridData;
	}

	public List<TenderDTO> getSaveDataDB() {
		return saveDataDB;
	}

	public void setSaveDataDB(List<TenderDTO> saveDataDB) {
		this.saveDataDB = saveDataDB;
	}

	public TenderDTO getSelectList() {
		return selectList;
	}

	public void setSelectList(TenderDTO selectList) {
		this.selectList = selectList;
	}

	public String getViewCreateTenderURL() {
		return viewCreateTenderURL;
	}

	public void setViewCreateTenderURL(String viewCreateTenderURL) {
		this.viewCreateTenderURL = viewCreateTenderURL;
	}

	public String getSelectTenderDate() {
		return selectTenderDate;
	}

	public void setSelectTenderDate(String selectTenderDate) {
		this.selectTenderDate = selectTenderDate;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public List<TenderDTO> getUpdateApprove() {
		return updateApprove;
	}

	public void setUpdateApprove(List<TenderDTO> updateApprove) {
		this.updateApprove = updateApprove;
	}

	public List<TenderDTO> getDuplicateRaw() {
		return duplicateRaw;
	}

	public void setDuplicateRaw(List<TenderDTO> duplicateRaw) {
		this.duplicateRaw = duplicateRaw;
	}

	public List<TenderDTO> getTenderDetailList() {
		return tenderDetailList;
	}

	public void setTenderDetailList(List<TenderDTO> tenderDetailList) {
		this.tenderDetailList = tenderDetailList;
	}

}
