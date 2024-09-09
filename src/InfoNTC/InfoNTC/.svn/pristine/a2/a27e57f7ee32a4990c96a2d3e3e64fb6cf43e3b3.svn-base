package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "createTenderBackingBean")
@ViewScoped
public class CreateTenderBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private TenderDTO tenderDTO, selectDTO, advertisementObjectDTO, addDTO, viewSelect;
	public TenderService tenderService;
	public DocumentManagementService documentManagementService;
	private List<TenderDTO> getTrafficProposalList = new ArrayList<TenderDTO>(0);
	private Date closeDate, closeTime;
	private List<TenderDTO> routeDetailList, advertisementList;
	private String errorMessage, successMessage, alertMSG, tenderRefNo, tenderReferenceNo, result = null;;
	private int slNo;
	private CommonService commonService;
	private boolean disabledAdd, disabledClearTwo, disabledDoc, disabledCancel, disabledSave, isEditAction;
	private boolean backBtnRender, viewMode = false;

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	public CreateTenderBackingBean() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		tenderDTO = new TenderDTO();
		viewSelect = new TenderDTO();
		advertisementList = new ArrayList<>();
		advertisementObjectDTO = new TenderDTO();
		loadValues();

		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object objCallerbackBtn = fcontext.getExternalContext().getSessionMap().get("TENDER_DETAILS");
		Object objCallerTenderRefNo = fcontext.getExternalContext().getSessionMap().get("TENDER_REF_NO");
		Object objCallerTrafficProposalNo = fcontext.getExternalContext().getSessionMap().get("TRAFFIC_PROPOSAL_NO");
		backBtnRender = false;
		viewMode = false;
		if (objCallerbackBtn != null) {
			String backBtn = (String) objCallerbackBtn;
			if (backBtn != null && !backBtn.isEmpty() && backBtn.equalsIgnoreCase("true")) {
				backBtnRender = true;
				String strTenderRefNo = (String) objCallerTenderRefNo;
				String strTrafficProposalNo = (String) objCallerTrafficProposalNo;
				tenderDTO.setTenderRefNo(strTenderRefNo);
				tenderDTO.setTrafficProposalNo(strTrafficProposalNo);
				search();
				TenderDTO tempDTO = tenderService.getDetails_tender_management(strTenderRefNo);
				tenderDTO.setTenClosingDate(tempDTO.getTenClosingDate());
				tenderDTO.setTenClosingTime(tempDTO.getTenClosingTime());
				tenderDTO.setTenderDes(tempDTO.getTenderDes());
				advertisementList = tenderService.getDetails_tender_details(strTenderRefNo);
				viewMode = true;
			}
		}
		fcontext.getExternalContext().getSessionMap().put("TENDER_DETAILS", "false");
	}

	public void loadValues() {
		getTrafficProposalList = tenderService.getTrafficProposeNoList();
		disabledAdd = true;
		disabledClearTwo = true;
		disabledDoc = true;
		disabledCancel = true;
		disabledSave = true;

	}

	public void search() {

		if (tenderDTO.getTrafficProposalNo() != null && !tenderDTO.getTrafficProposalNo().trim().equalsIgnoreCase("")) {

			String surveyNo = tenderService.getSurveyNo(tenderDTO);

			if (surveyNo != null) {

				tenderDTO.setSurveyNo(surveyNo);

				routeDetailList = new ArrayList<TenderDTO>();
				routeDetailList = tenderService.getRouteDetails(tenderDTO);

				if (routeDetailList.isEmpty()) {
					setErrorMessage("No Data Found For Selected Traffic Proposal No.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {

					disabledAdd = false;
					disabledClearTwo = false;
				}

			} else {
				setErrorMessage("Selected Reference No. Does Not Have a Survey No.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {

			setErrorMessage("Please Select the Traffic Proposal No.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void selectRow() {
		String strAppNo = "";

		tenderDTO.setRouteNo(selectDTO.getRouteNo());
		tenderDTO.setVia(selectDTO.getVia());
		tenderDTO.setTypeOfServices(selectDTO.getTypeOfServices());
		tenderDTO.setOneWayBusFare(selectDTO.getOneWayBusFare());
		tenderDTO.setEffectiveRoutes(selectDTO.getEffectiveRoutes());
		advertisementObjectDTO.setParallelRoads(null);
		advertisementObjectDTO.setTurnsPerDay(0);
		advertisementObjectDTO.setTreasureHolderPrice(null);

	}

	public void addTender() {

		if (selectDTO != null || isEditAction == true) {

			if (tenderDTO.getRouteNo() != null && !tenderDTO.getRouteNo().trim().equalsIgnoreCase("")) {

				if (advertisementObjectDTO.getTurnsPerDay() != 0) {

					if (advertisementObjectDTO.getTreasureHolderPrice() != null) {

						if (advertisementObjectDTO.getParallelRoads() != null
								&& !advertisementObjectDTO.getParallelRoads().trim().equalsIgnoreCase("")) {

							if (isEditAction == false) {

								advertisementObjectDTO.setRouteNo(selectDTO.getRouteNo());
								advertisementObjectDTO.setVia(selectDTO.getVia());
								advertisementObjectDTO.setTypeOfServices(selectDTO.getTypeOfServices());
								advertisementObjectDTO.setOneWayBusFare(selectDTO.getOneWayBusFare());
								advertisementObjectDTO.setDeparture(selectDTO.getDeparture());
								advertisementObjectDTO.setArrival(selectDTO.getArrival());
								advertisementObjectDTO.setEffectiveRoutes(selectDTO.getEffectiveRoutes());
								advertisementObjectDTO.setNoOfPermits(selectDTO.getNoOfPermits());

								if (this.result != null) {

									String number = result.substring(3, 6);
									int returncountvalue = Integer.valueOf(number) + 1;

									String ApprecordcountN = "";

									if (returncountvalue >= 1 && returncountvalue < 10) {
										ApprecordcountN = "00" + returncountvalue;
									} else if (returncountvalue >= 10 && returncountvalue < 100) {
										ApprecordcountN = "0" + returncountvalue;
									} else {
										ApprecordcountN = "00" + returncountvalue;
									}
									result = "SLN" + ApprecordcountN;
								} else {
									result = "SLN" + "001";
								}

								addDTO = new TenderDTO(result, advertisementObjectDTO.getRouteNo(),
										advertisementObjectDTO.getVia(), advertisementObjectDTO.getTypeOfServices(),
										advertisementObjectDTO.getOneWayBusFare(),
										advertisementObjectDTO.getTurnsPerDay(),
										advertisementObjectDTO.getTreasureHolderPrice(),
										advertisementObjectDTO.getParallelRoads(),
										advertisementObjectDTO.getEffectiveRoutes(),
										advertisementObjectDTO.getNoOfPermits(), advertisementObjectDTO.getDeparture(),
										advertisementObjectDTO.getArrival());

								boolean isfound = false;

								for (int i = 0; i < advertisementList.size(); i++) {
									if (advertisementList.get(i).getRouteNo().equals(addDTO.getRouteNo())) {
										isfound = true;
										break;
									} else {
										isfound = false;
									}
								}

								if (isfound == false) {
									advertisementList.add(addDTO);
									tenderDTO.setRouteNo(null);
									tenderDTO.setTypeOfServices(null);
									tenderDTO.setOneWayBusFare(null);
									tenderDTO.setVia(null);
									tenderDTO.setEffectiveRoutes(null);
									advertisementObjectDTO = new TenderDTO();
									disabledDoc = false;
									disabledCancel = false;
									disabledSave = false;

								} else {

									String number = result.substring(3, 6);
									int returncountvalue = Integer.valueOf(number) - 1;

									String ApprecordcountN = "";

									if (returncountvalue >= 1 && returncountvalue < 10) {
										ApprecordcountN = "00" + returncountvalue;
									} else if (returncountvalue >= 10 && returncountvalue < 100) {
										ApprecordcountN = "0" + returncountvalue;
									} else {
										ApprecordcountN = "00" + returncountvalue;
									}
									result = "SLN" + ApprecordcountN;

									setErrorMessage("Selected Route Data Already Added.");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}

							} else {

								addDTO = new TenderDTO(tenderDTO.getSlNumber(), tenderDTO.getRouteNo(),
										tenderDTO.getVia(), tenderDTO.getTypeOfServices(), tenderDTO.getOneWayBusFare(),
										advertisementObjectDTO.getTurnsPerDay(),
										advertisementObjectDTO.getTreasureHolderPrice(),
										advertisementObjectDTO.getParallelRoads(),
										advertisementObjectDTO.getEffectiveRoutes(), tenderDTO.getNoOfPermits(),
										tenderDTO.getDeparture(), tenderDTO.getArrival());

								advertisementList.remove(viewSelect);
								advertisementList.add(addDTO);

								tenderDTO.setRouteNo(null);
								tenderDTO.setTypeOfServices(null);
								tenderDTO.setOneWayBusFare(null);
								tenderDTO.setVia(null);
								tenderDTO.setEffectiveRoutes(null);
								advertisementObjectDTO = new TenderDTO();
								disabledDoc = false;
								disabledCancel = false;
								disabledSave = false;
								isEditAction = false;

							}

						} else {
							setErrorMessage("Please Enter Parallel Roads.");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Please Enter Treasure holder price.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please Enter No. of Turns Per Bus Per Day.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("No Data Found To Add");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void save() {

		String logedUser = sessionBackingBean.getLoginUser();

		if (!advertisementList.isEmpty()) {

			if (closeDate != null) {

				if (closeTime != null) {

					if (tenderDTO.getTenderDes() != null && !tenderDTO.getTenderDes().trim().equalsIgnoreCase("")) {

						tenderRefNo = tenderService.generateReferenceNo();

						Timestamp timestamp = new Timestamp(closeDate.getTime());
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						String strDate = dateFormat.format(closeDate.getTime());
						tenderDTO.setCloseDate(timestamp);

						SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
						String time = localDateFormat.format(closeTime);

						tenderDTO.setTime(time);
						tenderDTO.setTenderRefNo(tenderRefNo);

						boolean isTenderManagementDataSave = tenderService.insertINTOTenderManagement(tenderDTO,
								logedUser, strDate);
						boolean isTenderDetailsDataSave = tenderService.insertINTOTenderDetails(tenderDTO, logedUser,
								advertisementList);

						if (isTenderManagementDataSave == true && isTenderDetailsDataSave == true) {

							setSuccessMessage("Data Saved Sucessfully.");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

							this.result = null;
							tenderService.updateTrafficProposalNo(tenderDTO, sessionBackingBean.getLoginUser());
							setTenderReferenceNo(tenderDTO.getTenderRefNo());
							RequestContext.getCurrentInstance().update("tenderRef");

							boolean isTasKTD001Available = tenderService.checkTaskDetailsInSurvey(tenderDTO, "TD001",
									"C");
							getTrafficProposalList = tenderService.getTrafficProposeNoList();

							if (isTasKTD001Available == false) {

								tenderService.insertTaskDetailsSurvey(tenderDTO, logedUser, "TD001", "C");

							} else {

							}

						} else {
							setErrorMessage("Data Saving Not Successful");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Please Enter the Tender Description");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please Enter the Tender Close Time");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please Enter the Tender Close Date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("No Data to Save.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void editAction() {

		tenderDTO.setSlNumber(viewSelect.getSlNumber());
		tenderDTO.setRouteNo(viewSelect.getRouteNo());
		tenderDTO.setTypeOfServices(viewSelect.getTypeOfServices());
		tenderDTO.setOneWayBusFare(viewSelect.getOneWayBusFare());
		tenderDTO.setVia(viewSelect.getVia());
		tenderDTO.setNoOfPermits(viewSelect.getNoOfPermits());
		tenderDTO.setDeparture(viewSelect.getDeparture());
		tenderDTO.setArrival(viewSelect.getArrival());
		tenderDTO.setEffectiveRoutes(viewSelect.getEffectiveRoutes());
		isEditAction = true;

	}

	public void clearOne() {

		disabledAdd = true;
		disabledClearTwo = true;
		disabledSave = true;
		disabledDoc = true;
		disabledCancel = true;
		closeDate = null;
		closeTime = null;
		tenderDTO = new TenderDTO();
		advertisementObjectDTO = new TenderDTO();
		advertisementList = new ArrayList<>();
		routeDetailList = new ArrayList<>();
		setTenderRefNo(null);
		getTrafficProposalList = tenderService.getTrafficProposeNoList();
		slNo = 0;
		result = null;
		isEditAction = false;

	}

	public void clearTwo() {
		tenderDTO.setRouteNo(null);
		tenderDTO.setVia(null);
		tenderDTO.setTypeOfServices(null);
		tenderDTO.setOneWayBusFare(null);
		tenderDTO.setEffectiveRoutes(null);
		advertisementObjectDTO = new TenderDTO();
	}

	public void clearThree() {

		advertisementList = new ArrayList<>();

	}

	public void uploadDoc() {

		DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
		try {

			sessionBackingBean.setPermitRenewalPermitNo(tenderRefNo);

			sessionBackingBean.setPermitRenewalTranstractionTypeDescription("TENDER");

			String applicationNo = tenderService.getApplicationNoFromRefNo(tenderRefNo);

			sessionBackingBean.setApplicationNoForDoc(applicationNo);

			uploaddocumentManagementDTO.setUpload_Permit(tenderRefNo);
			uploaddocumentManagementDTO.setTransaction_Type("TENDER");

			mandatoryList = documentManagementService.mandatoryDocs("01", tenderRefNo);
			optionalList = documentManagementService.optionalDocs("01", tenderRefNo);

			sessionBackingBean.optionalList = documentManagementService.optionalDocs("01", tenderRefNo);

			sessionBackingBean.newPermitMandatoryDocumentList = documentManagementService
					.newPermitMandatoryList(tenderRefNo);
			sessionBackingBean.newPermitOptionalDocumentList = documentManagementService
					.newPermitOptionalList(tenderRefNo);
			sessionBackingBean.permitRenewalMandatoryDocumentList = documentManagementService
					.permitRenewalMandatoryList(tenderRefNo);
			sessionBackingBean.permitRenewalOptionalDocumentList = documentManagementService
					.permitRenewalOptionalList(tenderRefNo);
			sessionBackingBean.backlogManagementOptionalDocumentList = documentManagementService
					.backlogManagementOptionalList(tenderRefNo);

			sessionBackingBean.amendmentToBusOwnerMandatoryDocumentList = documentManagementService
					.amendmentToBusOwnerMandatoryList(tenderRefNo);
			sessionBackingBean.amendmentToBusOwnerOptionalDocumentList = documentManagementService
					.amendmentToBusOwnerOptionalList(tenderRefNo);
			sessionBackingBean.amendmentToBusMandatoryDocumentList = documentManagementService
					.amendmentToBusMandatoryList(tenderRefNo);
			sessionBackingBean.amendmentToBusOptionalDocumentList = documentManagementService
					.amendmentToBusOptionalList(tenderRefNo);
			sessionBackingBean.amendmentToOwnerBusMandatoryDocumentList = documentManagementService
					.amendmentToOwnerBusMandatoryList(tenderRefNo);
			sessionBackingBean.amendmentToOwnerBusOptionalDocumentList = documentManagementService
					.amendmentToOwnerBusOptionalList(tenderRefNo);
			sessionBackingBean.amendmentToServiceBusMandatoryDocumentList = documentManagementService
					.amendmentToServiceBusMandatoryList(tenderRefNo);
			sessionBackingBean.amendmentToServiceBusOptionalDocumentList = documentManagementService
					.amendmentToServiceBusOptionalList(tenderRefNo);
			sessionBackingBean.amendmentToServiceMandatoryDocumentList = documentManagementService
					.amendmentToServiceMandatoryList(tenderRefNo);
			sessionBackingBean.amendmentToServiceOptionalDocumentList = documentManagementService
					.amendmentToServiceOptionalList(tenderRefNo);

			sessionBackingBean.tenderMandatoryDocumentList = documentManagementService.tenderMandatoryList(tenderRefNo);
			sessionBackingBean.tenderOptionalDocumentList = documentManagementService.tenderOptionalList(tenderRefNo);
			RequestContext.getCurrentInstance().execute("PF('uploadTenderDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void backButonAction() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TENDER_DETAILS_BACK", "true");
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/tenderManagement/approveElectedBidder.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TenderService getTenderService() {
		return tenderService;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
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

	public List<TenderDTO> getGetTrafficProposalList() {
		return getTrafficProposalList;
	}

	public void setGetTrafficProposalList(List<TenderDTO> getTrafficProposalList) {
		this.getTrafficProposalList = getTrafficProposalList;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public List<TenderDTO> getRouteDetailList() {
		return routeDetailList;
	}

	public void setRouteDetailList(List<TenderDTO> routeDetailList) {
		this.routeDetailList = routeDetailList;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public String getTenderRefNo() {
		return tenderRefNo;
	}

	public void setTenderRefNo(String tenderRefNo) {
		this.tenderRefNo = tenderRefNo;
	}

	public String getTenderReferenceNo() {
		return tenderReferenceNo;
	}

	public void setTenderReferenceNo(String tenderReferenceNo) {
		this.tenderReferenceNo = tenderReferenceNo;
	}

	public TenderDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(TenderDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public List<TenderDTO> getAdvertisementList() {
		return advertisementList;
	}

	public void setAdvertisementList(List<TenderDTO> advertisementList) {
		this.advertisementList = advertisementList;
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

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public TenderDTO getAdvertisementObjectDTO() {
		return advertisementObjectDTO;
	}

	public void setAdvertisementObjectDTO(TenderDTO advertisementObjectDTO) {
		this.advertisementObjectDTO = advertisementObjectDTO;
	}

	public TenderDTO getAddDTO() {
		return addDTO;
	}

	public void setAddDTO(TenderDTO addDTO) {
		this.addDTO = addDTO;
	}

	public int getSlNo() {
		return slNo;
	}

	public void setSlNo(int slNo) {
		this.slNo = slNo;
	}

	public boolean isDisabledAdd() {
		return disabledAdd;
	}

	public void setDisabledAdd(boolean disabledAdd) {
		this.disabledAdd = disabledAdd;
	}

	public boolean isDisabledClearTwo() {
		return disabledClearTwo;
	}

	public void setDisabledClearTwo(boolean disabledClearTwo) {
		this.disabledClearTwo = disabledClearTwo;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisabledDoc() {
		return disabledDoc;
	}

	public void setDisabledDoc(boolean disabledDoc) {
		this.disabledDoc = disabledDoc;
	}

	public boolean isDisabledCancel() {
		return disabledCancel;
	}

	public void setDisabledCancel(boolean disabledCancel) {
		this.disabledCancel = disabledCancel;
	}

	public boolean isDisabledSave() {
		return disabledSave;
	}

	public void setDisabledSave(boolean disabledSave) {
		this.disabledSave = disabledSave;
	}

	public TenderDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(TenderDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<DocumentManagementDTO> getMandatoryList() {
		return mandatoryList;
	}

	public void setMandatoryList(List<DocumentManagementDTO> mandatoryList) {
		this.mandatoryList = mandatoryList;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public boolean isBackBtnRender() {
		return backBtnRender;
	}

	public void setBackBtnRender(boolean backBtnRender) {
		this.backBtnRender = backBtnRender;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

}
