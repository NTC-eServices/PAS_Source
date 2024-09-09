package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.LogSheetDTO;
import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.dto.SubSidyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "sisuSariyaBulkPaymentBackingBean")
@ViewScoped

public class SisuSariyaBulkPaymentBackingBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<SisuSeriyaDTO> getRequestNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getServiceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getServiceTypeList = new ArrayList<SisuSeriyaDTO>();
	private SisuSeriyaDTO sisuSeriyaDTO, selectDTO, viewDTO;
	private SubSidyDTO subSidyDTO;
	private boolean disabledIssueServiceAgreement, disabledIssuePermitSticker, disabledIssueLogSheets;
	private String rejectReason, alertMSG, successMessage, errorMessage, loginUser;
	private SisuSariyaService sisuSariyaService;
	private CommonService commonService;
	private List<LogSheetMaintenanceDTO> approvedLogSheetList;
	private List<LogSheetMaintenanceDTO> selectedLogSheetList;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private boolean isSearch, createMode, viewMode, disabledMode;
	private String logSheetYear;
	private String year;
	private int logSheetCopies;
	private LogSheetDTO logSheetDTO;
	private List<LogSheetDTO> logSheetList;
	private List<LogSheetDTO> newLogSheetList;

	@PostConstruct
	public void init() {

		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		sisuSeriyaDTO = new SisuSeriyaDTO();

		loginUser = sessionBackingBean.getLoginUser();

		loadValues();

		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN112", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN112", "V");
		disabledMode=false;
	
	}

	private void loadValues() {

		getRequestNoList = sisuSariyaService.getApprovedRequestNo();
		getServiceReferenceNoList = sisuSariyaService.getApprovedServiceRefNoList();
		getServiceNoList = sisuSariyaService.getApprovedServiceNoList();
		getServiceTypeList = sisuSariyaService.serviceTypeDropDown();
		selectDTO = new SisuSeriyaDTO();
		viewDTO = new SisuSeriyaDTO();
		subSidyDTO = new SubSidyDTO();
		disabledIssueServiceAgreement = true;
		disabledIssuePermitSticker = true;
		disabledIssueLogSheets = true;
		logSheetList = new ArrayList<>();
		newLogSheetList = new ArrayList<>();
		selectedLogSheetList = new ArrayList<>();
		//approvedLogSheetList = sisuSariyaService.getSisuSeriyaForBulkPayment();
		logSheetCopies = 0;
		logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		subSidyDTO.setHoldingPercentage(new BigDecimal(0));

	}

	public void search() {

		if ((sisuSeriyaDTO.getServiceTypeCode() == null
				|| sisuSeriyaDTO.getServiceTypeCode().trim().equalsIgnoreCase(""))
				&& (sisuSeriyaDTO.getRequestStartDate() == null) && (sisuSeriyaDTO.getRequestEndDate() == null)) {

			setErrorMessage("Please Select Atleast One Field");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}else if(sisuSeriyaDTO.getServiceTypeCode() == null || sisuSeriyaDTO.getServiceTypeCode().trim().equalsIgnoreCase("")){
			
			setErrorMessage("Please Select Service Type");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			
		}else if(year == null || year.trim().equalsIgnoreCase("")) {
			
			setErrorMessage("Please Enter Year");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			
		}
		else {
			// need to change request date data

			approvedLogSheetList = new ArrayList<>();
			approvedLogSheetList = sisuSariyaService.getSisuSeriyaForBulkPaymentBySearch(sisuSeriyaDTO, year);

			if (approvedLogSheetList.isEmpty()) {
				approvedLogSheetList = new ArrayList<>();
				setErrorMessage("No Data Found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			} else {

				isSearch = true;
			}
		}

	}

	public void yearValidator() {

		if (year != null && !year.isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{4}$");
			boolean valid = ptr.matcher(year).matches();
			if (valid) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy");
				Date date = new Date();
				String year = dateFormat.format(date);
				int curYear = Integer.parseInt(year);

				int manuYear = Integer.parseInt(year);

			} else {
				setErrorMessage("Invalid Year");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
			}

		}

	}
	public void selectRow() {
		int totalAmount = 0;
		BigDecimal voucherAmount = null;

		/*
		 * commonService.updateSurveyTaskDetails(selectDTO.getServiceRefNo(), "SS005",
		 * "SS006", "C", sessionBackingBean.loginUser);
		 */

		if (!selectedLogSheetList.isEmpty()) {
			
			subSidyDTO.setTotalNoOfLogs(selectedLogSheetList.size());

			for (LogSheetMaintenanceDTO temp : selectedLogSheetList) {
				totalAmount = totalAmount + temp.getPayment();

				if (subSidyDTO.getHoldingPercentage() != null
						&& subSidyDTO.getHoldingPercentage().compareTo(BigDecimal.ZERO) > 0) {

					BigDecimal bigAmount = BigDecimal.valueOf(temp.getPayment());
					BigDecimal ONE_HUNDRED = new BigDecimal(100);

					temp.setVoucherAmount(bigAmount
							.subtract(bigAmount.multiply(subSidyDTO.getHoldingPercentage()).divide(ONE_HUNDRED)));

				} else {
					BigDecimal bigAmount = BigDecimal.valueOf(temp.getPayment());
					temp.setVoucherAmount(bigAmount);

				}

			}

			if (subSidyDTO.getHoldingPercentage() != null
					&& subSidyDTO.getHoldingPercentage().compareTo(BigDecimal.ZERO) > 0) {

				BigDecimal bigTotalAmount = BigDecimal.valueOf(totalAmount);
				BigDecimal ONE_HUNDRED = new BigDecimal(100);

				voucherAmount = bigTotalAmount
						.subtract(bigTotalAmount.multiply(subSidyDTO.getHoldingPercentage()).divide(ONE_HUNDRED));

			} else {

				BigDecimal bigTotalAmount = BigDecimal.valueOf(totalAmount);
				voucherAmount = bigTotalAmount;

			}

			subSidyDTO.setTotalBalanceAmount(totalAmount);

			subSidyDTO.setVoucherAmount(voucherAmount);

		} else {

			subSidyDTO.setTotalNoOfLogs(selectedLogSheetList.size());

			subSidyDTO.setTotalBalanceAmount(totalAmount);

			subSidyDTO.setVoucherAmount(voucherAmount);

		}
	}

	public void clearOne() {

		sisuSeriyaDTO = new SisuSeriyaDTO();
		subSidyDTO = new SubSidyDTO();
		disabledIssuePermitSticker = true;
		disabledIssueServiceAgreement = true;
		disabledIssueLogSheets = true;
		approvedLogSheetList = new ArrayList<>();
		//approvedLogSheetList = sisuSariyaService.getSisuSeriyaForBulkPayment();
		subSidyDTO.setHoldingPercentage(new BigDecimal(0));
		year = null;
	}

	public void clearTwo() {

		disabledIssuePermitSticker = true;
		disabledIssueServiceAgreement = true;
		disabledIssueLogSheets = true;
		selectDTO = new SisuSeriyaDTO();
		subSidyDTO = new SubSidyDTO();
		approvedLogSheetList = new ArrayList<>();
	//	approvedLogSheetList = sisuSariyaService.getSisuSeriyaForBulkPayment();
		disabledMode=false;
		subSidyDTO.setHoldingPercentage(new BigDecimal(0));

	}

	public void generateVoucher() {
		String voucherNo;

		if (!selectedLogSheetList.isEmpty()) {

			if (subSidyDTO.getOwnerType() != null && !subSidyDTO.getOwnerType().isEmpty()
					&& !subSidyDTO.getOwnerType().equalsIgnoreCase("")) {

				if (subSidyDTO.getHoldingPercentage() != null) {
					
					subSidyDTO.setServiceType(sisuSeriyaDTO.getServiceTypeCode());
					voucherNo = sisuSariyaService.bulkPaymentGenerateVoucher(subSidyDTO, selectedLogSheetList,
							loginUser);
					
//					Added error message : dhananjika.d (24/06/2024)
					if(voucherNo != null) {
						subSidyDTO.setVoucherNo(voucherNo);

						setSuccessMessage("Voucher Generated");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						disabledMode=true;
					}else {
						setErrorMessage("An error occurred during the process");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						disabledMode=false;
					}
					

				} else {
					setErrorMessage("Please select Holding Percentage");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please select Owner Type");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
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

	public SisuSeriyaDTO getViewDTO() {
		return viewDTO;
	}

	public void setViewDTO(SisuSeriyaDTO viewDTO) {
		this.viewDTO = viewDTO;
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

	public DateFormat getDf() {
		return df;
	}

	public void setDf(DateFormat df) {
		this.df = df;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isDisabledIssueServiceAgreement() {
		return disabledIssueServiceAgreement;
	}

	public void setDisabledIssueServiceAgreement(boolean disabledIssueServiceAgreement) {
		this.disabledIssueServiceAgreement = disabledIssueServiceAgreement;
	}

	public boolean isDisabledIssuePermitSticker() {
		return disabledIssuePermitSticker;
	}

	public void setDisabledIssuePermitSticker(boolean disabledIssuePermitSticker) {
		this.disabledIssuePermitSticker = disabledIssuePermitSticker;
	}

	public boolean isDisabledIssueLogSheets() {
		return disabledIssueLogSheets;
	}

	public void setDisabledIssueLogSheets(boolean disabledIssueLogSheets) {
		this.disabledIssueLogSheets = disabledIssueLogSheets;
	}

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	public String getLogSheetYear() {
		return logSheetYear;
	}

	public void setLogSheetYear(String logSheetYear) {
		this.logSheetYear = logSheetYear;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public LogSheetDTO getLogSheetDTO() {
		return logSheetDTO;
	}

	public void setLogSheetDTO(LogSheetDTO logSheetDTO) {
		this.logSheetDTO = logSheetDTO;
	}

	public List<LogSheetDTO> getLogSheetList() {
		return logSheetList;
	}

	public void setLogSheetList(List<LogSheetDTO> logSheetList) {
		this.logSheetList = logSheetList;
	}

	public int getLogSheetCopies() {
		return logSheetCopies;
	}

	public void setLogSheetCopies(int logSheetCopies) {
		this.logSheetCopies = logSheetCopies;
	}

	public List<LogSheetDTO> getNewLogSheetList() {
		return newLogSheetList;
	}

	public void setNewLogSheetList(List<LogSheetDTO> newLogSheetList) {
		this.newLogSheetList = newLogSheetList;
	}

	public List<SisuSeriyaDTO> getGetServiceTypeList() {
		return getServiceTypeList;
	}

	public void setGetServiceTypeList(List<SisuSeriyaDTO> getServiceTypeList) {
		this.getServiceTypeList = getServiceTypeList;
	}

	public List<LogSheetMaintenanceDTO> getApprovedLogSheetList() {
		return approvedLogSheetList;
	}

	public void setApprovedLogSheetList(List<LogSheetMaintenanceDTO> approvedLogSheetList) {
		this.approvedLogSheetList = approvedLogSheetList;
	}

	public List<LogSheetMaintenanceDTO> getSelectedLogSheetList() {
		return selectedLogSheetList;
	}

	public void setSelectedLogSheetList(List<LogSheetMaintenanceDTO> selectedLogSheetList) {
		this.selectedLogSheetList = selectedLogSheetList;
	}

	public SubSidyDTO getSubSidyDTO() {
		return subSidyDTO;
	}

	public void setSubSidyDTO(SubSidyDTO subSidyDTO) {
		this.subSidyDTO = subSidyDTO;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isDisabledMode() {
		return disabledMode;
	}

	public void setDisabledMode(boolean disabledMode) {
		this.disabledMode = disabledMode;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	

}