package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.AdvancedPaymentDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.GenerateReceiptDTO;
import lk.informatics.ntc.model.dto.PaymentVoucherDTO;

public interface GenerateReceiptService {

	public List<CommonDTO> counterdropdown();

	public void counterStatus(String counterId, String user);

	public List<GenerateReceiptDTO> getTransactionType();

	public List<GenerateReceiptDTO> getDepartment();

	public List<GenerateReceiptDTO> getUnit();

	public List<GenerateReceiptDTO> getReceiptNo();

	public List<GenerateReceiptDTO> getVoucherNo();

	public List<GenerateReceiptDTO> getVoucherNo(String transTypeCode);

	public List<GenerateReceiptDTO> getApplicationNo();

	public List<GenerateReceiptDTO> getPaymentMode();

	public List<GenerateReceiptDTO> getBankName();

	public List<GenerateReceiptDTO> getBranchName();

	public List<GenerateReceiptDTO> getBranchName(String bankCode);

	public List<GenerateReceiptDTO> getGenerateReceiptList(GenerateReceiptDTO generateReceiptDTO);

	public List<AdvancedPaymentDTO> getAdvancedPaymentDetailsList(String voucherNo);

	public List<PaymentVoucherDTO> getVoucherDetailsList(String voucherNo);

	public boolean checkDepartment(GenerateReceiptDTO dto);

	public boolean checkTransaction(GenerateReceiptDTO dto);

	public void saveReceipt(GenerateReceiptDTO selectedRow, String loginUser);

	public void updateTaskStatus(String applicationNo, String user);

	public void updateTenderTaskStatus(String applicationNo, String user);

	public void updateSisuSariyaTaskStatus(String applicationNo, String user,String serviceNo);

	public boolean isReceiptgenerated(String voucherNo);

	public String getReceiptNoForPrint(String voucherNo);

	public boolean isPrintCompleted(String voucherNo);

	public void rePrintUpdate(String voucherNo);

	public void printUpdate(String voucherNo);

	public GenerateReceiptDTO getApplicationDetailsByQueueNo(String queueNo);

	public int getParaTotalAmount();

	public GenerateReceiptDTO getReceiptDetails(String voucherNo);

	public GenerateReceiptDTO getSearchFields(String voucherNo);

	/**
	 * Driver Conductor Receipt Generator
	 */
	public List<DriverConductorRegistrationDTO> getVoucherNoListForDc(); // only approved vouchers

	public List<DriverConductorRegistrationDTO> getApplicationNoListForDC();// only approved vouchers

	public List<DriverConductorRegistrationDTO> getDriverConductorIdListForDC();// only approved vouchers

	public DriverConductorRegistrationDTO getValuesAccordingtoVoucherNo(String voucherNO);

	public DriverConductorRegistrationDTO getValuesAccordingToApplicationNo(String appNo);

	public DriverConductorRegistrationDTO getValuesAccordingToDcIdNo(String dcIdNo);

	public List<DriverConductorRegistrationDTO> getGenerateReceiptListForDriverConductor(
			DriverConductorRegistrationDTO dcDTo);

	public boolean isReceiptgeneratedForDriverConducctor(String voucherNo);

	public boolean isPrintCompletedForDriverConductor(String voucherNo);

	public List<DriverConductorRegistrationDTO> getVoucherDetailsListForDriverCOnductor(String voucherNo);

	public void saveReceiptForDriverCOnductor(DriverConductorRegistrationDTO selectedRow, String loginUser,String statusType, String status, String appNo);

	public DriverConductorRegistrationDTO getReceiptDetailsForDriverConductor(String voucherNo);

	public String getReceiptNoForPrintForDriverConductor(String voucherNo);

	public List<DriverConductorRegistrationDTO> getpendingReceiptListForDriverConductor();
	/*
	 * End
	 */

}
