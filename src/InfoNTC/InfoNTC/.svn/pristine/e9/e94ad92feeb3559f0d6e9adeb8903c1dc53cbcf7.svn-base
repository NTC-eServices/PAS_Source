package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.TrafficProposalDTO;

@ManagedBean(name = "generateTrafficProposalSisuSeriyaBackingBean")
@ViewScoped
public class GenerateTrafficProposalSisuSeriyaBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// DTO
	private TrafficProposalDTO trafficProposalDTO = new TrafficProposalDTO();
	private TrafficProposalDTO selectDTO;
	private TrafficProposalDTO selectedRow;
	private TrafficProposalDTO selectedDeleteRow;

	// List
	private List<TrafficProposalDTO> requestNoList = new ArrayList<TrafficProposalDTO>();
	private List<TrafficProposalDTO> dataList = new ArrayList<TrafficProposalDTO>();

	private String selectedRequestNo;
	private boolean showDetailsPanels = false;
	private boolean disabledEditBtn = false;
	private boolean readOnlyInputRemarks = false;
	private boolean disabledPrintTrafBtn = true;
	private boolean disabledRePrintTrafBtn = true;
	private boolean disabledWithSelectedRow = true;

	public GenerateTrafficProposalSisuSeriyaBackingBean() {

	}

	@PostConstruct
	public void init() {

	}

	public void onRequestNoChange() {

	}

	public void searchAct() {
		setShowDetailsPanels(true);
		setReadOnlyInputRemarks(false);
	}

	public void clearFields() {
		setShowDetailsPanels(false);
		setReadOnlyInputRemarks(false);
	}

	public void selectRow(SelectEvent event) {

	}

	public void onNoOfPermitsReqIssueChange() {

	}

	public void editAction() {

	}

	public void deleteAction() {

	}

	public void updateRecord() {

	}

	public void clearForm() {

	}

	public void printTrafficProAct() {

	}

	public void rePrintTrafficProAct() {

	}

	public void uploadDocPopUp() {

	}

	public void viewTimeTableAct() {

	}

	public void viewFareTableAct() {

	}

	public void generateTenderReq() {

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public TrafficProposalDTO getTrafficProposalDTO() {
		return trafficProposalDTO;
	}

	public void setTrafficProposalDTO(TrafficProposalDTO trafficProposalDTO) {
		this.trafficProposalDTO = trafficProposalDTO;
	}

	public List<TrafficProposalDTO> getRequestNoList() {
		return requestNoList;
	}

	public void setRequestNoList(List<TrafficProposalDTO> requestNoList) {
		this.requestNoList = requestNoList;
	}

	public String getSelectedRequestNo() {
		return selectedRequestNo;
	}

	public void setSelectedRequestNo(String selectedRequestNo) {
		this.selectedRequestNo = selectedRequestNo;
	}

	public boolean isShowDetailsPanels() {
		return showDetailsPanels;
	}

	public void setShowDetailsPanels(boolean showDetailsPanels) {
		this.showDetailsPanels = showDetailsPanels;
	}

	public List<TrafficProposalDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<TrafficProposalDTO> dataList) {
		this.dataList = dataList;
	}

	public TrafficProposalDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(TrafficProposalDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public TrafficProposalDTO getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(TrafficProposalDTO selectedRow) {
		this.selectedRow = selectedRow;
	}

	public boolean isDisabledEditBtn() {
		return disabledEditBtn;
	}

	public void setDisabledEditBtn(boolean disabledEditBtn) {
		this.disabledEditBtn = disabledEditBtn;
	}

	public TrafficProposalDTO getSelectedDeleteRow() {
		return selectedDeleteRow;
	}

	public void setSelectedDeleteRow(TrafficProposalDTO selectedDeleteRow) {
		this.selectedDeleteRow = selectedDeleteRow;
	}

	public boolean isReadOnlyInputRemarks() {
		return readOnlyInputRemarks;
	}

	public void setReadOnlyInputRemarks(boolean readOnlyInputRemarks) {
		this.readOnlyInputRemarks = readOnlyInputRemarks;
	}

	public boolean isDisabledPrintTrafBtn() {
		return disabledPrintTrafBtn;
	}

	public void setDisabledPrintTrafBtn(boolean disabledPrintTrafBtn) {
		this.disabledPrintTrafBtn = disabledPrintTrafBtn;
	}

	public boolean isDisabledRePrintTrafBtn() {
		return disabledRePrintTrafBtn;
	}

	public void setDisabledRePrintTrafBtn(boolean disabledRePrintTrafBtn) {
		this.disabledRePrintTrafBtn = disabledRePrintTrafBtn;
	}

	public boolean isDisabledWithSelectedRow() {
		return disabledWithSelectedRow;
	}

	public void setDisabledWithSelectedRow(boolean disabledWithSelectedRow) {
		this.disabledWithSelectedRow = disabledWithSelectedRow;
	}

}
