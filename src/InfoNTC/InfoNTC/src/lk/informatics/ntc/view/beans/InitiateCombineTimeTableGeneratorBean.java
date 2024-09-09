package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.BusFareDTO;
import lk.informatics.ntc.model.dto.MidPointTimesDTO;
import lk.informatics.ntc.model.dto.MidpointUIDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.service.CombineTimeTableGenerateService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "initiateCombineTimeTableGeneratorBean")
@ViewScoped
public class InitiateCombineTimeTableGeneratorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String selectedRouteNo;
	private List<RouteDTO> routeList;
	private RouteDTO routeDTO;
	private boolean normal;
	private boolean superLuxury;
	private boolean express;
	private boolean luxury;
	private boolean semiLuxury;
	private String selectedGroup;

	private static List<String> VALID_COLUMN_KEYS = null;
	private String columnTemplate;

	private List<ColumnModel> columnsNormal;
	private List<ColumnModel> columnsSuperLuxury;
	private List<ColumnModel> columnsExpress;
	private List<ColumnModel> columnsLuxury;
	private List<ColumnModel> columnsSemiLuxury;

	private List<MidPointTimesDTO> midPointsNormal;
	private List<MidPointTimesDTO> midPointsNormalList;
	private List<MidPointTimesDTO> midPointsSuperLuxury;
	private List<MidPointTimesDTO> midPointsExpress;
	private List<MidPointTimesDTO> midPointsLuxury;
	private List<MidPointTimesDTO> midPointsSemiLuxury;

	private List<ColumnModel> columnsNormalDtoO;
	private List<ColumnModel> columnsSuperLuxuryDtoO;
	private List<ColumnModel> columnsExpressDtoO;
	private List<ColumnModel> columnsLuxuryDtoO;
	private List<ColumnModel> columnsSemiLuxuryDtoO;

	private List<MidPointTimesDTO> midPointsNormalDtoO;
	private List<MidPointTimesDTO> midPointsSuperLuxuryDtoO;
	private List<MidPointTimesDTO> midPointsExpressDtoO;
	private List<MidPointTimesDTO> midPointsLuxuryDtoO;
	private List<MidPointTimesDTO> midPointsSemiLuxuryDtoO;

	private List<RouteDTO> similarRoutesList;
	private String[] selectedRoutes;

	private List<MidPointTimesDTO> midPointTblOneNormal;
	private List<MidPointTimesDTO> midPointTblOneSuperLuxury;
	private List<MidPointTimesDTO> midPointTblOneExpress;
	private List<MidPointTimesDTO> midPointTblOneLuxury;
	private List<MidPointTimesDTO> midPointTblOneSemiLuxury;
	private List<MidPointTimesDTO> midPointTblOneNormalDtoO;
	private List<MidPointTimesDTO> midPointTblOneSuperLuxuryDtoO;
	private List<MidPointTimesDTO> midPointTblOneExpressDtoO;
	private List<MidPointTimesDTO> midPointTblOneLuxuryDtoO;
	private List<MidPointTimesDTO> midPointTblOneSemiLuxuryDtoO;

	private List<MidPointTimesDTO> midPointTblTwoNormal;
	private List<MidPointTimesDTO> midPointTblTwoSuperLuxury;
	private List<MidPointTimesDTO> midPointTblTwoExpress;
	private List<MidPointTimesDTO> midPointTblTwoLuxury;
	private List<MidPointTimesDTO> midPointTblTwoSemiLuxury;
	private List<MidPointTimesDTO> midPointTblTwoNormalDtoO;
	private List<MidPointTimesDTO> midPointTblTwoSuperLuxuryDtoO;
	private List<MidPointTimesDTO> midPointTblTwoExpressDtoO;
	private List<MidPointTimesDTO> midPointTblTwoLuxuryDtoO;
	private List<MidPointTimesDTO> midPointTblTwoSemiLuxuryDtoO;

	private List<MidPointTimesDTO> midPointTblThreeNormal;
	private List<MidPointTimesDTO> midPointTblThreeSuperLuxury;
	private List<MidPointTimesDTO> midPointTblThreeExpress;
	private List<MidPointTimesDTO> midPointTblThreeLuxury;
	private List<MidPointTimesDTO> midPointTblThreeSemiLuxury;
	private List<MidPointTimesDTO> midPointTblThreeNormalDtoO;
	private List<MidPointTimesDTO> midPointTblThreeSuperLuxuryDtoO;
	private List<MidPointTimesDTO> midPointTblThreeExpressDtoO;
	private List<MidPointTimesDTO> midPointTblThreeLuxuryDtoO;
	private List<MidPointTimesDTO> midPointTblThreeSemiLuxuryDtoO;

	private List<MidPointTimesDTO> midPointTblFourNormal;
	private List<MidPointTimesDTO> midPointTblFourSuperLuxury;
	private List<MidPointTimesDTO> midPointTblFourExpress;
	private List<MidPointTimesDTO> midPointTblFourLuxury;
	private List<MidPointTimesDTO> midPointTblFourSemiLuxury;
	private List<MidPointTimesDTO> midPointTblFourNormalDtoO;
	private List<MidPointTimesDTO> midPointTblFourSuperLuxuryDtoO;
	private List<MidPointTimesDTO> midPointTblFourExpressDtoO;
	private List<MidPointTimesDTO> midPointTblFourLuxuryDtoO;
	private List<MidPointTimesDTO> midPointTblFourSemiLuxuryDtoO;

	private List<MidPointTimesDTO> midPointTblFiveNormal;
	private List<MidPointTimesDTO> midPointTblFiveSuperLuxury;
	private List<MidPointTimesDTO> midPointTblFiveExpress;
	private List<MidPointTimesDTO> midPointTblFiveLuxury;
	private List<MidPointTimesDTO> midPointTblFiveSemiLuxury;
	private List<MidPointTimesDTO> midPointTblFiveNormalDtoO;
	private List<MidPointTimesDTO> midPointTblFiveSuperLuxuryDtoO;
	private List<MidPointTimesDTO> midPointTblFiveExpressDtoO;
	private List<MidPointTimesDTO> midPointTblFiveLuxuryDtoO;
	private List<MidPointTimesDTO> midPointTblFiveSemiLuxuryDtoO;

	private List<ColumnModel> columnsTblOneNormal;
	private List<ColumnModel> columnsTblOneSuperLuxury;
	private List<ColumnModel> columnsTblOneExpress;
	private List<ColumnModel> columnsTblOneLuxury;
	private List<ColumnModel> columnsTblOneSemiLuxury;
	private List<ColumnModel> columnsTblOneNormalDtoO;
	private List<ColumnModel> columnsTblOneSuperLuxuryDtoO;
	private List<ColumnModel> columnsTblOneExpressDtoO;
	private List<ColumnModel> columnsTblOneLuxuryDtoO;
	private List<ColumnModel> columnsTblOneSemiLuxuryDtoO;

	private List<ColumnModel> columnsTblTwoNormal;
	private List<ColumnModel> columnsTblTwoSuperLuxury;
	private List<ColumnModel> columnsTblTwoExpress;
	private List<ColumnModel> columnsTblTwoLuxury;
	private List<ColumnModel> columnsTblTwoSemiLuxury;
	private List<ColumnModel> columnsTblTwoNormalDtoO;
	private List<ColumnModel> columnsTblTwoSuperLuxuryDtoO;
	private List<ColumnModel> columnsTblTwoExpressDtoO;
	private List<ColumnModel> columnsTblTwoLuxuryDtoO;
	private List<ColumnModel> columnsTblTwoSemiLuxuryDtoO;

	private List<ColumnModel> columnsTblThreeNormal;
	private List<ColumnModel> columnsTblThreeSuperLuxury;
	private List<ColumnModel> columnsTblThreeExpress;
	private List<ColumnModel> columnsTblThreeLuxury;
	private List<ColumnModel> columnsTblThreeSemiLuxury;
	private List<ColumnModel> columnsTblThreeNormalDtoO;
	private List<ColumnModel> columnsTblThreeSuperLuxuryDtoO;
	private List<ColumnModel> columnsTblThreeExpressDtoO;
	private List<ColumnModel> columnsTblThreeLuxuryDtoO;
	private List<ColumnModel> columnsTblThreeSemiLuxuryDtoO;

	private List<ColumnModel> columnsTblFourNormal;
	private List<ColumnModel> columnsTblFourSuperLuxury;
	private List<ColumnModel> columnsTblFourExpress;
	private List<ColumnModel> columnsTblFourLuxury;
	private List<ColumnModel> columnsTblFourSemiLuxury;
	private List<ColumnModel> columnsTblFourNormalDtoO;
	private List<ColumnModel> columnsTblFourSuperLuxuryDtoO;
	private List<ColumnModel> columnsTblFourExpressDtoO;
	private List<ColumnModel> columnsTblFourLuxuryDtoO;
	private List<ColumnModel> columnsTblFourSemiLuxuryDtoO;

	private List<ColumnModel> columnsTblFiveNormal;
	private List<ColumnModel> columnsTblFiveSuperLuxury;
	private List<ColumnModel> columnsTblFiveExpress;
	private List<ColumnModel> columnsTblFiverLuxury;
	private List<ColumnModel> columnsTblFiveSemiLuxury;
	private List<ColumnModel> columnsTblFiveNormalDtoO;
	private List<ColumnModel> columnsTblFiveSuperLuxuryDtoO;
	private List<ColumnModel> columnsTblFiveExpressDtoO;
	private List<ColumnModel> columnsTblFiverLuxuryDtoO;
	private List<ColumnModel> columnsTblFiveSemiLuxuryDtoO;

	private String similarRouteOne;
	private String similarRouteTwo;
	private String similarRouteThree;
	private String similarRouteFour;
	private String similarRouteFive;

	private boolean similarRouteOneRender;
	private boolean similarRouteTwoRender;
	private boolean similarRouteThreeRender;
	private boolean similarRouteFourRender;
	private boolean similarRouteFiveRender;

	private boolean similarRoutesData;

	private MidPointTimesDTO selectedMidPoint;
	private MidPointTimesDTO editMidpointDTO;

	private String routeOnEdit;
	private String tripType;

	private List<BusFareDTO> categoryList;
	private String selectedCategory;

	private CombineTimeTableGenerateService combineTimeTableGenerateService;

	@PostConstruct
	public void init() {

		combineTimeTableGenerateService = (CombineTimeTableGenerateService) SpringApplicationContex
				.getBean("combineTimeTableGenerateService");
		loadData();
	}

	public void loadData() {
		setRouteOnEdit(null);
		selectedRouteNo = null;
		routeDTO = new RouteDTO();
		selectedGroup = null;

		routeList = new ArrayList<RouteDTO>();
		routeList = combineTimeTableGenerateService.getRouteNoList();

		categoryList = new ArrayList<BusFareDTO>();
		categoryList = combineTimeTableGenerateService.retrieveCategoryList();

		columnTemplate = null;

		setColumnsNormal(new ArrayList<ColumnModel>());

		columnsNormal = new ArrayList<ColumnModel>();
		columnsSuperLuxury = new ArrayList<ColumnModel>();
		columnsExpress = new ArrayList<ColumnModel>();
		columnsLuxury = new ArrayList<ColumnModel>();
		columnsSemiLuxury = new ArrayList<ColumnModel>();

		midPointsNormal = new ArrayList<MidPointTimesDTO>();
		midPointsSuperLuxury = new ArrayList<MidPointTimesDTO>();
		midPointsExpress = new ArrayList<MidPointTimesDTO>();
		midPointsLuxury = new ArrayList<MidPointTimesDTO>();
		midPointsSemiLuxury = new ArrayList<MidPointTimesDTO>();

		columnsNormalDtoO = new ArrayList<ColumnModel>();
		columnsSuperLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsExpressDtoO = new ArrayList<ColumnModel>();
		columnsLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsSemiLuxuryDtoO = new ArrayList<ColumnModel>();

		midPointsNormalDtoO = new ArrayList<MidPointTimesDTO>();
		midPointsSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointsExpressDtoO = new ArrayList<MidPointTimesDTO>();
		midPointsLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointsSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();

		midPointsNormalList = new ArrayList<MidPointTimesDTO>();

		normal = false;
		superLuxury = false;
		express = false;
		luxury = false;
		semiLuxury = false;

		similarRoutesList = new ArrayList<RouteDTO>();

		midPointTblOneNormal = new ArrayList<MidPointTimesDTO>();
		midPointTblOneSuperLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblOneExpress = new ArrayList<MidPointTimesDTO>();
		midPointTblOneLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblOneSemiLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblOneNormalDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblOneSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblOneExpressDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblOneLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblOneSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();

		midPointTblTwoNormal = new ArrayList<MidPointTimesDTO>();
		midPointTblTwoSuperLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblTwoExpress = new ArrayList<MidPointTimesDTO>();
		midPointTblTwoLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblTwoSemiLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblTwoNormalDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblTwoSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblTwoExpressDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblTwoLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblTwoSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();

		midPointTblThreeNormal = new ArrayList<MidPointTimesDTO>();
		midPointTblThreeSuperLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblThreeExpress = new ArrayList<MidPointTimesDTO>();
		midPointTblThreeLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblThreeSemiLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblThreeNormalDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblThreeSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblThreeExpressDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblThreeLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblThreeSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();

		midPointTblFourNormal = new ArrayList<MidPointTimesDTO>();
		midPointTblFourSuperLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblFourExpress = new ArrayList<MidPointTimesDTO>();
		midPointTblFourLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblFourSemiLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblFourNormalDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblFourSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblFourExpressDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblFourLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblFourSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();

		midPointTblFiveNormal = new ArrayList<MidPointTimesDTO>();
		midPointTblFiveSuperLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblFiveExpress = new ArrayList<MidPointTimesDTO>();
		midPointTblFiveLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblFiveSemiLuxury = new ArrayList<MidPointTimesDTO>();
		midPointTblFiveNormalDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblFiveSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblFiveExpressDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblFiveLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
		midPointTblFiveSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();

		columnsTblOneNormal = new ArrayList<ColumnModel>();
		columnsTblOneSuperLuxury = new ArrayList<ColumnModel>();
		columnsTblOneExpress = new ArrayList<ColumnModel>();
		columnsTblOneLuxury = new ArrayList<ColumnModel>();
		columnsTblOneSemiLuxury = new ArrayList<ColumnModel>();
		columnsTblOneNormalDtoO = new ArrayList<ColumnModel>();
		columnsTblOneSuperLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsTblOneExpressDtoO = new ArrayList<ColumnModel>();
		columnsTblOneLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsTblOneSemiLuxuryDtoO = new ArrayList<ColumnModel>();

		columnsTblTwoNormal = new ArrayList<ColumnModel>();
		columnsTblTwoSuperLuxury = new ArrayList<ColumnModel>();
		columnsTblTwoExpress = new ArrayList<ColumnModel>();
		columnsTblTwoLuxury = new ArrayList<ColumnModel>();
		columnsTblTwoSemiLuxury = new ArrayList<ColumnModel>();
		columnsTblTwoNormalDtoO = new ArrayList<ColumnModel>();
		columnsTblTwoSuperLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsTblTwoExpressDtoO = new ArrayList<ColumnModel>();
		columnsTblTwoLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsTblTwoSemiLuxuryDtoO = new ArrayList<ColumnModel>();

		columnsTblThreeNormal = new ArrayList<ColumnModel>();
		columnsTblThreeSuperLuxury = new ArrayList<ColumnModel>();
		columnsTblThreeExpress = new ArrayList<ColumnModel>();
		columnsTblThreeLuxury = new ArrayList<ColumnModel>();
		columnsTblThreeSemiLuxury = new ArrayList<ColumnModel>();
		columnsTblThreeNormalDtoO = new ArrayList<ColumnModel>();
		columnsTblThreeSuperLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsTblThreeExpressDtoO = new ArrayList<ColumnModel>();
		columnsTblThreeLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsTblThreeSemiLuxuryDtoO = new ArrayList<ColumnModel>();

		columnsTblFourNormal = new ArrayList<ColumnModel>();
		columnsTblFourSuperLuxury = new ArrayList<ColumnModel>();
		columnsTblFourExpress = new ArrayList<ColumnModel>();
		columnsTblFourLuxury = new ArrayList<ColumnModel>();
		columnsTblFourSemiLuxury = new ArrayList<ColumnModel>();
		columnsTblFourNormalDtoO = new ArrayList<ColumnModel>();
		columnsTblFourSuperLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsTblFourExpressDtoO = new ArrayList<ColumnModel>();
		columnsTblFourLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsTblFourSemiLuxuryDtoO = new ArrayList<ColumnModel>();

		columnsTblFiveNormal = new ArrayList<ColumnModel>();
		columnsTblFiveSuperLuxury = new ArrayList<ColumnModel>();
		columnsTblFiveExpress = new ArrayList<ColumnModel>();
		columnsTblFiverLuxury = new ArrayList<ColumnModel>();
		columnsTblFiveSemiLuxury = new ArrayList<ColumnModel>();
		columnsTblFiveNormalDtoO = new ArrayList<ColumnModel>();
		columnsTblFiveSuperLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsTblFiveExpressDtoO = new ArrayList<ColumnModel>();
		columnsTblFiverLuxuryDtoO = new ArrayList<ColumnModel>();
		columnsTblFiveSemiLuxuryDtoO = new ArrayList<ColumnModel>();

		similarRouteOne = null;
		similarRouteTwo = null;
		similarRouteThree = null;
		similarRouteFour = null;
		similarRouteFive = null;

		similarRouteOneRender = false;
		similarRouteTwoRender = false;
		similarRouteThreeRender = false;
		similarRouteFourRender = false;
		similarRouteFiveRender = false;

		similarRoutesData = false;

		selectedMidPoint = new MidPointTimesDTO();
		editMidpointDTO = new MidPointTimesDTO();

		routeOnEdit = null;
		tripType = null;

		selectedCategory = null;
	}

	public void generateOriginAndDestination() {

		TimeTableDTO dto = new TimeTableDTO();
		dto = combineTimeTableGenerateService.getRouteData(selectedRouteNo);

		routeDTO.setOrigin(dto.getOrigin());
		routeDTO.setDestination(dto.getDestination());

	}

	public void searchBtnAction() {

		similarRoutesList = new ArrayList<RouteDTO>();
		similarRoutesList = combineTimeTableGenerateService.retrieveSimilarRoutes(selectedRouteNo);

		boolean routeNoValid = validateRouteNo();
		if (!routeNoValid) {
			sessionBackingBean.setMessage("Please select Route No.");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			return;
		}

		boolean valid = validateCategories();
		if (!valid) {
			sessionBackingBean.setMessage("Please select a Category");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			return;
		}

		/** check category types start **/
		if (selectedCategory.equalsIgnoreCase("001")) {
			normal = true;
			luxury = false;
			superLuxury = false;
			semiLuxury = false;
			express = false;
		} else if (selectedCategory.equalsIgnoreCase("002")) {
			luxury = true;
			normal = false;
			superLuxury = false;
			semiLuxury = false;
			express = false;
		} else if (selectedCategory.equalsIgnoreCase("003")) {
			superLuxury = true;
			normal = false;
			luxury = false;
			semiLuxury = false;
			express = false;
		} else if (selectedCategory.equalsIgnoreCase("004")) {
			semiLuxury = true;
			superLuxury = false;
			normal = false;
			luxury = false;
			express = false;
		} else if (selectedCategory.equalsIgnoreCase("EB")) {
			express = true;
			semiLuxury = false;
			superLuxury = false;
			normal = false;
			luxury = false;
		}
		/** check category types end **/

		similarRoutesData = true;

		if (normal) {
			// O to D start
			/** check already insert to database start **/
			boolean insert = combineTimeTableGenerateService
					.checkDataAvailableInNt_t_midpoint_timetable(selectedRouteNo, "001", "O", selectedGroup);
			/** check already insert to database end **/
			if (insert) {
				// select from DB table and show
				midPointsNormal = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "001", "O",
						selectedGroup);
				midPointsNormalList = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "001", "O",
						selectedGroup);

				List<String> midPointNamesList = new ArrayList<String>();
				midPointNamesList = combineTimeTableGenerateService.retrieveMidPointNamesForRoute(selectedRouteNo,
						"001");
				createDynamicColumnsNormal(midPointNamesList);
			} else {
				// calculate, save in DB and show
				midPointsNormal = BusTypeProcess("001", selectedRouteNo);
				midPointsNormalList = BusTypeProcessDtoO("001", selectedRouteNo);
				List<String> midPointNamesList = new ArrayList<String>();
				midPointNamesList = combineTimeTableGenerateService.retrieveMidPointNamesForRoute(selectedRouteNo,
						"001");
				createDynamicColumnsNormal(midPointNamesList);
				/*
				 * combineTimeTableGenerateService.
				 * insertMidpointDataToNt_t_midpoint_timetable(
				 * midPointsNormalDtoO, selectedRouteNo, "001", "O" ,
				 * selectedGroup, sessionBackingBean.getLoginUser());
				 */
				/** comment by tharushi.e **/
				combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(midPointsNormal,
						selectedRouteNo, "001", "O", selectedGroup, sessionBackingBean.getLoginUser());
				midPointsNormal = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "001", "O",
						selectedGroup);
				midPointsNormalList = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "001", "O",
						selectedGroup);
			}
			// O to D end

			// D to O start
			/** check already insert to database start **/
			boolean insertDtoO = combineTimeTableGenerateService
					.checkDataAvailableInNt_t_midpoint_timetable(selectedRouteNo, "001", "D", selectedGroup);
			/** check already insert to database end **/
			if (insertDtoO) {
				// select from DB table and show
				midPointsNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "001", "D",
						selectedGroup);

				List<String> midPointNamesListDtoO = new ArrayList<String>();
				midPointNamesListDtoO = combineTimeTableGenerateService
						.retrieveMidPointNamesForRouteDtoO(selectedRouteNo, "001");
				createDynamicColumnsNormalDtoO(midPointNamesListDtoO);
			} else {
				// calculate, save in DB and show
				midPointsNormalDtoO = BusTypeProcessDtoO("001", selectedRouteNo);
				List<String> midPointNamesListDtoO = new ArrayList<String>();
				midPointNamesListDtoO = combineTimeTableGenerateService
						.retrieveMidPointNamesForRouteDtoO(selectedRouteNo, "001");
				createDynamicColumnsNormalDtoO(midPointNamesListDtoO);
				combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(midPointsNormalDtoO,
						selectedRouteNo, "001", "D", selectedGroup, sessionBackingBean.getLoginUser());
				midPointsNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "001", "D",
						selectedGroup);
			}
			// D to O end
		}
		if (express) {
			// O to D start
			/** check already insert to database start **/
			boolean insert = combineTimeTableGenerateService
					.checkDataAvailableInNt_t_midpoint_timetable(selectedRouteNo, "EB", "O", selectedGroup);
			/** check already insert to database end **/
			if (insert) {
				// select from DB table and show
				midPointsExpress = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "EB", "O",
						selectedGroup);

				List<String> midPointNamesList = new ArrayList<String>();
				midPointNamesList = combineTimeTableGenerateService.retrieveMidPointNamesForRoute(selectedRouteNo,
						"EB");
				createDynamicColumnsExpress(midPointNamesList);

			} else {
				// calculate, save in DB and show
				midPointsExpress = BusTypeProcess("EB", selectedRouteNo);
				List<String> midPointNamesList = new ArrayList<String>();
				midPointNamesList = combineTimeTableGenerateService.retrieveMidPointNamesForRoute(selectedRouteNo,
						"EB");
				createDynamicColumnsExpress(midPointNamesList);
				combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(midPointsExpress,
						selectedRouteNo, "EB", "O", selectedGroup, sessionBackingBean.getLoginUser());
				midPointsExpress = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "EB", "O",
						selectedGroup);
			}
			// O to D end

			// D to O start
			/** check already insert to database start **/
			boolean insertDtoO = combineTimeTableGenerateService
					.checkDataAvailableInNt_t_midpoint_timetable(selectedRouteNo, "EB", "D", selectedGroup);
			/** check already insert to database end **/
			if (insertDtoO) {
				// select from DB table and show
				midPointsExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "EB", "D",
						selectedGroup);

				List<String> midPointNamesListDtoO = new ArrayList<String>();
				midPointNamesListDtoO = combineTimeTableGenerateService
						.retrieveMidPointNamesForRouteDtoO(selectedRouteNo, "EB");
				createDynamicColumnsExpressDtoO(midPointNamesListDtoO);

			} else {
				// calculate, save in DB and show
				midPointsExpressDtoO = BusTypeProcessDtoO("EB", selectedRouteNo);
				List<String> midPointNamesListDtoO = new ArrayList<String>();
				midPointNamesListDtoO = combineTimeTableGenerateService
						.retrieveMidPointNamesForRouteDtoO(selectedRouteNo, "EB");
				createDynamicColumnsExpressDtoO(midPointNamesListDtoO);
				combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(midPointsExpressDtoO,
						selectedRouteNo, "EB", "D", selectedGroup, sessionBackingBean.getLoginUser());
				midPointsExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "EB", "D",
						selectedGroup);
			}
			// D to O end
		}
		if (luxury) {
			// O to D start
			/** check already insert to database start **/
			boolean insert = combineTimeTableGenerateService
					.checkDataAvailableInNt_t_midpoint_timetable(selectedRouteNo, "002", "O", selectedGroup);
			/** check already insert to database end **/
			if (insert) {
				// select from DB table and show
				midPointsLuxury = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "002", "O",
						selectedGroup);

				List<String> midPointNamesList = new ArrayList<String>();
				midPointNamesList = combineTimeTableGenerateService.retrieveMidPointNamesForRoute(selectedRouteNo,
						"002");
				createDynamicColumnsLuxury(midPointNamesList);

			} else {
				// calculate, save in DB and show
				midPointsLuxury = BusTypeProcess("002", selectedRouteNo);
				List<String> midPointNamesList = new ArrayList<String>();
				midPointNamesList = combineTimeTableGenerateService.retrieveMidPointNamesForRoute(selectedRouteNo,
						"002");
				createDynamicColumnsLuxury(midPointNamesList);
				combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(midPointsLuxury,
						selectedRouteNo, "002", "O", selectedGroup, sessionBackingBean.getLoginUser());
				midPointsLuxury = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "002", "O",
						selectedGroup);
			}
			// O to D end

			// D to O start
			/** check already insert to database start **/
			boolean insertDtoO = combineTimeTableGenerateService
					.checkDataAvailableInNt_t_midpoint_timetable(selectedRouteNo, "002", "D", selectedGroup);
			/** check already insert to database end **/
			if (insertDtoO) {
				// select from DB table and show
				midPointsLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "002", "D",
						selectedGroup);

				List<String> midPointNamesListDtoO = new ArrayList<String>();
				midPointNamesListDtoO = combineTimeTableGenerateService
						.retrieveMidPointNamesForRouteDtoO(selectedRouteNo, "002");
				createDynamicColumnsExpressDtoO(midPointNamesListDtoO);

			} else {
				midPointsLuxuryDtoO = BusTypeProcessDtoO("002", selectedRouteNo);
				List<String> midPointNamesListDtoO = new ArrayList<String>();
				midPointNamesListDtoO = combineTimeTableGenerateService
						.retrieveMidPointNamesForRouteDtoO(selectedRouteNo, "002");
				createDynamicColumnsLuxuryDtoO(midPointNamesListDtoO);
				combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(midPointsLuxuryDtoO,
						selectedRouteNo, "002", "D", selectedGroup, sessionBackingBean.getLoginUser());
				midPointsLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "002", "D",
						selectedGroup);
			}
			// D to O end
		}
		if (semiLuxury) {
			// O to D start
			boolean insert = combineTimeTableGenerateService
					.checkDataAvailableInNt_t_midpoint_timetable(selectedRouteNo, "004", "O", selectedGroup);
			/** check already insert to database end **/
			if (insert) {
				// select from DB table and show
				midPointsSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "004", "O",
						selectedGroup);

				List<String> midPointNamesList = new ArrayList<String>();
				midPointNamesList = combineTimeTableGenerateService.retrieveMidPointNamesForRoute(selectedRouteNo,
						"004");
				createDynamicColumnsSemiLuxury(midPointNamesList);

			} else {
				// calculate, save in DB and show
				midPointsSemiLuxury = BusTypeProcess("004", selectedRouteNo);
				List<String> midPointNamesList = new ArrayList<String>();
				midPointNamesList = combineTimeTableGenerateService.retrieveMidPointNamesForRoute(selectedRouteNo,
						"004");
				createDynamicColumnsSemiLuxury(midPointNamesList);
				combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(midPointsSemiLuxury,
						selectedRouteNo, "004", "O", selectedGroup, sessionBackingBean.getLoginUser());
				midPointsSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "004", "O",
						selectedGroup);
			}
			// O to D end

			// D to O start
			/** check already insert to database start **/
			boolean insertDtoO = combineTimeTableGenerateService
					.checkDataAvailableInNt_t_midpoint_timetable(selectedRouteNo, "004", "D", selectedGroup);
			/** check already insert to database end **/
			if (insertDtoO) {
				// select from DB table and show
				midPointsSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "004",
						"D", selectedGroup);

				List<String> midPointNamesListDtoO = new ArrayList<String>();
				midPointNamesListDtoO = combineTimeTableGenerateService
						.retrieveMidPointNamesForRouteDtoO(selectedRouteNo, "004");
				createDynamicColumnsSemiLuxuryDtoO(midPointNamesListDtoO);

			} else {
				// calculate, save in DB and show
				midPointsSemiLuxuryDtoO = BusTypeProcessDtoO("004", selectedRouteNo);
				List<String> midPointNamesListDtoO = new ArrayList<String>();
				midPointNamesListDtoO = combineTimeTableGenerateService
						.retrieveMidPointNamesForRouteDtoO(selectedRouteNo, "004");
				createDynamicColumnsSemiLuxuryDtoO(midPointNamesListDtoO);
				combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(midPointsSemiLuxuryDtoO,
						selectedRouteNo, "004", "D", selectedGroup, sessionBackingBean.getLoginUser());
				midPointsSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "004",
						"D", selectedGroup);
			}
			// D to O end
		}
		if (superLuxury) {
			// O to D start
			boolean insert = combineTimeTableGenerateService
					.checkDataAvailableInNt_t_midpoint_timetable(selectedRouteNo, "003", "O", selectedGroup);
			/** check already insert to database end **/
			if (insert) {
				// select from DB table and show
				midPointsSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "003", "O",
						selectedGroup);

				List<String> midPointNamesList = new ArrayList<String>();
				midPointNamesList = combineTimeTableGenerateService.retrieveMidPointNamesForRoute(selectedRouteNo,
						"003");
				createDynamicColumnsSuperLuxury(midPointNamesList);

			} else {
				// calculate, save in DB and show
				midPointsSuperLuxury = BusTypeProcess("003", selectedRouteNo);
				List<String> midPointNamesList = new ArrayList<String>();
				midPointNamesList = combineTimeTableGenerateService.retrieveMidPointNamesForRoute(selectedRouteNo,
						"003");
				createDynamicColumnsSuperLuxury(midPointNamesList);
				combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(midPointsSuperLuxury,
						selectedRouteNo, "003", "O", selectedGroup, sessionBackingBean.getLoginUser());
				midPointsSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "003", "O",
						selectedGroup);
			}
			// O to D end

			// D to O start
			/** check already insert to database start **/
			boolean insertDtoO = combineTimeTableGenerateService
					.checkDataAvailableInNt_t_midpoint_timetable(selectedRouteNo, "003", "D", selectedGroup);
			/** check already insert to database end **/
			if (insertDtoO) {
				// select from DB table and show
				midPointsSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "003",
						"D", selectedGroup);

				List<String> midPointNamesListDtoO = new ArrayList<String>();
				midPointNamesListDtoO = combineTimeTableGenerateService
						.retrieveMidPointNamesForRouteDtoO(selectedRouteNo, "003");
				createDynamicColumnsSuperLuxuryDtoO(midPointNamesListDtoO);

			} else {
				// calculate, save in DB and show
				midPointsSuperLuxuryDtoO = BusTypeProcessDtoO("003", selectedRouteNo);
				List<String> midPointNamesListDtoO = new ArrayList<String>();
				midPointNamesListDtoO = combineTimeTableGenerateService
						.retrieveMidPointNamesForRouteDtoO(selectedRouteNo, "003");
				createDynamicColumnsSuperLuxuryDtoO(midPointNamesListDtoO);
				combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(midPointsSuperLuxuryDtoO,
						selectedRouteNo, "003", "D", selectedGroup, sessionBackingBean.getLoginUser());
				midPointsSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(selectedRouteNo, "003",
						"D", selectedGroup);
			}
			// D to O end
		}

		RequestContext.getCurrentInstance().update("frmcombineTimeTable");
	}

	private boolean validateRouteNo() {

		if (selectedRouteNo != null && !selectedRouteNo.isEmpty() && !selectedRouteNo.trim().equalsIgnoreCase("")) {
			return true;
		}

		return false;
	}

	private boolean validateCategories() {

		if (selectedCategory != null && !selectedCategory.isEmpty() && !selectedCategory.trim().equalsIgnoreCase("")) {
			return true;
		}

		return false;
	}

	private void createDynamicColumnsSuperLuxuryDtoO(List<String> midPointNamesListDtoO) {

		List<String> columnKeys = new ArrayList<String>();
		VALID_COLUMN_KEYS = new ArrayList<String>();

		for (int i = 0; i < midPointNamesListDtoO.size(); i++) {
			VALID_COLUMN_KEYS.add(midPointNamesListDtoO.get(i));
		}

		for (int i = 1; i <= midPointNamesListDtoO.size(); i++) {
			columnKeys.add(String.valueOf(i));
		}

		columnsSuperLuxuryDtoO = new ArrayList<ColumnModel>();

		int count = 0;
		for (String columnKey : columnKeys) {
			String key = VALID_COLUMN_KEYS.get(count);
			columnsSuperLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
			count = count + 1;
		}

	}

	private void createDynamicColumnsSemiLuxuryDtoO(List<String> midPointNamesListDtoO) {

		List<String> columnKeys = new ArrayList<String>();
		VALID_COLUMN_KEYS = new ArrayList<String>();

		for (int i = 0; i < midPointNamesListDtoO.size(); i++) {
			VALID_COLUMN_KEYS.add(midPointNamesListDtoO.get(i));
		}

		for (int i = 1; i <= midPointNamesListDtoO.size(); i++) {
			columnKeys.add(String.valueOf(i));
		}

		columnsSemiLuxuryDtoO = new ArrayList<ColumnModel>();

		int count = 0;
		for (String columnKey : columnKeys) {
			String key = VALID_COLUMN_KEYS.get(count);
			columnsSemiLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
			count = count + 1;
		}

	}

	private void createDynamicColumnsLuxuryDtoO(List<String> midPointNamesListDtoO) {

		List<String> columnKeys = new ArrayList<String>();
		VALID_COLUMN_KEYS = new ArrayList<String>();

		for (int i = 0; i < midPointNamesListDtoO.size(); i++) {
			VALID_COLUMN_KEYS.add(midPointNamesListDtoO.get(i));
		}

		for (int i = 1; i <= midPointNamesListDtoO.size(); i++) {
			columnKeys.add(String.valueOf(i));
		}

		columnsLuxuryDtoO = new ArrayList<ColumnModel>();

		int count = 0;
		for (String columnKey : columnKeys) {
			String key = VALID_COLUMN_KEYS.get(count);
			columnsLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
			count = count + 1;
		}

	}

	private void createDynamicColumnsExpressDtoO(List<String> midPointNamesList) {

		List<String> columnKeys = new ArrayList<String>();
		VALID_COLUMN_KEYS = new ArrayList<String>();

		for (int i = 0; i < midPointNamesList.size(); i++) {
			VALID_COLUMN_KEYS.add(midPointNamesList.get(i));
		}

		for (int i = 1; i <= midPointNamesList.size(); i++) {
			columnKeys.add(String.valueOf(i));
		}

		columnsExpressDtoO = new ArrayList<ColumnModel>();

		int count = 0;
		for (String columnKey : columnKeys) {
			String key = VALID_COLUMN_KEYS.get(count);
			columnsExpressDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
			count = count + 1;
		}

	}

	private void createDynamicColumnsNormalDtoO(List<String> midPointNamesList) {

		List<String> columnKeys = new ArrayList<String>();
		VALID_COLUMN_KEYS = new ArrayList<String>();

		for (int i = 0; i < midPointNamesList.size(); i++) {
			VALID_COLUMN_KEYS.add(midPointNamesList.get(i));
		}

		for (int i = 1; i <= midPointNamesList.size(); i++) {
			columnKeys.add(String.valueOf(i));
		}

		columnsNormalDtoO = new ArrayList<ColumnModel>();

		int count = 0;
		for (String columnKey : columnKeys) {
			String key = VALID_COLUMN_KEYS.get(count);
			columnsNormalDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
			count = count + 1;
		}

	}

	private List<MidPointTimesDTO> BusTypeProcessDtoO(String busType, String routeNo) {

		List<MidPointTimesDTO> midPoints = new ArrayList<MidPointTimesDTO>();

		try {
			List<String> MidPointTimesForCateList = new ArrayList<String>();

			List<TimeTableDTO> startEndTimeList = new ArrayList<TimeTableDTO>();
			startEndTimeList = combineTimeTableGenerateService.retireveStartEndTimes(busType, routeNo, selectedGroup);

			List<MidpointUIDTO> midPointTimeList = new ArrayList<MidpointUIDTO>();
			midPointTimeList = combineTimeTableGenerateService.retrieveMidPointTimeTakenForRouteDtoO(routeNo, busType);

			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			boolean firstTimeCalculate = false;
			String startTime = null;
			for (TimeTableDTO timeTableDTO : startEndTimeList) {
				firstTimeCalculate = false;
				MidPointTimesForCateList = new ArrayList<String>();
				for (MidpointUIDTO midPointDTO : midPointTimeList) {

					if (!firstTimeCalculate) {
						startTime = timeTableDTO.getStartTime();
						firstTimeCalculate = true;
					}

					String plusTime = midPointDTO.getTimeTakenStr();

					Date startTimeDate = timeFormat.parse(startTime);
					Date plusTimeDate = timeFormat.parse(plusTime);

					long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
					String finalAddedTime = timeFormat.format(new Date(addTime));

					startTime = finalAddedTime;

					MidPointTimesForCateList.add(startTime + " (" + timeTableDTO.getBusNo() + ")");
				}

				MidPointTimesDTO dto = new MidPointTimesDTO();
				dto = addDataToMidPointsList(MidPointTimesForCateList, busType);
				midPoints.add(dto);
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		return midPoints;
	}

	public List<MidPointTimesDTO> BusTypeProcess(String busType, String routeNo) {

		List<MidPointTimesDTO> midPoints = new ArrayList<MidPointTimesDTO>();

		try {
			List<String> MidPointTimesForCateList = new ArrayList<String>();

			List<TimeTableDTO> startEndTimeList = new ArrayList<TimeTableDTO>();
			startEndTimeList = combineTimeTableGenerateService.retireveStartEndTimes(busType, routeNo, selectedGroup);

			List<MidpointUIDTO> midPointTimeList = new ArrayList<MidpointUIDTO>();
			midPointTimeList = combineTimeTableGenerateService.retrieveMidPointTimeTakenForRoute(routeNo, busType);

			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			boolean firstTimeCalculate = false;
			String startTime = null;
			for (TimeTableDTO timeTableDTO : startEndTimeList) {
				firstTimeCalculate = false;
				MidPointTimesForCateList = new ArrayList<String>();
				for (MidpointUIDTO midPointDTO : midPointTimeList) {

					if (!firstTimeCalculate) {
						startTime = timeTableDTO.getStartTime();
						firstTimeCalculate = true;
					}

					String plusTime = midPointDTO.getTimeTakenStr();

					Date startTimeDate = timeFormat.parse(startTime);
					Date plusTimeDate = timeFormat.parse(plusTime);

					long addTime = startTimeDate.getTime() + plusTimeDate.getTime();
					String finalAddedTime = timeFormat.format(new Date(addTime));

					startTime = finalAddedTime;

					MidPointTimesForCateList.add(startTime + " (" + timeTableDTO.getBusNo() + ")");
				}

				MidPointTimesDTO dto = new MidPointTimesDTO();
				dto = addDataToMidPointsList(MidPointTimesForCateList, busType);
				midPoints.add(dto);
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		return midPoints;
	}

	static public class ColumnModel implements Serializable {

		private static final long serialVersionUID = 1L;

		private String header;
		private String property;

		public ColumnModel(String header, String property) {
			this.header = header;
			this.property = property;
		}

		public String getHeader() {
			return header;
		}

		public String getProperty() {
			return property;
		}
	}

	public MidPointTimesDTO addDataToMidPointsList(List<String> midPointsStringList, String busType) {

		MidPointTimesDTO dto = new MidPointTimesDTO();
		int count = 0;
		String busCategoryDesc = combineTimeTableGenerateService.retrieveBusTypeDesc(busType);
		for (String s : midPointsStringList) {

			if (count == 0) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint1(midPointsStringList.get(0));
			}
			if (count == 1) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint2(midPointsStringList.get(1));
			}
			if (count == 2) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint3(midPointsStringList.get(2));
			}
			if (count == 3) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint34(midPointsStringList.get(3));
			}
			if (count == 34) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint5(midPointsStringList.get(34));
			}
			if (count == 5) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint6(midPointsStringList.get(5));
			}
			if (count == 6) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint7(midPointsStringList.get(6));
			}
			if (count == 7) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint8(midPointsStringList.get(7));
			}
			if (count == 8) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint9(midPointsStringList.get(8));
			}
			if (count == 9) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint10(midPointsStringList.get(9));
			}
			if (count == 10) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint11(midPointsStringList.get(10));
			}
			if (count == 11) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint12(midPointsStringList.get(11));
			}
			if (count == 12) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint13(midPointsStringList.get(12));
			}
			if (count == 13) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint14(midPointsStringList.get(13));
			}
			if (count == 14) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint15(midPointsStringList.get(134));
			}
			if (count == 15) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint16(midPointsStringList.get(15));
			}
			if (count == 16) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint17(midPointsStringList.get(16));
			}
			if (count == 17) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint18(midPointsStringList.get(17));
			}
			if (count == 18) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint19(midPointsStringList.get(18));
			}
			if (count == 19) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint20(midPointsStringList.get(19));
			}
			if (count == 20) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint21(midPointsStringList.get(20));
			}
			if (count == 21) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint22(midPointsStringList.get(21));
			}
			if (count == 22) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint23(midPointsStringList.get(22));
			}
			if (count == 23) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint24(midPointsStringList.get(23));
			}
			if (count == 234) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint25(midPointsStringList.get(234));
			}
			if (count == 25) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint26(midPointsStringList.get(25));
			}
			if (count == 26) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint27(midPointsStringList.get(26));
			}
			if (count == 27) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint28(midPointsStringList.get(27));
			}
			if (count == 28) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint29(midPointsStringList.get(28));
			}
			if (count == 29) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint30(midPointsStringList.get(29));
			}
			if (count == 30) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint31(midPointsStringList.get(30));
			}
			if (count == 31) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint32(midPointsStringList.get(31));
			}
			if (count == 32) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint33(midPointsStringList.get(32));
			}
			if (count == 33) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint34(midPointsStringList.get(33));
			}
			if (count == 334) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint35(midPointsStringList.get(334));
			}
			if (count == 35) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint36(midPointsStringList.get(35));
			}
			if (count == 36) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint37(midPointsStringList.get(36));
			}
			if (count == 37) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint38(midPointsStringList.get(37));
			}
			if (count == 38) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint39(midPointsStringList.get(38));
			}
			if (count == 39) {
				dto.setCategory(busCategoryDesc);
				dto.setMidPoint40(midPointsStringList.get(39));
			}
			count = count + 1;
		}

		return dto;
	}

	private void createDynamicColumnsNormal(List<String> midPointNamesList) {

		List<String> columnKeys = new ArrayList<String>();
		VALID_COLUMN_KEYS = new ArrayList<String>();

		for (int i = 0; i < midPointNamesList.size(); i++) {
			VALID_COLUMN_KEYS.add(midPointNamesList.get(i));
		}

		for (int i = 1; i <= midPointNamesList.size(); i++) {
			columnKeys.add(String.valueOf(i));
		}

		columnsNormal = new ArrayList<ColumnModel>();

		int count = 0;
		for (String columnKey : columnKeys) {
			String key = VALID_COLUMN_KEYS.get(count);
			columnsNormal.add(new ColumnModel(key, "midPoint" + columnKey));
			count = count + 1;
		}

	}

	private void createDynamicColumnsExpress(List<String> midPointNamesList) {

		List<String> columnKeys = new ArrayList<String>();
		VALID_COLUMN_KEYS = new ArrayList<String>();

		for (int i = 0; i < midPointNamesList.size(); i++) {
			VALID_COLUMN_KEYS.add(midPointNamesList.get(i));
		}

		for (int i = 1; i <= midPointNamesList.size(); i++) {
			columnKeys.add(String.valueOf(i));
		}

		columnsExpress = new ArrayList<ColumnModel>();

		int count = 0;
		for (String columnKey : columnKeys) {
			String key = VALID_COLUMN_KEYS.get(count);
			columnsExpress.add(new ColumnModel(key, "midPoint" + columnKey));
			count = count + 1;
		}

	}

	private void createDynamicColumnsLuxury(List<String> midPointNamesList) {

		List<String> columnKeys = new ArrayList<String>();
		VALID_COLUMN_KEYS = new ArrayList<String>();

		for (int i = 0; i < midPointNamesList.size(); i++) {
			VALID_COLUMN_KEYS.add(midPointNamesList.get(i));
		}

		for (int i = 1; i <= midPointNamesList.size(); i++) {
			columnKeys.add(String.valueOf(i));
		}

		columnsLuxury = new ArrayList<ColumnModel>();

		int count = 0;
		for (String columnKey : columnKeys) {
			String key = VALID_COLUMN_KEYS.get(count);
			columnsLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
			count = count + 1;
		}

	}

	private void createDynamicColumnsSemiLuxury(List<String> midPointNamesList) {

		List<String> columnKeys = new ArrayList<String>();
		VALID_COLUMN_KEYS = new ArrayList<String>();

		for (int i = 0; i < midPointNamesList.size(); i++) {
			VALID_COLUMN_KEYS.add(midPointNamesList.get(i));
		}

		for (int i = 1; i <= midPointNamesList.size(); i++) {
			columnKeys.add(String.valueOf(i));
		}

		columnsSemiLuxury = new ArrayList<ColumnModel>();

		int count = 0;
		for (String columnKey : columnKeys) {
			String key = VALID_COLUMN_KEYS.get(count);
			columnsSemiLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
			count = count + 1;
		}

	}

	private void createDynamicColumnsSuperLuxury(List<String> midPointNamesList) {

		List<String> columnKeys = new ArrayList<String>();
		VALID_COLUMN_KEYS = new ArrayList<String>();

		for (int i = 0; i < midPointNamesList.size(); i++) {
			VALID_COLUMN_KEYS.add(midPointNamesList.get(i));
		}

		for (int i = 1; i <= midPointNamesList.size(); i++) {
			columnKeys.add(String.valueOf(i));
		}

		columnsSuperLuxury = new ArrayList<ColumnModel>();

		int count = 0;
		for (String columnKey : columnKeys) {
			String key = VALID_COLUMN_KEYS.get(count);
			columnsSuperLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
			count = count + 1;
		}

	}

	public void clearBtnAction() {
		loadData();
		RequestContext.getCurrentInstance().update("frmcombineTimeTable");
	}

	public void saveEditsButtonAction() {

		boolean success = combineTimeTableGenerateService.updateNt_t_midpoint_timeTable(routeOnEdit, editMidpointDTO,
				selectedMidPoint.getCategory(), tripType, selectedGroup, sessionBackingBean.getLoginUser());

		if (!success) {
			sessionBackingBean.setMessage("Please check entered time is correct");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			return;
		} else {
			sessionBackingBean.setMessage("Data updated successfully");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");

			searchOnSelectSimilarRoutes();

			RequestContext.getCurrentInstance().update("frmcombineTimeTable");
			RequestContext.getCurrentInstance().execute("PF('dlgEditMidpoints').hide()");
		}
	}

	public String getSelectedRouteNo() {
		return selectedRouteNo;
	}

	public void setSelectedRouteNo(String selectedRouteNo) {
		this.selectedRouteNo = selectedRouteNo;
	}

	public void searchOnSelectSimilarRoutes() {

		List<String> midPointNamesListNormal = new ArrayList<String>();
		List<String> midPointNamesListExpress = new ArrayList<String>();
		List<String> midPointNamesListLuxury = new ArrayList<String>();
		List<String> midPointNamesListSemiLuxury = new ArrayList<String>();
		List<String> midPointNamesListSuperLuxury = new ArrayList<String>();

		List<String> midPointNamesListNormalDtoO = new ArrayList<String>();
		List<String> midPointNamesListExpressDtoO = new ArrayList<String>();
		List<String> midPointNamesListLuxuryDtoO = new ArrayList<String>();
		List<String> midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
		List<String> midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();

		int count = 0;
		for (String route : selectedRoutes) {

			/** TABLE 01 START **/
			if (count == 0) {
				similarRouteOne = route;
				similarRouteOneRender = true;
				if (normal) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"001", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						// select from DB table and show
						midPointTblOneNormal = combineTimeTableGenerateService.selectTimeTableData(route, "001", "O",
								selectedGroup);
						if (midPointTblOneNormal.size() != 0 && midPointTblOneNormal.get(0).getCategory() != null
								&& !midPointTblOneNormal.get(0).getCategory().isEmpty()
								&& !midPointTblOneNormal.get(0).getCategory().equals("")) {
							midPointNamesListNormal = new ArrayList<String>();
							midPointNamesListNormal = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "001");
						} else {
							midPointTblOneNormal = new ArrayList<MidPointTimesDTO>();
						}

					} else {
						// calculate, save in DB and show
						midPointTblOneNormal = BusTypeProcess("001", route);
						if (midPointTblOneNormal.size() != 0 && midPointTblOneNormal.get(0).getCategory() != null
								&& !midPointTblOneNormal.get(0).getCategory().isEmpty()
								&& !midPointTblOneNormal.get(0).getCategory().equals("")) {
							midPointNamesListNormal = new ArrayList<String>();
							midPointNamesListNormal = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "001");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblOneNormal, route, "001", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblOneNormal = combineTimeTableGenerateService.selectTimeTableData(route, "001",
									"O", selectedGroup);
						} else {
							midPointTblOneNormal = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "001", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						// select from DB table and show
						midPointTblOneNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "001",
								"D", selectedGroup);
						if (midPointTblOneNormalDtoO.size() != 0
								&& midPointTblOneNormalDtoO.get(0).getCategory() != null
								&& !midPointTblOneNormalDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblOneNormalDtoO.get(0).getCategory().equals("")) {
							midPointNamesListNormalDtoO = new ArrayList<String>();
							midPointNamesListNormalDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "001");
						} else {
							midPointTblOneNormalDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						// calculate, save in DB and show
						midPointTblOneNormalDtoO = BusTypeProcessDtoO("001", route);
						if (midPointTblOneNormalDtoO.size() != 0
								&& midPointTblOneNormalDtoO.get(0).getCategory() != null
								&& !midPointTblOneNormalDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblOneNormalDtoO.get(0).getCategory().equals("")) {
							midPointNamesListNormalDtoO = new ArrayList<String>();
							midPointNamesListNormalDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "001");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblOneNormalDtoO, route, "001", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblOneNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "001",
									"D", selectedGroup);
						} else {
							midPointTblOneNormalDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (express) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"EB", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						// select from DB table and show
						midPointTblOneExpress = combineTimeTableGenerateService.selectTimeTableData(route, "EB", "O",
								selectedGroup);
						if (midPointTblOneExpress.size() != 0 && midPointTblOneExpress.get(0).getCategory() != null
								&& !midPointTblOneExpress.get(0).getCategory().isEmpty()
								&& !midPointTblOneExpress.get(0).getCategory().equals("")) {

							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
						} else {
							midPointTblOneExpress = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblOneExpress = BusTypeProcess("EB", route);
						if (midPointTblOneExpress.size() != 0 && midPointTblOneExpress.get(0).getCategory() != null
								&& !midPointTblOneExpress.get(0).getCategory().isEmpty()
								&& !midPointTblOneExpress.get(0).getCategory().equals("")) {
							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblOneExpress, route, "EB", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblOneExpress = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
									"O", selectedGroup);
						} else {
							midPointTblOneExpress = new ArrayList<MidPointTimesDTO>();
						}
					} // O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "EB", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						// select from DB table and show
						midPointTblOneExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
								"D", selectedGroup);
						if (midPointTblOneExpressDtoO.size() != 0
								&& midPointTblOneExpressDtoO.get(0).getCategory() != null
								&& !midPointTblOneExpressDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblOneExpressDtoO.get(0).getCategory().equals("")) {

							midPointNamesListExpressDtoO = new ArrayList<String>();
							midPointNamesListExpressDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "EB");

						} else {
							midPointTblOneExpressDtoO = new ArrayList<MidPointTimesDTO>();
						}

					} else {
						// calculate, save in DB and show
						midPointTblOneExpressDtoO = BusTypeProcessDtoO("EB", route);
						if (midPointTblOneExpressDtoO.size() != 0
								&& midPointTblOneExpressDtoO.get(0).getCategory() != null
								&& !midPointTblOneExpressDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblOneExpressDtoO.get(0).getCategory().equals("")) {

							midPointNamesListExpressDtoO = new ArrayList<String>();
							midPointNamesListExpressDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "EB");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblOneExpressDtoO, route, "EB", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblOneExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
									"D", selectedGroup);
						} else {
							midPointTblOneExpressDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (luxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"002", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						// select from DB table and show
						midPointTblOneLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "002", "O",
								selectedGroup);

						if (midPointTblOneLuxury.size() != 0 && midPointTblOneLuxury.get(0).getCategory() != null
								&& !midPointTblOneLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblOneLuxury.get(0).getCategory().equals("")) {
							midPointNamesListLuxury = new ArrayList<String>();
							midPointNamesListLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "002");
						} else {
							midPointTblOneLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						// calculate, save in DB and show
						midPointTblOneLuxury = BusTypeProcess("002", route);
						if (midPointTblOneLuxury.size() != 0 && midPointTblOneLuxury.get(0).getCategory() != null
								&& !midPointTblOneLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblOneLuxury.get(0).getCategory().equals("")) {

							midPointNamesListLuxury = new ArrayList<String>();
							midPointNamesListLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "002");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblOneLuxury, route, "002", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblOneLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "002",
									"O", selectedGroup);
						} else {
							midPointTblOneLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "002", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						// select from DB table and show
						midPointTblOneLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "002",
								"D", selectedGroup);
						if (midPointTblOneLuxuryDtoO.size() != 0
								&& midPointTblOneLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblOneLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblOneLuxuryDtoO.get(0).getCategory().equals("")) {

							midPointNamesListLuxuryDtoO = new ArrayList<String>();
							midPointNamesListLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "002");
						} else {
							midPointTblOneLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						// calculate, save in DB and show
						midPointTblOneLuxuryDtoO = BusTypeProcessDtoO("002", route);
						if (midPointTblOneLuxuryDtoO.size() != 0
								&& midPointTblOneLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblOneLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblOneLuxuryDtoO.get(0).getCategory().equals("")) {

							midPointNamesListLuxuryDtoO = new ArrayList<String>();
							midPointNamesListLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "002");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblOneLuxuryDtoO, route, "002", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblOneLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "002",
									"D", selectedGroup);
						} else {
							midPointTblOneLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (semiLuxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"004", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						// select from DB table and show
						midPointTblOneSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "004",
								"O", selectedGroup);
						if (midPointTblOneSemiLuxury.size() != 0
								&& midPointTblOneSemiLuxury.get(0).getCategory() != null
								&& !midPointTblOneSemiLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblOneSemiLuxury.get(0).getCategory().equals("")) {

							midPointNamesListSemiLuxury = new ArrayList<String>();
							midPointNamesListSemiLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "004");
						} else {
							midPointTblOneSemiLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						// calculate, save in DB and show
						midPointTblOneSemiLuxury = BusTypeProcess("004", route);
						if (midPointTblOneSemiLuxury.size() != 0
								&& midPointTblOneSemiLuxury.get(0).getCategory() != null
								&& !midPointTblOneSemiLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblOneSemiLuxury.get(0).getCategory().equals("")) {

							midPointNamesListSemiLuxury = new ArrayList<String>();
							midPointNamesListSemiLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "004");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblOneSemiLuxury, route, "004", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblOneSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "004",
									"O", selectedGroup);
						} else {
							midPointTblOneSemiLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "004", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						// select from DB table and show
						midPointTblOneSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "004",
								"D", selectedGroup);
						if (midPointTblOneSemiLuxuryDtoO.size() != 0
								&& midPointTblOneSemiLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblOneSemiLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblOneSemiLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSemiLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "004");
						} else {
							midPointTblOneSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						// calculate, save in DB and show
						midPointTblOneSemiLuxuryDtoO = BusTypeProcessDtoO("004", route);
						if (midPointTblOneSemiLuxuryDtoO.size() != 0
								&& midPointTblOneSemiLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblOneSemiLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblOneSemiLuxuryDtoO.get(0).getCategory().equals("")) {

							midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSemiLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "004");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblOneSemiLuxuryDtoO, route, "004", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblOneSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"004", "D", selectedGroup);
						} else {
							midPointTblOneSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (superLuxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"003", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblOneSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "003",
								"O", selectedGroup);
						if (midPointTblOneSuperLuxury.size() != 0
								&& midPointTblOneSuperLuxury.get(0).getCategory() != null
								&& !midPointTblOneSuperLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblOneSuperLuxury.get(0).getCategory().equals("")) {

							midPointNamesListSuperLuxury = new ArrayList<String>();
							midPointNamesListSuperLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "003");
						} else {
							midPointTblOneSuperLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblOneSuperLuxury = BusTypeProcess("003", route);
						if (midPointTblOneSuperLuxury.size() != 0
								&& midPointTblOneSuperLuxury.get(0).getCategory() != null
								&& !midPointTblOneSuperLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblOneSuperLuxury.get(0).getCategory().equals("")) {

							midPointNamesListSuperLuxury = new ArrayList<String>();
							midPointNamesListSuperLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "003");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblOneSuperLuxury, route, "003", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblOneSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(route,
									"003", "O", selectedGroup);
						} else {
							midPointTblOneSuperLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "003", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						// select from DB table and show
						midPointTblOneSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
								"003", "D", selectedGroup);
						if (midPointTblOneSuperLuxuryDtoO.size() != 0
								&& midPointTblOneSuperLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblOneSuperLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblOneSuperLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSuperLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "003");
						} else {
							midPointTblOneSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblOneSuperLuxuryDtoO = BusTypeProcessDtoO("003", route);
						if (midPointTblOneSuperLuxuryDtoO.size() != 0
								&& midPointTblOneSuperLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblOneSuperLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblOneSuperLuxuryDtoO.get(0).getCategory().equals("")) {

							midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSuperLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "003");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblOneSuperLuxuryDtoO, route, "003", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblOneSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"003", "D", selectedGroup);
						} else {
							midPointTblOneSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} // D to O end
				}

				createDynamicColumnsTableOne(midPointNamesListNormal, midPointNamesListExpress, midPointNamesListLuxury,
						midPointNamesListSemiLuxury, midPointNamesListSuperLuxury);
				createDynamicColumnsTableOneDtoO(midPointNamesListNormalDtoO, midPointNamesListExpressDtoO,
						midPointNamesListLuxuryDtoO, midPointNamesListSemiLuxuryDtoO, midPointNamesListSuperLuxuryDtoO);
			}
			/** TABLE 01 END **/

			/** TABLE 02 START **/
			if (count == 1) {
				similarRouteTwo = route;
				similarRouteTwoRender = true;
				if (normal) {

					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"001", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblTwoNormal = combineTimeTableGenerateService.selectTimeTableData(route, "001", "O",
								selectedGroup);
						if (midPointTblTwoNormal.size() != 0 && midPointTblTwoNormal.get(0).getCategory() != null
								&& !midPointTblTwoNormal.get(0).getCategory().isEmpty()
								&& !midPointTblTwoNormal.get(0).getCategory().equals("")) {
							midPointNamesListNormal = new ArrayList<String>();
							midPointNamesListNormal = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "001");
						} else {
							midPointTblTwoNormal = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblTwoNormal = BusTypeProcess("001", route);
						if (midPointTblTwoNormal.size() != 0 && midPointTblTwoNormal.get(0).getCategory() != null
								&& !midPointTblTwoNormal.get(0).getCategory().isEmpty()
								&& !midPointTblTwoNormal.get(0).getCategory().equals("")) {
							midPointNamesListNormal = new ArrayList<String>();
							midPointNamesListNormal = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "001");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblTwoNormal, route, "001", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblTwoNormal = combineTimeTableGenerateService.selectTimeTableData(route, "001",
									"O", selectedGroup);
						} else {
							midPointTblTwoNormal = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "001", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblTwoNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "001",
								"D", selectedGroup);
						if (midPointTblTwoNormalDtoO.size() != 0
								&& midPointTblTwoNormalDtoO.get(0).getCategory() != null
								&& !midPointTblTwoNormalDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblTwoNormalDtoO.get(0).getCategory().equals("")) {
							midPointNamesListNormalDtoO = new ArrayList<String>();
							midPointNamesListNormalDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "001");
						} else {
							midPointTblTwoNormalDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblTwoNormalDtoO = BusTypeProcessDtoO("001", route);
						if (midPointTblTwoNormalDtoO.size() != 0
								&& midPointTblTwoNormalDtoO.get(0).getCategory() != null
								&& !midPointTblTwoNormalDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblTwoNormalDtoO.get(0).getCategory().equals("")) {
							midPointNamesListNormalDtoO = new ArrayList<String>();
							midPointNamesListNormalDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "001");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblTwoNormalDtoO, route, "001", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblTwoNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "001",
									"D", selectedGroup);
						} else {
							midPointTblTwoNormalDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (express) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"EB", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblTwoExpress = combineTimeTableGenerateService.selectTimeTableData(route, "EB", "O",
								selectedGroup);
						if (midPointTblTwoExpress.size() != 0 && midPointTblTwoExpress.get(0).getCategory() != null
								&& !midPointTblTwoExpress.get(0).getCategory().isEmpty()
								&& !midPointTblTwoExpress.get(0).getCategory().equals("")) {
							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
						} else {
							midPointTblTwoExpress = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblTwoExpress = BusTypeProcess("EB", route);
						if (midPointTblTwoExpress.size() != 0 && midPointTblTwoExpress.get(0).getCategory() != null
								&& !midPointTblTwoExpress.get(0).getCategory().isEmpty()
								&& !midPointTblTwoExpress.get(0).getCategory().equals("")) {
							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblTwoExpress, route, "EB", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblTwoExpress = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
									"O", selectedGroup);
						} else {
							midPointTblTwoExpress = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "EB", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblTwoExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
								"D", selectedGroup);
						if (midPointTblTwoExpressDtoO.size() != 0
								&& midPointTblTwoExpressDtoO.get(0).getCategory() != null
								&& !midPointTblTwoExpressDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblTwoExpressDtoO.get(0).getCategory().equals("")) {
							midPointNamesListExpressDtoO = new ArrayList<String>();
							midPointNamesListExpressDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "EB");
						} else {
							midPointTblTwoExpressDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblTwoExpressDtoO = BusTypeProcessDtoO("EB", route);
						if (midPointTblTwoExpressDtoO.size() != 0
								&& midPointTblTwoExpressDtoO.get(0).getCategory() != null
								&& !midPointTblTwoExpressDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblTwoExpressDtoO.get(0).getCategory().equals("")) {
							midPointNamesListExpressDtoO = new ArrayList<String>();
							midPointNamesListExpressDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "EB");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblTwoExpressDtoO, route, "EB", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblTwoExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
									"D", selectedGroup);
						} else {
							midPointTblTwoExpressDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (luxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"002", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblTwoLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "002", "O",
								selectedGroup);
						if (midPointTblTwoLuxury.size() != 0 && midPointTblTwoLuxury.get(0).getCategory() != null
								&& !midPointTblTwoLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblTwoLuxury.get(0).getCategory().equals("")) {
							midPointNamesListLuxury = new ArrayList<String>();
							midPointNamesListLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "002");
						} else {
							midPointTblTwoLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblTwoLuxury = BusTypeProcess("002", route);
						if (midPointTblTwoLuxury.size() != 0 && midPointTblTwoLuxury.get(0).getCategory() != null
								&& !midPointTblTwoLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblTwoLuxury.get(0).getCategory().equals("")) {
							midPointNamesListLuxury = new ArrayList<String>();
							midPointNamesListLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "002");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblTwoLuxury, route, "002", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblTwoLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "002",
									"O", selectedGroup);
						} else {
							midPointTblTwoLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "002", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblTwoLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "002",
								"D", selectedGroup);
						if (midPointTblTwoLuxuryDtoO.size() != 0
								&& midPointTblTwoLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblTwoLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblTwoLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListLuxuryDtoO = new ArrayList<String>();
							midPointNamesListLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "002");
						} else {
							midPointTblTwoLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblTwoLuxuryDtoO = BusTypeProcessDtoO("002", route);
						if (midPointTblTwoLuxuryDtoO.size() != 0
								&& midPointTblTwoLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblTwoLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblTwoLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListLuxuryDtoO = new ArrayList<String>();
							midPointNamesListLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "002");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblTwoLuxuryDtoO, route, "002", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblTwoLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "002",
									"D", selectedGroup);
						} else {
							midPointTblTwoLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (semiLuxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"004", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblTwoSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "004",
								"O", selectedGroup);
						if (midPointTblTwoSemiLuxury.size() != 0
								&& midPointTblTwoSemiLuxury.get(0).getCategory() != null
								&& !midPointTblTwoSemiLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblTwoSemiLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxury = new ArrayList<String>();
							midPointNamesListSemiLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "004");
						} else {
							midPointTblTwoSemiLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblTwoSemiLuxury = BusTypeProcess("004", route);
						if (midPointTblTwoSemiLuxury.size() != 0
								&& midPointTblTwoSemiLuxury.get(0).getCategory() != null
								&& !midPointTblTwoSemiLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblTwoSemiLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxury = new ArrayList<String>();
							midPointNamesListSemiLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "004");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblTwoSemiLuxury, route, "004", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblTwoSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "004",
									"O", selectedGroup);
						} else {
							midPointTblTwoSemiLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "004", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblTwoSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "004",
								"D", selectedGroup);
						if (midPointTblTwoSemiLuxuryDtoO.size() != 0
								&& midPointTblTwoSemiLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblTwoSemiLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblTwoSemiLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSemiLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "004");
						} else {
							midPointTblTwoSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblTwoSemiLuxuryDtoO = BusTypeProcessDtoO("004", route);
						if (midPointTblTwoSemiLuxuryDtoO.size() != 0
								&& midPointTblTwoSemiLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblTwoSemiLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblTwoSemiLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSemiLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "004");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblTwoSemiLuxuryDtoO, route, "004", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblTwoSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"004", "D", selectedGroup);
						} else {
							midPointTblTwoSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (superLuxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"003", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblTwoSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "003",
								"O", selectedGroup);
						if (midPointTblTwoSuperLuxury.size() != 0
								&& midPointTblTwoSuperLuxury.get(0).getCategory() != null
								&& !midPointTblTwoSuperLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblTwoSuperLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxury = new ArrayList<String>();
							midPointNamesListSuperLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "003");
						} else {
							midPointTblTwoSuperLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblTwoSuperLuxury = BusTypeProcess("003", route);
						if (midPointTblTwoSuperLuxury.size() != 0
								&& midPointTblTwoSuperLuxury.get(0).getCategory() != null
								&& !midPointTblTwoSuperLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblTwoSuperLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxury = new ArrayList<String>();
							midPointNamesListSuperLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "003");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblTwoSuperLuxury, route, "003", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblTwoSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(route,
									"003", "O", selectedGroup);
						} else {
							midPointTblTwoSuperLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} // O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "003", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblTwoSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
								"003", "D", selectedGroup);
						if (midPointTblTwoSuperLuxuryDtoO.size() != 0
								&& midPointTblTwoSuperLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblTwoSuperLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblTwoSuperLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSuperLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "003");
						} else {
							midPointTblTwoSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblTwoSuperLuxuryDtoO = BusTypeProcessDtoO("003", route);
						if (midPointTblTwoSuperLuxuryDtoO.size() != 0
								&& midPointTblTwoSuperLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblTwoSuperLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblTwoSuperLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSuperLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "003");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblTwoSuperLuxuryDtoO, route, "003", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblTwoSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"003", "D", selectedGroup);
						} else {
							midPointTblTwoSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				createDynamicColumnsTableTwo(midPointNamesListNormal, midPointNamesListExpress, midPointNamesListLuxury,
						midPointNamesListSemiLuxury, midPointNamesListSuperLuxury);
				createDynamicColumnsTableTwoDtoO(midPointNamesListNormalDtoO, midPointNamesListExpressDtoO,
						midPointNamesListLuxuryDtoO, midPointNamesListSemiLuxuryDtoO, midPointNamesListSuperLuxuryDtoO);
			}
			/** TABLE 02 END **/

			/** TABLE 03 START **/
			if (count == 2) {
				similarRouteThree = route;
				similarRouteThreeRender = true;
				if (normal) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"001", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblThreeNormal = combineTimeTableGenerateService.selectTimeTableData(route, "001", "O",
								selectedGroup);
						if (midPointTblThreeNormal.size() != 0 && midPointTblThreeNormal.get(0).getCategory() != null
								&& !midPointTblThreeNormal.get(0).getCategory().isEmpty()
								&& !midPointTblThreeNormal.get(0).getCategory().equals("")) {
							midPointNamesListNormal = new ArrayList<String>();
							midPointNamesListNormal = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "001");
						} else {
							midPointTblThreeNormal = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblThreeNormal = BusTypeProcess("001", route);
						if (midPointTblThreeNormal.size() != 0 && midPointTblThreeNormal.get(0).getCategory() != null
								&& !midPointTblThreeNormal.get(0).getCategory().isEmpty()
								&& !midPointTblThreeNormal.get(0).getCategory().equals("")) {
							midPointNamesListNormal = new ArrayList<String>();
							midPointNamesListNormal = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "001");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblThreeNormal, route, "001", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblThreeNormal = combineTimeTableGenerateService.selectTimeTableData(route, "001",
									"O", selectedGroup);
						} else {
							midPointTblThreeNormal = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "001", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblThreeNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "001",
								"D", selectedGroup);
						if (midPointTblThreeNormalDtoO.size() != 0
								&& midPointTblThreeNormalDtoO.get(0).getCategory() != null
								&& !midPointTblThreeNormalDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblThreeNormalDtoO.get(0).getCategory().equals("")) {
							midPointNamesListNormalDtoO = new ArrayList<String>();
							midPointNamesListNormalDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "001");
						} else {
							midPointTblThreeNormalDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblThreeNormalDtoO = BusTypeProcessDtoO("001", route);
						if (midPointTblThreeNormalDtoO.size() != 0
								&& midPointTblThreeNormalDtoO.get(0).getCategory() != null
								&& !midPointTblThreeNormalDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblThreeNormalDtoO.get(0).getCategory().equals("")) {
							midPointNamesListNormalDtoO = new ArrayList<String>();
							midPointNamesListNormalDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "001");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblThreeNormalDtoO, route, "001", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblThreeNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"001", "D", selectedGroup);
						} else {
							midPointTblThreeNormalDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O start
				}

				if (express) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"EB", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblThreeExpress = combineTimeTableGenerateService.selectTimeTableData(route, "EB", "O",
								selectedGroup);
						if (midPointTblThreeExpress.size() != 0 && midPointTblThreeExpress.get(0).getCategory() != null
								&& !midPointTblThreeExpress.get(0).getCategory().isEmpty()
								&& !midPointTblThreeExpress.get(0).getCategory().equals("")) {
							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
						} else {
							midPointTblThreeExpress = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblThreeExpress = BusTypeProcess("EB", route);
						if (midPointTblThreeExpress.size() != 0 && midPointTblThreeExpress.get(0).getCategory() != null
								&& !midPointTblThreeExpress.get(0).getCategory().isEmpty()
								&& !midPointTblThreeExpress.get(0).getCategory().equals("")) {
							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblThreeExpress, route, "EB", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblThreeExpress = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
									"O", selectedGroup);
						} else {
							midPointTblThreeExpress = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "EB", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblThreeExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
								"D", selectedGroup);
						if (midPointTblThreeExpressDtoO.size() != 0
								&& midPointTblThreeExpressDtoO.get(0).getCategory() != null
								&& !midPointTblThreeExpressDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblThreeExpressDtoO.get(0).getCategory().equals("")) {
							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
						} else {
							midPointTblThreeExpressDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblThreeExpressDtoO = BusTypeProcessDtoO("EB", route);
						if (midPointTblThreeExpressDtoO.size() != 0
								&& midPointTblThreeExpressDtoO.get(0).getCategory() != null
								&& !midPointTblThreeExpressDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblThreeExpressDtoO.get(0).getCategory().equals("")) {
							midPointNamesListExpressDtoO = new ArrayList<String>();
							midPointNamesListExpressDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "EB");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblThreeExpressDtoO, route, "EB", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblThreeExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"EB", "D", selectedGroup);
						} else {
							midPointTblThreeExpressDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O start
				}

				if (luxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"002", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblThreeLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "002", "O",
								selectedGroup);
						if (midPointTblThreeLuxury.size() != 0 && midPointTblThreeLuxury.get(0).getCategory() != null
								&& !midPointTblThreeLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblThreeLuxury.get(0).getCategory().equals("")) {
							midPointNamesListLuxury = new ArrayList<String>();
							midPointNamesListLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "002");
						} else {
							midPointTblThreeLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblThreeLuxury = BusTypeProcess("002", route);
						if (midPointTblThreeLuxury.size() != 0 && midPointTblThreeLuxury.get(0).getCategory() != null
								&& !midPointTblThreeLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblThreeLuxury.get(0).getCategory().equals("")) {
							midPointNamesListLuxury = new ArrayList<String>();
							midPointNamesListLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "002");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblThreeLuxury, route, "002", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblThreeLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "002",
									"O", selectedGroup);
						} else {
							midPointTblThreeLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "002", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblThreeLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "002",
								"D", selectedGroup);
						if (midPointTblThreeLuxuryDtoO.size() != 0
								&& midPointTblThreeLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblThreeLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblThreeLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListLuxuryDtoO = new ArrayList<String>();
							midPointNamesListLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "002");
						} else {
							midPointTblThreeLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblThreeLuxuryDtoO = BusTypeProcessDtoO("002", route);
						if (midPointTblThreeLuxuryDtoO.size() != 0
								&& midPointTblThreeLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblThreeLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblThreeLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListLuxuryDtoO = new ArrayList<String>();
							midPointNamesListLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "002");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblThreeLuxuryDtoO, route, "002", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblThreeLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"002", "D", selectedGroup);
						} else {
							midPointTblThreeLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O start
				}

				if (semiLuxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"004", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblThreeSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "004",
								"O", selectedGroup);
						if (midPointTblThreeSemiLuxury.size() != 0
								&& midPointTblThreeSemiLuxury.get(0).getCategory() != null
								&& !midPointTblThreeSemiLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblThreeSemiLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxury = new ArrayList<String>();
							midPointNamesListSemiLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "004");
						} else {
							midPointTblThreeSemiLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblThreeSemiLuxury = BusTypeProcess("004", route);
						if (midPointTblThreeSemiLuxury.size() != 0
								&& midPointTblThreeSemiLuxury.get(0).getCategory() != null
								&& !midPointTblThreeSemiLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblThreeSemiLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxury = new ArrayList<String>();
							midPointNamesListSemiLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "004");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblThreeSemiLuxury, route, "004", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblThreeSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(route,
									"004", "O", selectedGroup);
						} else {
							midPointTblThreeSemiLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "004", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblThreeSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
								"004", "D", selectedGroup);
						if (midPointTblThreeSemiLuxuryDtoO.size() != 0
								&& midPointTblThreeSemiLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblThreeSemiLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblThreeSemiLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSemiLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "004");
						} else {
							midPointTblThreeSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblThreeSemiLuxuryDtoO = BusTypeProcessDtoO("004", route);
						if (midPointTblThreeSemiLuxuryDtoO.size() != 0
								&& midPointTblThreeSemiLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblThreeSemiLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblThreeSemiLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSemiLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "004");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblThreeSemiLuxuryDtoO, route, "004", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblThreeSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"004", "D", selectedGroup);
						} else {
							midPointTblThreeSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O start
				}

				if (superLuxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"003", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblThreeSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "003",
								"O", selectedGroup);
						if (midPointTblThreeSuperLuxury.size() != 0
								&& midPointTblThreeSuperLuxury.get(0).getCategory() != null
								&& !midPointTblThreeSuperLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblThreeSuperLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxury = new ArrayList<String>();
							midPointNamesListSuperLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "003");
						} else {
							midPointTblThreeSuperLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblThreeSuperLuxury = BusTypeProcess("003", route);
						if (midPointTblThreeSuperLuxury.size() != 0
								&& midPointTblThreeSuperLuxury.get(0).getCategory() != null
								&& !midPointTblThreeSuperLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblThreeSuperLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxury = new ArrayList<String>();
							midPointNamesListSuperLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "003");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblThreeSuperLuxury, route, "003", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblThreeSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(route,
									"003", "O", selectedGroup);
						} else {
							midPointTblThreeSuperLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "003", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblThreeSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
								"003", "D", selectedGroup);
						if (midPointTblThreeSuperLuxuryDtoO.size() != 0
								&& midPointTblThreeSuperLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblThreeSuperLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblThreeSuperLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSuperLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "003");
						} else {
							midPointTblThreeSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblThreeSuperLuxuryDtoO = BusTypeProcessDtoO("003", route);
						if (midPointTblThreeSuperLuxuryDtoO.size() != 0
								&& midPointTblThreeSuperLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblThreeSuperLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblThreeSuperLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSuperLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "003");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblThreeSuperLuxuryDtoO, route, "003", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblThreeSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"003", "D", selectedGroup);
						} else {
							midPointTblThreeSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O start
				}

				createDynamicColumnsTableThree(midPointNamesListNormal, midPointNamesListExpress,
						midPointNamesListLuxury, midPointNamesListSemiLuxury, midPointNamesListSuperLuxury);
				createDynamicColumnsTableThreeDtoO(midPointNamesListNormalDtoO, midPointNamesListExpressDtoO,
						midPointNamesListLuxuryDtoO, midPointNamesListSemiLuxuryDtoO, midPointNamesListSuperLuxuryDtoO);
			}
			/** TABLE 03 END **/

			/** TABLE 034 START **/
			if (count == 3) {
				similarRouteFour = route;
				similarRouteFourRender = true;
				if (normal) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"001", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblFourNormal = combineTimeTableGenerateService.selectTimeTableData(route, "001", "O",
								selectedGroup);
						if (midPointTblFourNormal.size() != 0 && midPointTblFourNormal.get(0).getCategory() != null
								&& !midPointTblFourNormal.get(0).getCategory().isEmpty()
								&& !midPointTblFourNormal.get(0).getCategory().equals("")) {
							midPointNamesListNormal = new ArrayList<String>();
							midPointNamesListNormal = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "001");
						} else {
							midPointTblFourNormal = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFourNormal = BusTypeProcess("001", route);
						if (midPointTblFourNormal.size() != 0 && midPointTblFourNormal.get(0).getCategory() != null
								&& !midPointTblFourNormal.get(0).getCategory().isEmpty()
								&& !midPointTblFourNormal.get(0).getCategory().equals("")) {
							midPointNamesListNormal = new ArrayList<String>();
							midPointNamesListNormal = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "001");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFourNormal, route, "001", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFourNormal = combineTimeTableGenerateService.selectTimeTableData(route, "001",
									"O", selectedGroup);
						} else {
							midPointTblFourNormal = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "001", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblFourNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "001",
								"D", selectedGroup);
						if (midPointTblFourNormalDtoO.size() != 0
								&& midPointTblFourNormalDtoO.get(0).getCategory() != null
								&& !midPointTblFourNormalDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFourNormalDtoO.get(0).getCategory().equals("")) {
							midPointNamesListNormalDtoO = new ArrayList<String>();
							midPointNamesListNormalDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "001");
						} else {
							midPointTblFourNormalDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFourNormalDtoO = BusTypeProcessDtoO("001", route);
						if (midPointTblFourNormalDtoO.size() != 0
								&& midPointTblFourNormalDtoO.get(0).getCategory() != null
								&& !midPointTblFourNormalDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFourNormalDtoO.get(0).getCategory().equals("")) {
							midPointNamesListNormalDtoO = new ArrayList<String>();
							midPointNamesListNormalDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "001");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFourNormalDtoO, route, "001", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFourNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"001", "D", selectedGroup);
						} else {
							midPointTblFourNormalDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (express) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"EB", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblFourExpress = combineTimeTableGenerateService.selectTimeTableData(route, "EB", "O",
								selectedGroup);
						if (midPointTblFourExpress.size() != 0 && midPointTblFourExpress.get(0).getCategory() != null
								&& !midPointTblFourExpress.get(0).getCategory().isEmpty()
								&& !midPointTblFourExpress.get(0).getCategory().equals("")) {
							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
						} else {
							midPointTblFourExpress = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFourExpress = BusTypeProcess("EB", route);
						if (midPointTblFourExpress.size() != 0 && midPointTblFourExpress.get(0).getCategory() != null
								&& !midPointTblFourExpress.get(0).getCategory().isEmpty()
								&& !midPointTblFourExpress.get(0).getCategory().equals("")) {
							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFourExpress, route, "EB", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFourExpress = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
									"O", selectedGroup);
						} else {
							midPointTblFourExpress = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "EB", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblFourExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
								"D", selectedGroup);
						if (midPointTblFourExpressDtoO.size() != 0
								&& midPointTblFourExpressDtoO.get(0).getCategory() != null
								&& !midPointTblFourExpressDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFourExpressDtoO.get(0).getCategory().equals("")) {
							midPointNamesListExpressDtoO = new ArrayList<String>();
							midPointNamesListExpressDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "EB");
						} else {
							midPointTblFourExpressDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFourExpressDtoO = BusTypeProcessDtoO("EB", route);
						if (midPointTblFourExpressDtoO.size() != 0
								&& midPointTblFourExpressDtoO.get(0).getCategory() != null
								&& !midPointTblFourExpressDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFourExpressDtoO.get(0).getCategory().equals("")) {
							midPointNamesListExpressDtoO = new ArrayList<String>();
							midPointNamesListExpressDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "EB");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFourExpressDtoO, route, "EB", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFourExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"EB", "D", selectedGroup);
						} else {
							midPointTblFourExpressDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (luxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"002", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblFourLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "002", "O",
								selectedGroup);
						if (midPointTblFourLuxury.size() != 0 && midPointTblFourLuxury.get(0).getCategory() != null
								&& !midPointTblFourLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFourLuxury.get(0).getCategory().equals("")) {
							midPointNamesListLuxury = new ArrayList<String>();
							midPointNamesListLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "002");
						} else {
							midPointTblFourLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFourLuxury = BusTypeProcess("002", route);
						if (midPointTblFourLuxury.size() != 0 && midPointTblFourLuxury.get(0).getCategory() != null
								&& !midPointTblFourLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFourLuxury.get(0).getCategory().equals("")) {
							midPointNamesListLuxury = new ArrayList<String>();
							midPointNamesListLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "002");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFourLuxury, route, "002", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFourLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "002",
									"O", selectedGroup);
						} else {
							midPointTblFourLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "002", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblFourLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "002",
								"D", selectedGroup);
						if (midPointTblFourLuxuryDtoO.size() != 0
								&& midPointTblFourLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFourLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFourLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListLuxuryDtoO = new ArrayList<String>();
							midPointNamesListLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "002");
						} else {
							midPointTblFourLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFourLuxuryDtoO = BusTypeProcessDtoO("002", route);
						if (midPointTblFourLuxuryDtoO.size() != 0
								&& midPointTblFourLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFourLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFourLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListLuxuryDtoO = new ArrayList<String>();
							midPointNamesListLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "002");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFourLuxuryDtoO, route, "002", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFourLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"002", "D", selectedGroup);
						} else {
							midPointTblFourLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (semiLuxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"004", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblFourSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "004",
								"O", selectedGroup);
						if (midPointTblFourSemiLuxury.size() != 0
								&& midPointTblFourSemiLuxury.get(0).getCategory() != null
								&& !midPointTblFourSemiLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFourSemiLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxury = new ArrayList<String>();
							midPointNamesListSemiLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "004");
						} else {
							midPointTblFourSemiLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFourSemiLuxury = BusTypeProcess("004", route);
						if (midPointTblFourSemiLuxury.size() != 0
								&& midPointTblFourSemiLuxury.get(0).getCategory() != null
								&& !midPointTblFourSemiLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFourSemiLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxury = new ArrayList<String>();
							midPointNamesListSemiLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "004");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFourSemiLuxury, route, "004", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFourSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(route,
									"004", "O", selectedGroup);
						} else {
							midPointTblFourSemiLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "004", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblFourSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
								"004", "D", selectedGroup);
						if (midPointTblFourSemiLuxuryDtoO.size() != 0
								&& midPointTblFourSemiLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFourSemiLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFourSemiLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSemiLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "004");
						} else {
							midPointTblFourSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFourSemiLuxuryDtoO = BusTypeProcessDtoO("004", route);
						if (midPointTblFourSemiLuxuryDtoO.size() != 0
								&& midPointTblFourSemiLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFourSemiLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFourSemiLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSemiLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "004");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFourSemiLuxuryDtoO, route, "004", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFourSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"004", "D", selectedGroup);
						} else {
							midPointTblFourSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (superLuxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"003", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblFourSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "003",
								"O", selectedGroup);
						if (midPointTblFourSuperLuxury.size() != 0
								&& midPointTblFourSuperLuxury.get(0).getCategory() != null
								&& !midPointTblFourSuperLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFourSuperLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxury = new ArrayList<String>();
							midPointNamesListSuperLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "003");
						} else {
							midPointTblFourSuperLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFourSuperLuxury = BusTypeProcess("003", route);
						if (midPointTblFourSuperLuxury.size() != 0
								&& midPointTblFourSuperLuxury.get(0).getCategory() != null
								&& !midPointTblFourSuperLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFourSuperLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxury = new ArrayList<String>();
							midPointNamesListSuperLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "003");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFourSuperLuxury, route, "003", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFourSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(route,
									"003", "O", selectedGroup);
						} else {
							midPointTblFourSuperLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "003", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblFourSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
								"003", "D", selectedGroup);
						if (midPointTblFourSuperLuxuryDtoO.size() != 0
								&& midPointTblFourSuperLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFourSuperLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFourSuperLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSuperLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "003");
						} else {
							midPointTblFourSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFourSuperLuxuryDtoO = BusTypeProcessDtoO("003", route);
						if (midPointTblFourSuperLuxuryDtoO.size() != 0
								&& midPointTblFourSuperLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFourSuperLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFourSuperLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSuperLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "003");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFourSuperLuxuryDtoO, route, "003", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFourSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"003", "D", selectedGroup);
						} else {
							midPointTblFourSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				createDynamicColumnsTableFour(midPointNamesListNormal, midPointNamesListExpress,
						midPointNamesListLuxury, midPointNamesListSemiLuxury, midPointNamesListSuperLuxury);
				createDynamicColumnsTableFourDtoO(midPointNamesListNormalDtoO, midPointNamesListExpressDtoO,
						midPointNamesListLuxuryDtoO, midPointNamesListSemiLuxuryDtoO, midPointNamesListSuperLuxuryDtoO);
			}
			/** TABLE 034 END **/

			/** TABLE 05 START **/
			if (count == 4) {
				similarRouteFive = route;
				similarRouteFiveRender = true;
				if (normal) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"001", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblFiveNormal = combineTimeTableGenerateService.selectTimeTableData(route, "001", "O",
								selectedGroup);
						if (midPointTblFiveNormal.size() != 0 && midPointTblFiveNormal.get(0).getCategory() != null
								&& !midPointTblFiveNormal.get(0).getCategory().isEmpty()
								&& !midPointTblFiveNormal.get(0).getCategory().equals("")) {
							midPointNamesListNormal = new ArrayList<String>();
							midPointNamesListNormal = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "001");
						} else {
							midPointTblFiveNormal = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFiveNormal = BusTypeProcess("001", route);
						if (midPointTblFiveNormal.size() != 0 && midPointTblFiveNormal.get(0).getCategory() != null
								&& !midPointTblFiveNormal.get(0).getCategory().isEmpty()
								&& !midPointTblFiveNormal.get(0).getCategory().equals("")) {
							midPointNamesListNormal = new ArrayList<String>();
							midPointNamesListNormal = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "001");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFiveNormal, route, "001", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFiveNormal = combineTimeTableGenerateService.selectTimeTableData(route, "001",
									"O", selectedGroup);
						} else {
							midPointTblFiveNormal = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "001", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblFiveNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "001",
								"D", selectedGroup);
						if (midPointTblFiveNormalDtoO.size() != 0
								&& midPointTblFiveNormalDtoO.get(0).getCategory() != null
								&& !midPointTblFiveNormalDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFiveNormalDtoO.get(0).getCategory().equals("")) {
							midPointNamesListNormalDtoO = new ArrayList<String>();
							midPointNamesListNormalDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "001");
						} else {
							midPointTblFiveNormalDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFiveNormalDtoO = BusTypeProcessDtoO("001", route);
						if (midPointTblFiveNormalDtoO.size() != 0
								&& midPointTblFiveNormalDtoO.get(0).getCategory() != null
								&& !midPointTblFiveNormalDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFiveNormalDtoO.get(0).getCategory().equals("")) {
							midPointNamesListNormalDtoO = new ArrayList<String>();
							midPointNamesListNormalDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "001");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFiveNormalDtoO, route, "001", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFiveNormalDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"001", "D", selectedGroup);
						} else {
							midPointTblFiveNormalDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (express) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"EB", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblFiveExpress = combineTimeTableGenerateService.selectTimeTableData(route, "EB", "O",
								selectedGroup);
						if (midPointTblFiveExpress.size() != 0 && midPointTblFiveExpress.get(0).getCategory() != null
								&& !midPointTblFiveExpress.get(0).getCategory().isEmpty()
								&& !midPointTblFiveExpress.get(0).getCategory().equals("")) {
							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
						} else {
							midPointTblFiveExpress = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFiveExpress = BusTypeProcess("EB", route);
						if (midPointTblFiveExpress.size() != 0 && midPointTblFiveExpress.get(0).getCategory() != null
								&& !midPointTblFiveExpress.get(0).getCategory().isEmpty()
								&& !midPointTblFiveExpress.get(0).getCategory().equals("")) {
							midPointNamesListExpress = new ArrayList<String>();
							midPointNamesListExpress = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "EB");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFiveExpress, route, "EB", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFiveExpress = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
									"O", selectedGroup);
						} else {
							midPointTblFiveExpress = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "EB", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblFiveExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "EB",
								"D", selectedGroup);
						if (midPointTblFiveExpressDtoO.size() != 0
								&& midPointTblFiveExpressDtoO.get(0).getCategory() != null
								&& !midPointTblFiveExpressDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFiveExpressDtoO.get(0).getCategory().equals("")) {
							midPointNamesListExpressDtoO = new ArrayList<String>();
							midPointNamesListExpressDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "EB");
						} else {
							midPointTblFiveExpressDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFiveExpressDtoO = BusTypeProcessDtoO("EB", route);
						if (midPointTblFiveExpressDtoO.size() != 0
								&& midPointTblFiveExpressDtoO.get(0).getCategory() != null
								&& !midPointTblFiveExpressDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFiveExpressDtoO.get(0).getCategory().equals("")) {
							midPointNamesListExpressDtoO = new ArrayList<String>();
							midPointNamesListExpressDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "EB");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFiveExpressDtoO, route, "EB", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFiveExpressDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"EB", "D", selectedGroup);
						} else {
							midPointTblFiveExpressDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (luxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"002", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblFiveLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "002", "O",
								selectedGroup);
						if (midPointTblFiveLuxury.size() != 0 && midPointTblFiveLuxury.get(0).getCategory() != null
								&& !midPointTblFiveLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFiveLuxury.get(0).getCategory().equals("")) {
							midPointNamesListLuxury = new ArrayList<String>();
							midPointNamesListLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "002");
						} else {
							midPointTblFiveLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFiveLuxury = BusTypeProcess("002", route);
						if (midPointTblFiveLuxury.size() != 0 && midPointTblFiveLuxury.get(0).getCategory() != null
								&& !midPointTblFiveLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFiveLuxury.get(0).getCategory().equals("")) {
							midPointNamesListLuxury = new ArrayList<String>();
							midPointNamesListLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "002");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFiveLuxury, route, "002", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFiveLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "002",
									"O", selectedGroup);
						} else {
							midPointTblFiveLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "002", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblFiveLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route, "002",
								"D", selectedGroup);
						if (midPointTblFiveLuxuryDtoO.size() != 0
								&& midPointTblFiveLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFiveLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFiveLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListLuxuryDtoO = new ArrayList<String>();
							midPointNamesListLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "002");
						} else {
							midPointTblFiveLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFiveLuxuryDtoO = BusTypeProcessDtoO("002", route);
						if (midPointTblFiveLuxuryDtoO.size() != 0
								&& midPointTblFiveLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFiveLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFiveLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListLuxuryDtoO = new ArrayList<String>();
							midPointNamesListLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "002");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFiveLuxuryDtoO, route, "002", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFiveLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"002", "D", selectedGroup);
						} else {
							midPointTblFiveLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (semiLuxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"004", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblFiveSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "004",
								"O", selectedGroup);
						if (midPointTblFiveSemiLuxury.size() != 0
								&& midPointTblFiveSemiLuxury.get(0).getCategory() != null
								&& !midPointTblFiveSemiLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFiveSemiLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxury = new ArrayList<String>();
							midPointNamesListSemiLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "004");
						} else {
							midPointTblFiveSemiLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFiveSemiLuxury = BusTypeProcess("004", route);
						if (midPointTblFiveSemiLuxury.size() != 0
								&& midPointTblFiveSemiLuxury.get(0).getCategory() != null
								&& !midPointTblFiveSemiLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFiveSemiLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxury = new ArrayList<String>();
							midPointNamesListSemiLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "004");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFiveSemiLuxury, route, "004", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFiveSemiLuxury = combineTimeTableGenerateService.selectTimeTableData(route,
									"004", "O", selectedGroup);
						} else {
							midPointTblFiveSemiLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "004", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblFiveSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
								"004", "D", selectedGroup);
						if (midPointTblFiveSemiLuxuryDtoO.size() != 0
								&& midPointTblFiveSemiLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFiveSemiLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFiveSemiLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSemiLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "004");
						} else {
							midPointTblFiveSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFiveSemiLuxuryDtoO = BusTypeProcessDtoO("004", route);
						if (midPointTblFiveSemiLuxuryDtoO.size() != 0
								&& midPointTblFiveSemiLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFiveSemiLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFiveSemiLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSemiLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSemiLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "004");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFiveSemiLuxuryDtoO, route, "004", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFiveSemiLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"004", "D", selectedGroup);
						} else {
							midPointTblFiveSemiLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				if (superLuxury) {
					// O to D start
					/** check already insert to database start **/
					boolean insert = combineTimeTableGenerateService.checkDataAvailableInNt_t_midpoint_timetable(route,
							"003", "O", selectedGroup);
					/** check already insert to database end **/
					if (insert) {
						midPointTblFiveSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(route, "003",
								"O", selectedGroup);
						if (midPointTblFiveSuperLuxury.size() != 0
								&& midPointTblFiveSuperLuxury.get(0).getCategory() != null
								&& !midPointTblFiveSuperLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFiveSuperLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxury = new ArrayList<String>();
							midPointNamesListSuperLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "003");
						} else {
							midPointTblFiveSuperLuxury = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFiveSuperLuxury = BusTypeProcess("003", route);
						if (midPointTblFiveSuperLuxury.size() != 0
								&& midPointTblFiveSuperLuxury.get(0).getCategory() != null
								&& !midPointTblFiveSuperLuxury.get(0).getCategory().isEmpty()
								&& !midPointTblFiveSuperLuxury.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxury = new ArrayList<String>();
							midPointNamesListSuperLuxury = combineTimeTableGenerateService
									.retrieveMidPointNamesForRoute(route, "003");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFiveSuperLuxury, route, "003", "O", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFiveSuperLuxury = combineTimeTableGenerateService.selectTimeTableData(route,
									"003", "O", selectedGroup);
						} else {
							midPointTblFiveSuperLuxury = new ArrayList<MidPointTimesDTO>();
						}
					}
					// O to D end

					// D to O start
					/** check already insert to database start **/
					boolean insertDtoO = combineTimeTableGenerateService
							.checkDataAvailableInNt_t_midpoint_timetable(route, "003", "D", selectedGroup);
					/** check already insert to database end **/
					if (insertDtoO) {
						midPointTblFiveSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
								"003", "D", selectedGroup);
						if (midPointTblFiveSuperLuxuryDtoO.size() != 0
								&& midPointTblFiveSuperLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFiveSuperLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFiveSuperLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSuperLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "003");
						} else {
							midPointTblFiveSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					} else {
						midPointTblFiveSuperLuxuryDtoO = BusTypeProcessDtoO("003", route);
						if (midPointTblFiveSuperLuxuryDtoO.size() != 0
								&& midPointTblFiveSuperLuxuryDtoO.get(0).getCategory() != null
								&& !midPointTblFiveSuperLuxuryDtoO.get(0).getCategory().isEmpty()
								&& !midPointTblFiveSuperLuxuryDtoO.get(0).getCategory().equals("")) {
							midPointNamesListSuperLuxuryDtoO = new ArrayList<String>();
							midPointNamesListSuperLuxuryDtoO = combineTimeTableGenerateService
									.retrieveMidPointNamesForRouteDtoO(route, "003");
							combineTimeTableGenerateService.insertMidpointDataToNt_t_midpoint_timetable(
									midPointTblFiveSuperLuxuryDtoO, route, "003", "D", selectedGroup,
									sessionBackingBean.getLoginUser());
							midPointTblFiveSuperLuxuryDtoO = combineTimeTableGenerateService.selectTimeTableData(route,
									"003", "D", selectedGroup);
						} else {
							midPointTblFiveSuperLuxuryDtoO = new ArrayList<MidPointTimesDTO>();
						}
					}
					// D to O end
				}

				createDynamicColumnsTableFive(midPointNamesListNormal, midPointNamesListExpress,
						midPointNamesListLuxury, midPointNamesListSemiLuxury, midPointNamesListSuperLuxury);
				createDynamicColumnsTableFiveDtoO(midPointNamesListNormalDtoO, midPointNamesListExpressDtoO,
						midPointNamesListLuxuryDtoO, midPointNamesListSemiLuxuryDtoO, midPointNamesListSuperLuxuryDtoO);
			}
			/** TABLE 05 END **/

			count = count + 1;
		}

		RequestContext.getCurrentInstance().update("frmcombineTimeTable");
	}

	private void createDynamicColumnsTableFiveDtoO(List<String> midPointNamesListNormal,
			List<String> midPointNamesListExpress, List<String> midPointNamesListLuxury,
			List<String> midPointNamesListSemiLuxury, List<String> midPointNamesListSuperLuxury) {

		if (normal) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListNormal.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFiveNormalDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFiveNormalDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (express) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListExpress.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFiveExpressDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFiveExpressDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (luxury) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFiverLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFiverLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (semiLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSemiLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFiveSemiLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFiveSemiLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

		if (superLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSuperLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFiveSuperLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFiveSuperLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

	}

	private void createDynamicColumnsTableFourDtoO(List<String> midPointNamesListNormal,
			List<String> midPointNamesListExpress, List<String> midPointNamesListLuxury,
			List<String> midPointNamesListSemiLuxury, List<String> midPointNamesListSuperLuxury) {

		if (normal) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListNormal.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFourNormalDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFourNormalDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (express) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListExpress.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFourExpressDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFourExpressDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (luxury) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFourLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFourLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (semiLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSemiLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFourSemiLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFourSemiLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

		if (superLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSuperLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFourSuperLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFourSuperLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

	}

	private void createDynamicColumnsTableThreeDtoO(List<String> midPointNamesListNormal,
			List<String> midPointNamesListExpress, List<String> midPointNamesListLuxury,
			List<String> midPointNamesListSemiLuxury, List<String> midPointNamesListSuperLuxury) {

		if (normal) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListNormal.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblThreeNormalDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblThreeNormalDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (express) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListExpress.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblThreeExpressDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblThreeExpressDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (luxury) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblThreeLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblThreeLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (semiLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSemiLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblThreeSemiLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblThreeSemiLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

		if (superLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSuperLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblThreeSuperLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblThreeSuperLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

	}

	private void createDynamicColumnsTableTwoDtoO(List<String> midPointNamesListNormal,
			List<String> midPointNamesListExpress, List<String> midPointNamesListLuxury,
			List<String> midPointNamesListSemiLuxury, List<String> midPointNamesListSuperLuxury) {

		if (normal) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListNormal.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblTwoNormalDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblTwoNormalDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (express) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListExpress.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblTwoExpressDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblTwoExpressDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (luxury) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblTwoLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblTwoLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (semiLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSemiLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblTwoSemiLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblTwoSemiLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

		if (superLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSuperLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblTwoSuperLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblTwoSuperLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

	}

	private void createDynamicColumnsTableOneDtoO(List<String> midPointNamesListNormal,
			List<String> midPointNamesListExpress, List<String> midPointNamesListLuxury,
			List<String> midPointNamesListSemiLuxury, List<String> midPointNamesListSuperLuxury) {

		if (normal) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListNormal.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblOneNormalDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblOneNormalDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (express) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListExpress.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblOneExpressDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblOneExpressDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (luxury) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblOneLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblOneLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (semiLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSemiLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblOneSemiLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblOneSemiLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

		if (superLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSuperLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblOneSuperLuxuryDtoO = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblOneSuperLuxuryDtoO.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

	}

	private void createDynamicColumnsTableFive(List<String> midPointNamesListNormal,
			List<String> midPointNamesListExpress, List<String> midPointNamesListLuxury,
			List<String> midPointNamesListSemiLuxury, List<String> midPointNamesListSuperLuxury) {

		if (normal) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListNormal.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFiveNormal = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFiveNormal.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (express) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListExpress.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFiveExpress = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFiveExpress.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (luxury) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFiverLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFiverLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (semiLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSemiLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFiveSemiLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFiveSemiLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

		if (superLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSuperLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFiveSuperLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFiveSuperLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

	}

	private void createDynamicColumnsTableFour(List<String> midPointNamesListNormal,
			List<String> midPointNamesListExpress, List<String> midPointNamesListLuxury,
			List<String> midPointNamesListSemiLuxury, List<String> midPointNamesListSuperLuxury) {

		if (normal) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListNormal.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFourNormal = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFourNormal.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (express) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListExpress.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFourExpress = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFourExpress.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (luxury) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFourLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFourLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (semiLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSemiLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFourSemiLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFourSemiLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

		if (superLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSuperLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblFourSuperLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblFourSuperLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

	}

	private void createDynamicColumnsTableThree(List<String> midPointNamesListNormal,
			List<String> midPointNamesListExpress, List<String> midPointNamesListLuxury,
			List<String> midPointNamesListSemiLuxury, List<String> midPointNamesListSuperLuxury) {

		if (normal) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListNormal.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblThreeNormal = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblThreeNormal.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (express) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListExpress.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblThreeExpress = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblThreeExpress.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (luxury) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblThreeLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblThreeLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (semiLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSemiLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblThreeSemiLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblThreeSemiLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

		if (superLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSuperLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblThreeSuperLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblThreeSuperLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

	}

	private void createDynamicColumnsTableTwo(List<String> midPointNamesListNormal,
			List<String> midPointNamesListExpress, List<String> midPointNamesListLuxury,
			List<String> midPointNamesListSemiLuxury, List<String> midPointNamesListSuperLuxury) {

		if (normal) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListNormal.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblTwoNormal = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblTwoNormal.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (express) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListExpress.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblTwoExpress = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblTwoExpress.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (luxury) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblTwoLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblTwoLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (semiLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSemiLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblTwoSemiLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblTwoSemiLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

		if (superLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSuperLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblTwoSuperLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblTwoSuperLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

	}

	private void createDynamicColumnsTableOne(List<String> midPointNamesListNormal,
			List<String> midPointNamesListExpress, List<String> midPointNamesListLuxury,
			List<String> midPointNamesListSemiLuxury, List<String> midPointNamesListSuperLuxury) {

		if (normal) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListNormal.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListNormal.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblOneNormal = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblOneNormal.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (express) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListExpress.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListExpress.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblOneExpress = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblOneExpress.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (luxury) {
			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblOneLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblOneLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}
		}

		if (semiLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSemiLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSemiLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblOneSemiLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblOneSemiLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

		if (superLuxury) {

			List<String> columnKeys = new ArrayList<String>();
			VALID_COLUMN_KEYS = new ArrayList<String>();

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				VALID_COLUMN_KEYS.add(midPointNamesListSuperLuxury.get(i - 1));
			}

			for (int i = 1; i <= midPointNamesListSuperLuxury.size(); i++) {
				columnKeys.add(String.valueOf(i));
			}

			columnsTblOneSuperLuxury = new ArrayList<ColumnModel>();

			int count = 0;
			for (String columnKey : columnKeys) {
				String key = VALID_COLUMN_KEYS.get(count);
				columnsTblOneSuperLuxury.add(new ColumnModel(key, "midPoint" + columnKey));
				count = count + 1;
			}

		}

	}

	public void onSimilarRoutedSelect() {

		if (selectedRoutes.length > 5) {
			sessionBackingBean.setMessage("You can select only five routes at a time");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			return;
		}
	}

	public void editButtonAction() {

		Map<String, String> requestParameterMap = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();
		String route = requestParameterMap.get("routeNumber");
		String tempTripType = requestParameterMap.get("tripType");
		routeOnEdit = route;
		tripType = tempTripType;

		if (selectedMidPoint.getMidPoint1() != null && !selectedMidPoint.getMidPoint1().isEmpty()
				&& !selectedMidPoint.getMidPoint1().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint1(selectedMidPoint.getMidPoint1());
			editMidpointDTO.setNewMidpointRender1(true);
		}
		if (selectedMidPoint.getMidPoint2() != null && !selectedMidPoint.getMidPoint2().isEmpty()
				&& !selectedMidPoint.getMidPoint2().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint2(selectedMidPoint.getMidPoint2());
			editMidpointDTO.setNewMidpointRender2(true);
		}
		if (selectedMidPoint.getMidPoint3() != null && !selectedMidPoint.getMidPoint3().isEmpty()
				&& !selectedMidPoint.getMidPoint3().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint3(selectedMidPoint.getMidPoint3());
			editMidpointDTO.setNewMidpointRender3(true);
		}
		if (selectedMidPoint.getMidPoint34() != null && !selectedMidPoint.getMidPoint34().isEmpty()
				&& !selectedMidPoint.getMidPoint34().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint34(selectedMidPoint.getMidPoint34());
			editMidpointDTO.setNewMidpointRender34(true);
		}
		if (selectedMidPoint.getMidPoint5() != null && !selectedMidPoint.getMidPoint5().isEmpty()
				&& !selectedMidPoint.getMidPoint5().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint5(selectedMidPoint.getMidPoint5());
			editMidpointDTO.setNewMidpointRender5(true);
		}
		if (selectedMidPoint.getMidPoint6() != null && !selectedMidPoint.getMidPoint6().isEmpty()
				&& !selectedMidPoint.getMidPoint6().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint6(selectedMidPoint.getMidPoint6());
			editMidpointDTO.setNewMidpointRender6(true);
		}
		if (selectedMidPoint.getMidPoint7() != null && !selectedMidPoint.getMidPoint7().isEmpty()
				&& !selectedMidPoint.getMidPoint7().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint7(selectedMidPoint.getMidPoint7());
			editMidpointDTO.setNewMidpointRender7(true);
		}
		if (selectedMidPoint.getMidPoint8() != null && !selectedMidPoint.getMidPoint8().isEmpty()
				&& !selectedMidPoint.getMidPoint8().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint8(selectedMidPoint.getMidPoint8());
			editMidpointDTO.setNewMidpointRender8(true);
		}
		if (selectedMidPoint.getMidPoint9() != null && !selectedMidPoint.getMidPoint9().isEmpty()
				&& !selectedMidPoint.getMidPoint9().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint9(selectedMidPoint.getMidPoint9());
			editMidpointDTO.setNewMidpointRender9(true);
		}
		if (selectedMidPoint.getMidPoint10() != null && !selectedMidPoint.getMidPoint10().isEmpty()
				&& !selectedMidPoint.getMidPoint10().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint10(selectedMidPoint.getMidPoint10());
			editMidpointDTO.setNewMidpointRender10(true);
		}
		if (selectedMidPoint.getMidPoint11() != null && !selectedMidPoint.getMidPoint11().isEmpty()
				&& !selectedMidPoint.getMidPoint11().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint11(selectedMidPoint.getMidPoint11());
			editMidpointDTO.setNewMidpointRender11(true);
		}
		if (selectedMidPoint.getMidPoint12() != null && !selectedMidPoint.getMidPoint12().isEmpty()
				&& !selectedMidPoint.getMidPoint12().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint12(selectedMidPoint.getMidPoint12());
			editMidpointDTO.setNewMidpointRender12(true);
		}
		if (selectedMidPoint.getMidPoint13() != null && !selectedMidPoint.getMidPoint13().isEmpty()
				&& !selectedMidPoint.getMidPoint13().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint13(selectedMidPoint.getMidPoint13());
			editMidpointDTO.setNewMidpointRender13(true);
		}
		if (selectedMidPoint.getMidPoint14() != null && !selectedMidPoint.getMidPoint14().isEmpty()
				&& !selectedMidPoint.getMidPoint14().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint14(selectedMidPoint.getMidPoint14());
			editMidpointDTO.setNewMidpointRender14(true);
		}
		if (selectedMidPoint.getMidPoint15() != null && !selectedMidPoint.getMidPoint15().isEmpty()
				&& !selectedMidPoint.getMidPoint15().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint15(selectedMidPoint.getMidPoint15());
			editMidpointDTO.setNewMidpointRender15(true);
		}
		if (selectedMidPoint.getMidPoint16() != null && !selectedMidPoint.getMidPoint16().isEmpty()
				&& !selectedMidPoint.getMidPoint16().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint16(selectedMidPoint.getMidPoint16());
			editMidpointDTO.setNewMidpointRender16(true);
		}
		if (selectedMidPoint.getMidPoint17() != null && !selectedMidPoint.getMidPoint17().isEmpty()
				&& !selectedMidPoint.getMidPoint17().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint17(selectedMidPoint.getMidPoint17());
			editMidpointDTO.setNewMidpointRender17(true);
		}
		if (selectedMidPoint.getMidPoint18() != null && !selectedMidPoint.getMidPoint18().isEmpty()
				&& !selectedMidPoint.getMidPoint18().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint18(selectedMidPoint.getMidPoint18());
			editMidpointDTO.setNewMidpointRender18(true);
		}
		if (selectedMidPoint.getMidPoint19() != null && !selectedMidPoint.getMidPoint19().isEmpty()
				&& !selectedMidPoint.getMidPoint19().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint19(selectedMidPoint.getMidPoint19());
			editMidpointDTO.setNewMidpointRender19(true);
		}
		if (selectedMidPoint.getMidPoint20() != null && !selectedMidPoint.getMidPoint20().isEmpty()
				&& !selectedMidPoint.getMidPoint20().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint20(selectedMidPoint.getMidPoint20());
			editMidpointDTO.setNewMidpointRender20(true);
		}
		if (selectedMidPoint.getMidPoint21() != null && !selectedMidPoint.getMidPoint21().isEmpty()
				&& !selectedMidPoint.getMidPoint21().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint21(selectedMidPoint.getMidPoint21());
			editMidpointDTO.setNewMidpointRender21(true);
		}
		if (selectedMidPoint.getMidPoint22() != null && !selectedMidPoint.getMidPoint22().isEmpty()
				&& !selectedMidPoint.getMidPoint22().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint22(selectedMidPoint.getMidPoint22());
			editMidpointDTO.setNewMidpointRender22(true);
		}
		if (selectedMidPoint.getMidPoint23() != null && !selectedMidPoint.getMidPoint23().isEmpty()
				&& !selectedMidPoint.getMidPoint23().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint23(selectedMidPoint.getMidPoint23());
			editMidpointDTO.setNewMidpointRender23(true);
		}
		if (selectedMidPoint.getMidPoint24() != null && !selectedMidPoint.getMidPoint24().isEmpty()
				&& !selectedMidPoint.getMidPoint24().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint24(selectedMidPoint.getMidPoint24());
			editMidpointDTO.setNewMidpointRender24(true);
		}
		if (selectedMidPoint.getMidPoint25() != null && !selectedMidPoint.getMidPoint25().isEmpty()
				&& !selectedMidPoint.getMidPoint25().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint25(selectedMidPoint.getMidPoint25());
			editMidpointDTO.setNewMidpointRender25(true);
		}
		if (selectedMidPoint.getMidPoint26() != null && !selectedMidPoint.getMidPoint26().isEmpty()
				&& !selectedMidPoint.getMidPoint26().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint26(selectedMidPoint.getMidPoint26());
			editMidpointDTO.setNewMidpointRender26(true);
		}
		if (selectedMidPoint.getMidPoint27() != null && !selectedMidPoint.getMidPoint27().isEmpty()
				&& !selectedMidPoint.getMidPoint27().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint27(selectedMidPoint.getMidPoint27());
			editMidpointDTO.setNewMidpointRender27(true);
		}
		if (selectedMidPoint.getMidPoint28() != null && !selectedMidPoint.getMidPoint28().isEmpty()
				&& !selectedMidPoint.getMidPoint28().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint28(selectedMidPoint.getMidPoint28());
			editMidpointDTO.setNewMidpointRender28(true);
		}
		if (selectedMidPoint.getMidPoint29() != null && !selectedMidPoint.getMidPoint29().isEmpty()
				&& !selectedMidPoint.getMidPoint29().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint29(selectedMidPoint.getMidPoint29());
			editMidpointDTO.setNewMidpointRender29(true);
		}
		if (selectedMidPoint.getMidPoint30() != null && !selectedMidPoint.getMidPoint30().isEmpty()
				&& !selectedMidPoint.getMidPoint30().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint30(selectedMidPoint.getMidPoint30());
			editMidpointDTO.setNewMidpointRender30(true);
		}
		if (selectedMidPoint.getMidPoint31() != null && !selectedMidPoint.getMidPoint31().isEmpty()
				&& !selectedMidPoint.getMidPoint31().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint31(selectedMidPoint.getMidPoint31());
			editMidpointDTO.setNewMidpointRender31(true);
		}
		if (selectedMidPoint.getMidPoint32() != null && !selectedMidPoint.getMidPoint32().isEmpty()
				&& !selectedMidPoint.getMidPoint32().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint32(selectedMidPoint.getMidPoint32());
			editMidpointDTO.setNewMidpointRender32(true);
		}
		if (selectedMidPoint.getMidPoint33() != null && !selectedMidPoint.getMidPoint33().isEmpty()
				&& !selectedMidPoint.getMidPoint33().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint33(selectedMidPoint.getMidPoint33());
			editMidpointDTO.setNewMidpointRender33(true);
		}
		if (selectedMidPoint.getMidPoint34() != null && !selectedMidPoint.getMidPoint34().isEmpty()
				&& !selectedMidPoint.getMidPoint34().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint34(selectedMidPoint.getMidPoint34());
			editMidpointDTO.setNewMidpointRender34(true);
		}
		if (selectedMidPoint.getMidPoint35() != null && !selectedMidPoint.getMidPoint35().isEmpty()
				&& !selectedMidPoint.getMidPoint35().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint35(selectedMidPoint.getMidPoint35());
			editMidpointDTO.setNewMidpointRender35(true);
		}
		if (selectedMidPoint.getMidPoint36() != null && !selectedMidPoint.getMidPoint36().isEmpty()
				&& !selectedMidPoint.getMidPoint36().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint36(selectedMidPoint.getMidPoint36());
			editMidpointDTO.setNewMidpointRender36(true);
		}
		if (selectedMidPoint.getMidPoint37() != null && !selectedMidPoint.getMidPoint37().isEmpty()
				&& !selectedMidPoint.getMidPoint37().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint37(selectedMidPoint.getMidPoint37());
			editMidpointDTO.setNewMidpointRender37(true);
		}
		if (selectedMidPoint.getMidPoint38() != null && !selectedMidPoint.getMidPoint38().isEmpty()
				&& !selectedMidPoint.getMidPoint38().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint38(selectedMidPoint.getMidPoint38());
			editMidpointDTO.setNewMidpointRender38(true);
		}
		if (selectedMidPoint.getMidPoint39() != null && !selectedMidPoint.getMidPoint39().isEmpty()
				&& !selectedMidPoint.getMidPoint39().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint39(selectedMidPoint.getMidPoint39());
			editMidpointDTO.setNewMidpointRender39(true);
		}
		if (selectedMidPoint.getMidPoint40() != null && !selectedMidPoint.getMidPoint40().isEmpty()
				&& !selectedMidPoint.getMidPoint40().trim().equalsIgnoreCase("")) {
			editMidpointDTO.setMidPoint40(selectedMidPoint.getMidPoint40());
			editMidpointDTO.setNewMidpointRender40(true);
		}

		RequestContext.getCurrentInstance().execute("PF('dlgEditMidpoints').show()");
	}

	public List<RouteDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteDTO> routeList) {
		this.routeList = routeList;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public boolean isNormal() {
		return normal;
	}

	public void setNormal(boolean normal) {
		this.normal = normal;
	}

	public boolean isSuperLuxury() {
		return superLuxury;
	}

	public void setSuperLuxury(boolean superLuxury) {
		this.superLuxury = superLuxury;
	}

	public boolean isExpress() {
		return express;
	}

	public void setExpress(boolean express) {
		this.express = express;
	}

	public boolean isLuxury() {
		return luxury;
	}

	public void setLuxury(boolean luxury) {
		this.luxury = luxury;
	}

	public boolean isSemiLuxury() {
		return semiLuxury;
	}

	public void setSemiLuxury(boolean semiLuxury) {
		this.semiLuxury = semiLuxury;
	}

	public String getSelectedGroup() {
		return selectedGroup;
	}

	public void setSelectedGroup(String selectedGroup) {
		this.selectedGroup = selectedGroup;
	}

	public CombineTimeTableGenerateService getCombineTimeTableGenerateService() {
		return combineTimeTableGenerateService;
	}

	public void setCombineTimeTableGenerateService(CombineTimeTableGenerateService combineTimeTableGenerateService) {
		this.combineTimeTableGenerateService = combineTimeTableGenerateService;
	}

	public static List<String> getVALID_COLUMN_KEYS() {
		return VALID_COLUMN_KEYS;
	}

	public static void setVALID_COLUMN_KEYS(List<String> vALID_COLUMN_KEYS) {
		VALID_COLUMN_KEYS = vALID_COLUMN_KEYS;
	}

	public String getColumnTemplate() {
		return columnTemplate;
	}

	public void setColumnTemplate(String columnTemplate) {
		this.columnTemplate = columnTemplate;
	}

	public List<MidPointTimesDTO> getMidPointsNormal() {
		return midPointsNormal;
	}

	public void setMidPointsNormal(List<MidPointTimesDTO> midPointsNormal) {
		this.midPointsNormal = midPointsNormal;
	}

	public List<MidPointTimesDTO> getMidPointsSuperLuxury() {
		return midPointsSuperLuxury;
	}

	public void setMidPointsSuperLuxury(List<MidPointTimesDTO> midPointsSuperLuxury) {
		this.midPointsSuperLuxury = midPointsSuperLuxury;
	}

	public List<MidPointTimesDTO> getMidPointsExpress() {
		return midPointsExpress;
	}

	public void setMidPointsExpress(List<MidPointTimesDTO> midPointsExpress) {
		this.midPointsExpress = midPointsExpress;
	}

	public List<MidPointTimesDTO> getMidPointsLuxury() {
		return midPointsLuxury;
	}

	public void setMidPointsLuxury(List<MidPointTimesDTO> midPointsLuxury) {
		this.midPointsLuxury = midPointsLuxury;
	}

	public List<MidPointTimesDTO> getMidPointsSemiLuxury() {
		return midPointsSemiLuxury;
	}

	public void setMidPointsSemiLuxury(List<MidPointTimesDTO> midPointsSemiLuxury) {
		this.midPointsSemiLuxury = midPointsSemiLuxury;
	}

	public List<ColumnModel> getColumnsNormal() {
		return columnsNormal;
	}

	public void setColumnsNormal(List<ColumnModel> columnsNormal) {
		this.columnsNormal = columnsNormal;
	}

	public List<ColumnModel> getColumnsSuperLuxury() {
		return columnsSuperLuxury;
	}

	public void setColumnsSuperLuxury(List<ColumnModel> columnsSuperLuxury) {
		this.columnsSuperLuxury = columnsSuperLuxury;
	}

	public List<ColumnModel> getColumnsExpress() {
		return columnsExpress;
	}

	public void setColumnsExpress(List<ColumnModel> columnsExpress) {
		this.columnsExpress = columnsExpress;
	}

	public List<ColumnModel> getColumnsLuxury() {
		return columnsLuxury;
	}

	public void setColumnsLuxury(List<ColumnModel> columnsLuxury) {
		this.columnsLuxury = columnsLuxury;
	}

	public List<ColumnModel> getColumnsSemiLuxury() {
		return columnsSemiLuxury;
	}

	public void setColumnsSemiLuxury(List<ColumnModel> columnsSemiLuxury) {
		this.columnsSemiLuxury = columnsSemiLuxury;
	}

	public List<ColumnModel> getColumnsNormalDtoO() {
		return columnsNormalDtoO;
	}

	public void setColumnsNormalDtoO(List<ColumnModel> columnsNormalDtoO) {
		this.columnsNormalDtoO = columnsNormalDtoO;
	}

	public List<ColumnModel> getColumnsSuperLuxuryDtoO() {
		return columnsSuperLuxuryDtoO;
	}

	public void setColumnsSuperLuxuryDtoO(List<ColumnModel> columnsSuperLuxuryDtoO) {
		this.columnsSuperLuxuryDtoO = columnsSuperLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsExpressDtoO() {
		return columnsExpressDtoO;
	}

	public void setColumnsExpressDtoO(List<ColumnModel> columnsExpressDtoO) {
		this.columnsExpressDtoO = columnsExpressDtoO;
	}

	public List<ColumnModel> getColumnsLuxuryDtoO() {
		return columnsLuxuryDtoO;
	}

	public void setColumnsLuxuryDtoO(List<ColumnModel> columnsLuxuryDtoO) {
		this.columnsLuxuryDtoO = columnsLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsSemiLuxuryDtoO() {
		return columnsSemiLuxuryDtoO;
	}

	public void setColumnsSemiLuxuryDtoO(List<ColumnModel> columnsSemiLuxuryDtoO) {
		this.columnsSemiLuxuryDtoO = columnsSemiLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointsNormalDtoO() {
		return midPointsNormalDtoO;
	}

	public void setMidPointsNormalDtoO(List<MidPointTimesDTO> midPointsNormalDtoO) {
		this.midPointsNormalDtoO = midPointsNormalDtoO;
	}

	public List<MidPointTimesDTO> getMidPointsSuperLuxuryDtoO() {
		return midPointsSuperLuxuryDtoO;
	}

	public void setMidPointsSuperLuxuryDtoO(List<MidPointTimesDTO> midPointsSuperLuxuryDtoO) {
		this.midPointsSuperLuxuryDtoO = midPointsSuperLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointsExpressDtoO() {
		return midPointsExpressDtoO;
	}

	public void setMidPointsExpressDtoO(List<MidPointTimesDTO> midPointsExpressDtoO) {
		this.midPointsExpressDtoO = midPointsExpressDtoO;
	}

	public List<MidPointTimesDTO> getMidPointsLuxuryDtoO() {
		return midPointsLuxuryDtoO;
	}

	public void setMidPointsLuxuryDtoO(List<MidPointTimesDTO> midPointsLuxuryDtoO) {
		this.midPointsLuxuryDtoO = midPointsLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointsSemiLuxuryDtoO() {
		return midPointsSemiLuxuryDtoO;
	}

	public void setMidPointsSemiLuxuryDtoO(List<MidPointTimesDTO> midPointsSemiLuxuryDtoO) {
		this.midPointsSemiLuxuryDtoO = midPointsSemiLuxuryDtoO;
	}

	public List<RouteDTO> getSimilarRoutesList() {
		return similarRoutesList;
	}

	public void setSimilarRoutesList(List<RouteDTO> similarRoutesList) {
		this.similarRoutesList = similarRoutesList;
	}

	public String[] getSelectedRoutes() {
		return selectedRoutes;
	}

	public void setSelectedRoutes(String[] selectedRoutes) {
		this.selectedRoutes = selectedRoutes;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<MidPointTimesDTO> getMidPointTblOneNormal() {
		return midPointTblOneNormal;
	}

	public void setMidPointTblOneNormal(List<MidPointTimesDTO> midPointTblOneNormal) {
		this.midPointTblOneNormal = midPointTblOneNormal;
	}

	public List<MidPointTimesDTO> getMidPointTblOneSuperLuxury() {
		return midPointTblOneSuperLuxury;
	}

	public void setMidPointTblOneSuperLuxury(List<MidPointTimesDTO> midPointTblOneSuperLuxury) {
		this.midPointTblOneSuperLuxury = midPointTblOneSuperLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblOneExpress() {
		return midPointTblOneExpress;
	}

	public void setMidPointTblOneExpress(List<MidPointTimesDTO> midPointTblOneExpress) {
		this.midPointTblOneExpress = midPointTblOneExpress;
	}

	public List<MidPointTimesDTO> getMidPointTblOneLuxury() {
		return midPointTblOneLuxury;
	}

	public void setMidPointTblOneLuxury(List<MidPointTimesDTO> midPointTblOneLuxury) {
		this.midPointTblOneLuxury = midPointTblOneLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblOneSemiLuxury() {
		return midPointTblOneSemiLuxury;
	}

	public void setMidPointTblOneSemiLuxury(List<MidPointTimesDTO> midPointTblOneSemiLuxury) {
		this.midPointTblOneSemiLuxury = midPointTblOneSemiLuxury;
	}

	public List<ColumnModel> getColumnsTblOneNormal() {
		return columnsTblOneNormal;
	}

	public void setColumnsTblOneNormal(List<ColumnModel> columnsTblOneNormal) {
		this.columnsTblOneNormal = columnsTblOneNormal;
	}

	public List<ColumnModel> getColumnsTblOneSuperLuxury() {
		return columnsTblOneSuperLuxury;
	}

	public void setColumnsTblOneSuperLuxury(List<ColumnModel> columnsTblOneSuperLuxury) {
		this.columnsTblOneSuperLuxury = columnsTblOneSuperLuxury;
	}

	public List<ColumnModel> getColumnsTblOneExpress() {
		return columnsTblOneExpress;
	}

	public void setColumnsTblOneExpress(List<ColumnModel> columnsTblOneExpress) {
		this.columnsTblOneExpress = columnsTblOneExpress;
	}

	public List<ColumnModel> getColumnsTblOneLuxury() {
		return columnsTblOneLuxury;
	}

	public void setColumnsTblOneLuxury(List<ColumnModel> columnsTblOneLuxury) {
		this.columnsTblOneLuxury = columnsTblOneLuxury;
	}

	public List<ColumnModel> getColumnsTblOneSemiLuxury() {
		return columnsTblOneSemiLuxury;
	}

	public void setColumnsTblOneSemiLuxury(List<ColumnModel> columnsTblOneSemiLuxury) {
		this.columnsTblOneSemiLuxury = columnsTblOneSemiLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblTwoNormal() {
		return midPointTblTwoNormal;
	}

	public void setMidPointTblTwoNormal(List<MidPointTimesDTO> midPointTblTwoNormal) {
		this.midPointTblTwoNormal = midPointTblTwoNormal;
	}

	public List<MidPointTimesDTO> getMidPointTblTwoSuperLuxury() {
		return midPointTblTwoSuperLuxury;
	}

	public void setMidPointTblTwoSuperLuxury(List<MidPointTimesDTO> midPointTblTwoSuperLuxury) {
		this.midPointTblTwoSuperLuxury = midPointTblTwoSuperLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblTwoExpress() {
		return midPointTblTwoExpress;
	}

	public void setMidPointTblTwoExpress(List<MidPointTimesDTO> midPointTblTwoExpress) {
		this.midPointTblTwoExpress = midPointTblTwoExpress;
	}

	public List<MidPointTimesDTO> getMidPointTblTwoLuxury() {
		return midPointTblTwoLuxury;
	}

	public void setMidPointTblTwoLuxury(List<MidPointTimesDTO> midPointTblTwoLuxury) {
		this.midPointTblTwoLuxury = midPointTblTwoLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblTwoSemiLuxury() {
		return midPointTblTwoSemiLuxury;
	}

	public void setMidPointTblTwoSemiLuxury(List<MidPointTimesDTO> midPointTblTwoSemiLuxury) {
		this.midPointTblTwoSemiLuxury = midPointTblTwoSemiLuxury;
	}

	public List<ColumnModel> getColumnsTblTwoNormal() {
		return columnsTblTwoNormal;
	}

	public void setColumnsTblTwoNormal(List<ColumnModel> columnsTblTwoNormal) {
		this.columnsTblTwoNormal = columnsTblTwoNormal;
	}

	public List<ColumnModel> getColumnsTblTwoSuperLuxury() {
		return columnsTblTwoSuperLuxury;
	}

	public void setColumnsTblTwoSuperLuxury(List<ColumnModel> columnsTblTwoSuperLuxury) {
		this.columnsTblTwoSuperLuxury = columnsTblTwoSuperLuxury;
	}

	public List<ColumnModel> getColumnsTblTwoExpress() {
		return columnsTblTwoExpress;
	}

	public void setColumnsTblTwoExpress(List<ColumnModel> columnsTblTwoExpress) {
		this.columnsTblTwoExpress = columnsTblTwoExpress;
	}

	public List<ColumnModel> getColumnsTblTwoLuxury() {
		return columnsTblTwoLuxury;
	}

	public void setColumnsTblTwoLuxury(List<ColumnModel> columnsTblTwoLuxury) {
		this.columnsTblTwoLuxury = columnsTblTwoLuxury;
	}

	public List<ColumnModel> getColumnsTblTwoSemiLuxury() {
		return columnsTblTwoSemiLuxury;
	}

	public void setColumnsTblTwoSemiLuxury(List<ColumnModel> columnsTblTwoSemiLuxury) {
		this.columnsTblTwoSemiLuxury = columnsTblTwoSemiLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblThreeNormal() {
		return midPointTblThreeNormal;
	}

	public void setMidPointTblThreeNormal(List<MidPointTimesDTO> midPointTblThreeNormal) {
		this.midPointTblThreeNormal = midPointTblThreeNormal;
	}

	public List<MidPointTimesDTO> getMidPointTblThreeSuperLuxury() {
		return midPointTblThreeSuperLuxury;
	}

	public void setMidPointTblThreeSuperLuxury(List<MidPointTimesDTO> midPointTblThreeSuperLuxury) {
		this.midPointTblThreeSuperLuxury = midPointTblThreeSuperLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblThreeExpress() {
		return midPointTblThreeExpress;
	}

	public void setMidPointTblThreeExpress(List<MidPointTimesDTO> midPointTblThreeExpress) {
		this.midPointTblThreeExpress = midPointTblThreeExpress;
	}

	public List<MidPointTimesDTO> getMidPointTblThreeLuxury() {
		return midPointTblThreeLuxury;
	}

	public void setMidPointTblThreeLuxury(List<MidPointTimesDTO> midPointTblThreeLuxury) {
		this.midPointTblThreeLuxury = midPointTblThreeLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblThreeSemiLuxury() {
		return midPointTblThreeSemiLuxury;
	}

	public void setMidPointTblThreeSemiLuxury(List<MidPointTimesDTO> midPointTblThreeSemiLuxury) {
		this.midPointTblThreeSemiLuxury = midPointTblThreeSemiLuxury;
	}

	public List<ColumnModel> getColumnsTblThreeNormal() {
		return columnsTblThreeNormal;
	}

	public void setColumnsTblThreeNormal(List<ColumnModel> columnsTblThreeNormal) {
		this.columnsTblThreeNormal = columnsTblThreeNormal;
	}

	public List<ColumnModel> getColumnsTblThreeSuperLuxury() {
		return columnsTblThreeSuperLuxury;
	}

	public void setColumnsTblThreeSuperLuxury(List<ColumnModel> columnsTblThreeSuperLuxury) {
		this.columnsTblThreeSuperLuxury = columnsTblThreeSuperLuxury;
	}

	public List<ColumnModel> getColumnsTblThreeExpress() {
		return columnsTblThreeExpress;
	}

	public void setColumnsTblThreeExpress(List<ColumnModel> columnsTblThreeExpress) {
		this.columnsTblThreeExpress = columnsTblThreeExpress;
	}

	public List<ColumnModel> getColumnsTblThreeLuxury() {
		return columnsTblThreeLuxury;
	}

	public void setColumnsTblThreeLuxury(List<ColumnModel> columnsTblThreeLuxury) {
		this.columnsTblThreeLuxury = columnsTblThreeLuxury;
	}

	public List<ColumnModel> getColumnsTblThreeSemiLuxury() {
		return columnsTblThreeSemiLuxury;
	}

	public void setColumnsTblThreeSemiLuxury(List<ColumnModel> columnsTblThreeSemiLuxury) {
		this.columnsTblThreeSemiLuxury = columnsTblThreeSemiLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblFourNormal() {
		return midPointTblFourNormal;
	}

	public void setMidPointTblFourNormal(List<MidPointTimesDTO> midPointTblFourNormal) {
		this.midPointTblFourNormal = midPointTblFourNormal;
	}

	public List<MidPointTimesDTO> getMidPointTblFourSuperLuxury() {
		return midPointTblFourSuperLuxury;
	}

	public void setMidPointTblFourSuperLuxury(List<MidPointTimesDTO> midPointTblFourSuperLuxury) {
		this.midPointTblFourSuperLuxury = midPointTblFourSuperLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblFourExpress() {
		return midPointTblFourExpress;
	}

	public void setMidPointTblFourExpress(List<MidPointTimesDTO> midPointTblFourExpress) {
		this.midPointTblFourExpress = midPointTblFourExpress;
	}

	public List<MidPointTimesDTO> getMidPointTblFourLuxury() {
		return midPointTblFourLuxury;
	}

	public void setMidPointTblFourLuxury(List<MidPointTimesDTO> midPointTblFourLuxury) {
		this.midPointTblFourLuxury = midPointTblFourLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblFourSemiLuxury() {
		return midPointTblFourSemiLuxury;
	}

	public void setMidPointTblFourSemiLuxury(List<MidPointTimesDTO> midPointTblFourSemiLuxury) {
		this.midPointTblFourSemiLuxury = midPointTblFourSemiLuxury;
	}

	public List<ColumnModel> getColumnsTblFourNormal() {
		return columnsTblFourNormal;
	}

	public void setColumnsTblFourNormal(List<ColumnModel> columnsTblFourNormal) {
		this.columnsTblFourNormal = columnsTblFourNormal;
	}

	public List<ColumnModel> getColumnsTblFourSuperLuxury() {
		return columnsTblFourSuperLuxury;
	}

	public void setColumnsTblFourSuperLuxury(List<ColumnModel> columnsTblFourSuperLuxury) {
		this.columnsTblFourSuperLuxury = columnsTblFourSuperLuxury;
	}

	public List<ColumnModel> getColumnsTblFourExpress() {
		return columnsTblFourExpress;
	}

	public void setColumnsTblFourExpress(List<ColumnModel> columnsTblFourExpress) {
		this.columnsTblFourExpress = columnsTblFourExpress;
	}

	public List<ColumnModel> getColumnsTblFourLuxury() {
		return columnsTblFourLuxury;
	}

	public void setColumnsTblFourLuxury(List<ColumnModel> columnsTblFourLuxury) {
		this.columnsTblFourLuxury = columnsTblFourLuxury;
	}

	public List<ColumnModel> getColumnsTblFourSemiLuxury() {
		return columnsTblFourSemiLuxury;
	}

	public void setColumnsTblFourSemiLuxury(List<ColumnModel> columnsTblFourSemiLuxury) {
		this.columnsTblFourSemiLuxury = columnsTblFourSemiLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblFiveNormal() {
		return midPointTblFiveNormal;
	}

	public void setMidPointTblFiveNormal(List<MidPointTimesDTO> midPointTblFiveNormal) {
		this.midPointTblFiveNormal = midPointTblFiveNormal;
	}

	public List<MidPointTimesDTO> getMidPointTblFiveSuperLuxury() {
		return midPointTblFiveSuperLuxury;
	}

	public void setMidPointTblFiveSuperLuxury(List<MidPointTimesDTO> midPointTblFiveSuperLuxury) {
		this.midPointTblFiveSuperLuxury = midPointTblFiveSuperLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblFiveExpress() {
		return midPointTblFiveExpress;
	}

	public void setMidPointTblFiveExpress(List<MidPointTimesDTO> midPointTblFiveExpress) {
		this.midPointTblFiveExpress = midPointTblFiveExpress;
	}

	public List<MidPointTimesDTO> getMidPointTblFiveLuxury() {
		return midPointTblFiveLuxury;
	}

	public void setMidPointTblFiveLuxury(List<MidPointTimesDTO> midPointTblFiveLuxury) {
		this.midPointTblFiveLuxury = midPointTblFiveLuxury;
	}

	public List<MidPointTimesDTO> getMidPointTblFiveSemiLuxury() {
		return midPointTblFiveSemiLuxury;
	}

	public void setMidPointTblFiveSemiLuxury(List<MidPointTimesDTO> midPointTblFiveSemiLuxury) {
		this.midPointTblFiveSemiLuxury = midPointTblFiveSemiLuxury;
	}

	public List<ColumnModel> getColumnsTblFiveNormal() {
		return columnsTblFiveNormal;
	}

	public void setColumnsTblFiveNormal(List<ColumnModel> columnsTblFiveNormal) {
		this.columnsTblFiveNormal = columnsTblFiveNormal;
	}

	public List<ColumnModel> getColumnsTblFiveSuperLuxury() {
		return columnsTblFiveSuperLuxury;
	}

	public void setColumnsTblFiveSuperLuxury(List<ColumnModel> columnsTblFiveSuperLuxury) {
		this.columnsTblFiveSuperLuxury = columnsTblFiveSuperLuxury;
	}

	public List<ColumnModel> getColumnsTblFiveExpress() {
		return columnsTblFiveExpress;
	}

	public void setColumnsTblFiveExpress(List<ColumnModel> columnsTblFiveExpress) {
		this.columnsTblFiveExpress = columnsTblFiveExpress;
	}

	public List<ColumnModel> getColumnsTblFiverLuxury() {
		return columnsTblFiverLuxury;
	}

	public void setColumnsTblFiverLuxury(List<ColumnModel> columnsTblFiverLuxury) {
		this.columnsTblFiverLuxury = columnsTblFiverLuxury;
	}

	public List<ColumnModel> getColumnsTblFiveSemiLuxury() {
		return columnsTblFiveSemiLuxury;
	}

	public void setColumnsTblFiveSemiLuxury(List<ColumnModel> columnsTblFiveSemiLuxury) {
		this.columnsTblFiveSemiLuxury = columnsTblFiveSemiLuxury;
	}

	public String getSimilarRouteOne() {
		return similarRouteOne;
	}

	public void setSimilarRouteOne(String similarRouteOne) {
		this.similarRouteOne = similarRouteOne;
	}

	public String getSimilarRouteTwo() {
		return similarRouteTwo;
	}

	public void setSimilarRouteTwo(String similarRouteTwo) {
		this.similarRouteTwo = similarRouteTwo;
	}

	public String getSimilarRouteThree() {
		return similarRouteThree;
	}

	public void setSimilarRouteThree(String similarRouteThree) {
		this.similarRouteThree = similarRouteThree;
	}

	public String getSimilarRouteFour() {
		return similarRouteFour;
	}

	public void setSimilarRouteFour(String similarRouteFour) {
		this.similarRouteFour = similarRouteFour;
	}

	public String getSimilarRouteFive() {
		return similarRouteFive;
	}

	public void setSimilarRouteFive(String similarRouteFive) {
		this.similarRouteFive = similarRouteFive;
	}

	public boolean isSimilarRouteOneRender() {
		return similarRouteOneRender;
	}

	public void setSimilarRouteOneRender(boolean similarRouteOneRender) {
		this.similarRouteOneRender = similarRouteOneRender;
	}

	public boolean isSimilarRouteTwoRender() {
		return similarRouteTwoRender;
	}

	public void setSimilarRouteTwoRender(boolean similarRouteTwoRender) {
		this.similarRouteTwoRender = similarRouteTwoRender;
	}

	public boolean isSimilarRouteThreeRender() {
		return similarRouteThreeRender;
	}

	public void setSimilarRouteThreeRender(boolean similarRouteThreeRender) {
		this.similarRouteThreeRender = similarRouteThreeRender;
	}

	public boolean isSimilarRouteFourRender() {
		return similarRouteFourRender;
	}

	public void setSimilarRouteFourRender(boolean similarRouteFourRender) {
		this.similarRouteFourRender = similarRouteFourRender;
	}

	public boolean isSimilarRouteFiveRender() {
		return similarRouteFiveRender;
	}

	public void setSimilarRouteFiveRender(boolean similarRouteFiveRender) {
		this.similarRouteFiveRender = similarRouteFiveRender;
	}

	public boolean isSimilarRoutesData() {
		return similarRoutesData;
	}

	public void setSimilarRoutesData(boolean similarRoutesData) {
		this.similarRoutesData = similarRoutesData;
	}

	public List<MidPointTimesDTO> getMidPointTblOneNormalDtoO() {
		return midPointTblOneNormalDtoO;
	}

	public void setMidPointTblOneNormalDtoO(List<MidPointTimesDTO> midPointTblOneNormalDtoO) {
		this.midPointTblOneNormalDtoO = midPointTblOneNormalDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblOneSuperLuxuryDtoO() {
		return midPointTblOneSuperLuxuryDtoO;
	}

	public void setMidPointTblOneSuperLuxuryDtoO(List<MidPointTimesDTO> midPointTblOneSuperLuxuryDtoO) {
		this.midPointTblOneSuperLuxuryDtoO = midPointTblOneSuperLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblOneExpressDtoO() {
		return midPointTblOneExpressDtoO;
	}

	public void setMidPointTblOneExpressDtoO(List<MidPointTimesDTO> midPointTblOneExpressDtoO) {
		this.midPointTblOneExpressDtoO = midPointTblOneExpressDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblOneLuxuryDtoO() {
		return midPointTblOneLuxuryDtoO;
	}

	public void setMidPointTblOneLuxuryDtoO(List<MidPointTimesDTO> midPointTblOneLuxuryDtoO) {
		this.midPointTblOneLuxuryDtoO = midPointTblOneLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblOneSemiLuxuryDtoO() {
		return midPointTblOneSemiLuxuryDtoO;
	}

	public void setMidPointTblOneSemiLuxuryDtoO(List<MidPointTimesDTO> midPointTblOneSemiLuxuryDtoO) {
		this.midPointTblOneSemiLuxuryDtoO = midPointTblOneSemiLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblOneNormalDtoO() {
		return columnsTblOneNormalDtoO;
	}

	public void setColumnsTblOneNormalDtoO(List<ColumnModel> columnsTblOneNormalDtoO) {
		this.columnsTblOneNormalDtoO = columnsTblOneNormalDtoO;
	}

	public List<ColumnModel> getColumnsTblOneSuperLuxuryDtoO() {
		return columnsTblOneSuperLuxuryDtoO;
	}

	public void setColumnsTblOneSuperLuxuryDtoO(List<ColumnModel> columnsTblOneSuperLuxuryDtoO) {
		this.columnsTblOneSuperLuxuryDtoO = columnsTblOneSuperLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblOneExpressDtoO() {
		return columnsTblOneExpressDtoO;
	}

	public void setColumnsTblOneExpressDtoO(List<ColumnModel> columnsTblOneExpressDtoO) {
		this.columnsTblOneExpressDtoO = columnsTblOneExpressDtoO;
	}

	public List<ColumnModel> getColumnsTblOneLuxuryDtoO() {
		return columnsTblOneLuxuryDtoO;
	}

	public void setColumnsTblOneLuxuryDtoO(List<ColumnModel> columnsTblOneLuxuryDtoO) {
		this.columnsTblOneLuxuryDtoO = columnsTblOneLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblOneSemiLuxuryDtoO() {
		return columnsTblOneSemiLuxuryDtoO;
	}

	public void setColumnsTblOneSemiLuxuryDtoO(List<ColumnModel> columnsTblOneSemiLuxuryDtoO) {
		this.columnsTblOneSemiLuxuryDtoO = columnsTblOneSemiLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblTwoNormalDtoO() {
		return columnsTblTwoNormalDtoO;
	}

	public void setColumnsTblTwoNormalDtoO(List<ColumnModel> columnsTblTwoNormalDtoO) {
		this.columnsTblTwoNormalDtoO = columnsTblTwoNormalDtoO;
	}

	public List<ColumnModel> getColumnsTblTwoSuperLuxuryDtoO() {
		return columnsTblTwoSuperLuxuryDtoO;
	}

	public void setColumnsTblTwoSuperLuxuryDtoO(List<ColumnModel> columnsTblTwoSuperLuxuryDtoO) {
		this.columnsTblTwoSuperLuxuryDtoO = columnsTblTwoSuperLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblTwoExpressDtoO() {
		return columnsTblTwoExpressDtoO;
	}

	public void setColumnsTblTwoExpressDtoO(List<ColumnModel> columnsTblTwoExpressDtoO) {
		this.columnsTblTwoExpressDtoO = columnsTblTwoExpressDtoO;
	}

	public List<ColumnModel> getColumnsTblTwoLuxuryDtoO() {
		return columnsTblTwoLuxuryDtoO;
	}

	public void setColumnsTblTwoLuxuryDtoO(List<ColumnModel> columnsTblTwoLuxuryDtoO) {
		this.columnsTblTwoLuxuryDtoO = columnsTblTwoLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblTwoSemiLuxuryDtoO() {
		return columnsTblTwoSemiLuxuryDtoO;
	}

	public void setColumnsTblTwoSemiLuxuryDtoO(List<ColumnModel> columnsTblTwoSemiLuxuryDtoO) {
		this.columnsTblTwoSemiLuxuryDtoO = columnsTblTwoSemiLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblTwoNormalDtoO() {
		return midPointTblTwoNormalDtoO;
	}

	public void setMidPointTblTwoNormalDtoO(List<MidPointTimesDTO> midPointTblTwoNormalDtoO) {
		this.midPointTblTwoNormalDtoO = midPointTblTwoNormalDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblTwoSuperLuxuryDtoO() {
		return midPointTblTwoSuperLuxuryDtoO;
	}

	public void setMidPointTblTwoSuperLuxuryDtoO(List<MidPointTimesDTO> midPointTblTwoSuperLuxuryDtoO) {
		this.midPointTblTwoSuperLuxuryDtoO = midPointTblTwoSuperLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblTwoExpressDtoO() {
		return midPointTblTwoExpressDtoO;
	}

	public void setMidPointTblTwoExpressDtoO(List<MidPointTimesDTO> midPointTblTwoExpressDtoO) {
		this.midPointTblTwoExpressDtoO = midPointTblTwoExpressDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblTwoLuxuryDtoO() {
		return midPointTblTwoLuxuryDtoO;
	}

	public void setMidPointTblTwoLuxuryDtoO(List<MidPointTimesDTO> midPointTblTwoLuxuryDtoO) {
		this.midPointTblTwoLuxuryDtoO = midPointTblTwoLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblTwoSemiLuxuryDtoO() {
		return midPointTblTwoSemiLuxuryDtoO;
	}

	public void setMidPointTblTwoSemiLuxuryDtoO(List<MidPointTimesDTO> midPointTblTwoSemiLuxuryDtoO) {
		this.midPointTblTwoSemiLuxuryDtoO = midPointTblTwoSemiLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblThreeNormalDtoO() {
		return midPointTblThreeNormalDtoO;
	}

	public void setMidPointTblThreeNormalDtoO(List<MidPointTimesDTO> midPointTblThreeNormalDtoO) {
		this.midPointTblThreeNormalDtoO = midPointTblThreeNormalDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblThreeSuperLuxuryDtoO() {
		return midPointTblThreeSuperLuxuryDtoO;
	}

	public void setMidPointTblThreeSuperLuxuryDtoO(List<MidPointTimesDTO> midPointTblThreeSuperLuxuryDtoO) {
		this.midPointTblThreeSuperLuxuryDtoO = midPointTblThreeSuperLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblThreeExpressDtoO() {
		return midPointTblThreeExpressDtoO;
	}

	public void setMidPointTblThreeExpressDtoO(List<MidPointTimesDTO> midPointTblThreeExpressDtoO) {
		this.midPointTblThreeExpressDtoO = midPointTblThreeExpressDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblThreeLuxuryDtoO() {
		return midPointTblThreeLuxuryDtoO;
	}

	public void setMidPointTblThreeLuxuryDtoO(List<MidPointTimesDTO> midPointTblThreeLuxuryDtoO) {
		this.midPointTblThreeLuxuryDtoO = midPointTblThreeLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblThreeSemiLuxuryDtoO() {
		return midPointTblThreeSemiLuxuryDtoO;
	}

	public void setMidPointTblThreeSemiLuxuryDtoO(List<MidPointTimesDTO> midPointTblThreeSemiLuxuryDtoO) {
		this.midPointTblThreeSemiLuxuryDtoO = midPointTblThreeSemiLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblThreeNormalDtoO() {
		return columnsTblThreeNormalDtoO;
	}

	public void setColumnsTblThreeNormalDtoO(List<ColumnModel> columnsTblThreeNormalDtoO) {
		this.columnsTblThreeNormalDtoO = columnsTblThreeNormalDtoO;
	}

	public List<ColumnModel> getColumnsTblThreeSuperLuxuryDtoO() {
		return columnsTblThreeSuperLuxuryDtoO;
	}

	public void setColumnsTblThreeSuperLuxuryDtoO(List<ColumnModel> columnsTblThreeSuperLuxuryDtoO) {
		this.columnsTblThreeSuperLuxuryDtoO = columnsTblThreeSuperLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblThreeExpressDtoO() {
		return columnsTblThreeExpressDtoO;
	}

	public void setColumnsTblThreeExpressDtoO(List<ColumnModel> columnsTblThreeExpressDtoO) {
		this.columnsTblThreeExpressDtoO = columnsTblThreeExpressDtoO;
	}

	public List<ColumnModel> getColumnsTblThreeLuxuryDtoO() {
		return columnsTblThreeLuxuryDtoO;
	}

	public void setColumnsTblThreeLuxuryDtoO(List<ColumnModel> columnsTblThreeLuxuryDtoO) {
		this.columnsTblThreeLuxuryDtoO = columnsTblThreeLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblThreeSemiLuxuryDtoO() {
		return columnsTblThreeSemiLuxuryDtoO;
	}

	public void setColumnsTblThreeSemiLuxuryDtoO(List<ColumnModel> columnsTblThreeSemiLuxuryDtoO) {
		this.columnsTblThreeSemiLuxuryDtoO = columnsTblThreeSemiLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblFourNormalDtoO() {
		return midPointTblFourNormalDtoO;
	}

	public void setMidPointTblFourNormalDtoO(List<MidPointTimesDTO> midPointTblFourNormalDtoO) {
		this.midPointTblFourNormalDtoO = midPointTblFourNormalDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblFourSuperLuxuryDtoO() {
		return midPointTblFourSuperLuxuryDtoO;
	}

	public void setMidPointTblFourSuperLuxuryDtoO(List<MidPointTimesDTO> midPointTblFourSuperLuxuryDtoO) {
		this.midPointTblFourSuperLuxuryDtoO = midPointTblFourSuperLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblFourExpressDtoO() {
		return midPointTblFourExpressDtoO;
	}

	public void setMidPointTblFourExpressDtoO(List<MidPointTimesDTO> midPointTblFourExpressDtoO) {
		this.midPointTblFourExpressDtoO = midPointTblFourExpressDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblFourLuxuryDtoO() {
		return midPointTblFourLuxuryDtoO;
	}

	public void setMidPointTblFourLuxuryDtoO(List<MidPointTimesDTO> midPointTblFourLuxuryDtoO) {
		this.midPointTblFourLuxuryDtoO = midPointTblFourLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblFourSemiLuxuryDtoO() {
		return midPointTblFourSemiLuxuryDtoO;
	}

	public void setMidPointTblFourSemiLuxuryDtoO(List<MidPointTimesDTO> midPointTblFourSemiLuxuryDtoO) {
		this.midPointTblFourSemiLuxuryDtoO = midPointTblFourSemiLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblFourNormalDtoO() {
		return columnsTblFourNormalDtoO;
	}

	public void setColumnsTblFourNormalDtoO(List<ColumnModel> columnsTblFourNormalDtoO) {
		this.columnsTblFourNormalDtoO = columnsTblFourNormalDtoO;
	}

	public List<ColumnModel> getColumnsTblFourSuperLuxuryDtoO() {
		return columnsTblFourSuperLuxuryDtoO;
	}

	public void setColumnsTblFourSuperLuxuryDtoO(List<ColumnModel> columnsTblFourSuperLuxuryDtoO) {
		this.columnsTblFourSuperLuxuryDtoO = columnsTblFourSuperLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblFourExpressDtoO() {
		return columnsTblFourExpressDtoO;
	}

	public void setColumnsTblFourExpressDtoO(List<ColumnModel> columnsTblFourExpressDtoO) {
		this.columnsTblFourExpressDtoO = columnsTblFourExpressDtoO;
	}

	public List<ColumnModel> getColumnsTblFourLuxuryDtoO() {
		return columnsTblFourLuxuryDtoO;
	}

	public void setColumnsTblFourLuxuryDtoO(List<ColumnModel> columnsTblFourLuxuryDtoO) {
		this.columnsTblFourLuxuryDtoO = columnsTblFourLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblFourSemiLuxuryDtoO() {
		return columnsTblFourSemiLuxuryDtoO;
	}

	public void setColumnsTblFourSemiLuxuryDtoO(List<ColumnModel> columnsTblFourSemiLuxuryDtoO) {
		this.columnsTblFourSemiLuxuryDtoO = columnsTblFourSemiLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblFiveNormalDtoO() {
		return midPointTblFiveNormalDtoO;
	}

	public void setMidPointTblFiveNormalDtoO(List<MidPointTimesDTO> midPointTblFiveNormalDtoO) {
		this.midPointTblFiveNormalDtoO = midPointTblFiveNormalDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblFiveSuperLuxuryDtoO() {
		return midPointTblFiveSuperLuxuryDtoO;
	}

	public void setMidPointTblFiveSuperLuxuryDtoO(List<MidPointTimesDTO> midPointTblFiveSuperLuxuryDtoO) {
		this.midPointTblFiveSuperLuxuryDtoO = midPointTblFiveSuperLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblFiveExpressDtoO() {
		return midPointTblFiveExpressDtoO;
	}

	public void setMidPointTblFiveExpressDtoO(List<MidPointTimesDTO> midPointTblFiveExpressDtoO) {
		this.midPointTblFiveExpressDtoO = midPointTblFiveExpressDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblFiveLuxuryDtoO() {
		return midPointTblFiveLuxuryDtoO;
	}

	public void setMidPointTblFiveLuxuryDtoO(List<MidPointTimesDTO> midPointTblFiveLuxuryDtoO) {
		this.midPointTblFiveLuxuryDtoO = midPointTblFiveLuxuryDtoO;
	}

	public List<MidPointTimesDTO> getMidPointTblFiveSemiLuxuryDtoO() {
		return midPointTblFiveSemiLuxuryDtoO;
	}

	public void setMidPointTblFiveSemiLuxuryDtoO(List<MidPointTimesDTO> midPointTblFiveSemiLuxuryDtoO) {
		this.midPointTblFiveSemiLuxuryDtoO = midPointTblFiveSemiLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblFiveNormalDtoO() {
		return columnsTblFiveNormalDtoO;
	}

	public void setColumnsTblFiveNormalDtoO(List<ColumnModel> columnsTblFiveNormalDtoO) {
		this.columnsTblFiveNormalDtoO = columnsTblFiveNormalDtoO;
	}

	public List<ColumnModel> getColumnsTblFiveSuperLuxuryDtoO() {
		return columnsTblFiveSuperLuxuryDtoO;
	}

	public void setColumnsTblFiveSuperLuxuryDtoO(List<ColumnModel> columnsTblFiveSuperLuxuryDtoO) {
		this.columnsTblFiveSuperLuxuryDtoO = columnsTblFiveSuperLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblFiveExpressDtoO() {
		return columnsTblFiveExpressDtoO;
	}

	public void setColumnsTblFiveExpressDtoO(List<ColumnModel> columnsTblFiveExpressDtoO) {
		this.columnsTblFiveExpressDtoO = columnsTblFiveExpressDtoO;
	}

	public List<ColumnModel> getColumnsTblFiverLuxuryDtoO() {
		return columnsTblFiverLuxuryDtoO;
	}

	public void setColumnsTblFiverLuxuryDtoO(List<ColumnModel> columnsTblFiverLuxuryDtoO) {
		this.columnsTblFiverLuxuryDtoO = columnsTblFiverLuxuryDtoO;
	}

	public List<ColumnModel> getColumnsTblFiveSemiLuxuryDtoO() {
		return columnsTblFiveSemiLuxuryDtoO;
	}

	public void setColumnsTblFiveSemiLuxuryDtoO(List<ColumnModel> columnsTblFiveSemiLuxuryDtoO) {
		this.columnsTblFiveSemiLuxuryDtoO = columnsTblFiveSemiLuxuryDtoO;
	}

	public MidPointTimesDTO getSelectedMidPoint() {
		return selectedMidPoint;
	}

	public void setSelectedMidPoint(MidPointTimesDTO selectedMidPoint) {
		this.selectedMidPoint = selectedMidPoint;
	}

	public MidPointTimesDTO getEditMidpointDTO() {
		return editMidpointDTO;
	}

	public void setEditMidpointDTO(MidPointTimesDTO editMidpointDTO) {
		this.editMidpointDTO = editMidpointDTO;
	}

	public String getRouteOnEdit() {
		return routeOnEdit;
	}

	public void setRouteOnEdit(String routeOnEdit) {
		this.routeOnEdit = routeOnEdit;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public List<BusFareDTO> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<BusFareDTO> categoryList) {
		this.categoryList = categoryList;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public List<MidPointTimesDTO> getMidPointsNormalList() {
		return midPointsNormalList;
	}

	public void setMidPointsNormalList(List<MidPointTimesDTO> midPointsNormalList) {
		this.midPointsNormalList = midPointsNormalList;
	}

}
