package lk.informatics.ntc.model.dto;

public class FlyingSquadGroupsDTO {

	String groupCd;
	String groupName;
	String status;
	String statusDesc;
	String displayGroupName;
	
	public String getDisplayGroupName() {
		return displayGroupName;
	}
	public void setDisplayGroupName(String displayGroupName) {
		this.displayGroupName = displayGroupName;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getGroupCd() {
		return groupCd;
	}
	public void setGroupCd(String groupCd) {
		this.groupCd = groupCd;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
