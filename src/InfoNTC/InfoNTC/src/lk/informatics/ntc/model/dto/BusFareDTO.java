package lk.informatics.ntc.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class BusFareDTO {

	private String busCategory;
	private String busCategoryCode;
	private String status;
	private double rate;
	private String selectBusCategorySinhala;
	private String selectBusCategoryTamil;
	private double perviousCostChange;
	private Date perviousCostChangeDate;
	private double curentCostChange;
	private String fareReferenceNo;
	private boolean fareCalculate;
	private int stageNo;
	private String busOrder;
	private Date createDate;
	private String createDateString;

	private String equation;
	private List<String> abbreviationList;

	private Date ammendmentsDateObj;
	private String ammendmentsDateVal;
	private double currentCostPresentage;
	private String statusDes;
	private Date createdDateObj;
	private String createdDateVal;
	private Date startDateObj;
	private String startDateVal;
	private Date endDateObj;
	private String endDateVal;
	private String prevActiveBusFareRefNo;
	private String createBy;
	private String modifiedBy;
	private Timestamp createdDateTS;
	private Timestamp modifiedDateTS;
	private BigDecimal differenceWithCurrentFare;

	private BigDecimal editNormalRoundFee;
	private BigDecimal editSemiLuxuaryRoundFee;
	private BigDecimal editLuxuryRoundFee;
	private BigDecimal editSuperLuxuryRoundFee;
	private BigDecimal edithighWayRoundFee;
	private BigDecimal editSisuSariyaHalfAdjestFee;
	private BigDecimal editSisuSariyaQuaterAdjestFee;

	/* Tempoary Details */

	private String tempBusCategory;
	private String tempBusCategoryCode;
	private String tempStatus;
	private double tempRate;
	private String tempSelectBusCategorySinhala;
	private String tempSelectBusCategoryTamil;
	private double tempCostChange;
	private boolean tempFareCalculate;
	private String tempFareReferenceNo;
	private boolean disabledFareCalculate;
	private String tempBusOrder;

	private String tempEquation;
	private List<String> tempAbbreviationList;

	/* Fees */

	private BigDecimal normalCurrentFee;
	private BigDecimal normalNewFee;
	private BigDecimal normalRoundFee;

	private BigDecimal luxuryCurrentFee;
	private BigDecimal luxuryNewFee;
	private BigDecimal luxuryRoundFee;

	private BigDecimal semiLuxuryCurrentFee;
	private BigDecimal semiLuxuryNewFee;
	private BigDecimal semiLuxuryRoundFee;

	private BigDecimal superLuxuryCurrentFee;
	private BigDecimal superLuxuryNewFee;
	private BigDecimal superLuxuryRoundFee;

	private BigDecimal highwayCurrentFee;
	private BigDecimal highwayNewFee;
	private BigDecimal highwayRoundFee;

	private BigDecimal sisuSariyaHalfNoramlFee;
	private BigDecimal sisuSariyaHalfAdjestedFee;
	private BigDecimal sisuSariyaHalfdiffrentFee;

	private BigDecimal sisuSariyaQuarterNoramlFee;
	private BigDecimal sisuSariyaQuarterAdjestedFee;
	private BigDecimal sisuSariyaQuarterdiffrentFee;

	public BusFareDTO() {

	}

	public BusFareDTO(String busCategory, String busCategoryCode, String status, double rate,
			String selectBusCategorySinhala, String selectBusCategoryTamil, boolean fareCalculate, String order) {

		this.busCategory = busCategory;
		this.busCategoryCode = busCategoryCode;
		this.status = status;
		this.rate = rate;
		this.selectBusCategorySinhala = selectBusCategorySinhala;
		this.selectBusCategoryTamil = selectBusCategoryTamil;
		this.fareCalculate = fareCalculate;
		this.busOrder = order;

	}

	public BusFareDTO(String tempBusCategory, String tempBusCategoryCode, String tempStatus, double tempRate,
			String tempSelectBusCategorySinhala, String tempSelectBusCategoryTamil, String order) {

		this.tempBusCategory = tempBusCategory;
		this.tempBusCategoryCode = tempBusCategoryCode;
		this.tempStatus = tempStatus;
		this.tempRate = tempRate;
		this.tempSelectBusCategorySinhala = tempSelectBusCategorySinhala;
		this.tempSelectBusCategoryTamil = tempSelectBusCategoryTamil;
		this.tempBusOrder = order;

	}

	public BusFareDTO(int stageNo, BigDecimal normalCurrentFee, BigDecimal normalNewFee, BigDecimal normalRoundFee,
			BigDecimal semiLuxuryCurrentFee, BigDecimal semiLuxuryNewFee, BigDecimal semiLuxuryRoundFee,
			BigDecimal luxuryCurrentFee, BigDecimal luxuryNewFee, BigDecimal luxuryRoundFee,
			BigDecimal highwayCurrentFee, BigDecimal highwayNewFee, BigDecimal highwayRoundFee,
			BigDecimal superLuxuryCurrentFee, BigDecimal superLuxuryNewFee, BigDecimal superLuxuryRoundFee,
			BigDecimal sisuSariyaHalfNoramlFee, BigDecimal sisuSariyaHalfAdjestedFee,
			BigDecimal sisuSariyaHalfdiffrentFee, BigDecimal sisuSariyaQuarterNoramlFee,
			BigDecimal sisuSariyaQuarterAdjestedFee, BigDecimal sisuSariyaQuarterdiffrentFee) {

		this.stageNo = stageNo;
		this.normalCurrentFee = normalCurrentFee;
		this.normalNewFee = normalNewFee;
		this.normalRoundFee = normalRoundFee;

		this.semiLuxuryCurrentFee = semiLuxuryCurrentFee;
		this.semiLuxuryNewFee = semiLuxuryNewFee;
		this.semiLuxuryRoundFee = semiLuxuryRoundFee;

		this.luxuryCurrentFee = luxuryCurrentFee;
		this.luxuryNewFee = luxuryNewFee;
		this.luxuryRoundFee = luxuryRoundFee;

		this.highwayCurrentFee = highwayCurrentFee;
		this.highwayNewFee = highwayNewFee;
		this.highwayRoundFee = highwayRoundFee;

		this.superLuxuryCurrentFee = superLuxuryCurrentFee;
		this.superLuxuryNewFee = superLuxuryNewFee;
		this.superLuxuryRoundFee = superLuxuryRoundFee;

		this.sisuSariyaHalfNoramlFee = sisuSariyaHalfNoramlFee;
		this.sisuSariyaHalfAdjestedFee = sisuSariyaHalfAdjestedFee;
		this.sisuSariyaHalfdiffrentFee = sisuSariyaHalfdiffrentFee;

		this.sisuSariyaQuarterNoramlFee = sisuSariyaQuarterNoramlFee;
		this.sisuSariyaQuarterAdjestedFee = sisuSariyaQuarterAdjestedFee;
		this.sisuSariyaQuarterdiffrentFee = sisuSariyaQuarterdiffrentFee;

	}

	public BusFareDTO(String refNo, int stageNo, BigDecimal normalCurrentFee, BigDecimal normalNewFee,
			BigDecimal normalRoundFee, BigDecimal semiLuxuryCurrentFee, BigDecimal semiLuxuryNewFee,
			BigDecimal semiLuxuryRoundFee, BigDecimal luxuryCurrentFee, BigDecimal luxuryNewFee,
			BigDecimal luxuryRoundFee, BigDecimal highwayCurrentFee, BigDecimal highwayNewFee,
			BigDecimal highwayRoundFee, BigDecimal superLuxuryCurrentFee, BigDecimal superLuxuryNewFee,
			BigDecimal superLuxuryRoundFee, BigDecimal sisuSariyaHalfNoramlFee, BigDecimal sisuSariyaHalfAdjestedFee,
			BigDecimal sisuSariyaHalfdiffrentFee, BigDecimal sisuSariyaQuarterNoramlFee,
			BigDecimal sisuSariyaQuarterAdjestedFee, BigDecimal sisuSariyaQuarterdiffrentFee) {

		this.fareReferenceNo = refNo;
		this.stageNo = stageNo;
		this.normalCurrentFee = normalCurrentFee;
		this.normalNewFee = normalNewFee;
		this.normalRoundFee = normalRoundFee;

		this.semiLuxuryCurrentFee = semiLuxuryCurrentFee;
		this.semiLuxuryNewFee = semiLuxuryNewFee;
		this.semiLuxuryRoundFee = semiLuxuryRoundFee;

		this.luxuryCurrentFee = luxuryCurrentFee;
		this.luxuryNewFee = luxuryNewFee;
		this.luxuryRoundFee = luxuryRoundFee;

		this.highwayCurrentFee = highwayCurrentFee;
		this.highwayNewFee = highwayNewFee;
		this.highwayRoundFee = highwayRoundFee;

		this.superLuxuryCurrentFee = superLuxuryCurrentFee;
		this.superLuxuryNewFee = superLuxuryNewFee;
		this.superLuxuryRoundFee = superLuxuryRoundFee;

		this.sisuSariyaHalfNoramlFee = sisuSariyaHalfNoramlFee;
		this.sisuSariyaHalfAdjestedFee = sisuSariyaHalfAdjestedFee;
		this.sisuSariyaHalfdiffrentFee = sisuSariyaHalfdiffrentFee;

		this.sisuSariyaQuarterNoramlFee = sisuSariyaQuarterNoramlFee;
		this.sisuSariyaQuarterAdjestedFee = sisuSariyaQuarterAdjestedFee;
		this.sisuSariyaQuarterdiffrentFee = sisuSariyaQuarterdiffrentFee;

	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getSelectBusCategorySinhala() {
		return selectBusCategorySinhala;
	}

	public void setSelectBusCategorySinhala(String selectBusCategorySinhala) {
		this.selectBusCategorySinhala = selectBusCategorySinhala;
	}

	public String getSelectBusCategoryTamil() {
		return selectBusCategoryTamil;
	}

	public void setSelectBusCategoryTamil(String selectBusCategoryTamil) {
		this.selectBusCategoryTamil = selectBusCategoryTamil;
	}

	public double getPerviousCostChange() {
		return perviousCostChange;
	}

	public void setPerviousCostChange(double perviousCostChange) {
		this.perviousCostChange = perviousCostChange;
	}

	public double getCurentCostChange() {
		return curentCostChange;
	}

	public void setCurentCostChange(double curentCostChange) {
		this.curentCostChange = curentCostChange;
	}

	public String getFareReferenceNo() {
		return fareReferenceNo;
	}

	public void setFareReferenceNo(String fareReferenceNo) {
		this.fareReferenceNo = fareReferenceNo;
	}

	public boolean isFareCalculate() {
		return fareCalculate;
	}

	public void setFareCalculate(boolean fareCalculate) {
		this.fareCalculate = fareCalculate;
	}

	public Date getPerviousCostChangeDate() {
		return perviousCostChangeDate;
	}

	public void setPerviousCostChangeDate(Date perviousCostChangeDate) {
		this.perviousCostChangeDate = perviousCostChangeDate;
	}

	public String getBusCategory() {
		return busCategory;
	}

	public void setBusCategory(String busCategory) {
		this.busCategory = busCategory;
	}

	public String getBusCategoryCode() {
		return busCategoryCode;
	}

	public void setBusCategoryCode(String busCategoryCode) {
		this.busCategoryCode = busCategoryCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTempBusCategory() {
		return tempBusCategory;
	}

	public void setTempBusCategory(String tempBusCategory) {
		this.tempBusCategory = tempBusCategory;
	}

	public String getTempBusCategoryCode() {
		return tempBusCategoryCode;
	}

	public void setTempBusCategoryCode(String tempBusCategoryCode) {
		this.tempBusCategoryCode = tempBusCategoryCode;
	}

	public String getTempStatus() {
		return tempStatus;
	}

	public void setTempStatus(String tempStatus) {
		this.tempStatus = tempStatus;
	}

	public double getTempRate() {
		return tempRate;
	}

	public void setTempRate(double tempRate) {
		this.tempRate = tempRate;
	}

	public String getTempSelectBusCategorySinhala() {
		return tempSelectBusCategorySinhala;
	}

	public void setTempSelectBusCategorySinhala(String tempSelectBusCategorySinhala) {
		this.tempSelectBusCategorySinhala = tempSelectBusCategorySinhala;
	}

	public String getTempSelectBusCategoryTamil() {
		return tempSelectBusCategoryTamil;
	}

	public void setTempSelectBusCategoryTamil(String tempSelectBusCategoryTamil) {
		this.tempSelectBusCategoryTamil = tempSelectBusCategoryTamil;
	}

	public double getTempCostChange() {
		return tempCostChange;
	}

	public void setTempCostChange(double tempCostChange) {
		this.tempCostChange = tempCostChange;
	}

	public boolean isTempFareCalculate() {
		return tempFareCalculate;
	}

	public void setTempFareCalculate(boolean tempFareCalculate) {
		this.tempFareCalculate = tempFareCalculate;
	}

	public BigDecimal getNormalCurrentFee() {
		return normalCurrentFee;
	}

	public void setNormalCurrentFee(BigDecimal normalCurrentFee) {
		this.normalCurrentFee = normalCurrentFee;
	}

	public BigDecimal getNormalNewFee() {
		return normalNewFee;
	}

	public void setNormalNewFee(BigDecimal normalNewFee) {
		this.normalNewFee = normalNewFee;
	}

	public BigDecimal getNormalRoundFee() {
		return normalRoundFee;
	}

	public void setNormalRoundFee(BigDecimal normalRoundFee) {
		this.normalRoundFee = normalRoundFee;
	}

	public BigDecimal getLuxuryCurrentFee() {
		return luxuryCurrentFee;
	}

	public void setLuxuryCurrentFee(BigDecimal luxuryCurrentFee) {
		this.luxuryCurrentFee = luxuryCurrentFee;
	}

	public BigDecimal getLuxuryNewFee() {
		return luxuryNewFee;
	}

	public void setLuxuryNewFee(BigDecimal luxuryNewFee) {
		this.luxuryNewFee = luxuryNewFee;
	}

	public BigDecimal getLuxuryRoundFee() {
		return luxuryRoundFee;
	}

	public void setLuxuryRoundFee(BigDecimal luxuryRoundFee) {
		this.luxuryRoundFee = luxuryRoundFee;
	}

	public BigDecimal getSemiLuxuryCurrentFee() {
		return semiLuxuryCurrentFee;
	}

	public void setSemiLuxuryCurrentFee(BigDecimal semiLuxuryCurrentFee) {
		this.semiLuxuryCurrentFee = semiLuxuryCurrentFee;
	}

	public BigDecimal getSemiLuxuryNewFee() {
		return semiLuxuryNewFee;
	}

	public void setSemiLuxuryNewFee(BigDecimal semiLuxuryNewFee) {
		this.semiLuxuryNewFee = semiLuxuryNewFee;
	}

	public BigDecimal getSemiLuxuryRoundFee() {
		return semiLuxuryRoundFee;
	}

	public void setSemiLuxuryRoundFee(BigDecimal semiLuxuryRoundFee) {
		this.semiLuxuryRoundFee = semiLuxuryRoundFee;
	}

	public BigDecimal getSuperLuxuryCurrentFee() {
		return superLuxuryCurrentFee;
	}

	public void setSuperLuxuryCurrentFee(BigDecimal superLuxuryCurrentFee) {
		this.superLuxuryCurrentFee = superLuxuryCurrentFee;
	}

	public BigDecimal getSuperLuxuryNewFee() {
		return superLuxuryNewFee;
	}

	public void setSuperLuxuryNewFee(BigDecimal superLuxuryNewFee) {
		this.superLuxuryNewFee = superLuxuryNewFee;
	}

	public BigDecimal getSuperLuxuryRoundFee() {
		return superLuxuryRoundFee;
	}

	public void setSuperLuxuryRoundFee(BigDecimal superLuxuryRoundFee) {
		this.superLuxuryRoundFee = superLuxuryRoundFee;
	}

	public BigDecimal getHighwayCurrentFee() {
		return highwayCurrentFee;
	}

	public void setHighwayCurrentFee(BigDecimal highwayCurrentFee) {
		this.highwayCurrentFee = highwayCurrentFee;
	}

	public BigDecimal getHighwayNewFee() {
		return highwayNewFee;
	}

	public void setHighwayNewFee(BigDecimal highwayNewFee) {
		this.highwayNewFee = highwayNewFee;
	}

	public BigDecimal getHighwayRoundFee() {
		return highwayRoundFee;
	}

	public void setHighwayRoundFee(BigDecimal highwayRoundFee) {
		this.highwayRoundFee = highwayRoundFee;
	}

	public BigDecimal getSisuSariyaHalfNoramlFee() {
		return sisuSariyaHalfNoramlFee;
	}

	public void setSisuSariyaHalfNoramlFee(BigDecimal sisuSariyaHalfNoramlFee) {
		this.sisuSariyaHalfNoramlFee = sisuSariyaHalfNoramlFee;
	}

	public BigDecimal getSisuSariyaHalfAdjestedFee() {
		return sisuSariyaHalfAdjestedFee;
	}

	public void setSisuSariyaHalfAdjestedFee(BigDecimal sisuSariyaHalfAdjestedFee) {
		this.sisuSariyaHalfAdjestedFee = sisuSariyaHalfAdjestedFee;
	}

	public BigDecimal getSisuSariyaHalfdiffrentFee() {
		return sisuSariyaHalfdiffrentFee;
	}

	public void setSisuSariyaHalfdiffrentFee(BigDecimal sisuSariyaHalfdiffrentFee) {
		this.sisuSariyaHalfdiffrentFee = sisuSariyaHalfdiffrentFee;
	}

	public BigDecimal getSisuSariyaQuarterNoramlFee() {
		return sisuSariyaQuarterNoramlFee;
	}

	public void setSisuSariyaQuarterNoramlFee(BigDecimal sisuSariyaQuarterNoramlFee) {
		this.sisuSariyaQuarterNoramlFee = sisuSariyaQuarterNoramlFee;
	}

	public BigDecimal getSisuSariyaQuarterAdjestedFee() {
		return sisuSariyaQuarterAdjestedFee;
	}

	public void setSisuSariyaQuarterAdjestedFee(BigDecimal sisuSariyaQuarterAdjestedFee) {
		this.sisuSariyaQuarterAdjestedFee = sisuSariyaQuarterAdjestedFee;
	}

	public BigDecimal getSisuSariyaQuarterdiffrentFee() {
		return sisuSariyaQuarterdiffrentFee;
	}

	public void setSisuSariyaQuarterdiffrentFee(BigDecimal sisuSariyaQuarterdiffrentFee) {
		this.sisuSariyaQuarterdiffrentFee = sisuSariyaQuarterdiffrentFee;
	}

	public int getStageNo() {
		return stageNo;
	}

	public void setStageNo(int stageNo) {
		this.stageNo = stageNo;
	}

	public String getTempFareReferenceNo() {
		return tempFareReferenceNo;
	}

	public Date getAmmendmentsDateObj() {
		return ammendmentsDateObj;
	}

	public void setTempFareReferenceNo(String tempFareReferenceNo) {
		this.tempFareReferenceNo = tempFareReferenceNo;
	}

	public boolean isDisabledFareCalculate() {
		return disabledFareCalculate;
	}

	public void setDisabledFareCalculate(boolean disabledFareCalculate) {
		this.disabledFareCalculate = disabledFareCalculate;
	}

	public String getBusOrder() {
		return busOrder;
	}

	public void setBusOrder(String busOrder) {
		this.busOrder = busOrder;
	}

	public String getTempBusOrder() {
		return tempBusOrder;
	}

	public void setTempBusOrder(String tempBusOrder) {
		this.tempBusOrder = tempBusOrder;
	}

	public void setAmmendmentsDateObj(Date ammendmentsDateObj) {
		this.ammendmentsDateObj = ammendmentsDateObj;
	}

	public String getAmmendmentsDateVal() {
		return ammendmentsDateVal;
	}

	public void setAmmendmentsDateVal(String ammendmentsDateVal) {
		this.ammendmentsDateVal = ammendmentsDateVal;
	}

	public double getCurrentCostPresentage() {
		return currentCostPresentage;
	}

	public void setCurrentCostPresentage(double currentCostPresentage) {
		this.currentCostPresentage = currentCostPresentage;
	}

	public String getStatusDes() {
		return statusDes;
	}

	public void setStatusDes(String statusDes) {
		this.statusDes = statusDes;
	}

	public Date getCreatedDateObj() {
		return createdDateObj;
	}

	public void setCreatedDateObj(Date createdDateObj) {
		this.createdDateObj = createdDateObj;
	}

	public String getCreatedDateVal() {
		return createdDateVal;
	}

	public void setCreatedDateVal(String createdDateVal) {
		this.createdDateVal = createdDateVal;
	}

	public Date getStartDateObj() {
		return startDateObj;
	}

	public void setStartDateObj(Date startDateObj) {
		this.startDateObj = startDateObj;
	}

	public String getStartDateVal() {
		return startDateVal;
	}

	public void setStartDateVal(String startDateVal) {
		this.startDateVal = startDateVal;
	}

	public Date getEndDateObj() {
		return endDateObj;
	}

	public void setEndDateObj(Date endDateObj) {
		this.endDateObj = endDateObj;
	}

	public String getEndDateVal() {
		return endDateVal;
	}

	public void setEndDateVal(String endDateVal) {
		this.endDateVal = endDateVal;
	}

	public String getPrevActiveBusFareRefNo() {
		return prevActiveBusFareRefNo;
	}

	public void setPrevActiveBusFareRefNo(String prevActiveBusFareRefNo) {
		this.prevActiveBusFareRefNo = prevActiveBusFareRefNo;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getCreatedDateTS() {
		return createdDateTS;
	}

	public void setCreatedDateTS(Timestamp createdDateTS) {
		this.createdDateTS = createdDateTS;
	}

	public Timestamp getModifiedDateTS() {
		return modifiedDateTS;
	}

	public void setModifiedDateTS(Timestamp modifiedDateTS) {
		this.modifiedDateTS = modifiedDateTS;
	}

	public BigDecimal getDifferenceWithCurrentFare() {
		return differenceWithCurrentFare;
	}

	public void setDifferenceWithCurrentFare(BigDecimal differenceWithCurrentFare) {
		this.differenceWithCurrentFare = differenceWithCurrentFare;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateDateString() {
		return createDateString;
	}

	public void setCreateDateString(String createDateString) {
		this.createDateString = createDateString;
	}

	public BigDecimal getEditNormalRoundFee() {
		return editNormalRoundFee;
	}

	public void setEditNormalRoundFee(BigDecimal editNormalRoundFee) {
		this.editNormalRoundFee = editNormalRoundFee;
	}

	public BigDecimal getEditSemiLuxuaryRoundFee() {
		return editSemiLuxuaryRoundFee;
	}

	public void setEditSemiLuxuaryRoundFee(BigDecimal editSemiLuxuaryRoundFee) {
		this.editSemiLuxuaryRoundFee = editSemiLuxuaryRoundFee;
	}

	public BigDecimal getEditLuxuryRoundFee() {
		return editLuxuryRoundFee;
	}

	public void setEditLuxuryRoundFee(BigDecimal editLuxuryRoundFee) {
		this.editLuxuryRoundFee = editLuxuryRoundFee;
	}

	public BigDecimal getEditSuperLuxuryRoundFee() {
		return editSuperLuxuryRoundFee;
	}

	public void setEditSuperLuxuryRoundFee(BigDecimal editSuperLuxuryRoundFee) {
		this.editSuperLuxuryRoundFee = editSuperLuxuryRoundFee;
	}

	public BigDecimal getEdithighWayRoundFee() {
		return edithighWayRoundFee;
	}

	public void setEdithighWayRoundFee(BigDecimal edithighWayRoundFee) {
		this.edithighWayRoundFee = edithighWayRoundFee;
	}

	public BigDecimal getEditSisuSariyaHalfAdjestFee() {
		return editSisuSariyaHalfAdjestFee;
	}

	public void setEditSisuSariyaHalfAdjestFee(BigDecimal editSisuSariyaHalfAdjestFee) {
		this.editSisuSariyaHalfAdjestFee = editSisuSariyaHalfAdjestFee;
	}

	public BigDecimal getEditSisuSariyaQuaterAdjestFee() {
		return editSisuSariyaQuaterAdjestFee;
	}

	public void setEditSisuSariyaQuaterAdjestFee(BigDecimal editSisuSariyaQuaterAdjestFee) {
		this.editSisuSariyaQuaterAdjestFee = editSisuSariyaQuaterAdjestFee;
	}

	public String getEquation() {
		return equation;
	}

	public void setEquation(String equation) {
		this.equation = equation;
	}

	public List<String> getAbbreviationList() {
		return abbreviationList;
	}

	public void setAbbreviationList(List<String> abbreviationList) {
		this.abbreviationList = abbreviationList;
	}

	public String getTempEquation() {
		return tempEquation;
	}

	public void setTempEquation(String tempEquation) {
		this.tempEquation = tempEquation;
	}

	public List<String> getTempAbbreviationList() {
		return tempAbbreviationList;
	}

	public void setTempAbbreviationList(List<String> tempAbbreviationList) {
		this.tempAbbreviationList = tempAbbreviationList;
	}

}
