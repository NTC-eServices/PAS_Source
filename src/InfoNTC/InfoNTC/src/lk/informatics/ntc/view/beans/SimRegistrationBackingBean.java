package lk.informatics.ntc.view.beans;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.SimRegistrationDTO;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
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

/**
 * 
 * @author Viraj.K
 *
 */
@ViewScoped
@ManagedBean(name = "simRegistrationBackingBean")

public class SimRegistrationBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private SimRegistrationDTO simRegistrationDTO;
	private SimRegistrationService simRegistrationService;
	private PaymentVoucherService paymentVoucherService;

	private DocumentManagementService documentManagementService;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private SimRegistrationDTO voucherDTO;
	private List<SimRegistrationDTO> voucherDTOList;

	private List<String> permitNoList;
	private List<String> busNoList;
	private List<String> accountNoList;

	private List<SimRegistrationDTO> emiDTOList;
	private SimRegistrationDTO emiDTO;

	private String errorStatus, successStatus, confirmStatus;

	private boolean createVoucherCheck = true;
	private boolean saveBtnCheck = true;
	private boolean updateBtnCheck = false;
	private boolean addBtnCheck = false;
	private boolean disableDocumentManagment = true;
	private boolean disableEmiDelete = false;

	private boolean disableAccountNo = true;
	private boolean disableAmount = true;
	private boolean editMood = false;
	private boolean renderVouAdd = true;
	private boolean renderVouUpdate = false;
	private boolean disableVouEdit = false;
	private boolean disableVouDelete = false;
	private boolean disableChargeType = false;
	private boolean renderVouClear = true;
	private boolean renderVouEditClear = false;
	private boolean renderGenerateVoucher = true;
	private boolean renderUpdateVoucher = false;

	private double totalAmount;

	private SimRegistrationDTO selectEmiDTO;
	private SimRegistrationDTO selectDTO;
	private boolean disablePrint;
	private boolean disableRePrint;
	private boolean disablefields = false;

	String valueForPrint;
	private StreamedContent files;
	boolean addEmi = false;

	public SimRegistrationBackingBean() {
		simRegistrationService = (SimRegistrationService) SpringApplicationContex.getBean("simRegistrationService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		simRegistrationDTO = new SimRegistrationDTO();
		emiDTO = new SimRegistrationDTO();
		emiDTO.setEmiReIssueDate(new Date());

		permitNoList = new ArrayList<>();
		busNoList = new ArrayList<>();

		emiDTOList = new ArrayList<>();

		simRegistrationDTO.setIssueDate(new Date());

		voucherDTOList = new ArrayList<>();

		voucherDTOList = simRegistrationService.getChargeDetailsList();
		disablePrint = true;
		disableRePrint = true;

	}

	public void onServiceCategoryChange() {

		permitNoList = new ArrayList<>();
		busNoList = new ArrayList<>();
		simRegistrationDTO.setBusNo(null);
		simRegistrationDTO.setPermitNo(null);

		if (simRegistrationDTO.getServiceCategory().equals("other")) {

		} else {
			permitNoList = simRegistrationService.getPermitNoByService(simRegistrationDTO.getServiceCategory());
			busNoList = simRegistrationService.getBusNoByService(simRegistrationDTO.getServiceCategory());
		}
	}

	public void onPermitNoSelect() {
		simRegistrationDTO.setBusNo(simRegistrationService.getBusNoByPermitNo(simRegistrationDTO.getServiceCategory(),
				simRegistrationDTO.getPermitNo()));
	}

	public void onBusNoSelect() {
		simRegistrationDTO.setPermitNo(simRegistrationService
				.getPermitNoByBusNo(simRegistrationDTO.getServiceCategory(), simRegistrationDTO.getBusNo()));
	}

	public List<String> completePermitNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < permitNoList.size(); i++) {
			String cm = permitNoList.get(i);
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public List<String> completeBusNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < busNoList.size(); i++) {
			String cm = busNoList.get(i);
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public boolean validatSave() {

		if (simRegistrationDTO.getServiceCategory().isEmpty() || simRegistrationDTO.getServiceCategory() == null) {
			setErrorStatus("Please select Service Category.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			return false;
		} else if (simRegistrationDTO.getPermitNo().isEmpty() || simRegistrationDTO.getBusNo() == null) {
			setErrorStatus("Please add Permit Number.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			return false;
		} else if (simRegistrationDTO.getBusNo().isEmpty() || simRegistrationDTO.getBusNo() == null) {
			setErrorStatus("Please add Bus Number.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			return false;
		}

		else if (simRegistrationDTO.getSimNo().isEmpty() || simRegistrationDTO.getSimNo() == null) {
			setErrorStatus("Please enter SIM Number (Mobile No.).");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			return false;
		} else if (simRegistrationDTO.getEmiNo().isEmpty() || simRegistrationDTO.getEmiNo() == null) {
			setErrorStatus("Please enter EMI Number.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			return false;
		}

		else if (simRegistrationDTO.getReceiversName().isEmpty() || simRegistrationDTO.getReceiversName() == null) {
			setErrorStatus("Please enter Receivers Name.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			return false;
		} else if (!validateName(simRegistrationDTO.getReceiversName())) {
			setErrorStatus("Please enter valid Receivers Name.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			simRegistrationDTO.setReceiversName(null);
			return false;
		} else if (simRegistrationDTO.getNicNo().isEmpty() || simRegistrationDTO.getNicNo() == null) {
			setErrorStatus("Please enter NIC Number.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			return false;
		} else if (simRegistrationDTO.getValidUntilDate() == null) {
			setErrorStatus("Please select a valid Until Date.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			return false;
		} else {
			return true;
		}
	}

	public static boolean validateName(String txt) {
		String regx = "^[\\p{L} .'-]+$";
		Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(txt);
		return matcher.find();
	}

	public void ajaxFilterAcountNo() {

		if (voucherDTO.getVouChargeType() == null || voucherDTO.getVouChargeType().isEmpty()) {

			disableAccountNo = true;
			disableAmount = true;

		} else {
			PaymentVoucherDTO dto = new PaymentVoucherDTO();

			String chargeCode = paymentVoucherService.getChargeCode(voucherDTO.getVouChargeType());
			accountNoList = paymentVoucherService.getAccountNo(chargeCode, dto);

			if (accountNoList.isEmpty()) {
				disableAccountNo = true;
				errorStatus = "No active account found for selected Charge Type.";
				sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
				return;
			}

			if (editMood == false) {
				disableAccountNo = false;
				disableAmount = false;
			} else {
				disableAccountNo = true;
				disableAmount = true;
			}
		}
	}

	public void calculateTotalAmount() {
		totalAmount = 0.0;
		for (SimRegistrationDTO dto : voucherDTOList) {
			totalAmount = totalAmount + dto.getVouAmmount();
		}
	}

	public void addEmiBtnAction() {

		if (emiDTO.getEmiBusNo().isEmpty() || emiDTO.getEmiBusNo() == null) {
			setErrorStatus("Please enter Bus Number.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
		} else if (emiDTO.getEmiReIssueDate() == null) {
			setErrorStatus("Please enter Transaction Date.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
		} else {

			if (emiDTO.getEmiStatus().equals("Active")) {
				for (SimRegistrationDTO eDto : emiDTOList) {
					if (eDto.getEmiStatus().equals("Active")) {
						eDto.setEmiStatus("Inactive");
					}
				}
			}

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			emiDTO.setEmiReIssueDateString(dateFormat.format(emiDTO.getEmiReIssueDate()));
			emiDTOList.add(emiDTO);

			emiDTO = new SimRegistrationDTO();
			addEmi = true;
		}
		emiDTO.setEmiReIssueDate(new Date());

	}

	public void emiDelete() {

		for (int i = 0; i < emiDTOList.size(); i++) {
			if (emiDTOList.get(i).equals(selectEmiDTO)) {
				emiDTOList.remove(i);
			}
		}

	}

	public void saveBtnAction() {
		String simRegNo = null;
		if (!simRegistrationService.validateExistBusPermit(simRegistrationDTO)) {
			setErrorStatus("SIM already registered for this Permit No. and Bus No.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
		} else {
			if (validatSave()) {

				simRegistrationDTO.setSimCreatedBy(sessionBackingBean.getLoginUser());
				simRegistrationDTO.setSimCreatedDate(new Date());

				simRegNo = simRegistrationService.generateSimRegNo();
				simRegistrationDTO.setSimRegNo(simRegNo);

				setCreateVoucherCheck(false);
				setDisableDocumentManagment(false);
				setSaveBtnCheck(false);
				setUpdateBtnCheck(true);
				setDisablefields(true);

				boolean simSave = simRegistrationService.insertSimAndEmiDetails(simRegistrationDTO, emiDTOList);
				if (simSave) {
					setSuccessStatus("SIM added successfully.");
					RequestContext.getCurrentInstance().update("successMessage");
					RequestContext.getCurrentInstance().execute("PF('successStatus').show()");
				}
			}

		}
	}

	public void updateBtnAction() {
		int val = simRegistrationService.checkDuplicatePermit(simRegistrationDTO.getPermitNo());
		if (val >= 0) {
			if (validatSave()) {

				simRegistrationDTO.setSimModifiedBy(sessionBackingBean.getLoginUser());
				simRegistrationDTO.setSimModifiedDate(new Date());

				boolean simUpdate = simRegistrationService.updateSimAndEmiDetails(simRegistrationDTO, emiDTOList);
				if (simUpdate) {
					setSuccessStatus("SIM updated successfully.");
					RequestContext.getCurrentInstance().update("successMessage");
					RequestContext.getCurrentInstance().execute("PF('successStatus').show()");
				}
			}
		} else {
			setErrorStatus("SIM already registered for this Permit No. and Bus No.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
		}

	}

	public void createVoucherBtnAction() {
		voucherDTO = new SimRegistrationDTO();
		selectDTO = new SimRegistrationDTO();
		voucherDTO.setSimRegNo(simRegistrationDTO.getSimRegNo());

		disableAccountNo = true;
		disableAmount = true;

		accountNoList = new ArrayList<>();
		totalAmount = 0.0;

		calculateTotalAmount();

		RequestContext.getCurrentInstance().execute("PF('createVoucherDialog').show()");
	}

	public void documentManagement() {
		try {
			sessionBackingBean.setSimRegNo(simRegistrationDTO.getSimRegNo());
			sessionBackingBean.setSimNo(simRegistrationDTO.getSimNo());
			sessionBackingBean.setTransactionType("SIM REGISTRATION");

			String strTransactionType = "SR";

			setMandatoryList(documentManagementService.mandatoryDocsForSIMReg(strTransactionType,
					simRegistrationDTO.getSimRegNo()));
			setOptionalList(documentManagementService.optionalDocsForSIMReg(strTransactionType,
					simRegistrationDTO.getSimRegNo()));

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.simRegMandatoryListM(simRegistrationDTO.getSimRegNo(), strTransactionType);
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.simRegOptionalListM(simRegistrationDTO.getSimRegNo(), strTransactionType);

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearBtnAction() {
		simRegistrationDTO = new SimRegistrationDTO();
		emiDTO = new SimRegistrationDTO();
		emiDTO.setEmiReIssueDate(new Date());
		simRegistrationDTO.setIssueDate(new Date());

		permitNoList = new ArrayList<>();
		busNoList = new ArrayList<>();

		emiDTOList = new ArrayList<>();

		setCreateVoucherCheck(true);
		setDisableDocumentManagment(true);
		setUpdateBtnCheck(false);
		setSaveBtnCheck(true);
		setAddBtnCheck(false);
		setDisableEmiDelete(false);
		setDisablefields(false);
		renderUpdateVoucher = false;

	}

	public void voucherEdit() {

		disableChargeType = true;
		disableVouEdit = true;
		disableVouDelete = true;

		renderVouAdd = false;
		renderVouUpdate = true;
		renderVouClear = false;
		renderVouEditClear = true;

		voucherDTO = new SimRegistrationDTO();

		voucherDTO.setVouChargeType(selectDTO.getVouChargeType());
		ajaxFilterAcountNo();
		voucherDTO.setVouAccountNo(selectDTO.getVouAccountNo());
		voucherDTO.setVouAmmount(selectDTO.getVouAmmount());

		disableAccountNo = true;

		selectDTO = new SimRegistrationDTO();
	}

	public void voucherDelete() {

		for (int i = 0; i < voucherDTOList.size(); i++) {
			if (selectDTO.getVouChargeType().equals(voucherDTOList.get(i).getVouChargeType())
					&& selectDTO.getVouAccountNo().equals(voucherDTOList.get(i).getVouAccountNo())) {
				voucherDTOList.remove(i);
			}
		}

		calculateTotalAmount();

	}

	public boolean checkExistVoucher() {
		boolean check = false;

		for (int i = 0; i < voucherDTOList.size(); i++) {
			if (voucherDTOList.get(i).getVouChargeType().equals(voucherDTO.getVouChargeType())
					&& voucherDTOList.get(i).getVouAccountNo().equals(voucherDTO.getVouAccountNo())) {
				check = true;
			}
		}
		return check;
	}

	public void voucherAddBtnAction() {

		if (voucherDTO.getVouChargeType().isEmpty() || voucherDTO.getVouChargeType() == null) {
			errorStatus = "Select Charge Type.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else if (voucherDTO.getVouAccountNo().isEmpty() || voucherDTO.getVouAccountNo() == null) {
			errorStatus = "Select Account No.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else if (voucherDTO.getVouAmmount() == null) {
			errorStatus = "Please Enter Amount.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else if (checkExistVoucher()) {
			errorStatus = "Inserted Charge Details already exist.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			voucherDetailsClearBtnActions();
		} else {

			voucherDTOList.add(voucherDTO);

			voucherDTO = new SimRegistrationDTO();
			voucherDTO.setSimRegNo(simRegistrationDTO.getSimRegNo());

			disableAccountNo = true;
			disableAmount = true;

			calculateTotalAmount();
		}

	}

	public void voucherUpdateBtnAction() {
		if (voucherDTO.getVouAmmount() == null) {
			errorStatus = "Enter Charge Amount.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else {
			for (int i = 0; i < voucherDTOList.size(); i++) {
				if (voucherDTOList.get(i).getVouChargeType().equals(voucherDTO.getVouChargeType())
						&& voucherDTOList.get(i).getVouAccountNo().equals(voucherDTO.getVouAccountNo())) {
					voucherDTOList.set(i, voucherDTO);
				}
			}

			voucherDTO = new SimRegistrationDTO();
			disableAccountNo = true;
			disableAmount = true;
			disableChargeType = false;

			disableVouEdit = false;
			disableVouDelete = false;

			renderVouAdd = true;
			renderVouUpdate = false;
			renderVouClear = true;
			renderVouEditClear = false;

			calculateTotalAmount();
		}

	}

	public void voucherDetailsClearBtnActions() {
		voucherDTO = new SimRegistrationDTO();
		voucherDTO.setSimRegNo(simRegistrationDTO.getSimRegNo());
		disableAccountNo = true;
		disableAmount = true;
	}

	public void voucherEditClearBtnAction() {
		voucherDTO.setVouAmmount(null);
	}

	public void generateVoucher() {
		if (voucherDTOList.isEmpty()) {
			errorStatus = "Add Charges to continue.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else {

			simRegistrationDTO.setVouNo(simRegistrationService.generateVoucherNo()); // generate Voucher Number

			simRegistrationDTO.setVouTotalAmount(totalAmount);
			simRegistrationDTO.setVouCreatedBy(sessionBackingBean.getLoginUser());
			simRegistrationDTO.setVouCreatedDate(new Date());

			simRegistrationService.addVoucher(simRegistrationDTO, voucherDTOList);

			successStatus = "Voucher generated succesfully";
			sessionBackingBean.showMessage("Success", successStatus, "SUCCESS_DIALOG");
			disablePrint = false;
			renderGenerateVoucher = false;
			renderUpdateVoucher = true;

		}
	}

	public void updateVoucher() {
		if (voucherDTOList.isEmpty()) {
			errorStatus = "Add Charges to continue.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else {

			simRegistrationDTO.setVouTotalAmount(totalAmount);
			simRegistrationDTO.setVouModifiedBy(sessionBackingBean.getLoginUser());
			simRegistrationDTO.setVouModifiedDate(new Date());

			simRegistrationService.updateVoucher(simRegistrationDTO, voucherDTOList);
			disablePrint = false;
			successStatus = "Generated voucher updated succesfully";
			sessionBackingBean.showMessage("Success", successStatus, "SUCCESS_DIALOG");

		}
	}

	public StreamedContent printVoucher() throws JRException {

		valueForPrint = simRegistrationDTO.getVouNo();

		sessionBackingBean.getLoginUser();
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//debitVoucherForSimRegistration.jrxml";

		try {
			conn = ConnectionManager.getConnection();

			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_voucher_No", valueForPrint);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "SIM Voucher.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			simRegistrationService.updateVoucherPrintReprintStatus(simRegistrationDTO, "Y", null);

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
		disablePrint = true;
		disableRePrint = false;
		return files;
	}

	public void rePrintVoucher(ActionEvent ae) throws JRException {

		valueForPrint = simRegistrationDTO.getVouNo();
		sessionBackingBean.getLoginUser();
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//debitVoucherForSimRegistration.jrxml";

		try {
			conn = ConnectionManager.getConnection();

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_voucher_No", valueForPrint);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "SIM Voucher.pdf");

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

	}

	public SimRegistrationDTO getSimRegistrationDTO() {
		return simRegistrationDTO;
	}

	public void setSimRegistrationDTO(SimRegistrationDTO simRegistrationDTO) {
		this.simRegistrationDTO = simRegistrationDTO;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public SimRegistrationService getSimRegistrationService() {
		return simRegistrationService;
	}

	public void setSimRegistrationService(SimRegistrationService simRegistrationService) {
		this.simRegistrationService = simRegistrationService;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	public String getSuccessStatus() {
		return successStatus;
	}

	public void setSuccessStatus(String successStatus) {
		this.successStatus = successStatus;
	}

	public String getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<String> getBusNoList() {
		return busNoList;
	}

	public void setBusNoList(List<String> busNoList) {
		this.busNoList = busNoList;
	}

	public List<SimRegistrationDTO> getEmiDTOList() {
		return emiDTOList;
	}

	public void setEmiDTOList(List<SimRegistrationDTO> emiDTOList) {
		this.emiDTOList = emiDTOList;
	}

	public SimRegistrationDTO getEmiDTO() {
		return emiDTO;
	}

	public void setEmiDTO(SimRegistrationDTO emiDTO) {
		this.emiDTO = emiDTO;
	}

	public boolean isCreateVoucherCheck() {
		return createVoucherCheck;
	}

	public void setCreateVoucherCheck(boolean createVoucherCheck) {
		this.createVoucherCheck = createVoucherCheck;
	}

	public boolean isSaveBtnCheck() {
		return saveBtnCheck;
	}

	public void setSaveBtnCheck(boolean saveBtnCheck) {
		this.saveBtnCheck = saveBtnCheck;
	}

	public boolean isUpdateBtnCheck() {
		return updateBtnCheck;
	}

	public void setUpdateBtnCheck(boolean updateBtnCheck) {
		this.updateBtnCheck = updateBtnCheck;
	}

	public boolean isAddBtnCheck() {
		return addBtnCheck;
	}

	public void setAddBtnCheck(boolean addBtnCheck) {
		this.addBtnCheck = addBtnCheck;
	}

	public SimRegistrationDTO getVoucherDTO() {
		return voucherDTO;
	}

	public void setVoucherDTO(SimRegistrationDTO voucherDTO) {
		this.voucherDTO = voucherDTO;
	}

	public List<SimRegistrationDTO> getVoucherDTOList() {
		return voucherDTOList;
	}

	public void setVoucherDTOList(List<SimRegistrationDTO> voucherDTOList) {
		this.voucherDTOList = voucherDTOList;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public List<String> getAccountNoList() {
		return accountNoList;
	}

	public void setAccountNoList(List<String> accountNoList) {
		this.accountNoList = accountNoList;
	}

	public boolean isDisableAccountNo() {
		return disableAccountNo;
	}

	public void setDisableAccountNo(boolean disableAccountNo) {
		this.disableAccountNo = disableAccountNo;
	}

	public boolean isDisableAmount() {
		return disableAmount;
	}

	public void setDisableAmount(boolean disableAmount) {
		this.disableAmount = disableAmount;
	}

	public boolean isEditMood() {
		return editMood;
	}

	public void setEditMood(boolean editMood) {
		this.editMood = editMood;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public SimRegistrationDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(SimRegistrationDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public boolean isRenderVouAdd() {
		return renderVouAdd;
	}

	public void setRenderVouAdd(boolean renderVouAdd) {
		this.renderVouAdd = renderVouAdd;
	}

	public boolean isDisableChargeType() {
		return disableChargeType;
	}

	public void setDisableChargeType(boolean disableChargeType) {
		this.disableChargeType = disableChargeType;
	}

	public boolean isRenderVouUpdate() {
		return renderVouUpdate;
	}

	public void setRenderVouUpdate(boolean renderVouUpdate) {
		this.renderVouUpdate = renderVouUpdate;
	}

	public boolean isDisableVouEdit() {
		return disableVouEdit;
	}

	public void setDisableVouEdit(boolean disableVouEdit) {
		this.disableVouEdit = disableVouEdit;
	}

	public boolean isDisableVouDelete() {
		return disableVouDelete;
	}

	public void setDisableVouDelete(boolean disableVouDelete) {
		this.disableVouDelete = disableVouDelete;
	}

	public boolean isRenderVouClear() {
		return renderVouClear;
	}

	public void setRenderVouClear(boolean renderVouClear) {
		this.renderVouClear = renderVouClear;
	}

	public boolean isRenderVouEditClear() {
		return renderVouEditClear;
	}

	public void setRenderVouEditClear(boolean renderVouEditClear) {
		this.renderVouEditClear = renderVouEditClear;
	}

	public boolean isDisableDocumentManagment() {
		return disableDocumentManagment;
	}

	public void setDisableDocumentManagment(boolean disableDocumentManagment) {
		this.disableDocumentManagment = disableDocumentManagment;
	}

	public boolean isDisableEmiDelete() {
		return disableEmiDelete;
	}

	public void setDisableEmiDelete(boolean disableEmiDelete) {
		this.disableEmiDelete = disableEmiDelete;
	}

	public SimRegistrationDTO getSelectEmiDTO() {
		return selectEmiDTO;
	}

	public void setSelectEmiDTO(SimRegistrationDTO selectEmiDTO) {
		this.selectEmiDTO = selectEmiDTO;
	}

	public boolean isRenderGenerateVoucher() {
		return renderGenerateVoucher;
	}

	public void setRenderGenerateVoucher(boolean renderGenerateVoucher) {
		this.renderGenerateVoucher = renderGenerateVoucher;
	}

	public boolean isRenderUpdateVoucher() {
		return renderUpdateVoucher;
	}

	public void setRenderUpdateVoucher(boolean renderUpdateVoucher) {
		this.renderUpdateVoucher = renderUpdateVoucher;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isDisablePrint() {
		return disablePrint;
	}

	public void setDisablePrint(boolean disablePrint) {
		this.disablePrint = disablePrint;
	}

	public boolean isDisableRePrint() {
		return disableRePrint;
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

	public void setDisableRePrint(boolean disableRePrint) {
		this.disableRePrint = disableRePrint;
	}

	public boolean isDisablefields() {
		return disablefields;
	}

	public void setDisablefields(boolean disablefields) {
		this.disablefields = disablefields;
	}

}
