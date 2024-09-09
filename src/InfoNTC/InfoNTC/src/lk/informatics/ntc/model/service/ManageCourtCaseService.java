package lk.informatics.ntc.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CourtCaseDTO;

public interface ManageCourtCaseService {

	public String retrieveLastNoForNumberGeneration(String code);

	public void updateOffenceCodeInNumberGenTable(String code, String loginUser);

	public String generateCourtCaseCode();

	public void insertdata(CourtCaseDTO courtCaseDTO, String user, String orgin, String destination);

	public void updatedata(CourtCaseDTO courtCaseDTO, String user);

	public ArrayList<CourtCaseDTO> getcourtcaseList(String status);

	public void inserthistorydata(CourtCaseDTO courtCaseDTO, String user);

	public ArrayList<CourtCaseDTO> getcourtcaseListwithparam(String status, String vehicleno, String permitno,
			String courtcaseno, String groupNo, Date startDate, Date endDate);

	public ArrayList<CourtCaseDTO> courtcaseNoList();

	/** For Flying Squad Court Information Status Page **/
	public ArrayList<CourtCaseDTO> getCourtCaseVehiNum();

	public ArrayList<CourtCaseDTO> getCourtCasePermitNum();

	public ArrayList<CourtCaseDTO> getCourtCaseServiceNum();

	public ArrayList<CourtCaseDTO> getOrgin();

	public ArrayList<CourtCaseDTO> getDestination();

	public ArrayList<CourtCaseDTO> getCaseNum();

	public ArrayList<CourtCaseDTO> getSearchedData(CourtCaseDTO ccDTO);

	public CourtCaseDTO getAjaxData(CourtCaseDTO ccDTO);

	public CourtCaseDTO getManageCourtcaseViewData(String caseNo);

	public List<Object> getCourtNames();

	/** end **/

	/** For Manage Court Case page modifications **/

	public void updateCourtCaseCloseDate(CourtCaseDTO ccDTO, String remarks, String user);

	public void insertNextCallingDate(CourtCaseDTO ccDTO, String user);

	public ArrayList<CourtCaseDTO> getnextCallingDateValues(String courtCaseNo);

	public void updateNextCallingDate(CourtCaseDTO ccDTO, String user);

	public void recordUpdateAsDelete(CourtCaseDTO ccDTO, String user);

	/** end **/

	public void insertTotalHoursForGroup(String startDate, String endDate, String cumalativeDate);

	public void deleteTempTab();

	public void InsertTempData(String startDate, String endDate);

	public void deleteFlyingProgressTempTab();

	public void insertdataN(CourtCaseDTO courtCaseDTO, String user);

	public void updatedataN(CourtCaseDTO courtCaseDTO, String user);

	public ArrayList<CourtCaseDTO> getcourtcaseListN(String status);

	public ArrayList<CourtCaseDTO> getcourtcaseListwithparamN(String status, String vehicleno, String permitno,
			String courtcaseno, String groupNo, Date startDate, Date endDate);

	public List<String> getAllVehicleN();

	public List<String> getAllPermitN();

	public boolean checkCourtCaseNo(String number);
}
