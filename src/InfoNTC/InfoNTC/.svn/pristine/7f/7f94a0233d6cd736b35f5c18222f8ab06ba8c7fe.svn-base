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

@ManagedBean(name = "stationDetailsBackingBean")
@ViewScoped
public class StationDetailsBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private StationDetailsDTO stationDetailsDTO;
	private List<StationDetailsDTO> stationDetailsDTOList;
	private StationDetailsDTO selectedStationDTO;
	private boolean edit;
	private boolean exit;
	private StationDetailsService stationDetailsService;

	@PostConstruct
	public void init() {
		stationDetailsService = (StationDetailsService) SpringApplicationContex.getBean("stationDetailsService");

		stationDetailsDTO = new StationDetailsDTO();
		stationDetailsDTOList = new ArrayList<StationDetailsDTO>();
		edit = false;
		exit = false;
	}

	public void addValuesAction() {
		if (stationDetailsDTO.getStationNameEn() == null || stationDetailsDTO.getStationNameEn().isEmpty()
				|| stationDetailsDTO.getStationNameEn().trim().equalsIgnoreCase("")) {

			sessionBackingBean.setMessage("Please enter station name");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
			return;
		}

		boolean codeExist = stationDetailsService.checkDuplicateCode(stationDetailsDTO.getStationCode());
		if (codeExist) {

			sessionBackingBean.setMessage("Duplicate code");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonWarning').show()");
			return;
		}

		boolean duplicate = stationDetailsService.checkDuplicateData(stationDetailsDTO, true);
		if (duplicate) {

			RequestContext.getCurrentInstance().execute("PF('duplicateCode').show()");
			return;
		}

		if (edit) {
			stationDetailsDTOList.remove(selectedStationDTO);
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

		exit = true;
		stationDetailsDTO = new StationDetailsDTO();
		RequestContext.getCurrentInstance().update("stationform");
	}

	public void editButtonAction() {
		stationDetailsDTO = new StationDetailsDTO();
		stationDetailsDTO = selectedStationDTO;
		edit = true;
		RequestContext.getCurrentInstance().update("stationform");
	}

	public void clearRecordAction() {
		stationDetailsDTO = new StationDetailsDTO();
		RequestContext.getCurrentInstance().update("stationform");
	}

	public void saveAction() {
		stationDetailsService.saveAction(stationDetailsDTOList, sessionBackingBean.getLoginUser());
		exit = false;
		sessionBackingBean.setMessage("Data saved successfully");
		RequestContext.getCurrentInstance().execute("PF('dlgCommonSuccess').show()");
		clearFormAction();

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
		edit = false;
		exit = false;
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

}
