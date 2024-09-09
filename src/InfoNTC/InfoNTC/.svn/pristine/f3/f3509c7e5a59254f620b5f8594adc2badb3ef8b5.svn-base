package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.ManageInvestigationDTO;
import lk.informatics.ntc.model.dto.ManageRetainDocsDTO;
import lk.informatics.ntc.model.service.ManageRetainDocumentsService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "manageRetainDocumentsBean")
@ViewScoped
public class ManageRetainDocumentsBean implements Serializable {

	// Session
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private static final long serialVersionUID = 1L;

	private Date date = new Date();

	String strDate;

	private String chargeRefNo = null;
	private String permitOwnerName = null;
	private String vehicleNo = null;
	private String permitNo = null;

	private ManageInvestigationDTO manageInvestigationDTO;
	private ManageRetainDocsDTO manageRetainDocsDTO;
	private List<ManageInvestigationDTO> chargeRefNoList;
	private List<ManageRetainDocsDTO> manageRetainDocsDTOList, selectedDocuments;

	private ManageRetainDocumentsService manageRetainDocumentsService;

	private String approveStatus, errorStatus, attentionStatus;

	private StreamedContent files;

	private Boolean printBtncheck = true;

	// init

	@PostConstruct
	public void init() {
		loadData();
	}

	public void loadData() {

		printBtncheck = true;

		manageRetainDocsDTO = new ManageRetainDocsDTO();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		strDate = formatter.format(date);
		manageRetainDocsDTO.setReleaseDate(strDate);

		manageInvestigationDTO = new ManageInvestigationDTO();
		chargeRefNoList = new ArrayList<ManageInvestigationDTO>();
		manageRetainDocsDTOList = new ArrayList<ManageRetainDocsDTO>();
		selectedDocuments = new ArrayList<ManageRetainDocsDTO>();

		manageRetainDocumentsService = (ManageRetainDocumentsService) SpringApplicationContex
				.getBean("manageRetainDocumentsService");

		chargeRefNoList = manageRetainDocumentsService.getChargeRefNoList();
	}

	/**
	 * show Vehicle Number, Permit Number and Permit Owner name
	 */
	public void onChargeRefNoChange() {
		clearButtonAction();

		printBtncheck = true;

		vehicleNo = manageRetainDocumentsService.getVehicleNoByChargeRefNo(manageInvestigationDTO.getChargeRefCode());
		manageInvestigationDTO.setVehicleNo(vehicleNo);

		permitNo = manageRetainDocumentsService.getPermitNoByChargeRefNo(manageInvestigationDTO.getChargeRefCode());
		manageInvestigationDTO.setPermitNo(permitNo);

		permitOwnerName = manageRetainDocumentsService
				.getPermitOwnerByChargeRefNo(manageInvestigationDTO.getChargeRefCode());
		manageInvestigationDTO.setPermitOwnerName(permitOwnerName);

		manageRetainDocsDTOList = manageRetainDocumentsService.getDocDetailsByChargeRefNo(manageInvestigationDTO);

	}

	public void onChargeRefNoChangeNoClear() {

		vehicleNo = manageRetainDocumentsService.getVehicleNoByChargeRefNo(manageInvestigationDTO.getChargeRefCode());
		manageInvestigationDTO.setVehicleNo(vehicleNo);

		permitNo = manageRetainDocumentsService.getPermitNoByChargeRefNo(manageInvestigationDTO.getChargeRefCode());
		manageInvestigationDTO.setPermitNo(permitNo);

		permitOwnerName = manageRetainDocumentsService
				.getPermitOwnerByChargeRefNo(manageInvestigationDTO.getChargeRefCode());
		manageInvestigationDTO.setPermitOwnerName(permitOwnerName);

		manageRetainDocsDTOList = manageRetainDocumentsService.getDocDetailsByChargeRefNo(manageInvestigationDTO);

	}

	// Clear Button Action

	public void clearButtonAction() {

		manageRetainDocsDTO = new ManageRetainDocsDTO();
		date = new Date();
		selectedDocuments = new ArrayList<ManageRetainDocsDTO>();

		printBtncheck = true;
	}

	// Permit Release Button Action

	public void releaseBtnAction() {

		if (manageInvestigationDTO.getChargeRefCode().isEmpty()) {
			setErrorStatus("Please Select a Charge Reffernce Number.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
		}

		else if (selectedDocuments.isEmpty()) {
			setErrorStatus("Please select document(s) to release.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
		}

		else if (manageRetainDocsDTO.getReleaseTo().isEmpty() || manageRetainDocsDTO.getReleaseId().isEmpty()
				|| date == null) {
			setErrorStatus("Please fill mandatory fields.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");

		} else {

			if (!selectedDocuments.isEmpty()) {

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				strDate = formatter.format(date);
				manageRetainDocsDTO.setReleaseDate(strDate);

				// add current date and time as modified date
				java.util.Date date = new java.util.Date();
				Timestamp timestamp = new Timestamp(date.getTime());
				manageRetainDocsDTO.setModifiedDate(timestamp);

				// add current user as modified by
				manageRetainDocsDTO.setModifiedBy(sessionBackingBean.getLoginUser());

				int realesedCount = 0;

				for (int a = 0; a < selectedDocuments.size(); a++) {

					if (!manageRetainDocumentsService.checkReleased(selectedDocuments.get(a))) {
						manageInvestigationDTO.setLoginUser(sessionBackingBean.getLoginUser());
						manageRetainDocsDTO.setChargeRefNo(selectedDocuments.get(a).getChargeRefNo());
						manageRetainDocsDTO.setCode(selectedDocuments.get(a).getCode());
						

						manageRetainDocumentsService.addReleaseDetails(manageInvestigationDTO, manageRetainDocsDTO);

					}

					else {
						realesedCount++;
					}

				}

				if (realesedCount != 0) {
					setAttentionStatus(
							"Selected " + realesedCount + " Document(s) already Released. Other Document(s) Released.");
					RequestContext.getCurrentInstance().update("attentionMsg");
					RequestContext.getCurrentInstance().execute("PF('attentionMsg').show()");

					// update data grid
					onChargeRefNoChangeNoClear();

				} else {
					manageRetainDocumentsService.beanLinkMethod(manageInvestigationDTO, "Documents Release", "Manage Retain Documents");
					setApproveStatus("Selected Documents Release Successfully Saved.");
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

					// update data grid
					onChargeRefNoChangeNoClear();

				}

				printBtncheck = false;

			}

			else {
				setErrorStatus("Please select document(s) to release.");
				RequestContext.getCurrentInstance().update("errorMessage");
				RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			}

		}

	}

	// Print Clearance Letter Action

	public StreamedContent printClearanceLetterBtnAction() {

		if (manageInvestigationDTO.getChargeRefCode().isEmpty()) {
			setErrorStatus("Please Select a Refernace Number.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");

			return null;

		} else if (manageRetainDocsDTO.getReleaseId().isEmpty()) {
			setErrorStatus("Please Enter the ID Number of person.");
			RequestContext.getCurrentInstance().update("errorMessage");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");

			return null;

		} else {

			files = null;
			String sourceFileName = null;
			Connection conn = null;

			try {

				conn = ConnectionManager.getConnection();
				String logopath = "//lk//informatics//ntc//view//reports//";
				sourceFileName = "..//reports//RetainedDocumentReleaseLetter.jrxml";

				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();

				parameters.put("P_Charge_ref_no", manageInvestigationDTO.getChargeRefCode());
				parameters.put("P_Charge_Release_ID", manageRetainDocsDTO.getReleaseId());

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "RetainDocReleaseLetter.pdf");

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "pdf");
				
				try {
					manageRetainDocumentsService.beanLinkMethod(manageInvestigationDTO,"Print Clearance Letter","Manage Retain Documents");
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(conn);
			}

			return files;

		}

	}

	public void onSelectReleaseDoc() throws ParseException {
		if (manageRetainDocumentsService.checkReleased(selectedDocuments.get(0))) {

			manageRetainDocsDTO = manageRetainDocumentsService.getReleaseDetailsByDocDetails(selectedDocuments.get(0));
			String sDate1 = manageRetainDocsDTO.getReleaseDate();
			date = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);

			printBtncheck = false;
		} else {
			manageRetainDocsDTO = new ManageRetainDocsDTO();
			date = new Date();

			printBtncheck = true;
		}

	}

	public void onUnSelectReleaseDoc() {
		printBtncheck = true;
	}

	// getters and setters

	public ManageInvestigationDTO getManageInvestigationDTO() {
		return manageInvestigationDTO;
	}

	public void setManageInvestigationDTO(ManageInvestigationDTO manageInvestigationDTO) {
		this.manageInvestigationDTO = manageInvestigationDTO;
	}

	public List<ManageInvestigationDTO> getChargeRefNoList() {
		return chargeRefNoList;
	}

	public void setChargeRefNoList(List<ManageInvestigationDTO> chargeRefNoList) {
		this.chargeRefNoList = chargeRefNoList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getPermitOwnerName() {
		return permitOwnerName;
	}

	public void setPermitOwnerName(String permitOwnerName) {
		this.permitOwnerName = permitOwnerName;
	}

	public String getChargeRefNo() {
		return chargeRefNo;
	}

	public void setChargeRefNo(String chargeRefNo) {
		this.chargeRefNo = chargeRefNo;
	}

	public ManageRetainDocumentsService getManageRetainDocumentsService() {
		return manageRetainDocumentsService;
	}

	public void setManageRetainDocumentsService(ManageRetainDocumentsService manageRetainDocumentsService) {
		this.manageRetainDocumentsService = manageRetainDocumentsService;
	}

	public List<ManageRetainDocsDTO> getManageRetainDocsDTOList() {
		return manageRetainDocsDTOList;
	}

	public void setManageRetainDocsDTOList(List<ManageRetainDocsDTO> manageRetainDocsDTOList) {
		this.manageRetainDocsDTOList = manageRetainDocsDTOList;
	}

	public ManageRetainDocsDTO getManageRetainDocsDTO() {
		return manageRetainDocsDTO;
	}

	public void setManageRetainDocsDTO(ManageRetainDocsDTO manageRetainDocsDTO) {
		this.manageRetainDocsDTO = manageRetainDocsDTO;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<ManageRetainDocsDTO> getSelectedDocuments() {
		return selectedDocuments;
	}

	public void setSelectedDocuments(List<ManageRetainDocsDTO> selectedDocuments) {
		this.selectedDocuments = selectedDocuments;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public Boolean getPrintBtncheck() {
		return printBtncheck;
	}

	public void setPrintBtncheck(Boolean printBtncheck) {
		this.printBtncheck = printBtncheck;
	}

	public String getAttentionStatus() {
		return attentionStatus;
	}

	public void setAttentionStatus(String attentionStatus) {
		this.attentionStatus = attentionStatus;
	}

}
