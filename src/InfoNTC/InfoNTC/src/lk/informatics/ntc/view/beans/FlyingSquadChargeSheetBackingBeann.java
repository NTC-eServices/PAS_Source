package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import lk.informatics.ntc.model.dto.FluingSquadVioConditionDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.dto.FlyingSquadVioDocumentsDTO;
import lk.informatics.ntc.model.service.FlyingSquadChargeSheetService;
import lk.informatics.ntc.model.service.FlyingSquadInvestigationLogService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "flyingSquadChargeSheetBackingBeann")
@ViewScoped
public class FlyingSquadChargeSheetBackingBeann implements Serializable {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO;
	private FlyingSquadChargeSheetService flyingSquadChargeSheetService;
	private ArrayList<FluingSquadVioConditionDTO> conditionList;
	private ArrayList<FlyingSquadVioDocumentsDTO> documentList;
	private String user;
	private String savemsg;
	private String errormsg;
	public FlyingSquadInvestigationLogService flyingSquadInvestigationLogService;
	public ArrayList<FlyingSquadInvestiMasterDTO> refNoList;
	public ArrayList<FlyingManageInvestigationLogDTO> reportNoList;
	public ArrayList<FlyingManageInvestigationLogDTO> invesNoList;
	public ArrayList<FlyingManageInvestigationLogDTO> driverList;
	public ArrayList<FlyingManageInvestigationLogDTO> conductorList;

	@PostConstruct
	public void init() {
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		setFlyingSquadChargeSheetService(
				(FlyingSquadChargeSheetService) SpringApplicationContex.getBean("flyingSquadChargeSheetService"));
		setFlyingSquadInvestigationLogService((FlyingSquadInvestigationLogService) SpringApplicationContex
				.getBean("flyingSquadInvestigationLogService"));
		setUser(sessionBackingBean.getLoginUser());
		refNoList = flyingSquadInvestigationLogService.getrefNo();
		invesNoList = flyingSquadChargeSheetService.getinvesnolist(null, null);
		driverList = flyingSquadChargeSheetService.getdriverList();
		conductorList = flyingSquadChargeSheetService.getConductorList();

		if (sessionBackingBean.getInvesNo() != null && !sessionBackingBean.getInvesNo().equalsIgnoreCase("")) {
			String refNo = sessionBackingBean.getInvesNo();
			flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
			flyingManageInvestigationLogDTO = flyingSquadChargeSheetService.getmasterDetails(refNo);
			conditionList = new ArrayList<FluingSquadVioConditionDTO>();
			documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
			conditionList = flyingSquadChargeSheetService.getConditionList(refNo);
			documentList = flyingSquadChargeSheetService.getdocumentlist(refNo);
			reportNoList = flyingSquadInvestigationLogService.getreportNo(flyingManageInvestigationLogDTO.getRefNo());
			flyingManageInvestigationLogDTO.setInvesNo(refNo);
		}
	}

	public void getreportNo() {
		reportNoList = flyingSquadInvestigationLogService.getreportNo(flyingManageInvestigationLogDTO.getRefNo());
		invesNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
	}

	public void getinvesNolist() {
		invesNoList = flyingSquadChargeSheetService.getinvesnolist(null, flyingManageInvestigationLogDTO.getReportNo());
	}

	public void getName() {
		flyingManageInvestigationLogDTO
				.setDriverName(flyingSquadChargeSheetService.getname(flyingManageInvestigationLogDTO.getDriverID()));
	}

	public void getConductor() {
		flyingManageInvestigationLogDTO.setConductName(
				flyingSquadChargeSheetService.getname(flyingManageInvestigationLogDTO.getConductorID()));
	}

	public void getinvesNo() {
		invesNoList = flyingSquadChargeSheetService.getinvesnolist(flyingManageInvestigationLogDTO.getRefNo(),
				flyingManageInvestigationLogDTO.getReportNo());
	}

	public void search() {
		if (flyingManageInvestigationLogDTO.getInvesNo() != null
				&& !flyingManageInvestigationLogDTO.getInvesNo().equalsIgnoreCase("")) {
			String refNo = flyingManageInvestigationLogDTO.getInvesNo();
			flyingManageInvestigationLogDTO = flyingSquadChargeSheetService
					.getmasterDetails(flyingManageInvestigationLogDTO.getInvesNo());
			conditionList = new ArrayList<FluingSquadVioConditionDTO>();
			documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
			conditionList = flyingSquadChargeSheetService.getConditionList(refNo);
			documentList = flyingSquadChargeSheetService.getdocumentlist(refNo);
			reportNoList = flyingSquadInvestigationLogService.getreportNo(flyingManageInvestigationLogDTO.getRefNo());
			flyingManageInvestigationLogDTO.setInvesNo(refNo);
		} else {
			if (flyingManageInvestigationLogDTO.getRefNo() != null
					&& !flyingManageInvestigationLogDTO.getRefNo().equalsIgnoreCase("")
					&& !flyingManageInvestigationLogDTO.getRefNo().isEmpty()) {
				if (flyingManageInvestigationLogDTO.getReportNo() != null
						&& !flyingManageInvestigationLogDTO.getReportNo().equalsIgnoreCase("")) {
					if (flyingManageInvestigationLogDTO.getInvesNo() != null
							&& !flyingManageInvestigationLogDTO.getInvesNo().equalsIgnoreCase("")) {

						String refNo = flyingManageInvestigationLogDTO.getInvesNo();
						flyingManageInvestigationLogDTO = flyingSquadChargeSheetService
								.getmasterDetails(flyingManageInvestigationLogDTO.getInvesNo());
						conditionList = new ArrayList<FluingSquadVioConditionDTO>();
						documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
						conditionList = flyingSquadChargeSheetService.getConditionList(refNo);
						documentList = flyingSquadChargeSheetService.getdocumentlist(refNo);
						reportNoList = flyingSquadInvestigationLogService.getreportNo(refNo);
						flyingManageInvestigationLogDTO.setInvesNo(refNo);
					} else {

						sessionBackingBean.setMessage("Please Enter Charge Reference No.");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					}

				} else {

					sessionBackingBean.setMessage("Please Enter  Report No");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				}

			} else {

				sessionBackingBean.setMessage("Please Enter  Reference No.");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}
		}

	}

	public void save() {
		if (flyingManageInvestigationLogDTO.getInvesNo() != null
				&& !flyingManageInvestigationLogDTO.getInvesNo().equalsIgnoreCase("")) {

			flyingSquadChargeSheetService.save(flyingManageInvestigationLogDTO, user);
			flyingSquadChargeSheetService.savecondition(conditionList, user, flyingManageInvestigationLogDTO.getRefNo(),
					flyingManageInvestigationLogDTO.getReportNo(), flyingManageInvestigationLogDTO.getBusNo(),
					flyingManageInvestigationLogDTO.getInvesNo(), true);
			flyingSquadChargeSheetService.savemasterdata(documentList, user, flyingManageInvestigationLogDTO.getRefNo(),
					flyingManageInvestigationLogDTO.getReportNo(), flyingManageInvestigationLogDTO.getBusNo(),
					flyingManageInvestigationLogDTO.getInvesNo(), true);
			savemsg = "Successfully Saved";
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
		} else {
			sessionBackingBean.setMessage("Please Enter Master Details");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}

	public void cancel() {
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		refNoList = flyingSquadInvestigationLogService.getrefNo();
		invesNoList = flyingSquadChargeSheetService.getinvesnolist(null, null);
		driverList = flyingSquadChargeSheetService.getdriverList();
		conductorList = flyingSquadChargeSheetService.getConductorList();
		documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		conditionList = new ArrayList<FluingSquadVioConditionDTO>();

	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
	}

	public void close() {
		sessionBackingBean.setInvesNo("");
		RequestContext.getCurrentInstance().update("frmview");
		RequestContext.getCurrentInstance().execute("PF('showPageDialog').hide()");
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public FlyingManageInvestigationLogDTO getFlyingManageInvestigationLogDTO() {
		return flyingManageInvestigationLogDTO;
	}

	public void setFlyingManageInvestigationLogDTO(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO) {
		this.flyingManageInvestigationLogDTO = flyingManageInvestigationLogDTO;
	}

	public FlyingSquadChargeSheetService getFlyingSquadChargeSheetService() {
		return flyingSquadChargeSheetService;
	}

	public void setFlyingSquadChargeSheetService(FlyingSquadChargeSheetService flyingSquadChargeSheetService) {
		this.flyingSquadChargeSheetService = flyingSquadChargeSheetService;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ArrayList<FlyingSquadVioDocumentsDTO> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(ArrayList<FlyingSquadVioDocumentsDTO> documentList) {
		this.documentList = documentList;
	}

	public ArrayList<FluingSquadVioConditionDTO> getConditionList() {
		return conditionList;
	}

	public void setConditionList(ArrayList<FluingSquadVioConditionDTO> conditionList) {
		this.conditionList = conditionList;
	}

	public String getSavemsg() {
		return savemsg;
	}

	public void setSavemsg(String savemsg) {
		this.savemsg = savemsg;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public FlyingSquadInvestigationLogService getFlyingSquadInvestigationLogService() {
		return flyingSquadInvestigationLogService;
	}

	public void setFlyingSquadInvestigationLogService(
			FlyingSquadInvestigationLogService flyingSquadInvestigationLogService) {
		this.flyingSquadInvestigationLogService = flyingSquadInvestigationLogService;
	}

	public ArrayList<FlyingSquadInvestiMasterDTO> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(ArrayList<FlyingSquadInvestiMasterDTO> refNoList) {
		this.refNoList = refNoList;
	}

	public ArrayList<FlyingManageInvestigationLogDTO> getReportNoList() {
		return reportNoList;
	}

	public void setReportNoList(ArrayList<FlyingManageInvestigationLogDTO> reportNoList) {
		this.reportNoList = reportNoList;
	}

	public ArrayList<FlyingManageInvestigationLogDTO> getInvesNoList() {
		return invesNoList;
	}

	public void setInvesNoList(ArrayList<FlyingManageInvestigationLogDTO> invesNoList) {
		this.invesNoList = invesNoList;
	}

	public ArrayList<FlyingManageInvestigationLogDTO> getDriverList() {
		return driverList;
	}

	public void setDriverList(ArrayList<FlyingManageInvestigationLogDTO> driverList) {
		this.driverList = driverList;
	}

	public ArrayList<FlyingManageInvestigationLogDTO> getConductorList() {
		return conductorList;
	}

	public void setConductorList(ArrayList<FlyingManageInvestigationLogDTO> conductorList) {
		this.conductorList = conductorList;
	}

}
