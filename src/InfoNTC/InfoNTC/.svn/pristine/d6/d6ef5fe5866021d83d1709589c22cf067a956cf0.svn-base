package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.AdvertisementDTO;

public interface TenderAdvertisementService {

	public List<AdvertisementDTO> refNodropdown();

	public String description(String refNo);

	public AdvertisementDTO headerfooter(String refno);

	public AdvertisementDTO headerfootersinhala(String refno);

	public AdvertisementDTO headerfootertamil(String refno);

	public List<AdvertisementDTO> dataTable(String refno);

	public boolean insertHeaderFooter(AdvertisementDTO dto, int language, String user);

	public boolean insertDataTable(AdvertisementDTO dto, String tenderNo);

	public String approvalStatus(String refNo);

	public void updateStatus(AdvertisementDTO dto, String status, String rejectReason);

	public List publishTable();

	public void updateTenderStatus(AdvertisementDTO dto);

	public List<AdvertisementDTO> approvalrefNodropdown();

	/* Generate Tender Advertisement */

	public boolean checkTenderStatus(AdvertisementDTO dto, String user);
}
