package lk.informatics.ntc.model.service;

import java.util.ArrayList;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiDetailDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;

public interface FlyingSquadInvestigationService {

	public ArrayList<FlyingSquadGroupsDTO> getGroupcode(int year, int month);

	public ArrayList<FlyingSquadInvestiDetailDTO> groupDetail(String groupCd);

	public String getSeqNo(int year);

	public void initializeSequenceNo(int year);

	public void UpdateNextSequenceNo(int year, int seqNo);

	public void saveMasterDta(FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO, String user);

	public boolean saveDetailDta(ArrayList<FlyingSquadInvestiDetailDTO> flyingSquadInvestiDetailList, String user,
			String refNo);

	public void updatemasterData(FlyingSquadInvestiMasterDTO flyingSquadInvestiMasterDTO, String user);

	public void updatedetail(ArrayList<FlyingSquadInvestiDetailDTO> detail, String user, String refNo);

	public ArrayList<FlyingSquadInvestiDetailDTO> getmemberDetails(String refNo);

	public FlyingSquadInvestiMasterDTO getmasterDetails(String refNo);

	public ArrayList<FlyingSquadInvestiMasterDTO> getrefNo();

	public ArrayList<FlyingSquadInvestiMasterDTO> getrefNoforInvestigation();

	public String validateRefNo(String refNo);

}
