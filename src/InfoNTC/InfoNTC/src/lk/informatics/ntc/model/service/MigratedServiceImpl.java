package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import lk.informatics.ntc.model.dto.ParamerDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.roc.utils.common.Utils;

public class MigratedServiceImpl implements MigratedService {

	public static Logger logger = Logger.getLogger("MigratedServiceImpl");
	private TimeTableService timeTableService;
	private PanelGeneratorWithoutFixedTimeService panelGeneratorWithoutFixedTimeService;

	@Override
	public void updateStatusOfQueueNumberAfterCallNext(String queueNumber, String taskStatus) {
		logger.info("updateQueueNumberAfterCallNext start");

		// task status - once call queue number called = 'O' after save queue number
		// ='C'

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {

			String sql3 = "UPDATE public.nt_m_queue_master SET task_status=? ,que_task_status =? WHERE que_number=? and que_date like '"
					+ today + "%'";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(sql3);

			ps.setString(1, taskStatus);
			ps.setString(2, taskStatus);
			ps.setString(3, queueNumber);

			ps.executeUpdate();

			ConnectionManager.commit(con);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateQueueNumberAfterCallNext error: " + ex.toString());
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		logger.info("updateQueueNumberAfterCallNext end");
	}

	@Override
	public void updateQueueNumberTaskInQueueMaster(String queueNo, String applicatioNo, String taskCode,
			String taskStatus) {
		logger.info("updateQueueNumberTaskInQueueMaster start");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String query = "UPDATE public.nt_m_queue_master SET que_task_code=?, que_task_status=? WHERE que_application_no=?  AND que_number=?";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, taskStatus);
			ps.setString(3, applicatioNo);
			ps.setString(4, queueNo);

			ps.executeUpdate();

			ConnectionManager.close(ps);

			/** for queue counter display start **/
			if (taskStatus != null && !taskStatus.isEmpty() && !taskStatus.trim().equalsIgnoreCase("")
					&& taskStatus.equalsIgnoreCase("C")) {
				String query2 = "UPDATE public.nt_m_queue_master SET que_skip_count=null where que_number=? and que_application_no=?";

				ps = con.prepareStatement(query2);
				ps.setString(1, queueNo);
				ps.setString(2, applicatioNo);

				ps.executeUpdate();
			}

			/** for queue counter display end **/
			ConnectionManager.commit(con);
		}catch (SQLException ex) {
			logger.error("updateQueueNumberTaskInQueueMaster error: " + ex.toString());
			
			try {
				if(con != null) {
					con.rollback();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch (Exception ex) {
			logger.error("updateQueueNumberTaskInQueueMaster error: " + ex.toString());
			ex.printStackTrace();
		} finally {

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		logger.info("updateQueueNumberTaskInQueueMaster end");
	}

	@Override
	public void updateCounterIdOfQueueNumberAfterCallNext(String queueNumber, String counterId) {
		logger.info("updateCounderIdOfQueueNumberAfterCallNext start( queue number: " + queueNumber + ", counter ID: "
				+ counterId);

		// task status - once call queue number called = 'O' after save queue number
		// ='C'

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {

			String sql3 = "UPDATE public.nt_m_queue_master SET que_counter_id=? WHERE que_number=? and que_date like '"
					+ today + "%'";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(sql3);

			ps.setString(1, counterId);
			ps.setString(2, queueNumber);

			ps.executeUpdate();

			ConnectionManager.commit(con);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateCounderIdOfQueueNumberAfterCallNext error: " + ex.toString());

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		logger.info("updateCounderIdOfQueueNumberAfterCallNext end");
	}

	@Override
	public void updateSkipQueueNumberStatus(String queueNumber) {
		logger.info("updateQueueNumberAfterCallNext start");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		int skipCount = 0;
		String skipCnt = null;
		try {

			String sql = "SELECT que_skip_count FROM public.nt_m_queue_master WHERE que_number=? and que_date like '"
					+ today + "%'";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, queueNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				skipCnt = rs.getString("que_skip_count");
			}

			if (skipCnt != null && !skipCnt.isEmpty() && !skipCnt.trim().equalsIgnoreCase("")) {
				skipCount = Integer.parseInt(skipCnt);
			}

			ConnectionManager.close(ps);

			if (skipCount == 0) {
				skipCount = 1;
			} else {
				skipCount = skipCount + 1;
			}

			String sql3 = "UPDATE public.nt_m_queue_master SET que_skip_count=? WHERE que_number=? and que_date like '"
					+ today + "%'";

			ps = con.prepareStatement(sql3);

			ps.setString(1, Integer.toString(skipCount));
			ps.setString(2, queueNumber);

			ps.executeUpdate();

			ConnectionManager.commit(con);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateQueueNumberAfterCallNext error: " + ex.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		logger.info("updateQueueNumberAfterCallNext end");
	}

	@Override
	public ParamerDTO retrieveParameterValuesForParamName(String paramName) {
		logger.info("retrieveParameterValuesForParamName start");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ParamerDTO parameterDto = null;

		try {
			String sql1 = "SELECT string_value,number_value,date_value,type FROM public.nt_r_parameters WHERE param_name=?";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(sql1);

			ps.setString(1, paramName);
			rs = ps.executeQuery();

			while (rs.next()) {
				parameterDto = new ParamerDTO();
				parameterDto.setStringValue(rs.getString("string_value"));
				parameterDto.setIntValue(rs.getInt("number_value"));
				parameterDto.setDateValue(rs.getString("date_value"));
				parameterDto.setType(rs.getString("type"));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("retrieveParameterValuesForParamName start");
		return parameterDto;
	}

	@Override
	public int updateTransactionTypeCodeForQueueNo(String queueNumber, String currentQueueType) {
		logger.info("updateTransactionTypeCodeForQueueNo start");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		String trnTypeCode = null;
		boolean update = false;
		try {

			String sql = "select que_trn_type_code from public.nt_m_queue_master where que_number=? and que_date like '"
					+ today + "%'";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, queueNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				trnTypeCode = rs.getString("que_trn_type_code");
			}

			ConnectionManager.close(ps);

			if (trnTypeCode != null && !trnTypeCode.isEmpty() && !trnTypeCode.trim().equalsIgnoreCase("")
					&& trnTypeCode.trim().equalsIgnoreCase(currentQueueType)) {
				// no need to update
			} else {
				// update queue TRN type code
				update = true;
			}

			if (update) {
				String sql2 = "UPDATE public.nt_m_queue_master SET que_trn_type_code=? where que_number=? and que_date like '"
						+ today + "%'";
				
				try {
					ps = con.prepareStatement(sql2);

					ps.setString(1, currentQueueType);
					ps.setString(2, queueNumber);

					ps.executeUpdate();
					ConnectionManager.commit(con);
				} catch (SQLException e) {
					con.rollback();
					logger.error("updateTransactionTypeCodeForQueueNo error: " + e.toString());
				}
				
			}

			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateTransactionTypeCodeForQueueNo error: " + ex.toString());
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		logger.error("updateTransactionTypeCodeForQueueNo end");

		return 0;
	}

	@Override
	public boolean findCancelledQueueNumber(String queueNo) {
		logger.info("findCancelledQueueNumber start");

		boolean cancelQueueNo = false;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {

			String sql = "select task_status from public.nt_m_queue_master where que_number=? and que_date like '"
					+ today + "%'";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, queueNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				String taskStatus = rs.getString("task_status");
				if (taskStatus != null && !taskStatus.isEmpty() && !taskStatus.trim().equalsIgnoreCase("")
						&& taskStatus.equalsIgnoreCase("O")) {
					cancelQueueNo = true;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("findCancelledQueueNumber error: " + ex.toString());
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("findCancelledQueueNumber end");
		return cancelQueueNo;
	}

	@Override
	public String findQueueNumberFromApplicationNo(String appNumber) {
		logger.info("findQueueNumberFromApplicationNo start");

		String queueNo = null;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String sql = "select pm_queue_no from public.nt_t_pm_application where pm_application_no=?";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, appNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				queueNo = rs.getString("pm_queue_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("findQueueNumberFromApplicationNo error: " + ex.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		logger.info("findQueueNumberFromApplicationNo end");
		return queueNo;
	}

	@Override
	public boolean findCancelledQueueNumberForRenewals(String queueNo, String string, String string2) {
		logger.info("findCancelledQueueNumber start");

		boolean cancelQueueNo = false;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {

			String sql = "select que_number from public.nt_m_queue_master where que_number=? and que_date like '"
					+ today
					+ "%'  and (que_task_code='PM101' and que_task_status='C') or (que_task_code='PR200' and que_task_status='O');";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, queueNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				String taskCurrentQueueNo = rs.getString("que_number");
				if (taskCurrentQueueNo != null && !taskCurrentQueueNo.isEmpty()
						&& !taskCurrentQueueNo.trim().equalsIgnoreCase("")) {
					cancelQueueNo = true;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("findCancelledQueueNumber error: " + ex.toString());
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("findCancelledQueueNumber end");
		return cancelQueueNo;
	}

	@Override
	public void updateNT_R_NT_R_PARAMETERS(ParamerDTO paramDto, String paramName) {
		logger.info("updateCurrentQueueNumber start");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String sql3 = "UPDATE public.nt_r_parameters SET string_value=?,number_value=?,date_value=? WHERE param_name=? ";

			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(sql3);

			ps.setString(1, paramDto.getStringValue());
			ps.setInt(2, paramDto.getIntValue());
			ps.setString(3, paramDto.getDateValue());
			ps.setString(4, paramName);

			ps.executeUpdate();

			ConnectionManager.commit(con);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateCurrentQueueNumber error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		logger.info("updateCurrentQueueNumber end");
	}

	@Override
	public void updateStatusOfQueApp(String queueNo, String appNo) {
		logger.info("updateQueueNumberAfterCallNext start");

		// task status - once call queue number called = 'O' after save queue number
		// ='C'

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {

			String sql3 = "update nt_m_queue_master " + "set que_application_no=? "
					+ " WHERE que_number=? and que_date like '" + today + "%'";
			con = ConnectionManager.getConnection();
			try {
				ps = con.prepareStatement(sql3);

				ps.setString(1, appNo);
				ps.setString(2, queueNo);

				ps.executeUpdate();

				ConnectionManager.commit(con);
			}catch (SQLException e) {
				con.rollback();
				logger.error("updateQueueNumberAfterCallNext error: " + e.toString());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateQueueNumberAfterCallNext error: " + ex.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		logger.info("updateQueueNumberAfterCallNext end");
	}

	@Override
	public void updataApplicationDetailQueueCallCounterId(String counterId, String vehicleNo, String appNo,
			String tskCode, String tskStatus) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String query = "UPDATE public.nt_t_task_det SET tsd_counterid=? WHERE tsd_vehicle_no=? AND tsd_app_no=? AND tsd_task_code=? AND tsd_status=?";
			con = ConnectionManager.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, counterId);
			ps.setString(2, vehicleNo);
			ps.setString(3, appNo);
			ps.setString(4, tskCode);
			ps.setString(5, tskStatus);

			ps.executeUpdate();

			ConnectionManager.commit(con);
		} catch (Exception ex) {
			logger.error("updateQueueNumberTaskInQueueMaster error: " + ex.toString());
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public void moveDocumentsA(String applicationNo, String fileName, String newPath, int i) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_application_document_version");

			String sql;

			sql = "INSERT INTO nt_t_application_document_version "
					+ "(seqno, apd_transaction_type,apd_application_no,apd_permit_no,apd_file_path, apd_doc_type, apd_created_date, apd_versionno,apd_doc_code,apd_document_des) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, "10");
			stmt.setString(3, applicationNo);
			stmt.setString(4, fileName);
			stmt.setString(5, newPath);
			stmt.setString(6, "O");
			stmt.setTimestamp(7, timestamp);
			stmt.setInt(8, i);
			stmt.setString(9, "SCAN");
			stmt.setString(10, "SCAN DOCUMENT");

			stmt.executeUpdate();

			ConnectionManager.commit(con);

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	public void moveDocumentsB(String applicationNo, String fileName, String mainPath) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_application_document");
			String sql;

			sql = "INSERT INTO nt_t_application_document "
					+ "(seqno, apd_transaction_type,apd_application_no,apd_permit_no,apd_file_path, apd_doc_type, apd_created_date,apd_doc_code,apd_document_des) "
					+ "VALUES(?,?,?,?,?,?,?,?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, "10");
			stmt.setString(3, applicationNo);
			stmt.setString(4, fileName);
			stmt.setString(5, mainPath);
			stmt.setString(6, "O");
			stmt.setTimestamp(7, timestamp);
			stmt.setString(8, "SCAN");
			stmt.setString(9, "SCAN DOCUMENT");

			stmt.executeUpdate();

			ConnectionManager.commit(con);

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void moveDocumentsC(String fileName) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_r_error_document");
			String sql;

			sql = "INSERT INTO nt_r_error_document " + "(seq_no,permit_no) " + "VALUES(?,?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, fileName);

			stmt.executeUpdate();

			ConnectionManager.commit(con);

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void editTimeTableManagementBeanA(String referanceNum, String groupNum, String tripType,
			String duplicateBusNum, String startTime, String endTime, String busNum, int tripIdInt,
			String withFixedTimeCode, String user) {

		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			/** add updated data to history table start **/
			con = ConnectionManager.getConnection();
			long masterSeq = 0;
			String query1 = "select seq_no  from public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and trip_type=?";
			ps = con.prepareStatement(query1);
			ps.setString(1, referanceNum);
			ps.setString(2, groupNum);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			while (rs.next()) {
				masterSeq = rs.getLong("seq_no");
			}

			List<TimeTableDTO> list = new ArrayList<TimeTableDTO>();
			TimeTableDTO historyDTO = new TimeTableDTO();
			historyDTO.setMasterSeq(masterSeq);
			historyDTO.setGenereatedRefNo(referanceNum);
			historyDTO.setBusNo(duplicateBusNum);
			historyDTO.setStartTime(startTime);
			historyDTO.setEndTime(endTime);
			historyDTO.setAssigneBusNo(busNum);
			historyDTO.setTripIdInt(tripIdInt);
			historyDTO.setWithFixedTimeCode(withFixedTimeCode);
			historyDTO.setTripType(tripType);
			historyDTO.setGroup(groupNum);
			list.add(historyDTO);

			timeTableService.insertTimeTableGeneratorDetDataForHistorySave(list, user);
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		/** add updated data to history table end **/
	}

	@Override
	public void editTimeTableManagementBeanB(String referanceNum, String groupNum, TimeTableDTO chagedDTO,
			String user) {

		timeTableService = (TimeTableService) SpringApplicationContex.getBean("timeTableService");
		panelGeneratorWithoutFixedTimeService = (PanelGeneratorWithoutFixedTimeService) SpringApplicationContex
				.getBean("panelGeneratorWithoutFixedTimeService");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();
			long masterSeq = 0;

			String query = "select seq_no  from public.nt_m_timetable_generator where generator_ref_no=? and group_no=? and trip_type=?";

			ps = con.prepareStatement(query);
			ps.setString(1, referanceNum);
			ps.setString(2, groupNum);
			ps.setString(3, chagedDTO.getTripType());
			rs = ps.executeQuery();

			while (rs.next()) {
				masterSeq = rs.getLong("seq_no");
			}

			panelGeneratorWithoutFixedTimeService.updateIntoTimetableGenerator(con, chagedDTO, groupNum,
					chagedDTO.getTripType(), referanceNum, masterSeq, user, chagedDTO.getDuplicateBusNum());

			ConnectionManager.commit(con);
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public void updateRouteScheduleGeneratorTable(String referanceNum, String groupNum, String duplicateBusNum,
			String busNum, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();
			
			String query = "UPDATE public.nt_t_route_schedule_generator_det01 SET bus_no=?, edited_from=? WHERE generator_ref_no=? and bus_no=?";
//			 and group_no=?
			ps = con.prepareStatement(query);
			ps.setString(1, busNum);// new bus number
			ps.setString(2, "EF");
			ps.setString(3, referanceNum);
//			ps.setString(4, groupNum);
			ps.setString(4, duplicateBusNum);// old bus number

			ps.executeUpdate();

			ConnectionManager.commit(con);
		} catch (Exception ex) {
			logger.error("updateRouteScheduleGeneratorTable error: " + ex.toString());
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	
		
	}
}
