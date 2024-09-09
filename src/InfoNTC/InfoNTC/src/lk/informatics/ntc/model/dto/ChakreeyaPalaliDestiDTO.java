package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class ChakreeyaPalaliDestiDTO implements Serializable {
	private static final long serialVersionUID = 8344243187243931903L;

	
	private String busNoDestination;
	private String startTimeDestination;
	private String endTImeDestination;
	
	public String getBusNoDestination() {
		return busNoDestination;
	}
	public void setBusNoDestination(String busNoDestination) {
		this.busNoDestination = busNoDestination;
	}
	public String getStartTimeDestination() {
		return startTimeDestination;
	}
	public void setStartTimeDestination(String startTimeDestination) {
		this.startTimeDestination = startTimeDestination;
	}
	public String getEndTImeDestination() {
		return endTImeDestination;
	}
	public void setEndTImeDestination(String endTImeDestination) {
		this.endTImeDestination = endTImeDestination;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
