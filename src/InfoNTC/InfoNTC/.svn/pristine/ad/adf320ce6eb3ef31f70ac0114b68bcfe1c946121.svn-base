package lk.informatics.ntc.view.beans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.context.RequestContext;
import lk.informatics.ntc.model.dto.AdvertisementDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.TenderAdvertisementService;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "approveBackingBean")
@ViewScoped
public class ApproveTenderAdvertisementBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private TenderAdvertisementService addService;
	public DocumentManagementService documentManagementService;
	private List<AdvertisementDTO> dataList = new ArrayList<AdvertisementDTO>();
	private List<AdvertisementDTO> publishList = new ArrayList<AdvertisementDTO>();
	private AdvertisementDTO advertisementDTO = new AdvertisementDTO();
	private CommonService commonService;
	private TenderService tenderService;
	private AdvertisementDTO tempheadfootDTO = new AdvertisementDTO();
	private boolean approvebutton = true;
	private boolean rejectbutton = true;
	private boolean pdfCheck = true;
	private TenderDTO tenderDTO = new TenderDTO();
	public String rejectReason;
	public String status;
	public String errorMsg;
	public String successMSG;
	public String description;
	private String filePath = "/resources/demo/media/guide.pdf";
	private List<TenderDTO> tenderDetailList;

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

	@PostConstruct
	public void init() {
		addService = (TenderAdvertisementService) SpringApplicationContex.getBean("addService");
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		dataList = addService.approvalrefNodropdown();
		pdfCheck = true;

		RequestContext.getCurrentInstance().update("xx");
		RequestContext.getCurrentInstance().execute("PF('fill').hide()");
	}

	public void onDateChange() {
		sessionBackingBean.setPublishDate(advertisementDTO.getPublishDate());
	}

	public void onReasonChange() {
		sessionBackingBean.setRejectReason(rejectReason);
	}

	public void onrefNoChange() {
		description = addService.description(advertisementDTO.getTenderNo());
		advertisementDTO.setDescription(description);

		// setting values for session bean
		sessionBackingBean.setTenderNo(advertisementDTO.getTenderNo());
		sessionBackingBean.setDescription(description);
	}

	public void search() {
		if (advertisementDTO.getTenderNo().equals("")) {
			errorMsg = "Please select a Reference No.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
		} else {
			status = addService.approvalStatus(advertisementDTO.getTenderNo());
			pdfCheck = false;
			if (status == null) {
				status = "N";
			}

			if (status.equals("A") || status.equals("R")) {
				approvebutton = true;
				rejectbutton = true;
				pdfCheck = false;
				// checking if file exists to enable button
				File file = new File("D:\\Shared NTC\\Tender Advertisement\\Tender Advertisement"
						+ advertisementDTO.getTenderNo() + ".pdf");
				boolean exists = file.exists();

				if (exists == false) {
					pdfCheck = true;
				}
				sessionBackingBean.setPdfCheck(pdfCheck);
				sessionBackingBean.setApprovebutton(approvebutton);
				sessionBackingBean.setRejectbutton(rejectbutton);
			} else {
				pdfCheck = false;
				approvebutton = false;
				rejectbutton = false;

				// checking to enable button
				File file = new File("D:\\Shared NTC\\Tender Advertisement\\Tender Advertisement"
						+ advertisementDTO.getTenderNo() + ".pdf");
				boolean exists = file.exists();

				if (exists == false) {
					pdfCheck = true;
				} else {
					filePath = file.toString();
				}
				sessionBackingBean.setPdfCheck(pdfCheck);
				sessionBackingBean.setApprovebutton(approvebutton);
				sessionBackingBean.setRejectbutton(rejectbutton);

				tenderDetailList = new ArrayList<>();
				tenderDetailList = tenderService.getDetails_tender_details(advertisementDTO.getTenderNo());

				commonService.updateTaskStatusTenderInSurveyTaskTabel(advertisementDTO.getTenderNo(), "TD002", "TD003",
						"C", sessionBackingBean.getLoginUser());
			}
		}
	}

	public void clear() {
		advertisementDTO = new AdvertisementDTO();
		approvebutton = true;
		rejectbutton = true;
		sessionBackingBean.setApprovebutton(approvebutton);
		sessionBackingBean.setRejectbutton(rejectbutton);
		rejectReason = "";
		pdfCheck = true;
		sessionBackingBean.setTenderNo(advertisementDTO.getTenderNo());
		sessionBackingBean.setDescription(advertisementDTO.getDescription());
		sessionBackingBean.setRejectReason(rejectReason);
	}

	public void checkPublication() {
		publishList = addService.publishTable();
		RequestContext.getCurrentInstance().update("lala");
		RequestContext.getCurrentInstance().execute("PF('dlg3').show()");
	}

	public void approve() {
		String setStatus = "A";
		String empty = "";
		if (advertisementDTO.getPublishDate() != null) {

			addService.updateStatus(advertisementDTO, setStatus, empty);
			successMSG = "Approved Successfully";
			RequestContext.getCurrentInstance().update("frmsuccess");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			approvebutton = true;
			sessionBackingBean.setApprovebutton(approvebutton);
			sessionBackingBean.setPublishDate(advertisementDTO.getPublishDate());

			/*
			 * Change the task updating method, Edited by Gayathra
			 */
			commonService.updateTaskStatusCompletedTenderInSurveyTaskTabel(advertisementDTO.getTenderNo(), "TD003");
			dataList = addService.approvalrefNodropdown();
		} else {
			errorMsg = "Tender publish Date is Mandatory";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
		}
	}

	public void reject() {

		String setStatus = "R";
		boolean check = false;
		boolean firsterror = false;
		boolean valid = true;
		boolean checker = true;

		if (advertisementDTO.getPublishDate() != null && !advertisementDTO.getPublishDate().equals("")) {
			checker = false;
		}

		if (rejectReason.equals(null) || (rejectReason.equals(""))) {

			errorMsg = "Please enter valid reject Reason";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
			firsterror = true;
			valid = false;
			checker = false;

		}

		if (checker == true && firsterror == false) {
			{
				errorMsg = "Tender Publish Date is Mandatory";
				RequestContext.getCurrentInstance().update("xx");
				RequestContext.getCurrentInstance().execute("PF('fill').show()");
				valid = false;
			}
		}

		if (valid == true) {

			status = addService.approvalStatus(advertisementDTO.getTenderNo());
			addService.updateStatus(advertisementDTO, setStatus, rejectReason);
			rejectbutton = false;
			sessionBackingBean.setPublishDate(advertisementDTO.getPublishDate());
			sessionBackingBean.setRejectbutton(rejectbutton);
			successMSG = "Rejected Successfully";
			RequestContext.getCurrentInstance().update("frmsuccess");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			clear();
			boolean isTasKTD003Available = tenderService.checkTaskDetailsInSurvey(tenderDTO, "TD003", "C");
			commonService.updateSurveyTaskDetails(advertisementDTO.getTenderNo(), null, "TD003", "O",
					sessionBackingBean.getLoginUser());
			clear();
			if (isTasKTD003Available == false) {
				String loggedUser = sessionBackingBean.loginUser;
				tenderService.insertTaskDetailsSurvey(tenderDTO, loggedUser, "TD003", "C");

			} 

		}
	}

	public void downloadPDF() throws IOException {

		// Prepare.
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
		
		File file = new File(
				"D:\\Shared NTC\\Tender Advertisement\\Tender Advertisement" + advertisementDTO.getTenderNo() + ".pdf");
		boolean exists = file.exists();

		BufferedInputStream input = null;
		BufferedOutputStream output = null;

		try {
			// Open file.
			input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);

			// Init servlet response.
			response.reset();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Length", String.valueOf(file.length()));
			response.setHeader("Content-Disposition", "inline; filename=\"" + "TokenNumber" + "\"");
			output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

			// Write file contents to response.
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}

			// Finalize task.
			output.flush();
		}

		catch (IOException ex) {

			ex.printStackTrace();
			errorMsg = "PDF not found for advertisement.";
			RequestContext.getCurrentInstance().update("xx");
			RequestContext.getCurrentInstance().execute("PF('fill').show()");
		}

		finally {
			// Gently close streams.
			close(output);
			close(input);
		}

		facesContext.responseComplete();
	}

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void documentUpload() {

		DocumentManagementDTO uploaddocumentManagementDTO = new DocumentManagementDTO();
		try {

			sessionBackingBean.setPermitRenewalPermitNo(advertisementDTO.getTenderNo());

			sessionBackingBean.setPermitRenewalTranstractionTypeDescription("TENDER");

			String applicationNo = tenderService.getApplicationNoFromRefNo(advertisementDTO.getTenderNo());

			sessionBackingBean.setApplicationNoForDoc(applicationNo);

			uploaddocumentManagementDTO.setUpload_Permit(advertisementDTO.getTenderNo());
			uploaddocumentManagementDTO.setTransaction_Type("TENDER");

			mandatoryList = documentManagementService.mandatoryDocs("01", advertisementDTO.getTenderNo());
			optionalList = documentManagementService.optionalDocs("01", advertisementDTO.getTenderNo());

			sessionBackingBean.optionalList = documentManagementService.optionalDocs("01",
					advertisementDTO.getTenderNo());

			sessionBackingBean.newPermitMandatoryDocumentList = documentManagementService
					.newPermitMandatoryList(advertisementDTO.getTenderNo());
			sessionBackingBean.newPermitOptionalDocumentList = documentManagementService
					.newPermitOptionalList(advertisementDTO.getTenderNo());
			sessionBackingBean.permitRenewalMandatoryDocumentList = documentManagementService
					.permitRenewalMandatoryList(advertisementDTO.getTenderNo());
			sessionBackingBean.permitRenewalOptionalDocumentList = documentManagementService
					.permitRenewalOptionalList(advertisementDTO.getTenderNo());
			sessionBackingBean.backlogManagementOptionalDocumentList = documentManagementService
					.backlogManagementOptionalList(advertisementDTO.getTenderNo());

			sessionBackingBean.amendmentToBusOwnerMandatoryDocumentList = documentManagementService
					.amendmentToBusOwnerMandatoryList(advertisementDTO.getTenderNo());
			sessionBackingBean.amendmentToBusOwnerOptionalDocumentList = documentManagementService
					.amendmentToBusOwnerOptionalList(advertisementDTO.getTenderNo());
			sessionBackingBean.amendmentToBusMandatoryDocumentList = documentManagementService
					.amendmentToBusMandatoryList(advertisementDTO.getTenderNo());
			sessionBackingBean.amendmentToBusOptionalDocumentList = documentManagementService
					.amendmentToBusOptionalList(advertisementDTO.getTenderNo());
			sessionBackingBean.amendmentToOwnerBusMandatoryDocumentList = documentManagementService
					.amendmentToOwnerBusMandatoryList(advertisementDTO.getTenderNo());
			sessionBackingBean.amendmentToOwnerBusOptionalDocumentList = documentManagementService
					.amendmentToOwnerBusOptionalList(advertisementDTO.getTenderNo());
			sessionBackingBean.amendmentToServiceBusMandatoryDocumentList = documentManagementService
					.amendmentToServiceBusMandatoryList(advertisementDTO.getTenderNo());
			sessionBackingBean.amendmentToServiceBusOptionalDocumentList = documentManagementService
					.amendmentToServiceBusOptionalList(advertisementDTO.getTenderNo());
			sessionBackingBean.amendmentToServiceMandatoryDocumentList = documentManagementService
					.amendmentToServiceMandatoryList(advertisementDTO.getTenderNo());
			sessionBackingBean.amendmentToServiceOptionalDocumentList = documentManagementService
					.amendmentToServiceOptionalList(advertisementDTO.getTenderNo());

			sessionBackingBean.tenderMandatoryDocumentList = documentManagementService
					.tenderMandatoryList(advertisementDTO.getTenderNo());
			sessionBackingBean.tenderOptionalDocumentList = documentManagementService
					.tenderOptionalList(advertisementDTO.getTenderNo());
			RequestContext.getCurrentInstance().execute("PF('uploadTenderDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Getters and Setters

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public TenderAdvertisementService getAddService() {
		return addService;
	}

	public void setAddService(TenderAdvertisementService addService) {
		this.addService = addService;
	}

	public AdvertisementDTO getAdvertisementDTO() {
		return advertisementDTO;
	}

	public void setAdvertisementDTO(AdvertisementDTO advertisementDTO) {
		this.advertisementDTO = advertisementDTO;
	}

	public List<AdvertisementDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<AdvertisementDTO> dataList) {
		this.dataList = dataList;
	}

	public boolean isApprovebutton() {
		return approvebutton;
	}

	public void setApprovebutton(boolean approvebutton) {
		this.approvebutton = approvebutton;
	}

	public boolean isRejectbutton() {
		return rejectbutton;
	}

	public void setRejectbutton(boolean rejectbutton) {
		this.rejectbutton = rejectbutton;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSuccessMSG() {
		return successMSG;
	}

	public void setSuccessMSG(String successMSG) {
		this.successMSG = successMSG;
	}

	public List<AdvertisementDTO> getPublishList() {
		return publishList;
	}

	public void setPublishList(List<AdvertisementDTO> publishList) {
		this.publishList = publishList;
	}

	public AdvertisementDTO getTempheadfootDTO() {
		return tempheadfootDTO;
	}

	public void setTempheadfootDTO(AdvertisementDTO tempheadfootDTO) {
		this.tempheadfootDTO = tempheadfootDTO;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public TenderService getTenderService() {
		return tenderService;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
	}

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
	}

	public boolean isPdfCheck() {
		return pdfCheck;
	}

	public void setPdfCheck(boolean pdfCheck) {
		this.pdfCheck = pdfCheck;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<TenderDTO> getTenderDetailList() {
		return tenderDetailList;
	}

	public void setTenderDetailList(List<TenderDTO> tenderDetailList) {
		this.tenderDetailList = tenderDetailList;
	}

}
