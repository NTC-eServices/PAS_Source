package lk.informatics.ntc.model.service;

import java.util.ArrayList;
import java.util.Date;

import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadDocumentDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.dto.FlyingSquadVioDocumentsDTO;

public interface FlyingSquadRelaseDocumentService {

	public FlyingManageInvestigationLogDTO getmasterDetails(String invesNo);

	public void updateData(ArrayList<FlyingSquadVioDocumentsDTO> docList, String user, String invesNo);

	public ArrayList<FlyingSquadVioDocumentsDTO> getdocumentlist(String invesno);

	public ArrayList<FlyingSquadDocumentDTO> getmasterData(Date startDate, Date endDate, String refNo, String reportNo,
			String invesNo);

	public ArrayList<FlyingManageInvestigationLogDTO> getinvesnolist(String refNo, String reportNo, Date startDate,
			Date endDate);

	public ArrayList<FlyingManageInvestigationLogDTO> getreportNo(String refNo, Date startDate, Date endDate);

	public ArrayList<FlyingSquadInvestiMasterDTO> getrefNo(String repNo, String invesNo, Date startDate, Date endDate);

	public FlyingManageInvestigationLogDTO getrefNoNew(String reportNo, String invesNo);

}
