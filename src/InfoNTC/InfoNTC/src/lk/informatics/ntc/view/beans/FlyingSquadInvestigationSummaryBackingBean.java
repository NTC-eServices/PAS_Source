package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.dto.FlyngSquadAttendanceDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.FlyingSquadInvestigationLogService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "flyingSquadSummaryBackingBean")
@ViewScoped
public class FlyingSquadInvestigationSummaryBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO;
	private FlyingManageInvestigationLogDTO flyingManageInvestigationLogDetailDTO;
	private ArrayList<FlyngSquadAttendanceDTO> detailList;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	public FlyingSquadInvestigationLogService flyingSquadInvestigationLogService;
	private String user;
	public ArrayList<FlyingSquadInvestiMasterDTO> refNoList;
	public ArrayList<FlyingManageInvestigationLogDTO> reportNoList;
	public ArrayList<FlyingManageInvestigationLogDTO> detailListn;
	private String savemsg;
	private int tot = 0;
	private boolean viewMode;
	private CommonService commonService;
	private boolean disabledSaved;
	private boolean createMode;

	@PostConstruct
	public void init() {
		setFlyingSquadInvestigationLogService((FlyingSquadInvestigationLogService) SpringApplicationContex
				.getBean("flyingSquadInvestigationLogService"));
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN363", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN363", "V");
		setUser(sessionBackingBean.getLoginUser());
		refNoList = flyingSquadInvestigationLogService.getrefNon();
		reportNoList = flyingSquadInvestigationLogService.getreportNo(null);
		flyingManageInvestigationLogDetailDTO = new FlyingManageInvestigationLogDTO();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		detailList = new ArrayList<FlyngSquadAttendanceDTO>();

		if (viewMode == true) {
			disabledSaved = true;
		} else {
			disabledSaved = false;
		}

		if (createMode == true) {
			disabledSaved = false;
		} else {
			disabledSaved = true;
		}
	}

	public void reportNoList() {
		String refno = flyingManageInvestigationLogDTO.getRefNo();
		reportNoList = flyingSquadInvestigationLogService.getreportNo(flyingManageInvestigationLogDTO.getRefNo());
		flyingManageInvestigationLogDetailDTO = new FlyingManageInvestigationLogDTO();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		detailList = new ArrayList<FlyngSquadAttendanceDTO>();
		flyingManageInvestigationLogDTO.setRefNo(refno);
		tot = 0;
	}

	public void search() {
		if (flyingManageInvestigationLogDTO.getRefNo() != null
				&& !flyingManageInvestigationLogDTO.getRefNo().equalsIgnoreCase("")) {
			if (flyingManageInvestigationLogDTO.getReportNo() != null
					&& !flyingManageInvestigationLogDTO.getReportNo().equalsIgnoreCase("")) {
				String reportno = flyingManageInvestigationLogDTO.getReportNo();
				String refNo = flyingManageInvestigationLogDTO.getRefNo();
				flyingManageInvestigationLogDTO = flyingSquadInvestigationLogService.getmasterData(
						flyingManageInvestigationLogDTO.getRefNo(), flyingManageInvestigationLogDTO.getReportNo());
				reportNoList = flyingSquadInvestigationLogService.getreportNo(refNo);
				flyingManageInvestigationLogDetailDTO = flyingSquadInvestigationLogService.getdetail(refNo, reportno);
				flyingManageInvestigationLogDTO.setReportNo(reportno);
				flyingManageInvestigationLogDTO.setRefNo(refNo);
				detailList = flyingSquadInvestigationLogService.getexsistingdetails(refNo,
						flyingManageInvestigationLogDTO.getInvestigationDate());
				detailListn = flyingSquadInvestigationLogService.getdetails(refNo, reportno);
				setTot(detailListn.size());
			} else {
				sessionBackingBean.setMessage("Please Enter Report No.");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}

		} else {
			sessionBackingBean.setMessage("Please Enter Reference No.");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}

	public void save() {
		if (flyingManageInvestigationLogDTO.getReportNo() != null
				&& !flyingManageInvestigationLogDTO.getReportNo().equalsIgnoreCase("")) {
			if ((flyingManageInvestigationLogDetailDTO.getKilometers() == null
					|| flyingManageInvestigationLogDetailDTO.getKilometers().equalsIgnoreCase(""))
					&& (flyingManageInvestigationLogDetailDTO.getChanges() == null
							|| flyingManageInvestigationLogDetailDTO.getChanges().equalsIgnoreCase(""))
					&& (flyingManageInvestigationLogDetailDTO.getDefects() == null
							|| flyingManageInvestigationLogDetailDTO.getDefects().equalsIgnoreCase(""))
					&& (flyingManageInvestigationLogDetailDTO.getObservations() == null
							|| flyingManageInvestigationLogDetailDTO.getObservations().equalsIgnoreCase(""))) {

				sessionBackingBean.setMessage("No changes to Save.");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");

			} else {

				flyingSquadInvestigationLogService.updatemasterdet(flyingManageInvestigationLogDetailDTO, user,
						flyingManageInvestigationLogDTO.getRefNo(), flyingManageInvestigationLogDTO.getReportNo());
				setSavemsg("Successfully Saved.");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
			}

		} else {

			sessionBackingBean.setMessage("Please Enter Report No.");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}

	public void cancel() {
		refNoList = flyingSquadInvestigationLogService.getrefNon();
		flyingManageInvestigationLogDetailDTO = new FlyingManageInvestigationLogDTO();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		detailList = new ArrayList<FlyngSquadAttendanceDTO>();
		reportNoList = flyingSquadInvestigationLogService.getreportNo(null);
		tot = 0;
	}

	public void getrefNo() {
		String repNo = flyingManageInvestigationLogDTO.getReportNo();
		String refNo = flyingSquadInvestigationLogService.getrefNoNew(flyingManageInvestigationLogDTO.getReportNo());
		refNoList = flyingSquadInvestigationLogService.getrefNon();
		flyingManageInvestigationLogDetailDTO = new FlyingManageInvestigationLogDTO();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		detailList = new ArrayList<FlyngSquadAttendanceDTO>();
		flyingManageInvestigationLogDTO.setRefNo(refNo);
		flyingManageInvestigationLogDTO.setReportNo(repNo);
		tot = 0;
	}

	public FlyingManageInvestigationLogDTO getFlyingManageInvestigationLogDTO() {
		return flyingManageInvestigationLogDTO;
	}

	public void setFlyingManageInvestigationLogDTO(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO) {
		this.flyingManageInvestigationLogDTO = flyingManageInvestigationLogDTO;
	}

	public FlyingManageInvestigationLogDTO getFlyingManageInvestigationLogDetailDTO() {
		return flyingManageInvestigationLogDetailDTO;
	}

	public void setFlyingManageInvestigationLogDetailDTO(
			FlyingManageInvestigationLogDTO flyingManageInvestigationLogDetailDTO) {
		this.flyingManageInvestigationLogDetailDTO = flyingManageInvestigationLogDetailDTO;
	}

	public ArrayList<FlyngSquadAttendanceDTO> getDetailList() {
		return detailList;
	}

	public void setDetailList(ArrayList<FlyngSquadAttendanceDTO> detailList) {
		this.detailList = detailList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public FlyingSquadInvestigationLogService getFlyingSquadInvestigationLogService() {
		return flyingSquadInvestigationLogService;
	}

	public void setFlyingSquadInvestigationLogService(
			FlyingSquadInvestigationLogService flyingSquadInvestigationLogService) {
		this.flyingSquadInvestigationLogService = flyingSquadInvestigationLogService;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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

	public String getSavemsg() {
		return savemsg;
	}

	public void setSavemsg(String savemsg) {
		this.savemsg = savemsg;
	}

	public ArrayList<FlyingManageInvestigationLogDTO> getDetailListn() {
		return detailListn;
	}

	public void setDetailListn(ArrayList<FlyingManageInvestigationLogDTO> detailListn) {
		this.detailListn = detailListn;
	}

	public int getTot() {
		return tot;
	}

	public void setTot(int tot) {
		this.tot = tot;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisabledSaved() {
		return disabledSaved;
	}

	public void setDisabledSaved(boolean disabledSaved) {
		this.disabledSaved = disabledSaved;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

}
