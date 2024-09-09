package lk.informatics.ntc.model.dto;

public class EventParticipationDTO extends ProgramDTO {
	
	private static final long serialVersionUID = 526526959852L;
	
	private long epd_seq;
	private String typeOfParticipation;
	private String empNo;
	private String epfNo;
	private String participantName;
	private String idNo;
	private String officerType;
	
	public long getEpd_seq() {
		return epd_seq;
	}
	public void setEpd_seq(long epd_seq) {
		this.epd_seq = epd_seq;
	}
	public String getTypeOfParticipation() {
		return typeOfParticipation;
	}
	public void setTypeOfParticipation(String typeOfParticipation) {
		this.typeOfParticipation = typeOfParticipation;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getEpfNo() {
		return epfNo;
	}
	public void setEpfNo(String epfNo) {
		this.epfNo = epfNo;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getOfficerType() {
		return officerType;
	}
	public void setOfficerType(String officerType) {
		this.officerType = officerType;
	}
	
}
