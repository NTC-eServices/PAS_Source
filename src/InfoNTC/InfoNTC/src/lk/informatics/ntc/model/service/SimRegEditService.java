package lk.informatics.ntc.model.service;

import java.util.List;
import lk.informatics.ntc.model.dto.SimRegistrationDTO;

/**
 * @author viraj.k
 *
 */
public interface SimRegEditService {

	public List<SimRegistrationDTO> getSIMDetails();

	public List<Object> getSearchedDTO(SimRegistrationDTO keyDTO);

	public List<SimRegistrationDTO> getEmiListBySimNo(String simRegNo);

	public SimRegistrationDTO getVoucherDetailsBySimNo(String simRegNo);

	public List<SimRegistrationDTO> getChargeDetailsByVoucherSeq(String vouSeq);

	public String getChargeTypeDescByCode(String code);

	public void deleteVoucher(String vouSeq);

	public List<SimRegistrationDTO> getApprovedSIMDetails();

	public String saveSimRenewal(SimRegistrationDTO simRegistrationDTO, List<SimRegistrationDTO> emiDtoList);

	public void updateSimRenewal(SimRegistrationDTO simRegistrationDTO);

	public String updateSimRegStatus(String simRegNo, String status, String user);

	public List<SimRegistrationDTO> getSIMDetailsBySimStatus(String status);

	public boolean cancelVoucher(String voucherNo, String voucherStatus, String reason, String loginUser,
			String simRegNo, String simRegStatus);

}
