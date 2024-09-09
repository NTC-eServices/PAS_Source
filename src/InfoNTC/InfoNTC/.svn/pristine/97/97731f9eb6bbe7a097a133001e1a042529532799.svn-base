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
import lk.informatics.ntc.model.dto.PanelGeneratorDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.PanelGeneratorService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "panelGeneratorBackingBean")
@ViewScoped

public class PanelGeneratorBackingBean {

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

	private boolean noChangeTimeTable;

	private RouteDTO routeDTO;

	// Services
	private PanelGeneratorService panelGeneratorService;
	private AdminService adminService;

	// Lists
	private List<String> groupList = new ArrayList<String>();
	private List<String> dateList_Group1 = new ArrayList<String>();
	private List<String> dateList_Group2 = new ArrayList<String>();
	private List<String> dateList_Group3 = new ArrayList<String>();

	private List<PanelGeneratorDTO> group1_OD_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group2_OD_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group3_OD_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group1_DO_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group2_DO_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group3_DO_List = new ArrayList<PanelGeneratorDTO>();

	private List<String> group1_OrigintoDestinationList = new ArrayList<String>();
	private List<String> group2_OrigintoDestinationList = new ArrayList<String>();
	private List<String> group3_OrigintoDestinationList = new ArrayList<String>();

	private List<String> group1_DestinationtoOriginList = new ArrayList<String>();
	private List<String> group2_DestinationtoOriginList = new ArrayList<String>();
	private List<String> group3_DestinationtoOriginList = new ArrayList<String>();

	private List<CommonDTO> selectedRouteList = new ArrayList<CommonDTO>();

	private List<CommonDTO> routefordropdownList;
	private List<CommonDTO> serviceTypedropdownList;

	// DTOs

	private PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();
	private boolean disablePanel;

	@PostConstruct
	public void init() {

		panelGeneratorService = (PanelGeneratorService) SpringApplicationContex.getBean("panelGeneratorService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");

		routefordropdownList = panelGeneratorService.getRoutesToDropdown();
		serviceTypedropdownList = adminService.getServiceTypeToDropdown();

		selectedRouteList = panelGeneratorService.getSelectedRoutes();

		dateList_Group1.clear();
		dateList_Group2.clear();
		dateList_Group3.clear();

		group1_OD_List.clear();
		group2_OD_List.clear();
		group3_OD_List.clear();

		group1_DO_List.clear();
		group2_DO_List.clear();
		group3_DO_List.clear();

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

		panelGeneratorDTO = new PanelGeneratorDTO();
		setDisablepanel1(false);
	}

	public void addPanel2() {

		setErrorFound(false);

		boolean haveRouteBusCategory = panelGeneratorService.getActiveDataRecord(panelGeneratorDTO.getRouteNo(),
				panelGeneratorDTO.getBusCategory(), panelGeneratorDTO.getSelectGroup(),panelGeneratorDTO.getNoOfTimeTables());

		if (!haveRouteBusCategory) {
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

//					if (panelGeneratorDTO.getNoOfTimeTables().equals("1")) {
//						if (dateList_Group1.size() < 7) {
//
//							errorMsg = "Please Select All the days to proceed";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//
//						}
//					} else 
					if (panelGeneratorDTO.getNoOfTimeTables().equals("2")) {
						if (dateList_Group1.size() == 0 && dateList_Group2.size() == 0) {

							errorMsg = "Please Select day/days to proceed";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);
						}
						if (dateList_Group1.size() == 0 && dateList_Group2.size() != 0) {

//							if (dateList_Group2.size() == 7) {
//								errorMsg = "Select 6 or less days";
//								RequestContext.getCurrentInstance().update("frmrequiredField");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//								setErrorFound(true);
//							} else {
							int total = 7;
							int remaining = total - dateList_Group2.size();

							if (remaining < 7) {
								warningMsg = "You have " + remaining + " days remaining";

								RequestContext.getCurrentInstance().update("frmwarningField");
								RequestContext.getCurrentInstance().execute("PF('warningField').show()");

							}
//							}

						}
						if (dateList_Group1.size() != 0 && dateList_Group2.size() == 0) {

//							if (dateList_Group1.size() == 7) {
//								errorMsg = "Select 6 or less days";
//								RequestContext.getCurrentInstance().update("frmrequiredField");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//								setErrorFound(true);
//							} else {
							int total = 7;
							int remaining = total - dateList_Group1.size();

							if (remaining < 7) {
								warningMsg = "You have " + remaining + " days remaining";

								RequestContext.getCurrentInstance().update("frmwarningField");
								RequestContext.getCurrentInstance().execute("PF('warningField').show()");
							}
//							}

						}
//						if (dateList_Group1.size() != 0 && dateList_Group2.size() != 0) {
//
//							int total = dateList_Group1.size() + dateList_Group2.size();
//
//							if (total < 7) {
//								errorMsg = "Please Select All the days to proceed";
//								RequestContext.getCurrentInstance().update("frmrequiredField");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//								setErrorFound(true);
//
//							}
//
//						}
					} else if (panelGeneratorDTO.getNoOfTimeTables().equals("3")) {
						if (dateList_Group1.size() == 0 && dateList_Group2.size() == 0 && dateList_Group3.size() == 0) {

							errorMsg = "Please Select day/days to proceed";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							setErrorFound(true);
						}
						if (dateList_Group1.size() == 0 && dateList_Group2.size() == 0 && dateList_Group3.size() != 0) {

//							if (dateList_Group3.size() == 7 || dateList_Group3.size() == 6) {
//								errorMsg = "Select 5 or less days";
//								RequestContext.getCurrentInstance().update("frmrequiredField");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//								setErrorFound(true);
//							} else {

							int remaining = 7 - dateList_Group3.size();

							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//							}

						}
						if (dateList_Group1.size() == 0 && dateList_Group2.size() != 0 && dateList_Group3.size() == 0) {

//							if (dateList_Group2.size() == 7 || dateList_Group2.size() == 6) {
//								errorMsg = "Select 5 or less days";
//								RequestContext.getCurrentInstance().update("frmrequiredField");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//								setErrorFound(true);
//							} else {

							int remaining = 7 - dateList_Group2.size();

							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//							}

						}

						if (dateList_Group1.size() != 0 && dateList_Group2.size() == 0 && dateList_Group3.size() == 0) {

//							if (dateList_Group1.size() == 7 || dateList_Group1.size() == 6) {
//								errorMsg = "Select 5 or less days";
//								RequestContext.getCurrentInstance().update("frmrequiredField");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//								setErrorFound(true);
//							} else {

							int remaining = 7 - dateList_Group1.size();

							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//							}
						}

//						if (dateList_Group1.size() == 0 && dateList_Group2.size() != 0 && dateList_Group3.size() != 0) {
//
//							int selectDays = dateList_Group2.size() + dateList_Group3.size();
//
//							if (selectDays == 7) {
//
//								errorMsg = "Please reduce a day/days";
//								RequestContext.getCurrentInstance().update("frmrequiredField");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//								setErrorFound(true);
//
//							} else {
//
//								int remaining = 7 - selectDays;
//								warningMsg = "You have " + remaining + " days remaining";
//
//								RequestContext.getCurrentInstance().update("frmwarningField");
//								RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//							}
//						}
//
//						if (dateList_Group1.size() != 0 && dateList_Group2.size() == 0 && dateList_Group3.size() != 0) {
//
//							int selectDays = dateList_Group1.size() + dateList_Group3.size();
//
//							if (selectDays == 7) {
//
//								errorMsg = "Please reduce a day/days";
//								RequestContext.getCurrentInstance().update("frmrequiredField");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//								setErrorFound(true);
//
//							} else {
//
//								int remaining = 7 - selectDays;
//								warningMsg = "You have " + remaining + " days remaining";
//
//								RequestContext.getCurrentInstance().update("frmwarningField");
//								RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//							}
//
//						}
//						if (dateList_Group1.size() != 0 && dateList_Group2.size() != 0 && dateList_Group3.size() == 0) {
//
//							int selectDays = dateList_Group1.size() + dateList_Group2.size();
//
//							if (selectDays == 7) {
//
//								errorMsg = "Please reduce a day/days";
//								RequestContext.getCurrentInstance().update("frmrequiredField");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//								setErrorFound(true);
//
//							} else {
//
//								int remaining = 7 - selectDays;
//								warningMsg = "You have " + remaining + " days remaining";
//
//								RequestContext.getCurrentInstance().update("frmwarningField");
//								RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//							}
//						}
//						if (dateList_Group1.size() != 0 && dateList_Group2.size() != 0 && dateList_Group3.size() != 0) {
//							int total = dateList_Group1.size() + dateList_Group2.size() + dateList_Group3.size();
//
//							if (total < 7) {
//								errorMsg = "Please Select All the days to proceed";
//								RequestContext.getCurrentInstance().update("frmrequiredField");
//								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//								setErrorFound(true);
//
//							}
//
//						}
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
		} else {
			RequestContext.getCurrentInstance().execute("PF('comfirmMSG').show()");
		}

	}
	
	public void inactiveData() {
		RequestContext.getCurrentInstance().execute("PF('comfirmMSG').hide()");
		
		String refNo = panelGeneratorService.getActiveRefNo(panelGeneratorDTO.getRouteNo(),
				panelGeneratorDTO.getBusCategory(), panelGeneratorDTO.getSelectGroup(),panelGeneratorDTO.getNoOfTimeTables());
		
		if(refNo != null) {
			boolean success = panelGeneratorService.UpdateStatus(refNo);
			boolean success1 = panelGeneratorService.UpdateStatusByRoute(panelGeneratorDTO.getRouteNo(),
				panelGeneratorDTO.getBusCategory(), panelGeneratorDTO.getSelectGroup());
			
			if(success && success1) {
				setSucessMsg("Status updated Successfully");
				;
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				
				addPanel2();
			}else {
				errorMsg = "Status update unsuccessful";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
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

		if (startTime != null) {

			if (endTime != null) {

				group1_OD_List.clear();

				PanelGeneratorDTO insertGroup1_OD = new PanelGeneratorDTO();

				insertGroup1_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
				insertGroup1_OD.setGroup01_OD_StartTime(panelGeneratorDTO.getGroup01_OD_StartTime());
				insertGroup1_OD.setGroup01_OD_EndTime(panelGeneratorDTO.getGroup01_OD_EndTime());

				group1_OD_List.add(insertGroup1_OD);

				setSucessMsg("Successfully Saved");
				;
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

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

	public void clearGroup01_OriginToDestination() {
		panelGeneratorDTO.setGroup01_OD_StartTime(null);
		panelGeneratorDTO.setGroup01_OD_EndTime(null);
	}

	public void saveGroup02_OriginToDestination() {

		if (panelGeneratorDTO.getGroup02_OD_StartTime() != null) {
			if (panelGeneratorDTO.getGroup02_OD_EndTime() != null) {

				group2_OD_List.clear();

				PanelGeneratorDTO insertGroup2_OD = new PanelGeneratorDTO();

				insertGroup2_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
				insertGroup2_OD.setGroup02_OD_StartTime(panelGeneratorDTO.getGroup02_OD_StartTime());
				insertGroup2_OD.setGroup02_OD_EndTime(panelGeneratorDTO.getGroup02_OD_EndTime());

				group2_OD_List.add(insertGroup2_OD);

				setSucessMsg("Successfully Saved");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

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

	public void clearGroup02_OriginToDestination() {
		panelGeneratorDTO.setGroup02_OD_StartTime(null);
		panelGeneratorDTO.setGroup02_OD_EndTime(null);
	}

	public void saveGroup03_OriginToDestination() {

		if (panelGeneratorDTO.getGroup03_OD_StartTime() != null) {
			if (panelGeneratorDTO.getGroup03_OD_EndTime() != null) {

				group3_OD_List.clear();

				PanelGeneratorDTO insertGroup3_OD = new PanelGeneratorDTO();

				insertGroup3_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
				insertGroup3_OD.setGroup03_OD_StartTime(panelGeneratorDTO.getGroup03_OD_StartTime());
				insertGroup3_OD.setGroup03_OD_EndTime(panelGeneratorDTO.getGroup03_OD_EndTime());

				group3_OD_List.add(insertGroup3_OD);

				setSucessMsg("Successfully Saved");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

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

	public void clearGroup03_OriginToDestination() {
		panelGeneratorDTO.setGroup03_OD_StartTime(null);
		panelGeneratorDTO.setGroup03_OD_EndTime(null);
	}

	public void saveGroup01_DestinationToOrigin() {

		if (panelGeneratorDTO.getGroup01_DO_StartTime() != null) {
			if (panelGeneratorDTO.getGroup01_DO_EndTime() != null) {

				group1_DO_List.clear();
				PanelGeneratorDTO insertGroup1_DO = new PanelGeneratorDTO();

				insertGroup1_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
				insertGroup1_DO.setGroup01_DO_StartTime(panelGeneratorDTO.getGroup01_DO_StartTime());
				insertGroup1_DO.setGroup01_DO_EndTime(panelGeneratorDTO.getGroup01_DO_EndTime());

				group1_DO_List.add(insertGroup1_DO);

				setSucessMsg("Successfully Saved");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

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

	public void clearGroup01_DestinationToOrigin() {
		panelGeneratorDTO.setGroup01_DO_StartTime(null);
		panelGeneratorDTO.setGroup01_DO_EndTime(null);
	}

	public void saveGroup02_DestinationToOrigin() {

		if (panelGeneratorDTO.getGroup02_DO_StartTime() != null) {
			if (panelGeneratorDTO.getGroup02_DO_EndTime() != null) {

				group2_DO_List.clear();
				PanelGeneratorDTO insertGroup2_DO = new PanelGeneratorDTO();

				insertGroup2_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
				insertGroup2_DO.setGroup02_DO_StartTime(panelGeneratorDTO.getGroup02_DO_StartTime());
				insertGroup2_DO.setGroup02_DO_EndTime(panelGeneratorDTO.getGroup02_DO_EndTime());

				group2_DO_List.add(insertGroup2_DO);

				setSucessMsg("Successfully Saved");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

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

	public void clearGroup02_DestinationToOrigin() {
		panelGeneratorDTO.setGroup02_DO_StartTime(null);
		panelGeneratorDTO.setGroup02_DO_EndTime(null);
	}

	public void saveGroup03_DestinationToOrigin() {

		if (panelGeneratorDTO.getGroup03_DO_StartTime() != null) {
			if (panelGeneratorDTO.getGroup03_DO_EndTime() != null) {

				group3_DO_List.clear();
				PanelGeneratorDTO insertGroup3_DO = new PanelGeneratorDTO();

				insertGroup3_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
				insertGroup3_DO.setGroup03_DO_StartTime(panelGeneratorDTO.getGroup03_DO_StartTime());
				insertGroup3_DO.setGroup03_DO_EndTime(panelGeneratorDTO.getGroup03_DO_EndTime());

				group3_DO_List.add(insertGroup3_DO);

				setSucessMsg("Successfully Saved");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

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

	public void clearGroup03_DestinationToOrigin() {
		panelGeneratorDTO.setGroup03_DO_StartTime(null);
		panelGeneratorDTO.setGroup03_DO_EndTime(null);
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
		}

		if (group1_DO_List.size() == 0) {
			panelGeneratorDTO.setGroup01_DO_StartTime(null);
			panelGeneratorDTO.setGroup01_DO_EndTime(null);
		}

		if (group2_OD_List.size() == 0) {

			panelGeneratorDTO.setGroup02_OD_StartTime(null);
			panelGeneratorDTO.setGroup02_OD_EndTime(null);
		}

		if (group2_DO_List.size() == 0) {
			panelGeneratorDTO.setGroup02_DO_StartTime(null);
			panelGeneratorDTO.setGroup02_DO_EndTime(null);
		}

		if (group3_OD_List.size() == 0) {

			panelGeneratorDTO.setGroup03_OD_StartTime(null);
			panelGeneratorDTO.setGroup03_OD_EndTime(null);
		}

		if (group3_DO_List.size() == 0) {
			panelGeneratorDTO.setGroup03_DO_StartTime(null);
			panelGeneratorDTO.setGroup03_DO_EndTime(null);
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

		panelGeneratorDTO = new PanelGeneratorDTO();
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

	
	/* Changed by dhananjika.d 14/03/2024 */
	public void mainSave() {
		if (panelGeneratorDTO.getSelectGroup().equals("1")) {
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
							disablePanel = true;
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
		if (panelGeneratorDTO.getSelectGroup().equals("2")) {
			if (group2_OD_List.size() != 0) {
				if (group2_DO_List.size() != 0) {
					if (dateList_Group2.size() != 0) {

						String user = sessionBackingBean.getLoginUser();
						String refNo = panelGeneratorService.generateRefNo();
						panelGeneratorDTO.setRefNo(refNo);

						int result = panelGeneratorService.insertPanel1(panelGeneratorDTO, user, refNo);

						int result3 = panelGeneratorService.group02_details(panelGeneratorDTO, user, group2_OD_List,
								group2_DO_List, dateList_Group2);

						if (result3 == 0) {
							disablePanel = true;
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
		}
		if (panelGeneratorDTO.getSelectGroup().equals("3")) {
			if (group3_OD_List.size() != 0) {
				if (group3_DO_List.size() != 0) {
					if (dateList_Group3.size() != 0) {

						String user = sessionBackingBean.getLoginUser();
						String refNo = panelGeneratorService.generateRefNo();
						panelGeneratorDTO.setRefNo(refNo);

						int result = panelGeneratorService.insertPanel1(panelGeneratorDTO, user, refNo);

						int result4 = panelGeneratorService.group03_details(panelGeneratorDTO, user, group3_OD_List,
								group3_DO_List, dateList_Group3);

						if (result4 == 0) {
							disablePanel = true;
							setSucessMsg("Successfully Saved");
							;
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						}

					} else {
						errorMsg = "Group 3 Days are not selected";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
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
		}

	
	}

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

	public PanelGeneratorService getPanelGeneratorService() {
		return panelGeneratorService;
	}

	public void setPanelGeneratorService(PanelGeneratorService panelGeneratorService) {
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

	public PanelGeneratorDTO getPanelGeneratorDTO() {
		return panelGeneratorDTO;
	}

	public void setPanelGeneratorDTO(PanelGeneratorDTO panelGeneratorDTO) {
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

	public List<PanelGeneratorDTO> getGroup1_OD_List() {
		return group1_OD_List;
	}

	public void setGroup1_OD_List(List<PanelGeneratorDTO> group1_OD_List) {
		this.group1_OD_List = group1_OD_List;
	}

	public List<PanelGeneratorDTO> getGroup2_OD_List() {
		return group2_OD_List;
	}

	public void setGroup2_OD_List(List<PanelGeneratorDTO> group2_OD_List) {
		this.group2_OD_List = group2_OD_List;
	}

	public List<PanelGeneratorDTO> getGroup3_OD_List() {
		return group3_OD_List;
	}

	public void setGroup3_OD_List(List<PanelGeneratorDTO> group3_OD_List) {
		this.group3_OD_List = group3_OD_List;
	}

	public List<PanelGeneratorDTO> getGroup1_DO_List() {
		return group1_DO_List;
	}

	public void setGroup1_DO_List(List<PanelGeneratorDTO> group1_DO_List) {
		this.group1_DO_List = group1_DO_List;
	}

	public List<PanelGeneratorDTO> getGroup2_DO_List() {
		return group2_DO_List;
	}

	public void setGroup2_DO_List(List<PanelGeneratorDTO> group2_DO_List) {
		this.group2_DO_List = group2_DO_List;
	}

	public List<PanelGeneratorDTO> getGroup3_DO_List() {
		return group3_DO_List;
	}

	public void setGroup3_DO_List(List<PanelGeneratorDTO> group3_DO_List) {
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

	public boolean isDisablePanel() {
		return disablePanel;
	}

	public void setDisablePanel(boolean disablePanel) {
		this.disablePanel = disablePanel;
	}

}
