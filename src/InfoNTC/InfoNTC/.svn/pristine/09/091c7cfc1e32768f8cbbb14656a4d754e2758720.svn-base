package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import lk.informatics.ntc.model.dto.EmployeeDTO;
import lk.informatics.ntc.model.dto.UserRoleDTO;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.model.service.UserService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "approveEmployeeProfile")
@ViewScoped
public class ApproveEmployeeProfile implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private List<String> epfNumberList = new ArrayList<String>(0);
	private List<String> epfLocationList = new ArrayList<String>(0);
	private List<String> empNo = new ArrayList<String>(0);
	private List<String> userIdList = new ArrayList<String>(0);
	private List<String> filteredThemes = new ArrayList<String>(0);

	private EmployeeProfileService employeeProfileService;
	private UserService userService;

	private EmployeeDTO selectedEmp, searchEmployeeDTO;
	private List<EmployeeDTO> selectedEmps, empolyeeDetails;
	private List<UserRoleDTO> selectUserRole, userDetails;

	private String selEmpolyeeEPT, selEmpolyeeStatus, selUserId, selEmpolyeeNIC = null, selEmpolyeeLocation,
			selEmpolyeeNO, nicNumber;
	private boolean disable, disable2, isApprove, value;
	private String approveStatus, errorStatus, alertStatus;
	private String selectAssignUser, rejectReason;

	@PostConstruct
	public void init() {

		if (sessionBackingBean.approveURLStatus) {
			searchEmployeeDTO = new EmployeeDTO();
			loadValues();

		}
		if (!sessionBackingBean.approveURLStatus) {
			sessionBackingBean.setApproveURL(null);
			empolyeeDetails = sessionBackingBean.getTempEmpDataList();
			sessionBackingBean.setApproveURLStatus(true);
			searchEmployeeDTO = new EmployeeDTO();
			RequestContext.getCurrentInstance().update("checkboxDT");
			this.disable = false;
			this.disable2 = false;
		}
	}

	public void loadValues() {
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		userService = (UserService) SpringApplicationContex.getBean("userService");

		epfNumberList = employeeProfileService.getAllEpfNubers();
		epfLocationList = employeeProfileService.getAllEpfLocation();
		empNo = employeeProfileService.getEmpNo();
		userIdList = employeeProfileService.getAllEpfUserID();
		this.disable = true;
		this.disable2 = true;
		empolyeeDetails = new ArrayList<EmployeeDTO>();
		empolyeeDetails = employeeProfileService.getPendingEmpDetails();
	}

	public ApproveEmployeeProfile() {
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		userService = (UserService) SpringApplicationContex.getBean("userService");
		loadValues();
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msg = new FacesMessage("Car Selected", ((EmployeeDTO) event.getObject()).getEmpEpfNo());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void onRowUnselect(UnselectEvent event) {
		FacesMessage msg = new FacesMessage("Car Unselected", ((EmployeeDTO) event.getObject()).getEmpEpfNo());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public List<String> completeTheme(String query) {
		List<String> allThemes = epfNumberList;
		List<String> filteredThemes = new ArrayList<String>();
		for (int i = 0; i < allThemes.size(); i++) {
			if (allThemes.get(i).contains(query)) {
				filteredThemes.add(allThemes.get(i));
			}
		}

		return filteredThemes;
	}

	public List<String> completeLocation(String query) {
		List<String> allThemes = epfLocationList;
		List<String> filteredThemes = new ArrayList<String>();
		for (int i = 0; i < allThemes.size(); i++) {
			if (allThemes.get(i).contains(query)) {
				filteredThemes.add(allThemes.get(i));
			}
		}
		return filteredThemes;
	}

	public List<String> completeEmpNo(String query) {
		List<String> allThemes = empNo;
		List<String> filteredThemes = new ArrayList<String>();
		for (int i = 0; i < allThemes.size(); i++) {
			if (allThemes.get(i).contains(query)) {
				filteredThemes.add(allThemes.get(i));
			}
		}
		return filteredThemes;
	}

	public List<String> completeUserId(String query) {
		List<String> allThemes = userIdList;
		List<String> filteredThemes = new ArrayList<String>();
		for (int i = 0; i < allThemes.size(); i++) {
			if (allThemes.get(i).contains(query)) {
				filteredThemes.add(allThemes.get(i));
			}
		}
		return filteredThemes;
	}

	public List<EmployeeDTO> showData() {

		selEmpolyeeEPT = searchEmployeeDTO.getEmpEpfNo();
		selEmpolyeeStatus = searchEmployeeDTO.getStatus();
		selEmpolyeeNIC = nicNumber;
		selEmpolyeeLocation = searchEmployeeDTO.getLocation();
		selEmpolyeeNO = searchEmployeeDTO.getEmpNo();
		selUserId = searchEmployeeDTO.getUserId();

		if ((selEmpolyeeEPT == null || selEmpolyeeEPT.trim().equalsIgnoreCase(""))
				&& (selEmpolyeeStatus == null || selEmpolyeeStatus.trim().equalsIgnoreCase(""))
				&& (selEmpolyeeNIC == null || selEmpolyeeNIC.trim().equalsIgnoreCase(""))
				&& (selEmpolyeeLocation == null || selEmpolyeeLocation.trim().equalsIgnoreCase(""))
				&& (selEmpolyeeNO == null || selEmpolyeeNO.trim().equalsIgnoreCase(""))
				&& (selUserId == null || selUserId.trim().equalsIgnoreCase(""))) {

			setErrorStatus("Please select a field.");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			return null;

		} else {

			boolean checkinputData = employeeProfileService.checkIsDataAvailable(selEmpolyeeEPT, selEmpolyeeNIC,
					selUserId, selEmpolyeeStatus, selEmpolyeeLocation, selEmpolyeeNO);

			if (checkinputData == true) {

				if ((selEmpolyeeStatus == null || selEmpolyeeStatus.trim().equalsIgnoreCase(""))
						&& (selEmpolyeeLocation == null || selEmpolyeeLocation.trim().equalsIgnoreCase(""))
						&& (selUserId == null || selUserId.trim().equalsIgnoreCase(""))) {

					boolean isDepaAvailble = employeeProfileService.isDepartmentAvailable(selEmpolyeeNO, selEmpolyeeEPT,
							selEmpolyeeNIC);

					if (isDepaAvailble == true) {

						boolean isDesignationaAvailble = employeeProfileService.isDesignationAvailable(selEmpolyeeNO,
								selEmpolyeeEPT, selEmpolyeeNIC);

						if (isDesignationaAvailble == true) {

							empolyeeDetails = new ArrayList<EmployeeDTO>();
							empolyeeDetails = employeeProfileService.getEmpDetails(selEmpolyeeEPT, selEmpolyeeNIC,
									selUserId, selEmpolyeeStatus, selEmpolyeeLocation, selEmpolyeeNO);

							if (!empolyeeDetails.isEmpty()) {

								this.disable = false;
								this.disable2 = false;

								return empolyeeDetails;

							} else {
								setAlertStatus("No data for selected value.");
								RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
								return null;
							}

						} else {
							setAlertStatus("No active designation for selected value.");
							RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
							return null;
						}

					} else {
						setAlertStatus("No active department for selected value.");
						RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
						return null;
					}

				} else {

					empolyeeDetails = new ArrayList<EmployeeDTO>();
					empolyeeDetails = employeeProfileService.getEmpDetails(selEmpolyeeEPT, selEmpolyeeNIC, selUserId,
							selEmpolyeeStatus, selEmpolyeeLocation, selEmpolyeeNO);

					if (!empolyeeDetails.isEmpty()) {

						this.disable = false;
						this.disable2 = false;

						return empolyeeDetails;

					} else {
						setAlertStatus("No data for selected value.");
						RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
						return null;
					}

				}

			} else {
				setAlertStatus("No data for entered value.");
				RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");
				return null;
			}

		}

	}

	public void clearAction() {
		searchEmployeeDTO = new EmployeeDTO();
		nicNumber = null;
		empolyeeDetails = new ArrayList<EmployeeDTO>();
		empolyeeDetails = employeeProfileService.getPendingEmpDetails();
		disable = true;
		disable2 = true;
	}

	public String editAction() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		sessionBackingBean.setEmployeeNo(selectedEmp.getEmpNo());
		sessionBackingBean.setPageMode("V");
		sessionBackingBean.setApproveURL(request.getRequestURL().toString());
		sessionBackingBean.setSearchURL(null);
		sessionBackingBean.setApproveURLStatus(true);
		sessionBackingBean.tempEmpDataList = empolyeeDetails;

		return "/pages/um/employeeProfile.xhtml";
	}

	public void assignRole() {

		String selectUser = selectedEmp.getEmpNo();
		String depCode = selectedEmp.getDepCode();

		boolean isAvailble = employeeProfileService.CheckUserForAssign(selectUser);

		if (isAvailble == true) {
			setAlertStatus("User already approved. Can not assign a role.");
			RequestContext.getCurrentInstance().execute("PF('alertMessage').show()");

		} else {

			setSelectAssignUser(selectUser);

			userDetails = new ArrayList<UserRoleDTO>();
			userDetails = employeeProfileService.assignRole(selectUser, depCode);

			RequestContext.getCurrentInstance().execute("PF('assignRole').show()");
		}

	}

	public void saveRole() {

		boolean isAssigned = false;
		String loginUser = sessionBackingBean.getLoginUser();

		isAssigned = userService.assignRoleToEmployee(selectUserRole, getSelectAssignUser(), loginUser);

		if (isAssigned == true) {

			setApproveStatus("Role assigned successfully");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

		} else {

			setErrorStatus("Role assigning not successful");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");

		}

	}

	public void approveAction() {

		disable = false;
		String reqAcoount = null;

		if (!selectedEmps.isEmpty()) {

			boolean isreqaccount = employeeProfileService.checkReqType(selectedEmps);

			for (int a = 0; a < selectedEmps.size(); a++) {

				if (selectedEmps.get(a).getStatus().equals("APPROVED") && isreqaccount == true) {

					setErrorStatus("Employee is already Approved");
					RequestContext.getCurrentInstance().execute("PF('errStatus').show()");

				} else {

					String val = null;
					String loginUser = sessionBackingBean.getLoginUser();

					if (selectedEmps.get(a).isReqBool() == true) {
						reqAcoount = "y";
					} else {
						reqAcoount = "n";
					}

					employeeProfileService.approveUser(selectedEmps, loginUser, reqAcoount);
					empolyeeDetails = new ArrayList<EmployeeDTO>();
					empolyeeDetails = employeeProfileService.getEmpDetails(selEmpolyeeEPT, selEmpolyeeNIC, selUserId,
							selEmpolyeeStatus, selEmpolyeeLocation, selEmpolyeeNO);

					setApproveStatus("Employee is Approved");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				}

			}

		} else {
			setErrorStatus("Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			disable = false;
		}

	}

	public void checkRejectList() {
		disable = false;

		if (!selectedEmps.isEmpty()) {

			for (int i = 0; i < selectedEmps.size(); i++) {

				if (selectedEmps.get(i).getStatus().equals("REJECT")) {

					setErrorStatus("Employee is already rejected");
					RequestContext.getCurrentInstance().execute("PF('errStatus').show()");

				} else {
					RequestContext.getCurrentInstance().execute("PF('dlg1').show()");
				}

			}
		} else {
			setErrorStatus("Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
			disable = false;
		}
	}

	public void reject() {

		String loginUser = sessionBackingBean.getLoginUser();

		boolean isReject = employeeProfileService.rejectUser(selectedEmps, loginUser, rejectReason);

		if (isReject == true) {

			setApproveStatus("Employee is Rejected.");
			RequestContext.getCurrentInstance().execute("PF('dlg1').hide()");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");

		} else {
			setErrorStatus("Employee is not Rejected. ");
			RequestContext.getCurrentInstance().execute("PF('errStatus').show()");
		}

		empolyeeDetails = new ArrayList<EmployeeDTO>();
		empolyeeDetails = employeeProfileService.getEmpDetails(selEmpolyeeEPT, selEmpolyeeNIC, selUserId,
				selEmpolyeeStatus, selEmpolyeeLocation, selEmpolyeeNO);

	}

	public List<UserRoleDTO> getUserDetails() {
		return userDetails;
	}

	public List<String> getFilteredThemes() {
		return filteredThemes;
	}

	public void setFilteredThemes(List<String> filteredThemes) {
		this.filteredThemes = filteredThemes;
	}

	public List<UserRoleDTO> getSelectUserRole() {
		return selectUserRole;
	}

	public String getSelectAssignUser() {
		return selectAssignUser;
	}

	public void setSelectAssignUser(String selectAssignUser) {
		this.selectAssignUser = selectAssignUser;
	}

	public void setSelectUserRole(List<UserRoleDTO> selectUserRole) {
		this.selectUserRole = selectUserRole;
	}

	public void setUserDetails(List<UserRoleDTO> userDetails) {
		this.userDetails = userDetails;
	}

	public boolean isDisable2() {
		return disable2;
	}

	public void setDisable2(boolean disable2) {
		this.disable2 = disable2;
	}

	public String getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(String alertStatus) {
		this.alertStatus = alertStatus;
	}

	public String getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}

	public List<String> getEmpNo() {
		return empNo;
	}

	public void setEmpNo(List<String> empNo) {
		this.empNo = empNo;
	}

	public List<String> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<String> userIdList) {
		this.userIdList = userIdList;
	}

	public boolean isApprove() {
		return isApprove;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public void setApprove(boolean isApprove) {
		this.isApprove = isApprove;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public List<EmployeeDTO> getEmpolyeeDetails() {
		return empolyeeDetails;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public EmployeeDTO getSelectedEmp() {
		return selectedEmp;
	}

	public void setSelectedEmp(EmployeeDTO selectedEmp) {
		this.selectedEmp = selectedEmp;
	}

	public List<EmployeeDTO> getSelectedEmps() {
		return selectedEmps;
	}

	public void setSelectedEmps(List<EmployeeDTO> selectedEmps) {
		this.selectedEmps = selectedEmps;
	}

	public void setEmpolyeeDetails(List<EmployeeDTO> empolyeeDetails) {
		this.empolyeeDetails = empolyeeDetails;
	}

	public List<String> getEpfUserNo() {
		return empNo;
	}

	public void setEpfUserNo(List<String> epfUserNo) {
		this.empNo = epfUserNo;
	}

	public List<String> getEpfLocationList() {
		return epfLocationList;
	}

	public void setEpfLocationList(List<String> epfLocationList) {
		this.epfLocationList = epfLocationList;
	}

	public String getNicNumber() {
		return nicNumber;
	}

	public void setNicNumber(String nicNumber) {
		this.nicNumber = nicNumber;
	}

	public List<String> getEpfNumberList() {
		return epfNumberList;
	}

	public void setEpfNumberList(List<String> epfNumberList) {
		this.epfNumberList = epfNumberList;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public EmployeeDTO getSearchEmployeeDTO() {
		return searchEmployeeDTO;
	}

	public void setSearchEmployeeDTO(EmployeeDTO searchEmployeeDTO) {
		this.searchEmployeeDTO = searchEmployeeDTO;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

}
