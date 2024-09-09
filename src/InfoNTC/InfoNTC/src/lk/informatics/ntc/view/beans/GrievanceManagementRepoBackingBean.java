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

@ManagedBean(name = "grievanceReportBackingBean")
@ViewScoped
public class GrievanceManagementRepoBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private Boolean disableStartDateField = false;
	private Boolean disableEndDateField = false;
	private StreamedContent files;

	private ComplaintRequestDTO repoDTO = new ComplaintRequestDTO();
	private List<ComplaintRequestDTO> userList = new ArrayList<ComplaintRequestDTO>();
	private List<CommonDTO> provincelList;
	private List<String> complaintNumList;
	private String loginUser, errorMessage, userID;

	// services
	private GrievanceManagementService grievanceRepoService;
	private ManageInquiryService manageInquiryService;
	private AdminService adminService;
	
	private Date endDate;
	private Date startDate;

	
	@PostConstruct
	public void init() {
		grievanceRepoService = (GrievanceManagementService) SpringApplicationContex
				.getBean("grievanceManagementService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		manageInquiryService = (ManageInquiryService) SpringApplicationContex.getBean("manageInquiryService");
		userList = grievanceRepoService.showUser();
		provincelList = adminService.getProvinceToDropdown();
		complaintNumList = manageInquiryService.retrieveComplaintNumbers();

	}

	public void generateReport() throws JRException {
		String user = sessionBackingBean.getLoginUser();
		files = null;
		String sourceFileName = null;

		loginUser = repoDTO.getUserId();
		byte[] pdfByteArray = null;

		Connection conn = null;

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDateParam = null;
		String endDateParam = null;
		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//GrievanceManagmentReport.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";

			Map<String, Object> parameters = new HashMap<String, Object>();
			if (startDate == null && endDate == null) {

				parameters.put("P_Start_Date", null);
				parameters.put("P_End_Date", null);

				if (repoDTO.getUserId().isEmpty()) {
					parameters.put("P_User", null);

				} else {
					parameters.put("P_User", repoDTO.getUserId());

				}
				

				if (repoDTO.getProvince().isEmpty()) {
					parameters.put("P_Provice", null);

				} else {
					parameters.put("P_Provice", repoDTO.getProvince());

				}
				if (repoDTO.getComplaintNo().isEmpty()) {
					parameters.put("P_Complaint_No", null);

				} else {
					parameters.put("P_Complaint_No", repoDTO.getComplaintNo());

				}

				if (repoDTO.getPermitAuthority().isEmpty() || repoDTO.getPermitAuthority().equals("All")) {
					parameters.put("P_Permit_Authority", null);

				} else {
					parameters.put("P_Permit_Authority", repoDTO.getPermitAuthority());
				}
				if (repoDTO.getComplainTypeCode().isEmpty() || repoDTO.getComplainTypeCode().equals("All")) {
					parameters.put("P_Complain_Type", null);

				} else {
					parameters.put("P_Complain_Type", repoDTO.getComplainTypeCode());
				}

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				// Compile jrxml file.
				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

				pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "GrievanceManagmentReport.pdf");
				
			}

			else if ((startDate != null && endDate != null ) && startDate.compareTo(endDate) > 0) {

				setErrorMessage("End Date should be after Start Date.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

			else {
			

				if (repoDTO.getUserId().isEmpty()) {
					parameters.put("P_User", null);
				} else {
					parameters.put("P_User", repoDTO.getUserId());
				}
				if (repoDTO.getPermitAuthority().isEmpty() || repoDTO.getPermitAuthority().equals("All")) {
					parameters.put("P_Permit_Authority", null);
				} else {
					parameters.put("P_Permit_Authority", repoDTO.getPermitAuthority());
				}
				if (repoDTO.getComplainTypeCode().isEmpty() || repoDTO.getComplainTypeCode().equals("All")) {
					parameters.put("P_Complain_Type", null);
				} else {
					parameters.put("P_Complain_Type", repoDTO.getComplainTypeCode());
				}

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
				
				if (repoDTO.getProvince().isEmpty()) {
					parameters.put("P_Provice", null);

				} else {
					parameters.put("P_Provice", repoDTO.getProvince());

				}
				if (repoDTO.getComplaintNo().isEmpty()) {
					parameters.put("P_Complaint_No", null);

				} else {
					parameters.put("P_Complaint_No", repoDTO.getComplaintNo());

				}

				parameters.put("P_LOGO_PATH", logopath);

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

			if (pdfByteArray != null) {
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "pdf");		
			}
			
			grievanceRepoService.beanLinkMethod(repoDTO, user, "Grievance Report Generate", "Grievance Management Report");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

	}

	public void clearField() {
		repoDTO = new ComplaintRequestDTO();

		startDate = null;
		endDate = null;
		provincelList = adminService.getProvinceToDropdown();
		complaintNumList = manageInquiryService.retrieveComplaintNumbers();


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

	public ComplaintRequestDTO getRepoDTO() {
		return repoDTO;
	}

	public void setRepoDTO(ComplaintRequestDTO repoDTO) {
		this.repoDTO = repoDTO;
	}

	public GrievanceManagementService getGrievanceRepoService() {
		return grievanceRepoService;
	}

	public void setGrievanceRepoService(GrievanceManagementService grievanceRepoService) {
		this.grievanceRepoService = grievanceRepoService;
	}

	public List<ComplaintRequestDTO> getUserList() {
		return userList;
	}

	public void setUserList(List<ComplaintRequestDTO> userList) {
		this.userList = userList;
	}

	public List<CommonDTO> getProvincelList() {
		return provincelList;
	}

	public void setProvincelList(List<CommonDTO> provincelList) {
		this.provincelList = provincelList;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<String> getComplaintNumList() {
		return complaintNumList;
	}

	public void setComplaintNumList(List<String> complaintNumList) {
		this.complaintNumList = complaintNumList;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public ManageInquiryService getManageInquiryService() {
		return manageInquiryService;
	}

	public void setManageInquiryService(ManageInquiryService manageInquiryService) {
		this.manageInquiryService = manageInquiryService;
	}

}
