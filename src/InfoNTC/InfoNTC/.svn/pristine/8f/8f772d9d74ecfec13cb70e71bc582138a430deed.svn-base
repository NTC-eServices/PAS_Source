package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.RouteDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "routeBackingBean")
@ViewScoped

public class RouteBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// Services
	private AdminService adminService;

	// DTOs
	private List<RouteDTO> routeList = new ArrayList<RouteDTO>();
	private RouteDTO routeDTO;
	private RouteDTO strSelectedRouteSeq;
	private List<CommonDTO> statusList;

	// SelectedValues
	private String strSelectedStatus;
	private Boolean createMode;
	private Boolean editMode;
	private Boolean viewMode;
	private Boolean disable;
	private String errorMsg;
	private Boolean boolEdit;

	@PostConstruct
	public void init() {
		routeDTO = new RouteDTO();
		setEditMode(false);
		setCreateMode(true);
		setViewMode(false);
		setDisable(false);
		disable = false;
		boolEdit = false;
		LoadValues();
	}

	// Methods
	public void LoadValues() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		setRouteList(adminService.getRouteDetailsbyLoginUser(sessionBackingBean.loginUser));
	}

	public void addRoute() {
		routeDTO.setStatus(strSelectedStatus);
		if (routeDTO.getRouteNo() != null && !routeDTO.getRouteNo().isEmpty()
				&& !routeDTO.getRouteNo().equalsIgnoreCase("")) {
			if (routeDTO.getOrigin() != null && !routeDTO.getOrigin().isEmpty()
					&& !routeDTO.getOrigin().equalsIgnoreCase("")) {
				if (routeDTO.getDestination() != null && !routeDTO.getDestination().isEmpty()
						&& !routeDTO.getDestination().equalsIgnoreCase("")) {
					if (routeDTO.getVia() != null && !routeDTO.getVia().isEmpty()
							&& !routeDTO.getVia().equalsIgnoreCase("")) {
						if (routeDTO.getOriginS() != null && !routeDTO.getOriginS().isEmpty()
								&& !routeDTO.getOriginS().equalsIgnoreCase("")) {
							if (routeDTO.getDestinationS() != null && !routeDTO.getDestinationS().isEmpty()
									&& !routeDTO.getDestinationS().equalsIgnoreCase("")) {
								if (routeDTO.getViaS() != null && !routeDTO.getViaS().isEmpty()
										&& !routeDTO.getViaS().equalsIgnoreCase("")) {
									if (routeDTO.getOriginT() != null && !routeDTO.getOriginT().isEmpty()
											&& !routeDTO.getOriginT().equalsIgnoreCase("")) {
										if (routeDTO.getDestinationT() != null && !routeDTO.getDestinationT().isEmpty()
												&& !routeDTO.getDestinationT().equalsIgnoreCase("")) {
											if (routeDTO.getViaT() != null && !routeDTO.getViaT().isEmpty()
													&& !routeDTO.getViaT().equalsIgnoreCase("")) {

									if (routeDTO.getStatus() != null && !routeDTO.getStatus().isEmpty()
											&& !routeDTO.getStatus().equalsIgnoreCase("")) {
										if (boolEdit == true) {
											// Update

											routeDTO.setStatus(strSelectedStatus);
											routeDTO.setModifiedBy(sessionBackingBean.loginUser);
											routeDTO.setSeq(strSelectedRouteSeq.getSeq());

											int result = adminService.updateRouteDetails(routeDTO);
											if (result == 0) {
												RequestContext.getCurrentInstance().execute("PF('successSve').show()");
												clearRoute();
												LoadValues();
											} else {
												RequestContext.getCurrentInstance()
														.execute("PF('generalError').show()");
											}
										} else {
											// insert
											String strResult = adminService.chkDuplicates(routeDTO.getRouteNo());
											if (strResult != null) {
												// Duplicate Record.
												errorMsg = "Duplicate data.";
												RequestContext.getCurrentInstance().update("frmerrorMsge");
												RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
											} else {

												routeDTO.setStatus(strSelectedStatus);
												routeDTO.setCreatedBy(sessionBackingBean.loginUser);

												int result = adminService.saveRoute(routeDTO);
												if (result == 0) {
													RequestContext.getCurrentInstance()
															.execute("PF('successSve').show()");
													clearRoute();
													LoadValues();

												} else {
													RequestContext.getCurrentInstance()
															.execute("PF('generalError').show()");

												}
											}

										}

									} else {
										setErrorMsg("Status should be selected.");
										RequestContext.getCurrentInstance().update("frmrequiredField");
										RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
									}
								
								} else {
									setErrorMsg("Via in Tamil should be entered.");
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								setErrorMsg("Location 2 in Tamil should be entered.");
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							setErrorMsg("Location 1 in Tamil should be entered.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}
								
								} else {
									setErrorMsg("Via in Sinhala should be entered.");
									RequestContext.getCurrentInstance().update("frmrequiredField");
									RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
								}

							} else {
								setErrorMsg("Location 2 in Sinhala should be entered.");
								RequestContext.getCurrentInstance().update("frmrequiredField");
								RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
							}

						} else {
							setErrorMsg("Location 1 in Sinhala should be entered.");
							RequestContext.getCurrentInstance().update("frmrequiredField");
							RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
						}

					} else {
						setErrorMsg("Via in English should be entered.");
						RequestContext.getCurrentInstance().update("frmrequiredField");
						RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
					}
				} else {
					setErrorMsg("Location 2 in English should be entered.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
					RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
				}
			} else {
				setErrorMsg("Location 1 in English should be entered.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			setErrorMsg("Route No. should be entered.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clearRoute() {
		strSelectedStatus = null;
		routeList = new ArrayList<RouteDTO>();
		strSelectedRouteSeq = new RouteDTO();
		init();
	}

	public void editAction() {

		routeDTO.setRouteNo(strSelectedRouteSeq.getRouteNo());
		routeDTO.setRouteDes(strSelectedRouteSeq.getRouteDes());
		routeDTO.setDestination(strSelectedRouteSeq.getDestination());
		routeDTO.setOrigin(strSelectedRouteSeq.getOrigin());
		routeDTO.setVia(strSelectedRouteSeq.getVia());
		routeDTO.setDistance(strSelectedRouteSeq.getDistance());
		routeDTO.setStatus(strSelectedRouteSeq.getStatusCode());
		routeDTO.setBusFare(strSelectedRouteSeq.getBusFare());
		strSelectedStatus = routeDTO.getStatus();
		
		routeDTO.setDestinationS(strSelectedRouteSeq.getDestinationS());
		routeDTO.setOriginS(strSelectedRouteSeq.getOriginS());
		routeDTO.setViaS(strSelectedRouteSeq.getViaS());
		routeDTO.setDestinationT(strSelectedRouteSeq.getDestinationT());
		routeDTO.setOriginT(strSelectedRouteSeq.getOriginT());
		routeDTO.setViaT(strSelectedRouteSeq.getViaT());
		
		disable = true;
		boolEdit = true;
	}

	public void totalBusFareValidation() {

		if (routeDTO.getBusFare() != null && routeDTO.getBusFare().signum() < 0) {
			errorMsg = "Invalid Total Bus Fare";
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			routeDTO.setBusFare(null);
		}

	}

	public String getStrSelectedStatus() {
		return strSelectedStatus;
	}

	public void setStrSelectedStatus(String strSelectedStatus) {
		this.strSelectedStatus = strSelectedStatus;
	}

	public Boolean getCreateMode() {
		return createMode;
	}

	public void setCreateMode(Boolean createMode) {
		this.createMode = createMode;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public Boolean getViewMode() {
		return viewMode;
	}

	public void setViewMode(Boolean viewMode) {
		this.viewMode = viewMode;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public List<RouteDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteDTO> routeList) {
		this.routeList = routeList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public RouteDTO getStrSelectedRouteSeq() {
		return strSelectedRouteSeq;
	}

	public void setStrSelectedRouteSeq(RouteDTO strSelectedRouteSeq) {
		this.strSelectedRouteSeq = strSelectedRouteSeq;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<CommonDTO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<CommonDTO> statusList) {
		this.statusList = statusList;
	}

	public Boolean getBoolEdit() {
		return boolEdit;
	}

	public void setBoolEdit(Boolean boolEdit) {
		this.boolEdit = boolEdit;
	}

}
