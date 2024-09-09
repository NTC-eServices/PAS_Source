package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.AmendmentDTO;
import lk.informatics.ntc.model.service.AmendmentService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.HistoryService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "amendmentsAuthorization")
@ViewScoped

public class AmendmentsAuthorizationBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private AmendmentDTO amendmentDTO, selectDTO, filledamendmentDTO, viewSelectDTO;
	private AmendmentService amendmentService;
	private List<AmendmentDTO> permitNoList = new ArrayList<AmendmentDTO>(0);
	private List<AmendmentDTO> applicationNoList = new ArrayList<AmendmentDTO>(0);
	private List<AmendmentDTO> amendmentsDetailList;
	private List<AmendmentDTO> transactionList = new ArrayList<AmendmentDTO>(0);
	private String alertMSG, successMessage, errorMessage, rejectReason;
	private CommonService commonService;
	private boolean disabledReject, disabledCancel, disabledApprovalTwo, disabledApprovalOne, disabledPermitNo,
			disabledApplicationNo;
	private boolean director, chairman = false;
	private HistoryService historyService;
	private AmendmentDTO amendmentHistoryDTO;
	private int accessLevel;

	@PostConstruct
	public void init() {
		amendmentDTO = new AmendmentDTO();
		selectDTO = new AmendmentDTO();
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		amendmentService = (AmendmentService) SpringApplicationContex.getBean("amendmentService");
		historyService = (HistoryService) SpringApplicationContex.getBean("historyService");

		director = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN310", "DA");
		chairman = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN310", "CA");

		if (director == true && chairman == true) {
			accessLevel = 3;
		} else if (director == true) {
			accessLevel = 1;
		} else if (chairman == true) {
			accessLevel = 2;
		} else {
			/* Show Every Thing */
			accessLevel = 3;
		}

		loadValues();
	}

	public void loadValues() {
		amendmentsDetailList = new ArrayList<>();
		amendmentsDetailList = amendmentService.getAmendmentsAuthorizationDefaultDetails(amendmentDTO, accessLevel);

		transactionList = amendmentService.getTransactionType();
		disabledReject = true;

		disabledApprovalTwo = true;
		disabledApprovalOne = true;
		disabledCancel = true;

		disabledApplicationNo = true;
		disabledPermitNo = true;
	}

	public void ajaxFillData() {

		amendmentDTO.setPermitNo(null);
		amendmentDTO.setNewBusNo(null);
		amendmentDTO.setExisitingBusNo(null);

		filledamendmentDTO = new AmendmentDTO();
		filledamendmentDTO = amendmentService.ajaxFillData(amendmentDTO);

		if (filledamendmentDTO.getApplicationNo() != null) {
			amendmentDTO.setApplicationNo(filledamendmentDTO.getApplicationNo());

			if (filledamendmentDTO.getPermitNo() != null) {
				amendmentDTO.setPermitNo(filledamendmentDTO.getPermitNo());

				if (filledamendmentDTO.getExisitingBusNo() != null) {
					amendmentDTO.setExisitingBusNo(filledamendmentDTO.getExisitingBusNo());

					if (filledamendmentDTO.getNewBusNo() != null) {
						amendmentDTO.setNewBusNo(filledamendmentDTO.getNewBusNo());
					}
				}
			}

		}

	}

	public void ajaxFilterApplicationNoANDPermitNo() {

		disabledApplicationNo = false;
		disabledPermitNo = false;

		permitNoList = amendmentService.getAmendmentsHigherApporvalPermitNO(amendmentDTO);
		applicationNoList = amendmentService.getAmendmentsHigherApporvalApplicationNO(amendmentDTO);
	}

	public void search() {
		if ((amendmentDTO.getApplicationNo() != null && !amendmentDTO.getApplicationNo().trim().equalsIgnoreCase(""))
				|| (amendmentDTO.getPermitNo() != null && !amendmentDTO.getPermitNo().trim().equalsIgnoreCase("")
						|| amendmentDTO.getTranCode() != null
								&& !amendmentDTO.getTranCode().trim().equalsIgnoreCase(""))) {

			amendmentsDetailList = new ArrayList<>();
			amendmentsDetailList = amendmentService.getGrantApprovalDetails(amendmentDTO);

			if (!amendmentsDetailList.isEmpty()) {

				disabledCancel = false;

			} else {
				setErrorMessage("No Data Found.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select the Application No. / Permit No. or Transaction Type");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void selectRow() {

		if (!amendmentsDetailList.isEmpty()) {

			if (selectDTO.getTranCode().equals("13")) {
				amendmentDTO.setTranCode("13");
			} else if (selectDTO.getTranCode().equals("21")) {
				amendmentDTO.setTranCode("21");
			} else if (selectDTO.getTranCode().equals("22")) {
				amendmentDTO.setTranCode("22");
			} else if (selectDTO.getTranCode().equals("23")) {
				amendmentDTO.setTranCode("23");
			} else if (selectDTO.getTranCode().equals("14")) {
				amendmentDTO.setTranCode("14");
			} else if (selectDTO.getTranCode().equals("15")) {
				amendmentDTO.setTranCode("15");
			} else if (selectDTO.getTranCode().equals("16")) {
				amendmentDTO.setTranCode("16");
			}

			if (selectDTO.getApplicationNo() != null) {
				amendmentDTO.setApplicationNo(selectDTO.getApplicationNo());
			}
		}

		changeButtonState();

	}

	public void changeButtonState() {

		disabledCancel = false;
		disabledReject = false;

		if (selectDTO.getTranCode().equals("13")) {

			if (amendmentService.checkTaskDetails(selectDTO, "AM100", "C") == true) {
				disabledApprovalOne = false;
				disabledApprovalTwo = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM101", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM100", "C") == true) {
				disabledApprovalOne = false;
				disabledApprovalTwo = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM100", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM100", "C") == true) {
				disabledApprovalOne = false;
				disabledApprovalTwo = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "AM105", "C") == true) {
				disabledApprovalOne = true;
				disabledApprovalTwo = false;
			} else {
				disabledApprovalOne = true;
				disabledApprovalTwo = true;
			}

		} else if (selectDTO.getTranCode().equals("14") || selectDTO.getTranCode().equals("15")
				|| selectDTO.getTranCode().equals("16") || selectDTO.getTranCode().equals("21")
				|| selectDTO.getTranCode().equals("23") || selectDTO.getTranCode().equals("22")) {

			if (amendmentService.checkTaskDetails(selectDTO, "AM103", "C") == true) {
				disabledApprovalOne = false;
				disabledApprovalTwo = true;
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM101", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM103", "C") == true) {
				disabledApprovalOne = false;
				disabledApprovalTwo = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM100", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM103", "C") == true) {
				disabledApprovalOne = false;
				disabledApprovalTwo = true;
				// Ongoing vehicle inspection task allow to continue the process
			} else if (amendmentService.checkTaskDetails(selectDTO, "PM101", "O") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM103", "C") == true) {
				disabledApprovalOne = false;
				disabledApprovalTwo = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "PM100", "O") == true
					&& amendmentService.checkTaskHistory(selectDTO, "AM103", "C") == true
					|| amendmentService.checkTaskDetails(selectDTO, "PM101", "C") == true
					&& amendmentService.checkTaskHistory(selectDTO, "PM101", "O") == true) {
				disabledApprovalOne = false;
				disabledApprovalTwo = true;

			} else if (amendmentService.checkTaskDetails(selectDTO, "AM105", "C") == true) {
				disabledApprovalOne = true;
				disabledApprovalTwo = false;
			} else {
				disabledApprovalOne = true;
				disabledApprovalTwo = true;
			}

		} else {
			disabledApprovalOne = true;
			disabledApprovalTwo = true;
		}
	}

	public void clearOne() {

		amendmentDTO = new AmendmentDTO();
		disabledApplicationNo = true;
		disabledPermitNo = true;
		disabledApprovalOne = true;
		disabledApprovalTwo = true;
		disabledReject = true;
		disabledCancel = true;
		amendmentsDetailList = new ArrayList<>();
		amendmentsDetailList = amendmentService.getAmendmentsAuthorizationDefaultDetails(amendmentDTO, accessLevel);

	}

	public void clearTwo() {

		amendmentsDetailList = new ArrayList<>();
		disabledReject = true;
		disabledCancel = true;

	}

//	public void approvalOne() {
//
//		/* Added DG / AD Approval function to all transaction type */
//
//		if (selectDTO != null) {
//
//			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {
//
//				boolean isAA001TaskDetails = amendmentService.checkTaskDetails(selectDTO, "AA001", "C");
//				boolean isAA001TaskHistory = amendmentService.checkTaskHistory(selectDTO, "AA001", "C");
//
//				if (isAA001TaskDetails == false && isAA001TaskHistory == false) {
//
//					boolean isPM101TaskDetails = amendmentService.checkTaskDetails(selectDTO, "PM101", "C");
//
//					if (isPM101TaskDetails == true) {
//
//						amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
//								sessionBackingBean.getLoginUser());
//						commonService.updateTaskStatus(selectDTO.getApplicationNo(), "PM101", "AA001", "C",
//								sessionBackingBean.getLoginUser());
//						boolean isApproved = commonService
//								.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "AA001");
//						amendmentService.updateAmendmentsTableStatus(selectDTO, "AD",
//								sessionBackingBean.getLoginUser());
//
//						if (isApproved == true) {
//
//							historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);
//
//							setSuccessMessage("Director / Asst. Director Approval Sccuessfull");
//							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
//							changeButtonState();
//
//							amendmentsDetailList = amendmentService
//									.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);
//
//						} else {
//							setErrorMessage("Director / Asst. Director Approval Sccuessfull is not Success.");
//							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//						}
//					} else {
//						setErrorMessage("Vehicle Inspection / Upload Photos is not completed");
//						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//
//					}
//
//				} else {
//					setErrorMessage("Selected Data is already approved");
//					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//				}
//
//			} else {
//				setErrorMessage("Selected Application No. is Rejected");
//				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//			}
//
//		} else
//
//		{
//			setErrorMessage("Please Select a row.");
//			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
//		}
//
//	}

	public void approvalOne() {

		/* Added DG / AD Approval function to all transaction type */

		if (selectDTO != null) {
          /**check entered application number in Inspection Incomplete and proceed Status in N**/
			boolean inspectionProceed =	commonService.IsHavingIncompleteInspection(selectDTO.getNewBusNo());
			if(!inspectionProceed) {
			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

				boolean isAA001TaskDetails = amendmentService.checkTaskDetails(selectDTO, "AA001", "C");
				boolean isAA001TaskHistory = amendmentService.checkTaskHistory(selectDTO, "AA001", "C");

				if (isAA001TaskDetails == false && isAA001TaskHistory == false) {

					boolean isPM101TaskDetails = amendmentService.checkTaskDetails(selectDTO, "PM101", "C");

					if (isPM101TaskDetails == true) {
						if(selectDTO.getTranCode().equals("22")) //added by dinushi.r
						{
							//validate route status	
							boolean istemap = amendmentService.checkRouteStatus(selectDTO.getApplicationNo());
							if(istemap)
							{
								setErrorMessage("Cannot approve Temporary Route No./Empty Route No.");
								RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
							}
							else
							{

								amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
										sessionBackingBean.getLoginUser());
								commonService.updateTaskStatus(selectDTO.getApplicationNo(), "PM101", "AA001", "C",
										sessionBackingBean.getLoginUser());
								boolean isApproved = commonService
										.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "AA001");
								amendmentService.updateAmendmentsTableStatus(selectDTO, "AD",
										sessionBackingBean.getLoginUser());

								if (isApproved == true) {

									historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);

									setSuccessMessage("Director / Asst. Director Approval Sccuessfull");
									RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
									changeButtonState();

									amendmentsDetailList = amendmentService
											.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);

								} else {
									setErrorMessage("Director / Asst. Director Approval Sccuessfull is not Success.");
									RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
								}
							
							}
						}
						else
						{
						amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
								sessionBackingBean.getLoginUser());
						commonService.updateTaskStatus(selectDTO.getApplicationNo(), "PM101", "AA001", "C",
								sessionBackingBean.getLoginUser());
						boolean isApproved = commonService
								.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "AA001");
						amendmentService.updateAmendmentsTableStatus(selectDTO, "AD",
								sessionBackingBean.getLoginUser());

						if (isApproved == true) {

							historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);

							setSuccessMessage("Director / Asst. Director Approval Sccuessfull");
							RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
							changeButtonState();

							amendmentsDetailList = amendmentService
									.getGrantApprovalDefaultDetailsForSelectedValue(selectDTO);

						} else {
							setErrorMessage("Director / Asst. Director Approval Sccuessfull is not Success.");
							RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						}
					} 
				}
					
					else {
						setErrorMessage("Vehicle Inspection / Upload Photos is not completed");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

					}

				} else {
					setErrorMessage("Selected Data is already approved");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Application No. is Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
			
			} else {
				setErrorMessage("Vehicle Inspection is Incomplete.Please Refer to High Authorized Officer");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				
			}

		} else

		{
			setErrorMessage("Please Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}


	public void approveTwo() {

		if (selectDTO != null) {

			if (amendmentService.CheckAmendmentsTableStatus(selectDTO, "R") == false) {

				boolean isAA002FoundDetails = amendmentService.checkTaskDetails(selectDTO, "AA002", "C");
				boolean isAA002FoundHistory = amendmentService.checkTaskHistory(selectDTO, "AA002", "C");

				if (isAA002FoundDetails == false && isAA002FoundHistory == false) {

					amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
							sessionBackingBean.getLoginUser());
					commonService.updateTaskStatus(selectDTO.getApplicationNo(), "AM105", "AA002", "C",
							sessionBackingBean.getLoginUser());
					boolean isApproved = commonService
							.updateTaskStatusCompletedForAmendments(selectDTO.getApplicationNo(), "AA002");
					amendmentService.updateAmendmentsTableStatus(selectDTO, "DG", sessionBackingBean.getLoginUser());

					if (isApproved == true) {
						historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);

						setSuccessMessage("DG / Chairman Approval Sccuessfull");
						RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

						amendmentsDetailList = new ArrayList<>();
						amendmentsDetailList = amendmentService.getAmendmentsAuthorizationDefaultDetails(amendmentDTO,
								accessLevel);
						disabledApprovalTwo = true;

					} else {
						setErrorMessage("DG / Chairman Approval Sccuessfull is not Success.");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						changeButtonState();
					}

				} else {
					setErrorMessage("Selected Data is already approved");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else {
				setErrorMessage("Selected Application No. is Rejected");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void preReject() {
		if (selectDTO != null) {

			boolean isFoundAM104InTaskDetails = amendmentService.checkTaskDetails(selectDTO, "AA002", "C");
			boolean isFoundAM104InTaskHistory = amendmentService.checkTaskHistory(selectDTO, "AA002", "C");

			if (isFoundAM104InTaskDetails == false && isFoundAM104InTaskHistory == false) {

				RequestContext.getCurrentInstance().execute("PF('dlg1').show()");

			} else {
				setErrorMessage("Can Not Reject, Approved By Director");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Please Select a row.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void reject() {

		boolean isAlreadyRejected = amendmentService.CheckAmendmentsTableStatus(selectDTO, "R");

		if (isAlreadyRejected == false) {

			if (rejectReason != null && !rejectReason.trim().equalsIgnoreCase("")) {

				amendmentHistoryDTO = historyService.getAmendmentTableData(selectDTO.getApplicationNo(),
						sessionBackingBean.getLoginUser());

				boolean isRejected = amendmentService.updateRejectData(selectDTO, rejectReason,
						sessionBackingBean.getLoginUser());
				if (isRejected == true) {
					historyService.insertAmendmentsHistoryData(amendmentHistoryDTO);

					setSuccessMessage("Successfully Rejected.");
					RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
					RequestContext.getCurrentInstance().execute("PF('dlg1').hide()");
					changeButtonState();
				} else {

					setErrorMessage("Rejection is not Successful");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

				}

			} else {
				setErrorMessage("Please Enter The Reject Reason");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Selected Application No. Already Rejected");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public AmendmentDTO getAmendmentDTO() {
		return amendmentDTO;
	}

	public void setAmendmentDTO(AmendmentDTO amendmentDTO) {
		this.amendmentDTO = amendmentDTO;
	}

	public AmendmentDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(AmendmentDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public AmendmentDTO getFilledamendmentDTO() {
		return filledamendmentDTO;
	}

	public void setFilledamendmentDTO(AmendmentDTO filledamendmentDTO) {
		this.filledamendmentDTO = filledamendmentDTO;
	}

	public AmendmentDTO getViewSelectDTO() {
		return viewSelectDTO;
	}

	public void setViewSelectDTO(AmendmentDTO viewSelectDTO) {
		this.viewSelectDTO = viewSelectDTO;
	}

	public AmendmentService getAmendmentService() {
		return amendmentService;
	}

	public void setAmendmentService(AmendmentService amendmentService) {
		this.amendmentService = amendmentService;
	}

	public List<AmendmentDTO> getPermitNoList() {
		return permitNoList;
	}

	public void setPermitNoList(List<AmendmentDTO> permitNoList) {
		this.permitNoList = permitNoList;
	}

	public List<AmendmentDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<AmendmentDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public List<AmendmentDTO> getAmendmentsDetailList() {
		return amendmentsDetailList;
	}

	public void setAmendmentsDetailList(List<AmendmentDTO> amendmentsDetailList) {
		this.amendmentsDetailList = amendmentsDetailList;
	}

	public List<AmendmentDTO> getTransactionList() {
		return transactionList;
	}

	public void setTransactionList(List<AmendmentDTO> transactionList) {
		this.transactionList = transactionList;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisabledReject() {
		return disabledReject;
	}

	public void setDisabledReject(boolean disabledReject) {
		this.disabledReject = disabledReject;
	}

	public boolean isDisabledCancel() {
		return disabledCancel;
	}

	public void setDisabledCancel(boolean disabledCancel) {
		this.disabledCancel = disabledCancel;
	}

	public boolean isDisabledApprovalTwo() {
		return disabledApprovalTwo;
	}

	public void setDisabledApprovalTwo(boolean disabledApprovalTwo) {
		this.disabledApprovalTwo = disabledApprovalTwo;
	}

	public boolean isDisabledApprovalOne() {
		return disabledApprovalOne;
	}

	public void setDisabledApprovalOne(boolean disabledApprovalOne) {
		this.disabledApprovalOne = disabledApprovalOne;
	}

	public boolean isDisabledPermitNo() {
		return disabledPermitNo;
	}

	public void setDisabledPermitNo(boolean disabledPermitNo) {
		this.disabledPermitNo = disabledPermitNo;
	}

	public boolean isDisabledApplicationNo() {
		return disabledApplicationNo;
	}

	public void setDisabledApplicationNo(boolean disabledApplicationNo) {
		this.disabledApplicationNo = disabledApplicationNo;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isDirector() {
		return director;
	}

	public void setDirector(boolean director) {
		this.director = director;
	}

	public boolean isChairman() {
		return chairman;
	}

	public void setChairman(boolean chairman) {
		this.chairman = chairman;
	}

}
