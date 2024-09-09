package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.util.List;

import lk.informatics.ntc.model.dto.LogSheetMaintenanceDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;

public interface LogsheetMaintenanceService {
	public List serviceRefNoDropDown();

	public List serviceTypeDropDown();

	public List serviceNoDropDown();

	public LogSheetMaintenanceDTO search(LogSheetMaintenanceDTO logDTO);

	public List sheetTable(LogSheetMaintenanceDTO logDTO);

	public List sheetTableCheckBy(LogSheetMaintenanceDTO logDTO);

	public void insertToLogSheet(LogSheetMaintenanceDTO logDTO);

	public boolean editFromgLogSheet(LogSheetMaintenanceDTO logDTO);

	public boolean deleteFromLogSheet(LogSheetMaintenanceDTO logDTO);

	public LogSheetMaintenanceDTO datafromLogSheet(LogSheetMaintenanceDTO logDTO);

	public boolean checkData(LogSheetMaintenanceDTO logDTO);

	public LogSheetMaintenanceDTO getRefNo(LogSheetMaintenanceDTO logDTO);

	public boolean approvalstatusupdate(String status, String user, String date, String logRefNo);

	public String paymentRate();

	public boolean initialSearch(LogSheetMaintenanceDTO logDTO);

	public String totalLength(String serviceNo);

	// Log Sheet Recieved Maintenance
	public List<SisuSeriyaDTO> getServiceTypeForDropdown();

	public List<SisuSeriyaDTO> getServiceRefNoForDropdown(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> getServiceAgtNoForDropdown(SisuSeriyaDTO dto);

	public List<SisuSeriyaDTO> showSearchedData(SisuSeriyaDTO sisuSeriyaDto, String year);

	public List<SisuSeriyaDTO> showDefaultSearchedData();

	public boolean updateLogSheetReceivedData(SisuSeriyaDTO sisuSeriyaDto, String user,
			List<SisuSeriyaDTO> sisuSariyaList);

	public SisuSeriyaDTO fillSerNo(String serviceNo);

	public List<SisuSeriyaDTO> getServiceAgtNoForGamiSeriyaDropdown(SisuSeriyaDTO sisuSeriyaDto);

	public List<SisuSeriyaDTO> showSearchedDataForGamiSeriya(SisuSeriyaDTO sisuSeriyaDto, String logSheetYear);

	public List<String> serviceNoSisuSeriyaDropDown(String serviceType);

	public List<String> serviceNoSisuSeriyaDropDown(LogSheetMaintenanceDTO dto);

	public List<String> serviceNoGamiSeriyaDropDown(String serviceType);

	public List<String> serviceNoGamiSeriyaDropDown(LogSheetMaintenanceDTO dto);

	public boolean initialGamiSeriyaSearch(LogSheetMaintenanceDTO logDTO);

	public LogSheetMaintenanceDTO searchGamiSeriya(LogSheetMaintenanceDTO logDTO);

	public LogSheetMaintenanceDTO getRefNoGamiSeriya(LogSheetMaintenanceDTO logDTO);

	public String paymentRateGamiSeriya();

	public String paymentRateNisiSeriya();

	public String totalLengthGamiSeriya(String serviceNo);

	public List<SisuSeriyaDTO> getServiceAgtNoForNisiSeriyaDropdown(SisuSeriyaDTO sisuSeriyaDto);

	public List<SisuSeriyaDTO> showSearchedDataForNisiSeriya(SisuSeriyaDTO sisuSeriyaDto, String logSheetYear);

	public List<String> serviceNoNisiSeriyaDropDown(String serviceType);

	public List<String> serviceNoNisiSeriyaDropDown(LogSheetMaintenanceDTO dto);

	public boolean initialNisiSeriyaSearch(LogSheetMaintenanceDTO logDTO);

	public LogSheetMaintenanceDTO searchNisiSeriya(LogSheetMaintenanceDTO logDTO);

	public LogSheetMaintenanceDTO getRefNoNisiSeriya(LogSheetMaintenanceDTO logDTO);

	public String totalLengthNisiSeriya(String serviceNo);

	public boolean editFromgLogSheetGamiAndNisi(LogSheetMaintenanceDTO logDTO);

	public List<SisuSeriyaDTO> getServiceAgtNoByRefNo(SisuSeriyaDTO dto);

	public String getApprovedSchool(LogSheetMaintenanceDTO logDTO);
	
	List<String>getBusNumberListForSisu(String subsidyType);
	List<String>getBusNumberListForGami(String subsidyType);
	List<String>getBusNumberListForNisi(String subsidyType);
	List<String>getNameListForSisu(String subsidyType);
	List<String>getNameListForGami(String subsidyType);
	List<String>getNameListForNisi(String subsidyType);
	
	BigDecimal getPaymentValueByRefNo(String refNo);

	String checkDataHaveForMonth(SisuSeriyaDTO list, String service);

}
