package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.TrafficProposalDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "generateTrafficProposal")
@ViewScoped
public class GenerateTrafficProposalBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private SurveyService surveyService;
	private DocumentManagementService documentManagementService;
	private CommonService commonService;
	private TrafficProposalDTO trafficProposalDTO = new TrafficProposalDTO();

	private String selectedRequestNo;
	private String errorMsg;
	private String sucessMsg;
	private String selectedSurveyNo;
	private String value;
	private String infoMsg;

	private boolean showDetailsPanels = false;
	private boolean disabledPrintTrafBtn = true;
	// added
	private boolean disabledRePrintTrafBtn = true;
	private boolean readOnlyInputRemarks = false;
	private boolean disabledWithSelectedRow = true;
	private boolean disabledEditBtn = false;

	public List<TrafficProposalDTO> requestNoList = new ArrayList<TrafficProposalDTO>(0);
	public List<TrafficProposalDTO> dataList = new ArrayList<TrafficProposalDTO>(0);
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private TrafficProposalDTO selectedRow;
	private TrafficProposalDTO displayValuesForSelectedReqNo;
	private TrafficProposalDTO selectDTO;
	private TrafficProposalDTO dispplayValuesInTraTbDTO;

	private StreamedContent files;

	@PostConstruct
	public void init() {
		requestNoList = surveyService.getRequestNoList();
	}

	public GenerateTrafficProposalBackingBean() {
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
	}

	public void onRequestNoChange() {
		displayValuesForSelectedReqNo = surveyService.displayValuesForSelectedReqNo(selectedRequestNo);
		setTrafficProposalDTO(displayValuesForSelectedReqNo);
		selectedSurveyNo = trafficProposalDTO.getSurveyNo();
	}

	public void onTrafficProposalNoChange() {

	}

	public void searchAct() {

		if (!selectedRequestNo.equals("") || selectedRequestNo != null || !selectedRequestNo.isEmpty()) {

			setShowDetailsPanels(true);
			setReadOnlyInputRemarks(false);
			dataList = new ArrayList<TrafficProposalDTO>(0);
			dataList = surveyService.getRouteDetailsForSelectedSurveyNo(selectedSurveyNo, selectedRequestNo);
			String selectedProposalNo = surveyService.getTrafficProposalNoForSelectedSurveyNo(selectedSurveyNo);
			if (selectedProposalNo != null) {
				trafficProposalDTO.setTrafficProposalNo(selectedProposalNo);

				dispplayValuesInTraTbDTO = surveyService.getDetailsForTrafficProNo(selectedSurveyNo, selectedRequestNo,
						selectedProposalNo);
				trafficProposalDTO.setSpecialRemark(dispplayValuesInTraTbDTO.getSpecialRemark());
				trafficProposalDTO.setSpecialPrintNote(dispplayValuesInTraTbDTO.getSpecialPrintNote());
				trafficProposalDTO.setSuggestions(dispplayValuesInTraTbDTO.getSuggestions());
			} else {

			}
			if (!dataList.isEmpty()) {
				RequestContext.getCurrentInstance().update("trafficProposalTable");
				updateStatus();
			} else {
				errorMsg = "No data for this Request No.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else if (selectedRequestNo.equals("") || selectedRequestNo == null || selectedRequestNo.isEmpty()) {

			errorMsg = "Request No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	private void updateStatus() {
		String loginUser = sessionBackingBean.getLoginUser();
		boolean checkTaskCodeSU009IsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
				selectedSurveyNo, "SU009", "O");
		boolean checkTaskCodeSU011IsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
				selectedSurveyNo, "SU011", "C");
		boolean checkTaskCodeSU012IsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
				selectedSurveyNo, "SU012", "C");
		boolean checkTaskCodeSU009OIsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
				selectedSurveyNo, "SU009", "C");
		boolean checkTaskSU008 = false;
		if (checkTaskCodeSU009IsValid == false && checkTaskCodeSU011IsValid == false
				&& checkTaskCodeSU009OIsValid == false && checkTaskCodeSU012IsValid == false) {

			surveyService.insertTaskDetails(selectedRequestNo, selectedSurveyNo, loginUser, "SU009", "O");

			checkTaskSU008 = surveyService.checkTaskDetails(selectedRequestNo, selectedSurveyNo, "SU008", "C");
			if (checkTaskSU008 == true) {
				surveyService.CopyTaskDetailsANDinsertTaskHistory(selectedRequestNo, selectedSurveyNo, loginUser,
						"SU008");
				surveyService.deleteTaskDetails(selectedRequestNo, selectedSurveyNo, "SU008");
			}
		} else if (checkTaskCodeSU009IsValid == true) {

			checkTaskSU008 = surveyService.checkTaskDetails(selectedRequestNo, selectedSurveyNo, "SU008", "C");
			if (checkTaskSU008 == true) {
				surveyService.CopyTaskDetailsANDinsertTaskHistory(selectedRequestNo, selectedSurveyNo, loginUser,
						"SU008");
				surveyService.deleteTaskDetails(selectedRequestNo, selectedSurveyNo, "SU008");
			}
		}
		if (checkTaskCodeSU011IsValid == true) {

			setDisabledPrintTrafBtn(false);
			setReadOnlyInputRemarks(true);
			setDisabledEditBtn(true);
			setDisabledRePrintTrafBtn(true);
		} else if (checkTaskCodeSU012IsValid == true) {

			setDisabledPrintTrafBtn(true);
			setDisabledRePrintTrafBtn(false);
			setReadOnlyInputRemarks(true);
			setDisabledEditBtn(true);
		} else {

		}
	}

	public void clearFields() {
		setTrafficProposalDTO(null);
		dataList = new ArrayList<TrafficProposalDTO>(0);
		setSelectedRequestNo(null);
		RequestContext.getCurrentInstance().update("searchPanel");
		RequestContext.getCurrentInstance().update("trafficProposalTable");
		setShowDetailsPanels(false);
		setReadOnlyInputRemarks(false);
		setDisabledPrintTrafBtn(true);
		setDisabledRePrintTrafBtn(true);
		setDisabledWithSelectedRow(true);
		setDisabledEditBtn(false);
		requestNoList = new ArrayList<TrafficProposalDTO>(0);
		requestNoList = surveyService.getRequestNoList();
		selectDTO = new TrafficProposalDTO();
	}

	public void selectRow(SelectEvent event) {

		setDisabledWithSelectedRow(false);
		RequestContext.getCurrentInstance().update("cmdbtnTimeTb");
		RequestContext.getCurrentInstance().update("cmdbtnFareTb");
	}

	public void onNoOfPermitsReqIssueChange() {

	}

	public void editAction() {
		int beforeNoOfPermitsReqToIssue = surveyService.getCurrentNoOfPermitsReqToIssue(selectedRow.getSurveyNo(),
				selectedRow.getRouteNo());
		int updatedNoOfPermitsReqToIssue = selectedRow.getNoOfPermitsReqToIssue();

		boolean checkThisRecordBoardApproved = surveyService.checkIsBoardApproved(selectedSurveyNo, selectedRequestNo);
		if (checkThisRecordBoardApproved == false) {

			if (!(selectedRow.getNoOfPermitsReqToIssue() == 0)) {
				if (!(beforeNoOfPermitsReqToIssue == updatedNoOfPermitsReqToIssue)) {
					String modifyBy = sessionBackingBean.loginUser;
					selectedRow.setModifyBy(modifyBy);
					int result = surveyService.updateRouteDet(selectedRow);
					if (result == 0) {
						sucessMsg = "Successfully Updated.";
						RequestContext.getCurrentInstance().update("frmsuccessSve");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");

					} else {
						errorMsg = "Please update record before save.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "No. of permits req. to issue should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "No. of permits req. to issue should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else if (checkThisRecordBoardApproved == true) {

			errorMsg = "This survey no. is already board approved";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void updateRecord() {
		String createdBy = sessionBackingBean.loginUser;
		String loginUser = sessionBackingBean.getLoginUser();

		boolean checkTrafficProposalNo = surveyService.checkTrafficProposalNo(selectedRequestNo, selectedSurveyNo);

		if (checkTrafficProposalNo == false) {
			value = surveyService.generateReferenceNo();
			trafficProposalDTO.setTrafficProposalNo(value);
			RequestContext.getCurrentInstance().update("trafficProposalNoId");
			int result = surveyService.insertDataIntoTrafficProTb(trafficProposalDTO, createdBy);
			if (result == 0) {
				sucessMsg = "Successfully Saved.";
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				setReadOnlyInputRemarks(true);
				boolean checkTaskCodeSU009IsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
						selectedSurveyNo, "SU009", "C");
				boolean checkTaskCodeSU011IsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
						selectedSurveyNo, "SU011", "C");
				boolean checkTaskCodeSU012IsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
						selectedSurveyNo, "SU012", "C");
				if (checkTaskCodeSU009IsValid == false && checkTaskCodeSU011IsValid == false
						&& checkTaskCodeSU012IsValid == false) {

					commonService.updateSurveyTaskDetails(selectedRequestNo, selectedSurveyNo, "SU009", "C", loginUser);
				} else if (checkTaskCodeSU009IsValid == true) {

				}
				if (checkTaskCodeSU011IsValid == true) {

					setDisabledPrintTrafBtn(false);
					setDisabledRePrintTrafBtn(true);
				} else if (checkTaskCodeSU012IsValid == true) {

					setDisabledPrintTrafBtn(true);
					setDisabledRePrintTrafBtn(false);
				}

			} else {
				errorMsg = "You can not save this record.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			String selectedProposalNo = surveyService.getTrafficProposalNoForSelectedSurveyNo(selectedSurveyNo);
			infoMsg = "This Survey No. has a Traffic Proposal No. Traffic Proposal No. is " + selectedProposalNo + ".";
			RequestContext.getCurrentInstance().update("frmInfoPopUp");
			RequestContext.getCurrentInstance().execute("PF('infoPopUp').show()");
			trafficProposalDTO.setSurveyNo(selectedSurveyNo);
			trafficProposalDTO.setTrafficProposalNo(selectedProposalNo);
			int result = surveyService.updateDataInTrafficProTb(trafficProposalDTO, createdBy);
			if (result == 0) {
				sucessMsg = "Successfully Updated.";
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				setReadOnlyInputRemarks(true);
				setDisabledPrintTrafBtn(false);
				boolean checkTaskCodeSU009IsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
						selectedSurveyNo, "SU009", "C");
				if (checkTaskCodeSU009IsValid == false) {

					int updateTaskCodeResult = surveyService.updateTaskCode(selectedRequestNo, selectedSurveyNo,
							loginUser, "SU009", "C");

				}
			} else {
				errorMsg = "You can not update this record.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}

	}

	public void clearForm() {
		trafficProposalDTO.setSpecialRemark(null);
		trafficProposalDTO.setSpecialPrintNote(null);
		trafficProposalDTO.setSuggestions(null);
		setDisabledPrintTrafBtn(true);
		selectDTO = new TrafficProposalDTO();
		setReadOnlyInputRemarks(false);
		searchAct();
		setDisabledWithSelectedRow(true);
	}

	public StreamedContent printTrafficProAct() throws JRException {
		boolean checkTaskCodeSU011IsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
				selectedSurveyNo, "SU011", "C");
		if (checkTaskCodeSU011IsValid == false) {

		} else if (checkTaskCodeSU011IsValid == true) {

			String loginUser = sessionBackingBean.getLoginUser();
			boolean checkTaskCodeSU012IsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
					selectedSurveyNo, "SU012", "C");
			if (checkTaskCodeSU012IsValid == false) {
				commonService.updateSurveyTaskDetails(selectedRequestNo, selectedSurveyNo, "SU012", "O", loginUser);
				surveyService.CopyTaskDetailsANDinsertTaskHistory(selectedRequestNo, selectedSurveyNo, loginUser,
						"SU012");
				commonService.updateSurveyTaskDet(selectedRequestNo, selectedSurveyNo, "SU012", "C", loginUser);
				setDisabledPrintTrafBtn(true);
				setDisabledRePrintTrafBtn(false);
			} else if (checkTaskCodeSU012IsValid == true) {

			}

			String requestNo = selectedRequestNo;

			files = null;
			String sourceFileName = null;

			Connection conn = null;

			try {
				conn = ConnectionManager.getConnection();
				String logopath = "//lk//informatics//ntc//view//reports//";

				sourceFileName = "..//reports//TrafficProposal.jrxml";

				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();

				parameters.put("P_LOG0", logopath);

				parameters.put("P_REQ_NO", requestNo);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "Traffic Proposal.pdf");

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "pdf");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(conn);
			}

		}

		return files;
	}

	public StreamedContent rePrintTrafficProAct() throws JRException {
		boolean checkTaskCodeSU012IsValid = surveyService.checkTaskCodeForCurrentSurveyNo(selectedRequestNo,
				selectedSurveyNo, "SU012", "C");
		if (checkTaskCodeSU012IsValid == false) {

		} else if (checkTaskCodeSU012IsValid == true) {

			String loginUser = sessionBackingBean.getLoginUser();

			String requestNo = selectedRequestNo;

			files = null;
			String sourceFileName = null;

			Connection conn = null;

			try {
				conn = ConnectionManager.getConnection();
				String logopath = "//lk//informatics//ntc//view//reports//";
				sourceFileName = "..//reports//TrafficProposal.jrxml";

				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();

				parameters.put("P_LOG0", logopath);

				parameters.put("P_REQ_NO", requestNo);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "Traffic Proposal.pdf");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(conn);
			}

		}

		return files;
	}

	public void uploadDocPopUp() {

		sessionBackingBean.setEmpNo(selectedRequestNo);
		sessionBackingBean.setEmpTransaction("SURVEY");

		mandatoryList = documentManagementService.mandatoryDocsForUserManagement("08", selectedRequestNo);
		optionalList = documentManagementService.optionalDocsForUserManagement("08", selectedRequestNo);

		sessionBackingBean.surveyMandatoryDocumentList = documentManagementService
				.surveyMandatoryList(selectedRequestNo);
		sessionBackingBean.surveyOptionalDocumentList = documentManagementService.surveyOptionalList(selectedRequestNo);

		RequestContext.getCurrentInstance().execute("PF('uploadSurveyDocument').show()");

	}

	public void viewTimeTableAct() {

	}

	public void viewFareTableAct() {

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public TrafficProposalDTO getTrafficProposalDTO() {
		return trafficProposalDTO;
	}

	public void setTrafficProposalDTO(TrafficProposalDTO trafficProposalDTO) {
		this.trafficProposalDTO = trafficProposalDTO;
	}

	public String getSelectedRequestNo() {
		return selectedRequestNo;
	}

	public void setSelectedRequestNo(String selectedRequestNo) {
		this.selectedRequestNo = selectedRequestNo;
	}

	public List<TrafficProposalDTO> getRequestNoList() {
		return requestNoList;
	}

	public void setRequestNoList(List<TrafficProposalDTO> requestNoList) {
		this.requestNoList = requestNoList;
	}

	public List<TrafficProposalDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<TrafficProposalDTO> dataList) {
		this.dataList = dataList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public TrafficProposalDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(TrafficProposalDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public boolean isShowDetailsPanels() {
		return showDetailsPanels;
	}

	public void setShowDetailsPanels(boolean showDetailsPanels) {
		this.showDetailsPanels = showDetailsPanels;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isDisabledPrintTrafBtn() {
		return disabledPrintTrafBtn;
	}

	public void setDisabledPrintTrafBtn(boolean disabledPrintTrafBtn) {
		this.disabledPrintTrafBtn = disabledPrintTrafBtn;
	}

	public TrafficProposalDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(TrafficProposalDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public boolean isReadOnlyInputRemarks() {
		return readOnlyInputRemarks;
	}

	public void setReadOnlyInputRemarks(boolean readOnlyInputRemarks) {
		this.readOnlyInputRemarks = readOnlyInputRemarks;
	}

	public boolean isDisabledWithSelectedRow() {
		return disabledWithSelectedRow;
	}

	public void setDisabledWithSelectedRow(boolean disabledWithSelectedRow) {
		this.disabledWithSelectedRow = disabledWithSelectedRow;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
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

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisabledEditBtn() {
		return disabledEditBtn;
	}

	public void setDisabledEditBtn(boolean disabledEditBtn) {
		this.disabledEditBtn = disabledEditBtn;
	}

	public boolean isDisabledRePrintTrafBtn() {
		return disabledRePrintTrafBtn;
	}

	public void setDisabledRePrintTrafBtn(boolean disabledRePrintTrafBtn) {
		this.disabledRePrintTrafBtn = disabledRePrintTrafBtn;
	}

}
