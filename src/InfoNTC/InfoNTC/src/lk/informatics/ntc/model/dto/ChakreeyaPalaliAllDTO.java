package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class ChakreeyaPalaliAllDTO implements Serializable {
	private static final long serialVersionUID = 8344243187243931903L;

	
	private String busNoDestination;
	private String startTimeDestination;
	private String endTImeDestination;
	private String busNoOrigin;
	private String startTimeOrigin;
	private String endTImeOrigin;
	
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
	
	
	public String getBusNoOrigin() {
		return busNoOrigin;
	}
	public void setBusNoOrigin(String busNoOrigin) {
		this.busNoOrigin = busNoOrigin;
	}
	public String getStartTimeOrigin() {
		return startTimeOrigin;
	}
	public void setStartTimeOrigin(String startTimeOrigin) {
		this.startTimeOrigin = startTimeOrigin;
	}
	public String getEndTImeOrigin() {
		return endTImeOrigin;
	}
	public void setEndTImeOrigin(String endTImeOrigin) {
		this.endTImeOrigin = endTImeOrigin;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
