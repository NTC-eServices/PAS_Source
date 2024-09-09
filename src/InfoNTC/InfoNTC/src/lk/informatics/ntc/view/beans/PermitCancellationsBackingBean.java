package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.service.CancellationsPermitService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.model.service.PermitRenewalsService;
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

@ManagedBean(name = "permitCancellationsBackingBean")
@ViewScoped
public class PermitCancellationsBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private CancellationsPermitService cancellationsPermitService;
	private List<PermitDTO> permitNoList = new ArrayList<PermitDTO>(0);
	private List<PermitDTO> busRegNoList = new ArrayList<PermitDTO>(0);
	private PermitDTO permitDTO, selectDTO, viewSelect, selectPermitDTO, viewDTO;
	private String errorMessage, successMessage, alertMSG, rejectReason;
	private List<PermitDTO> cancellationList;
	private boolean diableONETWO = true, disablePrint = true, disabledCheckBox;
	private Date cancelTo, cancelFrom, date;
	private boolean temporaryCancel, checkBoxClicked, searched, readOnly;
	private PermitRenewalsDTO applicationHistoryDTO;
	private PermitRenewalsService permitRenewalsService;
	private HistoryService historyService;
	public StreamedContent files;
	public String appNo;

	public PermitCancellationsBackingBean() {
		cancellationsPermitService = (CancellationsPermitService) SpringApplicationContex
				.getBean("cancellationsPermitService");
		permitRenewalsService = (PermitRenewalsService) SpringApplicationContex.getBean("permitRenewalsService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");

		permitDTO = new PermitDTO();
		viewDTO = new PermitDTO();
		loadValues();
		readOnly = false;
		applicationHistoryDTO = new PermitRenewalsDTO();
	}

	public void loadValues() {
		cancellationList = new ArrayList<PermitDTO>();
		cancellationList = cancellationsPermitService.getdefaultData();
		busRegNoList = cancellationsPermitService.getExpiredBusRegNo();
		permitNoList = cancellationsPermitService.getExpiredPermitNo();
		diableONETWO = true;
		disablePrint = true;
	}

	public void completePermitNo() {
		permitDTO = cancellationsPermitService.completePermitNoData(permitDTO);
	}

	public void completevehicleNO() {
		permitDTO = cancellationsPermitService.completeVehicleNoData(permitDTO);
	}

	public void search() {
		if ((permitDTO.getBusRegNo() == null || permitDTO.getBusRegNo().trim().equalsIgnoreCase(""))
				&& (permitDTO.getPermitNo() == null || permitDTO.getPermitNo().trim().equalsIgnoreCase(""))
				&& (date == null)) {

			setErrorMessage("Please Select a Filed to Search");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {

			cancellationList = new ArrayList<PermitDTO>();
			cancellationList = cancellationsPermitService.getCancellationData(permitDTO, date);

			if (!cancellationList.isEmpty()) {
				diableONETWO = false;
				disablePrint = true;
				disabledCheckBox = true;
				searched = true;

			} else {
				setAlertMSG("No records found for the selected data.");
				RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
			}
		}
	}

	public void clearOne() {
		permitDTO = new PermitDTO();
		date = null;
		selectDTO = new PermitDTO();
	}

	public void closeTemporaryCancellation() {
		permitDTO.setCancelFrom(null);
		permitDTO.setCancelTO(null);
		permitDTO.setTempcancelReason(null);
		selectDTO.setTemporary(true);
		RequestContext.getCurrentInstance().update("box");
		RequestContext.getCurrentInstance().update("dataTable");
		RequestContext.getCurrentInstance().execute("PF('temporaryCancellationDB').hide()");

	}

	public void unSelectRow(UnselectEvent event) {
		((PermitDTO) event.getObject()).setTemporary(true);
		RequestContext.getCurrentInstance().update("box");
		RequestContext.getCurrentInstance().update("dataTable");
	}

	public void selectRow(SelectEvent event) {
		diableONETWO = false;
		((PermitDTO) event.getObject()).setTemporary(true);
		((PermitDTO) event.getObject()).setTemporary(false);
		setCheckBoxClicked(true);
		cancelFrom = null;
		cancelTo = null;
		permitDTO.setTempcancelReason(null);

		appNo = selectDTO.getApplicationNo();
		if (selectDTO.getCancelType() != null && selectDTO.getCancelType().equalsIgnoreCase("T")) {
			selectDTO.setTemporary(true);
		} else {
			selectDTO.setTemporary(false);
		}

		RequestContext.getCurrentInstance().update("frmTemporaryCancellation");
	}

	public void checkBoxSelect() {

	}

	public void clearTwo() {
		cancellationList = new ArrayList<PermitDTO>();
		permitDTO = new PermitDTO();
		permitDTO.setRejectReason(null);
		diableONETWO = true;
		disablePrint = true;
		cancellationList = new ArrayList<PermitDTO>();
		cancellationList = cancellationsPermitService.getdefaultData();
		cancelFrom = null;
		cancelTo = null;
		permitDTO.setTempcancelReason(null);
		readOnly = false;
		RequestContext.getCurrentInstance().update("frmTemporaryCancellation");
		selectDTO = new PermitDTO();
	}

	public void view() {

		if (viewSelect.getApplicationNo() != null && !viewSelect.getApplicationNo().trim().equalsIgnoreCase("")) {
			viewDTO = cancellationsPermitService.getViewData(viewSelect);
			RequestContext.getCurrentInstance().execute("PF('renewalPermitDetailsDB').show()");

		} else {
			setErrorMessage("Can not find details.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void saveTemporaryCancellation() throws Exception {

		if (permitDTO.getTempcancelReason() != null && !permitDTO.getTempcancelReason().trim().equalsIgnoreCase("")) {

			if (cancelFrom != null) {

				if (cancelTo != null) {

					if (cancelFrom.before(cancelTo) == true) {

						java.sql.Date sqlFromDate = new java.sql.Date(cancelFrom.getTime());
						java.sql.Date sqlToDate = new java.sql.Date(cancelTo.getTime());

						permitDTO.setCancelFrom(sqlFromDate);
						permitDTO.setCancelTO(sqlToDate);
						permitDTO.setTempcancelReason(permitDTO.getTempcancelReason());

						RequestContext.getCurrentInstance().execute("PF('temporaryCancellationDB').hide()");
						readOnly = true;
						RequestContext.getCurrentInstance().update("box");
						RequestContext.getCurrentInstance().update("dataTable");
						temporaryCancel = true;

					} else {
						setErrorMessage("Cancel To Date should be greater than Cancel From Date");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please Select the Cancel To Date");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Please Select the Cancel From Date");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Enter the Cancel Reason");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void preReject() {
		if (selectDTO != null) {

			if (selectDTO.getApplicationNo() != null && selectDTO.getPermitNo() != null
					&& selectDTO.getBusRegNo() != null) {

				boolean isAvailabel = cancellationsPermitService.checkDataInPermitCancellation(selectDTO, "R");

				if (isAvailabel == false) {
					RequestContext.getCurrentInstance().execute("PF('dlg1').show()");
				} else {
					setErrorMessage("Already Rejected");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Selected row does not have mandatory data.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void rejectCancel() {
		RequestContext.getCurrentInstance().execute("PF('dlg1').hide()");
		rejectReason = null;

	}

	/*
	 * changed method success message logic and added transactions to method service
	 * methods : dhananjika.d (04/07/2024)
	 */ 
	public void approve() {

		boolean isupdateApllicationStatus = false;
		boolean isInsertInToCancellation = false;
		String logUser = sessionBackingBean.getLoginUser();

		if (selectDTO != null) {

			if (selectDTO.getApplicationNo() != null && selectDTO.getPermitNo() != null
					&& selectDTO.getBusRegNo() != null) {

				boolean isApproved = cancellationsPermitService.checkApplicationTabel(selectDTO, "C");

				if (isApproved == false) {

					boolean isAvailabel = cancellationsPermitService.checkDataInPermitCancellation(selectDTO, "R");

					/* Save Application Data */
					applicationHistoryDTO = historyService.getApplicationTableData(selectDTO.getApplicationNo(),
							sessionBackingBean.getLoginUser());

					if (isAvailabel == false) {

						isInsertInToCancellation = cancellationsPermitService
								.insertPermitCancleApproveDetails(selectDTO, permitDTO, logUser, temporaryCancel);

						if(isInsertInToCancellation) {
							isupdateApllicationStatus = cancellationsPermitService.updateApplicationTableStatus(selectDTO,
									sessionBackingBean.getLoginUser(), temporaryCancel);
						}			

					} else {

						isInsertInToCancellation = cancellationsPermitService
								.updatePermitCancleApproveDetails(selectDTO, permitDTO, logUser, temporaryCancel);

						if(isInsertInToCancellation) {
							isupdateApllicationStatus = cancellationsPermitService.updateApplicationTableStatus(selectDTO,
									sessionBackingBean.getLoginUser(), temporaryCancel);
						}
					}

					if (isupdateApllicationStatus == true && isInsertInToCancellation == true
							&& temporaryCancel == true) {

						/* Insert Data to history table */
						boolean historyInsertSuccess = historyService.insertApplicationHistoryData(applicationHistoryDTO);

						if(!historyInsertSuccess) {
							setErrorMessage("History Table Update Unsuccessful");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}else {							
							if ((permitDTO.getBusRegNo() == null || permitDTO.getBusRegNo().trim().equalsIgnoreCase(""))
									&& (permitDTO.getPermitNo() == null
											|| permitDTO.getPermitNo().trim().equalsIgnoreCase(""))
									&& (date == null)) {

								loadValues();
							} else {
								search();
							}

							permitDTO.setCancelFrom(null);
							permitDTO.setCancelTO(null);
							permitDTO.setTempcancelReason(null);

							temporaryCancel = false;

							boolean isTaskFound = cancellationsPermitService.checkTaskDetails(selectDTO, "PM402", "C");

							if (isTaskFound == true) {
								if(cancellationsPermitService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, logUser, "PM401")) {		
									if(cancellationsPermitService.deleteTaskDetails(selectDTO, "PM401")) {
										selectDTO = new PermitDTO();
										
										setSuccessMessage("Temporary Permit Cancellations Success.");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
										disablePrint = false;
									}else {
										setErrorMessage("Task Detail Update Unsuccessful");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
								}else {
									setErrorMessage("Task Detail Update Unsuccessful");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
								
								
							} else {
//								cancellationsPermitService.insertTaskDetails(selectDTO, logUser, "PM402", "C");
//								cancellationsPermitService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, logUser, "PM401");
//								cancellationsPermitService.deleteTaskDetails(selectDTO, "PM401");
//								selectDTO = new PermitDTO();
								if(cancellationsPermitService.insertTaskDetails(selectDTO, logUser, "PM402", "C")) {
									if(cancellationsPermitService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, logUser, "PM401")) {		
										if(cancellationsPermitService.deleteTaskDetails(selectDTO, "PM401")) {
											selectDTO = new PermitDTO();
											
											setSuccessMessage("Temporary Permit Cancellations Success.");
											RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
											disablePrint = false;
										}else {
											setErrorMessage("Task Detail Update Unsuccessful");
											RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
										}
									}else {
										setErrorMessage("Task Detail Update Unsuccessful");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
								}else {
									setErrorMessage("Task Detail Update Unsuccessful");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
								
							}
							
							
						}

					} else if (isupdateApllicationStatus == true && isInsertInToCancellation == true
							&& temporaryCancel == false) {

						/* Insert Data to history table */	
						boolean historyInsertSuccess = historyService.insertApplicationHistoryData(applicationHistoryDTO);

						if(!historyInsertSuccess) {
							setErrorMessage("History Table Update Unsuccessful");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}else {
							if ((permitDTO.getBusRegNo() == null || permitDTO.getBusRegNo().trim().equalsIgnoreCase(""))
									&& (permitDTO.getPermitNo() == null
											|| permitDTO.getPermitNo().trim().equalsIgnoreCase(""))
									&& (date == null)) {

								loadValues();
							} else {
								search();
							}

							boolean isTaskFound = cancellationsPermitService.checkTaskDetails(selectDTO, "PM402", "C");

//							if (isTaskFound == true) {
//  							cancellationsPermitService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, logUser, "PM401");
//  							cancellationsPermitService.deleteTaskDetails(selectDTO, "PM401");
//  							selectDTO = new PermitDTO();
//							} else {
//								cancellationsPermitService.insertTaskDetails(selectDTO, logUser, "PM402", "C");
//								cancellationsPermitService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, logUser, "PM401");
//								cancellationsPermitService.deleteTaskDetails(selectDTO, "PM401");
//								selectDTO = new PermitDTO();
//							}			
							
							if (isTaskFound == true) {
								if(cancellationsPermitService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, logUser, "PM401")) {		
									if(cancellationsPermitService.deleteTaskDetails(selectDTO, "PM401")) {
										selectDTO = new PermitDTO();
										
										setSuccessMessage("Temporary Permit Cancellations Success.");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
										disablePrint = false;
									}else {
										setErrorMessage("Task Detail Update Unsuccessful");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
								}else {
									setErrorMessage("Task Detail Update Unsuccessful");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
								
								
							} else {
								if(cancellationsPermitService.insertTaskDetails(selectDTO, logUser, "PM402", "C")) {
									if(cancellationsPermitService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, logUser, "PM401")) {		
										if(cancellationsPermitService.deleteTaskDetails(selectDTO, "PM401")) {
											selectDTO = new PermitDTO();
											
											setSuccessMessage("Temporary Permit Cancellations Success.");
											RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
											disablePrint = false;
										}else {
											setErrorMessage("Task Detail Update Unsuccessful");
											RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
										}
									}else {
										setErrorMessage("Task Detail Update Unsuccessful");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
								}else {
									setErrorMessage("Task Detail Update Unsuccessful");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
								
							}
						}
					} else {
						setErrorMessage("Temporary Permit Cancellations Fail.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Application No. Already Cancel");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Selected row does not have mandatory data.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void reject() {
		String logUser = sessionBackingBean.getLoginUser();
		if (rejectReason != null && !rejectReason.trim().equalsIgnoreCase("")) {

			permitDTO.setRejectReason(rejectReason);

			boolean isreject = cancellationsPermitService.insertPermitCancleRejectDetails(selectDTO, permitDTO,
					logUser);

			if (isreject == true) {

				cancellationList = new ArrayList<PermitDTO>();
				cancellationList = cancellationsPermitService.getdefaultData();

				setSuccessMessage("Permit Cancellations Reject Success.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				rejectReason = null;

				boolean isTaskFound = cancellationsPermitService.checkTaskDetails(selectDTO, "PM402", "C");

				if (isTaskFound == true) {
					cancellationsPermitService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, logUser, "PM401");
					cancellationsPermitService.deleteTaskDetails(selectDTO, "PM401");
				} else {
					cancellationsPermitService.insertTaskDetails(selectDTO, logUser, "PM402", "C");
					cancellationsPermitService.CopyTaskDetailsANDinsertTaskHistory(selectDTO, logUser, "PM401");
					cancellationsPermitService.deleteTaskDetails(selectDTO, "PM401");
				}

			} else {
				setErrorMessage("Permit Cancellation Reject Fail");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please enter the reject reason.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public StreamedContent printCancellationLetter() throws JRException {

		files = null;
		String sourceFileName = null;
		Connection conn = null;
		String reprintLabel = null;
		boolean checkData = false;
		checkData = cancellationsPermitService.checkDataForReport(appNo);
		try {
			conn = ConnectionManager.getConnection();
			sourceFileName = "..//reports//PermitCancellationLetter.jrxml";
			String logopath = "//lk//informatics//ntc//view//reports//";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_AppNO", appNo);
			parameters.put("P_LOGO", logopath);
			parameters.put("P_REPRINT", reprintLabel);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "permitCancelLeter.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			if (checkData) {

				cancellationsPermitService.updateCancelLetterPrintStatus(appNo);
			}
			setDisablePrint(true);

			return files;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			ConnectionManager.close(conn);
		}

	}

	public CancellationsPermitService getCancellationsPermitService() {
		return cancellationsPermitService;
	}

	public void setCancellationsPermitService(CancellationsPermitService cancellationsPermitService) {
		this.cancellationsPermitService = cancellationsPermitService;
	}

	public PermitDTO getPermitDTO() {
		return permitDTO;
	}

	public void setPermitDTO(PermitDTO permitDTO) {
		this.permitDTO = permitDTO;
	}

	public List<PermitDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<PermitDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<PermitDTO> getBusRegNoList() {
		return busRegNoList;
	}

	public void setBusRegNoList(List<PermitDTO> busRegNoList) {
		this.busRegNoList = busRegNoList;
	}

	public PermitDTO getSelectDTO() {
		return selectDTO;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public void setSelectDTO(PermitDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public Date getDate() {
		return date;
	}

	public boolean isCheckBoxClicked() {
		return checkBoxClicked;
	}

	public void setCheckBoxClicked(boolean checkBoxClicked) {
		this.checkBoxClicked = checkBoxClicked;
	}

	public boolean isTemporaryCancel() {
		return temporaryCancel;
	}

	public void setTemporaryCancel(boolean temporaryCancel) {
		this.temporaryCancel = temporaryCancel;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public List<PermitDTO> getCancellationList() {
		return cancellationList;
	}

	public boolean isDiableONETWO() {
		return diableONETWO;
	}

	public void setDiableONETWO(boolean diableONETWO) {
		this.diableONETWO = diableONETWO;
	}

	public void setCancellationList(List<PermitDTO> cancellationList) {
		this.cancellationList = cancellationList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isDisablePrint() {
		return disablePrint;
	}

	public void setDisablePrint(boolean disablePrint) {
		this.disablePrint = disablePrint;
	}

	public PermitDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(PermitDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public PermitDTO getSelectPermitDTO() {
		return selectPermitDTO;
	}

	public void setSelectPermitDTO(PermitDTO selectPermitDTO) {
		this.selectPermitDTO = selectPermitDTO;
	}

	public Date getCancelTo() {
		return cancelTo;
	}

	public void setCancelTo(Date cancelTo) {
		this.cancelTo = cancelTo;
	}

	public Date getCancelFrom() {
		return cancelFrom;
	}

	public void setCancelFrom(Date cancelFrom) {
		this.cancelFrom = cancelFrom;
	}

	public boolean isDisabledCheckBox() {
		return disabledCheckBox;
	}

	public void setDisabledCheckBox(boolean disabledCheckBox) {
		this.disabledCheckBox = disabledCheckBox;
	}

	public PermitDTO getViewDTO() {
		return viewDTO;
	}

	public void setViewDTO(PermitDTO viewDTO) {
		this.viewDTO = viewDTO;
	}

	public boolean isSearched() {
		return searched;
	}

	public void setSearched(boolean searched) {
		this.searched = searched;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public PermitRenewalsDTO getApplicationHistoryDTO() {
		return applicationHistoryDTO;
	}

	public void setApplicationHistoryDTO(PermitRenewalsDTO applicationHistoryDTO) {
		this.applicationHistoryDTO = applicationHistoryDTO;
	}

	public PermitRenewalsService getPermitRenewalsService() {
		return permitRenewalsService;
	}

	public void setPermitRenewalsService(PermitRenewalsService permitRenewalsService) {
		this.permitRenewalsService = permitRenewalsService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

}
