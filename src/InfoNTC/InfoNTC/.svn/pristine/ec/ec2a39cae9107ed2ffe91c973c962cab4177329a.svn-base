package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.GamiRouteDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "gsCreateTenderBackingBean")
@ViewScoped
public class GsCreateTenderBackingBean {

	private static final long serialVersionUID = 1L;

	private TenderDTO tenderDTO;
	private GamiRouteDTO gamiRouteDTO;
	private GamiRouteDTO selectedRoute;

	private GamiRouteDTO onTrafficProposalChangeDTO;

	private List<TenderDTO> drpdTrafficProposalList;
	private List<GamiRouteDTO> tblTrafficProposalRouteInfo;
	private List<GamiRouteDTO> tblRouteInfo;

	private GamiSariyaService gamiSariyaService;
	private CommonService commonService;

	private boolean renderTblrouteInfo, renderTblTrafficProRouteInfo;
	private boolean renderBtnUpdate;

	private String warningMsg, sucessMsg, errorMsg;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@PostConstruct
	public void init() {

		tenderDTO = new TenderDTO();
		gamiRouteDTO = new GamiRouteDTO();
		onTrafficProposalChangeDTO = new GamiRouteDTO();
		selectedRoute = new GamiRouteDTO();

		drpdTrafficProposalList = new ArrayList<TenderDTO>();
		tblTrafficProposalRouteInfo = new ArrayList<GamiRouteDTO>();
		tblRouteInfo = new ArrayList<GamiRouteDTO>();

		loadValues();
	}

	public void loadValues() {

		errorMsg = "Data did not save.";
		sucessMsg = "Successfuly saved.";

		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		renderTblrouteInfo = true;
		drpdTrafficProposalList = gamiSariyaService.getDrpdTrafficProposalList("", "", "O");

	}

	public void onTrafficProposalNoChange() {

		if (tenderDTO.getTrafficProposalNo() != null && !tenderDTO.getTrafficProposalNo().isEmpty()
				&& !tenderDTO.getTrafficProposalNo().equals("")) {
			onTrafficProposalChangeDTO = gamiSariyaService.onTraficProposalChange(tenderDTO);
			tenderDTO.setTenderRefNo(onTrafficProposalChangeDTO.getTenderReferenceNo());
		} else {
			errorMsg = "Traffic proposal No. shoud be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnSearch() {

		String temptTrafficProposalNo = tenderDTO.getTrafficProposalNo();
		tenderDTO = new TenderDTO();
		tenderDTO.setTrafficProposalNo(temptTrafficProposalNo);

		if (tenderDTO.getTrafficProposalNo() != null && !tenderDTO.getTrafficProposalNo().isEmpty()
				&& !tenderDTO.getTrafficProposalNo().equals("")) {

			renderTblrouteInfo = true;

			// get tender details
			tenderDTO = gamiSariyaService.getGamiTenderDetails(tenderDTO);

			if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().isEmpty()
					&& !tenderDTO.getTenderRefNo().equals("")) {
				renderBtnUpdate = true;
			} else {
				renderBtnUpdate = false;
			}

			// get traffic proposal selected
			gamiRouteDTO.setSurveyReqNo(onTrafficProposalChangeDTO.getSurveyReqNo());
			tblTrafficProposalRouteInfo = gamiSariyaService.getTblTrafficProposalSelectedRouteInfo(gamiRouteDTO);

			gamiRouteDTO.setTrafficProposalNo(tenderDTO.getTrafficProposalNo());

			// get tender selected routes
			tblRouteInfo = gamiSariyaService.getTblTenderSelectedRouteInfo(gamiRouteDTO);

			if (!(commonService.checkTaskOnSurveyHisDetails(onTrafficProposalChangeDTO.getSurveyNo(), "SU009", "C"))) {
				commonService.updateSurveyTaskDetailsBySurveyNo("", onTrafficProposalChangeDTO.getSurveyNo(), "TD001",
						"O", sessionBackingBean.getLoginUser());
			}

		} else {
			errorMsg = "Traffic proposal No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnClear() {
		tenderDTO = new TenderDTO();
		gamiRouteDTO = new GamiRouteDTO();

		renderBtnUpdate = false;
		tblTrafficProposalRouteInfo = new ArrayList<GamiRouteDTO>();
		tblRouteInfo = new ArrayList<GamiRouteDTO>();
	}

	public void onRouteRowSelect(SelectEvent event) {

		GamiRouteDTO tempt = (((GamiRouteDTO) event.getObject()));

		gamiRouteDTO = tempt;

	}

	public void onOriginChange() {

	}

	public void btnAdd() {

		if (gamiRouteDTO.getSeqNo() != 0) {

			tblRouteInfo.add(gamiRouteDTO);
			gamiRouteDTO.setIsTenderSelected("Y");
			gamiRouteDTO.setTrafficProposalNo(onTrafficProposalChangeDTO.getTenderReferenceNo());
			boolean saved = gamiSariyaService.saveSelectedRouteInfo(gamiRouteDTO, sessionBackingBean.loginUser);
			if (saved) {

				gamiRouteDTO.setSurveyReqNo(onTrafficProposalChangeDTO.getSurveyReqNo());
				gamiRouteDTO.setIsTenderSelected("Y");
				if (gamiSariyaService.insertGamiRouteHistory(gamiRouteDTO, sessionBackingBean.loginUser)) {
					sucessMsg = "Successfuly saved.";
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				} else {
					errorMsg = "Data did not save.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Data did not save.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

			gamiRouteDTO.setTrafficProposalNo(tenderDTO.getTrafficProposalNo());
			tblRouteInfo = gamiSariyaService.getTblTenderSelectedRouteInfo(gamiRouteDTO);

		} else {
			errorMsg = "Select a route from the table.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnSave() {

		if (tenderDTO.getTenderClosinDate() != null) {

			if (tenderDTO.getTenderClosinTime() != null && !tenderDTO.getTenderClosinTime().equals("")) {

				if (tenderDTO.getTenderDes() != null && !tenderDTO.getTenderDes().isEmpty()
						&& !tenderDTO.getTenderDes().equals("")) {
					tenderDTO.setTenderStatus("O");
					tenderDTO = gamiSariyaService.saveCreateTenderInfo(tenderDTO, sessionBackingBean.loginUser);

					if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().isEmpty()
							&& !tenderDTO.getTenderRefNo().equals("")) {
						renderBtnUpdate = true;
						commonService.updateSurveyTaskDetailsBySurveyNo("", onTrafficProposalChangeDTO.getSurveyNo(),
								"TD001", "C", sessionBackingBean.getLoginUser());
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					} else {
						renderBtnUpdate = false;
					}
				} else {
					errorMsg = "Tender Description should be inseted.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Tender Closing Time should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Tender Closing Date should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnUpdate() {

		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().isEmpty()
				&& !tenderDTO.getTenderRefNo().equals("")) {

			if (tenderDTO.getTenderClosinDate() != null) {

				if (tenderDTO.getTenderClosinTime() != null) {

					if (tenderDTO.getTenderDes() != null && !tenderDTO.getTenderDes().isEmpty()
							&& !tenderDTO.getTenderDes().equals("")) {
						if (gamiSariyaService.updateCreateTenderInfo(tenderDTO, sessionBackingBean.loginUser)) {
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						} else {
							errorMsg = "Data didn't save.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "Tender Description should be selected.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Tender Closing Time should be selected.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Tender Closing Date should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Tender Reference No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void deleteTblTenderSelectedRouteRaw(GamiRouteDTO tblTenderDeletingInfo) {
		tblTenderDeletingInfo.setIsTenderSelected(null);
		boolean saved = gamiSariyaService.saveSelectedRouteInfo(tblTenderDeletingInfo, sessionBackingBean.loginUser);
		if (saved) {

			tblTenderDeletingInfo.setSurveyReqNo(onTrafficProposalChangeDTO.getSurveyReqNo());
			tblTenderDeletingInfo.setIsTenderSelected("Y");
			if (gamiSariyaService.insertGamiRouteHistory(tblTenderDeletingInfo, sessionBackingBean.loginUser)) {
				sucessMsg = "Successfuly saved.";
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			} else {
				errorMsg = "Data did not save.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
			tblRouteInfo = gamiSariyaService.getTblTenderSelectedRouteInfo(gamiRouteDTO);
		} else {
			errorMsg = "Data did not save.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnCancel() {

	}

	public void btnDocumentManagement() {

	}

	public void btnApprove() {

	}

	public void btnReject() {

	}

	public void btnClearRouteInfo() {

		gamiRouteDTO = new GamiRouteDTO();

	}

	// getters and setters
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GamiRouteDTO getGamiRouteDTO() {
		return gamiRouteDTO;
	}

	public void setGamiRouteDTO(GamiRouteDTO gamiRouteDTO) {
		this.gamiRouteDTO = gamiRouteDTO;
	}

	public List<GamiRouteDTO> getTblRouteInfo() {
		return tblRouteInfo;
	}

	public void setTblRouteInfo(List<GamiRouteDTO> tblRouteInfo) {
		this.tblRouteInfo = tblRouteInfo;
	}

	public boolean isRenderTblrouteInfo() {
		return renderTblrouteInfo;
	}

	public void setRenderTblrouteInfo(boolean renderTblrouteInfo) {
		this.renderTblrouteInfo = renderTblrouteInfo;
	}

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
	}

	public List<TenderDTO> getDrpdTrafficProposalList() {
		return drpdTrafficProposalList;
	}

	public void setDrpdTrafficProposalList(List<TenderDTO> drpdTrafficProposalList) {
		this.drpdTrafficProposalList = drpdTrafficProposalList;
	}

	public List<GamiRouteDTO> getTblTrafficProposalRouteInfo() {
		return tblTrafficProposalRouteInfo;
	}

	public void setTblTrafficProposalRouteInfo(List<GamiRouteDTO> tblTrafficProposalRouteInfo) {
		this.tblTrafficProposalRouteInfo = tblTrafficProposalRouteInfo;
	}

	public boolean isRenderTblTrafficProRouteInfo() {
		return renderTblTrafficProRouteInfo;
	}

	public void setRenderTblTrafficProRouteInfo(boolean renderTblTrafficProRouteInfo) {
		this.renderTblTrafficProRouteInfo = renderTblTrafficProRouteInfo;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public GamiRouteDTO getSelectedRoute() {
		return selectedRoute;
	}

	public void setSelectedRoute(GamiRouteDTO selectedRoute) {
		this.selectedRoute = selectedRoute;
	}

	public boolean isRenderBtnUpdate() {
		return renderBtnUpdate;
	}

	public void setRenderBtnUpdate(boolean renderBtnUpdate) {
		this.renderBtnUpdate = renderBtnUpdate;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getWarningMsg() {
		return warningMsg;
	}

	public void setWarningMsg(String warningMsg) {
		this.warningMsg = warningMsg;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public GamiRouteDTO getOnTrafficProposalChangeDTO() {
		return onTrafficProposalChangeDTO;
	}

	public void setOnTrafficProposalChangeDTO(GamiRouteDTO onTrafficProposalChangeDTO) {
		this.onTrafficProposalChangeDTO = onTrafficProposalChangeDTO;
	}

}
