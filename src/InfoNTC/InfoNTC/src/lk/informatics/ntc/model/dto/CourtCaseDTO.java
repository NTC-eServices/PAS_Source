package lk.informatics.ntc.model.dto;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class CourtCaseDTO implements Serializable{
	private static final long serialVersionUID = 568345455L;
	
	private Long courtCaseSeq;
	private String vehicleNo;
	private String permitNo;
	private String routeNo;
	private String courtName;
	private String caseNo;
	private Date dateofCourtCase;
	private String groupCode;
	private String status;
	private String createdBy;
	private String modifyBy;
	private Timestamp createdDate;
	private Timestamp modifiedDate;
	private String groupName;
	private String statusdes;
	private String dateOfCourt;
	private String refrenceNo;
	private Date inspectStartDate;
	private Date inspectEndDate;
	private Date courtStartDate;
	private Date courtEndDate;
	private String orgin;
	private String destination;
	private String remarks;
	private String actionByCourt;
	private Date nextCallingDate;
	private String courtNameS;
	private String caseNoS;
	private Date inspectionDate;
	private String inspDate;
	private Date courtCaseCloseDate;
	private String nextCallingDateS;
	private long courtCaseSeqS;
	private Number totHours;
	private Number totHoursCum;
	private Number employeeCount;
	private String invesDate;
	private Number inspectedVehiCount;
	private Number vioConditionCount;
	private Number vioVehiConditionCount;
	private Number detectedVehiCount;
	
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifyBy() {
		return modifyBy;
	}
	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
	public Long getCourtCaseSeq() {
		return courtCaseSeq;
	}
	public void setCourtCaseSeq(Long courtCaseSeq) {
		this.courtCaseSeq = courtCaseSeq;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
	public String getRouteNo() {
		return routeNo;
	}
	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Date getDateofCourtCase() {
		return dateofCourtCase;
	}
	public void setDateofCourtCase(Date dateofCourtCase) {
		this.dateofCourtCase = dateofCourtCase;
	}
	public String getStatusdes() {
		return statusdes;
	}
	public void setStatusdes(String statusdes) {
		this.statusdes = statusdes;
	}
	public String getDateOfCourt() {
		return dateOfCourt;
	}
	public void setDateOfCourt(String dateOfCourt) {
		this.dateOfCourt = dateOfCourt;
	}
	public String getRefrenceNo() {
		return refrenceNo;
	}
	public void setRefrenceNo(String refrenceNo) {
		this.refrenceNo = refrenceNo;
	}
	public Date getInspectStartDate() {
		return inspectStartDate;
	}
	public void setInspectStartDate(Date inspectStartDate) {
		this.inspectStartDate = inspectStartDate;
	}
	public Date getInspectEndDate() {
		return inspectEndDate;
	}
	public void setInspectEndDate(Date inspectEndDate) {
		this.inspectEndDate = inspectEndDate;
	}
	public Date getCourtStartDate() {
		return courtStartDate;
	}
	public void setCourtStartDate(Date courtStartDate) {
		this.courtStartDate = courtStartDate;
	}
	public Date getCourtEndDate() {
		return courtEndDate;
	}
	public void setCourtEndDate(Date courtEndDate) {
		this.courtEndDate = courtEndDate;
	}
	public String getOrgin() {
		return orgin;
	}
	public void setOrgin(String orgin) {
		this.orgin = orgin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getActionByCourt() {
		return actionByCourt;
	}
	public void setActionByCourt(String actionByCourt) {
		this.actionByCourt = actionByCourt;
	}
	public Date getNextCallingDate() {
		return nextCallingDate;
	}
	public void setNextCallingDate(Date nextCallingDate) {
		this.nextCallingDate = nextCallingDate;
	}
	public String getCourtNameS() {
		return courtNameS;
	}
	public void setCourtNameS(String courtNameS) {
		this.courtNameS = courtNameS;
	}
	public String getCaseNoS() {
		return caseNoS;
	}
	public void setCaseNoS(String caseNoS) {
		this.caseNoS = caseNoS;
	}
	public Date getInspectionDate() {
		return inspectionDate;
	}
	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}
	public String getInspDate() {
		return inspDate;
	}
	public void setInspDate(String inspDate) {
		this.inspDate = inspDate;
	}
	public Date getCourtCaseCloseDate() {
		return courtCaseCloseDate;
	}
	public void setCourtCaseCloseDate(Date courtCaseCloseDate) {
		this.courtCaseCloseDate = courtCaseCloseDate;
	}
	public String getNextCallingDateS() {
		return nextCallingDateS;
	}
	public void setNextCallingDateS(String nextCallingDateS) {
		this.nextCallingDateS = nextCallingDateS;
	}
	public long getCourtCaseSeqS() {
		return courtCaseSeqS;
	}
	public void setCourtCaseSeqS(long courtCaseSeqS) {
		this.courtCaseSeqS = courtCaseSeqS;
	}
	public Number getTotHours() {
		return totHours;
	}
	public void setTotHours(Number totHours) {
		this.totHours = totHours;
	}
	public Number getTotHoursCum() {
		return totHoursCum;
	}
	public void setTotHoursCum(Number totHoursCum) {
		this.totHoursCum = totHoursCum;
	}
	public Number getEmployeeCount() {
		return employeeCount;
	}
	public void setEmployeeCount(Number employeeCount) {
		this.employeeCount = employeeCount;
	}
	public String getInvesDate() {
		return invesDate;
	}
	public void setInvesDate(String invesDate) {
		this.invesDate = invesDate;
	}
	public Number getInspectedVehiCount() {
		return inspectedVehiCount;
	}
	public void setInspectedVehiCount(Number inspectedVehiCount) {
		this.inspectedVehiCount = inspectedVehiCount;
	}
	public Number getVioConditionCount() {
		return vioConditionCount;
	}
	public void setVioConditionCount(Number vioConditionCount) {
		this.vioConditionCount = vioConditionCount;
	}
	public Number getVioVehiConditionCount() {
		return vioVehiConditionCount;
	}
	public void setVioVehiConditionCount(Number vioVehiConditionCount) {
		this.vioVehiConditionCount = vioVehiConditionCount;
	}
	public Number getDetectedVehiCount() {
		return detectedVehiCount;
	}
	public void setDetectedVehiCount(Number detectedVehiCount) {
		this.detectedVehiCount = detectedVehiCount;
	}
	
	

}
