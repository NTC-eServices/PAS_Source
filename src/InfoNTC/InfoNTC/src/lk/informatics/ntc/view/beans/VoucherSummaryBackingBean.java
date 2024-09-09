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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.GenerateReceiptDTO;

import lk.informatics.ntc.model.service.GenerateReceiptService;

import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.ntc.view.util.ConnectionManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "voucherSumarryBackingBean")
@ViewScoped
public class VoucherSummaryBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private GenerateReceiptDTO receiptDto;
	private List<GenerateReceiptDTO> receiptNoList = new ArrayList<GenerateReceiptDTO>();

	private StreamedContent files;

	private String errorMessage;

	// services
	public GenerateReceiptService generateReceiptService;
	private Date endDate;
	private Date startDate;

	@PostConstruct
	public void init() {
		receiptDto = new GenerateReceiptDTO();
		generateReceiptService = (GenerateReceiptService) SpringApplicationContex.getBean("generateReceiptService");
		receiptNoList = generateReceiptService.getReceiptNo();

	}

	public void clearField() {

		startDate = null;
		endDate = null;
		receiptDto = new GenerateReceiptDTO();

	}

	public void generateReport() throws JRException {
		if (startDate == null && endDate == null
				&& (receiptDto.getReceiptNo() == null || receiptDto.getReceiptNo().isEmpty())) {

			setErrorMessage("Please select Date Range or receipt No.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

			files = null;
			String sourceFileName = null;
			String loginUser = sessionBackingBean.getLoginUser();

			byte[] pdfByteArray = null;

			Connection conn = null;

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String startDateParam = null;
			String endDateParam = null;
			try {
				conn = ConnectionManager.getConnection();

				sourceFileName = "..//reports//RecieptSummaryReport.jrxml";
				String logopath = "//lk//informatics//ntc//view//reports//";

				Map<String, Object> parameters = new HashMap<String, Object>();
				if (startDate == null || endDate == null && receiptDto.getReceiptNo() != null) {

					parameters.put("P_Start_date", null);
					parameters.put("P_end_date", null);

					parameters.put("P_User", loginUser);
					parameters.put("P_Receipt", receiptDto.getReceiptNo());

					parameters.put("P_logopath", logopath);

					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

					pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					files = new DefaultStreamedContent(stream, "Application/pdf", "ReceiptDetailsSummary.pdf");

				}

				else if (startDate.compareTo(endDate) > 0) {

					setErrorMessage("End Date should be after Start Date.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

				else {

					if (receiptDto.getReceiptNo().isEmpty()) {
						parameters.put("P_Receipt", null);

					} else {
						parameters.put("P_Receipt", receiptDto.getReceiptNo());
						parameters.put("P_User", loginUser);
					}

					if (startDate == null) {

						parameters.put("P_Start_date", null);
					} else {
						startDateParam = sdf.format(startDate);
						parameters.put("P_Start_date", startDateParam);

					}

					if (endDate == null) {

						parameters.put("P_end_date", null);

					} else {
						endDateParam = sdf.format(endDate);
						parameters.put("P_end_date", endDateParam);

					}

					parameters.put("P_logopath", logopath);
					parameters.put("P_User", loginUser);
					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

					pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					files = new DefaultStreamedContent(stream, "Application/pdf", "ReceiptDetailsSummary.pdf");
				}

				if (pdfByteArray != null) {
					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "pdf");
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(conn);
			}

		}

	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public GenerateReceiptDTO getReceiptDto() {
		return receiptDto;
	}

	public void setReceiptDto(GenerateReceiptDTO receiptDto) {
		this.receiptDto = receiptDto;
	}

	public List<GenerateReceiptDTO> getReceiptNoList() {
		return receiptNoList;
	}

	public void setReceiptNoList(List<GenerateReceiptDTO> receiptNoList) {
		this.receiptNoList = receiptNoList;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public GenerateReceiptService getGenerateReceiptService() {
		return generateReceiptService;
	}

	public void setGenerateReceiptService(GenerateReceiptService generateReceiptService) {
		this.generateReceiptService = generateReceiptService;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

}
