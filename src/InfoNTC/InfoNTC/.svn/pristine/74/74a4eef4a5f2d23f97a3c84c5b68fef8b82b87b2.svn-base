package lk.informatics.ntc.view.beans;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.RouteScheduleService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "panelWithFixedTimeEditViewBackingBean")
@ViewScoped
public class PanelGeneratorWithFixedTimeEditBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services

	private TimeTableService panelGenWithFixedTimeService;
	private CommonService commonService;

	// DTO
	private TimeTableDTO panelGenFixedTimeDTO = new TimeTableDTO();
	private TimeTableDTO travleTimeDTO = new TimeTableDTO();
	private TimeTableDTO busCategoryVal = new TimeTableDTO();
	private TimeTableDTO selectedRow = new TimeTableDTO();
	private TimeTableDTO refNoDTO = new TimeTableDTO();
	private TimeTableDTO abbriDTO = new TimeTableDTO();
	private TimeTableDTO routeDet = new TimeTableDTO();
	private ArrayList<String> groupAndTripTypeDTO = new ArrayList<>();
	private TimeTableDTO nonPrivateBusDTO = new TimeTableDTO();
	// list
	private List<TimeTableDTO> routeNoList = new ArrayList<>();
	private List<TimeTableDTO> busCategoryList = new ArrayList<>();
	private List<TimeTableDTO> showTimeSlotDet1 = new ArrayList<>();
	private List<TimeTableDTO> showTimeSlotDet2 = new ArrayList<>();
	private List<TimeTableDTO> showTimeSlotDet3 = new ArrayList<>();
	private List<TimeTableDTO> timeStartList = new ArrayList<>();
	private List<TimeTableDTO> timeStartList2 = new ArrayList<>();
	private List<TimeTableDTO> timeStartList3 = new ArrayList<>();
	private List<TimeTableDTO> timeStartListSecond = new ArrayList<>();
	private List<TimeTableDTO> timeStartList2Second = new ArrayList<>();
	private List<TimeTableDTO> timeStartList3Second = new ArrayList<>();

	private List<TimeTableDTO> showInsertedDataG1O = new ArrayList<>();
	private List<TimeTableDTO> showInsertedDataG2O = new ArrayList<>();
	private List<TimeTableDTO> showInsertedDataG3O = new ArrayList<>();

	private List<TimeTableDTO> showInsertedDataG1D = new ArrayList<>();
	private List<TimeTableDTO> showInsertedDataG2D = new ArrayList<>();
	private List<TimeTableDTO> showInsertedDataG3D = new ArrayList<>();

	private List<TimeTableDTO> busTypeList = new ArrayList<>();
	private List<TimeTableDTO> busTypeSetList = new ArrayList<>();
	private List<TimeTableDTO> busTypeSetList2 = new ArrayList<>();
	private List<TimeTableDTO> busTypeSetList3 = new ArrayList<>();
	private List<TimeTableDTO> busCategoryListMain = new ArrayList<>();
	private boolean busTypeDisable1, busTypeDisable2, busTypeDisable3, busTypeDisableD1;
	private int noOfBus;
	private String errorMsg, loginUser, sucessMsg;
	String restTime = "00:00";
	String afterAddStr = null;
	String afterAddStrCor = null;
	long afterAdd;
	String TimeStartVal = null;
	int j, k, l, jD, kD, lD = 0;
	int noOFTripsValGOne;
	int noOFTripsValGTwo;
	int noOFTripsValGThree;
	private boolean bool;

	private List<TimeTableDTO> busCatList = new ArrayList<>();
	private List<TimeTableDTO> busCatList2 = new ArrayList<>();
	private List<TimeTableDTO> busCatList3 = new ArrayList<>();
	private List<TimeTableDTO> busCatListDtoO = new ArrayList<>();
	private List<TimeTableDTO> busCatListDtoO2 = new ArrayList<>();
	private List<TimeTableDTO> busCatListDtoO3 = new ArrayList<>();

	private boolean saveG1OrtoDe;
	private boolean saveG2OrtoDe;
	private boolean saveG3OrtoDe;
	private boolean saveG1DetoOr;
	private boolean saveG2DetoOr;
	private boolean saveG3DetoOr;

	private boolean checkDataAvailable = false;
	private boolean checkDataEditable = false;
	private int tripsG1O, tripsG2O, tripsG3O, tripsG1D, tripsG2D, tripsG3D;
	private RouteScheduleService routeScheduleService;

	private TimeTableDTO nonSltbEtcBusDTO = new TimeTableDTO();

	@PostConstruct
	public void init() {
		panelGenWithFixedTimeService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		routeScheduleService = (RouteScheduleService) SpringApplicationContex.getBean("routeScheduleService");
		panelGenFixedTimeDTO = new TimeTableDTO();
		routeNoList = panelGenWithFixedTimeService.getRouteNoListFromTripsGen();
		loginUser = sessionBackingBean.getLoginUser();

		busTypeDisable1 = false;
		busTypeDisable2 = false;
		busTypeDisable3 = false;
		busTypeDisableD1 = false;

		saveG1OrtoDe = false;
		saveG2OrtoDe = false;
		saveG3OrtoDe = false;
		saveG1DetoOr = false;
		saveG2DetoOr = false;
		saveG3DetoOr = false;

	}

	public void getBusCategory() {

		busCategoryVal = panelGenWithFixedTimeService.getBusCategory(panelGenFixedTimeDTO.getRouteNo());
		busCategoryListMain = panelGenWithFixedTimeService.getBusCategoryList(panelGenFixedTimeDTO.getRouteNo());

	}

	public void getrouteOrDes() {

		routeDet = panelGenWithFixedTimeService.getRouteData(panelGenFixedTimeDTO.getRouteNo());
		panelGenFixedTimeDTO.setOrigin(routeDet.getOrigin());
		panelGenFixedTimeDTO.setDestination(routeDet.getDestination());
	}

	public void Search() throws ParseException {
		String time = null;
		String refNo = null;

		if (panelGenFixedTimeDTO.getRouteNo() != null && !panelGenFixedTimeDTO.getRouteNo().trim().isEmpty()) {
			if (panelGenFixedTimeDTO.getBusCategory() != null
					&& !panelGenFixedTimeDTO.getBusCategory().trim().isEmpty()) {
				// get refNo.
				refNo = panelGenWithFixedTimeService.getPanelGenNo(panelGenFixedTimeDTO.getRouteNo(),
						panelGenFixedTimeDTO.getBusCategory());
				panelGenFixedTimeDTO.setPanelGenNo(refNo);
				// show already saved data
				checkDataAvailable = panelGenWithFixedTimeService
						.dataAvailableForShow(panelGenFixedTimeDTO.getPanelGenNo());

				refNoDTO = panelGenWithFixedTimeService.getReffrenceNo(panelGenFixedTimeDTO.getRouteNo(),
						panelGenFixedTimeDTO.getBusCategory());
				panelGenFixedTimeDTO.setTripRefNo(refNoDTO.getTripRefNo());
				panelGenFixedTimeDTO.setPanelGenNo(refNoDTO.getPanelGenNo());

				abbriDTO = panelGenWithFixedTimeService.getAbriviatiosForRoute(panelGenFixedTimeDTO.getRouteNo(),
						panelGenFixedTimeDTO.getBusCategory());
				panelGenFixedTimeDTO.setBusCategoryDes(busCategoryVal.getBusCategoryDes());
				panelGenFixedTimeDTO.setBusCategory(busCategoryVal.getBusCategory());

				panelGenFixedTimeDTO.setAbbriAtOrigin(abbriDTO.getAbbriAtOrigin());
				panelGenFixedTimeDTO.setAbbriAtDestination(abbriDTO.getAbbriAtDestination());

				if (checkDataAvailable) {

					boolean notAssigned = checkBusesAssigned();
					if (!notAssigned) {
						sessionBackingBean.setMessage("Searched data is not eligible. Buses are already assigned");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
						return;
					}

					showInsertedDataG1O = panelGenWithFixedTimeService.getInsertedData("1", "O",
							panelGenFixedTimeDTO.getPanelGenNo());
					showInsertedDataG2O = panelGenWithFixedTimeService.getInsertedData("2", "O",
							panelGenFixedTimeDTO.getPanelGenNo());
					showInsertedDataG3O = panelGenWithFixedTimeService.getInsertedData("3", "O",
							panelGenFixedTimeDTO.getPanelGenNo());
					showInsertedDataG1D = panelGenWithFixedTimeService.getInsertedData("1", "D",
							panelGenFixedTimeDTO.getPanelGenNo());
					showInsertedDataG2D = panelGenWithFixedTimeService.getInsertedData("2", "D",
							panelGenFixedTimeDTO.getPanelGenNo());
					showInsertedDataG3D = panelGenWithFixedTimeService.getInsertedData("3", "D",
							panelGenFixedTimeDTO.getPanelGenNo());

					if (showInsertedDataG1O != null) {
						// update no of trips available
						int noOfTripsVal = panelGenWithFixedTimeService
								.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "1", "O");
						tripsG1O = noOfTripsVal;
						for (TimeTableDTO tt : showInsertedDataG1O) {
							timeStartList.add(tt);
						}

					}
					if (showInsertedDataG2O != null) {
						// update no of trips available
						int noOfTripsVal = panelGenWithFixedTimeService
								.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "2", "O");
						tripsG2O = noOfTripsVal;
						for (TimeTableDTO tt : showInsertedDataG2O)
							timeStartList2.add(tt);
					}
					if (showInsertedDataG3O != null) {
						// update no of trips available
						int noOfTripsVal = panelGenWithFixedTimeService
								.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "3", "O");
						tripsG3O = noOfTripsVal;
						for (TimeTableDTO tt : showInsertedDataG3O)
							timeStartList3.add(tt);
					}
					if (showInsertedDataG1D != null) {
						// update no of trips available
						int noOfTripsVal = panelGenWithFixedTimeService
								.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "1", "D");
						tripsG1D = noOfTripsVal;
						for (TimeTableDTO tt : showInsertedDataG1D)
							timeStartListSecond.add(tt);
					}
					if (showInsertedDataG2D != null) {
						// update no of trips available
						int noOfTripsVal = panelGenWithFixedTimeService
								.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "2", "D");
						tripsG2D = noOfTripsVal;
						for (TimeTableDTO tt : showInsertedDataG2D)
							timeStartList2Second.add(tt);
					}
					if (showInsertedDataG3D != null) {
						// update no of trips available
						int noOfTripsVal = panelGenWithFixedTimeService
								.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "3", "D");
						tripsG3D = noOfTripsVal;
						for (TimeTableDTO tt : showInsertedDataG3D)
							timeStartList3Second.add(tt);
					}
				} else {
					setErrorMsg("No data for search.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

				groupAndTripTypeDTO = panelGenWithFixedTimeService.isGroup(panelGenFixedTimeDTO.getTripRefNo(), "O");

				if (groupAndTripTypeDTO.contains("1")) {

					panelGenFixedTimeDTO.setGroupOne("Y");
				}

				if (groupAndTripTypeDTO.contains("2")) {
					panelGenFixedTimeDTO.setGroupTwo("Y");

				}
				if (groupAndTripTypeDTO.contains("3")) {
					panelGenFixedTimeDTO.setGroupThree("Y");

				}

				if (panelGenFixedTimeDTO.getGroupOne() == null) {

					panelGenFixedTimeDTO.setGroupOne("N");
				}

				if (panelGenFixedTimeDTO.getGroupTwo() == null) {

					panelGenFixedTimeDTO.setGroupTwo("N");
				}

				if (panelGenFixedTimeDTO.getGroupThree() == null) {

					panelGenFixedTimeDTO.setGroupThree("N");
				}

				if (panelGenFixedTimeDTO.getGroupOne().equalsIgnoreCase("Y")) {

					showTimeSlotDet1 = panelGenWithFixedTimeService
							.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "1", "O");

					TimeTableDTO detDTO1 = new TimeTableDTO();

					for (TimeTableDTO TTDto : showTimeSlotDet1) {

						panelGenFixedTimeDTO.setNoOfBuses(TTDto.getNoOfBuses());
						int noOfBus = TTDto.getNoOfBuses();

						String busAbbAtOrgin = panelGenFixedTimeDTO.getAbbriAtOrigin();

						for (int i = 0; i < noOfBus; i++) {

							j = j + 1;
							detDTO1 = new TimeTableDTO();
							String busName = busAbbAtOrgin + String.valueOf(j);
							detDTO1.setBusType(busName);
							busCatList.add(detDTO1);

						}
					}

				}

				if (panelGenFixedTimeDTO.getGroupTwo().equalsIgnoreCase("Y")
						&& panelGenFixedTimeDTO.getGroupTwo() != null) {
					showTimeSlotDet2 = panelGenWithFixedTimeService
							.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "2", "O");
					TimeTableDTO detDTO1 = new TimeTableDTO();
					for (TimeTableDTO TTDto : showTimeSlotDet2) {

						panelGenFixedTimeDTO.setNoOfBuses(TTDto.getNoOfBuses());
						int noOfBus = TTDto.getNoOfBuses();
						String busAbbAtOrgin = panelGenFixedTimeDTO.getAbbriAtOrigin();

						for (int i = 0; i < noOfBus; i++) {

							k = k + 1;
							detDTO1 = new TimeTableDTO();
							String busName = busAbbAtOrgin + String.valueOf(k);
							detDTO1.setBusType(busName);
							busCatList2.add(detDTO1);

						}

					}
				}

				if (panelGenFixedTimeDTO.getGroupThree().equalsIgnoreCase("Y")
						&& panelGenFixedTimeDTO.getGroupThree() != null) {
					showTimeSlotDet3 = panelGenWithFixedTimeService
							.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "3", "O");
					TimeTableDTO detDTO1 = new TimeTableDTO();
					for (TimeTableDTO TTDto : showTimeSlotDet3) {

						panelGenFixedTimeDTO.setNoOfBuses(TTDto.getNoOfBuses());
						int noOfBus = TTDto.getNoOfBuses();
						String busAbbAtOrgin = panelGenFixedTimeDTO.getAbbriAtOrigin();

						for (int i = 0; i < noOfBus; i++) {

							l = l + 1;
							detDTO1 = new TimeTableDTO();
							String busName = busAbbAtOrgin + String.valueOf(l);
							detDTO1.setBusType(busName);
							busCatList3.add(detDTO1);

						}

					}
				}

				searchForDestinationToOrgin();

				// added by thilna.d on 30-11-2021
				// adding des abbriv to origin & origin abbriv to des
				List<TimeTableDTO> tempL1 = new ArrayList<>();
				tempL1.addAll(busCatList);
				tempL1.addAll(busCatListDtoO);
				List<TimeTableDTO> tempL1D = new ArrayList<>();
				tempL1D.addAll(busCatListDtoO);
				tempL1D.addAll(busCatList);
				busCatList = tempL1;
				busCatListDtoO = tempL1D;

				List<TimeTableDTO> tempL2 = new ArrayList<>();
				tempL2.addAll(busCatList2);
				tempL2.addAll(busCatListDtoO2);
				List<TimeTableDTO> tempL2D = new ArrayList<>();
				tempL2D.addAll(busCatList2);
				tempL2D.addAll(busCatListDtoO2);
				busCatList2 = tempL2;
				busCatListDtoO2 = tempL2D;

				List<TimeTableDTO> tempL3 = new ArrayList<>();
				tempL3.addAll(busCatList3);
				tempL3.addAll(busCatListDtoO3);
				List<TimeTableDTO> tempL3D = new ArrayList<>();
				tempL3D.addAll(busCatListDtoO3);
				tempL3D.addAll(busCatList3);
				busCatList3 = tempL3;
				busCatListDtoO3 = tempL3D;

			} else {
				setErrorMsg("Please select a bus category for search");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else {

			setErrorMsg("Please select a route no. for search");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void searchForDestinationToOrgin() throws ParseException {

		groupAndTripTypeDTO = panelGenWithFixedTimeService.isGroup(panelGenFixedTimeDTO.getTripRefNo(), "D");

		if (groupAndTripTypeDTO.contains("1")) {

			panelGenFixedTimeDTO.setDesToOrGroupOne("Y");
		}

		if (groupAndTripTypeDTO.contains("2")) {
			panelGenFixedTimeDTO.setDesToOrGroupTwo("Y");

		}
		if (groupAndTripTypeDTO.contains("3")) {
			panelGenFixedTimeDTO.setDesToOrGroupThree("Y");

		}

		if (panelGenFixedTimeDTO.getDesToOrGroupOne() == null) {

			panelGenFixedTimeDTO.setDesToOrGroupOne("N");
		}

		if (panelGenFixedTimeDTO.getDesToOrGroupTwo() == null) {

			panelGenFixedTimeDTO.setDesToOrGroupTwo("N");
		}

		if (panelGenFixedTimeDTO.getDesToOrGroupThree() == null) {

			panelGenFixedTimeDTO.setDesToOrGroupThree("N");
		}

		if (panelGenFixedTimeDTO.getDesToOrGroupOne().equalsIgnoreCase("Y")) {
			showTimeSlotDet1 = panelGenWithFixedTimeService
					.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "1", "D");
			TimeTableDTO detDTO1 = new TimeTableDTO();
			for (TimeTableDTO TTDto : showTimeSlotDet1) {

				panelGenFixedTimeDTO.setNoOfBuses(TTDto.getNoOfBuses());
				int noOfBus = TTDto.getNoOfBuses();
				String busAbbAtOrgin = panelGenFixedTimeDTO.getAbbriAtDestination();

				for (int i = 0; i < noOfBus; i++) {

					jD = jD + 1;
					detDTO1 = new TimeTableDTO();
					String busName = busAbbAtOrgin + String.valueOf(jD);
					detDTO1.setBusType(busName);
					busCatListDtoO.add(detDTO1);

				}

			}
		}

		if (panelGenFixedTimeDTO.getDesToOrGroupTwo().equalsIgnoreCase("Y")
				&& panelGenFixedTimeDTO.getDesToOrGroupTwo() != null) {
			showTimeSlotDet2 = panelGenWithFixedTimeService
					.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "2", "D");
			TimeTableDTO detDTO1 = new TimeTableDTO();
			for (TimeTableDTO TTDto : showTimeSlotDet2) {

				panelGenFixedTimeDTO.setNoOfBuses(TTDto.getNoOfBuses());
				int noOfBus = TTDto.getNoOfBuses();
				String busAbbAtOrgin = panelGenFixedTimeDTO.getAbbriAtDestination();

				for (int i = 0; i < noOfBus; i++) {

					kD = kD + 1;
					detDTO1 = new TimeTableDTO();
					String busName = busAbbAtOrgin + String.valueOf(kD);
					detDTO1.setBusType(busName);
					busCatListDtoO2.add(detDTO1);

				}
			}
		}

		if (panelGenFixedTimeDTO.getDesToOrGroupThree().equalsIgnoreCase("Y")
				&& panelGenFixedTimeDTO.getDesToOrGroupThree() != null) {
			showTimeSlotDet3 = panelGenWithFixedTimeService
					.showTimeSlotDetForGroups(panelGenFixedTimeDTO.getTripRefNo(), "3", "D");
			TimeTableDTO detDTO1 = new TimeTableDTO();
			for (TimeTableDTO TTDto : showTimeSlotDet3) {

				panelGenFixedTimeDTO.setNoOfBuses(TTDto.getNoOfBuses());
				int noOfBus = TTDto.getNoOfBuses();
				String busAbbAtOrgin = panelGenFixedTimeDTO.getAbbriAtDestination();

				for (int i = 0; i < noOfBus; i++) {

					lD = lD + 1;
					detDTO1 = new TimeTableDTO();
					String busName = busAbbAtOrgin + String.valueOf(lD);
					detDTO1.setBusType(busName);
					busCatListDtoO3.add(detDTO1);

				}

			}

		}

	}

	public void ClearButton() {
		panelGenFixedTimeDTO.setRouteNo(null);
		panelGenFixedTimeDTO.setBusCategory(null);
		panelGenFixedTimeDTO.setBusCategoryDes(null);

	}

	public void setBusType(TimeTableDTO dto1) {

		saveG1OrtoDe = true;

	}

	public void setBusType2(TimeTableDTO dto1) {

		saveG2OrtoDe = true;

	}

	public void setBusType3(TimeTableDTO dto1) {

		saveG3OrtoDe = true;

	}

	public void setBusTypeDtoO(TimeTableDTO dto1) {

		saveG1DetoOr = true;

	}

	public void setBusTypeDtoO2(TimeTableDTO dto1) {

		saveG2DetoOr = true;

	}

	public void setBusTypeDtoO3(TimeTableDTO dto1) {

		saveG3DetoOr = true;

	}

	public void saveRecords() {

		// save data in master table
		ArrayList<String> etcBusesList = new ArrayList<>();
		ArrayList<String> sLTBBusesList = new ArrayList<>();
		ArrayList<String> pvtBusesList = new ArrayList<>();

		boolean dataSet1 = false;
		boolean dataSet2 = false;
		boolean dataSet3 = false;
		boolean dataSet4 = false;
		boolean dataSet5 = false;
		boolean dataSet6 = false;

		panelGenFixedTimeDTO.getBusType();
		busCategoryList.add(panelGenFixedTimeDTO);
		String TimeTableRefNo = null;

		if (saveG1OrtoDe || saveG2OrtoDe || saveG3OrtoDe || saveG1DetoOr || saveG2DetoOr || saveG3DetoOr) {
			// check can edit or not
			checkDataEditable = panelGenWithFixedTimeService.availableForEdit(panelGenFixedTimeDTO.getPanelGenNo());
			if (checkDataEditable) {
				// noOFTripsValGOne = busTypeSetList.size();
				if (saveG1OrtoDe) {

					// save data in detail table
					nonPrivateBusDTO = panelGenWithFixedTimeService
							.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "1", "O");
					nonSltbEtcBusDTO = panelGenWithFixedTimeService
							.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(), "1");
					if (timeStartList != null) {
						dataSet1 = true;
						for (TimeTableDTO td : timeStartList) {
							if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
								if (td.getBusType().equals("ETC-O") || td.getBusType().equals("ETC-D")) {
									String etcBuses = td.getBusType();
									etcBusesList.add(etcBuses);

								} else if (td.getBusType().equals("SLTB-O") || td.getBusType().equals("SLTB-D")) {
									String sLTBBuses = td.getBusType();
									sLTBBusesList.add(sLTBBuses);
								} else {
									String pvtBBuses = td.getBusType();
									pvtBusesList.add(pvtBBuses);
								}
							}
						}

					}
				}

				if (saveG2OrtoDe) {

					etcBusesList = new ArrayList<>();
					sLTBBusesList = new ArrayList<>();
					pvtBusesList = new ArrayList<>();

					// save data in detail table
					if (timeStartList2 != null) {
						dataSet2 = true;
						nonPrivateBusDTO = panelGenWithFixedTimeService
								.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "2", "O");

						nonSltbEtcBusDTO = panelGenWithFixedTimeService
								.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(), "2");
						for (TimeTableDTO td : timeStartList2) {
							if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
								if (td.getBusType().equals("ETC-O") || td.getBusType().equals("ETC-D")) {
									String etcBuses = td.getBusType();
									etcBusesList.add(etcBuses);

								} else if (td.getBusType().equals("SLTB-O") || td.getBusType().equals("SLTB-D")) {
									String sLTBBuses = td.getBusType();
									sLTBBusesList.add(sLTBBuses);
								} else {
									String pvtBBuses = td.getBusType();
									pvtBusesList.add(pvtBBuses);
								}
							}
						}

					}
				}

				if (saveG3OrtoDe) {
					etcBusesList = new ArrayList<>();
					sLTBBusesList = new ArrayList<>();
					pvtBusesList = new ArrayList<>();

					// save data in detail table
					if (timeStartList3 != null) {
						dataSet3 = true;
						nonPrivateBusDTO = panelGenWithFixedTimeService
								.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "3", "O");
						nonSltbEtcBusDTO = panelGenWithFixedTimeService
								.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(), "3");

						for (TimeTableDTO td : timeStartList3) {
							if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
								if (td.getBusType().equals("ETC-O") || td.getBusType().equals("ETC-D")) {
									String etcBuses = td.getBusType();
									etcBusesList.add(etcBuses);

								} else if (td.getBusType().equals("SLTB-O") || td.getBusType().equals("SLTB-D")) {
									String sLTBBuses = td.getBusType();
									sLTBBusesList.add(sLTBBuses);
								} else {
									String pvtBBuses = td.getBusType();
									pvtBusesList.add(pvtBBuses);
								}
							}
						}

					}
				}

				if (saveG1DetoOr) {
					etcBusesList = new ArrayList<>();
					sLTBBusesList = new ArrayList<>();
					pvtBusesList = new ArrayList<>();
					// save data in detail table
					if (timeStartListSecond != null) {
						dataSet4 = true;
						nonPrivateBusDTO = panelGenWithFixedTimeService
								.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "1", "D");
						nonSltbEtcBusDTO = panelGenWithFixedTimeService
								.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(), "1");
						for (TimeTableDTO td : timeStartListSecond) {
							if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
								if (td.getBusType().equals("ETC-D") || td.getBusType().equals("ETC-O")) {
									String etcBuses = td.getBusType();
									etcBusesList.add(etcBuses);

								} else if (td.getBusType().equals("SLTB-D") || td.getBusType().equals("SLTB-O")) {
									String sLTBBuses = td.getBusType();
									sLTBBusesList.add(sLTBBuses);
								} else {
									String pvtBBuses = td.getBusType();
									pvtBusesList.add(pvtBBuses);
								}
							}
						}

					}
				}

				if (saveG2DetoOr) {
					etcBusesList = new ArrayList<>();
					sLTBBusesList = new ArrayList<>();
					pvtBusesList = new ArrayList<>();

					// save data in detail table
					if (timeStartList2Second != null) {
						dataSet5 = true;
						nonPrivateBusDTO = panelGenWithFixedTimeService
								.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "2", "D");
						nonSltbEtcBusDTO = panelGenWithFixedTimeService
								.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(), "2");
						for (TimeTableDTO td : timeStartList2Second) {
							if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
								if (td.getBusType().equals("ETC-D") || td.getBusType().equals("ETC-O")) {
									String etcBuses = td.getBusType();
									etcBusesList.add(etcBuses);

								} else if (td.getBusType().equals("SLTB-D") || td.getBusType().equals("SLTB-O")) {
									String sLTBBuses = td.getBusType();
									sLTBBusesList.add(sLTBBuses);
								} else {
									String pvtBBuses = td.getBusType();
									pvtBusesList.add(pvtBBuses);
								}
							}
						}
						
					}
				}

				if (saveG3DetoOr) {
					etcBusesList = new ArrayList<>();
					sLTBBusesList = new ArrayList<>();
					pvtBusesList = new ArrayList<>();
					// save data in detail table
					if (timeStartList3Second != null) {
						dataSet6 = true;
						nonPrivateBusDTO = panelGenWithFixedTimeService
								.getNoOfNonPrivateBusses(panelGenFixedTimeDTO.getTripRefNo(), "3", "D");
						nonSltbEtcBusDTO = panelGenWithFixedTimeService
								.getNoOfSltbEtcBusses(panelGenFixedTimeDTO.getTripRefNo(), "3");

						for (TimeTableDTO td : timeStartList3Second) {
							if (td.getBusType() != null && !td.getBusType().trim().isEmpty()) {
								if (td.getBusType().equals("ETC-D") || td.getBusType().equals("ETC-O")) {
									String etcBuses = td.getBusType();
									etcBusesList.add(etcBuses);

								} else if (td.getBusType().equals("SLTB-D") || td.getBusType().equals("SLTB-O")) {
									String sLTBBuses = td.getBusType();
									sLTBBusesList.add(sLTBBuses);
								} else {
									String pvtBBuses = td.getBusType();
									pvtBusesList.add(pvtBBuses);
								}
							}
						}
						
					}
				}

				if (dataSet1 || dataSet2 || dataSet3 || dataSet4 || dataSet5 || dataSet6) {

					if (dataSet1 || dataSet4) {
						
						panelGenWithFixedTimeService.updatePanelGeneratorWithFixedTimeAllInOne(panelGenFixedTimeDTO.getPanelGenNo(), "1", loginUser,
								timeStartList, timeStartListSecond);

						noOfBus = panelGenFixedTimeDTO.getNoOfBuses();
						
						tripsG1O = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "1", "O");
						tripsG1D = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "1", "D");

					}

					if (dataSet2 || dataSet5) {
						
						panelGenWithFixedTimeService.updatePanelGeneratorWithFixedTimeAllInOne(panelGenFixedTimeDTO.getPanelGenNo(), "2", loginUser,
								timeStartList2, timeStartList2Second);
						
						noOfBus = panelGenFixedTimeDTO.getNoOfBuses();

						tripsG2O = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "2", "O");
						tripsG2D = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "2", "D");
						
					}
					if (dataSet3 || dataSet6) {
						
						panelGenWithFixedTimeService.updatePanelGeneratorWithFixedTimeAllInOne(panelGenFixedTimeDTO.getPanelGenNo(), "3", loginUser,
								timeStartList3, timeStartList3Second);
						
						noOfBus = panelGenFixedTimeDTO.getNoOfBuses();
						
						tripsG3O = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "3", "O");
						tripsG3D = panelGenWithFixedTimeService.getNoOfTripsavail(panelGenFixedTimeDTO.getPanelGenNo(), "3", "D");

					}
					
					setSucessMsg("Successfully updated.");
					RequestContext.getCurrentInstance().update("successSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				}

			} else {
				setErrorMsg("Could not edit.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		}

		else {
			setErrorMsg("Please select at least one bus for save");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void ClearAllRecords() {

		timeStartList = new ArrayList<>();
		timeStartList2 = new ArrayList<>();
		timeStartList3 = new ArrayList<>();
		timeStartListSecond = new ArrayList<>();
		timeStartList2Second = new ArrayList<>();
		timeStartList3Second = new ArrayList<>();
		busCatList = new ArrayList<>();
		busCatList2 = new ArrayList<>();
		busCatList3 = new ArrayList<>();
		panelGenFixedTimeDTO.setRouteNo(null);
		panelGenFixedTimeDTO.setBusCategory(null);
		panelGenFixedTimeDTO.setBusCategoryDes(null);
		panelGenFixedTimeDTO.setBusType(null);
		tripsG1O = 0;
		tripsG2O = 0;
		tripsG3O = 0;
		tripsG1D = 0;
		tripsG2D = 0;
		tripsG3D = 0;
		
		busCatListDtoO = new ArrayList<>();
		busCatListDtoO2 = new ArrayList<>();
		busCatListDtoO3 = new ArrayList<>();

		panelGenFixedTimeDTO.setDestination(null);
		panelGenFixedTimeDTO.setOrigin(null);

		j = 0;
		k = 0;
		l = 0;
		jD = 0;
		kD = 0;
		lD = 0;

	}

	public boolean checkBusesAssigned() {

		boolean assigned = routeScheduleService.checkBusAssignerDone(panelGenFixedTimeDTO.getBusCategory(),
				panelGenFixedTimeDTO.getPanelGenNo(), "1", "O");

		if (assigned) {
			return false;
		}

		boolean assignedD = routeScheduleService.checkBusAssignerDone(panelGenFixedTimeDTO.getBusCategory(),
				panelGenFixedTimeDTO.getPanelGenNo(), "1", "D");

		if (assignedD) {
			return false;
		}

		boolean assigned2 = routeScheduleService.checkBusAssignerDone(panelGenFixedTimeDTO.getBusCategory(),
				panelGenFixedTimeDTO.getPanelGenNo(), "2", "O");

		if (assigned2) {
			return false;
		}

		boolean assignedD2 = routeScheduleService.checkBusAssignerDone(panelGenFixedTimeDTO.getBusCategory(),
				panelGenFixedTimeDTO.getPanelGenNo(), "2", "D");

		if (assignedD2) {
			return false;
		}

		boolean assigned3 = routeScheduleService.checkBusAssignerDone(panelGenFixedTimeDTO.getBusCategory(),
				panelGenFixedTimeDTO.getPanelGenNo(), "3", "O");

		if (assigned3) {
			return false;
		}

		boolean assignedD3 = routeScheduleService.checkBusAssignerDone(panelGenFixedTimeDTO.getBusCategory(),
				panelGenFixedTimeDTO.getPanelGenNo(), "3", "D");

		if (assignedD3) {
			return false;
		}

		return true;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public TimeTableService getPanelGenWithFixedTimeService() {
		return panelGenWithFixedTimeService;
	}

	public void setPanelGenWithFixedTimeService(TimeTableService panelGenWithFixedTimeService) {
		this.panelGenWithFixedTimeService = panelGenWithFixedTimeService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public TimeTableDTO getPanelGenFixedTimeDTO() {
		return panelGenFixedTimeDTO;
	}

	public void setPanelGenFixedTimeDTO(TimeTableDTO panelGenFixedTimeDTO) {
		this.panelGenFixedTimeDTO = panelGenFixedTimeDTO;
	}

	public List<TimeTableDTO> getRouteNoList() {
		return routeNoList;
	}

	public void setRouteNoList(List<TimeTableDTO> routeNoList) {
		this.routeNoList = routeNoList;
	}

	public List<TimeTableDTO> getBusCategoryList() {
		return busCategoryList;
	}

	public void setBusCategoryList(List<TimeTableDTO> busCategoryList) {
		this.busCategoryList = busCategoryList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<TimeTableDTO> getShowTimeSlotDet1() {
		return showTimeSlotDet1;
	}

	public void setShowTimeSlotDet1(List<TimeTableDTO> showTimeSlotDet1) {
		this.showTimeSlotDet1 = showTimeSlotDet1;
	}

	public List<TimeTableDTO> getShowTimeSlotDet2() {
		return showTimeSlotDet2;
	}

	public void setShowTimeSlotDet2(List<TimeTableDTO> showTimeSlotDet2) {
		this.showTimeSlotDet2 = showTimeSlotDet2;
	}

	public List<TimeTableDTO> getShowTimeSlotDet3() {
		return showTimeSlotDet3;
	}

	public void setShowTimeSlotDet3(List<TimeTableDTO> showTimeSlotDet3) {
		this.showTimeSlotDet3 = showTimeSlotDet3;
	}

	public TimeTableDTO getTravleTimeDTO() {
		return travleTimeDTO;
	}

	public void setTravleTimeDTO(TimeTableDTO travleTimeDTO) {
		this.travleTimeDTO = travleTimeDTO;
	}

	public TimeTableDTO getBusCategoryVal() {
		return busCategoryVal;
	}

	public void setBusCategoryVal(TimeTableDTO busCategoryVal) {
		this.busCategoryVal = busCategoryVal;
	}

	public List<TimeTableDTO> getTimeStartList() {
		return timeStartList;
	}

	public void setTimeStartList(List<TimeTableDTO> timeStartList) {
		this.timeStartList = timeStartList;
	}

	public String getRestTime() {
		return restTime;
	}

	public void setRestTime(String restTime) {
		this.restTime = restTime;
	}

	public String getAfterAddStr() {
		return afterAddStr;
	}

	public void setAfterAddStr(String afterAddStr) {
		this.afterAddStr = afterAddStr;
	}

	public long getAfterAdd() {
		return afterAdd;
	}

	public void setAfterAdd(long afterAdd) {
		this.afterAdd = afterAdd;
	}

	public TimeTableDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(TimeTableDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public List<TimeTableDTO> getBusTypeList() {
		return busTypeList;
	}

	public void setBusTypeList(List<TimeTableDTO> busTypeList) {
		this.busTypeList = busTypeList;
	}

	public String getTimeStartVal() {
		return TimeStartVal;
	}

	public void setTimeStartVal(String timeStartVal) {
		TimeStartVal = timeStartVal;
	}

	public TimeTableDTO getRefNoDTO() {
		return refNoDTO;
	}

	public void setRefNoDTO(TimeTableDTO refNoDTO) {
		this.refNoDTO = refNoDTO;
	}

	public ArrayList<String> getGroupAndTripTypeDTO() {
		return groupAndTripTypeDTO;
	}

	public void setGroupAndTripTypeDTO(ArrayList<String> groupAndTripTypeDTO) {
		this.groupAndTripTypeDTO = groupAndTripTypeDTO;
	}

	public List<TimeTableDTO> getTimeStartList2() {
		return timeStartList2;
	}

	public void setTimeStartList2(List<TimeTableDTO> timeStartList2) {
		this.timeStartList2 = timeStartList2;
	}

	public List<TimeTableDTO> getTimeStartList3() {
		return timeStartList3;
	}

	public void setTimeStartList3(List<TimeTableDTO> timeStartList3) {
		this.timeStartList3 = timeStartList3;
	}

	public boolean isBusTypeDisable1() {
		return busTypeDisable1;
	}

	public void setBusTypeDisable1(boolean busTypeDisable1) {
		this.busTypeDisable1 = busTypeDisable1;
	}

	public boolean isBusTypeDisable2() {
		return busTypeDisable2;
	}

	public void setBusTypeDisable2(boolean busTypeDisable2) {
		this.busTypeDisable2 = busTypeDisable2;
	}

	public boolean isBusTypeDisable3() {
		return busTypeDisable3;
	}

	public void setBusTypeDisable3(boolean busTypeDisable3) {
		this.busTypeDisable3 = busTypeDisable3;
	}

	public List<TimeTableDTO> getTimeStartListSecond() {
		return timeStartListSecond;
	}

	public void setTimeStartListSecond(List<TimeTableDTO> timeStartListSecond) {
		this.timeStartListSecond = timeStartListSecond;
	}

	public List<TimeTableDTO> getTimeStartList2Second() {
		return timeStartList2Second;
	}

	public void setTimeStartList2Second(List<TimeTableDTO> timeStartList2Second) {
		this.timeStartList2Second = timeStartList2Second;
	}

	public List<TimeTableDTO> getTimeStartList3Second() {
		return timeStartList3Second;
	}

	public void setTimeStartList3Second(List<TimeTableDTO> timeStartList3Second) {
		this.timeStartList3Second = timeStartList3Second;
	}

	public List<TimeTableDTO> getBusTypeSetList() {
		return busTypeSetList;
	}

	public void setBusTypeSetList(List<TimeTableDTO> busTypeSetList) {
		this.busTypeSetList = busTypeSetList;
	}

	public int getNoOfBus() {
		return noOfBus;
	}

	public void setNoOfBus(int noOfBus) {
		this.noOfBus = noOfBus;
	}

	public List<TimeTableDTO> getBusCatList() {
		return busCatList;
	}

	public void setBusCatList(List<TimeTableDTO> busCatList) {
		this.busCatList = busCatList;
	}

	public List<TimeTableDTO> getBusTypeSetList2() {
		return busTypeSetList2;
	}

	public void setBusTypeSetList2(List<TimeTableDTO> busTypeSetList2) {
		this.busTypeSetList2 = busTypeSetList2;
	}

	public List<TimeTableDTO> getBusTypeSetList3() {
		return busTypeSetList3;
	}

	public void setBusTypeSetList3(List<TimeTableDTO> busTypeSetList3) {
		this.busTypeSetList3 = busTypeSetList3;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public int getNoOFTripsValGOne() {
		return noOFTripsValGOne;
	}

	public void setNoOFTripsValGOne(int noOFTripsValGOne) {
		this.noOFTripsValGOne = noOFTripsValGOne;
	}

	public int getNoOFTripsValGTwo() {
		return noOFTripsValGTwo;
	}

	public void setNoOFTripsValGTwo(int noOFTripsValGTwo) {
		this.noOFTripsValGTwo = noOFTripsValGTwo;
	}

	public int getNoOFTripsValGThree() {
		return noOFTripsValGThree;
	}

	public void setNoOFTripsValGThree(int noOFTripsValGThree) {
		this.noOFTripsValGThree = noOFTripsValGThree;
	}

	public List<TimeTableDTO> getBusCatList2() {
		return busCatList2;
	}

	public void setBusCatList2(List<TimeTableDTO> busCatList2) {
		this.busCatList2 = busCatList2;
	}

	public List<TimeTableDTO> getBusCatList3() {
		return busCatList3;
	}

	public void setBusCatList3(List<TimeTableDTO> busCatList3) {
		this.busCatList3 = busCatList3;
	}

	public List<TimeTableDTO> getBusCatListDtoO() {
		return busCatListDtoO;
	}

	public void setBusCatListDtoO(List<TimeTableDTO> busCatListDtoO) {
		this.busCatListDtoO = busCatListDtoO;
	}

	public List<TimeTableDTO> getBusCatListDtoO2() {
		return busCatListDtoO2;
	}

	public void setBusCatListDtoO2(List<TimeTableDTO> busCatListDtoO2) {
		this.busCatListDtoO2 = busCatListDtoO2;
	}

	public List<TimeTableDTO> getBusCatListDtoO3() {
		return busCatListDtoO3;
	}

	public void setBusCatListDtoO3(List<TimeTableDTO> busCatListDtoO3) {
		this.busCatListDtoO3 = busCatListDtoO3;
	}

	public boolean isBusTypeDisableD1() {
		return busTypeDisableD1;
	}

	public void setBusTypeDisableD1(boolean busTypeDisableD1) {
		this.busTypeDisableD1 = busTypeDisableD1;
	}

	public List<TimeTableDTO> getBusCategoryListMain() {
		return busCategoryListMain;
	}

	public void setBusCategoryListMain(List<TimeTableDTO> busCategoryListMain) {
		this.busCategoryListMain = busCategoryListMain;
	}

	public List<TimeTableDTO> getShowInsertedDataG1O() {
		return showInsertedDataG1O;
	}

	public void setShowInsertedDataG1O(List<TimeTableDTO> showInsertedDataG1O) {
		this.showInsertedDataG1O = showInsertedDataG1O;
	}

	public List<TimeTableDTO> getShowInsertedDataG2O() {
		return showInsertedDataG2O;
	}

	public void setShowInsertedDataG2O(List<TimeTableDTO> showInsertedDataG2O) {
		this.showInsertedDataG2O = showInsertedDataG2O;
	}

	public List<TimeTableDTO> getShowInsertedDataG3O() {
		return showInsertedDataG3O;
	}

	public void setShowInsertedDataG3O(List<TimeTableDTO> showInsertedDataG3O) {
		this.showInsertedDataG3O = showInsertedDataG3O;
	}

	public List<TimeTableDTO> getShowInsertedDataG1D() {
		return showInsertedDataG1D;
	}

	public void setShowInsertedDataG1D(List<TimeTableDTO> showInsertedDataG1D) {
		this.showInsertedDataG1D = showInsertedDataG1D;
	}

	public List<TimeTableDTO> getShowInsertedDataG2D() {
		return showInsertedDataG2D;
	}

	public void setShowInsertedDataG2D(List<TimeTableDTO> showInsertedDataG2D) {
		this.showInsertedDataG2D = showInsertedDataG2D;
	}

	public List<TimeTableDTO> getShowInsertedDataG3D() {
		return showInsertedDataG3D;
	}

	public void setShowInsertedDataG3D(List<TimeTableDTO> showInsertedDataG3D) {
		this.showInsertedDataG3D = showInsertedDataG3D;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public TimeTableDTO getRouteDet() {
		return routeDet;
	}

	public void setRouteDet(TimeTableDTO routeDet) {
		this.routeDet = routeDet;
	}

	public int getTripsG1O() {
		return tripsG1O;
	}

	public void setTripsG1O(int tripsG1O) {
		this.tripsG1O = tripsG1O;
	}

	public int getTripsG2O() {
		return tripsG2O;
	}

	public void setTripsG2O(int tripsG2O) {
		this.tripsG2O = tripsG2O;
	}

	public int getTripsG3O() {
		return tripsG3O;
	}

	public void setTripsG3O(int tripsG3O) {
		this.tripsG3O = tripsG3O;
	}

	public int getTripsG1D() {
		return tripsG1D;
	}

	public void setTripsG1D(int tripsG1D) {
		this.tripsG1D = tripsG1D;
	}

	public int getTripsG2D() {
		return tripsG2D;
	}

	public void setTripsG2D(int tripsG2D) {
		this.tripsG2D = tripsG2D;
	}

	public int getTripsG3D() {
		return tripsG3D;
	}

	public void setTripsG3D(int tripsG3D) {
		this.tripsG3D = tripsG3D;
	}

	public RouteScheduleService getRouteScheduleService() {
		return routeScheduleService;
	}

	public void setRouteScheduleService(RouteScheduleService routeScheduleService) {
		this.routeScheduleService = routeScheduleService;
	}

	public TimeTableDTO getNonSltbEtcBusDTO() {
		return nonSltbEtcBusDTO;
	}

	public void setNonSltbEtcBusDTO(TimeTableDTO nonSltbEtcBusDTO) {
		this.nonSltbEtcBusDTO = nonSltbEtcBusDTO;
	}

}
