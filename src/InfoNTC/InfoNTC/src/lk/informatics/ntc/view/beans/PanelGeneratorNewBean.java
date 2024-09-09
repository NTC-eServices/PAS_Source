package lk.informatics.ntc.view.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PanelGeneratorNewDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.PanelGeneratorNewService;
import lk.informatics.ntc.model.service.TimeTableService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "panelGeneratorNewBean")
@ViewScoped
public class PanelGeneratorNewBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private boolean groupPanel;
	private boolean tabelPanel;
	private boolean group1;
	private boolean group2;
	private boolean group3;
	private boolean view;
	private boolean addBtn;
	private boolean disablepanel1;
	private boolean disablepanel2;
	private boolean disablepanel3;

	private boolean errorFound;

	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;

	private String errorMsg;
	private String sucessMsg;
	private String warningMsg;

	private String clickMonday;
	private String clickTuesday;
	private String clickWednesday;
	private String clickThursday;
	private String clickFriday;
	private String clickSaturday;
	private String clickSunday;

	private boolean checkdate;

	private boolean disablePanel;
	private boolean noChangeTimeTable;

	private RouteDTO routeDTO;

	// Services
	@ManagedProperty("#{panelGeneratorNewService}")
	private PanelGeneratorNewService panelGeneratorService;

	private AdminService adminService;
	private TimeTableService timeTableService;

	// Lists
	private List<String> groupList = new ArrayList<String>();
	private List<String> dateList_Group1 = new ArrayList<String>();
	private List<String> dateList_Group2 = new ArrayList<String>();
	private List<String> dateList_Group3 = new ArrayList<String>();

	private List<PanelGeneratorNewDTO> group1_OD_List = new ArrayList<PanelGeneratorNewDTO>();
	private List<PanelGeneratorNewDTO> group2_OD_List = new ArrayList<PanelGeneratorNewDTO>();
	private List<PanelGeneratorNewDTO> group3_OD_List = new ArrayList<PanelGeneratorNewDTO>();
	private List<PanelGeneratorNewDTO> group1_DO_List = new ArrayList<PanelGeneratorNewDTO>();
	private List<PanelGeneratorNewDTO> group2_DO_List = new ArrayList<PanelGeneratorNewDTO>();
	private List<PanelGeneratorNewDTO> group3_DO_List = new ArrayList<PanelGeneratorNewDTO>();

	private List<String> group1_OrigintoDestinationList = new ArrayList<String>();
	private List<String> group2_OrigintoDestinationList = new ArrayList<String>();
	private List<String> group3_OrigintoDestinationList = new ArrayList<String>();

	private List<String> group1_DestinationtoOriginList = new ArrayList<String>();
	private List<String> group2_DestinationtoOriginList = new ArrayList<String>();
	private List<String> group3_DestinationtoOriginList = new ArrayList<String>();

	private List<CommonDTO> selectedRouteList = new ArrayList<CommonDTO>();

	private List<CommonDTO> routefordropdownList;
	private List<CommonDTO> serviceTypedropdownList;

	private List<TimeTableDTO> tripListOrigin;

	// DTOs
	private PanelGeneratorNewDTO panelGeneratorDTO = new PanelGeneratorNewDTO();

	private List<PanelGeneratorNewDTO> refNoList;


	private TimeTableDTO timeTableDTO, tripsDTO;

//	Panel Generator New

	private int noOfTripsOrigin;
	private int noOfPvtBusesOrigin;
	private int noOfTemporaryBusesOrigin;
	private int noOfCtbOrigin;
	private int noOfEtcOrigin;
	private int noOfPvtLeaveBusesOrigin;
	private int noOfDummyBusesOrigin;
	private String restTimeOrigin;
	private int totalBusesOrigin;
	private String abbreviationOrigin;
	private String permitNoOrigin;
	private String busNoOrigin;
	private String startTimeOrigin;
	private String endTimeOrigin;
	private boolean fixBusOrigin;

	private int noOfTripsDestination;
	private int noOfPvtBusesDestination;
	private int noOfTemporaryBusesDestination;
	private int noOfCtbDestination;
	private int noOfEtcDestination;
	private int noOfPvtLeaveBusesDestination;
	private int noOfDummyBusesDestination;
	private String restTimeDestination;
	private int totalBusesDestination;
	private String abbreviationDestination;
	private String permitNoDestination;
	private String nusNoDestination;
	private String startTimeDestination;
	private String endTimeDestination;
	private boolean fixBusDestination;

	@PostConstruct
	public void init() {

		panelGeneratorService = (PanelGeneratorNewService) SpringApplicationContex.getBean("panelGeneratorNewService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		
		dateList_Group1.clear();
		dateList_Group2.clear();
		dateList_Group3.clear();

		group1_OD_List.clear();
		group2_OD_List.clear();
		group3_OD_List.clear();

		group1_DO_List.clear();
		group2_DO_List.clear();
		group3_DO_List.clear();

		refNoList = panelGeneratorService.getRefNoList();
		tripListOrigin = panelGeneratorService.getTrips();
		
		noOfPvtBusesOrigin = timeTableService.getPVTbusCount(timeTableDTO.getRouteNo(), false,
				timeTableDTO.getBusCategory(),timeTableDTO.getGroupNo());
		System.out.println(noOfPvtBusesOrigin);
		
		disablePanel = false;
	}

	public void generateOriginAndDestination() {

		boolean found = false;
		String route_no = panelGeneratorDTO.getRouteNo();

		routeDTO = adminService.getDetailsbyRouteNo(route_no);

		panelGeneratorDTO.setOrigin(routeDTO.getOrigin());
		panelGeneratorDTO.setDestination(routeDTO.getDestination());

	}

	public void savePanel1() {

		if (panelGeneratorDTO.getRouteNo() != null && !panelGeneratorDTO.getRouteNo().isEmpty()
				&& !panelGeneratorDTO.getRouteNo().equalsIgnoreCase("")) {
			if (panelGeneratorDTO.getBusCategory() != null && !panelGeneratorDTO.getBusCategory().isEmpty()
					&& !panelGeneratorDTO.getBusCategory().equalsIgnoreCase("")) {
				if (panelGeneratorDTO.getNoOfTimeTables() != null && !panelGeneratorDTO.getNoOfTimeTables().isEmpty()
						&& !panelGeneratorDTO.getNoOfTimeTables().equalsIgnoreCase("")) {
					if (panelGeneratorDTO.getPanel1Status() != null && !panelGeneratorDTO.getPanel1Status().isEmpty()
							&& !panelGeneratorDTO.getPanel1Status().equalsIgnoreCase("")) {

						/** restrict duplicate same route and bus service **/
						boolean haveRouteBusCategory = panelGeneratorService.getActiveDataRecord(
								panelGeneratorDTO.getRouteNo(), panelGeneratorDTO.getBusCategory());

						if (!haveRouteBusCategory) {
							setGroupPanel(true);
							setTabelPanel(false);
							setView(false);
							setDisablepanel1(true);
							groupList.clear();

							int timetables = Integer.parseInt(panelGeneratorDTO.getNoOfTimeTables());

							for (int i = 0; i < timetables; i++) {

								int j = i + 1;
								String number = new Integer(j).toString();

								groupList.add(number);

							}
						} else {
							errorMsg = "This route and service type already have active record.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {

						errorMsg = "Please Select the No. of Time Tables per week";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {

					errorMsg = "Please Select the No. of Time Tables per week";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				errorMsg = "Please Select the Bus Category";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else {

			errorMsg = "Please Select the Route No.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void clearPanel1() {

		panelGeneratorDTO = new PanelGeneratorNewDTO();
		setDisablepanel1(false);
	}

	public void addPanel2() {

		setErrorFound(false);

		if (panelGeneratorDTO.getSelectGroup() != null && !panelGeneratorDTO.getSelectGroup().isEmpty()
				&& !panelGeneratorDTO.getSelectGroup().equalsIgnoreCase("")) {
			if (panelGeneratorDTO.getPanel2Status() != null && !panelGeneratorDTO.getPanel2Status().isEmpty()
					&& !panelGeneratorDTO.getPanel2Status().equalsIgnoreCase("")) {

				if (panelGeneratorDTO.getSelectGroup().equals("1")) {

					if (clickMonday.equals("true")) {
						setMonday(true);

						dateList_Group1.add("Monday");

					}

					if (clickTuesday.equals("true")) {
						setTuesday(true);

						dateList_Group1.add("Tuesday");

					}
					if (clickWednesday.equals("true")) {
						setWednesday(true);

						dateList_Group1.add("Wednesday");

					}
					if (clickThursday.equals("true")) {
						setThursday(true);

						dateList_Group1.add("Thursday");

					}
					if (clickFriday.equals("true")) {
						setFriday(true);

						dateList_Group1.add("Friday");

					}
					if (clickSaturday.equals("true")) {
						setSaturday(true);

						dateList_Group1.add("Saturday");

					}
					if (clickSunday.equals("true")) {
						setSunday(true);

						dateList_Group1.add("Sunday");

					}

				}

				if (panelGeneratorDTO.getSelectGroup().equals("2")) {

					if (clickMonday.equals("true")) {
						setMonday(true);

						dateList_Group2.add("Monday");

					}
					if (clickTuesday.equals("true")) {
						setTuesday(true);
						dateList_Group2.add("Tuesday");
					}
					if (clickWednesday.equals("true")) {
						setWednesday(true);
						dateList_Group2.add("Wednesday");
					}
					if (clickThursday.equals("true")) {
						setThursday(true);
						dateList_Group2.add("Thursday");
					}
					if (clickFriday.equals("true")) {
						setFriday(true);
						dateList_Group2.add("Friday");
					}
					if (clickSaturday.equals("true")) {
						setSaturday(true);
						dateList_Group2.add("Saturday");
					}
					if (clickSunday.equals("true")) {
						setSunday(true);
						dateList_Group2.add("Sunday");
					}

				}

				if (panelGeneratorDTO.getSelectGroup().equals("3")) {

					if (clickMonday.equals("true")) {
						setMonday(true);

						dateList_Group3.add("Monday");

					}
					if (clickTuesday.equals("true")) {
						setTuesday(true);
						dateList_Group3.add("Tuesday");
					}
					if (clickWednesday.equals("true")) {
						setWednesday(true);
						dateList_Group3.add("Wednesday");
					}
					if (clickThursday.equals("true")) {
						setThursday(true);
						dateList_Group3.add("Thursday");
					}
					if (clickFriday.equals("true")) {
						setFriday(true);
						dateList_Group3.add("Friday");
					}
					if (clickSaturday.equals("true")) {
						setSaturday(true);
						dateList_Group3.add("Saturday");
					}
					if (clickSunday.equals("true")) {
						setSunday(true);
						dateList_Group3.add("Sunday");
					}

				}

				/** validation **/
				if (panelGeneratorDTO.getNoOfTimeTables().equals("1")) {
					if (dateList_Group1.size() < 7) {

						errorMsg = "Please Select All the days to proceed";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

						setErrorFound(true);

					}
				} else if (panelGeneratorDTO.getNoOfTimeTables().equals("2")) {
					if (dateList_Group1.size() == 0 && dateList_Group2.size() == 0) {

						errorMsg = "Please Select day/days to proceed";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

						setErrorFound(true);
					}
					if (dateList_Group1.size() == 0 && dateList_Group2.size() != 0) {

						if (dateList_Group2.size() == 7) {
							errorMsg = "Select 6 or less days";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);
						} else {
							int total = 7;
							int remaining = total - dateList_Group2.size();

							if (remaining < 7) {
								warningMsg = "You have " + remaining + " days remaining";

								RequestContext.getCurrentInstance().update("frmwarningField");
								RequestContext.getCurrentInstance().execute("PF('warningField').show()");

							}
						}

					}
					if (dateList_Group1.size() != 0 && dateList_Group2.size() == 0) {

						if (dateList_Group1.size() == 7) {
							errorMsg = "Select 6 or less days";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);
						} else {
							int total = 7;
							int remaining = total - dateList_Group1.size();

							if (remaining < 7) {
								warningMsg = "You have " + remaining + " days remaining";

								RequestContext.getCurrentInstance().update("frmwarningField");
								RequestContext.getCurrentInstance().execute("PF('warningField').show()");
							}
						}

					}
					if (dateList_Group1.size() != 0 && dateList_Group2.size() != 0) {

						int total = dateList_Group1.size() + dateList_Group2.size();

						if (total < 7) {
							errorMsg = "Please Select All the days to proceed";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);

						}

					}
				} else if (panelGeneratorDTO.getNoOfTimeTables().equals("3")) {
					if (dateList_Group1.size() == 0 && dateList_Group2.size() == 0 && dateList_Group3.size() == 0) {

						errorMsg = "Please Select day/days to proceed";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

						setErrorFound(true);
					}
					if (dateList_Group1.size() == 0 && dateList_Group2.size() == 0 && dateList_Group3.size() != 0) {

						if (dateList_Group3.size() == 7 || dateList_Group3.size() == 6) {
							errorMsg = "Select 5 or less days";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);
						} else {

							int remaining = 7 - dateList_Group3.size();

							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
						}

					}
					if (dateList_Group1.size() == 0 && dateList_Group2.size() != 0 && dateList_Group3.size() == 0) {

						if (dateList_Group2.size() == 7 || dateList_Group2.size() == 6) {
							errorMsg = "Select 5 or less days";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);
						} else {

							int remaining = 7 - dateList_Group2.size();

							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
						}

					}

					if (dateList_Group1.size() != 0 && dateList_Group2.size() == 0 && dateList_Group3.size() == 0) {

						if (dateList_Group1.size() == 7 || dateList_Group1.size() == 6) {
							errorMsg = "Select 5 or less days";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);
						} else {

							int remaining = 7 - dateList_Group1.size();

							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
						}
					}

					if (dateList_Group1.size() == 0 && dateList_Group2.size() != 0 && dateList_Group3.size() != 0) {

						int selectDays = dateList_Group2.size() + dateList_Group3.size();

						if (selectDays == 7) {

							errorMsg = "Please reduce a day/days";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);

						} else {

							int remaining = 7 - selectDays;
							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
						}
					}

					if (dateList_Group1.size() != 0 && dateList_Group2.size() == 0 && dateList_Group3.size() != 0) {

						int selectDays = dateList_Group1.size() + dateList_Group3.size();

						if (selectDays == 7) {

							errorMsg = "Please reduce a day/days";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);

						} else {

							int remaining = 7 - selectDays;
							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
						}

					}
					if (dateList_Group1.size() != 0 && dateList_Group2.size() != 0 && dateList_Group3.size() == 0) {

						int selectDays = dateList_Group1.size() + dateList_Group2.size();

						if (selectDays == 7) {

							errorMsg = "Please reduce a day/days";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);

						} else {

							int remaining = 7 - selectDays;
							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
						}
					}
					if (dateList_Group1.size() != 0 && dateList_Group2.size() != 0 && dateList_Group3.size() != 0) {
						int total = dateList_Group1.size() + dateList_Group2.size() + dateList_Group3.size();

						if (total < 7) {
							errorMsg = "Please Select All the days to proceed";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);

						}

					}
				}
				/** validation **/

				if (isErrorFound() == false) {

					setDisablepanel2(true);
					setAddBtn(true);
					setTabelPanel(true);

					if (panelGeneratorDTO.getSelectGroup().equals("1")) {

						setGroup1(false);
						setGroup2(true);
						setGroup3(true);
					} else if (panelGeneratorDTO.getSelectGroup().equals("2")) {

						setGroup1(true);
						setGroup2(false);
						setGroup3(true);
					} else if (panelGeneratorDTO.getSelectGroup().equals("3")) {

						setGroup1(true);
						setGroup2(true);
						setGroup3(false);
					}

				}

			} else {

				errorMsg = "Please Select A Status";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else {

			errorMsg = "Please Select A group to proceed";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void clearPanel2() {
		setDisablepanel2(false);
		setAddBtn(false);
		panelGeneratorDTO.setSelectGroup("");
		setClickMonday("false");
		setClickTuesday("false");
		setClickWednesday("false");
		setClickThursday("false");
		setClickFriday("false");
		setClickSaturday("false");
		setClickSunday("false");
		setGroup1(true);
		setGroup2(true);
		setGroup3(true);

	}

	public void saveGroup01_OriginToDestination() {

		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
		String startTime = localDateFormat.format(panelGeneratorDTO.getGroup01_OD_StartTime());
		String endTime = localDateFormat.format(panelGeneratorDTO.getGroup01_OD_EndTime());
		String divideRange = localDateFormat.format(panelGeneratorDTO.getGroup01_OD_DivideRange());

		if (startTime != null) {

			if (endTime != null) {

				if (divideRange != null) {

					group1_OD_List.clear();

					PanelGeneratorNewDTO insertGroup1_OD = new PanelGeneratorNewDTO();

					insertGroup1_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
					insertGroup1_OD.setGroup01_OD_StartTime(panelGeneratorDTO.getGroup01_OD_StartTime());
					insertGroup1_OD.setGroup01_OD_EndTime(panelGeneratorDTO.getGroup01_OD_EndTime());
					insertGroup1_OD.setGroup01_OD_DivideRange(panelGeneratorDTO.getGroup01_OD_DivideRange());

					group1_OD_List.add(insertGroup1_OD);

					setSucessMsg("Successfully Saved");
					;
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {

					errorMsg = "Please Enter the Divide Range";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				errorMsg = "Please Enter the End Time";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Please Enter the Start Time";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void saveGroup02_OriginToDestination() {

		if (panelGeneratorDTO.getGroup02_OD_StartTime() != null) {
			if (panelGeneratorDTO.getGroup02_OD_EndTime() != null) {

				if (panelGeneratorDTO.getGroup02_OD_DivideRange() != null) {

					group2_OD_List.clear();

					PanelGeneratorNewDTO insertGroup2_OD = new PanelGeneratorNewDTO();

					insertGroup2_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
					insertGroup2_OD.setGroup02_OD_StartTime(panelGeneratorDTO.getGroup02_OD_StartTime());
					insertGroup2_OD.setGroup02_OD_EndTime(panelGeneratorDTO.getGroup02_OD_EndTime());
					insertGroup2_OD.setGroup02_OD_DivideRange(panelGeneratorDTO.getGroup02_OD_DivideRange());

					group2_OD_List.add(insertGroup2_OD);

					setSucessMsg("Successfully Saved");
					;
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {

					errorMsg = "Please Enter the Divide Range";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				errorMsg = "Please Enter the End Time";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {

			errorMsg = "Please Enter the Start Time";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void saveGroup03_OriginToDestination() {

		if (panelGeneratorDTO.getGroup03_OD_StartTime() != null) {
			if (panelGeneratorDTO.getGroup03_OD_EndTime() != null) {

				if (panelGeneratorDTO.getGroup03_OD_DivideRange() != null) {

					group3_OD_List.clear();

					PanelGeneratorNewDTO insertGroup3_OD = new PanelGeneratorNewDTO();

					insertGroup3_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
					insertGroup3_OD.setGroup03_OD_StartTime(panelGeneratorDTO.getGroup03_OD_StartTime());
					insertGroup3_OD.setGroup03_OD_EndTime(panelGeneratorDTO.getGroup03_OD_EndTime());
					insertGroup3_OD.setGroup03_OD_DivideRange(panelGeneratorDTO.getGroup03_OD_DivideRange());

					group3_OD_List.add(insertGroup3_OD);

					setSucessMsg("Successfully Saved");
					;
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {

					errorMsg = "Please Enter the Divide Range";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				errorMsg = "Please Enter the End Time";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {

			errorMsg = "Please Enter the Start Time";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void saveGroup01_DestinationToOrigin() {

		if (panelGeneratorDTO.getGroup01_DO_StartTime() != null) {
			if (panelGeneratorDTO.getGroup01_DO_EndTime() != null) {

				if (panelGeneratorDTO.getGroup01_DO_DivideRange() != null) {

					group1_DO_List.clear();
					PanelGeneratorNewDTO insertGroup1_DO = new PanelGeneratorNewDTO();

					insertGroup1_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
					insertGroup1_DO.setGroup01_DO_StartTime(panelGeneratorDTO.getGroup01_DO_StartTime());
					insertGroup1_DO.setGroup01_DO_EndTime(panelGeneratorDTO.getGroup01_DO_EndTime());
					insertGroup1_DO.setGroup01_DO_DivideRange(panelGeneratorDTO.getGroup01_DO_DivideRange());

					group1_DO_List.add(insertGroup1_DO);

					setSucessMsg("Successfully Saved");
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {

					errorMsg = "Please Enter the Divide Range";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				errorMsg = "Please Enter the End Time";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {

			errorMsg = "Please Enter the Start Time";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void saveGroup02_DestinationToOrigin() {

		if (panelGeneratorDTO.getGroup02_DO_StartTime() != null) {
			if (panelGeneratorDTO.getGroup02_DO_EndTime() != null) {

				if (panelGeneratorDTO.getGroup02_DO_DivideRange() != null) {

					group2_DO_List.clear();
					PanelGeneratorNewDTO insertGroup2_DO = new PanelGeneratorNewDTO();

					insertGroup2_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
					insertGroup2_DO.setGroup02_DO_StartTime(panelGeneratorDTO.getGroup02_DO_StartTime());
					insertGroup2_DO.setGroup02_DO_EndTime(panelGeneratorDTO.getGroup02_DO_EndTime());
					insertGroup2_DO.setGroup02_DO_DivideRange(panelGeneratorDTO.getGroup02_DO_DivideRange());

					group2_DO_List.add(insertGroup2_DO);

					setSucessMsg("Successfully Saved");
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {

					errorMsg = "Please Enter the Divide Range";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				errorMsg = "Please Enter the End Time";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {

			errorMsg = "Please Enter the Start Time";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void saveGroup03_DestinationToOrigin() {

		if (panelGeneratorDTO.getGroup03_DO_StartTime() != null) {
			if (panelGeneratorDTO.getGroup03_DO_EndTime() != null) {

				if (panelGeneratorDTO.getGroup03_DO_DivideRange() != null) {

					group3_DO_List.clear();
					PanelGeneratorNewDTO insertGroup3_DO = new PanelGeneratorNewDTO();

					insertGroup3_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
					insertGroup3_DO.setGroup03_DO_StartTime(panelGeneratorDTO.getGroup03_DO_StartTime());
					insertGroup3_DO.setGroup03_DO_EndTime(panelGeneratorDTO.getGroup03_DO_EndTime());
					insertGroup3_DO.setGroup03_DO_DivideRange(panelGeneratorDTO.getGroup03_DO_DivideRange());

					group3_DO_List.add(insertGroup3_DO);

					setSucessMsg("Successfully Saved");
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {

					errorMsg = "Please Enter the Divide Range";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				errorMsg = "Please Enter the End Time";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {

			errorMsg = "Please Enter the Start Time";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void checkDates() {

		setClickMonday("false");
		setClickTuesday("false");
		setClickWednesday("false");
		setClickThursday("false");
		setClickFriday("false");
		setClickSaturday("false");
		setClickSunday("false");

		panelGeneratorDTO.setSelectGroup(panelGeneratorDTO.getSelectGroup());

		if (panelGeneratorDTO.getSelectGroup().equals("1")) {

			if (dateList_Group1.size() == 0) {
				setAddBtn(false);

			} else {
				setGroup1(false);
				setGroup2(true);
				setGroup3(true);
				setAddBtn(true);
				for (int i = 0; i < dateList_Group1.size(); i++) {

					String day = dateList_Group1.get(i);

					if (day.equals("Monday")) {
						setClickMonday("true");
					} else if (day.equals("Tuesday")) {
						setClickTuesday("true");
					} else if (day.equals("Wednesday")) {
						setClickWednesday("true");
					} else if (day.equals("Thursday")) {
						setClickThursday("true");
					} else if (day.equals("Friday")) {
						setClickFriday("true");
					} else if (day.equals("Saturday")) {
						setClickSaturday("true");
					} else if (day.equals("Sunday")) {
						setClickSunday("true");
					}
				}

			}
		}

		if (panelGeneratorDTO.getSelectGroup().equals("2")) {

			if (dateList_Group2.size() == 0) {
				setAddBtn(false);

			} else {
				setGroup1(true);
				setGroup2(false);
				setGroup3(true);
				setAddBtn(true);
				for (int i = 0; i < dateList_Group2.size(); i++) {

					String day = dateList_Group2.get(i);

					if (day.equals("Monday")) {
						setClickMonday("true");
					} else if (day.equals("Tuesday")) {
						setClickTuesday("true");
					} else if (day.equals("Wednesday")) {
						setClickWednesday("true");
					} else if (day.equals("Thursday")) {
						setClickThursday("true");
					} else if (day.equals("Friday")) {
						setClickFriday("true");
					} else if (day.equals("Saturday")) {
						setClickSaturday("true");
					} else if (day.equals("Sunday")) {
						setClickSunday("true");
					}
				}

			}

		}

		if (panelGeneratorDTO.getSelectGroup().equals("3")) {
			if (dateList_Group3.size() == 0) {
				setAddBtn(false);

			} else {
				setGroup1(true);
				setGroup2(true);
				setGroup3(false);
				setAddBtn(true);
				for (int i = 0; i < dateList_Group3.size(); i++) {

					String day = dateList_Group3.get(i);

					if (day.equals("Monday")) {
						setClickMonday("true");
					} else if (day.equals("Tuesday")) {
						setClickTuesday("true");
					} else if (day.equals("Wednesday")) {
						setClickWednesday("true");
					} else if (day.equals("Thursday")) {
						setClickThursday("true");
					} else if (day.equals("Friday")) {
						setClickFriday("true");
					} else if (day.equals("Saturday")) {
						setClickSaturday("true");
					} else if (day.equals("Sunday")) {
						setClickSunday("true");
					}
				}

			}

		}

		if (group1_OD_List.size() == 0) {

			panelGeneratorDTO.setGroup01_OD_StartTime(null);
			panelGeneratorDTO.setGroup01_OD_EndTime(null);
			panelGeneratorDTO.setGroup01_OD_DivideRange(null);
		}

		if (group1_DO_List.size() == 0) {
			panelGeneratorDTO.setGroup01_DO_StartTime(null);
			panelGeneratorDTO.setGroup01_DO_EndTime(null);
			panelGeneratorDTO.setGroup01_DO_DivideRange(null);
		}

		if (group2_OD_List.size() == 0) {

			panelGeneratorDTO.setGroup02_OD_StartTime(null);
			panelGeneratorDTO.setGroup02_OD_EndTime(null);
			panelGeneratorDTO.setGroup02_OD_DivideRange(null);
		}

		if (group2_DO_List.size() == 0) {
			panelGeneratorDTO.setGroup02_DO_StartTime(null);
			panelGeneratorDTO.setGroup02_DO_EndTime(null);
			panelGeneratorDTO.setGroup02_DO_DivideRange(null);
		}

		if (group3_OD_List.size() == 0) {

			panelGeneratorDTO.setGroup03_OD_StartTime(null);
			panelGeneratorDTO.setGroup03_OD_EndTime(null);
			panelGeneratorDTO.setGroup03_OD_DivideRange(null);
		}

		if (group3_DO_List.size() == 0) {
			panelGeneratorDTO.setGroup03_DO_StartTime(null);
			panelGeneratorDTO.setGroup03_DO_EndTime(null);
			panelGeneratorDTO.setGroup03_DO_DivideRange(null);
		}

	}

	public void checkMonday() {

		String day = "Monday";

		for (int i = 0; i < dateList_Group1.size(); i++) {

			if (dateList_Group1.get(i).equals(day)) {

				setClickMonday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group2.size(); i++) {

			if (dateList_Group2.get(i).equals(day)) {

				setClickMonday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group3.size(); i++) {

			if (dateList_Group3.get(i).equals(day)) {

				setClickMonday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

	}

	public void checkTuesday() {

		String day = "Tuesday";

		for (int i = 0; i < dateList_Group1.size(); i++) {

			if (dateList_Group1.get(i).equals(day)) {

				setClickTuesday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group2.size(); i++) {

			if (dateList_Group2.get(i).equals(day)) {

				setClickTuesday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group3.size(); i++) {

			if (dateList_Group3.get(i).equals(day)) {

				setClickTuesday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

	}

	public void checkWednesday() {

		String day = "Wednesday";

		for (int i = 0; i < dateList_Group1.size(); i++) {

			if (dateList_Group1.get(i).equals(day)) {

				setClickWednesday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group2.size(); i++) {

			if (dateList_Group2.get(i).equals(day)) {

				setClickWednesday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group3.size(); i++) {

			if (dateList_Group3.get(i).equals(day)) {

				setClickWednesday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

	}

	public void checkThursday() {

		String day = "Thursday";

		for (int i = 0; i < dateList_Group1.size(); i++) {

			if (dateList_Group1.get(i).equals(day)) {

				setClickThursday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group2.size(); i++) {

			if (dateList_Group2.get(i).equals(day)) {

				setClickThursday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group3.size(); i++) {

			if (dateList_Group3.get(i).equals(day)) {

				setClickThursday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

	}

	public void checkFriday() {

		String day = "Friday";

		for (int i = 0; i < dateList_Group1.size(); i++) {

			if (dateList_Group1.get(i).equals(day)) {

				setClickFriday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group2.size(); i++) {

			if (dateList_Group2.get(i).equals(day)) {

				setClickFriday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group3.size(); i++) {

			if (dateList_Group3.get(i).equals(day)) {

				setClickFriday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

	}

	public void checkSaturday() {

		String day = "Saturday";

		for (int i = 0; i < dateList_Group1.size(); i++) {

			if (dateList_Group1.get(i).equals(day)) {

				setClickSaturday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group2.size(); i++) {

			if (dateList_Group2.get(i).equals(day)) {

				setClickSaturday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group3.size(); i++) {

			if (dateList_Group3.get(i).equals(day)) {

				setClickSaturday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

	}

	public void checkSunday() {

		String day = "Sunday";

		for (int i = 0; i < dateList_Group1.size(); i++) {

			if (dateList_Group1.get(i).equals(day)) {

				setClickSunday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group2.size(); i++) {

			if (dateList_Group2.get(i).equals(day)) {

				setClickSunday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

		for (int i = 0; i < dateList_Group3.size(); i++) {

			if (dateList_Group3.get(i).equals(day)) {

				setClickSunday("false");

				errorMsg = day + " Reserved by Another Group";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				setCheckdate(true);

			}
		}

	}

	public void mainClear() {

		dateList_Group1.clear();
		dateList_Group2.clear();
		dateList_Group3.clear();

		group1_OD_List.clear();
		group2_OD_List.clear();
		group3_OD_List.clear();

		group1_DO_List.clear();
		group2_DO_List.clear();
		group3_DO_List.clear();

		panelGeneratorDTO = new PanelGeneratorNewDTO();
		setDisablepanel1(false);
		setGroupPanel(false);
		setTabelPanel(false);
		setDisablepanel2(false);

		setClickMonday("false");
		setClickTuesday("false");
		setClickWednesday("false");
		setClickThursday("false");
		setClickFriday("false");
		setClickSaturday("false");
		setClickSunday("false");
	}

	public void mainSave() {

		if (panelGeneratorDTO.getNoOfTimeTables().equals("1")) {
			if (group1_OD_List.size() != 0) {
				if (group1_DO_List.size() != 0) {
					if (dateList_Group1.size() != 0) {

						String user = sessionBackingBean.getLoginUser();
						String refNo = panelGeneratorService.generateRefNo();
						panelGeneratorDTO.setRefNo(refNo);

						int result = panelGeneratorService.insertPanel1(panelGeneratorDTO, user, refNo);

						int result2 = panelGeneratorService.group01_details(panelGeneratorDTO, user, group1_OD_List,
								group1_DO_List, dateList_Group1);

						if (result2 == 0) {
							setDisablePanel(true);
							setSucessMsg("Successfully Saved");
							;
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						}

					} else {
						errorMsg = "Group 1 Days are not selected";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Group 1 Destination to Origin table is incomplete";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Group 1 Origin to Destination table is incomplete";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		}

		if (panelGeneratorDTO.getNoOfTimeTables().equals("2")) {

			if (group1_OD_List.size() != 0) {
				if (group1_DO_List.size() != 0) {
					if (dateList_Group1.size() != 0) {
						if (group2_OD_List.size() != 0) {
							if (group2_DO_List.size() != 0) {
								if (dateList_Group2.size() != 0) {

									String user = sessionBackingBean.getLoginUser();
									String refNo = panelGeneratorService.generateRefNo();
									panelGeneratorDTO.setRefNo(refNo);

									int result = panelGeneratorService.insertPanel1(panelGeneratorDTO, user, refNo);

									int result2 = panelGeneratorService.group01_details(panelGeneratorDTO, user,
											group1_OD_List, group1_DO_List, dateList_Group1);
									int result3 = panelGeneratorService.group02_details(panelGeneratorDTO, user,
											group2_OD_List, group2_DO_List, dateList_Group2);

									if (result2 == 0 && result3 == 0) {
										setDisablePanel(true);
										setSucessMsg("Successfully Saved");
										;
										RequestContext.getCurrentInstance().update("frmsuccessSve");
										RequestContext.getCurrentInstance().execute("PF('successSve').show()");
									}

								} else {
									errorMsg = "Group 2 Days are not selected";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								errorMsg = "Group 2 Destination to Origin table is incomplete";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							errorMsg = "Group 2 Origin to Destination table is incomplete";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

						}
					} else {
						errorMsg = "Group 1 Days are not selected";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Group 1 Destination to Origin table is incomplete";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Group 1 Origin to Destination table is incomplete";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		}

		if (panelGeneratorDTO.getNoOfTimeTables().equals("3")) {

			if (group1_OD_List.size() != 0) {
				if (group1_DO_List.size() != 0) {
					if (dateList_Group1.size() != 0) {
						if (group2_OD_List.size() != 0) {
							if (group2_DO_List.size() != 0) {
								if (dateList_Group2.size() != 0) {
									if (group3_OD_List.size() != 0) {
										if (group3_DO_List.size() != 0) {
											if (dateList_Group3.size() != 0) {

												String user = sessionBackingBean.getLoginUser();
												String refNo = panelGeneratorService.generateRefNo();
												panelGeneratorDTO.setRefNo(refNo);

												int result = panelGeneratorService.insertPanel1(panelGeneratorDTO, user,
														refNo);

												int result2 = panelGeneratorService.group01_details(panelGeneratorDTO,
														user, group1_OD_List, group1_DO_List, dateList_Group1);
												int result3 = panelGeneratorService.group02_details(panelGeneratorDTO,
														user, group2_OD_List, group2_DO_List, dateList_Group2);
												int result4 = panelGeneratorService.group03_details(panelGeneratorDTO,
														user, group3_OD_List, group3_DO_List, dateList_Group3);

												if (result2 == 0 && result3 == 0 && result4 == 0) {
													setDisablePanel(true);
													setSucessMsg("Successfully Saved");
													;
													RequestContext.getCurrentInstance().update("frmsuccessSve");
													RequestContext.getCurrentInstance()
															.execute("PF('successSve').show()");
												}

											} else {
												errorMsg = "Group 3 Days are not selected";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else {
											errorMsg = "Group 3 Destination to Origin table is incomplete";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										errorMsg = "Group 3 Origin to Destination table is incomplete";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

									}
								} else {
									errorMsg = "Group 2 Days are not selected";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								errorMsg = "Group 2 Destination to Origin table is incomplete";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							errorMsg = "Group 2 Origin to Destination table is incomplete";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

						}
					} else {
						errorMsg = "Group 1 Days are not selected";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Group 1 Destination to Origin table is incomplete";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Group 1 Origin to Destination table is incomplete";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		}

	}

	public void saveOriginDestinationData() {
		panelGeneratorService.saveOriginDestinationData(this);
	}

//	public void generateTimeSlots() {
//
//		pvtBusCountOrigin = timeTableService.getPVTbusCount(timeTableDTO.getRouteNo(), "N",
//				timeTableDTO.getBusCategory());
//		pvtBusCountDestination = timeTableService.getPVTbusCount(timeTableDTO.getRouteNo(), "Y",
//				timeTableDTO.getBusCategory());
//		/** timeTableDTO.getBusCategory() added by tharushi.e **/
//		originPVTBusesList = new ArrayList<VehicleInspectionDTO>();
//		originPVTBusesList = timeTableService.getPVTbuses(timeTableDTO.getRouteNo(), "N");// origin
//																							// PVT
//																							// buses
//		destinatinoPVTBusesList = new ArrayList<VehicleInspectionDTO>();
//		destinatinoPVTBusesList = timeTableService.getPVTbuses(timeTableDTO.getRouteNo(), "Y");// destination
//																								// PVT
//																								// buses
//
//		if (pvtBusCountOrigin > 0) {
//
//			groupOneDTO.setNoOfPVTbuessOriginOne(pvtBusCountOrigin);
//
//			int PVT_O01 = groupOneDTO.getNoOfPVTbuessOriginOne();
//			groupOneDTO.setTotalBusesOriginOne(PVT_O01);
//
//			if (pvtBusCountDestination > 0) { 
//
//				groupOneDTO.setNoOfPVTbuessDestinationOne(pvtBusCountDestination);
//
//				int PVT_D01 = groupOneDTO.getNoOfPVTbuessDestinationOne();
//				groupOneDTO.setTotalBusesDestinationOne(PVT_D01);
//
//				restTimeString = timeTableService.getRestTime(timeTableDTO.getRouteNo());
//
//				if (restTimeString != null) {
//					String[] parts = restTimeString.split(":");
//
//					int hours = Integer.parseInt(parts[0]);
//					int minute = Integer.parseInt(parts[1]);
//
//					restTime = minute + (hours * 60);
//
//					groupOneDTO.setRestTimeOriginOne(restTime);
//					groupOneDTO.setRestTimeDestinationOne(restTime);
//
//				}
//
//				groupManagerForSave(groupCount);
//				editMode = false;
//
//			} else {
//
//				setErrorMessage("No. of PVT bus can not be zero for destination");
//				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//			}
//		} else {
//			setErrorMessage("No. of PVT bus can not be zero for origin");
//			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//		}
//
//	}

	public boolean isGroupPanel() {
		return groupPanel;
	}

	public void setGroupPanel(boolean groupPanel) {
		this.groupPanel = groupPanel;
	}

	public boolean isTabelPanel() {
		return tabelPanel;
	}

	public void setTabelPanel(boolean tabelPanel) {
		this.tabelPanel = tabelPanel;
	}

	public List<String> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<String> groupList) {
		this.groupList = groupList;
	}

	public boolean isGroup1() {
		return group1;
	}

	public void setGroup1(boolean group1) {
		this.group1 = group1;
	}

	public boolean isGroup2() {
		return group2;
	}

	public void setGroup2(boolean group2) {
		this.group2 = group2;
	}

	public boolean isGroup3() {
		return group3;
	}

	public void setGroup3(boolean group3) {
		this.group3 = group3;
	}

	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	public PanelGeneratorNewService getPanelGeneratorService() {
		return panelGeneratorService;
	}

	public void setPanelGeneratorService(PanelGeneratorNewService panelGeneratorService) {
		this.panelGeneratorService = panelGeneratorService;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<CommonDTO> getRoutefordropdownList() {
		return routefordropdownList;
	}

	public void setRoutefordropdownList(List<CommonDTO> routefordropdownList) {
		this.routefordropdownList = routefordropdownList;
	}

	public PanelGeneratorNewDTO getPanelGeneratorDTO() {
		return panelGeneratorDTO;
	}

	public void setPanelGeneratorDTO(PanelGeneratorNewDTO panelGeneratorDTO) {
		this.panelGeneratorDTO = panelGeneratorDTO;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public List<CommonDTO> getServiceTypedropdownList() {
		return serviceTypedropdownList;
	}

	public void setServiceTypedropdownList(List<CommonDTO> serviceTypedropdownList) {
		this.serviceTypedropdownList = serviceTypedropdownList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	public boolean isSunday() {
		return sunday;
	}

	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

	public String getClickMonday() {
		return clickMonday;
	}

	public void setClickMonday(String clickMonday) {
		this.clickMonday = clickMonday;
	}

	public String getClickTuesday() {
		return clickTuesday;
	}

	public void setClickTuesday(String clickTuesday) {
		this.clickTuesday = clickTuesday;
	}

	public String getClickWednesday() {
		return clickWednesday;
	}

	public void setClickWednesday(String clickWednesday) {
		this.clickWednesday = clickWednesday;
	}

	public String getClickThursday() {
		return clickThursday;
	}

	public void setClickThursday(String clickThursday) {
		this.clickThursday = clickThursday;
	}

	public String getClickFriday() {
		return clickFriday;
	}

	public void setClickFriday(String clickFriday) {
		this.clickFriday = clickFriday;
	}

	public String getClickSaturday() {
		return clickSaturday;
	}

	public void setClickSaturday(String clickSaturday) {
		this.clickSaturday = clickSaturday;
	}

	public String getClickSunday() {
		return clickSunday;
	}

	public void setClickSunday(String clickSunday) {
		this.clickSunday = clickSunday;
	}

	public boolean isAddBtn() {
		return addBtn;
	}

	public void setAddBtn(boolean addBtn) {
		this.addBtn = addBtn;
	}

	public boolean isDisablepanel1() {
		return disablepanel1;
	}

	public void setDisablepanel1(boolean disablepanel1) {
		this.disablepanel1 = disablepanel1;
	}

	public List<String> getDateList_Group1() {
		return dateList_Group1;
	}

	public void setDateList_Group1(List<String> dateList_Group1) {
		this.dateList_Group1 = dateList_Group1;
	}

	public List<String> getDateList_Group2() {
		return dateList_Group2;
	}

	public void setDateList_Group2(List<String> dateList_Group2) {
		this.dateList_Group2 = dateList_Group2;
	}

	public List<String> getDateList_Group3() {
		return dateList_Group3;
	}

	public void setDateList_Group3(List<String> dateList_Group3) {
		this.dateList_Group3 = dateList_Group3;
	}

	public List<String> getGroup1_OrigintoDestinationList() {
		return group1_OrigintoDestinationList;
	}

	public void setGroup1_OrigintoDestinationList(List<String> group1_OrigintoDestinationList) {
		this.group1_OrigintoDestinationList = group1_OrigintoDestinationList;
	}

	public List<String> getGroup2_OrigintoDestinationList() {
		return group2_OrigintoDestinationList;
	}

	public void setGroup2_OrigintoDestinationList(List<String> group2_OrigintoDestinationList) {
		this.group2_OrigintoDestinationList = group2_OrigintoDestinationList;
	}

	public List<String> getGroup3_OrigintoDestinationList() {
		return group3_OrigintoDestinationList;
	}

	public void setGroup3_OrigintoDestinationList(List<String> group3_OrigintoDestinationList) {
		this.group3_OrigintoDestinationList = group3_OrigintoDestinationList;
	}

	public List<String> getGroup1_DestinationtoOriginList() {
		return group1_DestinationtoOriginList;
	}

	public void setGroup1_DestinationtoOriginList(List<String> group1_DestinationtoOriginList) {
		this.group1_DestinationtoOriginList = group1_DestinationtoOriginList;
	}

	public List<String> getGroup2_DestinationtoOriginList() {
		return group2_DestinationtoOriginList;
	}

	public void setGroup2_DestinationtoOriginList(List<String> group2_DestinationtoOriginList) {
		this.group2_DestinationtoOriginList = group2_DestinationtoOriginList;
	}

	public List<String> getGroup3_DestinationtoOriginList() {
		return group3_DestinationtoOriginList;
	}

	public void setGroup3_DestinationtoOriginList(List<String> group3_DestinationtoOriginList) {
		this.group3_DestinationtoOriginList = group3_DestinationtoOriginList;
	}

	public List<PanelGeneratorNewDTO> getGroup1_OD_List() {
		return group1_OD_List;
	}

	public void setGroup1_OD_List(List<PanelGeneratorNewDTO> group1_OD_List) {
		this.group1_OD_List = group1_OD_List;
	}

	public List<PanelGeneratorNewDTO> getGroup2_OD_List() {
		return group2_OD_List;
	}

	public void setGroup2_OD_List(List<PanelGeneratorNewDTO> group2_OD_List) {
		this.group2_OD_List = group2_OD_List;
	}

	public List<PanelGeneratorNewDTO> getGroup3_OD_List() {
		return group3_OD_List;
	}

	public void setGroup3_OD_List(List<PanelGeneratorNewDTO> group3_OD_List) {
		this.group3_OD_List = group3_OD_List;
	}

	public List<PanelGeneratorNewDTO> getGroup1_DO_List() {
		return group1_DO_List;
	}

	public void setGroup1_DO_List(List<PanelGeneratorNewDTO> group1_DO_List) {
		this.group1_DO_List = group1_DO_List;
	}

	public List<PanelGeneratorNewDTO> getGroup2_DO_List() {
		return group2_DO_List;
	}

	public void setGroup2_DO_List(List<PanelGeneratorNewDTO> group2_DO_List) {
		this.group2_DO_List = group2_DO_List;
	}

	public List<PanelGeneratorNewDTO> getGroup3_DO_List() {
		return group3_DO_List;
	}

	public void setGroup3_DO_List(List<PanelGeneratorNewDTO> group3_DO_List) {
		this.group3_DO_List = group3_DO_List;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public boolean isCheckdate() {
		return checkdate;
	}

	public void setCheckdate(boolean checkdate) {
		this.checkdate = checkdate;
	}

	public String getWarningMsg() {
		return warningMsg;
	}

	public void setWarningMsg(String warningMsg) {
		this.warningMsg = warningMsg;
	}

	public boolean isErrorFound() {
		return errorFound;
	}

	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}

	public boolean isDisablepanel2() {
		return disablepanel2;
	}

	public void setDisablepanel2(boolean disablepanel2) {
		this.disablepanel2 = disablepanel2;
	}

	public boolean isDisablepanel3() {
		return disablepanel3;
	}

	public void setDisablepanel3(boolean disablepanel3) {
		this.disablepanel3 = disablepanel3;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isNoChangeTimeTable() {
		return noChangeTimeTable;
	}

	public void setNoChangeTimeTable(boolean noChangeTimeTable) {
		this.noChangeTimeTable = noChangeTimeTable;
	}

	public List<CommonDTO> getSelectedRouteList() {
		return selectedRouteList;
	}

	public void setSelectedRouteList(List<CommonDTO> selectedRouteList) {
		this.selectedRouteList = selectedRouteList;
	}

	public List<PanelGeneratorNewDTO> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(List<PanelGeneratorNewDTO> refNoList) {
		this.refNoList = refNoList;
	}

	public int getNoOfTripsOrigin() {
		return timeTableDTO.getNoOfTripsOrigin();
	}

	public void setNoOfTripsOrigin(int noOfTripsOrigin) {
		this.noOfTripsOrigin = timeTableDTO.getNoOfTripsOrigin();
	}



	public TimeTableService getTimeTableService() {
		return timeTableService;
	}

	public void setTimeTableService(TimeTableService timeTableService) {
		this.timeTableService = timeTableService;
	}

	public int getNoOfPvtBusesOrigin() {
		return noOfPvtBusesOrigin;
	}

	public void setNoOfPvtBusesOrigin(int noOfPvtBusesOrigin) {
		this.noOfPvtBusesOrigin = noOfPvtBusesOrigin;
	}

	public int getNoOfTemporaryBusesOrigin() {
		return noOfTemporaryBusesOrigin;
	}

	public void setNoOfTemporaryBusesOrigin(int noOfTemporaryBusesOrigin) {
		this.noOfTemporaryBusesOrigin = noOfTemporaryBusesOrigin;
	}

	public int getNoOfCtbOrigin() {
		return noOfCtbOrigin;
	}

	public void setNoOfCtbOrigin(int noOfCtbOrigin) {
		this.noOfCtbOrigin = noOfCtbOrigin;
	}

	public int getNoOfEtcOrigin() {
		return noOfEtcOrigin;
	}

	public void setNoOfEtcOrigin(int noOfEtcOrigin) {
		this.noOfEtcOrigin = noOfEtcOrigin;
	}

	public int getNoOfPvtLeaveBusesOrigin() {
		return noOfPvtLeaveBusesOrigin;
	}

	public void setNoOfPvtLeaveBusesOrigin(int noOfPvtLeaveBusesOrigin) {
		this.noOfPvtLeaveBusesOrigin = noOfPvtLeaveBusesOrigin;
	}

	public int getNoOfDummyBusesOrigin() {
		return noOfDummyBusesOrigin;
	}

	public void setNoOfDummyBusesOrigin(int noOfDummyBusesOrigin) {
		this.noOfDummyBusesOrigin = noOfDummyBusesOrigin;
	}

	public String getRestTimeOrigin() {
		return restTimeOrigin;
	}

	public void setRestTimeOrigin(String restTimeOrigin) {
		this.restTimeOrigin = restTimeOrigin;
	}

	public int getTotalBusesOrigin() {
		return totalBusesOrigin;
	}

	public void setTotalBusesOrigin(int totalBusesOrigin) {
		this.totalBusesOrigin = totalBusesOrigin;
	}

	public String getAbbreviationOrigin() {
		return abbreviationOrigin;
	}

	public void setAbbreviationOrigin(String abbreviationOrigin) {
		this.abbreviationOrigin = abbreviationOrigin;
	}

	public String getPermitNoOrigin() {
		return permitNoOrigin;
	}

	public void setPermitNoOrigin(String permitNoOrigin) {
		this.permitNoOrigin = permitNoOrigin;
	}

	public String getBusNoOrigin() {
		return busNoOrigin;
	}

	public void setBusNoOrigin(String nusNoOrigin) {
		this.busNoOrigin = nusNoOrigin;
	}

	public String getStartTimeOrigin() {
		return startTimeOrigin;
	}

	public void setStartTimeOrigin(String startTimeOrigin) {
		this.startTimeOrigin = startTimeOrigin;
	}

	public String getEndTimeOrigin() {
		return endTimeOrigin;
	}

	public void setEndTimeOrigin(String endTimeOrigin) {
		this.endTimeOrigin = endTimeOrigin;
	}

	public boolean isFixBusOrigin() {
		return fixBusOrigin;
	}

	public void setFixBusOrigin(boolean fixBusOrigin) {
		this.fixBusOrigin = fixBusOrigin;
	}

	public int getNoOfTripsDestination() {
		return noOfTripsDestination;
	}

	public void setNoOfTripsDestination(int noOfTripsDestination) {
		this.noOfTripsDestination = noOfTripsDestination;
	}

	public int getNoOfPvtBusesDestination() {
		return noOfPvtBusesDestination;
	}

	public void setNoOfPvtBusesDestination(int noOfPvtBusesDestination) {
		this.noOfPvtBusesDestination = noOfPvtBusesDestination;
	}

	public int getNoOfTemporaryBusesDestination() {
		return noOfTemporaryBusesDestination;
	}

	public void setNoOfTemporaryBusesDestination(int noOfTemporaryBusesDestination) {
		this.noOfTemporaryBusesDestination = noOfTemporaryBusesDestination;
	}

	public int getNoOfCtbDestination() {
		return noOfCtbDestination;
	}

	public void setNoOfCtbDestination(int noOfCtbDestination) {
		this.noOfCtbDestination = noOfCtbDestination;
	}

	public int getNoOfEtcDestination() {
		return noOfEtcDestination;
	}

	public void setNoOfEtcDestination(int noOfEtcDestination) {
		this.noOfEtcDestination = noOfEtcDestination;
	}

	public int getNoOfPvtLeaveBusesDestination() {
		return noOfPvtLeaveBusesDestination;
	}

	public void setNoOfPvtLeaveBusesDestination(int noOfPvtLeaveBusesDestination) {
		this.noOfPvtLeaveBusesDestination = noOfPvtLeaveBusesDestination;
	}

	public int getNoOfDummyBusesDestination() {
		return noOfDummyBusesDestination;
	}

	public void setNoOfDummyBusesDestination(int noOfDummyBusesDestination) {
		this.noOfDummyBusesDestination = noOfDummyBusesDestination;
	}

	public String getRestTimeDestination() {
		return restTimeDestination;
	}

	public void setRestTimeDestination(String restTimeDestination) {
		this.restTimeDestination = restTimeDestination;
	}

	public int getTotalBusesDestination() {
		return totalBusesDestination;
	}

	public void setTotalBusesDestination(int totalBusesDestination) {
		this.totalBusesDestination = totalBusesDestination;
	}

	public String getAbbreviationDestination() {
		return abbreviationDestination;
	}

	public void setAbbreviationDestination(String abbreviationDestination) {
		this.abbreviationDestination = abbreviationDestination;
	}

	public String getPermitNoDestination() {
		return permitNoDestination;
	}

	public void setPermitNoDestination(String permitNoDestination) {
		this.permitNoDestination = permitNoDestination;
	}

	public String getNusNoDestination() {
		return nusNoDestination;
	}

	public void setNusNoDestination(String nusNoDestination) {
		this.nusNoDestination = nusNoDestination;
	}

	public String getStartTimeDestination() {
		return startTimeDestination;
	}

	public void setStartTimeDestination(String startTimeDestination) {
		this.startTimeDestination = startTimeDestination;
	}

	public String getEndTimeDestination() {
		return endTimeDestination;
	}

	public void setEndTimeDestination(String endTimeDestination) {
		this.endTimeDestination = endTimeDestination;
	}

	public boolean isFixBusDestination() {
		return fixBusDestination;
	}

	public void setFixBusDestination(boolean fixBusDestination) {
		this.fixBusDestination = fixBusDestination;
	}

	public List<TimeTableDTO> getTripListOrigin() {
		return tripListOrigin;
	}

	public void setTripListOrigin(List<TimeTableDTO> tripListOrigin) {
		this.tripListOrigin = tripListOrigin;
	}

	public TimeTableDTO getTimeTableDTO() {
		return timeTableDTO;
	}

	public void setTimeTableDTO(TimeTableDTO timeTableDTO) {
		this.timeTableDTO = timeTableDTO;
	}
	public TimeTableDTO getTripsDTO() {
		return tripsDTO;
	}

	public void setTripsDTO(TimeTableDTO tripsDTO) {
		this.tripsDTO = tripsDTO;
	}

	public boolean isDisablePanel() {
		return disablePanel;
	}

	public void setDisablePanel(boolean disablePanel) {
		this.disablePanel = disablePanel;
	}

}
