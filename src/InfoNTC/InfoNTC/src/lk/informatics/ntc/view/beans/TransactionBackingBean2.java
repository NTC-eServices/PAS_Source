package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.TransactionDTO;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "transactionBAckingBean")
@ViewScoped
public class TransactionBackingBean2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private TransactionDTO transactionDTO = new TransactionDTO();

	private List<PaymentVoucherDTO> transactionTypeListRpt = new ArrayList<PaymentVoucherDTO>(0);

	// services
	public PaymentVoucherService paymentVoucherService;
	private Date endDate;
	private Date startDate;
	private String errorMessage;
	private Date myDate;
	private String currentDate;

	private StreamedContent files;

	@PostConstruct
	public void init() {
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		transactionTypeListRpt = paymentVoucherService.getTranactionTypeForRpt();
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		currentDate = localDate.format(formatter);
	}

	public StreamedContent generateReport(ActionEvent ae) throws JRException {
		if (startDate == null) {

			setErrorMessage("Please select a trasnaction date.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else {

			files = null;
			String sourceFileName = null;

			Connection conn = null;

			try {
				conn = ConnectionManager.getConnection();

				sourceFileName = "..//reports//TransactionReport.jrxml";

				String logopath = "//lk//informatics//ntc//view//reports//";
				Map<String, Object> parameters = new HashMap<String, Object>();

				if (transactionDTO.getTransactionCode().trim().isEmpty()
						|| transactionDTO.getTransactionCode().equals(null)) {
					parameters.put("P_Transaction_Code", null);
				} else {
					parameters.put("P_Transaction_Code", transactionDTO.getTransactionCode());
				}
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy");
				String strStartDate = formatter.format(startDate);
				System.out.println("parameter date = " + strStartDate);
				parameters.put("P_Date", strStartDate);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				
				if (jasperPrint.getPages().isEmpty()) {
		            setErrorMessage("No data was found on the selected criteria");
		        	RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		            return null; 
		        }else {
		        	jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
					jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

					byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					files = new DefaultStreamedContent(stream, "Application/pdf", "Transaction Report.pdf");
					RequestContext.getCurrentInstance().execute("window.open('/InfoNTC/showUpload?reportName=Transaction Report');");

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
		return files;

	}

	public void clearField() {
		transactionDTO = new TransactionDTO();
		startDate = null;
		endDate = null;
	}
//report
	public StreamedContent generateAppTaskReport(ActionEvent ae) throws JRException {

		files = null;
		String sourceFileName = null;
		String strStartDate;
		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//ApplicationTaskStatusRepo.jrxml";

			String logopath = "//lk//informatics//ntc//view//reports//";
			Map<String, Object> parameters = new HashMap<String, Object>();

			if (myDate == null) {
				strStartDate = currentDate;

			} else {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy");
				strStartDate = formatter.format(myDate);

			}
			parameters.put("P_Date", strStartDate);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			if (jasperPrint.getPages().isEmpty()) {
	            setErrorMessage("No data was found on the selected criteria");
	        	RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	            return null; 
	        }else {
	        	jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "Transaction Report.pdf");
				RequestContext.getCurrentInstance().execute("window.open('/InfoNTC/showUpload?reportName=Application Task Status Report');");

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

		return files;

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public TransactionDTO getTransactionDTO() {
		return transactionDTO;
	}

	public void setTransactionDTO(TransactionDTO transactionDTO) {
		this.transactionDTO = transactionDTO;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public List<PaymentVoucherDTO> getTransactionTypeListRpt() {
		return transactionTypeListRpt;
	}

	public void setTransactionTypeListRpt(List<PaymentVoucherDTO> transactionTypeListRpt) {
		this.transactionTypeListRpt = transactionTypeListRpt;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public Date getMyDate() {
		return myDate;
	}

	public void setMyDate(Date myDate) {
		this.myDate = myDate;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

}
