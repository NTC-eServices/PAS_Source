package lk.informatics.ntc.model.service;

import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.SubSidyDTO;

public interface SubSidyManagementService {

	public List<SubSidyDTO> getServiceType();

	public List<SubSidyDTO> getApprovedVoucherNo(String servicecode);

	// added for subsidy payment approval
	public List<SubSidyDTO> getVoucherNoList(String selectedSubsidyService);

	public List<SubSidyDTO> SisuPaymentData(Date date1, Date date2, String voucherNo, String serviceType);

	public List<SubSidyDTO> getApprovedServiceRefNoList();

	public List<SubSidyDTO> getPaymentListForUpdating(SubSidyDTO dto);

	public String updateCertifyApproval(SubSidyDTO dto, String loginUser);

	public String updateRecomendedApproval(SubSidyDTO dto, String loginUser);

	public String updateDirectorApproval(SubSidyDTO dto, String loginUser);

	public String updateDGApproval(SubSidyDTO dto, String loginUser);

	public SubSidyDTO getApprovalStatus(String voucherNO);

	public List<SubSidyDTO> showLogSheetDet(String voucherNo, long seq);

	public List<SubSidyDTO> getVoucherNoList(SubSidyDTO subSidyDTO);

	public List<SubSidyDTO> getVoucherNoListNew(SubSidyDTO subSidyDTO, String serviceType);

	public List<SubSidyDTO> getDefaultPaymentListForUpdating();

	public List<SubSidyDTO> getSelectedPaymentListForUpdating(SubSidyDTO dto);

	public boolean updateReferenceNo(String oldValue, String newValue, String user);

	public boolean updatePaymentDetails(String user, List<SubSidyDTO> list, String voucherNO);

	public boolean isFoundReferenceNo(String oldValue);

	public List<SubSidyDTO> getLogSheetSeq(String voucherNo);

}
