package lk.informatics.ntc.model.service;

import java.util.ArrayList;
import java.util.Date;

import lk.informatics.ntc.model.dto.FluingSquadVioConditionDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadVioDocumentsDTO;

public interface FlyingSquadChargeSheetService {

	public void save(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO, String user);

	public void savecondition(ArrayList<FluingSquadVioConditionDTO> conditionlist, String user, String refno,
			String reportNo, String vehicleno, String invesno, Boolean isInsert);

	public void savemasterdata(ArrayList<FlyingSquadVioDocumentsDTO> doclist, String user, String refno,
			String reportNo, String vehicleno, String invesno, Boolean isInsert);

	public ArrayList<FlyingSquadVioDocumentsDTO> getdocumentListn(String invesno);

	public ArrayList<FlyingSquadVioDocumentsDTO> getdocumentlist(String invesno);

	public ArrayList<FluingSquadVioConditionDTO> getConditionListn(String invesNo);

	public ArrayList<FluingSquadVioConditionDTO> getConditionList(String invesno);

	public String getname(String id);

	public ArrayList<FlyingManageInvestigationLogDTO> getConductorList();

	public ArrayList<FlyingManageInvestigationLogDTO> getdriverList();

	public FlyingManageInvestigationLogDTO getVehicleDetails(String vehicleno);

	public FlyingManageInvestigationLogDTO getmasterDetails(String invesNo);

	public ArrayList<FlyingManageInvestigationLogDTO> getinvesnolist(String refNo, String reportNo);

	public String getrefNoNew(String reportNo);

	public FlyingManageInvestigationLogDTO getShowDetails(String permitNo);

	public FlyingManageInvestigationLogDTO getShowDetailsN(String investigationNo);

	public String checkDataAlreadyHv(String invNo);

	public ArrayList<FluingSquadVioConditionDTO> getConditionListN(String invesno);

	public ArrayList<FlyingSquadVioDocumentsDTO> getdocumentlistN(String invesno);

	public String getNIC(String id);

	public String getInspectionLocation(String reportNo);
	 String getInspectionLocationNew(String reportNo);
	 Date getInspectionDate(String reportNo);
	
}
