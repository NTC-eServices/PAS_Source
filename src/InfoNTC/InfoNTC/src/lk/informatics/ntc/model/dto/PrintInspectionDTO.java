package lk.informatics.ntc.model.dto;

public class PrintInspectionDTO {
	private String applicationNo;
	private String vehicleNo;
	private String owner;
	private String permitNo;
	private String printingSeq;
	private String printingTaskCode;
	private String taskStatus;
	private String ownerSeq;
	private String ownerApplicationNo;
	private String ownerPermitNo;
	private String ownerVehicleNo;
	private String ownerTaskCode;
	private String ownerTaskStatus;
	private String inspectionType;
	
	
	public String getOwnerTaskCode() {
		return ownerTaskCode;
	}
	public void setOwnerTaskCode(String ownerTaskCode) {
		this.ownerTaskCode = ownerTaskCode;
	}
	public String getOwnerTaskStatus() {
		return ownerTaskStatus;
	}
	public void setOwnerTaskStatus(String ownerTaskStatus) {
		this.ownerTaskStatus = ownerTaskStatus;
	}
	public String getOwnerApplicationNo() {
		return ownerApplicationNo;
	}
	public void setOwnerApplicationNo(String ownerApplicationNo) {
		this.ownerApplicationNo = ownerApplicationNo;
	}
	public String getOwnerPermitNo() {
		return ownerPermitNo;
	}
	public void setOwnerPermitNo(String ownerPermitNo) {
		this.ownerPermitNo = ownerPermitNo;
	}
	public String getOwnerVehicleNo() {
		return ownerVehicleNo;
	}
	public void setOwnerVehicleNo(String ownerVehicleNo) {
		this.ownerVehicleNo = ownerVehicleNo;
	}
	public String getOwnerSeq() {
		return ownerSeq;
	}
	public void setOwnerSeq(String ownerSeq) {
		this.ownerSeq = ownerSeq;
	}
	public String getPrintingSeq() {
		return printingSeq;
	}
	public void setPrintingSeq(String printingSeq) {
		this.printingSeq = printingSeq;
	}
	public String getPrintingTaskCode() {
		return printingTaskCode;
	}
	public void setPrintingTaskCode(String printingTaskCode) {
		this.printingTaskCode = printingTaskCode;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
	public String getInspectionType() {
		return inspectionType;
	}
	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	
}
