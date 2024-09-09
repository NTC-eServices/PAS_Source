package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.AmendmentBusOwnerDTO;
import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.ParamerDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.AmendmentService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "viewTimeApproval")
@ViewScoped
public class ViewTimeApprovalBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private AmendmentDTO amendmentDTO, selectDTO, filledamendmentDTO, viewSelectDTO, amendmentHistoryDTO;
	private PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
	private AmendmentService amendmentService;
	private List<AmendmentDTO> permitNoList = new ArrayList<AmendmentDTO>(0);
	private List<AmendmentDTO> applicationNoList = new ArrayList<AmendmentDTO>(0);
	private List<AmendmentDTO> amendmentsDetailList;
	private List<AmendmentDTO> transactionList = new ArrayList<AmendmentDTO>(0);
	private List<AmendmentDTO> transactionListforTimeApproval = new ArrayList<AmendmentDTO>(0);
	private String alertMSG, successMessage, errorMessage;
	private List<AmendmentDTO> ApplicationDocuments = new ArrayList<AmendmentDTO>(0);
	private List<AmendmentDTO> ApplicationVersionDocuments = new ArrayList<AmendmentDTO>(0);
	private CommonService commonService;
	private MigratedService migratedService;
	private IssuePermitService issuePermitService;
	private boolean disabledApplicationNo;
	private boolean disabledPermitNo;
	
	private StreamedContent files;
	private AdminService adminService;
	private HistoryService historyService;
	private PermitRenewalsDTO applicationHistoryDTO, ownerHistoryDTO;
	private OminiBusDTO ominiBusHistoryDTO;

	private AmendmentBusOwnerDTO routeDetDTO;

	public ViewTimeApprovalBackingBean() {
		amendmentDTO = new AmendmentDTO();
		selectDTO = new AmendmentDTO();
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		amendmentService = (AmendmentService) SpringApplicationContex.getBean("amendmentService");
		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");

		loadValues();
	}

	public void loadValues() {
		amendmentsDetailList = new ArrayList<>();
		amendmentsDetailList = amendmentService.getGrantApprovalDefaultDetails(amendmentDTO);
 
		transactionList = amendmentService.getTransactionTypeForTimeApproval();
		disabledApplicationNo = true;
		disabledPermitNo = true;
		
	
	}

	public void ajaxFillData() {

		amendmentDTO.setPermitNo(null);
		amendmentDTO.setNewBusNo(null);
		amendmentDTO.setExisitingBusNo(null);

		filledamendmentDTO = new AmendmentDTO();
		filledamendmentDTO = amendmentService.ajaxFillData(amendmentDTO);

		if (filledamendmentDTO.getApplicationNo() != null) {
			amendmentDTO.setApplicationNo(filledamendmentDTO.getApplicationNo());

			if (filledamendmentDTO.getPermitNo() != null) {
				amendmentDTO.setPermitNo(filledamendmentDTO.getPermitNo());

				if (filledamendmentDTO.getExisitingBusNo() != null) {
					amendmentDTO.setExisitingBusNo(filledamendmentDTO.getExisitingBusNo());

					if (filledamendmentDTO.getNewBusNo() != null) {
						amendmentDTO.setNewBusNo(filledamendmentDTO.getNewBusNo());
					}
				}
			}

		}

	}

	public void ajaxFilterApplicationNoANDPermitNo() {
		disabledApplicationNo = false;
		disabledPermitNo = false;
		permitNoList = amendmentService.getPermitNO(amendmentDTO);
		applicationNoList = amendmentService.getApplicationNO(amendmentDTO);

	}

	public void search() {

		if ((amendmentDTO.getApplicationNo() != null && !amendmentDTO.getApplicationNo().trim().equalsIgnoreCase(""))
				|| (amendmentDTO.getPermitNo() != null && !amendmentDTO.getPermitNo().trim().equalsIgnoreCase("")
						|| amendmentDTO.getTranCode() != null
								&& !amendmentDTO.getTranCode().trim().equalsIgnoreCase(""))) {

			amendmentsDetailList = new ArrayList<>();
			amendmentsDetailList = amendmentService.getGrantApprovalDetails(amendmentDTO);

			
		} else {
			setErrorMessage("Please Select the Application No. / Permit No. or Transaction Type");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearOne() {

		amendmentDTO = new AmendmentDTO();
		amendmentsDetailList = new ArrayList<>();
		
		amendmentsDetailList = new ArrayList<>();
		amendmentsDetailList = amendmentService.getGrantApprovalDefaultDetails(amendmentDTO);
		disabledApplicationNo = true;
		disabledPermitNo = true;
	}

	public void clearTwo() {
		amendmentsDetailList = new ArrayList<>();
		
	}

	public void selectRow() {

		if (!amendmentsDetailList.isEmpty()) {
			
			if (selectDTO.getApplicationNo() != null) {
				amendmentDTO.setApplicationNo(selectDTO.getApplicationNo());
			}
			
		}

	}

	public String getEmpId(String param) {

		String val = null;

		ParamerDTO paramDTO1 = migratedService.retrieveParameterValuesForParamName(param);
		val = paramDTO1.getStringValue();

		return val;

	}

	public String view() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		sessionBackingBean.setPageMode("V");
		sessionBackingBean.setApproveURL(request.getRequestURL().toString());
		sessionBackingBean.setSearchURL(null);
		sessionBackingBean.setApproveURLStatus(true);
		sessionBackingBean.setAmendmentsApplicationNo(viewSelectDTO.getApplicationNo());
		sessionBackingBean.amendmentsList = amendmentsDetailList;
		sessionBackingBean.amendmentsViewMood = true;

		return "/pages/amendmentsToBusOrOwner/viewBusOwnerServiceAmendment.xhtml#!";

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public AmendmentDTO getAmendmentDTO() {
		return amendmentDTO;
	}

	public void setAmendmentDTO(AmendmentDTO amendmentDTO) {
		this.amendmentDTO = amendmentDTO;
	}

	public AmendmentService getAmendmentService() {
		return amendmentService;
	}

	public void setAmendmentService(AmendmentService amendmentService) {
		this.amendmentService = amendmentService;
	}

	public List<AmendmentDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<AmendmentDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<AmendmentDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<AmendmentDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
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

	public AmendmentDTO getFilledamendmentDTO() {
		return filledamendmentDTO;
	}

	public void setFilledamendmentDTO(AmendmentDTO filledamendmentDTO) {
		this.filledamendmentDTO = filledamendmentDTO;
	}

	public AmendmentDTO getViewSelectDTO() {
		return viewSelectDTO;
	}

	public void setViewSelectDTO(AmendmentDTO viewSelectDTO) {
		this.viewSelectDTO = viewSelectDTO;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	
	public AmendmentDTO getSelectDTO() {
		return selectDTO;
	}

	public IssuePermitService getIssuePermitService() {
		return issuePermitService;
	}

	public void setIssuePermitService(IssuePermitService issuePermitService) {
		this.issuePermitService = issuePermitService;
	}

	public void setSelectDTO(AmendmentDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public List<AmendmentDTO> getAmendmentsDetailList() {
		return amendmentsDetailList;
	}

	public void setAmendmentsDetailList(List<AmendmentDTO> amendmentsDetailList) {
		this.amendmentsDetailList = amendmentsDetailList;
	}

	public List<AmendmentDTO> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<AmendmentDTO> transactionList) {
		this.transactionList = transactionList;
	}
	
	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public PermitRenewalsDTO getPermitRenewalsDTO() {
		return permitRenewalsDTO;
	}

	public void setPermitRenewalsDTO(PermitRenewalsDTO permitRenewalsDTO) {
		this.permitRenewalsDTO = permitRenewalsDTO;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<AmendmentDTO> getApplicationDocuments() {
		return ApplicationDocuments;
	}

	public void setApplicationDocuments(List<AmendmentDTO> applicationDocuments) {
		ApplicationDocuments = applicationDocuments;
	}

	
	public List<AmendmentDTO> getApplicationVersionDocuments() {
		return ApplicationVersionDocuments;
	}

	public void setApplicationVersionDocuments(List<AmendmentDTO> applicationVersionDocuments) {
		ApplicationVersionDocuments = applicationVersionDocuments;
	}

	
	public AmendmentDTO getAmendmentHistoryDTO() {
		return amendmentHistoryDTO;
	}

	public void setAmendmentHistoryDTO(AmendmentDTO amendmentHistoryDTO) {
		this.amendmentHistoryDTO = amendmentHistoryDTO;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public PermitRenewalsDTO getApplicationHistoryDTO() {
		return applicationHistoryDTO;
	}

	public void setApplicationHistoryDTO(PermitRenewalsDTO applicationHistoryDTO) {
		this.applicationHistoryDTO = applicationHistoryDTO;
	}

	public PermitRenewalsDTO getOwnerHistoryDTO() {
		return ownerHistoryDTO;
	}

	public void setOwnerHistoryDTO(PermitRenewalsDTO ownerHistoryDTO) {
		this.ownerHistoryDTO = ownerHistoryDTO;
	}

	public OminiBusDTO getOminiBusHistoryDTO() {
		return ominiBusHistoryDTO;
	}

	public void setOminiBusHistoryDTO(OminiBusDTO ominiBusHistoryDTO) {
		this.ominiBusHistoryDTO = ominiBusHistoryDTO;
	}

	public AmendmentBusOwnerDTO getRouteDetDTO() {
		return routeDetDTO;
	}

	public void setRouteDetDTO(AmendmentBusOwnerDTO routeDetDTO) {
		this.routeDetDTO = routeDetDTO;
	}


	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public List<AmendmentDTO> getTransactionListforTimeApproval() {
		return transactionListforTimeApproval;
	}

	public void setTransactionListforTimeApproval(List<AmendmentDTO> transactionListforTimeApproval) {
		this.transactionListforTimeApproval = transactionListforTimeApproval;
	}

	public boolean isDisabledApplicationNo() {
		return disabledApplicationNo;
	}

	public void setDisabledApplicationNo(boolean disabledApplicationNo) {
		this.disabledApplicationNo = disabledApplicationNo;
	}

	public boolean isDisabledPermitNo() {
		return disabledPermitNo;
	}

	public void setDisabledPermitNo(boolean disabledPermitNo) {
		this.disabledPermitNo = disabledPermitNo;
	}
	

}
