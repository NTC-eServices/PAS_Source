package lk.informatics.ntc.view.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.PrintReminderLetterDTO;
import lk.informatics.ntc.model.service.PrintLetterService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "printReminderLetterBackingBean")
@ViewScoped
public class PrintReminderLetterBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private String errorMessage, successMessage;
	// services
	private PrintLetterService printLetterService;

	// DTO
	private PrintReminderLetterDTO printReminderDTO;

	private List<PrintReminderLetterDTO> letterTypeList = new ArrayList<PrintReminderLetterDTO>();

	private List<PrintReminderLetterDTO> showSearchedData;
	private PrintReminderLetterDTO selectList;

	@PostConstruct
	public void init() {
		printLetterService = (PrintLetterService) SpringApplicationContex.getBean("printLetterService");
		letterTypeList = printLetterService.getLetterTypeDropDown();
		printReminderDTO = new PrintReminderLetterDTO();

	}

	public void searchAction() {

		Date start = printReminderDTO.getStartDate();
		Date end = printReminderDTO.getEndDate();
		Timestamp startDt = new Timestamp(start.getTime());
		Timestamp endDt = new Timestamp(end.getTime());

		if (printReminderDTO.getLetterTypeCode() != null && !printReminderDTO.getLetterTypeCode().trim().isEmpty()) {
			if (printReminderDTO.getStartDate() != null) {
				if (printReminderDTO.getEndDate() != null) {

					showSearchedData = printLetterService.getDataForSearch(printReminderDTO.getLetterTypeCode(),
							startDt, endDt);

					if (showSearchedData.size() == 0) {
						setErrorMessage("No data for searched values");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					}

				} else {
					setErrorMessage("Please select a end date");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
			} else {
				setErrorMessage("Please select a start date");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		} else {
			setErrorMessage("Please select a letter type");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void clearSearch()

	{

		printReminderDTO = new PrintReminderLetterDTO();
	}

	public void edit() {

	}

	public void printAction() {
		String loginUser = sessionBackingBean.getLoginUser();
		if (selectList == null) {
			setErrorMessage("Please  select a value");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		} else {

			// update 1st notice or 2nd notice status in reminder letter table
			if (printReminderDTO.getLetterTypeCode().equals("LTR01")) {
				printLetterService.updatePrinteStatus("Y", null, null, loginUser, selectList.getAppNo());
			} else if (printReminderDTO.getLetterTypeCode().equals("LTR02")) {
				printLetterService.updatePrinteStatus(null, "Y", null, loginUser, selectList.getAppNo());
			}

			else if (printReminderDTO.getLetterTypeCode().equals("LTR03")) {
				printLetterService.updatePrinteStatus(null, null, "Y", loginUser, selectList.getAppNo());
			}
		}
	}

	public void clearPage() {
		printReminderDTO = new PrintReminderLetterDTO();
		showSearchedData = new ArrayList<PrintReminderLetterDTO>();

	}

	public PrintReminderLetterDTO getPrintReminderDTO() {
		return printReminderDTO;
	}

	public void setPrintReminderDTO(PrintReminderLetterDTO printReminderDTO) {
		this.printReminderDTO = printReminderDTO;
	}

	public List<PrintReminderLetterDTO> getLetterTypeList() {
		return letterTypeList;
	}

	public void setLetterTypeList(List<PrintReminderLetterDTO> letterTypeList) {
		this.letterTypeList = letterTypeList;
	}

	public PrintLetterService getPrintLetterService() {
		return printLetterService;
	}

	public void setPrintLetterService(PrintLetterService printLetterService) {
		this.printLetterService = printLetterService;
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

	public List<PrintReminderLetterDTO> getShowSearchedData() {
		return showSearchedData;
	}

	public void setShowSearchedData(List<PrintReminderLetterDTO> showSearchedData) {
		this.showSearchedData = showSearchedData;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public PrintReminderLetterDTO getSelectList() {
		return selectList;
	}

	public void setSelectList(PrintReminderLetterDTO selectList) {
		this.selectList = selectList;
	}

}
