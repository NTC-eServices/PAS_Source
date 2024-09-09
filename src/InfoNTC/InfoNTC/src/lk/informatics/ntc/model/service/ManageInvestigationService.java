package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CommittedOffencesDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.ManageInvestigationDTO;

public interface ManageInvestigationService {

	List<ManageInvestigationDTO> getChargeRefNoList();

	List<ManageInvestigationDTO> searchInvestigationDetailsByDate(Date investigationFrom, Date investigationTo);

	List<ManageInvestigationDTO> searchInvestigationDetails(ManageInvestigationDTO searchDTO);

	DriverConductorRegistrationDTO getDriverConductorData(DriverConductorRegistrationDTO dcData);

	List<DropDownDTO> getDrivers();

	List<DropDownDTO> getConductors();

	public List<ManageInvestigationDTO> getInvestigationChargesByRef(ManageInvestigationDTO selectedInvestigation,String chargeRefNo);

	List<DropDownDTO> getAttempts();

	List<DropDownDTO> getActions();

	boolean saveChargeSheet(ManageInvestigationDTO chargeFinalization, List<ManageInvestigationDTO> chargesList);

	boolean saveInvestigationMaster(ManageInvestigationDTO selectedInvestigation, BigDecimal latePayment);

	boolean updateCurrentStatus(ManageInvestigationDTO selectedInvestigation, String chargeRef, String status, String user,String des);

	String getApplicationStatus(String chargeRef);

	List<ManageInvestigationDTO> getChargeSheetByChargeRef(ManageInvestigationDTO selectedInvestigation, String chargeRef);

	boolean updateAppStatus(ManageInvestigationDTO selectedInvestigation, String chargeRef, String status, String user);

	ManageInvestigationDTO getChargeSheetMaster(ManageInvestigationDTO chargeDTO);

	String getStatusDesc(String status);

	String createVoucher(ManageInvestigationDTO selectedInvestigation);

	boolean approveVoucher(String voucherNo, String user);

	boolean rejectVoucher(String voucherNo, String user, String rejectReason);

	String getApplicationCurrentStatus(String chargeRef);

	BigDecimal getChargeTotAmount(String chargeRef);

	boolean updateAction(String category, String chargeRef, String department, String actionType, String actionDesc);

	boolean saveDriverConductorDet(ManageInvestigationDTO selectedInvestigation,
			DriverConductorRegistrationDTO driverData, DriverConductorRegistrationDTO conductorData);

	DriverConductorRegistrationDTO getConfirmedDriver(ManageInvestigationDTO selectedInvestigation,String chargeRef);

	DriverConductorRegistrationDTO getConfirmedConductor(ManageInvestigationDTO selectedInvestigation,String chargeRef);

	/** added for receipt generation **/
	public List<ManageInvestigationDTO> approvedVoucherForInves();

	public List<ManageInvestigationDTO> approvedChargeRefNO();

	public ManageInvestigationDTO showDataByVoucherOrChargeSheetNo(String voucherNo, String chargeRefNo);

	public boolean isReceiptgeneratedForInves(String voucherNo);

	public ManageInvestigationDTO getReceiptDetailsForInves(String voucherNo);

	public List<ManageInvestigationDTO> getVoucherDetailsListForInves(String chargeReffNo);

	public void saveReceiptForInvestigation(ManageInvestigationDTO selectedRow, String loginUser);

	public String getReceiptNoForPrintForInvestigation(String voucherNo);
	
	public void beanLinkMethod(ManageInvestigationDTO selectedInvestigation,String des);

	/** end **/

	boolean checkDriverConductorDet(ManageInvestigationDTO selectedInvestigation);

	String getServiceTypeDesc(String code);

	boolean updateVoucherStatus(ManageInvestigationDTO selectedInvestigation, String refNo, String status, String user);
	
	public boolean updateCurrentStatus(String chargeRef, String status, String user);

	List<ManageInvestigationDTO> getPendingGenerateReceiptListForInves();
	List<ManageInvestigationDTO> getInvestigationChargesByRefAndAttempt(String chargeRefNo,String attempt);
	BigDecimal getAmountPerAttempt(String offenceCode,String attemptCode);
	BigDecimal getLatePaymentFeeByVoucherNumer(String voucherNumber);

	List<ManageInvestigationDTO> searchDataForCommonInquiry(String refNo, String busNo, String permitNo,String offence);

	List<ManageInvestigationDTO> getRefNoListForCommonInquiry();

	List<ManageInvestigationDTO> getBusNoListForCommonInquiry();

	List<ManageInvestigationDTO> getPermitNoListForCommonInquiry();

	List<ManageInvestigationDTO> getOffNameListForCommonInquiry();
	
	public List<ComplaintRequestDTO> getComplaintHistoryByOwner(String vehicleNo);
	
	public List<ComplaintRequestDTO> getComplaintHistoryByDriver(String driverId, String driverNic);
	
	public List<ComplaintRequestDTO> getComplaintHistoryByConductor(String conductorId, String conductorNic);
	
	public BigDecimal getOffenceCharge(String offenceCode);
	
	public List<CommittedOffencesDTO> getCommittedOffencesById(Long complainSeq, String driverId, String conductorId);

	public List<CommittedOffencesDTO> getViolationHistory(String complaintNo);

	

}
