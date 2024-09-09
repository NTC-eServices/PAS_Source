package lk.informatics.ntc.view.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "editViewOtherInspection")
@ViewScoped
public class otherInspectionEditViewBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private VehicleInspectionDTO vehicleDTO;
	private VehicleInspectionDTO selectedViewRow;
	private VehicleInspectionDTO selectedEditRow;
	private VehicleInspectionService vehicleInspectionService;

	private List<VehicleInspectionDTO> applicationNoList;
	private List<VehicleInspectionDTO> vehcileNoList;
	public List<VehicleInspectionDTO> dataList;
	private String successMessage, errorMessage;

	@PostConstruct
	public void init() {

		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		vehicleDTO = new VehicleInspectionDTO();
		applicationNoList = vehicleInspectionService.getApplicationNoList();
		vehcileNoList = vehicleInspectionService.getVehicleNoList();
		dataList = vehicleInspectionService.getOtherInspectionRecords(vehicleDTO, true);

		FacesContext fcontext = FacesContext.getCurrentInstance();
		fcontext.getExternalContext().getSessionMap().remove("VIEW_OTHER");
		fcontext.getExternalContext().getSessionMap().remove("EDIT_OTHER");
		fcontext.getExternalContext().getSessionMap().remove("UPLOAD_VIEW");
		fcontext.getExternalContext().getSessionMap().remove("VEHICLE_INSPECTION");

	}

	public void search() {

		if ((vehicleDTO.getApplicationNo() != null && !vehicleDTO.getApplicationNo().trim().equalsIgnoreCase(""))
				|| (vehicleDTO.getVehicleNo() != null && !vehicleDTO.getVehicleNo().trim().equalsIgnoreCase(""))) {

			dataList = vehicleInspectionService.getOtherInspectionRecords(vehicleDTO, false);

		} else {

			setErrorMessage("Please select application no. or vehicle no.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void edit() {

		try {
			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("EDIT_OTHER", selectedEditRow);

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/vehicleInspectionSet/viewOtherInspection.xhtml");

		} catch (Exception e) {
			setErrorMessage("Error occurred. Could not edit.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			e.printStackTrace();
		}

	}

	public void view() {

		try {
			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("VIEW_OTHER", selectedViewRow);

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/vehicleInspectionSet/viewOtherInspection.xhtml");

		} catch (Exception e) {
			setErrorMessage("Error occurred. Could not view.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			e.printStackTrace();
		}

	}

	public void clear() {
		vehicleDTO = new VehicleInspectionDTO();
		dataList = vehicleInspectionService.getOtherInspectionRecords(vehicleDTO, true);
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public VehicleInspectionDTO getVehicleDTO() {
		return vehicleDTO;
	}

	public void setVehicleDTO(VehicleInspectionDTO vehicleDTO) {
		this.vehicleDTO = vehicleDTO;
	}

	public VehicleInspectionDTO getSelectedViewRow() {
		return selectedViewRow;
	}

	public void setSelectedViewRow(VehicleInspectionDTO selectedViewRow) {
		this.selectedViewRow = selectedViewRow;
	}

	public VehicleInspectionDTO getSelectedEditRow() {
		return selectedEditRow;
	}

	public void setSelectedEditRow(VehicleInspectionDTO selectedEditRow) {
		this.selectedEditRow = selectedEditRow;
	}

	public List<VehicleInspectionDTO> getApplicationNoList() {
		return applicationNoList;
	}

	public void setApplicationNoList(List<VehicleInspectionDTO> applicationNoList) {
		this.applicationNoList = applicationNoList;
	}

	public List<VehicleInspectionDTO> getVehcileNoList() {
		return vehcileNoList;
	}

	public void setVehcileNoList(List<VehicleInspectionDTO> vehcileNoList) {
		this.vehcileNoList = vehcileNoList;
	}

	public List<VehicleInspectionDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<VehicleInspectionDTO> dataList) {
		this.dataList = dataList;
	}

	public VehicleInspectionService getVehicleInspectionService() {
		return vehicleInspectionService;
	}

	public void setVehicleInspectionService(VehicleInspectionService vehicleInspectionService) {
		this.vehicleInspectionService = vehicleInspectionService;
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

}
