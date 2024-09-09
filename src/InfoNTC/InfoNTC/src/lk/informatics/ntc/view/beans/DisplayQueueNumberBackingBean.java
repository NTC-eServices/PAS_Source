package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lk.informatics.ntc.model.dto.DisplayQueueNumbersDTO;
import lk.informatics.ntc.model.dto.ParamerDTO;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.DisplayQueueNumbersService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "displayQueueNumber")
@ViewScoped
public class DisplayQueueNumberBackingBean {

	private ArrayList<String> preCurrentNumbers = new ArrayList<String>();
	private ArrayList<String> currentNumbers = new ArrayList<String>();
	private List<DisplayQueueNumbersDTO> panel1;
	private List<DisplayQueueNumbersDTO> panel2;
	private List<DisplayQueueNumbersDTO> calledNumbers;
	private DisplayQueueNumbersService displayQueueNumberService;
	private CommonService commonService;
	private MigratedService migratedService;
	private QueueManagementService queueManagementService;
	private int calledNumbersValue;
	private int skipCount;
	private List<String> images;
	private List<DisplayQueueNumbersDTO> permenantlySkipedNo;
	private ParamerDTO countOfPermanentluSkiped;

	@PostConstruct
	public void init() {
		calledNumbers = new ArrayList<DisplayQueueNumbersDTO>();
		images = new ArrayList<String>();
		for (int i = 1; i <= 3; i++) {
			images.add("ntc" + i + ".jpg");
		}
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		getCalledNumValue();
		displayQueueNumberService = (DisplayQueueNumbersService) SpringApplicationContex
				.getBean("displayQueueNumberService");

		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");

		loadData();

	}

	public void getCalledNumValue() {

		ParamerDTO paramDTO1 = migratedService
				.retrieveParameterValuesForParamName("COUNTER_DISPLAY_CALLED_NUMBER_VALUE");
		calledNumbersValue = paramDTO1.getIntValue();
		countOfPermanentluSkiped = migratedService.retrieveParameterValuesForParamName("QUEUE_MASTER_SKIP_COUNT");

	}

	public void loadData() {

		panel1 = queueManagementService.currentNumbers();
		panel2 = queueManagementService.counterDetails();

	}

	public void updateCounterTable() {
		currentNumbers.clear();
		preCurrentNumbers.clear();

		for (DisplayQueueNumbersDTO displayQueueNumbersDTO : panel2) {

			preCurrentNumbers.add(displayQueueNumbersDTO.getCounterCurrentNo());

		}

		panel1 = queueManagementService.currentNumbers();
		panel2 = queueManagementService.counterDetails();

		for (DisplayQueueNumbersDTO pnl1 : panel1) {

			for (DisplayQueueNumbersDTO pnl2 : panel2) {

				if (pnl1.getCounterCurrentNo().equals(pnl2.getQueueNo4())) {
					if (pnl2.getQueueNo5() != null && !pnl1.getCounterCurrentNo().equals(pnl2.getQueueNo5())) {
						pnl2.setQueueNo4(pnl2.getQueueNo5());
						pnl2.setQueueNo5(null);
					} else {
						pnl2.setQueueNo4(null);
					}

				}
				if (pnl1.getCounterCurrentNo().equals(pnl2.getQueueNo5())) {
					pnl2.setQueueNo5(null);
				}

			}

		}

	}

	public void called() {
		calledNumbers = displayQueueNumberService.calledNumbers();

	}

	public void permenantlySkipedNumbers() {

		permenantlySkipedNo = displayQueueNumberService
				.permenantlySkipedNumbers(String.valueOf(countOfPermanentluSkiped.getIntValue()));
	}

	public boolean compareList(List<String> ls1, List<String> ls2) {
		return ls1.toString().contentEquals(ls2.toString()) ? true : false;
	}

	public List<DisplayQueueNumbersDTO> getPanel2() {
		return panel2;
	}

	public DisplayQueueNumbersService getDisplayQueueNumberService() {
		return displayQueueNumberService;
	}

	public void setPanel2(List<DisplayQueueNumbersDTO> panel2) {
		this.panel2 = panel2;
	}

	public void setDisplayQueueNumberService(DisplayQueueNumbersService displayQueueNumberService) {
		this.displayQueueNumberService = displayQueueNumberService;
	}

	public List<DisplayQueueNumbersDTO> getCalledNumbers() {
		return calledNumbers;
	}

	public void setCalledNumbers(List<DisplayQueueNumbersDTO> calledNumbers) {
		this.calledNumbers = calledNumbers;
	}

	public ArrayList<String> getPreCurrentNumbers() {
		return preCurrentNumbers;
	}

	public ArrayList<String> getCurrentNumbers() {
		return currentNumbers;
	}

	public void setPreCurrentNumbers(ArrayList<String> preCurrentNumbers) {
		this.preCurrentNumbers = preCurrentNumbers;
	}

	public void setCurrentNumbers(ArrayList<String> currentNumbers) {
		this.currentNumbers = currentNumbers;
	}

	public void setCalledNumbersValue(int calledNumbersValue) {
		this.calledNumbersValue = calledNumbersValue;
	}

	public int getCalledNumbersValue() {
		return calledNumbersValue;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public List<DisplayQueueNumbersDTO> getPanel1() {
		return panel1;
	}

	public void setPanel1(List<DisplayQueueNumbersDTO> panel1) {
		this.panel1 = panel1;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public List<DisplayQueueNumbersDTO> getPermenantlySkipedNo() {
		return permenantlySkipedNo;
	}

	public void setPermenantlySkipedNo(List<DisplayQueueNumbersDTO> permenantlySkipedNo) {
		this.permenantlySkipedNo = permenantlySkipedNo;
	}

	public int getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public ParamerDTO getCountOfPermanentluSkiped() {
		return countOfPermanentluSkiped;
	}

	public void setCountOfPermanentluSkiped(ParamerDTO countOfPermanentluSkiped) {
		this.countOfPermanentluSkiped = countOfPermanentluSkiped;
	}

}
