package lk.informatics.ntc.view.beans;

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
import lk.informatics.ntc.model.service.SimRegEditService;
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
@ManagedBean(name = "simTransferBean")

public class SimTransferBackingBean {

	// variable Initiation

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private SimRegistrationDTO simRegistrationDTO;
	private SimRegistrationDTO voucherDTO;
	private SimRegistrationDTO emiDTO;
	private SimRegistrationDTO selectEmiDTO;
	private SimRegistrationDTO selectDTO;
	private SimRegistrationDTO simSearchDTO;
	private SimRegistrationDTO voucher;

	private SimRegistrationService simRegistrationService;
	private PaymentVoucherService paymentVoucherService;
	private SimRegEditService simRegEditService;

	private List<SimRegistrationDTO> voucherDTOList;
	private List<String> accountNoList;
	private List<SimRegistrationDTO> emiDTOList;
	private List<SimRegistrationDTO> simDetailsList;
	private List<String> permitNoList;
	private List<String> busNoList;

	private String errorStatus, successStatus, confirmStatus;

	private boolean createVoucherCheck = true;
	private boolean cancelVoucherCheck = true;
	private boolean saveBtnCheck = true;
	private boolean updateBtnCheck = false;
	private boolean addBtnCheck = false;
	private boolean disableDocumentManagment = true;
	private boolean disableEmiDelete = false;
	private boolean disableEmiNo = true;

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
	String valueForPrint;
	private StreamedContent files;
	private double totalAmount;
	private boolean disableReprint;
	private boolean disableViewBtn;
	private boolean disableButton, disableButtonUp;

	private DocumentManagementService documentManagementService;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	private List<SimRegistrationDTO> oldPermitDetails;

	public SimTransferBackingBean() {

		simRegistrationService = (SimRegistrationService) SpringApplicationContex.getBean("simRegistrationService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		simRegEditService = (SimRegEditService) SpringApplicationContex.getBean("simRegEditService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		simRegistrationDTO = new SimRegistrationDTO();

		// EMI details
		emiDTO = new SimRegistrationDTO();
		emiDTO.setEmiReIssueDate(new Date());
		emiDTOList = new ArrayList<>();

		// for Search SIM
		simDetailsList = new ArrayList<>();
		simDetailsList = simRegEditService.getSIMDetailsBySimStatus("A");
		simSearchDTO = new SimRegistrationDTO();

		voucherDTOList = new ArrayList<>();
		// add voucher initial data
		voucherDTOList = simRegistrationService.getChargeDetailsList();
		voucherDTO = new SimRegistrationDTO();

		disableReprint = true;
		disableButton = true;
		disableViewBtn = true;
		disableButtonUp = true;

	}

	// Search Action
	public void searchAction() {
		disableReprint = true;
		disableButton = false;
		disableButtonUp = true;
		if ((simSearchDTO.getSimRegNo().isEmpty() || simSearchDTO.getSimRegNo().equals(null))
				&& (simSearchDTO.getPermitNo().isEmpty() || simSearchDTO.getPermitNo().equals(null))
				&& (simSearchDTO.getBusNo().isEmpty() || simSearchDTO.getBusNo().equals(null))) {
			errorStatus = "Select data to search.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else {

			List<Object> list = new ArrayList<>();
			simRegistrationDTO = new SimRegistrationDTO();
			emiDTOList = new ArrayList<>();
			voucher = new SimRegistrationDTO();

			list = simRegEditService.getSearchedDTO(simSearchDTO);

			if (list == null) {
				errorStatus = "No data found.";
				sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			} else {

				createVoucherCheck = false;

				simRegistrationDTO = (SimRegistrationDTO) list.get(0);

				emiDTOList = simRegEditService.getEmiListBySimNo(simRegistrationDTO.getSimRegNo());

				for (SimRegistrationDTO dto : emiDTOList) {
					if (dto.getEmiStatus().equals("A")) {
						dto.setEmiStatus("Active");
					} else if (dto.getEmiStatus().equals("I")) {
						dto.setEmiStatus("Inactive");
					}
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					dto.setEmiReIssueDateString(dateFormat.format(dto.getEmiReIssueDate()));
				}

				voucher = simRegEditService.getVoucherDetailsBySimNo(simRegistrationDTO.getSimRegNo());

				if (voucher != null) {

					cancelVoucherCheck = false;
					renderGenerateVoucher = false;
					renderUpdateVoucher = true;

					// set voucher details to simRegDTO
					simRegistrationDTO.setVouNo(voucher.getVouNo());
					simRegistrationDTO.setVouTotalAmount(voucher.getVouTotalAmount());
					simRegistrationDTO.setReceiptNo(voucher.getReceiptNo());
					simRegistrationDTO.setVoucherApprovedStatus(voucher.getVoucherApprovedStatus());
					simRegistrationDTO.setVoucherSeqNo(voucher.getVoucherSeqNo());

					voucherDTOList = new ArrayList<>();
					if (voucher.getVouPrint() != null) {
						disableReprint = false;
					}
					voucherDTOList = simRegEditService
							.getChargeDetailsByVoucherSeq(simRegistrationDTO.getVoucherSeqNo());

				} else {
					cancelVoucherCheck = true;
					voucherDTOList = new ArrayList<>();

					voucherDTOList = simRegistrationService.getChargeDetailsList();
					voucherDTO = new SimRegistrationDTO();

					renderUpdateVoucher = false;
					renderGenerateVoucher = true;
				}
			}

			simSearchDTO = new SimRegistrationDTO();

		}
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

	// Auto key Complete Methods
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

	// validate save
	public boolean validatSave() {

		if (simRegistrationDTO.getSimRegNo() == null) {
			errorStatus = "Please search and get SIM Registration details to Update.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			return false;
		} else if (simRegistrationDTO.getServiceCategory().isEmpty()
				|| simRegistrationDTO.getServiceCategory() == null) {
			errorStatus = "Please Select Service Category.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			return false;
		} else if (simRegistrationDTO.getPermitNo().isEmpty() || simRegistrationDTO.getBusNo() == null) {
			errorStatus = "Please Add Permit Number.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			return false;
		} else if (simRegistrationDTO.getBusNo().isEmpty() || simRegistrationDTO.getBusNo() == null) {
			errorStatus = "Please Add Bus Number.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			return false;
		} else if (simRegistrationDTO.getSimNo().isEmpty() || simRegistrationDTO.getSimNo() == null) {
			errorStatus = "Please Enter SIM Number (Phone Number).";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			return false;
		} else if (simRegistrationDTO.getEmiNo().isEmpty() || simRegistrationDTO.getEmiNo() == null) {
			errorStatus = "Please Enter EMI Number.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			return false;
		}

		else if (Integer.parseInt(simRegistrationDTO.getSimNo()) <= 0100000000) {
			errorStatus = "Please Enter Valid SIM Number (Phone Number).";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			simRegistrationDTO.setSimNo(null);
			return false;
		} else if (simRegistrationDTO.getReceiversName().isEmpty() || simRegistrationDTO.getReceiversName() == null) {
			errorStatus = "Please Enter Recivers Name.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			return false;
		} else if (simRegistrationDTO.getNicNo().isEmpty() || simRegistrationDTO.getNicNo() == null) {
			errorStatus = "Please Enter NIC Number.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			return false;
		} else if (simRegistrationDTO.getValidUntilDate() == null) {
			errorStatus = "Please Select Valid Until Date.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			return false;
		} else {
			return true;
		}
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
				errorStatus = "No active acount found selected Charge Type";
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

	public void simTransferBtnAction() {
		disableEmiNo = false;
	}

	// Add EMI
	public void addEmiBtnAction() {

		if (emiDTO.getEmiBusNo().isEmpty() || emiDTO.getEmiBusNo() == null) {
			errorStatus = "Please Enter Bus Number.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else if (emiDTO.getEmiReIssueDate() == null) {
			errorStatus = "Please Enter Transaction Date.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else {
			// select active when already have active status
			if (emiDTO.getEmiStatus().equals("Active")) {
				for (SimRegistrationDTO eDto : emiDTOList) {
					if (eDto.getEmiStatus().equals("Active")) {
						eDto.setEmiStatus("Inactive");
					}
				}
			}

			emiDTO.setSimRegNo(simRegistrationDTO.getSimRegNo());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			emiDTO.setEmiReIssueDateString(dateFormat.format(emiDTO.getEmiReIssueDate()));
			emiDTOList.add(emiDTO);

			emiDTO = new SimRegistrationDTO();
			emiDTO.setEmiReIssueDate(new Date());

			disableButtonUp = false;
		}

	}

	// delete EMI
	public void emiDelete() {

		for (int i = 0; i < emiDTOList.size(); i++) {
			if (emiDTOList.get(i).equals(selectEmiDTO)) {
				emiDTOList.remove(i);
			}
		}

	}

	// Save
	public void saveBtnAction() {

		if (validatSave()) {
			// set data for add SIM
			simRegistrationDTO.setSimCreatedBy(sessionBackingBean.getLoginUser());
			simRegistrationDTO.setSimCreatedDate(new Date());

			simRegistrationService.addSimRegistration(simRegistrationDTO); // add SIM

			for (SimRegistrationDTO dto : emiDTOList) {
				// set data for add EMI
				dto.setSimCreatedBy(sessionBackingBean.getLoginUser());
				dto.setSimCreatedDate(new Date());

				simRegistrationService.addEmi(dto); // add EMI
			}

			setCreateVoucherCheck(false);
			setDisableDocumentManagment(false);
			setSaveBtnCheck(false);
			setUpdateBtnCheck(true);
			setAddBtnCheck(true);
			setDisableEmiDelete(true);

			setSuccessStatus("SIM Added Successfully.");
			RequestContext.getCurrentInstance().update("successMessage");
			RequestContext.getCurrentInstance().execute("PF('successStatus').show()");
		}

	}

	// Update
	public void updateBtnAction() {

		if (validatSave()) {
			// set data for update SIM
			simRegistrationDTO.setSimModifiedBy(sessionBackingBean.getLoginUser());
			simRegistrationDTO.setSimModifiedDate(new Date());

			// update SIM Registration
			simRegistrationService.updateSimTransfer(simRegistrationDTO);

			// Delete existing EMI details
			simRegistrationService.deleteEmiDetails(simRegistrationDTO.getSimRegNo());

			for (SimRegistrationDTO dto : emiDTOList) {
				// set data for update EMI
				dto.setSimCreatedBy(sessionBackingBean.getLoginUser());
				dto.setSimCreatedDate(new Date());
				dto.setSimRegNo(simRegistrationDTO.getSimRegNo());
				if (dto.getEmiStatus().equalsIgnoreCase("Active")) {
					dto.setEmiNo(simRegistrationDTO.getEmiNo());
				} else {
					dto.setEmiNo(dto.getEmiNo());
				}

				simRegistrationService.updateEmi(dto); // add EMI
			}

			disableEmiNo = true;
			disableViewBtn = false;
			successStatus = "SIM Updated Successfully.";
			sessionBackingBean.showMessage("Success", successStatus, "SUCCESS_DIALOG");

		}

	}

	// Create Voucher
	public void createVoucherBtnAction() {
		voucherDTO = new SimRegistrationDTO();
		selectDTO = new SimRegistrationDTO();
		voucherDTO.setSimRegNo(simRegistrationDTO.getSimRegNo());

		disableAccountNo = true;
		disableAmount = true;

		accountNoList = new ArrayList<>();
		totalAmount = 0.0;

		calculateTotalAmount();

		if (voucher != null) {
			renderGenerateVoucher = false;
			renderUpdateVoucher = true;
		} else {
			renderUpdateVoucher = false;
			renderGenerateVoucher = true;
		}

		if (simRegistrationDTO.getVouNo() == null) {
			renderUpdateVoucher = false;
			renderGenerateVoucher = true;
		} else {
			renderGenerateVoucher = false;
			renderUpdateVoucher = true;
		}

		RequestContext.getCurrentInstance().execute("PF('createVoucherDialog').show()");
	}

	// cancel voucher
	public void cancelVoucherBtnAction() {

		if (simRegistrationDTO.getVoucherApprovedStatus().equals("A")) {
			errorStatus = "Receipt already generated for selected SIM Reg.No. Can not cancel now.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else if (simRegistrationDTO.getVoucherApprovedStatus().equals("P")
				|| simRegistrationDTO.getVoucherApprovedStatus().equals("R")) {
			
			/** update spv_voucherapproved_status as VC
			 * update sim_status_type as R -> SIM Registration **/
			simRegEditService.cancelVoucher(voucher.getVouNo(), "VC", null,
					sessionBackingBean.getLoginUser(), simRegistrationDTO.getSimRegNo(), "R");
			/** end **/

			voucher = new SimRegistrationDTO();
			simRegistrationDTO.setVouNo(null);
			simRegistrationDTO.setVouTotalAmount(null);
			simRegistrationDTO.setVoucherApprovedStatus(null);
			simRegistrationDTO.setVoucherSeqNo(null);

			cancelVoucherCheck = true;
			renderUpdateVoucher = false;
			renderGenerateVoucher = true;

			voucherDTOList = new ArrayList<>();
			// add voucher initial data
			voucherDTOList = simRegistrationService.getChargeDetailsList();
			voucherDTO = new SimRegistrationDTO();

			successStatus = "generated Voucher Cancelled Succesfully.";
			sessionBackingBean.showMessage("Success", successStatus, "SUCCESS_DIALOG");

		} else {
			errorStatus = "Can not find the approved status.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		}
	}

	// Document Management
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

	// Clear
	public void clearBtnAction() {
		simRegistrationDTO = new SimRegistrationDTO();
		emiDTO = new SimRegistrationDTO();
		emiDTO.setEmiReIssueDate(new Date());

		emiDTOList = new ArrayList<>();

		setCreateVoucherCheck(true);
		setCancelVoucherCheck(true);
		setDisableDocumentManagment(true);
		setUpdateBtnCheck(false);
		setSaveBtnCheck(true);
		setAddBtnCheck(false);
		setDisableEmiDelete(false);
		disableEmiNo = true;
		disableButton = true;
		disableButtonUp = true;
		disableViewBtn = true;

	}

	// *** Voucher Creation *** //

	// voucher edit
	public void vouEdit() {

		// editMood = true;
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

	// voucher delete
	public void vouDelete() {

		for (int i = 0; i < voucherDTOList.size(); i++) {
			if (selectDTO.getVouChargeType().equals(voucherDTOList.get(i).getVouChargeType())
					&& selectDTO.getVouAccountNo().equals(voucherDTOList.get(i).getVouAccountNo())) {
				voucherDTOList.remove(i);
			}
		}

		calculateTotalAmount();

	}

	public boolean checkExistVou() {
		boolean check = false;

		for (int i = 0; i < voucherDTOList.size(); i++) {
			if (voucherDTOList.get(i).getVouChargeType().equals(voucherDTO.getVouChargeType())
					&& voucherDTOList.get(i).getVouAccountNo().equals(voucherDTO.getVouAccountNo())) {
				check = true;
			}
		}
		return check;
	}

	// add voucher details
	public void vouAddBtnAction() {

		if (voucherDTO.getVouChargeType().isEmpty() || voucherDTO.getVouChargeType() == null) {
			errorStatus = "Select Charge Type.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else if (voucherDTO.getVouAccountNo().isEmpty() || voucherDTO.getVouAccountNo() == null) {
			errorStatus = "Select Account No.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else if (voucherDTO.getVouAmmount() == null) {
			errorStatus = "Please Enter Amount.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else if (checkExistVou()) {
			errorStatus = "Inserted Charge Details already Exist.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
			vouDetailsClearBtnAction();
		} else {

			voucherDTOList.add(voucherDTO);

			voucherDTO = new SimRegistrationDTO();
			voucherDTO.setSimRegNo(simRegistrationDTO.getSimRegNo());

			disableAccountNo = true;
			disableAmount = true;

			calculateTotalAmount();
		}

	}

	public void vouUpdateBtnAction() {
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

	// clear voucher details
	public void vouDetailsClearBtnAction() {
		voucherDTO = new SimRegistrationDTO();
		voucherDTO.setSimRegNo(simRegistrationDTO.getSimRegNo());
		disableAccountNo = true;
		disableAmount = true;
	}

	public void vouEditClearBtnAction() {
		voucherDTO.setVouAmmount(null);
	}

	// generate Voucher
	public void generateVoucher() {
		if (voucherDTOList.isEmpty()) {
			errorStatus = "Add Charges to Continue.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else {

			// set data for Add SIM Voucher
			simRegistrationDTO.setVouNo(simRegistrationService.generateVoucherNo()); // generate Voucher Number

			simRegistrationDTO.setVouTotalAmount(totalAmount);
			simRegistrationDTO.setVouCreatedBy(sessionBackingBean.getLoginUser());
			simRegistrationDTO.setVouCreatedDate(new Date());

			simRegistrationService.addVoucher(simRegistrationDTO, voucherDTOList);

			successStatus = "Voucher Generated Succesfully";
			sessionBackingBean.showMessage("Success", successStatus, "SUCCESS_DIALOG");

			renderGenerateVoucher = false;
			renderUpdateVoucher = true;
			cancelVoucherCheck = false;

			voucher = simRegEditService.getVoucherDetailsBySimNo(simRegistrationDTO.getSimRegNo());

			if (voucher != null) {

				cancelVoucherCheck = false;
				renderGenerateVoucher = false;
				renderUpdateVoucher = true;

				// set voucher details to simRegDTO
				simRegistrationDTO.setVouNo(voucher.getVouNo());
				simRegistrationDTO.setVouTotalAmount(voucher.getVouTotalAmount());
				simRegistrationDTO.setReceiptNo(voucher.getReceiptNo());
				simRegistrationDTO.setVoucherApprovedStatus(voucher.getVoucherApprovedStatus());
				simRegistrationDTO.setVoucherSeqNo(voucher.getVoucherSeqNo());

				voucherDTOList = new ArrayList<>();
				if (voucher.getVouPrint() != null) {
					disableReprint = false;
				}
				voucherDTOList = simRegEditService.getChargeDetailsByVoucherSeq(simRegistrationDTO.getVoucherSeqNo());

			} else {
				cancelVoucherCheck = true;
				voucherDTOList = new ArrayList<>();
				// add voucher initial data
				voucherDTOList = simRegistrationService.getChargeDetailsList();
				voucherDTO = new SimRegistrationDTO();

				renderUpdateVoucher = false;
				renderGenerateVoucher = true;
			}
		}
	}

	// update generated voucher
	public void updateVoucher() {
		if (voucherDTOList.isEmpty()) {
			errorStatus = "Add Charges to Continue.";
			sessionBackingBean.showMessage("Error", errorStatus, "ERROR_DIALOG");
		} else {
			// set data for Update SIM Voucher
			simRegistrationDTO.setVouTotalAmount(totalAmount);
			simRegistrationDTO.setVouModifiedBy(sessionBackingBean.getLoginUser());
			simRegistrationDTO.setVouModifiedDate(new Date());

			/** Insert history data **/
			if (simRegistrationDTO.getVouNo() != null) {
				simRegistrationService.insertInHistory(simRegistrationDTO.getVouNo());
			}
			/** end **/

			simRegistrationService.updateVoucher(simRegistrationDTO, voucherDTOList);

			successStatus = "generated Voucher Updated Succesfully";
			sessionBackingBean.showMessage("Success", successStatus, "SUCCESS_DIALOG");

		}
	}

	public StreamedContent rePrintVoucher() throws JRException {

		valueForPrint = voucher.getVouNo();

		String loginUser = sessionBackingBean.getLoginUser();
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//debitVoucherForSimRegistration.jrxml";

		try {
			conn = ConnectionManager.getConnection();

			// Parameters for report
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
		return files;

	}

	public StreamedContent printVoucher() throws JRException {

		valueForPrint = simRegistrationDTO.getVouNo();

		String loginUser = sessionBackingBean.getLoginUser();

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//debitVoucherForSimRegistration.jrxml";

		try {
			conn = ConnectionManager.getConnection();

			// Parameters for report
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
			/** Update is print as Y **/
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

		return files;
	}

	// view permit history

	public void viewBtnAction() {
		RequestContext.getCurrentInstance().execute("PF('hisDet').show()");
		if (simRegistrationDTO.getSimRegNo() != null && !simRegistrationDTO.getSimRegNo().trim().isEmpty()) {

			oldPermitDetails = simRegistrationService.getOldPermitDetails(simRegistrationDTO.getSimRegNo());

		}

	}

	// Setters and Getters

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

	public SimRegEditService getSimRegEditService() {
		return simRegEditService;
	}

	public void setSimRegEditService(SimRegEditService simRegEditService) {
		this.simRegEditService = simRegEditService;
	}

	public SimRegistrationDTO getSimSearchDTO() {
		return simSearchDTO;
	}

	public void setSimSearchDTO(SimRegistrationDTO simSearchDTO) {
		this.simSearchDTO = simSearchDTO;
	}

	public List<SimRegistrationDTO> getSimDetailsList() {
		return simDetailsList;
	}

	public void setSimDetailsList(List<SimRegistrationDTO> simDetailsList) {
		this.simDetailsList = simDetailsList;
	}

	public SimRegistrationDTO getVoucher() {
		return voucher;
	}

	public void setVoucher(SimRegistrationDTO voucher) {
		this.voucher = voucher;
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

	public boolean isCancelVoucherCheck() {
		return cancelVoucherCheck;
	}

	public void setCancelVoucherCheck(boolean cancelVoucherCheck) {
		this.cancelVoucherCheck = cancelVoucherCheck;
	}

	public boolean isDisableEmiNo() {
		return disableEmiNo;
	}

	public void setDisableEmiNo(boolean disableEmiNo) {
		this.disableEmiNo = disableEmiNo;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
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

	public boolean isDisableReprint() {
		return disableReprint;
	}

	public void setDisableReprint(boolean disableReprint) {
		this.disableReprint = disableReprint;
	}

	public boolean isDisableButton() {
		return disableButton;
	}

	public void setDisableButton(boolean disableButton) {
		this.disableButton = disableButton;
	}

	public boolean isDisableViewBtn() {
		return disableViewBtn;
	}

	public void setDisableViewBtn(boolean disableViewBtn) {
		this.disableViewBtn = disableViewBtn;
	}

	public List<SimRegistrationDTO> getOldPermitDetails() {
		return oldPermitDetails;
	}

	public void setOldPermitDetails(List<SimRegistrationDTO> oldPermitDetails) {
		this.oldPermitDetails = oldPermitDetails;
	}

	public boolean isDisableButtonUp() {
		return disableButtonUp;
	}

	public void setDisableButtonUp(boolean disableButtonUp) {
		this.disableButtonUp = disableButtonUp;
	}

}
