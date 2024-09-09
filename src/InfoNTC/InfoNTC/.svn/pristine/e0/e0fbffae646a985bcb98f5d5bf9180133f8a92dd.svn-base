package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "serviceRenewalAgreementAuthorizationBackingBean")
@ViewScoped
public class ServiceRenewalAgreementAuthorizationBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// DTO
	private SisuSeriyaDTO agreementRenewalsDTO = new SisuSeriyaDTO();
	private SisuSeriyaDTO selectDTO = new SisuSeriyaDTO();
	private SisuSeriyaDTO viewSelect = new SisuSeriyaDTO();
	private SisuSeriyaDTO schoolInfoDTO = new SisuSeriyaDTO();
	private SisuSeriyaDTO bankInfoDTO = new SisuSeriyaDTO();
	private SisuSeriyaDTO searchedServiceInfoDTO = new SisuSeriyaDTO();
	private SisuSeriyaDTO searchedSchoolInfoDTO = new SisuSeriyaDTO();
	// List
	private List<SisuSeriyaDTO> serviceRefNoList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> serviceAgreementNoList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> languageList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> serviceTypeList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> originTypeList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> destinationTypeList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> provinceList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> districtList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> divisionList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> statusList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> bankNameList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> bankBranchNameList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> dataList = new ArrayList<SisuSeriyaDTO>();

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private int activeTabIndex;
	private String selectedServiceRefNo;
	private String selectedServiceAgreementNo;
	private boolean showDetailsForm = false;
	private String selectedLangauge;
	private boolean disabledCheckby = true;
	private boolean disabledRecommend = true;
	private boolean disabledApprove = true;
	private boolean disabledReject = true;
	private boolean disabledPrintServiceAgreement = true;
	private boolean disabledClear = false;
	private boolean disabledListMenu = true;
	private String errorMsg;
	private String successMsg;
	private String selectedServiceNoInGrid;
	private String selectedRefNoInGrid;
	private String selectedServiceNoForDoc;
	private String selectedRefNoForDoc;
	private CommonService commonService;
	private boolean createMode, viewMode, renderButton;
	private String rejectReason;
	private boolean cancellation;

	// Service classes
	private SisuSariyaService sisuSariyaService;
	private AdminService adminService;
	private DocumentManagementService documentManagementService;

	private StreamedContent files;

	public ServiceRenewalAgreementAuthorizationBackingBean() {

	}

	@PostConstruct
	public void init() {
		agreementRenewalsDTO = new SisuSeriyaDTO();
		activeTabIndex = 0;
		schoolInfoDTO = new SisuSeriyaDTO();
		bankInfoDTO = new SisuSeriyaDTO();
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		loadvalues();

		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN108", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN108", "V");

		if (createMode) {

			renderButton = true;
		}
		serviceAgreementNoList = sisuSariyaService.getServiceNoListForRenewalNew();

	}

	private void loadvalues() {
		dataList = sisuSariyaService.getDefaultValuesForRefNo();
		languageList = sisuSariyaService.getPrefLanguForDropDown();
		bankNameList = sisuSariyaService.getBankList();
		drpdOperatorDepoNameList = sisuSariyaService.getOperatorDepoNameDropDown();
	}

	public void onStartDateChange() {
		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

		agreementRenewalsDTO
				.setRenewalRequestStartDateVal(frm.format(agreementRenewalsDTO.getRenewalRequestStartDateObj()));

		String dateFormat2 = "yyyy-MM-dd";
		//changed date format from  "dd-MM-yyyy" to "yyyy-MM-dd"
		SimpleDateFormat frm2 = new SimpleDateFormat(dateFormat2);
		String requestStartDateStr = null;
		if (agreementRenewalsDTO.getRenewalRequestStartDateObj() != null) {
			requestStartDateStr = frm2.format(agreementRenewalsDTO.getRenewalRequestStartDateObj());
		}
		String requestEndDateStr = null;
		if (agreementRenewalsDTO.getRenewalRequestEndDateObj() != null) {
			requestEndDateStr = frm2.format(agreementRenewalsDTO.getRenewalRequestEndDateObj());
		}
		drpdOperatorDepoNameList = sisuSariyaService.drpdOperatorDepoNameListForRenewal(requestStartDateStr,
				requestEndDateStr);
	}

	public void onEndDateChange() {
		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

		agreementRenewalsDTO
				.setRenewalRequestEndDateVal(frm.format(agreementRenewalsDTO.getRenewalRequestEndDateObj()));
		setDisabledListMenu(false);

		String dateFormat2 = "yyyy-MM-dd";
		//changed date format from  "dd-MM-yyyy" to "yyyy-MM-dd"
		SimpleDateFormat frm2 = new SimpleDateFormat(dateFormat2);
		String requestStartDateStr = null;
		if (agreementRenewalsDTO.getRenewalRequestStartDateObj() != null) {
			requestStartDateStr = frm2.format(agreementRenewalsDTO.getRenewalRequestStartDateObj());
		}
		String requestEndDateStr = null;
		if (agreementRenewalsDTO.getRenewalRequestEndDateObj() != null) {
			requestEndDateStr = frm2.format(agreementRenewalsDTO.getRenewalRequestEndDateObj());
		}

		drpdOperatorDepoNameList = sisuSariyaService.drpdOperatorDepoNameListForRenewal(requestStartDateStr,
				requestEndDateStr);

		serviceRefNoList = sisuSariyaService.getRefNoListForRenewal(requestStartDateStr, requestEndDateStr,
				agreementRenewalsDTO.getNameOfOperator());
		serviceAgreementNoList = sisuSariyaService.getServiceNoListForRenewal(requestStartDateStr, requestEndDateStr,
				agreementRenewalsDTO.getNameOfOperator());
	}

	public void onNameofOperatorChange() {
		String dateFormat2 = "yyyy-MM-dd";
		//changed date format from  "dd-MM-yyyy" to "yyyy-MM-dd"
		SimpleDateFormat frm2 = new SimpleDateFormat(dateFormat2);
		String requestStartDateStr = null;
		if (agreementRenewalsDTO.getRenewalRequestStartDateObj() != null) {
			requestStartDateStr = frm2.format(agreementRenewalsDTO.getRenewalRequestStartDateObj());
		}
		String requestEndDateStr = null;
		if (agreementRenewalsDTO.getRenewalRequestEndDateObj() != null) {
			requestEndDateStr = frm2.format(agreementRenewalsDTO.getRenewalRequestEndDateObj());
		}

		serviceRefNoList = sisuSariyaService.getRefNoListForRenewal(requestStartDateStr, requestEndDateStr,
				agreementRenewalsDTO.getNameOfOperator());
		serviceAgreementNoList = sisuSariyaService.getServiceNoListForRenewal(requestStartDateStr, requestEndDateStr,
				agreementRenewalsDTO.getNameOfOperator());
		setDisabledListMenu(false);
	}

	public void onRefNoChange() {
		setSelectedServiceAgreementNo(sisuSariyaService.getCurrentServiceNo(selectedServiceRefNo));
	}

	public void onServiceNoChange() {
		setSelectedServiceRefNo(sisuSariyaService.getCurrentRefNo(selectedServiceAgreementNo));
	}

	public void onBankBranchesChangeForBanks() {
		if (bankInfoDTO.getBankNameCode().equals("") || bankInfoDTO.getBankNameCode() != null) {
			bankBranchNameList = new ArrayList<SisuSeriyaDTO>();
			bankBranchNameList = sisuSariyaService.getBranchesForBanksList(bankInfoDTO.getBankNameCode());
		} else {
			bankBranchNameList = new ArrayList<SisuSeriyaDTO>();
		}
	}

	public void searchAction() {
		/*if (agreementRenewalsDTO.getRenewalRequestStartDateObj() == null) {
			setErrorMsg("Renewal Request Start Date should be entered.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (agreementRenewalsDTO.getRenewalRequestEndDateObj() == null) {
			setErrorMsg("Renewal Request End Date should be entered.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (selectedServiceRefNo.equals("") || selectedServiceRefNo == null) {
			setErrorMsg("Service Ref No. should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else*/
		
		/**Commented due to request on user wants to search only from service no***/
		if (selectedServiceAgreementNo.equals("") || selectedServiceAgreementNo == null) {

			setErrorMsg("Service No. should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		//else if (agreementRenewalsDTO.getRenewalRequestStartDateObj() != null
			//	&& agreementRenewalsDTO.getRenewalRequestEndDateObj() != null && selectedServiceRefNo != null
			//	&& selectedServiceAgreementNo != null) {
			dataList = new ArrayList<SisuSeriyaDTO>();
			/**dataList = sisuSariyaService.getListForSelectedRefNoOrServiceNo(selectedServiceRefNo,
					selectedServiceAgreementNo);*/
			
			dataList = sisuSariyaService.getListForSelectedServiceAgreementNo(selectedServiceAgreementNo);
		//}
	}

	public void clearAll() {
		agreementRenewalsDTO = new SisuSeriyaDTO();
		selectDTO = new SisuSeriyaDTO();
		setDisabledListMenu(false);
		setSelectedServiceAgreementNo(null);
		setSelectedServiceRefNo(null);
		setDisabledListMenu(true);
		loadvalues();
	}

	public void selectRow(SelectEvent event) {
		agreementRenewalsDTO.setRequestNo(selectDTO.getRequestNo());
		agreementRenewalsDTO.setServiceRefNo(selectDTO.getServiceRefNo());
		agreementRenewalsDTO.setServiceNo(selectDTO.getServiceNo());
		agreementRenewalsDTO.setNameOfOperator(selectDTO.getNameOfOperator());
		agreementRenewalsDTO.setExpiryDateVal(selectDTO.getExpiryDateVal());
		agreementRenewalsDTO.setStatusCode(selectDTO.getStatusCode());
		agreementRenewalsDTO.setNewExpiryDateObj(selectDTO.getNewExpiryDateObj());

		disabledCheckby = true;
		disabledRecommend = true;
		disabledApprove = true;
		disabledReject = true;
		disabledPrintServiceAgreement = true;

		if (selectDTO != null) {
			if (selectDTO.getIsChecked() != null) {
				disabledCheckby = true;
				if (selectDTO.getIsChecked().equals("Y")) {
					if (selectDTO.getIsRecommended() != null) {
						disabledRecommend = true;
						if (selectDTO.getIsRecommended().equals("Y")) {
							if (selectDTO.getStatus() != null) {
								if (selectDTO.getStatus().equals("A")) {
									if (selectDTO.getCancellationStatus() != null
											&& selectDTO.getCancellationStatus().equals("Y")) {
										// approve and reject if cancellation
										disabledApprove = false;
										disabledReject = false;
										disabledPrintServiceAgreement = true;
									} else {
										// enable print if approved
										disabledApprove = true;
										disabledReject = true;
										disabledPrintServiceAgreement = false;
									}
								} else if (selectDTO.getStatus().equals("R")) {
									// enable recommend
									disabledApprove = true;
									disabledReject = true;
									disabledPrintServiceAgreement = true;
								} else if (selectDTO.getStatus().equals("P")) {
									// enable approve and reject if pending
									disabledApprove = false;
									disabledReject = false;
									disabledPrintServiceAgreement = true;
								}
							}
						} else {
							disabledApprove = true;
							disabledReject = true;
						}
					} else {
						disabledRecommend = false;// enable recommend
						disabledReject = false;
					}
				} else {
					disabledRecommend = true;
					disabledReject = true;
				}
			} else {
				disabledCheckby = false;// enable check by
				disabledReject = false;
			}
		}
		disabledClear = false;

	}

	public void viewAction() {
		setShowDetailsForm(true);
		activeTabIndex = 0;
		selectedServiceNoInGrid = viewSelect.getServiceNo();
		selectedRefNoInGrid = viewSelect.getServiceRefNo();
		selectedServiceNoForDoc = viewSelect.getServiceNo();
		selectedRefNoForDoc = viewSelect.getServiceRefNo();

		searchedServiceInfoDTO = sisuSariyaService.getSearchedServiceInfo(viewSelect.getServiceRefNo(),
				viewSelect.getServiceNo(), viewSelect.getRequestNo());
		if (searchedServiceInfoDTO.getCancellationStatus() != null
				&& searchedServiceInfoDTO.getCancellationStatus().equals("Y")) {
			cancellation = true;
		} else {
			cancellation = false;
		}
		setAgreementRenewalsDTO(searchedServiceInfoDTO);
		setSelectedLangauge(searchedServiceInfoDTO.getLanguageCode());
		if (searchedServiceInfoDTO.getProvinceCode() != null) {
			agreementRenewalsDTO.setProvinceDes(
					sisuSariyaService.getProvincesDescription(searchedServiceInfoDTO.getProvinceCode()));
		} else if (searchedServiceInfoDTO.getDistrictCode() != null
				&& searchedServiceInfoDTO.getProvinceCode() != null) {
			agreementRenewalsDTO.setDistrictDes(sisuSariyaService.getDistrictDescription(
					searchedServiceInfoDTO.getDistrictCode(), searchedServiceInfoDTO.getProvinceCode()));
		} else if (searchedServiceInfoDTO.getDistrictCode() != null && searchedServiceInfoDTO.getProvinceCode() != null
				&& searchedServiceInfoDTO.getDivisionalSecCode() != null) {
			agreementRenewalsDTO.setDivisionalSecCode(
					sisuSariyaService.getDivisionSectionDescription(searchedServiceInfoDTO.getDistrictCode(),
							searchedServiceInfoDTO.getProvinceCode(), searchedServiceInfoDTO.getDivisionalSecCode()));
		}

		if (searchedServiceInfoDTO.getServiceTypeCode() != null) {
			agreementRenewalsDTO.setServiceTypeDes(
					sisuSariyaService.getServiceTypeDescription(searchedServiceInfoDTO.getServiceTypeCode()));
		} else {
			agreementRenewalsDTO.setServiceTypeDes("N/A");
		}

		bankInfoDTO.setAccountNo(searchedServiceInfoDTO.getAccountNo());

		if (searchedServiceInfoDTO.getBankNameCode() != null) {
			bankInfoDTO.setBankNameCode(searchedServiceInfoDTO.getBankNameCode());
			bankBranchNameList = new ArrayList<SisuSeriyaDTO>();
			bankBranchNameList = sisuSariyaService.getBranchesForBanksList(searchedServiceInfoDTO.getBankNameCode());
			if (searchedServiceInfoDTO.getBankBranchNameCode().equals("")
					|| searchedServiceInfoDTO.getBankBranchNameCode() != null) {
				bankInfoDTO.setBankBranchNameCode(searchedServiceInfoDTO.getBankBranchNameCode());
			}
		} else {

			bankInfoDTO.setAccountNo(null);
			bankInfoDTO.setBankNameCode(null);
			bankInfoDTO.setBankBranchNameCode(null);
		}

		searchedSchoolInfoDTO = sisuSariyaService.getSearchedSchoolInfo(viewSelect.getServiceRefNo(),
				viewSelect.getServiceNo(), viewSelect.getRequestNo());
		setSchoolInfoDTO(searchedSchoolInfoDTO);
		if (searchedSchoolInfoDTO.getSchoolProvinceCode() != null) {
			schoolInfoDTO.setSchoolProvinceDes(
					sisuSariyaService.getProvincesDescription(searchedSchoolInfoDTO.getSchoolProvinceCode()));
		} else if (searchedSchoolInfoDTO.getSchoolDistrictCode() != null
				&& searchedSchoolInfoDTO.getSchoolProvinceCode() != null) {
			schoolInfoDTO.setSchoolDistrictDes(sisuSariyaService.getDistrictDescription(
					searchedSchoolInfoDTO.getSchoolDistrictCode(), searchedSchoolInfoDTO.getSchoolProvinceCode()));
		} else if (searchedSchoolInfoDTO.getSchoolDistrictCode() != null
				&& searchedSchoolInfoDTO.getSchoolProvinceCode() != null
				&& searchedSchoolInfoDTO.getSchoolDivisinSecCode() != null) {
			schoolInfoDTO.setSchoolDivisionSecDes(sisuSariyaService.getDivisionSectionDescription(
					searchedSchoolInfoDTO.getSchoolDistrictCode(), searchedSchoolInfoDTO.getSchoolProvinceCode(),
					searchedSchoolInfoDTO.getSchoolDivisinSecCode()));
		}
	}

	public void updateBankInfoRecord() {
		if (bankInfoDTO.getAccountNo() != null
				&& (!bankInfoDTO.getBankNameCode().equals("") || bankInfoDTO.getBankNameCode() != null)) {
			int result = sisuSariyaService.updateBankInfoDTO(bankInfoDTO, sessionBackingBean.getLoginUser(),
					selectedServiceNoInGrid, selectedRefNoInGrid);
			if (result == 0) {
				setSuccessMsg("Successfully Saved.");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			} else {
				setErrorMsg("Errors.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			setErrorMsg("Fill mandatory field/s.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void clearBankInfoFields() {
		SisuSeriyaDTO displayValuesInServiceTb = sisuSariyaService.getBankDetDTO(selectedServiceNoInGrid,
				selectedRefNoInGrid);
		bankInfoDTO.setAccountNo(displayValuesInServiceTb.getAccountNo());

		if (displayValuesInServiceTb.getBankNameCode().equals("")
				|| displayValuesInServiceTb.getBankNameCode() != null) {
			bankInfoDTO.setBankNameCode(displayValuesInServiceTb.getBankNameCode());
			bankBranchNameList = new ArrayList<SisuSeriyaDTO>();
			bankBranchNameList = sisuSariyaService.getBranchesForBanksList(displayValuesInServiceTb.getBankNameCode());
			if (displayValuesInServiceTb.getBankBranchNameCode().equals("")
					|| displayValuesInServiceTb.getBankBranchNameCode() != null) {
				bankInfoDTO.setBankBranchNameCode(displayValuesInServiceTb.getBankBranchNameCode());
			}
		}
	}

	public void clearFields() {
		activeTabIndex = 0;
	}

	public void docManagement() {

		try {

			sessionBackingBean.setServiceNoForSisuSariya(selectedServiceNoForDoc);
			sessionBackingBean.setServiceRefNo(selectedRefNoForDoc);

			String requestNo = sisuSariyaService.getRequestNoByServiceNo(selectedRefNoForDoc, selectedServiceNoForDoc);

			sessionBackingBean.setRequestNoForSisuSariya(requestNo);

			sessionBackingBean.setTransactionType("SISU SARIYA");

			mandatoryList = documentManagementService.mandatoryDocsForSisuSariyaAgreementRenewals("18",
					selectedServiceNoForDoc);
			optionalList = documentManagementService.optionalDocsForSisuSariyaAgreementRenewals("18",
					selectedServiceNoForDoc);

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.sisuSariyaMandatoryList(requestNo);
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.sisuSariyaOptionalList(requestNo);

			if (selectedRefNoForDoc != null && requestNo != null) {

				sessionBackingBean.sisuSariyaServiceRefMandatoryDocumentList = documentManagementService
						.sisuSariyaPermitHolderMandatoryList(requestNo, selectedRefNoForDoc);
				sessionBackingBean.sisuSariyaServiceRefOptionalDocumentList = documentManagementService
						.sisuSariyaPermitHolderOptionalList(requestNo, selectedRefNoForDoc);
			}

			if (selectedServiceNoForDoc != null && selectedRefNoForDoc != null && requestNo != null) {

				sessionBackingBean.sisuSariyaServiceMandatoryDocumentList = documentManagementService
						.sisuSariyaAgreementRenewalsMandatoryList(requestNo, selectedRefNoForDoc,
								selectedServiceNoForDoc);
				sessionBackingBean.sisuSariyaServiceOptionalDocumentList = documentManagementService
						.sisuSariyaAgreementRenewalsOptionalList(requestNo, selectedRefNoForDoc,
								selectedServiceNoForDoc);

			}

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkby() {

		if (sisuSariyaService.isServiceDataEnterd(selectDTO) == true) {

			if (sisuSariyaService.isAlreadyChecked(selectDTO) == false) {

				if (sisuSariyaService.isAlreadyRejected(selectDTO) == false) {

					if (sisuSariyaService.checkSisuSariyaRenewal(selectDTO,
							sessionBackingBean.getLoginUser()) == true) {

						setSuccessMsg("Selected record checked successfully.");
						RequestContext.getCurrentInstance().update("successSve");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");

						disabledCheckby = true;
						disabledRecommend = false;
						disabledApprove = true;
						disabledPrintServiceAgreement = true;
						disabledReject = false;

						dataList = new ArrayList<SisuSeriyaDTO>();
						dataList = sisuSariyaService.getDefaultValuesForRefNo();

					} else {
						setErrorMsg("Sisu sariya authorization is failed.");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					setErrorMsg("Selected data already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMsg("Selected data already checked.");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			setErrorMsg("Please update service start / end dates.");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void recommend() {

		if (sisuSariyaService.isServiceDataEnterd(selectDTO) == true) {

			if (sisuSariyaService.isAlreadyRecomended(selectDTO) == false) {

				if (sisuSariyaService.isAlreadyRejected(selectDTO) == false) {

					if (sisuSariyaService.recommendSisuSariyaRenewal(selectDTO,
							sessionBackingBean.getLoginUser()) == true) {

						setSuccessMsg("Selected record recommended successfully.");
						RequestContext.getCurrentInstance().update("successSve");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");

						disabledCheckby = true;
						disabledRecommend = true;
						disabledApprove = false;
						disabledPrintServiceAgreement = true;
						disabledReject = false;

						dataList = new ArrayList<SisuSeriyaDTO>();
						dataList = sisuSariyaService.getDefaultValuesForRefNo();

					} else {
						setErrorMsg("Sisu sariya authorization is failed.");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					setErrorMsg("Selected data already rejected.");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				setErrorMsg("Selected data already recommended.");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			setErrorMsg("Please update service start / end dates.");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void approve() {

		if (selectDTO != null) {
			if (selectDTO.getStatusCode().equals("P")
					|| (selectDTO.getCancellationStatus().equals("Y") && selectDTO.getStatusCode().equals("A"))) {
				try {
					if (selectDTO.getNewExpiryDateObj() != null || selectDTO.getCancellationStatus().equals("Y")) {

						boolean isApprove = false;
						isApprove = sisuSariyaService.renewalApprove(selectDTO, sessionBackingBean.getLoginUser());
						if (isApprove == true) {
							setSuccessMsg("Sisu sariya approval successful.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");

							disabledCheckby = true;
							disabledRecommend = true;
							disabledApprove = true;
							disabledPrintServiceAgreement = false;
							disabledReject = true;

							if (selectDTO.getCancellationStatus() != null
									&& selectDTO.getCancellationStatus().equals("Y")) {
								disabledPrintServiceAgreement = true;
								dataList = new ArrayList<SisuSeriyaDTO>();
								dataList = sisuSariyaService.getDefaultValuesForRefNo();
							} else {
								dataList = sisuSariyaService
										.getListForSelectedRefNoOrServiceNoAfterApproved(selectDTO.getServiceRefNo());
							}

						} else {
							setErrorMsg("Sisu sariya approval fail.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
						}
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

			} else if (selectDTO.getStatusCode().equals("A")) {
				setErrorMsg("Selected sisu sariya is already approved.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (selectDTO.getStatusCode().equals("R")) {
				setErrorMsg("Selected sisu sariya is already rejected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			setErrorMsg("Please select a row.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}
	}

	public void reject() {
		if (sisuSariyaService.isAlreadyRejected(selectDTO) == false) {

			if (sisuSariyaService.isAlreadyApproved(selectDTO) == false) {

				if (selectDTO.getIsChecked() == null) {

					rejectCheckby();
				} else if (selectDTO.getIsRecommended() == null) {

					rejectRecommend();
				} else if (selectDTO.getIsRecommended().equals("Y")) {

					RequestContext.getCurrentInstance().execute("PF('rejectDialog').show()");
				}

			} else {
				setErrorMsg("Selected data already approved.");
				RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
			}
		} else {
			setErrorMsg("Selected data already rejected.");
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
		}
	}

	public void rejectCheckby() {

		if (sisuSariyaService.rejectCheckBySisuSeriyaRenewal(selectDTO, sessionBackingBean.getLoginUser())) {

			setSuccessMsg("Rejected successfully.");
			RequestContext.getCurrentInstance().update("successSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			dataList = new ArrayList<SisuSeriyaDTO>();
			dataList = sisuSariyaService.getDefaultValuesForRefNo();

			disabledCheckby = true;
			disabledRecommend = true;
			disabledApprove = true;
			disabledClear = true;
			disabledPrintServiceAgreement = true;
			disabledReject = true;

		} else {
			setErrorMsg("Rejection failed.");
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
		}

	}

	public void rejectRecommend() {

		if (sisuSariyaService.rejectRecommendSisuSeriyaRenewal(selectDTO, sessionBackingBean.getLoginUser())) {

			setSuccessMsg("Rejected successfully.");
			RequestContext.getCurrentInstance().update("successSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			dataList = new ArrayList<SisuSeriyaDTO>();
			dataList = sisuSariyaService.getDefaultValuesForRefNo();

			disabledCheckby = true;
			disabledRecommend = true;
			disabledApprove = true;
			disabledClear = true;
			disabledPrintServiceAgreement = true;
			disabledReject = true;

		} else {
			setErrorMsg("Rejection failed.");
			RequestContext.getCurrentInstance().execute("PF('frmrequiredField').show()");
		}

	}

	public void rejectApproval() {

		if (selectDTO != null) {
			if (selectDTO.getStatusCode().equals("P")
					|| (selectDTO.getCancellationStatus().equals("Y") && selectDTO.getStatusCode().equals("A"))) {
				if (rejectReason != null) {

					try {

						boolean isReject = false;
						isReject = sisuSariyaService.renewalReject(selectDTO, sessionBackingBean.getLoginUser(),
								rejectReason);
						if (isReject == true) {
							setSuccessMsg("Sisu sariya renewal rejected successfully.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().update("successSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");

							dataList = new ArrayList<SisuSeriyaDTO>();
							dataList = sisuSariyaService.getDefaultValuesForRefNo();

							disabledCheckby = true;
							disabledRecommend = true;
							disabledApprove = true;
							disabledClear = true;
							disabledPrintServiceAgreement = true;
							disabledReject = true;

						} else {
							setErrorMsg("Sisu sariya renewal reject fail.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
						}

					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} else {
					setErrorMsg("Reject Reason should be entered.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else if (selectDTO.getStatusCode().equals("A")) {
				setErrorMsg("Selected permit is already approved.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (selectDTO.getStatusCode().equals("R")) {
				setErrorMsg("Selected permit is already rejected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			setErrorMsg("Please select a row.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}
	}

	public StreamedContent printServiceAgreement() throws JRException {

		if (selectDTO != null) {

			String serviceNo = selectDTO.getServiceNo();
			files = null;
			String sourceFileName = null;

			Connection conn = null;

			try {
				conn = ConnectionManager.getConnection();

				sourceFileName = "..//reports//SisusariyaPrintServiceAggrement.jrxml";
				String logopath = "//lk//informatics//ntc//view//reports//";

				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("P_service_no", serviceNo);
				parameters.put("P_logo", logopath);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf",
						"Service Agreement Sisu Sariya Report_" + serviceNo + ".pdf");

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "pdf");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(conn);
			}

		} else {
			setErrorMsg("Please select a data row.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		return files;

	}

	public void clearTwo() {
		selectDTO = new SisuSeriyaDTO();
		disabledCheckby = true;
		disabledRecommend = true;
		disabledApprove = true;
		disabledPrintServiceAgreement = true;
		disabledReject = true;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public SisuSeriyaDTO getAgreementRenewalsDTO() {
		return agreementRenewalsDTO;
	}

	public void setAgreementRenewalsDTO(SisuSeriyaDTO agreementRenewalsDTO) {
		this.agreementRenewalsDTO = agreementRenewalsDTO;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public List<SisuSeriyaDTO> getServiceRefNoList() {
		return serviceRefNoList;
	}

	public void setServiceRefNoList(List<SisuSeriyaDTO> serviceRefNoList) {
		this.serviceRefNoList = serviceRefNoList;
	}

	public List<SisuSeriyaDTO> getServiceAgreementNoList() {
		return serviceAgreementNoList;
	}

	public void setServiceAgreementNoList(List<SisuSeriyaDTO> serviceAgreementNoList) {
		this.serviceAgreementNoList = serviceAgreementNoList;
	}

	public List<SisuSeriyaDTO> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(List<SisuSeriyaDTO> languageList) {
		this.languageList = languageList;
	}

	public List<SisuSeriyaDTO> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<SisuSeriyaDTO> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public List<SisuSeriyaDTO> getOriginTypeList() {
		return originTypeList;
	}

	public void setOriginTypeList(List<SisuSeriyaDTO> originTypeList) {
		this.originTypeList = originTypeList;
	}

	public List<SisuSeriyaDTO> getDestinationTypeList() {
		return destinationTypeList;
	}

	public void setDestinationTypeList(List<SisuSeriyaDTO> destinationTypeList) {
		this.destinationTypeList = destinationTypeList;
	}

	public List<SisuSeriyaDTO> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<SisuSeriyaDTO> provinceList) {
		this.provinceList = provinceList;
	}

	public List<SisuSeriyaDTO> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<SisuSeriyaDTO> districtList) {
		this.districtList = districtList;
	}

	public List<SisuSeriyaDTO> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<SisuSeriyaDTO> divisionList) {
		this.divisionList = divisionList;
	}

	public List<SisuSeriyaDTO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SisuSeriyaDTO> statusList) {
		this.statusList = statusList;
	}

	public List<SisuSeriyaDTO> getBankNameList() {
		return bankNameList;
	}

	public void setBankNameList(List<SisuSeriyaDTO> bankNameList) {
		this.bankNameList = bankNameList;
	}

	public List<SisuSeriyaDTO> getBankBranchNameList() {
		return bankBranchNameList;
	}

	public void setBankBranchNameList(List<SisuSeriyaDTO> bankBranchNameList) {
		this.bankBranchNameList = bankBranchNameList;
	}

	public String getSelectedServiceRefNo() {
		return selectedServiceRefNo;
	}

	public void setSelectedServiceRefNo(String selectedServiceRefNo) {
		this.selectedServiceRefNo = selectedServiceRefNo;
	}

	public String getSelectedServiceAgreementNo() {
		return selectedServiceAgreementNo;
	}

	public void setSelectedServiceAgreementNo(String selectedServiceAgreementNo) {
		this.selectedServiceAgreementNo = selectedServiceAgreementNo;
	}

	public boolean isShowDetailsForm() {
		return showDetailsForm;
	}

	public void setShowDetailsForm(boolean showDetailsForm) {
		this.showDetailsForm = showDetailsForm;
	}

	public List<SisuSeriyaDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<SisuSeriyaDTO> dataList) {
		this.dataList = dataList;
	}

	public SisuSeriyaDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(SisuSeriyaDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public SisuSeriyaDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(SisuSeriyaDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public SisuSeriyaDTO getSchoolInfoDTO() {
		return schoolInfoDTO;
	}

	public void setSchoolInfoDTO(SisuSeriyaDTO schoolInfoDTO) {
		this.schoolInfoDTO = schoolInfoDTO;
	}

	public SisuSeriyaDTO getBankInfoDTO() {
		return bankInfoDTO;
	}

	public void setBankInfoDTO(SisuSeriyaDTO bankInfoDTO) {
		this.bankInfoDTO = bankInfoDTO;
	}

	public String getSelectedLangauge() {
		return selectedLangauge;
	}

	public void setSelectedLangauge(String selectedLangauge) {
		this.selectedLangauge = selectedLangauge;
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

	public boolean isDisabledPrintServiceAgreement() {
		return disabledPrintServiceAgreement;
	}

	public void setDisabledPrintServiceAgreement(boolean disabledPrintServiceAgreement) {
		this.disabledPrintServiceAgreement = disabledPrintServiceAgreement;
	}

	public boolean isDisabledClear() {
		return disabledClear;
	}

	public void setDisabledClear(boolean disabledClear) {
		this.disabledClear = disabledClear;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

	public boolean isDisabledListMenu() {
		return disabledListMenu;
	}

	public void setDisabledListMenu(boolean disabledListMenu) {
		this.disabledListMenu = disabledListMenu;
	}

	public SisuSeriyaDTO getSearchedServiceInfoDTO() {
		return searchedServiceInfoDTO;
	}

	public void setSearchedServiceInfoDTO(SisuSeriyaDTO searchedServiceInfoDTO) {
		this.searchedServiceInfoDTO = searchedServiceInfoDTO;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public SisuSeriyaDTO getSearchedSchoolInfoDTO() {
		return searchedSchoolInfoDTO;
	}

	public void setSearchedSchoolInfoDTO(SisuSeriyaDTO searchedSchoolInfoDTO) {
		this.searchedSchoolInfoDTO = searchedSchoolInfoDTO;
	}

	public String getSelectedServiceNoForDoc() {
		return selectedServiceNoForDoc;
	}

	public void setSelectedServiceNoForDoc(String selectedServiceNoForDoc) {
		this.selectedServiceNoForDoc = selectedServiceNoForDoc;
	}

	public String getSelectedRefNoForDoc() {
		return selectedRefNoForDoc;
	}

	public void setSelectedRefNoForDoc(String selectedRefNoForDoc) {
		this.selectedRefNoForDoc = selectedRefNoForDoc;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
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

	public String getSelectedServiceNoInGrid() {
		return selectedServiceNoInGrid;
	}

	public void setSelectedServiceNoInGrid(String selectedServiceNoInGrid) {
		this.selectedServiceNoInGrid = selectedServiceNoInGrid;
	}

	public String getSelectedRefNoInGrid() {
		return selectedRefNoInGrid;
	}

	public void setSelectedRefNoInGrid(String selectedRefNoInGrid) {
		this.selectedRefNoInGrid = selectedRefNoInGrid;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isRenderButton() {
		return renderButton;
	}

	public void setRenderButton(boolean renderButton) {
		this.renderButton = renderButton;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<SisuSeriyaDTO> getDrpdOperatorDepoNameList() {
		return drpdOperatorDepoNameList;
	}

	public void setDrpdOperatorDepoNameList(List<SisuSeriyaDTO> drpdOperatorDepoNameList) {
		this.drpdOperatorDepoNameList = drpdOperatorDepoNameList;
	}

	public boolean isDisabledCheckby() {
		return disabledCheckby;
	}

	public void setDisabledCheckby(boolean disabledCheckby) {
		this.disabledCheckby = disabledCheckby;
	}

	public boolean isDisabledRecommend() {
		return disabledRecommend;
	}

	public void setDisabledRecommend(boolean disabledRecommend) {
		this.disabledRecommend = disabledRecommend;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isCancellation() {
		return cancellation;
	}

	public void setCancellation(boolean cancellation) {
		this.cancellation = cancellation;
	}

}
