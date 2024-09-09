package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.SetupCommitteeBoardDTO;
import lk.informatics.ntc.model.service.SetupCommitteeBoardService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "setupCommitteeBoardBackingBean")
@ViewScoped
public class SetupCommitteeBoardBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	SetupCommitteeBoardDTO setupCommitteeBoardDTO;
	SetupCommitteeBoardDTO deleteMemberDTO;
	SetupCommitteeBoardDTO authorizeCommitteeBoardDTO;
	SetupCommitteeBoardDTO editCommitteeBoardDTO;
	public SetupCommitteeBoardService setupCommitteeBoardService;
	private List<SetupCommitteeBoardDTO> transactionTypeList = new ArrayList<SetupCommitteeBoardDTO>(0);
	private List<SetupCommitteeBoardDTO> organizationList = new ArrayList<SetupCommitteeBoardDTO>(0);
	private List<SetupCommitteeBoardDTO> userIDList = new ArrayList<SetupCommitteeBoardDTO>(0);
	private List<SetupCommitteeBoardDTO> designationList = new ArrayList<SetupCommitteeBoardDTO>(0);
	private List<SetupCommitteeBoardDTO> membersList = new ArrayList<SetupCommitteeBoardDTO>(0);
	private List<SetupCommitteeBoardDTO> referenceNoListforAuth = new ArrayList<SetupCommitteeBoardDTO>(0);
	private List<SetupCommitteeBoardDTO> referenceNoListforEdit = new ArrayList<SetupCommitteeBoardDTO>(0);
	private List<SetupCommitteeBoardDTO> referenceNoListforView = new ArrayList<SetupCommitteeBoardDTO>(0);

	private String generateStatus;
	private String errorMessage, successMessage, infoMessage;
	boolean disabledRefNo = false, disabledBtnAuthorized = true, disabledBtnSave = false, disableUserID = true,
			disableTransactionType = false, disableType = false, disableDate = false, disableAddMembers = false,
			disableDesignationName = true, displayOption_com = false, displayOption_brd = false,
			displayOption_env = false, disableOnSelectRefNo = true;
	String loginUser;
	String authorizedStr;

	@PostConstruct
	public void init() {
		loginUser = sessionBackingBean.loginUser;
		setupCommitteeBoardDTO = new SetupCommitteeBoardDTO();
		authorizeCommitteeBoardDTO = new SetupCommitteeBoardDTO();
		editCommitteeBoardDTO = new SetupCommitteeBoardDTO();
		setupCommitteeBoardService = (SetupCommitteeBoardService) SpringApplicationContex
				.getBean("setupCommitteeBoardService");
		transactionTypeList = setupCommitteeBoardService.getTransactionTypeList();
		organizationList = setupCommitteeBoardService.getOrganizationListList();
		userIDList = setupCommitteeBoardService.getUserIDList();
		designationList = setupCommitteeBoardService.getDesignationList();
		referenceNoListforAuth = setupCommitteeBoardService.getReferenceNoListforAuth();
		referenceNoListforEdit = setupCommitteeBoardService.getReferenceNoListforEdit();
		referenceNoListforView = setupCommitteeBoardService.getReferenceNoListforView();
	}

	public void onChangeTransactionType() {
		if (setupCommitteeBoardDTO.getTransactionTypeCode() != null
				&& !setupCommitteeBoardDTO.getTransactionTypeCode().trim().isEmpty()) {
			if (setupCommitteeBoardDTO.getTransactionTypeCode().equals("01")) {
				displayOption_com = false;
				displayOption_brd = true;
				displayOption_env = true;
			} else {
				displayOption_com = true;
				displayOption_brd = true;
				displayOption_env = false;
			}
		} else {
			displayOption_com = false;
			displayOption_brd = false;
			displayOption_env = false;
		}
	}

	@SuppressWarnings("deprecation")
	public void onDateSelectSetup() {
		if (setupCommitteeBoardDTO.getActiveFromDate() != null && setupCommitteeBoardDTO.getActiveToDate() != null) {
			if ((setupCommitteeBoardDTO.getActiveFromDate().compareTo(setupCommitteeBoardDTO.getActiveToDate()) > 0)) {
				setupCommitteeBoardDTO.setActiveToDate(null);
				setErrorMessage("Active To Date should be after Active From Date.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void onDateSelectEdit() {
		if (editCommitteeBoardDTO.getActiveFromDate() != null && editCommitteeBoardDTO.getActiveToDate() != null) {
			if ((editCommitteeBoardDTO.getActiveFromDate().compareTo(editCommitteeBoardDTO.getActiveToDate()) > 0)) {
				editCommitteeBoardDTO.setActiveToDate(null);
				setErrorMessage("Active To Date should be after Active From Date.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void addToGrid() {
		if (setupCommitteeBoardDTO.getTransactionTypeCode() == null
				|| setupCommitteeBoardDTO.getTransactionTypeCode().trim().isEmpty()) {
			setErrorMessage("Transaction Type should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (setupCommitteeBoardDTO.getType() == null || setupCommitteeBoardDTO.getType().trim().isEmpty()) {
			setErrorMessage("Type should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (setupCommitteeBoardDTO.getActiveFromDate() == null) {
			setErrorMessage("Active From Date should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else if (setupCommitteeBoardDTO.getActiveToDate() == null) {
			setErrorMessage("Active To Date should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else {
			Boolean userIdEntered = true;
			if (setupCommitteeBoardDTO.getOrganizationCode().equals("ORG01")) {
				if (setupCommitteeBoardDTO.getUserID() != null && !setupCommitteeBoardDTO.getUserID().equals("")) {
					userIdEntered = true;
				} else {
					userIdEntered = false;
				}
			}
			if (setupCommitteeBoardDTO.getOrganizationCode() != null
					&& !setupCommitteeBoardDTO.getOrganizationCode().equals("")
					&& setupCommitteeBoardDTO.getDesignationCode() != null
					&& !setupCommitteeBoardDTO.getDesignationCode().equals("")
					&& setupCommitteeBoardDTO.getName() != null && !setupCommitteeBoardDTO.getName().equals("")
					&& setupCommitteeBoardDTO.getResponsibilities() != null
					&& !setupCommitteeBoardDTO.getResponsibilities().equals("") && userIdEntered) {

				SetupCommitteeBoardDTO memberDTO = new SetupCommitteeBoardDTO();
				memberDTO.setOrganizationCode(setupCommitteeBoardDTO.getOrganizationCode());
				memberDTO.setDesignationCode(setupCommitteeBoardDTO.getDesignationCode());
				memberDTO.setOrganizationDes(setupCommitteeBoardDTO.getOrganizationDes());
				memberDTO.setDesignationDes(setupCommitteeBoardDTO.getDesignationDes());
				memberDTO.setUserID(setupCommitteeBoardDTO.getUserID());
				memberDTO.setName(setupCommitteeBoardDTO.getName());
				memberDTO.setResponsibilities(setupCommitteeBoardDTO.getResponsibilities());

				if (membersList.size() > 0) {
					boolean isDuplicate = false;
					for (int i = 0; i < membersList.size(); i++) {
						if (setupCommitteeBoardDTO.getUserID() != null) {
							if (setupCommitteeBoardDTO.getUserID().equals(membersList.get(i).getUserID())) {
								setErrorMessage("Duplicate User ID.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								isDuplicate = true;
								break;
							}
						}
					}
					if (isDuplicate == false) {
						membersList.add(memberDTO);
					}
				} else {
					membersList.add(memberDTO);
				}

				clearMemberDetails();

			} else {
				setErrorMessage("Fill all mandatory fields.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}
	}

	public void clearMemberDetails() {
		setupCommitteeBoardDTO.setOrganizationCode(null);
		setupCommitteeBoardDTO.setDesignationCode(null);
		setupCommitteeBoardDTO.setOrganizationDes(null);
		setupCommitteeBoardDTO.setDesignationDes(null);
		setupCommitteeBoardDTO.setUserID(null);
		setupCommitteeBoardDTO.setName(null);
		setupCommitteeBoardDTO.setResponsibilities(null);
		disableUserID = true;
		disableDesignationName = true;
	}

	public void addOrganizationDesToBean() {
		if (setupCommitteeBoardDTO.getOrganizationCode() != null) {
			for (SetupCommitteeBoardDTO dto : organizationList) {
				if (setupCommitteeBoardDTO.getOrganizationCode().equals(dto.getOrganizationCode())) {
					setupCommitteeBoardDTO.setOrganizationDes(dto.getOrganizationDes());
				}
			}

			if (!setupCommitteeBoardDTO.getOrganizationCode().equals("ORG01")) {
				disableUserID = true;
				disableDesignationName = false;
				setupCommitteeBoardDTO.setUserID(null);
				setupCommitteeBoardDTO.setName(null);
				setupCommitteeBoardDTO.setDesignationCode(null);
				setupCommitteeBoardDTO.setDesignationDes(null);
			} else {
				disableUserID = false;
				setupCommitteeBoardDTO.setName(null);
				setupCommitteeBoardDTO.setDesignationCode(null);
				disableDesignationName = true;
			}

		}
	}

	public void addDesignationDesToBean() {
		if (setupCommitteeBoardDTO.getDesignationCode() != null) {
			for (SetupCommitteeBoardDTO dto : designationList) {
				if (setupCommitteeBoardDTO.getDesignationCode().equals(dto.getDesignationCode())) {
					setupCommitteeBoardDTO.setDesignationDes(dto.getDesignationDes());
				}
			}
		}
	}

	public void onUserIDChange() {
		if (setupCommitteeBoardDTO.getUserID() != null) {
			for (SetupCommitteeBoardDTO dto : userIDList) {
				if (setupCommitteeBoardDTO.getUserID().equals(dto.getUserID())) {
					setupCommitteeBoardDTO.setName(dto.getName());
					setupCommitteeBoardDTO.setDesignationCode(dto.getDesignationCode());
					setupCommitteeBoardDTO.setDesignationDes(dto.getDesignationDes());
				}
			}
			if (setupCommitteeBoardDTO.getUserID().equals("")) {
				setupCommitteeBoardDTO.setName(null);
				setupCommitteeBoardDTO.setDesignationCode(null);
				setupCommitteeBoardDTO.setDesignationDes(null);
			}
		}
	}

	public void clearAll() {
		setupCommitteeBoardDTO.setType(null);
		setupCommitteeBoardDTO.setReferenceNo(null);
		setupCommitteeBoardDTO.setTransactionTypeCode(null);
		setupCommitteeBoardDTO.setTransactionTypeDes(null);
		setupCommitteeBoardDTO.setStatus("A");
		setupCommitteeBoardDTO.setActiveFromDate(null);
		setupCommitteeBoardDTO.setActiveToDate(null);
		setupCommitteeBoardDTO.setAuthorization(null);
		clearMemberDetails();
		membersList.clear();
		disabledBtnAuthorized = true;
		disabledBtnSave = false;
		disableAddMembers = false;
		authorizedStr = null;
		disableTransactionType = false;
		disableType = false;
		displayOption_com = false;
		displayOption_brd = false;
		displayOption_env = false;
	}

	@SuppressWarnings("deprecation")
	public void deleteAction() {
		if (deleteMemberDTO != null) {

			membersList.remove(deleteMemberDTO);

		} else {

			setErrorMessage("Delete unsuccesssful.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			deleteMemberDTO = null;
			return;
		}
	}

	@SuppressWarnings("deprecation")
	public void saveAction() {

		if (setupCommitteeBoardDTO.getType() != null && !setupCommitteeBoardDTO.getType().trim().isEmpty()) {
			if (setupCommitteeBoardDTO.getTransactionTypeCode() != null
					&& !setupCommitteeBoardDTO.getTransactionTypeCode().trim().isEmpty()) {
				if (setupCommitteeBoardDTO.getActiveFromDate() != null) {
					if (setupCommitteeBoardDTO.getActiveToDate() != null) {
						if (!(setupCommitteeBoardDTO.getActiveFromDate()
								.compareTo(setupCommitteeBoardDTO.getActiveToDate()) > 0)) {
							if (membersList.size() > 0) {

								if (setupCommitteeBoardDTO.getReferenceNo() != null
										&& !setupCommitteeBoardDTO.getReferenceNo().trim().isEmpty()) {
									int rs = setupCommitteeBoardService.updateMasterData(setupCommitteeBoardDTO,
											setupCommitteeBoardDTO.getReferenceNo(), loginUser);
									if (rs > 0) {
										setSuccessMessage("Updated successfully.");
										RequestContext.getCurrentInstance().update("successMSG");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									} else {
										setErrorMessage("Update unsuccessful.");
										RequestContext.getCurrentInstance().update("errorMSG");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
								} else {
									String referenceNo = setupCommitteeBoardService.generateReferenceNo();
									setupCommitteeBoardService.saveMasterData(setupCommitteeBoardDTO, referenceNo,
											loginUser);
									setupCommitteeBoardDTO.setReferenceNo(referenceNo);
									disableType = true;
									disableTransactionType = true;
									setupCommitteeBoardService.saveMemberDetails(membersList, referenceNo, loginUser);
									setSuccessMessage("Saved successfully.");
									RequestContext.getCurrentInstance().update("successMSG");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
								}
								disabledBtnAuthorized = false;
								disableAddMembers = true;
								clearMemberDetails();
							} else {
								setErrorMessage("No members in the Committee/Board.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
						} else {
							setErrorMessage("Active To Date should be after Active From Date.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					} else {
						setErrorMessage("Fill all mandatory fields.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Fill all mandatory fields.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Fill all mandatory fields.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Fill all mandatory fields.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	@SuppressWarnings("deprecation")
	public void updateAction() {

		if (editCommitteeBoardDTO.getType() != null && !editCommitteeBoardDTO.getType().trim().isEmpty()) {
			if (editCommitteeBoardDTO.getTransactionTypeCode() != null
					&& !editCommitteeBoardDTO.getTransactionTypeCode().trim().isEmpty()) {
				if (editCommitteeBoardDTO.getActiveFromDate() != null) {
					if (editCommitteeBoardDTO.getActiveToDate() != null) {
						if (!(editCommitteeBoardDTO.getActiveFromDate()
								.compareTo(editCommitteeBoardDTO.getActiveToDate()) > 0)) {
							if (membersList.size() > 0) {

								if (editCommitteeBoardDTO.getReferenceNo() != null
										&& !editCommitteeBoardDTO.getReferenceNo().trim().isEmpty()) {

									int rs = setupCommitteeBoardService.updateMasterData(editCommitteeBoardDTO,
											editCommitteeBoardDTO.getReferenceNo(), loginUser);
									if (rs > 0) {
										setSuccessMessage("Updated successfully.");
										RequestContext.getCurrentInstance().update("successMSG");
										RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									} else {
										setErrorMessage("Already authorized.");
										RequestContext.getCurrentInstance().update("errorMSG");
										RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
									}
								}
							} else {
								setErrorMessage("No members in the Committee/Board.");
								RequestContext.getCurrentInstance().update("errorMSG");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
						} else {
							setErrorMessage("Active To Date should be after Active From Date.");
							RequestContext.getCurrentInstance().update("errorMSG");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					} else {
						setErrorMessage("Fill all mandatory fields.");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}
				} else {
					setErrorMessage("Fill all mandatory fields.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Fill all mandatory fields.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Fill all mandatory fields.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	@SuppressWarnings("deprecation")
	public void authorizedAction() {
		disabledBtnSave = true;
		setupCommitteeBoardService.authorizedData(authorizeCommitteeBoardDTO.getReferenceNo(), loginUser);
		authorizedStr = "GRANTED";
		disabledBtnAuthorized = true;
		disabledRefNo = true;
		setSuccessMessage("Authorization granted.");
		RequestContext.getCurrentInstance().update("successMSG");
		RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
	}

	@SuppressWarnings("deprecation")
	public void showConfirmRemoveMessage() {
		RequestContext.getCurrentInstance().update("deleteconfirmationAct");
		RequestContext.getCurrentInstance().execute("PF('deleteconfirmationAct').show()");
	}

	public void onReferenceNoChange() {
		// for authorize CommitteeBoard
		if (authorizeCommitteeBoardDTO.getReferenceNo() != null
				&& !authorizeCommitteeBoardDTO.getReferenceNo().equals("")) {

			SetupCommitteeBoardDTO dto = setupCommitteeBoardService
					.getMasterDataFromReferenceNo(authorizeCommitteeBoardDTO.getReferenceNo());
			authorizeCommitteeBoardDTO.setType(dto.getType());
			authorizeCommitteeBoardDTO.setTransactionTypeCode(dto.getTransactionTypeCode());
			authorizeCommitteeBoardDTO.setTransactionTypeDes(dto.getTransactionTypeDes());
			authorizeCommitteeBoardDTO.setStatus(dto.getStatus());
			authorizeCommitteeBoardDTO.setActiveFromDateStr(dto.getActiveFromDateStr());
			authorizeCommitteeBoardDTO.setActiveToDateStr(dto.getActiveToDateStr());

			membersList = setupCommitteeBoardService
					.getMemberDataFromReferenceNo(authorizeCommitteeBoardDTO.getReferenceNo());
			if (dto.getIsauthorized() != null && dto.getIsauthorized().equals("Y")) {
				disabledBtnAuthorized = true;
				authorizedStr = "GRANTED";
			} else {
				disabledBtnAuthorized = false;
				authorizedStr = "PENDING";
			}
		} else {
			disabledBtnAuthorized = true;
			clearAuthorizeCommitteeBoard();
		}

		// for edit CommitteeBoard
		if (editCommitteeBoardDTO.getReferenceNo() != null && !editCommitteeBoardDTO.getReferenceNo().equals("")) {
			SetupCommitteeBoardDTO dto = setupCommitteeBoardService
					.getMasterDataFromReferenceNo(editCommitteeBoardDTO.getReferenceNo());
			editCommitteeBoardDTO.setType(dto.getType());
			editCommitteeBoardDTO.setTransactionTypeCode(dto.getTransactionTypeCode());
			editCommitteeBoardDTO.setTransactionTypeDes(dto.getTransactionTypeDes());
			editCommitteeBoardDTO.setStatus(dto.getStatus());
			editCommitteeBoardDTO.setActiveFromDate(dto.getActiveFromDate());
			editCommitteeBoardDTO.setActiveToDate(dto.getActiveToDate());

			String authorized = setupCommitteeBoardService.getAuthorizedStatus(editCommitteeBoardDTO.getReferenceNo());

			if (authorized != null && authorized.equals("Y")) {

				setDisableOnSelectRefNo(true);

			} else {

				setDisableOnSelectRefNo(false);

			}

			setGenerateStatus(dto.getStatus());
			membersList = setupCommitteeBoardService
					.getMemberDataFromReferenceNo(editCommitteeBoardDTO.getReferenceNo());
		}
	}

	public void clearAuthorizeCommitteeBoard() {
		authorizeCommitteeBoardDTO = new SetupCommitteeBoardDTO();
		membersList.clear();
		disabledBtnAuthorized = true;
		disabledRefNo = false;
		authorizedStr = null;
		referenceNoListforAuth = setupCommitteeBoardService.getReferenceNoListforAuth();
	}

	public void clearAllEdit() {
		editCommitteeBoardDTO = new SetupCommitteeBoardDTO();
		membersList.clear();
		referenceNoListforEdit = setupCommitteeBoardService.getReferenceNoListforEdit();
	}

	public void check() {

		int result = setupCommitteeBoardService.getRefNo(setupCommitteeBoardDTO.getType(),
				setupCommitteeBoardDTO.getTransactionTypeCode());

		if (result == 1) {

			setErrorMessage("Active record found for this Type");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			setupCommitteeBoardDTO.setType("");
		}

	}

	public void checkStatus() {

		if (editCommitteeBoardDTO.getStatus().equals("A")
				&& !editCommitteeBoardDTO.getStatus().equals(getGenerateStatus())) {

			int result = setupCommitteeBoardService.getActiveStatus(editCommitteeBoardDTO.getType(),
					editCommitteeBoardDTO.getTransactionTypeCode());

			if (result == 1) {

				setErrorMessage("Active record found for this Type");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				editCommitteeBoardDTO.setStatus("I");

			}

		}

	}

	public SetupCommitteeBoardDTO getSetupCommitteeBoardDTO() {
		return setupCommitteeBoardDTO;
	}

	public void setSetupCommitteeBoardDTO(SetupCommitteeBoardDTO setupCommitteeBoardDTO) {
		this.setupCommitteeBoardDTO = setupCommitteeBoardDTO;
	}

	public SetupCommitteeBoardService getSetupCommitteeBoardService() {
		return setupCommitteeBoardService;
	}

	public void setSetupCommitteeBoardService(SetupCommitteeBoardService setupCommitteeBoardService) {
		this.setupCommitteeBoardService = setupCommitteeBoardService;
	}

	public List<SetupCommitteeBoardDTO> getTransactionTypeList() {
		return transactionTypeList;
	}

	public void setTransactionTypeList(List<SetupCommitteeBoardDTO> transactionTypeList) {
		this.transactionTypeList = transactionTypeList;
	}

	public List<SetupCommitteeBoardDTO> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<SetupCommitteeBoardDTO> organizationList) {
		this.organizationList = organizationList;
	}

	public List<SetupCommitteeBoardDTO> getUserIDList() {
		return userIDList;
	}

	public void setUserIDList(List<SetupCommitteeBoardDTO> userIDList) {
		this.userIDList = userIDList;
	}

	public List<SetupCommitteeBoardDTO> getDesignationList() {
		return designationList;
	}

	public void setDesignationList(List<SetupCommitteeBoardDTO> designationList) {
		this.designationList = designationList;
	}

	public List<SetupCommitteeBoardDTO> getMembersList() {
		return membersList;
	}

	public void setMembersList(List<SetupCommitteeBoardDTO> membersList) {
		this.membersList = membersList;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public SetupCommitteeBoardDTO getDeleteMemberDTO() {
		return deleteMemberDTO;
	}

	public void setDeleteMemberDTO(SetupCommitteeBoardDTO deleteMemberDTO) {
		this.deleteMemberDTO = deleteMemberDTO;
	}

	public boolean isDisabledBtnAuthorized() {
		return disabledBtnAuthorized;
	}

	public void setDisabledBtnAuthorized(boolean disabledBtnAuthorized) {
		this.disabledBtnAuthorized = disabledBtnAuthorized;
	}

	public boolean isDisabledBtnSave() {
		return disabledBtnSave;
	}

	public void setDisabledBtnSave(boolean disabledBtnSave) {
		this.disabledBtnSave = disabledBtnSave;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public boolean isDisableUserID() {
		return disableUserID;
	}

	public void setDisableUserID(boolean disableUserID) {
		this.disableUserID = disableUserID;
	}

	public boolean isDisableTransactionType() {
		return disableTransactionType;
	}

	public void setDisableTransactionType(boolean disableTransactionType) {
		this.disableTransactionType = disableTransactionType;
	}

	public boolean isDisableType() {
		return disableType;
	}

	public void setDisableType(boolean disableType) {
		this.disableType = disableType;
	}

	public boolean isDisableDate() {
		return disableDate;
	}

	public void setDisableDate(boolean disableDate) {
		this.disableDate = disableDate;
	}

	public boolean isDisableAddMembers() {
		return disableAddMembers;
	}

	public void setDisableAddMembers(boolean disableAddMembers) {
		this.disableAddMembers = disableAddMembers;
	}

	public String getAuthorizedStr() {
		return authorizedStr;
	}

	public void setAuthorizedStr(String authorizedStr) {
		this.authorizedStr = authorizedStr;
	}

	public List<SetupCommitteeBoardDTO> getReferenceNoListforAuth() {
		return referenceNoListforAuth;
	}

	public void setReferenceNoListforAuth(List<SetupCommitteeBoardDTO> referenceNoListforAuth) {
		this.referenceNoListforAuth = referenceNoListforAuth;
	}

	public List<SetupCommitteeBoardDTO> getReferenceNoListforEdit() {
		return referenceNoListforEdit;
	}

	public void setReferenceNoListforEdit(List<SetupCommitteeBoardDTO> referenceNoListforEdit) {
		this.referenceNoListforEdit = referenceNoListforEdit;
	}

	public List<SetupCommitteeBoardDTO> getReferenceNoListforView() {
		return referenceNoListforView;
	}

	public void setReferenceNoListforView(List<SetupCommitteeBoardDTO> referenceNoListforView) {
		this.referenceNoListforView = referenceNoListforView;
	}

	public SetupCommitteeBoardDTO getAuthorizeCommitteeBoardDTO() {
		return authorizeCommitteeBoardDTO;
	}

	public void setAuthorizeCommitteeBoardDTO(SetupCommitteeBoardDTO authorizeCommitteeBoardDTO) {
		this.authorizeCommitteeBoardDTO = authorizeCommitteeBoardDTO;
	}

	public boolean isDisabledRefNo() {
		return disabledRefNo;
	}

	public void setDisabledRefNo(boolean disabledRefNo) {
		this.disabledRefNo = disabledRefNo;
	}

	public SetupCommitteeBoardDTO getEditCommitteeBoardDTO() {
		return editCommitteeBoardDTO;
	}

	public void setEditCommitteeBoardDTO(SetupCommitteeBoardDTO editCommitteeBoardDTO) {
		this.editCommitteeBoardDTO = editCommitteeBoardDTO;
	}

	public boolean isDisableDesignationName() {
		return disableDesignationName;
	}

	public void setDisableDesignationName(boolean disableDesignationName) {
		this.disableDesignationName = disableDesignationName;
	}

	public boolean isDisplayOption_com() {
		return displayOption_com;
	}

	public void setDisplayOption_com(boolean displayOption_com) {
		this.displayOption_com = displayOption_com;
	}

	public boolean isDisplayOption_brd() {
		return displayOption_brd;
	}

	public void setDisplayOption_brd(boolean displayOption_brd) {
		this.displayOption_brd = displayOption_brd;
	}

	public boolean isDisplayOption_env() {
		return displayOption_env;
	}

	public void setDisplayOption_env(boolean displayOption_env) {
		this.displayOption_env = displayOption_env;
	}

	public String getGenerateStatus() {
		return generateStatus;
	}

	public void setGenerateStatus(String generateStatus) {
		this.generateStatus = generateStatus;
	}

	public boolean isDisableOnSelectRefNo() {
		return disableOnSelectRefNo;
	}

	public void setDisableOnSelectRefNo(boolean disableOnSelectRefNo) {
		this.disableOnSelectRefNo = disableOnSelectRefNo;
	}

}