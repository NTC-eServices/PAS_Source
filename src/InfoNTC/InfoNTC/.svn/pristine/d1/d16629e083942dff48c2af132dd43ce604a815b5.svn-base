package lk.informatics.ntc.model.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.BlockRouteDetailDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.CommonInquiryDTO;
import lk.informatics.ntc.model.dto.MainStationDetailsDTO;
import lk.informatics.ntc.model.dto.PaymentTypeDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.TerminalArrivalDepartureDTO;
import lk.informatics.ntc.model.dto.TerminalArrivalDepartureTimeDTO;
import lk.informatics.ntc.model.dto.TerminalBlockDetailsDTO;
import lk.informatics.ntc.model.dto.TerminalDetailsDTO;
import lk.informatics.ntc.model.dto.TerminalPayCancellationDTO;
import lk.informatics.ntc.model.dto.TerminalPaymentDTO;

public interface TerminalManagementService {

	public List<MainStationDetailsDTO> selectAllMainStations();

	public long saveTerminalHeader(TerminalDetailsDTO terminalDetailsDTO, String user);

	public int saveTerminalDetails(TerminalDetailsDTO terminalDetailsDTO, long stationSeqno, String terminal,
			String platform, String user);

	public List<TerminalDetailsDTO> selectTerminalsByStation(String stationSeq);

	public int terminalDetailStatusUpdate(long stationSeq, String status, String user);

	public boolean deleteSelectedBusTerminal(long terminalSeqNo);

	public int getActiveTerminalBlockCount(long terminalSeqNo);

	public TerminalDetailsDTO selectTerminalBySequence(Long seq);

	public int saveTerminalDetailsHistory(TerminalDetailsDTO terminalDetailsDTO);

	public List<String> selectDistinctTerminalsByStation(String stationCode);

	public List<TerminalDetailsDTO> selectTerminalDetailsByTerminal(String terminal, String code);

	public int saveBlockHeader(TerminalBlockDetailsDTO terminalBlockDetailsDTO, String user);

	public int saveTerminalBlockDetails(TerminalBlockDetailsDTO terminalBlockDetailsDTO, String stationSeqno,
			String platform, String block, String user);

	public List<TerminalBlockDetailsDTO> selectTerminalBlocksBySequence(Long seq);

	public int terminalBlockDetailStatusUpdate(long stationSeq, String status, String user);

	public TerminalBlockDetailsDTO selectTerminalBlockBySequence(Long seq);

	public int saveTerminalBlockDetailsHistory(TerminalBlockDetailsDTO terminalDetailsDTO);

	public boolean deleteSelectedTerminalBlock(long terminalBlockSeqNo);

	public List<CommonDTO> getServiceTypeToDropdown();

	public long saveAssignedBusRouteHeader(long terminalBlockSeq, String stationId, long terminalId, String lineNo,
			String blockId, String user);

	public boolean saveAssignedBusRouteDetails(long blockRouteSeq, String serviceType, String routeNo, String user);

	public List<BlockRouteDetailDTO> selectBlockRoutes(String stationCode, String platformId);

	public boolean isBlockAssigned(String route, String service, String stationCode);

	public boolean updateAssignedBusRouteDetails(BlockRouteDetailDTO oldRouteDTO, BlockRouteDetailDTO newRouteDTO,
			String user);

	public boolean removeAssignedBusRoute(BlockRouteDetailDTO oldRouteDTO, String user);

	public List<PaymentTypeDTO> getPaymentTypes();
	
	public List<PaymentTypeDTO> getResciptNoList(String paymentTypeCode, String terminalCode);
	

	public double[] getIssuanceAmt(String paymentType, String serviceType);

	public boolean validateVehicleOrPermitNo(String type, String no);
	public PermitDTO getPermitInfoByBusNoPermitNo(String permitNo, String busNo);
	
	public TerminalPaymentDTO getTerminalPaymentInfoBySeq(long seq);
	
	public PermitDTO getTemporaryPermitInfoByBusNoPermitNo(String permitNo, String busNo);

	public long getTotalCollectionAmt();

	public boolean saveTerminalPaymentInfo(String paymentType, String serviceType, String vehihcleNo, Date issuedDate,
			String issuedMonth, String payMode, String receiptRefNo, long noOfTurn, Date fromDate, Date toDate,
			double basicAmt, double penaltyAmt, double paidAmt, double totalCollection, String permitNo, String origin,
			String destination, String via, String owner, String gender, String contact, String user, Date dueDate,
			Date paidDate, Date expireDate, String hirePermitRemark, String tsOrigin, String tsDestination,
			String tsVia, String tsOwner, String tsGender, String tsContactNo, String tsServiceType, String tsRouteNo,
			String routeNo, String tsPermitNo,String selectedTerminalLocation);

	public boolean saveTerminalPaymentInfoP(String paymentType, String serviceType, String vehihcleNo, Date issuedDate,
			String issuedMonth, String payMode, String receiptRefNo, long noOfTurn, Date fromDate, Date toDate,
			double basicAmt, double penaltyAmt, double paidAmt, double totalCollection, String permitNo, String origin,
			String destination, String via, String owner, String gender, String contact, String user, Date dueDate,
			Date paidDate);

	public long getPaymentSummarySequence(String paymentType);

	public boolean updatePaymentSummary(long seq, String paymentType, double totalCollection, long receiptCount,
			boolean receiptGenerated, boolean insert, String user);

	public String generatePermitNo();

	public boolean updatePermitNoGenerateSeq();

	public String generateSpecialPermitNo();

	public boolean updateSpecialPermitNoGenerateSeq();

	public String getServiceTypeByPermitNo(String permitNo);

	// generate Voucher number
	public String generateVoucherNo();

	// update Voucher number
	public boolean updateVoucherNumber(String receiptVal, String vehicleRegNo, String payReciptRef, String modiFyName,
			boolean receiptGenerated);

	public List<TerminalPayCancellationDTO> getDetailsByVoucherNo(String voucherNo);

	public boolean cancelledPayment(long seq, String user);

	public boolean referenceNoExists(String payReciptRef);

	public boolean updateServiceAmounts(String paymentType, String serviceType, double basicAmountOld, double penaltyOld,
			double basicAmount, double penalty, String user);

	// Arrival
	public TerminalArrivalDepartureDTO getDetailsByBusNo(String busRegNo, String currentStation);

	public List<TerminalArrivalDepartureTimeDTO> getScheduledArrivalForDay(
			TerminalArrivalDepartureDTO terminalArrivalData, int dayOfWeek);

	public List<TerminalArrivalDepartureTimeDTO> getScheduledDepartureForDay(
			TerminalArrivalDepartureDTO terminalDepartureData, int dayOfWeek);

	public TerminalArrivalDepartureDTO insertTerminalArrivalLog(TerminalArrivalDepartureDTO scheduleListForDay,
			int dayOfWeek, String user);

	public TerminalArrivalDepartureDTO insertTerminalDepartureLog(TerminalArrivalDepartureDTO scheduleListForDay,
			int dayOfWeek, String user);

	public boolean updateTerminalArrivalLog(Long sequence, String actualArrivalTime, int turnNo, String string,
			String errors, int dayOfWeek, String user);

	public boolean updateTerminalDepartureLog(Long arrivalSeq,Long sequence, String actualArrivalTime, int turnNo, String string,
			String errors, int dayOfWeek, String user);

	// Receipt
	public List<TerminalDetailsDTO> getRecieptRefList();

	public List<TerminalDetailsDTO> getReceiptRefAggainstPayType(String selectedPayType);

	public TerminalDetailsDTO showsearchedData(String selectedPayType, String recieptRefNo);

	public boolean checkReceiptCanceled(String selectedRfNo);

	public List<CommonDTO> getPaymentModes();
	
	public String getRouteAndServiceTypeByBusNo(String busNo,boolean bool);
	public TerminalArrivalDepartureDTO getPanelGeneratorDetails(String refNo);
	public boolean checkEnteredBusLeave(String busNo, String tripType,String refNo,int dayNo);
	public List<String> checkEnteredBusForInOut(String busNo, String tripType,String refNo,int dayNo);
	public String getParamTimeAdded(String paramName);
	boolean checkPermitStatusInActive(String busNumber);
	boolean checkRouteCorrectForStation(String route,String service,String stationCode);
	String getNoOfCouplesForRefNo(String refNo, String busNo);
	int getNoOfTrips(String busNo);
	boolean checkDriverConductorSuspend(String busNo);
	boolean isPaymentDoneForCurrentMonth(String busNo);
	boolean checkBusIsArrived(String busNo,String busRoute,String busService,String station);
	String getStationNameByID(String code);
	boolean checkBusDepartured(String busNo,String busRoute,String busService,String station);
	int getNoOfTurns(String busNo,String busRoute,String busService,String station);
	int getNoOfTurnsForDepart(String busNo,String busRoute,String busService,String station);
	long getarrivallatestSeq(String busNo,String busRoute,String busService,String station);
	boolean getIsBusDepartCheck(String busNo,String busRoute,String busService,String station, String side,String paramTime);
	boolean checkFirstArrive(String busNo,String busRoute,String busService,String station);
	boolean checkYerterDayArriveNDepart(String busNo,String busRoute,String busService,String station);
	boolean checkLatestActualArrivedBusIdDepartued(String busNo,String busRoute,String busService,String station);
	boolean checkEntererdBusArriveToday(String busNo,String busRoute,String busService,String station);
	public TerminalArrivalDepartureDTO insertTerminalDepartureLogForYersterday(TerminalArrivalDepartureDTO scheduleListForDay,
			int dayOfWeek, String user);
	String getBusAbbrivationForEnteredBus(String busNo,String refNo);
	List<String>getAlltimeSlotsForTheDay(String route,String service,String refNo,String busNo, int day);
	List<String>getAllEndtimeSlotsForTheDay(String route,String service,String refNo,String busNo, int day);
	List<String> getLeaveBusAbbriviationListForTheDay(String refNo ,int day);
	List<String> getBusAbbriviationListForTheDay(String refNo ,int day,String busAbbr);
	String getChargeAmmount(String receiptNo);
	
	public int updateTerminalPayment(TerminalPaymentDTO paymentDTO, String paymentType);

	void beanLinkMethod(TerminalDetailsDTO terminalDetailsDTO, String user, String des, String funDes);

	void beanLinkMethod(TerminalBlockDetailsDTO terminalDetailsDTO, String user, String des, String funDes);

	void beanLinkMethod(String status, String user, String functiondes, String busNo, String permitNo, String payType);

	void beanLinkMethod(String status, String user, String functiondes, String voucherNo);

	List<CommonInquiryDTO> getTerminalNoListForCommonInquiry();

	List<CommonInquiryDTO> getBusNoListForCommonInquiry();

	List<CommonInquiryDTO> getVoucherNoListForCommonInquiry();

	List<CommonInquiryDTO> getPaymentTypeListForCommonInquiry();

	List<CommonInquiryDTO> searchTerminalDataForCommonInquiry(String terminalNo);

	List<CommonInquiryDTO> searchVoucherDataForCommonInquiry(String voucherNo);

	List<CommonInquiryDTO> searchDataForCommonInquiry(String busNo, String paymentType);

	List<String> getActivePanelgeneratorNumber(String busNum, int day);
}
