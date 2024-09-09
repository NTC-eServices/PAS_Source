package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.QueueNumberDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "generateMainQueueBean")
@ViewScoped
public class GenerateMainQueuNumberBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private QueueManagementService queueManagementService;
	private String alertMessage, errorMessage, successMessage;
	private StreamedContent files;
	private QueueNumberDTO queueNumberDTO = new QueueNumberDTO();

	public GenerateMainQueuNumberBackingBean() {
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
	}

	 public void generateQueueNumber(String type) throws JRException {

		if (type != null) {

			queueNumberDTO = queueManagementService.generateMainTokenNumber(type);

			if (queueNumberDTO.getQueueNumber() != null) {

				if (queueNumberDTO.getQueueNumber() == "LIMIT_EXCEEDED") {

					setAlertMessage("Token numbers limit per day exceeded.");
					RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");

				} else {
					this.printToken(queueNumberDTO.getTransTypeCode(), queueNumberDTO.getQueueNumber());
				}

			} else {

				setErrorMessage("Cannot generate a token number.");
				RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
			}

		}

	}

	public StreamedContent printToken(String transactionType, String tokenNumber) throws JRException {

		files = null;
		List<QueueNumberDTO> dtoList = new ArrayList<QueueNumberDTO>();
		QueueNumberDTO queueNumberDTO = new QueueNumberDTO();
		dtoList.add(queueNumberDTO);
		String sourceFileName = "..//reports//main_token.jrxml";

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

		String pdfName = transactionType + " - " + tokenNumber;

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("DATE", df.format(date));
		parameters.put("TOKEN_NO", tokenNumber);
		parameters.put("TRANS_TYPE", transactionType);

		JasperDesign jasperDesign = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));
		JasperReport report = JasperCompileManager.compileReport(jasperDesign);
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dtoList, false);

		JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, beanCollectionDataSource);

		byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
		InputStream stream = new ByteArrayInputStream(pdfByteArray);
		files = new DefaultStreamedContent(stream, "Application/pdf", pdfName + ".pdf");

		return files;
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

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
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

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

}
