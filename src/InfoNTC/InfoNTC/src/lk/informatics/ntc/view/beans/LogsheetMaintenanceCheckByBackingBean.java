package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.LogsheetMaintenanceService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "logsheetMaintenanceCheckByBackingBean")
@ViewScoped
public class LogsheetMaintenanceCheckByBackingBean implements Serializable {

	private static final long serialVersionUID = -5898964178243229652L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private LogSheetMaintenanceDTO logDTO;
	private List<String> refNoList = new ArrayList<String>();
	private List<String> serviceNoList = new ArrayList<String>();
	private List<LogSheetMaintenanceDTO> serviceTypeList = new ArrayList<LogSheetMaintenanceDTO>();
	private List<LogSheetMaintenanceDTO> datatableList = new ArrayList<LogSheetMaintenanceDTO>();
	private List<SisuSeriyaDTO> operatorDepoNameList = new ArrayList<SisuSeriyaDTO>(0);
	private LogsheetMaintenanceService logsheetMaintenanceService;
	private SisuSariyaService sisuSariyaService;
	private LogSheetMaintenanceDTO selectedRow = new LogSheetMaintenanceDTO();
	String date = LocalDate.now().toString();
	String status = "";
	String errorMsg;
	String logRefNo = "";
	boolean check = false;
	boolean searchCheck = false;
	String user = "";
	String sucessMsg = "";
	boolean disable = true;
	boolean approvalBoolean = true;
	String warningMsg = "";

	private CommonService commonService;
	private boolean approveMode, createMode, viewMode;

	private boolean showSchoolCalPanel = false;
	private boolean showGamiSeriyaCalPanel = false;
	private boolean showNisiCalPanel = false;

	@PostConstruct
	public void init() {

		logDTO = new LogSheetMaintenanceDTO();
		logsheetMaintenanceService = (LogsheetMaintenanceService) SpringApplicationContex
				.getBean("logSheetMaintenanceService");
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		refNoList = logsheetMaintenanceService.serviceRefNoDropDown();
		serviceTypeList = logsheetMaintenanceService.serviceTypeDropDown();

		user = sessionBackingBean.loginUser;
		approvalBoolean = sessionBackingBean.isSesssionapprovalBoolean();
		disable = sessionBackingBean.isSessiondisable();
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		approveMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN111", "A");
		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN111", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN111", "V");

	}

	public void onChangeServiceType() {
		if (!logDTO.getServiceType().equals("") && logDTO.getServiceType() != null) {
			if (logDTO.getServiceType().equals("S01")) {
				serviceNoList = new ArrayList<String>();
				serviceNoList = logsheetMaintenanceService.serviceNoSisuSeriyaDropDown(logDTO.getServiceType());
			} else if (logDTO.getServiceType().equals("S02")) {
				serviceNoList = new ArrayList<String>();
				serviceNoList = logsheetMaintenanceService.serviceNoGamiSeriyaDropDown(logDTO.getServiceType());
			} else if (logDTO.getServiceType().equals("S03")) {
				serviceNoList = new ArrayList<String>();
				serviceNoList = logsheetMaintenanceService.serviceNoNisiSeriyaDropDown(logDTO.getServiceType());
			}

			operatorDepoNameList = sisuSariyaService.getOperatorDepoNameListForIssueLogSheets(logDTO.getServiceType());
		}
	}
	
	public void onChangeOperatorDepoName() {
		if (!logDTO.getServiceType().equals("") && logDTO.getServiceType() != null
				&& !logDTO.getNameOfOperator().equals("") && logDTO.getNameOfOperator() != null) {
			if (logDTO.getServiceType().equals("S01")) {
				serviceNoList = new ArrayList<String>();
				serviceNoList = logsheetMaintenanceService.serviceNoSisuSeriyaDropDown(logDTO);
			} else if (logDTO.getServiceType().equals("S02")) {
				serviceNoList = new ArrayList<String>();
				serviceNoList = logsheetMaintenanceService.serviceNoGamiSeriyaDropDown(logDTO);
			} else if (logDTO.getServiceType().equals("S03")) {
				serviceNoList = new ArrayList<String>();
				serviceNoList = logsheetMaintenanceService.serviceNoNisiSeriyaDropDown(logDTO);
			}
		}
	}

	public void approve() {
		status = "Y";
		if (logRefNo.equals("")) {
			setErrorMsg("Please select a record from the table.");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {
			setApprovalBoolean(true);
			boolean success = logsheetMaintenanceService.approvalstatusupdate(status, user, date, logRefNo);
			
			if(success) {
				setSucessMsg("Checked successfully.");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			}else {
				setErrorMsg("Unsuccessful");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
						
			datatableList = logsheetMaintenanceService.sheetTableCheckBy(logDTO);
			RequestContext.getCurrentInstance().update("e");
			sessionBackingBean.setSessLogDTO(logDTO);
			sessionBackingBean.setSessiondatatableList(datatableList);
			sessionBackingBean.setApprovebutton(true);

		}
	}

	public void reject() {
		status = "N";
		if (logRefNo.equals("")) {
			setErrorMsg("Please select a record from the table.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

		else {
			boolean success = logsheetMaintenanceService.approvalstatusupdate(status, user, date, logRefNo);
			
			if(success) {
				setSucessMsg("Rejected successfully.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				setApprovalBoolean(true);
			}else {
				setErrorMsg("Unsuccessful");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
			
			datatableList = logsheetMaintenanceService.sheetTableCheckBy(logDTO);

			RequestContext.getCurrentInstance().update("e");
			sessionBackingBean.setSessLogDTO(logDTO);
			sessionBackingBean.setSessiondatatableList(datatableList);
			sessionBackingBean.setApprovebutton(true);
		}
	}

	public void clear() {
		logDTO = new LogSheetMaintenanceDTO();
		datatableList = null;
		logRefNo = "";
		check = false;
		disable = true;
		searchCheck = false;
		approvalBoolean = true;
		setShowSchoolCalPanel(false);
		sessionBackingBean.setSessLogDTO(logDTO);
		sessionBackingBean.setSessiondatatableList(null);
		sessionBackingBean.setSessiondisable(disable);
		sessionBackingBean.setSesssionapprovalBoolean(approvalBoolean);
	}

	public void clearThird() {
		disable = true;
		approvalBoolean = true;
		clearThirdPanelInputs();
	}

	private void clearThirdPanelInputs() {

		logDTO.setNoOfTurns(0);
		logDTO.setLogType(null);
		logDTO.setNoOfTurnsinGPS(0);
		logDTO.setPaymentType(null);
		logDTO.setLogSheetDelay(0);
		logDTO.setRunning(0);
		logDTO.setAveragePasengers(0);
		logDTO.setTotalLength(0);
		logDTO.setSchoolRequiredRunningDate(0);
		logDTO.setSubsidy(0);
		logDTO.setLateTrips(0);
		logDTO.setLateFee(0);
		logDTO.setApprovalBoolean(false);
		logDTO.setPenaltyFee(0);
		logDTO.setArrears(0);
		logDTO.setPayment(0);
		logDTO.setDeductions(0);
		logDTO.setSpecialRemark(null);
		logDTO.setOtherPercentage(0);
		logDTO.setOtherPercentageCalculationVal(0);
		disable = true;
		logDTO.setRequested(0);
		logDTO.setTripsPerDay(0);

		logDTO.setPaymentD(new BigDecimal(0.00));
		logDTO.setTotalLengthD(new BigDecimal(0.00));
		logDTO.setLateFeeD(new BigDecimal(0.00));
		logDTO.setOtherPercentageCalculationValD(new BigDecimal(0.00));
		logDTO.setPenaltyFeeD(new BigDecimal(0.00));
		logDTO.setRunningD(new BigDecimal(0.00));
		logDTO.setSubsidyD(new BigDecimal(0.00));

	}

	public void clearSecond() {
		disable = true;
		approvalBoolean = true;
		selectedRow = new LogSheetMaintenanceDTO();

	}

	public void search() {

		if (logDTO.getServiceNo() == null || logDTO.getServiceNo().isEmpty() || logDTO.getServiceNo().equals("")) {
			errorMsg = "Please select a service no.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		else if (logDTO.getServiceType() == null || logDTO.getServiceType().isEmpty()
				|| logDTO.getServiceType().equals("")) {
			errorMsg = "Please select a service type.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		else if (logDTO.getYear() == null || logDTO.getYear().isEmpty() || logDTO.getYear().equals("")) {
			errorMsg = "Please enter a year!";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {

			if (logDTO.getServiceType().equals("S01")) {
				if (logsheetMaintenanceService.initialSearch(logDTO) == false) {
					errorMsg = "No data for searched record!";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					clear();
				}

				if ((logsheetMaintenanceService.initialSearch(logDTO) == true)) {
					logDTO = logsheetMaintenanceService.search(logDTO);
					logDTO = logsheetMaintenanceService.getRefNo(logDTO);
					searchCheck = true;
					datatableList = logsheetMaintenanceService.sheetTableCheckBy(logDTO);
					RequestContext.getCurrentInstance().update("@form");
					sessionBackingBean.setSessLogDTO(logDTO);
					sessionBackingBean.setSessiondatatableList(datatableList);
				}
				setShowSchoolCalPanel(true);
				setShowGamiSeriyaCalPanel(false);
				setShowNisiCalPanel(false);
			} else if (logDTO.getServiceType().equals("S02")) {

				if (logsheetMaintenanceService.initialGamiSeriyaSearch(logDTO) == false) {
					errorMsg = "No data for searched record!";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					clear();
				}

				if ((logsheetMaintenanceService.initialGamiSeriyaSearch(logDTO) == true)) {
					logDTO = logsheetMaintenanceService.searchGamiSeriya(logDTO);
					logDTO = logsheetMaintenanceService.getRefNoGamiSeriya(logDTO);
					searchCheck = true;
					datatableList = logsheetMaintenanceService.sheetTableCheckBy(logDTO);
					RequestContext.getCurrentInstance().update("@form");
					sessionBackingBean.setSessLogDTO(logDTO);
					sessionBackingBean.setSessiondatatableList(datatableList);
				}

				setShowSchoolCalPanel(false);
				setShowGamiSeriyaCalPanel(true);
				setShowNisiCalPanel(false);
			} else if (logDTO.getServiceType().equals("S03")) {
				if (logsheetMaintenanceService.initialNisiSeriyaSearch(logDTO) == false) {
					errorMsg = "No data for searched record!";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					clear();
				}

				if ((logsheetMaintenanceService.initialNisiSeriyaSearch(logDTO) == true)) {
					logDTO = logsheetMaintenanceService.searchNisiSeriya(logDTO);
					logDTO = logsheetMaintenanceService.getRefNoNisiSeriya(logDTO);
					searchCheck = true;

					datatableList = logsheetMaintenanceService.sheetTableCheckBy(logDTO);
					RequestContext.getCurrentInstance().update("@form");
					sessionBackingBean.setSessLogDTO(logDTO);
					sessionBackingBean.setSessiondatatableList(datatableList);
				}
				setShowSchoolCalPanel(false);
				setShowGamiSeriyaCalPanel(false);
				setShowNisiCalPanel(true);

			}

		}
	}

	public List<String> completeRefNo(String query) {
		query.toUpperCase();
		List<String> allNos = refNoList;
		List<String> filteredPermits = new ArrayList<String>();
		for (int i = 0; i < allNos.size(); i++) {
			if (allNos.get(i).contains(query)) {
				filteredPermits.add(allNos.get(i));
			}
		}

		return filteredPermits;
	}

	public void onRowSelect(SelectEvent event) {

		logRefNo = ((LogSheetMaintenanceDTO) event.getObject()).getLogRefNo();
		logDTO.setLogRefNo(logRefNo);
		boolean checkdata = logsheetMaintenanceService.checkData(logDTO);
		if (checkdata == true) {
			approvalBoolean = false;
			sessionBackingBean.setSesssionapprovalBoolean(approvalBoolean);
			RequestContext.getCurrentInstance().update("xx");
		}

		else {
			approvalBoolean = true;
			sessionBackingBean.setSesssionapprovalBoolean(approvalBoolean);
			RequestContext.getCurrentInstance().update("xx");
		}
	}

	public void presetValues(boolean edit) {

		if (logDTO.getServiceType() != null || !logDTO.getServiceType().isEmpty()
				|| !logDTO.getServiceType().equals("")) {
			if (logDTO.getServiceType().equals("S01")) {
				if(!edit) {
				String subsidy = logsheetMaintenanceService.paymentRate();

				BigDecimal subsidyAmount = new BigDecimal(subsidy);
				logDTO.setSubsidyD(subsidyAmount);
				}
				String length = logsheetMaintenanceService.totalLength(logDTO.getServiceNo());
				BigDecimal totLength = new BigDecimal(length);
				logDTO.setTotalLengthD(totLength);

			} else if (logDTO.getServiceType().equals("S02")) {
				if(!edit) {
				String subsidy = logsheetMaintenanceService.paymentRateGamiSeriya();

				BigDecimal subsidyAmount = new BigDecimal(subsidy);
				logDTO.setSubsidyD(subsidyAmount);
				}
				String length = logsheetMaintenanceService.totalLengthGamiSeriya(logDTO.getServiceNo());
				BigDecimal totLength = new BigDecimal(length);
				logDTO.setTotalLengthD(totLength);

			} else if (logDTO.getServiceType().equals("S03")) {
				if(!edit) {
				String subsidy = logsheetMaintenanceService.paymentRateNisiSeriya();

				BigDecimal subsidyAmount = new BigDecimal(subsidy);
				logDTO.setSubsidyD(subsidyAmount);
				}
				String length = logsheetMaintenanceService.totalLengthNisiSeriya(logDTO.getServiceNo());
				BigDecimal totLength = new BigDecimal(length);
				logDTO.setTotalLengthD(totLength);

			} else {

			}
		} else {

		}
	}

	public void calculation() {
		BigDecimal totalAmount = new BigDecimal(0.00);
		BigDecimal z = new BigDecimal(100.00);
		BigDecimal turns = new BigDecimal(2.00);
		boolean calcpay = false;

		double percentageN = (logDTO.getNoOfTurns() * 100) / (logDTO.getSchoolRequiredRunningDate() * 2);
		BigDecimal percentage1 = (BigDecimal.valueOf(logDTO.getNoOfTurns()).multiply(z));
		BigDecimal percentage2 = (BigDecimal.valueOf(logDTO.getSchoolRequiredRunningDate()).multiply(turns));
		BigDecimal percentage = percentage1.divide(percentage2, 2, RoundingMode.HALF_DOWN);

		BigDecimal finalpercentage = percentage;
		logDTO.setRunningD(finalpercentage);

		if (percentageN >= 90) {
			// pending calculations
			BigDecimal a = (BigDecimal.valueOf(logDTO.getSchoolRequiredRunningDate()).multiply(turns));
			BigDecimal b = a.subtract(BigDecimal.valueOf(logDTO.getNoOfTurns()));
			BigDecimal x = b.multiply(turns);

			BigDecimal testPenaltyFee = x.multiply(logDTO.getSubsidyD()).multiply(logDTO.getTotalLengthD());

			BigDecimal subsidyPayment = BigDecimal.valueOf(logDTO.getNoOfTurns()).multiply(logDTO.getTotalLengthD())
					.multiply(logDTO.getSubsidyD());
			BigDecimal penaltyFee = new BigDecimal(0.00);

			logDTO.setPenaltyFeeD(testPenaltyFee);

			BigDecimal lateFee = BigDecimal.valueOf(logDTO.getLateTrips()).multiply(logDTO.getTotalLengthD())
					.multiply(logDTO.getSubsidyD());
			logDTO.setLateFeeD(lateFee);

			if (logDTO.getOtherPercentage() == 0) {
				totalAmount = (subsidyPayment.subtract(penaltyFee).subtract(lateFee)
						.subtract(BigDecimal.valueOf(logDTO.getDeductions())).subtract(logDTO.getPenaltyFeeD())
						.add(BigDecimal.valueOf(logDTO.getArrears())));
				logDTO.setPaymentD(totalAmount);
				logDTO.setOtherPercentageCalculationValD(new BigDecimal("0.00"));
			} else if (logDTO.getOtherPercentage() != 0 && logDTO.getOtherPercentage() > 0) {
				double selectedPercentage = 100 - logDTO.getOtherPercentage();
				BigDecimal selectedPrecentageRound = BigDecimal.valueOf(selectedPercentage);
				BigDecimal j = subsidyPayment.subtract(penaltyFee).subtract(lateFee).subtract(logDTO.getPenaltyFeeD());
				BigDecimal currentCal = (j.multiply(selectedPrecentageRound)).divide(z);
				totalAmount = (currentCal.subtract(BigDecimal.valueOf(logDTO.getDeductions())))
						.add(BigDecimal.valueOf(logDTO.getArrears()));
				logDTO.setPaymentD(totalAmount);
				BigDecimal selectedPreCalValue = (BigDecimal.valueOf(logDTO.getOtherPercentage()).multiply(j))
						.divide(z);
				logDTO.setOtherPercentageCalculationValD(selectedPreCalValue);
			} else {
				logDTO.setPaymentD(totalAmount);
			}

		} else {
			logDTO.setPaymentD(totalAmount);
		}

		RequestContext.getCurrentInstance().update("xx");
		RequestContext.getCurrentInstance().update("xx:payment");

		// formula given by damith on 27/6/2019
	}

	public void calculationGami() {
		BigDecimal totalAmount = new BigDecimal("0.00");
		boolean calcpay = false;

		int noOfTipsPerDay = logDTO.getTripsPerDay();
		BigDecimal r = new BigDecimal("90.00");
		BigDecimal s = new BigDecimal("100.00");

		BigDecimal validTurnsFor90 = ((BigDecimal.valueOf(logDTO.getSchoolRequiredRunningDate())
				.multiply(BigDecimal.valueOf(noOfTipsPerDay))).multiply(r)).divide(s);

		double percentageN = (logDTO.getNoOfTurns() * 100) / (logDTO.getSchoolRequiredRunningDate() * noOfTipsPerDay);
		BigDecimal percentage1 = (BigDecimal.valueOf(logDTO.getNoOfTurns())).multiply(s);
		BigDecimal percentage2 = (BigDecimal.valueOf(logDTO.getSchoolRequiredRunningDate())
				.multiply(BigDecimal.valueOf(noOfTipsPerDay)));
		BigDecimal percentage = percentage1.divide(percentage2, 2, RoundingMode.HALF_DOWN);

		BigDecimal finalpercentage = percentage;
		logDTO.setRunningD(finalpercentage);

		int requestedPercentage = logDTO.getRequested();

		if (percentageN >= 45) {
			if (percentageN >= requestedPercentage) {

				BigDecimal penaltyFee = new BigDecimal("0.00");
				logDTO.setPenaltyFeeD(penaltyFee);

				BigDecimal lateFee = (BigDecimal.valueOf(logDTO.getLateTrips()).multiply(logDTO.getTotalLengthD())
						.multiply(logDTO.getSubsidyD()));
				logDTO.setLateFeeD(lateFee);

				BigDecimal subsidyPayment = (BigDecimal.valueOf(logDTO.getNoOfTurns())
						.multiply(logDTO.getTotalLengthD()).multiply(logDTO.getSubsidyD()));
				if (logDTO.getOtherPercentage() == 0) {
					totalAmount = subsidyPayment.subtract(lateFee).subtract(BigDecimal.valueOf(logDTO.getDeductions()))
							.subtract(logDTO.getPenaltyFeeD()).add(BigDecimal.valueOf(logDTO.getArrears()));
					logDTO.setPaymentD(totalAmount);
					logDTO.setOtherPercentageCalculationValD(new BigDecimal("0.00"));

				} else if (logDTO.getOtherPercentage() != 0 && logDTO.getOtherPercentage() > 0) {
					BigDecimal selectedPercentage = s.subtract(BigDecimal.valueOf(logDTO.getOtherPercentage()));
					BigDecimal selectedPrecentageRound = selectedPercentage;
					BigDecimal j = subsidyPayment.subtract(logDTO.getPenaltyFeeD()).subtract(lateFee);
					BigDecimal currentCal = (j.multiply(selectedPrecentageRound)).divide(s, 2, RoundingMode.HALF_DOWN);

					totalAmount = currentCal.subtract(BigDecimal.valueOf(logDTO.getDeductions()))
							.add(BigDecimal.valueOf(logDTO.getArrears()));
					logDTO.setPaymentD(totalAmount);
					BigDecimal selectedPreCalValue = (BigDecimal.valueOf(logDTO.getOtherPercentage()).multiply(j))
							.divide(s, 2, RoundingMode.HALF_DOWN);
					logDTO.setOtherPercentageCalculationValD(selectedPreCalValue);
				} else {
					logDTO.setPaymentD(totalAmount);
				}
			} else {

				// calculate penalty fee
				BigDecimal x = validTurnsFor90.subtract(BigDecimal.valueOf(logDTO.getNoOfTurns()));

				BigDecimal penaltyFee = new BigDecimal("0.00");

				penaltyFee = x.multiply(logDTO.getTotalLengthD()).multiply(logDTO.getSubsidyD());
				logDTO.setPenaltyFeeD(penaltyFee);

				BigDecimal lateFee = BigDecimal.valueOf(logDTO.getLateTrips()).multiply(logDTO.getTotalLengthD())
						.multiply(logDTO.getSubsidyD());
				logDTO.setLateFeeD(lateFee);

				BigDecimal subsidyPayment = BigDecimal.valueOf(logDTO.getNoOfTurns()).multiply(logDTO.getTotalLengthD())
						.multiply(logDTO.getSubsidyD());
				if (logDTO.getOtherPercentage() == 0) {

					totalAmount = subsidyPayment.subtract(lateFee).subtract(BigDecimal.valueOf(logDTO.getDeductions()))
							.subtract(logDTO.getPenaltyFeeD()).add(BigDecimal.valueOf(logDTO.getArrears()));

					logDTO.setPaymentD(totalAmount);
					logDTO.setOtherPercentageCalculationValD(new BigDecimal("0.00"));
				} else if (logDTO.getOtherPercentage() != 0 && logDTO.getOtherPercentage() > 0) {

					BigDecimal selectedPercentage = s.subtract(BigDecimal.valueOf(logDTO.getOtherPercentage()));
					BigDecimal selectedPrecentageRound = selectedPercentage;

					BigDecimal j = subsidyPayment.subtract(logDTO.getPenaltyFeeD()).subtract(lateFee);

					BigDecimal currentCal = (j.multiply(selectedPrecentageRound)).divide(s, 2, RoundingMode.HALF_DOWN);

					totalAmount = currentCal.subtract(BigDecimal.valueOf(logDTO.getDeductions()))
							.add(BigDecimal.valueOf(logDTO.getArrears()));
					logDTO.setPaymentD(totalAmount);

					BigDecimal selectedPreCalValue = (BigDecimal.valueOf(logDTO.getOtherPercentage()).multiply(j))
							.divide(s, 2, RoundingMode.HALF_DOWN);
					logDTO.setOtherPercentageCalculationValD(selectedPreCalValue);
				} else {
					logDTO.setPaymentD(totalAmount);
				}
			}
		} else {
			logDTO.setPaymentD(totalAmount);
		}

		RequestContext.getCurrentInstance().update("xx");
		RequestContext.getCurrentInstance().update("xx:payment");
	}

	public void calculationNisi() {
		BigDecimal totalAmount = new BigDecimal(0.0);
		boolean calcpay = false;
		int noOfTipsPerDay = logDTO.getTripsPerDay();
		BigDecimal z = new BigDecimal(100.00);
		Boolean boolValidate = false;

		BigDecimal validTurnsFor100 = (BigDecimal.valueOf(logDTO.getSchoolRequiredRunningDate())
				.multiply(BigDecimal.valueOf(noOfTipsPerDay))).multiply(z).divide(z, 2, RoundingMode.HALF_DOWN);

		BigDecimal percentage1 = (BigDecimal.valueOf(logDTO.getNoOfTurns()).multiply(z));
		BigDecimal percentage2 = (BigDecimal.valueOf(logDTO.getSchoolRequiredRunningDate())
				.multiply(BigDecimal.valueOf(noOfTipsPerDay)));
		BigDecimal percentage = percentage1.divide(percentage2, 2, RoundingMode.HALF_DOWN);
		BigDecimal finalpercentage = percentage;

		logDTO.setRunningD(finalpercentage);

		BigDecimal requestedPercentage = new BigDecimal(0.00);
		requestedPercentage = new BigDecimal("100.00");

		double percentageN = (logDTO.getNoOfTurns() * 100) / (logDTO.getSchoolRequiredRunningDate() * noOfTipsPerDay);
		int requestedPercentageN = logDTO.getRequested();

		if (finalpercentage.equals(requestedPercentage)) {
			BigDecimal penaltyFee = new BigDecimal(0.00);
			logDTO.setPenaltyFeeD(penaltyFee);

			BigDecimal lateFee = (BigDecimal.valueOf(logDTO.getLateTrips()).multiply(logDTO.getTotalLengthD())
					.multiply(logDTO.getSubsidyD()));
			logDTO.setLateFeeD(lateFee);

			BigDecimal subsidyPayment = (BigDecimal.valueOf(logDTO.getNoOfTurns()).multiply(logDTO.getTotalLengthD())
					.multiply(logDTO.getSubsidyD()));

			if (logDTO.getOtherPercentage() == 0) {

				totalAmount = (subsidyPayment.subtract(lateFee).subtract(BigDecimal.valueOf(logDTO.getDeductions()))
						.subtract(logDTO.getPenaltyFeeD()).add(BigDecimal.valueOf(logDTO.getArrears())));
				logDTO.setPaymentD(totalAmount);
				logDTO.setOtherPercentageCalculationValD(new BigDecimal(0.00));

			} else if (logDTO.getOtherPercentage() != 0 && logDTO.getOtherPercentage() > 0) {

				boolValidate = true;
				double selectedPercentage = 100 - logDTO.getOtherPercentage();
				int selectedPrecentageRound = (int) selectedPercentage;

				BigDecimal j = (subsidyPayment.subtract(logDTO.getPenaltyFeeD()).subtract(lateFee));
				BigDecimal y = new BigDecimal(100.00);
				BigDecimal currentCal = ((j.multiply(BigDecimal.valueOf(selectedPrecentageRound))).divide(y, 2,
						RoundingMode.HALF_DOWN));
				totalAmount = (currentCal.subtract(BigDecimal.valueOf(logDTO.getDeductions())))
						.add(BigDecimal.valueOf(logDTO.getArrears()));
				logDTO.setPaymentD(totalAmount);
				BigDecimal selectedPreCalValue = (BigDecimal.valueOf(logDTO.getOtherPercentage()).multiply(j)).divide(y,
						2, RoundingMode.HALF_DOWN);

				logDTO.setOtherPercentageCalculationValD(selectedPreCalValue);

			} else {
				logDTO.setPaymentD(totalAmount);
			}

		} else if (percentageN < requestedPercentageN) {
			BigDecimal x = validTurnsFor100.subtract(BigDecimal.valueOf(logDTO.getNoOfTurns()));
			BigDecimal penaltyFee = new BigDecimal(0.0);
			penaltyFee = x.multiply(logDTO.getTotalLengthD()).multiply(logDTO.getSubsidyD());
			logDTO.setPenaltyFeeD(penaltyFee);
			BigDecimal lateFee = BigDecimal.valueOf(logDTO.getLateTrips()).multiply(logDTO.getTotalLengthD())
					.multiply(logDTO.getSubsidyD());
			logDTO.setLateFeeD(lateFee);
			BigDecimal subsidyPayment = (BigDecimal.valueOf(logDTO.getNoOfTurns())).multiply(logDTO.getTotalLengthD())
					.multiply(logDTO.getSubsidyD());

			if (logDTO.getOtherPercentage() == 0) {
				totalAmount = subsidyPayment.subtract(lateFee).subtract(BigDecimal.valueOf(logDTO.getDeductions()))
						.subtract(logDTO.getPenaltyFeeD()).add(BigDecimal.valueOf(logDTO.getArrears()));
				logDTO.setPaymentD(totalAmount);
				logDTO.setOtherPercentageCalculationValD(new BigDecimal(0.00));

			} else if (logDTO.getOtherPercentage() != 0 && logDTO.getOtherPercentage() > 0) {
				double selectedPercentage = 100 - logDTO.getOtherPercentage();
				BigDecimal selectedPrecentageRound = BigDecimal.valueOf(selectedPercentage);
				BigDecimal j = (subsidyPayment.subtract(logDTO.getPenaltyFeeD()).subtract(lateFee));
				BigDecimal currentCal = (j.multiply(selectedPrecentageRound).divide(z, 2, RoundingMode.HALF_DOWN));
				totalAmount = currentCal.subtract(BigDecimal.valueOf(logDTO.getDeductions()))
						.add(BigDecimal.valueOf(logDTO.getArrears()));
				logDTO.setPaymentD(totalAmount);
				BigDecimal selectedPreCalValue = ((BigDecimal.valueOf(logDTO.getOtherPercentage()).multiply(j))
						.divide(z, 2, RoundingMode.HALF_DOWN));
				logDTO.setOtherPercentageCalculationValD(selectedPreCalValue);

			} else {
				// logDTO.setPayment(0);
				logDTO.setPaymentD(totalAmount);
			}

		}
		RequestContext.getCurrentInstance().update("xx");
		RequestContext.getCurrentInstance().update("xx:payment");

	}

	public void save() {

		if (!logDTO.getServiceType().equals("") && logDTO.getServiceType() != null) {
			if (logDTO.getServiceType().equals("S01")) {
				calculation();
				logsheetMaintenanceService.editFromgLogSheet(logDTO);
				disable = true;
				sessionBackingBean.setSessLogDTO(logDTO);
				sessionBackingBean.setSessiondatatableList(datatableList);
				sessionBackingBean.setSessiondisable(disable);
				setSucessMsg("Data saved successfully");

				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			} else if (logDTO.getServiceType().equals("S02")) {
				calculationGami();
				logsheetMaintenanceService.editFromgLogSheetGamiAndNisi(logDTO);
				disable = true;
				sessionBackingBean.setSessLogDTO(logDTO);
				sessionBackingBean.setSessiondatatableList(datatableList);
				sessionBackingBean.setSessiondisable(disable);
				setSucessMsg("Data saved successfully");

				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			} else if (logDTO.getServiceType().equals("S03")) {
				calculationNisi();
				logsheetMaintenanceService.editFromgLogSheetGamiAndNisi(logDTO);
				disable = true;
				sessionBackingBean.setSessLogDTO(logDTO);
				sessionBackingBean.setSessiondatatableList(datatableList);
				sessionBackingBean.setSessiondisable(disable);
				setSucessMsg("Data saved successfully.");

				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			} else {

			}
		} else {

		}

	}

	public void delete() {

		logDTO.setLogRefNo(selectedRow.getLogRefNo());
		logsheetMaintenanceService.deleteFromLogSheet(logDTO);
		setSucessMsg("Data deleted for the record successfully.");
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		sessionBackingBean.setSessLogDTO(logDTO);
		sessionBackingBean.setSessiondatatableList(datatableList);
	}

	public void edit() {

		logDTO.setLogRefNo(selectedRow.getLogRefNo());
		boolean checkdata = logsheetMaintenanceService.checkData(logDTO);
		if (checkdata == true) {
			logDTO.setLogRefNo(selectedRow.getLogRefNo());
			logDTO = logsheetMaintenanceService.datafromLogSheet(logDTO);
			presetValues(true);

			setWarningMsg("Data for the record loaded successfully. Edit below.");
			RequestContext.getCurrentInstance().update("frmWarning");
			RequestContext.getCurrentInstance().execute("PF('warning').show()");
			disable = false;
			sessionBackingBean.setSessLogDTO(logDTO);
			sessionBackingBean.setSessiondatatableList(datatableList);
			sessionBackingBean.setSessiondisable(disable);

		} else if (checkdata == false) {
			errorMsg = "Data is not available for selected record. Add data first.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			sessionBackingBean.setSessLogDTO(logDTO);
			sessionBackingBean.setSessiondatatableList(datatableList);

		}

	}

	public void add() {
		logDTO.setLogRefNo(selectedRow.getLogRefNo());
		boolean checkdata = logsheetMaintenanceService.checkData(logDTO);
		if (checkdata == true) {
			errorMsg = "Data is already entered. Delete to add data again.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			sessionBackingBean.setSessLogDTO(logDTO);
			sessionBackingBean.setSessiondatatableList(datatableList);
		} else if (checkdata == false) {
			setWarningMsg("Add data for the record below.");
			RequestContext.getCurrentInstance().update("frmWarning");
			RequestContext.getCurrentInstance().execute("PF('warning').show()");
			disable = false;
			if (logDTO.getServiceType() != null || !logDTO.getServiceType().isEmpty()
					|| !logDTO.getServiceType().equals("")) {
				if (logDTO.getServiceType().equals("S01")) {

				} else if (logDTO.getServiceType().equals("S02")) {
					logDTO.setRequestedD(new BigDecimal(90.00));
					logDTO.setRequested(90);

				} else if (logDTO.getServiceType().equals("S03")) {
					logDTO.setRequestedD(new BigDecimal(100.00));
					logDTO.setRequested(100);

				} else {

				}
			} else {

			}
			presetValues(false);
			sessionBackingBean.setSessLogDTO(logDTO);
			sessionBackingBean.setSessiondatatableList(datatableList);
			sessionBackingBean.setSessiondisable(disable);
		}

	}

	public void view() {
		logDTO.setLogRefNo(selectedRow.getLogRefNo());
		boolean checkdata = logsheetMaintenanceService.checkData(logDTO);
		if (checkdata == false) {
			disable = true;

			errorMsg = "No data available for the selected record. Please insert data.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			sessionBackingBean.setSessLogDTO(logDTO);
			sessionBackingBean.setSessiondatatableList(datatableList);
			sessionBackingBean.setSessiondisable(disable);

		}

		else {
			setSucessMsg("Data loaded successfully.");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			disable = true;
			logDTO = logsheetMaintenanceService.datafromLogSheet(logDTO);
			presetValues(true);
			sessionBackingBean.setSessLogDTO(logDTO);
			sessionBackingBean.setSessiondatatableList(datatableList);
			sessionBackingBean.setSessiondisable(disable);
			sessionBackingBean.setApprovebutton(false);

		}
	}

	public LogSheetMaintenanceDTO getLogDTO() {
		return logDTO;
	}

	public void setLogDTO(LogSheetMaintenanceDTO logDTO) {
		this.logDTO = logDTO;
	}

	public List<String> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(List<String> refNoList) {
		this.refNoList = refNoList;
	}

	public List<LogSheetMaintenanceDTO> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<LogSheetMaintenanceDTO> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public List<String> getServiceNoList() {
		return serviceNoList;
	}

	public void setServiceNoList(List<String> serviceNoList) {
		this.serviceNoList = serviceNoList;
	}

	public List<LogSheetMaintenanceDTO> getDatatableList() {
		return datatableList;
	}

	public void setDatatableList(List<LogSheetMaintenanceDTO> datatableList) {
		this.datatableList = datatableList;
	}

	public LogSheetMaintenanceDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(LogSheetMaintenanceDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getWarningMsg() {
		return warningMsg;
	}

	public void setWarningMsg(String warningMsg) {
		this.warningMsg = warningMsg;
	}

	public boolean isApprovalBoolean() {
		return approvalBoolean;
	}

	public void setApprovalBoolean(boolean approvalBoolean) {
		this.approvalBoolean = approvalBoolean;
	}

	public boolean isSearchCheck() {
		return searchCheck;
	}

	public void setSearchCheck(boolean searchCheck) {
		this.searchCheck = searchCheck;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isApproveMode() {
		return approveMode;
	}

	public void setApproveMode(boolean approveMode) {
		this.approveMode = approveMode;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isShowSchoolCalPanel() {
		return showSchoolCalPanel;
	}

	public void setShowSchoolCalPanel(boolean showSchoolCalPanel) {
		this.showSchoolCalPanel = showSchoolCalPanel;
	}

	public boolean isShowGamiSeriyaCalPanel() {
		return showGamiSeriyaCalPanel;
	}

	public void setShowGamiSeriyaCalPanel(boolean showGamiSeriyaCalPanel) {
		this.showGamiSeriyaCalPanel = showGamiSeriyaCalPanel;
	}

	public boolean isShowNisiCalPanel() {
		return showNisiCalPanel;
	}

	public void setShowNisiCalPanel(boolean showNisiCalPanel) {
		this.showNisiCalPanel = showNisiCalPanel;
	}

	public List<SisuSeriyaDTO> getOperatorDepoNameList() {
		return operatorDepoNameList;
	}

	public void setOperatorDepoNameList(List<SisuSeriyaDTO> operatorDepoNameList) {
		this.operatorDepoNameList = operatorDepoNameList;
	}

	public LogsheetMaintenanceService getLogsheetMaintenanceService() {
		return logsheetMaintenanceService;
	}

	public void setLogsheetMaintenanceService(LogsheetMaintenanceService logsheetMaintenanceService) {
		this.logsheetMaintenanceService = logsheetMaintenanceService;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

}
