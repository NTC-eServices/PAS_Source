package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.DriverConductorRegistrationDTO;
import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.SisuSariyaService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "subsidyManagementInforBackingBean")
@ViewScoped
public class subsidyManagementInformationBackingBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private SisuSariyaService sisuSariyaService;
	private List<SisuSeriyaDTO> getServiceTypeList = new ArrayList<SisuSeriyaDTO>();
	private SisuSeriyaDTO searchDto;

	private boolean disableField, disableFieldNP,disableFieldRN,disableFieldSR,disableFieldSA,disableFieldVN;
	private List<SisuSeriyaDTO> dropDownListData = new ArrayList<>();
	private List<SisuSeriyaDTO> vehiDropDownListData = new ArrayList<>();
	private List<SisuSeriyaDTO> nameDropDownListData = new ArrayList<>();
	private List<SisuSeriyaDTO> reqDropDownListData = new ArrayList<>();
	private List<SisuSeriyaDTO> refDropDownListData = new ArrayList<>();
	private List<SisuSeriyaDTO> agreDropDownListData = new ArrayList<>();
	private List<SisuSeriyaDTO> searchedDataList = new ArrayList<>();
	private String sucessMsg, errorMsg;

	@PostConstruct
	public void init() {
		sisuSariyaService = (SisuSariyaService) SpringApplicationContex.getBean("sisuSariyaService");
		searchDto = new SisuSeriyaDTO();
		loadData();
	}

	public void loadData() {
		getServiceTypeList = sisuSariyaService.serviceTypeDropDown();
		disableFieldVN=true;
		disableFieldRN=true;
		disableFieldSR=true;
		disableFieldSA=true;
		disableFieldNP = true;
	}

	public void searchButtonAction() {
		if (searchDto.getServiceTypeCode() != null && !searchDto.getServiceTypeCode().isEmpty()) {
			if (searchDto.getBusRegNo() != null && !searchDto.getBusRegNo().trim().isEmpty()
					|| searchDto.getNameOfOperator() != null && !searchDto.getNameOfOperator().trim().isEmpty()
					|| searchDto.getRequestNo() != null && !searchDto.getRequestNo().trim().isEmpty()
					|| searchDto.getServiceRefNo() != null && !searchDto.getServiceRefNo().trim().isEmpty()
					|| searchDto.getServiceAgreementNo() != null
							&& !searchDto.getServiceAgreementNo().trim().isEmpty()) {
				searchedDataList = sisuSariyaService.getSearchedData(searchDto);
				

			} else {
				errorMsg = "Please select a field for search.";
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else {
			errorMsg = "Please select a subsidy Type.";
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		}
	}

	public void clearButtonAction() {
		searchDto = new SisuSeriyaDTO();
		searchedDataList = new ArrayList<>();
		loadData();

	}

	public void onsubsidyTypeChange() {
		disableFieldNP=false;
		disableFieldRN=false;
		disableFieldSR=false;
		disableFieldSA=false;
		disableFieldVN=false;
		if (searchDto.getServiceTypeCode() != null && !searchDto.getServiceTypeCode().isEmpty()) {

			dropDownListData = sisuSariyaService.getdropDownDataByService(searchDto.getServiceTypeCode());
			vehiDropDownListData= sisuSariyaService.getVehiNumByService(searchDto.getServiceTypeCode());
			nameDropDownListData= sisuSariyaService.getNamesByService(searchDto.getServiceTypeCode());
			reqDropDownListData =sisuSariyaService.getReqNoByService(searchDto.getServiceTypeCode());
			 refDropDownListData = sisuSariyaService.getRefNoByService(searchDto.getServiceTypeCode());
			 agreDropDownListData=sisuSariyaService.getAgreeNoByService(searchDto.getServiceTypeCode());

		}

	}

	public void onvalChange(String s) {
		if (s.equals("1")) {
			disableFieldNP=true;
			disableFieldRN=true;
			disableFieldSR=true;
			disableFieldSA=true;
			disableFieldVN=false;
		}
		if (s.equals("2")) {
			disableFieldVN=true;
			disableFieldRN=true;
			disableFieldSR=true;
			disableFieldSA=true;
			disableFieldNP=false;

		}
		if (s.equals("3")) {
			disableFieldVN=true;
			disableFieldRN=false;
			disableFieldSR=true;
			disableFieldSA=true;
			disableFieldNP=true;

		}
		if (s.equals("4")) {
			disableFieldVN=true;
			disableFieldRN=true;
			disableFieldSR=false;
			disableFieldSA=true;
			disableFieldNP=true;

		}
		if (s.equals("5")) {
			disableFieldVN=true;
			disableFieldRN=true;
			disableFieldSR=true;
			disableFieldSA=false;
			disableFieldNP=true;


		}

	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public SisuSariyaService getSisuSariyaService() {
		return sisuSariyaService;
	}

	public void setSisuSariyaService(SisuSariyaService sisuSariyaService) {
		this.sisuSariyaService = sisuSariyaService;
	}

	public List<SisuSeriyaDTO> getGetServiceTypeList() {
		return getServiceTypeList;
	}

	public void setGetServiceTypeList(List<SisuSeriyaDTO> getServiceTypeList) {
		this.getServiceTypeList = getServiceTypeList;
	}

	public boolean isDisableField() {
		return disableField;
	}

	public void setDisableField(boolean disableField) {
		this.disableField = disableField;
	}

	public SisuSeriyaDTO getSearchDto() {
		return searchDto;
	}

	public void setSearchDto(SisuSeriyaDTO searchDto) {
		this.searchDto = searchDto;
	}

	public List<SisuSeriyaDTO> getDropDownListData() {
		return dropDownListData;
	}

	public void setDropDownListData(List<SisuSeriyaDTO> dropDownListData) {
		this.dropDownListData = dropDownListData;
	}

	public boolean isDisableFieldNP() {
		return disableFieldNP;
	}

	public void setDisableFieldNP(boolean disableFieldNP) {
		this.disableFieldNP = disableFieldNP;
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

	public List<SisuSeriyaDTO> getSearchedDataList() {
		return searchedDataList;
	}

	public void setSearchedDataList(List<SisuSeriyaDTO> searchedDataList) {
		this.searchedDataList = searchedDataList;
	}

	public boolean isDisableFieldRN() {
		return disableFieldRN;
	}

	public void setDisableFieldRN(boolean disableFieldRN) {
		this.disableFieldRN = disableFieldRN;
	}

	public boolean isDisableFieldSR() {
		return disableFieldSR;
	}

	public void setDisableFieldSR(boolean disableFieldSR) {
		this.disableFieldSR = disableFieldSR;
	}

	public boolean isDisableFieldSA() {
		return disableFieldSA;
	}

	public void setDisableFieldSA(boolean disableFieldSA) {
		this.disableFieldSA = disableFieldSA;
	}

	public boolean isDisableFieldVN() {
		return disableFieldVN;
	}

	public void setDisableFieldVN(boolean disableFieldVN) {
		this.disableFieldVN = disableFieldVN;
	}

	public List<SisuSeriyaDTO> getVehiDropDownListData() {
		return vehiDropDownListData;
	}

	public void setVehiDropDownListData(List<SisuSeriyaDTO> vehiDropDownListData) {
		this.vehiDropDownListData = vehiDropDownListData;
	}

	public List<SisuSeriyaDTO> getNameDropDownListData() {
		return nameDropDownListData;
	}

	public void setNameDropDownListData(List<SisuSeriyaDTO> nameDropDownListData) {
		this.nameDropDownListData = nameDropDownListData;
	}

	public List<SisuSeriyaDTO> getReqDropDownListData() {
		return reqDropDownListData;
	}

	public void setReqDropDownListData(List<SisuSeriyaDTO> reqDropDownListData) {
		this.reqDropDownListData = reqDropDownListData;
	}

	public List<SisuSeriyaDTO> getRefDropDownListData() {
		return refDropDownListData;
	}

	public void setRefDropDownListData(List<SisuSeriyaDTO> refDropDownListData) {
		this.refDropDownListData = refDropDownListData;
	}

	public List<SisuSeriyaDTO> getAgreDropDownListData() {
		return agreDropDownListData;
	}

	public void setAgreDropDownListData(List<SisuSeriyaDTO> agreDropDownListData) {
		this.agreDropDownListData = agreDropDownListData;
	}
	

}
