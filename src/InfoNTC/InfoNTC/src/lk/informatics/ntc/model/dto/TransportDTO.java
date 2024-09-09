package lk.informatics.ntc.model.dto;

public class TransportDTO extends ProgramDTO {
	
	private static final long serialVersionUID = 526526959823L;
	
	private Long etd_seq;
	private String transportDate;
	private String vehicleNo;
	private String driverName;
	private String km;
	
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getKm() {
		return km;
	}
	public void setKm(String km) {
		this.km = km;
	}
	public Long getEtd_seq() {
		return etd_seq;
	}
	public void setEtd_seq(Long etd_seq) {
		this.etd_seq = etd_seq;
	}
	public String getTransportDate() {
		return transportDate;
	}
	public void setTransportDate(String transportDate) {
		this.transportDate = transportDate;
	}	
	
}
