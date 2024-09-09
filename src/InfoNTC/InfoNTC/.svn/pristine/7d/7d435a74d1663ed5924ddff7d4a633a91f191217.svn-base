package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.ManageInquiryDTO;
import lk.informatics.ntc.model.dto.SimRegistrationDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;

public interface ManageInquiryService {

	public VehicleInspectionDTO retrieveVehicleInfo(String vehicleNum);

	public List<String> retrieveComplaintNumbers();

	public List<ManageInquiryDTO> retrieveSeverity();

	public List<ManageInquiryDTO> retrievePriorityOrderList();

	public List<ManageInquiryDTO> retrieveInquiryComplaintData(ManageInquiryDTO manageInquiryDTO, String user);

	public List<ManageInquiryDTO> retreiveReservedTimes(String selectedDate);

	public void updateInvetigationData(ManageInquiryDTO selectedData, String startTime, String endTime,
			String loginUser);

	public void deleteInvetigationDataAndTime(ManageInquiryDTO selectedData, String startTime, String endTime,
			String loginUser);

	public List<ManageInquiryDTO> retrieveBusDriverConductorData(String vehicleNum, String permitNum);

	public String retrieveBusDriverConductorAddress(String contactName);

	public boolean savePrintLetterData(ManageInquiryDTO sendMailDTO, String loginUser);

	public boolean checkLetterDataExist(String contactName);

	public ManageInquiryDTO retrieveLetterDate(String contactName);

	public String retrieveBusDriverConductorContactNumber(String contactName);

	public boolean checkSMSDataExist(String contactNumber);

	public ManageInquiryDTO retrieveSMSDate(String contactNumber);

	public boolean saveSMSData(ManageInquiryDTO sendMailDTO, String loginUser);

	public ComplaintRequestDTO getComplaintDetails(String complaintNo, String vehicleNo, String permitNo);

	public ComplaintRequestDTO getComplaintDetailsByComplainNo(String complainNo);

	public void sendMail(ManageInquiryDTO sendMailDTO, String loginUser);

	public ManageInquiryDTO retrieveSMSDetails(String complainNo);

	public boolean checkDataAvailableInNt_r_investigation_alerts(String complainNo, boolean letter);

	public ManageInquiryDTO retrieveLetterDetails(String complainNo);

	public List<ComplaintRequestDTO> getComplaintDetailsForPublicComplain(String permitNo);

	public List<FlyingManageInvestigationLogDTO> getInvestigationDetails(String permitNo);

	public SimRegistrationDTO getGPSDetails(String permitNo);

	public List<SimRegistrationDTO> getEmiDetails(String permitNo);

	public boolean updateComplaintStatus(ManageInquiryDTO complaint, String status, String user);

	public boolean chekDataFromReport(String complainNo);

	public List<String> retrieveVehicleNumbers();

	public String getOffenceInSinhala(String complainNo);

	public List<String> retrieveComplaintNumbersForManageInquiry(ManageInquiryDTO dto);

	public boolean checkGrievanceTask(String complainNo, String taskCode, String taskStatus);

	public boolean sendPtaCtbMail(List<ManageInquiryDTO> sendMailDTOList, String loginUser,ManageInquiryDTO selectedData);

	public List<ManageInquiryDTO> retrieveActionDepartmentsList();

	public boolean assignToActionDepartment(ManageInquiryDTO selectedData, List<ManageInquiryDTO> complaintDTOList, String loginUser);

	public List<ManageInquiryDTO> getActionDetails(String complainNo);
	
	public boolean chekDataFromReportForTP(String complainNo);

	List<String> retrieveComplaintNumbersForManageInquiry();

	boolean insertGrievanceTask(ManageInquiryDTO complaint, String loginUser, String taskCode, String taskStatus);

	void beanLinkMethod(ManageInquiryDTO selectedData, String user, String des, String funDes);
	
	public String getApplicationCurrentStatus(String chargeRef);
	
	public String getStatusDesc(String status);
	
}