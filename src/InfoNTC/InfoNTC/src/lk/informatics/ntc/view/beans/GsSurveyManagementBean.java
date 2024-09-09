package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.AdvancedPaymentDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.SurveyDTO;
import lk.informatics.ntc.model.dto.SurveyLocationTeamDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.GamiSariyaService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.model.service.SurveyService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "gsSurveyManagementBean")
@ViewScoped
public class GsSurveyManagementBean {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private SurveyDTO surveyDTO;
	private SurveyLocationTeamDTO surveyLocationTeamDTO;
	private PaymentVoucherDTO paymentVoucherDTO;
	private AdvancedPaymentDTO advancedPaymentDTO = new AdvancedPaymentDTO();

	private GamiSariyaService gamiSariyaService;

	private List<SurveyDTO> drpdSurveyNoList;
	private List<SurveyDTO> drpdLocationList;
	private List<SurveyDTO> tblLocationDet;
	private List<EmployeeDTO> drpdUserIdList;
	private List<SurveyDTO> drpdOrganizationList;
	private List<SurveyLocationTeamDTO> drpdResponsibilitiesList;
	private List<GenerateReceiptDTO> drpdBankList;
	private List<GenerateReceiptDTO> drpdBankBranchList;
	private List<SurveyLocationTeamDTO> drpdTeamDetailsTableList;

	private int activeTabIndex;
	private boolean renderTabView;
	private boolean renderTeamDetails;
	private boolean disableUserId;
	private String errorMsg;
	private String alertMsg;
	private String date1;

	private SurveyService surveyService;

	// tab 02
	private boolean booleandisableCostEstimationSave;
	private boolean disableCostEstimationAdd;
	private boolean approvedEstimation;
	private boolean disableCostEstimationSave;

	private List<SurveyLocationTeamDTO> drpdCostCodeList;
	private List<SurveyLocationTeamDTO> tblCostEstimateList;

	// tab 03
	private boolean renderGrantApproval;

	// tab 04
	private boolean renderPaymentVoucher;
	private boolean generatebtn;
	private boolean printbtn;

	private List<CommonDTO> drpdTransactionTypeList;
	private List<CommonDTO> drpdUnitList;
	private List<CommonDTO> drpdDeparmentList;

	private AdvancedPaymentDTO dto;
	private List<AdvancedPaymentDTO> paymentDetails;
	private BigDecimal totalfee;
	private String value;

	public PaymentVoucherService paymentVoucherService;
	public EmployeeProfileService employeeProfileService;
	public CommonService commonService;
	private StreamedContent files;
	private boolean disable;

	@PostConstruct
	public void init() {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date1 = dateFormat.format(date);

		surveyDTO = new SurveyDTO();
		surveyLocationTeamDTO = new SurveyLocationTeamDTO();

		drpdSurveyNoList = new ArrayList<SurveyDTO>();
		drpdLocationList = new ArrayList<SurveyDTO>();
		tblLocationDet = new ArrayList<SurveyDTO>();
		drpdUserIdList = new ArrayList<EmployeeDTO>();
		drpdOrganizationList = new ArrayList<SurveyDTO>();
		drpdResponsibilitiesList = new ArrayList<SurveyLocationTeamDTO>();
		drpdBankList = new ArrayList<GenerateReceiptDTO>();
		drpdBankBranchList = new ArrayList<GenerateReceiptDTO>();
		drpdTeamDetailsTableList = new ArrayList<SurveyLocationTeamDTO>();
		drpdCostCodeList = new ArrayList<SurveyLocationTeamDTO>();

		tblCostEstimateList = new ArrayList<SurveyLocationTeamDTO>();
		drpdTransactionTypeList = new ArrayList<CommonDTO>();
		drpdUnitList = new ArrayList<CommonDTO>();
		drpdDeparmentList = new ArrayList<CommonDTO>();

		paymentVoucherDTO = new PaymentVoucherDTO();
		paymentDetails = new ArrayList<AdvancedPaymentDTO>();
		loadValues();

	}

	public void loadValues() {

		gamiSariyaService = (GamiSariyaService) SpringApplicationContex.getBean("gamiSariyaService");
		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");

		drpdSurveyNoList = gamiSariyaService.drpdSurveyNoListForSurveyManagement();
		drpdLocationList = surveyService.getLocationDropDown();

		drpdOrganizationList = surveyService.getOrganizationListDropDown();
		drpdUserIdList = surveyService.getUserIdDropDown();
		drpdResponsibilitiesList = surveyService.getResponsibilitiesList();
		drpdBankList = surveyService.getBankListDropDown();

		// tab 02
		drpdCostCodeList = surveyService.getCostCodeDropDownForGSSurveyManagement();

		// tab 04
		drpdDeparmentList = employeeProfileService.GetDepartmentsToDropdown();
		drpdUnitList = employeeProfileService.GetUnitsToDropdown();
		totalfee = new BigDecimal(0);
	}

	public void onCostTypeChange(String costCode) {
		surveyLocationTeamDTO
				.setCostCodeDescription(surveyService.getCodeDescription(surveyLocationTeamDTO.getSelectedCostCode()));
	}

	public void onSurveyNoChange() {

		approvedEstimation = true;

		String temptSurveyNo = surveyDTO.getSurveyNo();
		surveyDTO = new SurveyDTO();
		tblLocationDet = new ArrayList<SurveyDTO>();
		drpdTeamDetailsTableList = new ArrayList<SurveyLocationTeamDTO>();
		surveyDTO.setSurveyNo(temptSurveyNo);
		surveyDTO = gamiSariyaService.getSurveyNoDet(surveyDTO);
		surveyLocationTeamDTO.setSpecialRemark(surveyDTO.getRemarks());

		if (surveyDTO.getStatus() != null && (surveyDTO.getStatus().equals("A") || surveyDTO.getStatus().equals("R"))) {
			disableCostEstimationAdd = true;
			disableCostEstimationSave = true;
			approvedEstimation = true;
		} else if (surveyDTO.getStatus() != null && surveyDTO.getStatus().equals("S")) {

			approvedEstimation = false;
			disableCostEstimationAdd = true;
			disableCostEstimationSave = true;

		} else {
			disableCostEstimationAdd = false;
			disableCostEstimationSave = false;
		}

		tblCostEstimateList = new ArrayList<SurveyLocationTeamDTO>();
		renderTabView = false;
	}

	public void btnSearch() {

		if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().isEmpty()
				&& !surveyDTO.getSurveyNo().equalsIgnoreCase("")) {

			paymentVoucherDTO = new PaymentVoucherDTO();
			advancedPaymentDTO = new AdvancedPaymentDTO();
			paymentDetails = new ArrayList<AdvancedPaymentDTO>();
			dto = new AdvancedPaymentDTO();
			totalfee = new BigDecimal(0);
			generatebtn = false;

			activeTabIndex = 0;
			renderTabView = true;
			tblLocationDet = surveyService.getLocationTimeTable(surveyDTO.getSurveyNo());

			retriveCostEstimationTable();
			paymentVoucherDTO.setTransactionCode("19");

		} else {
			errorMsg = "Survey No should be selected.";
			RequestContext.getCurrentInstance().update("frm-serviceInformation");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void btnClear() {

		surveyDTO = new SurveyDTO();
		surveyLocationTeamDTO = new SurveyLocationTeamDTO();

		tblCostEstimateList = new ArrayList<SurveyLocationTeamDTO>();
		tblLocationDet = surveyService.getLocationTimeTable(surveyDTO.getSurveyNo());

		drpdTeamDetailsTableList = new ArrayList<SurveyLocationTeamDTO>();

	}

	public void btnTab01AddLocationDet() {

		errorMsg = "";

		if (surveyDTO.getLocation() != null && !surveyDTO.getLocation().isEmpty()
				&& !surveyDTO.getLocation().equalsIgnoreCase("")) {
			if (surveyDTO.getStartDate() != null && !surveyDTO.getStartDate().toString().isEmpty()
					&& !surveyDTO.getStartDate().toString().equalsIgnoreCase("")) {
				if (surveyDTO.getEndDate() != null && !surveyDTO.getEndDate().toString().isEmpty()
						&& !surveyDTO.getEndDate().toString().equalsIgnoreCase("")) {
					if (surveyDTO.getStartTime() != null && !surveyDTO.getStartTime().toString().isEmpty()
							&& !surveyDTO.getStartTime().toString().equalsIgnoreCase("")) {
						if (surveyDTO.getEndTime() != null && !surveyDTO.getEndTime().toString().isEmpty()
								&& !surveyDTO.getEndTime().toString().equalsIgnoreCase("")) {

							if (surveyDTO.getEndDate().equals(surveyDTO.getStartDate())) {

								if (surveyDTO.getEndTime().after(surveyDTO.getStartTime())) {

									surveyDTO.setLoginUser(sessionBackingBean.getLoginUser());
									surveyService.addLoactionTime(surveyDTO);
									tblLocationDet = surveyService.getLocationTimeTable(surveyDTO.getSurveyNo());

									surveyDTO.setLocation(null);
									surveyDTO.setStartDate(null);
									surveyDTO.setEndDate(null);
									surveyDTO.setStartTime(null);
									surveyDTO.setEndTime(null);

									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								} else {
									surveyDTO.setEndTime(null);
									errorMsg = "End Time should be equal or after Start Time.";
									RequestContext.getCurrentInstance().update("frm_Tender");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else if (surveyDTO.getEndDate().after(surveyDTO.getStartDate())) {

								surveyDTO.setLoginUser(sessionBackingBean.getLoginUser());
								surveyService.addLoactionTime(surveyDTO);
								tblLocationDet = surveyService.getLocationTimeTable(surveyDTO.getSurveyNo());

								surveyDTO.setLocation(null);
								surveyDTO.setStartDate(null);
								surveyDTO.setEndDate(null);
								surveyDTO.setStartTime(null);
								surveyDTO.setEndTime(null);

								RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

							} else {

								surveyDTO.setEndDate(null);
								errorMsg = "End Date should be equal or after Start Date.";
								RequestContext.getCurrentInstance().update("frm_Tender");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

							}

						} else {
							errorMsg = "End Time should be entered.";
							RequestContext.getCurrentInstance().update("frm_Tender");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
					} else {
						errorMsg = "Start Time should be entered.";
						RequestContext.getCurrentInstance().update("frm_Tender");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					errorMsg = "End Date should be entered.";
					RequestContext.getCurrentInstance().update("frm_Tender");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				errorMsg = "Start Date should be entered.";
				RequestContext.getCurrentInstance().update("frm_Tender");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Location should be entered.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	public void onLocationRowSelect(SelectEvent event) {
		String locationSeqNo = ((SurveyDTO) event.getObject()).getLocationSeqNo().toString();

		surveyDTO.setLocationSeqNo(locationSeqNo);
		renderTeamDetails = true;
		drpdTeamDetailsTableList = surveyService.getTeamDetailsTableList(surveyDTO);
	}

	public void onLocationRowUnSelect() {

	}

	public void onOrganizationChange() {
		surveyLocationTeamDTO.setUserId(null);
		surveyLocationTeamDTO.setFullName(null);

		if (surveyLocationTeamDTO.getOrganization().equals("ORG01")) {
			setDisableUserId(false);
		} else {
			setDisableUserId(true);
			surveyLocationTeamDTO.setFullName(null);
		}

	}

	public void delete_tbl_LocationTimeRaw(String locationSeqNo) {

		surveyService.deleteRawFromLocationTable(locationSeqNo);
		deleteTeamDetailsByLocation(locationSeqNo);
		retriveLocationTimeTable();
		retriveTeamDetailsTable();
		RequestContext.getCurrentInstance().execute("PF('deleteMessage').show()");

	}

	public void deleteTeamDetailsByLocation(String locationSeqNo) {
		surveyService.deleteTeamDetailsByLocation(locationSeqNo);
		RequestContext.getCurrentInstance().execute("PF('deleteMessage').show()");
	}

	public void retriveLocationTimeTable() {
		tblLocationDet = surveyService.getLocationTimeTable(surveyDTO.getSurveyNo());
	}

	public void onUserIdChange() {
		surveyLocationTeamDTO.setFullName(surveyService.returnName(surveyLocationTeamDTO.getUserId()));
		surveyLocationTeamDTO.setNic(surveyService.returnNICNo(surveyLocationTeamDTO.getUserId()));
	}

	public void btnAddLocationTime() {

	}

	public void onBankChange() {
		drpdBankBranchList = surveyService.getBankBranchDropDown(surveyLocationTeamDTO.getBank());
	}

	public void btnAddTeamByLocation() {
		if (surveyDTO.getLocationSeqNo() != null && !surveyDTO.getLocationSeqNo().isEmpty()
				&& !surveyDTO.getLocationSeqNo().equalsIgnoreCase("")) {

			if (surveyLocationTeamDTO.getOrganization() != null && !surveyLocationTeamDTO.getOrganization().isEmpty()
					&& !surveyLocationTeamDTO.getOrganization().equalsIgnoreCase("")) {

				// not ntc
				// dosent want to check user id
				if (disableUserId) {
					validateAddTeamByLocation();
					if (!commonService.checkTaskOnSurveyHisDetails(surveyDTO.getSurveyNo(), "SU004", "C")) {
						commonService.updateSurveyTaskDetailsBySurveyNo(surveyDTO.getRequestNo(),
								surveyDTO.getSurveyNo(), "SU005", "O", sessionBackingBean.getLoginUser());
					}
					// organization ntc
					// check user id
				} else {
					if (surveyLocationTeamDTO.getUserId() != null && !surveyLocationTeamDTO.getUserId().isEmpty()
							&& !surveyLocationTeamDTO.getUserId().equalsIgnoreCase("")) {

						validateAddTeamByLocation();
						if (!commonService.checkTaskOnSurveyHisDetails(surveyDTO.getSurveyNo(), "SU004", "C")) {
							commonService.updateSurveyTaskDetailsBySurveyNo(surveyDTO.getRequestNo(),
									surveyDTO.getSurveyNo(), "SU005", "O", sessionBackingBean.getLoginUser());
						}

					} else {
						errorMsg = "User Id should be entered.";
						RequestContext.getCurrentInstance().update("frm_Tender");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				}

			} else {
				errorMsg = "Organization should be entered.";
				RequestContext.getCurrentInstance().update("frm_Tender");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Location should be Selected.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void validateAddTeamByLocation() {

		if (surveyLocationTeamDTO.getFullName() != null && !surveyLocationTeamDTO.getFullName().isEmpty()
				&& !surveyLocationTeamDTO.getFullName().equalsIgnoreCase("")) {

			if (surveyLocationTeamDTO.getNic() != null && !surveyLocationTeamDTO.getNic().isEmpty()
					&& !surveyLocationTeamDTO.getNic().equalsIgnoreCase("")) {

				if (surveyLocationTeamDTO.getResponsibilities() != null
						&& !surveyLocationTeamDTO.getResponsibilities().isEmpty()
						&& !surveyLocationTeamDTO.getNic().equalsIgnoreCase("")) {

					surveyDTO.setLoginUser(sessionBackingBean.loginUser);
					surveyService.addTeamByLocation(surveyDTO, surveyLocationTeamDTO);
					retriveTeamDetailsTable();

					surveyLocationTeamDTO.setUserId(null);
					surveyLocationTeamDTO.setOrganization(null);
					surveyLocationTeamDTO.setFullName(null);
					surveyLocationTeamDTO.setNic(null);
					surveyLocationTeamDTO.setResponsibilities(null);
					surveyLocationTeamDTO.setBank(null);
					surveyLocationTeamDTO.setBranch(null);
					surveyLocationTeamDTO.setAccountNo(null);
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

				} else {
					errorMsg = "Responsibilities should be entered.";
					RequestContext.getCurrentInstance().update("frm_Tender");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "NIC should be entered.";
				RequestContext.getCurrentInstance().update("frm_Tender");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Full Name should be entered.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void retriveTeamDetailsTable() {
		drpdTeamDetailsTableList = surveyService.getTeamDetailsTableList(surveyDTO);
	}

	public void delete_tbl_TeamDetialsRaw(String memberSeqNo) {

		surveyService.deleteRawFromTeamDetailsTable(memberSeqNo);
		retriveTeamDetailsTable();
		RequestContext.getCurrentInstance().execute("PF('deleteMessage').show()");
	}

	// tab 02

	public void addCostEstimation() {

		if (surveyLocationTeamDTO.getSelectedCostCode() != null
				&& !surveyLocationTeamDTO.getSelectedCostCode().isEmpty()
				&& !surveyLocationTeamDTO.getSelectedCostCode().equalsIgnoreCase("")) {
			if (surveyLocationTeamDTO.getCostCodeDescription() != null
					&& !surveyLocationTeamDTO.getCostCodeDescription().isEmpty()
					&& !surveyLocationTeamDTO.getCostCodeDescription().equalsIgnoreCase("")) {
				if (surveyLocationTeamDTO.getAmount() != null) {

					if (validateCostCode()) {

						errorMsg = "Amount allready added to related code.";
						RequestContext.getCurrentInstance().update("frm_Tender");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					} else {
						surveyDTO.setLoginUser(sessionBackingBean.loginUser);
						surveyService.addCostEstimation(surveyLocationTeamDTO, surveyDTO);
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
						retriveCostEstimationTable();
						disableCostEstimationSave = false;

					}
					surveyLocationTeamDTO.setSelectedCostCode(null);
					surveyLocationTeamDTO.setCostCodeDescription(null);
					surveyLocationTeamDTO.setAmount(null);

				} else {
					errorMsg = "Amount shoud be entered.";
					RequestContext.getCurrentInstance().update("frm_Tender");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Description not found.";
				RequestContext.getCurrentInstance().update("frm_Tender");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Code should be selected.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public boolean validateCostCode() {
		boolean costCode = false;

		for (SurveyLocationTeamDTO costEstimateTableList : tblCostEstimateList) {
			if (costEstimateTableList.getCostCode() != null
					&& costEstimateTableList.getCostCode().equals(surveyLocationTeamDTO.getSelectedCostCode())) {
				costCode = true;
			}
		}
		return costCode;
	}

	public void delete_tbl_CostEstimateRaw(String seqNO) {

		surveyService.deleteRawFromCostEstimateTable(seqNO);
		RequestContext.getCurrentInstance().execute("PF('deleteMessage').show()");
		retriveCostEstimationTable();

		if (tblCostEstimateList.size() > 0) {
			approvedEstimation = false;
		} else {
			approvedEstimation = true;
		}
	}

	public void retriveCostEstimationTable() {

		tblCostEstimateList = surveyService.getCostEstimationTableList(surveyDTO);
		surveyLocationTeamDTO.setTotalAmount(getTotalCostEstimate());
		advancedPaymentDTO.setAmount(surveyLocationTeamDTO.getTotalAmount());
	}

	public BigDecimal getTotalCostEstimate() {
		BigDecimal total = new BigDecimal(0);
		BigDecimal amt = null;

		for (SurveyLocationTeamDTO surveyLocationTeamDTO : tblCostEstimateList) {
			amt = surveyLocationTeamDTO.getAmount();
			total = total.add(amt);
		}

		return total;
	}

	public void updateCostEstimateTask() {

		if (gamiSariyaService.updateApproveCostEstimateStatus("S", surveyDTO.getSurveyNo())) {
			approvedEstimation = false;
			disableCostEstimationAdd = true;
			disableCostEstimationSave = true;
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			if (!commonService.checkTaskOnSurveyHisDetails(surveyDTO.getSurveyNo(), "SU004", "C")) {
				commonService.updateSurveyTaskDetailsBySurveyNo(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(),
						"SU005", "O", sessionBackingBean.getLoginUser());
			}
		}
	}

	public void btnTab02ApproveCostEstimation() {

		if (gamiSariyaService.approveRejectCostEstimation(surveyDTO.getSurveyNo(), "A", sessionBackingBean.loginUser,
				surveyLocationTeamDTO)) {

			advancedPaymentDTO.setAmount(surveyLocationTeamDTO.getTotalAmount());
			disableCostEstimationAdd = true;
			disableCostEstimationSave = true;
			approvedEstimation = true;

			surveyLocationTeamDTO.setTotalAmount(getTotalCostEstimate());
			advancedPaymentDTO.setAmount(surveyLocationTeamDTO.getTotalAmount());
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {

			disableCostEstimationAdd = false;
			disableCostEstimationSave = false;
			approvedEstimation = false;
		}

	}

	public void btnTab02RejectCostEstimation() {
		if (gamiSariyaService.approveRejectCostEstimation(surveyDTO.getSurveyNo(), "R", sessionBackingBean.loginUser,
				surveyLocationTeamDTO)) {

			advancedPaymentDTO.setAmount(surveyLocationTeamDTO.getTotalAmount());
			disableCostEstimationAdd = true;
			disableCostEstimationSave = true;
			approvedEstimation = true;

			surveyLocationTeamDTO.setTotalAmount(getTotalCostEstimate());
			advancedPaymentDTO.setAmount(surveyLocationTeamDTO.getTotalAmount());

			if (!commonService.checkTaskOnSurveyHisDetails(surveyDTO.getSurveyNo(), "SU005", "O")) {
				commonService.updateSurveyTaskDetailsBySurveyNo(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(),
						"SU005", "C", sessionBackingBean.getLoginUser());
			}
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

		} else {

			disableCostEstimationAdd = false;
			disableCostEstimationSave = false;
			approvedEstimation = false;
		}
	}

	public void noOfUnitValidation() {

		if (advancedPaymentDTO.getNoofUnits() != null && advancedPaymentDTO.getNoofUnits().signum() < 0) {
			errorMsg = "Invalid No.of Units/Rate";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
			advancedPaymentDTO.setNoofUnits(null);
		}

	}

	public void totalBusFareValidation() {

		if (advancedPaymentDTO.getAmount() != null && advancedPaymentDTO.getAmount().signum() < 0) {
			errorMsg = "Invalid Amount";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
			advancedPaymentDTO.setAmount(null);
		}

	}

	public void addNewChargeDetails() {

		if (paymentVoucherDTO.getTransactionCode() != null && !paymentVoucherDTO.getTransactionCode().isEmpty()) {
			if (paymentVoucherDTO.getDepartmentCode() != null && !paymentVoucherDTO.getDepartmentCode().isEmpty()) {
				if (paymentVoucherDTO.getUnitCode() != null && !paymentVoucherDTO.getUnitCode().isEmpty()) {
					if (advancedPaymentDTO.getServiceDetails() != null
							&& !advancedPaymentDTO.getServiceDetails().isEmpty()) {
						if (advancedPaymentDTO.getNoofUnits() != null) {
							if (advancedPaymentDTO.getAmount() != null) {

								dto = new AdvancedPaymentDTO(advancedPaymentDTO.getServiceDetails(),
										advancedPaymentDTO.getNoofUnits(), advancedPaymentDTO.getAmount(), date1);

								if (paymentDetails == null) {
									paymentDetails = new ArrayList<AdvancedPaymentDTO>();
									paymentDetails.add(dto);
									advancedPaymentDTO = new AdvancedPaymentDTO();
									BigDecimal d = new BigDecimal(0);
									BigDecimal amt = new BigDecimal(0);
									amt = dto.getAmount();
									totalfee = d.add(amt);

									clearEnteredVal();
								} else {
									boolean isfound = false;

									for (int i = 0; i < paymentDetails.size(); i++) {

										if (paymentDetails.get(i).getServiceDetails().equals(dto.getServiceDetails())) {
											isfound = true;
											break;
										} else {
											isfound = false;
										}

									}
									if (isfound == false) {
										paymentDetails.add(dto);
										advancedPaymentDTO = new AdvancedPaymentDTO();
										BigDecimal amt = null;
										amt = dto.getAmount();
										totalfee = totalfee.add(amt);

									} else {
										errorMsg = "Duplicate Details of Service or Items Provided.";
										sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
										return;
									}
								}
							} else {
								errorMsg = "Amounts should be entered.";
								sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
								return;
							}
						} else {
							errorMsg = "No. of Units should be entered.";
							sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
							return;
						}
					} else {
						errorMsg = "Details of Service or Items Provided should be entered.";
						sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
						return;
					}
				}

				else {
					errorMsg = "Unit should be selected.";
					sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
					return;
				}
			} else {
				errorMsg = "Department should be selected.";
				sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
				return;
			}
		} else {
			errorMsg = "Transaction Type should be selected.";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");

			return;
		}

	}

	public void clearEnteredVal() {
		advancedPaymentDTO = new AdvancedPaymentDTO();
	}

	public void delete(String serviceDescription) {

		for (int i = 0; i < paymentDetails.size(); i++) {

			if (paymentDetails.get(i).getServiceDetails().equals(serviceDescription)) {
				BigDecimal amt = paymentDetails.get(i).getAmount();
				amt = paymentDetails.get(i).getAmount();
				totalfee = totalfee.subtract(amt);

				setTotalfee(totalfee);
				paymentDetails.remove((i));

			}
		}

	}

	public void generateVoucher() {
		PaymentVoucherDTO rptDTO = new PaymentVoucherDTO();
		if (totalfee != null && totalfee.compareTo(BigDecimal.ZERO) != 0) {
			activeTabIndex = 3;
			String logInUser = sessionBackingBean.getLoginUser();
			paymentVoucherDTO.setApplicationNo(surveyDTO.getSurveyNo());
			paymentVoucherDTO.setPermitNo("");

			int result = paymentVoucherService.insertPaymentVoucher(paymentVoucherDTO, logInUser, totalfee,
					paymentDetails);
			rptDTO.setVoucherNo(paymentVoucherDTO.getVoucherNo());

			if (!commonService.checkTaskOnSurveyHisDetails(surveyDTO.getSurveyNo(), "SU005", "O")) {
				commonService.updateSurveyTaskDetailsBySurveyNo(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(),
						"SU005", "C", sessionBackingBean.getLoginUser());
			}

			if (result == 0) {
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
				setDisable(true);
				setPrintbtn(false);
				setGeneratebtn(true);
			}

		} else {
			errorMsg = "Insert Payment details to continue.";
			sessionBackingBean.showMessage("Error", errorMsg, "ERROR_DIALOG");
		}

	}

	public StreamedContent printVoucher() throws JRException {

		value = paymentVoucherDTO.getVoucherNo();
		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports// AdvancepaymentConfirmationReceiptNew.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Voucher_No", value);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "PaymentVoucher.pdf");

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

	public void clearMain() {
		advancedPaymentDTO = new AdvancedPaymentDTO();
		paymentVoucherDTO = new PaymentVoucherDTO();
		paymentVoucherDTO.setTransactionCode("19");
		paymentDetails = null;
		setDisable(false);
		totalfee = null;
		setPrintbtn(true);
		setGeneratebtn(true);
	}
	
	public void loadLocations(TabChangeEvent event) {
		if (activeTabIndex == 0) {
			drpdLocationList = new ArrayList<SurveyDTO>();
			drpdLocationList = surveyService.getLocationDropDown();
		}
	}

	// getters and setters

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public SurveyDTO getSurveyDTO() {
		return surveyDTO;
	}

	public void setSurveyDTO(SurveyDTO surveyDTO) {
		this.surveyDTO = surveyDTO;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public SurveyLocationTeamDTO getSurveyLocationTeamDTO() {
		return surveyLocationTeamDTO;
	}

	public void setSurveyLocationTeamDTO(SurveyLocationTeamDTO surveyLocationTeamDTO) {
		this.surveyLocationTeamDTO = surveyLocationTeamDTO;
	}

	public List<SurveyDTO> getDrpdSurveyNoList() {
		return drpdSurveyNoList;
	}

	public void setDrpdSurveyNoList(List<SurveyDTO> drpdSurveyNoList) {
		this.drpdSurveyNoList = drpdSurveyNoList;
	}

	public List<SurveyDTO> getDrpdLocationList() {
		return drpdLocationList;
	}

	public void setDrpdLocationList(List<SurveyDTO> drpdLocationList) {
		this.drpdLocationList = drpdLocationList;
	}

	public List<SurveyDTO> getTblLocationDet() {
		return tblLocationDet;
	}

	public void setTblLocationDet(List<SurveyDTO> tblLocationDet) {
		this.tblLocationDet = tblLocationDet;
	}

	public List<SurveyDTO> getDrpdOrganizationList() {
		return drpdOrganizationList;
	}

	public void setDrpdOrganizationList(List<SurveyDTO> drpdOrganizationList) {
		this.drpdOrganizationList = drpdOrganizationList;
	}

	public List<SurveyLocationTeamDTO> getDrpdResponsibilitiesList() {
		return drpdResponsibilitiesList;
	}

	public void setDrpdResponsibilitiesList(List<SurveyLocationTeamDTO> drpdResponsibilitiesList) {
		this.drpdResponsibilitiesList = drpdResponsibilitiesList;
	}

	public List<GenerateReceiptDTO> getDrpdBankList() {
		return drpdBankList;
	}

	public void setDrpdBankList(List<GenerateReceiptDTO> drpdBankList) {
		this.drpdBankList = drpdBankList;
	}

	public List<GenerateReceiptDTO> getDrpdBankBranchList() {
		return drpdBankBranchList;
	}

	public void setDrpdBankBranchList(List<GenerateReceiptDTO> drpdBankBranchList) {
		this.drpdBankBranchList = drpdBankBranchList;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public boolean isRenderTabView() {
		return renderTabView;
	}

	public void setRenderTabView(boolean renderTabView) {
		this.renderTabView = renderTabView;
	}

	public boolean isRenderTeamDetails() {
		return renderTeamDetails;
	}

	public void setRenderTeamDetails(boolean renderTeamDetails) {
		this.renderTeamDetails = renderTeamDetails;
	}

	public boolean isDisableUserId() {
		return disableUserId;
	}

	public void setDisableUserId(boolean disableUserId) {
		this.disableUserId = disableUserId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public GamiSariyaService getGamiSariyaService() {
		return gamiSariyaService;
	}

	public void setGamiSariyaService(GamiSariyaService gamiSariyaService) {
		this.gamiSariyaService = gamiSariyaService;
	}

	public List<EmployeeDTO> getDrpdUserIdList() {
		return drpdUserIdList;
	}

	public void setDrpdUserIdList(List<EmployeeDTO> drpdUserIdList) {
		this.drpdUserIdList = drpdUserIdList;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public List<SurveyLocationTeamDTO> getDrpdTeamDetailsTableList() {
		return drpdTeamDetailsTableList;
	}

	public void setDrpdTeamDetailsTableList(List<SurveyLocationTeamDTO> drpdTeamDetailsTableList) {
		this.drpdTeamDetailsTableList = drpdTeamDetailsTableList;
	}

	public boolean isBooleandisableCostEstimationSave() {
		return booleandisableCostEstimationSave;
	}

	public void setBooleandisableCostEstimationSave(boolean booleandisableCostEstimationSave) {
		this.booleandisableCostEstimationSave = booleandisableCostEstimationSave;
	}

	public boolean isDisableCostEstimationAdd() {
		return disableCostEstimationAdd;
	}

	public void setDisableCostEstimationAdd(boolean disableCostEstimationAdd) {
		this.disableCostEstimationAdd = disableCostEstimationAdd;
	}

	public boolean isApprovedEstimation() {
		return approvedEstimation;
	}

	public void setApprovedEstimation(boolean approvedEstimation) {
		this.approvedEstimation = approvedEstimation;
	}

	public boolean isDisableCostEstimationSave() {
		return disableCostEstimationSave;
	}

	public void setDisableCostEstimationSave(boolean disableCostEstimationSave) {
		this.disableCostEstimationSave = disableCostEstimationSave;
	}

	public List<SurveyLocationTeamDTO> getDrpdCostCodeList() {
		return drpdCostCodeList;
	}

	public void setDrpdCostCodeList(List<SurveyLocationTeamDTO> drpdCostCodeList) {
		this.drpdCostCodeList = drpdCostCodeList;
	}

	public List<SurveyLocationTeamDTO> getTblCostEstimateList() {
		return tblCostEstimateList;
	}

	public void setTblCostEstimateList(List<SurveyLocationTeamDTO> tblCostEstimateList) {
		this.tblCostEstimateList = tblCostEstimateList;
	}

	public boolean isRenderGrantApproval() {
		return renderGrantApproval;
	}

	public void setRenderGrantApproval(boolean renderGrantApproval) {
		this.renderGrantApproval = renderGrantApproval;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public boolean isRenderPaymentVoucher() {
		return renderPaymentVoucher;
	}

	public void setRenderPaymentVoucher(boolean renderPaymentVoucher) {
		this.renderPaymentVoucher = renderPaymentVoucher;
	}

	public List<CommonDTO> getDrpdTransactionTypeList() {
		return drpdTransactionTypeList;
	}

	public void setDrpdTransactionTypeList(List<CommonDTO> drpdTransactionTypeList) {
		this.drpdTransactionTypeList = drpdTransactionTypeList;
	}

	public List<CommonDTO> getDrpdUnitList() {
		return drpdUnitList;
	}

	public void setDrpdUnitList(List<CommonDTO> drpdUnitList) {
		this.drpdUnitList = drpdUnitList;
	}

	public List<CommonDTO> getDrpdDeparmentList() {
		return drpdDeparmentList;
	}

	public void setDrpdDeparmentList(List<CommonDTO> drpdDeparmentList) {
		this.drpdDeparmentList = drpdDeparmentList;
	}

	public AdvancedPaymentDTO getAdvancedPaymentDTO() {
		return advancedPaymentDTO;
	}

	public void setAdvancedPaymentDTO(AdvancedPaymentDTO advancedPaymentDTO) {
		this.advancedPaymentDTO = advancedPaymentDTO;
	}

	public boolean isGeneratebtn() {
		return generatebtn;
	}

	public void setGeneratebtn(boolean generatebtn) {
		this.generatebtn = generatebtn;
	}

	public AdvancedPaymentDTO getDto() {
		return dto;
	}

	public void setDto(AdvancedPaymentDTO dto) {
		this.dto = dto;
	}

	public List<AdvancedPaymentDTO> getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(List<AdvancedPaymentDTO> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public BigDecimal getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(BigDecimal totalfee) {
		this.totalfee = totalfee;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public boolean isPrintbtn() {
		return printbtn;
	}

	public void setPrintbtn(boolean printbtn) {
		this.printbtn = printbtn;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

}
