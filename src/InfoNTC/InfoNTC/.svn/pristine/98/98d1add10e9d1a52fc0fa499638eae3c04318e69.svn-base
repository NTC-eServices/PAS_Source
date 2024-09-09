package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "driverConductorPaymentVoucherBckingBean")
@ViewScoped
public class DriverConductorPaymentVoucherBckingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private DriverConductorRegistrationDTO driverConductorPaymentDTO;
	public PaymentVoucherService paymentVoucherService;

	private DriverConductorTrainingService driverConductorPaymentService;
	private List<DriverConductorRegistrationDTO> traniningTypeList = new ArrayList<>();
	private DriverConductorRegistrationDTO accountAmountNoDTO = new DriverConductorRegistrationDTO();
	private List<DriverConductorRegistrationDTO> applicationNoList = new ArrayList<DriverConductorRegistrationDTO>(0);
	private List<DriverConductorRegistrationDTO> idNoList = new ArrayList<>();
	private List<DriverConductorRegistrationDTO> chargeTypeDetails = new ArrayList<>();
	private DriverConductorRegistrationDTO addListDTO = new DriverConductorRegistrationDTO();
	private List<DriverConductorRegistrationDTO> driverConducIdList = new ArrayList<>();
	private DriverConductorRegistrationDTO driverConducIdDto;
	private List<DriverConductorRegistrationDTO> chargeTypeList = new ArrayList<DriverConductorRegistrationDTO>(0);
	private List<String> accountNoList = new ArrayList<String>(0);
	private DriverConductorRegistrationDTO selectDTO;

	private String value, errorMessage, selectingApplicationNo, selectingPermitNo, logInUser, successMessage;
	private List<PaymentVoucherDTO> voucherDetails;
	private List<PaymentVoucherDTO> detailsList = new ArrayList<PaymentVoucherDTO>(0);
	private BigDecimal totalfee;
	private boolean disableAdd, disableClearTwo, isSearched, isVoucherGenerate, disableRePrint, isPhotoUpload,
			wasGenerated, disableGenarate, disablePrint, disableChargeType, disableAccountNo, disableAmount,
			isPhotoUploadHistory, isApproved, diableApplicationNo, editMood = false, skipChargeTypes,
			disabledskipChargeTypes, disabledDelete, disabledEdit, disabledFiled, disabledsearch, disableModeOne,
			isSisuSariya, disableDCId;
	private BigDecimal input1 = new BigDecimal(0);

	private String date1, transactionCode, generatedVoucherNo;
	private CommonService commonService;

	private boolean idNoDropDownShow;
	private StreamedContent files;

	public DriverConductorPaymentVoucherBckingBean() {
		driverConductorPaymentService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		traniningTypeList = driverConductorPaymentService.getTrainingTypeList();
		chargeTypeList = driverConductorPaymentService.getChargeType();
		driverConductorPaymentDTO = new DriverConductorRegistrationDTO();

		selectDTO = new DriverConductorRegistrationDTO();

		disableGenarate = true;
		disableAdd = true;
		disableClearTwo = true;
		disablePrint = true;
		input1 = null;
		disableRePrint = true;
		disableChargeType = true;
		disableAccountNo = true;
		disableAmount = true;
		diableApplicationNo = true;
		disabledskipChargeTypes = true;
		idNoDropDownShow = true;
		disableDCId = true;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date1 = dateFormat.format(date);

	}

	public void ajaxFillApplicationNo() {
		disableDCId = true;
		diableApplicationNo = false;
		driverConductorPaymentDTO.setNic(null);
		idNoDropDownShow = true;
		driverConductorPaymentDTO.setDriverConductorId(null);
		chargeTypeDetails = new ArrayList<>();
		applicationNoList = driverConductorPaymentService
				.getAppNolistForselectedTraining(driverConductorPaymentDTO.getTrainingTypeCode());

	}

	public void completeIdNoList() {
		disableDCId = true;
		idNoDropDownShow = false;
		idNoList = driverConductorPaymentService.getIdNoListForSelectedAppNo(
				driverConductorPaymentDTO.getTrainingTypeCode(), driverConductorPaymentDTO.getAppNo());

	}

	public void completeDriverConducIdNo() {
		disableDCId = false;
		driverConducIdDto = driverConductorPaymentService.getDriverConducByID(driverConductorPaymentDTO.getAppNo(),
				driverConductorPaymentDTO.getNic());
		driverConductorPaymentDTO.setDriverConductorId(driverConducIdDto.getDriverConductorId());
		disabledsearch = false;
	}

	public void searchVoucher() {
		String loginUser = sessionBackingBean.getLoginUser();
		if (driverConductorPaymentDTO.getTrainingTypeCode() != null && driverConductorPaymentDTO.getAppNo() != null
				&& driverConductorPaymentDTO.getNic() != null
				&& driverConductorPaymentDTO.getDriverConductorId() != null) {
			disableChargeType = false;
			disableAmount = false;
			generatedVoucherNo = driverConductorPaymentService.getVoucherNo(driverConductorPaymentDTO.getAppNo(),
					driverConductorPaymentDTO.getTrainingTypeCode());
			value = generatedVoucherNo;
			if (value != null) {
				disableRePrint = false;
			}
			/* show charge type data already enterd */
			chargeTypeDetails = driverConductorPaymentService
					.getChargeTypeDetails(driverConductorPaymentDTO.getTrainingTypeCode());
			totalfee = new BigDecimal(0);

			for (int i = 0; i < chargeTypeDetails.size(); i++) {

				try {

					BigDecimal amt = null;
					amt = chargeTypeDetails.get(i).getAmmount();

					totalfee = totalfee.add(amt);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (chargeTypeDetails.equals(null) || chargeTypeDetails.isEmpty()) {
				disableAdd = true;
				disableAmount = true;
				disableChargeType = false;
				disableClearTwo = true;
				disableAccountNo = true;
				errorMessage = "No charge type entered";
				sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
				return;

			} else {
				disableAdd = true;
				disableAmount = true;
				disableChargeType = false;
				disableAccountNo = true;
				disableClearTwo = false;

				disableGenarate = false;
			}
		} else {

			errorMessage = "Please enter all mandatory fields";
			sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
			return;
		}

	}

	public void addNewCharge() {
		boolean alreadyadded = false;
		if (editMood == true) {

			if (driverConductorPaymentDTO.getChargeTypeCode() != null) {
				if (driverConductorPaymentDTO.getAccountNO() != null) {
					if (driverConductorPaymentDTO.getAmmount() != null) {
						disableGenarate = false;
						String chargeTypeDes = driverConductorPaymentService
								.getChargeTypeDes(driverConductorPaymentDTO.getChargeTypeCode());
						selectDTO.setChargeTypeDes(chargeTypeDes);
						selectDTO.setChargeTypeCode(driverConductorPaymentDTO.getChargeTypeCode());
						selectDTO.setAccountNO(driverConductorPaymentDTO.getAccountNO());
						selectDTO.setAmmount(driverConductorPaymentDTO.getAmmount());
						BigDecimal amt = driverConductorPaymentDTO.getAmmount();

						this.totalfee = totalfee.add(amt);

						driverConductorPaymentService.beanLinkMethod(driverConductorPaymentDTO,
								sessionBackingBean.getLoginUser(), "Add New Charge",
								"Create Payment Voucher For Driver/Conductor Training");
					} else {
						disableAdd = false;
						errorMessage = "Please enter amount";
						sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
						return;
					}
				} else {
					disableAdd = false;
					errorMessage = "Please enter account no.";
					sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
					return;
				}

			} else {
				disableAdd = false;
				errorMessage = "Please select charge type";
				sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
				return;

			}
		} else {
			if (driverConductorPaymentDTO.getChargeTypeCode() != null) {
				if (driverConductorPaymentDTO.getAccountNO() != null) {
					if (driverConductorPaymentDTO.getAmmount() != null) {

						addListDTO = new DriverConductorRegistrationDTO();
						disableGenarate = false;
						String chargeTypeDes = driverConductorPaymentService
								.getChargeTypeDes(driverConductorPaymentDTO.getChargeTypeCode());
						addListDTO.setChargeTypeDes(chargeTypeDes);
						addListDTO.setChargeTypeCode(driverConductorPaymentDTO.getChargeTypeCode());
						addListDTO.setAccountNO(driverConductorPaymentDTO.getAccountNO());
						addListDTO.setAmmount(driverConductorPaymentDTO.getAmmount());
						BigDecimal amt = driverConductorPaymentDTO.getAmmount();

						this.totalfee = totalfee.add(amt);
						for (DriverConductorRegistrationDTO dcDTO : chargeTypeDetails) {
							if (dcDTO.getChargeTypeDes().equals(addListDTO.getChargeTypeDes())) {
								alreadyadded = true;
								break;
							} else {
								alreadyadded = false;
							}

						}
						if (alreadyadded) {
							errorMessage = "Already added.";
							sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
							return;
						} else {
							chargeTypeDetails.add(addListDTO);
						}

					} else {
						disableAdd = false;
						errorMessage = "Please enter amount";
						sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
						return;
					}
				} else {
					disableAdd = false;
					errorMessage = "Please enter account no.";
					sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
					return;
				}

			} else {
				disableAdd = false;
				errorMessage = "Please select charge type";
				sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
				return;

			}

		}
	}

	public void clearOne() {

		driverConductorPaymentDTO = new DriverConductorRegistrationDTO();
		totalfee = null;
		disableAccountNo = true;
		disableAdd = true;
		disableClearTwo = true;
		disableRePrint = true;
		diableApplicationNo = true;
		disabledskipChargeTypes = true;
		disableGenarate = true;
		disableChargeType = true;
		value = null;
		disabledFiled = false;
		diableApplicationNo = false;
		disabledsearch = false;

	}

	public void delete(String chargeDescription) {

		for (int i = 0; i < chargeTypeDetails.size(); i++) {

			if (chargeTypeDetails.get(i).getChargeTypeDes().equals(chargeDescription)) {
				BigDecimal valFromArray = chargeTypeDetails.get(i).getAmmount();
				this.totalfee = totalfee.subtract(valFromArray);
				setTotalfee(totalfee);
				chargeTypeDetails.remove((i));

				driverConductorPaymentService.beanLinkMethod(driverConductorPaymentDTO,
						sessionBackingBean.getLoginUser(), "Delete Charge Details",
						"Create Payment Voucher For Driver/Conductor Training");

			}
		}

	}

	public void ajaxFilterAcountNo() {
		if (driverConductorPaymentDTO.getChargeTypeCode() != null
				&& !driverConductorPaymentDTO.getChargeTypeCode().trim().isEmpty()) {

			disableAdd = false;
			disableAmount = false;
			disableChargeType = false;
			disableAccountNo = false;
			disableClearTwo = false;

			accountAmountNoDTO = driverConductorPaymentService
					.getAccountAndAmountbyChargeType(driverConductorPaymentDTO.getChargeTypeCode());

			if (accountAmountNoDTO == null) {

				disableAccountNo = true;
				errorMessage = "No active acount found selected charge type";
				sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
				return;

			} else {
				driverConductorPaymentDTO.setAccountNO(accountAmountNoDTO.getAccountNO());
				driverConductorPaymentDTO.setAmmount(accountAmountNoDTO.getAmmount());
			}

		}
	}

	public void edit() {
		disableModeOne = true;
		disableChargeType = false;
		disabledsearch = false;
		disableAccountNo = false;
		disableAdd = false;
		disableGenarate = false;
		disabledEdit = false;
		disabledDelete = false;
		disabledskipChargeTypes = false;
		disableAmount = false;

		BigDecimal subtractAmount = null;
		BigDecimal bigDecimalCurrency = selectDTO.getAmmount();
		subtractAmount = bigDecimalCurrency;
		this.totalfee = totalfee.subtract(subtractAmount);

		driverConductorPaymentDTO.setChargeTypeDes(selectDTO.getChargeTypeDes());
		driverConductorPaymentDTO.setChargeTypeCode(selectDTO.getChargeTypeCode());
		driverConductorPaymentDTO.setAmmount(selectDTO.getAmmount());
		ajaxFilterAcountNo();
		driverConductorPaymentDTO.setAccountNO(selectDTO.getAccountNO());

		editMood = true;

	}

	public void clearThree() {

		chargeTypeDetails = new ArrayList<DriverConductorRegistrationDTO>();
		totalfee = new BigDecimal(0);
		value = null;
		disableGenarate = true;

	}

	public void clearTwo() {
		driverConductorPaymentDTO.setAccountNO(null);
		driverConductorPaymentDTO.setAmmount(null);
		driverConductorPaymentDTO.setChargeTypeCode(null);
		driverConductorPaymentDTO.setChargeTypeDes(null);
		addListDTO = new DriverConductorRegistrationDTO();

		disableAccountNo = true;
		disableClearTwo = false;
		skipChargeTypes = false;
		disableChargeType = false;
		disabledskipChargeTypes = false;
		disabledsearch = false;
		disabledDelete = false;
		disabledEdit = false;
		disableModeOne = false;
		disableAdd = false;
		disableGenarate = false;

	}

	public String generateVoucher() {

		String loginUser = sessionBackingBean.getLoginUser();
		String totFee = null;

		if (chargeTypeList != null) {
			logInUser = sessionBackingBean.getLoginUser();

			// check for vouchers with status 'P' (not 'R')
			wasGenerated = driverConductorPaymentService.alreadyGenerate(driverConductorPaymentDTO.getAppNo());

			if (wasGenerated == false) {

				value = driverConductorPaymentService.generateVoucherNo();

				isVoucherGenerate = driverConductorPaymentService.insertVoucherDetInMaster(driverConductorPaymentDTO,
						loginUser, value, totalfee);

				if (isVoucherGenerate == true) {

					/* Update task details and voucher details */

					driverConductorPaymentService.insertVoucherDetailsInDetTable(driverConductorPaymentDTO,
							chargeTypeDetails, loginUser, value);

					disableGenarate = true;

					disablePrint = false;
					disableAdd = true;
					disableClearTwo = true;

					// update status type as V
					driverConductorPaymentService.updateStatusType("V", driverConductorPaymentDTO.getAppNo());
					driverConductorPaymentService.beanLinkMethod(driverConductorPaymentDTO,
							sessionBackingBean.getLoginUser(), "Voucher Generated",
							"Create Payment Voucher For Driver/Conductor Training");
					successMessage = "Voucher generated successfully.";
					sessionBackingBean.showMessage("Success", successMessage, "INFO_DIALOG");
					return value;

				} else {
					errorMessage = "Voucher generate failed.";
					sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
					return null;
				}
			} else {
				setErrorMessage("Voucher already generated.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				disableRePrint = false;
			}

		} else {
			errorMessage = "Atleast one Charge Type should be added.";
			sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
			return value;
		}

		// added for generate voucher
		return value;
	}

	public StreamedContent downloadFile(ActionEvent ae) throws JRException {
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//debitVoucherForDriverConductor.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Parameter", value);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf",
					"PaymentVoucher_Driver_Conductor - " + value + ".pdf");

			disableRePrint = false;
			disablePrint = true;
			RequestContext.getCurrentInstance().update("rePrint");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			driverConductorPaymentService.beanLinkMethod(driverConductorPaymentDTO, sessionBackingBean.getLoginUser(),
					"Download Payment Voucher", "Create Payment Voucher For Driver/Conductor Training");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

	}

	public StreamedContent rePrint(ActionEvent ae) throws JRException {

		value = generatedVoucherNo;
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//debitVoucherForDriverConductor.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Parameter", value);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "PaymentVoucher - " + value + ".pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			driverConductorPaymentService.beanLinkMethod(driverConductorPaymentDTO, sessionBackingBean.getLoginUser(),
					"Generate Charge Report", "Create Payment Voucher For Driver/Conductor Training");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

	}

	public boolean isDisablePrint() {
		return disablePrint;
	}

	public void setDisablePrint(boolean disablePrint) {
		this.disablePrint = disablePrint;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public boolean isDisableChargeType() {
		return disableChargeType;
	}

	public void setDisableChargeType(boolean disableChargeType) {
		this.disableChargeType = disableChargeType;
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

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getLogInUser() {
		return logInUser;
	}

	public void setLogInUser(String logInUser) {
		this.logInUser = logInUser;
	}

	public String getSelectingPermitNo() {
		return selectingPermitNo;
	}

	public BigDecimal getInput1() {
		return input1;
	}

	public void setInput1(BigDecimal input1) {
		this.input1 = input1;
	}

	public List<PaymentVoucherDTO> getDetailsList() {
		return detailsList;
	}

	public void setDetailsList(List<PaymentVoucherDTO> detailsList) {
		this.detailsList = detailsList;
	}

	public boolean isSearched() {
		return isSearched;
	}

	public void setSearched(boolean isSearched) {
		this.isSearched = isSearched;
	}

	public boolean isVoucherGenerate() {
		return isVoucherGenerate;
	}

	public void setVoucherGenerate(boolean isVoucherGenerate) {
		this.isVoucherGenerate = isVoucherGenerate;
	}

	public boolean isPhotoUpload() {
		return isPhotoUpload;
	}

	public void setPhotoUpload(boolean isPhotoUpload) {
		this.isPhotoUpload = isPhotoUpload;
	}

	public boolean isWasGenerated() {
		return wasGenerated;
	}

	public void setWasGenerated(boolean wasGenerated) {
		this.wasGenerated = wasGenerated;
	}

	public boolean isDisableGenarate() {
		return disableGenarate;
	}

	public void setDisableGenarate(boolean disableGenarate) {
		this.disableGenarate = disableGenarate;
	}

	public void setSelectingPermitNo(String selectingPermitNo) {
		this.selectingPermitNo = selectingPermitNo;
	}

	public boolean isDisableRePrint() {
		return disableRePrint;
	}

	public void setDisableRePrint(boolean disableRePrint) {
		this.disableRePrint = disableRePrint;
	}

	public String getSelectingApplicationNo() {
		return selectingApplicationNo;
	}

	public void setSelectingApplicationNo(String selectingApplicationNo) {
		this.selectingApplicationNo = selectingApplicationNo;
	}

	public boolean isDisableClearTwo() {
		return disableClearTwo;
	}

	public void setDisableClearTwo(boolean disableClearTwo) {
		this.disableClearTwo = disableClearTwo;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isDisableAdd() {
		return disableAdd;
	}

	public void setDisableAdd(boolean disableAdd) {
		this.disableAdd = disableAdd;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public BigDecimal getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(BigDecimal totalfee) {
		this.totalfee = totalfee;
	}

	public List<PaymentVoucherDTO> getVoucherDetails() {
		return voucherDetails;
	}

	public void setVoucherDetails(List<PaymentVoucherDTO> voucherDetails) {
		this.voucherDetails = voucherDetails;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getAccountNoList() {
		return accountNoList;
	}

	public void setAccountNoList(List<String> accountNoList) {
		this.accountNoList = accountNoList;
	}

	public List<DriverConductorRegistrationDTO> getChargeTypeList() {
		return chargeTypeList;
	}

	public void setChargeTypeList(List<DriverConductorRegistrationDTO> chargeTypeList) {
		this.chargeTypeList = chargeTypeList;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public boolean isPhotoUploadHistory() {
		return isPhotoUploadHistory;
	}

	public void setPhotoUploadHistory(boolean isPhotoUploadHistory) {
		this.isPhotoUploadHistory = isPhotoUploadHistory;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDiableApplicationNo() {
		return diableApplicationNo;
	}

	public void setDiableApplicationNo(boolean diableApplicationNo) {
		this.diableApplicationNo = diableApplicationNo;
	}

	public boolean isEditMood() {
		return editMood;
	}

	public void setEditMood(boolean editMood) {
		this.editMood = editMood;
	}

	public boolean isSkipChargeTypes() {
		return skipChargeTypes;
	}

	public void setSkipChargeTypes(boolean skipChargeTypes) {
		this.skipChargeTypes = skipChargeTypes;
	}

	public boolean isDisabledskipChargeTypes() {
		return disabledskipChargeTypes;
	}

	public void setDisabledskipChargeTypes(boolean disabledskipChargeTypes) {
		this.disabledskipChargeTypes = disabledskipChargeTypes;
	}

	public boolean isDisabledDelete() {
		return disabledDelete;
	}

	public void setDisabledDelete(boolean disabledDelete) {
		this.disabledDelete = disabledDelete;
	}

	public boolean isDisabledEdit() {
		return disabledEdit;
	}

	public void setDisabledEdit(boolean disabledEdit) {
		this.disabledEdit = disabledEdit;
	}

	public boolean isDisabledFiled() {
		return disabledFiled;
	}

	public void setDisabledFiled(boolean disabledFiled) {
		this.disabledFiled = disabledFiled;
	}

	public boolean isDisabledsearch() {
		return disabledsearch;
	}

	public void setDisabledsearch(boolean disabledsearch) {
		this.disabledsearch = disabledsearch;
	}

	public boolean isDisableModeOne() {
		return disableModeOne;
	}

	public void setDisableModeOne(boolean disableModeOne) {
		this.disableModeOne = disableModeOne;
	}

	public boolean isSisuSariya() {
		return isSisuSariya;
	}

	public void setSisuSariya(boolean isSisuSariya) {
		this.isSisuSariya = isSisuSariya;
	}

	public DriverConductorRegistrationDTO getDriverConductorPaymentDTO() {
		return driverConductorPaymentDTO;
	}

	public void setDriverConductorPaymentDTO(DriverConductorRegistrationDTO driverConductorPaymentDTO) {
		this.driverConductorPaymentDTO = driverConductorPaymentDTO;
	}

	public DriverConductorTrainingService getDriverConductorPaymentService() {
		return driverConductorPaymentService;
	}

	public void setDriverConductorPaymentService(DriverConductorTrainingService driverConductorPaymentService) {
		this.driverConductorPaymentService = driverConductorPaymentService;
	}

	public List<DriverConductorRegistrationDTO> getTraniningTypeList() {
		return traniningTypeList;
	}

	public void setTraniningTypeList(List<DriverConductorRegistrationDTO> traniningTypeList) {
		this.traniningTypeList = traniningTypeList;
	}

	public List<DriverConductorRegistrationDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<DriverConductorRegistrationDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public List<DriverConductorRegistrationDTO> getIdNoList() {
		return idNoList;
	}

	public void setIdNoList(List<DriverConductorRegistrationDTO> idNoList) {
		this.idNoList = idNoList;
	}

	public boolean isIdNoDropDownShow() {
		return idNoDropDownShow;
	}

	public void setIdNoDropDownShow(boolean idNoDropDownShow) {
		this.idNoDropDownShow = idNoDropDownShow;
	}

	public DriverConductorRegistrationDTO getDriverConducIdDto() {
		return driverConducIdDto;
	}

	public void setDriverConducIdDto(DriverConductorRegistrationDTO driverConducIdDto) {
		this.driverConducIdDto = driverConducIdDto;
	}

	public List<DriverConductorRegistrationDTO> getChargeTypeDetails() {
		return chargeTypeDetails;
	}

	public void setChargeTypeDetails(List<DriverConductorRegistrationDTO> chargeTypeDetails) {
		this.chargeTypeDetails = chargeTypeDetails;
	}

	public DriverConductorRegistrationDTO getAccountAmountNoDTO() {
		return accountAmountNoDTO;
	}

	public void setAccountAmountNoDTO(DriverConductorRegistrationDTO accountAmountNoDTO) {
		this.accountAmountNoDTO = accountAmountNoDTO;
	}

	public DriverConductorRegistrationDTO getAddListDTO() {
		return addListDTO;
	}

	public void setAddListDTO(DriverConductorRegistrationDTO addListDTO) {
		this.addListDTO = addListDTO;
	}

	public DriverConductorRegistrationDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(DriverConductorRegistrationDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public List<DriverConductorRegistrationDTO> getDriverConducIdList() {
		return driverConducIdList;
	}

	public void setDriverConducIdList(List<DriverConductorRegistrationDTO> driverConducIdList) {
		this.driverConducIdList = driverConducIdList;
	}

	public boolean isDisableDCId() {
		return disableDCId;
	}

	public void setDisableDCId(boolean disableDCId) {
		this.disableDCId = disableDCId;
	}

	public String getGeneratedVoucherNo() {
		return generatedVoucherNo;
	}

	public void setGeneratedVoucherNo(String generatedVoucherNo) {
		this.generatedVoucherNo = generatedVoucherNo;
	}

}
