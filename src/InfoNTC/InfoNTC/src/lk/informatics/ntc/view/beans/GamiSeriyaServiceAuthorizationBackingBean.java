package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import lk.informatics.ntc.model.dto.GamiSeriyaServiceAgreementRenewalsDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;


@ManagedBean(name = "gamiSeriyaServiceAuthorizationBackingBean")
@ViewScoped
public class GamiSeriyaServiceAuthorizationBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
	//DTO
	GamiSeriyaServiceAgreementRenewalsDTO gamiSeriyaServiceAgreementRenewalsDTO;
	private SisuSeriyaDTO selectDTO;
	private SisuSeriyaDTO viewSelect;
	private SisuSeriyaDTO schoolInfoDTO;
	private SisuSeriyaDTO bankInfoDTO;
	
	//List
	private List<GamiSeriyaServiceAgreementRenewalsDTO> tenderRefNoList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> serviceRefNoList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> serviceAgreementNoList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> languageList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> serviceTypeList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> originTypeList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> destinationTypeList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> provinceList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> districtList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> divisionList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> statusList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> bankNameList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> bankBranchNameList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> dataList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	private List<GamiSeriyaServiceAgreementRenewalsDTO> permitNoofPRPTAList=new ArrayList<GamiSeriyaServiceAgreementRenewalsDTO>();
	
	private String selectedTenderRefNo;
	private String selectedServiceRefNo;
	private String selectedPermitNoofPRPTA;
	private String selectedServiceAgreementNo;
	private int activeTabIndex;
	private boolean showDetailsForm=false;
	private String selectedLangauge;
	
	public GamiSeriyaServiceAuthorizationBackingBean(){
		
	}
	
	@PostConstruct
	public void init() {
		gamiSeriyaServiceAgreementRenewalsDTO=new GamiSeriyaServiceAgreementRenewalsDTO();
		activeTabIndex =0;
		schoolInfoDTO=new SisuSeriyaDTO();
		bankInfoDTO=new SisuSeriyaDTO();
	}
	
	public void searchAction(){
		setShowDetailsForm(true);
		activeTabIndex=1;
	}
	
	public void clearAll(){
		
	}
	
	public void selectRow() {
		
	}
	
	public void viewAction(){
		
	}
	
	public void viewRdMapAct(){
		
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public GamiSeriyaServiceAgreementRenewalsDTO getGamiSeriyaServiceAgreementRenewalsDTO() {
		return gamiSeriyaServiceAgreementRenewalsDTO;
	}

	public void setGamiSeriyaServiceAgreementRenewalsDTO(
			GamiSeriyaServiceAgreementRenewalsDTO gamiSeriyaServiceAgreementRenewalsDTO) {
		this.gamiSeriyaServiceAgreementRenewalsDTO = gamiSeriyaServiceAgreementRenewalsDTO;
	}

	public SisuSeriyaDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(SisuSeriyaDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public SisuSeriyaDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(SisuSeriyaDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getTenderRefNoList() {
		return tenderRefNoList;
	}

	public void setTenderRefNoList(List<GamiSeriyaServiceAgreementRenewalsDTO> tenderRefNoList) {
		this.tenderRefNoList = tenderRefNoList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getServiceRefNoList() {
		return serviceRefNoList;
	}

	public void setServiceRefNoList(List<GamiSeriyaServiceAgreementRenewalsDTO> serviceRefNoList) {
		this.serviceRefNoList = serviceRefNoList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getServiceAgreementNoList() {
		return serviceAgreementNoList;
	}

	public void setServiceAgreementNoList(List<GamiSeriyaServiceAgreementRenewalsDTO> serviceAgreementNoList) {
		this.serviceAgreementNoList = serviceAgreementNoList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(List<GamiSeriyaServiceAgreementRenewalsDTO> languageList) {
		this.languageList = languageList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<GamiSeriyaServiceAgreementRenewalsDTO> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getOriginTypeList() {
		return originTypeList;
	}

	public void setOriginTypeList(List<GamiSeriyaServiceAgreementRenewalsDTO> originTypeList) {
		this.originTypeList = originTypeList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getDestinationTypeList() {
		return destinationTypeList;
	}

	public void setDestinationTypeList(List<GamiSeriyaServiceAgreementRenewalsDTO> destinationTypeList) {
		this.destinationTypeList = destinationTypeList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<GamiSeriyaServiceAgreementRenewalsDTO> provinceList) {
		this.provinceList = provinceList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<GamiSeriyaServiceAgreementRenewalsDTO> districtList) {
		this.districtList = districtList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getDivisionList() {
		return divisionList;
	}

	public void setDivisionList(List<GamiSeriyaServiceAgreementRenewalsDTO> divisionList) {
		this.divisionList = divisionList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<GamiSeriyaServiceAgreementRenewalsDTO> statusList) {
		this.statusList = statusList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getBankNameList() {
		return bankNameList;
	}

	public void setBankNameList(List<GamiSeriyaServiceAgreementRenewalsDTO> bankNameList) {
		this.bankNameList = bankNameList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getBankBranchNameList() {
		return bankBranchNameList;
	}

	public void setBankBranchNameList(List<GamiSeriyaServiceAgreementRenewalsDTO> bankBranchNameList) {
		this.bankBranchNameList = bankBranchNameList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<GamiSeriyaServiceAgreementRenewalsDTO> dataList) {
		this.dataList = dataList;
	}

	public List<GamiSeriyaServiceAgreementRenewalsDTO> getPermitNoofPRPTAList() {
		return permitNoofPRPTAList;
	}

	public void setPermitNoofPRPTAList(List<GamiSeriyaServiceAgreementRenewalsDTO> permitNoofPRPTAList) {
		this.permitNoofPRPTAList = permitNoofPRPTAList;
	}

	public String getSelectedTenderRefNo() {
		return selectedTenderRefNo;
	}

	public void setSelectedTenderRefNo(String selectedTenderRefNo) {
		this.selectedTenderRefNo = selectedTenderRefNo;
	}

	public String getSelectedServiceRefNo() {
		return selectedServiceRefNo;
	}

	public void setSelectedServiceRefNo(String selectedServiceRefNo) {
		this.selectedServiceRefNo = selectedServiceRefNo;
	}

	public String getSelectedPermitNoofPRPTA() {
		return selectedPermitNoofPRPTA;
	}

	public void setSelectedPermitNoofPRPTA(String selectedPermitNoofPRPTA) {
		this.selectedPermitNoofPRPTA = selectedPermitNoofPRPTA;
	}

	public String getSelectedServiceAgreementNo() {
		return selectedServiceAgreementNo;
	}

	public void setSelectedServiceAgreementNo(String selectedServiceAgreementNo) {
		this.selectedServiceAgreementNo = selectedServiceAgreementNo;
	}

	public int getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(int activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

	public boolean isShowDetailsForm() {
		return showDetailsForm;
	}

	public void setShowDetailsForm(boolean showDetailsForm) {
		this.showDetailsForm = showDetailsForm;
	}

	public String getSelectedLangauge() {
		return selectedLangauge;
	}

	public void setSelectedLangauge(String selectedLangauge) {
		this.selectedLangauge = selectedLangauge;
	}

	public SisuSeriyaDTO getSchoolInfoDTO() {
		return schoolInfoDTO;
	}

	public void setSchoolInfoDTO(SisuSeriyaDTO schoolInfoDTO) {
		this.schoolInfoDTO = schoolInfoDTO;
	}

	public SisuSeriyaDTO getBankInfoDTO() {
		return bankInfoDTO;
	}

	public void setBankInfoDTO(SisuSeriyaDTO bankInfoDTO) {
		this.bankInfoDTO = bankInfoDTO;
	}
	
}
