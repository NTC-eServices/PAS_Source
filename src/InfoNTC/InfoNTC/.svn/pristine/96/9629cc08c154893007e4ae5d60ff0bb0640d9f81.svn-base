package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.AmendmentBusOwnerDTO;
import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.ParamerDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.AmendmentService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.IssuePermitService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
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

@ManagedBean(name = "grantApprovalAmendments")
@ViewScoped
public class GrantApprovalAmendmentsBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private AmendmentDTO amendmentDTO, selectDTO, filledamendmentDTO, viewSelectDTO, amendmentHistoryDTO;
	private PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
	private AmendmentService amendmentService;
	private List<AmendmentDTO> permitNoList = new ArrayList<AmendmentDTO>(0);
	private List<AmendmentDTO> applicationNoList = new ArrayList<AmendmentDTO>(0);
	private List<AmendmentDTO> amendmentsDetailList;
	private List<AmendmentDTO> transactionList = new ArrayList<AmendmentDTO>(0);
	private String alertMSG, successMessage, errorMessage, rejectReason, labelForApproveForPrint;
	private List<AmendmentDTO> ApplicationDocuments = new ArrayList<AmendmentDTO>(0);
	private List<AmendmentDTO> ApplicationVersionDocuments = new ArrayList<AmendmentDTO>(0);
	private CommonService commonService;
	private MigratedService migratedService;
	private IssuePermitService issuePermitService;
	private boolean disabledReject, disabledCancel, disabledPrint, disabledApproveTwo, disabledApproveOne,
			disabledGenerateResponse, disabledBoardApprove, disabledCommitteeApprove, disabledPermitNo,
			disabledApplicationNo, disabledTimeApproval, disabledSkipPTA, disabledReqPTA, disabledNewRoute, renderOne,
			disabledRejectPTA, disabledApprovePTA, disabledPrintButton, disablGenRepo, disabledRePrint;
	private StreamedContent files;
	private AdminService adminService;
	private HistoryService historyService;
	private PermitRenewalsDTO applicationHistoryDTO, ownerHistoryDTO;
	private OminiBusDTO ominiBusHistoryDTO;

	private AmendmentBusOwnerDTO routeDetDTO;

	public GrantApprovalAmendmentsBackingBean() {
		amendmentDTO = new AmendmentDTO();
		selectDTO = new AmendmentDTO();
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		amendmentService = (AmendmentService) SpringApplicationContex.getBean("amendmentService");
		issuePermitService = (IssuePermitService) SpringApplicationContex.getBean("issuePermitService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");

		loadValues();
	}

	public void loadValues() {
		amendmentsDetailList = new ArrayList<>();
		amendmentsDetailList = amendmentService.getGrantApprovalDefaultDetails(amendmentDTO);

		transactionList = amendmentService.getTransactionType();
		disabledReject = true;
		disabledPrint = true;
		disabledApproveTwo = true;
		disabledApproveOne = true;
		disabledCancel = true;
		disabledBoardApprove = true;
		disabledCommitteeApprove = true;
		disabledGenerateResponse = true;
		disabledApplicationNo = true;
		disabledPermitNo = true;
		disabledTimeApproval = true;
		disabledSkipPTA = true;
		disabledReqPTA = true;
		disabledNewRoute = true;
		disabledRejectPTA = true;
		disabledApprovePTA = true;
		renderOne = false;
		disabledPrintButton = true;
		disablGenRepo = true;
		disabledRePrint = true;
	}

	public void ajaxFillData() {

		amendmentDTO.setPermitNo(null);
		amendmentDTO.setNewBusNo(null);
		amendmentDTO.setExisitingBusNo(null);

		filledamendmentDTO = new AmendmentDTO();
		filledamendmentDTO = amendmentService.ajaxFillData(amendmentDTO);

		if (filledamendmentDTO.getApplicationNo() != null) {
			amendmentDTO.setApplicationNo(filledamendmentDTO.getApplicationNo());

			if (filledamendmentDTO.getPermitNo() != null) {
				amendmentDTO.setPermitNo(filledamendmentDTO.getPermitNo());

				if (filledamendmentDTO.getExisitingBusNo() != null) {
					amendmentDTO.setExisitingBusNo(filledamendmentDTO.getExisitingBusNo());

					if (filledamendmentDTO.getNewBusNo() != null) {
						amendmentDTO.setNewBusNo(filledamendmentDTO.getNewBusNo());
					}
				}
			}

		}

	}

	public void ajaxFilterApplicationNoANDPermitNo() {

		disabledApplicationNo = false;
		disabledPermitNo = false;

		permitNoList = amendmentService.getPermitNO(amendmentDTO);
		applicationNoList = amendmentService.getApplicationNO(amendmentDTO);

	}

	public void search() {

		if ((amendmentDTO.getApplicationNo() != null && !amendmentDTO.getApplicationNo().trim().equalsIgnoreCase(""))
				|| (amendmentDTO.getPermitNo() != null && !amendmentDTO.getPermitNo().trim().equalsIgnoreCase("")
						|| amendmentDTO.getTranCode() != null
								&& !amendmentDTO.getTranCode().trim().equalsIgnoreCase(""))) {

			amendmentsDetailList = new ArrayList<>();
			amendmentsDetailList = amendmentService.getGrantApprovalDetails(amendmentDTO);

			if (!amendmentsDetailList.isEmpty()) {

				disabledCancel = false;

				if (amendmentDTO.getTranCode().equals("13")) {
					disabledBoardApprove = true;
					disabledCommitteeApprove = true;
					disabledGenerateResponse = true;
				} else if (amendmentDTO.getTranCode().equals("21")) {
					renderOne = true;
					disabledPrintButton = false;

					disablGenRepo = false;// added tharushi.e
				} else if (amendmentDTO.getTranCode().equals("22")) {
					renderOne = true;
					disabledPrintButton = false;
					disablGenRepo = false;// added tharushi.e
				} else if (amendmentDTO.getTranCode().equals("23")) {
					renderOne = true;
					disabledPrintButton = false;
					disablGenRepo = false;// added tharushi.e
				}
				// added by tharushi.e
				else if (amendmentDTO.getTranCode().equals("14")) {
					renderOne = true;

					disabledReqPTA = true;
					disabledRejectPTA = true;
					disabledApprovePTA = true;
					disablGenRepo = false;
				}

			} else {
				setErrorMessage("No Data Found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select the Application No. / Permit No. or Transaction Type");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearOne() {

		amendmentDTO = new AmendmentDTO();
		amendmentsDetailList = new ArrayList<>();
		disabledApproveOne = true;
		disabledApproveTwo = true;
		disabledPrint = true;
		disabledReject = true;
		disabledCancel = true;
		disabledBoardApprove = true;
		disabledCommitteeApprove = true;
		disabledGenerateResponse = true;
		disabledApplicationNo = true;
		disabledPermitNo = true;
		disabledTimeApproval = true;
		disabledSkipPTA = true;
		disabledReqPTA = true;
		disabledNewRoute = true;
		renderOne = false;
		amendmentsDetailList = new ArrayList<>();
		amendmentsDetailList = amendmentService.getGrantApprovalDefaultDetails(amendmentDTO);
	}

	public void clearTwo() {
		amendmentsDetailList = new ArrayList<>();
		disabledApproveOne = true;
		disabledApproveTwo = true;
		disabledPrint = true;
		disabledReject = true;
		disabledCancel = true;
		disabledBoardApprove = true;
		disabledCommitteeApprove = true;
		disabledGenerateResponse = true;
		disabledTimeApproval = true;
		disabledSkipPTA = true;
		disabledReqPTA = true;
		disabledNewRoute = true;
		renderOne = false;
	}

	public void selectRow() {

		if (!amendmentsDetailList.isEmpty()) {

			disabledCancel = false;
			disabledReject = false;

			if (selectDTO.getTranCode().equals("13")) {
				amendmentDTO.setTranCode("13");
			} else if (selectDTO.getTranCode().equals("21")) {
				amendmentDTO.setTranCode("21");
				renderOne = true;
			} else if (selectDTO.getTranCode().equals("22")) {
				amendmentDTO.setTranCode("22");
				renderOne = true;
			} else if (selectDTO.getTranCode().equals("23")) {
				amendmentDTO.setTranCode("23");
				renderOne = true;
			} else if (selectDTO.getTranCode().equals("14")) {
				amendmentDTO.setTranCode("14");
			} else if (selectDTO.getTranCode().equals("15")) {
				amendmentDTO.setTranCode("15");
			} else if (selectDTO.getTranCode().equals("16")) {
				amendmentDTO.setTranCode("16");
			}

			if (selectDTO.getApplicationNo() != null) {
				amendmentDTO.setApplicationNo(selectDTO.getApplicationNo());
			}
			changeButtonState();
		}

	}

	public void changeButtonState() {
		boolean isPrinted = false;

		isPrinted = amendmentService.isPrintPermit(selectDTO.getApplicationNo());
		if (isPrinted) {
			disabledRePrint = false;

		}
		if (selectDTO.getTranCode().equals("13")) {
			disabledBoardApprove = true;
			disabledCommitteeApprove = true;
			disabledGenerateResponse = true;

			if (amendmentService.checkTaskDetails(selectDTO, "AM100", "C") == true) {
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AA001", "C") == true) {
				disabledApproveOne = false;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM104", "C") == true) {
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM300", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM301", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM302", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM105", "C") == true) {
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AA002", "C") == true) {
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = false;
			}
		} else if (selectDTO.getTranCode().equals("15") || selectDTO.getTranCode().equals("16")) {
			/* Owner and bus * service and bus */
			if (amendmentService.checkTaskDetails(selectDTO, "AM100", "C") == true) {
				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM300", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM100", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == false) {

				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM301", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM100", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == false) {

				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM302", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM100", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == false) {

				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "AM101", "O") == true) {
				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM101", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = false;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM102", "O") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = false;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM102", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = false;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AA001", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = false;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM300", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM301", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM302", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "AA002", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = false;
			}

		} else if (selectDTO.getTranCode().equals("14")) {

			// delete service change and add new Else block
			/* Owner Change */
			if (amendmentService.checkTaskDetails(selectDTO, "AM100", "C") == true) {
				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM300", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM100", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM101", "C") == false) {

				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM301", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM100", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM101", "C") == false) {

				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM302", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM100", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM101", "C") == false) {

				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "AM101", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = false;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM101", "O") == true) {
				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM102", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = false;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM102", "O") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = false;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AA001", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = false;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM300", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM301", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM302", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AA002", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = false;
			}
		} else if (selectDTO.getTranCode().equals("22")) {
			/* Route Change */

			/* Service Change */
			if (amendmentService.checkTaskDetails(selectDTO, "AM100", "C") == true) {
				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM101", "O") == true) {
				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM101", "C")
					&& amendmentService.checkPTAStatus(selectDTO, null)) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = true;
				disabledSkipPTA = false;
				disabledReqPTA = false;

			} else if (amendmentService.checkPTAStatus(selectDTO, "P")) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = true;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledApprovePTA = false;
				disabledRejectPTA = false;

			} else if (amendmentService.checkPTAStatus(selectDTO, "A")
					&& amendmentService.checkTimeApprovalStatus(selectDTO, null)) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = false;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledApprovePTA = true;
				disabledRejectPTA = true;
			} else if (amendmentService.checkPTAStatus(selectDTO, "R")
					&& amendmentService.checkTimeApprovalStatus(selectDTO, null)) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = false;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledApprovePTA = true;
				disabledRejectPTA = true;

			} else if (amendmentService.checkPTAStatus(selectDTO, "S")
					&& amendmentService.checkTimeApprovalStatus(selectDTO, null)) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = false;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledApprovePTA = true;
				disabledRejectPTA = true;

			} else if (amendmentService.checkTimeApprovalStatus(selectDTO, "A")
					&& amendmentService.checkTaskDetails(selectDTO, "AM102", "C") == false
					&& amendmentService.checkTaskHistory(selectDTO, "AM102", "C") == false) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = false;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = true;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledApprovePTA = true;
				disabledRejectPTA = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "AM102", "O") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = false;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM102", "C")
					&& amendmentService.checkTempRouteStatus(selectDTO, "P")) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = true;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledNewRoute = false;
			} else if (amendmentService.checkTempRouteStatus(selectDTO, "P") == false
					&& amendmentService.checkTaskDetails(selectDTO, "AM102", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = false;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = true;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledNewRoute = true;
			} else if (amendmentService.checkTempRouteStatus(selectDTO, "R")
					&& amendmentService.checkTaskDetails(selectDTO, "AM102", "C") == true
					&& amendmentService.checkTaskDetails(selectDTO, "AM103", "C") == false) {

				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = false;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = true;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledNewRoute = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "AA001", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = false;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM300", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM301", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM302", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AA002", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = false;
			}

		} else {

			/* Service Change */
			if (amendmentService.checkTaskDetails(selectDTO, "AM100", "C") == true) {
				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM101", "O") == true) {
				disabledCommitteeApprove = false;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM101", "C")
					&& amendmentService.checkPTAStatus(selectDTO, null)) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = true;
				disabledSkipPTA = false;
				disabledReqPTA = false;

			} else if (amendmentService.checkPTAStatus(selectDTO, "P")) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = true;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledApprovePTA = false;
				disabledRejectPTA = false;

			} else if (amendmentService.checkPTAStatus(selectDTO, "A")
					&& amendmentService.checkTimeApprovalStatus(selectDTO, null)) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = false;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledApprovePTA = true;
				disabledRejectPTA = true;
			} else if (amendmentService.checkPTAStatus(selectDTO, "R")
					&& amendmentService.checkTimeApprovalStatus(selectDTO, null)) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = false;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledApprovePTA = true;
				disabledRejectPTA = true;

			} else if (amendmentService.checkPTAStatus(selectDTO, "S")
					&& amendmentService.checkTimeApprovalStatus(selectDTO, null)) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = false;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledApprovePTA = true;
				disabledRejectPTA = true;

			} else if (amendmentService.checkTimeApprovalStatus(selectDTO, "A")
					&& amendmentService.checkTaskDetails(selectDTO, "AM102", "C") == false
					&& amendmentService.checkTaskHistory(selectDTO, "AM102", "C") == false) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = false;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = true;
				disabledSkipPTA = true;
				disabledReqPTA = true;
				disabledApprovePTA = true;
				disabledRejectPTA = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "AM102", "O") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = false;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM102", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = false;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = true;
				disabledTimeApproval = true;
				disabledSkipPTA = true;
				disabledReqPTA = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "AA001", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = false;
				disabledApproveTwo = true;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM300", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM301", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM302", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM104", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = false;
				disabledPrint = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "AA002", "C") == true) {
				disabledCommitteeApprove = true;
				disabledBoardApprove = true;
				disabledGenerateResponse = true;
				disabledApproveOne = true;
				disabledApproveTwo = true;
				disabledPrint = false;
			}

		}

	}

	/* need to add if else block and check transaction types */

	public void committeeApprove() {

		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

				boolean isAM101FoundInTaskDetailsAsOngoing = amendmentService.checkTaskDetails(selectDTO, "AM101", "O");

				if (isAM101FoundInTaskDetailsAsOngoing == false) {

					boolean isAM101FoundInTaskDetailsAsComplete = amendmentService.checkTaskDetails(selectDTO, "AM101",
							"C");
					boolean isAM101FoundInTaskHistoryAsComplete = amendmentService.checkTaskHistory(selectDTO, "AM101",
							"C");

					if (isAM101FoundInTaskDetailsAsComplete == false && isAM101FoundInTaskHistoryAsComplete == false) {

						/* Owner Change. Previous Task is PM302 */
						if (amendmentDTO.getTranCode().equals("14") || amendmentDTO.getTranCode().equals("15")
								|| amendmentDTO.getTranCode().equals("16")) {

							boolean isFoundInTaskPM302Details = amendmentService.checkTaskDetails(selectDTO, "PM302",
									"C");
							boolean isFoundInTaskPM302History = amendmentService.checkTaskHistory(selectDTO, "PM302",
									"C");

							if (isFoundInTaskPM302History == true || isFoundInTaskPM302Details == true) {

								/* Update Amendment table and Amendment history table */
								amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
										sessionBackingBean.getLoginUser());
								boolean isUpdateCommitteeStatus = amendmentService.updateAmendmentsTableCommitteeStatus(
										selectDTO, "P", sessionBackingBean.getLoginUser());

								if (isUpdateCommitteeStatus == true) {

									commonService.updateTaskStatus(selectDTO.getApplicationNo(), "PM302", "AM101", "C",
											sessionBackingBean.getLoginUser());
									setSuccessMessage("Committee Approval is On Progress");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									/**
									 * added for update que master table task by tharushi.e
									 */

									/**
									 * end by tharushi.e
									 */
									amendmentsDetailList = amendmentService
											.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
									historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);

									changeButtonState();

								} else {
									setErrorMessage("Committee Approval is Not On Progress");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}

							} else {
								setErrorMessage("Receipt Generation Should Be Compeleted For Amendments");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							/*
							 * Type Change, Route Change, End Change, Owner + Bus Change, Service + Bus
							 * Change. Previous Task is AM100
							 */

							/* Update Amendment table and Amendment history table */
							amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
									sessionBackingBean.getLoginUser());
							boolean isUpdateCommitteeStatus = amendmentService.updateAmendmentsTableCommitteeStatus(
									selectDTO, "P", sessionBackingBean.getLoginUser());

							if (isUpdateCommitteeStatus == true) {

								commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AM100", "AM101", "C",
										sessionBackingBean.getLoginUser());
								setSuccessMessage("Committee Approval is On Progress");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								/**
								 * added for update que master table task by tharushi.e
								 */

								commonService.updateQueMasterTableTask(selectDTO.getApplicationNo(), "AM101", "O");
								/**
								 * end by tharushi.e
								 */

								amendmentsDetailList = amendmentService
										.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
								historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
								changeButtonState();

							} else {
								setErrorMessage("Committee Approval is Not On Progress");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						}

					} else {
						setErrorMessage("Already Complete Committee Approval");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Committee Approval Request Already On progress");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Data already Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select A Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	/* need to add if else block and check transaction types */
	public void boardApprove() {

		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableCommitteeStatus(selectDTO, "R") == false) {

				if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

					boolean isAM102FoundInTaskDetailsAsOngoing = amendmentService.checkTaskDetails(selectDTO, "AM102",
							"O");

					if (isAM102FoundInTaskDetailsAsOngoing == false) {

						boolean isApprovedByCommittee = amendmentService.CheckAmendmentsTableCommitteeStatus(selectDTO);

						if (isApprovedByCommittee == true) {

							boolean isAM102FoundInTaskDetailsAsComplete = amendmentService.checkTaskDetails(selectDTO,
									"AM102", "C");
							boolean isAM102FoundInTaskHistoryAsComplete = amendmentService.checkTaskHistory(selectDTO,
									"AM102", "C");
							if (isAM102FoundInTaskDetailsAsComplete == false
									&& isAM102FoundInTaskHistoryAsComplete == false) {

								if (amendmentDTO.getTranCode().equals("21") || amendmentDTO.getTranCode().equals("22")
										|| amendmentDTO.getTranCode().equals("23")) {

									/*
									 * Type Change, Route Change, End Change. Previous Task is AM101
									 */

									/* Check REQ PTA and REQ TIME APPROVAL */

									commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AM101", "AM102", "C",
											sessionBackingBean.getLoginUser());

									/* Update Amendment table and Amendment history table */
									amendmentHistoryDTO = historyService.getAmendmentTableData(
											selectDTO.getApplicationNo(), sessionBackingBean.getLoginUser());
									boolean isUpdateBoarderStatus = amendmentService.updateAmendmentsTableBoardStatus(
											selectDTO, "P", sessionBackingBean.getLoginUser());

									if (isUpdateBoarderStatus == true /* && isApproved == true */) {

										setSuccessMessage("Board Approval is On Progress");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
										/**
										 * added for update que master table task by tharushi.e
										 */

										commonService.updateQueMasterTableTask(selectDTO.getApplicationNo(), "AM102",
												"O");
										/**
										 * end by tharushi.e
										 */
										amendmentsDetailList = amendmentService
												.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
										changeButtonState();
										historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);

									} else {
										setErrorMessage("Board Approval is Not On Progress");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}

								} else {
									/*
									 * Owner + Bus Change, Service + Bus Change, owner Change Previous Task is AM101
									 */
									commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AM101", "AM102", "C",
											sessionBackingBean.getLoginUser());

									/* Update Amendment table and Amendment history table */
									amendmentHistoryDTO = historyService.getAmendmentTableData(
											selectDTO.getApplicationNo(), sessionBackingBean.getLoginUser());
									boolean isUpdateBoarderStatus = amendmentService.updateAmendmentsTableBoardStatus(
											selectDTO, "P", sessionBackingBean.getLoginUser());

									if (isUpdateBoarderStatus == true /* && isApproved == true */) {

										setSuccessMessage("Board Approval is On Progress");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
										/**
										 * added for update que master table task by tharushi.e
										 */

										// commonService.updateQueMasterTableTask(selectDTO.getApplicationNo(),"AM102","O");
										/**
										 * end by tharushi.e
										 */
										amendmentsDetailList = amendmentService
												.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
										historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
										changeButtonState();

									} else {
										setErrorMessage("Board Approval is Not On Progress");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}

								}

							} else {
								setErrorMessage("Already Complete Board Approval");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							setErrorMessage("Please Complete Committee Approval");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Board Approval Request Already On progress");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Selected Data already Rejected");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Rejected By Committee");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select A Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public StreamedContent requestPTA() throws JRException {

		routeDetDTO = amendmentService.getOldOriginDestination(selectDTO.getPermitNo());
		files = null;
		String sourceFileName = null;
		Connection conn = null;
		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

				if (amendmentService.checkPTAStatus(selectDTO, "P") == false) {

					/* Update Amendment table and Amendment history table */
					amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
							sessionBackingBean.getLoginUser());

					if (amendmentService.updatePTAStatus(selectDTO, "P", sessionBackingBean.getLoginUser())) {
						/** Update task in history table by tharushi.e **/
						commonService.updateCommonTaskHistory(selectDTO.getExisitingBusNo(),
								selectDTO.getApplicationNo(), "AM107", "C", sessionBackingBean.getLoginUser());
						/** End Update task in history table by tharushi.e **/
						setSuccessMessage("PTA Request Successfull");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						amendmentsDetailList = amendmentService
								.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
						historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
						// added PTA Request letter

						try {
							conn = ConnectionManager.getConnection();

							sourceFileName = "..//reports//PTARequestLetterForAmendments.jrxml";

							String logopath = "//lk//informatics//ntc//view//reports//";
							Map<String, Object> parameters = new HashMap<String, Object>();

							parameters.put("P_permit_no", selectDTO.getPermitNo());
							parameters.put("P_old_Origin", routeDetDTO.getOldOrigin());
							parameters.put("P_Old_Destination", routeDetDTO.getOldDestination());
							parameters.put("P_transaction", amendmentDTO.getTranCode());
							parameters.put("P_Amend_APP_no", selectDTO.getApplicationNo());

							JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

							JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

							JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

							byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
							InputStream stream = new ByteArrayInputStream(pdfByteArray);
							files = new DefaultStreamedContent(stream, "Application/pdf", "PTARequestLetter.pdf");

							ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
							Map<String, Object> sessionMap = externalContext.getSessionMap();
							sessionMap.put("reportBytes", pdfByteArray);
							sessionMap.put("docType", "pdf");

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							ConnectionManager.close(conn);
						}

						changeButtonState();

					} else {
						setErrorMessage("PTA Request Not Successfull");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Already Requested PTA");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Data already Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select A Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		return files;

	}

	public void skipPTA() {
		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

				if (amendmentService.checkPTAStatus(selectDTO, null) == true) {

					/* Update Amendment table and Amendment history table */
					amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
							sessionBackingBean.getLoginUser());

					if (amendmentService.updatePTAStatus(selectDTO, "S", sessionBackingBean.getLoginUser())) {

						setSuccessMessage("PTA Skip Successfull");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						/** Update task in history table by tharushi.e **/
						commonService.updateCommonTaskHistory(selectDTO.getExisitingBusNo(),
								selectDTO.getApplicationNo(), "AM108", "C", sessionBackingBean.getLoginUser());
						/** End Update task in history table by tharushi.e **/
						amendmentsDetailList = amendmentService
								.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
						historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
						changeButtonState();

					} else {
						setErrorMessage("PTA Skip Not Successfull");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Can Not Skip. PTA Request Already On Progress");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Data already Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select A Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void rejectPTA() {

		files = null;
		String sourceFileName = null;
		Connection conn = null;
		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

				if (amendmentService.checkPTAStatus(selectDTO, "R") == false) {

					/* Update Amendment table and Amendment history table */
					amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
							sessionBackingBean.getLoginUser());

					if (amendmentService.updatePTAStatus(selectDTO, "R", sessionBackingBean.getLoginUser())) {
						/** Update task in history table by tharushi.e **/
						commonService.updateCommonTaskHistory(selectDTO.getExisitingBusNo(),
								selectDTO.getApplicationNo(), "AM110", "C", sessionBackingBean.getLoginUser());
						/** End Update task in history table by tharushi.e **/
						setSuccessMessage("PTA Reject Successfull");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						try {
							conn = ConnectionManager.getConnection();

							sourceFileName = "..//reports//PTARejectLetterForAmendments2.jrxml";
							String logopath = "//lk//informatics//ntc//view//reports//";
							Map<String, Object> parameters = new HashMap<String, Object>();

							parameters.put("P_permit_no", selectDTO.getPermitNo());
							parameters.put("P_Amend_APP_no", selectDTO.getApplicationNo());

							JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

							JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

							JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
							jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

							byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
							InputStream stream = new ByteArrayInputStream(pdfByteArray);
							files = new DefaultStreamedContent(stream, "Application/pdf", "PTARejecttLetter.pdf");

							ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
							Map<String, Object> sessionMap = externalContext.getSessionMap();
							sessionMap.put("reportBytes", pdfByteArray);
							sessionMap.put("docType", "pdf");

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							ConnectionManager.close(conn);
						}
						// finished
						amendmentsDetailList = amendmentService
								.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
						historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
						changeButtonState();

					} else {
						setErrorMessage("PTA Reject Not Successfull");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("PTA Request Already Reject");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Data already Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select A Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public StreamedContent approvePTA() throws JRException {

		routeDetDTO = amendmentService.getOldOriginDestination(selectDTO.getPermitNo());
		files = null;
		String sourceFileName = null;
		Connection conn = null;
		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

				if (amendmentService.checkPTAStatus(selectDTO, "A") == false) {

					/* Update Amendment table and Amendment history table */
					amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
							sessionBackingBean.getLoginUser());

					if (amendmentService.updatePTAStatus(selectDTO, "A", sessionBackingBean.getLoginUser())) {

						/** Update task in history table by tharushi.e **/
						commonService.updateCommonTaskHistory(selectDTO.getExisitingBusNo(),
								selectDTO.getApplicationNo(), "AM109", "C", sessionBackingBean.getLoginUser());
						/** End Update task in history table by tharushi.e **/
						setSuccessMessage("PTA Approve Successfull");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						amendmentsDetailList = amendmentService
								.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
						historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
						changeButtonState();

					} else {
						setErrorMessage("PTA Approve Not Successfull");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("PTA Request Already Approve");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Data already Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select A Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
		return files;
	}

	public void requestTimeApproval() {

		if (selectDTO != null) {

			if (amendmentService.checkTimeApprovalStatus(selectDTO, "R") == false) {

				/* Update Amendment table and Amendment history table */
				amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
						sessionBackingBean.getLoginUser());

				if (amendmentService.updateTimeApprovalStatus(selectDTO, "R", sessionBackingBean.getLoginUser())) {

					setSuccessMessage("Time Approval Request Successfull");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					/** Update task in history table by tharushi.e **/
					commonService.updateCommonTaskHistory(selectDTO.getExisitingBusNo(), selectDTO.getApplicationNo(),
							"AM111", "C", sessionBackingBean.getLoginUser());
					/** End Update task in history table by tharushi.e **/
					amendmentsDetailList = amendmentService.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
					historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
					changeButtonState();

				} else {
					setErrorMessage("Time Approval Request Is Not Successfull");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Time Approval Request Already On Progress");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select A Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	/* need to add if else block and check transaction types */

	public StreamedContent responeLetterPrint() throws JRException {

		// generate letter according to new changes 24/06/2020
		String sourceFileName = null;
		Connection conn = null;
		routeDetDTO = amendmentService.getOldOriginDestination(selectDTO.getPermitNo());
		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//PTAApprovalLetter.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_transaction", amendmentDTO.getTranCode());
			parameters.put("P_permit_no", selectDTO.getPermitNo());
			parameters.put("P_old_Origin", routeDetDTO.getOldOrigin());
			parameters.put("P_Old_Destination", routeDetDTO.getOldDestination());
			parameters.put("P_Amend_APP_no", selectDTO.getApplicationNo());

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "PTARequestLetter.pdf");

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

	public StreamedContent responeLetterPrintForPermitOwner(String val) throws JRException {

		files = null;
		Connection conn = null;
		String sourceFileName = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//PermitTransferOnRelationship.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Permit_No", val);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "ApprovalLetter" + val + ".pdf");

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

	public StreamedContent generateResponseLetters() throws JRException {
		String sourceFileName = null;
		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableBoardStatus(selectDTO, "R") == false) {

				if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

					boolean isFoundInTaskDetails = amendmentService.checkTaskDetails(selectDTO, "AM103", "C");
					boolean isFoundInTaskHistory = amendmentService.checkTaskHistory(selectDTO, "AM103", "C");

					if (isFoundInTaskDetails == false && isFoundInTaskHistory == false) {

						if (amendmentDTO.getTranCode().equals("21") || amendmentDTO.getTranCode().equals("22")
								|| amendmentDTO.getTranCode().equals("23")) {
							/*
							 * Type Change, Route Change, End Change. Previous Task is AM101
							 */

							/* check REQ NEW ROUTE DONE OR NOT */

							/* Update Amendment table and Amendment history table */
							amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
									sessionBackingBean.getLoginUser());

							commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AM102", "AM103", "C",
									sessionBackingBean.getLoginUser());
							boolean isApproved = commonService
									.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "AM103");

							amendmentService.updateAmendmentsTableStatus(selectDTO, "GR",
									sessionBackingBean.getLoginUser());

							if (isApproved == true) {
								changeButtonState();
								historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
								setSuccessMessage("Generate Response Letter Successfull ");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								/**
								 * added for update que master table task by tharushi.e
								 */

								commonService.updateQueMasterTableTask(selectDTO.getApplicationNo(), "AM103", "C");
								/**
								 * end by tharushi.e
								 */
								amendmentsDetailList = amendmentService
										.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
								// added for generate report

								String value = selectDTO.getPermitNo();
								// added for get application number as parameter
								String val = selectDTO.getApplicationNo();
								responeLetterPrint(); // newly added 24/06/2020

							} else {
								setErrorMessage("Generate Response Letter Not Successfull");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}

						} else {
							/*
							 * Owner + Bus Change, Service + Bus Change, owner Change Previous Task is AM101
							 */

							boolean isApprovedByCommittee = amendmentService
									.CheckAmendmentsTableBoarderStatus(selectDTO);

							if (isApprovedByCommittee == true) {

								/* Update Amendment table and Amendment history table */
								amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
										sessionBackingBean.getLoginUser());

								commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AM102", "AM103", "C",
										sessionBackingBean.getLoginUser());
								boolean isApproved = commonService
										.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "AM103");

								amendmentService.updateAmendmentsTableStatus(selectDTO, "GR",
										sessionBackingBean.getLoginUser());

								if (isApproved == true) {
									changeButtonState();
									historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
									setSuccessMessage("Generate Response Letter Successfull ");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									/**
									 * added for update que master table task by tharushi.e
									 */

									commonService.updateQueMasterTableTask(selectDTO.getApplicationNo(), "AM103", "C");
									/**
									 * end by tharushi.e
									 */
									amendmentsDetailList = amendmentService
											.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
									// added for generate report

									String value = selectDTO.getPermitNo();
									// added for get application number as parameter
									String val = selectDTO.getApplicationNo();

									responeLetterPrintForPermitOwner(value);// newly added 24/06/2020

								} else {
									setErrorMessage("Generate Response Letter Not Successfull");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}

							} else {
								setErrorMessage("Please Complete Board Approval");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
						}

					} else {
						setErrorMessage("Selected Application No. Already Done Generate Response Letter");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Selected Data already Rejected");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Rejected By Board");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select A Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		return files;

	}

	public void requestNewRoute() {
		if (selectDTO != null) {

			if (amendmentService.checkTimeApprovalStatus(selectDTO, "R") == false) {

				if (amendmentService.checkTempRouteStatus(selectDTO, "R") == false) {

					/* Update Amendment table and Amendment history table */
					amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
							sessionBackingBean.getLoginUser());

					if (amendmentService.updateTempRouteStatus(selectDTO, "R", sessionBackingBean.getLoginUser())) {

						amendmentsDetailList = amendmentService
								.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
						historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
						changeButtonState();

						setSuccessMessage("New Route Request Successfull");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

					} else {
						setErrorMessage("New Route Request Is Not Successfull");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("New Route Request Already On Progress");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Data already Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select A Row");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void approveOne() {

		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

				commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AA001", "AM104", "C",
						sessionBackingBean.getLoginUser());
				boolean isApproved = commonService.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(),
						"AM104");

				/* Update Amendment table and Amendment history table */
				amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
						sessionBackingBean.getLoginUser());

				amendmentService.updateAmendmentsTableStatus(selectDTO, "FA", sessionBackingBean.getLoginUser());

				if (isApproved == true) {

					setSuccessMessage("Approval for Amendment Payments is Success.");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					amendmentsDetailList = amendmentService.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
					historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
					changeButtonState();
					updateNewPermitExpiryDate(selectDTO);
				} else {

					setErrorMessage("Approval for Amendment Payments is not Success.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Data already Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void approveTwo() {

		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

				boolean isApproveInTaskDetails = amendmentService.checkTaskDetails(selectDTO, "AM105", "C");
				boolean isApproveInTaskHistory = amendmentService.checkTaskHistory(selectDTO, "AM105", "C");

				if (isApproveInTaskDetails == false && isApproveInTaskHistory == false) {

					boolean isApproveOneInTaskPM302Details = amendmentService.checkTaskDetails(selectDTO, "PM302", "C");

					if (isApproveOneInTaskPM302Details == true) {

						/* Update Amendment table and Amendment history table */
						amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						commonService.updateTaskStatus(selectDTO.getApplicationNo(), "PM302", "AM105", "C",
								sessionBackingBean.getLoginUser());
						boolean isApproved = commonService
								.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "AM105");
						amendmentService.updateAmendmentsTableStatus(selectDTO, "SA",
								sessionBackingBean.getLoginUser());

						String message = "New Bus Change approval request received";
						String subject = "Amendment - Request Chairman/DG approval";

						commonService.sendEmail(getEmpId("CHAIRMAN_EMP_NO"), message, subject);
						commonService.sendSMS(getEmpId("CHAIRMAN_EMP_NO"), message, subject);

						if (isApproved == true) {

							setSuccessMessage("Request For DG/Chairman Approval is Successful");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
							changeButtonState();
							amendmentsDetailList = amendmentService
									.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);

						} else {
							setErrorMessage("Request For DG/Chairman Approval is not successful");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					} else {
						setErrorMessage("Payment is not completed");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Selected Data is already approved");

					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Data already Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public String getEmpId(String param) {

		String val = null;

		ParamerDTO paramDTO1 = migratedService.retrieveParameterValuesForParamName(param);
		val = paramDTO1.getStringValue();

		return val;

	}

	public void preReject() {
		if (selectDTO != null) {

			boolean isFoundAM104InTaskDetails = amendmentService.checkTaskDetails(selectDTO, "AA002", "C");
			boolean isFoundAM104InTaskHistory = amendmentService.checkTaskHistory(selectDTO, "AA002", "C");

			if (isFoundAM104InTaskDetails == false && isFoundAM104InTaskHistory == false) {

				RequestContext.getCurrentInstance().execute("PF('dlg1').show()");

			} else {
				setErrorMessage("Can Not Reject, Approved By Director");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void reject() {

		if (rejectReason != null && !rejectReason.trim().equalsIgnoreCase("")) {

			/* Update Amendment table and Amendment history table */
			amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
					sessionBackingBean.getLoginUser());

			boolean isRejected = amendmentService.updateRejectData(selectDTO, rejectReason,
					sessionBackingBean.getLoginUser());
			if (isRejected == true) {
				historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
				setSuccessMessage("Successfully Rejected.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				RequestContext.getCurrentInstance().execute("PF('dlg1').hide()");
				amendmentsDetailList = amendmentService.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);

			} else {

				setErrorMessage("Rejection is not Successful");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				changeButtonState();
			}

		} else {
			setErrorMessage("Please Enter The Reject Reason");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	/**
	 * 
	 * tharushi.e for Reprint Permit report fire only when PM400 C in task det table
	 */
	public void ReprintPermit() throws JRException {

		if (selectDTO != null) {

			prinAction();
			disabledRePrint = true;
		} else

		{
			setErrorMessage("Please Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void printPermit() throws JRException {
		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

				if (selectDTO.getTranCode().equals("14")) {

					/* Owner Change */

					boolean isCountinue = amendmentService.checkFourChanges(selectDTO, "13", "21", "22", "23");
					/* Checking Pending Amendments */
					if (isCountinue == true) {

						RequestContext.getCurrentInstance().execute("PF('confirmMSGWithPermitNO').show()");
					} else {

						amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						ominiBusHistoryDTO = historyService.getOminiBusTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						ownerHistoryDTO = historyService.getVehicleOwnerTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						/* For NO button in confirm dialog box */
						// 20-10-2020 dinushi.r
						// String permitNo = issuePermitService.generatePermitNo();
						String permitNo = issuePermitService.generatePermitNoNew(selectDTO.getApplicationNo());

						amendmentService.updateNewPermitNoOnAmendmentForOwnerChange(selectDTO, permitNo,
								sessionBackingBean.getLoginUser());
						amendmentService.updateApplicationTableOldRecordForOwnerChange(selectDTO, permitNo);
						/**
						 * update old permit number in new application number special remarks and new
						 * permit number in old application number special remarks
						 **/

						amendmentService.updateApplicationTableRemarkOldRecordForOwnerChange(selectDTO, permitNo);
						/** end **/
						amendmentService.updateApplicationTableNewRecordForOwnerChange(selectDTO, permitNo,
								sessionBackingBean.getLoginUser());
						amendmentService.updateVehicelOwnerTableOwnerChange(selectDTO, permitNo,
								sessionBackingBean.getLoginUser());

						amendmentService.updateOminiBusTableOwnerChange(selectDTO, permitNo,
								sessionBackingBean.getLoginUser());

						commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AA002", "PM400", "C",
								sessionBackingBean.getLoginUser());
						boolean isComplete = commonService
								.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "PM400");

						if (isComplete == true) {
							setSuccessMessage("Print Permit For Permit Holder Is Successfull");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							/**
							 * added for update que master table task by tharushi.e
							 */

							commonService.updateQueMasterTableTask(selectDTO.getApplicationNo(), "PM400", "C");
							/**
							 * end by tharushi.e
							 */
							
							/*** temporary active vehicle number status roll back in complain request table***/
							
						
							boolean isTemporaryActive =commonService.isTemporaryActiveBusNumber(selectDTO.getBusRegNo());
									if(isTemporaryActive) {
										commonService.insertDataIntoComplainRequestHistoryAndUpdate(selectDTO.getBusRegNo(),sessionBackingBean.getLoginUser());
										
									}
							
							/***end***/
							// create permit Folder

							historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
							historyService.insertOminiBusHistoryData(ominiBusHistoryDTO);
							historyService.insertVehicleOwnerHistoryData(ownerHistoryDTO);
							historyService.insertApplicationHistoryData(applicationHistoryDTO);

							String oldApplicationNo = amendmentService.getOldApplicationNo(selectDTO);
							String oldPermitNo = amendmentService.getOldPermitNo(oldApplicationNo);

							// update old permit no in application table
							amendmentService.updateOldPermitNoInAppTb(selectDTO.getApplicationNo(), oldPermitNo,
									permitNo);

							amendmentDTO.setOldpermitNo(oldPermitNo);
							copyDocument_Permit(permitNo);

							prinAction();
							disabledPrint = true;
							amendmentsDetailList = amendmentService
									.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);

						} else {
							setErrorMessage("Print Permit For Permit Holder Change Is Not Successfull");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					}

				} else if (selectDTO.getTranCode().equals("15")) {

					boolean isCountinue = amendmentService.checkOneChanges(selectDTO, "16");

					if (isCountinue == true) {

						RequestContext.getCurrentInstance().execute("PF('confirmMSGWithPermitNO').show()");

					} else {

						amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						ominiBusHistoryDTO = historyService.getOminiBusTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						ownerHistoryDTO = historyService.getVehicleOwnerTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						
						// 20-10-2021 dinushi.r
						// String permitNo = issuePermitService.generatePermitNo();
						String permitNo = issuePermitService.generatePermitNoNew(selectDTO.getApplicationNo());
						
						amendmentService.updateNewPermitNoOnAmendmentForOwnerChange(selectDTO, permitNo,
								sessionBackingBean.getLoginUser());
						amendmentService.updateApplicationTableOldRecordForOwnerChange(selectDTO, permitNo);
						amendmentService.updateApplicationTableNewRecordForOwnerChange(selectDTO, permitNo,
								sessionBackingBean.getLoginUser());

						commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AA002", "PM400", "C",
								sessionBackingBean.getLoginUser());

						amendmentService.updateVehicelOwnerTableOwnerChange(selectDTO, permitNo,
								sessionBackingBean.getLoginUser());

						amendmentService.updateOminiBusTableOwnerChange(selectDTO, permitNo,
								sessionBackingBean.getLoginUser());

						boolean isComplete = commonService
								.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "PM400");

						if (isComplete == true) {
							setSuccessMessage("Print Permit For Permit Holder and Bus Change Is Successfull");

							historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
							historyService.insertOminiBusHistoryData(ominiBusHistoryDTO);
							historyService.insertVehicleOwnerHistoryData(ownerHistoryDTO);
							historyService.insertApplicationHistoryData(applicationHistoryDTO);

							String oldApplicationNo = amendmentService.getOldApplicationNo(selectDTO);
							String oldPermitNo = amendmentService.getOldPermitNo(oldApplicationNo);

							amendmentDTO.setOldpermitNo(oldPermitNo);

							copyDocument_Permit(permitNo);

							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							/**
							 * added for update que master table task by tharushi.e
							 */

							commonService.updateQueMasterTableTask(selectDTO.getApplicationNo(), "PM400", "C");
							/**
							 * end by tharushi.e
							 */
							prinAction();
							/*** temporary active vehicle number status roll back in complain request table***/
							
							
							boolean isTemporaryActive =commonService.isTemporaryActiveBusNumber(selectDTO.getBusRegNo());
									if(isTemporaryActive) {
										commonService.insertDataIntoComplainRequestHistoryAndUpdate(selectDTO.getBusRegNo(),sessionBackingBean.getLoginUser());
										
									}
							/***end***/
							disabledPrint = true;
							amendmentsDetailList = amendmentService
									.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);

							copyDocument_Permit(permitNo);
						} else {
							setErrorMessage("Print Permit For Owner and Bus Change Is Not Successfull");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					}

				} else if (selectDTO.getTranCode().equals("13")) {

					boolean isCountinue = amendmentService.checkFourChanges(selectDTO, "14", "21", "22", "23");

					if (isCountinue == true) {

						RequestContext.getCurrentInstance().execute("PF('confirmMSGWithOutPermitNO').show()");

					} else {

						amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						amendmentService.updateAmendmentForServiceAndBusChange(selectDTO,
								sessionBackingBean.getLoginUser());
						amendmentService.updateApplicationTableOldRecordForServiceAndBusChange(selectDTO);
						amendmentService.updateApplicationTableNewRecordForServiceAndBusChange(selectDTO,
								sessionBackingBean.getLoginUser());

						commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AA002", "PM400", "C",
								sessionBackingBean.getLoginUser());
						boolean isComplete = commonService
								.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "PM400");

						if (isComplete == true) {
							setSuccessMessage("Print Permit For Bus Change Is Successfull");

							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							/**
							 * added for update que master table task by tharushi.e
							 */

							commonService.updateQueMasterTableTask(selectDTO.getApplicationNo(), "PM400", "C");
							/**
							 * end by tharushi.e
							 */
							historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
							historyService.insertApplicationHistoryData(applicationHistoryDTO);

							prinAction();
		/*** temporary active vehicle number status roll back in complain request table***/
							
							
							boolean isTemporaryActive =commonService.isTemporaryActiveBusNumber(selectDTO.getExisitingBusNo());
									if(isTemporaryActive) {
										commonService.insertDataIntoComplainRequestHistoryAndUpdate(selectDTO.getExisitingBusNo(),sessionBackingBean.getLoginUser());
										
									}
							/***end***/
							disabledPrint = true;
							amendmentsDetailList = amendmentService
									.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
						} else {
							setErrorMessage("Print Permit  For Bus Change Is Not Successfull");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					}

				} else if (selectDTO.getTranCode().equals("21") || selectDTO.getTranCode().equals("22")
						|| selectDTO.getTranCode().equals("23")) {

					boolean isCountinue = amendmentService.checkTwoChanges(selectDTO, "14", "13");

					if (isCountinue == true) {

						RequestContext.getCurrentInstance().execute("PF('confirmMSGWithOutPermitNO').show()");

					} else {

						amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						amendmentService.updateAmendmentForServiceAndBusChange(selectDTO,
								sessionBackingBean.getLoginUser());
						amendmentService.updateApplicationTableOldRecordForServiceAndBusChange(selectDTO);
						amendmentService.updateApplicationTableNewRecordForServiceAndBusChange(selectDTO,
								sessionBackingBean.getLoginUser());

						commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AA002", "PM400", "C",
								sessionBackingBean.getLoginUser());
						boolean isComplete = commonService
								.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "PM400");

						if (isComplete == true) {
							setSuccessMessage("Print Permit For Service Change Is Successfull");

							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							/**
							 * added for update que master table task by tharushi.e
							 */

							commonService.updateQueMasterTableTask(selectDTO.getApplicationNo(), "PM400", "C");
							/**
							 * end by tharushi.e
							 */
							historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
							historyService.insertApplicationHistoryData(applicationHistoryDTO);

							prinAction();
		/*** temporary active vehicle number status roll back in complain request table***/
							
							
							boolean isTemporaryActive =commonService.isTemporaryActiveBusNumber(selectDTO.getBusRegNo());
									if(isTemporaryActive) {
										commonService.insertDataIntoComplainRequestHistoryAndUpdate(selectDTO.getBusRegNo(),sessionBackingBean.getLoginUser());
										
									}
							/***end***/
							disabledPrint = true;
							amendmentsDetailList = amendmentService
									.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
						} else {
							setErrorMessage("Print Permit  For Service Change Is Not Successfull");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					}

				} else if (selectDTO.getTranCode().equals("16")) {

					boolean isCountinue = amendmentService.checkOneChanges(selectDTO, "15");

					if (isCountinue == true) {

						RequestContext.getCurrentInstance().execute("PF('confirmMSGWithOutPermitNO').show()");

					} else {

						amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());

						amendmentService.updateAmendmentForServiceAndBusChange(selectDTO,
								sessionBackingBean.getLoginUser());
						amendmentService.updateApplicationTableOldRecordForServiceAndBusChange(selectDTO);
						amendmentService.updateApplicationTableNewRecordForServiceAndBusChange(selectDTO,
								sessionBackingBean.getLoginUser());

						commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AA002", "PM400", "C",
								sessionBackingBean.getLoginUser());
						boolean isComplete = commonService
								.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "PM400");

						if (isComplete == true) {
							setSuccessMessage("Print Permit For Service and Bus Change Is Successfull");

							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							/**
							 * added for update que master table task by tharushi.e
							 */

							commonService.updateQueMasterTableTask(selectDTO.getApplicationNo(), "PM400", "C");
							/**
							 * end by tharushi.e
							 */
							historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
							historyService.insertApplicationHistoryData(applicationHistoryDTO);

							prinAction();
		/*** temporary active vehicle number status roll back in complain request table***/
							
							
							boolean isTemporaryActive =commonService.isTemporaryActiveBusNumber(selectDTO.getBusRegNo());
									if(isTemporaryActive) {
										commonService.insertDataIntoComplainRequestHistoryAndUpdate(selectDTO.getBusRegNo(),sessionBackingBean.getLoginUser());
										
									}
							/***end***/
							disabledPrint = true;
							amendmentsDetailList = amendmentService
									.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
						} else {
							setErrorMessage("Print Permit  For Service and Bus Change Is Not Successfull");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}

					}

				} else {
					setErrorMessage("Can Not Find Transaction Type");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Data already Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else

		{
			setErrorMessage("Please Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	private void updateNewPermitExpiryDate(AmendmentDTO selectDTO2) {

		if (selectDTO2.getApplicationNo() != null && !selectDTO2.getApplicationNo().isEmpty()
				&& !selectDTO2.getApplicationNo().trim().equalsIgnoreCase("")) {
			String selectedAppNo = selectDTO2.getApplicationNo();
			permitRenewalsDTO = adminService.renewalsByApplicationNoWithNewExpiredDate(selectedAppNo);
			permitRenewalsDTO.setApplicationNo(selectedAppNo);
			if (permitRenewalsDTO.getNewPermitExpirtDate() != null) {

				applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
						sessionBackingBean.getLoginUser());
				boolean updatePermitDate = adminService.updatePermitDateN(permitRenewalsDTO,
						sessionBackingBean.getLoginUser());
				if (updatePermitDate == true) {

					historyService.insertApplicationHistoryData(applicationHistoryDTO);
				} else {

				}
			} else {

			}

		}

	}

	public void copyDocument(String permitNo) {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}
		String originalPath = propertyFilePath;

		String newCreatedPath = originalPath + File.separator + permitNo;

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {

			} else {

			}
		} else {

		}

		String source = originalPath + File.separator + amendmentDTO.getApplicationNo();
		File srcDir = new File(source);

		String destination = originalPath + File.separator + permitNo;
		File destDir = new File(destination);

		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void copyDocument_Permit(String permitNo) {

		String propertyFilePath = null;
		try {
			propertyFilePath = PropertyReader.getPropertyValue("documentManagement.upload.file.path");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}
		String originalPath = propertyFilePath;

		String newCreatedPath = originalPath + File.separator + permitNo;

		File file1 = new File(newCreatedPath);
		if (!file1.exists()) {
			if (file1.mkdir()) {

			} else {

			}
		} else {

		}

		String source = originalPath + File.separator + amendmentDTO.getOldpermitNo();
		File srcDir = new File(source);

		String destination = originalPath + File.separator + permitNo;
		File destDir = new File(destination);

		try {
			FileUtils.copyDirectory(srcDir, destDir);

			ApplicationDocuments = amendmentService.getFilePaths(amendmentDTO.getOldpermitNo());

			String user = sessionBackingBean.getLoginUser();
			for (int i = 0; i < ApplicationDocuments.size(); i++) {

				String filepath = ApplicationDocuments.get(i).getFilePath();

				String newFilePath = filepath.replaceAll(amendmentDTO.getOldpermitNo(), permitNo);

				amendmentDTO.setDocumentTransactionType(ApplicationDocuments.get(i).getDocumentTransactionType());
				;
				amendmentDTO.setDocApplicationNo(ApplicationDocuments.get(i).getDocApplicationNo());
				amendmentDTO.setDocCode(ApplicationDocuments.get(i).getDocCode());
				amendmentDTO.setDocDes(ApplicationDocuments.get(i).getDocDes());
				amendmentDTO.setDocType(ApplicationDocuments.get(i).getDocType());

				amendmentService.updateNewPermitDocuments(amendmentDTO, permitNo, user, newFilePath);

			}

			ApplicationVersionDocuments = amendmentService.getVersionFilePaths(amendmentDTO.getOldpermitNo());

			for (int i = 0; i < ApplicationVersionDocuments.size(); i++) {

				String filepath = ApplicationVersionDocuments.get(i).getFilePath();

				String newFilePath = filepath.replaceAll(amendmentDTO.getOldpermitNo(), permitNo);

				amendmentDTO
						.setDocumentTransactionType(ApplicationVersionDocuments.get(i).getDocumentTransactionType());
				;
				amendmentDTO.setDocApplicationNo(ApplicationVersionDocuments.get(i).getDocApplicationNo());
				amendmentDTO.setDocCode(ApplicationVersionDocuments.get(i).getDocCode());
				amendmentDTO.setDocDes(ApplicationVersionDocuments.get(i).getDocDes());
				amendmentDTO.setDocType(ApplicationVersionDocuments.get(i).getDocType());
				amendmentDTO.setDocVersionNo(ApplicationVersionDocuments.get(i).getDocVersionNo());

				amendmentService.updateNewPermitDocumentsVersion(amendmentDTO, permitNo, user, newFilePath);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < ApplicationDocuments.size(); i++) {

			String filepath = ApplicationDocuments.get(i).getFilePath();

			String newFilePath = filepath.replaceAll(amendmentDTO.getOldpermitNo(), permitNo);

		}

	}

	public void printPermitMethodsWithPermitNo() throws JRException {

		amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
				sessionBackingBean.getLoginUser());
		applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
				sessionBackingBean.getLoginUser());

		String permitNo = issuePermitService.generatePermitNo();
		amendmentService.updateNewPermitNoOnAmendmentForOwnerChange(selectDTO, permitNo,
				sessionBackingBean.getLoginUser());
		amendmentService.updateApplicationTableOldRecordForOwnerChange(selectDTO, permitNo);
		amendmentService.updateApplicationTableNewRecordForOwnerChange(selectDTO, permitNo,
				sessionBackingBean.getLoginUser());

		commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AM105", "PM400", "C",
				sessionBackingBean.getLoginUser());
		boolean isComplete = commonService.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(),
				"PM400");

		if (isComplete == true) {
			setSuccessMessage("Print Permit For Permit Holder Is Successfull");
			String oldApplicationNo = amendmentService.getOldApplicationNo(selectDTO);
			historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
			historyService.insertApplicationHistoryData(applicationHistoryDTO);
			String oldPermitNo = amendmentService.getOldPermitNo(oldApplicationNo);

			amendmentDTO.setOldpermitNo(oldPermitNo);

			copyDocument_Permit(permitNo);
			RequestContext.getCurrentInstance().update("frmsuccess");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			prinAction();
			amendmentsDetailList = amendmentService.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
		} else {
			setErrorMessage("Print Permit For Permit Holder Change Is Not Successfull");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		RequestContext.getCurrentInstance().execute("PF('confirmMSGWithPermitNO').hide()");
	}

	public void printPermitMethodsWithOutPermitNo() throws JRException {

		amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
				sessionBackingBean.getLoginUser());
		applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
				sessionBackingBean.getLoginUser());

		amendmentService.updateAmendmentForServiceAndBusChange(selectDTO, sessionBackingBean.getLoginUser());
		amendmentService.updateApplicationTableOldRecordForServiceAndBusChange(selectDTO);
		amendmentService.updateApplicationTableNewRecordForServiceAndBusChange(selectDTO,
				sessionBackingBean.getLoginUser());

		if (selectDTO.getTranCode().equals("13")) {
			commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AA002", "PM400", "C",
					sessionBackingBean.getLoginUser());
		} else {
			commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AM105", "PM400", "C",
					sessionBackingBean.getLoginUser());
		}

		boolean isComplete = commonService.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(),
				"PM400");

		if (isComplete == true) {
			setSuccessMessage("Print Permit For Bus/Service Change Is Successfull");

			RequestContext.getCurrentInstance().update("frmsuccess");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			prinAction();
			amendmentsDetailList = amendmentService.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
			historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
			historyService.insertApplicationHistoryData(applicationHistoryDTO);
		} else {
			setErrorMessage("Print Permit  For Bus/Service Change Is Not Successfull");
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

		RequestContext.getCurrentInstance().execute("PF('confirmMSGWithOutPermitNO').hide()");

	}

	public String view() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		sessionBackingBean.setPageMode("V");
		sessionBackingBean.setApproveURL(request.getRequestURL().toString());
		sessionBackingBean.setSearchURL(null);
		sessionBackingBean.setApproveURLStatus(true);
		sessionBackingBean.setAmendmentsApplicationNo(viewSelectDTO.getApplicationNo());
		sessionBackingBean.amendmentsList = amendmentsDetailList;
		sessionBackingBean.amendmentsViewMood = true;

		return "/pages/amendmentsToBusOrOwner/viewBusOwnerServiceAmendment.xhtml#!";

	}

	public void prinAction() throws JRException {
		files = null;
		String sourceFileName = null;
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//NewPermitInformation.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_APP", amendmentDTO.getApplicationNo());
			parameters.put("P_LOGO", logopath);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "New Permit Printing.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

	}

	// added by tharushi.e
	public void generateReport() {

		String sourceFileName = null;
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			String logopath = "//lk//informatics//ntc//view//reports//";

			if (amendmentDTO.getTranCode().equals("21")) {

				sourceFileName = "..//reports//AmmenmendtToServiceTypeCommitte.jrxml";
			} else if (amendmentDTO.getTranCode().equals("22")) {

				sourceFileName = "..//reports//AmmenmendtToRouteCommitte.jrxml";
			} else if (amendmentDTO.getTranCode().equals("23")) {

				sourceFileName = "..//reports//AmmenmendtToEndChange.jrxml";
			} else if (amendmentDTO.getTranCode().equals("14")) {

				sourceFileName = "..//reports//AmmenmendtToOwnerChangeCommitteApproveSin.jrxml";
			}
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
			files = new DefaultStreamedContent(stream, "Application/pdf", "Report.pdf");

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

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public AmendmentDTO getAmendmentDTO() {
		return amendmentDTO;
	}

	public void setAmendmentDTO(AmendmentDTO amendmentDTO) {
		this.amendmentDTO = amendmentDTO;
	}

	public AmendmentService getAmendmentService() {
		return amendmentService;
	}

	public void setAmendmentService(AmendmentService amendmentService) {
		this.amendmentService = amendmentService;
	}

	public List<AmendmentDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<AmendmentDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<AmendmentDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<AmendmentDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public AmendmentDTO getFilledamendmentDTO() {
		return filledamendmentDTO;
	}

	public void setFilledamendmentDTO(AmendmentDTO filledamendmentDTO) {
		this.filledamendmentDTO = filledamendmentDTO;
	}

	public AmendmentDTO getViewSelectDTO() {
		return viewSelectDTO;
	}

	public void setViewSelectDTO(AmendmentDTO viewSelectDTO) {
		this.viewSelectDTO = viewSelectDTO;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisabledReject() {
		return disabledReject;
	}

	public void setDisabledReject(boolean disabledReject) {
		this.disabledReject = disabledReject;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public boolean isDisabledCancel() {
		return disabledCancel;
	}

	public void setDisabledCancel(boolean disabledCancel) {
		this.disabledCancel = disabledCancel;
	}

	public boolean isDisabledPrint() {
		return disabledPrint;
	}

	public void setDisabledPrint(boolean disabledPrint) {
		this.disabledPrint = disabledPrint;
	}

	public boolean isDisabledApproveTwo() {
		return disabledApproveTwo;
	}

	public void setDisabledApproveTwo(boolean disabledApproveTwo) {
		this.disabledApproveTwo = disabledApproveTwo;
	}

	public boolean isDisabledApproveOne() {
		return disabledApproveOne;
	}

	public void setDisabledApproveOne(boolean disabledApproveOne) {
		this.disabledApproveOne = disabledApproveOne;
	}

	public AmendmentDTO getSelectDTO() {
		return selectDTO;
	}

	public IssuePermitService getIssuePermitService() {
		return issuePermitService;
	}

	public void setIssuePermitService(IssuePermitService issuePermitService) {
		this.issuePermitService = issuePermitService;
	}

	public boolean isDisabledGenerateResponse() {
		return disabledGenerateResponse;
	}

	public void setDisabledGenerateResponse(boolean disabledGenerateResponse) {
		this.disabledGenerateResponse = disabledGenerateResponse;
	}

	public boolean isDisabledBoardApprove() {
		return disabledBoardApprove;
	}

	public void setDisabledBoardApprove(boolean disabledBoardApprove) {
		this.disabledBoardApprove = disabledBoardApprove;
	}

	public boolean isDisabledCommitteeApprove() {
		return disabledCommitteeApprove;
	}

	public void setDisabledCommitteeApprove(boolean disabledCommitteeApprove) {
		this.disabledCommitteeApprove = disabledCommitteeApprove;
	}

	public void setSelectDTO(AmendmentDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public List<AmendmentDTO> getAmendmentsDetailList() {
		return amendmentsDetailList;
	}

	public void setAmendmentsDetailList(List<AmendmentDTO> amendmentsDetailList) {
		this.amendmentsDetailList = amendmentsDetailList;
	}

	public List<AmendmentDTO> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<AmendmentDTO> transactionList) {
		this.transactionList = transactionList;
	}

	public boolean isDisabledPermitNo() {
		return disabledPermitNo;
	}

	public void setDisabledPermitNo(boolean disabledPermitNo) {
		this.disabledPermitNo = disabledPermitNo;
	}

	public boolean isDisabledApplicationNo() {
		return disabledApplicationNo;
	}

	public void setDisabledApplicationNo(boolean disabledApplicationNo) {
		this.disabledApplicationNo = disabledApplicationNo;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public PermitRenewalsDTO getPermitRenewalsDTO() {
		return permitRenewalsDTO;
	}

	public void setPermitRenewalsDTO(PermitRenewalsDTO permitRenewalsDTO) {
		this.permitRenewalsDTO = permitRenewalsDTO;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<AmendmentDTO> getApplicationDocuments() {
		return ApplicationDocuments;
	}

	public void setApplicationDocuments(List<AmendmentDTO> applicationDocuments) {
		ApplicationDocuments = applicationDocuments;
	}

	public boolean isDisabledTimeApproval() {
		return disabledTimeApproval;
	}

	public void setDisabledTimeApproval(boolean disabledTimeApproval) {
		this.disabledTimeApproval = disabledTimeApproval;
	}

	public boolean isDisabledSkipPTA() {
		return disabledSkipPTA;
	}

	public void setDisabledSkipPTA(boolean disabledSkipPTA) {
		this.disabledSkipPTA = disabledSkipPTA;
	}

	public boolean isDisabledReqPTA() {
		return disabledReqPTA;
	}

	public void setDisabledReqPTA(boolean disabledReqPTA) {
		this.disabledReqPTA = disabledReqPTA;
	}

	public boolean isDisabledNewRoute() {
		return disabledNewRoute;
	}

	public void setDisabledNewRoute(boolean disabledNewRoute) {
		this.disabledNewRoute = disabledNewRoute;
	}

	public boolean isRenderOne() {
		return renderOne;
	}

	public void setRenderOne(boolean renderOne) {
		this.renderOne = renderOne;
	}

	public String getLabelForApproveForPrint() {
		return labelForApproveForPrint;
	}

	public void setLabelForApproveForPrint(String labelForApproveForPrint) {
		this.labelForApproveForPrint = labelForApproveForPrint;
	}

	public boolean isDisabledRejectPTA() {
		return disabledRejectPTA;
	}

	public void setDisabledRejectPTA(boolean disabledRejectPTA) {
		this.disabledRejectPTA = disabledRejectPTA;
	}

	public boolean isDisabledApprovePTA() {
		return disabledApprovePTA;
	}

	public void setDisabledApprovePTA(boolean disabledApprovePTA) {
		this.disabledApprovePTA = disabledApprovePTA;
	}

	public List<AmendmentDTO> getApplicationVersionDocuments() {
		return ApplicationVersionDocuments;
	}

	public void setApplicationVersionDocuments(List<AmendmentDTO> applicationVersionDocuments) {
		ApplicationVersionDocuments = applicationVersionDocuments;
	}

	public boolean isDisabledPrintButton() {
		return disabledPrintButton;
	}

	public void setDisabledPrintButton(boolean disabledPrintButton) {
		this.disabledPrintButton = disabledPrintButton;
	}

	public AmendmentDTO getAmendmentHistoryDTO() {
		return amendmentHistoryDTO;
	}

	public void setAmendmentHistoryDTO(AmendmentDTO amendmentHistoryDTO) {
		this.amendmentHistoryDTO = amendmentHistoryDTO;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	public PermitRenewalsDTO getApplicationHistoryDTO() {
		return applicationHistoryDTO;
	}

	public void setApplicationHistoryDTO(PermitRenewalsDTO applicationHistoryDTO) {
		this.applicationHistoryDTO = applicationHistoryDTO;
	}

	public PermitRenewalsDTO getOwnerHistoryDTO() {
		return ownerHistoryDTO;
	}

	public void setOwnerHistoryDTO(PermitRenewalsDTO ownerHistoryDTO) {
		this.ownerHistoryDTO = ownerHistoryDTO;
	}

	public OminiBusDTO getOminiBusHistoryDTO() {
		return ominiBusHistoryDTO;
	}

	public void setOminiBusHistoryDTO(OminiBusDTO ominiBusHistoryDTO) {
		this.ominiBusHistoryDTO = ominiBusHistoryDTO;
	}

	public AmendmentBusOwnerDTO getRouteDetDTO() {
		return routeDetDTO;
	}

	public void setRouteDetDTO(AmendmentBusOwnerDTO routeDetDTO) {
		this.routeDetDTO = routeDetDTO;
	}

	public boolean isDisablGenRepo() {
		return disablGenRepo;
	}

	public void setDisablGenRepo(boolean disablGenRepo) {
		this.disablGenRepo = disablGenRepo;
	}

	public boolean isDisabledRePrint() {
		return disabledRePrint;
	}

	public void setDisabledRePrint(boolean disabledRePrint) {
		this.disabledRePrint = disabledRePrint;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

}
