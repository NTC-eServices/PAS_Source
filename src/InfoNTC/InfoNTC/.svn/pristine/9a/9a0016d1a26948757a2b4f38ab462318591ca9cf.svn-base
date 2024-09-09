package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.SubSidyDTO;
import lk.informatics.ntc.model.service.SubSidyManagementService;
import lk.informatics.ntc.model.service.SubsidyReportsService;
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
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@ManagedBean(name = "subsidyBusesInfoBackingBean")
@ViewScoped
public class SubsidyBusesInfoBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// DTO

	private SubSidyDTO subsidyDTO;

	private List<SubSidyDTO> subsidyServiceTypeList;
	private List<SubSidyDTO> provinceList;
	private List<SubSidyDTO> districtList;

	private List<SubSidyDTO> schoolList;

	// Service
	private SubSidyManagementService subsidyManagmentService;
	private SubsidyReportsService subsidyReportsService;

	private String errorMsg, successMessage;
	private StreamedContent files;
	private boolean paidNonPaidShow, penaltyFeeShow;
	private Date endDate;
	private Date startDate;
	private String privateSltb;

	// for Sisu Sariya Monthly Operated KM Report
	private String selectedYear;
	private String startMonthValue;
	private String endMonthValue;
	private boolean isPrinting = false;


	@PostConstruct
	public void init() {
		subsidyManagmentService = (SubSidyManagementService) SpringApplicationContex
				.getBean("subSidyManagementService");
		subsidyReportsService = (SubsidyReportsService) SpringApplicationContex.getBean("subsidyReportsService");

		subsidyDTO = new SubSidyDTO();
		subsidyServiceTypeList = subsidyManagmentService.getServiceType();
		provinceList = subsidyReportsService.getprovinceList();
		districtList = subsidyReportsService.getDistrictList();
		schoolList = subsidyReportsService.getSchoolNamesList();
		paidNonPaidShow = false;
		penaltyFeeShow = false;
	}

	public void getDistrictForProvince() {

		districtList = subsidyReportsService.getDistrictByProvince(subsidyDTO.getProvinceCode());
	}

	// get School names against province
	public void getSchoolForProvince() {
		schoolList = subsidyReportsService.getSchoolByProvince(subsidyDTO.getProvinceCode());

	}

	// get school names against district
	public void getSchoolForDistrict() {
		schoolList = subsidyReportsService.getSchoolByDistrict(subsidyDTO.getDistrictCode());

	}

	public void clearField() {
		subsidyDTO = new SubSidyDTO();
		startDate = null;
		endDate = null;
	}

	// show items accoring to report type in sisusariya running percentage report
	public void showSubItems() {
		if (subsidyDTO.getRptType().equalsIgnoreCase("PayType")) {

			paidNonPaidShow = true;
			penaltyFeeShow = false;
		}
		if (subsidyDTO.getRptType().equalsIgnoreCase("PenaltyType")) {
			paidNonPaidShow = false;
			penaltyFeeShow = true;
		}
		if (subsidyDTO.getRptType().equalsIgnoreCase("cancelType")
				|| subsidyDTO.getRptType().equalsIgnoreCase("logSheetNotIssue")
				|| subsidyDTO.getRptType().equalsIgnoreCase("notApproved")) {
			paidNonPaidShow = false;
			penaltyFeeShow = false;
		}

	}
	// get subsidy bus Information Report
	public StreamedContent genSubBusInfoRepo(ActionEvent ae) throws JRException {

		if (subsidyDTO.getServiceCode().equals(null) || subsidyDTO.getServiceCode().trim().isEmpty()) {
			setErrorMsg("Please select a service type.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

			files = null;
			String sourceFileName = null;
			Connection conn = null;

			try {
				conn = ConnectionManager.getConnection();
				if (subsidyDTO.getServiceCode().equals("S01")) {
					sourceFileName = "..//reports//subsidyBusInformation.jrxml";
				} else if (subsidyDTO.getServiceCode().equals("S02")) {
					sourceFileName = "..//reports//subsidyGamiBusInformation.jrxml";
				} else if (subsidyDTO.getServiceCode().equals("S03")) {
					sourceFileName = "..//reports//subsidyNisiBusInformation.jrxml";
				}
				String logopath = "//lk//informatics//ntc//view//reports//";
				Map<String, Object> parameters = new HashMap<String, Object>();

				if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
					parameters.put("P_Province", null);
				} else {
					parameters.put("P_Province", subsidyDTO.getProvinceCode());
				}
				if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
					parameters.put("P_District", null);
				} else {
					parameters.put("P_District", subsidyDTO.getDistrictCode());
				}
				parameters.put("P_ntc_logo", logopath);
				parameters.put("P_national_logo", logopath);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				
				if (jasperPrint.getPages().isEmpty()) {
		            setErrorMsg("No data was found on the selected criteria");
		        	RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		            return null; 
		        }else {
		        	jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
					jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

					byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					files = new DefaultStreamedContent(stream, "Application/pdf", "Subsidy Bus Info.pdf");
					RequestContext.getCurrentInstance().execute("window.open('/InfoNTC/showUpload?reportName=Bus Info Report');");
					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "pdf");
		        }
				
				

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionManager.close(conn);
			}

		}
		return files;
	}

	// get Subsiy Bus Info Report in excel formal
	public void generateSubsidyBusInfoExcel() {

		if (subsidyDTO.getServiceCode().equals(null) || subsidyDTO.getServiceCode().trim().isEmpty()) {
			setErrorMsg("Please select a service type.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

			files = null;
			String sourceFileName = null;

			Connection conn = null;

			try {
				conn = ConnectionManager.getConnection();
				if (subsidyDTO.getServiceCode().equals("S01")) {
					sourceFileName = "..//reports//subsidyBusInformation.jrxml";
				} else if (subsidyDTO.getServiceCode().equals("S02")) {
					sourceFileName = "..//reports//subsidyGamiBusInformation.jrxml";
				} else if (subsidyDTO.getServiceCode().equals("S03")) {
					sourceFileName = "..//reports//subsidyNisiBusInformation.jrxml";
				}

				String logopath = "//lk//informatics//ntc//view//reports//";
				Map<String, Object> parameters = new HashMap<String, Object>();

				if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
					parameters.put("P_Province", null);
				} else {
					parameters.put("P_Province", subsidyDTO.getProvinceCode());
				}
				if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
					parameters.put("P_District", null);
				} else {
					parameters.put("P_District", subsidyDTO.getDistrictCode());
				}
				parameters.put("P_ntc_logo", logopath);
				parameters.put("P_national_logo", logopath);

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
				

				if (jasperPrint.getPages().isEmpty()) {
		            setErrorMsg("No data was found on the selected criteria");
		        	RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		            return; 
		        }else {
		        	jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
					jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					JRXlsxExporter exporter = new JRXlsxExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
					exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
					SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
					configuration.setForcePageBreaks(false);
					configuration.setRemoveEmptySpaceBetweenRows(true);
					configuration.setRemoveEmptySpaceBetweenColumns(true);
					exporter.setConfiguration(configuration);
					exporter.exportReport();

					byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					files = new DefaultStreamedContent(stream, "Application/xlsx", "SisuSraiya Bus Info.Report.xlsx");
					RequestContext.getCurrentInstance().execute("PF('genReport').hide();");

					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "xlsx");
		        }
				

			} catch (Exception e) {

			} finally {
				ConnectionManager.close(conn);
			}
		}
	}
	// get Sisu Sraiya School Wise bus information Report

	public StreamedContent genRepoSisuSchlWiseBusInfo(ActionEvent ae) throws JRException {

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//sisuSariyaSchoolWiseBusInfor.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if (subsidyDTO.getSchool().trim().isEmpty()) {
				parameters.put("P_school", null);
			} else {
				parameters.put("P_school", subsidyDTO.getSchool());
			}
			if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
				parameters.put("P_province_code", null);
			} else {
				parameters.put("P_province_code", subsidyDTO.getProvinceCode());
			}
			if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
				parameters.put("P_district_code", null);

			} else {
				parameters.put("P_district_code", subsidyDTO.getDistrictCode());

			}
			parameters.put("P_ntc_logo", logopath);
			parameters.put("P_national_logo", logopath);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			
			if (jasperPrint.getPages().isEmpty()) {
	            setErrorMsg("No data was found on the selected criteria");
	        	RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	            return null; 
	        }else {
	        	jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "Sisu Sariya.pdf");

				RequestContext.getCurrentInstance().execute("window.open('/InfoNTC/showUpload?reportName=School Wise Bus Info Report');");
				
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "pdf");
	        }
			
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

	}

	public void generateReportExcel() {

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//sisuSariyaSchoolWiseBusInfor.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if (subsidyDTO.getSchool().trim().isEmpty()) {
				parameters.put("P_school", null);
			} else {
				parameters.put("P_school", subsidyDTO.getSchool());
			}
			if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
				parameters.put("P_province_code", null);
			} else {
				parameters.put("P_province_code", subsidyDTO.getProvinceCode());
			}
			if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
				parameters.put("P_district_code", null);

			} else {
				parameters.put("P_district_code", subsidyDTO.getDistrictCode());

			}
			parameters.put("P_ntc_logo", logopath);
			parameters.put("P_national_logo", logopath);

			// excel report
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			
			if (jasperPrint.getPages().isEmpty()) {
	            setErrorMsg("No data was found on the selected criteria");
	        	RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	            return; 
	        }else {
	        	jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
				jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				JRXlsxExporter exporter = new JRXlsxExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
				SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
				configuration.setForcePageBreaks(false);
				configuration.setRemoveEmptySpaceBetweenRows(true);
				configuration.setRemoveEmptySpaceBetweenColumns(true);
				exporter.setConfiguration(configuration);
				exporter.exportReport();

				byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/xlsx", "SisuSraiya Bus Info.Report.xlsx");
				RequestContext.getCurrentInstance().execute("PF('genReport').hide();");

				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("reportBytes", pdfByteArray);
				sessionMap.put("docType", "xlsx");
	        }
			
			

		} catch (Exception e) {

		} finally {
			ConnectionManager.close(conn);
		}

		;

	}

	// get payment report
	public StreamedContent generatePaymentReport(ActionEvent ae) throws JRException {
	    isPrinting = true;
	    Connection conn = null;
	    
	    try {
	        if (selectedYear == null || selectedYear.trim().isEmpty() || startMonthValue == null
	                || startMonthValue.trim().isEmpty() || endMonthValue == null || endMonthValue.trim().isEmpty()) {
	            setErrorMsg("Please Fill Mandatory Data");
	            RequestContext.getCurrentInstance().update("requiredField");
	            RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	            return null;
	        }

	        if (Integer.parseInt(startMonthValue) > Integer.parseInt(endMonthValue)) {
	            setErrorMsg("The end month must be later than the starting month.");
	            RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	            return null;
	        }

	        files = null;
	        String sourceFileName;
	        
	        if (subsidyDTO.getServiceCode() == null || subsidyDTO.getServiceCode().trim().isEmpty()) {
	            setErrorMsg("Please select a service type.");
	            RequestContext.getCurrentInstance().update("requiredField");
	            RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	            return null;
	        }

	        try {
	            conn = ConnectionManager.getConnection();
	            sourceFileName = subsidyDTO.getServiceCode().equals("S02") ?
	                             "..//reports//subsidyPaymentRepoGami.jrxml" :
	                             "..//reports//subsidyPaymentRepoSisujrxml.jrxml";
	            
	            String logopath = "//lk//informatics//ntc//view//reports//";
	            Map<String, Object> parameters = new HashMap<>();

	            parameters.put("P_subsidy_type", subsidyDTO.getServiceCode());
	            parameters.put("P_start_month_value", Integer.parseInt(startMonthValue));
	            parameters.put("P_end_month_value", Integer.parseInt(endMonthValue));
	            parameters.put("P_selected_year", selectedYear);
	            parameters.put("P_date_range", setDateRangeParameter(Integer.parseInt(startMonthValue), Integer.parseInt(endMonthValue)));

	            String provinceCode = subsidyDTO.getProvinceCode().isEmpty() ? null : subsidyDTO.getProvinceCode();
	            String provinceDes = subsidyDTO.getProvinceCode().isEmpty() ? null : subsidyDTO.getProvinceDes();
	            parameters.put("P_province_code", provinceCode);
	            parameters.put("P_province_des", provinceDes);

	            String districtCode = subsidyDTO.getDistrictCode().isEmpty() ? null : subsidyDTO.getDistrictCode();
	            String districtDes = subsidyDTO.getDistrictCode().isEmpty() ? null : subsidyDTO.getDistrictDes();
	            parameters.put("P_district_code", districtCode);
	            parameters.put("P_district_des", districtDes);

	            parameters.put("P_ntc_logo", logopath);
	            parameters.put("P_national_logo", logopath);
	            parameters.put("P_is_sltb", subsidyDTO.getPrivateSltb().equalsIgnoreCase("S") ? "Y" :
	                                               subsidyDTO.getPrivateSltb().equalsIgnoreCase("P") ? "N" : null);

	            JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));
	            JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);
	            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

	            if (jasperPrint.getPages().isEmpty()) {
	                setErrorMsg("No data was found on the selected criteria");
	                RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	                return null;
	            }

	            jasperPrint = UnicodeShaper.shapeUp(jasperPrint); // Not sure why this is called twice
	            byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
	            InputStream stream = new ByteArrayInputStream(pdfByteArray);
	            files = new DefaultStreamedContent(stream, "application/pdf", "SubsidyPayment.pdf");
	            
	            RequestContext.getCurrentInstance().execute("window.open('/InfoNTC/showUpload?reportName=Payment PDF Report');");
	            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	            Map<String, Object> sessionMap = externalContext.getSessionMap();
	            sessionMap.put("reportBytes", pdfByteArray);
	            sessionMap.put("docType", "pdf");
	            
	        } catch (NumberFormatException e) {
	            setErrorMsg("Invalid month format.");
	            RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	        } catch (Exception e) {
	            setErrorMsg("An unexpected error occurred.");
	            RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	        } finally {
	            if (conn != null) {
	                ConnectionManager.close(conn);
	            }
	        }
	    } finally {
	        isPrinting = false;
	    }
	    
	    return files;
	}

//	public StreamedContent generatePaymentReport(ActionEvent ae) throws JRException {
//		isPrinting = true;
//		try {
//			if (selectedYear == null || selectedYear.trim().isEmpty() || startMonthValue == null
//					|| startMonthValue.trim().isEmpty() || endMonthValue == null || endMonthValue.trim().isEmpty()) {
//				setErrorMsg("Please Fill Mandatory Data");
//				RequestContext.getCurrentInstance().update("requiredField");
//				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//				return null;
//			} else {
//
//				if (Integer.parseInt(startMonthValue) > Integer.parseInt(endMonthValue)) {
//					setErrorMsg("The end month must be later than the starting month.");
//					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//					return null;
//				} else {
//					files = null;
//					String sourceFileName = null;
//
//					Connection conn = null;
//					if (subsidyDTO.getServiceCode().equals(null) || subsidyDTO.getServiceCode().trim().isEmpty()) {
//						setErrorMsg("Please select a service type.");
//						RequestContext.getCurrentInstance().update("requiredField");
//						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//					} else {
//
//						try {
//							conn = ConnectionManager.getConnection();
//							if (subsidyDTO.getServiceCode().equals("S02")) {
//								sourceFileName = "..//reports//subsidyPaymentRepoGami.jrxml";
//							} else {
//								sourceFileName = "..//reports//subsidyPaymentRepoSisujrxml.jrxml";
//							}
//
//							String logopath = "//lk//informatics//ntc//view//reports//";
//							Map<String, Object> parameters = new HashMap<String, Object>();
//
//							parameters.put("P_subsidy_type", subsidyDTO.getServiceCode());
//							parameters.put("P_start_month_value", Integer.parseInt(startMonthValue));
//							parameters.put("P_end_month_value", Integer.parseInt(endMonthValue));
//							parameters.put("P_selected_year", selectedYear);
//							parameters.put("P_date_range", setDateRangeParameter(Integer.parseInt(startMonthValue), Integer.parseInt(endMonthValue)));
//
//							if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
//								parameters.put("P_province_code", null);
//								parameters.put("P_province_des", null);
//							} else {
//								parameters.put("P_province_code", subsidyDTO.getProvinceCode());
//								parameters.put("P_province_des", subsidyDTO.getProvinceDes());
//							}
//							if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
//								parameters.put("P_district_code", null);
//								parameters.put("P_district_des", null);
//
//							} else {
//								parameters.put("P_district_code", subsidyDTO.getDistrictCode());
//								parameters.put("P_district_des", subsidyDTO.getDistrictDes());
//
//							}
//							parameters.put("P_ntc_logo", logopath);
//							parameters.put("P_national_logo", logopath);
//							if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")) {
//								parameters.put("P_is_sltb", "N");
//							} else if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
//								parameters.put("P_is_sltb", "Y");
//							} else if (!subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")
//									&& !subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
//								parameters.put("P_is_sltb", null);
//							}
//
//							JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));
//
//							JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);
//
//							JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
//
//							if (jasperPrint.getPages().isEmpty()) {
//								setErrorMsg("No data was found on the selected criteria");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//								return null;
//							} else {
//								jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
//								jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
//
//								byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
//								InputStream stream = new ByteArrayInputStream(pdfByteArray);
//								files = new DefaultStreamedContent(stream, "Application/pdf", "SubsidyPAyment.pdf");
//								RequestContext.getCurrentInstance()
//										.execute("window.open('/InfoNTC/showUpload?reportName=Payment PDF Report');");
//								ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//								Map<String, Object> sessionMap = externalContext.getSessionMap();
//								sessionMap.put("reportBytes", pdfByteArray);
//								sessionMap.put("docType", "pdf");
//							}
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						} finally {
//							ConnectionManager.close(conn);
//						}
//
//					}
//					return files;
//				}
//
//			}
//
//		}finally {
//	        isPrinting = false; // Set isPrinting to false after executing the method
//	    }
//		
//	}

	// payemnt report excel
	public void generatePAymentRepoExcel() {

		if (subsidyDTO.getServiceCode().equals(null) || subsidyDTO.getServiceCode().trim().isEmpty()) {
			setErrorMsg("Please select a service type.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

			if (selectedYear == null || selectedYear.trim().isEmpty() || startMonthValue == null
					|| startMonthValue.trim().isEmpty() || endMonthValue == null || endMonthValue.trim().isEmpty()) {
				setErrorMsg("Please Fill Mandatory Data");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else {

				if (Integer.parseInt(startMonthValue) > Integer.parseInt(endMonthValue)) {
					setErrorMsg("The end month must be later than the starting month.");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				} else {
					files = null;
					String sourceFileName = null;

					Connection conn = null;

					try {
						conn = ConnectionManager.getConnection();

						if (subsidyDTO.getServiceCode().equals("S02")) {
							sourceFileName = "..//reports//subsidyPaymentRepoGami.jrxml";
						} else {
							sourceFileName = "..//reports//subsidyPaymentRepoSisujrxml.jrxml";
						}
						String logopath = "//lk//informatics//ntc//view//reports//";
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("P_subsidy_type", subsidyDTO.getServiceCode());
						parameters.put("P_start_month_value", Integer.parseInt(startMonthValue));
						parameters.put("P_end_month_value", Integer.parseInt(endMonthValue));
						parameters.put("P_selected_year", selectedYear);
						parameters.put("P_date_range", setDateRangeParameter(Integer.parseInt(startMonthValue), Integer.parseInt(endMonthValue)));
						parameters.put("P_subsidy_type", subsidyDTO.getServiceCode());

						if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
							parameters.put("P_province_code", null);
						} else {
							parameters.put("P_province_code", subsidyDTO.getProvinceCode());
						}
						if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
							parameters.put("P_district_code", null);

						} else {
							parameters.put("P_district_code", subsidyDTO.getDistrictCode());

						}
						parameters.put("P_ntc_logo", logopath);
						parameters.put("P_national_logo", logopath);
						if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")) {
							parameters.put("P_is_sltb", "N");
						} else if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", "Y");
						} else if (!subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")
								&& !subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", null);
						}
						
						JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

						JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

						if (jasperPrint.getPages().isEmpty()) {
							setErrorMsg("No data was found on the selected criteria");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							return;
						} else {
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

							ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
							JRXlsxExporter exporter = new JRXlsxExporter();
							exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
							exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
							SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
							configuration.setForcePageBreaks(false);
							configuration.setRemoveEmptySpaceBetweenRows(true);
							configuration.setRemoveEmptySpaceBetweenColumns(true);
							exporter.setConfiguration(configuration);
							exporter.exportReport();

							byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
							InputStream stream = new ByteArrayInputStream(pdfByteArray);
							files = new DefaultStreamedContent(stream, "Application/xlsx",
									"SubsisyPayemnt.Report.xlsx");
							RequestContext.getCurrentInstance()
									.execute("window.open('/InfoNTC/showUpload?reportName=Payment Excel Report');");

							ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
							Map<String, Object> sessionMap = externalContext.getSessionMap();
							sessionMap.put("reportBytes", pdfByteArray);
							sessionMap.put("docType", "xlsx");
						}

					} catch (Exception e) {

					} finally {
						ConnectionManager.close(conn);
					}
				}

			}

		}
	}

	// running percentage report
	public StreamedContent genRunPerPaidReport(ActionEvent ae) throws JRException {

		if (selectedYear == null || selectedYear.trim().isEmpty() || startMonthValue == null
				|| startMonthValue.trim().isEmpty() || endMonthValue == null || endMonthValue.trim().isEmpty()) {
			setErrorMsg("Please Fill Mandatory Data");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return null;
		} else {
			if (Integer.parseInt(startMonthValue) > Integer.parseInt(endMonthValue)) {
				setErrorMsg("The end month must be later than the starting month.");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return null;
			} else {
				files = null;
				String sourceFileName = null;

				Connection conn = null;
				if (subsidyDTO.getRptType().equals(null) || subsidyDTO.getRptType().trim().isEmpty()) {
					setErrorMsg("Please select a report type.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				} else {

					try {
						conn = ConnectionManager.getConnection();
						if (subsidyDTO.getRptType().equalsIgnoreCase("PayType")) {

							if (subsidyDTO.getPaidNonPaid().trim().isEmpty()
									|| subsidyDTO.getPaidNonPaid().equals(null)) {
								setErrorMsg("Please select paid or non paid.");
								RequestContext.getCurrentInstance().update("requiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							} else {
								if (subsidyDTO.getRptType().equalsIgnoreCase("PayType")
										&& subsidyDTO.getPaidNonPaid().equalsIgnoreCase("P")) {
									sourceFileName = "..//reports//sisuPaidNonPaidRuningPerRpt.jrxml";
								}
								if (subsidyDTO.getRptType().equalsIgnoreCase("PayType")
										&& subsidyDTO.getPaidNonPaid().equalsIgnoreCase("NP")) {
									sourceFileName = "..//reports//sisuNonPaidRuningPerReport.jrxml";
								}
							}
						} else if (subsidyDTO.getRptType().equalsIgnoreCase("cancelType")) {
							sourceFileName = "..//reports//sisuCancelRuningPerRpt.jrxml";

						} else if (subsidyDTO.getRptType().equalsIgnoreCase("notApproved")) {
							sourceFileName = "..//reports//sisuNotApprovedRuningPerRpt.jrxml";

						} else if (subsidyDTO.getRptType().equalsIgnoreCase("logSheetNotIssue")) {

							sourceFileName = "..//reports//sisulogSheetNotIssueRuningPerRpt.jrxml";

						} else if (subsidyDTO.getRptType().equalsIgnoreCase("PenaltyType")) {
							if (subsidyDTO.getPenaltyFeePer().equals(null)
									|| subsidyDTO.getPenaltyFeePer().trim().isEmpty()) {
								setErrorMsg("Please enter penalty fee.");
								RequestContext.getCurrentInstance().update("requiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							} else {
								sourceFileName = "..//reports//sisuPenaltyFeeRuningPerRpt.jrxml";
							}
						}

						String logopath = "//lk//informatics//ntc//view//reports//";
						Map<String, Object> parameters = new HashMap<String, Object>();

						parameters.put("P_start_month_value", Integer.parseInt(startMonthValue));
						parameters.put("P_end_month_value", Integer.parseInt(endMonthValue));
						parameters.put("P_selected_year", selectedYear);
						parameters.put("P_date_range", setDateRangeParameter(Integer.parseInt(startMonthValue), Integer.parseInt(endMonthValue)));
						
						if (subsidyDTO.getRptType().equals("PenaltyType") && subsidyDTO.getPenaltyFeePer() != null
								&& !subsidyDTO.getPenaltyFeePer().trim().isEmpty()) {
							parameters.put("P_penalty_fee", Integer.parseInt(subsidyDTO.getPenaltyFeePer()));
						}

						if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
							parameters.put("P_Province", null);
						} else {
							parameters.put("P_Province", subsidyDTO.getProvinceCode());
						}
						if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
							parameters.put("P_District", null);

						} else {
							parameters.put("P_District", subsidyDTO.getDistrictCode());

						}
						parameters.put("P_ntc_logo", logopath);
						parameters.put("P_national_logo", logopath);
						if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")) {
							parameters.put("P_is_sltb", "N");
						} else if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", "Y");
						} else if (!subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")
								&& !subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", null);
						}

						JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

						JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

						if (jasperPrint.getPages().isEmpty()) {
							setErrorMsg("No data was found on the selected criteria");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							return null;
						} else {
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

							byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
							InputStream stream = new ByteArrayInputStream(pdfByteArray);
							files = new DefaultStreamedContent(stream, "Application/pdf",
									"sisuSariyaPaidNonPaidReport.pdf");
							RequestContext.getCurrentInstance().execute(
									"window.open('/InfoNTC/showUpload?reportName=RUnning Percentage Report');");
							ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
							Map<String, Object> sessionMap = externalContext.getSessionMap();
							sessionMap.put("reportBytes", pdfByteArray);
							sessionMap.put("docType", "pdf");
						}

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ConnectionManager.close(conn);
					}

				}
				return files;
			}
		}
	}

	// running percentage paid excel Report
	public void genRunPerPaidExcelReport() {

		if (selectedYear == null || selectedYear.trim().isEmpty() || startMonthValue == null
				|| startMonthValue.trim().isEmpty() || endMonthValue == null || endMonthValue.trim().isEmpty()) {
			setErrorMsg("Please Fill Mandatory Data");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {
			files = null;
			String sourceFileName = null;

			Connection conn = null;
			if (subsidyDTO.getRptType().equals(null) || subsidyDTO.getRptType().trim().isEmpty()) {
				setErrorMsg("Please select a report type.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else {

				if (Integer.parseInt(startMonthValue) > Integer.parseInt(endMonthValue)) {
					setErrorMsg("The end month must be later than the starting month.");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				} else {
					try {
						conn = ConnectionManager.getConnection();
						if (subsidyDTO.getRptType().equalsIgnoreCase("PayType")) {

							if (subsidyDTO.getPaidNonPaid().trim().isEmpty()
									|| subsidyDTO.getPaidNonPaid().equals(null)) {
								setErrorMsg("Please select paid or non paid.");
								RequestContext.getCurrentInstance().update("requiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							} else {
								if (subsidyDTO.getRptType().equalsIgnoreCase("PayType")
										&& subsidyDTO.getPaidNonPaid().equalsIgnoreCase("P")) {
									sourceFileName = "..//reports//sisuPaidNonPaidRuningPerRpt.jrxml";
								}
								if (subsidyDTO.getRptType().equalsIgnoreCase("PayType")
										&& subsidyDTO.getPaidNonPaid().equalsIgnoreCase("NP")) {
									sourceFileName = "..//reports//sisuNonPaidRuningPerReport.jrxml";
								}

							}
						} else if (subsidyDTO.getRptType().equalsIgnoreCase("cancelType")) {
							sourceFileName = "..//reports//sisuCancelRuningPerRpt.jrxml";

						} else if (subsidyDTO.getRptType().equalsIgnoreCase("notApproved")) {
							sourceFileName = "..//reports//sisuNotApprovedRuningPerRpt.jrxml";

						} else if (subsidyDTO.getRptType().equalsIgnoreCase("logSheetNotIssue")) {

							sourceFileName = "..//reports//sisulogSheetNotIssueRuningPerRpt.jrxml";

						} else if (subsidyDTO.getRptType().equalsIgnoreCase("PenaltyType")) {
							if (subsidyDTO.getPenaltyFeePer().equals(null)
									|| subsidyDTO.getPenaltyFeePer().trim().isEmpty()) {
								setErrorMsg("Please enter penalty fee.");
								RequestContext.getCurrentInstance().update("requiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							} else {

								sourceFileName = "..//reports//sisuPenaltyFeeRuningPerRpt.jrxml";
							}

						}

						String logopath = "//lk//informatics//ntc//view//reports//";
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("P_start_month_value", Integer.parseInt(startMonthValue));
						parameters.put("P_end_month_value", Integer.parseInt(endMonthValue));
						parameters.put("P_selected_year", selectedYear);
						parameters.put("P_date_range", setDateRangeParameter(Integer.parseInt(startMonthValue),
								Integer.parseInt(endMonthValue)));
						if (subsidyDTO.getRptType().equals("PenaltyType") && subsidyDTO.getPenaltyFeePer() != null
								&& !subsidyDTO.getPenaltyFeePer().trim().isEmpty()) {
							parameters.put("P_penalty_fee", Integer.parseInt(subsidyDTO.getPenaltyFeePer()));
						}

						if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
							parameters.put("P_Province", null);
						} else {
							parameters.put("P_Province", subsidyDTO.getProvinceCode());
						}
						if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
							parameters.put("P_District", null);

						} else {
							parameters.put("P_District", subsidyDTO.getDistrictCode());

						}
						parameters.put("P_ntc_logo", logopath);
						parameters.put("P_national_logo", logopath);
						if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")) {
							parameters.put("P_is_sltb", "N");
						} else if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", "Y");
						} else if (!subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")
								&& !subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", null);
						}

						JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

						JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

						if (jasperPrint.getPages().isEmpty()) {
							setErrorMsg("No data was found on the selected criteria");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							return;
						} else {
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

							ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
							JRXlsxExporter exporter = new JRXlsxExporter();
							exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
							exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
							SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
							configuration.setForcePageBreaks(false);
							configuration.setRemoveEmptySpaceBetweenRows(true);
							configuration.setRemoveEmptySpaceBetweenColumns(true);
							exporter.setConfiguration(configuration);
							exporter.exportReport();

							byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
							InputStream stream = new ByteArrayInputStream(pdfByteArray);
							files = new DefaultStreamedContent(stream, "Application/xlsx",
									"SisuRunningPercentageReport.xlsx");
							RequestContext.getCurrentInstance().execute("PF('genReport').hide();");

							ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
							Map<String, Object> sessionMap = externalContext.getSessionMap();
							sessionMap.put("reportBytes", pdfByteArray);
							sessionMap.put("docType", "xlsx");
						}

					} catch (Exception e) {

						e.printStackTrace();
					} finally {
						ConnectionManager.close(conn);
					}
				}

			}
		}

	}

	// monthly operated KM Report
	public StreamedContent genMonthlyOPKmERep(ActionEvent ae) throws JRException {

		if (selectedYear == null || selectedYear.trim().isEmpty() || startMonthValue == null
				|| startMonthValue.trim().isEmpty()
				|| endMonthValue == null
				|| endMonthValue.trim().isEmpty()) {
			setErrorMsg("Please Fill Mandatory Data");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {
			if(Integer.parseInt(startMonthValue) > Integer.parseInt(endMonthValue)) {
				setErrorMsg("The end month must be later than the starting month.");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return null;
			}else {
				if (subsidyDTO.getPaidNonPaid().equals(null) || subsidyDTO.getPaidNonPaid().trim().isEmpty()) {
					setErrorMsg("Please select  paid or non paid.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				} else {

					files = null;
					String sourceFileName = null;

					Connection conn = null;

					try {
						conn = ConnectionManager.getConnection();
						if (subsidyDTO.getPaidNonPaid().equalsIgnoreCase("P")) {
							sourceFileName = "..//reports//monthlyOPeratedKMReport.jrxml";
						} else if (subsidyDTO.getPaidNonPaid().equalsIgnoreCase("NP")) {
							sourceFileName = "..//reports//monthlyOPeratedNonPaidKMReport.jrxml";
						}

						String logopath = "//lk//informatics//ntc//view//reports//";
						Map<String, Object> parameters = new HashMap<String, Object>();
						
						parameters.put("P_start_month_value", Integer.parseInt(startMonthValue));
						parameters.put("P_end_month_value", Integer.parseInt(endMonthValue));
						parameters.put("P_selected_year", selectedYear);
						parameters.put("P_date_range", setDateRangeParameter(Integer.parseInt(startMonthValue), Integer.parseInt(endMonthValue)));

						if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
							parameters.put("P_province_code", null);
						} else {
							parameters.put("P_province_code", subsidyDTO.getProvinceCode());
						}
						if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
							parameters.put("P_district_code", null);
						} else {
							parameters.put("P_district_code", subsidyDTO.getDistrictCode());
						}
						parameters.put("P_ntc_logo", logopath);
						parameters.put("P_national_logo", logopath);

						if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")) {
							parameters.put("P_is_sltb", "N");
						} else if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", "Y");
						} else if (!subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")
								&& !subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", null);
						}

						JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

						JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

						if (jasperPrint.getPages().isEmpty()) {
							setErrorMsg("No data was found on the selected criteria");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							return null;
						} else {
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

							byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
							InputStream stream = new ByteArrayInputStream(pdfByteArray);
							files = new DefaultStreamedContent(stream, "Application/pdf", "Subsidy Bus Info.pdf");
							RequestContext.getCurrentInstance()
									.execute("window.open('/InfoNTC/showUpload?reportName=Monthly Operated KM Report');");
							ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
							Map<String, Object> sessionMap = externalContext.getSessionMap();
							sessionMap.put("reportBytes", pdfByteArray);
							sessionMap.put("docType", "pdf");
						}

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						ConnectionManager.close(conn);
					}
				}
			}
		}
		return files;

	}
	
	// added by danilka.j
	private String setDateRangeParameter(int startMonthValue, int endMonthValue) {
	    String[] monthNames = {"", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	    
	    if(startMonthValue == endMonthValue) {
	    	String startMonth = (startMonthValue >= 1 && startMonthValue <= 12) ? monthNames[startMonthValue] : "";
	    	return "For " + startMonth;
	    }else {
	    	String startMonth = (startMonthValue >= 1 && startMonthValue <= 12) ? monthNames[startMonthValue] : "";
		    String endMonth = (endMonthValue >= 1 && endMonthValue <= 12) ? monthNames[endMonthValue] : "";

		    return "From " + startMonth + " to " + endMonth;
	    }

	    
	}

	// montly operated km Excel Report
	public void genMonthlyOPKmEXcelRep() {

		if (selectedYear == null || selectedYear.trim().isEmpty() || startMonthValue == null
				|| startMonthValue.trim().isEmpty() || endMonthValue == null || endMonthValue.trim().isEmpty()) {
			setErrorMsg("Please Fill Mandatory Data");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

			if (Integer.parseInt(startMonthValue) > Integer.parseInt(endMonthValue)) {
				setErrorMsg("The end month must be later than the starting month.");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else {
				if (subsidyDTO.getPaidNonPaid().equals(null) || subsidyDTO.getPaidNonPaid().trim().isEmpty()) {
					setErrorMsg("Please select  paid or non paid.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				} else {
					files = null;
					String sourceFileName = null;

					Connection conn = null;

					try {
						conn = ConnectionManager.getConnection();
						if (subsidyDTO.getPaidNonPaid().equalsIgnoreCase("P")) {
							sourceFileName = "..//reports//monthlyOPeratedKMReport.jrxml";
						} else if (subsidyDTO.getPaidNonPaid().equalsIgnoreCase("NP")) {
							sourceFileName = "..//reports//monthlyOPeratedNonPaidKMReport.jrxml";
						}

						String logopath = "//lk//informatics//ntc//view//reports//";
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("P_start_month_value", Integer.parseInt(startMonthValue));
						parameters.put("P_end_month_value", Integer.parseInt(endMonthValue));
						parameters.put("P_selected_year", selectedYear);
						parameters.put("P_date_range", setDateRangeParameter(Integer.parseInt(startMonthValue),
								Integer.parseInt(endMonthValue)));
						if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
							parameters.put("P_province_code", null);
						} else {
							parameters.put("P_province_code", subsidyDTO.getProvinceCode());
						}
						if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
							parameters.put("P_district_code", null);
						} else {
							parameters.put("P_district_code", subsidyDTO.getDistrictCode());
						}
						parameters.put("P_ntc_logo", logopath);
						parameters.put("P_national_logo", logopath);

						if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")) {
							parameters.put("P_is_sltb", "N");
						} else if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", "Y");
						} else if (!subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")
								&& !subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", null);
						}

						JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

						JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

						if (jasperPrint.getPages().isEmpty()) {
							setErrorMsg("No data was found on the selected criteria");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							return;
						} else {
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

							ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
							JRXlsxExporter exporter = new JRXlsxExporter();
							exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
							exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
							SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
							configuration.setForcePageBreaks(false);
							configuration.setRemoveEmptySpaceBetweenRows(true);
							configuration.setRemoveEmptySpaceBetweenColumns(true);
							exporter.setConfiguration(configuration);
							exporter.exportReport();

							byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
							InputStream stream = new ByteArrayInputStream(pdfByteArray);
							files = new DefaultStreamedContent(stream, "Application/xlsx",
									"SisuMOnthlyOperatedKM.Report.xlsx");
							RequestContext.getCurrentInstance().execute("PF('genReport').hide();");

							ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
							Map<String, Object> sessionMap = externalContext.getSessionMap();
							sessionMap.put("reportBytes", pdfByteArray);
							sessionMap.put("docType", "xlsx");
						}

					} catch (Exception e) {

						setErrorMsg("Please close or save opend excel sheet.");
						RequestContext.getCurrentInstance().update("requiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					} finally {
						ConnectionManager.close(conn);
					}
				}

			}

		}

	}

	// genarate Sisu Bus wise School Report
	public StreamedContent genBusWiseSchoolRpt(ActionEvent ae) throws JRException {
		files = null;
		String sourceFileName = null;
		Connection conn = null;
		if (selectedYear == null || selectedYear.trim().isEmpty() || startMonthValue == null
				|| startMonthValue.trim().isEmpty() || endMonthValue == null || endMonthValue.trim().isEmpty()) {
			setErrorMsg("Please Fill Mandatory Data");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

			if (Integer.parseInt(startMonthValue) > Integer.parseInt(endMonthValue)) {
				setErrorMsg("The end month must be later than the starting month.");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return null;
			} else {

				try {

					conn = ConnectionManager.getConnection();

					sourceFileName = "..//reports//sisuBusWIseScholRpt.jrxml";

					String logopath = "//lk//informatics//ntc//view//reports//";
					Map<String, Object> parameters = new HashMap<String, Object>();

					parameters.put("P_start_month_value", Integer.parseInt(startMonthValue));
					parameters.put("P_end_month_value", Integer.parseInt(endMonthValue));
					parameters.put("P_selected_year", selectedYear);
					parameters.put("P_date_range",
							setDateRangeParameter(Integer.parseInt(startMonthValue), Integer.parseInt(endMonthValue)));
					if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
						parameters.put("P_Province", null);
					} else {
						parameters.put("P_Province", subsidyDTO.getProvinceCode());
					}
					if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
						parameters.put("P_District", null);
					} else {
						parameters.put("P_District", subsidyDTO.getDistrictCode());
					}
					parameters.put("P_ntc_logo", logopath);
					parameters.put("P_national_logo", logopath);

					if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")) {
						parameters.put("P_is_sltb", "N");
					} else if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
						parameters.put("P_is_sltb", "Y");
					} else if (!subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")
							&& !subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
						parameters.put("P_is_sltb", null);
					}

					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));
					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

					if (jasperPrint.getPages().isEmpty()) {
						setErrorMsg("No data was found on the selected criteria");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						return null;
					} else {
						jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
						jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

						byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
						InputStream stream = new ByteArrayInputStream(pdfByteArray);
						files = new DefaultStreamedContent(stream, "Application/pdf", "Subsidy Bus Info.pdf");
						RequestContext.getCurrentInstance()
								.execute("window.open('/InfoNTC/showUpload?reportName=Sisu Bus Wise School Report');");
						ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
						Map<String, Object> sessionMap = externalContext.getSessionMap();
						sessionMap.put("reportBytes", pdfByteArray);
						sessionMap.put("docType", "pdf");
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					ConnectionManager.close(conn);
				}
			}

		}

		return files;

	}
	
	public void validateEndDate() {
		//System.out.println("validating end date : " + "start date : " + startDate + " end date : " + endDate);
		if (startDate.compareTo(endDate) > 0) {
	        // Add FacesMessage or any other logic to handle validation failure
	    	setErrorMsg("End Date should be after Start Date.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
	    }else {
	    	System.out.println("correct date range");
	    }
	}


	// generate Sisu Bus Wise School Report in excel
	public void genBusWiseSchoolRptExcel() {

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		if (selectedYear == null || selectedYear.trim().isEmpty() || startMonthValue == null
				|| startMonthValue.trim().isEmpty() || endMonthValue == null || endMonthValue.trim().isEmpty()) {
			setErrorMsg("Please Fill Mandatory Data");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {
			if (Integer.parseInt(startMonthValue) > Integer.parseInt(endMonthValue)) {
				setErrorMsg("The end month must be later than the starting month.");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else {
				try {
					conn = ConnectionManager.getConnection();

					sourceFileName = "..//reports//sisuBusWIseScholRpt.jrxml";

					String logopath = "//lk//informatics//ntc//view//reports//";
					Map<String, Object> parameters = new HashMap<String, Object>();
					

					if ((startDate == null && endDate != null) || (startDate != null && endDate == null)) {
						
						setErrorMsg("Please enter both start and end date");
						RequestContext.getCurrentInstance().update("requiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");		
						return;
			
					}else {
						parameters.put("P_start_month_value", Integer.parseInt(startMonthValue));
						parameters.put("P_end_month_value", Integer.parseInt(endMonthValue));
						parameters.put("P_selected_year", selectedYear);
						parameters.put("P_date_range",
								setDateRangeParameter(Integer.parseInt(startMonthValue), Integer.parseInt(endMonthValue)));
						if (subsidyDTO.getProvinceCode().trim().isEmpty()) {
							parameters.put("P_Province", null);
						} else {
							parameters.put("P_Province", subsidyDTO.getProvinceCode());
						}
						if (subsidyDTO.getDistrictCode().trim().isEmpty()) {
							parameters.put("P_District", null);
						} else {
							parameters.put("P_District", subsidyDTO.getDistrictCode());
						}
						parameters.put("P_ntc_logo", logopath);
						parameters.put("P_national_logo", logopath);

						if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")) {
							parameters.put("P_is_sltb", "N");
						} else if (subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", "Y");
						} else if (!subsidyDTO.getPrivateSltb().equalsIgnoreCase("P")
								&& !subsidyDTO.getPrivateSltb().equalsIgnoreCase("S")) {
							parameters.put("P_is_sltb", null);
						}
						
						JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));
						JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);
						JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
						jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
						jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
						
						if (jasperPrint.getPages().isEmpty()) {
				            setErrorMsg("No data was found on the selected criteria");
				        	RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				            return; 
				        }else{
				        	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
							JRXlsxExporter exporter = new JRXlsxExporter();
							exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
							exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
							SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
							configuration.setForcePageBreaks(false);
							configuration.setRemoveEmptySpaceBetweenRows(true);
							configuration.setRemoveEmptySpaceBetweenColumns(true);
							exporter.setConfiguration(configuration);
							exporter.exportReport();

							byte[] pdfByteArray = byteArrayOutputStream.toByteArray();
							InputStream stream = new ByteArrayInputStream(pdfByteArray);
							files = new DefaultStreamedContent(stream, "Application/xlsx", "SisuBusWiswSchoolReport.xlsx");
							RequestContext.getCurrentInstance().execute("PF('genReport').hide();");

							ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
							Map<String, Object> sessionMap = externalContext.getSessionMap();
							sessionMap.put("reportBytes", pdfByteArray);
							sessionMap.put("docType", "xlsx");
				        }

					}

				} catch (Exception e) {

					e.printStackTrace();
				} finally {
					ConnectionManager.close(conn);
				}
			}
			
		}
		

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
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

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public List<SubSidyDTO> getSubsidyServiceTypeList() {
		return subsidyServiceTypeList;
	}

	public void setSubsidyServiceTypeList(List<SubSidyDTO> subsidyServiceTypeList) {
		this.subsidyServiceTypeList = subsidyServiceTypeList;
	}

	public SubSidyManagementService getSubsidyManagmentService() {
		return subsidyManagmentService;
	}

	public void setSubsidyManagmentService(SubSidyManagementService subsidyManagmentService) {
		this.subsidyManagmentService = subsidyManagmentService;
	}

	public SubSidyDTO getSubsidyDTO() {
		return subsidyDTO;
	}

	public void setSubsidyDTO(SubSidyDTO subsidyDTO) {
		this.subsidyDTO = subsidyDTO;
	}

	public List<SubSidyDTO> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<SubSidyDTO> provinceList) {
		this.provinceList = provinceList;
	}

	public SubsidyReportsService getSubsidyReportsService() {
		return subsidyReportsService;
	}

	public void setSubsidyReportsService(SubsidyReportsService subsidyReportsService) {
		this.subsidyReportsService = subsidyReportsService;
	}

	public List<SubSidyDTO> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<SubSidyDTO> districtList) {
		this.districtList = districtList;
	}

	public List<SubSidyDTO> getSchoolList() {
		return schoolList;
	}

	public void setSchoolList(List<SubSidyDTO> schoolList) {
		this.schoolList = schoolList;
	}

	public boolean isPaidNonPaidShow() {
		return paidNonPaidShow;
	}

	public void setPaidNonPaidShow(boolean paidNonPaidShow) {
		this.paidNonPaidShow = paidNonPaidShow;
	}

	public boolean isPenaltyFeeShow() {
		return penaltyFeeShow;
	}

	public void setPenaltyFeeShow(boolean penaltyFeeShow) {
		this.penaltyFeeShow = penaltyFeeShow;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getPrivateSltb() {
		return privateSltb;
	}

	public void setPrivateSltb(String privateSltb) {
		this.privateSltb = privateSltb;
	}

	public String getSelectedYear() {
		return selectedYear;
	}

	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}

	public String getStartMonthValue() {
		return startMonthValue;
	}

	public void setStartMonthValue(String startMonthValue) {
		this.startMonthValue = startMonthValue;
	}

	public String getEndMonthValue() {
		return endMonthValue;
	}

	public void setEndMonthValue(String endMonthValue) {
		this.endMonthValue = endMonthValue;
	}

	public boolean getIsPrinting() {
		return isPrinting;
	}

	public void setIsPrinting(boolean isPrinting) {
		this.isPrinting = isPrinting;
	}

}
