package lk.informatics.ntc.model.service;

import java.util.ArrayList;
import java.util.Date;

import lk.informatics.ntc.model.dto.FlyingSquadApprovalInvestigationDTO;

public interface FlyingSquadApprovalInvestigationService {

	public ArrayList<FlyingSquadApprovalInvestigationDTO> getDetails(Date startDate, Date EndDate, String refNo);

	public void updatereasons(boolean reject, boolean recomended, boolean finalized, boolean cancel, String remark,
			String user, String refNo);

	public ArrayList<FlyingSquadApprovalInvestigationDTO> getReferenceNo(Date startDate, Date endDate);
}
