package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.PrintInspectionDTO;
import lk.informatics.ntc.model.dto.ProceedIncompleteApplicationDTO;

public interface IncompleteApprovalService extends Serializable {

	public List<PrintInspectionDTO> getAllApplicationNoListForIncompleteApproval();

	public List<PrintInspectionDTO> getAllVehicleNoListForIncompleteApproval();

	public PrintInspectionDTO getCurrentVehicleNo(String selectedApplicationNo);

	public List<PrintInspectionDTO> searchApplications(String currentApplicationNo, String currentVehicleNo,
			String selectedInspectionStatus);

	public boolean proceedIncompleteApplication(ProceedIncompleteApplicationDTO dto);

	public boolean enableProceedBtn(String selectedApplicationNo);

}
