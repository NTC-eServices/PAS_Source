package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.GenerateSurveyFormDTO;
import lk.informatics.ntc.model.service.GenerateSurveyFormService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "generateSurveyFormBackingBean")
@ViewScoped
public class GenerateSurveyFormBackingBean implements Serializable {

	private static final long serialVersionUID = -4514329923833377344L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private GenerateSurveyFormDTO generateSurveyFormDTO;

	private GenerateSurveyFormDTO LOVvaluesDTO, removeLOVvaluesDTO, indicatorsDTO, editIndicatorDTO,
			removeIndicatorsDTO;

	private GenerateSurveyFormService generateSurveyFormService;

	private List<GenerateSurveyFormDTO> surveyNoList = new ArrayList<GenerateSurveyFormDTO>();
	private List<GenerateSurveyFormDTO> formIDList = new ArrayList<GenerateSurveyFormDTO>();
	private List<GenerateSurveyFormDTO> fieldTypeList = new ArrayList<GenerateSurveyFormDTO>();
	private List<GenerateSurveyFormDTO> validationMethodList = new ArrayList<GenerateSurveyFormDTO>();
	private List<GenerateSurveyFormDTO> fieldDefinitionList = new ArrayList<GenerateSurveyFormDTO>();

	private List<GenerateSurveyFormDTO> indicators_list = new ArrayList<GenerateSurveyFormDTO>();
	private List<GenerateSurveyFormDTO> indicator_values_list = new ArrayList<GenerateSurveyFormDTO>();

	private List<GenerateSurveyFormDTO> indicatorsOrder_list = new ArrayList<GenerateSurveyFormDTO>();

	private Boolean disableLOV_Values_Btn = true, disableGenerateForm_Btn = true, disableSaveForm_Btn = false,
			disableUpdate_Btn = true, disableAdd_Btn = false, disableSurveyNo = false, renderUpdateForm_Btn = false, disableDelete_if_routeNo = true, disableFields_if_routeNo = false;
	private Boolean renderPanelTwo = false, renderPanelThree = false, renderPanelFour = false;
	private String errorMessage, successMessage, infoMessage, user;
	private int displayOrder = 1;

	@PostConstruct
	public void init() {
		generateSurveyFormDTO = new GenerateSurveyFormDTO();
		indicatorsDTO = new GenerateSurveyFormDTO();
		LOVvaluesDTO = new GenerateSurveyFormDTO();
		generateSurveyFormService = (GenerateSurveyFormService) SpringApplicationContex
				.getBean("generateSurveyFormService");
		surveyNoList = generateSurveyFormService.getApprovedSurveyNoList();
		formIDList = generateSurveyFormService.getFormIDList();
		fieldTypeList = generateSurveyFormService.getFieldTypeList();
		validationMethodList = generateSurveyFormService.getValidationMethodList();
		fieldDefinitionList = generateSurveyFormService.getFieldDefinitionList();
		user = sessionBackingBean.getLoginUser();

		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object objCallerbackBtn = fcontext.getExternalContext().getSessionMap().get("GENERATE_SURVEY_BACK");
		Object objCallersurveyNo = fcontext.getExternalContext().getSessionMap().get("SURVEY_NO");
		Object objCallersurveyType = fcontext.getExternalContext().getSessionMap().get("SURVEY_TYPE");
		Object objCallersurveyMethod = fcontext.getExternalContext().getSessionMap().get("SURVEY_METHOD");

		if (objCallerbackBtn != null) {
			String backBtn = (String) objCallerbackBtn;
			if (backBtn != null && !backBtn.isEmpty() && backBtn.equalsIgnoreCase("true")) {
				String surveyNo = (String) objCallersurveyNo;
				String surveyType = (String) objCallersurveyType;
				String surveyMethod = (String) objCallersurveyMethod;
				generateSurveyFormDTO.setSurveyNo(surveyNo);
				generateSurveyFormDTO.setSurveyType_des(surveyType);
				generateSurveyFormDTO.setSurveyMethod_des(surveyMethod);
				search();
				fcontext.getExternalContext().getSessionMap().put("GENERATE_SURVEY_BACK", "false");
				fcontext.getExternalContext().getSessionMap().put("SURVEY_NO", null);
				fcontext.getExternalContext().getSessionMap().put("SURVEY_TYPE", null);
				fcontext.getExternalContext().getSessionMap().put("SURVEY_METHOD", null);
			}
		}

	}

	public void onSurveyNoChange() {
		if (generateSurveyFormDTO.getSurveyNo() != null && !generateSurveyFormDTO.getSurveyNo().trim().isEmpty()) {
			for (GenerateSurveyFormDTO dto : surveyNoList) {
				if (generateSurveyFormDTO.getSurveyNo().equals(dto.getSurveyNo())) {
					generateSurveyFormDTO.setSurveyType(dto.getSurveyType());
					generateSurveyFormDTO.setSurveyMethod(dto.getSurveyType());
					generateSurveyFormDTO.setSurveyType_des(dto.getSurveyType_des());
					generateSurveyFormDTO.setSurveyMethod_des(dto.getSurveyMethod_des());
					break;
				}
			}
		} else {
			generateSurveyFormDTO = new GenerateSurveyFormDTO();
		}
	}

	@SuppressWarnings("deprecation")
	public void search() {
		if (generateSurveyFormDTO.getSurveyNo() != null && !generateSurveyFormDTO.getSurveyNo().trim().isEmpty()) {

			GenerateSurveyFormDTO indicatorsDTO = new GenerateSurveyFormDTO();
			indicatorsOrder_list = new ArrayList<GenerateSurveyFormDTO>();
			indicatorsDTO.setFieldName("Header");
			indicatorsDTO.setDisplayOrder(1);
			indicatorsDTO.setDisplayAfter("Header");
			indicatorsOrder_list.add(indicatorsDTO);

			GenerateSurveyFormDTO formDetails = generateSurveyFormService.getFormDetails(generateSurveyFormDTO);
			
			
			if (formDetails.getFormID() != null && !formDetails.getFormID().trim().isEmpty()) {
				generateSurveyFormDTO.setFormID(formDetails.getFormID());
				generateSurveyFormDTO.setCopyTemplateID(formDetails.getCopyTemplateID());
				generateSurveyFormDTO.setFormDescription(formDetails.getFormDescription());
				generateSurveyFormDTO.setHeaderLabel(formDetails.getHeaderLabel());
				generateSurveyFormDTO.setHeaderLabel_sinhala(formDetails.getHeaderLabel_sinhala());
				generateSurveyFormDTO.setHeaderLabel_tamil(formDetails.getHeaderLabel_tamil());
				
				indicators_list = generateSurveyFormService.getIndicatorsList(generateSurveyFormDTO);

				// assign values for indicator value list
				for (GenerateSurveyFormDTO dto : indicators_list) {
					List<GenerateSurveyFormDTO> data = new ArrayList<GenerateSurveyFormDTO>();
					if (dto.getFieldType().equals("FT01")) {
						data = generateSurveyFormService.getIndicatorsValueList(dto);
						dto.setIndicator_values_list(data);
					}
				}

				indicatorsOrder_list.addAll(indicators_list);
				disableSaveForm_Btn = true;
				disableGenerateForm_Btn = false;
				renderPanelThree = true;
				renderUpdateForm_Btn = true;
			} else {

				displayOrder = 1;
				
//				START - add route no
				GenerateSurveyFormDTO indicator_RouteNoDTO = new GenerateSurveyFormDTO();
				indicator_RouteNoDTO.setFieldName("Route No.");
				indicator_RouteNoDTO.setFieldType("FT02");
				indicator_RouteNoDTO.setFieldType_des("NUMERIC");
				indicator_RouteNoDTO.setFieldLength(10);
				indicator_RouteNoDTO.setMandatoryField(true);
				indicator_RouteNoDTO.setActive(true);
				indicator_RouteNoDTO.setDisplayOrder(displayOrder);
				indicator_RouteNoDTO.setDisplayAfter("Header");
				
				indicators_list.add(indicator_RouteNoDTO);
				indicatorsOrder_list.add(indicator_RouteNoDTO);
//				END 

				renderPanelThree = true;
				disableSaveForm_Btn = false;
				disableGenerateForm_Btn = true;
				renderUpdateForm_Btn = false;
			}
			renderPanelTwo = true;
			renderPanelFour = true;
			disableSurveyNo = true;
		} else {
			setErrorMessage("Survey No. should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clear() {
		generateSurveyFormDTO = new GenerateSurveyFormDTO();
		indicatorsDTO = new GenerateSurveyFormDTO();
		LOVvaluesDTO = new GenerateSurveyFormDTO();
		disableLOV_Values_Btn = true;
		renderUpdateForm_Btn = false;
		indicator_values_list = new ArrayList<GenerateSurveyFormDTO>();
		indicators_list = new ArrayList<GenerateSurveyFormDTO>();
		renderPanelTwo = false;
		renderPanelThree = false;
		renderPanelFour = false;
		disableSurveyNo = false;
		disableGenerateForm_Btn = true;
		disableFields_if_routeNo = false;
	}

	@SuppressWarnings("deprecation")
	public void check_FormID() {
		generateSurveyFormDTO.setFormID(generateSurveyFormDTO.getFormID().trim());
		boolean duplicate = generateSurveyFormService.check_FormID_duplicate(generateSurveyFormDTO.getFormID());

		if (duplicate) {
			generateSurveyFormDTO.setFormID("");
			setErrorMessage("Duplicate Form ID.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void onTempIDChange() {
		
		
		if (generateSurveyFormDTO.getCopyTemplateID() != null
				&& !generateSurveyFormDTO.getCopyTemplateID().trim().isEmpty() && !generateSurveyFormDTO.getCopyTemplateID().equals("")) {
			
			indicators_list = new ArrayList<GenerateSurveyFormDTO>();
			
			List<GenerateSurveyFormDTO> indicators_list_fr_temp_id = generateSurveyFormService
					.getIndicatorsListFromTempID(generateSurveyFormDTO.getCopyTemplateID());
			indicators_list.addAll(indicators_list_fr_temp_id);

			// assign values for indicator value list
			for (GenerateSurveyFormDTO dto : indicators_list) {
				List<GenerateSurveyFormDTO> data = new ArrayList<GenerateSurveyFormDTO>();
				if (dto.getFieldType().equals("FT01")) {
					data = generateSurveyFormService.getIndicatorsValueList(dto);
					dto.setIndicator_values_list(data);
				}
			}
			indicatorsOrder_list = new ArrayList<GenerateSurveyFormDTO>();
			GenerateSurveyFormDTO tempDTO = new GenerateSurveyFormDTO();
			tempDTO.setFieldName("Header");
			tempDTO.setDisplayOrder(1);
			tempDTO.setDisplayAfter("Header");
			indicatorsOrder_list.add(tempDTO);

			indicatorsOrder_list.addAll(indicators_list);
		} else {
			indicators_list = new ArrayList<GenerateSurveyFormDTO>();
			indicatorsOrder_list = new ArrayList<GenerateSurveyFormDTO>();
			
			GenerateSurveyFormDTO tempDTO = new GenerateSurveyFormDTO();
			tempDTO.setFieldName("Header");
			tempDTO.setDisplayOrder(1);
			tempDTO.setDisplayAfter("Header");
			indicatorsOrder_list.add(tempDTO);
			
			GenerateSurveyFormDTO indicator_RouteNoDTO = new GenerateSurveyFormDTO();
			indicator_RouteNoDTO.setFieldName("Route No.");
			indicator_RouteNoDTO.setFieldType("FT02");
			indicator_RouteNoDTO.setFieldType_des("NUMERIC");
			indicator_RouteNoDTO.setFieldLength(10);
			indicator_RouteNoDTO.setMandatoryField(true);
			indicator_RouteNoDTO.setActive(true);
			indicator_RouteNoDTO.setDisplayOrder(displayOrder);
			indicator_RouteNoDTO.setDisplayAfter("Header");
			
			indicators_list.add(indicator_RouteNoDTO);
			indicatorsOrder_list.add(indicator_RouteNoDTO);
		}
	}

	public void onFieldTypeChange() {
		if (indicatorsDTO.getFieldType() != null || !indicatorsDTO.getSurveyNo().trim().isEmpty()) {
			for (GenerateSurveyFormDTO dto : fieldTypeList) {
				if (indicatorsDTO.getFieldType().equals(dto.getFieldType())) {
					indicatorsDTO.setFieldType_des(dto.getFieldType_des());
					break;
				}
			}
			if (indicatorsDTO.getFieldType().equals("FT01")) {
				disableLOV_Values_Btn = false;
				indicatorsDTO.setFieldLength(0);
				indicatorsDTO.setFieldDefinition("FD01");
				indicatorsDTO.setFieldDefinition_des("LOV");
			} else {
				LOVvaluesDTO = new GenerateSurveyFormDTO();
				disableLOV_Values_Btn = true;
				indicatorsDTO.setFieldDefinition(null);
				indicatorsDTO.setFieldDefinition_des(null);
			}
		}
	}

	public void onValidationMethodChange() {
		if (indicatorsDTO.getValidationMethod() != null || !indicatorsDTO.getValidationMethod().trim().isEmpty()) {
			for (GenerateSurveyFormDTO dto : validationMethodList) {
				if (indicatorsDTO.getValidationMethod().equals(dto.getValidationMethod())) {
					indicatorsDTO.setValidationMethod_des(dto.getValidationMethod_des());
					break;
				}
			}
		}
	}

	public void onFieldDefinitionChange() {
		if (indicatorsDTO.getFieldDefinition() != null || !indicatorsDTO.getFieldDefinition().trim().isEmpty()) {
			for (GenerateSurveyFormDTO dto : fieldDefinitionList) {
				if (indicatorsDTO.getFieldDefinition().equals(dto.getFieldDefinition())) {
					indicatorsDTO.setFieldDefinition_des(dto.getFieldDefinition_des());
					break;
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void openLOV_values() {
		RequestContext.getCurrentInstance().update("LOV_values");
		RequestContext.getCurrentInstance().execute("PF('LOV_values_Dialog').show()");
	}

	@SuppressWarnings("deprecation")
	public void check_LOV_code() {
		boolean duplicate = generateSurveyFormService.check_LOV_code_duplicate(LOVvaluesDTO.getLOV_code());

		boolean alreadyInList = false;
		for (int i = 0; i < indicator_values_list.size(); i++) {
			if (indicator_values_list.get(i).getLOV_code().equals(LOVvaluesDTO.getLOV_code().trim())) {
				alreadyInList = true;
				break;
			}
		}

		if (duplicate || alreadyInList) {
			LOVvaluesDTO.setLOV_code("");
			setErrorMessage("Duplicate Code.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (LOVvaluesDTO.getLOV_code().length() > 5) {
			setErrorMessage("Code should be less than 6 characters.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	@SuppressWarnings("deprecation")
	public void addLOV_values() {
		if (LOVvaluesDTO.getLOV_code() != null && !LOVvaluesDTO.getLOV_code().trim().isEmpty()) {
			if (LOVvaluesDTO.getLOV_description() != null && !LOVvaluesDTO.getLOV_description().trim().isEmpty()) {
				if (LOVvaluesDTO.getLOV_description_sinhala() != null
						&& !LOVvaluesDTO.getLOV_description_sinhala().trim().isEmpty()) {
					if (LOVvaluesDTO.getLOV_description_tamil() != null
							&& !LOVvaluesDTO.getLOV_description_tamil().trim().isEmpty()) {
						
						
						indicator_values_list.add(LOVvaluesDTO);
						
						LOVvaluesDTO = new GenerateSurveyFormDTO();
					} else {
						setErrorMessage("Description (Tamil) should be entered.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Description (Sinhala) should be entered.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Description (English) should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Code should be entered.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void removeLOV_values() {
		if (removeLOVvaluesDTO != null) {
			indicator_values_list.remove(removeLOVvaluesDTO);
		}
	}

	@SuppressWarnings("deprecation")
	public void cancelLOV_values() {
		LOVvaluesDTO = new GenerateSurveyFormDTO();
		RequestContext.getCurrentInstance().execute("PF('LOV_values_Dialog').hide()");
	}

	@SuppressWarnings("deprecation")
	public void add() {
		if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().trim().isEmpty()) {
			if (generateSurveyFormDTO.getFormDescription() != null
					&& !generateSurveyFormDTO.getFormDescription().trim().isEmpty()) {
				if (generateSurveyFormDTO.getHeaderLabel() != null
						&& !generateSurveyFormDTO.getHeaderLabel().trim().isEmpty()) {
					if (indicatorsDTO.getFieldName() != null && !indicatorsDTO.getFieldName().trim().isEmpty()) {
						if (indicatorsDTO.getFieldType() != null && !indicatorsDTO.getFieldType().trim().isEmpty()) {
							
							if (indicatorsDTO.getDisplayAfter() != null
									&& !indicatorsDTO.getDisplayAfter().trim().isEmpty()) {
								if (indicatorsDTO.getFieldLength() > 0 || !disableLOV_Values_Btn) {
									if ((indicatorsDTO.getFieldType().equals("FT01")
											&& indicator_values_list.size() > 0)
											|| (!indicatorsDTO.getFieldType().equals("FT01"))) {

										if (!indicatorsDTO.getFieldType().equals("FT01")) {
											indicator_values_list = new ArrayList<GenerateSurveyFormDTO>();
										}

										indicatorsDTO.setIndicator_values_list(indicator_values_list);

										int size = indicators_list.size();
										displayOrder = size + 1;
										indicatorsDTO.setDisplayOrder(displayOrder);
										//START - method for re-order
										Boolean reOrder = false;
										for (GenerateSurveyFormDTO dto : indicators_list) {
											if (dto.getDisplayAfter().equals(indicatorsDTO.getDisplayAfter())) {
												reOrder = true;
												if (reOrder) {
													dto.setDisplayAfter(indicatorsDTO.getFieldName());

													indicatorsDTO.setDisplayOrder(dto.getDisplayOrder());

												}
											}
											if (reOrder) {
												dto.setDisplayOrder(dto.getDisplayOrder() + 1);
											}
										}
										// END
										
										indicators_list.add(indicatorsDTO);
										indicatorsOrder_list.add(indicatorsDTO);
										
										indicatorsDTO = new GenerateSurveyFormDTO();
										disableLOV_Values_Btn = true;
										indicator_values_list = new ArrayList<GenerateSurveyFormDTO>();
									} else {
										setErrorMessage("At least one value for LOV should be added.");
										RequestContext.getCurrentInstance().update("errorMSG");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}

								} else {
									setErrorMessage("Field Length should be entered.");
									RequestContext.getCurrentInstance().update("errorMSG");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
							} else {
								setErrorMessage("Display After field should be selected.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
							
						} else {
							setErrorMessage("Field Type should be selected.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					} else {
						setErrorMessage("Field Name should be entered.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Header Label should be entered.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Form Description should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Form ID should be entered.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	@SuppressWarnings("deprecation")
	public void update() {
		if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().trim().isEmpty()) {
			if (generateSurveyFormDTO.getFormDescription() != null
					&& !generateSurveyFormDTO.getFormDescription().trim().isEmpty()) {
				if (generateSurveyFormDTO.getHeaderLabel() != null
						&& !generateSurveyFormDTO.getHeaderLabel().trim().isEmpty()) {
					if (indicatorsDTO.getFieldName() != null && !indicatorsDTO.getFieldName().trim().isEmpty()) {
						if (indicatorsDTO.getFieldType() != null && !indicatorsDTO.getFieldType().trim().isEmpty()) {
							
							if (indicatorsDTO.getDisplayAfter() != null
									&& !indicatorsDTO.getDisplayAfter().trim().isEmpty()) {
								
								if (indicatorsDTO.getFieldLength() > 0 || !disableLOV_Values_Btn) {
									
									if ((indicatorsDTO.getFieldType().equals("FT01")
											&& indicator_values_list.size() > 0)
											|| (!indicatorsDTO.getFieldType().equals("FT01"))) {
										
										if (editIndicatorDTO != null) {
											
											if (!editIndicatorDTO.getFieldName()
													.equals(indicatorsDTO.getDisplayAfter())) {
												indicators_list.remove(editIndicatorDTO);
												int index = 0;
												
												
												
												for (GenerateSurveyFormDTO dto : indicators_list) {
													if (editIndicatorDTO.getDisplayAfter()
															.equals(indicatorsDTO.getDisplayAfter())) {
														index = editIndicatorDTO.getDisplayOrder() - 1;
														break;
													} else {
														if (!dto.getDisplayAfter()
																.equals(indicatorsDTO.getDisplayAfter())) {
															index++;
														} else {
															break;
														}
													}
												}

												if (!indicatorsDTO.getFieldType().equals("FT01")) {
													indicator_values_list = new ArrayList<GenerateSurveyFormDTO>();
												}
												// inserting the DTO(indicator) to the arrayList
												indicatorsDTO.setIndicator_values_list(indicator_values_list);
												indicators_list.add(index, indicatorsDTO);
												indicatorsOrder_list.add(indicatorsDTO);
												// START - Add Display After column
												Boolean isFirst = true;
												String tempFieldName = null;
												for (GenerateSurveyFormDTO dto : indicators_list) {
													if (isFirst) {
														dto.setDisplayAfter("Header");
														isFirst = false;
														tempFieldName = dto.getFieldName();
													} else {
														dto.setDisplayAfter(tempFieldName);
														tempFieldName = dto.getFieldName();
													}
												}
												// END

												// START - Display Order
												int displayOrder = 1;
												for (GenerateSurveyFormDTO dto : indicators_list) {
													dto.setDisplayOrder(displayOrder++);
												}
												// END

												// START - remove previous field name from drop down
												for (GenerateSurveyFormDTO dto : indicatorsOrder_list) {
													if (dto.getFieldName().equals(editIndicatorDTO.getFieldName())) {
														indicatorsOrder_list.remove(dto);
														break;
													}
												}
												// END

												// START - change Display After field according to the updated field
												// name
												for (GenerateSurveyFormDTO dto : indicators_list) {
													if (dto.getDisplayAfter().equals(editIndicatorDTO.getFieldName())) {
														dto.setDisplayAfter(indicatorsDTO.getFieldName());
														break;
													}
												}
												// END

												indicator_values_list = new ArrayList<GenerateSurveyFormDTO>();
												editIndicatorDTO = new GenerateSurveyFormDTO();
												indicatorsDTO = new GenerateSurveyFormDTO();
												disableLOV_Values_Btn = true;
												disableAdd_Btn = false;
												disableUpdate_Btn = true;
												disableFields_if_routeNo = false;
											} else {
												indicatorsDTO.setDisplayAfter(null);
												setErrorMessage("Invalid input for Display After.");
												RequestContext.getCurrentInstance().update("errorMSG");
												RequestContext.getCurrentInstance()
														.execute("PF('errorMessage').show()");
											}
										}
									} else {
										setErrorMessage("At least one value for LOV should be added.");
										RequestContext.getCurrentInstance().update("errorMSG");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
								} else {
									setErrorMessage("Field Length should be entered.");
									RequestContext.getCurrentInstance().update("errorMSG");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
							} else {
								setErrorMessage("Display After field should be selected.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
							
						} else {
							setErrorMessage("Field Type should be selected.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					} else {
						setErrorMessage("Field Name should be entered.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Header Label should be entered.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Form Description should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Form ID should be entered.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}
	
	public void printListForTest(List<GenerateSurveyFormDTO> indicators_list) {
		
	}

	public void clearIndicator() {
		editIndicatorDTO = new GenerateSurveyFormDTO();
		indicatorsDTO = new GenerateSurveyFormDTO();
		if (indicator_values_list != null && indicator_values_list.size() > 0) {
			indicator_values_list = new ArrayList<GenerateSurveyFormDTO>();
		}
		disableLOV_Values_Btn = true;
		disableAdd_Btn = false;
		disableUpdate_Btn = true;
		disableFields_if_routeNo = false;
	}

	public void editIndicators(GenerateSurveyFormDTO dataDTO) {
		setEditIndicatorDTO(dataDTO);
		indicatorsDTO.setSeqNo(dataDTO.getSeqNo());
		indicatorsDTO.setFieldName(dataDTO.getFieldName());
		indicatorsDTO.setFieldName_sinhala(dataDTO.getFieldName_sinhala());
		indicatorsDTO.setFieldName_tamil(dataDTO.getFieldName_tamil());
		indicatorsDTO.setDisplayAfter(dataDTO.getDisplayAfter());
		indicatorsDTO.setFieldType(dataDTO.getFieldType());
		indicatorsDTO.setFieldType_des(dataDTO.getFieldType_des());
		indicatorsDTO.setValidationMethod(dataDTO.getValidationMethod());
		indicatorsDTO.setValidationMethod_des(dataDTO.getValidationMethod_des());
		indicatorsDTO.setFieldDefinition(dataDTO.getFieldDefinition());
		indicatorsDTO.setFieldDefinition_des(dataDTO.getFieldDefinition_des());
		indicatorsDTO.setFieldLength(dataDTO.getFieldLength());
		indicatorsDTO.setActive(dataDTO.getActive());
		indicatorsDTO.setMandatoryField(dataDTO.getMandatoryField());
		indicatorsDTO.setIndicator_values_list(dataDTO.getIndicator_values_list());
		indicatorsDTO.setDisplayOrder(dataDTO.getDisplayOrder());

		if (indicatorsDTO.getIndicator_values_list() != null) {
			indicator_values_list = indicatorsDTO.getIndicator_values_list();
		} else {
			indicator_values_list = new ArrayList<GenerateSurveyFormDTO>();
		}

		disableAdd_Btn = true;
		disableUpdate_Btn = false;
		if (indicatorsDTO.getFieldType().equals("FT01")) {
			disableLOV_Values_Btn = false;
		} else {
			disableLOV_Values_Btn = true;
		}
		if(indicatorsDTO.getFieldName().equals("Route No.")) {
			disableFields_if_routeNo = true;
		} else {
			disableFields_if_routeNo = false;
		}

	}

	@SuppressWarnings("deprecation")
	public void removeIndicators() {
		if (removeIndicatorsDTO != null) {
			if(!removeIndicatorsDTO.getFieldName().equals("Route No.")) {
				indicatorsDTO = new GenerateSurveyFormDTO();
				disableLOV_Values_Btn = true;
				indicator_values_list = new ArrayList<GenerateSurveyFormDTO>();
				String displayAfterStr = null;

				// remove field from Grid and reorder
				Boolean reOrder = false;
				Boolean reOrderDisplayString = false;
				for (GenerateSurveyFormDTO dto : indicators_list) {
					if (reOrderDisplayString) {
						dto.setDisplayAfter(displayAfterStr);
						reOrderDisplayString = false;
					}
					if (dto.getDisplayOrder() == removeIndicatorsDTO.getDisplayOrder()) {
						reOrder = true;
						displayOrder = removeIndicatorsDTO.getDisplayOrder() - 1;
						reOrderDisplayString = true;
						displayAfterStr = dto.getDisplayAfter();
					}
					if (reOrder) {
						dto.setDisplayOrder(displayOrder);
						displayOrder++;
					}

				}

				
				// remove field from Display After drop down
				for (GenerateSurveyFormDTO dto : indicatorsOrder_list) {
					if (dto.getFieldName().equals(removeIndicatorsDTO.getFieldName())) {
						indicatorsOrder_list.remove(dto);
						break;
					}
				}
				indicators_list.remove(removeIndicatorsDTO);
			} else {
				setInfoMessage("Route No. is mandatory for Survey Form.");
				RequestContext.getCurrentInstance().update("infoMSG");
				RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
			}
			
		}
	}

	@SuppressWarnings("deprecation")
	public void save() {
		if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().trim().isEmpty()) {
			if (generateSurveyFormDTO.getFormDescription() != null
					&& !generateSurveyFormDTO.getFormDescription().trim().isEmpty()) {
				if (!generateSurveyFormDTO.getFormID().equals(generateSurveyFormDTO.getCopyTemplateID())) {
					if (generateSurveyFormDTO.getHeaderLabel() != null
							&& !generateSurveyFormDTO.getHeaderLabel().trim().isEmpty()) {
						if (indicators_list.size() > 0) {
							boolean success = generateSurveyFormService.saveForm(generateSurveyFormDTO, indicators_list,
									user);
							indicatorsDTO = new GenerateSurveyFormDTO();
							LOVvaluesDTO = new GenerateSurveyFormDTO();
							if (success) {
								// update task details & history
								generateSurveyFormService.updateSurveyTaskDetails(generateSurveyFormDTO.getSurveyNo(),
										"SU004", "O");
								// disableSaveForm_Btn = true;
								disableGenerateForm_Btn = false;
								renderUpdateForm_Btn = true;
								
								formIDList = generateSurveyFormService.getFormIDList();
								setSuccessMessage("Saved successfully.");
								RequestContext.getCurrentInstance().update("successMSG");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							} else {
								setErrorMessage("Save unsuccessful.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
						} else {
							setErrorMessage("At least one field should be added to create a Survey Form.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					} else {
						setErrorMessage("Header Label should be entered.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Invalid input for Copy Template ID.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Form Description should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Form ID should be entered.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	@SuppressWarnings("deprecation")
	public void updateForm() {
		String taskCode = generateSurveyFormService.getTaskCode(generateSurveyFormDTO.getSurveyNo());
		if (taskCode.equals("SU004")) {
			if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().trim().isEmpty()) {
				if (generateSurveyFormDTO.getFormDescription() != null
						&& !generateSurveyFormDTO.getFormDescription().trim().isEmpty()) {
					if (!generateSurveyFormDTO.getFormID().equals(generateSurveyFormDTO.getCopyTemplateID())) {
						if (indicators_list.size() > 0) {
							boolean success = generateSurveyFormService.updateForm(generateSurveyFormDTO,
									indicators_list, user);
							if (success) {
								// get updated values from database
								indicators_list = generateSurveyFormService.getIndicatorsList(generateSurveyFormDTO);

								// assign values for indicator value list
								for (GenerateSurveyFormDTO dto : indicators_list) {
									List<GenerateSurveyFormDTO> data = new ArrayList<GenerateSurveyFormDTO>();
									if (dto.getFieldType().equals("FT01")) {
										data = generateSurveyFormService.getIndicatorsValueList(dto);
										dto.setIndicator_values_list(data);
									}
								}
								
								clearIndicator();								
								setSuccessMessage("Updated successfully.");
								RequestContext.getCurrentInstance().update("successMSG");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							} else {
								clearIndicator();
								setErrorMessage("Update unsuccessful.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
							indicatorsDTO = new GenerateSurveyFormDTO();
							LOVvaluesDTO = new GenerateSurveyFormDTO();
						} else {
							setErrorMessage("At least one field should be added to create a Survey Form.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					} else {
						setErrorMessage("Invalid input for Copy Template ID.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Form Description should be entered.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Form ID should be entered.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setInfoMessage("Cannot update. Generate Survey task was already completed.");
			RequestContext.getCurrentInstance().update("infoMSG");
			RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
			search();
		}

	}

	public void cancel() {
		generateSurveyFormDTO = new GenerateSurveyFormDTO();
		indicatorsDTO = new GenerateSurveyFormDTO();
		LOVvaluesDTO = new GenerateSurveyFormDTO();
		indicator_values_list = new ArrayList<GenerateSurveyFormDTO>();
		indicators_list = new ArrayList<GenerateSurveyFormDTO>();
		disableLOV_Values_Btn = true;
		disableGenerateForm_Btn = true;
		disableSaveForm_Btn = false;
		renderPanelTwo = false;
		renderPanelThree = false;
		renderPanelFour = false;
		disableSurveyNo = false;
		renderUpdateForm_Btn = false;
		disableFields_if_routeNo = false;
	}

	public void generateForm() {
		// call generate form method
		try {
			// update task details & history
			generateSurveyFormService.updateSurveyTaskDetails(generateSurveyFormDTO.getSurveyNo(), "SU004", "C");
			// refresh survey no. drop-down
			surveyNoList = generateSurveyFormService.getApprovedSurveyNoList();

			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("GENERATE_SURVEY", "true");
			fcontext.getExternalContext().getSessionMap().put("SURVEY_FORM_ID", generateSurveyFormDTO.getFormID());
			fcontext.getExternalContext().getSessionMap().put("SURVEY_NO", generateSurveyFormDTO.getSurveyNo());
			fcontext.getExternalContext().getSessionMap().put("SURVEY_TYPE", generateSurveyFormDTO.getSurveyType_des());
			fcontext.getExternalContext().getSessionMap().put("SURVEY_METHOD",
					generateSurveyFormDTO.getSurveyMethod_des());

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/surveyManagement/generateDraftSurvey.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GenerateSurveyFormDTO getGenerateSurveyFormDTO() {
		return generateSurveyFormDTO;
	}

	public void setGenerateSurveyFormDTO(GenerateSurveyFormDTO generateSurveyFormDTO) {
		this.generateSurveyFormDTO = generateSurveyFormDTO;
	}

	public GenerateSurveyFormService getGenerateSurveyFormService() {
		return generateSurveyFormService;
	}

	public void setGenerateSurveyFormService(GenerateSurveyFormService generateSurveyFormService) {
		this.generateSurveyFormService = generateSurveyFormService;
	}

	public Boolean getDisableLOV_Values_Btn() {
		return disableLOV_Values_Btn;
	}

	public void setDisableLOV_Values_Btn(Boolean disableLOV_Values_Btn) {
		this.disableLOV_Values_Btn = disableLOV_Values_Btn;
	}

	public List<GenerateSurveyFormDTO> getSurveyNoList() {
		return surveyNoList;
	}

	public void setSurveyNoList(List<GenerateSurveyFormDTO> surveyNoList) {
		this.surveyNoList = surveyNoList;
	}

	public List<GenerateSurveyFormDTO> getFormIDList() {
		return formIDList;
	}

	public void setFormIDList(List<GenerateSurveyFormDTO> formIDList) {
		this.formIDList = formIDList;
	}

	public List<GenerateSurveyFormDTO> getFieldTypeList() {
		return fieldTypeList;
	}

	public void setFieldTypeList(List<GenerateSurveyFormDTO> fieldTypeList) {
		this.fieldTypeList = fieldTypeList;
	}

	public List<GenerateSurveyFormDTO> getValidationMethodList() {
		return validationMethodList;
	}

	public void setValidationMethodList(List<GenerateSurveyFormDTO> validationMethodList) {
		this.validationMethodList = validationMethodList;
	}

	public Boolean getDisableGenerateForm_Btn() {
		return disableGenerateForm_Btn;
	}

	public void setDisableGenerateForm_Btn(Boolean disableGenerateForm_Btn) {
		this.disableGenerateForm_Btn = disableGenerateForm_Btn;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public GenerateSurveyFormDTO getRemoveLOVvaluesDTO() {
		return removeLOVvaluesDTO;
	}

	public void setRemoveLOVvaluesDTO(GenerateSurveyFormDTO removeLOVvaluesDTO) {
		this.removeLOVvaluesDTO = removeLOVvaluesDTO;
	}

	public GenerateSurveyFormDTO getLOVvaluesDTO() {
		return LOVvaluesDTO;
	}

	public void setLOVvaluesDTO(GenerateSurveyFormDTO lOVvaluesDTO) {
		LOVvaluesDTO = lOVvaluesDTO;
	}

	public List<GenerateSurveyFormDTO> getIndicators_list() {
		return indicators_list;
	}

	public void setIndicators_list(List<GenerateSurveyFormDTO> indicators_list) {
		this.indicators_list = indicators_list;
	}

	public List<GenerateSurveyFormDTO> getIndicator_values_list() {
		return indicator_values_list;
	}

	public void setIndicator_values_list(List<GenerateSurveyFormDTO> indicator_values_list) {
		this.indicator_values_list = indicator_values_list;
	}

	public GenerateSurveyFormDTO getIndicatorsDTO() {
		return indicatorsDTO;
	}

	public void setIndicatorsDTO(GenerateSurveyFormDTO indicatorsDTO) {
		this.indicatorsDTO = indicatorsDTO;
	}

	public GenerateSurveyFormDTO getRemoveIndicatorsDTO() {
		return removeIndicatorsDTO;
	}

	public void setRemoveIndicatorsDTO(GenerateSurveyFormDTO removeIndicatorsDTO) {
		this.removeIndicatorsDTO = removeIndicatorsDTO;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Boolean getRenderPanelTwo() {
		return renderPanelTwo;
	}

	public void setRenderPanelTwo(Boolean renderPanelTwo) {
		this.renderPanelTwo = renderPanelTwo;
	}

	public Boolean getRenderPanelThree() {
		return renderPanelThree;
	}

	public void setRenderPanelThree(Boolean renderPanelThree) {
		this.renderPanelThree = renderPanelThree;
	}

	public Boolean getRenderPanelFour() {
		return renderPanelFour;
	}

	public void setRenderPanelFour(Boolean renderPanelFour) {
		this.renderPanelFour = renderPanelFour;
	}

	public Boolean getDisableSaveForm_Btn() {
		return disableSaveForm_Btn;
	}

	public void setDisableSaveForm_Btn(Boolean disableSaveForm_Btn) {
		this.disableSaveForm_Btn = disableSaveForm_Btn;
	}

	public GenerateSurveyFormDTO getEditIndicatorDTO() {
		return editIndicatorDTO;
	}

	public void setEditIndicatorDTO(GenerateSurveyFormDTO editIndicatorDTO) {
		this.editIndicatorDTO = editIndicatorDTO;
	}

	public Boolean getDisableUpdate_Btn() {
		return disableUpdate_Btn;
	}

	public void setDisableUpdate_Btn(Boolean disableUpdate_Btn) {
		this.disableUpdate_Btn = disableUpdate_Btn;
	}

	public Boolean getDisableAdd_Btn() {
		return disableAdd_Btn;
	}

	public void setDisableAdd_Btn(Boolean disableAdd_Btn) {
		this.disableAdd_Btn = disableAdd_Btn;
	}

	public Boolean getDisableSurveyNo() {
		return disableSurveyNo;
	}

	public void setDisableSurveyNo(Boolean disableSurveyNo) {
		this.disableSurveyNo = disableSurveyNo;
	}

	public List<GenerateSurveyFormDTO> getFieldDefinitionList() {
		return fieldDefinitionList;
	}

	public void setFieldDefinitionList(List<GenerateSurveyFormDTO> fieldDefinitionList) {
		this.fieldDefinitionList = fieldDefinitionList;
	}

	public List<GenerateSurveyFormDTO> getIndicatorsOrder_list() {
		return indicatorsOrder_list;
	}

	public void setIndicatorsOrder_list(List<GenerateSurveyFormDTO> indicatorsOrder_list) {
		this.indicatorsOrder_list = indicatorsOrder_list;
	}

	public Boolean getRenderUpdateForm_Btn() {
		return renderUpdateForm_Btn;
	}

	public void setRenderUpdateForm_Btn(Boolean renderUpdateForm_Btn) {
		this.renderUpdateForm_Btn = renderUpdateForm_Btn;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getDisableDelete_if_routeNo() {
		return disableDelete_if_routeNo;
	}

	public void setDisableDelete_if_routeNo(Boolean disableDelete_if_routeNo) {
		this.disableDelete_if_routeNo = disableDelete_if_routeNo;
	}

	public Boolean getDisableFields_if_routeNo() {
		return disableFields_if_routeNo;
	}

	public void setDisableFields_if_routeNo(Boolean disableFields_if_routeNo) {
		this.disableFields_if_routeNo = disableFields_if_routeNo;
	}
	 
}
