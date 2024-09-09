package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitPaymentDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
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

@ManagedBean(name = "editPrevPermitDetailsBackingBean")
@ViewScoped
public class EditPrevPermitDetailsBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Services
	private PermitRenewalsService permitRenewalsService;
	private AdminService adminService;
	private CommonService commonService;
	private EmployeeProfileService employeeProfileService;

	// DTO

	private PermitDTO permitDTO = new PermitDTO();;
	private BusOwnerDTO busOwnerDTO = new BusOwnerDTO();
	private BusOwnerDTO previousBusOwnerDTO = new BusOwnerDTO();
	private OminiBusDTO ominiBusDTO = new OminiBusDTO();
	private PermitPaymentDTO permitPaymentDTO = new PermitPaymentDTO();
	private RouteDTO routeDTO = new RouteDTO();
	private PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

	// Lists
	private List<CommonDTO> routefordropdownList;
	private List<CommonDTO> serviceTypedropdownList;
	private List<CommonDTO> titleList;
	private List<CommonDTO> genderList;
	private List<CommonDTO> martialList;
	private List<CommonDTO> provincelList;
	private List<CommonDTO> districtList;
	private List<CommonDTO> divSecList;
	private List<CommonDTO> makeList;
	private List<CommonDTO> modelList;
	private List<CommonDTO> permitNoListN;
	public List<String> permitNoList = new ArrayList<String>(0);
	public List<String> appNoList = new ArrayList<String>(0);
	public List<String> appNoListN = new ArrayList<String>(0);
	public List<String> busRegNoList = new ArrayList<String>(0);

	// SelectedValues
	private String strSelectedRoute;
	private String strSelectedVal;
	private String strSelectedServiceType;
	private String strSelectedTitle;
	private String strSelectedGender;
	private String strSelectedMartial;
	private String strSelectedProvince;
	private String strSelectedDistrict;
	private String strSelectedDivSec;
	private String strSelectedLanguage;
	private String strSelectedMake;
	private String strSelectedModel;
	private BigDecimal calTot;
	private String strSelectedPermitNo;
	private String strSelectedApplicationNo;
	private String strSelectedBusRegNo;

	private String errorMsg;
	private Boolean boolEmpAddDet;
	private Boolean booldisable;
	private Boolean bisablebutton;
	private Boolean disableMode;
	private Boolean disupdateBtn = false;
	private String successMsg, alertMSG;

	private Boolean routeFlag, readOnlyBusFare;

	private Boolean seachDis;
	private boolean disableCreateModels, disabledModel;
	private boolean disabledGender = false;
	private boolean disabledDOB = false;

	private Boolean checkValiationsForInputFields = false;

	private boolean showThirdForm = false;
	private boolean showbacklogvalue12 = false;
	private boolean showbacklogvalue = false;
	private boolean showbacklogvalueLoad = false;
	private boolean requestNewPeriodReadOnly = false;
	private boolean disabledReqPeriodInput = true;
	private boolean showNewPermitDateInput = true;
	private boolean checkNewExpiryDateBoolean = false;
	private boolean disablePrint;
	private StreamedContent files;

	@PostConstruct
	public void init() {
		permitRenewalsService = (PermitRenewalsService) SpringApplicationContex.getBean("permitRenewalsService");
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		permitDTO = new PermitDTO();
		busOwnerDTO = new BusOwnerDTO();
		ominiBusDTO = new OminiBusDTO();
		permitPaymentDTO = new PermitPaymentDTO();
		permitNoList = adminService.getAllActivePermits();
		appNoList = adminService.getAllApplicationsWithoutCheckingStatus();
		appNoListN = adminService.getAllActiveApp();
		busRegNoList = adminService.getAllActiveBusRegNos();
		permitNoListN = adminService.getAllActivePermit();
		routeFlag = false;
		booldisable = false;
		LoadValues();
		alertMSG = null;
		disablePrint = true;
	}

	private void LoadValues() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		routefordropdownList = adminService.getRoutesToDropdown();
		serviceTypedropdownList = adminService.getServiceTypeToDropdown();
		titleList = employeeProfileService.GetTitleToDropdown();
		genderList = employeeProfileService.GetGenderToDropdown();
		martialList = employeeProfileService.GetMartialToDropdown();

		provincelList = adminService.getProvinceToDropdown();
		districtList = adminService.getDistrictToDropdown();
		divSecList = adminService.getDivSecToDropdown();

		makeList = adminService.getMakesToDropdown();
		modelList = adminService.getModelsToDropdown();
		disabledModel = true;
		disupdateBtn = false;
		seachDis = false;
	}

	public EditPrevPermitDetailsBackingBean() {

	}

	public void generateApplicationNoAndBusNoN() {
		String AppNo = adminService.getApplicationNoFromPermitNo(strSelectedPermitNo);
		String busNo = adminService.getBusNoFromPermitNo(strSelectedPermitNo);
		setStrSelectedApplicationNo(AppNo);
		setStrSelectedBusRegNo(busNo);

	}

	public void generatePermitNoAndBusNo() {
		String permitNo = adminService.getPermitNoFromAppNo(strSelectedApplicationNo);
		String busNo = adminService.getBusNoFromAppNo(strSelectedApplicationNo);
		setStrSelectedPermitNo(permitNo);
		setStrSelectedBusRegNo(busNo);
	}

	public void generateApplicationNoAndPermitNo() {

	}

	public void searchDetails() {
		
		
		if((strSelectedApplicationNo != null && !strSelectedApplicationNo.trim().equalsIgnoreCase("")) && (strSelectedBusRegNo != null && !strSelectedBusRegNo.trim().equalsIgnoreCase(""))
				&& (strSelectedPermitNo != null && !strSelectedPermitNo.trim().equalsIgnoreCase(""))) {
			searchPermitDetails();
			disupdateBtn = false;
		}else {
			errorMsg = "Please select valid Application No. or Permit No.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");	
		}
		
	}

	private void searchPermitDetails() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		permitDTO = adminService.searchByPermitNo(strSelectedPermitNo, strSelectedApplicationNo, strSelectedBusRegNo);
		seachDis = true;
		if (permitDTO.getBusRegNo() != null && !permitDTO.getBusRegNo().isEmpty()
				&& !permitDTO.getBusRegNo().equalsIgnoreCase("")) {
			booldisable = true;
			strSelectedRoute = permitDTO.getRouteNo();
			strSelectedServiceType = permitDTO.getServiceType();
			strSelectedVal = permitDTO.getIsTenderPermit();

			if (permitDTO.getRouteFlag() != null && !permitDTO.getRouteFlag().trim().equals("")
					&& permitDTO.getRouteFlag().equalsIgnoreCase("Y")) {
				routeFlag = true;
				routeFlagListener();

			} else if (permitDTO.getRouteFlag() != null && !permitDTO.getRouteFlag().trim().equals("")
					&& permitDTO.getRouteFlag().equalsIgnoreCase("N")) {

			}

			getOwnerDetails();
			getOminiBusDetails();
			getPaymentDetails();
			getRenewalDetails();
		} else {
			seachDis = false;
			RequestContext.getCurrentInstance().update("frmnoDataMsge");
			RequestContext.getCurrentInstance().execute("PF('noDataMsge').show()");
		}
	}

	private void getRenewalDetails() {
		if (!strSelectedApplicationNo.equals("")) {
			permitRenewalsDTO = adminService.renewalsByApplicationNo(strSelectedApplicationNo);
			String format = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			if (permitRenewalsDTO.getNewPermitExpirtDate() != null) {
				Date newPermitDateObj;
				try {
					newPermitDateObj = sdf.parse(permitRenewalsDTO.getNewPermitExpirtDate());
					permitRenewalsDTO.setNewPermitExpirtDateObj(newPermitDateObj);
				} catch (ParseException e) {

					e.printStackTrace();
				}

			}

			if (permitRenewalsDTO.getValidToDate() != null) {
				Date validDateObj;
				try {
					validDateObj = sdf.parse(permitRenewalsDTO.getValidToDate());
					permitRenewalsDTO.setPermitExpiredValidToDateObj(validDateObj);

				} catch (ParseException e) {

					e.printStackTrace();
				}

			}
			if (permitRenewalsDTO.getFromToDate() != null) {
				Date fromDateObj;
				try {
					fromDateObj = sdf.parse(permitRenewalsDTO.getFromToDate());
					permitRenewalsDTO.setPermitExpiredFromDateObj(fromDateObj);

				} catch (ParseException e) {

					e.printStackTrace();
				}

			} else {
				Date permitExpiredDateForFromDate;
				try {
					permitExpiredDateForFromDate = sdf.parse(permitRenewalsDTO.getPermitExpiryDate());
					permitRenewalsDTO.setPermitExpiredFromDateObj(permitExpiredDateForFromDate);
				} catch (ParseException e) {

					e.printStackTrace();
				}

			}
			setShowbacklogvalue(permitRenewalsDTO.isCheckBacklogValue());
			setShowbacklogvalue12(permitRenewalsDTO.isCheckBacklogValue());
			if (permitRenewalsDTO.getPermitExpiredValidToDateObj() != null) {
				setDisabledReqPeriodInput(false);
			}
		}
	}

	public void onFromDateChange(SelectEvent event) {
		if (permitRenewalsDTO.getPermitExpiredFromDateObj() != null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
			String fromDateNewValue = frm.format(event.getObject());

			permitRenewalsDTO.setFromToDate(fromDateNewValue);
		} else {
			setErrorMsg("From Date should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void onValidDateChange(SelectEvent event) {
		if (permitRenewalsDTO.getPermitExpiredValidToDateObj() != null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

			String validDateNewValue = frm.format(event.getObject());
			permitRenewalsDTO.setValidToDate(validDateNewValue);
			setDisabledReqPeriodInput(false);
		} else {
			setErrorMsg("Valid to Date should be selected.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void onNewPermitDateChangeNew() {
		if (permitRenewalsDTO.getPermitExpiryDate() != null && permitRenewalsDTO.getValidToDate() != null) {
			if (permitRenewalsDTO.getRequestRenewPeriod() == 0) {
				permitRenewalsDTO.setNewPermitExpirtDate(null);
				RequestContext.getCurrentInstance().update("newExpiryDateId");
			} else {
				int noOfMonths = permitRenewalsDTO.getRequestRenewPeriod();
			}
		} else {
			if (permitRenewalsDTO.getFromToDate() == null && permitRenewalsDTO.getValidToDate() == null) {
				setErrorMsg("From to date and Valid to date should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (permitRenewalsDTO.getFromToDate() == null && permitRenewalsDTO.getValidToDate() != null) {
				setErrorMsg("Valid from date should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else if (permitRenewalsDTO.getFromToDate() != null && permitRenewalsDTO.getValidToDate() == null) {
				setErrorMsg("Valid to date should be selected.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
	}

	public void renewalSaveNew() throws ParseException {

		if (permitRenewalsDTO.getPermitExpiredValidToDateObj() != null) {
			if (permitRenewalsDTO.getRequestRenewPeriod() != 0) {
				if (permitRenewalsDTO.getNewPermitExpirtDateObj() != null) {
					String newPermitDate = new SimpleDateFormat("dd/MM/yyyy")
							.format(permitRenewalsDTO.getNewPermitExpirtDateObj());
					permitRenewalsDTO.setNewPermitExpirtDate(newPermitDate);
					if (!permitRenewalsDTO.getNewPermitExpirtDate().equals("")
							|| permitRenewalsDTO.getNewPermitExpirtDate() != null) {
						String modifyBy = sessionBackingBean.loginUser;
						String permitDate = permitRenewalsDTO.getPermitExpiryDate();
						Date permitdate1 = new SimpleDateFormat("dd/MM/yyyy").parse(permitDate);
						if (permitRenewalsDTO.getNewPermitExpirtDate() != null) {
							setCheckNewExpiryDateBoolean(true);
						}
						if (checkNewExpiryDateBoolean == true) {
							permitRenewalsDTO.setModifyBy(modifyBy);
							permitRenewalsDTO.setRequestRenewPeriod(permitRenewalsDTO.getRequestRenewPeriod());
							permitRenewalsDTO.setNewPermitExpirtDate(permitRenewalsDTO.getNewPermitExpirtDate());
							permitRenewalsDTO.setSpecialRemark(permitRenewalsDTO.getSpecialRemark());
							permitRenewalsDTO.setApplicationNo(strSelectedApplicationNo);

						} else {
							setErrorMsg("Valid Date should be after New Permit Expiry Date.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
						boolean insertPermitHistoryRecrd = adminService.CopyPermitDetailsANDinsertPermitHistory(
								strSelectedApplicationNo, sessionBackingBean.getLoginUser());
						if (insertPermitHistoryRecrd == true) {
							// update
							int resultApplicationTable = adminService.updatePermitRenewalRecord(permitRenewalsDTO);

							if (resultApplicationTable == 0) {
								commonService.updateCommonTaskHistory(strSelectedBusRegNo, strSelectedApplicationNo,
										"MP005", "C", sessionBackingBean.getLoginUser());
								RequestContext.getCurrentInstance().update("frmRenewalInfo");
								setSuccessMsg("Successfully Saved.");
								RequestContext.getCurrentInstance().update("frmsuccessSve");
								RequestContext.getCurrentInstance().execute("PF('successSve').show()");

							} else {
								setErrorMsg("Could not save.");
								RequestContext.getCurrentInstance().update("frmerrorMsge");
								RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
							}
						} else {
							setErrorMsg("Could not save in history.");
							RequestContext.getCurrentInstance().update("frmerrorMsge");
							RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
						}
					} else {
						errorMsg = "Valid Request Renew Period should be entered.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Valid Request Renew Period should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Request Renew Period should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Permit Expired Valid to Date should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void renewalSave() throws ParseException {
		if (permitRenewalsDTO.getPermitExpiredValidToDateObj() != null) {
			if (permitRenewalsDTO.getRequestRenewPeriod() != 0) {
				if (!permitRenewalsDTO.getNewPermitExpirtDate().equals("")
						|| permitRenewalsDTO.getNewPermitExpirtDate() != null) {
					String modifyBy = sessionBackingBean.loginUser;
					String permitDate = permitRenewalsDTO.getPermitExpiryDate();
					Date permitdate1 = new SimpleDateFormat("dd/MM/yyyy").parse(permitDate);
					if (permitRenewalsDTO.getNewPermitExpirtDate() != null) {
						setCheckNewExpiryDateBoolean(true);
					}
					if (checkNewExpiryDateBoolean == true) {
						permitRenewalsDTO.setModifyBy(modifyBy);
						permitRenewalsDTO.setRequestRenewPeriod(permitRenewalsDTO.getRequestRenewPeriod());
						permitRenewalsDTO.setNewPermitExpirtDate(permitRenewalsDTO.getNewPermitExpirtDate());
						permitRenewalsDTO.setSpecialRemark(permitRenewalsDTO.getSpecialRemark());
						permitRenewalsDTO.setApplicationNo(strSelectedApplicationNo);

					} else {
						setErrorMsg("Valid Date should be after New Permit Expiry Date.");
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
					boolean insertPermitHistoryRecrd = adminService.CopyPermitDetailsANDinsertPermitHistory(
							strSelectedApplicationNo, sessionBackingBean.getLoginUser());
					if (insertPermitHistoryRecrd == true) {
						// update

						int resultApplicationTable = adminService.updatePermitRenewalRecord(permitRenewalsDTO);

						if (resultApplicationTable == 0) {
							commonService.updateCommonTaskHistory(strSelectedBusRegNo, strSelectedApplicationNo,
									"MP005", "C", sessionBackingBean.getLoginUser());
							RequestContext.getCurrentInstance().update("frmRenewalInfo");
							setSuccessMsg("Successfully Saved.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");

						} else {
							setErrorMsg("Could not save.");
							RequestContext.getCurrentInstance().update("frmerrorMsge");
							RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
						}
					} else {
						setErrorMsg("Could not save in history.");
						RequestContext.getCurrentInstance().update("frmerrorMsge");
						RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					}
				} else {
					errorMsg = "Valid Request Renew Period should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Request Renew Period should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Permit Expired Valid to Date should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void renewalClear() {
		getRenewalDetails();
	}

	private void getPaymentDetails() {
		if (!strSelectedApplicationNo.equals("")) {
			permitPaymentDTO = adminService.paymentByApplicationNo(strSelectedApplicationNo);
		}
	}

	private void getOminiBusDetails() {
		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().isEmpty() && permitDTO.getPermitNo() != ""
				&& !strSelectedApplicationNo.equals("")) {

			ominiBusDTO = adminService.ominiBusByApplicationNo(strSelectedApplicationNo);
			ominiBusDTO.setVehicleRegNo(permitDTO.getBusRegNo());
			if (ominiBusDTO.getVehicleRegNo() != null && ominiBusDTO.getVehicleRegNo() != " "
					&& !ominiBusDTO.getVehicleRegNo().isEmpty()) {
				strSelectedModel = ominiBusDTO.getModel();
				strSelectedMake = ominiBusDTO.getMake();
			}
		}
	}

	private void getOwnerDetails() {
		setDisabledDOB(false);
		setDisabledGender(false);
		strSelectedTitle = null;
		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().isEmpty() && permitDTO.getPermitNo() != "") {
			busOwnerDTO = adminService.ownerDetailsByApplicationNo(strSelectedApplicationNo);
			if (busOwnerDTO.getTitle() != null && busOwnerDTO.getTitle() != " " && !busOwnerDTO.getTitle().isEmpty()) {
				strSelectedTitle = busOwnerDTO.getTitle();
				strSelectedMartial = busOwnerDTO.getMaritalStatus();
				strSelectedGender = busOwnerDTO.getGender();
				strSelectedDivSec = busOwnerDTO.getDivSec();
				strSelectedDistrict = busOwnerDTO.getDistrict();
				strSelectedProvince = busOwnerDTO.getProvince();
				strSelectedLanguage = busOwnerDTO.getPerferedLanguage();
				onChangeTitle();
			}
		}
		setPreviousBusOwnerDTO(busOwnerDTO);
	}

	public void masterClear() {
		strSelectedApplicationNo = null;
		strSelectedPermitNo = null;
		strSelectedBusRegNo = null;
		booldisable = false;

		seachDis = false;

		setDisabledDOB(false);
		setDisabledGender(false);
		strSelectedTitle = null;
	}

	@SuppressWarnings("deprecation")
	public void permitNumberValidator() {
		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().isEmpty()) {
			Pattern ptr = Pattern.compile("^[a-zA-Z0-9\\-]*$");
			boolean valid = ptr.matcher(permitDTO.getPermitNo()).matches();
			if (valid) {
				String chkDuplicates = adminService.checkDuplicatePermitNo(permitDTO.getPermitNo());
				if (chkDuplicates != null) {
					errorMsg = "You have already entered this Permit Number";
					RequestContext.getCurrentInstance().update("frmerrorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					permitDTO.setPermitNo(null);
				}
			} else {
				errorMsg = "Invalid Permit Number";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				permitDTO.setPermitNo(null);
			}

		}
	}

	// check for renewals
	public void endDateExpireDateValidator() {
		if (permitDTO.getPermitExpire() != null && permitDTO.getPermitEndDate() != null
				&& permitDTO.getPermitExpire().after(permitDTO.getPermitEndDate())) {

			errorMsg = "Permit Expire Date should be less than or same as End Date of the Permit";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitDTO.setPermitExpire(null);

		}
	}

	public void busNoValidator() {
		if (permitDTO.getBusRegNo() != null && !permitDTO.getBusRegNo().isEmpty()) {
			String ifDublicate_permitNO = adminService.checkVehiNo(permitDTO.getBusRegNo());
			if (ifDublicate_permitNO != null && !ifDublicate_permitNO.trim().isEmpty()) {
				if (!ifDublicate_permitNO.equals(permitDTO.getPermitNo())) {
					errorMsg = "Duplicate Registration No. of the Bus";
					RequestContext.getCurrentInstance().update("frmerrorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					permitDTO.setBusRegNo(null);
				}

			}
		}
	}

	public void onRouteChange() {
		if (strSelectedRoute != null && !strSelectedRoute.equals("")) {
			routeDTO = adminService.getDetailsbyRouteNo(strSelectedRoute);
			if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().equals("")) {
				permitDTO.setVia(routeDTO.getVia());
				permitDTO.setDestination(routeDTO.getDestination());
				permitDTO.setOrigin(routeDTO.getOrigin());
				permitDTO.setBusFare(routeDTO.getBusFare());
			}
		} else {
			routeDTO = null;
		}
		routeFlag = false;
	}

	public void checkBusFare() {
		if (permitDTO.getBusFare() != null) {
			readOnlyBusFare = true;
		} else {
			readOnlyBusFare = false;
			permitDTO.setBusFare(null);
		}
	}

	public void totalBusFareValidation() {
		if (permitDTO.getBusFare() != null && permitDTO.getBusFare().signum() < 0) {
			errorMsg = "Invalid Total Bus Fare";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitDTO.setBusFare(null);
		}
	}

	public boolean routeFlagListener() {
		String location1;
		String location2;
		if (routeFlag) {
			location1 = permitDTO.getOrigin();
			location2 = permitDTO.getDestination();
			permitDTO.setOrigin(location2);
			permitDTO.setDestination(location1);
			permitDTO.setRouteFlag("Y");
			return false;
		} else {
			location1 = permitDTO.getOrigin();
			location2 = permitDTO.getDestination();
			permitDTO.setOrigin(location2);
			permitDTO.setDestination(location1);
			permitDTO.setRouteFlag("N");
			return true;
		}
	}

	@SuppressWarnings("deprecation")
	public void updatePermit() {
		if (permitDTO.getPermitNo() != null && !permitDTO.getPermitNo().trim().isEmpty()) {
			if (permitDTO.getBusRegNo() != null && !permitDTO.getBusRegNo().isEmpty()
					&& !permitDTO.getBusRegNo().equalsIgnoreCase("")) {

				//Commented By : Dinushi Ranasinghe
				//Given permission to edit the inactive backlog applications using the (service type, route no.)
				//Request By Damith on 16/11/2021 via teams call
				
				/*String ifDuplicate_busNo = adminService.checkVehiNo_Edit(permitDTO.getBusRegNo(), permitDTO.getSeq());
				if (ifDuplicate_busNo != null) {
					errorMsg = "Duplicate Registration No. of the bus.";
					RequestContext.getCurrentInstance().update("frmerrorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					permitDTO.setBusRegNo(null);
				} else {*/
					if (strSelectedServiceType != null && !strSelectedServiceType.isEmpty()
							&& !strSelectedServiceType.equalsIgnoreCase("")) {
						if (strSelectedRoute != null && !strSelectedRoute.isEmpty()
								&& !strSelectedRoute.equalsIgnoreCase("")) {

							if (permitDTO.getBusFare() != null) {
								// update
								boolean insertPermitHistoryRecrd = adminService.CopyPermitDetailsANDinsertPermitHistory(
										strSelectedApplicationNo, sessionBackingBean.getLoginUser());

								String prevPermitNo = adminService.getPreviousPermitNo(strSelectedApplicationNo);
								String prevVehicleNo = adminService.getPreviousVehicleNo(strSelectedApplicationNo);

								if (!permitDTO.getPermitNo().equals(prevPermitNo)) {
									boolean insertOminiBusHistoryRecrd = adminService
											.CopyOwnerDetailsANDinsertOminiBusHistory(strSelectedApplicationNo,
													sessionBackingBean.getLoginUser());
									boolean insertOwnerHistoryRecrd = adminService
											.CopyOwnerDetailsANDinsertOwnerHistory(strSelectedApplicationNo,
													sessionBackingBean.getLoginUser());
									if (insertOminiBusHistoryRecrd == true && insertOwnerHistoryRecrd == true) {

									}
								}

								if (!permitDTO.getBusRegNo().equals(prevVehicleNo)) {
									boolean insertOminiBusHistoryRecrd = adminService
											.CopyOwnerDetailsANDinsertOminiBusHistory(strSelectedApplicationNo,
													sessionBackingBean.getLoginUser());
									boolean insertOwnerHistoryRecrd = adminService
											.CopyOwnerDetailsANDinsertOwnerHistory(strSelectedApplicationNo,
													sessionBackingBean.getLoginUser());
									if (insertOminiBusHistoryRecrd == true && insertOwnerHistoryRecrd == true) {

									}
								}

								if (insertPermitHistoryRecrd == true) {
									permitDTO.setRouteNo(strSelectedRoute);
									permitDTO.setServiceType(strSelectedServiceType);
									permitDTO.setIsTenderPermit(strSelectedVal);
									permitDTO.setIsBacklogApp("Y");
									permitDTO.setModifiedBy(sessionBackingBean.loginUser);
									permitDTO.setStatus("A");
									int result = adminService.updateEditPermitRecord(permitDTO);
									int resultOfUpdaingOminiBusTbRegNo = adminService
											.updateBacklogOminiBusRegNo(permitDTO);
									int resultofUpdatingVehiOwnerTb = adminService.updateBacklogVehiOwner(permitDTO);

									boolean hasValidAmendmentApplication = adminService
											.hasValidAmendmentApplication(permitDTO.getApplicationNo());
									int updateAmendmentTbl = 0;
									if (hasValidAmendmentApplication) {
										updateAmendmentTbl = adminService.updateAmendmentDataForModifyPermitData(
												permitDTO, sessionBackingBean.loginUser);
									}

									if (result == 0 && resultOfUpdaingOminiBusTbRegNo == 0
											&& resultofUpdatingVehiOwnerTb == 0 && updateAmendmentTbl == 0) {
										commonService.updateCommonTaskHistory(strSelectedBusRegNo,strSelectedApplicationNo, "MP001", "C", sessionBackingBean.getLoginUser());
										RequestContext.getCurrentInstance().update("frmPermitInfo");
										setSuccessMsg("Successfully saved.");
										RequestContext.getCurrentInstance().update("frmsuccessSve");
										RequestContext.getCurrentInstance().execute("PF('successSve').show()");
										ominiBusDTO.setVehicleRegNo(permitDTO.getBusRegNo());

										permitDTO = adminService.searchBySeq(permitDTO.getSeq());
										permitNoList = adminService.getAllActivePermits();
										busRegNoList = adminService.getAllActiveBusRegNos();
										setStrSelectedPermitNo(permitDTO.getPermitNo());
										setStrSelectedBusRegNo(permitDTO.getBusRegNo());
										if (permitDTO.getRouteFlag() != null && !permitDTO.getRouteFlag().trim().equals("") && permitDTO.getRouteFlag().equalsIgnoreCase("Y")) {
											routeFlag = true;
											routeFlagListener();

										} else if (permitDTO.getRouteFlag() != null
												&& !permitDTO.getRouteFlag().trim().equals("")
												&& permitDTO.getRouteFlag().equalsIgnoreCase("N")) {

										}

									} else {
										setErrorMsg("Could not save.");
										RequestContext.getCurrentInstance().update("frmerrorMsge");
										RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
									}
								} else {
									setErrorMsg("Could not save to history.");
									RequestContext.getCurrentInstance().update("frmerrorMsge");
									RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
								}

							} else {
								errorMsg = "Total Bus Fare should be entered.";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							errorMsg = "Route No. should be selected.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "Service Type should be selected.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				}
			//Commented By : Dinushi Ranasinghe
			/*} else {
				errorMsg = "Registration No. of the Bus should be entered.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}*/

		} else {
			errorMsg = "Permit No. should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void mainClear() {
		strSelectedRoute = null;
		strSelectedServiceType = null;
		strSelectedVal = null;
		strSelectedTitle = null;
		strSelectedGender = null;
		strSelectedMartial = null;
		strSelectedProvince = null;
		strSelectedDistrict = null;
		strSelectedDivSec = null;
		strSelectedLanguage = null;
		strSelectedMake = null;
		strSelectedModel = null;
		searchDetails();
	}

	public void onChangeTitle() {
		String titleCode = adminService.getParaCodeForTitle();
		if (strSelectedTitle.equals(titleCode)) {
			setDisabledDOB(true);
			setDisabledGender(true);
			setCheckValiationsForInputFields(true);
			RequestContext.getCurrentInstance().update("Dob");
			setStrSelectedGender(null);
			busOwnerDTO.setDob(null);
		} else {
			setDisabledDOB(false);
			setDisabledGender(false);
			setCheckValiationsForInputFields(false);
		}
	}

	public void getDetailsByNicNo() {
		if (adminService.ownerDetailsByNicNo(busOwnerDTO.getNicNo()).getNicNo() != null) {

			busOwnerDTO = adminService.ownerDetailsByNicNo(busOwnerDTO.getNicNo());
			busOwnerDTO.setSeq(previousBusOwnerDTO.getSeq());
			strSelectedTitle = busOwnerDTO.getTitle();
			strSelectedMartial = busOwnerDTO.getMaritalStatus();
			strSelectedGender = busOwnerDTO.getGender();
			strSelectedDivSec = busOwnerDTO.getDivSec();
			strSelectedDistrict = busOwnerDTO.getDistrict();
			strSelectedProvince = busOwnerDTO.getProvince();
			strSelectedLanguage = busOwnerDTO.getPerferedLanguage();
		}
	}

	public void phoneNumberValidator() {
		if (busOwnerDTO.getTelephoneNo() != null && !busOwnerDTO.getTelephoneNo().isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{10}$");
			boolean valid = ptr.matcher(busOwnerDTO.getTelephoneNo()).matches();
			if (valid) {

			} else {
				errorMsg = "Invalid Telephone Number";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				busOwnerDTO.setTelephoneNo(null);
			}

		}
		if (busOwnerDTO.getMobileNo() != null && !busOwnerDTO.getMobileNo().isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{10}$");
			boolean valid = ptr.matcher(busOwnerDTO.getMobileNo()).matches();
			if (valid) {

			} else {
				errorMsg = "Invalid Mobile Number";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				busOwnerDTO.setMobileNo(null);
			}

		}
	}

	public void onProvinceChange() {
		if (!strSelectedProvince.equals("") || strSelectedProvince != null || !strSelectedProvince.isEmpty()) {
			districtList = adminService.getDistrictByProvinceToDropdown(strSelectedProvince);
		}
	}

	public void onDistrictChange() {
		if (!strSelectedDistrict.equals("") || strSelectedDistrict != null || !strSelectedDistrict.isEmpty()) {
			divSecList = adminService.getDivSecByDistrictToDropdown(strSelectedDistrict);
		}
	}

	public void busOwnerSave() {

		if (checkValiationsForInputFields == false) {
			if (strSelectedTitle != null && !strSelectedTitle.isEmpty() && !strSelectedTitle.equalsIgnoreCase("")) {
				if (strSelectedGender != null && !strSelectedGender.isEmpty()
						&& !strSelectedGender.equalsIgnoreCase("")) {
					if (busOwnerDTO.getDob() != null) {
						if (busOwnerDTO.getFullName() != null && !busOwnerDTO.getFullName().isEmpty()
								&& !busOwnerDTO.getFullName().equalsIgnoreCase("")) {

							if (busOwnerDTO.getAddress1() != null && !busOwnerDTO.getAddress1().isEmpty()
									&& !busOwnerDTO.getAddress1().equalsIgnoreCase("")) {
								if (busOwnerDTO.getAddress2() != null && !busOwnerDTO.getAddress2().isEmpty()
										&& !busOwnerDTO.getAddress2().equalsIgnoreCase("")) {
									if (busOwnerDTO.getCity() != null && !busOwnerDTO.getCity().isEmpty()
											&& !busOwnerDTO.getCity().equalsIgnoreCase("")) {
										if (strSelectedProvince != null && !strSelectedProvince.isEmpty()
												&& !strSelectedProvince.equalsIgnoreCase("")) {
											if (busOwnerDTO.getNicNo() != null && !busOwnerDTO.getNicNo().isEmpty()
													&& !busOwnerDTO.getNicNo().equalsIgnoreCase("")) {
												if (strSelectedLanguage.equals("S")) {
													if (busOwnerDTO.getFullNameSinhala() != null
															&& !busOwnerDTO.getFullNameSinhala().isEmpty()
															&& !busOwnerDTO.getFullNameSinhala().equalsIgnoreCase("")) {
														if (busOwnerDTO.getAddress1Sinhala() != null
																&& !busOwnerDTO.getAddress1Sinhala().isEmpty()
																&& !busOwnerDTO.getAddress1Sinhala()
																		.equalsIgnoreCase("")) {
															if (busOwnerDTO.getAddress2Sinhala() != null
																	&& !busOwnerDTO.getAddress2Sinhala().isEmpty()
																	&& !busOwnerDTO.getAddress2Sinhala()
																			.equalsIgnoreCase("")) {
																if (busOwnerDTO.getCitySinhala() != null
																		&& !busOwnerDTO.getCitySinhala().isEmpty()
																		&& !busOwnerDTO.getCitySinhala()
																				.equalsIgnoreCase("")) {
																	if (busOwnerDTO.getSeq() != 0) {
																		// Update
																		updateOwner();
																	} else {
																		// save
																		saveOwner();
																	}
																} else {
																	errorMsg = "City (Sinhala) should be entered.";
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																errorMsg = "Address 2 (Sinhala) should be entered.";
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
														} else {
															errorMsg = "Address 1 (Sinhala) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else {
														errorMsg = "Nane in Full (Sinhala) should be entered.";
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
												} else if (strSelectedLanguage.equals("T")) {
													if (busOwnerDTO.getFullNameTamil() != null
															&& !busOwnerDTO.getFullNameTamil().isEmpty()
															&& !busOwnerDTO.getFullNameTamil().equalsIgnoreCase("")) {
														if (busOwnerDTO.getAddress1Tamil() != null
																&& !busOwnerDTO.getAddress1Tamil().isEmpty()
																&& !busOwnerDTO.getAddress1Tamil()
																		.equalsIgnoreCase("")) {
															if (busOwnerDTO.getAddress2Tamil() != null
																	&& !busOwnerDTO.getAddress2Tamil().isEmpty()
																	&& !busOwnerDTO.getAddress2Tamil()
																			.equalsIgnoreCase("")) {
																if (busOwnerDTO.getCity() != null
																		&& !busOwnerDTO.getCity().isEmpty()
																		&& !busOwnerDTO.getCity()
																				.equalsIgnoreCase("")) {
																	if (busOwnerDTO.getSeq() != 0) {
																		// Update
																		updateOwner();
																	} else {
																		// save
																		saveOwner();
																	}
																} else {
																	errorMsg = "City (Tamil) should be entered.";
																	RequestContext.getCurrentInstance()
																			.update("frmrequiredField");
																	RequestContext.getCurrentInstance()
																			.execute("PF('requiredField').show()");
																}
															} else {
																errorMsg = "Address 2 (Tamil) should be entered.";
																RequestContext.getCurrentInstance()
																		.update("frmrequiredField");
																RequestContext.getCurrentInstance()
																		.execute("PF('requiredField').show()");
															}
														} else {
															errorMsg = "Address 1 (Tamil) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else {
														errorMsg = "Nane in Full (Tamil) should be entered.";
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
												} else {
													if (busOwnerDTO.getSeq() != 0) {
														// Update
														updateOwner();
													} else {
														// save
														saveOwner();
													}
												}

											} else {
												errorMsg = "NIC/Org.Reg. No. should be entered.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else {
											errorMsg = "Province should be selected.";
											RequestContext.getCurrentInstance().update("frmrequiredField");
											RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
										}
									} else {
										errorMsg = "City should be entered.";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									errorMsg = "Address 2 should be entered.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								errorMsg = "Address 1 should be entered.";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							errorMsg = "Full Name should be entered.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "Date of Birth should be selected.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Gender should be selected.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Title should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else if (checkValiationsForInputFields == true) {
			if (strSelectedTitle != null && !strSelectedTitle.isEmpty() && !strSelectedTitle.equalsIgnoreCase("")) {
				if (busOwnerDTO.getFullName() != null && !busOwnerDTO.getFullName().isEmpty()
						&& !busOwnerDTO.getFullName().equalsIgnoreCase("")) {

					if (busOwnerDTO.getAddress1() != null && !busOwnerDTO.getAddress1().isEmpty()
							&& !busOwnerDTO.getAddress1().equalsIgnoreCase("")) {
						if (busOwnerDTO.getAddress2() != null && !busOwnerDTO.getAddress2().isEmpty()
								&& !busOwnerDTO.getAddress2().equalsIgnoreCase("")) {
							if (busOwnerDTO.getCity() != null && !busOwnerDTO.getCity().isEmpty()
									&& !busOwnerDTO.getCity().equalsIgnoreCase("")) {
								if (strSelectedProvince != null && !strSelectedProvince.isEmpty()
										&& !strSelectedProvince.equalsIgnoreCase("")) {
									if (busOwnerDTO.getNicNo() != null && !busOwnerDTO.getNicNo().isEmpty()
											&& !busOwnerDTO.getNicNo().equalsIgnoreCase("")) {
										if (strSelectedLanguage.equals("S")) {
											if (busOwnerDTO.getFullNameSinhala() != null
													&& !busOwnerDTO.getFullNameSinhala().isEmpty()
													&& !busOwnerDTO.getFullNameSinhala().equalsIgnoreCase("")) {
												if (busOwnerDTO.getAddress1Sinhala() != null
														&& !busOwnerDTO.getAddress1Sinhala().isEmpty()
														&& !busOwnerDTO.getAddress1Sinhala().equalsIgnoreCase("")) {
													if (busOwnerDTO.getAddress2Sinhala() != null
															&& !busOwnerDTO.getAddress2Sinhala().isEmpty()
															&& !busOwnerDTO.getAddress2Sinhala().equalsIgnoreCase("")) {
														if (busOwnerDTO.getCitySinhala() != null
																&& !busOwnerDTO.getCitySinhala().isEmpty()
																&& !busOwnerDTO.getCitySinhala().equalsIgnoreCase("")) {
															if (busOwnerDTO.getSeq() != 0) {
																// Update
																updateOwner();
															} else {
																// save
																saveOwner();
															}
														} else {
															errorMsg = "City (Sinhala) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else {
														errorMsg = "Address 2 (Sinhala) should be entered.";
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
												} else {
													errorMsg = "Address 1 (Sinhala) should be entered.";
													RequestContext.getCurrentInstance().update("frmrequiredField");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}
											} else {
												errorMsg = "Nane in Full (Sinhala) should be entered.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else if (strSelectedLanguage.equals("T")) {
											if (busOwnerDTO.getFullNameTamil() != null
													&& !busOwnerDTO.getFullNameTamil().isEmpty()
													&& !busOwnerDTO.getFullNameTamil().equalsIgnoreCase("")) {
												if (busOwnerDTO.getAddress1Tamil() != null
														&& !busOwnerDTO.getAddress1Tamil().isEmpty()
														&& !busOwnerDTO.getAddress1Tamil().equalsIgnoreCase("")) {
													if (busOwnerDTO.getAddress2Tamil() != null
															&& !busOwnerDTO.getAddress2Tamil().isEmpty()
															&& !busOwnerDTO.getAddress2Tamil().equalsIgnoreCase("")) {
														if (busOwnerDTO.getCity() != null
																&& !busOwnerDTO.getCity().isEmpty()
																&& !busOwnerDTO.getCity().equalsIgnoreCase("")) {
															if (busOwnerDTO.getSeq() != 0) {
																// Update
																updateOwner();
															} else {
																// save
																saveOwner();
															}
														} else {
															errorMsg = "City (Tamil) should be entered.";
															RequestContext.getCurrentInstance()
																	.update("frmrequiredField");
															RequestContext.getCurrentInstance()
																	.execute("PF('requiredField').show()");
														}
													} else {
														errorMsg = "Address 2 (Tamil) should be entered.";
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
												} else {
													errorMsg = "Address 1 (Tamil) should be entered.";
													RequestContext.getCurrentInstance().update("frmrequiredField");
													RequestContext.getCurrentInstance()
															.execute("PF('requiredField').show()");
												}
											} else {
												errorMsg = "Nane in Full (Tamil) should be entered.";
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
											}
										} else {
											if (busOwnerDTO.getSeq() != 0) {
												// Update
												updateOwner();
											} else {
												// save
												saveOwner();
											}
										}

									} else {
										errorMsg = "NIC/Org.Reg. No. should be entered.";
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								} else {
									errorMsg = "Province should be selected.";
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}
							} else {
								errorMsg = "City should be entered.";
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}
						} else {
							errorMsg = "Address 2 should be entered.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "Address 1 should be entered.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}

				} else {
					errorMsg = "Full Name should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Title should be selected.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}

	}

	private void saveOwner() {
		busOwnerDTO.setTitle(strSelectedTitle);
		busOwnerDTO.setGender(strSelectedGender);
		busOwnerDTO.setPerferedLanguage(strSelectedLanguage);
		busOwnerDTO.setProvince(strSelectedProvince);
		busOwnerDTO.setDistrict(strSelectedDistrict);
		busOwnerDTO.setDivSec(strSelectedDivSec);
		busOwnerDTO.setApplicationNo(permitDTO.getApplicationNo());
		busOwnerDTO.setPermitNo(permitDTO.getPermitNo());
		busOwnerDTO.setBusRegNo(permitDTO.getBusRegNo());
		busOwnerDTO.setCreatedBy(sessionBackingBean.loginUser);
		busOwnerDTO.setIsBacklogApp("Y");

		int result = adminService.saveBusOwnerDetails(busOwnerDTO);

		if (result == 0) {
			getOwnerDetails();
			commonService.updateCommonTaskHistory(strSelectedBusRegNo, strSelectedApplicationNo, "MP002", "C", sessionBackingBean.getLoginUser());
			RequestContext.getCurrentInstance().update("frmBusOwnerInfo");
			setSuccessMsg("Successfully saved.");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

		} else {
			setErrorMsg("Could not Save.");
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
		}
	}

	private void updateOwner() {
		// select prev values DTO and Insert in history
		boolean insertOwnerHistoryRecrd = adminService.CopyOwnerDetailsANDinsertOwnerHistory(strSelectedApplicationNo,
				sessionBackingBean.getLoginUser());
		if (insertOwnerHistoryRecrd == true) {
			busOwnerDTO.setTitle(strSelectedTitle);
			busOwnerDTO.setGender(strSelectedGender);
			busOwnerDTO.setPerferedLanguage(strSelectedLanguage);
			busOwnerDTO.setProvince(strSelectedProvince);
			busOwnerDTO.setDistrict(strSelectedDistrict);
			busOwnerDTO.setDivSec(strSelectedDivSec);
			busOwnerDTO.setApplicationNo(permitDTO.getApplicationNo());
			busOwnerDTO.setPermitNo(permitDTO.getPermitNo());
			busOwnerDTO.setBusRegNo(permitDTO.getBusRegNo());
			busOwnerDTO.setModifiedBy(sessionBackingBean.loginUser);
			busOwnerDTO.setIsBacklogApp("Y");
			int result = adminService.updateBusOwner(busOwnerDTO);
			if (result == 0) {
				getOwnerDetails();
				commonService.updateCommonTaskHistory(strSelectedBusRegNo, strSelectedApplicationNo, "MP002", "C", sessionBackingBean.getLoginUser());
				RequestContext.getCurrentInstance().update("frmBusOwnerInfo");
				setSuccessMsg("Successfully Saved.");
				RequestContext.getCurrentInstance().update("frmsuccessSve");
				RequestContext.getCurrentInstance().execute("PF('successSve').show()");

			} else {
				setErrorMsg("Could not save.");
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			}

		} else {
			setErrorMsg("Could not save in history.");
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
		}
	}

	public void busOwnerClear() {
		strSelectedTitle = null;
		strSelectedMartial = null;
		strSelectedGender = null;
		strSelectedDivSec = null;
		strSelectedDistrict = null;
		strSelectedProvince = null;
		strSelectedLanguage = null;
		busOwnerDTO = new BusOwnerDTO();
		setDisabledDOB(false);
		setDisabledGender(false);
		getOwnerDetails();
	}

	public void manufactureYearRegistrationYearValidator() {
		if (ominiBusDTO.getManufactureDate() != null && !ominiBusDTO.getManufactureDate().isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{4}$");
			boolean valid = ptr.matcher(ominiBusDTO.getManufactureDate()).matches();
			if (valid) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy");
				Date date = new Date();
				String year = dateFormat.format(date);
				int curYear = Integer.parseInt(year);
				int manuYear = Integer.parseInt(ominiBusDTO.getManufactureDate());
				if (curYear >= manuYear) {

				} else {
					errorMsg = "Invalid Manufacture Year";
					RequestContext.getCurrentInstance().update("frmerrorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
					ominiBusDTO.setManufactureDate(null);
				}

			} else {
				errorMsg = "Invalid Manufacture Year";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				ominiBusDTO.setManufactureDate(null);
			}

		}

		if (ominiBusDTO.getRegistrationDate() != null && ominiBusDTO.getManufactureDate() != null) {
			int regYear = ominiBusDTO.getRegistrationDate().getYear();
			regYear = regYear + 1900;
			int manuYear = Integer.parseInt(ominiBusDTO.getManufactureDate());

			if (manuYear > regYear) {
				errorMsg = "Date of Registration should be greater than or same as Manufacture Year";
				RequestContext.getCurrentInstance().update("frmerrorMsge");
				RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				ominiBusDTO.setRegistrationDate(null);

			}

		}

	}

	public void seatingCapacityValidation() {
		if (ominiBusDTO.getSeating() != null && ominiBusDTO.getSeating().charAt(0) == '-') {
			errorMsg = "Invalid Seating Capacity";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			ominiBusDTO.setSeating(null);

		}
	}

	public void onMakeChange() {
		if (!strSelectedMake.equals("") || strSelectedMake != null || !strSelectedMake.isEmpty()) {
			disableCreateModels = true;
			disabledModel = false;
			modelList = adminService.getModelsByMakeToDropdown(strSelectedMake);
		}
	}

	public void noOfDoorsValidation() {
		if (ominiBusDTO.getNoofDoors() != null && ominiBusDTO.getNoofDoors().charAt(0) == '-') {
			errorMsg = "Invalid No. of Doors";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			ominiBusDTO.setNoofDoors(null);

		}
	}

	public void modelChange() {
		disableCreateModels = true;
	}

	public void weightValidation() {
		if (ominiBusDTO.getWeight() != null && ominiBusDTO.getWeight().charAt(0) == '-') {
			errorMsg = "Invalid Weight";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			ominiBusDTO.setWeight(null);

		}
	}

	public void createModels() {
		RequestContext.getCurrentInstance().execute("PF('viewCreateModels').show()");
	}

	public void ominiBusSave() {
		if (ominiBusDTO.getVehicleRegNo() != null && !ominiBusDTO.getVehicleRegNo().isEmpty()
				&& !ominiBusDTO.getVehicleRegNo().equalsIgnoreCase("")) {
			if (ominiBusDTO.getVehicleRegNo().equals(permitDTO.getBusRegNo())) {
				if (ominiBusDTO.getSeating() != null && !ominiBusDTO.getSeating().isEmpty()
						&& !ominiBusDTO.getSeating().equalsIgnoreCase("")) {
					if (ominiBusDTO.getNoofDoors() != null && !ominiBusDTO.getNoofDoors().isEmpty()
							&& !ominiBusDTO.getNoofDoors().equalsIgnoreCase("")) {
						if (ominiBusDTO.getWeight() != null && !ominiBusDTO.getWeight().isEmpty()
								&& !ominiBusDTO.getWeight().equalsIgnoreCase("")) {
							if (ominiBusDTO.getSeq() != 0) {
								// update
								boolean insertOminiBusHistoryRecrd = adminService
										.CopyOwnerDetailsANDinsertOminiBusHistory(strSelectedApplicationNo,
												sessionBackingBean.getLoginUser());
								if (insertOminiBusHistoryRecrd == true) {
									ominiBusDTO.setModifiedBy(sessionBackingBean.getLoginUser());
									ominiBusDTO.setMake(strSelectedMake);
									ominiBusDTO.setModel(strSelectedModel);
									ominiBusDTO.setApplicationNo(permitDTO.getApplicationNo());
									ominiBusDTO.setPermitNo(permitDTO.getPermitNo());

									int result = adminService.updateOminiBus(ominiBusDTO);

									if (result == 0) {
										getOminiBusDetails();
										commonService.updateCommonTaskHistory(strSelectedBusRegNo,strSelectedApplicationNo, "MP003", "C",sessionBackingBean.getLoginUser());
										RequestContext.getCurrentInstance().update("frmOminiBusInfo");
										setSuccessMsg("Successfully Saved.");
										RequestContext.getCurrentInstance().update("frmsuccessSve");
										RequestContext.getCurrentInstance().execute("PF('successSve').show()");

									} else {
										setErrorMsg("Could not save.");
										RequestContext.getCurrentInstance().update("frmerrorMsge");
										RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
									}
								} else {
									setErrorMsg("Could not save in history.");
									RequestContext.getCurrentInstance().update("frmerrorMsge");
									RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
								}

							} else {
								// Save
								ominiBusDTO.setIsBacklogApp("Y");
								ominiBusDTO.setCreatedBy(sessionBackingBean.getLoginUser());
								ominiBusDTO.setMake(strSelectedMake);
								ominiBusDTO.setModel(strSelectedModel);
								ominiBusDTO.setApplicationNo(permitDTO.getApplicationNo());
								ominiBusDTO.setPermitNo(permitDTO.getPermitNo());

								int result = adminService.saveBacklogOminiBus(ominiBusDTO);

								if (result == 0) {
									getOminiBusDetails();
									commonService.updateCommonTaskHistory(strSelectedBusRegNo,strSelectedApplicationNo, "MP003", "C",sessionBackingBean.getLoginUser());
									setSuccessMsg("Successfully Saved.");
									RequestContext.getCurrentInstance().update("frmsuccessSve");
									RequestContext.getCurrentInstance().execute("PF('successSve').show()");

								} else {
									setErrorMsg("Could not save.");
									RequestContext.getCurrentInstance().update("frmerrorMsge");
									RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
								}
							}
						} else {
							errorMsg = "Weight should be entered.";
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "No. of Doors should be entered.";
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "Seating Capacity should be entered.";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Entered Registration No. is mismatch with Permit Information.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Registration No. of the Bus should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void ominiBusClear() {
		String busReg = ominiBusDTO.getVehicleRegNo();
		strSelectedMake = null;
		strSelectedModel = null;
		ominiBusDTO = new OminiBusDTO();
		getOminiBusDetails();
		ominiBusDTO.setVehicleRegNo(busReg);
		disableCreateModels = false;
		disabledModel = true;
	}

	public void totalPermitAmountValidation() {
		if (permitPaymentDTO.getTotalPermitAmt().signum() < 0) {
			errorMsg = "Invalid Total Permit Amount";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setTotalPermitAmt(null);
		}
	}

	public void excessAmountValidation() {
		if (permitPaymentDTO.getExcessAmt().signum() < 0) {
			errorMsg = "Invalid Excess Amount";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setExcessAmt(null);
		}

	}

	public void renewalAmountValidation() {
		if (permitPaymentDTO.getRenewalAmt() != null && permitPaymentDTO.getRenewalAmt().signum() < 0) {
			errorMsg = "Invalid Renewal Fee";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setRenewalAmt(null);
		}
		calculateTotal();
	}

	private void calculateTotal() {
		BigDecimal ren1 = null;
		BigDecimal penal1 = null;
		BigDecimal tender1 = null;
		BigDecimal ser1 = null;
		BigDecimal oth1 = null;
		BigDecimal sum = null;

		BigDecimal d = new BigDecimal(0);
		if (permitPaymentDTO.getRenewalAmt() != null)
			ren1 = permitPaymentDTO.getRenewalAmt();
		else
			ren1 = new BigDecimal(0);
		if (permitPaymentDTO.getPenaltyAmt() != null)
			penal1 = permitPaymentDTO.getPenaltyAmt();
		else
			penal1 = new BigDecimal(0);
		if (permitPaymentDTO.getTenderFee() != null)
			tender1 = permitPaymentDTO.getTenderFee();
		else
			tender1 = new BigDecimal(0);
		if (permitPaymentDTO.getServiceFee() != null)
			ser1 = permitPaymentDTO.getServiceFee();
		else
			ser1 = new BigDecimal(0);
		if (permitPaymentDTO.getOtherFee() != null)
			oth1 = permitPaymentDTO.getOtherFee();
		else
			oth1 = new BigDecimal(0);

		sum = d.add(ren1).add(penal1).add(tender1).add(ser1).add(oth1);
		permitPaymentDTO.setTotalFee(sum);

	}

	public void penaltyAmountValidation() {
		if (permitPaymentDTO.getPenaltyAmt() != null && permitPaymentDTO.getPenaltyAmt().signum() < 0) {
			errorMsg = "Invalid Penalty Fee";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setPenaltyAmt(null);
		}
		calculateTotal();
	}

	public void tenderAmountValidation() {
		if (permitPaymentDTO.getTenderFee() != null && permitPaymentDTO.getTenderFee().signum() < 0) {
			errorMsg = "Invalid Tender Fee";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setTenderFee(null);
		}
		calculateTotal();
	}

	public void serviceAmountValidation() {
		if (permitPaymentDTO.getServiceFee() != null && permitPaymentDTO.getServiceFee().signum() < 0) {
			errorMsg = "Invalid Service Fee";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setServiceFee(null);
		}
		calculateTotal();
	}

	public void otherAmountValidation() {
		if (permitPaymentDTO.getOtherFee() != null && permitPaymentDTO.getOtherFee().signum() < 0) {
			errorMsg = "Invalid Other Fee";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			permitPaymentDTO.setOtherFee(null);
		}
		calculateTotal();
	}

	public void paymentSave() {

		if (permitPaymentDTO.getSeq() != 0) {
			// update
			boolean insertPaymentHistoryRecrd = adminService.CopyPaymentDetailsANDinsertPaymentHistory(
					strSelectedApplicationNo, sessionBackingBean.getLoginUser());
			if (insertPaymentHistoryRecrd == true) {
				permitPaymentDTO.setModifiedBy(sessionBackingBean.getLoginUser());

				int result = adminService.updateBacklogPayments(permitPaymentDTO);

				if (result == 0) {
					commonService.updateCommonTaskHistory(strSelectedBusRegNo, strSelectedApplicationNo, "MP004", "C",
							sessionBackingBean.getLoginUser());
					getPaymentDetails();
					RequestContext.getCurrentInstance().update("frmPaymentInfo");
					setSuccessMsg("Successfully saved.");
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {
					setErrorMsg("Could not save.");
					RequestContext.getCurrentInstance().update("frmerrorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				}
			} else {
				// save
				permitPaymentDTO.setIsBacklogApp("Y");
				permitPaymentDTO.setCreatedBy(sessionBackingBean.getLoginUser());
				permitPaymentDTO.setPermitNo(permitDTO.getPermitNo());
				permitPaymentDTO.setApplicationNo(permitDTO.getApplicationNo());
				permitPaymentDTO.setVehicleRegNo(permitDTO.getBusRegNo());

				int result = adminService.saveBacklogPayments(permitPaymentDTO);

				if (result == 0) {
					getPaymentDetails();
					commonService.updateCommonTaskHistory(strSelectedBusRegNo, strSelectedApplicationNo, "MP004", "C",
							sessionBackingBean.getLoginUser());
					RequestContext.getCurrentInstance().update("frmPaymentInfo");
					setSuccessMsg("Successfully saved.");
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");

				} else {
					setErrorMsg("Could not save.");
					RequestContext.getCurrentInstance().update("frmerrorMsge");
					RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
				}
			}

		} else {
			setErrorMsg("Could not save in history.");
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
		}

	}

	public void paymentClear() {
		permitPaymentDTO = new PermitPaymentDTO();
		getPaymentDetails();
	}

	public void removeBus() {
		RequestContext.getCurrentInstance().execute("PF('comfirmMSG').show()");
	}

	public void saveDate() {
		String strUser = sessionBackingBean.loginUser;
		String strBusNo = permitDTO.getBusRegNo() + "-R";

		// update omini bus,vehicle owner,application and insert removed bus det
		// table
		boolean boolIsInserted = adminService.removeBusProcess(permitDTO, strBusNo, strUser);
		if (boolIsInserted) {
			String strOldBusNo = permitDTO.getBusRegNo();
			commonService.updateCommonTaskHistory(strOldBusNo, permitDTO.getApplicationNo(), "BR001", "C", strUser);
			setSuccessMsg("Successfully Saved.");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			permitDTO.setBusRegNo(strBusNo);
			disupdateBtn = true;
			disablePrint = false;

		} else {
			errorMsg = "Data not Updated.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			disupdateBtn = false;
		}
	}

	// print bus remove inform letter
	public StreamedContent print() throws JRException {
		String sourceFileName = null;
		files = null;
		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//RemovedBusInformLetter.jrxml";
			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_permit_no", permitDTO.getPermitNo());
			parameters.put("P_bus_no", permitDTO.getBusRegNo());

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "Bus Removal letter.pdf");

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

	}

	public List<String> getAppNoListN() {
		return appNoListN;
	}

	public void setAppNoListN(List<String> appNoListN) {
		this.appNoListN = appNoListN;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public PermitRenewalsService getPermitRenewalsService() {
		return permitRenewalsService;
	}

	public void setPermitRenewalsService(PermitRenewalsService permitRenewalsService) {
		this.permitRenewalsService = permitRenewalsService;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public PermitDTO getPermitDTO() {
		return permitDTO;
	}

	public void setPermitDTO(PermitDTO permitDTO) {
		this.permitDTO = permitDTO;
	}

	public BusOwnerDTO getBusOwnerDTO() {
		return busOwnerDTO;
	}

	public void setBusOwnerDTO(BusOwnerDTO busOwnerDTO) {
		this.busOwnerDTO = busOwnerDTO;
	}

	public OminiBusDTO getOminiBusDTO() {
		return ominiBusDTO;
	}

	public void setOminiBusDTO(OminiBusDTO ominiBusDTO) {
		this.ominiBusDTO = ominiBusDTO;
	}

	public PermitPaymentDTO getPermitPaymentDTO() {
		return permitPaymentDTO;
	}

	public void setPermitPaymentDTO(PermitPaymentDTO permitPaymentDTO) {
		this.permitPaymentDTO = permitPaymentDTO;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public List<CommonDTO> getRoutefordropdownList() {
		return routefordropdownList;
	}

	public void setRoutefordropdownList(List<CommonDTO> routefordropdownList) {
		this.routefordropdownList = routefordropdownList;
	}

	public List<CommonDTO> getServiceTypedropdownList() {
		return serviceTypedropdownList;
	}

	public void setServiceTypedropdownList(List<CommonDTO> serviceTypedropdownList) {
		this.serviceTypedropdownList = serviceTypedropdownList;
	}

	public List<CommonDTO> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<CommonDTO> titleList) {
		this.titleList = titleList;
	}

	public List<CommonDTO> getGenderList() {
		return genderList;
	}

	public void setGenderList(List<CommonDTO> genderList) {
		this.genderList = genderList;
	}

	public List<CommonDTO> getMartialList() {
		return martialList;
	}

	public void setMartialList(List<CommonDTO> martialList) {
		this.martialList = martialList;
	}

	public List<CommonDTO> getProvincelList() {
		return provincelList;
	}

	public void setProvincelList(List<CommonDTO> provincelList) {
		this.provincelList = provincelList;
	}

	public List<CommonDTO> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<CommonDTO> districtList) {
		this.districtList = districtList;
	}

	public List<CommonDTO> getDivSecList() {
		return divSecList;
	}

	public void setDivSecList(List<CommonDTO> divSecList) {
		this.divSecList = divSecList;
	}

	public List<CommonDTO> getMakeList() {
		return makeList;
	}

	public void setMakeList(List<CommonDTO> makeList) {
		this.makeList = makeList;
	}

	public List<CommonDTO> getModelList() {
		return modelList;
	}

	public void setModelList(List<CommonDTO> modelList) {
		this.modelList = modelList;
	}

	public List<String> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<String> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<String> getAppNoList() {
		return appNoList;
	}

	public void setAppNoList(List<String> appNoList) {
		this.appNoList = appNoList;
	}

	public List<String> getBusRegNoList() {
		return busRegNoList;
	}

	public void setBusRegNoList(List<String> busRegNoList) {
		this.busRegNoList = busRegNoList;
	}

	public String getStrSelectedRoute() {
		return strSelectedRoute;
	}

	public void setStrSelectedRoute(String strSelectedRoute) {
		this.strSelectedRoute = strSelectedRoute;
	}

	public String getStrSelectedVal() {
		return strSelectedVal;
	}

	public void setStrSelectedVal(String strSelectedVal) {
		this.strSelectedVal = strSelectedVal;
	}

	public String getStrSelectedServiceType() {
		return strSelectedServiceType;
	}

	public void setStrSelectedServiceType(String strSelectedServiceType) {
		this.strSelectedServiceType = strSelectedServiceType;
	}

	public String getStrSelectedTitle() {
		return strSelectedTitle;
	}

	public void setStrSelectedTitle(String strSelectedTitle) {
		this.strSelectedTitle = strSelectedTitle;
	}

	public String getStrSelectedGender() {
		return strSelectedGender;
	}

	public void setStrSelectedGender(String strSelectedGender) {
		this.strSelectedGender = strSelectedGender;
	}

	public String getStrSelectedMartial() {
		return strSelectedMartial;
	}

	public void setStrSelectedMartial(String strSelectedMartial) {
		this.strSelectedMartial = strSelectedMartial;
	}

	public String getStrSelectedProvince() {
		return strSelectedProvince;
	}

	public void setStrSelectedProvince(String strSelectedProvince) {
		this.strSelectedProvince = strSelectedProvince;
	}

	public String getStrSelectedDistrict() {
		return strSelectedDistrict;
	}

	public void setStrSelectedDistrict(String strSelectedDistrict) {
		this.strSelectedDistrict = strSelectedDistrict;
	}

	public String getStrSelectedDivSec() {
		return strSelectedDivSec;
	}

	public void setStrSelectedDivSec(String strSelectedDivSec) {
		this.strSelectedDivSec = strSelectedDivSec;
	}

	public String getStrSelectedLanguage() {
		return strSelectedLanguage;
	}

	public void setStrSelectedLanguage(String strSelectedLanguage) {
		this.strSelectedLanguage = strSelectedLanguage;
	}

	public String getStrSelectedMake() {
		return strSelectedMake;
	}

	public void setStrSelectedMake(String strSelectedMake) {
		this.strSelectedMake = strSelectedMake;
	}

	public String getStrSelectedModel() {
		return strSelectedModel;
	}

	public void setStrSelectedModel(String strSelectedModel) {
		this.strSelectedModel = strSelectedModel;
	}

	public BigDecimal getCalTot() {
		return calTot;
	}

	public void setCalTot(BigDecimal calTot) {
		this.calTot = calTot;
	}

	public String getStrSelectedPermitNo() {
		return strSelectedPermitNo;
	}

	public void setStrSelectedPermitNo(String strSelectedPermitNo) {
		this.strSelectedPermitNo = strSelectedPermitNo;
	}

	public String getStrSelectedApplicationNo() {
		return strSelectedApplicationNo;
	}

	public void setStrSelectedApplicationNo(String strSelectedApplicationNo) {
		this.strSelectedApplicationNo = strSelectedApplicationNo;
	}

	public String getStrSelectedBusRegNo() {
		return strSelectedBusRegNo;
	}

	public void setStrSelectedBusRegNo(String strSelectedBusRegNo) {
		this.strSelectedBusRegNo = strSelectedBusRegNo;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getBoolEmpAddDet() {
		return boolEmpAddDet;
	}

	public void setBoolEmpAddDet(Boolean boolEmpAddDet) {
		this.boolEmpAddDet = boolEmpAddDet;
	}

	public Boolean getBooldisable() {
		return booldisable;
	}

	public void setBooldisable(Boolean booldisable) {
		this.booldisable = booldisable;
	}

	public Boolean getBisablebutton() {
		return bisablebutton;
	}

	public void setBisablebutton(Boolean bisablebutton) {
		this.bisablebutton = bisablebutton;
	}

	public Boolean getDisableMode() {
		return disableMode;
	}

	public void setDisableMode(Boolean disableMode) {
		this.disableMode = disableMode;
	}

	public Boolean getRouteFlag() {
		return routeFlag;
	}

	public void setRouteFlag(Boolean routeFlag) {
		this.routeFlag = routeFlag;
	}

	public Boolean getReadOnlyBusFare() {
		return readOnlyBusFare;
	}

	public void setReadOnlyBusFare(Boolean readOnlyBusFare) {
		this.readOnlyBusFare = readOnlyBusFare;
	}

	public Boolean getSeachDis() {
		return seachDis;
	}

	public void setSeachDis(Boolean seachDis) {
		this.seachDis = seachDis;
	}

	public boolean isDisableCreateModels() {
		return disableCreateModels;
	}

	public void setDisableCreateModels(boolean disableCreateModels) {
		this.disableCreateModels = disableCreateModels;
	}

	public boolean isDisabledModel() {
		return disabledModel;
	}

	public void setDisabledModel(boolean disabledModel) {
		this.disabledModel = disabledModel;
	}

	public boolean isDisabledGender() {
		return disabledGender;
	}

	public void setDisabledGender(boolean disabledGender) {
		this.disabledGender = disabledGender;
	}

	public boolean isDisabledDOB() {
		return disabledDOB;
	}

	public void setDisabledDOB(boolean disabledDOB) {
		this.disabledDOB = disabledDOB;
	}

	public Boolean getCheckValiationsForInputFields() {
		return checkValiationsForInputFields;
	}

	public void setCheckValiationsForInputFields(Boolean checkValiationsForInputFields) {
		this.checkValiationsForInputFields = checkValiationsForInputFields;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public BusOwnerDTO getPreviousBusOwnerDTO() {
		return previousBusOwnerDTO;
	}

	public void setPreviousBusOwnerDTO(BusOwnerDTO previousBusOwnerDTO) {
		this.previousBusOwnerDTO = previousBusOwnerDTO;
	}

	public PermitRenewalsDTO getPermitRenewalsDTO() {
		return permitRenewalsDTO;
	}

	public void setPermitRenewalsDTO(PermitRenewalsDTO permitRenewalsDTO) {
		this.permitRenewalsDTO = permitRenewalsDTO;
	}

	public boolean isShowThirdForm() {
		return showThirdForm;
	}

	public void setShowThirdForm(boolean showThirdForm) {
		this.showThirdForm = showThirdForm;
	}

	public boolean isShowbacklogvalue12() {
		return showbacklogvalue12;
	}

	public void setShowbacklogvalue12(boolean showbacklogvalue12) {
		this.showbacklogvalue12 = showbacklogvalue12;
	}

	public boolean isShowbacklogvalue() {
		return showbacklogvalue;
	}

	public void setShowbacklogvalue(boolean showbacklogvalue) {
		this.showbacklogvalue = showbacklogvalue;
	}

	public boolean isShowbacklogvalueLoad() {
		return showbacklogvalueLoad;
	}

	public void setShowbacklogvalueLoad(boolean showbacklogvalueLoad) {
		this.showbacklogvalueLoad = showbacklogvalueLoad;
	}

	public boolean isRequestNewPeriodReadOnly() {
		return requestNewPeriodReadOnly;
	}

	public void setRequestNewPeriodReadOnly(boolean requestNewPeriodReadOnly) {
		this.requestNewPeriodReadOnly = requestNewPeriodReadOnly;
	}

	public boolean isDisabledReqPeriodInput() {
		return disabledReqPeriodInput;
	}

	public void setDisabledReqPeriodInput(boolean disabledReqPeriodInput) {
		this.disabledReqPeriodInput = disabledReqPeriodInput;
	}

	public boolean isShowNewPermitDateInput() {
		return showNewPermitDateInput;
	}

	public void setShowNewPermitDateInput(boolean showNewPermitDateInput) {
		this.showNewPermitDateInput = showNewPermitDateInput;
	}

	public boolean isCheckNewExpiryDateBoolean() {
		return checkNewExpiryDateBoolean;
	}

	public void setCheckNewExpiryDateBoolean(boolean checkNewExpiryDateBoolean) {
		this.checkNewExpiryDateBoolean = checkNewExpiryDateBoolean;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public List<CommonDTO> getPermitNoListN() {
		return permitNoListN;
	}

	public void setPermitNoListN(List<CommonDTO> permitNoListN) {
		this.permitNoListN = permitNoListN;
	}

	public Boolean getDisupdateBtn() {
		return disupdateBtn;
	}

	public void setDisupdateBtn(Boolean disupdateBtn) {
		this.disupdateBtn = disupdateBtn;
	}

	public boolean isDisablePrint() {
		return disablePrint;
	}

	public void setDisablePrint(boolean disablePrint) {
		this.disablePrint = disablePrint;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

}
