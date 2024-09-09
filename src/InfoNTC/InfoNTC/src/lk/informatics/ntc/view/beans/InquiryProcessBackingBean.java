package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.AccessPermissionDTO;
import lk.informatics.ntc.model.dto.CommittedOffencesDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.service.AccessPermissionService;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.ApproveActionChargesService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.GrievanceManagementService;
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

/**
 * 
 * @author dilakshi.h
 *
 */
@ViewScoped
@ManagedBean(name = "inquiryProcessBackingBean")
public class InquiryProcessBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private GrievanceManagementService grievanceManagementService;
	private CommonService commonService;
	private AdminService adminService;
	private AccessPermissionService accessPermissionService;
	private DocumentManagementService documentManagementService;
	
	private ApproveActionChargesService approveActionChargesService;

	private String sucessMsg;
	private String errorMsg;

	private String dateFormatStr = "dd/MM/yyyy";
	private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);

	private List<String> complaintNoList;
	private String complaintNo;
	private Date investigationFrom;
	private Date investigationTo;
	private boolean complaintSelected;
	private boolean newDriver;
	private boolean newConductor;
	private List<ComplaintRequestDTO> complaintDetailDTOList = new ArrayList<ComplaintRequestDTO>();
	private ComplaintRequestDTO selectedComplaintDTO = new ComplaintRequestDTO();
	private List<DropDownDTO> driverList;
	private List<DropDownDTO> conductorList;
	private DriverConductorRegistrationDTO driverData = new DriverConductorRegistrationDTO();
	private DriverConductorRegistrationDTO conductorData = new DriverConductorRegistrationDTO();
	private BigDecimal totDriverPoints;
	private BigDecimal totConductorPoints;
	// private boolean driverConductorDataSaved = true;
	private StreamedContent file;
	private String voucherVal;
	private boolean disablePrint;
	private String existingVoucherVal;
	private boolean disableRePrint;
	// complain history dialog
	private String registeredDate;
	private List<ComplaintRequestDTO> complaintHistoryList;
	private List<CommittedOffencesDTO> violationConditionList;
	private boolean historySelected;
	private ComplaintRequestDTO selectedHistory;

	// action dialog
	private List<DropDownDTO> actionList;
	private List<AccessPermissionDTO> deptList;

	// charge sheet
	private BigDecimal totalAmt;
	private BigDecimal totalDriverPoints;
	private BigDecimal totalConductorPoints;

	private String[] warningLetterPersons;

	private String viewType; // O,D,C
	static String OWNER = "O";
	static String DRIVER = "D";
	static String CONDUCTOR = "C";

	private boolean chargeSheetGenerated;

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	public InquiryProcessBackingBean() {
		grievanceManagementService = (GrievanceManagementService) SpringApplicationContex
				.getBean("grievanceManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		accessPermissionService = (AccessPermissionService) SpringApplicationContex.getBean("accessPermissionService");
		complaintNoList = grievanceManagementService.getAllComplainNo();
		driverList = grievanceManagementService.getInquiryDrivers();
		conductorList = grievanceManagementService.getInquiryConductors();
		approveActionChargesService = (ApproveActionChargesService) SpringApplicationContex
				.getBean("approveActionChargesService");
		disablePrint = true;
	}

	public List<String> completeComplaintNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < complaintNoList.size(); i++) {
			String cm = complaintNoList.get(i);
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}

		return filteredNo;
	}

	public void searchComplaint() {
		if ((complaintNo == null || complaintNo.trim().isEmpty()) && investigationFrom == null
				&& investigationTo == null) {
			errorMsg = "Please enter a value first.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}

		if (complaintNo != null && !complaintNo.trim().isEmpty()) {
			complaintNo = complaintNo.toUpperCase();
			complaintDetailDTOList = grievanceManagementService.getComplaintDetails(complaintNo, null, null);
		}
		if (complaintDetailDTOList.isEmpty() && (investigationFrom != null || investigationTo != null)) {
			complaintDetailDTOList = grievanceManagementService.getComplaintListByInvestigationDate(investigationFrom,
					investigationTo);
		}
		if (complaintDetailDTOList.isEmpty()) {
			showMsg("ERROR", "No Data found.");
			return;
		}

		if (complaintDetailDTOList.size() == 1) {
			selectedComplaintDTO = complaintDetailDTOList.get(0);
			selectOneComplaint();
		}
	}

	public void selectOneComplaint() {
		complaintSelected = true;
		String[] data = commonService.getOwnerByVehicleNo(selectedComplaintDTO.getVehicleNo());
		selectedComplaintDTO.setOwnerName(data[0]);
		selectedComplaintDTO.setOwnerNic(data[1]);
		newDriver = false;
		newConductor = false;
		clearDriverData();
		clearConductorData();
		loadInquiryData();

		existingVoucherVal = grievanceManagementService.getExisteingVoucherNo(selectedComplaintDTO.getComplaintNo());
		if (existingVoucherVal != null) {
			if (selectedComplaintDTO.getApproved() == null) {
				chargeSheetGenerated = true;
			} else {
				chargeSheetGenerated = false;
			}
		} else {
			chargeSheetGenerated = false;
		}
	}

	public void clearSearchView() {
		complaintNo = null;
		investigationFrom = null;
		investigationTo = null;
		complaintSelected = false;
		selectedComplaintDTO = new ComplaintRequestDTO();
		complaintDetailDTOList = new ArrayList<ComplaintRequestDTO>();
		newDriver = false;
		newConductor = false;
		clearDriverData();
		clearConductorData();
		warningLetterPersons = null;
	}

	public void clearDriverData() {
		driverData = new DriverConductorRegistrationDTO();
		totDriverPoints = new BigDecimal(0);
	}

	public void clearConductorData() {
		conductorData = new DriverConductorRegistrationDTO();
		totalConductorPoints = new BigDecimal(0);
	}

	public void loadDriverConductorData(String dc) {
		if (dc.equalsIgnoreCase(DRIVER)) {
			driverData = grievanceManagementService.getDriverConductorData(driverData);
			totDriverPoints = grievanceManagementService.getDriverPointsByComplaint(complaintNo, driverData.getNic());
		} else if (dc.equalsIgnoreCase(CONDUCTOR)) {
			conductorData = grievanceManagementService.getDriverConductorData(conductorData);
			totConductorPoints = grievanceManagementService.getConductorPointsByComplaint(complaintNo,
					conductorData.getNic());
		}
	}

	public void saveInquiryProcess() {
		String user = sessionBackingBean.loginUser;

		if (grievanceManagementService.saveUpdateInquiryProcess(selectedComplaintDTO, driverData, conductorData, user)
				&& grievanceManagementService.updateComplaintStatus(selectedComplaintDTO, "O", user)) {
			grievanceManagementService.beanLinkMethod(selectedComplaintDTO, user, "Save Inquiry Data", "Inquiry Process");
			showMsg("SUCCESS", "Saved Successfully");
		}
		else {
			showMsg("ERROR", "Error");
		}
	}

	public void loadInquiryData() {
		newDriver = false;
		newConductor = false;
		driverData = grievanceManagementService.getComplaintDriver(selectedComplaintDTO.getComplaintNo());
		if ((driverData.getDriverConductorId() == null || driverData.getDriverConductorId().isEmpty())
				&& driverData.getNic() != null)
			newDriver = true;
		totDriverPoints = grievanceManagementService.getDriverPointsByComplaint(complaintNo, driverData.getNic());

		conductorData = grievanceManagementService.getComplaintConductor(selectedComplaintDTO.getComplaintNo());
		if ((conductorData.getDriverConductorId() == null || conductorData.getDriverConductorId().isEmpty())
				&& conductorData.getNic() != null)
			newConductor = true;
		totConductorPoints = grievanceManagementService.getConductorPointsByComplaint(complaintNo,
				conductorData.getNic());
	}

	public void closeComplaint() {
		String user = sessionBackingBean.loginUser;
		if (grievanceManagementService.updateComplaintStatus(selectedComplaintDTO, "C", user)) {
			grievanceManagementService.beanLinkMethod(selectedComplaintDTO, user, "Close Complaint", "Inquiry Process");
			showMsg("SUCCESS", "Saved Successfully");
		}
		else {
			showMsg("ERROR", "Error");
		}
	}

	// COMPLAINT HISTORY
	public void loadComplaintHistory(String dco) {
		viewType = dco;
		registeredDate = null;
		complaintHistoryList = new ArrayList<ComplaintRequestDTO>();
		historySelected = false;
		if (viewType.equalsIgnoreCase(OWNER))
			complaintHistoryList = grievanceManagementService
					.getComplaintHistoryByOwner(selectedComplaintDTO.getVehicleNo());

		if (viewType.equalsIgnoreCase(DRIVER))
			complaintHistoryList = grievanceManagementService
					.getComplaintHistoryByDriver(driverData.getDriverConductorId(), driverData.getNic());

		if (viewType.equalsIgnoreCase(CONDUCTOR))
			complaintHistoryList = grievanceManagementService
					.getComplaintHistoryByConductor(conductorData.getDriverConductorId(), conductorData.getNic());

		// get total offence charge amount
		for (ComplaintRequestDTO com : nullSafe(complaintHistoryList)) {
			BigDecimal totAmount = new BigDecimal(0);
			for (CommittedOffencesDTO off : nullSafe(com.getCommittedOffences()))
				totAmount = totAmount.add(grievanceManagementService.getOffenceCharge(off.getCode()));

			com.setTotalCharge(totAmount);
		}

		RequestContext.getCurrentInstance().execute("PF('dlgComplaintHistory').show()");
	}

	public void selectComplaintHistory() {
		historySelected = true;
		violationConditionList = grievanceManagementService.getCommittedOffencesById(selectedHistory.getComplainSeq(),
				driverData.getDriverConductorId(), conductorData.getDriverConductorId());
	}

	// ACTION
	public void loadActionDialog(String dco) {
		viewType = dco;
		actionList = grievanceManagementService.getActionTypes();
		deptList = accessPermissionService.getDeptToDropdown();
		resetActionDialog();
		RequestContext.getCurrentInstance().execute("PF('dlgAction').show()");
	}

	public void saveActionDialog() {
		String user = sessionBackingBean.loginUser;

		if (grievanceManagementService.saveActionData(viewType, selectedComplaintDTO, user)) {
			grievanceManagementService.beanLinkMethod(selectedComplaintDTO, user, "Save Action Data", "Inquiry Process");
			showMsg("SUCCESS", "Saved Successfully");
			RequestContext.getCurrentInstance().execute("PF('dlgAction').hide();");
		} else {
			showMsg("ERROR", "Error");
		}

	}

	public void resetActionDialog() {
		selectedComplaintDTO = grievanceManagementService.getActionData(viewType, selectedComplaintDTO);
	}

	// CHARGE SHEET
	public void loadChargeSheet() {
		resetChargeSheet();
		RequestContext.getCurrentInstance().execute("PF('dlgChargeSheet').show()");
		existingVoucherVal = grievanceManagementService.getExisteingVoucherNo(selectedComplaintDTO.getComplaintNo());
		/** check data saved or not **/
		String offenceCode = grievanceManagementService.checkDataIntable(selectedComplaintDTO.getComplaintNo());
		if (offenceCode != null) {
			if (existingVoucherVal != null && !existingVoucherVal.isEmpty()) {
				disablePrint = true;
				disableRePrint = false;
			} else {

				disablePrint = false;
				disableRePrint = true;
			}
		} else {
			disablePrint = true;
			disableRePrint = true;
		}
	}

	public void calculateTotAmount() {
		totalAmt = new BigDecimal(0);
		for (CommittedOffencesDTO a : selectedComplaintDTO.getCommittedOffences()) {
			if (a.getCharge() != null)
				totalAmt = totalAmt.add(a.getCharge());
		}
		totalAmt = totalAmt.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	public void calculateDriPoints() {
		totalDriverPoints = new BigDecimal(0);
		for (CommittedOffencesDTO a : selectedComplaintDTO.getCommittedOffences()) {
			if (a.getDriverPoints() != null)
				totalDriverPoints = totalDriverPoints.add(a.getDriverPoints());
		}
		totalDriverPoints = totalDriverPoints.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	public void calculateCondPoints() {
		totalConductorPoints = new BigDecimal(0);
		for (CommittedOffencesDTO a : selectedComplaintDTO.getCommittedOffences()) {
			if (a.getConductorPoints() != null)
				totalConductorPoints = totalConductorPoints.add(a.getConductorPoints());
		}
		totalConductorPoints = totalConductorPoints.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	public void applyDriver(CommittedOffencesDTO offence) {
		//offence.setDriverPoints(null);
		calculateDriPoints();
	}

	public void applyConductor(CommittedOffencesDTO offence) {
		//offence.setConductorPoints(null);
		calculateCondPoints();
	}

	public void saveChargeSheet() {
		String user = sessionBackingBean.loginUser;

		for (CommittedOffencesDTO offence : nullSafe(selectedComplaintDTO.getCommittedOffences())) {
			if (grievanceManagementService.saveOffenceCharge(
					offence.isDriverApplicable() ? driverData.getDriverConductorId() : null,
					offence.isDriverApplicable() ? driverData.getNic() : null,
					offence.isConductorApplicable() ? conductorData.getDriverConductorId() : null,
					offence.isConductorApplicable() ? conductorData.getNic() : null,
					selectedComplaintDTO.getComplaintNo(), offence, user)) {
				grievanceManagementService.beanLinkMethod(selectedComplaintDTO, user, "Save Offence Charge", "Inquiry Process");
				showMsg("SUCCESS", "Saved Successfully");
				RequestContext.getCurrentInstance().execute("PF('dlgChargeSheet').hide();");
				RequestContext.getCurrentInstance().update("frmInquiryProcess:pnlComplaintDetails");
				loadInquiryData();
				// disablePrint=false;

				// Check whether nt_t_offence_charge_sheet_voucher table has
				// data.
				// if available,update nt_t_offence_charge_sheet_voucher
				String loginUser = sessionBackingBean.getLoginUser();
				grievanceManagementService.updateChargeSheetVoucher(selectedComplaintDTO, loginUser, totalAmt);
			} else
				showMsg("ERROR", "Error");
		}

		existingVoucherVal = grievanceManagementService.getExisteingVoucherNo(selectedComplaintDTO.getComplaintNo());
		if (existingVoucherVal != null) {
			if (selectedComplaintDTO.getRecommended() != null && !selectedComplaintDTO.getRecommended().isEmpty()
					&& selectedComplaintDTO.getRecommended().trim().equalsIgnoreCase("N")) {

				grievanceManagementService.complainRequestHistory(selectedComplaintDTO, selectedComplaintDTO.getComplaintNo(),user);

				grievanceManagementService.updateInquiryRecommendation(null, null, null,
						selectedComplaintDTO.getComplaintNo());
				grievanceManagementService.updateInquiryApproval(null, null, null,
						selectedComplaintDTO.getComplaintNo());

			}

			if (selectedComplaintDTO.getApproved() != null && !selectedComplaintDTO.getApproved().isEmpty()
					&& selectedComplaintDTO.getApproved().trim().equalsIgnoreCase("N")) {

				grievanceManagementService.complainRequestHistory(selectedComplaintDTO, selectedComplaintDTO.getComplaintNo(),user);

				grievanceManagementService.updateInquiryRecommendation(null, null, null,
						selectedComplaintDTO.getComplaintNo());
				grievanceManagementService.updateInquiryApproval(null, null, null,
						selectedComplaintDTO.getComplaintNo());

			}
			searchComplaint();
			RequestContext.getCurrentInstance().update("frmInquiryProcess:pnlComplaintDetails");
		}
	}

	public void resetChargeSheet() {
		List<CommittedOffencesDTO> chageSheetOffenceList = grievanceManagementService
				.getCommittedOffencesByComplaint(selectedComplaintDTO.getComplainSeq(), true);

		for (CommittedOffencesDTO c : nullSafe(chageSheetOffenceList)) {
			if (c.getChr_seq() == null || c.getChr_seq() == 0) {
				// get default amount if no values in nt_t_offence_charge_sheet
				c.setCharge(grievanceManagementService.getOffenceCharge(c.getCode()));
			}
			
			// get driver/conductor points
			c = grievanceManagementService.getDriverConductorPoints(complaintNo, c);
			if (c.getDriverPoints() != null && c.getDriverPoints().compareTo(new BigDecimal(0)) > 0) {
				c.setDriverApplicable(true);
			}else {
				c.setDriverPoints(approveActionChargesService.getDemeritPoints(c.getCode(),"FT"));
			}
				
			if (c.getConductorPoints() != null && c.getConductorPoints().compareTo(new BigDecimal(0)) > 0) {
				c.setConductorApplicable(true);
			}else {
				c.setConductorPoints(approveActionChargesService.getDemeritPoints(c.getCode(),"FT"));
			}
				
		}

		selectedComplaintDTO.setCommittedOffences(chageSheetOffenceList);
		calculateTotAmount();
		calculateDriPoints();
		calculateCondPoints();
	}

	// WARNING LETTERS
	public void sendWarningLetters() {

		showMsg("SUCCESS", "Letters sent");
		warningLetterPersons = null;
	}

	// Common methods
	public void showMsg(String type, String msg) {
		if (type.equalsIgnoreCase("ERROR")) {
			errorMsg = msg;
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		} else {
			sucessMsg = msg;
			RequestContext.getCurrentInstance().update("frmSuccess");
			RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
		}
	}

	private static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	/** generate voucher **/
	public void printChargeSheetVoucher() {
		boolean checkReportData = false;
		existingVoucherVal = grievanceManagementService.getExisteingVoucherNo(selectedComplaintDTO.getComplaintNo());
		if (existingVoucherVal != null) {
			showMsg("ERROR", "Voucher already generated.");

		} else {
			checkReportData = grievanceManagementService
					.checkDataInChargeSheetTable(selectedComplaintDTO.getComplaintNo());

			if (checkReportData) {
				generateChargeSheetVoucherNo();

				String loginUser = sessionBackingBean.getLoginUser();

				file = null;
				String sourceFileName = null;

				Connection conn = null;

				sourceFileName = "..//reports//debitVoucherForChargeSheet.jrxml";

				try {
					conn = ConnectionManager.getConnection();

					// Parameters for report
					Map<String, Object> parameters = new HashMap<String, Object>();

					parameters.put("P_VoucherNO", voucherVal);
					if (selectedComplaintDTO.getPermitNo() != null && !selectedComplaintDTO.getPermitNo().isEmpty()) {
						parameters.put("P_Permit_No", selectedComplaintDTO.getPermitNo());
					} else {
						parameters.put("P_Permit_No", null);
					}
					if (selectedComplaintDTO.getOwnerName() != null && !selectedComplaintDTO.getOwnerName().isEmpty()) {
						parameters.put("P_Permit_Owner", selectedComplaintDTO.getOwnerName());
					} else {
						parameters.put("P_Permit_Owner", null);
					}
					parameters.put("P_Complain_No", complaintNo);

					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

					byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					file = new DefaultStreamedContent(stream, "Application/pdf", "Control_Sheet.pdf");

					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "pdf");

					try {
						grievanceManagementService.beanLinkMethod(selectedComplaintDTO, loginUser, "Print voucher","Inquiry Process");
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} catch (JRException e) {
					e.printStackTrace();
				} finally {
					try {
						if (conn != null)
							conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				grievanceManagementService.insertVoucherDetails(voucherVal, selectedComplaintDTO, loginUser, totalAmt);

				searchComplaint();
				RequestContext.getCurrentInstance().execute("PF('dlgChargeSheet').hide();");
				RequestContext.getCurrentInstance().update("frmInquiryProcess:pnlComplaintDetails");

				showMsg("SUCCESS", "Charge Sheet Voucher Printed Successfully");

			} else {
				showMsg("ERROR", "Relevant data missing for the Complain No.");

			}
		}

	}

	public String generateChargeSheetVoucherNo() {
		voucherVal = grievanceManagementService.generateVoucherNOForChrgeSheet();

		return voucherVal;
	}

	public void rePrintChargeSheetVoucher() {

		generateChargeSheetVoucherNo();

		String loginUser = sessionBackingBean.getLoginUser();

		file = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//debitVoucherForChargeSheet.jrxml";

		try {
			conn = ConnectionManager.getConnection();

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_VoucherNO", existingVoucherVal);
			if (selectedComplaintDTO.getPermitNo() != null && !selectedComplaintDTO.getPermitNo().isEmpty()) {
				parameters.put("P_Permit_No", selectedComplaintDTO.getPermitNo());
			} else {
				parameters.put("P_Permit_No", null);
			}
			if (selectedComplaintDTO.getOwnerName() != null && !selectedComplaintDTO.getOwnerName().isEmpty()) {
				parameters.put("P_Permit_Owner", selectedComplaintDTO.getOwnerName());
			} else {
				parameters.put("P_Permit_Owner", null);
			}
			parameters.put("P_Complain_No", complaintNo);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			file = new DefaultStreamedContent(stream, "Application/pdf", "Control_Sheet.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			grievanceManagementService.beanLinkMethod(selectedComplaintDTO, loginUser, "Charge Sheet Voucher Re - Print","Inquiry Process");
		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		searchComplaint();
		RequestContext.getCurrentInstance().execute("PF('dlgChargeSheet').hide();");
		RequestContext.getCurrentInstance().update("frmInquiryProcess:pnlComplaintDetails");

		showMsg("SUCCESS", "Charge Sheet Voucher Re-Printed Successfully");
	}

	/** generate voucher finished **/

	/** Permit Owner Warning Letter not used due to changes **/

	public StreamedContent printWarning() throws JRException {

		String valueForPrint = null;
		valueForPrint = complaintNo;

		String loginUser = sessionBackingBean.getLoginUser();

		file = null;
		String sourceFileName = null;

		Connection conn = null;

		sourceFileName = "..//reports//PublicComplainWarningLetter.jrxml";

		try {
			conn = ConnectionManager.getConnection();

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Complain_no", valueForPrint);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			file = new DefaultStreamedContent(stream, "Application/pdf", "Permitholder_warningLetter.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			try {
				grievanceManagementService.beanLinkMethod(selectedComplaintDTO, loginUser, "Print Warning Letter","Inquiry Process");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;

	}

	public void loadInquiryRecommend() {
		RequestContext.getCurrentInstance().update("inquiryRecommendDlg");
		RequestContext.getCurrentInstance().execute("PF('inquiryRecommendDlg').show()");
	}

	public void inquiryRecommendationRecommend() {
		String user = sessionBackingBean.getLoginUser();

		if (selectedComplaintDTO.getRecommendRemark() != null && !selectedComplaintDTO.getRecommendRemark().isEmpty()
				&& !selectedComplaintDTO.getRecommendRemark().trim().equalsIgnoreCase("")) {

			grievanceManagementService.complainRequestHistory(selectedComplaintDTO, selectedComplaintDTO.getComplaintNo(),user);

			grievanceManagementService.updateInquiryRecommendation( selectedComplaintDTO.getRecommendRemark(), "Y",
					sessionBackingBean.getLoginUser(), selectedComplaintDTO.getComplaintNo());
			searchComplaint();

			RequestContext.getCurrentInstance().update("inquiryRecommendDlg");
			RequestContext.getCurrentInstance().execute("PF('inquiryRecommendDlg').hide()");

			grievanceManagementService.beanLinkMethod(selectedComplaintDTO, user, "Inquiry Reject", "Inquiry Process");
			showMsg("SUCCESS", "Recommended Successfully");

		} else {
			showMsg("ERROR", "Inquiry Recommendation Remark Required");
		}

	}

	public void inquiryRecommendationReject() {
		String user = sessionBackingBean.getLoginUser();
		if (selectedComplaintDTO.getRecommendRemark() != null && !selectedComplaintDTO.getRecommendRemark().isEmpty()
				&& !selectedComplaintDTO.getRecommendRemark().trim().equalsIgnoreCase("")) {

			grievanceManagementService.complainRequestHistory(selectedComplaintDTO, selectedComplaintDTO.getComplaintNo(),user);

			grievanceManagementService.updateInquiryRecommendation(selectedComplaintDTO.getRecommendRemark(), "N",
					sessionBackingBean.getLoginUser(), selectedComplaintDTO.getComplaintNo());
			searchComplaint();

			RequestContext.getCurrentInstance().update("inquiryRecommendDlg");
			RequestContext.getCurrentInstance().execute("PF('inquiryRecommendDlg').hide()");

			grievanceManagementService.beanLinkMethod(selectedComplaintDTO, user, "Inquiry Recommend Reject", "Inquiry Process");
			showMsg("SUCCESS", "Rejected Successfully");

		} else {
			showMsg("ERROR", "Inquiry Recommendation Remark Required");
		}
	}

	public void loadInquiryApproval() {
		RequestContext.getCurrentInstance().update("inquiryApprovalDlg");
		RequestContext.getCurrentInstance().execute("PF('inquiryApprovalDlg').show()");
	}

	public void inquiryApprovalApprove() {
		String user = sessionBackingBean.getLoginUser();

		if (selectedComplaintDTO.getApprovedRemark() != null && !selectedComplaintDTO.getApprovedRemark().isEmpty()
				&& !selectedComplaintDTO.getApprovedRemark().trim().equalsIgnoreCase("")) {
			/**reduce  dermerit points from the total points based on Damith Req. 12/06/2021**/
			
			grievanceManagementService.reduceDemeritPointsFromTotalPoints(driverData.getDriverConductorId(),
					conductorData.getDriverConductorId(),totDriverPoints,totConductorPoints, selectedComplaintDTO, user);
			
			/**end**/
			grievanceManagementService.complainRequestHistory(selectedComplaintDTO,selectedComplaintDTO.getComplaintNo(),user);

			grievanceManagementService.updateInquiryApproval(selectedComplaintDTO.getApprovedRemark(), "Y",
					sessionBackingBean.getLoginUser(), selectedComplaintDTO.getComplaintNo());
			searchComplaint();

			RequestContext.getCurrentInstance().update("inquiryApprovalDlg");
			RequestContext.getCurrentInstance().execute("PF('inquiryApprovalDlg').hide()");

			grievanceManagementService.beanLinkMethod(selectedComplaintDTO, user, "Inquiry Approve", "Inquiry Process");
			showMsg("SUCCESS", "Approved Successfully");

		} else {
			showMsg("ERROR", "Inquiry Approval Remark Required");
		}
	}

	public void inquiryApprovalReject() {

		String user = sessionBackingBean.getLoginUser();
		if (selectedComplaintDTO.getApprovedRemark() != null && !selectedComplaintDTO.getApprovedRemark().isEmpty()
				&& !selectedComplaintDTO.getApprovedRemark().trim().equalsIgnoreCase("")) {

			grievanceManagementService.complainRequestHistory(selectedComplaintDTO.getComplaintNo());

			grievanceManagementService.updateInquiryApproval(selectedComplaintDTO.getApprovedRemark(), "N",
					sessionBackingBean.getLoginUser(), selectedComplaintDTO.getComplaintNo());
			searchComplaint();

			RequestContext.getCurrentInstance().update("inquiryApprovalDlg");
			RequestContext.getCurrentInstance().execute("PF('inquiryApprovalDlg').hide()");

			grievanceManagementService.beanLinkMethod(selectedComplaintDTO, user, "Inquiry Recommend", "Inquiry Process");
			showMsg("SUCCESS", "Rejected Successfully");

		} else {
			showMsg("ERROR", "Inquiry Approval Remark Required");
		}
	}

	public void documentManagement() {
		try {

			sessionBackingBean.setComplainNo(complaintNo);

			String strTransactionType = "24";
			sessionBackingBean.setTransactionType("COMPLAIN");

			setMandatoryList(documentManagementService.mandatoryDocsForGrievance(strTransactionType, complaintNo));
			setOptionalList(documentManagementService.optionalDocsForGrievance(strTransactionType, complaintNo));

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.grievanceMandatoryListM(complaintNo, strTransactionType);
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.grievanceOptionalListM(complaintNo, strTransactionType);

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");
			grievanceManagementService.beanLinkMethod(selectedComplaintDTO, sessionBackingBean.getLoginUser(), "Document Submit", "Inquiry Process");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** Permit Owner Warning Letter End **/

	//////////////// getter & setters /////////////////////

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public GrievanceManagementService getGrievanceManagementService() {
		return grievanceManagementService;
	}

	public void setGrievanceManagementService(GrievanceManagementService grievanceManagementService) {
		this.grievanceManagementService = grievanceManagementService;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getDateFormatStr() {
		return dateFormatStr;
	}

	public void setDateFormatStr(String dateFormatStr) {
		this.dateFormatStr = dateFormatStr;
	}

	public DateTimeFormatter getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(DateTimeFormatter timeFormat) {
		this.timeFormat = timeFormat;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isComplaintSelected() {
		return complaintSelected;
	}

	public void setComplaintSelected(boolean complaintSelected) {
		this.complaintSelected = complaintSelected;
	}

	public String getComplaintNo() {
		return complaintNo;
	}

	public void setComplaintNo(String complaintNo) {
		this.complaintNo = complaintNo;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public ComplaintRequestDTO getSelectedComplaintDTO() {
		return selectedComplaintDTO;
	}

	public void setSelectedComplaintDTO(ComplaintRequestDTO selectedComplaintDTO) {
		this.selectedComplaintDTO = selectedComplaintDTO;
	}

	public List<ComplaintRequestDTO> getComplaintDetailDTOList() {
		return complaintDetailDTOList;
	}

	public void setComplaintDetailDTOList(List<ComplaintRequestDTO> complaintDetailDTOList) {
		this.complaintDetailDTOList = complaintDetailDTOList;
	}

	public List<String> getComplaintNoList() {
		return complaintNoList;
	}

	public void setComplaintNoList(List<String> complaintNoList) {
		this.complaintNoList = complaintNoList;
	}

	public Date getInvestigationFrom() {
		return investigationFrom;
	}

	public void setInvestigationFrom(Date investigationFrom) {
		this.investigationFrom = investigationFrom;
	}

	public Date getInvestigationTo() {
		return investigationTo;
	}

	public void setInvestigationTo(Date investigationTo) {
		this.investigationTo = investigationTo;
	}

	public boolean isNewDriver() {
		return newDriver;
	}

	public void setNewDriver(boolean newDriver) {
		this.newDriver = newDriver;
	}

	public boolean isNewConductor() {
		return newConductor;
	}

	public void setNewConductor(boolean newConductor) {
		this.newConductor = newConductor;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public AccessPermissionService getAccessPermissionService() {
		return accessPermissionService;
	}

	public void setAccessPermissionService(AccessPermissionService accessPermissionService) {
		this.accessPermissionService = accessPermissionService;
	}

	public List<DropDownDTO> getDriverList() {
		return driverList;
	}

	public void setDriverList(List<DropDownDTO> driverList) {
		this.driverList = driverList;
	}

	public List<DropDownDTO> getConductorList() {
		return conductorList;
	}

	public void setConductorList(List<DropDownDTO> conductorList) {
		this.conductorList = conductorList;
	}

	public List<DropDownDTO> getActionList() {
		return actionList;
	}

	public void setActionList(List<DropDownDTO> actionList) {
		this.actionList = actionList;
	}

	public List<AccessPermissionDTO> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<AccessPermissionDTO> deptList) {
		this.deptList = deptList;
	}

	public String getRegisteredDate() {
		return registeredDate;
	}

	public void setRegisteredDate(String registeredDate) {
		this.registeredDate = registeredDate;
	}

	public BigDecimal getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	public DriverConductorRegistrationDTO getDriverData() {
		return driverData;
	}

	public void setDriverData(DriverConductorRegistrationDTO driverData) {
		this.driverData = driverData;
	}

	public DriverConductorRegistrationDTO getConductorData() {
		return conductorData;
	}

	public void setConductorData(DriverConductorRegistrationDTO conductorData) {
		this.conductorData = conductorData;
	}

	public List<ComplaintRequestDTO> getComplaintHistoryList() {
		return complaintHistoryList;
	}

	public void setComplaintHistoryList(List<ComplaintRequestDTO> complaintHistoryList) {
		this.complaintHistoryList = complaintHistoryList;
	}

	public List<CommittedOffencesDTO> getViolationConditionList() {
		return violationConditionList;
	}

	public void setViolationConditionList(List<CommittedOffencesDTO> violationConditionList) {
		this.violationConditionList = violationConditionList;
	}

	public ComplaintRequestDTO getSelectedHistory() {
		return selectedHistory;
	}

	public void setSelectedHistory(ComplaintRequestDTO selectedHistory) {
		this.selectedHistory = selectedHistory;
	}

	public boolean isHistorySelected() {
		return historySelected;
	}

	public void setHistorySelected(boolean historySelected) {
		this.historySelected = historySelected;
	}

	public String[] getWarningLetterPersons() {
		return warningLetterPersons;
	}

	public void setWarningLetterPersons(String[] warningLetterPersons) {
		this.warningLetterPersons = warningLetterPersons;
	}

	public BigDecimal getTotalDriverPoints() {
		return totalDriverPoints;
	}

	public void setTotalDriverPoints(BigDecimal totalDriverPoints) {
		this.totalDriverPoints = totalDriverPoints;
	}

	public BigDecimal getTotalConductorPoints() {
		return totalConductorPoints;
	}

	public void setTotalConductorPoints(BigDecimal totalConductorPoints) {
		this.totalConductorPoints = totalConductorPoints;
	}

	public BigDecimal getTotDriverPoints() {
		return totDriverPoints;
	}

	public void setTotDriverPoints(BigDecimal totDriverPoints) {
		this.totDriverPoints = totDriverPoints;
	}

	public BigDecimal getTotConductorPoints() {
		return totConductorPoints;
	}

	public void setTotConductorPoints(BigDecimal totConductorPoints) {
		this.totConductorPoints = totConductorPoints;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public String getVoucherVal() {
		return voucherVal;
	}

	public void setVoucherVal(String voucherVal) {
		this.voucherVal = voucherVal;
	}

	public boolean isDisablePrint() {
		return disablePrint;
	}

	public void setDisablePrint(boolean disablePrint) {
		this.disablePrint = disablePrint;
	}

	public String getExistingVoucherVal() {
		return existingVoucherVal;
	}

	public void setExistingVoucherVal(String existingVoucherVal) {
		this.existingVoucherVal = existingVoucherVal;
	}

	public boolean isDisableRePrint() {
		return disableRePrint;
	}

	public void setDisableRePrint(boolean disableRePrint) {
		this.disableRePrint = disableRePrint;
	}

	public DateFormat getOldDf() {
		return oldDf;
	}

	public void setOldDf(DateFormat oldDf) {
		this.oldDf = oldDf;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public static String getOWNER() {
		return OWNER;
	}

	public static void setOWNER(String oWNER) {
		OWNER = oWNER;
	}

	public static String getDRIVER() {
		return DRIVER;
	}

	public static void setDRIVER(String dRIVER) {
		DRIVER = dRIVER;
	}

	public static String getCONDUCTOR() {
		return CONDUCTOR;
	}

	public static void setCONDUCTOR(String cONDUCTOR) {
		CONDUCTOR = cONDUCTOR;
	}

	public boolean isChargeSheetGenerated() {
		return chargeSheetGenerated;
	}

	public void setChargeSheetGenerated(boolean chargeSheetGenerated) {
		this.chargeSheetGenerated = chargeSheetGenerated;
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

	public ApproveActionChargesService getApproveActionChargesService() {
		return approveActionChargesService;
	}

	public void setApproveActionChargesService(ApproveActionChargesService approveActionChargesService) {
		this.approveActionChargesService = approveActionChargesService;
	}

}
