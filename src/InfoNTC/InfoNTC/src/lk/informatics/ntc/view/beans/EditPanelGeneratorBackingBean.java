package lk.informatics.ntc.view.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.PanelGeneratorDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.PanelGeneratorService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "editPanelGeneratorBackingBean")
@ViewScoped
public class EditPanelGeneratorBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	// Services
	private PanelGeneratorService panelGeneratorService;
	private AdminService adminService;

	private PanelGeneratorDTO panelGeneratorDTO = new PanelGeneratorDTO();

	private List<PanelGeneratorDTO> refNoList;
	private List<PanelGeneratorDTO> tripGeneratorList;

	private List<String> groupList = new ArrayList<String>();

	private List<String> panel1_List = new ArrayList<String>();

	private List<String> dateList_Group1 = new ArrayList<String>();
	private List<String> dateList_Group2 = new ArrayList<String>();
	private List<String> dateList_Group3 = new ArrayList<String>();

	private List<PanelGeneratorDTO> group1_OD_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group2_OD_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group3_OD_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group1_DO_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group2_DO_List = new ArrayList<PanelGeneratorDTO>();
	private List<PanelGeneratorDTO> group3_DO_List = new ArrayList<PanelGeneratorDTO>();

	private boolean tabelPanel;
	private boolean addBtn;
	private boolean disablePanel1;
	private boolean disablePanel2;
	private boolean disablePanel3;

	private boolean group1;
	private boolean group2;
	private boolean group3;

	private String clickMonday;
	private String clickTuesday;
	private String clickWednesday;
	private String clickThursday;
	private String clickFriday;
	private String clickSaturday;
	private String clickSunday;

	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;

	private boolean errorFound;

	private RouteDTO routeDTO;

	private String oldNoOfTimeTables;
	private String oldStatus;
	private String oldRoute;
	private String oldService;
	private String errorMsg;
	private String warningMsg;
	private String sucessMsg;

	private boolean noChangeTimeTable;
	private boolean changeTimeTable;

	@PostConstruct
	public void init() {
		panelGeneratorService = (PanelGeneratorService) SpringApplicationContex.getBean("panelGeneratorService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");

		refNoList = panelGeneratorService.getRefNoList();
		tripGeneratorList = panelGeneratorService.getTripGeneratorList();

		for (int i = 0; i < tripGeneratorList.size(); i++) {
			for (int j = 0; j < refNoList.size(); j++) {

				if (tripGeneratorList.get(i).getRefNo().equals(refNoList.get(j).getRefNo())) {

					refNoList.remove(j);
				}
			}
		}

		setDisablePanel2(true);
		setDisablePanel3(true);
		setGroup1(true);
		setGroup2(true);
		setGroup3(true);

	}

	public void generateDetails() {

		clearLists();
		clearTables();

		panelGeneratorDTO = panelGeneratorService.getDetails(panelGeneratorDTO.getRefNo(), panelGeneratorDTO);
		setOldNoOfTimeTables(panelGeneratorDTO.getNoOfTimeTables());
		setOldStatus(panelGeneratorDTO.getPanel1Status());
		setOldRoute(panelGeneratorDTO.getRouteNo());
		setOldService(panelGeneratorDTO.getBusCategory());
		String route_no = panelGeneratorDTO.getRouteNo();
		routeDTO = adminService.getDetailsbyRouteNo(route_no);
		panelGeneratorDTO.setOrigin(routeDTO.getOrigin());
		panelGeneratorDTO.setDestination(routeDTO.getDestination());

		dateList_Group1 = panelGeneratorService.getDateList_Group1(panelGeneratorDTO.getSeqNo(),panelGeneratorDTO.getRefNo());
		dateList_Group2 = panelGeneratorService.getDateList_Group2(panelGeneratorDTO.getSeqNo(),panelGeneratorDTO.getRefNo());
		dateList_Group3 = panelGeneratorService.getDateList_Group3(panelGeneratorDTO.getSeqNo(),panelGeneratorDTO.getRefNo());

		if (panelGeneratorDTO.getNoOfTimeTables().equals("1")) {

			group1_OD_List = panelGeneratorService.getODList_Group1(panelGeneratorDTO.getSeqNo());
			group1_DO_List = panelGeneratorService.getDOList_Group1(panelGeneratorDTO.getSeqNo());

			panelGeneratorDTO.setGroup01_OD_StartTime(group1_OD_List.get(0).getGroup01_OD_StartTime());
			panelGeneratorDTO.setGroup01_OD_EndTime(group1_OD_List.get(0).getGroup01_OD_EndTime());

			panelGeneratorDTO.setGroup01_DO_StartTime(group1_DO_List.get(0).getGroup01_DO_StartTime());
			panelGeneratorDTO.setGroup01_DO_EndTime(group1_DO_List.get(0).getGroup01_DO_EndTime());
		}

		if (panelGeneratorDTO.getNoOfTimeTables().equals("2")) {

			group1_OD_List = panelGeneratorService.getODList_Group1(panelGeneratorDTO.getSeqNo());
			group1_DO_List = panelGeneratorService.getDOList_Group1(panelGeneratorDTO.getSeqNo());
			group2_OD_List = panelGeneratorService.getODList_Group2(panelGeneratorDTO.getSeqNo());
			group2_DO_List = panelGeneratorService.getDOList_Group2(panelGeneratorDTO.getSeqNo());
			
			if(!group1_OD_List.isEmpty() || !group1_DO_List.isEmpty()) {
				panelGeneratorDTO.setGroup01_OD_StartTime(group1_OD_List.get(0).getGroup01_OD_StartTime());
				panelGeneratorDTO.setGroup01_OD_EndTime(group1_OD_List.get(0).getGroup01_OD_EndTime());

				panelGeneratorDTO.setGroup01_DO_StartTime(group1_DO_List.get(0).getGroup01_DO_StartTime());
				panelGeneratorDTO.setGroup01_DO_EndTime(group1_DO_List.get(0).getGroup01_DO_EndTime());
			}
			if(!group2_OD_List.isEmpty() || !group2_DO_List.isEmpty()) {
				panelGeneratorDTO.setGroup02_OD_StartTime(group2_OD_List.get(0).getGroup02_OD_StartTime());
				panelGeneratorDTO.setGroup02_OD_EndTime(group2_OD_List.get(0).getGroup02_OD_EndTime());

				panelGeneratorDTO.setGroup02_DO_StartTime(group2_DO_List.get(0).getGroup02_DO_StartTime());
				panelGeneratorDTO.setGroup02_DO_EndTime(group2_DO_List.get(0).getGroup02_DO_EndTime());
			}	

		}

		if (panelGeneratorDTO.getNoOfTimeTables().equals("3")) {

			group1_OD_List = panelGeneratorService.getODList_Group1(panelGeneratorDTO.getSeqNo());
			group1_DO_List = panelGeneratorService.getDOList_Group1(panelGeneratorDTO.getSeqNo());
			group2_OD_List = panelGeneratorService.getODList_Group2(panelGeneratorDTO.getSeqNo());
			group2_DO_List = panelGeneratorService.getDOList_Group2(panelGeneratorDTO.getSeqNo());
			group3_OD_List = panelGeneratorService.getODList_Group3(panelGeneratorDTO.getSeqNo());
			group3_DO_List = panelGeneratorService.getDOList_Group3(panelGeneratorDTO.getSeqNo());

			if(!group1_OD_List.isEmpty() || !group1_DO_List.isEmpty()) {
				panelGeneratorDTO.setGroup01_OD_StartTime(group1_OD_List.get(0).getGroup01_OD_StartTime());
				panelGeneratorDTO.setGroup01_OD_EndTime(group1_OD_List.get(0).getGroup01_OD_EndTime());

				panelGeneratorDTO.setGroup01_DO_StartTime(group1_DO_List.get(0).getGroup01_DO_StartTime());
				panelGeneratorDTO.setGroup01_DO_EndTime(group1_DO_List.get(0).getGroup01_DO_EndTime());
			}
			if(!group2_OD_List.isEmpty() || !group2_DO_List.isEmpty()) {
				panelGeneratorDTO.setGroup02_OD_StartTime(group2_OD_List.get(0).getGroup02_OD_StartTime());
				panelGeneratorDTO.setGroup02_OD_EndTime(group2_OD_List.get(0).getGroup02_OD_EndTime());

				panelGeneratorDTO.setGroup02_DO_StartTime(group2_DO_List.get(0).getGroup02_DO_StartTime());
				panelGeneratorDTO.setGroup02_DO_EndTime(group2_DO_List.get(0).getGroup02_DO_EndTime());
			}
			if (!group3_OD_List.isEmpty() || !group3_DO_List.isEmpty()) {

				panelGeneratorDTO.setGroup03_OD_StartTime(group3_OD_List.get(0).getGroup03_OD_StartTime());
				panelGeneratorDTO.setGroup03_OD_EndTime(group3_OD_List.get(0).getGroup03_OD_EndTime());

				panelGeneratorDTO.setGroup03_DO_StartTime(group3_DO_List.get(0).getGroup03_DO_StartTime());
				panelGeneratorDTO.setGroup03_DO_EndTime(group3_DO_List.get(0).getGroup03_DO_EndTime());
			}
		}
		
		if(panelGeneratorDTO.getSelectGroup().equals("1")) {
			if(dateList_Group1.contains("Monday")) {
				setClickMonday("true");
			}
			if(dateList_Group1.contains("Tuesday")) {
				setClickTuesday("true");
			}
			if(dateList_Group1.contains("Wednesday")) {
				setClickWednesday("true");
			}
			if(dateList_Group1.contains("Thursday")) {
				setClickThursday("true");
			}
			if(dateList_Group1.contains("Friday")) {
				setClickFriday("true");
			}
			if(dateList_Group1.contains("Saturday")) {
				setClickSaturday("true");
			}
			if(dateList_Group1.contains("Sunday")) {
				setClickSunday("true");
			}
		}else if(panelGeneratorDTO.getSelectGroup().equals("2")) {
			if(dateList_Group2.contains("Monday")) {
				setClickMonday("true");
			}
			if(dateList_Group2.contains("Tuesday")) {
				setClickTuesday("true");
			}
			if(dateList_Group2.contains("Wednesday")) {
				setClickWednesday("true");
			}
			if(dateList_Group2.contains("Thursday")) {
				setClickThursday("true");
			}
			if(dateList_Group2.contains("Friday")) {
				setClickFriday("true");
			}
			if(dateList_Group2.contains("Saturday")) {
				setClickSaturday("true");
			}
			if(dateList_Group2.contains("Sunday")) {
				setClickSunday("true");
			}
		}else if(panelGeneratorDTO.getSelectGroup().equals("3")) {
			if(dateList_Group3.contains("Monday")) {
				setClickMonday("true");
			}
			if(dateList_Group3.contains("Tuesday")) {
				setClickTuesday("true");
			}
			if(dateList_Group3.contains("Wednesday")) {
				setClickWednesday("true");
			}
			if(dateList_Group3.contains("Thursday")) {
				setClickThursday("true");
			}
			if(dateList_Group3.contains("Friday")) {
				setClickFriday("true");
			}
			if(dateList_Group3.contains("Saturday")) {
				setClickSaturday("true");
			}
			if(dateList_Group3.contains("Sunday")) {
				setClickSunday("true");
			}
		}
	}
	
	public void generateOriginAndDestination() {

		boolean found = false;
		String route_no = panelGeneratorDTO.getRouteNo();

		routeDTO = adminService.getDetailsbyRouteNo(route_no);

		panelGeneratorDTO.setOrigin(routeDTO.getOrigin());
		panelGeneratorDTO.setDestination(routeDTO.getDestination());

	}

	public void updatePanel1() {

		if (panelGeneratorDTO.getNoOfTimeTables().equals(oldNoOfTimeTables)) {
			String user = sessionBackingBean.getLoginUser();

			if ((!panelGeneratorDTO.getPanel1Status().equals(oldStatus)) || (!panelGeneratorDTO.getRouteNo().equals(oldRoute))
					||(!panelGeneratorDTO.getBusCategory().equals(oldService) )) {
				int status = panelGeneratorService.updateStatus(panelGeneratorDTO.getPanel1Status(), user,
						panelGeneratorDTO.getRefNo(),panelGeneratorDTO.getRouteNo(),panelGeneratorDTO.getBusCategory());
			}

			if (panelGeneratorDTO.getNoOfTimeTables().equals("1")) {
				setGroup1(false);
				setGroup2(true);
				setGroup3(true);

			} else if (panelGeneratorDTO.getNoOfTimeTables().equals("2")) {
				setGroup1(false);
				setGroup2(false);
				setGroup3(true);
			} else if (panelGeneratorDTO.getNoOfTimeTables().equals("3")) {
				setGroup1(false);
				setGroup2(false);
				setGroup3(false);
			}

//			panelGeneratorDTO.setSelectGroup("1");
			setDisablePanel1(true);
			setDisablePanel2(false);
			setDisablePanel3(false);

			setNoChangeTimeTable(true);

			if(panelGeneratorDTO.getSelectGroup().equals("1")) {
				for (int f = 0; f < dateList_Group1.size(); f++) {

					String day = dateList_Group1.get(f);

					if (day.equals("Monday")) {
						setClickMonday("true");
						setMonday(true);
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
			}if(panelGeneratorDTO.getSelectGroup().equals("2")) {
				for (int f = 0; f < dateList_Group2.size(); f++) {

					String day = dateList_Group2.get(f);

					if (day.equals("Monday")) {
						setClickMonday("true");
						setMonday(true);
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
			}if(panelGeneratorDTO.getSelectGroup().equals("3")) {
				for (int f = 0; f < dateList_Group3.size(); f++) {

					String day = dateList_Group3.get(f);

					if (day.equals("Monday")) {
						setClickMonday("true");
						setMonday(true);
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
			

			groupList.clear();
			int timetables = Integer.parseInt(panelGeneratorDTO.getNoOfTimeTables());
			for (int i = 0; i < timetables; i++) {

				int j = i + 1;
				String number = new Integer(j).toString();

				groupList.add(number);

			}

		} else {

			clearLists();

			setDisablePanel1(true);
			setDisablePanel2(false);
			setDisablePanel3(true);

			setChangeTimeTable(true);

			clearTables();

			panelGeneratorDTO.setSelectGroup("");

			int timetables = Integer.parseInt(panelGeneratorDTO.getNoOfTimeTables());
			for (int i = 0; i < timetables; i++) {

				int j = i + 1;
				String number = new Integer(j).toString();

				groupList.add(number);

			}

		}

	}

	public void clearLists() {

		dateList_Group1.clear();
		dateList_Group2.clear();
		dateList_Group3.clear();

		group1_OD_List.clear();
		group2_OD_List.clear();
		group3_OD_List.clear();

		group1_DO_List.clear();
		group2_DO_List.clear();
		group3_DO_List.clear();
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

			} else {

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

			} else {

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

			} else {

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

		if (clickMonday.equals("true")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					setClickMonday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					setClickMonday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					setClickMonday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			if (panelGeneratorDTO.getSelectGroup().equals("1") && clickMonday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group1.add(day);
			}
			if (panelGeneratorDTO.getSelectGroup().equals("2") && clickMonday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group2.add(day);
			}
			if (panelGeneratorDTO.getSelectGroup().equals("3") && clickMonday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group3.add(day);
			}

		} else if (clickMonday.equals("false")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					dateList_Group1.remove(day);

					warningMsg = "Add Monday to a Group";

					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					dateList_Group2.remove(day);

					warningMsg = "Add Monday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					dateList_Group3.remove(day);

					warningMsg = "Add Monday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");
				}
			}

		}

		int total = dateList_Group1.size() + dateList_Group2.size() + dateList_Group3.size();

	}

	public void checkTuesday() {

		String day = "Tuesday";

		if (clickTuesday.equals("true")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					setClickTuesday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					setClickTuesday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					setClickTuesday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			if (panelGeneratorDTO.getSelectGroup().equals("1") && clickTuesday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group1.add(day);
			}
			if (panelGeneratorDTO.getSelectGroup().equals("2") && clickTuesday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group2.add(day);
			}
			if (panelGeneratorDTO.getSelectGroup().equals("3") && clickTuesday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group3.add(day);
			}

		} else if (clickTuesday.equals("false")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					dateList_Group1.remove(day);

					warningMsg = "Add Tuesday to a Group";

					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					dateList_Group2.remove(day);

					warningMsg = "Add Tuesday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					dateList_Group3.remove(day);

					warningMsg = "Add Tuesday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");
				}
			}

		}

		int total = dateList_Group1.size() + dateList_Group2.size() + dateList_Group3.size();

	}

	public void checkWednesday() {

		String day = "Wednesday";

		if (clickWednesday.equals("true")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					setClickWednesday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					setClickWednesday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					setClickWednesday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			if (panelGeneratorDTO.getSelectGroup().equals("1") && clickWednesday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group1.add(day);
			}
			if (panelGeneratorDTO.getSelectGroup().equals("2") && clickWednesday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group2.add(day);
			}
			if (panelGeneratorDTO.getSelectGroup().equals("3") && clickWednesday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group3.add(day);
			}

		} else if (clickWednesday.equals("false")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					dateList_Group1.remove(day);

					warningMsg = "Add Wednesday to a Group";

					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					dateList_Group2.remove(day);

					warningMsg = "Add Wednesday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					dateList_Group3.remove(day);

					warningMsg = "Add Wednesday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");
				}
			}

		}

		int total = dateList_Group1.size() + dateList_Group2.size() + dateList_Group3.size();

	}

	public void checkThursday() {

		String day = "Thursday";

		if (clickThursday.equals("true")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					setClickThursday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					setClickThursday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					setClickThursday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			if (panelGeneratorDTO.getSelectGroup().equals("1") && clickThursday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group1.add(day);
			}

			if (panelGeneratorDTO.getSelectGroup().equals("2") && clickThursday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group2.add(day);
			}
			if (panelGeneratorDTO.getSelectGroup().equals("3") && clickThursday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group3.add(day);
			}

		} else if (clickThursday.equals("false")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					dateList_Group1.remove(day);

					warningMsg = "Add Thursday to a Group";

					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					dateList_Group2.remove(day);

					warningMsg = "Add Thursday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					dateList_Group3.remove(day);

					warningMsg = "Add Thursday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");
				}
			}

		}

		int total = dateList_Group1.size() + dateList_Group2.size() + dateList_Group3.size();

	}

	public void checkFriday() {

		String day = "Friday";

		if (clickFriday.equals("true")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					setClickFriday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					setClickFriday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					setClickFriday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			if (panelGeneratorDTO.getSelectGroup().equals("1") && clickFriday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group1.add(day);
			}

			if (panelGeneratorDTO.getSelectGroup().equals("2") && clickFriday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group2.add(day);
			}

			if (panelGeneratorDTO.getSelectGroup().equals("3") && clickFriday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group3.add(day);
			}

		} else if (clickFriday.equals("false")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					dateList_Group1.remove(day);

					warningMsg = "Add Friday to a Group";

					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					dateList_Group2.remove(day);

					warningMsg = "Add Friday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					dateList_Group3.remove(day);

					warningMsg = "Add Friday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");
				}
			}

		}

		int total = dateList_Group1.size() + dateList_Group2.size() + dateList_Group3.size();

	}

	public void checkSaturday() {

		String day = "Saturday";

		if (clickSaturday.equals("true")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					setClickSaturday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					setClickSaturday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					setClickSaturday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			if (panelGeneratorDTO.getSelectGroup().equals("1") && clickSaturday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group1.add(day);
			}

			if (panelGeneratorDTO.getSelectGroup().equals("2") && clickSaturday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group2.add(day);
			}

			if (panelGeneratorDTO.getSelectGroup().equals("3") && clickSaturday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group3.add(day);
			}

		} else if (clickSaturday.equals("false")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					dateList_Group1.remove(day);

					warningMsg = "Add Saturday to a Group";

					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					dateList_Group2.remove(day);

					warningMsg = "Add Saturday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					dateList_Group3.remove(day);

					warningMsg = "Add Saturday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");
				}
			}

		}

		int total = dateList_Group1.size() + dateList_Group2.size() + dateList_Group3.size();

	}

	public void checkSunday() {

		String day = "Sunday";

		if (clickSunday.equals("true")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					setClickSunday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}

			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					setClickSunday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					setClickSunday("false");

					errorMsg = day + " Reserved by Another Group";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

				}
			}

			if (panelGeneratorDTO.getSelectGroup().equals("1") && clickSunday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group1.add(day);
			}

			if (panelGeneratorDTO.getSelectGroup().equals("2") && clickSunday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group2.add(day);
			}

			if (panelGeneratorDTO.getSelectGroup().equals("3") && clickSunday.equals("true")
					&& noChangeTimeTable == true) {

				dateList_Group3.add(day);
			}

		} else if (clickSunday.equals("false")) {

			for (int i = 0; i < dateList_Group1.size(); i++) {

				if (dateList_Group1.get(i).equals(day)) {

					dateList_Group1.remove(day);

					warningMsg = "Add Sunday to a Group";

					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group2.size(); i++) {

				if (dateList_Group2.get(i).equals(day)) {

					dateList_Group2.remove(day);

					warningMsg = "Add Sunday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");

				}
			}

			for (int i = 0; i < dateList_Group3.size(); i++) {

				if (dateList_Group3.get(i).equals(day)) {

					dateList_Group3.remove(day);

					warningMsg = "Add Sunday to a Group";
					RequestContext.getCurrentInstance().update("frmwarningField");
					RequestContext.getCurrentInstance().execute("PF('warningField').show()");
				}
			}

		}

		int total = dateList_Group1.size() + dateList_Group2.size() + dateList_Group3.size();

	}

	public void updateGroup01_OriginToDestination() {

		group1_OD_List.clear();

		PanelGeneratorDTO insertGroup1_OD = new PanelGeneratorDTO();

		insertGroup1_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup1_OD.setGroup01_OD_StartTime(panelGeneratorDTO.getGroup01_OD_StartTime());
		insertGroup1_OD.setGroup01_OD_EndTime(panelGeneratorDTO.getGroup01_OD_EndTime());

		group1_OD_List.add(insertGroup1_OD);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup1_OriginToDestination(seqNo, user, group1_OD_List);

		if (update == 0) {
			setSucessMsg("Successfully Updated");
			;
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		}

	}

	public void updateGroup02_OriginToDestination() {

		group2_OD_List.clear();

		PanelGeneratorDTO insertGroup2_OD = new PanelGeneratorDTO();

		insertGroup2_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup2_OD.setGroup02_OD_StartTime(panelGeneratorDTO.getGroup02_OD_StartTime());
		insertGroup2_OD.setGroup02_OD_EndTime(panelGeneratorDTO.getGroup02_OD_EndTime());

		group2_OD_List.add(insertGroup2_OD);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup2_OriginToDestination(seqNo, user, group2_OD_List);

		if (update == 0) {
			setSucessMsg("Successfully Updated");
			;
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		}
	}

	public void updateGroup03_OriginToDestination() {

		group3_OD_List.clear();

		PanelGeneratorDTO insertGroup3_OD = new PanelGeneratorDTO();

		insertGroup3_OD.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup3_OD.setGroup03_OD_StartTime(panelGeneratorDTO.getGroup03_OD_StartTime());
		insertGroup3_OD.setGroup03_OD_EndTime(panelGeneratorDTO.getGroup03_OD_EndTime());

		group3_OD_List.add(insertGroup3_OD);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup3_OriginToDestination(seqNo, user, group3_OD_List);

		if (update == 0) {
			setSucessMsg("Successfully Updated");
			;
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		}

	}

	public void updateGroup01_DestinationToOrigin() {

		group1_DO_List.clear();

		PanelGeneratorDTO insertGroup1_DO = new PanelGeneratorDTO();

		insertGroup1_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup1_DO.setGroup01_DO_StartTime(panelGeneratorDTO.getGroup01_DO_StartTime());
		insertGroup1_DO.setGroup01_DO_EndTime(panelGeneratorDTO.getGroup01_DO_EndTime());

		group1_DO_List.add(insertGroup1_DO);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup1_DestinationToOrigin(seqNo, user, group1_DO_List);

		if (update == 0) {
			setSucessMsg("Successfully Updated");
			;
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		}

	}

	public void updateGroup02_DestinationToOrigin() {

		group2_DO_List.clear();

		PanelGeneratorDTO insertGroup2_DO = new PanelGeneratorDTO();

		insertGroup2_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup2_DO.setGroup02_DO_StartTime(panelGeneratorDTO.getGroup02_DO_StartTime());
		insertGroup2_DO.setGroup02_DO_EndTime(panelGeneratorDTO.getGroup02_DO_EndTime());

		group2_DO_List.add(insertGroup2_DO);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup2_DestinationToOrigin(seqNo, user, group2_DO_List);

		if (update == 0) {
			setSucessMsg("Successfully Updated");
			;
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		}
	}

	public void updateGroup03_DestinationToOrigin() {

		group3_DO_List.clear();

		PanelGeneratorDTO insertGroup3_DO = new PanelGeneratorDTO();

		insertGroup3_DO.setSelectGroup(panelGeneratorDTO.getSelectGroup());
		insertGroup3_DO.setGroup03_DO_StartTime(panelGeneratorDTO.getGroup03_DO_StartTime());
		insertGroup3_DO.setGroup03_DO_EndTime(panelGeneratorDTO.getGroup03_DO_EndTime());

		group3_DO_List.add(insertGroup3_DO);

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		int update = panelGeneratorService.updateGroup3_DestinationToOrigin(seqNo, user, group3_DO_List);

		if (update == 0) {
			setSucessMsg("Successfully Updated");
			;
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		}
	}

	public void updateDays() {

		Long seqNo = panelGeneratorDTO.getSeqNo();
		String user = sessionBackingBean.getLoginUser();

		if (panelGeneratorDTO.getNoOfTimeTables().equals("1")) {

			if (dateList_Group1.size() != 0) {
				int update_dateListGroup1 = panelGeneratorService.updateDateList_Group01(seqNo, user, dateList_Group1);
				
				setSucessMsg("Successfully Updated");
				;
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			} else {
				errorMsg = "Assign days for group 1";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}

		if (panelGeneratorDTO.getNoOfTimeTables().equals("2")) {

			if (dateList_Group1.size() != 0) {
				int update_dateListGroup1 = panelGeneratorService.updateDateList_Group01(seqNo, user, dateList_Group1);

				setSucessMsg("Successfully Updated");
				;
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			} 
			else if (dateList_Group2.size() != 0) {
				int update_dateListGroup2 = panelGeneratorService.updateDateList_Group02(seqNo, user, dateList_Group2);

				setSucessMsg("Successfully Updated");
				;
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			} else {
				errorMsg = "Assign days";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}

		if (panelGeneratorDTO.getNoOfTimeTables().equals("3")) {
			if (dateList_Group1.size() != 0) {
				int update_dateListGroup1 = panelGeneratorService.updateDateList_Group01(seqNo, user, dateList_Group1);

				setSucessMsg("Successfully Updated");
				;
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			} 
			else if (dateList_Group2.size() != 0) {
				int update_dateListGroup2 = panelGeneratorService.updateDateList_Group02(seqNo, user, dateList_Group2);

				setSucessMsg("Successfully Updated");
				;
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			}else if (dateList_Group3.size() != 0) {
				int update_dateListGroup3 = panelGeneratorService.updateDateList_Group03(seqNo, user,
						dateList_Group3);

				setSucessMsg("Successfully Updated");
				;
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			}else {
				errorMsg = "Assign days";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
	}

	public void addPanel2() {

		setErrorFound(false);

		if (panelGeneratorDTO.getSelectGroup() != null && !panelGeneratorDTO.getSelectGroup().isEmpty()
				&& !panelGeneratorDTO.getSelectGroup().equalsIgnoreCase("")) {
			if (panelGeneratorDTO.getPanel2Status() != null && !panelGeneratorDTO.getPanel2Status().isEmpty()
					&& !panelGeneratorDTO.getPanel2Status().equalsIgnoreCase("")) {

				if (panelGeneratorDTO.getSelectGroup().equals("1")) {

					dateList_Group1.clear();
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

					dateList_Group2.clear();
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

					dateList_Group3.clear();
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
//				if (panelGeneratorDTO.getNoOfTimeTables().equals("1")) {
//					if (dateList_Group1.size() < 7) {
//
//						errorMsg = "Please Select All the days to proceed";
//						RequestContext.getCurrentInstance().update("frmrequiredField");
//						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//						setErrorFound(true);
//
//					}
//				} else 
				if (panelGeneratorDTO.getNoOfTimeTables().equals("2")) {
					if (dateList_Group1.size() == 0 && dateList_Group2.size() == 0) {

						errorMsg = "Please Select day/days to proceed";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

						setErrorFound(true);
					}
					if (dateList_Group1.size() == 0 && dateList_Group2.size() != 0) {

//						if (dateList_Group2.size() == 7) {
//							errorMsg = "Select 6 or less days";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//						} else {
							int total = 7;
							int remaining = total - dateList_Group2.size();

							if (remaining < 7) {
								warningMsg = "You have " + remaining + " days remaining";

								RequestContext.getCurrentInstance().update("frmwarningField");
								RequestContext.getCurrentInstance().execute("PF('warningField').show()");

							}
//						}

					}
					if (dateList_Group1.size() != 0 && dateList_Group2.size() == 0) {

//						if (dateList_Group1.size() == 7) {
//							errorMsg = "Select 6 or less days";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//						} else {
							int total = 7;
							int remaining = total - dateList_Group1.size();

							if (remaining < 7) {
								warningMsg = "You have " + remaining + " days remaining";

								RequestContext.getCurrentInstance().update("frmwarningField");
								RequestContext.getCurrentInstance().execute("PF('warningField').show()");
							}
//						}

					}
//					if (dateList_Group1.size() != 0 && dateList_Group2.size() != 0) {
//
//						int total = dateList_Group1.size() + dateList_Group2.size();
//
//						if (total < 7) {
//							errorMsg = "Please Select All the days to proceed";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//
//						}
//
//					}
				} else if (panelGeneratorDTO.getNoOfTimeTables().equals("3")) {
					if (dateList_Group1.size() == 0 && dateList_Group2.size() == 0 && dateList_Group3.size() == 0) {

						errorMsg = "Please Select day/days to proceed";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

						setErrorFound(true);
					}
					if (dateList_Group1.size() == 0 && dateList_Group2.size() == 0 && dateList_Group3.size() != 0) {

//						if (dateList_Group3.size() == 7 || dateList_Group3.size() == 6) {
//							errorMsg = "Select 5 or less days";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//						} else {

							int remaining = 7 - dateList_Group3.size();

							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//						}

					}
					if (dateList_Group1.size() == 0 && dateList_Group2.size() != 0 && dateList_Group3.size() == 0) {

//						if (dateList_Group2.size() == 7 || dateList_Group2.size() == 6) {
//							errorMsg = "Select 5 or less days";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//						} else {

							int remaining = 7 - dateList_Group2.size();

							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//						}

					}

					if (dateList_Group1.size() != 0 && dateList_Group2.size() == 0 && dateList_Group3.size() == 0) {

//						if (dateList_Group1.size() == 7 || dateList_Group1.size() == 6) {
//							errorMsg = "Select 5 or less days";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//						} else {

							int remaining = 7 - dateList_Group1.size();

							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//						}
					}

					if (dateList_Group1.size() == 0 && dateList_Group2.size() != 0 && dateList_Group3.size() != 0) {

						int selectDays = dateList_Group2.size() + dateList_Group3.size();

//						if (selectDays == 7) {
//
//							errorMsg = "Please reduce a day/days";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//
//						} else {

							int remaining = 7 - selectDays;
							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//						}
					}

					if (dateList_Group1.size() != 0 && dateList_Group2.size() == 0 && dateList_Group3.size() != 0) {

						int selectDays = dateList_Group1.size() + dateList_Group3.size();

//						if (selectDays == 7) {
//
//							errorMsg = "Please reduce a day/days";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//
//						} else {

							int remaining = 7 - selectDays;
							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//						}

					}
					if (dateList_Group1.size() != 0 && dateList_Group2.size() != 0 && dateList_Group3.size() == 0) {

						int selectDays = dateList_Group1.size() + dateList_Group2.size();

//						if (selectDays == 7) {
//
//							errorMsg = "Please reduce a day/days";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//
//						} else {

							int remaining = 7 - selectDays;
							warningMsg = "You have " + remaining + " days remaining";

							RequestContext.getCurrentInstance().update("frmwarningField");
							RequestContext.getCurrentInstance().execute("PF('warningField').show()");
//						}
					}
//					if (dateList_Group1.size() != 0 && dateList_Group2.size() != 0 && dateList_Group3.size() != 0) {
//						int total = dateList_Group1.size() + dateList_Group2.size() + dateList_Group3.size();
//
//						if (total < 7) {
//							errorMsg = "Please Select All the days to proceed";
//							RequestContext.getCurrentInstance().update("frmrequiredField");
//							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
//
//							setErrorFound(true);
//
//						}
//
//					}
				}
				/** validation **/

				if (isErrorFound() == false) {

					setDisablePanel2(true);
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

			errorMsg = "Please Select A group to proceed ";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void clearPanel2() {
		setDisablePanel2(false);
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

	public void clearTables() {

		panelGeneratorDTO.setGroup01_DO_StartTime(null);
		panelGeneratorDTO.setGroup01_DO_EndTime(null);

		panelGeneratorDTO.setGroup02_DO_StartTime(null);
		panelGeneratorDTO.setGroup02_DO_EndTime(null);

		panelGeneratorDTO.setGroup03_DO_StartTime(null);
		panelGeneratorDTO.setGroup03_DO_EndTime(null);

		panelGeneratorDTO.setGroup01_OD_StartTime(null);
		panelGeneratorDTO.setGroup01_OD_EndTime(null);

		panelGeneratorDTO.setGroup02_OD_StartTime(null);
		panelGeneratorDTO.setGroup02_OD_EndTime(null);

		panelGeneratorDTO.setGroup03_OD_StartTime(null);
		panelGeneratorDTO.setGroup03_OD_EndTime(null);

	}

	public void clearForm() {

		panelGeneratorDTO = new PanelGeneratorDTO();
		clearLists();
		clearTables();
		setDisablePanel1(false);
		setDisablePanel2(true);
		setDisablePanel3(true);

		setGroup1(true);
		setGroup2(true);
		setGroup3(true);
		
		setClickMonday("false");
		setClickTuesday("false");
		setClickWednesday("false");
		setClickThursday("false");
		setClickFriday("false");
		setClickSaturday("false");
		setClickSunday("false");
	}

	public void mainSave() {

		if (oldNoOfTimeTables.equals("1") && panelGeneratorDTO.getNoOfTimeTables().equals("2")) {

			if (group1_OD_List.size() != 0) {
				if (group1_DO_List.size() != 0) {
					if (dateList_Group1.size() != 0) {
						if (group2_OD_List.size() != 0) {
							if (group2_DO_List.size() != 0) {
								if (dateList_Group2.size() != 0) {

									String user = sessionBackingBean.getLoginUser();
									Long seqNo = panelGeneratorDTO.getSeqNo();
									if (!panelGeneratorDTO.getPanel1Status().equals(oldStatus)) {
										int status = panelGeneratorService.updateStatus(
												panelGeneratorDTO.getPanel1Status(), user,
												panelGeneratorDTO.getRefNo(),panelGeneratorDTO.getRouteNo(),panelGeneratorDTO.getBusCategory());
									}

									int result0 = panelGeneratorService.updateDateList_Group01(seqNo, user,
											dateList_Group1);
									int result1 = panelGeneratorService.updateGroup1_DestinationToOrigin(seqNo, user,
											group1_DO_List);
									int result2 = panelGeneratorService.updateGroup1_OriginToDestination(seqNo, user,
											group1_OD_List);
									int result3 = panelGeneratorService.group02_details(panelGeneratorDTO, user,
											group2_OD_List, group2_DO_List, dateList_Group2);
									int result4 = panelGeneratorService.updatePanelGenerator(
											panelGeneratorDTO.getNoOfTimeTables(), user, panelGeneratorDTO.getRefNo());

									if (result0 == 0 && result1 == 0 && result2 == 0 && result3 == 0 && result4 == 0) {
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
		} else if (oldNoOfTimeTables.equals("1") && panelGeneratorDTO.getNoOfTimeTables().equals("3")) {
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
												if (!panelGeneratorDTO.getPanel1Status().equals(oldStatus)) {
													int status = panelGeneratorService.updateStatus(
															panelGeneratorDTO.getPanel1Status(), user,
															panelGeneratorDTO.getRefNo(),panelGeneratorDTO.getRouteNo(),panelGeneratorDTO.getBusCategory());
												}
												Long seqNo = panelGeneratorDTO.getSeqNo();

												int result0 = panelGeneratorService.updateDateList_Group01(seqNo, user,
														dateList_Group1);
												int result1 = panelGeneratorService
														.updateGroup1_DestinationToOrigin(seqNo, user, group1_DO_List);
												int result2 = panelGeneratorService
														.updateGroup1_OriginToDestination(seqNo, user, group1_OD_List);
												int result3 = panelGeneratorService.group02_details(panelGeneratorDTO,
														user, group2_OD_List, group2_DO_List, dateList_Group2);
												int result4 = panelGeneratorService.group03_details(panelGeneratorDTO,
														user, group3_OD_List, group3_DO_List, dateList_Group3);

												int result5 = panelGeneratorService.updatePanelGenerator(
														panelGeneratorDTO.getNoOfTimeTables(), user,
														panelGeneratorDTO.getRefNo());

												if (result0 == 0 && result1 == 0 && result2 == 0 && result3 == 0
														&& result4 == 0 && result5 == 0) {
													setSucessMsg("Successfully Saved");
													;
													RequestContext.getCurrentInstance().update("frmsuccessSve");
													RequestContext.getCurrentInstance()
															.execute("PF('successSve').show()");
												}

											} else {
												errorMsg = "Group 1 Days are not selected";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
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

		} else if (oldNoOfTimeTables.equals("2") && panelGeneratorDTO.getNoOfTimeTables().equals("1")) {
			if (group1_OD_List.size() != 0) {
				if (group1_DO_List.size() != 0) {
					if (dateList_Group1.size() != 0) {

						String user = sessionBackingBean.getLoginUser();
						if (!panelGeneratorDTO.getPanel1Status().equals(oldStatus)) {
							int status = panelGeneratorService.updateStatus(panelGeneratorDTO.getPanel1Status(), user,
									panelGeneratorDTO.getRefNo(),panelGeneratorDTO.getRouteNo(),panelGeneratorDTO.getBusCategory());
						}
						Long seqNo = panelGeneratorDTO.getSeqNo();

						int result0 = panelGeneratorService.updateDateList_Group01(seqNo, user, dateList_Group1);
						int result1 = panelGeneratorService.updateGroup1_DestinationToOrigin(seqNo, user,
								group1_DO_List);
						int result2 = panelGeneratorService.updateGroup1_OriginToDestination(seqNo, user,
								group1_OD_List);

						int result3 = panelGeneratorService.inActiveGroup02(seqNo, user);
						int result4 = panelGeneratorService.updatePanelGenerator(panelGeneratorDTO.getNoOfTimeTables(),
								user, panelGeneratorDTO.getRefNo());

						if (result0 == 0 && result1 == 0 && result2 == 0 && result3 == 0 && result4 == 0) {
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
		} else if (oldNoOfTimeTables.equals("2") && panelGeneratorDTO.getNoOfTimeTables().equals("3")) {
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
												if (!panelGeneratorDTO.getPanel1Status().equals(oldStatus)) {
													int status = panelGeneratorService.updateStatus(
															panelGeneratorDTO.getPanel1Status(), user,
															panelGeneratorDTO.getRefNo(),panelGeneratorDTO.getRouteNo(),panelGeneratorDTO.getBusCategory());
												}
												Long seqNo = panelGeneratorDTO.getSeqNo();

												int result0 = panelGeneratorService.updateDateList_Group01(seqNo, user,
														dateList_Group1);
												int result1 = panelGeneratorService
														.updateGroup1_DestinationToOrigin(seqNo, user, group1_DO_List);
												int result2 = panelGeneratorService
														.updateGroup1_OriginToDestination(seqNo, user, group1_OD_List);

												int result3 = panelGeneratorService.updateDateList_Group02(seqNo, user,
														dateList_Group2);
												int result4 = panelGeneratorService
														.updateGroup2_DestinationToOrigin(seqNo, user, group2_DO_List);
												int result5 = panelGeneratorService
														.updateGroup2_OriginToDestination(seqNo, user, group2_OD_List);

												int result6 = panelGeneratorService.group03_details(panelGeneratorDTO,
														user, group3_OD_List, group3_DO_List, dateList_Group3);

												int result7 = panelGeneratorService.updatePanelGenerator(
														panelGeneratorDTO.getNoOfTimeTables(), user,
														panelGeneratorDTO.getRefNo());

												if (result0 == 0 && result1 == 0 && result2 == 0 && result3 == 0
														&& result4 == 0 && result5 == 0 && result6 == 0
														&& result7 == 0) {
													setSucessMsg("Successfully Saved");
													;
													RequestContext.getCurrentInstance().update("frmsuccessSve");
													RequestContext.getCurrentInstance()
															.execute("PF('successSve').show()");
												}

											} else {
												errorMsg = "Group 1 Days are not selected";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
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
		} else if (oldNoOfTimeTables.equals("3") && panelGeneratorDTO.getNoOfTimeTables().equals("1")) {
			if (group1_OD_List.size() != 0) {
				if (group1_DO_List.size() != 0) {
					if (dateList_Group1.size() != 0) {

						String user = sessionBackingBean.getLoginUser();

						Long seqNo = panelGeneratorDTO.getSeqNo();
						if (!panelGeneratorDTO.getPanel1Status().equals(oldStatus)) {
							int status = panelGeneratorService.updateStatus(panelGeneratorDTO.getPanel1Status(), user,
									panelGeneratorDTO.getRefNo(),panelGeneratorDTO.getRouteNo(),panelGeneratorDTO.getBusCategory());
						}
						int result0 = panelGeneratorService.updateDateList_Group01(seqNo, user, dateList_Group1);
						int result1 = panelGeneratorService.updateGroup1_DestinationToOrigin(seqNo, user,
								group1_DO_List);
						int result2 = panelGeneratorService.updateGroup1_OriginToDestination(seqNo, user,
								group1_OD_List);

						int result3 = panelGeneratorService.inActiveGroup02(seqNo, user);
						int result4 = panelGeneratorService.inActiveGroup03(seqNo, user);
						int result5 = panelGeneratorService.updatePanelGenerator(panelGeneratorDTO.getNoOfTimeTables(),
								user, panelGeneratorDTO.getRefNo());

						if (result0 == 0 && result1 == 0 && result2 == 0 && result3 == 0 && result4 == 0
								&& result5 == 0) {
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
		} else if (oldNoOfTimeTables.equals("3") && panelGeneratorDTO.getNoOfTimeTables().equals("2")) {
			if (group1_OD_List.size() != 0) {
				if (group1_DO_List.size() != 0) {
					if (dateList_Group1.size() != 0) {
						if (group2_OD_List.size() != 0) {
							if (group2_DO_List.size() != 0) {
								if (dateList_Group2.size() != 0) {

									String user = sessionBackingBean.getLoginUser();
									if (!panelGeneratorDTO.getPanel1Status().equals(oldStatus)) {
										int status = panelGeneratorService.updateStatus(
												panelGeneratorDTO.getPanel1Status(), user,
												panelGeneratorDTO.getRefNo(),panelGeneratorDTO.getRouteNo(),panelGeneratorDTO.getBusCategory());
									}
									Long seqNo = panelGeneratorDTO.getSeqNo();

									int result0 = panelGeneratorService.updateDateList_Group01(seqNo, user,
											dateList_Group1);
									int result1 = panelGeneratorService.updateGroup1_DestinationToOrigin(seqNo, user,
											group1_DO_List);
									int result2 = panelGeneratorService.updateGroup1_OriginToDestination(seqNo, user,
											group1_OD_List);

									int result3 = panelGeneratorService.updateDateList_Group02(seqNo, user,
											dateList_Group2);
									int result4 = panelGeneratorService.updateGroup2_DestinationToOrigin(seqNo, user,
											group2_DO_List);
									int result5 = panelGeneratorService.updateGroup2_OriginToDestination(seqNo, user,
											group2_OD_List);

									int result6 = panelGeneratorService.inActiveGroup03(seqNo, user);

									int result7 = panelGeneratorService.updatePanelGenerator(
											panelGeneratorDTO.getNoOfTimeTables(), user, panelGeneratorDTO.getRefNo());

									if (result0 == 0 && result1 == 0 && result2 == 0 && result3 == 0 && result4 == 0
											&& result5 == 0 && result6 == 0 && result7 == 0) {
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

	public List<PanelGeneratorDTO> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(List<PanelGeneratorDTO> refNoList) {
		this.refNoList = refNoList;
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

	public String getOldNoOfTimeTables() {
		return oldNoOfTimeTables;
	}

	public void setOldNoOfTimeTables(String oldNoOfTimeTables) {
		this.oldNoOfTimeTables = oldNoOfTimeTables;
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

	public List<String> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<String> groupList) {
		this.groupList = groupList;
	}

	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isDisablePanel1() {
		return disablePanel1;
	}

	public void setDisablePanel1(boolean disablePanel1) {
		this.disablePanel1 = disablePanel1;
	}

	public boolean isDisablePanel2() {
		return disablePanel2;
	}

	public void setDisablePanel2(boolean disablePanel2) {
		this.disablePanel2 = disablePanel2;
	}

	public boolean isDisablePanel3() {
		return disablePanel3;
	}

	public void setDisablePanel3(boolean disablePanel3) {
		this.disablePanel3 = disablePanel3;
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

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getWarningMsg() {
		return warningMsg;
	}

	public void setWarningMsg(String warningMsg) {
		this.warningMsg = warningMsg;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public List<String> getPanel1_List() {
		return panel1_List;
	}

	public void setPanel1_List(List<String> panel1_List) {
		this.panel1_List = panel1_List;
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

	public boolean isChangeTimeTable() {
		return changeTimeTable;
	}

	public void setChangeTimeTable(boolean changeTimeTable) {
		this.changeTimeTable = changeTimeTable;
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

	public boolean isErrorFound() {
		return errorFound;
	}

	public void setErrorFound(boolean errorFound) {
		this.errorFound = errorFound;
	}

	public boolean isTabelPanel() {
		return tabelPanel;
	}

	public void setTabelPanel(boolean tabelPanel) {
		this.tabelPanel = tabelPanel;
	}

	public boolean isAddBtn() {
		return addBtn;
	}

	public void setAddBtn(boolean addBtn) {
		this.addBtn = addBtn;
	}

	public List<PanelGeneratorDTO> getTripGeneratorList() {
		return tripGeneratorList;
	}

	public void setTripGeneratorList(List<PanelGeneratorDTO> tripGeneratorList) {
		this.tripGeneratorList = tripGeneratorList;
	}

	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getOldRoute() {
		return oldRoute;
	}

	public void setOldRoute(String oldRoute) {
		this.oldRoute = oldRoute;
	}

	public String getOldService() {
		return oldService;
	}

	public void setOldService(String oldService) {
		this.oldService = oldService;
	}

}
