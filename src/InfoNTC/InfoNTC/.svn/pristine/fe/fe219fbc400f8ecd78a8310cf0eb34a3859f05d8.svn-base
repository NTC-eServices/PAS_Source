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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.AdvancedPaymentDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "createAdvancePaymentVoucherBackingBean")
@ViewScoped
public class CreateAdvancedPaymentBckingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	public PaymentVoucherService paymentVoucherService;
	public EmployeeProfileService employeeProfileService;
	private List<CommonDTO> transactionTypeList;
	private List<CommonDTO> deparmentList;
	private List<CommonDTO> unitList;
	private PaymentVoucherDTO paymentVoucherDTO = new PaymentVoucherDTO();
	private AdvancedPaymentDTO advancedPaymentDTO = new AdvancedPaymentDTO();
	private List<AdvancedPaymentDTO> paymentDetails;
	private AdvancedPaymentDTO dto;
	private String date1;
	private String vouherNo, selectingApplicationNo, selectingPermitNo, successMessage;
	private String errorMsg;
	private BigDecimal totalfee;
	private boolean disable;
	// added
	private StreamedContent files;
	private String value;
	private boolean generatebtn;
	private boolean printbtn;

	@PostConstruct
	public void init() {
		LoadValues();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date1 = dateFormat.format(date);
		// vouherNo = paymentVoucherService.generateReferenceNo();
		advancedPaymentDTO = new AdvancedPaymentDTO();
		disable = false;
		setPrintbtn(true);
		setGeneratebtn(true);

	}

	public void LoadValues() {
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		setTransactionTypeList(paymentVoucherService.GetTransactionToDropdown());
		deparmentList = employeeProfileService.GetDepartmentsToDropdown();
		unitList = employeeProfileService.GetUnitsToDropdown();
	}

	public void completeAppNo() {

		selectingApplicationNo = paymentVoucherService.getApplicationNo(paymentVoucherDTO.getPermitNo());
		paymentVoucherDTO.setApplicationNo(selectingApplicationNo);

	}

	public void completePermitNo() {
		selectingPermitNo = paymentVoucherService.getPermitNo(paymentVoucherDTO.getApplicationNo());
		paymentVoucherDTO.setPermitNo(selectingPermitNo);

	}

	public void totalBusFareValidation() {

		if (advancedPaymentDTO.getAmount() != null && advancedPaymentDTO.getAmount().signum() < 0) {
			errorMsg = "Invalid Amount";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
			advancedPaymentDTO.setAmount(null);
		}

	}

	public void noOfUnitValidation() {

		if (advancedPaymentDTO.getNoofUnits() != null && advancedPaymentDTO.getNoofUnits().signum() < 0) {
			errorMsg = "Invalid No.of Units/Rate";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
			advancedPaymentDTO.setNoofUnits(null);
		}

	}

	// TO DO : If selected transaction type is Tender then application no should
	// get from the Tender table.
	public void validateApplicationNo() {
		if (paymentVoucherDTO.getTransactionCode().equals("01")) {

		}
	}

	public void addNewChargeDetails() {
		if (paymentVoucherDTO.getTransactionCode() != null && !paymentVoucherDTO.getTransactionCode().isEmpty()) {
			if (paymentVoucherDTO.getDepartmentCode() != null && !paymentVoucherDTO.getDepartmentCode().isEmpty()) {
				if (paymentVoucherDTO.getUnitCode() != null && !paymentVoucherDTO.getUnitCode().isEmpty()) {
					if (advancedPaymentDTO.getServiceDetails() != null
							&& !advancedPaymentDTO.getServiceDetails().isEmpty()) {
						if (advancedPaymentDTO.getNoofUnits() != null) {
							if (advancedPaymentDTO.getAmount() != null) {

								dto = new AdvancedPaymentDTO(advancedPaymentDTO.getServiceDetails(),
										advancedPaymentDTO.getNoofUnits(), advancedPaymentDTO.getAmount(), date1);

								if (paymentDetails == null) {
									paymentDetails = new ArrayList<AdvancedPaymentDTO>();
									paymentDetails.add(dto);
									advancedPaymentDTO = new AdvancedPaymentDTO();
									BigDecimal d = new BigDecimal(0);
									BigDecimal amt = null;
									amt = dto.getAmount();
									totalfee = d.add(amt);
									// totalfee +=
									// Double.parseDouble(dto.getAmount());
									clearEnteredVal();
								} else {
									boolean isfound = false;

									for (int i = 0; i < paymentDetails.size(); i++) {

										if (paymentDetails.get(i).getServiceDetails().equals(dto.getServiceDetails())) {
											isfound = true;
											break;
										} else {
											isfound = false;
										}

									}
									if (isfound == false) {
										paymentDetails.add(dto);
										advancedPaymentDTO = new AdvancedPaymentDTO();
										BigDecimal amt = null;
										amt = dto.getAmount();
										totalfee = totalfee.add(amt);
										// totalfee +=
										// Double.parseDouble(dto.getAmount());
									} else {
										errorMsg = "Duplicate Details of Service or Items Provided.";
										sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
										return;
									}
								}
							} else {
								errorMsg = "Amounts should be entered.";
								sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
								return;
							}
						} else {
							errorMsg = "No. of Units should be entered.";
							sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
							return;
						}
					} else {
						errorMsg = "Details of Service or Items Provided should be entered.";
						sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
						return;
					}
				}

				else {
					errorMsg = "Unit should be selected.";
					sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
					return;
				}
			} else {
				errorMsg = "Department should be selected.";
				sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
				return;
			}
		} else {
			errorMsg = "Transaction Type should be selected.";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");

			return;
		}

		if (paymentDetails.size() > 0) {
			setGeneratebtn(false);
		}
	}

	public void delete(String serviceDescription) {

		for (int i = 0; i < paymentDetails.size(); i++) {

			if (paymentDetails.get(i).getServiceDetails().equals(serviceDescription)) {
				BigDecimal amt = paymentDetails.get(i).getAmount();
				amt = paymentDetails.get(i).getAmount();
				totalfee = totalfee.subtract(amt);
				// totalfee = totalfee -
				// Double.parseDouble(paymentDetails.get(i).getAmount());
				setTotalfee(totalfee);
				paymentDetails.remove((i));

			}
		}

	}

	public void clearEnteredVal() {
		advancedPaymentDTO = new AdvancedPaymentDTO();
	}

	public void clearMain() {
		advancedPaymentDTO = new AdvancedPaymentDTO();
		paymentVoucherDTO = new PaymentVoucherDTO();
		paymentDetails = null;
		setDisable(false);
		totalfee = null;
		setPrintbtn(true);
		setGeneratebtn(true);
	}

	public void generateVoucher() {
		String logInUser = sessionBackingBean.getLoginUser();
		int result = paymentVoucherService.insertPaymentVoucher(paymentVoucherDTO, logInUser, totalfee, paymentDetails);
		if (result == 0) {
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			setDisable(true);
			setPrintbtn(false);
			setGeneratebtn(true);
		}

	}

	public StreamedContent printVoucher() throws JRException {

		value = paymentVoucherDTO.getVoucherNo();

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports// AdvancepaymentConfirmationReceiptNew.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Voucher_No", value);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "PaymentVoucher.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<CommonDTO> getDeparmentList() {
		return deparmentList;
	}

	public void setDeparmentList(List<CommonDTO> deparmentList) {
		this.deparmentList = deparmentList;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public List<CommonDTO> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<CommonDTO> unitList) {
		this.unitList = unitList;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public AdvancedPaymentDTO getAdvancedPaymentDTO() {
		return advancedPaymentDTO;
	}

	public void setAdvancedPaymentDTO(AdvancedPaymentDTO advancedPaymentDTO) {
		this.advancedPaymentDTO = advancedPaymentDTO;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getVouherNo() {
		return vouherNo;
	}

	public void setVouherNo(String vouherNo) {
		this.vouherNo = vouherNo;
	}

	public BigDecimal getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(BigDecimal totalfee) {
		this.totalfee = totalfee;
	}

	public String getSelectingApplicationNo() {
		return selectingApplicationNo;
	}

	public void setSelectingApplicationNo(String selectingApplicationNo) {
		this.selectingApplicationNo = selectingApplicationNo;
	}

	public String getSelectingPermitNo() {
		return selectingPermitNo;
	}

	public void setSelectingPermitNo(String selectingPermitNo) {
		this.selectingPermitNo = selectingPermitNo;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public AdvancedPaymentDTO getDto() {
		return dto;
	}

	public void setDto(AdvancedPaymentDTO dto) {
		this.dto = dto;
	}

	public List<AdvancedPaymentDTO> getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(List<AdvancedPaymentDTO> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public List<CommonDTO> getTransactionTypeList() {
		return transactionTypeList;
	}

	public void setTransactionTypeList(List<CommonDTO> transactionTypeList) {
		this.transactionTypeList = transactionTypeList;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isGeneratebtn() {
		return generatebtn;
	}

	public void setGeneratebtn(boolean generatebtn) {
		this.generatebtn = generatebtn;
	}

	public boolean isPrintbtn() {
		return printbtn;
	}

	public void setPrintbtn(boolean printbtn) {
		this.printbtn = printbtn;
	}

}
