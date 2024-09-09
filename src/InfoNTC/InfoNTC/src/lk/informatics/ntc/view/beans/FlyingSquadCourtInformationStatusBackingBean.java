package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.CourtCaseDTO;
import lk.informatics.ntc.model.service.ManageCourtCaseService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@ManagedBean(name = "flyingSquadCourtInformationStatusBackingBean")
@ViewScoped
public class FlyingSquadCourtInformationStatusBackingBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	// services
	private ManageCourtCaseService manageCourtCaseService;

	// DTO
	private CourtCaseDTO courtCaseStatusDTO;
	private CourtCaseDTO viewDTO;
	private CourtCaseDTO selectRow;
	private CourtCaseDTO ajaxDTO;
	private CourtCaseDTO selectedViewDTO;
	// List
	private List<CourtCaseDTO> getVehiNoList;
	private List<CourtCaseDTO> getPermitNoNoList;
	private List<CourtCaseDTO> getServiceNoList;
	private ArrayList<CourtCaseDTO> searchedDataList;
	private List<CourtCaseDTO> getOrginiList;
	private List<CourtCaseDTO> getDestiList;
	private List<CourtCaseDTO> getCaseNoList;
	private List<Object> getCourtName;
	private ArrayList<CourtCaseDTO> selectDTO;

	private String sucessMsg, errorMsg;
	private boolean printBtnDisable;
	private StreamedContent files;
	private boolean disableVehi, disablepermit, disableRef, disableOrg, disableDes, disableCase, disableInsS,
			disableInsE, disableCourtS, disableCourtE, disableCourtName;

	@PostConstruct
	public void init() {
		manageCourtCaseService = (ManageCourtCaseService) SpringApplicationContex.getBean("manageCourtCaseService");
		courtCaseStatusDTO = new CourtCaseDTO();
		loadData();
	}

	public void loadData() {
		getVehiNoList = manageCourtCaseService.getCourtCaseVehiNum();
		getPermitNoNoList = manageCourtCaseService.getCourtCasePermitNum();
		getServiceNoList = manageCourtCaseService.getCourtCaseServiceNum();
		getOrginiList = manageCourtCaseService.getOrgin();
		getDestiList = manageCourtCaseService.getDestination();
		getCaseNoList = manageCourtCaseService.getCaseNum();
		getCourtName = manageCourtCaseService.getCourtNames();
		printBtnDisable = false;
		disableVehi = false;
		disablepermit = false;
		disableRef = false;
		disableOrg = false;
		disableDes = false;
		disableCase = false;
		disableInsS = false;
		disableInsE = false;
		disableCourtS = false;
		disableCourtE = false;
		disableCourtName = false;
	}

	public void ajaxOnVehiNo() {
		ajaxDTO = new CourtCaseDTO();
		if (!courtCaseStatusDTO.getVehicleNo().trim().isEmpty() && courtCaseStatusDTO.getVehicleNo() != null) {
			ajaxDTO = manageCourtCaseService.getAjaxData(courtCaseStatusDTO);
			if (ajaxDTO != null) {
				courtCaseStatusDTO.setVehicleNo(ajaxDTO.getVehicleNo());
				courtCaseStatusDTO.setPermitNo(ajaxDTO.getPermitNo());
				courtCaseStatusDTO.setRefrenceNo(ajaxDTO.getRefrenceNo());

				courtCaseStatusDTO.setOrgin(ajaxDTO.getOrgin());
				courtCaseStatusDTO.setDestination(ajaxDTO.getDestination());

			}
			disableVehi = false;
			disablepermit = true;
			disableRef = true;
			disableOrg = true;
			disableDes = true;
			disableCase = true;
			disableInsS = true;
			disableInsE = true;
			disableCourtS = true;
			disableCourtE = true;
			disableCourtName = true;
		}
	}

	public void ajaxOnPermitNo() {
		ajaxDTO = new CourtCaseDTO();
		if (!courtCaseStatusDTO.getPermitNo().trim().isEmpty() && courtCaseStatusDTO.getPermitNo() != null) {
			ajaxDTO = manageCourtCaseService.getAjaxData(courtCaseStatusDTO);
			if (ajaxDTO != null) {
				courtCaseStatusDTO.setVehicleNo(ajaxDTO.getVehicleNo());
				courtCaseStatusDTO.setPermitNo(ajaxDTO.getPermitNo());
				courtCaseStatusDTO.setRefrenceNo(ajaxDTO.getRefrenceNo());

				courtCaseStatusDTO.setOrgin(ajaxDTO.getOrgin());
				courtCaseStatusDTO.setDestination(ajaxDTO.getDestination());

			}
			disableVehi = true;
			disablepermit = false;
			disableRef = true;
			disableOrg = true;
			disableDes = true;
			disableCase = true;
			disableInsS = true;
			disableInsE = true;
			disableCourtS = true;
			disableCourtE = true;
			disableCourtName = true;
		}
	}

	public void ajaxOnRefNo() {
		ajaxDTO = new CourtCaseDTO();
		if (!courtCaseStatusDTO.getRefrenceNo().trim().isEmpty() && courtCaseStatusDTO.getRefrenceNo() != null) {
			ajaxDTO = manageCourtCaseService.getAjaxData(courtCaseStatusDTO);
			if (ajaxDTO != null) {
				courtCaseStatusDTO.setVehicleNo(ajaxDTO.getVehicleNo());
				courtCaseStatusDTO.setPermitNo(ajaxDTO.getPermitNo());

				courtCaseStatusDTO.setCaseNo(ajaxDTO.getCaseNo());
				courtCaseStatusDTO.setCourtName(ajaxDTO.getCourtName());
				courtCaseStatusDTO.setOrgin(ajaxDTO.getOrgin());
				courtCaseStatusDTO.setDestination(ajaxDTO.getDestination());
			}
			disableVehi = true;
			disablepermit = true;
			disableRef = false;
			disableOrg = true;
			disableDes = true;
			disableCase = true;
			disableInsS = true;
			disableInsE = true;
			disableCourtS = true;
			disableCourtE = true;
			disableCourtName = true;
		}
	}

	public void ajaxOnCaseNo() {
		ajaxDTO = new CourtCaseDTO();

		disableVehi = true;
		disablepermit = true;
		disableRef = true;
		disableOrg = true;
		disableDes = true;
		disableCase = false;
		disableInsS = true;
		disableInsE = true;
		disableCourtS = true;
		disableCourtE = true;
		disableCourtName = true;

	}

	public void ajaxOnInspectDateS() {
		disableVehi = true;
		disablepermit = true;
		disableRef = true;
		disableOrg = true;
		disableDes = true;
		disableCase = true;
		disableInsS = false;
		disableInsE = false;
		disableCourtS = true;
		disableCourtE = true;
		disableCourtName = true;

	}

	public void ajaxOnInspectDateE() {
		disableVehi = true;
		disablepermit = true;
		disableRef = true;
		disableOrg = true;
		disableDes = true;
		disableCase = true;
		disableInsS = false;
		disableInsE = false;
		disableCourtS = true;
		disableCourtE = true;
		disableCourtName = true;

	}

	public void ajaxOnCourtDateS() {

		disableVehi = true;
		disablepermit = true;
		disableRef = true;
		disableOrg = true;
		disableDes = true;
		disableCase = true;
		disableInsS = true;
		disableInsE = true;
		disableCourtS = false;
		disableCourtE = false;
		disableCourtName = true;
	}

	public void ajaxOnCourtDateE() {

		disableVehi = true;
		disablepermit = true;
		disableRef = true;
		disableOrg = true;
		disableDes = true;
		disableCase = true;
		disableInsS = true;
		disableInsE = true;
		disableCourtS = false;
		disableCourtE = false;
		disableCourtName = true;
	}

	public void ajaxOnOrgn() {

		disableVehi = true;
		disablepermit = true;
		disableRef = true;
		disableOrg = false;
		disableDes = true;
		disableCase = true;
		disableInsS = true;
		disableInsE = true;
		disableCourtS = true;
		disableCourtE = true;
		disableCourtName = true;

	}

	public void ajaxOnDes() {

		disableVehi = true;
		disablepermit = true;
		disableRef = true;
		disableOrg = true;
		disableDes = false;
		disableCase = true;
		disableInsS = true;
		disableInsE = true;
		disableCourtS = true;
		disableCourtE = true;
		disableCourtName = true;
	}

	public void searchButtonAction() {

		if ((courtCaseStatusDTO.getCaseNo() != null && !courtCaseStatusDTO.getCaseNo().trim().isEmpty())
				|| (courtCaseStatusDTO.getVehicleNo() != null && !courtCaseStatusDTO.getVehicleNo().trim().isEmpty())
				|| (courtCaseStatusDTO.getPermitNo() != null && !courtCaseStatusDTO.getPermitNo().trim().isEmpty())
				|| (courtCaseStatusDTO.getRefrenceNo() != null && !courtCaseStatusDTO.getRefrenceNo().trim().isEmpty())
				|| (courtCaseStatusDTO.getOrgin() != null && !courtCaseStatusDTO.getOrgin().trim().isEmpty())
				|| (courtCaseStatusDTO.getDestination() != null
						&& !courtCaseStatusDTO.getDestination().trim().isEmpty())
				|| (courtCaseStatusDTO.getInspectStartDate() != null && courtCaseStatusDTO.getInspectEndDate() != null)
				|| (courtCaseStatusDTO.getCourtStartDate() != null && courtCaseStatusDTO.getCourtStartDate() != null)
				|| (courtCaseStatusDTO.getCourtName() != null && !courtCaseStatusDTO.getCourtName().trim().isEmpty())) {

			searchedDataList = manageCourtCaseService.getSearchedData(courtCaseStatusDTO);
			if (searchedDataList == null || searchedDataList.isEmpty()) {

				errorMsg = "No data found. Please change the search criteria and try again.";
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}

		else {

			errorMsg = "Search Criteria Not Entered";
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearButtonAction() {

		courtCaseStatusDTO = new CourtCaseDTO();
		searchedDataList = new ArrayList<CourtCaseDTO>();
		disableVehi = false;
		disablepermit = false;
		disableRef = false;
		disableOrg = false;
		disableDes = false;
		disableCase = false;
		disableInsS = false;
		disableInsE = false;
		disableCourtS = false;
		disableCourtE = false;
		disableCourtName = false;
	}

	public void view() {
		RequestContext.getCurrentInstance().execute("PF('viewDlg').show()");
		RequestContext.getCurrentInstance().update("viewDlg");
		viewDTO = manageCourtCaseService.getManageCourtcaseViewData(selectedViewDTO.getCaseNo());

	}

	public void selectRow() {
		printBtnDisable = false;
	}

	public StreamedContent PrintBtn() throws JRException {
		if (!selectDTO.isEmpty()) {
			ArrayList<String> arr = new ArrayList<>();
			String loginUser = sessionBackingBean.getLoginUser();

			files = null;
			String sourceFileName = null;

			Connection conn = null;

			sourceFileName = "..//reports//FlyingSquadCourtInformationStatusReport.jrxml";

			try {
				conn = ConnectionManager.getConnection();
				String imagepath = "//lk//informatics//ntc//view//reports//";

				// Parameters for report
				Map<String, Object> parameters = new HashMap<String, Object>();

				for (CourtCaseDTO ssDTO : selectDTO) {
					arr.add(ssDTO.getCaseNo());

				}
				parameters.put("P_image", imagepath);
				parameters.put("P_case_no", arr);
				parameters.put("P_user", loginUser);
				if (courtCaseStatusDTO.getCourtStartDate() != null && courtCaseStatusDTO.getCourtEndDate() != null) {
					parameters.put("P_case_S_date", courtCaseStatusDTO.getCourtStartDate());
					parameters.put("P_case_E_date", courtCaseStatusDTO.getCourtEndDate());
				} else {
					parameters.put("P_case_S_date", "-");
					parameters.put("P_case_E_date", "-");
				}
				if (courtCaseStatusDTO.getCourtStartDate() != null && courtCaseStatusDTO.getCourtEndDate() != null) {
					parameters.put("P_Inves_S_date", courtCaseStatusDTO.getInspectStartDate());
					parameters.put("P_Inves_E_date", courtCaseStatusDTO.getInspectEndDate());
				} else {
					parameters.put("P_Inves_S_date", "-");
					parameters.put("P_Inves_E_date", "-");
				}
				if (courtCaseStatusDTO.getVehicleNo() != null && !courtCaseStatusDTO.getVehicleNo().trim().isEmpty()) {
					parameters.put("P_vehi_num", courtCaseStatusDTO.getVehicleNo());
				} else {
					parameters.put("P_vehi_num", "-");
				}
				if (courtCaseStatusDTO.getPermitNo() != null && !courtCaseStatusDTO.getPermitNo().trim().isEmpty()) {
					parameters.put("P_permit_no", courtCaseStatusDTO.getPermitNo());
				} else {
					parameters.put("P_permit_no", "-");
				}
				if (courtCaseStatusDTO.getCourtName() != null && !courtCaseStatusDTO.getCourtName().trim().isEmpty()) {
					parameters.put("P_court_name", courtCaseStatusDTO.getCourtName());
				} else {
					parameters.put("P_court_name", "-");
				}
				if (courtCaseStatusDTO.getOrgin() != null && !courtCaseStatusDTO.getOrgin().trim().isEmpty()) {
					parameters.put("P_origin", courtCaseStatusDTO.getOrgin());
				} else {
					parameters.put("P_origin", "-");
				}
				if (courtCaseStatusDTO.getDestination() != null
						&& !courtCaseStatusDTO.getDestination().trim().isEmpty()) {
					parameters.put("P_destination", courtCaseStatusDTO.getDestination());
				} else {
					parameters.put("P_destination", "-");
				}

				JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

				JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

				byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
				InputStream stream = new ByteArrayInputStream(pdfByteArray);
				files = new DefaultStreamedContent(stream, "Application/pdf", "FlyingSquadCourtStatus.pdf");

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
		} else {

			errorMsg = "Please select a data for Print";
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		}
		return files;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public CourtCaseDTO getCourtCaseStatusDTO() {
		return courtCaseStatusDTO;
	}

	public void setCourtCaseStatusDTO(CourtCaseDTO courtCaseStatusDTO) {
		this.courtCaseStatusDTO = courtCaseStatusDTO;
	}

	public ArrayList<CourtCaseDTO> getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(ArrayList<CourtCaseDTO> selectDTO) {
		this.selectDTO = selectDTO;
	}

	public List<CourtCaseDTO> getGetVehiNoList() {
		return getVehiNoList;
	}

	public void setGetVehiNoList(List<CourtCaseDTO> getVehiNoList) {
		this.getVehiNoList = getVehiNoList;
	}

	public List<CourtCaseDTO> getGetPermitNoNoList() {
		return getPermitNoNoList;
	}

	public void setGetPermitNoNoList(List<CourtCaseDTO> getPermitNoNoList) {
		this.getPermitNoNoList = getPermitNoNoList;
	}

	public List<CourtCaseDTO> getGetServiceNoList() {
		return getServiceNoList;
	}

	public void setGetServiceNoList(List<CourtCaseDTO> getServiceNoList) {
		this.getServiceNoList = getServiceNoList;
	}

	public ArrayList<CourtCaseDTO> getSearchedDataList() {
		return searchedDataList;
	}

	public void setSearchedDataList(ArrayList<CourtCaseDTO> searchedDataList) {
		this.searchedDataList = searchedDataList;
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

	public ManageCourtCaseService getManageCourtCaseService() {
		return manageCourtCaseService;
	}

	public void setManageCourtCaseService(ManageCourtCaseService manageCourtCaseService) {
		this.manageCourtCaseService = manageCourtCaseService;
	}

	public CourtCaseDTO getViewDTO() {
		return viewDTO;
	}

	public void setViewDTO(CourtCaseDTO viewDTO) {
		this.viewDTO = viewDTO;
	}

	public CourtCaseDTO getSelectRow() {
		return selectRow;
	}

	public void setSelectRow(CourtCaseDTO selectRow) {
		this.selectRow = selectRow;
	}

	public CourtCaseDTO getAjaxDTO() {
		return ajaxDTO;
	}

	public void setAjaxDTO(CourtCaseDTO ajaxDTO) {
		this.ajaxDTO = ajaxDTO;
	}

	public List<CourtCaseDTO> getGetOrginiList() {
		return getOrginiList;
	}

	public void setGetOrginiList(List<CourtCaseDTO> getOrginiList) {
		this.getOrginiList = getOrginiList;
	}

	public List<CourtCaseDTO> getGetDestiList() {
		return getDestiList;
	}

	public void setGetDestiList(List<CourtCaseDTO> getDestiList) {
		this.getDestiList = getDestiList;
	}

	public List<CourtCaseDTO> getGetCaseNoList() {
		return getCaseNoList;
	}

	public void setGetCaseNoList(List<CourtCaseDTO> getCaseNoList) {
		this.getCaseNoList = getCaseNoList;
	}

	public CourtCaseDTO getSelectedViewDTO() {
		return selectedViewDTO;
	}

	public void setSelectedViewDTO(CourtCaseDTO selectedViewDTO) {
		this.selectedViewDTO = selectedViewDTO;
	}

	public boolean isPrintBtnDisable() {
		return printBtnDisable;
	}

	public void setPrintBtnDisable(boolean printBtnDisable) {
		this.printBtnDisable = printBtnDisable;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isDisableVehi() {
		return disableVehi;
	}

	public void setDisableVehi(boolean disableVehi) {
		this.disableVehi = disableVehi;
	}

	public boolean isDisablepermit() {
		return disablepermit;
	}

	public void setDisablepermit(boolean disablepermit) {
		this.disablepermit = disablepermit;
	}

	public boolean isDisableRef() {
		return disableRef;
	}

	public void setDisableRef(boolean disableRef) {
		this.disableRef = disableRef;
	}

	public boolean isDisableOrg() {
		return disableOrg;
	}

	public void setDisableOrg(boolean disableOrg) {
		this.disableOrg = disableOrg;
	}

	public boolean isDisableDes() {
		return disableDes;
	}

	public void setDisableDes(boolean disableDes) {
		this.disableDes = disableDes;
	}

	public boolean isDisableCase() {
		return disableCase;
	}

	public void setDisableCase(boolean disableCase) {
		this.disableCase = disableCase;
	}

	public boolean isDisableInsS() {
		return disableInsS;
	}

	public void setDisableInsS(boolean disableInsS) {
		this.disableInsS = disableInsS;
	}

	public boolean isDisableInsE() {
		return disableInsE;
	}

	public void setDisableInsE(boolean disableInsE) {
		this.disableInsE = disableInsE;
	}

	public boolean isDisableCourtS() {
		return disableCourtS;
	}

	public void setDisableCourtS(boolean disableCourtS) {
		this.disableCourtS = disableCourtS;
	}

	public boolean isDisableCourtE() {
		return disableCourtE;
	}

	public void setDisableCourtE(boolean disableCourtE) {
		this.disableCourtE = disableCourtE;
	}

	public List<Object> getGetCourtName() {
		return getCourtName;
	}

	public void setGetCourtName(List<Object> getCourtName) {
		this.getCourtName = getCourtName;
	}

	public boolean isDisableCourtName() {
		return disableCourtName;
	}

	public void setDisableCourtName(boolean disableCourtName) {
		this.disableCourtName = disableCourtName;
	}

}
