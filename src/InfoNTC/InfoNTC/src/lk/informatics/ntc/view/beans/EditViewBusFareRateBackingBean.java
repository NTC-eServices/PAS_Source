package lk.informatics.ntc.view.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.BusFareDTO;
import lk.informatics.ntc.model.service.BusFareService;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "editViewBusFareRateBean")
@ViewScoped
public class EditViewBusFareRateBackingBean {

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private CommonService commonService;
	private BusFareService busFareService;
	private BusFareDTO busFareDTO, viewSelect, editSelect, viewBusFeeDTO, selectDTO;
	private boolean disableCancel;
	private List<BusFareDTO> busRateList, referenceNoList, viewBusFareRateList, editBusFareRateList, stageList;
	private String alertMSG, successMessage, errorMessage, yearString;

	private boolean normalFareAdd, semiLuxuaryFareAdded, luxuaryFareAdded, superLuxuaryFareAdded, highWayFareAdded,
			sisuSariyaHalfAdded, sisuSariyaQuarterAdded, editAccess, viewAccess;

	private List<BusFareDTO> normalRateList;
	private List<BusFareDTO> semiLuxuaryRateList;
	private List<BusFareDTO> luxuaryList;
	private List<BusFareDTO> superLuxuaryList;
	private List<BusFareDTO> highWayList;
	private List<BusFareDTO> sisuSariyaHalfList;
	private List<BusFareDTO> sisuSariyaQuarterList;

	@PostConstruct
	public void init() {
		busFareDTO = new BusFareDTO();
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		busFareService = (BusFareService) SpringApplicationContex.getBean("busFareService");
		loadValues();

	}

	private void loadValues() {
		selectDTO = new BusFareDTO();
		viewSelect = new BusFareDTO();
		editSelect = new BusFareDTO();
		disableCancel = true;
		referenceNoList = new ArrayList<>();
		referenceNoList.addAll(busFareService.getBusFareReferenceNo());
		referenceNoList.addAll(busFareService.getTempBusFareReferenceNo());

		busRateList = new ArrayList<>();
		busRateList = busFareService.getDefaultBusFareRate();
	}

	public void search() {

		if ((busFareDTO.getFareReferenceNo() == null || busFareDTO.getFareReferenceNo().trim().equalsIgnoreCase(""))
				&& (busFareDTO.getStatus() == null || busFareDTO.getStatus().trim().equalsIgnoreCase(""))) {

			setErrorMessage("Please select at least one filed.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {

			if (busFareDTO.getFareReferenceNo() != null
					&& !busFareDTO.getFareReferenceNo().trim().equalsIgnoreCase("")) {

				String refNo = busFareDTO.getFareReferenceNo().substring(0, 3);

				if (refNo.equals("FRN")) {

					busRateList = new ArrayList<>();
					busRateList = busFareService.getBusFareRateData(busFareDTO);

					if (busRateList.isEmpty()) {
						setErrorMessage("No data found");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					} else {
						disableCancel = false;
					}

				} else if (refNo.equals("TPR")) {

					busRateList = new ArrayList<>();
					busRateList = busFareService.getTempBusFareRateData(busFareDTO);

					if (busRateList.isEmpty()) {
						setErrorMessage("No data found");
						RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
					} else {
						disableCancel = false;
					}

				} else {
					setErrorMessage("Can not find reference no. type.");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				}

			} else if (busFareDTO.getStatus() != null && !busFareDTO.getStatus().trim().equalsIgnoreCase("")) {

				busRateList = new ArrayList<>();
				busRateList = busFareService.getBusFareRateData(busFareDTO);

				if (busRateList.isEmpty()) {
					setErrorMessage("No data found");
					RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
				} else {
					disableCancel = false;
				}
			}

		}

	}

	public void viewRate() {

		if (viewSelect.getStatus().equals("APPROVED")) {

			listManager("nt_t_fee_circle", "tfc_stage", false, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMessage("No data found for view.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else if (viewSelect.getStatus().equals("PENDING")) {

			listManager("nt_temp_fee_circle", "tfc_stage", false, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMessage("No data found for view.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else if (viewSelect.getStatus().equals("CHECKED")) {

			listManager("nt_temp_fee_circle", "tfc_stage", false, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMessage("No data found for view.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else if (viewSelect.getStatus().equals("RECOMMENDED")) {

			listManager("nt_temp_fee_circle", "tfc_stage", false, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMessage("No data found for view.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else if (viewSelect.getStatus().equals("REJECTED")) {

			listManager("nt_h_fee_circle", "tfc_stage", true, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMessage("No data found for view.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else if (viewSelect.getStatus().equals("INACTIVE")) {

			listManager("nt_h_fee_circle", "tfc_stage", true, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMessage("No data found for view.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else if (viewSelect.getStatus().equals("TEMPORARY")) {
			listManager("nt_temporary_fee_circle", "tfc_stage", true, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMessage("No data found for view.");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}

	}

	public void listManager(String tableName, String satge, boolean isHistory, String referenceNo) {

		stageList = new ArrayList<>();
		stageList = busFareService.getTableStageList(tableName, satge, isHistory, referenceNo);

		normalRateList = new ArrayList<>();
		normalRateList = busFareService.getNormalList(tableName, satge, isHistory, referenceNo);

		semiLuxuaryRateList = new ArrayList<>();
		semiLuxuaryRateList = busFareService.getSemiLuxuaryList(tableName, satge, isHistory, referenceNo);

		luxuaryList = new ArrayList<>();
		luxuaryList = busFareService.getLuxuaryList(tableName, satge, isHistory, referenceNo);

		superLuxuaryList = new ArrayList<>();
		superLuxuaryList = busFareService.getSuperLuxuaryList(tableName, satge, isHistory, referenceNo);

		highWayList = new ArrayList<>();
		highWayList = busFareService.getHighWayList(tableName, satge, isHistory, referenceNo);

		sisuSariyaHalfList = new ArrayList<>();
		sisuSariyaHalfList = busFareService.getSisuSariyaHalfList(tableName, satge, isHistory, referenceNo);

		sisuSariyaQuarterList = new ArrayList<>();
		sisuSariyaQuarterList = busFareService.getSisuSariyaQuaterList(tableName, satge, isHistory, referenceNo);

		if (isNormalListOfNulls(normalRateList) == false) {
			normalFareAdd = true;
		}
		if (isSemiLuxuryListOfNulls(semiLuxuaryRateList) == false) {
			semiLuxuaryFareAdded = true;
		}
		if (isLuxuryListOfNulls(luxuaryList) == false) {
			luxuaryFareAdded = true;
		}
		if (isSuperLuxuryListOfNulls(superLuxuaryList) == false) {
			superLuxuaryFareAdded = true;
		}
		if (isHighWayListOfNulls(highWayList) == false) {
			highWayFareAdded = true;
		}
		if (isSisuHalfListOfNulls(sisuSariyaHalfList) == false) {
			sisuSariyaHalfAdded = true;
		}
		if (isSisuQuaterListOfNulls(sisuSariyaQuarterList) == false) {
			sisuSariyaQuarterAdded = true;
		}

	}

	public boolean addAllFareRatesToViewList(boolean view) {

		boolean isViewListEmpty = false;

		if (view == true) {
			viewBusFareRateList = new ArrayList<>();
		} else {
			editBusFareRateList = new ArrayList<>();
		}

		for (int x = 0; x < stageList.size(); x++) {

			if (normalFareAdd == false) {
				normalRateList.add(noramlDTO(null, null, null));
			}
			if (semiLuxuaryFareAdded == false) {
				semiLuxuaryRateList.add(sisuSariyaHalfDTO(null, null, null));
			}
			if (luxuaryFareAdded == false) {
				luxuaryList.add(luxuaryDTO(null, null, null));
			}
			if (superLuxuaryFareAdded == false) {
				superLuxuaryList.add(superDTO(null, null, null));
			}
			if (highWayFareAdded == false) {
				highWayList.add(highwayDTO(null, null, null));
			}
			if (sisuSariyaHalfAdded == false) {
				sisuSariyaHalfList.add(sisuSariyaHalfDTO(null, null, null));
			}
			if (sisuSariyaQuarterAdded == false) {
				sisuSariyaQuarterList.add(sisuSariyaQuaterDTO(null, null, null));
			}
		}

		for (int i = 0; i < stageList.size(); i++) {

			if (stageList.get(i).getStageNo() != 0) {

				viewBusFeeDTO = new BusFareDTO(stageList.get(i).getFareReferenceNo(), stageList.get(i).getStageNo(),
						normalRateList.get(i).getNormalCurrentFee(), normalRateList.get(i).getNormalNewFee(),
						normalRateList.get(i).getNormalRoundFee(), semiLuxuaryRateList.get(i).getSemiLuxuryCurrentFee(),
						semiLuxuaryRateList.get(i).getSemiLuxuryNewFee(),
						semiLuxuaryRateList.get(i).getSemiLuxuryRoundFee(), luxuaryList.get(i).getLuxuryCurrentFee(),
						luxuaryList.get(i).getLuxuryNewFee(), luxuaryList.get(i).getLuxuryRoundFee(),
						highWayList.get(i).getHighwayCurrentFee(), highWayList.get(i).getHighwayNewFee(),
						highWayList.get(i).getHighwayRoundFee(), superLuxuaryList.get(i).getSuperLuxuryCurrentFee(),
						superLuxuaryList.get(i).getSuperLuxuryNewFee(),
						superLuxuaryList.get(i).getSuperLuxuryRoundFee(),
						sisuSariyaHalfList.get(i).getSisuSariyaHalfNoramlFee(),
						sisuSariyaHalfList.get(i).getSisuSariyaHalfAdjestedFee(),
						sisuSariyaHalfList.get(i).getSisuSariyaHalfdiffrentFee(),
						sisuSariyaQuarterList.get(i).getSisuSariyaQuarterNoramlFee(),
						sisuSariyaQuarterList.get(i).getSisuSariyaQuarterAdjestedFee(),
						sisuSariyaQuarterList.get(i).getSisuSariyaQuarterdiffrentFee());

				if (view == true) {

					viewBusFareRateList.add(viewBusFeeDTO);
					if (viewBusFareRateList.isEmpty()) {

						isViewListEmpty = true;
					} else {
						isViewListEmpty = false;
					}

				} else {
					editBusFareRateList.add(viewBusFeeDTO);

					if (editBusFareRateList.isEmpty()) {

						isViewListEmpty = true;
					} else {
						isViewListEmpty = false;
					}
				}

			}

		}

		return isViewListEmpty;

	}

	public void editRate() {

		if (editSelect.getStatus().equals("PENDING")) {

			listManager("nt_temp_fee_circle", "tfc_stage", false, editSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(false) == false) {

				RequestContext.getCurrentInstance().execute("PF('editFareRate').show()");

			} else {
				setErrorMessage("No data found!");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}

		} else {
			setErrorMessage("Only pending record allow to edit.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}

	}

	public void selectRow() {

		RequestContext.getCurrentInstance().execute("PF('editRate').show()");

		if (selectDTO.getNormalRoundFee() != null) {

			busFareDTO.setEditNormalRoundFee(selectDTO.getNormalRoundFee());
		}

		if (selectDTO.getSemiLuxuryRoundFee() != null) {
			busFareDTO.setEditSemiLuxuaryRoundFee(selectDTO.getSemiLuxuryRoundFee());
		}

		if (selectDTO.getLuxuryRoundFee() != null) {
			busFareDTO.setEditLuxuryRoundFee(selectDTO.getLuxuryRoundFee());
		}

		if (selectDTO.getSuperLuxuryRoundFee() != null) {
			busFareDTO.setEditSuperLuxuryRoundFee(selectDTO.getSuperLuxuryRoundFee());
		}

		if (selectDTO.getHighwayRoundFee() != null) {
			busFareDTO.setEdithighWayRoundFee(selectDTO.getHighwayRoundFee());
		}

		if (selectDTO.getSisuSariyaHalfAdjestedFee() != null) {
			busFareDTO.setEditSisuSariyaHalfAdjestFee(selectDTO.getSisuSariyaHalfAdjestedFee());
		}

		if (selectDTO.getSisuSariyaQuarterAdjestedFee() != null) {
			busFareDTO.setEditSisuSariyaQuaterAdjestFee(selectDTO.getSisuSariyaQuarterAdjestedFee());
		}

	}

	public void saveEditData() {

		if (busFareDTO.getEditNormalRoundFee() == null && busFareDTO.getEditSemiLuxuaryRoundFee() == null
				&& busFareDTO.getEditLuxuryRoundFee() == null && busFareDTO.getEditSuperLuxuryRoundFee() == null
				&& busFareDTO.getEdithighWayRoundFee() == null && busFareDTO.getEditSisuSariyaHalfAdjestFee() == null
				&& busFareDTO.getEditSisuSariyaQuaterAdjestFee() == null) {

			setErrorMessage("No data found for saving.");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");

		} else {

			if (busFareService.saveEditBusRateFee(busFareDTO, editSelect.getFareReferenceNo(),
					sessionBackingBean.getLoginUser(), selectDTO.getStageNo())) {

				RequestContext.getCurrentInstance().execute("PF('editRate').hide()");

				listManager("nt_temp_fee_circle", "tfc_stage", false, editSelect.getFareReferenceNo());
				addAllFareRatesToViewList(false);

				setSuccessMessage("Data saved successflly.");
				RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			} else {
				setErrorMessage("Can not save data. Error occurred!");
				RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
			}
		}

	}

	public void clearOne() {

		busFareDTO = new BusFareDTO();
	}

	public BusFareDTO noramlDTO(BigDecimal currentFee, BigDecimal newFee, BigDecimal roundUpFee) {
		BusFareDTO dto = new BusFareDTO();

		dto.setNormalCurrentFee(currentFee);
		dto.setNormalNewFee(newFee);
		dto.setNormalRoundFee(roundUpFee);

		return dto;
	}

	public BusFareDTO SemiDTO(BigDecimal currentFee, BigDecimal newFee, BigDecimal roundUpFee) {
		BusFareDTO dto = new BusFareDTO();

		dto.setSemiLuxuryCurrentFee(currentFee);
		dto.setSemiLuxuryNewFee(newFee);
		dto.setSemiLuxuryRoundFee(roundUpFee);

		return dto;
	}

	public BusFareDTO luxuaryDTO(BigDecimal currentFee, BigDecimal newFee, BigDecimal roundUpFee) {
		BusFareDTO dto = new BusFareDTO();

		dto.setLuxuryCurrentFee(currentFee);
		dto.setLuxuryNewFee(newFee);
		dto.setLuxuryRoundFee(roundUpFee);

		return dto;
	}

	public BusFareDTO superDTO(BigDecimal currentFee, BigDecimal newFee, BigDecimal roundUpFee) {
		BusFareDTO dto = new BusFareDTO();

		dto.setSuperLuxuryCurrentFee(currentFee);
		dto.setSuperLuxuryNewFee(newFee);
		dto.setSuperLuxuryRoundFee(roundUpFee);

		return dto;
	}

	public BusFareDTO highwayDTO(BigDecimal currentFee, BigDecimal newFee, BigDecimal roundUpFee) {
		BusFareDTO dto = new BusFareDTO();

		dto.setHighwayCurrentFee(currentFee);
		dto.setHighwayNewFee(newFee);
		dto.setHighwayRoundFee(roundUpFee);

		return dto;
	}

	public BusFareDTO sisuSariyaHalfDTO(BigDecimal normal, BigDecimal Adjuest, BigDecimal diffrent) {
		BusFareDTO dto = new BusFareDTO();

		dto.setSisuSariyaHalfNoramlFee(normal);
		dto.setSisuSariyaHalfAdjestedFee(Adjuest);
		dto.setSisuSariyaHalfdiffrentFee(diffrent);

		return dto;
	}

	public BusFareDTO sisuSariyaQuaterDTO(BigDecimal normal, BigDecimal Adjuest, BigDecimal diffrent) {
		BusFareDTO dto = new BusFareDTO();

		dto.setSisuSariyaQuarterNoramlFee(normal);
		dto.setSisuSariyaQuarterAdjestedFee(Adjuest);
		dto.setSisuSariyaQuarterdiffrentFee(diffrent);

		return dto;
	}

	public boolean isNormalListOfNulls(List<BusFareDTO> myList) {
		for (BusFareDTO dto : myList)

			if (dto.getNormalCurrentFee() != null)
				return false;
		return true;
	}

	public boolean isSemiLuxuryListOfNulls(List<BusFareDTO> myList) {
		for (BusFareDTO dto : myList)

			if (dto.getSemiLuxuryCurrentFee() != null)
				return false;
		return true;
	}

	public boolean isLuxuryListOfNulls(List<BusFareDTO> myList) {
		for (BusFareDTO dto : myList)

			if (dto.getLuxuryCurrentFee() != null)
				return false;
		return true;
	}

	public boolean isSuperLuxuryListOfNulls(List<BusFareDTO> myList) {
		for (BusFareDTO dto : myList)

			if (dto.getSuperLuxuryCurrentFee() != null)
				return false;
		return true;
	}

	public boolean isHighWayListOfNulls(List<BusFareDTO> myList) {
		for (BusFareDTO dto : myList)

			if (dto.getHighwayCurrentFee() != null)
				return false;
		return true;
	}

	public boolean isSisuHalfListOfNulls(List<BusFareDTO> myList) {
		for (BusFareDTO dto : myList)

			if (dto.getSisuSariyaHalfAdjestedFee() != null)
				return false;
		return true;
	}

	public boolean isSisuQuaterListOfNulls(List<BusFareDTO> myList) {
		for (BusFareDTO dto : myList)

			if (dto.getSisuSariyaQuarterAdjestedFee() != null)
				return false;
		return true;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public BusFareService getBusFareService() {
		return busFareService;
	}

	public void setBusFareService(BusFareService busFareService) {
		this.busFareService = busFareService;
	}

	public BusFareDTO getBusFareDTO() {
		return busFareDTO;
	}

	public void setBusFareDTO(BusFareDTO busFareDTO) {
		this.busFareDTO = busFareDTO;
	}

	public boolean isDisableCancel() {
		return disableCancel;
	}

	public void setDisableCancel(boolean disableCancel) {
		this.disableCancel = disableCancel;
	}

	public List<BusFareDTO> getBusRateList() {
		return busRateList;
	}

	public void setBusRateList(List<BusFareDTO> busRateList) {
		this.busRateList = busRateList;
	}

	public List<BusFareDTO> getReferenceNoList() {
		return referenceNoList;
	}

	public void setReferenceNoList(List<BusFareDTO> referenceNoList) {
		this.referenceNoList = referenceNoList;
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

	public BusFareDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(BusFareDTO viewSelect) {
		this.viewSelect = viewSelect;
	}

	public BusFareDTO getEditSelect() {
		return editSelect;
	}

	public void setEditSelect(BusFareDTO editSelect) {
		this.editSelect = editSelect;
	}

	public List<BusFareDTO> getViewBusFareRateList() {
		return viewBusFareRateList;
	}

	public void setViewBusFareRateList(List<BusFareDTO> viewBusFareRateList) {
		this.viewBusFareRateList = viewBusFareRateList;
	}

	public List<BusFareDTO> getEditBusFareRateList() {
		return editBusFareRateList;
	}

	public void setEditBusFareRateList(List<BusFareDTO> editBusFareRateList) {
		this.editBusFareRateList = editBusFareRateList;
	}

	public String getYearString() {
		return yearString;
	}

	public void setYearString(String yearString) {
		this.yearString = yearString;
	}

	public boolean isNormalFareAdd() {
		return normalFareAdd;
	}

	public void setNormalFareAdd(boolean normalFareAdd) {
		this.normalFareAdd = normalFareAdd;
	}

	public boolean isSemiLuxuaryFareAdded() {
		return semiLuxuaryFareAdded;
	}

	public void setSemiLuxuaryFareAdded(boolean semiLuxuaryFareAdded) {
		this.semiLuxuaryFareAdded = semiLuxuaryFareAdded;
	}

	public boolean isLuxuaryFareAdded() {
		return luxuaryFareAdded;
	}

	public void setLuxuaryFareAdded(boolean luxuaryFareAdded) {
		this.luxuaryFareAdded = luxuaryFareAdded;
	}

	public boolean isSuperLuxuaryFareAdded() {
		return superLuxuaryFareAdded;
	}

	public void setSuperLuxuaryFareAdded(boolean superLuxuaryFareAdded) {
		this.superLuxuaryFareAdded = superLuxuaryFareAdded;
	}

	public boolean isHighWayFareAdded() {
		return highWayFareAdded;
	}

	public void setHighWayFareAdded(boolean highWayFareAdded) {
		this.highWayFareAdded = highWayFareAdded;
	}

	public boolean isSisuSariyaHalfAdded() {
		return sisuSariyaHalfAdded;
	}

	public void setSisuSariyaHalfAdded(boolean sisuSariyaHalfAdded) {
		this.sisuSariyaHalfAdded = sisuSariyaHalfAdded;
	}

	public boolean isSisuSariyaQuarterAdded() {
		return sisuSariyaQuarterAdded;
	}

	public void setSisuSariyaQuarterAdded(boolean sisuSariyaQuarterAdded) {
		this.sisuSariyaQuarterAdded = sisuSariyaQuarterAdded;
	}

	public List<BusFareDTO> getNormalRateList() {
		return normalRateList;
	}

	public void setNormalRateList(List<BusFareDTO> normalRateList) {
		this.normalRateList = normalRateList;
	}

	public List<BusFareDTO> getSemiLuxuaryRateList() {
		return semiLuxuaryRateList;
	}

	public void setSemiLuxuaryRateList(List<BusFareDTO> semiLuxuaryRateList) {
		this.semiLuxuaryRateList = semiLuxuaryRateList;
	}

	public List<BusFareDTO> getLuxuaryList() {
		return luxuaryList;
	}

	public void setLuxuaryList(List<BusFareDTO> luxuaryList) {
		this.luxuaryList = luxuaryList;
	}

	public List<BusFareDTO> getSuperLuxuaryList() {
		return superLuxuaryList;
	}

	public void setSuperLuxuaryList(List<BusFareDTO> superLuxuaryList) {
		this.superLuxuaryList = superLuxuaryList;
	}

	public List<BusFareDTO> getHighWayList() {
		return highWayList;
	}

	public void setHighWayList(List<BusFareDTO> highWayList) {
		this.highWayList = highWayList;
	}

	public List<BusFareDTO> getSisuSariyaHalfList() {
		return sisuSariyaHalfList;
	}

	public void setSisuSariyaHalfList(List<BusFareDTO> sisuSariyaHalfList) {
		this.sisuSariyaHalfList = sisuSariyaHalfList;
	}

	public List<BusFareDTO> getSisuSariyaQuarterList() {
		return sisuSariyaQuarterList;
	}

	public void setSisuSariyaQuarterList(List<BusFareDTO> sisuSariyaQuarterList) {
		this.sisuSariyaQuarterList = sisuSariyaQuarterList;
	}

	public BusFareDTO getViewBusFeeDTO() {
		return viewBusFeeDTO;
	}

	public void setViewBusFeeDTO(BusFareDTO viewBusFeeDTO) {
		this.viewBusFeeDTO = viewBusFeeDTO;
	}

	public BusFareDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(BusFareDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public List<BusFareDTO> getStageList() {
		return stageList;
	}

	public void setStageList(List<BusFareDTO> stageList) {
		this.stageList = stageList;
	}

}
