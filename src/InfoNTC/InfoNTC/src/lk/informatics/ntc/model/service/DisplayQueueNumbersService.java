package lk.informatics.ntc.model.service;

import java.util.List;

import lk.informatics.ntc.model.dto.DisplayQueueNumbersDTO;

public interface DisplayQueueNumbersService {

	public List<DisplayQueueNumbersDTO> counterDetails();

	public List<DisplayQueueNumbersDTO> inspectionNextNumbers();

	public List<DisplayQueueNumbersDTO> calledNumbers();

	public List<String> callNextQueueNumbersForDisplayAction(String transactionType, String previousTransType);

	public List<DisplayQueueNumbersDTO> permenantlySkipedNumbers(String skipCount);

}
