package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.DateFormat;
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
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.SisuSariyaService;
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

@ManagedBean(name = "authorizedAndPrintingAgreementBean")
@ViewScoped
public class AuthorizedAndPrintingServiceAgreementBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<SisuSeriyaDTO> getRequestNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getServiceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>(0);
	private SisuSeriyaDTO sisuSeriyaDTO, selectDTO, viewDTO;
	private boolean disabledApprove, disabledCheckby, disabledRecommend, disabledReject, disabledClear,
			disabledPrintServiceAgreement, disabledServiceNo, disabledRefNo, disabledEndDate, disabledStartDate,
			disabledReqNo, serviceNoSearch, viewMode, edit = true, createMode, printMode;
	private String rejectReason, alertMSG, successMessage, errorMessage, serviesNo;
	private SisuSariyaService sisuSariyaService;
	private CommonService commonService;
	private List<SisuSeriyaDTO> sisuSeriyaList;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private boolean isSearch;
	private StreamedContent files;

	private Date serviceStartDateObj;
	private Date serviceEndDateObj;
	private String serviceReferenceNo;

	private SisuSeriyaDTO editServiceInfoDTO = new SisuSeriyaDTO();

	public AuthorizedAndPrintingServiceAgreementBackingBean() {
		sisuSeriyaDTO = new SisuSeriyaDTO();
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

	}

	public void loadValues() {
		getRequestNoList = sisuSariyaService.getRequestNo();
		getServiceNoList = sisuSariyaService.getServiceNoList();
		drpdOperatorDepoNameList = sisuSariyaService.getOperatorDepoNameDropDownForSisuApproval();
		selectDTO = new SisuSeriyaDTO();
		viewDTO = new SisuSeriyaDTO();
		sisuSeriyaList = new ArrayList<>();
		sisuSeriyaList = sisuSariyaService.getDefaultSisuSeriyaDataForSisuApproval("P");
	}

	@PostConstruct
	public void init() {

		disabledApprove = true;
		disabledCheckby = true;
		disabledRecommend = true;
		disabledPrintServiceAgreement = true;
		disabledReject = true;
		disabledClear = true;
		disabledRefNo = true;
		loadValues();

		if (!sessionBackingBean.approveURLStatus) {
			sessionBackingBean.setApproveURL(null);
			sisuSeriyaList = sessionBackingBean.getTempSisuSeriyaDataList();
			sessionBackingBean.setApproveURLStatus(true);
		}

		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN103", "V");
		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN103", "C");
		printMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN103", "P");

		if (createMode == true) {
			viewMode = true;
		}

	}

	public void selectRow() {
		if (createMode == true) {
			
			disabledCheckby = true;
			disabledRecommend = true;
			disabledApprove = true;
			disabledReject = true;
			disabledPrintServiceAgreement = true;
			
			if (selectDTO != null) {
				if (selectDTO.getIsChecked() != null) {
					disabledCheckby = true;
					if (selectDTO.getIsChecked().equals("Y")) {
						if (selectDTO.getIsRecommended() != null) {
							disabledRecommend = true;
							if (selectDTO.getIsRecommended().equals("Y")) {
								if (selectDTO.getStatus() != null) {
									if (selectDTO.getStatus().equals("A")) {
										// enable print if approved
										disabledApprove = true;
										disabledReject = true;
										disabledPrintServiceAgreement = false;
									} else if (selectDTO.getStatus().equals("R")) {
										// disable all if rejected
										disabledApprove = true;
										disabledReject = true;
										disabledPrintServiceAgreement = true;
									} else if (selectDTO.getStatus().equals("P")) {
										// enable approve and reject if pending
										disabledApprove = false;
										disabledReject = false;
										disabledPrintServiceAgreement = true;
									}
								}
							} else {
								disabledApprove = true;
								disabledReject = true;
							}
						} else {
							disabledRecommend = false;// enable recommend
							disabledReject = false;
						}
					} else {
						disabledRecommend = true;
						disabledReject = true;
					}
				} else {
					disabledCheckby = false;// enable check by
					disabledReject = false;
				}
			}
			disabledClear = false;

		} else if (printMode == true) {
			edit = false;
			disabledPrintServiceAgreement = false;
		} else if (viewMode == true) {
			edit = false;
			disabledApprove = true;
			disabledCheckby = true;
			disabledRecommend = true;
			disabledReject = true;
			disabledClear = true;
			disabledPrintServiceAgreement = true;
		}
	}

	public void ajaxServiceNoSearch() {
		disabledEndDate = true;
		disabledRefNo = true;
		disabledReqNo = true;
		disabledStartDate = true;
		serviceNoSearch = true;
		disabledApprove = true;
		disabledCheckby = true;
		disabledRecommend = true;
		disabledClear = true;
		disabledPrintServiceAgreement = true;
		disabledReject = true;
	}

	public void ajaxOtherNoSearch() {
		disabledServiceNo = true;
		serviceNoSearch = false;
		disabledApprove = true;
		disabledCheckby = true;
		disabledRecommend = true;
		disabledClear = true;
		disabledPrintServiceAgreement = true;
		disabledReject = true;
		getServiceReferenceNoList = sisuSariyaService.getServiceRefNoListForSisuApproval(sisuSeriyaDTO);
		disabledRefNo = false;
	}

	public void onRequestStartDateChange(SelectEvent event) throws ParseException {
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		String startDateValue = frm.format(event.getObject());

		sisuSeriyaDTO.setRequestStartDateString(startDateValue);
		if (sisuSeriyaDTO.getRequestStartDateString() != null && !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
				&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
			drpdOperatorDepoNameList = sisuSariyaService.getOperatorDepoNameDropDownForSisuApproval(sisuSeriyaDTO);
		}

		ajaxOtherNoSearch();
	}

	public void onRequestEndDateChange(SelectEvent event) throws ParseException {
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		String endDateValue = frm.format(event.getObject());

		sisuSeriyaDTO.setRequestEndDateString(endDateValue);
		if (sisuSeriyaDTO.getRequestEndDateString() != null && !sisuSeriyaDTO.getRequestEndDateString().isEmpty()
				&& !sisuSeriyaDTO.getRequestEndDateString().equalsIgnoreCase("")) {
			drpdOperatorDepoNameList = sisuSariyaService.getOperatorDepoNameDropDownForSisuApproval(sisuSeriyaDTO);
		}

		ajaxOtherNoSearch();
	}

	public void search() {

		if ((sisuSeriyaDTO.getServiceRefNo() == null || sisuSeriyaDTO.getServiceRefNo().trim().equalsIgnoreCase(""))
				&& (sisuSeriyaDTO.getRequestNo() == null || sisuSeriyaDTO.getRequestNo().trim().equalsIgnoreCase(""))
				&& (sisuSeriyaDTO.getServiceNo() == null || sisuSeriyaDTO.getServiceNo().trim().equalsIgnoreCase(""))
				&& (sisuSeriyaDTO.getRequestStartDate() == null) && (sisuSeriyaDTO.getRequestEndDate() == null)
				&& (sisuSeriyaDTO.getNameOfOperator() == null
						|| sisuSeriyaDTO.getNameOfOperator().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please select at least one field.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {

			if (serviceNoSearch == true) {

				sisuSeriyaList = new ArrayList<>();
				sisuSeriyaList = sisuSariyaService.getApprovedSisuSeriyaData(sisuSeriyaDTO);

				if (sisuSeriyaList.isEmpty()) {
					setErrorMessage("No data found.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					disabledClear = false;
					isSearch = true;
					disabledPrintServiceAgreement = false;
				}

			} else {

				sisuSeriyaList = new ArrayList<>();
				sisuSeriyaList = sisuSariyaService.getSisuSeriyaData(sisuSeriyaDTO);

				if (sisuSeriyaList.isEmpty()) {
					setErrorMessage("No data found.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					disabledClear = false;
					isSearch = true;
				}
			}

		}

	}

	public void editServiceStartEndDates(String serviceRefNum) {

		serviceReferenceNo = serviceRefNum;

		editServiceInfoDTO = sisuSariyaService.getServiceDates(serviceReferenceNo);
		setServiceStartDateObj(editServiceInfoDTO.getServiceStartDateObj());
		setServiceEndDateObj(editServiceInfoDTO.getServiceEndDateObj());
		RequestContext.getCurrentInstance().update("form");
		RequestContext.getCurrentInstance().execute("PF('wdv-update').show()");
		RequestContext.getCurrentInstance().update("frm-updateServiceStartEndDate");

	}

	public void updateServiceStartEndDate() {
		boolean updated = sisuSariyaService.updateServiceStartEndDateInApproval(serviceReferenceNo, serviceStartDateObj,
				serviceEndDateObj);
		if (updated) {
			sisuSeriyaList = sisuSariyaService.getSelectSisuSariyaData(serviceReferenceNo, null);
		}
	}

	public void checkby() {

		if (sisuSariyaService.isServiceDataEnterd(selectDTO) == true) {

			if (sisuSariyaService.isAlreadyChecked(selectDTO) == false) {

				if (sisuSariyaService.isAlreadyRejected(selectDTO) == false) {

					if (sisuSariyaService.checkSisuSariyaRequest(selectDTO,
							sessionBackingBean.getLoginUser()) == true) {

						setSuccessMessage("Selected record checked successfully.");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						disabledCheckby = true;
						disabledRecommend = false;
						disabledApprove = true;
						disabledPrintServiceAgreement = true;
						disabledReject = false;

						sisuSeriyaList = new ArrayList<>();
						sisuSeriyaList = sisuSariyaService.getSelectSisuSariyaData(selectDTO.getServiceRefNo(), null);

					} else {
						setErrorMessage("Sisu seriya authorization is failed.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Selected data already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected data already checked.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please update service start / end dates.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void recommend() {

		if (sisuSariyaService.isServiceDataEnterd(selectDTO) == true) {

			if (sisuSariyaService.isAlreadyRecomended(selectDTO) == false) {

				if (sisuSariyaService.isAlreadyRejected(selectDTO) == false) {

					if (sisuSariyaService.recommendSisuSariyaRequest(selectDTO,
							sessionBackingBean.getLoginUser()) == true) {

						setSuccessMessage("Selected record recommended successfully.");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						disabledCheckby = true;
						disabledRecommend = true;
						disabledApprove = false;
						disabledPrintServiceAgreement = true;
						disabledReject = false;

						sisuSeriyaList = new ArrayList<>();
						sisuSeriyaList = sisuSariyaService.getSelectSisuSariyaData(selectDTO.getServiceRefNo(), null);

					} else {
						setErrorMessage("Sisu seriya authorization is failed.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Selected data already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected data already recommended.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please update service start / end dates.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void approve() {

		if (sisuSariyaService.isServiceDataEnterd(selectDTO) == true) {

			if (sisuSariyaService.isAlreadyApproved(selectDTO) == false) {

				if (sisuSariyaService.isAlreadyRejected(selectDTO) == false) {

					serviesNo = sisuSariyaService.generateServiceNoN(sessionBackingBean.getLoginUser(), "SER");

					if (sisuSariyaService.approveSisuSeriyaRequest(selectDTO, sessionBackingBean.getLoginUser(),
							serviesNo) == true) {

						setSuccessMessage("Sisu sariya authorization is successfull.");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						disabledCheckby = true;
						disabledRecommend = true;
						disabledApprove = true;
						disabledPrintServiceAgreement = false;
						disabledReject = true;

						sisuSeriyaList = new ArrayList<>();
						sisuSeriyaList = sisuSariyaService.getSelectSisuSariyaData(selectDTO.getServiceRefNo(), null);

					} else {
						setErrorMessage("Sisu sariya authorization is failed.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Selected data already rejected.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected data already approved.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please update service start / end dates.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void reject() {
		if (sisuSariyaService.isAlreadyRejected(selectDTO) == false) {

			if (sisuSariyaService.isAlreadyApproved(selectDTO) == false) {

				if (selectDTO.getIsChecked() == null) {
					// check by stage
					rejectCheckby();
				} else if (selectDTO.getIsRecommended() == null) {
					// recommend stage
					rejectRecommend();
				} else if (selectDTO.getIsRecommended().equals("Y")) {
					// approve stage
					RequestContext.getCurrentInstance().execute("PF('rejectDialog').show()");
				}

			} else {
				setErrorMessage("Selected data already approved.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Selected data already rejected.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void rejectCheckby() {

		if (sisuSariyaService.rejectCheckBySisuSeriyaRequest(selectDTO, sessionBackingBean.getLoginUser())) {

			setSuccessMessage("Rejected successfully.");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			sisuSeriyaList = new ArrayList<>();
			sisuSeriyaList = sisuSariyaService.getDefaultSisuSeriyaDataForSisuApproval("P");

			disabledCheckby = true;
			disabledRecommend = true;
			disabledApprove = true;
			disabledClear = true;
			disabledPrintServiceAgreement = true;
			disabledReject = true;

		} else {
			setErrorMessage("Rejection failed.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void rejectRecommend() {

		if (sisuSariyaService.rejectRecommendSisuSeriyaRequest(selectDTO, sessionBackingBean.getLoginUser())) {

			setSuccessMessage("Rejected successfully.");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			sisuSeriyaList = new ArrayList<>();
			sisuSeriyaList = sisuSariyaService.getDefaultSisuSeriyaDataForSisuApproval("P");

			disabledCheckby = true;
			disabledRecommend = true;
			disabledApprove = true;
			disabledClear = true;
			disabledPrintServiceAgreement = true;
			disabledReject = true;

		} else {
			setErrorMessage("Rejection failed.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void rejectPermit() {
		if (rejectReason != null || !rejectReason.trim().equalsIgnoreCase("")) {

			if (sisuSariyaService.rejectSisuSeriyaRequest(selectDTO, sessionBackingBean.getLoginUser(), rejectReason)) {

				setSuccessMessage("Rejected successfully.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				sisuSeriyaList = new ArrayList<>();
				sisuSeriyaList = sisuSariyaService.getDefaultSisuSeriyaDataForSisuApproval("P");

				disabledCheckby = true;
				disabledRecommend = true;
				disabledApprove = true;
				disabledClear = true;
				disabledPrintServiceAgreement = true;
				disabledReject = true;

			} else {
				setErrorMessage("Rejection failed.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please enter the reject reason.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public StreamedContent printServiceAgreement() throws JRException {

		if (selectDTO != null) {
			String serviceNo = selectDTO.getServiceNo();
			files = null;
			String sourceFileName = null;

			Connection conn = null;

			try {
				conn = ConnectionManager.getConnection();

				sourceFileName = "..//reports//SisusariyaPrintServiceAggrement.jrxml";
				String logopath = "//lk//informatics//ntc//view//reports//";

				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("P_service_no", serviceNo);
				parameters.put("P_logo", logopath);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf",
						"Service Agreement Sisu Sariya Report_" + serviceNo + ".pdf");

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "pdf");

				sisuSariyaService.updateTaskCompleteSubsidyTaskTableForPrintAgreement(selectDTO.getRequestNo(),
						selectDTO.getServiceNo(), selectDTO.getServiceRefNo(), "SM004", "C",
						sessionBackingBean.getLoginUser());

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(conn);
			}

		} else {
			setErrorMessage("Please select a data raw.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}

		return files;

	}

	public void clearOne() {
		sisuSeriyaDTO = new SisuSeriyaDTO();
		disabledApprove = true;
		disabledCheckby = true;
		disabledRecommend = true;
		disabledClear = true;
		disabledPrintServiceAgreement = true;
		disabledReject = true;
		sisuSeriyaList = new ArrayList<>();
		sisuSeriyaList = sisuSariyaService.getDefaultSisuSeriyaDataForSisuApproval("P");
		getRequestNoList = sisuSariyaService.getRequestNo();
		getServiceReferenceNoList = sisuSariyaService.getServiceRefNoListForSisuApproval(sisuSeriyaDTO);
		getServiceNoList = sisuSariyaService.getServiceNoList();
		disabledReqNo = false;
		disabledRefNo = false;
		disabledStartDate = false;
		disabledEndDate = false;
		serviceNoSearch = false;
		disabledServiceNo = false;
		isSearch = false;
		selectDTO = new SisuSeriyaDTO();
	}

	public void clearTwo() {
		selectDTO = new SisuSeriyaDTO();
		disabledApprove = true;
		disabledCheckby = true;
		disabledRecommend = true;
		disabledClear = true;
		disabledPrintServiceAgreement = true;
		disabledReject = true;
		isSearch = false;
	}

	public String view() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		sessionBackingBean.setPageMode("V");
		sessionBackingBean.setApproveURL(request.getRequestURL().toString());
		sessionBackingBean.setSearchURL(null);
		sessionBackingBean.setApproveURLStatus(true);
		sessionBackingBean.setServiceNo(viewDTO.getServiceNo());
		sessionBackingBean.setServiceRefNo(viewDTO.getServiceRefNo());
		sessionBackingBean.setRequestNo(viewDTO.getRequestNo());
		sessionBackingBean.setTempSisuSeriyaDataList(sisuSeriyaList);
		sessionBackingBean.setSisuSariya(true);

		return "/pages/sisuSeriya/ssPermitHolderInformation.xhtml#!";

	}

	/* Getters and Setters */

	public boolean isDisabledApprove() {
		return disabledApprove;
	}

	public List<SisuSeriyaDTO> getGetRequestNoList() {
		return getRequestNoList;
	}

	public void setGetRequestNoList(List<SisuSeriyaDTO> getRequestNoList) {
		this.getRequestNoList = getRequestNoList;
	}

	public List<SisuSeriyaDTO> getGetServiceReferenceNoList() {
		return getServiceReferenceNoList;
	}

	public void setGetServiceReferenceNoList(List<SisuSeriyaDTO> getServiceReferenceNoList) {
		this.getServiceReferenceNoList = getServiceReferenceNoList;
	}

	public List<SisuSeriyaDTO> getGetServiceNoList() {
		return getServiceNoList;
	}

	public void setGetServiceNoList(List<SisuSeriyaDTO> getServiceNoList) {
		this.getServiceNoList = getServiceNoList;
	}

	public void setDisabledApprove(boolean disabledApprove) {
		this.disabledApprove = disabledApprove;
	}

	public boolean isDisabledReject() {
		return disabledReject;
	}

	public void setDisabledReject(boolean disabledReject) {
		this.disabledReject = disabledReject;
	}

	public boolean isDisabledClear() {
		return disabledClear;
	}

	public void setDisabledClear(boolean disabledClear) {
		this.disabledClear = disabledClear;
	}

	public boolean isDisabledPrintServiceAgreement() {
		return disabledPrintServiceAgreement;
	}

	public void setDisabledPrintServiceAgreement(boolean disabledPrintServiceAgreement) {
		this.disabledPrintServiceAgreement = disabledPrintServiceAgreement;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
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

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public SisuSeriyaDTO getSisuSeriyaDTO() {
		return sisuSeriyaDTO;
	}

	public void setSisuSeriyaDTO(SisuSeriyaDTO sisuSeriyaDTO) {
		this.sisuSeriyaDTO = sisuSeriyaDTO;
	}

	public SisuSeriyaDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(SisuSeriyaDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

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

	public List<SisuSeriyaDTO> getSisuSeriyaList() {
		return sisuSeriyaList;
	}

	public void setSisuSeriyaList(List<SisuSeriyaDTO> sisuSeriyaList) {
		this.sisuSeriyaList = sisuSeriyaList;
	}

	public SisuSeriyaDTO getViewDTO() {
		return viewDTO;
	}

	public void setViewDTO(SisuSeriyaDTO viewDTO) {
		this.viewDTO = viewDTO;
	}

	public boolean isDisabledServiceNo() {
		return disabledServiceNo;
	}

	public void setDisabledServiceNo(boolean disabledServiceNo) {
		this.disabledServiceNo = disabledServiceNo;
	}

	public boolean isDisabledRefNo() {
		return disabledRefNo;
	}

	public void setDisabledRefNo(boolean disabledRefNo) {
		this.disabledRefNo = disabledRefNo;
	}

	public boolean isDisabledEndDate() {
		return disabledEndDate;
	}

	public void setDisabledEndDate(boolean disabledEndDate) {
		this.disabledEndDate = disabledEndDate;
	}

	public boolean isDisabledStartDate() {
		return disabledStartDate;
	}

	public void setDisabledStartDate(boolean disabledStartDate) {
		this.disabledStartDate = disabledStartDate;
	}

	public boolean isDisabledReqNo() {
		return disabledReqNo;
	}

	public void setDisabledReqNo(boolean disabledReqNo) {
		this.disabledReqNo = disabledReqNo;
	}

	public String getServiesNo() {
		return serviesNo;
	}

	public void setServiesNo(String serviesNo) {
		this.serviesNo = serviesNo;
	}

	public DateFormat getDf() {
		return df;
	}

	public void setDf(DateFormat df) {
		this.df = df;
	}

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isServiceNoSearch() {
		return serviceNoSearch;
	}

	public void setServiceNoSearch(boolean serviceNoSearch) {
		this.serviceNoSearch = serviceNoSearch;
	}

	public Date getServiceStartDateObj() {
		return serviceStartDateObj;
	}

	public void setServiceStartDateObj(Date serviceStartDateObj) {
		this.serviceStartDateObj = serviceStartDateObj;
	}

	public Date getServiceEndDateObj() {
		return serviceEndDateObj;
	}

	public void setServiceEndDateObj(Date serviceEndDateObj) {
		this.serviceEndDateObj = serviceEndDateObj;
	}

	public String getServiceReferenceNo() {
		return serviceReferenceNo;
	}

	public void setServiceReferenceNo(String serviceReferenceNo) {
		this.serviceReferenceNo = serviceReferenceNo;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public boolean isPrintMode() {
		return printMode;
	}

	public void setPrintMode(boolean printMode) {
		this.printMode = printMode;
	}

	public List<SisuSeriyaDTO> getDrpdOperatorDepoNameList() {
		return drpdOperatorDepoNameList;
	}

	public void setDrpdOperatorDepoNameList(List<SisuSeriyaDTO> drpdOperatorDepoNameList) {
		this.drpdOperatorDepoNameList = drpdOperatorDepoNameList;
	}

	public boolean isDisabledCheckby() {
		return disabledCheckby;
	}

	public void setDisabledCheckby(boolean disabledCheckby) {
		this.disabledCheckby = disabledCheckby;
	}

	public boolean isDisabledRecommend() {
		return disabledRecommend;
	}

	public void setDisabledRecommend(boolean disabledRecommend) {
		this.disabledRecommend = disabledRecommend;
	}

}
