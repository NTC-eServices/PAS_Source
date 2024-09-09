package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import lk.informatics.ntc.model.dto.FlyingSquadApprovalInvestigationDTO;
import lk.informatics.ntc.model.service.FlyingSquadApprovalInvestigationService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "flyingSquadInvestigationApprovalBean")
@ViewScoped
public class FlyingSquadApprovalInvestigationBean implements Serializable {

	private static final long serialVersionUID = 4590075407419180167L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private Date startDate;
	private Date endDate;
	private String refNo;
	private String user;
	private String errormsg;
	private String remark;
	private boolean reject;
	private boolean recomended;
	private boolean finalized;
	private boolean cancel;
	private String rejectReason;
	private String recomendedReason;
	private String finalizedReason;
	private boolean select;
	private String savemsg;
	private ArrayList<FlyingSquadApprovalInvestigationDTO> flyingSquadDetailList;
	private FlyingSquadApprovalInvestigationService flyingSquadApprovalInvestigationService;
	private ArrayList<FlyingSquadApprovalInvestigationDTO> referenceNoList;
	private FlyingSquadApprovalInvestigationDTO flyingSquadApprovalInvestigationDTO;
	private FlyingSquadApprovalInvestigationDTO selectedflyingSquadApprovalInvestigationDTO;

	private boolean disablereject;
	private boolean disablerecomended;
	private boolean disablefinalized;

	@PostConstruct
	public void init() {

		setFlyingSquadApprovalInvestigationService((FlyingSquadApprovalInvestigationService) SpringApplicationContex
				.getBean("flyingSquadApprovalInvestigationService"));
		setUser(sessionBackingBean.getLoginUser());
		flyingSquadDetailList = new ArrayList<FlyingSquadApprovalInvestigationDTO>();
		referenceNoList = new ArrayList<FlyingSquadApprovalInvestigationDTO>();
		referenceNoList = flyingSquadApprovalInvestigationService.getReferenceNo(startDate, endDate);
		flyingSquadApprovalInvestigationDTO = new FlyingSquadApprovalInvestigationDTO();
		selectedflyingSquadApprovalInvestigationDTO = new FlyingSquadApprovalInvestigationDTO();
		disablereject = true;
		disablerecomended = true;
		disablefinalized = true;
	}

	public void search() {
		if (startDate != null && endDate == null) {
			errormsg = "Please Enter End Date.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
		}

		else if (startDate == null && endDate != null) {
			errormsg = "Please Enter Start Date.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
		}

		else if (startDate == null && endDate == null && (refNo.equalsIgnoreCase("") || refNo.isEmpty())) {
			errormsg = "Please Enter At Least One Search Criteria.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
		} else {
			flyingSquadDetailList = flyingSquadApprovalInvestigationService.getDetails(startDate, endDate, refNo);
		}

	}

	public void updatereject() {

		reject = true;
		recomended = false;
		finalized = false;
		String failrejectrefNo = null;

		if (rejectReason.equalsIgnoreCase("") || rejectReason.isEmpty() || rejectReason == null) {
			errormsg = "Please Enter Reject Reason.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('ErrorMsg').show()");
		} else {
			flyingSquadApprovalInvestigationService.updatereasons(reject, recomended, finalized, cancel, rejectReason,
					user, selectedflyingSquadApprovalInvestigationDTO.getReferenceNo());
			flyingSquadDetailList = flyingSquadApprovalInvestigationService.getDetails(startDate, endDate, refNo);
			rejectReason = null;
			finalizedReason = null;
			recomendedReason = null;
			selectedflyingSquadApprovalInvestigationDTO = new FlyingSquadApprovalInvestigationDTO();
			disablereject = true;
			disablerecomended = true;
			disablefinalized = true;
			savemsg = "Successfully Rejected";
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");

		}

	}

	public void updaterecomended() {
		reject = false;
		recomended = true;
		finalized = false;
		String failrecorefNo = null;

		flyingSquadApprovalInvestigationService.updatereasons(reject, recomended, finalized, cancel, recomendedReason,
				user, selectedflyingSquadApprovalInvestigationDTO.getReferenceNo());
		flyingSquadDetailList = flyingSquadApprovalInvestigationService.getDetails(startDate, endDate, refNo);
		savemsg = "Successfully Recommended";
		recomendedReason = null;
		selectedflyingSquadApprovalInvestigationDTO = new FlyingSquadApprovalInvestigationDTO();
		disablereject = true;
		disablerecomended = true;
		disablefinalized = true;
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");

	}

	public void updateFinalized() {
		reject = false;
		recomended = false;
		finalized = true;
		String failrefNos = null;

		flyingSquadApprovalInvestigationService.updatereasons(reject, recomended, finalized, cancel, finalizedReason,
				user, selectedflyingSquadApprovalInvestigationDTO.getReferenceNo());
		flyingSquadDetailList = flyingSquadApprovalInvestigationService.getDetails(startDate, endDate, refNo);
		savemsg = "Successfully Finalized";
		finalizedReason = null;
		rejectReason = null;
		rejectReason = null;
		selectedflyingSquadApprovalInvestigationDTO = new FlyingSquadApprovalInvestigationDTO();
		disablereject = true;
		disablerecomended = true;
		disablefinalized = true;
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");

	}

	public void cancel() {
		startDate = null;
		endDate = null;
		refNo = null;
		reject = false;
		recomended = false;
		finalized = false;
		flyingSquadDetailList = flyingSquadApprovalInvestigationService.getDetails(startDate, endDate, refNo);
		rejectReason = null;
		recomendedReason = null;
		finalizedReason = null;

	}

	public void clear() {
		startDate = null;
		endDate = null;
		refNo = null;
		flyingSquadDetailList = new ArrayList<FlyingSquadApprovalInvestigationDTO>();
		rejectReason = null;
		recomendedReason = null;
		finalizedReason = null;
	}

	public void getreferenceNo() {
		referenceNoList = flyingSquadApprovalInvestigationService.getReferenceNo(startDate, endDate);
	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
	}

	public void viewAction() {
		sessionBackingBean.setFlyrefNo(flyingSquadApprovalInvestigationDTO.getReferenceNo());
		RequestContext.getCurrentInstance().update("frmview");
		RequestContext.getCurrentInstance().execute("PF('showPageDialog').show()");
	}

	public void selectedRecord() {
		if (selectedflyingSquadApprovalInvestigationDTO.getStatus() == "PENDING") {
			disablereject = false;
			disablerecomended = false;
			disablefinalized = true;

		} else if (selectedflyingSquadApprovalInvestigationDTO.getStatus() == "RECOMMENDED") {
			disablereject = false;
			disablerecomended = true;
			disablefinalized = false;

		} else if (selectedflyingSquadApprovalInvestigationDTO.getStatus() == "REJECT") {
			disablereject = true;
			disablerecomended = true;
			disablefinalized = true;

		} else if (selectedflyingSquadApprovalInvestigationDTO.getStatus() == "FINALIZED") {
			disablereject = true;
			disablerecomended = true;
			disablefinalized = true;

		}

	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public ArrayList<FlyingSquadApprovalInvestigationDTO> getFlyingSquadDetailList() {
		return flyingSquadDetailList;
	}

	public void setFlyingSquadDetailList(ArrayList<FlyingSquadApprovalInvestigationDTO> flyingSquadDetailList) {
		this.flyingSquadDetailList = flyingSquadDetailList;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public FlyingSquadApprovalInvestigationService getFlyingSquadApprovalInvestigationService() {
		return flyingSquadApprovalInvestigationService;
	}

	public void setFlyingSquadApprovalInvestigationService(
			FlyingSquadApprovalInvestigationService flyingSquadApprovalInvestigationService) {
		this.flyingSquadApprovalInvestigationService = flyingSquadApprovalInvestigationService;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRecomendedReason() {
		return recomendedReason;
	}

	public void setRecomendedReason(String recomendedReason) {
		this.recomendedReason = recomendedReason;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public String getFinalizedReason() {
		return finalizedReason;
	}

	public void setFinalizedReason(String finalizedReason) {
		this.finalizedReason = finalizedReason;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isReject() {
		return reject;
	}

	public void setReject(boolean reject) {
		this.reject = reject;
	}

	public boolean isRecomended() {
		return recomended;
	}

	public void setRecomended(boolean recomended) {
		this.recomended = recomended;
	}

	public boolean isFinalized() {
		return finalized;
	}

	public void setFinalized(boolean finalized) {
		this.finalized = finalized;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public ArrayList<FlyingSquadApprovalInvestigationDTO> getReferenceNoList() {
		return referenceNoList;
	}

	public void setReferenceNoList(ArrayList<FlyingSquadApprovalInvestigationDTO> referenceNoList) {
		this.referenceNoList = referenceNoList;
	}

	public String getSavemsg() {
		return savemsg;
	}

	public void setSavemsg(String savemsg) {
		this.savemsg = savemsg;
	}

	public FlyingSquadApprovalInvestigationDTO getFlyingSquadApprovalInvestigationDTO() {
		return flyingSquadApprovalInvestigationDTO;
	}

	public void setFlyingSquadApprovalInvestigationDTO(
			FlyingSquadApprovalInvestigationDTO flyingSquadApprovalInvestigationDTO) {
		this.flyingSquadApprovalInvestigationDTO = flyingSquadApprovalInvestigationDTO;
	}

	public FlyingSquadApprovalInvestigationDTO getSelectedflyingSquadApprovalInvestigationDTO() {
		return selectedflyingSquadApprovalInvestigationDTO;
	}

	public void setSelectedflyingSquadApprovalInvestigationDTO(
			FlyingSquadApprovalInvestigationDTO selectedflyingSquadApprovalInvestigationDTO) {
		this.selectedflyingSquadApprovalInvestigationDTO = selectedflyingSquadApprovalInvestigationDTO;
	}

	public boolean isDisablereject() {
		return disablereject;
	}

	public void setDisablereject(boolean disablereject) {
		this.disablereject = disablereject;
	}

	public boolean isDisablerecomended() {
		return disablerecomended;
	}

	public void setDisablerecomended(boolean disablerecomended) {
		this.disablerecomended = disablerecomended;
	}

	public boolean isDisablefinalized() {
		return disablefinalized;
	}

	public void setDisablefinalized(boolean disablefinalized) {
		this.disablefinalized = disablefinalized;
	}

}
