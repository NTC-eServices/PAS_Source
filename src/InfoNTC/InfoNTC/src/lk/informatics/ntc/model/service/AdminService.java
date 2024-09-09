package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lk.informatics.ntc.model.dto.AssignModelDTO;
import lk.informatics.ntc.model.dto.AttorneyHolderDTO;
import lk.informatics.ntc.model.dto.BusOwnerDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.PermitPaymentDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.dto.TokenDTO;

public interface AdminService extends Serializable {
	public List<RouteDTO> getRouteDetails();

	public int saveRoute(RouteDTO routeDTO);

	public int updateRouteDetails(RouteDTO routeDTO);

	public List<CommonDTO> GetStatusToDropdown();

	public String getUserActivity(String userName, String functionId);

	public List<RouteDTO> getRouteDetailsbyLoginUser(String userId);

	public String chkDuplicates(String routeNo);

	public List<RouteDTO> getRouteDetailsByRouteNo(String routeNo);

	public RouteDTO getDetailsbyRouteNo(String routeNo);

	public List<CommonDTO> getRoutesToDropdown();

	public List<CommonDTO> getServiceTypeToDropdown();

	public List<CommonDTO> getProvinceToDropdown();

	public List<CommonDTO> getDistrictToDropdown();

	public List<CommonDTO> getDivSecToDropdown();

	public List<CommonDTO> getDistrictByProvinceToDropdown(String provinceCode);

	public List<CommonDTO> getDivSecByDistrictToDropdown(String districtCode);

	public List<CommonDTO> getMakesToDropdown();

	public List<CommonDTO> getModelsToDropdown();

	public List<CommonDTO> getModelsByMakeToDropdown(String make);

	public int saveBacklogPermit(PermitDTO permitDTO);

	public PermitDTO searchByPermitNo(String permitNo);
	
	public PermitDTO searchBySeq(long seq);

	public int updateBacklogPermit(PermitDTO permitDTO);

	public int saveBusOwnerDetails(BusOwnerDTO busOwnerDTO);

	public BusOwnerDTO ownerDetailsByPermitNo(String permitNo);

	public int updateBusOwner(BusOwnerDTO busOwnerDTO);

	public int saveBacklogOminiBus(OminiBusDTO ominiBusDTO);

	public int saveBacklogOminiBusWithApplicationNo(OminiBusDTO ominiBusDTO, String generatedApllicationNo);

	public OminiBusDTO ominiBusByVehicleNo(String vehicleNo);

	public int updateOminiBus(OminiBusDTO ominiBusDTO);

	public int saveBacklogPayments(PermitPaymentDTO permitPaymentDTO);

	public PermitPaymentDTO paymentBypermitNo(String permitNo);

	public int updateBacklogPayments(PermitPaymentDTO permitPaymentDTO);

	public String checkVehiNo(String vehicleNo);

	public String checkDuplicatePermitNo(String permitNo);

	public Long checkDuplicatePermitNo_Edit(String permitNo, Long seq);

	public String checkVehiNo_Edit(String busNo, Long seq);

	public List<String> getAllActivePermits();

	public List<String> getAllActiveApplications();

	public List<String> getAllActiveBusRegNos();

	public PermitDTO searchByPermitNo(String permitNo, String appNo, String busRegNo);

	public List<String> getAllActivePermitsforAttorney();

	public List<String> getAllActiveApplicationsforAttorney();

	public List<String> getAllActiveBusRegNosforAttorney();

	public AttorneyHolderDTO searchAttorneyDetails(String permitNo, String applicationNo, String vehicleNo);

	public int updateAttorneyHolder(AttorneyHolderDTO attorneyHolderDTO);

	public List<AssignModelDTO> getMakes();

	public List<AssignModelDTO> getModelDetails(AssignModelDTO assignModelDTO);

	public boolean insertModel(AssignModelDTO assignModelDTO, String logInUser);

	public boolean checkModel(AssignModelDTO assignModelDTO);

	public boolean deleteModel(String code);

	public String getMakesDescription(AssignModelDTO assignModelDTO);

	public BigDecimal completeBusFare(String routeNo);

	public BusOwnerDTO ownerDetailsByNicNo(String nicNo);

	public String getApplicationNoFromPermitNo(String permitNo);

	public String getBusNoFromPermitNo(String permitNo);

	public String getPermitNoFromAppNo(String appNo);

	public String getBusNoFromAppNo(String appNo);

	public String getPermitNoFromBusNo(String busNo);

	public String getAppNoFromBusNo(String busNo);

	public String getParaCodeForTitle();

	public RouteDTO getDetailsbyRouteNoX(String routeNo);

	public int updateBacklogOminiBusRegNo(PermitDTO permitDTO);

	public BusOwnerDTO ownerDetailsByApplicationNo(String strSelectedApplicationNo);

	public OminiBusDTO ominiBusByApplicationNo(String strSelectedApplicationNo);

	public PermitPaymentDTO paymentByApplicationNo(String strSelectedApplicationNo);

	public List<String> getAllApplicationsWithoutCheckingStatus();

	public boolean CopyOwnerDetailsANDinsertOwnerHistory(String strSelectedApplicationNo, String loginUser);

	public boolean deletePrevOwnerRecord(String strSelectedApplicationNo, String loginUser);

	public boolean CopyOwnerDetailsANDinsertOminiBusHistory(String strSelectedApplicationNo, String loginUser);

	public boolean CopyPaymentDetailsANDinsertPaymentHistory(String strSelectedApplicationNo, String loginUser);

	public boolean CopyPermitDetailsANDinsertPermitHistory(String strSelectedApplicationNo, String loginUser);

	public PermitRenewalsDTO renewalsByApplicationNo(String strSelectedApplicationNo);

	public int updatePermitRenewalRecord(PermitRenewalsDTO permitRenewalsDTO);

	public int updateBacklogVehiOwner(PermitDTO permitDTO);

	public int updatePermitRenewalRecordForAmmendments(PermitRenewalsDTO permitRenewalsDTO);

	public int updatePermitRenewalRecordsInOmniForAmmendments(PermitRenewalsDTO permitRenewalsDTO);

	public boolean updatePermitDate(PermitRenewalsDTO permitRenewalsDTO, String loginUser);

	public boolean updatePermitDateN(PermitRenewalsDTO permitRenewalsDTO, String loginUser);

	public PermitRenewalsDTO renewalsByApplicationNoWithNewExpiredDate(String selectedAppNo);

	public int updateEditPermitRecord(PermitDTO permitDTO);

	public String getPreviousPermitNo(String strSelectedApplicationNo);

	public String getPreviousVehicleNo(String strSelectedApplicationNo);

	public List<TokenDTO> getUnporcessedTokenDet();

	public boolean deleteToken(long seq);

	public boolean removeBusProcess(PermitDTO permitDTO, String newBusNo, String strUser);

	public List<String> getAllActiveApp();

	public List<CommonDTO> getAllActivePermit();

	public List<TokenDTO> getRenewalTokenDet();

	public int updateAmendmentDataForModifyPermitData(PermitDTO permitDTO, String loggedUser);

	public boolean hasValidAmendmentApplication(String applicationNo);
	
	List<RouteDTO> getNisiRouteDetailsbyLoginUser(String userId);
	List<RouteDTO> getNisiRouteDetails();
	List<RouteDTO> getNisiRouteDetailsByRouteNo(String routeNo);
}
