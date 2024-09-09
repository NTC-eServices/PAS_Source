package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.PrintInspectionDTO;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "printInspectionReportBackingBean")
@ViewScoped
public class PrintInspectionReportBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private String selectedApplicationNo;
	private String selectedVehicleNo;
	private String errorMsg;
	private String printedApplicationNo;
	private String printedVehicleNo;

	private boolean readOnlyApplicationNoField = false;
	private boolean readOnlyVehicleNoField = false;

	// added
	private StreamedContent files;

	private PrintInspectionDTO printInspectionDTO = new PrintInspectionDTO();
	private PrintInspectionDTO selectedPrintRwo;
	private PrintInspectionDTO vehicleNoForSelectedApplicationNo;
	private PrintInspectionDTO currentVehicleNoChange;
	private PrintInspectionDTO currentApplicationNoChange;

	private InspectionActionPointService inspectionActionPointService;

	public List<PrintInspectionDTO> applicationNoList = new ArrayList<PrintInspectionDTO>(0);
	public List<PrintInspectionDTO> vehcileNoList = new ArrayList<PrintInspectionDTO>(0);
	public List<PrintInspectionDTO> dataList = new ArrayList<PrintInspectionDTO>(0);
	public List<PrintInspectionDTO> viewList = new ArrayList<PrintInspectionDTO>(0);

	public PrintInspectionReportBackingBean() {
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		applicationNoList = inspectionActionPointService.getAllApplicationNoListForViewInspection();
		vehcileNoList = inspectionActionPointService.getAllVehicleNoListForViewInspection();
		defaultValues();

	}

	private void defaultValues() {
		viewList = new ArrayList<PrintInspectionDTO>(0);
		dataList = new ArrayList<PrintInspectionDTO>(0);
		viewList = inspectionActionPointService.getAllRecordsForViewInspectionsDefault();
		dataList.addAll(viewList);
	}

	public void onApplicationNoChange() {

		currentVehicleNoChange = inspectionActionPointService.getCurrentVehicleNoPrint(selectedApplicationNo);
		setSelectedVehicleNo(currentVehicleNoChange.getVehicleNo());
	}

	public void onVehicleNoChange() {

		currentApplicationNoChange = inspectionActionPointService.getCurrentApplicationNoPrint(selectedVehicleNo);
		setSelectedApplicationNo(currentApplicationNoChange.getApplicationNo());
	}

	public void searchDetails() {

		if (!selectedApplicationNo.isEmpty() && selectedApplicationNo != null && !selectedApplicationNo.equals("")
				&& selectedVehicleNo.equals("")) {

			dataList = inspectionActionPointService.getAllRecords(selectedApplicationNo, selectedVehicleNo);
			vehicleNoForSelectedApplicationNo = inspectionActionPointService.getDetails(selectedApplicationNo,
					selectedVehicleNo);
			selectedApplicationNo = vehicleNoForSelectedApplicationNo.getApplicationNo();
			selectedVehicleNo = vehicleNoForSelectedApplicationNo.getVehicleNo();
		} else if (selectedApplicationNo.equals("") && !selectedVehicleNo.isEmpty() && selectedVehicleNo != null
				&& !selectedVehicleNo.equals("")) {

			dataList = inspectionActionPointService.getAllRecords(selectedApplicationNo, selectedVehicleNo);
			vehicleNoForSelectedApplicationNo = inspectionActionPointService.getDetails(selectedApplicationNo,
					selectedVehicleNo);
			selectedApplicationNo = vehicleNoForSelectedApplicationNo.getApplicationNo();
			selectedVehicleNo = vehicleNoForSelectedApplicationNo.getVehicleNo();
		} else if (!selectedApplicationNo.isEmpty() && selectedApplicationNo != null
				&& !selectedApplicationNo.equals("") && !selectedVehicleNo.isEmpty() && selectedVehicleNo != null
				&& !selectedVehicleNo.equals("")) {

			dataList = inspectionActionPointService.getAllRecordsForBoth(selectedApplicationNo, selectedVehicleNo);
		} else if (selectedApplicationNo.equals("") && selectedVehicleNo.equals("")) {

			dataList = new ArrayList<PrintInspectionDTO>(0);

			applicationNoList = inspectionActionPointService.getAllApplicationNoList();
			for (int i = 0; i < applicationNoList.size(); i++) {
				String currentApplicationNo = applicationNoList.get(i).getApplicationNo();
				String currentVehicleNo = applicationNoList.get(i).getVehicleNo();

				viewList = inspectionActionPointService.getAllRecords(currentApplicationNo, currentVehicleNo);
				dataList.addAll(viewList);
			}

		}
	}

	public void clearFields() {

		setSelectedApplicationNo(null);
		setSelectedVehicleNo(null);
		dataList = new ArrayList<PrintInspectionDTO>(0);
		viewList = new ArrayList<PrintInspectionDTO>(0);

		defaultValues();

	}

	public void printActButtonAction() {

		printedApplicationNo = selectedPrintRwo.getOwnerApplicationNo();

	}

	public StreamedContent printActButtonAction(ActionEvent ae) throws JRException {

		String appNumber = (String) ae.getComponent().getAttributes().get("appNo");

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//vehicalInspectionReport.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";
			String subReportPath = "lk/informatics/ntc/view/reports/";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("Application_NO", appNumber);
			parameters.put("LOGO_PATH", logopath);
			parameters.put("SUBREPORT_PATH", subReportPath);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf",
					"Vehicle Inspection Report_" + appNumber + ".pdf");

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

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public PrintInspectionDTO getPrintInspectionDTO() {
		return printInspectionDTO;
	}

	public void setPrintInspectionDTO(PrintInspectionDTO printInspectionDTO) {
		this.printInspectionDTO = printInspectionDTO;
	}

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public List<PrintInspectionDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<PrintInspectionDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public String getSelectedApplicationNo() {
		return selectedApplicationNo;
	}

	public void setSelectedApplicationNo(String selectedApplicationNo) {
		this.selectedApplicationNo = selectedApplicationNo;
	}

	public String getSelectedVehicleNo() {
		return selectedVehicleNo;
	}

	public void setSelectedVehicleNo(String selectedVehicleNo) {
		this.selectedVehicleNo = selectedVehicleNo;
	}

	public PrintInspectionDTO getSelectedPrintRwo() {
		return selectedPrintRwo;
	}

	public void setSelectedPrintRwo(PrintInspectionDTO selectedPrintRwo) {
		this.selectedPrintRwo = selectedPrintRwo;
	}

	public List<PrintInspectionDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PrintInspectionDTO> dataList) {
		this.dataList = dataList;
	}

	public List<PrintInspectionDTO> getViewList() {
		return viewList;
	}

	public void setViewList(List<PrintInspectionDTO> viewList) {
		this.viewList = viewList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isReadOnlyApplicationNoField() {
		return readOnlyApplicationNoField;
	}

	public void setReadOnlyApplicationNoField(boolean readOnlyApplicationNoField) {
		this.readOnlyApplicationNoField = readOnlyApplicationNoField;
	}

	public boolean isReadOnlyVehicleNoField() {
		return readOnlyVehicleNoField;
	}

	public void setReadOnlyVehicleNoField(boolean readOnlyVehicleNoField) {
		this.readOnlyVehicleNoField = readOnlyVehicleNoField;
	}

	public PrintInspectionDTO getVehicleNoForSelectedApplicationNo() {
		return vehicleNoForSelectedApplicationNo;
	}

	public void setVehicleNoForSelectedApplicationNo(PrintInspectionDTO vehicleNoForSelectedApplicationNo) {
		this.vehicleNoForSelectedApplicationNo = vehicleNoForSelectedApplicationNo;
	}

	public List<PrintInspectionDTO> getVehcileNoList() {
		return vehcileNoList;
	}

	public void setVehcileNoList(List<PrintInspectionDTO> vehcileNoList) {
		this.vehcileNoList = vehcileNoList;
	}

	public PrintInspectionDTO getCurrentVehicleNoChange() {
		return currentVehicleNoChange;
	}

	public void setCurrentVehicleNoChange(PrintInspectionDTO currentVehicleNoChange) {
		this.currentVehicleNoChange = currentVehicleNoChange;
	}

	public PrintInspectionDTO getCurrentApplicationNoChange() {
		return currentApplicationNoChange;
	}

	public void setCurrentApplicationNoChange(PrintInspectionDTO currentApplicationNoChange) {
		this.currentApplicationNoChange = currentApplicationNoChange;
	}

}
