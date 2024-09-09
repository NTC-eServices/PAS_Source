package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.util.List;

public class ProjectDTO implements Serializable {
	private static final long serialVersionUID = 12312312441L;

	private Long awarenessProgram_seq;
	private String refNo;
	private String projectName;
	private int noOfProject;
	private String year;
	private String status;
	private List<ProgramDTO> subProgramList;
	
	public Long getAwarenessProgram_seq() {
		return awarenessProgram_seq;
	}
	public void setAwarenessProgram_seq(Long awarenessProgram_seq) {
		this.awarenessProgram_seq = awarenessProgram_seq;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getNoOfProject() {
		return noOfProject;
	}
	public void setNoOfProject(int noOfProject) {
		this.noOfProject = noOfProject;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ProgramDTO> getSubProgramList() {
		return subProgramList;
	}
	public void setSubProgramList(List<ProgramDTO> subProgramList) {
		this.subProgramList = subProgramList;
	}
}