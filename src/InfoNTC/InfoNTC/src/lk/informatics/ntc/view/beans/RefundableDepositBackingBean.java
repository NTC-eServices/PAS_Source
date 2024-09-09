package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.TenderService;
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

@ManagedBean(name = "refundableDepositBean")
@ViewScoped
public class RefundableDepositBackingBean {

	private TenderService tenderService;
	private TenderDTO tenderDTO, selectDTO, ajaxDTO;
	private List<TenderDTO> getTenderRefNoList = new ArrayList<TenderDTO>(0);
	private List<TenderDTO> refundingDetails;
	private String alertMSG, successMessage, errorMessage;
	private boolean disabledPrint;
	private StreamedContent files;

	public RefundableDepositBackingBean() {
		tenderDTO = new TenderDTO();
		selectDTO = new TenderDTO();
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		loadValues();
	}

	public void loadValues() {
		getTenderRefNoList = tenderService.getTenderRefNoList();
		disabledPrint = true;
	}

	public void search() {

		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {

			refundingDetails = new ArrayList<>();
			refundingDetails = tenderService.getRefundableDetails(tenderDTO);

			if (!refundingDetails.isEmpty()) {
				disabledPrint = false;
			} else {
				setErrorMessage("No Data Found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Enter Tender Reference No.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void ajaxTenderRefNoSearch() {
		ajaxDTO = new TenderDTO();
		ajaxDTO = tenderService.getTenderDetails(tenderDTO);
		tenderDTO.setTenderDes(ajaxDTO.getTenderDes());
	}

	public void clear() {

		tenderDTO = new TenderDTO();
		refundingDetails.clear();

	}

	public StreamedContent print() throws JRException {

		if (selectDTO != null) {

			String appNo = selectDTO.getTenderAppNo();
			String tenderNO = tenderDTO.getTenderRefNo();

			files = null;
			String sourceFileName = null;

			Connection conn = null;

			try {
				conn = ConnectionManager.getConnection();

				sourceFileName = "..//reports//RefundableDepositLetter.jrxml";

				Map<String, Object> parameters = new HashMap<String, Object>();

				parameters.put("TAP_REF_NO", tenderNO);
				parameters.put("TAP_APP_NO", appNo);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "Refundable deposit Letter.pdf");

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "pdf");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(conn);
			}

		} else {
			setErrorMessage("Please Select a Row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		return files;
	}

	public void selectROW() {

	}

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
	}

	public TenderService getTenderService() {
		return tenderService;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
	}

	public List<TenderDTO> getGetTenderRefNoList() {
		return getTenderRefNoList;
	}

	public void setGetTenderRefNoList(List<TenderDTO> getTenderRefNoList) {
		this.getTenderRefNoList = getTenderRefNoList;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public boolean isDisabledPrint() {
		return disabledPrint;
	}

	public void setDisabledPrint(boolean disabledPrint) {
		this.disabledPrint = disabledPrint;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<TenderDTO> getRefundingDetails() {
		return refundingDetails;
	}

	public TenderDTO getAjaxDTO() {
		return ajaxDTO;
	}

	public void setAjaxDTO(TenderDTO ajaxDTO) {
		this.ajaxDTO = ajaxDTO;
	}

	public void setRefundingDetails(List<TenderDTO> refundingDetails) {
		this.refundingDetails = refundingDetails;
	}

	public TenderDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(TenderDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

}
