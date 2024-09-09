package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.SetupCommitteeBoardDTO;

public interface SetupCommitteeBoardService {
	public List<SetupCommitteeBoardDTO> getTransactionTypeList();

	public List<SetupCommitteeBoardDTO> getOrganizationListList();

	public List<SetupCommitteeBoardDTO> getUserIDList();

	public List<SetupCommitteeBoardDTO> getDesignationList();

	public List<SetupCommitteeBoardDTO> getReferenceNoListforAuth();

	public List<SetupCommitteeBoardDTO> getReferenceNoListforEdit();

	public List<SetupCommitteeBoardDTO> getReferenceNoListforView();

	public void saveMasterData(SetupCommitteeBoardDTO setupCommitteeBoardDTO, String referenceNo, String loginUser);

	public void saveMemberDetails(List<SetupCommitteeBoardDTO> membersList, String referenceNo, String loginUser);

	public String generateReferenceNo();

	public int updateMasterData(SetupCommitteeBoardDTO setupCommitteeBoardDTO, String referenceNo, String loginUser);

	public void authorizedData(String referenceNo, String loginUser);

	public SetupCommitteeBoardDTO getMasterDataFromReferenceNo(String referenceNo);

	public List<SetupCommitteeBoardDTO> getMemberDataFromReferenceNo(String referenceNo);

	public int getRefNo(String type, String transCode);

	public int getActiveStatus(String type, String transCode);

	public String getAuthorizedStatus(String refNo);

}
