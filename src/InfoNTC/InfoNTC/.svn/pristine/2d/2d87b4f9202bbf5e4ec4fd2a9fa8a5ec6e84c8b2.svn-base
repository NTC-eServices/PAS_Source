package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.component.export.ExporterFactory;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.BusDetailsDTO;
import lk.informatics.ntc.model.dto.SetUpMidPointsDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.SetUpMidPointsService;
import lk.informatics.ntc.model.service.TimeTableService;
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

import com.lowagie.text.Document;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;

@ManagedBean(name = "setUpMidPointsBackingBean")
@ViewScoped

public class SetUpMidPointsBackingBean implements Serializable {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String selectedRouteNumber;
	private String selectedServiceType;
	private String groupNo;
	private boolean tickingOption;
	private String stationTime;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
	private boolean saveClicked;
	private boolean visibleColumn;
	private boolean disableDropDown;
	private boolean disableButton;
	private String errorMessage, alertMSG, successMessage;
	
	private boolean print;

	// DTOs
	private SetUpMidPointsDTO routeDTO;
	private SetUpMidPointsDTO setUpMidPointsDTO;
	private SetUpMidPointsDTO midData;
	private SetUpMidPointsDTO descript;
	// Services
	private SetUpMidPointsService setUpMidPointsService;
	private AdminService adminService;
	private TimeTableService timeTableService;

	// Lists
	private List<SetUpMidPointsDTO> routeList = new ArrayList<SetUpMidPointsDTO>();
	private List<SetUpMidPointsDTO> serviceType = new ArrayList<SetUpMidPointsDTO>();
	private List<SetUpMidPointsDTO> orgToDesMidPointList;
	private List<SetUpMidPointsDTO> desToOrgMidPointList;
	private List<SetUpMidPointsDTO> selectedOrgToDesMidPointList;
	private List<SetUpMidPointsDTO> selectedDesToOrgMidPointList;
	private List<TimeTableDTO> midList = new ArrayList<TimeTableDTO>();
	private List<BusDetailsDTO> busDetailsList;
	private List<BusDetailsDTO> busDetailsListDesToOrg = new ArrayList<BusDetailsDTO>();
	private List<BusDetailsDTO> selectedOrgToDesBusDetailsList;
	private List<BusDetailsDTO> selectedDesToOrgBusDetailsList;
	private List<BusDetailsDTO> selectedAllOrgToDesBusDetailsList;
	private List<BusDetailsDTO> selectedAllDesToOrgBusDetailsList;
	private List<BusDetailsDTO> combineList;
	private List<Map<String, List<Map<String, String>>>> timeList;
	private List<Map<String, List<Map<String, String>>>> TimeListDes;
	private List<SetUpMidPointsDTO> abbreviationListOrigin = new ArrayList<SetUpMidPointsDTO>();
	private List<SetUpMidPointsDTO> abbreviationListDestination = new ArrayList<SetUpMidPointsDTO>();
	private List<SetUpMidPointsDTO> busNoList = new ArrayList<SetUpMidPointsDTO>();
	private List<SetUpMidPointsDTO> permitNoList = new ArrayList<SetUpMidPointsDTO>();
	private List<Column> generatedColumns = new ArrayList<>();
	private List<Column> generatedColumns2 = new ArrayList<>();
	private List<SelectItem> routeNumbersWithNames;
	public List<SetUpMidPointsDTO> startTimeList = new ArrayList<SetUpMidPointsDTO>();
	public List<SetUpMidPointsDTO> midPointNameList = new ArrayList<SetUpMidPointsDTO>();
	public List<SetUpMidPointsDTO> midPointTimeList = new ArrayList<SetUpMidPointsDTO>();
	private List<String> stationTimes;
	public List<SetUpMidPointsDTO> startTimeList2 = new ArrayList<SetUpMidPointsDTO>();
	public List<SetUpMidPointsDTO> nameList = new ArrayList<SetUpMidPointsDTO>();
	private List<SetUpMidPointsDTO> startTimeListtt = new ArrayList<SetUpMidPointsDTO>();
	private List<BusDetailsDTO> permitNumList = new ArrayList<BusDetailsDTO>();
	private List<String> midNameOri;
	private List<String> midTimeOri;

	private List<String> midNameDes;
	private List<String> midTimeDes;

	private boolean saveButton;

	private StreamedContent files;

	@PostConstruct
	public void init() {

		setUpMidPointsService = (SetUpMidPointsService) SpringApplicationContex.getBean("setUpMidPointsService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");

		loadValues();
	}

	public void loadValues() {

		routeList = setUpMidPointsService.getRoutesToDropdown();

		serviceType = setUpMidPointsService.getServiceTypeToDropDown();
		routeDTO = new SetUpMidPointsDTO();
		setUpMidPointsDTO = new SetUpMidPointsDTO();
		selectedRouteNumber = null;
		selectedServiceType = null;
		groupNo = null;
		abbreviationListOrigin = setUpMidPointsService.getAbbriviation();
		abbreviationListDestination = setUpMidPointsService.getAbbriviationDes();
		busNoList = setUpMidPointsService.getBusNoToDropDown();
		permitNoList = setUpMidPointsService.getPermitNoToDropDown();

		orgToDesMidPointList = new ArrayList<SetUpMidPointsDTO>(0);
		desToOrgMidPointList = new ArrayList<SetUpMidPointsDTO>(0);
		selectedOrgToDesMidPointList = new ArrayList<SetUpMidPointsDTO>(0);
		selectedDesToOrgMidPointList = new ArrayList<SetUpMidPointsDTO>(0);
		selectedDesToOrgBusDetailsList = new ArrayList<BusDetailsDTO>();
		selectedOrgToDesBusDetailsList = new ArrayList<BusDetailsDTO>();

		selectedAllOrgToDesBusDetailsList = new ArrayList<BusDetailsDTO>();
		selectedAllDesToOrgBusDetailsList = new ArrayList<BusDetailsDTO>();
		
		combineList = new ArrayList<BusDetailsDTO>();

		setSaveClicked(true);
		visibleColumn = false;
		disableDropDown = false;
		disableButton = true;
		saveButton = true;
		print  = true;

		midNameOri = new ArrayList<>();
		midTimeOri = new ArrayList<>();

		midNameDes = new ArrayList<>();
		midTimeDes = new ArrayList<>();
	}

	// Get Route numbers with Description
	public List<SelectItem> getRouteNumbersWithNames() {
		if (routeNumbersWithNames == null) {
			routeNumbersWithNames = new ArrayList<>();
			// Populate the list of route numbers with their names from the database
			List<SetUpMidPointsDTO> routes = setUpMidPointsService.getdesCriptionToDropdown();
			for (SetUpMidPointsDTO route : routes) {
				String routeNumber = route.getRouteNo();
				String routeNameOrigin = route.getOrigin();
				String routeNameDes = route.getDestination();
				String label = routeNumber + "  " + routeNameOrigin + "-" + routeNameDes;
				routeNumbersWithNames.add(new SelectItem(routeNumber, label));
			}
		}
		return routeNumbersWithNames;
	}

	// Get Origin And Destination
	public void generateOriginAndDestination() {

		routeDTO = setUpMidPointsService.getDetailsbyRouteNo(selectedRouteNumber);

		if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().trim().equalsIgnoreCase("")) {
			setUpMidPointsDTO.setOrigin(routeDTO.getOrigin());
		}

		if (routeDTO.getDestination() != null && !routeDTO.getDestination().trim().equalsIgnoreCase("")) {
			setUpMidPointsDTO.setDestination(routeDTO.getDestination());
		}

		visibleColumn = true;

	}

	// Get Mid Points To The Table
	// Search
	public void search() {

		if ((selectedRouteNumber == null || selectedRouteNumber.trim().equalsIgnoreCase(""))
				|| (selectedServiceType == null || selectedServiceType.trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please Select All Fields");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {
			generateOriginAndDestination();

			orgToDesMidPointList = setUpMidPointsService.getAllMidPointsOrgToDes(selectedRouteNumber,
					selectedServiceType);

			if (orgToDesMidPointList.isEmpty()) {
				setErrorMessage("Data Not Found");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

			desToOrgMidPointList = setUpMidPointsService.getAllMidPointsDesToOrg(selectedRouteNumber,
					selectedServiceType);

			if (desToOrgMidPointList.isEmpty()) {
				setErrorMessage("Data Not Found");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

			startTimeListtt = setUpMidPointsService.getStartTime(selectedRouteNumber, selectedServiceType);

			selectedOrgToDesMidPointList = new ArrayList<SetUpMidPointsDTO>();
			selectedOrgToDesMidPointList.addAll(orgToDesMidPointList);
			selectedDesToOrgMidPointList = new ArrayList<SetUpMidPointsDTO>();
			selectedDesToOrgMidPointList.addAll(desToOrgMidPointList);
			// selectedDesToOrgMidPointList = desToOrgMidPointList;
		}

	}

	public void clearAction() {
		routeDTO = new SetUpMidPointsDTO();
		setUpMidPointsDTO = new SetUpMidPointsDTO();
		selectedRouteNumber = null;
		selectedServiceType = null;
		groupNo = null;
		
		selectedRouteNumber = null;
		selectedServiceType = null;

		orgToDesMidPointList = new ArrayList<SetUpMidPointsDTO>(0);
		desToOrgMidPointList = new ArrayList<SetUpMidPointsDTO>(0);
		selectedOrgToDesMidPointList = new ArrayList<SetUpMidPointsDTO>(0);
		selectedDesToOrgMidPointList = new ArrayList<SetUpMidPointsDTO>(0);
		selectedDesToOrgBusDetailsList = new ArrayList<BusDetailsDTO>();
		selectedOrgToDesBusDetailsList = new ArrayList<BusDetailsDTO>();

		selectedAllOrgToDesBusDetailsList = new ArrayList<BusDetailsDTO>();
		selectedAllDesToOrgBusDetailsList = new ArrayList<BusDetailsDTO>();
		busDetailsList = new ArrayList<BusDetailsDTO>();
		busDetailsListDesToOrg = new ArrayList<BusDetailsDTO>();
		
		combineList = new ArrayList<BusDetailsDTO>();

		setSaveClicked(true);
		visibleColumn = false;
		disableDropDown = false;
		disableButton = true;
		saveButton = true;
		print  = true;

		midNameOri = new ArrayList<>();
		midTimeOri = new ArrayList<>();

		midNameDes = new ArrayList<>();
		midTimeDes = new ArrayList<>();

		generatedColumns.clear();
		generatedColumns2.clear();
	}

	// Save Data
	public void saveAction(String side) {
		List<BusDetailsDTO> busDetailsListToUse = side.equals("O") ? busDetailsList : busDetailsListDesToOrg;
		List<BusDetailsDTO> selectedListToUse = side.equals("O") ? selectedOrgToDesBusDetailsList
				: selectedDesToOrgBusDetailsList;

		if (side.equals("O") || side.equals("D")) {
			if (side.equals("O")) {
				setUpMidPointsService.updateTimeOrigin(busDetailsList);
			} else if (side.equals("D")) {
				setUpMidPointsService.updateTimeDestination(busDetailsListDesToOrg);
			}

			for (BusDetailsDTO data : busDetailsListToUse) {
				data.setFix(selectedListToUse.contains(data) ? "Y" : "N");
			}

			if (side.equals("O")) {
				setUpMidPointsService.setMidPointDataOrgToDes(busDetailsList,groupNo);
			} else if (side.equals("D")) {
				setUpMidPointsService.setMidPointDataDesToOrg(busDetailsListDesToOrg,groupNo);
			}

			setSuccessMessage("Saved Successfully");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		}
	}

	public void saveActionDO() {
		if (!selectedDesToOrgBusDetailsList.isEmpty()) {

			selectedAllDesToOrgBusDetailsList.addAll(selectedDesToOrgBusDetailsList);
			selectedDesToOrgBusDetailsList = new ArrayList<>();
			busDetailsListDesToOrg = new ArrayList<>();

			setSuccessMessage("Saved Successfull");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {
			setErrorMessage("Please select Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	// Save Selected Data In The Database
	public void saveCheckedRows() {
		if ((!selectedOrgToDesMidPointList.isEmpty()) || (!selectedDesToOrgMidPointList.isEmpty())) {
			setUpMidPointsService.updateTakenTime(selectedOrgToDesMidPointList, selectedDesToOrgMidPointList);

			for (SetUpMidPointsDTO data : selectedOrgToDesMidPointList) {
				midNameOri.add(data.getMidPointName());
				midTimeOri.add(data.getTimeTaken());
			}

			for (SetUpMidPointsDTO data : selectedDesToOrgMidPointList) {
				midNameDes.add(data.getMidPointName());
				midTimeDes.add(data.getTimeTaken());
			}

			setSaveClicked(false);

		} else {
			setErrorMessage("Please select Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	// Generate Time Slots
	public void generateTimeSlots() {
		if (!selectedOrgToDesMidPointList.isEmpty()) {
			busDetailsList = new ArrayList<BusDetailsDTO>();
			busDetailsListDesToOrg = new ArrayList<BusDetailsDTO>();
			
			busDetailsList = setUpMidPointsService.getDetailsByRouteAndServiceType(selectedRouteNumber,
					selectedServiceType, selectedOrgToDesMidPointList, orgToDesMidPointList,groupNo);
			saveButton = false;

			for (BusDetailsDTO data : busDetailsList) {
				if (data.isFixedBus()) {
					selectedOrgToDesBusDetailsList.add(data);
				}
			}

			if (busDetailsList.isEmpty()) {
				setErrorMessage("Panel Generator Submission Pending");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
		if (!selectedDesToOrgMidPointList.isEmpty()) {
			busDetailsListDesToOrg = setUpMidPointsService.getDetailsByRouteAndServiceTypeDesToOrg(selectedRouteNumber,
					selectedServiceType, selectedDesToOrgMidPointList, desToOrgMidPointList,groupNo);

			for (BusDetailsDTO data : busDetailsListDesToOrg) {
				if (data.isFixedBusDes()) {
					selectedDesToOrgBusDetailsList.add(data);
				}
			}

			if (busDetailsListDesToOrg.isEmpty()) {
				setErrorMessage("Panel Generator Submission Pending");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}

		permitNumList = setUpMidPointsService.getpermitNo(selectedRouteNumber);

		// columns for Origin To Destination Table
		generatedColumns.clear();

		for (SetUpMidPointsDTO midpoint : selectedOrgToDesMidPointList) {
			// Create a new column for each midpoint
			Column column = new Column();
			column.setHeaderText(midpoint.getMidPointName());
			generatedColumns.add(column);
		}

		// Columns For Destination To origin Table
		generatedColumns2.clear();

		for (SetUpMidPointsDTO midpoint : selectedDesToOrgMidPointList) {
			// Create a new column for each midpoint
			Column column = new Column();
			column.setHeaderText(midpoint.getMidPointName());
			generatedColumns2.add(column);
		}
		setSaveClicked(false);
		disableDropDown = true;
		disableButton = false;
		print = false;

		int maxSize = Math.max(busDetailsList.size(), busDetailsListDesToOrg.size());

		for (int i = 0; i < maxSize; i++) {
		    BusDetailsDTO newData = new BusDetailsDTO();

		    if (i < busDetailsList.size()) {
		        BusDetailsDTO data = busDetailsList.get(i);
		        newData.setAbbreviation(data.getAbbreviation());
		        newData.setStartTime(data.getStartTime());
		        newData.setEndTime(data.getEndTime());
		        newData.setTakenTime(data.getTakenTime());
		    }

		    if (i < busDetailsListDesToOrg.size()) {
		        BusDetailsDTO dataCheck = busDetailsListDesToOrg.get(i);
		        newData.setAbbreviationDestination(dataCheck.getAbbreviation());
		        newData.setStartTimeDestination(dataCheck.getStartTime());
		        newData.setEndTimeDestination(dataCheck.getEndTime());
		        newData.setTakenTimeDestination(dataCheck.getTakenTime());
		    }

		    combineList.add(newData);
		}
	}
	
	public List<BusDetailsDTO> getCombinedData() {
	    List<BusDetailsDTO> combinedData = new ArrayList<>();

	    if (busDetailsList != null) {
	        combinedData.addAll(busDetailsList);
	    }

	    if (busDetailsListDesToOrg != null) {
	        combinedData.addAll(busDetailsListDesToOrg);
	    }

	    return combinedData;
	}
 

	public void preProcessXLS(Object document) {
	    HSSFWorkbook workbook = (HSSFWorkbook) document;

	    HSSFSheet sheet = workbook.createSheet("CombinedData");

	    List<BusDetailsDTO> combinedData = getCombinedData();

	    // Add the data to the Excel sheet
	    for (int i = 0; i < combinedData.size(); i++) {
	    	BusDetailsDTO data = combinedData.get(i);
	        HSSFRow row = sheet.createRow(i);

	        // Create cells and populate data from YourDataClass
	        HSSFCell cell1 = row.createCell(0);
	        cell1.setCellValue(data.getAbbreviation());

	        HSSFCell cell2 = row.createCell(1);
	        cell2.setCellValue(data.getAbbreviation());

	        // Add more cells for other properties as needed
	    }
	}


	public void preProcessPDFDes(Object document) throws IOException, BadElementException, DocumentException {
		SetUpMidPointsDTO details = setUpMidPointsService.getDetails(selectedRouteNumber, selectedServiceType);
		Document pdf = (Document) document;
		pdf.open();
		pdf.setPageSize(PageSize.A4);
		pdf.setMargins(12.7f, 12.7f, 12.7f, 12.7f);

		Paragraph title = new Paragraph(routeDTO.getDestination().toUpperCase() + " - "
				+ routeDTO.getOrigin().toUpperCase() + " - Route No(" + selectedRouteNumber + ")");
		Paragraph title1 = new Paragraph(selectedServiceType);
		title.setAlignment(Element.ALIGN_CENTER);
		title1.setAlignment(Element.ALIGN_CENTER);

		pdf.add(title);
		pdf.add(title1);
		pdf.add(Chunk.NEWLINE);

		Paragraph details1 = new Paragraph("Distance : " + details.getLength() + " km");
		Paragraph details2 = new Paragraph("Schedule : " + details.getTime() + " h");
		Paragraph details3 = new Paragraph("Speed : " + details.getSpeed() + " kmph");

		pdf.add(details1);
		pdf.add(details2);
		pdf.add(details3);
		pdf.add(Chunk.NEWLINE);

		List<String> pairs = new ArrayList<>();

		String[] timeParts = midTimeDes.get(0).split(":");
		int hours = Integer.parseInt(timeParts[0]);
		int minutes = Integer.parseInt(timeParts[1]);

		pairs.add(routeDTO.getDestination().toUpperCase() + " - " + midNameDes.get(0) + " :- " + hours + " hours "
				+ minutes + " minutes");

		for (int i = 0; i < midNameDes.size() - 1; i++) {
			String[] timePart = midTimeDes.get(i).split(":");
			int hoursNext = Integer.parseInt(timePart[0]);
			int minutesNext = Integer.parseInt(timePart[1]);

			pairs.add(midNameDes.get(i) + " - " + midNameDes.get(i + 1) + " :- " + hoursNext + " hours " + minutesNext
					+ " minutes");
		}

		String[] timePart = midTimeDes.get(midNameDes.size() - 1).split(":");
		int hoursLast = Integer.parseInt(timePart[0]);
		int minutesLast = Integer.parseInt(timePart[1]);
		pairs.add(midNameDes.get(midNameDes.size() - 1) + " - " + routeDTO.getOrigin().toUpperCase() + " :- "
				+ hoursLast + " hours " + minutesLast + " minutes");

		for (int i = 0; i < pairs.size(); i++) {
			Paragraph midDetails = new Paragraph();
			midDetails.add(pairs.get(i));
			pdf.add(midDetails);
		}
		pdf.add(Chunk.NEWLINE);
	}

	public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
		SetUpMidPointsDTO details = setUpMidPointsService.getDetails(selectedRouteNumber, selectedServiceType);
		Document pdf = (Document) document;
		pdf.open();
		pdf.setPageSize(PageSize.A4);
		pdf.setMargins(12.7f, 12.7f, 12.7f, 12.7f);

		Paragraph title = new Paragraph(routeDTO.getOrigin().toUpperCase() + " - "
				+ routeDTO.getDestination().toUpperCase() + " - Route No(" + selectedRouteNumber + ")");
		Paragraph title1 = new Paragraph(selectedServiceType);
		title.setAlignment(Element.ALIGN_CENTER);
		title1.setAlignment(Element.ALIGN_CENTER);

		pdf.add(title);
		pdf.add(title1);
		pdf.add(Chunk.NEWLINE);

		Paragraph details1 = new Paragraph("Distance : " + details.getLength() + " km");
		Paragraph details2 = new Paragraph("Schedule : " + details.getTime() + " h");
		Paragraph details3 = new Paragraph("Speed : " + details.getSpeed() + " kmph");

		pdf.add(details1);
		pdf.add(details2);
		pdf.add(details3);
		pdf.add(Chunk.NEWLINE);

		List<String> pairs = new ArrayList<>();

		String[] timeParts = midTimeOri.get(0).split(":");
		int hours = Integer.parseInt(timeParts[0]);
		int minutes = Integer.parseInt(timeParts[1]);

		pairs.add(routeDTO.getOrigin().toUpperCase() + " - " + midNameOri.get(0) + " :- " + hours + " hours " + minutes
				+ " minutes");

		for (int i = 0; i < midNameOri.size() - 1; i++) {
			String[] timePart = midTimeOri.get(i).split(":");
			int hoursNext = Integer.parseInt(timePart[0]);
			int minutesNext = Integer.parseInt(timePart[1]);

			pairs.add(midNameOri.get(i) + " - " + midNameOri.get(i + 1) + " :- " + hoursNext + " hours " + minutesNext
					+ " minutes");
		}

		String[] timePart = midTimeOri.get(midNameOri.size() - 1).split(":");
		int hoursLast = Integer.parseInt(timePart[0]);
		int minutesLast = Integer.parseInt(timePart[1]);
		pairs.add(midNameOri.get(midNameOri.size() - 1) + " - " + routeDTO.getDestination().toUpperCase() + " :- "
				+ hoursLast + " hours " + minutesLast + " minutes");

		for (int i = 0; i < pairs.size(); i++) {
			Paragraph midDetails = new Paragraph();
			midDetails.add(pairs.get(i));
			pdf.add(midDetails);
		}
		pdf.add(Chunk.NEWLINE);
	}

	public StreamedContent panelGenerateAction() {
		files = null;

		Connection conn = null;
		try {
			// Obtain a database connection (you need to implement ConnectionManager)
			conn = ConnectionManager.getConnection();

			// Load the JasperReport from the JRXML file
			String sourceFileName = "..//reports//SetUpMidPoints.jrxml";
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			// Set parameters for the report
			HashMap<String, Object> parameters = new HashMap<>();
			TimeTableDTO dto = timeTableService.getDetailsForReport(selectedRouteNumber,selectedServiceType);
			parameters.put("routeDesSinhala", dto.getDescription());
			parameters.put("routeNo", selectedRouteNumber);
			parameters.put("serviceType", selectedServiceType);

			// Create a JasperPrint object by filling the report with data from the database
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			// Export the report to PDF as a byte array
			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);

			// Stream the PDF directly to the user for download
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "application/pdf", "SetUpMidPoints.pdf");

			// Store the report bytes and document type in the session map if needed
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			// Print the generated PDF byte array (for demonstration purposes)
			System.out.println("Generated PDF byte array: " + Arrays.toString(pdfByteArray));

		} catch (JRException e) {
			e.printStackTrace();
			System.err.println("Error generating report: " + e.getMessage());
		} finally {
			ConnectionManager.close(conn); // Make sure to close the connection
		}

		return files;
	}

	// Getters And Setters

	public SetUpMidPointsDTO setSetUpMidPointsDTO() {
		return setUpMidPointsDTO;
	}

	public SetUpMidPointsDTO getSetUpMidPointsDTO() {
		return setUpMidPointsDTO;
	}

	public void setSetUpMidPointsDTO(SetUpMidPointsDTO setUpMidPointsDTO) {
		this.setUpMidPointsDTO = setUpMidPointsDTO;
	}

	public List<SetUpMidPointsDTO> setRouteList() {
		return routeList;
	}

	public List<SetUpMidPointsDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<SetUpMidPointsDTO> routeList) {
		this.routeList = routeList;
	}

	public SetUpMidPointsDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(SetUpMidPointsDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<SetUpMidPointsDTO> getServiceType() {
		return serviceType;
	}

	public void setServiceType(List<SetUpMidPointsDTO> serviceType) {
		this.serviceType = serviceType;
	}

	public List<SetUpMidPointsDTO> getOrgToDesMidPointList() {
		return orgToDesMidPointList;
	}

	public void setOrgToDesMidPointList(List<SetUpMidPointsDTO> orgToDesMidPointList) {
		this.orgToDesMidPointList = orgToDesMidPointList;
	}

	public List<SetUpMidPointsDTO> getDesToOrgMidPointList() {
		return desToOrgMidPointList;
	}

	public void setDesToOrgMidPointList(List<SetUpMidPointsDTO> desToOrgMidPointList) {
		this.desToOrgMidPointList = desToOrgMidPointList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<TimeTableDTO> getMidList() {
		return midList;
	}

	public void setMidList(List<TimeTableDTO> midList) {
		this.midList = midList;
	}

	public SetUpMidPointsDTO getMidData() {
		return midData;
	}

	public void setMidData(SetUpMidPointsDTO midData) {
		this.midData = midData;
	}

	public String getSelectedRouteNumber() {
		return selectedRouteNumber;
	}

	public void setSelectedRouteNumber(String selectedRouteNumber) {
		this.selectedRouteNumber = selectedRouteNumber;
	}

	public String getSelectedServiceType() {
		return selectedServiceType;
	}

	public void setSelectedServiceType(String selectedServiceType) {
		this.selectedServiceType = selectedServiceType;
	}

	public boolean isTickingOption() {
		return tickingOption;
	}

	public void setTickingOption(boolean tickingOption) {
		this.tickingOption = tickingOption;
	}

	public List<BusDetailsDTO> getBusDetailsList() {
		return busDetailsList;
	}

	public void setBusDetailsList(List<BusDetailsDTO> busDetailsList) {
		this.busDetailsList = busDetailsList;
	}

	public List<BusDetailsDTO> getBusDetailsListDesToOrg() {
		return busDetailsListDesToOrg;
	}

	public void setBusDetailsListDesToOrg(List<BusDetailsDTO> busDetailsListDesToOrg) {
		this.busDetailsListDesToOrg = busDetailsListDesToOrg;
	}

	public List<Map<String, List<Map<String, String>>>> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<Map<String, List<Map<String, String>>>> timeList) {
		this.timeList = timeList;
	}

	public List<Map<String, List<Map<String, String>>>> getTimeListDes() {
		return TimeListDes;
	}

	public void setTimeListDes(List<Map<String, List<Map<String, String>>>> timeListDes) {
		TimeListDes = timeListDes;
	}

	public List<SetUpMidPointsDTO> getBusNoList() {
		return busNoList;
	}

	public void setBusNoList(List<SetUpMidPointsDTO> busNoList) {
		this.busNoList = busNoList;
	}

	public List<SetUpMidPointsDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<SetUpMidPointsDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<Column> getGeneratedColumns() {
		return generatedColumns;
	}

	public void setGeneratedColumns(List<Column> generatedColumns) {
		this.generatedColumns = generatedColumns;
	}

	public List<Column> getGeneratedColumns2() {
		return generatedColumns2;
	}

	public void setGeneratedColumns2(List<Column> generatedColumns2) {
		this.generatedColumns2 = generatedColumns2;
	}

	public SetUpMidPointsDTO getDescript() {
		return descript;
	}

	public void setDescript(SetUpMidPointsDTO descript) {
		this.descript = descript;
	}

	public List<SetUpMidPointsDTO> getStartTimeList() {
		return startTimeList;
	}

	public void setStartTimeList(List<SetUpMidPointsDTO> startTimeList) {
		this.startTimeList = startTimeList;
	}

	public List<SetUpMidPointsDTO> getMidPointNameList() {
		return midPointNameList;
	}

	public void setMidPointNameList(List<SetUpMidPointsDTO> midPointNameList) {
		this.midPointNameList = midPointNameList;
	}

	public List<SetUpMidPointsDTO> getMidPointTimeList() {
		return midPointTimeList;
	}

	public void setMidPointTimeList(List<SetUpMidPointsDTO> midPointTimeList) {
		this.midPointTimeList = midPointTimeList;
	}

	public String getStationTime() {
		return stationTime;
	}

	public void setStationTime(String stationTime) {
		this.stationTime = stationTime;
	}

	public List<String> getStationTimes() {
		return stationTimes;
	}

	public void setStationTimes(List<String> stationTimes) {
		this.stationTimes = stationTimes;
	}

	public List<SetUpMidPointsDTO> getStartTimeListtt() {
		return startTimeListtt;
	}

	public void setStartTimeListtt(List<SetUpMidPointsDTO> startTimeListtt) {
		this.startTimeListtt = startTimeListtt;
	}

	public List<BusDetailsDTO> getPermitNumList() {
		return permitNumList;
	}

	public void setPermitNumList(List<BusDetailsDTO> permitNumList) {
		this.permitNumList = permitNumList;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public List<SetUpMidPointsDTO> getSelectedOrgToDesMidPointList() {
		return selectedOrgToDesMidPointList;
	}

	public void setSelectedOrgToDesMidPointList(List<SetUpMidPointsDTO> selectedOrgToDesMidPointList) {
		this.selectedOrgToDesMidPointList = selectedOrgToDesMidPointList;
	}

	public List<SetUpMidPointsDTO> getSelectedDesToOrgMidPointList() {
		return selectedDesToOrgMidPointList;
	}

	public void setSelectedDesToOrgMidPointList(List<SetUpMidPointsDTO> selectedDesToOrgMidPointList) {
		this.selectedDesToOrgMidPointList = selectedDesToOrgMidPointList;
	}

	public boolean isVisibleColumn() {
		return visibleColumn;
	}

	public void setVisibleColumn(boolean visibleColumn) {
		this.visibleColumn = visibleColumn;
	}

	public boolean isDisableDropDown() {
		return disableDropDown;
	}

	public void setDisableDropDown(boolean disableDropDown) {
		this.disableDropDown = disableDropDown;
	}

	public List<BusDetailsDTO> getSelectedDesToOrgBusDetailsList() {
		return selectedDesToOrgBusDetailsList;
	}

	public void setSelectedDesToOrgBusDetailsList(List<BusDetailsDTO> selectedDesToOrgBusDetailsList) {
		this.selectedDesToOrgBusDetailsList = selectedDesToOrgBusDetailsList;
	}

	public boolean isSaveClicked() {
		return saveClicked;
	}

	public void setSaveClicked(boolean saveClicked) {
		this.saveClicked = saveClicked;
	}

	public boolean isDisableButton() {
		return disableButton;
	}

	public void setDisableButton(boolean disableButton) {
		this.disableButton = disableButton;
	}

	public SetUpMidPointsService getSetUpMidPointsService() {
		return setUpMidPointsService;
	}

	public void setSetUpMidPointsService(SetUpMidPointsService setUpMidPointsService) {
		this.setUpMidPointsService = setUpMidPointsService;
	}

	public List<BusDetailsDTO> getSelectedOrgToDesBusDetailsList() {
		return selectedOrgToDesBusDetailsList;
	}

	public void setSelectedOrgToDesBusDetailsList(List<BusDetailsDTO> selectedOrgToDesBusDetailsList) {
		this.selectedOrgToDesBusDetailsList = selectedOrgToDesBusDetailsList;
	}

	public List<BusDetailsDTO> getSelectedAllOrgToDesBusDetailsList() {
		return selectedAllOrgToDesBusDetailsList;
	}

	public void setSelectedAllOrgToDesBusDetailsList(List<BusDetailsDTO> selectedAllOrgToDesBusDetailsList) {
		this.selectedAllOrgToDesBusDetailsList = selectedAllOrgToDesBusDetailsList;
	}

	public List<BusDetailsDTO> getSelectedAllDesToOrgBusDetailsList() {
		return selectedAllDesToOrgBusDetailsList;
	}

	public void setSelectedAllDesToOrgBusDetailsList(List<BusDetailsDTO> selectedAllDesToOrgBusDetailsList) {
		this.selectedAllDesToOrgBusDetailsList = selectedAllDesToOrgBusDetailsList;
	}

	public TimeTableService getTimeTableService() {
		return timeTableService;
	}

	public void setTimeTableService(TimeTableService timeTableService) {
		this.timeTableService = timeTableService;
	}

	public List<SetUpMidPointsDTO> getAbbreviationListOrigin() {
		return abbreviationListOrigin;
	}

	public void setAbbreviationListOrigin(List<SetUpMidPointsDTO> abbreviationListOrigin) {
		this.abbreviationListOrigin = abbreviationListOrigin;
	}

	public List<SetUpMidPointsDTO> getAbbreviationListDestination() {
		return abbreviationListDestination;
	}

	public void setAbbreviationListDestination(List<SetUpMidPointsDTO> abbreviationListDestination) {
		this.abbreviationListDestination = abbreviationListDestination;
	}

	public List<String> getMidName() {
		return midNameOri;
	}

	public void setMidName(List<String> midName) {
		this.midNameOri = midName;
	}

	public List<String> getMidTime() {
		return midTimeOri;
	}

	public void setMidTime(List<String> midTime) {
		this.midTimeOri = midTime;
	}

	public List<String> getMidNameDes() {
		return midNameDes;
	}

	public void setMidNameDes(List<String> midNameDes) {
		this.midNameDes = midNameDes;
	}

	public List<String> getMidTimeDes() {
		return midTimeDes;
	}

	public void setMidTimeDes(List<String> midTimeDes) {
		this.midTimeDes = midTimeDes;
	}

	public boolean isSaveButton() {
		return saveButton;
	}

	public void setSaveButton(boolean saveButton) {
		this.saveButton = saveButton;
	}

	public List<BusDetailsDTO> getCombineList() {
		return combineList;
	}

	public void setCombineList(List<BusDetailsDTO> combineList) {
		this.combineList = combineList;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

}
