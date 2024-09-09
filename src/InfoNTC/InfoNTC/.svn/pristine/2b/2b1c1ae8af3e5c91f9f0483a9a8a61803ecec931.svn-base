package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.DisplayQueueNumbersDTO;
import lk.informatics.ntc.model.dto.ParamerDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.SpringApplicationContex;

/**
 * @author pathum.m@informaticsint.com
 * 
 */

/* implements DisplayQueueNumbersService , Serializable */
public class DisplayQueueNumbersServiceImpl implements DisplayQueueNumbersService, Serializable {

	private MigratedService migratedService;

	private static final long serialVersionUID = 1L;
	private static int numOfCurrentRemoving = 1;
	private static int numOfCurrentRemovingSkiped = 1;
	private int queueNumCount;

	// Get current date
	private java.util.Date date = new java.util.Date();
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	private String today = df.format(date);

	/**
	 * This method retrieve Counter,Counter No,Counter Name from nt_r_counter table
	 * Then retrieve Next Numbers for each related Counter from nt_m_queue_master
	 * table
	 * 
	 * @return List<DisplayQueueNumbersDTO> queueDisplayBoard This returns counter
	 *         details objects which includes counter counter No counter Name next
	 *         Numbers
	 */
	@Override
	public List<DisplayQueueNumbersDTO> counterDetails() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<DisplayQueueNumbersDTO> queueDisplayBoard = new ArrayList<DisplayQueueNumbersDTO>();

		try {

			con = ConnectionManager.getConnection();
			// Get Counter,Counter No,Counter Name from nt_r_counter table
			String sql = "select cou_counter_id,cou_counter_name,cou_serving_queueno from nt_r_counter where cou_status='A' order by cou_counter_id";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			ResultSet rs1 = null;
			PreparedStatement ps1 = null;

			while (rs.next()) {
				// Set Counter,Counter No,Counter Name to the DTO
				DisplayQueueNumbersDTO tableRaw_obj = new DisplayQueueNumbersDTO();
				tableRaw_obj.setCounterId(rs.getString("cou_counter_id"));
				tableRaw_obj.setCounterName(rs.getString("cou_counter_name"));
				tableRaw_obj.setCounterCurrentNo(rs.getString("cou_serving_queueno"));
				// Get Next Numbers related to counter Id from nt_m_queue_master table
				String sql1 = "select * from nt_m_queue_master where que_date like '" + today + "%' "
						+ "and que_trn_type_code = '02'  and que_task_code is null and que_task_status is null \r\n"
						+ "and (que_skip_count < '5' or que_skip_count is null)\r\n" + "order by que_order limit '5'";
				ps1 = con.prepareStatement(sql1);
				rs1 = ps1.executeQuery();
				String[] nextNumbers = null; // use to store Next Numbers for counter id at each time
				int i = 0;
				// Set Next Numbers to DTO for Next Number labels in table
				while (rs1.next()) {
					if (i == 0)
						tableRaw_obj.setQueueNo1(rs1.getString("que_number"));
					if (i == 1)
						tableRaw_obj.setQueueNo2(rs1.getString("que_number"));
					if (i == 2)
						tableRaw_obj.setQueueNo3(rs1.getString("que_number"));
					if (i == 3)
						tableRaw_obj.setQueueNo4(rs1.getString("que_number"));
					if (i == 4)
						tableRaw_obj.setQueueNo5(rs1.getString("que_number"));
					i++;
				}
				// Set Next Numbers to
				tableRaw_obj.setNextNumbers(nextNumbers);
				// Insert one raw to the counter details table
				queueDisplayBoard.add(tableRaw_obj);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return queueDisplayBoard;

	}

	public List<DisplayQueueNumbersDTO> inspectionNextNumbers() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		ParamerDTO paramDto = migratedService.retrieveParameterValuesForParamName("QUEUE_MASTER_SKIP_COUNT");
		int skipCount = paramDto.getIntValue();

		List<DisplayQueueNumbersDTO> queueDisplayBoard = new ArrayList<DisplayQueueNumbersDTO>();
		DisplayQueueNumbersDTO tableRaw_obj = new DisplayQueueNumbersDTO();
		try {
			con = ConnectionManager.getConnection();

			// Get Next Numbers related to counter Id from nt_m_queue_master table
			// '"+today+"%'
			String sql1 = "select * from nt_m_queue_master where que_date like '" + today + "%' "
					+ "and que_trn_type_code = '02'  and que_task_code is null and que_task_status is null \r\n"
					+ "and (que_skip_count < ? or que_skip_count is null)\r\n" + "order by que_order limit '5'";
			ps = con.prepareStatement(sql1);
			ps.setInt(1, skipCount);
			rs = ps.executeQuery();
			String[] nextNumbers = null; // use to store Next Numbers for counter id at each time
			int i = 0;
			// Set Next Numbers to DTO for Next number labels in table
			while (rs.next()) {
				if (i == 0)
					tableRaw_obj.setQueueNo1(rs.getString("que_number"));
				if (i == 1)
					tableRaw_obj.setQueueNo2(rs.getString("que_number"));
				if (i == 2)
					tableRaw_obj.setQueueNo3(rs.getString("que_number"));
				if (i == 3)
					tableRaw_obj.setQueueNo4(rs.getString("que_number"));
				if (i == 4)
					tableRaw_obj.setQueueNo5(rs.getString("que_number"));
				i++;
			}
			// Set Next Numbers to
			tableRaw_obj.setNextNumbers(nextNumbers);
			// Insert one raw to the counter details table
			queueDisplayBoard.add(tableRaw_obj);

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return queueDisplayBoard;
	}

	@Override
	public List<String> callNextQueueNumbersForDisplayAction(String transactionType, String previousTransType) {
		return null;
	}

	/**
	 * This method retrieve called numbers and remove numbers step by step according
	 * to look like changes. This happens because we are calling this method in
	 * every 4 seconds in backing bean. every times we are removing first
	 * 1,2,3,...objects
	 */

	@Override
	public List<DisplayQueueNumbersDTO> calledNumbers() {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<DisplayQueueNumbersDTO> calledNumbers = new ArrayList<DisplayQueueNumbersDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "select distinct que_order , que_number from nt_m_queue_master where que_date like '" + today
					+ "%' and que_task_status = 'C' order by que_order DESC ";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			queueNumCount = 0;

			boolean results = false;
			while (rs.next()) {
				results = true;
				queueNumCount++;
				DisplayQueueNumbersDTO dto = new DisplayQueueNumbersDTO();
				dto.setQueueNo1(rs.getString("que_number"));
				calledNumbers.add(dto);
			}

			if (results) {
				if (calledNumbers.size() - numOfCurrentRemoving < 7) { // if numbers are less than six add removed to
																		// end
					DisplayQueueNumbersDTO tempt = null;
					for (int i = 0; i < numOfCurrentRemoving; i++) {
						tempt = calledNumbers.get(0);
						calledNumbers.remove(0);
						calledNumbers.add(tempt);
					}
				} else {
					for (int i = 0; i < numOfCurrentRemoving; i++) {
						calledNumbers.remove(0);
					}
				}
				if (queueNumCount <= numOfCurrentRemoving) {
					numOfCurrentRemoving = 0;
				}
				numOfCurrentRemoving++;
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return calledNumbers;
	}

	@Override
	public List<DisplayQueueNumbersDTO> permenantlySkipedNumbers(String skipCount) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<DisplayQueueNumbersDTO> skipedNumbers = new ArrayList<DisplayQueueNumbersDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT distinct que_order , que_number FROM nt_m_queue_master " + "WHERE que_date like '"
					+ today + "%' and que_skip_count >= ? " + "AND que_number NOT IN ( SELECT cou_serving_queueno "
					+ "FROM nt_r_counter WHERE cou_serving_queueno IS NOT NULL) " + "order by que_order DESC ";// 3

			ps = con.prepareStatement(sql);
			ps.setString(1, skipCount);
			rs = ps.executeQuery();
			queueNumCount = 0;

			boolean results = false;
			while (rs.next()) {
				results = true;
				queueNumCount++;
				DisplayQueueNumbersDTO dto = new DisplayQueueNumbersDTO();
				dto.setQueueNo1(rs.getString("que_number"));
				skipedNumbers.add(dto);
			}

			if (results) {
				if (skipedNumbers.size() - numOfCurrentRemovingSkiped < 7) { // if numbers are less than six add removed
																				// to end
					DisplayQueueNumbersDTO tempt = null;
					for (int i = 0; i < numOfCurrentRemovingSkiped; i++) {
						tempt = skipedNumbers.get(0);
						skipedNumbers.remove(0);
						skipedNumbers.add(tempt);
					}
				} else {
					for (int i = 0; i < numOfCurrentRemovingSkiped; i++) {
						skipedNumbers.remove(0);
					}
				}
				if (queueNumCount <= numOfCurrentRemovingSkiped) {
					numOfCurrentRemovingSkiped = 0;
				}
				numOfCurrentRemovingSkiped++;
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return skipedNumbers;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public static int getNumOfCurrentRemoving() {
		return numOfCurrentRemoving;
	}

	public static void setNumOfCurrentRemoving(int numOfCurrentRemoving) {
		DisplayQueueNumbersServiceImpl.numOfCurrentRemoving = numOfCurrentRemoving;
	}

	public static int getNumOfCurrentRemovingSkiped() {
		return numOfCurrentRemovingSkiped;
	}

	public static void setNumOfCurrentRemovingSkiped(int numOfCurrentRemovingSkiped) {
		DisplayQueueNumbersServiceImpl.numOfCurrentRemovingSkiped = numOfCurrentRemovingSkiped;
	}

	public int getQueueNumCount() {
		return queueNumCount;
	}

	public void setQueueNumCount(int queueNumCount) {
		this.queueNumCount = queueNumCount;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public DateFormat getDf() {
		return df;
	}

	public void setDf(DateFormat df) {
		this.df = df;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
