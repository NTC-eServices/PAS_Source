package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.model.service.StationDetailsService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "stationDetailsViewBackingBean")
@ViewScoped
public class StationDetailsViewBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private StationDetailsDTO stationDetailsDTO;
	private List<StationDetailsDTO> stationDetailsDTOList;
	private StationDetailsDTO selectedStationDTO;
	private StationDetailsDTO tempSelectedStationDTO;
	private boolean edit;
	private boolean exit;
	private StationDetailsService stationDetailsService;
	private boolean disabled;
	private List<StationDetailsDTO> stationList;
	private String selectedStation;

	@PostConstruct
	public void init() {
		stationDetailsService = (StationDetailsService) SpringApplicationContex.getBean("stationDetailsService");

		stationDetailsDTO = new StationDetailsDTO();
		stationDetailsDTOList = new ArrayList<StationDetailsDTO>();
		edit = false;
		exit = false;
		disabled = false;
		selectedStation = null;
		stationList = new ArrayList<StationDetailsDTO>();
		stationList = stationDetailsService.selectAllStations();
		tempSelectedStationDTO = new StationDetailsDTO();
	}

	public void searchAction() {

		stationDetailsDTO.setStationNameEn(selectedStation);

		stationDetailsDTOList = stationDetailsService.searchStationData(stationDetailsDTO);

		if (stationDetailsDTOList == null || stationDetailsDTOList.size() == 0) {
			sessionBackingBean.setMessage("No records found for selected station");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
			return;
		}

		RequestContext.getCurrentInstance().update("stationform");
	}

	public void addValuesAction() {

		// selected data not updated
		if (edit) {
			if (selectedStationDTO.getStationNameEn() != null && !selectedStationDTO.getStationNameEn().trim().isEmpty()
					&& selectedStationDTO.getStationNameEn().trim().equals(tempSelectedStationDTO.getStationNameEn())
					&& selectedStationDTO.getStationNameSin() != null
					&& !selectedStationDTO.getStationNameSin().trim().isEmpty()
					&& selectedStationDTO.getStationNameSin().trim().equals(tempSelectedStationDTO.getStationNameSin())
					&& selectedStationDTO.getStationNameTam() != null
					&& !selectedStationDTO.getStationNameTam().trim().isEmpty()
					&& selectedStationDTO.getStationNameTam().trim().equals(tempSelectedStationDTO.getStationNameTam())
					&& selectedStationDTO.getStatus() != null && !selectedStationDTO.getStatus().trim().isEmpty()
					&& selectedStationDTO.getStatus().trim().equals(tempSelectedStationDTO.getStatus())) {

				sessionBackingBean.setMessage("Please update selected record");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
				return;
			}

			if (selectedStationDTO.getStationNameEn() == null || selectedStationDTO.getStationNameEn().trim().isEmpty()
					|| selectedStationDTO.getStationNameSin() == null
					|| selectedStationDTO.getStationNameSin().trim().isEmpty()
					|| selectedStationDTO.getStationNameTam() == null
					|| selectedStationDTO.getStationNameTam().trim().isEmpty() || selectedStationDTO.getStatus() == null
					|| selectedStationDTO.getStatus().trim().isEmpty()) {

				sessionBackingBean.setMessage("Station data cannot be empty");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
				return;
			}
		}

		// check duplicate station details
		boolean exist = stationDetailsService.checkDuplicateData(selectedStationDTO, false);

		if (exist) {
			if (stationDetailsDTOList != null && stationDetailsDTOList.size() == 1) {
				stationDetailsDTOList = new ArrayList<StationDetailsDTO>();
				stationDetailsDTOList.add(stationDetailsDTO);
			}

			if (selectedStationDTO.getStatus() != null && !selectedStationDTO.getStatus().isEmpty()
					&& !selectedStationDTO.getStatus().trim().equalsIgnoreCase("")) {
				if (selectedStationDTO.getStatus().equalsIgnoreCase("A")) {
					selectedStationDTO.setStatus("Active");
				}
				if (selectedStationDTO.getStatus().equalsIgnoreCase("I")) {
					selectedStationDTO.setStatus("Inactive");
				}
			}
			RequestContext.getCurrentInstance().execute("PF('duplicateCode').show()");
			return;
		}

		if (edit) {
			if (stationDetailsDTOList != null && stationDetailsDTOList.size() == 1) {
				stationDetailsDTOList = new ArrayList<StationDetailsDTO>();
				if (selectedStationDTO.getStatus() != null && !selectedStationDTO.getStatus().isEmpty()
						&& !selectedStationDTO.getStatus().trim().equalsIgnoreCase("")) {
					if (selectedStationDTO.getStatus().equalsIgnoreCase("A")) {
						selectedStationDTO.setStatus("Active");
					}
					if (selectedStationDTO.getStatus().equalsIgnoreCase("I")) {
						selectedStationDTO.setStatus("Inactive");
					}
				}
				stationDetailsDTOList.add(selectedStationDTO);
			} else {

				if (selectedStationDTO.getStatus() != null && !selectedStationDTO.getStatus().isEmpty()
						&& !selectedStationDTO.getStatus().trim().equalsIgnoreCase("")) {
					if (selectedStationDTO.getStatus().equalsIgnoreCase("A")) {
						selectedStationDTO.setStatus("Active");
					}
					if (selectedStationDTO.getStatus().equalsIgnoreCase("I")) {
						selectedStationDTO.setStatus("Inactive");
					}
				}
			}

			edit = false;
		} else {
			if (stationDetailsDTO.getStatus() != null && !stationDetailsDTO.getStatus().isEmpty()
					&& !stationDetailsDTO.getStatus().trim().equalsIgnoreCase("")) {
				if (stationDetailsDTO.getStatus().equalsIgnoreCase("A")) {
					stationDetailsDTO.setStatus("Active");
				}
				if (stationDetailsDTO.getStatus().equalsIgnoreCase("I")) {
					stationDetailsDTO.setStatus("Inactive");
				}
			}
			stationDetailsDTOList.add(stationDetailsDTO);
		}

		// update record
		stationDetailsService.updateStationDetailsRecord(selectedStationDTO);

		stationDetailsDTO = new StationDetailsDTO();
		RequestContext.getCurrentInstance().update("stationform");
		exit = false;
		selectedStationDTO = new StationDetailsDTO();
		sessionBackingBean.setMessage("Data saved successfully");
		RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");

	}

	public void editButtonAction() {

		stationDetailsDTO = new StationDetailsDTO();
		stationDetailsDTO.setStationCode(selectedStationDTO.getStationCode());
		stationDetailsDTO.setStationNameEn(selectedStationDTO.getStationNameEn());
		stationDetailsDTO.setStationNameSin(selectedStationDTO.getStationNameSin());
		stationDetailsDTO.setStationNameTam(selectedStationDTO.getStationNameTam());
		stationDetailsDTO.setStatus(selectedStationDTO.getStatus());

		tempSelectedStationDTO = new StationDetailsDTO();
		tempSelectedStationDTO.setStationCode(selectedStationDTO.getStationCode());
		tempSelectedStationDTO.setStationNameEn(selectedStationDTO.getStationNameEn());
		tempSelectedStationDTO.setStationNameSin(selectedStationDTO.getStationNameSin());
		tempSelectedStationDTO.setStationNameTam(selectedStationDTO.getStationNameTam());
		tempSelectedStationDTO.setStatus(selectedStationDTO.getStatus());

		edit = true;
		disabled = true;
		RequestContext.getCurrentInstance().update("stationform");
	}

	public void clearRecordAction() {
		stationDetailsDTO = new StationDetailsDTO();
		RequestContext.getCurrentInstance().update("stationform");
	}

	public void exitAction() {

		try {

			if (exit) {
				sessionBackingBean.setMessage("Please save data before exit");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
				return;
			}

			FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/pages/home/welcomePage.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clearFormAction() {
		stationDetailsDTO = new StationDetailsDTO();
		stationDetailsDTOList = new ArrayList<StationDetailsDTO>();
		selectedStationDTO = new StationDetailsDTO();
		edit = false;
		exit = false;
		disabled = false;
		stationList = new ArrayList<StationDetailsDTO>();
		stationList = stationDetailsService.selectAllStations();
		selectedStation = null;
		tempSelectedStationDTO = new StationDetailsDTO();
		RequestContext.getCurrentInstance().update("stationform");
	}

	public StationDetailsDTO getStationDetailsDTO() {
		return stationDetailsDTO;
	}

	public void setStationDetailsDTO(StationDetailsDTO stationDetailsDTO) {
		this.stationDetailsDTO = stationDetailsDTO;
	}

	public List<StationDetailsDTO> getStationDetailsDTOList() {
		return stationDetailsDTOList;
	}

	public void setStationDetailsDTOList(List<StationDetailsDTO> stationDetailsDTOList) {
		this.stationDetailsDTOList = stationDetailsDTOList;
	}

	public StationDetailsDTO getSelectedStationDTO() {
		return selectedStationDTO;
	}

	public void setSelectedStationDTO(StationDetailsDTO selectedStationDTO) {
		this.selectedStationDTO = selectedStationDTO;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public StationDetailsService getStationDetailsService() {
		return stationDetailsService;
	}

	public void setStationDetailsService(StationDetailsService stationDetailsService) {
		this.stationDetailsService = stationDetailsService;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public boolean isExit() {
		return exit;
	}

	public void setExit(boolean exit) {
		this.exit = exit;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public List<StationDetailsDTO> getStationList() {
		return stationList;
	}

	public void setStationList(List<StationDetailsDTO> stationList) {
		this.stationList = stationList;
	}

	public String getSelectedStation() {
		return selectedStation;
	}

	public void setSelectedStation(String selectedStation) {
		this.selectedStation = selectedStation;
	}

	public StationDetailsDTO getTempSelectedStationDTO() {
		return tempSelectedStationDTO;
	}

	public void setTempSelectedStationDTO(StationDetailsDTO tempSelectedStationDTO) {
		this.tempSelectedStationDTO = tempSelectedStationDTO;
	}

}
