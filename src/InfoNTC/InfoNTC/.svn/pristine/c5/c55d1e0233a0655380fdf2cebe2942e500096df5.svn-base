package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.primefaces.context.RequestContext;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import lk.informatics.ntc.model.dto.TerminalArrivalDepartureDTO;
import lk.informatics.ntc.model.dto.TerminalArrivalDepartureTimeDTO;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

/**
 * 
 * @author dilakshi.h
 *
 */
@ViewScoped
@ManagedBean(name = "terminalAuthorizeBackingBean")
public class TerminalAuthorizeBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private TerminalManagementService terminalManagementService;

	private String sucessMsg;
	private String errorMsg;

	String dateFormatStr = "dd/MM/yyyy";
	DateFormat oldDf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
	DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
	private Date currentDate = new Date();

	private String busNo;
	private boolean showDetail = false;
	private boolean validity;
	private List<String> authorizeMsgs = new ArrayList<String>();
	private String delayTimeDiff = "";
	//private int turnNo;

	private String estimatedArrival;
	private String scheduleArrivalTime;
	private String sheduleDepartureTime;
	private String actualArrivalTime;
	private String arrivingStation;
	private String arrivingTerminal;
	private String arrivingBlock;
	private String arriveFromStation;
	private String arriveFromTerminal;
	private String tripStartDate;
	private String tripStartTime;

	private String estimatedDeparture;
	private String actualDepartureTime;
	private String departingStation;
	private String departingTerminal;
	private String departingBlock;
	private String departForStation;
	private String departForTerminal;
	private String tripEndDate;
	private String tripEndTime;

	private String route;
	// vehicle info
	private String permitType;
	private String permitNo;
	private String dateOfIssue;
	private String dateOfExpiry;
	private String owner;
	private String address;
	private String contactNo;
	private String stationDes;
	private TerminalArrivalDepartureDTO vehicleDTO = new TerminalArrivalDepartureDTO();
	private TerminalArrivalDepartureTimeDTO currentSchedule = new TerminalArrivalDepartureTimeDTO();
	private int dayOfWeek; // 0=SUNDAY, 1=MONDAY...
	private boolean localcheckcounter = true;
	private String stationName = null;
	boolean showStationPopup = true;
	private String routeDestination= null; ;
	public boolean leaveBus;
	public int  turnNoA;
	public int  turnNoD;
	List<String> busInTime = new ArrayList<>();
	List<String> busOutTime = new ArrayList<>();
	private boolean wrongAbrriviation = false;
	List<String> busInTimeFinal = new ArrayList<>();
	List<String> busOutTimeFinal = new ArrayList<>();
	public TerminalAuthorizeBackingBean() {
		terminalManagementService = (TerminalManagementService) SpringApplicationContex
				.getBean("terminalManagementService");
		
		

	}

	/****************************************************************************************************************************
	 ********************************** ARRIVAL AUTHORIZATION
	 * *******************************************************************
	 ***************************************************************************************************************************/

	public void onCounterSelect() {
		sessionBackingBean.setStationName(stationDes);
		stationName = terminalManagementService.getStationNameByID(stationDes);
		localcheckcounter = false;
		RequestContext context = RequestContext.getCurrentInstance();

		context.execute("PF('dlg2').hide();");

	}

	public void handleClose() throws InterruptedException {

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/pages/home/welcomePage.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void openStationPopUp() {
		busNo = null;
		stationDes = null;
		stationName = null;
		localcheckcounter = true;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlg2').show();");

	}

	public void cancelDialog() {
		sessionBackingBean.setStationName(stationDes);
		stationName = terminalManagementService.getStationNameByID(stationDes);
	}
	/*
	 * > check scheduled arrivals for current date > insert all scheduled arrival
	 * time list into nt_t_terminal_arrival_log > when bus is entered into station,
	 * > nt_t_terminal_arrival_log is updated with actual_arrival, turn_no,
	 * authorization_status
	 */

	public void searchByBusNumberArrival() throws ParseException {
		authorizeMsgs = new ArrayList<String>();
		LocalDateTime now = LocalDateTime.now();
		LocalTime nowTime = LocalTime.now();
		actualArrivalTime = now.format(timeFormat);
		String user = sessionBackingBean.loginUser;
		sessionBackingBean.setStationName(stationDes);
		String busRoute;
		String busService;
		String estimatedArrivalNew = null;
		// validate bus number
		if (busNo == null || busNo.trim().isEmpty()
				|| !terminalManagementService.validateVehicleOrPermitNo("VEHICLE", busNo.trim().toUpperCase())) {
			errorMsg = "Invalid Bus Number!";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}

		// check station has route number according to bus number
		busRoute = terminalManagementService.getRouteAndServiceTypeByBusNo(busNo.trim().toUpperCase(), true);
		busService = terminalManagementService.getRouteAndServiceTypeByBusNo(busNo.trim().toUpperCase(), false);
		boolean isCorrectRouteForStation = terminalManagementService.checkRouteCorrectForStation(busRoute, busService,
				sessionBackingBean.getStationName());

		if (!isCorrectRouteForStation) {

			errorMsg = "Route not assigned for this station.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}
		
		try {
			checkCorrectBusOnTime("O", busRoute, busService);
			if(leaveBus) {
				errorMsg = " This bus is on leave today.";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				return;
			}
			if(wrongAbrriviation) {
				errorMsg = " Please check entered bus number again.";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				return;
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		
		// check entered bus number is departed to arrive again
		
		boolean isFirstArrive = terminalManagementService.checkFirstArrive(busNo.trim().toUpperCase(), busRoute,
				busService, sessionBackingBean.getStationName());
		/*//chech whether bus came yesterday and depart
	    boolean cameYesterdayNDepart=terminalManagementService.checkYerterDayArriveNDepart(busNo.trim().toUpperCase(), busRoute,
				busService, sessionBackingBean.getStationName()); 2021/09/03 based on discussion had with BA team */
		
		//if(cameYesterdayNDepart) {
		if(!isFirstArrive) {
		boolean busDepartued = terminalManagementService.checkLatestActualArrivedBusIdDepartued(busNo.trim().toUpperCase(), busRoute,
				busService, sessionBackingBean.getStationName());	
		 if(busDepartued) {
		 turnNoA =terminalManagementService.getNoOfTurns(busNo.trim().toUpperCase(), busRoute,
				busService, sessionBackingBean.getStationName());	
		
		}
		else {
			errorMsg = "This bus is not depart yet.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}
		}
		else {
			turnNoA =1;
		}
		/*}
		else {
			errorMsg = "This bus is arrive yesterday and  not depart yet.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}*/
		// check permit number is active
		boolean isActivePermit = terminalManagementService.checkPermitStatusInActive(busNo.trim().toUpperCase());
		if (!isActivePermit) {
			validity =false;
			authorizeMsgs.add("Permit is not in active status.");
		}
		vehicleDTO = authorizeData("ARRIVAL");

		/*List<TerminalArrivalDepartureTimeDTO> scheduledTimeList = terminalManagementService
				.getScheduledArrivalForDay(vehicleDTO, dayOfWeek);*/
		List<TerminalArrivalDepartureTimeDTO> scheduledTimeList = new ArrayList<>();
		
		
		
		if(!busInTime.isEmpty() && !busOutTime.isEmpty() ) {
			for(int i=0;i<busInTime.size();i++) {
			TerminalArrivalDepartureTimeDTO dto = new TerminalArrivalDepartureTimeDTO();
			dto.setStartTime(busInTime.get(i));
			dto.setEndTime(busOutTime.get(i));
			scheduledTimeList.add(dto);
			}
		}
		                                                   
		
		vehicleDTO.setScheduledTimeListForDay(scheduledTimeList);
		vehicleDTO.setStationCode(sessionBackingBean.getStationName());
		vehicleDTO = terminalManagementService.insertTerminalArrivalLog(vehicleDTO, dayOfWeek, user);
		terminalManagementService.beanLinkMethod("Add Terminal Arrival Log", sessionBackingBean.getLoginUser(), "Arrival Authorization", busNo, null, null);
		
		
		// checked Suspend driver/condutor

		boolean isSuspendDriverConductor = terminalManagementService
				.checkDriverConductorSuspend(busNo.trim().toUpperCase());
		if (isSuspendDriverConductor) {
			validity= false;
			authorizeMsgs.add("Driver/Condutor blacklisted. ");
		}
		// check the payment has done for month
		boolean paymentDone = terminalManagementService.isPaymentDoneForCurrentMonth(busNo.trim().toUpperCase());
		if (!paymentDone) {
			validity = false;
			authorizeMsgs.add("Should complete the payment. ");
		}
		// match the expected arrival with sorted schedule list //when ActualArrival is
		// null bus has not arrived
		for (TerminalArrivalDepartureTimeDTO schedule : nullSafe(vehicleDTO.getScheduledTimeListForDay())) {
			if (schedule.getActualArrivalDeparture() == null || schedule.getActualArrivalDeparture().isEmpty()) {
				String paramTime = terminalManagementService
						.getParamTimeAdded("BUS_ARRIVAL_DEPARTURE_TIME_FOR_TERMINAL");
				estimatedArrival = schedule.getStartTime();
				estimatedArrivalNew =schedule.getStartTime();
				int paramTimeInt = Integer.parseInt(paramTime);
				SimpleDateFormat df = new SimpleDateFormat("HH:mm"); // changed here
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

				Date it = df.parse(estimatedArrival);
				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(it);
				cal2.add(Calendar.MINUTE, (paramTimeInt * -1));

				DateFormat dateFormat = new SimpleDateFormat("HH:mm");
				String strDate = dateFormat.format(cal2.getTime());

				scheduleArrivalTime = strDate;
				Date it2 = df.parse(estimatedArrival);
				Calendar cal3 = Calendar.getInstance();
				cal3.setTime(it2);
				cal3.add(Calendar.MINUTE, (paramTimeInt * 1));
				String strDate2 = dateFormat.format(cal3.getTime());
				estimatedArrival=strDate2;
				tripStartTime = schedule.getStartTime();
				currentSchedule = schedule;
				
				// show etimated time in 24 formats
				
				break;
			}
		}

		// validate arrival with schedule, no of trips
		if (vehicleDTO.getScheduledTimeListForDay() == null || vehicleDTO.getScheduledTimeListForDay().isEmpty()) {
			validity = false;
			authorizeMsgs.add("Bus is not scheduled for today");
		} else if (estimatedArrival == null || estimatedArrival.isEmpty()) {
			validity = false;
			authorizeMsgs.add("Exceeded the no of trips for the day");
		} else {
			// calculate arrival time difference
			
			LocalTime arrival = LocalTime.parse(estimatedArrivalNew);
			long hr = ChronoUnit.HOURS.between(nowTime, arrival);
			long min = ChronoUnit.MINUTES.between(nowTime, arrival);

			if (hr != 0)
				delayTimeDiff = Math.abs(hr) + " hr ";
			if (min != 0) {
				long m = Math.abs(min) - Math.abs(hr * 60);
				delayTimeDiff += m + " min";
			}
			if (hr > 0 || min > 0)
				delayTimeDiff += " early";
			else if (hr < 0 || min < 0)
				delayTimeDiff += " delay";
		}

	
		
		vehicleDTO.setTurnNo(turnNoA);
		terminalManagementService.beanLinkMethod("Add Terminal Arrival Log", sessionBackingBean.getLoginUser(), "Arrival Authorization", busNo, null, null);

	}

	public void allowToEnter() {
		String user = sessionBackingBean.loginUser;
		String errors = String.join(",", authorizeMsgs);

		if (currentSchedule.getSequence() != null
				&& terminalManagementService.updateTerminalArrivalLog(currentSchedule.getSequence(), actualArrivalTime,
						turnNoA, validity ? "Y" : "N", errors, dayOfWeek, user)) {
			terminalManagementService.beanLinkMethod("Update Terminal Arrival Log (Enter)", sessionBackingBean.getLoginUser(), "Arrival Authorization", busNo, null, null);

			sucessMsg = "Arrival Log Updated Successfuly!";
			RequestContext.getCurrentInstance().update("frmSuccess");
			RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
			resetView();
		} else {
			errorMsg = "Error!";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		}
	}

	public TerminalArrivalDepartureDTO authorizeData(String arrivalDepartureType) {
		showDetail = true;
		//validity = true;

		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		// using current station as 'CMB'. this should be change when multiple stations
		// are in use
		vehicleDTO = terminalManagementService.getDetailsByBusNo(busNo.trim().toUpperCase(), stationDes);
		routeDestination = vehicleDTO.getDestinationDesc();
		busNo = busNo.trim().toUpperCase();
		route = vehicleDTO.getRouteDesc();
		vehicleDTO.getRouteNo();
		owner = vehicleDTO.getOwner();
		permitNo = vehicleDTO.getPermitNo();
		permitType = vehicleDTO.getServiceType();
		dateOfIssue = vehicleDTO.getPermitIssueDate();
		dateOfExpiry = vehicleDTO.getPermitExpireDate();
		address = vehicleDTO.getAddress1();
		if (vehicleDTO.getAddress2() != null && !vehicleDTO.getAddress2().trim().isEmpty())
			address += ", " + vehicleDTO.getAddress2();
		if (vehicleDTO.getAddress3() != null && !vehicleDTO.getAddress3().trim().isEmpty())
			address += ", " + vehicleDTO.getAddress3();
		if (vehicleDTO.getCity() != null && !vehicleDTO.getCity().trim().isEmpty())
			address += ", " + vehicleDTO.getCity();

		contactNo = vehicleDTO.getMobileNo();
		if (contactNo == null || contactNo.trim().isEmpty())
			contactNo = vehicleDTO.getTelephoneNo();

		if (arrivalDepartureType.equals("ARRIVAL")) {
			arrivingStation = vehicleDTO.getStationDesc();
			arrivingTerminal = vehicleDTO.getTerminal();
			arrivingBlock = vehicleDTO.getBlock();
		} else {
			departingStation = vehicleDTO.getStationDesc();
			departingTerminal = vehicleDTO.getTerminal();
			departingBlock = vehicleDTO.getBlock();

		}

		if (dateOfExpiry != null) {
			try {
				Date expDate = dateFormat.parse(dateOfExpiry);
				if (getDateDifference(expDate, currentDate) > 0) {
					validity = false;
					authorizeMsgs.add("Permit has Expired");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return vehicleDTO;
	}

	public void resetView() {
		busNo = null;
		showDetail = false;
		validity = true;
		authorizeMsgs = new ArrayList<String>();
		delayTimeDiff = "";
		turnNoA = 0;
		turnNoD = 0;
		route = null;
		estimatedArrival = null;
		scheduleArrivalTime = null;
		sheduleDepartureTime=null;
		actualArrivalTime = null;
		arrivingStation = null;
		arrivingTerminal = null;
		arrivingBlock = null;
		arriveFromStation = null;
		arriveFromTerminal = null;
		tripStartDate = null;
		tripStartTime = null;
		estimatedDeparture = null;
		actualDepartureTime = null;
		departingStation = null;
		departingTerminal = null;
		departingBlock = null;
		departForStation = null;
		departForTerminal = null;
		tripEndDate = null;
		tripEndTime = null;
		permitType = null;
		permitNo = null;
		dateOfIssue = null;
		dateOfExpiry = null;
		owner = null;
		address = null;
		contactNo = null;
		vehicleDTO = new TerminalArrivalDepartureDTO();
		currentSchedule = new TerminalArrivalDepartureTimeDTO();
	}

	/****************************************************************************************************************************
	 ********************************** DEPARTURE AUTHORIZATION
	 * *
	 * @throws ParseException ******************************************************************
	 ***************************************************************************************************************************/
	/*
	 * > check scheduled arrivals for current date > insert all scheduled arrival
	 * time list into nt_t_terminal_departure_log > when bus is leaving the station,
	 * > nt_t_terminal_departure_log is updated with actual_departure, turn_no,
	 * authorization_status
	 */

	public void searchByBusNumberDeparture() throws ParseException {
		authorizeMsgs = new ArrayList<String>();
		LocalDateTime now = LocalDateTime.now();
		LocalTime nowTime = LocalTime.now();
		actualDepartureTime = now.format(timeFormat);
		String user = sessionBackingBean.loginUser;

		String busRoute = null;
		String busService = null;
		String estimatedDepartureNew = null;
		// validate bus number
		if (busNo == null || busNo.trim().isEmpty()
				|| !terminalManagementService.validateVehicleOrPermitNo("VEHICLE", busNo.trim().toUpperCase())) {
			errorMsg = "Invalid Bus Number!";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}
		
		
		
		// check station has route number according to bus number
		busRoute = terminalManagementService.getRouteAndServiceTypeByBusNo(busNo.trim().toUpperCase(), true);
		busService = terminalManagementService.getRouteAndServiceTypeByBusNo(busNo.trim().toUpperCase(), false);
		boolean isCorrectRouteForStation = terminalManagementService.checkRouteCorrectForStation(busRoute, busService,
				sessionBackingBean.getStationName());

		if (!isCorrectRouteForStation) {

			errorMsg = "Route not assigned for this station.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}
		
		
		try {
			checkCorrectBusOnTime("D", busRoute, busService);
			if(leaveBus) {
				errorMsg = " This bus is on leave today.";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
				return;
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		
		// check bus has arrived to Arival side it can be day +1
		
		String paramTimeForDepart = terminalManagementService
				.getParamTimeAdded("TERMINAL_BUS_AWAIT_TIME_FOR_DEPARTURE");
		boolean isBusCanDepart =terminalManagementService.getIsBusDepartCheck(busNo.trim().toUpperCase(), busRoute,
				busService, sessionBackingBean.getStationName(),"D",paramTimeForDepart);
     
		if (!isBusCanDepart) {
			
			errorMsg = " This bus is not arrived.";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			return;
		}
		 turnNoD =terminalManagementService.getNoOfTurnsForDepart(busNo.trim().toUpperCase(), busRoute,
					busService, sessionBackingBean.getStationName());
		// check permit number is active
		boolean isActivePermit = terminalManagementService.checkPermitStatusInActive(busNo.trim().toUpperCase());
		if (!isActivePermit) {
			validity= false;
			authorizeMsgs.add("Permit is not in active status.");
		}

		

	
		vehicleDTO = authorizeData("DEPARTURE");

		/*List<TerminalArrivalDepartureTimeDTO> scheduledTimeList = terminalManagementService
				.getScheduledDepartureForDay(vehicleDTO, dayOfWeek);*/
		List<TerminalArrivalDepartureTimeDTO> scheduledTimeList = new ArrayList<>();
		TerminalArrivalDepartureTimeDTO dto = new TerminalArrivalDepartureTimeDTO();
		
		
		
		if(!busInTime.isEmpty() && !busOutTime.isEmpty() ) {
			for(int i=0;i<busInTime.size();i++) {
			TerminalArrivalDepartureTimeDTO dto1 = new TerminalArrivalDepartureTimeDTO();
			dto1.setStartTime(busInTime.get(i));
			dto1.setEndTime(busOutTime.get(i));
			scheduledTimeList.add(dto1);
			}
		}
		vehicleDTO.setScheduledTimeListForDay(scheduledTimeList);
		vehicleDTO.setStationCode(sessionBackingBean.getStationName());
		boolean arrivalToday =terminalManagementService.checkEntererdBusArriveToday(busNo.trim().toUpperCase(), busRoute,
				busService, sessionBackingBean.getStationName());
		if(arrivalToday) {
		vehicleDTO=terminalManagementService.insertTerminalDepartureLog(vehicleDTO, dayOfWeek, user);
		}
		else {
			vehicleDTO=terminalManagementService.insertTerminalDepartureLogForYersterday(vehicleDTO, dayOfWeek, user);
		}
	
	
		// checked Suspend driver/condutor

		boolean isSuspendDriverConductor = terminalManagementService
				.checkDriverConductorSuspend(busNo.trim().toUpperCase());
		if (isSuspendDriverConductor) {
			validity= false;
			authorizeMsgs.add("Driver/Condutor blacklisted. ");
		}
		// check the payment has done for month
		boolean paymentDone = terminalManagementService.isPaymentDoneForCurrentMonth(busNo.trim().toUpperCase());
		if (!paymentDone) {
			validity= false;
			authorizeMsgs.add("Should complete the payment. ");
		}

		// match the expected departure with sorted schedule list //when ActualArrival
		// is null bus has not arrived
		for (TerminalArrivalDepartureTimeDTO schedule : nullSafe(vehicleDTO.getScheduledTimeListForDay())) {
			if (schedule.getActualArrivalDeparture() == null || schedule.getActualArrivalDeparture().isEmpty()) {
				String paramTime = terminalManagementService
						.getParamTimeAdded("BUS_ARRIVAL_DEPARTURE_TIME_FOR_TERMINAL");
				int paramTimeInt = Integer.parseInt(paramTime);
				estimatedDeparture = schedule.getStartTime();
				estimatedDepartureNew = schedule.getStartTime();
				tripEndTime = schedule.getEndTime();
				currentSchedule = schedule;
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				Date it = df.parse(estimatedDeparture);
				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(it);
				cal2.add(Calendar.MINUTE, (paramTimeInt * -1));

				DateFormat dateFormat = new SimpleDateFormat("HH:mm");
				String strDate = dateFormat.format(cal2.getTime());

				sheduleDepartureTime = strDate;
				
				Date it2 = df.parse(estimatedDeparture);
				Calendar cal3 = Calendar.getInstance();
				cal3.setTime(it2);
				cal3.add(Calendar.MINUTE, (paramTimeInt * 1));
				String strDate2 = dateFormat.format(cal3.getTime());
				estimatedDeparture=strDate2;
				break;
			}
		}

		// validate arrival with schedule, no of trips
		if (scheduledTimeList == null || scheduledTimeList.isEmpty()) {
			validity = false;
			authorizeMsgs.add("Bus is not scheduled for today");
		} else if (estimatedDeparture == null || estimatedDeparture.isEmpty()) {
			validity = false;
			authorizeMsgs.add("Exceeded the no of trips for the day");
		}else {
			// calculate departure time difference
			LocalTime departure = LocalTime.parse(estimatedDepartureNew);
			long hr = ChronoUnit.HOURS.between(nowTime, departure);
			long min = ChronoUnit.MINUTES.between(nowTime, departure);

			if (hr != 0)
				delayTimeDiff = Math.abs(hr) + " hr ";
			if (min != 0) {
				long m = Math.abs(min) - Math.abs(hr * 60);
				delayTimeDiff += m + " min";
			}
			if (hr > 0 || min > 0)
				delayTimeDiff += " early";
			else if (hr < 0 || min < 0)
				delayTimeDiff += " delay";

		}

	
		vehicleDTO.setTurnNo(turnNoD);
		terminalManagementService.beanLinkMethod("Add Terminal Departure Log", sessionBackingBean.getLoginUser(), "Departure Authorization", busNo, null, null);
		
	}

	public void allowToLeave() {
		String user = sessionBackingBean.loginUser;
		String errors = String.join(",", authorizeMsgs);
		String busRoute = terminalManagementService.getRouteAndServiceTypeByBusNo(busNo.trim().toUpperCase(), true);
		String busService = terminalManagementService.getRouteAndServiceTypeByBusNo(busNo.trim().toUpperCase(), false);
		long arrivalCurrentSeq =terminalManagementService.getarrivallatestSeq(busNo.trim().toUpperCase(),busRoute,busService,sessionBackingBean.getStationName());
		if (currentSchedule.getSequence() != null
				&& terminalManagementService.updateTerminalDepartureLog(arrivalCurrentSeq,currentSchedule.getSequence(),
						actualDepartureTime, turnNoD, validity ? "Y" : "N", errors, dayOfWeek, user)) {
			
			terminalManagementService.beanLinkMethod("Update Terminal Departure Log (Leave)", sessionBackingBean.getLoginUser(), "Departure Authorization", busNo, null, null);
			sucessMsg = "Departure Log Updated Successfuly!";
			RequestContext.getCurrentInstance().update("frmSuccess");
			RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
			resetView();
		} else {
			errorMsg = "Error!";
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		}
	}

	/*
	 * Common methods
	 */
	private int getDateDifference(Date fromDate, Date toDate) {
		LocalDate fromDateLocal = convertToLocalDateViaInstant(fromDate);
		LocalDate toDateLocal = convertToLocalDateViaInstant(toDate);
		long dateDiff = ChronoUnit.DAYS.between(fromDateLocal, toDateLocal);
		return (int) dateDiff;
	}

	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	/**
	 * check arrival time is correct when compare with timetable module
	 * 
	 * @throws ParseException
	 ***/

	public void checkInTimeIscorrectForCoupleOne(List<String> busTime, String ParamTime) {

		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime currentTime = LocalTime.now();
		String inTime = null;
		int index = 0;
		//String secondTime = busInTime.get(1);
		
		//LocalTime secondTImeL = LocalTime.parse(secondTime, format);
		

		//LocalTime paramPlusSecondTime = secondTImeL.plusMinutes(Long.parseLong(ParamTime));
		//LocalTime paramMinusSecondTime = secondTImeL.minusMinutes(Long.parseLong(ParamTime));
        
		for(int i = 0; i < busTime.size(); i++) {
			String firstTime = busTime.get(i);
			LocalTime firstTimeL = LocalTime.parse(firstTime, format);
			LocalTime paramPlusFirstTime = firstTimeL.plusMinutes(Long.parseLong(ParamTime));
			LocalTime paramMinusFirstTime = firstTimeL.minusMinutes(Long.parseLong(ParamTime));
			if (!(currentTime.isAfter(paramMinusFirstTime) && currentTime.isBefore(paramPlusFirstTime))) {
				if(!authorizeMsgs.contains("This bus is not in correct time slot.")) {
					validity = false;
					authorizeMsgs.add("This bus is not in correct time slot.");
				}	
			}else {
				validity = true;
				inTime = busTime.get(i);
				index = i;
				if(authorizeMsgs.contains("This bus is not in correct time slot.")) {
					authorizeMsgs.remove(authorizeMsgs.size() - 1);
				}
				
				break;
			}
		}
		
		if(inTime != null) {
			busInTime.clear();
			busInTime.add(inTime);
			
			String outTime = busOutTime.get(index);
			busOutTime.clear();
			busOutTime.add(outTime);
		}
		
		
	}

	public void checkInTimeIscorrectForCoupleTwo(List<String> busInTime, String ParamTime) {

		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
		String firstTime = busInTime.get(0);
		String secondTime = busInTime.get(1);
		//String thirdTImeIn = busInTime.get(2);
		//String fourthTImeIn = busInTime.get(3);
		LocalTime firstTimeL = LocalTime.parse(firstTime, format);
		LocalTime secondTImeL = LocalTime.parse(secondTime, format);
		///LocalTime thirdTImeL = LocalTime.parse(thirdTImeIn, format);
		//LocalTime fourthTImeL = LocalTime.parse(fourthTImeIn, format);

		LocalTime currentTime = LocalTime.now();
		LocalTime paramPlusFirstTime = firstTimeL.plusMinutes(Long.parseLong(ParamTime));
		LocalTime paramMinusFirstTime = firstTimeL.minusMinutes(Long.parseLong(ParamTime));

		LocalTime paramPlusSecondTime = secondTImeL.plusMinutes(Long.parseLong(ParamTime));
		LocalTime paramMinusSecondTime = secondTImeL.minusMinutes(Long.parseLong(ParamTime));

		//LocalTime paramPlusThirdTime = thirdTImeL.plusMinutes(Long.parseLong(ParamTime));
		//LocalTime paramMinusThirdTime = thirdTImeL.minusMinutes(Long.parseLong(ParamTime));

		//LocalTime paramPlusFourthTime = fourthTImeL.plusMinutes(Long.parseLong(ParamTime));
		//LocalTime paramMinusFourthTime = fourthTImeL.minusMinutes(Long.parseLong(ParamTime));

		if (!(currentTime.isAfter(paramMinusFirstTime) && currentTime.isBefore(paramPlusFirstTime))) {
			if (!(currentTime.isAfter(paramMinusSecondTime) && currentTime.isBefore(paramPlusSecondTime))) {
					validity = false;
						authorizeMsgs.add("This bus is not in correct time slot.");
				
			}
		}
	}
	

	/* Changed method scope on (03/05/2024) by dhananjika.d 
	 * This method will check the bus time slot with actual time */

	public boolean checkCorrectBusOnTime(String tripSide, String busRoute, String busService) throws ParseException {
		String terminalDateStr;
		String terminalTime;
		boolean correctTerminalDate = false;
		TerminalArrivalDepartureDTO TADto = new TerminalArrivalDepartureDTO();
		List<String> panelGeneratorNumList = new ArrayList<>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		terminalDateStr = formatter.format(date);
		busInTime.clear();
		busOutTime.clear();
		busInTimeFinal.clear();
		busOutTimeFinal.clear();
		SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
		Date time = new Date();
		terminalTime = formatter2.format(time);
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		String dayOfMonth = formatter.format(date).substring(0, 2);
		Date dateTime;
		if(tripSide.equals("O")) {
			dateTime = timeFormat.parse(actualArrivalTime);
		}else {
			dateTime = timeFormat.parse(actualDepartureTime);
		}
		
		panelGeneratorNumList = terminalManagementService.getActivePanelgeneratorNumber(busNo.trim().toUpperCase(), Integer.valueOf(dayOfMonth));  //Reference number
		
		for(String panelGeneratorNum : panelGeneratorNumList) {
			TADto = terminalManagementService.getPanelGeneratorDetails(panelGeneratorNum);
			
			if ((TADto.getStartDate() != null && !TADto.getStartDate().isEmpty()) || 
					(TADto.getEndDate() != null && !TADto.getEndDate().isEmpty())){
				Date startDateMonth = new SimpleDateFormat("dd/MM/yyyy").parse(TADto.getStartDate());
				Date endDateMonth = new SimpleDateFormat("dd/MM/yyyy").parse(TADto.getEndDate());
				Date terminalDate = new SimpleDateFormat("dd/MM/yyyy").parse(terminalDateStr);
				
				correctTerminalDate = startDateMonth.compareTo(terminalDate) * terminalDate.compareTo(endDateMonth) >= 0;
				
				if (correctTerminalDate) {
					long difference_In_Time = (terminalDate.getTime() - startDateMonth.getTime());
					long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
					int indays = (int) difference_In_Days + 1;
					
					List<String> busNumListForTheDay =terminalManagementService.getBusAbbriviationListForTheDay(panelGeneratorNum,indays,tripSide); 
					List<String> AllTimeslotList =terminalManagementService.getAlltimeSlotsForTheDay(busRoute, busService,panelGeneratorNum,tripSide, indays);
					List<String> AllTimeslotListForEndTime =terminalManagementService.getAllEndtimeSlotsForTheDay(busRoute, busService,panelGeneratorNum,tripSide, indays);
					
					Hashtable<String, String> busWithTime = new Hashtable<String, String>();
					for(int i = 0;i<busNumListForTheDay.size();i++) {
						busWithTime.put(AllTimeslotList.get(i), busNumListForTheDay.get(i));
					}
					
					if(busWithTime.containsValue(busNo.trim().toUpperCase())) {		
						String key= null;
				        String value=busNo.trim().toUpperCase();
				        
				        for(Map.Entry entry: busWithTime.entrySet()){
				            if(value.equals(entry.getValue())){
				                key = (String) entry.getKey();
				                busInTime.add(key);
				               
				            }
				        }
				        LinkedHashSet<String> hashSet = new LinkedHashSet<>(busInTime);				         
				        ArrayList<String> busInTime = new ArrayList<>(hashSet);				
					}
					
					Hashtable<String, String> busWithEndTime = new Hashtable<String, String>();
					for(int i = 0;i<busNumListForTheDay.size();i++) {
						busWithEndTime.put(AllTimeslotListForEndTime.get(i), busNumListForTheDay.get(i));
					}
						
					if(busWithEndTime.containsValue(busNo.trim().toUpperCase())) {
						
						String key= null;
				        String value=busNo.trim().toUpperCase();
				        
				        for(Map.Entry entry: busWithEndTime.entrySet()){
				            if(value.equals(entry.getValue())){
				                key = (String) entry.getKey();
				                busOutTime.add(key);
				               
				            }
				        }
				        LinkedHashSet<String> hashSet = new LinkedHashSet<>(busOutTime);				         
				        ArrayList<String> busOutTime = new ArrayList<>(hashSet);	
					}
					
					String paramTime = terminalManagementService
							.getParamTimeAdded("BUS_ARRIVAL_DEPARTURE_TIME_FOR_TERMINAL");
					
					boolean found = checkTimeFound(busInTime, paramTime);
					
					
				}else {
					validity= false;
					authorizeMsgs.add("Can not enter this bus today");
				}
			}else {
				errorMsg = "Error!";
				RequestContext.getCurrentInstance().update("frmError");
				RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
			}
		}
			
		for(String inTime : busInTime) {
			if(!busInTimeFinal.contains(inTime)) {
				busInTimeFinal.add(inTime);
			}
		}
		
		for(String outTime : busOutTime) {
			if(!busOutTimeFinal.contains(outTime)) {
				busOutTimeFinal.add(outTime);
			}
		}
		
		busInTime.clear();
		busOutTime.clear();
		
		busInTime.addAll(busInTimeFinal);
		busOutTime.addAll(busOutTimeFinal);
		
		return correctTerminalDate;
	}
	
	public boolean checkTimeFound(List<String> busTime, String ParamTime) {
		boolean found = false;
		DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime currentTime = LocalTime.now();
		
		for(int i = 0; i < busTime.size(); i++) {
			String firstTime = busTime.get(i);
			LocalTime firstTimeL = LocalTime.parse(firstTime, format);
			LocalTime paramPlusFirstTime = firstTimeL.plusMinutes(Long.parseLong(ParamTime));
			LocalTime paramMinusFirstTime = firstTimeL.minusMinutes(Long.parseLong(ParamTime));
			if (!(currentTime.isAfter(paramMinusFirstTime) && currentTime.isBefore(paramPlusFirstTime))) {
				if(!authorizeMsgs.contains("This bus is not in correct time slot.")) {
					validity = false;
					authorizeMsgs.add("This bus is not in correct time slot.");
				}	
			}
		}
	
		return found;
	}

    
	
	
	

	
//	public boolean checkCorrectBusOnTime(String tripSide, String busRoute, String busService) throws ParseException {
//		String terminalDateStr;
//		String terminalTime;
//		boolean correctTerminalDate = false;
//		TerminalArrivalDepartureDTO TADto = new TerminalArrivalDepartureDTO();
//		String panelGeneratorNum = null;
//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//		Date date = new Date();
//		terminalDateStr = formatter.format(date);
//		busInTime.clear();
//		busOutTime.clear();
//		SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
//		Date time = new Date();
//		terminalTime = formatter2.format(time);
//		
//		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//		Date dateTime;
//		if(tripSide.equals("O")) {
//			dateTime = timeFormat.parse(actualArrivalTime);
//		}else {
//			dateTime = timeFormat.parse(actualDepartureTime);
//		}
//		
//		Calendar calendar = Calendar.getInstance();
//        calendar.setTime(dateTime);   
//        calendar.add(Calendar.MINUTE, -22);
//        Date modifiedTime = calendar.getTime();
//        String modifiedStartTimeString = formatter2.format(modifiedTime);
//        
//        calendar.add(Calendar.MINUTE, 44); // Adding 5 minutes twice since we already subtracted 5 minutes
//        modifiedTime = calendar.getTime();
//        String modifiedEndTimeString = formatter2.format(modifiedTime);
//        
//        String startHour = modifiedStartTimeString.substring(0, 2);
//        String startMin = modifiedStartTimeString.substring(3, 5);
//        
//        String endHour = modifiedEndTimeString.substring(0, 2);
//        String endMin = modifiedEndTimeString.substring(3, 5);
//        
//        String dayOfMonth = formatter.format(date).substring(0, 2);
//
//		panelGeneratorNum = terminalManagementService.getActivePanelgeneratorNumber(busNo.trim().toUpperCase(), startHour, endHour, startMin, endMin, Integer.valueOf(dayOfMonth));  //Reference number
//		TADto = terminalManagementService.getPanelGeneratorDetails(panelGeneratorNum); 		//Reference number, start date, end date, no of days
//		
//
//		if (TADto.getPanelGenNumber() != null && !TADto.getPanelGenNumber().isEmpty()) {
//			Date startDateMonth = new SimpleDateFormat("dd/MM/yyyy").parse(TADto.getStartDate());
//			Date endDateMonth = new SimpleDateFormat("dd/MM/yyyy").parse(TADto.getEndDate());
//			Date terminalDate = new SimpleDateFormat("dd/MM/yyyy").parse(terminalDateStr);
//
//			/***
//			 * check whether terminal date is in between or not in route schedule generator
//			 * dates
//			 ***/
//			correctTerminalDate = startDateMonth.compareTo(terminalDate) * terminalDate.compareTo(endDateMonth) >= 0;
//			if (correctTerminalDate) {
//				/*** check whether entered bus leave or not in today ***/
//				// have to check start date and current date is same ?
//				long difference_In_Time = (terminalDate.getTime() - startDateMonth.getTime());
//
//				long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
//				int indays = (int) difference_In_Days + 1;
//				 leaveBus = terminalManagementService.checkEnteredBusLeave(busNo.trim().toUpperCase(), tripSide,
//						TADto.getPanelGenNumber(), indays);				// check leave bus
//
//				if (!leaveBus) {
//					/*** check whether entered bus is in or not in correct time ***/
//					/*List<String> busInTime = terminalManagementService.checkEnteredBusForInOut(busNo.trim().toUpperCase(), tripSide,
//							TADto.getPanelGenNumber(), indays);*/
//					//getting correct start time and end time slots 
//					String busAbbrForSelectedBusNo =terminalManagementService.getBusAbbrivationForEnteredBus(busNo.trim().toUpperCase(),TADto.getPanelGenNumber());
//					if(busAbbrForSelectedBusNo==null) {
//						wrongAbrriviation = true;
//						
//					}
//					if(!wrongAbrriviation) {
//						// get the bus numbers for day
//						List<String> busNumListForTheDay =terminalManagementService.getBusAbbriviationListForTheDay(busNo.trim().toUpperCase(),indays,tripSide); 		
//					
//						// get the leave bus numbers for day
////					List<String>LeaveBusNumListForTheDay= terminalManagementService.getLeaveBusAbbriviationListForTheDay(TADto.getPanelGenNumber(),indays);
//					
//						// get the all start timeslots without SLTB & ETC
//					List<String>AllTimeslotList =terminalManagementService.getAlltimeSlotsForTheDay(busRoute, busService,busNo.trim().toUpperCase(),tripSide, indays);
//						
//					// get the all end timeslots without SLTB & ETC
//					List<String>AllTimeslotListForEndTime =terminalManagementService.getAllEndtimeSlotsForTheDay(busRoute, busService,busNo.trim().toUpperCase(),tripSide, indays);
//					//remove leave bus abbrivations
////					if(!LeaveBusNumListForTheDay.isEmpty() && LeaveBusNumListForTheDay!=null) {
////						for(String l :LeaveBusNumListForTheDay) {
////							while(busNumListForTheDay.remove(l)){};
////						}
////						
////					}
//				
//					// for start time slots
//					Hashtable<String, String> busWithTime = new Hashtable<String, String>();
//					for(int i = 0;i<busNumListForTheDay.size();i++) {
//						busWithTime.put(AllTimeslotList.get(i), busNumListForTheDay.get(i));
//					}
//					
//					
//					if(busWithTime.containsValue(busNo.trim().toUpperCase())) {
//						
//						String key= null;
//				        String value=busNo.trim().toUpperCase();
//				        
//				        for(Map.Entry entry: busWithTime.entrySet()){
//				            if(value.equals(entry.getValue())){
//				                key = (String) entry.getKey();
//				                busInTime.add(key);
//				               
//				            }
//				        }
////				        Collections.sort(busInTime);
//				        LinkedHashSet<String> hashSet = new LinkedHashSet<>(busInTime);
//				         
//				        ArrayList<String> busInTime = new ArrayList<>(hashSet);
//				
//					}
//					
//					// for end time slots
//					Hashtable<String, String> busWithEndTime = new Hashtable<String, String>();
//					for(int i = 0;i<busNumListForTheDay.size();i++) {
//						busWithEndTime.put(AllTimeslotListForEndTime.get(i), busNumListForTheDay.get(i));
//					}
//					
//					
//					if(busWithEndTime.containsValue(busNo.trim().toUpperCase())) {
//						
//						String key= null;
//				        String value=busNo.trim().toUpperCase();
//				        
//				        for(Map.Entry entry: busWithEndTime.entrySet()){
//				            if(value.equals(entry.getValue())){
//				                key = (String) entry.getKey();
//				                busOutTime.add(key);
//				               
//				            }
//				        }
////				        Collections.sort(busOutTime);
//				        LinkedHashSet<String> hashSet = new LinkedHashSet<>(busOutTime);
//				         
//				        ArrayList<String> busOutTime = new ArrayList<>(hashSet);
//				
//					}
//					
//					String paramTime = terminalManagementService
//							.getParamTimeAdded("BUS_ARRIVAL_DEPARTURE_TIME_FOR_TERMINAL");
//					
//					checkInTimeIscorrectForCoupleOne(busInTime, paramTime);
//
//				}
//				} else {
//
//					/*** can not enter to terminal bus is leave for today ***/
//					
//					return  leaveBus;
//				}
//
//			} else {
//				validity= false;
//				authorizeMsgs.add("Can not enter this bus today");
//
//			}
//
//		}
//		return correctTerminalDate;
//
//	}

	/** end check arrival time is correct when compare with timetable module ***/

	//////////////// getter & setters /////////////////////

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public TerminalManagementService getTerminalManagementService() {
		return terminalManagementService;
	}

	public void setTerminalManagementService(TerminalManagementService terminalManagementService) {
		this.terminalManagementService = terminalManagementService;
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

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

	public boolean isShowDetail() {
		return showDetail;
	}

	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	public boolean isValidity() {
		return validity;
	}

	public void setValidity(boolean validity) {
		this.validity = validity;
	}

	public List<String> getAuthorizeMsgs() {
		return authorizeMsgs;
	}

	public void setAuthorizeMsgs(List<String> authorizeMsgs) {
		this.authorizeMsgs = authorizeMsgs;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getEstimatedArrival() {
		return estimatedArrival;
	}

	public void setEstimatedArrival(String estimatedArrival) {
		this.estimatedArrival = estimatedArrival;
	}

	public String getEstimatedDeparture() {
		return estimatedDeparture;
	}

	public void setEstimatedDeparture(String estimatedDeparture) {
		this.estimatedDeparture = estimatedDeparture;
	}

	public String getArrivingStation() {
		return arrivingStation;
	}

	public void setArrivingStation(String arrivingStation) {
		this.arrivingStation = arrivingStation;
	}

	public String getArrivingTerminal() {
		return arrivingTerminal;
	}

	public void setArrivingTerminal(String arrivingTerminal) {
		this.arrivingTerminal = arrivingTerminal;
	}

	public String getArrivingBlock() {
		return arrivingBlock;
	}

	public void setArrivingBlock(String arrivingBlock) {
		this.arrivingBlock = arrivingBlock;
	}

	public String getActualArrivalTime() {
		return actualArrivalTime;
	}

	public void setActualArrivalTime(String actualArrivalTime) {
		this.actualArrivalTime = actualArrivalTime;
	}

	public String getArriveFromStation() {
		return arriveFromStation;
	}

	public void setArriveFromStation(String arriveFromStation) {
		this.arriveFromStation = arriveFromStation;
	}

	public String getArriveFromTerminal() {
		return arriveFromTerminal;
	}

	public void setArriveFromTerminal(String arriveFromTerminal) {
		this.arriveFromTerminal = arriveFromTerminal;
	}

	public String getActualDepartureTime() {
		return actualDepartureTime;
	}

	public void setActualDepartureTime(String actualDepartureTime) {
		this.actualDepartureTime = actualDepartureTime;
	}

	public String getDepartingStation() {
		return departingStation;
	}

	public void setDepartingStation(String departingStation) {
		this.departingStation = departingStation;
	}

	public String getDepartingTerminal() {
		return departingTerminal;
	}

	public void setDepartingTerminal(String departingTerminal) {
		this.departingTerminal = departingTerminal;
	}

	public String getDepartingBlock() {
		return departingBlock;
	}

	public void setDepartingBlock(String departingBlock) {
		this.departingBlock = departingBlock;
	}

	public String getDepartForStation() {
		return departForStation;
	}

	public void setDepartForStation(String departForStation) {
		this.departForStation = departForStation;
	}

	public String getDepartForTerminal() {
		return departForTerminal;
	}

	public void setDepartForTerminal(String departForTerminal) {
		this.departForTerminal = departForTerminal;
	}

	public String getDelayTimeDiff() {
		return delayTimeDiff;
	}

	public void setDelayTimeDiff(String delayTimeDiff) {
		this.delayTimeDiff = delayTimeDiff;
	}

	

	public String getPermitType() {
		return permitType;
	}

	public void setPermitType(String permitType) {
		this.permitType = permitType;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getDateOfIssue() {
		return dateOfIssue;
	}

	public void setDateOfIssue(String dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}

	public String getDateOfExpiry() {
		return dateOfExpiry;
	}

	public void setDateOfExpiry(String dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public String getTripStartDate() {
		return tripStartDate;
	}

	public void setTripStartDate(String tripStartDate) {
		this.tripStartDate = tripStartDate;
	}

	public String getTripStartTime() {
		return tripStartTime;
	}

	public void setTripStartTime(String tripStartTime) {
		this.tripStartTime = tripStartTime;
	}

	public String getTripEndDate() {
		return tripEndDate;
	}

	public void setTripEndDate(String tripEndDate) {
		this.tripEndDate = tripEndDate;
	}

	public String getTripEndTime() {
		return tripEndTime;
	}

	public void setTripEndTime(String tripEndTime) {
		this.tripEndTime = tripEndTime;
	}

	public String getStationDes() {
		return stationDes;
	}

	public void setStationDes(String stationDes) {
		this.stationDes = stationDes;
	}

	public boolean isLocalcheckcounter() {
		return localcheckcounter;
	}

	public void setLocalcheckcounter(boolean localcheckcounter) {
		this.localcheckcounter = localcheckcounter;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public boolean isShowStationPopup() {
		return showStationPopup;
	}

	public void setShowStationPopup(boolean showStationPopup) {
		this.showStationPopup = showStationPopup;
	}

	public String getScheduleArrivalTime() {
		return scheduleArrivalTime;
	}

	public void setScheduleArrivalTime(String scheduleArrivalTime) {
		this.scheduleArrivalTime = scheduleArrivalTime;
	}

	public String getRouteDestination() {
		return routeDestination;
	}

	public void setRouteDestination(String routeDestination) {
		this.routeDestination = routeDestination;
	}

	public int getTurnNoA() {
		return turnNoA;
	}

	public void setTurnNoA(int turnNoA) {
		this.turnNoA = turnNoA;
	}

	public int getTurnNoD() {
		return turnNoD;
	}

	public void setTurnNoD(int turnNoD) {
		this.turnNoD = turnNoD;
	}

	public String getSheduleDepartureTime() {
		return sheduleDepartureTime;
	}

	public void setSheduleDepartureTime(String sheduleDepartureTime) {
		this.sheduleDepartureTime = sheduleDepartureTime;
	}

	public String getDateFormatStr() {
		return dateFormatStr;
	}

	public void setDateFormatStr(String dateFormatStr) {
		this.dateFormatStr = dateFormatStr;
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

	public DateTimeFormatter getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(DateTimeFormatter timeFormat) {
		this.timeFormat = timeFormat;
	}

	public TerminalArrivalDepartureDTO getVehicleDTO() {
		return vehicleDTO;
	}

	public void setVehicleDTO(TerminalArrivalDepartureDTO vehicleDTO) {
		this.vehicleDTO = vehicleDTO;
	}

	public TerminalArrivalDepartureTimeDTO getCurrentSchedule() {
		return currentSchedule;
	}

	public void setCurrentSchedule(TerminalArrivalDepartureTimeDTO currentSchedule) {
		this.currentSchedule = currentSchedule;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public boolean isLeaveBus() {
		return leaveBus;
	}

	public void setLeaveBus(boolean leaveBus) {
		this.leaveBus = leaveBus;
	}

	public List<String> getBusInTime() {
		return busInTime;
	}

	public void setBusInTime(List<String> busInTime) {
		this.busInTime = busInTime;
	}

	public List<String> getBusOutTime() {
		return busOutTime;
	}

	public void setBusOutTime(List<String> busOutTime) {
		this.busOutTime = busOutTime;
	}

	public boolean isWrongAbrriviation() {
		return wrongAbrriviation;
	}

	public void setWrongAbrriviation(boolean wrongAbrriviation) {
		this.wrongAbrriviation = wrongAbrriviation;
	}

	public List<String> getBusInTimeFinal() {
		return busInTimeFinal;
	}

	public void setBusInTimeFinal(List<String> busInTimeFinal) {
		this.busInTimeFinal = busInTimeFinal;
	}

	public List<String> getBusOutTimeFinal() {
		return busOutTimeFinal;
	}

	public void setBusOutTimeFinal(List<String> busOutTimeFinal) {
		this.busOutTimeFinal = busOutTimeFinal;
	}
	
	
}
