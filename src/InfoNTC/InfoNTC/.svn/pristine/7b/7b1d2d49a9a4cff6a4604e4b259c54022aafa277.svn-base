package lk.informatics.ntc.view.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "sisuSeriyaVoucherCreationBean")
@ViewScoped
public class SisuSeriyaVoucherCreation {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<SisuSeriyaDTO> getRequestNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> getServiceReferenceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private List<SisuSeriyaDTO> drpdOperatorDepoNameList = new ArrayList<SisuSeriyaDTO>(0);

	private List<SisuSeriyaDTO> getServiceNoList = new ArrayList<SisuSeriyaDTO>(0);
	private SisuSeriyaDTO sisuSeriyaDTO, selectDTO, viewDTO, fillServiceNo;
	private SisuSariyaService sisuSariyaService;
	private CommonService commonService;
	private boolean disabledCreateVoucher, disabledReject, viewMode, createMode, renderVoucherButton;
	private String rejectReason, alertMSG, successMessage, errorMessage;
	private List<SisuSeriyaDTO> sisuSeriyaList;
	
	@PostConstruct
	public void init() {

		if (sessionBackingBean.approveURLStatus) {
			loadValues();
		}
		if (!sessionBackingBean.approveURLStatus) {
			sessionBackingBean.setApproveURL(null);
			sisuSeriyaList = sessionBackingBean.getTempSisuSeriyaDataList();
			sessionBackingBean.setApproveURLStatus(true);
		}

		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN104", "V");
		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN104", "C");

		if (createMode == true) {
			renderVoucherButton = true;
			disabledReject = false;
		} else if (viewMode == true) {
			renderVoucherButton = false;
			disabledReject = true;
		}
	}

	public SisuSeriyaVoucherCreation() {
		sisuSeriyaDTO = new SisuSeriyaDTO();
		viewDTO = new SisuSeriyaDTO();
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
	}

	public void loadValues() {
		selectDTO = new SisuSeriyaDTO();
		getRequestNoList = sisuSariyaService.getVoucherPendingRequestNo();
		getRequestNoList = sisuSariyaService.getVoucherPendingAndVoucherCreatedRequestNo();
		getServiceReferenceNoList = sisuSariyaService.getVoucherPendingServiceRefNoList();
		getServiceNoList = sisuSariyaService.getVoucherPendingServiceNoList();
		drpdOperatorDepoNameList = sisuSariyaService.getOperatorDepoNameListForVoucherCreation();
		disabledCreateVoucher = true;
		disabledReject = true;
		sisuSeriyaList = sisuSariyaService.getDefaultSisuSariyaVoucherData();
	}

	public void fillServiceRefNo() {
		getServiceReferenceNoList = sisuSariyaService
				.getFilterdVoucherPendingServiceRefNoList(sisuSeriyaDTO.getRequestNo());
		drpdOperatorDepoNameList = sisuSariyaService.drpdOperatorDepoNameListByRequestNoServiceRefNo(sisuSeriyaDTO);
	}

	public void fillServiceNo() {
		fillServiceNo = sisuSariyaService.getVoucherPendingServiceNo(sisuSeriyaDTO.getServiceRefNo());
		sisuSeriyaDTO.setServiceNo(fillServiceNo.getServiceNo());
		drpdOperatorDepoNameList = sisuSariyaService.drpdOperatorDepoNameListByRequestNoServiceRefNo(sisuSeriyaDTO);
	}
	
	public void onRequestStartDateChange(SelectEvent event) throws ParseException {
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		String startDateValue = frm.format(event.getObject());

		sisuSeriyaDTO.setRequestStartDateString(startDateValue);
		if (sisuSeriyaDTO.getRequestStartDateString() != null && !sisuSeriyaDTO.getRequestStartDateString().isEmpty()
				&& !sisuSeriyaDTO.getRequestStartDateString().equalsIgnoreCase("")) {
			drpdOperatorDepoNameList = sisuSariyaService.drpdOperatorDepoNameList(sisuSeriyaDTO);
		}

	}

	public void onRequestEndDateChange(SelectEvent event) throws ParseException {
		String dateFormat = "yyyy/MM/dd";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		String endDateValue = frm.format(event.getObject());

		sisuSeriyaDTO.setRequestEndDateString(endDateValue);
		if (sisuSeriyaDTO.getRequestEndDateString() != null && !sisuSeriyaDTO.getRequestEndDateString().isEmpty()
				&& !sisuSeriyaDTO.getRequestEndDateString().equalsIgnoreCase("")) {
			drpdOperatorDepoNameList = sisuSariyaService.drpdOperatorDepoNameList(sisuSeriyaDTO);
		}
	}

	public void selectRow() {
		if (createMode == true) {
			renderVoucherButton = true;
			disabledReject = false;
			disabledCreateVoucher = false;
		} else if (viewMode == true) {
			renderVoucherButton = false;
			disabledReject = true;
		}
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
			sisuSeriyaList = new ArrayList<>();
			sisuSeriyaList = sisuSariyaService.getSisuSariyaVoucherData(null, sisuSeriyaDTO);

			if (sisuSeriyaList.isEmpty()) {
				setErrorMessage("No data found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}

	}

	public void clearOne() {
		sisuSeriyaDTO = new SisuSeriyaDTO();
		sisuSeriyaList = new ArrayList<>();
		sisuSeriyaList = sisuSariyaService.getDefaultSisuSariyaVoucherData();
		disabledCreateVoucher = true;
	}

	public void createVoucher() {
		try {
			sessionBackingBean.setServiceRefNo(selectDTO.getServiceRefNo());
			sessionBackingBean.setServiceNo(selectDTO.getServiceNo());
			sessionBackingBean.setRequestNo(selectDTO.getRequestNo());
			sessionBackingBean.setTransactionDescription("SISU SARIYA");
			sisuSeriyaDTO.setServiceNo(selectDTO.getServiceNo());
			sisuSeriyaDTO.setTransactionDescription("SISU SARIYA");

			RequestContext.getCurrentInstance().update("serviceNo");
			RequestContext.getCurrentInstance().update("frmGenerateVoucher");
			RequestContext.getCurrentInstance().execute("PF('generateVoucher').show()");
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public boolean isDisabledCreateVoucher() {
		return disabledCreateVoucher;
	}

	public void setDisabledCreateVoucher(boolean disabledCreateVoucher) {
		this.disabledCreateVoucher = disabledCreateVoucher;
	}

	public boolean isDisabledReject() {
		return disabledReject;
	}

	public void setDisabledReject(boolean disabledReject) {
		this.disabledReject = disabledReject;
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

	public SisuSeriyaDTO getViewDTO() {
		return viewDTO;
	}

	public void setViewDTO(SisuSeriyaDTO viewDTO) {
		this.viewDTO = viewDTO;
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

	public List<SisuSeriyaDTO> getSisuSeriyaList() {
		return sisuSeriyaList;
	}

	public void setSisuSeriyaList(List<SisuSeriyaDTO> sisuSeriyaList) {
		this.sisuSeriyaList = sisuSeriyaList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public SisuSeriyaDTO getFillServiceNo() {
		return fillServiceNo;
	}

	public void setFillServiceNo(SisuSeriyaDTO fillServiceNo) {
		this.fillServiceNo = fillServiceNo;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public boolean isRenderVoucherButton() {
		return renderVoucherButton;
	}

	public void setRenderVoucherButton(boolean renderVoucherButton) {
		this.renderVoucherButton = renderVoucherButton;
	}

	public List<SisuSeriyaDTO> getDrpdOperatorDepoNameList() {
		return drpdOperatorDepoNameList;
	}

	public void setDrpdOperatorDepoNameList(List<SisuSeriyaDTO> drpdOperatorDepoNameList) {
		this.drpdOperatorDepoNameList = drpdOperatorDepoNameList;
	}

}
