package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PaymentTypeDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.TerminalDetailsDTO;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.RouteCreatorService;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.ConvertNumberToWord;
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

@ViewScoped
@ManagedBean(name = "acceptPaymentViewBackingBean")
public class AcceptPaymentViewBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String sucessMsg;
	private String errorMsg;

	String dateFormatStr = "dd/MM/yyyy";
	DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);

	private TerminalManagementService terminalManagementService;
	private RouteCreatorService routeCreatorService;
	private EmployeeProfileService employeeProfileService;
	List<RouteCreationDTO> routeList = new ArrayList<RouteCreationDTO>();

	private TerminalDetailsDTO accpetPayViewDTO = new TerminalDetailsDTO();
	private TerminalDetailsDTO showData;
	private List<PaymentTypeDTO> payTypeList;
	private List<TerminalDetailsDTO> receiptRefNo;
	private String selectedPayTypeCode, selectedRecieptRefNO;
	private PaymentTypeDTO selectedPayType = new PaymentTypeDTO();
	private List<CommonDTO> genderList;
	private String vehicleRegNo;
	private String dueDate;
	private String paidDate;
	private String issuedDate;
	private String issuedMonth;
	private String validFromDate;
	private String validToDate;
	private String noOfTurn;
	private String payReciptRef;
	private String voucherNo;
	private Long basicAmount;
	private String penalty;
	private String amountPaid;
	private Long totalCollection;
	private String permitNo;
	private String routeOrigin;
	private String routeOriginCode;
	private String routeDestination;
	private String routeDestinationCode;
	private String owner;
	private String gender;
	private String contactNo;
	private long receiptCount;
	private boolean receiptGenerated;
	private String vehicleServiceType;
	private String terminalLocation;

	static String LOG_SHEET_ISSUANCE = "001";
	static String TEMPORARY_PERMIT_ISSUANCE = "002";
	static String SPECIAL_HIRE_PERMIT_ISSUANCE = "003";

	private boolean showGenReciept, enablePermit;
	private boolean showSave;
	Date currentDate = new Date();
	private StreamedContent files;
	private boolean validFromShow, validToShow, noOfTurnShow, issuedMonthShow, issuedDateShow, showPrintButt;

	public AcceptPaymentViewBackingBean() {
		terminalManagementService = (TerminalManagementService) SpringApplicationContex
				.getBean("terminalManagementService");
		routeCreatorService = (RouteCreatorService) SpringApplicationContex.getBean("routeCreatorService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");

		payTypeList = terminalManagementService.getPaymentTypes();
		receiptRefNo = terminalManagementService.getRecieptRefList();
		routeList = routeCreatorService.getAllRoutes();
		genderList = employeeProfileService.GetGenderToDropdown();
		showGenReciept = true;
		showSave = false;
		validFromShow = false;
		validToShow = false;
		noOfTurnShow = false;
		issuedMonthShow = false;
		issuedDateShow = false;
		showPrintButt = true;
		enablePermit = true;
	}

	public void onPaymentTypeChange(ValueChangeEvent e) {
		// clearFields();
		selectedRecieptRefNO = null;
		clearForm();

		// get receipt ref numbers according to payment type
		selectedPayTypeCode = e.getNewValue().toString();

		receiptRefNo = terminalManagementService.getReceiptRefAggainstPayType(selectedPayTypeCode);
	}

	public void onReceiptChange(ValueChangeEvent e) {
		clearForm();
	}

	public void fieldsShow() {
		if (selectedPayTypeCode.equals("001")) {
			issuedDateShow = true;
			issuedMonthShow = true;
		} else if (selectedPayTypeCode.equals("002")) {
			validFromShow = true;
			validToShow = true;
			noOfTurnShow = true;
		} else if (selectedPayTypeCode.equals("003")) {
			validFromShow = true;
			validToShow = true;
			noOfTurnShow = true;
		}
	}

	public void searchDet() {
		boolean isCanceldReciept = false;
		if (selectedPayTypeCode != null) {

			if (selectedRecieptRefNO != null) {
				isCanceldReciept = terminalManagementService.checkReceiptCanceled(selectedRecieptRefNO);
				if (isCanceldReciept) {
					errorMsg = "This Receipt Reference has been canceled";
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");

				} else {
					showData = terminalManagementService.showsearchedData(selectedPayTypeCode, selectedRecieptRefNO);
					if (showData.getVoucherNO() != null) {
						showPrintButt = false;
						enablePermit = false;
					}

					fieldsShow();
					permitNo = showData.getPermitNo();
					vehicleRegNo = showData.getVehiNo();
					routeOrigin = showData.getRouteOrigin();
					dueDate = showData.getDueDate();
					routeDestination = showData.getRouteDestination();
					owner = showData.getOwner();
					voucherNo = showData.getVoucherNO();
					gender = showData.getGender();
					issuedDate = showData.getIssuDate();
					validFromDate = showData.getValidFrom();
					contactNo = showData.getContactNo();
					issuedMonth = showData.getIssueMont();
					validToDate = showData.getValidTo();
					noOfTurn = showData.getNoOfTurns();
					penalty = showData.getPenalty();
					amountPaid = showData.getAmountPaid();
					paidDate = showData.getPaidDate();
					terminalLocation= showData.getTerminalLocation();
				}
			} else {

				errorMsg = "Please select a receipt refference number";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			}
		} else {
			errorMsg = "Please select paymnet type.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");

		}
	}

	public void clearFields() {
		selectedPayTypeCode = null;
		selectedRecieptRefNO = null;
		selectedPayType = new PaymentTypeDTO();
		clearForm();
	}

	public void clearForm() {
		vehicleRegNo = null;
		dueDate = null;
		paidDate = null;
		issuedDate = null;
		issuedMonth = null;
		validFromDate = null;
		validToDate = null;
		noOfTurn = null;
		payReciptRef = null;
		basicAmount = null;
		penalty = null;
		amountPaid = null;
		totalCollection = null;
		permitNo = null;
		routeOrigin = null;
		routeOriginCode = null;
		routeDestination = null;
		routeDestinationCode = null;
		owner = null;
		gender = null;
		voucherNo = null;
		contactNo = null;
		vehicleServiceType = null;
		receiptGenerated = false;
		validFromShow = false;
		validToShow = false;
		noOfTurnShow = false;
		issuedMonthShow = false;
		issuedDateShow = false;
		enablePermit = true;
		terminalLocation= null;
	}

	/*
	 * Generate Receipt modify by tharushi.e
	 */
	public void generateReceipt(ActionEvent ae) throws JRException {
		generateRecieptReport(selectedPayTypeCode);
		enablePermit = false;
	}

	public StreamedContent generateRecieptReport(String selectedPayTypeCode) throws JRException {
		files = null;
		String sourceFileName = null;
		String modifiedBy = sessionBackingBean.loginUser;
		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//TerminalPaymentReceipt.jrxml";

			String logopath = "//lk//informatics//ntc//view//reports//";
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Payment_type_code", selectedPayTypeCode);
			
			parameters.put("P_Reciept_Ref", selectedRecieptRefNO);
			parameters.put("P_user", modifiedBy);
			parameters.put("P_receipt_no", voucherNo);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "TerminalPaymentReceipt.pdf");

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

	public StreamedContent genratePermit() throws JRException {
		files = null;
		String sourceFileName = null;
		String modifiedBy = sessionBackingBean.loginUser;
		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();
			if (selectedPayTypeCode.equals("002")) {
				sourceFileName = "..//reports//TemporyPermitPrePrint.jrxml";
			
			}
			if (selectedPayTypeCode.equals("003")) {
				sourceFileName = "..//reports//SpecialHirePermitPrePrint.jrxml";
			}
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			parameters.put("P_Receipt_No", selectedRecieptRefNO);
			
			if(selectedPayTypeCode.equals("003")) {
				String chargeAmount =terminalManagementService.getChargeAmmount(selectedRecieptRefNO);
				String amountInWord= ConvertNumberToWord.getDecimalValue(chargeAmount);
				
					parameters.put("P_ammount_inLetter", amountInWord.toUpperCase());
				
				
				
			}
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "TemporarayPermit" + vehicleRegNo + ".pdf");
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}
		enablePermit = true;
		return files;

	}

	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	//////////////// getter & setters /////////////////////

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSelectedPayTypeCode() {
		return selectedPayTypeCode;
	}

	public void setSelectedPayTypeCode(String selectedPayTypeCode) {
		this.selectedPayTypeCode = selectedPayTypeCode;
	}

	public List<PaymentTypeDTO> getPayTypeList() {
		return payTypeList;
	}

	public void setPayTypeList(List<PaymentTypeDTO> payTypeList) {
		this.payTypeList = payTypeList;
	}

	public String getVehicleRegNo() {
		return vehicleRegNo;
	}

	public void setVehicleRegNo(String vehicleRegNo) {
		this.vehicleRegNo = vehicleRegNo;
	}

	public String getIssuedMonth() {
		return issuedMonth;
	}

	public void setIssuedMonth(String issuedMonth) {
		this.issuedMonth = issuedMonth;
	}

	public String getPayReciptRef() {
		return payReciptRef;
	}

	public void setPayReciptRef(String payReciptRef) {
		this.payReciptRef = payReciptRef;
	}

	public Long getTotalCollection() {
		return totalCollection;
	}

	public void setTotalCollection(Long totalCollection) {
		this.totalCollection = totalCollection;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getOwner() {
		return owner;
	}

	public String getRouteOrigin() {
		return routeOrigin;
	}

	public void setRouteOrigin(String routeOrigin) {
		this.routeOrigin = routeOrigin;
	}

	public String getRouteDestination() {
		return routeDestination;
	}

	public void setRouteDestination(String routeDestination) {
		this.routeDestination = routeDestination;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public TerminalManagementService getTerminalManagementService() {
		return terminalManagementService;
	}

	public void setTerminalManagementService(TerminalManagementService terminalManagementService) {
		this.terminalManagementService = terminalManagementService;
	}

	public PaymentTypeDTO getSelectedPayType() {
		return selectedPayType;
	}

	public void setSelectedPayType(PaymentTypeDTO selectedPayType) {
		this.selectedPayType = selectedPayType;
	}

	public String getRouteOriginCode() {
		return routeOriginCode;
	}

	public void setRouteOriginCode(String routeOriginCode) {
		this.routeOriginCode = routeOriginCode;
	}

	public String getRouteDestinationCode() {
		return routeDestinationCode;
	}

	public void setRouteDestinationCode(String routeDestinationCode) {
		this.routeDestinationCode = routeDestinationCode;
	}

	public List<RouteCreationDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteCreationDTO> routeList) {
		this.routeList = routeList;
	}

	public String getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(String validFromDate) {
		this.validFromDate = validFromDate;
	}

	public String getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(String validToDate) {
		this.validToDate = validToDate;
	}

	public String getNoOfTurn() {
		return noOfTurn;
	}

	public void setNoOfTurn(String noOfTurn) {
		this.noOfTurn = noOfTurn;
	}

	public boolean isReceiptGenerated() {
		return receiptGenerated;
	}

	public void setReceiptGenerated(boolean receiptGenerated) {
		this.receiptGenerated = receiptGenerated;
	}

	public long getReceiptCount() {
		return receiptCount;
	}

	public void setReceiptCount(long receiptCount) {
		this.receiptCount = receiptCount;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

	public Long getBasicAmount() {
		return basicAmount;
	}

	public void setBasicAmount(Long basicAmount) {
		this.basicAmount = basicAmount;
	}

	public String getPenalty() {
		return penalty;
	}

	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}

	public String getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}

	public List<CommonDTO> getGenderList() {
		return genderList;
	}

	public void setGenderList(List<CommonDTO> genderList) {
		this.genderList = genderList;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isShowGenReciept() {
		return showGenReciept;
	}

	public void setShowGenReciept(boolean showGenReciept) {
		this.showGenReciept = showGenReciept;
	}

	public boolean isShowSave() {
		return showSave;
	}

	public void setShowSave(boolean showSave) {
		this.showSave = showSave;
	}

	public String getSelectedRecieptRefNO() {
		return selectedRecieptRefNO;
	}

	public void setSelectedRecieptRefNO(String selectedRecieptRefNO) {
		this.selectedRecieptRefNO = selectedRecieptRefNO;
	}

	public TerminalDetailsDTO getAccpetPayViewDTO() {
		return accpetPayViewDTO;
	}

	public void setAccpetPayViewDTO(TerminalDetailsDTO accpetPayViewDTO) {
		this.accpetPayViewDTO = accpetPayViewDTO;
	}

	public List<TerminalDetailsDTO> getReceiptRefNo() {
		return receiptRefNo;
	}

	public void setReceiptRefNo(List<TerminalDetailsDTO> receiptRefNo) {
		this.receiptRefNo = receiptRefNo;
	}

	public TerminalDetailsDTO getShowData() {
		return showData;
	}

	public void setShowData(TerminalDetailsDTO showData) {
		this.showData = showData;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public boolean isValidFromShow() {
		return validFromShow;
	}

	public void setValidFromShow(boolean validFromShow) {
		this.validFromShow = validFromShow;
	}

	public boolean isValidToShow() {
		return validToShow;
	}

	public void setValidToShow(boolean validToShow) {
		this.validToShow = validToShow;
	}

	public boolean isNoOfTurnShow() {
		return noOfTurnShow;
	}

	public void setNoOfTurnShow(boolean noOfTurnShow) {
		this.noOfTurnShow = noOfTurnShow;
	}

	public boolean isIssuedMonthShow() {
		return issuedMonthShow;
	}

	public void setIssuedMonthShow(boolean issuedMonthShow) {
		this.issuedMonthShow = issuedMonthShow;
	}

	public boolean isIssuedDateShow() {
		return issuedDateShow;
	}

	public void setIssuedDateShow(boolean issuedDateShow) {
		this.issuedDateShow = issuedDateShow;
	}

	public boolean isShowPrintButt() {
		return showPrintButt;
	}

	public void setShowPrintButt(boolean showPrintButt) {
		this.showPrintButt = showPrintButt;
	}

	public boolean isEnablePermit() {
		return enablePermit;
	}

	public void setEnablePermit(boolean enablePermit) {
		this.enablePermit = enablePermit;
	}

	public String getTerminalLocation() {
		return terminalLocation;
	}

	public void setTerminalLocation(String terminalLocation) {
		this.terminalLocation = terminalLocation;
	}
	
}
