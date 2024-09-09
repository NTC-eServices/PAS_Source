package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class TerminalDetailsDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String stationCode;
	private int noOfTerminals;
	private String terminalDisplayType= "N"; 	
	private String terminalIdStartWith;
	private int noOfPlatforms;
	private String platformDisplayType= "A"; 	
	private String platformIdStartWith;
	private String status;
	
	private long stationTerminalseq;
	private String createdBy;
	private Timestamp createdDate;
	private String modifiedBy;
	private Timestamp modifiedDate;
	
	private long seq;
	private String terminal;
	private String platform;
	
	private String blockStartValue;
	private String blockEndValue;
	// for accepet payment view 
	private String receiptRefNo;
	private String permitNo;
	private String routeOrigin;
	private String routeDestination;
	private String gender;
	private String validFrom;
	private String validTo;
	private String noOfTurns;
	private String vehiNo;
	private String voucherNO;
    private String dueDate;
    private String owner;
    private String contactNo;
    private String issuDate;
    private String issueMont;
    private String penalty;
    private String amountPaid;
    private String paidDate;
    private String terminalLocation;
  
	public String getStationCode() {
		return stationCode;
	}
	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}
	public String getTerminalDisplayType() {
		return terminalDisplayType;
	}
	public void setTerminalDisplayType(String terminalDisplayType) {
		this.terminalDisplayType = terminalDisplayType;
	}
	public String getTerminalIdStartWith() {
		return terminalIdStartWith;
	}
	public void setTerminalIdStartWith(String terminalIdStartWith) {
		this.terminalIdStartWith = terminalIdStartWith;
	}
	
	public String getPlatformDisplayType() {
		return platformDisplayType;
	}
	public void setPlatformDisplayType(String platformDisplayType) {
		this.platformDisplayType = platformDisplayType;
	}
	public String getPlatformIdStartWith() {
		return platformIdStartWith;
	}
	public int getNoOfTerminals() {
		return noOfTerminals;
	}
	public void setNoOfTerminals(int noOfTerminals) {
		this.noOfTerminals = noOfTerminals;
	}
	public int getNoOfPlatforms() {
		return noOfPlatforms;
	}
	public void setNoOfPlatforms(int noOfPlatforms) {
		this.noOfPlatforms = noOfPlatforms;
	}
	public void setPlatformIdStartWith(String platformIdStartWith) {
		this.platformIdStartWith = platformIdStartWith;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public long getStationTerminalseq() {
		return stationTerminalseq;
	}
	public void setStationTerminalseq(long stationTerminalseq) {
		this.stationTerminalseq = stationTerminalseq;
	}
	public String getBlockStartValue() {
		return blockStartValue;
	}
	public void setBlockStartValue(String blockStartValue) {
		this.blockStartValue = blockStartValue;
	}
	public String getBlockEndValue() {
		return blockEndValue;
	}
	public void setBlockEndValue(String blockEndValue) {
		this.blockEndValue = blockEndValue;
	}
	public String getReceiptRefNo() {
		return receiptRefNo;
	}
	public void setReceiptRefNo(String receiptRefNo) {
		this.receiptRefNo = receiptRefNo;
	}
	public String getPermitNo() {
		return permitNo;
	}
	public void setPermitNo(String permitNo) {
		this.permitNo = permitNo;
	}
	public String getRouteOrigin() {
		return routeOrigin;
	}
	public void setRouteOrigin(String routeOrigin) {
		this.routeOrigin = routeOrigin;
	}
	public String getRouteDestination() {
		return routeDestination;
	}
	public void setRouteDestination(String routeDestination) {
		this.routeDestination = routeDestination;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}
	public String getValidTo() {
		return validTo;
	}
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}
	public String getNoOfTurns() {
		return noOfTurns;
	}
	public void setNoOfTurns(String noOfTurns) {
		this.noOfTurns = noOfTurns;
	}
	public String getVehiNo() {
		return vehiNo;
	}
	public void setVehiNo(String vehiNo) {
		this.vehiNo = vehiNo;
	}
	public String getVoucherNO() {
		return voucherNO;
	}
	public void setVoucherNO(String voucherNO) {
		this.voucherNO = voucherNO;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getIssuDate() {
		return issuDate;
	}
	public void setIssuDate(String issuDate) {
		this.issuDate = issuDate;
	}
	public String getIssueMont() {
		return issueMont;
	}
	public void setIssueMont(String issueMont) {
		this.issueMont = issueMont;
	}
	public String getPenalty() {
		return penalty;
	}
	public void setPenalty(String penalty) {
		this.penalty = penalty;
	}
	public String getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}
	public String getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}
	public String getTerminalLocation() {
		return terminalLocation;
	}
	public void setTerminalLocation(String terminalLocation) {
		this.terminalLocation = terminalLocation;
	}
	
	
	
		
}
