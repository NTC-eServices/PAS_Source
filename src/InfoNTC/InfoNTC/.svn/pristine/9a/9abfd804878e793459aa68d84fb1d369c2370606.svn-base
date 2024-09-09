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

@ManagedBean(name = "viewEditRequestForSisuSariyaBackingBean")
@ViewScoped
public class ViewEditRequestForSisuSariyaBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// services
	private DocumentManagementService documentManagementService;
	private SisuSariyaService sisuSariyaService;
	private AdminService adminService;
	private CommonService commonService;

	// DTO

	private SisuSeriyaDTO sisuSariyaViewDTO;
	private List<SisuSeriyaDTO> requestNoList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> prefLanguList = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> showDataOnGrid;
	private SisuSeriyaDTO selectedRow;
	private SisuSeriyaDTO showDataDTO;;

	private SisuSeriyaDTO showDataFirstTab;
	private List<CommonDTO> drpdProvincelList;
	private List<CommonDTO> drpdDistrictList;
	private List<CommonDTO> drpdDevsecList;

	private String dataPass;
	private boolean editFlag, renderButton;

	public boolean isEditFlag() {
		return editFlag;
	}

	public void setEditFlag(boolean editFlag) {
		this.editFlag = editFlag;
	}

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	private int activeTabIndex;
	private String selectedReqNo, selectedRequestorType, selectedPrefLang, selectedReqType, selectedIdNo, selectAdd1,
			selectNameFull, selectAdd1Sin, selectNameFullSin, selectAdd1Tamil, selectNameFullTamil, selectAdd2,
			selectTelNo, selectAdd2Sin, selectMobNo, selectAdd2Tamil, selectEmail, selectCity, selectCitySin,
			selectCityTamil, selectedSchoolName, selectedSchoolAdd1, selectedSchoolNameSin, selectedSchoolAdd1Sin,
			selectedSchoolNameTamil, selectedSchoolAdd1Tamil, selectedSchoolTelNO, selectedSchoolAdd2,
			selectedSchoolMobNO, selectedSchoolAdd2Sin, selectedSchoolEmail, selectedSchoolAdd2Tamil, selectedProvince,
			selectedSchoolCity, selectedSchoolDistrict, selectedSchoolCitySin, selectedSchoolDivSec,
			selectedSchoolCityTamil, selectNoOfPassen, selectRoutInfoOrigin, selectRoutInfoDestina,
			selectRoutInfoOriginSinhala, selectRoutInfoDestiSinhala, selectRoutInfoOriginTamil,
			selectRoutInfoDestinaTamil, selectRoutInfoVia, selectRoutInfoViaSin, selectRoutInfoViaTamil,
			selectRequestNO, successMessage, errorMsg, loginUser, selectReqNo;

	@PostConstruct
	public void init() {
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		sisuSariyaViewDTO = new SisuSeriyaDTO();
		requestNoList = sisuSariyaService.getRequesNoDropDown();

		prefLanguList = sisuSariyaService.getPrefLanguForDropDown();
		loginUser = sessionBackingBean.getLoginUser();

		drpdProvincelList = adminService.getProvinceToDropdown();
		drpdDevsecList = adminService.getDivSecToDropdown();
		drpdDistrictList = adminService.getDistrictToDropdown();

		if (commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN101", "E")) {
			renderButton = true;
		} else if (commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN101", "V")) {
			renderButton = false;
		}

	}

	public void searchButton() {

		if (sisuSariyaViewDTO.getRequestNo() != null && !sisuSariyaViewDTO.getRequestNo().trim().isEmpty()
				&& !sisuSariyaViewDTO.getRequestNo().equals("")) {

			showDataDTO = sisuSariyaService.showData(sisuSariyaViewDTO);
			showDataOnGrid = sisuSariyaService.getDataonGrid(sisuSariyaViewDTO);
			selectReqNo = sisuSariyaViewDTO.getRequestNo();

			showDataFirstTab = sisuSariyaService.showDataFirstTab(sisuSariyaViewDTO);

			selectedReqType = showDataFirstTab.getRequestorTypeDes();
			selectedPrefLang = showDataFirstTab.getLanguageDes();
			selectedIdNo = showDataFirstTab.getNicNo();
			selectAdd1 = showDataFirstTab.getAddressOne();
			selectNameFull = showDataFirstTab.getNameOfOperator();
			selectAdd1Sin = showDataFirstTab.getAddressOneSin();
			selectNameFullSin = showDataFirstTab.getNameOfOperatorSin();

			selectAdd1Tamil = showDataFirstTab.getSchoolAddressOneTamil();
			selectNameFullTamil = showDataFirstTab.getNameOfOperatorTamil();
			selectAdd2 = showDataFirstTab.getAddressTwo();
			selectTelNo = showDataFirstTab.getTelNo();
			selectAdd2Sin = showDataFirstTab.getAdressTwoSin();
			selectMobNo = showDataFirstTab.getMobileNo();
			selectAdd2Tamil = showDataFirstTab.getAdressTwoTamil();
			selectEmail = showDataFirstTab.getEmail();
			selectCity = showDataFirstTab.getCity();
			selectCitySin = showDataFirstTab.getCitySin();

			selectCityTamil = showDataFirstTab.getSchoolCityTamil();

			selectedSchoolName = showDataFirstTab.getSchoolName();
			selectedSchoolAdd1 = showDataFirstTab.getSchoolAddressOne();
			selectedSchoolNameSin = showDataFirstTab.getSchoolNameSin();
			selectedSchoolAdd1Sin = showDataFirstTab.getSchoolAdrressOneSin();
			selectedSchoolNameTamil = showDataFirstTab.getSchoolNameTamil();
			selectedSchoolAdd1Tamil = showDataFirstTab.getSchoolAddressOneTamil();
			selectedSchoolTelNO = showDataFirstTab.getSchoolTelNo();
			selectedSchoolAdd2 = showDataFirstTab.getSchoolAddressTwo();
			selectedSchoolMobNO = showDataFirstTab.getSchoolMobileNo();
			selectedSchoolAdd2Sin = showDataFirstTab.getSchoolAddressTwoSin();
			selectedSchoolEmail = showDataFirstTab.getSchoolEmailAdd();
			selectedSchoolAdd2Tamil = showDataFirstTab.getSchoolAddressTwoTamil();
			selectedProvince = showDataFirstTab.getSchoolProvinceDes();
			selectedSchoolCity = showDataFirstTab.getSchoolCity();
			selectedSchoolDistrict = showDataFirstTab.getSchoolDistrictDes();
			selectedSchoolCitySin = showDataFirstTab.getSchoolCitySin();
			selectedSchoolDivSec = showDataFirstTab.getSchoolDivisionSecDes();
			selectedSchoolCityTamil = showDataFirstTab.getSchoolCityTamil();

		}

		else {

			setErrorMsg("Please select a Request No.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public String getDataPass() {
		return dataPass;
	}

	public void setDataPass(String dataPass) {
		this.dataPass = dataPass;
	}

	public void updateRecord() {

		sisuSariyaViewDTO.setNameOfOperator(selectNameFull);
		sisuSariyaViewDTO.setNicNo(selectedIdNo);

		sisuSariyaViewDTO.setNameOfOperatorSin(selectNameFullSin);
		sisuSariyaViewDTO.setNameOfOperatorTamil(selectNameFullTamil);
		sisuSariyaViewDTO.setAddressOne(selectAdd1);
		sisuSariyaViewDTO.setAddressOneSin(selectAdd1Sin);
		sisuSariyaViewDTO.setAddressOneTamil(selectAdd1Tamil);
		sisuSariyaViewDTO.setAddressTwo(selectAdd2);
		sisuSariyaViewDTO.setAddressTwo(selectAdd2Sin);
		sisuSariyaViewDTO.setAddressTwo(selectAdd2Tamil);
		sisuSariyaViewDTO.setCity(selectCity);

		sisuSariyaViewDTO.setCitySin(selectCitySin);
		sisuSariyaViewDTO.setCityTamil(selectCityTamil);
		sisuSariyaViewDTO.setMobileNo(selectMobNo);
		sisuSariyaViewDTO.setTelNo(selectTelNo);
		sisuSariyaViewDTO.setEmail(selectEmail);

		sisuSariyaViewDTO.setLanguageDes(selectedPrefLang);
		sisuSariyaViewDTO.setSchoolName(selectedSchoolName);
		sisuSariyaViewDTO.setSchoolNameSin(selectedSchoolNameSin);
		sisuSariyaViewDTO.setSchoolNameTamil(selectedSchoolNameTamil);
		sisuSariyaViewDTO.setSchoolAddressOne(selectedSchoolAdd1);
		sisuSariyaViewDTO.setSchoolAdrressOneSin(selectedSchoolAdd1Sin);
		sisuSariyaViewDTO.setSchoolAddressOneTamil(selectedSchoolAdd1Tamil);
		sisuSariyaViewDTO.setSchoolAddressTwo(selectedSchoolAdd2);
		sisuSariyaViewDTO.setSchoolAddressTwoSin(selectedSchoolAdd2Sin);
		sisuSariyaViewDTO.setSchoolAddressTwoTamil(selectedSchoolAdd2Tamil);
		sisuSariyaViewDTO.setSchoolCity(selectedSchoolCity);
		sisuSariyaViewDTO.setSchoolCitySin(selectedSchoolCitySin);
		sisuSariyaViewDTO.setSchoolCityTamil(selectedSchoolCityTamil);

		sisuSariyaViewDTO.setSchoolProvinceCode(selectedProvince);
		sisuSariyaViewDTO.setSchoolDistrictCode(selectedSchoolDistrict);
		sisuSariyaViewDTO.setSchoolDivisinSecCode(selectedSchoolDivSec);
		sisuSariyaViewDTO.setSchoolTelNo(selectedSchoolTelNO);
		sisuSariyaViewDTO.setSchoolMobileNo(selectedSchoolMobNO);
		sisuSariyaViewDTO.setSchoolEmailAdd(selectedSchoolEmail);

		sisuSariyaService.updateRequestSisusariData(sisuSariyaViewDTO);

		searchButton();
		setSuccessMessage("Succesfully Updated.");
		RequestContext.getCurrentInstance().update("successMSG");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

	}

	public void onProvinceChange() {
		if (selectedProvince != null && !selectedProvince.isEmpty()) {
			String strSelectedProvinceCode;
			strSelectedProvinceCode = selectedProvince;
			drpdDistrictList = adminService.getDistrictByProvinceToDropdown(strSelectedProvinceCode);
		}
	}

	public void onDistrictChange() {
		if (selectedSchoolDistrict != null && !selectedSchoolDistrict.isEmpty()) {
			String strSelectedDistrictCode;
			strSelectedDistrictCode = selectedSchoolDistrict;
			drpdDevsecList = adminService.getDivSecByDistrictToDropdown(strSelectedDistrictCode);
		}
	}

	public void saveRecord() {

		boolean success = sisuSariyaService.saveRequestSisusariData(sisuSariyaViewDTO, loginUser);
		if(success) {
			setSuccessMessage("Succesfully saved.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
		}else {
			setErrorMsg("Failed to save.");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		
	}

	public void clearFields() {
		sisuSariyaViewDTO = new SisuSeriyaDTO();
		clearAddDet();
	}

	public void addButton() {

		if (sisuSariyaViewDTO.getRequestNo() != null && !sisuSariyaViewDTO.getRequestNo().trim().isEmpty()) {
			if (sisuSariyaService.isReqNoExist(sisuSariyaViewDTO) == true) {

				if (editFlag) {
					sisuSariyaViewDTO.setNoOfPassengers(selectNoOfPassen);

					sisuSariyaViewDTO.setOriginDes(selectRoutInfoOrigin);
					sisuSariyaViewDTO.setDestinationDes(selectRoutInfoDestina);
					sisuSariyaViewDTO.setOriginDesSin(selectRoutInfoOriginSinhala);
					sisuSariyaViewDTO.setDestinationDesSin(selectRoutInfoDestiSinhala);
					sisuSariyaViewDTO.setOriginDesTamil(selectRoutInfoOriginTamil);
					sisuSariyaViewDTO.setDestinationDesTamil(selectRoutInfoDestinaTamil);
					sisuSariyaViewDTO.setVia(selectRoutInfoVia);
					sisuSariyaViewDTO.setViaSin(selectRoutInfoViaSin);
					sisuSariyaViewDTO.setViaTamil(selectRoutInfoViaTamil);

					String editOrigin = selectedRow.getOriginDes();
					String editDestination = selectedRow.getDestinationDes();
					String editVia = selectedRow.getVia();
					String editNoOfPaassen = selectedRow.getNoOfPassengers();

					sisuSariyaService.updateRouteInformationData(sisuSariyaViewDTO, editOrigin, editDestination,
							editVia, editNoOfPaassen);
					showDataOnGrid = sisuSariyaService.getDataonGrid(sisuSariyaViewDTO);
					setSuccessMessage("Succesfully Added.");
					RequestContext.getCurrentInstance().update("successMSG");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					sisuSariyaViewDTO.setNoOfPassengers(selectNoOfPassen);

					selectNoOfPassen = null;
					selectRoutInfoOrigin = null;
					selectRoutInfoDestina = null;
					selectRoutInfoOriginSinhala = null;
					selectRoutInfoDestiSinhala = null;
					selectRoutInfoOriginTamil = null;
					selectRoutInfoDestinaTamil = null;
					selectRoutInfoVia = null;
					selectRoutInfoViaSin = null;
					selectRoutInfoViaTamil = null;
					editFlag = false;
				} else {
					// not edit mode but insert new record for existing request number
					if (selectReqNo != null && !selectReqNo.trim().isEmpty()) {
						if ((selectNoOfPassen != null && !selectNoOfPassen.trim().isEmpty())
								|| (selectRoutInfoOrigin != null && !selectRoutInfoOrigin.trim().isEmpty())
								|| (selectRoutInfoOriginSinhala != null
										&& !selectRoutInfoOriginSinhala.trim().isEmpty())
								|| (selectRoutInfoOriginTamil != null && !selectRoutInfoOriginTamil.trim().isEmpty())
								|| (selectRoutInfoDestina != null && !selectRoutInfoDestina.trim().isEmpty())
								|| (selectRoutInfoDestiSinhala != null && !selectRoutInfoDestiSinhala.trim().isEmpty())
								|| (selectRoutInfoDestinaTamil != null && !selectRoutInfoDestinaTamil.trim().isEmpty())
								|| (selectRoutInfoVia != null && !selectRoutInfoVia.trim().isEmpty())
								|| (selectRoutInfoViaSin != null && !selectRoutInfoViaSin.trim().isEmpty())
								|| (selectRoutInfoViaTamil != null && !selectRoutInfoViaTamil.trim().isEmpty())) {

							sisuSariyaViewDTO.setNoOfPassengers(selectNoOfPassen);

							sisuSariyaViewDTO.setOriginDes(selectRoutInfoOrigin);
							sisuSariyaViewDTO.setDestinationDes(selectRoutInfoDestina);
							sisuSariyaViewDTO.setOriginDesSin(selectRoutInfoOriginSinhala);
							sisuSariyaViewDTO.setDestinationDesSin(selectRoutInfoDestiSinhala);
							sisuSariyaViewDTO.setOriginDesTamil(selectRoutInfoOriginTamil);
							sisuSariyaViewDTO.setDestinationDesTamil(selectRoutInfoDestinaTamil);
							sisuSariyaViewDTO.setVia(selectRoutInfoVia);
							sisuSariyaViewDTO.setViaSin(selectRoutInfoViaSin);
							sisuSariyaViewDTO.setViaTamil(selectRoutInfoViaTamil);

							sisuSariyaService.saveRouteInformationData(sisuSariyaViewDTO, loginUser);
							showDataOnGrid = sisuSariyaService.getDataonGrid(sisuSariyaViewDTO);
							setSuccessMessage("Succesfully Added.");
							RequestContext.getCurrentInstance().update("successMSG");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							selectNoOfPassen = null;
							selectRoutInfoOrigin = null;
							selectRoutInfoDestina = null;
							selectRoutInfoOriginSinhala = null;
							selectRoutInfoDestiSinhala = null;
							selectRoutInfoOriginTamil = null;
							selectRoutInfoDestinaTamil = null;
							selectRoutInfoVia = null;
							selectRoutInfoViaSin = null;
							selectRoutInfoViaTamil = null;
						} else {
							setErrorMsg("Please fill data for added");
							RequestContext.getCurrentInstance().update("requiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						setErrorMsg("Please fill Request No.");
						RequestContext.getCurrentInstance().update("requiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

					}

				}

			} else {
				if ((selectNoOfPassen != null && !selectNoOfPassen.trim().isEmpty())
						|| (selectRoutInfoOrigin != null && !selectRoutInfoOrigin.trim().isEmpty())
						|| (selectRoutInfoOriginSinhala != null && !selectRoutInfoOriginSinhala.trim().isEmpty())
						|| (selectRoutInfoOriginTamil != null && !selectRoutInfoOriginTamil.trim().isEmpty())
						|| (selectRoutInfoDestina != null && !selectRoutInfoDestina.trim().isEmpty())
						|| (selectRoutInfoDestiSinhala != null && !selectRoutInfoDestiSinhala.trim().isEmpty())
						|| (selectRoutInfoDestinaTamil != null && !selectRoutInfoDestinaTamil.trim().isEmpty())
						|| (selectRoutInfoVia != null && !selectRoutInfoVia.trim().isEmpty())
						|| (selectRoutInfoViaSin != null && !selectRoutInfoViaSin.trim().isEmpty())
						|| (selectRoutInfoViaTamil != null && !selectRoutInfoViaTamil.trim().isEmpty())) {
					sisuSariyaViewDTO.setNoOfPassengers(selectNoOfPassen);

					sisuSariyaViewDTO.setOriginDes(selectRoutInfoOrigin);
					sisuSariyaViewDTO.setDestinationDes(selectRoutInfoDestina);
					sisuSariyaViewDTO.setOriginDesSin(selectRoutInfoOriginSinhala);
					sisuSariyaViewDTO.setDestinationDesSin(selectRoutInfoDestiSinhala);
					sisuSariyaViewDTO.setOriginDesTamil(selectRoutInfoOriginTamil);
					sisuSariyaViewDTO.setDestinationDesTamil(selectRoutInfoDestinaTamil);
					sisuSariyaViewDTO.setVia(selectRoutInfoVia);
					sisuSariyaViewDTO.setViaSin(selectRoutInfoViaSin);
					sisuSariyaViewDTO.setViaTamil(selectRoutInfoViaTamil);

					sisuSariyaService.saveRouteInformationData(sisuSariyaViewDTO, loginUser);
					showDataOnGrid = sisuSariyaService.getDataonGrid(sisuSariyaViewDTO);
					setSuccessMessage("Succesfully Added.");
					RequestContext.getCurrentInstance().update("successMSG");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

					selectNoOfPassen = null;
					selectRoutInfoOrigin = null;
					selectRoutInfoDestina = null;
					selectRoutInfoOriginSinhala = null;
					selectRoutInfoDestiSinhala = null;
					selectRoutInfoOriginTamil = null;
					selectRoutInfoDestinaTamil = null;
					selectRoutInfoVia = null;
					selectRoutInfoViaSin = null;
					selectRoutInfoViaTamil = null;
				} else {
					setErrorMsg("Please fill data for added");
					RequestContext.getCurrentInstance().update("requiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			}
		} else {
			setErrorMsg("Without Requst No. can not add data");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}
	}

	public void deleteButton() {
		if (selectedRow != null) {
			sisuSariyaViewDTO.setNoOfPassengers(selectNoOfPassen);

			sisuSariyaViewDTO.setOriginDes(selectRoutInfoOrigin);
			sisuSariyaViewDTO.setDestinationDes(selectRoutInfoDestina);
			sisuSariyaViewDTO.setOriginDesSin(selectRoutInfoOriginSinhala);
			sisuSariyaViewDTO.setDestinationDesSin(selectRoutInfoDestiSinhala);
			sisuSariyaViewDTO.setOriginDesTamil(selectRoutInfoOriginTamil);
			sisuSariyaViewDTO.setDestinationDesTamil(selectRoutInfoDestinaTamil);
			sisuSariyaViewDTO.setVia(selectRoutInfoVia);
			sisuSariyaViewDTO.setViaSin(selectRoutInfoViaSin);
			sisuSariyaViewDTO.setViaTamil(selectRoutInfoViaTamil);

			sisuSariyaService.deleteDataFromGrid(sisuSariyaViewDTO, selectedRow.getOriginDes(),
					selectedRow.getDestinationDes(), selectedRow.getVia(), selectedRow.getNoOfPassengers());
			showDataOnGrid = sisuSariyaService.getDataonGrid(sisuSariyaViewDTO);
			setSuccessMessage("Succesfully Deleted.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			selectNoOfPassen = null;
			selectRoutInfoOrigin = null;
			selectRoutInfoDestina = null;
			selectRoutInfoOriginSinhala = null;
			selectRoutInfoDestiSinhala = null;
			selectRoutInfoOriginTamil = null;
			selectRoutInfoDestinaTamil = null;
			selectRoutInfoVia = null;
			selectRoutInfoViaSin = null;
			selectRoutInfoViaTamil = null;

		} else {

			setErrorMsg("Please select a data row");
			RequestContext.getCurrentInstance().update("requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void editButton() {
		selectRoutInfoOrigin = selectedRow.getOriginDes();
		selectRoutInfoDestina = selectedRow.getDestinationDes();

		selectRoutInfoVia = selectedRow.getVia();

		selectNoOfPassen = selectedRow.getNoOfPassengers();

		showDataDTO = sisuSariyaService.showData(sisuSariyaViewDTO);

		selectRoutInfoOriginSinhala = selectedRow.getOriginDesSin();
		selectRoutInfoOriginTamil = selectedRow.getOriginDesTamil();
		selectRoutInfoDestiSinhala = selectedRow.getDestinationDesSin();
		selectRoutInfoDestinaTamil = selectedRow.getDestinationDesTamil();
		selectRoutInfoViaSin = selectedRow.getViaSin();
		selectRoutInfoViaTamil = selectedRow.getViaTamil();

		editFlag = true;

	}

	public void documentManagement() {

		try {

			sessionBackingBean.setRequestNoForSisuSariya(sisuSariyaViewDTO.getRequestNo());
			sessionBackingBean.setTransactionType("SISU SARIYA");

			mandatoryList = documentManagementService.mandatoryDocsForSisuSariya("18",
					sisuSariyaViewDTO.getRequestNo());
			optionalList = documentManagementService.optionalDocsForSisuSariya("18", sisuSariyaViewDTO.getRequestNo());

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.sisuSariyaMandatoryList(sisuSariyaViewDTO.getRequestNo());
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.sisuSariyaOptionalList(sisuSariyaViewDTO.getRequestNo());

			if (sisuSariyaViewDTO.getServiceRefNo() != null && sisuSariyaViewDTO.getRequestNo() != null) {

				sessionBackingBean.sisuSariyaServiceRefMandatoryDocumentList = documentManagementService
						.sisuSariyaPermitHolderMandatoryList(sisuSariyaViewDTO.getRequestNo(),
								sisuSariyaViewDTO.getServiceRefNo());
				sessionBackingBean.sisuSariyaServiceRefOptionalDocumentList = documentManagementService
						.sisuSariyaPermitHolderOptionalList(sisuSariyaViewDTO.getRequestNo(),
								sisuSariyaViewDTO.getServiceRefNo());
			}

			if (sisuSariyaViewDTO.getServiceNo() != null && sisuSariyaViewDTO.getServiceRefNo() != null
					&& sisuSariyaViewDTO.getRequestNo() != null) {

				sessionBackingBean.sisuSariyaServiceMandatoryDocumentList = documentManagementService
						.sisuSariyaAgreementRenewalsMandatoryList(sisuSariyaViewDTO.getRequestNo(),
								sisuSariyaViewDTO.getServiceRefNo(), sisuSariyaViewDTO.getServiceNo());
				sessionBackingBean.sisuSariyaServiceOptionalDocumentList = documentManagementService
						.sisuSariyaAgreementRenewalsOptionalList(sisuSariyaViewDTO.getRequestNo(),
								sisuSariyaViewDTO.getServiceRefNo(), sisuSariyaViewDTO.getServiceNo());

			}

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void clearAddDet() {

		sisuSariyaViewDTO = new SisuSeriyaDTO();

		selectNoOfPassen = null;
		selectReqNo = null;
		selectRoutInfoOrigin = null;
		selectRoutInfoDestina = null;
		selectRoutInfoOriginSinhala = null;
		selectRoutInfoDestiSinhala = null;
		selectRoutInfoOriginTamil = null;
		selectRoutInfoDestinaTamil = null;
		selectRoutInfoVia = null;
		selectRoutInfoViaSin = null;
		selectRoutInfoViaTamil = null;

		// requstor info
		selectedReqType = null;
		selectedPrefLang = null;

		selectedIdNo = null;
		selectAdd1 = null;
		selectNameFull = null;
		selectAdd1Sin = null;
		selectNameFullSin = null;
		selectAdd1Tamil = null;
		selectNameFullTamil = null;
		selectAdd2 = null;
		selectTelNo = null;
		selectAdd2Sin = null;
		selectMobNo = null;
		selectAdd2Tamil = null;
		selectEmail = null;
		selectCity = null;

		selectCitySin = null;
		selectCityTamil = null;

		// school info

		selectedSchoolName = null;
		selectedSchoolAdd1 = null;
		selectedSchoolNameSin = null;
		selectedSchoolAdd1Sin = null;
		selectedSchoolNameTamil = null;
		selectedSchoolAdd1Tamil = null;
		selectedSchoolTelNO = null;
		selectedSchoolAdd2 = null;
		selectedSchoolMobNO = null;
		selectedSchoolAdd2Sin = null;
		selectedSchoolEmail = null;
		selectedSchoolAdd2Tamil = null;
		selectedProvince = null;
		selectedSchoolCity = null;
		selectedSchoolDistrict = null;
		selectedSchoolCitySin = null;
		selectedSchoolDivSec = null;
		selectedSchoolCityTamil = null;

		showDataOnGrid = new ArrayList<SisuSeriyaDTO>();

	}

	public void clearAddDet1() {

		selectNoOfPassen = null;

		selectRoutInfoOrigin = null;
		selectRoutInfoDestina = null;
		selectRoutInfoOriginSinhala = null;
		selectRoutInfoDestiSinhala = null;
		selectRoutInfoOriginTamil = null;
		selectRoutInfoDestinaTamil = null;
		selectRoutInfoVia = null;
		selectRoutInfoViaSin = null;
		selectRoutInfoViaTamil = null;

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

	public SisuSeriyaDTO getSisuSariyaViewDTO() {
		return sisuSariyaViewDTO;
	}

	public void setSisuSariyaViewDTO(SisuSeriyaDTO sisuSariyaViewDTO) {
		this.sisuSariyaViewDTO = sisuSariyaViewDTO;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

	public List<SisuSeriyaDTO> getRequestNoList() {
		return requestNoList;
	}

	public void setRequestNoList(List<SisuSeriyaDTO> requestNoList) {
		this.requestNoList = requestNoList;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
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

	public String getSelectAdd1() {
		return selectAdd1;
	}

	public void setSelectAdd1(String selectAdd1) {
		this.selectAdd1 = selectAdd1;
	}

	public String getSelectNameFull() {
		return selectNameFull;
	}

	public void setSelectNameFull(String selectNameFull) {
		this.selectNameFull = selectNameFull;
	}

	public String getSelectAdd1Sin() {
		return selectAdd1Sin;
	}

	public void setSelectAdd1Sin(String selectAdd1Sin) {
		this.selectAdd1Sin = selectAdd1Sin;
	}

	public String getSelectNameFullSin() {
		return selectNameFullSin;
	}

	public void setSelectNameFullSin(String selectNameFullSin) {
		this.selectNameFullSin = selectNameFullSin;
	}

	public String getSelectAdd1Tamil() {
		return selectAdd1Tamil;
	}

	public void setSelectAdd1Tamil(String selectAdd1Tamil) {
		this.selectAdd1Tamil = selectAdd1Tamil;
	}

	public String getSelectNameFullTamil() {
		return selectNameFullTamil;
	}

	public void setSelectNameFullTamil(String selectNameFullTamil) {
		this.selectNameFullTamil = selectNameFullTamil;
	}

	public String getSelectAdd2() {
		return selectAdd2;
	}

	public void setSelectAdd2(String selectAdd2) {
		this.selectAdd2 = selectAdd2;
	}

	public String getSelectTelNo() {
		return selectTelNo;
	}

	public void setSelectTelNo(String selectTelNo) {
		this.selectTelNo = selectTelNo;
	}

	public String getSelectAdd2Sin() {
		return selectAdd2Sin;
	}

	public void setSelectAdd2Sin(String selectAdd2Sin) {
		this.selectAdd2Sin = selectAdd2Sin;
	}

	public String getSelectMobNo() {
		return selectMobNo;
	}

	public void setSelectMobNo(String selectMobNo) {
		this.selectMobNo = selectMobNo;
	}

	public String getSelectAdd2Tamil() {
		return selectAdd2Tamil;
	}

	public void setSelectAdd2Tamil(String selectAdd2Tamil) {
		this.selectAdd2Tamil = selectAdd2Tamil;
	}

	public String getSelectEmail() {
		return selectEmail;
	}

	public void setSelectEmail(String selectEmail) {
		this.selectEmail = selectEmail;
	}

	public String getSelectCity() {
		return selectCity;
	}

	public void setSelectCity(String selectCity) {
		this.selectCity = selectCity;
	}

	public String getSelectCitySin() {
		return selectCitySin;
	}

	public void setSelectCitySin(String selectCitySin) {
		this.selectCitySin = selectCitySin;
	}

	public String getSelectCityTamil() {
		return selectCityTamil;
	}

	public void setSelectCityTamil(String selectCityTamil) {
		this.selectCityTamil = selectCityTamil;
	}

	public String getSelectedSchoolName() {
		return selectedSchoolName;
	}

	public void setSelectedSchoolName(String selectedSchoolName) {
		this.selectedSchoolName = selectedSchoolName;
	}

	public String getSelectedSchoolAdd1() {
		return selectedSchoolAdd1;
	}

	public void setSelectedSchoolAdd1(String selectedSchoolAdd1) {
		this.selectedSchoolAdd1 = selectedSchoolAdd1;
	}

	public String getSelectedSchoolNameSin() {
		return selectedSchoolNameSin;
	}

	public void setSelectedSchoolNameSin(String selectedSchoolNameSin) {
		this.selectedSchoolNameSin = selectedSchoolNameSin;
	}

	public String getSelectedSchoolAdd1Sin() {
		return selectedSchoolAdd1Sin;
	}

	public void setSelectedSchoolAdd1Sin(String selectedSchoolAdd1Sin) {
		this.selectedSchoolAdd1Sin = selectedSchoolAdd1Sin;
	}

	public String getSelectedSchoolNameTamil() {
		return selectedSchoolNameTamil;
	}

	public void setSelectedSchoolNameTamil(String selectedSchoolNameTamil) {
		this.selectedSchoolNameTamil = selectedSchoolNameTamil;
	}

	public String getSelectedSchoolAdd1Tamil() {
		return selectedSchoolAdd1Tamil;
	}

	public void setSelectedSchoolAdd1Tamil(String selectedSchoolAdd1Tamil) {
		this.selectedSchoolAdd1Tamil = selectedSchoolAdd1Tamil;
	}

	public String getSelectedSchoolTelNO() {
		return selectedSchoolTelNO;
	}

	public void setSelectedSchoolTelNO(String selectedSchoolTelNO) {
		this.selectedSchoolTelNO = selectedSchoolTelNO;
	}

	public String getSelectedSchoolAdd2() {
		return selectedSchoolAdd2;
	}

	public void setSelectedSchoolAdd2(String selectedSchoolAdd2) {
		this.selectedSchoolAdd2 = selectedSchoolAdd2;
	}

	public String getSelectedSchoolMobNO() {
		return selectedSchoolMobNO;
	}

	public void setSelectedSchoolMobNO(String selectedSchoolMobNO) {
		this.selectedSchoolMobNO = selectedSchoolMobNO;
	}

	public String getSelectedSchoolAdd2Sin() {
		return selectedSchoolAdd2Sin;
	}

	public void setSelectedSchoolAdd2Sin(String selectedSchoolAdd2Sin) {
		this.selectedSchoolAdd2Sin = selectedSchoolAdd2Sin;
	}

	public String getSelectedSchoolEmail() {
		return selectedSchoolEmail;
	}

	public void setSelectedSchoolEmail(String selectedSchoolEmail) {
		this.selectedSchoolEmail = selectedSchoolEmail;
	}

	public String getSelectedSchoolAdd2Tamil() {
		return selectedSchoolAdd2Tamil;
	}

	public void setSelectedSchoolAdd2Tamil(String selectedSchoolAdd2Tamil) {
		this.selectedSchoolAdd2Tamil = selectedSchoolAdd2Tamil;
	}

	public String getSelectedProvince() {
		return selectedProvince;
	}

	public void setSelectedProvince(String selectedProvince) {
		this.selectedProvince = selectedProvince;
	}

	public String getSelectedSchoolCity() {
		return selectedSchoolCity;
	}

	public void setSelectedSchoolCity(String selectedSchoolCity) {
		this.selectedSchoolCity = selectedSchoolCity;
	}

	public String getSelectedSchoolDistrict() {
		return selectedSchoolDistrict;
	}

	public void setSelectedSchoolDistrict(String selectedSchoolDistrict) {
		this.selectedSchoolDistrict = selectedSchoolDistrict;
	}

	public String getSelectedSchoolCitySin() {
		return selectedSchoolCitySin;
	}

	public void setSelectedSchoolCitySin(String selectedSchoolCitySin) {
		this.selectedSchoolCitySin = selectedSchoolCitySin;
	}

	public String getSelectedSchoolDivSec() {
		return selectedSchoolDivSec;
	}

	public void setSelectedSchoolDivSec(String selectedSchoolDivSec) {
		this.selectedSchoolDivSec = selectedSchoolDivSec;
	}

	public String getSelectedSchoolCityTamil() {
		return selectedSchoolCityTamil;
	}

	public void setSelectedSchoolCityTamil(String selectedSchoolCityTamil) {
		this.selectedSchoolCityTamil = selectedSchoolCityTamil;
	}

	public String getSelectNoOfPassen() {
		return selectNoOfPassen;
	}

	public void setSelectNoOfPassen(String selectNoOfPassen) {
		this.selectNoOfPassen = selectNoOfPassen;
	}

	public String getSelectRoutInfoOrigin() {
		return selectRoutInfoOrigin;
	}

	public void setSelectRoutInfoOrigin(String selectRoutInfoOrigin) {
		this.selectRoutInfoOrigin = selectRoutInfoOrigin;
	}

	public String getSelectRoutInfoDestina() {
		return selectRoutInfoDestina;
	}

	public void setSelectRoutInfoDestina(String selectRoutInfoDestina) {
		this.selectRoutInfoDestina = selectRoutInfoDestina;
	}

	public String getSelectRoutInfoOriginSinhala() {
		return selectRoutInfoOriginSinhala;
	}

	public void setSelectRoutInfoOriginSinhala(String selectRoutInfoOriginSinhala) {
		this.selectRoutInfoOriginSinhala = selectRoutInfoOriginSinhala;
	}

	public String getSelectRoutInfoDestiSinhala() {
		return selectRoutInfoDestiSinhala;
	}

	public void setSelectRoutInfoDestiSinhala(String selectRoutInfoDestiSinhala) {
		this.selectRoutInfoDestiSinhala = selectRoutInfoDestiSinhala;
	}

	public String getSelectRoutInfoOriginTamil() {
		return selectRoutInfoOriginTamil;
	}

	public void setSelectRoutInfoOriginTamil(String selectRoutInfoOriginTamil) {
		this.selectRoutInfoOriginTamil = selectRoutInfoOriginTamil;
	}

	public String getSelectRoutInfoDestinaTamil() {
		return selectRoutInfoDestinaTamil;
	}

	public void setSelectRoutInfoDestinaTamil(String selectRoutInfoDestinaTamil) {
		this.selectRoutInfoDestinaTamil = selectRoutInfoDestinaTamil;
	}

	public String getSelectRoutInfoVia() {
		return selectRoutInfoVia;
	}

	public void setSelectRoutInfoVia(String selectRoutInfoVia) {
		this.selectRoutInfoVia = selectRoutInfoVia;
	}

	public String getSelectRoutInfoViaSin() {
		return selectRoutInfoViaSin;
	}

	public void setSelectRoutInfoViaSin(String selectRoutInfoViaSin) {
		this.selectRoutInfoViaSin = selectRoutInfoViaSin;
	}

	public String getSelectRoutInfoViaTamil() {
		return selectRoutInfoViaTamil;
	}

	public void setSelectRoutInfoViaTamil(String selectRoutInfoViaTamil) {
		this.selectRoutInfoViaTamil = selectRoutInfoViaTamil;
	}

	public String getSelectRequestNO() {
		return selectRequestNO;
	}

	public void setSelectRequestNO(String selectRequestNO) {
		this.selectRequestNO = selectRequestNO;
	}

	public SisuSeriyaDTO getShowDataDTO() {
		return showDataDTO;
	}

	public void setShowDataDTO(SisuSeriyaDTO showDataDTO) {
		this.showDataDTO = showDataDTO;
	}

	public SisuSeriyaDTO getShowDataFirstTab() {
		return showDataFirstTab;
	}

	public void setShowDataFirstTab(SisuSeriyaDTO showDataFirstTab) {
		this.showDataFirstTab = showDataFirstTab;
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

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSelectReqNo() {
		return selectReqNo;
	}

	public void setSelectReqNo(String selectReqNo) {
		this.selectReqNo = selectReqNo;
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

	public boolean isRenderButton() {
		return renderButton;
	}

	public void setRenderButton(boolean renderButton) {
		this.renderButton = renderButton;
	}

}
