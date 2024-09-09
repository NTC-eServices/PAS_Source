package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class PanelGeneratorWithoutFixedTimeServiceImpl implements Serializable, PanelGeneratorWithoutFixedTimeService {

	private static final long serialVersionUID = 1L;
	public static Logger logger = Logger.getLogger("PanelGeneratorWithoutFixedTimeServiceImpl");

	@Override
	public String retrieveDaysForGroup(String routeNo, String refNo, String groupNo) {
		logger.info("retrieveDaysForGroup start param: routeNo: " + routeNo + ", refNo: " + refNo + ", groupNo: "
				+ groupNo);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String days = null;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select d_sunday, d_monday, d_tuesday, d_wednesday, d_thursday, d_friday, d_saturday  from public.nt_t_panelgenerator_det "
					+ "where seq_panelgenerator=(select seq from public.nt_m_panelgenerator "
					+ "where route_no=? and ref_no=?) and group_no=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, refNo);
			ps.setString(3, groupNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getString("d_sunday") != null && !rs.getString("d_sunday").isEmpty()
						&& rs.getString("d_sunday").trim().equalsIgnoreCase("Y")) {
					days = "Sunday";
				}

				if (rs.getString("d_monday") != null && !rs.getString("d_monday").isEmpty()
						&& rs.getString("d_monday").trim().equalsIgnoreCase("Y")) {
					if (days != null && !days.isEmpty() && !days.trim().equalsIgnoreCase("")) {
						days = days + ", Monday";
					} else {
						days = "Monday";
					}

				}

				if (rs.getString("d_tuesday") != null && !rs.getString("d_tuesday").isEmpty()
						&& rs.getString("d_tuesday").trim().equalsIgnoreCase("Y")) {
					if (days != null && !days.isEmpty() && !days.trim().equalsIgnoreCase("")) {
						days = days + ", Tuesday";
					} else {
						days = "Tuesday";
					}
				}

				if (rs.getString("d_wednesday") != null && !rs.getString("d_wednesday").isEmpty()
						&& rs.getString("d_wednesday").trim().equalsIgnoreCase("Y")) {
					if (days != null && !days.isEmpty() && !days.trim().equalsIgnoreCase("")) {
						days = days + ", Wednesday";
					} else {
						days = "Wednesday";
					}
				}

				if (rs.getString("d_thursday") != null && !rs.getString("d_thursday").isEmpty()
						&& rs.getString("d_thursday").trim().equalsIgnoreCase("Y")) {
					if (days != null && !days.isEmpty() && !days.trim().equalsIgnoreCase("")) {
						days = days + ", Thursday";
					} else {
						days = "Thursday";
					}
				}

				if (rs.getString("d_friday") != null && !rs.getString("d_friday").isEmpty()
						&& rs.getString("d_friday").trim().equalsIgnoreCase("Y")) {
					if (days != null && !days.isEmpty() && !days.trim().equalsIgnoreCase("")) {
						days = days + ", Friday";
					} else {
						days = "Friday";
					}
				}

				if (rs.getString("d_saturday") != null && !rs.getString("d_saturday").isEmpty()
						&& rs.getString("d_saturday").trim().equalsIgnoreCase("Y")) {
					if (days != null && !days.isEmpty() && !days.trim().equalsIgnoreCase("")) {
						days = days + ", Saturday";
					} else {
						days = "Saturday";
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("retrieveDaysForGroup error : " + e.toString());
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("retrieveDaysForGroup end");
		return days;
	}

	@Override
	public List<String> retrieveStartEndTimes(String routeNo, String refNum, String groupNo) {
		logger.info("retrieveStartEndTimes start param: routeNo: " + routeNo + ", refNum: " + refNum + ", groupNo: "
				+ groupNo);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> startEndTimes = new ArrayList<String>();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select origin_start_time, origin_end_time, destination_start_time, destination_end_time  "
					+ "from public.nt_t_panelgenerator_det "
					+ "where seq_panelgenerator=(select seq from public.nt_m_panelgenerator where route_no=? and ref_no=?) and group_no=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			ps.setString(2, refNum);
			ps.setString(3, groupNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				startEndTimes.add(rs.getString("origin_start_time"));
				startEndTimes.add(rs.getString("origin_end_time"));
				startEndTimes.add(rs.getString("destination_start_time"));
				startEndTimes.add(rs.getString("destination_end_time"));
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("retrieveStartEndTimes error: " + e.toString());
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("retrieveStartEndTimes end");
		return startEndTimes;
	}

	@Override
	public String retrieveLastPrivateBusNumber(String genereatedRefNo, String groupNo, String type,
			String abbreviation) {
		logger.info("retrieveLastPrivateBusNumber start param: genereatedRefNo: " + genereatedRefNo + ", groupNo: "
				+ groupNo + ", type: " + type + ", abbreviation: " + abbreviation);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String lastBusNumber = null;
		String busNumberStart = "";
		try {
			con = ConnectionManager.getConnection();

			busNumberStart = abbreviation + "%";

			String query2 = "select b.bus_num as last_bus_no from nt_m_timetable_generator a, public.nt_m_timetable_generator_det b "
					+ "where a.generator_ref_no=? and b.group_no=? and b.trip_type=? " + "and b.bus_num like '"
					+ busNumberStart + "' and  a.seq_no = b.seq_master order by b.bus_num desc limit 1 ";

			ps = con.prepareStatement(query2);
			ps.setString(1, genereatedRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, type);
			rs = ps.executeQuery();

			while (rs.next()) {
				lastBusNumber = rs.getString("last_bus_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("retrieveLastPrivateBusNumber error: " + e.toString());
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("retrieveLastPrivateBusNumber end");
		return lastBusNumber;
	}

	@Override
	public String retrieveDriverRestTime(String routeNo) {
		logger.info("retrieveDriverRestTime start param: routeNo: " + routeNo);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String driverRestTime = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select rc_driver_rest_time from nt_t_route_creator where rc_route_no=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				driverRestTime = rs.getString("rc_driver_rest_time");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("retrieveDriverRestTime error: " + e.toString());
			return null;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("retrieveDriverRestTime end");
		return driverRestTime;
	}

	private void addDataIntoTimetableGenerator(List<TimeTableDTO> TimeTableList, String routeNum, String groupNo,
			String referenceNum, String tripType, String loginUser, boolean coupling, Connection con) throws Exception {
		logger.info("addDataIntoTimetableGenerator start param: TimeTableList: " + TimeTableList.toString()
				+ ", routeNum: " + routeNum + ", groupNo: " + groupNo + ", referenceNum: " + referenceNum
				+ ", tripType: " + tripType + ", loginUser: " + loginUser + ", coupling: " + coupling);

		PreparedStatement ps = null;
		ResultSet rs = null;
		long masterSeq = 0;

		String query1 = "select seq_no  from public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and trip_type=?";

		ps = con.prepareStatement(query1);
		ps.setString(1, referenceNum);
		ps.setString(2, groupNo);
		ps.setString(3, tripType);
		rs = ps.executeQuery();

		while (rs.next()) {
			masterSeq = rs.getLong("seq_no");
		}

		for (TimeTableDTO dto : TimeTableList) {
			// update
			updateIntoTimetableGenerator(con, dto, groupNo, tripType, referenceNum, masterSeq, loginUser, null);
		}

		updateTimetableGenerator(con, groupNo, tripType, referenceNum, loginUser, coupling);

		ConnectionManager.close(rs);
		ConnectionManager.close(ps);

		logger.info("addDataIntoTimetableGenerator end");
	}

	public void updateTimetableGenerator(Connection con, String groupNo, String tripType, String referenceNum,
			String loginUser, boolean coupling) {
		logger.info("updateTimetableGenerator start param: groupNo: " + groupNo + ", tripType: " + tripType
				+ ", referenceNum: " + referenceNum + " , loginUser: " + loginUser + ", coupling: " + coupling);

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			// update without fixed time added by and added date to master table
			String query2 = "UPDATE public.nt_m_timetable_generator SET create_by_without_fixed_time=?, created_date_without_fixed_time=?, no_of_couples_per_one_bus=? "
					+ " WHERE generator_ref_no=? and group_no=? and trip_type=? ";

			ps = con.prepareStatement(query2);

			ps.setString(1, loginUser);
			ps.setTimestamp(2, timestamp);
			if (coupling) {
				ps.setString(3, "2");
			} else {
				ps.setString(3, "1");
			}

			ps.setString(4, referenceNum);
			ps.setString(5, groupNo);
			ps.setString(6, tripType);

			ps.executeUpdate();

		} catch (Exception ex) {
			logger.error("updateTimetableGenerator error: " + ex.toString());
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

		}
		logger.info("updateTimetableGenerator end");
	}

	@Override
	public void updateIntoTimetableGenerator(Connection con, TimeTableDTO dTO, String groupNo, String tripType,
			String referenceNum, Long masterSeq, String loginUser, String duplicateBusNum) {
		logger.info("updateIntoTimetableGenerator start param: TimeTableDTO: " + dTO.toString() + ", groupNo: "
				+ groupNo + ", tripType: " + tripType + ", referenceNum: " + referenceNum + ", masterSeq: " + masterSeq
				+ ", loginUser: " + loginUser + ", duplicateBusNum: " + duplicateBusNum);

		PreparedStatement ps = null;
		ResultSet rs = null;
		String where = "";
		try {
			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			TimeTableDTO timeTableDTO = new TimeTableDTO();
			timeTableDTO = dTO;

			if (duplicateBusNum != null && !duplicateBusNum.isEmpty() && !duplicateBusNum.trim().equalsIgnoreCase("")) {
				where = "and bus_num='" + duplicateBusNum + "'";
			}

			TimeTableDTO dataTO = new TimeTableDTO();
			String fixedTime = null;
			String pvtBus = null;
			String selectQ = "SELECT * FROM public.nt_m_timetable_generator_det WHERE generator_ref_no=? and bus_num=? and group_no=? and trip_type=? ";
			ps = con.prepareStatement(selectQ);

			ps.setString(1, referenceNum);
			ps.setString(2, duplicateBusNum);
			ps.setString(3, groupNo);
			ps.setString(4, tripType);

			rs = ps.executeQuery();
			while (rs.next()) {
				dataTO.setTripId(rs.getString("trip_id"));
				fixedTime = rs.getString("is_fixed_time");
				pvtBus = rs.getString("is_pvt_bus");
			}

			ConnectionManager.close(ps);

			String query = "UPDATE public.nt_m_timetable_generator_det SET bus_num=?, start_time_slot=? , end_time_slot=?, modify_by=?, modify_date=? "
					+ " WHERE seq_master=? and generator_ref_no=? and group_no=? and trip_type=? and start_time_slot=? and end_time_slot=? "
					+ where;

			ps = con.prepareStatement(query);

			ps.setString(1, timeTableDTO.getBusNo());
			ps.setString(2, timeTableDTO.getStartTime());
			ps.setString(3, timeTableDTO.getEndTime());
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestamp);
			ps.setLong(6, masterSeq);
			ps.setString(7, referenceNum);
			ps.setString(8, groupNo);
			ps.setString(9, tripType);
			ps.setString(10, timeTableDTO.getTempStartTime());
			ps.setString(11, timeTableDTO.getTempEndTime());

			ps.executeUpdate();
			ConnectionManager.close(ps);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_timetable_generator_det");

			String queryh = "INSERT INTO public.nt_h_timetable_generator_det "
					+ "(seq_no, ref_seq_no, generator_ref_no, bus_num, start_time_slot, end_time_slot, assigned_bus_no, trip_id, "
					+ " is_fixed_time, group_no, trip_type, created_by, created_date, is_pvt_bus) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" + " ";

			ps = con.prepareStatement(queryh);

			ps.setLong(1, seqNo);
			ps.setLong(2, masterSeq);
			ps.setString(3, referenceNum);
			ps.setString(4, duplicateBusNum);
			ps.setString(5, timeTableDTO.getStartTime());
			ps.setString(6, timeTableDTO.getEndTime());
			ps.setString(7, timeTableDTO.getBusNo());
			if (dataTO.getTripId() != null && !dataTO.getTripId().isEmpty()
					&& !dataTO.getTripId().trim().equalsIgnoreCase("")) {
				ps.setInt(8, Integer.valueOf(dataTO.getTripId()));
			} else {
				ps.setNull(8, java.sql.Types.INTEGER);
			}

			ps.setString(9, fixedTime);
			ps.setString(10, groupNo);
			ps.setString(11, tripType);
			ps.setString(12, loginUser);
			ps.setTimestamp(13, timestamp);
			ps.setString(14, pvtBus);

			ps.executeUpdate();
			/** insert into history table end **/

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateIntoTimetableGenerator error: " + e.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		logger.info("updateIntoTimetableGenerator end");
	}

	@Override
	public String getBusRideTime(String routeNO, String busType) {
		logger.info("getBusRideTime start param: routeNO: " + routeNO + ", busType: " + busType);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String buRideTime = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select rc_travel_time  FROM public.nt_t_route_creator where rc_route_no=? and rc_bus_type=? and rc_status='A'";

			ps = con.prepareStatement(query2);
			ps.setString(1, routeNO);
			ps.setString(2, busType);
			rs = ps.executeQuery();

			while (rs.next()) {
				buRideTime = rs.getString("rc_travel_time");

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getBusRideTime error: " + e.toString());
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getBusRideTime end");
		return buRideTime;
	}

	@Override
	public boolean checkDataAlreadySaved(String genereatedRefNo, String groupNum, String tripType) {
		logger.info("checkDataAlreadySaved start param: genereatedRefNo: " + genereatedRefNo + ", groupNum: " + groupNum
				+ ", tripType: " + tripType);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean saved = false;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select create_by_without_fixed_time,created_date_without_fixed_time "
					+ "FROM public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and trip_type=? "
					+ "and create_by_without_fixed_time is not null and created_date_without_fixed_time is not null";

			ps = con.prepareStatement(query2);
			ps.setString(1, genereatedRefNo);
			ps.setString(2, groupNum);
			ps.setString(3, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				saved = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("checkDataAlreadySaved error: " + e.toString());
			return saved;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("checkDataAlreadySaved end");
		return saved;
	}

	@Override
	public String retrieveCouplesForRoute(String genereatedRefNo, String groupNo) {
		logger.info("retrieveCouplesForRoute start param: genereatedRefNo: " + genereatedRefNo);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String couplesPerBuses = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select no_of_couples_per_one_bus FROM public.nt_m_timetable_generator where generator_ref_no=? and  group_no =? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, genereatedRefNo);
			ps.setString(2, groupNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				couplesPerBuses = rs.getString("no_of_couples_per_one_bus");

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("retrieveCouplesForRoute error: " + e.toString());
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("retrieveCouplesForRoute end");
		return couplesPerBuses;
	}

	@Override
	public int retrievePrivateBusNumbers(String genereatedRefNo, String groupNum, String tripType) {
		logger.info("retrievePrivateBusNumbers start param: genereatedRefNo: " + genereatedRefNo + ", groupNum: "
				+ groupNum + ", tripType: " + tripType);

		int busCount = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT a.no_of_pvt_bus FROM public.nt_t_trips_generator_det02 a, public.nt_m_trips_generator b "
					+ "where a.group_no = ? and a.trip_type = ? and b.tg_generator_ref_no=? and a.trips_ref_code = b.tg_trips_ref_code";

			ps = con.prepareStatement(query2);
			ps.setString(1, groupNum);
			ps.setString(2, tripType);
			ps.setString(3, genereatedRefNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				busCount = rs.getInt("no_of_pvt_bus");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("retrievePrivateBusNumbers error: " + e.toString());
			return 0;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("retrievePrivateBusNumbers end");
		return busCount;

	}

	@Override
	public List<String> retreivePrivateBusNumbersInFixedBuses(TimeTableDTO timeTableDTO, String groupNum,
			String tripType, String abbreviation) {
		logger.info("retreivePrivateBusNumbersInFixedBuses start param: timeTableDTO: " + timeTableDTO.toString()
				+ ", groupNum: " + groupNum + ", tripType: " + tripType + ", abbreviation: " + abbreviation);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> fixedPrivateBusesList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select b.bus_num  from nt_m_timetable_generator a  inner join public.nt_m_timetable_generator_det b on b.seq_master = a.seq_no  "
					+ "where a.generator_ref_no=? and b.group_no=? and b.trip_type=? and is_pvt_bus='Y' order by b.start_time_slot";

			ps = con.prepareStatement(query2);

			ps.setString(1, timeTableDTO.getGenereatedRefNo());
			ps.setString(2, groupNum);
			ps.setString(3, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				fixedPrivateBusesList.add(rs.getString("bus_num"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("retreivePrivateBusNumbersInFixedBuses error: " + e.toString());
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("retreivePrivateBusNumbersInFixedBuses end");
		return fixedPrivateBusesList;
	}

	@Override
	public int retrieveFixedBusNumbers(String genereatedRefNo, String groupNum, String tripType, String pvtBus) {
		logger.info("retrieveFixedBusNumbers start param: genereatedRefNo: " + genereatedRefNo + ", groupNum: "
				+ groupNum + ", tripType: " + tripType + ", pvtBus: " + pvtBus);
		int busCount = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query2 = null;
		try {
			con = ConnectionManager.getConnection();

			/* for prevent get count for D and O **/

			if (tripType.equalsIgnoreCase("O")) {
				query2 = "select count(bus_num) as bus_count from  public.nt_m_timetable_generator_det \r\n"
						+ "where generator_ref_no=? and group_no=? and trip_type=? and is_pvt_bus=? and bus_num is not null\r\n"
						+ "and bus_num not like '%-D'";
			}

			else {
				query2 = "select count(bus_num) as bus_count from  public.nt_m_timetable_generator_det \r\n"
						+ "where generator_ref_no=? and group_no=? and trip_type=? and is_pvt_bus=? and bus_num is not null\r\n"
						+ "and bus_num not like '%-O'";
			}
			ps = con.prepareStatement(query2);
			ps.setString(1, genereatedRefNo);
			ps.setString(2, groupNum);
			ps.setString(3, tripType);
			ps.setString(4, pvtBus);

			rs = ps.executeQuery();

			while (rs.next()) {
				busCount = rs.getInt("bus_count");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("retrieveFixedBusNumbers error: " + e.toString());
			return 0;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("retrieveFixedBusNumbers end");
		return busCount;

	}

	@Override
	public List<TimeTableDTO> selectDataForGroups(String genereatedRefNo, String groupNum, String TripType) {
		logger.info("selectDataForGroups start param: genereatedRefNo: " + genereatedRefNo + ", groupNum: " + groupNum
				+ ", TripType: " + TripType);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> dataList = new ArrayList<TimeTableDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select * from public.nt_m_timetable_generator_det where generator_ref_no=? and group_no=? and trip_type=? order by trip_id  ";
			ps = con.prepareStatement(query2);
			ps.setString(1, genereatedRefNo);
			ps.setString(2, groupNum);
			ps.setString(3, TripType);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setTimeTableDetSeq(rs.getLong("seq_no"));
				e.setBusNo(rs.getString("bus_num"));
				e.setStartTime(rs.getString("start_time_slot"));
				e.setEndTime(rs.getString("end_time_slot"));
				e.setAssigneBusNo(rs.getString("assigned_bus_no"));
				e.setTripType(rs.getString("trip_type"));
				e.setDuplicateBusNum(rs.getString("bus_num"));
				e.setTempStartTime(rs.getString("start_time_slot"));
				e.setTempEndTime(rs.getString("end_time_slot"));
				e.setTripId(Integer.toString(rs.getInt("trip_id")));

				String isCTBCode = rs.getString("is_fixed_time");
				boolean isCTB = false;
				if (isCTBCode != null) {
					if (isCTBCode.equals("Y")) {
						isCTB = true;
						e.setFixedTime(true);
					} else {
						isCTB = false;
					}
				} else {
					isCTB = false;
				}
				e.setCtbBus(isCTB);
				String fixedTime = rs.getString("is_fixed_time");
				if (fixedTime != null && !fixedTime.isEmpty() && fixedTime.equalsIgnoreCase("Y")) {
					e.setFixedTime(true);
				} else {
					e.setFixedTime(false);
				}

				dataList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("selectDataForGroups error: " + e.toString());
			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("selectDataForGroups end");
		return dataList;
	}

	private boolean updateLeaves(String genereatedRefNo, String groupNum, String TripType, int leaves,
			String loginUser, Connection con) throws Exception {
		logger.info("updateLeaves start param: genereatedRefNo: " + genereatedRefNo + ", groupNum: " + groupNum
				+ ", TripType: " + TripType + ", leaves: " + leaves + ", loginUser: " + loginUser);

		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean updated = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String query = "UPDATE public.nt_m_timetable_generator SET no_of_buses_on_leave=?, modified_by=?, modified_date=? "
				+ " WHERE generator_ref_no=? and group_no=? and trip_type=? ";

		ps = con.prepareStatement(query);

		if (leaves == 0) {
			ps.setNull(1, java.sql.Types.INTEGER);
		} else {

			if (leaves > 9) {
				ps.setInt(1, 9);
			} else {

				ps.setInt(1, leaves);
			}
		}
		ps.setString(2, loginUser);
		ps.setTimestamp(3, timestamp);
		ps.setString(4, genereatedRefNo);
		ps.setString(5, groupNum);
		ps.setString(6, TripType);

		ps.executeUpdate();
		updated = true;

		ConnectionManager.close(rs);
		ConnectionManager.close(ps);

		logger.info("updateLeaves end");
		return updated;
	}

	@Override
	public int retrieveNumOfLeaves(String genereatedRefNo, String groupNum, String tripType) {
		logger.info("retrieveCouplesForRoute start param: genereatedRefNo: " + genereatedRefNo + " groupNum: "
				+ groupNum + " tripType: " + tripType);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int leaves = 0;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select * from public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and trip_type=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, genereatedRefNo);
			ps.setString(2, groupNum);
			ps.setString(3, tripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				leaves = rs.getInt("no_of_buses_on_leave");

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("retrieveCouplesForRoute error: " + e.toString());
			return 0;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("retrieveCouplesForRoute end");
		return leaves;
	}

	@Override
	public void updateTimetableGenerator(Connection con, TimeTableDTO timeTableDTO, String groupNo, String tripType,
			String referenceNum, Long masterSeq, String loginUser, String duplicateBusNum) {
		logger.info("updateIntoTimetableGenerator start param: TimeTableDTO: " + timeTableDTO.toString() + ", groupNo: "
				+ groupNo + ", tripType: " + tripType + ", referenceNum: " + referenceNum + ", masterSeq: " + masterSeq
				+ ", loginUser: " + loginUser + ", duplicateBusNum: " + duplicateBusNum);

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			TimeTableDTO dataTO = new TimeTableDTO();
			String fixedTime = null;
			String pvtBus = null;
			String prevBusNo = null;
			String prevAssignedBusNo = null;
			String selectQ = "SELECT * FROM public.nt_m_timetable_generator_det WHERE generator_ref_no=? and bus_num=? and group_no=? and trip_type=? ";
			ps = con.prepareStatement(selectQ);

			ps.setString(1, referenceNum);
			ps.setString(2, duplicateBusNum);
			ps.setString(3, groupNo);
			ps.setString(4, tripType);

			rs = ps.executeQuery();
			while (rs.next()) {
				dataTO.setTripId(rs.getString("trip_id"));
				fixedTime = rs.getString("is_fixed_time");
				pvtBus = rs.getString("is_pvt_bus");
				timeTableDTO.setTempStartTime(rs.getString("start_time_slot"));
				timeTableDTO.setTempEndTime(rs.getString("end_time_slot"));
				prevBusNo = rs.getString("bus_num");
				prevAssignedBusNo = rs.getString("assigned_bus_no");
				break;
			}

			ConnectionManager.close(ps);

			String isPvt = "Y";
			if (timeTableDTO.getBusNo() == null || timeTableDTO.getBusNo().isEmpty()) {
				timeTableDTO.setBusNo(null);

			} else {
				if (timeTableDTO.getBusNo().equalsIgnoreCase("SLTB-O")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("SLTB-D")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("ETC-O")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("ETC-D")) {
					isPvt = "N";

				}
			}

			String query = "UPDATE public.nt_m_timetable_generator_det SET bus_num=?, start_time_slot=? , end_time_slot=?, modify_by=?, modify_date=?, is_pvt_bus=? "
					+ " WHERE seq_master=? and generator_ref_no=? and group_no=? and trip_type=? and trip_id =? ";

			ps = con.prepareStatement(query);

			ps.setString(1, timeTableDTO.getBusNo());
			ps.setString(2, timeTableDTO.getStartTime());
			ps.setString(3, timeTableDTO.getEndTime());
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestamp);
			ps.setString(6, isPvt);
			ps.setLong(7, masterSeq);
			ps.setString(8, referenceNum);
			ps.setString(9, groupNo);
			ps.setString(10, tripType);

			ps.setInt(11, Integer.parseInt(timeTableDTO.getId()));
			ps.executeUpdate();
			ConnectionManager.close(ps);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_timetable_generator_det");

			String queryh = "INSERT INTO public.nt_h_timetable_generator_det "
					+ "(seq_no, ref_seq_no, generator_ref_no, bus_num, start_time_slot, end_time_slot, assigned_bus_no, trip_id, "
					+ " is_fixed_time, group_no, trip_type, created_by, created_date, is_pvt_bus) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" + " ";

			ps = con.prepareStatement(queryh);

			ps.setLong(1, seqNo);
			ps.setLong(2, masterSeq);
			ps.setString(3, referenceNum);
			ps.setString(4, prevBusNo);
			ps.setString(5, timeTableDTO.getTempStartTime());
			ps.setString(6, timeTableDTO.getTempEndTime());
			ps.setString(7, prevAssignedBusNo);
			if (dataTO.getTripId() != null && !dataTO.getTripId().isEmpty()
					&& !dataTO.getTripId().trim().equalsIgnoreCase("")) {
				ps.setInt(8, Integer.valueOf(dataTO.getTripId()));
			} else {
				ps.setNull(8, java.sql.Types.INTEGER);
			}

			ps.setString(9, fixedTime);
			ps.setString(10, groupNo);
			ps.setString(11, tripType);
			ps.setString(12, loginUser);
			ps.setTimestamp(13, timestamp);
			ps.setString(14, pvtBus);

			ps.executeUpdate();
			/** insert into history table end **/

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateIntoTimetableGenerator error: " + e.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("updateIntoTimetableGenerator end");
	}

	@Override
	public boolean checkLeavesOnPanelRefNo(String refNo, String groupNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean saved = false;
		try {
			con = ConnectionManager.getConnection();

			String query2 = "select bus_num from  public.nt_m_timetable_generator_det where generator_ref_no=? and group_no=? and bus_num is not null limit 1";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				saved = true;
			}

		} catch (Exception e) {
			e.printStackTrace();

			return saved;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("checkDataAlreadySaved end");
		return saved;
	}

	@Override
	public int getNumOfLeaves(String genereatedRefNo, String groupNum, String TripType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer leave = 0;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select no_of_buses_on_leave  from public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and trip_type=? ";

			ps = con.prepareStatement(query2);
			ps.setString(1, genereatedRefNo);
			ps.setString(2, groupNum);
			ps.setString(3, TripType);
			rs = ps.executeQuery();

			while (rs.next()) {
				leave = rs.getInt("no_of_buses_on_leave");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("selectDataForGroups error: " + e.toString());
			return 0;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("selectDataForGroups end");
		return leave;
	}

	private void updateCouplesInMasterTable(String genereatedRefNo, String groupNum, String TripType, String coupleNo, Connection con) throws Exception {

		PreparedStatement ps = null;
		ResultSet rs = null;

			String query = "UPDATE public.nt_m_timetable_generator SET no_of_couples_per_one_bus =? "
					+ " WHERE generator_ref_no=? and group_no=? and trip_type=? ";

			ps = con.prepareStatement(query);

			ps.setString(1, coupleNo);

			ps.setString(2, genereatedRefNo);
			ps.setString(3, groupNum);
			ps.setString(4, TripType);

			ps.executeUpdate();

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

		logger.info("updateLeaves end");

	}

	@Override
	public void updateTimetableGeneratorEdit(Connection con, TimeTableDTO timeTableDTO, String groupNo, String tripType,
			String referenceNum, Long masterSeq, String loginUser, String duplicateBusNum, int tripId) {
		logger.info("updateIntoTimetableGenerator start param: TimeTableDTO: " + timeTableDTO.toString() + ", groupNo: "
				+ groupNo + ", tripType: " + tripType + ", referenceNum: " + referenceNum + ", masterSeq: " + masterSeq
				+ ", loginUser: " + loginUser + ", duplicateBusNum: " + duplicateBusNum);

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			TimeTableDTO dataTO = new TimeTableDTO();
			String fixedTime = null;
			String pvtBus = null;
			String prevBusNo = null;
			String prevAssignedBusNo = null;
			String selectQ = "SELECT * FROM public.nt_m_timetable_generator_det WHERE generator_ref_no=? and bus_num=? and group_no=? and trip_type=? and  trip_id =? order by trip_id  ";
			ps = con.prepareStatement(selectQ);

			ps.setString(1, referenceNum);
			ps.setString(2, duplicateBusNum);
			ps.setString(3, groupNo);
			ps.setString(4, tripType);
			ps.setInt(5, tripId);

			rs = ps.executeQuery();
			while (rs.next()) {
				dataTO.setTripId(rs.getString("trip_id"));
				fixedTime = rs.getString("is_fixed_time");
				pvtBus = rs.getString("is_pvt_bus");
				timeTableDTO.setTempStartTime(rs.getString("start_time_slot"));
				timeTableDTO.setTempEndTime(rs.getString("end_time_slot"));
				prevBusNo = rs.getString("bus_num");
				prevAssignedBusNo = rs.getString("assigned_bus_no");
				break;
			}

			ConnectionManager.close(ps);

			String isPvt = "Y";
			if (timeTableDTO.getBusNo() == null || timeTableDTO.getBusNo().isEmpty()) {
				timeTableDTO.setBusNo(null);

			} else {
				if (timeTableDTO.getBusNo().equalsIgnoreCase("SLTB-O")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("SLTB-D")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("ETC-O")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("ETC-D")) {
					isPvt = "N";

				}
			}

			String query = "UPDATE public.nt_m_timetable_generator_det SET bus_num=?, start_time_slot=? , end_time_slot=?, modify_by=?, modify_date=?, is_pvt_bus=? "
					+ " WHERE seq_master=? and generator_ref_no=? and group_no=? and trip_type=? and trip_id =? ";

			ps = con.prepareStatement(query);

			ps.setString(1, timeTableDTO.getBusNo());
			ps.setString(2, timeTableDTO.getStartTime());
			ps.setString(3, timeTableDTO.getEndTime());
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestamp);
			ps.setString(6, isPvt);
			ps.setLong(7, masterSeq);
			ps.setString(8, referenceNum);
			ps.setString(9, groupNo);
			ps.setString(10, tripType);
			if (dataTO.getTripId() != null && !dataTO.getTripId().trim().isEmpty()) {
				ps.setInt(11, Integer.parseInt(dataTO.getTripId()));
			} else {
				ps.setInt(11, Integer.parseInt(timeTableDTO.getTripId()));
			}

			/** modify by tharushi.e **/
			ps.executeUpdate();
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_timetable_generator_det");

			String queryh = "INSERT INTO public.nt_h_timetable_generator_det "
					+ "(seq_no, ref_seq_no, generator_ref_no, bus_num, start_time_slot, end_time_slot, assigned_bus_no, trip_id, "
					+ " is_fixed_time, group_no, trip_type, created_by, created_date, is_pvt_bus) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" + " ";

			ps = con.prepareStatement(queryh);

			ps.setLong(1, seqNo);
			ps.setLong(2, masterSeq);
			ps.setString(3, referenceNum);
			ps.setString(4, prevBusNo);
			ps.setString(5, timeTableDTO.getTempStartTime());
			ps.setString(6, timeTableDTO.getTempEndTime());
			ps.setString(7, prevAssignedBusNo);
			if (dataTO.getTripId() != null && !dataTO.getTripId().isEmpty()
					&& !dataTO.getTripId().trim().equalsIgnoreCase("")) {
				ps.setInt(8, Integer.valueOf(dataTO.getTripId()));
			} else {
				ps.setNull(8, java.sql.Types.INTEGER);
			}

			ps.setString(9, fixedTime);
			ps.setString(10, groupNo);
			ps.setString(11, tripType);
			ps.setString(12, loginUser);
			ps.setTimestamp(13, timestamp);
			ps.setString(14, pvtBus);

			ps.executeUpdate();
			/** insert into history table end **/

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateIntoTimetableGenerator error: " + e.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("updateIntoTimetableGenerator end");
	}

	@Override
	public void updateTimetableGeneratorNew(TimeTableDTO timeTableDTO, String groupNo, String tripType,
			String referenceNum, String loginUser, String duplicateBusNum) {

		PreparedStatement ps = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		ResultSet rs4 = null;
		Connection con = null;
		try {
			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			con = ConnectionManager.getConnection();

			TimeTableDTO dataTO = new TimeTableDTO();
			String fixedTime = null;
			String pvtBus = null;
			String prevBusNo = null;
			String prevAssignedBusNo = null;
			String selectQ = "SELECT * FROM public.nt_m_timetable_generator_det WHERE generator_ref_no=? and bus_num=? and group_no=? and trip_type=? ";
			ps = con.prepareStatement(selectQ);

			ps.setString(1, referenceNum);
			ps.setString(2, duplicateBusNum);
			ps.setString(3, groupNo);
			ps.setString(4, tripType);

			rs = ps.executeQuery();
			while (rs.next()) {
				dataTO.setTripId(rs.getString("trip_id"));
				fixedTime = rs.getString("is_fixed_time");
				pvtBus = rs.getString("is_pvt_bus");
				timeTableDTO.setTempStartTime(rs.getString("start_time_slot"));
				timeTableDTO.setTempEndTime(rs.getString("end_time_slot"));
				prevBusNo = rs.getString("bus_num");
				prevAssignedBusNo = rs.getString("assigned_bus_no");
				break;
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			String isPvt = "Y";
			if (timeTableDTO.getBusNo() == null || timeTableDTO.getBusNo().isEmpty()) {
				timeTableDTO.setBusNo(null);

			} else {
				if (timeTableDTO.getBusNo().equalsIgnoreCase("SLTB-O")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("SLTB-D")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("ETC-O")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("ETC-D")) {
					isPvt = "N";

				}
			}

			long masterSeq = 0;

			String query1 = "select seq_no  from public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and trip_type=?";

			ps4 = con.prepareStatement(query1);
			ps4.setString(1, referenceNum);
			ps4.setString(2, groupNo);
			ps4.setString(3, tripType);
			rs4 = ps4.executeQuery();

			while (rs4.next()) {

				masterSeq = rs4.getLong("seq_no");

			}

			String query = "UPDATE public.nt_m_timetable_generator_det SET bus_num=?, start_time_slot=? , end_time_slot=?, modify_by=?, modify_date=?, is_pvt_bus=? "
					+ " WHERE seq_master=? and generator_ref_no=? and group_no=? and trip_type=? and trip_id =? ";

			ps = con.prepareStatement(query);

			ps.setString(1, timeTableDTO.getBusNo());
			ps.setString(2, timeTableDTO.getStartTime());
			ps.setString(3, timeTableDTO.getEndTime());
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestamp);
			ps.setString(6, isPvt);
			ps.setLong(7, masterSeq);
			ps.setString(8, referenceNum);
			ps.setString(9, groupNo);
			ps.setString(10, tripType);

			ps.setInt(11, Integer.parseInt(timeTableDTO.getId()));

			ps.executeUpdate();
			ConnectionManager.close(ps);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_timetable_generator_det");

			String queryh = "INSERT INTO public.nt_h_timetable_generator_det "
					+ "(seq_no, ref_seq_no, generator_ref_no, bus_num, start_time_slot, end_time_slot, assigned_bus_no, trip_id, "
					+ " is_fixed_time, group_no, trip_type, created_by, created_date, is_pvt_bus) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" + " ";

			ps = con.prepareStatement(queryh);

			ps.setLong(1, seqNo);
			ps.setLong(2, masterSeq);
			ps.setString(3, referenceNum);
			ps.setString(4, prevBusNo);
			ps.setString(5, timeTableDTO.getTempStartTime());
			ps.setString(6, timeTableDTO.getTempEndTime());
			ps.setString(7, prevAssignedBusNo);
			if (dataTO.getTripId() != null && !dataTO.getTripId().isEmpty()
					&& !dataTO.getTripId().trim().equalsIgnoreCase("")) {
				ps.setInt(8, Integer.valueOf(dataTO.getTripId()));
			} else {
				ps.setNull(8, java.sql.Types.INTEGER);
			}

			ps.setString(9, fixedTime);
			ps.setString(10, groupNo);
			ps.setString(11, tripType);
			ps.setString(12, loginUser);
			ps.setTimestamp(13, timestamp);
			ps.setString(14, pvtBus);

			ps.executeUpdate();
			/** insert into history table end **/
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs4);
			ConnectionManager.close(ps4);
			ConnectionManager.close(con);

		}

	}

//	changed by thilna.d on 1-12-2021 - added new method on same name
	private void updateTimetableGeneratorEditNew(String groupNo, String tripType, String referenceNum, String loginUser,
			List<TimeTableDTO> originDesBusList, Connection con) throws Exception {

		PreparedStatement ps = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		ResultSet rs4 = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		for (TimeTableDTO timeTableDTO : originDesBusList) {

			TimeTableDTO dataTO = new TimeTableDTO();
			String fixedTime = null;
			String pvtBus = null;
			String prevBusNo = null;
			String prevAssignedBusNo = null;
			
			String selectQ = "";
			if(timeTableDTO.getDuplicateBusNum()!=null) {
				selectQ = "SELECT * FROM public.nt_m_timetable_generator_det WHERE generator_ref_no=? and bus_num=? and group_no=? and trip_type=? and  trip_id =? order by trip_id  ";
			} else {
				selectQ = "SELECT * FROM public.nt_m_timetable_generator_det WHERE generator_ref_no=? and bus_num is NULL and group_no=? and trip_type=? and  trip_id =? order by trip_id  ";
			}
			
			ps = con.prepareStatement(selectQ);
			
			if(timeTableDTO.getDuplicateBusNum()!=null) {
				ps.setString(1, referenceNum);
				ps.setString(2, timeTableDTO.getDuplicateBusNum());
				ps.setString(3, groupNo);
				ps.setString(4, tripType);
				ps.setInt(5, Integer.parseInt(timeTableDTO.getTripId()));
			} else {
				ps.setString(1, referenceNum);
				ps.setString(2, groupNo);
				ps.setString(3, tripType);
				ps.setInt(4, Integer.parseInt(timeTableDTO.getTripId()));
			}
			
			rs = ps.executeQuery();
			while (rs.next()) {
				dataTO.setTripId(rs.getString("trip_id"));
				fixedTime = rs.getString("is_fixed_time");
				pvtBus = rs.getString("is_pvt_bus");
				timeTableDTO.setTempStartTime(rs.getString("start_time_slot"));
				timeTableDTO.setTempEndTime(rs.getString("end_time_slot"));
				prevBusNo = rs.getString("bus_num");
				prevAssignedBusNo = rs.getString("assigned_bus_no");
				break;
			}

			ConnectionManager.close(ps);

			String isPvt = "Y";
			if (timeTableDTO.getBusNo() == null || timeTableDTO.getBusNo().isEmpty()) {
				timeTableDTO.setBusNo(null);

			} else {
				if (timeTableDTO.getBusNo().equalsIgnoreCase("SLTB-O")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("SLTB-D")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("ETC-O")
						|| timeTableDTO.getBusNo().equalsIgnoreCase("ETC-D")) {
					isPvt = "N";

				}
			}

			long masterSeq = 0;

			String query1 = "select seq_no  from public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and trip_type=?";

			ps4 = con.prepareStatement(query1);
			ps4.setString(1, referenceNum);
			ps4.setString(2, groupNo);
			ps4.setString(3, tripType);
			rs4 = ps4.executeQuery();

			while (rs4.next()) {

				masterSeq = rs4.getLong("seq_no");

			}

			String query = "UPDATE public.nt_m_timetable_generator_det SET bus_num=?, start_time_slot=? , end_time_slot=?, modify_by=?, modify_date=?, is_pvt_bus=? "
					+ " WHERE seq_master=? and generator_ref_no=? and group_no=? and trip_type=? and trip_id =? ";

			ps = con.prepareStatement(query);

			ps.setString(1, timeTableDTO.getBusNo());
			ps.setString(2, timeTableDTO.getStartTime());
			ps.setString(3, timeTableDTO.getEndTime());
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestamp);
			ps.setString(6, isPvt);
			ps.setLong(7, masterSeq);
			ps.setString(8, referenceNum);
			ps.setString(9, groupNo);
			ps.setString(10, tripType);
			if (dataTO.getTripId() != null && !dataTO.getTripId().trim().isEmpty()) {
				ps.setInt(11, Integer.parseInt(dataTO.getTripId()));

			} else {
				ps.setInt(11, Integer.parseInt(timeTableDTO.getId()));

			}
			ps.executeUpdate();
			ConnectionManager.close(ps);

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_timetable_generator_det");

			String queryh = "INSERT INTO public.nt_h_timetable_generator_det "
					+ "(seq_no, ref_seq_no, generator_ref_no, bus_num, start_time_slot, end_time_slot, assigned_bus_no, trip_id, "
					+ " is_fixed_time, group_no, trip_type, created_by, created_date, is_pvt_bus) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" + " ";

			ps = con.prepareStatement(queryh);

			ps.setLong(1, seqNo);
			ps.setLong(2, masterSeq);
			ps.setString(3, referenceNum);
			ps.setString(4, prevBusNo);
			ps.setString(5, timeTableDTO.getTempStartTime());
			ps.setString(6, timeTableDTO.getTempEndTime());
			ps.setString(7, prevAssignedBusNo);
			if (dataTO.getTripId() != null && !dataTO.getTripId().isEmpty()
					&& !dataTO.getTripId().trim().equalsIgnoreCase("")) {
				ps.setInt(8, Integer.valueOf(dataTO.getTripId()));
			} else {
				ps.setNull(8, java.sql.Types.INTEGER);
			}

			ps.setString(9, fixedTime);
			ps.setString(10, groupNo);
			ps.setString(11, tripType);
			ps.setString(12, loginUser);
			ps.setTimestamp(13, timestamp);
			ps.setString(14, pvtBus);

			ps.executeUpdate();
			/** insert into history table end **/
		}

		ConnectionManager.close(rs);
		ConnectionManager.close(ps);
		ConnectionManager.close(rs4);
		ConnectionManager.close(ps4);

	}

	private void updateTimetableGeneratorNew(String groupNo, String tripType, String referenceNum, String loginUser,
			List<TimeTableDTO> originDesBusList, Connection con) throws Exception {

		PreparedStatement ps = null;
		PreparedStatement ps4 = null;
		ResultSet rs = null;
		ResultSet rs4 = null;

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());

			for (TimeTableDTO timeTableDTO : originDesBusList) {

				TimeTableDTO dataTO = new TimeTableDTO();
				String fixedTime = null;
				String pvtBus = null;
				String prevBusNo = null;
				String prevAssignedBusNo = null;
				String selectQ = "SELECT * FROM public.nt_m_timetable_generator_det WHERE generator_ref_no=? and bus_num=? and group_no=? and trip_type=? ";
				ps = con.prepareStatement(selectQ);

				ps.setString(1, referenceNum);
				ps.setString(2, timeTableDTO.getDuplicateBusNum());
				ps.setString(3, groupNo);
				ps.setString(4, tripType);

				rs = ps.executeQuery();
				while (rs.next()) {
					dataTO.setTripId(rs.getString("trip_id"));
					fixedTime = rs.getString("is_fixed_time");
					pvtBus = rs.getString("is_pvt_bus");
					timeTableDTO.setTempStartTime(rs.getString("start_time_slot"));
					timeTableDTO.setTempEndTime(rs.getString("end_time_slot"));
					prevBusNo = rs.getString("bus_num");
					prevAssignedBusNo = rs.getString("assigned_bus_no");
					break;
				}

				ConnectionManager.close(ps);

				String isPvt = "Y";
				if (timeTableDTO.getBusNo() == null || timeTableDTO.getBusNo().isEmpty()) {
					timeTableDTO.setBusNo(null);

				} else {
					if (timeTableDTO.getBusNo().equalsIgnoreCase("SLTB-O")
							|| timeTableDTO.getBusNo().equalsIgnoreCase("SLTB-D")
							|| timeTableDTO.getBusNo().equalsIgnoreCase("ETC-O")
							|| timeTableDTO.getBusNo().equalsIgnoreCase("ETC-D")) {
						isPvt = "N";

					}
				}
				long masterSeq = 0;

				String query1 = "select seq_no  from public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and trip_type=?";

				ps4 = con.prepareStatement(query1);
				ps4.setString(1, referenceNum);
				ps4.setString(2, groupNo);
				ps4.setString(3, tripType);
				rs4 = ps4.executeQuery();

				while (rs4.next()) {
					masterSeq = rs4.getLong("seq_no");
				}

				String query = "UPDATE public.nt_m_timetable_generator_det SET bus_num=?, start_time_slot=? , end_time_slot=?, modify_by=?, modify_date=?, is_pvt_bus=? "
						+ " WHERE seq_master=? and generator_ref_no=? and group_no=? and trip_type=? and trip_id =? ";

				ps = con.prepareStatement(query);

				ps.setString(1, timeTableDTO.getBusNo());
				ps.setString(2, timeTableDTO.getStartTime());
				ps.setString(3, timeTableDTO.getEndTime());
				ps.setString(4, loginUser);
				ps.setTimestamp(5, timestamp);
				ps.setString(6, isPvt);
				ps.setLong(7, masterSeq);
				ps.setString(8, referenceNum);
				ps.setString(9, groupNo);
				ps.setString(10, tripType);

				ps.setInt(11, Integer.parseInt(timeTableDTO.getId()));

				/** modify by tharushi.e **/
				ps.executeUpdate();
				ConnectionManager.close(ps);

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_timetable_generator_det");

				String queryh = "INSERT INTO public.nt_h_timetable_generator_det "
						+ "(seq_no, ref_seq_no, generator_ref_no, bus_num, start_time_slot, end_time_slot, assigned_bus_no, trip_id, "
						+ " is_fixed_time, group_no, trip_type, created_by, created_date, is_pvt_bus) "
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" + " ";

				ps = con.prepareStatement(queryh);

				ps.setLong(1, seqNo);
				ps.setLong(2, masterSeq);
				ps.setString(3, referenceNum);
				ps.setString(4, prevBusNo);
				ps.setString(5, timeTableDTO.getTempStartTime());
				ps.setString(6, timeTableDTO.getTempEndTime());
				ps.setString(7, prevAssignedBusNo);
				if (dataTO.getTripId() != null && !dataTO.getTripId().isEmpty()
						&& !dataTO.getTripId().trim().equalsIgnoreCase("")) {
					ps.setInt(8, Integer.valueOf(dataTO.getTripId()));
				} else {
					ps.setNull(8, java.sql.Types.INTEGER);
				}

				ps.setString(9, fixedTime);
				ps.setString(10, groupNo);
				ps.setString(11, tripType);
				ps.setString(12, loginUser);
				ps.setTimestamp(13, timestamp);
				ps.setString(14, pvtBus);

				ps.executeUpdate();
				/** insert into history table end **/
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(rs4);
			ConnectionManager.close(ps4);

	}

	private void updateFixedBuses(String groupNo, String referenceNum, List<TimeTableDTO> originList,
			List<TimeTableDTO> destinationList, Connection con) throws Exception {

		PreparedStatement ps = null;
		List<TimeTableDTO> combinedList = originList;
		combinedList.addAll(destinationList);

		for (TimeTableDTO timeTableDTO : combinedList) {
			if (timeTableDTO.isFixedTime()) {
				String query2 = "UPDATE public.nt_m_timetable_generator_det SET is_fixed_time='Y' "
						+ " WHERE generator_ref_no=? and group_no=? and bus_num =? ";

				ps = con.prepareStatement(query2);
				ps.setString(1, referenceNum);
				ps.setString(2, groupNo);
				ps.setString(3, timeTableDTO.getBusNo());
				ps.executeUpdate();
				ConnectionManager.close(ps);
			}
		}
		ConnectionManager.close(ps);
	}

	@Override
	public void updateTimetableGeneratorEditNew(String groupNo, String referenceNum, String loginUser,
			List<TimeTableDTO> originBusList, List<TimeTableDTO> desBusList) throws Exception {

		Connection con = null;
		try {
			con = ConnectionManager.getConnection();

			updateTimetableGeneratorEditNew(groupNo, "O", referenceNum, loginUser, originBusList, con);
			updateTimetableGeneratorEditNew(groupNo, "D", referenceNum, loginUser, desBusList, con);
			
			updateFixedBuses(groupNo, referenceNum, originBusList, desBusList, con);

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateTimetableGeneratorEditNew main error: " + e.toString());
			throw new ApplicationException("updateTimetableGeneratorEditNew main error: " + e.toString());
		} finally {
			ConnectionManager.close(con);
		}
	}

	@Override
	public void updateTimetableGeneratorNew(String groupNo, String referenceNum, String loginUser,
			List<TimeTableDTO> originBusList, List<TimeTableDTO> desBusList) {
		Connection con = null;
		try {
			con = ConnectionManager.getConnection();

			updateTimetableGeneratorNew(groupNo, "O", referenceNum, loginUser, originBusList, con);
			updateTimetableGeneratorNew(groupNo, "D", referenceNum, loginUser, desBusList, con);

			updateFixedBuses(groupNo, referenceNum, originBusList, desBusList, con);

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateTimetableGeneratorNew mian error: " + e.toString());
		} finally {
			ConnectionManager.close(con);
		}

	}

	@Override
	public void addDataIntoTimetableGenerator(List<TimeTableDTO> originList, List<TimeTableDTO> desList,
			String routeNum, String groupNo, String referenceNum, String loginUser, boolean coupling) {
		Connection con = null;
		try {
			con = ConnectionManager.getConnection();

			addDataIntoTimetableGenerator(originList, routeNum, groupNo, referenceNum, "O", loginUser, coupling, con);
			addDataIntoTimetableGenerator(desList, routeNum, groupNo, referenceNum, "D", loginUser, coupling, con);

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("addDataIntoTimetableGenerator mian error: " + e.toString());
		} finally {
			ConnectionManager.close(con);
		}

	}

	@Override
	public boolean updateLeaves(String genereatedRefNo, String groupNum, int originLeaves,
			int desLeaves, String loginUser) {
		Connection con = null;
		boolean returnVal = false;
		try {
			con = ConnectionManager.getConnection();

			updateLeaves(genereatedRefNo, groupNum, "O", originLeaves, loginUser, con);
			updateLeaves(genereatedRefNo, groupNum, "D", desLeaves, loginUser, con);
			returnVal = true;

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateLeaves mian error: " + e.toString());
			returnVal = false;
		} finally {
			ConnectionManager.close(con);
		}
		return returnVal;
	}

	@Override
	public void updateCouplesInMasterTable(String genereatedRefNo, String groupNum, String coupleNoOrigin,
			String coupleNoDes) {
		Connection con = null;
		try {
			con = ConnectionManager.getConnection();

			updateCouplesInMasterTable(genereatedRefNo, groupNum, "O", coupleNoOrigin, con);
			updateCouplesInMasterTable(genereatedRefNo, groupNum, "D", coupleNoDes, con);

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateCouplesInMasterTable mian error: " + e.toString());
		} finally {
			ConnectionManager.close(con);
		}
	}

}
