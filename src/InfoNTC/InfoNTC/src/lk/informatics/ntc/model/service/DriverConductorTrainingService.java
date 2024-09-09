package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.CommonInquiryDTO;
import lk.informatics.ntc.model.dto.DriverConductorBlacklistDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.DriverConductorTrainingDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.dto.MaintainTrainingScheduleDTO;
import lk.informatics.ntc.model.dto.PermitDTO;

public interface DriverConductorTrainingService {

	public List<CommonDTO> GetGenderToDropdown();

	public List<CommonDTO> GetTrainingTypesByMode(String strMode);

	public List<PermitDTO> getActivePermitList();

	public boolean insertDriverConductorReg(DriverConductorRegistrationDTO dto);

	public String retrieveLastNoForNumberGeneration(String code);

	public DriverConductorRegistrationDTO insertDriverConductorRegNew(DriverConductorRegistrationDTO dto, String user);

	public boolean updateDriverConductorRegistration(DriverConductorRegistrationDTO driverConductorRegistrationDTO, String user);

	public List<CommonDTO> getVehicleDetails(String vehicleNo);

	public List<CommonDTO> GetAllTrainingTypesToDropdown();

	public String checkDuplicateNICforSameTraining(String nicNo, String traingType);


	/*********/
	// Edit View
	/*********/
	public List<CommonDTO> getPendingIDList();

	public List<CommonDTO> getPendingIDListByTrainingType(String trainingType);

	public List<CommonDTO> getPendingDCIDList();

	public List<CommonDTO> getPendingDCIDListByTrainingType(String trainingType);

	public List<CommonDTO> getPendingDCIDListByTrainingandID(String trainingType, String idNo);

	public DriverConductorRegistrationDTO getDetailsByDCId(String strDCId);

	public boolean updateDriverConductorRegistrationEV(DriverConductorRegistrationDTO driverConductorRegistrationDTO,
			String strSelectedType, String loginUser);

	public List<CommonDTO> GetAllTrainingTypes();

	public DriverConductorRegistrationDTO getDetailsByDCIdandType(String strDCId, String strType);

	/**
	 * Re-Register Form
	 */
	public List<CommonDTO> GetTrainingTypesByModeForRe(String strMode);

	public List<DriverConductorRegistrationDTO> getNicNumberList();

	public List<DriverConductorRegistrationDTO> getDCIDListByID(String id);

	public DriverConductorRegistrationDTO insertDriverConductorReReg(DriverConductorRegistrationDTO dto);

	public String getOldAppNo(String driverConducID, String nicNo);

	public String getChargeTypeDes(String code);

	public List<DriverConductorRegistrationDTO> getDriverConducFilterList(String trainingType);
	/**
	 * end
	 */

	/******************/
	// Maintain Training
	/******************/
	public List<CommonDTO> GetAllMonths();

	public String insertTrainingSchedule(MaintainTrainingScheduleDTO dto, String loginUser);

	public List<MaintainTrainingScheduleDTO> getScheduleDetails();

	public boolean updateTrainingSchedule(MaintainTrainingScheduleDTO dto, String loginUser);

	/***********************/
	// Setup Temporary Period
	/** *********************/
	public String getNamebyNICOrDCId(String nicNo, String idNo);

	public List<DriverConductorRegistrationDTO> getPaymentCompletedDC();

	public boolean InsertDCTempPeriod(DriverConductorRegistrationDTO dcDto, String user,
			List<DriverConductorRegistrationDTO> dcTempPeriodList);

	public List<CommonDTO> getTempANICList();

	public List<CommonDTO> getTempANICListByTrainingType(String trainingType);

	public List<CommonDTO> getTempDCIDListByTrainingandID(String trainingType, String idNo);

	public List<DriverConductorRegistrationDTO> getsearchDataByEnterdVal(String trainingType, String nicNo, String dcId,
			String name);

	public boolean updateDCTempPeriod(DriverConductorRegistrationDTO dcDto, String user);

	/**
	 * Driver Conductor Payment voucher / approve Voucher
	 */
	public List<DriverConductorRegistrationDTO> getTrainingTypeList();

	public List<DriverConductorRegistrationDTO> getAppNolistForselectedTraining(String trainingType);

	public List<DriverConductorRegistrationDTO> getIdNoListForSelectedAppNo(String trainingType, String appNo);

	public DriverConductorRegistrationDTO getDriverConducByID(String appNo, String idNo);

	public List<DriverConductorRegistrationDTO> getChargeTypeDetails(String trainingType);

	public List<DriverConductorRegistrationDTO> getChargeType();

	public DriverConductorRegistrationDTO getAccountAndAmountbyChargeType(String chargeType);

	public boolean alreadyGenerate(String appNo);

	public String generateVoucherNo();

	public boolean insertVoucherDetInMaster(DriverConductorRegistrationDTO driverConducDTO, String loginUSer,
			String value, BigDecimal totalfee);

	public boolean insertVoucherDetailsInDetTable(DriverConductorRegistrationDTO driverDTO,
			List<DriverConductorRegistrationDTO> chargeTypeDetList, String loginUser, String voucherNo);

	public List<DriverConductorRegistrationDTO> getAppNoForApproveByTrainingType(String trainingCode);

	public List<DriverConductorRegistrationDTO> getVoucherNoList();

	public List<DriverConductorRegistrationDTO> getPaymentDetailsOnGrid(DriverConductorRegistrationDTO dcDto);

	public DriverConductorRegistrationDTO getVoucherSatus(String voucherNo);

	public String updateApproveRejectVoucher(String voucherNo, String status, String reason, String loginUser);

	public List<DriverConductorRegistrationDTO> getPaymentDetailsOnGridBySearch(DriverConductorRegistrationDTO dcDto);

	public DriverConductorRegistrationDTO updateStatusType(String statusType, String appNo);

	public List<String> getScheduledTrainingDates(String typeOfTraining);

	public List<DriverConductorTrainingDTO> getDriverConductorTrainings(boolean pendingOnly, String typeOfTraining,
			Date regStartDate, Date regEndDate, String appNo, String name, String driverId, String conductorId,
			String languageCode);

	public boolean saveTraingingDates(DriverConductorTrainingDTO dcTrainingDateDTO, String user);

	public boolean updateStatus(String statusType, String status, String oldStatusType, String oldStatus, String appNo,
			String user);

	public boolean updateStatusAndInactivePrevious(String statusType, String status, String oldStatusType,
			String oldStatus, String appNo, String user, String driverConducId);

	public List<DriverConductorTrainingDTO> getAttendanceList(Date trainingDate, String selectedTrainingType,
			Date startTime, Date endTime, String location);

	public boolean saveAttendance(List<DriverConductorTrainingDTO> attendanceList, String user);

	public List<DriverConductorTrainingDTO> getCertificateInfo(boolean listAll, String selectedTrainingType,
			Date trainingDate, String applicationNo, String driverId, String conductorId);

	public byte[] getDriverConductorPhoto(String appNo);

	// Blacklist Management
	public List<DropDownDTO> getBlacklistTypes();

	public DriverConductorBlacklistDTO getBlacklisterInfo(String searchNic, String searchDriverConductor);

	public List<DropDownDTO> getNonBlackListedDriCond();

	public List<DropDownDTO> getPendingBlackListedDriCond();

	public List<DropDownDTO> getBlackListedDriCond();

	public boolean createBlacklister(DriverConductorBlacklistDTO blacklisterDTO, String driConId, String user);

	public List<DriverConductorBlacklistDTO> getPendingBlacklister(String searchStatus, String searchNic,
			String searchDriverConductor);

	public boolean approvalBlacklister(String nic, String user);

	public boolean rejectBlacklisters(DriverConductorBlacklistDTO blacklister, String rejectReason, String user);

	public List<DriverConductorBlacklistDTO> getBlacklistedList(String searchStatus, String searchNic,
			String searchDriverConductor);

	public boolean clearanceBlacklist(String currentStatus, String nic, String user);

	public void updateStatusandStatusType(String statusType, String status, String appNo);

	public String getVoucherNo(String appNo, String trainingType);

	public String getApproveStatus(String nicNo);

	public String getAppNoByDriverID(String driverID);

	public String getTransactionTypeDes(String code);

	public String getTrainingTypeDes(String trainingCode);

	public List<DriverConductorRegistrationDTO> getDriverConducFilterListForReReg(String trainingType);

	// for Driver COnductor Re REgister Form
	public String getDriConducIdByNIC(String nic);

	public List<DriverConductorRegistrationDTO> getBySearcgPaymentCompletedDC(String trainingType, String nicNo,
			String dcId, String name);

	public List<CommonDTO> getTempANICListN();

	public List<CommonDTO> getTempANICListByTrainingTypeN(String trainingType);

	public boolean getDataInDriverConductortemp(String nicNo, String dcId);

	public String getTrainingTypeByAppNo(String appNo);

	public boolean receiptGenerated(String appNo);

	public List<DriverConductorRegistrationDTO> getTrainingLanguageList();

	public List<DriverConductorRegistrationDTO> getReceiptNoForDriverConductor(String trainingType);

	public DriverConductorRegistrationDTO getDetailsByDCIdandTypeForView(String strDCId, String strType,
			String receiptNo);

	public String getNoOfDuplicateTraining(String trainingType, String nicNo);

	public List<CommonDTO> getPendingIDListForView();

	public List<CommonDTO> getPendingIDListForViewByTrainingType(String trainingType);

	public List<CommonDTO> getPendingDCIDListForView();

	public List<CommonDTO> getPendingDCIDListByTrainingTypeForView(String trainingType);

	public List<CommonDTO> getPendingDCIDListByTrainingandIDForView(String trainingType, String idNo);

	public List<CommonDTO> GetAllTrainingTypesWithoutDuplicate();

	public void updateNewNic(String nic, String oldAppno, String oldNic, String loginUser, boolean photoHave);

	public void updateNICInPhotoUpload(String oldNIC, String newNIC, String appNo, String loginUser);

	List<CommonDTO> getTrainingTypeByNic(String Nic);

	/** Edit for Authorized Page **/
	List<CommonDTO> getAllIDListForViewByTrainingType(String trainingType);

	List<CommonDTO> getAllDCIDListByTrainingType(String trainingType);

	List<CommonDTO> getDCIDListByTrainingandID(String trainingType, String idNo);

	boolean updateDriverConductorByHighAuthOfficer(DriverConductorRegistrationDTO driverConductorRegistrationDTO,
			String strSelectedType);
	
	public int checkAlreadyActiveTrainingForNIC(String nicNo);
	
	public int checkBlackListCount(String nicNo, String status);

	void beanLinkMethod(DriverConductorRegistrationDTO registrationDto, String user, String des, String funDes);

	void beanLinkMethod(MaintainTrainingScheduleDTO trainingScheduleDTO, String user, String des, String funDes);

	void beanLinkMethod(DriverConductorTrainingDTO trainingDto, String user, String des, String funDes);

	public void beanLinkMethod(DriverConductorBlacklistDTO blacklisterDTO, String user, String des, String funDes);

	List<CommonInquiryDTO> getAppNoListForCommonInquiry();

	List<CommonInquiryDTO> getNICNoListForCommonInquiry();

	List<CommonInquiryDTO> getDriConNoListForCommonInquiry();

	List<CommonInquiryDTO> getBusNoListForCommonInquiry();

	List<CommonInquiryDTO> searchDataForCommonInquiry(String NIC, String appNo, String busNo, String dcId);

	public List<CommonInquiryDTO> searchDateDataForCommonInquiry(Date sheduleDate);
}
