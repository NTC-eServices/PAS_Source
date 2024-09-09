package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
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

@ManagedBean(name = "surveyManageBackingBean")
@ViewScoped
public class SurveyLocationBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<SurveyDTO> surveyNoList;
	private List<SurveyDTO> locationList;
	private List<SurveyDTO> locationAndTimeTableList;
	private List<EmployeeDTO> userIdList;
	private List<SurveyDTO> organizationList;
	private List<GenerateReceiptDTO> bankList;
	private List<SurveyLocationTeamDTO> responsibilitiesList;
	private List<GenerateReceiptDTO> bankBranchList;
	private List<SurveyLocationTeamDTO> teamDetailsTableList;
	private List<String> costCodeList;
	private List<SurveyLocationTeamDTO> costEstimateTableList;

	private SurveyDTO surveyDTO;
	private SurveyLocationTeamDTO surveyLocationTeamDTO;

	private PaymentVoucherDTO paymentVoucherDTO = new PaymentVoucherDTO();
	private AdvancedPaymentDTO advancedPaymentDTO = new AdvancedPaymentDTO();

	// Advanced payment dto
	private AdvancedPaymentDTO dto;
	private List<AdvancedPaymentDTO> paymentDetails;
	private BigDecimal totalfee;
	private boolean generatebtn;
	private boolean disable;
	private boolean printbtn;
	private boolean approvedEstimation;
	private String value;
	private StreamedContent files;

	private List<CommonDTO> transactionTypeList;
	private List<CommonDTO> deparmentList;
	private List<CommonDTO> unitList;

	private SurveyService surveyService;
	public PaymentVoucherService paymentVoucherService;
	public EmployeeProfileService employeeProfileService;
	public CommonService commonService;

	private String errorMsg;
	private String alertMsg;
	private String date1;
	private int activeTabIndex;
	private boolean renderTabView;
	private boolean renderTeamDetails;
	private boolean renderGrantApproval;
	private boolean renderPaymentVoucher;
	private boolean disableCostEstimationSave;
	private boolean disableCostEstimationAdd;
	private boolean disableUserId;

	@PostConstruct
	public void init() {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date1 = dateFormat.format(date);
		disable = false;
		generatebtn = false;
		printbtn = false;
		renderTabView = false;
		renderTeamDetails = false;
		renderPaymentVoucher = false;
		disableCostEstimationSave = true;
		disableCostEstimationAdd = false;
		setGeneratebtn(true);

		surveyDTO = new SurveyDTO();
		surveyLocationTeamDTO = new SurveyLocationTeamDTO();
		locationAndTimeTableList = new ArrayList<SurveyDTO>();
		organizationList = new ArrayList<SurveyDTO>();
		bankList = new ArrayList<GenerateReceiptDTO>();
		responsibilitiesList = new ArrayList<SurveyLocationTeamDTO>();
		bankBranchList = new ArrayList<GenerateReceiptDTO>();
		costEstimateTableList = new ArrayList<SurveyLocationTeamDTO>();
		teamDetailsTableList = new ArrayList<SurveyLocationTeamDTO>();
		loadValues();

		retriveCostEstimationTable();

	}

	private void loadValues() {

		surveyService = (SurveyService) SpringApplicationContex.getBean("surveyService");
		surveyNoList = surveyService.getApprovedSurveyNoDropDown();
		locationList = surveyService.getLocationDropDown();
		userIdList = surveyService.getUserIdDropDown();
		organizationList = surveyService.getOrganizationListDropDown();
		bankList = surveyService.getBankListDropDown();
		responsibilitiesList = surveyService.getResponsibilitiesList();

		costCodeList = surveyService.getCostCodeDropDown();

		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		setTransactionTypeList(paymentVoucherService.GetTransactionToDropdown());

		deparmentList = employeeProfileService.GetDepartmentsToDropdown();
		unitList = employeeProfileService.GetUnitsToDropdown();

	}

	/**
	 * This method check for current Survey No and set related to Survey Type and
	 * Survey Method
	 */
	public void onSurveyNoChange() {
		clearMain();
		activeTabIndex = 0;
		renderPaymentVoucher = false;
		renderTabView = false;

		if (surveyDTO.getSurveyNo() != null) {
			surveyDTO = surveyService.getDetailsByServeyNo(surveyDTO.getSurveyNo());
			surveyLocationTeamDTO.setSpecialRemark(surveyDTO.getRemarks());
			surveyDTO.setRemarks(null);

			retriveLocationTimeTable();
		} else {
			surveyDTO = new SurveyDTO();
		}
		retriveCostEstimationTable();

		boolean tabThreeCompleted = surveyService.getCostApprovedStatus(surveyDTO.getSurveyNo(), "Y");

		if (tabThreeCompleted) {
			approvedEstimation = true;

			if (renderGrantApproval) {
				activeTabIndex = 2;
			} else {
				activeTabIndex = 3;
			}

		} else if (teamDetailsTableList.size() > 0) {
			activeTabIndex = 1;

			if (costEstimateTableList.size() > 0) {
				approvedEstimation = false;
			} else {
				approvedEstimation = true;
			}

		} else {
			if (costEstimateTableList.size() > 0) {
				approvedEstimation = false;
			} else {
				approvedEstimation = true;
			}
			activeTabIndex = 0;
		}

		if (surveyService.getCostEstimationApproveStatus(surveyDTO.getSurveyNo()).equals("A")
				|| surveyService.getCostEstimationApproveStatus(surveyDTO.getSurveyNo()).equals("R")) {
			renderPaymentVoucher = true;
			disableCostEstimationAdd = true;
			disableCostEstimationSave = true;
			approvedEstimation = true;
		}

	}

	public void searchApplication() {

		renderGrantApproval = surveyService.getSurManApprovalStatus(sessionBackingBean.getLoginUser());

		if (surveyDTO.getSurveyNo() != null && !surveyDTO.getSurveyNo().isEmpty()
				&& !surveyDTO.getSurveyNo().equalsIgnoreCase("")) {
			if (surveyDTO.getSurveyType() != null && !surveyDTO.getSurveyType().isEmpty()
					&& !surveyDTO.getSurveyType().equalsIgnoreCase("")) {
				if (surveyDTO.getSurveyMethod() != null && !surveyDTO.getSurveyMethod().isEmpty()
						&& !surveyDTO.getSurveyMethod().equalsIgnoreCase("")) {

					renderTabView = true;
					paymentVoucherDTO.setTransactionCode("08");
					paymentVoucherDTO.setAccountNO(surveyDTO.getSurveyNo());
					clearMain();

					advancedPaymentDTO.setAmount(surveyLocationTeamDTO.getTotalAmount());

				} else {
					errorMsg = "Servey method not found.";
					RequestContext.getCurrentInstance().update("frm_Tender");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}

			} else {
				errorMsg = "Servey type not found.";
				RequestContext.getCurrentInstance().update("frm_Tender");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else {
			errorMsg = "Servey No. Should be selected.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

	}

	/**
	 * This method insert one record to a tbl_LocationAndTime
	 */
	public void addLocationTime() {

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
									locationAndTimeTableList = surveyService
											.getLocationTimeTable(surveyDTO.getSurveyNo());

									surveyDTO.setLocation(null);
									surveyDTO.setStartDate(null);
									surveyDTO.setEndDate(null);
									surveyDTO.setStartTime(null);
									surveyDTO.setEndTime(null);
								} else {
									surveyDTO.setEndTime(null);
									errorMsg = "End Time should be equal or after Start Time.";
									RequestContext.getCurrentInstance().update("frm_Tender");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else if (surveyDTO.getEndDate().after(surveyDTO.getStartDate())) {

								surveyDTO.setLoginUser(sessionBackingBean.getLoginUser());
								surveyService.addLoactionTime(surveyDTO);
								locationAndTimeTableList = surveyService.getLocationTimeTable(surveyDTO.getSurveyNo());

								surveyDTO.setLocation(null);
								surveyDTO.setStartDate(null);
								surveyDTO.setEndDate(null);
								surveyDTO.setStartTime(null);
								surveyDTO.setEndTime(null);

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
		teamDetailsTableList = surveyService.getTeamDetailsTableList(surveyDTO);

		renderTeamDetails = true;
	}

	public void retriveLocationTimeTable() {
		locationAndTimeTableList = surveyService.getLocationTimeTable(surveyDTO.getSurveyNo());
	}

	public void onLocationRowUnSelect(SelectEvent event) {

	}

	public void onUserIdChange() {
		surveyLocationTeamDTO.setFullName(surveyService.returnName(surveyLocationTeamDTO.getUserId()));
		surveyLocationTeamDTO.setNic(surveyService.returnNICNo(surveyLocationTeamDTO.getUserId()));
	}

	/**
	 * This method provide bank branches related to bank
	 */
	public void onBankChange() {

		bankBranchList = surveyService.getBankBranchDropDown(surveyLocationTeamDTO.getBank());
	}

	/**
	 * This method insert one record to the public.nt_t_survey_loc_member Table
	 */
	public void addTeamByLocation() {

		if (surveyDTO.getLocationSeqNo() != null && !surveyDTO.getLocationSeqNo().isEmpty()
				&& !surveyDTO.getLocationSeqNo().equalsIgnoreCase("")) {

			if (surveyLocationTeamDTO.getOrganization() != null && !surveyLocationTeamDTO.getOrganization().isEmpty()
					&& !surveyLocationTeamDTO.getOrganization().equalsIgnoreCase("")) {

				if (disableUserId) {
					validateAddTeamByLocation();
					if (!commonService.checkTaskOnSurveyHisDetails(surveyDTO.getSurveyNo(), "SU004", "C")) {
						commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(),
								"SU005", "O", sessionBackingBean.getLoginUser());
					}

				} else {
					if (surveyLocationTeamDTO.getUserId() != null && !surveyLocationTeamDTO.getUserId().isEmpty()
							&& !surveyLocationTeamDTO.getUserId().equalsIgnoreCase("")) {

						validateAddTeamByLocation();

						if (!commonService.checkTaskOnSurveyHisDetails(surveyDTO.getSurveyNo(), "SU004", "C")) {
							commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(),
									"SU005", "O", sessionBackingBean.getLoginUser());
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

	public boolean validateDuplicateOrganization() {
		boolean organization = false;

		for (SurveyLocationTeamDTO teamDetailsTableList : teamDetailsTableList) {
			if (teamDetailsTableList.getOrganization().equals(surveyLocationTeamDTO.getOrganization())) {
				organization = true;
			}
		}
		return organization;
	}

	public boolean validateCostCode() {
		boolean costCode = false;

		for (SurveyLocationTeamDTO costEstimateTableList : costEstimateTableList) {

			if (costEstimateTableList.getCostCode().equals(surveyLocationTeamDTO.getSelectedCostCode())) {
				costCode = true;
			}
		}
		return costCode;
	}

	public void retriveTeamDetailsTable() {
		teamDetailsTableList = surveyService.getTeamDetailsTableList(surveyDTO);
	}

	public void delete_tbl_LocationTimeRaw(String locationSeqNo) {

		surveyService.deleteRawFromLocationTable(locationSeqNo);
		deleteTeamDetailsByLocation(locationSeqNo);
		retriveLocationTimeTable();
		retriveTeamDetailsTable();
	}

	public void delete_tbl_TeamDetialsRaw(String memberSeqNo) {

		surveyService.deleteRawFromTeamDetailsTable(memberSeqNo);
		retriveTeamDetailsTable();
	}

	public void deleteTeamDetailsByLocation(String locationSeqNo) {
		surveyService.deleteTeamDetailsByLocation(locationSeqNo);
	}

	public void onChangeCostCode(String costCode) {

		surveyLocationTeamDTO
				.setCostCodeDescription(surveyService.getCodeDescription(surveyLocationTeamDTO.getSelectedCostCode()));

	}

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

	public void retriveCostEstimationTable() {

		costEstimateTableList = surveyService.getCostEstimationTableList(surveyDTO);
		surveyLocationTeamDTO.setTotalAmount(getTotalCostEstimate());
	}

	public void delete_tbl_CostEstimateRaw(String seqNO) {

		surveyService.deleteRawFromCostEstimateTable(seqNO);
		retriveCostEstimationTable();

		if (costEstimateTableList.size() > 0) {
			approvedEstimation = false;
		} else {
			approvedEstimation = true;
		}
	}

	public BigDecimal getTotalCostEstimate() {
		BigDecimal total = new BigDecimal(0);
		BigDecimal amt = null;

		for (SurveyLocationTeamDTO surveyLocationTeamDTO : costEstimateTableList) {
			amt = surveyLocationTeamDTO.getAmount();
			total = total.add(amt);
		}

		return total;
	}

	public void updateCostEstimateTask() {

		approvedEstimation = false;

		if (surveyService.getSurManApprovalStatus(sessionBackingBean.getLoginUser())) {
			commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(), "SU005", "O",
					sessionBackingBean.getLoginUser());
			activeTabIndex = 2;

			if (costEstimateTableList.size() > 0) {
				approvedEstimation = false;
			} else {
				approvedEstimation = true;
			}

		} else {
			activeTabIndex = 1;
			commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(), "SU005", "O",
					sessionBackingBean.getLoginUser());

		}

		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

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
									BigDecimal amt = null;
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

		if (paymentDetails.size() > 0) {
			setGeneratebtn(false);
		}
	}

	public void clearEnteredVal() {
		advancedPaymentDTO = new AdvancedPaymentDTO();
	}

	public void setGeneratebtn(boolean generatebtn) {
		this.generatebtn = generatebtn;
	}

	public void generateVoucher() {
		activeTabIndex = 3;
		String logInUser = sessionBackingBean.getLoginUser();
		paymentVoucherDTO.setApplicationNo(surveyDTO.getSurveyNo());
		paymentVoucherDTO.setPermitNo("");
		int result = paymentVoucherService.insertPaymentVoucher(paymentVoucherDTO, logInUser, totalfee, paymentDetails);

		if (!commonService.checkTaskOnSurveyHisDetails(surveyDTO.getRequestNo(), "SU005", "O")) {
			commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(), "SU005", "C",
					sessionBackingBean.getLoginUser());
		}

		if (result == 0) {
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			setDisable(true);
			setPrintbtn(false);
			setGeneratebtn(true);
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
		paymentVoucherDTO.setTransactionCode("08");
		paymentDetails = null;
		setDisable(false);
		totalfee = null;
		setPrintbtn(true);
		setGeneratebtn(true);
	}

	public void surveyManageBackingBean() {

		surveyService.updateTaskOnDetTable(surveyDTO.getSurveyNo());
		surveyService.deleteTaskOnDetTable(surveyDTO.getSurveyNo());
		surveyService.updateTaskOnHisTable(surveyDTO.getSurveyNo());
	}

	public void approveCostEstimation() {

		activeTabIndex = 2;
		if ((surveyLocationTeamDTO.getSpecialRemark() != null && !surveyLocationTeamDTO.getSpecialRemark().isEmpty()
				&& !surveyLocationTeamDTO.getSpecialRemark().equalsIgnoreCase(""))) {

			surveyService.approveRejectCostEstimation(surveyDTO.getSurveyNo(), "A", surveyLocationTeamDTO);
			surveyDTO.setLoginUser(sessionBackingBean.getLoginUser());

			String surveyNo = surveyService.insertInToTblCostEstimation(surveyDTO, surveyLocationTeamDTO);

			renderPaymentVoucher = true;
			advancedPaymentDTO.setAmount(surveyLocationTeamDTO.getTotalAmount());
			approvedEstimation = true;
			activeTabIndex = 3;
			disableCostEstimationSave = true;
			disableCostEstimationAdd = true;

		} else {

			errorMsg = "Special remarks shoud be entered.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void rejectCostEstimation() {

		if ((surveyLocationTeamDTO.getSpecialRemark() != null && !surveyLocationTeamDTO.getSpecialRemark().isEmpty()
				&& !surveyLocationTeamDTO.getSpecialRemark().equalsIgnoreCase(""))) {

			commonService.updateSurveyTaskDetails(surveyDTO.getRequestNo(), surveyDTO.getSurveyNo(), "SU005", "C",
					sessionBackingBean.getLoginUser());

			surveyService.approveRejectCostEstimation(surveyDTO.getSurveyNo(), "R", surveyLocationTeamDTO);
			renderPaymentVoucher = true;
			approvedEstimation = true;
			activeTabIndex = 3;

		} else {

			errorMsg = "Special remarks shoud be entered.";
			RequestContext.getCurrentInstance().update("frm_Tender");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

	}

	public void clear() {
		surveyDTO = new SurveyDTO();
		surveyLocationTeamDTO = new SurveyLocationTeamDTO();
		paymentVoucherDTO = new PaymentVoucherDTO();
		advancedPaymentDTO = new AdvancedPaymentDTO();
		dto = new AdvancedPaymentDTO();
		renderTabView = false;
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

	// Getters and setters
	public SurveyDTO getSurveyDTO() {
		return surveyDTO;
	}

	public void setSurveyDTO(SurveyDTO surveyDTO) {
		this.surveyDTO = surveyDTO;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SurveyDTO> getSurveyNoList() {
		return surveyNoList;
	}

	public void setSurveyNoList(List<SurveyDTO> surveyNoList) {
		this.surveyNoList = surveyNoList;
	}

	public List<SurveyDTO> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<SurveyDTO> locationList) {
		this.locationList = locationList;
	}

	public List<SurveyDTO> getLocationAndTimeTableList() {
		return locationAndTimeTableList;
	}

	public void setLocationAndTimeTableList(List<SurveyDTO> locationAndTimeTableList) {
		this.locationAndTimeTableList = locationAndTimeTableList;
	}

	public List<EmployeeDTO> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<EmployeeDTO> userIdList) {
		this.userIdList = userIdList;
	}

	public SurveyLocationTeamDTO getSurveyLocationTeamDTO() {
		return surveyLocationTeamDTO;
	}

	public void setSurveyLocationTeamDTO(SurveyLocationTeamDTO surveyLocationTeamDTO) {
		this.surveyLocationTeamDTO = surveyLocationTeamDTO;
	}

	public List<SurveyDTO> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<SurveyDTO> organizationList) {
		this.organizationList = organizationList;
	}

	public List<GenerateReceiptDTO> getBankList() {
		return bankList;
	}

	public void setBankList(List<GenerateReceiptDTO> bankList) {
		this.bankList = bankList;
	}

	public List<SurveyLocationTeamDTO> getResponsibilitiesList() {
		return responsibilitiesList;
	}

	public void setResponsibilitiesList(List<SurveyLocationTeamDTO> responsibilitiesList) {
		this.responsibilitiesList = responsibilitiesList;
	}

	public List<GenerateReceiptDTO> getBankBranchList() {
		return bankBranchList;
	}

	public void setBankBranchList(List<GenerateReceiptDTO> bankBranchList) {
		this.bankBranchList = bankBranchList;
	}

	public List<SurveyLocationTeamDTO> getTeamDetailsTableList() {
		return teamDetailsTableList;
	}

	public void setTeamDetailsTableList(List<SurveyLocationTeamDTO> teamDetailsTableList) {
		this.teamDetailsTableList = teamDetailsTableList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<String> getCostCodeList() {
		return costCodeList;
	}

	public void setCostCodeList(List<String> costCodeList) {
		this.costCodeList = costCodeList;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public List<CommonDTO> getTransactionTypeList() {
		return transactionTypeList;
	}

	public void setTransactionTypeList(List<CommonDTO> transactionTypeList) {
		this.transactionTypeList = transactionTypeList;
	}

	public List<CommonDTO> getDeparmentList() {
		return deparmentList;
	}

	public void setDeparmentList(List<CommonDTO> deparmentList) {
		this.deparmentList = deparmentList;
	}

	public List<CommonDTO> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<CommonDTO> unitList) {
		this.unitList = unitList;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public AdvancedPaymentDTO getAdvancedPaymentDTO() {
		return advancedPaymentDTO;
	}

	public void setAdvancedPaymentDTO(AdvancedPaymentDTO advancedPaymentDTO) {
		this.advancedPaymentDTO = advancedPaymentDTO;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public List<SurveyLocationTeamDTO> getCostEstimateTableList() {
		return costEstimateTableList;
	}

	public void setCostEstimateTableList(List<SurveyLocationTeamDTO> costEstimateTableList) {
		this.costEstimateTableList = costEstimateTableList;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
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

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public boolean isGeneratebtn() {
		return generatebtn;
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

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
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

	public boolean isRenderGrantApproval() {
		return renderGrantApproval;
	}

	public void setRenderGrantApproval(boolean renderGrantApproval) {
		this.renderGrantApproval = renderGrantApproval;
	}

	public boolean isRenderPaymentVoucher() {
		return renderPaymentVoucher;
	}

	public void setRenderPaymentVoucher(boolean renderPaymentVoucher) {
		this.renderPaymentVoucher = renderPaymentVoucher;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisableCostEstimationSave() {
		return disableCostEstimationSave;
	}

	public void setDisableCostEstimationSave(boolean disableCostEstimationSave) {
		this.disableCostEstimationSave = disableCostEstimationSave;
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

	public boolean isDisableUserId() {
		return disableUserId;
	}

	public void setDisableUserId(boolean disableUserId) {
		this.disableUserId = disableUserId;
	}

}
