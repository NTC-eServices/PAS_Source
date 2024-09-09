package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.OffenceManagementDTO;

public interface OffenceManagementService {
	public List<OffenceManagementDTO> getExistingOffenceCode();

	public OffenceManagementDTO getDescriptionForCodes(String code);

	public List<OffenceManagementDTO> getOffenceCodeDataForGrid();
	// generate offense code

	public String generateOffenceCode();

	public void insertOffenceCodeDesriptionDet(OffenceManagementDTO offenceMgtDto, String loginUser);

	public void updateOffenceCodeInNumberGenTable(String code, String loginUser);

	public String retrieveLastNoForNumberGeneration(String code);

	public boolean deleteSelectedOffenceCode(String offenceCode);

	public void updateEditDetails(OffenceManagementDTO offenceDto, String loginUser);

	public void insertOffenceDetailInDetTable(OffenceManagementDTO offenceDto, String loginUser, String code);

	public List<OffenceManagementDTO> getOffenceDetailOnSecondGrid(String code);

	public void updateOffenceDetailInDetTable(OffenceManagementDTO offenceMangDTO, String loginUser,
			String selectedNoOFAttempt, String offenceCode);

	public List<String> getNoOfAttemptForCheckDuplicates(String code);

	public List<OffenceManagementDTO> getAttemptsListDropDown();

	public List<OffenceManagementDTO> getOffenceCodeDataForGridForDropDown(String code);

	public String checkDuplicate(String offenceCode);
	
	public List<String> getActiveOffences();
	
	public void createTempTable(String offence, String startDate ,String endDate);
	public void deleteInvestigationData();
	
	public void beanLinkMethod(String code,String user, String status);

}
