package lk.informatics.ntc.model.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TimeTableDTO {
	private String code;
	private String description;

	private String id;
	private long seq;
	private String routeNo;
	private String busCategory;
	private String originStartTimeString;
	private String originEndTimeString;
	private String originDivideRangeString;

	private String destinationStartTimeString;
	private String destinationEndTimeString;
	private String destinationDivideRangeString;

	private String firstValue;
	private String secondValue;

	private int noOfBuses;
	private int frequency;

	private int noOfPVTbuessOriginOne;
	private int noOfCTBbuessOriginOne;
	private int noOfOtherbuessOriginOne;
	private int restTimeOriginOne;
	private int totalBusesOriginOne;
	private int busesOnLeaveOriginOne;

	private int noOfPVTbuessDestinationOne;
	private int noOfCTBbuessDestinationOne;
	private int noOfOtherbuessDestinationOne;
	private int restTimeDestinationOne;
	private int totalBusesDestinationOne;
	private int busesOnLeaveDestinationOne;
	// panel generator with fixed time

	private String timeRange;
	private int noOfPVTbuessOriginTwo;
	private int noOfCTBbuessOriginTwo;
	private int noOfOtherbuessOriginTwo;
	private int restTimeOriginTwo;
	private int totalBusesOriginTwo;
	private int busesOnLeaveOriginTwo;

	private int noOfPVTbuessDestinationTwo;
	private int noOfCTBbuessDestinationTwo;
	private int noOfOtherbuessDestinationTwo;
	private int restTimeDestinationTwo;
	private int totalBusesDestinationTwo;
	private int busesOnLeaveDestinationTwo;

	private int noOfPVTbuessOriginThree;
	private int noOfCTBbuessOriginThree;
	private int noOfOtherbuessOriginThree;
	private int restTimeOriginThree;
	private int totalBusesOriginThree;
	private int busesOnLeaveOriginThree;

	private int noOfPVTbuessDestinationThree;
	private int noOfCTBbuessDestinationThree;
	private int noOfOtherbuessDestinationThree;
	private int restTimeDestinationThree;
	private int totalBusesDestinationThree;
	private int busesOnLeaveDestinationThree;

	private int totalTripsOriginOne;
	private int totalTripsDestinationOne;

	private int totalTripsOriginTwo;
	private int totalTripsDestinationTwo;

	private int totalTripsOriginThree;
	private int totalTripsDestinationThree;

	private String genereatedRefNo;
	private String origin;
	private String destination;

	private String type;
	private String group;
	private int totalTrips;
	private int noOfPVTbueses;
	private int noOfCTBbueses;
	private int noOfOtherbuses;
	private String restTime;
	private int totalBuses;
	private String tripRefNo;
	private int restTimeInt;
	private String busCategoryDes;

	private boolean alreadyAdded;
	private String duplicateBusNum;
	private String tempStartTime;
	private String tempEndTime;

	private int numOfLeaves;
	private boolean rowColor;
	private String timeDifferece;
	private boolean rowDisable;
	private boolean isOrigin;
	
	private String abbreviationLeave;
	private String permitNoLeave;
	private String busNoLeave;
	
	private String abbreviationLeaveDes;
	private String permitNoLeaveDes;
	private String busNoLeaveDes;
	
	private boolean checkAbbri;


	// for Route SetUp

	private String midPoint;
	private boolean isApplicable;
	private String timeTaken;
	
	private String user;
	private boolean isForSubmit;
	private boolean fixBus;
	
	private boolean check;
	
	private String abbreviation;
	private String abbreviationDes;
	private String serviceType;
	private String arrival;
	private String depature;
	private String arrivalOne;
	private String depatureOne;
	private String arrival2;
	private String depature2;
	private String arrival3;
	private String depature3;
	private String arrival4;
	private String depature4;
	private String arrival5;
	private String depature5;
	private long tripno;
	private String serviceTime;
	private String steringHours;
	private String break1;
	private String break2;
	private String break3;
	private String travelTime;
	private long abbno;
	
	private boolean coupleTwo;
	
	private int noOfDays;
	private String rowNumber;
	private long totalDistance;
	private Timestamp date;
	List<String> fullRotationList;

	public TimeTableDTO(String id, String firstValue, String secondValue, int noOfBuses, int frequency) {
		this.id = id;
		this.firstValue = firstValue;
		this.secondValue = secondValue;
		this.noOfBuses = noOfBuses;
		this.frequency = frequency;
	}

	public List<String> getFullRotationList() {
		return fullRotationList;
	}

	public void setFullRotationList(List<String> fullRotationList) {
		this.fullRotationList = fullRotationList;
	}

	public TimeTableDTO(String type, String group, int totalTrips, int noOfPVTbueses, int noOfCTBbueses,
			int noOfOtherbuses, String restTime, int totalBuses) {
		this.type = type;
		this.group = group;
		this.totalTrips = totalTrips;
		this.noOfPVTbueses = noOfPVTbueses;
		this.noOfCTBbueses = noOfCTBbueses;
		this.noOfOtherbuses = noOfOtherbuses;
		this.restTime = restTime;
		this.totalBuses = totalBuses;
	}

	public TimeTableDTO(String id, String startTime, String endTime, String restTime, boolean fixBus, boolean isOrigin,
			String abbreviation, String permitNo, String busNo,String traveltime) {
		this.id = id;

		if (isOrigin == true) {
			this.startTimeOrigin = startTime;
			this.endTimeOrigin = endTime;
			this.restTimeOrigin = restTime;
			this.fixBusOrigin = fixBus;
			this.abbreviationOrigin = abbreviation;
			this.permitNoOrigin = permitNo;
			this.busNoOrigin = busNo;
			this.traveltime = traveltime;
		} else {

			this.startTimeDestination = startTime;
			this.endTimeDestination = endTime;
			this.restTimeDestination = restTime;
			this.fixBusDestination = fixBus;
			this.abbreviationDestination = abbreviation;
			this.permitNoDestination = permitNo;
			this.busNoDestination = busNo;
			this.traveltime = traveltime;
		}

	}

	public TimeTableDTO(String abbreviation, String permitNo, String busNo, String startTime, String endTime,
			String restTime, boolean fixBus, boolean isOrigin) {

		if (isOrigin == true) {
			this.abbreviationOrigin = abbreviation;
			this.permitNoOrigin = permitNo;
			this.busNo = busNo;
			this.startTime = startTime;
			this.endTime = endTime;
			this.restTime = restTime;
			this.fixBusOrigin = fixBus;
		} else {
			this.abbreviationDestination = abbreviation;
			this.permitNoDestination = permitNo;
			this.busNo = busNo;
			this.startTime = startTime;
			this.endTime = endTime;
			this.restTime = restTime;
			this.fixBusDestination = fixBus;
		}

	}

	public TimeTableDTO() {
	}
	// panel generator with fixed time

	private String timeRangeStr;
	private String tripRefNoStr;
	private String groupNo;
	private String tripType;
	private String timeRangeStart;
	private String timeRangeEnd;
	private String timeStartVal;
	private String timeEndVal;
	private String busType;
	private int busCount;
	private int busTypeName;
	private String busNo;
	private String startTime;
	private String endTime;
	private String assigneBusNo;
	private boolean ctbBus;

	private String groupOneDays;
	private String groupTwoDays;
	private String groupThreeDays;
	private long masterSeq;
	private String tripId;

	private String groupOne;
	private String groupTwo;
	private String groupThree;
	private String timeRangeTo;
	private String DesToOrGroupOne;
	private String DesToOrGroupTwo;
	private String DesToOrGroupThree;
	private int noOfTrips;
	private int noOfTripsG10;
	private int noOfTripsG20;
	private int noOfTripsG30;
	private int noOfTripsG1D;
	private int noOfTripsG2D;
	private int noOfTripsG3D;

	private int tripIdInt;

	private String createdBy;
	private Timestamp createdDate;

	private String withFixedTimeCode;
	private String withOutFixedTimeCode;
	private String withFixedBusesCode;

	private String createBywithOutFixedTimeCode;
	private String createBywithFixedBusesCode;

	private Timestamp createDatewithOutFixedTimeCode;
	private Timestamp createDatewithFixedBusesCode;
	private long timeTableDetSeq;

	private boolean fixedTime;

	private String abbriAtOrigin;
	private String abbriAtDestination;
	private String busType1;
	private String busType2;
	private String busType3;
	private String busTypeDtoO1;
	private String busTypeDtoO2;
	private String busTypeDtoO3;
	private String panelGenNo;

	private String permiteExpireDate;

	private String comment;
	/*** for owner sheet ***/
	private int index;
	ArrayList<ArrayList<Integer>> monthArray;
	/*** for owner sheet end ***/

//	Panel Generator New

	private boolean isDailyRotation, isTwoDayRotation;
	private String noOfTimeTablesPerWeek;
	private String routeStatus;
	private String traveltime,busSpeed,distanceString,status;
	private int distance;

	private int noOfTripsOrigin;
	private int noOfPvtBusesOrigin;
	private int noOfTemporaryBusesOrigin;
	private int noOfCtbOrigin;
	private int noOfEtcOrigin;
	private int noOfPvtLeaveBusesOrigin;
	private int noOfDummyBusesOrigin;
	private String restTimeOrigin;
	private int totalBusesOrigin;
	private String abbreviationOrigin;
	private String permitNoOrigin;
	private String busNoOrigin;
	private String startTimeOrigin;
	private String endTimeOrigin;
	private boolean fixBusOrigin;

	private int noOfTripsDestination;
	private int noOfPvtBusesDestination;
	private int noOfTemporaryBusesDestination;
	private int noOfCtbDestination;
	private int noOfEtcDestination;
	private int noOfPvtLeaveBusesDestination;
	private int noOfDummyBusesDestination;
	private String restTimeDestination;
	private int totalBusesDestination;
	private String abbreviationDestination;
	private String permitNoDestination;
	private String busNoDestination;
	private String startTimeDestination;
	private String endTimeDestination;
	private boolean fixBusDestination;

	public int getNoOfTripsOrigin() {
		return noOfTripsOrigin;
	}

	public void setNoOfTripsOrigin(int noOfTripsOrigin) {
		this.noOfTripsOrigin = noOfTripsOrigin;
	}

	public int getNoOfPvtBusesOrigin() {
		return noOfPvtBusesOrigin;
	}

	public void setNoOfPvtBusesOrigin(int noOfPvtBusesOrigin) {
		this.noOfPvtBusesOrigin = noOfPvtBusesOrigin;
	}

	public int getNoOfTemporaryBusesOrigin() {
		return noOfTemporaryBusesOrigin;
	}

	public void setNoOfTemporaryBusesOrigin(int noOfTemporaryBusesOrigin) {
		this.noOfTemporaryBusesOrigin = noOfTemporaryBusesOrigin;
	}

	public int getNoOfCtbOrigin() {
		return noOfCtbOrigin;
	}

	public void setNoOfCtbOrigin(int noOfCtbOrigin) {
		this.noOfCtbOrigin = noOfCtbOrigin;
	}

	public int getNoOfEtcOrigin() {
		return noOfEtcOrigin;
	}

	public void setNoOfEtcOrigin(int noOfEtcOrigin) {
		this.noOfEtcOrigin = noOfEtcOrigin;
	}

	public int getNoOfPvtLeaveBusesOrigin() {
		return noOfPvtLeaveBusesOrigin;
	}

	public void setNoOfPvtLeaveBusesOrigin(int noOfPvtLeaveBusesOrigin) {
		this.noOfPvtLeaveBusesOrigin = noOfPvtLeaveBusesOrigin;
	}

	public int getNoOfDummyBusesOrigin() {
		return noOfDummyBusesOrigin;
	}

	public void setNoOfDummyBusesOrigin(int noOfDummyBusesOrigin) {
		this.noOfDummyBusesOrigin = noOfDummyBusesOrigin;
	}

	public String getRestTimeOrigin() {
		return restTimeOrigin;
	}

	public void setRestTimeOrigin(String restTimeOrigin) {
		this.restTimeOrigin = restTimeOrigin;
	}

	public int getTotalBusesOrigin() {
		return totalBusesOrigin;
	}

	public void setTotalBusesOrigin(int totalBusesOrigin) {
		this.totalBusesOrigin = totalBusesOrigin;
	}

	public String getAbbreviationOrigin() {
		return abbreviationOrigin;
	}

	public void setAbbreviationOrigin(String abbreviationOrigin) {
		this.abbreviationOrigin = abbreviationOrigin;
	}

	public String getPermitNoOrigin() {
		return permitNoOrigin;
	}

	public void setPermitNoOrigin(String permitNoOrigin) {
		this.permitNoOrigin = permitNoOrigin;
	}

	public String getBusNoOrigin() {
		return busNoOrigin;
	}

	public void setBusNoOrigin(String busNoOrigin) {
		this.busNoOrigin = busNoOrigin;
	}

	public String getStartTimeOrigin() {
		return startTimeOrigin;
	}

	public void setStartTimeOrigin(String startTimeOrigin) {
		this.startTimeOrigin = startTimeOrigin;
	}

	public String getEndTimeOrigin() {
		return endTimeOrigin;
	}

	public void setEndTimeOrigin(String endTimeOrigin) {
		this.endTimeOrigin = endTimeOrigin;
	}

	public boolean isFixBusOrigin() {
		return fixBusOrigin;
	}

	public void setFixBusOrigin(boolean fixBusOrigin) {
		this.fixBusOrigin = fixBusOrigin;
	}

	public int getNoOfTripsDestination() {
		return noOfTripsDestination;
	}

	public void setNoOfTripsDestination(int noOfTripsDestination) {
		this.noOfTripsDestination = noOfTripsDestination;
	}

	public int getNoOfPvtBusesDestination() {
		return noOfPvtBusesDestination;
	}

	public void setNoOfPvtBusesDestination(int noOfPvtBusesDestination) {
		this.noOfPvtBusesDestination = noOfPvtBusesDestination;
	}

	public int getNoOfTemporaryBusesDestination() {
		return noOfTemporaryBusesDestination;
	}

	public void setNoOfTemporaryBusesDestination(int noOfTemporaryBusesDestination) {
		this.noOfTemporaryBusesDestination = noOfTemporaryBusesDestination;
	}

	public int getNoOfCtbDestination() {
		return noOfCtbDestination;
	}

	public void setNoOfCtbDestination(int noOfCtbDestination) {
		this.noOfCtbDestination = noOfCtbDestination;
	}

	public int getNoOfEtcDestination() {
		return noOfEtcDestination;
	}

	public void setNoOfEtcDestination(int noOfEtcDestination) {
		this.noOfEtcDestination = noOfEtcDestination;
	}

	public int getNoOfPvtLeaveBusesDestination() {
		return noOfPvtLeaveBusesDestination;
	}

	public void setNoOfPvtLeaveBusesDestination(int noOfPvtLeaveBusesDestination) {
		this.noOfPvtLeaveBusesDestination = noOfPvtLeaveBusesDestination;
	}

	public int getNoOfDummyBusesDestination() {
		return noOfDummyBusesDestination;
	}

	public void setNoOfDummyBusesDestination(int noOfDummyBusesDestination) {
		this.noOfDummyBusesDestination = noOfDummyBusesDestination;
	}

	public String getRestTimeDestination() {
		return restTimeDestination;
	}

	public void setRestTimeDestination(String restTimeDestination) {
		this.restTimeDestination = restTimeDestination;
	}

	public int getTotalBusesDestination() {
		return totalBusesDestination;
	}

	public void setTotalBusesDestination(int totalBusesDestination) {
		this.totalBusesDestination = totalBusesDestination;
	}

	public String getAbbreviationDestination() {
		return abbreviationDestination;
	}

	public void setAbbreviationDestination(String abbreviationDestination) {
		this.abbreviationDestination = abbreviationDestination;
	}

	public String getPermitNoDestination() {
		return permitNoDestination;
	}

	public void setPermitNoDestination(String permitNoDestination) {
		this.permitNoDestination = permitNoDestination;
	}

	public String getBusNoDestination() {
		return busNoDestination;
	}

	public void setBusNoDestination(String busNoDestination) {
		this.busNoDestination = busNoDestination;
	}

	public String getStartTimeDestination() {
		return startTimeDestination;
	}

	public void setStartTimeDestination(String startTimeDestination) {
		this.startTimeDestination = startTimeDestination;
	}

	public String getEndTimeDestination() {
		return endTimeDestination;
	}

	public void setEndTimeDestination(String endTimeDestination) {
		this.endTimeDestination = endTimeDestination;
	}

	public boolean isFixBusDestination() {
		return fixBusDestination;
	}

	public void setFixBusDestination(boolean fixBusDestination) {
		this.fixBusDestination = fixBusDestination;
	}

	public String getBusCategory() {
		return busCategory;
	}

	public void setBusCategory(String busCategory) {
		this.busCategory = busCategory;
	}

	public String getRouteNo() {
		return routeNo;
	}

	public void setRouteNo(String routeNo) {
		this.routeNo = routeNo;
	}

	public String getOriginStartTimeString() {
		return originStartTimeString;
	}

	public void setOriginStartTimeString(String originStartTimeString) {
		this.originStartTimeString = originStartTimeString;
	}

	public String getOriginEndTimeString() {
		return originEndTimeString;
	}

	public void setOriginEndTimeString(String originEndTimeString) {
		this.originEndTimeString = originEndTimeString;
	}

	public String getOriginDivideRangeString() {
		return originDivideRangeString;
	}

	public void setOriginDivideRangeString(String originDivideRangeString) {
		this.originDivideRangeString = originDivideRangeString;
	}

	public String getDestinationStartTimeString() {
		return destinationStartTimeString;
	}

	public void setDestinationStartTimeString(String destinationStartTimeString) {
		this.destinationStartTimeString = destinationStartTimeString;
	}

	public String getDestinationEndTimeString() {
		return destinationEndTimeString;
	}

	public void setDestinationEndTimeString(String destinationEndTimeString) {
		this.destinationEndTimeString = destinationEndTimeString;
	}

	public String getDestinationDivideRangeString() {
		return destinationDivideRangeString;
	}

	public void setDestinationDivideRangeString(String destinationDivideRangeString) {
		this.destinationDivideRangeString = destinationDivideRangeString;
	}

	public String getFirstValue() {
		return firstValue;
	}

	public void setFirstValue(String firstValue) {
		this.firstValue = firstValue;
	}

	public String getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}

	public int getNoOfBuses() {
		return noOfBuses;
	}

	public void setNoOfBuses(int noOfBuses) {
		this.noOfBuses = noOfBuses;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getNoOfPVTbuessOriginOne() {
		return noOfPVTbuessOriginOne;
	}

	public void setNoOfPVTbuessOriginOne(int noOfPVTbuessOriginOne) {
		this.noOfPVTbuessOriginOne = noOfPVTbuessOriginOne;
	}

	public int getNoOfCTBbuessOriginOne() {
		return noOfCTBbuessOriginOne;
	}

	public void setNoOfCTBbuessOriginOne(int noOfCTBbuessOriginOne) {
		this.noOfCTBbuessOriginOne = noOfCTBbuessOriginOne;
	}

	public int getNoOfOtherbuessOriginOne() {
		return noOfOtherbuessOriginOne;
	}

	public void setNoOfOtherbuessOriginOne(int noOfOtherbuessOriginOne) {
		this.noOfOtherbuessOriginOne = noOfOtherbuessOriginOne;
	}

	public int getRestTimeOriginOne() {
		return restTimeOriginOne;
	}

	public void setRestTimeOriginOne(int restTimeOriginOne) {
		this.restTimeOriginOne = restTimeOriginOne;
	}

	public int getTotalBusesOriginOne() {
		return totalBusesOriginOne;
	}

	public void setTotalBusesOriginOne(int totalBusesOriginOne) {
		this.totalBusesOriginOne = totalBusesOriginOne;
	}

	public int getNoOfPVTbuessDestinationOne() {
		return noOfPVTbuessDestinationOne;
	}

	public void setNoOfPVTbuessDestinationOne(int noOfPVTbuessDestinationOne) {
		this.noOfPVTbuessDestinationOne = noOfPVTbuessDestinationOne;
	}

	public int getNoOfCTBbuessDestinationOne() {
		return noOfCTBbuessDestinationOne;
	}

	public void setNoOfCTBbuessDestinationOne(int noOfCTBbuessDestinationOne) {
		this.noOfCTBbuessDestinationOne = noOfCTBbuessDestinationOne;
	}

	public int getNoOfOtherbuessDestinationOne() {
		return noOfOtherbuessDestinationOne;
	}

	public void setNoOfOtherbuessDestinationOne(int noOfOtherbuessDestinationOne) {
		this.noOfOtherbuessDestinationOne = noOfOtherbuessDestinationOne;
	}

	public int getRestTimeDestinationOne() {
		return restTimeDestinationOne;
	}

	public void setRestTimeDestinationOne(int restTimeDestinationOne) {
		this.restTimeDestinationOne = restTimeDestinationOne;
	}

	public int getTotalBusesDestinationOne() {
		return totalBusesDestinationOne;
	}

	public void setTotalBusesDestinationOne(int totalBusesDestinationOne) {
		this.totalBusesDestinationOne = totalBusesDestinationOne;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNoOfPVTbuessOriginTwo() {
		return noOfPVTbuessOriginTwo;
	}

	public void setNoOfPVTbuessOriginTwo(int noOfPVTbuessOriginTwo) {
		this.noOfPVTbuessOriginTwo = noOfPVTbuessOriginTwo;
	}

	public int getNoOfCTBbuessOriginTwo() {
		return noOfCTBbuessOriginTwo;
	}

	public void setNoOfCTBbuessOriginTwo(int noOfCTBbuessOriginTwo) {
		this.noOfCTBbuessOriginTwo = noOfCTBbuessOriginTwo;
	}

	public int getNoOfOtherbuessOriginTwo() {
		return noOfOtherbuessOriginTwo;
	}

	public void setNoOfOtherbuessOriginTwo(int noOfOtherbuessOriginTwo) {
		this.noOfOtherbuessOriginTwo = noOfOtherbuessOriginTwo;
	}

	public int getRestTimeOriginTwo() {
		return restTimeOriginTwo;
	}

	public void setRestTimeOriginTwo(int restTimeOriginTwo) {
		this.restTimeOriginTwo = restTimeOriginTwo;
	}

	public int getTotalBusesOriginTwo() {
		return totalBusesOriginTwo;
	}

	public void setTotalBusesOriginTwo(int totalBusesOriginTwo) {
		this.totalBusesOriginTwo = totalBusesOriginTwo;
	}

	public int getNoOfPVTbuessDestinationTwo() {
		return noOfPVTbuessDestinationTwo;
	}

	public void setNoOfPVTbuessDestinationTwo(int noOfPVTbuessDestinationTwo) {
		this.noOfPVTbuessDestinationTwo = noOfPVTbuessDestinationTwo;
	}

	public int getNoOfCTBbuessDestinationTwo() {
		return noOfCTBbuessDestinationTwo;
	}

	public void setNoOfCTBbuessDestinationTwo(int noOfCTBbuessDestinationTwo) {
		this.noOfCTBbuessDestinationTwo = noOfCTBbuessDestinationTwo;
	}

	public int getNoOfOtherbuessDestinationTwo() {
		return noOfOtherbuessDestinationTwo;
	}

	public void setNoOfOtherbuessDestinationTwo(int noOfOtherbuessDestinationTwo) {
		this.noOfOtherbuessDestinationTwo = noOfOtherbuessDestinationTwo;
	}

	public int getRestTimeDestinationTwo() {
		return restTimeDestinationTwo;
	}

	public void setRestTimeDestinationTwo(int restTimeDestinationTwo) {
		this.restTimeDestinationTwo = restTimeDestinationTwo;
	}

	public int getTotalBusesDestinationTwo() {
		return totalBusesDestinationTwo;
	}

	public void setTotalBusesDestinationTwo(int totalBusesDestinationTwo) {
		this.totalBusesDestinationTwo = totalBusesDestinationTwo;
	}

	public int getNoOfPVTbuessOriginThree() {
		return noOfPVTbuessOriginThree;
	}

	public void setNoOfPVTbuessOriginThree(int noOfPVTbuessOriginThree) {
		this.noOfPVTbuessOriginThree = noOfPVTbuessOriginThree;
	}

	public int getNoOfCTBbuessOriginThree() {
		return noOfCTBbuessOriginThree;
	}

	public void setNoOfCTBbuessOriginThree(int noOfCTBbuessOriginThree) {
		this.noOfCTBbuessOriginThree = noOfCTBbuessOriginThree;
	}

	public int getNoOfOtherbuessOriginThree() {
		return noOfOtherbuessOriginThree;
	}

	public void setNoOfOtherbuessOriginThree(int noOfOtherbuessOriginThree) {
		this.noOfOtherbuessOriginThree = noOfOtherbuessOriginThree;
	}

	public int getRestTimeOriginThree() {
		return restTimeOriginThree;
	}

	public void setRestTimeOriginThree(int restTimeOriginThree) {
		this.restTimeOriginThree = restTimeOriginThree;
	}

	public int getTotalBusesOriginThree() {
		return totalBusesOriginThree;
	}

	public void setTotalBusesOriginThree(int totalBusesOriginThree) {
		this.totalBusesOriginThree = totalBusesOriginThree;
	}

	public int getNoOfPVTbuessDestinationThree() {
		return noOfPVTbuessDestinationThree;
	}

	public void setNoOfPVTbuessDestinationThree(int noOfPVTbuessDestinationThree) {
		this.noOfPVTbuessDestinationThree = noOfPVTbuessDestinationThree;
	}

	public int getNoOfCTBbuessDestinationThree() {
		return noOfCTBbuessDestinationThree;
	}

	public void setNoOfCTBbuessDestinationThree(int noOfCTBbuessDestinationThree) {
		this.noOfCTBbuessDestinationThree = noOfCTBbuessDestinationThree;
	}

	public int getNoOfOtherbuessDestinationThree() {
		return noOfOtherbuessDestinationThree;
	}

	public void setNoOfOtherbuessDestinationThree(int noOfOtherbuessDestinationThree) {
		this.noOfOtherbuessDestinationThree = noOfOtherbuessDestinationThree;
	}

	public int getRestTimeDestinationThree() {
		return restTimeDestinationThree;
	}

	public void setRestTimeDestinationThree(int restTimeDestinationThree) {
		this.restTimeDestinationThree = restTimeDestinationThree;
	}

	public int getTotalBusesDestinationThree() {
		return totalBusesDestinationThree;
	}

	public void setTotalBusesDestinationThree(int totalBusesDestinationThree) {
		this.totalBusesDestinationThree = totalBusesDestinationThree;
	}

	public int getTotalTripsOriginOne() {
		return totalTripsOriginOne;
	}

	public void setTotalTripsOriginOne(int totalTripsOriginOne) {
		this.totalTripsOriginOne = totalTripsOriginOne;
	}

	public int getTotalTripsDestinationOne() {
		return totalTripsDestinationOne;
	}

	public void setTotalTripsDestinationOne(int totalTripsDestinationOne) {
		this.totalTripsDestinationOne = totalTripsDestinationOne;
	}

	public int getTotalTripsOriginTwo() {
		return totalTripsOriginTwo;
	}

	public void setTotalTripsOriginTwo(int totalTripsOriginTwo) {
		this.totalTripsOriginTwo = totalTripsOriginTwo;
	}

	public int getTotalTripsDestinationTwo() {
		return totalTripsDestinationTwo;
	}

	public void setTotalTripsDestinationTwo(int totalTripsDestinationTwo) {
		this.totalTripsDestinationTwo = totalTripsDestinationTwo;
	}

	public int getTotalTripsOriginThree() {
		return totalTripsOriginThree;
	}

	public void setTotalTripsOriginThree(int totalTripsOriginThree) {
		this.totalTripsOriginThree = totalTripsOriginThree;
	}

	public int getTotalTripsDestinationThree() {
		return totalTripsDestinationThree;
	}

	public void setTotalTripsDestinationThree(int totalTripsDestinationThree) {
		this.totalTripsDestinationThree = totalTripsDestinationThree;
	}

	public String getTimeRange() {
		return timeRange;
	}

	public String getGenereatedRefNo() {
		return genereatedRefNo;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

	public String getTripRefNo() {
		return tripRefNo;
	}

	public void setTripRefNo(String tripRefNo) {
		this.tripRefNo = tripRefNo;
	}

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getTimeRangeStart() {
		return timeRangeStart;
	}

	public void setTimeRangeStart(String timeRangeStart) {
		this.timeRangeStart = timeRangeStart;
	}

	public String getTimeRangeEnd() {
		return timeRangeEnd;
	}

	public void setTimeRangeEnd(String timeRangeEnd) {
		this.timeRangeEnd = timeRangeEnd;
	}

	public void setGenereatedRefNo(String genereatedRefNo) {
		this.genereatedRefNo = genereatedRefNo;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getNoOfPVTbueses() {
		return noOfPVTbueses;
	}

	public void setNoOfPVTbueses(int noOfPVTbueses) {
		this.noOfPVTbueses = noOfPVTbueses;
	}

	public int getNoOfCTBbueses() {
		return noOfCTBbueses;
	}

	public void setNoOfCTBbueses(int noOfCTBbueses) {
		this.noOfCTBbueses = noOfCTBbueses;
	}

	public int getNoOfOtherbuses() {
		return noOfOtherbuses;
	}

	public void setNoOfOtherbuses(int noOfOtherbuses) {
		this.noOfOtherbuses = noOfOtherbuses;
	}

	public String getRestTime() {
		return restTime;
	}

	public void setRestTime(String restTime) {
		this.restTime = restTime;
	}

	public int getTotalBuses() {
		return totalBuses;
	}

	public void setTotalBuses(int totalBuses) {
		this.totalBuses = totalBuses;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getTotalTrips() {
		return totalTrips;
	}

	public void setTotalTrips(int totalTrips) {
		this.totalTrips = totalTrips;
	}

	public String getBusCategoryDes() {
		return busCategoryDes;
	}

	public void setBusCategoryDes(String busCategoryDes) {
		this.busCategoryDes = busCategoryDes;
	}

	public String getTimeRangeStr() {
		return timeRangeStr;
	}

	public void setTimeRangeStr(String timeRangeStr) {
		this.timeRangeStr = timeRangeStr;
	}

	public String getTripRefNoStr() {
		return tripRefNoStr;
	}

	public void setTripRefNoStr(String tripRefNoStr) {
		this.tripRefNoStr = tripRefNoStr;
	}

	public String getTimeStartVal() {
		return timeStartVal;
	}

	public void setTimeStartVal(String timeStartVal) {
		this.timeStartVal = timeStartVal;
	}

	public String getTimeEndVal() {
		return timeEndVal;
	}

	public void setTimeEndVal(String timeEndVal) {
		this.timeEndVal = timeEndVal;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public int getBusCount() {
		return busCount;
	}

	public void setBusCount(int busCount) {
		this.busCount = busCount;
	}

	public int getBusTypeName() {
		return busTypeName;
	}

	public void setBusTypeName(int busTypeName) {
		this.busTypeName = busTypeName;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAssigneBusNo() {
		return assigneBusNo;
	}

	public void setAssigneBusNo(String assigneBusNo) {
		this.assigneBusNo = assigneBusNo;
	}

	public boolean isCtbBus() {
		return ctbBus;
	}

	public void setCtbBus(boolean ctbBus) {
		this.ctbBus = ctbBus;
	}

	public String getGroupOneDays() {
		return groupOneDays;
	}

	public void setGroupOneDays(String groupOneDays) {
		this.groupOneDays = groupOneDays;
	}

	public String getGroupTwoDays() {
		return groupTwoDays;
	}

	public void setGroupTwoDays(String groupTwoDays) {
		this.groupTwoDays = groupTwoDays;
	}

	public String getGroupThreeDays() {
		return groupThreeDays;
	}

	public void setGroupThreeDays(String groupThreeDays) {
		this.groupThreeDays = groupThreeDays;
	}

	public long getMasterSeq() {
		return masterSeq;
	}

	public void setMasterSeq(long masterSeq) {
		this.masterSeq = masterSeq;
	}

	public String getGroupOne() {
		return groupOne;
	}

	public void setGroupOne(String groupOne) {
		this.groupOne = groupOne;
	}

	public String getGroupTwo() {
		return groupTwo;
	}

	public void setGroupTwo(String groupTwo) {
		this.groupTwo = groupTwo;
	}

	public String getGroupThree() {
		return groupThree;
	}

	public void setGroupThree(String groupThree) {
		this.groupThree = groupThree;
	}

	public String getTimeRangeTo() {
		return timeRangeTo;
	}

	public void setTimeRangeTo(String timeRangeTo) {
		this.timeRangeTo = timeRangeTo;
	}

	public String getDesToOrGroupOne() {
		return DesToOrGroupOne;
	}

	public void setDesToOrGroupOne(String desToOrGroupOne) {
		DesToOrGroupOne = desToOrGroupOne;
	}

	public String getDesToOrGroupTwo() {
		return DesToOrGroupTwo;
	}

	public void setDesToOrGroupTwo(String desToOrGroupTwo) {
		DesToOrGroupTwo = desToOrGroupTwo;
	}

	public String getDesToOrGroupThree() {
		return DesToOrGroupThree;
	}

	public void setDesToOrGroupThree(String desToOrGroupThree) {
		DesToOrGroupThree = desToOrGroupThree;
	}

	public int getNoOfTrips() {
		return noOfTrips;
	}

	public void setNoOfTrips(int noOfTrips) {
		this.noOfTrips = noOfTrips;
	}

	public int getRestTimeInt() {
		return restTimeInt;
	}

	public void setRestTimeInt(int restTimeInt) {
		this.restTimeInt = restTimeInt;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getWithFixedTimeCode() {
		return withFixedTimeCode;
	}

	public void setWithFixedTimeCode(String withFixedTimeCode) {
		this.withFixedTimeCode = withFixedTimeCode;
	}

	public String getWithOutFixedTimeCode() {
		return withOutFixedTimeCode;
	}

	public void setWithOutFixedTimeCode(String withOutFixedTimeCode) {
		this.withOutFixedTimeCode = withOutFixedTimeCode;
	}

	public String getWithFixedBusesCode() {
		return withFixedBusesCode;
	}

	public void setWithFixedBusesCode(String withFixedBusesCode) {
		this.withFixedBusesCode = withFixedBusesCode;
	}

	public String getCreateBywithOutFixedTimeCode() {
		return createBywithOutFixedTimeCode;
	}

	public void setCreateBywithOutFixedTimeCode(String createBywithOutFixedTimeCode) {
		this.createBywithOutFixedTimeCode = createBywithOutFixedTimeCode;
	}

	public String getCreateBywithFixedBusesCode() {
		return createBywithFixedBusesCode;
	}

	public void setCreateBywithFixedBusesCode(String createBywithFixedBusesCode) {
		this.createBywithFixedBusesCode = createBywithFixedBusesCode;
	}

	public Timestamp getCreateDatewithOutFixedTimeCode() {
		return createDatewithOutFixedTimeCode;
	}

	public void setCreateDatewithOutFixedTimeCode(Timestamp createDatewithOutFixedTimeCode) {
		this.createDatewithOutFixedTimeCode = createDatewithOutFixedTimeCode;
	}

	public Timestamp getCreateDatewithFixedBusesCode() {
		return createDatewithFixedBusesCode;
	}

	public void setCreateDatewithFixedBusesCode(Timestamp createDatewithFixedBusesCode) {
		this.createDatewithFixedBusesCode = createDatewithFixedBusesCode;
	}

	public long getTimeTableDetSeq() {
		return timeTableDetSeq;
	}

	public void setTimeTableDetSeq(long timeTableDetSeq) {
		this.timeTableDetSeq = timeTableDetSeq;
	}

	public int getTripIdInt() {
		return tripIdInt;
	}

	public void setTripIdInt(int tripIdInt) {
		this.tripIdInt = tripIdInt;
	}

	public boolean isFixedTime() {
		return fixedTime;
	}

	public void setFixedTime(boolean fixedTime) {
		this.fixedTime = fixedTime;
	}

	public String getAbbriAtOrigin() {
		return abbriAtOrigin;
	}

	public void setAbbriAtOrigin(String abbriAtOrigin) {
		this.abbriAtOrigin = abbriAtOrigin;
	}

	public String getAbbriAtDestination() {
		return abbriAtDestination;
	}

	public void setAbbriAtDestination(String abbriAtDestination) {
		this.abbriAtDestination = abbriAtDestination;
	}

	public String getBusType1() {
		return busType1;
	}

	public void setBusType1(String busType1) {
		this.busType1 = busType1;
	}

	public String getBusType2() {
		return busType2;
	}

	public void setBusType2(String busType2) {
		this.busType2 = busType2;
	}

	public String getBusType3() {
		return busType3;
	}

	public void setBusType3(String busType3) {
		this.busType3 = busType3;
	}

	public String getBusTypeDtoO1() {
		return busTypeDtoO1;
	}

	public void setBusTypeDtoO1(String busTypeDtoO1) {
		this.busTypeDtoO1 = busTypeDtoO1;
	}

	public String getBusTypeDtoO2() {
		return busTypeDtoO2;
	}

	public void setBusTypeDtoO2(String busTypeDtoO2) {
		this.busTypeDtoO2 = busTypeDtoO2;
	}

	public String getBusTypeDtoO3() {
		return busTypeDtoO3;
	}

	public void setBusTypeDtoO3(String busTypeDtoO3) {
		this.busTypeDtoO3 = busTypeDtoO3;
	}

	public String getPanelGenNo() {
		return panelGenNo;
	}

	public void setPanelGenNo(String panelGenNo) {
		this.panelGenNo = panelGenNo;
	}

	public boolean isAlreadyAdded() {
		return alreadyAdded;
	}

	public void setAlreadyAdded(boolean alreadyAdded) {
		this.alreadyAdded = alreadyAdded;
	}

	public int getNoOfTripsG10() {
		return noOfTripsG10;
	}

	public void setNoOfTripsG10(int noOfTripsG10) {
		this.noOfTripsG10 = noOfTripsG10;
	}

	public int getNoOfTripsG20() {
		return noOfTripsG20;
	}

	public void setNoOfTripsG20(int noOfTripsG20) {
		this.noOfTripsG20 = noOfTripsG20;
	}

	public int getNoOfTripsG30() {
		return noOfTripsG30;
	}

	public void setNoOfTripsG30(int noOfTripsG30) {
		this.noOfTripsG30 = noOfTripsG30;
	}

	public int getNoOfTripsG1D() {
		return noOfTripsG1D;
	}

	public void setNoOfTripsG1D(int noOfTripsG1D) {
		this.noOfTripsG1D = noOfTripsG1D;
	}

	public int getNoOfTripsG2D() {
		return noOfTripsG2D;
	}

	public void setNoOfTripsG2D(int noOfTripsG2D) {
		this.noOfTripsG2D = noOfTripsG2D;
	}

	public int getNoOfTripsG3D() {
		return noOfTripsG3D;
	}

	public void setNoOfTripsG3D(int noOfTripsG3D) {
		this.noOfTripsG3D = noOfTripsG3D;
	}

	public String getDuplicateBusNum() {
		return duplicateBusNum;
	}

	public void setDuplicateBusNum(String duplicateBusNum) {
		this.duplicateBusNum = duplicateBusNum;
	}

	public String getTempStartTime() {
		return tempStartTime;
	}

	public void setTempStartTime(String tempStartTime) {
		this.tempStartTime = tempStartTime;
	}

	public String getTempEndTime() {
		return tempEndTime;
	}

	public void setTempEndTime(String tempEndTime) {
		this.tempEndTime = tempEndTime;
	}

	public int getBusesOnLeaveOriginOne() {
		return busesOnLeaveOriginOne;
	}

	public void setBusesOnLeaveOriginOne(int busesOnLeaveOriginOne) {
		this.busesOnLeaveOriginOne = busesOnLeaveOriginOne;
	}

	public int getBusesOnLeaveDestinationOne() {
		return busesOnLeaveDestinationOne;
	}

	public void setBusesOnLeaveDestinationOne(int busesOnLeaveDestinationOne) {
		this.busesOnLeaveDestinationOne = busesOnLeaveDestinationOne;
	}

	public int getBusesOnLeaveOriginTwo() {
		return busesOnLeaveOriginTwo;
	}

	public void setBusesOnLeaveOriginTwo(int busesOnLeaveOriginTwo) {
		this.busesOnLeaveOriginTwo = busesOnLeaveOriginTwo;
	}

	public int getBusesOnLeaveDestinationTwo() {
		return busesOnLeaveDestinationTwo;
	}

	public void setBusesOnLeaveDestinationTwo(int busesOnLeaveDestinationTwo) {
		this.busesOnLeaveDestinationTwo = busesOnLeaveDestinationTwo;
	}

	public int getBusesOnLeaveOriginThree() {
		return busesOnLeaveOriginThree;
	}

	public void setBusesOnLeaveOriginThree(int busesOnLeaveOriginThree) {
		this.busesOnLeaveOriginThree = busesOnLeaveOriginThree;
	}

	public int getBusesOnLeaveDestinationThree() {
		return busesOnLeaveDestinationThree;
	}

	public void setBusesOnLeaveDestinationThree(int busesOnLeaveDestinationThree) {
		this.busesOnLeaveDestinationThree = busesOnLeaveDestinationThree;
	}

	public int getNumOfLeaves() {
		return numOfLeaves;
	}

	public void setNumOfLeaves(int numOfLeaves) {
		this.numOfLeaves = numOfLeaves;
	}

	public boolean isRowColor() {
		return rowColor;
	}

	public void setRowColor(boolean rowColor) {
		this.rowColor = rowColor;
	}

	public String getPermiteExpireDate() {
		return permiteExpireDate;
	}

	public void setPermiteExpireDate(String permiteExpireDate) {
		this.permiteExpireDate = permiteExpireDate;
	}

	public String getTimeDifferece() {
		return timeDifferece;
	}

	public void setTimeDifferece(String timeDifferece) {
		this.timeDifferece = timeDifferece;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isRowDisable() {
		return rowDisable;
	}

	public void setRowDisable(boolean rowDisable) {
		this.rowDisable = rowDisable;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ArrayList<ArrayList<Integer>> getMonthArray() {
		return monthArray;
	}

	public void setMonthArray(ArrayList<ArrayList<Integer>> monthArray) {
		this.monthArray = monthArray;
	}

	public String getMidPoint() {
		return midPoint;
	}

	public void setMidPoint(String midPoint) {
		this.midPoint = midPoint;
	}

	public boolean isApplicable() {
		return isApplicable;
	}

	public void setApplicable(boolean isApplicable) {
		this.isApplicable = isApplicable;
	}

	public String getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}

	public boolean getIsDailyRotation() {
		return isDailyRotation;
	}

	public void setDailyRotation(boolean isDailyRotation) {
		this.isDailyRotation = isDailyRotation;
	}

	public boolean getIsTwoDayRotation() {
		return isTwoDayRotation;
	}

	public void setTwoDayRotation(boolean isTwoDayRotation) {
		this.isTwoDayRotation = isTwoDayRotation;
	}

	public boolean getIsOrigin() {
		return isOrigin;
	}

	public void setIsOrigin(boolean isOrigin) {
		this.isOrigin = isOrigin;
	}

	public String getNoOfTimeTablesPerWeek() {
		return noOfTimeTablesPerWeek;
	}

	public void setNoOfTimeTablesPerWeek(String noOfTimeTablesPerWeek) {
		this.noOfTimeTablesPerWeek = noOfTimeTablesPerWeek;
	}

	public String getRouteStatus() {
		return routeStatus;
	}

	public void setRouteStatus(String routeStatus) {
		this.routeStatus = routeStatus;
	}

	public void setOrigin(boolean isOrigin) {
		this.isOrigin = isOrigin;
	}

	public String getAbbreviationLeave() {
		return abbreviationLeave;
	}

	public void setAbbreviationLeave(String abbreviationLeave) {
		this.abbreviationLeave = abbreviationLeave;
	}

	public String getPermitNoLeave() {
		return permitNoLeave;
	}

	public void setPermitNoLeave(String permitNoLeave) {
		this.permitNoLeave = permitNoLeave;
	}

	public String getBusNoLeave() {
		return busNoLeave;
	}

	public void setBusNoLeave(String busNoLeave) {
		this.busNoLeave = busNoLeave;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getTraveltime() {
		return traveltime;
	}

	public void setTraveltime(String traveltime) {
		this.traveltime = traveltime;
	}

	public String getBusSpeed() {
		return busSpeed;
	}

	public void setBusSpeed(String busSpeed) {
		this.busSpeed = busSpeed;
	}

	public String getDistanceString() {
		return distanceString;
	}

	public void setDistanceString(String distanceString) {
		this.distanceString = distanceString;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public boolean isForSubmit() {
		return isForSubmit;
	}

	public void setForSubmit(boolean isForSubmit) {
		this.isForSubmit = isForSubmit;
	}

	public String getAbbreviationLeaveDes() {
		return abbreviationLeaveDes;
	}

	public void setAbbreviationLeaveDes(String abbreviationLeaveDes) {
		this.abbreviationLeaveDes = abbreviationLeaveDes;
	}

	public String getPermitNoLeaveDes() {
		return permitNoLeaveDes;
	}

	public void setPermitNoLeaveDes(String permitNoLeaveDes) {
		this.permitNoLeaveDes = permitNoLeaveDes;
	}

	public String getBusNoLeaveDes() {
		return busNoLeaveDes;
	}

	public void setBusNoLeaveDes(String busNoLeaveDes) {
		this.busNoLeaveDes = busNoLeaveDes;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public boolean isFixBus() {
		return fixBus;
	}

	public void setFixBus(boolean fixBus) {
		this.fixBus = fixBus;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public boolean isCheckAbbri() {
		return checkAbbri;
	}

	public void setCheckAbbri(boolean checkAbbri) {
		this.checkAbbri = checkAbbri;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getAbbreviationDes() {
		return abbreviationDes;
	}

	public void setAbbreviationDes(String abbreviationDes) {
		this.abbreviationDes = abbreviationDes;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public String getArrivalOne() {
		return arrivalOne;
	}

	public void setArrivalOne(String arrivalOne) {
		this.arrivalOne = arrivalOne;
	}

	public String getDepature() {
		return depature;
	}

	public void setDepature(String depature) {
		this.depature = depature;
	}

	public String getDepatureOne() {
		return depatureOne;
	}

	public void setDepatureOne(String depatureOne) {
		this.depatureOne = depatureOne;
	}


	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getSteringHours() {
		return steringHours;
	}

	public void setSteringHours(String steringHours) {
		this.steringHours = steringHours;
	}

	public String getBreak1() {
		return break1;
	}

	public void setBreak1(String break1) {
		this.break1 = break1;
	}

	public String getBreak2() {
		return break2;
	}

	public void setBreak2(String break2) {
		this.break2 = break2;
	}

	public String getBreak3() {
		return break3;
	}

	public void setBreak3(String break3) {
		this.break3 = break3;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public long getTripno() {
		return tripno;
	}

	public void setTripno(long tripno) {
		this.tripno = tripno;
	}

	public String getArrival2() {
		return arrival2;
	}

	public void setArrival2(String arrival2) {
		this.arrival2 = arrival2;
	}

	public String getDepature2() {
		return depature2;
	}

	public void setDepature2(String depature2) {
		this.depature2 = depature2;
	}

	public String getArrival3() {
		return arrival3;
	}

	public void setArrival3(String arrival3) {
		this.arrival3 = arrival3;
	}

	public String getDepature3() {
		return depature3;
	}

	public void setDepature3(String depature3) {
		this.depature3 = depature3;
	}

	public String getArrival4() {
		return arrival4;
	}

	public void setArrival4(String arrival4) {
		this.arrival4 = arrival4;
	}

	public String getDepature4() {
		return depature4;
	}

	public void setDepature4(String depature4) {
		this.depature4 = depature4;
	}

	public String getArrival5() {
		return arrival5;
	}

	public void setArrival5(String arrival5) {
		this.arrival5 = arrival5;
	}

	public String getDepature5() {
		return depature5;
	}

	public void setDepature5(String depature5) {
		this.depature5 = depature5;
	}

	public long getAbbno() {
		return abbno;
	}

	public void setAbbno(long abbno) {
		this.abbno = abbno;
	}

	public boolean isCoupleTwo() {
		return coupleTwo;
	}

	public void setCoupleTwo(boolean coupleTwo) {
		this.coupleTwo = coupleTwo;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public long getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(long totalDistance) {
		this.totalDistance = totalDistance;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}
	

	
}
