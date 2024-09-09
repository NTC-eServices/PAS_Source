package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import lk.informatics.ntc.model.dto.BusAssignOnRoutesDTO;
import lk.informatics.ntc.model.service.BusAssignOnRoutesService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "busAssignOnRoutesBackingBean")
@ViewScoped
public class BusAssignOnRoutesBackingBean {
	
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
	private BusAssignOnRoutesService busAssignOnRoutesService;
	private List<String> routeList = new ArrayList<String>();
	private List<String> busList = new ArrayList<String>();
	private List<BusAssignOnRoutesDTO> busCategoryList = new ArrayList<BusAssignOnRoutesDTO>();
	private String user;
	private String errorMsg;
	private String sucessMsg;
	private List<BusAssignOnRoutesDTO> dataTableList = new ArrayList<BusAssignOnRoutesDTO>();
	private BusAssignOnRoutesDTO busDTO;
	private BusAssignOnRoutesDTO tempDTO;

	@PostConstruct
	public void init() {
		busAssignOnRoutesService = (BusAssignOnRoutesService) SpringApplicationContex
				.getBean("busAssignOnRoutesService");
		busDTO = new BusAssignOnRoutesDTO();
		user = sessionBackingBean.getLoginUser();
		routeList = busAssignOnRoutesService.routeNoDropDown();

		dataTableList = busAssignOnRoutesService.table();
	}

	public void onRouteChange() {
		busList = busAssignOnRoutesService.busNoDropDown(busDTO.getRouteNo());
	}

	public void onBusChange() {
		setTempDTO(new BusAssignOnRoutesDTO());
		tempDTO = busAssignOnRoutesService.originDestination(busDTO.getBusNo(), busDTO.getRouteNo());
		busDTO.setOrigin(tempDTO.getOrigin());
		busDTO.setDestination(tempDTO.getDestination());
		tempDTO = busAssignOnRoutesService.busCategoryDropDown(busDTO);
		busDTO.setBusCategory(tempDTO.getBusCategory());
	}

	public void add() {
		boolean check = false;
		check = busAssignOnRoutesService.addCheck(busDTO);
		if (busDTO.getRouteNo() == null || busDTO.getRouteNo().isEmpty() || busDTO.getRouteNo().equals("")) {
			errorMsg = "Please Select A Route No.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		else if (busDTO.getStatus() == null || busDTO.getStatus().isEmpty() || busDTO.getStatus().equals("")) {
			errorMsg = "Please Select A Status.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (check == true) {
			errorMsg = "Data for bus No. already exists";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}

		else {
			setSucessMsg("Data Added successfully");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			busAssignOnRoutesService.add(busDTO, user);
			dataTableList = busAssignOnRoutesService.table();
			clear();
		}
	}

	public void onRowEdit(RowEditEvent event) {
		String val1 = ((BusAssignOnRoutesDTO) event.getObject()).getBusNo();
		String val2 = ((BusAssignOnRoutesDTO) event.getObject()).getStatus();
		boolean done = busAssignOnRoutesService.editTable(sessionBackingBean.getLoginUser(), val1, val2);
		if (done == true) {
			setSucessMsg("Status Edited Successfully");
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
		}
		dataTableList = busAssignOnRoutesService.table();

	}

	public void clear() {
		init();
	}

	public List<String> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<String> routeList) {
		this.routeList = routeList;
	}

	public BusAssignOnRoutesDTO getBusDTO() {
		return busDTO;
	}

	public void setBusDTO(BusAssignOnRoutesDTO busDTO) {
		this.busDTO = busDTO;
	}

	public List<BusAssignOnRoutesDTO> getDataTableList() {
		return dataTableList;
	}

	public void setDataTableList(List<BusAssignOnRoutesDTO> dataTableList) {
		this.dataTableList = dataTableList;
	}

	public BusAssignOnRoutesService getBusAssignOnRoutesService() {
		return busAssignOnRoutesService;
	}

	public void setBusAssignOnRoutesService(BusAssignOnRoutesService busAssignOnRoutesService) {
		this.busAssignOnRoutesService = busAssignOnRoutesService;
	}

	public BusAssignOnRoutesDTO getTempDTO() {
		return tempDTO;
	}

	public void setTempDTO(BusAssignOnRoutesDTO tempDTO) {
		this.tempDTO = tempDTO;
	}

	public List<String> getBusList() {
		return busList;
	}

	public void setBusList(List<String> busList) {
		this.busList = busList;
	}

	public List<BusAssignOnRoutesDTO> getBusCategoryList() {
		return busCategoryList;
	}

	public void setBusCategoryList(List<BusAssignOnRoutesDTO> busCategoryList) {
		this.busCategoryList = busCategoryList;
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

}
