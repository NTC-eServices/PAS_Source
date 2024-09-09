package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import lk.informatics.ntc.model.dto.SubSidyDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.SubSidyManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "subSidyPaymentUpdateBean")
@ViewScoped
public class SubSidyPaymentUpdateBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	public List<SubSidyDTO> getServiceTypeList = new ArrayList<SubSidyDTO>(0);
	public List<SubSidyDTO> getVoucherNoList = new ArrayList<SubSidyDTO>(0);
	private List<SubSidyDTO> paymentUpdateList;
	private List<SubSidyDTO> voucherSeqList;
	private List<SubSidyDTO> showSubsidyLogSheetData;
	public boolean disabledPaymentUpdate, renderViewList;
	private String errorMessage, successMessage, alertMSG;
	private SubSidyManagementService subSidyManagementService;
	private SubSidyDTO subSidyDTO, selectDTO, viewSelect;
	private boolean disabledVoucherNo, disabledStartDate, disabledEndDate;
	private boolean isSearch, createMode, viewMode;
	private CommonService commonService;

	@PostConstruct
	public void init() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		subSidyManagementService = (SubSidyManagementService) SpringApplicationContex
				.getBean("subSidyManagementService");
		subSidyDTO = new SubSidyDTO();
		loadValues();

		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN114", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN114", "V");
	}

	public void loadValues() {
		viewSelect = new SubSidyDTO();
		getServiceTypeList = subSidyManagementService.getServiceType();
		disabledEndDate = true;
		disabledStartDate = true;
		disabledVoucherNo = true;

		paymentUpdateList = new ArrayList<>();
		paymentUpdateList = subSidyManagementService.getDefaultPaymentListForUpdating();
	}

	public void ajaxselectType() {
		disabledEndDate = false;
		disabledStartDate = false;
		disabledVoucherNo = false;
		subSidyDTO.setVoucherStartDate(null);
		subSidyDTO.setVoucherEndDate(null);
		subSidyDTO.setVoucherNo(null);

		getVoucherNoList = new ArrayList<SubSidyDTO>(0);
		getVoucherNoList = subSidyManagementService.getApprovedVoucherNo(subSidyDTO.getServiceCode());
	}

	public void ajaxDateSelect() {
		disabledVoucherNo = true;
	}

	public void ajaxVoucherSelect() {
		disabledEndDate = true;
		disabledStartDate = true;
	}

	public void selectRow() {

	}

	public void search() {

		if (subSidyDTO.getServiceCode() != null && !subSidyDTO.getServiceCode().trim().equalsIgnoreCase("")) {

			if ((subSidyDTO.getVoucherNo() == null || subSidyDTO.getVoucherNo().trim().equalsIgnoreCase(""))
					&& (subSidyDTO.getVoucherStartDate() == null) && (subSidyDTO.getVoucherEndDate() == null)) {

				setErrorMessage("Please Select Voucher No. / Date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			} else {

				paymentUpdateList = new ArrayList<>();
				paymentUpdateList = subSidyManagementService.getPaymentListForUpdating(subSidyDTO);

				isSearch = true;
			}

		} else {
			setErrorMessage("Please Select Service Type");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearOne() {

		disabledEndDate = true;
		disabledStartDate = true;
		disabledVoucherNo = true;
		subSidyDTO = new SubSidyDTO();
		isSearch = false;
		paymentUpdateList = new ArrayList<>();
		paymentUpdateList = subSidyManagementService.getDefaultPaymentListForUpdating();

		showSubsidyLogSheetData = new ArrayList<>();
		renderViewList = false;
	}

	public void ajaxUpdatePayment() {

		if (createMode) {
			RequestContext.getCurrentInstance().execute("PF('confirmMSG').show()");
			voucherSeqList = new ArrayList<>();
			voucherSeqList = subSidyManagementService.getLogSheetSeq(selectDTO.getVoucherNo());
		}
	}

	public void updatePayment() {
		RequestContext.getCurrentInstance().execute("PF('confirmMSG').hide()");

		if (subSidyDTO.getEnteredPaymentRefNo() != null
				&& !subSidyDTO.getEnteredPaymentRefNo().trim().equalsIgnoreCase("")) {

			if (subSidyManagementService.isFoundReferenceNo(subSidyDTO.getEnteredPaymentRefNo()) == false) {

				if (subSidyManagementService.updateReferenceNo(selectDTO.getVoucherNo(),
						subSidyDTO.getEnteredPaymentRefNo(), sessionBackingBean.getLoginUser()) == true) {

					boolean success = subSidyManagementService.updatePaymentDetails(sessionBackingBean.getLoginUser(),
							this.voucherSeqList, selectDTO.getVoucherNo());

					if(success) {
						setSuccessMessage("Payment Reference No. Update Successfull");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					}else {
						setErrorMessage("Failed to update.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						RequestContext.getCurrentInstance().update("frmError");
					}
					

					if (isSearch == true) {
						paymentUpdateList = new ArrayList<>();
						paymentUpdateList = subSidyManagementService.getPaymentListForUpdating(subSidyDTO);

					} else {
						subSidyDTO.setVoucherNo(selectDTO.getVoucherNo());
						paymentUpdateList = new ArrayList<>();
						paymentUpdateList = subSidyManagementService.getPaymentListForUpdating(subSidyDTO);
					}

				} else {
					setErrorMessage("Update Fail");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					RequestContext.getCurrentInstance().update("frmError");
				}

			} else {
				setErrorMessage("Entered Reference No. Already Found");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				RequestContext.getCurrentInstance().update("frmError");
			}

		} else {
			setErrorMessage("Please enter payment reference no.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("frmError");
		}

	}

	public void view() {
		renderViewList = true;
		showSubsidyLogSheetData = new ArrayList<>();
		showSubsidyLogSheetData = subSidyManagementService.showLogSheetDet(viewSelect.getVoucherNo(),
				viewSelect.getBulkSeq());

	}

	public boolean isDisabledPaymentUpdate() {
		return disabledPaymentUpdate;
	}

	public void setDisabledPaymentUpdate(boolean disabledPaymentUpdate) {
		this.disabledPaymentUpdate = disabledPaymentUpdate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public SubSidyManagementService getSubSidyManagementService() {
		return subSidyManagementService;
	}

	public void setSubSidyManagementService(SubSidyManagementService subSidyManagementService) {
		this.subSidyManagementService = subSidyManagementService;
	}

	public List<SubSidyDTO> getGetServiceTypeList() {
		return getServiceTypeList;
	}

	public void setGetServiceTypeList(List<SubSidyDTO> getServiceTypeList) {
		this.getServiceTypeList = getServiceTypeList;
	}

	public List<SubSidyDTO> getGetVoucherNoList() {
		return getVoucherNoList;
	}

	public void setGetVoucherNoList(List<SubSidyDTO> getVoucherNoList) {
		this.getVoucherNoList = getVoucherNoList;
	}

	public List<SubSidyDTO> getPaymentUpdateList() {
		return paymentUpdateList;
	}

	public void setPaymentUpdateList(List<SubSidyDTO> paymentUpdateList) {
		this.paymentUpdateList = paymentUpdateList;
	}

	public SubSidyDTO getSubSidyDTO() {
		return subSidyDTO;
	}

	public void setSubSidyDTO(SubSidyDTO subSidyDTO) {
		this.subSidyDTO = subSidyDTO;
	}

	public SubSidyDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(SubSidyDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public boolean isDisabledVoucherNo() {
		return disabledVoucherNo;
	}

	public void setDisabledVoucherNo(boolean disabledVoucherNo) {
		this.disabledVoucherNo = disabledVoucherNo;
	}

	public boolean isDisabledStartDate() {
		return disabledStartDate;
	}

	public void setDisabledStartDate(boolean disabledStartDate) {
		this.disabledStartDate = disabledStartDate;
	}

	public boolean isDisabledEndDate() {
		return disabledEndDate;
	}

	public void setDisabledEndDate(boolean disabledEndDate) {
		this.disabledEndDate = disabledEndDate;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	public List<SubSidyDTO> getVoucherSeqList() {
		return voucherSeqList;
	}

	public void setVoucherSeqList(List<SubSidyDTO> voucherSeqList) {
		this.voucherSeqList = voucherSeqList;
	}

	public SubSidyDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(SubSidyDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public List<SubSidyDTO> getShowSubsidyLogSheetData() {
		return showSubsidyLogSheetData;
	}

	public void setShowSubsidyLogSheetData(List<SubSidyDTO> showSubsidyLogSheetData) {
		this.showSubsidyLogSheetData = showSubsidyLogSheetData;
	}

	public boolean isRenderViewList() {
		return renderViewList;
	}

	public void setRenderViewList(boolean renderViewList) {
		this.renderViewList = renderViewList;
	}

}
