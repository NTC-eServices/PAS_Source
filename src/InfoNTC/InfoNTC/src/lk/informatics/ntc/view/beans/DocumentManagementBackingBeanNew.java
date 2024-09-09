package lk.informatics.ntc.view.beans;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.component.filedownload.FileDownloadActionListener;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.util.SystemPropertyUtils;

import com.sun.media.sound.FFT;

import javafx.scene.control.Alert;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "documentManagementBeanNew")
@ViewScoped
public class DocumentManagementBackingBeanNew implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public DocumentManagementDTO documentManagement = new DocumentManagementDTO();
	private DocumentManagementDTO selectedEditRow;
	private DocumentManagementDTO selectedRemovePathRow;
	private DocumentManagementDTO documentManagementEdit = new DocumentManagementDTO();
	private DocumentManagementDTO documentManagementDelete = new DocumentManagementDTO();
	private DocumentManagementDTO documentManagementDTO = new DocumentManagementDTO();
	private DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
	private DocumentManagementDTO documentPermitInfo = new DocumentManagementDTO();
	private DocumentManagementDTO viewDocumentHistory = new DocumentManagementDTO();
	private DocumentManagementDTO viewUserManagementDocumentHistory = new DocumentManagementDTO();

	private DocumentManagementDTO selectedRow;
	// private boolean

	private String errorMsg;
	private String transactionType;
	private String doc_Code;
	private boolean response;
	private boolean mandatory;
	private String seqNo;
	private String uploadPermitNo;
	private String refNo;

	private String successMsg;
	private boolean checkSearch;

	private boolean viewAllTable;
	private boolean viewUserManagementTable;

	private boolean showNewPermitSearch, showTenderSearch, showSurveySearch, showAmendmentsSearch, showPermitRenewal;
	private boolean permitPanel, applicationPanel, btnPanel, tenderRefPanel;
	private boolean surveyUpload, fileUpload, tenderUpload, amendmentUpload;

	private String alertMessage;
	private String docErrorMsg;
	private String warningMsg;

	private FileUploadEvent file;
	private StreamedContent downlodfile;
	private String generate;

	private String uploadTransactionType;
	private String uploadPermit;
	private String uploadApplication;

	private String newDocCode;
	private String newDocDes;
	private String createdBy;

	private boolean currentChecked = false;
	private boolean currentCheckedForCancelationDocs = false;
	private boolean amendmentCheck = false;
	private boolean historyBtn;
	private boolean permitForRenewals;
	private boolean checkdelete = true;
	private String uploadPermitNoVal;
	private String trNTypeCodeDesVal;
	private String permitType;
	private String appType;
	private String selectPermitType;
	private String selectAppType;

	private DocumentManagementService documentManagementService;
	private EmployeeProfileService employeeProfileService;
	private TenderService tenderService;

	private List<String> transactionList = new ArrayList<String>();
	private List<String> addDocumentTransactionList = new ArrayList<String>();
	private List<String> docCodeList = new ArrayList<String>();
	private List<String> docDescriptionList = new ArrayList<String>();
	private List<DocumentManagementDTO> docDataList = new ArrayList<DocumentManagementDTO>();
	private List<String> permitNoList = new ArrayList<String>();
	private List<String> allPermitNoList = new ArrayList<String>();
	private List<String> applicationNoList = new ArrayList<String>();
	private List<DocumentManagementDTO> deleteDocument = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> mandatoryViewList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> optionalViewList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> checkList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> permitInfo = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> userManagementMandatoryList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> userManagementOptionalList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> historyViewList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> historyUserManagementViewList = new ArrayList<DocumentManagementDTO>();
	private List<String> empNoList = new ArrayList<String>(0);
	private List<String> joinVehicleNoList = new ArrayList<String>();
	private List<String> joinAppNoList = new ArrayList<String>();

	private List<DocumentManagementDTO> mandatoryUserManagementList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> optionalUserManagementList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> mandatoryViewUserManagementList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO> optionalViewUserManagementList = new ArrayList<DocumentManagementDTO>();
	private List<DocumentManagementDTO>filterdTransacrtions = null;
	private boolean disableUntilPermitSelect= true;
	private boolean disableUntilPermitAndTransSelect = true;
	@PostConstruct
	public void init() {
		String typeCode = "";

		try {
			typeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.usermanagement");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		transactionList = documentManagementService.GetTrasactionType(typeCode);
		addDocumentTransactionList = documentManagementService.GetAddDocumentTrasactionType();
		docCodeList = documentManagementService.GetDocCode();
		docDescriptionList = documentManagementService.GetDocDescription();

		empNoList = employeeProfileService.getEmpNo();
		
		allPermitNoList =documentManagementService.getPermitNumbersForView();
		Set<String> set = new HashSet<>(allPermitNoList);
		allPermitNoList.clear();
		allPermitNoList.addAll(set);
		/**All Permit numbers shows according to CR from NTC 21/03/2022**/
		setShowAmendmentsSearch(false);
		setShowNewPermitSearch(false);
		setShowTenderSearch(false);
		setShowSurveySearch(false);
		setShowPermitRenewal(false);

		setPermitPanel(false);
		setApplicationPanel(false);
		setBtnPanel(false);

		int delete = documentManagementService.DeleteTemp();
		
		currentChecked = false;

	}

	public List<DocumentManagementDTO>transactionTypeFilter(){
		String selectedPermitNumber =uploaddocumentManagementDTO.getUpload_Permit();
		if(uploaddocumentManagementDTO.getUpload_Permit() != null && 
				!uploaddocumentManagementDTO.getUpload_Permit().isEmpty()) {
			disableUntilPermitSelect = false;
			filterdTransacrtions =	documentManagementService.getTransactionsByPermitNumber(uploaddocumentManagementDTO.getUpload_Permit());
			// Transaction type get from nt_t_application_document table
		}
		uploaddocumentManagementDTO.setUpload_Permit(selectedPermitNumber);	
		return filterdTransacrtions;
	}
	public List<String> completeTrarnsaction(String query) {
		List<String> allPermits = transactionList;
		List<String> filteredPermits = new ArrayList<String>();
		for (int i = 0; i < allPermits.size(); i++) {
			if (allPermits.get(i).contains(query)) {
				filteredPermits.add(allPermits.get(i));
			}
		}

		return filteredPermits;
	}

	public List<String> completeDocCode(String query) {
		List<String> allPermits = docCodeList;
		List<String> filteredPermits = new ArrayList<String>();
		for (int i = 0; i < allPermits.size(); i++) {
			if (allPermits.get(i).contains(query)) {
				filteredPermits.add(allPermits.get(i));
			}
		}

		return filteredPermits;
	}

	public void selection() {

		mandatoryList.clear();
		optionalList.clear();
		mandatoryViewList.clear();
		optionalViewList.clear();
		
		setShowAmendmentsSearch(false);
		setShowNewPermitSearch(false);
		setShowPermitRenewal(false);
		setShowSurveySearch(false);
		setShowTenderSearch(false);
		setTenderRefPanel(false);

		uploaddocumentManagementDTO.setUpload_Application(null);
		
		setRefNo(null);
		disableUntilPermitAndTransSelect =false;
	
		String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
		String selectedPermitNo =uploaddocumentManagementDTO.getUpload_Permit();
		
		
		if (typeCodeTrans.equals("05") || typeCodeTrans.equals("13") || typeCodeTrans.equals("14")
				|| typeCodeTrans.equals("15") || typeCodeTrans.equals("16") || typeCodeTrans.equals("22")
				|| typeCodeTrans.equals("21") || typeCodeTrans.equals("23")) {

			setTenderRefPanel(false);
			setPermitPanel(false);
			setApplicationPanel(true);
			setPermitForRenewals(true);
			setPermitType("Permit No.");
			setAppType("Application No.");
			
			applicationNoList = documentManagementService.GetApplicationNoListByPermitAndTransType(typeCodeTrans,selectedPermitNo);
			//application numbers get from nt_t_application_document
			setShowAmendmentsSearch(true);

		}

		if (typeCodeTrans.equals("03")) {
			setTenderRefPanel(false);
			setPermitPanel(true);
			setApplicationPanel(true);
			setPermitForRenewals(false);
			setPermitType("Permit No.");
			setAppType("Application No.");
			applicationNoList = documentManagementService.GetApplicationNoListByPermitAndTransType(typeCodeTrans,selectedPermitNo);

			setShowNewPermitSearch(true);

		}

		if (typeCodeTrans.equals("04")) {
			setTenderRefPanel(false);
			setPermitPanel(false);
			setApplicationPanel(true);
			setPermitForRenewals(true);
			setPermitType("Permit No.");
			setAppType("Application No.");

			applicationNoList = documentManagementService.GetApplicationNoListByPermitAndTransType(typeCodeTrans,selectedPermitNo);

			setShowPermitRenewal(true);

		}
		if (typeCodeTrans.equals("10")) {
			setTenderRefPanel(false);
			setPermitPanel(false);
			setApplicationPanel(true);
			setPermitForRenewals(true);
			setPermitType("Permit No.");
			setAppType("Application No.");

			applicationNoList = documentManagementService.GetApplicationNoListByPermitAndTransType(typeCodeTrans,selectedPermitNo);

			setShowPermitRenewal(true);

		}

		if (typeCodeTrans.equals("08")) {
			setTenderRefPanel(false);
			setPermitPanel(true);
			setApplicationPanel(true);
			setPermitForRenewals(false);
			setPermitType("Survey No.");
			setAppType("Survey Request No.");
			setSelectPermitType("--Select Survey No. --");
			setSelectAppType("--Select Request No. --");
			//permitNoList = documentManagementService.GetSurveyNoListForSurvey();
			applicationNoList = documentManagementService.GetSurveyRequestNoListForSurvey();

			setShowSurveySearch(true);

		}

		if (typeCodeTrans.equals("01")) {

			setTenderRefPanel(true);
			setPermitPanel(false);
			setApplicationPanel(true);
			setPermitForRenewals(false);
			setAppType("Tender Application No.");
			setSelectAppType("--Select Application No. --");
			applicationNoList = documentManagementService.GetTenderApplicationNoListForTender();
			setShowTenderSearch(true);

		}
		searchViewDoc();

	}

	public void generateDescription() {
		String result = documentManagementService.getDescriptionWithCode(documentManagement);
		if (result != null) {
			documentManagement.setAdd_Doc_Description(result);
			
		} else {
			documentManagement.setAdd_Doc_Description(null);
			
		}

	}

	public void generateCode() {
		String result = documentManagementService.getCodeWithDescription(documentManagement);

		if (result != null) {
			documentManagement.setDoc_Code(result);
			
		} else {
			documentManagement.setDoc_Code(null);
			
		}
	}

	public void generateApplicationNo() {

		mandatoryList.clear();
		optionalList.clear();
		mandatoryViewList.clear();
		optionalViewList.clear();
		if (showNewPermitSearch == true) {

			uploadPermit = uploaddocumentManagementDTO.getUpload_Permit();
			String appNo = documentManagementService.getApplicationNoWithPermit(uploadPermit);
			uploaddocumentManagementDTO.setUpload_Application(appNo);

		}

		if (showPermitRenewal == true) {

			uploadPermit = uploaddocumentManagementDTO.getUpload_Permit();
			String appNo = documentManagementService.getApplicationNoWithPermitRenewal(uploadPermit);
			uploaddocumentManagementDTO.setUpload_Application(appNo);

		}

		if (showAmendmentsSearch == true) {

			uploadPermit = uploaddocumentManagementDTO.getUpload_Permit();
			String appNo = documentManagementService.getApplicationNoWithPermitForAmendment(uploadPermit);
			uploaddocumentManagementDTO.setUpload_Application(appNo);
		}

		if (showSurveySearch == true) {

			uploadPermit = uploaddocumentManagementDTO.getUpload_Permit();
			String appNo = documentManagementService.getSurveyRequestNoWithSurveyNoforSurvey(uploadPermit);
			uploaddocumentManagementDTO.setUpload_Application(appNo);

		}

	}

	public void generatePermitNo() {
		mandatoryList.clear();
		optionalList.clear();
		mandatoryViewList.clear();
		optionalViewList.clear();
		setBtnPanel(true);
		

	}

	// ADD NEW DOCUMENT PAGE METHODS

	public void addBtn() {

		if ((!documentManagement.getTransaction_Type().isEmpty() && documentManagement.getTransaction_Type() != null
				&& !documentManagement.getTransaction_Type().equalsIgnoreCase(""))
				&& (!documentManagement.getDoc_Code().isEmpty() && documentManagement.getDoc_Code() != null
						&& !documentManagement.getDoc_Code().equalsIgnoreCase(""))) {
			documentManagement.setCreated_by(sessionBackingBean.loginUser);
			
			String type = documentManagement.getTransaction_Type();

			String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);

			documentManagement.setTransaction_Type_Code(type);
			documentManagement.setTransaction_Type_Code(typeCode);

			checkList = documentManagementService.check(documentManagement, typeCode);


			if (documentManagement.getCheckTrans() == null || documentManagement.getCheckDocCode() == null) {

				int result = documentManagementService.saveDocumentTypes(documentManagement);
				if (result == 0) {
					documentManagementDTO = new DocumentManagementDTO();

					documentManagementDTO.setAdd_Doc_Description(documentManagement.getAdd_Doc_Description());
					documentManagementDTO.setDoc_Code(documentManagement.getDoc_Code());
					documentManagementDTO.setDoc_Respond(documentManagement.isDoc_Respond());
					documentManagementDTO.setDoc_Mandatory(documentManagement.isDoc_Mandatory());
					documentManagementDTO.setTransaction_Type(documentManagement.getTransaction_Type());
					documentManagementDTO.setSeq(documentManagement.getSeq());

					docDataList.add(documentManagementDTO);

					documentManagement.setAdd_Doc_Description(null);
					documentManagement.setSeq(null);
					documentManagement.setDoc_Code(null);
					documentManagement.setTransaction_Type(null);
					documentManagement.setDoc_Respond(false);
					documentManagement.setDoc_Mandatory(false);
					documentManagement.setCheckDocCode(null);
					documentManagement.setCheckTrans(null);

					setSuccessMsg("Successfully Added.");
					RequestContext.getCurrentInstance().update("successSveUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

				} else {
					setErrorMsg("Error");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			}

			else {

				
				setErrorMsg("Duplicate Record Found");
				RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
				RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

			}

		} else if (documentManagement.getTransaction_Type() == null
				|| documentManagement.getTransaction_Type().equalsIgnoreCase("")) {

			setErrorMsg("Please Select a Transaction Type");
			RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
			RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
		} else if (documentManagement.getDoc_Code() == null || documentManagement.getDoc_Code().equalsIgnoreCase("")) {

			setErrorMsg("Please Select a Document Code");
			RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
			RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
		}
	}

	public void search() {

		String type = documentManagement.getTransaction_Type();

		String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
		if (type == "") {
			typeCode = "";
		}

		doc_Code = documentManagement.getDoc_Code();
		response = documentManagement.isDoc_Respond();
		mandatory = documentManagement.isDoc_Mandatory();
		docDataList = documentManagementService.serachAllDetailsForDoc(typeCode, doc_Code, response, mandatory, type);

		if (docDataList.isEmpty()) {
			setWarningMsg("No Records Found");
			RequestContext.getCurrentInstance().update("frmwarningField");
			RequestContext.getCurrentInstance().execute("PF('warningField').show()");
		}

	}

	public void cancel() {
		documentManagement.setAdd_Doc_Description(null);
		documentManagement.setSeq(null);
		documentManagement.setDoc_Code(null);
		documentManagement.setTransaction_Type(null);
		documentManagement.setDoc_Respond(false);
		documentManagement.setDoc_Mandatory(false);
		docDataList.clear();

	}

	public void showEdit() {

		documentManagementEdit.setSeq(selectedEditRow.getSeq());
		
		documentManagementEdit.setDoc_Mandatory(selectedEditRow.isDoc_Mandatory());
		documentManagementEdit.setDoc_Respond(selectedEditRow.isDoc_Respond());

		Long seq = selectedEditRow.getSeq();
		String user = sessionBackingBean.getLoginUser();
		int selectedRowdata = documentManagementService.getSelectedRow(seq, documentManagement);

		if (documentManagement.isDoc_Mandatory() != documentManagementEdit.isDoc_Mandatory()
				|| documentManagement.isDoc_Respond() != documentManagementEdit.isDoc_Respond()) {
			int result = documentManagementService.updateNewDocTypes(documentManagementEdit, user);

			setSuccessMsg("Successfully Edited.");
			RequestContext.getCurrentInstance().update("successSveUploadDoc");
			RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

		} else {
			setErrorMsg("No changes Done");
			RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
			RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
			
		}

	}

	public void delete() {

		int result = documentManagementService.DeleteDoc(documentManagementDelete);
		if (result == 0) {

			setSuccessMsg("Successfully Deleted.");
			RequestContext.getCurrentInstance().update("successSveUploadDoc");
			RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

			docDataList.remove(selectedEditRow);

		}

	}

	public void deleteDialog() {
		documentManagementDelete.setSeq(selectedEditRow.getSeq());

		RequestContext.getCurrentInstance().execute("PF('deleteconfirmationRole').show()");
	}

	// UPLOAD DOCUMENTS PAGE METHODS

	public void searchDocumentUpload() {

		if (!uploaddocumentManagementDTO.getUpload_Application().isEmpty()
				&& uploaddocumentManagementDTO.getUpload_Application() != null) {
			
			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			String typeTrans = documentManagementService.getTranactionDescription(typeCodeTrans);
			

			if (showAmendmentsSearch == true) {

				setAmendmentUpload(true);
				setFileUpload(false);
				setSurveyUpload(false);
				setTenderUpload(false);

				sessionBackingBean.setApplicationNoForDoc(uploaddocumentManagementDTO.getUpload_Application());
				sessionBackingBean.setPermitRenewalTranstractionTypeDescription(
						uploaddocumentManagementDTO.getUpload_TransactionType());
				sessionBackingBean.setPermitRenewalPermitNo(uploaddocumentManagementDTO.getUpload_Permit());

				mandatoryList = documentManagementService.mandatoryDocsForAmendments(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit(),
						uploaddocumentManagementDTO.getUpload_Application());
				optionalList = documentManagementService.optionalDocsForAmendments(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit(),
						uploaddocumentManagementDTO.getUpload_Application());
			}

			if (showNewPermitSearch == true || showPermitRenewal == true) {

				setFileUpload(true);
				setSurveyUpload(false);
				setTenderUpload(false);
				setAmendmentUpload(false);
				mandatoryList = documentManagementService.mandatoryDocs(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit());
				optionalList = documentManagementService.optionalDocs(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit());

			}

			if (showSurveySearch == true) {

				setFileUpload(false);
				setSurveyUpload(true);
				setTenderUpload(false);
				setAmendmentUpload(false);
				sessionBackingBean.setEmpNo(uploaddocumentManagementDTO.getUpload_Application());
				sessionBackingBean.setEmpTransaction(typeTrans);
				mandatoryList = documentManagementService.userManagementMandatoryDocs(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Application());
				optionalList = documentManagementService.userManagementOptionalDocs(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Application());
			}

			if (showTenderSearch == true) {

				setFileUpload(false);
				setSurveyUpload(false);
				setTenderUpload(true);
				setAmendmentUpload(false);

				sessionBackingBean.setApplicationNoForDoc(uploaddocumentManagementDTO.getUpload_Application());
				sessionBackingBean.setPermitRenewalPermitNo(refNo);
				sessionBackingBean.setPermitRenewalTranstractionTypeDescription(typeTrans);

				mandatoryList = documentManagementService.mandatoryDocsForTender(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Application());
				optionalList = documentManagementService.optionalDocsForTender(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Application());

			}

		} else {
			setErrorMsg("Please Select a Application No.");
			RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
			RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

		}

	}

	public void clearAction() {

		uploaddocumentManagementDTO.setUpload_TransactionType(null);
		uploaddocumentManagementDTO.setUpload_Permit(null);
		uploaddocumentManagementDTO.setUpload_Application(null);
		mandatoryList.clear();
		optionalList.clear();
		mandatoryViewList.clear();
		optionalViewList.clear();
		checkSearch = false;
		historyBtn = false;
		setShowAmendmentsSearch(false);
		setPermitForRenewals(false);
		setShowNewPermitSearch(false);
		setShowPermitRenewal(false);
		setShowSurveySearch(false);
		setShowTenderSearch(false);
		setPermitPanel(false);
		setApplicationPanel(false);
		setBtnPanel(false);
		setTenderRefPanel(false);
		applicationNoList =null;
		filterdTransacrtions = null;
		disableUntilPermitSelect = true;
		disableUntilPermitAndTransSelect = true;

	}

	// mandatory upload and view documents
	public void saveMandatoryUploadDocuments(FileUploadEvent event) {
		currentChecked = sessionBackingBean.isGoToPermitRenewalUploadDocPopUp();
		currentCheckedForCancelationDocs = sessionBackingBean.isClickedCancelationDocPopup();

		if (currentChecked == true) {

			
			uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getApplicationNoForDoc());
			uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getPermitRenewalPermitNo());
			uploaddocumentManagementDTO
					.setUpload_TransactionType(sessionBackingBean.getPermitRenewalTranstractionTypeDescription());

		}

		if (currentCheckedForCancelationDocs == true) {
			uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getCancelationPermitNo());
			uploaddocumentManagementDTO
					.setUpload_TransactionType(sessionBackingBean.getCancelationTranstractionTypeDes());
		}

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");
			
			CreateDirectory();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}
			String appPath = null;
			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
			String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 

			if (uploaddocumentManagementDTO.getUpload_TransactionType().equals("RENEWAL")) {

				appPath = docTransactionType + File.separator + uploaddocumentManagementDTO.getUpload_Application();

				File file3 = new File(appPath);
				if (!file3.exists()) {
					if (file3.mkdir()) {
						
					} else {
						
						setErrorMsg("Upload Failed");
						RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
					}
				} 

			}

			
			String documentPath = propertyFilePath;
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

		
			
			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			
			String docType = "M";

			int versionNo = documentManagementService.getVersionNumber(uploaddocumentManagementDTO, typeCodeTrans,
					docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = null;

			if (uploaddocumentManagementDTO.getUpload_TransactionType().equals("RENEWAL")) {

				docuString = appPath + File.separator + str;

			} else {

				docuString = tempDocumentPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit()
						+ File.separator + "Mandatory Documents" + File.separator
						+ uploaddocumentManagementDTO.getUpload_TransactionType() + File.separator + str;
			}
			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			if (files.exists()) {
				if (!files.mkdirs()) {
					copyFile(docuString, in);

					obj.setUploadFilePath_file(docuString);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

					obj.setUploadFilePath_file(docuString);

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();

					String permitNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						permitNo = (String) event.getComponent().getAttributes().get("permitNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePath(filePath, doc_des, permitNo, transaction_Code,
							doc_code, applicationNo);

					
					if (file != null) {
						int result2 = documentManagementService.replaceDoc(doc_des, permitNo, transaction_Code, user,
								docuString);
						int result1 = documentManagementService.saveMandatoryUploadsVersions(filePath, doc_des,
								doc_code, permitNo, applicationNo, transaction_Code, user, versionNo);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							
							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploads(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user);

						int result1 = documentManagementService.saveMandatoryUploadsVersions(filePath, doc_des,
								doc_code, permitNo, applicationNo, transaction_Code, user, versionNo);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");


						sessionBackingBean.newPermitMandatoryDocumentList = documentManagementService
								.newPermitMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.permitRenewalMandatoryDocumentList = documentManagementService
								.permitRenewalMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());

					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void viewAction(ActionEvent actionEvent) throws IOException {
		try {
		
			DocumentManagementDTO dto = (DocumentManagementDTO) actionEvent.getComponent().getAttributes()
					.get("documentPathMandatory");
			downlodfile = null;
			
			String pathString = dto.getUploadFilePath().trim();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			byte[] file = null;
			try {
				file = FileUtils.readFileToByteArray(new File(pathString));
				sessionMap.put("reportBytes", file);
				sessionMap.put("docType", FilenameUtils.getExtension(pathString));

			} catch (Exception e) {
				e.printStackTrace();
			}

			/*String str = dto.getUploadFilePath();
			Pattern regex = Pattern.compile("\\.(\\S+)");
			Matcher match = regex.matcher(str);

			final InputStream stream = Files.newInputStream(Paths.get(dto.getUploadFilePath().trim()));
			downlodfile = new DefaultStreamedContent(stream, "application/" + match.group(1) + "",
					dto.getAdd_Doc_Description());

			File fileIS = new File(dto.getUploadFilePath());
			FileInputStream fileInputStream;

			fileInputStream = new FileInputStream(fileIS);
			downlodfile = new DefaultStreamedContent(fileInputStream, "Application/" + match.group(1) + "",
					dto.getAdd_Doc_Description() + "." + match.group(1));

			RequestContext context = RequestContext.getCurrentInstance();
			context.update(":frmdoc");

			
			String pathString = dto.getUploadFilePath().trim();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			byte[] file = null;

			file = FileUtils.readFileToByteArray(new File(pathString));
			sessionMap.put("reportBytes", file);
			sessionMap.put("docType", FilenameUtils.getExtension(pathString));*/

		} catch (Exception e) {
			sessionBackingBean.setMessage("Document not found");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}
	public void CreateDirectory() {
		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;


		if (currentChecked == true) {
			
			uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getPermitRenewalPermitNo());

		}

		if (currentCheckedForCancelationDocs == true) {
			uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getCancelationPermitNo());
			uploaddocumentManagementDTO
					.setUpload_TransactionType(sessionBackingBean.getCancelationTranstractionTypeDes());
		}

		String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
		

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
			
			} 
		} 

		String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
		
			}
		} 

		String optionalPath = newCreatedPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
				
			} 
		} 
	}

	// optional upload and view documents
	public void saveOptionalUploadDocuments(FileUploadEvent event) {
		currentChecked = sessionBackingBean.isGoToPermitRenewalUploadDocPopUp();

		if (currentChecked == true) {

			
			uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getApplicationNoForDoc());
			uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getPermitRenewalPermitNo());
			uploaddocumentManagementDTO
					.setUpload_TransactionType(sessionBackingBean.getPermitRenewalTranstractionTypeDescription());

		}
		currentCheckedForCancelationDocs = sessionBackingBean.isClickedCancelationDocPopup();
		if (currentCheckedForCancelationDocs == true) {
			uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getCancelationPermitNo());
			uploaddocumentManagementDTO
					.setUpload_TransactionType(sessionBackingBean.getCancelationTranstractionTypeDes());
		}

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectory();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String appPath = null;
			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
			String mandatoryPath = newCreatedPath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
				
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 

			if (uploaddocumentManagementDTO.getUpload_TransactionType().equals("RENEWAL")) {

				appPath = docTransactionType + File.separator + uploaddocumentManagementDTO.getUpload_Application();

				File file3 = new File(appPath);
				if (!file3.exists()) {
					if (file3.mkdir()) {
						
					} else {
						
						setErrorMsg("Upload Failed");
						RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
					}
				}

			}

			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;
			String doc = tempDocumentPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit()
					+ File.separator + "Optional Documents" + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			
			
			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();

			String docType = "O";

			int versionNo = documentManagementService.getVersionNumber(uploaddocumentManagementDTO, typeCodeTrans,
					docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = null;

			if (uploaddocumentManagementDTO.getUpload_TransactionType().equals("RENEWAL")) {

				docuString = appPath + File.separator + str;

			} else {

				docuString = tempDocumentPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit()
						+ File.separator + "Optional Documents" + File.separator
						+ uploaddocumentManagementDTO.getUpload_TransactionType() + File.separator + str;
			}
			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					String documentCode = obj.getDoc_Code();
					int changeActive = documentManagementService.updateAddNewDocument(documentCode);
					

					String output = documentManagementService.checkDocCodeWhenUploading(documentCode);

					if (output == "") {

						String code = obj.getDoc_Code();
						String description = obj.getAdd_Doc_Description();
						String user = sessionBackingBean.loginUser;

						int input = documentManagementService.saveaddnewDocumentWhenUploading(code, description, user);

						
					}
					obj.setUploadFilePath(doc);
					obj.setUploadOptionalFilePath(docuString);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					

					obj.setUploadFilePath(doc);
					obj.setUploadOptionalFilePath(docuString);

					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();
					String permitNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						permitNo = (String) event.getComponent().getAttributes().get("permitNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePath(filePath, doc_des, permitNo,
							transaction_Code, applicationNo);

					String filenew = documentManagementService.getAddNewDocFilePath(filePath, doc_des, permitNo,
							transaction_Code);

				

					if (file != null || filenew != null) {
						
						int result2 = documentManagementService.replaceOptionalDoc(doc_des, permitNo, transaction_Code,
								user, docuString);
						int result1 = documentManagementService.saveOptionalUploadsVersions(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user, versionNo);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploads(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user);

						int result1 = documentManagementService.saveOptionalUploadsVersions(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user, versionNo);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						
						sessionBackingBean.backlogManagementOptionalDocumentList = documentManagementService
								.backlogManagementOptionalList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.newPermitOptionalDocumentList = documentManagementService
								.newPermitOptionalList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.permitRenewalOptionalDocumentList = documentManagementService
								.permitRenewalOptionalList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
					
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void viewOptionalDocAction(ActionEvent actionEvent) throws IOException {

		try {
			DocumentManagementDTO dto = (DocumentManagementDTO) actionEvent.getComponent().getAttributes()
					.get("documentPathOptional");

			downlodfile = null;

			
			String pathString = dto.getUploadOptionalFilePath().trim();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			byte[] file = null;
			try {
				file = FileUtils.readFileToByteArray(new File(pathString));
				sessionMap.put("reportBytes", file);
				sessionMap.put("docType", FilenameUtils.getExtension(pathString));

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			sessionBackingBean.setMessage("Document not found");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}
	
	// add new document pop-up
	public void addNewDocument() {
		if (checkSearch == true) {

			RequestContext.getCurrentInstance().execute("PF('viewAddDoc').show()");
		} else {

			setErrorMsg("Please Add New Document Types After The Search");
			RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
			RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

		}
	}
	public void addNewDocumentSave() {
		setDocErrorMsg(null);
		createdBy = sessionBackingBean.loginUser;
		int checkDoc = documentManagementService.getDocumentDetailsCode(documentManagement, newDocCode, newDocDes);
		int checkDoc2 = documentManagementService.getDocumentDetailsDes(documentManagement, newDocCode, newDocDes);

		
		if (newDocCode.isEmpty() || newDocDes.isEmpty()) {
			
			if (newDocCode.isEmpty() && newDocDes.isEmpty()) {
				
				setDocErrorMsg("Please insert a Document Code");

			} else if (!newDocCode.isEmpty() && newDocDes.isEmpty()) {

				
				setDocErrorMsg("Please insert a Document Description");

			}

		} else if (documentManagement.getAddCheckDocCode() != null || documentManagement.getAddCheckDocDes() != null) {
			setDocErrorMsg("Duplicate Record Found");
			documentManagement.setAddCheckDocDes(null);
			documentManagement.setAddCheckDocCode(null);
			setNewDocCode(null);
			setNewDocDes(null);

		} else {

			int result = documentManagementService.saveaddnewDocument(newDocCode, newDocDes, createdBy);
			if (result == 0) {
				boolean addRenewal = false;
				boolean addCancel = false;

				DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
				RequestContext.getCurrentInstance().execute("PF('viewAddDoc').hide()");

				setSuccessMsg("Successfully Added.");
				RequestContext.getCurrentInstance().update("successSveUploadDoc");
				RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");
				uploaddocumentManagementDTO.setDoc_Code(newDocCode);
				uploaddocumentManagementDTO.setAdd_Doc_Description(newDocDes);
				uploaddocumentManagementDTO.setUploadOptionalFilePath(null);
				uploaddocumentManagementDTO.setDoc_Respond(false);
				uploaddocumentManagementDTO.setTransaction_Type(null);

				currentChecked = sessionBackingBean.isGoToPermitRenewalUploadDocPopUp();
				currentCheckedForCancelationDocs = sessionBackingBean.isClickedCancelationDocPopup();
				if (currentChecked == true) {
					addRenewal = true;
					
					sessionBackingBean.optionalList.add(uploaddocumentManagementDTO);
				}

				if (currentCheckedForCancelationDocs == true) {
					addCancel = true;
					
					uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getCancelationPermitNo());
					uploaddocumentManagementDTO
							.setUpload_TransactionType(sessionBackingBean.getCancelationTranstractionTypeDes());
					sessionBackingBean.optionalList.add(uploaddocumentManagementDTO);

				}

				if (addCancel == false && addRenewal == false) {
					optionalList.add(uploaddocumentManagementDTO);
					optionalUserManagementList.add(uploaddocumentManagementDTO);

				}

				setNewDocCode(null);
				setNewDocDes(null);

			} 

		}

	}

	public void clear() {

		setNewDocCode(null);
		setNewDocDes(null);
		setDocErrorMsg(null);

	}

	public void onCancel() {
		setNewDocCode(null);
		setNewDocDes(null);
		setDocErrorMsg(null);
		
	}

	// VIEW UPLOAD DOCUMENTS PAGE METHODS

	public void searchViewDoc() {

		
			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
		
		if(typeCodeTrans.isEmpty() || typeCodeTrans.equals(null)
				||uploaddocumentManagementDTO.getUpload_Permit().isEmpty() ||
				uploaddocumentManagementDTO.getUpload_Permit()== null) {
			
			setErrorMsg("Please Select Mandatory Fields");
			RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
			RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
		}
		else {
			if(uploaddocumentManagementDTO.getUpload_Application()!= null
					&& !uploaddocumentManagementDTO.getUpload_Application().isEmpty()) {
			
				if (showAmendmentsSearch == true) {

				mandatoryViewList = documentManagementService.mandatoryDocsForAmendments(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit(),
						uploaddocumentManagementDTO.getUpload_Application());
				optionalViewList = documentManagementService.optionalDocsForAmendments(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit(),
						uploaddocumentManagementDTO.getUpload_Application());

			}
				else {
				
				mandatoryViewList = documentManagementService.mandatoryViewDocsByAppNo(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit(),
						uploaddocumentManagementDTO.getUpload_Application());
				optionalViewList = documentManagementService.optionalViewDocsByAppNo(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit(),
						uploaddocumentManagementDTO.getUpload_Application());
				}
			}
			else {
			if (showNewPermitSearch == true) {

				mandatoryViewList = documentManagementService.mandatoryViewDocs(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit());
				optionalViewList = documentManagementService.optionalViewDocs(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit());

			}

			if (showPermitRenewal == true) {

				mandatoryViewList = documentManagementService.mandatoryViewDocs(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit());
				optionalViewList = documentManagementService.optionalViewDocs(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit());

			}

			if (showSurveySearch == true) {

				mandatoryViewList = documentManagementService.mandatoryUserManagementViewDocs(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Application());
				optionalViewList = documentManagementService.optionalUserManagementViewDocs(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Application());
			}

			if (showTenderSearch == true) {

				mandatoryViewList = documentManagementService.mandatoryDocsForTender(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Application());

				optionalViewList = documentManagementService.optionalDocsForTender(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Application());

			}

			if (showAmendmentsSearch == true) {

				mandatoryViewList = documentManagementService.mandatoryDocsForAmendmentsWithOutAppNo(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit());
				optionalViewList = documentManagementService.optionalDocsForAmendmentsWithOutAppNo(typeCodeTrans,
						uploaddocumentManagementDTO.getUpload_Permit());

			}
			}
			if (mandatoryViewList.isEmpty() && optionalViewList.isEmpty()) {

				setWarningMsg("No Documents To View");
				RequestContext.getCurrentInstance().update("warningField");
				RequestContext.getCurrentInstance().execute("PF('warningField').show()");
			}

		}

	}

	public void viewMandatoryUploadDocs(ActionEvent actionEvent) throws IOException {
		try {
			DocumentManagementDTO dto = (DocumentManagementDTO) actionEvent.getComponent().getAttributes()
					.get("documentMandatory");
			downlodfile = null;
						
			String pathString = dto.getUploadFilePath().trim();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			byte[] file = null;
			try {
				file = FileUtils.readFileToByteArray(new File(pathString));
				sessionMap.put("reportBytes", file);
				sessionMap.put("docType", FilenameUtils.getExtension(pathString));

			} catch (Exception e) {
				e.printStackTrace();
			}

			/*String str = dto.getUploadFilePath();
			Pattern regex = Pattern.compile("\\.(\\S+)");
			Matcher match = regex.matcher(str);
			

			final InputStream stream = Files.newInputStream(Paths.get(dto.getUploadFilePath().trim()));
			downlodfile = new DefaultStreamedContent(stream, "application/" + match.group(1) + "",
					dto.getAdd_Doc_Description());

			File fileIS = new File(dto.getUploadFilePath());
			FileInputStream fileInputStream;

			fileInputStream = new FileInputStream(fileIS);
			downlodfile = new DefaultStreamedContent(fileInputStream, "Application/" + match.group(1) + "",
					dto.getAdd_Doc_Description() + "." + match.group(1));

			RequestContext context = RequestContext.getCurrentInstance();
			context.update(":frmdoc");

			
			String pathString = dto.getUploadFilePath().trim();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			byte[] file = null;

			file = FileUtils.readFileToByteArray(new File(pathString));
			sessionMap.put("reportBytes", file);
			sessionMap.put("docType", FilenameUtils.getExtension(pathString));*/

		} catch (Exception e) {
			sessionBackingBean.setMessage("Document not found");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}

	public void viewUploadHistory(ActionEvent actionEvent) throws IOException {

		DocumentManagementDTO obj = (DocumentManagementDTO) actionEvent.getComponent().getAttributes()
				.get("documentHistory");

		viewDocumentHistory.setAdd_Doc_Description(obj.getAdd_Doc_Description());
		viewDocumentHistory.setDoc_Code(obj.getDoc_Code());

		
		String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();

		viewDocumentHistory.setTransaction_Type_Code(typeCodeTrans);
		viewDocumentHistory.setUpload_Permit(uploaddocumentManagementDTO.getUpload_Permit());

		String result = documentManagementService.getuploadfilepath(viewDocumentHistory);
		

		viewDocumentHistory.setCommonUploadFilePath(result);

		historyViewList = documentManagementService.ViewHistoryList(viewDocumentHistory);

		RequestContext.getCurrentInstance().execute("PF('historyView').show()");

	}
	

	public void viewUploadedOptionalDocAction(ActionEvent actionEvent) throws IOException {

		try {
			DocumentManagementDTO dto = (DocumentManagementDTO) actionEvent.getComponent().getAttributes()
					.get("documentOptional");
			
			downlodfile = null;

			String pathString;
			if (dto.getUploadFilePath() != null && !dto.getUploadFilePath().isEmpty()) {
				pathString = dto.getUploadFilePath();
			} else {
				pathString = dto.getUploadOptionalFilePath() != null ? dto.getUploadOptionalFilePath().trim() : null;
			}
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			byte[] file = null;
			try {
				file = FileUtils.readFileToByteArray(new File(pathString));
				sessionMap.put("reportBytes", file);
				sessionMap.put("docType", FilenameUtils.getExtension(pathString));

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			/*String str = dto.getUploadFilePath();
			Pattern regex = Pattern.compile("\\.(\\S+)");
			Matcher match = regex.matcher(str);
		

			final InputStream stream = Files.newInputStream(Paths.get(dto.getUploadFilePath().trim()));
			downlodfile = new DefaultStreamedContent(stream, "application/" + match.group(1) + "",
					dto.getAdd_Doc_Description());

			File fileIS = new File(dto.getUploadFilePath());
			FileInputStream fileInputStream;

			fileInputStream = new FileInputStream(fileIS);
			downlodfile = new DefaultStreamedContent(fileInputStream, "Application/" + match.group(1) + "",
					dto.getAdd_Doc_Description() + "." + match.group(1));

			RequestContext context = RequestContext.getCurrentInstance();
			context.update(":frmdoc");

			String pathString = dto.getUploadFilePath().trim();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			byte[] file = null;

			file = FileUtils.readFileToByteArray(new File(pathString));
			sessionMap.put("reportBytes", file);
			sessionMap.put("docType", FilenameUtils.getExtension(pathString));*/

		} catch (Exception e) {
			sessionBackingBean.setMessage("Document not found");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}
	
	public void viewUploadedVersions(ActionEvent actionEvent) throws IOException {
		try {
			DocumentManagementDTO dto = (DocumentManagementDTO) actionEvent.getComponent().getAttributes()
					.get("documentVersion");
			downlodfile = null;

			String str = dto.getUploadFilePath();
			Pattern regex = Pattern.compile("\\.(\\S+)");
			Matcher match = regex.matcher(str);
			if (match.find())
				System.out.println(match.group(1));

		

			final InputStream stream = Files.newInputStream(Paths.get(dto.getUploadFilePath().trim()));
			downlodfile = new DefaultStreamedContent(stream, "application/" + match.group(1) + "",
					dto.getAdd_Doc_Description() + "_" + dto.getVersionNo());

			File fileIS = new File(dto.getUploadFilePath());
			FileInputStream fileInputStream;

			fileInputStream = new FileInputStream(fileIS);
			downlodfile = new DefaultStreamedContent(fileInputStream, "Application/" + match.group(1) + "",
					dto.getAdd_Doc_Description() + "_" + dto.getVersionNo() + "." + match.group(1));

			RequestContext context = RequestContext.getCurrentInstance();
			context.update(":frmdoc");

			
			String pathString = dto.getUploadFilePath().trim();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			byte[] file = null;

			file = FileUtils.readFileToByteArray(new File(pathString));
			sessionMap.put("reportBytes", file);
			sessionMap.put("docType", FilenameUtils.getExtension(pathString));

		} catch (Exception e) {
			sessionBackingBean.setMessage("Document not found");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}

	public void removeAction() {

		String path = selectedRemovePathRow.getUploadFilePath();

		String user = sessionBackingBean.getLoginUser();
		
		deleteDocument = documentManagementService.getDeleteDocumentInfo(path);

		int result = documentManagementService.insertDeleteRecord(deleteDocument, user);

		
		int result2 = documentManagementService.DeleteVersionDoc(path);

		if (result2 != 0) {
		

		} else {

			String str = path;
			Pattern regex = Pattern.compile("\\.(\\S+)");
			Matcher match = regex.matcher(str);
			if (match.find())
				System.out.println(match.group(1));
			
			String filePath = path;

			File file = new File(filePath);
			
			boolean f = file.delete();
			
			historyViewList = documentManagementService.ViewHistoryList(viewDocumentHistory);

			selectedRemovePathRow.setUploadFilePath(null);

		}

	}

	// UPLOAD USER MANAGEMENT DOCUMENTS PAGE METHODS

	public void searchUserManagementUpload() {
		if (!uploaddocumentManagementDTO.getUpload_empNo().isEmpty()
				&& uploaddocumentManagementDTO.getUpload_empNo() != null) {

			checkSearch = true;

			String typeCode = "";

			try {
				typeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.usermanagement");
			} catch (ApplicationException e) {
			
				e.printStackTrace();
			}
			String uploadEmpNo = uploaddocumentManagementDTO.getUpload_empNo();

			mandatoryUserManagementList = documentManagementService.userManagementMandatoryDocs(typeCode, uploadEmpNo);
			optionalUserManagementList = documentManagementService.userManagementOptionalDocs(typeCode, uploadEmpNo);

		} else {

			setErrorMsg("Please Select Employee No.");
			RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
			RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

		}
	}

	public void clearActionUserManagement() {

		uploaddocumentManagementDTO.setUpload_empNo(null);
		mandatoryUserManagementList.clear();
		optionalUserManagementList.clear();
		mandatoryViewUserManagementList.clear();
		optionalViewUserManagementList.clear();
		checkSearch = false;
		historyBtn = false;

	}

	public void saveMandatoryUserManagementUploadDocuments(FileUploadEvent event) {

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			
			createDirectoryUserManagementDocuments();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}
			uploaddocumentManagementDTO.setTransaction_Type("User Management");

			String originalPath = propertyFilePath;
			String createUMPath = originalPath + File.separator + "User Management";
			String newCreatedPath = createUMPath + File.separator + uploaddocumentManagementDTO.getUpload_empNo();
			String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";

			
			String documentPath = propertyFilePath;
			

			
			String tempDocumentPath = documentPath + File.separator + "User Management";
			String doc = tempDocumentPath + File.separator + uploaddocumentManagementDTO.getUpload_empNo()
					+ File.separator + "Mandatory Documents";

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			String empNo = uploaddocumentManagementDTO.getUpload_empNo();
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			
			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			String typeTrans = documentManagementService.getTranactionDescription(typeCodeTrans);

			 typeCodeTrans = "";

			try {
				typeCodeTrans = PropertyReader.getPropertyValue("queue.transaction.type.code.usermanagement");
			} catch (ApplicationException e) {
			
				e.printStackTrace();
			}
			String docType = "M";
			String doc_code = obj.getDoc_Code();

			int versionNo = documentManagementService.getVersionNumberForUserManagement(uploaddocumentManagementDTO,
					typeCodeTrans, docType);
			
			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = tempDocumentPath + File.separator + uploaddocumentManagementDTO.getUpload_empNo()
					+ File.separator + "Mandatory Documents" + File.separator + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					obj.setUploadFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();

					String transaction_Code = "";

					try {
						transaction_Code = PropertyReader
								.getPropertyValue("queue.transaction.type.code.usermanagement");
					} catch (ApplicationException e) {
						
						e.printStackTrace();
					}

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getUserManagementFilePath(filePath, doc_des, empNo,
							transaction_Code);

					
					if (file != null) {

						int result2 = documentManagementService.replaceUserManagementDoc(doc_des, empNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveUserManagementMandatoryUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);

						if (result2 != 0 || result1 != 0) {
							
						} else {

							String typeCode = "";

							try {
								typeCode = PropertyReader
										.getPropertyValue("queue.transaction.type.code.usermanagement");
							} catch (ApplicationException e) {
								
								e.printStackTrace();
							}

							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveUserManagementMandatoryUploads(filePath, doc_des,
								doc_code, empNo, transaction_Code, user);
						int result1 = documentManagementService.saveUserManagementMandatoryUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);

						if (result != 0 || result1 != 0) {
							
						} else {

							String typeCode = "";

							try {
								typeCode = PropertyReader
										.getPropertyValue("queue.transaction.type.code.usermanagement");
							} catch (ApplicationException e) {
								
								e.printStackTrace();
							}

							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully Uploaded. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						
					}

				} else {
				
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveOptionalUserManagementUploadDocuments(FileUploadEvent event) {

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectoryUserManagement();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
			
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String createUMPath = originalPath + File.separator + "User Management";
			String newCreatedPath = createUMPath + File.separator + uploaddocumentManagementDTO.getUpload_empNo();
			String mandatoryPath = newCreatedPath + File.separator + "Optional Documents";

			
			String documentPath = propertyFilePath;
			String tempDocumentPath = documentPath + File.separator + "User Management";
			String doc = tempDocumentPath + File.separator + uploaddocumentManagementDTO.getUpload_empNo()
					+ File.separator + "Optional Documents";
			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			String empNo = uploaddocumentManagementDTO.getUpload_empNo();
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = "";

			try {
				typeCodeTrans = PropertyReader.getPropertyValue("queue.transaction.type.code.usermanagement");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}
			String docType = "O";
			String doc_code = obj.getDoc_Code();

			int versionNo = documentManagementService.getVersionNumberForUserManagement(uploaddocumentManagementDTO,
					typeCodeTrans, docType);
			

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = tempDocumentPath + File.separator + uploaddocumentManagementDTO.getUpload_empNo()
					+ File.separator + "Optional Documents" + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					
					copyFile(docuString, in);

					obj.setUploadOptionalFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadOptionalFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();

					String transaction_Code = "";

					try {
						transaction_Code = PropertyReader
								.getPropertyValue("queue.transaction.type.code.usermanagement");
					} catch (ApplicationException e) {
						
						e.printStackTrace();
					}
					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getUserManagementFilePath(filePath, doc_des, empNo,
							transaction_Code);

					

					if (file != null) {

						int result2 = documentManagementService.replaceOptionalUserManagementDoc(doc_des, empNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveUserManagementOptionalUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);
						if (result2 != 0 || result1 != 0) {
						
						} else {

							String typeCode = "";

							try {
								typeCode = PropertyReader
										.getPropertyValue("queue.transaction.type.code.usermanagement");
							} catch (ApplicationException e) {
								
								e.printStackTrace();
							}
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveUserManagementOptionalUploads(filePath, doc_des,
								doc_code, empNo, transaction_Code, user);
						int result1 = documentManagementService.saveUserManagementOptionalUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);
						if (result != 0 || result1 != 0) {
						
						} else {

							String typeCode = "";

							try {
								typeCode = PropertyReader
										.getPropertyValue("queue.transaction.type.code.usermanagement");
							} catch (ApplicationException e) {
							
								e.printStackTrace();
							}
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully Uploaded. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void createDirectoryUserManagementDocuments() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;

		String createUMPath = originalPath + File.separator + "User Management";

		File file = new File(createUMPath);
		if (!file.exists()) {
			if (file.mkdir()) {
			
			}
		} 

		String newCreatedPath = createUMPath + File.separator + uploaddocumentManagementDTO.getUpload_empNo();
		

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
			
			} 
		} 

		String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
				
			} 
		} 

		String optionalPath = newCreatedPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
				
			} 
		} 

	}

	// VIEW USER MANAGEMENT DOCUMENTS PAGE METHODS

	public void searchViewUserManagement() {
		if (!uploaddocumentManagementDTO.getUpload_empNo().isEmpty()) {

			String typeCode = "";

			try {
				typeCode = PropertyReader.getPropertyValue("queue.transaction.type.code.usermanagement");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}
			String uploadEmpNo = uploaddocumentManagementDTO.getUpload_empNo();

			mandatoryViewUserManagementList = documentManagementService.mandatoryUserManagementViewDocs(typeCode,
					uploadEmpNo);
			optionalViewUserManagementList = documentManagementService.optionalUserManagementViewDocs(typeCode,
					uploadEmpNo);

		}
	}

	public void viewUserManagementUploadHistory(ActionEvent actionEvent) throws IOException {

		DocumentManagementDTO obj = (DocumentManagementDTO) actionEvent.getComponent().getAttributes()
				.get("documentHistory");

		viewUserManagementDocumentHistory.setAdd_Doc_Description(obj.getAdd_Doc_Description());
		viewUserManagementDocumentHistory.setDoc_Code(obj.getDoc_Code());

		String typeCodeTrans = "";

		try {
			typeCodeTrans = PropertyReader.getPropertyValue("queue.transaction.type.code.usermanagement");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}

		viewUserManagementDocumentHistory.setTransaction_Type_Code(typeCodeTrans);
		viewUserManagementDocumentHistory.setUpload_empNo(uploaddocumentManagementDTO.getUpload_empNo());

		String result = documentManagementService.getUserManagementuploadfilepath(viewUserManagementDocumentHistory);

		

		viewUserManagementDocumentHistory.setCommonUploadFilePath(result);

		historyUserManagementViewList = documentManagementService
				.ViewUserManagementHistoryList(viewUserManagementDocumentHistory);

		RequestContext.getCurrentInstance().execute("PF('historyView').show()");

	}

	public void removeActionUserManagement() {

		String empNo = uploaddocumentManagementDTO.getUpload_empNo();

		String transaction_Code = "";

		try {
			transaction_Code = PropertyReader.getPropertyValue("queue.transaction.type.code.usermanagement");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String docDes = selectedRemovePathRow.getAdd_Doc_Description();
		String path = selectedRemovePathRow.getUploadFilePath();
		int versionNo = selectedRemovePathRow.getVersionNo();
		
		String str = path;
		Pattern regex = Pattern.compile("\\.(\\S+)");
		Matcher match = regex.matcher(str);
		if (match.find())
			System.out.println(match.group(1));
		
		String filePath = path;

		File file = new File(filePath);
	
		boolean f = file.delete();
	

		int result2 = documentManagementService.DeleteUserManagementVersionDoc(empNo, transaction_Code, docDes,
				versionNo);

		if (result2 != 0) {
			

		} else {			

			historyUserManagementViewList = documentManagementService
					.ViewUserManagementHistoryList(viewUserManagementDocumentHistory);
			
			selectedRemovePathRow.setUploadFilePath(null);

		}

	}

	// USER MANAGEMENT UPLOAD DOCUMENT POP-UP

	public void saveUserManagementMandatoryUploadDocuments(FileUploadEvent event) {

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			
			CreateDirectoryUserManagement();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
			
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String createUMPath = originalPath + File.separator + "User Management";
			String newCreatedPath = createUMPath + File.separator + sessionBackingBean.getEmpNo();

			
			String documentPath = propertyFilePath;
			
			
			String tempDocumentPath = documentPath + File.separator + "User Management";
			String doc = tempDocumentPath + File.separator + sessionBackingBean.getEmpNo() + File.separator
					+ "Mandatory Documents";

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			String empNo = sessionBackingBean.getEmpNo();
			uploaddocumentManagementDTO.setUpload_empNo(sessionBackingBean.getEmpNo());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeTrans = sessionBackingBean.getEmpTransaction();
			String typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);
			
			String docType = "M";
			String doc_code = obj.getDoc_Code();

			int versionNo = documentManagementService.getVersionNumberForUserManagement(uploaddocumentManagementDTO,
					typeCodeTrans, docType);
			

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = tempDocumentPath + File.separator + sessionBackingBean.getEmpNo() + File.separator
					+ "Mandatory Documents" + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					obj.setUploadFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();

					String transaction_Code = "";

					try {
						transaction_Code = PropertyReader
								.getPropertyValue("queue.transaction.type.code.usermanagement");
					} catch (ApplicationException e) {
						
						e.printStackTrace();
					}

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getUserManagementFilePath(filePath, doc_des, empNo,
							transaction_Code);

					
					if (file != null) {

					
						int result2 = documentManagementService.replaceUserManagementDoc(doc_des, empNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveUserManagementMandatoryUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);

						if (result2 != 0 || result1 != 0) {
							
						} else {

							String typeCode = "";

							try {
								typeCode = PropertyReader
										.getPropertyValue("queue.transaction.type.code.usermanagement");
							} catch (ApplicationException e) {
								
								e.printStackTrace();
							}
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveUserManagementMandatoryUploads(filePath, doc_des,
								doc_code, empNo, transaction_Code, user);
						int result1 = documentManagementService.saveUserManagementMandatoryUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);

						if (result != 0 || result1 != 0) {
							
						} else {

							String typeCode = "";

							try {
								typeCode = PropertyReader
										.getPropertyValue("queue.transaction.type.code.usermanagement");
							} catch (ApplicationException e) {
								
								e.printStackTrace();
							}
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully Uploaded. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveUserManagementOptionalUploadDocuments(FileUploadEvent event) {

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectoryUserManagement();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
			
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String createUMPath = originalPath + File.separator + "User Management";
			String newCreatedPath = createUMPath + File.separator + sessionBackingBean.getEmpNo();

			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath + File.separator + "User Management";
			String doc = tempDocumentPath + File.separator + sessionBackingBean.getEmpNo() + File.separator
					+ "Optional Documents" + File.separator + sessionBackingBean.getEmpTransaction();

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			String empNo = sessionBackingBean.getEmpNo();
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
			uploaddocumentManagementDTO.setUpload_empNo(sessionBackingBean.getEmpNo());

			String typeTrans = sessionBackingBean.getEmpTransaction();
			String typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);
			String docType = "O";
			String doc_code = obj.getDoc_Code();

			int versionNo = documentManagementService.getVersionNumberForUserManagement(uploaddocumentManagementDTO,
					typeCodeTrans, docType);
			
			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = tempDocumentPath + File.separator + sessionBackingBean.getEmpNo() + File.separator
					+ "Optional Documents" + File.separator + str;

			File files = new File(documentPath);
		

			InputStream in = event.getFile().getInputstream();
		
			if (files.exists()) {
				if (!files.mkdirs()) {
					
					copyFile(docuString, in);

					obj.setUploadOptionalFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadOptionalFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();

					String transaction_Code = "";

					try {
						transaction_Code = PropertyReader
								.getPropertyValue("queue.transaction.type.code.usermanagement");
					} catch (ApplicationException e) {
						
						e.printStackTrace();
					}

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getUserManagementFilePath(filePath, doc_des, empNo,
							transaction_Code);
					

					if (file != null) {
						
						int result2 = documentManagementService.replaceOptionalUserManagementDoc(doc_des, empNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveUserManagementOptionalUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);
						if (result2 != 0 || result1 != 0) {
							
						} else {

							String typeCode = "";

							try {
								typeCode = PropertyReader
										.getPropertyValue("queue.transaction.type.code.usermanagement");
							} catch (ApplicationException e) {
							
								e.printStackTrace();
							}
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveUserManagementOptionalUploads(filePath, doc_des,
								doc_code, empNo, transaction_Code, user);
						int result1 = documentManagementService.saveUserManagementOptionalUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);
						if (result != 0 || result1 != 0) {
							
						} else {

							String typeCode = "";

							try {
								typeCode = PropertyReader
										.getPropertyValue("queue.transaction.type.code.usermanagement");
							} catch (ApplicationException e) {
								
								e.printStackTrace();
							}
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully Uploaded. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void CreateDirectoryUserManagement() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;

		
		String createUMPath = originalPath + File.separator + "User Management";

		File file = new File(createUMPath);
		if (!file.exists()) {
			if (file.mkdir()) {
				
			} 
		} 
		String newCreatedPath = createUMPath + File.separator + sessionBackingBean.getEmpNo();
		

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
		
			} 
		} 
		
		String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
		
			} 
		} 
		
		String optionalPath = newCreatedPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
			
			} 
		} 
	}

	public void clearActionViewUserManagement() {

		uploaddocumentManagementDTO.setUpload_empNo(null);
		mandatoryViewUserManagementList.clear();
		optionalViewUserManagementList.clear();

		historyBtn = false;

	}

	// SURVEY UPLOAD DOCUMENT POP-UP

	public void saveSurveyMandatoryUploadDocuments(FileUploadEvent event) {

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			

			CreateDirectorySurvey();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String createUMPath = originalPath + File.separator + "Survey";
			String newCreatedPath = createUMPath + File.separator + sessionBackingBean.getEmpNo();

			
			String documentPath = propertyFilePath;
			

			
			String tempDocumentPath = documentPath + File.separator + "Survey";
			String doc = tempDocumentPath + File.separator + sessionBackingBean.getEmpNo() + File.separator
					+ "Mandatory Documents";

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			String empNo = sessionBackingBean.getEmpNo();
			uploaddocumentManagementDTO.setUpload_empNo(sessionBackingBean.getEmpNo());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeTrans = sessionBackingBean.getEmpTransaction();
			String typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);
			String docType = "M";
			String doc_code = obj.getDoc_Code();

			int versionNo = documentManagementService.getVersionNumberForUserManagement(uploaddocumentManagementDTO,
					typeCodeTrans, docType);
			

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = tempDocumentPath + File.separator + sessionBackingBean.getEmpNo() + File.separator
					+ "Mandatory Documents" + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					obj.setUploadFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
			

					obj.setUploadFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();

					String transaction_Code = typeCodeTrans;

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getUserManagementFilePath(filePath, doc_des, empNo,
							transaction_Code);

			
					if (file != null) {
				
						int result2 = documentManagementService.replaceUserManagementDoc(doc_des, empNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveUserManagementMandatoryUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);

						if (result2 != 0 || result1 != 0) {
							
						} else {

							String typeCode = typeCodeTrans;
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveUserManagementMandatoryUploads(filePath, doc_des,
								doc_code, empNo, transaction_Code, user);
						int result1 = documentManagementService.saveUserManagementMandatoryUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);

						if (result != 0 || result1 != 0) {
						
						} else {

							String typeCode = typeCodeTrans;
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully Uploaded. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						sessionBackingBean.surveyMandatoryDocumentList = documentManagementService
								.surveyMandatoryList(sessionBackingBean.getEmpNo());

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveSurveyOptionalUploadDocuments(FileUploadEvent event) {

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectorySurvey();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String createUMPath = originalPath + File.separator + "Survey";
			String newCreatedPath = createUMPath + File.separator + sessionBackingBean.getEmpNo();

			
			String documentPath = propertyFilePath;
			

			
			String tempDocumentPath = documentPath + File.separator + "Survey";
			String doc = tempDocumentPath + File.separator + sessionBackingBean.getEmpNo() + File.separator
					+ "Optional Documents" + File.separator + sessionBackingBean.getEmpTransaction();

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			String empNo = sessionBackingBean.getEmpNo();
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
			uploaddocumentManagementDTO.setUpload_empNo(sessionBackingBean.getEmpNo());

			String typeTrans = sessionBackingBean.getEmpTransaction();
			String typeCodeTrans = documentManagementService.getTransactionCodeForAddDocument(typeTrans);
			String docType = "O";
			String doc_code = obj.getDoc_Code();

			int versionNo = documentManagementService.getVersionNumberForUserManagement(uploaddocumentManagementDTO,
					typeCodeTrans, docType);
			

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = tempDocumentPath + File.separator + sessionBackingBean.getEmpNo() + File.separator
					+ "Optional Documents" + File.separator + str;

			File files = new File(documentPath);
		

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					obj.setUploadOptionalFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
				

					obj.setUploadOptionalFilePath(doc);
					obj.setUploadFilePath_file(docuString);

					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();

					String transaction_Code = typeCodeTrans;

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getUserManagementFilePath(filePath, doc_des, empNo,
							transaction_Code);

					

					if (file != null) {

						

						int result2 = documentManagementService.replaceOptionalUserManagementDoc(doc_des, empNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveUserManagementOptionalUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);
						if (result2 != 0 || result1 != 0) {
							
						} else {

							String typeCode = typeCodeTrans;

							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveUserManagementOptionalUploads(filePath, doc_des,
								doc_code, empNo, transaction_Code, user);
						int result1 = documentManagementService.saveUserManagementOptionalUploadsVersions(
								transaction_Code, empNo, doc_code, doc_des, docuString, user, versionNo);
						if (result != 0 || result1 != 0) {
							
						} else {

							String typeCode = typeCodeTrans;

							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getEmployeeInfo(empNo, documentPermitInfo);
							String titleNo = documentPermitInfo.getPermitTitle();
							String title = documentManagementService.getTitleName(titleNo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + title + ". " + documentPermitInfo.getPermitName()
										+ " Your Document is successfully Uploaded. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						sessionBackingBean.surveyOptionalDocumentList = documentManagementService
								.surveyOptionalList(sessionBackingBean.getEmpNo());

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void CreateDirectorySurvey() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;

		

		String createUMPath = originalPath + File.separator + "Survey";

		File file = new File(createUMPath);
		if (!file.exists()) {
			if (file.mkdir()) {
				
			} 
		} 

		String newCreatedPath = createUMPath + File.separator + sessionBackingBean.getEmpNo();
		

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
				
			} 
		} 

		String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
				
			} 
		} 

		String optionalPath = newCreatedPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
				
			} 
		} 

	}

	// AMENDMENTS UPLOAD DOCUMENT POP-UP
	public void saveAmendmentsMandatoryUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getApplicationNoForDoc());
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getPermitRenewalPermitNo());
		uploaddocumentManagementDTO
				.setUpload_TransactionType(sessionBackingBean.getPermitRenewalTranstractionTypeDescription());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			

			CreateDirectoryAmendment();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
			String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 

			String applicationPath = docTransactionType + File.separator
					+ uploaddocumentManagementDTO.getUpload_Application();
			File file3 = new File(applicationPath);
			if (!file3.exists()) {
				if (file3.mkdir()) {
				
				} else {
				
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 

			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "M";

			int versionNo = documentManagementService.getVersionNumber(uploaddocumentManagementDTO, typeCodeTrans,
					docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = applicationPath + File.separator + str;

			File files = new File(documentPath);
			
			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					obj.setUploadFilePath_file(docuString);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadFilePath_file(docuString);

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();

					String permitNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						permitNo = (String) event.getComponent().getAttributes().get("permitNo");

					

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");

					

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePath(filePath, doc_des, permitNo, transaction_Code,
							doc_code, applicationNo);

				

					if (file != null) {

						int result2 = documentManagementService.replaceDoc(doc_des, permitNo, transaction_Code, user,
								docuString);
						int result1 = documentManagementService.saveMandatoryUploadsVersions(filePath, doc_des,
								doc_code, permitNo, applicationNo, transaction_Code, user, versionNo);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploads(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user);

						int result1 = documentManagementService.saveMandatoryUploadsVersions(filePath, doc_des,
								doc_code, permitNo, applicationNo, transaction_Code, user, versionNo);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						
						sessionBackingBean.amendmentToBusOwnerMandatoryDocumentList = documentManagementService
								.amendmentToBusOwnerMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.amendmentToBusMandatoryDocumentList = documentManagementService
								.amendmentToBusMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.amendmentToOwnerBusMandatoryDocumentList = documentManagementService
								.amendmentToOwnerBusMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.amendmentToServiceBusMandatoryDocumentList = documentManagementService
								.amendmentToServiceBusMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.amendmentToServiceMandatoryDocumentList = documentManagementService
								.amendmentToServiceMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveAmendmentsOptionalUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getApplicationNoForDoc());
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getPermitRenewalPermitNo());
		uploaddocumentManagementDTO
				.setUpload_TransactionType(sessionBackingBean.getPermitRenewalTranstractionTypeDescription());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			

			CreateDirectoryAmendment();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {

				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
			String mandatoryPath = newCreatedPath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} else {
			

			}

			String applicationPath = docTransactionType + File.separator
					+ uploaddocumentManagementDTO.getUpload_Application();
			File file3 = new File(applicationPath);
			if (!file3.exists()) {
				if (file3.mkdir()) {
				
				} else {
				
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} else {
				

			}

			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "O";

			int versionNo = documentManagementService.getVersionNumber(uploaddocumentManagementDTO, typeCodeTrans,
					docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = applicationPath + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					obj.setUploadOptionalFilePath(docuString);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					

					obj.setUploadOptionalFilePath(docuString);

					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();
					String permitNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						permitNo = (String) event.getComponent().getAttributes().get("permitNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePath(filePath, doc_des, permitNo,
							transaction_Code, applicationNo);

					String filenew = documentManagementService.getAddNewDocFilePath(filePath, doc_des, permitNo,
							transaction_Code);


					if (file != null || filenew != null) {
					
						int result2 = documentManagementService.replaceOptionalDoc(doc_des, permitNo, transaction_Code,
								user, docuString);
						int result1 = documentManagementService.saveOptionalUploadsVersions(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user, versionNo);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploads(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user);

						int result1 = documentManagementService.saveOptionalUploadsVersions(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user, versionNo);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						

						sessionBackingBean.amendmentToBusOwnerMandatoryDocumentList = documentManagementService
								.amendmentToBusOwnerMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.amendmentToBusMandatoryDocumentList = documentManagementService
								.amendmentToBusMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.amendmentToOwnerBusMandatoryDocumentList = documentManagementService
								.amendmentToOwnerBusMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.amendmentToServiceBusMandatoryDocumentList = documentManagementService
								.amendmentToServiceBusMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
						sessionBackingBean.amendmentToServiceMandatoryDocumentList = documentManagementService
								.amendmentToServiceMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void CreateDirectoryAmendment() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;


		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.applicationNoForDoc);

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getPermitRenewalPermitNo());
		uploaddocumentManagementDTO
				.setUpload_TransactionType(sessionBackingBean.getPermitRenewalTranstractionTypeDescription());

		String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
		

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
				
			} 
		} 

		String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
			
			} 
		} 

		String optionalPath = newCreatedPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
				
			}
		} 

	}

	// TENDER UPLOAD DOCUMENT POP-UP

	public void saveTenderMandatoryUploadDocuments(FileUploadEvent event) {
		
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getApplicationNoForDoc());
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getPermitRenewalPermitNo());
		uploaddocumentManagementDTO
				.setUpload_TransactionType(sessionBackingBean.getPermitRenewalTranstractionTypeDescription());
		
		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			
			CreateDirectoryTender();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String appPath = null;
			if (uploaddocumentManagementDTO.getUpload_Application() != null
					&& !uploaddocumentManagementDTO.getUpload_Application().isEmpty()
					&& !uploaddocumentManagementDTO.getUpload_Application().equalsIgnoreCase("")) {

				appPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			} else {

				appPath = newCreatedPath;
			}

			String mandatoryPath = appPath + File.separator + "Mandatory Documents";
			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "M";

			int versionNo = documentManagementService.getVersionNumber(uploaddocumentManagementDTO, typeCodeTrans,
					docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = mandatoryPath + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();

					String permitNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						permitNo = (String) event.getComponent().getAttributes().get("permitNo");

					

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePath(filePath, doc_des, permitNo, transaction_Code,
							doc_code, applicationNo);

					

					if (file != null) {
						
						int result2 = documentManagementService.replaceDoc(doc_des, permitNo, transaction_Code, user,
								docuString);
						int result1 = documentManagementService.saveMandatoryUploadsVersions(filePath, doc_des,
								doc_code, permitNo, applicationNo, transaction_Code, user, versionNo);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							
							}
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploads(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user);

						int result1 = documentManagementService.saveMandatoryUploadsVersions(filePath, doc_des,
								doc_code, permitNo, applicationNo, transaction_Code, user, versionNo);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
							
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						sessionBackingBean.tenderMandatoryDocumentList = documentManagementService
								.tenderMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveTenderOptionalUploadDocuments(FileUploadEvent event) {
		
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getApplicationNoForDoc());
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getPermitRenewalPermitNo());
		uploaddocumentManagementDTO
				.setUpload_TransactionType(sessionBackingBean.getPermitRenewalTranstractionTypeDescription());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectoryTender();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
			
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String appPath = null;
			if (uploaddocumentManagementDTO.getUpload_Application() != null
					&& !uploaddocumentManagementDTO.getUpload_Application().isEmpty()
					&& !uploaddocumentManagementDTO.getUpload_Application().equalsIgnoreCase("")) {

				appPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			} else {

				appPath = newCreatedPath;
			}

			String mandatoryPath = appPath + File.separator + "Optional Documents";
			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
	
			String docType = "O";

			int versionNo = documentManagementService.getVersionNumber(uploaddocumentManagementDTO, typeCodeTrans,
					docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = mandatoryPath + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					

					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();
					String permitNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						permitNo = (String) event.getComponent().getAttributes().get("permitNo");

					

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");



					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePath(filePath, doc_des, permitNo,
							transaction_Code, applicationNo);

					String filenew = documentManagementService.getAddNewDocFilePath(filePath, doc_des, permitNo,
							transaction_Code);


					if (file != null || filenew != null) {
						

						int result2 = documentManagementService.replaceOptionalDoc(doc_des, permitNo, transaction_Code,
								user, docuString);
						int result1 = documentManagementService.saveOptionalUploadsVersions(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user, versionNo);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							}
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploads(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user);

						int result1 = documentManagementService.saveOptionalUploadsVersions(filePath, doc_des, doc_code,
								permitNo, applicationNo, transaction_Code, user, versionNo);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						
						sessionBackingBean.tenderOptionalDocumentList = documentManagementService
								.tenderOptionalList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void CreateDirectoryTender() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;

		
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.applicationNoForDoc);
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.permitRenewalPermitNo);

		String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
		

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
				
			} 
		} 
		
		String applicationPath = null;
		if (uploaddocumentManagementDTO.getUpload_Application() != null
				&& !uploaddocumentManagementDTO.getUpload_Application().isEmpty()
				&& !uploaddocumentManagementDTO.getUpload_Application().equalsIgnoreCase("")) {

			applicationPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();
			

			File fileapp = new File(applicationPath);
			if (!fileapp.exists()) {
				if (fileapp.mkdir()) {
				
				}
			} 
			} else {

			applicationPath = newCreatedPath;
		}

		String mandatoryPath = applicationPath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
				
			} 
		} 

		String optionalPath = applicationPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
				
			} 
		} 

	}
	
	// SUBSIDY - START

	// SISU SARIYA DOCUMENT POP-UP
	public void saveSisuSariyaMandatoryUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			

			CreateDirectorySisuSariya();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String appPath = null;

			appPath = newCreatedPath;

			String mandatoryPath = appPath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 
			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			String docType = "M";

			int versionNo = documentManagementService.getVersionNumberForSisuSariya(uploaddocumentManagementDTO,
					typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
		
			InputStream in = event.getFile().getInputstream();
		
			if (files.exists()) {
				if (!files.mkdirs()) {
				
					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();

					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePathForSisuSariya(filePath, doc_des, requestNo,
							transaction_Code, doc_code, applicationNo);

					
					if (file != null) {

						int result2 = documentManagementService.replaceDocForSisuSariya(doc_des, requestNo,
								transaction_Code, user, docuString,expiryDate);
						int result1 = documentManagementService.saveMandatoryUploadsVersionsForSisuSariya(filePath,
								doc_des, doc_code, requestNo, applicationNo, transaction_Code, user, versionNo,expiryDate);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							}

							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploadsForSisuSariya(filePath, doc_des,
								doc_code, requestNo, applicationNo, transaction_Code, user,expiryDate);

						int result1 = documentManagementService.saveMandatoryUploadsVersionsForSisuSariya(filePath,
								doc_des, doc_code, requestNo, applicationNo, transaction_Code, user, versionNo,expiryDate);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");


						sessionBackingBean.tenderMandatoryDocumentList = documentManagementService
								.tenderMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void saveSisuSariyaOptionalUploadDocuments(FileUploadEvent event) {
		
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectorySisuSariya();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String appPath = null;

			appPath = newCreatedPath;

			String mandatoryPath = appPath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			}


			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "O";

			int versionNo = documentManagementService.getVersionNumberForSisuSariya(uploaddocumentManagementDTO,
					typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					

					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();
					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePathForSisuSariya(filePath, doc_des,
							requestNo, transaction_Code, doc_code, applicationNo);

					
					if (file != null) {
						
						int result2 = documentManagementService.replaceOptionalDocForSisuSariya(doc_des, requestNo,
								transaction_Code, user, docuString,expiryDate);
						int result1 = documentManagementService.saveOptionalUploadsVersionsForSisuSariya(filePath,
								doc_des, doc_code, requestNo, applicationNo, transaction_Code, user, versionNo,expiryDate);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							}

							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploadsForSisuSariya(filePath, doc_des,
								doc_code, requestNo, applicationNo, transaction_Code, user,expiryDate);

						int result1 = documentManagementService.saveOptionalUploadsVersionsForSisuSariya(filePath,
								doc_des, doc_code, requestNo, applicationNo, transaction_Code, user, versionNo,expiryDate);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							}
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						
						sessionBackingBean.tenderOptionalDocumentList = documentManagementService
								.tenderOptionalList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
				
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void CreateDirectorySisuSariya() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;

				
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());

		String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
		

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
				
			}
		} 

		String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
				
			} 
		}

		String optionalPath = newCreatedPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
				
			} 
		} 

	}

	// SISU SARIYA PERMIT HOLDER DOCUMENT POP-UP
	public void saveSisuSariyaPermitHolderMandatoryUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getRefNoSisuSariya());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			
			CreateDirectorySisuSariyaPermitHolder();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			String mandatoryPath = refPath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 
			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "M";

			int versionNo = documentManagementService
					.getVersionNumberForSisuSariyaPermitHolder(uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			
			InputStream in = event.getFile().getInputstream();
		
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();

					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String refNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

					

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						refNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePathForSisuSariyaPermitHolder(filePath, doc_des,
							requestNo, transaction_Code, doc_code, refNo);

					

					if (file != null) {
				
						int result2 = documentManagementService.replaceDocForSisuSariyaPermitHolder(doc_des, requestNo,
								transaction_Code, user, docuString, refNo,expiryDate);
						int result1 = documentManagementService.saveMandatoryUploadsVersionsForSisuSariyaPermitHolder(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, versionNo,expiryDate);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								}
							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploadsForSisuSariyaPermitHolder(filePath,
								doc_des, doc_code, requestNo, refNo, transaction_Code, user,expiryDate);

						int result1 = documentManagementService.saveMandatoryUploadsVersionsForSisuSariyaPermitHolder(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, versionNo,expiryDate);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void saveSisuSariyaPermitHolderOptionalUploadDocuments(FileUploadEvent event) {
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getRefNoSisuSariya());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectorySisuSariyaPermitHolder();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			String mandatoryPath = refPath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
				
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 
			String documentPath = propertyFilePath;
			

			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
		
			String docType = "O";

			int versionNo = documentManagementService
					.getVersionNumberForSisuSariyaPermitHolder(uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					
					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					

					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();
					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String refNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						refNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePathForSisuSariyaPermitHolder(filePath,
							doc_des, requestNo, transaction_Code, doc_code, refNo);

					
					if (file != null) {
						
						int result2 = documentManagementService.replaceOptionalDocForSisuSariyaPermitHolder(doc_des,
								requestNo, transaction_Code, user, docuString, refNo,expiryDate);
						int result1 = documentManagementService.saveOptionalUploadsVersionsForSisuSariyaPermitHolder(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, versionNo,expiryDate);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploadsForSisuSariyaPermitHolder(filePath,
								doc_des, doc_code, requestNo, refNo, transaction_Code, user,expiryDate);

						int result1 = documentManagementService.saveOptionalUploadsVersionsForSisuSariyaPermitHolder(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, versionNo,expiryDate);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void CreateDirectorySisuSariyaPermitHolder() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;

		
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getRefNoSisuSariya());

		String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
		

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
				
			} 
		}

		String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();
		File file4 = new File(refPath);
		if (!file4.exists()) {
			if (file4.mkdir()) {
				
			} 
		}

		String mandatoryPath = refPath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
				
			} 
		} 

		String optionalPath = refPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
				
			} 
		} 
	}

	// SISU SARIYA AGREEMENT RENEWALS DOCUMENT POP-UP
	public void saveSisuSariyaAgreementRenewalsMandatoryUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getServiceRefNo());
		uploaddocumentManagementDTO.setServiceNo(sessionBackingBean.getServiceNoForSisuSariya());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			
			CreateDirectorySisuSariyaAgreementRenewals();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			String servicePath = refPath + File.separator + uploaddocumentManagementDTO.getServiceNo();

			String mandatoryPath = servicePath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 

			String documentPath = propertyFilePath;
			
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "M";

			int versionNo = documentManagementService.getVersionNumberForSisuSariyaAgreementRenewals(
					uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					
					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();

					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String refNo = uploaddocumentManagementDTO.getUpload_Application();
					String serviceNo = uploaddocumentManagementDTO.getServiceNo();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						refNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePathForSisuSariyaAgreementRenewals(filePath, doc_des,
							requestNo, transaction_Code, doc_code, refNo, serviceNo);


					if (file != null) {

						int result2 = documentManagementService.replaceDocForSisuSariyaAgreementRenwals(doc_des,
								requestNo, transaction_Code, user, docuString, refNo, serviceNo);
						int result1 = documentManagementService
								.saveMandatoryUploadsVersionsForSisuSariyaAgreementRenewals(filePath, doc_des, doc_code,
										requestNo, refNo, transaction_Code, user, versionNo, serviceNo);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							}
							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploadsForSisuSariyaAgreementRenewals(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, serviceNo);

						int result1 = documentManagementService
								.saveMandatoryUploadsVersionsForSisuSariyaAgreementRenewals(filePath, doc_des, doc_code,
										requestNo, refNo, transaction_Code, user, versionNo, serviceNo);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void saveSisuSariyaAgreementRenewalsOptionalUploadDocuments(FileUploadEvent event) {

		

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getServiceRefNo());
		uploaddocumentManagementDTO.setServiceNo(sessionBackingBean.getServiceNoForSisuSariya());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectorySisuSariyaAgreementRenewals();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			String servicePath = refPath + File.separator + uploaddocumentManagementDTO.getServiceNo();

			String mandatoryPath = servicePath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 
			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
		
			String docType = "O";

			int versionNo = documentManagementService.getVersionNumberForSisuSariyaAgreementRenewals(
					uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			
			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					
					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();
					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String refNo = uploaddocumentManagementDTO.getUpload_Application();
					String serviceNo = uploaddocumentManagementDTO.getServiceNo();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						refNo = (String) event.getComponent().getAttributes().get("applicationNo");

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePathForSisuSariyaAgreementRenewals(filePath,
							doc_des, requestNo, transaction_Code, doc_code, refNo, serviceNo);

					if (file != null) {
						
						int result2 = documentManagementService.replaceOptionalDocForSisuSariyaAgreementRenewals(
								doc_des, requestNo, transaction_Code, user, docuString, refNo, serviceNo);
						int result1 = documentManagementService
								.saveOptionalUploadsVersionsForSisuSariyaAgreementRenewals(filePath, doc_des, doc_code,
										requestNo, refNo, transaction_Code, user, versionNo, serviceNo);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 
							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {
						int result = documentManagementService.saveOptionalUploadsForSisuSariyaAgreementRenewals(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, serviceNo);

						int result1 = documentManagementService
								.saveOptionalUploadsVersionsForSisuSariyaAgreementRenewals(filePath, doc_des, doc_code,
										requestNo, refNo, transaction_Code, user, versionNo, serviceNo);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);
								
							} 

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void CreateDirectorySisuSariyaAgreementRenewals() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getServiceRefNo());
		uploaddocumentManagementDTO.setServiceNo(sessionBackingBean.getServiceNoForSisuSariya());

		String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
		

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
				
			} 
		} 

		String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();
		File file4 = new File(refPath);
		if (!file4.exists()) {
			if (file4.mkdir()) {
				
			} 
		}

		String servicePath = refPath + File.separator + uploaddocumentManagementDTO.getServiceNo();

		File file5 = new File(servicePath);
		if (!file5.exists()) {
			if (file5.mkdir()) {
				
			} 
		} 

		String mandatoryPath = servicePath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
				
			} 
		} 

		String optionalPath = servicePath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
			
			} 
		} 

	}

	// GAMI SARIYA REQUEST
	public void saveGamiSariyaMandatoryUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

		
			CreateDirectorySisuSariya();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String appPath = null;

			appPath = newCreatedPath;

			String mandatoryPath = appPath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			}

			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "M";

			int versionNo = documentManagementService.getVersionNumberForSisuSariya(uploaddocumentManagementDTO,
					typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			
			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {
					
					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();

					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");


					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");


					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePathForSisuSariya(filePath, doc_des, requestNo,
							transaction_Code, doc_code, applicationNo);

					if (file != null) {

						int result2 = documentManagementService.replaceDocForSisuSariya(doc_des, requestNo,
								transaction_Code, user, docuString,null);
						int result1 = documentManagementService.saveMandatoryUploadsVersionsForSisuSariya(filePath,
								doc_des, doc_code, requestNo, applicationNo, transaction_Code, user, versionNo,null);
						if (result2 != 0 || result1 != 0) {
						
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
						

							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							
							} 
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploadsForSisuSariya(filePath, doc_des,
								doc_code, requestNo, applicationNo, transaction_Code, user,null);

						int result1 = documentManagementService.saveMandatoryUploadsVersionsForSisuSariya(filePath,
								doc_des, doc_code, requestNo, applicationNo, transaction_Code, user, versionNo,null);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							}
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");
				

						sessionBackingBean.tenderMandatoryDocumentList = documentManagementService
								.tenderMandatoryList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void saveGamiSariyaOptionalUploadDocuments(FileUploadEvent event) {
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectorySisuSariya();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String appPath = null;

			appPath = newCreatedPath;

			String mandatoryPath = appPath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 

			String documentPath = propertyFilePath;
		
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "O";

			int versionNo = documentManagementService.getVersionNumberForSisuSariya(uploaddocumentManagementDTO,
					typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			
			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					
					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();
					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");


					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");


					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePathForSisuSariya(filePath, doc_des,
							requestNo, transaction_Code, doc_code, applicationNo);
				

					if (file != null) {
						int result2 = documentManagementService.replaceOptionalDocForSisuSariya(doc_des, requestNo,
								transaction_Code, user, docuString,null);
						int result1 = documentManagementService.saveOptionalUploadsVersionsForSisuSariya(filePath,
								doc_des, doc_code, requestNo, applicationNo, transaction_Code, user, versionNo,null);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							}
							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploadsForSisuSariya(filePath, doc_des,
								doc_code, requestNo, applicationNo, transaction_Code, user,null);

						int result1 = documentManagementService.saveOptionalUploadsVersionsForSisuSariya(filePath,
								doc_des, doc_code, requestNo, applicationNo, transaction_Code, user, versionNo,null);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(applicationNo,
									documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
									alertMessage);
								
							} 
							
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");
						
						sessionBackingBean.tenderOptionalDocumentList = documentManagementService
								.tenderOptionalList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// OTHERS
	// OTHERS

	// GAMI SARIYA SURVEY REQUEST	
	public void saveGamiSariyaInitiateSurveyRequestMandatoryUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getRefNoSisuSariya());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			CreateDirectorySisuSariyaPermitHolder();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			String mandatoryPath = refPath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 
			
			String documentPath = propertyFilePath;
			
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "M";

			int versionNo = documentManagementService
					.getVersionNumberForSisuSariyaPermitHolder(uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {
					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();

					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String refNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						refNo = (String) event.getComponent().getAttributes().get("applicationNo");
						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");
						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePathForSisuSariyaPermitHolder(filePath, doc_des,
							requestNo, transaction_Code, doc_code, refNo);

					
					if (file != null) {
						
						int result2 = documentManagementService.replaceDocForSisuSariyaPermitHolder(doc_des, requestNo,
								transaction_Code, user, docuString, refNo,expiryDate);
						int result1 = documentManagementService.saveMandatoryUploadsVersionsForSisuSariyaPermitHolder(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, versionNo,expiryDate);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

								
							} 

							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploadsForSisuSariyaPermitHolder(filePath,
								doc_des, doc_code, requestNo, refNo, transaction_Code, user,expiryDate);

						int result1 = documentManagementService.saveMandatoryUploadsVersionsForSisuSariyaPermitHolder(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, versionNo,expiryDate);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							}
							
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void saveGamiSariyaInitiateSurveyRequestOptionalUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getRefNoSisuSariya());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectorySisuSariyaPermitHolder();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			String mandatoryPath = refPath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					 
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 
			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "O";

			int versionNo = documentManagementService
					.getVersionNumberForSisuSariyaPermitHolder(uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					
					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();
					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String refNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

						
					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						refNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePathForSisuSariyaPermitHolder(filePath,
							doc_des, requestNo, transaction_Code, doc_code, refNo);

					
					if (file != null) {
						int result2 = documentManagementService.replaceOptionalDocForSisuSariyaPermitHolder(doc_des,
								requestNo, transaction_Code, user, docuString, refNo,null);
						int result1 = documentManagementService.saveOptionalUploadsVersionsForSisuSariyaPermitHolder(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, versionNo,null);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 
							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploadsForSisuSariyaPermitHolder(filePath,
								doc_des, doc_code, requestNo, refNo, transaction_Code, user,null);

						int result1 = documentManagementService.saveOptionalUploadsVersionsForSisuSariyaPermitHolder(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, versionNo,null);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);
								
							} 
							
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// GAMI SARIYA SURVEY
	public void saveGamiSariyaSurveyMandatoryUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getServiceRefNo());
		uploaddocumentManagementDTO.setServiceNo(sessionBackingBean.getServiceNoForSisuSariya());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			CreateDirectorySisuSariyaAgreementRenewals();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			String servicePath = refPath + File.separator + uploaddocumentManagementDTO.getServiceNo();

			String mandatoryPath = servicePath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 

			String documentPath = propertyFilePath;

			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
		
			String docType = "M";

			int versionNo = documentManagementService.getVersionNumberForSisuSariyaAgreementRenewals(
					uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			
			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {
					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					
					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();

					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String refNo = uploaddocumentManagementDTO.getUpload_Application();
					String serviceNo = uploaddocumentManagementDTO.getServiceNo();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						refNo = (String) event.getComponent().getAttributes().get("applicationNo");

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePathForSisuSariyaAgreementRenewals(filePath, doc_des,
							requestNo, transaction_Code, doc_code, refNo, serviceNo);

					
					if (file != null) {
						
						int result2 = documentManagementService.replaceDocForSisuSariyaAgreementRenwals(doc_des,
								requestNo, transaction_Code, user, docuString, refNo, serviceNo);
						int result1 = documentManagementService
								.saveMandatoryUploadsVersionsForSisuSariyaAgreementRenewals(filePath, doc_des, doc_code,
										requestNo, refNo, transaction_Code, user, versionNo, serviceNo);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);
								
							} 
							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploadsForSisuSariyaAgreementRenewals(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, serviceNo);

						int result1 = documentManagementService
								.saveMandatoryUploadsVersionsForSisuSariyaAgreementRenewals(filePath, doc_des, doc_code,
										requestNo, refNo, transaction_Code, user, versionNo, serviceNo);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 
							
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

					}

				} else {

					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void saveGamiSariyaSurveyOptionalUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getRequestNoForSisuSariya());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getServiceRefNo());
		uploaddocumentManagementDTO.setServiceNo(sessionBackingBean.getServiceNoForSisuSariya());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectorySisuSariyaAgreementRenewals();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			String servicePath = refPath + File.separator + uploaddocumentManagementDTO.getServiceNo();

			String mandatoryPath = servicePath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			}
			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "O";

			int versionNo = documentManagementService.getVersionNumberForSisuSariyaAgreementRenewals(
					uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			
			InputStream in = event.getFile().getInputstream();
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					
					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					Date expiryDate = obj.getExpiryDate();
					String doc_code = obj.getDoc_Code();
					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String refNo = uploaddocumentManagementDTO.getUpload_Application();
					String serviceNo = uploaddocumentManagementDTO.getServiceNo();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						refNo = (String) event.getComponent().getAttributes().get("applicationNo");

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");
				
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePathForSisuSariyaAgreementRenewals(filePath,
							doc_des, requestNo, transaction_Code, doc_code, refNo, serviceNo);
					
					if (file != null) {
						
						int result2 = documentManagementService.replaceOptionalDocForSisuSariyaAgreementRenewals(
								doc_des, requestNo, transaction_Code, user, docuString, refNo, serviceNo);
						int result1 = documentManagementService
								.saveOptionalUploadsVersionsForSisuSariyaAgreementRenewals(filePath, doc_des, doc_code,
										requestNo, refNo, transaction_Code, user, versionNo, serviceNo);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully replaced. This is the File Path -"
										+ docuString);
								
								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);

							} 
							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploadsForSisuSariyaAgreementRenewals(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, serviceNo);

						int result1 = documentManagementService
								.saveOptionalUploadsVersionsForSisuSariyaAgreementRenewals(filePath, doc_des, doc_code,
										requestNo, refNo, transaction_Code, user, versionNo, serviceNo);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							permitInfo = documentManagementService.getPermitHolderInfo(refNo, documentPermitInfo);
							
							if (response == true && documentPermitInfo.getPermitMobileNum() != null
									&& !documentPermitInfo.getPermitMobileNum().isEmpty()) {

								setAlertMessage("Dear " + documentPermitInfo.getPermitTitle() + ". "
										+ documentPermitInfo.getPermitName()
										+ " Your Document is successfully uploaded. This is the File Path -"
										+ docuString);
								

								int permitInfo = documentManagementService.saveAlertMessage(documentPermitInfo,
										alertMessage);								
							} 
							
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// SUBSIDY - END	
	public void copyFile(String documentPath, InputStream in) {
		OutputStream out = null;

		try {
			out = new FileOutputStream(new File(documentPath));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			try {
				out.flush();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	public void addNewDocumentOtherPages() {

		RequestContext.getCurrentInstance().execute("PF('viewAddDoc').show()");
	}

	
	// DRIVER CONDUCTOR
	public void saveDriverConductorMandatoryUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getDriverConductorId());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getDcAppNo());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");
			CreateDirectoryDriverConductor();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			String mandatoryPath = refPath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 
			
			String documentPath = propertyFilePath;
			
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "M";

			int versionNo = documentManagementService
					.getVersionNumberForDriverConductor(uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			
			InputStream in = event.getFile().getInputstream();
						
			if (files.exists()) {
				if (!files.mkdirs()) {
					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());
					
					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();

					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String refNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");
					

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						refNo = (String) event.getComponent().getAttributes().get("applicationNo");
						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");
						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePathForDriverConductor(filePath, doc_des,
							requestNo, transaction_Code, doc_code, refNo);

					
					if (file != null) {

						int result2 = documentManagementService.replaceDocForDriverConductor(doc_des, requestNo,
								transaction_Code, user, docuString, refNo);
						int result1 = documentManagementService.saveMandatoryUploadsVersionsForDriverConductor(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, versionNo);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploadsForDriverConductor(filePath,
								doc_des, doc_code, requestNo, refNo, transaction_Code, user);

						int result1 = documentManagementService.saveMandatoryUploadsVersionsForDriverConductor(
								filePath, doc_des, doc_code, requestNo, refNo, transaction_Code, user, versionNo);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");


					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void saveDriverConductorOptionalUploadDocuments(FileUploadEvent event) {
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getDriverConductorId());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getDcAppNo());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectoryDriverConductor();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
			
			String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();

			String appPath = null;

			appPath = refPath;

			String mandatoryPath = appPath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 
			
			String documentPath = propertyFilePath;

			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "O";

			int versionNo = documentManagementService.getVersionNumberForDriverConductor(uploaddocumentManagementDTO,
					typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					

					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();
					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String applicationNo = uploaddocumentManagementDTO.getUpload_Application();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");
				

					}
					if (uploaddocumentManagementDTO.getUpload_Application() == null) {
						applicationNo = (String) event.getComponent().getAttributes().get("applicationNo");

						

					}
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePathForDriverConductor(filePath, doc_des,
							requestNo, transaction_Code, doc_code, applicationNo);

					
					if (file != null) {
						
						int result2 = documentManagementService.replaceOptionalDocForDriverConductor(doc_des, requestNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveOptionalUploadsVersionsForDriverConductor(filePath,
								doc_des, doc_code, requestNo, applicationNo, transaction_Code, user, versionNo);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploadsForDriverConductor(filePath, doc_des,
								doc_code, requestNo, applicationNo, transaction_Code, user);

						int result1 = documentManagementService.saveOptionalUploadsVersionsForDriverConductor(filePath,
								doc_des, doc_code, requestNo, applicationNo, transaction_Code, user, versionNo);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");


						sessionBackingBean.tenderOptionalDocumentList = documentManagementService
								.tenderOptionalList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void CreateDirectoryDriverConductor() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;
			
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getDriverConductorId());
		uploaddocumentManagementDTO.setUpload_Application(sessionBackingBean.getDcAppNo());

		String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
		
		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
				
			} 
		}
		
		String refPath = newCreatedPath + File.separator + uploaddocumentManagementDTO.getUpload_Application();
		File file4 = new File(refPath);
		if (!file4.exists()) {
			if (file4.mkdir()) {
				
			} 
		}
		
		String mandatoryPath = refPath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
				
			} 
		} 
		
		String optionalPath = refPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
			
			} 
		} 
	}
	
	// GRIVANCE MANAGEMENT	
	public void saveGrievanceMandatoryUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getComplainNo());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			CreateDirectoryGrivance();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			
			String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			}
			
			String documentPath = propertyFilePath;
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "M";

			int versionNo = documentManagementService
					.getVersionNumberForGrievance(uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			
			InputStream in = event.getFile().getInputstream();
						
			if (files.exists()) {
				if (!files.mkdirs()) {
					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();

					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

						
					}

					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePathForGrievance(filePath, doc_des,
							requestNo, transaction_Code, doc_code);

					

					if (file != null) {

						int result2 = documentManagementService.replaceDocForGrievance(doc_des, requestNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveMandatoryUploadsVersionsForGrievance(
								filePath, doc_des, doc_code, requestNo, transaction_Code, user, versionNo);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
						
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploadsForGrievance(filePath,
								doc_des, doc_code, requestNo, transaction_Code, user);

						int result1 = documentManagementService.saveMandatoryUploadsVersionsForGrievance(
								filePath, doc_des, doc_code, requestNo, transaction_Code, user, versionNo);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
						

						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");


					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void saveGrievanceOptionalUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getComplainNo());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());
		
		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectoryGrivance();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
						
			String appPath = null;

			appPath = newCreatedPath;

			String mandatoryPath = appPath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 
			
			String documentPath = propertyFilePath;

			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
		
			String docType = "O";

			int versionNo = documentManagementService.getVersionNumberForGrievance(uploaddocumentManagementDTO,
					typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					
					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();
					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

					}
					
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");

						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePathForGrievance(filePath, doc_des,
							requestNo, transaction_Code, doc_code);

					
					if (file != null) {
						int result2 = documentManagementService.replaceOptionalDocForGrievance(doc_des, requestNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveOptionalUploadsVersionsForGrievance(filePath,
								doc_des, doc_code, requestNo, transaction_Code, user, versionNo);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploadsForGrievance(filePath, doc_des,
								doc_code, requestNo, transaction_Code, user);

						int result1 = documentManagementService.saveOptionalUploadsVersionsForGrievance(filePath,
								doc_des, doc_code, requestNo, transaction_Code, user, versionNo);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
				
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");


						sessionBackingBean.tenderOptionalDocumentList = documentManagementService
								.tenderOptionalList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void CreateDirectoryGrivance() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;
			
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getComplainNo());
		
		String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
		
		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
				
			} 
		} 
		
		String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
				
			}
		} 
		
		String optionalPath = newCreatedPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
				
			}
		}
	}
	
	
	//SIM REGISTRATION
	public void saveSIMMandatoryUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getSimRegNo());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes().get("uploadedObj");

			CreateDirectorySIM();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();


			String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			} 
			
			String documentPath = propertyFilePath;
			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setExpiryDate(obj.getExpiryDate());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "M";

			int versionNo = documentManagementService
					.getVersionNumberForSim(uploaddocumentManagementDTO, typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);
			
			InputStream in = event.getFile().getInputstream();
						
			if (files.exists()) {
				if (!files.mkdirs()) {
					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadFilePath(docuString);
					uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
					uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

					obj.setUploadFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();

					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

					}
					
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");
						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getFilePathForSIM(filePath, doc_des,
							requestNo, transaction_Code, doc_code);

					
					if (file != null) {
						
						int result2 = documentManagementService.replaceDocForSIM(doc_des, requestNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveMandatoryUploadsVersionsForSIM(
								filePath, doc_des, doc_code, requestNo, transaction_Code, user, versionNo);
						if (result2 != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveMandatoryUploadsForSIM(filePath,
								doc_des, doc_code, requestNo, transaction_Code, user);

						int result1 = documentManagementService.saveMandatoryUploadsVersionsForSIM(
								filePath, doc_des, doc_code, requestNo, transaction_Code, user, versionNo);

						if (result != 0 || result1 != 0) {
							
						} else {

							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();

							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
						
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void saveSIMOptionalUploadDocuments(FileUploadEvent event) {

		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getComplainNo());
		uploaddocumentManagementDTO.setUpload_TransactionType(sessionBackingBean.getTransactionType());

		try {
			DocumentManagementDTO obj = (DocumentManagementDTO) event.getComponent().getAttributes()
					.get("uploadedOptionalObj");

			CreateDirectorySIM();

			String propertyFilePath = null;
			try {
				propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
			} catch (ApplicationException e) {
				e.printStackTrace();
			}

			String originalPath = propertyFilePath;
			String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();

			String appPath = null;

			appPath = newCreatedPath;

			String mandatoryPath = appPath + File.separator + "Optional Documents";
			String docTransactionType = mandatoryPath + File.separator
					+ uploaddocumentManagementDTO.getUpload_TransactionType();

			File file2 = new File(docTransactionType);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					
				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");
				}
			}
			
			String documentPath = propertyFilePath;

			String tempDocumentPath = documentPath;

			uploaddocumentManagementDTO.setAdd_Doc_Description(obj.getAdd_Doc_Description());
			uploaddocumentManagementDTO.setDoc_Code(obj.getDoc_Code());

			String typeCodeTrans = uploaddocumentManagementDTO.getUpload_TransactionType();
			
			String docType = "O";

			int versionNo = documentManagementService.getVersionNumberForSim(uploaddocumentManagementDTO,
					typeCodeTrans, docType);

			String str = event.getFile().getFileName();

			int i = str.length();
			String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
			i = i - 4;
			str = str.substring(0, i);
			str = obj.getAdd_Doc_Description() + "_" + versionNo + ext;

			String docuString = docTransactionType + File.separator + str;

			File files = new File(documentPath);

			InputStream in = event.getFile().getInputstream();
			
			if (files.exists()) {
				if (!files.mkdirs()) {

					copyFile(docuString, in);

					uploaddocumentManagementDTO.setUploadOptionalFilePath(docuString);
					
					obj.setUploadOptionalFilePath(docuString);
					obj.setUploadFilePath_file(docuString);

					String filePath = obj.getUploadOptionalFilePath();
					String doc_des = obj.getAdd_Doc_Description();
					String doc_code = obj.getDoc_Code();
					String requestNo = uploaddocumentManagementDTO.getUpload_Permit();
					String transaction = uploaddocumentManagementDTO.getUpload_TransactionType();

					if (uploaddocumentManagementDTO.getUpload_Permit() == null) {
						requestNo = (String) event.getComponent().getAttributes().get("permitNo");

					}
					
					if (uploaddocumentManagementDTO.getUpload_TransactionType() == null) {
						transaction = (String) event.getComponent().getAttributes().get("transactionCode");
						
					}

					String transaction_Code = documentManagementService.getTransactionCode(transaction);

					String user = sessionBackingBean.loginUser;

					String file = documentManagementService.getOptionalFilePathForSIM(filePath, doc_des,
							requestNo, transaction_Code, doc_code);
					

					if (file != null) {

						int result2 = documentManagementService.replaceOptionalDocForSIM(doc_des, requestNo,
								transaction_Code, user, docuString);
						int result1 = documentManagementService.saveOptionalUploadsVersionsForSIM(filePath,
								doc_des, doc_code, requestNo, transaction_Code, user, versionNo);
						if (result2 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);

							
							setSuccessMsg("Successfully Uploaded.");
							RequestContext.getCurrentInstance().update("successSveUploadDoc");
							RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");

						}

					} else {

						int result = documentManagementService.saveOptionalUploadsForSIM(filePath, doc_des,
								doc_code, requestNo, transaction_Code, user);

						int result1 = documentManagementService.saveOptionalUploadsVersionsForSIM(filePath,
								doc_des, doc_code, requestNo, transaction_Code, user, versionNo);
						if (result != 0 || result1 != 0) {
							
						} else {
							String type = uploaddocumentManagementDTO.getUpload_TransactionType();

							String typeCode = documentManagementService.getTransactionCodeForAddDocument(type);
							String doc_Code = obj.getDoc_Code();
							boolean doc_Mandatory = obj.isDoc_Mandatory();
							boolean response = documentManagementService.getResponse(doc_Code, typeCode, doc_Mandatory);
							
						}

						setSuccessMsg("Successfully Uploaded.");
						RequestContext.getCurrentInstance().update("successSveUploadDoc");
						RequestContext.getCurrentInstance().execute("PF('successSveUploadDoc').show()");


						sessionBackingBean.tenderOptionalDocumentList = documentManagementService
								.tenderOptionalList(uploaddocumentManagementDTO.getUpload_Permit());
					}

				} else {
					
					setErrorMsg("Upload Failed");
					RequestContext.getCurrentInstance().update("frmrequiredFieldUploadDoc");
					RequestContext.getCurrentInstance().execute("PF('requiredFieldUploadDoc').show()");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void CreateDirectorySIM() {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {
			
			e.printStackTrace();
		}
		String originalPath = propertyFilePath;
		uploaddocumentManagementDTO.setUpload_Permit(sessionBackingBean.getSimRegNo());

		String newCreatedPath = originalPath + File.separator + uploaddocumentManagementDTO.getUpload_Permit();
		
		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {
				
			} 
		} 
		
		String mandatoryPath = newCreatedPath + File.separator + "Mandatory Documents";
		
		File file2 = new File(mandatoryPath);
		if (!file2.exists()) {
			if (file2.mkdir()) {
				
			} 
		} 
		
		String optionalPath = newCreatedPath + File.separator + "Optional Documents";
		File file3 = new File(optionalPath);
		if (!file3.exists()) {
			if (file3.mkdir()) {
				
			} 
		} 
	}
	
	
		
	// GETTERS AND SETTERS

	public DocumentManagementDTO getSelectedEditRow() {
		return selectedEditRow;
	}

	public void setSelectedEditRow(DocumentManagementDTO selectedEditRow) {
		this.selectedEditRow = selectedEditRow;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public List<String> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<String> transactionList) {
		this.transactionList = transactionList;
	}

	public List<String> getDocCodeList() {
		return docCodeList;
	}

	public void setDocCodeList(List<String> docCodeList) {
		this.docCodeList = docCodeList;
	}

	public DocumentManagementDTO getDocumentManagement() {
		return documentManagement;
	}

	public void setDocumentManagement(DocumentManagementDTO documentManagement) {
		this.documentManagement = documentManagement;
	}

	public List<DocumentManagementDTO> getDocDataList() {
		return docDataList;
	}

	public void setDocDataList(List<DocumentManagementDTO> docDataList) {
		this.docDataList = docDataList;
	}

	public DocumentManagementDTO getDocumentManagementEdit() {
		return documentManagementEdit;
	}

	public void setDocumentManagementEdit(DocumentManagementDTO documentManagementEdit) {
		this.documentManagementEdit = documentManagementEdit;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public DocumentManagementDTO getDocumentManagementDelete() {
		return documentManagementDelete;
	}

	public void setDocumentManagementDelete(DocumentManagementDTO documentManagementDelete) {
		this.documentManagementDelete = documentManagementDelete;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getDoc_Code() {
		return doc_Code;
	}

	public void setDoc_Code(String doc_Code) {
		this.doc_Code = doc_Code;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public DocumentManagementDTO getDocumentManagementDTO() {
		return documentManagementDTO;
	}

	public void setDocumentManagementDTO(DocumentManagementDTO documentManagementDTO) {
		this.documentManagementDTO = documentManagementDTO;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<String> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<String> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public String getUploadTransactionType() {
		return uploadTransactionType;
	}

	public void setUploadTransactionType(String uploadTransactionType) {
		this.uploadTransactionType = uploadTransactionType;
	}

	public String getUploadPermit() {
		return uploadPermit;
	}

	public void setUploadPermit(String uploadPermit) {
		this.uploadPermit = uploadPermit;
	}

	public String getUploadApplication() {
		return uploadApplication;
	}

	public void setUploadApplication(String uploadApplication) {
		this.uploadApplication = uploadApplication;
	}

	public DocumentManagementDTO getUploaddocumentManagementDTO() {
		return uploaddocumentManagementDTO;
	}

	public void setUploaddocumentManagementDTO(DocumentManagementDTO uploaddocumentManagementDTO) {
		this.uploaddocumentManagementDTO = uploaddocumentManagementDTO;
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

	public String getNewDocCode() {
		return newDocCode;
	}

	public void setNewDocCode(String newDocCode) {
		this.newDocCode = newDocCode;
	}

	public String getNewDocDes() {
		return newDocDes;
	}

	public void setNewDocDes(String newDocDes) {
		this.newDocDes = newDocDes;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public FileUploadEvent getFile() {
		return file;
	}

	public void setFile(FileUploadEvent file) {
		this.file = file;
	}

	public DocumentManagementDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(DocumentManagementDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public String getGenerate() {
		return generate;
	}

	public void setGenerate(String generate) {
		this.generate = generate;
	}

	public DocumentManagementDTO getSelectedRemovePathRow() {
		return selectedRemovePathRow;
	}

	public void setSelectedRemovePathRow(DocumentManagementDTO selectedRemovePathRow) {
		this.selectedRemovePathRow = selectedRemovePathRow;
	}

	public StreamedContent getDownlodfile() {
		return downlodfile;
	}

	public void setDownlodfile(StreamedContent downlodfile) {
		this.downlodfile = downlodfile;
	}

	public List<DocumentManagementDTO> getMandatoryViewList() {
		return mandatoryViewList;
	}

	public void setMandatoryViewList(List<DocumentManagementDTO> mandatoryViewList) {
		this.mandatoryViewList = mandatoryViewList;
	}

	public List<DocumentManagementDTO> getOptionalViewList() {
		return optionalViewList;
	}

	public void setOptionalViewList(List<DocumentManagementDTO> optionalViewList) {
		this.optionalViewList = optionalViewList;
	}

	public List<DocumentManagementDTO> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<DocumentManagementDTO> checkList) {
		this.checkList = checkList;
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	public List<DocumentManagementDTO> getPermitInfo() {
		return permitInfo;
	}

	public void setPermitInfo(List<DocumentManagementDTO> permitInfo) {
		this.permitInfo = permitInfo;
	}

	public DocumentManagementDTO getDocumentPermitInfo() {
		return documentPermitInfo;
	}

	public void setDocumentPermitInfo(DocumentManagementDTO documentPermitInfo) {
		this.documentPermitInfo = documentPermitInfo;
	}

	public String getDocErrorMsg() {
		return docErrorMsg;
	}

	public void setDocErrorMsg(String docErrorMsg) {
		this.docErrorMsg = docErrorMsg;
	}

	public String getWarningMsg() {
		return warningMsg;
	}

	public void setWarningMsg(String warningMsg) {
		this.warningMsg = warningMsg;
	}

	public boolean isCheckSearch() {
		return checkSearch;
	}

	public void setCheckSearch(boolean checkSearch) {
		this.checkSearch = checkSearch;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public String getUploadPermitNo() {
		return uploadPermitNo;
	}

	public void setUploadPermitNo(String uploadPermitNo) {
		this.uploadPermitNo = uploadPermitNo;
	}

	public boolean isCurrentChecked() {
		return currentChecked;
	}

	public void setCurrentChecked(boolean currentChecked) {
		this.currentChecked = currentChecked;
	}

	public String getUploadPermitNoVal() {
		return uploadPermitNoVal;
	}

	public void setUploadPermitNoVal(String uploadPermitNoVal) {
		this.uploadPermitNoVal = uploadPermitNoVal;
	}

	public String getTrNTypeCodeDesVal() {
		return trNTypeCodeDesVal;
	}

	public void setTrNTypeCodeDesVal(String trNTypeCodeDesVal) {
		this.trNTypeCodeDesVal = trNTypeCodeDesVal;
	}

	public List<DocumentManagementDTO> getUserManagementMandatoryList() {
		return userManagementMandatoryList;
	}

	public void setUserManagementMandatoryList(List<DocumentManagementDTO> userManagementMandatoryList) {
		this.userManagementMandatoryList = userManagementMandatoryList;
	}

	public List<DocumentManagementDTO> getUserManagementOptionalList() {
		return userManagementOptionalList;
	}

	public void setUserManagementOptionalList(List<DocumentManagementDTO> userManagementOptionalList) {
		this.userManagementOptionalList = userManagementOptionalList;
	}

	public boolean isHistoryBtn() {
		return historyBtn;
	}

	public void setHistoryBtn(boolean historyBtn) {
		this.historyBtn = historyBtn;
	}

	public List<DocumentManagementDTO> getHistoryViewList() {
		return historyViewList;
	}

	public void setHistoryViewList(List<DocumentManagementDTO> historyViewList) {
		this.historyViewList = historyViewList;
	}

	public DocumentManagementDTO getViewDocumentHistory() {
		return viewDocumentHistory;
	}

	public void setViewDocumentHistory(DocumentManagementDTO viewDocumentHistory) {
		this.viewDocumentHistory = viewDocumentHistory;
	}

	public boolean isCheckdelete() {
		return checkdelete;
	}

	public void setCheckdelete(boolean checkdelete) {
		this.checkdelete = checkdelete;
	}

	public boolean isViewAllTable() {
		return viewAllTable;
	}

	public void setViewAllTable(boolean viewAllTable) {
		this.viewAllTable = viewAllTable;
	}

	public boolean isViewUserManagementTable() {
		return viewUserManagementTable;
	}

	public void setViewUserManagementTable(boolean viewUserManagementTable) {
		this.viewUserManagementTable = viewUserManagementTable;
	}

	public List<String> getAddDocumentTransactionList() {
		return addDocumentTransactionList;
	}

	public void setAddDocumentTransactionList(List<String> addDocumentTransactionList) {
		this.addDocumentTransactionList = addDocumentTransactionList;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public List<String> getEmpNoList() {
		return empNoList;
	}

	public void setEmpNoList(List<String> empNoList) {
		this.empNoList = empNoList;
	}

	public List<DocumentManagementDTO> getMandatoryUserManagementList() {
		return mandatoryUserManagementList;
	}

	public void setMandatoryUserManagementList(List<DocumentManagementDTO> mandatoryUserManagementList) {
		this.mandatoryUserManagementList = mandatoryUserManagementList;
	}

	public List<DocumentManagementDTO> getOptionalUserManagementList() {
		return optionalUserManagementList;
	}

	public void setOptionalUserManagementList(List<DocumentManagementDTO> optionalUserManagementList) {
		this.optionalUserManagementList = optionalUserManagementList;
	}

	public List<DocumentManagementDTO> getMandatoryViewUserManagementList() {
		return mandatoryViewUserManagementList;
	}

	public void setMandatoryViewUserManagementList(List<DocumentManagementDTO> mandatoryViewUserManagementList) {
		this.mandatoryViewUserManagementList = mandatoryViewUserManagementList;
	}

	public List<DocumentManagementDTO> getOptionalViewUserManagementList() {
		return optionalViewUserManagementList;
	}

	public void setOptionalViewUserManagementList(List<DocumentManagementDTO> optionalViewUserManagementList) {
		this.optionalViewUserManagementList = optionalViewUserManagementList;
	}

	public DocumentManagementDTO getViewUserManagementDocumentHistory() {
		return viewUserManagementDocumentHistory;
	}

	public void setViewUserManagementDocumentHistory(DocumentManagementDTO viewUserManagementDocumentHistory) {
		this.viewUserManagementDocumentHistory = viewUserManagementDocumentHistory;
	}

	public List<DocumentManagementDTO> getHistoryUserManagementViewList() {
		return historyUserManagementViewList;
	}

	public void setHistoryUserManagementViewList(List<DocumentManagementDTO> historyUserManagementViewList) {
		this.historyUserManagementViewList = historyUserManagementViewList;
	}

	public boolean isCurrentCheckedForCancelationDocs() {
		return currentCheckedForCancelationDocs;
	}

	public void setCurrentCheckedForCancelationDocs(boolean currentCheckedForCancelationDocs) {
		this.currentCheckedForCancelationDocs = currentCheckedForCancelationDocs;
	}

	public boolean isAmendmentCheck() {
		return amendmentCheck;
	}

	public void setAmendmentCheck(boolean amendmentCheck) {
		this.amendmentCheck = amendmentCheck;
	}

	public List<String> getDocDescriptionList() {
		return docDescriptionList;
	}

	public void setDocDescriptionList(List<String> docDescriptionList) {
		this.docDescriptionList = docDescriptionList;
	}

	public boolean isShowNewPermitSearch() {
		return showNewPermitSearch;
	}

	public void setShowNewPermitSearch(boolean showNewPermitSearch) {
		this.showNewPermitSearch = showNewPermitSearch;
	}

	public boolean isShowTenderSearch() {
		return showTenderSearch;
	}

	public void setShowTenderSearch(boolean showTenderSearch) {
		this.showTenderSearch = showTenderSearch;
	}

	public boolean isShowSurveySearch() {
		return showSurveySearch;
	}

	public void setShowSurveySearch(boolean showSurveySearch) {
		this.showSurveySearch = showSurveySearch;
	}

	public boolean isShowAmendmentsSearch() {
		return showAmendmentsSearch;
	}

	public void setShowAmendmentsSearch(boolean showAmendmentsSearch) {
		this.showAmendmentsSearch = showAmendmentsSearch;
	}

	public String getPermitType() {
		return permitType;
	}

	public void setPermitType(String permitType) {
		this.permitType = permitType;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public boolean isShowPermitRenewal() {
		return showPermitRenewal;
	}

	public void setShowPermitRenewal(boolean showPermitRenewal) {
		this.showPermitRenewal = showPermitRenewal;
	}

	public boolean isPermitPanel() {
		return permitPanel;
	}

	public void setPermitPanel(boolean permitPanel) {
		this.permitPanel = permitPanel;
	}

	public boolean isApplicationPanel() {
		return applicationPanel;
	}

	public void setApplicationPanel(boolean applicationPanel) {
		this.applicationPanel = applicationPanel;
	}

	public boolean isBtnPanel() {
		return btnPanel;
	}

	public void setBtnPanel(boolean btnPanel) {
		this.btnPanel = btnPanel;
	}

	public boolean isSurveyUpload() {
		return surveyUpload;
	}

	public void setSurveyUpload(boolean surveyUpload) {
		this.surveyUpload = surveyUpload;
	}

	public boolean isFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(boolean fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public boolean isTenderRefPanel() {
		return tenderRefPanel;
	}

	public void setTenderRefPanel(boolean tenderRefPanel) {
		this.tenderRefPanel = tenderRefPanel;
	}

	public TenderService getTenderService() {
		return tenderService;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
	}

	public boolean isTenderUpload() {
		return tenderUpload;
	}

	public void setTenderUpload(boolean tenderUpload) {
		this.tenderUpload = tenderUpload;
	}

	public String getSelectPermitType() {
		return selectPermitType;
	}

	public void setSelectPermitType(String selectPermitType) {
		this.selectPermitType = selectPermitType;
	}

	public String getSelectAppType() {
		return selectAppType;
	}

	public void setSelectAppType(String selectAppType) {
		this.selectAppType = selectAppType;
	}

	public List<String> getJoinVehicleNoList() {
		return joinVehicleNoList;
	}

	public void setJoinVehicleNoList(List<String> joinVehicleNoList) {
		this.joinVehicleNoList = joinVehicleNoList;
	}

	public List<String> getJoinAppNoList() {
		return joinAppNoList;
	}

	public void setJoinAppNoList(List<String> joinAppNoList) {
		this.joinAppNoList = joinAppNoList;
	}

	public boolean isPermitForRenewals() {
		return permitForRenewals;
	}

	public void setPermitForRenewals(boolean permitForRenewals) {
		this.permitForRenewals = permitForRenewals;
	}

	public boolean isAmendmentUpload() {
		return amendmentUpload;
	}

	public void setAmendmentUpload(boolean amendmentUpload) {
		this.amendmentUpload = amendmentUpload;
	}

	public List<DocumentManagementDTO> getDeleteDocument() {
		return deleteDocument;
	}

	public void setDeleteDocument(List<DocumentManagementDTO> deleteDocument) {
		this.deleteDocument = deleteDocument;
	}

	public List<String> getAllPermitNoList() {
		return allPermitNoList;
	}

	public void setAllPermitNoList(List<String> allPermitNoList) {
		this.allPermitNoList = allPermitNoList;
	}

	public List<DocumentManagementDTO> getFilterdTransacrtions() {
		return filterdTransacrtions;
	}

	public void setFilterdTransacrtions(List<DocumentManagementDTO> filterdTransacrtions) {
		this.filterdTransacrtions = filterdTransacrtions;
	}

	public boolean isDisableUntilPermitSelect() {
		return disableUntilPermitSelect;
	}

	public void setDisableUntilPermitSelect(boolean disableUntilPermitSelect) {
		this.disableUntilPermitSelect = disableUntilPermitSelect;
	}

	public boolean isDisableUntilPermitAndTransSelect() {
		return disableUntilPermitAndTransSelect;
	}

	public void setDisableUntilPermitAndTransSelect(boolean disableUntilPermitAndTransSelect) {
		this.disableUntilPermitAndTransSelect = disableUntilPermitAndTransSelect;
	}
	
	
	
}
