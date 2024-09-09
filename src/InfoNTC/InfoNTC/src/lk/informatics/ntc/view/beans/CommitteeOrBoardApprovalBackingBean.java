package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.CommitteeOrBoardApprovalDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.service.AmendmentService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "committeeOrBoardApprovalBean")
@ViewScoped
public class CommitteeOrBoardApprovalBackingBean implements Serializable {

	private String errorMsg;
	private String sucessMsg;

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private boolean showSurveyCommittee;
	private boolean showSurveyForm;
	private boolean showTenderForm;
	private boolean showAmendmentsForm;
	private boolean showViewBtn;

	private boolean proceedSurveySearch;
	private boolean proceedFurtherSurveySearch;
	private boolean boardApproval;
	private boolean procceedBoardApproval;
	private boolean searchDone;
	private boolean saveDone;
	private boolean showDocumentManagement;
	private boolean disableAfterSave;
	private boolean disableSaveBtn;
	private boolean showCommitteeTable;
	private boolean showSecondCommitteeTable;
	private boolean showBoardTable;

	private boolean showDocPanel;

	private CommitteeOrBoardApprovalDTO committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();

	private List<CommitteeOrBoardApprovalDTO> surveyNoListForCommitteeApproval = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> viewSurveyNoListForCommitteeApprovalStatus = new ArrayList<CommitteeOrBoardApprovalDTO>();

	private List<CommitteeOrBoardApprovalDTO> getDataForCommitteeDetails_01 = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> getDataForCommitteeDetails_02 = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> viewDataForCommitteeDetails = new ArrayList<CommitteeOrBoardApprovalDTO>();

	private List<CommitteeOrBoardApprovalDTO> getDataForBoardDetails_01 = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> getDataForBoardDetails_02 = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> viewDataForBoardDetails = new ArrayList<CommitteeOrBoardApprovalDTO>();

	private List<CommitteeOrBoardApprovalDTO> getDataFor02ndCommitteeDetails_01 = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> getDataFor02ndCommitteeDetails_02 = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> viewDataFor02ndCommitteeDetails = new ArrayList<CommitteeOrBoardApprovalDTO>();

	private List<CommitteeOrBoardApprovalDTO> transactionTypeListForCommitteBoardApproval = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> amendmentsApplicationNoListForCommitteeApproval = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> viewAmendmentsApplicationNoListForCommitteeApproval = new ArrayList<CommitteeOrBoardApprovalDTO>();

	private List<CommitteeOrBoardApprovalDTO> amendmentsPermitNoListForCommitteeApproval = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> committeeBoardApprovalList = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> selectList;
	private List<CommitteeOrBoardApprovalDTO> tenderApplicationNoListForCommitteeApproval = new ArrayList<CommitteeOrBoardApprovalDTO>();
	private List<CommitteeOrBoardApprovalDTO> viewTenderApplicationNoListForCommitteeApproval = new ArrayList<CommitteeOrBoardApprovalDTO>();

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private String date;
	private String approvalType;
	private String refNo;
	private String comType;
	private String typeTrans;
	private String typeCodeTrans;
	private String checkFirstApproval;
	private String checkCommitteeBoardAuthStatus;
	private String selectTransactionType;

	private String committeeMainRemark;
	private String secondCommitteeMainRemark;
	private String boardMainRemark;

	private SurveyService surveyService;
	private CommonService commonService;
	private DocumentManagementService documentManagementService;
	private AmendmentService amendmentService;
	private TenderService tenderService;
	private GamiSariyaService gamiSariyaService;

	private HistoryService historyService;
	private AmendmentDTO amendmentHistoryDTO;

	@PostConstruct
	public void init() {
		Date todaysDate = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String testDateString = df.format(todaysDate);
		setDate(testDateString);

		amendmentService = (AmendmentService) SpringApplicationContex.getBean("amendmentService");
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");

		// amendmentsPermitNoListForCommitteeApproval =
		// amendmentService.getAmendmentsPermitNoForCommitteeApproval();
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");

		transactionTypeListForCommitteBoardApproval = surveyService.getTransactionTypeDropDown();

		setSearchDone(true);
		setShowDocumentManagement(true);
		setDisableSaveBtn(true);

		if (sessionBackingBean.isPrintOffterLetterMode() == true) {
			committeeApprovalDTO.setCommitteeTenderApplicationNo(sessionBackingBean.getTenderApplicationNo());
			committeeApprovalDTO.setTransactionType("TENDER");
			tenderSearch();
		}

		if (sessionBackingBean.isBackCommittee() == true) {

			sessionBackingBean.setBackCommittee(false);
			setDisableSaveBtn(false);
			committeeApprovalDTO.setSelectedTransactionType(sessionBackingBean.getSelectedTransactionType());
			selection();
			committeeApprovalDTO.setCommitteeAmendmentsApplicationNo(sessionBackingBean.getCommitteeApplicationNo());
			generatePermitNo();
			amendmentSearch();
		}

	}

	public void generatePermitNo() {

		setShowDocPanel(false);
		setShowCommitteeTable(false);
		setShowBoardTable(false);
		String permitNo = amendmentService
				.getAmendmentPermitNo(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo());

		if (permitNo != null) {

			committeeApprovalDTO.setCommitteeAmendmentsPermitNo(permitNo);
		} else {

			committeeApprovalDTO.setCommitteeAmendmentsPermitNo(null);
		}

	}

	public void generateTenderRefNo() {

		setShowDocPanel(false);
		setShowCommitteeTable(false);
		setShowBoardTable(false);

		String applicationNo = committeeApprovalDTO.getCommitteeTenderApplicationNo();

		String refNo = tenderService.getRefNo(applicationNo);

		if (refNo != null) {
			committeeApprovalDTO.setCommitteTenderRefNo(refNo);
		} else {

		}

	}

	public void search() {
		setSearchDone(true);
		if (showSurveyForm == true) {

			typeTrans = committeeApprovalDTO.getSelectedTransactionType();
			typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);
			if (typeCodeTrans.equals("08")) {
				surveySearch();
			}

			// added by tharushi.e

			if (typeCodeTrans.equals("19")) {
				gamiSurveySearch();
			}

		}

		if (showAmendmentsForm == true) {

			amendmentSearch();
		}

		if (showTenderForm == true) {

			tenderSearch();

		}

		if (searchDone == false) {

			setDisableSaveBtn(false);
		}

	}

	public void tenderSearch() {

		if (committeeApprovalDTO.getCommitteeTenderApplicationNo() != null
				&& !committeeApprovalDTO.getCommitteeTenderApplicationNo().isEmpty()
				&& !committeeApprovalDTO.getCommitteeTenderApplicationNo().equalsIgnoreCase("")) {

			checkFirstApproval = tenderService
					.getFirstApprovalStatus(committeeApprovalDTO.getCommitteeTenderApplicationNo());
			String checkSecondApproval = tenderService
					.getSecondApprovalStatus(committeeApprovalDTO.getCommitteeTenderApplicationNo());
			String checkBoardApproval = tenderService
					.getBoardApprovalStatus(committeeApprovalDTO.getCommitteeTenderApplicationNo());

			setSearchDone(false);
			if (checkFirstApproval == null) {

				showSurveyCommittee = true;

				comType = "F";

				setApprovalType("First Committee Approval");

				setBoardApproval(false);

				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				if (refNo != null) {
					checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
					if (checkCommitteeBoardAuthStatus == null) {
						errorMsg = "No Committe/Board Assigned";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}

					else if (checkCommitteeBoardAuthStatus.equals("Y")) {
						committeeBoardApprovalList = surveyService.getDataToCommitteBoardApprovalList(refNo);

					}
				} else {

					errorMsg = "No Committe/Board Assigned";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
				commonService.updateTaskStatus(committeeApprovalDTO.getCommitteeTenderApplicationNo(), "TD013", "TD014",
						"C", sessionBackingBean.getLoginUser());

			}

			if (checkFirstApproval != null && checkSecondApproval == null) {

				showSurveyCommittee = true;

				comType = "S";

				setApprovalType("Second Committee Approval");

				setBoardApproval(false);

				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				if (refNo != null) {
					checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
					if (checkCommitteeBoardAuthStatus == null) {
						errorMsg = "No Committe/Board Assigned";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}

					else if (checkCommitteeBoardAuthStatus.equals("Y")) {
						committeeBoardApprovalList = surveyService.getDataToCommitteBoardApprovalList(refNo);

					}
				} else {

					errorMsg = "No Committe/Board Assigned";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
				commonService.updateTaskStatus(committeeApprovalDTO.getCommitteeTenderApplicationNo(), "TD014", "TD015",
						"C", sessionBackingBean.getLoginUser());

			}

			if (checkFirstApproval != null && checkSecondApproval != null
					&& (checkBoardApproval == null || checkBoardApproval.equals("S"))) {

				showSurveyCommittee = true;

				comType = "B";

				setApprovalType("Board Approval");

				setBoardApproval(true);

				refNo = surveyService.getRefNo(typeCodeTrans, comType);

				if (refNo != null) {
					checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
					if (checkCommitteeBoardAuthStatus == null) {
						errorMsg = "No Committe/Board Assigned";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}

					else if (checkCommitteeBoardAuthStatus.equals("Y")) {
						committeeBoardApprovalList = surveyService.getDataToCommitteBoardApprovalList(refNo);

					}
				} else {

					errorMsg = "No Committe/Board Assigned";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
				commonService.updateTaskStatus(committeeApprovalDTO.getCommitteeTenderApplicationNo(), "TD016", "TD017",
						"C", sessionBackingBean.getLoginUser());

			}

		}

		else {

			errorMsg = "Application No. or Permit No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void amendmentSearch() {

		if (committeeApprovalDTO.getCommitteeAmendmentsApplicationNo() != null
				&& !committeeApprovalDTO.getCommitteeAmendmentsApplicationNo().isEmpty()
				&& !committeeApprovalDTO.getCommitteeAmendmentsApplicationNo().equalsIgnoreCase("")) {

			String checkApproval = amendmentService
					.getApprovalStatus(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo());
			String checkBoardApproval = amendmentService
					.getBoardApprovalStatus(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo());

			if (checkBoardApproval == null) {
				checkBoardApproval = "N";
			} else {

			}

			if (checkApproval.equals("P")) {

				showSurveyCommittee = true;
				String currentTask = "O";
				String taskCode = "AM101";
				comType = "C";

				setShowViewBtn(true);
				setApprovalType("Committee Approval");

				setBoardApproval(false);

				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				if (refNo != null) {
					checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
					if (checkCommitteeBoardAuthStatus == null) {
						errorMsg = "No Committe/Board Assigned";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}

					else if (checkCommitteeBoardAuthStatus.equals("Y")) {
						committeeBoardApprovalList = surveyService.getDataToCommitteBoardApprovalList(refNo);

					}
				} else {

					errorMsg = "No Committe/Board Assigned";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

				commonService.updateTaskStatus(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(), "AM100",
						"AM101", "C", sessionBackingBean.getLoginUser());
				setSearchDone(false);

			}

			else if (checkBoardApproval.equals("P")) {

				String currentTask = "O";
				String taskCode = "AM102";
				comType = "B";

				setShowViewBtn(true);
				setApprovalType("Board Approval");

				setBoardApproval(true);

				refNo = surveyService.getRefNo(typeCodeTrans, comType);

				if (refNo != null) {
					checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
					if (checkCommitteeBoardAuthStatus == null) {
						errorMsg = "No Committe/Board Assigned";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}

					else if (checkCommitteeBoardAuthStatus.equals("Y")) {
						committeeBoardApprovalList = surveyService.getDataToCommitteBoardApprovalList(refNo);

					}
				} else {

					errorMsg = "No Committe/Board Assigned";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

				commonService.updateTaskStatus(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(), "AM101",
						"AM102", "C", sessionBackingBean.getLoginUser());
				setSearchDone(false);
			} else {

				errorMsg = "Committee/Board Approval should be requested";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		}

		else {

			errorMsg = "Application No. or Permit No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	/*
	 * public String viewAmendment() {
	 * 
	 * if (committeeApprovalDTO.getCommitteeAmendmentsApplicationNo() != null &&
	 * !committeeApprovalDTO.getCommitteeAmendmentsApplicationNo().isEmpty() &&
	 * !committeeApprovalDTO.getCommitteeAmendmentsApplicationNo().
	 * equalsIgnoreCase( "")) {
	 * 
	 * HttpServletRequest request = (HttpServletRequest)
	 * FacesContext.getCurrentInstance().getExternalContext() .getRequest();
	 * sessionBackingBean.setPageMode("V");
	 * sessionBackingBean.setApproveURL(request.getRequestURL().toString());
	 * sessionBackingBean.setSearchURL(null);
	 * sessionBackingBean.setApproveURLStatus(true);
	 * sessionBackingBean.setAmendmentsApplicationNo(committeeApprovalDTO.
	 * getCommitteeAmendmentsApplicationNo());
	 * 
	 * sessionBackingBean.amendmentsViewMood = true;
	 * 
	 * return
	 * "/pages/amendmentsToBusOrOwner/viewBusOwnerServiceAmendment.xhtml#!";
	 * 
	 * } else {
	 * 
	 * errorMsg = "Application No. should be selected.";
	 * RequestContext.getCurrentInstance().update("frmrequiredField");
	 * RequestContext.getCurrentInstance().execute("PF('requiredField').show()")
	 * ;
	 * 
	 * } return null; }
	 */

	// added by tharushi.e
	public void gamiSurveySearch() {

		if (committeeApprovalDTO.getCommitteeSurveyNo() != null
				&& !committeeApprovalDTO.getCommitteeSurveyNo().isEmpty()
				&& !committeeApprovalDTO.getCommitteeSurveyNo().equalsIgnoreCase("")) {

			String requestNo = committeeApprovalDTO.getRequestNo();
			String surveyNo = committeeApprovalDTO.getCommitteeSurveyNo();

			//Comment by Dinushi on 12/11/2021 to Remove committee approval from Traffic Proposal
			// String task1 = surveyService.getTaskStatusForApproval(requestNo);
			// String task2 =
			// surveyService.getBoardTaskStatusForApproval(requestNo);

			/*String checkApproval = surveyService.checkCommitteeApprovalForGamiSariya(surveyNo);
			setSearchDone(false);

			if (checkApproval == null) {

				showSurveyCommittee = true;
				String currentTask = "O";
				String taskCode = "SU010";
				comType = "C";

				setApprovalType("Committee Approval");

				setBoardApproval(false);

				commonService.updateSurveyTaskDetails(requestNo, surveyNo, taskCode, currentTask,
						sessionBackingBean.getLoginUser());
				checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
				refNo = surveyService.getRefNo(typeCodeTrans, comType);

				if (refNo != null) {
					checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
					if (checkCommitteeBoardAuthStatus == null) {
						errorMsg = "No Committe/Board Assigned";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}

					else if (checkCommitteeBoardAuthStatus.equals("Y")) {
						committeeBoardApprovalList = surveyService.getDataToCommitteBoardApprovalList(refNo);

					}
				} else {

					errorMsg = "No Committe/Board Assigned";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

				// Display the list in the data table

			} else {*/
				setSearchDone(false);
				String currentTask = "O";
				String taskCode = "SU011";
				comType = "B";

				setBoardApproval(true);

				setApprovalType("Board Approval");

				commonService.updateSurveyTaskDetails(requestNo, surveyNo, taskCode, currentTask,
						sessionBackingBean.getLoginUser());
				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				if (refNo != null) {
					checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
					if (checkCommitteeBoardAuthStatus == null) {
						errorMsg = "No Committe/Board Assigned";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}

					else if (checkCommitteeBoardAuthStatus.equals("Y")) {
						committeeBoardApprovalList = surveyService.getDataToCommitteBoardApprovalList(refNo);

					}
				} else {

					errorMsg = "No Board is Assigned";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			/*}*/

		} else {

			errorMsg = "Survey No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	// finished
	public void surveySearch() {

		if (committeeApprovalDTO.getCommitteeSurveyNo() != null
				&& !committeeApprovalDTO.getCommitteeSurveyNo().isEmpty()
				&& !committeeApprovalDTO.getCommitteeSurveyNo().equalsIgnoreCase("")) {

			String requestNo = committeeApprovalDTO.getRequestNo();
			String surveyNo = committeeApprovalDTO.getCommitteeSurveyNo();

			// String task1 = surveyService.getTaskStatusForApproval(requestNo);
			// String task2 =
			// surveyService.getBoardTaskStatusForApproval(requestNo);

			String checkApproval = surveyService.checkCommitteeApproval(requestNo);
			setSearchDone(false);

			if (checkApproval == null) {

				showSurveyCommittee = true;
				String currentTask = "O";
				String taskCode = "SU010";
				comType = "C";

				setApprovalType("Committee Approval");

				setBoardApproval(false);

				commonService.updateSurveyTaskDetails(requestNo, surveyNo, taskCode, currentTask,
						sessionBackingBean.getLoginUser());
				checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
				refNo = surveyService.getRefNo(typeCodeTrans, comType);

				if (refNo != null) {
					checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
					if (checkCommitteeBoardAuthStatus == null) {
						errorMsg = "No Committe/Board Assigned";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}

					else if (checkCommitteeBoardAuthStatus.equals("Y")) {
						committeeBoardApprovalList = surveyService.getDataToCommitteBoardApprovalList(refNo);

					}
				} else {

					errorMsg = "No Committe/Board Assigned";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

				// Display the list in the data table

			} else {

				String currentTask = "O";
				String taskCode = "SU011";
				comType = "B";

				setBoardApproval(true);

				setApprovalType("Board Approval");

				commonService.updateSurveyTaskDetails(requestNo, surveyNo, taskCode, currentTask,
						sessionBackingBean.getLoginUser());
				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				if (refNo != null) {
					checkCommitteeBoardAuthStatus = surveyService.checkAuthStatus(refNo);
					if (checkCommitteeBoardAuthStatus == null) {
						errorMsg = "No Committe/Board Assigned";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}

					else if (checkCommitteeBoardAuthStatus.equals("Y")) {
						committeeBoardApprovalList = surveyService.getDataToCommitteBoardApprovalList(refNo);

					}
				} else {

					errorMsg = "No Committe/Board Assigned";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			}

		} else {

			errorMsg = "Survey No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}
	}

	public void generateTypeAndMethod() {

		setShowDocPanel(false);
		setShowCommitteeTable(false);
		setShowBoardTable(false);
		typeTrans = committeeApprovalDTO.getSelectedTransactionType();
		typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);

		String surveyNo = committeeApprovalDTO.getCommitteeSurveyNo();

		String surveyType = surveyService.getSurveyType(surveyNo);
		String surveyMethod = surveyService.getSurveyMethod(surveyNo);
		String requestNo = surveyService.getRequestNo(surveyNo);
		String gamiRequestNo = surveyService.getGamiRequestNo(surveyNo);
		String gamiSurveyType = surveyService.getGamiSurveyType(surveyNo);
		String gamiSurveyMethod = surveyService.getGamiSurveyMethod(surveyNo);

		if (surveyType == null && surveyMethod == null && gamiSurveyType == null && gamiSurveyMethod == null) {

		} else {

			// added by tharushi.e for gami sariya development
			if (typeCodeTrans.equals("19")) {

				committeeApprovalDTO.setCommitteSurveyType(gamiSurveyType);
				committeeApprovalDTO.setCommitteeSurveyMethod(gamiSurveyMethod);
				committeeApprovalDTO.setRequestNo(gamiRequestNo);

			} else {

				committeeApprovalDTO.setCommitteSurveyType(surveyType);
				committeeApprovalDTO.setCommitteeSurveyMethod(surveyMethod);
				committeeApprovalDTO.setRequestNo(requestNo);

			}

		}
	}

	public void selection() {

		setShowDocPanel(false);
		setShowDocumentManagement(true);
		typeTrans = committeeApprovalDTO.getSelectedTransactionType();
		typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);

		setDisableAfterSave(false);

		if (typeCodeTrans.equals("08")) {
			showSurveyForm = true;
			showTenderForm = false;
			showAmendmentsForm = false;
			surveyNoListForCommitteeApproval = surveyService.getSurveyNoForCommitteeApproval();
			viewSurveyNoListForCommitteeApprovalStatus = surveyService.getViewSurveyNoForCommitteeApprovalStatus();

			committeeApprovalDTO.setCommitteeSurveyNo(null);
			committeeApprovalDTO.setCommitteeSurveyMethod(null);
			committeeApprovalDTO.setCommitteSurveyType(null);
			showCommitteeTable = false;
			showBoardTable = false;
			showSecondCommitteeTable = false;
		}

		if (typeCodeTrans.equals("19")) {
			showSurveyForm = true;
			showTenderForm = false;
			showAmendmentsForm = false;
			surveyNoListForCommitteeApproval = surveyService.getGamiSurveyNoForCommitteeApproval();
			viewSurveyNoListForCommitteeApprovalStatus = surveyService.getGamiViewSurveyNoForCommitteeApprovalStatus();

			committeeApprovalDTO.setCommitteeSurveyNo(null);
			committeeApprovalDTO.setCommitteeSurveyMethod(null);
			committeeApprovalDTO.setCommitteSurveyType(null);
			showCommitteeTable = false;
			showBoardTable = false;
			showSecondCommitteeTable = false;
		}

		if (typeCodeTrans.equals("01")) {
			showTenderForm = true;
			showSurveyForm = false;
			showAmendmentsForm = false;
			tenderApplicationNoListForCommitteeApproval = tenderService.getTenderApplicationNoForCommitteeApproval();
			viewTenderApplicationNoListForCommitteeApproval = tenderService
					.getViewTenderApplicationNoForCommitteeApproval();

			committeeApprovalDTO.setCommitteeTenderApplicationNo(null);
			committeeApprovalDTO.setCommitteTenderRefNo(null);

			showCommitteeTable = false;
			showBoardTable = false;
			showSecondCommitteeTable = false;
		}

		if (typeCodeTrans.equals("21") || typeCodeTrans.equals("22") || typeCodeTrans.equals("23")
				|| typeCodeTrans.equals("14") || typeCodeTrans.equals("15") || typeCodeTrans.equals("16")) {

			amendmentsApplicationNoListForCommitteeApproval = amendmentService
					.getAmendmentsApplicationNoForCommitteeApproval(typeCodeTrans);
			viewAmendmentsApplicationNoListForCommitteeApproval = amendmentService
					.getViewAmendmentsNoForCommitteeApprovalStatus(typeCodeTrans);
			showCommitteeTable = false;
			showBoardTable = false;
			showSecondCommitteeTable = false;
			committeeApprovalDTO.setCommitteeAmendmentsApplicationNo(null);
			committeeApprovalDTO.setCommitteeAmendmentsPermitNo(null);
			showAmendmentsForm = true;
			showSurveyForm = false;
			showTenderForm = false;

		}

	}

	public void save() {
		setSaveDone(false);
		if (showSurveyForm == true) {
			typeTrans = committeeApprovalDTO.getSelectedTransactionType();
			typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);
			if (typeCodeTrans.equals("08")) {
				surverySave();
			}
			// added by tharushi.e for gami sariya
			if (typeCodeTrans.equals("19")) {
				gamisariyaSave();
			}
		}

		if (showAmendmentsForm == true) {

			amendmentSave();
		}

		if (showTenderForm == true) {

			tenderSave();

		}

		if (saveDone == true) {

			setDisableSaveBtn(true);
			setShowDocumentManagement(false);
			setDisableAfterSave(true);
		} else {

			setShowDocumentManagement(true);
		}

	}

	public void tenderSave() {

		int i = 0;
		int j = 0;

		if (isBoardApproval() == true) {

			procceedBoardApproval = true;
			proceedSurveySearch = true;
		}

		// validation Reject/Approve process
		for (int a = 0; a < committeeBoardApprovalList.size(); a++) {

			String status = committeeBoardApprovalList.get(a).getApproveRejectStatus();
			String remark = committeeBoardApprovalList.get(a).getSelectedRemark();

			if (status.equals("S")) {
				errorMsg = "Approve/Reject Status not Selected";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				proceedSurveySearch = false;
				break;

			}

			if (status.equals("R") && remark.isEmpty() && remark.equalsIgnoreCase("")) {

				errorMsg = "Remark is compulsory if the status is reject";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				proceedSurveySearch = false;
				break;
			} else {

				proceedSurveySearch = true;
			}

			if (status.equals("A")) {
				i++;
				proceedSurveySearch = true;

			}

			if (status.equals("R")) {
				j++;
				proceedSurveySearch = true;
			}

		}

		if (proceedSurveySearch == true && procceedBoardApproval == true && i < j) {

			if (committeeApprovalDTO.getMainRemark().isEmpty()
					|| committeeApprovalDTO.getMainRemark().equalsIgnoreCase("")) {

				errorMsg = "Remark should be entered";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				proceedSurveySearch = false;
				procceedBoardApproval = false;
			}
		}

		if (proceedSurveySearch == true) {

			String applicationNo = committeeApprovalDTO.getCommitteeTenderApplicationNo();
			String remark = committeeApprovalDTO.getMainRemark();
			String user = sessionBackingBean.loginUser;
			String status = "P";

			int result = surveyService.insertCommitteeApprovalData(applicationNo, refNo, remark, status, user,
					committeeApprovalDTO);

			if (result == 0) {

				proceedFurtherSurveySearch = true;

			} else {

			}

		}

		if (proceedSurveySearch == true && proceedFurtherSurveySearch == true) {
			int result = 1;
			long seqNo = committeeApprovalDTO.getCommitteApprovalSeqNo();
			String user = sessionBackingBean.loginUser;
			for (int a = 0; a < committeeBoardApprovalList.size(); a++) {

				String userName = committeeBoardApprovalList.get(a).getUserName();
				String status = committeeBoardApprovalList.get(a).getApproveRejectStatus();
				String date = committeeBoardApprovalList.get(a).getTransactionDate();
				String remark = committeeBoardApprovalList.get(a).getSelectedRemark();

				if (userName == null) {
					result = surveyService.insertCommitteeApprovalDetailDataNoUserName(seqNo, status, date, remark,
							user);
				} else {
					result = surveyService.insertCommitteeApprovalDetailData(seqNo, userName, status, date, remark,
							user);
				}
			}

			// first committee approval
			if (result == 0 && isBoardApproval() == false && checkFirstApproval == null) {

				String remark = committeeApprovalDTO.getMainRemark();

				commonService.updateTaskStatusCompleted(committeeApprovalDTO.getCommitteeTenderApplicationNo(), "TD014",
						sessionBackingBean.getLoginUser());

				int result1 = tenderService
						.insertFirstApproveStatus(committeeApprovalDTO.getCommitteeTenderApplicationNo(), remark, user);

				if (result1 == 0) {
					setSaveDone(true);
					sucessMsg = "First Committee Approved Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {

				}

			}
			// second committee approval
			if (result == 0 && isBoardApproval() == false && checkFirstApproval != null) {

				String remark = committeeApprovalDTO.getMainRemark();

				commonService.updateTaskStatusCompleted(committeeApprovalDTO.getCommitteeTenderApplicationNo(), "TD015",
						sessionBackingBean.getLoginUser());
				// int result1 = surveyService.insertApproveStatus(requestNo,
				// proccessStatus,
				// user,remark);

				int result1 = tenderService.insertSecondApproveStatus(
						committeeApprovalDTO.getCommitteeTenderApplicationNo(), remark, user);

				if (result1 == 0) {
					setSaveDone(true);

					sucessMsg = "Second Committee Approved Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {

				}

			}

			if (result == 0 && isBoardApproval() == true) {

				commonService.updateTaskStatusCompleted(committeeApprovalDTO.getCommitteeTenderApplicationNo(), "TD017",
						sessionBackingBean.getLoginUser());

				int check = surveyService.checkBoardApproveStatus(seqNo);

				if (check == 0) {

					String remark = committeeApprovalDTO.getMainRemark();
					String proccessStatus = "A";

					int result1 = tenderService.insertBoardStatus(
							committeeApprovalDTO.getCommitteeTenderApplicationNo(), proccessStatus, remark, user);
					setSaveDone(true);
					sucessMsg = "Board Approved Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				}

				if (check == 1) {
					String proccessStatus = "R";
					String remark = committeeApprovalDTO.getMainRemark();

					int result1 = tenderService.insertBoardStatus(
							committeeApprovalDTO.getCommitteeTenderApplicationNo(), proccessStatus, remark, user);
					setSaveDone(true);
					sucessMsg = "Board Rejected Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				}

			}

		}

	}

	public void amendmentSave() {

		int i = 0;
		int j = 0;

		if (isBoardApproval() == true) {

			procceedBoardApproval = true;
			proceedSurveySearch = true;
		}

		// validation Reject/Approve process
		for (int a = 0; a < committeeBoardApprovalList.size(); a++) {

			String status = committeeBoardApprovalList.get(a).getApproveRejectStatus();
			String remark = committeeBoardApprovalList.get(a).getSelectedRemark();

			if (status.equals("S")) {
				errorMsg = "Approve/Reject Status not Selected";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				proceedSurveySearch = false;
				break;

			}

			if (status.equals("R") && remark.isEmpty() && remark.equalsIgnoreCase("")) {

				errorMsg = "Remark is compulsory if the status is reject";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				proceedSurveySearch = false;
				break;
			} else {

				proceedSurveySearch = true;
			}

			if (status.equals("A")) {
				i++;
				proceedSurveySearch = true;

			}

			if (status.equals("R")) {
				j++;
				proceedSurveySearch = true;
			}

		}

		if (proceedSurveySearch == true && procceedBoardApproval == true && i < j) {

			if (committeeApprovalDTO.getMainRemark().isEmpty()
					|| committeeApprovalDTO.getMainRemark().equalsIgnoreCase("")) {

				errorMsg = "Remark should be entered";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				proceedSurveySearch = false;
				procceedBoardApproval = false;
			}
		}

		if (proceedSurveySearch == true) {

			String applicationNo = committeeApprovalDTO.getCommitteeAmendmentsApplicationNo();
			String remark = committeeApprovalDTO.getMainRemark();
			String user = sessionBackingBean.loginUser;
			String status = "P";

			int result = surveyService.insertCommitteeApprovalData(applicationNo, refNo, remark, status, user,
					committeeApprovalDTO);

			if (result == 0) {

				proceedFurtherSurveySearch = true;

			} else {

			}

		}

		if (proceedSurveySearch == true && proceedFurtherSurveySearch == true) {
			int result = 1;
			long seqNo = committeeApprovalDTO.getCommitteApprovalSeqNo();
			String user = sessionBackingBean.loginUser;
			for (int a = 0; a < committeeBoardApprovalList.size(); a++) {

				String userName = committeeBoardApprovalList.get(a).getUserName();
				String status = committeeBoardApprovalList.get(a).getApproveRejectStatus();
				String date = committeeBoardApprovalList.get(a).getTransactionDate();
				String remark = committeeBoardApprovalList.get(a).getSelectedRemark();

				if (userName == null) {
					result = surveyService.insertCommitteeApprovalDetailDataNoUserName(seqNo, status, date, remark,
							user);
				} else {
					result = surveyService.insertCommitteeApprovalDetailData(seqNo, userName, status, date, remark,
							user);
				}
			}

			if (result == 0 && isBoardApproval() == false) {

				String remark = committeeApprovalDTO.getMainRemark();

				commonService.updateTaskStatusCompleted(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(),
						"AM101", sessionBackingBean.getLoginUser());
				// int result1 = surveyService.insertApproveStatus(requestNo,
				// proccessStatus,
				// user,remark);

				/* Update Amendment History Table Data */
				amendmentHistoryDTO = historyService.getAmendmentTableData(
						committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(), sessionBackingBean.getLoginUser());
				int result1 = amendmentService
						.insertApproveStatus(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(), remark, user);

				if (result1 == 0) {
					setSaveDone(true);
					sucessMsg = "Committee Approved Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					setShowViewBtn(false);
					if (typeCodeTrans.equals("21") || typeCodeTrans.equals("22") || typeCodeTrans.equals("23")) {

						/**
						 * added for update que master table task by tharushi.e
						 */

						commonService.updateQueMasterTableTask(
								committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(), "AM101", "C");
						/**
						 * end by tharushi.e
						 */
					}

					historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
				} else {

				}

			}

			if (result == 0 && isBoardApproval() == true) {

				commonService.updateTaskStatusCompleted(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(),
						"AM102", sessionBackingBean.getLoginUser());

				int check = surveyService.checkBoardApproveStatus(seqNo);

				if (check == 0) {

					String proccessStatus = "A";

					// int result1 =
					// amendmentService.insertBoardApproveStatus(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(),
					// proccessStatus, user);
					String remark = committeeApprovalDTO.getMainRemark();

					/* Update Amendment History Table Data */
					amendmentHistoryDTO = historyService.getAmendmentTableData(
							committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(),
							sessionBackingBean.getLoginUser());

					int result1 = amendmentService.insertBoardRejectStatus(
							committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(), proccessStatus, remark, user);
					historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);

					setSaveDone(true);
					sucessMsg = "Board Approved Successfully";
					if (typeCodeTrans.equals("21") || typeCodeTrans.equals("22") || typeCodeTrans.equals("23")) {

						/**
						 * added for update que master table task by tharushi.e
						 */

						commonService.updateQueMasterTableTask(
								committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(), "AM102", "C");
						/**
						 * end by tharushi.e
						 */
					}
					setShowViewBtn(false);
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				}

				if (check == 1) {
					String proccessStatus = "R";
					String remark = committeeApprovalDTO.getMainRemark();

					/* Update Amendment History Table Data */
					amendmentHistoryDTO = historyService.getAmendmentTableData(
							committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(),
							sessionBackingBean.getLoginUser());

					int result1 = amendmentService.insertBoardRejectStatus(
							committeeApprovalDTO.getCommitteeAmendmentsApplicationNo(), proccessStatus, remark, user);
					historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
					setSaveDone(true);
					sucessMsg = "Board Rejected Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					setShowViewBtn(false);
				}

			}

		}

	}

	// added by tharushi.e for gami sariya
	public void gamisariyaSave() {

		int i = 0;
		int j = 0;

		if (isBoardApproval() == true) {

			procceedBoardApproval = true;
			proceedSurveySearch = true;
		}

		// validation Reject/Approve process
		for (int a = 0; a < committeeBoardApprovalList.size(); a++) {

			String status = committeeBoardApprovalList.get(a).getApproveRejectStatus();
			String remark = committeeBoardApprovalList.get(a).getSelectedRemark();

			if (status.equals("S")) {
				errorMsg = "Approve/Reject Status not Selected";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				proceedSurveySearch = false;
				break;

			}

			if (status.equals("R") && remark.isEmpty() && remark.equalsIgnoreCase("")) {

				errorMsg = "Remark is compulsory if the status is reject";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				proceedSurveySearch = false;
				break;
			} else {

				proceedSurveySearch = true;
			}

			if (status.equals("A")) {
				i++;
				proceedSurveySearch = true;

			}

			if (status.equals("R")) {
				j++;
				proceedSurveySearch = true;
			}

		}

		if (proceedSurveySearch == true && procceedBoardApproval == true && i < j) {

			if (committeeApprovalDTO.getMainRemark().isEmpty()
					|| committeeApprovalDTO.getMainRemark().equalsIgnoreCase("")) {

				errorMsg = "Remark should be entered";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				proceedSurveySearch = false;
				procceedBoardApproval = false;
			}
		}

		if (proceedSurveySearch == true) {

			String surveyNo = committeeApprovalDTO.getCommitteeSurveyNo();
			String remark = committeeApprovalDTO.getMainRemark();
			String user = sessionBackingBean.loginUser;
			String status = "P";

			int result = surveyService.insertCommitteeApprovalData(surveyNo, refNo, remark, status, user,
					committeeApprovalDTO);

			if (result == 0) {

				proceedFurtherSurveySearch = true;

			} else {

			}

		}

		// Insert table data into database
		if (proceedSurveySearch == true && proceedFurtherSurveySearch == true) {
			int result = 1;
			long seqNo = committeeApprovalDTO.getCommitteApprovalSeqNo();
			String user = sessionBackingBean.loginUser;
			for (int a = 0; a < committeeBoardApprovalList.size(); a++) {

				String userName = committeeBoardApprovalList.get(a).getUserName();
				String status = committeeBoardApprovalList.get(a).getApproveRejectStatus();
				String date = committeeBoardApprovalList.get(a).getTransactionDate();
				String remark = committeeBoardApprovalList.get(a).getSelectedRemark();

				if (userName == null) {
					result = surveyService.insertCommitteeApprovalDetailDataNoUserName(seqNo, status, date, remark,
							user);
				} else {
					result = surveyService.insertCommitteeApprovalDetailData(seqNo, userName, status, date, remark,
							user);
				}

			}

			if (result == 0 && isBoardApproval() == false) {

				String requestNo = committeeApprovalDTO.getRequestNo();
				String surveyNo = committeeApprovalDTO.getCommitteeSurveyNo();

				String currentTask = "C";
				String taskCode = "SU010";
				comType = "C";

				String proccessStatus = "A";
				String remark = committeeApprovalDTO.getMainRemark();

				commonService.updateSurveyTaskDetails(requestNo, surveyNo, taskCode, currentTask,
						sessionBackingBean.getLoginUser());
				int result1 = surveyService.insertApproveStatusInGamisariya(surveyNo, proccessStatus, user, remark);

				if (result1 == 0) {

					setSaveDone(true);
					sucessMsg = "Committee Approved Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {

				}

			}

			if (result == 0 && isBoardApproval() == true) {

				String requestNo = committeeApprovalDTO.getRequestNo();
				String surveyNo = committeeApprovalDTO.getCommitteeSurveyNo();

				String currentTask = "C";
				String taskCode = "SU011";
				comType = "B";

				String proccessStatus;

				commonService.updateSurveyTaskDetails(requestNo, surveyNo, taskCode, currentTask,
						sessionBackingBean.getLoginUser());

				int check = surveyService.checkBoardApproveStatus(seqNo);

				if (check == 0) {

					proccessStatus = "A";
					String remark = committeeApprovalDTO.getMainRemark();

					// int result1 =
					// surveyService.insertBoardApproveStatus(requestNo,
					// proccessStatus, user);
					int result1 = surveyService.insertBoardRejectStatusInGamiSariya(surveyNo, proccessStatus, user,
							remark);

					setSaveDone(true);
					sucessMsg = "Board Approved Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				}

				if (check == 1) {
					proccessStatus = "R";
					String remark = committeeApprovalDTO.getMainRemark();

					int result1 = surveyService.insertBoardRejectStatusInGamiSariya(surveyNo, proccessStatus, user,
							remark);

					setSaveDone(true);
					sucessMsg = "Board Rejected Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				}

			}
		}

	}

	// finished gami sariya
	public void surverySave() {

		int i = 0;
		int j = 0;

		if (isBoardApproval() == true) {

			procceedBoardApproval = true;
			proceedSurveySearch = true;
		}

		// validation Reject/Approve process
		for (int a = 0; a < committeeBoardApprovalList.size(); a++) {

			String status = committeeBoardApprovalList.get(a).getApproveRejectStatus();
			String remark = committeeBoardApprovalList.get(a).getSelectedRemark();

			if (status.equals("S")) {
				errorMsg = "Approve/Reject Status not Selected";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				proceedSurveySearch = false;
				break;

			}

			if (status.equals("R") && remark.isEmpty() && remark.equalsIgnoreCase("")) {

				errorMsg = "Remark is compulsory if the status is reject";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				proceedSurveySearch = false;
				break;
			} else {

				proceedSurveySearch = true;
			}

			if (status.equals("A")) {
				i++;
				proceedSurveySearch = true;

			}

			if (status.equals("R")) {
				j++;
				proceedSurveySearch = true;
			}

		}

		if (proceedSurveySearch == true && procceedBoardApproval == true && i < j) {

			if (committeeApprovalDTO.getMainRemark().isEmpty()
					|| committeeApprovalDTO.getMainRemark().equalsIgnoreCase("")) {

				errorMsg = "Remark should be entered";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				proceedSurveySearch = false;
				procceedBoardApproval = false;
			}
		}

		if (proceedSurveySearch == true) {

			String surveyNo = committeeApprovalDTO.getCommitteeSurveyNo();
			String remark = committeeApprovalDTO.getMainRemark();
			String user = sessionBackingBean.loginUser;
			String status = "P";

			int result = surveyService.insertCommitteeApprovalData(surveyNo, refNo, remark, status, user,
					committeeApprovalDTO);

			if (result == 0) {

				proceedFurtherSurveySearch = true;

			} else {

			}

		}

		// Insert table data into database
		if (proceedSurveySearch == true && proceedFurtherSurveySearch == true) {
			int result = 1;
			long seqNo = committeeApprovalDTO.getCommitteApprovalSeqNo();
			String user = sessionBackingBean.loginUser;
			for (int a = 0; a < committeeBoardApprovalList.size(); a++) {

				String userName = committeeBoardApprovalList.get(a).getUserName();
				String status = committeeBoardApprovalList.get(a).getApproveRejectStatus();
				String date = committeeBoardApprovalList.get(a).getTransactionDate();
				String remark = committeeBoardApprovalList.get(a).getSelectedRemark();

				if (userName == null) {
					result = surveyService.insertCommitteeApprovalDetailDataNoUserName(seqNo, status, date, remark,
							user);
				} else {
					result = surveyService.insertCommitteeApprovalDetailData(seqNo, userName, status, date, remark,
							user);
				}

			}

			if (result == 0 && isBoardApproval() == false) {

				String requestNo = committeeApprovalDTO.getRequestNo();
				String surveyNo = committeeApprovalDTO.getCommitteeSurveyNo();

				String currentTask = "C";
				String taskCode = "SU010";
				comType = "C";

				String proccessStatus = "A";
				String remark = committeeApprovalDTO.getMainRemark();

				commonService.updateSurveyTaskDetails(requestNo, surveyNo, taskCode, currentTask,
						sessionBackingBean.getLoginUser());
				int result1 = surveyService.insertApproveStatus(requestNo, proccessStatus, user, remark);

				if (result1 == 0) {

					setSaveDone(true);
					sucessMsg = "Committee Approved Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {

				}

			}

			if (result == 0 && isBoardApproval() == true) {

				String requestNo = committeeApprovalDTO.getRequestNo();
				String surveyNo = committeeApprovalDTO.getCommitteeSurveyNo();

				String currentTask = "C";
				String taskCode = "SU011";
				comType = "B";

				String proccessStatus;

				commonService.updateSurveyTaskDetails(requestNo, surveyNo, taskCode, currentTask,
						sessionBackingBean.getLoginUser());

				int check = surveyService.checkBoardApproveStatus(seqNo);

				if (check == 0) {

					proccessStatus = "A";
					String remark = committeeApprovalDTO.getMainRemark();

					// int result1 =
					// surveyService.insertBoardApproveStatus(requestNo,
					// proccessStatus, user);
					int result1 = surveyService.insertBoardRejectStatus(requestNo, proccessStatus, user, remark);

					setSaveDone(true);
					sucessMsg = "Board Approved Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				}

				if (check == 1) {
					proccessStatus = "R";
					String remark = committeeApprovalDTO.getMainRemark();

					int result1 = surveyService.insertBoardRejectStatus(requestNo, proccessStatus, user, remark);

					setSaveDone(true);
					sucessMsg = "Board Rejected Successfully";
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				}

			}
		}

	}

	public void clear() {

		committeeBoardApprovalList.clear();
		committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();
		setShowViewBtn(false);
		setSearchDone(true);
		setApprovalType(null);
		setShowSurveyForm(false);
		setShowAmendmentsForm(false);
		setShowTenderForm(false);
		setShowDocumentManagement(true);
		setDisableSaveBtn(true);

	}

	public void cancel() {

		setShowDocumentManagement(true);
		committeeBoardApprovalList.clear();
		committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();
		setSearchDone(true);
		setDisableAfterSave(false);
		setDisableSaveBtn(true);
		setShowSurveyForm(false);
		setShowAmendmentsForm(false);
		setShowTenderForm(false);
		setApprovalType(null);
	}

	public void documentManagement() {

		if (showSurveyForm == true) {

			typeTrans = committeeApprovalDTO.getSelectedTransactionType();
			typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);

			if (typeCodeTrans.equals("08")) {
				try {

					sessionBackingBean.setEmpNo(committeeApprovalDTO.getRequestNo());
					sessionBackingBean.setEmpTransaction("SURVEY");

					mandatoryList = documentManagementService.mandatoryDocsForUserManagement("08",
							committeeApprovalDTO.getRequestNo());
					optionalList = documentManagementService.optionalDocsForUserManagement("08",
							committeeApprovalDTO.getRequestNo());

					sessionBackingBean.surveyMandatoryDocumentList = documentManagementService
							.surveyMandatoryList(committeeApprovalDTO.getRequestNo());
					sessionBackingBean.surveyOptionalDocumentList = documentManagementService
							.surveyOptionalList(committeeApprovalDTO.getRequestNo());

					RequestContext.getCurrentInstance().execute("PF('uploadSurveyDocument').show()");

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (typeCodeTrans.equals("19")) {

				String requestNo = gamiSariyaService.getGamiRequestNo(committeeApprovalDTO.getCommitteeSurveyNo());
				String surveyRequestNo = gamiSariyaService
						.getGamiSurveyRequestNo(committeeApprovalDTO.getCommitteeSurveyNo());
				sessionBackingBean.setServiceNoForSisuSariya(committeeApprovalDTO.getCommitteeSurveyNo());
				sessionBackingBean.setServiceRefNo(surveyRequestNo);
				sessionBackingBean.setRequestNoForSisuSariya(requestNo);

				sessionBackingBean.setTransactionType("GAMI SARIYA");

				mandatoryList = documentManagementService.mandatoryDocsForSisuSariyaAgreementRenewals("19",
						committeeApprovalDTO.getCommitteeSurveyNo());
				optionalList = documentManagementService.optionalDocsForSisuSariyaAgreementRenewals("19",
						committeeApprovalDTO.getCommitteeSurveyNo());

				sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
						.gamiSariyaMandatoryList(requestNo);
				sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
						.gamiSariyaOptionalList(requestNo);

				if (surveyRequestNo != null && requestNo != null) {

					sessionBackingBean.sisuSariyaServiceRefMandatoryDocumentList = documentManagementService
							.gamiSariyaSurveyRequestMandatoryList(requestNo, surveyRequestNo);
					sessionBackingBean.sisuSariyaServiceRefOptionalDocumentList = documentManagementService
							.gamiSariyaSurveyRequestOptionalList(requestNo, surveyRequestNo);
				}

				if (committeeApprovalDTO.getCommitteeSurveyNo() != null && surveyRequestNo != null
						&& requestNo != null) {

					sessionBackingBean.sisuSariyaServiceMandatoryDocumentList = documentManagementService
							.gamiSariyaSurveyMandatoryList(requestNo, surveyRequestNo,
									committeeApprovalDTO.getCommitteeSurveyNo());
					sessionBackingBean.sisuSariyaServiceOptionalDocumentList = documentManagementService
							.gamiSariyaSurveyOptionalList(requestNo, surveyRequestNo,
									committeeApprovalDTO.getCommitteeSurveyNo());

				}

				RequestContext.getCurrentInstance().execute("PF('uploadGamiDocument').show()");

			}
		}

		if (showAmendmentsForm == true) {

			DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
			try {
				sessionBackingBean.setApplicationNoForDoc(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo());
				sessionBackingBean.setPermitRenewalPermitNo(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.setPermitRenewalTranstractionTypeDescription(typeTrans);
				sessionBackingBean.setAmendmentPopUp(true);

				uploaddocumentManagementDTO.setUpload_Permit(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				uploaddocumentManagementDTO.setTransaction_Type(typeTrans);

				mandatoryList = documentManagementService.mandatoryDocs(typeCodeTrans,
						committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				optionalList = documentManagementService.optionalDocs(typeCodeTrans,
						committeeApprovalDTO.getCommitteeAmendmentsPermitNo());

				sessionBackingBean.optionalList = documentManagementService.optionalDocs(typeTrans,
						committeeApprovalDTO.getCommitteeAmendmentsPermitNo());

				sessionBackingBean.newPermitMandatoryDocumentList = documentManagementService
						.newPermitMandatoryList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.newPermitOptionalDocumentList = documentManagementService
						.newPermitOptionalList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.permitRenewalMandatoryDocumentList = documentManagementService
						.permitRenewalMandatoryList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.permitRenewalOptionalDocumentList = documentManagementService
						.permitRenewalOptionalList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.backlogManagementOptionalDocumentList = documentManagementService
						.backlogManagementOptionalList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());

				sessionBackingBean.amendmentToBusOwnerMandatoryDocumentList = documentManagementService
						.amendmentToBusOwnerMandatoryList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.amendmentToBusOwnerOptionalDocumentList = documentManagementService
						.amendmentToBusOwnerOptionalList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.amendmentToBusMandatoryDocumentList = documentManagementService
						.amendmentToBusMandatoryList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.amendmentToBusOptionalDocumentList = documentManagementService
						.amendmentToBusOptionalList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.amendmentToOwnerBusMandatoryDocumentList = documentManagementService
						.amendmentToOwnerBusMandatoryList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.amendmentToOwnerBusOptionalDocumentList = documentManagementService
						.amendmentToOwnerBusOptionalList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.amendmentToServiceBusMandatoryDocumentList = documentManagementService
						.amendmentToServiceBusMandatoryList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.amendmentToServiceBusOptionalDocumentList = documentManagementService
						.amendmentToServiceBusOptionalList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.amendmentToServiceMandatoryDocumentList = documentManagementService
						.amendmentToServiceMandatoryList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				sessionBackingBean.amendmentToServiceOptionalDocumentList = documentManagementService
						.amendmentToServiceOptionalList(committeeApprovalDTO.getCommitteeAmendmentsPermitNo());

				sessionBackingBean.tenderMandatoryDocumentList = documentManagementService
						.tenderMandatoryList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.tenderOptionalDocumentList = documentManagementService
						.tenderOptionalList(committeeApprovalDTO.getCommitteTenderRefNo());

				RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (showTenderForm == true) {

			DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
			try {
				sessionBackingBean.setApplicationNoForDoc(committeeApprovalDTO.getCommitteeTenderApplicationNo());
				sessionBackingBean.setPermitRenewalPermitNo(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.setPermitRenewalTranstractionTypeDescription(typeTrans);

				uploaddocumentManagementDTO.setUpload_Permit(committeeApprovalDTO.getCommitteTenderRefNo());
				uploaddocumentManagementDTO.setTransaction_Type(typeTrans);

				mandatoryList = documentManagementService.mandatoryDocs(typeCodeTrans,
						committeeApprovalDTO.getCommitteTenderRefNo());
				optionalList = documentManagementService.optionalDocs(typeCodeTrans,
						committeeApprovalDTO.getCommitteTenderRefNo());

				sessionBackingBean.optionalList = documentManagementService.optionalDocs(typeCodeTrans,
						committeeApprovalDTO.getCommitteTenderRefNo());

				sessionBackingBean.newPermitMandatoryDocumentList = documentManagementService
						.newPermitMandatoryList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.newPermitOptionalDocumentList = documentManagementService
						.newPermitOptionalList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.permitRenewalMandatoryDocumentList = documentManagementService
						.permitRenewalMandatoryList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.permitRenewalOptionalDocumentList = documentManagementService
						.permitRenewalOptionalList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.backlogManagementOptionalDocumentList = documentManagementService
						.backlogManagementOptionalList(committeeApprovalDTO.getCommitteTenderRefNo());

				sessionBackingBean.amendmentToBusOwnerMandatoryDocumentList = documentManagementService
						.amendmentToBusOwnerMandatoryList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.amendmentToBusOwnerOptionalDocumentList = documentManagementService
						.amendmentToBusOwnerOptionalList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.amendmentToBusMandatoryDocumentList = documentManagementService
						.amendmentToBusMandatoryList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.amendmentToBusOptionalDocumentList = documentManagementService
						.amendmentToBusOptionalList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.amendmentToOwnerBusMandatoryDocumentList = documentManagementService
						.amendmentToOwnerBusMandatoryList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.amendmentToOwnerBusOptionalDocumentList = documentManagementService
						.amendmentToOwnerBusOptionalList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.amendmentToServiceBusMandatoryDocumentList = documentManagementService
						.amendmentToServiceBusMandatoryList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.amendmentToServiceBusOptionalDocumentList = documentManagementService
						.amendmentToServiceBusOptionalList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.amendmentToServiceMandatoryDocumentList = documentManagementService
						.amendmentToServiceMandatoryList(committeeApprovalDTO.getCommitteTenderRefNo());
				sessionBackingBean.amendmentToServiceOptionalDocumentList = documentManagementService
						.amendmentToServiceOptionalList(committeeApprovalDTO.getCommitteTenderRefNo());

				sessionBackingBean.tenderMandatoryDocumentList = documentManagementService
						.tenderMandatoryList(committeeApprovalDTO.getCommitteTenderRefNo());

				sessionBackingBean.tenderOptionalDocumentList = documentManagementService
						.tenderOptionalList(committeeApprovalDTO.getCommitteTenderRefNo());

				RequestContext.getCurrentInstance().execute("PF('uploadTenderDocument').show()");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// View Committee/Board Approval Status Methods

	public void viewSearch() {

		setSearchDone(true);
		if (showSurveyForm == true) {

			typeTrans = committeeApprovalDTO.getSelectedTransactionType();
			typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);
			if (typeCodeTrans.equals("08")) {
				viewSurveySearch();
			}

			// added by tharushi.e

			if (typeCodeTrans.equals("19")) {
				viewGamiSurveySearch();
			}

		}

		if (showAmendmentsForm == true) {

			viewAmendmentSearch();
		}

		if (showTenderForm == true) {

			viewTenderSearch();

		}

		if (searchDone == false) {

			setDisableSaveBtn(false);
		}

	}

	public void viewSurveySearch() {

		if (committeeApprovalDTO.getCommitteeSurveyNo() != null
				&& !committeeApprovalDTO.getCommitteeSurveyNo().isEmpty()
				&& !committeeApprovalDTO.getCommitteeSurveyNo().equalsIgnoreCase("")) {

			showCommitteeTable = true;
			showDocPanel = true;
			viewDataForCommitteeDetails.clear();
			getDataForCommitteeDetails_01.clear();
			getDataForCommitteeDetails_02.clear();
			getDataForBoardDetails_01.clear();
			getDataForBoardDetails_02.clear();
			viewDataForBoardDetails.clear();

			String boardApproveCheck = surveyService.getBoardApproveStatus(committeeApprovalDTO.getCommitteeSurveyNo());
			String boardApproveCheckGami = surveyService
					.getGamiBoardApproveStatus(committeeApprovalDTO.getCommitteeSurveyNo());

			approvalType = "Committee Approval Status";

			comType = "C";
			refNo = surveyService.getRefNo(typeCodeTrans, comType);
			int seqNo = surveyService.getSeqNo(refNo, committeeApprovalDTO.getCommitteeSurveyNo());
			setCommitteeMainRemark(surveyService.getMainRemark(refNo, committeeApprovalDTO.getCommitteeSurveyNo()));
			getDataForCommitteeDetails_01 = surveyService.getDetails01(refNo);
			getDataForCommitteeDetails_02 = surveyService.getDetails02(seqNo);

			for (int a = 0; a < getDataForCommitteeDetails_01.size(); a++) {

				CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

				approvalDTO.setUserName(getDataForCommitteeDetails_01.get(a).getUserName());
				approvalDTO.setResponsibility(getDataForCommitteeDetails_01.get(a).getResponsibility());
				approvalDTO.setStatus(getDataForCommitteeDetails_01.get(a).getStatus());
				approvalDTO.setTransactionDate(getDataForCommitteeDetails_01.get(a).getTransactionDate());
				approvalDTO.setApproveRejectStatus(getDataForCommitteeDetails_02.get(a).getApproveRejectStatus());
				approvalDTO.setSelectedRemark(getDataForCommitteeDetails_02.get(a).getSelectedRemark());

				viewDataForCommitteeDetails.add(approvalDTO);

			}

			if (boardApproveCheck == null) {
				showBoardTable = false;

			} else if (boardApproveCheck.equals("A")) {

				showBoardTable = true;
				comType = "B";
				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				int seqNoBoard = surveyService.getSeqNo(refNo, committeeApprovalDTO.getCommitteeSurveyNo());
				setBoardMainRemark(surveyService.getMainRemark(refNo, committeeApprovalDTO.getCommitteeSurveyNo()));
				getDataForBoardDetails_01 = surveyService.getDetails01(refNo);
				getDataForBoardDetails_02 = surveyService.getDetails02(seqNoBoard);

				for (int a = 0; a < getDataForBoardDetails_01.size(); a++) {

					CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

					approvalDTO.setUserName(getDataForBoardDetails_01.get(a).getUserName());
					approvalDTO.setResponsibility(getDataForBoardDetails_01.get(a).getResponsibility());
					approvalDTO.setStatus(getDataForBoardDetails_01.get(a).getStatus());
					approvalDTO.setTransactionDate(getDataForBoardDetails_01.get(a).getTransactionDate());
					approvalDTO.setApproveRejectStatus(getDataForBoardDetails_02.get(a).getApproveRejectStatus());
					approvalDTO.setSelectedRemark(getDataForBoardDetails_02.get(a).getSelectedRemark());

					viewDataForBoardDetails.add(approvalDTO);

				}
			} else {

				showBoardTable = false;

			}

		} else {

			errorMsg = "Survey No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}
	}

	public void viewGamiSurveySearch() {

		if (committeeApprovalDTO.getCommitteeSurveyNo() != null
				&& !committeeApprovalDTO.getCommitteeSurveyNo().isEmpty()
				&& !committeeApprovalDTO.getCommitteeSurveyNo().equalsIgnoreCase("")) {

			
			showDocPanel = true;
			
			getDataForBoardDetails_01.clear();
			getDataForBoardDetails_02.clear();
			viewDataForBoardDetails.clear();

			String boardApproveCheckGami = surveyService
					.getGamiBoardApproveStatus(committeeApprovalDTO.getCommitteeSurveyNo());

			refNo = surveyService.getRefNo(typeCodeTrans, comType);
			
			showCommitteeTable = false; 

			if (boardApproveCheckGami == null) {
				showBoardTable = false;

			} else if (boardApproveCheckGami.equals("A")) {

				showBoardTable = true;
				comType = "B";
				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				int seqNoBoard = surveyService.getSeqNo(refNo, committeeApprovalDTO.getCommitteeSurveyNo());
				setBoardMainRemark(surveyService.getMainRemark(refNo, committeeApprovalDTO.getCommitteeSurveyNo()));
				getDataForBoardDetails_01 = surveyService.getDetails01(refNo);
				getDataForBoardDetails_02 = surveyService.getDetails02(seqNoBoard);

				for (int a = 0; a < getDataForBoardDetails_01.size(); a++) {

					CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

					approvalDTO.setUserName(getDataForBoardDetails_01.get(a).getUserName());
					approvalDTO.setResponsibility(getDataForBoardDetails_01.get(a).getResponsibility());
					approvalDTO.setStatus(getDataForBoardDetails_01.get(a).getStatus());
					approvalDTO.setTransactionDate(getDataForBoardDetails_01.get(a).getTransactionDate());
					approvalDTO.setApproveRejectStatus(getDataForBoardDetails_02.get(a).getApproveRejectStatus());
					approvalDTO.setSelectedRemark(getDataForBoardDetails_02.get(a).getSelectedRemark());

					viewDataForBoardDetails.add(approvalDTO);

				}
			} else {

				showBoardTable = false;

			}

		} else {

			errorMsg = "Survey No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}
	}

	//Back Up
	/*public void viewGamiSurveySearch() {

		if (committeeApprovalDTO.getCommitteeSurveyNo() != null
				&& !committeeApprovalDTO.getCommitteeSurveyNo().isEmpty()
				&& !committeeApprovalDTO.getCommitteeSurveyNo().equalsIgnoreCase("")) {

			showCommitteeTable = true;
			showDocPanel = true;
			viewDataForCommitteeDetails.clear();
			getDataForCommitteeDetails_01.clear();
			getDataForCommitteeDetails_02.clear();
			getDataForBoardDetails_01.clear();
			getDataForBoardDetails_02.clear();
			viewDataForBoardDetails.clear();

			String boardApproveCheckGami = surveyService
					.getGamiBoardApproveStatus(committeeApprovalDTO.getCommitteeSurveyNo());

			approvalType = "Committee Approval Status";

			comType = "C";
			refNo = surveyService.getRefNo(typeCodeTrans, comType);
			int seqNo = surveyService.getSeqNo(refNo, committeeApprovalDTO.getCommitteeSurveyNo());
			setCommitteeMainRemark(surveyService.getMainRemark(refNo, committeeApprovalDTO.getCommitteeSurveyNo()));
			getDataForCommitteeDetails_01 = surveyService.getDetails01(refNo);
			getDataForCommitteeDetails_02 = surveyService.getDetails02(seqNo);

			for (int a = 0; a < getDataForCommitteeDetails_01.size(); a++) {

				CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

				approvalDTO.setUserName(getDataForCommitteeDetails_01.get(a).getUserName());
				approvalDTO.setResponsibility(getDataForCommitteeDetails_01.get(a).getResponsibility());
				approvalDTO.setStatus(getDataForCommitteeDetails_01.get(a).getStatus());
				approvalDTO.setTransactionDate(getDataForCommitteeDetails_01.get(a).getTransactionDate());
				approvalDTO.setApproveRejectStatus(getDataForCommitteeDetails_02.get(a).getApproveRejectStatus());
				approvalDTO.setSelectedRemark(getDataForCommitteeDetails_02.get(a).getSelectedRemark());

				viewDataForCommitteeDetails.add(approvalDTO);

			}

			showCommitteeTable = false; // added by Dinushi on 12/11/2021
			if (boardApproveCheckGami == null) {
				showBoardTable = false;

			} else if (boardApproveCheckGami.equals("A")) {

				showBoardTable = true;
				comType = "B";
				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				int seqNoBoard = surveyService.getSeqNo(refNo, committeeApprovalDTO.getCommitteeSurveyNo());
				setBoardMainRemark(surveyService.getMainRemark(refNo, committeeApprovalDTO.getCommitteeSurveyNo()));
				getDataForBoardDetails_01 = surveyService.getDetails01(refNo);
				getDataForBoardDetails_02 = surveyService.getDetails02(seqNoBoard);

				for (int a = 0; a < getDataForBoardDetails_01.size(); a++) {

					CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

					approvalDTO.setUserName(getDataForBoardDetails_01.get(a).getUserName());
					approvalDTO.setResponsibility(getDataForBoardDetails_01.get(a).getResponsibility());
					approvalDTO.setStatus(getDataForBoardDetails_01.get(a).getStatus());
					approvalDTO.setTransactionDate(getDataForBoardDetails_01.get(a).getTransactionDate());
					approvalDTO.setApproveRejectStatus(getDataForBoardDetails_02.get(a).getApproveRejectStatus());
					approvalDTO.setSelectedRemark(getDataForBoardDetails_02.get(a).getSelectedRemark());

					viewDataForBoardDetails.add(approvalDTO);

				}
			} else {

				showBoardTable = false;

			}

		} else {

			errorMsg = "Survey No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}
	}

	*/
	
	public void viewAmendmentSearch() {

		if (committeeApprovalDTO.getCommitteeAmendmentsApplicationNo() != null
				&& !committeeApprovalDTO.getCommitteeAmendmentsApplicationNo().isEmpty()
				&& !committeeApprovalDTO.getCommitteeAmendmentsApplicationNo().equalsIgnoreCase("")) {

			showCommitteeTable = true;
			showDocPanel = true;
			viewDataForCommitteeDetails.clear();
			getDataForCommitteeDetails_01.clear();
			getDataForCommitteeDetails_02.clear();
			getDataForBoardDetails_01.clear();
			getDataForBoardDetails_02.clear();
			viewDataForBoardDetails.clear();

			String boardApproveCheck = amendmentService
					.getBoardApprovalStatus(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo());

			approvalType = "Committee Approval Status";

			comType = "C";
			refNo = surveyService.getRefNo(typeCodeTrans, comType);
			int seqNo = surveyService.getSeqNo(refNo, committeeApprovalDTO.getCommitteeAmendmentsApplicationNo());
			setCommitteeMainRemark(
					surveyService.getMainRemark(refNo, committeeApprovalDTO.getCommitteeAmendmentsApplicationNo()));
			getDataForCommitteeDetails_01 = surveyService.getDetails01(refNo);
			getDataForCommitteeDetails_02 = surveyService.getDetails02(seqNo);

			for (int a = 0; a < getDataForCommitteeDetails_01.size(); a++) {

				CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

				approvalDTO.setUserName(getDataForCommitteeDetails_01.get(a).getUserName());
				approvalDTO.setResponsibility(getDataForCommitteeDetails_01.get(a).getResponsibility());
				approvalDTO.setStatus(getDataForCommitteeDetails_01.get(a).getStatus());
				approvalDTO.setTransactionDate(getDataForCommitteeDetails_01.get(a).getTransactionDate());
				approvalDTO.setApproveRejectStatus(getDataForCommitteeDetails_02.get(a).getApproveRejectStatus());
				approvalDTO.setSelectedRemark(getDataForCommitteeDetails_02.get(a).getSelectedRemark());

				viewDataForCommitteeDetails.add(approvalDTO);

			}

			if (boardApproveCheck == null) {
				showBoardTable = false;

			} else if (boardApproveCheck.equals("A")) {

				showBoardTable = true;
				comType = "B";
				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				int seqNoBoard = surveyService.getSeqNo(refNo,
						committeeApprovalDTO.getCommitteeAmendmentsApplicationNo());
				setBoardMainRemark(
						surveyService.getMainRemark(refNo, committeeApprovalDTO.getCommitteeAmendmentsApplicationNo()));
				getDataForBoardDetails_01 = surveyService.getDetails01(refNo);
				getDataForBoardDetails_02 = surveyService.getDetails02(seqNoBoard);

				for (int a = 0; a < getDataForBoardDetails_01.size(); a++) {

					CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

					approvalDTO.setUserName(getDataForBoardDetails_01.get(a).getUserName());
					approvalDTO.setResponsibility(getDataForBoardDetails_01.get(a).getResponsibility());
					approvalDTO.setStatus(getDataForBoardDetails_01.get(a).getStatus());
					approvalDTO.setTransactionDate(getDataForBoardDetails_01.get(a).getTransactionDate());
					approvalDTO.setApproveRejectStatus(getDataForBoardDetails_02.get(a).getApproveRejectStatus());
					approvalDTO.setSelectedRemark(getDataForBoardDetails_02.get(a).getSelectedRemark());

					viewDataForBoardDetails.add(approvalDTO);

				}
			} else {
				showBoardTable = false;

			}

		} else {

			errorMsg = "Application No. or Permit No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void viewTenderSearch() {

		if (committeeApprovalDTO.getCommitteeTenderApplicationNo() != null
				&& !committeeApprovalDTO.getCommitteeTenderApplicationNo().isEmpty()
				&& !committeeApprovalDTO.getCommitteeTenderApplicationNo().equalsIgnoreCase("")) {
			showCommitteeTable = true;
			showDocPanel = true;
			viewDataForCommitteeDetails.clear();
			getDataForCommitteeDetails_01.clear();
			getDataForCommitteeDetails_02.clear();
			getDataForBoardDetails_01.clear();
			getDataForBoardDetails_02.clear();
			viewDataForBoardDetails.clear();
			getDataFor02ndCommitteeDetails_01.clear();
			getDataFor02ndCommitteeDetails_02.clear();
			viewDataFor02ndCommitteeDetails.clear();

			approvalType = "First Committee Approval Status";

			String secondCommitteeStatus = tenderService
					.getsecondCommitteeApprovalStatus(committeeApprovalDTO.getCommitteeTenderApplicationNo());

			String boardApproveCheck = tenderService
					.getBoardApprovalStatus(committeeApprovalDTO.getCommitteeTenderApplicationNo());

			comType = "F";
			refNo = surveyService.getRefNo(typeCodeTrans, comType);
			int seqNo = surveyService.getSeqNo(refNo, committeeApprovalDTO.getCommitteeTenderApplicationNo());
			setCommitteeMainRemark(
					surveyService.getMainRemark(refNo, committeeApprovalDTO.getCommitteeTenderApplicationNo()));
			getDataForCommitteeDetails_01 = surveyService.getDetails01(refNo);
			getDataForCommitteeDetails_02 = surveyService.getDetails02(seqNo);

			for (int a = 0; a < getDataForCommitteeDetails_01.size(); a++) {

				CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

				approvalDTO.setUserName(getDataForCommitteeDetails_01.get(a).getUserName());
				approvalDTO.setResponsibility(getDataForCommitteeDetails_01.get(a).getResponsibility());
				approvalDTO.setStatus(getDataForCommitteeDetails_01.get(a).getStatus());
				approvalDTO.setTransactionDate(getDataForCommitteeDetails_01.get(a).getTransactionDate());
				approvalDTO.setApproveRejectStatus(getDataForCommitteeDetails_02.get(a).getApproveRejectStatus());
				approvalDTO.setSelectedRemark(getDataForCommitteeDetails_02.get(a).getSelectedRemark());

				viewDataForCommitteeDetails.add(approvalDTO);

			}

			if (secondCommitteeStatus == null) {
				showSecondCommitteeTable = false;
			} else if (secondCommitteeStatus.equals("A")) {
				showSecondCommitteeTable = true;
				comType = "S";
				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				int seqNoSecond = surveyService.getSeqNo(refNo, committeeApprovalDTO.getCommitteeTenderApplicationNo());
				setSecondCommitteeMainRemark(
						surveyService.getMainRemark(refNo, committeeApprovalDTO.getCommitteeTenderApplicationNo()));
				getDataFor02ndCommitteeDetails_01 = surveyService.getDetails01(refNo);
				getDataFor02ndCommitteeDetails_02 = surveyService.getDetails02(seqNoSecond);

				for (int a = 0; a < getDataFor02ndCommitteeDetails_01.size(); a++) {

					CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

					approvalDTO.setUserName(getDataFor02ndCommitteeDetails_01.get(a).getUserName());
					approvalDTO.setResponsibility(getDataFor02ndCommitteeDetails_01.get(a).getResponsibility());
					approvalDTO.setStatus(getDataFor02ndCommitteeDetails_01.get(a).getStatus());
					approvalDTO.setTransactionDate(getDataFor02ndCommitteeDetails_01.get(a).getTransactionDate());
					approvalDTO
							.setApproveRejectStatus(getDataFor02ndCommitteeDetails_02.get(a).getApproveRejectStatus());
					approvalDTO.setSelectedRemark(getDataFor02ndCommitteeDetails_02.get(a).getSelectedRemark());

					viewDataFor02ndCommitteeDetails.add(approvalDTO);

				}

			} else {
				showSecondCommitteeTable = false;

			}

			if (boardApproveCheck == null) {
				showBoardTable = false;

			} else if (boardApproveCheck.equals("A")) {

				showBoardTable = true;
				comType = "B";
				refNo = surveyService.getRefNo(typeCodeTrans, comType);
				int seqNoBoard = surveyService.getSeqNo(refNo, committeeApprovalDTO.getCommitteeTenderApplicationNo());
				setBoardMainRemark(
						surveyService.getMainRemark(refNo, committeeApprovalDTO.getCommitteeTenderApplicationNo()));
				getDataForBoardDetails_01 = surveyService.getDetails01(refNo);
				getDataForBoardDetails_02 = surveyService.getDetails02(seqNoBoard);

				for (int a = 0; a < getDataForBoardDetails_01.size(); a++) {

					CommitteeOrBoardApprovalDTO approvalDTO = new CommitteeOrBoardApprovalDTO();

					approvalDTO.setUserName(getDataForBoardDetails_01.get(a).getUserName());
					approvalDTO.setResponsibility(getDataForBoardDetails_01.get(a).getResponsibility());
					approvalDTO.setStatus(getDataForBoardDetails_01.get(a).getStatus());
					approvalDTO.setTransactionDate(getDataForBoardDetails_01.get(a).getTransactionDate());
					approvalDTO.setApproveRejectStatus(getDataForBoardDetails_02.get(a).getApproveRejectStatus());
					approvalDTO.setSelectedRemark(getDataForBoardDetails_02.get(a).getSelectedRemark());

					viewDataForBoardDetails.add(approvalDTO);

				}
			} else {
				showBoardTable = false;

			}

		} else {

			errorMsg = "Application No. or Permit No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void viewClear() {

		committeeApprovalDTO.setSelectedTransactionType(null);

		viewDataForCommitteeDetails.clear();
		getDataForCommitteeDetails_01.clear();
		getDataForCommitteeDetails_02.clear();
		getDataForBoardDetails_01.clear();
		getDataForBoardDetails_02.clear();
		viewDataForBoardDetails.clear();
		getDataFor02ndCommitteeDetails_01.clear();
		getDataFor02ndCommitteeDetails_02.clear();
		viewDataFor02ndCommitteeDetails.clear();

		showDocPanel = false;
		showSurveyForm = false;
		showTenderForm = false;
		showAmendmentsForm = false;
		showCommitteeTable = false;
		showSecondCommitteeTable = false;
		showBoardTable = false;

	}

	public void viewDocuments() {
		typeTrans = committeeApprovalDTO.getSelectedTransactionType();
		typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);

		if (showSurveyForm == true) {

			if (typeCodeTrans.equals("08")) {
				try {

					mandatoryList = documentManagementService.mandatoryUserManagementViewDocs(typeCodeTrans,
							committeeApprovalDTO.getRequestNo());
					optionalList = documentManagementService.optionalUserManagementViewDocs(typeCodeTrans,
							committeeApprovalDTO.getRequestNo());

					RequestContext.getCurrentInstance().execute("PF('uploadSurveyDocument').show()");

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (typeCodeTrans.equals("19")) {
				String requestNo = gamiSariyaService.getGamiRequestNo(committeeApprovalDTO.getCommitteeSurveyNo());
				String surveyRequestNo = gamiSariyaService
						.getGamiSurveyRequestNo(committeeApprovalDTO.getCommitteeSurveyNo());
				mandatoryList = documentManagementService.gamiSariyaSurveyMandatoryList(requestNo, surveyRequestNo,
						committeeApprovalDTO.getCommitteeSurveyNo());
				optionalList = documentManagementService.gamiSariyaSurveyOptionalList(requestNo, surveyRequestNo,
						committeeApprovalDTO.getCommitteeSurveyNo());

				RequestContext.getCurrentInstance().execute("PF('uploadGamiDocument').show()");
			}

		}

		if (showAmendmentsForm == true) {

			DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
			try {

				mandatoryList = documentManagementService.mandatoryViewDocs(typeCodeTrans,
						committeeApprovalDTO.getCommitteeAmendmentsPermitNo());
				optionalList = documentManagementService.optionalViewDocs(typeCodeTrans,
						committeeApprovalDTO.getCommitteeAmendmentsPermitNo());

				RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if (showTenderForm == true) {

			DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
			try {

				mandatoryList = documentManagementService.mandatoryViewDocs(typeCodeTrans,
						committeeApprovalDTO.getCommitteTenderRefNo());
				optionalList = documentManagementService.optionalViewDocs(typeCodeTrans,
						committeeApprovalDTO.getCommitteTenderRefNo());

				RequestContext.getCurrentInstance().execute("PF('uploadTenderDocument').show()");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void viewAction() throws InterruptedException {

		if (typeCodeTrans.equals("21") || typeCodeTrans.equals("22") || typeCodeTrans.equals("23")
				|| typeCodeTrans.equals("14") || typeCodeTrans.equals("15") || typeCodeTrans.equals("16")) {
			sessionBackingBean.setBackCommittee(false);
			sessionBackingBean.setSelectedTransactionType(committeeApprovalDTO.getSelectedTransactionType());
			sessionBackingBean.setCommitteeView(true);
			sessionBackingBean.setCommitteeApplicationNo(committeeApprovalDTO.getCommitteeAmendmentsApplicationNo());

		}

		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/amendmentsToBusOrOwner/viewBusOwnerServiceAmendment.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public CommitteeOrBoardApprovalDTO getCommitteeApprovalDTO() {
		return committeeApprovalDTO;
	}

	public void setCommitteeApprovalDTO(CommitteeOrBoardApprovalDTO committeeApprovalDTO) {
		this.committeeApprovalDTO = committeeApprovalDTO;
	}

	public List<CommitteeOrBoardApprovalDTO> getSurveyNoListForCommitteeApproval() {
		return surveyNoListForCommitteeApproval;
	}

	public void setSurveyNoListForCommitteeApproval(
			List<CommitteeOrBoardApprovalDTO> surveyNoListForCommitteeApproval) {
		this.surveyNoListForCommitteeApproval = surveyNoListForCommitteeApproval;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<CommitteeOrBoardApprovalDTO> getCommitteeBoardApprovalList() {
		return committeeBoardApprovalList;
	}

	public void setCommitteeBoardApprovalList(List<CommitteeOrBoardApprovalDTO> committeeBoardApprovalList) {
		this.committeeBoardApprovalList = committeeBoardApprovalList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isShowSurveyCommittee() {
		return showSurveyCommittee;
	}

	public void setShowSurveyCommittee(boolean showSurveyCommittee) {
		this.showSurveyCommittee = showSurveyCommittee;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public List<CommitteeOrBoardApprovalDTO> getTransactionTypeListForCommitteBoardApproval() {
		return transactionTypeListForCommitteBoardApproval;
	}

	public void setTransactionTypeListForCommitteBoardApproval(
			List<CommitteeOrBoardApprovalDTO> transactionTypeListForCommitteBoardApproval) {
		this.transactionTypeListForCommitteBoardApproval = transactionTypeListForCommitteBoardApproval;
	}

	public boolean isShowSurveyForm() {
		return showSurveyForm;
	}

	public void setShowSurveyForm(boolean showSurveyForm) {
		this.showSurveyForm = showSurveyForm;
	}

	public boolean isShowTenderForm() {
		return showTenderForm;
	}

	public void setShowTenderForm(boolean showTenderForm) {
		this.showTenderForm = showTenderForm;
	}

	public boolean isShowAmendmentsForm() {
		return showAmendmentsForm;
	}

	public void setShowAmendmentsForm(boolean showAmendmentsForm) {
		this.showAmendmentsForm = showAmendmentsForm;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public String getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(String approvalType) {
		this.approvalType = approvalType;
	}

	public List<CommitteeOrBoardApprovalDTO> getSelectList() {
		return selectList;
	}

	public void setSelectList(List<CommitteeOrBoardApprovalDTO> selectList) {
		this.selectList = selectList;
	}

	public boolean isProceedSurveySearch() {
		return proceedSurveySearch;
	}

	public void setProceedSurveySearch(boolean proceedSurveySearch) {
		this.proceedSurveySearch = proceedSurveySearch;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getComType() {
		return comType;
	}

	public void setComType(String comType) {
		this.comType = comType;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getTypeTrans() {
		return typeTrans;
	}

	public void setTypeTrans(String typeTrans) {
		this.typeTrans = typeTrans;
	}

	public String getTypeCodeTrans() {
		return typeCodeTrans;
	}

	public void setTypeCodeTrans(String typeCodeTrans) {
		this.typeCodeTrans = typeCodeTrans;
	}

	public boolean isProceedFurtherSurveySearch() {
		return proceedFurtherSurveySearch;
	}

	public void setProceedFurtherSurveySearch(boolean proceedFurtherSurveySearch) {
		this.proceedFurtherSurveySearch = proceedFurtherSurveySearch;
	}

	public boolean isBoardApproval() {
		return boardApproval;
	}

	public void setBoardApproval(boolean boardApproval) {
		this.boardApproval = boardApproval;
	}

	public boolean isProcceedBoardApproval() {
		return procceedBoardApproval;
	}

	public void setProcceedBoardApproval(boolean procceedBoardApproval) {
		this.procceedBoardApproval = procceedBoardApproval;
	}

	public List<CommitteeOrBoardApprovalDTO> getAmendmentsApplicationNoListForCommitteeApproval() {
		return amendmentsApplicationNoListForCommitteeApproval;
	}

	public void setAmendmentsApplicationNoListForCommitteeApproval(
			List<CommitteeOrBoardApprovalDTO> amendmentsApplicationNoListForCommitteeApproval) {
		this.amendmentsApplicationNoListForCommitteeApproval = amendmentsApplicationNoListForCommitteeApproval;
	}

	public List<CommitteeOrBoardApprovalDTO> getAmendmentsPermitNoListForCommitteeApproval() {
		return amendmentsPermitNoListForCommitteeApproval;
	}

	public void setAmendmentsPermitNoListForCommitteeApproval(
			List<CommitteeOrBoardApprovalDTO> amendmentsPermitNoListForCommitteeApproval) {
		this.amendmentsPermitNoListForCommitteeApproval = amendmentsPermitNoListForCommitteeApproval;
	}

	public AmendmentService getAmendmentService() {
		return amendmentService;
	}

	public void setAmendmentService(AmendmentService amendmentService) {
		this.amendmentService = amendmentService;
	}

	public TenderService getTenderService() {
		return tenderService;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
	}

	public List<CommitteeOrBoardApprovalDTO> getTenderApplicationNoListForCommitteeApproval() {
		return tenderApplicationNoListForCommitteeApproval;
	}

	public void setTenderApplicationNoListForCommitteeApproval(
			List<CommitteeOrBoardApprovalDTO> tenderApplicationNoListForCommitteeApproval) {
		this.tenderApplicationNoListForCommitteeApproval = tenderApplicationNoListForCommitteeApproval;
	}

	public String getCheckFirstApproval() {
		return checkFirstApproval;
	}

	public void setCheckFirstApproval(String checkFirstApproval) {
		this.checkFirstApproval = checkFirstApproval;
	}

	public List<DocumentManagementDTO> getMandatoryList() {
		return mandatoryList;
	}

	public void setMandatoryList(List<DocumentManagementDTO> mandatoryList) {
		this.mandatoryList = mandatoryList;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public boolean isSearchDone() {
		return searchDone;
	}

	public void setSearchDone(boolean searchDone) {
		this.searchDone = searchDone;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public boolean isSaveDone() {
		return saveDone;
	}

	public void setSaveDone(boolean saveDone) {
		this.saveDone = saveDone;
	}

	public boolean isShowDocumentManagement() {
		return showDocumentManagement;
	}

	public void setShowDocumentManagement(boolean showDocumentManagement) {
		this.showDocumentManagement = showDocumentManagement;
	}

	public boolean isDisableAfterSave() {
		return disableAfterSave;
	}

	public void setDisableAfterSave(boolean disableAfterSave) {
		this.disableAfterSave = disableAfterSave;
	}

	public boolean isDisableSaveBtn() {
		return disableSaveBtn;
	}

	public void setDisableSaveBtn(boolean disableSaveBtn) {
		this.disableSaveBtn = disableSaveBtn;
	}

	public List<CommitteeOrBoardApprovalDTO> getViewSurveyNoListForCommitteeApprovalStatus() {
		return viewSurveyNoListForCommitteeApprovalStatus;
	}

	public void setViewSurveyNoListForCommitteeApprovalStatus(
			List<CommitteeOrBoardApprovalDTO> viewSurveyNoListForCommitteeApprovalStatus) {
		this.viewSurveyNoListForCommitteeApprovalStatus = viewSurveyNoListForCommitteeApprovalStatus;
	}

	public List<CommitteeOrBoardApprovalDTO> getGetDataForCommitteeDetails_01() {
		return getDataForCommitteeDetails_01;
	}

	public void setGetDataForCommitteeDetails_01(List<CommitteeOrBoardApprovalDTO> getDataForCommitteeDetails_01) {
		this.getDataForCommitteeDetails_01 = getDataForCommitteeDetails_01;
	}

	public List<CommitteeOrBoardApprovalDTO> getGetDataForCommitteeDetails_02() {
		return getDataForCommitteeDetails_02;
	}

	public void setGetDataForCommitteeDetails_02(List<CommitteeOrBoardApprovalDTO> getDataForCommitteeDetails_02) {
		this.getDataForCommitteeDetails_02 = getDataForCommitteeDetails_02;
	}

	public List<CommitteeOrBoardApprovalDTO> getViewDataForCommitteeDetails() {
		return viewDataForCommitteeDetails;
	}

	public void setViewDataForCommitteeDetails(List<CommitteeOrBoardApprovalDTO> viewDataForCommitteeDetails) {
		this.viewDataForCommitteeDetails = viewDataForCommitteeDetails;
	}

	public List<CommitteeOrBoardApprovalDTO> getGetDataForBoardDetails_01() {
		return getDataForBoardDetails_01;
	}

	public void setGetDataForBoardDetails_01(List<CommitteeOrBoardApprovalDTO> getDataForBoardDetails_01) {
		this.getDataForBoardDetails_01 = getDataForBoardDetails_01;
	}

	public List<CommitteeOrBoardApprovalDTO> getGetDataForBoardDetails_02() {
		return getDataForBoardDetails_02;
	}

	public void setGetDataForBoardDetails_02(List<CommitteeOrBoardApprovalDTO> getDataForBoardDetails_02) {
		this.getDataForBoardDetails_02 = getDataForBoardDetails_02;
	}

	public List<CommitteeOrBoardApprovalDTO> getViewDataForBoardDetails() {
		return viewDataForBoardDetails;
	}

	public void setViewDataForBoardDetails(List<CommitteeOrBoardApprovalDTO> viewDataForBoardDetails) {
		this.viewDataForBoardDetails = viewDataForBoardDetails;
	}

	public boolean isShowCommitteeTable() {
		return showCommitteeTable;
	}

	public void setShowCommitteeTable(boolean showCommitteeTable) {
		this.showCommitteeTable = showCommitteeTable;
	}

	public boolean isShowBoardTable() {
		return showBoardTable;
	}

	public void setShowBoardTable(boolean showBoardTable) {
		this.showBoardTable = showBoardTable;
	}

	public String getCommitteeMainRemark() {
		return committeeMainRemark;
	}

	public void setCommitteeMainRemark(String committeeMainRemark) {
		this.committeeMainRemark = committeeMainRemark;
	}

	public String getBoardMainRemark() {
		return boardMainRemark;
	}

	public void setBoardMainRemark(String boardMainRemark) {
		this.boardMainRemark = boardMainRemark;
	}

	public List<CommitteeOrBoardApprovalDTO> getViewAmendmentsApplicationNoListForCommitteeApproval() {
		return viewAmendmentsApplicationNoListForCommitteeApproval;
	}

	public void setViewAmendmentsApplicationNoListForCommitteeApproval(
			List<CommitteeOrBoardApprovalDTO> viewAmendmentsApplicationNoListForCommitteeApproval) {
		this.viewAmendmentsApplicationNoListForCommitteeApproval = viewAmendmentsApplicationNoListForCommitteeApproval;
	}

	public List<CommitteeOrBoardApprovalDTO> getViewTenderApplicationNoListForCommitteeApproval() {
		return viewTenderApplicationNoListForCommitteeApproval;
	}

	public void setViewTenderApplicationNoListForCommitteeApproval(
			List<CommitteeOrBoardApprovalDTO> viewTenderApplicationNoListForCommitteeApproval) {
		this.viewTenderApplicationNoListForCommitteeApproval = viewTenderApplicationNoListForCommitteeApproval;
	}

	public List<CommitteeOrBoardApprovalDTO> getGetDataFor02ndCommitteeDetails_01() {
		return getDataFor02ndCommitteeDetails_01;
	}

	public void setGetDataFor02ndCommitteeDetails_01(
			List<CommitteeOrBoardApprovalDTO> getDataFor02ndCommitteeDetails_01) {
		this.getDataFor02ndCommitteeDetails_01 = getDataFor02ndCommitteeDetails_01;
	}

	public List<CommitteeOrBoardApprovalDTO> getGetDataFor02ndCommitteeDetails_02() {
		return getDataFor02ndCommitteeDetails_02;
	}

	public void setGetDataFor02ndCommitteeDetails_02(
			List<CommitteeOrBoardApprovalDTO> getDataFor02ndCommitteeDetails_02) {
		this.getDataFor02ndCommitteeDetails_02 = getDataFor02ndCommitteeDetails_02;
	}

	public List<CommitteeOrBoardApprovalDTO> getViewDataFor02ndCommitteeDetails() {
		return viewDataFor02ndCommitteeDetails;
	}

	public void setViewDataFor02ndCommitteeDetails(List<CommitteeOrBoardApprovalDTO> viewDataFor02ndCommitteeDetails) {
		this.viewDataFor02ndCommitteeDetails = viewDataFor02ndCommitteeDetails;
	}

	public boolean isShowSecondCommitteeTable() {
		return showSecondCommitteeTable;
	}

	public void setShowSecondCommitteeTable(boolean showSecondCommitteeTable) {
		this.showSecondCommitteeTable = showSecondCommitteeTable;
	}

	public String getSecondCommitteeMainRemark() {
		return secondCommitteeMainRemark;
	}

	public void setSecondCommitteeMainRemark(String secondCommitteeMainRemark) {
		this.secondCommitteeMainRemark = secondCommitteeMainRemark;
	}

	public boolean isShowDocPanel() {
		return showDocPanel;
	}

	public void setShowDocPanel(boolean showDocPanel) {
		this.showDocPanel = showDocPanel;
	}

	public String getCheckCommitteeBoardAuthStatus() {
		return checkCommitteeBoardAuthStatus;
	}

	public void setCheckCommitteeBoardAuthStatus(String checkCommitteeBoardAuthStatus) {
		this.checkCommitteeBoardAuthStatus = checkCommitteeBoardAuthStatus;
	}

	public boolean isShowViewBtn() {
		return showViewBtn;
	}

	public void setShowViewBtn(boolean showViewBtn) {
		this.showViewBtn = showViewBtn;
	}

	public String getSelectTransactionType() {
		return selectTransactionType;
	}

	public void setSelectTransactionType(String selectTransactionType) {
		this.selectTransactionType = selectTransactionType;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

}
