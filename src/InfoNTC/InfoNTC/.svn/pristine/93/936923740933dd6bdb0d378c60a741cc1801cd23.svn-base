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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.PaymentVoucherDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;

@ManagedBean(name = "createPaymentVoucher")
@ViewScoped
public class CreatePaymentVoucherBckingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	public PaymentVoucherService paymentVoucherService;
	private List<PaymentVoucherDTO> transactionTypeList = new ArrayList<PaymentVoucherDTO>(0);
	private List<PaymentVoucherDTO> deaprtmentList = new ArrayList<PaymentVoucherDTO>(0);
	private List<PaymentVoucherDTO> applicationNoList = new ArrayList<PaymentVoucherDTO>(0);
	private List<PaymentVoucherDTO> permitNoList = new ArrayList<PaymentVoucherDTO>(0);

	private List<String> chargeTypeList = new ArrayList<String>(0);
	private List<String> accountNoList = new ArrayList<String>(0);
	private PaymentVoucherDTO paymentVoucherDTO, selectDTO;
	private TenderDTO tenderDTO;
	private String value, errorMessage, selectingApplicationNo, selectingPermitNo, logInUser, successMessage, alertMSG;
	private List<PaymentVoucherDTO> voucherDetails;
	private List<PaymentVoucherDTO> detailsList = new ArrayList<PaymentVoucherDTO>(0);
	private BigDecimal totalfee;
	private boolean disableAdd, disableClearTwo, isSearched, isVoucherGenerate, disableRePrint, isPhotoUpload,
			wasGenerated, disableGenarate, disablePrint, disableChargeType, disableAccountNo, disableAmount,
			isPhotoUploadHistory, isApproved, diableApplicationNo, editMood = false, skipChargeTypes,
			disabledskipChargeTypes, disabledDelete, disabledEdit, disabledFiled, disabledsearch, disableModeOne,
			isSisuSariya;
	private BigDecimal input1 = new BigDecimal(0);
	private PaymentVoucherDTO dto;
	private String date1, transactionCode;
	private CommonService commonService;
	private SisuSariyaService sisuSariyaService;
	private StreamedContent files;

	public CreatePaymentVoucherBckingBean() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		transactionTypeList = paymentVoucherService.getTranactionType();
		chargeTypeList = paymentVoucherService.getChargeType();

		paymentVoucherDTO = new PaymentVoucherDTO();
		selectDTO = new PaymentVoucherDTO();
		deaprtmentList = paymentVoucherService.getDepartment();
		disableGenarate = true;
		disableAdd = true;
		disableClearTwo = true;
		disablePrint = true;
		input1 = null;
		disableRePrint = true;
		disableChargeType = true;
		disableAccountNo = true;
		disableAmount = true;
		diableApplicationNo = true;
		disabledskipChargeTypes = true;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		date1 = dateFormat.format(date);

	}

	public void ajaxFillApplicationNo() {
		diableApplicationNo = false;

		String transactionTypeCode = paymentVoucherService.getTranCode(paymentVoucherDTO);

		if (transactionTypeCode != null) {
			applicationNoList = paymentVoucherService.getApplicationNoUsingTranactionType(paymentVoucherDTO,
					transactionTypeCode);

			permitNoList = paymentVoucherService.getPermitNoUsingTranactionType(paymentVoucherDTO, transactionTypeCode);

		}

	}

	public void completeAppNo() {

		selectingApplicationNo = paymentVoucherService.getApplicationNo(paymentVoucherDTO.getPermitNo());
		paymentVoucherDTO.setApplicationNo(selectingApplicationNo);

	}

	public void completePermitNo() {
		selectingPermitNo = paymentVoucherService.getPermitNo(paymentVoucherDTO.getApplicationNo());
		paymentVoucherDTO.setPermitNo(selectingPermitNo);

	}

	public void delete(String chargeDescription) {

		for (int i = 0; i < voucherDetails.size(); i++) {

			if (voucherDetails.get(i).getChargeDescription().equals(chargeDescription)) {

				this.totalfee = totalfee.subtract(voucherDetails.get(i).getAmount());
				setTotalfee(totalfee);
				voucherDetails.remove((i));

			}
		}

	}

	/* Search Voucher Details For Sisu Sariya Flow */
	public void searchSisuSariyaVoucher() {
		if (sessionBackingBean.getServiceNo() != null && sessionBackingBean.getTransactionDescription() != null
				&& sessionBackingBean.getServiceRefNo() != null) {

			paymentVoucherDTO.setServiceNo(sessionBackingBean.getServiceNo());
			paymentVoucherDTO.setTransactionDescription(sessionBackingBean.getTransactionDescription());
			paymentVoucherDTO.setServiceRefNo(sessionBackingBean.getServiceRefNo());
		}

		if (paymentVoucherDTO.getTransactionDescription() == null
				|| paymentVoucherDTO.getTransactionDescription().trim().equalsIgnoreCase("")) {

			errorMessage = "Please select the Transaction Type.";
			sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
			return;

		} else {

			if ((paymentVoucherDTO.getDepartmentCode() == null
					|| paymentVoucherDTO.getDepartmentCode().trim().equalsIgnoreCase(""))) {

				errorMessage = "Please select the Department. ";
				sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
				return;

			} else {

				if ((paymentVoucherDTO.getServiceNo() == null
						|| paymentVoucherDTO.getServiceNo().trim().equalsIgnoreCase(""))) {

					errorMessage = "Please enter Service No.";
					sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
					return;

				} else {

					boolean isApplicationNoCorrect = paymentVoucherService
							.checkSisuSariyaApplicationNumber(paymentVoucherDTO.getServiceNo());

					if (isApplicationNoCorrect == true && paymentVoucherDTO.getServiceNo() != null
							|| !paymentVoucherDTO.getServiceNo().trim().equalsIgnoreCase("")) {

						/*boolean wasVoucherGenerated = paymentVoucherService
								.isTenderVoucherGenerated(paymentVoucherDTO.getServiceNo());*/
						/***commented on 01/31/2022 pass service ref number instead service number**/
						boolean wasVoucherGenerated = paymentVoucherService
								.isTenderVoucherGenerated(paymentVoucherDTO.getServiceRefNo());

						if (wasVoucherGenerated == true) {
							voucherDetails = new ArrayList<PaymentVoucherDTO>();
							/*voucherDetails = paymentVoucherService
									.getGeneratedVoucherDetails(paymentVoucherDTO.getServiceNo());*/
							
							voucherDetails = paymentVoucherService
									.getGeneratedVoucherDetails(paymentVoucherDTO.getServiceRefNo());

							if (voucherDetails.isEmpty()) {
								disableGenarate = true;
								disableClearTwo = true;
								disableAdd = true;
								disableRePrint = true;
								disablePrint = true;
								setTotalfee(null);

								errorMessage = "No data found for search values.";
								sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
								return;

							} else {
								setValue(voucherDetails.get(0).getVoucherNo());
								RequestContext.getCurrentInstance().update("voucherNo");

								disableGenarate = true;
								disableClearTwo = true;
								disableAdd = true;
								isSearched = true;
								disableChargeType = true;
								disableAccountNo = true;
								disablePrint = true;
								disableAmount = true;
								disableRePrint = false;

								totalfee = new BigDecimal(0);

								for (int i = 0; i < voucherDetails.size(); i++) {

									try {

										BigDecimal amt = null;
										amt = voucherDetails.get(i).getAmount();
										totalfee = totalfee.add(amt);

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

						} else {

							paymentVoucherDTO.setTransactionCode(paymentVoucherService.getTranCode(paymentVoucherDTO));

							voucherDetails = new ArrayList<PaymentVoucherDTO>();
							voucherDetails = paymentVoucherService
									.getVoucherDetails(paymentVoucherDTO.getTransactionDescription());

							if (voucherDetails.isEmpty()) {

								disableGenarate = true;
								disableClearTwo = true;
								disableAdd = true;
								disableRePrint = true;
								disablePrint = true;
								setTotalfee(null);

								errorMessage = "No data found for searched transaction type.";
								sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
								return;

							} else {
								isSisuSariya = true;
								disableGenarate = false;
								disabledFiled = true;
								diableApplicationNo = true;
								disableClearTwo = false;
								disableAdd = false;
								isSearched = true;
								disableChargeType = false;
								disableAccountNo = true;
								disableAmount = false;
								disabledskipChargeTypes = false;

								totalfee = new BigDecimal(0);

								for (int i = 0; i < voucherDetails.size(); i++) {

									try {
										BigDecimal amt = null;
										amt = voucherDetails.get(i).getAmount();
										totalfee = totalfee.add(amt);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

								sisuSariyaService.updateTaskStatusOngoingSubsidyTaskTable(
										sessionBackingBean.getRequestNo(), paymentVoucherDTO.getServiceNo(),
										sessionBackingBean.getServiceRefNo(), "SM001", "O",
										sessionBackingBean.getLoginUser());

							}
						}

					} else {
						errorMessage = "Invalid Service No.";
						sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
						return;
					}

				}
			}
		}
	}

	/* Search Voucher Details For Tender Flow */
	public void searchIssueTenderVoucher() {

		if (sessionBackingBean.getApplicationNo() != null && sessionBackingBean.getTransactionDescription() != null) {

			paymentVoucherDTO.setApplicationNo(sessionBackingBean.getApplicationNo());
			paymentVoucherDTO.setTransactionDescription(sessionBackingBean.getTransactionDescription());
		}

		if (paymentVoucherDTO.getTransactionDescription() == null
				|| paymentVoucherDTO.getTransactionDescription().trim().equalsIgnoreCase("")) {

			errorMessage = "Please select the Transaction Type.";
			sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
			return;

		} else {

			if ((paymentVoucherDTO.getDepartmentCode() == null
					|| paymentVoucherDTO.getDepartmentCode().trim().equalsIgnoreCase(""))) {

				errorMessage = "Please select the Department. ";
				sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
				return;

			} else {

				if ((paymentVoucherDTO.getApplicationNo() == null
						|| paymentVoucherDTO.getApplicationNo().trim().equalsIgnoreCase(""))) {

					errorMessage = "Please enter Application";
					sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
					return;

				} else {

					boolean isApplicationNoCorrect = paymentVoucherService
							.checkTenderApplicationNumber(paymentVoucherDTO.getApplicationNo());

					if (isApplicationNoCorrect == true && paymentVoucherDTO.getApplicationNo() != null
							|| !paymentVoucherDTO.getApplicationNo().trim().equalsIgnoreCase("")) {

						boolean wasVoucherGenerated = paymentVoucherService
								.isTenderVoucherGenerated(paymentVoucherDTO.getApplicationNo());

						if (wasVoucherGenerated == true) {
							voucherDetails = new ArrayList<PaymentVoucherDTO>();
							voucherDetails = paymentVoucherService
									.getGeneratedVoucherDetails(paymentVoucherDTO.getApplicationNo());

							if (voucherDetails.isEmpty()) {
								disableGenarate = true;
								disableClearTwo = true;
								disableAdd = true;
								disableRePrint = true;
								disablePrint = true;
								setTotalfee(null);

								errorMessage = "No data found for search values.";
								sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
								return;

							} else {
								setValue(voucherDetails.get(0).getVoucherNo());
								RequestContext.getCurrentInstance().update("voucherNo");

								disableGenarate = true;
								disableClearTwo = true;
								disableAdd = true;
								isSearched = true;
								disableChargeType = true;
								disableAccountNo = true;
								disableAmount = true;
								disableRePrint = false;

								totalfee = new BigDecimal(0);

								for (int i = 0; i < voucherDetails.size(); i++) {

									try {

										BigDecimal amt = null;
										amt = voucherDetails.get(i).getAmount();
										totalfee = totalfee.add(amt);

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}

						} else {

							paymentVoucherDTO.setTransactionCode(paymentVoucherService.getTranCode(paymentVoucherDTO));

							voucherDetails = new ArrayList<PaymentVoucherDTO>();
							voucherDetails = paymentVoucherService
									.getVoucherDetails(paymentVoucherDTO.getTransactionDescription());

							if (voucherDetails.isEmpty()) {

								disableGenarate = true;
								disableClearTwo = true;
								disableAdd = true;
								disableRePrint = true;
								disablePrint = true;
								setTotalfee(null);

								errorMessage = "No data found for searched transaction type.";
								sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
								return;

							} else {
								disableGenarate = false;
								disabledFiled = true;
								diableApplicationNo = true;
								disableClearTwo = false;
								disableAdd = false;
								isSearched = true;
								disableChargeType = false;
								disableAccountNo = true;
								disableAmount = false;
								disabledskipChargeTypes = false;

								totalfee = new BigDecimal(0);

								for (int i = 0; i < voucherDetails.size(); i++) {

									try {
										BigDecimal amt = null;
										amt = voucherDetails.get(i).getAmount();
										totalfee = totalfee.add(amt);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								commonService.updateTaskStatus(paymentVoucherDTO.getApplicationNo(), "TD006", "TD007",
										"C", sessionBackingBean.getLoginUser());

							}
						}

					} else {
						errorMessage = "Invalid Application No.";
						sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
						return;
					}

				}
			}
		}
	}

	/* Search Voucher Details For Normal Flow */
	public void searchVoucher() {

		sessionBackingBean.setIssueTenderMood(false);
		String loginUser = sessionBackingBean.getLoginUser();
		/* Get Transaction code */
		String transCode = paymentVoucherService.getTranCode(paymentVoucherDTO);
  if(transCode.equals("03")) {
	  if (sessionBackingBean.getApplicationNo() != null 
				&& sessionBackingBean.getTransactionDescription() != null) {

			paymentVoucherDTO.setApplicationNo(sessionBackingBean.getApplicationNo());
			paymentVoucherDTO.setTransactionDescription(sessionBackingBean.getTransactionDescription());
			
		}
  }
  else {
		if (sessionBackingBean.getApplicationNo() != null && sessionBackingBean.getPermitNo() != null
				&& sessionBackingBean.getTransactionDescription() != null) {

			paymentVoucherDTO.setApplicationNo(sessionBackingBean.getApplicationNo());
			paymentVoucherDTO.setTransactionDescription(sessionBackingBean.getTransactionDescription());
			paymentVoucherDTO.setPermitNo(sessionBackingBean.getPermitNo());
		}
	}

		if (paymentVoucherDTO.getTransactionDescription() == null
				|| paymentVoucherDTO.getTransactionDescription().trim().equalsIgnoreCase("")) {

			errorMessage = "Please select the Transaction Type.";
			sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
			return;

		} else {

			if ((paymentVoucherDTO.getDepartmentCode() == null
					|| paymentVoucherDTO.getDepartmentCode().trim().equalsIgnoreCase(""))) {

				errorMessage = "Please select the Department. ";
				sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
				return;

			} else {

				if ((paymentVoucherDTO.getApplicationNo() == null
						|| paymentVoucherDTO.getApplicationNo().trim().equalsIgnoreCase(""))
						&& (paymentVoucherDTO.getPermitNo() == null
								|| paymentVoucherDTO.getPermitNo().trim().equalsIgnoreCase(""))) {

					errorMessage = "Please enter Application/Permit No.";
					sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
					return;

				} else {

					boolean isPermitNoCorrect = paymentVoucherService
							.checkPermitNumber(paymentVoucherDTO.getPermitNo());
					boolean isApplicationNoCorrect = paymentVoucherService
							.checkApplicationNumber(paymentVoucherDTO.getApplicationNo());

				

					if (transCode.equals("21") || transCode.equals("22") || transCode.equals("23")
							|| transCode.equals("13") || transCode.equals("14") || transCode.equals("15")
							|| transCode.equals("16")) {
						/*
						 * This method gets only approved Application No. So
						 * Amendments Application No Approved Manually
						 */
						isApproved = true;

					} else {
						/*
						 * If application no. are not amendments, Then Check
						 * application Status
						 */

						isApproved = paymentVoucherService.checkApplicationNo(paymentVoucherDTO);

					}

					/* Checking application No and Permit No */
					if (isPermitNoCorrect == true && paymentVoucherDTO.getApplicationNo() != null
							|| !paymentVoucherDTO.getApplicationNo().trim().equalsIgnoreCase("")) {

						if (isApplicationNoCorrect == true && paymentVoucherDTO.getPermitNo() != null
								|| !paymentVoucherDTO.getPermitNo().trim().equalsIgnoreCase("")) {

							/* If application No. approved */
							if (isApproved == true) {

								String taskCode = "PM201";

								boolean wasVoucherGenerated = paymentVoucherService
										.alreadyGenerate(paymentVoucherDTO.getApplicationNo(), loginUser);

								/* If voucher Generated. Show Voucher Details */
								if (wasVoucherGenerated == true) {
									voucherDetails = new ArrayList<PaymentVoucherDTO>();
									voucherDetails = paymentVoucherService
											.getGeneratedVoucherDetails(paymentVoucherDTO.getApplicationNo());

									if (voucherDetails.isEmpty()) {
										disableGenarate = true;
										disableClearTwo = true;
										disableAdd = true;
										disableRePrint = true;
										disablePrint = true;
										setTotalfee(null);

										errorMessage = "No data found for search values.";
										sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
										return;

									} else {
										setValue(voucherDetails.get(0).getVoucherNo());
										disableGenarate = true;
										disableClearTwo = true;
										disableAdd = true;
										isSearched = true;
										disableChargeType = true;
										disableAccountNo = true;
										disableAmount = true;
										disableRePrint = false;

										totalfee = new BigDecimal(0);

										for (int i = 0; i < voucherDetails.size(); i++) {

											try {

												BigDecimal amt = null;
												amt = voucherDetails.get(i).getAmount();
												totalfee = totalfee.add(amt);

											} catch (Exception e) {
												e.printStackTrace();
											}
										}

										boolean isAttornyFound = paymentVoucherService
												.checkPowerOfAttorney(paymentVoucherDTO.getPermitNo());

										if (isAttornyFound) {

											setAlertMSG("Power of Attorney Holder exists for this Permit Number.");
											RequestContext.getCurrentInstance().update("frmAlert");
											RequestContext.getCurrentInstance().execute("PF('alertMSG').show()");
										}
									}
									/* If Voucher Is Not Generated */
								} else {
									transactionCode = paymentVoucherService.getTranCode(paymentVoucherDTO);

									/*
									 * Get Transaction Code and If code equal to
									 * amendments
									 */

									if (transactionCode.equals("21") || transactionCode.equals("22")
											|| transactionCode.equals("23") || transactionCode.equals("13")
											|| transactionCode.equals("14") || transactionCode.equals("15")
											|| transactionCode.equals("16")) {

										/*
										 * Checking For Amendments after the
										 * application no. generated
										 */

										boolean isAM100TaskComplete1 = paymentVoucherService
												.checkTaskDetails(paymentVoucherDTO, "AM100", "C");
										boolean isSearchedPayment1 = paymentVoucherService
												.checkTaskDetails(paymentVoucherDTO, "PM300", "O");
										boolean isAM100TaskFoundTaskHistory = paymentVoucherService
												.checkTaskHistory(paymentVoucherDTO, "AM100", "C");

										/*
										 * Checking For Amendments after the
										 * application no. generated first
										 * approval
										 */

										boolean isAM104TaskComplete = paymentVoucherService
												.checkTaskDetails(paymentVoucherDTO, "AM104", "C");

										boolean isSearchedPayment = paymentVoucherService
												.checkTaskDetails(paymentVoucherDTO, "PM300", "O");

										boolean isAM104TaskFoundTaskHistory = paymentVoucherService
												.checkTaskHistory(paymentVoucherDTO, "AM104", "C");

										/* Checking.......... */

										if ((isAM100TaskComplete1 == true || isSearchedPayment1 == true)
												&& !transactionCode.equals("13")) {

											paymentVoucherDTO.setTransactionCode(
													paymentVoucherService.getTranCode(paymentVoucherDTO));

											voucherDetails = new ArrayList<PaymentVoucherDTO>();
											voucherDetails = paymentVoucherService
													.getVoucherDetails(paymentVoucherDTO.getTransactionDescription());

											if (voucherDetails.isEmpty()) {

												disableGenarate = true;
												disableClearTwo = true;
												disableAdd = true;
												disableRePrint = true;
												disablePrint = true;
												setTotalfee(null);

												errorMessage = "No data found for search values.";
												sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
												return;

											} else {
												disableGenarate = false;
												disabledFiled = true;
												diableApplicationNo = true;
												disableClearTwo = false;
												disableAdd = false;
												isSearched = true;
												disableChargeType = false;
												disableAccountNo = true;
												disableAmount = false;
												disabledskipChargeTypes = false;

												totalfee = new BigDecimal(0);

												for (int i = 0; i < voucherDetails.size(); i++) {

													try {
														BigDecimal amt = null;
														amt = voucherDetails.get(i).getAmount();
														totalfee = totalfee.add(amt);
													} catch (Exception e) {
														e.printStackTrace();
													}
												}

												boolean isAttornyFound = paymentVoucherService
														.checkPowerOfAttorney(paymentVoucherDTO.getPermitNo());

												if (isAttornyFound) {

													setAlertMSG(
															"Power of Attorney Holder exists for this Permit Number.");
													RequestContext.getCurrentInstance().update("frmAlert");
													RequestContext.getCurrentInstance()
															.execute("PF('alertMSG').show()");

												}

											}

											transactionCode = paymentVoucherService.getTranCode(paymentVoucherDTO);
											/* Update Task Table Details */
											commonService.updateTaskStatus(paymentVoucherDTO.getApplicationNo(),
													"AM100", "PM300", "C", sessionBackingBean.getLoginUser());

										} else if (isAM104TaskComplete == true || isSearchedPayment == true) {

											paymentVoucherDTO.setTransactionCode(
													paymentVoucherService.getTranCode(paymentVoucherDTO));

											voucherDetails = new ArrayList<PaymentVoucherDTO>();
											voucherDetails = paymentVoucherService
													.getVoucherDetails(paymentVoucherDTO.getTransactionDescription());

											if (voucherDetails.isEmpty()) {

												disableGenarate = true;
												disableClearTwo = true;
												disableAdd = true;
												disableRePrint = true;
												disablePrint = true;
												setTotalfee(null);

												errorMessage = "No data found for search values.";
												sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
												return;

											} else {

												disableGenarate = false;
												disabledFiled = true;
												diableApplicationNo = true;
												disableClearTwo = false;
												disableAdd = false;
												isSearched = true;
												disableChargeType = false;
												disableAccountNo = true;
												disableAmount = false;
												disabledskipChargeTypes = false;

												totalfee = new BigDecimal(0);

												for (int i = 0; i < voucherDetails.size(); i++) {

													try {
														BigDecimal amt = null;
														amt = voucherDetails.get(i).getAmount();
														totalfee = totalfee.add(amt);
													} catch (Exception e) {
														e.printStackTrace();
													}
												}

												boolean isAttornyFound = paymentVoucherService
														.checkPowerOfAttorney(paymentVoucherDTO.getPermitNo());

												if (isAttornyFound) {

													setAlertMSG(
															"Power of Attorney Holder exists for this Permit Number.");
													RequestContext.getCurrentInstance().update("frmAlert");
													RequestContext.getCurrentInstance()
															.execute("PF('alertMSG').show()");
												}

											}

											transactionCode = paymentVoucherService.getTranCode(paymentVoucherDTO);
											/* Update Task Table Details */
											commonService.updateTaskStatus(paymentVoucherDTO.getApplicationNo(),
													"AM104", "PM300", "C", sessionBackingBean.getLoginUser());

										} else {
											errorMessage = "Approval for amendments payment should be completed";
											sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
											return;
										}

									} else {

										/*
										 * Checking For Permit Renewals And
										 * Issue New Permits
										 */

										boolean isRelatedTaskComplete = paymentVoucherService
												.checkTaskDetails(paymentVoucherDTO, taskCode, "C");

										boolean isSearchedPayment = paymentVoucherService
												.checkTaskDetails(paymentVoucherDTO, "PM300", "O");

										if (isRelatedTaskComplete == true || isSearchedPayment == true) {

											paymentVoucherDTO.setTransactionCode(
													paymentVoucherService.getTranCode(paymentVoucherDTO));

											voucherDetails = new ArrayList<PaymentVoucherDTO>();
											voucherDetails = paymentVoucherService
													.getVoucherDetails(paymentVoucherDTO.getTransactionDescription());

											if (voucherDetails.isEmpty()) {

												disableGenarate = true;
												disableClearTwo = true;
												disableAdd = true;
												disableRePrint = true;
												disablePrint = true;
												setTotalfee(null);

												errorMessage = "No data found for search values.";
												sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
												return;

											} else {
												disableGenarate = false;
												disabledFiled = true;
												diableApplicationNo = true;
												disableClearTwo = false;
												disableAdd = false;
												isSearched = true;
												disableChargeType = false;
												disableAccountNo = true;
												disableAmount = false;
												disabledskipChargeTypes = false;

												totalfee = new BigDecimal(0);

												for (int i = 0; i < voucherDetails.size(); i++) {

													try {
														BigDecimal amt = null;
														amt = voucherDetails.get(i).getAmount();
														totalfee = totalfee.add(amt);
													} catch (Exception e) {
														e.printStackTrace();
													}
												}

												boolean isAttornyFound = paymentVoucherService
														.checkPowerOfAttorney(paymentVoucherDTO.getPermitNo());

												if (isAttornyFound) {

													setAlertMSG(
															"Power of Attorney Holder exists for this Permit Number.");
													RequestContext.getCurrentInstance().update("frmAlert");
													RequestContext.getCurrentInstance()
															.execute("PF('alertMSG').show()");
												}

											}

											transactionCode = paymentVoucherService.getTranCode(paymentVoucherDTO);
											/* Update Task Table Details */
											commonService.updateTaskStatus(paymentVoucherDTO.getApplicationNo(),
													"PM201", "PM300", "C", sessionBackingBean.getLoginUser());

										} else {
											errorMessage = "Permit Approval task should be completed.";
											sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
											return;
										}
									}

								}
							} else {

								errorMessage = "Entered Application No. is not Approved";
								sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
								return;
							}

						} else {

							errorMessage = "Invalid Application No.";
							sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
							return;
						}

					} else {

						errorMessage = "Invalid  Permit No.";
						sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
						return;
					}

				}
			}
		}
	}

	public void ajaxSkipChargeTypes() {

		boolean isVoucherGenerated = paymentVoucherService.checkTaskDetails(paymentVoucherDTO, "PM300", "C");

		if (skipChargeTypes == true && isVoucherGenerated == false) {
			disableGenarate = false;
			disableClearTwo = true;
			disableAdd = true;
			isSearched = true;
			disableChargeType = true;
			disableAccountNo = true;
			disableAmount = true;

		} else {

			disableGenarate = false;
			disableClearTwo = false;
			disableAdd = false;
			isSearched = true;
			disableChargeType = false;
			disableAccountNo = true;
			disableAmount = false;

		}

	}

	public void ajaxFilterAcountNo() {
		if (paymentVoucherDTO.getChargeDescription() != null
				&& !paymentVoucherDTO.getChargeDescription().trim().isEmpty()) {

			String chargeCode = paymentVoucherService.getChargeCode(paymentVoucherDTO.getChargeDescription());
			accountNoList = paymentVoucherService.getAccountNo(chargeCode, paymentVoucherDTO);

			if (accountNoList.isEmpty()) {

				disableAccountNo = true;
				errorMessage = "No active acount found selected charge type";
				sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
				return;

			}

			if (editMood == false && disableModeOne == false) {
				disableAccountNo = false;
			} else {
				disableAccountNo = true;
			}

		}
	}

	public void addNewCharge() {

		if (paymentVoucherDTO.getChargeDescription() != null && !paymentVoucherDTO.getChargeDescription().isEmpty()) {

			if (paymentVoucherDTO.getAccountNO() != null && !paymentVoucherDTO.getAccountNO().trim().isEmpty()) {

				if (paymentVoucherDTO.getAmount() != null) {

					String chargeCode = paymentVoucherService.getChargeCode(paymentVoucherDTO.getChargeDescription());

					dto = new PaymentVoucherDTO(paymentVoucherDTO.getChargeDescription(),
							paymentVoucherDTO.getAccountNO(), paymentVoucherDTO.getAmount(), chargeCode);

					boolean isfound = false;

					for (int i = 0; i < voucherDetails.size(); i++) {

						if (voucherDetails.get(i).getChargeDescription().equals(dto.getChargeDescription())) {
							isfound = true;
							break;
						} else {
							isfound = false;
						}

					}

					if (isfound == false || editMood == true) {

						if (editMood == false) {

							voucherDetails.add(dto);

							/*
							 * Old Code this value change because of tender
							 * payment
							 */

							paymentVoucherDTO.setAccountNO(null);
							paymentVoucherDTO.setChargeCode(null);
							paymentVoucherDTO.setChargeDescription(null);
							paymentVoucherDTO.setAmount(null);

							disableAdd = false;
							disableGenarate = false;
							disableClearTwo = false;
							BigDecimal amt = null;
							amt = dto.getAmount();
							this.totalfee = totalfee.add(amt);

						} else {

							for (int i = 0; i < voucherDetails.size(); i++) {

								if (voucherDetails.get(i).getChargeDescription()
										.equals(selectDTO.getChargeDescription()) == true) {

									disableAdd = false;
									disableClearTwo = false;
									voucherDetails.get(i).setAmount(paymentVoucherDTO.getAmount());
								}

							}

							BigDecimal newAmount = null;
							newAmount = paymentVoucherDTO.getAmount();
							this.totalfee = totalfee.add(newAmount);

							paymentVoucherDTO.setChargeDescription(null);
							paymentVoucherDTO.setChargeCode(null);
							paymentVoucherDTO.setAmount(null);
							paymentVoucherDTO.setAccountNO(null);
							disabledsearch = false;
							disabledDelete = false;
							disabledEdit = false;
							editMood = false;
							disableChargeType = false;
							disableGenarate = false;
							disabledskipChargeTypes = false;
							disableModeOne = false;
						}

					} else {

						disableAdd = false;
						disableClearTwo = false;
						errorMessage = "Charge Type is alrady added.";
						sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
						return;
					}

				} else {

					disableAdd = false;
					disableClearTwo = false;
					errorMessage = "Please enter the Amount.";
					sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
					return;
				}
			} else {

				disableAdd = false;
				disableClearTwo = false;
				errorMessage = "Please select Account No.";
				sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
				return;
			}

		} else {

			disableAdd = false;
			disableClearTwo = false;
			errorMessage = "Please select Charge Type";
			sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
			return;
		}

	}

	public void edit() {

		if (editMood == false) {
			disableModeOne = true;
			disableChargeType = true;
			disabledsearch = true;
			disableAccountNo = true;
			disableAdd = false;
			disableGenarate = true;
			disabledEdit = true;
			disabledDelete = true;
			disabledskipChargeTypes = true;

			BigDecimal subtractAmount = null;
			subtractAmount = selectDTO.getAmount();
			this.totalfee = totalfee.subtract(subtractAmount);

			paymentVoucherDTO.setChargeDescription(selectDTO.getChargeDescription());
			paymentVoucherDTO.setAmount(selectDTO.getAmount());
			ajaxFilterAcountNo();
			paymentVoucherDTO.setAccountNO(selectDTO.getAccountNO());

			editMood = true;

		} else {
			errorMessage = "Please Complete the Edit Amount For " + selectDTO.getChargeDescription();
			sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
			return;
		}
	}

	public void clearThree() {
		if (editMood == false) {
			voucherDetails = new ArrayList<PaymentVoucherDTO>();
			totalfee = new BigDecimal(0);
			value = null;
			disableGenarate = true;

		} else {
			errorMessage = "Please Complete the Edit Amount For " + selectDTO.getChargeDescription();
			sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
			return;
		}
	}

	public void clearTwo() {
		paymentVoucherDTO.setAccountNO(null);
		paymentVoucherDTO.setAmount(null);
		paymentVoucherDTO.setChargeDescription(null);
		paymentVoucherDTO.setChargeCode(null);

		if (editMood == true) {
			BigDecimal addAmount = null;
			addAmount = selectDTO.getAmount();
			this.totalfee = totalfee.add(addAmount);
			editMood = false;
		}

		disableAccountNo = true;
		disableClearTwo = false;
		skipChargeTypes = false;
		disableChargeType = false;
		disabledskipChargeTypes = false;
		disabledsearch = false;
		disabledDelete = false;
		disabledEdit = false;
		disableModeOne = false;
		disableAdd = false;
		disableGenarate = false;

	}

	public void clearOne() {
		voucherDetails = new ArrayList<PaymentVoucherDTO>();
		paymentVoucherDTO = new PaymentVoucherDTO();
		totalfee = null;
		disableAccountNo = true;
		disableAdd = true;
		disableClearTwo = true;
		disableRePrint = true;
		diableApplicationNo = true;
		disabledskipChargeTypes = true;
		disableGenarate = true;
		disableChargeType = true;
		value = null;
		disabledFiled = false;
		diableApplicationNo = false;
		disabledsearch = false;

	}

	public void generateVoucher() {

		String loginUser = sessionBackingBean.getLoginUser();

		if (isSearched == true && !voucherDetails.isEmpty()) {
			logInUser = sessionBackingBean.getLoginUser();

			wasGenerated = paymentVoucherService.alreadyGenerate(paymentVoucherDTO.getApplicationNo(), logInUser);

			if (wasGenerated == false) {

				value = paymentVoucherService.generateReferenceNo();
				transactionCode = paymentVoucherService.getTranCode(paymentVoucherDTO);

				paymentVoucherDTO.setTransactionCode(transactionCode);

				boolean istender = sessionBackingBean.isIssueTenderMood();

				isVoucherGenerate = paymentVoucherService.generateVoucher(paymentVoucherDTO, logInUser, totalfee, value,
						istender, skipChargeTypes, isSisuSariya);

				if (isVoucherGenerate == true) {

					/* Update task details and voucher details */
					if (skipChargeTypes == true) {

						paymentVoucherService.updateVoucherDetails(paymentVoucherDTO, null, logInUser, value, null,
								false);
						disabledskipChargeTypes = true;

						// Complete payment task
						commonService.updateTaskStatus(paymentVoucherDTO.getApplicationNo(), "PM300", "PM301", "C",
								sessionBackingBean.getLoginUser());
						commonService.updateTaskStatusCompleted(paymentVoucherDTO.getApplicationNo(), "PM301",
								sessionBackingBean.getLoginUser());

						// Complete receipt task
						commonService.updateTaskStatus(paymentVoucherDTO.getApplicationNo(), "PM301", "PM302", "C",
								sessionBackingBean.getLoginUser());
						commonService.updateTaskStatusCompleted(paymentVoucherDTO.getApplicationNo(), "PM302",
								sessionBackingBean.getLoginUser());

					} else if (sessionBackingBean.isIssueTenderMood() == true && skipChargeTypes == false) {

						paymentVoucherService.updateVoucherDetails(paymentVoucherDTO, totalfee, logInUser, value,
								voucherDetails, false);

						commonService.updateTaskStatusCompleted(paymentVoucherDTO.getApplicationNo(), "TD007",
								sessionBackingBean.getLoginUser());
						paymentVoucherService.updateTenderApplicant(paymentVoucherDTO, value);

						disabledskipChargeTypes = true;

					} else if (isSisuSariya == true && skipChargeTypes == false) {

						paymentVoucherService.updateVoucherDetails(paymentVoucherDTO, totalfee, logInUser, value,
								voucherDetails, isSisuSariya);
						commonService.updateTaskStatusCompletedSubsidyTaskTabel(sessionBackingBean.getRequestNo(),
								paymentVoucherDTO.getServiceNo(), sessionBackingBean.getServiceRefNo(), "SM001", "C");
						disabledskipChargeTypes = true;

					} else {

						paymentVoucherService.updateVoucherDetails(paymentVoucherDTO, totalfee, logInUser, value,
								voucherDetails, false);
						commonService.updateTaskStatusCompleted(paymentVoucherDTO.getApplicationNo(), "PM300",
								sessionBackingBean.getLoginUser());
					}

					disableGenarate = true;

					disablePrint = false;
					disableAdd = true;
					disableClearTwo = true;

					sessionBackingBean.setIssueTenderMood(false);

					successMessage = "Voucher generated successfully.";
					sessionBackingBean.showMessage("Success", successMessage, "INFO_DIALOG");
					return;

				} else {
					errorMessage = "Voucher generate failed.";
					sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
					return;
				}
			} else {
				setErrorMessage("Already generate voucher.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {

			errorMessage = "Atleast one Charge Type should be added";
			sessionBackingBean.showMessage("Error", errorMessage, "ERROR_DIALOG");
			return;
		}

	}

	public void enableRePrint() {

	}

	public StreamedContent downloadFile(ActionEvent ae) throws JRException {

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//debitVoucher.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Parameter", value);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "PaymentVoucher - " + value + ".pdf");

			disableRePrint = false;
			disablePrint = true;
			RequestContext.getCurrentInstance().update("rePrint");

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

	public StreamedContent rePrint(ActionEvent ae) throws JRException {

		files = null;
		String sourceFileName = null;

		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();

			sourceFileName = "..//reports//debitVoucher.jrxml";

			// Parameters for report
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_Parameter", value);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "PaymentVoucher - " + value + ".pdf");

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

	public void closeTenderDialogBox() {

		clearOne();
		skipChargeTypes = false;
		RequestContext.getCurrentInstance().execute("PF('generateVoucher').hide()");
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

	public boolean isDisableChargeType() {
		return disableChargeType;
	}

	public void setDisableChargeType(boolean disableChargeType) {
		this.disableChargeType = disableChargeType;
	}

	public boolean isDisableAccountNo() {
		return disableAccountNo;
	}

	public void setDisableAccountNo(boolean disableAccountNo) {
		this.disableAccountNo = disableAccountNo;
	}

	public boolean isDisableAmount() {
		return disableAmount;
	}

	public void setDisableAmount(boolean disableAmount) {
		this.disableAmount = disableAmount;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getLogInUser() {
		return logInUser;
	}

	public void setLogInUser(String logInUser) {
		this.logInUser = logInUser;
	}

	public String getSelectingPermitNo() {
		return selectingPermitNo;
	}

	public BigDecimal getInput1() {
		return input1;
	}

	public void setInput1(BigDecimal input1) {
		this.input1 = input1;
	}

	public List<PaymentVoucherDTO> getDetailsList() {
		return detailsList;
	}

	public void setDetailsList(List<PaymentVoucherDTO> detailsList) {
		this.detailsList = detailsList;
	}

	public boolean isSearched() {
		return isSearched;
	}

	public void setSearched(boolean isSearched) {
		this.isSearched = isSearched;
	}

	public boolean isVoucherGenerate() {
		return isVoucherGenerate;
	}

	public void setVoucherGenerate(boolean isVoucherGenerate) {
		this.isVoucherGenerate = isVoucherGenerate;
	}

	public boolean isPhotoUpload() {
		return isPhotoUpload;
	}

	public void setPhotoUpload(boolean isPhotoUpload) {
		this.isPhotoUpload = isPhotoUpload;
	}

	public boolean isWasGenerated() {
		return wasGenerated;
	}

	public void setWasGenerated(boolean wasGenerated) {
		this.wasGenerated = wasGenerated;
	}

	public boolean isDisableGenarate() {
		return disableGenarate;
	}

	public List<PaymentVoucherDTO> getDeaprtmentList() {
		return deaprtmentList;
	}

	public void setDeaprtmentList(List<PaymentVoucherDTO> deaprtmentList) {
		this.deaprtmentList = deaprtmentList;
	}

	public void setDisableGenarate(boolean disableGenarate) {
		this.disableGenarate = disableGenarate;
	}

	public PaymentVoucherDTO getDto() {
		return dto;
	}

	public void setDto(PaymentVoucherDTO dto) {
		this.dto = dto;
	}

	public void setSelectingPermitNo(String selectingPermitNo) {
		this.selectingPermitNo = selectingPermitNo;
	}

	public boolean isDisableRePrint() {
		return disableRePrint;
	}

	public void setDisableRePrint(boolean disableRePrint) {
		this.disableRePrint = disableRePrint;
	}

	public String getSelectingApplicationNo() {
		return selectingApplicationNo;
	}

	public void setSelectingApplicationNo(String selectingApplicationNo) {
		this.selectingApplicationNo = selectingApplicationNo;
	}

	public boolean isDisableClearTwo() {
		return disableClearTwo;
	}

	public void setDisableClearTwo(boolean disableClearTwo) {
		this.disableClearTwo = disableClearTwo;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isDisableAdd() {
		return disableAdd;
	}

	public void setDisableAdd(boolean disableAdd) {
		this.disableAdd = disableAdd;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public BigDecimal getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(BigDecimal totalfee) {
		this.totalfee = totalfee;
	}

	public List<PaymentVoucherDTO> getVoucherDetails() {
		return voucherDetails;
	}

	public void setVoucherDetails(List<PaymentVoucherDTO> voucherDetails) {
		this.voucherDetails = voucherDetails;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<String> getAccountNoList() {
		return accountNoList;
	}

	public void setAccountNoList(List<String> accountNoList) {
		this.accountNoList = accountNoList;
	}

	public List<String> getChargeTypeList() {
		return chargeTypeList;
	}

	public void setChargeTypeList(List<String> chargeTypeList) {
		this.chargeTypeList = chargeTypeList;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public List<PaymentVoucherDTO> getTransactionTypeList() {
		return transactionTypeList;
	}

	public void setTransactionTypeList(List<PaymentVoucherDTO> transactionTypeList) {
		this.transactionTypeList = transactionTypeList;
	}

	public PaymentVoucherDTO getPaymentVoucherDTO() {
		return paymentVoucherDTO;
	}

	public List<PaymentVoucherDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<PaymentVoucherDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public boolean isPhotoUploadHistory() {
		return isPhotoUploadHistory;
	}

	public void setPhotoUploadHistory(boolean isPhotoUploadHistory) {
		this.isPhotoUploadHistory = isPhotoUploadHistory;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public void setPaymentVoucherDTO(PaymentVoucherDTO paymentVoucherDTO) {
		this.paymentVoucherDTO = paymentVoucherDTO;
	}

	public boolean isDiableApplicationNo() {
		return diableApplicationNo;
	}

	public void setDiableApplicationNo(boolean diableApplicationNo) {
		this.diableApplicationNo = diableApplicationNo;
	}

	public List<PaymentVoucherDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<PaymentVoucherDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public PaymentVoucherDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(PaymentVoucherDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public boolean isEditMood() {
		return editMood;
	}

	public void setEditMood(boolean editMood) {
		this.editMood = editMood;
	}

	public boolean isSkipChargeTypes() {
		return skipChargeTypes;
	}

	public void setSkipChargeTypes(boolean skipChargeTypes) {
		this.skipChargeTypes = skipChargeTypes;
	}

	public boolean isDisabledskipChargeTypes() {
		return disabledskipChargeTypes;
	}

	public void setDisabledskipChargeTypes(boolean disabledskipChargeTypes) {
		this.disabledskipChargeTypes = disabledskipChargeTypes;
	}

	public boolean isDisabledDelete() {
		return disabledDelete;
	}

	public void setDisabledDelete(boolean disabledDelete) {
		this.disabledDelete = disabledDelete;
	}

	public boolean isDisabledEdit() {
		return disabledEdit;
	}

	public void setDisabledEdit(boolean disabledEdit) {
		this.disabledEdit = disabledEdit;
	}

	public boolean isDisabledFiled() {
		return disabledFiled;
	}

	public void setDisabledFiled(boolean disabledFiled) {
		this.disabledFiled = disabledFiled;
	}

	public boolean isDisabledsearch() {
		return disabledsearch;
	}

	public void setDisabledsearch(boolean disabledsearch) {
		this.disabledsearch = disabledsearch;
	}

	public TenderDTO getTenderDTO() {
		return tenderDTO;
	}

	public void setTenderDTO(TenderDTO tenderDTO) {
		this.tenderDTO = tenderDTO;
	}

	public boolean isDisableModeOne() {
		return disableModeOne;
	}

	public void setDisableModeOne(boolean disableModeOne) {
		this.disableModeOne = disableModeOne;
	}

	public boolean isSisuSariya() {
		return isSisuSariya;
	}

	public void setSisuSariya(boolean isSisuSariya) {
		this.isSisuSariya = isSisuSariya;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

}
