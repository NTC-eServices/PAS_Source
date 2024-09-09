package lk.informatics.ntc.view.beans;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.BusFareDTO;
import lk.informatics.ntc.model.service.BusFareService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "fareTableApprovalBackingBean")
@ViewScoped
public class FareTableApprovalBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private String selectedFareRefNo;
	private String selectedStatus;
	private boolean disabledFareEndDate = true;
	private String successMsg;
	private String errorMsg;
	private boolean isSearch;
	private boolean disabledApproveBtn = true;
	private boolean disabledCheckedBtn = true;
	private boolean disabledRecommendedBtn = true;

	private boolean normalFareAdd, semiLuxuaryFareAdded, luxuaryFareAdded, superLuxuaryFareAdded, highWayFareAdded,
			sisuSariyaHalfAdded, sisuSariyaQuarterAdded;
	private String yearString;

	private BusFareDTO busFareDTO = new BusFareDTO();
	private BusFareDTO selectDTO = new BusFareDTO();
	private BusFareDTO prevActiveRerdDTO = new BusFareDTO();
	private BusFareDTO viewSelect = new BusFareDTO();

	// view action DTO
	private BusFareDTO viewBusFeeDTO;

	private List<BusFareDTO> fareRefNoList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> dataList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> statusList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> tFeeListForPrevActiveRef = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> tempNormalFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> tempLuxeryFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> tempSemiLuxeryFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> tempSuperLuxeryFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> tempHighwayFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> tempSisuSeriyaHalfFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> tempSisuSeriyaQuaterFeeList = new ArrayList<BusFareDTO>();

	private List<BusFareDTO> prevActiveNormalFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> prevActiveLuxeryFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> prevActiveSemiLuxeryFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> prevActiveSuperLuxeryFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> prevActiveHighwayFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> prevActiveSisuSeriyaHalfFeeList = new ArrayList<BusFareDTO>();
	private List<BusFareDTO> prevActiveSisuSeriyaQuaterFeeList = new ArrayList<BusFareDTO>();

	private List<BusFareDTO> viewTempListForPrevRefNo = new ArrayList<BusFareDTO>();

	// view btn lists
	private List<BusFareDTO> normalRateList;
	private List<BusFareDTO> semiLuxuaryRateList;
	private List<BusFareDTO> luxuaryList;
	private List<BusFareDTO> superLuxuaryList;
	private List<BusFareDTO> highWayList;
	private List<BusFareDTO> sisuSariyaHalfList;
	private List<BusFareDTO> sisuSariyaQuarterList;
	private List<BusFareDTO> stageList;
	private List<BusFareDTO> viewBusFareRateList;

	private BusFareService busFareService;

	public FareTableApprovalBackingBean() {

	}

	@PostConstruct
	public void init() {

		busFareService = (BusFareService) SpringApplicationContex.getBean("busFareService");
		loadvalues();
	}

	private void loadvalues() {
		fareRefNoList = busFareService.getFareRefNoList();
		dataList = busFareService.getDefaultList();
		statusList = busFareService.getStatusList();
	}

	public void onFareNoChange() {

	}

	public void onStatusChange() {
		fareRefNoList = new ArrayList<BusFareDTO>();
		fareRefNoList = busFareService.getFilteredFareRefNoList(selectedStatus);
	}

	public void onStartDateChange() {
		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

		busFareDTO.setStartDateVal(frm.format(busFareDTO.getStartDateObj()));
		setDisabledFareEndDate(false);
	}

	public void onEndDateChange() {
		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

		busFareDTO.setEndDateVal(frm.format(busFareDTO.getEndDateObj()));

		Date requestStartDate = busFareDTO.getStartDateObj();
		Date requestEndDate = busFareDTO.getEndDateObj();

	}

	public void searchAction() {
		if (busFareDTO.getStartDateObj() == null && busFareDTO.getEndDateObj() == null && selectedStatus.equals("")
				&& selectedFareRefNo.equals("")) {

		} else if (busFareDTO.getStartDateObj() != null || busFareDTO.getEndDateObj() != null
				|| !selectedStatus.equalsIgnoreCase("") || !selectedFareRefNo.equalsIgnoreCase("")) {
			dataList = new ArrayList<BusFareDTO>();
			dataList = busFareService.getFilteredDateList(busFareDTO.getStartDateObj(), busFareDTO.getEndDateObj(),
					selectedStatus, selectedFareRefNo);

			if (dataList.isEmpty()) {
				dataList = new ArrayList<BusFareDTO>();
				setErrorMsg("No Data Found.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			} else {

				isSearch = false;
			}

			isSearch = true;
		}
	}

	public void clearAction() {
		setSelectedFareRefNo(null);
		setSelectedStatus(null);
		busFareDTO = new BusFareDTO();
		dataList = new ArrayList<BusFareDTO>();
		dataList = busFareService.getDefaultList();
		isSearch = false;
		selectDTO = new BusFareDTO();
		setDisabledCheckedBtn(true);
		setDisabledRecommendedBtn(true);
		setDisabledApproveBtn(true);
		fareRefNoList = new ArrayList<BusFareDTO>();
		fareRefNoList = busFareService.getFareRefNoList();
		statusList = new ArrayList<BusFareDTO>();
		statusList = busFareService.getStatusList();
	}

	public void selectRow() {
		if (selectDTO != null) {
			if (selectDTO.getStatus().equals("P")) {
				setDisabledCheckedBtn(false);
				setDisabledRecommendedBtn(true);
				setDisabledApproveBtn(true);
			} else if (selectDTO.getStatus().equals("C")) {
				setDisabledRecommendedBtn(false);
				setDisabledCheckedBtn(true);
				setDisabledApproveBtn(true);
			} else if (selectDTO.getStatus().equals("M")) {
				setDisabledCheckedBtn(true);
				setDisabledRecommendedBtn(true);
				setDisabledApproveBtn(false);
			} else if (selectDTO.getStatus().equals("A")) {
				setDisabledCheckedBtn(true);
				setDisabledRecommendedBtn(true);
				setDisabledApproveBtn(true);
			} else {
				setDisabledCheckedBtn(true);
				setDisabledRecommendedBtn(true);
				setDisabledApproveBtn(true);
			}
		}
	}

	public void checkedAction() {
		String loginUser = sessionBackingBean.getLoginUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if (selectDTO != null) {
			if (selectDTO.getStatus().equals("P")) {
				boolean ischecked = false;
				ischecked = busFareService.busFareChecked(selectDTO, sessionBackingBean.getLoginUser());
				if (ischecked == true) {
					setSuccessMsg("Checked by successful.");
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					loadSearchingValues();
					setDisabledCheckedBtn(true);
				} else {
					setErrorMsg("Checked by approval fail.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
				}
			}
		}
	}

	public void recommendedAction() {
		String loginUser = sessionBackingBean.getLoginUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if (selectDTO != null) {
			if (selectDTO.getStatus().equals("C")) {
				boolean ischecked = false;
				ischecked = busFareService.busFareRecommended(selectDTO, sessionBackingBean.getLoginUser());
				if (ischecked == true) {
					setSuccessMsg("Recommended by successful.");
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					loadSearchingValues();
					setDisabledRecommendedBtn(true);
				} else {
					setErrorMsg("Recommended by approval fail.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
				}
			}
		}
	}

	public void approveAction() {
		String loginUser = sessionBackingBean.getLoginUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if (selectDTO != null) {
			if (selectDTO.getStatus().equals("M")) {
				boolean isApproved = false;
				boolean isInactived = false;
				prevActiveRerdDTO = busFareService.getPrevActiveRecordDetails(selectDTO);
				busFareDTO.setPrevActiveBusFareRefNo(prevActiveRerdDTO.getPrevActiveBusFareRefNo());
				isInactived = busFareService.busFareInactivePrevRecrd(selectDTO, sessionBackingBean.getLoginUser());
				isApproved = busFareService.busFareApproved(selectDTO, sessionBackingBean.getLoginUser());
				if (isApproved == true && isInactived == true) {
					tFeeListForPrevActiveRef = busFareService
							.getTFeeListForPrevActiveRecord(busFareDTO.getPrevActiveBusFareRefNo());
					boolean insertedHistory = false;
					insertedHistory = busFareService.insertFareHistory(tFeeListForPrevActiveRef);
					if (insertedHistory == true) {
						setSuccessMsg("Saved in history.");
						RequestContext.getCurrentInstance().update("frmsuccessSve");
						RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					}

					tempNormalFeeList = busFareService.getTempNormalFeeList(selectDTO);
					tempLuxeryFeeList = busFareService.getTempLuxeryFeeList(selectDTO);
					tempSemiLuxeryFeeList = busFareService.getTempSemiLuxeryFeeList(selectDTO);
					tempSuperLuxeryFeeList = busFareService.getTempSuperLuxeryFeeList(selectDTO);
					tempHighwayFeeList = busFareService.getTempHighwayFeeList(selectDTO);
					tempSisuSeriyaHalfFeeList = busFareService.getTempSisuSeriyaHalfFeeList(selectDTO);
					tempSisuSeriyaQuaterFeeList = busFareService.getTempSisuSeriyaQuaterFeeList(selectDTO);

					// update t table
					if (tempNormalFeeList.isEmpty()) {

					} else {

						boolean insertNormalTemp = false;
						insertNormalTemp = busFareService.updateFareNormalDet(tempNormalFeeList, selectDTO,
								sessionBackingBean.getLoginUser());
						if (insertNormalTemp == true) {
							setSuccessMsg("Saved in t table -- > normal det.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						}
					}

					if (tempLuxeryFeeList.isEmpty()) {

					} else {

						boolean insertLuxeryTemp = false;
						insertLuxeryTemp = busFareService.updateFareLuxeryDet(tempLuxeryFeeList, selectDTO,
								sessionBackingBean.getLoginUser());
						if (insertLuxeryTemp == true) {
							setSuccessMsg("Saved in t table -- > luxery det.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						}
					}

					if (tempSemiLuxeryFeeList.isEmpty()) {

					} else {

						boolean insertSemiLuxeryTemp = false;
						insertSemiLuxeryTemp = busFareService.updateFareSemiLuxeryDet(tempSemiLuxeryFeeList, selectDTO,
								sessionBackingBean.getLoginUser());
						if (insertSemiLuxeryTemp == true) {
							setSuccessMsg("Saved in t table -- > semmi luxery det.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						}
					}

					if (tempSuperLuxeryFeeList.isEmpty()) {

					} else {

						boolean insertSuperLuxeryTemp = false;
						insertSuperLuxeryTemp = busFareService.updateFareSuperLuxeryDet(tempSuperLuxeryFeeList,
								selectDTO, sessionBackingBean.getLoginUser());
						if (insertSuperLuxeryTemp == true) {
							setSuccessMsg("Saved in t table -- > super luxery det.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						}
					}

					if (tempHighwayFeeList.isEmpty()) {

					} else {

						boolean insertHighwayTemp = false;
						insertHighwayTemp = busFareService.updateFareHighwayDet(tempHighwayFeeList, selectDTO,
								sessionBackingBean.getLoginUser());
						if (insertHighwayTemp == true) {
							setSuccessMsg("Saved in t table -- > highway det.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						}
					}

					if (tempSisuSeriyaHalfFeeList.isEmpty()) {

					} else {

						boolean insertSisuHalfTemp = false;
						insertSisuHalfTemp = busFareService.updateFareSisuHalfDet(tempSisuSeriyaHalfFeeList, selectDTO,
								sessionBackingBean.getLoginUser());
						if (insertSisuHalfTemp == true) {
							setSuccessMsg("Saved in t table -- > sisu half det.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						}
					}

					if (tempSisuSeriyaQuaterFeeList.isEmpty()) {

					} else {

						boolean insertSisuQuaterTemp = false;
						insertSisuQuaterTemp = busFareService.updateFareSisuQuaterDet(tempSisuSeriyaQuaterFeeList,
								selectDTO, sessionBackingBean.getLoginUser());
						if (insertSisuQuaterTemp == true) {
							setSuccessMsg("Saved in t table -- > sisu quater det.");
							RequestContext.getCurrentInstance().update("frmsuccessSve");
							RequestContext.getCurrentInstance().execute("PF('successSve').show()");
						}
					}

					viewTempListForPrevRefNo = busFareService.getPrevActiveTempList(selectDTO);
					boolean deleteTempTb = false;
					deleteTempTb = busFareService.deleteTempList(viewTempListForPrevRefNo, selectDTO,
							sessionBackingBean.getLoginUser());

					setSuccessMsg("Approved by successful.");
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					loadSearchingValues();
					setDisabledApproveBtn(true);
				} else {
					setErrorMsg("Approved by approval fail.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
				}
			}
		}
	}

	public void rejectAction() {
		String loginUser = sessionBackingBean.getLoginUser();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if (selectDTO != null) {
			if (selectDTO.getStatus().equals("M")) {
				boolean ischecked = false;
				ischecked = busFareService.busFareRejected(selectDTO, sessionBackingBean.getLoginUser());
				if (ischecked == true) {
					setSuccessMsg("Rejected.");
					RequestContext.getCurrentInstance().update("frmsuccessSve");
					RequestContext.getCurrentInstance().execute("PF('successSve').show()");
					loadSearchingValues();
					setDisabledApproveBtn(true);
				} else {
					setErrorMsg("Rejected by approval fail.");
					RequestContext.getCurrentInstance().update("frmrequiredField");
				}
			}
		}
	}

	public void loadSearchingValues() {
		if (isSearch == true) {
			dataList = new ArrayList<BusFareDTO>();
			dataList = busFareService.getFilteredDateList(busFareDTO.getStartDateObj(), busFareDTO.getEndDateObj(),
					selectedStatus, selectedFareRefNo);
		} else {
			dataList = new ArrayList<BusFareDTO>();
			dataList = busFareService.getDefaultList();
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

	public boolean addAllFareRatesToViewList(boolean view) {

		boolean isViewListEmpty = false;

		if (view == true) {
			viewBusFareRateList = new ArrayList<>();
		} else {

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
				}

			}

		}

		return isViewListEmpty;

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

	/** Method used view active fare details in common inquiry form **/
	public void viewInquery() {

		String refernceNo = busFareService.getActiveReferenceNo();

		if (refernceNo != null) {

			listManager("nt_t_fee_circle", "tfc_stage", false, refernceNo);

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMsg("No Data Found For View.");
				RequestContext.getCurrentInstance().update("frmrequiredFieldBusFare");
				RequestContext.getCurrentInstance().execute("PF('requiredFieldFare').show()");

			}

		} else {
			setErrorMsg("Can not find active data");
			RequestContext.getCurrentInstance().update("frmrequiredFieldBusFare");
			RequestContext.getCurrentInstance().execute("PF('requiredFieldFare').show()");
		}

	}

	public void viewRate() {

		if (viewSelect.getStatus().equals("A")) {

			listManager("nt_t_fee_circle", "tfc_stage", false, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMsg("No Data Found For View.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		} else if (viewSelect.getStatus().equals("P")) {

			listManager("nt_temp_fee_circle", "tfc_stage", false, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMsg("No Data Found For View.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else if (viewSelect.getStatus().equals("R")) {

			listManager("nt_h_fee_circle", "tfc_stage", true, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMsg("No Data Found For View.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else if (viewSelect.getStatus().equals("I")) {

			listManager("nt_h_fee_circle", "tfc_stage", true, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMsg("No Data Found For View.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}

		} else if (viewSelect.getStatus().equals("T")) {
			listManager("nt_temporary_fee_circle", "tfc_stage", true, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMsg("No Data Found For View.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else if (viewSelect.getStatus().equals("M")) {

			listManager("nt_temp_fee_circle", "tfc_stage", false, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMsg("No Data Found For View.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		} else if (viewSelect.getStatus().equals("C")) {

			listManager("nt_temp_fee_circle", "tfc_stage", false, viewSelect.getFareReferenceNo());

			if (addAllFareRatesToViewList(true) == false) {

				RequestContext.getCurrentInstance().execute("PF('viewFareRate').show()");

			} else {
				setErrorMsg("No Data Found For View.");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
			}
		}
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String getSelectedFareRefNo() {
		return selectedFareRefNo;
	}

	public void setSelectedFareRefNo(String selectedFareRefNo) {
		this.selectedFareRefNo = selectedFareRefNo;
	}

	public BusFareDTO getBusFareDTO() {
		return busFareDTO;
	}

	public void setBusFareDTO(BusFareDTO busFareDTO) {
		this.busFareDTO = busFareDTO;
	}

	public BusFareDTO getSelectDTO() {
		return selectDTO;
	}

	public void setSelectDTO(BusFareDTO selectDTO) {
		this.selectDTO = selectDTO;
	}

	public List<BusFareDTO> getFareRefNoList() {
		return fareRefNoList;
	}

	public void setFareRefNoList(List<BusFareDTO> fareRefNoList) {
		this.fareRefNoList = fareRefNoList;
	}

	public List<BusFareDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<BusFareDTO> dataList) {
		this.dataList = dataList;
	}

	public BusFareService getBusFareService() {
		return busFareService;
	}

	public void setBusFareService(BusFareService busFareService) {
		this.busFareService = busFareService;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public List<BusFareDTO> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<BusFareDTO> statusList) {
		this.statusList = statusList;
	}

	public boolean isDisabledFareEndDate() {
		return disabledFareEndDate;
	}

	public void setDisabledFareEndDate(boolean disabledFareEndDate) {
		this.disabledFareEndDate = disabledFareEndDate;
	}

	public String getSuccessMsg() {
		return successMsg;
	}

	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

	public boolean isDisabledApproveBtn() {
		return disabledApproveBtn;
	}

	public void setDisabledApproveBtn(boolean disabledApproveBtn) {
		this.disabledApproveBtn = disabledApproveBtn;
	}

	public boolean isDisabledCheckedBtn() {
		return disabledCheckedBtn;
	}

	public void setDisabledCheckedBtn(boolean disabledCheckedBtn) {
		this.disabledCheckedBtn = disabledCheckedBtn;
	}

	public boolean isDisabledRecommendedBtn() {
		return disabledRecommendedBtn;
	}

	public void setDisabledRecommendedBtn(boolean disabledRecommendedBtn) {
		this.disabledRecommendedBtn = disabledRecommendedBtn;
	}

	public BusFareDTO getViewSelect() {
		return viewSelect;
	}

	public void setViewSelect(BusFareDTO viewSelect) {
		this.viewSelect = viewSelect;
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

	public BusFareDTO getPrevActiveRerdDTO() {
		return prevActiveRerdDTO;
	}

	public void setPrevActiveRerdDTO(BusFareDTO prevActiveRerdDTO) {
		this.prevActiveRerdDTO = prevActiveRerdDTO;
	}

	public BusFareDTO getViewBusFeeDTO() {
		return viewBusFeeDTO;
	}

	public void setViewBusFeeDTO(BusFareDTO viewBusFeeDTO) {
		this.viewBusFeeDTO = viewBusFeeDTO;
	}

	public List<BusFareDTO> gettFeeListForPrevActiveRef() {
		return tFeeListForPrevActiveRef;
	}

	public void settFeeListForPrevActiveRef(List<BusFareDTO> tFeeListForPrevActiveRef) {
		this.tFeeListForPrevActiveRef = tFeeListForPrevActiveRef;
	}

	public List<BusFareDTO> getTempNormalFeeList() {
		return tempNormalFeeList;
	}

	public void setTempNormalFeeList(List<BusFareDTO> tempNormalFeeList) {
		this.tempNormalFeeList = tempNormalFeeList;
	}

	public List<BusFareDTO> getTempLuxeryFeeList() {
		return tempLuxeryFeeList;
	}

	public void setTempLuxeryFeeList(List<BusFareDTO> tempLuxeryFeeList) {
		this.tempLuxeryFeeList = tempLuxeryFeeList;
	}

	public List<BusFareDTO> getTempSemiLuxeryFeeList() {
		return tempSemiLuxeryFeeList;
	}

	public void setTempSemiLuxeryFeeList(List<BusFareDTO> tempSemiLuxeryFeeList) {
		this.tempSemiLuxeryFeeList = tempSemiLuxeryFeeList;
	}

	public List<BusFareDTO> getTempSuperLuxeryFeeList() {
		return tempSuperLuxeryFeeList;
	}

	public void setTempSuperLuxeryFeeList(List<BusFareDTO> tempSuperLuxeryFeeList) {
		this.tempSuperLuxeryFeeList = tempSuperLuxeryFeeList;
	}

	public List<BusFareDTO> getTempHighwayFeeList() {
		return tempHighwayFeeList;
	}

	public void setTempHighwayFeeList(List<BusFareDTO> tempHighwayFeeList) {
		this.tempHighwayFeeList = tempHighwayFeeList;
	}

	public List<BusFareDTO> getTempSisuSeriyaHalfFeeList() {
		return tempSisuSeriyaHalfFeeList;
	}

	public void setTempSisuSeriyaHalfFeeList(List<BusFareDTO> tempSisuSeriyaHalfFeeList) {
		this.tempSisuSeriyaHalfFeeList = tempSisuSeriyaHalfFeeList;
	}

	public List<BusFareDTO> getTempSisuSeriyaQuaterFeeList() {
		return tempSisuSeriyaQuaterFeeList;
	}

	public void setTempSisuSeriyaQuaterFeeList(List<BusFareDTO> tempSisuSeriyaQuaterFeeList) {
		this.tempSisuSeriyaQuaterFeeList = tempSisuSeriyaQuaterFeeList;
	}

	public List<BusFareDTO> getPrevActiveNormalFeeList() {
		return prevActiveNormalFeeList;
	}

	public void setPrevActiveNormalFeeList(List<BusFareDTO> prevActiveNormalFeeList) {
		this.prevActiveNormalFeeList = prevActiveNormalFeeList;
	}

	public List<BusFareDTO> getPrevActiveLuxeryFeeList() {
		return prevActiveLuxeryFeeList;
	}

	public void setPrevActiveLuxeryFeeList(List<BusFareDTO> prevActiveLuxeryFeeList) {
		this.prevActiveLuxeryFeeList = prevActiveLuxeryFeeList;
	}

	public List<BusFareDTO> getPrevActiveSemiLuxeryFeeList() {
		return prevActiveSemiLuxeryFeeList;
	}

	public void setPrevActiveSemiLuxeryFeeList(List<BusFareDTO> prevActiveSemiLuxeryFeeList) {
		this.prevActiveSemiLuxeryFeeList = prevActiveSemiLuxeryFeeList;
	}

	public List<BusFareDTO> getPrevActiveSuperLuxeryFeeList() {
		return prevActiveSuperLuxeryFeeList;
	}

	public void setPrevActiveSuperLuxeryFeeList(List<BusFareDTO> prevActiveSuperLuxeryFeeList) {
		this.prevActiveSuperLuxeryFeeList = prevActiveSuperLuxeryFeeList;
	}

	public List<BusFareDTO> getPrevActiveHighwayFeeList() {
		return prevActiveHighwayFeeList;
	}

	public void setPrevActiveHighwayFeeList(List<BusFareDTO> prevActiveHighwayFeeList) {
		this.prevActiveHighwayFeeList = prevActiveHighwayFeeList;
	}

	public List<BusFareDTO> getPrevActiveSisuSeriyaHalfFeeList() {
		return prevActiveSisuSeriyaHalfFeeList;
	}

	public void setPrevActiveSisuSeriyaHalfFeeList(List<BusFareDTO> prevActiveSisuSeriyaHalfFeeList) {
		this.prevActiveSisuSeriyaHalfFeeList = prevActiveSisuSeriyaHalfFeeList;
	}

	public List<BusFareDTO> getPrevActiveSisuSeriyaQuaterFeeList() {
		return prevActiveSisuSeriyaQuaterFeeList;
	}

	public void setPrevActiveSisuSeriyaQuaterFeeList(List<BusFareDTO> prevActiveSisuSeriyaQuaterFeeList) {
		this.prevActiveSisuSeriyaQuaterFeeList = prevActiveSisuSeriyaQuaterFeeList;
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

	public List<BusFareDTO> getStageList() {
		return stageList;
	}

	public void setStageList(List<BusFareDTO> stageList) {
		this.stageList = stageList;
	}

	public List<BusFareDTO> getViewBusFareRateList() {
		return viewBusFareRateList;
	}

	public void setViewBusFareRateList(List<BusFareDTO> viewBusFareRateList) {
		this.viewBusFareRateList = viewBusFareRateList;
	}

	public String getYearString() {
		return yearString;
	}

	public void setYearString(String yearString) {
		this.yearString = yearString;
	}

}
