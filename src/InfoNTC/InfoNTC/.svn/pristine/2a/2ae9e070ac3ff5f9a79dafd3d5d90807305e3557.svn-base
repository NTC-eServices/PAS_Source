package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CommittedOffencesDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;

public interface GrievanceManagementService {

	public String generateCIFNo(String complainType);

	public boolean updateParamSequence(String paramName);

	public List<DropDownDTO> getPriorityOrderList();

	public List<DropDownDTO> getSeverityList();

	public List<DropDownDTO> getComplaintMediaList();

	public List<CommittedOffencesDTO> getCommittedOffenceList();

	public long insertComplaintRequest(String permitAuth, String permitNo, String vehicleNo, String complainType,
			String complainNo, String priority, String severity, String route, String origin, String dest, String depot,
			String province, String eplace, String edatetime, String complaintMedia, String lang, String name,
			String name_si, String name_ta, String address1, String address1_si, String address1_ta, String address2,
			String address2_si, String address2_ta, String city, String city_si, String city_ta, String contact_no1,
			String contact_no2, boolean participation, boolean proof, String other, String details, String user,
			String serviceTypeDes);

	public void insertCommitedOffences(long complaintSeq, List<CommittedOffencesDTO> committedOffencesList,
			String user);

	// view,edit complaints
	public List<ComplaintRequestDTO> getComplaintDetails(String complaintNo, String vehicleNo, String permitNo);

	public List<ComplaintRequestDTO> getCommittedOffenceListByComplainSeqList(String vehicleNo, String permitNo,
			String complainNumber);

	public boolean updateComplaintRequest(ComplaintRequestDTO complaintDetailDTO, String user);

	public void updateCommitedOffences(ComplaintRequestDTO selectedComplaintDTO, long complaintSeq, List<CommittedOffencesDTO> committedOffencesList,
			String user);

	public List<String> getAllComplainNo();

	public List<ComplaintRequestDTO> getComplaintListByInvestigationDate(Date investigationFrom, Date investigationTo);

	public List<DropDownDTO> getDrivers();

	public List<DropDownDTO> getConductors();

	public DriverConductorRegistrationDTO getDriverConductorData(DriverConductorRegistrationDTO dcData);

	public List<DropDownDTO> getActionTypes();

	public List<CommittedOffencesDTO> getCommittedOffencesByComplaint(Long complaintSeq, boolean applicableOnly);

	public List<CommittedOffencesDTO> getCommittedOffencesById(Long complainSeq, String driverId, String conductorId);

	public List<ComplaintRequestDTO> getComplaintHistoryByOwner(String vehicleNo);

	public List<ComplaintRequestDTO> getComplaintHistoryByDriver(String driverId, String driverNic);

	public List<ComplaintRequestDTO> getComplaintHistoryByConductor(String conductorId, String conductorNic);

	public ComplaintRequestDTO getActionData(String viewType, ComplaintRequestDTO selectedComplaintDTO);

	public boolean saveActionData(String viewType, ComplaintRequestDTO selectedComplaintDTO, String user);

	public boolean saveUpdateInquiryProcess(ComplaintRequestDTO selectedComplaintDTO,
			DriverConductorRegistrationDTO driverData, DriverConductorRegistrationDTO conductorData, String user);

	public DriverConductorRegistrationDTO getComplaintDriver(String complaintNo);

	public DriverConductorRegistrationDTO getComplaintConductor(String complaintNo);

	public BigDecimal getOffenceCharge(String offenceCode);

	public boolean saveOffenceCharge(String driID, String driNic, String condID, String condNic, String complaintNo,
			CommittedOffencesDTO offence, String user);

	public List<DropDownDTO> getApplicationNos();

	public CommittedOffencesDTO getDriverConductorPoints(String complaintNo, CommittedOffencesDTO c);

	public BigDecimal getDriverPointsByComplaint(String complaintNo, String driverNic);

	public BigDecimal getConductorPointsByComplaint(String complaintNo, String conductorNic);

	public List<DropDownDTO> getApplicationNoByTrainingType(String trainingType);

	public List<DropDownDTO> getDriversByTrainingType(String type);

	public List<DropDownDTO> getConductorsByTrainingType(String type);

	public List<DropDownDTO> getInquiryDrivers();

	public List<DropDownDTO> getInquiryConductors();

	public boolean updateComplaintStatus(ComplaintRequestDTO selectedComplaintDTO, String string, String user);

	public void sendComplaintRefSMS(String contactNo, String message, String subject);

	public String checkDataIntable(String complainNo);

	/** For Voucher Creation in Grievance */
	public String generateVoucherNOForChrgeSheet();

	public void insertVoucherDetails(String voucher, ComplaintRequestDTO selectedComplaintDTO, String user,
			BigDecimal totalAmt);

	public String getExisteingVoucherNo(String complainNo);

	/** Finished **/

	/** For Voucher Approval in Grievance **/
	public List<String> getComplainNoPendingApprove();

	public List<String> getVoucherNoList();

	public ComplaintRequestDTO getValuesByComplainNo(String compliantNo);

	public List<String> getvoucherNoListByComplaintNo(String complaintNo);

	public List<ComplaintRequestDTO> getPaymentDetailsOnGrid(String compliantNo);

	public ComplaintRequestDTO getVoucherSatus(String voucherNo);

	public boolean updateApproveRejectVoucher(String voucheNo, String status, String reason, String approvedBy);

	public List<ComplaintRequestDTO> showUser();

	public List<String> getReceiptNoList();

	/** for receipt generation in Grievance **/
	public List<String> getApprovedVoucherNoList();

	public List<String> complaintNoList();

	public ComplaintRequestDTO showDataByVoucherOrComplaintNo(String string1, String string2);

	public List<ComplaintRequestDTO> getGenerateReceiptListForGrievance(ComplaintRequestDTO dto);

	public List<ComplaintRequestDTO> getPendingGenerateReceiptListForGrievance();

	public int getTotalAmount(String voucherNo);

	public boolean isReceiptgeneratedForGrievance(String voucherNo);

	public ComplaintRequestDTO getReceiptDetailsForGrievance(String voucherNo);

	public boolean isPrintCompletedForGrievance(String voucherNo);

	public List<ComplaintRequestDTO> getVoucherDetailsListForGrievance(String complaintNo);

	public void saveReceiptForGrievance(ComplaintRequestDTO selectedRow, String loginUser);

	public String getReceiptNoForPrintForGrievance(String voucherNo);

	public String getReceiptNoForPrintForGrievanceByComplaintno(String complaintNo);

	public boolean updateParamSequenceN(String paramName);

	public boolean checkDataInChargeSheetTable(String compliantNo);

	public boolean updateChargeSheetVoucher(ComplaintRequestDTO selectedComplaintDTO, String user, BigDecimal totalAmt);

	public void updateInquiryRecommendation(String remark, String status, String user, String complainNo);

	public void updateInquiryApproval(String remark, String status, String user, String complainNo);

	public void complainRequestHistory(String complainNo);

	public List<ComplaintRequestDTO> getComplaintDetailsByStatusAndGrantPermission(String complaintNo, String vehicleNo,
			String permitNo);

	public void updateGrantPermission(String remark, String status, String user, String vehicleNo);

	public void grantPermission(String complainNo, String vehicleNo, String permitNo, String loginUser,
			String specialRemark);
	void reduceDemeritPointsFromTotalPoints(String driverId ,String conductorId,BigDecimal driverDemeritPoints,BigDecimal conductorDemeritPoints,ComplaintRequestDTO selectedComplaintDTO,String user);
	
	 List<CommittedOffencesDTO> getCommittedOffenceListForPublicComplaint();

	void beanLinkMethod(ComplaintRequestDTO selectedComplaintDTO,String user,String des,String funDes);

	void complainRequestHistory(ComplaintRequestDTO selectedComplaintDTO, String complainNo, String user);


	void beanLinkMethod(String user, String busNo, String complaintNo, String permitNo,String funDes,String status);

	

	
}
