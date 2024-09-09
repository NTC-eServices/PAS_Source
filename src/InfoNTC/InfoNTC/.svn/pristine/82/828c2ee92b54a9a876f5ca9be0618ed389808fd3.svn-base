package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.ManageInquiryDTO;
import lk.informatics.ntc.model.dto.ParamerDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.ManageInquiryService;
import lk.informatics.ntc.model.service.MigratedService;
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
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.model.exception.ApplicationException;

@ManagedBean(name = "manageInquiryBean")
@ViewScoped
public class ManageInquiryBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private ManageInquiryDTO manageInquiryDTO;
	private List<String> complaintNumList;
	private List<String> vehicleNumList;
	private List<CommonDTO> provincelList;
	private List<ManageInquiryDTO> priorityOrderList;
	private List<ManageInquiryDTO> severityList;
	private List<ManageInquiryDTO> manageInquiryDTOList;
	private ManageInquiryDTO selectedData;
	private VehicleInspectionDTO vehicleInfoData;
	private List<ManageInquiryDTO> availableTimesList;
	private boolean mainDataTableShow, showbutton;
	private ManageInquiryDTO selectedTime;
	private boolean setTimeBtnRender;
	private boolean disableProvinceSearch = true;
	private boolean disableSearchOptions = false;

	private List<ManageInquiryDTO> actionDepartmentsList;

	private List<ManageInquiryDTO> selectDTOList;

	private ManageInquiryService manageInquiryService;
	private DocumentManagementService documentManagementService;
	private CommonService commonService;
	private AdminService adminService;
	private MigratedService migratedService;
	private Date today;
	private ManageInquiryDTO sendMailDTO;
	private List<ManageInquiryDTO> conductorDriverList;
	private String lunchstartTime;
	private String lunchendTime;

	private boolean oldContact;

	private ComplaintRequestDTO selectedComplaintDTO;
	private StreamedContent file;

	private boolean printWarningDisable;
	private boolean disablePtaCtbBtn;
	private boolean renderCloseBtn = false;
	private String specialRemark;

	private List<ManageInquiryDTO> actionTakenList;

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private String successMessage = "", errorMessage = "";

	@PostConstruct
	public void init() {

		manageInquiryService = (ManageInquiryService) SpringApplicationContex.getBean("manageInquiryService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		loadDate();
	}

	public void loadDate() {
		manageInquiryDTO = new ManageInquiryDTO();

		complaintNumList = new ArrayList<String>();
		complaintNumList = manageInquiryService.retrieveComplaintNumbersForManageInquiry();

		vehicleNumList = new ArrayList<String>();
		vehicleNumList = manageInquiryService.retrieveVehicleNumbers();

		provincelList = new ArrayList<CommonDTO>();
		provincelList = adminService.getProvinceToDropdown();

		actionDepartmentsList = new ArrayList<ManageInquiryDTO>();
		actionDepartmentsList = manageInquiryService.retrieveActionDepartmentsList();

		priorityOrderList = new ArrayList<ManageInquiryDTO>();
		priorityOrderList = manageInquiryService.retrievePriorityOrderList();

		severityList = new ArrayList<ManageInquiryDTO>();
		severityList = manageInquiryService.retrieveSeverity();

		manageInquiryDTOList = new ArrayList<ManageInquiryDTO>();

		selectedData = new ManageInquiryDTO();
		vehicleInfoData = new VehicleInspectionDTO();

		availableTimesList = new ArrayList<ManageInquiryDTO>();
		mainDataTableShow = false;
		showbutton = false;

		selectedTime = new ManageInquiryDTO();
		setTimeBtnRender = false;
		today = new Date();
		sendMailDTO = new ManageInquiryDTO();
		conductorDriverList = new ArrayList<ManageInquiryDTO>();

		lunchstartTime = null;
		lunchendTime = null;

		selectedComplaintDTO = new ComplaintRequestDTO();
		oldContact = false;
		printWarningDisable = true;
		disableProvinceSearch = true;
		disableSearchOptions = false;
		specialRemark = null;

		selectDTOList = new ArrayList<ManageInquiryDTO>();

		actionTakenList = new ArrayList<ManageInquiryDTO>();

		renderCloseBtn = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN452", "CL");

		RequestContext.getCurrentInstance().update("manageInqFrm");

	}

	public void onPermitAuthorityChange() {
		if (manageInquiryDTO.getAuthority() != null && !manageInquiryDTO.getAuthority().trim().equals("")) {
			if (manageInquiryDTO.getAuthority().trim().equals("PTA")
					|| manageInquiryDTO.getAuthority().trim().equals("NTC")
					|| manageInquiryDTO.getAuthority().trim().equals("CTB")) {
				disableProvinceSearch = false;
			} else {
				manageInquiryDTO.setProvince(null);
				disableProvinceSearch = true;
			}
			complaintNumList = manageInquiryService.retrieveComplaintNumbersForManageInquiry(manageInquiryDTO);
		}
	}

	public void onProvinceChange() {
		if (manageInquiryDTO.getProvince() != null && !manageInquiryDTO.getProvince().trim().equals("")) {
			complaintNumList = manageInquiryService.retrieveComplaintNumbersForManageInquiry(manageInquiryDTO);
		}
	}

	public void searchButtonAction() {

		if (manageInquiryDTO.getAuthority() != null && manageInquiryDTO.getAuthority().trim().equals("PTA")) {
			if (manageInquiryDTO.getProvince() == null || manageInquiryDTO.getProvince().trim().equals("")) {
				setErrorMessage("Province should be selected.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				return;
			}
		}

		if (manageInquiryDTO.getStartDate() != null) {
			if (manageInquiryDTO.getEndDate() == null) {
				setErrorMessage("Please select End Date.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				return;
			}
		}
		if (manageInquiryDTO.getEndDate() != null) {
			if (manageInquiryDTO.getStartDate() == null) {
				setErrorMessage("Please select End Date.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				return;
			}
		}

		disableSearchOptions = true;
		manageInquiryDTOList = manageInquiryService.retrieveInquiryComplaintData(manageInquiryDTO,sessionBackingBean.getLoginUser());
		mainDataTableShow = true;
		showbutton = true;
		printWarningDisable = false;

		RequestContext.getCurrentInstance().update("manageInqDT");
		RequestContext.getCurrentInstance().update("manageInqFrm");

	}

	public void clearButtonAction() {
		loadDate();
	}

	public void printInquiryLetterBtnAction(ManageInquiryDTO passedData) {
		selectedData = passedData;
		if (selectedData.getProcess_status().equals("P") || selectedData.getProcess_status().equals("O")) {
			try {
				if (selectedData.getAuthority().trim().equals("NTC")) {
					downloadInquiryLetters();
				} else {
					downloadInquiryLettersForOthers();
				}
				if (file != null) {
					RequestContext.getCurrentInstance().execute("window.open('/InfoNTC/showUpload')");
					manageInquiryService.insertGrievanceTask(selectedData, sessionBackingBean.loginUser, "GM103", "C");
					manageInquiryService.beanLinkMethod(selectedData, sessionBackingBean.loginUser, "Inquiry Letter Print", "Manage Inquiry");
				}
			} catch (JRException e) {
				e.printStackTrace();
			}
		} else {
			setErrorMessage("Selected record is already closed.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void onConducterDriverSelect() {

		String address = null;
		if (conductorDriverList != null && !conductorDriverList.isEmpty() && conductorDriverList.size() != 0) {
			for (ManageInquiryDTO dto : conductorDriverList) {
				if (dto.getContactName().equals(sendMailDTO.getContactName())) {
					address = dto.getContactAddress();
				}
			}
		}
		try {
			if (sendMailDTO.getContactName() != null && !sendMailDTO.getContactName().isEmpty()
					&& !sendMailDTO.getContactName().trim().equals("")) {

				if (address != null && !address.isEmpty() && !address.trim().equals("")) {
					sendMailDTO.setContactAddress(address);

					boolean exist = manageInquiryService
							.checkDataAvailableInNt_r_investigation_alerts(sendMailDTO.getComplainNo(), true);

					if (exist) {
						sendMailDTO = manageInquiryService.retrieveLetterDetails(sendMailDTO.getContactNum());
					} else {

						ParamerDTO sentenceOneDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_ONE");
						ParamerDTO sentencTwoDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_TWO");
						ParamerDTO sentenceThreeDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_THREE");

						if (sentenceOneDTO != null && sentenceOneDTO.getStringValue() != null
								&& !sentenceOneDTO.getStringValue().isEmpty()
								&& !sentenceOneDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceOne(sentenceOneDTO.getStringValue());
						}
						if (sentencTwoDTO != null && sentencTwoDTO.getStringValue() != null
								&& !sentencTwoDTO.getStringValue().isEmpty()
								&& !sentencTwoDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceTwo(sentencTwoDTO.getStringValue());
						}
						if (sentenceThreeDTO != null && sentenceThreeDTO.getStringValue() != null
								&& !sentenceThreeDTO.getStringValue().isEmpty()
								&& !sentenceThreeDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceThree(sentenceThreeDTO.getStringValue());
						}
					}

				}

				RequestContext.getCurrentInstance().update("printLetterDlg:printLetterFrm");
			} else {
				setErrorMessage("Please select the Contact Name.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void onTabChange(TabChangeEvent event) {

		boolean exist = false;
		if (event.getTab().getId().equals("detailTab")) {

			try {
				if (sendMailDTO.getContactName() != null && !sendMailDTO.getContactName().isEmpty()
						&& !sendMailDTO.getContactName().trim().equals("")) {

					exist = manageInquiryService
							.checkDataAvailableInNt_r_investigation_alerts(selectedData.getComplainNo(), false);
					if (exist) {
						ManageInquiryDTO dto = new ManageInquiryDTO();
						dto = manageInquiryService.retrieveSMSDetails(sendMailDTO.getComplainNo());

						sendMailDTO.setNotifySentenceOne(dto.getNotifySentenceOne());
						sendMailDTO.setNotifySentenceTwo(dto.getNotifySentenceTwo());
						sendMailDTO.setNotifySentenceThree(dto.getNotifySentenceThree());
						sendMailDTO.setNotifySentencFour(dto.getNotifySentencFour());
					} else {

						ParamerDTO sentenceSMSDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_SMS");
						ParamerDTO sentenceOneDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_ONE");
						ParamerDTO sentencTwoDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_TWO");
						ParamerDTO sentenceThreeDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_THREE");

						if (sentenceSMSDTO != null && sentenceSMSDTO.getStringValue() != null
								&& !sentenceSMSDTO.getStringValue().isEmpty()
								&& !sentenceSMSDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceOne(sentenceSMSDTO.getStringValue());
						}
						if (sentenceOneDTO != null && sentenceOneDTO.getStringValue() != null
								&& !sentenceOneDTO.getStringValue().isEmpty()
								&& !sentenceOneDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceTwo(sentenceOneDTO.getStringValue());
						}
						if (sentencTwoDTO != null && sentencTwoDTO.getStringValue() != null
								&& !sentencTwoDTO.getStringValue().isEmpty()
								&& !sentencTwoDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceThree(sentencTwoDTO.getStringValue());
						}
						if (sentenceThreeDTO != null && sentenceThreeDTO.getStringValue() != null
								&& !sentenceThreeDTO.getStringValue().isEmpty()
								&& !sentenceThreeDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentencFour(sentenceThreeDTO.getStringValue());
						}
					}

					RequestContext.getCurrentInstance().update("sendSMSDlg:smsDlgFrm");
				} else {
					setErrorMessage("Please enter the Contact Name.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		}

	}

	public void tabChangeLetter(TabChangeEvent event) {

		boolean exist = false;
		if (event.getTab().getId().equals("letterInfo")) {

			try {
				if (sendMailDTO.getContactName() != null && !sendMailDTO.getContactName().isEmpty()
						&& !sendMailDTO.getContactName().trim().equals("")) {

					exist = manageInquiryService
							.checkDataAvailableInNt_r_investigation_alerts(sendMailDTO.getComplainNo(), true);
					if (exist) {
						ManageInquiryDTO dto = new ManageInquiryDTO();
						dto = manageInquiryService.retrieveLetterDetails(sendMailDTO.getComplainNo());

						sendMailDTO.setNotifySentenceOne(dto.getNotifySentenceOne());
						sendMailDTO.setNotifySentenceTwo(dto.getNotifySentenceTwo());
						sendMailDTO.setNotifySentenceThree(dto.getNotifySentenceThree());
					} else {

						ParamerDTO sentenceOneDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_ONE");
						ParamerDTO sentencTwoDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_TWO");
						ParamerDTO sentenceThreeDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_THREE");

						if (sentenceOneDTO != null && sentenceOneDTO.getStringValue() != null
								&& !sentenceOneDTO.getStringValue().isEmpty()
								&& !sentenceOneDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceOne(sentenceOneDTO.getStringValue());
						}
						if (sentencTwoDTO != null && sentencTwoDTO.getStringValue() != null
								&& !sentencTwoDTO.getStringValue().isEmpty()
								&& !sentencTwoDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceTwo(sentencTwoDTO.getStringValue());
						}
						if (sentenceThreeDTO != null && sentenceThreeDTO.getStringValue() != null
								&& !sentenceThreeDTO.getStringValue().isEmpty()
								&& !sentenceThreeDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceThree(sentenceThreeDTO.getStringValue());
						}
					}

					RequestContext.getCurrentInstance().update("sendSMSDlg:smsDlgFrm");
				} else {
					setErrorMessage("Please enter the Contact Name.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		}

	}

	public void closeBtnAction() {
		if (selectDTOList.size() > 0) {
			if (selectDTOList.size() == 1) {
				if (selectDTOList.get(0).getAuthority() != null
						&& !selectDTOList.get(0).getAuthority().trim().equals("")
						&& !selectDTOList.get(0).getAuthority().trim().equals("PTA")
						&& !selectDTOList.get(0).getAuthority().trim().equals("CTB")) {

					if (selectDTOList.get(0).getProcess_status().equals("P")
							|| selectDTOList.get(0).getProcess_status().equals("O")) {
						// ADD special remark to the only DTO
						selectDTOList.get(0).setSpecialRemark(specialRemark);
						boolean assignedToDept = manageInquiryService
								.checkGrievanceTask(selectDTOList.get(0).getComplainNo(), "GM102", "C");
						if (assignedToDept) {
							boolean actionCompleted = manageInquiryService
									.checkGrievanceTask(selectDTOList.get(0).getComplainNo(), "GM105", "C");
							if (actionCompleted) {
								boolean closed = manageInquiryService.updateComplaintStatus(selectDTOList.get(0), "C",
										sessionBackingBean.loginUser);
								if (closed) {
									setSuccessMessage("Complaint closed successfully.");
									RequestContext.getCurrentInstance().update("successMSG");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									searchButtonAction();
								} else {
									setErrorMessage("System error. Could not close the complaint.");
									RequestContext.getCurrentInstance().update("errorMSG");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
							} else {
								setErrorMessage("Action from the assigned department is still pending.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
						} else {
							boolean closed = manageInquiryService.updateComplaintStatus(selectDTOList.get(0), "C",
									sessionBackingBean.loginUser);
							if (closed) {
								setSuccessMessage("Complaint closed successfully.");
								RequestContext.getCurrentInstance().update("successMSG");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								searchButtonAction();
							} else {
								setErrorMessage("System error. Could not close the complaint.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
						}
					} else {
						setErrorMessage("Selected record is already closed.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Invalid action.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Invalid action. Multiple selections are not allowed.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select a record from the table.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	/** Permit Owner Warning Letter **/
	public void printWarningLetter() {
		if (selectDTOList.size() > 0) {
			if (selectDTOList.size() == 1) {
				if (!manageInquiryDTO.getAuthority().trim().equals("PTA")
						&& !manageInquiryDTO.getAuthority().trim().equals("CTB")) {

					if (selectDTOList.get(0).getProcess_status().equals("P")
							|| selectDTOList.get(0).getProcess_status().equals("O")) {
						boolean assignedToDept = manageInquiryService
								.checkGrievanceTask(selectDTOList.get(0).getComplainNo(), "GM102", "C");
						if (!assignedToDept) {
							if (selectDTOList.get(0).getAuthority().trim().equals("NTC")) {
								try {
									downloadWarningLetter();
									manageInquiryService.beanLinkMethod(manageInquiryDTO, sessionBackingBean.loginUser, "Print Warning Letter(NTC)", "Manage Inquiry");
								} catch (JRException e) {
									e.printStackTrace();
								}
								if (file != null) {
									RequestContext.getCurrentInstance().execute("window.open('/InfoNTC/showUpload')");
									manageInquiryService.insertGrievanceTask( selectDTOList.get(0),
											sessionBackingBean.loginUser, "GM100", "C");
								}
							}

							if (selectDTOList.get(0).getAuthority().trim().equals("TP")) {
								try {
									downloadWarningLetterForTP();
									manageInquiryService.beanLinkMethod(selectedData, sessionBackingBean.loginUser, "Print Warning Letter(TP)", "Manage Inquiry");
								} catch (JRException e) {
									e.printStackTrace();
								}
								if (file != null) {
									RequestContext.getCurrentInstance().execute("window.open('/InfoNTC/showUpload')");
									manageInquiryService.insertGrievanceTask(selectDTOList.get(0),
											sessionBackingBean.loginUser, "GM100", "C");
								}
							}

						} else {
							setErrorMessage("Complaint already assigned to a department.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Selected record is already closed.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Invalid action.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Invalid action. Multiple selections are not allowed.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select a record from the table.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public StreamedContent downloadInquiryLetters() throws JRException {
		String valueForPrint = null;
		valueForPrint = selectedData.getComplainNo();

		file = null;

		String sourceFileName = null;

		List<String> reports = new ArrayList<>();
		String offence = manageInquiryService.getOffenceInSinhala(selectedData.getComplainNo());
		Connection con = null;

		String covidText = null;
		try {
			covidText = PropertyReader.getPropertyValue("covid.advice.text");
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sourceFileName = "..//reports//PublicComplainInquiryLetter.jrxml";
		String subReportPath = "lk/informatics/ntc/view/reports/";

		// Parameters for report
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("P_Complain_no", valueForPrint);
		parameters.put("P_offence", offence);
		parameters.put("SUBREPORT_PATH", subReportPath);
		if (covidText.equalsIgnoreCase("print")) {
			parameters.put("P_covid_txt", "P");
		} else {
			parameters.put("P_covid_txt", null);
		}
		con = ConnectionManager.getConnection();
		JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

		JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
		jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
		byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);

		InputStream stream = new ByteArrayInputStream(pdfByteArray);
        file = new DefaultStreamedContent(stream, "Application/pdf", "complainInquiry.pdf");
        
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();

		sessionMap.put("reportBytes", pdfByteArray);
		sessionMap.put("docType", "pdf");

		ConnectionManager.close(con);

		return file;

	}

	public StreamedContent downloadInquiryLettersForOthers() throws JRException {
		String valueForPrint = null;
		valueForPrint = selectedData.getComplainNo();

		file = null;

		String sourceFileName = null;

		List<String> reports = new ArrayList<>();
		String offence = manageInquiryService.getOffenceInSinhala(selectedData.getComplainNo());
		Connection con = null;

		String covidText = null;
		try {
			covidText = PropertyReader.getPropertyValue("covid.advice.text");
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sourceFileName = "..//reports//PublicComplainInquiryLetterForTP.jrxml";
		String subReportPath = "lk/informatics/ntc/view/reports/";

		// Parameters for report
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("P_Complain_no", valueForPrint);
		parameters.put("P_offence", offence);
		parameters.put("SUBREPORT_PATH", subReportPath);

		con = ConnectionManager.getConnection();
		JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

		JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
		jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
		byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);

		InputStream stream = new ByteArrayInputStream(pdfByteArray);
        file = new DefaultStreamedContent(stream, "Application/pdf", "complainInquiry.pdf");
        
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();

		sessionMap.put("reportBytes", pdfByteArray);
		sessionMap.put("docType", "pdf");

		ConnectionManager.close(con);

		return file;

	}

	public StreamedContent downloadWarningLetter() throws JRException {

		String valueForPrint = null;
		valueForPrint = selectDTOList.get(0).getComplainNo();

		file = null;

		String sourceFileName = null;

		List<String> reports = new ArrayList<>();
		String offence = manageInquiryService.getOffenceInSinhala(selectDTOList.get(0).getComplainNo());
		Connection con = null;
		String covidText = null;
		try {
			covidText = PropertyReader.getPropertyValue("covid.advice.text");
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sourceFileName = "..//reports//PublicComplainWarningLetter.jrxml";
		String subReportPath = "lk/informatics/ntc/view/reports/";

		// Parameters for report
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("P_Complain_no", valueForPrint);
		parameters.put("P_offence", offence);
		parameters.put("SUBREPORT_PATH", subReportPath);
		if (covidText.equalsIgnoreCase("print")) {
			parameters.put("P_covid_txt", "P");
		} else {
			parameters.put("P_covid_txt", null);
		}
		con = ConnectionManager.getConnection();
		JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

		JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
		jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
		byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);

		InputStream stream = new ByteArrayInputStream(pdfByteArray);
        file = new DefaultStreamedContent(stream, "Application/pdf", "complainWarning.pdf");
        
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();

		sessionMap.put("reportBytes", pdfByteArray);
		sessionMap.put("docType", "pdf");

		ConnectionManager.close(con);

		return file;
	}

	public StreamedContent downloadWarningLetterForTP() throws JRException {

		String valueForPrint = null;
		valueForPrint = selectDTOList.get(0).getComplainNo();

		file = null;

		String sourceFileName = null;

		List<String> reports = new ArrayList<>();
		String offence = manageInquiryService.getOffenceInSinhala(selectDTOList.get(0).getComplainNo());
		Connection con = null;
		String covidText = null;
		try {
			covidText = PropertyReader.getPropertyValue("covid.advice.text");
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sourceFileName = "..//reports//PublicComplainWarningLetterForTP.jrxml";
		String subReportPath = "lk/informatics/ntc/view/reports/";

		// Parameters for report
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("P_Complain_no", valueForPrint);
		parameters.put("P_offence", offence);
		parameters.put("SUBREPORT_PATH", subReportPath);

		con = ConnectionManager.getConnection();
		JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

		JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
		jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
		byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);

		InputStream stream = new ByteArrayInputStream(pdfByteArray);
        file = new DefaultStreamedContent(stream, "Application/pdf", "complainWarning.pdf");
        
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();

		sessionMap.put("reportBytes", pdfByteArray);
		sessionMap.put("docType", "pdf");

		ConnectionManager.close(con);

		return file;
	}

	/** Permit Owner Warning Letter End **/

	public void selectRow() {
		if (selectDTOList.size() == 1) {
			specialRemark = selectDTOList.get(0).getSpecialRemark();
		} else {
			specialRemark = null;
		}
	}

	public void viewVehicleInfoAction() {

		vehicleInfoData = manageInquiryService.retrieveVehicleInfo(selectedData.getVehicleNum());

		RequestContext.getCurrentInstance().update("vehicelInfoDlg");
		RequestContext.getCurrentInstance().execute("PF('vehicelInfoDlg').show()");
	}

	public void checkTimeAvailablityAction() {

		setTimeBtnRender = false;
		availableTimesList = new ArrayList<ManageInquiryDTO>();

		ManageInquiryDTO dto = new ManageInquiryDTO();
		dto.setStartTime("08:00");
		dto.setEndTime("08:05");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:05");
		dto.setEndTime("08:10");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:10");
		dto.setEndTime("08:15");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:15");
		dto.setEndTime("08:20");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:20");
		dto.setEndTime("08:25");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:25");
		dto.setEndTime("08:30");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:30");
		dto.setEndTime("08:35");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:35");
		dto.setEndTime("08:40");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:40");
		dto.setEndTime("08:45");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:45");
		dto.setEndTime("08:50");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:50");
		dto.setEndTime("08:55");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("08:55");
		dto.setEndTime("09:00");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:00");
		dto.setEndTime("09:05");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:05");
		dto.setEndTime("09:10");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:10");
		dto.setEndTime("09:15");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:15");
		dto.setEndTime("09:20");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:20");
		dto.setEndTime("09:25");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:25");
		dto.setEndTime("09:30");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:30");
		dto.setEndTime("09:35");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:35");
		dto.setEndTime("09:40");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:40");
		dto.setEndTime("09:45");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:45");
		dto.setEndTime("09:50");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:50");
		dto.setEndTime("09:55");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("09:55");
		dto.setEndTime("10:00");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:00");
		dto.setEndTime("10:05");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:05");
		dto.setEndTime("10:10");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:10");
		dto.setEndTime("10:15");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:15");
		dto.setEndTime("10:20");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:20");
		dto.setEndTime("10:25");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:25");
		dto.setEndTime("10:30");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:30");
		dto.setEndTime("10:35");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:35");
		dto.setEndTime("10:40");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:40");
		dto.setEndTime("10:45");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:45");
		dto.setEndTime("10:50");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:50");
		dto.setEndTime("10:55");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("10:55");
		dto.setEndTime("11:00");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:00");
		dto.setEndTime("11:05");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:05");
		dto.setEndTime("11:10");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:10");
		dto.setEndTime("11:15");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:15");
		dto.setEndTime("11:20");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:20");
		dto.setEndTime("11:25");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:25");
		dto.setEndTime("11:30");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:30");
		dto.setEndTime("11:35");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:35");
		dto.setEndTime("11:40");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:40");
		dto.setEndTime("11:45");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:45");
		dto.setEndTime("11:50");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:50");
		dto.setEndTime("11:55");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("11:55");
		dto.setEndTime("12:00");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:00");
		dto.setEndTime("12:05");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:05");
		dto.setEndTime("12:10");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:10");
		dto.setEndTime("12:15");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:15");
		dto.setEndTime("12:20");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:20");
		dto.setEndTime("12:25");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:25");
		dto.setEndTime("12:30");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:30");
		dto.setEndTime("12:35");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:35");
		dto.setEndTime("12:40");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:40");
		dto.setEndTime("12:45");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:45");
		dto.setEndTime("12:50");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:50");
		dto.setEndTime("12:55");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("12:55");
		dto.setEndTime("01:00");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:00");
		dto.setEndTime("01:05");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:05");
		dto.setEndTime("01:10");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:10");
		dto.setEndTime("01:15");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:15");
		dto.setEndTime("01:20");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:20");
		dto.setEndTime("01:25");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:25");
		dto.setEndTime("01:30");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:30");
		dto.setEndTime("01:35");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:35");
		dto.setEndTime("01:40");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:40");
		dto.setEndTime("01:45");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:45");
		dto.setEndTime("01:50");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:50");
		dto.setEndTime("01:55");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("01:55");
		dto.setEndTime("02:00");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:00");
		dto.setEndTime("02:05");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:05");
		dto.setEndTime("02:10");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:10");
		dto.setEndTime("02:15");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:15");
		dto.setEndTime("02:20");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:20");
		dto.setEndTime("02:25");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:25");
		dto.setEndTime("02:30");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:30");
		dto.setEndTime("02:35");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:35");
		dto.setEndTime("02:40");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:40");
		dto.setEndTime("02:45");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:45");
		dto.setEndTime("02:50");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:50");
		dto.setEndTime("02:55");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("02:55");
		dto.setEndTime("03:00");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:00");
		dto.setEndTime("03:05");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:05");
		dto.setEndTime("03:10");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:10");
		dto.setEndTime("03:15");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:15");
		dto.setEndTime("03:20");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:20");
		dto.setEndTime("03:25");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:25");
		dto.setEndTime("03:30");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:30");
		dto.setEndTime("03:35");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:35");
		dto.setEndTime("03:40");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:40");
		dto.setEndTime("03:45");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:45");
		dto.setEndTime("03:50");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:50");
		dto.setEndTime("03:55");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("03:55");
		dto.setEndTime("04:00");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("04:00");
		dto.setEndTime("04:05");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("04:05");
		dto.setEndTime("04:10");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("04:10");
		dto.setEndTime("04:15");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("04:15");
		dto.setEndTime("04:20");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("04:20");
		dto.setEndTime("04:25");
		availableTimesList.add(dto);

		dto = new ManageInquiryDTO();
		dto.setStartTime("04:25");
		dto.setEndTime("04:30");
		availableTimesList.add(dto);

		lunchstartTime = "";
		lunchendTime = "";
		try {

			ParamerDTO paramDTO = new ParamerDTO();
			paramDTO = migratedService.retrieveParameterValuesForParamName("GRIEVE_MANAGE_INQUIRY_LUNCH_TIME");

			String time = paramDTO.getStringValue();
			lunchstartTime = time.substring(0, 5);
			lunchendTime = time.substring(6, 11);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		/** get lunch time start **/
		boolean lunchStarted = false;
		boolean lunchEnded = false;
		List<ManageInquiryDTO> tempDtoList = new ArrayList<ManageInquiryDTO>();
		for (ManageInquiryDTO manageInquiryDTO : availableTimesList) {
			ManageInquiryDTO tempDto = new ManageInquiryDTO();

			if (manageInquiryDTO.getStartTime().equals(lunchstartTime)) {
				lunchStarted = true;
			}

			if (manageInquiryDTO.getStartTime().equals(lunchendTime)) {
				lunchEnded = true;
			}

			if (lunchStarted && !lunchEnded) {
				tempDto = manageInquiryDTO;
				tempDto.setLunchTime(true);
				tempDto.setTimeTaken(true);
			} else {
				tempDto = manageInquiryDTO;
			}

			tempDtoList.add(tempDto);
		}

		availableTimesList = new ArrayList<ManageInquiryDTO>();
		availableTimesList = tempDtoList;
		/** get lunch time start **/

		manageInquiryDTO.setAvailableDateSelect(null);
		RequestContext.getCurrentInstance().update("checkAvailabilityDlg");
		RequestContext.getCurrentInstance().execute("PF('checkAvailabilityDlg').show()");

	}

	public void setTimeConfirmAction() {

		RequestContext.getCurrentInstance().execute("PF('confirmTimeDlg').show()");
	}

	public void setTimeForIvestigationBtn() {

		selectedData.setAvailableDateSelect(manageInquiryDTO.getAvailableDateSelect());
		manageInquiryService.updateInvetigationData(selectedData, selectedTime.getStartTime(),
				selectedTime.getEndTime(), sessionBackingBean.getLoginUser());

		manageInquiryDTOList = new ArrayList<ManageInquiryDTO>();
		manageInquiryDTOList = manageInquiryService.retrieveInquiryComplaintData(manageInquiryDTO,sessionBackingBean.getLoginUser());

		RequestContext.getCurrentInstance().execute("PF('checkAvailabilityDlg').hide()");
		RequestContext.getCurrentInstance().execute("PF('confirmTimeDlg').hide()");

		RequestContext.getCurrentInstance().update("manageInqDT");

		setSuccessMessage("Time set for investigation successfully.");
		RequestContext.getCurrentInstance().update("successMSG");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

	}

	public void onDateSelect(SelectEvent event) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String selectedDate = format.format(event.getObject());

		List<ManageInquiryDTO> addedDtoList = new ArrayList<ManageInquiryDTO>();

		List<ManageInquiryDTO> tempDtoList = new ArrayList<ManageInquiryDTO>();

		tempDtoList = manageInquiryService.retreiveReservedTimes(selectedDate);

		boolean found = false;
		boolean lunchStarted = false;
		boolean lunchEnded = false;

		for (ManageInquiryDTO timeDto : availableTimesList) {
			found = false;
			setTimeBtnRender = true;
			for (ManageInquiryDTO reservedTime : tempDtoList) {
				if (reservedTime.getStartTime().equals(timeDto.getStartTime())
						&& reservedTime.getEndTime().equals(timeDto.getEndTime())) {
					ManageInquiryDTO tempDto = new ManageInquiryDTO();
					tempDto = timeDto;
					tempDto.setTimeTaken(true);
					addedDtoList.add(tempDto);
					found = true;
				}
			}

			ManageInquiryDTO tempDto = new ManageInquiryDTO();

			if (timeDto.getStartTime().equals(lunchstartTime)) {
				lunchStarted = true;
			}

			if (timeDto.getStartTime().equals(lunchendTime)) {
				lunchEnded = true;
			}

			if (lunchStarted && !lunchEnded) {
				tempDto = timeDto;
				tempDto.setLunchTime(true);
				tempDto.setTimeTaken(true);
				addedDtoList.add(tempDto);
				found = true;
			}

			if (!found) {
				timeDto.setTimeTaken(false);
				addedDtoList.add(timeDto);
			}
		}

		availableTimesList = new ArrayList<ManageInquiryDTO>();
		availableTimesList = addedDtoList;

	}

	public void checkDateTimeDeletionAction() {
		RequestContext.getCurrentInstance().execute("PF('confirmDeleteDlg').show()");
	}

	public void deleteInvestigationBtnAction() {

		selectedData.setAvailableDateSelect(manageInquiryDTO.getAvailableDateSelect());
		selectedData.setComplainNo(selectedData.getComplainNo());
		manageInquiryService.deleteInvetigationDataAndTime(selectedData, selectedTime.getStartTime(),
				selectedTime.getEndTime(), sessionBackingBean.getLoginUser());

		manageInquiryDTOList = new ArrayList<ManageInquiryDTO>();
		manageInquiryDTOList = manageInquiryService.retrieveInquiryComplaintData(manageInquiryDTO,sessionBackingBean.getLoginUser());

		RequestContext.getCurrentInstance().execute("PF('confirmDeleteDlg').hide()");
		RequestContext.getCurrentInstance().update("manageInqDT");

		manageInquiryService.beanLinkMethod(selectedData, sessionBackingBean.loginUser, "Delete Investigation", "Manage Inquiry");
		setSuccessMessage("Investigation Date and Time for investigation successfully deleted.");
		RequestContext.getCurrentInstance().update("successMSG");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
	}

	public void sendSMSButtonnAction() {
		conductorDriverList = new ArrayList<ManageInquiryDTO>();
		conductorDriverList = manageInquiryService.retrieveBusDriverConductorData(selectedData.getVehicleNum(),
				selectedData.getPermitNum());
		sendMailDTO = new ManageInquiryDTO();
		sendMailDTO = manageInquiryService.retrieveSMSDetails(selectedData.getComplainNo());
		RequestContext.getCurrentInstance().update("sendSMSDlg:smsDlgFrm");
		RequestContext.getCurrentInstance().execute("PF('sendSMSDlg').show()");
	}

	public void saveSendSMSBtnAction() {
		sendMailDTO.setComplainNo(selectedData.getComplainNo());
		if (sendMailDTO.getContactName() != null && !sendMailDTO.getContactName().isEmpty()
				&& !sendMailDTO.getContactName().trim().equals("")) {
			if (sendMailDTO.getContactNum() != null && !sendMailDTO.getContactNum().isEmpty()
					&& !sendMailDTO.getContactNum().trim().equals("")) {
				if (sendMailDTO.getNotifySentenceOne() != null && !sendMailDTO.getNotifySentenceOne().isEmpty()
						&& !sendMailDTO.getNotifySentenceOne().trim().equals("")) {

					if (sendMailDTO.getEmail() != null && !sendMailDTO.getEmail().isEmpty()
							&& !sendMailDTO.getEmail().trim().equals("")) {
						if (sendMailDTO.getNotifySentenceTwo() != null && !sendMailDTO.getNotifySentenceTwo().isEmpty()
								&& !sendMailDTO.getNotifySentenceTwo().trim().equals("")
								|| sendMailDTO.getNotifySentenceThree() != null
										&& !sendMailDTO.getNotifySentenceThree().isEmpty()
										&& !sendMailDTO.getNotifySentenceThree().trim().equals("")
								|| sendMailDTO.getNotifySentencFour() != null
										&& !sendMailDTO.getNotifySentencFour().isEmpty()
										&& !sendMailDTO.getNotifySentencFour().trim().equals("")) {

							manageInquiryService.sendMail(sendMailDTO, sessionBackingBean.getLoginUser());

						} else {
							setErrorMessage("Email cannot be empty.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							return;
						}
					}
					boolean saved = manageInquiryService.saveSMSData(sendMailDTO, sessionBackingBean.getLoginUser());

					if (saved) {
						RequestContext.getCurrentInstance().execute("PF('sendSMSDlg').hide()");

						setSuccessMessage("Data saved successfully.");
						RequestContext.getCurrentInstance().update("successMSG");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					} else {
						setErrorMessage("Data cannot be saved.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please enter a message.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please enter Contact Number.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select Contact Name.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearSendSMSBtnAction() {
		sendMailDTO = new ManageInquiryDTO();
	}

	public void savePrintLetterBtnAction() {

		if (sendMailDTO.getContactName() != null && !sendMailDTO.getContactName().isEmpty()
				&& !sendMailDTO.getContactName().trim().equals("")) {
			if (sendMailDTO.getContactAddress() != null && !sendMailDTO.getContactAddress().isEmpty()
					&& !sendMailDTO.getContactAddress().trim().equals("")) {
				if (sendMailDTO.getNotifySentenceOne() != null && !sendMailDTO.getNotifySentenceOne().isEmpty()
						&& !sendMailDTO.getNotifySentenceOne().trim().equals("")
						|| sendMailDTO.getNotifySentenceTwo() != null && !sendMailDTO.getNotifySentenceTwo().isEmpty()
								&& !sendMailDTO.getNotifySentenceTwo().trim().equals("")
						|| sendMailDTO.getNotifySentenceThree() != null
								&& !sendMailDTO.getNotifySentenceThree().isEmpty()
								&& !sendMailDTO.getNotifySentenceThree().trim().equals("")) {
					sendMailDTO.setComplainNo(selectedData.getComplainNo());
					boolean saved = manageInquiryService.savePrintLetterData(sendMailDTO,
							sessionBackingBean.getLoginUser());

					if (saved) {
						RequestContext.getCurrentInstance().execute("PF('printLetterDlg').hide()");

						setSuccessMessage("Data saved successfully.");
						RequestContext.getCurrentInstance().update("successMSG");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					} else {
						setErrorMessage("Data cannot be saved.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please enter a message");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please enter Contact Address.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select Contact Name.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearPrintLetterBtnAction() {
		sendMailDTO = new ManageInquiryDTO();
	}

	public void onConducterDriverSelectShowContactNum() {

		String contactNum = null;
		if (conductorDriverList != null && !conductorDriverList.isEmpty() && conductorDriverList.size() != 0) {
			for (ManageInquiryDTO dto : conductorDriverList) {
				if (dto.getContactName().equals(sendMailDTO.getContactName())) {
					contactNum = dto.getContactNum();
				}
			}
		}

		try {
			if (sendMailDTO.getContactName() != null && !sendMailDTO.getContactName().isEmpty()
					&& !sendMailDTO.getContactName().trim().equals("")) {

				if (contactNum != null && !contactNum.isEmpty() && !contactNum.trim().equals("")) {
					sendMailDTO.setContactNum(contactNum);

					boolean exist = manageInquiryService
							.checkDataAvailableInNt_r_investigation_alerts(selectedData.getComplainNo(), false);

					if (exist) {
						sendMailDTO = manageInquiryService.retrieveSMSDetails(selectedData.getComplainNo());
					} else {

						ParamerDTO sentenceSMSDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_SMS");
						if (sentenceSMSDTO != null && sentenceSMSDTO.getStringValue() != null
								&& !sentenceSMSDTO.getStringValue().isEmpty()
								&& !sentenceSMSDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceOne(sentenceSMSDTO.getStringValue());
						}

						ParamerDTO sentenceOneDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_ONE");
						ParamerDTO sentencTwoDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_TWO");
						ParamerDTO sentenceThreeDTO = migratedService
								.retrieveParameterValuesForParamName("GRIEVANCE_MNG_INQ_LETTER_SENTENCE_THREE");

						if (sentenceOneDTO != null && sentenceOneDTO.getStringValue() != null
								&& !sentenceOneDTO.getStringValue().isEmpty()
								&& !sentenceOneDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceTwo(sentenceOneDTO.getStringValue());
						}
						if (sentencTwoDTO != null && sentencTwoDTO.getStringValue() != null
								&& !sentencTwoDTO.getStringValue().isEmpty()
								&& !sentencTwoDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentenceThree(sentencTwoDTO.getStringValue());
						}
						if (sentenceThreeDTO != null && sentenceThreeDTO.getStringValue() != null
								&& !sentenceThreeDTO.getStringValue().isEmpty()
								&& !sentenceThreeDTO.getStringValue().trim().equals("")) {
							sendMailDTO.setNotifySentencFour(sentenceThreeDTO.getStringValue());
						}
					}

				}

				RequestContext.getCurrentInstance().update("sendSMSDlg:smsDlgFrm");
			} else {
				setErrorMessage("Please select the Contact Name.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public void complaintAction() {
		selectedComplaintDTO = new ComplaintRequestDTO();
		selectedComplaintDTO = manageInquiryService.getComplaintDetails(selectedData.getComplainNo(),
				selectedData.getVehicleNum(), selectedData.getPermitNum());
		RequestContext.getCurrentInstance().update("complaintDlg");
		RequestContext.getCurrentInstance().execute("PF('complaintDlg').show()");
	}

	public void viewActionTaken() {
		actionTakenList = new ArrayList<ManageInquiryDTO>();
		actionTakenList = manageInquiryService.getActionDetails(selectedData.getComplainNo());
		RequestContext.getCurrentInstance().update("actionTakenDialog");
		RequestContext.getCurrentInstance().execute("PF('actionTakenDialog').show()");
	}

	public void ptactbBtnAction() {
		if (selectDTOList.size() > 0) {
			if (selectDTOList.size() > 1) {
				if (manageInquiryDTO.getAuthority() != null && !manageInquiryDTO.getAuthority().trim().equals("")) {
					if (manageInquiryDTO.getAuthority().trim().equals("PTA")
							|| manageInquiryDTO.getAuthority().trim().equals("CTB")) {
						boolean sent = manageInquiryService.sendPtaCtbMail(selectDTOList,
								sessionBackingBean.getLoginUser(),selectedData);
						if (sent) {
							searchButtonAction();
							manageInquiryService.beanLinkMethod(manageInquiryDTO, sessionBackingBean.loginUser, "Email Sent", "Manage Inquiry");
							setSuccessMessage("Emails sent successfully.");
							RequestContext.getCurrentInstance().update("successMSG");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						}
					} else {
						setErrorMessage("Invalid action.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Permit authority should be selected to process multiple records.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				ManageInquiryDTO selectedDTO = selectDTOList.get(0);
				if (selectedDTO.getAuthority() != null && !selectedDTO.getAuthority().trim().equals("")
						&& (selectedDTO.getAuthority().trim().equals("PTA")
								|| selectedDTO.getAuthority().trim().equals("CTB"))) {
					if (selectedDTO.getProcess_status().equals("P") || selectedDTO.getProcess_status().equals("O")) {
						boolean sent = manageInquiryService.sendPtaCtbMail(selectDTOList,
								sessionBackingBean.getLoginUser(),selectedData);
						if (sent) {
							searchButtonAction();
							manageInquiryService.beanLinkMethod(manageInquiryDTO, sessionBackingBean.loginUser, "Email Sent", "Manage Inquiry");
							setSuccessMessage("Email sent successfully.");
							RequestContext.getCurrentInstance().update("successMSG");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						}
					} else {
						setErrorMessage("Selected record is already closed.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Invalid action.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			}
		} else {
			setErrorMessage("Please select a record/records from the table.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void sendBtnAction() {
		if (selectDTOList.size() > 0) {
			if (selectDTOList.size() == 1) {
				if (selectDTOList.get(0).getAuthority() != null
						&& !selectDTOList.get(0).getAuthority().trim().equals("")
						&& !selectDTOList.get(0).getAuthority().trim().equals("PTA")
						&& !selectDTOList.get(0).getAuthority().trim().equals("CTB")) {
					if (selectDTOList.get(0).getProcess_status().equals("P")
							|| selectDTOList.get(0).getProcess_status().equals("O")) {
						if (selectDTOList.get(0).getActionDepartment() != null
								&& !selectDTOList.get(0).getActionDepartment().trim().equals("")) {
							if (selectDTOList.get(0).getActionStatus() == null
									|| selectDTOList.get(0).getActionStatus().trim().equals("")) {
								// ADD special remark to the only DTO
								selectDTOList.get(0).setSpecialRemark(specialRemark);
								boolean success = manageInquiryService.assignToActionDepartment(selectedData, selectDTOList,
										sessionBackingBean.getLoginUser());
								if (success) {
									searchButtonAction();
									manageInquiryService.beanLinkMethod(selectedData, sessionBackingBean.loginUser, "Assign Action To The Department", "Manage Inquiry");
									setSuccessMessage("Transfered to the department successfully.");
									RequestContext.getCurrentInstance().update("successMSG");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								}
							} else {
								setErrorMessage("The selected record has already been transferred to a department.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
						} else {
							setErrorMessage("A department should be selected from the dropdown.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					} else {
						setErrorMessage("Selected record is already closed.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Invalid action.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Invalid action. Multiple selections are not allowed.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select a record from the table.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void documentManagement() {
		try {
			sessionBackingBean.sisuSariyaMandatoryDocumentList = new ArrayList<DocumentManagementDTO>();
			sessionBackingBean.sisuSariyaOptionalDocumentList = new ArrayList<DocumentManagementDTO>();
			mandatoryList = new ArrayList<DocumentManagementDTO>(0);
			optionalList = new ArrayList<DocumentManagementDTO>(0);

			sessionBackingBean.setComplainNo(selectedData.getComplainNo());

			String strTransactionType = "24";
			sessionBackingBean.setTransactionType("COMPLAIN");

			setMandatoryList(documentManagementService.mandatoryDocsForGrievance(strTransactionType,
					selectedData.getComplainNo()));
			setOptionalList(documentManagementService.optionalDocsForGrievance(strTransactionType,
					selectedData.getComplainNo()));

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.grievanceMandatoryListM(selectedData.getComplainNo(), strTransactionType);
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.grievanceOptionalListM(selectedData.getComplainNo(), strTransactionType);

			RequestContext.getCurrentInstance().update("uploadDocumentsDialog");
			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ManageInquiryDTO getManageInquiryDTO() {
		return manageInquiryDTO;
	}

	public void setManageInquiryDTO(ManageInquiryDTO manageInquiryDTO) {
		this.manageInquiryDTO = manageInquiryDTO;
	}

	public List<String> getComplaintNumList() {
		return complaintNumList;
	}

	public void setComplaintNumList(List<String> complaintNumList) {
		this.complaintNumList = complaintNumList;
	}

	public List<ManageInquiryDTO> getPriorityOrderList() {
		return priorityOrderList;
	}

	public void setPriorityOrderList(List<ManageInquiryDTO> priorityOrderList) {
		this.priorityOrderList = priorityOrderList;
	}

	public List<ManageInquiryDTO> getSeverityList() {
		return severityList;
	}

	public void setSeverityList(List<ManageInquiryDTO> severityList) {
		this.severityList = severityList;
	}

	public List<ManageInquiryDTO> getManageInquiryDTOList() {
		return manageInquiryDTOList;
	}

	public void setManageInquiryDTOList(List<ManageInquiryDTO> manageInquiryDTOList) {
		this.manageInquiryDTOList = manageInquiryDTOList;
	}

	public ManageInquiryService getManageInquiryService() {
		return manageInquiryService;
	}

	public void setManageInquiryService(ManageInquiryService manageInquiryService) {
		this.manageInquiryService = manageInquiryService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public ManageInquiryDTO getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(ManageInquiryDTO selectedData) {
		this.selectedData = selectedData;
	}

	public VehicleInspectionDTO getVehicleInfoData() {
		return vehicleInfoData;
	}

	public void setVehicleInfoData(VehicleInspectionDTO vehicleInfoData) {
		this.vehicleInfoData = vehicleInfoData;
	}

	public List<ManageInquiryDTO> getAvailableTimesList() {
		return availableTimesList;
	}

	public void setAvailableTimesList(List<ManageInquiryDTO> availableTimesList) {
		this.availableTimesList = availableTimesList;
	}

	public boolean isMainDataTableShow() {
		return mainDataTableShow;
	}

	public void setMainDataTableShow(boolean mainDataTableShow) {
		this.mainDataTableShow = mainDataTableShow;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public ManageInquiryDTO getSelectedTime() {
		return selectedTime;
	}

	public void setSelectedTime(ManageInquiryDTO selectedTime) {
		this.selectedTime = selectedTime;
	}

	public boolean isSetTimeBtnRender() {
		return setTimeBtnRender;
	}

	public void setSetTimeBtnRender(boolean setTimeBtnRender) {
		this.setTimeBtnRender = setTimeBtnRender;
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public ManageInquiryDTO getSendMailDTO() {
		return sendMailDTO;
	}

	public void setSendMailDTO(ManageInquiryDTO sendMailDTO) {
		this.sendMailDTO = sendMailDTO;
	}

	public String getLunchstartTime() {
		return lunchstartTime;
	}

	public void setLunchstartTime(String lunchstartTime) {
		this.lunchstartTime = lunchstartTime;
	}

	public String getLunchendTime() {
		return lunchendTime;
	}

	public void setLunchendTime(String lunchendTime) {
		this.lunchendTime = lunchendTime;
	}

	public ComplaintRequestDTO getSelectedComplaintDTO() {
		return selectedComplaintDTO;
	}

	public void setSelectedComplaintDTO(ComplaintRequestDTO selectedComplaintDTO) {
		this.selectedComplaintDTO = selectedComplaintDTO;
	}

	public boolean isOldContact() {
		return oldContact;
	}

	public void setOldContact(boolean oldContact) {
		this.oldContact = oldContact;
	}

	public List<ManageInquiryDTO> getConductorDriverList() {
		return conductorDriverList;
	}

	public void setConductorDriverList(List<ManageInquiryDTO> conductorDriverList) {
		this.conductorDriverList = conductorDriverList;
	}

	public boolean isShowbutton() {
		return showbutton;
	}

	public void setShowbutton(boolean showbutton) {
		this.showbutton = showbutton;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public List<String> getVehicleNumList() {
		return vehicleNumList;
	}

	public void setVehicleNumList(List<String> vehicleNumList) {
		this.vehicleNumList = vehicleNumList;
	}

	public boolean isPrintWarningDisable() {
		return printWarningDisable;
	}

	public void setPrintWarningDisable(boolean printWarningDisable) {
		this.printWarningDisable = printWarningDisable;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<CommonDTO> getProvincelList() {
		return provincelList;
	}

	public void setProvincelList(List<CommonDTO> provincelList) {
		this.provincelList = provincelList;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public boolean isDisableProvinceSearch() {
		return disableProvinceSearch;
	}

	public void setDisableProvinceSearch(boolean disableProvinceSearch) {
		this.disableProvinceSearch = disableProvinceSearch;
	}

	public boolean isDisablePtaCtbBtn() {
		return disablePtaCtbBtn;
	}

	public void setDisablePtaCtbBtn(boolean disablePtaCtbBtn) {
		this.disablePtaCtbBtn = disablePtaCtbBtn;
	}

	public List<ManageInquiryDTO> getSelectDTOList() {
		return selectDTOList;
	}

	public void setSelectDTOList(List<ManageInquiryDTO> selectDTOList) {
		this.selectDTOList = selectDTOList;
	}

	public List<ManageInquiryDTO> getActionDepartmentsList() {
		return actionDepartmentsList;
	}

	public void setActionDepartmentsList(List<ManageInquiryDTO> actionDepartmentsList) {
		this.actionDepartmentsList = actionDepartmentsList;
	}

	public boolean isRenderCloseBtn() {
		return renderCloseBtn;
	}

	public void setRenderCloseBtn(boolean renderCloseBtn) {
		this.renderCloseBtn = renderCloseBtn;
	}

	public List<ManageInquiryDTO> getActionTakenList() {
		return actionTakenList;
	}

	public void setActionTakenList(List<ManageInquiryDTO> actionTakenList) {
		this.actionTakenList = actionTakenList;
	}

	public boolean isDisableSearchOptions() {
		return disableSearchOptions;
	}

	public void setDisableSearchOptions(boolean disableSearchOptions) {
		this.disableSearchOptions = disableSearchOptions;
	}

	public String getSpecialRemark() {
		return specialRemark;
	}

	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
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

	public String getSuccessMessage() {
		return successMessage;
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

}
