package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.util.List;

import lk.informatics.ntc.model.dto.AttorneyHolderDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.EmployeeAddressDTO;
import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.EmployeeDeptDTO;
import lk.informatics.ntc.model.dto.EmployeeDesignationDTO;
import lk.informatics.ntc.model.dto.ManageRoleDTO;
import lk.informatics.ntc.model.dto.SearchEmployeeDTO;
import lk.informatics.ntc.model.dto.UserRoleDTO;

public interface EmployeeProfileService extends Serializable {
	public List<CommonDTO> GetTitleToDropdown();

	public List<CommonDTO> GetLocationToDropdown();

	public List<CommonDTO> GetCarderToDropdown();

	public List<CommonDTO> GetQualificationToDropdown();

	public List<CommonDTO> GetMartialToDropdown();

	public List<CommonDTO> GetGenderToDropdown();

	public List<CommonDTO> GetAddressTypeToDropdown();

	public List<CommonDTO> GetCityToDropdown();

	public List<CommonDTO> GetDepartmentsToDropdown();

	public List<CommonDTO> GetUnitsToDropdown();

	public List<CommonDTO> GetDesignationToDropdown();

	public List<CommonDTO> GetSalaryCatToDropdown();

	public List<CommonDTO> GetEmpGradeToDropdown();

	public List<CommonDTO> GetRolesToDropdown();

	public int saveEmployee(EmployeeDTO employeeDTO);

	public List<SearchEmployeeDTO> getsearchEmployeeDetails(String empNo);

	public List<SearchEmployeeDTO> searchEmployee(SearchEmployeeDTO searchEmployeeDTO);

	public List<AttorneyHolderDTO> attorney(AttorneyHolderDTO attorneyDTO);

	public List<AttorneyHolderDTO> attorneyCheck(AttorneyHolderDTO attorneyDTO);

	public List<String> getAllEmpNumbers();

	public EmployeeDTO getEmployeeDetails(String empNo);

	public List<EmployeeAddressDTO> findAddressDetByEmpNo(String empNo);

	public int saveEmpAddress(EmployeeAddressDTO employeeAddressDTO);

	public int updateAddressDetails(EmployeeAddressDTO empAddressDTO);

	public int statusUpdate(long addressSeq);

	public List<String> getAllEpfNubers();

	public List<CommonDTO> getAllRoleNamesList(String depcode);

	public List<EmployeeDeptDTO> findDeparmentDetByEmpNo(String empNo);

	public int saveDepDetais(EmployeeDeptDTO employeeDeptDTO);

	public int updateDepDetails(EmployeeDeptDTO employeeDeptDTO);

	public int statusUpdateDept(long deptSeq);

	public EmployeeDeptDTO isDeptDetAvilable(String empNo, String deptCode, String UnitCode, String Status);

	public List<EmployeeDesignationDTO> findDesignationDetByEmpNo(String empNo);

	public int saveDesigDetais(EmployeeDesignationDTO employeeDesignationDTO);

	public int updateDesigDetails(EmployeeDesignationDTO employeeDesignationDTO);

	public int statusUpdateDesig(long deptSeq, String deptendDate);

	public EmployeeDeptDTO isDesigDetAvilable(String empNo, String desigCode, String grade, String salCat);

	public EmployeeDeptDTO isEmpNoAvilable(String empNo);

	public int updateEmployee(EmployeeDTO employeeDTO);

	public String getStatus(AttorneyHolderDTO attorneyDTO);

	public String getApplication_No_status(AttorneyHolderDTO attorneyDTO);

	public String getVehicle_No_status(AttorneyHolderDTO attorneyDTO);

	public List<String> getAllEpfLocation();

	public List<String> getAllEpfUserID();

	public List<String> getEmpNo();

	public List<String> getUserId();

	public boolean approveUser(List<EmployeeDTO> approveList, String approveBy, String reqAccount);

	public boolean rejectUser(List<EmployeeDTO> approveList, String approveBy, String val);

	public String checkbutton();

	public List<String> getAllVehiclePermit();

	public List<String> getAllVehicleNo();

	public List<String> getAllApplicationNo();

	public List<EmployeeDTO> getEmpDetails(String epfNo, String nicNo, String userId, String status, String location,
			String empNo);

	public List<ManageRoleDTO> searchDetails(String depNo, String roleCode);

	public List<EmployeeDTO> GetDeptandRoles();

	public List<EmployeeDTO> SearchDeptandRoles(String deptCode, String roleCode);

	public int updateRoleManagement(String rolecode, String depcode, String roleName, String status,
			String beforerolecode, String modifyBy);

	public boolean checkHaveFunction(String roleCode, String depCode);

	public int insertNewRole(String newRoleCode, String depcode, String newRoleName, String newStatus,
			String createdBy);

	public ManageRoleDTO getCurrentDepartmentDes(String depcode);

	public int deleteCurrentRoleDetails(String deleteRoleCode);

	public int AttorneyHolderInfo(AttorneyHolderDTO attorneyDTO);

	public List<UserRoleDTO> assignRole(String empNo, String depCode);

	public List<ManageRoleDTO> serachAllDetailsForDep(String dep);

	public String getDepartment(String empNo);

	public String getLocation(String empNo);

	public boolean CheckUserForAssign(String empnumber);

	public String isEpfNoAvilable(String epfNo);

	public String isNICNoAvilable(String nicNo);

	public boolean isDepartmentAvailable(String data, String epfnumber, String nicnumber);

	public boolean isDesignationAvailable(String data, String epfnumber, String nicnumber);

	public boolean isActiveEmployee(String data);

	public boolean checkReqType(List<EmployeeDTO> checkList);

	public boolean checkIsDataAvailable(String epfNo, String nicNo, String userId, String status, String location,
			String empNo);

	public List<EmployeeDTO> getPendingEmpDetails();

	public String checkCurrentStatusForEmp(String employeeNo);

}
