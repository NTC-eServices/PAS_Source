package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.ManageInvestigationDTO;
import lk.informatics.ntc.model.dto.ManageRetainDocsDTO;

public interface ManageRetainDocumentsService {

	public List<ManageInvestigationDTO> getChargeRefNoList();

	public String getVehicleNoByChargeRefNo(String chargeRefNo);

	public String getPermitNoByChargeRefNo(String chargeRefNo);

	public String getPermitOwnerByChargeRefNo(String chargeRefNo);

	public void addReleaseDetails(ManageInvestigationDTO manageInvestigationDTO, ManageRetainDocsDTO manageRetainDocsDTO);

	public List<ManageRetainDocsDTO> getDocDetailsByChargeRefNo(ManageInvestigationDTO ChargeRefNo);

	public boolean checkReleased(ManageRetainDocsDTO manageRetainDocsDTO);

	public ManageRetainDocsDTO getReleaseDetailsByDocDetails(ManageRetainDocsDTO manageRetainDocsDTO);

	public void beanLinkMethod(ManageInvestigationDTO manageInvestigationDTO, String des, String funDes);
}
