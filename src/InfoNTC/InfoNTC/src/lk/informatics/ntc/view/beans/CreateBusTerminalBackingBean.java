package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import lk.informatics.ntc.model.dto.MainStationDetailsDTO;
import lk.informatics.ntc.model.dto.TerminalDetailsDTO;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ViewScoped
@ManagedBean(name = "createBusTerminalBackingBean")
public class CreateBusTerminalBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String sucessMsg;
	private String errorMsg;

	// numeric
	String strTerminalStatusRegex = "/[\\d]/";
	// alphabetic
	String strPlatformStatusRegex = "/[A-Z_]/i";

	private Long selectedTerminalDetailsId;
	private String selectedTerminalStatus;
	private TerminalDetailsDTO deleteTerminalDetailsDTO = new TerminalDetailsDTO();

	public List<MainStationDetailsDTO> mainStationDetailsList = new ArrayList<MainStationDetailsDTO>(0);
	public List<TerminalDetailsDTO> terminalDetailsDtoList = new ArrayList<TerminalDetailsDTO>(0);

	private TerminalManagementService terminalManagementService;
	private MainStationDetailsDTO mainStationDetailsDTO = new MainStationDetailsDTO();
	private TerminalDetailsDTO terminalDetailsDTO = new TerminalDetailsDTO();

	public CreateBusTerminalBackingBean() {
		terminalManagementService = (TerminalManagementService) SpringApplicationContex
				.getBean("terminalManagementService");
		mainStationDetailsList = terminalManagementService.selectAllMainStations();
	}

	public void onMainStationChange() {
		String code = terminalDetailsDTO.getStationCode();

		for (MainStationDetailsDTO mainStationDTO : mainStationDetailsList) {
			if (mainStationDTO.getCode().equalsIgnoreCase(code)) {

				mainStationDetailsDTO.setDescription(mainStationDTO.getDescription());
			} else if (code == null || code == "") {
				mainStationDetailsDTO.setDescription("");
			}
		}
		terminalDetailsDtoList = terminalManagementService.selectTerminalsByStation(code);
	}

	public void generateTerminals() {
		String createdBy = sessionBackingBean.loginUser;
		createTerminalDetails(createdBy);
	}

	public void createTerminalDetails(String user) {

		if (!(terminalDetailsDtoList.size() > 0 || terminalDetailsDTO.getStationCode().isEmpty()
				|| terminalDetailsDTO.getNoOfPlatforms() <= 0 || terminalDetailsDTO.getNoOfTerminals() <= 0
				|| terminalDetailsDTO.getPlatformIdStartWith().isEmpty()
				|| terminalDetailsDTO.getTerminalIdStartWith().isEmpty())) {

			if (validateStartValue(terminalDetailsDTO.getTerminalIdStartWith(),
					terminalDetailsDTO.getNoOfTerminals())) {
				if (validateStartValue(terminalDetailsDTO.getPlatformIdStartWith(),
						terminalDetailsDTO.getNoOfPlatforms())) {

					long seqno = terminalManagementService.saveTerminalHeader(terminalDetailsDTO, user);

					String strTerminalIdStartChar = terminalDetailsDTO.getTerminalIdStartWith();
					int charValue = 0, terminalCode = 0;
					if (strTerminalIdStartChar.matches("[0-9]+")) {
						terminalCode = Integer.parseInt(strTerminalIdStartChar);
					} else {
						charValue = strTerminalIdStartChar.charAt(0);
					}

					for (int i = 0; i < terminalDetailsDTO.getNoOfTerminals(); i++) {
						if (terminalDetailsDTO.getTerminalDisplayType().equalsIgnoreCase("A")) {
							String character = String.valueOf((char) (charValue));

							createPlatforms(seqno, character);
							charValue++;
						} else {
							String terminalStringCode = createNumberPads(terminalCode);

							createPlatforms(seqno, terminalStringCode);
							terminalCode++;
						}
					}
					RequestContext.getCurrentInstance().update("frmCreateBusTerminal");

					terminalManagementService.beanLinkMethod(terminalDetailsDTO, sessionBackingBean.getLoginUser(), "Create Bus Terminal", " Manage Terminals");
					sucessMsg = "Successfully Saved.";
					RequestContext.getCurrentInstance().execute("PF('successSave').show()");

					clearfields();
					terminalDetailsDtoList = terminalManagementService
							.selectTerminalsByStation(terminalDetailsDTO.getStationCode());
				} else {

					errorMsg = "Platform Start value exceeded the alphabhetical length with given no of platforms";
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {

				errorMsg = "Terminals Start value exceeded the alphabhetical length with given no of terminals";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else if (terminalDetailsDtoList.size() > 0) {

			errorMsg = "Terminals are already created for the selected station.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (terminalDetailsDTO.getStationCode().isEmpty()) {

			errorMsg = "Station Code should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (terminalDetailsDTO.getNoOfTerminals() <= 0) {

			errorMsg = "No Of Terminals should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (terminalDetailsDTO.getTerminalIdStartWith().isEmpty()) {

			errorMsg = "Terminal Id start value should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (terminalDetailsDTO.getNoOfPlatforms() <= 0) {

			errorMsg = "No Of Platforms should be entered.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (terminalDetailsDTO.getPlatformIdStartWith().isEmpty()) {

			errorMsg = "Platform Id start value should be entered.";
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

	public void createPlatforms(long seqno, String terminal) {

		String createdBy = sessionBackingBean.loginUser;
		String strPlatformIdStartLetter = terminalDetailsDTO.getPlatformIdStartWith();

		int charValue = 0, platformCode = 0;
		if (strPlatformIdStartLetter.matches("[0-9]+")) {
			platformCode = Integer.parseInt(strPlatformIdStartLetter);
		} else {
			charValue = strPlatformIdStartLetter.charAt(0);
		}
		for (int i = 0; i < terminalDetailsDTO.getNoOfPlatforms(); i++) {
			if (terminalDetailsDTO.getPlatformDisplayType().equalsIgnoreCase("A")) {
				String platform = String.valueOf((char) (charValue));
				System.out.print(" " + terminal + platform);
				terminalManagementService.saveTerminalDetails(terminalDetailsDTO, seqno, terminal, platform, createdBy);
				charValue++;
			} else {
				System.out.print(" " + terminal + platformCode);
				terminalManagementService.saveTerminalDetails(terminalDetailsDTO, seqno, terminal,
						Integer.toString(platformCode), createdBy);
				platformCode++;
			}
		}
	}

	public void deleteBusTerminalAction() {

		RequestContext.getCurrentInstance().execute("PF('deleteconfirmationTerminal').show()");
	}

	public void removeAction() {

		int activeBlockCount = terminalManagementService.getActiveTerminalBlockCount(deleteTerminalDetailsDTO.getSeq());
		if (activeBlockCount > 0) {
			errorMsg = "Can not delete selected terminal. Terminal has Active blocks assigned !";
			RequestContext.getCurrentInstance().update("frmCreateBusTerminal:frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return;
		}

		boolean result = terminalManagementService.deleteSelectedBusTerminal(deleteTerminalDetailsDTO.getSeq());
		if (result) {
			RequestContext.getCurrentInstance().update("frmCreateBusTerminal");

			sucessMsg = "Successfully Deleted.";
			RequestContext.getCurrentInstance().execute("PF('successSave').show()");
			terminalDetailsDtoList.remove(deleteTerminalDetailsDTO);

		} else {
			errorMsg = "You can not delete this record.";
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
		}
	}

	public void onTerminalRadioChange() {
		if (terminalDetailsDTO.getTerminalDisplayType().equalsIgnoreCase("N")) {
			strTerminalStatusRegex = "/[\\d]/";
			terminalDetailsDTO.setTerminalIdStartWith("");

		} else {
			strTerminalStatusRegex = "/[a-z_]/i";
			terminalDetailsDTO.setTerminalIdStartWith("");

		}
	}

	public void onPlatformRadioChange() {
		if (terminalDetailsDTO.getPlatformDisplayType().equalsIgnoreCase("N")) {
			strPlatformStatusRegex = "/[\\d]/";
			terminalDetailsDTO.setPlatformIdStartWith("");

		} else {
			strPlatformStatusRegex = "/[a-z_]/i";
			terminalDetailsDTO.setPlatformIdStartWith("");

		}
	}

	public void onEditStatus(RowEditEvent event) {
		String modifiedBy = sessionBackingBean.loginUser;
		selectedTerminalDetailsId = ((TerminalDetailsDTO) event.getObject()).getSeq();
		selectedTerminalStatus = ((TerminalDetailsDTO) event.getObject()).getStatus();

		// update history table
		TerminalDetailsDTO terminalDetailsHistoryDTO = terminalManagementService
				.selectTerminalBySequence(selectedTerminalDetailsId);
		terminalManagementService.saveTerminalDetailsHistory(terminalDetailsHistoryDTO);
		// update status
		terminalManagementService.terminalDetailStatusUpdate(selectedTerminalDetailsId,
				selectedTerminalStatus.substring(0, 1), modifiedBy);
	}

	public void clearDetails() {
		clearfields();
		terminalDetailsDTO.setStationCode("");
		// clear grid
		terminalDetailsDtoList.clear();
		mainStationDetailsDTO.setDescription("");
	}

	private void clearfields() {
		terminalDetailsDTO.setNoOfTerminals(0);
		terminalDetailsDTO.setTerminalDisplayType("N");
		strTerminalStatusRegex = "/[\\d]/";
		terminalDetailsDTO.setTerminalIdStartWith("");
		terminalDetailsDTO.setNoOfPlatforms(0);
		terminalDetailsDTO.setPlatformDisplayType("A");
		strPlatformStatusRegex = "/[a-z_]/i";
		terminalDetailsDTO.setPlatformIdStartWith("");
		terminalDetailsDTO.setStatus("A");
	}

	public String createNumberPads(int number) {
		String padded = String.format("%02d", number);
		return padded;
	}

	public TerminalManagementService getTerminalManagementService() {
		return terminalManagementService;
	}

	public void setTerminalManagementService(TerminalManagementService terminalManagementService) {
		this.terminalManagementService = terminalManagementService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<MainStationDetailsDTO> getMainStationDetailsList() {
		return mainStationDetailsList;
	}

	public void setMainStationDetailsList(List<MainStationDetailsDTO> mainStationDetailsList) {
		this.mainStationDetailsList = mainStationDetailsList;
	}

	public MainStationDetailsDTO getMainStationDetailsDTO() {
		return mainStationDetailsDTO;
	}

	public void setMainStationDetailsDTO(MainStationDetailsDTO mainStationDetailsDTO) {
		this.mainStationDetailsDTO = mainStationDetailsDTO;
	}

	public String getStrTerminalStatusRegex() {
		return strTerminalStatusRegex;
	}

	public void setStrTerminalStatusRegex(String strTerminalStatusRegex) {
		this.strTerminalStatusRegex = strTerminalStatusRegex;
	}

	public String getStrPlatformStatusRegex() {
		return strPlatformStatusRegex;
	}

	public void setStrPlatformStatusRegex(String strPlatformStatusRegex) {
		this.strPlatformStatusRegex = strPlatformStatusRegex;
	}

	public TerminalDetailsDTO getTerminalDetailsDTO() {
		return terminalDetailsDTO;
	}

	public void setTerminalDetailsDTO(TerminalDetailsDTO terminalDetailsDTO) {
		this.terminalDetailsDTO = terminalDetailsDTO;
	}

	public List<TerminalDetailsDTO> getTerminalDetailsDtoList() {
		return terminalDetailsDtoList;
	}

	public void setTerminalDetailsDtoList(List<TerminalDetailsDTO> terminalDetailsDtoList) {
		this.terminalDetailsDtoList = terminalDetailsDtoList;
	}

	public Long getSelectedTerminalDetailsId() {
		return selectedTerminalDetailsId;
	}

	public void setSelectedTerminalDetailsId(Long selectedTerminalDetailsId) {
		this.selectedTerminalDetailsId = selectedTerminalDetailsId;
	}

	public TerminalDetailsDTO getDeleteTerminalDetailsDTO() {
		return deleteTerminalDetailsDTO;
	}

	public void setDeleteTerminalDetailsDTO(TerminalDetailsDTO deleteTerminalDetailsDTO) {
		this.deleteTerminalDetailsDTO = deleteTerminalDetailsDTO;
	}

	public String getSelectedTerminalStatus() {
		return selectedTerminalStatus;
	}

	public void setSelectedTerminalStatus(String selectedTerminalStatus) {
		this.selectedTerminalStatus = selectedTerminalStatus;
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

}
