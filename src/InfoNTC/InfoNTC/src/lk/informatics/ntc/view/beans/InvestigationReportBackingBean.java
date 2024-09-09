package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
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

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.GrievanceManagementService;
import lk.informatics.ntc.model.service.ManageInquiryService;
import lk.informatics.ntc.model.service.OffenceManagementService;
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

@ManagedBean(name = "investigationReportBackingBean")
@ViewScoped
public class InvestigationReportBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private Boolean disableStartDateField = false;
	private Boolean disableEndDateField = false;
	private StreamedContent files;
    private List<String> offenceList = new ArrayList<String>();

	// services
	private OffenceManagementService offenceManagementService;
	
	private Date endDate;
	private Date startDate;
    private String offenceName ;
	private String  errorMessage;
	private String user;
	@PostConstruct
	public void init() {
		offenceManagementService = (OffenceManagementService) SpringApplicationContex.getBean("offenceManagementService");
		
		offenceList = offenceManagementService.getActiveOffences();
		
		user = sessionBackingBean.getLoginUser();
		
	}

	public void generateReport() throws JRException {

		files = null;
		String sourceFileName = null;

		byte[] pdfByteArray = null;

		Connection conn = null;

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDateParam = null;
		String endDateParam = null;
		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//InvestigationReport.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";

			Map<String, Object> parameters = new HashMap<String, Object>();
			if (startDate == null && endDate == null && (offenceName ==null || offenceName.trim().isEmpty())) { 
				setErrorMessage("Please select offence with date range or date range for generate report.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
			
			else {
				if (startDate == null && endDate == null ) {
					
					setErrorMessage("Please select  date range for generate report.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			

			}
			

			else if ((startDate != null && endDate != null ) && startDate.compareTo(endDate) > 0) {
               
				setErrorMessage("End Date should be after Start Date.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

			else {
				if((startDate == null || endDate == null ) ) {
					setErrorMessage("Please select both start and end dates");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
				else {
				if(startDate.toString().substring(startDate.toString().length() - 4).equals(endDate.toString().substring(endDate.toString().length() - 4))) {
				// create temp table 
					offenceManagementService.createTempTable(offenceName,sdf.format(startDate), sdf.format(endDate));
					offenceManagementService.beanLinkMethod(offenceName, user,"Report Generated");
					
				if (startDate == null) {
					parameters.put("P_Start_Date", null);
				} else {
					startDateParam = sdf.format(startDate);
					parameters.put("P_Start_Date", startDateParam);
				}

				if (endDate == null) {

					parameters.put("P_End_Date", null);

				} else {
					endDateParam = sdf.format(endDate);
					parameters.put("P_End_Date", endDateParam);
				}
				
				if (offenceName.isEmpty() || offenceName == null) {
					parameters.put("P_Offence", null);
				} else {
					parameters.put("P_Offence", offenceName);
				}
				
				parameters.put("P_year", startDate.toString().substring(startDate.toString().length() - 4));

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				// Compile jrxml file.
				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
				pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "GrievanceReport.pdf");
			}
				
				else  {
					setErrorMessage("Please select same years for start and end date.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					
				}
				
				
			}	
			}
			
				
		}
				
	

			if (pdfByteArray != null) {
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "pdf");
			}
			offenceManagementService.deleteInvestigationData();
			offenceManagementService.beanLinkMethod(offenceName, user,"Delete Investigation Report");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

	}

	public void clearField() {
	
        offenceName = null;
		startDate = null;
		endDate = null;
		offenceList = offenceManagementService.getActiveOffences();


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



	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}



	public String getOffenceName() {
		return offenceName;
	}

	public void setOffenceName(String offenceName) {
		this.offenceName = offenceName;
	}

	public List<String> getOffenceList() {
		return offenceList;
	}

	public void setOffenceList(List<String> offenceList) {
		this.offenceList = offenceList;
	}

	public OffenceManagementService getOffenceManagementService() {
		return offenceManagementService;
	}

	public void setOffenceManagementService(OffenceManagementService offenceManagementService) {
		this.offenceManagementService = offenceManagementService;
	}
	
	

}
