package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class LogSheetDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/* For log sheet */
	private String logSheetRefNo;
	private int logSheetSeqNo;
	private int noOfCopies;
	private long logSheetDetSeq;
	private int id;

	public String getLogSheetRefNo() {
		return logSheetRefNo;
	}

	public void setLogSheetRefNo(String logSheetRefNo) {
		this.logSheetRefNo = logSheetRefNo;
	}

	public int getLogSheetSeqNo() {
		return logSheetSeqNo;
	}

	public void setLogSheetSeqNo(int logSheetSeqNo) {
		this.logSheetSeqNo = logSheetSeqNo;
	}

	public int getNoOfCopies() {
		return noOfCopies;
	}

	public void setNoOfCopies(int noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

	public long getLogSheetDetSeq() {
		return logSheetDetSeq;
	}

	public void setLogSheetDetSeq(long logSheetDetSeq) {
		this.logSheetDetSeq = logSheetDetSeq;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

}
