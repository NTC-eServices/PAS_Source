package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.ManageInvestigationDTO;

public interface ApproveActionChargesService {

	List<ManageInvestigationDTO> getChargeRefNoList();

	List<ManageInvestigationDTO> searchInvestigationDetailsByDate(Date investigationFrom, Date investigationTo);

	List<ManageInvestigationDTO> searchInvestigationDetails(ManageInvestigationDTO searchDTO);

	List<ManageInvestigationDTO> getChargeSheetByChargeRef(String chargeRef);

	BigDecimal getDemeritPoints(String offenceCode, String attemptCode);

	List<ManageInvestigationDTO> searchSavedSuspensionsDetails();

	boolean approveCharges(ManageInvestigationDTO actionChargesDTO, String chargeRef, String user);

	boolean rejectCharges(ManageInvestigationDTO actionChargesDTO,String chargeRef, String user);

	String getServiceTypeDesc(String code);
	
	BigDecimal getLatePaymentFee(String chargeRefNo);
	BigDecimal getTotalAmount(String chargeRefNo);

	void beanLinkMethod(ManageInvestigationDTO actionChargesDTO, String des);

}
