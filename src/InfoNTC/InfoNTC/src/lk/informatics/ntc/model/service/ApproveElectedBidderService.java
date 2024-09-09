package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.dto.TenderApplicantDTO;

public interface ApproveElectedBidderService {
	public List<String> getReferenceNoList();

	public TenderDTO getDetailsByReferenceNo(TenderDTO tenderDTO);

	public List<TenderApplicantDTO> getRouteNoList(String tenderRefNo);

	public TenderApplicantDTO getDetailsByRouteNo(TenderApplicantDTO tenderApplicantDTO);

	public boolean addApplications(List<TenderApplicantDTO> temptSelectedApplicationList);

	public List<TenderApplicantDTO> getSelectingApplications(String serialNo, String tenderRefNo);

	public List<TenderApplicantDTO> getSelectedApplications(String tenderRefNo);

	public boolean removeSelectedApplication(String applicationNo);

	public boolean approveElectedBidder(String tenderRefNo, String logingUser);

	public String getTrafficProposalNoFromTenderRefNo(String tenderRefNo);

	public boolean isApproved(String tenderRefNo);
}
