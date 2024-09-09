package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class UploadImageDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String vehicleOwnerPhotoPath;
	private String firstVehiImagePath;
	private String secondVehiImagePath;
	private String thirdVehiImagePath;
	private String forthVehiImagePath;
	private String fifthVehiImagePath;
	private String sixthVehiImagePath;
	
	private String applicationNo;
	private String vehicleNo;
	private String vehicleOwnerName;
	private String inspectionType;
	private String queueNo;
	public String getFirstVehiImagePath() {
		return firstVehiImagePath;
	}

	public void setFirstVehiImagePath(String firstVehiImagePath) {
		this.firstVehiImagePath = firstVehiImagePath;
	}

	public String getSecondVehiImagePath() {
		return secondVehiImagePath;
	}

	public void setSecondVehiImagePath(String secondVehiImagePath) {
		this.secondVehiImagePath = secondVehiImagePath;
	}

	public String getThirdVehiImagePath() {
		return thirdVehiImagePath;
	}

	public void setThirdVehiImagePath(String thirdVehiImagePath) {
		this.thirdVehiImagePath = thirdVehiImagePath;
	}

	public String getForthVehiImagePath() {
		return forthVehiImagePath;
	}

	public void setForthVehiImagePath(String forthVehiImagePath) {
		this.forthVehiImagePath = forthVehiImagePath;
	}

	public String getFifthVehiImagePath() {
		return fifthVehiImagePath;
	}

	public void setFifthVehiImagePath(String fifthVehiImagePath) {
		this.fifthVehiImagePath = fifthVehiImagePath;
	}

	public String getSixthVehiImagePath() {
		return sixthVehiImagePath;
	}

	public void setSixthVehiImagePath(String sixthVehiImagePath) {
		this.sixthVehiImagePath = sixthVehiImagePath;
	}

	public String getVehicleOwnerPhotoPath() {
		return vehicleOwnerPhotoPath;
	}

	public void setVehicleOwnerPhotoPath(String vehicleOwnerPhotoPath) {
		this.vehicleOwnerPhotoPath = vehicleOwnerPhotoPath;
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

	public String getVehicleOwnerName() {
		return vehicleOwnerName;
	}

	public void setVehicleOwnerName(String vehicleOwnerName) {
		this.vehicleOwnerName = vehicleOwnerName;
	}

	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}

	public String getQueueNo() {
		return queueNo;
	}

	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}


}
