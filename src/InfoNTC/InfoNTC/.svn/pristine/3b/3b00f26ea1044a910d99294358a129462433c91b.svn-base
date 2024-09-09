package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.GamiSeriyaDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.ntc.view.util.UtilityClass;

@ManagedBean(name = "requestForGamiSeriyaBackingBean")
@ViewScoped
public class requestForGamiSeriyaBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private GamiSariyaService gamiSariyaService;
	private DocumentManagementService documentManagementService;
	private AdminService adminService;

	private GamiSeriyaDTO gamiDTO;
	private GamiSeriyaDTO gamiDTO2;
	private GamiSeriyaDTO selectedRow;

	private int activeIndex;
	private String errorMsg;
	private String sucessMsg;
	private String testDateString;
	private boolean saveCheck;
	private String reqVariable;
	private boolean tabCheck = true;

	private List<GamiSeriyaDTO> requestorTypeList;
	private List<CommonDTO> provinceList;
	private List<GamiSeriyaDTO> tableList;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	@PostConstruct
	public void init() {

		setGamiSariyaService((GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService"));
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		setGamiDTO(new GamiSeriyaDTO());
		setGamiDTO2(new GamiSeriyaDTO());

		requestorTypeList = new ArrayList<GamiSeriyaDTO>();
		provinceList = new ArrayList<CommonDTO>();
		tableList = new ArrayList<GamiSeriyaDTO>();

		requestorTypeList = gamiSariyaService.requestorDropDown();
		provinceList = adminService.getProvinceToDropdown();
		Date todaysDate = new Date();
		gamiDTO.setRequestDate(todaysDate);
		selectedRow = new GamiSeriyaDTO();

	}

	public void add() {

		if (gamiDTO2.getOrigin() != null && !gamiDTO2.getOrigin().isEmpty() && !gamiDTO2.getOrigin().equals("")) {
			if (gamiDTO2.getDestination() != null && !gamiDTO2.getDestination().isEmpty()
					&& !gamiDTO2.getDestination().equals("")) {
				if (gamiDTO2.getVia() != null && !gamiDTO2.getVia().isEmpty() && !gamiDTO2.getVia().equals("")) {

					boolean validateByLanguage = false;
					/** start */
					if (gamiDTO.getPreferedLanguage() != null && gamiDTO.getPreferedLanguage().equals("SIN")) {

						if (gamiDTO2.getOriginSinhala() != null && !gamiDTO2.getOriginSinhala().isEmpty()
								&& !gamiDTO2.getOriginSinhala().equals("")) {
							if (gamiDTO2.getDestinationSinhala() != null && !gamiDTO2.getDestinationSinhala().isEmpty()
									&& !gamiDTO2.getDestinationSinhala().equals("")) {
								if (gamiDTO2.getViaSinhala() != null && !gamiDTO2.getViaSinhala().isEmpty()
										&& !gamiDTO2.getViaSinhala().equals("")) {

									validateByLanguage = true;

								} else {
									setErrorMsg("Via in Sinhala is mandotory.");
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								setErrorMsg("Destination in Sinhala is mandotory.");
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							setErrorMsg("Origin in Sinhala is mandotory.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else if (gamiDTO.getPreferedLanguage() != null && gamiDTO.getPreferedLanguage().equals("TAM")) {
						if (gamiDTO2.getOriginTamil() != null && !gamiDTO2.getOriginTamil().isEmpty()
								&& !gamiDTO2.getOriginTamil().equals("")) {
							if (gamiDTO2.getDestinationTamil() != null && !gamiDTO2.getDestinationTamil().isEmpty()
									&& !gamiDTO2.getDestinationTamil().equals("")) {
								if (gamiDTO2.getViaTamil() != null && !gamiDTO2.getViaTamil().isEmpty()
										&& !gamiDTO2.getViaTamil().equals("")) {

									validateByLanguage = true;

								} else {
									setErrorMsg("Via in Tamil is mandotory.");
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								setErrorMsg("Destination in Tamil is mandotory.");
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							setErrorMsg("Origin in Tamil is mandotory.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else if (gamiDTO.getPreferedLanguage() != null && gamiDTO.getPreferedLanguage().equals("ENG")) {
						validateByLanguage = true;
					}

					/** after validation */

					if (validateByLanguage) {
						gamiDTO2.setRequestNo(gamiDTO.getRequestNo());

						if (gamiSariyaService.updateGamiRequestorStatus(gamiDTO2)) {
							if (gamiSariyaService.saveRequstorRouteInfo(gamiDTO2,sessionBackingBean.getLoginUser())) {								
								tableList = gamiSariyaService.tblGamiRequestorRouteList(gamiDTO);
								tabCheck = false;
								setSucessMsg("Data Added successfully");
								RequestContext.getCurrentInstance().update("frmsuccessSve");
								RequestContext.getCurrentInstance().execute("PF('successSve').show()");
								clear2();
							} else {
								setSucessMsg("Data did not save.");
								RequestContext.getCurrentInstance().update("frmsuccessSve");
								RequestContext.getCurrentInstance().execute("PF('successSve').show()");
							}
						} else {
							setSucessMsg("Data did not save.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						}

					}

					/* end **/

				} else {
					setErrorMsg("Via is mandotory.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				setErrorMsg("Destination is mandotory.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			setErrorMsg("Origin is mandotory.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void validateEmail() {

		if (!UtilityClass.isValidEmailAddress(gamiDTO.getEmail())) {
			setErrorMsg("Invalid Email.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void save() {

		if (gamiDTO.getPreferedLanguage() != null && !gamiDTO.getPreferedLanguage().isEmpty()
				&& !gamiDTO.getPreferedLanguage().equals("")) {

			if (gamiDTO.getRequestDate() != null) {
				if (gamiDTO.getRequestortype() != null && !gamiDTO.getRequestortype().isEmpty()
						&& !gamiDTO.getRequestortype().equals("")) {

					if (gamiDTO.getNameinFull() != null && !gamiDTO.getNameinFull().isEmpty()
							&& !gamiDTO.getNameinFull().equals("")) {

						if (gamiDTO.getAddress1() != null && !gamiDTO.getAddress1().isEmpty()
								&& !gamiDTO.getAddress1().equals("")) {

							if (gamiDTO.getCity() != null && !gamiDTO.getCity().isEmpty()
									&& !gamiDTO.getCity().equals("")) {

								boolean validateViaLanguage = false;

								/** start */
								if (gamiDTO.getPreferedLanguage().equals("SIN")) {

									if (gamiDTO.getAddress1sinhala() != null && !gamiDTO.getAddress1sinhala().isEmpty()
											&& !gamiDTO.getAddress1sinhala().equals("")) {

										if (gamiDTO.getCitySinhala() != null && !gamiDTO.getCitySinhala().isEmpty()
												&& !gamiDTO.getCitySinhala().equals("")) {

											validateViaLanguage = true;

										} else {
											setErrorMsg("City in Sinhala is mandatory.");
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}

									} else {
										setErrorMsg("Address Line 1 in Sinhala is mandatory.");
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else if (gamiDTO.getPreferedLanguage().equals("TAM")) {

									if (gamiDTO.getAddress1Tamil() != null && !gamiDTO.getAddress1Tamil().isEmpty()
											&& !gamiDTO.getAddress1Tamil().equals("")) {

										if (gamiDTO.getCityTamil() != null && !gamiDTO.getCityTamil().isEmpty()
												&& !gamiDTO.getCityTamil().equals("")) {

											validateViaLanguage = true;

										} else {
											setErrorMsg("City in Tamil is mandatory.");
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}

									} else {
										setErrorMsg("Address Line 1 in Tamil is mandatory.");
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}

								} else {
									validateViaLanguage = true;
								}
								/** end */

								if (validateViaLanguage) {

									gamiDTO = gamiSariyaService.saveGamiRequestorInfo(gamiDTO,sessionBackingBean.getLoginUser());

									if (gamiDTO.getRequestNo() != null && !gamiDTO.getRequestNo().isEmpty()
											&& !gamiDTO.getRequestNo().equals("")) {

										saveCheck = true;
										tabCheck = false;
										activeIndex = 1;

										gamiDTO2 = new GamiSeriyaDTO();
										tableList = new ArrayList<GamiSeriyaDTO>();

										setSucessMsg("Data Saved successfully");
										RequestContext.getCurrentInstance().update("frmsuccessSve");
										RequestContext.getCurrentInstance().execute("PF('successSve').show()");
									} else {
										setErrorMsg("Data did not save.");
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}


								}

							} else {
								setErrorMsg("City in English is mandatory.");
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							setErrorMsg("Address Line 1 in English is mandatory.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						setErrorMsg("Name in Full is mandatory.");
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					setErrorMsg("Required Type is mandatory.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				setErrorMsg("Request Date is mandatory.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			setErrorMsg("Prefered Language is mandatory.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
		}

	//}

	public void documentManagement() {

		try {

			sessionBackingBean.setRequestNoForSisuSariya(gamiDTO.getRequestNo());

			sessionBackingBean.setRequestNoForGamiSariya(gamiDTO.getRequestNo());

			sessionBackingBean.setRequestNoForGamiSariya(gamiDTO.getRequestNo());

			sessionBackingBean.setTransactionType("GAMI SARIYA");

			mandatoryList = documentManagementService.mandatoryDocsForSisuSariya("19", gamiDTO.getRequestNo());
			optionalList = documentManagementService.optionalDocsForSisuSariya("19", gamiDTO.getRequestNo());

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.gamiSariyaMandatoryList(gamiDTO.getRequestNo());
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.gamiSariyaOptionalList(gamiDTO.getRequestNo());

			
			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void clear2() {
		gamiDTO2 = new GamiSeriyaDTO();
		activeIndex = 1;
		tabCheck = false;

	}

	public void clearRequestInfo() {
		gamiDTO = new GamiSeriyaDTO();
		saveCheck = false;
		tabCheck = true;
		activeIndex = 0;
	}

	public void clearRequestorInfo() {
		GamiSeriyaDTO tempt = new GamiSeriyaDTO();
		tempt.setPreferedLanguage(gamiDTO.getPreferedLanguage());
		tempt.setRequestDate(gamiDTO.getRequestDate());
		tempt.setRequestortype(gamiDTO.getRequestortype());
		gamiDTO = new GamiSeriyaDTO();
		gamiDTO = tempt;
		saveCheck = false;
		tabCheck = true;
		activeIndex = 0;
	}

	public void onRowSelect(SelectEvent event) {
		
	}

	public void delete() {
		String value = selectedRow.getSeqNo();
		gamiSariyaService.delete(value);
		tableList = gamiSariyaService.tblGamiRequestorRouteList(gamiDTO);
		RequestContext.getCurrentInstance().update("e");

	}

	public void getRequesterInfoByNicNo() {

		gamiDTO = gamiSariyaService.getRequesterInfoByNicNo(gamiDTO);
	}

	public GamiSeriyaDTO getGamiDTO() {
		return gamiDTO;
	}

	public void setGamiDTO(GamiSeriyaDTO gamiDTO) {
		this.gamiDTO = gamiDTO;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public List<GamiSeriyaDTO> getRequestorTypeList() {
		return requestorTypeList;
	}

	public void setRequestorTypeList(List<GamiSeriyaDTO> requestorTypeList) {
		this.requestorTypeList = requestorTypeList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getTestDateString() {
		return testDateString;
	}

	public void setTestDateString(String testDateString) {
		this.testDateString = testDateString;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public boolean isSaveCheck() {
		return saveCheck;
	}

	public void setSaveCheck(boolean saveCheck) {
		this.saveCheck = saveCheck;
	}

	public boolean isTabCheck() {
		return tabCheck;
	}

	public void setTabCheck(boolean tabCheck) {
		this.tabCheck = tabCheck;
	}

	public String getReqVariable() {
		return reqVariable;
	}

	public void setReqVariable(String reqVariable) {
		this.reqVariable = reqVariable;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public GamiSeriyaDTO getGamiDTO2() {
		return gamiDTO2;
	}

	public void setGamiDTO2(GamiSeriyaDTO gamiDTO2) {
		this.gamiDTO2 = gamiDTO2;
	}

	public List<GamiSeriyaDTO> getTableList() {
		return tableList;
	}

	public void setTableList(List<GamiSeriyaDTO> tableList) {
		this.tableList = tableList;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public GamiSeriyaDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(GamiSeriyaDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<CommonDTO> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<CommonDTO> provinceList) {
		this.provinceList = provinceList;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
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

}
