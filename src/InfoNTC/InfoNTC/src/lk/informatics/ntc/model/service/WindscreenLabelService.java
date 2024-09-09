package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.WindscreenLabelDTO;

public interface WindscreenLabelService extends Serializable {

	public List<WindscreenLabelDTO> getTrasactionType();

	public List<WindscreenLabelDTO> getApplicationNo();

	public List<WindscreenLabelDTO> getPermitNo();

	public WindscreenLabelDTO fillDetailsFromAppNo(String applicationNo);

	public List<WindscreenLabelDTO> getBusNumbers();

	public WindscreenLabelDTO showSearchedDetails(String busNo, String permitNo);

	public WindscreenLabelDTO getPermitNo(String busNo);

	public WindscreenLabelDTO getBusStatus(String Busno, String permitNo);

	public void updateWindscreenLabel(WindscreenLabelDTO windscreenDTO, String loginUser);
}
