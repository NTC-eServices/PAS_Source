package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "welcomeBackingBean")
@ViewScoped

public class WelcomeBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private CommonService commonService;

	private int approvePendingEmployee;
	private int newPermitApproval;
	private int approveVoucher;
	private int printPermit;
	private int permitRenewalApproval;
	private int grantApprovalforAmmendments;
	private int approveSurveyProcessRequests;
	private int approveTenderAdvertisement;
	private int approveElectedBidder;
	private int committeeBoardAuthorization;
	private int committeeApproval;
	private int boardApproval;
	private int generateReceipt;
	private int cancelledPermits;
	private int grantApprovalforAmmendmentsDirector;
	private int grantApprovalforAmmendmentsChairman;
	private boolean director, chairman = false;

	@PostConstruct
	public void init() {

		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		approvePendingEmployee = commonService.dashboardPendingCount("approvePendingEmployee");
		newPermitApproval = commonService.dashboardPendingCount("newPermitApproval");
		approveVoucher = commonService.dashboardPendingCount("approveVoucher");
		printPermit = commonService.dashboardPendingCount("printPermit");
		permitRenewalApproval = commonService.dashboardPendingCount("permitRenewalApproval");
		grantApprovalforAmmendments = commonService.dashboardPendingCount("grantApprovalforAmmendments");
		grantApprovalforAmmendmentsDirector = commonService
				.dashboardPendingCount("grantApprovalforAmmendmentsDirector");
		grantApprovalforAmmendmentsChairman = commonService
				.dashboardPendingCount("grantApprovalforAmmendmentsChairman");
		approveSurveyProcessRequests = commonService.dashboardPendingCount("approveSurveyProcessRequests");
		approveTenderAdvertisement = commonService.dashboardPendingCount("approveTenderAdvertisement");
		approveElectedBidder = commonService.dashboardPendingCount("approveElectedBidder");
		committeeBoardAuthorization = commonService.dashboardPendingCount("committeeBoardAuthorization");
		committeeApproval = commonService.dashboardPendingCount("committeeApproval");
		boardApproval = commonService.dashboardPendingCount("boardApproval");
		generateReceipt = commonService.dashboardPendingCount("generateReceipt");
		cancelledPermits = commonService.dashboardPendingCount("cancelledPermits");

		director = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN310", "DA");
		chairman = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN310", "CA");

	}

	public String directToPages(int number) {

		String pageURL = null;

		if (number == 1) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/um/approveEmployeeProfile.xhtml#!";

		} else if (number == 2) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/viewPermitRenewals/newPermitPrintingNew.xhtml#!";

		} else if (number == 3) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/payment/paymentVoucherApproval.xhtml#!";

		} else if (number == 4) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/viewPermitRenewals/newPermitPrintingNew.xhtml#!";

		} else if (number == 5) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/Renewal/permitRenewalApproval.xhtml#!";

		} else if (number == 6) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/amendmentsToBusOrOwner/grantApprovalAmendments.xhtml#!";

		} else if (number == 7) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/surveyManagement/approveSurveyProcessRequest.xhtml#!";

		} else if (number == 8) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/tenderManagement/approveAdvertisement.xhtml#!";

		} else if (number == 9) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/tenderManagement/approveElectedBidder.xhtml#!";

		} else if (number == 10) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/amendmentsToBusOrOwner/authorizeCommitteeBoard.xhtml#!";

		} else if (number == 11) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/surveyManagement/CommitteeBoardApproval.xhtml#!";

		} else if (number == 12) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/surveyManagement/CommitteeBoardApproval.xhtml#!";

		} else if (number == 13) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/generateReceipt/generateReceipt.xhtml#!";

		} else if (number == 14) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/Renewal/permitCancellations.xhtml#!";
		} else if (number == 15) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			sessionBackingBean.setApproveURL(request.getRequestURL().toString());
			sessionBackingBean.setSearchURL(null);
			sessionBackingBean.setApproveURLStatus(true);
			pageURL = "/pages/amendmentsToBusOrOwner/amendmentsAuthorization.xhtml";
		} else {
			return null;
		}
		return pageURL;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getApprovePendingEmployee() {
		return approvePendingEmployee;
	}

	public void setApprovePendingEmployee(int approvePendingEmployee) {
		this.approvePendingEmployee = approvePendingEmployee;
	}

	public int getNewPermitApproval() {
		return newPermitApproval;
	}

	public void setNewPermitApproval(int newPermitApproval) {
		this.newPermitApproval = newPermitApproval;
	}

	public int getApproveVoucher() {
		return approveVoucher;
	}

	public void setApproveVoucher(int approveVoucher) {
		this.approveVoucher = approveVoucher;
	}

	public int getPrintPermit() {
		return printPermit;
	}

	public void setPrintPermit(int printPermit) {
		this.printPermit = printPermit;
	}

	public int getPermitRenewalApproval() {
		return permitRenewalApproval;
	}

	public void setPermitRenewalApproval(int permitRenewalApproval) {
		this.permitRenewalApproval = permitRenewalApproval;
	}

	public int getGrantApprovalforAmmendments() {
		return grantApprovalforAmmendments;
	}

	public void setGrantApprovalforAmmendments(int grantApprovalforAmmendments) {
		this.grantApprovalforAmmendments = grantApprovalforAmmendments;
	}

	public int getApproveSurveyProcessRequests() {
		return approveSurveyProcessRequests;
	}

	public void setApproveSurveyProcessRequests(int approveSurveyProcessRequests) {
		this.approveSurveyProcessRequests = approveSurveyProcessRequests;
	}

	public int getApproveTenderAdvertisement() {
		return approveTenderAdvertisement;
	}

	public void setApproveTenderAdvertisement(int approveTenderAdvertisement) {
		this.approveTenderAdvertisement = approveTenderAdvertisement;
	}

	public int getApproveElectedBidder() {
		return approveElectedBidder;
	}

	public void setApproveElectedBidder(int approveElectedBidder) {
		this.approveElectedBidder = approveElectedBidder;
	}

	public int getCommitteeBoardAuthorization() {
		return committeeBoardAuthorization;
	}

	public void setCommitteeBoardAuthorization(int committeeBoardAuthorization) {
		this.committeeBoardAuthorization = committeeBoardAuthorization;
	}

	public int getCommitteeApproval() {
		return committeeApproval;
	}

	public void setCommitteeApproval(int committeeApproval) {
		this.committeeApproval = committeeApproval;
	}

	public int getBoardApproval() {
		return boardApproval;
	}

	public void setBoardApproval(int boardApproval) {
		this.boardApproval = boardApproval;
	}

	public int getGenerateReceipt() {
		return generateReceipt;
	}

	public void setGenerateReceipt(int generateReceipt) {
		this.generateReceipt = generateReceipt;
	}

	public int getCancelledPermits() {
		return cancelledPermits;
	}

	public void setCancelledPermits(int cancelledPermits) {
		this.cancelledPermits = cancelledPermits;
	}

	public int getGrantApprovalforAmmendmentsDirector() {
		return grantApprovalforAmmendmentsDirector;
	}

	public void setGrantApprovalforAmmendmentsDirector(int grantApprovalforAmmendmentsDirector) {
		this.grantApprovalforAmmendmentsDirector = grantApprovalforAmmendmentsDirector;
	}

	public int getGrantApprovalforAmmendmentsChairman() {
		return grantApprovalforAmmendmentsChairman;
	}

	public void setGrantApprovalforAmmendmentsChairman(int grantApprovalforAmmendmentsChairman) {
		this.grantApprovalforAmmendmentsChairman = grantApprovalforAmmendmentsChairman;
	}

	public boolean isDirector() {
		return director;
	}

	public void setDirector(boolean director) {
		this.director = director;
	}

	public boolean isChairman() {
		return chairman;
	}

	public void setChairman(boolean chairman) {
		this.chairman = chairman;
	}

}
