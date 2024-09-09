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
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.FareOfSemiLuxuryReportDTO;
import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
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

@ManagedBean(name = "fareOfSemiLuxuryReportBackingBean")
@ViewScoped
public class FareOfSemiLuxuryReportBackingBean {
	private List<String> routeList = new ArrayList<String>();
	private List<LogSheetMaintenanceDTO> serviceList = new ArrayList<LogSheetMaintenanceDTO>();
	private String route;
	private String language;
	private String serviceType;
	private ReportService reportService;
	private StreamedContent files;
	private List<Integer> stagesList = new ArrayList<>();
	private List<Integer> stagesListTwo = new ArrayList<>();
	private List<StationDetailsDTO> stagesListTemp = new ArrayList<>();
	private List<StationDetailsDTO> semiFeeList = new ArrayList<>();
	private StationDetailsDTO semiFeeListDTO = new StationDetailsDTO();
	private StationDetailsDTO checkServiceDTO;
	private List<StationDetailsDTO> stagesListDTO = new ArrayList<>();
	private List<Integer> stagesKeep = new ArrayList<>();
	private String errorMsg;

	@PostConstruct
	public void init() {
		reportService = (ReportService) SpringApplicationContex.getBean("reportService");
		setRouteList(reportService.routeNoDropdown());
		serviceList = reportService.serviceTypeDropDownWithOutNormal();

	}

	public void english() {
		language = "ENG";
	}

	public StreamedContent sinhala() throws JRException {
		language = "SIN";
		files = null;
		checkServiceDTO = reportService.checkServiceTypeForRoute(route, serviceType);
		
		if(route != null && !route.isEmpty() && !route.trim().equalsIgnoreCase("")) {
			if (serviceType != null && !serviceType.isEmpty() && !serviceType.trim().equalsIgnoreCase("")) {
				
				if(checkServiceDTO.getExpressrep()!= null && checkServiceDTO.getExpressrep().equalsIgnoreCase("Y")) {
					
					try {
						RouteDTO routeDto = new RouteDTO();
						Connection conn = ConnectionManager.getConnection();
						
							if (serviceType.equalsIgnoreCase("EB") ) {
								routeDto = reportService.retrieveFareforExpresswayAndSuperLuxuryReport(serviceType, route);
								if (routeDto != null) {
									String sourceFileName = "..//reports//highwayBusServiceFareReport.jrxml";
									String logopath = "//lk//informatics//ntc//view//reports//";
									Map<String, Object> parameters = new HashMap<String, Object>();

									parameters.put("P_ROUTE_NUM", route);
									parameters.put("P_STATUS", routeDto.getOrigin());
									parameters.put("P_STAGE", routeDto.getDestination());
									parameters.put("P_FEE", (routeDto.getBusFare().toString()) + ".00");
									parameters.put("P_FEE_DATE", routeDto.getFareChangedDate());
									parameters.put("P_logo1", logopath);
									parameters.put("P_logo2", logopath);

									JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

									JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

									JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
									jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

									byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
									InputStream stream = new ByteArrayInputStream(pdfByteArray);
									files = new DefaultStreamedContent(stream, "Application/pdf",
											"highwayBusServiceFareReport123.pdf");

									ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
									Map<String, Object> sessionMap = externalContext.getSessionMap();
									sessionMap.put("reportBytes", pdfByteArray);
									sessionMap.put("docType", "pdf");
								}
							}else if (serviceType.equalsIgnoreCase("003")) {
								// super luxuary bus report
								routeDto = reportService.retrieveFareforExpresswayAndSuperLuxuryReport(serviceType, route);
								if (routeDto != null) {
									String sourceFileName = "..//reports//fareofSuperLuxuryBusServices.jrxml";
									String logopath = "//lk//informatics//ntc//view//reports//";
									Map<String, Object> parameters = new HashMap<String, Object>();

									parameters.put("P_RouteNo", route);
									parameters.put("P_Service_Type", serviceType);
									parameters.put("P_logo1", logopath);
									parameters.put("P_logo2", logopath);
									JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

									JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

									JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
									jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

									byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
									InputStream stream = new ByteArrayInputStream(pdfByteArray);
									files = new DefaultStreamedContent(stream, "Application/pdf",
											"luxuaryBusServiceFareReport.pdf");

									ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
									Map<String, Object> sessionMap = externalContext.getSessionMap();
									sessionMap.put("reportBytes", pdfByteArray);
									sessionMap.put("docType", "pdf");
								}
							}else if (serviceType.equalsIgnoreCase("004")) {
								String sourceFileName =null;
								// semi luxuary bus report
								stagesList = reportService.getStagesAccordingToRouteNo(route);
								stagesListTwo = reportService.getStagesAccordingToRouteNo(route);
								stagesListDTO = reportService.getStagesAccordingToRouteNoDTO(route);

								// get semifee against to stages
								// stages and fee in DTO
								semiFeeList = reportService.getsemiFeeAgainstStage(stagesList);

								/** Difference if stages start **/
								List<FareOfSemiLuxuryReportDTO> tempDtoList = new ArrayList<FareOfSemiLuxuryReportDTO>();
								for (int i : stagesList) {
									for (int j : stagesListTwo) {

										FareOfSemiLuxuryReportDTO reportDto = new FareOfSemiLuxuryReportDTO();
										reportDto.setMainStage(Integer.toString(i));
										reportDto.setStage(Integer.toString(j));
										int value = i - j;
										if (value >= 0) {
											reportDto.setDifference(value);

											for (StationDetailsDTO feeDto : semiFeeList) {
												if (Integer.toString(reportDto.getDifference())
														.equalsIgnoreCase(feeDto.getStage())) {
													reportDto.setFee(feeDto.getSemifare());
													tempDtoList.add(reportDto);
												}
											}
										}
									}
								}

								/** Difference if stages end **/

								/** insert data into temp table start **/
								reportService.insertDataIntoNt_temp_stages_amount(tempDtoList, stagesList, stagesListDTO);
								/** insert data into temp table end **/
								List<String> stagesName = new ArrayList<>();
								stagesName = reportService.getStagesNameListFromTempTable(route);
								Map<String, Object> parameters = new HashMap<String, Object>();
								if(stagesList.size()>21) {

								 sourceFileName = "..//reports//FareofSemiLuxuryBusServices.jrxml";
								 parameters.put("P_stage0", stagesName.get(0));
								 parameters.put("P_stage1", stagesName.get(1));
								 parameters.put("P_stage2", stagesName.get(2));
								 parameters.put("P_stage3", stagesName.get(3));
								 parameters.put("P_stage4", stagesName.get(4));
								 parameters.put("P_stage5", stagesName.get(5));
								 parameters.put("P_stage6", stagesName.get(6));
								 parameters.put("P_stage7", stagesName.get(7));
								 parameters.put("P_stage8", stagesName.get(8));
								 parameters.put("P_stage9", stagesName.get(9));
								 parameters.put("P_stage10", stagesName.get(10));
								 parameters.put("P_stage11", stagesName.get(11));
								 parameters.put("P_stage12", stagesName.get(12));
								 parameters.put("P_stage13", stagesName.get(13));
								 parameters.put("P_stage14", stagesName.get(14));
								 parameters.put("P_stage15", stagesName.get(15));
								 parameters.put("P_stage16", stagesName.get(16));
								 parameters.put("P_stage17", stagesName.get(17));
								 parameters.put("P_stage18", stagesName.get(18));
								 parameters.put("P_stage19", stagesName.get(19));
								 parameters.put("P_stage20", stagesName.get(20));;
								 parameters.put("P_stage21", stagesName.get(21));
								 parameters.put("P_stage22", stagesName.get(22));
								 parameters.put("P_stage23", stagesName.get(23));
								 parameters.put("P_stage24", stagesName.get(24));
								 parameters.put("P_stage25", stagesName.get(25));
								 parameters.put("P_stage26", stagesName.get(26));
								 parameters.put("P_stage27", stagesName.get(27));
								 parameters.put("P_stage28", stagesName.get(28));
								 parameters.put("P_stage29", stagesName.get(29));
								 parameters.put("P_stage30", stagesName.get(30));
								 parameters.put("P_stage31", stagesName.get(31));
								 parameters.put("P_stage32", stagesName.get(32));
								 parameters.put("P_stage33", stagesName.get(33));
								 parameters.put("P_stage34", stagesName.get(34));
								 parameters.put("P_stage35", stagesName.get(35));
								 parameters.put("P_stage36", stagesName.get(36));
								 parameters.put("P_stage37", stagesName.get(37));
								 parameters.put("P_stage38", stagesName.get(38));
								 parameters.put("P_stage39", stagesName.get(39));
								 parameters.put("P_stage40", stagesName.get(40));
								}
								if(stagesList.size()<= 21 && stagesList.size() >=11) {

									 sourceFileName = "..//reports//FareofSemiLuxuryBusServices2.jrxml";
									 parameters.put("P_stage0", stagesName.get(0));
									 parameters.put("P_stage1", stagesName.get(1));
									 parameters.put("P_stage2", stagesName.get(2));
									 parameters.put("P_stage3", stagesName.get(3));
									 parameters.put("P_stage4", stagesName.get(4));
									 parameters.put("P_stage5", stagesName.get(5));
									 parameters.put("P_stage6", stagesName.get(6));
									 parameters.put("P_stage7", stagesName.get(7));
									 parameters.put("P_stage8", stagesName.get(8));
									 parameters.put("P_stage9", stagesName.get(9));
									 parameters.put("P_stage10", stagesName.get(10));
									 parameters.put("P_stage11", stagesName.get(11));
									 parameters.put("P_stage12", stagesName.get(12));
									 parameters.put("P_stage13", stagesName.get(13));
									 parameters.put("P_stage14", stagesName.get(14));
									 parameters.put("P_stage15", stagesName.get(15));
									 parameters.put("P_stage16", stagesName.get(16));
									 parameters.put("P_stage17", stagesName.get(17));
									 parameters.put("P_stage18", stagesName.get(18));
									 parameters.put("P_stage19", stagesName.get(19));
									 parameters.put("P_stage20", stagesName.get(20));
									}
								
								if(stagesList.size()<= 11 ) {

									 sourceFileName = "..//reports//FareofSemiLuxuryBusServices3.jrxml";
									 parameters.put("P_stage0", stagesName.get(0));
									 parameters.put("P_stage1", stagesName.get(1));
									 parameters.put("P_stage2", stagesName.get(2));
									 parameters.put("P_stage3", stagesName.get(3));
									 parameters.put("P_stage4", stagesName.get(4));
									 parameters.put("P_stage5", stagesName.get(5));
									 parameters.put("P_stage6", stagesName.get(6));
									 parameters.put("P_stage7", stagesName.get(7));
									 parameters.put("P_stage8", stagesName.get(8));
									 parameters.put("P_stage9", stagesName.get(9));
									 parameters.put("P_stage10", stagesName.get(10));
									}
								String logopath = "//lk//informatics//ntc//view//reports//";
								

								parameters.put("P_ROUTE_NUM", route);
								parameters.put("P_SERVICE_TYPE", serviceType);
								parameters.put("P_logo1", logopath);
								parameters.put("P_logo2", logopath);

								JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

								JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

								JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
								jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

								byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
								InputStream stream = new ByteArrayInputStream(pdfByteArray);
								files = new DefaultStreamedContent(stream, "Application/pdf", "SemiLuxuryBusServiceFareReport.pdf");

								ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
								Map<String, Object> sessionMap = externalContext.getSessionMap();
								sessionMap.put("reportBytes", pdfByteArray);
								sessionMap.put("docType", "pdf");

							}else if (serviceType.equalsIgnoreCase("002")) {

								// luxury bus report
								stagesList = reportService.getStagesAccordingToRouteNoLuxuary(route);
								stagesListTwo = reportService.getStagesAccordingToRouteNoLuxuary(route);
								stagesListDTO = reportService.getStagesAccordingToRouteNoDTOLuxuary(route);

								// get semifee against to stages
								// stages and fee in DTO
								semiFeeList = reportService.getsemiFeeAgainstStageLuxuary(stagesList);

								/** Difference if stages start **/
								List<FareOfSemiLuxuryReportDTO> tempDtoList = new ArrayList<FareOfSemiLuxuryReportDTO>();
								for (int i : stagesList) {
									for (int j : stagesListTwo) {

										FareOfSemiLuxuryReportDTO reportDto = new FareOfSemiLuxuryReportDTO();
										reportDto.setMainStage(Integer.toString(i));
										reportDto.setStage(Integer.toString(j));
										int value = i - j;
										if (value >= 0) {
											reportDto.setDifference(value);

											for (StationDetailsDTO feeDto : semiFeeList) {
												if (Integer.toString(reportDto.getDifference())
														.equalsIgnoreCase(feeDto.getStage())) {
													reportDto.setFee(feeDto.getSemifare());
													tempDtoList.add(reportDto);
												}
											}
										}

									}
								}

								/** Difference if stages end **/

								/** insert data into temp table start **/
								reportService.insertDataIntoNt_temp_stages_amount(tempDtoList, stagesList, stagesListDTO);
								/** insert data into temp table end **/
								String sourceFileName =null;
								String logopath = "//lk//informatics//ntc//view//reports//";
								
								
								
							
									/** insert data into temp table end **/
									List<String> stagesName = new ArrayList<>();
									stagesName = reportService.getStagesNameListFromTempTable(route);
									Map<String, Object> parameters = new HashMap<String, Object>();
									if(stagesList.size()>21) {

										sourceFileName = "..//reports//FareofLuxuryBusServices.jrxml";
									 parameters.put("P_stage0", stagesName.get(0));
									 parameters.put("P_stage1", stagesName.get(1));
									 parameters.put("P_stage2", stagesName.get(2));
									 parameters.put("P_stage3", stagesName.get(3));
									 parameters.put("P_stage4", stagesName.get(4));
									 parameters.put("P_stage5", stagesName.get(5));
									 parameters.put("P_stage6", stagesName.get(6));
									 parameters.put("P_stage7", stagesName.get(7));
									 parameters.put("P_stage8", stagesName.get(8));
									 parameters.put("P_stage9", stagesName.get(9));
									 parameters.put("P_stage10", stagesName.get(10));
									 parameters.put("P_stage11", stagesName.get(11));
									 parameters.put("P_stage12", stagesName.get(12));
									 parameters.put("P_stage13", stagesName.get(13));
									 parameters.put("P_stage14", stagesName.get(14));
									 parameters.put("P_stage15", stagesName.get(15));
									 parameters.put("P_stage16", stagesName.get(16));
									 parameters.put("P_stage17", stagesName.get(17));
									 parameters.put("P_stage18", stagesName.get(18));
									 parameters.put("P_stage19", stagesName.get(19));
									 parameters.put("P_stage20", stagesName.get(20));;
									 parameters.put("P_stage21", stagesName.get(21));
									 parameters.put("P_stage22", stagesName.get(22));
									 parameters.put("P_stage23", stagesName.get(23));
									 parameters.put("P_stage24", stagesName.get(24));
									 parameters.put("P_stage25", stagesName.get(25));
									 parameters.put("P_stage26", stagesName.get(26));
									 parameters.put("P_stage27", stagesName.get(27));
									 parameters.put("P_stage28", stagesName.get(28));
									 parameters.put("P_stage29", stagesName.get(29));
									 parameters.put("P_stage30", stagesName.get(30));
									 parameters.put("P_stage31", stagesName.get(31));
									 parameters.put("P_stage32", stagesName.get(32));
									 parameters.put("P_stage33", stagesName.get(33));
									 parameters.put("P_stage34", stagesName.get(34));
									 parameters.put("P_stage35", stagesName.get(35));
									 parameters.put("P_stage36", stagesName.get(36));
									 parameters.put("P_stage37", stagesName.get(37));
									 parameters.put("P_stage38", stagesName.get(38));
									 parameters.put("P_stage39", stagesName.get(39));
									 parameters.put("P_stage40", stagesName.get(40));
									}
									if(stagesList.size()<= 21 && stagesList.size() >=11) {

										 sourceFileName = "..//reports//FareofLuxuryBusServices2.jrxml";
										 parameters.put("P_stage0", stagesName.get(0));
										 parameters.put("P_stage1", stagesName.get(1));
										 parameters.put("P_stage2", stagesName.get(2));
										 parameters.put("P_stage3", stagesName.get(3));
										 parameters.put("P_stage4", stagesName.get(4));
										 parameters.put("P_stage5", stagesName.get(5));
										 parameters.put("P_stage6", stagesName.get(6));
										 parameters.put("P_stage7", stagesName.get(7));
										 parameters.put("P_stage8", stagesName.get(8));
										 parameters.put("P_stage9", stagesName.get(9));
										 parameters.put("P_stage10", stagesName.get(10));
										 parameters.put("P_stage11", stagesName.get(11));
										 parameters.put("P_stage12", stagesName.get(12));
										 parameters.put("P_stage13", stagesName.get(13));
										 parameters.put("P_stage14", stagesName.get(14));
										 parameters.put("P_stage15", stagesName.get(15));
										 parameters.put("P_stage16", stagesName.get(16));
										 parameters.put("P_stage17", stagesName.get(17));
										 parameters.put("P_stage18", stagesName.get(18));
										 parameters.put("P_stage19", stagesName.get(19));
										 parameters.put("P_stage20", stagesName.get(20));
										}
									
									if(stagesList.size()<= 11 ) {

										 sourceFileName = "..//reports//FareofLuxuryBusServices3.jrxml";
										 parameters.put("P_stage0", stagesName.get(0));
										 parameters.put("P_stage1", stagesName.get(1));
										 parameters.put("P_stage2", stagesName.get(2));
										 parameters.put("P_stage3", stagesName.get(3));
										 parameters.put("P_stage4", stagesName.get(4));
										 parameters.put("P_stage5", stagesName.get(5));
										 parameters.put("P_stage6", stagesName.get(6));
										 parameters.put("P_stage7", stagesName.get(7));
										 parameters.put("P_stage8", stagesName.get(8));
										 parameters.put("P_stage9", stagesName.get(9));
										 parameters.put("P_stage10", stagesName.get(10));
										}
							

								parameters.put("P_ROUTE_NUM", route);
								parameters.put("P_SERVICE_TYPE", serviceType);
								parameters.put("P_logo1", logopath);
								parameters.put("P_logo2", logopath);

								JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

								JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

								JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
								jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

								byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
								InputStream stream = new ByteArrayInputStream(pdfByteArray);
								files = new DefaultStreamedContent(stream, "Application/pdf", "LuxuryBusServiceFareReport.pdf");

								ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
								Map<String, Object> sessionMap = externalContext.getSessionMap();
								sessionMap.put("reportBytes", pdfByteArray);
								sessionMap.put("docType", "pdf");

							}
							reportService.deleteData();
							
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}else {
					setErrorMsg("No data found for selected service type for route.");
					RequestContext.getCurrentInstance().update("errorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				}
			}else {
				setErrorMsg("Please select service type.");
				RequestContext.getCurrentInstance().update("errorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			}
		}else {
			setErrorMsg("Please select route number.");
			RequestContext.getCurrentInstance().update("errorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
		}
		return files;
	}

	public void tamil() {
		language = "TAM";
	}

	public void clear() {
		language = "";
		route = "";
		serviceType = "";
	}

	public StreamedContent getExpressWayBusReport(RouteDTO routeDto) {
		StreamedContent tempFile = null;

		try {
			String sourceFileName = "..//reports//highwayBusServiceFareReport.jrxml";

			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_ROUTE_NUM", route);
			parameters.put("P_STATUS", routeDto.getStatus());
			parameters.put("P_STAGE", routeDto.getOrigin());
			parameters.put("P_FEE", routeDto.getDestination());
			parameters.put("P_FEE_DATE", routeDto.getFareChangedDate());

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
			// jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			tempFile = new DefaultStreamedContent(stream, "Application/pdf", "highwayBusServiceFareReport123.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tempFile;
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public List<String> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<String> routeList) {
		this.routeList = routeList;
	}

	public List<LogSheetMaintenanceDTO> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<LogSheetMaintenanceDTO> serviceList) {
		this.serviceList = serviceList;
	}

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public List<Integer> getStagesList() {
		return stagesList;
	}

	public void setStagesList(List<Integer> stagesList) {
		this.stagesList = stagesList;
	}

	public List<Integer> getStagesListTwo() {
		return stagesListTwo;
	}

	public void setStagesListTwo(List<Integer> stagesListTwo) {
		this.stagesListTwo = stagesListTwo;
	}

	public List<StationDetailsDTO> getStagesListTemp() {
		return stagesListTemp;
	}

	public void setStagesListTemp(List<StationDetailsDTO> stagesListTemp) {
		this.stagesListTemp = stagesListTemp;
	}

	public List<StationDetailsDTO> getStagesListDTO() {
		return stagesListDTO;
	}

	public void setStagesListDTO(List<StationDetailsDTO> stagesListDTO) {
		this.stagesListDTO = stagesListDTO;
	}

	public List<Integer> getStagesKeep() {
		return stagesKeep;
	}

	public void setStagesKeep(List<Integer> stagesKeep) {
		this.stagesKeep = stagesKeep;
	}

	public List<StationDetailsDTO> getSemiFeeList() {
		return semiFeeList;
	}

	public void setSemiFeeList(List<StationDetailsDTO> semiFeeList) {
		this.semiFeeList = semiFeeList;
	}

	public StationDetailsDTO getSemiFeeListDTO() {
		return semiFeeListDTO;
	}

	public void setSemiFeeListDTO(StationDetailsDTO semiFeeListDTO) {
		this.semiFeeListDTO = semiFeeListDTO;
	}

	public StationDetailsDTO getCheckServiceDTO() {
		return checkServiceDTO;
	}

	public void setCheckServiceDTO(StationDetailsDTO checkServiceDTO) {
		this.checkServiceDTO = checkServiceDTO;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
