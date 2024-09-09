package lk.informatics.ntc.view.beans;

import javax.faces.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

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

@ManagedBean(name = "fareTableReportBackingBean")
@ViewScoped
public class FareTableReportBackingBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Date ammendment;
	private String language = "";
	private String vehicelNo;
	private StreamedContent files;

	public StreamedContent reportGenerateEng(ActionEvent ae) throws JRException {
		language = "ENG";
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strAmendDate = dateFormat.format(ammendment);
			conn = ConnectionManager.getConnection();
			String logopath = "//lk//informatics//ntc//view//reports//";
			sourceFileName = "..//reports//FareTableEnglish.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Amenmend_date", strAmendDate);
			parameters.put("P_ntc_logo", logopath);
			parameters.put("P_national_logo", logopath);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "busfare.pdf");

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

	public StreamedContent reportGenerateSin(ActionEvent ae) throws JRException {
		language = "SIN";
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strAmendDate = dateFormat.format(ammendment);
			
			conn = ConnectionManager.getConnection();
			String logopath = "//lk//informatics//ntc//view//reports//";
			sourceFileName = "..//reports//FareTableSinhala.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Amenmend_date", strAmendDate);
			parameters.put("P_national_logo", logopath);
			parameters.put("P_ntc_logo", logopath);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "busfare.pdf");
		
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

	public void english() {
		language = "ENG";
	}

	public void sinhala() {
		language = "SIN";
	}

	public void tamil() {
		language = "TAM";
	}

	public void clear() {
		language = "";
		ammendment = null;
	}

	public Date getAmmendment() {
		return ammendment;
	}

	public void setAmmendment(Date ammendment) {
		this.ammendment = ammendment;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public String getVehicelNo() {
		return vehicelNo;
	}

	public void setVehicelNo(String vehicelNo) {
		this.vehicelNo = vehicelNo;
	}

}
