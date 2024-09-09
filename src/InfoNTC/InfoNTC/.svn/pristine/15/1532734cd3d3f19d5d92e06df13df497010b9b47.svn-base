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
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.PermitService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "permitRenewalApproval")
@ViewScoped
public class PermitRenewalApprovalBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// added
	private StreamedContent files;
	public PermitService permitservice;
	public List<String> getApplicationNo = new ArrayList<String>(0);
	public List<String> getPermitNo = new ArrayList<String>(0);
	public List<PermitDTO> permitRenewalList;
	private String busRegNo, applicationNo, permitNo, queueNo, date, errorMessage, successMessage, rejectReason,
			newDate, alertMSG;
	private PermitDTO permitDTO, selectDTO, viewSelect;
	private Date date2;
	private boolean disabledClear, disabledPrint, disabledReject, disabledApprove, isPermitRenewalsDone = false,
			disabledRePrintvoucher;
	private CommonService commonService;
	private PermitRenewalsDTO applicationHistoryDTO;
	private HistoryService historyService;

	public PermitRenewalApprovalBackingBean() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		permitservice = (PermitService) SpringApplicationContex.getBean("permitservice");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");
		getApplicationNo = permitservice.getTaskApplicationNo();
		getPermitNo = permitservice.getTaskPermitNo();
		permitDTO = new PermitDTO();
		viewSelect = new PermitDTO();
		disabledPrint = true;

	}

	@PostConstruct
	public void init() {

		if (sessionBackingBean.approveURLStatus) {
			disabledRePrintvoucher = true;
			loadValues();

		}
		if (!sessionBackingBean.approveURLStatus) {
			sessionBackingBean.setApproveURL(null);
			permitRenewalList = sessionBackingBean.getTempRenewalsDataList();
			sessionBackingBean.setApproveURLStatus(true);
			disabledRePrintvoucher = true;
		}
	}

	public void loadValues() {
		permitRenewalList = new ArrayList<PermitDTO>();
		permitRenewalList = permitservice.getApprovedData(permitDTO);

		if (permitRenewalList.isEmpty()) {
			disabledClear = true;
			disabledPrint = true;
			disabledRePrintvoucher = true;
			disabledReject = true;
			disabledApprove = true;
		} else {
			disabledRePrintvoucher = true;
			disabledReject = false;
			disabledApprove = false;
			disabledClear = false;
		}

	}

	public void selectRow() {

		boolean isRenewalDone = permitservice.isVoucherGenerated(selectDTO);

		if (isRenewalDone == false) {
			commonService.updateTaskStatus(selectDTO.getApplicationNo(), "PR200", "PM201", "C",
					sessionBackingBean.getLoginUser());
		}

	}

	public void completeRegNo() {

		busRegNo = permitservice.getBusRegNo(permitDTO.getApplicationNo(), permitDTO.getPermitNo(),
				permitDTO.getQueueNo());
		permitDTO.setBusRegNo(busRegNo);

	}

	public void completeApplicationNo() {

		applicationNo = permitservice.completeApplicationNo(permitDTO.getPermitNo(), permitDTO.getQueueNo());
		permitDTO.setApplicationNo(applicationNo);
	}

	public void completePermitNo() {
		permitNo = permitservice.completePermitNo(permitDTO.getApplicationNo(), permitDTO.getQueueNo());
		permitDTO.setPermitNo(permitNo);
	}

	public void completeQueueNo() {
		queueNo = permitservice.completeQueueNo(permitDTO.getApplicationNo(), permitDTO.getPermitNo());
		permitDTO.setQueueNo(queueNo);
	}

	public void completeDate() {

		date = permitservice.completeDate(permitDTO.getApplicationNo(), permitDTO.getPermitNo(),
				permitDTO.getQueueNo());
		permitDTO.setExpirDate(date);

	}

	public void checkQueueNo() {
		boolean isValidQueueNo = false;

		isValidQueueNo = permitservice.checkQueueNo(permitDTO.getQueueNo());

	}

	public void clearTwo() {
		permitDTO = new PermitDTO();
		disabledRePrintvoucher = true;

	}

	public void clearOne() {
		permitDTO = new PermitDTO();
		permitRenewalList.clear();
		disabledClear = true;
		disabledPrint = true;
		disabledRePrintvoucher = true;
		disabledReject = true;
		setRejectReason(null);
		disabledApprove = true;

		loadValues();

	}

	public void searchData() {

		if ((permitDTO.getApplicationNo() == null || permitDTO.getApplicationNo().trim().equalsIgnoreCase(""))
				&& (permitDTO.getQueueNo() == null || permitDTO.getQueueNo().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Select Renewal Application No. OR Enter Queue No.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {

			permitRenewalList = new ArrayList<PermitDTO>();
			permitRenewalList = permitservice.getData(permitDTO);

			if (permitRenewalList.isEmpty()) {

				setAlertMSG("No records found for searched values.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
				disabledClear = true;
				disabledPrint = true;
				disabledRePrintvoucher = true;
				disabledReject = true;
				disabledApprove = true;

			} else {

				boolean isRecepitGenerated = permitservice.isReceiptGenerated(permitDTO);

				if (isRecepitGenerated == true) {
					disabledClear = false;
					disabledPrint = false;
					disabledRePrintvoucher = false;
					disabledReject = true;
					disabledApprove = true;
					isPermitRenewalsDone = true;
					setAlertMSG("The Receipt is Already Generated For Selected Application No.");
					RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");

				} else {

					isPermitRenewalsDone = false;
					disabledClear = false;
					disabledPrint = true;
					disabledReject = false;
					disabledApprove = false;
					disabledRePrintvoucher = true;

					if (permitservice.isVoucherGenerated(permitDTO) == true) {

						disabledRePrintvoucher = false;
					}
				}

			}

		}
	}

	public void approvePermit() {
		String loginUser = sessionBackingBean.getLoginUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		if (selectDTO != null) {
			try {
				if (selectDTO.getNewExpirDate() != null) {
					String expireDate = df.format(selectDTO.getNewExpirDate());
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			boolean isPermitApprove = false;
			isPermitApprove = permitservice.isPermitApprove(selectDTO);

			if (isPermitApprove == false) {

				boolean isAvailableForApprove = false;
				isAvailableForApprove = permitservice.checkTaskPR200(selectDTO.getApplicationNo());

				boolean isSearchPermitApproval = permitservice.checkTaskDetails(selectDTO, "PM201", "O");

				if (isAvailableForApprove == true || isSearchPermitApproval == true) {

					boolean isPermitReject = false;
					isPermitReject = permitservice.isPermitReject(selectDTO);

					if (isPermitReject == false) {

						/* Update Application History Table */
						applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());
						boolean isApprove = false;

						/**change method 30/12/2021 status updating and task updates are in same methods**/
						isApprove = permitservice.approvePermitAndTaskUpdateAndInsertHistory(selectDTO, sessionBackingBean.getLoginUser(),
							"PM201"	,applicationHistoryDTO);
						/**end**/

						disabledPrint = true;

						if (isApprove == true) {
							setSuccessMessage("Permit renewal approval successful.");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							
							permitRenewalList = permitservice.getApprovedData(permitDTO);

						} else {
							setErrorMessage("Permit renewal approval fail..");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Selected permit is already rejected.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please complete permit renewal task.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Selected permit is already approved.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void preReject() {

		if (selectDTO != null) {

			boolean isPermitApprove = false;
			isPermitApprove = permitservice.isPermitApprove(selectDTO);

			if (isPermitApprove == false) {

				boolean isPermitReject = false;
				isPermitReject = permitservice.isPermitReject(selectDTO);

				if (isPermitReject == false) {

					boolean isAvailableForReject = false;
					isAvailableForReject = permitservice.checkTaskPR200(selectDTO.getApplicationNo());

					if (isAvailableForReject == true) {

						RequestContext.getCurrentInstance().execute("PF('dlg1').show()");

					} else {
						setErrorMessage("Please complete permit renewal task.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Selected permit is already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Selected permit is already approved.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void rejectPermit() {
		String loginUser = sessionBackingBean.getLoginUser();

		if (rejectReason != null && !rejectReason.trim().equalsIgnoreCase("")) {

			permitDTO.setRejectReason(rejectReason);

			/* Update Application History Table */
			applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
					sessionBackingBean.getLoginUser());
			boolean permitReject = false;
			permitReject = permitservice.rejectPermit(selectDTO, permitDTO);
			historyService.insertApplicationHistoryData(applicationHistoryDTO);

			if (permitReject == true) {

				setSuccessMessage("Successfully Reject Permit.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			} else {
				setErrorMessage("Permit Rejection Fail");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please add a reject reason.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public StreamedContent prinAction() throws JRException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if (selectDTO != null) {

			try {
				if (selectDTO.getNewExpirDate() != null) {

					String expireDate = df.format(selectDTO.getNewExpirDate());
					selectDTO.setExpirDate(expireDate);
					if (selectDTO.getExpirDate() != null) {
						/** commented by tharushi.e for update pm_expire_date by pm_new_expired_date **/
						permitservice.changePermitRenewalPeriod(selectDTO, sessionBackingBean.loginUser);
						permitservice.updateExpireDateByNewExpireDate(selectDTO, sessionBackingBean.loginUser);// added
																												// by
																												// tharushi.e
					}

				}

			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			files = null;
			String sourceFileName = null;
			Connection conn = null;
			String reprintLabel = null;

			try {
				conn = ConnectionManager.getConnection();

				sourceFileName = "..//reports//NewPermitInformation.jrxml";
				String logopath = "//lk//informatics//ntc//view//reports//";

				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();

				parameters.put("P_APP", permitRenewalList.get(0).getApplicationNo());
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

				commonService.insertPermitPrintInfo(permitRenewalList.get(0).getApplicationNo(),
						permitRenewalList.get(0).getPermitNo(), permitRenewalList.get(0).getBusRegNo(),
						sessionBackingBean.getLoginUser());

				if (permitservice.checkTaskDetails(selectDTO, "PM400", "C") == false) {

					commonService.updateTaskStatus(permitRenewalList.get(0).getApplicationNo(), "PM302", "PM400", "C",
							sessionBackingBean.getLoginUser());
					commonService.updateTaskStatusCompleted(permitRenewalList.get(0).getApplicationNo(), "PM400",
							sessionBackingBean.getLoginUser());
					
					/*** temporary active vehicle number status roll back in complain request table***/
					boolean isTemporaryActive =commonService.isTemporaryActiveBusNumber(permitRenewalList.get(0).getBusRegNo());
							if(isTemporaryActive) {
								commonService.insertDataIntoComplainRequestHistoryAndUpdate(permitRenewalList.get(0).getBusRegNo(),sessionBackingBean.getLoginUser());
								
							}
					/***end***/

				}

				return files;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				ConnectionManager.close(conn);
			}
		} else {

			setErrorMessage("Please Select a Row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		return files;

	}

	public String viewAction() {

		boolean isTaskCompleteInDetails = false;
		boolean isTaskCompleteInHistory = false;

		isTaskCompleteInDetails = commonService.checkTaskDetails(viewSelect.getApplicationNo(), "PR200");
		isTaskCompleteInHistory = commonService.checkTaskHistory(viewSelect.getApplicationNo(), "PR200");

		if (isTaskCompleteInDetails == true || isTaskCompleteInHistory == true) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setPageMode("V");
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			sessionBackingBean.setApplicationNo(viewSelect.getApplicationNo());
			sessionBackingBean.setBusRegNo(viewSelect.getBusRegNo());
			sessionBackingBean.setPermitNo(viewSelect.getPermitNo());
			sessionBackingBean.setTempRenewalsDataList(permitRenewalList);
			sessionBackingBean.renewalViewMood = true;

			return "/pages/viewPermitRenewals/viewPermitRenewalsNew.xhtml#!";

		} else {

			setAlertMSG("Should be completed Permit Renewal task.");
			RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
			return null;
		}

	}

	public StreamedContent rePrintvoucher() throws JRException {

		if (selectDTO != null) {

			if (selectDTO.getApplicationNo() != null || !selectDTO.getApplicationNo().trim().equalsIgnoreCase("")) {

				String voucherNo = permitservice.getVoucherNo(selectDTO.getApplicationNo());

				files = null;
				String sourceFileName = null;

				Connection conn = null;

				try {
					conn = ConnectionManager.getConnection();

					sourceFileName = "..//reports//debitVoucher.jrxml";

					// Parameters for report
					Map<String, Object> parameters = new HashMap<String, Object>();

					parameters.put("P_Parameter", voucherNo);

					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
					jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

					byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					files = new DefaultStreamedContent(stream, "Application/pdf",
							"PaymentVoucher - " + voucherNo + ".pdf");

					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "pdf");

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					ConnectionManager.close(conn);
				}

			}

		} else {

			setErrorMessage("Please Select a Row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		return files;

	}

	public List<PermitDTO> getPermitRenewalList() {
		return permitRenewalList;
	}

	public void setPermitRenewalList(List<PermitDTO> permitRenewalList) {
		this.permitRenewalList = permitRenewalList;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public String getNewDate() {
		return newDate;
	}

	public void setNewDate(String newDate) {
		this.newDate = newDate;
	}

	public boolean isDisabledClear() {
		return disabledClear;
	}

	public void setDisabledClear(boolean disabledClear) {
		this.disabledClear = disabledClear;
	}

	public boolean isDisabledPrint() {
		return disabledPrint;
	}

	public void setDisabledPrint(boolean disabledPrint) {
		this.disabledPrint = disabledPrint;
	}

	public boolean isDisabledReject() {
		return disabledReject;
	}

	public void setDisabledReject(boolean disabledReject) {
		this.disabledReject = disabledReject;
	}

	public boolean isDisabledApprove() {
		return disabledApprove;
	}

	public void setDisabledApprove(boolean disabledApprove) {
		this.disabledApprove = disabledApprove;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public void setBusRegNo(String busRegNo) {
		this.busRegNo = busRegNo;
	}

	public PermitDTO getPermitDTO() {
		return permitDTO;
	}

	public void setPermitDTO(PermitDTO permitDTO) {
		this.permitDTO = permitDTO;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public PermitService getPermitservice() {
		return permitservice;
	}

	public List<String> getGetApplicationNo() {
		return getApplicationNo;
	}

	public void setGetApplicationNo(List<String> getApplicationNo) {
		this.getApplicationNo = getApplicationNo;
	}

	public PermitDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(PermitDTO selectDTO) {

		this.selectDTO = selectDTO;
	}

	public void setPermitservice(PermitService permitservice) {
		this.permitservice = permitservice;
	}

	public List<String> getGetPermitNo() {
		return getPermitNo;
	}

	public void setGetPermitNo(List<String> getPermitNo) {
		this.getPermitNo = getPermitNo;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isPermitRenewalsDone() {
		return isPermitRenewalsDone;
	}

	public void setPermitRenewalsDone(boolean isPermitRenewalsDone) {
		this.isPermitRenewalsDone = isPermitRenewalsDone;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public PermitDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(PermitDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public boolean isDisabledRePrintvoucher() {
		return disabledRePrintvoucher;
	}

	public void setDisabledRePrintvoucher(boolean disabledRePrintvoucher) {
		this.disabledRePrintvoucher = disabledRePrintvoucher;
	}

	public PermitRenewalsDTO getApplicationHistoryDTO() {
		return applicationHistoryDTO;
	}

	public void setApplicationHistoryDTO(PermitRenewalsDTO applicationHistoryDTO) {
		this.applicationHistoryDTO = applicationHistoryDTO;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

}
