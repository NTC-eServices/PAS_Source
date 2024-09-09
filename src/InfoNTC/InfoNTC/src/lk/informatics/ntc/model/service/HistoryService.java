package lk.informatics.ntc.model.service;

import lk.informatics.ntc.model.dto.AccidentDTO;
import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.dto.AmendmentServiceDTO;
import lk.informatics.ntc.model.dto.IssuePermitDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;

public interface HistoryService {

	/* History Method Start Here */

	public PermitRenewalsDTO getVehicleOwnerTableData(String applicationNo, String user);

	public void insertVehicleOwnerHistoryData(PermitRenewalsDTO dto);

	public OminiBusDTO getOminiBusTableData(String applicationNo, String user);

	public void insertOminiBusHistoryData(OminiBusDTO dto);

	public PermitRenewalsDTO getApplicationTableData(String applicationNo, String user);

	public boolean insertApplicationHistoryData(PermitRenewalsDTO dto);

	public AmendmentDTO getAmendmentTableData(String applicationNo, String user);

	public void insertAmendmentsHistoryData(AmendmentDTO dto);

	public AmendmentServiceDTO getRouteRequsetedTableData(String applicationNo, String user);

	public void insertRouteRequsetedHistoryData(AmendmentServiceDTO dto);

	public AccidentDTO getAccidentMasterTableData(String vehicleNo, String user);

	public void insertAccidentMasterHistoryData(AccidentDTO dto);

	public AmendmentDTO getNewOminiBusTableData(int value, String user);

	public void insertNewOminiBusHistoryData(AmendmentDTO dto);

	public AccidentDTO getAccidentDetailsTableData(int value, String user);

	public void insertAccidentDetailsHistoryData(AccidentDTO dto);

	public AccidentDTO getLegalCasesTableData(int value, String user);

	public void insertLegalCasesHistoryData(AccidentDTO dto);

	public IssuePermitDTO getServiceTableData(String applicationNO, String user);

	public void insertServiceHistoryData(IssuePermitDTO dto);

	public void insertApplicationAndOminiBusHistory(PermitRenewalsDTO applicationHistoryDTO,
			OminiBusDTO ominiBusHistoryDTO);

	/* History Method end Here */

}
