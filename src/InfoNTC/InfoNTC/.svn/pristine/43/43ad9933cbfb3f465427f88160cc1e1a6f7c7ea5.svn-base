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

import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.service.PermitService;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.ntc.view.util.ConnectionManager;

@ManagedBean(name = "backlogTransaction")
@ViewScoped
public class backLogTransactionBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private PermitDTO permitdto = new PermitDTO();
	private List<PermitDTO> appNoList = new ArrayList<PermitDTO>();
	public List<String> getPermitNo = new ArrayList<String>(0);
	public List<String> vehicleNoList = new ArrayList<String>(0);
	private Boolean disableStartDateField = false;
	private Boolean disableEndDateField = false;
	private StreamedContent files;
	public List<PermitDTO> reportDTOList;
	private EmployeeDTO employeedto = new EmployeeDTO();

	private String applicationNo, busRegNo, permitNo, loginUser, errorMessage;
	private List<EmployeeDTO> userList = new ArrayList<EmployeeDTO>();
	// services
	private PermitService permitservice;
	private Date endDate;
	private Date startDate;

	@PostConstruct
	public void init() {

		LoadValuesforDropdown();
		employeedto.setUserId(sessionBackingBean.getLoginUser());
	}

	private void LoadValuesforDropdown() {
		permitservice = (PermitService) SpringApplicationContex.getBean("permitservice");
		appNoList = permitservice.showAppNo(sessionBackingBean.getLoginUser());

		getPermitNo = permitservice.showPermitNo(sessionBackingBean.getLoginUser());
		vehicleNoList = permitservice.getVehicleNoDropDowm(sessionBackingBean.getLoginUser());
		loginUser = employeedto.getUserId();
		userList = permitservice.showUser();
	}

	// update fields

	public void showAppNoList() {

		appNoList = permitservice.showAppNo(employeedto.getUserId());

	}

	public void showPermitNoList() {
		getPermitNo = permitservice.showPermitNo(employeedto.getUserId());
	}

	public void showVehicleNoList() {
		vehicleNoList = permitservice.getVehicleNoDropDowm(employeedto.getUserId());

	}

	public void fillAppliNo() {
		applicationNo = permitservice.fillAppliNo(permitdto.getPermitNo(), permitdto.getBusRegNo());
		permitdto.setApplicationNo(applicationNo);

	}

	public void fillVehicleNo() {
		busRegNo = permitservice.fillVehicleNo(permitdto.getPermitNo(), permitdto.getApplicationNo());
		permitdto.setBusRegNo(busRegNo);

	}

	public void fillPermit() {
		permitNo = permitservice.fillPermit(permitdto.getApplicationNo(), permitdto.getBusRegNo());
		permitdto.setPermitNo(permitNo);

	}

	public void generateReport() throws JRException {

		// new added

		files = null;
		String sourceFileName = null;

		loginUser = employeedto.getUserId();
		byte[] pdfByteArray = null;

		Connection conn = null;

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startDateParam = null;
		String endDateParam = null;
		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//backlogTransactionViewReport.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";

			Map<String, Object> parameters = new HashMap<String, Object>();
			if (startDate == null || endDate == null) {

				parameters.put("P_Start_Date", null);
				parameters.put("P_End_Date", null);

				if (employeedto.getUserId().isEmpty()) {
					parameters.put("P_User_Id", null);

				} else {
					parameters.put("P_User_Id", employeedto.getUserId());

				}

				if (permitdto.getApplicationNo().isEmpty()) {
					parameters.put("P_APP_no", null);

				} else {
					parameters.put("P_APP_no", permitdto.getApplicationNo());
				}
				if (permitdto.getPermitNo().isEmpty()) {
					parameters.put("P_Permit_No", null);

				} else {
					parameters.put("P_Permit_No", permitdto.getPermitNo());
				}
				if (permitdto.getBusRegNo().isEmpty()) {
					parameters.put("P_Vehi_No", null);

				} else {
					parameters.put("P_Vehi_No", permitdto.getBusRegNo());
				}
				parameters.put("P_LOGO_PATH", logopath);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				// Compile jrxml file.
				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

				pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "BacklogTransaction.pdf");

			}

			else if (startDate.compareTo(endDate) > 0) {

				setErrorMessage("End Date should be after Start Date.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

			else {

				if (employeedto.getUserId().isEmpty()) {
					parameters.put("P_User_Id", null);

				} else {
					parameters.put("P_User_Id", employeedto.getUserId());

				}
				if (permitdto.getApplicationNo().isEmpty()) {
					parameters.put("P_APP_no", null);

				} else {
					parameters.put("P_APP_no", permitdto.getApplicationNo());
				}
				if (permitdto.getPermitNo().isEmpty()) {
					parameters.put("P_Permit_No", null);

				} else {
					parameters.put("P_Permit_No", permitdto.getPermitNo());
				}
				if (permitdto.getBusRegNo().isEmpty()) {
					parameters.put("P_Vehi_No", null);

				} else {
					parameters.put("P_Vehi_No", permitdto.getBusRegNo());
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

				parameters.put("P_LOGO_PATH", logopath);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				// Compile jrxml file.
				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

				pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "BacklogTransaction.pdf");
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

	public void clearField() {
		permitdto = new PermitDTO();
		employeedto.setUserId(sessionBackingBean.getLoginUser());

		startDate = null;
		endDate = null;
		showAppNoList();
		showPermitNoList();
		showVehicleNoList();
	}

	public backLogTransactionBackingBean() {
		permitdto = new PermitDTO();
	}

	public PermitDTO getPermitdto() {
		return permitdto;
	}

	public void setPermitdto(PermitDTO permitdto) {
		this.permitdto = permitdto;
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

	public PermitService getPermitservice() {
		return permitservice;
	}

	public void setPermitservice(PermitService permitservice) {
		this.permitservice = permitservice;
	}

	public List<PermitDTO> getAppNoList() {
		return appNoList;
	}

	public void setAppNoList(List<PermitDTO> appNoList) {
		this.appNoList = appNoList;
	}

	public List<String> getGetPermitNo() {
		return getPermitNo;
	}

	public void setGetPermitNo(List<String> getPermitNo) {
		this.getPermitNo = getPermitNo;
	}

	public List<String> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<String> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
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

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getBusRegNo() {
		return busRegNo;
	}

	public void setBusRegNo(String busRegNo) {
		this.busRegNo = busRegNo;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
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

	public EmployeeDTO getEmployeedto() {
		return employeedto;
	}

	public void setEmployeedto(EmployeeDTO employeedto) {
		this.employeedto = employeedto;
	}

	public List<EmployeeDTO> getUserList() {
		return userList;
	}

	public void setUserList(List<EmployeeDTO> userList) {
		this.userList = userList;
	}

}
