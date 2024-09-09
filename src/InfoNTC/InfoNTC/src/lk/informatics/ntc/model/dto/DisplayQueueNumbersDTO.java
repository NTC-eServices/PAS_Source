package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class DisplayQueueNumbersDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String counterId;
	private String counterCurrentNo;
	private String counterName;
	private String[] nextNumbers = null;
	private String queueNo1;
	private String queueNo2;
	private String queueNo3;
	private String queueNo4;
	private String queueNo5;
	private String CalledNumbers;
	
	public String getCounterId() {
		return counterId;
	}
	public String getCounterCurrentNo() {
		return counterCurrentNo;
	}
	public String getCounterName() {
		return counterName;
	}
	public String getQueueNo1() {
		return queueNo1;
	}
	public String getQueueNo2() {
		return queueNo2;
	}
	public String getQueueNo3() {
		return queueNo3;
	}
	public String getQueueNo4() {
		return queueNo4;
	}
	public String getQueueNo5() {
		return queueNo5;
	}
	public void setCounterId(String counterId) {
		this.counterId = counterId;
	}
	public void setCounterCurrentNo(String counterCurrentNo) {
		this.counterCurrentNo = counterCurrentNo;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
	public void setQueueNo1(String queueNo1) {
		this.queueNo1 = queueNo1;
	}
	public void setQueueNo2(String queueNo2) {
		this.queueNo2 = queueNo2;
	}
	public void setQueueNo3(String queueNo3) {
		this.queueNo3 = queueNo3;
	}
	public void setQueueNo4(String queueNo4) {
		this.queueNo4 = queueNo4;
	}
	public void setQueueNo5(String queueNo5) {
		this.queueNo5 = queueNo5;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String[] getNextNumbers() {
		return nextNumbers;
	}
	public void setNextNumbers(String[] nextNumbers) {
		this.nextNumbers = nextNumbers;
	}
	public String getCalledNumbers() {
		return CalledNumbers;
	}
	public void setCalledNumbers(String calledNumbers) {
		CalledNumbers = calledNumbers;
	}

	
	
	

}
