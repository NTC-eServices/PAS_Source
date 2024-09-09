package lk.informatics.ntc.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.dto.FlyngSquadAttendanceDTO;

public interface FlyingSquadInvestigationLogService {

	public ArrayList<FlyingSquadInvestiMasterDTO> getrefNo();

	public ArrayList<FlyingManageInvestigationLogDTO> getreportNo(String refNo);

	public FlyingManageInvestigationLogDTO getmasterData(String refNo, String reportNo);

	public String savemasterdata(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO, String user);

	public void updatemasterdata(String user);

	public void updatedetaildata(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO, String user,
			String reportNo, String refNo);

	public String getInvesNo(String reportNo);

	public void savedetaildata(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO, String user,
			String reportNo, String refNo);

	public FlyingManageInvestigationLogDTO getVehicle(String vehicleNo);

	public List<FlyingManageInvestigationLogDTO> getVehicleDetail();

	public FlyingManageInvestigationLogDTO getroutedata(String routeNo);

	public List<FlyingManageInvestigationLogDTO> routeNodropdown();

	public ArrayList<FlyingManageInvestigationLogDTO> getdetails(String refNo, String reportNo);

	public String getReportNo(String refNo);

	public List<FlyingManageInvestigationLogDTO> getServiceTypeToDropdown();

	public void updateStatus(String invesno, FlyingManageInvestigationLogDTO deletededflyingManageInvestigationLogDTO,
			String user);

	public void updateendDate(String reportNo, String user);

	public void updatemasterdet(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO, String user,
			String refno, String reportNo);

	public ArrayList<FlyngSquadAttendanceDTO> getexsistingdetails(String referenceNo, Date invesDate);

	public ArrayList<FlyingSquadInvestiMasterDTO> getrefNon();

	public FlyingManageInvestigationLogDTO getdetail(String refno, String reportNo);

	public void delete(String invesno);

	public String getrefNoNew(String reportNo);

	public List<FlyingManageInvestigationLogDTO> getPermitNoByService(String serviceType);

	public FlyingManageInvestigationLogDTO getVehicle(String vehicleNo, String serviceType);
	
	Date getInvestigationDateAndTime(String refNo);
	
	List<String> getRouteDataNew(String routeNo);
}
