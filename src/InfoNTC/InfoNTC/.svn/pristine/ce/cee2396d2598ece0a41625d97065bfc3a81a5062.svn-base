package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadSheduleDTO;
import lk.informatics.ntc.model.dto.RosterDTO;
import lk.informatics.ntc.model.service.FlyingSquadScheduleService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "flyingSquadScheduleBackingBean")
@ViewScoped
public class FlyingSquadScheduleBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private int year;
	private int selectedyear;
	private int selectedMonth;

	private String selectedyearn;
	private String selectedMonthn;
	private String savemsg;
	private String user;
	private ArrayList<String> yearList;
	private ArrayList<FlyingSquadSheduleDTO> detailList;
	private ArrayList<RosterDTO> rosterList;
	private FlyingSquadScheduleService flyingSquadScheduleService;
	private FlyingSquadSheduleDTO flyingSquadSheduleDTO;
	private List<FlyingSquadGroupsDTO> serviceList = new ArrayList<FlyingSquadGroupsDTO>();
	private int tol;
	private boolean allowinsert;
	private boolean allowupdate;
	private boolean allowday30;
	private boolean allowday31;
	private boolean allowday29;
	private boolean allowday28;

	private boolean day1n;
	private boolean day2n;
	private boolean day3n;
	private boolean day4n;
	private boolean day5n;
	private boolean day6n;
	private boolean day7n;
	private boolean day8n;
	private boolean day9n;
	private boolean day10n;
	private boolean day11n;
	private boolean day12n;
	private boolean day13n;
	private boolean day14n;
	private boolean day15n;
	private boolean day16n;
	private boolean day17n;
	private boolean day18n;
	private boolean day19n;
	private boolean day20n;
	private boolean day21n;
	private boolean day22n;
	private boolean day23n;
	private boolean day24n;
	private boolean day25n;
	private boolean day26n;
	private boolean day27n;
	private boolean day28n;
	private boolean day29n;
	private boolean day30n;
	private boolean day31n;

	@PostConstruct
	public void init() {

		allowinsert = false;
		allowupdate = false;
		setAllowday30(false);
		setAllowday29(false);
		setAllowday28(false);
		setAllowday31(false);

		day1n = false;
		day2n = false;
		day3n = false;
		day4n = false;
		day5n = false;
		day6n = false;
		day7n = false;
		day8n = false;
		day9n = false;
		day10n = false;
		day11n = false;
		day12n = false;
		day13n = false;
		day14n = false;
		day15n = false;
		day16n = false;
		day17n = false;
		day18n = false;
		day19n = false;
		day20n = false;
		day21n = false;
		day22n = false;
		day23n = false;
		day24n = false;
		day25n = false;
		day26n = false;
		day27n = false;
		day28n = false;
		day29n = false;
		day30n = false;
		day31n = false;

		flyingSquadScheduleService = (FlyingSquadScheduleService) SpringApplicationContex
				.getBean("flyingSquadScheduleService");
		setUser(sessionBackingBean.getLoginUser());

		yearList = new ArrayList();
		Calendar c = Calendar.getInstance();
		setYear(c.get(Calendar.YEAR));
		selectedyear = c.get(Calendar.YEAR);
		selectedMonth = c.get(Calendar.MONTH);

		selectedMonthn = String.valueOf(selectedMonth);
		selectedyearn = String.valueOf(selectedyear);

		for (int i = 0; i < 10; i++) {
			yearList.add(String.valueOf(year++));
		}

		tol = flyingSquadScheduleService.getcount(selectedyear, selectedMonth);

		if (tol == 0) {
			allowinsert = true;
			detailList = flyingSquadScheduleService.getGroupcode();
		} else {
			allowupdate = true;
			detailList = flyingSquadScheduleService.genarateDetails(selectedyear, selectedMonth);
		}

		rosterList = flyingSquadScheduleService.getRosterCd();

		if (selectedMonthn.equalsIgnoreCase("1")) {
			Calendar cal = Calendar.getInstance();
			cal.set(selectedyear, cal.FEBRUARY, 1);
			int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

			if (maxDay == 28) {
				setAllowday30(false);
				setAllowday29(false);
				setAllowday28(true);
				setAllowday31(false);
			}

			else {
				setAllowday30(false);
				setAllowday29(true);
				setAllowday28(true);
				setAllowday31(false);
			}

		} else if (selectedMonthn.equalsIgnoreCase("0") || selectedMonthn.equalsIgnoreCase("2")
				|| selectedMonthn.equalsIgnoreCase("4") || selectedMonthn.equalsIgnoreCase("6")
				|| selectedMonthn.equalsIgnoreCase("7") || selectedMonthn.equalsIgnoreCase("9")
				|| selectedMonthn.equalsIgnoreCase("11")) {
			setAllowday30(true);
			setAllowday31(true);
			setAllowday29(true);
			setAllowday28(true);

		}

		else {
			setAllowday30(true);
			setAllowday29(true);
			setAllowday28(true);
			setAllowday31(false);

		}

		weekendDays();
		loadValues();

	}

	public void loadValues() {
		setServiceList(flyingSquadScheduleService.getServiceDetails());
	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

	}

	public void search() {

		allowinsert = false;
		allowupdate = false;

		day1n = false;
		day2n = false;
		day3n = false;
		day4n = false;
		day5n = false;
		day6n = false;
		day7n = false;
		day8n = false;
		day9n = false;
		day10n = false;
		day11n = false;
		day12n = false;
		day13n = false;
		day14n = false;
		day15n = false;
		day16n = false;
		day17n = false;
		day18n = false;
		day19n = false;
		day20n = false;
		day21n = false;
		day22n = false;
		day23n = false;
		day24n = false;
		day25n = false;
		day26n = false;
		day27n = false;
		day28n = false;
		day29n = false;
		day30n = false;
		day31n = false;

		tol = flyingSquadScheduleService.getcount(Integer.parseInt(selectedyearn), Integer.parseInt(selectedMonthn));

		if (tol == 0) {
			allowinsert = true;
			detailList = flyingSquadScheduleService.getGroupcode();
		} else {
			allowupdate = true;
			detailList = flyingSquadScheduleService.genarateDetails(Integer.parseInt(selectedyearn),
					Integer.parseInt(selectedMonthn));

		}

		rosterList = flyingSquadScheduleService.getRosterCd();

		if (selectedMonthn.equalsIgnoreCase("1")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Integer.parseInt(selectedyearn), cal.FEBRUARY, 1);
			int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

			if (maxDay == 28) {
				setAllowday30(false);
				setAllowday29(false);
				setAllowday28(true);
				setAllowday31(false);
			}

			else {
				setAllowday30(false);
				setAllowday29(true);
				setAllowday28(true);
				setAllowday31(false);
			}
		} else if (selectedMonthn.equalsIgnoreCase("0") || selectedMonthn.equalsIgnoreCase("2")
				|| selectedMonthn.equalsIgnoreCase("4") || selectedMonthn.equalsIgnoreCase("6")
				|| selectedMonthn.equalsIgnoreCase("7") || selectedMonthn.equalsIgnoreCase("9")
				|| selectedMonthn.equalsIgnoreCase("11")) {
			setAllowday30(true);
			setAllowday31(true);
			setAllowday29(true);
			setAllowday28(true);

		}

		else {
			setAllowday30(true);
			setAllowday29(true);
			setAllowday28(true);
			setAllowday31(false);

		}

		weekendDays();

	}

	public void save() {

		if (allowinsert) {
			flyingSquadScheduleService.insertDetails(detailList, user, Integer.parseInt(selectedyearn),
					Integer.parseInt(selectedMonthn));
			setSavemsg("Successfully Saved!");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			allowinsert = false;
			allowupdate = false;

		}

		else if (allowupdate) {
			flyingSquadScheduleService.updateShedule(detailList, user, Integer.parseInt(selectedyearn),
					Integer.parseInt(selectedMonthn));
			setSavemsg("Successfully Updated!");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			allowinsert = false;
			allowupdate = false;
		}
	}

	public void clean() {

		detailList = new ArrayList<FlyingSquadSheduleDTO>();
		tol = flyingSquadScheduleService.getcount(Integer.parseInt(selectedyearn), Integer.parseInt(selectedMonthn));

		if (tol == 0) {
			allowinsert = true;
			detailList = flyingSquadScheduleService.getGroupcode();
		} else {
			allowupdate = true;
			detailList = flyingSquadScheduleService.genarateDetails(Integer.parseInt(selectedyearn),
					Integer.parseInt(selectedMonthn));
		}

		rosterList = flyingSquadScheduleService.getRosterCd();

		if (selectedMonthn.equalsIgnoreCase("1")) {
			Calendar cal = Calendar.getInstance();
			cal.set(selectedyear, cal.FEBRUARY, 1);
			int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

			if (maxDay == 28) {
				setAllowday30(false);
				setAllowday29(false);
				setAllowday28(true);
				setAllowday31(false);
			}

			else {
				setAllowday30(false);
				setAllowday29(true);
				setAllowday28(true);
				setAllowday31(false);
			}

		} else if (selectedMonthn.equalsIgnoreCase("0") || selectedMonthn.equalsIgnoreCase("2")
				|| selectedMonthn.equalsIgnoreCase("4") || selectedMonthn.equalsIgnoreCase("6")
				|| selectedMonthn.equalsIgnoreCase("7") || selectedMonthn.equalsIgnoreCase("9")
				|| selectedMonthn.equalsIgnoreCase("11")) {
			setAllowday30(true);
			setAllowday31(true);
			setAllowday29(true);
			setAllowday28(true);

		}

		else {
			setAllowday30(true);
			setAllowday29(true);
			setAllowday28(true);
			setAllowday31(false);

		}

	}

	public void clear() {

		day1n = false;
		day2n = false;
		day3n = false;
		day4n = false;
		day5n = false;
		day6n = false;
		day7n = false;
		day8n = false;
		day9n = false;
		day10n = false;
		day11n = false;
		day12n = false;
		day13n = false;
		day14n = false;
		day15n = false;
		day16n = false;
		day17n = false;
		day18n = false;
		day19n = false;
		day20n = false;
		day21n = false;
		day22n = false;
		day23n = false;
		day24n = false;
		day25n = false;
		day26n = false;
		day27n = false;
		day28n = false;
		day29n = false;
		day30n = false;
		day31n = false;

		yearList = new ArrayList();
		Calendar c = Calendar.getInstance();
		setYear(c.get(Calendar.YEAR));
		selectedyear = c.get(Calendar.YEAR);
		selectedMonth = c.get(Calendar.MONTH);

		selectedMonthn = String.valueOf(selectedMonth);
		selectedyearn = String.valueOf(selectedyear);

		for (int i = 0; i < 10; i++) {
			yearList.add(String.valueOf(year++));
		}

		tol = flyingSquadScheduleService.getcount(selectedyear, selectedMonth);

		if (tol == 0) {
			allowinsert = true;
			detailList = flyingSquadScheduleService.getGroupcode();
		} else {
			allowupdate = true;
			detailList = flyingSquadScheduleService.genarateDetails(selectedyear, selectedMonth);
		}

		rosterList = flyingSquadScheduleService.getRosterCd();

		if (selectedMonthn.equalsIgnoreCase("1")) {
			Calendar cal = Calendar.getInstance();
			cal.set(selectedyear, cal.FEBRUARY, 1);
			int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

			if (maxDay == 28) {
				setAllowday30(false);
				setAllowday29(false);
				setAllowday28(true);
				setAllowday31(false);
			}

			else {
				setAllowday30(false);
				setAllowday29(true);
				setAllowday28(true);
				setAllowday31(false);
			}

		} else if (selectedMonthn.equalsIgnoreCase("0") || selectedMonthn.equalsIgnoreCase("2")
				|| selectedMonthn.equalsIgnoreCase("4") || selectedMonthn.equalsIgnoreCase("6")
				|| selectedMonthn.equalsIgnoreCase("7") || selectedMonthn.equalsIgnoreCase("9")
				|| selectedMonthn.equalsIgnoreCase("11")) {
			setAllowday30(true);
			setAllowday31(true);
			setAllowday29(true);
			setAllowday28(true);

		}

		else {
			setAllowday30(true);
			setAllowday29(true);
			setAllowday28(true);
			setAllowday31(false);

		}

		weekendDays();
	}

	public void weekendDays() {

		List list = new ArrayList();
		Calendar cal = new GregorianCalendar(Integer.parseInt(selectedyearn), Integer.parseInt(selectedMonthn), 1);
		int month = Integer.parseInt(selectedMonthn);

		do {
			// get the day of the week for the current day
			int day = cal.get(Calendar.DAY_OF_WEEK);
			// check if it is a Saturday or Sunday
			if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {

				list.add(cal.get(Calendar.DAY_OF_MONTH));

				if (cal.get(Calendar.DAY_OF_MONTH) == 1) {
					day1n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 2) {
					day2n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 3) {
					day3n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 4) {
					day4n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 5) {
					day5n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 6) {
					day6n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 7) {
					day7n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 8) {
					day8n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 9) {
					day9n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 10) {
					day10n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 11) {
					day11n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 12) {
					day12n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 13) {
					day13n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 14) {
					day14n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 15) {
					day15n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 16) {
					day16n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 17) {
					day17n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 18) {
					day18n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 19) {
					day19n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 20) {
					day20n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 21) {
					day21n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 22) {
					day22n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 23) {
					day23n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 24) {
					day24n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 25) {
					day25n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 26) {
					day26n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 27) {
					day27n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 28) {
					day28n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 29) {
					day29n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 30) {
					day30n = true;
				}

				if (cal.get(Calendar.DAY_OF_MONTH) == 31) {
					day31n = true;
				}
			}
			// advance to the next day
			cal.add(Calendar.DAY_OF_YEAR, 1);
		} while (cal.get(Calendar.MONTH) == month);

	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getSelectedyear() {
		return selectedyear;
	}

	public void setSelectedyear(int selectedyear) {
		this.selectedyear = selectedyear;
	}

	public int getSelectedMonth() {
		return selectedMonth;
	}

	public void setSelectedMonth(int selectedMonth) {
		this.selectedMonth = selectedMonth;
	}

	public ArrayList<FlyingSquadSheduleDTO> getDetailList() {
		return detailList;
	}

	public void setDetailList(ArrayList<FlyingSquadSheduleDTO> detailList) {
		this.detailList = detailList;
	}

	public ArrayList<RosterDTO> getRosterList() {
		return rosterList;
	}

	public void setRosterList(ArrayList<RosterDTO> rosterList) {
		this.rosterList = rosterList;
	}

	public ArrayList<String> getYearList() {
		return yearList;
	}

	public void setYearList(ArrayList<String> yearList) {
		this.yearList = yearList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public FlyingSquadScheduleService getFlyingSquadScheduleService() {
		return flyingSquadScheduleService;
	}

	public void setFlyingSquadScheduleService(FlyingSquadScheduleService flyingSquadScheduleService) {
		this.flyingSquadScheduleService = flyingSquadScheduleService;
	}

	public FlyingSquadSheduleDTO getFlyingSquadSheduleDTO() {
		return flyingSquadSheduleDTO;
	}

	public void setFlyingSquadSheduleDTO(FlyingSquadSheduleDTO flyingSquadSheduleDTO) {
		this.flyingSquadSheduleDTO = flyingSquadSheduleDTO;
	}

	public int getTol() {
		return tol;
	}

	public void setTol(int tol) {
		this.tol = tol;
	}

	public boolean isAllowinsert() {
		return allowinsert;
	}

	public void setAllowinsert(boolean allowinsert) {
		this.allowinsert = allowinsert;
	}

	public boolean isAllowupdate() {
		return allowupdate;
	}

	public void setAllowupdate(boolean allowupdate) {
		this.allowupdate = allowupdate;
	}

	public String getSelectedyearn() {
		return selectedyearn;
	}

	public void setSelectedyearn(String selectedyearn) {
		this.selectedyearn = selectedyearn;
	}

	public String getSelectedMonthn() {
		return selectedMonthn;
	}

	public void setSelectedMonthn(String selectedMonthn) {
		this.selectedMonthn = selectedMonthn;
	}

	public boolean isAllowday30() {
		return allowday30;
	}

	public void setAllowday30(boolean allowday30) {
		this.allowday30 = allowday30;
	}

	public boolean isAllowday31() {
		return allowday31;
	}

	public void setAllowday31(boolean allowday31) {
		this.allowday31 = allowday31;
	}

	public boolean isAllowday29() {
		return allowday29;
	}

	public void setAllowday29(boolean allowday29) {
		this.allowday29 = allowday29;
	}

	public boolean isAllowday28() {
		return allowday28;
	}

	public void setAllowday28(boolean allowday28) {
		this.allowday28 = allowday28;
	}

	public String getSavemsg() {
		return savemsg;
	}

	public void setSavemsg(String savemsg) {
		this.savemsg = savemsg;
	}

	public boolean isDay1n() {
		return day1n;
	}

	public void setDay1n(boolean day1n) {
		this.day1n = day1n;
	}

	public boolean isDay2n() {
		return day2n;
	}

	public void setDay2n(boolean day2n) {
		this.day2n = day2n;
	}

	public boolean isDay3n() {
		return day3n;
	}

	public void setDay3n(boolean day3n) {
		this.day3n = day3n;
	}

	public boolean isDay4n() {
		return day4n;
	}

	public void setDay4n(boolean day4n) {
		this.day4n = day4n;
	}

	public boolean isDay5n() {
		return day5n;
	}

	public void setDay5n(boolean day5n) {
		this.day5n = day5n;
	}

	public boolean isDay6n() {
		return day6n;
	}

	public void setDay6n(boolean day6n) {
		this.day6n = day6n;
	}

	public boolean isDay7n() {
		return day7n;
	}

	public void setDay7n(boolean day7n) {
		this.day7n = day7n;
	}

	public boolean isDay8n() {
		return day8n;
	}

	public void setDay8n(boolean day8n) {
		this.day8n = day8n;
	}

	public boolean isDay9n() {
		return day9n;
	}

	public void setDay9n(boolean day9n) {
		this.day9n = day9n;
	}

	public boolean isDay10n() {
		return day10n;
	}

	public void setDay10n(boolean day10n) {
		this.day10n = day10n;
	}

	public boolean isDay11n() {
		return day11n;
	}

	public void setDay11n(boolean day11n) {
		this.day11n = day11n;
	}

	public boolean isDay12n() {
		return day12n;
	}

	public void setDay12n(boolean day12n) {
		this.day12n = day12n;
	}

	public boolean isDay13n() {
		return day13n;
	}

	public void setDay13n(boolean day13n) {
		this.day13n = day13n;
	}

	public boolean isDay14n() {
		return day14n;
	}

	public void setDay14n(boolean day14n) {
		this.day14n = day14n;
	}

	public boolean isDay15n() {
		return day15n;
	}

	public void setDay15n(boolean day15n) {
		this.day15n = day15n;
	}

	public boolean isDay16n() {
		return day16n;
	}

	public void setDay16n(boolean day16n) {
		this.day16n = day16n;
	}

	public boolean isDay17n() {
		return day17n;
	}

	public void setDay17n(boolean day17n) {
		this.day17n = day17n;
	}

	public boolean isDay18n() {
		return day18n;
	}

	public void setDay18n(boolean day18n) {
		this.day18n = day18n;
	}

	public boolean isDay19n() {
		return day19n;
	}

	public void setDay19n(boolean day19n) {
		this.day19n = day19n;
	}

	public boolean isDay20n() {
		return day20n;
	}

	public void setDay20n(boolean day20n) {
		this.day20n = day20n;
	}

	public boolean isDay21n() {
		return day21n;
	}

	public void setDay21n(boolean day21n) {
		this.day21n = day21n;
	}

	public boolean isDay22n() {
		return day22n;
	}

	public void setDay22n(boolean day22n) {
		this.day22n = day22n;
	}

	public boolean isDay23n() {
		return day23n;
	}

	public void setDay23n(boolean day23n) {
		this.day23n = day23n;
	}

	public boolean isDay24n() {
		return day24n;
	}

	public void setDay24n(boolean day24n) {
		this.day24n = day24n;
	}

	public boolean isDay25n() {
		return day25n;
	}

	public void setDay25n(boolean day25n) {
		this.day25n = day25n;
	}

	public boolean isDay26n() {
		return day26n;
	}

	public void setDay26n(boolean day26n) {
		this.day26n = day26n;
	}

	public boolean isDay27n() {
		return day27n;
	}

	public void setDay27n(boolean day27n) {
		this.day27n = day27n;
	}

	public boolean isDay28n() {
		return day28n;
	}

	public void setDay28n(boolean day28n) {
		this.day28n = day28n;
	}

	public boolean isDay29n() {
		return day29n;
	}

	public void setDay29n(boolean day29n) {
		this.day29n = day29n;
	}

	public boolean isDay30n() {
		return day30n;
	}

	public void setDay30n(boolean day30n) {
		this.day30n = day30n;
	}

	public boolean isDay31n() {
		return day31n;
	}

	public void setDay31n(boolean day31n) {
		this.day31n = day31n;
	}

	public List<FlyingSquadGroupsDTO> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<FlyingSquadGroupsDTO> serviceList) {
		this.serviceList = serviceList;
	}

}
