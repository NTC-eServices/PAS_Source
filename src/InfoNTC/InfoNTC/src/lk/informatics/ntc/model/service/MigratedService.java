package lk.informatics.ntc.model.service;

import lk.informatics.ntc.model.dto.ParamerDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;

public interface MigratedService {

	void updateStatusOfQueueNumberAfterCallNext(String queueNumber, String taskStatus);

	void updateQueueNumberTaskInQueueMaster(String queueNo, String applicatioNo, String taskCode, String taskStatus);

	void updateCounterIdOfQueueNumberAfterCallNext(String queueNumber, String counterId);

	void updateSkipQueueNumberStatus(String queueNumber);

	public ParamerDTO retrieveParameterValuesForParamName(String paramName);

	public void updateNT_R_NT_R_PARAMETERS(ParamerDTO paramDto, String paramName);

	public void updateStatusOfQueApp(String queueNo, String appNo);

	public int updateTransactionTypeCodeForQueueNo(String queueNumber, String currentQueueType);

	public boolean findCancelledQueueNumber(String queueNo);

	public String findQueueNumberFromApplicationNo(String appNumber);

	public boolean findCancelledQueueNumberForRenewals(String queueNo, String string, String string2);

	void updataApplicationDetailQueueCallCounterId(String counterId, String vehicleNo, String appNo, String tskCode,
			String tskStatus);

	void moveDocumentsA(String applicationNo, String fileName, String newPath, int i);

	void moveDocumentsB(String applicationNo, String fileName, String mainPath);

	void moveDocumentsC(String fileName);

	void editTimeTableManagementBeanA(String referanceNum, String groupNum, String tripType, String duplicateBusNum,
			String startTime, String endTime, String busNum, int tripIdInt, String withFixedTimeCode, String user);

	void editTimeTableManagementBeanB(String referanceNum, String groupNum, TimeTableDTO chagedDTO, String user);

	void updateRouteScheduleGeneratorTable(String referanceNum, String groupNum,
			String duplicateBusNum, String busNum, String loginUser);
}
