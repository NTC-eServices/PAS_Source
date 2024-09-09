package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.WindscreenLabelDTO;
import lk.informatics.ntc.model.service.WindscreenLabelService;
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

@ManagedBean(name = "windsCreenPrintBackingBean")
@ViewScoped
public class WindscreenLabelPrintBackingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// DTO
	private WindscreenLabelDTO windscreenDTO, fillterPermitNo, showData, statusDTO;

	private List<WindscreenLabelDTO> busNumbersList;
	private List<WindscreenLabelDTO> permitNumbersList;

	// Service
	private WindscreenLabelService windscreenService;

	private String errorMsg, successMessage, vehicelNo;
	private StreamedContent files;

	@PostConstruct
	public void init() {
		windscreenService = (WindscreenLabelService) SpringApplicationContex.getBean("windscreenLabelService");
		windscreenDTO = new WindscreenLabelDTO();
		busNumbersList = windscreenService.getBusNumbers();
		permitNumbersList = windscreenService.getPermitNo();
	}

	public void fillPermitNo() {

		fillterPermitNo = windscreenService.getPermitNo(windscreenDTO.getBusNo());
		windscreenDTO.setPermitNo(fillterPermitNo.getPermitNo());

	}

	public void searchDetails() {

		if (windscreenDTO.getBusNo() != null && !windscreenDTO.getBusNo().trim().isEmpty()
				&& windscreenDTO.getPermitNo() != null && !windscreenDTO.getPermitNo().trim().isEmpty()) {

			showData = windscreenService.showSearchedDetails(windscreenDTO.getBusNo(), windscreenDTO.getPermitNo());
			windscreenDTO.setRouteNo(showData.getRouteNo());
			windscreenDTO.setServiceType(showData.getServiceType());
			windscreenDTO.setOrigin(showData.getOrigin());
			windscreenDTO.setDestination(showData.getDestination());
			windscreenDTO.setVia(showData.getVia());
			windscreenDTO.setApplicationNo(showData.getApplicationNo());
			windscreenDTO.setExpiryDate(showData.getExpiryDate());

		} else {

			setErrorMsg("Please select a bus number for searching");
			RequestContext.getCurrentInstance().update("errorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
		}
	}

	public void clearAll() {

		windscreenDTO = new WindscreenLabelDTO();

	}

	public StreamedContent printWindscreenLable(ActionEvent ae) throws JRException {

		vehicelNo = windscreenDTO.getBusNo();

		files = null;
		String sourceFileName = null;

		Connection conn = null;
		statusDTO = windscreenService.getBusStatus(vehicelNo, windscreenDTO.getPermitNo());
		if (statusDTO.getStatus().equalsIgnoreCase("A")) {
			try {
				conn = ConnectionManager.getConnection();

				sourceFileName = "..//reports//windscreenLabel.jrxml";
				String logopath = "//lk//informatics//ntc//view//reports//";
				Map<String, Object> parameters = new HashMap<String, Object>();

				parameters.put("P_vehicle_no", vehicelNo);
				parameters.put("P_permit_no", windscreenDTO.getPermitNo());
				parameters.put("P_logo", logopath);
				parameters.put("P_watermark", logopath);


				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "WindscreenLabel.pdf");

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

		else {

			setErrorMsg("This bus number not in active status");
			RequestContext.getCurrentInstance().update("errorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
		}
		updateTaskHistory();
		return files;
	}

	public void updateTaskHistory() {
		String loginUser = sessionBackingBean.getLoginUser();
		windscreenService.updateWindscreenLabel(windscreenDTO, loginUser);

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public WindscreenLabelDTO getWindscreenDTO() {
		return windscreenDTO;
	}

	public void setWindscreenDTO(WindscreenLabelDTO windscreenDTO) {
		this.windscreenDTO = windscreenDTO;
	}

	public WindscreenLabelService getWindscreenService() {
		return windscreenService;
	}

	public void setWindscreenService(WindscreenLabelService windscreenService) {
		this.windscreenService = windscreenService;
	}

	public List<WindscreenLabelDTO> getBusNumbersList() {
		return busNumbersList;
	}

	public void setBusNumbersList(List<WindscreenLabelDTO> busNumbersList) {
		this.busNumbersList = busNumbersList;
	}

	public List<WindscreenLabelDTO> getPermitNumbersList() {
		return permitNumbersList;
	}

	public void setPermitNumbersList(List<WindscreenLabelDTO> permitNumbersList) {
		this.permitNumbersList = permitNumbersList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public WindscreenLabelDTO getFillterPermitNo() {
		return fillterPermitNo;
	}

	public void setFillterPermitNo(WindscreenLabelDTO fillterPermitNo) {
		this.fillterPermitNo = fillterPermitNo;
	}

	public WindscreenLabelDTO getShowData() {
		return showData;
	}

	public void setShowData(WindscreenLabelDTO showData) {
		this.showData = showData;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public WindscreenLabelDTO getStatusDTO() {
		return statusDTO;
	}

	public void setStatusDTO(WindscreenLabelDTO statusDTO) {
		this.statusDTO = statusDTO;
	}

}
