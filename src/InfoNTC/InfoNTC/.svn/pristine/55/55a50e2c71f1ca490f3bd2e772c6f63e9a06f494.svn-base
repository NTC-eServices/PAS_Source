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
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.model.service.GenerateSurveyFormService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "gsGenerateSurveyFormBean")
@ViewScoped
public class GsGenerateSurveyFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private GenerateSurveyFormDTO generateSurveyFormDTO;
	private GenerateSurveyFormDTO indicatorsDTO;
	private GenerateSurveyFormDTO LOVvaluesDTO;
	private GenerateSurveyFormDTO removeLOVvaluesDTO;
	private GenerateSurveyFormDTO editIndicatorDTO;
	private GenerateSurveyFormDTO removeIndicatorsDTO;

	private List<GenerateSurveyFormDTO> drpdSurveyNoList;
	private List<GenerateSurveyFormDTO> drpdFormIdList;
	private List<GenerateSurveyFormDTO> drpdValidationMethodList;
	private List<GenerateSurveyFormDTO> drpdIndicatorOrderList;
	private List<GenerateSurveyFormDTO> drpdFieldDefinitionList;
	private List<GenerateSurveyFormDTO> drpdFieldTypeList;

	private List<GenerateSurveyFormDTO> tblIndicatorsList;
	private List<GenerateSurveyFormDTO> tblLovValuesList;

	private String errorMessage, successMessage, infoMessage, user;
	private int displayOrder = 1;

	private boolean pnlContent;
	private boolean disableTxtFieldLength, disabledTxtFormId;
	private boolean disableDrpdFieldDefiniton, disableDrpdTemplateId;
	private boolean disableBtnAdd, disableBtnUpdate, disableBtnLovValues, disableBtnGenerateForm, disableBtnUpdateForm;
	private boolean renderBtnUpdate, renderBtnUpdateForm;

	private GamiSariyaService gamiSariyaService;
	private GenerateSurveyFormService generateSurveyFormService;
	private CommonService commonService;

	@PostConstruct
	public void init() {

		generateSurveyFormDTO = new GenerateSurveyFormDTO();
		indicatorsDTO = new GenerateSurveyFormDTO();
		LOVvaluesDTO = new GenerateSurveyFormDTO();
		removeLOVvaluesDTO = new GenerateSurveyFormDTO();
		editIndicatorDTO = new GenerateSurveyFormDTO();
		removeIndicatorsDTO = new GenerateSurveyFormDTO();

		drpdSurveyNoList = new ArrayList<GenerateSurveyFormDTO>();
		drpdFormIdList = new ArrayList<GenerateSurveyFormDTO>();
		drpdValidationMethodList = new ArrayList<GenerateSurveyFormDTO>();
		drpdIndicatorOrderList = new ArrayList<GenerateSurveyFormDTO>();
		drpdFieldDefinitionList = new ArrayList<GenerateSurveyFormDTO>();
		drpdFieldTypeList = new ArrayList<GenerateSurveyFormDTO>();
		tblIndicatorsList = new ArrayList<GenerateSurveyFormDTO>();
		tblLovValuesList = new ArrayList<GenerateSurveyFormDTO>();

		pnlContent = false;

		loadValues();
	}

	private void loadValues() {

		generateSurveyFormService = (GenerateSurveyFormService) SpringApplicationContex
				.getBean("generateSurveyFormService");
		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		drpdSurveyNoList = gamiSariyaService.drpdSurveyNoListForGenerateSurveyForm("SU003", "O", "A");

		drpdFieldTypeList = generateSurveyFormService.getFieldTypeList();
		drpdValidationMethodList = generateSurveyFormService.getValidationMethodList();
		drpdFieldDefinitionList = generateSurveyFormService.getFieldDefinitionList();

		drpdFormIdList = gamiSariyaService.getGamiTemplateIdFormList();

		FacesContext fcontext = FacesContext.getCurrentInstance();

		Object objCallerbackBtn = fcontext.getExternalContext().getSessionMap().get("GAMI_GENERATE_SURVEY_BACK");
		Object objCallersurveyNo = fcontext.getExternalContext().getSessionMap().get("GAMI_SURVEY_NO");
		Object objCallersurveyType = fcontext.getExternalContext().getSessionMap().get("GAMI_SURVEY_TYPE");
		Object objCallersurveyMethod = fcontext.getExternalContext().getSessionMap().get("GAMI_SURVEY_METHOD");

		if (objCallerbackBtn != null) {
			pnlContent = true;
			String backBtn = (String) objCallerbackBtn;
			if (backBtn != null && !backBtn.isEmpty() && backBtn.equalsIgnoreCase("true")) {
				String surveyNo = (String) objCallersurveyNo;
				String surveyType = (String) objCallersurveyType;
				String surveyMethod = (String) objCallersurveyMethod;
				generateSurveyFormDTO.setSurveyNo(surveyNo);
				generateSurveyFormDTO.setSurveyType_des(surveyType);
				generateSurveyFormDTO.setSurveyMethod_des(surveyMethod);
				btnSearch();
				fcontext.getExternalContext().getSessionMap().put("GAMI_GENERATE_SURVEY_BACK", "false");
				fcontext.getExternalContext().getSessionMap().put("GAMI_SURVEY_NO", null);
				fcontext.getExternalContext().getSessionMap().put("GAMI_SURVEY_TYPE", null);
				fcontext.getExternalContext().getSessionMap().put("GAMI_SURVEY_METHOD", null);
			}
		}

		if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().trim().isEmpty()
				&& !generateSurveyFormDTO.getFormID().equals("")) {
			renderBtnUpdateForm = true;
		} else {
			renderBtnUpdateForm = false;
		}

		disableBtnLovValues = true;

	}

	public void btnSearch() {

		pnlContent = true;
		renderBtnUpdate = false;

		if (generateSurveyFormDTO.getSurveyNo() != null && !generateSurveyFormDTO.getSurveyNo().trim().isEmpty()
				&& !generateSurveyFormDTO.getSurveyNo().equals("")) {

			if (commonService.checkTaskOnSurveyHisDetails(generateSurveyFormDTO.getSurveyNo(), "SU003", "C")) {

			} else {
				generateSurveyFormService.updateSurveyTaskDetails(generateSurveyFormDTO.getSurveyNo(), "SU004", "O");
			}

			GenerateSurveyFormDTO temptIndicatorsDTO = new GenerateSurveyFormDTO();

			drpdIndicatorOrderList = new ArrayList<GenerateSurveyFormDTO>();
			temptIndicatorsDTO.setFieldName("Header");
			temptIndicatorsDTO.setDisplayOrder(1);
			temptIndicatorsDTO.setDisplayAfter("Header");
			drpdIndicatorOrderList.add(temptIndicatorsDTO);

			generateSurveyFormDTO = gamiSariyaService.getGamiFormDetails(generateSurveyFormDTO);

			if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().isEmpty()
					&& !generateSurveyFormDTO.getFormID().equals("")) {
				disabledTxtFormId = true;
				renderBtnUpdateForm = true;
			} else {
				disabledTxtFormId = false;
				renderBtnUpdateForm = false;
			}

			tblIndicatorsList = gamiSariyaService.getGamiTblIndicatorsList(generateSurveyFormDTO);

			// if field type = LOV add lov list to each lov records
			for (GenerateSurveyFormDTO dto : tblIndicatorsList) {
				List<GenerateSurveyFormDTO> data = new ArrayList<GenerateSurveyFormDTO>();
				if (dto.getFieldType().equals("FT01")) {
					data = generateSurveyFormService.getIndicatorsValueList(dto);
					dto.setIndicator_values_list(data);
				}
			}

			drpdIndicatorOrderList.addAll(tblIndicatorsList);

			if (!(tblIndicatorsList.size() > 0)) {
				disableDrpdTemplateId = false;
			} else {
				disableDrpdTemplateId = true;
			}

		} else {
			setErrorMessage("Survey No. should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void BtnClear() {

		generateSurveyFormDTO = new GenerateSurveyFormDTO();
		indicatorsDTO = new GenerateSurveyFormDTO();
		LOVvaluesDTO = new GenerateSurveyFormDTO();
		removeLOVvaluesDTO = new GenerateSurveyFormDTO();
		editIndicatorDTO = new GenerateSurveyFormDTO();
		removeIndicatorsDTO = new GenerateSurveyFormDTO();

		drpdFormIdList = new ArrayList<GenerateSurveyFormDTO>();
		drpdIndicatorOrderList = new ArrayList<GenerateSurveyFormDTO>();
		tblIndicatorsList = new ArrayList<GenerateSurveyFormDTO>();

	}

	public void onSurveyNoChange() {

		pnlContent = false;
		String tempSutveyNo = generateSurveyFormDTO.getSurveyNo();
		generateSurveyFormDTO = new GenerateSurveyFormDTO();
		generateSurveyFormDTO.setSurveyNo(tempSutveyNo);
		generateSurveyFormDTO = gamiSariyaService.getSurveyNoDetForGenerateSurveyForm(generateSurveyFormDTO);

	}

	public void onFormIdValidate() {

		generateSurveyFormDTO.setFormID(generateSurveyFormDTO.getFormID().trim());
		boolean duplicate = gamiSariyaService.validateGamiSeriyaFormId(generateSurveyFormDTO.getFormID());

		if (duplicate) {
			generateSurveyFormDTO.setFormID("");
			setErrorMessage("Duplicate Form ID.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void onTempIdChange() {

		// if template id is not null
		if (generateSurveyFormDTO.getCopyTemplateID() != null
				&& !generateSurveyFormDTO.getCopyTemplateID().trim().isEmpty()
				&& !generateSurveyFormDTO.getCopyTemplateID().equals("")) {

			// initiate table
			tblIndicatorsList = new ArrayList<GenerateSurveyFormDTO>();

			// get table related to template id

			GenerateSurveyFormDTO getTableByFormId = new GenerateSurveyFormDTO();
			getTableByFormId.setFormID(generateSurveyFormDTO.getCopyTemplateID());
			tblIndicatorsList = gamiSariyaService.getGamiTblIndicatorsList(getTableByFormId);

			// if table record is lov field get lov values related to each
			// record
			for (GenerateSurveyFormDTO dto : tblIndicatorsList) {
				List<GenerateSurveyFormDTO> data = new ArrayList<GenerateSurveyFormDTO>();
				if (dto.getFieldType().equals("FT01")) {
					data = generateSurveyFormService.getIndicatorsValueList(dto);
					dto.setIndicator_values_list(data);
				}
			}

			// initiate display after list
			drpdIndicatorOrderList = new ArrayList<GenerateSurveyFormDTO>();

			// add header to the display after list
			GenerateSurveyFormDTO tempDTO = new GenerateSurveyFormDTO();
			tempDTO.setFieldName("Header");
			tempDTO.setDisplayOrder(1);
			tempDTO.setDisplayAfter("Header");
			drpdIndicatorOrderList.add(tempDTO);

			// add display after values from table
			drpdIndicatorOrderList.addAll(tblIndicatorsList);

		} else {
			tblIndicatorsList = new ArrayList<GenerateSurveyFormDTO>();
			drpdIndicatorOrderList = new ArrayList<GenerateSurveyFormDTO>();

			GenerateSurveyFormDTO tempDTO = new GenerateSurveyFormDTO();
			tempDTO.setFieldName("Header");
			tempDTO.setDisplayOrder(1);
			tempDTO.setDisplayAfter("Header");
			drpdIndicatorOrderList.add(tempDTO);

			GenerateSurveyFormDTO indicator_RouteNoDTO = new GenerateSurveyFormDTO();
			indicator_RouteNoDTO.setFieldName("Route No.");
			indicator_RouteNoDTO.setFieldType("FT02");
			indicator_RouteNoDTO.setFieldType_des("NUMERIC");
			indicator_RouteNoDTO.setFieldLength(10);
			indicator_RouteNoDTO.setMandatoryField(true);
			indicator_RouteNoDTO.setActive(true);
			indicator_RouteNoDTO.setDisplayOrder(displayOrder);
			indicator_RouteNoDTO.setDisplayAfter("Header");

			tblIndicatorsList.add(indicator_RouteNoDTO);
			drpdIndicatorOrderList.add(indicator_RouteNoDTO);
		}

	}

	public void onValidationMethodChange() {

	}

	public void onFieldDefinitionChange() {

	}

	public void onFieldTypeChange() {

		if (indicatorsDTO.getFieldType() != null && indicatorsDTO.getFieldType().equals("FT01")) {
			disableBtnLovValues = false;
			disableTxtFieldLength = true;
			disableDrpdFieldDefiniton = true;

		} else {
			disableBtnLovValues = true;
			disableTxtFieldLength = false;
			disableDrpdFieldDefiniton = false;
		}

		if (indicatorsDTO.getFieldType() != null && !indicatorsDTO.getFieldType().equals("FT01")) {
			for (GenerateSurveyFormDTO dto : drpdFieldDefinitionList) {
				if (indicatorsDTO.getFieldDefinition()!= null && indicatorsDTO.getFieldDefinition().equalsIgnoreCase(dto.getFieldDefinition())) {
					indicatorsDTO.setFieldDefinition_des(dto.getFieldDefinition_des());
				}
			}
		}

	}

	public void btnLovValues() {

		RequestContext.getCurrentInstance().update("LOV_values");
		RequestContext.getCurrentInstance().execute("PF('LOV_values_Dialog').show()");

	}

	public void btnAdd() {

		if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().trim().isEmpty()) { // form
																												// id
			if (generateSurveyFormDTO.getFormDescription() != null
					&& !generateSurveyFormDTO.getFormDescription().trim().isEmpty()) { // form
																						// description
				if (generateSurveyFormDTO.getHeaderLabel() != null
						&& !generateSurveyFormDTO.getHeaderLabel().trim().isEmpty()) { // header
																						// label
					if (indicatorsDTO.getFieldName() != null && !indicatorsDTO.getFieldName().trim().isEmpty()) { // field
																													// name
						if (indicatorsDTO.getFieldType() != null && !indicatorsDTO.getFieldType().trim().isEmpty()) { // field
																														// type

							if (indicatorsDTO.getDisplayAfter() != null
									&& !indicatorsDTO.getDisplayAfter().trim().isEmpty()) { // display
																							// after

								if (indicatorsDTO.getFieldLength() > 0 || !disableBtnLovValues) { // field
																									// length
																									// disable
																									// and
																									// field
																									// type
																									// equal
																									// to
																									// lov

									if ((indicatorsDTO.getFieldType().equals("FT01") && tblLovValuesList.size() > 0)
											|| (!indicatorsDTO.getFieldType().equals("FT01"))) {

										// not selected as lov
										if (!indicatorsDTO.getFieldType().equals("FT01")) {
											tblLovValuesList = new ArrayList<GenerateSurveyFormDTO>();
										}

										indicatorsDTO.setIndicator_values_list(tblLovValuesList);

										int size = tblIndicatorsList.size();
										displayOrder = size + 1;
										indicatorsDTO.setDisplayOrder(displayOrder);

										// START - method for re-order
										Boolean reOrder = false;

										for (GenerateSurveyFormDTO dto : tblIndicatorsList) {
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

										for (GenerateSurveyFormDTO dto : drpdFieldTypeList) {
											if (indicatorsDTO.getFieldType().equalsIgnoreCase(dto.getFieldType())) {
												indicatorsDTO.setFieldType_des(dto.getFieldType_des());
											}
										}
										for (GenerateSurveyFormDTO dto : drpdValidationMethodList) {
											if (indicatorsDTO.getValidationMethod()
													.equalsIgnoreCase(dto.getValidationMethod())) {
												indicatorsDTO.setValidationMethod_des(dto.getValidationMethod_des());
											}
										}

										tblIndicatorsList.add(indicatorsDTO);
										drpdIndicatorOrderList.add(indicatorsDTO);

										indicatorsDTO = new GenerateSurveyFormDTO();
										disableBtnLovValues = true;
										tblLovValuesList = new ArrayList<GenerateSurveyFormDTO>();
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

	public void btnUpdate() {
		if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().trim().isEmpty()) {
			if (generateSurveyFormDTO.getFormDescription() != null
					&& !generateSurveyFormDTO.getFormDescription().trim().isEmpty()) {
				if (generateSurveyFormDTO.getHeaderLabel() != null
						&& !generateSurveyFormDTO.getHeaderLabel().trim().isEmpty()) {
					if (indicatorsDTO.getFieldName() != null && !indicatorsDTO.getFieldName().trim().isEmpty()) {
						if (indicatorsDTO.getFieldType() != null && !indicatorsDTO.getFieldType().trim().isEmpty()) {

							if (indicatorsDTO.getDisplayAfter() != null
									&& !indicatorsDTO.getDisplayAfter().trim().isEmpty()) {

								if (indicatorsDTO.getFieldLength() > 0 || !disableBtnLovValues) {

									if ((indicatorsDTO.getFieldType().equals("FT01") && tblLovValuesList.size() > 0)
											|| (!indicatorsDTO.getFieldType().equals("FT01"))) {

										if (editIndicatorDTO != null) {

											if (!editIndicatorDTO.getFieldName()
													.equals(indicatorsDTO.getDisplayAfter())) {
												tblIndicatorsList.remove(editIndicatorDTO);
												int index = 0;

												for (GenerateSurveyFormDTO dto : tblIndicatorsList) {
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
													tblLovValuesList = new ArrayList<GenerateSurveyFormDTO>();
												}

												for (GenerateSurveyFormDTO dto : drpdFieldTypeList) {
													if (indicatorsDTO.getFieldType()
															.equalsIgnoreCase(dto.getFieldType())) {
														indicatorsDTO.setFieldType_des(dto.getFieldType_des());
													}
												}
												for (GenerateSurveyFormDTO dto : drpdValidationMethodList) {
													if (indicatorsDTO.getValidationMethod()
															.equalsIgnoreCase(dto.getValidationMethod())) {
														indicatorsDTO
																.setValidationMethod_des(dto.getValidationMethod_des());
													}
												}
												for (GenerateSurveyFormDTO dto : drpdFieldDefinitionList) {
													if (indicatorsDTO.getFieldDefinition()
															.equalsIgnoreCase(dto.getFieldDefinition())) {
														indicatorsDTO
																.setFieldDefinition_des(dto.getFieldDefinition_des());
													}
												}

												// inserting the DTO(indicator)
												// to the arrayList
												indicatorsDTO.setIndicator_values_list(tblLovValuesList);
												tblIndicatorsList.add(index, indicatorsDTO);
												drpdIndicatorOrderList.add(indicatorsDTO);
												// START - Add Display After
												// column
												Boolean isFirst = true;
												String tempFieldName = null;
												for (GenerateSurveyFormDTO dto : tblIndicatorsList) {
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
												for (GenerateSurveyFormDTO dto : tblIndicatorsList) {
													dto.setDisplayOrder(displayOrder++);
												}
												// END

												// START - remove previous field
												// name from drop down
												for (GenerateSurveyFormDTO dto : drpdIndicatorOrderList) {
													if (dto.getFieldName().equals(editIndicatorDTO.getFieldName())) {
														drpdIndicatorOrderList.remove(dto);
														break;
													}
												}
												// END

												// START - change Display After
												// field according to the
												// updated field
												// name
												for (GenerateSurveyFormDTO dto : tblIndicatorsList) {
													if (dto.getDisplayAfter().equals(editIndicatorDTO.getFieldName())) {
														dto.setDisplayAfter(indicatorsDTO.getFieldName());
														break;
													}
												}
												// END

												tblLovValuesList = new ArrayList<GenerateSurveyFormDTO>();
												editIndicatorDTO = new GenerateSurveyFormDTO();
												indicatorsDTO = new GenerateSurveyFormDTO();
												disableBtnLovValues = true;
												disableBtnAdd = false;
												disableBtnUpdate = true;
												renderBtnUpdate = false;

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

	public void btnClearTwo() {
		editIndicatorDTO = new GenerateSurveyFormDTO();
		indicatorsDTO = new GenerateSurveyFormDTO();
		if (tblLovValuesList != null && tblLovValuesList.size() > 0) {
			tblLovValuesList = new ArrayList<GenerateSurveyFormDTO>();
		}
		disableBtnLovValues = true;
		disableBtnAdd = false;
		disableBtnUpdate = true;
		renderBtnUpdate = false;

	}

	public void linkEditIndicators(GenerateSurveyFormDTO dataDTO) {

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
			tblLovValuesList = indicatorsDTO.getIndicator_values_list();
		} else {
			tblLovValuesList = new ArrayList<GenerateSurveyFormDTO>();
		}

		renderBtnUpdate = true;

		if (indicatorsDTO.getFieldType().equals("FT01")) {
			disableBtnLovValues = false;
		} else {
			disableBtnLovValues = true;
		}

	}

	public void removeIndicators() {
		if (removeIndicatorsDTO != null) {

			indicatorsDTO = new GenerateSurveyFormDTO();
			disableBtnLovValues = true;

			tblLovValuesList = new ArrayList<GenerateSurveyFormDTO>();
			String displayAfterStr = null;

			// remove field from Grid and reorder
			Boolean reOrder = false;
			Boolean reOrderDisplayString = false;

			for (GenerateSurveyFormDTO dto : tblIndicatorsList) {
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
			for (GenerateSurveyFormDTO dto : drpdIndicatorOrderList) {
				if (dto.getFieldName().equals(removeIndicatorsDTO.getFieldName())) {
					drpdIndicatorOrderList.remove(dto);
					break;
				}
			}
			tblIndicatorsList.remove(removeIndicatorsDTO);

		}

		if (!(tblIndicatorsList.size() > 0)) {
			disableDrpdTemplateId = false;
		} else {
			disableDrpdTemplateId = true;
		}

		drpdFormIdList = gamiSariyaService.getGamiTemplateIdFormList();
	}

	public void btnUpdateForm() {
		String taskCode = generateSurveyFormService.getTaskCode(generateSurveyFormDTO.getSurveyNo());
		if (taskCode.equals("SU004")) {
			if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().trim().isEmpty()) {
				if (generateSurveyFormDTO.getFormDescription() != null
						&& !generateSurveyFormDTO.getFormDescription().trim().isEmpty()) {
					if (!generateSurveyFormDTO.getFormID().equals(generateSurveyFormDTO.getCopyTemplateID())) {
						if (tblIndicatorsList.size() > 0) {

							/** update */
							boolean success = gamiSariyaService.gamiSeriyaUpdateGenerateSurveyForm(
									generateSurveyFormDTO, tblIndicatorsList, sessionBackingBean.loginUser);
							if (success) {
								// get updated values from database
								tblIndicatorsList = gamiSariyaService.getGamiTblIndicatorsList(generateSurveyFormDTO);

								// assign values for indicator value list
								for (GenerateSurveyFormDTO dto : tblIndicatorsList) {
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
			btnSearch();
		}
	}

	public void clearIndicator() {
		editIndicatorDTO = new GenerateSurveyFormDTO();
		indicatorsDTO = new GenerateSurveyFormDTO();
		if (tblLovValuesList != null && tblLovValuesList.size() > 0) {
			tblLovValuesList = new ArrayList<GenerateSurveyFormDTO>();
		}
		disableBtnLovValues = true;
		disableBtnAdd = false;
		disableBtnUpdate = true;

	}

	public void btnSave() {
		if (generateSurveyFormDTO.getFormID() != null && !generateSurveyFormDTO.getFormID().trim().isEmpty()) {
			if (generateSurveyFormDTO.getFormDescription() != null
					&& !generateSurveyFormDTO.getFormDescription().trim().isEmpty()) {
				if (!generateSurveyFormDTO.getFormID().equals(generateSurveyFormDTO.getCopyTemplateID())) {
					if (generateSurveyFormDTO.getHeaderLabel() != null
							&& !generateSurveyFormDTO.getHeaderLabel().trim().isEmpty()) {
						if (tblIndicatorsList.size() > 0) {

							/**
							 * save
							 */
							boolean success = gamiSariyaService.gamiSeriyaSaveForm(generateSurveyFormDTO,
									tblIndicatorsList, sessionBackingBean.loginUser);
							indicatorsDTO = new GenerateSurveyFormDTO();
							LOVvaluesDTO = new GenerateSurveyFormDTO();
							if (success) {
								// update task details & history
								generateSurveyFormService.updateSurveyTaskDetails(generateSurveyFormDTO.getSurveyNo(),
										"SU004", "C");

								disableBtnGenerateForm = false;

								drpdFormIdList = generateSurveyFormService.getFormIDList();

								renderBtnUpdateForm = true;

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

	public void btnCancel() {

	}

	public void btnGenerateForm() {

		try {
			// update task details & history
			generateSurveyFormService.updateSurveyTaskDetails(generateSurveyFormDTO.getSurveyNo(), "SU004", "C");
			// refresh survey no. drop-down
			FacesContext fcontextForSurveyDraft = FacesContext.getCurrentInstance();
			fcontextForSurveyDraft.getExternalContext().getSessionMap().put("GENERATE_GAMI_SURVEY", "true");
			fcontextForSurveyDraft.getExternalContext().getSessionMap().put("SURVEY_GAMI_FORM_ID",
					generateSurveyFormDTO.getFormID());
			fcontextForSurveyDraft.getExternalContext().getSessionMap().put("GAMI_SURVEY_NO",
					generateSurveyFormDTO.getSurveyNo());
			fcontextForSurveyDraft.getExternalContext().getSessionMap().put("GAMI_SURVEY_TYPE",
					generateSurveyFormDTO.getSurveyType_des());
			fcontextForSurveyDraft.getExternalContext().getSessionMap().put("GAMI_SURVEY_METHOD",
					generateSurveyFormDTO.getSurveyMethod_des());

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/gamiSariya/gamiSeriyaGenerateDraftSurveyForm.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void check_LOV_code() {
		boolean duplicate = generateSurveyFormService.check_LOV_code_duplicate(LOVvaluesDTO.getLOV_code());

		boolean alreadyInList = false;
		for (int i = 0; i < tblLovValuesList.size(); i++) {
			if (tblLovValuesList.get(i).getLOV_code() != null
					&& tblLovValuesList.get(i).getLOV_code().equals(LOVvaluesDTO.getLOV_code().trim())) {
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

	public void addLOV_values() {
		if (LOVvaluesDTO.getLOV_code() != null && !LOVvaluesDTO.getLOV_code().trim().isEmpty()) {
			if (LOVvaluesDTO.getLOV_description() != null && !LOVvaluesDTO.getLOV_description().trim().isEmpty()) {
				if (LOVvaluesDTO.getLOV_description_sinhala() != null
						&& !LOVvaluesDTO.getLOV_description_sinhala().trim().isEmpty()) {
					if (LOVvaluesDTO.getLOV_description_tamil() != null
							&& !LOVvaluesDTO.getLOV_description_tamil().trim().isEmpty()) {

						tblLovValuesList.add(LOVvaluesDTO);

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
			tblLovValuesList.remove(removeLOVvaluesDTO);
		}
	}

	public void cancelLOV_values() {
		LOVvaluesDTO = new GenerateSurveyFormDTO();
		RequestContext.getCurrentInstance().execute("PF('LOV_values_Dialog').hide()");
	}

	// getters and setters

	public GenerateSurveyFormDTO getGenerateSurveyFormDTO() {
		return generateSurveyFormDTO;
	}

	public void setGenerateSurveyFormDTO(GenerateSurveyFormDTO generateSurveyFormDTO) {
		this.generateSurveyFormDTO = generateSurveyFormDTO;
	}

	public List<GenerateSurveyFormDTO> getDrpdSurveyNoList() {
		return drpdSurveyNoList;
	}

	public void setDrpdSurveyNoList(List<GenerateSurveyFormDTO> drpdSurveyNoList) {
		this.drpdSurveyNoList = drpdSurveyNoList;
	}

	public List<GenerateSurveyFormDTO> getDrpdFormIdList() {
		return drpdFormIdList;
	}

	public void setDrpdFormIdList(List<GenerateSurveyFormDTO> drpdFormIdList) {
		this.drpdFormIdList = drpdFormIdList;
	}

	public List<GenerateSurveyFormDTO> getDrpdValidationMethodList() {
		return drpdValidationMethodList;
	}

	public void setDrpdValidationMethodList(List<GenerateSurveyFormDTO> drpdValidationMethodList) {
		this.drpdValidationMethodList = drpdValidationMethodList;
	}

	public List<GenerateSurveyFormDTO> getDrpdIndicatorOrderList() {
		return drpdIndicatorOrderList;
	}

	public void setDrpdIndicatorOrderList(List<GenerateSurveyFormDTO> drpdIndicatorOrderList) {
		this.drpdIndicatorOrderList = drpdIndicatorOrderList;
	}

	public List<GenerateSurveyFormDTO> getDrpdFieldDefinitionList() {
		return drpdFieldDefinitionList;
	}

	public void setDrpdFieldDefinitionList(List<GenerateSurveyFormDTO> drpdFieldDefinitionList) {
		this.drpdFieldDefinitionList = drpdFieldDefinitionList;
	}

	public List<GenerateSurveyFormDTO> getDrpdFieldTypeList() {
		return drpdFieldTypeList;
	}

	public void setDrpdFieldTypeList(List<GenerateSurveyFormDTO> drpdFieldTypeList) {
		this.drpdFieldTypeList = drpdFieldTypeList;
	}

	public List<GenerateSurveyFormDTO> getTblIndicatorsList() {
		return tblIndicatorsList;
	}

	public void setTblIndicatorsList(List<GenerateSurveyFormDTO> tblIndicatorsList) {
		this.tblIndicatorsList = tblIndicatorsList;
	}

	public GenerateSurveyFormDTO getIndicatorsDTO() {
		return indicatorsDTO;
	}

	public void setIndicatorsDTO(GenerateSurveyFormDTO indicatorsDTO) {
		this.indicatorsDTO = indicatorsDTO;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GenerateSurveyFormDTO getLOVvaluesDTO() {
		return LOVvaluesDTO;
	}

	public void setLOVvaluesDTO(GenerateSurveyFormDTO lOVvaluesDTO) {
		LOVvaluesDTO = lOVvaluesDTO;
	}

	public GenerateSurveyFormDTO getRemoveLOVvaluesDTO() {
		return removeLOVvaluesDTO;
	}

	public void setRemoveLOVvaluesDTO(GenerateSurveyFormDTO removeLOVvaluesDTO) {
		this.removeLOVvaluesDTO = removeLOVvaluesDTO;
	}

	public GenerateSurveyFormDTO getEditIndicatorDTO() {
		return editIndicatorDTO;
	}

	public void setEditIndicatorDTO(GenerateSurveyFormDTO editIndicatorDTO) {
		this.editIndicatorDTO = editIndicatorDTO;
	}

	public GenerateSurveyFormDTO getRemoveIndicatorsDTO() {
		return removeIndicatorsDTO;
	}

	public void setRemoveIndicatorsDTO(GenerateSurveyFormDTO removeIndicatorsDTO) {
		this.removeIndicatorsDTO = removeIndicatorsDTO;
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public boolean isDisableDrpdFieldDefiniton() {
		return disableDrpdFieldDefiniton;
	}

	public void setDisableDrpdFieldDefiniton(boolean disableDrpdFieldDefiniton) {
		this.disableDrpdFieldDefiniton = disableDrpdFieldDefiniton;
	}

	public boolean isDisableTxtFieldLength() {
		return disableTxtFieldLength;
	}

	public void setDisableTxtFieldLength(boolean disableTxtFieldLength) {
		this.disableTxtFieldLength = disableTxtFieldLength;
	}

	public boolean isDisableBtnLovValues() {
		return disableBtnLovValues;
	}

	public void setDisableBtnLovValues(boolean disableBtnLovValues) {
		this.disableBtnLovValues = disableBtnLovValues;
	}

	public boolean isDisableDrpdTemplateId() {
		return disableDrpdTemplateId;
	}

	public void setDisableDrpdTemplateId(boolean disableDrpdTemplateId) {
		this.disableDrpdTemplateId = disableDrpdTemplateId;
	}

	public GenerateSurveyFormService getGenerateSurveyFormService() {
		return generateSurveyFormService;
	}

	public void setGenerateSurveyFormService(GenerateSurveyFormService generateSurveyFormService) {
		this.generateSurveyFormService = generateSurveyFormService;
	}

	public List<GenerateSurveyFormDTO> getTblLovValuesList() {
		return tblLovValuesList;
	}

	public void setTblLovValuesList(List<GenerateSurveyFormDTO> tblLovValuesList) {
		this.tblLovValuesList = tblLovValuesList;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public boolean isDisableBtnAdd() {
		return disableBtnAdd;
	}

	public void setDisableBtnAdd(boolean disableBtnAdd) {
		this.disableBtnAdd = disableBtnAdd;
	}

	public boolean isDisableBtnUpdate() {
		return disableBtnUpdate;
	}

	public void setDisableBtnUpdate(boolean disableBtnUpdate) {
		this.disableBtnUpdate = disableBtnUpdate;
	}

	public boolean isDisableBtnGenerateForm() {
		return disableBtnGenerateForm;
	}

	public void setDisableBtnGenerateForm(boolean disableBtnGenerateForm) {
		this.disableBtnGenerateForm = disableBtnGenerateForm;
	}

	public boolean isDisableBtnUpdateForm() {
		return disableBtnUpdateForm;
	}

	public void setDisableBtnUpdateForm(boolean disableBtnUpdateForm) {
		this.disableBtnUpdateForm = disableBtnUpdateForm;
	}

	public boolean isRenderBtnUpdate() {
		return renderBtnUpdate;
	}

	public void setRenderBtnUpdate(boolean renderBtnUpdate) {
		this.renderBtnUpdate = renderBtnUpdate;
	}

	public boolean isRenderBtnUpdateForm() {
		return renderBtnUpdateForm;
	}

	public void setRenderBtnUpdateForm(boolean renderBtnUpdateForm) {
		this.renderBtnUpdateForm = renderBtnUpdateForm;
	}

	public boolean isDisabledTxtFormId() {
		return disabledTxtFormId;
	}

	public void setDisabledTxtFormId(boolean disabledTxtFormId) {
		this.disabledTxtFormId = disabledTxtFormId;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isPnlContent() {
		return pnlContent;
	}

	public void setPnlContent(boolean pnlContent) {
		this.pnlContent = pnlContent;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

}
