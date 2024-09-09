package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
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

import lk.informatics.ntc.model.dto.RevenueCollectionDTO;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * @author viraj.k
 **/

@ManagedBean(name = "revenueCollectionReportBackingBean")
@ViewScoped

public class RevenueCollectionReportBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private StreamedContent files;
	private List<RevenueCollectionDTO> transactionTypesList;
	private String errorStatus, sucessStatus;
	private Boolean printBtncheck = false;
	private Boolean generateBtncheck = true;

	// dto
	private RevenueCollectionDTO revenueCollectionDTO;

	// services
	private DocumentManagementService documentManagementService;

	@PostConstruct
	public void init() {
		loadData();
	}

	public void loadData() {

		revenueCollectionDTO = new RevenueCollectionDTO();

		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		transactionTypesList = documentManagementService.GetAllDocumentTrasactionTypes();

	}

	public void clearBtnAction() {
		revenueCollectionDTO = new RevenueCollectionDTO();
		generateBtncheck = true;
		printBtncheck = false;
	}

	public void printBtnAction() {

		generateReport();

		printBtncheck = false;
		generateBtncheck = true;
	}

	public StreamedContent generateReport() {

		files = null;
		String sourceFileName = null;
		Connection conn = null;

		revenueCollectionDTO.setCurrentUser(sessionBackingBean.getLoginUser());

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = formatter.format(revenueCollectionDTO.getStartDate());

		String strDate1 = formatter.format(revenueCollectionDTO.getEndDate());

		try {

			conn = ConnectionManager.getConnection();
			String logopath = "//lk//informatics//ntc//view//reports//";
			sourceFileName = "..//reports//RevenueCollectionReport.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Rev_Current_User", sessionBackingBean.getLoginUser());
			parameters.put("P_Rev_Start_Date", strDate);
			parameters.put("P_Rev_End_Date", strDate1);
			if (revenueCollectionDTO.getTypeCode() != null && !revenueCollectionDTO.getTypeCode().isEmpty())
				parameters.put("P_Rev_Transaction_Type_Code", revenueCollectionDTO.getTypeCode());
			else if (revenueCollectionDTO.getTypeCode() == null || revenueCollectionDTO.getTypeCode().isEmpty()) {
				parameters.put("P_Rev_Transaction_Type_Code", null);
			}

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "RevenueCollectionReport.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return null;
	}

	public void generateBtnAction() {

		if (revenueCollectionDTO.getStartDate() == null) {
			setErrorStatus("Please Select start date.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");

		} else if (revenueCollectionDTO.getEndDate() == null) {
			setErrorStatus("Please Select end date.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");

		} else if (revenueCollectionDTO.getStartDate().compareTo(revenueCollectionDTO.getEndDate()) > 0) {
			setErrorStatus("Please Select Valid dates for start date and end date.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");

		} else {
			generateBtncheck = false;
			printBtncheck = true;
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<RevenueCollectionDTO> getTransactionTypesList() {
		return transactionTypesList;
	}

	public void setTransactionTypesList(List<RevenueCollectionDTO> transactionTypesList) {
		this.transactionTypesList = transactionTypesList;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	public String getSucessStatus() {
		return sucessStatus;
	}

	public void setSucessStatus(String sucessStatus) {
		this.sucessStatus = sucessStatus;
	}

	public Boolean getPrintBtncheck() {
		return printBtncheck;
	}

	public void setPrintBtncheck(Boolean printBtncheck) {
		this.printBtncheck = printBtncheck;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public RevenueCollectionDTO getRevenueCollectionDTO() {
		return revenueCollectionDTO;
	}

	public void setRevenueCollectionDTO(RevenueCollectionDTO revenueCollectionDTO) {
		this.revenueCollectionDTO = revenueCollectionDTO;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public Boolean getGenerateBtncheck() {
		return generateBtncheck;
	}

	public void setGenerateBtncheck(Boolean generateBtncheck) {
		this.generateBtncheck = generateBtncheck;
	}

}
