package lk.informatics.ntc.view.beans;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DriverConductorTrainingDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.MaintainTrainingScheduleDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.model.service.GrievanceManagementService;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

/**
 * 
 * @author dilakshi.h
 *
 */
@ViewScoped
@ManagedBean(name = "driverConductorPrintIDsBackingBean")
public class DriverConductorPrintIDsBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private DriverConductorTrainingService driverConductorTrainingService;
	private GrievanceManagementService grievanceManagementService;
	private CommonService commonService;

	private MaintainTrainingScheduleDTO maintainTrainingScheduleDTO = new MaintainTrainingScheduleDTO();

	private String sucessMsg;
	private String errorMsg;

	private List<DriverConductorTrainingDTO> driverConductorList;
	private DriverConductorTrainingDTO selectedRow;
	private List<CommonDTO> trainingTypeList;
	private List<DropDownDTO> applicationNoList;
	private List<DropDownDTO> driverList;
	private List<DropDownDTO> conductorList;
	private String selectedTrainingType;
	private Date trainingDate;
	private String applicationNo;
	private String driverId;
	private String conductorId;
	private boolean listAll; // In initial loading show only items to be printed
								// > listAll = false, When Search button is
								// clicked list down all data > listAll = true
	private boolean paymentOkeyForDuplicates = true;

	public DriverConductorPrintIDsBackingBean() {
		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");
		grievanceManagementService = (GrievanceManagementService) SpringApplicationContex
				.getBean("grievanceManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		trainingTypeList = driverConductorTrainingService.GetAllTrainingTypes();
		applicationNoList = grievanceManagementService.getApplicationNos();
		driverList = grievanceManagementService.getDrivers();
		conductorList = grievanceManagementService.getConductors();
		clearSearchView();
	}

	public void clearSearchView() {
		selectedTrainingType = "";
		trainingDate = null;
		applicationNo = null;
		driverId = null;
		conductorId = null;
		driverConductorList = new ArrayList<DriverConductorTrainingDTO>();
		selectedRow = new DriverConductorTrainingDTO();
		listAll = false;
		loadCertificateInfo();
		paymentOkeyForDuplicates = true;
	}

	public void search() {
		if ((selectedTrainingType == null || selectedTrainingType.isEmpty()) && trainingDate == null
				&& (applicationNo == null || applicationNo.isEmpty()) && (driverId == null || driverId.isEmpty())
				&& (conductorId == null || conductorId.isEmpty())) {
			showMsg("ERROR", "Please enter search values");
			return;
		}

		listAll = true;
		loadCertificateInfo();

		if (driverConductorList == null || driverConductorList.isEmpty())
			showMsg("ERROR", "No Data found.");
	}

	private void loadCertificateInfo() {
		driverConductorList = driverConductorTrainingService.getCertificateInfo(listAll, selectedTrainingType,
				trainingDate, applicationNo, driverId, conductorId);
		selectedRow = new DriverConductorTrainingDTO();
	}

	public void printCards() {
		String user = sessionBackingBean.loginUser;
		String trainingType = driverConductorTrainingService.getTrainingTypeByAppNo(selectedRow.getAppNo());
		maintainTrainingScheduleDTO.setTrainingDate(trainingDate);

		if (selectedRow == null || selectedRow.getAppNo() == null) {
			showMsg("ERROR", "Please select a record");
			return;
		}
		if (selectedRow.getTrainingDate() != null && selectedRow.getStatusTypeCode() != null
				&& selectedRow.getStatusTypeCode().equalsIgnoreCase("A")) {
			if (!driverConductorTrainingService.updateStatus("CP", "A", selectedRow.getStatusTypeCode(),
					selectedRow.getStatusCode(), selectedRow.getAppNo(), user)) {

				showMsg("ERROR", "Error printing cards");
				return;
			}
			driverConductorTrainingService.beanLinkMethod(selectedRow, user, "Print Cards", "Printing Driver-Conductor ID Cards / Maintain Certificate Information");

			showMsg("SUCCESS", "Saved Successfully");
			loadCertificateInfo();
			/**
			 * check duplicate driver conductor payment complete for print
			 * button enable
			 **/

		} else if (trainingType.equals("DD") || trainingType.equals("DC")) {

			if (!driverConductorTrainingService.updateStatusAndInactivePrevious("CP", "A",
					selectedRow.getStatusTypeCode(), selectedRow.getStatusCode(), selectedRow.getAppNo(), user,
					selectedRow.getDriverConductorId())) {

				showMsg("ERROR", "Error printing cards");
				return;
			}

			driverConductorTrainingService.beanLinkMethod(selectedRow, user, "Print Cards", "Printing Driver-Conductor ID Cards / Maintain Certificate Information");
			showMsg("SUCCESS", "Saved Successfully");
			loadCertificateInfo();
			/**
			 * check duplicate driver conductor payment complete for print
			 * button enable
			 **/

		}

	}

	public void reprintCards() {
		if (selectedRow == null || selectedRow.getAppNo() == null) {
			showMsg("ERROR", "Please select a record");
			return;
		}
		// TODO increase print count

		loadCertificateInfo();
		driverConductorTrainingService.beanLinkMethod(selectedRow, sessionBackingBean.getLoginUser(), "Re Print Cards", "Printing Driver-Conductor ID Cards / Maintain Certificate Information");
	}

	public void onRowSelect(SelectEvent event) {
		selectedRow = (DriverConductorTrainingDTO) event.getObject();
		selectedRow.setIdPhotoPath(null);
		boolean isReceiptGenerated = false;
		/** get selected application number's training type **/
		String trainingType = driverConductorTrainingService.getTrainingTypeByAppNo(selectedRow.getAppNo());
		/*** end ***/

		if (trainingType.equals("DD") || trainingType.equals("DC")) {
			/*** check receipt generated or not ***/
			isReceiptGenerated = driverConductorTrainingService.receiptGenerated(selectedRow.getAppNo());

			if (isReceiptGenerated) {
				paymentOkeyForDuplicates = true;
			} else {
				paymentOkeyForDuplicates = false;
			}

		}
		byte[] bytes = driverConductorTrainingService.getDriverConductorPhoto(selectedRow.getAppNo());

		if (bytes != null) {

			try {
				String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
				Properties props = PropertyReader.loadPropertyFile();

				File dir = new File(path + props.getProperty("driverconductor.idcards.temp.images.path"));
				String filePath = path + props.getProperty("driverconductor.idcards.temp.images.path")
						+ selectedRow.getAppNo() + ".png";

				if (!dir.exists())
					dir.mkdir();
				else
					FileUtils.cleanDirectory(dir);

				FileUtils.writeByteArrayToFile(new File(filePath), bytes);

				selectedRow.setIdPhotoPath(filePath);

			} catch (java.io.IOException e) {
				e.printStackTrace();
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}

	}

	public void onRowUnSelect(UnselectEvent event) {
		selectedRow = new DriverConductorTrainingDTO();
		paymentOkeyForDuplicates = true;
	}

	public void changeTrainingType() {
		getApplicationNos(selectedTrainingType);
		getDrivers(selectedTrainingType);
		getConductors(selectedTrainingType);
	}

	private void getApplicationNos(String trainingType) {
		if (trainingType != null && !trainingType.trim().isEmpty())
			applicationNoList = grievanceManagementService.getApplicationNoByTrainingType(trainingType);
		else
			applicationNoList = grievanceManagementService.getApplicationNos();
	}

	private void getDrivers(String trainingType) {
		if (trainingType != null && !trainingType.trim().isEmpty())
			driverList = grievanceManagementService.getDriversByTrainingType(trainingType);
		else
			driverList = grievanceManagementService.getDrivers();
	}

	private void getConductors(String trainingType) {
		if (trainingType != null && !trainingType.trim().isEmpty())
			conductorList = grievanceManagementService.getConductorsByTrainingType(trainingType);
		else
			conductorList = grievanceManagementService.getConductors();
	}

	/*
	 * Common methodss
	 */

	public void showMsg(String type, String msg) {
		if (type.equalsIgnoreCase("ERROR")) {
			errorMsg = msg;
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		} else {
			sucessMsg = msg;
			RequestContext.getCurrentInstance().update("frmSuccess");
			RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
		}
	}

	private static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	//////////////// getter & setters /////////////////////

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public GrievanceManagementService getGrievanceManagementService() {
		return grievanceManagementService;
	}

	public void setGrievanceManagementService(GrievanceManagementService grievanceManagementService) {
		this.grievanceManagementService = grievanceManagementService;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public String getSelectedTrainingType() {
		return selectedTrainingType;
	}

	public void setSelectedTrainingType(String selectedTrainingType) {
		this.selectedTrainingType = selectedTrainingType;
	}

	public DriverConductorTrainingService getDriverConductorTrainingService() {
		return driverConductorTrainingService;
	}

	public void setDriverConductorTrainingService(DriverConductorTrainingService driverConductorTrainingService) {
		this.driverConductorTrainingService = driverConductorTrainingService;
	}

	public List<CommonDTO> getTrainingTypeList() {
		return trainingTypeList;
	}

	public void setTrainingTypeList(List<CommonDTO> trainingTypeList) {
		this.trainingTypeList = trainingTypeList;
	}

	public List<DriverConductorTrainingDTO> getDriverConductorList() {
		return driverConductorList;
	}

	public void setDriverConductorList(List<DriverConductorTrainingDTO> driverConductorList) {
		this.driverConductorList = driverConductorList;
	}

	public List<DropDownDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<DropDownDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public List<DropDownDTO> getDriverList() {
		return driverList;
	}

	public void setDriverList(List<DropDownDTO> driverList) {
		this.driverList = driverList;
	}

	public List<DropDownDTO> getConductorList() {
		return conductorList;
	}

	public void setConductorList(List<DropDownDTO> conductorList) {
		this.conductorList = conductorList;
	}

	public Date getTrainingDate() {
		return trainingDate;
	}

	public void setTrainingDate(Date trainingDate) {
		this.trainingDate = trainingDate;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getConductorId() {
		return conductorId;
	}

	public void setConductorId(String conductorId) {
		this.conductorId = conductorId;
	}

	public boolean isListAll() {
		return listAll;
	}

	public DriverConductorTrainingDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(DriverConductorTrainingDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public void setListAll(boolean listAll) {
		this.listAll = listAll;
	}

	public boolean isPaymentOkeyForDuplicates() {
		return paymentOkeyForDuplicates;
	}

	public void setPaymentOkeyForDuplicates(boolean paymentOkeyForDuplicates) {
		this.paymentOkeyForDuplicates = paymentOkeyForDuplicates;
	}

	public MaintainTrainingScheduleDTO getMaintainTrainingScheduleDTO() {
		return maintainTrainingScheduleDTO;
	}

	public void setMaintainTrainingScheduleDTO(MaintainTrainingScheduleDTO maintainTrainingScheduleDTO) {
		this.maintainTrainingScheduleDTO = maintainTrainingScheduleDTO;
	}
}
