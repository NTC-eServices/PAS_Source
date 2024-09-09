package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.AdvancedPaymentDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.ManageInvestigationDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.SimRegistrationDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.model.service.GenerateReceiptService;
import lk.informatics.ntc.model.service.GrievanceManagementService;
import lk.informatics.ntc.model.service.ManageInvestigationService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.model.service.SimRegistrationService;
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

@ManagedBean(name = "generateReceiptBackingBean")
@ViewScoped
public class GenerateReceiptBackingBean implements Serializable {

	private static final long serialVersionUID = 2229504294407115602L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private ManageInvestigationDTO selectedInvestigation;
	private GenerateReceiptDTO generateReceiptDTO;
	public GenerateReceiptService generateReceiptService;
	public DriverConductorTrainingService driverConductorService;
	private List<GenerateReceiptDTO> transactionTypeList = new ArrayList<GenerateReceiptDTO>(0);
	private List<GenerateReceiptDTO> departmentList = new ArrayList<GenerateReceiptDTO>(0);
	private List<GenerateReceiptDTO> unitList = new ArrayList<GenerateReceiptDTO>(0);
	private List<GenerateReceiptDTO> receiptNoList = new ArrayList<GenerateReceiptDTO>(0);
	private List<GenerateReceiptDTO> voucherNoList = new ArrayList<GenerateReceiptDTO>(0);
	private List<GenerateReceiptDTO> applicationNo = new ArrayList<GenerateReceiptDTO>(0);
	private List<GenerateReceiptDTO> generateReceiptList = new ArrayList<GenerateReceiptDTO>(0);
	private List<GenerateReceiptDTO> paymentModeList = new ArrayList<GenerateReceiptDTO>(0);
	private List<GenerateReceiptDTO> bankNameList = new ArrayList<GenerateReceiptDTO>(0);
	private List<GenerateReceiptDTO> branchNameList = new ArrayList<GenerateReceiptDTO>(0);

	private List<AdvancedPaymentDTO> advancedPaymentDetailsList = new ArrayList<AdvancedPaymentDTO>(0);
	private List<PaymentVoucherDTO> voucherDetailsList = new ArrayList<PaymentVoucherDTO>(0);

	private List<CommonDTO> counterList = new ArrayList<>();
	private QueueManagementService queueManagementService;
	private CommonService commonService;
	private MigratedService migratedService;

	private String date1;
	public StreamedContent file;

	private GenerateReceiptDTO selectedRow;
	private String errorMessage, successMessage, infoMessage;

	private boolean disabledBank = true;
	private boolean disabledChequeBankReceiptNo = false; // --
	private boolean disabledDepositToBank = false; // --
	private boolean disabledPaymentMode = false;

	private boolean requiredBankDisplay = false;
	private boolean requiredchequeReceiptNoDisplay = false;

	private boolean disableGenarate = false;
	private boolean disablePrint = true;
	private boolean disableReprint = true;

	boolean callnext = false;
	boolean skip = true;

	private boolean disabledPaymentModeDC = false;
	private boolean requiredBankDisplayDC = false;
	private boolean disabledBankDC = true;
	private boolean disabledDepositToBankDC = false;
	private boolean requiredchequeReceiptNoDisplayDC = false;
	private boolean disabledChequeBankReceiptNoDC = false;
	private boolean disableGenarateDC = false;
	private boolean disablePrintDC = true;
	private boolean disableReprintDC = true;
	BigDecimal totalAmountD = new BigDecimal(0);

	private CommonDTO commonDTO;

	private String strQueueNo;
	private StreamedContent files;
	private String valueForPrint;
	/** Driver Conductor Receipt Generate **/
	// DTO
	private DriverConductorRegistrationDTO driverConductorReceiptDTO = new DriverConductorRegistrationDTO();
	private DriverConductorRegistrationDTO showDataByVoucherDTO = new DriverConductorRegistrationDTO();
	private DriverConductorRegistrationDTO selectedRowDriverConductor;
	// List

	private List<DriverConductorRegistrationDTO> driverConductorVoucherNoList = new ArrayList<>();
	private List<DriverConductorRegistrationDTO> driverConductorapplicationNoList = new ArrayList<>();
	private List<DriverConductorRegistrationDTO> driverConductorDcIdList = new ArrayList<>();
	private List<DriverConductorRegistrationDTO> generateReceiptListForDriverConductor = new ArrayList<>();

	private List<DriverConductorRegistrationDTO> voucherDetailsListForDC = new ArrayList<DriverConductorRegistrationDTO>(
			0);

	/** Grievnace Management Function **/
	// DTO
	private ComplaintRequestDTO grievanceReceiptDTO = new ComplaintRequestDTO();
	private GrievanceManagementService receiptGrievanceManagement;
	private ComplaintRequestDTO ajaxShowDataDTO;
	private ComplaintRequestDTO selectedRowGrievance;
	private BigDecimal latePaymentFee ;

	// Services
	private GrievanceManagementService grievanceReceiptService;

	// List
	private List<String> approvedVoucherNoList;
	private List<String> complaintNoList;
	private List<ComplaintRequestDTO> generateReceiptListForGrievance = new ArrayList<>();
	private List<ComplaintRequestDTO> voucherDetailsListForGr = new ArrayList<ComplaintRequestDTO>(0);

	private boolean disabledPaymentModeGr = false;
	private boolean requiredBankDisplayGr = false;
	private boolean disabledBankGr = true;
	private boolean disabledDepositToBankGr = false;
	private boolean requiredchequeReceiptNoDisplayGr = false;
	private boolean disabledChequeBankReceiptNoGr = false;
	private boolean disableGenarateGr = false;
	private boolean disablePrintGr = true;
	private boolean disableReprintGr = true;
	BigDecimal totalAmountG = new BigDecimal(0);

	/** Grievance Management Function End **/

	/** Investigation Function Start **/
	// DTO

	private ManageInvestigationDTO manageInvestigation = new ManageInvestigationDTO();
	private ManageInvestigationDTO ajaxShowDataDTOInves;
	private ManageInvestigationDTO selectedRowInves;

	// Services
	private ManageInvestigationService investigationService;

	// List
	private List<ManageInvestigationDTO> approvedVoucherForInves;
	private List<ManageInvestigationDTO> chargeRefNoList;
	private List<ManageInvestigationDTO> voucherDetailsListForInves = new ArrayList<ManageInvestigationDTO>(0);
	private List<ManageInvestigationDTO> generateReceiptListForInves = new ArrayList<>();

	private boolean disabledPaymentModeInves = false;
	private boolean requiredBankDisplayInves = false;
	private boolean disabledBankInves = true;
	private boolean disabledDepositToBankInves = false;
	private boolean requiredchequeReceiptNoDisplayInves = false;
	private boolean disabledChequeBankReceiptNoInves = false;
	private boolean disablePrintInves = true;
	private boolean disableReprintInves = true;
	BigDecimal totalAmount = new BigDecimal(0);

	/** Investigation Function End **/

	/** SIM Registration Function **/
	// services
	private SimRegistrationService simRegistrationService;

	// DTO
	private SimRegistrationDTO simregDTO;
	private SimRegistrationDTO onchangeDTO;
	private SimRegistrationDTO selectedRowSimReg;

	// List

	private List<SimRegistrationDTO> approvedVoucherForSimReg = new ArrayList<>();
	private List<SimRegistrationDTO> generateReceiptListForSimReg;
	private List<SimRegistrationDTO> voucherDetailsListForSimReg = new ArrayList<>();

	private boolean disabledPaymentModeSimReg = false;
	private boolean requiredBankDisplaySimReg = false;
	private boolean disabledBankSimReg = true;
	private boolean disabledDepositToBankSimReg = false;
	private boolean requiredchequeReceiptNoDisplaySimReg = false;
	private boolean disabledChequeBankReceiptNoSimReg = false;
	private boolean disablePrintSimReg = true;
	private boolean disableReprintSimReg = true;
	BigDecimal totalAmountSim = new BigDecimal(0);

	/** SIM Registration Function End **/
	String sisuServiceNo =null;
	@PostConstruct
	public void init() {
		generateReceiptDTO = new GenerateReceiptDTO();
		commonDTO = new CommonDTO();

		generateReceiptService = (GenerateReceiptService) SpringApplicationContex.getBean("generateReceiptService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		driverConductorService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		grievanceReceiptService = (GrievanceManagementService) SpringApplicationContex
				.getBean("grievanceManagementService");
		investigationService = (ManageInvestigationService) SpringApplicationContex
				.getBean("manageInvestigationService");
		counterList = generateReceiptService.counterdropdown();
		transactionTypeList = generateReceiptService.getTransactionType();
		departmentList = generateReceiptService.getDepartment();
		unitList = generateReceiptService.getUnit();
		receiptNoList = generateReceiptService.getReceiptNo();
		voucherNoList = generateReceiptService.getVoucherNo();
		applicationNo = generateReceiptService.getApplicationNo();

		paymentModeList = generateReceiptService.getPaymentMode();
		bankNameList = generateReceiptService.getBankName();

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		date1 = dateFormat.format(date);
		totalAmountD = new BigDecimal(0);
		generateReceiptList = generateReceiptService.getGenerateReceiptList(generateReceiptDTO);
		/** Driver Conductor Receipt Generator **/

		driverConductorVoucherNoList = generateReceiptService.getVoucherNoListForDc();
		driverConductorapplicationNoList = generateReceiptService.getApplicationNoListForDC();
		driverConductorDcIdList = generateReceiptService.getDriverConductorIdListForDC();
		generateReceiptListForDriverConductor = generateReceiptService.getpendingReceiptListForDriverConductor();

		/* * End* */

		/** Grievance Managemnet **/
		approvedVoucherNoList = grievanceReceiptService.getApprovedVoucherNoList();
		complaintNoList = grievanceReceiptService.complaintNoList();
		generateReceiptListForGrievance = grievanceReceiptService.getPendingGenerateReceiptListForGrievance();// display all approved voucher list
		/** End **/

		/** Investigation **/
		approvedVoucherForInves = investigationService.approvedVoucherForInves();
		chargeRefNoList = investigationService.approvedChargeRefNO();
		generateReceiptListForInves = investigationService.getPendingGenerateReceiptListForInves();
		/** End **/

		/** SIM Registration **/
		simRegistrationService = (SimRegistrationService) SpringApplicationContex.getBean("simRegistrationService");

		simregDTO = new SimRegistrationDTO();
		approvedVoucherForSimReg = simRegistrationService.getApprovedVouNoList();

		generateReceiptListForSimReg = simRegistrationService.pendingDTO(null, null, null, null, null, null, "A");

		/** End **/
	}

	@SuppressWarnings("deprecation")
	public void search() {
		if (generateReceiptDTO.getVoucherNo() != null && !generateReceiptDTO.getVoucherNo().trim().isEmpty()) {

			generateReceiptList = new ArrayList<GenerateReceiptDTO>();
			generateReceiptList = generateReceiptService.getGenerateReceiptList(generateReceiptDTO);
			if (generateReceiptList.isEmpty()) {
				setInfoMessage("No records for selected criteria.");
				RequestContext.getCurrentInstance().update("infoMSG");
				RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
			}

		} else {
			setErrorMessage("Voucher No. should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clear() {
		generateReceiptDTO.setDepartmentCode("");
		generateReceiptDTO.setTransactionCode("");
		generateReceiptDTO.setDepartmentDescription("");
		generateReceiptDTO.setTransactionDescription("");
		generateReceiptDTO.setReceiptNo("");
		generateReceiptDTO.setPermitNo("");
		generateReceiptDTO.setVoucherNo("");
		generateReceiptDTO.setApplicationNo("");
		generateReceiptDTO.setSearchDate(null);
		generateReceiptDTO.setUnitCode("");
		generateReceiptDTO.setQueueNo("");
		generateReceiptList.clear();
		callnext = false;
		skip = true;
		valueForPrint = null;
		voucherNoList = generateReceiptService.getVoucherNo();
		generateReceiptList = generateReceiptService.getGenerateReceiptList(generateReceiptDTO);
	}

	@SuppressWarnings("deprecation")
	public void view() {
		disablePopupButtons();
		selectedRow.setPaymentModeCode(null);
		selectedRow.setBankCode("");
		selectedRow.setBankDescription("");
		selectedRow.setBranchCode("");
		selectedRow.setBranchDescription("");
		selectedRow.setChequeOrBankReceipt("");
		selectedRow.setRemarks("");
		selectedRow.setDepositToBank(false);

		boolean isReceiptgenerated = generateReceiptService.isReceiptgenerated(selectedRow.getVoucherNo());
		if (isReceiptgenerated) {
			GenerateReceiptDTO receiptDetails = generateReceiptService.getReceiptDetails(selectedRow.getVoucherNo());
			selectedRow.setPaymentModeCode(receiptDetails.getPaymentModeCode());
			selectedRow.setBankCode(receiptDetails.getBankCode());
			onBankChange();
			selectedRow.setBranchCode(receiptDetails.getBranchCode());
			selectedRow.setBankDescription(receiptDetails.getBankDescription());
			selectedRow.setBranchDescription(receiptDetails.getBranchDescription());
			selectedRow.setDepositToBank(receiptDetails.getDepositToBank());
			selectedRow.setChequeOrBankReceipt(receiptDetails.getChequeOrBankReceipt());
			selectedRow.setRemarks(receiptDetails.getRemarks());
		}
		if (selectedRow.getPaymentType().equals("V")) {
			// START update task status as "O"
			if (selectedRow.getTransactionCode().equals("01")) {
				generateReceiptService.updateTenderTaskStatus(selectedRow.getApplicationNo(),
						sessionBackingBean.getLoginUser());
			} else if (selectedRow.getTransactionCode().equals("18")) {
				sisuServiceNo =commonService.getSisurServiceNoInTaskTable(selectedRow.getApplicationNo());
				generateReceiptService.updateSisuSariyaTaskStatus(selectedRow.getApplicationNo(),
						sessionBackingBean.getLoginUser(),sisuServiceNo);
				
			} else {
				generateReceiptService.updateTaskStatus(selectedRow.getApplicationNo(),
						sessionBackingBean.getLoginUser());
			}
			// END update task status as "O"
			voucherDetailsList = generateReceiptService.getVoucherDetailsList(selectedRow.getVoucherNo());
			RequestContext.getCurrentInstance().update("dialogVoucher");
			RequestContext.getCurrentInstance().execute("PF('voucherDialog').show()");

		} else if (selectedRow.getPaymentType().equals("AP")) {

			advancedPaymentDetailsList = generateReceiptService
					.getAdvancedPaymentDetailsList(selectedRow.getVoucherNo());
			RequestContext.getCurrentInstance().update("dialogAdvancedPayment");
			RequestContext.getCurrentInstance().execute("PF('advancedPaymentDialog').show()");
		}
	}

	public void disablePopupButtons() {
		boolean isReceiptgenerated = generateReceiptService.isReceiptgenerated(selectedRow.getVoucherNo());
		boolean isPrintCompleted = generateReceiptService.isPrintCompleted(selectedRow.getVoucherNo());

		disabledBank = true;
		disabledChequeBankReceiptNo = false;
		disabledDepositToBank = false;
		/* render required */
		requiredBankDisplay = false;
		requiredchequeReceiptNoDisplay = false;

		if (isReceiptgenerated) {
			disableGenarate = true;
			disabledPaymentMode = true;

			if (isPrintCompleted) {
				disablePrint = true;
				disableReprint = false;
			} else {
				disablePrint = false;
				disableReprint = true;
			}
		} else {
			disableGenarate = false;
			disabledPaymentMode = false;
			disablePrint = false;
			disableReprint = true;
		}
	}

	@SuppressWarnings("deprecation")
	public void generateReceipt() {
		String loginUser = sessionBackingBean.getLoginUser();
		boolean isReceiptgenerated = generateReceiptService.isReceiptgenerated(selectedRow.getVoucherNo());
		BigDecimal bg1 = selectedRow.getTotalAmount();
		int paraTotalAmount = generateReceiptService.getParaTotalAmount();
		BigDecimal bg2 = new BigDecimal(paraTotalAmount);
		boolean EmptyChequeOrBankReceipt = (selectedRow.getChequeOrBankReceipt() == null
				|| selectedRow.getChequeOrBankReceipt().trim().isEmpty());
		if (isReceiptgenerated) {
			setInfoMessage("Receipt already generated.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		} else if (selectedRow.getPaymentModeCode() == null || selectedRow.getPaymentModeCode().trim().isEmpty()) {
			setErrorMessage("Payment Mode should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (selectedRow.getPaymentModeCode().equals("CASH")) {
			if (bg1.compareTo(bg2) == 1 && EmptyChequeOrBankReceipt) {
				setErrorMessage("Bank Receipt No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if ((selectedRow.getDepositToBank() == true && EmptyChequeOrBankReceipt)) {
				setErrorMessage("Bank Receipt No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				// generateReceipt for CASH
				//modified --->selectedRow.getApplicationNo() means service ref number
				if(selectedRow.getTransactionCode().equals("18")){
					selectedRow.setSisuServiceNo(commonService.getSisurServiceNoInTaskTable(selectedRow.getApplicationNo()));
				}
				generateReceiptService.saveReceipt(selectedRow, loginUser);

				disablePopupButtons();

				generateReceiptList = generateReceiptService.getGenerateReceiptList(generateReceiptDTO);
				setSuccessMessage("Receipt generated successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			}
		} else if (selectedRow.getPaymentModeCode().equals("CHEQ")) {
			if (selectedRow.getBankCode().equals("")) {
				setErrorMessage("Bank Name should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (selectedRow.getBranchCode().equals("")) {
				setErrorMessage("Branch Name should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (EmptyChequeOrBankReceipt) {
				setErrorMessage("Cheque No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				// generateReceipt for CHEQ
				if(selectedRow.getTransactionCode().equals("18")){
					selectedRow.setSisuServiceNo(commonService.getSisurServiceNoInTaskTable(selectedRow.getApplicationNo()));
				}
				generateReceiptService.saveReceipt(selectedRow, loginUser);

				disablePopupButtons();

				generateReceiptList = generateReceiptService.getGenerateReceiptList(generateReceiptDTO);
				setSuccessMessage("Receipt generated successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			}

		}
		String showReceiptNo = generateReceiptService.getReceiptNoForPrint(selectedRow.getVoucherNo());
		selectedRow.setReceiptNo(showReceiptNo);

		if (showReceiptNo != null && !showReceiptNo.isEmpty() && !showReceiptNo.equals("")
				&& !showReceiptNo.equals(null)) {

			generateReceiptService.printUpdate(selectedRow.getVoucherNo());
			disablePopupButtons();

			/** 1/31/2020 Changed by tharushi.e for preventing file download **/
		}

	}

	public void ifDepositToBank() {
		if (selectedRow.getDepositToBank() == true) {
			requiredchequeReceiptNoDisplay = true;
		} else {
			requiredchequeReceiptNoDisplay = false;
		}
	}

	public void onPaymentModeChange() {
		if (selectedRow.getPaymentModeCode().equals("CHEQ")) {
			selectedRow.setDepositToBank(false);
			disabledBank = false;
			disabledChequeBankReceiptNo = false;
			disabledDepositToBank = true;
			requiredBankDisplay = true;
			requiredchequeReceiptNoDisplay = true;

		} else if (selectedRow.getPaymentModeCode().equals("CASH")) {
			int paraTotalAmount = generateReceiptService.getParaTotalAmount();
			BigDecimal bg1 = selectedRow.getTotalAmount();
			BigDecimal bg2 = new BigDecimal(paraTotalAmount);

			selectedRow.setBankDescription(null);
			selectedRow.setBankCode(null);
			selectedRow.setBranchCode(null);
			selectedRow.setBranchDescription(null);
			disabledDepositToBank = false;
			disabledChequeBankReceiptNo = false;
			disabledBank = true;
			requiredchequeReceiptNoDisplay = false;

			if (bg1.compareTo(bg2) == 1) {
				selectedRow.setDepositToBank(true);
				disabledDepositToBank = true;
				requiredchequeReceiptNoDisplay = true;
			}
		} else {
			disabledBank = true;
			disabledChequeBankReceiptNo = false;
			disabledDepositToBank = false;
			requiredBankDisplay = false;
			requiredchequeReceiptNoDisplay = false;
			selectedRow.setDepositToBank(false);
			selectedRow.setBankDescription(null);
			selectedRow.setBankCode(null);
			selectedRow.setBranchCode(null);
			selectedRow.setBranchDescription(null);
			selectedRow.setBranchDescription(null);
			selectedRow.setChequeOrBankReceipt(null);
		}

	}

	@SuppressWarnings("deprecation")
	public void onCounterSelect() {
		sessionBackingBean.setCounterId(commonDTO.getCounterId());
		generateReceiptService = (GenerateReceiptService) SpringApplicationContex.getBean("generateReceiptService");
		generateReceiptService.counterStatus(sessionBackingBean.getCounterId(), sessionBackingBean.getLoginUser());

		sessionBackingBean.setCounterCheck(false);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('selectCounterDialog').hide();");
	}

	public void handleClose() throws InterruptedException {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../home/welcomePage.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updateStatusOfQueueNumberAfterCallNextComplete() {
		strQueueNo = generateReceiptDTO.getQueueNo();

		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {

			migratedService.updateStatusOfQueueNumberAfterCallNext(strQueueNo, "C");
			migratedService.updateTransactionTypeCodeForQueueNo(strQueueNo, "06");

		}
	}

	@SuppressWarnings({ "deprecation" })
	public void nextAction() {
		String queueNo = queueManagementService.callNextQueueNumberAction("06", null);
		strQueueNo = queueNo;

		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {

			generateReceiptDTO = generateReceiptService.getApplicationDetailsByQueueNo(strQueueNo);
			generateReceiptDTO.setQueueNo(strQueueNo);

			migratedService.updateStatusOfQueueNumberAfterCallNext(strQueueNo, "O");
			commonService.updateCounterQueueNo(strQueueNo, sessionBackingBean.getCounterId());
			migratedService.updateCounterIdOfQueueNumberAfterCallNext(strQueueNo, sessionBackingBean.getCounterId());

			callnext = true;
			skip = false;

			migratedService.updateCounterIdOfQueueNumberAfterCallNext(strQueueNo, sessionBackingBean.getCounterId());

		} else {
			setInfoMessage("Queue is empty.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		}
	}

	public void skipAction() {
		if (strQueueNo != null && !strQueueNo.isEmpty() && !strQueueNo.trim().equalsIgnoreCase("")) {

			migratedService.updateSkipQueueNumberStatus(strQueueNo);

			callnext = true;
			skip = false;
			clear();

		}
	}

	public void downloadFile() {

		valueForPrint = selectedRow.getVoucherNo();

		files = null;
		String sourceFileName = null;
		String loginUser = sessionBackingBean.getLoginUser();

		Connection conn = null;

		if (selectedRow.getPaymentType().equals("V")) {
			sourceFileName = "..//reports//VoucherpaymentReceipt.jrxml";
		} else if (selectedRow.getPaymentType().equals("AP")) {
			sourceFileName = "..//reports//AdvancepaymentReceipt.jrxml";
		}

		try {
			conn = ConnectionManager.getConnection();

			String logopath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Voucher_No", valueForPrint);
			parameters.put("image_path", logopath);
			parameters.put("P_login_user", loginUser);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// update the status of re-print no.
		generateReceiptService.printUpdate(selectedRow.getVoucherNo());
		disablePopupButtons();

	}

	public void rePrint(ActionEvent ae) throws JRException {

		valueForPrint = selectedRow.getVoucherNo();

		String loginUser = sessionBackingBean.getLoginUser();

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		if (selectedRow.getPaymentType().equals("V")) {
			sourceFileName = "..//reports//VoucherpaymentReceipt.jrxml";
		} else if (selectedRow.getPaymentType().equals("AP")) {
			sourceFileName = "..//reports//AdvancepaymentReceipt.jrxml";
		}

		try {
			conn = ConnectionManager.getConnection();
			String imagepath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Voucher_No", valueForPrint);
			parameters.put("image_path", imagepath);
			parameters.put("P_login_user", loginUser);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Receipt_reprint.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// update the status of re-print no.
		generateReceiptService.rePrintUpdate(selectedRow.getVoucherNo());
		disablePopupButtons();

	}

	public void onBankChange() {
		branchNameList = generateReceiptService.getBranchName(selectedRow.getBankCode());
	}

	public void onTransTypeChange() {
		if (generateReceiptDTO.getTransactionCode().equals("")) {
			voucherNoList = generateReceiptService.getVoucherNo();
		} else {
			voucherNoList = generateReceiptService.getVoucherNo(generateReceiptDTO.getTransactionCode());
		}
	}

	public void onVoucherChange() {
		if (!generateReceiptDTO.getVoucherNo().equals("") && generateReceiptDTO.getVoucherNo() != null) {
			GenerateReceiptDTO searchFields = generateReceiptService.getSearchFields(generateReceiptDTO.getVoucherNo());
			generateReceiptDTO.setTransactionCode(searchFields.getTransactionCode());
			generateReceiptDTO.setDepartmentCode(searchFields.getDepartmentCode());
			generateReceiptDTO.setTransactionDescription(searchFields.getTransactionDescription());
			generateReceiptDTO.setDepartmentDescription(searchFields.getDepartmentDescription());
			generateReceiptDTO.setReceiptNo(searchFields.getReceiptNo());
			generateReceiptDTO.setApplicationNo(searchFields.getApplicationNo());
			generateReceiptDTO.setPermitNo(searchFields.getPermitNo());
			generateReceiptDTO.setUnitCode(searchFields.getUnitCode());
			generateReceiptDTO.setSearchDate(searchFields.getSearchDate());
			generateReceiptDTO.setQueueNo(searchFields.getQueueNo());
			generateReceiptList.clear();
		} else {
			clear();
		}
	}

	/**
	 * 
	 * Driver Conductor Receipt Generatot
	 */
	public void onDriverConductorVoucherChange() {

		showDataByVoucherDTO = generateReceiptService
				.getValuesAccordingtoVoucherNo(driverConductorReceiptDTO.getVoucher());
		driverConductorReceiptDTO.setAppNo(showDataByVoucherDTO.getAppNo());
		driverConductorReceiptDTO.setNic(showDataByVoucherDTO.getNic());
		driverConductorReceiptDTO.setDriverConductorId(showDataByVoucherDTO.getDriverConductorId());
		driverConductorReceiptDTO.setTrainingTYpeDes(showDataByVoucherDTO.getTrainingTYpeDes());
		driverConductorReceiptDTO.setReceiptNo(showDataByVoucherDTO.getReceiptNo());

	}

	public void onDriverConductorApplicationChange() {
		showDataByVoucherDTO = generateReceiptService
				.getValuesAccordingToApplicationNo(driverConductorReceiptDTO.getAppNo());
		driverConductorReceiptDTO.setVoucher(showDataByVoucherDTO.getVoucher());
		driverConductorReceiptDTO.setNic(showDataByVoucherDTO.getNic());
		driverConductorReceiptDTO.setDriverConductorId(showDataByVoucherDTO.getDriverConductorId());
		driverConductorReceiptDTO.setTrainingTYpeDes(showDataByVoucherDTO.getTrainingTYpeDes());
		driverConductorReceiptDTO.setReceiptNo(showDataByVoucherDTO.getReceiptNo());

	}

	public void onDriverConductorDcIdChange() {
		showDataByVoucherDTO = generateReceiptService
				.getValuesAccordingToDcIdNo(driverConductorReceiptDTO.getDriverConductorId());
		driverConductorReceiptDTO.setVoucher(showDataByVoucherDTO.getVoucher());
		driverConductorReceiptDTO.setNic(showDataByVoucherDTO.getNic());
		driverConductorReceiptDTO.setAppNo(showDataByVoucherDTO.getAppNo());
		driverConductorReceiptDTO.setTrainingTYpeDes(showDataByVoucherDTO.getTrainingTYpeDes());
		driverConductorReceiptDTO.setReceiptNo(showDataByVoucherDTO.getReceiptNo());
	}

	public void searchDataForDriceConductor() {
		if (driverConductorReceiptDTO.getVoucher() != null
				&& !driverConductorReceiptDTO.getVoucher().trim().isEmpty()) {
			generateReceiptListForDriverConductor = new ArrayList<DriverConductorRegistrationDTO>();
			generateReceiptListForDriverConductor = generateReceiptService
					.getGenerateReceiptListForDriverConductor(driverConductorReceiptDTO);
			if (generateReceiptListForDriverConductor.isEmpty()) {
				setInfoMessage("No records for selected criteria.");
				RequestContext.getCurrentInstance().update("infoMSG");
				RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
			}
		} else {
			setErrorMessage("Voucher No. should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearForDriceConductor() {

		driverConductorReceiptDTO = new DriverConductorRegistrationDTO();
		generateReceiptListForDriverConductor = generateReceiptService.getpendingReceiptListForDriverConductor();
	}

	public void disablePopupButtonsForDriverConductor() {
		boolean isReceiptgenerated = generateReceiptService
				.isReceiptgeneratedForDriverConducctor(selectedRowDriverConductor.getVoucher());

		boolean isPrintCompleted = generateReceiptService
				.isPrintCompletedForDriverConductor(selectedRowDriverConductor.getVoucher());

		disabledBankDC = true;
		disabledChequeBankReceiptNoDC = false;
		disabledDepositToBankDC = false;

		requiredBankDisplayDC = false;
		requiredchequeReceiptNoDisplayDC = false;

		if (isReceiptgenerated) {
			disableGenarateDC = true;
			disabledPaymentModeDC = true;
			disablePrintDC = true;
			disableReprintDC = false;

		} else {
			disableGenarateDC = false;
			disabledPaymentModeDC = false;
			disablePrintDC = false; // --
			disableReprintDC = true;
		}

	}

	public void viewDriverConductorRe() {

		disablePopupButtonsForDriverConductor();
		selectedRowDriverConductor.setPaymentModeCodeForDC(null);
		selectedRowDriverConductor.setBankCodeForDC("");
		selectedRowDriverConductor.setBankDesForDC("");
		selectedRowDriverConductor.setBranchCodeForDC("");
		selectedRowDriverConductor.setBranchDesForDC("");
		selectedRowDriverConductor.setChequeOrBankReceipt("");
		selectedRowDriverConductor.setReceiptRemarks("");
		selectedRowDriverConductor.setDepositToBankForDC(false);

		boolean isReceiptgenerated = generateReceiptService
				.isReceiptgeneratedForDriverConducctor(selectedRowDriverConductor.getVoucher());

		if (isReceiptgenerated) {
			DriverConductorRegistrationDTO receiptDetails = generateReceiptService
					.getReceiptDetailsForDriverConductor(selectedRowDriverConductor.getVoucher());
			selectedRowDriverConductor.setPaymentModeCodeForDC(receiptDetails.getPaymentModeCodeForDC());
			selectedRowDriverConductor.setBankCodeForDC(receiptDetails.getBankCodeForDC());
			onBankChangeForDriverConductor();
			selectedRowDriverConductor.setBranchCodeForDC(receiptDetails.getBranchCodeForDC());
			selectedRowDriverConductor.setBankDesForDC(receiptDetails.getBankDesForDC());
			selectedRowDriverConductor.setBranchDesForDC(receiptDetails.getBranchDesForDC());
			selectedRowDriverConductor.setDepositToBankForDC(receiptDetails.getDepositToBankForDC());
			selectedRowDriverConductor.setChequeOrBankReceipt(receiptDetails.getChequeOrBankReceipt());
			selectedRowDriverConductor.setReceiptRemarks(receiptDetails.getReceiptRemarks());
		}

		// END update task status as "O"
		voucherDetailsListForDC = generateReceiptService
				.getVoucherDetailsListForDriverCOnductor(selectedRowDriverConductor.getVoucher());

		for (int i = 0; i < voucherDetailsListForDC.size(); i++) {

			try {

				BigDecimal amt = null;
				amt = voucherDetailsListForDC.get(i).getAmmount();
				totalAmountD = totalAmountD.add(amt);
				selectedRowDriverConductor.setTotalAmount(totalAmountD);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		RequestContext.getCurrentInstance().update("dialogVoucherDc");
		RequestContext.getCurrentInstance().execute("PF('voucherDialogForDriverConductor').show()");

	}

	public void onPaymentModeChangeDriverConductor() {

		if (selectedRowDriverConductor.getPaymentModeCodeForDC().equals("CHEQ")) {
			selectedRowDriverConductor.setDepositToBankForDC(false);
			disabledBankDC = false;
			disabledChequeBankReceiptNoDC = false;
			disabledDepositToBankDC = true;
			requiredBankDisplayDC = true;
			requiredchequeReceiptNoDisplayDC = true;

		} else if (selectedRowDriverConductor.getPaymentModeCodeForDC().equals("CASH")) {
			int paraTotalAmount = generateReceiptService.getParaTotalAmount();
			BigDecimal bg1 = selectedRowDriverConductor.getTotalAmount();
			BigDecimal bg2 = new BigDecimal(paraTotalAmount);

			selectedRowDriverConductor.setBankDesForDC(null);
			selectedRowDriverConductor.setBankCodeForDC(null);
			selectedRowDriverConductor.setBranchCodeForDC(null);
			selectedRowDriverConductor.setBranchDesForDC(null);
			disabledDepositToBankDC = false;
			disabledChequeBankReceiptNoDC = false;
			disabledBankDC = true;
			requiredchequeReceiptNoDisplayDC = false;

			if (bg1.compareTo(bg2) == 1) {
				selectedRowDriverConductor.setDepositToBankForDC(true);
				disabledDepositToBankDC = true;
				requiredchequeReceiptNoDisplayDC = true;
			}
		} else {
			disabledBankDC = true;
			disabledChequeBankReceiptNoDC = false;
			disabledDepositToBankDC = false;
			requiredBankDisplayDC = false;
			requiredchequeReceiptNoDisplayDC = false;
			selectedRowDriverConductor.setDepositToBankForDC(false);
			selectedRowDriverConductor.setBankDesForDC(null);
			selectedRowDriverConductor.setBankCodeForDC(null);
			selectedRowDriverConductor.setBranchCodeForDC(null);
			selectedRowDriverConductor.setBranchDesForDC(null);

			selectedRowDriverConductor.setChequeOrBankReceipt(null);
		}

	}

	public void onBankChangeForDriverConductor() {
		branchNameList = generateReceiptService.getBranchName(selectedRowDriverConductor.getBankCodeForDC());
	}

	public void ifDepositToBankForDriverConductor() {
		if (selectedRowDriverConductor.getDepositToBankForDC() == true) {
			requiredchequeReceiptNoDisplay = true;
		} else {
			requiredchequeReceiptNoDisplay = false;
		}
	}

	public void generateReceiptForDriverConduc() {

		String loginUser = sessionBackingBean.getLoginUser();
		boolean isReceiptgenerated = generateReceiptService
				.isReceiptgeneratedForDriverConducctor(selectedRowDriverConductor.getVoucher());
		BigDecimal bg1 = selectedRowDriverConductor.getTotalAmount();

		int paraTotalAmount = generateReceiptService.getParaTotalAmount();
		BigDecimal bg2 = new BigDecimal(paraTotalAmount);
		boolean EmptyChequeOrBankReceipt = (selectedRowDriverConductor.getChequeOrBankReceipt() == null
				|| selectedRowDriverConductor.getChequeOrBankReceipt().trim().isEmpty());
		if (isReceiptgenerated) {
			setInfoMessage("Receipt already generated.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		} else if (selectedRowDriverConductor.getPaymentModeCodeForDC() == null
				|| selectedRowDriverConductor.getPaymentModeCodeForDC().trim().isEmpty()) {
			setErrorMessage("Payment Mode should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (selectedRowDriverConductor.getPaymentModeCodeForDC().equals("CASH")) {
			if (bg1.compareTo(bg2) == 1 && EmptyChequeOrBankReceipt) {
				setErrorMessage("Bank Receipt No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if ((selectedRowDriverConductor.getDepositToBankForDC() == true && EmptyChequeOrBankReceipt)) {
				setErrorMessage("Bank Receipt No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				// generateReceipt for CASH
				generateReceiptService.saveReceiptForDriverCOnductor(selectedRowDriverConductor, loginUser,
						"R", "TA", selectedRowDriverConductor.getAppNo());

				disablePopupButtonsForDriverConductor();

				generateReceiptListForDriverConductor = generateReceiptService
						.getGenerateReceiptListForDriverConductor(driverConductorReceiptDTO);

				//driverConductorService.updateStatusandStatusType("R", "TA", selectedRowDriverConductor.getAppNo());

				setSuccessMessage("Receipt generated successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			}
		} else if (selectedRowDriverConductor.getPaymentModeCodeForDC().equals("CHEQ")) {
			if (selectedRowDriverConductor.getBankCodeForDC().equals("")) {
				setErrorMessage("Bank Name should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (selectedRowDriverConductor.getBranchCodeForDC().equals("")) {
				setErrorMessage("Branch Name should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (EmptyChequeOrBankReceipt) {
				setErrorMessage("Cheque No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				// generateReceipt for CHEQ
				generateReceiptService.saveReceiptForDriverCOnductor(selectedRowDriverConductor, loginUser,
						"R", "TA", selectedRowDriverConductor.getAppNo());

				disablePopupButtonsForDriverConductor();

				generateReceiptListForDriverConductor = generateReceiptService
						.getGenerateReceiptListForDriverConductor(driverConductorReceiptDTO);

			//	driverConductorService.updateStatusandStatusType("R", "TA", selectedRowDriverConductor.getAppNo());

				setSuccessMessage("Receipt generated successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			}

		}
		String showReceiptNo = generateReceiptService
				.getReceiptNoForPrintForDriverConductor(selectedRowDriverConductor.getVoucher());
		selectedRowDriverConductor.setReceiptNo(showReceiptNo);

		if (showReceiptNo != null && !showReceiptNo.isEmpty() && !showReceiptNo.equals("")
				&& !showReceiptNo.equals(null)) {

			disablePopupButtonsForDriverConductor();
		}

	}

	private void downloadFileForDriverConductor() {

		valueForPrint = selectedRowDriverConductor.getVoucher();

		files = null;
		String sourceFileName = null;
		String loginUser = sessionBackingBean.getLoginUser();

		Connection conn = null;

		sourceFileName = "..//reports//DriverConductorVoucherpaymentReceipt.jrxml";

		try {
			conn = ConnectionManager.getConnection();

			String logopath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Voucher_No", valueForPrint);
			parameters.put("image_path", logopath);
			parameters.put("P_login_user", loginUser);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", valueForPrint + "Receipt.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		disablePopupButtonsForDriverConductor();

		driverConductorVoucherNoList = generateReceiptService.getVoucherNoListForDc();
		driverConductorapplicationNoList = generateReceiptService.getApplicationNoListForDC();
		driverConductorDcIdList = generateReceiptService.getDriverConductorIdListForDC();
		generateReceiptListForDriverConductor = generateReceiptService.getpendingReceiptListForDriverConductor();

	}

	public void rePrintForDriverConductor(ActionEvent ae) throws JRException {

		valueForPrint = selectedRowDriverConductor.getVoucher();

		String loginUser = sessionBackingBean.getLoginUser();

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//DriverConductorVoucherpaymentReceipt.jrxml";

		try {
			conn = ConnectionManager.getConnection();
			String imagepath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Voucher_No", valueForPrint);
			parameters.put("image_path", imagepath);
			parameters.put("P_login_user", loginUser);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Receipt_reprint.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		disablePopupButtonsForDriverConductor();

	}

	/**
	 * 
	 * End
	 */

	/** methods for grievance Managment **/

	public void onGrievanceVoucherChange() {

		ajaxShowDataDTO = grievanceReceiptService.showDataByVoucherOrComplaintNo(grievanceReceiptDTO.getVoucherNo(),
				null);
		grievanceReceiptDTO.setComplaintNo(ajaxShowDataDTO.getComplaintNo());
		grievanceReceiptDTO.setVehicleNo(ajaxShowDataDTO.getVehicleNo());
		String receiptNo = null;
		receiptNo = grievanceReceiptService.getReceiptNoForPrintForGrievance(grievanceReceiptDTO.getVoucherNo());
		grievanceReceiptDTO.setReceiptNoForGr(receiptNo);
	}

	public void onGrievanceComplaintChange() {

		ajaxShowDataDTO = grievanceReceiptService.showDataByVoucherOrComplaintNo(null,
				grievanceReceiptDTO.getComplaintNo());
		grievanceReceiptDTO.setVoucherNo(ajaxShowDataDTO.getVoucherNo());
		grievanceReceiptDTO.setVehicleNo(ajaxShowDataDTO.getVehicleNo());
		String receiptNo = null;
		receiptNo = grievanceReceiptService
				.getReceiptNoForPrintForGrievanceByComplaintno(grievanceReceiptDTO.getComplaintNo());
		grievanceReceiptDTO.setReceiptNoForGr(receiptNo);
	}

	public void searchDataForGrievance() {

		if (grievanceReceiptDTO.getVoucherNo() != null && !grievanceReceiptDTO.getVoucherNo().trim().isEmpty()) {
			if (grievanceReceiptDTO.getComplaintNo() != null
					&& !grievanceReceiptDTO.getComplaintNo().trim().isEmpty()) {
				generateReceiptListForGrievance = new ArrayList<ComplaintRequestDTO>();
				generateReceiptListForGrievance = grievanceReceiptService
						.getGenerateReceiptListForGrievance(grievanceReceiptDTO);
				if (generateReceiptListForGrievance.isEmpty()) {
					setInfoMessage("No records for selected criteria.");
					RequestContext.getCurrentInstance().update("infoMSG");
					RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
				}
			} else {
				setErrorMessage("Compliant No. should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Voucher No. should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearForGrievance() {
		grievanceReceiptDTO = new ComplaintRequestDTO();
		generateReceiptListForGrievance = new ArrayList<>();
		generateReceiptListForGrievance = grievanceReceiptService.getPendingGenerateReceiptListForGrievance();
	}

	public void viewGrievanceReciept() {

		disablePopupButtonsForDGrievance();
		selectedRowGrievance.setPaymentModeCodeForGr(null);
		selectedRowGrievance.setBankCodeForGr("");
		selectedRowGrievance.setBankDesForGr("");
		selectedRowGrievance.setBranchCodeForGr("");
		selectedRowGrievance.setBranchDesForGr("");
		selectedRowGrievance.setChequeOrBankReceipt("");
		selectedRowGrievance.setReceiptRemarks("");
		selectedRowGrievance.setDepositToBankForGr(false);

		boolean isReceiptgenerated = grievanceReceiptService
				.isReceiptgeneratedForGrievance(selectedRowGrievance.getVoucherNo());
		if (isReceiptgenerated) {
			ComplaintRequestDTO receiptDetails = grievanceReceiptService
					.getReceiptDetailsForGrievance(selectedRowGrievance.getVoucherNo());
			selectedRowGrievance.setPaymentModeCodeForGr(receiptDetails.getPaymentModeCodeForGr());
			selectedRowGrievance.setBankCodeForGr(receiptDetails.getBankCodeForGr());
			onBankChangeForGrievance();
			;
			selectedRowGrievance.setBranchCodeForGr(receiptDetails.getBranchCodeForGr());
			selectedRowGrievance.setBankDesForGr(receiptDetails.getBankDesForGr());
			selectedRowGrievance.setBranchDesForGr(receiptDetails.getBranchDesForGr());
			selectedRowGrievance.setDepositToBankForGr(receiptDetails.getDepositToBankForGr());
			selectedRowGrievance.setChequeOrBankReceipt(receiptDetails.getChequeOrBankReceipt());
			selectedRowGrievance.setReceiptRemarks(receiptDetails.getReceiptRemarks());
			selectedRowGrievance.setReceiptNoForGr(grievanceReceiptDTO.getReceiptNoForGr());
		}

		voucherDetailsListForGr = grievanceReceiptService
				.getVoucherDetailsListForGrievance(selectedRowGrievance.getComplaintNo());

		for (int i = 0; i < voucherDetailsListForGr.size(); i++) {

			try {

				BigDecimal amt = null;
				amt = voucherDetailsListForGr.get(i).getAmmount();
				totalAmountD = totalAmountD.add(amt);
				selectedRowGrievance.setTotalAmount(totalAmountD);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		RequestContext.getCurrentInstance().update("dialogVoucherGr");
		RequestContext.getCurrentInstance().execute("PF('voucherDialogForGrievance').show()");

	}

	public void disablePopupButtonsForDGrievance() {

		boolean isReceiptgenerated = grievanceReceiptService
				.isReceiptgeneratedForGrievance(selectedRowGrievance.getVoucherNo());
		boolean isPrintCompleted = grievanceReceiptService
				.isPrintCompletedForGrievance(selectedRowGrievance.getVoucherNo());

		disabledBankGr = true;
		disabledChequeBankReceiptNoGr = false;
		disabledDepositToBankGr = false;

		requiredBankDisplayGr = false;
		requiredchequeReceiptNoDisplayGr = false;

		if (isReceiptgenerated) {
			disableGenarateGr = true;
			disabledPaymentModeGr = true;
			disablePrintGr = true;
			disableReprintGr = false;

		} else {
			disableGenarateGr = false;
			disabledPaymentModeGr = false;
			disablePrintGr = false;
			disableReprintGr = true;
		}

	}

	public void onPaymentModeChangeGrievance() {

		if (selectedRowGrievance.getPaymentModeCodeForGr().equals("CHEQ")) {
			selectedRowGrievance.setDepositToBankForGr(false);
			;
			disabledBankGr = false;
			disabledChequeBankReceiptNoGr = false;
			disabledDepositToBankGr = true;
			requiredBankDisplayGr = true;
			requiredchequeReceiptNoDisplayGr = true;

		} else if (selectedRowGrievance.getPaymentModeCodeForGr().equals("CASH")) {
			int paraTotalAmount = generateReceiptService.getParaTotalAmount();
			int tot = grievanceReceiptService.getTotalAmount(selectedRowGrievance.getVoucherNo());
			BigDecimal bg1 = new BigDecimal(tot);

			BigDecimal bg2 = new BigDecimal(paraTotalAmount);

			selectedRowGrievance.setBankDesForGr(null);
			selectedRowGrievance.setBankCodeForGr(null);
			selectedRowGrievance.setBranchCodeForGr(null);
			selectedRowGrievance.setBranchDesForGr(null);
			disabledDepositToBankGr = false;
			disabledChequeBankReceiptNoGr = false;
			disabledBankGr = true;
			requiredchequeReceiptNoDisplayGr = false;

			if (bg1.compareTo(bg2) == 1) { // First Value is greater
				selectedRowGrievance.setDepositToBankForGr(true);
				;
				disabledDepositToBankGr = true;
				requiredchequeReceiptNoDisplayGr = true;
			}
		} else {
			disabledBankGr = true;
			disabledChequeBankReceiptNoGr = false;
			disabledDepositToBankGr = false;
			requiredBankDisplayGr = false;
			requiredchequeReceiptNoDisplayGr = false;
			selectedRowGrievance.setDepositToBankForGr(false);

			selectedRowGrievance.setBankDesForGr(null);
			selectedRowGrievance.setBankCodeForGr(null);
			selectedRowGrievance.setBranchCodeForGr(null);
			selectedRowGrievance.setBranchDesForGr(null);

			selectedRowGrievance.setChequeOrBankReceipt(null);
		}

	}

	public void onBankChangeForGrievance() {

		branchNameList = generateReceiptService.getBranchName(selectedRowGrievance.getBankCodeForGr());

	}

	public void ifDepositToBankForGrievance() {

		if (selectedRowGrievance.getDepositToBankForGr() == true) {
			requiredchequeReceiptNoDisplayGr = true;
		} else {
			requiredchequeReceiptNoDisplayGr = false;
		}

	}

	public void generateReceiptForGrievance() {

		String loginUser = sessionBackingBean.getLoginUser();
		boolean isReceiptgenerated = grievanceReceiptService
				.isReceiptgeneratedForGrievance(selectedRowGrievance.getVoucherNo());
		BigDecimal bg1 = selectedRowGrievance.getTotalAmount();

		int paraTotalAmount = generateReceiptService.getParaTotalAmount();
		BigDecimal bg2 = new BigDecimal(paraTotalAmount);
		boolean EmptyChequeOrBankReceipt = (selectedRowGrievance.getChequeOrBankReceipt() == null
				|| selectedRowGrievance.getChequeOrBankReceipt().trim().isEmpty());
		if (isReceiptgenerated) {
			setInfoMessage("Receipt already generated.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		} else if (selectedRowGrievance.getPaymentModeCodeForGr() == null
				|| selectedRowGrievance.getPaymentModeCodeForGr().trim().isEmpty()) {
			setErrorMessage("Payment Mode should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (selectedRowGrievance.getPaymentModeCodeForGr().equals("CASH")) {
			if (bg1.compareTo(bg2) == 1 && EmptyChequeOrBankReceipt) {
				setErrorMessage("Bank Receipt No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if ((selectedRowGrievance.getDepositToBankForGr() == true && EmptyChequeOrBankReceipt)) {
				setErrorMessage("Bank Receipt No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				// generateReceipt for CASH

				grievanceReceiptService.saveReceiptForGrievance(selectedRowGrievance, loginUser);

				disablePopupButtonsForDGrievance();

				generateReceiptListForGrievance = grievanceReceiptService
						.getGenerateReceiptListForGrievance(grievanceReceiptDTO);

				setSuccessMessage("Receipt generated successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			}
		} else if (selectedRowGrievance.getPaymentModeCodeForGr().equals("CHEQ")) {
			if (selectedRowGrievance.getBankCodeForGr().equals("")) {
				setErrorMessage("Bank Name should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (selectedRowGrievance.getBranchCodeForGr().equals("")) {
				setErrorMessage("Branch Name should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (EmptyChequeOrBankReceipt) {
				setErrorMessage("Cheque No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {

				grievanceReceiptService.saveReceiptForGrievance(selectedRowGrievance, loginUser);

				disablePopupButtonsForDGrievance();

				generateReceiptListForGrievance = grievanceReceiptService
						.getGenerateReceiptListForGrievance(grievanceReceiptDTO);

				setSuccessMessage("Receipt generated successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			}

		}
		String showReceiptNo = grievanceReceiptService
				.getReceiptNoForPrintForGrievance(selectedRowGrievance.getVoucherNo());
		selectedRowGrievance.setReceiptNoForGr(showReceiptNo);
		grievanceReceiptDTO.setReceiptNoForGr(showReceiptNo);

		if (showReceiptNo != null && !showReceiptNo.isEmpty() && !showReceiptNo.equals("")
				&& !showReceiptNo.equals(null)) {

			disablePopupButtonsForDGrievance();
		}

	}

	public void downloadFileForGrievance() {

		valueForPrint = selectedRowGrievance.getComplaintNo();

		files = null;
		String sourceFileName = null;
		String loginUser = sessionBackingBean.getLoginUser();

		Connection conn = null;

		sourceFileName = "..//reports//GrievanceVoucherpaymentReceipt.jrxml";

		try {
			conn = ConnectionManager.getConnection();

			String logopath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_complain_no", valueForPrint);
			parameters.put("image_path", logopath);
			parameters.put("P_login_user", loginUser);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", valueForPrint + "Receipt.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		disablePopupButtonsForDGrievance();

	}

	public void rePrintForGrievance(ActionEvent ae) throws JRException {

		valueForPrint = selectedRowGrievance.getComplaintNo();

		String loginUser = sessionBackingBean.getLoginUser();

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//GrievanceVoucherpaymentReceipt.jrxml";

		try {
			conn = ConnectionManager.getConnection();
			String imagepath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_complain_no", valueForPrint);
			parameters.put("image_path", imagepath);
			parameters.put("P_login_user", loginUser);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Receipt_reprint.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		disablePopupButtonsForDGrievance();

	}

	/** end **/

	/** Investigation Methods **/
	public void onInvestiVoucherChange() {
		ajaxShowDataDTOInves = investigationService.showDataByVoucherOrChargeSheetNo(manageInvestigation.getVoucherNo(),
				null);
		manageInvestigation.setInvReferenceNo(ajaxShowDataDTOInves.getInvReferenceNo());
		manageInvestigation.setVehicleNo(ajaxShowDataDTOInves.getVehicleNo());
		String receiptNo = null;
		receiptNo = investigationService.getReceiptNoForPrintForInvestigation(manageInvestigation.getVoucherNo());
		manageInvestigation.setReceiptNoForInves(receiptNo);

	}

	public void onInvestiComplaintChange() {

		ajaxShowDataDTOInves = investigationService.showDataByVoucherOrChargeSheetNo(null,
				manageInvestigation.getInvReferenceNo());
		manageInvestigation.setVoucherNo(ajaxShowDataDTOInves.getVoucherNo());
		manageInvestigation.setVehicleNo(ajaxShowDataDTOInves.getVehicleNo());
		String receiptNo = null;
		receiptNo = investigationService.getReceiptNoForPrintForInvestigation(manageInvestigation.getVoucherNo());
		manageInvestigation.setReceiptNoForInves(receiptNo);
	}

	public void searchDataForInves() {

		if (manageInvestigation.getVoucherNo() != null && !manageInvestigation.getVoucherNo().trim().isEmpty()) {
			if (manageInvestigation.getInvReferenceNo() != null
					&& !manageInvestigation.getInvReferenceNo().trim().isEmpty()) {
				ManageInvestigationDTO generateReceiptForInvesDTO = new ManageInvestigationDTO();
				generateReceiptForInvesDTO.setVoucherNo(manageInvestigation.getVoucherNo());
				generateReceiptForInvesDTO.setInvReferenceNo(manageInvestigation.getInvReferenceNo());
				generateReceiptForInvesDTO.setVehicleNo(manageInvestigation.getVehicleNo());
				generateReceiptForInvesDTO.setVoucherApprovedStatus("Approved");

				generateReceiptListForInves = new ArrayList<>();
				generateReceiptListForInves.add(generateReceiptForInvesDTO);

				if (generateReceiptListForInves == null) {
					setInfoMessage("No records for selected criteria.");
					RequestContext.getCurrentInstance().update("infoMSG");
					RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
				}
			} else {
				setErrorMessage("Charge Reff. No. should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Voucher No. should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void viewInvesReciept() {
		latePaymentFee = new BigDecimal(0);
		totalAmount = new BigDecimal(0);
		disablePopupButtonsForInves();
		selectedRowInves.setPaymentModeCodeForGrInves(null);
		selectedRowInves.setBankCodeForInves("");
		selectedRowInves.setBankDesForInves("");
		selectedRowInves.setBranchCodeForInves("");
		selectedRowInves.setBranchDesForInves("");
		selectedRowInves.setChequeOrBankReceipt("");
		selectedRowInves.setReceiptRemarks("");
		selectedRowInves.setDepositToBankForInves(false);
		latePaymentFee = investigationService.getLatePaymentFeeByVoucherNumer(selectedRowInves.getVoucherNo());
		boolean isReceiptgenerated = investigationService.isReceiptgeneratedForInves(selectedRowInves.getVoucherNo());
		if (isReceiptgenerated) {
			ManageInvestigationDTO receiptDetails = investigationService
					.getReceiptDetailsForInves(selectedRowInves.getVoucherNo());
			selectedRowInves.setPaymentModeCodeForGrInves(receiptDetails.getPaymentModeCodeForGrInves());
			selectedRowInves.setBankCodeForInves(receiptDetails.getBankCodeForInves());
			onBankChangeForInves();
			;
			selectedRowInves.setBranchCodeForInves(receiptDetails.getBranchCodeForInves());
			selectedRowInves.setBankDesForInves(receiptDetails.getBankDesForInves());
			selectedRowInves.setBranchDesForInves(receiptDetails.getBranchDesForInves());
			selectedRowInves.setDepositToBankForInves(receiptDetails.isDepositToBankForInves());
			selectedRowInves.setChequeOrBankReceipt(receiptDetails.getChequeOrBankReceipt());
			selectedRowInves.setReceiptRemarks(receiptDetails.getReceiptRemarks());
			selectedRowInves.setReceiptNoForInves(receiptDetails.getReceiptNoForInves());
		}

		voucherDetailsListForInves = investigationService
				.getVoucherDetailsListForInves(selectedRowInves.getInvReferenceNo());

		for (int i = 0; i < voucherDetailsListForInves.size(); i++) {

			try {
				String amtString = null;

				amtString = voucherDetailsListForInves.get(i).getVouAmmount();
				BigDecimal amt = new BigDecimal(amtString);
				totalAmount = totalAmount.add(amt);
				//selectedRowInves.setTotalAmount(totalAmount);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(latePaymentFee != null) {
		totalAmount = totalAmount.add(latePaymentFee);
		}
		selectedRowInves.setTotalAmount(totalAmount);
		
		RequestContext.getCurrentInstance().update("dialogVoucherInves");
		RequestContext.getCurrentInstance().execute("PF('voucherDialogForInves').show()");

	}

	public void disablePopupButtonsForInves() {

		boolean isReceiptgenerated = investigationService.isReceiptgeneratedForInves(selectedRowInves.getVoucherNo());

		disabledBankInves = true;
		disabledChequeBankReceiptNoInves = false;
		disabledDepositToBankInves = false;
		/* render required */
		requiredBankDisplayInves = false;
		requiredchequeReceiptNoDisplayInves = false;

		if (isReceiptgenerated) {

			disabledPaymentModeInves = true;
			disablePrintInves = true;
			disableReprintInves = false;

		} else {

			disabledPaymentModeInves = false;
			disablePrintInves = false; // --
			disableReprintInves = true;
		}

	}

	public void onBankChangeForInves() {

		branchNameList = generateReceiptService.getBranchName(selectedRowInves.getBankCodeForInves());

	}

	public void onPaymentModeChangeInves() {

		if (selectedRowInves.getPaymentModeCodeForGrInves().equals("CHEQ")) {
			selectedRowInves.setDepositToBankForInves(false);
			disabledBankInves = false;
			disabledChequeBankReceiptNoInves = false;
			disabledDepositToBankInves = true;
			requiredBankDisplayInves = true;
			requiredchequeReceiptNoDisplayInves = true;

		} else if (selectedRowInves.getPaymentModeCodeForGrInves().equals("CASH")) {
			int paraTotalAmount = generateReceiptService.getParaTotalAmount();
			int tot = grievanceReceiptService.getTotalAmount(selectedRowInves.getVoucherNo());
			BigDecimal bg1 = new BigDecimal(tot);

			BigDecimal bg2 = new BigDecimal(paraTotalAmount);

			selectedRowInves.setBankDesForInves(null);
			selectedRowInves.setBankCodeForInves(null);
			selectedRowInves.setBranchCodeForInves(null);
			selectedRowInves.setBranchDesForInves(null);
			disabledDepositToBankInves = false;
			disabledChequeBankReceiptNoInves = false;
			disabledBankInves = true;
			requiredchequeReceiptNoDisplayInves = false;

			if (bg1.compareTo(bg2) == 1) {
				selectedRowInves.setDepositToBankForInves(true);
				disabledDepositToBankInves = true;
				requiredchequeReceiptNoDisplayInves = true;
			}
		} else {
			disabledBankInves = true;
			disabledChequeBankReceiptNoInves = false;
			disabledDepositToBankInves = false;
			requiredBankDisplayInves = false;
			requiredchequeReceiptNoDisplayInves = false;
			selectedRowInves.setDepositToBankForInves(false);
			selectedRowInves.setBankDesForInves(null);
			selectedRowInves.setBankCodeForInves(null);
			selectedRowInves.setBranchCodeForInves(null);
			selectedRowInves.setBranchDesForInves(null);

			selectedRowInves.setChequeOrBankReceipt(null);
		}

	}

	public void ifDepositToBankForInves() {

		if (selectedRowInves.isDepositToBankForInves() == true) {
			requiredchequeReceiptNoDisplayInves = true;
		} else {
			requiredchequeReceiptNoDisplayInves = false;
		}

	}

	public void generateReceiptForInves() {

		String loginUser = sessionBackingBean.getLoginUser();
		boolean isReceiptgenerated = investigationService.isReceiptgeneratedForInves(selectedRowInves.getVoucherNo());
		BigDecimal bg1 = selectedRowInves.getTotalAmount();

		int paraTotalAmount = generateReceiptService.getParaTotalAmount();
		BigDecimal bg2 = new BigDecimal(paraTotalAmount);
		boolean EmptyChequeOrBankReceipt = (selectedRowInves.getChequeOrBankReceipt() == null
				|| selectedRowInves.getChequeOrBankReceipt().trim().isEmpty());
		if (isReceiptgenerated) {
			setInfoMessage("Receipt already generated.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		} else if (selectedRowInves.getPaymentModeCodeForGrInves() == null
				|| selectedRowInves.getPaymentModeCodeForGrInves().trim().isEmpty()) {
			setErrorMessage("Payment Mode should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (selectedRowInves.getPaymentModeCodeForGrInves().equals("CASH")) {
			if (bg1.compareTo(bg2) == 1 && EmptyChequeOrBankReceipt) {
				setErrorMessage("Bank Receipt No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if ((selectedRowInves.isDepositToBankForInves() == true && EmptyChequeOrBankReceipt)) {
				setErrorMessage("Bank Receipt No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {

				investigationService.saveReceiptForInvestigation(selectedRowInves, loginUser);

				disablePopupButtonsForInves();

//				ManageInvestigationDTO generateReceiptForInvesDTO = new ManageInvestigationDTO();
//				generateReceiptForInvesDTO.setVoucherNo(selectedRowInves.getVoucherNo());
//				generateReceiptForInvesDTO.setInvReferenceNo(selectedRowInves.getInvReferenceNo());
//				generateReceiptForInvesDTO.setVehicleNo(selectedRowInves.getVehicleNo());
//				generateReceiptForInvesDTO.setVoucherApprovedStatus("Approved");
//
//				generateReceiptListForInves = new ArrayList<>();
//				generateReceiptListForInves.add(generateReceiptForInvesDTO);

				generateReceiptListForInves = investigationService.getPendingGenerateReceiptListForInves();

				setSuccessMessage("Receipt generated successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				/** update task as RG in charge_master table **/
				investigationService.updateCurrentStatus(manageInvestigation.getInvReferenceNo(), "RG", loginUser);
			}
		} else if (selectedRowInves.getPaymentModeCodeForGrInves().equals("CHEQ")) {
			if (selectedRowInves.getBankCodeForInves().equals("")) {
				setErrorMessage("Bank Name should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (selectedRowInves.getBranchCodeForInves().equals("")) {
				setErrorMessage("Branch Name should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (EmptyChequeOrBankReceipt) {
				setErrorMessage("Cheque No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {

				investigationService.saveReceiptForInvestigation(selectedRowInves, loginUser);

				disablePopupButtonsForInves();

//				ManageInvestigationDTO generateReceiptForInvesDTO = new ManageInvestigationDTO();
//				generateReceiptForInvesDTO.setVoucherNo(selectedRowInves.getVoucherNo());
//				generateReceiptForInvesDTO.setInvReferenceNo(selectedRowInves.getInvReferenceNo());
//				generateReceiptForInvesDTO.setVehicleNo(selectedRowInves.getVehicleNo());
//				generateReceiptForInvesDTO.setVoucherApprovedStatus("Approved");
//
//				generateReceiptListForInves = new ArrayList<>();
//				generateReceiptListForInves.add(generateReceiptForInvesDTO);
				
				generateReceiptListForInves = investigationService.getPendingGenerateReceiptListForInves();

				setSuccessMessage("Receipt generated successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				/** update task as RG in charge_master table **/
				investigationService.updateCurrentStatus(selectedInvestigation, manageInvestigation.getInvReferenceNo(), "RG", loginUser, null);

			}

		}
		String showReceiptNo = investigationService
				.getReceiptNoForPrintForInvestigation(selectedRowInves.getVoucherNo());
		selectedRowInves.setReceiptNoForInves(showReceiptNo);
		selectedRowInves.setReceiptNoForInves(showReceiptNo);

		if (showReceiptNo != null && !showReceiptNo.isEmpty() && !showReceiptNo.equals("")
				&& !showReceiptNo.equals(null)) {

			disablePopupButtonsForInves();
		}

	}

	public void rePrintForInvesti(ActionEvent ae) throws JRException {

		valueForPrint = selectedRowInves.getInvReferenceNo();
		String loginUser = sessionBackingBean.getLoginUser();

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//InvestigationVoucherpaymentReceipt.jrxml";

		try {
			conn = ConnectionManager.getConnection();
			String imagepath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_charge_Ref", valueForPrint);
			parameters.put("image_path", imagepath);
			parameters.put("P_login_user", loginUser);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Receipt_reprint.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		disablePopupButtonsForInves();

	}

	public void clearForInves() {
		selectedRowInves = new ManageInvestigationDTO();
		manageInvestigation = new ManageInvestigationDTO();

		approvedVoucherForInves = investigationService.approvedVoucherForInves();
		chargeRefNoList = investigationService.approvedChargeRefNO();
		generateReceiptListForInves = investigationService.getPendingGenerateReceiptListForInves();
	}

	/** end **/

	/** SIM Registration Methods **/
	public void onVouChange() {
		onchangeDTO = simRegistrationService.filterDTO(null, null, null, null, null, simregDTO.getVouNo());
		simregDTO.setSimRegNo(onchangeDTO.getSimRegNo());
		simregDTO.setReceiptNo(onchangeDTO.getReceiptNo());
		simregDTO.setPermitNo(onchangeDTO.getPermitNo());

	}

	public void onSimNoChange() {
		onchangeDTO = simRegistrationService.filterDTO(simregDTO.getSimRegNo(), null, null, null, null, null);
		simregDTO.setVouNo(onchangeDTO.getVouNo());
		simregDTO.setReceiptNo(onchangeDTO.getReceiptNo());
		simregDTO.setPermitNo(onchangeDTO.getPermitNo());

	}

	public void onPermitChange() {
		onchangeDTO = simRegistrationService.filterDTO(null, null, null, null, simregDTO.getPermitNo(), null);
		simregDTO.setVouNo(onchangeDTO.getVouNo());
		simregDTO.setReceiptNo(onchangeDTO.getReceiptNo());
		simregDTO.setSimRegNo(onchangeDTO.getSimRegNo());
	}

	public void searchDataForSimReg() {
		if ((simregDTO.getVouNo() != null && !simregDTO.getVouNo().trim().isEmpty())
				|| (simregDTO.getSimRegNo() != null && !simregDTO.getSimRegNo().trim().isEmpty())) {

			generateReceiptListForSimReg = simRegistrationService.searchedList(simregDTO.getSimRegNo(), null, null,
					null, simregDTO.getPermitNo(), simregDTO.getVouNo(), null);
			if (generateReceiptListForSimReg == null) {
				setErrorMessage("No data for searched fields.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {

			}
		} else {

			setErrorMessage("Please select a field for search.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearForSimReg() {
		simregDTO = new SimRegistrationDTO();
		generateReceiptListForSimReg = simRegistrationService.pendingDTO(null, null, null, null, null, null, "A");

	}

	public void viewSimRegReciept() {
		disablePopupButtonsForSimReg();
		selectedRowSimReg.setPaymentModeCodeForSimReg(null);
		selectedRowSimReg.setBankCodeForSimReg("");
		selectedRowSimReg.setBankDesForSimReg("");
		selectedRowSimReg.setBranchCodeForSimReg("");
		selectedRowSimReg.setBranchDesForSimReg("");
		selectedRowSimReg.setChequeOrBankReceipt("");
		selectedRowSimReg.setReceiptRemarks("");
		selectedRowSimReg.setDepositToBankForSimReg(false);

		boolean isReceiptgenerated = simRegistrationService.isReceiptgeneratedForInves(selectedRowSimReg.getVouNo());
		if (isReceiptgenerated) {
			SimRegistrationDTO receiptDetails = simRegistrationService
					.getReceiptDetailsForInves(selectedRowSimReg.getVouNo());
			selectedRowSimReg.setPaymentModeCodeForSimReg(receiptDetails.getPaymentModeCodeForSimReg());
			selectedRowSimReg.setBankCodeForSimReg(receiptDetails.getBankCodeForSimReg());
			onBankChangeForSimReg();
			selectedRowSimReg.setBranchCodeForSimReg(receiptDetails.getBranchCodeForSimReg());
			selectedRowSimReg.setBankDesForSimReg(receiptDetails.getBankDesForSimReg());
			selectedRowSimReg.setBranchDesForSimReg(receiptDetails.getBranchDesForSimReg());
			selectedRowSimReg.setDepositToBankForSimReg(receiptDetails.isDepositToBankForSimReg());
			selectedRowSimReg.setChequeOrBankReceipt(receiptDetails.getChequeOrBankReceipt());
			selectedRowSimReg.setReceiptRemarks(receiptDetails.getReceiptRemarks());
			selectedRowSimReg.setReceiptNo(receiptDetails.getReceiptNo());
		}

		voucherDetailsListForSimReg = simRegistrationService.pendingList(null, null, null, null, null,
				selectedRowSimReg.getVouNo(), "A");
		totalAmount = new BigDecimal(0);

		for (int i = 0; i < voucherDetailsListForSimReg.size(); i++) {

			try {

				BigDecimal amt = new BigDecimal(voucherDetailsListForSimReg.get(i).getVouAmmount());
				totalAmount = totalAmount.add(amt);
				selectedRowSimReg.setTotalAmount(totalAmount);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		RequestContext.getCurrentInstance().update("dialogVoucherSimReg");
		RequestContext.getCurrentInstance().execute("PF('voucherDialogForSimReg').show()");

	}

	public void onPaymentModeChangeSimReg() {

		if (selectedRowSimReg.getPaymentModeCodeForSimReg().equals("CHEQ")) {
			selectedRowSimReg.setDepositToBankForSimReg(false);
			disabledBankSimReg = false;
			disabledChequeBankReceiptNoSimReg = false;
			disabledDepositToBankSimReg = true;
			requiredBankDisplaySimReg = true;
			requiredchequeReceiptNoDisplaySimReg = true;

		} else if (selectedRowSimReg.getPaymentModeCodeForSimReg().equals("CASH")) {
			int paraTotalAmount = generateReceiptService.getParaTotalAmount();
			int tot = simRegistrationService.getTotalAmount(selectedRowSimReg.getVouNo());
			BigDecimal bg1 = new BigDecimal(tot);

			BigDecimal bg2 = new BigDecimal(paraTotalAmount);

			selectedRowSimReg.setBankDesForSimReg(null);
			selectedRowSimReg.setBankCodeForSimReg(null);
			selectedRowSimReg.setBranchCodeForSimReg(null);
			selectedRowSimReg.setBranchDesForSimReg(null);
			disabledDepositToBankSimReg = false;
			disabledChequeBankReceiptNoSimReg = false;
			disabledBankSimReg = true;
			requiredchequeReceiptNoDisplaySimReg = false;

			if (bg1.compareTo(bg2) == 1) {
				selectedRowSimReg.setDepositToBankForSimReg(true);

				disabledDepositToBankSimReg = true;
				requiredchequeReceiptNoDisplaySimReg = true;
			}
		} else {
			disabledBankSimReg = true;
			disabledChequeBankReceiptNoSimReg = false;
			disabledDepositToBankSimReg = false;
			requiredBankDisplaySimReg = false;
			requiredchequeReceiptNoDisplaySimReg = false;
			selectedRowSimReg.setDepositToBankForSimReg(false);
			;
			selectedRowSimReg.setBankDesForSimReg(null);
			selectedRowSimReg.setBankCodeForSimReg(null);
			selectedRowSimReg.setBranchCodeForSimReg(null);
			selectedRowSimReg.setBranchDesForSimReg(null);

			selectedRowSimReg.setChequeOrBankReceipt(null);
		}

	}

	public void onBankChangeForSimReg() {

		branchNameList = generateReceiptService.getBranchName(selectedRowSimReg.getBankCodeForSimReg());
	}

	public void disablePopupButtonsForSimReg() {

		boolean isReceiptgenerated = simRegistrationService.isReceiptgeneratedForInves(selectedRowSimReg.getVouNo());

		disabledBankSimReg = true;
		disabledChequeBankReceiptNoSimReg = false;
		disabledDepositToBankSimReg = false;
		/* render required */
		requiredBankDisplaySimReg = false;
		requiredchequeReceiptNoDisplaySimReg = false;

		if (isReceiptgenerated) {

			disabledPaymentModeSimReg = true;
			disablePrintSimReg = true;
			disableReprintSimReg = false;

		} else {

			disabledPaymentModeSimReg = false;
			disablePrintSimReg = false; // --
			disableReprintSimReg = true;
		}

	}

	public void generateReceiptForSimReg() {

		String loginUser = sessionBackingBean.getLoginUser();
		boolean isReceiptgenerated = simRegistrationService.isReceiptgeneratedForInves(selectedRowSimReg.getVouNo());
		BigDecimal bg1 = selectedRowSimReg.getTotalAmount();

		int paraTotalAmount = generateReceiptService.getParaTotalAmount();
		BigDecimal bg2 = new BigDecimal(paraTotalAmount);
		boolean EmptyChequeOrBankReceipt = (selectedRowSimReg.getChequeOrBankReceipt() == null
				|| selectedRowSimReg.getChequeOrBankReceipt().trim().isEmpty());
		if (isReceiptgenerated) {
			setInfoMessage("Receipt already generated.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
		} else if (selectedRowSimReg.getPaymentModeCodeForSimReg() == null
				|| selectedRowSimReg.getPaymentModeCodeForSimReg().trim().isEmpty()) {
			setErrorMessage("Payment Mode should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (selectedRowSimReg.getPaymentModeCodeForSimReg().equals("CASH")) {
			if (bg1.compareTo(bg2) == 1 && EmptyChequeOrBankReceipt) {
				setErrorMessage("Bank Receipt No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if ((selectedRowSimReg.isDepositToBankForSimReg() == true && EmptyChequeOrBankReceipt)) {
				setErrorMessage("Bank Receipt No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				// generateReceipt for CASH

				simRegistrationService.saveReceiptForSim(selectedRowSimReg, loginUser);

				disablePopupButtonsForSimReg();

				setSuccessMessage("Receipt generated successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				/** update task as RG in table **/
				simRegistrationService.updateStatusType("RG", selectedRowSimReg.getSimRegNo());
				/** Active Sim by update A in status **/
				String showReceiptNo = simRegistrationService.getReceiptNo(selectedRowSimReg.getVouNo());
				simRegistrationService.activeSim("A", selectedRowSimReg.getSimRegNo(), showReceiptNo);
			}
		} else if (selectedRowSimReg.getPaymentModeCodeForSimReg().equals("CHEQ")) {
			if (selectedRowSimReg.getBankCodeForSimReg().equals("")) {
				setErrorMessage("Bank Name should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (selectedRowSimReg.getBranchCodeForSimReg().equals("")) {
				setErrorMessage("Branch Name should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else if (EmptyChequeOrBankReceipt) {
				setErrorMessage("Cheque No. should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {

				simRegistrationService.saveReceiptForSim(selectedRowSimReg, loginUser);

				disablePopupButtonsForSimReg();

				setSuccessMessage("Receipt generated successfully.");
				RequestContext.getCurrentInstance().update("successMSG");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				/** update task as RG in table **/
				simRegistrationService.updateStatusType("RG", selectedRowSimReg.getSimRegNo());
				/** Active Sim by update A in status **/
				String showReceiptNo = simRegistrationService.getReceiptNo(selectedRowSimReg.getVouNo());
				simRegistrationService.activeSim("A", selectedRowSimReg.getSimRegNo(), showReceiptNo);

			}

		}

		String showReceiptNo = simRegistrationService.getReceiptNo(selectedRowSimReg.getVouNo());

		selectedRowSimReg.setReceiptNo(showReceiptNo);
		selectedRowSimReg.setReceiptNo(showReceiptNo);

		// update receipt no in voucher table
		simRegistrationService.updateReceiptNo(selectedRowSimReg.getVouNo(), showReceiptNo);
		if (showReceiptNo != null && !showReceiptNo.isEmpty() && !showReceiptNo.equals("")
				&& !showReceiptNo.equals(null)) {

			isReceiptgenerated = true;
			disablePopupButtonsForSimReg();
			;
		}

	}

	public void rePrintForSimReg(ActionEvent ae) throws JRException {

		valueForPrint = selectedRowSimReg.getSimRegNo();

		String loginUser = sessionBackingBean.getLoginUser();

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//SimRegistrationVoucherpaymentReceipt.jrxml";

		try {
			conn = ConnectionManager.getConnection();
			String imagepath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_SIm_Reg", valueForPrint);
			parameters.put("image_path", imagepath);
			parameters.put("P_login_user", loginUser);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Receipt_reprint.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		disablePopupButtonsForSimReg();
		;

	}

	/** end **/
	public boolean isCallnext() {
		return callnext;
	}

	public void setCallnext(boolean callnext) {
		this.callnext = callnext;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public List<CommonDTO> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<CommonDTO> counterList) {
		this.counterList = counterList;
	}

	public CommonDTO getCommonDTO() {
		return commonDTO;
	}

	public void setCommonDTO(CommonDTO commonDTO) {
		this.commonDTO = commonDTO;
	}

	public List<GenerateReceiptDTO> getBankNameList() {
		return bankNameList;
	}

	public void setBankNameList(List<GenerateReceiptDTO> bankNameList) {
		this.bankNameList = bankNameList;
	}

	public List<GenerateReceiptDTO> getBranchNameList() {
		return branchNameList;
	}

	public void setBranchNameList(List<GenerateReceiptDTO> branchNameList) {
		this.branchNameList = branchNameList;
	}

	public List<GenerateReceiptDTO> getPaymentModeList() {
		return paymentModeList;
	}

	public void setPaymentModeList(List<GenerateReceiptDTO> paymentModeList) {
		this.paymentModeList = paymentModeList;
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

	public GenerateReceiptDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(GenerateReceiptDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public List<GenerateReceiptDTO> getGenerateReceiptList() {
		return generateReceiptList;
	}

	public void setGenerateReceiptList(List<GenerateReceiptDTO> generateReceiptList) {
		this.generateReceiptList = generateReceiptList;
	}

	public List<GenerateReceiptDTO> getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(List<GenerateReceiptDTO> applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public GenerateReceiptDTO getGenerateReceiptDTO() {
		return generateReceiptDTO;
	}

	public void setGenerateReceiptDTO(GenerateReceiptDTO generateReceiptDTO) {
		this.generateReceiptDTO = generateReceiptDTO;
	}

	public List<GenerateReceiptDTO> getTransactionTypeList() {
		return transactionTypeList;
	}

	public void setTransactionTypeList(List<GenerateReceiptDTO> transactionTypeList) {
		this.transactionTypeList = transactionTypeList;
	}

	public List<GenerateReceiptDTO> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<GenerateReceiptDTO> departmentList) {
		this.departmentList = departmentList;
	}

	public List<GenerateReceiptDTO> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<GenerateReceiptDTO> unitList) {
		this.unitList = unitList;
	}

	public List<GenerateReceiptDTO> getReceiptNoList() {
		return receiptNoList;
	}

	public void setReceiptNoList(List<GenerateReceiptDTO> receiptNoList) {
		this.receiptNoList = receiptNoList;
	}

	public List<GenerateReceiptDTO> getVoucherNoList() {
		return voucherNoList;
	}

	public void setVoucherNoList(List<GenerateReceiptDTO> voucherNoList) {
		this.voucherNoList = voucherNoList;
	}

	public List<AdvancedPaymentDTO> getAdvancedPaymentDetailsList() {
		return advancedPaymentDetailsList;
	}

	public void setAdvancedPaymentDetailsList(List<AdvancedPaymentDTO> advancedPaymentDetailsList) {
		this.advancedPaymentDetailsList = advancedPaymentDetailsList;
	}

	public List<PaymentVoucherDTO> getVoucherDetailsList() {
		return voucherDetailsList;
	}

	public void setVoucherDetailsList(List<PaymentVoucherDTO> voucherDetailsList) {
		this.voucherDetailsList = voucherDetailsList;
	}

	public boolean isDisabledBank() {
		return disabledBank;
	}

	public void setDisabledBank(boolean disabledBank) {
		this.disabledBank = disabledBank;
	}

	public boolean isDisabledChequeBankReceiptNo() {
		return disabledChequeBankReceiptNo;
	}

	public void setDisabledChequeBankReceiptNo(boolean disabledChequeBankReceiptNo) {
		this.disabledChequeBankReceiptNo = disabledChequeBankReceiptNo;
	}

	public boolean isDisabledDepositToBank() {
		return disabledDepositToBank;
	}

	public void setDisabledDepositToBank(boolean disabledDepositToBank) {
		this.disabledDepositToBank = disabledDepositToBank;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isDisableGenarate() {
		return disableGenarate;
	}

	public void setDisableGenarate(boolean disableGenarate) {
		this.disableGenarate = disableGenarate;
	}

	public boolean isDisablePrint() {
		return disablePrint;
	}

	public void setDisablePrint(boolean disablePrint) {
		this.disablePrint = disablePrint;
	}

	public boolean isDisableReprint() {
		return disableReprint;
	}

	public void setDisableReprint(boolean disableReprint) {
		this.disableReprint = disableReprint;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public String getValueForPrint() {
		return valueForPrint;
	}

	public void setValueForPrint(String valueForPrint) {
		this.valueForPrint = valueForPrint;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public boolean isDisabledPaymentMode() {
		return disabledPaymentMode;
	}

	public void setDisabledPaymentMode(boolean disabledPaymentMode) {
		this.disabledPaymentMode = disabledPaymentMode;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public boolean isRequiredBankDisplay() {
		return requiredBankDisplay;
	}

	public void setRequiredBankDisplay(boolean requiredBankDisplay) {
		this.requiredBankDisplay = requiredBankDisplay;
	}

	public boolean isRequiredchequeReceiptNoDisplay() {
		return requiredchequeReceiptNoDisplay;
	}

	public void setRequiredchequeReceiptNoDisplay(boolean requiredchequeReceiptNoDisplay) {
		this.requiredchequeReceiptNoDisplay = requiredchequeReceiptNoDisplay;
	}

	// getters setters for Driver Conductor Receipt Generator

	public DriverConductorRegistrationDTO getDriverConductorReceiptDTO() {
		return driverConductorReceiptDTO;
	}

	public void setDriverConductorReceiptDTO(DriverConductorRegistrationDTO driverConductorReceiptDTO) {
		this.driverConductorReceiptDTO = driverConductorReceiptDTO;
	}

	public List<DriverConductorRegistrationDTO> getDriverConductorVoucherNoList() {
		return driverConductorVoucherNoList;
	}

	public void setDriverConductorVoucherNoList(List<DriverConductorRegistrationDTO> driverConductorVoucherNoList) {
		this.driverConductorVoucherNoList = driverConductorVoucherNoList;
	}

	public List<DriverConductorRegistrationDTO> getDriverConductorapplicationNoList() {
		return driverConductorapplicationNoList;
	}

	public void setDriverConductorapplicationNoList(
			List<DriverConductorRegistrationDTO> driverConductorapplicationNoList) {
		this.driverConductorapplicationNoList = driverConductorapplicationNoList;
	}

	public List<DriverConductorRegistrationDTO> getDriverConductorDcIdList() {
		return driverConductorDcIdList;
	}

	public void setDriverConductorDcIdList(List<DriverConductorRegistrationDTO> driverConductorDcIdList) {
		this.driverConductorDcIdList = driverConductorDcIdList;
	}

	public List<DriverConductorRegistrationDTO> getGenerateReceiptListForDriverConductor() {
		return generateReceiptListForDriverConductor;
	}

	public void setGenerateReceiptListForDriverConductor(
			List<DriverConductorRegistrationDTO> generateReceiptListForDriverConductor) {
		this.generateReceiptListForDriverConductor = generateReceiptListForDriverConductor;
	}

	public DriverConductorRegistrationDTO getSelectedRowDriverConductor() {
		return selectedRowDriverConductor;
	}

	public void setSelectedRowDriverConductor(DriverConductorRegistrationDTO selectedRowDriverConductor) {
		this.selectedRowDriverConductor = selectedRowDriverConductor;
	}

	public boolean isDisabledPaymentModeDC() {
		return disabledPaymentModeDC;
	}

	public void setDisabledPaymentModeDC(boolean disabledPaymentModeDC) {
		this.disabledPaymentModeDC = disabledPaymentModeDC;
	}

	public boolean isRequiredBankDisplayDC() {
		return requiredBankDisplayDC;
	}

	public void setRequiredBankDisplayDC(boolean requiredBankDisplayDC) {
		this.requiredBankDisplayDC = requiredBankDisplayDC;
	}

	public boolean isDisabledBankDC() {
		return disabledBankDC;
	}

	public void setDisabledBankDC(boolean disabledBankDC) {
		this.disabledBankDC = disabledBankDC;
	}

	public boolean isDisabledDepositToBankDC() {
		return disabledDepositToBankDC;
	}

	public void setDisabledDepositToBankDC(boolean disabledDepositToBankDC) {
		this.disabledDepositToBankDC = disabledDepositToBankDC;
	}

	public boolean isRequiredchequeReceiptNoDisplayDC() {
		return requiredchequeReceiptNoDisplayDC;
	}

	public void setRequiredchequeReceiptNoDisplayDC(boolean requiredchequeReceiptNoDisplayDC) {
		this.requiredchequeReceiptNoDisplayDC = requiredchequeReceiptNoDisplayDC;
	}

	public boolean isDisabledChequeBankReceiptNoDC() {
		return disabledChequeBankReceiptNoDC;
	}

	public void setDisabledChequeBankReceiptNoDC(boolean disabledChequeBankReceiptNoDC) {
		this.disabledChequeBankReceiptNoDC = disabledChequeBankReceiptNoDC;
	}

	public boolean isDisableGenarateDC() {
		return disableGenarateDC;
	}

	public void setDisableGenarateDC(boolean disableGenarateDC) {
		this.disableGenarateDC = disableGenarateDC;
	}

	public boolean isDisablePrintDC() {
		return disablePrintDC;
	}

	public void setDisablePrintDC(boolean disablePrintDC) {
		this.disablePrintDC = disablePrintDC;
	}

	public boolean isDisableReprintDC() {
		return disableReprintDC;
	}

	public void setDisableReprintDC(boolean disableReprintDC) {
		this.disableReprintDC = disableReprintDC;
	}

	public List<DriverConductorRegistrationDTO> getVoucherDetailsListForDC() {
		return voucherDetailsListForDC;
	}

	public void setVoucherDetailsListForDC(List<DriverConductorRegistrationDTO> voucherDetailsListForDC) {
		this.voucherDetailsListForDC = voucherDetailsListForDC;
	}

	public DriverConductorTrainingService getDriverConductorService() {
		return driverConductorService;
	}

	public void setDriverConductorService(DriverConductorTrainingService driverConductorService) {
		this.driverConductorService = driverConductorService;
	}

	/*** getters setters for Grievance Management Receipt **/

	public ComplaintRequestDTO getGrievanceReceiptDTO() {
		return grievanceReceiptDTO;
	}

	public void setGrievanceReceiptDTO(ComplaintRequestDTO grievanceReceiptDTO) {
		this.grievanceReceiptDTO = grievanceReceiptDTO;
	}

	public GrievanceManagementService getReceiptGrievanceManagement() {
		return receiptGrievanceManagement;
	}

	public void setReceiptGrievanceManagement(GrievanceManagementService receiptGrievanceManagement) {
		this.receiptGrievanceManagement = receiptGrievanceManagement;
	}

	public GenerateReceiptService getGenerateReceiptService() {
		return generateReceiptService;
	}

	public void setGenerateReceiptService(GenerateReceiptService generateReceiptService) {
		this.generateReceiptService = generateReceiptService;
	}

	public GrievanceManagementService getGrievanceReceiptService() {
		return grievanceReceiptService;
	}

	public void setGrievanceReceiptService(GrievanceManagementService grievanceReceiptService) {
		this.grievanceReceiptService = grievanceReceiptService;
	}

	public List<String> getApprovedVoucherNoList() {
		return approvedVoucherNoList;
	}

	public void setApprovedVoucherNoList(List<String> approvedVoucherNoList) {
		this.approvedVoucherNoList = approvedVoucherNoList;
	}

	public List<String> getComplaintNoList() {
		return complaintNoList;
	}

	public void setComplaintNoList(List<String> complaintNoList) {
		this.complaintNoList = complaintNoList;
	}

	public ComplaintRequestDTO getAjaxShowDataDTO() {
		return ajaxShowDataDTO;
	}

	public void setAjaxShowDataDTO(ComplaintRequestDTO ajaxShowDataDTO) {
		this.ajaxShowDataDTO = ajaxShowDataDTO;
	}

	public List<ComplaintRequestDTO> getGenerateReceiptListForGrievance() {
		return generateReceiptListForGrievance;
	}

	public void setGenerateReceiptListForGrievance(List<ComplaintRequestDTO> generateReceiptListForGrievance) {
		this.generateReceiptListForGrievance = generateReceiptListForGrievance;
	}

	public ComplaintRequestDTO getSelectedRowGrievance() {
		return selectedRowGrievance;
	}

	public void setSelectedRowGrievance(ComplaintRequestDTO selectedRowGrievance) {
		this.selectedRowGrievance = selectedRowGrievance;
	}

	public boolean isDisabledPaymentModeGr() {
		return disabledPaymentModeGr;
	}

	public void setDisabledPaymentModeGr(boolean disabledPaymentModeGr) {
		this.disabledPaymentModeGr = disabledPaymentModeGr;
	}

	public boolean isRequiredBankDisplayGr() {
		return requiredBankDisplayGr;
	}

	public void setRequiredBankDisplayGr(boolean requiredBankDisplayGr) {
		this.requiredBankDisplayGr = requiredBankDisplayGr;
	}

	public boolean isDisabledBankGr() {
		return disabledBankGr;
	}

	public void setDisabledBankGr(boolean disabledBankGr) {
		this.disabledBankGr = disabledBankGr;
	}

	public boolean isDisabledDepositToBankGr() {
		return disabledDepositToBankGr;
	}

	public void setDisabledDepositToBankGr(boolean disabledDepositToBankGr) {
		this.disabledDepositToBankGr = disabledDepositToBankGr;
	}

	public boolean isRequiredchequeReceiptNoDisplayGr() {
		return requiredchequeReceiptNoDisplayGr;
	}

	public void setRequiredchequeReceiptNoDisplayGr(boolean requiredchequeReceiptNoDisplayGr) {
		this.requiredchequeReceiptNoDisplayGr = requiredchequeReceiptNoDisplayGr;
	}

	public boolean isDisabledChequeBankReceiptNoGr() {
		return disabledChequeBankReceiptNoGr;
	}

	public void setDisabledChequeBankReceiptNoGr(boolean disabledChequeBankReceiptNoGr) {
		this.disabledChequeBankReceiptNoGr = disabledChequeBankReceiptNoGr;
	}

	public boolean isDisableGenarateGr() {
		return disableGenarateGr;
	}

	public void setDisableGenarateGr(boolean disableGenarateGr) {
		this.disableGenarateGr = disableGenarateGr;
	}

	public boolean isDisablePrintGr() {
		return disablePrintGr;
	}

	public void setDisablePrintGr(boolean disablePrintGr) {
		this.disablePrintGr = disablePrintGr;
	}

	public boolean isDisableReprintGr() {
		return disableReprintGr;
	}

	public void setDisableReprintGr(boolean disableReprintGr) {
		this.disableReprintGr = disableReprintGr;
	}

	public List<ComplaintRequestDTO> getVoucherDetailsListForGr() {
		return voucherDetailsListForGr;
	}

	public void setVoucherDetailsListForGr(List<ComplaintRequestDTO> voucherDetailsListForGr) {
		this.voucherDetailsListForGr = voucherDetailsListForGr;
	}

	/** End **/

	/*** getters setters for Investigation Receipt **/
	public ManageInvestigationDTO getManageInvestigation() {
		return manageInvestigation;
	}

	public void setManageInvestigation(ManageInvestigationDTO manageInvestigation) {
		this.manageInvestigation = manageInvestigation;
	}

	public List<ManageInvestigationDTO> getApprovedVoucherForInves() {
		return approvedVoucherForInves;
	}

	public void setApprovedVoucherForInves(List<ManageInvestigationDTO> approvedVoucherForInves) {
		this.approvedVoucherForInves = approvedVoucherForInves;
	}

	public ManageInvestigationService getInvestigationService() {
		return investigationService;
	}

	public void setInvestigationService(ManageInvestigationService investigationService) {
		this.investigationService = investigationService;
	}

	public List<ManageInvestigationDTO> getChargeRefNoList() {
		return chargeRefNoList;
	}

	public void setChargeRefNoList(List<ManageInvestigationDTO> chargeRefNoList) {
		this.chargeRefNoList = chargeRefNoList;
	}

	public ManageInvestigationDTO getAjaxShowDataDTOInves() {
		return ajaxShowDataDTOInves;
	}

	public void setAjaxShowDataDTOInves(ManageInvestigationDTO ajaxShowDataDTOInves) {
		this.ajaxShowDataDTOInves = ajaxShowDataDTOInves;
	}

	public List<ManageInvestigationDTO> getGenerateReceiptListForInves() {
		return generateReceiptListForInves;
	}

	public void setGenerateReceiptListForInves(List<ManageInvestigationDTO> generateReceiptListForInves) {
		this.generateReceiptListForInves = generateReceiptListForInves;
	}

	public ManageInvestigationDTO getSelectedRowInves() {
		return selectedRowInves;
	}

	public void setSelectedRowInves(ManageInvestigationDTO selectedRowInves) {
		this.selectedRowInves = selectedRowInves;
	}

	public List<ManageInvestigationDTO> getVoucherDetailsListForInves() {
		return voucherDetailsListForInves;
	}

	public void setVoucherDetailsListForInves(List<ManageInvestigationDTO> voucherDetailsListForInves) {
		this.voucherDetailsListForInves = voucherDetailsListForInves;
	}

	public boolean isDisabledPaymentModeInves() {
		return disabledPaymentModeInves;
	}

	public void setDisabledPaymentModeInves(boolean disabledPaymentModeInves) {
		this.disabledPaymentModeInves = disabledPaymentModeInves;
	}

	public boolean isRequiredBankDisplayInves() {
		return requiredBankDisplayInves;
	}

	public void setRequiredBankDisplayInves(boolean requiredBankDisplayInves) {
		this.requiredBankDisplayInves = requiredBankDisplayInves;
	}

	public boolean isDisabledBankInves() {
		return disabledBankInves;
	}

	public void setDisabledBankInves(boolean disabledBankInves) {
		this.disabledBankInves = disabledBankInves;
	}

	public boolean isDisabledDepositToBankInves() {
		return disabledDepositToBankInves;
	}

	public void setDisabledDepositToBankInves(boolean disabledDepositToBankInves) {
		this.disabledDepositToBankInves = disabledDepositToBankInves;
	}

	public boolean isRequiredchequeReceiptNoDisplayInves() {
		return requiredchequeReceiptNoDisplayInves;
	}

	public void setRequiredchequeReceiptNoDisplayInves(boolean requiredchequeReceiptNoDisplayInves) {
		this.requiredchequeReceiptNoDisplayInves = requiredchequeReceiptNoDisplayInves;
	}

	public boolean isDisabledChequeBankReceiptNoInves() {
		return disabledChequeBankReceiptNoInves;
	}

	public void setDisabledChequeBankReceiptNoInves(boolean disabledChequeBankReceiptNoInves) {
		this.disabledChequeBankReceiptNoInves = disabledChequeBankReceiptNoInves;
	}

	public boolean isDisablePrintInves() {
		return disablePrintInves;
	}

	public void setDisablePrintInves(boolean disablePrintInves) {
		this.disablePrintInves = disablePrintInves;
	}

	public boolean isDisableReprintInves() {
		return disableReprintInves;
	}

	public void setDisableReprintInves(boolean disableReprintInves) {
		this.disableReprintInves = disableReprintInves;
	}

	/** End **/

	/** SIM Registration **/

	public SimRegistrationService getSimRegistrationService() {
		return simRegistrationService;
	}

	public void setSimRegistrationService(SimRegistrationService simRegistrationService) {
		this.simRegistrationService = simRegistrationService;
	}

	public SimRegistrationDTO getSimregDTO() {
		return simregDTO;
	}

	public void setSimregDTO(SimRegistrationDTO simregDTO) {
		this.simregDTO = simregDTO;
	}

	public List<SimRegistrationDTO> getApprovedVoucherForSimReg() {
		return approvedVoucherForSimReg;
	}

	public void setApprovedVoucherForSimReg(List<SimRegistrationDTO> approvedVoucherForSimReg) {
		this.approvedVoucherForSimReg = approvedVoucherForSimReg;
	}

	public SimRegistrationDTO getOnchangeDTO() {
		return onchangeDTO;
	}

	public void setOnchangeDTO(SimRegistrationDTO onchangeDTO) {
		this.onchangeDTO = onchangeDTO;
	}

	public List<SimRegistrationDTO> getGenerateReceiptListForSimReg() {
		return generateReceiptListForSimReg;
	}

	public void setGenerateReceiptListForSimReg(List<SimRegistrationDTO> generateReceiptListForSimReg) {
		this.generateReceiptListForSimReg = generateReceiptListForSimReg;
	}

	public SimRegistrationDTO getSelectedRowSimReg() {
		return selectedRowSimReg;
	}

	public void setSelectedRowSimReg(SimRegistrationDTO selectedRowSimReg) {
		this.selectedRowSimReg = selectedRowSimReg;
	}

	public boolean isDisabledPaymentModeSimReg() {
		return disabledPaymentModeSimReg;
	}

	public void setDisabledPaymentModeSimReg(boolean disabledPaymentModeSimReg) {
		this.disabledPaymentModeSimReg = disabledPaymentModeSimReg;
	}

	public boolean isDisabledBankSimReg() {
		return disabledBankSimReg;
	}

	public void setDisabledBankSimReg(boolean disabledBankSimReg) {
		this.disabledBankSimReg = disabledBankSimReg;
	}

	public boolean isDisabledDepositToBankSimReg() {
		return disabledDepositToBankSimReg;
	}

	public void setDisabledDepositToBankSimReg(boolean disabledDepositToBankSimReg) {
		this.disabledDepositToBankSimReg = disabledDepositToBankSimReg;
	}

	public boolean isDisabledChequeBankReceiptNoSimReg() {
		return disabledChequeBankReceiptNoSimReg;
	}

	public void setDisabledChequeBankReceiptNoSimReg(boolean disabledChequeBankReceiptNoSimReg) {
		this.disabledChequeBankReceiptNoSimReg = disabledChequeBankReceiptNoSimReg;
	}

	public boolean isDisablePrintSimReg() {
		return disablePrintSimReg;
	}

	public void setDisablePrintSimReg(boolean disablePrintSimReg) {
		this.disablePrintSimReg = disablePrintSimReg;
	}

	public boolean isDisableReprintSimReg() {
		return disableReprintSimReg;
	}

	public void setDisableReprintSimReg(boolean disableReprintSimReg) {
		this.disableReprintSimReg = disableReprintSimReg;
	}

	public boolean isRequiredBankDisplaySimReg() {
		return requiredBankDisplaySimReg;
	}

	public void setRequiredBankDisplaySimReg(boolean requiredBankDisplaySimReg) {
		this.requiredBankDisplaySimReg = requiredBankDisplaySimReg;
	}

	public boolean isRequiredchequeReceiptNoDisplaySimReg() {
		return requiredchequeReceiptNoDisplaySimReg;
	}

	public void setRequiredchequeReceiptNoDisplaySimReg(boolean requiredchequeReceiptNoDisplaySimReg) {
		this.requiredchequeReceiptNoDisplaySimReg = requiredchequeReceiptNoDisplaySimReg;
	}

	public List<SimRegistrationDTO> getVoucherDetailsListForSimReg() {
		return voucherDetailsListForSimReg;
	}

	public void setVoucherDetailsListForSimReg(List<SimRegistrationDTO> voucherDetailsListForSimReg) {
		this.voucherDetailsListForSimReg = voucherDetailsListForSimReg;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public BigDecimal getTotalAmountD() {
		return totalAmountD;
	}

	public void setTotalAmountD(BigDecimal totalAmountD) {
		this.totalAmountD = totalAmountD;
	}

	public String getStrQueueNo() {
		return strQueueNo;
	}

	public void setStrQueueNo(String strQueueNo) {
		this.strQueueNo = strQueueNo;
	}

	public DriverConductorRegistrationDTO getShowDataByVoucherDTO() {
		return showDataByVoucherDTO;
	}

	public void setShowDataByVoucherDTO(DriverConductorRegistrationDTO showDataByVoucherDTO) {
		this.showDataByVoucherDTO = showDataByVoucherDTO;
	}

	public BigDecimal getTotalAmountG() {
		return totalAmountG;
	}

	public void setTotalAmountG(BigDecimal totalAmountG) {
		this.totalAmountG = totalAmountG;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalAmountSim() {
		return totalAmountSim;
	}

	public void setTotalAmountSim(BigDecimal totalAmountSim) {
		this.totalAmountSim = totalAmountSim;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BigDecimal getLatePaymentFee() {
		return latePaymentFee;
	}

	public void setLatePaymentFee(BigDecimal latePaymentFee) {
		this.latePaymentFee = latePaymentFee;
	}

	public ManageInvestigationDTO getSelectedInvestigation() {
		return selectedInvestigation;
	}

	public void setSelectedInvestigation(ManageInvestigationDTO selectedInvestigation) {
		this.selectedInvestigation = selectedInvestigation;
	}
	

	/** End **/

}
