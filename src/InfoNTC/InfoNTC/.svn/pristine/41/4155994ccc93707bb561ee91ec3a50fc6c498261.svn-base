package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.util.List;

import lk.informatics.ntc.model.dto.AdvancedPaymentDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;

public interface PaymentVoucherService {

	public List<PaymentVoucherDTO> getTranactionType();

	// for transaction Report
	public List<PaymentVoucherDTO> getTranactionTypeForRpt();

	public boolean checkApplicationNo(PaymentVoucherDTO dto);

	public List<String> getChargeType();

	public List<String> getAccountNo(String code, PaymentVoucherDTO dto);

	public String generateReferenceNo();

	public String getTranCode(PaymentVoucherDTO dto);

	public List<PaymentVoucherDTO> getVoucherDetails(String description);

	public List<PaymentVoucherDTO> getGeneratedVoucherDetails(String value);

	public String getApplicationNo(String permitNo);

	public String getPermitNo(String ApplicationNo);

	public boolean checkPermitNumber(String permitNo);

	public boolean checkApplicationNumber(String ApplicationNo);

	public boolean checkTenderApplicationNumber(String ApplicationNo);

	public boolean generateVoucher(PaymentVoucherDTO paymentVoucherDTO, String loginUser, BigDecimal totalAmount,
			String voucherNo, boolean isTender, boolean isSkipVoucher, boolean isSiSuSariya);

	public boolean checkPhotoUploadTaskDetails(String applicatinNo);

	public boolean checkPhotoUploadHistory(String applicatinNo, String taskStatus);

	public boolean isTenderVoucherGenerated(String applicationNo);

	public boolean alreadyGenerate(String applicationNo, String loginUser);

	public boolean updateVoucherDetails(PaymentVoucherDTO dto, BigDecimal totalFee, String logingUser, String vocherNo,
			List<PaymentVoucherDTO> voucherDetails, boolean isSisuSariya);

	public boolean updateTenderApplicant(PaymentVoucherDTO paymentDetails, String value);

	public Long getSeqNo(String applicationNo);

	public String getChargeCode(String chargeDes);

	public int insertPaymentVoucher(PaymentVoucherDTO paymentVoucherDTO, String logInUser, BigDecimal totalAmount,
			List<AdvancedPaymentDTO> paymentDetails);

	public List<CommonDTO> GetTransactionToDropdown();

	public boolean checkTaskDetails(PaymentVoucherDTO dto, String taskCode, String status);

	public boolean insertTaskDetails(PaymentVoucherDTO dto, String loginUSer, String taskCode, String status);

	public boolean deleteTaskDetails(PaymentVoucherDTO dto, String taskCode);

	public boolean CopyTaskDetailsANDinsertTaskHistory(PaymentVoucherDTO dto, String loginUSer, String taskCode);

	public boolean updateTaskDetails(PaymentVoucherDTO dto, String taskCode, String status);

	public boolean changeTaskDetails(PaymentVoucherDTO dto, String oldTaskCode, String nretaskCode, String status,
			String preStatus);

	public List<PaymentVoucherDTO> getDepartment();

	public List<PaymentVoucherDTO> getUnit();

	public List<PaymentVoucherDTO> getReceiptNo();

	public List<String> getVoucherNo();

	public List<PaymentVoucherDTO> getApplicationNo();

	public List<PaymentVoucherDTO> getPaymentDetails(PaymentVoucherDTO dto);

	public List<PaymentVoucherDTO> getApprovedPaymentDetails(PaymentVoucherDTO dto);

	public List<PaymentVoucherDTO> getAdvancePaymentDetails(PaymentVoucherDTO dto);

	public List<PaymentVoucherDTO> getVoucherPaymentDetails(PaymentVoucherDTO dto);

	public boolean isPaymentApprove(PaymentVoucherDTO dto);

	public boolean isPaymentReject(PaymentVoucherDTO dto);

	public boolean approvePayment(PaymentVoucherDTO dto);

	public boolean rejectPayment(PaymentVoucherDTO dto);

	public boolean isVoucherPayment(PaymentVoucherDTO dto);

	public boolean saveAdvanceRemark(PaymentVoucherDTO dto);

	public boolean checkDepartment(PaymentVoucherDTO dto);

	public boolean checkTransaction(PaymentVoucherDTO dto);

	public String getAppNoforVoucher(String voucherNo, String paymentType);

	public List<AdvancedPaymentDTO> getAdvancedPaymentDet(String voucherNo);

	public BigDecimal getTotAmtforVoucher(String voucherNo);

	public List<PaymentVoucherDTO> getVoucherPaymentDet(String voucherNo);

	public boolean checkTaskHistory(PaymentVoucherDTO dto, String taskCode, String status);

	public List<PaymentVoucherDTO> getApplicationNoUsingTranactionType(PaymentVoucherDTO dto, String type);

	public List<PaymentVoucherDTO> getPermitNoUsingTranactionType(PaymentVoucherDTO dto, String type);

	public String getAccountNoForEdit(String code);

	public boolean checkVehicleNumber(String VehicleNo);

	public boolean CopyTaskDetailsANDinsertTaskHistorySubsidy(PaymentVoucherDTO dto, String loginUSer, String taskCode);

	public boolean changeTaskDetailsSubsidy(PaymentVoucherDTO dto, String oldTaskCode, String nretaskCode,
			String status);

	public boolean checkSisuSariyaApplicationNumber(String ApplicationNo);

	public boolean isReceiptGenerated(PaymentVoucherDTO dto);

	public boolean checkPowerOfAttorney(String permitNo);

}
