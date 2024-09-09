package lk.informatics.ntc.model.service;

import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.BusFareDTO;
import lk.informatics.ntc.model.dto.BusFareEquationDTO;

public interface BusFareService {

	/* Bus Rate Methods */

	public List<BusFareDTO> getBusCategory();

	public BusFareDTO getBusCategoryDescription(BusFareDTO dto);

	public String getBusCategoryDescription(String busCategoryCode);

	public List<BusFareDTO> getCurrentBusFare();

	public String generateFareReferenceNo();

	public List<BusFareDTO> getDefaultBusCategory();

	public int getStageCount();

	public BusFareDTO getCurrentDetails();

	public boolean updateNormalBusRate(List<BusFareDTO> rateList, String user);

	public boolean updateSemiLuxuryBusRate(List<BusFareDTO> rateList, String user);

	public boolean updateLuxuryBusRate(List<BusFareDTO> rateList, String user);

	public boolean updateSuperLuxuryBusRate(List<BusFareDTO> rateList, String user);

	public boolean updateHighWayBusRate(List<BusFareDTO> rateList, String user);

	public boolean updatetHalfSisuSariyaBusRate(List<BusFareDTO> rateList, String user);

	public boolean updateQuarterSisuSariyBusRate(List<BusFareDTO> rateList, String user);

	public boolean insertReferenceData(String refNo, List<BusFareDTO> stageList, String user,
			List<BusFareDTO> newFeeList);

	public boolean masterTableInjection(BusFareDTO dto, String fareReferenceNo, String user);

	public String getBusOrderCode(String code);

	public int checkPendingRecordCount();

	/* Temporary Bus Rate Methods */

	public BusFareDTO getTempBusCategoryDescription(BusFareDTO dto);

	public List<BusFareDTO> getTempBusCategory();

	public List<BusFareDTO> getTempDefaultBusCategory();

	public String generateTempFareReferenceNo();

	public boolean insertTempReferenceData(String refNo, List<BusFareDTO> stageList, String user,
			List<BusFareDTO> newFeeList, BusFareDTO busFareDTO);

	public boolean updateTempNormalBusRate(List<BusFareDTO> rateList, String user, String referenceNO);

	public boolean updateTempSemiLuxuryBusRate(List<BusFareDTO> rateList, String user, String referenceNO);

	public boolean updateTempLuxuryBusRate(List<BusFareDTO> rateList, String user, String referenceNO);

	public boolean updateTempSuperLuxuryBusRate(List<BusFareDTO> rateList, String user, String referenceNO);

	public boolean updateTempHighWayBusRate(List<BusFareDTO> rateList, String user, String referenceNO);

	public boolean updateTemptHalfSisuSariyaBusRate(List<BusFareDTO> rateList, String user, String referenceNO);

	public boolean updateTempQuarterSisuSariyBusRate(List<BusFareDTO> rateList, String user, String referenceNO);

	public List<BusFareDTO> getFareRefNoList();

	public List<BusFareDTO> getDefaultList();

	public List<BusFareDTO> getStatusList();

	public List<BusFareDTO> getFilteredFareRefNoList(String selectedStatus);

	public List<BusFareDTO> getFilteredDateList(Date startDateObj, Date endDateObj, String selectedStatus,
			String selectedFareRefNo);

	public boolean busFareChecked(BusFareDTO selectDTO, String loginUser);

	public boolean busFareRecommended(BusFareDTO selectDTO, String loginUser);

	public boolean busFareApproved(BusFareDTO selectDTO, String loginUser);

	public boolean busFareInactivePrevRecrd(BusFareDTO selectDTO, String loginUser);

	public BusFareDTO getPrevActiveRecordDetails(BusFareDTO selectDTO);

	public List<BusFareDTO> getTFeeListForPrevActiveRecord(String prevActiveBusFareRefNo);

	public boolean insertFareHistory(List<BusFareDTO> tFeeListForPrevActiveRef);

	public List<BusFareDTO> getTempNormalFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getTempLuxeryFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getBusFareReferenceNo();

	public List<BusFareDTO> getTempBusFareReferenceNo();

	public List<BusFareDTO> getBusFareRateData(BusFareDTO busFareDTO);

	public List<BusFareDTO> getTempBusFareRateData(BusFareDTO busFareDTO);

	public List<BusFareDTO> getTempSemiLuxeryFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getTempSuperLuxeryFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getTempHighwayFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getTempSisuSeriyaHalfFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getTempSisuSeriyaQuaterFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getPrevActiveNormalFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getPrevActiveLuxeryFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getPrevActiveSemiLuxeryFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getPrevActiveSuperLuxeryFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getPrevActiveHighwayFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getPrevActiveSisuSeriyaHalfFeeList(BusFareDTO selectDTO);

	public List<BusFareDTO> getPrevActiveSisuSeriyaQuaterFeeList(BusFareDTO selectDTO);

	public boolean updateFareNormalDet(List<BusFareDTO> tempNormalFeeList, BusFareDTO selectDTO, String user);

	public boolean updateFareLuxeryDet(List<BusFareDTO> tempLuxeryFeeList, BusFareDTO selectDTO, String loginUser);

	public boolean updateFareSemiLuxeryDet(List<BusFareDTO> tempSemiLuxeryFeeList, BusFareDTO selectDTO,
			String loginUser);

	public boolean updateFareSuperLuxeryDet(List<BusFareDTO> tempSuperLuxeryFeeList, BusFareDTO selectDTO,
			String loginUser);

	public boolean updateFareHighwayDet(List<BusFareDTO> tempHighwayFeeList, BusFareDTO selectDTO, String loginUser);

	public boolean updateFareSisuHalfDet(List<BusFareDTO> tempSisuSeriyaHalfFeeList, BusFareDTO selectDTO,
			String loginUser);

	public boolean updateFareSisuQuaterDet(List<BusFareDTO> tempSisuSeriyaQuaterFeeList, BusFareDTO selectDTO,
			String loginUser);

	public List<BusFareDTO> getDefaultBusFareRate();

	public List<BusFareDTO> getNormalList(String tableName, String satge, boolean isHistory, String referenceNO);

	public List<BusFareDTO> getSemiLuxuaryList(String tableName, String satge, boolean isHistory, String referenceNO);

	public List<BusFareDTO> getLuxuaryList(String tableName, String satge, boolean isHistory, String referenceNO);

	public List<BusFareDTO> getHighWayList(String tableName, String satge, boolean isHistory, String referenceNO);

	public List<BusFareDTO> getSuperLuxuaryList(String tableName, String satge, boolean isHistory, String referenceNO);

	public List<BusFareDTO> getSisuSariyaHalfList(String tableName, String satge, boolean isHistory,
			String referenceNO);

	public List<BusFareDTO> getSisuSariyaQuaterList(String tableName, String satge, boolean isHistory,
			String referenceNO);

	public List<BusFareDTO> getTableStageList(String tableName, String satge, boolean isHistory, String refeNo);

	public boolean busFareRejected(BusFareDTO selectDTO, String loginUser);

	public boolean saveEditBusRateFee(BusFareDTO busFareDTO, String referenceNO, String user, int stageNo);

	public List<BusFareDTO> getPrevActiveTempList(BusFareDTO selectDTO);

	public boolean deleteTempList(List<BusFareDTO> viewTempListForPrevRefNo, BusFareDTO selectDTO, String loginUser);

	public String getActiveReferenceNo();

	public void insertFareEquationDetails(List<BusFareDTO> currentFareList, String fareReferenceNo);
	
	public List<BusFareEquationDTO> getPreviosApprovedEquation(String referenceNo);

}
