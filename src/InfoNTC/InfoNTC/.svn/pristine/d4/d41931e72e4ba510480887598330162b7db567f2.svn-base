package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
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

@ManagedBean(name = "viewEditForGamiSeriyaBackingBean")
@ViewScoped
public class viewEditForGamiSariyaBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private GamiSariyaService gamiSariyaService;
	private AdminService adminService;
	private DocumentManagementService documentManagementService;

	private GamiSeriyaDTO gamiDTO;
	private GamiSeriyaDTO gamiDTO2;
	private boolean editCheck;
	private boolean editSave;
	private boolean searchCheck;
	private boolean saveCheck;
	private boolean addCheck;
	private boolean saveEditCheck;
	private String errorMsg;
	private String sucessMsg;
	private List<GamiSeriyaDTO> requestorTypeList = new ArrayList<GamiSeriyaDTO>();
	private List<CommonDTO> provinceList = new ArrayList<CommonDTO>();
	private List<GamiSeriyaDTO> tableList = new ArrayList<GamiSeriyaDTO>();
	private List<String> requestNoList = new ArrayList<String>();
	private GamiSeriyaDTO selectedRow;

	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);

	@PostConstruct
	public void init() {
		saveCheck = true;
		addCheck = true;
		saveEditCheck = true;
		editCheck = true;
		editSave = false;
		setGamiSariyaService((GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService"));
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");
		requestNoList = gamiSariyaService.refNodropDown();
		requestorTypeList = gamiSariyaService.requestorDropDown();
		tableList = new ArrayList<>();
		provinceList = adminService.getProvinceToDropdown();
		gamiDTO = new GamiSeriyaDTO();
		gamiDTO2 = new GamiSeriyaDTO();
		selectedRow = new GamiSeriyaDTO();

	}

	public void search() {

		if (gamiDTO.getRequestNo() == null || gamiDTO.getRequestNo().isEmpty() || gamiDTO.getRequestNo().equals("")) {
			setErrorMsg("Select all mandatory fields to search");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else {
			searchCheck = gamiSariyaService.checkViewEdit(gamiDTO);

			if (searchCheck == true) {
				gamiDTO = gamiSariyaService.searchForViewEdit(gamiDTO);
				RequestContext.getCurrentInstance().update("xx");
				editCheck = false;
				addCheck = false;
				saveCheck = false;
				tableList = gamiSariyaService.tblGamiRequestorRouteList(gamiDTO);

			}

			else {
				editCheck = true;

				setErrorMsg("No data for the selected request No.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
	}

	public void add() {
		boolean test = false;
		if (gamiDTO.getPreferedLanguage().equals("ENG")) {
			if (gamiDTO2.getVia() == null || gamiDTO2.getVia().isEmpty() || gamiDTO2.getVia().equals("")
					|| gamiDTO2.getOrigin() == null || gamiDTO2.getOrigin().isEmpty() || gamiDTO2.getOrigin().equals("")
					|| gamiDTO2.getDestination() == null || gamiDTO2.getDestination().isEmpty()
					|| gamiDTO2.getDestination().equals("")) {
				test = true;
				setErrorMsg("Please fill all relevant fields in english language");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else if (gamiDTO.getPreferedLanguage().equals("SIN")) {

			if (gamiDTO2.getViaSinhala() == null || gamiDTO2.getViaSinhala().isEmpty()
					|| gamiDTO2.getViaSinhala().equals("") || gamiDTO2.getOriginSinhala() == null
					|| gamiDTO2.getOriginSinhala().isEmpty() || gamiDTO2.getOriginSinhala().equals("")
					|| gamiDTO2.getDestinationSinhala() == null || gamiDTO2.getDestinationSinhala().isEmpty()
					|| gamiDTO2.getDestinationSinhala().equals("")) {

				test = true;
				setErrorMsg("Please fill all relevant fields in sinhala language");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}
		} else if (gamiDTO.getPreferedLanguage().equals("TAM")) {

			if (gamiDTO2.getViaSinhala() == null || gamiDTO2.getViaSinhala().isEmpty()
					|| gamiDTO2.getViaSinhala().equals("") || gamiDTO2.getOriginSinhala() == null
					|| gamiDTO2.getOriginSinhala().isEmpty() || gamiDTO2.getOriginSinhala().equals("")
					|| gamiDTO2.getDestinationSinhala() == null || gamiDTO2.getDestinationSinhala().isEmpty()
					|| gamiDTO2.getDestinationSinhala().equals("")) {
				test = true;
				setErrorMsg("Please fill all relevant fields in tamil language");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		}

		if (test == false) {

			gamiDTO2.setRequestNo(gamiDTO.getRequestNo());

			if ((gamiDTO2.getOriginSinhala() != null) && gamiDTO2.getOriginSinhala() != null
					&& !gamiDTO2.getOriginSinhala().isEmpty() && !gamiDTO2.getOriginSinhala().equalsIgnoreCase("")) {
				gamiDTO2.setOriginSinhala(gamiDTO2.getOriginSinhala());
			}
			if ((gamiDTO2.getOriginTamil() != null) && gamiDTO2.getOriginTamil() != null
					&& !gamiDTO2.getOriginTamil().isEmpty() && !gamiDTO2.getOriginTamil().equalsIgnoreCase("")) {
				gamiDTO2.setOriginTamil(gamiDTO2.getOriginTamil());
			}
			if ((gamiDTO2.getViaSinhala() != null) && gamiDTO2.getViaSinhala() != null
					&& !gamiDTO2.getViaSinhala().isEmpty() && !gamiDTO2.getViaSinhala().equalsIgnoreCase("")) {
				gamiDTO2.setViaSinhala(gamiDTO2.getViaSinhala());
			}
			if ((gamiDTO2.getViaTamil() != null) && gamiDTO2.getViaTamil() != null && !gamiDTO2.getViaTamil().isEmpty()
					&& !gamiDTO2.getViaTamil().equalsIgnoreCase("")) {
				gamiDTO2.setViaTamil(gamiDTO2.getViaTamil());
			}
			if ((gamiDTO2.getDestinationSinhala() != null) && gamiDTO2.getDestinationSinhala() != null
					&& !gamiDTO2.getDestinationSinhala().isEmpty()
					&& !gamiDTO2.getDestinationSinhala().equalsIgnoreCase("")) {
				gamiDTO2.setDestinationSinhala(gamiDTO2.getDestinationSinhala());
			}
			if ((gamiDTO2.getDestinationTamil() != null) && gamiDTO2.getDestinationTamil() != null
					&& !gamiDTO2.getDestinationTamil().isEmpty()
					&& !gamiDTO2.getDestinationTamil().equalsIgnoreCase("")) {
				gamiDTO2.setDestinationTamil(gamiDTO2.getDestinationTamil());
			}

			gamiSariyaService.updateGamiRequestorStatus(gamiDTO2);
			gamiSariyaService.saveRequstorRouteInfo(gamiDTO2, sessionBackingBean.getLoginUser());
			tableList = gamiSariyaService.tblGamiRequestorRouteList(gamiDTO2);

			setSucessMsg("Data Added successfully");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			gamiDTO2 = new GamiSeriyaDTO();

			RequestContext.getCurrentInstance().update("xx");
		}
	}

	public void edit() {
		addCheck = true;
		String editvalue = selectedRow.getSeqNo();
		gamiDTO2 = selectedRow;
		setSaveEditCheck(false);

	}

	public void editSave() {
		gamiSariyaService.editRouteInfo(gamiDTO2, sessionBackingBean.getLoginUser());
		RequestContext.getCurrentInstance().update("xx");
		setSucessMsg("Data Edited Successfully");
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		gamiDTO2 = new GamiSeriyaDTO();
		saveEditCheck = true;
		addCheck = false;
		RequestContext.getCurrentInstance().update("xx");
	}

	public void mainClear() {
		init();
	}

	public void clear() {
		addCheck = true;
		editCheck = true;
		saveCheck = true;
		gamiDTO = new GamiSeriyaDTO();

	}

	public void clear2() {
		gamiDTO2 = new GamiSeriyaDTO();
		addCheck = false;
		setEditSave(true);
		saveEditCheck = true;
	}

	public void clear3() {
		addCheck = true;
		setEditSave(false);
		gamiDTO2 = new GamiSeriyaDTO();
		tableList = new ArrayList<GamiSeriyaDTO>();
		saveEditCheck = true;
	}

	public void onRowSelect(SelectEvent event) {

	}

	public void delete() {
		String value = selectedRow.getSeqNo();
		gamiSariyaService.delete(value);
		tableList = gamiSariyaService.tblGamiRequestorRouteList(gamiDTO);
		RequestContext.getCurrentInstance().update("e");
		setSucessMsg("Data deleted sucessfully");
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");
	}

	public void save() {
		gamiSariyaService.editReqInfo(gamiDTO, sessionBackingBean.getLoginUser());
		RequestContext.getCurrentInstance().update("xx");
		setSucessMsg("Data updated sucessfully");
		RequestContext.getCurrentInstance().update("frmsuccessSve");
		RequestContext.getCurrentInstance().execute("PF('successSve').show()");

	}

	public void documentManagement() {

		try {

			sessionBackingBean.setRequestNoForSisuSariya(gamiDTO.getRequestNo());

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

	public GamiSeriyaDTO getGamiDTO() {
		return gamiDTO;
	}

	public void setGamiDTO(GamiSeriyaDTO gamiDTO) {
		this.gamiDTO = gamiDTO;
	}

	public boolean isEditCheck() {
		return editCheck;
	}

	public void setEditCheck(boolean editCheck) {
		this.editCheck = editCheck;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<GamiSeriyaDTO> getRequestorTypeList() {
		return requestorTypeList;
	}

	public void setRequestorTypeList(List<GamiSeriyaDTO> requestorTypeList) {
		this.requestorTypeList = requestorTypeList;
	}

	public List<CommonDTO> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<CommonDTO> provinceList) {
		this.provinceList = provinceList;
	}

	public List<GamiSeriyaDTO> getTableList() {
		return tableList;
	}

	public void setTableList(List<GamiSeriyaDTO> tableList) {
		this.tableList = tableList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public List<String> getRequestNoList() {
		return requestNoList;
	}

	public void setRequestNoList(List<String> requestNoList) {
		this.requestNoList = requestNoList;
	}

	public boolean isSaveCheck() {
		return saveCheck;
	}

	public void setSaveCheck(boolean saveCheck) {
		this.saveCheck = saveCheck;
	}

	public GamiSeriyaDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(GamiSeriyaDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public boolean isAddCheck() {
		return addCheck;
	}

	public void setAddCheck(boolean addCheck) {
		this.addCheck = addCheck;
	}

	public GamiSeriyaDTO getGamiDTO2() {
		return gamiDTO2;
	}

	public void setGamiDTO2(GamiSeriyaDTO gamiDTO2) {
		this.gamiDTO2 = gamiDTO2;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isSearchCheck() {
		return searchCheck;
	}

	public void setSearchCheck(boolean searchCheck) {
		this.searchCheck = searchCheck;
	}

	public boolean isSaveEditCheck() {
		return saveEditCheck;
	}

	public void setSaveEditCheck(boolean saveEditCheck) {
		this.saveEditCheck = saveEditCheck;
	}

	public boolean isEditSave() {
		return editSave;
	}

	public void setEditSave(boolean editSave) {
		this.editSave = editSave;
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
