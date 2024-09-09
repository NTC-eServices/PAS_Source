package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class VehicleInspectionDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String permitOwner;
	private String nicreg;
	private String permitNo;
	private String routeDetails;
	private String routeNo;
	private String routeFlag;
	private String serviceType;
	private String queueNo;
	private String vehicleNo;
	private String make;
	private String model;
	private String manyear;
	private String chassisNo;
	private String applicationNo;
	private String oldApplicationNo;
	private Long permitSeqNo;
	private String gridno;
	private int newGridNo;
	private String section;
	private String description;
	private String remark;
	private boolean exists;
	private String existsText;
	private String date1;
	private String date2;
	private String serviceTypeCode;
	private String routeTypeCode;
	private String makeTypeCode;
	private String modelTypeCode;
	private String productDate;
	private String finalremarkDescription;
	private String serviceTypeDescription;
	private String makeDescription;
	private String modelDescription;
	private Date calender1;
	private Date calender2;
	private String finalRemark;
	private String proceedRemark;
	private String loginUser;
	private String sectionCode;
	private BigDecimal busFare;
	private String validTo;
	private String expireDate;
	private String tenderPermit;
	private String validFrom;

	private long taskSeq;
	private String taskCode;
	private Timestamp createDate;
	private String createBy;
	private String taskStatus;

	private boolean backlogApp = false;

	private String backlogStatus;
	private String origine;
	private String destination;
	private String routeVia;

	private String permitIssueDate;

	private String inspectionStatus;

	private String isNewPermit;
	private String actionPointSeqNo;

	private String functionCode;
	private String activityCode;

	private String taskDetCode;
	private String taskDetStatus;

	// check edit btn is valid or not
	private boolean disabledEditBtnInGrid = false;

	private String transtractionTypeCode;

	private int renewalPeriod;
	private String inspecLocationCode;
	private String inspecLocationDes;
	private int inspectionLocationorder;
	private String inspectioncomplIncomplStatus;
	private String normalInspectionType;

	private boolean isApplicationNoFound;
	private boolean isDataFound;
	private String inspectionTypeCode;
	private String inspectionStatusCode;

	private String inspectionTypeDes;
	private String ownerName;

	/** Queue master column start **/
	private String queueStatus;
	private String queueTaskCode;
	private String quueTaskStatus;
	private boolean isQueueDataFound;

	/** Queue master column end **/
	
	private String applicationStatus;
	private long vehicleInspectionSeq;
	private String tenderRefNo;
	
	private String engineNo;
	
	private String inspectedBy;
	private String inspectedDate;
	
	public boolean isApplicationNoFound() {
		return isApplicationNoFound;
	}

	public void setApplicationNoFound(boolean isApplicationNoFound) {
		this.isApplicationNoFound = isApplicationNoFound;
	}

	public boolean isDataFound() {
		return isDataFound;
	}

	public void setDataFound(boolean isDataFound) {
		this.isDataFound = isDataFound;
	}

	public String getInspectionTypeCode() {
		return inspectionTypeCode;
	}

	public void setInspectionTypeCode(String inspectionTypeCode) {
		this.inspectionTypeCode = inspectionTypeCode;
	}

	public String getInspectionStatusCode() {
		return inspectionStatusCode;
	}

	public void setInspectionStatusCode(String inspectionStatusCode) {
		this.inspectionStatusCode = inspectionStatusCode;
	}

	public String getInspectionTypeDes() {
		return inspectionTypeDes;
	}

	public void setInspectionTypeDes(String inspectionTypeDes) {
		this.inspectionTypeDes = inspectionTypeDes;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOrigine() {
		return origine;
	}

	public void setOrigine(String origine) {
		this.origine = origine;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getRouteVia() {
		return routeVia;
	}

	public void setRouteVia(String routeVia) {
		this.routeVia = routeVia;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public long getTaskSeq() {
		return taskSeq;
	}

	public void setTaskSeq(long taskSeq) {
		this.taskSeq = taskSeq;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public int getNewGridNo() {
		return newGridNo;
	}

	public void setNewGridNo(int newGridNo) {
		this.newGridNo = newGridNo;
	}

	public String getGridno() {
		return gridno;
	}

	public void setGridno(String gridno) {
		this.gridno = gridno;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public String getPermitOwner() {
		return permitOwner;
	}

	public void setPermitOwner(String permitOwner) {
		this.permitOwner = permitOwner;
	}

	public String getNicreg() {
		return nicreg;
	}

	public void setNicreg(String nicreg) {
		this.nicreg = nicreg;
	}

	public String getPermitNo() {
		return permitNo;
	}

	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}

	public String getRouteDetails() {
		return routeDetails;
	}

	public void setRouteDetails(String routeDetails) {
		this.routeDetails = routeDetails;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManyear() {
		return manyear;
	}

	public void setManyear(String manyear) {
		this.manyear = manyear;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public Long getPermitSeqNo() {
		return permitSeqNo;
	}

	public void setPermitSeqNo(Long permitSeqNo) {
		this.permitSeqNo = permitSeqNo;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public String getServiceTypeDescription() {
		return serviceTypeDescription;
	}

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public void setServiceTypeDescription(String serviceTypeDescription) {
		this.serviceTypeDescription = serviceTypeDescription;

	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public String getMakeDescription() {
		return makeDescription;
	}

	public String getRouteTypeCode() {
		return routeTypeCode;
	}

	public void setMakeDescription(String makeDescription) {
		this.makeDescription = makeDescription;

	}

	public void setRouteTypeCode(String routeTypeCode) {
		this.routeTypeCode = routeTypeCode;

	}

	public String getModelDescription() {
		return modelDescription;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public String getMakeTypeCode() {
		return makeTypeCode;
	}

	public void setMakeTypeCode(String makeTypeCode) {
		this.makeTypeCode = makeTypeCode;
	}

	public String getModelTypeCode() {
		return modelTypeCode;
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getProductDate() {
		return productDate;
	}

	public void setProductDate(String productDate) {
		this.productDate = productDate;
	}

	public String getFinalremarkDescription() {
		return finalremarkDescription;
	}

	public void setFinalremarkDescription(String finalremarkDescription) {
		this.finalremarkDescription = finalremarkDescription;
	}

	public String getExistsText() {
		return existsText;
	}

	public void setExistsText(String existsText) {
		this.existsText = existsText;
	}

	public String getFinalRemark() {
		return finalRemark;
	}

	public void setFinalRemark(String finalRemark) {
		this.finalRemark = finalRemark;
	}

	public Date getCalender1() {
		return calender1;
	}

	public void setCalender1(Date calender1) {
		this.calender1 = calender1;
	}

	public Date getCalender2() {
		return calender2;
	}

	public void setCalender2(Date calender2) {
		this.calender2 = calender2;
	}

	@Override
	public String toString() {
		return "VehicleInspectionDTO [permitOwner=" + permitOwner + ", nicreg=" + nicreg + ", permitNo=" + permitNo
				+ ", routeDetails=" + routeDetails + ", routeNo=" + routeNo + ", serviceType=" + serviceType
				+ ", queueNo=" + queueNo + ", vehicleNo=" + vehicleNo + ", make=" + make + ", model=" + model
				+ ", manyear=" + manyear + ", chassisNo=" + chassisNo + ", applicationNo=" + applicationNo
				+ ", permitSeqNo=" + permitSeqNo + ", gridno=" + gridno + ", section=" + section + ", description="
				+ description + ", remark=" + remark + ", exists=" + exists + ", existsText=" + existsText + ", date1="
				+ date1 + ", date2=" + date2 + ", serviceTypeCode=" + serviceTypeCode + ", routeTypeCode="
				+ routeTypeCode + ", makeTypeCode=" + makeTypeCode + ", modelTypeCode=" + modelTypeCode
				+ ", productDate=" + productDate + ", finalremarkDescription=" + finalremarkDescription
				+ ", serviceTypeDescription=" + serviceTypeDescription + ", makeDescription=" + makeDescription
				+ ", modelDescription=" + modelDescription + ", calender1=" + calender1 + ", calender2=" + calender2
				+ ", finalRemark=" + finalRemark + ", loginUser=" + loginUser + "]";
	}

	public String getOldApplicationNo() {
		return oldApplicationNo;
	}

	public void setOldApplicationNo(String oldApplicationNo) {
		this.oldApplicationNo = oldApplicationNo;
	}

	public boolean isBacklogApp() {
		return backlogApp;
	}

	public void setBacklogApp(boolean backlogApp) {
		this.backlogApp = backlogApp;
	}

	public String getBacklogStatus() {
		return backlogStatus;
	}

	public void setBacklogStatus(String backlogStatus) {
		this.backlogStatus = backlogStatus;
	}

	public BigDecimal getBusFare() {
		return busFare;
	}

	public String getRouteFlag() {
		return routeFlag;
	}

	public void setBusFare(BigDecimal busFare) {
		this.busFare = busFare;
	}

	public void setRouteFlag(String routeFlag) {
		this.routeFlag = routeFlag;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;

	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getPermitIssueDate() {
		return permitIssueDate;
	}

	public void setPermitIssueDate(String permitIssueDate) {
		this.permitIssueDate = permitIssueDate;
	}

	public String getTenderPermit() {
		return tenderPermit;
	}

	public void setTenderPermit(String tenderPermit) {
		this.tenderPermit = tenderPermit;
	}

	public String getInspectionStatus() {
		return inspectionStatus;
	}

	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}

	public String getIsNewPermit() {
		return isNewPermit;
	}

	public void setIsNewPermit(String isNewPermit) {
		this.isNewPermit = isNewPermit;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getActionPointSeqNo() {
		return actionPointSeqNo;
	}

	public void setActionPointSeqNo(String actionPointSeqNo) {
		this.actionPointSeqNo = actionPointSeqNo;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public boolean isDisabledEditBtnInGrid() {
		return disabledEditBtnInGrid;
	}

	public void setDisabledEditBtnInGrid(boolean disabledEditBtnInGrid) {
		this.disabledEditBtnInGrid = disabledEditBtnInGrid;
	}

	public String getTaskDetCode() {
		return taskDetCode;
	}

	public void setTaskDetCode(String taskDetCode) {
		this.taskDetCode = taskDetCode;
	}

	public String getTaskDetStatus() {
		return taskDetStatus;
	}

	public void setTaskDetStatus(String taskDetStatus) {
		this.taskDetStatus = taskDetStatus;
	}

	public String getTranstractionTypeCode() {
		return transtractionTypeCode;
	}

	public void setTranstractionTypeCode(String transtractionTypeCode) {
		this.transtractionTypeCode = transtractionTypeCode;
	}

	public int getRenewalPeriod() {
		return renewalPeriod;
	}

	public void setRenewalPeriod(int renewalPeriod) {
		this.renewalPeriod = renewalPeriod;
	}

	public String getInspecLocationCode() {
		return inspecLocationCode;
	}

	public void setInspecLocationCode(String inspecLocationCode) {
		this.inspecLocationCode = inspecLocationCode;
	}

	public String getInspecLocationDes() {
		return inspecLocationDes;
	}

	public void setInspecLocationDes(String inspecLocationDes) {
		this.inspecLocationDes = inspecLocationDes;
	}

	public int getInspectionLocationorder() {
		return inspectionLocationorder;
	}

	public void setInspectionLocationorder(int inspectionLocationorder) {
		this.inspectionLocationorder = inspectionLocationorder;
	}

	public String getInspectioncomplIncomplStatus() {
		return inspectioncomplIncomplStatus;
	}

	public void setInspectioncomplIncomplStatus(String inspectioncomplIncomplStatus) {
		this.inspectioncomplIncomplStatus = inspectioncomplIncomplStatus;
	}

	public String getNormalInspectionType() {
		return normalInspectionType;
	}

	public void setNormalInspectionType(String normalInspectionType) {
		this.normalInspectionType = normalInspectionType;
	}

	public String getQueueStatus() {
		return queueStatus;
	}

	public void setQueueStatus(String queueStatus) {
		this.queueStatus = queueStatus;
	}

	public String getQueueTaskCode() {
		return queueTaskCode;
	}

	public void setQueueTaskCode(String queueTaskCode) {
		this.queueTaskCode = queueTaskCode;
	}

	public String getQuueTaskStatus() {
		return quueTaskStatus;
	}

	public void setQuueTaskStatus(String quueTaskStatus) {
		this.quueTaskStatus = quueTaskStatus;
	}

	public boolean isQueueDataFound() {
		return isQueueDataFound;
	}

	public void setQueueDataFound(boolean isQueueDataFound) {
		this.isQueueDataFound = isQueueDataFound;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public long getVehicleInspectionSeq() {
		return vehicleInspectionSeq;
	}

	public void setVehicleInspectionSeq(long vehicleInspectionSeq) {
		this.vehicleInspectionSeq = vehicleInspectionSeq;
	}

	public String getProceedRemark() {
		return proceedRemark;
	}

	public void setProceedRemark(String proceedRemark) {
		this.proceedRemark = proceedRemark;
	}

	public String getTenderRefNo() {
		return tenderRefNo;
	}

	public void setTenderRefNo(String tenderRefNo) {
		this.tenderRefNo = tenderRefNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getInspectedBy() {
		return inspectedBy;
	}

	public void setInspectedBy(String inspectedBy) {
		this.inspectedBy = inspectedBy;
	}

	public String getInspectedDate() {
		return inspectedDate;
	}

	public void setInspectedDate(String inspectedDate) {
		this.inspectedDate = inspectedDate;
	}
	
}
