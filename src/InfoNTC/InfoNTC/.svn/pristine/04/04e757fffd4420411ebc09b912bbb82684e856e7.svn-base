package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.NisiSeriyaDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.NisiSeriyaService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "nisiSeriyaServiceConfirmationBean")
@ViewScoped
public class NisiSeriyaServiceConfirmationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	/** nisi seriya variables **/
	private NisiSeriyaService nisiSeriyaService;

	private boolean genReNumDisable;
	private boolean disableAddBtn;
	private boolean disableDataTable;
	private List<String> requestNumList;
	private List<String> serviceRefNumList;
	private List<String> permitNumList;
	private List<String> serviceAgreementNumList;
	private String requestedNo;
	private String serviceRefNo;
	private String permitNo;
	private String serviceAgreementNo;
	private boolean searchEditDisable;
	private NisiSeriyaDTO nisiSeriyaDTO;
	private List<RouteDTO> originList;
	private List<RouteDTO> destinationList;
	private List<RouteDTO> viaList;
	private List<NisiSeriyaDTO> nisiSeriyaDTOList;
	private int activeTabIndex;
	private boolean view;
	private boolean disableTab01;
	private boolean disableTab02;
	private boolean disableBankInfo;
	private boolean renderClear;
	private String alertMsg;
	private List<CommonDTO> drpdServiceTypeList;
	private List<SisuSeriyaDTO> drpdOriginList;
	private List<SisuSeriyaDTO> drpdDestinationList;
	private List<SisuSeriyaDTO> drpdViaList;
	private List<CommonDTO> drpdProvincelList;
	private List<CommonDTO> drpdDistrictList;
	private List<CommonDTO> drpdDevsecList;
	private List<GenerateReceiptDTO> drpdBankList;
	private List<GenerateReceiptDTO> drpdBankBranchList;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	private List<NisiSeriyaDTO> selectedNisiSeriyaDTOList;
	private AdminService adminService;
	private SurveyService surveyService;
	private DocumentManagementService documentManagementService;;
	private CommonService commonService;
	private String errorMsg;
	String dateFormat = "dd/MM/yyyy";
	SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
	private boolean renderBtnSearch;
	private boolean serviceBtnsDisable;
	private boolean serviceApprRejDisable;
	private String rejectReason;
	private boolean printserviceAggrDisable;

	@PostConstruct
	public void init() {

		/** nisi seriya start **/
		nisiSeriyaService = (NisiSeriyaService) SpringApplicationContex.getBean("nisiSeriyaService");
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		setRequestNumList(new ArrayList<String>());
		serviceRefNumList = new ArrayList<String>();
		permitNumList = new ArrayList<String>();
		serviceAgreementNumList = new ArrayList<String>();
		setRequestNumList(nisiSeriyaService.retrieveRequestNumbers());
		nisiSeriyaDTO = new NisiSeriyaDTO();
		requestedNo = null;
		serviceRefNo = null;
		permitNo = null;
		serviceAgreementNo = null;
		searchEditDisable = false;
		view = true;
		disableBankInfo = true;
		serviceBtnsDisable = false;
		serviceApprRejDisable = false;
		errorMsg = null;
		rejectReason = null;
		printserviceAggrDisable = false;
		genReNumDisable = false;
		originList = new ArrayList<RouteDTO>();
		originList = nisiSeriyaService.retrieveOriginList();
		destinationList = new ArrayList<RouteDTO>();
		viaList = new ArrayList<RouteDTO>();
		nisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
		selectedNisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
		setDisableAddBtn(false);
		disableTab02 = true;
		setDisableDataTable(false);
		renderClear = true;

		adminService = (AdminService) SpringApplicationContex.getBean("adminService");

		drpdOriginList = new ArrayList<SisuSeriyaDTO>();
		drpdDestinationList = new ArrayList<SisuSeriyaDTO>();
		drpdBankList = new ArrayList<GenerateReceiptDTO>();
		drpdBankBranchList = new ArrayList<GenerateReceiptDTO>();
		drpdViaList = new ArrayList<SisuSeriyaDTO>();

		drpdServiceTypeList = adminService.getServiceTypeToDropdown();
		drpdProvincelList = adminService.getProvinceToDropdown();
		drpdDevsecList = adminService.getDivSecToDropdown();
		drpdDistrictList = adminService.getDistrictToDropdown();
		drpdBankList = surveyService.getBankListDropDown();

		if (sessionBackingBean.isSisuSariya() == true) {

			renderClear = false;
			sessionBackingBean.setSisuSariya(false);
			renderBtnSearch = true;
		}
	}

	/** nisi seriya edit methods start **/
	public void onRequestNumChange() {
		serviceRefNumList = new ArrayList<String>();
		serviceRefNumList = nisiSeriyaService.retrieveServiceRefNumbers(requestedNo, permitNo, serviceAgreementNo);

		permitNumList = new ArrayList<String>();
		permitNumList = nisiSeriyaService.retrievePermitNumbers(requestedNo, serviceRefNo, serviceAgreementNo);

		serviceAgreementNumList = new ArrayList<String>();
		serviceAgreementNumList = nisiSeriyaService.retrieveServiceAgreementNumbers(requestedNo, serviceRefNo,
				permitNo);

		searchEditDisable = true;

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
	}

	public void onServiceRefNumChange() {

		permitNumList = new ArrayList<String>();
		permitNumList = nisiSeriyaService.retrievePermitNumbers(requestedNo, serviceRefNo, serviceAgreementNo);

		serviceAgreementNumList = new ArrayList<String>();
		serviceAgreementNumList = nisiSeriyaService.retrieveServiceAgreementNumbers(requestedNo, serviceRefNo,
				permitNo);

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
	}

	public void onPermitNumChange() {
		serviceRefNumList = new ArrayList<String>();
		serviceRefNumList = nisiSeriyaService.retrieveServiceRefNumbers(requestedNo, permitNo, serviceAgreementNo);

		serviceAgreementNumList = new ArrayList<String>();
		serviceAgreementNumList = nisiSeriyaService.retrieveServiceAgreementNumbers(requestedNo, serviceRefNo,
				permitNo);

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
	}

	public void onServiceAgreeNumChange() {
		serviceRefNumList = new ArrayList<String>();
		serviceRefNumList = nisiSeriyaService.retrieveServiceRefNumbers(requestedNo, permitNo, serviceAgreementNo);

		permitNumList = new ArrayList<String>();
		permitNumList = nisiSeriyaService.retrievePermitNumbers(requestedNo, serviceRefNo, serviceAgreementNo);

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
	}

	public void btnSearch() {

		disableTab02 = false;
		disableDataTable = true;
		serviceBtnsDisable = true;
		serviceApprRejDisable = false;
		printserviceAggrDisable = false;
		nisiSeriyaDTOList = nisiSeriyaService.searchDataFromNt_t_task_det(requestedNo, permitNo, serviceAgreementNo,
				serviceRefNo);

		List<NisiSeriyaDTO> serviceConfirmedList = new ArrayList<NisiSeriyaDTO>();
		for (NisiSeriyaDTO dto : nisiSeriyaDTOList) {
			boolean exit = nisiSeriyaService.checkServiceApproved(dto);
			if (exit) {
				serviceConfirmedList.add(dto);
			}
		}

		if (serviceConfirmedList.size() == nisiSeriyaDTOList.size()) {
			serviceBtnsDisable = true;
			serviceApprRejDisable = true;
			printserviceAggrDisable = false;

		}

		int approveCount = nisiSeriyaDTOList.get(nisiSeriyaDTOList.size() - 1).getApproveCount();
		if (approveCount == nisiSeriyaDTOList.size()) {
			serviceBtnsDisable = false;
			serviceApprRejDisable = false;
			printserviceAggrDisable = true;
		}

		RequestContext.getCurrentInstance().update("btnBackSisuSariyaAuth");

	}

	public void serviceInfoConfirmationAction() {
		boolean allGenerated = false;

		List<String> confirmRejectList = new ArrayList<String>();

		if (selectedNisiSeriyaDTOList != null && selectedNisiSeriyaDTOList.size() != 0) {
			serviceApprRejDisable = true;

			for (NisiSeriyaDTO dto : selectedNisiSeriyaDTOList) {

				/** check already confirmed or rejected start **/
				boolean confirmRejectDone = nisiSeriyaService.checkServiceConfirmationAndRejectStatus(dto);
				if (confirmRejectDone) {
					confirmRejectList.add(dto.getServiceRefNo());
				} else {
					/** check already confirmed or rejected end **/

					if (dto.getServiceAgreementNo() == null || dto.getServiceAgreementNo().isEmpty()
							|| dto.getServiceAgreementNo().trim().equalsIgnoreCase("")) {
						nisiSeriyaService.generateServiceAgreementNumber(dto.getServiceRefNo(),
								sessionBackingBean.getLoginUser());
						String prevTaskCode = commonService.selectPreviousTaskCodeSubsidyTaskTabel(dto.getRequestNo(),
								dto.getServiceAgreementNo(), dto.getServiceRefNo());
						commonService.updateTaskStatusSubsidyTaskTabel(dto.getRequestNo(), null, dto.getServiceRefNo(),
								prevTaskCode, "NS002", "O", sessionBackingBean.getLoginUser());
						allGenerated = true;
					}
				}
			}

			if (!allGenerated) {
				sessionBackingBean.setMessage("Selected Record/s are already confirmed");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			} else {
				if (confirmRejectList.size() != 0) {
					String numbers = null;
					for (String s : confirmRejectList) {
						if (numbers != null && !numbers.isEmpty() && !numbers.trim().equalsIgnoreCase("")) {
							numbers = numbers + " , " + s;
						} else {
							numbers = s;
						}
					}
					sessionBackingBean.setMessage("Selected Record/s are already Confirmed or Rejected");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				} else {
					sessionBackingBean.setMessage("Records confirmed successfully");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");
				}
			}

			selectedNisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
			nisiSeriyaDTO = new NisiSeriyaDTO();
			nisiSeriyaDTOList = nisiSeriyaService.searchDataFromNt_t_task_det(requestedNo, permitNo, serviceAgreementNo,
					serviceRefNo);
			serviceAgreementNumList = nisiSeriyaService.retrieveServiceAgreementNumbers(requestedNo, serviceRefNo,
					permitNo);
			serviceBtnsDisable = true;
			serviceApprRejDisable = true;
			printserviceAggrDisable = false;

			int approveCount = nisiSeriyaDTOList.get(nisiSeriyaDTOList.size() - 1).getApproveCount();
			if (approveCount == nisiSeriyaDTOList.size()) {
				serviceBtnsDisable = false;
				serviceApprRejDisable = false;
				printserviceAggrDisable = true;
			}

			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");

		} else {

			sessionBackingBean.setMessage("Please select a record to confirm");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
		}

	}

	public void serviceInfoRejectConfirmAction() {

		if (selectedNisiSeriyaDTOList != null && selectedNisiSeriyaDTOList.size() != 0) {
			List<String> confirmRejectList = new ArrayList<String>();
			// before asking too many confirmations check whether selected
			// records are
			// already rejected
			for (NisiSeriyaDTO dto : selectedNisiSeriyaDTOList) {

				/** check already confirmed or rejected start **/
				boolean confirmRejectDone = nisiSeriyaService.checkServiceConfirmationAndRejectStatus(dto);
				if (confirmRejectDone) {
					confirmRejectList.add(dto.getServiceRefNo());
				}
				/** check already confirmed or rejected end **/
			}

			if (confirmRejectList.size() == selectedNisiSeriyaDTOList.size()) {
				sessionBackingBean.setMessage("Selected Record/s are already Confirmed or Rejected");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				serviceBtnsDisable = true;
				serviceApprRejDisable = false;
				printserviceAggrDisable = false;

				int approveCount = nisiSeriyaDTOList.get(nisiSeriyaDTOList.size() - 1).getApproveCount();
				if (approveCount == nisiSeriyaDTOList.size()) {
					serviceBtnsDisable = false;
					serviceApprRejDisable = false;
					printserviceAggrDisable = true;
				}

				RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");

				return;
			}
			// before asking too many confirmations check whether selected
			// records are
			// already rejected

			RequestContext.getCurrentInstance().execute("PF('dlgRejectConfirm').show()");

		} else {

			sessionBackingBean.setMessage("Please select a record to reject");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
		}
	}

	public void serviceInfoRejectAction() {

		List<String> confirmRejectList = new ArrayList<String>();

		if (selectedNisiSeriyaDTOList != null && selectedNisiSeriyaDTOList.size() != 0) {
			if (rejectReason != null && !rejectReason.isEmpty() && !rejectReason.trim().equalsIgnoreCase("")) {

				for (NisiSeriyaDTO dto : selectedNisiSeriyaDTOList) {

					/** check already confirmed or rejected start **/
					boolean confirmRejectDone = nisiSeriyaService.checkServiceConfirmationAndRejectStatus(dto);
					if (confirmRejectDone) {
						confirmRejectList.add(dto.getServiceRefNo());
					} else {
						/** check already confirmed or rejected end **/
						nisiSeriyaService.serviceConfirmationReject(dto.getServiceRefNo(), rejectReason,
								sessionBackingBean.getLoginUser(), dto.getServiceAgreementNo());
						String prevTaskCode = commonService.selectPreviousTaskCodeSubsidyTaskTabel(dto.getRequestNo(),
								dto.getServiceAgreementNo(), dto.getServiceRefNo());
						commonService.updateTaskStatusSubsidyTaskTabel(dto.getRequestNo(), null, dto.getServiceRefNo(),
								prevTaskCode, "NS002", "O", sessionBackingBean.getLoginUser());
					}
				}

				if (confirmRejectList.size() != 0) {
					String numbers = null;
					for (String s : confirmRejectList) {
						if (numbers != null && !numbers.isEmpty() && !numbers.trim().equalsIgnoreCase("")) {
							numbers = numbers + " , " + s;
						} else {
							numbers = s;
						}
					}
					sessionBackingBean.setMessage("Selected Record/s are already Confirmed or Rejected");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				} else {
					sessionBackingBean.setMessage("Record/s rejected successfully");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");
				}
				rejectReason = null;
				selectedNisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
				nisiSeriyaDTO = new NisiSeriyaDTO();
				nisiSeriyaDTOList = nisiSeriyaService.searchDataFromNt_t_task_det(requestedNo, permitNo,
						serviceAgreementNo, serviceRefNo);
				serviceAgreementNumList = nisiSeriyaService.retrieveServiceAgreementNumbers(requestedNo, serviceRefNo,
						permitNo);
				serviceBtnsDisable = true;
				serviceApprRejDisable = false;
				printserviceAggrDisable = false;

				int approveCount = nisiSeriyaDTOList.get(nisiSeriyaDTOList.size() - 1).getApproveCount();
				if (approveCount == nisiSeriyaDTOList.size()) {
					serviceBtnsDisable = false;
					serviceApprRejDisable = false;
					printserviceAggrDisable = true;
				}

				RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");

			} else {
				sessionBackingBean.setMessage("Please enter a reject reason");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
			}
		} else {
			sessionBackingBean.setMessage("Please select a record to reject");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
		}

	}

	public void viewEditDataListener(NisiSeriyaDTO dTO) {

		nisiSeriyaDTO = dTO;
		originList = nisiSeriyaService.retrieveOriginList();
		destinationList = nisiSeriyaService.retrieveDestinationList(nisiSeriyaDTO.getOriginCode());
		viaList = nisiSeriyaService.retrieveViaList(nisiSeriyaDTO.getOriginCode(), nisiSeriyaDTO.getDestinationCode());
		drpdBankBranchList = nisiSeriyaService.getBankBranchDropDown(nisiSeriyaDTO.getBankNameCode());
		view = true;
		disableBankInfo = true;
		RequestContext.getCurrentInstance().update("btnBackSisuSariyaAuth");
	}

	public void serviceApproveAction() {

		List<String> confirmRejectList = new ArrayList<String>();

		if (selectedNisiSeriyaDTOList != null && selectedNisiSeriyaDTOList.size() != 0) {
			serviceApprRejDisable = true;

			for (NisiSeriyaDTO dto : selectedNisiSeriyaDTOList) {

				boolean confirmRejectDone = nisiSeriyaService.checkServiceApproveAndRejectStatus(dto);
				if (confirmRejectDone) {
					confirmRejectList.add(dto.getServiceRefNo());
				} else {
					if (dto.getServiceAgreementNo() != null && !dto.getServiceAgreementNo().isEmpty()
							&& !dto.getServiceAgreementNo().trim().equalsIgnoreCase("")) {
						nisiSeriyaService.approveServiceConfirmation(dto.getServiceRefNo(),
								sessionBackingBean.getLoginUser());
						String prevTaskCode = commonService.selectPreviousTaskCodeSubsidyTaskTabel(dto.getRequestNo(),
								dto.getServiceAgreementNo(), dto.getServiceRefNo());
						commonService.updateTaskStatusSubsidyTaskTabel(dto.getRequestNo(), null, dto.getServiceRefNo(),
								prevTaskCode, "NS003", "O", sessionBackingBean.getLoginUser());
					} else {
						sessionBackingBean.setMessage("Data cannot be saved without Service Agreement No.");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
						return;
					}
				}
			}

			if (confirmRejectList.size() != 0) {
				String numbers = null;
				for (String s : confirmRejectList) {
					if (numbers != null && !numbers.isEmpty() && !numbers.trim().equalsIgnoreCase("")) {
						numbers = numbers + " , " + s;
					} else {
						numbers = s;
					}
				}
				sessionBackingBean.setMessage("Selected Record/s are already Approved or Rejected");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			} else {
				sessionBackingBean.setMessage("Record/s approved successfully");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");
			}

			selectedNisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
			nisiSeriyaDTO = new NisiSeriyaDTO();
			nisiSeriyaDTOList = nisiSeriyaService.searchDataFromNt_t_task_det(requestedNo, permitNo, serviceAgreementNo,
					serviceRefNo);
			serviceAgreementNumList = nisiSeriyaService.retrieveServiceAgreementNumbers(requestedNo, serviceRefNo,
					permitNo);
			serviceBtnsDisable = false;
			serviceApprRejDisable = false;
			printserviceAggrDisable = true;

			int approveCount = nisiSeriyaDTOList.get(nisiSeriyaDTOList.size() - 1).getApproveCount();
			if (approveCount == nisiSeriyaDTOList.size()) {
				serviceBtnsDisable = false;
				serviceApprRejDisable = false;
				printserviceAggrDisable = true;
			}

			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");

		} else {

			sessionBackingBean.setMessage("Please select a record/s to approve");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
		}

	}

	public void confirmRejectAction() {

		if (selectedNisiSeriyaDTOList != null && selectedNisiSeriyaDTOList.size() != 0) {

			RequestContext.getCurrentInstance().execute("PF('dlgReject').show()");

		} else {

			sessionBackingBean.setMessage("Please select a record to reject");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
		}
	}

	public void serviceRejectAction() {

		List<String> confirmRejectList = new ArrayList<String>();

		if (selectedNisiSeriyaDTOList != null && selectedNisiSeriyaDTOList.size() != 0) {
			serviceApprRejDisable = true;

			for (NisiSeriyaDTO dto : selectedNisiSeriyaDTOList) {

				boolean confirmRejectDone = nisiSeriyaService.checkServiceApproveAndRejectStatus(dto);
				if (confirmRejectDone) {
					confirmRejectList.add(dto.getServiceRefNo());
				} else {
					if (dto.getServiceAgreementNo() != null && !dto.getServiceAgreementNo().isEmpty()
							&& !dto.getServiceAgreementNo().trim().equalsIgnoreCase("")) {

						nisiSeriyaService.rejectServiceConfirmation(dto.getServiceRefNo(),
								sessionBackingBean.getLoginUser());
						String prevTaskCode = commonService.selectPreviousTaskCodeSubsidyTaskTabel(dto.getRequestNo(),
								dto.getServiceAgreementNo(), dto.getServiceRefNo());
						commonService.updateTaskStatusSubsidyTaskTabel(dto.getRequestNo(), null, dto.getServiceRefNo(),
								prevTaskCode, "NS003", "O", sessionBackingBean.getLoginUser());
					} else {
						sessionBackingBean.setMessage("Data cannot be saved without Service Agreement No.");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
					}
				}
			}

			if (confirmRejectList.size() != 0) {

				String numbers = null;
				for (String s : confirmRejectList) {
					if (numbers != null && !numbers.isEmpty() && !numbers.trim().equalsIgnoreCase("")) {
						numbers = numbers + " , " + s;
					} else {
						numbers = s;
					}
				}
				sessionBackingBean.setMessage("Selected Record/s are already Approved or Rejected");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");

			} else {
				sessionBackingBean.setMessage("Record/s rejected successfully");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");
			}

			selectedNisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
			nisiSeriyaDTO = new NisiSeriyaDTO();
			nisiSeriyaDTOList = nisiSeriyaService.searchDataFromNt_t_task_det(requestedNo, permitNo, serviceAgreementNo,
					serviceRefNo);
			serviceAgreementNumList = nisiSeriyaService.retrieveServiceAgreementNumbers(requestedNo, serviceRefNo,
					permitNo);
			serviceBtnsDisable = false;
			serviceApprRejDisable = false;
			printserviceAggrDisable = false;

			int approveCount = nisiSeriyaDTOList.get(nisiSeriyaDTOList.size() - 1).getApproveCount();
			if (approveCount == nisiSeriyaDTOList.size()) {
				serviceBtnsDisable = false;
				serviceApprRejDisable = false;
				printserviceAggrDisable = true;
			}

			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");

		} else {

			sessionBackingBean.setMessage("Please select a record/s to reject");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
		}

	}

	public void btnClear() {

		serviceRefNumList = new ArrayList<String>();
		permitNumList = new ArrayList<String>();
		serviceAgreementNumList = new ArrayList<String>();
		searchEditDisable = false;
		requestedNo = null;
		serviceRefNo = null;
		permitNo = null;
		serviceAgreementNo = null;
		view = true;
		disableBankInfo = true;
		nisiSeriyaDTO = new NisiSeriyaDTO();
		nisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
		originList = new ArrayList<RouteDTO>();
		destinationList = new ArrayList<RouteDTO>();
		viaList = new ArrayList<RouteDTO>();
		selectedNisiSeriyaDTOList = new ArrayList<NisiSeriyaDTO>();
		serviceBtnsDisable = false;
		serviceApprRejDisable = false;
		printserviceAggrDisable = false;
		rejectReason = null;

		requestNumList = new ArrayList<String>();
		requestNumList = nisiSeriyaService.retrieveRequestNumbers();

		RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");

	}

	/** nisi seriya edit methods end **/

	public void onOriginChange() {

		if (nisiSeriyaDTO.getOriginCode() != null && !nisiSeriyaDTO.getOriginCode().isEmpty()
				&& !nisiSeriyaDTO.getOriginCode().equals("")) {

			destinationList = nisiSeriyaService.retrieveDestinationList(nisiSeriyaDTO.getOriginCode());

		}
	}

	public void onDestinationChange() {

		if (nisiSeriyaDTO.getDestinationCode() != null && !nisiSeriyaDTO.getDestinationCode().isEmpty()
				&& !nisiSeriyaDTO.getDestinationCode().equals("") && nisiSeriyaDTO.getOriginCode() != null
				&& !nisiSeriyaDTO.getOriginCode().isEmpty() && !nisiSeriyaDTO.getOriginCode().equals("")) {

			viaList = nisiSeriyaService.retrieveViaList(nisiSeriyaDTO.getOriginCode(),
					nisiSeriyaDTO.getDestinationCode());
		}
	}

	public void onProvinceChange() {

		drpdDistrictList = adminService.getDistrictByProvinceToDropdown(nisiSeriyaDTO.getProvinceCode());

	}

	public void onDistrictChange() {
		drpdDevsecList = adminService.getDivSecByDistrictToDropdown(nisiSeriyaDTO.getDistrictCode());
	}

	// tab 01 end

	// tab 02 start
	public void onBankChange() {
		drpdBankBranchList = nisiSeriyaService.getBankBranchDropDown(nisiSeriyaDTO.getBankNameCode());
	}

	public void saveBtnActionForBankInfo() {

		boolean added = nisiSeriyaService.insertBankDetails(nisiSeriyaDTO, sessionBackingBean.loginUser);

		if (added) {

			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {

			errorMsg = "Bank details cannot be addded";
			RequestContext.getCurrentInstance().update("nisiSeriyaMainFrm");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clearBankInformationAction() {
		nisiSeriyaDTO.setAccountNo(null);
		nisiSeriyaDTO.setBankNameCode(null);
		nisiSeriyaDTO.setBankBranchNameCode(null);

	}
	// tab 02 end

	public void documentManagement() {
	}

	/** nisi seriya methods end **/

	// getters and setters
	public List<CommonDTO> getDrpdProvincelList() {
		return drpdProvincelList;
	}

	public void setDrpdProvincelList(List<CommonDTO> drpdProvincelList) {
		this.drpdProvincelList = drpdProvincelList;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
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

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public List<SisuSeriyaDTO> getDrpdOriginList() {
		return drpdOriginList;
	}

	public void setDrpdOriginList(List<SisuSeriyaDTO> drpdOriginList) {
		this.drpdOriginList = drpdOriginList;
	}

	public List<SisuSeriyaDTO> getDrpdDestinationList() {
		return drpdDestinationList;
	}

	public void setDrpdDestinationList(List<SisuSeriyaDTO> drpdDestinationList) {
		this.drpdDestinationList = drpdDestinationList;
	}

	public List<CommonDTO> getDrpdDistrictList() {
		return drpdDistrictList;
	}

	public void setDrpdDistrictList(List<CommonDTO> drpdDistrictList) {
		this.drpdDistrictList = drpdDistrictList;
	}

	public List<CommonDTO> getDrpdDevsecList() {
		return drpdDevsecList;
	}

	public void setDrpdDevsecList(List<CommonDTO> drpdDevsecList) {
		this.drpdDevsecList = drpdDevsecList;
	}

	public List<GenerateReceiptDTO> getDrpdBankList() {
		return drpdBankList;
	}

	public void setDrpdBankList(List<GenerateReceiptDTO> drpdBankList) {
		this.drpdBankList = drpdBankList;
	}

	public List<GenerateReceiptDTO> getDrpdBankBranchList() {
		return drpdBankBranchList;
	}

	public void setDrpdBankBranchList(List<GenerateReceiptDTO> drpdBankBranchList) {
		this.drpdBankBranchList = drpdBankBranchList;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public List<CommonDTO> getDrpdServiceTypeList() {
		return drpdServiceTypeList;
	}

	public void setDrpdServiceTypeList(List<CommonDTO> drpdServiceTypeList) {
		this.drpdServiceTypeList = drpdServiceTypeList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public SimpleDateFormat getFrm() {
		return frm;
	}

	public void setFrm(SimpleDateFormat frm) {
		this.frm = frm;
	}

	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
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

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public boolean isRenderClear() {
		return renderClear;
	}

	public void setRenderClear(boolean renderClear) {
		this.renderClear = renderClear;
	}

	public boolean isDisableTab01() {
		return disableTab01;
	}

	public void setDisableTab01(boolean disableTab01) {
		this.disableTab01 = disableTab01;
	}

	public boolean isDisableTab02() {
		return disableTab02;
	}

	public void setDisableTab02(boolean disableTab02) {
		this.disableTab02 = disableTab02;
	}

	public boolean isDisableBankInfo() {
		return disableBankInfo;
	}

	public void setDisableBankInfo(boolean disableBankInfo) {
		this.disableBankInfo = disableBankInfo;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public List<SisuSeriyaDTO> getDrpdViaList() {
		return drpdViaList;
	}

	public void setDrpdViaList(List<SisuSeriyaDTO> drpdViaList) {
		this.drpdViaList = drpdViaList;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isRenderBtnSearch() {
		return renderBtnSearch;
	}

	public void setRenderBtnSearch(boolean renderBtnSearch) {
		this.renderBtnSearch = renderBtnSearch;
	}

	public NisiSeriyaDTO getNisiSeriyaDTO() {
		return nisiSeriyaDTO;
	}

	public void setNisiSeriyaDTO(NisiSeriyaDTO nisiSeriyaDTO) {
		this.nisiSeriyaDTO = nisiSeriyaDTO;
	}

	public String getRequestedNo() {
		return requestedNo;
	}

	public void setRequestedNo(String requestedNo) {
		this.requestedNo = requestedNo;
	}

	public boolean isGenReNumDisable() {
		return genReNumDisable;
	}

	public void setGenReNumDisable(boolean genReNumDisable) {
		this.genReNumDisable = genReNumDisable;
	}

	public NisiSeriyaService getNisiSeriyaService() {
		return nisiSeriyaService;
	}

	public void setNisiSeriyaService(NisiSeriyaService nisiSeriyaService) {
		this.nisiSeriyaService = nisiSeriyaService;
	}

	public List<RouteDTO> getOriginList() {
		return originList;
	}

	public void setOriginList(List<RouteDTO> originList) {
		this.originList = originList;
	}

	public List<RouteDTO> getDestinationList() {
		return destinationList;
	}

	public void setDestinationList(List<RouteDTO> destinationList) {
		this.destinationList = destinationList;
	}

	public List<RouteDTO> getViaList() {
		return viaList;
	}

	public void setViaList(List<RouteDTO> viaList) {
		this.viaList = viaList;
	}

	public String getServiceRefNo() {
		return serviceRefNo;
	}

	public void setServiceRefNo(String serviceRefNo) {
		this.serviceRefNo = serviceRefNo;
	}

	public List<NisiSeriyaDTO> getNisiSeriyaDTOList() {
		return nisiSeriyaDTOList;
	}

	public void setNisiSeriyaDTOList(List<NisiSeriyaDTO> nisiSeriyaDTOList) {
		this.nisiSeriyaDTOList = nisiSeriyaDTOList;
	}

	public boolean isDisableAddBtn() {
		return disableAddBtn;
	}

	public void setDisableAddBtn(boolean disableAddBtn) {
		this.disableAddBtn = disableAddBtn;
	}

	public boolean isDisableDataTable() {
		return disableDataTable;
	}

	public void setDisableDataTable(boolean disableDataTable) {
		this.disableDataTable = disableDataTable;
	}

	public List<String> getRequestNumList() {
		return requestNumList;
	}

	public void setRequestNumList(List<String> requestNumList) {
		this.requestNumList = requestNumList;
	}

	public List<String> getServiceRefNumList() {
		return serviceRefNumList;
	}

	public void setServiceRefNumList(List<String> serviceRefNumList) {
		this.serviceRefNumList = serviceRefNumList;
	}

	public List<String> getPermitNumList() {
		return permitNumList;
	}

	public void setPermitNumList(List<String> permitNumList) {
		this.permitNumList = permitNumList;
	}

	public List<String> getServiceAgreementNumList() {
		return serviceAgreementNumList;
	}

	public void setServiceAgreementNumList(List<String> serviceAgreementNumList) {
		this.serviceAgreementNumList = serviceAgreementNumList;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getServiceAgreementNo() {
		return serviceAgreementNo;
	}

	public void setServiceAgreementNo(String serviceAgreementNo) {
		this.serviceAgreementNo = serviceAgreementNo;
	}

	public boolean isSearchEditDisable() {
		return searchEditDisable;
	}

	public void setSearchEditDisable(boolean searchEditDisable) {
		this.searchEditDisable = searchEditDisable;
	}

	public List<NisiSeriyaDTO> getSelectedNisiSeriyaDTOList() {
		return selectedNisiSeriyaDTOList;
	}

	public void setSelectedNisiSeriyaDTOList(List<NisiSeriyaDTO> selectedNisiSeriyaDTOList) {
		this.selectedNisiSeriyaDTOList = selectedNisiSeriyaDTOList;
	}

	public boolean isServiceBtnsDisable() {
		return serviceBtnsDisable;
	}

	public void setServiceBtnsDisable(boolean serviceBtnsDisable) {
		this.serviceBtnsDisable = serviceBtnsDisable;
	}

	public boolean isServiceApprRejDisable() {
		return serviceApprRejDisable;
	}

	public void setServiceApprRejDisable(boolean serviceApprRejDisable) {
		this.serviceApprRejDisable = serviceApprRejDisable;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public boolean isPrintserviceAggrDisable() {
		return printserviceAggrDisable;
	}

	public void setPrintserviceAggrDisable(boolean printserviceAggrDisable) {
		this.printserviceAggrDisable = printserviceAggrDisable;
	}

}
