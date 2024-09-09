package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.model.service.TenderService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "printAgreementBackingBean")
@ViewScoped
public class PrintAgreementBackingBean implements Serializable {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private static final long serialVersionUID = 1L;

	private TenderDTO tenderDto;
	private List<TenderDTO> tenderApplicationNoList = new ArrayList<TenderDTO>();;
	private String selectTenderReferenceNo;
	private String selectTenderDecription, selectBidderName, selectNic, selectSerialNo, selectDeparture, selectArrival,
			selectRouteNo, selectTenderBankSlipReferenceNo;
	private String errorMessage, infoMessage;
	private String sucessMsg;
	private StreamedContent files;

	// Services
	private TenderService tenderService;

	@PostConstruct
	public void init() {
		tenderDto = new TenderDTO();
		tenderService = (TenderService) SpringApplicationContex.getBean("tenderService");
		tenderApplicationNoList = tenderService.getTenderApplicationNoList();
	}

	// When user use to select data item from drop-down, should get auto data
	// use
	// this (mainly for searching purpose/ other purposes
	// method, change the drop-down field in xhtml file
	public void onTenderNoChange() {
		if (tenderDto.getTenderAppNo() != null && !tenderDto.getTenderAppNo().trim().isEmpty()) {
			for (TenderDTO dto : tenderApplicationNoList) {
				if (tenderDto.getTenderAppNo().equals(dto.getTenderAppNo())) {
					tenderDto.setTenderRefNo(dto.getTenderRefNo());
					break;
				}
			}
		} else {
			tenderDto = new TenderDTO();
		}
	}

	@SuppressWarnings("deprecation")
	public void searchPrintData() {

		if (tenderDto.getTenderAppNo() != null && !tenderDto.getTenderAppNo().trim().isEmpty()) {

			tenderDto = tenderService.getPrintDetails(tenderDto.getTenderAppNo());
			if (tenderApplicationNoList.isEmpty()) {
				setInfoMessage("No records for selected Tender Application No. .");
				RequestContext.getCurrentInstance().update("infoMSG");
				RequestContext.getCurrentInstance().execute("PF('infoMessage').show()");
			}

		} else {
			setErrorMessage("Tender Application No. should be selected.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearPrintData() {
		tenderDto = new TenderDTO();
	}

	public void savePrintData() {

		if (tenderDto.getTenderAppNo() != null && !tenderDto.getTenderAppNo().trim().isEmpty()) {
			if (tenderDto.getTenderBankSlipRefNo() != null && !tenderDto.getTenderBankSlipRefNo().trim().isEmpty()) {
				Boolean updateSuccess = tenderService.updatePrintAgreementData(tenderDto,
						sessionBackingBean.getLoginUser());
				if (updateSuccess == true) {
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					setSucessMsg("Successfully Saved.");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
				} else {
					setErrorMessage("Update error.");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					RequestContext.getCurrentInstance().update("formOne");
				}

			} else {
				setErrorMessage("Please enter the Tender Bank Slip Reference No.");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				RequestContext.getCurrentInstance().update("formOne");
			}
		}

		else {
			setErrorMessage("Please enter Tender Application No.");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("formOne");

		}

	}

	public void cancelPrintData() {

		tenderDto = new TenderDTO();

	}

	public void printAgreement() {
		TenderDTO newValues = tenderService.checkedIsPrinted(tenderDto.getTenderAppNo(), tenderDto.getTenderRefNo(),
				sessionBackingBean.getLoginUser());
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public TenderDTO getTenderDto() {
		return tenderDto;
	}

	public void setTenderDto(TenderDTO tenderDto) {
		this.tenderDto = tenderDto;
	}

	public List<TenderDTO> getTenderApplicationNoList() {
		return tenderApplicationNoList;
	}

	public void setTenderApplicationNoList(List<TenderDTO> tenderApplicationNoList) {
		this.tenderApplicationNoList = tenderApplicationNoList;
	}

	public String getSelectTenderReferenceNo() {
		return selectTenderReferenceNo;
	}

	public void setSelectTenderReferenceNo(String selectTenderReferenceNo) {
		this.selectTenderReferenceNo = selectTenderReferenceNo;
	}

	public String getSelectTenderDecription() {
		return selectTenderDecription;
	}

	public void setSelectTenderDecription(String selectTenderDecription) {
		this.selectTenderDecription = selectTenderDecription;
	}

	public String getSelectBidderName() {
		return selectBidderName;
	}

	public void setSelectBidderName(String selectBidderName) {
		this.selectBidderName = selectBidderName;
	}

	public String getSelectNic() {
		return selectNic;
	}

	public void setSelectNic(String selectNic) {
		this.selectNic = selectNic;
	}

	public String getSelectSerialNo() {
		return selectSerialNo;
	}

	public void setSelectSerialNo(String selectSerialNo) {
		this.selectSerialNo = selectSerialNo;
	}

	public String getSelectDeparture() {
		return selectDeparture;
	}

	public void setSelectDeparture(String selectDeparture) {
		this.selectDeparture = selectDeparture;
	}

	public String getSelectArrival() {
		return selectArrival;
	}

	public void setSelectArrival(String selectArrival) {
		this.selectArrival = selectArrival;
	}

	public String getSelectRouteNo() {
		return selectRouteNo;
	}

	public void setSelectRouteNo(String selectRouteNo) {
		this.selectRouteNo = selectRouteNo;
	}

	public String getSelectTenderBankSlipReferenceNo() {
		return selectTenderBankSlipReferenceNo;
	}

	public void setSelectTenderBankSlipReferenceNo(String selectTenderBankSlipReferenceNo) {
		this.selectTenderBankSlipReferenceNo = selectTenderBankSlipReferenceNo;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSucessMsg() {
		return sucessMsg;
	}

	public void setSucessMsg(String sucessMsg) {
		this.sucessMsg = sucessMsg;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public TenderService getTenderService() {
		return tenderService;
	}

	public void setTenderService(TenderService tenderService) {
		this.tenderService = tenderService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
