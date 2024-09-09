package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.AccessPermissionDTO;
import lk.informatics.ntc.model.dto.CommittedOffencesDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.ManageInvestigationDTO;
import lk.informatics.ntc.model.service.AccessPermissionService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.ManageInvestigationService;
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

@ManagedBean(name = "manageInvestigationChargesBean")
@ViewScoped
public class ManageInvestigationChargesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@ManagedProperty(value = "#{flyingSquadChargeSheetBackingBean}")
	private FlyingSquadChargeSheetBackingBean flyingSquadChargeSheetBackingBean;

	private FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO;

	private ManageInvestigationService manageInvestigationService;
	private AccessPermissionService accessPermissionService;

	private ManageInvestigationDTO manageInvestigationDTO;
	private ManageInvestigationDTO selectedInvestigation;
	private ManageInvestigationDTO chargeFinalization;

	private List<DropDownDTO> actionList;
	private List<AccessPermissionDTO> deptList;
	private List<DropDownDTO> attemptList;
	private List<DropDownDTO> driverList;
	private List<DropDownDTO> conductorList;

	private List<ManageInvestigationDTO> chargeRefList;
	private List<ManageInvestigationDTO> investigationsList;
	private List<ManageInvestigationDTO> chargesList;
	private DriverConductorRegistrationDTO driverData;
	private DriverConductorRegistrationDTO conductorData;
	private ManageInvestigationDTO selectDTO;
	private boolean itemSelected;

	private BigDecimal totalChargeAmount;
	private BigDecimal totalDriverPoints;
	private BigDecimal totalConductorPoints;

	private String successMsg;
	private String errorMsg;

	private boolean enableChargeFinalization;
	private boolean enableCreateVoucher;
	private boolean enableCancelVoucher;
	private boolean enablePrintVoucher;

	private String currStatus;
	private boolean updateCurrentStatus;

	private String viewType; // O,D,C
	static String OWNER = "O";
	static String DRIVER = "D";
	static String CONDUCTOR = "C";

	private boolean newDriver;
	private boolean newConductor;

	private BigDecimal totDriverPoints;
	private BigDecimal totConductorPoints;

	private boolean saveDriverConductor;
	private boolean enableSaveDriverConductor;

	private ManageInvestigationDTO savedActions;
	private StreamedContent files;
	private boolean enableActionPanel;
	private boolean enableInitiateDriverConductor;
	private boolean enableUpdateDriverConductor;

	private String chargeReferenceNo;
	private String applicationStatus;
	private String applicationCurrentStatus;
	private boolean enableDriverCheck;
	private boolean enableConductorCheck;
	private String attemptDescN;
	private boolean attemptChange;
	private BigDecimal latePaymentFee;
	private List<ComplaintRequestDTO> complaintHistoryList;
	private boolean historySelected;
	private List<CommittedOffencesDTO> violationConditionList;
	private ComplaintRequestDTO selectedHistory;
	private CommonService commonService;
	
	boolean disableBtn;
	boolean disableBtnCon;

	@PostConstruct
	public void init() {

		manageInvestigationService = (ManageInvestigationService) SpringApplicationContex
				.getBean("manageInvestigationService");

		accessPermissionService = (AccessPermissionService) SpringApplicationContex.getBean("accessPermissionService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		loadData();
	}

	public void loadData() {
		manageInvestigationDTO = new ManageInvestigationDTO();
		selectedInvestigation = new ManageInvestigationDTO();
		chargeFinalization = new ManageInvestigationDTO();
		driverData = new DriverConductorRegistrationDTO();
		conductorData = new DriverConductorRegistrationDTO();

		investigationsList = new ArrayList<ManageInvestigationDTO>();
		chargeRefList = new ArrayList<ManageInvestigationDTO>();
		chargesList = new ArrayList<ManageInvestigationDTO>();

		actionList = new ArrayList<DropDownDTO>();
		attemptList = new ArrayList<DropDownDTO>();
		driverList = new ArrayList<DropDownDTO>();
		conductorList = new ArrayList<DropDownDTO>();

		itemSelected = false;

		/*
		 * Load LOV data
		 */
		chargeRefList = manageInvestigationService.getChargeRefNoList();

		attemptList = manageInvestigationService.getAttempts();
		driverList = manageInvestigationService.getDrivers();
		conductorList = manageInvestigationService.getConductors();

		enableChargeFinalization = false;
		enableCreateVoucher = false;
		enableCancelVoucher = false;
		enablePrintVoucher = false;

		newDriver = false;
		newConductor = false;

		clearDriverData();
		clearConductorData();

		enableSaveDriverConductor = false;
		savedActions = new ManageInvestigationDTO();

		enableActionPanel = false;
		enableUpdateDriverConductor = false;
		enableDriverCheck = false;
		enableConductorCheck = false;
		enableInitiateDriverConductor = false;
		attemptChange =false;
		latePaymentFee = new BigDecimal(0);
		disableBtn = false;
		disableBtnCon = false;
	}

	public void searchButtonAction() {

		investigationsList = new ArrayList<ManageInvestigationDTO>();
		chargeFinalization = new ManageInvestigationDTO();
		selectedInvestigation = new ManageInvestigationDTO();
		itemSelected = false;

		enableActionPanel = false;
		enableUpdateDriverConductor = false;
		enableInitiateDriverConductor = false;

		if (manageInvestigationDTO.getStartInvestigationDate() == null
				&& manageInvestigationDTO.getEndInvestigationDate() != null) {
			showMsg("ERROR", "Please select Start date");
			return;
		} else if (manageInvestigationDTO.getStartInvestigationDate() != null
				&& manageInvestigationDTO.getEndInvestigationDate() == null) {
			showMsg("ERROR", "Please select End date");
			return;
		} else if (manageInvestigationDTO.getStartInvestigationDate() != null
				&& manageInvestigationDTO.getEndInvestigationDate() != null) {
			investigationsList = manageInvestigationService.searchInvestigationDetailsByDate(
					manageInvestigationDTO.getStartInvestigationDate(),
					manageInvestigationDTO.getEndInvestigationDate());
		} else {
			
			investigationsList = manageInvestigationService.searchInvestigationDetails(manageInvestigationDTO);
	
		}

		if (investigationsList == null || investigationsList.size() == 0) {
			showMsg("ERROR", "No Data found. Please change the search criteria and try again");
			return;
		} else {
			Collections.sort(investigationsList, new Comparator<ManageInvestigationDTO>() {
	            @Override
	            public int compare(ManageInvestigationDTO obj1, ManageInvestigationDTO obj2) {
	                // Use compareTo to compare the status strings in reverse order
	                return obj2.getStatus().compareTo(obj1.getStatus());
	            }
	        });
		}
		
		

		/*
		 * Set selected record
		 */
		if (investigationsList.size() == 1) {

			for (ManageInvestigationDTO dto : investigationsList) {
				selectedInvestigation = dto;
				
				if(selectedInvestigation != null && selectedInvestigation.getVehicleNo() != null) {
					String[] data = commonService.getOwnerByVehicleNo(selectedInvestigation.getVehicleNo());
					selectedInvestigation.setPermitOwnerNic(data[1]);
				}
				
			// To disable History button for empty data. dhananjika.d (19/04/2024) 
				if(selectedInvestigation.getDriverId() == null || selectedInvestigation.getDriverId().equalsIgnoreCase("")) {
					disableBtn = true;
				}if(selectedInvestigation.getConductorId() == null || selectedInvestigation.getConductorId().equalsIgnoreCase("")) {
					disableBtnCon = true;
				}
			}

			chargeFinalization = selectedInvestigation;

			itemSelected = true;

			chargeReferenceNo = selectedInvestigation.getChargeRefCode();
			applicationStatus = selectedInvestigation.getStatus();
			applicationCurrentStatus = currStatus;

			driverData.setDriverConductorId(selectedInvestigation.getDriverId());
			driverData = manageInvestigationService.getDriverConductorData(driverData);

			conductorData.setDriverConductorId(selectedInvestigation.getConductorId());
			conductorData = manageInvestigationService.getDriverConductorData(conductorData);

			DriverConductorRegistrationDTO savedDriver = manageInvestigationService
					.getConfirmedDriver(selectedInvestigation, selectedInvestigation.getChargeRefCode());

			if (savedDriver != null) {
				driverData = savedDriver;
				if (savedDriver.getDriverConductorId() != null) {
					newDriver = false;
					chargeFinalization.setDriverId(driverData.getDriverConductorId());
					selectedInvestigation.setDriverId(driverData.getDriverConductorId());

				} else {
					newDriver = true;
					chargeFinalization.setDriverId(driverData.getNic());
					selectedInvestigation.setDriverId(driverData.getNic());
				}

				chargeFinalization.setDriverName(driverData.getFullNameEng());

			}

			DriverConductorRegistrationDTO savedConductor = manageInvestigationService
					.getConfirmedConductor(selectedInvestigation, selectedInvestigation.getChargeRefCode());
			if (savedConductor != null) {
				conductorData = savedConductor;
				if (savedConductor.getDriverConductorId() != null) {
					newConductor = false;
					chargeFinalization.setConductorId(conductorData.getDriverConductorId());
					selectedInvestigation.setConductorId(conductorData.getDriverConductorId());
				} else {
					newConductor = true;
					chargeFinalization.setConductorId(conductorData.getNic());
					selectedInvestigation.setConductorId(conductorData.getNic());
				}
				chargeFinalization.setConductorName(conductorData.getFullNameEng());
			}

			totDriverPoints = chargeFinalization.getTotalDriverPoints();
			totConductorPoints = chargeFinalization.getTotalConductorPoints();

			boolean checkInitiated = manageInvestigationService.checkDriverConductorDet(selectedInvestigation);

			if (!selectedInvestigation.getStatus().equalsIgnoreCase("P")) {

				if (!checkInitiated) {

					if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("MS")) {
						enableInitiateDriverConductor = true;
						enableUpdateDriverConductor = false;

						enableChargeFinalization = true;
						enableCreateVoucher = false;
						enableCancelVoucher = false;
						enablePrintVoucher = false;

						enableActionPanel = true;

					} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CS")) {
						enableInitiateDriverConductor = true;
						enableUpdateDriverConductor = false;

						enableChargeFinalization = true;
						enableCreateVoucher = false;
						enableCancelVoucher = false;
						enablePrintVoucher = false;

						enableActionPanel = true;

					} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CA")) {
						enableInitiateDriverConductor = true;
						enableUpdateDriverConductor = true;

						enableChargeFinalization = false;
						enableCreateVoucher = true;
						enableCancelVoucher = false;
						enablePrintVoucher = false;

						enableActionPanel = true;

						selectedInvestigation.setFinalAmount(manageInvestigationService
								.getChargeTotAmount(selectedInvestigation.getChargeRefCode()));

					} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CV")) {
						enableInitiateDriverConductor = true;
						enableUpdateDriverConductor = true;

						enableChargeFinalization = false;
						enableCreateVoucher = false;
						enableCancelVoucher = true;
						enablePrintVoucher = true;

						enableActionPanel = true;

					} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CP")) {
						enableInitiateDriverConductor = true;
						enableUpdateDriverConductor = true;

						enableChargeFinalization = false;
						enableCreateVoucher = false;
						enableCancelVoucher = true;
						enablePrintVoucher = true;

						enableActionPanel = true;
					} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CI")) {
						enableInitiateDriverConductor = true;
						enableUpdateDriverConductor = true;

						enableChargeFinalization = false;
						enableCreateVoucher = false;
						enableCancelVoucher = false;
						enablePrintVoucher = false;

						enableActionPanel = false;
					} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("RG")) {
						enableInitiateDriverConductor = true;
						enableUpdateDriverConductor = true;

						enableChargeFinalization = false;
						enableCreateVoucher = false;
						enableCancelVoucher = false;
						enablePrintVoucher = false;

						enableActionPanel = true;
					}

					else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CC")) {
						enableInitiateDriverConductor = true;
						enableChargeFinalization = true;

						enableActionPanel = true;

					} /**
						 * else if part added by tharushi.e for start charge
						 * finalizaion by cancelling voucher
						 **/

				}

			} else if (selectedInvestigation.getStatus().equalsIgnoreCase("P")) {
				enableInitiateDriverConductor = false;
				enableUpdateDriverConductor = true;

				enableChargeFinalization = false;

				enableActionPanel = false;
				enableChargeFinalization = false;
			}
			

		}

		RequestContext.getCurrentInstance().update("manageInvsFrm:panelDriver:panelConductor");
	}

	public void saveDriverConductorAction() {

		if (driverData != null || conductorData != null) {
			selectedInvestigation.setLoginUser(sessionBackingBean.getLoginUser());
			saveDriverConductor = manageInvestigationService.saveDriverConductorDet(selectedInvestigation, driverData,
					conductorData);

			if (saveDriverConductor) {
				manageInvestigationService.beanLinkMethod(selectedInvestigation, "Save Driver/Conductor Details");

				boolean saveMaster = manageInvestigationService.saveInvestigationMaster(selectedInvestigation, new BigDecimal(0));

				if (saveMaster) {
					showMsg("SUCCESS", "Successfully Initiated the Investigation");
					manageInvestigationService.beanLinkMethod(selectedInvestigation, "Initiated the Investigation");
					enableChargeFinalization = true;
					enableActionPanel = true;
				}

			} else {
				showMsg("ERROR", "Trouble Confirming Driver/Conductor Data");
			}
		}

		manageInvestigationDTO = new ManageInvestigationDTO();
		manageInvestigationDTO.setChargeRefCode(selectedInvestigation.getChargeRefCode());

		searchButtonAction();

		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void clearButtonAction() {

		loadData();

		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void onRowSelect(SelectEvent event) {

		itemSelected = true;

		chargeReferenceNo = selectedInvestigation.getChargeRefCode();
		applicationStatus = selectedInvestigation.getStatus();
		applicationCurrentStatus = currStatus;

		enableActionPanel = false;
		enableUpdateDriverConductor = false;
		enableInitiateDriverConductor = false;

		driverData.setDriverConductorId(selectedInvestigation.getDriverId());
		driverData = manageInvestigationService.getDriverConductorData(driverData);

		conductorData.setDriverConductorId(selectedInvestigation.getConductorId());
		conductorData = manageInvestigationService.getDriverConductorData(conductorData);

		DriverConductorRegistrationDTO savedDriver = manageInvestigationService
				.getConfirmedDriver(selectedInvestigation, selectedInvestigation.getChargeRefCode());
		
		if(selectedInvestigation != null && selectedInvestigation.getVehicleNo() != null) {
			String[] data = commonService.getOwnerByVehicleNo(selectedInvestigation.getVehicleNo());
			selectedInvestigation.setPermitOwnerNic(data[1]);
		}

		if (savedDriver != null) {
			driverData = savedDriver;
			if (savedDriver.getDriverConductorId() != null) {
				newDriver = false;
				chargeFinalization.setDriverId(driverData.getDriverConductorId());
				selectedInvestigation.setDriverId(driverData.getDriverConductorId());

			} else {
				newDriver = true;
				chargeFinalization.setDriverId(driverData.getNic());
				selectedInvestigation.setDriverId(driverData.getNic());
			}

			chargeFinalization.setDriverName(driverData.getFullNameEng());

		}

		DriverConductorRegistrationDTO savedConductor = manageInvestigationService
				.getConfirmedConductor(selectedInvestigation, selectedInvestigation.getChargeRefCode());
		if (savedConductor != null) {
			conductorData = savedConductor;
			if (savedConductor.getDriverConductorId() != null) {
				newConductor = false;
				chargeFinalization.setConductorId(conductorData.getDriverConductorId());
				selectedInvestigation.setConductorId(conductorData.getDriverConductorId());
			} else {
				newConductor = true;
				chargeFinalization.setConductorId(conductorData.getNic());
				selectedInvestigation.setConductorId(conductorData.getNic());
			}
			chargeFinalization.setConductorName(conductorData.getFullNameEng());
		}

		totDriverPoints = chargeFinalization.getTotalDriverPoints();
		totConductorPoints = chargeFinalization.getTotalConductorPoints();

		boolean checkInitiated = manageInvestigationService.checkDriverConductorDet(selectedInvestigation);

		if (!selectedInvestigation.getStatus().equalsIgnoreCase("P")) {

			if (!checkInitiated) {

				if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("MS")) {
					enableInitiateDriverConductor = true;
					enableUpdateDriverConductor = false;

					enableChargeFinalization = true;
					enableCreateVoucher = false;
					enableCancelVoucher = false;
					enablePrintVoucher = false;

					enableActionPanel = true;

				} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CS")) {
					enableInitiateDriverConductor = true;
					enableUpdateDriverConductor = false;

					enableChargeFinalization = true;
					enableCreateVoucher = false;
					enableCancelVoucher = false;
					enablePrintVoucher = false;

					enableActionPanel = true;

				} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CA")) {
					enableInitiateDriverConductor = true;
					enableUpdateDriverConductor = true;

					enableChargeFinalization = false;
					enableCreateVoucher = true;
					enableCancelVoucher = false;
					enablePrintVoucher = false;

					enableActionPanel = true;

					selectedInvestigation.setFinalAmount(
							manageInvestigationService.getChargeTotAmount(selectedInvestigation.getChargeRefCode()));

				} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CV")) {
					enableInitiateDriverConductor = true;
					enableUpdateDriverConductor = true;

					enableChargeFinalization = false;
					enableCreateVoucher = false;
					enableCancelVoucher = true;
					enablePrintVoucher = true;

					enableActionPanel = true;

				} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CP")) {
					enableInitiateDriverConductor = true;
					enableUpdateDriverConductor = true;

					enableChargeFinalization = false;
					enableCreateVoucher = false;
					enableCancelVoucher = true;
					enablePrintVoucher = true;

					enableActionPanel = true;
				} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CI")) {
					enableInitiateDriverConductor = true;
					enableUpdateDriverConductor = true;

					enableChargeFinalization = false;
					enableCreateVoucher = false;
					enableCancelVoucher = false;
					enablePrintVoucher = false;

					enableActionPanel = false;
				} else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("RG")) {
					enableInitiateDriverConductor = true;
					enableUpdateDriverConductor = true;

					enableChargeFinalization = false;
					enableCreateVoucher = false;
					enableCancelVoucher = false;
					enablePrintVoucher = false;

					enableActionPanel = true;
				}

				else if (selectedInvestigation.getCurrentStatus().equalsIgnoreCase("CC")) {
					enableInitiateDriverConductor = false;
					enableChargeFinalization = false;

					enableActionPanel = true;

				} /**
					 * else if part added by tharushi.e for start charge
					 * finalizaion by cancelling voucher
					 **/

			}
		} else if (selectedInvestigation.getStatus().equalsIgnoreCase("P")) {
			enableInitiateDriverConductor = false;
			enableUpdateDriverConductor = true;

			enableChargeFinalization = false;

			enableActionPanel = false;
			enableChargeFinalization = false;
		}

		RequestContext.getCurrentInstance().update("manageInvsFrm:panelDriver:panelConductor");

	}

	public void onRowUnselect(UnselectEvent event) {
		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void viewBtnAction() {

		if (chargeFinalization.getChargeRefCode() != null) {
			sessionBackingBean.setInvesNo(chargeFinalization.getChargeRefCode());
		} else {
			sessionBackingBean.setInvesNo(chargeReferenceNo);
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FlyingSquadChargeSheetBackingBeann flyingSquadChargeSheetBackingBeann = facesContext.getApplication()
				.evaluateExpressionGet(facesContext, "#{flyingSquadChargeSheetBackingBeann}",
						FlyingSquadChargeSheetBackingBeann.class);
		flyingSquadChargeSheetBackingBeann.init();
		RequestContext.getCurrentInstance().update("frmview");
		RequestContext.getCurrentInstance().execute("PF('showPageDialog').show()");
	}

	public void loadDriverConductorData(String dc) {

		if (dc.equalsIgnoreCase(DRIVER)) {
			driverData = manageInvestigationService.getDriverConductorData(driverData);

		} else if (dc.equalsIgnoreCase(CONDUCTOR)) {
			conductorData = manageInvestigationService.getDriverConductorData(conductorData);

		}

		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void clearDriverData() {
		driverData = new DriverConductorRegistrationDTO();
	}

	public void clearConductorData() {
		conductorData = new DriverConductorRegistrationDTO();
	}

	public void chargeFinalizationBtnAction() {

		chargeReferenceNo = selectedInvestigation.getChargeRefCode();
		applicationStatus = selectedInvestigation.getStatus();
		applicationCurrentStatus = selectedInvestigation.getCurrentStatus();
		selectedInvestigation.setLoginUser(sessionBackingBean.getLoginUser());

		if (applicationStatus.equalsIgnoreCase("O") && applicationCurrentStatus.equalsIgnoreCase("MS")) {
			chargesList = manageInvestigationService.getInvestigationChargesByRef(selectedInvestigation, chargeReferenceNo);
		} else {
			chargesList = manageInvestigationService.getChargeSheetByChargeRef(selectedInvestigation, chargeReferenceNo);

			chargeFinalization.setChargeRefCode(chargeReferenceNo);
			chargeFinalization = manageInvestigationService.getChargeSheetMaster(chargeFinalization);

		}

		DriverConductorRegistrationDTO savedDriver = manageInvestigationService
				.getConfirmedDriver(selectedInvestigation,selectedInvestigation.getChargeRefCode());
		if (savedDriver != null) {
			driverData = savedDriver;
			if (savedDriver.getDriverConductorId() != null && !savedDriver.getDriverConductorId().equals("")) {
				newDriver = false;
				chargeFinalization.setDriverId(driverData.getDriverConductorId());

				if (driverData.getDriverConductorId().substring(0, 1).equalsIgnoreCase("D")) {
					enableDriverCheck = true;
				}
			} else {
				newDriver = true;
				chargeFinalization.setDriverId(driverData.getNic());
				enableDriverCheck = false;
			}

			chargeFinalization.setDriverName(driverData.getFullNameEng());
		}

		DriverConductorRegistrationDTO savedConductor = manageInvestigationService
				.getConfirmedConductor(selectedInvestigation,selectedInvestigation.getChargeRefCode());
		if (savedConductor != null) {
			conductorData = savedConductor;
			if (savedConductor.getDriverConductorId() != null && !savedConductor.getDriverConductorId().equals("")) {
				newConductor = false;
				chargeFinalization.setConductorId(conductorData.getDriverConductorId());

				if (conductorData.getDriverConductorId().substring(0, 1).equalsIgnoreCase("C")) {
					enableConductorCheck = true;
				}

			} else {
				newConductor = true;
				chargeFinalization.setConductorId(conductorData.getNic());
				enableConductorCheck = false;
			}
			chargeFinalization.setConductorName(conductorData.getFullNameEng());
		}

		calculateTotalAmount(false);
		calculatePointsDriver();
		calculatePointsConductor();
		
		manageInvestigationService.beanLinkMethod(selectedInvestigation, "Charge Finalize");

		RequestContext.getCurrentInstance().update("frmChargeFinalize");
		RequestContext.getCurrentInstance().execute("PF('dlgChargeFinalization').show()");
	}
    public void onAttemptChange(ManageInvestigationDTO dto,String attempDes) {
    
    		attemptChange = true;
    	
    		if(attempDes.equals("First Time")) {
    			dto.setAttemptCode("FT") ;
			}
			if(attempDes.equals("Second Time")) {
				dto.setAttemptCode("ST") ;
			}
			if(attempDes.equals("Third Time")) {
				dto.setAttemptCode("TT") ;
			}
			if(attempDes.equals("Fourth Time")) {
				dto.setAttemptCode("FOT") ;
			}
		
    	if(dto.getAttemptCode() != null) {
    		dto.setAmount(manageInvestigationService.getAmountPerAttempt(dto.getChargeCode(),dto.getAttemptCode()));
    	}
    	
    	
    	 calculateTotalAmount(attemptChange);
    }
	public void calculateTotalAmount(boolean attempChange) {

		totalChargeAmount = new BigDecimal(0);
		
		for (ManageInvestigationDTO dto : chargesList) {
			
			
			if(dto.getAttemptDesc().trim().equalsIgnoreCase("First Time") ) {
				dto.setAttemptCode("FT");
			}
			if(dto.getAttemptDesc().trim().equalsIgnoreCase("Second Time") ) {
				dto.setAttemptCode("ST");
			}
			if(dto.getAttemptDesc().trim().equalsIgnoreCase("Third Time")) {
				dto.setAttemptCode("TT");
			}
			if(dto.getAttemptDesc().trim().equalsIgnoreCase("Fourth Time")) {
				dto.setAttemptCode("FOT");
			}
		
    	if(attempChange && dto.getAttemptCode() != null) {
    		dto.setAmount(manageInvestigationService.getAmountPerAttempt(dto.getChargeCode(),dto.getAttemptCode()));
    	}
			if (dto.getAmount() != null)
				totalChargeAmount = totalChargeAmount.add(dto.getAmount());
		}

		chargeFinalization.setFinalAmount(totalChargeAmount);
	}
	
	/**CR request by NTC ---> add late payment fee***/
	
	public void lateFeeAddButton() {
		selectedInvestigation.setLoginUser(sessionBackingBean.getLoginUser());
		System.out.print("lateFee" + latePaymentFee);
		BigDecimal tempTotaAmount= new BigDecimal(0);
		tempTotaAmount = chargeFinalization.getFinalAmount();
		if (latePaymentFee != null && latePaymentFee.compareTo(BigDecimal.ZERO)>0) {
			tempTotaAmount = tempTotaAmount.add(latePaymentFee);
			totalChargeAmount =tempTotaAmount;
			chargeFinalization.setFinalAmount(totalChargeAmount);
			chargeFinalization.setLatePaymentFee(latePaymentFee);
			try {
				manageInvestigationService.beanLinkMethod(selectedInvestigation,"Late Fee Added");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		
	}
	
	public void lateFeeClearButton() {
		System.out.print("lateFee reduce" + latePaymentFee);
		BigDecimal tempTotaAmount= new BigDecimal(0);
		tempTotaAmount = chargeFinalization.getFinalAmount();
		if (latePaymentFee != null && latePaymentFee.compareTo(BigDecimal.ZERO)>0) {
			tempTotaAmount = tempTotaAmount.subtract(latePaymentFee);
			totalChargeAmount =tempTotaAmount;
			chargeFinalization.setFinalAmount(totalChargeAmount);
			chargeFinalization.setLatePaymentFee(new  BigDecimal(0));
			try {
				manageInvestigationService.beanLinkMethod(selectedInvestigation,"Late Fee Cancel");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		latePaymentFee = new BigDecimal(0);
		
	}
	
	/***end****/

	public void calculatePointsDriver() {

		totalDriverPoints = new BigDecimal(0);

		for (ManageInvestigationDTO dto : chargesList) {
			if (dto.getDriverPoints() != null)
				totalDriverPoints = totalDriverPoints.add(dto.getDriverPoints());
		}

		selectedInvestigation.setTotalDriverPoints(totalDriverPoints);
	}

	public void calculatePointsConductor() {

		totalConductorPoints = new BigDecimal(0);

		for (ManageInvestigationDTO dto : chargesList) {
			if (dto.getConductorPoints() != null)
				totalConductorPoints = totalConductorPoints.add(dto.getConductorPoints());
		}

		selectedInvestigation.setTotalConductorPoints(totalConductorPoints);
	}

	public void applyDriver(ManageInvestigationDTO dto) {
		dto.setDriverPoints(null);
		calculatePointsDriver();
	}

	public void applyConductor(ManageInvestigationDTO dto) {
		dto.setConductorPoints(null);
		calculatePointsConductor();
	}

	public void createVoucherBtnAction() {

		selectedInvestigation.setLoginUser(sessionBackingBean.getLoginUser());

		String voucherNo = manageInvestigationService.createVoucher(selectedInvestigation);

		if (voucherNo != null) {

			updateCurrentStatus = manageInvestigationService.updateCurrentStatus(selectedInvestigation, 
					selectedInvestigation.getChargeRefCode(), "CV", sessionBackingBean.getLoginUser(),"Voucher Create Status Update");

			if (updateCurrentStatus) {
				manageInvestigationService.beanLinkMethod(selectedInvestigation, "Voucher Create");
				showMsg("SUCCESS", "Voucher Creation - Success\r\n Voucher No.: " + voucherNo);
			}

		} else {
			showMsg("ERROR", "Voucher Creation - Error");
		}

		manageInvestigationDTO = new ManageInvestigationDTO();
		manageInvestigationDTO.setChargeRefCode(selectedInvestigation.getChargeRefCode());
		searchButtonAction();

		RequestContext.getCurrentInstance().update("manageInvsFrm");
	}

	public void cancelVoucherBtnAction() {
		selectedInvestigation.setLoginUser(sessionBackingBean.getLoginUser());
		updateCurrentStatus = manageInvestigationService.updateCurrentStatus(selectedInvestigation, chargeFinalization.getChargeRefCode(),
				"CC", sessionBackingBean.getLoginUser(),"Voucher Cancel Status Update");
		if (updateCurrentStatus) {
			/** update voucher status as C by tharushi.e **/

			manageInvestigationService.updateVoucherStatus(selectedInvestigation, chargeFinalization.getChargeRefCode(), "C",
					sessionBackingBean.getLoginUser());
			manageInvestigationService.beanLinkMethod(selectedInvestigation, "Voucher Cancel");
			showMsg("SUCCESS", "Voucher Cancel - Success");

		}

		manageInvestigationDTO = new ManageInvestigationDTO();
		manageInvestigationDTO.setChargeRefCode(selectedInvestigation.getChargeRefCode());

		searchButtonAction();
		enablePrintVoucher = false;
	}

	public StreamedContent printVoucherBtnAction(ActionEvent ae) throws JRException {
		selectedInvestigation.setLoginUser(sessionBackingBean.getLoginUser());
		updateCurrentStatus = manageInvestigationService.updateCurrentStatus(selectedInvestigation, chargeFinalization.getChargeRefCode(),
				"CP", sessionBackingBean.getLoginUser(),"Voucher Printed");
		if (updateCurrentStatus) {
			manageInvestigationService.beanLinkMethod(selectedInvestigation, "Voucher Print");
			showMsg("SUCCESS", "Voucher Print - Success");
		}
		/** for Voucher Print **/
		files = null;
		String sourceFileName = null; 
		String value = null;
		value = manageInvestigationDTO.getChargeRefCode();
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//debitVoucherForInvestigation.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_charge_reff", value);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf",
					"PaymentVoucher_Investigation - " + value + ".pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;

		/** Voucher print end **/

	}

	public void cancelBtnAction() {

	}

	public void chargeFinalizationSaveBtnAction() {
		selectedInvestigation.setLoginUser(sessionBackingBean.getLoginUser());
		System.out.print("chargesList"+chargesList);
		selectedInvestigation.setLoginUser(sessionBackingBean.getLoginUser());

		boolean saveInvestigation = manageInvestigationService.saveInvestigationMaster(selectedInvestigation,latePaymentFee);

		if (saveInvestigation) {
			chargeFinalization.setLoginUser(sessionBackingBean.getLoginUser());
			boolean saveChargeSheet = manageInvestigationService.saveChargeSheet(chargeFinalization, chargesList);
			if (saveChargeSheet)

				updateCurrentStatus = manageInvestigationService.updateCurrentStatus(selectedInvestigation, 
						chargeFinalization.getChargeRefCode(), "CS", chargeFinalization.getLoginUser(),"Charge Finalize Status Update");

			totDriverPoints = chargeFinalization.getTotalDriverPoints();
			totConductorPoints = chargeFinalization.getTotalConductorPoints();

			if (updateCurrentStatus == true) {
				manageInvestigationService.beanLinkMethod(selectedInvestigation, "Charge Finalize Complete");
				showMsg("SUCCESS", "Transaction Completed");
				currStatus = "CS";
			} else {
				showMsg("ERROR", "Unable to Update Charge Sheet");
			}

			RequestContext.getCurrentInstance().execute("PF('dlgChargeFinalization').hide()");

			manageInvestigationDTO = new ManageInvestigationDTO();
			manageInvestigationDTO.setChargeRefCode(selectedInvestigation.getChargeRefCode());

			searchButtonAction();

			RequestContext.getCurrentInstance().update("manageInvsFrm");
		}

	}

	public void execute() {
		manageInvestigationDTO = new ManageInvestigationDTO();
		manageInvestigationDTO.setChargeRefCode(selectedInvestigation.getChargeRefCode());
		searchButtonAction();
		RequestContext.getCurrentInstance().update("manageInvsFrm:panelDriver:panelConductor");
	}

	public void closeInvestigationBtnAction() {
		selectedInvestigation.setLoginUser(sessionBackingBean.getLoginUser());
		
		Boolean appStatus = manageInvestigationService.updateAppStatus(selectedInvestigation,chargeFinalization.getChargeRefCode(), "C",
				chargeFinalization.getLoginUser());

		if (appStatus == true) {
			updateCurrentStatus = manageInvestigationService.updateCurrentStatus(selectedInvestigation, chargeFinalization.getChargeRefCode(),
					"CI", chargeFinalization.getLoginUser(),"Investigation Close Status Update");

			if (updateCurrentStatus) {
				manageInvestigationService.beanLinkMethod(selectedInvestigation, "Investigation Closed");
				showMsg("SUCCESS", "Investigation Closed");
			}

		}

		manageInvestigationDTO = new ManageInvestigationDTO();
		manageInvestigationDTO.setChargeRefCode(selectedInvestigation.getChargeRefCode());

		searchButtonAction();

		RequestContext.getCurrentInstance().update("manageInvsFrm:panelDriver:panelConductor");

	}

	public void loadActionDialog(String dco) {
		viewType = dco;
		actionList = manageInvestigationService.getActions();
		deptList = accessPermissionService.getDeptToDropdown();

		selectedInvestigation.setDepartmentType(null);
		selectedInvestigation.setActionCode(null);
		selectedInvestigation.setActionDesc(null);

		savedActions = manageInvestigationService.getChargeSheetMaster(selectedInvestigation);

		if (viewType.equalsIgnoreCase("O")) {
			selectedInvestigation.setDepartmentType(savedActions.getPermitDepartment());
			selectedInvestigation.setActionCode(savedActions.getPermitActionType());
			selectedInvestigation.setActionDesc(savedActions.getPermitActionDesc());
		} else if (viewType.equalsIgnoreCase("D")) {
			selectedInvestigation.setDepartmentType(savedActions.getDriverDepartment());
			selectedInvestigation.setActionCode(savedActions.getDriverActionType());
			selectedInvestigation.setActionDesc(savedActions.getDriverActionDesc());
		} else if (viewType.equalsIgnoreCase("D")) {
			selectedInvestigation.setDepartmentType(savedActions.getConductorDepartment());
			selectedInvestigation.setActionCode(savedActions.getConductorActionType());
			selectedInvestigation.setActionDesc(savedActions.getConductorActionDesc());
		}

		RequestContext.getCurrentInstance().execute("PF('dlgAction').show()");
	}

	public void resetAction() {
		savedActions = manageInvestigationService.getChargeSheetMaster(selectedInvestigation);

		if (viewType.equalsIgnoreCase("O")) {
			selectedInvestigation.setDepartmentType(savedActions.getPermitDepartment());
			selectedInvestigation.setActionCode(savedActions.getPermitActionType());
			selectedInvestigation.setActionDesc(savedActions.getPermitActionDesc());
		} else if (viewType.equalsIgnoreCase("D")) {
			selectedInvestigation.setDepartmentType(savedActions.getDriverDepartment());
			selectedInvestigation.setActionCode(savedActions.getDriverActionType());
			selectedInvestigation.setActionDesc(savedActions.getDriverActionDesc());
		} else if (viewType.equalsIgnoreCase("D")) {
			selectedInvestigation.setDepartmentType(savedActions.getConductorDepartment());
			selectedInvestigation.setActionCode(savedActions.getConductorActionType());
			selectedInvestigation.setActionDesc(savedActions.getConductorActionDesc());
		}
	}

	public void actionBtnAction() {

		boolean updateAction = manageInvestigationService.updateAction(viewType,
				selectedInvestigation.getChargeRefCode(), selectedInvestigation.getDepartmentType(),
				selectedInvestigation.getActionCode(), selectedInvestigation.getActionDesc());

		if (updateAction) {
			showMsg("SUCCESS", "Action Updated Successfully");
		}

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
	

	
	public void loadComplaintHistory(String dco) {
		viewType = dco;
		complaintHistoryList = new ArrayList<ComplaintRequestDTO>();
		historySelected = false;
		if (viewType.equalsIgnoreCase(OWNER))
			complaintHistoryList = manageInvestigationService
					.getComplaintHistoryByOwner(selectedInvestigation.getVehicleNo());

		if (viewType.equalsIgnoreCase(DRIVER))
			complaintHistoryList = manageInvestigationService
					.getComplaintHistoryByDriver(driverData.getDriverConductorId(), driverData.getNic());

		if (viewType.equalsIgnoreCase(CONDUCTOR))
			complaintHistoryList = manageInvestigationService
					.getComplaintHistoryByConductor(conductorData.getDriverConductorId(), conductorData.getNic());

		// get total offence charge amount
		for (ComplaintRequestDTO com : nullSafe(complaintHistoryList)) {
			BigDecimal totAmount = new BigDecimal(0);
			for (CommittedOffencesDTO off : nullSafe(com.getCommittedOffences()))
				totAmount = totAmount.add(manageInvestigationService.getOffenceCharge(off.getCode()));

			com.setTotalCharge(totAmount);
		}

		RequestContext.getCurrentInstance().execute("PF('dlgComplaintHistory').show()");
	}

	public void selectComplaintHistory() {
		historySelected = true;
//		violationConditionList = manageInvestigationService.getCommittedOffencesById(selectedHistory.getComplainSeq(),
//				driverData.getDriverConductorId(), conductorData.getDriverConductorId());
		//System.out.println(selectedHistory.getComplaintNo());
		violationConditionList = manageInvestigationService.getViolationHistory(selectedHistory.getComplaintNo());
	}
	
	private static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	/*
	 * getters & setters
	 */

	public ManageInvestigationDTO getManageInvestigationDTO() {
		return manageInvestigationDTO;
	}

	public void setManageInvestigationDTO(ManageInvestigationDTO manageInvestigationDTO) {
		this.manageInvestigationDTO = manageInvestigationDTO;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public ManageInvestigationService getInvestigationManagementService() {
		return manageInvestigationService;
	}

	public void setInvestigationManagementService(ManageInvestigationService investigationManagementService) {
		this.manageInvestigationService = investigationManagementService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<ManageInvestigationDTO> getChargeRefList() {
		return chargeRefList;
	}

	public void setChargeRefList(List<ManageInvestigationDTO> chargeRefList) {
		this.chargeRefList = chargeRefList;
	}

	public List<ManageInvestigationDTO> getInvestigationsList() {
		return investigationsList;
	}

	public void setInvestigationsList(List<ManageInvestigationDTO> investigationsList) {
		this.investigationsList = investigationsList;
	}

	public ManageInvestigationService getManageInvestigationService() {
		return manageInvestigationService;
	}

	public void setManageInvestigationService(ManageInvestigationService manageInvestigationService) {
		this.manageInvestigationService = manageInvestigationService;
	}

	public ManageInvestigationDTO getSelectedInvestigation() {
		return selectedInvestigation;
	}

	public void setSelectedInvestigation(ManageInvestigationDTO selectedInvestigation) {
		this.selectedInvestigation = selectedInvestigation;
	}

	public ManageInvestigationDTO getChargeFinalization() {
		return chargeFinalization;
	}

	public void setChargeFinalization(ManageInvestigationDTO chargeFinalization) {
		this.chargeFinalization = chargeFinalization;
	}

	public boolean isItemSelected() {
		return itemSelected;
	}

	public void setItemSelected(boolean itemSelected) {
		this.itemSelected = itemSelected;
	}

	public List<DropDownDTO> getActionList() {
		return actionList;
	}

	public void setActionList(List<DropDownDTO> actionList) {
		this.actionList = actionList;
	}

	public List<DropDownDTO> getAttemptList() {
		return attemptList;
	}

	public void setAttemptList(List<DropDownDTO> attemptList) {
		this.attemptList = attemptList;
	}

	public List<ManageInvestigationDTO> getChargesList() {
		return chargesList;
	}

	public void setChargesList(List<ManageInvestigationDTO> chargesList) {
		this.chargesList = chargesList;
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

	public DriverConductorRegistrationDTO getDriverData() {
		return driverData;
	}

	public void setDriverData(DriverConductorRegistrationDTO driverData) {
		this.driverData = driverData;
	}

	public DriverConductorRegistrationDTO getConductorData() {
		return conductorData;
	}

	public void setConductorData(DriverConductorRegistrationDTO conductorData) {
		this.conductorData = conductorData;
	}

	public BigDecimal getTotalDriverPoints() {
		return totalDriverPoints;
	}

	public void setTotalDriverPoints(BigDecimal totalDriverPoints) {
		this.totalDriverPoints = totalDriverPoints;
	}

	public BigDecimal getTotalConductorPoints() {
		return totalConductorPoints;
	}

	public void setTotalConductorPoints(BigDecimal totalConductorPoints) {
		this.totalConductorPoints = totalConductorPoints;
	}

	public BigDecimal getTotalChargeAmount() {
		return totalChargeAmount;
	}

	public void setTotalChargeAmount(BigDecimal totalChargeAmount) {
		this.totalChargeAmount = totalChargeAmount;
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

	public boolean isEnableChargeFinalization() {
		return enableChargeFinalization;
	}

	public void setEnableChargeFinalization(boolean enableChargeFinalization) {
		this.enableChargeFinalization = enableChargeFinalization;
	}

	public boolean isEnableCreateVoucher() {
		return enableCreateVoucher;
	}

	public void setEnableCreateVoucher(boolean enableCreateVoucher) {
		this.enableCreateVoucher = enableCreateVoucher;
	}

	public boolean isEnableCancelVoucher() {
		return enableCancelVoucher;
	}

	public void setEnableCancelVoucher(boolean enableCancelVoucher) {
		this.enableCancelVoucher = enableCancelVoucher;
	}

	public boolean isEnablePrintVoucher() {
		return enablePrintVoucher;
	}

	public void setEnablePrintVoucher(boolean enablePrintVoucher) {
		this.enablePrintVoucher = enablePrintVoucher;
	}

	public String getCurrStatus() {
		return currStatus;
	}

	public void setCurrStatus(String currStatus) {
		this.currStatus = currStatus;
	}

	public boolean isUpdateCurrentStatus() {
		return updateCurrentStatus;
	}

	public void setUpdateCurrentStatus(boolean updateCurrentStatus) {
		this.updateCurrentStatus = updateCurrentStatus;
	}

	public List<AccessPermissionDTO> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<AccessPermissionDTO> deptList) {
		this.deptList = deptList;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public AccessPermissionService getAccessPermissionService() {
		return accessPermissionService;
	}

	public void setAccessPermissionService(AccessPermissionService accessPermissionService) {
		this.accessPermissionService = accessPermissionService;
	}

	public boolean isNewDriver() {
		return newDriver;
	}

	public void setNewDriver(boolean newDriver) {
		this.newDriver = newDriver;
	}

	public boolean isNewConductor() {
		return newConductor;
	}

	public void setNewConductor(boolean newConductor) {
		this.newConductor = newConductor;
	}

	public BigDecimal getTotDriverPoints() {
		return totDriverPoints;
	}

	public void setTotDriverPoints(BigDecimal totDriverPoints) {
		this.totDriverPoints = totDriverPoints;
	}

	public BigDecimal getTotConductorPoints() {
		return totConductorPoints;
	}

	public void setTotConductorPoints(BigDecimal totConductorPoints) {
		this.totConductorPoints = totConductorPoints;
	}

	public boolean isSaveDriverConductor() {
		return saveDriverConductor;
	}

	public void setSaveDriverConductor(boolean saveDriverConductor) {
		this.saveDriverConductor = saveDriverConductor;
	}

	public boolean isEnableSaveDriverConductor() {
		return enableSaveDriverConductor;
	}

	public void setEnableSaveDriverConductor(boolean enableSaveDriverConductor) {
		this.enableSaveDriverConductor = enableSaveDriverConductor;
	}

	public ManageInvestigationDTO getSavedActions() {
		return savedActions;
	}

	public void setSavedActions(ManageInvestigationDTO savedActions) {
		this.savedActions = savedActions;
	}

	public boolean isEnableActionPanel() {
		return enableActionPanel;
	}

	public void setEnableActionPanel(boolean enableActionPanel) {
		this.enableActionPanel = enableActionPanel;
	}

	public boolean isEnableUpdateDriverConductor() {
		return enableUpdateDriverConductor;
	}

	public void setEnableUpdateDriverConductor(boolean enableUpdateDriverConductor) {
		this.enableUpdateDriverConductor = enableUpdateDriverConductor;
	}

	public String getChargeReferenceNo() {
		return chargeReferenceNo;
	}

	public void setChargeReferenceNo(String chargeReferenceNo) {
		this.chargeReferenceNo = chargeReferenceNo;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getApplicationCurrentStatus() {
		return applicationCurrentStatus;
	}

	public void setApplicationCurrentStatus(String applicationCurrentStatus) {
		this.applicationCurrentStatus = applicationCurrentStatus;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isEnableDriverCheck() {
		return enableDriverCheck;
	}

	public void setEnableDriverCheck(boolean enableDriverCheck) {
		this.enableDriverCheck = enableDriverCheck;
	}

	public boolean isEnableConductorCheck() {
		return enableConductorCheck;
	}

	public void setEnableConductorCheck(boolean enableConductorCheck) {
		this.enableConductorCheck = enableConductorCheck;
	}

	public boolean isEnableInitiateDriverConductor() {
		return enableInitiateDriverConductor;
	}

	public void setEnableInitiateDriverConductor(boolean enableInitiateDriverConductor) {
		this.enableInitiateDriverConductor = enableInitiateDriverConductor;
	}

	public FlyingSquadChargeSheetBackingBean getFlyingSquadChargeSheetBackingBean() {
		return flyingSquadChargeSheetBackingBean;
	}

	public void setFlyingSquadChargeSheetBackingBean(
			FlyingSquadChargeSheetBackingBean flyingSquadChargeSheetBackingBean) {
		this.flyingSquadChargeSheetBackingBean = flyingSquadChargeSheetBackingBean;
	}

	public FlyingManageInvestigationLogDTO getFlyingManageInvestigationLogDTO() {
		return flyingManageInvestigationLogDTO;
	}

	public void setFlyingManageInvestigationLogDTO(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO) {
		this.flyingManageInvestigationLogDTO = flyingManageInvestigationLogDTO;
	}

	public String getAttemptDescN() {
		return attemptDescN;
	}

	public void setAttemptDescN(String attemptDescN) {
		this.attemptDescN = attemptDescN;
	}

	public ManageInvestigationDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(ManageInvestigationDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public BigDecimal getLatePaymentFee() {
		return latePaymentFee;
	}

	public void setLatePaymentFee(BigDecimal latePaymentFee) {
		this.latePaymentFee = latePaymentFee;
	}

	public static String getOWNER() {
		return OWNER;
	}

	public static void setOWNER(String oWNER) {
		OWNER = oWNER;
	}

	public static String getDRIVER() {
		return DRIVER;
	}

	public static void setDRIVER(String dRIVER) {
		DRIVER = dRIVER;
	}

	public static String getCONDUCTOR() {
		return CONDUCTOR;
	}

	public static void setCONDUCTOR(String cONDUCTOR) {
		CONDUCTOR = cONDUCTOR;
	}

	public boolean isAttemptChange() {
		return attemptChange;
	}

	public void setAttemptChange(boolean attemptChange) {
		this.attemptChange = attemptChange;
	}

	public List<ComplaintRequestDTO> getComplaintHistoryList() {
		return complaintHistoryList;
	}

	public void setComplaintHistoryList(List<ComplaintRequestDTO> complaintHistoryList) {
		this.complaintHistoryList = complaintHistoryList;
	}

	public boolean isHistorySelected() {
		return historySelected;
	}

	public void setHistorySelected(boolean historySelected) {
		this.historySelected = historySelected;
	}

	public List<CommittedOffencesDTO> getViolationConditionList() {
		return violationConditionList;
	}

	public void setViolationConditionList(List<CommittedOffencesDTO> violationConditionList) {
		this.violationConditionList = violationConditionList;
	}

	public ComplaintRequestDTO getSelectedHistory() {
		return selectedHistory;
	}

	public void setSelectedHistory(ComplaintRequestDTO selectedHistory) {
		this.selectedHistory = selectedHistory;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisableBtn() {
		return disableBtn;
	}

	public void setDisableBtn(boolean disableBtn) {
		this.disableBtn = disableBtn;
	}

	public boolean isDisableBtnCon() {
		return disableBtnCon;
	}

	public void setDisableBtnCon(boolean disableBtnCon) {
		this.disableBtnCon = disableBtnCon;
	}
	
	
	
	
}
