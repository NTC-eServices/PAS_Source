package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.PaymentTypeMaintenanceDTO;

public interface PaymentTypeMaintenanceService extends Serializable {

	List<PaymentTypeMaintenanceDTO> getAllTranstractionTypeList();

	PaymentTypeMaintenanceDTO getTranstractionDescription(String selectedTranstractionType);

	List<PaymentTypeMaintenanceDTO> getAllPaymentTypeList();

	PaymentTypeMaintenanceDTO getPaymentTypeDescription(String selectedChargeType);

	List<PaymentTypeMaintenanceDTO> getAllAccountNoList(String selectedTranstractionType, String selectedChargeType);

	PaymentTypeMaintenanceDTO getCurrentAmount(String transtractionTypeCode, String paymentTypeCode, String accountNo);

	PaymentTypeMaintenanceDTO getStatusDescription(String selectedStatus);

	List<PaymentTypeMaintenanceDTO> getAllStatusList();

	List<PaymentTypeMaintenanceDTO> getDisplaySearchDetailsWithTranstractionType(String selectedTranstractionType);

	List<PaymentTypeMaintenanceDTO> getDisplayRecordsTransAndCharge(String selectedTranstractionType,
			String selectedChargeType);

	List<PaymentTypeMaintenanceDTO> getSearchRecordsWithStatus(String selectedStatus);

	List<PaymentTypeMaintenanceDTO> getSearchRecordsWithAll(String selectedTranstractionType, String selectedChargeType,
			String selectedAccountNo, Double displayAmount, String selectedStatus);

	List<PaymentTypeMaintenanceDTO> getSearchRecordsWithTransAndStatus(String selectedTranstractionType,
			String selectedStatus);

	int updateTable(PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO);

	int deleteRecord(String tectCode);

	List<PaymentTypeMaintenanceDTO> getAllTctCodeList();

	int insertNewRecordWithTechCode(PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO);

	boolean checkHaveFunction(String paymentTypeCode, String accountNo);

	List<PaymentTypeMaintenanceDTO> getAllChargeTypeCodeList();

	int insertChargeType(PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO);

	boolean checkHaveFunctionWithChargeTypeCode(String popUpChargeTypeCode);

	boolean checkHaveFunctionWithChargeTypeCodeOne(String popUpChargeTypeCode);

	int deleteChargeTypeRecord(String popUpChargeTypeCode);

	List<PaymentTypeMaintenanceDTO> getAllChargeTypesList();

	int updateChargeType(PaymentTypeMaintenanceDTO paymentTypeMaintenanceDTO);

	List<PaymentTypeMaintenanceDTO> getAllChargeTypeCodesForCurrentTranstractionType(String selectedTranstractionType);

}
