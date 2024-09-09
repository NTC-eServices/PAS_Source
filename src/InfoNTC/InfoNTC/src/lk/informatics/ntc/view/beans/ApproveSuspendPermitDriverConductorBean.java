package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.ManageUserDTO;
import lk.informatics.ntc.model.dto.SuspendDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.ManageUserService;
import lk.informatics.ntc.model.service.SuspendService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "approveSuspendBean")
@ViewScoped
public class ApproveSuspendPermitDriverConductorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private SuspendService suspendService;
	private CommonService commonService;
	private ManageUserService manageUserService;

	private SuspendDTO searchDTO;
	private SuspendDTO suspendDTO;
	private SuspendDTO originalSuspendDTO;
	private SuspendDTO selectedDTO;
	private ManageUserDTO manageUserDTO;

	private List<SuspendDTO> chargeRefList;
	private List<SuspendDTO> suspendList;

	private List<SuspendDTO> selectedList;

	private List<SuspendDTO> savedSuspendList;

	private List<DropDownDTO> suspendTypeList;

	private List<ManageUserDTO> roleList;
	private List<ManageUserDTO> functionList;
	private List<ManageUserDTO> activityList;

	private boolean itemSelected;
	private boolean editMode;

	private boolean typePermit;
	private boolean typeDriver;
	private boolean typeConductor;

	private String successMsg;
	private String errorMsg;

	private List<String> vehicleNoList;
	private List<String> permitNoList;
	private List<String> driverList;
	private List<String> conductorList;

	private boolean director;

	private boolean directorApproved;

	private boolean enableNormalApprove;
	private boolean enableDirectorApprove;
	private boolean enableLetterPrint;
	private boolean enablePermitPrint;
	private StreamedContent files;
	private String remarks;

	@PostConstruct
	public void init() {
		suspendService = (SuspendService) SpringApplicationContex.getBean("suspendService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		manageUserService = (ManageUserService) SpringApplicationContex.getBean("manageUserService");

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
		director = false;

		enableNormalApprove = false;
		enableDirectorApprove = false;
		enableLetterPrint = false;
		enablePermitPrint = false;

		// Load LOV data
		chargeRefList = suspendService.getChargeRefNoList();
		suspendTypeList = suspendService.getSuspendTypeList();

		// Load AutoComplete Lists/
		vehicleNoList = suspendService.filterVehicleForApproval();
		permitNoList = suspendService.filterPermitsForApproval();
		driverList = suspendService.filterDriverForApproval();
		conductorList = suspendService.filterConductorForApproval();

		savedSuspendList = suspendService.searchSuspendDetailsForApprove(searchDTO, "B");
	}

	public void onRowSelect(SelectEvent event) {

		enableNormalApprove = false;
		enableDirectorApprove = false;
		enableLetterPrint = false;

		itemSelected = true;
		checkDirector();

		if (selectedDTO.getCurrentStatus().equalsIgnoreCase("SS")) {
			directorApproved = false;
		} else if (selectedDTO.getCurrentStatus().equalsIgnoreCase("SA")) {
			directorApproved = true;
		} else if (selectedDTO.getCurrentStatus().equalsIgnoreCase("DA")) {
			/** modify by tharushi.e for enable print buttons **/
			if (selectedDTO.getPermitNo() != null && !selectedDTO.getPermitNo().trim().isEmpty()
					&& (selectedDTO.getDriverID() != null && !selectedDTO.getDriverID().trim().isEmpty()
							|| selectedDTO.getConductorID() != null
									&& !selectedDTO.getConductorID().trim().isEmpty())) {
				enableLetterPrint = true;
				enablePermitPrint = true;
			} else if (selectedDTO.getPermitNo() != null && !selectedDTO.getPermitNo().trim().isEmpty()
					&& (selectedDTO.getDriverID() == null
							|| selectedDTO.getDriverID().trim().isEmpty() && selectedDTO.getConductorID() == null
							|| selectedDTO.getConductorID().trim().isEmpty())) {

				enableLetterPrint = false;
				enablePermitPrint = true;

			} else if (selectedDTO.getPermitNo() == null || selectedDTO.getPermitNo().trim().isEmpty()
					&& (selectedDTO.getDriverID() != null && !selectedDTO.getDriverID().trim().isEmpty()
							|| selectedDTO.getConductorID() != null
									&& !selectedDTO.getConductorID().trim().isEmpty())) {

				enableLetterPrint = true;
				enablePermitPrint = false;
			}

		}

		if (!enableLetterPrint) {
			if (director) {
				if (directorApproved) {
					enableDirectorApprove = true;
				} else {
					enableNormalApprove = true;
				}
			} else {
				if (!directorApproved) {
					enableNormalApprove = true;
				}
			}
		}

		RequestContext.getCurrentInstance().update("frmSuspend");
	}

	public void onRowUnselect(UnselectEvent event) {
		RequestContext.getCurrentInstance().update("frmSuspend");
	}

	public boolean checkDirector() {

		roleList = manageUserService.viewDetails(sessionBackingBean.getLoginUser());

		for (ManageUserDTO dto : roleList) {
			if (dto.getTableStatus().equalsIgnoreCase("Active")) {
				functionList = manageUserService.showFuncDetails(dto.getRoleCode());

				for (ManageUserDTO dto1 : functionList) {
					if (dto1.getCode().equalsIgnoreCase("FN523")) {
						activityList = manageUserService.getFuncRoleActivity(dto.getRoleCode(), "FN523");
						if (activityList.size() != 0) {
							for (ManageUserDTO dto2 : activityList) {
								if (dto2.getActivityCode().equalsIgnoreCase("DA")) {
									director = true;
								}
							}
						}
					}
				}
			}
		}

		return director;
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

	public List<String> completeDriver(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < driverList.size(); i++) {
			String cm = driverList.get(i);
			if (cm != null && cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public List<String> completeConductor(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < conductorList.size(); i++) {
			String cm = conductorList.get(i);
			if (cm != null && cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public void searchButtonAction() {

		enableNormalApprove = false;
		enableDirectorApprove = false;
		enableLetterPrint = false;
		enablePermitPrint = false;

		if (director) {
			savedSuspendList = suspendService.searchSuspendDetailsForApprove(searchDTO, "Y");
		} else {
			savedSuspendList = suspendService.searchSuspendDetailsForApprove(searchDTO, "N");
		}

		RequestContext.getCurrentInstance().update("frmSuspend");
	}

	public void clearButtonAction() {
		loadData();
		RequestContext.getCurrentInstance().update("frmSuspend");
		remarks = null;
	}

	public void approveButtonAction() {
		boolean update = false;
		selectedDTO.setCurrentStatus("SA");
		selectedDTO.setCreatedUser(sessionBackingBean.getLoginUser());

		update = suspendService.updateApproveStatus(selectedDTO, null,"Suspend Approve");

		if (update == true) {
			suspendService.beanLinkMethod(selectedDTO,"Suspend Approve","Approval for Suspend Permit/ Driver ID/ Conductor ID");
			showMsg("SUCCESS", "Approved Successfully");
		} else {
			showMsg("ERROR", "Transaction Failed");
			return;
		}

		loadData();
	}

	public void rejectButtonAction() {
		boolean update = false;
		selectedDTO.setCurrentStatus("SR");
		selectedDTO.setCreatedUser(sessionBackingBean.getLoginUser());

		update = suspendService.updateApproveStatus(selectedDTO, remarks, "Suspend Reject");

		if (update == true) {
			suspendService.beanLinkMethod(selectedDTO,"Suspend Reject","Approval for Suspend Permit/ Driver ID/ Conductor ID");
			showMsg("SUCCESS", "Rejected");
			remarks = null;
		} else {
			showMsg("ERROR", "Transaction Failed");
			return;
		}

		loadData();
	}

	public void dirApproveButtonAction() {
		boolean update = false;
		selectedDTO.setCurrentStatus("DA");
		selectedDTO.setCreatedUser(sessionBackingBean.getLoginUser());
		SuspendDTO dto = new SuspendDTO();
		String prviApp = null;
		String preAppStatus = null;
		dto = suspendService.getPreviousAppDet(selectedDTO.getPermitNo(), selectedDTO.getDriverID(),
				selectedDTO.getConductorID());
		prviApp = dto.getPreAppNo();
		preAppStatus = dto.getPreAppStatus();
		update = suspendService.updateDirectorApproveStatus(selectedDTO, prviApp, preAppStatus,"Director Approved");

		if (update == true) {
			if (selectedDTO.getPermitNo() != null) {
				boolean updatePermit = suspendService.updatePermitStatus(selectedDTO, selectedDTO.getPermitNo(), "I",
						selectedDTO.getCreatedUser());
			}
			/**
			 * added by tharushi.e for inactive driver conductor in driver
			 * conductor registration table
			 **/
			else if (selectedDTO.getPermitNo() == null && selectedDTO.getDriverID() != null) {

				suspendService.updateDriverConductorStatus(selectedDTO, selectedDTO.getDriverID(), "I",
						selectedDTO.getCreatedUser(), "Driver Status Updated");
			} else if (selectedDTO.getPermitNo() == null && selectedDTO.getConductorID() != null) {

				suspendService.updateDriverConductorStatus(selectedDTO, selectedDTO.getConductorID(), "I",
						selectedDTO.getCreatedUser(),"Conductor Status Updated");
			}

			/** end **/
			suspendService.beanLinkMethod(selectedDTO,"Director Approve","Approval for Suspend Permit/ Driver ID/ Conductor ID");
			showMsg("SUCCESS", "Director Approved");
		} else {
			showMsg("ERROR", "Transaction Failed");
			return;
		}

		loadData();
	}

	public void dirRejButtonAction() {
		boolean update = false;
		selectedDTO.setCurrentStatus("DR");
		selectedDTO.setCreatedUser(sessionBackingBean.getLoginUser());

		update = suspendService.updateDirectorApproveStatus(selectedDTO, null, null, "Director Rejected");

		if (update == true) {
			suspendService.beanLinkMethod(selectedDTO,"Director Reject","Approval for Suspend Permit/ Driver ID/ Conductor ID");
			showMsg("SUCCESS", "Director Rejected");
		} else {
			showMsg("ERROR", "Transaction Failed");
			return;
		}

		loadData();
	}

	/*
	 * Generate Suspend Letters for Permit and Driver/Conductor
	 */
	public StreamedContent generateSuspendLetters() throws JRException {
		String sourceFileName = null;
		files = null;
		Connection conn = null;
		String driverName = null;
		String conductorName = null;
		if (selectedDTO != null) {

			if (selectedDTO.getDriverID() != null || selectedDTO.getConductorID() != null) {
				driverName = suspendService.getNameByDCId(selectedDTO.getDriverID());
				conductorName = suspendService.getNameByDCId(selectedDTO.getConductorID());

				try {
					conn = ConnectionManager.getConnection();
					sourceFileName = "..//reports//SuspendLetterNew.jrxml";

					String logopath = "//lk//informatics//ntc//view//reports//";
					Map<String, Object> parameters = new HashMap<String, Object>();

					parameters.put("P_Suspend_Ref_No", selectedDTO.getSuspendRefNo());
					if (conductorName != null) {
						parameters.put("P_conductor_name", conductorName);
					} else {
						parameters.put("P_conductor_name", "");
					}
					if (driverName != null) {
						parameters.put("P_driver_name", driverName);
					}

					else {
						parameters.put("P_driver_name", "");

					}

					JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

					JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

					jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

					byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
					InputStream stream = new ByteArrayInputStream(pdfByteArray);
					files = new DefaultStreamedContent(stream, "Application/pdf", "SuspendLetter.pdf");

					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					sessionMap.put("reportBytes", pdfByteArray);
					sessionMap.put("docType", "pdf");

					suspendService.beanLinkMethod(selectedDTO,"Suspend Letter Print","Approval for Suspend Permit/ Driver ID/ Conductor ID");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					ConnectionManager.close(conn);
				}

			}

		}

		return files;
	}

	public StreamedContent getPermitSuspendLetter() throws JRException {
		String sourceFileName = null;
		files = null;
		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();
			sourceFileName = "..//reports//PermitSuspendLetterNew.jrxml";

			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Suspend_Ref_No", selectedDTO.getSuspendRefNo());

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Permit suspend Letter.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			try {
				suspendService.beanLinkMethod(selectedDTO,"Permit Suspend Letter Print","Approval for Suspend Permit/ Driver ID/ Conductor ID");
			} catch (Exception e) {
				e.printStackTrace();
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}
		return files;
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

	public List<SuspendDTO> getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(List<SuspendDTO> selectedList) {
		this.selectedList = selectedList;
	}

	public List<String> getDriverList() {
		return driverList;
	}

	public void setDriverList(List<String> driverList) {
		this.driverList = driverList;
	}

	public List<String> getConductorList() {
		return conductorList;
	}

	public void setConductorList(List<String> conductorList) {
		this.conductorList = conductorList;
	}

	public ManageUserDTO getManageUserDTO() {
		return manageUserDTO;
	}

	public void setManageUserDTO(ManageUserDTO manageUserDTO) {
		this.manageUserDTO = manageUserDTO;
	}

	public ManageUserService getManageUserService() {
		return manageUserService;
	}

	public void setManageUserService(ManageUserService manageUserService) {
		this.manageUserService = manageUserService;
	}

	public List<ManageUserDTO> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<ManageUserDTO> roleList) {
		this.roleList = roleList;
	}

	public List<ManageUserDTO> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<ManageUserDTO> functionList) {
		this.functionList = functionList;
	}

	public List<ManageUserDTO> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<ManageUserDTO> activityList) {
		this.activityList = activityList;
	}

	public boolean isDirector() {
		return director;
	}

	public void setDirector(boolean director) {
		this.director = director;
	}

	public boolean isDirectorApproved() {
		return directorApproved;
	}

	public void setDirectorApproved(boolean directorApproved) {
		this.directorApproved = directorApproved;
	}

	public boolean isEnableNormalApprove() {
		return enableNormalApprove;
	}

	public void setEnableNormalApprove(boolean enableNormalApprove) {
		this.enableNormalApprove = enableNormalApprove;
	}

	public boolean isEnableDirectorApprove() {
		return enableDirectorApprove;
	}

	public void setEnableDirectorApprove(boolean enableDirectorApprove) {
		this.enableDirectorApprove = enableDirectorApprove;
	}

	public boolean isEnableLetterPrint() {
		return enableLetterPrint;
	}

	public void setEnableLetterPrint(boolean enableLetterPrint) {
		this.enableLetterPrint = enableLetterPrint;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isEnablePermitPrint() {
		return enablePermitPrint;
	}

	public void setEnablePermitPrint(boolean enablePermitPrint) {
		this.enablePermitPrint = enablePermitPrint;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
