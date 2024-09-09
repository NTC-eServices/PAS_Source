package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import lk.informatics.ntc.model.dto.BlockRouteDetailDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.MainStationDetailsDTO;
import lk.informatics.ntc.model.dto.RouteCreationDTO;
import lk.informatics.ntc.model.dto.TerminalBlockDetailsDTO;
import lk.informatics.ntc.model.dto.TerminalDetailsDTO;
import lk.informatics.ntc.model.service.RouteCreatorService;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ViewScoped
@ManagedBean(name = "assignBusRoutesToBlockBackingBean")
public class AssignBusRoutesToBlockBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String sucessMsg;
	private String errorMsg;

	public List<MainStationDetailsDTO> mainStationDetailsList = new ArrayList<MainStationDetailsDTO>(0);
	public List<TerminalDetailsDTO> terminalDetailsDtoList = new ArrayList<TerminalDetailsDTO>(0);
	public List<TerminalBlockDetailsDTO> terminalBlockDetailsDtoList = new ArrayList<TerminalBlockDetailsDTO>(0);
	public List<BlockRouteDetailDTO> blockRoutesDtoList = new ArrayList<BlockRouteDetailDTO>();
	public List<BlockRouteDetailDTO> routeHistoryList = new ArrayList<BlockRouteDetailDTO>();
	private BlockRouteDetailDTO deletingBlockRoute = new BlockRouteDetailDTO();

	List<String> terminalList;
	List<CommonDTO> serviceTypeList;

	private TerminalManagementService terminalManagementService;
	private MainStationDetailsDTO mainStationDetailsDTO = new MainStationDetailsDTO();
	private TerminalBlockDetailsDTO terminalBlockDetailsDTO = new TerminalBlockDetailsDTO();
	private TerminalDetailsDTO selectedTerminal = new TerminalDetailsDTO();

	private RouteCreatorService routeCreatorService;
	List<RouteCreationDTO> routeList = new ArrayList<RouteCreationDTO>(0);

	private String[] selectedServiceTypes;
	private String selectedPlatform;
	private String selectedBlockName;
	private String blockSeq;
	private String routeId;
	private String newRouteNo;
	private String newServiceType;

	public AssignBusRoutesToBlockBackingBean() {
		terminalManagementService = (TerminalManagementService) SpringApplicationContex
				.getBean("terminalManagementService");
		mainStationDetailsList = terminalManagementService.selectAllMainStations();
		routeCreatorService = (RouteCreatorService) SpringApplicationContex.getBean("routeCreatorService");
		routeList = routeCreatorService.getAllRoutes();
		serviceTypeList = terminalManagementService.getServiceTypeToDropdown();
	}

	public void onMainStationChange() {
		terminalBlockDetailsDTO.setTerminal("");
		terminalBlockDetailsDTO.setTerminalDetailsId("");
		terminalDetailsDtoList.clear();
		terminalBlockDetailsDtoList.clear();
		selectedServiceTypes = null;
		routeId = null;
		blockRoutesDtoList.clear();

		String code = terminalBlockDetailsDTO.getStationCode();

		for (MainStationDetailsDTO mainStationDTO : mainStationDetailsList) {
			if (mainStationDTO.getCode().equalsIgnoreCase(code)) {
				
				mainStationDetailsDTO.setDescription(mainStationDTO.getDescription());
			} else if (code == null || code == "") {
				mainStationDetailsDTO.setDescription("");
			}
		}
		terminalList = terminalManagementService.selectDistinctTerminalsByStation(code);
	}

	public void onTerminalChange() {
		String terminal = terminalBlockDetailsDTO.getTerminal();
		String code = terminalBlockDetailsDTO.getStationCode();

		if (terminal == null || terminal == "") {
			terminalBlockDetailsDTO.setTerminalDetailsId("");
			terminalDetailsDtoList.clear();
		} else {
			terminalDetailsDtoList = terminalManagementService.selectTerminalDetailsByTerminal(terminal, code);

			for (TerminalDetailsDTO TerminalDetailsDTO : terminalDetailsDtoList) {
				List<TerminalBlockDetailsDTO> terminalBlockDetailsDtoList = terminalManagementService
						.selectTerminalBlocksBySequence(TerminalDetailsDTO.getSeq());
				if (!terminalBlockDetailsDtoList.isEmpty()) {

					TerminalDetailsDTO.setBlockStartValue(terminalBlockDetailsDtoList.get(0).getBlock());
					TerminalDetailsDTO.setBlockEndValue(
							terminalBlockDetailsDtoList.get(terminalBlockDetailsDtoList.size() - 1).getBlock());
				}
			}
		}
		terminalBlockDetailsDtoList.clear();
	}

	public void onPlatformChange() {
		String terminalDetailsID = terminalBlockDetailsDTO.getTerminalDetailsId();
		if (!terminalDetailsID.isEmpty()) {

			selectedTerminal = new TerminalDetailsDTO();
			selectedTerminal.setSeq(Long.parseLong(terminalDetailsID));
			viewAction();

			for (TerminalDetailsDTO platform : terminalDetailsDtoList) {
				if (Long.toString(platform.getSeq()).equals(terminalDetailsID)) {
					selectedPlatform = platform.getPlatform();
					break;
				}
			}
			retrieveBlockRoutes(terminalBlockDetailsDTO.getStationCode(), selectedPlatform);

		} else {
			blockRoutesDtoList = new ArrayList<BlockRouteDetailDTO>();
		}
	}

	public void onBlockChange() {
		if (blockSeq != null) {
			for (TerminalBlockDetailsDTO block : terminalBlockDetailsDtoList)
				if (Long.toString(block.getSeq()).equals(blockSeq))
					selectedBlockName = block.getBlock();
		} else {
			selectedBlockName = null;
		}
	}

	public void viewAction() {

		terminalBlockDetailsDtoList = terminalManagementService
				.selectTerminalBlocksBySequence(selectedTerminal.getSeq());

	}

	public void selectPlatform(SelectEvent event) {
		if (event != null) {
			selectedPlatform = ((TerminalDetailsDTO) event.getObject()).getPlatform();
			long terminalDetailsID = ((TerminalDetailsDTO) event.getObject()).getSeq();
			selectedTerminal = new TerminalDetailsDTO();
			selectedTerminal.setSeq(terminalDetailsID);

			blockSeq = null;
			viewAction();
			retrieveBlockRoutes(terminalBlockDetailsDTO.getStationCode(), selectedPlatform);
		}
	}

	public void saveAssignedBusRoutes() {


		Long headerSeq = null;
		

		if (terminalBlockDetailsDTO == null || terminalBlockDetailsDTO.getStationCode() == null
				|| terminalBlockDetailsDTO.getStationCode().trim().isEmpty()) {
			errorMsg = "Station Code should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return;
		}
		if (terminalBlockDetailsDTO == null || terminalBlockDetailsDTO.getTerminal() == null
				|| terminalBlockDetailsDTO.getTerminal().trim().isEmpty()) {
			errorMsg = "Terminal should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return;
		}
		if (selectedPlatform == null || selectedPlatform.trim().isEmpty()) {
			errorMsg = "Platform should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return;
		}
		if (blockSeq == null || blockSeq.trim().isEmpty()) {
			errorMsg = "Block should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return;
		}
		if (routeId == null || routeId.trim().isEmpty()) {
			errorMsg = "Route should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return;
		}
		if (selectedServiceTypes == null || selectedServiceTypes.length < 1) {
			errorMsg = "At least one Service Type should be selected.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			return;
		}

		// check whether route has a block assigned already
		for (String serv : selectedServiceTypes) {
			if (terminalManagementService.isBlockAssigned(routeId, serv, selectedTerminal.getStationCode())) {
				errorMsg = "A Block is already assigned for the selected Bus route & Service type.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				return;
			}
		}

		String createdByUser = sessionBackingBean.loginUser;

		if (headerSeq == null)
			headerSeq = terminalManagementService.saveAssignedBusRouteHeader(Long.valueOf(blockSeq),
					terminalBlockDetailsDTO.getStationCode(), selectedTerminal.getSeq(), selectedPlatform,
					selectedBlockName, createdByUser);

		if (headerSeq != -1 && headerSeq != 0) {
			for (String serv : selectedServiceTypes)
				terminalManagementService.saveAssignedBusRouteDetails(headerSeq, serv, routeId, createdByUser);

			retrieveBlockRoutes(terminalBlockDetailsDTO.getStationCode(), selectedPlatform);
			sucessMsg = "Saved Succesfully";
			RequestContext.getCurrentInstance().update("frmsuccessSave");
			RequestContext.getCurrentInstance().execute("PF('successSave').show()");
			terminalManagementService.beanLinkMethod(terminalBlockDetailsDTO, createdByUser, "Assign Bus Routes To Block", "Assign Bus Routes To Terminal Blocks");
			selectedBlockName = null;
			blockSeq = null;
			routeId = null;
			selectedServiceTypes = null;
			newRouteNo = null;
			newServiceType = null;
			// clearFields();
		} else {
			errorMsg = "Error saving data.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clearFields() {
		terminalBlockDetailsDTO = new TerminalBlockDetailsDTO();
		terminalDetailsDtoList = new ArrayList<TerminalDetailsDTO>();
		blockRoutesDtoList = new ArrayList<BlockRouteDetailDTO>();
		routeHistoryList = new ArrayList<BlockRouteDetailDTO>();
		mainStationDetailsDTO = new MainStationDetailsDTO();
		selectedPlatform = null;
		selectedBlockName = null;
		blockSeq = null;
		routeId = null;
		selectedServiceTypes = null;
		newRouteNo = null;
		newServiceType = null;
	}

	public void retrieveBlockRoutes(String stationCode, String platform) {
		blockRoutesDtoList = terminalManagementService.selectBlockRoutes(stationCode, platform);
		routeHistoryList = new ArrayList<BlockRouteDetailDTO>(blockRoutesDtoList); // to
																					// be
																					// used
																					// when
																					// modify
																					// data
		RequestContext.getCurrentInstance().update("frmAssignBusRoutesToBlock:blockRoutesData");
	}

	public void onEditAssignedRoute(RowEditEvent event) {
		String modifiedBy = sessionBackingBean.loginUser;
		BlockRouteDetailDTO modifiedRouteDTO = (BlockRouteDetailDTO) event.getObject();
		BlockRouteDetailDTO oldRouteDTO = new BlockRouteDetailDTO();

		// remove assigned routes
		if (modifiedRouteDTO.getRouteNo() == null || modifiedRouteDTO.getRouteNo().isEmpty()) {

			for (BlockRouteDetailDTO dto : routeHistoryList)
				if (dto.getDetailSeq() == modifiedRouteDTO.getDetailSeq()) {
					oldRouteDTO = dto;
					break;
				}

			if (terminalManagementService.removeAssignedBusRoute(oldRouteDTO, modifiedBy)) {
				sucessMsg = "Saved Successfully";
				RequestContext.getCurrentInstance().update("frmAssignBusRoutesToBlock:frmsuccessSave");
				RequestContext.getCurrentInstance().execute("PF('successSave').show()");
				retrieveBlockRoutes(terminalBlockDetailsDTO.getStationCode(), selectedPlatform);
			}
			return;
		}

		// check whether route has a block assigned already
		if (terminalManagementService.isBlockAssigned(modifiedRouteDTO.getRouteNo(),
				modifiedRouteDTO.getServiceTypeCode(), selectedTerminal.getStationCode())) {
			errorMsg = "A Block is already assigned for the selected Bus route & Service type.";
			RequestContext.getCurrentInstance().update("frmAssignBusRoutesToBlock:requiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			retrieveBlockRoutes(terminalBlockDetailsDTO.getStationCode(), selectedPlatform);
			return;
		}

		// update route/service
		for (BlockRouteDetailDTO dto : routeHistoryList)
			if (dto.getDetailSeq() == modifiedRouteDTO.getDetailSeq()) {
				oldRouteDTO = dto;
				break;
			}

		if (terminalManagementService.updateAssignedBusRouteDetails(oldRouteDTO, modifiedRouteDTO, modifiedBy)) {
			sucessMsg = "Saved Successfully";
			RequestContext.getCurrentInstance().update("frmAssignBusRoutesToBlock:frmsuccessSave");
			RequestContext.getCurrentInstance().execute("PF('successSave').show()");
			retrieveBlockRoutes(terminalBlockDetailsDTO.getStationCode(), selectedPlatform);
		}

	}

	public void removeAssignedRoute() {
		if (deletingBlockRoute == null)
			return;

		String modifiedBy = sessionBackingBean.loginUser;
		BlockRouteDetailDTO oldRouteDTO = new BlockRouteDetailDTO();

		if (deletingBlockRoute.getRouteNo() == null || deletingBlockRoute.getRouteNo().isEmpty()) {

			for (BlockRouteDetailDTO dto : routeHistoryList)
				if (dto.getDetailSeq() == deletingBlockRoute.getDetailSeq()) {
					oldRouteDTO = dto;
					break;
				}

			if (terminalManagementService.removeAssignedBusRoute(oldRouteDTO, modifiedBy)) {
				sucessMsg = "Saved Successfully";
				RequestContext.getCurrentInstance().update("frmAssignBusRoutesToBlock:frmsuccessSave");
				RequestContext.getCurrentInstance().execute("PF('successSave').show()");
				retrieveBlockRoutes(terminalBlockDetailsDTO.getStationCode(), selectedPlatform);
				terminalManagementService.beanLinkMethod(terminalBlockDetailsDTO, modifiedBy, "Delete Assign Bus Routes To Block", "Assign Bus Routes To Terminal Blocks");
			}
			return;
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
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

	public RouteCreatorService getRouteCreatorService() {
		return routeCreatorService;
	}

	public void setRouteCreatorService(RouteCreatorService routeCreatorService) {
		this.routeCreatorService = routeCreatorService;
	}

	public List<RouteCreationDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteCreationDTO> routeList) {
		this.routeList = routeList;
	}

	public String[] getSelectedServiceTypes() {
		return selectedServiceTypes;
	}

	public void setSelectedServiceTypes(String[] selectedServiceTypes) {
		this.selectedServiceTypes = selectedServiceTypes;
	}

	public List<CommonDTO> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<CommonDTO> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public List<BlockRouteDetailDTO> getBlockRoutesDtoList() {
		return blockRoutesDtoList;
	}

	public void setBlockRoutesDtoList(List<BlockRouteDetailDTO> blockRoutesDtoList) {
		this.blockRoutesDtoList = blockRoutesDtoList;
	}

	public String getSelectedPlatform() {
		return selectedPlatform;
	}

	public void setSelectedPlatform(String selectedPlatform) {
		this.selectedPlatform = selectedPlatform;
	}

	public String getSelectedBlockName() {
		return selectedBlockName;
	}

	public void setSelectedBlockName(String selectedBlockName) {
		this.selectedBlockName = selectedBlockName;
	}

	public String getBlockSeq() {
		return blockSeq;
	}

	public void setBlockSeq(String blockSeq) {
		this.blockSeq = blockSeq;
	}

	public String getNewRouteNo() {
		return newRouteNo;
	}

	public void setNewRouteNo(String newRouteNo) {
		this.newRouteNo = newRouteNo;
	}

	public String getNewServiceType() {
		return newServiceType;
	}

	public void setNewServiceType(String newServiceType) {
		this.newServiceType = newServiceType;
	}

	public BlockRouteDetailDTO getDeletingBlockRoute() {
		return deletingBlockRoute;
	}

	public void setDeletingBlockRoute(BlockRouteDetailDTO deletingBlockRoute) {
		this.deletingBlockRoute = deletingBlockRoute;
	}

}
