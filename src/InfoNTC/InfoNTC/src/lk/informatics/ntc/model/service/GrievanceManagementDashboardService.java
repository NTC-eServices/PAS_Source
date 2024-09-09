package lk.informatics.ntc.model.service;

import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CommonInquiryDTO;
import lk.informatics.ntc.model.dto.ComplaintActionDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DepartmentDTO;

public interface GrievanceManagementDashboardService {

	List<DepartmentDTO> actionDepartmentList();

	List<ComplaintActionDTO> getComplaintActionData(String actionDepartmentCode, Date startDate, Date endDate);

	void updateComplainAction(ComplaintActionDTO complaintAction, String loginUser);

	void complainActionHistory(ComplaintActionDTO complaintAction, String loginUser);

	ComplaintActionDTO getComplaintActionDataByComplainNo(String complainNo);

	void actionTaken(String loginUser, String complainNo, String actionDepartment, String action);

	void beanLinkMethod(ComplaintActionDTO selectedComplaintDTO, String user, String des, String funDes);

	List<CommonInquiryDTO> getPermitNoListForCommonInquiry();

	List<CommonInquiryDTO> getBusNoListForCommonInquiry();

	List<CommonInquiryDTO> getComNoListForCommonInquiry();

	List<CommonInquiryDTO> searchDataForCommonInquiry(String complaintNo, String busNo, String permitNo);

}
