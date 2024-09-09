package lk.informatics.ntc.view.beans;

import java.math.BigDecimal;
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

@ManagedBean(name = "nisiViewrouteBackingBean")
@ViewScoped

public class NisiViewRouteBackingBean {
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
	private Boolean disableMode;
	private String reqRouteNo;
	private Boolean boolRendered;
	private String ErrorMsg;
	private String routeDesBeforeEditing;
	private String originBeforeEditing;
	private String destinationBeforeEditing;
	private String viaBeforeEditing;
	private BigDecimal distanceBeforeEditing;
	private String statusBeforeEditing;
	private BigDecimal busFareBeforeEditing;

	public BigDecimal getBusFareBeforeEditing() {
		return busFareBeforeEditing;
	}

	public void setBusFareBeforeEditing(BigDecimal busFareBeforeEditing) {
		this.busFareBeforeEditing = busFareBeforeEditing;
	}

	@PostConstruct
	public void init() {
		setRouteDTO(new RouteDTO());
		disableMode = false;
		boolRendered = false;

		LoadValues();
		String result = adminService.getUserActivity(sessionBackingBean.loginUser, "FN7_2");
		if (result != null) {
			disableMode = false;
		} else {
			disableMode = true;
		}
	}

	// Methods
	public void LoadValues() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		setRouteList(adminService.getNisiRouteDetails());
	}

	public void searchRoute() {
		routeList = adminService.getNisiRouteDetailsByRouteNo(reqRouteNo);
	}

	public void clearRoute() {
		init();
		setReqRouteNo(null);
		boolRendered = false;
		routeDTO = new RouteDTO();
	}

	public void editAction() {

		boolRendered = true;
		routeDTO.setRouteNo(strSelectedRouteSeq.getRouteNo());
		routeDTO.setRouteDes(strSelectedRouteSeq.getRouteDes());
		routeDTO.setDestination(strSelectedRouteSeq.getDestination());
		routeDTO.setOrigin(strSelectedRouteSeq.getOrigin());
		routeDTO.setVia(strSelectedRouteSeq.getVia());
		routeDTO.setDistance(strSelectedRouteSeq.getDistance());
		routeDTO.setStatus(strSelectedRouteSeq.getStatusCode());
		routeDTO.setBusFare(strSelectedRouteSeq.getBusFare());

		routeDTO.setDestinationS(strSelectedRouteSeq.getDestinationS());
		routeDTO.setOriginS(strSelectedRouteSeq.getOriginS());
		routeDTO.setViaS(strSelectedRouteSeq.getViaS());
		routeDTO.setDestinationT(strSelectedRouteSeq.getDestinationT());
		routeDTO.setOriginT(strSelectedRouteSeq.getOriginT());
		routeDTO.setViaT(strSelectedRouteSeq.getViaT());

		strSelectedStatus = routeDTO.getStatus();
		routeDesBeforeEditing = strSelectedRouteSeq.getRouteDes();
		originBeforeEditing = strSelectedRouteSeq.getOrigin();
		destinationBeforeEditing = strSelectedRouteSeq.getDestination();
		viaBeforeEditing = strSelectedRouteSeq.getVia();
		distanceBeforeEditing = strSelectedRouteSeq.getDistance();

		statusBeforeEditing = strSelectedRouteSeq.getStatusCode();
		busFareBeforeEditing = strSelectedRouteSeq.getBusFare();

		RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
	}

	public void updateRoute() {
		// Update

		routeDTO.setStatus(strSelectedStatus);
		routeDTO.setModifiedBy(sessionBackingBean.loginUser);
		routeDTO.setSeq(strSelectedRouteSeq.getSeq());

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
												if (routeDTO.getDistance() != null) {

													if (!(originBeforeEditing.equals(routeDTO.getOrigin())
															&& destinationBeforeEditing
																	.equals(routeDTO.getDestination())
															&& viaBeforeEditing.equals(routeDTO.getVia())
															&& statusBeforeEditing.equalsIgnoreCase(strSelectedStatus)
															&& distanceBeforeEditing
																	.equals(routeDTO.getDestination()))) {
														int result = adminService.updateRouteDetails(routeDTO);
														if (result == 0) {
															RequestContext.getCurrentInstance()
																	.execute("PF('successSve').show()");
															clearRoute();
															LoadValues();
														} else {
															RequestContext.getCurrentInstance()
																	.execute("PF('generalError').show()");
														}
													} else {

														ErrorMsg = "You should be updated.";
														RequestContext.getCurrentInstance().update("frmrequiredField");
														RequestContext.getCurrentInstance()
																.execute("PF('requiredField').show()");
													}
												}

											} else {
												setErrorMsg("Status should be selected.");
												RequestContext.getCurrentInstance().update("frmrequiredField");
												RequestContext.getCurrentInstance()
														.execute("PF('requiredField').show()");
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

	}

	public void totalBusFareValidation() {

		if (routeDTO.getBusFare() != null && routeDTO.getBusFare().signum() < 0) {
			setErrorMsg("Invalid Total Bus Fare");
			RequestContext.getCurrentInstance().update("frmerrorMsge");
			RequestContext.getCurrentInstance().execute("PF('errorMsge').show()");
			routeDTO.setBusFare(null);
		}

	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<RouteDTO> getRouteList() {
		return routeList;
	}

	public void setRouteList(List<RouteDTO> routeList) {
		this.routeList = routeList;
	}

	public RouteDTO getStrSelectedRouteSeq() {
		return strSelectedRouteSeq;
	}

	public void setStrSelectedRouteSeq(RouteDTO strSelectedRouteSeq) {
		this.strSelectedRouteSeq = strSelectedRouteSeq;
	}

	public List<CommonDTO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<CommonDTO> statusList) {
		this.statusList = statusList;
	}

	public String getStrSelectedStatus() {
		return strSelectedStatus;
	}

	public void setStrSelectedStatus(String strSelectedStatus) {
		this.strSelectedStatus = strSelectedStatus;
	}

	public RouteDTO getRouteDTO() {
		return routeDTO;
	}

	public void setRouteDTO(RouteDTO routeDTO) {
		this.routeDTO = routeDTO;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getReqRouteNo() {
		return reqRouteNo;
	}

	public void setReqRouteNo(String reqRouteNo) {
		this.reqRouteNo = reqRouteNo;
	}

	public Boolean getDisableMode() {
		return disableMode;
	}

	public void setDisableMode(Boolean disableMode) {
		this.disableMode = disableMode;
	}

	public Boolean getBoolRendered() {
		return boolRendered;
	}

	public void setBoolRendered(Boolean boolRendered) {
		this.boolRendered = boolRendered;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}

}
