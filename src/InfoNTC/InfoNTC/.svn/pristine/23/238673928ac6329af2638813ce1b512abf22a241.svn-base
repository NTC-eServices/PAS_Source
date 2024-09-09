package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.AssignDivSecDTO;

public interface AssignDivSecService {
	public List<AssignDivSecDTO> getDistrictCode();

	public String getDivSecName(String districtCode);

	public List<AssignDivSecDTO> getAssignedDivSecList(String districtCode);

	public boolean isCodeDuplicate(String districtCode, String divSecCode);

	public void saveRecord(AssignDivSecDTO assignDivSecDTO, String username);

	public void editRecord(AssignDivSecDTO assignDivSecDTO, String username);

	public int deleteRecord(AssignDivSecDTO assignDivSecDTO, String districtCodeStr);
}
