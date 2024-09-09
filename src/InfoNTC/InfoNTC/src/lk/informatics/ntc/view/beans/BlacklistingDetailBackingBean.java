package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lk.informatics.ntc.model.dto.DocumentManagementDTO;
import lk.informatics.ntc.model.dto.DriverConductorBlacklistDTO;
import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.DropDownDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DocumentManagementService;
import lk.informatics.ntc.model.service.DriverConductorTrainingService;
import lk.informatics.ntc.model.service.GrievanceManagementService;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
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

/**
 * 
 * @author dilakshi.h
 *
 */
@ViewScoped
@ManagedBean(name = "blacklistingDetailBackingBean")
public class BlacklistingDetailBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private DriverConductorTrainingService driverConductorTrainingService;
	private GrievanceManagementService grievanceManagementService;
	private CommonService commonService;
	private DocumentManagementService documentManagementService;
	
	private DriverConductorRegistrationDTO driverConductorRegistrationDTO = new DriverConductorRegistrationDTO();

	private String sucessMsg;
	private String errorMsg;

	private String dateFormatStr = "dd/MM/yyyy";

	// search fields
	private String searchNic;
	private String searchDriverConductor;
	private String searchStatus;

	// common
	private List<DropDownDTO> driverCondList;
	private List<DropDownDTO> blackListTypes;
	private DriverConductorBlacklistDTO blacklisterDTO = new DriverConductorBlacklistDTO();

	// blacklist-add
	private boolean isDriver;
	private boolean isConductor;
	// blacklist-approval/clearance
	private List<DropDownDTO> bPendingDriCon;
	private List<DropDownDTO> blackListedDriCon;
	private List<DriverConductorBlacklistDTO> blacklisterDTOList = new ArrayList<DriverConductorBlacklistDTO>();
	private List<DriverConductorBlacklistDTO> selectedBlacklisterList = new ArrayList<DriverConductorBlacklistDTO>();
	private String approvalRejectReason;
	private List<DocumentManagementDTO> mandatoryList = new ArrayList<DocumentManagementDTO>(0);
	private List<DocumentManagementDTO> optionalList = new ArrayList<DocumentManagementDTO>(0);
	private boolean disDocument;
	private boolean enableLetterPrint;
	private StreamedContent files;
	private boolean disbledcId;
	private List<DriverConductorRegistrationDTO> driverConNoList;

	public BlacklistingDetailBackingBean() {
		driverConductorTrainingService = (DriverConductorTrainingService) SpringApplicationContex
				.getBean("driverConductorTrainingService");
		grievanceManagementService = (GrievanceManagementService) SpringApplicationContex
				.getBean("grievanceManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		documentManagementService = (DocumentManagementService) SpringApplicationContex
				.getBean("documentManagementService");

		blackListTypes = driverConductorTrainingService.getBlacklistTypes();
		driverCondList = driverConductorTrainingService.getNonBlackListedDriCond();
		bPendingDriCon = driverConductorTrainingService.getPendingBlackListedDriCond();
		blackListedDriCon = driverConductorTrainingService.getBlackListedDriCond();
		clearSearchView();
		clearApprovalSearchView();
		clearClearanceSearchView();
		disDocument = true;
		enableLetterPrint = true;
		disbledcId = true;

	}

	public List<String> completeDriverCondIdAll(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < driverCondList.size(); i++) {
			String cm = driverCondList.get(i).getCode();
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public List<String> completeDriverCondIdBPending(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < bPendingDriCon.size(); i++) {
			String cm = bPendingDriCon.get(i).getCode();
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	public List<String> completeDriverCondIdBlacklisted(String query) {
		query = query.toUpperCase();
		List<String> filteredNo = new ArrayList<String>();

		for (int i = 0; i < blackListedDriCon.size(); i++) {
			String cm = blackListedDriCon.get(i).getCode();
			if (cm.toUpperCase().contains(query)) {
				filteredNo.add(cm);
			}
		}
		return filteredNo;
	}

	// ADD TO BLACKLIST
	public void search() {
		enableLetterPrint = true;
		if ((searchNic == null || searchNic.isEmpty())
				&& (searchDriverConductor == null || searchDriverConductor.isEmpty())) {
			showMsg("ERROR", "Please enter a value first");
			return;
		}
		isDriver = false;
		isConductor = false;

		blacklisterDTO = driverConductorTrainingService.getBlacklisterInfo(searchNic, searchDriverConductor);
		//if (blacklisterDTO.getDriverId() != null) {
		if (blacklisterDTO.getTrainingType().equals("ND") ||blacklisterDTO.getTrainingType().equals("RD")
				|| blacklisterDTO.getTrainingType().equals("RRD")|| blacklisterDTO.getTrainingType().equals("RRRD")
				|| blacklisterDTO.getTrainingType().equals("DD") || blacklisterDTO.getTrainingType().equals("FD")) {
			isDriver = true;
			byte[] driverImg = driverConductorTrainingService.getDriverConductorPhoto(blacklisterDTO.getDriverAppNo());
			blacklisterDTO.setDriverImgPath(readImages(driverImg, blacklisterDTO.getDriverAppNo()));
			if (blacklisterDTO.getDriverStatus().equalsIgnoreCase("B")) {
				disDocument = false;
				enableLetterPrint = false;
			} else {
				disDocument = true;
			}
		}
		//if (blacklisterDTO.getConductorId() != null) {
		if (blacklisterDTO.getTrainingType().equals("NC") ||blacklisterDTO.getTrainingType().equals("RC")
				|| blacklisterDTO.getTrainingType().equals("RRC")|| blacklisterDTO.getTrainingType().equals("RRRC")
				|| blacklisterDTO.getTrainingType().equals("DC") || blacklisterDTO.getTrainingType().equals("FC")) {
			isConductor = true;
			byte[] conductorImg = driverConductorTrainingService
					.getDriverConductorPhoto(blacklisterDTO.getConductorAppNo());
			blacklisterDTO.setConductorImgPath(readImages(conductorImg, blacklisterDTO.getConductorAppNo()));
			if (blacklisterDTO.getConductorStatus().equalsIgnoreCase("B")) {
				disDocument = false;
			} else {
				disDocument = true;
			}
		}
	}

	public void clearSearchView() {
		searchNic = null;
		searchDriverConductor = null;
		blacklisterDTO = new DriverConductorBlacklistDTO();
		isDriver = false;
		disDocument = true;
		isConductor = false;
		enableLetterPrint = true;
		disbledcId = true;
	}

	public void saveBlacklistData() {
		String user = sessionBackingBean.loginUser;
//		driverConductorRegistrationDTO.setDriverConductorId(searchDriverConductor);
//		driverConductorRegistrationDTO.setDriverConductorId(blacklisterDTO.getDcId());
		if(blacklisterDTO.getNic() != null) {
			//System.out.println(blacklisterDTO.getNic());
			driverConductorRegistrationDTO.setNic(blacklisterDTO.getNic());
		}
		

		if (blacklisterDTO.getBlacklistType() == null || blacklisterDTO.getBlacklistType().trim().isEmpty()) {
			showMsg("ERROR", "Please select a type of blacklist");
			return;
		}

		if (blacklisterDTO.getDriverId() != null) {
			driverConductorRegistrationDTO.setDriverConductorId(blacklisterDTO.getDriverId());
			if (!driverConductorTrainingService.createBlacklister(blacklisterDTO, blacklisterDTO.getDriverId(), user)) {
				showMsg("ERROR", "Error while updating status");
				return;
			}
		}
		if (blacklisterDTO.getConductorId() != null) {
			driverConductorRegistrationDTO.setDriverConductorId(blacklisterDTO.getConductorId());
			if (!driverConductorTrainingService.createBlacklister(blacklisterDTO, blacklisterDTO.getConductorId(),
					user)) {
				showMsg("ERROR", "Error while updating status");
				return;
			}
		}

		driverCondList = driverConductorTrainingService.getNonBlackListedDriCond();
		driverConductorTrainingService.beanLinkMethod(driverConductorRegistrationDTO, user, "Save Blacklist Driver / Conductor", "Blacklist Driver / Conductor");
		showMsg("SUCCESS", "Saved Successfully");
		search();
		disDocument = false;

	}

	// BLACKLIST APPROVAL/ CLEARANCE
	public void searchApproval() {
		if ((searchNic == null || searchNic.isEmpty())
				&& (searchDriverConductor == null || searchDriverConductor.isEmpty())
				&& (searchStatus == null || searchStatus.isEmpty())) {
			showMsg("ERROR", "Please enter a value first");
			return;
		}

		blacklisterDTOList = driverConductorTrainingService.getPendingBlacklister(searchStatus, searchNic,
				searchDriverConductor);

		if (blacklisterDTOList == null || blacklisterDTOList.isEmpty()) {
			showMsg("ERROR", "No data found.");
			return;
		}
	}

	public void clearApprovalSearchView() {
		searchStatus = null;
		searchNic = null;
		searchDriverConductor = null;
		blacklisterDTOList = new ArrayList<DriverConductorBlacklistDTO>();
		selectedBlacklisterList = new ArrayList<DriverConductorBlacklistDTO>();
	}

	public void Approval() {
		String user = sessionBackingBean.loginUser;
		String approveStatus = null;
		if (selectedBlacklisterList == null || selectedBlacklisterList.isEmpty()) {
			showMsg("ERROR", "Please select at least one record");
			return;
		}

		for (DriverConductorBlacklistDTO blacklister : nullSafe(selectedBlacklisterList)) {
			if (!driverConductorTrainingService.approvalBlacklister(blacklister.getNic(), user)) {
				showMsg("ERROR", "Error while updating status");
				return;
			}

			/** added by tharushi.e for checked already approved records **/
			if (blacklister.getConductorId() != null) {
				approveStatus = driverConductorTrainingService.getApproveStatus(blacklister.getConductorId());
			}
			if (blacklister.getConductorId() != null) {
				approveStatus = driverConductorTrainingService.getApproveStatus(blacklister.getDriverId());
			}

			if (approveStatus != null) {
				if (approveStatus.equals("B")) {
					showMsg("ERROR", "Already Approved");

				}
			}

			/** finished **/
		}
		if (approveStatus != "B" || approveStatus.equals(null)) {
			bPendingDriCon = driverConductorTrainingService.getPendingBlackListedDriCond();
			
			for(DriverConductorBlacklistDTO  data : selectedBlacklisterList) {
				blacklisterDTO.setNic(data.getNic());
				driverConductorTrainingService.beanLinkMethod(blacklisterDTO, user, "Approve Blacklist Driver / Conductor", "Approval - Blacklist Driver / Conductor");
				
			}
			showMsg("SUCCESS", "Approved Successfully");
		}
		clearApprovalSearchView();

	}

	public void showApprovalRejectDialog() {
		if (selectedBlacklisterList == null || selectedBlacklisterList.isEmpty()) {
			showMsg("ERROR", "Please select at least one record");
			return;
		}
		approvalRejectReason = null;
	}

	public void approvalReject() {
		String user = sessionBackingBean.loginUser;

		if (approvalRejectReason == null || approvalRejectReason.trim().isEmpty()) {
			showMsg("ERROR", "Please enter a reject reason");
			return;
		}

		for (DriverConductorBlacklistDTO blacklister : nullSafe(selectedBlacklisterList)) {
			if (!driverConductorTrainingService.rejectBlacklisters(blacklister, approvalRejectReason, user)) {
				showMsg("ERROR", "Error while updating status");
				return;
			}
		}

		for(DriverConductorBlacklistDTO  data : selectedBlacklisterList) {
			blacklisterDTO.setNic(data.getNic());
			driverConductorTrainingService.beanLinkMethod(blacklisterDTO, user, "Reject Blacklist Driver / Conductor", "Approval - Blacklist Driver / Conductor");		
		}
		
		bPendingDriCon = driverConductorTrainingService.getPendingBlackListedDriCond();
		RequestContext.getCurrentInstance().execute("PF('rejectConfirm').hide()");
		showMsg("SUCCESS", "Saved Successfully");
		searchApproval();
		
	}

	// BLACKLIST CLEARANCE
	public void searchClearance() {
		if ((searchNic == null || searchNic.isEmpty())
				&& (searchDriverConductor == null || searchDriverConductor.isEmpty())
				&& (searchStatus == null || searchStatus.isEmpty())) {
			showMsg("ERROR", "Please enter a value first");
			return;
		}

		blacklisterDTOList = driverConductorTrainingService.getBlacklistedList(searchStatus, searchNic,
				searchDriverConductor);

		if (blacklisterDTOList == null || blacklisterDTOList.isEmpty()) {
			showMsg("ERROR", "No data found.");
			return;
		}
	}

	public void clearClearanceSearchView() {
		searchStatus = null;
		searchNic = null;
		searchDriverConductor = null;
		blacklisterDTOList = new ArrayList<DriverConductorBlacklistDTO>();
		selectedBlacklisterList = new ArrayList<DriverConductorBlacklistDTO>();
	}

	public void ApproveClearance() {
		String user = sessionBackingBean.loginUser;
		if (selectedBlacklisterList == null || selectedBlacklisterList.isEmpty()) {
			showMsg("ERROR", "Please select at least one record");
			return;
		}

		for (DriverConductorBlacklistDTO blacklister : selectedBlacklisterList) {
			if (!driverConductorTrainingService.clearanceBlacklist(blacklister.getStatus(), blacklister.getNic(),
					user)) {
				showMsg("ERROR", "Error while updating status");
				return;
			}
		}

		blackListedDriCon = driverConductorTrainingService.getBlackListedDriCond();
		
		for(DriverConductorBlacklistDTO  data : selectedBlacklisterList) {
			blacklisterDTO.setNic(data.getNic());
			driverConductorTrainingService.beanLinkMethod(blacklisterDTO, user, "Blacklist Cleared", "Blacklist Driver / Conductor- Clearance");
			
		}
		
		showMsg("SUCCESS", "Blacklist Cleared Successfully");
		clearClearanceSearchView();
	}

	// END OF BLACKLIST CLEARANCE
	// Common methods

	private String readImages(byte[] bytes, String fileName) {
		String imgPath = null;

		if (bytes != null) {

			try {
				String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
				Properties props = PropertyReader.loadPropertyFile();

				File dir = new File(path + props.getProperty("driverconductor.idcards.temp.images.path"));
				imgPath = path + props.getProperty("driverconductor.idcards.temp.images.path") + fileName + ".png";

				if (!dir.exists())
					dir.mkdir();
				else
					FileUtils.cleanDirectory(dir);

				FileUtils.writeByteArrayToFile(new File(imgPath), bytes);

			} catch (java.io.IOException e) {
				e.printStackTrace();
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}
		return imgPath;
	}

	private void showMsg(String type, String msg) {
		if (type.equalsIgnoreCase("ERROR")) {
			errorMsg = msg;
			RequestContext.getCurrentInstance().update("frmError");
			RequestContext.getCurrentInstance().execute("PF('errorDialog').show()");
		} else {
			sucessMsg = msg;
			RequestContext.getCurrentInstance().update("frmSuccess");
			RequestContext.getCurrentInstance().execute("PF('successDialog').show()");
		}
	}

	private static <T> Iterable<T> nullSafe(Iterable<T> iterable) {
		return iterable == null ? Collections.<T>emptyList() : iterable;
	}

	// Black List Document Management
	public void documentManagement() {
		driverConductorRegistrationDTO.setDriverConductorId(searchDriverConductor);
		driverConductorRegistrationDTO.setNic(searchNic);
		try {

			String trainingType = null;
			trainingType = driverConductorTrainingService.getTransactionTypeDes("BL");

			sessionBackingBean.setTransactionType(trainingType.toUpperCase());

			sessionBackingBean.setDriverConductorId(searchDriverConductor);
			String appNo = null;
			if (blacklisterDTO.getDriverAppNo() != null) {
				appNo = blacklisterDTO.getDriverAppNo();

			} else {
				appNo = blacklisterDTO.getConductorAppNo();

			}
			sessionBackingBean.setDcAppNo(appNo);
			String strTransactionType = "BL";

			setMandatoryList(documentManagementService.mandatoryDocsFordriverConductor(strTransactionType,
					searchDriverConductor));
			setOptionalList(documentManagementService.optionalDocsFordriverConductor(strTransactionType, appNo));

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.driverConductorMandatoryListM(searchDriverConductor, strTransactionType);
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.driverConductorOptionalListM(searchDriverConductor, strTransactionType);

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");
			driverConductorTrainingService.beanLinkMethod(driverConductorRegistrationDTO, sessionBackingBean.getLoginUser(), "Upload Blacklist Driver / Conductor documents", "Blacklist Driver / Conductor");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// black list approve Document Management
	public void documentsManagementForApprove() {

		try {
			String trainingType = null;
			trainingType = driverConductorTrainingService.getTransactionTypeDes("BL");

			sessionBackingBean.setTransactionType(trainingType.toUpperCase());

			sessionBackingBean.setDriverConductorId(searchDriverConductor);
			String appNo = null;
			if (blacklisterDTO.getDriverAppNo() != null) {
				appNo = blacklisterDTO.getDriverAppNo();

			} else {
				appNo = blacklisterDTO.getConductorAppNo();

			}
			sessionBackingBean.setDcAppNo(appNo);
			String strTransactionType = "BL";

			setMandatoryList(documentManagementService.mandatoryDocsFordriverConductor(strTransactionType,
					searchDriverConductor));
			setOptionalList(documentManagementService.optionalDocsFordriverConductor(strTransactionType,
					searchDriverConductor));

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.driverConductorMandatoryListM(searchDriverConductor, strTransactionType);
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.driverConductorOptionalListM(searchDriverConductor, strTransactionType);

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");
			
			blacklisterDTO.setDcId(searchDriverConductor);
			driverConductorTrainingService.beanLinkMethod(blacklisterDTO, sessionBackingBean.getLoginUser(), "Document Manage For Approval", "Approval - Blacklist Driver / Conductor");

			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// end black list clearnes Document management
	public void documentsManagementForClear() {

		try {
			String trainingType = null;
			trainingType = driverConductorTrainingService.getTransactionTypeDes("BL");

			sessionBackingBean.setTransactionType(trainingType.toUpperCase());

			sessionBackingBean.setDriverConductorId(searchDriverConductor);
			String appNo = null;
			if (blacklisterDTO.getDriverAppNo() != null) {
				appNo = blacklisterDTO.getDriverAppNo();

			} else {
				appNo = blacklisterDTO.getConductorAppNo();

			}
			sessionBackingBean.setDcAppNo(appNo);
			String strTransactionType = "BL";

			setMandatoryList(documentManagementService.mandatoryDocsFordriverConductor(strTransactionType,
					searchDriverConductor));
			setOptionalList(documentManagementService.optionalDocsFordriverConductor(strTransactionType,
					searchDriverConductor));

			sessionBackingBean.sisuSariyaMandatoryDocumentList = documentManagementService
					.driverConductorMandatoryListM(searchDriverConductor, strTransactionType);
			sessionBackingBean.sisuSariyaOptionalDocumentList = documentManagementService
					.driverConductorOptionalListM(searchDriverConductor, strTransactionType);

			RequestContext.getCurrentInstance().execute("PF('uploadDocument').show()");

			blacklisterDTO.setDcId(searchDriverConductor);
			driverConductorTrainingService.beanLinkMethod(blacklisterDTO, sessionBackingBean.getLoginUser(), "Document Manage For Approval", "Approval - Blacklist Driver / Conductor");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public StreamedContent generateBlackListLetters() throws JRException {
		String sourceFileName = null;
		files = null;
		Connection conn = null;

		try {
			conn = ConnectionManager.getConnection();
			sourceFileName = "..//reports//BlackListedLetter.jrxml";

			String logopath = "//lk//informatics//ntc//view//reports//";
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("P_driver_id", searchDriverConductor);

			JasperDesign jasDes = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);
			// jasperReport = UnicodeShaper.sinhalaShapeUp(jasperReport);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

			jasperPrint = UnicodeShaper.shapeUp(jasperPrint);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", "BlackListLetter.pdf");

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("reportBytes", pdfByteArray);
			sessionMap.put("docType", "pdf");

			driverConductorRegistrationDTO.setDriverConductorId(searchDriverConductor);
			driverConductorRegistrationDTO.setNic(searchNic);
			driverConductorTrainingService.beanLinkMethod(driverConductorRegistrationDTO, sessionBackingBean.getLoginUser(), "Print Blacklist Letter", "Blacklist Driver / Conductor");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn);
		}

		return files;
	}

	// added for update driver conductor id by tharushi.e for avoid page
	// slowness
	public void onNicChange(AjaxBehaviorEvent event) {
		String dcId = null;
		disbledcId = false;
		driverConNoList = driverConductorTrainingService.getDCIDListByID(searchNic);
		//dcId = driverConductorTrainingService.getDriConducIdByNIC(searchNic);
		//setSearchDriverConductor(dcId);

	}

	// end

	//////////////// getter & setters /////////////////////

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public GrievanceManagementService getGrievanceManagementService() {
		return grievanceManagementService;
	}

	public void setGrievanceManagementService(GrievanceManagementService grievanceManagementService) {
		this.grievanceManagementService = grievanceManagementService;
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

	public String getDateFormatStr() {
		return dateFormatStr;
	}

	public void setDateFormatStr(String dateFormatStr) {
		this.dateFormatStr = dateFormatStr;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public DriverConductorTrainingService getDriverConductorTrainingService() {
		return driverConductorTrainingService;
	}

	public void setDriverConductorTrainingService(DriverConductorTrainingService driverConductorTrainingService) {
		this.driverConductorTrainingService = driverConductorTrainingService;
	}

	public String getSearchNic() {
		return searchNic;
	}

	public void setSearchNic(String searchNic) {
		this.searchNic = searchNic;
	}

	public String getSearchDriverConductor() {
		return searchDriverConductor;
	}

	public void setSearchDriverConductor(String searchDriverConductor) {
		this.searchDriverConductor = searchDriverConductor;
	}

	public List<DropDownDTO> getDriverCondList() {
		return driverCondList;
	}

	public void setDriverCondList(List<DropDownDTO> driverCondList) {
		this.driverCondList = driverCondList;
	}

	public List<DropDownDTO> getBlackListTypes() {
		return blackListTypes;
	}

	public void setBlackListTypes(List<DropDownDTO> blackListTypes) {
		this.blackListTypes = blackListTypes;
	}

	public DriverConductorBlacklistDTO getBlacklisterDTO() {
		return blacklisterDTO;
	}

	public void setBlacklisterDTO(DriverConductorBlacklistDTO blacklisterDTO) {
		this.blacklisterDTO = blacklisterDTO;
	}

	public boolean isIsDriver() {
		return isDriver;
	}

	public void setDriver(boolean isDriver) {
		this.isDriver = isDriver;
	}

	public boolean isIsConductor() {
		return isConductor;
	}

	public void setConductor(boolean isConductor) {
		this.isConductor = isConductor;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public List<DriverConductorBlacklistDTO> getBlacklisterDTOList() {
		return blacklisterDTOList;
	}

	public void setBlacklisterDTOList(List<DriverConductorBlacklistDTO> blacklisterDTOList) {
		this.blacklisterDTOList = blacklisterDTOList;
	}

	public List<DriverConductorBlacklistDTO> getSelectedBlacklisterList() {
		return selectedBlacklisterList;
	}

	public void setSelectedBlacklisterList(List<DriverConductorBlacklistDTO> selectedBlacklisterList) {
		this.selectedBlacklisterList = selectedBlacklisterList;
	}

	public List<DropDownDTO> getBlackListedDriCon() {
		return blackListedDriCon;
	}

	public void setBlackListedDriCon(List<DropDownDTO> blackListedDriCon) {
		this.blackListedDriCon = blackListedDriCon;
	}

	public List<DropDownDTO> getbPendingDriCon() {
		return bPendingDriCon;
	}

	public void setbPendingDriCon(List<DropDownDTO> bPendingDriCon) {
		this.bPendingDriCon = bPendingDriCon;
	}

	public String getApprovalRejectReason() {
		return approvalRejectReason;
	}

	public void setApprovalRejectReason(String approvalRejectReason) {
		this.approvalRejectReason = approvalRejectReason;
	}

	public DocumentManagementService getDocumentManagementService() {
		return documentManagementService;
	}

	public void setDocumentManagementService(DocumentManagementService documentManagementService) {
		this.documentManagementService = documentManagementService;
	}

	public List<DocumentManagementDTO> getMandatoryList() {
		return mandatoryList;
	}

	public void setMandatoryList(List<DocumentManagementDTO> mandatoryList) {
		this.mandatoryList = mandatoryList;
	}

	public List<DocumentManagementDTO> getOptionalList() {
		return optionalList;
	}

	public void setOptionalList(List<DocumentManagementDTO> optionalList) {
		this.optionalList = optionalList;
	}

	public boolean isDisDocument() {
		return disDocument;
	}

	public void setDisDocument(boolean disDocument) {
		this.disDocument = disDocument;
	}

	public boolean isEnableLetterPrint() {
		return enableLetterPrint;
	}

	public void setEnableLetterPrint(boolean enableLetterPrint) {
		this.enableLetterPrint = enableLetterPrint;
	}

	public boolean isDisbledcId() {
		return disbledcId;
	}

	public void setDisbledcId(boolean disbledcId) {
		this.disbledcId = disbledcId;
	}

	public List<DriverConductorRegistrationDTO> getDriverConNoList() {
		return driverConNoList;
	}

	public void setDriverConNoList(List<DriverConductorRegistrationDTO> driverConNoList) {
		this.driverConNoList = driverConNoList;
	}

	public DriverConductorRegistrationDTO getDriverConductorRegistrationDTO() {
		return driverConductorRegistrationDTO;
	}

	public void setDriverConductorRegistrationDTO(DriverConductorRegistrationDTO driverConductorRegistrationDTO) {
		this.driverConductorRegistrationDTO = driverConductorRegistrationDTO;
	}
	

}
