package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.StationDetailsDTO;

public interface StationDetailsService {

	public void saveAction(List<StationDetailsDTO> stationDetailsDTOList, String loginUSer);

	public List<StationDetailsDTO> searchStationData(StationDetailsDTO stationDetailsDTO);

	public void updateStationDetailsRecord(StationDetailsDTO stationDTO);

	public boolean checkDuplicateData(StationDetailsDTO stationDTO, boolean stationCode);

	public List<StationDetailsDTO> selectAllStations();

	public boolean checkDuplicateCode(String code);

	public boolean checkAssignedStationCode(String code);
}
