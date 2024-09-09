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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.service.CommonService;
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

@ManagedBean(name = "newRouteRequestBackingBean")
@ViewScoped
public class NewRouteRequestBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<CommonDTO> datatableList = new ArrayList<CommonDTO>();
	private CommonDTO selectedRow = new CommonDTO();
	private CommonService commonService;
	private String sucessMsg;
	private StreamedContent files;

	@PostConstruct
	public void init() {
		setCommonService((CommonService) SpringApplicationContex.getBean("commonService"));
		setDatatableList(commonService.routeRequest());
	}

	public void completed() {
		String user = sessionBackingBean.getLoginUser();
		commonService.completed(selectedRow.getSeqNo(), user);
		selectedRow.getSeqNo();
		setSucessMsg("Completed successfully");
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		setDatatableList(commonService.routeRequest());
	}

	public void generateReport() throws JRException {
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			String logopath = "//lk//informatics//ntc//view//reports//";

			sourceFileName = "..//reports//ExistingAnd RequestedRouteDetails.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_LOGO", logopath);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "NewRequestedRouteInformation.pdf");

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

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public CommonDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(CommonDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public List<CommonDTO> getDatatableList() {
		return datatableList;
	}

	public void setDatatableList(List<CommonDTO> datatableList) {
		this.datatableList = datatableList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}
}
