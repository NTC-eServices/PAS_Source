package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;

@ManagedBean(name = "createRefundVoucher")
@ViewScoped
public class CreateRefundVoucherBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private TenderService tenderService;
	private TenderDTO tenderDTO, selectDTO, generatedDTO;
	private List<TenderDTO> accountNoList = new ArrayList<>();
	private List<TenderDTO> tenderRefNoList = new ArrayList<>();
	private List<TenderDTO> tenderAppNoList = new ArrayList<>();
	private String errorMessage, successMessage, alertMSG;
	private boolean disabledClear, disabledReprint, disabledGenerateVoucher, disabledRemark, disabledAmount,
			disabledApplicationNO, disabledAccountNo;
	private LocalDateTime ldt;
	// print voucher
	private StreamedContent files;

	public CreateRefundVoucherBackingBean() {
		tenderDTO = new TenderDTO();
		selectDTO = new TenderDTO();
		generatedDTO = new TenderDTO();
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		loadValues();

	}

	public void loadValues() {

		tenderDTO.setTransType("TENDER");
		tenderDTO.setTransCode("01");
		ldt = LocalDateTime.now();
		String date = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH).format(ldt);
		tenderDTO.setDate(date);
		disabledAccountNo = true;
		disabledAmount = true;
		disabledClear = true;
		disabledGenerateVoucher = true;
		disabledRemark = true;
		disabledReprint = true;
		disabledApplicationNO = true;
		accountNoList = tenderService.getAccountNo(tenderDTO);
		tenderRefNoList = tenderService.getTenderRefNoForRefund(tenderDTO);

	}

	public void ajaxFillAmount() {
		tenderDTO.setAmount(tenderService.getAmount(tenderDTO));
	}

	public void ajaxFillApplicationNO() {
		tenderAppNoList = tenderService.getRejectedApplicationNo(tenderDTO);

		if (!tenderAppNoList.isEmpty()) {
			disabledApplicationNO = false;
		} else {
			setErrorMessage("Selected Tender Ref. No. Does Not Have Application No.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void ajaxClearVoucherNo() {
		tenderDTO.setRefundvoucherNo(null);
		tenderDTO.setAmount(tenderService.getAmount(tenderDTO));
	}

	public void search() {
		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {

			if (tenderDTO.getTenderAppNo() != null && !tenderDTO.getTenderAppNo().trim().equalsIgnoreCase("")) {

				boolean isVoucherGenerated = tenderService.isAlreadyGenerateVoucher(tenderDTO);

				if (isVoucherGenerated == false) {

					disabledAccountNo = false;
					disabledAmount = false;
					disabledClear = false;
					disabledGenerateVoucher = false;
					disabledRemark = false;

				} else {

					setErrorMessage("Voucher Already Generated.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

					generatedDTO = tenderService.getGeneratedData(tenderDTO);
					tenderDTO.setAccountNo(generatedDTO.getAccountNo());
					tenderDTO.setAmount(generatedDTO.getAmount());
					tenderDTO.setRefundvoucherNo(generatedDTO.getRefundvoucherNo());
					tenderDTO.setVoucherRemark(generatedDTO.getVoucherRemark());

					disabledReprint = false;
					disabledClear = true;
					disabledGenerateVoucher = true;

				}

			} else {
				setErrorMessage("Please Select Application No.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select Tender Reference No.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public StreamedContent generateVoucher() throws JRException {
		files = null;
		String sourceFileName = null;
		Connection conn = null;
		String loginUser = sessionBackingBean.getLoginUser();
		String value = null;
		if (tenderDTO.getAccountNo() != null && !tenderDTO.getAccountNo().trim().equalsIgnoreCase("")) {

			value = tenderService.generateReferenceNoForRefund();

			boolean generateVoucher = tenderService.generateVoucher(tenderDTO, value, loginUser);
			boolean isVoucherDetailsUpdated = tenderService.updateVoucherDetails(tenderDTO, value, loginUser, ldt);

			if (generateVoucher == true && isVoucherDetailsUpdated == true) {

				tenderDTO.setRefundvoucherNo(value);
				disabledReprint = false;
				disabledGenerateVoucher = true;
				tenderService.updateTenderApplicant(tenderDTO);

				try {
					conn = ConnectionManager.getConnection();

					sourceFileName = "..//reports//RefundVoucher.jrxml";
					String logopath = "//lk//informatics//ntc//view//reports//";
					// Parameters for report
					Map<String, Object> parameters = new HashMap<String, Object>();

					parameters.put("P_Voucher_No", value);
					parameters.put("P_LOGO", logopath);

					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

					jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
					jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
					byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					files = new DefaultStreamedContent(stream, "Application/pdf", "Refund Voucher.pdf");

					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "pdf");

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					ConnectionManager.close(conn);
				}

				setSuccessMessage("Voucher Generated Successfully.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			} else {
				setErrorMessage("Voucher Generate Failed.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select Account No.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		return files;
	}

	public StreamedContent rePrint() throws JRException {

		files = null;
		String sourceFileName = null;
		Connection conn = null;
		String value = tenderDTO.getRefundvoucherNo();
		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//RefundVoucher.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";
			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Voucher_No", value);
			parameters.put("P_LOGO", logopath);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Refund Voucher.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		setSuccessMessage("Voucher Reprinted Successfully.");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		return files;

	}

	public void clearTwo() {

		tenderDTO.setAmount(null);
		tenderDTO.setAccountNo(null);
		tenderDTO.setVoucherRemark(null);

	}

	public void clearOne() {

		tenderDTO.setAmount(null);
		tenderDTO.setAccountNo(null);
		tenderDTO.setVoucherRemark(null);
		tenderDTO.setTenderAppNo(null);
		tenderDTO.setTenderRefNo(null);
		tenderDTO.setRefundvoucherNo(null);
		disabledAccountNo = true;
		disabledAmount = true;
		disabledClear = true;
		disabledGenerateVoucher = true;
		disabledRemark = true;
		disabledReprint = true;
		disabledApplicationNO = true;

	}

	public TenderService getTenderService() {
		return tenderService;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
	}

	public List<TenderDTO> getTenderAppNoList() {
		return tenderAppNoList;
	}

	public void setTenderAppNoList(List<TenderDTO> tenderAppNoList) {
		this.tenderAppNoList = tenderAppNoList;
	}

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
	}

	public TenderDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(TenderDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public TenderDTO getGeneratedDTO() {
		return generatedDTO;
	}

	public void setGeneratedDTO(TenderDTO generatedDTO) {
		this.generatedDTO = generatedDTO;
	}

	public List<TenderDTO> getTenderRefNoList() {
		return tenderRefNoList;
	}

	public void setTenderRefNoList(List<TenderDTO> tenderRefNoList) {
		this.tenderRefNoList = tenderRefNoList;
	}

	public List<TenderDTO> getAccountNoList() {
		return accountNoList;
	}

	public void setAccountNoList(List<TenderDTO> accountNoList) {
		this.accountNoList = accountNoList;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public boolean isDisabledClear() {
		return disabledClear;
	}

	public void setDisabledClear(boolean disabledClear) {
		this.disabledClear = disabledClear;
	}

	public boolean isDisabledReprint() {
		return disabledReprint;
	}

	public void setDisabledReprint(boolean disabledReprint) {
		this.disabledReprint = disabledReprint;
	}

	public boolean isDisabledGenerateVoucher() {
		return disabledGenerateVoucher;
	}

	public void setDisabledGenerateVoucher(boolean disabledGenerateVoucher) {
		this.disabledGenerateVoucher = disabledGenerateVoucher;
	}

	public boolean isDisabledRemark() {
		return disabledRemark;
	}

	public void setDisabledRemark(boolean disabledRemark) {
		this.disabledRemark = disabledRemark;
	}

	public boolean isDisabledAmount() {
		return disabledAmount;
	}

	public void setDisabledAmount(boolean disabledAmount) {
		this.disabledAmount = disabledAmount;
	}

	public boolean isDisabledApplicationNO() {
		return disabledApplicationNO;
	}

	public void setDisabledApplicationNO(boolean disabledApplicationNO) {
		this.disabledApplicationNO = disabledApplicationNO;
	}

	public boolean isDisabledAccountNo() {
		return disabledAccountNo;
	}

	public void setDisabledAccountNo(boolean disabledAccountNo) {
		this.disabledAccountNo = disabledAccountNo;
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

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

}
