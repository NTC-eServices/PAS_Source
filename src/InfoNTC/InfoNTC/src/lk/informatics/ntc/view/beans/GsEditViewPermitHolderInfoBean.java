package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.GamiRouteDTO;
import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "gsEditViewPermitHolderInfoBean")
@ViewScoped
public class GsEditViewPermitHolderInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private int activeTabIndex;
	private boolean renderTabView;

	private boolean disableTab01, disableTab02;
	private boolean disableDrpdTenderRefNo, disabledDrpdServiceRefNo, disableDrpdPermitNo;
	private boolean renderBtnClear, renderBtnUpdateServiceInformation, renderBtnSaveServiceInformation;
	private boolean edit, renderViewMode;
	private boolean renderTblPermitHolderInfo;

	private GamiSeriyaDTO gamiSeriyaDTO;
	private SisuSeriyaDTO sisuSeriyaDTO;

	private List<GamiSeriyaDTO> drpdViaList;
	private List<GamiSeriyaDTO> drpdServiceRefNoList, drpdTenderRefNoList, drpdServiceNoList;
	private List<GamiSeriyaDTO> drpdPermitNoList, drpdOriginList, drpdDestinationList;
	private List<GamiRouteDTO> drpdSequenceNoList;
	private List<GenerateReceiptDTO> drpdBankList, drpdBankBranchList;

	private List<CommonDTO> drpdServiceTypeList, drpdProvinceList, drpdDistrictList, drpdDevsecList;

	private List<GamiSeriyaDTO> tblPermitHolderInfo;

	private String errorMsg;
	private String alertMsg;

	private GamiSariyaService gamiSariyaService;
	private AdminService adminService;
	private SurveyService surveyService;

	@PostConstruct
	public void init() {

		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");

		gamiSeriyaDTO = new GamiSeriyaDTO();
		sisuSeriyaDTO = new SisuSeriyaDTO();

		drpdViaList = new ArrayList<GamiSeriyaDTO>();
		drpdServiceRefNoList = new ArrayList<GamiSeriyaDTO>();
		drpdTenderRefNoList = new ArrayList<GamiSeriyaDTO>();
		drpdServiceNoList = new ArrayList<GamiSeriyaDTO>();

		drpdPermitNoList = new ArrayList<GamiSeriyaDTO>();
		drpdServiceTypeList = new ArrayList<CommonDTO>();
		drpdOriginList = new ArrayList<GamiSeriyaDTO>();
		drpdDestinationList = new ArrayList<GamiSeriyaDTO>();
		drpdProvinceList = new ArrayList<CommonDTO>();
		drpdDistrictList = new ArrayList<CommonDTO>();
		drpdDevsecList = new ArrayList<CommonDTO>();
		drpdBankList = new ArrayList<GenerateReceiptDTO>();
		drpdBankBranchList = new ArrayList<GenerateReceiptDTO>();
		drpdSequenceNoList = new ArrayList<GamiRouteDTO>();
		tblPermitHolderInfo = new ArrayList<GamiSeriyaDTO>();

		loadValues();
	}

	private void loadValues() {

		renderBtnUpdateServiceInformation = true;

		drpdServiceTypeList = adminService.getServiceTypeToDropdown();
		drpdProvinceList = adminService.getProvinceToDropdown();

		drpdTenderRefNoList = gamiSariyaService.getDrpdTenderReferenceNoList();
		drpdServiceRefNoList = gamiSariyaService.getDrpdServiceReferenceNoList();
		drpdPermitNoList = gamiSariyaService.getDrpdPermitNoList();
		drpdServiceNoList = gamiSariyaService.getDrpdServiceNoList();
		drpdBankList = surveyService.getBankListDropDown();

	}

	public void onTenderRefNoChange() {
		gamiSeriyaDTO.setServiceRefNo(null);
		gamiSeriyaDTO.setPermitNo(null);
		gamiSeriyaDTO.setServiceNo(null);
		renderTblPermitHolderInfo = false;
		renderTabView = false;
	}

	public void onServiceRefNoChange() {
		gamiSeriyaDTO.setTenderRefNo(null);
		gamiSeriyaDTO.setPermitNo(null);
		gamiSeriyaDTO.setServiceNo(null);
		renderTabView = false;
	}

	public void onPermitNoChange() {
		gamiSeriyaDTO.setTenderRefNo(null);
		gamiSeriyaDTO.setServiceRefNo(null);
		gamiSeriyaDTO.setServiceNo(null);
		renderTabView = false;
	}

	public void onServiceNoChange() {
		gamiSeriyaDTO.setTenderRefNo(null);
		gamiSeriyaDTO.setServiceRefNo(null);
		gamiSeriyaDTO.setPermitNo(null);
		renderTabView = false;
	}

	public void btnSearch() {

		renderTabView = true;
		renderBtnSaveServiceInformation = false;
		renderTblPermitHolderInfo = false;
		drpdSequenceNoList = gamiSariyaService.drpdSequenceNoListForPermotHolderInfo(gamiSeriyaDTO);

		if (gamiSeriyaDTO.getTenderRefNo() != null && !gamiSeriyaDTO.getTenderRefNo().isEmpty()
				&& !gamiSeriyaDTO.getTenderRefNo().equalsIgnoreCase("")) {

			renderTblPermitHolderInfo = true;
			renderViewMode = false;
			tblPermitHolderInfo = gamiSariyaService.getTblGamiServiceInformation(gamiSeriyaDTO);
			alertMsg = "To update a record use the grid below.";

			RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");

		} else if (gamiSeriyaDTO.getServiceRefNo() != null && !gamiSeriyaDTO.getServiceRefNo().isEmpty()
				&& !gamiSeriyaDTO.getServiceRefNo().equalsIgnoreCase("")) {
			gamiSeriyaDTO = gamiSariyaService.viewGamiServiceInformation(gamiSeriyaDTO);
		} else if (gamiSeriyaDTO.getPermitNo() != null && !gamiSeriyaDTO.getPermitNo().isEmpty()
				&& !gamiSeriyaDTO.getPermitNo().equalsIgnoreCase("")) {
			gamiSeriyaDTO = gamiSariyaService.viewGamiServiceInformation(gamiSeriyaDTO);
		} else if (gamiSeriyaDTO.getServiceNo() != null && !gamiSeriyaDTO.getServiceNo().isEmpty()
				&& !gamiSeriyaDTO.getServiceNo().equalsIgnoreCase("")) {
			gamiSeriyaDTO = gamiSariyaService.viewGamiServiceInformation(gamiSeriyaDTO);
		} else {

			errorMsg = "Tender Reference No. should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}
		
		// added by thilna.d on 28-10-2021
		String tempDivisionalSec = gamiSeriyaDTO.getDivisionalSecCode();
		onProvinceChange();
		onDistrictChange();
		gamiSeriyaDTO.setDivisionalSecCode(tempDivisionalSec);

		renderTabView = true;

	}

	public void btnClear() {

		gamiSeriyaDTO = new GamiSeriyaDTO();
		renderTabView = false;
		renderTblPermitHolderInfo = false;
	}

	public void onOriginChange() {

	}

	public void onDestinationChange() {

	}

	public void onProvinceChange() {

		drpdDistrictList = adminService.getDistrictByProvinceToDropdown(gamiSeriyaDTO.getProvinceCode());
		gamiSeriyaDTO.setDivisionalSecCode(null);

	}

	public void onDistrictChange() {

		drpdDevsecList = adminService.getDivSecByDistrictToDropdown(gamiSeriyaDTO.getDistrictCode());

	}

	public void onSequenceNoChange() {

		GamiRouteDTO routeDetails = gamiSariyaService.getGamiRouteDetailsBySequenceNo(gamiSeriyaDTO);
		gamiSeriyaDTO.setOriginDesc(routeDetails.getOriginDesc());
		gamiSeriyaDTO.setDestinationDesc(routeDetails.getDestinationDesc());
		gamiSeriyaDTO.setVia(routeDetails.getViaDesc());
		gamiSeriyaDTO.setUnservedPortion(routeDetails.getUnservedPortion());
		gamiSeriyaDTO.setTotalLength(routeDetails.getTotalLength());
	}

	public void btnTab01Save() {

		boolean continueBtnTabOneSave = false;

		if (gamiSeriyaDTO.getPreferedLanguage() != null && !gamiSeriyaDTO.getPreferedLanguage().isEmpty()
				&& !gamiSeriyaDTO.getPreferedLanguage().equalsIgnoreCase("")) {

			if (gamiSeriyaDTO.getPreferedLanguage().equalsIgnoreCase("E")
					|| gamiSeriyaDTO.getPreferedLanguage().equalsIgnoreCase("S")
					|| gamiSeriyaDTO.getPreferedLanguage().equalsIgnoreCase("T")) {

				// validate for English
				if (gamiSeriyaDTO.getPreferedLanguage().equalsIgnoreCase("E")) {

					if (gamiSeriyaDTO.getNameinFull() != null && !gamiSeriyaDTO.getNameinFull().isEmpty()
							&& !gamiSeriyaDTO.getNameinFull().equalsIgnoreCase("")) {

						if (gamiSeriyaDTO.getAddress1() != null && !gamiSeriyaDTO.getAddress1().isEmpty()
								&& !gamiSeriyaDTO.getAddress1().equalsIgnoreCase("")) {

							if (gamiSeriyaDTO.getCity() != null && !gamiSeriyaDTO.getCity().isEmpty()
									&& !gamiSeriyaDTO.getCity().equalsIgnoreCase("")) {

								// validate for sinhala

								if (gamiSeriyaDTO.getPreferedLanguage().equalsIgnoreCase("S")) {

									if (gamiSeriyaDTO.getNameinFullSinhala() != null
											&& !gamiSeriyaDTO.getNameinFullSinhala().isEmpty()
											&& !gamiSeriyaDTO.getNameinFullSinhala().equalsIgnoreCase("")) {

										if (gamiSeriyaDTO.getAddress1sinhala() != null
												&& !gamiSeriyaDTO.getAddress1sinhala().isEmpty()
												&& !gamiSeriyaDTO.getAddress1sinhala().equalsIgnoreCase("")) {

											if (gamiSeriyaDTO.getCitySinhala() != null
													&& !gamiSeriyaDTO.getCitySinhala().isEmpty()
													&& !gamiSeriyaDTO.getCitySinhala().equalsIgnoreCase("")) {

												continueBtnTabOneSave = true;

											} else {
												errorMsg = "City in Sinhala should be entered.";
												RequestContext.getCurrentInstance().update("frm-serviceInformation");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}

										} else {
											errorMsg = "Address Line One in Sinhala should be entered.";
											RequestContext.getCurrentInstance().update("frm-serviceInformation");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										errorMsg = "Name Of Operetor in Sinhala should be entered.";
										RequestContext.getCurrentInstance().update("frm-serviceInformation");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

									// validate for Tamil
								} else if (gamiSeriyaDTO.getPreferedLanguage().equalsIgnoreCase("T")) {

									if (gamiSeriyaDTO.getNameinFullTamil() != null
											&& !gamiSeriyaDTO.getNameinFullTamil().isEmpty()
											&& !gamiSeriyaDTO.getNameinFullTamil().equalsIgnoreCase("")) {

										if (gamiSeriyaDTO.getAddress1Tamil() != null
												&& !gamiSeriyaDTO.getAddress1Tamil().isEmpty()
												&& !gamiSeriyaDTO.getAddress1Tamil().equalsIgnoreCase("")) {

											if (gamiSeriyaDTO.getCityTamil() != null
													&& !gamiSeriyaDTO.getCityTamil().isEmpty()
													&& !gamiSeriyaDTO.getCityTamil().equalsIgnoreCase("")) {

												continueBtnTabOneSave = true;

											} else {
												errorMsg = "City in Tamil should be entered.";
												RequestContext.getCurrentInstance().update("frm-serviceInformation");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}

										} else {
											errorMsg = "Address Line one in Tamil should be entered.";
											RequestContext.getCurrentInstance().update("frm-serviceInformation");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}

									} else {
										errorMsg = "Name Of Operetor in Tamil should be entered.";
										RequestContext.getCurrentInstance().update("frm-serviceInformation");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								} else {
									continueBtnTabOneSave = true;
								}

								/**
								 * After validate
								 **/
								if (continueBtnTabOneSave) {

									if (gamiSeriyaDTO.getServiceTypeCode() != null
											&& !gamiSeriyaDTO.getServiceTypeCode().isEmpty()
											&& !gamiSeriyaDTO.getServiceTypeCode().equalsIgnoreCase("")) {

										if (gamiSeriyaDTO.getBusNo() != null && !gamiSeriyaDTO.getBusNo().isEmpty()
												&& !gamiSeriyaDTO.getBusNo().equalsIgnoreCase("")) {

											if (gamiSeriyaDTO.getUnservedPortion() != 0) {

												if (gamiSeriyaDTO.getSequenceNo() != null
														&& !gamiSeriyaDTO.getSequenceNo().isEmpty()
														&& !gamiSeriyaDTO.getSequenceNo().equalsIgnoreCase("")) {

													if (gamiSeriyaDTO.getPermitNo() != null
															&& !gamiSeriyaDTO.getPermitNo().isEmpty()
															&& !gamiSeriyaDTO.getPermitNo().equalsIgnoreCase("")) {
														if (gamiSeriyaDTO.getOriginDesc() != null
																&& !gamiSeriyaDTO.getOriginDesc().isEmpty()
																&& !gamiSeriyaDTO.getOriginDesc()
																		.equalsIgnoreCase("")) {

															if (gamiSeriyaDTO.getTotalLength() != 0) {

																if (gamiSeriyaDTO.getDestinationDesc() != null
																		&& !gamiSeriyaDTO.getDestinationDesc().isEmpty()
																		&& !gamiSeriyaDTO.getDestinationDesc()
																				.equalsIgnoreCase("")) {

																	if (gamiSeriyaDTO.getAgreedAmount() != null
																			&& gamiSeriyaDTO.getAgreedAmount()
																					.compareTo(new BigDecimal(
																							"0.00")) != 0) {

																		if (gamiSeriyaDTO.getIdNo() != null
																				&& !gamiSeriyaDTO.getIdNo().isEmpty()
																				&& !gamiSeriyaDTO.getIdNo()
																						.equalsIgnoreCase("")) {

																			if (gamiSeriyaDTO.getProvinceCode() != null
																					&& !gamiSeriyaDTO.getProvinceCode()
																							.isEmpty()
																					&& !gamiSeriyaDTO.getProvinceCode()
																							.equalsIgnoreCase("")) {

																				/** Update Service Information */
																				if (renderBtnUpdateServiceInformation) {

																					if (gamiSariyaService
																							.updateGamiServiceInformation(
																									gamiSeriyaDTO,
																									sessionBackingBean.loginUser)) {
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('successMessage').show()");
																					} else {
																						errorMsg = "Data didn't update.";
																						RequestContext
																								.getCurrentInstance()
																								.update("frm-serviceInformation");
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('requiredField').show()");
																					}

																					/** Save Service Information */
																				} else {

																					gamiSeriyaDTO.setStatus("O");
																					gamiSeriyaDTO = gamiSariyaService
																							.saveGamiServiceInformation(
																									gamiSeriyaDTO,
																									sessionBackingBean.loginUser);
																					if (gamiSeriyaDTO
																							.getServiceRefNo() != null
																							&& !gamiSeriyaDTO
																									.getServiceRefNo()
																									.isEmpty()
																							&& !gamiSeriyaDTO
																									.getServiceRefNo()
																									.equalsIgnoreCase(
																											"")) {
																						tblPermitHolderInfo = gamiSariyaService
																								.getTblGamiServiceInformation(
																										gamiSeriyaDTO);
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('successMessage').show()");

																						renderBtnSaveServiceInformation = false;

																					} else {
																						errorMsg = "Data didn't save.";
																						RequestContext
																								.getCurrentInstance()
																								.update("frm-serviceInformation");
																						RequestContext
																								.getCurrentInstance()
																								.execute(
																										"PF('requiredField').show()");
																					}

																				}

																			} else {
																				errorMsg = "Province should be entered.";
																				RequestContext.getCurrentInstance()
																						.update("frm-serviceInformation");
																				RequestContext.getCurrentInstance()
																						.execute(
																								"PF('requiredField').show()");
																			}

																		} else {
																			errorMsg = "Nic No. should be entered.";
																			RequestContext.getCurrentInstance()
																					.update("frm-serviceInformation");
																			RequestContext.getCurrentInstance().execute(
																					"PF('requiredField').show()");
																		}

																	} else {
																		errorMsg = "Agreed Amount should be entered.";
																		RequestContext.getCurrentInstance()
																				.update("frm-serviceInformation");
																		RequestContext.getCurrentInstance()
																				.execute("PF('requiredField').show()");
																	}

																} else {
																	errorMsg = "Destination should be entered.";
																	RequestContext.getCurrentInstance()
																			.update("frm-serviceInformation");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}

															} else {
																errorMsg = "Total Length should be entered.";
																RequestContext.getCurrentInstance()
																		.update("frm-serviceInformation");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}

														} else {
															errorMsg = "Origin No should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frm-serviceInformation");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}

													} else {
														errorMsg = "Permit No should be entered.";
														RequestContext.getCurrentInstance()
																.update("frm-serviceInformation");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}

												} else {
													errorMsg = "Sequence No should be entered.";
													RequestContext.getCurrentInstance()
															.update("frm-serviceInformation");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}

											} else {
												errorMsg = "Unserved Portion should be entered.";
												RequestContext.getCurrentInstance().update("frm-serviceInformation");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}

										} else {
											errorMsg = "Bus No should be entered.";
											RequestContext.getCurrentInstance().update("frm-serviceInformation");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}

									} else {
										errorMsg = "Service Type should be entered.";
										RequestContext.getCurrentInstance().update("frm-serviceInformation");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								}
								/***/
							} else {
								errorMsg = "City in English should be entered.";
								RequestContext.getCurrentInstance().update("frm-serviceInformation");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							errorMsg = "Address Line One in English should be entered.";
							RequestContext.getCurrentInstance().update("frm-serviceInformation");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						errorMsg = "Name of Operator in English should be entered.";
						RequestContext.getCurrentInstance().update("frm-serviceInformation");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {

				}

			}

		} else {
			errorMsg = "Preferred Language should be entered.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void tblViewServiceInformation(GamiSeriyaDTO permitHolderInfo) {

		gamiSeriyaDTO.setServiceRefNo(permitHolderInfo.getServiceRefNo());
		gamiSeriyaDTO = gamiSariyaService.viewGamiServiceInformation(gamiSeriyaDTO);
		drpdBankBranchList = surveyService.getBankBranchDropDown(gamiSeriyaDTO.getBankNameCode());
		renderViewMode = true;
		if (gamiSeriyaDTO.getProvinceCode() != null) {
			drpdDistrictList = adminService.getDistrictByProvinceToDropdown(gamiSeriyaDTO.getProvinceCode());
		}

		if (gamiSeriyaDTO.getDistrictCode() != null) {
			drpdDevsecList = adminService.getDivSecByDistrictToDropdown(gamiSeriyaDTO.getDistrictCode());
		}
	}

	public void tblEditServiceInformation(GamiSeriyaDTO permitHolderInfo) {

		renderBtnUpdateServiceInformation = true;

		gamiSeriyaDTO.setServiceRefNo(permitHolderInfo.getServiceRefNo());
		gamiSeriyaDTO = gamiSariyaService.viewGamiServiceInformation(gamiSeriyaDTO);
		drpdBankBranchList = surveyService.getBankBranchDropDown(gamiSeriyaDTO.getBankNameCode());
		renderViewMode = false;
		if (gamiSeriyaDTO.getProvinceCode() != null) {
			drpdDistrictList = adminService.getDistrictByProvinceToDropdown(gamiSeriyaDTO.getProvinceCode());
		}
		if (sisuSeriyaDTO.getDistrictCode() != null) {
			drpdDevsecList = adminService.getDivSecByDistrictToDropdown(gamiSeriyaDTO.getDistrictCode());
		}

	}

	public void onBankChange() {
		drpdBankBranchList = surveyService.getBankBranchDropDown(gamiSeriyaDTO.getBankNameCode());
	}

	public void btnTab01Clear() {

		renderBtnSaveServiceInformation = true;

	}

	public void btnTab01UploadRoadMap() {

	}

	public void btnTab02Save() {

		if (gamiSariyaService.updateBankInformation(gamiSeriyaDTO, sessionBackingBean.loginUser)) {
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {
			errorMsg = "Data didn't save.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnTab02Clear() {
		gamiSeriyaDTO.setAccountNo(null);
		gamiSeriyaDTO.setBankNameCode(null);
		gamiSeriyaDTO.setBankBranchNameCode(null);
		RequestContext.getCurrentInstance().update("frm-serviceInformation");
	}

	public void btnTab02DocumentManagement() {

	}

	// getters and setters
	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public boolean isRenderTabView() {
		return renderTabView;
	}

	public void setRenderTabView(boolean renderTabView) {
		this.renderTabView = renderTabView;
	}

	public boolean isDisableTab01() {
		return disableTab01;
	}

	public void setDisableTab01(boolean disableTab01) {
		this.disableTab01 = disableTab01;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GamiSeriyaDTO getGamiSeriyaDTO() {
		return gamiSeriyaDTO;
	}

	public void setGamiSeriyaDTO(GamiSeriyaDTO gamiSeriyaDTO) {
		this.gamiSeriyaDTO = gamiSeriyaDTO;
	}

	public boolean isDisableTab02() {
		return disableTab02;
	}

	public void setDisableTab02(boolean disableTab02) {
		this.disableTab02 = disableTab02;
	}

	public boolean isDisableDrpdTenderRefNo() {
		return disableDrpdTenderRefNo;
	}

	public void setDisableDrpdTenderRefNo(boolean disableDrpdTenderRefNo) {
		this.disableDrpdTenderRefNo = disableDrpdTenderRefNo;
	}

	public boolean isDisabledDrpdServiceRefNo() {
		return disabledDrpdServiceRefNo;
	}

	public void setDisabledDrpdServiceRefNo(boolean disabledDrpdServiceRefNo) {
		this.disabledDrpdServiceRefNo = disabledDrpdServiceRefNo;
	}

	public boolean isDisableDrpdPermitNo() {
		return disableDrpdPermitNo;
	}

	public void setDisableDrpdPermitNo(boolean disableDrpdPermitNo) {
		this.disableDrpdPermitNo = disableDrpdPermitNo;
	}

	public boolean isRenderBtnClear() {
		return renderBtnClear;
	}

	public void setRenderBtnClear(boolean renderBtnClear) {
		this.renderBtnClear = renderBtnClear;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public SisuSeriyaDTO getSisuSeriyaDTO() {
		return sisuSeriyaDTO;
	}

	public void setSisuSeriyaDTO(SisuSeriyaDTO sisuSeriyaDTO) {
		this.sisuSeriyaDTO = sisuSeriyaDTO;
	}

	public List<GamiSeriyaDTO> getDrpdViaList() {
		return drpdViaList;
	}

	public void setDrpdViaList(List<GamiSeriyaDTO> drpdViaList) {
		this.drpdViaList = drpdViaList;
	}

	public List<GamiSeriyaDTO> getDrpdServiceRefNoList() {
		return drpdServiceRefNoList;
	}

	public void setDrpdServiceRefNoList(List<GamiSeriyaDTO> drpdServiceRefNoList) {
		this.drpdServiceRefNoList = drpdServiceRefNoList;
	}

	public List<GamiSeriyaDTO> getDrpdPermitNoList() {
		return drpdPermitNoList;
	}

	public void setDrpdPermitNoList(List<GamiSeriyaDTO> drpdPermitNoList) {
		this.drpdPermitNoList = drpdPermitNoList;
	}

	public List<CommonDTO> getDrpdServiceTypeList() {
		return drpdServiceTypeList;
	}

	public void setDrpdServiceTypeList(List<CommonDTO> drpdServiceTypeList) {
		this.drpdServiceTypeList = drpdServiceTypeList;
	}

	public List<GamiSeriyaDTO> getDrpdOriginList() {
		return drpdOriginList;
	}

	public void setDrpdOriginList(List<GamiSeriyaDTO> drpdOriginList) {
		this.drpdOriginList = drpdOriginList;
	}

	public List<GamiSeriyaDTO> getDrpdDestinationList() {
		return drpdDestinationList;
	}

	public void setDrpdDestinationList(List<GamiSeriyaDTO> drpdDestinationList) {
		this.drpdDestinationList = drpdDestinationList;
	}

	public List<GenerateReceiptDTO> getDrpdBankList() {
		return drpdBankList;
	}

	public void setDrpdBankList(List<GenerateReceiptDTO> drpdBankList) {
		this.drpdBankList = drpdBankList;
	}

	public List<GenerateReceiptDTO> getDrpdBankBranchList() {
		return drpdBankBranchList;
	}

	public void setDrpdBankBranchList(List<GenerateReceiptDTO> drpdBankBranchList) {
		this.drpdBankBranchList = drpdBankBranchList;
	}

	public List<GamiSeriyaDTO> getTblPermitHolderInfo() {
		return tblPermitHolderInfo;
	}

	public void setTblPermitHolderInfo(List<GamiSeriyaDTO> tblPermitHolderInfo) {
		this.tblPermitHolderInfo = tblPermitHolderInfo;
	}

	public List<GamiSeriyaDTO> getDrpdTenderRefNoList() {
		return drpdTenderRefNoList;
	}

	public void setDrpdTenderRefNoList(List<GamiSeriyaDTO> drpdTenderRefNoList) {
		this.drpdTenderRefNoList = drpdTenderRefNoList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public List<GamiSeriyaDTO> getDrpdServiceNoList() {
		return drpdServiceNoList;
	}

	public void setDrpdServiceNoList(List<GamiSeriyaDTO> drpdServiceNoList) {
		this.drpdServiceNoList = drpdServiceNoList;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public boolean isRenderBtnUpdateServiceInformation() {
		return renderBtnUpdateServiceInformation;
	}

	public void setRenderBtnUpdateServiceInformation(boolean renderBtnUpdateServiceInformation) {
		this.renderBtnUpdateServiceInformation = renderBtnUpdateServiceInformation;
	}

	public boolean isRenderBtnSaveServiceInformation() {
		return renderBtnSaveServiceInformation;
	}

	public void setRenderBtnSaveServiceInformation(boolean renderBtnSaveServiceInformation) {
		this.renderBtnSaveServiceInformation = renderBtnSaveServiceInformation;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<CommonDTO> getDrpdProvinceList() {
		return drpdProvinceList;
	}

	public void setDrpdProvinceList(List<CommonDTO> drpdProvinceList) {
		this.drpdProvinceList = drpdProvinceList;
	}

	public boolean isRenderTblPermitHolderInfo() {
		return renderTblPermitHolderInfo;
	}

	public void setRenderTblPermitHolderInfo(boolean renderTblPermitHolderInfo) {
		this.renderTblPermitHolderInfo = renderTblPermitHolderInfo;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public boolean isRenderViewMode() {
		return renderViewMode;
	}

	public void setRenderViewMode(boolean renderViewMode) {
		this.renderViewMode = renderViewMode;
	}

	public List<CommonDTO> getDrpdDistrictList() {
		return drpdDistrictList;
	}

	public void setDrpdDistrictList(List<CommonDTO> drpdDistrictList) {
		this.drpdDistrictList = drpdDistrictList;
	}

	public List<CommonDTO> getDrpdDevsecList() {
		return drpdDevsecList;
	}

	public void setDrpdDevsecList(List<CommonDTO> drpdDevsecList) {
		this.drpdDevsecList = drpdDevsecList;
	}

	public List<GamiRouteDTO> getDrpdSequenceNoList() {
		return drpdSequenceNoList;
	}

	public void setDrpdSequenceNoList(List<GamiRouteDTO> drpdSequenceNoList) {
		this.drpdSequenceNoList = drpdSequenceNoList;
	}

}
