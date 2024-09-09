package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "issueTenderBackingBean")
@ViewScoped
public class IssueTenderBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private PaymentVoucherDTO paymentVoucherDTO;
	private TenderDTO tenderDTO, tenderDetailsDTO, filledTenderDTO;
	private List<TenderDTO> getTenderApplicationTypeList = new ArrayList<TenderDTO>(0);
	private List<TenderDTO> getTenderRefNoList = new ArrayList<TenderDTO>(0);
	private TenderService issueTenderService;
	private List<TenderDTO> getTitleList = new ArrayList<TenderDTO>(0);
	private List<TenderDTO> getIdTypeList = new ArrayList<TenderDTO>(0);
	private List<TenderDTO> applicationDetails;
	private List<TenderDTO> getTenderAppNoList = new ArrayList<TenderDTO>(0);
	private List<TenderDTO> tenderDetailList;
	private Date closeDate, closeTime;
	private QueueManagementService queueManagementService;
	private CommonService commonService;
	private MigratedService migratedService;
	private String alertMSG, successMessage, errorMessage;
	private boolean disabledCallNext, disabledSkip, disabledViewTender, disabledGenerateVoucher, disabledCancel,
			disabledIssueTenderEnv, disabledReIssueTender, disabledIssueTender, disabledSave, disableApplicationNo,
			disableTenderRefNo, disableApplicationType;
	private StreamedContent files;

	public IssueTenderBackingBean() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		issueTenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		tenderDTO = new TenderDTO();
		loadValues();

	}

	public void loadValues() {
		getIdTypeList = issueTenderService.getIdTypeList();
		getTitleList = issueTenderService.getTitleList();
		getTenderApplicationTypeList = issueTenderService.getApplicationTypeList();
		getTenderRefNoList = issueTenderService.getTenderRefNoList();
		disabledSave = true;
		disabledCancel = true;
		disabledViewTender = true;
		disabledGenerateVoucher = true;
		disabledIssueTenderEnv = true;
		disabledReIssueTender = true;
		disabledIssueTender = true;
		disabledSkip = true;
	}

	public void search() {

		if (tenderDTO.getTenderAppNo() != null && !tenderDTO.getTenderAppNo().trim().equalsIgnoreCase("")) {

			String tenderRefNo = issueTenderService.getTenderRefNo(tenderDTO);

			if (tenderRefNo != null && !tenderRefNo.trim().equalsIgnoreCase("")) {
				tenderDTO.setTenderRefNo(tenderRefNo);

				tenderDetailsDTO = issueTenderService.getTenderDetails(tenderDTO);

				tenderDTO = issueTenderService.getApplicantDetails(tenderDTO);
				tenderDTO.setTenderDes(tenderDetailsDTO.getTenderDes());
				tenderDTO.setTime(tenderDetailsDTO.getTime());
				tenderDTO.setStringCloseDate(tenderDetailsDTO.getStringCloseDate());

				disabledCallNext = true;
				disabledSkip = true;
				disabledSave = true;
				disabledGenerateVoucher = false;
				disabledReIssueTender = true;
				disabledIssueTenderEnv = true;

				disabledCancel = false;
				disabledIssueTender = true;

				boolean isVoucherGenerated = issueTenderService.isVoucherGenerated(tenderDTO.getTenderAppNo());

				if (isVoucherGenerated == true) {

					disabledIssueTender = false;
					disabledGenerateVoucher = true;
					disabledCancel = true;

					boolean isPaymentDone = issueTenderService.isPaymentDone(tenderDTO.getTenderAppNo());

					if (isPaymentDone == true) {

						disabledIssueTender = false;
						disabledGenerateVoucher = true;
						disabledCancel = true;

						boolean isReciptGenerated = issueTenderService.isReciptGenerated(tenderDTO.getTenderAppNo());

						if (isReciptGenerated == true) {

							disabledIssueTender = false;
							disabledGenerateVoucher = true;
							disabledCancel = true;

							setSuccessMessage("Please Print The Issue Tender Document");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						} else {
							setAlertMSG("Generate Receipt is not Done ");
							RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");

							disabledIssueTender = true;
							disabledGenerateVoucher = false;
							disabledSkip = true;
							disabledCancel = true;
						}

					} else {
						setAlertMSG("Payment is not Approved.");
						RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");

						disabledIssueTender = true;
						disabledGenerateVoucher = false;
						disabledSkip = true;
						disabledCancel = true;
					}

				} else {

					disabledIssueTender = true;
					disabledGenerateVoucher = false;
					disabledCancel = true;

					setAlertMSG("Voucher is not Generated.");
					RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");

				}

			} else {
				setErrorMessage("No Data Found For Searched Application No.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {

			if (tenderDTO.getTenderAppType() != null && !tenderDTO.getTenderAppType().trim().equalsIgnoreCase("")) {

				if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {

					tenderDetailsDTO = issueTenderService.getTenderDetails(tenderDTO);

					tenderDTO.setTenderDes(tenderDetailsDTO.getTenderDes());
					tenderDTO.setTime(tenderDetailsDTO.getTime());
					tenderDTO.setStringCloseDate(tenderDetailsDTO.getStringCloseDate());

					disabledCallNext = true;
					disabledSkip = true;

					disabledSave = false;
					disabledCancel = false;

					setAlertMSG("Please Fill the Applicant Info to continue filling the Form.");
					RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");

				} else {
					setErrorMessage("Please Select the Tender Ref. No.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please Select the Application Type.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
	}

	public void ajaxTenderRefNoSearch() {
		disableApplicationNo = true;
	}

	public void ajaxApplicarionNoSearch() {

		disableApplicationType = true;
		disableTenderRefNo = true;
	}

	public void ajaxFillData() {

		if (tenderDTO.getNicNo() != null && !tenderDTO.getNicNo().trim().equalsIgnoreCase("")) {

			filledTenderDTO = new TenderDTO();
			filledTenderDTO = issueTenderService.fillApplicantDetails(tenderDTO);
			tenderDTO.setTitleCode(filledTenderDTO.getTitleCode());
			tenderDTO.setAddressOne(filledTenderDTO.getAddressOne());
			tenderDTO.setAddressTwo(filledTenderDTO.getAddressTwo());
			tenderDTO.setCity(filledTenderDTO.getCity());
			tenderDTO.setContactNo(filledTenderDTO.getContactNo());
			tenderDTO.setApplicantName(filledTenderDTO.getApplicantName());
		}
	}

	public void clearOne() {

		tenderDTO = new TenderDTO();

		disabledGenerateVoucher = true;
		disabledIssueTender = true;
		disabledIssueTenderEnv = true;
		disabledSave = true;
		disabledViewTender = true;
		disabledCancel = true;
		disabledReIssueTender = true;
		disableApplicationNo = false;
		disabledCallNext = false;
		disableApplicationType = false;
		disableTenderRefNo = false;
		disabledSkip = true;

	}

	public void clearTwo() {

		tenderDTO.setNicNo(null);
		tenderDTO.setAddressOne(null);
		tenderDTO.setAddressTwo(null);
		tenderDTO.setApplicantName(null);
		tenderDTO.setContactNo(null);
		tenderDTO.setTitleCode(null);
		tenderDTO.setApplicationRevPerson(null);
		tenderDTO.setRecPersonIdNo(null);
		tenderDTO.setRecPersonIdTypeCode(null);
		tenderDTO.setVoucherNo(null);
		tenderDTO.setRemark(null);
	}

	public void skip() {

		migratedService.updateSkipQueueNumberStatus(tenderDTO.getTenderQueueNo());
		disabledCallNext = false;
		disabledSkip = true;
		disableApplicationNo = true;

		setSuccessMessage("Queue No. Skip Success.");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

	}

	public void callNext() {

		tenderDTO.setTenderQueueNo(queueManagementService.callNextQueueNumberAction("01", "01"));

		if (tenderDTO.getTenderQueueNo() != null) {
			search();
			disabledCallNext = true;
			disabledSkip = false;
			disableApplicationNo = true;
		} else {
			setErrorMessage("Queue numbers not found");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void save() {
		String logedUser = sessionBackingBean.getLoginUser();

		if (tenderDTO.getTitleCode() != null && !tenderDTO.getTitleCode().trim().equalsIgnoreCase("")) {

			if (tenderDTO.getApplicantName() != null && !tenderDTO.getApplicantName().trim().equalsIgnoreCase("")) {

				if (tenderDTO.getAddressOne() != null && !tenderDTO.getAddressOne().trim().equalsIgnoreCase("")) {

					if (tenderDTO.getAddressTwo() != null && !tenderDTO.getAddressTwo().trim().equalsIgnoreCase("")) {

						if (tenderDTO.getCity() != null && !tenderDTO.getCity().trim().equalsIgnoreCase("")) {

							if (tenderDTO.getNicNo() != null && !tenderDTO.getNicNo().trim().equalsIgnoreCase("")) {

								if (tenderDTO.getContactNo() != null
										&& !tenderDTO.getContactNo().trim().equalsIgnoreCase("")) {

									String applicationNo = issueTenderService.generateApplicationNo();

									boolean isSaveApplicantData = issueTenderService.saveIssuteTenderDetails(tenderDTO,
											applicationNo, logedUser,tenderDTO.getTenderQueueNo()); 

									if (isSaveApplicantData == true) {
										setSuccessMessage("Issue Tender Application Success.");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
										RequestContext.getCurrentInstance().update("tenderAppNo");

										commonService.updateTaskStatusCompleted(tenderDTO.getTenderAppNo(), "TD006",
												sessionBackingBean.getLoginUser());

										tenderDTO.setTenderAppNo(applicationNo);
										disabledGenerateVoucher = false;
										disabledViewTender = false;
										disabledCancel = true;
										sessionBackingBean.setIssueTenderMood(true);

										/*
										 * update task details. Can not update task TD005 as ongoing, Because
										 * application no is empty
										 */
										boolean isTasKTD006Available = issueTenderService.checkTaskDetails(tenderDTO,
												"TD006", "C");

										if (isTasKTD006Available == false) {
											issueTenderService.insertTaskDetails(tenderDTO, logedUser, "TD006", "C");

										} else {

										}

									} else {
										setErrorMessage("Issue Tender Application Not Success.");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}

								} else {
									setErrorMessage("Please Enter the Contact No.");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}

							} else {
								setErrorMessage("Please Enter the NIC No.");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							setErrorMessage("Please Enter the City");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Please Enter the Address 02.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please Enter the Address 01.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please Enter the Applicant Name.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select the Title");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void generateVoucher() {
		CreatePaymentVoucherBckingBean c = new CreatePaymentVoucherBckingBean();

		try {

			sessionBackingBean.setApplicationNo(tenderDTO.getTenderAppNo());

			sessionBackingBean.setTransactionDescription("TENDER");
			sessionBackingBean.setPermitNo(null);

			paymentVoucherDTO = new PaymentVoucherDTO();
			paymentVoucherDTO.setApplicationNo(tenderDTO.getTenderAppNo());
			paymentVoucherDTO.setTransactionDescription("TENDER");

			RequestContext.getCurrentInstance().update("applicationNo");
			RequestContext.getCurrentInstance().update("frmGenerateVoucher");
			RequestContext.getCurrentInstance().execute("PF('generateVoucher').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void viewTender() {

		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {

			tenderDetailList = new ArrayList<>();
			tenderDetailList = issueTenderService.getDetails_tender_details(tenderDTO.getTenderRefNo());

			RequestContext.getCurrentInstance().execute("PF('tenderDetails').show()");

		} else {
			setErrorMessage("No Data Found For Selected Tender");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public StreamedContent issueTenderDoc() throws JRException {
		files = null;
		String sourceFileName = null;
		String logopath = "//lk//informatics//ntc//view//reports//";
		Connection conn = null;
		String tenderRef = issueTenderService.getTenderRefNo(tenderDTO);
		String tenderApp = tenderDTO.getTenderAppNo();
		String title = issueTenderService.getTitlefromcode(tenderDTO.getTitleCode());
		String name = tenderDTO.getApplicantName();
		String idNo = tenderDTO.getNicNo();
		String contactNo = tenderDTO.getContactNo();
		String appReceivedPerson = tenderDTO.getApplicationRevPerson();
		String receivedTypeIdtype = issueTenderService.getreceivefromcode(tenderDTO.getRecPersonIdTypeDescription());
		String receivedTypeIdnum = tenderDTO.getRecPersonIdNo();

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//TenderApplication.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("LOGO_PATH", logopath);
			parameters.put("P_TENDER_APP", tenderApp);
			parameters.put("reference_No", tenderRef);
			parameters.put("applicant_Name", name);
			parameters.put("title", title);
			parameters.put("idNo", idNo);
			parameters.put("contactNo", contactNo);
			parameters.put("receivedPerson", appReceivedPerson);
			parameters.put("receivedIdType", receivedTypeIdtype);
			parameters.put("receivedIdNumber", receivedTypeIdnum);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Refund Voucher.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");
			disabledReIssueTender = false;
			disabledIssueTender = true;
			disabledIssueTenderEnv = false;

		} catch (JRException e) {
			e.printStackTrace();
		} finally {

			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return files;
	}

	public StreamedContent reIssueTenderDoc() throws JRException {

		files = null;
		String sourceFileName = null;
		String logopath = "//lk//informatics//ntc//view//reports//";
		Connection conn = null;
		String tenderRef = issueTenderService.getTenderRefNo(tenderDTO);
		String tenderApp = tenderDTO.getTenderAppNo();
		String title = issueTenderService.getTitlefromcode(tenderDTO.getTitleCode());
		String name = tenderDTO.getApplicantName();
		String idNo = tenderDTO.getNicNo();
		String contactNo = tenderDTO.getContactNo();
		String appReceivedPerson = tenderDTO.getApplicationRevPerson();
		String receivedTypeIdtype = issueTenderService.getreceivefromcode(tenderDTO.getRecPersonIdTypeDescription());
		String receivedTypeIdnum = tenderDTO.getRecPersonIdNo();

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//TenderApplication.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("LOGO_PATH", logopath);
			parameters.put("P_TENDER_APP", tenderApp);
			parameters.put("reference_No", tenderRef);
			parameters.put("applicant_Name", name);
			parameters.put("title", title);
			parameters.put("idNo", idNo);
			parameters.put("contactNo", contactNo);
			parameters.put("receivedPerson", appReceivedPerson);
			parameters.put("receivedIdType", receivedTypeIdtype);
			parameters.put("receivedIdNumber", receivedTypeIdnum);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Refund Voucher.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");
			disabledReIssueTender = false;

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return files;

	}

	public void issueTenderEnvelop() {

	}

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
	}

	public List<TenderDTO> getGetTenderApplicationTypeList() {
		return getTenderApplicationTypeList;
	}

	public void setGetTenderApplicationTypeList(List<TenderDTO> getTenderApplicationTypeList) {
		this.getTenderApplicationTypeList = getTenderApplicationTypeList;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public List<TenderDTO> getGetTenderRefNoList() {
		return getTenderRefNoList;
	}

	public void setGetTenderRefNoList(List<TenderDTO> getTenderRefNoList) {
		this.getTenderRefNoList = getTenderRefNoList;
	}

	public List<TenderDTO> getGetTenderAppNoList() {
		return getTenderAppNoList;
	}

	public void setGetTenderAppNoList(List<TenderDTO> getTenderAppNoList) {
		this.getTenderAppNoList = getTenderAppNoList;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public boolean isDisableApplicationNo() {
		return disableApplicationNo;
	}

	public void setDisableApplicationNo(boolean disableApplicationNo) {
		this.disableApplicationNo = disableApplicationNo;
	}

	public boolean isDisableTenderRefNo() {
		return disableTenderRefNo;
	}

	public TenderDTO getFilledTenderDTO() {
		return filledTenderDTO;
	}

	public void setFilledTenderDTO(TenderDTO filledTenderDTO) {
		this.filledTenderDTO = filledTenderDTO;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public void setDisableTenderRefNo(boolean disableTenderRefNo) {
		this.disableTenderRefNo = disableTenderRefNo;
	}

	public boolean isDisableApplicationType() {
		return disableApplicationType;
	}

	public void setDisableApplicationType(boolean disableApplicationType) {
		this.disableApplicationType = disableApplicationType;
	}

	public List<TenderDTO> getGetTitleList() {
		return getTitleList;
	}

	public void setGetTitleList(List<TenderDTO> getTitleList) {
		this.getTitleList = getTitleList;
	}

	public List<TenderDTO> getGetIdTypeList() {
		return getIdTypeList;
	}

	public void setGetIdTypeList(List<TenderDTO> getIdTypeList) {
		this.getIdTypeList = getIdTypeList;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public TenderDTO getTenderDetailsDTO() {
		return tenderDetailsDTO;
	}

	public void setTenderDetailsDTO(TenderDTO tenderDetailsDTO) {
		this.tenderDetailsDTO = tenderDetailsDTO;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public TenderService getIssueTenderService() {
		return issueTenderService;
	}

	public List<TenderDTO> getApplicationDetails() {
		return applicationDetails;
	}

	public void setApplicationDetails(List<TenderDTO> applicationDetails) {
		this.applicationDetails = applicationDetails;
	}

	public boolean isDisabledIssueTenderEnv() {
		return disabledIssueTenderEnv;
	}

	public void setDisabledIssueTenderEnv(boolean disabledIssueTenderEnv) {
		this.disabledIssueTenderEnv = disabledIssueTenderEnv;
	}

	public boolean isDisabledReIssueTender() {
		return disabledReIssueTender;
	}

	public void setDisabledReIssueTender(boolean disabledReIssueTender) {
		this.disabledReIssueTender = disabledReIssueTender;
	}

	public boolean isDisabledIssueTender() {
		return disabledIssueTender;
	}

	public void setDisabledIssueTender(boolean disabledIssueTender) {
		this.disabledIssueTender = disabledIssueTender;
	}

	public void setIssueTenderService(TenderService issueTenderService) {
		this.issueTenderService = issueTenderService;
	}

	public boolean isDisabledCallNext() {
		return disabledCallNext;
	}

	public void setDisabledCallNext(boolean disabledCallNext) {
		this.disabledCallNext = disabledCallNext;
	}

	public boolean isDisabledCancel() {
		return disabledCancel;
	}

	public void setDisabledCancel(boolean disabledCancel) {
		this.disabledCancel = disabledCancel;
	}

	public boolean isDisabledSkip() {
		return disabledSkip;
	}

	public void setDisabledSkip(boolean disabledSkip) {
		this.disabledSkip = disabledSkip;
	}

	public boolean isDisabledViewTender() {
		return disabledViewTender;
	}

	public void setDisabledViewTender(boolean disabledViewTender) {
		this.disabledViewTender = disabledViewTender;
	}

	public boolean isDisabledGenerateVoucher() {
		return disabledGenerateVoucher;
	}

	public void setDisabledGenerateVoucher(boolean disabledGenerateVoucher) {
		this.disabledGenerateVoucher = disabledGenerateVoucher;
	}

	public boolean isDisabledSave() {
		return disabledSave;
	}

	public void setDisabledSave(boolean disabledSave) {
		this.disabledSave = disabledSave;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public List<TenderDTO> getTenderDetailList() {
		return tenderDetailList;
	}

	public void setTenderDetailList(List<TenderDTO> tenderDetailList) {
		this.tenderDetailList = tenderDetailList;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

}
