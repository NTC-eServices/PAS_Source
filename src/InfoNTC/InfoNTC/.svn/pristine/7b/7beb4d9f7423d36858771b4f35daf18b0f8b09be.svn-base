package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import lk.informatics.ntc.model.dto.BlockRouteDetailDTO;
import lk.informatics.ntc.model.dto.MainStationDetailsDTO;
import lk.informatics.ntc.model.dto.TerminalBlockDetailsDTO;
import lk.informatics.ntc.model.dto.TerminalDetailsDTO;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ViewScoped
@ManagedBean(name = "createBusTerminalBlockBackingBean")
public class CreateBusTerminalBlockBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String sucessMsg;
	private String errorMsg;

	// numeric
	String strBlockStatusRegex = "/[\\d]/";
	String strBlockStatus;

	private Long selectedTerminalBlockDetailsId;
	private String selectedTerminalBlockStatus;
	private TerminalBlockDetailsDTO deleteTerminalBlockDetailsDTO = new TerminalBlockDetailsDTO();

	public List<MainStationDetailsDTO> mainStationDetailsList = new ArrayList<MainStationDetailsDTO>(0);
	public List<TerminalDetailsDTO> terminalDetailsDtoList = new ArrayList<TerminalDetailsDTO>(0);
	public List<TerminalBlockDetailsDTO> terminalBlockDetailsDtoList = new ArrayList<TerminalBlockDetailsDTO>(0);

	List<String> terminalList;

	private TerminalManagementService terminalManagementService;
	private MainStationDetailsDTO mainStationDetailsDTO = new MainStationDetailsDTO();
	private TerminalBlockDetailsDTO terminalBlockDetailsDTO = new TerminalBlockDetailsDTO();

	private TerminalDetailsDTO selectedTerminal = new TerminalDetailsDTO();

	public CreateBusTerminalBlockBackingBean() {
		terminalManagementService = (TerminalManagementService) SpringApplicationContex
				.getBean("terminalManagementService");
		mainStationDetailsList = terminalManagementService.selectAllMainStations();
	}

	public void onMainStationChange() {
		String code = terminalBlockDetailsDTO.getStationCode();

		mainStationDetailsDTO.setDescription("");
		terminalBlockDetailsDTO.setTerminal("");
		terminalBlockDetailsDTO.setTerminalDetailsId("");
		terminalDetailsDtoList.clear();
		terminalBlockDetailsDtoList.clear();
		for (MainStationDetailsDTO mainStationDTO : mainStationDetailsList) {
			if (mainStationDTO.getCode().equalsIgnoreCase(code)) {

				mainStationDetailsDTO.setDescription(mainStationDTO.getDescription());
			}
		}
		terminalList = terminalManagementService.selectDistinctTerminalsByStation(code);
	}

	public void onTerminalChange() {
		String code = terminalBlockDetailsDTO.getStationCode();
		if (!(code == null || code == "")) {
			String terminal = terminalBlockDetailsDTO.getTerminal();

			if (terminal == null || terminal == "") {
				terminalBlockDetailsDTO.setTerminalDetailsId("");
				terminalDetailsDtoList.clear();
			} else {
				terminalDetailsDtoList = terminalManagementService.selectTerminalDetailsByTerminal(terminal, code);
			}
			terminalBlockDetailsDtoList.clear();
		}
	}

	public void onPlatformChange() {
		String terminalDetailsID = terminalBlockDetailsDTO.getTerminalDetailsId();
		if (!terminalDetailsID.isEmpty()) {

			selectedTerminal = new TerminalDetailsDTO();
			selectedTerminal.setSeq(Long.parseLong(terminalDetailsID));
			viewAction();
		}
	}

	public void generateBlocks() {
		String createdBy = sessionBackingBean.loginUser;
		createTerminalBlockDetails(createdBy);
	}

	public void createTerminalBlockDetails(String user) {

		if (!(terminalBlockDetailsDtoList.size() > 0 || terminalBlockDetailsDTO.getStationCode().isEmpty()
				|| terminalBlockDetailsDTO.getTerminal().isEmpty()
				|| terminalBlockDetailsDTO.getTerminalDetailsId().isEmpty()
				|| terminalBlockDetailsDTO.getNoOfBlocks() <= 0
				|| terminalBlockDetailsDTO.getBlockIdStartWith().isEmpty())) {

			if (validateStartValue(terminalBlockDetailsDTO.getBlockIdStartWith(),
					terminalBlockDetailsDTO.getNoOfBlocks())) {

				int seqno = terminalManagementService.saveBlockHeader(terminalBlockDetailsDTO, user);
				long selectedTerminalDetailsId = Long.parseLong(terminalBlockDetailsDTO.getTerminalDetailsId());
				TerminalDetailsDTO terminalDetailsDTO = terminalManagementService
						.selectTerminalBySequence(selectedTerminalDetailsId);
				String platform = terminalDetailsDTO.getPlatform();

				String strBlockIdStartChar = terminalBlockDetailsDTO.getBlockIdStartWith();
				int charValue = 0, blockCode = 0;
				if (strBlockIdStartChar.matches("[0-9]+")) {
					blockCode = Integer.parseInt(strBlockIdStartChar);
				} else {
					charValue = strBlockIdStartChar.charAt(0);
				}

				for (int i = 0; i < terminalBlockDetailsDTO.getNoOfBlocks(); i++) {
					if (terminalBlockDetailsDTO.getBlockDisplayType().equalsIgnoreCase("A")) {
						String blockCharacter = String.valueOf((char) (charValue));

						terminalManagementService.saveTerminalBlockDetails(terminalBlockDetailsDTO,
								String.valueOf(seqno), platform, blockCharacter, user);
						charValue++;
					} else {

						terminalManagementService.saveTerminalBlockDetails(terminalBlockDetailsDTO,
								String.valueOf(seqno), platform, String.valueOf(blockCode), user);
						blockCode++;
					}
				}
				RequestContext.getCurrentInstance().update("frmCreateBusTerminalBlocks");
				sucessMsg = "Successfully Saved.";
				RequestContext.getCurrentInstance().execute("PF('successSave').show()");
				terminalManagementService.beanLinkMethod(terminalBlockDetailsDTO, user, "Create Bus Terminal Blocks", "Manage Terminals Blocks");
				clearFields();
				viewAction();
			} else {

				errorMsg = "Terminals Start value exceeded the alphabhetical length with given no of terminals";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else if (terminalBlockDetailsDtoList.size() > 0) {
			errorMsg = "Blocks are already created for the selected station.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (terminalBlockDetailsDTO.getStationCode().isEmpty()) {

			errorMsg = "Station Code should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (terminalBlockDetailsDTO.getTerminal().isEmpty()) {

			errorMsg = "Terminal should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (terminalBlockDetailsDTO.getTerminalDetailsId().isEmpty()) {

			errorMsg = "Platform should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (terminalBlockDetailsDTO.getNoOfBlocks() <= 0) {

			errorMsg = "No Of Blocks should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (terminalBlockDetailsDTO.getBlockIdStartWith().isEmpty()) {

			errorMsg = "Blocks Id start value should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public boolean validateStartValue(String startVal, int count) {
		int length = count - 1;
		int charValue = startVal.charAt(0);
		int charValueZ = "Z".charAt(0);
		if ((charValue + length) > charValueZ) {
			return false;
		} else {
			return true;
		}
	}

	public void onBlockRadioChange() {
		if (terminalBlockDetailsDTO.getBlockDisplayType().equalsIgnoreCase("N")) {
			strBlockStatusRegex = "/[\\d]/";
			terminalBlockDetailsDTO.setBlockIdStartWith("");

		} else {
			strBlockStatusRegex = "/[a-z_]/i";
			terminalBlockDetailsDTO.setBlockIdStartWith("");

		}
	}

	public void clearDetails() {
		clearFields();
		terminalBlockDetailsDTO.setStationCode("");
		// clear grid
		terminalDetailsDtoList.clear();
		mainStationDetailsDTO.setDescription("");
	}

	public void clearFields() {
		terminalBlockDetailsDTO.setTerminal("");
		terminalBlockDetailsDTO.setTerminalDetailsId("");
		terminalBlockDetailsDTO.setPlatform("");
		terminalBlockDetailsDTO.setNoOfBlocks(0);
		terminalBlockDetailsDTO.setBlockDisplayType("N");
		strBlockStatusRegex = "/[\\d]/";
		terminalBlockDetailsDTO.setBlockIdStartWith("");
		terminalBlockDetailsDTO.setStatus("A");
		terminalBlockDetailsDtoList.clear();
	}

	public void onEditStatus(RowEditEvent event) {
		String modifiedBy = sessionBackingBean.loginUser;
		selectedTerminalBlockDetailsId = ((TerminalBlockDetailsDTO) event.getObject()).getSeq();
		selectedTerminalBlockStatus = ((TerminalBlockDetailsDTO) event.getObject()).getStatus();
		// update history table
		TerminalBlockDetailsDTO terminalBlockDetailsHistoryDTO = terminalManagementService
				.selectTerminalBlockBySequence(selectedTerminalBlockDetailsId);
		terminalManagementService.saveTerminalBlockDetailsHistory(terminalBlockDetailsHistoryDTO);
		// update status
		terminalManagementService.terminalBlockDetailStatusUpdate(selectedTerminalBlockDetailsId,
				selectedTerminalBlockStatus.substring(0, 1), modifiedBy);
	}

	public void deleteBusTerminalBlockAction() {
		RequestContext.getCurrentInstance().execute("PF('deleteconfirmationTerminal').show()");
	}

	public void removeAction() {
		List<BlockRouteDetailDTO> blockRouteList = terminalManagementService
				.selectBlockRoutes(terminalBlockDetailsDTO.getStationCode(), selectedTerminal.getPlatform());
		for (BlockRouteDetailDTO blkRoute : blockRouteList) {
			if (blkRoute.getBlockName().equals(deleteTerminalBlockDetailsDTO.getBlock())) {
				errorMsg = "Can not delete selecetd block. Block has a Bus route assigned !";
				RequestContext.getCurrentInstance().update("frmCreateBusTerminalBlocks:requiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return;
			}
		}

		boolean result = terminalManagementService.deleteSelectedTerminalBlock(deleteTerminalBlockDetailsDTO.getSeq());
		if (result) {
			RequestContext.getCurrentInstance().update("frmCreateBusTerminalBlocks");

			sucessMsg = "Successfully Deleted.";
			RequestContext.getCurrentInstance().execute("PF('successSave').show()");
			terminalBlockDetailsDtoList.remove(deleteTerminalBlockDetailsDTO);
			terminalManagementService.beanLinkMethod(terminalBlockDetailsDTO, sessionBackingBean.getLoginUser(), "Delete Bus Terminal Blocks", "Manage Terminals Blocks");

		} else {
			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
		}
	}

	public void viewAction() {
		terminalBlockDetailsDtoList = terminalManagementService
				.selectTerminalBlocksBySequence(selectedTerminal.getSeq());

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getStrBlockStatus() {
		return strBlockStatus;
	}

	public void setStrBlockStatus(String strBlockStatus) {
		this.strBlockStatus = strBlockStatus;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<MainStationDetailsDTO> getMainStationDetailsList() {
		return mainStationDetailsList;
	}

	public void setMainStationDetailsList(List<MainStationDetailsDTO> mainStationDetailsList) {
		this.mainStationDetailsList = mainStationDetailsList;
	}

	public List<TerminalDetailsDTO> getTerminalDetailsDtoList() {
		return terminalDetailsDtoList;
	}

	public void setTerminalDetailsDtoList(List<TerminalDetailsDTO> terminalDetailsDtoList) {
		this.terminalDetailsDtoList = terminalDetailsDtoList;
	}

	public TerminalManagementService getTerminalManagementService() {
		return terminalManagementService;
	}

	public void setTerminalManagementService(TerminalManagementService terminalManagementService) {
		this.terminalManagementService = terminalManagementService;
	}

	public MainStationDetailsDTO getMainStationDetailsDTO() {
		return mainStationDetailsDTO;
	}

	public void setMainStationDetailsDTO(MainStationDetailsDTO mainStationDetailsDTO) {
		this.mainStationDetailsDTO = mainStationDetailsDTO;
	}

	public TerminalBlockDetailsDTO getTerminalBlockDetailsDTO() {
		return terminalBlockDetailsDTO;
	}

	public void setTerminalBlockDetailsDTO(TerminalBlockDetailsDTO terminalBlockDetailsDTO) {
		this.terminalBlockDetailsDTO = terminalBlockDetailsDTO;
	}

	public List<String> getTerminalList() {
		return terminalList;
	}

	public void setTerminalList(List<String> terminalList) {
		this.terminalList = terminalList;
	}

	public String getStrBlockStatusRegex() {
		return strBlockStatusRegex;
	}

	public void setStrBlockStatusRegex(String strBlockStatusRegex) {
		this.strBlockStatusRegex = strBlockStatusRegex;
	}

	public TerminalDetailsDTO getSelectedTerminal() {
		return selectedTerminal;
	}

	public void setSelectedTerminal(TerminalDetailsDTO selectedTerminal) {
		this.selectedTerminal = selectedTerminal;
	}

	public List<TerminalBlockDetailsDTO> getTerminalBlockDetailsDtoList() {
		return terminalBlockDetailsDtoList;
	}

	public void setTerminalBlockDetailsDtoList(List<TerminalBlockDetailsDTO> terminalBlockDetailsDtoList) {
		this.terminalBlockDetailsDtoList = terminalBlockDetailsDtoList;
	}

	public Long getSelectedTerminalBlockDetailsId() {
		return selectedTerminalBlockDetailsId;
	}

	public void setSelectedTerminalBlockDetailsId(Long selectedTerminalBlockDetailsId) {
		this.selectedTerminalBlockDetailsId = selectedTerminalBlockDetailsId;
	}

	public String getSelectedTerminalBlockStatus() {
		return selectedTerminalBlockStatus;
	}

	public void setSelectedTerminalBlockStatus(String selectedTerminalBlockStatus) {
		this.selectedTerminalBlockStatus = selectedTerminalBlockStatus;
	}

	public TerminalBlockDetailsDTO getDeleteTerminalBlockDetailsDTO() {
		return deleteTerminalBlockDetailsDTO;
	}

	public void setDeleteTerminalBlockDetailsDTO(TerminalBlockDetailsDTO deleteTerminalBlockDetailsDTO) {
		this.deleteTerminalBlockDetailsDTO = deleteTerminalBlockDetailsDTO;
	}

}
