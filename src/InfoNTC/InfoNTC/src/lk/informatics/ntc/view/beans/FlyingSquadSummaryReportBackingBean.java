package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import lk.informatics.ntc.model.service.ManageCourtCaseService;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.ntc.view.util.ConnectionManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@ManagedBean(name = "flyingSquadSummaryReportBackingBean")
@ViewScoped
public class FlyingSquadSummaryReportBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private Boolean disableStartDateField = false;
	private Boolean disableEndDateField = false;
	private Boolean disableStartDateFieldP = false;
	private Boolean disableEndDateFieldP = false;
	private StreamedContent files;

	private String loginUser, errorMessage;

	private Date endDate;
	private Date startDate;
	private Date endDateP;
	private Date startDateP;
	private ManageCourtCaseService manageCourtCaseService;

	@PostConstruct
	public void init() {
		manageCourtCaseService = (ManageCourtCaseService) SpringApplicationContex.getBean("manageCourtCaseService");
		loginUser = sessionBackingBean.getLoginUser();

	}

	public StreamedContent generateProgressReport() throws JRException {

		// new added

		files = null;
		String sourceFileName = null;

		java.util.Date date = new java.util.Date();
		int curntYr = date.getYear() + 1900;

		String month = null;
		String substr = null;
		String cumalativeStartDate = curntYr + "-" + "01" + "-" + "01";
		Connection conn = null;

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDateParam = null;
		String endDateParam = null;
		try {
			conn = ConnectionManager.getConnection();
			manageCourtCaseService.deleteFlyingProgressTempTab();
			sourceFileName = "..//reports//FlyingSquadProgressReportInExcel.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";

			Map<String, Object> parameters = new HashMap<String, Object>();
			if (startDateP != null || endDateP != null) {

				if (startDateP.compareTo(endDateP) > 0) {

					setErrorMessage("End Date should be after Start Date.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

				else {
					startDateParam = sdf.format(startDateP);
					endDateParam = sdf.format(endDateP);
					// insert count of attend officers and working hours per group according to
					// investigation_date

					manageCourtCaseService.InsertTempData(startDateParam, endDateParam);
					parameters.put("P_Start_Date", startDateParam);
					parameters.put("P_End_Date", endDateParam);
					parameters.put("P_image", logopath);

					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

					// Compile jrxml file.
					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					JRXlsxExporter exporter = new JRXlsxExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
					SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
					configuration.setForcePageBreaks(false);
					configuration.setRemoveEmptySpaceBetweenRows(true);
					configuration.setRemoveEmptySpaceBetweenColumns(true);
					exporter.setConfiguration(configuration);
					exporter.exportReport();

					byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					files = new DefaultStreamedContent(stream, "Application/xlsx", "FlyingSquadProgress excel.xlsx");
					
					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "xlsx");

					manageCourtCaseService.deleteFlyingProgressTempTab();

				}
			} else {

				setErrorMessage("Please select date ranges for print.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}
		return files;

	}

	public StreamedContent generateReport() throws JRException {
		files = null;
		String sourceFileName = null;

		java.util.Date date = new java.util.Date();
		int curntYr = date.getYear() + 1900;
		byte[] pdfByteArray = null;
		String month = null;
		String substr = null; 
		Connection conn = null;

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDateParam = null;
		String endDateParam = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//FlyingSquadSummaryReport.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";
			manageCourtCaseService.deleteTempTab();
			Map<String, Object> parameters = new HashMap<String, Object>();
			if (startDate != null || endDate != null) {

				if (startDate.compareTo(endDate) > 0) {

					setErrorMessage("End Date should be after Start Date.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

				else {
					startDateParam = sdf.format(startDate);
					endDateParam = sdf.format(endDate);
					substr = startDateParam.substring(0, 4);
					String cumalativeStartDate = substr + "-" + "01" + "-" + "01";
					
					manageCourtCaseService.insertTotalHoursForGroup(startDateParam, endDateParam, cumalativeStartDate);

					parameters.put("P_Start_Date", startDateParam);

					int m = endDate.getMonth();
					if (m == 0) {
						month = "January.";
					}
					if (m == 1) {
						month = "February.";
					}
					if (m == 2) {
						month = "March.";
					}
					if (m == 3) {
						month = "April.";
					}
					if (m == 4) {
						month = "May.";
					}
					if (m == 5) {
						month = "June.";
					}
					if (m == 6) {
						month = "July.";
					}
					if (m == 7) {
						month = "August.";
					}
					if (m == 8) {
						month = "September.";
					}
					if (m == 9) {
						month = "October.";
					}

					if (m == 10) {
						month = "November.";
					}
					if (m == 11) {
						month = "December.";
					}
					parameters.put("P_month", month);

					parameters.put("P_cumalative_S_date", cumalativeStartDate);
					parameters.put("P_End_Date", endDateParam);
					parameters.put("P_image", logopath);

					parameters.put("P_cumalative_E_date", endDateParam);
					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

					// Compile jrxml file.
					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

					pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					files = new DefaultStreamedContent(stream, "Application/pdf", "FlyingSquadSummaray.pdf");
					manageCourtCaseService.deleteTempTab();

					if (pdfByteArray != null) {
						ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
						Map<String, Object> sessionMap = externalContext.getSessionMap();
						sessionMap.put("reportBytes", pdfByteArray);
						sessionMap.put("docType", "pdf");
					}
				}
			} else {

				setErrorMessage("Please select date ranges for print.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}
		return files;

	}

	public void clearField() {
		startDate = null;
		endDate = null;

	}

	public void clearFieldP() {
		startDateP = null;
		endDateP = null;

	}

	public Boolean getDisableStartDateField() {
		return disableStartDateField;
	}

	public void setDisableStartDateField(Boolean disableStartDateField) {
		this.disableStartDateField = disableStartDateField;
	}

	public Boolean getDisableEndDateField() {
		return disableEndDateField;
	}

	public void setDisableEndDateField(Boolean disableEndDateField) {
		this.disableEndDateField = disableEndDateField;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
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

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Date getEndDateP() {
		return endDateP;
	}

	public void setEndDateP(Date endDateP) {
		this.endDateP = endDateP;
	}

	public Date getStartDateP() {
		return startDateP;
	}

	public void setStartDateP(Date startDateP) {
		this.startDateP = startDateP;
	}

	public ManageCourtCaseService getManageCourtCaseService() {
		return manageCourtCaseService;
	}

	public void setManageCourtCaseService(ManageCourtCaseService manageCourtCaseService) {
		this.manageCourtCaseService = manageCourtCaseService;
	}

	public Boolean getDisableStartDateFieldP() {
		return disableStartDateFieldP;
	}

	public void setDisableStartDateFieldP(Boolean disableStartDateFieldP) {
		this.disableStartDateFieldP = disableStartDateFieldP;
	}

	public Boolean getDisableEndDateFieldP() {
		return disableEndDateFieldP;
	}

	public void setDisableEndDateFieldP(Boolean disableEndDateFieldP) {
		this.disableEndDateFieldP = disableEndDateFieldP;
	}

}
