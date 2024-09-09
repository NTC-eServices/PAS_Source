package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;

public interface PermitService extends Serializable {
	public List<PermitDTO> getAppNoToDropdown();

	public List<VehicleInspectionDTO> getServiceTypeToDropdown();

	public List<String> getApplicationNo();

	public List<PermitRenewalsDTO> getAppNo();

	public List<PermitRenewalsDTO> getAppNoForViewPermitRen();

	public List<String> getPermitNo();

	public List<String> getTaskPermitNo();

	public List<String> getPermit();

	public String getBusRegNo(String applicationNO, String PermitNo, String queueNo);

	public String BusRegNoUsingPermitNo(String PermitNo);

	public String completeApplicationNo(String permitNo, String queueNO);

	public String completePermitNo(String applicationNo, String queueNO);

	public boolean checkQueueNo(String queueNo);

	public String completeQueueNo(String applicationNo, String permitNo);

	public String completeDate(String applicationNo, String permitNo, String queueNO);

	public List<PermitDTO> getData(PermitDTO permitDTO);

	public List<PermitDTO> getApprovedData(PermitDTO permitDTO);

	public boolean isReceiptGenerated(PermitDTO permitDTO);

	public boolean checkTaskPR200(String applicationNO);

	public boolean checkTaskDetails(PermitDTO dto, String taskCode, String status);

	public boolean insertTaskDetails(PermitDTO dto, String loginUSer, String taskCode, String status);

	public boolean deleteTaskDetails(PermitDTO dto, String taskCode);

	public boolean updateTaskDetails(PermitDTO dto, String taskCode, String status);

	public boolean CopyTaskDetailsANDinsertTaskHistory(PermitDTO dto, String loginUSer, String taskCode);

	public boolean insertTaskHistory(PermitDTO dto, String loginUSer, String taskCode, String status);

	public boolean isPermitApprove(PermitDTO dto);

	public boolean isPermitReject(PermitDTO dto);

	public boolean rejectPermit(PermitDTO dto, PermitDTO dto2);

	public boolean approvePermit(PermitDTO dto, String user);
	public boolean approvePermitAndTaskUpdateAndInsertHistory(PermitDTO dto, String user,String taskCode,PermitRenewalsDTO pdto);
	public void updateTaskPM201();

	public boolean checkOwnerDetails(PermitDTO dto);

	// public List<PermitDTO> search(PermitDTO permitDTO) ;

	// added
	public List<PermitDTO> search(PermitDTO permitDTO);

	public List<String> getTenderRefNoToDropdown();

	public String fillAppNo(String permitNo, String queueNo, String tenderRefNo);

	public String fillQueueNo(String applicationNo);

	public String fillTenderNumber(String permitNo, String applicationNo, String queueNo);

	public String fillServiceType(String permitNo, String applicationNo, String queueNo, String tenderRefNo);

	public String fillRegNo(String applicationNo);

	public String fillPermitNo(String applicationNo);

	public List<PermitDTO> showDetails(PermitDTO permitDTO);

	// added for reject user and approve
	public void rejectUser(String string, String rejectReason);

	public String getPmStatus(String applicationNo);

	public void updatePmStatus(String string);

	// update permit No
	public void updatePermitNo(PermitDTO permitDTO);

	// update Print
	public void updatePrintStatus(String string);

	public String getTaskStatus(String applicationNo);
	// update PM201 in nt)t_task_det

	public void insertTaskDet(String string, String string1, String loginUSer);

	public void updatehistory(String string, String string1, String loginUSer);

	// for backing Log Transaction form
	public List<String> showPermitNo(String userId);

	public List<PermitDTO> showAppNo(String userId);

	public List<EmployeeDTO> showUser();

	public List<String> getVehicleNoDropDowm(String userId);

	public String fillAppliNo(String permitNo, String busRegNo);

	public String fillVehicleNo(String permitNo, String applicationNo);

	public String fillPermit(String applicationNo, String busRegNo);

	// for View Permit renewal form
	public PermitDTO showData(String permitNo, String applicationNo, String queueNo);

	public PermitRenewalsDTO showDataFromVehiOwner(String permitNo, String applicationNo);

	public List<String> getTaskApplicationNo();

	public List<PermitRenewalsDTO> getPermitNoForCurrentAppNo(String currentApplicationNo);

	public String checkTaskForPayment(String applicationNo);

	public String checkTaskForReprint(String applicationNo);

	public PermitDTO getLoadValuesForSelectedOne(String permitNo, String applicationNo, String queueNo,
			String busRegNo);

	PermitDTO ManageAssignedFunction(String logedUser);

	public boolean isVoucherGenerated(PermitDTO permitDTO);

	public boolean isPaymentApprove(String applicationNo);

	public String getVoucherNo(String applicationNo);

	public boolean changePermitRenewalPeriod(PermitDTO dto, String user);

	public List<PermitRenewalsDTO> getPermitNoList();

	public List<PermitDTO> getActivePermitList();

	public boolean updateExpireDateByNewExpireDate(PermitDTO dto, String user); // added by tharushi.e for update
																				// pm_expire_date by pm_new_expire_date

}
