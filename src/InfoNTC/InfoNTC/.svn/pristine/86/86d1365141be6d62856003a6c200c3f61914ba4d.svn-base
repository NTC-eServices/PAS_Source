package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.LogSheetDTO;
import lk.informatics.ntc.model.dto.NisiSeriyaDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.NisiSeriyaService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "nsIssueLogSheetBackingBean")
@ViewScoped
public class NisiSeriyaIssueServiceAgreementPermitStikerLogSheetBean implements Serializable {

	private static final long serialVersionUID = 1L;

	NisiSeriyaDTO nisiSeriyaDTO = new NisiSeriyaDTO();
	NisiSeriyaDTO tblSelectedDTO = new NisiSeriyaDTO();

	private List<NisiSeriyaDTO> drpdRequestNoList, drpdServiceRefNoList, drpdServiceNoList;

	private boolean disableIssueServiceAgreement, disableIssuePermitSticker, disableIssueLogSheets;
	private List<NisiSeriyaDTO> tblGamiIssueLogSheetList;

	private NisiSeriyaService nisiSeriyaService;

	private SisuSeriyaDTO sisuSeriyaDTO, selectDTO, viewDTO;
	private boolean disabledIssueServiceAgreement, disabledIssuePermitSticker, disabledIssueLogSheets;
	private String rejectReason, alertMSG, successMessage, errorMessage, loginUser;
	private SisuSariyaService sisuSariyaService;
	private CommonService commonService;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private boolean isSearch, serviceAgreementMode, permitStickerMode, logSheetMode, createMode, viewMode,
			renderLogSheet, renderPermitSticker, renderServiceAgreement;
	private String logSheetYear;
	private int logSheetCopies;
	private LogSheetDTO logSheetDTO;
	private List<LogSheetDTO> logSheetList;
	private List<LogSheetDTO> newLogSheetList;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {

		nisiSeriyaService = (NisiSeriyaService) SpringApplicationContex.getBean("nisiSeriyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		drpdRequestNoList = new ArrayList<NisiSeriyaDTO>();
		drpdServiceRefNoList = new ArrayList<NisiSeriyaDTO>();
		drpdServiceNoList = new ArrayList<NisiSeriyaDTO>();
		tblGamiIssueLogSheetList = new ArrayList<NisiSeriyaDTO>();
		logSheetCopies = 0;

		renderLogSheet = true;
		renderPermitSticker = true;
		renderServiceAgreement = true;
		loadValues();
	}

	private void loadValues() {

		drpdRequestNoList = nisiSeriyaService.getDrpdRequestNoListForNsIssueLogSheets();
		drpdServiceRefNoList = nisiSeriyaService.getDrpdServiceRefNoListForNsIssueLogSheets();
		drpdServiceNoList = nisiSeriyaService.getDrpdServiceNoListForNsIssueLogSheets();
	}

	public void btnSearch() {

		if ((nisiSeriyaDTO.getServiceRefNo() == null || nisiSeriyaDTO.getServiceRefNo().trim().equalsIgnoreCase(""))
				&& (nisiSeriyaDTO.getRequestNo() == null || nisiSeriyaDTO.getRequestNo().trim().equalsIgnoreCase(""))
				&& (nisiSeriyaDTO.getServiceNo() == null || nisiSeriyaDTO.getServiceNo().trim().equalsIgnoreCase(""))
				&& (nisiSeriyaDTO.getRequestStartDate() == null) && (nisiSeriyaDTO.getRequestEndDate() == null)) {

			setErrorMessage("Please Select Atleast One Field");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {

			tblGamiIssueLogSheetList = new ArrayList<NisiSeriyaDTO>();
			tblGamiIssueLogSheetList = nisiSeriyaService.getTblGamiIssueLogSheetList(nisiSeriyaDTO);

			if (tblGamiIssueLogSheetList.isEmpty()) {
				tblGamiIssueLogSheetList = new ArrayList<NisiSeriyaDTO>();
				setErrorMessage("No Data Found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}

	}

	public void btnClearOne() {
		nisiSeriyaDTO = new NisiSeriyaDTO();
		tblGamiIssueLogSheetList = new ArrayList<NisiSeriyaDTO>();
	}

	public void btnIssueServiceAgreement() {

	}

	public void btnIssuePermitSticker() {

	}

	public void btnIssueLogSheets() {

	}

	public void btnClearTwo() {

	}

	public void onTblRawSelect() {

		if (tblSelectedDTO.getServiceNo() != null || !tblSelectedDTO.getServiceNo().trim().equalsIgnoreCase("")) {

			if (tblSelectedDTO.isIssuedServiceAgreement()) {
				disableIssueServiceAgreement = true;
			} else {
				disableIssueServiceAgreement = false;
			}

			if (tblSelectedDTO.isIssuedPermitSticker()) {
				disableIssuePermitSticker = true;
			} else {
				disableIssuePermitSticker = false;
			}

			if (tblSelectedDTO.isIssuedLogSheets()) {
				disableIssueLogSheets = true;
			} else {
				disableIssueLogSheets = false;
			}

		} else {

			disableIssueServiceAgreement = true;
			disableIssuePermitSticker = true;
			disableIssueLogSheets = true;

		}
	}

	// getters and setters

	public NisiSeriyaDTO getNisiSeriyaDTO() {
		return nisiSeriyaDTO;
	}

	public void setNisiSeriyaDTO(NisiSeriyaDTO nisiSeriyaDTO) {
		this.nisiSeriyaDTO = nisiSeriyaDTO;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<NisiSeriyaDTO> getDrpdRequestNoList() {
		return drpdRequestNoList;
	}

	public void setDrpdRequestNoList(List<NisiSeriyaDTO> drpdRequestNoList) {
		this.drpdRequestNoList = drpdRequestNoList;
	}

	public List<NisiSeriyaDTO> getDrpdServiceRefNoList() {
		return drpdServiceRefNoList;
	}

	public void setDrpdServiceRefNoList(List<NisiSeriyaDTO> drpdServiceRefNoList) {
		this.drpdServiceRefNoList = drpdServiceRefNoList;
	}

	public List<NisiSeriyaDTO> getDrpdServiceNoList() {
		return drpdServiceNoList;
	}

	public void setDrpdServiceNoList(List<NisiSeriyaDTO> drpdServiceNoList) {
		this.drpdServiceNoList = drpdServiceNoList;
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

	public List<NisiSeriyaDTO> getTblGamiIssueLogSheetList() {
		return tblGamiIssueLogSheetList;
	}

	public void setTblGamiIssueLogSheetList(List<NisiSeriyaDTO> tblGamiIssueLogSheetList) {
		this.tblGamiIssueLogSheetList = tblGamiIssueLogSheetList;
	}

	public NisiSeriyaService getNisiSeriyaService() {
		return nisiSeriyaService;
	}

	public void setNisiSeriyaService(NisiSeriyaService nisiSeriyaService) {
		this.nisiSeriyaService = nisiSeriyaService;
	}

	public boolean isRenderServiceAgreement() {
		return renderServiceAgreement;
	}

	public void setRenderServiceAgreement(boolean renderServiceAgreement) {
		this.renderServiceAgreement = renderServiceAgreement;
	}

	public boolean isRenderPermitSticker() {
		return renderPermitSticker;
	}

	public void setRenderPermitSticker(boolean renderPermitSticker) {
		this.renderPermitSticker = renderPermitSticker;
	}

	public boolean isRenderLogSheet() {
		return renderLogSheet;
	}

	public void setRenderLogSheet(boolean renderLogSheet) {
		this.renderLogSheet = renderLogSheet;
	}

	public boolean isDisableIssueServiceAgreement() {
		return disableIssueServiceAgreement;
	}

	public void setDisableIssueServiceAgreement(boolean disableIssueServiceAgreement) {
		this.disableIssueServiceAgreement = disableIssueServiceAgreement;
	}

	public boolean isDisableIssuePermitSticker() {
		return disableIssuePermitSticker;
	}

	public void setDisableIssuePermitSticker(boolean disableIssuePermitSticker) {
		this.disableIssuePermitSticker = disableIssuePermitSticker;
	}

	public boolean isDisableIssueLogSheets() {
		return disableIssueLogSheets;
	}

	public void setDisableIssueLogSheets(boolean disableIssueLogSheets) {
		this.disableIssueLogSheets = disableIssueLogSheets;
	}

	public NisiSeriyaDTO getTblSelectedDTO() {
		return tblSelectedDTO;
	}

	public void setTblSelectedDTO(NisiSeriyaDTO tblSelectedDTO) {
		this.tblSelectedDTO = tblSelectedDTO;
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

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
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

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	public boolean isServiceAgreementMode() {
		return serviceAgreementMode;
	}

	public void setServiceAgreementMode(boolean serviceAgreementMode) {
		this.serviceAgreementMode = serviceAgreementMode;
	}

	public boolean isPermitStickerMode() {
		return permitStickerMode;
	}

	public void setPermitStickerMode(boolean permitStickerMode) {
		this.permitStickerMode = permitStickerMode;
	}

	public boolean isLogSheetMode() {
		return logSheetMode;
	}

	public void setLogSheetMode(boolean logSheetMode) {
		this.logSheetMode = logSheetMode;
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

	public String getLogSheetYear() {
		return logSheetYear;
	}

	public void setLogSheetYear(String logSheetYear) {
		this.logSheetYear = logSheetYear;
	}

	public int getLogSheetCopies() {
		return logSheetCopies;
	}

	public void setLogSheetCopies(int logSheetCopies) {
		this.logSheetCopies = logSheetCopies;
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

	public List<LogSheetDTO> getNewLogSheetList() {
		return newLogSheetList;
	}

	public void setNewLogSheetList(List<LogSheetDTO> newLogSheetList) {
		this.newLogSheetList = newLogSheetList;
	}

}
