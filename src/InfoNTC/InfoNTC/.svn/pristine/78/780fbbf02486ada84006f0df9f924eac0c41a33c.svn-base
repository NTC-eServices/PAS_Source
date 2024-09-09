package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
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
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.IssuePermitDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.PermitService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "newPermitPrintingBackingBean")
@ViewScoped
public class NewPermitPrintingBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services
	private PermitService permitservice;
	private CommonService commonService;
	private IssuePermitService issuePermitService;
	private DocumentManagementService documentManagementService;
	private StreamedContent files;

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	// DTO
	private DocumentManagementDTO documentManagementFileDTO = new DocumentManagementDTO();
	private List<PermitDTO> appNoList = new ArrayList<PermitDTO>();
	private List<VehicleInspectionDTO> serviceTypeList = new ArrayList<VehicleInspectionDTO>();
	private List<DocumentManagementDTO> filePathList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> filePathVersionList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> newFilePathList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> newFilePathVersionList = new ArrayList<DocumentManagementDTO>();
	private PermitDTO permitDTO;
	public List<PermitDTO> showDetailsForGrid;
	private PermitDTO selectList;
	private List<String> tenderNoList = new ArrayList<String>();

	private String strSelectedTenderRefNO;
	private String strSelectedAppNo;
	private String strSelectedServiceType;
	private String strSelectedPermiNo;
	private Date selctpermitDate;
	private String selectqueueNo;
	private String errorMsg;
	private String applicationNo;
	private String queueNo;
	private String tenderRefNo;
	private String serviceType;
	private String busRegNo;
	private String permitNo;
	private String myDate;
	private String errorMessage, successMessage;
	private boolean disableApprove, disableReject, disablePrint;
	private String rejectReason;
	private String status, taskStatus, paymentTask, reprint, showpermitNo;
	private String viewNewPermitURL;

	private IssuePermitDTO serviceHistoryDTO;
	private PermitRenewalsDTO vehicleOwnerHistoryDTO;
	private OminiBusDTO OminiBusHistoryDTO;
	private PermitRenewalsDTO applicationHistoryDTO;
	private HistoryService historyService;

	public NewPermitPrintingBackingBean() {
		permitservice = (PermitService) SpringApplicationContex.getBean("permitservice");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");
	}

	@PostConstruct
	public void init() {
		permitDTO = new PermitDTO();
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		appNoList = permitservice.getAppNoToDropdown();
		tenderNoList = permitservice.getTenderRefNoToDropdown();
		serviceTypeList = permitservice.getServiceTypeToDropdown();

		showDetailsForGrid = new ArrayList<PermitDTO>();
		showDetailsForGrid = permitservice.showDetails(permitDTO);
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		myDate = localDate.format(formatter);
		rejectReason = null;
		selectList = null;
		clearSearch();

		disableApprove = true;
		disableReject = true;
		disablePrint = true;

		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object objCallerApplicationNo = fcontext.getExternalContext().getSessionMap().get("APPLICATION_NO");

		if (objCallerApplicationNo != null) {
			String strApplicationNo = (String) objCallerApplicationNo;
			permitDTO.setApplicationNo(strApplicationNo);

			fillTenderNumber();
			fillQueueNo();
			fillServiceType();
			fillPermitNo();
			fillRegNo();

			searchForGrid();
			fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", null);

		}
	}

	public void fillTenderNumber() {
		tenderRefNo = permitservice.fillTenderNumber(permitDTO.getPermitNo(), permitDTO.getApplicationNo(),
				permitDTO.getQueueNo());
		permitDTO.setTenderRefNo(tenderRefNo);
	}

	public void fillQueueNo() {
		queueNo = permitservice.fillQueueNo(permitDTO.getApplicationNo());
		permitDTO.setQueueNo(queueNo);
	}

	public void fillServiceType() {
		serviceType = permitservice.fillServiceType(permitDTO.getPermitNo(), permitDTO.getApplicationNo(),
				permitDTO.getQueueNo(), permitDTO.getTenderRefNo());
		permitDTO.setServiceType(serviceType);
	}

	public void fillPermitNo() {
		permitNo = permitservice.fillPermitNo(permitDTO.getApplicationNo());
		permitDTO.setPermitNo(permitNo);
	}

	public void fillRegNo() {

		busRegNo = permitservice.fillRegNo(permitDTO.getApplicationNo());
		permitDTO.setBusRegNo(busRegNo);
	}

	public void fillAppNo() {

		applicationNo = permitservice.fillAppNo(permitDTO.getPermitNo(), permitDTO.getQueueNo(),
				permitDTO.getTenderRefNo());
		permitDTO.setApplicationNo(applicationNo);

	}

	public void searchForGrid() {
		String loginUser = sessionBackingBean.getLoginUser();
		boolean isTasKPM201Available = permitservice.checkTaskDetails(permitDTO, "PM201", "O");
		boolean isTasKPM201Completed = permitservice.checkTaskDetails(permitDTO, "PM201", "C");

		if ((permitDTO.getPermitNo() == null || permitDTO.getPermitNo().trim().equalsIgnoreCase(""))
				&& (permitDTO.getApplicationNo() == null || permitDTO.getApplicationNo().trim().equalsIgnoreCase(""))
				&& (permitDTO.getQueueNo() == null || permitDTO.getQueueNo().trim().equalsIgnoreCase(""))
				&& (permitDTO.getTenderRefNo() == null || permitDTO.getTenderRefNo().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please select  an application number for searching.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			disableApprove = true;
			disableReject = true;
			disablePrint = true;

		} else {
			showDetailsForGrid = new ArrayList<PermitDTO>();
			showDetailsForGrid = permitservice.showDetails(permitDTO);
			disableApprove = false;
			disableReject = false;
			disablePrint = false;

			if (selectList != null) {

				disableApprove = false;
				disableReject = false;
				disablePrint = false;

			}

			if (showDetailsForGrid.isEmpty()) {
				setErrorMessage("No records for searched values.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				disableApprove = true;
				disableReject = true;
				disablePrint = true;
			}

		}

	}

	public void clearDetails() {
		permitDTO = new PermitDTO();

		showDetailsForGrid = new ArrayList<PermitDTO>();
		rejectReason = null;
	}

	public void approveUser() {
		String loginUser = sessionBackingBean.getLoginUser();

		if (selectList != null) {

			permitservice = (PermitService) SpringApplicationContex.getBean("permitservice");
			status = permitservice.getPmStatus(selectList.getApplicationNo());
			taskStatus = permitservice.getTaskStatus(selectList.getApplicationNo());

			try {
				if (taskStatus == null) {
					if (status.equals("A")) {
						errorMessage = "Application already approved";
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						disablePrint = false;
						disableReject = false;
					} else if (status.equals("R")) {

						errorMessage = "Application already rejected";
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

						disableReject = false;
					}
				}
				if (taskStatus.equals("C")) {
					if (!"A".equals(status) && !"R".equals(status)) {

						/* Update Application History Data */
						applicationHistoryDTO = historyService.getApplicationTableData(selectList.getApplicationNo(),
								sessionBackingBean.getLoginUser());
						permitservice.updatePmStatus(selectList.getApplicationNo());
						historyService.insertApplicationHistoryData(applicationHistoryDTO);

						successMessage = "Application approved successfully";
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						String app = permitDTO.getApplicationNo();

						disableReject = true;
						disableApprove = true;

						//showpermitNo = issuePermitService.generatePermitNoNew(selectList.getApplicationNo());
						//String OldPermitNo = issuePermitService.getTempPermit(app);
						//permitDTO.setPermitNo(showpermitNo);

						/*
						 * Update Application, OminiBus, Vehicle Owner and
						 * Service History table
						 */

						applicationHistoryDTO = historyService.getApplicationTableData(permitDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						OminiBusHistoryDTO = historyService.getOminiBusTableData(permitDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						vehicleOwnerHistoryDTO = historyService.getVehicleOwnerTableData(permitDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						serviceHistoryDTO = historyService.getServiceTableData(permitDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());
						
						permitservice.updatePermitNo(permitDTO);
						disablePrint = false;

						/* Save History Data */
						historyService.insertApplicationHistoryData(applicationHistoryDTO);
						historyService.insertOminiBusHistoryData(OminiBusHistoryDTO);
						historyService.insertVehicleOwnerHistoryData(vehicleOwnerHistoryDTO);
						historyService.insertServiceHistoryData(serviceHistoryDTO);

						commonService.updateTaskStatus(permitDTO.getApplicationNo(), "PM200", "PM201", "C",
								sessionBackingBean.getLoginUser());
						commonService.updateTaskStatusCompleted(permitDTO.getApplicationNo(), "PM201",
								sessionBackingBean.getLoginUser());

				/*		String propertyFilePath = null;
						try {
							propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
						} catch (ApplicationException e) {
							e.printStackTrace();
						}

						String path = propertyFilePath + File.separator + OldPermitNo;
						String newPath = propertyFilePath + File.separator + showpermitNo;

						String ApplicationNo = permitDTO.getApplicationNo();

						File dir = new File(path);
						File newName = new File(newPath);
						if (dir.isDirectory()) {
							dir.renameTo(newName);

							filePathList = documentManagementService.FilePathList(ApplicationNo);

							for (int i = 0; i < filePathList.size(); i++) {
								DocumentManagementDTO str = new DocumentManagementDTO();

								String st = filePathList.get(i).getFilePath();
								Long seq = filePathList.get(i).getSeq();

								String test = st.replaceAll(OldPermitNo, showpermitNo);

								str.setFilePath(test);
								str.setSeq(seq);

								newFilePathList.add(str);

							}

							int result = documentManagementService.updateFilePaths(newFilePathList, showpermitNo,
									loginUser);

							filePathVersionList = documentManagementService.FilePathVersionList(ApplicationNo);

							for (int i = 0; i < filePathVersionList.size(); i++) {
								DocumentManagementDTO str = new DocumentManagementDTO();

								String st = filePathVersionList.get(i).getFilePath();
								Long seq = filePathVersionList.get(i).getSeq();

								String test = st.replaceAll(OldPermitNo, showpermitNo);

								str.setFilePath(test);
								str.setSeq(seq);

								newFilePathVersionList.add(str);

							}

							int result1 = documentManagementService.updateVersionFilePaths(newFilePathVersionList,
									showpermitNo, loginUser);

							
							
							
						} else {

						}
*/
					}

				} else if (!"C".equals(taskStatus)) {

					errorMessage = "Task is  not completed yet";
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				}
			} catch (NullPointerException e) {

			}

		} else {
			errorMessage = "Please select an application";
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		selectList = null;

	}

	public void rejectUser() {
		String loginUser = sessionBackingBean.getLoginUser();

		disablePrint = true;

		if (selectList != null) {
			permitservice = (PermitService) SpringApplicationContex.getBean("permitservice");
			status = permitservice.getPmStatus(selectList.getApplicationNo());
			taskStatus = permitservice.getTaskStatus(selectList.getApplicationNo());
			try {
				if (taskStatus == null) {
					if (status.equals("A")) {
						errorMessage = "Application already approved";
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						disablePrint = false;
						disableReject = false;
					} else if (status.equals("R")) {

						errorMessage = "Application already rejected";
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

						disableReject = false;
					}
				}
				if (taskStatus.equals("C")) {
					if (!"A".equals(status) && !"R".equals(status)) {

						if (rejectReason != null && !rejectReason.isEmpty()) {

							applicationHistoryDTO = historyService.getApplicationTableData(
									selectList.getApplicationNo(), sessionBackingBean.getLoginUser());
							permitservice.rejectUser(selectList.getApplicationNo(), rejectReason);
							successMessage = "Application rejected successfully";
							historyService.insertApplicationHistoryData(applicationHistoryDTO);
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							disableReject = true;
							disableApprove = true;
							commonService.updateTaskStatusCompleted(permitDTO.getApplicationNo(), "PM201",
									sessionBackingBean.getLoginUser());

						} else {
							errorMessage = "Please provide reject reason";
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

						}
					}

				} else if (!"C".equals(taskStatus)) {

					errorMessage = "Task is  not completed yet";
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				}

			} catch (NullPointerException e) {

			}
		} else {
			errorMessage = "Please select an application";
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		rejectReason = null;
		selectList = null;
	}

	public void clearSearch() {
		rejectReason = null;
		showDetailsForGrid = new ArrayList<PermitDTO>();
		permitDTO = new PermitDTO();

		showDetailsForGrid = new ArrayList<PermitDTO>();
		disableApprove = true;
		disableReject = true;
		disablePrint = true;
	}

	public String viewIssueNewPermit() {
		String Url = null;
		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().trim().equalsIgnoreCase("")) {

			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", permitDTO.getApplicationNo());

			String viewedApplicationNo = permitDTO.getApplicationNo();

			String searchedQueueNum = permitDTO.getQueueNo();
			String selectedTenderRef = permitDTO.getTenderRefNo();
			String selectedServiceNO = permitDTO.getServiceType();
			String selectedBusRegNo = permitDTO.getBusRegNo();

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			viewNewPermitURL = request.getRequestURL().toString();
			sessionBackingBean.setViewNewPermitURL(viewNewPermitURL);
			sessionBackingBean.setViewNewPermitURLStatus(true);
			sessionBackingBean.setViewedInspectionApplicationNo(viewedApplicationNo);
			sessionBackingBean.setViewedPermitNo(selectedTenderRef);
			sessionBackingBean.setSelectedQueueNo(searchedQueueNum);
			sessionBackingBean.setServiceType(selectedServiceNO);
			sessionBackingBean.setBusRegNo(selectedBusRegNo);

			Url = "/pages/issueNewPermit/viewNewPermit.xhtml";
		} else {

			errorMessage = "Permit Number not issue yet";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
		return Url;
	}

	public void search() {
	}

	public void prinAction() throws JRException {
		files = null;
		String sourceFileName = null;
		Connection conn = null;
		String reprintLabel = null;
		if (selectList != null) {

			permitservice = (PermitService) SpringApplicationContex.getBean("permitservice");
			// pm301
			paymentTask = permitservice.checkTaskForPayment(selectList.getApplicationNo());
			reprint = permitservice.checkTaskForReprint(selectList.getApplicationNo());
			status = permitservice.getPmStatus(selectList.getApplicationNo());

			if (!"A".equals(status)) {
				errorMessage = "Application not approved yet";
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			} else {

				try {

					if (paymentTask != null) {
						if (paymentTask.equalsIgnoreCase("C")) {
							// pm400 c in det and O in history
							reprintLabel = null;
							commonService.updateTaskStatus(selectList.getApplicationNo(), "PM302", "PM400", "C",
									sessionBackingBean.getLoginUser());
							commonService.updateTaskStatusCompleted(selectList.getApplicationNo(), "PM400",
									sessionBackingBean.getLoginUser());
							
							//genarate new permit no.
							String permitNo = issuePermitService.generatePermitNoNew(selectList.getApplicationNo());
							//update application table with permit no.
							permitDTO.setPermitNo(permitNo);
							permitservice.updatePermitNo(permitDTO);
										
							sourceFileName = "..//reports//NewPermitInformation.jrxml";
							permitservice.updatePrintStatus(selectList.getApplicationNo());
						}
					}

					else if (paymentTask == null) {

						if (reprint.equalsIgnoreCase("C")) {
							sourceFileName = "..//reports//NewPermitInformation.jrxml";
							reprintLabel = "RE-PRINT";

						}
					} else {

						errorMessage = "Payment has not done for this Application Number";
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

					try {
						conn = ConnectionManager.getConnection();

						String logopath = "//lk//informatics//ntc//view//reports//";

						// Parameters for report
						Map<String, Object> parameters = new HashMap<String, Object>();

						parameters.put("P_APP", selectList.getApplicationNo());
						parameters.put("P_LOGO", logopath);
						parameters.put("P_REPRINT", reprintLabel);

						JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

						JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

						byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
						InputStream stream = new ByteArrayInputStream(pdfByteArray);
						files = new DefaultStreamedContent(stream, "Application/pdf", "New Permit Printing.pdf");

						ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
						Map<String, Object> sessionMap = externalContext.getSessionMap();
						sessionMap.put("reportBytes", pdfByteArray);
						sessionMap.put("docType", "pdf");

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ConnectionManager.close(conn);
					}

				} catch (Exception e) {

					errorMessage = "Payment has not done for this Application Number";
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				}

			}

		}

		else {
			errorMessage = "Please select an application";
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

	}

	public List<PermitDTO> getAppNoList() {
		return appNoList;
	}

	public PermitService getPermitservice() {
		return permitservice;
	}

	public void setPermitservice(PermitService permitservice) {
		this.permitservice = permitservice;
	}

	public void setAppNoList(List<PermitDTO> appNoList) {
		this.appNoList = appNoList;
	}

	public PermitDTO getPermitDTO() {
		return permitDTO;
	}

	public void setPermitDTO(PermitDTO permitDTO) {
		this.permitDTO = permitDTO;
	}

	public String getStrSelectedAppNo() {
		return strSelectedAppNo;
	}

	public void setStrSelectedAppNo(String strSelectedAppNo) {
		this.strSelectedAppNo = strSelectedAppNo;
	}

	public List<VehicleInspectionDTO> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<VehicleInspectionDTO> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public String getStrSelectedServiceType() {
		return strSelectedServiceType;
	}

	public void setStrSelectedServiceType(String strSelectedServiceType) {
		this.strSelectedServiceType = strSelectedServiceType;
	}

	public String getSelectqueueNo() {
		return selectqueueNo;
	}

	public void setSelectqueueNo(String selectqueueNo) {
		this.selectqueueNo = selectqueueNo;
	}

	public Date getSelctpermitDate() {
		return selctpermitDate;
	}

	public void setSelctpermitDate(Date selctpermitDate) {
		this.selctpermitDate = selctpermitDate;
	}

	public String getMyDate() {
		return myDate;
	}

	public void setMyDate(String myDate) {
		this.myDate = myDate;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getStrSelectedPermiNo() {
		return strSelectedPermiNo;
	}

	public void setStrSelectedPermiNo(String strSelectedPermiNo) {
		this.strSelectedPermiNo = strSelectedPermiNo;
	}

	public List<String> getTenderNoList() {
		return tenderNoList;
	}

	public void setTenderNoList(List<String> tenderNoList) {
		this.tenderNoList = tenderNoList;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getStrSelectedTenderRefNO() {
		return strSelectedTenderRefNO;
	}

	public void setStrSelectedTenderRefNO(String strSelectedTenderRefNO) {
		this.strSelectedTenderRefNO = strSelectedTenderRefNO;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<PermitDTO> getShowDetailsForGrid() {
		return showDetailsForGrid;
	}

	public void setShowDetailsForGrid(List<PermitDTO> showDetailsForGrid) {
		this.showDetailsForGrid = showDetailsForGrid;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public boolean isDisableApprove() {
		return disableApprove;
	}

	public void setDisableApprove(boolean disableApprove) {
		this.disableApprove = disableApprove;
	}

	public boolean isDisableReject() {
		return disableReject;
	}

	public void setDisableReject(boolean disableReject) {
		this.disableReject = disableReject;
	}

	public boolean isDisablePrint() {
		return disablePrint;
	}

	public void setDisablePrint(boolean disablePrint) {
		this.disablePrint = disablePrint;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public PermitDTO getSelectList() {
		return selectList;
	}

	public void setSelectList(PermitDTO selectList) {
		this.selectList = selectList;
	}

	public String getViewNewPermitURL() {
		return viewNewPermitURL;
	}

	public void setViewNewPermitURL(String viewNewPermitURL) {
		this.viewNewPermitURL = viewNewPermitURL;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public IssuePermitService getIssuePermitService() {
		return issuePermitService;
	}

	public void setIssuePermitService(IssuePermitService issuePermitService) {
		this.issuePermitService = issuePermitService;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public List<DocumentManagementDTO> getFilePathList() {
		return filePathList;
	}

	public void setFilePathList(List<DocumentManagementDTO> filePathList) {
		this.filePathList = filePathList;
	}

	public List<DocumentManagementDTO> getNewFilePathList() {
		return newFilePathList;
	}

	public void setNewFilePathList(List<DocumentManagementDTO> newFilePathList) {
		this.newFilePathList = newFilePathList;
	}

	public DocumentManagementDTO getDocumentManagementFileDTO() {
		return documentManagementFileDTO;
	}

	public void setDocumentManagementFileDTO(DocumentManagementDTO documentManagementFileDTO) {
		this.documentManagementFileDTO = documentManagementFileDTO;
	}

	public List<DocumentManagementDTO> getNewFilePathVersionList() {
		return newFilePathVersionList;
	}

	public void setNewFilePathVersionList(List<DocumentManagementDTO> newFilePathVersionList) {
		this.newFilePathVersionList = newFilePathVersionList;
	}

	public List<DocumentManagementDTO> getFilePathVersionList() {
		return filePathVersionList;
	}

	public void setFilePathVersionList(List<DocumentManagementDTO> filePathVersionList) {
		this.filePathVersionList = filePathVersionList;
	}

}
