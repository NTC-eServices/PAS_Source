package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.LogsheetMaintenanceService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "timeApprovalRequestBackingBean")
@ViewScoped
public class TimeApprovalRequestBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<CommonDTO> datatableList = new ArrayList<CommonDTO>();
	private CommonDTO selectedRow = new CommonDTO();
	private CommonService commonService;
	private String sucessMsg;
	private String errorMsg;

	private HistoryService historyService;
	private AmendmentDTO amendmentHistoryDTO;

	@PostConstruct
	public void init() {
		setCommonService((CommonService) SpringApplicationContex.getBean("commonService"));
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");
		setDatatableList(commonService.timeApproval());
	}

	public void approve() {
		String user = sessionBackingBean.getLoginUser();
		if(selectedRow.getTimeSLots().equals(null) || selectedRow.getTimeSLots()== null||
				selectedRow.getTimeSLots().trim().isEmpty()) {
			errorMsg = "Please enter time slots.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			
		}
		else {	
		amendmentHistoryDTO = historyService.getAmendmentTableData(selectedRow.getApplicationNo(),
				sessionBackingBean.getLoginUser());
		commonService.timeapproveRequest(user,selectedRow.getTimeSLots(), selectedRow.getApplicationNo());
		historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);

		setSucessMsg("Approved successfully");
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

		
		/**Update task in history table by tharushi.e **/
		commonService.updateCommonTaskHistory(selectedRow.getVehicleNo(), selectedRow.getApplicationNo(), "AM112", "C",  sessionBackingBean.getLoginUser());
		/**End Update task in history table by tharushi.e **/
		setDatatableList(commonService.timeApproval());
		}
		
	}

	public String viewAmendment() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		sessionBackingBean.setPageMode("V");
		sessionBackingBean.setApproveURL(request.getRequestURL().toString());
		sessionBackingBean.setSearchURL(null);
		sessionBackingBean.setApproveURLStatus(true);
		sessionBackingBean.setAmendmentsApplicationNo(selectedRow.getApplicationNo());

		sessionBackingBean.amendmentsViewMood = true;

		return "/pages/amendmentsToBusOrOwner/viewBusOwnerServiceAmendment.xhtml#!";

	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public CommonDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(CommonDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public List<CommonDTO> getDatatableList() {
		return datatableList;
	}

	public void setDatatableList(List<CommonDTO> datatableList) {
		this.datatableList = datatableList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public AmendmentDTO getAmendmentHistoryDTO() {
		return amendmentHistoryDTO;
	}

	public void setAmendmentHistoryDTO(AmendmentDTO amendmentHistoryDTO) {
		this.amendmentHistoryDTO = amendmentHistoryDTO;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
}
