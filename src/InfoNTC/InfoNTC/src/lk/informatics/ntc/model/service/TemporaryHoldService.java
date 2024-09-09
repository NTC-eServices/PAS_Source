package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.VehicleInspectionDTO;

public interface TemporaryHoldService extends Serializable {

	public List<VehicleInspectionDTO> getPermitNoList();

	public List<VehicleInspectionDTO> getApplicationNoList(String selectedPermitNo);

	public String getApplicationStatus(String selectedApplicationNo);

	public boolean temporaryHold(String selectedApplicationNo, String LoginUser);

	public boolean removeTemporaryHold(String selectedApplicationNo, String LoginUser);

}
