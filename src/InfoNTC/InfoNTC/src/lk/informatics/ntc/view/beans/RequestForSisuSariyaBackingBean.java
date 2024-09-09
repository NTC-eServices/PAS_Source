package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "requestForSisuSariyaBackingBean")
@ViewScoped
public class RequestForSisuSariyaBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services
	private DocumentManagementService documentManagementService;
	private AdminService adminService;
	private CommonService commonService;

	private SisuSariyaService sisuSariyaService;
	// DTO

	private SisuSeriyaDTO sisuSariyaDTO;
	private List<SisuSeriyaDTO> requestTypeList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> prefLanguList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> showDataOnGrid;
	private SisuSeriyaDTO selectedRow;
	private List<CommonDTO> drpdProvincelList;
	private List<CommonDTO> drpdDistrictList;
	private List<CommonDTO> drpdDevsecList;

	private int activeTabIndex;
	private String selectedReqNo, selectedRequestorType, selectedPrefLang, selectedReqType, selectedIdNo,
			successMessage, errorMsg, loginUser;
	private boolean districtDisable;
	private boolean districDivSectDisable;
	// ArrayLists
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	@PostConstruct
	public void init() {
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		requestTypeList = sisuSariyaService.getRequestorTypeForDropDown();
		sisuSariyaDTO = new SisuSeriyaDTO();
		prefLanguList = sisuSariyaService.getPrefLanguForDropDown();
		drpdProvincelList = adminService.getProvinceToDropdown();
		drpdDevsecList = adminService.getDivSecToDropdown();
		drpdDistrictList = adminService.getDistrictToDropdown();
		loginUser = sessionBackingBean.getLoginUser();
		activeTabIndex = 0;
		districtDisable = true;
		districDivSectDisable = true;
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
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

	public void saveRecord() {
		if (sisuSariyaDTO.getRequestorTypeDes() != null && !sisuSariyaDTO.getRequestorTypeDes().trim().isEmpty()) {
			if (sisuSariyaDTO.getRequestDate() != null && !sisuSariyaDTO.getRequestDate().toString().trim().isEmpty()) {
				if (sisuSariyaDTO.getLanguageDes() != null && !sisuSariyaDTO.getLanguageDes().trim().isEmpty()) {
					if (sisuSariyaDTO.getLanguageDes().equalsIgnoreCase("ENGLISH")) {
						if (sisuSariyaDTO.getNameOfOperator() != null
								&& !sisuSariyaDTO.getNameOfOperator().trim().isEmpty()
								&& sisuSariyaDTO.getAddressOne() != null
								&& !sisuSariyaDTO.getAddressOne().trim().isEmpty()

								&& sisuSariyaDTO.getCity() != null && !sisuSariyaDTO.getCity().trim().isEmpty()
								&& sisuSariyaDTO.getSchoolName() != null
								&& !sisuSariyaDTO.getSchoolName().trim().isEmpty()
								&& sisuSariyaDTO.getSchoolAddressOne() != null
								&& !sisuSariyaDTO.getSchoolAddressOne().trim().isEmpty()

								&& sisuSariyaDTO.getSchoolCity() != null
								&& !sisuSariyaDTO.getSchoolCity().trim().isEmpty()

						) {
							boolean success = sisuSariyaService.saveRequestSisusariData(sisuSariyaDTO, loginUser);

							if (success) {
								setSuccessMessage("Succesfully saved.");
								RequestContext.getCurrentInstance().update("successMSG");
								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								activeTabIndex = 1;

								boolean insert = commonService.insertTaskDetailsSubsidy(sisuSariyaDTO.getRequestNo(), null,
										sessionBackingBean.getLoginUser(), "SS001", "O", null);
								boolean update = commonService.updateTaskStatusCompletedSubsidyTaskTabel(sisuSariyaDTO.getRequestNo(),
										null, null, "SS001", "C");
								
								if(!insert || !update) {
									setErrorMsg("Task details save unsuccessful");
									RequestContext.getCurrentInstance().update("requiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
								
							} else {
								setErrorMsg("Failed to save.");
								RequestContext.getCurrentInstance().update("requiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						}

						else {

							setErrorMsg("Please select all mandatory fields .");
							RequestContext.getCurrentInstance().update("requiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						if (sisuSariyaDTO.getLanguageDes().equals("SINHALA")) {
							if (sisuSariyaDTO.getNameOfOperatorSin() != null
									&& !sisuSariyaDTO.getNameOfOperatorSin().trim().isEmpty()
									&& sisuSariyaDTO.getAddressOneSin() != null
									&& !sisuSariyaDTO.getAddressOneSin().trim().isEmpty()
									&& sisuSariyaDTO.getAdressTwoSin() != null
									&& !sisuSariyaDTO.getAdressTwoSin().trim().isEmpty()
									&& sisuSariyaDTO.getCitySin() != null
									&& !sisuSariyaDTO.getCitySin().trim().isEmpty()
									&& sisuSariyaDTO.getSchoolNameSin() != null
									&& !sisuSariyaDTO.getSchoolNameSin().trim().isEmpty()
									&& sisuSariyaDTO.getSchoolAdrressOneSin() != null
									&& !sisuSariyaDTO.getSchoolAdrressOneSin().trim().isEmpty()
									&& sisuSariyaDTO.getSchoolAddressTwoSin() != null
									&& !sisuSariyaDTO.getSchoolAddressTwoSin().trim().isEmpty()
									&& sisuSariyaDTO.getSchoolCitySin() != null
									&& !sisuSariyaDTO.getSchoolCitySin().trim().isEmpty()
									&& sisuSariyaDTO.getNicNo() != null && !sisuSariyaDTO.getNicNo().trim().isEmpty())

							{

								boolean success = sisuSariyaService.saveRequestSisusariData(sisuSariyaDTO, loginUser);

								if (success) {
									setSuccessMessage("Succesfully saved.");
									RequestContext.getCurrentInstance().update("successMSG");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									activeTabIndex = 1;

									boolean insert = commonService.insertTaskDetailsSubsidy(sisuSariyaDTO.getRequestNo(), null,
											sessionBackingBean.getLoginUser(), "SS001", "O", null);
									boolean update = commonService.updateTaskStatusCompletedSubsidyTaskTabel(
											sisuSariyaDTO.getRequestNo(), null, null, "SS001", "C");
									
									if(!insert || !update) {
										setErrorMsg("Task details save unsuccessful");
										RequestContext.getCurrentInstance().update("requiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									setErrorMsg("Failed to save.");
									RequestContext.getCurrentInstance().update("requiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								setErrorMsg("Please Enter all mandatory fields in sinhala language.");
								RequestContext.getCurrentInstance().update("requiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							}

						} else {

							if (sisuSariyaDTO.getLanguageDes().equalsIgnoreCase("TAMIL")) {
								if (sisuSariyaDTO.getNameOfOperatorTamil() != null
										&& !sisuSariyaDTO.getNameOfOperatorTamil().trim().isEmpty()
										&& sisuSariyaDTO.getAddressOneTamil() != null
										&& !sisuSariyaDTO.getAddressOneTamil().trim().isEmpty()
										&& sisuSariyaDTO.getAdressTwoTamil() != null
										&& !sisuSariyaDTO.getAdressTwoTamil().trim().isEmpty()
										&& sisuSariyaDTO.getCityTamil() != null
										&& !sisuSariyaDTO.getCityTamil().trim().isEmpty()
										&& sisuSariyaDTO.getSchoolNameTamil() != null
										&& !sisuSariyaDTO.getSchoolNameTamil().trim().isEmpty()
										&& sisuSariyaDTO.getSchoolAddressOneTamil() != null
										&& !sisuSariyaDTO.getSchoolAddressOneTamil().trim().isEmpty()
										&& sisuSariyaDTO.getSchoolAddressTwoTamil() != null
										&& !sisuSariyaDTO.getSchoolAddressTwoTamil().trim().isEmpty()
										&& sisuSariyaDTO.getSchoolCityTamil() != null
										&& !sisuSariyaDTO.getSchoolCityTamil().trim().isEmpty()
										&& sisuSariyaDTO.getNicNo() != null
										&& !sisuSariyaDTO.getNicNo().trim().isEmpty()) {

									boolean success = sisuSariyaService.saveRequestSisusariData(sisuSariyaDTO,
											loginUser);

									if (success) {
										setSuccessMessage("Succesfully saved.");
										RequestContext.getCurrentInstance().update("successMSG");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
										activeTabIndex = 1;

										boolean insert = commonService.insertTaskDetailsSubsidy(sisuSariyaDTO.getRequestNo(), null,
												sessionBackingBean.getLoginUser(), "SS001", "O", null);
										boolean update = commonService.updateTaskStatusCompletedSubsidyTaskTabel(
												sisuSariyaDTO.getRequestNo(), null, null, "SS001", "C");
										
										if(!insert || !update) {
											setErrorMsg("Task details save unsuccessful");
											RequestContext.getCurrentInstance().update("requiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										setErrorMsg("Failed to save.");
										RequestContext.getCurrentInstance().update("requiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								}

								else {
									setErrorMsg("Please Enter all mandatory fields in tamil language.");
									RequestContext.getCurrentInstance().update("requiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

								}

							}

						}
					}
				}

				else {
					setErrorMsg("Please select a Preffer Language");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				setErrorMsg("Please select a Request Date.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else {
			setErrorMsg("Please select a Requestor Type.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void onProvinceChange() {
		if (sisuSariyaDTO.getSchoolProvinceCode() != null && !sisuSariyaDTO.getSchoolProvinceCode().isEmpty()) {
			String strSelectedProvince;
			strSelectedProvince = sisuSariyaDTO.getSchoolProvinceCode();

			districtDisable = false;
			drpdDistrictList = adminService.getDistrictByProvinceToDropdown(strSelectedProvince);

		}
	}

	public void onDistrictChange() {
		if (sisuSariyaDTO.getSchoolDistrictCode() != null && !sisuSariyaDTO.getSchoolDistrictCode().isEmpty()) {
			String strSelectedDistrict;
			strSelectedDistrict = sisuSariyaDTO.getSchoolDistrictCode();
			districDivSectDisable = false;
			drpdDevsecList = adminService.getDivSecByDistrictToDropdown(strSelectedDistrict);
		}
	}

	public void clearFields() {
		sisuSariyaDTO = new SisuSeriyaDTO();
		activeTabIndex = 0;
		districtDisable = true;
		districDivSectDisable = true;

	}

	public void addButton() {
		if (sisuSariyaDTO.getLanguageDes().equalsIgnoreCase("ENGLISH")) {
			if (sisuSariyaDTO.getNoOfPassengers() != null && !sisuSariyaDTO.getNoOfPassengers().trim().isEmpty()
					&& sisuSariyaDTO.getOriginDes() != null && !sisuSariyaDTO.getOriginDes().trim().isEmpty()
					&& sisuSariyaDTO.getDestinationDes() != null && !sisuSariyaDTO.getDestinationDes().trim().isEmpty()
					&& sisuSariyaDTO.getVia() != null && !sisuSariyaDTO.getVia().trim().isEmpty()) {
				boolean success = sisuSariyaService.saveRouteInformationData(sisuSariyaDTO, loginUser);

				if (success) {
					showDataOnGrid = sisuSariyaService.getDataonGrid(sisuSariyaDTO);
					setSuccessMessage("Succesfully Added.");
					RequestContext.getCurrentInstance().update("successMSG");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					sisuSariyaDTO.setNoOfPassengers("");
					sisuSariyaDTO.setOriginDesSin("");
					sisuSariyaDTO.setOriginDes("");
					sisuSariyaDTO.setOriginDesTamil("");
					sisuSariyaDTO.setDestinationDes("");
					sisuSariyaDTO.setDestinationDesSin("");
					sisuSariyaDTO.setDestinationDesTamil("");
					sisuSariyaDTO.setVia("");
					sisuSariyaDTO.setViaSin("");
					sisuSariyaDTO.setViaTamil("");

					boolean checkTask = commonService.checkTaskDetailsInSubsidy(sisuSariyaDTO.getRequestNo(), "SS002",
							"O");
					if (checkTask != true) {
						commonService.updateTaskStatusCompletedSubsidyTaskTabel(sisuSariyaDTO.getRequestNo(), null,
								null, "SS002", "O");
					}
				} else {
					setErrorMsg("Failed to save.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {

				setErrorMsg("Please fill all mandatory fields.");
				RequestContext.getCurrentInstance().update("requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			if (sisuSariyaDTO.getLanguageDes().equalsIgnoreCase("SINHALA")) {
				if (sisuSariyaDTO.getNoOfPassengers() != null && !sisuSariyaDTO.getNoOfPassengers().trim().isEmpty()
						&& sisuSariyaDTO.getOriginDesSin() != null && !sisuSariyaDTO.getOriginDesSin().trim().isEmpty()
						&& sisuSariyaDTO.getDestinationDesSin() != null
						&& !sisuSariyaDTO.getDestinationDesSin().trim().isEmpty() && sisuSariyaDTO.getVia() != null
						&& !sisuSariyaDTO.getVia().trim().isEmpty()) {

					boolean success = sisuSariyaService.saveRouteInformationData(sisuSariyaDTO, loginUser);

					if (success) {
						showDataOnGrid = sisuSariyaService.getDataonGrid(sisuSariyaDTO);
						setSuccessMessage("Succesfully Added.");
						RequestContext.getCurrentInstance().update("successMSG");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						sisuSariyaDTO.setNoOfPassengers("");
						sisuSariyaDTO.setOriginDesSin("");
						sisuSariyaDTO.setOriginDes("");
						sisuSariyaDTO.setOriginDesTamil("");
						sisuSariyaDTO.setDestinationDes("");
						sisuSariyaDTO.setDestinationDesSin("");
						sisuSariyaDTO.setDestinationDesTamil("");
						sisuSariyaDTO.setVia("");
						sisuSariyaDTO.setViaSin("");
						sisuSariyaDTO.setViaTamil("");

						boolean checkTask = commonService.checkTaskDetailsInSubsidy(sisuSariyaDTO.getRequestNo(),
								"SS002", "O");
						if (checkTask != true) {
							commonService.updateTaskStatusCompletedSubsidyTaskTabel(sisuSariyaDTO.getRequestNo(), null,
									null, "SS002", "O");
						}
					} else {
						setErrorMsg("Failed to save.");
						RequestContext.getCurrentInstance().update("requiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					setErrorMsg("Please fill all mandatory fields in sinhala language.");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				if (sisuSariyaDTO.getLanguageDes().equalsIgnoreCase("TAMIL")) {

					if (sisuSariyaDTO.getNoOfPassengers() != null && !sisuSariyaDTO.getNoOfPassengers().trim().isEmpty()
							&& sisuSariyaDTO.getOriginDesTamil() != null
							&& !sisuSariyaDTO.getOriginDesTamil().trim().isEmpty()
							&& sisuSariyaDTO.getDestinationDesTamil() != null
							&& !sisuSariyaDTO.getDestinationDesTamil().trim().isEmpty()
							&& sisuSariyaDTO.getViaTamil() != null && !sisuSariyaDTO.getViaTamil().trim().isEmpty()) {
						boolean success = sisuSariyaService.saveRouteInformationData(sisuSariyaDTO, loginUser);

						if (success) {
							showDataOnGrid = sisuSariyaService.getDataonGrid(sisuSariyaDTO);
							setSuccessMessage("Succesfully Added.");
							RequestContext.getCurrentInstance().update("successMSG");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							sisuSariyaDTO.setNoOfPassengers("");
							sisuSariyaDTO.setOriginDesSin("");
							sisuSariyaDTO.setOriginDes("");
							sisuSariyaDTO.setOriginDesTamil("");
							sisuSariyaDTO.setDestinationDes("");
							sisuSariyaDTO.setDestinationDesSin("");
							sisuSariyaDTO.setDestinationDesTamil("");
							sisuSariyaDTO.setVia("");
							sisuSariyaDTO.setViaSin("");
							sisuSariyaDTO.setViaTamil("");

							boolean checkTask = commonService.checkTaskDetailsInSubsidy(sisuSariyaDTO.getRequestNo(),
									"SS002", "O");
							if (checkTask != true) {
								commonService.updateTaskStatusCompletedSubsidyTaskTabel(sisuSariyaDTO.getRequestNo(),
										null, null, "SS002", "O");
							}
						} else {
							setErrorMsg("Failed to save.");
							RequestContext.getCurrentInstance().update("requiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {

						setErrorMsg("Please fill all mandatory fields in tamil language.");
						RequestContext.getCurrentInstance().update("requiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {

				}

			}

		}

	}

	public void deleteButton() {
		if (selectedRow != null) {

			sisuSariyaService.deleteDataFromGrid(sisuSariyaDTO, selectedRow.getOriginDes(),
					selectedRow.getDestinationDes(), selectedRow.getVia(), selectedRow.getNoOfPassengers());
			showDataOnGrid = sisuSariyaService.getDataonGrid(sisuSariyaDTO);
			setSuccessMessage("Succesfully Deleted.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		} else {

			setErrorMsg("Please select a data raw.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void documentManagement() {

		try {

			sessionBackingBean.setRequestNoForSisuSariya(sisuSariyaDTO.getRequestNo());
			sessionBackingBean.setTransactionType("SISU SARIYA");

			mandatoryList = documentManagementService.mandatoryDocsForSisuSariya("18", sisuSariyaDTO.getRequestNo());
			optionalList = documentManagementService.optionalDocsForSisuSariya("18", sisuSariyaDTO.getRequestNo());

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.sisuSariyaMandatoryList(sisuSariyaDTO.getRequestNo());
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.sisuSariyaOptionalList(sisuSariyaDTO.getRequestNo());

			if (sisuSariyaDTO.getServiceRefNo() != null && sisuSariyaDTO.getRequestNo() != null) {

				sessionBackingBean.sisuSariyaServiceRefMandatoryDocumentList = documentManagementService
						.sisuSariyaPermitHolderMandatoryList(sisuSariyaDTO.getRequestNo(),
								sisuSariyaDTO.getServiceRefNo());
				sessionBackingBean.sisuSariyaServiceRefOptionalDocumentList = documentManagementService
						.sisuSariyaPermitHolderOptionalList(sisuSariyaDTO.getRequestNo(),
								sisuSariyaDTO.getServiceRefNo());
			}

			if (sisuSariyaDTO.getServiceNo() != null && sisuSariyaDTO.getServiceRefNo() != null
					&& sisuSariyaDTO.getRequestNo() != null) {

				sessionBackingBean.sisuSariyaServiceMandatoryDocumentList = documentManagementService
						.sisuSariyaAgreementRenewalsMandatoryList(sisuSariyaDTO.getRequestNo(),
								sisuSariyaDTO.getServiceRefNo(), sisuSariyaDTO.getServiceNo());
				sessionBackingBean.sisuSariyaServiceOptionalDocumentList = documentManagementService
						.sisuSariyaAgreementRenewalsOptionalList(sisuSariyaDTO.getRequestNo(),
								sisuSariyaDTO.getServiceRefNo(), sisuSariyaDTO.getServiceNo());

			}

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void editButton() {
		sisuSariyaDTO.setOriginDes(selectedRow.getOriginDes());

		sisuSariyaDTO.setDestinationDes(selectedRow.getDestinationDes());

		sisuSariyaDTO.setVia(selectedRow.getVia());
		sisuSariyaDTO.setNoOfPassengers(selectedRow.getNoOfPassengers());

	}

	public void clearAddDet() {
		sisuSariyaDTO.setOriginDes("");
		sisuSariyaDTO.setOriginDesSin("");
		sisuSariyaDTO.setOriginDesTamil("");
		sisuSariyaDTO.setDestinationCode("");
		sisuSariyaDTO.setDestinationDes(" ");
		sisuSariyaDTO.setDestinationDesSin("");
		sisuSariyaDTO.setDestinationDesTamil("");
		sisuSariyaDTO.setNoOfPassengers("");
		sisuSariyaDTO.setVia("");
		sisuSariyaDTO.setViaSin("");
		sisuSariyaDTO.setViaTamil("");

	}

	public void clearAddDet1() {

		sisuSariyaDTO.setOriginDes("");
		sisuSariyaDTO.setOriginDesSin("");
		sisuSariyaDTO.setOriginDesTamil("");
		sisuSariyaDTO.setDestinationCode("");
		sisuSariyaDTO.setDestinationDes(" ");
		sisuSariyaDTO.setDestinationDesSin("");
		sisuSariyaDTO.setDestinationDesTamil("");
		sisuSariyaDTO.setNoOfPassengers("");
		sisuSariyaDTO.setVia("");
		sisuSariyaDTO.setViaSin("");
		sisuSariyaDTO.setViaTamil("");
		sisuSariyaDTO.setRequestNo(" ");
		showDataOnGrid = new ArrayList<SisuSeriyaDTO>();

	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public String getSelectedReqNo() {
		return selectedReqNo;
	}

	public void setSelectedReqNo(String selectedReqNo) {
		this.selectedReqNo = selectedReqNo;
	}

	public String getSelectedRequestorType() {
		return selectedRequestorType;
	}

	public void setSelectedRequestorType(String selectedRequestorType) {
		this.selectedRequestorType = selectedRequestorType;
	}

	public String getSelectedPrefLang() {
		return selectedPrefLang;
	}

	public void setSelectedPrefLang(String selectedPrefLang) {
		this.selectedPrefLang = selectedPrefLang;
	}

	public String getSelectedReqType() {
		return selectedReqType;
	}

	public void setSelectedReqType(String selectedReqType) {
		this.selectedReqType = selectedReqType;
	}

	public String getSelectedIdNo() {
		return selectedIdNo;
	}

	public void setSelectedIdNo(String selectedIdNo) {
		this.selectedIdNo = selectedIdNo;
	}

	public List<DocumentManagementDTO> getMandatoryList() {
		return mandatoryList;
	}

	public void setMandatoryList(List<DocumentManagementDTO> mandatoryList) {
		this.mandatoryList = mandatoryList;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public SisuSeriyaDTO getSisuSariyaDTO() {
		return sisuSariyaDTO;
	}

	public void setSisuSariyaDTO(SisuSeriyaDTO sisuSariyaDTO) {
		this.sisuSariyaDTO = sisuSariyaDTO;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

	public List<SisuSeriyaDTO> getRequestTypeList() {
		return requestTypeList;
	}

	public void setRequestTypeList(List<SisuSeriyaDTO> requestTypeList) {
		this.requestTypeList = requestTypeList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public List<SisuSeriyaDTO> getPrefLanguList() {
		return prefLanguList;
	}

	public void setPrefLanguList(List<SisuSeriyaDTO> prefLanguList) {
		this.prefLanguList = prefLanguList;
	}

	public List<SisuSeriyaDTO> getShowDataOnGrid() {
		return showDataOnGrid;
	}

	public void setShowDataOnGrid(List<SisuSeriyaDTO> showDataOnGrid) {
		this.showDataOnGrid = showDataOnGrid;
	}

	public SisuSeriyaDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(SisuSeriyaDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public List<CommonDTO> getDrpdProvincelList() {
		return drpdProvincelList;
	}

	public void setDrpdProvincelList(List<CommonDTO> drpdProvincelList) {
		this.drpdProvincelList = drpdProvincelList;
	}

	public boolean isDistrictDisable() {
		return districtDisable;
	}

	public void setDistrictDisable(boolean districtDisable) {
		this.districtDisable = districtDisable;
	}

	public boolean isDistricDivSectDisable() {
		return districDivSectDisable;
	}

	public void setDistricDivSectDisable(boolean districDivSectDisable) {
		this.districDivSectDisable = districDivSectDisable;
	}

}
