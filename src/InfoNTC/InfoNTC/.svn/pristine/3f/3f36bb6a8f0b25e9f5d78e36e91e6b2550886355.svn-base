package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.SuspendDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.SuspendService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "suspendBean")
@ViewScoped
public class SuspendPermitDriverConductorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private SuspendService suspendService;
	private CommonService commonService;

	private SuspendDTO searchDTO;
	private SuspendDTO suspendDTO;
	private SuspendDTO originalSuspendDTO;
	private SuspendDTO selectedDTO;

	private List<SuspendDTO> chargeRefList;
	private List<SuspendDTO> suspendList;
	private List<SuspendDTO> chargeRefListforSearch;

	private List<SuspendDTO> savedSuspendList;

	private List<DropDownDTO> suspendTypeList;

	private boolean itemSelected;
	private boolean editMode;

	private boolean typePermit;
	private boolean typeDriver;
	private boolean typeConductor;

	private String successMsg;
	private String errorMsg;

	private List<String> vehicleNoList;
	private List<String> permitNoList;

	@PostConstruct
	public void init() {
		suspendService = (SuspendService) SpringApplicationContex.getBean("suspendService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		loadData();
	}

	public void loadData() {
		searchDTO = new SuspendDTO();
		suspendDTO = new SuspendDTO();
		selectedDTO = new SuspendDTO();

		suspendTypeList = new ArrayList<DropDownDTO>();
		savedSuspendList = new ArrayList<SuspendDTO>();

		itemSelected = false;

		typePermit = false;
		typeDriver = false;
		typeConductor = false;

		editMode = false;

		/*
		 * Load LOV data
		 */
		chargeRefList = suspendService.getChargeRefNoList();
		suspendTypeList = suspendService.getSuspendTypeList();
		chargeRefListforSearch = suspendService.getChargeRefNoListforSearch();

		/*
		 * Load Saved Suspended Table
		 */
		savedSuspendList = suspendService.getSavedSuspendRecords();

		/*
		 * Load AutoComplete Lists
		 */

		vehicleNoList = suspendService.getAllVehicle();
		permitNoList = suspendService.getAllPermit();
	}

	public List<String> completeVehicleNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < vehicleNoList.size(); i++) {
			String cm = vehicleNoList.get(i);
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public List<String> completePermitNo(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < permitNoList.size(); i++) {
			String cm = permitNoList.get(i);
			if (cm != null && cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public void searchButtonAction() {

		if (searchDTO.getChargeRefNo().isEmpty() && searchDTO.getVehicleNo().isEmpty()
				&& searchDTO.getPermitNo().isEmpty() && searchDTO.getPermitOwnerName().isEmpty()) {
			showMsg("ERROR", "Search Criteria Not Entered");
		} else {

			savedSuspendList = suspendService.getSavedSuspendRecordsByChargeRef(searchDTO.getChargeRefNo());

			if (savedSuspendList.size() == 0) {

				savedSuspendList = suspendService.getSavedSuspendRecordsSearch(searchDTO);

				if (savedSuspendList.isEmpty()) {
					showMsg("ERROR", "No Data Found For The Given Criteria");
				}
			}
		}

		RequestContext.getCurrentInstance().update("frmSuspend");
	}

	public void clearButtonAction() {
		loadData();
		RequestContext.getCurrentInstance().update("frmSuspend");
	}

	public void changeChargeRefAction() {
		searchDTO = new SuspendDTO();
		searchDTO.setChargeRefNo(suspendDTO.getChargeRefNo());
		suspendDTO = suspendService.searchInvestigationDetails(searchDTO);
	}

	public void changeSuspendCategoryAction() {

		if (!itemSelected) {
			originalSuspendDTO = suspendDTO;
			itemSelected = true;
		}

		if (suspendDTO.getSuspendCategory().equalsIgnoreCase("P")) {
			String suspendCategory = suspendDTO.getSuspendCategory();

			typePermit = true;
			typeDriver = false;
			typeConductor = false;

			suspendDTO.setDriverID(null);
			suspendDTO.setConductorID(null);

			suspendDTO.setPermitNo(originalSuspendDTO.getPermitNo());
			suspendDTO.setPermitList(originalSuspendDTO.getPermitList());
			suspendDTO.setSuspendCategory(suspendCategory);

		} else if (suspendDTO.getSuspendCategory().equalsIgnoreCase("D")) {
			String suspendCategory = suspendDTO.getSuspendCategory();

			typePermit = false;
			typeDriver = true;
			typeConductor = false;

			changeChargeRefAction();

			suspendDTO.setPermitNo(null);
			suspendDTO.setConductorID(null);

			suspendDTO.setDriverID(originalSuspendDTO.getDriverID());
			suspendDTO.setDriverList(originalSuspendDTO.getDriverList());
			suspendDTO.setSuspendCategory(suspendCategory);

		} else if (suspendDTO.getSuspendCategory().equalsIgnoreCase("C")) {
			String suspendCategory = suspendDTO.getSuspendCategory();

			typePermit = false;
			typeDriver = false;
			typeConductor = true;

			changeChargeRefAction();

			suspendDTO.setPermitNo(null);
			suspendDTO.setDriverID(null);

			suspendDTO.setConductorID(originalSuspendDTO.getConductorID());
			suspendDTO.setConductorList(originalSuspendDTO.getConductorList());
			suspendDTO.setSuspendCategory(suspendCategory);

		}

	}

	public void onRowSelect(SelectEvent event) {

		searchDTO.setChargeRefNo(selectedDTO.getChargeRefNo());
		suspendDTO = suspendService.searchInvestigationDetails(searchDTO);

		RequestContext.getCurrentInstance().update("frmSuspend");
	}

	public void onRowUnselect(UnselectEvent event) {
		RequestContext.getCurrentInstance().update("frmSuspend");
	}

	public void saveBtnAction() {

		suspendDTO.setCreatedUser(sessionBackingBean.getLoginUser());

		if (editMode) {
			boolean suspendUpdate = suspendService.updateSuspendRecord(suspendDTO);

			if (suspendUpdate == true) {
				suspendService.beanLinkMethod(selectedDTO, "Transaction Completed","Suspend Permit/ Driver ID/ Conductor ID");
				showMsg("SUCCESS", "Transaction Completed");
			} else {
				showMsg("ERROR", "Transaction Failed");
				return;
			}

		} else {

			int duplicate = suspendService.checkDuplicate(suspendDTO);

			if (duplicate != 0) {
				showMsg("ERROR", "Record Already Exists For Charge Ref. No. & Suspend Category");
				return;
			}

			if (suspendDTO.getChargeRefNo() == null || suspendDTO.getChargeRefNo().isEmpty()) {
				showMsg("ERROR", "Select Charge Ref. No");
				return;
			}

			if (suspendDTO.getSuspendCategory() == null || suspendDTO.getSuspendCategory().isEmpty()) {
				showMsg("ERROR", "Select Suspend Category");
				return;
			}

			if (suspendDTO.getSuspendTypeCode() == null || suspendDTO.getSuspendTypeCode().isEmpty()) {
				showMsg("ERROR", "Select Suspend Type");
				return;
			}

			if (suspendDTO.getSuspendStartDate() == null) {
				showMsg("ERROR", "Select Suspend Start Date");
				return;
			}

			if (suspendDTO.getSuspendEndDate() == null) {
				showMsg("ERROR", "Select Suspend End Date");
				return;
			}

			boolean suspendSave = suspendService.saveSuspendMaster(suspendDTO);

			if (suspendSave == true) {
				suspendService.beanLinkMethod(selectedDTO, "Transaction Completed", "Suspend Permit/ Driver ID/ Conductor ID");
				showMsg("SUCCESS", "Transaction Completed");
			} else {
				showMsg("ERROR", "Transaction Failed");
				return;
			}
		}

		loadData();

	}

	public void cancelBtnAction() {
		loadData();
	}

	public void loadSuspendTable() {
		savedSuspendList = new ArrayList<SuspendDTO>();
		savedSuspendList = suspendService.getSavedSuspendRecords();

		RequestContext.getCurrentInstance().update("frmSuspend");
	}

	public void editButAction() {
		editMode = true;

		suspendDTO.setSuspendRefNo(selectedDTO.getSuspendRefNo());
		suspendDTO.setChargeRefNo(selectedDTO.getChargeRefNo());
		suspendDTO.setSuspendCategory(selectedDTO.getSuspendCategory());
		suspendDTO.setPermitNo(selectedDTO.getPermitNo());
		suspendDTO.setDriverID(selectedDTO.getDriverID());
		suspendDTO.setConductorID(selectedDTO.getConductorID());
		suspendDTO.setSuspendTypeCode(selectedDTO.getSuspendTypeCode());
		suspendDTO.setSuspendReason(selectedDTO.getSuspendReason());
		suspendDTO.setSuspendStartDate(selectedDTO.getSuspendStartDate());
		suspendDTO.setSuspendEndDate(selectedDTO.getSuspendEndDate());
		suspendDTO.setSuspendStatus(selectedDTO.getSuspendStatus());
		suspendDTO.setApplicationNo(selectedDTO.getApplicationNo());
		suspendDTO.setAppPrvStatus(selectedDTO.getAppPrvStatus());
		suspendDTO.setCurrentStatus(selectedDTO.getCurrentStatus());

		RequestContext.getCurrentInstance().update("frmSuspend");
	}

	public void showMsg(String type, String msg) {
		if (type.equalsIgnoreCase("ERROR")) {
			errorMsg = msg;
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		} else {
			successMsg = msg;
			RequestContext.getCurrentInstance().update("frmSuccess");
			RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
		}
	}

	/**
	 * added by tharushi.e for fixing issue clear vehi no permit no when change
	 * ref no
	 **/

	public void changeFirstChargeRef() {

		searchDTO.setVehicleNo(null);
		searchDTO.setPermitNo(null);
		searchDTO.setPermitOwnerName(null);

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isItemSelected() {
		return itemSelected;
	}

	public void setItemSelected(boolean itemSelected) {
		this.itemSelected = itemSelected;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SuspendDTO getSearchDTO() {
		return searchDTO;
	}

	public void setSearchDTO(SuspendDTO searchDTO) {
		this.searchDTO = searchDTO;
	}

	public SuspendService getSuspendService() {
		return suspendService;
	}

	public void setSuspendService(SuspendService suspendService) {
		this.suspendService = suspendService;
	}

	public List<SuspendDTO> getChargeRefList() {
		return chargeRefList;
	}

	public void setChargeRefList(List<SuspendDTO> chargeRefList) {
		this.chargeRefList = chargeRefList;
	}

	public SuspendDTO getSuspendDTO() {
		return suspendDTO;
	}

	public void setSuspendDTO(SuspendDTO suspendDTO) {
		this.suspendDTO = suspendDTO;
	}

	public List<DropDownDTO> getSuspendTypeList() {
		return suspendTypeList;
	}

	public void setSuspendTypeList(List<DropDownDTO> suspendTypeList) {
		this.suspendTypeList = suspendTypeList;
	}

	public List<SuspendDTO> getSuspendList() {
		return suspendList;
	}

	public void setSuspendList(List<SuspendDTO> suspendList) {
		this.suspendList = suspendList;
	}

	public boolean isTypePermit() {
		return typePermit;
	}

	public void setTypePermit(boolean typePermit) {
		this.typePermit = typePermit;
	}

	public boolean isTypeDriver() {
		return typeDriver;
	}

	public void setTypeDriver(boolean typeDriver) {
		this.typeDriver = typeDriver;
	}

	public boolean isTypeConductor() {
		return typeConductor;
	}

	public void setTypeConductor(boolean typeConductor) {
		this.typeConductor = typeConductor;
	}

	public SuspendDTO getOriginalSuspendDTO() {
		return originalSuspendDTO;
	}

	public void setOriginalSuspendDTO(SuspendDTO originalSuspendDTO) {
		this.originalSuspendDTO = originalSuspendDTO;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<SuspendDTO> getSavedSuspendList() {
		return savedSuspendList;
	}

	public void setSavedSuspendList(List<SuspendDTO> savedSuspendList) {
		this.savedSuspendList = savedSuspendList;
	}

	public SuspendDTO getSelectedDTO() {
		return selectedDTO;
	}

	public void setSelectedDTO(SuspendDTO selectedDTO) {
		this.selectedDTO = selectedDTO;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public List<String> getVehicleNoList() {
		return vehicleNoList;
	}

	public void setVehicleNoList(List<String> vehicleNoList) {
		this.vehicleNoList = vehicleNoList;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<SuspendDTO> getChargeRefListforSearch() {
		return chargeRefListforSearch;
	}

	public void setChargeRefListforSearch(List<SuspendDTO> chargeRefListforSearch) {
		this.chargeRefListforSearch = chargeRefListforSearch;
	}

}
