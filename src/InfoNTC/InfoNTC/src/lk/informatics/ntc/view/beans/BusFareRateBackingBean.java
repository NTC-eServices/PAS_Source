package lk.informatics.ntc.view.beans;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import lk.informatics.ntc.model.dto.BusFareDTO;
import lk.informatics.ntc.model.dto.BusFareEquationDTO;
import lk.informatics.ntc.model.dto.BusFareEquationResponseDTO;
import lk.informatics.ntc.model.service.BusFareService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "busFareBackingBean")
@ViewScoped
public class BusFareRateBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<BusFareDTO> busCategoryList = new ArrayList<BusFareDTO>(0);
	private List<BusFareDTO> tempBusCategoryList = new ArrayList<BusFareDTO>(0);

	private List<BusFareDTO> normalRateList;
	private List<BusFareDTO> semiLuxuaryRateList;
	private List<BusFareDTO> luxuaryList;
	private List<BusFareDTO> superLuxuaryList;
	private List<BusFareDTO> highWayList;
	private List<BusFareDTO> sisuSariyaHalfList;
	private List<BusFareDTO> sisuSariyaQuarterList;

	private List<BusFareDTO> rateDetailsList, tempRateDetailsList, currentBusFareDetailsList, viewBusFareRateList,
			tempCurrentBusFareDetailsList, tempViewBusFareRateList;
	private BusFareDTO busFareDTO, fareDTO, tempFareDTO, addBusCategory, viewSelect, tempViewSelect, tempAddBusCategory,
			currentDetailsDTO, viewBusFeeDTO, tempViewBusFeeDTO;
	private CommonService commonService;
	private BusFareService busFareService;
	private int activeTabIndex;
	private boolean disabledOne, editMode, disableTab02, disabledTwo, tempEditMode, disabledBusFareCalculateAction,
			disabledSaveBusFareRate, disabledClearTwo, disabledTempBusFareCalculateAction, disabledTempSaveBusFareRate,
			disabledTempClearTwo, normalFareAdd, luxuaryFareAdded, semiLuxuaryFareAdded, superLuxuaryFareAdded,
			highWayFareAdded, sisuSariyaHalfAdded, sisuSariyaQuarterAdded, tempNormalFareAdd, tempLuxuaryFareAdded,
			tempSemiLuxuaryFareAdded, tempSuperLuxuaryFareAdded, tempHighWayFareAdded, tempSisuSariyaHalfAdded,
			tempSisuSariyaQuarterAdded, disabledNoraml, disabledCategory, disabledTempNormal;
	private String alertMSG, successMessage, errorMessage, yearString;

	@PostConstruct
	public void init() {
		busFareDTO = new BusFareDTO();

		currentDetailsDTO = new BusFareDTO();
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		busFareService = (BusFareService) SpringApplicationContex.getBean("busFareService");
		loadValues();
		fixedNormalCategory();

	}

	public void loadValues() {
		busCategoryList = busFareService.getBusCategory();
		tempBusCategoryList = busFareService.getTempBusCategory();
		disabledOne = true;
		disabledNoraml = true;
		disabledTempNormal = true;
		disabledTwo = true;
		disabledSaveBusFareRate = true;
		disabledCategory = false;
		disabledBusFareCalculateAction = false;
		disabledClearTwo = true;

		disabledTempBusFareCalculateAction = false;
		disabledTempClearTwo = true;
		disabledTempSaveBusFareRate = true;

		rateDetailsList = new ArrayList<>();
		rateDetailsList = busFareService.getDefaultBusCategory();
		tempRateDetailsList = new ArrayList<>();
		tempRateDetailsList = busFareService.getTempDefaultBusCategory();
		busFareDTO.setFareCalculate(true);

		currentDetailsDTO = busFareService.getCurrentDetails();

		if (currentDetailsDTO.getPerviousCostChangeDate() != null) {
			busFareDTO.setPerviousCostChangeDate(currentDetailsDTO.getPerviousCostChangeDate());
		}
		if (currentDetailsDTO.getFareReferenceNo() != null) {
			List<BusFareEquationDTO> list = busFareService
					.getPreviosApprovedEquation(currentDetailsDTO.getFareReferenceNo());
			if (list != null && !list.isEmpty()) {

				for (BusFareDTO defaultRate : rateDetailsList) {
					BusFareEquationDTO equationDTO = getEquation(list, defaultRate.getBusCategoryCode());
					if (equationDTO.getRfeFareCalculated() != null && equationDTO.getRfeFareCalculated().equals("Y")) {
						defaultRate.setFareCalculate(true);
					} else {
						defaultRate.setFareCalculate(false);
					}
					defaultRate.setEquation(equationDTO.getRfeEquation());
				}

			}
		}

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		yearString = String.valueOf("Bus Fare Rate " + year);
	}

	public BusFareEquationDTO getEquation(List<BusFareEquationDTO> list, String busCategoryCode) {

		for (BusFareEquationDTO busFareEquationDTO : list) {
			if (busCategoryCode.equals(busFareEquationDTO.getRfeBusCategoryCode())) {
				return busFareEquationDTO;
			}
		}
		return null;
	}

	public BusFareEquationResponseDTO checkExpressionWithMockData(String equation) {

		BusFareEquationResponseDTO response = validateExpression(equation);
		if (response.getStatus().equals("S")) {

			if (equation.contains("#")) {
				Map<String, Object> contextMap = new HashMap<>();
				for (String abbreviation : response.getAbbreviationList()) {
					contextMap.put(abbreviation, 1);
				}
				Double rate = expressionParser(equation, contextMap);
				if (rate == null) {
					response.setStatus("E");
				}
			} else {
				Double rate = expressionParser(equation, new HashMap<String, Object>());
				if (rate == null) {
					response.setStatus("E");
				}
			}
		}
		return response;
	}

	public static boolean isHashFoundBeforeAbbreviation(String expression) {
		boolean validExpression = true;
		if (expression.contains("#")) {
			for (int i = 0; i < expression.toCharArray().length; i++) {
				char c = expression.toCharArray()[i];
				if (Character.isAlphabetic(c)) {
					try {
						if (expression.toCharArray()[i - 1] != '#'
								&& !Character.isAlphabetic(expression.toCharArray()[i - 1])) {
							validExpression = false;
							return validExpression;
						}
					} catch (Exception e) {
						validExpression = false;
						return validExpression;
					}
				}
			}
		}
		return validExpression;
	}

	public static BusFareEquationResponseDTO validateExpression(String expression) {

		List<String> allowedLetters = Arrays.asList("N", "SM", "L", "SL", "EB", "SH", "SQ");
		BusFareEquationResponseDTO response = new BusFareEquationResponseDTO(new ArrayList<String>(), "S", "Success");

		boolean validExpression = isHashFoundBeforeAbbreviation(expression);

		if (validExpression) {
			for (int i = 0; i < expression.toCharArray().length; i++) {
				char character = expression.toCharArray()[i];
				if (character == '#') {

					String abbreviation = null;

					try {

						if (Character.isAlphabetic(expression.toCharArray()[i + 1])) {
							abbreviation = Character.toString(expression.toCharArray()[i + 1]);
						} else {
							response.setStatus("E");
							response.setMessage("An abbreviation required after the hash tag.");
							return response;
						}
						if (expression.toCharArray().length > i + 2) {
							if (Character.isAlphabetic(expression.toCharArray()[i + 2])) {
								abbreviation = abbreviation + Character.toString(expression.toCharArray()[i + 2]);
							}
						}
						if (expression.toCharArray().length > i + 3) {
							if (Character.isAlphabetic(expression.toCharArray()[i + 3])) {
								response.setStatus("E");
								response.setMessage("Invalid abbreviation length.");
								return response;
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						response.setStatus("E");
						response.setMessage("An abbreviation required after hash tag.");
						return response;
					}

					if (abbreviation != null) {
						if (allowedLetters.contains(abbreviation)) {
							response.getAbbreviationList().add(abbreviation);
							continue;
						} else {
							response.setStatus("E");
							response.setMessage("Invalid abbreviation found in the expression.");
							return response;
						}
					}
				}
			}
		} else {
			response.setStatus("E");
			response.setMessage("Invalid expression!");
			return response;
		}
		return response;
	}

	public static Double expressionParser(String expression, Map<String, Object> mapContext) {
		try {

			StandardEvaluationContext context = new StandardEvaluationContext();
			ExpressionParser parser = new SpelExpressionParser();

			if (expression.contains("#") && mapContext != null && !mapContext.isEmpty()) {
				context.setVariables(mapContext);
			}

			Expression exp = parser.parseExpression(expression);
			Double result = exp.getValue(context, Double.class);
			System.out.println("result " + result);
			return result;
		} catch (ParseException e) {
			return null;
		} catch (EvaluationException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/* Current Fare Rate Method end Here */
	public void fixedNormalCategory() {

		for (BusFareDTO busFareDTO : rateDetailsList) {

			if (busFareDTO.getBusCategoryCode().equals("N")) {
				busFareDTO.setDisabledFareCalculate(true);
			}
		}
	}

	public void ajaxFillDescription() {

		if (activeTabIndex == 0) {

			if (busFareDTO.getBusCategoryCode().equals("N")) {
				disabledOne = false;
				disabledNoraml = true;
				fareDTO = new BusFareDTO();
				fareDTO = busFareService.getBusCategoryDescription(busFareDTO);

				busFareDTO.setSelectBusCategorySinhala(fareDTO.getSelectBusCategorySinhala());
				busFareDTO.setSelectBusCategoryTamil(fareDTO.getSelectBusCategoryTamil());
				activeTabIndex = 0;
				disabledNoraml = false;
			} else {
				disabledOne = false;
				disabledNoraml = false;
				fareDTO = new BusFareDTO();
				fareDTO = busFareService.getBusCategoryDescription(busFareDTO);

				busFareDTO.setSelectBusCategorySinhala(fareDTO.getSelectBusCategorySinhala());
				busFareDTO.setSelectBusCategoryTamil(fareDTO.getSelectBusCategoryTamil());
				activeTabIndex = 0;
			}

		} else {

		}

	}

	public void addBusCategory() {

		if (busFareDTO.getBusCategoryCode() != null && !busFareDTO.getBusCategoryCode().trim().equalsIgnoreCase("")) {

			if (busFareDTO.getSelectBusCategorySinhala() != null
					&& !busFareDTO.getSelectBusCategorySinhala().trim().equalsIgnoreCase("")) {

				if (busFareDTO.getSelectBusCategoryTamil() != null
						&& !busFareDTO.getSelectBusCategoryTamil().trim().equalsIgnoreCase("")) {

					if (busFareDTO.getStatus() != null && !busFareDTO.getStatus().trim().equalsIgnoreCase("")) {

						busCategoryAddingFunction();

					} else {
						setErrorMessage("Please select the status");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please enter selected category in Tamil");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please enter selected category in Sinhala");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please slect the bus category");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void busCategoryAddingFunction() {

		String busCategoryDescription = busFareService.getBusCategoryDescription(busFareDTO.getBusCategoryCode());

		if (editMode == false) {

			addBusCategory = new BusFareDTO(busCategoryDescription, busFareDTO.getBusCategoryCode(),
					busFareDTO.getStatus(), busFareDTO.getRate(), busFareDTO.getSelectBusCategorySinhala(),
					busFareDTO.getSelectBusCategoryTamil(), busFareDTO.isFareCalculate(),
					busFareService.getBusOrderCode(busFareDTO.getBusCategoryCode()));

			boolean isfound = false;

			for (int i = 0; i < rateDetailsList.size(); i++) {
				if (rateDetailsList.get(i).getBusCategoryCode().equals(addBusCategory.getBusCategoryCode())) {
					isfound = true;
					break;
				} else {
					isfound = false;
				}
			}

			if (isfound == false) {

				rateDetailsList.add(addBusCategory);
				fixedNormalCategory();
				disabledOne = true;
				disabledNoraml = true;
				busFareDTO.setBusCategory(null);
				busFareDTO.setBusCategoryCode(null);
				busFareDTO.setStatus(null);
				busFareDTO.setRate(0);
				busFareDTO.setSelectBusCategorySinhala(null);
				busFareDTO.setSelectBusCategoryTamil(null);
				busFareDTO.setFareCalculate(true);
				disabledCategory = false;

			} else {
				setErrorMessage("Selected Data Already Added.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			/* Edit Mood */
			addBusCategory = new BusFareDTO(busCategoryDescription, busFareDTO.getBusCategoryCode(),
					busFareDTO.getStatus(), busFareDTO.getRate(), busFareDTO.getSelectBusCategorySinhala(),
					busFareDTO.getSelectBusCategoryTamil(), busFareDTO.isFareCalculate(),
					busFareService.getBusOrderCode(busFareDTO.getBusCategoryCode()));

			rateDetailsList.remove(viewSelect);
			rateDetailsList.add(addBusCategory);
			fixedNormalCategory();
			busFareDTO.setBusCategory(null);
			busFareDTO.setBusCategoryCode(null);
			busFareDTO.setStatus(null);
			busFareDTO.setRate(0);
			busFareDTO.setSelectBusCategorySinhala(null);
			busFareDTO.setSelectBusCategoryTamil(null);
			busFareDTO.setFareCalculate(true);
			disabledCategory = false;
			disabledOne = true;
			disabledNoraml = true;
			editMode = false;
		}

	}

	public void editAction() {

		if (viewSelect.getBusCategoryCode().equals("N")) {
			busFareDTO.setBusCategoryCode(viewSelect.getBusCategoryCode());
			busFareDTO.setBusCategory(viewSelect.getBusCategory());
			busFareDTO.setRate(viewSelect.getRate());
			busFareDTO.setStatus(viewSelect.getStatus());
			busFareDTO.setSelectBusCategorySinhala(viewSelect.getSelectBusCategorySinhala());
			busFareDTO.setSelectBusCategoryTamil(viewSelect.getSelectBusCategoryTamil());
			busFareDTO.setFareCalculate(viewSelect.isFareCalculate());
			editMode = true;
			disabledOne = false;
			disabledNoraml = true;
			disabledCategory = true;
		} else {
			busFareDTO.setBusCategoryCode(viewSelect.getBusCategoryCode());
			busFareDTO.setBusCategory(viewSelect.getBusCategory());
			busFareDTO.setRate(viewSelect.getRate());
			busFareDTO.setStatus(viewSelect.getStatus());
			busFareDTO.setSelectBusCategorySinhala(viewSelect.getSelectBusCategorySinhala());
			busFareDTO.setSelectBusCategoryTamil(viewSelect.getSelectBusCategoryTamil());
			busFareDTO.setFareCalculate(viewSelect.isFareCalculate());
			editMode = true;
			disabledOne = false;
			disabledNoraml = false;
			disabledCategory = true;
		}

	}

	public Map<String, Object> getMapContext(List<String> abbreviationList, BusFareDTO currentStage) {

		Map<String, Object> contexMap = new HashMap<String, Object>();
		for (String abbreviation : abbreviationList) {
			switch (abbreviation) {
			case "N":
				contexMap.put("N", currentStage.getNormalCurrentFee().doubleValue());
				break;
			case "SM":
				contexMap.put("SM", currentStage.getSemiLuxuryCurrentFee().doubleValue());
				break;
			case "L":
				contexMap.put("L", currentStage.getLuxuryCurrentFee().doubleValue());
				break;
			case "SL":
				contexMap.put("SL", currentStage.getSuperLuxuryCurrentFee().doubleValue());
				break;
			case "EB":
				contexMap.put("EB", currentStage.getHighwayCurrentFee().doubleValue());
				break;
			case "SH":
				contexMap.put("SH", currentStage.getSisuSariyaHalfNoramlFee().doubleValue());
				break;
			case "SQ":
				contexMap.put("SQ", currentStage.getSisuSariyaQuarterNoramlFee().doubleValue());
				break;
			}
		}

		return contexMap;
	}

	public boolean checkExpressionIsEmpty() {
		for (BusFareDTO busFareDTO : rateDetailsList) {
			if ((busFareDTO.getEquation() == null || busFareDTO.getEquation().trim().equalsIgnoreCase(""))
					&& busFareDTO.isFareCalculate() == true && busFareDTO.getStatus().equals("ACTIVE")) {
				return true;
			}
		}
		return false;
	}

	public String checkExpressionIsValid() {
		for (BusFareDTO busFareDTO : rateDetailsList) {

			if (busFareDTO.isFareCalculate() && busFareDTO.getStatus().equals("ACTIVE")) {
				BusFareEquationResponseDTO responseDTO = checkExpressionWithMockData(busFareDTO.getEquation());
				if (!responseDTO.getStatus().equals("S")) {
					return "Invalid equation for " + busFareDTO.getBusCategory() + ".";
				}
			}
		}
		return "S";
	}

	/* Calculate Bus Fare Rate */
	public void busFareCalculateAction() {

		if (checkExpressionIsEmpty() == false) {
			String validateMessage = checkExpressionIsValid();

			if (validateMessage.equals("S")) {

				currentBusFareDetailsList = new ArrayList<>();
				currentBusFareDetailsList = busFareService.getCurrentBusFare();

				normalRateList = new ArrayList<BusFareDTO>();
				semiLuxuaryRateList = new ArrayList<BusFareDTO>();
				luxuaryList = new ArrayList<BusFareDTO>();
				highWayList = new ArrayList<BusFareDTO>();
				superLuxuaryList = new ArrayList<BusFareDTO>();
				sisuSariyaHalfList = new ArrayList<BusFareDTO>();
				sisuSariyaQuarterList = new ArrayList<BusFareDTO>();

				normalFareAdd = false;
				semiLuxuaryFareAdded = false;
				luxuaryFareAdded = false;
				superLuxuaryFareAdded = false;
				highWayFareAdded = false;
				sisuSariyaHalfAdded = false;
				sisuSariyaQuarterAdded = false;

				for (BusFareDTO dto : rateDetailsList) {

					/* Calculate Normal Bus Fare */
					if (dto.getBusCategoryCode().equals("N") && dto.isFareCalculate() == true
							&& dto.getStatus().equals("ACTIVE")) {

						if (dto.getEquation() != null && !dto.getEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < currentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											currentBusFareDetailsList.get(i));

									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getEquation(), new HashMap<String, Object>());
								}

								BigDecimal NEW_RATE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = NEW_RATE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = NEW_RATE.setScale(0, BigDecimal.ROUND_HALF_UP);
								normalRateList.add(noramlDTO(currentBusFareDetailsList.get(i).getNormalCurrentFee()
										.setScale(2, BigDecimal.ROUND_HALF_UP), NEW_RATE_WITH_DECIMAL, ROUNDED_FEE));
								normalFareAdd = true;
							}
						}

					}

					/* Calculate Semi Luxury Bus Fare */
					if (dto.getBusCategoryCode().equals("SM") && dto.isFareCalculate() == true
							&& dto.getStatus().equals("ACTIVE")) {

						if (dto.getEquation() != null && !dto.getEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < currentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (currentBusFareDetailsList.get(i).getStageNo() == 1) {
									int a = 0;
								}

								if (dto.getEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											currentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = BUS_FARE.setScale(0, BigDecimal.ROUND_HALF_UP);
								semiLuxuaryRateList.add(
										SemiDTO(currentBusFareDetailsList.get(i).getSemiLuxuryCurrentFee().setScale(2,
												BigDecimal.ROUND_HALF_UP), NEW_RATE_WITH_DECIMAL, ROUNDED_FEE));
								semiLuxuaryFareAdded = true;
							}
						}

					}

					/* Calculate Luxury Bus Fare */
					if (dto.getBusCategoryCode().equals("L") && dto.isFareCalculate() == true
							&& dto.getStatus().equals("ACTIVE")) {

						if (dto.getEquation() != null && !dto.getEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < currentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											currentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								luxuaryList.add(luxuaryDTO(currentBusFareDetailsList.get(i).getLuxuryCurrentFee()
										.setScale(2, BigDecimal.ROUND_HALF_UP), NEW_RATE_WITH_DECIMAL, roundToNearest5(NEW_RATE_WITH_DECIMAL)));

								luxuaryFareAdded = true;

							}
						}

					}

					/* Calculate High-way Bus Fare */
					if (dto.getBusCategoryCode().equals("EB") && dto.isFareCalculate() == true
							&& dto.getStatus().equals("ACTIVE")) {

						if (dto.getEquation() != null && !dto.getEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < currentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											currentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								highWayList.add(highwayDTO(currentBusFareDetailsList.get(i).getHighwayCurrentFee()
										.setScale(2, BigDecimal.ROUND_HALF_UP), NEW_RATE_WITH_DECIMAL, roundToNearest5(NEW_RATE_WITH_DECIMAL)));
								highWayFareAdded = true;

							}
						}

					}

					/* Calculate Super Luxury Bus Fare */
					if (dto.getBusCategoryCode().equals("SL") && dto.isFareCalculate() == true
							&& dto.getStatus().equals("ACTIVE")) {

						if (dto.getEquation() != null && !dto.getEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < currentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											currentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								superLuxuaryList.add(superDTO(currentBusFareDetailsList.get(i).getSuperLuxuryCurrentFee().setScale(2, BigDecimal.ROUND_HALF_UP), NEW_RATE_WITH_DECIMAL, roundToNearest10(NEW_RATE_WITH_DECIMAL)));
								superLuxuaryFareAdded = true;

							}
						}

					}

					/* Calculate Sisu Sariya 1/2 Bus Fare */
					if (dto.getBusCategoryCode().equals("SH") && dto.isFareCalculate() == true
							&& dto.getStatus().equals("ACTIVE")) {

						if (dto.getEquation() != null && !dto.getEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < currentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											currentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = BUS_FARE.setScale(0, BigDecimal.ROUND_HALF_UP);

								BigDecimal DIFFRENT_FARE_FINAL = ROUNDED_FEE.subtract(NEW_RATE_WITH_DECIMAL);
								sisuSariyaHalfList.add(
										sisuSariyaHalfDTO(NEW_RATE_WITH_DECIMAL, ROUNDED_FEE, DIFFRENT_FARE_FINAL));
								sisuSariyaHalfAdded = true;

							}
						}

					}

					/* Calculate Sisu Sariya 1/4 Bus Fare */
					if (dto.getBusCategoryCode().equals("SQ") && dto.isFareCalculate() == true
							&& dto.getStatus().equals("ACTIVE")) {

						if (dto.getEquation() != null && !dto.getEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < currentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											currentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = BUS_FARE.setScale(0, BigDecimal.ROUND_HALF_UP);
								BigDecimal DIFFRENT_FARE_FINAL = ROUNDED_FEE.subtract(NEW_RATE_WITH_DECIMAL);
								sisuSariyaQuarterList.add(
										sisuSariyaQuaterDTO(NEW_RATE_WITH_DECIMAL, ROUNDED_FEE, DIFFRENT_FARE_FINAL));
								sisuSariyaQuarterAdded = true;
							}
						}

					}

				}

				/* Add Calculated Fare To List */
				if (normalFareAdd == true || luxuaryFareAdded == true || semiLuxuaryFareAdded == true
						|| superLuxuaryFareAdded == true || highWayFareAdded == true || sisuSariyaHalfAdded == true
						|| sisuSariyaQuarterAdded == true) {

					if (addAllFareRatesToViewList() == false) {
						RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");
						disabledSaveBusFareRate = false;
						disabledClearTwo = false;

					} else {
						setErrorMessage("No data found. Please try again!");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				}
			} else {
				setErrorMessage(validateMessage);
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Equation cannot be empty.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	/* Add Fare Rates to List and View List */
	public boolean addAllFareRatesToViewList() {

		boolean isViewListEmpty = false;

		viewBusFareRateList = new ArrayList<>();
		/* If lists are empty, Add null values to each list */
		for (int x = 0; x < currentBusFareDetailsList.size(); x++) {

			if (normalFareAdd == false) {
				normalRateList.add(noramlDTO(null, null, null));
			}
			if (semiLuxuaryFareAdded == false) {
				semiLuxuaryRateList.add(sisuSariyaHalfDTO(null, null, null));
			}
			if (luxuaryFareAdded == false) {
				luxuaryList.add(luxuaryDTO(null, null, null));
			}
			if (superLuxuaryFareAdded == false) {
				superLuxuaryList.add(superDTO(null, null, null));
			}
			if (highWayFareAdded == false) {
				highWayList.add(highwayDTO(null, null, null));
			}
			if (sisuSariyaHalfAdded == false) {
				sisuSariyaHalfList.add(sisuSariyaHalfDTO(null, null, null));
			}
			if (sisuSariyaQuarterAdded == false) {
				sisuSariyaQuarterList.add(sisuSariyaQuaterDTO(null, null, null));
			}
		}
		/* Add fare rates to main list */
		for (int i = 0; i < currentBusFareDetailsList.size(); i++) {

			if (currentBusFareDetailsList.get(i).getStageNo() != 0) {

				viewBusFeeDTO = new BusFareDTO(currentBusFareDetailsList.get(i).getStageNo(),
						normalRateList.get(i).getNormalCurrentFee(), normalRateList.get(i).getNormalNewFee(),
						normalRateList.get(i).getNormalRoundFee(), semiLuxuaryRateList.get(i).getSemiLuxuryCurrentFee(),
						semiLuxuaryRateList.get(i).getSemiLuxuryNewFee(),
						semiLuxuaryRateList.get(i).getSemiLuxuryRoundFee(), luxuaryList.get(i).getLuxuryCurrentFee(),
						luxuaryList.get(i).getLuxuryNewFee(), luxuaryList.get(i).getLuxuryRoundFee(),
						highWayList.get(i).getHighwayCurrentFee(), highWayList.get(i).getHighwayNewFee(),
						highWayList.get(i).getHighwayRoundFee(), superLuxuaryList.get(i).getSuperLuxuryCurrentFee(),
						superLuxuaryList.get(i).getSuperLuxuryNewFee(),
						superLuxuaryList.get(i).getSuperLuxuryRoundFee(),
						sisuSariyaHalfList.get(i).getSisuSariyaHalfNoramlFee(),
						sisuSariyaHalfList.get(i).getSisuSariyaHalfAdjestedFee(),
						sisuSariyaHalfList.get(i).getSisuSariyaHalfdiffrentFee(),
						sisuSariyaQuarterList.get(i).getSisuSariyaQuarterNoramlFee(),
						sisuSariyaQuarterList.get(i).getSisuSariyaQuarterAdjestedFee(),
						sisuSariyaQuarterList.get(i).getSisuSariyaQuarterdiffrentFee());

				viewBusFareRateList.add(viewBusFeeDTO);

				if (viewBusFareRateList.isEmpty()) {
					isViewListEmpty = true;
				} else {
					isViewListEmpty = false;
				}
			}

		}

		return isViewListEmpty;

	}

	public void closeRatePopUp() {
		RequestContext.getCurrentInstance().execute("PF('viewFareRate').hide()");
	}

	/* Save Bus Fare */
	public void saveBusFareRate() {

		if (busFareService.checkPendingRecordCount() == 0) {

			/* Add Removed Stage to List [Stage 0] */

			BigDecimal zero = new BigDecimal(0);
			viewBusFeeDTO = new BusFareDTO(0, zero, zero, zero, zero, zero, zero, zero, zero, zero, zero, zero, zero,
					zero, zero, zero, zero, zero, zero, zero, zero, zero);

			this.viewBusFareRateList.add(0, viewBusFeeDTO);
			/* Generate Reference No */
			String fareReferenceNo = busFareService.generateFareReferenceNo();

			/* Save Bus Fare Rates */
			if (busFareService.masterTableInjection(busFareDTO, fareReferenceNo,
					sessionBackingBean.getLoginUser()) == true) {

				busFareService.insertReferenceData(fareReferenceNo, currentBusFareDetailsList,
						sessionBackingBean.getLoginUser(), this.viewBusFareRateList);

				if (normalFareAdd == true) {
					busFareService.updateNormalBusRate(viewBusFareRateList, sessionBackingBean.getLoginUser());
				}
				if (semiLuxuaryFareAdded == true) {

					busFareService.updateSemiLuxuryBusRate(viewBusFareRateList, sessionBackingBean.getLoginUser());
				}
				if (luxuaryFareAdded == true) {

					busFareService.updateLuxuryBusRate(viewBusFareRateList, sessionBackingBean.getLoginUser());
				}
				if (superLuxuaryFareAdded == true) {

					busFareService.updateSuperLuxuryBusRate(viewBusFareRateList, sessionBackingBean.getLoginUser());
				}
				if (highWayFareAdded == true) {

					busFareService.updateHighWayBusRate(viewBusFareRateList, sessionBackingBean.getLoginUser());
				}
				if (sisuSariyaHalfAdded == true) {

					busFareService.updatetHalfSisuSariyaBusRate(viewBusFareRateList, sessionBackingBean.getLoginUser());
				}
				if (sisuSariyaQuarterAdded == true) {

					busFareService.updateQuarterSisuSariyBusRate(viewBusFareRateList,
							sessionBackingBean.getLoginUser());
				}

				disabledSaveBusFareRate = true;

				busFareDTO.setFareReferenceNo(fareReferenceNo);
				setSuccessMessage("The bus fare saved successfully.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				busFareService.insertFareEquationDetails(rateDetailsList, fareReferenceNo);

			} else {

				disabledSaveBusFareRate = true;
				setErrorMessage("Cannot save bus fare. Error occurred!");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {

			setErrorMessage("A pending recoard already found.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearOne() {
		busFareDTO.setBusCategory(null);
		busFareDTO.setBusCategory(null);
		busFareDTO.setSelectBusCategorySinhala(null);
		busFareDTO.setSelectBusCategoryTamil(null);
		busFareDTO.setRate(0);
		busFareDTO.setStatus(null);
		disabledOne = true;
		disabledNoraml = true;
	}

	public void clearTwo() {

		busFareDTO.setBusCategory(null);
		busFareDTO = new BusFareDTO();
		busFareDTO.setSelectBusCategorySinhala(null);
		busFareDTO.setSelectBusCategoryTamil(null);
		busFareDTO.setRate(0);
		busFareDTO.setStatus(null);
		disabledOne = true;
		disabledClearTwo = true;
		disabledNoraml = true;
		disabledSaveBusFareRate = true;

		currentDetailsDTO = busFareService.getCurrentDetails();

		if (currentDetailsDTO.getPerviousCostChangeDate() != null) {
			busFareDTO.setPerviousCostChangeDate(currentDetailsDTO.getPerviousCostChangeDate());
		}
		if (currentDetailsDTO.getFareReferenceNo() != null) {
			List<BusFareEquationDTO> list = busFareService
					.getPreviosApprovedEquation(currentDetailsDTO.getFareReferenceNo());
			if (list != null && !list.isEmpty()) {

				for (BusFareDTO defaultRate : rateDetailsList) {
					BusFareEquationDTO equationDTO = getEquation(list, defaultRate.getBusCategoryCode());
					if (equationDTO.getRfeFareCalculated() != null && equationDTO.getRfeFareCalculated().equals("Y")) {
						defaultRate.setFareCalculate(true);
					} else {
						defaultRate.setFareCalculate(false);
					}
					defaultRate.setEquation(equationDTO.getRfeEquation());
				}

			}
		}

	}

	/* Current Fare Rate Method end Here */

	/* Temporary Fare Rate Method Start Here */

	public void ajaxTempFillDescription() {

		if (busFareDTO.getTempBusCategoryCode().equals("N")) {
			disabledTwo = false;
			disabledTempNormal = true;
			tempFareDTO = new BusFareDTO();
			tempFareDTO = busFareService.getTempBusCategoryDescription(busFareDTO);
			busFareDTO.setTempSelectBusCategorySinhala(tempFareDTO.getTempSelectBusCategorySinhala());
			busFareDTO.setTempSelectBusCategoryTamil(tempFareDTO.getTempSelectBusCategoryTamil());
			activeTabIndex = 1;
		} else {
			disabledTwo = false;
			disabledTempNormal = false;
			tempFareDTO = new BusFareDTO();
			tempFareDTO = busFareService.getTempBusCategoryDescription(busFareDTO);
			busFareDTO.setTempSelectBusCategorySinhala(tempFareDTO.getTempSelectBusCategorySinhala());
			busFareDTO.setTempSelectBusCategoryTamil(tempFareDTO.getTempSelectBusCategoryTamil());
			activeTabIndex = 1;
		}

	}

	public void addTempBusCategory() {
		if (busFareDTO.getTempBusCategoryCode() != null
				&& !busFareDTO.getTempBusCategoryCode().trim().equalsIgnoreCase("")) {

			if (busFareDTO.getTempSelectBusCategorySinhala() != null
					&& !busFareDTO.getTempSelectBusCategorySinhala().trim().equalsIgnoreCase("")) {

				if (busFareDTO.getTempSelectBusCategoryTamil() != null
						&& !busFareDTO.getTempSelectBusCategoryTamil().trim().equalsIgnoreCase("")) {

					if (busFareDTO.getTempStatus() != null && !busFareDTO.getTempStatus().trim().equalsIgnoreCase("")) {

						tempBusCategoryAddingFunction();

					} else {
						setErrorMessage("Please select the status");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Please enter selected category in tamil");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please enter selected category in sinhala");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please select the bus category");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void tempBusCategoryAddingFunction() {

		String busCategoryDescription = busFareService.getBusCategoryDescription(busFareDTO.getTempBusCategoryCode());

		if (tempEditMode == false) {

			tempAddBusCategory = new BusFareDTO(busCategoryDescription, busFareDTO.getTempBusCategoryCode(),
					busFareDTO.getTempStatus(), busFareDTO.getTempRate(), busFareDTO.getTempSelectBusCategorySinhala(),
					busFareDTO.getTempSelectBusCategoryTamil(),
					busFareService.getBusOrderCode(busFareDTO.getTempBusCategoryCode()));

			boolean isfoundTemp = false;

			for (int i = 0; i < tempRateDetailsList.size(); i++) {
				if (tempRateDetailsList.get(i).getTempBusCategoryCode()
						.equals(tempAddBusCategory.getTempBusCategoryCode())) {
					isfoundTemp = true;
					break;
				} else {
					isfoundTemp = false;
				}
			}

			if (isfoundTemp == false) {

				tempRateDetailsList.add(tempAddBusCategory);
				busFareDTO.setTempBusCategory(null);
				busFareDTO.setTempBusCategoryCode(null);
				busFareDTO.setTempStatus(null);
				busFareDTO.setTempRate(0);
				busFareDTO.setTempSelectBusCategorySinhala(null);
				busFareDTO.setTempSelectBusCategoryTamil(null);

				disabledTempClearTwo = false;
				disabledTempSaveBusFareRate = false;

				disabledTwo = true;
				disabledTempNormal = true;
			} else {
				setErrorMessage("Selected Data Already Added.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {

			tempAddBusCategory = new BusFareDTO(busCategoryDescription, busFareDTO.getTempBusCategoryCode(),
					busFareDTO.getTempStatus(), busFareDTO.getTempRate(), busFareDTO.getTempSelectBusCategorySinhala(),
					busFareDTO.getTempSelectBusCategoryTamil(),
					busFareService.getBusOrderCode(busFareDTO.getTempBusCategoryCode()));

			tempRateDetailsList.remove(tempViewSelect);
			tempRateDetailsList.add(tempAddBusCategory);
			busFareDTO.setTempBusCategory(null);
			busFareDTO.setTempBusCategoryCode(null);
			busFareDTO.setTempStatus(null);
			busFareDTO.setTempRate(0);
			busFareDTO.setTempSelectBusCategorySinhala(null);
			busFareDTO.setTempSelectBusCategoryTamil(null);

			disabledTempClearTwo = false;
			disabledTempSaveBusFareRate = false;

			disabledTwo = true;
			disabledTempNormal = true;
			tempEditMode = false;
		}
	}

	public void editTempAction() {

		if (tempViewSelect.getTempBusCategoryCode().equals("N")) {
			busFareDTO.setTempBusCategoryCode(tempViewSelect.getTempBusCategoryCode());
			busFareDTO.setTempBusCategory(tempViewSelect.getTempBusCategory());
			busFareDTO.setTempRate(tempViewSelect.getTempRate());
			busFareDTO.setTempStatus(tempViewSelect.getTempStatus());
			busFareDTO.setTempSelectBusCategorySinhala(tempViewSelect.getTempSelectBusCategorySinhala());
			busFareDTO.setTempSelectBusCategoryTamil(tempViewSelect.getTempSelectBusCategoryTamil());
			tempEditMode = true;
			disabledTwo = false;
			disabledTempNormal = true;
		} else {
			busFareDTO.setTempBusCategoryCode(tempViewSelect.getTempBusCategoryCode());
			busFareDTO.setTempBusCategory(tempViewSelect.getTempBusCategory());
			busFareDTO.setTempRate(tempViewSelect.getTempRate());
			busFareDTO.setTempStatus(tempViewSelect.getTempStatus());
			busFareDTO.setTempSelectBusCategorySinhala(tempViewSelect.getTempSelectBusCategorySinhala());
			busFareDTO.setTempSelectBusCategoryTamil(tempViewSelect.getTempSelectBusCategoryTamil());
			tempEditMode = true;
			disabledTwo = false;
			disabledTempNormal = false;
		}
	}

	/* Calculate Temporary Bus Fare Rate */
	public void tempBusFareCalculateAction() {

		if (tempCheckExpressionIsEmpty() == false) {
			String validateMessage = tempCheckExpressionIsValid();

			if (validateMessage.equals("S")) {

				tempCurrentBusFareDetailsList = new ArrayList<>();
				tempCurrentBusFareDetailsList = busFareService.getCurrentBusFare();

				normalRateList = new ArrayList<>();
				semiLuxuaryRateList = new ArrayList<>();
				luxuaryList = new ArrayList<>();
				superLuxuaryList = new ArrayList<>();
				highWayList = new ArrayList<>();
				sisuSariyaHalfList = new ArrayList<>();
				sisuSariyaQuarterList = new ArrayList<>();

				tempNormalFareAdd = false;
				tempSemiLuxuaryFareAdded = false;
				tempLuxuaryFareAdded = false;
				tempSuperLuxuaryFareAdded = false;
				tempHighWayFareAdded = false;
				tempSisuSariyaHalfAdded = false;
				tempSisuSariyaQuarterAdded = false;

				for (BusFareDTO dto : tempRateDetailsList) {

					/* Calculate Normal Bus Fare */
					if (dto.getTempBusCategoryCode().equals("N") && dto.getTempStatus().equals("ACTIVE")) {

						if (dto.getTempEquation() != null && !dto.getTempEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < tempCurrentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getTempEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getTempEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											tempCurrentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getTempEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getTempEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = BUS_FARE.setScale(0, BigDecimal.ROUND_HALF_UP);

								normalRateList.add(noramlDTO(tempCurrentBusFareDetailsList.get(i).getNormalCurrentFee()
										.setScale(2, BigDecimal.ROUND_HALF_UP), NEW_RATE_WITH_DECIMAL, ROUNDED_FEE));
								tempNormalFareAdd = true;

							}
						}
					}

					/* Calculate Semi Luxury Bus Fare */
					if (dto.getTempBusCategoryCode().equals("SM") && dto.getTempStatus().equals("ACTIVE")) {

						if (dto.getTempEquation() != null && !dto.getTempEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < tempCurrentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getTempEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getTempEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											tempCurrentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getTempEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getTempEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = BUS_FARE.setScale(0, BigDecimal.ROUND_HALF_UP);

								semiLuxuaryRateList
										.add(SemiDTO(
												tempCurrentBusFareDetailsList.get(i).getSemiLuxuryCurrentFee()
														.setScale(2, BigDecimal.ROUND_HALF_UP),
												NEW_RATE_WITH_DECIMAL, ROUNDED_FEE));
								tempSemiLuxuaryFareAdded = true;

							}
						}

					}

					/* Calculate Luxury Bus Fare */
					if (dto.getTempBusCategoryCode().equals("L") && dto.getTempStatus().equals("ACTIVE")) {

						if (dto.getTempEquation() != null && !dto.getTempEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < tempCurrentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getTempEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getTempEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											tempCurrentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getTempEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getTempEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = BUS_FARE.setScale(0, BigDecimal.ROUND_HALF_UP);

								luxuaryList.add(luxuaryDTO(tempCurrentBusFareDetailsList.get(i).getLuxuryCurrentFee()
										.setScale(2, BigDecimal.ROUND_HALF_UP), NEW_RATE_WITH_DECIMAL, ROUNDED_FEE));

								tempLuxuaryFareAdded = true;

							}
						}

					}

					/* Calculate High-way Bus Fare */
					if (dto.getTempBusCategoryCode().equals("EB") && dto.getTempStatus().equals("ACTIVE")) {

						if (dto.getTempEquation() != null && !dto.getTempEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < tempCurrentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getTempEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getTempEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											tempCurrentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getTempEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getTempEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = BUS_FARE.setScale(0, BigDecimal.ROUND_HALF_UP);

								highWayList.add(highwayDTO(tempCurrentBusFareDetailsList.get(i).getHighwayCurrentFee()
										.setScale(2, BigDecimal.ROUND_HALF_UP), NEW_RATE_WITH_DECIMAL, ROUNDED_FEE));

								tempHighWayFareAdded = true;

							}
						}

					}

					/* Calculate Super Luxury Bus Fare */
					if (dto.getTempBusCategoryCode().equals("SL") && dto.getTempStatus().equals("ACTIVE")) {

						if (dto.getTempEquation() != null && !dto.getTempEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < tempCurrentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getTempEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getTempEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											tempCurrentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getTempEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getTempEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = BUS_FARE.setScale(0, BigDecimal.ROUND_HALF_UP);

								superLuxuaryList
										.add(superDTO(
												tempCurrentBusFareDetailsList.get(i).getSuperLuxuryCurrentFee()
														.setScale(2, BigDecimal.ROUND_HALF_UP),
												NEW_RATE_WITH_DECIMAL, ROUNDED_FEE));
								tempSuperLuxuaryFareAdded = true;

							}
						}

					}

					/* Calculate Sisu Sariya 1/2 Bus Fare */
					if (dto.getTempBusCategoryCode().equals("SH") && dto.getTempStatus().equals("ACTIVE")) {

						if (dto.getTempEquation() != null && !dto.getTempEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < tempCurrentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getTempEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getTempEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											tempCurrentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getTempEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getTempEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = BUS_FARE.setScale(0, BigDecimal.ROUND_HALF_UP);

								BigDecimal DIFFRENT_FARE_FINAL = ROUNDED_FEE.subtract(NEW_RATE_WITH_DECIMAL);

								sisuSariyaHalfList.add(
										sisuSariyaHalfDTO(NEW_RATE_WITH_DECIMAL, ROUNDED_FEE, DIFFRENT_FARE_FINAL));
								tempSisuSariyaHalfAdded = true;

							}
						}

					}

					/* Calculate Sisu Sariya 1/4 Bus Fare */
					if (dto.getTempBusCategoryCode().equals("SQ") && dto.getTempStatus().equals("ACTIVE")) {

						if (dto.getTempEquation() != null && !dto.getTempEquation().trim().equalsIgnoreCase("")) {
							for (int i = 0; i < tempCurrentBusFareDetailsList.size(); i++) {
								Double rate = null;

								if (dto.getTempEquation().contains("#")) {
									List<String> abbreviationList = validateExpression(dto.getTempEquation())
											.getAbbreviationList();
									Map<String, Object> mapContext = getMapContext(abbreviationList,
											tempCurrentBusFareDetailsList.get(i));
									if (mapContext != null && !mapContext.isEmpty()) {
										rate = expressionParser(dto.getTempEquation(), mapContext);
									}
								} else {
									rate = expressionParser(dto.getTempEquation(), new HashMap<String, Object>());
								}

								BigDecimal BUS_FARE = BigDecimal.valueOf(rate);
								BigDecimal NEW_RATE_WITH_DECIMAL = BUS_FARE.setScale(2, BigDecimal.ROUND_HALF_UP);
								BigDecimal ROUNDED_FEE = BUS_FARE.setScale(0, BigDecimal.ROUND_HALF_UP);

								BigDecimal DIFFRENT_FARE_FINAL = ROUNDED_FEE.subtract(NEW_RATE_WITH_DECIMAL);

								sisuSariyaQuarterList.add(
										sisuSariyaQuaterDTO(NEW_RATE_WITH_DECIMAL, ROUNDED_FEE, DIFFRENT_FARE_FINAL));
								tempSisuSariyaQuarterAdded = true;

							}
						}

					}

				}

				if (tempNormalFareAdd == true || tempLuxuaryFareAdded == true || tempSemiLuxuaryFareAdded == true
						|| tempSuperLuxuaryFareAdded == true || tempHighWayFareAdded == true
						|| tempSisuSariyaHalfAdded == true || tempSisuSariyaQuarterAdded == true) {

					if (tempAddAllFareRatesToViewList() == false) {
						RequestContext.getCurrentInstance().execute("PF('tempviewFareRate').show()");
						disabledTempSaveBusFareRate = false;
						disabledTempClearTwo = false;
					} else {
						setErrorMessage("No data found. Please try again!");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				}

			} else {
				setErrorMessage(validateMessage);
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Equation cannot be empty.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public boolean tempCheckExpressionIsEmpty() {
		for (BusFareDTO busFareDTO : tempRateDetailsList) {
			if ((busFareDTO.getTempEquation() == null || busFareDTO.getTempEquation().trim().equalsIgnoreCase(""))
					&& busFareDTO.getTempStatus().equals("ACTIVE")) {
				return true;
			}
		}
		return false;
	}

	public String tempCheckExpressionIsValid() {
		for (BusFareDTO busFareDTO : tempRateDetailsList) {

			if (busFareDTO.isTempFareCalculate() && busFareDTO.getTempStatus().equals("ACTIVE")) {
				BusFareEquationResponseDTO responseDTO = checkExpressionWithMockData(busFareDTO.getTempEquation());
				if (!responseDTO.getStatus().equals("S")) {
					return "Invalid equation for " + busFareDTO.getBusCategory() + ".";
				}
			}
		}
		return "S";
	}

	/* Add Temporary Fare Rates to List and View List */
	public boolean tempAddAllFareRatesToViewList() {

		boolean isViewListEmpty = false;

		tempViewBusFareRateList = new ArrayList<>();

		/* If List is empty, Add null values to each list */
		for (int x = 0; x < tempCurrentBusFareDetailsList.size(); x++) {

			if (tempNormalFareAdd == false) {
				normalRateList.add(noramlDTO(null, null, null));
			}
			if (tempSemiLuxuaryFareAdded == false) {
				semiLuxuaryRateList.add(SemiDTO(null, null, null));
			}
			if (tempLuxuaryFareAdded == false) {
				luxuaryList.add(luxuaryDTO(null, null, null));
			}
			if (tempSuperLuxuaryFareAdded == false) {
				superLuxuaryList.add(superDTO(null, null, null));
			}
			if (tempHighWayFareAdded == false) {
				highWayList.add(highwayDTO(null, null, null));
			}
			if (tempSisuSariyaHalfAdded == false) {
				sisuSariyaHalfList.add(sisuSariyaHalfDTO(null, null, null));
			}
			if (tempSisuSariyaQuarterAdded == false) {
				sisuSariyaQuarterList.add(sisuSariyaQuaterDTO(null, null, null));
			}
		}

		/* Add fare rates to main list */
		for (int i = 0; i < tempCurrentBusFareDetailsList.size(); i++) {

			if (tempCurrentBusFareDetailsList.get(i).getStageNo() != 0) {

				tempViewBusFeeDTO = new BusFareDTO(tempCurrentBusFareDetailsList.get(i).getStageNo(),
						normalRateList.get(i).getNormalCurrentFee(), normalRateList.get(i).getNormalNewFee(),
						normalRateList.get(i).getNormalRoundFee(), semiLuxuaryRateList.get(i).getSemiLuxuryCurrentFee(),
						semiLuxuaryRateList.get(i).getSemiLuxuryNewFee(),
						semiLuxuaryRateList.get(i).getSemiLuxuryRoundFee(), luxuaryList.get(i).getLuxuryCurrentFee(),
						luxuaryList.get(i).getLuxuryNewFee(), luxuaryList.get(i).getLuxuryRoundFee(),
						highWayList.get(i).getHighwayCurrentFee(), highWayList.get(i).getHighwayNewFee(),
						highWayList.get(i).getHighwayRoundFee(), superLuxuaryList.get(i).getSuperLuxuryCurrentFee(),
						superLuxuaryList.get(i).getSuperLuxuryNewFee(),
						superLuxuaryList.get(i).getSuperLuxuryRoundFee(),
						sisuSariyaHalfList.get(i).getSisuSariyaHalfNoramlFee(),
						sisuSariyaHalfList.get(i).getSisuSariyaHalfAdjestedFee(),
						sisuSariyaHalfList.get(i).getSisuSariyaHalfdiffrentFee(),
						sisuSariyaQuarterList.get(i).getSisuSariyaQuarterNoramlFee(),
						sisuSariyaQuarterList.get(i).getSisuSariyaQuarterAdjestedFee(),
						sisuSariyaQuarterList.get(i).getSisuSariyaQuarterdiffrentFee());

				tempViewBusFareRateList.add(tempViewBusFeeDTO);

				if (tempViewBusFareRateList.isEmpty()) {
					isViewListEmpty = true;
				} else {
					isViewListEmpty = false;
				}
			}

		}

		return isViewListEmpty;
	}

	public void tempCloseRatePopUp() {
		RequestContext.getCurrentInstance().execute("PF('tempviewFareRate').hide()");
	}

	/* Save Temporary Bus Fare */
	public void tempSaveBusFareRate() {

		/* Add Removed Stage to List [Stage 0] */

		BigDecimal zero = new BigDecimal(0);
		tempViewBusFeeDTO = new BusFareDTO(0, zero, zero, zero, zero, zero, zero, zero, zero, zero, zero, zero, zero,
				zero, zero, zero, zero, zero, zero, zero, zero, zero);

		this.tempViewBusFareRateList.add(0, tempViewBusFeeDTO);

		/* Generate Reference No */
		String fareReferenceNo = busFareService.generateTempFareReferenceNo();

		/* Save fare rates */
		if (busFareService.insertTempReferenceData(fareReferenceNo, tempCurrentBusFareDetailsList,
				sessionBackingBean.getLoginUser(), this.tempViewBusFareRateList, busFareDTO) == true) {

			if (tempNormalFareAdd == true) {
				busFareService.updateTempNormalBusRate(tempViewBusFareRateList, sessionBackingBean.getLoginUser(),
						fareReferenceNo);
			}
			if (tempSemiLuxuaryFareAdded == true) {

				busFareService.updateTempSemiLuxuryBusRate(tempViewBusFareRateList, sessionBackingBean.getLoginUser(),
						fareReferenceNo);
			}
			if (tempLuxuaryFareAdded == true) {

				busFareService.updateTempLuxuryBusRate(tempViewBusFareRateList, sessionBackingBean.getLoginUser(),
						fareReferenceNo);
			}
			if (tempSuperLuxuaryFareAdded == true) {

				busFareService.updateTempSuperLuxuryBusRate(tempViewBusFareRateList, sessionBackingBean.getLoginUser(),
						fareReferenceNo);
			}
			if (tempHighWayFareAdded == true) {

				busFareService.updateTempHighWayBusRate(tempViewBusFareRateList, sessionBackingBean.getLoginUser(),
						fareReferenceNo);
			}
			if (tempSisuSariyaHalfAdded == true) {

				busFareService.updateTemptHalfSisuSariyaBusRate(tempViewBusFareRateList,
						sessionBackingBean.getLoginUser(), fareReferenceNo);
			}
			if (tempSisuSariyaQuarterAdded == true) {

				busFareService.updateTempQuarterSisuSariyBusRate(tempViewBusFareRateList,
						sessionBackingBean.getLoginUser(), fareReferenceNo);
			}

			disabledTempSaveBusFareRate = true;
			busFareDTO.setTempFareReferenceNo(fareReferenceNo);
			setSuccessMessage("The bus fare saved successfully.");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {

			disabledTempSaveBusFareRate = true;
			setErrorMessage("Cannot save bus fare. Error occurred!");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void tempClearOne() {
		busFareDTO.setTempBusCategory(null);
		busFareDTO.setTempBusCategory(null);
		busFareDTO.setTempSelectBusCategorySinhala(null);
		busFareDTO.setTempSelectBusCategoryTamil(null);
		busFareDTO.setTempRate(0);
		busFareDTO.setTempStatus(null);
		disabledTwo = true;
		disabledTempNormal = true;

	}

	public void tempClearTwo() {

		busFareDTO.setTempBusCategory(null);
		busFareDTO = new BusFareDTO();
		busFareDTO.setTempSelectBusCategorySinhala(null);
		busFareDTO.setTempSelectBusCategoryTamil(null);
		busFareDTO.setTempRate(0);
		busFareDTO.setTempStatus(null);
		disabledTwo = true;
		disabledTempClearTwo = true;
		disabledTempSaveBusFareRate = true;
		disabledTempNormal = true;
		if (tempRateDetailsList != null && !tempRateDetailsList.isEmpty()) {
			for (BusFareDTO busFareDTO : tempRateDetailsList) {
				busFareDTO.setTempEquation(null);
				busFareDTO.setTempStatus("ACTIVE");
			}
		}

	}

	public static BigDecimal roundToNearest5(BigDecimal value) {
		BigDecimal roundedValue = value.divide(new BigDecimal("5"), 0, RoundingMode.HALF_UP)
				.multiply(new BigDecimal("5"));
		return roundedValue;
	}

	public static BigDecimal roundToNearest10(BigDecimal value) {
		BigDecimal roundedValue = value.divide(new BigDecimal("10"), 0, RoundingMode.HALF_UP)
				.multiply(new BigDecimal("10"));
		return roundedValue;
	}

	/* Temporary Fare Rate Method End Here */

	/* BusFare DTO Object Methods */
	public BusFareDTO noramlDTO(BigDecimal currentFee, BigDecimal newFee, BigDecimal roundUpFee) {
		BusFareDTO dto = new BusFareDTO();

		dto.setNormalCurrentFee(currentFee);
		dto.setNormalNewFee(newFee);
		dto.setNormalRoundFee(roundUpFee);

		return dto;
	}

	public BusFareDTO SemiDTO(BigDecimal currentFee, BigDecimal newFee, BigDecimal roundUpFee) {
		BusFareDTO dto = new BusFareDTO();

		dto.setSemiLuxuryCurrentFee(currentFee);
		dto.setSemiLuxuryNewFee(newFee);
		dto.setSemiLuxuryRoundFee(roundUpFee);

		return dto;
	}

	public BusFareDTO luxuaryDTO(BigDecimal currentFee, BigDecimal newFee, BigDecimal roundUpFee) {
		BusFareDTO dto = new BusFareDTO();

		dto.setLuxuryCurrentFee(currentFee);
		dto.setLuxuryNewFee(newFee);
		dto.setLuxuryRoundFee(roundUpFee);

		return dto;
	}

	public BusFareDTO superDTO(BigDecimal currentFee, BigDecimal newFee, BigDecimal roundUpFee) {
		BusFareDTO dto = new BusFareDTO();

		dto.setSuperLuxuryCurrentFee(currentFee);
		dto.setSuperLuxuryNewFee(newFee);
		dto.setSuperLuxuryRoundFee(roundUpFee);

		return dto;
	}

	public BusFareDTO highwayDTO(BigDecimal currentFee, BigDecimal newFee, BigDecimal roundUpFee) {
		BusFareDTO dto = new BusFareDTO();

		dto.setHighwayCurrentFee(currentFee);
		dto.setHighwayNewFee(newFee);
		dto.setHighwayRoundFee(roundUpFee);

		return dto;
	}

	public BusFareDTO sisuSariyaHalfDTO(BigDecimal normal, BigDecimal Adjuest, BigDecimal diffrent) {
		BusFareDTO dto = new BusFareDTO();

		dto.setSisuSariyaHalfNoramlFee(normal);
		dto.setSisuSariyaHalfAdjestedFee(Adjuest);
		dto.setSisuSariyaHalfdiffrentFee(diffrent);

		return dto;
	}

	public BusFareDTO sisuSariyaQuaterDTO(BigDecimal normal, BigDecimal Adjuest, BigDecimal diffrent) {
		BusFareDTO dto = new BusFareDTO();

		dto.setSisuSariyaQuarterNoramlFee(normal);
		dto.setSisuSariyaQuarterAdjestedFee(Adjuest);
		dto.setSisuSariyaQuarterdiffrentFee(diffrent);

		return dto;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public BusFareDTO getBusFareDTO() {
		return busFareDTO;
	}

	public void setBusFareDTO(BusFareDTO busFareDTO) {
		this.busFareDTO = busFareDTO;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public BusFareService getBusFareService() {
		return busFareService;
	}

	public void setBusFareService(BusFareService busFareService) {
		this.busFareService = busFareService;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public boolean isDisabledOne() {
		return disabledOne;
	}

	public void setDisabledOne(boolean disabledOne) {
		this.disabledOne = disabledOne;
	}

	public BusFareDTO getFareDTO() {
		return fareDTO;
	}

	public void setFareDTO(BusFareDTO fareDTO) {
		this.fareDTO = fareDTO;
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

	public List<BusFareDTO> getRateDetailsList() {
		return rateDetailsList;
	}

	public void setRateDetailsList(List<BusFareDTO> rateDetailsList) {
		this.rateDetailsList = rateDetailsList;
	}

	public List<BusFareDTO> getBusCategoryList() {
		return busCategoryList;
	}

	public void setBusCategoryList(List<BusFareDTO> busCategoryList) {
		this.busCategoryList = busCategoryList;
	}

	public BusFareDTO getAddBusCategory() {
		return addBusCategory;
	}

	public void setAddBusCategory(BusFareDTO addBusCategory) {
		this.addBusCategory = addBusCategory;
	}

	public BusFareDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(BusFareDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public List<BusFareDTO> getTempBusCategoryList() {
		return tempBusCategoryList;
	}

	public void setTempBusCategoryList(List<BusFareDTO> tempBusCategoryList) {
		this.tempBusCategoryList = tempBusCategoryList;
	}

	public List<BusFareDTO> getTempRateDetailsList() {
		return tempRateDetailsList;
	}

	public void setTempRateDetailsList(List<BusFareDTO> tempRateDetailsList) {
		this.tempRateDetailsList = tempRateDetailsList;
	}

	public BusFareDTO getTempViewSelect() {
		return tempViewSelect;
	}

	public void setTempViewSelect(BusFareDTO tempViewSelect) {
		this.tempViewSelect = tempViewSelect;
	}

	public boolean isDisableTab02() {
		return disableTab02;
	}

	public void setDisableTab02(boolean disableTab02) {
		this.disableTab02 = disableTab02;
	}

	public boolean isDisabledTwo() {
		return disabledTwo;
	}

	public void setDisabledTwo(boolean disabledTwo) {
		this.disabledTwo = disabledTwo;
	}

	public BusFareDTO getTempFareDTO() {
		return tempFareDTO;
	}

	public void setTempFareDTO(BusFareDTO tempFareDTO) {
		this.tempFareDTO = tempFareDTO;
	}

	public BusFareDTO getTempAddBusCategory() {
		return tempAddBusCategory;
	}

	public void setTempAddBusCategory(BusFareDTO tempAddBusCategory) {
		this.tempAddBusCategory = tempAddBusCategory;
	}

	public boolean isTempEditMode() {
		return tempEditMode;
	}

	public void setTempEditMode(boolean tempEditMode) {
		this.tempEditMode = tempEditMode;
	}

	public boolean isDisabledBusFareCalculateAction() {
		return disabledBusFareCalculateAction;
	}

	public void setDisabledBusFareCalculateAction(boolean disabledBusFareCalculateAction) {
		this.disabledBusFareCalculateAction = disabledBusFareCalculateAction;
	}

	public boolean isDisabledSaveBusFareRate() {
		return disabledSaveBusFareRate;
	}

	public void setDisabledSaveBusFareRate(boolean disabledSaveBusFareRate) {
		this.disabledSaveBusFareRate = disabledSaveBusFareRate;
	}

	public boolean isDisabledClearTwo() {
		return disabledClearTwo;
	}

	public void setDisabledClearTwo(boolean disabledClearTwo) {
		this.disabledClearTwo = disabledClearTwo;
	}

	public boolean isDisabledTempBusFareCalculateAction() {
		return disabledTempBusFareCalculateAction;
	}

	public void setDisabledTempBusFareCalculateAction(boolean disabledTempBusFareCalculateAction) {
		this.disabledTempBusFareCalculateAction = disabledTempBusFareCalculateAction;
	}

	public boolean isDisabledTempSaveBusFareRate() {
		return disabledTempSaveBusFareRate;
	}

	public void setDisabledTempSaveBusFareRate(boolean disabledTempSaveBusFareRate) {
		this.disabledTempSaveBusFareRate = disabledTempSaveBusFareRate;
	}

	public boolean isDisabledTempClearTwo() {
		return disabledTempClearTwo;
	}

	public void setDisabledTempClearTwo(boolean disabledTempClearTwo) {
		this.disabledTempClearTwo = disabledTempClearTwo;
	}

	public List<BusFareDTO> getViewBusFareRateList() {
		return viewBusFareRateList;
	}

	public void setViewBusFareRateList(List<BusFareDTO> viewBusFareRateList) {
		this.viewBusFareRateList = viewBusFareRateList;
	}

	public List<BusFareDTO> getCurrentBusFareDetailsList() {
		return currentBusFareDetailsList;
	}

	public void setCurrentBusFareDetailsList(List<BusFareDTO> currentBusFareDetailsList) {
		this.currentBusFareDetailsList = currentBusFareDetailsList;
	}

	public BusFareDTO getCurrentDetailsDTO() {
		return currentDetailsDTO;
	}

	public void setCurrentDetailsDTO(BusFareDTO currentDetailsDTO) {
		this.currentDetailsDTO = currentDetailsDTO;
	}

	public BusFareDTO getViewBusFeeDTO() {
		return viewBusFeeDTO;
	}

	public void setViewBusFeeDTO(BusFareDTO viewBusFeeDTO) {
		this.viewBusFeeDTO = viewBusFeeDTO;
	}

	public List<BusFareDTO> getNormalRateList() {
		return normalRateList;
	}

	public void setNormalRateList(List<BusFareDTO> normalRateList) {
		this.normalRateList = normalRateList;
	}

	public List<BusFareDTO> getSemiLuxuaryRateList() {
		return semiLuxuaryRateList;
	}

	public void setSemiLuxuaryRateList(List<BusFareDTO> semiLuxuaryRateList) {
		this.semiLuxuaryRateList = semiLuxuaryRateList;
	}

	public List<BusFareDTO> getLuxuaryList() {
		return luxuaryList;
	}

	public void setLuxuaryList(List<BusFareDTO> luxuaryList) {
		this.luxuaryList = luxuaryList;
	}

	public List<BusFareDTO> getSuperLuxuaryList() {
		return superLuxuaryList;
	}

	public void setSuperLuxuaryList(List<BusFareDTO> superLuxuaryList) {
		this.superLuxuaryList = superLuxuaryList;
	}

	public List<BusFareDTO> getHighWayList() {
		return highWayList;
	}

	public void setHighWayList(List<BusFareDTO> highWayList) {
		this.highWayList = highWayList;
	}

	public List<BusFareDTO> getSisuSariyaHalfList() {
		return sisuSariyaHalfList;
	}

	public void setSisuSariyaHalfList(List<BusFareDTO> sisuSariyaHalfList) {
		this.sisuSariyaHalfList = sisuSariyaHalfList;
	}

	public List<BusFareDTO> getSisuSariyaQuarterList() {
		return sisuSariyaQuarterList;
	}

	public void setSisuSariyaQuarterList(List<BusFareDTO> sisuSariyaQuarterList) {
		this.sisuSariyaQuarterList = sisuSariyaQuarterList;
	}

	public boolean isNormalFareAdd() {
		return normalFareAdd;
	}

	public void setNormalFareAdd(boolean normalFareAdd) {
		this.normalFareAdd = normalFareAdd;
	}

	public boolean isLuxuaryFareAdded() {
		return luxuaryFareAdded;
	}

	public void setLuxuaryFareAdded(boolean luxuaryFareAdded) {
		this.luxuaryFareAdded = luxuaryFareAdded;
	}

	public boolean isSemiLuxuaryFareAdded() {
		return semiLuxuaryFareAdded;
	}

	public void setSemiLuxuaryFareAdded(boolean semiLuxuaryFareAdded) {
		this.semiLuxuaryFareAdded = semiLuxuaryFareAdded;
	}

	public boolean isSuperLuxuaryFareAdded() {
		return superLuxuaryFareAdded;
	}

	public void setSuperLuxuaryFareAdded(boolean superLuxuaryFareAdded) {
		this.superLuxuaryFareAdded = superLuxuaryFareAdded;
	}

	public boolean isHighWayFareAdded() {
		return highWayFareAdded;
	}

	public void setHighWayFareAdded(boolean highWayFareAdded) {
		this.highWayFareAdded = highWayFareAdded;
	}

	public boolean isSisuSariyaHalfAdded() {
		return sisuSariyaHalfAdded;
	}

	public void setSisuSariyaHalfAdded(boolean sisuSariyaHalfAdded) {
		this.sisuSariyaHalfAdded = sisuSariyaHalfAdded;
	}

	public boolean isSisuSariyaQuarterAdded() {
		return sisuSariyaQuarterAdded;
	}

	public void setSisuSariyaQuarterAdded(boolean sisuSariyaQuarterAdded) {
		this.sisuSariyaQuarterAdded = sisuSariyaQuarterAdded;
	}

	public String getYearString() {
		return yearString;
	}

	public void setYearString(String yearString) {
		this.yearString = yearString;
	}

	public List<BusFareDTO> getTempCurrentBusFareDetailsList() {
		return tempCurrentBusFareDetailsList;
	}

	public void setTempCurrentBusFareDetailsList(List<BusFareDTO> tempCurrentBusFareDetailsList) {
		this.tempCurrentBusFareDetailsList = tempCurrentBusFareDetailsList;
	}

	public List<BusFareDTO> getTempViewBusFareRateList() {
		return tempViewBusFareRateList;
	}

	public void setTempViewBusFareRateList(List<BusFareDTO> tempViewBusFareRateList) {
		this.tempViewBusFareRateList = tempViewBusFareRateList;
	}

	public BusFareDTO getTempViewBusFeeDTO() {
		return tempViewBusFeeDTO;
	}

	public void setTempViewBusFeeDTO(BusFareDTO tempViewBusFeeDTO) {
		this.tempViewBusFeeDTO = tempViewBusFeeDTO;
	}

	public boolean isTempNormalFareAdd() {
		return tempNormalFareAdd;
	}

	public void setTempNormalFareAdd(boolean tempNormalFareAdd) {
		this.tempNormalFareAdd = tempNormalFareAdd;
	}

	public boolean isTempLuxuaryFareAdded() {
		return tempLuxuaryFareAdded;
	}

	public void setTempLuxuaryFareAdded(boolean tempLuxuaryFareAdded) {
		this.tempLuxuaryFareAdded = tempLuxuaryFareAdded;
	}

	public boolean isTempSemiLuxuaryFareAdded() {
		return tempSemiLuxuaryFareAdded;
	}

	public void setTempSemiLuxuaryFareAdded(boolean tempSemiLuxuaryFareAdded) {
		this.tempSemiLuxuaryFareAdded = tempSemiLuxuaryFareAdded;
	}

	public boolean isTempSuperLuxuaryFareAdded() {
		return tempSuperLuxuaryFareAdded;
	}

	public void setTempSuperLuxuaryFareAdded(boolean tempSuperLuxuaryFareAdded) {
		this.tempSuperLuxuaryFareAdded = tempSuperLuxuaryFareAdded;
	}

	public boolean isTempHighWayFareAdded() {
		return tempHighWayFareAdded;
	}

	public void setTempHighWayFareAdded(boolean tempHighWayFareAdded) {
		this.tempHighWayFareAdded = tempHighWayFareAdded;
	}

	public boolean isTempSisuSariyaHalfAdded() {
		return tempSisuSariyaHalfAdded;
	}

	public void setTempSisuSariyaHalfAdded(boolean tempSisuSariyaHalfAdded) {
		this.tempSisuSariyaHalfAdded = tempSisuSariyaHalfAdded;
	}

	public boolean isTempSisuSariyaQuarterAdded() {
		return tempSisuSariyaQuarterAdded;
	}

	public void setTempSisuSariyaQuarterAdded(boolean tempSisuSariyaQuarterAdded) {
		this.tempSisuSariyaQuarterAdded = tempSisuSariyaQuarterAdded;
	}

	public boolean isDisabledNoraml() {
		return disabledNoraml;
	}

	public void setDisabledNoraml(boolean disabledNoraml) {
		this.disabledNoraml = disabledNoraml;
	}

	public boolean isDisabledCategory() {
		return disabledCategory;
	}

	public void setDisabledCategory(boolean disabledCategory) {
		this.disabledCategory = disabledCategory;
	}

	public boolean isDisabledTempNormal() {
		return disabledTempNormal;
	}

	public void setDisabledTempNormal(boolean disabledTempNormal) {
		this.disabledTempNormal = disabledTempNormal;
	}

}
