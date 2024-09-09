package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PaymentTypeDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.ServiceAmountDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.RouteCreatorService;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.ConvertNumberToWord;
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

@ViewScoped
@ManagedBean(name = "terminalPaymentNewBackingBean")
public class TerminalPaymentNewBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String sucessMsg;
	private String errorMsg;

	String dateFormatStr = "dd/MM/yyyy";
	DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);

	private TerminalManagementService terminalManagementService;
	private RouteCreatorService routeCreatorService;
	private EmployeeProfileService employeeProfileService;
	List<RouteCreationDTO> routeList = new ArrayList<RouteCreationDTO>();

	private List<PaymentTypeDTO> paymentTypeList;
	private String selectedPayTypeCode;
	private PaymentTypeDTO selectedPayType = new PaymentTypeDTO();
	private List<CommonDTO> genderList;
	private List<CommonDTO> serviceTypeList;
	private List<CommonDTO> paymentModes;
	private String vehicleRegNo;
	private Date dueDate;
	private Date paidDate;
	private Date issuedDate;
	private String issuedMonth;
	private Date validFromDate;
	private Date validToDate;
	private long noOfTurn;
	private String payMode;
	private String payReciptRef;

	/*
	 * private Long basicAmount; private Long penalty; private Long amountPaid;
	 * private Long totalCollection;
	 */

	private double basicAmount;
	private double penalty;
	private double amountPaid;
	private double totalCollection;

	private String permitNo;
	private String routeOrigin;
	private String routeOriginCode;
	private String routeDestination;
	private String routeDestinationCode;
	private String routeVia;
	private String owner;
	private String gender;
	private String contactNo;
	private long receiptCount;
	private boolean receiptGenerated;
	private String vehicleServiceType;

	private String tsPermitNo;
	private boolean withoutNTCPermit;
	private String expireDate;
	private String tsOriginDesc;
	private String tsOriginCode;
	private String tsDestination;
	private String tsVia;
	private String tsOwner;
	private String tsGender;
	private String tsContactNo;
	private String tsVehicleServiceType;
	private String hirePermitRemark;
	private String routeNo;
	private String tsRouteNo;
	private Date expireDateN;

	static String LOG_SHEET_ISSUANCE = "001";
	static String TEMPORARY_PERMIT_ISSUANCE = "002";
	static String SPECIAL_HIRE_PERMIT_ISSUANCE = "003";

	private boolean enableReceipt, enablePermit;
	Date currentDate = new Date();
	private StreamedContent files;

	// payment module
	private List<ServiceAmountDTO> serviceAmountList;
	private List<ServiceAmountDTO> oldServiceAmountList;
	/*
	 * private Long basicAmountOld; private Long penaltyOld;
	 */
	private double basicAmountOld;
	private double penaltyOld;
	private boolean registered;
	private String selectedTerminalLocation;
	private List<String> terminalLocationList;
	private String stationName = null;
	private boolean localcheckcounter = true;
	boolean showStationPopup = true;
	private String stationDes;
	private double userChangePenaltyVal = 0;
	private boolean penaltyDisable = false;

	public TerminalPaymentNewBackingBean() {
		terminalManagementService = (TerminalManagementService) SpringApplicationContex
				.getBean("terminalManagementService");
		routeCreatorService = (RouteCreatorService) SpringApplicationContex.getBean("routeCreatorService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");

		paymentTypeList = terminalManagementService.getPaymentTypes();
		routeList = routeCreatorService.getAllRoutes();
		genderList = employeeProfileService.GetGenderToDropdown();
		serviceTypeList = terminalManagementService.getServiceTypeToDropdown();
		paymentModes = terminalManagementService.getPaymentModes();
		enablePermit = true;
		withoutNTCPermit = true;

	}

	public void onCounterSelect() {
		sessionBackingBean.setStationName(selectedTerminalLocation);
		stationName = terminalManagementService.getStationNameByID(selectedTerminalLocation);
		localcheckcounter = false;
		RequestContext context = RequestContext.getCurrentInstance();

		context.execute("PF('dlg2').hide();");

	}

	public void openStationPopUp() {

		selectedTerminalLocation = null;
		stationName = null;
		localcheckcounter = true;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlg2').show();");

	}

	public void cancelDialog() {
		sessionBackingBean.setStationName(selectedTerminalLocation);
		stationName = terminalManagementService.getStationNameByID(selectedTerminalLocation);
	}

	public void handleClose() throws InterruptedException {

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/pages/home/welcomePage.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onPaymentTypeChange(ValueChangeEvent e) {
		clearFields();
		basicAmount = (long) 0;
		penalty = (long) 0;
		amountPaid = (long) 0;
		totalCollection = (long) 0;
		expireDateN = null;

		if (e.getNewValue() != null && !e.getNewValue().toString().trim().isEmpty()) {
			selectedPayTypeCode = e.getNewValue().toString();

			for (PaymentTypeDTO type : nullSafe(paymentTypeList))
				if (type.getCode().equals(e.getNewValue())) {
					selectedPayType = type;
					penalty = (long) 0;
					break;
				}

			if (selectedPayType.getCode().equals(LOG_SHEET_ISSUANCE)) {

				if (permitNo != null && !permitNo.trim().isEmpty()) {
					vehicleServiceType = terminalManagementService.getServiceTypeByPermitNo(permitNo);
					basicAmount = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, vehicleServiceType)[0];
				}

			} else if (selectedPayType.getCode().equals(TEMPORARY_PERMIT_ISSUANCE)) {

				tsPermitNo = terminalManagementService.generatePermitNo();
				basicAmount = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, null)[0];

			} else if (selectedPayType.getCode().equals(SPECIAL_HIRE_PERMIT_ISSUANCE)) {

				tsPermitNo = terminalManagementService.generateSpecialPermitNo();
				basicAmount = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, null)[0];
			}

			amountPaid = basicAmount + penalty;
			totalCollection = terminalManagementService.getTotalCollectionAmt() + amountPaid;
			calculatePenalty();

		}
	}

	public void onPermitNoChange() {
		if (selectedPayType != null && selectedPayType.getCode() != null
				&& selectedPayType.getCode().equals(LOG_SHEET_ISSUANCE)) {
			validatePermitNo();
		}
		if (permitNo != null && !permitNo.trim().isEmpty())
			permitNo = permitNo.trim().toUpperCase();
		registered = false;

		PermitDTO permitDetailDTO = terminalManagementService.getPermitInfoByBusNoPermitNo(permitNo, null);
		if (permitDetailDTO.getBusRegNo() != null) {
			registered = true;

			vehicleRegNo = permitDetailDTO.getBusRegNo();
			routeOrigin = permitDetailDTO.getOrigin();
			routeDestination = permitDetailDTO.getDestination();
			routeVia = permitDetailDTO.getVia();
			owner = permitDetailDTO.getVehicleOwner();
			gender = permitDetailDTO.getOwnerGender();
			contactNo = permitDetailDTO.getMobileNo();
			if (contactNo == null || contactNo.isEmpty())
				contactNo = permitDetailDTO.getTelephoneNo();

			expireDateN = permitDetailDTO.getNewExpirDate();
			withoutNTCPermit = true;
			routeNo = permitDetailDTO.getRouteNo();
		}

		else {
			vehicleServiceType = null;

			if (selectedPayType.getCode().equals(TEMPORARY_PERMIT_ISSUANCE)
					|| selectedPayType.getCode().equals(SPECIAL_HIRE_PERMIT_ISSUANCE)) {
				withoutNTCPermit = false;
			}
			clearfieldsNew();
		}

		if (selectedPayType != null && selectedPayType.getCode() != null
				&& selectedPayType.getCode().equals(LOG_SHEET_ISSUANCE)) {
			vehicleServiceType = terminalManagementService.getServiceTypeByPermitNo(permitNo);
			basicAmount = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, vehicleServiceType)[0];
			amountPaid = basicAmount + penalty;
			totalCollection = terminalManagementService.getTotalCollectionAmt() + amountPaid;
			calculatePenalty();
		}
	}

	public void onVehicleNoChange() {
		if (selectedPayType.getCode() == null) {
			errorMsg = "Please Select the Payment Type to Proceed.!";
			RequestContext.getCurrentInstance().update("frmwarningMsge");
			RequestContext.getCurrentInstance().execute("PF('warningMsge').show()");
			return;
		}
		if (selectedPayType != null && selectedPayType.getCode() != null
				&& selectedPayType.getCode().equals(LOG_SHEET_ISSUANCE)) {
			validateVehicleNo();
		}
		if (vehicleRegNo != null && !vehicleRegNo.trim().isEmpty())
			vehicleRegNo = vehicleRegNo.trim().toUpperCase();
		registered = false;

		PermitDTO permitDetailDTO = terminalManagementService.getPermitInfoByBusNoPermitNo(null, vehicleRegNo);
		if (permitDetailDTO.getPermitNo() != null) {
			registered = true;

			permitNo = permitDetailDTO.getPermitNo();
			routeOrigin = permitDetailDTO.getOrigin();
			routeOriginCode = permitDetailDTO.getRouteNo();
			routeDestination = permitDetailDTO.getDestination();
			routeVia = permitDetailDTO.getVia();
			owner = permitDetailDTO.getVehicleOwner();
			gender = permitDetailDTO.getOwnerGender();
			contactNo = permitDetailDTO.getMobileNo();
			if (contactNo == null || contactNo.isEmpty())
				contactNo = permitDetailDTO.getTelephoneNo();

			expireDateN = permitDetailDTO.getNewExpirDate();
			withoutNTCPermit = true;
			routeNo = permitDetailDTO.getRouteNo();
			vehicleServiceType = permitDetailDTO.getServiceType();
			tsOwner = owner;
			tsGender = gender;
			tsContactNo = contactNo;
			if (selectedPayType.getCode().equals(SPECIAL_HIRE_PERMIT_ISSUANCE)) {

				tsDestination = routeDestination;
				tsRouteNo = routeNo;
			}
		}

		else {
			vehicleServiceType = null;

			if (selectedPayType.getCode().equals(TEMPORARY_PERMIT_ISSUANCE)
					|| selectedPayType.getCode().equals(SPECIAL_HIRE_PERMIT_ISSUANCE)) {
				if (vehicleRegNo != null && !vehicleRegNo.trim().isEmpty()) {
					withoutNTCPermit = false;
					errorMsg = "Please Enter Permanent Permit Details to Proceed.!";
					RequestContext.getCurrentInstance().update("frmwarningMsge");
					RequestContext.getCurrentInstance().execute("PF('warningMsge').show()");
				}

			}
			clearfieldsNew();
		}

		if (selectedPayType != null && selectedPayType.getCode() != null
				&& selectedPayType.getCode().equals(LOG_SHEET_ISSUANCE)) {

			basicAmount = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, vehicleServiceType)[0];
			amountPaid = basicAmount + penalty;
			totalCollection = terminalManagementService.getTotalCollectionAmt() + amountPaid;
			calculatePenalty();
		}

	}

	private void validateVehicleNo() {
		if (vehicleRegNo == null || vehicleRegNo.trim().isEmpty()
				|| !terminalManagementService.validateVehicleOrPermitNo("VEHICLE", vehicleRegNo.trim().toUpperCase())) {
			errorMsg = "Invalid Vehicle Registration Number!";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		}
		vehicleRegNo = vehicleRegNo.trim().toUpperCase();
	}

	private void validatePermitNo() {
		if (selectedPayTypeCode == null || selectedPayTypeCode.trim().equals(TEMPORARY_PERMIT_ISSUANCE)
				|| selectedPayTypeCode.trim().equals(SPECIAL_HIRE_PERMIT_ISSUANCE))
			return;

		if (permitNo == null || permitNo.trim().isEmpty()
				|| !terminalManagementService.validateVehicleOrPermitNo("PERMIT", permitNo.trim().toUpperCase())) {
			errorMsg = "Invalid Permit Number!";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		}
		permitNo = permitNo.trim().toUpperCase();
	}

	public void getRouteData() {
		for (RouteCreationDTO r : nullSafe(routeList)) {

			if (tsOriginCode.equals(r.getRouteNo())) {
				tsRouteNo = r.getRouteNo();
				tsVia = r.getRouteVia();
				tsDestination = r.getEndAt();
			}
		}
	}

	public void calculatePenalty() {
		if (dueDate == null || paidDate == null || selectedPayTypeCode == null || selectedPayTypeCode.isEmpty()) {
			penalty = 0;
			if (basicAmount == 0)
				basicAmount = 0;
			amountPaid = basicAmount + penalty;
			totalCollection = terminalManagementService.getTotalCollectionAmt() + amountPaid;
			return;
		}

		LocalDate paidDateLocal = convertToLocalDateViaInstant(paidDate);
		LocalDate dueDateLocal = convertToLocalDateViaInstant(dueDate);
		long dateDiff = ChronoUnit.DAYS.between(dueDateLocal, paidDateLocal);

		try {

			if (dateDiff >= Integer.valueOf(PropertyReader.getPropertyValue("terminal.payment.penalty.min.days"))) {
				if (selectedPayTypeCode == null || selectedPayTypeCode.trim().equals(TEMPORARY_PERMIT_ISSUANCE)
						|| selectedPayTypeCode.trim().equals(SPECIAL_HIRE_PERMIT_ISSUANCE)) {
					penalty = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, null)[1];
				} else {

					penalty = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, vehicleServiceType)[1];

				}
				if (userChangePenaltyVal != 0 && userChangePenaltyVal != penalty) {
					penalty = userChangePenaltyVal;
				} /**
					 * this logic added by tharushi.e due to user need to change penalty as he want
					 * in log sheet
					 **/
				amountPaid = basicAmount + penalty;
				totalCollection = terminalManagementService.getTotalCollectionAmt() + amountPaid;
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

	}

	public void onDueDateChange(ValueChangeEvent e) {

		if (e.getNewValue() != null) {
			penaltyDisable = false;
			try {
				dueDate = oldDf.parse(e.getNewValue().toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		} else
			dueDate = null;

		calculatePenalty();
		if (!dueDate.before(paidDate)) {
			penalty = (long) 0;
			amountPaid = basicAmount - penalty;
			totalCollection = terminalManagementService.getTotalCollectionAmt() + amountPaid;
		}

	}

	public void onPaidDateChange(ValueChangeEvent e) {
		penaltyDisable = false;
		if (e.getNewValue() != null) {
			try {
				paidDate = oldDf.parse(e.getNewValue().toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		} else
			paidDate = null;

		calculatePenalty();
		if (!dueDate.before(paidDate)) {
			penalty = (long) 0;
			amountPaid = basicAmount - penalty;
			totalCollection = terminalManagementService.getTotalCollectionAmt() + amountPaid;
		}

	}

	public void onIssuedMonthChange(ValueChangeEvent e) {
		if (e.getNewValue() != null)
			issuedMonth = e.getNewValue().toString();
		else
			issuedMonth = null;
	}

	public void onPenaltyChange(ValueChangeEvent e) throws NumberFormatException, ApplicationException {
		/**
		 * check the paid date and paid due date if it has diffrence then user can add
		 * penalty otherwise cant 2022/09/09
		 **/
		LocalDate paidDateLocal = convertToLocalDateViaInstant(paidDate);
		LocalDate dueDateLocal = convertToLocalDateViaInstant(dueDate);
		long dateDiff = ChronoUnit.DAYS.between(dueDateLocal, paidDateLocal);

		if (dateDiff >= Integer.valueOf(PropertyReader.getPropertyValue("terminal.payment.penalty.min.days"))) {

			if (e.getNewValue() != null && !e.getNewValue().toString().trim().isEmpty()) {
				penalty = (double) e.getNewValue();
				userChangePenaltyVal = (double) e.getNewValue();
				amountPaid = basicAmount + penalty;
				totalCollection = terminalManagementService.getTotalCollectionAmt() + amountPaid;
			}
		} else {

			penalty = 0;
			penaltyDisable = true;
			errorMsg = "Can not add penalty. Please check the paid due date!";
			RequestContext.getCurrentInstance().update("frmwarningMsge");
			RequestContext.getCurrentInstance().execute("PF('warningMsge').show()");

			return;
		}
	}

	public void onBasicAmountChange(ValueChangeEvent e) {
		if (e.getNewValue() != null && !e.getNewValue().toString().trim().isEmpty()) {
			basicAmount = (double) e.getNewValue();
			amountPaid = basicAmount + penalty;
			totalCollection = terminalManagementService.getTotalCollectionAmt() + amountPaid;
		}
	}

	public void submitForm() {
		String modifiedBy = sessionBackingBean.loginUser;

		if (selectedPayType == null || selectedPayType.getCode() == null) {
			errorMsg = "Please select the Payment Type.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}

		/**edited by gayathra.m
		 * removed "gender == null || gender.trim().toString().isEmpty()"
		 * */
		if (vehicleRegNo == null || vehicleRegNo.trim().isEmpty() || permitNo == null || permitNo.trim().isEmpty()
				|| owner == null || owner.trim().toString().isEmpty() || routeNo == null || routeNo.trim().isEmpty()
				|| routeOrigin == null || routeOrigin.trim().isEmpty() || routeDestination == null
				|| routeDestination.trim().isEmpty() || expireDateN == null || vehicleServiceType == null
				|| vehicleServiceType.trim().isEmpty() || payReciptRef == null || payReciptRef.trim().isEmpty()) {
			errorMsg = "Mandatory fields cannot be empty.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}

		if (terminalManagementService.referenceNoExists(payReciptRef)) {
			errorMsg = "Payment is already done fo this reference No.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}

		if (selectedPayType.getCode().trim().equals(LOG_SHEET_ISSUANCE)) {
			tsPermitNo = permitNo;
			if (issuedDate == null || issuedMonth == null || issuedMonth.trim().toString().isEmpty()) {
				errorMsg = "Mandatory fields cannot be empty.";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				return;
			}
			if (!terminalManagementService.validateVehicleOrPermitNo("VEHICLE", vehicleRegNo.trim())) {
				errorMsg = "Invalid Vehicle Registration Number!";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				return;
			}
			if (!terminalManagementService.validateVehicleOrPermitNo("PERMIT", permitNo.trim())) {
				errorMsg = "Invalid Permit Number!";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				return;
			}

		} else if (selectedPayType.getCode().trim().equals(TEMPORARY_PERMIT_ISSUANCE)) {

			for (RouteCreationDTO r : nullSafe(routeList)) {
				if (r.getRouteNo().equals(getTsOriginCode())) {

					tsRouteNo = (r.getRouteNo());
					tsOriginDesc = (r.getStartFrom());
					tsDestination = r.getEndAt();
					tsVia = r.getRouteVia();
					break;
				}
			}
			if (validToDate == null || validFromDate == null || tsOwner == null || tsOwner.trim().toString().isEmpty()
					|| tsGender == null || tsGender.trim().toString().isEmpty() || tsOriginCode == null
					|| tsOriginCode.trim().toString().isEmpty() || tsVehicleServiceType == null
					|| tsVehicleServiceType.trim().toString().isEmpty()) {
				errorMsg = "Mandatory fields cannot be empty.";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				return;
			}

		} else if (selectedPayType.getCode().trim().equals(SPECIAL_HIRE_PERMIT_ISSUANCE)) {
			/*
			 * if ( validToDate==null || validFromDate == null || tsOwner == null ||
			 * tsOwner.trim().toString().isEmpty() || tsRouteNo == null ||
			 * tsRouteNo.trim().toString().isEmpty() || tsGender == null ||
			 * tsGender.trim().toString().isEmpty() || tsOriginCode == null ||
			 * tsOriginCode.trim().toString().isEmpty() || tsDestination == null ||
			 * tsDestination.trim().toString().isEmpty() || tsVia == null ||
			 * tsVia.trim().toString().isEmpty() || tsVehicleServiceType == null ||
			 * tsVehicleServiceType.trim().toString().isEmpty() || hirePermitRemark == null
			 * || hirePermitRemark.trim().isEmpty()) {
			 */

			if (validToDate == null || validFromDate == null || tsOwner == null || tsOwner.trim().toString().isEmpty()
					|| tsGender == null || tsGender.trim().toString().isEmpty() || tsVia == null
					|| tsVia.trim().toString().isEmpty() || tsVehicleServiceType == null
					|| tsVehicleServiceType.trim().toString().isEmpty() || hirePermitRemark == null
					|| hirePermitRemark.trim().isEmpty()) {
				errorMsg = "Mandatory fields cannot be empty.";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				return;
			} else {
				tsOriginDesc = tsOriginCode;
			}

		}

		boolean returnVal = terminalManagementService.saveTerminalPaymentInfo(selectedPayTypeCode, vehicleServiceType,
				vehicleRegNo, issuedDate, issuedMonth, payMode, payReciptRef, noOfTurn, validFromDate, validToDate,
				basicAmount, penalty, amountPaid, totalCollection, permitNo, routeOrigin, routeDestination, routeVia,
				owner, gender, contactNo, modifiedBy, dueDate, paidDate, expireDateN, hirePermitRemark, tsOriginDesc,
				tsDestination, tsVia, tsOwner, tsGender, tsContactNo, tsVehicleServiceType, tsRouteNo, routeNo,
				tsPermitNo, sessionBackingBean.getStationName());

		if (returnVal) {
			long paymentInfoSequence = terminalManagementService.getPaymentSummarySequence(selectedPayTypeCode);

			if (paymentInfoSequence > 0)
				terminalManagementService.updatePaymentSummary(0, selectedPayTypeCode, totalCollection, receiptCount,
						receiptGenerated, false, modifiedBy);
			else
				terminalManagementService.updatePaymentSummary(paymentInfoSequence, selectedPayTypeCode,
						totalCollection, receiptCount, receiptGenerated, true, modifiedBy);

//			if (selectedPayTypeCode.equals("002")) {
//				//terminalManagementService.updatePermitNoGenerateSeq();
//			} else if (selectedPayTypeCode.equals("003")) {
//				//terminalManagementService.updateSpecialPermitNoGenerateSeq();
//			}

			terminalManagementService.beanLinkMethod("Submit Accept Payment", modifiedBy, "Terminal Payments",
					vehicleRegNo, permitNo, stationName);
			sucessMsg = "Saved Succesfully!";
			RequestContext.getCurrentInstance().update("frmSuccess");
			RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
			enableReceipt = true;
			enablePermit = true;
		}
	}

	public void clearFields() {
		selectedPayTypeCode = null;
		selectedPayType = new PaymentTypeDTO();
		vehicleRegNo = null;
		dueDate = currentDate;
		paidDate = currentDate;
		issuedDate = null;
		issuedMonth = null;
		validFromDate = null;
		validToDate = null;
		noOfTurn = 0;
		payMode = "BN";
		payReciptRef = null;
		basicAmount = 0;
		penalty = 0;
		amountPaid = 0;
		totalCollection = 0;
		permitNo = null;
		routeOrigin = null;
		routeOriginCode = null;
		routeDestination = null;
		routeDestinationCode = null;
		routeVia = null;
		owner = null;
		gender = null;
		contactNo = null;
		vehicleServiceType = null;
		receiptGenerated = false;
		registered = false;
		enableReceipt = false;
		enablePermit = true;
		expireDate = null;
		tsPermitNo = null;
		withoutNTCPermit = true;
		routeNo = null;
		tsPermitNo = null;
		tsRouteNo = null;
		tsOriginCode = null;
		tsOriginDesc = null;
		tsDestination = null;
		tsVia = null;
		tsOwner = null;
		tsGender = null;
		tsContactNo = null;
		tsVehicleServiceType = null;
		hirePermitRemark = null;
		expireDateN = null;
		selectedTerminalLocation = null;
		userChangePenaltyVal = 0;
		penaltyDisable = false;
	}

	public void clearfieldsNew() {
		dueDate = currentDate;
		paidDate = currentDate;
		issuedDate = null;
		issuedMonth = null;
		validFromDate = null;
		validToDate = null;
		noOfTurn = 0;
		payMode = "BN";
		payReciptRef = null;
		permitNo = null;
		routeOrigin = null;
		routeOriginCode = null;
		routeDestination = null;
		routeDestinationCode = null;
		routeVia = null;
		owner = null;
		gender = null;
		contactNo = null;
		vehicleServiceType = null;
		receiptGenerated = false;
		registered = false;
		enableReceipt = false;
		expireDate = null;
		enablePermit = true;
		routeNo = null;
		penaltyDisable = false;
	}

	public void generateReceipt(ActionEvent ae) throws JRException {

		String receiptVal;
		String payTypeCode;
		String modifiedBy = sessionBackingBean.loginUser;
		if (selectedPayType == null || selectedPayType.getCode() == null) {

			errorMsg = "Please select a payment type!";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		} else {

			payTypeCode = selectedPayType.getCode();

			if (selectedPayType.getCode().equals(LOG_SHEET_ISSUANCE)) {

				if (vehicleRegNo == null || vehicleRegNo.trim().isEmpty() || permitNo == null
						|| permitNo.trim().isEmpty() || owner == null || owner.trim().toString().isEmpty()
						|| gender == null || gender.trim().toString().isEmpty() || payReciptRef == null
						|| payReciptRef.trim().isEmpty()) {
					errorMsg = "Mandatory fields cannot be empty.";
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
					return;
				}
				if (issuedDate == null || issuedMonth == null || issuedMonth.trim().toString().isEmpty()) {
					errorMsg = "Mandatory fields cannot be empty.";
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
					return;
				}
				if (!terminalManagementService.validateVehicleOrPermitNo("VEHICLE", vehicleRegNo.trim())) {
					errorMsg = "Invalid Vehicle Registration Number!";
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
					return;
				}
				if (!terminalManagementService.validateVehicleOrPermitNo("PERMIT", permitNo.trim())) {
					errorMsg = "Invalid Permit Number!";
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
					return;
				}

				receiptVal = terminalManagementService.generateVoucherNo();

				receiptGenerated = true;
				terminalManagementService.updateVoucherNumber(receiptVal, vehicleRegNo, payReciptRef, modifiedBy,
						receiptGenerated);
				generateRecieptReport(receiptVal, payTypeCode);
				
				terminalManagementService.beanLinkMethod("Generate Accept Payments Receipt", modifiedBy, "Terminal Payments",
						vehicleRegNo, permitNo, selectedPayTypeCode);

			} else if (selectedPayType.getCode().trim().equals(TEMPORARY_PERMIT_ISSUANCE)) {

//				for (RouteCreationDTO r : nullSafe(routeList)) {
//					if (r.getRouteNo().equals(getRouteOriginCode())) {
//
//						tsRouteNo = r.getRouteNo();
//						tsOriginDesc = r.getStartFrom();
//						tsDestination = r.getEndAt();
//						tsVia = r.getRouteVia();
//
//						break;
//					}
//				}
				receiptVal = terminalManagementService.generateVoucherNo();

				receiptGenerated = true;
				terminalManagementService.updateVoucherNumber(receiptVal, vehicleRegNo, payReciptRef, modifiedBy,
						receiptGenerated);
				generateRecieptReport(receiptVal, payTypeCode);
				enablePermit = false;
				
				terminalManagementService.beanLinkMethod("Generate Accept Payments Receipt", modifiedBy, "Terminal Payments",
						vehicleRegNo, permitNo, selectedPayTypeCode);

			} else if (selectedPayType.getCode().trim().equals(SPECIAL_HIRE_PERMIT_ISSUANCE)) {

				receiptVal = terminalManagementService.generateVoucherNo();

				receiptGenerated = true;
				terminalManagementService.updateVoucherNumber(receiptVal, vehicleRegNo, payReciptRef, modifiedBy,
						receiptGenerated);
				generateRecieptReport(receiptVal, payTypeCode);
				enablePermit = false;
				
				terminalManagementService.beanLinkMethod("Generate Accept Payments Receipt", modifiedBy, "Terminal Payments",
						vehicleRegNo, permitNo, selectedPayTypeCode);

			}

			receiptGenerated = true;
			enableReceipt = false;

		}

	}

	public StreamedContent generateRecieptReport(String receiptVal, String payTypeCode) throws JRException {
		files = null;
		String sourceFileName = null;
		String modifiedBy = sessionBackingBean.loginUser;
		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//TerminalPaymentReceipt.jrxml";

			String logopath = "//lk//informatics//ntc//view//reports//";
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Payment_type_code", payTypeCode);

			parameters.put("P_Reciept_Ref", payReciptRef);
			parameters.put("P_user", modifiedBy);
			parameters.put("P_receipt_no", receiptVal);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "TerminalPaymentReceipt.pdf");

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

	public StreamedContent genratePermit() throws JRException {
		files = null;
		String sourceFileName = null;
		String modifiedBy = sessionBackingBean.loginUser;
		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			conn = ConnectionManager.getConnection();
			if (selectedPayTypeCode.equals("002")) {
				sourceFileName = "..//reports//TemporyPermitPrePrint.jrxml";

			}
			if (selectedPayTypeCode.equals("003")) {
				sourceFileName = "..//reports//SpecialHirePermitPrePrint.jrxml";
			}

			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Receipt_No", payReciptRef);
			if (selectedPayTypeCode.equals("003")) {
				String chargeAmount = terminalManagementService.getChargeAmmount(payReciptRef);
				String amountInWord = ConvertNumberToWord.getDecimalValue(chargeAmount);

				parameters.put("P_ammount_inLetter", amountInWord.toUpperCase());

			}
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "TemporarayPermit" + vehicleRegNo + ".pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");
			
			terminalManagementService.beanLinkMethod("Print Permit Report", modifiedBy, "Terminal Payments",
					vehicleRegNo, permitNo, selectedPayTypeCode);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}
		enablePermit = true;
		return files;

	}

	/************** Adjust Service Management Fees ********************/
	public void changePaymentTypePaymentModule(ValueChangeEvent e) {
		oldServiceAmountList = new ArrayList<ServiceAmountDTO>();
		serviceAmountList = new ArrayList<ServiceAmountDTO>();
		basicAmount = (long) 0;
		penalty = (long) 0;
		basicAmountOld = (long) 0;
		penalty = (long) 0;

		if (e.getNewValue() != null && !e.getNewValue().toString().isEmpty()) {
			selectedPayTypeCode = e.getNewValue().toString();

			if (e.getNewValue().toString().equals(LOG_SHEET_ISSUANCE)) {
				List<CommonDTO> serviceTypeList = terminalManagementService.getServiceTypeToDropdown();
				for (CommonDTO type : nullSafe(serviceTypeList)) {
					String code = type.getCode();
					String desc = type.getDescription();
					double basic = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, type.getCode())[0];
					double pen = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, type.getCode())[1];

					ServiceAmountDTO dto = new ServiceAmountDTO();
					dto.setServiceTypeCode(code);
					dto.setServiceType(desc);
					dto.setBasicAmount(basic);
					dto.setPenalty(pen);
					oldServiceAmountList.add(dto);

					dto = new ServiceAmountDTO();
					dto.setServiceTypeCode(code);
					dto.setServiceType(desc);
					dto.setBasicAmount(basic);
					dto.setPenalty(pen);
					serviceAmountList.add(dto);
				}

			} else {
				basicAmount = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, null)[0];
				penalty = terminalManagementService.getIssuanceAmt(selectedPayTypeCode, null)[1];
				basicAmountOld = basicAmount;
				penaltyOld = penalty;
			}
		}
	}

	public void updateServiceAmounts() {
		String modifiedBy = sessionBackingBean.loginUser;

		if (selectedPayTypeCode.equals(LOG_SHEET_ISSUANCE)) {

			for (ServiceAmountDTO serv : serviceAmountList) {
				for (ServiceAmountDTO servOld : oldServiceAmountList) {
					if (serv.getServiceTypeCode().equals(servOld.getServiceTypeCode())) {

						if (serv.getBasicAmount() != servOld.getBasicAmount()
								|| serv.getPenalty() != servOld.getPenalty()) {
							if (!terminalManagementService.updateServiceAmounts(selectedPayTypeCode,
									serv.getServiceTypeCode(), servOld.getBasicAmount(), servOld.getPenalty(),
									serv.getBasicAmount(), serv.getPenalty(), modifiedBy)) {
								errorMsg = "Error!";
								RequestContext.getCurrentInstance().update("frmError");
								RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
							}
						}

					}
				}
			}

		} else {
			if (basicAmount != basicAmountOld || penalty != penaltyOld) {
				if (!terminalManagementService.updateServiceAmounts(selectedPayTypeCode, null, basicAmountOld,
						penaltyOld, basicAmount, penalty, modifiedBy)) {
					errorMsg = "Error!";
					RequestContext.getCurrentInstance().update("frmError");
					RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				}
			}
		}

		sucessMsg = "Saved Succesfully!";
		RequestContext.getCurrentInstance().update("frmSuccess");
		RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
		selectedPayTypeCode = null;
		basicAmount = 0;
		penalty = 0;
		basicAmountOld = 0;
		penaltyOld = 0;
		serviceAmountList = new ArrayList<ServiceAmountDTO>();
		oldServiceAmountList = new ArrayList<ServiceAmountDTO>();

	}

	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	//////////////// getter & setters /////////////////////

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

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSelectedPayTypeCode() {
		return selectedPayTypeCode;
	}

	public void setSelectedPayTypeCode(String selectedPayTypeCode) {
		this.selectedPayTypeCode = selectedPayTypeCode;
	}

	public List<PaymentTypeDTO> getPaymentTypeList() {
		return paymentTypeList;
	}

	public void setPaymentTypeList(List<PaymentTypeDTO> paymentTypeList) {
		this.paymentTypeList = paymentTypeList;
	}

	public String getVehicleRegNo() {
		return vehicleRegNo;
	}

	public void setVehicleRegNo(String vehicleRegNo) {
		this.vehicleRegNo = vehicleRegNo;
	}

	public String getIssuedMonth() {
		return issuedMonth;
	}

	public void setIssuedMonth(String issuedMonth) {
		this.issuedMonth = issuedMonth;
	}

	public String getPayReciptRef() {
		return payReciptRef;
	}

	public void setPayReciptRef(String payReciptRef) {
		this.payReciptRef = payReciptRef;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getOwner() {
		return owner;
	}

	public String getRouteOrigin() {
		return routeOrigin;
	}

	public void setRouteOrigin(String routeOrigin) {
		this.routeOrigin = routeOrigin;
	}

	public String getRouteDestination() {
		return routeDestination;
	}

	public void setRouteDestination(String routeDestination) {
		this.routeDestination = routeDestination;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public TerminalManagementService getTerminalManagementService() {
		return terminalManagementService;
	}

	public void setTerminalManagementService(TerminalManagementService terminalManagementService) {
		this.terminalManagementService = terminalManagementService;
	}

	public PaymentTypeDTO getSelectedPayType() {
		return selectedPayType;
	}

	public void setSelectedPayType(PaymentTypeDTO selectedPayType) {
		this.selectedPayType = selectedPayType;
	}

	public String getRouteOriginCode() {
		return routeOriginCode;
	}

	public void setRouteOriginCode(String routeOriginCode) {
		this.routeOriginCode = routeOriginCode;
	}

	public String getRouteDestinationCode() {
		return routeDestinationCode;
	}

	public void setRouteDestinationCode(String routeDestinationCode) {
		this.routeDestinationCode = routeDestinationCode;
	}

	public List<RouteCreationDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteCreationDTO> routeList) {
		this.routeList = routeList;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Date validFromDate) {
		this.validFromDate = validFromDate;
	}

	public Date getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(Date validToDate) {
		this.validToDate = validToDate;
	}

	public long getNoOfTurn() {
		return noOfTurn;
	}

	public void setNoOfTurn(long noOfTurn) {
		this.noOfTurn = noOfTurn;
	}

	public boolean isReceiptGenerated() {
		return receiptGenerated;
	}

	public void setReceiptGenerated(boolean receiptGenerated) {
		this.receiptGenerated = receiptGenerated;
	}

	public long getReceiptCount() {
		return receiptCount;
	}

	public void setReceiptCount(long receiptCount) {
		this.receiptCount = receiptCount;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public List<CommonDTO> getGenderList() {
		return genderList;
	}

	public void setGenderList(List<CommonDTO> genderList) {
		this.genderList = genderList;
	}

	public List<ServiceAmountDTO> getServiceAmountList() {
		return serviceAmountList;
	}

	public void setServiceAmountList(List<ServiceAmountDTO> serviceAmountList) {
		this.serviceAmountList = serviceAmountList;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isEnableReceipt() {
		return enableReceipt;
	}

	public void setEnableReceipt(boolean enableReceipt) {
		this.enableReceipt = enableReceipt;
	}

	public String getVehicleServiceType() {
		return vehicleServiceType;
	}

	public void setVehicleServiceType(String vehicleServiceType) {
		this.vehicleServiceType = vehicleServiceType;
	}

	public List<CommonDTO> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<CommonDTO> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public String getRouteVia() {
		return routeVia;
	}

	public void setRouteVia(String routeVia) {
		this.routeVia = routeVia;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public List<CommonDTO> getPaymentModes() {
		return paymentModes;
	}

	public void setPaymentModes(List<CommonDTO> paymentModes) {
		this.paymentModes = paymentModes;
	}

	public boolean isEnablePermit() {
		return enablePermit;
	}

	public void setEnablePermit(boolean enablePermit) {
		this.enablePermit = enablePermit;
	}

	public String getTsPermitNo() {
		return tsPermitNo;
	}

	public void setTsPermitNo(String tsPermitNo) {
		this.tsPermitNo = tsPermitNo;
	}

	public boolean isWithoutNTCPermit() {
		return withoutNTCPermit;
	}

	public void setWithoutNTCPermit(boolean withoutNTCPermit) {
		this.withoutNTCPermit = withoutNTCPermit;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getTsOriginCode() {
		return tsOriginCode;
	}

	public void setTsOriginCode(String tsOriginCode) {
		this.tsOriginCode = tsOriginCode;
	}

	public String getTsDestination() {
		return tsDestination;
	}

	public void setTsDestination(String tsDestination) {
		this.tsDestination = tsDestination;
	}

	public String getTsVia() {
		return tsVia;
	}

	public void setTsVia(String tsVia) {
		this.tsVia = tsVia;
	}

	public String getTsOwner() {
		return tsOwner;
	}

	public void setTsOwner(String tsOwner) {
		this.tsOwner = tsOwner;
	}

	public String getTsGender() {
		return tsGender;
	}

	public void setTsGender(String tsGender) {
		this.tsGender = tsGender;
	}

	public String getTsContactNo() {
		return tsContactNo;
	}

	public void setTsContactNo(String tsContactNo) {
		this.tsContactNo = tsContactNo;
	}

	public String getTsVehicleServiceType() {
		return tsVehicleServiceType;
	}

	public void setTsVehicleServiceType(String tsVehicleServiceType) {
		this.tsVehicleServiceType = tsVehicleServiceType;
	}

	public String getHirePermitRemark() {
		return hirePermitRemark;
	}

	public void setHirePermitRemark(String hirePermitRemark) {
		this.hirePermitRemark = hirePermitRemark;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getTsRouteNo() {
		return tsRouteNo;
	}

	public void setTsRouteNo(String tsRouteNo) {
		this.tsRouteNo = tsRouteNo;
	}

	public String getTsOriginDesc() {
		return tsOriginDesc;
	}

	public void setTsOriginDesc(String tsOriginDesc) {
		this.tsOriginDesc = tsOriginDesc;
	}

	public Date getExpireDateN() {
		return expireDateN;
	}

	public void setExpireDateN(Date expireDateN) {
		this.expireDateN = expireDateN;
	}

	public String getSelectedTerminalLocation() {
		return selectedTerminalLocation;
	}

	public void setSelectedTerminalLocation(String selectedTerminalLocation) {
		this.selectedTerminalLocation = selectedTerminalLocation;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public boolean isLocalcheckcounter() {
		return localcheckcounter;
	}

	public void setLocalcheckcounter(boolean localcheckcounter) {
		this.localcheckcounter = localcheckcounter;
	}

	public String getStationDes() {
		return stationDes;
	}

	public void setStationDes(String stationDes) {
		this.stationDes = stationDes;
	}

	public boolean isShowStationPopup() {
		return showStationPopup;
	}

	public void setShowStationPopup(boolean showStationPopup) {
		this.showStationPopup = showStationPopup;
	}

	public double getBasicAmount() {
		return basicAmount;
	}

	public void setBasicAmount(double basicAmount) {
		this.basicAmount = basicAmount;
	}

	public double getPenalty() {
		return penalty;
	}

	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public double getTotalCollection() {
		return totalCollection;
	}

	public void setTotalCollection(double totalCollection) {
		this.totalCollection = totalCollection;
	}

	public double getBasicAmountOld() {
		return basicAmountOld;
	}

	public void setBasicAmountOld(double basicAmountOld) {
		this.basicAmountOld = basicAmountOld;
	}

	public double getPenaltyOld() {
		return penaltyOld;
	}

	public void setPenaltyOld(double penaltyOld) {
		this.penaltyOld = penaltyOld;
	}

	public double getUserChangePenaltyVal() {
		return userChangePenaltyVal;
	}

	public void setUserChangePenaltyVal(double userChangePenaltyVal) {
		this.userChangePenaltyVal = userChangePenaltyVal;
	}

	public boolean isPenaltyDisable() {
		return penaltyDisable;
	}

	public void setPenaltyDisable(boolean penaltyDisable) {
		this.penaltyDisable = penaltyDisable;
	}

}
