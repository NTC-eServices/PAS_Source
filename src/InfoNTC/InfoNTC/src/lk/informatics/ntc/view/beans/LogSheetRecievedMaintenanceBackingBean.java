package lk.informatics.ntc.view.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.SisuSeriyaDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.LogsheetMaintenanceService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "logSheetRecievBackingBean")
@ViewScoped
public class LogSheetRecievedMaintenanceBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	// DTO
	private SisuSeriyaDTO sisuSeriyaDto, selectDTO, updateDTO;
	private List<SisuSeriyaDTO> showSearchData, updatedList;
	private SisuSeriyaDTO showServiceAgreementNo;
	// Service
	private LogsheetMaintenanceService logSheetServic;

	// Fields
	private List<SisuSeriyaDTO> serviceType = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> serviceReferenceNo = new ArrayList<SisuSeriyaDTO>();
	private List<SisuSeriyaDTO> serviceAgreementNo = new ArrayList<SisuSeriyaDTO>();
	private List<String> busNoList = new ArrayList<String>();
	private List<String> operatorNameList = new ArrayList<String>();
	private String rejectReason, alertMSG, successMessage, errorMessage, loginUser, logSheetYear, selectServiceRefNo,
			selectServiceNo;
	private int logRefNo, month;
	private CommonService commonService;
	// Disable Field before Search
	private boolean enableServiceType, disabledServiceRefNo, disabledServiceAgreementNo, disabledYear, disabledMonth,
			disabledDataTableEditableColumn, createMode, viewMode, renderButton;
	
	private List<String> monthList = new ArrayList<String>();
	private List<String> monthListFinal = new ArrayList<String>();
	private String monthName;
	private String monthNameAlradyhave;
	private String warningMsg;
	private String errorMessageNew;

	@PostConstruct
	public void init() {
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		logSheetServic = (LogsheetMaintenanceService) SpringApplicationContex.getBean("logSheetMaintenanceService");
		sisuSeriyaDto = new SisuSeriyaDTO();
		selectDTO = new SisuSeriyaDTO();

		loadValues();

		createMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN110", "C");
		viewMode = commonService.checkAccessPermission(sessionBackingBean.getLoginUser(), "FN110", "V");

		if (createMode) {
			renderButton = true;
		}

	}

	public void loadValues() {

		serviceType = logSheetServic.getServiceTypeForDropdown();

		logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		disabledServiceRefNo = true;
		disabledServiceAgreementNo = true;
		disabledYear = true;
		disabledMonth = true;
		disabledDataTableEditableColumn = true;

		showServiceAgreementNo = new SisuSeriyaDTO();

		showSearchData = new ArrayList<>();
		showSearchData = logSheetServic.showDefaultSearchedData();
		monthList = new ArrayList<String>();
		
		monthName = null;
	}

	public void yearValidator() {

		if (logSheetYear != null && !logSheetYear.isEmpty()) {
			Pattern ptr = Pattern.compile("^[0-9]{4}$");
			boolean valid = ptr.matcher(logSheetYear).matches();
			if (valid) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy");
				Date date = new Date();
				String year = dateFormat.format(date);
				int curYear = Integer.parseInt(year);

				int manuYear = Integer.parseInt(logSheetYear);

			} else {
				setErrorMessage("Invalid Year");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				logSheetYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
			}

		}

	}

	public void ajaxDisableBeforeSearch() {
		serviceReferenceNo = logSheetServic.getServiceRefNoForDropdown(sisuSeriyaDto);
		if (!sisuSeriyaDto.getServiceTypeCode().equals("") && sisuSeriyaDto.getServiceTypeCode() != null) {
			if (sisuSeriyaDto.getServiceTypeCode().equals("S01")) {
				serviceAgreementNo = new ArrayList<SisuSeriyaDTO>();
				serviceAgreementNo = logSheetServic.getServiceAgtNoForDropdown(sisuSeriyaDto);
				busNoList = new ArrayList<String>();
				operatorNameList = new ArrayList<String>();
				busNoList = logSheetServic.getBusNumberListForSisu("S01");
				operatorNameList= logSheetServic.getNameListForSisu("S01");
				
				
			} else if (sisuSeriyaDto.getServiceTypeCode().equals("S02")) {
				serviceAgreementNo = new ArrayList<SisuSeriyaDTO>();
				serviceAgreementNo = logSheetServic.getServiceAgtNoForGamiSeriyaDropdown(sisuSeriyaDto);
				busNoList = new ArrayList<String>();
				operatorNameList = new ArrayList<String>();
				busNoList = logSheetServic.getBusNumberListForGami("S02");
				operatorNameList= logSheetServic.getNameListForGami("S02");
			} else if (sisuSeriyaDto.getServiceTypeCode().equals("S03")) {
				serviceAgreementNo = new ArrayList<SisuSeriyaDTO>();
				serviceAgreementNo = logSheetServic.getServiceAgtNoForNisiSeriyaDropdown(sisuSeriyaDto);
				busNoList = new ArrayList<String>();
				operatorNameList = new ArrayList<String>();
				busNoList = logSheetServic.getBusNumberListForNisi("S03");
				operatorNameList= logSheetServic.getNameListForNisi("S03");
			} else {

			}
		} else {

		}

		enableServiceType = true;
		disabledServiceRefNo = false;
		disabledServiceAgreementNo = false;
		disabledYear = false;
		disabledMonth = false;

	}
	
	

	public void ajaxFilterAgrementNo() {
		if (sisuSeriyaDto.getServiceRefNo() != null && !sisuSeriyaDto.getServiceRefNo().trim().isEmpty()) {
			if (sisuSeriyaDto.getServiceTypeCode().equals("S01")) {
				serviceAgreementNo = new ArrayList<SisuSeriyaDTO>();
				serviceAgreementNo = logSheetServic.getServiceAgtNoByRefNo(sisuSeriyaDto);
			} else if (sisuSeriyaDto.getServiceTypeCode().equals("S02")) {
				serviceAgreementNo = new ArrayList<SisuSeriyaDTO>();
				serviceAgreementNo = logSheetServic.getServiceAgtNoByRefNo(sisuSeriyaDto);
			} else if (sisuSeriyaDto.getServiceTypeCode().equals("S03")) {
				serviceAgreementNo = new ArrayList<SisuSeriyaDTO>();
				serviceAgreementNo = logSheetServic.getServiceAgtNoByRefNo(sisuSeriyaDto);
			}
		}
	}

	public void searchAction() {

		if (sisuSeriyaDto.getServiceTypeCode() != null && !sisuSeriyaDto.getServiceTypeCode().trim().isEmpty()) {

			if ((sisuSeriyaDto.getServiceRefNo() == null || sisuSeriyaDto.getServiceRefNo().trim().equalsIgnoreCase(""))
					&& (sisuSeriyaDto.getServiceNo() == null
							|| sisuSeriyaDto.getServiceNo().trim().equalsIgnoreCase(""))
					&& (sisuSeriyaDto.getStringMonth() == null
							|| sisuSeriyaDto.getStringMonth().trim().equalsIgnoreCase(""))
					&& (logSheetYear == null || logSheetYear.trim().isEmpty())) {

				setErrorMessage("Please Select At Least One Filed ");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

			} else {

				if(logSheetYear != null && !logSheetYear.trim().isEmpty()) {
					if (sisuSeriyaDto.getServiceTypeCode().equals("S01")) {
						showSearchData = new ArrayList<>();
						showSearchData = logSheetServic.showSearchedData(sisuSeriyaDto, logSheetYear);
					} else if (sisuSeriyaDto.getServiceTypeCode().equals("S02")) {
						showSearchData = new ArrayList<>();
						showSearchData = logSheetServic.showSearchedDataForGamiSeriya(sisuSeriyaDto, logSheetYear);
					} else if (sisuSeriyaDto.getServiceTypeCode().equals("S03")) {
						showSearchData = new ArrayList<>();
						showSearchData = logSheetServic.showSearchedDataForNisiSeriya(sisuSeriyaDto, logSheetYear);
					} else {

					}

					if (showSearchData.isEmpty()) {

						setErrorMessage("No Data Found!");
						RequestContext.getCurrentInstance().update("errorMSG");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
						RequestContext.getCurrentInstance().update("formOne");

					}
				}else {
					setErrorMessage("Please Enter Year");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}
				

			}

		} else {
			setErrorMessage("Please Select Service Type");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public void clearData() {
		sisuSeriyaDto = new SisuSeriyaDTO();
		disabledMonth = true;
		disabledServiceAgreementNo = true;
		disabledServiceRefNo = true;
		disabledYear = true;

		/* Added By Gayathra */
		showSearchData = new ArrayList<>();
		showSearchData = logSheetServic.showDefaultSearchedData();

	}

	/*
	 * In Save Method, If user ticks the check-box of the record in the grid,
	 * next to Log Reference No, its manadatory to enter Month and Receive Date.
	 */

	/* Added By Gayathra */
	public void saveDate() {

		RequestContext.getCurrentInstance().execute("PF('comfirmMSG').hide()");
		
		/* Added By Dhananjika 29/02/2024 */
		RequestContext.getCurrentInstance().execute("PF('comfirmMSGNew').hide()");

		if (logSheetServic.updateLogSheetReceivedData(selectDTO, sessionBackingBean.getLoginUser(), this.updatedList)) {

			removeTick();
			setSuccessMessage("Successfully Updated.");
			RequestContext.getCurrentInstance().update("successMSG");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");
			RequestContext.getCurrentInstance().update("frmsuccess");
			
		} else {
			setErrorMessage("Update Fail");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("formOne");
		}
	}

	/* Added By Gayathra */
	
	public void saveLogSheetRecievedData() {

		if (!showSearchData.isEmpty()) {

			int result = dataCheckingMethod(showSearchData);
			if (result == 0) {

				setErrorMessage("No Value Found To Update");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				RequestContext.getCurrentInstance().update("formOne");

			} else if (result == 1) {

				setErrorMessage("All Ticked Row's Data Need To Be Entered");
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				RequestContext.getCurrentInstance().update("formOne");

			} else if (result == 2) {

				RequestContext.getCurrentInstance().execute("PF('comfirmMSG').show()");

			}

			/* Added By Dhananjika 29/02/2024 */
			else if (result == 3) {

				if (updatedList.size() != 0) {
					setWarningMsg("Do you want to update without already received log sheet?");
					RequestContext.getCurrentInstance().update("comfirmMSGNew");
					RequestContext.getCurrentInstance().execute("PF('comfirmMSGNew').show()");
				}

				String name = setMonthNames(monthListFinal);
				setErrorMessage("Already Received Log sheet For " + name);
				RequestContext.getCurrentInstance().update("errorMSG");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				RequestContext.getCurrentInstance().update("formOne");

			} else if (result == 4) {

				result4SaveList(showSearchData);

				if((monthName == null || monthName.isEmpty() || monthName.trim().equals(""))
						&& (monthNameAlradyhave == null || monthNameAlradyhave.isEmpty()
								|| monthNameAlradyhave.trim().equals(""))) {
					
					setErrorMessage("Error occured while saving data");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					RequestContext.getCurrentInstance().update("formOne");
				}
				else if ((monthName == null || monthName.isEmpty() || monthName.trim().equals(""))
						&& (monthNameAlradyhave != null || !monthNameAlradyhave.isEmpty()
								|| !monthNameAlradyhave.trim().equals(""))) {
					if (updatedList.size() != 0) {
						setWarningMsg("Do you want to update without already received log sheet?");
						RequestContext.getCurrentInstance().update("comfirmMSGNew");
						RequestContext.getCurrentInstance().execute("PF('comfirmMSGNew').show()");
					}

					setErrorMessage("Already Received Log sheet For " + monthNameAlradyhave);
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					RequestContext.getCurrentInstance().update("formOne");
				} 
				else if ((monthNameAlradyhave == null || monthNameAlradyhave.isEmpty()
						|| monthNameAlradyhave.trim().equals(""))
						&& (monthName != null || !monthName.isEmpty() || !monthName.trim().equals(""))) {
					if (updatedList.size() != 0) {
						setWarningMsg("Do you want to update without duplicate log sheet?");
						RequestContext.getCurrentInstance().update("comfirmMSGNew");
						RequestContext.getCurrentInstance().execute("PF('comfirmMSGNew').show()");
					}

					setErrorMessage(monthName + " Has more than one log sheets");
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					RequestContext.getCurrentInstance().update("formOne");
				} 
				else {
					if (updatedList.size() != 0) {
						setWarningMsg("Do you want to update without duplicate & already received log sheet?");
						RequestContext.getCurrentInstance().update("comfirmMSGNew");
						RequestContext.getCurrentInstance().execute("PF('comfirmMSGNew').show()");
					}

					setErrorMessage(monthName + " Has More Than One Log Sheets And Already Received Log sheet For "
							+ monthNameAlradyhave);
					RequestContext.getCurrentInstance().update("errorMSG");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					RequestContext.getCurrentInstance().update("formOne");
				}
			}

		} else {
			setErrorMessage("No Data Found For Save");
			RequestContext.getCurrentInstance().update("errorMSG");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			RequestContext.getCurrentInstance().update("formOne");
		}

	}

	public int dataCheckingMethod(List<SisuSeriyaDTO> myList) {
		monthListFinal = new ArrayList<String>();
		int dataEmpty = 0;
		updatedList = new ArrayList<>();
		monthList = new ArrayList<String>();
		List<SisuSeriyaDTO> list = new ArrayList<>();
		List<String> checkMonth = new ArrayList<>();
		List<String> checkRef = new ArrayList<>();
		List<Integer> checkYear = new ArrayList<>();
		List<String> refNoList = new ArrayList<>();
		LinkedHashMap<String, List<String>> alreadyRecievedDetails = new LinkedHashMap<>();

		for (SisuSeriyaDTO dto : myList) {

			if (dto.isLogRefCheck() == true) {

				if (dto.getReceivedDate() != null && dto.getStringMonth() != null
						&& !dto.getStringMonth().trim().equals("")) {

					updateDTO = new SisuSeriyaDTO(dto.getStringMonth(), dto.getReceivedDate(), dto.getLogRefNo(), dto.getServiceRefNo(), dto.getYear());
					list.add(updateDTO); 
					
				/* Added By Dhananjika 29/02/2024 */
					String month = logSheetServic.checkDataHaveForMonth(updateDTO, sisuSeriyaDto.getServiceTypeCode());
					
					if(month != null) {
						monthList.add(month);						
						refNoList.add(dto.getServiceRefNo());
						
						if(!alreadyRecievedDetails.containsKey(dto.getServiceRefNo())) {
							List<String> duplicateList = new ArrayList<>();
							duplicateList.add(dto.getStringMonth());
							alreadyRecievedDetails.put(dto.getServiceRefNo(), duplicateList);
						}else {
							List<String> duplicateList = alreadyRecievedDetails.get(dto.getServiceRefNo());
							if(!duplicateList.contains(dto.getStringMonth())) {
								duplicateList.add(dto.getStringMonth());
							}	
						}
					}
									
					if(!checkMonth.contains(dto.getStringMonth()) || !checkRef.contains(dto.getServiceRefNo()) || !checkYear.contains(dto.getYear())) {
						checkMonth.add(dto.getStringMonth());
						checkRef.add(dto.getServiceRefNo());
						checkYear.add(dto.getYear());
					}else {
						return dataEmpty = 4;
					}
					
					if(monthList.size() != 0) {
						dataEmpty = 3;
						
						for (SisuSeriyaDTO data : list) {
					        if (!monthList.contains(data.getStringMonth()) || !refNoList.contains(data.getServiceRefNo())) {
					        	updatedList.add(data);
					        }
					    }
					}else {
						dataEmpty = 2;
						updatedList.add(updateDTO);
					}
					
				} else {
					return dataEmpty = 1;
				}

			}

		}

		for(Map.Entry<String, List<String>> set : alreadyRecievedDetails.entrySet()) {
			String key = set.getKey();
			List<String> value = set.getValue();
			
			String months = setMonthNames(value);
			monthListFinal.add(key + " - " + months);
		}
		
		return dataEmpty;
	}
	
	public void result4SaveList(List<SisuSeriyaDTO> myList) {
		updatedList = new ArrayList<>();
		List<SisuSeriyaDTO> notSavedDataList = new ArrayList<>();
		LinkedHashMap<String, List<String>> logSheetDetails = new LinkedHashMap<>();
		LinkedHashMap<String, List<String>> duplicateMonthsDetails = new LinkedHashMap<>();
		List<String> monthNames = new ArrayList<String>();
		LinkedHashMap<String, List<String>> alreadyRecievedMonth = new LinkedHashMap<>();
		List<String> monthNamesAlreadyHave = new ArrayList<String>();
		
		for(SisuSeriyaDTO dto : myList) {
			if(dto.isLogRefCheck()) {
				if (dto.getReceivedDate() != null && dto.getStringMonth() != null
						&& !dto.getStringMonth().trim().equals("")) {
					String monthString = dto.getStringMonth();
					String refNo = dto.getServiceRefNo();
					
					updateDTO = new SisuSeriyaDTO(dto.getStringMonth(), dto.getReceivedDate(), dto.getLogRefNo(), dto.getServiceRefNo(), dto.getYear());
					String month = logSheetServic.checkDataHaveForMonth(updateDTO, sisuSeriyaDto.getServiceTypeCode());
					
					if(month == null) {
						if(!logSheetDetails.containsKey(refNo)) {
							List<String> monthList = new ArrayList<String>();
							monthList.add(monthString);
							logSheetDetails.put(refNo, monthList);
						} else {
							List<String> monthList = logSheetDetails.get(refNo);
							if(monthList.contains(monthString)) {
								monthList.remove(monthString);
								
								if(!duplicateMonthsDetails.containsKey(refNo)) {
									List<String> duplicateList = new ArrayList<>();
									duplicateList.add(monthString);
									duplicateMonthsDetails.put(refNo, duplicateList);
								}else {
									List<String> duplicateList = duplicateMonthsDetails.get(refNo);
									if(!duplicateList.contains(monthString)) {
										duplicateList.add(monthString);
									}									
								}
							}else {
								monthList.add(monthString);
							}
						}
						notSavedDataList.add(updateDTO);
					}else {
						if(!alreadyRecievedMonth.containsKey(refNo)) {
							List<String> duplicateList = new ArrayList<>();
							duplicateList.add(monthString);
							alreadyRecievedMonth.put(refNo, duplicateList);
						}else {
							List<String> duplicateList = alreadyRecievedMonth.get(refNo);
							if(!duplicateList.contains(monthString)) {
								duplicateList.add(monthString);
							}	
						}
					}
				}
			}
		}
		
		for(SisuSeriyaDTO data : notSavedDataList) {
			for (Map.Entry<String, List<String>> set : logSheetDetails.entrySet()) {
				String key = set.getKey();
				List<String> value = set.getValue();
				
				if(key.equals(data.getServiceRefNo()) && value.contains(data.getStringMonth())) {
					updatedList.add(data);
				}
			}
		}
		
		for(Map.Entry<String, List<String>> set : duplicateMonthsDetails.entrySet()) {
			String key = set.getKey();
			List<String> value = set.getValue();
			
			String months = setMonthNames(value);
			monthNames.add(key + " - " + months);
		}
		
		for(Map.Entry<String, List<String>> set : alreadyRecievedMonth.entrySet()) {
			String key = set.getKey();
			List<String> value = set.getValue();
			
			String months = setMonthNames(value);
			monthNamesAlreadyHave.add(key + " - " + months);
		}
		
		monthNameAlradyhave = setMonthNames(monthNamesAlreadyHave);
		monthName = setMonthNames(monthNames);
		
	}

	
	public String setMonthNames(List<String> monthList) {
		String name = null;
		
		StringBuilder result = new StringBuilder();
		for (String month : monthList) {
		    result.append(month).append(", ");
		}

		if (result.length() > 0) {
		    result.setLength(result.length() - 2);
		}

		name = result.toString();
		return name;
	}
	
	public void removeTick() {
		for(SisuSeriyaDTO dto : showSearchData) {
			if (dto.isLogRefCheck() == true) {
				dto.setLogRefCheck(false);
			}
		}
	}
	
	public void cancelLogSheetRecievedData() {
		sisuSeriyaDto.setStringMonth(null);
		sisuSeriyaDto.setServiceRefNo(null);
		sisuSeriyaDto.setServiceNo(null);

		showSearchData = new ArrayList<>();

	}

	public SisuSeriyaDTO getSisuSeriyaDto() {
		return sisuSeriyaDto;
	}

	public void setSisuSeriyaDto(SisuSeriyaDTO sisuSeriyaDto) {
		this.sisuSeriyaDto = sisuSeriyaDto;
	}

	public LogsheetMaintenanceService getLogSheetServic() {
		return logSheetServic;
	}

	public void setLogSheetServic(LogsheetMaintenanceService logSheetServic) {
		this.logSheetServic = logSheetServic;
	}

	public List<SisuSeriyaDTO> getServiceType() {
		return serviceType;
	}

	public void setServiceType(List<SisuSeriyaDTO> serviceType) {
		this.serviceType = serviceType;
	}

	public List<SisuSeriyaDTO> getServiceReferenceNo() {
		return serviceReferenceNo;
	}

	public void setServiceReferenceNo(List<SisuSeriyaDTO> serviceReferenceNo) {
		this.serviceReferenceNo = serviceReferenceNo;
	}

	public List<SisuSeriyaDTO> getServiceAgreementNo() {
		return serviceAgreementNo;
	}

	public void setServiceAgreementNo(List<SisuSeriyaDTO> serviceAgreementNo) {
		this.serviceAgreementNo = serviceAgreementNo;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getLogSheetYear() {
		return logSheetYear;
	}

	public void setLogSheetYear(String logSheetYear) {
		this.logSheetYear = logSheetYear;
	}

	public int getLogRefNo() {
		return logRefNo;
	}

	public void setLogRefNo(int logRefNo) {
		this.logRefNo = logRefNo;
	}

	public boolean isEnableServiceType() {
		return enableServiceType;
	}

	public void setEnableServiceType(boolean enableServiceType) {
		this.enableServiceType = enableServiceType;
	}

	public boolean isDisabledServiceRefNo() {
		return disabledServiceRefNo;
	}

	public void setDisabledServiceRefNo(boolean disabledServiceRefNo) {
		this.disabledServiceRefNo = disabledServiceRefNo;
	}

	public boolean isDisabledServiceAgreementNo() {
		return disabledServiceAgreementNo;
	}

	public void setDisabledServiceAgreementNo(boolean disabledServiceAgreementNo) {
		this.disabledServiceAgreementNo = disabledServiceAgreementNo;
	}

	public boolean isDisabledYear() {
		return disabledYear;
	}

	public void setDisabledYear(boolean disabledYear) {
		this.disabledYear = disabledYear;
	}

	public boolean isDisabledMonth() {
		return disabledMonth;
	}

	public void setDisabledMonth(boolean disabledMonth) {
		this.disabledMonth = disabledMonth;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
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

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public List<SisuSeriyaDTO> getShowSearchData() {
		return showSearchData;
	}

	public void setShowSearchData(List<SisuSeriyaDTO> showSearchData) {
		this.showSearchData = showSearchData;
	}

	public SisuSeriyaDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(SisuSeriyaDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public String getSelectServiceRefNo() {
		return selectServiceRefNo;
	}

	public void setSelectServiceRefNo(String selectServiceRefNo) {
		this.selectServiceRefNo = selectServiceRefNo;
	}

	public String getSelectServiceNo() {
		return selectServiceNo;
	}

	public void setSelectServiceNo(String selectServiceNo) {
		this.selectServiceNo = selectServiceNo;
	}

	public SisuSeriyaDTO getShowServiceAgreementNo() {
		return showServiceAgreementNo;
	}

	public void setShowServiceAgreementNo(SisuSeriyaDTO showServiceAgreementNo) {
		this.showServiceAgreementNo = showServiceAgreementNo;
	}

	public boolean isDisabledDataTableEditableColumn() {
		return disabledDataTableEditableColumn;
	}

	public void setDisabledDataTableEditableColumn(boolean disabledDataTableEditableColumn) {
		this.disabledDataTableEditableColumn = disabledDataTableEditableColumn;
	}

	public List<SisuSeriyaDTO> getUpdatedList() {
		return updatedList;
	}

	public void setUpdatedList(List<SisuSeriyaDTO> updatedList) {
		this.updatedList = updatedList;
	}

	public SisuSeriyaDTO getUpdateDTO() {
		return updateDTO;
	}

	public void setUpdateDTO(SisuSeriyaDTO updateDTO) {
		this.updateDTO = updateDTO;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isCreateMode() {
		return createMode;
	}

	public void setCreateMode(boolean createMode) {
		this.createMode = createMode;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public void setViewMode(boolean viewMode) {
		this.viewMode = viewMode;
	}

	public boolean isRenderButton() {
		return renderButton;
	}

	public void setRenderButton(boolean renderButton) {
		this.renderButton = renderButton;
	}

	public List<String> getBusNoList() {
		return busNoList;
	}

	public void setBusNoList(List<String> busNoList) {
		this.busNoList = busNoList;
	}

	public List<String> getOperatorNameList() {
		return operatorNameList;
	}

	public void setOperatorNameList(List<String> operatorNameList) {
		this.operatorNameList = operatorNameList;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public String getWarningMsg() {
		return warningMsg;
	}

	public void setWarningMsg(String warningMsg) {
		this.warningMsg = warningMsg;
	}

	public List<String> getMonthListFinal() {
		return monthListFinal;
	}

	public void setMonthListFinal(List<String> monthListFinal) {
		this.monthListFinal = monthListFinal;
	}

	public String getErrorMessageNew() {
		return errorMessageNew;
	}

	public void setErrorMessageNew(String errorMessageNew) {
		this.errorMessageNew = errorMessageNew;
	}

	public String getMonthNameAlradyhave() {
		return monthNameAlradyhave;
	}

	public void setMonthNameAlradyhave(String monthNameAlradyhave) {
		this.monthNameAlradyhave = monthNameAlradyhave;
	}
	

}
