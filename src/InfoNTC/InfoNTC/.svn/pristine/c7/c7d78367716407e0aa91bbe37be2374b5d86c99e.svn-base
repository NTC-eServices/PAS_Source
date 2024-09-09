package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.SimRegistrationDTO;

/**
 * @author viraj.k Modified By: dinushi.r , tharushi.e
 *
 */
public interface SimRegistrationService {

	public String generateSimRegNo();

	public void setGeneratedSimRegNo(String number);

	public List<SimRegistrationDTO> getPermitBusByService(String serviceType);

	public List<String> getPermitNoByService(String serviceType);

	public List<String> getBusNoByService(String serviceType);

	public String getBusNoByPermitNo(String service, String permitNum);

	public String getPermitNoByBusNo(String service, String permitNum);

	public void addSimRegistration(SimRegistrationDTO simRegistrationDTO);

	public void addEmi(SimRegistrationDTO emiDTO);

	public void updateEmi(SimRegistrationDTO emiDTO);

	public void updateSimRegistration(SimRegistrationDTO simRegistrationDTO);

	public void updateSimTransfer(SimRegistrationDTO simRegistrationDTO);

	public List<SimRegistrationDTO> getChargeDetailsList();

	public void addVoucher(SimRegistrationDTO voucherDTO, List<SimRegistrationDTO> voucherDTOList);

	public void updateVoucher(SimRegistrationDTO simRegistrationDTO, List<SimRegistrationDTO> voucherDTOList);

	public String generateVoucherNo();

	public void deleteEmiDetails(String simRegNo);

	public boolean validateExistBusPermit(SimRegistrationDTO simRegistrationDTO);

	public String getChargeTypeCodeByDescription(String desc);

	/** SIM REgistration Voucher Approval **/

	public List<SimRegistrationDTO> simRegNoList();

	public List<SimRegistrationDTO> pendingList(String simRegNo, String simNo, String simRenewNo, String busnO,
			String permitNo, String voucherNo, String status);

	public SimRegistrationDTO filterDTO(String simRegNo, String simNo, String simRenewNo, String busnO, String permitNo,
			String voucherNo);

	public String updateApproveRejectVoucher(String voucherNo, String status, String reason, String loginUser);

	public SimRegistrationDTO updateStatusType(String statusType, String simRegNo);

	/** End SIM REgistration Voucher Approval **/

	/** SIM Registration Receipt Generation **/
	public List<SimRegistrationDTO> getApprovedVouNoList();

	public int getTotalAmount(String voucherNo);

	public boolean isReceiptgeneratedForInves(String voucherNo);

	public SimRegistrationDTO getReceiptDetailsForInves(String voucherNo);

	public List<SimRegistrationDTO> pendingDTO(String simRegNo, String simNo, String simRenewNo, String busnO,
			String permitNo, String voucherNo, String status);

	public void saveReceiptForSim(SimRegistrationDTO selectedRow, String loginUser);

	public String updateReceiptNo(String voucherNo, String receiptNo);

	public String getReceiptNo(String voucherNO);

	public List<SimRegistrationDTO> searchedList(String simRegNo, String simNo, String simRenewNo, String busnO,
			String permitNo, String voucherNo, String status);

	public String activeSim(String status, String simRegno, String receiptNo);

	/** End SIM Registration Receipt Generation **/
	public void updateVoucherPrintReprintStatus(SimRegistrationDTO simRegistrationDTO, String isPrint,
			String isRePrint);

	public int checkDuplicatePermit(String permitNo);

	public int checkRegNoStatus(String regNo);

	public void insertInHistory(String voucherNO);

	public List<SimRegistrationDTO> getOldPermitDetails(String permitNo);

	public boolean insertSimAndEmiDetails(SimRegistrationDTO simRegistrationDTO, List<SimRegistrationDTO> emiDTOList);

	public boolean updateSimAndEmiDetails(SimRegistrationDTO simRegistrationDTO, List<SimRegistrationDTO> emiDTOList);

}
