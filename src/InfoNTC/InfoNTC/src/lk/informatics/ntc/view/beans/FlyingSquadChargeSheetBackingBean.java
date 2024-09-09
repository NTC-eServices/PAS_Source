package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.FluingSquadVioConditionDTO;
import lk.informatics.ntc.model.dto.FlyingManageInvestigationLogDTO;
import lk.informatics.ntc.model.dto.FlyingSquadChargeSheetReportDTO;
import lk.informatics.ntc.model.dto.FlyingSquadInvestiMasterDTO;
import lk.informatics.ntc.model.dto.FlyingSquadVioDocumentsDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.FlyingSquadChargeSheetService;
import lk.informatics.ntc.model.service.FlyingSquadInvestigationLogService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.unicode.UnicodeShaper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "flyingSquadChargeSheetBackingBean")
@ViewScoped
public class FlyingSquadChargeSheetBackingBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	private FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO;
	private FlyingSquadChargeSheetService flyingSquadChargeSheetService;
	private ArrayList<FluingSquadVioConditionDTO> conditionList;
	private ArrayList<FlyingSquadVioDocumentsDTO> documentList;
	private String user;
	private String savemsg;
	private String errormsg;
	public FlyingSquadInvestigationLogService flyingSquadInvestigationLogService;
	public ArrayList<FlyingSquadInvestiMasterDTO> refNoList;
	public ArrayList<FlyingManageInvestigationLogDTO> reportNoList;
	public ArrayList<FlyingManageInvestigationLogDTO> invesNoList;
	public ArrayList<FlyingManageInvestigationLogDTO> driverList;
	public ArrayList<FlyingManageInvestigationLogDTO> conductorList;
	private boolean viewMode;
	private CommonService commonService;
	private boolean disabledSaved;
	private boolean createMode;
	private ArrayList<FluingSquadVioConditionDTO> vioconditionList;
	private ArrayList<FlyingSquadVioDocumentsDTO> viodocumentList;
	private FlyingSquadChargeSheetReportDTO flyingSquadChargeSheetReportDTO;
	private StreamedContent files;
	boolean btnDisPrnit = true;
	boolean btnDisSave = true;

	@PostConstruct
	public void init() {
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		setFlyingSquadChargeSheetService(
				(FlyingSquadChargeSheetService) SpringApplicationContex.getBean("flyingSquadChargeSheetService"));
		setFlyingSquadInvestigationLogService((FlyingSquadInvestigationLogService) SpringApplicationContex
				.getBean("flyingSquadInvestigationLogService"));
		setUser(sessionBackingBean.getLoginUser());
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN361", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN361", "V");
		refNoList = flyingSquadInvestigationLogService.getrefNo();
		invesNoList = flyingSquadChargeSheetService.getinvesnolist(null, null);
		driverList = flyingSquadChargeSheetService.getdriverList();
		conductorList = flyingSquadChargeSheetService.getConductorList();
		reportNoList = flyingSquadInvestigationLogService.getreportNo(null);
		flyingSquadChargeSheetReportDTO = new FlyingSquadChargeSheetReportDTO();
		btnDisPrnit = true;
		btnDisSave = true;

		if (viewMode == true) {
			disabledSaved = true;
		} else {
			disabledSaved = false;
		}

		if (createMode == true) {
			disabledSaved = false;
		} else {
			disabledSaved = true;
		}

	}

	public void getreportNo() {
		String refno = flyingManageInvestigationLogDTO.getRefNo();
		reportNoList = flyingSquadInvestigationLogService.getreportNo(flyingManageInvestigationLogDTO.getRefNo());
		invesNoList = new ArrayList<FlyingManageInvestigationLogDTO>();
		invesNoList = flyingSquadChargeSheetService.getinvesnolist(flyingManageInvestigationLogDTO.getRefNo(), null);
		conditionList = new ArrayList<FluingSquadVioConditionDTO>();
		documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		flyingManageInvestigationLogDTO.setRefNo(refno);
		btnDisPrnit = true;
	}

	public void getinvesNolist() {
		String reportNo = flyingManageInvestigationLogDTO.getReportNo();
		invesNoList = flyingSquadChargeSheetService.getinvesnolist(null, flyingManageInvestigationLogDTO.getReportNo());
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		String no = flyingSquadChargeSheetService.getrefNoNew(reportNo);
		flyingManageInvestigationLogDTO.setRefNo(no);
		flyingManageInvestigationLogDTO.setReportNo(reportNo);
		conditionList = new ArrayList<FluingSquadVioConditionDTO>();
		documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		btnDisPrnit = true;
	}

	public void getName() {
		flyingManageInvestigationLogDTO
				.setDriverName(flyingSquadChargeSheetService.getname(flyingManageInvestigationLogDTO.getDriverID()));

	}

	public void getConductor() {
		flyingManageInvestigationLogDTO.setConductName(
				flyingSquadChargeSheetService.getname(flyingManageInvestigationLogDTO.getConductorID()));

	}

	public void getinvesNo() {
		invesNoList = flyingSquadChargeSheetService.getinvesnolist(flyingManageInvestigationLogDTO.getRefNo(),
				flyingManageInvestigationLogDTO.getReportNo());
	}

	public void search() {
		if (flyingManageInvestigationLogDTO.getInvesNo() != null
				&& !flyingManageInvestigationLogDTO.getInvesNo().equalsIgnoreCase("")) {
			String refNo = flyingManageInvestigationLogDTO.getInvesNo();
			flyingManageInvestigationLogDTO = flyingSquadChargeSheetService
					.getmasterDetails(flyingManageInvestigationLogDTO.getInvesNo());
			conditionList = new ArrayList<FluingSquadVioConditionDTO>();
			documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
			reportNoList = flyingSquadInvestigationLogService.getreportNo(flyingManageInvestigationLogDTO.getRefNo());
			flyingManageInvestigationLogDTO.setInvesNo(refNo);

			btnDisPrnit = false;
			getData(refNo);

		} else {
			if (flyingManageInvestigationLogDTO.getRefNo() != null
					&& !flyingManageInvestigationLogDTO.getRefNo().equalsIgnoreCase("")
					&& !flyingManageInvestigationLogDTO.getRefNo().isEmpty()) {
				if (flyingManageInvestigationLogDTO.getReportNo() != null
						&& !flyingManageInvestigationLogDTO.getReportNo().equalsIgnoreCase("")) {
					if (flyingManageInvestigationLogDTO.getInvesNo() != null
							&& !flyingManageInvestigationLogDTO.getInvesNo().equalsIgnoreCase("")) {

						String refNo = flyingManageInvestigationLogDTO.getInvesNo();
						flyingManageInvestigationLogDTO = flyingSquadChargeSheetService
								.getmasterDetails(flyingManageInvestigationLogDTO.getInvesNo());
						conditionList = new ArrayList<FluingSquadVioConditionDTO>();
						documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
						reportNoList = flyingSquadInvestigationLogService.getreportNo(refNo);
						flyingManageInvestigationLogDTO.setInvesNo(refNo);

						btnDisPrnit = false;
						getData(refNo);
					} else {

						sessionBackingBean.setMessage("Please Enter Charge Reference No.");
						RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
					}

				} else {

					sessionBackingBean.setMessage("Please Enter  Report No.");
					RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
				}

			} else {

				sessionBackingBean.setMessage("Please Enter  Reference No.");
				RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
			}
		}

	}

	public void save() {
		if (flyingManageInvestigationLogDTO.getInvesNo() != null
				&& !flyingManageInvestigationLogDTO.getInvesNo().equalsIgnoreCase("")) {

			flyingSquadChargeSheetService.save(flyingManageInvestigationLogDTO, user);
			flyingSquadChargeSheetService.savecondition(conditionList, user, flyingManageInvestigationLogDTO.getRefNo(),
					flyingManageInvestigationLogDTO.getReportNo(), flyingManageInvestigationLogDTO.getBusNo(),
					flyingManageInvestigationLogDTO.getInvesNo(), true);
			flyingSquadChargeSheetService.savemasterdata(documentList, user, flyingManageInvestigationLogDTO.getRefNo(),
					flyingManageInvestigationLogDTO.getReportNo(), flyingManageInvestigationLogDTO.getBusNo(),
					flyingManageInvestigationLogDTO.getInvesNo(), true);
			savemsg = "Successfully Saved";
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
			btnDisSave = false;
		} else {
			sessionBackingBean.setMessage("Please Enter Mandatory Details");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}

	public void update() {
		if (flyingManageInvestigationLogDTO.getInvesNo() != null
				&& !flyingManageInvestigationLogDTO.getInvesNo().equalsIgnoreCase("")) {

			flyingSquadChargeSheetService.save(flyingManageInvestigationLogDTO, user);
			flyingSquadChargeSheetService.savecondition(conditionList, user, flyingManageInvestigationLogDTO.getRefNo(),
					flyingManageInvestigationLogDTO.getReportNo(), flyingManageInvestigationLogDTO.getBusNo(),
					flyingManageInvestigationLogDTO.getInvesNo(), false);
			flyingSquadChargeSheetService.savemasterdata(documentList, user, flyingManageInvestigationLogDTO.getRefNo(),
					flyingManageInvestigationLogDTO.getReportNo(), flyingManageInvestigationLogDTO.getBusNo(),
					flyingManageInvestigationLogDTO.getInvesNo(), false);
			savemsg = "Successfully Updated";
			RequestContext.getCurrentInstance().update("frmsuccessSve");
			RequestContext.getCurrentInstance().execute("PF('SaveSuccess').show()");
		} else {
			sessionBackingBean.setMessage("Please Enter Master Details");
			RequestContext.getCurrentInstance().execute("PF('dlgCommonError').show()");
		}

	}

	public void cancel() {
		flyingManageInvestigationLogDTO = new FlyingManageInvestigationLogDTO();
		refNoList = flyingSquadInvestigationLogService.getrefNo();
		invesNoList = flyingSquadChargeSheetService.getinvesnolist(null, null);
		reportNoList = flyingSquadInvestigationLogService.getreportNo(null);
		driverList = flyingSquadChargeSheetService.getdriverList();
		conductorList = flyingSquadChargeSheetService.getConductorList();
		documentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		conditionList = new ArrayList<FluingSquadVioConditionDTO>();
		viodocumentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		vioconditionList = new ArrayList<FluingSquadVioConditionDTO>();
		btnDisPrnit = true;
		btnDisSave = true;

	}

	public void getData(String refNo) {
		// check if theres already data saved.
		String dataAlreadyhv = flyingSquadChargeSheetService.checkDataAlreadyHv(refNo);
		if (dataAlreadyhv == null) {
			conditionList = flyingSquadChargeSheetService.getConditionList(refNo);
			documentList = flyingSquadChargeSheetService.getdocumentlist(refNo);
			btnDisSave = true;
		} else {
			conditionList = flyingSquadChargeSheetService.getConditionListN(refNo);
			documentList = flyingSquadChargeSheetService.getdocumentlistN(refNo);
			btnDisSave = false;
		}
	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
	}

	public void close() {
		sessionBackingBean.setInvesNo("");
		RequestContext.getCurrentInstance().update("frmview");
		RequestContext.getCurrentInstance().execute("PF('showPageDialog').hide()");
	}

	public void print() {
		viodocumentList = new ArrayList<FlyingSquadVioDocumentsDTO>();
		vioconditionList = new ArrayList<FluingSquadVioConditionDTO>();
		flyingSquadChargeSheetReportDTO = new FlyingSquadChargeSheetReportDTO();

		for (FluingSquadVioConditionDTO fluingSquadVioConditionDTO : conditionList) {
			if (fluingSquadVioConditionDTO.isStstus() == true) {
				vioconditionList.add(fluingSquadVioConditionDTO);
			}
		}

		for (FlyingSquadVioDocumentsDTO flyingSquadVioDocumentsDTO : documentList) {
			if (flyingSquadVioDocumentsDTO.isViolated() == true) {
				viodocumentList.add(flyingSquadVioDocumentsDTO);
			}
		}

		flyingSquadChargeSheetReportDTO.setUser(user);
		flyingSquadChargeSheetReportDTO.setVehicleNo(flyingManageInvestigationLogDTO.getBusNo());
		flyingSquadChargeSheetReportDTO.setPermitno(flyingManageInvestigationLogDTO.getPermitNo());
		flyingSquadChargeSheetReportDTO.setOwnerName(flyingManageInvestigationLogDTO.getPermitowner());
		flyingSquadChargeSheetReportDTO.setRoute(flyingManageInvestigationLogDTO.getRouteNo());
		flyingSquadChargeSheetReportDTO.setFrom(flyingManageInvestigationLogDTO.getRouteFrom());
		flyingSquadChargeSheetReportDTO.setTo(flyingManageInvestigationLogDTO.getRouteTo());
		flyingSquadChargeSheetReportDTO.setInvesno(flyingManageInvestigationLogDTO.getInvesNo());

		flyingSquadChargeSheetReportDTO.setServiceTypeDet(flyingManageInvestigationLogDTO.getServiceTypeDes());
		flyingSquadChargeSheetReportDTO.setDriverId(flyingManageInvestigationLogDTO.getDriverID());
		flyingSquadChargeSheetReportDTO.setConductorId(flyingManageInvestigationLogDTO.getConductorID());
		flyingSquadChargeSheetReportDTO.setDriverName(flyingManageInvestigationLogDTO.getDriverName());
		flyingSquadChargeSheetReportDTO.setConductorName(flyingManageInvestigationLogDTO.getConductName());
		flyingSquadChargeSheetReportDTO.setReportNo(flyingManageInvestigationLogDTO.getReportNo());
		
		if(!flyingManageInvestigationLogDTO.getConductName().isEmpty() && !flyingManageInvestigationLogDTO.getConductName().equals(null))
		{flyingManageInvestigationLogDTO.setConductorNIC(
				flyingSquadChargeSheetService.getNIC(flyingManageInvestigationLogDTO.getConductName()));
		
		}
		if(!flyingManageInvestigationLogDTO.getDriverName().isEmpty() && !flyingManageInvestigationLogDTO.getDriverName().equals(null))
		{
		flyingManageInvestigationLogDTO
				.setDriverNIC(flyingSquadChargeSheetService.getNIC(flyingManageInvestigationLogDTO.getDriverName()));
		}
		flyingSquadChargeSheetReportDTO.setDriverNIC(flyingManageInvestigationLogDTO.getDriverNIC());
		flyingSquadChargeSheetReportDTO.setConductorNIC(flyingManageInvestigationLogDTO.getDriverNIC());

		flyingSquadChargeSheetReportDTO.setConditions(vioconditionList);
		flyingSquadChargeSheetReportDTO.setDocuments(viodocumentList);

		flyingSquadChargeSheetReportDTO.setInvesno(flyingManageInvestigationLogDTO.getInvesNo());
		if (flyingSquadChargeSheetReportDTO.getVehicleNo() != null
				|| !flyingSquadChargeSheetReportDTO.getVehicleNo().equalsIgnoreCase("")) {
			downloadFile(flyingSquadChargeSheetReportDTO);

		}

	}

	// download button
	public void downloadFile(FlyingSquadChargeSheetReportDTO flyingSquadChargeSheetReportDTO) {

		setFiles(null);
		String sourceFileName = null;
		Connection conn = null;
		/*String placeOfInspection = flyingSquadChargeSheetService
				.getInspectionLocation(flyingManageInvestigationLogDTO.getReportNo());*/
		//commented and change due request of Pramitha om 20/01/2022
		String placeOfInspection = flyingSquadChargeSheetService
				.getInspectionLocationNew(flyingManageInvestigationLogDTO.getReportNo());
		
		Date dateOfInspection=flyingSquadChargeSheetService
				.getInspectionDate(flyingManageInvestigationLogDTO.getReportNo());

		sourceFileName = "..//reports//FlyingSquadChargeSheet.jrxml";
		String logopath = "//lk//informatics//ntc//view//reports//";
		Date date = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		String sDate1 = sdf1.format(date);
		if(flyingManageInvestigationLogDTO.getDriverName()== null) {
			flyingManageInvestigationLogDTO.setDriverName("");
		}if(flyingManageInvestigationLogDTO.getConductName()== null) {
			flyingManageInvestigationLogDTO.setConductName("");
		}

		try {
			conn = ConnectionManager.getConnection();
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_national_logo", logopath);
			parameters.put("P_ntc_logo", logopath);
			parameters.put("P_Vehi_No", flyingManageInvestigationLogDTO.getBusNo());
			parameters.put("P_Permit_No", flyingManageInvestigationLogDTO.getPermitNo());
			parameters.put("P_Owner", flyingManageInvestigationLogDTO.getPermitowner());
			parameters.put("P_From", flyingManageInvestigationLogDTO.getRouteFrom());
			parameters.put("P_To", flyingManageInvestigationLogDTO.getRouteTo());
			parameters.put("P_route", flyingManageInvestigationLogDTO.getRouteNo());
			parameters.put("P_Service_Type", flyingManageInvestigationLogDTO.getServiceTypeDes());
			parameters.put("P_Driver_Name", flyingManageInvestigationLogDTO.getDriverName());
			if (flyingManageInvestigationLogDTO.getDriverID().equals(null)
					|| flyingManageInvestigationLogDTO.getDriverID().isEmpty()) {
				parameters.put("P_Driver_Id", flyingManageInvestigationLogDTO.getDriverNIC());
				flyingManageInvestigationLogDTO.setDriverID("");
			} else {
				parameters.put("P_Driver_Id", flyingManageInvestigationLogDTO.getDriverID());
			}
			parameters.put("P_Conductor_Name", flyingManageInvestigationLogDTO.getConductName());

			if (flyingManageInvestigationLogDTO.getConductorID().equals(null)
					|| flyingManageInvestigationLogDTO.getConductorID().isEmpty()) {
				parameters.put("P_Conductor_Id", flyingManageInvestigationLogDTO.getConductorNIC());
				flyingManageInvestigationLogDTO.setConductorID("");
			} else {
				parameters.put("P_Conductor_Id", flyingManageInvestigationLogDTO.getConductorID());
			}

			parameters.put("P_Report_no", flyingManageInvestigationLogDTO.getReportNo());
			parameters.put("P_User", sessionBackingBean.getLoginUser());
			parameters.put("P_Conductor_NIC", flyingManageInvestigationLogDTO.getConductorNIC());
			parameters.put("P_place_of_inspection", placeOfInspection);
			parameters.put("P_Date_of_inspection", dateOfInspection);
			parameters.put("P_charge_ref_no", flyingManageInvestigationLogDTO.getInvesNo()); // new
																								// change
																								// 2021/01/12
			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);
			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			setFiles(new DefaultStreamedContent(stream, "Application/pdf", "Charge_Sheet.pdf"));

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");
			

		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public FlyingManageInvestigationLogDTO getFlyingManageInvestigationLogDTO() {
		return flyingManageInvestigationLogDTO;
	}

	public void setFlyingManageInvestigationLogDTO(FlyingManageInvestigationLogDTO flyingManageInvestigationLogDTO) {
		this.flyingManageInvestigationLogDTO = flyingManageInvestigationLogDTO;
	}

	public FlyingSquadChargeSheetService getFlyingSquadChargeSheetService() {
		return flyingSquadChargeSheetService;
	}

	public void setFlyingSquadChargeSheetService(FlyingSquadChargeSheetService flyingSquadChargeSheetService) {
		this.flyingSquadChargeSheetService = flyingSquadChargeSheetService;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ArrayList<FlyingSquadVioDocumentsDTO> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(ArrayList<FlyingSquadVioDocumentsDTO> documentList) {
		this.documentList = documentList;
	}

	public ArrayList<FluingSquadVioConditionDTO> getConditionList() {
		return conditionList;
	}

	public void setConditionList(ArrayList<FluingSquadVioConditionDTO> conditionList) {
		this.conditionList = conditionList;
	}

	public String getSavemsg() {
		return savemsg;
	}

	public void setSavemsg(String savemsg) {
		this.savemsg = savemsg;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public FlyingSquadInvestigationLogService getFlyingSquadInvestigationLogService() {
		return flyingSquadInvestigationLogService;
	}

	public void setFlyingSquadInvestigationLogService(
			FlyingSquadInvestigationLogService flyingSquadInvestigationLogService) {
		this.flyingSquadInvestigationLogService = flyingSquadInvestigationLogService;
	}

	public ArrayList<FlyingSquadInvestiMasterDTO> getRefNoList() {
		return refNoList;
	}

	public void setRefNoList(ArrayList<FlyingSquadInvestiMasterDTO> refNoList) {
		this.refNoList = refNoList;
	}

	public ArrayList<FlyingManageInvestigationLogDTO> getReportNoList() {
		return reportNoList;
	}

	public void setReportNoList(ArrayList<FlyingManageInvestigationLogDTO> reportNoList) {
		this.reportNoList = reportNoList;
	}

	public ArrayList<FlyingManageInvestigationLogDTO> getInvesNoList() {
		return invesNoList;
	}

	public void setInvesNoList(ArrayList<FlyingManageInvestigationLogDTO> invesNoList) {
		this.invesNoList = invesNoList;
	}

	public ArrayList<FlyingManageInvestigationLogDTO> getDriverList() {
		return driverList;
	}

	public void setDriverList(ArrayList<FlyingManageInvestigationLogDTO> driverList) {
		this.driverList = driverList;
	}

	public ArrayList<FlyingManageInvestigationLogDTO> getConductorList() {
		return conductorList;
	}

	public void setConductorList(ArrayList<FlyingManageInvestigationLogDTO> conductorList) {
		this.conductorList = conductorList;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isDisabledSaved() {
		return disabledSaved;
	}

	public void setDisabledSaved(boolean disabledSaved) {
		this.disabledSaved = disabledSaved;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public ArrayList<FluingSquadVioConditionDTO> getVioconditionList() {
		return vioconditionList;
	}

	public void setVioconditionList(ArrayList<FluingSquadVioConditionDTO> vioconditionList) {
		this.vioconditionList = vioconditionList;
	}

	public ArrayList<FlyingSquadVioDocumentsDTO> getViodocumentList() {
		return viodocumentList;
	}

	public void setViodocumentList(ArrayList<FlyingSquadVioDocumentsDTO> viodocumentList) {
		this.viodocumentList = viodocumentList;
	}

	public FlyingSquadChargeSheetReportDTO getFlyingSquadChargeSheetReportDTO() {
		return flyingSquadChargeSheetReportDTO;
	}

	public void setFlyingSquadChargeSheetReportDTO(FlyingSquadChargeSheetReportDTO flyingSquadChargeSheetReportDTO) {
		this.flyingSquadChargeSheetReportDTO = flyingSquadChargeSheetReportDTO;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isBtnDisPrnit() {
		return btnDisPrnit;
	}

	public void setBtnDisPrnit(boolean btnDisPrnit) {
		this.btnDisPrnit = btnDisPrnit;
	}

	public boolean isBtnDisSave() {
		return btnDisSave;
	}

	public void setBtnDisSave(boolean btnDisSave) {
		this.btnDisSave = btnDisSave;
	}

}
