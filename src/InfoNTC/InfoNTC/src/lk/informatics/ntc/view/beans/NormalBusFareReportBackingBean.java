package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.service.ReportService;
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

@ManagedBean(name = "normalBusFareReportBackingBean")
@ViewScoped
public class NormalBusFareReportBackingBean {

	private String route;
	private String language;
	private ReportService reportService;
	private List<String> routeList = new ArrayList<String>();
	private StreamedContent files;
	private StationDetailsDTO stationDTO;
	private StationDetailsDTO showfirstOriginDTO, showSecondOriginDTO, showExampleFee;

	@PostConstruct
	public void init() {
		reportService = (ReportService) SpringApplicationContex.getBean("reportService");
		setRouteList(reportService.routeNoDropdown());
		stationDTO = new StationDetailsDTO();
	}

	public StreamedContent reportGenerateEng(ActionEvent ae) throws JRException {
		language = "ENG";

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			int stage = 1;
			int stage1 = 5;
			String stageDes1, stageDes2, feeAns;
			showfirstOriginDTO = reportService.getstartOrigin(route, stage);
			showSecondOriginDTO = reportService.getstartOrigin(route, stage1);
			stageDes1 = showfirstOriginDTO.getStationNameEn();
			stageDes2 = showSecondOriginDTO.getStationNameEn();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			int remainVal = stage1 - stage;

			String remainValString = Integer.toString(remainVal);

			showExampleFee = reportService.getExampleFee(route, remainVal);
			feeAns = showExampleFee.getExampleFee();
			conn = ConnectionManager.getConnection();
			String logopath = "//lk//informatics//ntc//view//reports//";
			sourceFileName = "..//reports//NormalFareTableEnglish.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Route_No", route);
			parameters.put("P_national_logo", logopath);
			parameters.put("P_ntc_logo", logopath);
			parameters.put("P_satge1", stage);
			parameters.put("P_stage2", stage1);
			parameters.put("P_Stage1_des", stageDes1);
			parameters.put("P_stage2_des", stageDes2);
			parameters.put("P_remainVal", remainVal);
			parameters.put("P_fare", feeAns);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "busfare.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

	}

	public StreamedContent reportGenerateSin(ActionEvent ae) throws JRException {
		language = "SIN";

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			int stage = 1;
			int stage1 = 5;
			String stageDes1, stageDes2, feeAns;
			showfirstOriginDTO = reportService.getstartOrigin(route, stage);
			showSecondOriginDTO = reportService.getstartOrigin(route, stage1);
			stageDes1 = showfirstOriginDTO.getStationNameSin();
			stageDes2 = showSecondOriginDTO.getStationNameSin();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			int remainVal = stage1 - stage;

			String remainValString = Integer.toString(remainVal);

			showExampleFee = reportService.getExampleFee(route, remainVal);
			feeAns = showExampleFee.getExampleFee();
			conn = ConnectionManager.getConnection();
			String logopath = "//lk//informatics//ntc//view//reports//";

			sourceFileName = "..//reports//NormalFareTableSinhala.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Route_No", route);
			parameters.put("P_national_logo", logopath);
			parameters.put("P_ntc_logo", logopath);
			parameters.put("P_satge1", stage);
			parameters.put("P_stage2", stage1);
			parameters.put("P_Stage1_des", stageDes1);
			parameters.put("P_stage2_des", stageDes2);
			parameters.put("P_remainVal", remainVal);
			parameters.put("P_fare", feeAns);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "busfare.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

	}

	public void english() {
		language = "ENG";
	}

	public void sinhala() {
		language = "SIN";
	}

	public void tamil() {
		language = "TAM";
	}

	public void clear() {
		language = "";
		route = "";
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<String> routeList) {
		this.routeList = routeList;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public StationDetailsDTO getStationDTO() {
		return stationDTO;
	}

	public void setStationDTO(StationDetailsDTO stationDTO) {
		this.stationDTO = stationDTO;
	}

	public StationDetailsDTO getShowfirstOriginDTO() {
		return showfirstOriginDTO;
	}

	public void setShowfirstOriginDTO(StationDetailsDTO showfirstOriginDTO) {
		this.showfirstOriginDTO = showfirstOriginDTO;
	}

	public StationDetailsDTO getShowSecondOriginDTO() {
		return showSecondOriginDTO;
	}

	public void setShowSecondOriginDTO(StationDetailsDTO showSecondOriginDTO) {
		this.showSecondOriginDTO = showSecondOriginDTO;
	}

}
