package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.ParseException;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.SubSidyDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.model.service.SubSidyManagementService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
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

@ManagedBean(name = "subsidyPaymentApproveBackingBean")
@ViewScoped
public class SubsidyMangmentPaymentApprovalBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services
	private DocumentManagementService documentManagementService;
	private AdminService adminService;
	private CommonService commonService;

	private SubSidyManagementService subSidyManagementService;

	private SisuSariyaService sisuSariyaService;
	// DTO

	private SubSidyDTO sisuSariyaSubsidyPaymentDTO;

	private SubSidyDTO fillServiceNO;
	private List<SubSidyDTO> fillServiceRefNoList = new ArrayList<SubSidyDTO>();
	private List<SubSidyDTO> voucherNOList = new ArrayList<SubSidyDTO>();
	private List<SubSidyDTO> serviceRefNOList = new ArrayList<SubSidyDTO>();
	private List<SubSidyDTO> showSisuSearchData;
	private List<SubSidyDTO> showSubsidyLogSheetData;
	private List<SubSidyDTO> showDataOnGrid;
	private SubSidyDTO selectedRow;
	private SubSidyDTO selectedRow1;
	private SubSidyDTO selectedRaw1;
	private SubSidyDTO getApprovals;
	private List<?> filSubsidyService;
	private List<CommonDTO> drpdDistrictList;
	private List<CommonDTO> drpdDevsecList;

	private int activeTabIndex;
	private String selectedReqNo, selectedRequestorType, selectedPrefLang, selectedReqType, selectedIdNo,
			successMessage, errorMessage, loginUser, errorMsg;
	private boolean editFlag;
	String endDateValue;
	String startDateValue;
	private boolean disCertify;
	private StreamedContent fileReport;
	private StreamedContent fileExcel;
	private StreamedContent filePdf;
	private boolean recommend, directorApproval, chairmanApproval = false;
	private boolean createMode, recomendMode, directorMode, chairmanMode, viewMode;
	private String selectedSubsidyService;

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private boolean disabledVoucherNoList = true;
	StreamedContent outputFile = null;

	@PostConstruct
	public void init() {
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		subSidyManagementService = (SubSidyManagementService) SpringApplicationContex
				.getBean("subSidyManagementService");

		disCertify = false;

		filSubsidyService = subSidyManagementService.getServiceType();

		sisuSariyaSubsidyPaymentDTO = new SubSidyDTO();

		loginUser = sessionBackingBean.getLoginUser();

		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN113", "C");
		recomendMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN113", "R");
		directorMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN113", "DA");
		chairmanMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN113", "CA");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN113", "V");

	}

	public void onChangeServiceType() {

		if (!selectedSubsidyService.equals("") && selectedSubsidyService != null) {
			if (selectedSubsidyService.equals("S01")) {
				voucherNOList = new ArrayList<SubSidyDTO>();
				voucherNOList = subSidyManagementService.getVoucherNoList(selectedSubsidyService);
				setDisabledVoucherNoList(false);
			} else if (selectedSubsidyService.equals("S02")) {
				voucherNOList = new ArrayList<SubSidyDTO>();
				voucherNOList = subSidyManagementService.getVoucherNoList(selectedSubsidyService);
				setDisabledVoucherNoList(false);
			} else if (selectedSubsidyService.equals("S03")) {
				voucherNOList = new ArrayList<SubSidyDTO>();
				voucherNOList = subSidyManagementService.getVoucherNoList(selectedSubsidyService);
				setDisabledVoucherNoList(false);
			} else {

			}
		} else {

		}
	}

	public void onVoucherStartDateChange(SelectEvent event) throws ParseException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		startDateValue = frm.format(event.getObject());

		Date startDateValueObj = frm.parse(startDateValue);

		sisuSariyaSubsidyPaymentDTO.setVouStartDateVal(startDateValue);
		if (sisuSariyaSubsidyPaymentDTO.getVouStartDateVal() != null
				&& !sisuSariyaSubsidyPaymentDTO.getVouStartDateVal().isEmpty()
				&& !sisuSariyaSubsidyPaymentDTO.getVouStartDateVal().equalsIgnoreCase("")) {

			voucherNOList = subSidyManagementService.getVoucherNoListNew(sisuSariyaSubsidyPaymentDTO,
					selectedSubsidyService);
		}

	}

	public void onVoucherEndDateChange(SelectEvent event) throws ParseException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		endDateValue = frm.format(event.getObject());

		Date startDateValueObj = frm.parse(endDateValue);
		sisuSariyaSubsidyPaymentDTO.setVouEndDateVal(endDateValue);
		if (sisuSariyaSubsidyPaymentDTO.getVouEndDateVal() != null
				&& !sisuSariyaSubsidyPaymentDTO.getVouEndDateVal().isEmpty()
				&& !sisuSariyaSubsidyPaymentDTO.getVouEndDateVal().equalsIgnoreCase("")) {

			voucherNOList = subSidyManagementService.getVoucherNoListNew(sisuSariyaSubsidyPaymentDTO,
					selectedSubsidyService);
		}

	}

	public void searchDetails() {

		if (selectedSubsidyService != null && !selectedSubsidyService.trim().isEmpty()) {
			if ((sisuSariyaSubsidyPaymentDTO.getVouStartDate() != null
					&& sisuSariyaSubsidyPaymentDTO.getVouEndDate() != null)
					|| sisuSariyaSubsidyPaymentDTO.getVoucherNo() != null
							&& !sisuSariyaSubsidyPaymentDTO.getVoucherNo().trim().isEmpty()) {

				showSisuSearchData = subSidyManagementService.SisuPaymentData(
						sisuSariyaSubsidyPaymentDTO.getVouStartDate(), sisuSariyaSubsidyPaymentDTO.getVouEndDate(),
						sisuSariyaSubsidyPaymentDTO.getVoucherNo(), selectedSubsidyService);

				if (showSisuSearchData == null || showSisuSearchData.isEmpty()) {
					setErrorMessage("No Data For searched values.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				}

			}

			else {
				setErrorMessage("Please select data for search");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {

			setErrorMessage("Please select Service Type");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearSearchDet() {

		sisuSariyaSubsidyPaymentDTO = new SubSidyDTO();
		selectedSubsidyService = null;
		showSisuSearchData = new ArrayList<SubSidyDTO>();
		voucherNOList = new ArrayList<SubSidyDTO>();
		showSubsidyLogSheetData = new ArrayList<SubSidyDTO>();
		setDisabledVoucherNoList(true);
	}

	public void certifyButton() {

		if (selectedRow != null) {
			getApprovals = subSidyManagementService.getApprovalStatus(selectedRow.getVoucherNo());
			if (getApprovals.getCertifyTic().equalsIgnoreCase("A")) {

				setErrorMessage("Already Certified");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {
				String success = subSidyManagementService.updateCertifyApproval(selectedRow, loginUser);

				if(success != null) {
					setSuccessMessage("Certified!");
					RequestContext.getCurrentInstance().update("successMSG");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				}else {
					setErrorMessage("Unsuccessful");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
				

			}

			showSisuSearchData = subSidyManagementService.SisuPaymentData(sisuSariyaSubsidyPaymentDTO.getVouStartDate(),
					sisuSariyaSubsidyPaymentDTO.getVouEndDate(), sisuSariyaSubsidyPaymentDTO.getVoucherNo(),
					selectedSubsidyService);
		} else {

			setErrorMessage("Please Select a Row");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
	}

	public void recommendedButton() {

		if (selectedRow != null) {
			getApprovals = subSidyManagementService.getApprovalStatus(selectedRow.getVoucherNo());
			if (getApprovals.getCertifyTic().equalsIgnoreCase("A")) {
				if (getApprovals.getRecomndedTic().equalsIgnoreCase("A")) {

					setErrorMessage("Already Recomended");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					String success = subSidyManagementService.updateRecomendedApproval(selectedRow, loginUser);
					
					if(success != null) {
						setSuccessMessage("Recomended!");
						RequestContext.getCurrentInstance().update("successMSG");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					}else {
						setErrorMessage("Unsuccessful");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
					

				}

				showSisuSearchData = subSidyManagementService.SisuPaymentData(
						sisuSariyaSubsidyPaymentDTO.getVouStartDate(), sisuSariyaSubsidyPaymentDTO.getVouEndDate(),
						sisuSariyaSubsidyPaymentDTO.getVoucherNo(), selectedSubsidyService);
			} else {
				setErrorMessage("This application No. not certified yet");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}
		} else {
			setErrorMessage("Please Select a Row");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
	}

	public void directorApproval() {

		if (selectedRow != null) {
			getApprovals = subSidyManagementService.getApprovalStatus(selectedRow.getVoucherNo());
			if (getApprovals.getRecomndedTic().equalsIgnoreCase("A")) {
				if (getApprovals.getDirectorApprovalTic().equalsIgnoreCase("A")) {

					setErrorMessage("Already Approved by Director");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					String success = subSidyManagementService.updateDirectorApproval(selectedRow, loginUser);
					
					if(success != null) {
						setSuccessMessage("Approved by Director");
						RequestContext.getCurrentInstance().update("successMSG");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					}else {
						setErrorMessage("Unsuccessful");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				}
				showSisuSearchData = subSidyManagementService.SisuPaymentData(
						sisuSariyaSubsidyPaymentDTO.getVouStartDate(), sisuSariyaSubsidyPaymentDTO.getVouEndDate(),
						sisuSariyaSubsidyPaymentDTO.getVoucherNo(), selectedSubsidyService);
			} else {

				setErrorMessage("This application No. not recommend yet");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please Select a Row");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
	}

	public void dgApproval() {

		if (selectedRow != null) {
			getApprovals = subSidyManagementService.getApprovalStatus(selectedRow.getVoucherNo());
			if (getApprovals.getDirectorApprovalTic().equalsIgnoreCase("A")) {
				if (getApprovals.getDgApprovalTic().equalsIgnoreCase("A")) {

					setErrorMessage("Already Approved by Chairman/DG");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					String success = subSidyManagementService.updateDGApproval(selectedRow, loginUser);
					
					if(success != null) {
						setSuccessMessage("Approved by Chairman");
						RequestContext.getCurrentInstance().update("successMSG");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					}else {
						setErrorMessage("Unsuccessful");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}	
				}
				showSisuSearchData = subSidyManagementService.SisuPaymentData(
						sisuSariyaSubsidyPaymentDTO.getVouStartDate(), sisuSariyaSubsidyPaymentDTO.getVouEndDate(),
						sisuSariyaSubsidyPaymentDTO.getVoucherNo(), selectedSubsidyService);
			} else {

				setErrorMessage("This application No. not approved by director yet");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {

			setErrorMessage("Please Select a Row");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
	}

	public void viweButton() {

		showSubsidyLogSheetData = new ArrayList<SubSidyDTO>();
		showSubsidyLogSheetData = subSidyManagementService.showLogSheetDet(selectedRow.getVoucherNo(),
				selectedRow.getBulkSeq());
	}

	public StreamedContent printReport() throws JRException {

		if (selectedRow != null) {
			getApprovals = subSidyManagementService.getApprovalStatus(selectedRow.getVoucherNo());
			if (getApprovals.getDirectorApprovalTic().equalsIgnoreCase("A")) {

				long bulkSeq = selectedRow.getBulkSeq();

				fileReport = null;
				String sourceFileName = null;

				Connection conn = null;

				try {
					conn = ConnectionManager.getConnection();

					sourceFileName = "..//reports//paymentReportForSubsidy.jrxml";
					String logopath = "//lk//informatics//ntc//view//reports//";

					// Parameters for report
					Map<String, Object> parameters = new HashMap<String, Object>();

					parameters.put("P_bulk_seq", bulkSeq);
					parameters.put("P_logo", logopath);

					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

					byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					fileReport = new DefaultStreamedContent(stream, "Application/pdf", "Print  Report_" + bulkSeq + ".pdf");

					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "pdf");

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					ConnectionManager.close(conn);
				}

			} else {
				setErrorMessage("Can not generate report. Director not approve yet!");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else {
			setErrorMessage("Please select a data row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		return fileReport;

	}

	public StreamedContent printReportExcel() throws JRException {

		String outPdfName = "test.pdf";
		String outXlsName = null;

		if (selectedRow != null) {
			getApprovals = subSidyManagementService.getApprovalStatus(selectedRow.getVoucherNo());
			if (getApprovals.getDgApprovalTic().equalsIgnoreCase("A")) {

				long bulkSeq = selectedRow.getBulkSeq();

				fileExcel = null;
				String sourceFileName = null;

				Connection conn = null;

				try {
					conn = ConnectionManager.getConnection();
					if (selectedSubsidyService.equals("S01")) {
						sourceFileName = "..//reports//paymentReportForSubsidyExcelFormat.jrxml";
					}

					if (selectedSubsidyService.equals("S02")) {
						sourceFileName = "..//reports//paymentReportForSubsidyExcelFormatGami.jrxml";
					}
					if (selectedSubsidyService.equals("S03")) {
						sourceFileName = "..//reports//paymentReportForSubsidyExcelFormatNisiSariya.jrxml";
					}
					String logopath = "//lk//informatics//ntc//view//reports//";

					// Parameters for report
					Map<String, Object> parameters = new HashMap<String, Object>();

					parameters.put("P_bulk_seq", bulkSeq);
					parameters.put("P_logo", logopath);

					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

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
					fileExcel = new DefaultStreamedContent(stream, "Application/xlsx", "Subsidy Payment excel.xlsx");

					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "xlsx");

				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					ConnectionManager.close(conn);
				}

			} else {
				setErrorMessage("Can not generate report. DG not approve yet!");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			}

		} else {
			setErrorMessage("Please select a data row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		return fileExcel;

	}
	
	
	public StreamedContent printReportPdf() throws JRException {

		String outPdfName = "test.pdf";
		String outXlsName = null;

		if (selectedRow != null) {
			

				long bulkSeq = selectedRow.getBulkSeq();

				filePdf = null;
				String sourceFileName = null;

				Connection conn = null;

				try {
					conn = ConnectionManager.getConnection();
					if (selectedSubsidyService.equals("S01")) {
						sourceFileName = "..//reports//paymentReportForSubsidyPDFFormath.jrxml";
					}

					//Report not done for S02 & S03
					if (selectedSubsidyService.equals("S02")) {
						sourceFileName = "..//reports//paymentReportForSubsidyPDFFormatGami.jrxml";
					}
					if (selectedSubsidyService.equals("S03")) {
						sourceFileName = "..//reports//paymentReportForSubsidyPDFFormatNisi.jrxml";
					}
					String logopath = "//lk//informatics//ntc//view//reports//";

					// Parameters for -report
					Map<String, Object> parameters = new HashMap<String, Object>();

					parameters.put("P_bulk_seq", bulkSeq);
					parameters.put("P_logo", logopath);

					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

					byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					filePdf = new DefaultStreamedContent(stream, "Application/pdf", "Print pdf Report_" + bulkSeq + ".pdf");

					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "pdf");

				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					ConnectionManager.close(conn);
				}

		
		} else {
			setErrorMessage("Please select a data row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		return filePdf;

	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public String getSelectedReqNo() {
		return selectedReqNo;
	}

	public void setSelectedReqNo(String selectedReqNo) {
		this.selectedReqNo = selectedReqNo;
	}

	public String getSelectedRequestorType() {
		return selectedRequestorType;
	}

	public void setSelectedRequestorType(String selectedRequestorType) {
		this.selectedRequestorType = selectedRequestorType;
	}

	public String getSelectedPrefLang() {
		return selectedPrefLang;
	}

	public void setSelectedPrefLang(String selectedPrefLang) {
		this.selectedPrefLang = selectedPrefLang;
	}

	public String getSelectedReqType() {
		return selectedReqType;
	}

	public void setSelectedReqType(String selectedReqType) {
		this.selectedReqType = selectedReqType;
	}

	public String getSelectedIdNo() {
		return selectedIdNo;
	}

	public void setSelectedIdNo(String selectedIdNo) {
		this.selectedIdNo = selectedIdNo;
	}

	public List<DocumentManagementDTO> getMandatoryList() {
		return mandatoryList;
	}

	public void setMandatoryList(List<DocumentManagementDTO> mandatoryList) {
		this.mandatoryList = mandatoryList;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public SubSidyDTO getsisuSariyaTeamAuthDTO() {
		return sisuSariyaSubsidyPaymentDTO;
	}

	public void setsisuSariyaTeamAuthDTO(SubSidyDTO sisuSariyaSubsidyPaymentDTO) {
		this.sisuSariyaSubsidyPaymentDTO = sisuSariyaSubsidyPaymentDTO;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public SubSidyDTO getSisuSariyaTeamAuthDTO() {
		return sisuSariyaSubsidyPaymentDTO;
	}

	public void setSisuSariyaTeamAuthDTO(SubSidyDTO sisuSariyaSubsidyPaymentDTO) {
		this.sisuSariyaSubsidyPaymentDTO = sisuSariyaSubsidyPaymentDTO;
	}

	public List<SubSidyDTO> getServiceRefNOList() {
		return serviceRefNOList;
	}

	public void setServiceRefNOList(List<SubSidyDTO> serviceRefNOList) {
		this.serviceRefNOList = serviceRefNOList;
	}

	public List<SubSidyDTO> getShowDataOnGrid() {
		return showDataOnGrid;
	}

	public void setShowDataOnGrid(List<SubSidyDTO> showDataOnGrid) {
		this.showDataOnGrid = showDataOnGrid;
	}

	public SubSidyDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(SubSidyDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<CommonDTO> getDrpdDistrictList() {
		return drpdDistrictList;
	}

	public void setDrpdDistrictList(List<CommonDTO> drpdDistrictList) {
		this.drpdDistrictList = drpdDistrictList;
	}

	public List<CommonDTO> getDrpdDevsecList() {
		return drpdDevsecList;
	}

	public void setDrpdDevsecList(List<CommonDTO> drpdDevsecList) {
		this.drpdDevsecList = drpdDevsecList;
	}

	public SubSidyDTO getFillServiceNO() {
		return fillServiceNO;
	}

	public void setFillServiceNO(SubSidyDTO fillServiceNO) {
		this.fillServiceNO = fillServiceNO;
	}

	public SubSidyDTO getSelectedRow1() {
		return selectedRow1;
	}

	public void setSelectedRow1(SubSidyDTO selectedRow1) {
		this.selectedRow1 = selectedRow1;
	}

	public List<SubSidyDTO> getFillServiceRefNoList() {
		return fillServiceRefNoList;
	}

	public void setFillServiceRefNoList(List<SubSidyDTO> fillServiceRefNoList) {
		this.fillServiceRefNoList = fillServiceRefNoList;
	}

	public String getEndDateValue() {
		return endDateValue;
	}

	public void setEndDateValue(String endDateValue) {
		this.endDateValue = endDateValue;
	}

	public String getStartDateValue() {
		return startDateValue;
	}

	public void setStartDateValue(String startDateValue) {
		this.startDateValue = startDateValue;
	}

	public boolean isEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}
	////

	public String getSelectedSubsidyService() {
		return selectedSubsidyService;
	}

	public void setSelectedSubsidyService(String selectedSubsidyService) {
		this.selectedSubsidyService = selectedSubsidyService;
	}

	public SubSidyManagementService getSubSidyManagementService() {
		return subSidyManagementService;
	}

	public void setSubSidyManagementService(SubSidyManagementService subSidyManagementService) {
		this.subSidyManagementService = subSidyManagementService;
	}

	public List<?> getFilSubsidyService() {
		return filSubsidyService;
	}

	public void setFilSubsidyService(List<?> filSubsidyService) {
		this.filSubsidyService = filSubsidyService;
	}

	public SubSidyDTO getSisuSariyaSubsidyPaymentDTO() {
		return sisuSariyaSubsidyPaymentDTO;
	}

	public void setSisuSariyaSubsidyPaymentDTO(SubSidyDTO sisuSariyaSubsidyPaymentDTO) {
		this.sisuSariyaSubsidyPaymentDTO = sisuSariyaSubsidyPaymentDTO;
	}

	public List<SubSidyDTO> getVoucherNOList() {
		return voucherNOList;
	}

	public void setVoucherNOList(List<SubSidyDTO> voucherNOList) {
		this.voucherNOList = voucherNOList;
	}

	public List<SubSidyDTO> getShowSisuSearchData() {
		return showSisuSearchData;
	}

	public void setShowSisuSearchData(List<SubSidyDTO> showSisuSearchData) {
		this.showSisuSearchData = showSisuSearchData;
	}

	public boolean isDisCertify() {
		return disCertify;
	}

	public void setDisCertify(boolean disCertify) {
		this.disCertify = disCertify;
	}

	public SubSidyDTO getGetApprovals() {
		return getApprovals;
	}

	public void setGetApprovals(SubSidyDTO getApprovals) {
		this.getApprovals = getApprovals;
	}

	public List<SubSidyDTO> getShowSubsidyLogSheetData() {
		return showSubsidyLogSheetData;
	}

	public void setShowSubsidyLogSheetData(List<SubSidyDTO> showSubsidyLogSheetData) {
		this.showSubsidyLogSheetData = showSubsidyLogSheetData;
	}

	public SubSidyDTO getSelectedRaw1() {
		return selectedRaw1;
	}

	public void setSelectedRaw1(SubSidyDTO selectedRaw1) {
		this.selectedRaw1 = selectedRaw1;
	}

	
	public boolean isRecommend() {
		return recommend;
	}

	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}

	public boolean isDirectorApproval() {
		return directorApproval;
	}

	public void setDirectorApproval(boolean directorApproval) {
		this.directorApproval = directorApproval;
	}

	public boolean isChairmanApproval() {
		return chairmanApproval;
	}

	public void setChairmanApproval(boolean chairmanApproval) {
		this.chairmanApproval = chairmanApproval;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public boolean isRecomendMode() {
		return recomendMode;
	}

	public void setRecomendMode(boolean recomendMode) {
		this.recomendMode = recomendMode;
	}

	public boolean isDirectorMode() {
		return directorMode;
	}

	public void setDirectorMode(boolean directorMode) {
		this.directorMode = directorMode;
	}

	public boolean isChairmanMode() {
		return chairmanMode;
	}

	public void setChairmanMode(boolean chairmanMode) {
		this.chairmanMode = chairmanMode;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isDisabledVoucherNoList() {
		return disabledVoucherNoList;
	}

	public void setDisabledVoucherNoList(boolean disabledVoucherNoList) {
		this.disabledVoucherNoList = disabledVoucherNoList;
	}

	public StreamedContent getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(StreamedContent outputFile) {
		this.outputFile = outputFile;
	}

	public StreamedContent getFileReport() {
		return fileReport;
	}

	public void setFileReport(StreamedContent fileReport) {
		this.fileReport = fileReport;
	}

	public StreamedContent getFileExcel() {
		return fileExcel;
	}

	public void setFileExcel(StreamedContent fileExcel) {
		this.fileExcel = fileExcel;
	}

	public StreamedContent getFilePdf() {
		return filePdf;
	}

	public void setFilePdf(StreamedContent filePdf) {
		this.filePdf = filePdf;
	}

	
}
