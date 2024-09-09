package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lk.informatics.ntc.model.dto.PrintReminderLetterDTO;

public interface PrintLetterService extends Serializable {
	public List<PrintReminderLetterDTO> getLetterTypeDropDown();

	public List<PrintReminderLetterDTO> getDataForSearch(String letterType, Timestamp startDt, Timestamp endDt);

	public void updatePrinteStatus(String firstStatus, String secondStatus, String thirdStatus, String loginUser,
			String appNumber);
}
