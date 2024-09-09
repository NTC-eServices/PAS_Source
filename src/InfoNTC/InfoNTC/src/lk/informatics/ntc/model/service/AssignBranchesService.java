package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.AssignBranchesDTO;

public interface AssignBranchesService {

	public List<AssignBranchesDTO> getBankCode();

	public String getBranchName(String bankCode);

	public List<AssignBranchesDTO> getAssignedBranchesList(String bankCode);

	public boolean isCodeDuplicate(String bankCode, String branchCode);

	public void saveRecord(AssignBranchesDTO assignBranchesDTO, String username);

	public void editRecord(AssignBranchesDTO assignBranchesDTO, String username);

	public int deleteRecord(AssignBranchesDTO assignBranchesDTO, String bankCodeStr);
}
