package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import lk.informatics.ntc.model.dto.DepartmentDTO;
import lk.informatics.ntc.model.dto.DisplayQueueNumbersDTO;
import lk.informatics.ntc.model.dto.DivisionDTO;
import lk.informatics.ntc.model.dto.MainCounterDTO;
import lk.informatics.ntc.model.dto.ParamerDTO;
import lk.informatics.ntc.model.dto.QueueNumberDTO;
import lk.informatics.ntc.model.dto.TransactionTypeDTO;
import lk.informatics.ntc.model.dto.UserClosedDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;
import lk.informatics.roc.utils.common.Utils;

public class QueueManagementServiceImpl implements QueueManagementService, Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger("QueueManagementServiceImpl");

	private static final String TENDER = getPropertyFileVale("queue.transaction.type.code.tender");// 01
	private static final String INSPECTION = getPropertyFileVale("queue.transaction.type.code.inspection");// 02
	private static final String PERMIT = getPropertyFileVale("queue.transaction.type.code.new.permit");// 03
	private static final String RENEWAL = getPropertyFileVale("queue.transaction.type.code.renewal");// 04
	private static final String AMMENDMENT = getPropertyFileVale("queue.transaction.type.code.ammendment");// 05
	private static final String PAYMENT = getPropertyFileVale("queue.transaction.type.code.payment");// 06
	private static final String CANCEL = getPropertyFileVale("queue.transaction.type.code.cancel");// 07
	private static final String SURVEY = getPropertyFileVale("queue.transaction.type.code.survey");// 08
	private static final String INQUIRY = getPropertyFileVale("queue.transaction.type.code.inquiry");// 09
	private static final String AMEND_BUS = getPropertyFileVale("queue.transaction.type.code.ammendment.bus");// 13
	private static final String AMEND_SERVICE = getPropertyFileVale("queue.transaction.type.code.ammendment.service");// 05
	private static final String AMEND_OWNER = getPropertyFileVale("queue.transaction.type.code.ammendment.bus.owner");// 14
	private static final String AMEND_OWNER_BUS = getPropertyFileVale(
			"queue.transaction.type.code.ammendment.owner.bus.owner");// 15
	private static final String AMEND_SERVICE_BUS = getPropertyFileVale(
			"queue.transaction.type.code.ammendment.service.bus.owner");// 16
	private static final String INSPECTION_AMMEND = getPropertyFileVale(
			"queue.transaction.type.code.inspection.ammendment");// 17
	private static final String AMMENDMENT_SERVICE = getPropertyFileVale(
			"queue.transaction.type.code.amendment.service");// 21
	private static final String AMMENDMENT_ROUTE = getPropertyFileVale("queue.transaction.type.code.amendment.route");// 22
	private static final String AMMENDMENT_END = getPropertyFileVale("queue.transaction.type.code.amendment.end");// 23

	private static final String OTHER_INSPECTION = getPropertyFileVale("queue.transaction.type.code.other.inspection");// OI
	private static final String OTHER_INSPECTION_COMPLAIN = getPropertyFileVale(
			"queue.transaction.type.code.other.inspection.complain");// 115
	private static final String OTHER_INSPECTION_INQUIRY = getPropertyFileVale(
			"queue.transaction.type.code.other.inspection.inquiry");// 116
	private static final String OTHER_INSPECTION_SITE_VISIT = getPropertyFileVale(
			"queue.transaction.type.code.other.inspection.site.visit");// 117

	private static final String RENEWAL_PREVIOUS_TASK = "PM101";

	private CommonService commonService;
	private MigratedService migratedService;

	private boolean normalCheck = false;
	private boolean priorityCheck = false;

	@Override
	public List<TransactionTypeDTO> retrieveTransactionTypeList() {
		logger.info("retrieveTransactionTypeList start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<TransactionTypeDTO> transTypeList = new ArrayList<TransactionTypeDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT code, description FROM nt_r_transaction_type WHERE active = 'Y' order by description";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				TransactionTypeDTO dto = new TransactionTypeDTO();
				dto.setTransCode(rs.getString("code"));
				dto.setTransDesc(rs.getString("description"));
				transTypeList.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("retrieveTransactionTypeList error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("retrieveTransactionTypeList end");

		return transTypeList;

	}

	@Override
	public String insertQueueDataIntoNT_M_QUEUE_MASTER(QueueNumberDTO queueNumberDTO, String loginUser) {
		logger.info("insertQueueDataIntoNT_M_QUEUE_MASTER start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String queueNo = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		try {

			con = ConnectionManager.getConnection();

			/** get current running value start **/
			long runningValue = retrieveCurrentQueueNumber(con, queueNumberDTO.getTransTypeCode().trim(),
					queueNumberDTO.getQueueService());// queueService-Normal(N),Priority(P)
			/** get current running value end **/

			/** get current queue number start **/
			queueNo = getFormattedQueueNumber(queueNumberDTO.getTransTypeCode().trim(), runningValue);
			/** get current queue number end **/

			/** update current queue number nt_r_queue_number_run_value start **/
			long tempRunVal = runningValue + 1;
			updateCurrentQueueNumber(con, Long.toString(tempRunVal), queueNumberDTO.getTransTypeCode().trim(),
					queueNumberDTO.getQueueService());// queueService-Normal(N),Priority(P)

			/** update current queue number nt_r_queue_number_run_value end **/

			/** select vehicle number and permit number start **/
			if (!queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INQUIRY)) {
				QueueNumberDTO dto = new QueueNumberDTO();
				if (queueNumberDTO.getTransTypeCode() != null && !queueNumberDTO.getTransTypeCode().isEmpty()
						&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("")
						&& (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(RENEWAL)
								|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(PERMIT))) {
					dto = validateUserInputValuesForRNAndNP(queueNumberDTO);
				} else {
					if (queueNumberDTO.getTransTypeCode() != null && !queueNumberDTO.getTransTypeCode().isEmpty()
							&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("")
							&& (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_BUS)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER_BUS)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE_BUS)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_SERVICE)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_ROUTE)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_END))) {
					} else {
						dto = validateUserInputValues(queueNumberDTO);
					}

				}

				if (dto != null) {
					if (dto.getVehicleNo() != null && !dto.getVehicleNo().isEmpty()
							&& !dto.getVehicleNo().trim().equalsIgnoreCase("")) {
						queueNumberDTO.setVehicleNo(dto.getVehicleNo());
					}

					if (dto.getPermitNo() != null && !dto.getPermitNo().isEmpty()
							&& !dto.getPermitNo().trim().equalsIgnoreCase("")) {
						queueNumberDTO.setPermitNo(dto.getPermitNo());
					}
				}
			}
			/** select vehicle number and permit number end **/

			/**
			 * in inspection after input vehicle number get application number from
			 **/
			if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INSPECTION)) {
				QueueNumberDTO dto = new QueueNumberDTO();
				dto = validateUserInputValues(queueNumberDTO);
				if (dto != null) {
					queueNumberDTO.setApplicationNo(dto.getApplicationNo());
					queueNumberDTO.setPermitNo(dto.getPermitNo());
				}
			}
			/**
			 * in inspection after input vehicle number get application number from
			 * nt_t_pm_application table end
			 **/

			/**
			 * in tender get permit and vehicle number both and insert to Queue Master table
			 * start
			 **/
			if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_BUS)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE_BUS)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER_BUS)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_SERVICE)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_ROUTE)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_END)) {

				if (queueNumberDTO.getVehicleNo() == null || queueNumberDTO.getVehicleNo().isEmpty()
						|| queueNumberDTO.getPermitNo() == null || queueNumberDTO.getPermitNo().isEmpty()) {
					QueueNumberDTO tempQueueNumberDTO = retrieveVehicleAndPermitNumberForAmendment(con, queueNumberDTO);
					if (tempQueueNumberDTO != null) {
						if (tempQueueNumberDTO.getVehicleNo() != null && !tempQueueNumberDTO.getVehicleNo().isEmpty()) {
							queueNumberDTO.setVehicleNo(tempQueueNumberDTO.getVehicleNo());
						}
						if (tempQueueNumberDTO.getPermitNo() != null && !tempQueueNumberDTO.getPermitNo().isEmpty()) {
							queueNumberDTO.setPermitNo(tempQueueNumberDTO.getPermitNo());
						}
					}
				}
			}
			/**
			 * in tender get permit and vehicle number both and insert to Queue Master table
			 * end
			 **/

			/*
			 * Changes : When generate the renewal token, old record should be delete from
			 * the nt_m_queue_master table and insert into nt_h_queue_master table, before
			 * inserting the new token in the nt_m_queue_master table. Done By : Dinushi
			 * Ranasinghe Date : 08/10/2019 --START--
			 */
			if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(RENEWAL)) {
				String strAppNo = queueNumberDTO.getApplicationNo();
				PreparedStatement ps0 = null;
				PreparedStatement ps1 = null;

				String strInsSQl = "INSERT INTO  public.nt_h_queue_master "
						+ " (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, que_issued_by, que_service_type, que_counter_id, que_curr_workflow_id, que_skip_count, created_by, created_date, modified_by, modified_date, que_permit_no, que_vehicle_no, que_application_no, tender_ref_no, task_status, que_order, que_order_set, que_task_code, que_task_status) "
						+ " SELECT que_seq, que_trn_type_code, que_date, que_number, que_issued_date, que_issued_by, que_service_type, que_counter_id, que_curr_workflow_id, que_skip_count, created_by, created_date, modified_by, modified_date, que_permit_no, que_vehicle_no, que_application_no, tender_ref_no, task_status, que_order, que_order_set, que_task_code, que_task_status "
						+ " FROM public.nt_m_queue_master where que_application_no = '" + strAppNo + "'";

				String strDetSql = "delete from public.nt_m_queue_master where que_application_no = '" + strAppNo + "'";

				ps0 = con.prepareStatement(strInsSQl);
				ps1 = con.prepareStatement(strDetSql);
				ps0.executeUpdate();
				ps1.executeUpdate();

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_queue_master");

				String sql = "INSERT INTO public.nt_m_queue_master (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, "
						+ "que_issued_by, que_service_type, created_by, created_date, que_permit_no, que_vehicle_no, que_application_no, que_counter_id, que_task_code, que_task_status) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,'PR200','O')";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, queueNumberDTO.getTransTypeCode().trim());
				ps.setString(3, df.format(date));
				ps.setString(4, queueNo);
				ps.setString(5, df.format(date));
				ps.setString(6, loginUser);
				if (queueNumberDTO.getQueueService().equalsIgnoreCase("priority")) {
					ps.setString(7, "P");
				} else {
					ps.setString(7, "N");
				}
				ps.setString(8, loginUser);
				ps.setTimestamp(9, timestamp);
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					ps.setString(10, queueNumberDTO.getPermitNo().trim());
				} else {
					ps.setNull(10, Types.VARCHAR);
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					ps.setString(11, queueNumberDTO.getVehicleNo().trim());
				} else {
					ps.setNull(11, Types.VARCHAR);
				}
				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					ps.setString(12, queueNumberDTO.getApplicationNo());
				} else {
					ps.setNull(12, Types.VARCHAR);
				}
				if (queueNumberDTO.getTenderRefNo() != null && !queueNumberDTO.getTenderRefNo().isEmpty()
						&& !queueNumberDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {
					ps.setString(13, queueNumberDTO.getTenderRefNo());
				} else {
					ps.setNull(13, Types.VARCHAR);
				}

				/*
				 * Changes : When generate the renewal token, old record should be delete from
				 * the nt_m_queue_master table and insert into nt_h_queue_master table, before
				 * inserting the new token in the nt_m_queue_master table. Done By : Dinushi
				 * Ranasinghe Date : 08/10/2019 --END--
				 */
			} else if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INSPECTION_AMMEND)) {

				String strAppNo = queueNumberDTO.getApplicationNo();
				PreparedStatement ps0 = null;
				PreparedStatement ps1 = null;

				String strInsSQl = "INSERT INTO  public.nt_h_queue_master "
						+ " (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, que_issued_by, que_service_type, que_counter_id, que_curr_workflow_id, que_skip_count, created_by, created_date, modified_by, modified_date, que_permit_no, que_vehicle_no, que_application_no, tender_ref_no, task_status, que_order, que_order_set, que_task_code, que_task_status) "
						+ " SELECT que_seq, que_trn_type_code, que_date, que_number, que_issued_date, que_issued_by, que_service_type, que_counter_id, que_curr_workflow_id, que_skip_count, created_by, created_date, modified_by, modified_date, que_permit_no, que_vehicle_no, que_application_no, tender_ref_no, task_status, que_order, que_order_set, que_task_code, que_task_status "
						+ " FROM public.nt_m_queue_master where que_application_no = '" + strAppNo + "'";

				String strDetSql = "delete from public.nt_m_queue_master where que_application_no = '" + strAppNo + "'";

				ps0 = con.prepareStatement(strInsSQl);
				ps1 = con.prepareStatement(strDetSql);
				ps0.executeUpdate();
				ps1.executeUpdate();

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_queue_master");

				String sql = "INSERT INTO public.nt_m_queue_master (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, "
						+ "que_issued_by, que_service_type, created_by, created_date, que_permit_no, que_vehicle_no, que_application_no, que_counter_id) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, queueNumberDTO.getTransTypeCode().trim());
				ps.setString(3, df.format(date));
				ps.setString(4, queueNo);
				ps.setString(5, df.format(date));
				ps.setString(6, loginUser);
				if (queueNumberDTO.getQueueService().equalsIgnoreCase("priority")) {
					ps.setString(7, "P");
				} else {
					ps.setString(7, "N");
				}
				ps.setString(8, loginUser);
				ps.setTimestamp(9, timestamp);
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					ps.setString(10, queueNumberDTO.getPermitNo().trim());
				} else {
					ps.setNull(10, Types.VARCHAR);
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					ps.setString(11, queueNumberDTO.getVehicleNo().trim());
				} else {
					ps.setNull(11, Types.VARCHAR);
				}
				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					ps.setString(12, queueNumberDTO.getApplicationNo());
				} else {
					ps.setNull(12, Types.VARCHAR);
				}
				if (queueNumberDTO.getTenderRefNo() != null && !queueNumberDTO.getTenderRefNo().isEmpty()
						&& !queueNumberDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {
					ps.setString(13, queueNumberDTO.getTenderRefNo());
				} else {
					ps.setNull(13, Types.VARCHAR);
				}

			} else {
				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_queue_master");

				String sql = "INSERT INTO public.nt_m_queue_master (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, "
						+ "que_issued_by, que_service_type, created_by, created_date, que_permit_no, que_vehicle_no, que_application_no, que_counter_id) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, queueNumberDTO.getTransTypeCode().trim());
				ps.setString(3, df.format(date));
				ps.setString(4, queueNo);
				ps.setString(5, df.format(date));
				ps.setString(6, loginUser);
				if (queueNumberDTO.getQueueService().equalsIgnoreCase("priority")) {
					ps.setString(7, "P");
				} else {
					ps.setString(7, "N");
				}
				ps.setString(8, loginUser);
				ps.setTimestamp(9, timestamp);
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					ps.setString(10, queueNumberDTO.getPermitNo().trim());
				} else {
					ps.setNull(10, Types.VARCHAR);
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					ps.setString(11, queueNumberDTO.getVehicleNo().trim());
				} else {
					ps.setNull(11, Types.VARCHAR);
				}
				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					ps.setString(12, queueNumberDTO.getApplicationNo());
				} else {
					ps.setNull(12, Types.VARCHAR);
				}
				if (queueNumberDTO.getTenderRefNo() != null && !queueNumberDTO.getTenderRefNo().isEmpty()
						&& !queueNumberDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {
					ps.setString(13, queueNumberDTO.getTenderRefNo());
				} else {
					ps.setNull(13, Types.VARCHAR);
				}
			}
			/* End Change */

			ps.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("insertQueueDataIntoNT_M_QUEUE_MASTER error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("insertQueueDataIntoNT_M_QUEUE_MASTER end");
		return queueNo;
	}

	private QueueNumberDTO retrieveVehicleAndPermitNumberForAmendment(Connection con, QueueNumberDTO queueNumberDTO) {
		logger.info("retrieveVehicleAndPermitNumberForAmendment start");

		ResultSet rs = null;
		PreparedStatement ps = null;
		String where = "";
		QueueNumberDTO returnDto = new QueueNumberDTO();
		;
		try {

			if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
					&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
				where = " pm_permit_no='" + queueNumberDTO.getPermitNo() + "'";
			} else if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
					&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
				where = " pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
			}

			String sql = "select * from public.nt_t_pm_application where pm_status='A' and " + where;

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				returnDto.setVehicleNo(rs.getString("pm_vehicle_regno"));
				returnDto.setPermitNo(rs.getString("pm_permit_no"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("retrieveVehicleAndPermitNumberForAmendment error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		logger.info("retrieveVehicleAndPermitNumberForAmendment end");
		return returnDto;
	}

	@Override
	public boolean checkAmmenmentValidation(QueueNumberDTO queueNumberDTO) {
		logger.info("checkAmmenmentValidation start");

		Connection con = null;
		boolean valid = false;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String where = "";
		boolean whereFill = false;
		try {
			con = ConnectionManager.getConnection();

			if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
					&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
				where = "pm_permit_no='" + queueNumberDTO.getPermitNo() + "'";
				whereFill = true;
			}

			if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
					&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
				if (whereFill) {
					where = where + " and pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
				} else {
					where = "pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
				}
			}

			String sql = "select * from public.nt_t_pm_application where pm_status = 'A' and " + where;

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("retrieveCurrentQueueNumber error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("checkAmmenmentValidation end");
		return valid;
	}

	private void updateCurrentQueueNumber(Connection con, String queueNo, String transType, String queueService) {
		logger.info("updateCurrentQueueNumber start");

		PreparedStatement ps = null;
		ResultSet rs = null;
		String setStatement = null;
		try {

			if (transType != null && !transType.isEmpty() && (transType.equalsIgnoreCase(AMEND_BUS)
					|| transType.equalsIgnoreCase(AMEND_SERVICE) || transType.equalsIgnoreCase(AMEND_OWNER)
					|| transType.equalsIgnoreCase(AMEND_SERVICE_BUS) || transType.equalsIgnoreCase(AMEND_OWNER_BUS)
					|| transType.equalsIgnoreCase(AMMENDMENT_SERVICE) || transType.equalsIgnoreCase(AMMENDMENT_ROUTE)
					|| transType.equalsIgnoreCase(AMMENDMENT_END))) {
				transType = AMMENDMENT;
			}

			if (transType != null && !transType.isEmpty() && transType.equalsIgnoreCase(INSPECTION_AMMEND)) {
				transType = INSPECTION;
			}

			if (queueService.equalsIgnoreCase("priority")) {
				setStatement = "priority_current_no";
			} else {
				setStatement = "normal_current_no";
			}

			String sql = "UPDATE public.nt_r_queue_number_run_value SET " + setStatement + "=? where trans_code=?";

			ps = con.prepareStatement(sql);

			ps.setString(1, queueNo);
			ps.setString(2, transType);

			ps.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateCurrentQueueNumber error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		logger.info("updateCurrentQueueNumber end");
	}

	public long retrieveCurrentQueueNumber(Connection con, String transType, String serviceType) {
		// transType - Transaction Type
		// serviceType - priority/normal
		logger.info("retrieveCurrentQueueNumber start");

		ResultSet rs = null;
		PreparedStatement ps = null;
		long currQueueNum = 0l;

		if (transType != null && !transType.isEmpty() && (transType.equalsIgnoreCase(AMEND_BUS)
				|| transType.isEmpty() && transType.equalsIgnoreCase(AMEND_SERVICE)
				|| transType.equalsIgnoreCase(AMEND_OWNER) || transType.equalsIgnoreCase(AMEND_SERVICE_BUS)
				|| transType.equalsIgnoreCase(AMEND_OWNER_BUS) || transType.equalsIgnoreCase(AMMENDMENT_SERVICE)
				|| transType.equalsIgnoreCase(AMMENDMENT_ROUTE) || transType.equalsIgnoreCase(AMMENDMENT_END))) {
			transType = AMMENDMENT;
		}

		if (transType != null && !transType.isEmpty() && transType.equalsIgnoreCase(INSPECTION_AMMEND)) {
			transType = INSPECTION;
		}

		try {
			String sql = "SELECT normal_current_no, priority_current_no FROM public.nt_r_queue_number_run_value WHERE trans_code=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, transType);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (serviceType.equalsIgnoreCase("priority")) {
					// priority
					currQueueNum = rs.getLong("priority_current_no");
				} else {
					// serviceType = "N"; normal
					currQueueNum = rs.getLong("normal_current_no");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("retrieveCurrentQueueNumber error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}
		logger.info("retrieveCurrentQueueNumber end");

		return currQueueNum;

	}

	@Override
	public String retrieveTransDescFromTransCode(String transCode) {
		logger.info("retrieveTransDescFromTransCode start");

		if (transCode != null && !transCode.isEmpty()
				&& (transCode.equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
						|| transCode.equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
						|| transCode.equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT))) {
			transCode = OTHER_INSPECTION;
		}

		String transDesc = null;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT description FROM public.nt_r_transaction_type WHERE code=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, transCode.trim());
			rs = ps.executeQuery();

			while (rs.next()) {
				transDesc = rs.getString("description");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("retrieveTransDescFromTransCode error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("retrieveTransDescFromTransCode end");

		return transDesc;
	}

	public String getFormattedQueueNumber(String transtype, long runningValue) {
		String formattedString = "";
		String prefix = null;

		if (transtype != null && !transtype.isEmpty() && (transtype.equalsIgnoreCase(AMEND_BUS)
				|| transtype.isEmpty() && transtype.equalsIgnoreCase(AMEND_SERVICE)
				|| transtype.equalsIgnoreCase(AMEND_OWNER) || transtype.equalsIgnoreCase(AMEND_SERVICE_BUS)
				|| transtype.equalsIgnoreCase(AMEND_OWNER_BUS) || transtype.equalsIgnoreCase(AMMENDMENT_SERVICE)
				|| transtype.equalsIgnoreCase(AMMENDMENT_ROUTE) || transtype.equalsIgnoreCase(AMMENDMENT_END))) {
			transtype = AMMENDMENT;
		}

		if (transtype != null && !transtype.isEmpty() && transtype.equalsIgnoreCase(INSPECTION_AMMEND)) {
			transtype = INSPECTION;
		}

		if (transtype.equalsIgnoreCase(RENEWAL)) {// RENEWAL - 04
			prefix = "RN";
		} else if (transtype.equalsIgnoreCase(PERMIT)) {// NEW PERMIT - 03
			prefix = "NP";
		} else if (transtype.equalsIgnoreCase(INSPECTION)) {// INSPECTION - 02
			prefix = "IN";
		} else if (transtype.equalsIgnoreCase(CANCEL)) {// CANCEL - 07
			prefix = "CN";
		} else if (transtype.equalsIgnoreCase(PAYMENT)) {// PAYMENT - 06
			prefix = "PY";
		} else if (transtype.equalsIgnoreCase(AMMENDMENT)) {// Amendments - 05
			prefix = "AM";
		} else if (transtype.equalsIgnoreCase(INQUIRY)) {// Inquiry - 09
			prefix = "INQ";
		} else if (transtype.equalsIgnoreCase(TENDER)) {// tender - 01
			prefix = "TN";
		}

		runningValue++;
		formattedString = String.format("%04d", runningValue);

		formattedString = prefix + "" + formattedString;

		return formattedString;
	}

	@Override
	public String retrieveOwnerName(QueueNumberDTO queueNumberDTO) {
		logger.info("retrieveOwnerName start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String ownerName = null;
		String gender = null;
		String maritalStatus = null;

		try {
			con = ConnectionManager.getConnection();
			QueueNumberDTO dto = new QueueNumberDTO();
			if (queueNumberDTO != null && queueNumberDTO.getApplicationNo() != null
					&& !queueNumberDTO.getApplicationNo().isEmpty()
					&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
				dto.setApplicationNo(queueNumberDTO.getApplicationNo());
			} else {
				if (queueNumberDTO.getTransTypeCode() != null && !queueNumberDTO.getTransTypeCode().isEmpty()
						&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("")
						&& (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_BUS)
								|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE)
								|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER))) {

					/** get app no start **/
					String where = null;
					boolean whereFill = false;
					if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
							&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
						where = " pm_permit_no='" + queueNumberDTO.getPermitNo() + "'";
						whereFill = true;
					}

					if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
							&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
						if (whereFill) {
							where = where + " and pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
						} else {
							where = " pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
						}
					}

					String sql = "SELECT pm_application_no FROM public.nt_t_pm_application WHERE " + where;

					ps = con.prepareStatement(sql);
					rs = ps.executeQuery();

					while (rs.next()) {
						dto.setApplicationNo(rs.getString("pm_application_no"));
					}

					ConnectionManager.close(rs);
					ConnectionManager.close(ps);
					/** get app no end **/
				} else {
					dto = validateUserInputValues(queueNumberDTO);
				}
			}

			if (dto != null) {

				String sql = "SELECT pmo_full_name,pmo_gender,pmo_marital_status FROM public.nt_t_pm_vehi_owner WHERE pmo_application_no=?";

				ps = con.prepareStatement(sql);
				ps.setString(1, dto.getApplicationNo());
				;
				rs = ps.executeQuery();

				while (rs.next()) {
					ownerName = rs.getString("pmo_full_name");
					gender = rs.getString("pmo_gender");
					maritalStatus = rs.getString("pmo_marital_status");
				}

				if (ownerName != null && !ownerName.isEmpty() && !ownerName.equalsIgnoreCase("")) {

					if (gender != null && !gender.isEmpty() && !gender.trim().equalsIgnoreCase("")) {
						if (gender.equalsIgnoreCase("M")) {
							ownerName = "Mr. " + ownerName;

						} else if (gender.equalsIgnoreCase("F")) {
							if (maritalStatus != null && !maritalStatus.isEmpty()
									&& !maritalStatus.trim().equalsIgnoreCase("")) {
								if (maritalStatus.equalsIgnoreCase("M")) {
									ownerName = "Mrs. " + ownerName;

								} else {
									ownerName = "Miss. " + ownerName;
								}
							}
						}
					}

				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("retrieveOwnerName error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("retrieveOwnerName end");

		return ownerName;
	}

	@Override
	public QueueNumberDTO validateUserInputValues(QueueNumberDTO queueNumberDTO) {
		logger.info("validateUserInputValues start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		QueueNumberDTO dto = null;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String dateStr = formatter.format(today);

		try {
			con = ConnectionManager.getConnection();

			String whereClause = "";
			if (queueNumberDTO != null) {

				if (queueNumberDTO.getTransTypeCode() != null && !queueNumberDTO.getTransTypeCode().isEmpty()
						&& (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_BUS)
								|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE)
								|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER)
								|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(TENDER))) {// TODO check tender
					return null; // if type is amendments no need to check back log validation
				}

				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					whereClause = whereClause + " pm_application_no='" + queueNumberDTO.getApplicationNo() + "'";
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					if (whereClause != null && !whereClause.isEmpty() && !whereClause.trim().equalsIgnoreCase("")) {
						whereClause = whereClause + " and pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
					} else {
						whereClause = whereClause + " pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
					}
				}
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					if (whereClause != null && !whereClause.isEmpty() && !whereClause.trim().equalsIgnoreCase("")) {
						whereClause = whereClause + " and pm_permit_no='" + queueNumberDTO.getPermitNo() + "'";
					} else {
						whereClause = whereClause + " pm_permit_no='" + queueNumberDTO.getPermitNo() + "'";
					}
				}

			} else {
				return null;
			}

			String sql = "";

			if (queueNumberDTO.getTransTypeCode() != null && !queueNumberDTO.getTransTypeCode().isEmpty()
					&& queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INSPECTION_AMMEND)) {

				sql = "SELECT pm_application_no,pm_permit_no,pm_vehicle_regno,pm_status FROM public.nt_t_pm_application "
						+ "WHERE " + whereClause + " and pm_status != 'G' order by seq DESC limit 1";

			} else {
				sql = "SELECT pm_application_no,pm_permit_no,pm_vehicle_regno,pm_status FROM public.nt_t_pm_application WHERE pm_is_backlog_app='N' and "
						+ whereClause + " and to_char(pm_created_date,'yyyy-MM-dd')='" + dateStr
						+ "' and pm_status != 'G' order by seq DESC limit 1";
			}

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new QueueNumberDTO();
				dto.setApplicationNo(rs.getString("pm_application_no"));
				dto.setPermitNo(rs.getString("pm_permit_no"));
				dto.setVehicleNo(rs.getString("pm_vehicle_regno"));
				dto.setApplicationPmStatus(rs.getString("pm_status"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("validateUserInputValues error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("validateUserInputValues end");
		return dto;

	}

	@Override
	public boolean checkSelectedTransactionTypeValidForQueueNumber(QueueNumberDTO queueNumberDTO, String transType) {
		logger.info("checkSelectedTransactionTypeValidForQueueNumber start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String taskCode = null;
		String status = null;
		String moduleTaskCode = null;
		QueueNumberDTO dto = new QueueNumberDTO();
		try {
			con = ConnectionManager.getConnection();

			if (transType != null && !transType.isEmpty() && !transType.trim().equalsIgnoreCase("")
					&& (transType.trim().equalsIgnoreCase(RENEWAL)
							|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(PERMIT))) {
				dto = validateUserInputValuesForRNAndNP(queueNumberDTO);
			} else {
				dto = validateUserInputValues(queueNumberDTO);
			}

			if (dto != null && dto.getApplicationNo() != null && !dto.getApplicationNo().isEmpty()
					&& !dto.getApplicationNo().equalsIgnoreCase("")) {
			} else {
				return false;
			}

			String sql = "SELECT tsd_task_code,tsd_status FROM public.nt_t_task_det WHERE tsd_app_no=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, dto.getApplicationNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				taskCode = rs.getString("tsd_task_code");// can not be null in DB
				status = rs.getString("tsd_status");// can be null in DB
			}
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			// Should not need to validate start
			if (transType.trim().equalsIgnoreCase(CANCEL)) {
				// if cancel(07) do not need to validate since cancellation can be done in any
				// time of process
				/** validate duplicate generate cancellation token for same data start **/
				boolean valid = validateDuplicateQueueNoGenerate(queueNumberDTO);
				if (valid) {
					return true;
				} else {
					return false;
				}
				/** validate duplicate generate cancellation token for same data end **/

			}
			// Should not need to validate end
			int formerTransType = 0;
			if (transType != null && !transType.isEmpty() && !transType.trim().equalsIgnoreCase("")
					&& transType.equalsIgnoreCase(RENEWAL)) {
				formerTransType = 2; // if transaction type is 04/renewal should check whether inspection is done.
										// bcz for transaction it comes from inspection always
			} else if (transType != null && !transType.isEmpty() && !transType.trim().equalsIgnoreCase("")
					&& transType.equalsIgnoreCase(INSPECTION_AMMEND)) {
				formerTransType = -1;
				/**
				 * added by tharushi.e for owner change service change allowed only after
				 * generating response letter
				 */
				if (transType.equalsIgnoreCase(AMEND_BUS)) {
					moduleTaskCode = "AM100";

				} else {
					moduleTaskCode = "AM103";
				}
				/** end **/
			} else {
				formerTransType = Integer.valueOf(transType.trim()) - 1;
			}

			if (formerTransType != -1) {
				String formerTran = "0" + Integer.toString(formerTransType);
				// check if application number's last task is finish to get a queue number start
				String sql2 = "SELECT code FROM public.nt_r_task where module=? ORDER BY code DESC";
				ps = con.prepareStatement(sql2);
				ps.setString(1, formerTran);
				rs = ps.executeQuery();

				while (rs.next()) {
					moduleTaskCode = rs.getString("code");// can not be null in DB
					break;
				}
				// check if application number's last task is finish to get a queue number end
			}

			// validate last task code status start
			/**
			 * For tender should check any information in queue master(Since tender is the
			 * first should not have any data in DB) start
			 **/
			if (transType.trim().equalsIgnoreCase(TENDER)) {
				// tender = 01
				// TODO from tender table get tender ref no and check whether it is available
				// and check duration time is valid
				return true;
			}
			/**
			 * For tender should check any information in queue master(Since tender is the
			 * first should not have any data in DB) end
			 **/

			/** For payment should complete any task code validation check start **/
			if (transType.trim().equalsIgnoreCase(PAYMENT)) {
				// payment = 06
				if (status != null && !status.isEmpty() && status.trim().equalsIgnoreCase("C")) {
					return true;
				}
			}
			/** For payment should complete any task code validation check end **/
			if (transType.trim().equalsIgnoreCase(RENEWAL)) {
				if (moduleTaskCode != null && !moduleTaskCode.isEmpty() && !moduleTaskCode.trim().equalsIgnoreCase("")
						&& taskCode != null && !taskCode.isEmpty() && !taskCode.trim().equalsIgnoreCase("")) {
					if (status != null && !status.isEmpty()
							&& ((status.trim().equalsIgnoreCase("C") && moduleTaskCode.equalsIgnoreCase(taskCode))
									|| (status.trim().equalsIgnoreCase("O")
											&& moduleTaskCode.equalsIgnoreCase(RENEWAL_PREVIOUS_TASK)))) {
						// task code for application number should be equal to last task code in the
						// queue transaction type process
						return true;
					}
				}
			} else {
				if (moduleTaskCode != null && !moduleTaskCode.isEmpty() && !moduleTaskCode.trim().equalsIgnoreCase("")
						&& taskCode != null && !taskCode.isEmpty() && !taskCode.trim().equalsIgnoreCase("")) {
					if (status != null && !status.isEmpty() && status.trim().equalsIgnoreCase("C")
							&& moduleTaskCode.equalsIgnoreCase(taskCode)) {
						// task code for application number should be equal to last task code in the
						// queue transaction type process
						return true;
					}
				}
			}
			// validate last task code status end

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("checkSelectedTransactionTypeValidForQueueNumber error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("checkSelectedTransactionTypeValidForQueueNumber end");
		return false;
	}

	private static String getPropertyFileVale(String key) {

		String value = null;
		try {
			value = PropertyReader.getPropertyValue(key);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		return value;

	}

	@Override
	public boolean validateDuplicateQueueNoGenerate(QueueNumberDTO queueNumberDTO) {
		logger.info("validateDuplicateQueueNoGenerate start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		QueueNumberDTO dto = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {
			con = ConnectionManager.getConnection();

			String whereClause = "";

			if (queueNumberDTO != null) {

				/** Tender Transaction Type can generate queue many times start **/// TODO check tender should let
																					// generate tokens for same
																					// transaction type once after a
																					// round complete
				if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_BUS)
						|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE)
						|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER)
						|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE_BUS)
						|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER_BUS)) {
					return true;
				}
				/** Tender Transaction Type can generate queue many times end **/

				whereClause = " que_trn_type_code='" + queueNumberDTO.getTransTypeCode().trim() + "'";// transaction
																										// type can not
																										// be null in
																										// this point

				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					whereClause = whereClause + " and que_application_no='" + queueNumberDTO.getApplicationNo().trim()
							+ "'";
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {

					whereClause = whereClause + " and que_vehicle_no='" + queueNumberDTO.getVehicleNo().trim() + "'";

				}
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {

					whereClause = whereClause + " and que_permit_no='" + queueNumberDTO.getPermitNo().trim() + "'";

				}

			} else {
				return false;
			}

			String sql = "SELECT que_application_no,que_permit_no,que_vehicle_no FROM public.nt_m_queue_master WHERE "
					+ whereClause + " and que_date like '" + today + "%'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new QueueNumberDTO();
				dto.setApplicationNo(rs.getString("que_application_no"));
				dto.setPermitNo(rs.getString("que_permit_no"));
				dto.setVehicleNo(rs.getString("que_vehicle_no"));
			}

			if (dto != null) {
				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					if (queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase(dto.getApplicationNo().trim())) {
						return false;
					}
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					if (queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase(dto.getVehicleNo().trim())) {
						return false;
					}
				}
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					if (queueNumberDTO.getPermitNo().trim().equalsIgnoreCase(dto.getPermitNo().trim())) {
						return false;
					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("validateDuplicateQueueNoGenerate error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("validateDuplicateQueueNoGenerate end");
		return true;

	}

	@Override
	public String callNextQueueNumberAction(String transactionType, String previousTransType) {
		logger.info("callNextQueueNumberAction start");
		// transactionType - 02-INSPECTION,03-NEW
		// PERMIT,04-RENEWAL,05-AMMENDMENT,06-PAYMENT,07-CANCEL,08-SURVEY,01-TENDER,09-INQUIRY

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String queueNumber = null;
		String tempTransType = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		String where = "";
		String table = "";
		String staticWhere = "IS null";
		String tenderWhere = "";

		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		try {
			con = ConnectionManager.getConnection();

			/**
			 * Check if transaction type is tender if so need to get ongoing as well bcz
			 * have to take second queue as well start
			 **/
			if (transactionType.equalsIgnoreCase(TENDER)) {
				staticWhere = staticWhere + " or task_status='O'";
				tenderWhere = " or (que_task_code='PM302' and que_task_status='C') ";
			}
			/**
			 * Check if transaction type is tender if so need to get ongoing as well bcz
			 * have to take second queue as well end
			 **/

			/**
			 * check transaction type for new permit and renewal to fill where and table
			 * start
			 **/
			if (transactionType.equalsIgnoreCase(PERMIT)) {
				where = " and pm_permit_no is null and pm_vehicle_regno=que_vehicle_no ";
				table = ", public.nt_t_pm_application";
			}
			if (transactionType.equalsIgnoreCase(RENEWAL)) {
				where = " and pm_permit_no is not null and pm_vehicle_regno=que_vehicle_no ";
				table = ", public.nt_t_pm_application";
			}
			/**
			 * check transaction type for new permit and renewal to fill where and table end
			 **/
			// since amendments got three types but goes to the same counter make skip
			// transaction parameter common start
			tempTransType = transactionType.substring(0, 2);
			if (tempTransType != null && !tempTransType.isEmpty()
					&& (tempTransType.equalsIgnoreCase(AMEND_BUS) || tempTransType.equalsIgnoreCase(AMEND_SERVICE)
							|| tempTransType.equalsIgnoreCase(AMEND_OWNER)
							|| tempTransType.equalsIgnoreCase(AMEND_OWNER_BUS)
							|| tempTransType.equalsIgnoreCase(AMEND_SERVICE_BUS)
							|| tempTransType.equalsIgnoreCase(AMMENDMENT_SERVICE)
							|| tempTransType.equalsIgnoreCase(AMMENDMENT_ROUTE)
							|| tempTransType.equalsIgnoreCase(AMMENDMENT_END))) {
				tempTransType = AMMENDMENT;
			}
			// since amendments got three types but goes to the same counter make skip
			// transaction parameter common end

			/** get skip number from skipped queue number list start **/
			String paramName = retrieveParameterValuesForType(con, tempTransType + "S");
			ParamerDTO paramDto = new ParamerDTO();
			paramDto = migratedService.retrieveParameterValuesForParamName(paramName); // skip number count to get a
																						// queue number from skip
																						// number list
			ParamerDTO paramDto1 = new ParamerDTO();
			paramDto1 = migratedService.retrieveParameterValuesForParamName("CALL_SKIP_QUEUE_NUMBER_VALUE");// after
																											// how
																											// many
																											// queue
																											// numbers
																											// a
																											// skipped
																											// queue
																											// number
																											// should
																											// come

			String paramNameForRunVal = retrieveParameterValuesForType(con, tempTransType);
			ParamerDTO paramDto3 = new ParamerDTO();
			paramDto3 = migratedService.retrieveParameterValuesForParamName(paramNameForRunVal);

			/** trans type convert start **/
			String temp = "";
			if (transactionType.length() == 2) {
				transactionType = "'" + transactionType + "'";
			} else if (transactionType.length() > 2) {
				List<String> items = Arrays.asList(transactionType.split("\\s*,\\s*"));

				for (String s : items) {
					temp += "'" + s + "'" + ",";

				}

				if (temp != null && temp.length() > 0 && temp.charAt(temp.length() - 1) == ',') {
					String trnTypes = temp.substring(0, temp.length() - 1);
					transactionType = trnTypes;
				}

			}
			/** trans type convert end **/

			// get queue number from skipped queue number list start
			if (paramDto.getIntValue() == paramDto1.getIntValue()) {
				queueNumber = selectSkippedQueueNumberFromSkipList(con, transactionType, previousTransType, paramName,
						paramDto3.getIntValue());

				if (queueNumber != null && !queueNumber.isEmpty() && !queueNumber.trim().equalsIgnoreCase("")) {
					ParamerDTO changedParamDto = new ParamerDTO();
					changedParamDto.setIntValue(0);
					migratedService.updateNT_R_NT_R_PARAMETERS(changedParamDto, paramName);// update run value for
																							// paramName
					con.commit();

					return queueNumber;
				}
			}
			// get queue number from skipped queue number list end

			String sql = null;
			if (transactionType.equalsIgnoreCase(INQUIRY)) {
				sql = "select que_number,que_trn_type_code,que_date,que_curr_workflow_id from public.nt_m_queue_master where "
						+ "que_skip_count IS null and task_status IS null and que_trn_type_code=? "
						+ "and que_date like '" + today + "%' order by que_order asc";

				ps = con.prepareStatement(sql);
				ps.setString(1, transactionType);

			} else {

				sql = "select que_number,que_trn_type_code,que_date,que_curr_workflow_id from public.nt_m_queue_master "
						+ table + " where " + "(que_skip_count IS null or que_skip_count > '0') and (((task_status " + staticWhere
						+ ") and que_trn_type_code in(" + transactionType
						+ ")) or (que_trn_type_code=? and task_status='C' " + where + ") " + tenderWhere + ") "
						+ "and que_date like '" + today + "%' order by que_order asc";

				ps = con.prepareStatement(sql);
				if (previousTransType != null && !previousTransType.isEmpty()
						&& !previousTransType.trim().equalsIgnoreCase("")) {
					ps.setString(1, previousTransType);
				} else {
					ps.setString(1, "");
				}

			}

			rs = ps.executeQuery();

			while (rs.next()) {
				queueNumber = rs.getString("que_number");
				break;
			}

			if (queueNumber != null && !queueNumber.isEmpty() && !queueNumber.trim().equalsIgnoreCase("")) {

				ParamerDTO changedParamDto = new ParamerDTO();
				if (paramDto.getIntValue() == 0 || paramDto.getIntValue() < paramDto1.getIntValue()) {
					changedParamDto.setIntValue(paramDto.getIntValue() + 1);
				} else {
					changedParamDto.setIntValue(0);
				}
				// increase skipped queue number get running value

				migratedService.updateNT_R_NT_R_PARAMETERS(changedParamDto, paramName);// update run value for
																						// paramName
				con.commit();
			} else {
				// if there are no queue numbers check on skip list and give skipped queue
				// number
				queueNumber = selectSkippedQueueNumberFromSkipList(con, transactionType, previousTransType, paramName,
						paramDto3.getIntValue());

			}

		} catch (SQLException e) {
			logger.info("callNextQueueNumberAction error: " + e.toString());
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("callNextQueueNumberAction end");
		return queueNumber;
	}

	@Override
	public void createQueuNumberOrder(String queueTypeCode, String loginUser) {
		logger.info("createQueuNumberOrder start - Queue Type Code: " + queueTypeCode);

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		List<QueueNumberDTO> queueNoList = new ArrayList<QueueNumberDTO>();
		List<QueueNumberDTO> priorityList = new ArrayList<QueueNumberDTO>();
		List<QueueNumberDTO> normalList = new ArrayList<QueueNumberDTO>();
		List<QueueNumberDTO> orderedList = new ArrayList<QueueNumberDTO>();

		int normal = 0;
		int priority = 0;
		boolean orderSetPriority = false;
		boolean orderSetNormal = false;

		boolean updateRunningValues = false;

		try {

			con = ConnectionManager.getConnection();

			String runningValParamName = retrieveParameterValuesForType(con, queueTypeCode);
			commonService = (CommonService) SpringApplicationContex.getBean("commonService");
			migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");

			String sql = "select que_number,que_service_type from public.nt_m_queue_master where task_status is null and que_date like '"
					+ today + "%' " + "and que_trn_type_code='" + queueTypeCode
					+ "' and que_skip_count is null and (que_order_set NOT IN('Y') or que_order_set is null) order by que_seq";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				QueueNumberDTO dto = new QueueNumberDTO();
				dto.setQueueNumber(rs.getString("que_number"));
				dto.setQueueService(rs.getString("que_service_type"));
				queueNoList.add(dto);// 1.get the list
			}

			// 2.make priority and normal lists
			for (QueueNumberDTO dto : queueNoList) {
				if (dto.getQueueService().equalsIgnoreCase("N")) {
					normalList.add(dto);
				} else {
					priorityList.add(dto);
				}
			}

			// 3.order list
			// select how many priority numbers to display start
			ParamerDTO paramDTO1 = migratedService.retrieveParameterValuesForParamName("PRIORITY_QUEUE_NUMBER_VALUE");
			int priorityQueueNoVal = paramDTO1.getIntValue();
			// select how many priority numbers to display end

			// get run value start
			ParamerDTO paramDTO2 = migratedService.retrieveParameterValuesForParamName(runningValParamName);
			int runVaue = paramDTO2.getIntValue();
			// get run value end

			for (QueueNumberDTO dto : queueNoList) {
				if (runVaue == 0) {
					// normal
					if (normal <= (normalList.size() - 1)) {
						if (orderSetPriority) {
							normalList.get(normal).setOrderSet("N");
							updateRunningValues = false;
						} else {
							normalList.get(normal).setOrderSet("Y");
							updateRunningValues = true;
						}

						orderedList.add(normalList.get(normal));
						normal++;
					} else {
						if (priority <= (priorityList.size() - 1)) {
							priorityList.get(priority).setOrderSet("N");
							orderedList.add(priorityList.get(priority));
							orderSetNormal = true;
							updateRunningValues = false;
							priority++;
						}
					}

				} else {
					// priority
					if (priority <= (priorityList.size() - 1)) {
						if (orderSetNormal) {
							priorityList.get(priority).setOrderSet("N");
							updateRunningValues = false;
						} else {
							priorityList.get(priority).setOrderSet("Y");
							updateRunningValues = true;
						}

						orderedList.add(priorityList.get(priority));
						priority++;
					} else {
						if (normal <= (normalList.size() - 1)) {
							normalList.get(normal).setOrderSet("N");
							orderedList.add(normalList.get(normal));
							orderSetPriority = true;
							updateRunningValues = false;
							normal++;
						}
					}

				}

				if (runVaue == 0 || runVaue < priorityQueueNoVal) {
					runVaue++;
				} else if (runVaue == priorityQueueNoVal) {
					runVaue = 0;
				}

				// 4.update run value
				if (updateRunningValues) {
					ParamerDTO paramDto = new ParamerDTO();
					paramDto.setIntValue(runVaue);
					migratedService.updateNT_R_NT_R_PARAMETERS(paramDto, runningValParamName);// update run value for
																								// paramName
				}
			}

			// 5.update nt_m_queue_master que_order and que_order_set
			for (QueueNumberDTO dto : orderedList) {

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_queue_number_order");
				String sqlupdate = "UPDATE public.nt_m_queue_master "
						+ "SET que_order=?, que_order_set=?, modified_by=?, modified_date=? " + "WHERE que_date like '"
						+ today + "%' AND que_number=?";

				ps = con.prepareStatement(sqlupdate);

				ps.setString(1, Long.toString(seqNo));
				ps.setString(2, dto.getOrderSet());
				ps.setString(3, loginUser);
				ps.setTimestamp(4, timestamp);
				ps.setString(5, dto.getQueueNumber());

				ps.executeUpdate();
			}
			con.commit();

		} catch (SQLException e) {
			logger.info("createQueuNumberOrder error: " + e.toString());
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("createQueuNumberOrder end");
	}

	public String retrieveParameterValuesForType(Connection con, String type) {
		logger.info("retrieveParameterValuesForParamName start");

		PreparedStatement ps = null;
		ResultSet rs = null;
		String paramName = null;

		if (type != null && !type.isEmpty() && !type.trim().equalsIgnoreCase("")
				&& (type.equalsIgnoreCase(AMEND_BUS) || type.equalsIgnoreCase(AMEND_SERVICE)
						|| type.equalsIgnoreCase(AMEND_OWNER) || type.equalsIgnoreCase(AMEND_OWNER_BUS)
						|| type.equalsIgnoreCase(AMEND_SERVICE_BUS) || type.equalsIgnoreCase(AMMENDMENT_SERVICE)
						|| type.equalsIgnoreCase(AMMENDMENT_ROUTE) || type.equalsIgnoreCase(AMMENDMENT_END))) {

			type = AMMENDMENT;
		}

		if (type != null && !type.isEmpty() && type.equalsIgnoreCase(INSPECTION_AMMEND)) {
			type = INSPECTION;
		}

		if (type != null && !type.isEmpty()
				&& (type.equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN) || type.equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
						|| type.equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT))) {
			type = INSPECTION;
		}

		try {
			String sql1 = "SELECT param_name FROM public.nt_r_parameters WHERE type=?";
			ps = con.prepareStatement(sql1);

			ps.setString(1, type);
			rs = ps.executeQuery();

			while (rs.next()) {
				paramName = rs.getString("param_name");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		logger.info("retrieveParameterValuesForParamName end");
		return paramName;
	}

	public String selectSkippedQueueNumberFromSkipList(Connection con, String transactionType, String previousTransType,
			String paramName, int runValue) {
		logger.info("selectSkippedQueueNumberFromSkipList start");

		PreparedStatement ps = null;
		ResultSet rs = null;
		String queueNumber = null;

		List<QueueNumberDTO> queueNoList = new ArrayList<QueueNumberDTO>();
		List<QueueNumberDTO> priorityList = new ArrayList<QueueNumberDTO>();
		List<QueueNumberDTO> normalList = new ArrayList<QueueNumberDTO>();

		boolean normal = false;
		boolean priority = false;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		String where = "";
		String table = "";
		int skipCount = 0;
		String tenderWhere = "";
		String inspectionWhere = "";
		String renewalDistinct = "";
		try {

			/** get skip queue number count from parameter table start **/
			ParamerDTO paramDto = migratedService.retrieveParameterValuesForParamName("QUEUE_MASTER_SKIP_COUNT");
			skipCount = paramDto.getIntValue();
			/** get skip queue number count from parameter table end **/

			/**
			 * Check if transaction type is tender if so need to get ongoing as well bcz
			 * have to take second queue as well start
			 **/
			if (transactionType.contains(TENDER)) {
				tenderWhere = " or (que_task_code='PM302' and que_task_status='C') ";
			}
			/**
			 * Check if transaction type is tender if so need to get ongoing as well bcz
			 * have to take second queue as well end
			 **/

			/**
			 * check transaction type for new permit and renewal to fill where and table
			 * start
			 **/
			if (transactionType.contains(PERMIT)) {
				where = " and pm_permit_no is null and pm_vehicle_regno=que_vehicle_no ";
				table = ", public.nt_t_pm_application";
			}
			if (transactionType.contains(RENEWAL)) {
				where = " or (task_status='O' and que_task_code='PM101' and que_task_status='C') and pm_permit_no is not null and pm_vehicle_regno=que_vehicle_no ";
				table = ", public.nt_t_pm_application";
				renewalDistinct = " distinct que_order, ";
			}
			/**
			 * check transaction type for new permit and renewal to fill where and table end
			 **/

			/** inspection start **/
			if (transactionType.contains(INSPECTION)) {
				inspectionWhere = " and (que_task_status not in('C') or que_task_status is null) ";
			}
			/** inspection end **/

			String sql = "select " + renewalDistinct
					+ " que_number,que_service_type,que_skip_count from public.nt_m_queue_master " + table + " "
					+ "where que_skip_count < '" + Integer.toString(skipCount)
					+ "' and ((task_status not in('C') and que_trn_type_code in(" + transactionType + ") "
					+ inspectionWhere + ") or (que_trn_type_code=? and task_status='C' " + where + ") " + tenderWhere
					+ ")  " + " and que_date like '" + today + "%' order by que_order";

			ps = con.prepareStatement(sql);
			ps.setString(1, previousTransType);

			rs = ps.executeQuery();

			while (rs.next()) {
				QueueNumberDTO dto = new QueueNumberDTO();
				dto.setQueueNumber(rs.getString("que_number"));
				dto.setQueueService(rs.getString("que_service_type"));
				dto.setSkipCount(Integer.parseInt(rs.getString("que_skip_count")));
				queueNoList.add(dto);// 1.get the list
			}

			if (queueNoList != null && queueNoList.size() != 0) {

				for (QueueNumberDTO dto : queueNoList) {
					if (dto.getQueueService().equalsIgnoreCase("N")) {
						normalList.add(dto);
					} else {
						priorityList.add(dto);
					}
				}

				/**
				 * check skipped count and add already skipped numbers into end of list start
				 **/
				int greatestSkipCountNormal = 0;
				boolean skipCountGetNor = false;
				for (QueueNumberDTO dto : normalList) {

					if (!skipCountGetNor) {
						greatestSkipCountNormal = dto.getSkipCount();
						skipCountGetNor = true;
					}

					if (dto.getSkipCount() > greatestSkipCountNormal) {
						greatestSkipCountNormal = dto.getSkipCount();
					}
				}

				int greatestSkipCountPriority = 0;
				boolean skipCountGetPrio = false;
				for (QueueNumberDTO dto : priorityList) {

					if (!skipCountGetPrio) {
						greatestSkipCountPriority = dto.getSkipCount();
						skipCountGetPrio = true;
					}

					if (dto.getSkipCount() > greatestSkipCountPriority) {
						greatestSkipCountPriority = dto.getSkipCount();
					}
				}

				/** check skipped count and add already skipped numbers into end of list end **/

				if (runValue == 0) {
					// normal
					if (normalList != null && normalList.size() != 0) {
						queueNumber = normalList.get(0).getQueueNumber();// 0->norCount++ //06/11/2018

						for (QueueNumberDTO dto : normalList) {
							if (dto.getSkipCount() < greatestSkipCountNormal) {
								queueNumber = dto.getQueueNumber();
								break;
							}
						}

					} else {
						if (priorityList != null && priorityList.size() != 0) {
							queueNumber = priorityList.get(0).getQueueNumber();// 0->proCount++ //06/11/2018

							for (QueueNumberDTO dto : priorityList) {
								if (dto.getSkipCount() < greatestSkipCountPriority) {
									queueNumber = dto.getQueueNumber();
									break;
								}
							}
						}
					}
					normal = true;

				} else {
					// priority
					if (priorityList != null && priorityList.size() != 0) {
						queueNumber = priorityList.get(0).getQueueNumber();// 0->proCount++ //06/11/2018

						for (QueueNumberDTO dto : priorityList) {
							if (dto.getSkipCount() < greatestSkipCountPriority) {
								queueNumber = dto.getQueueNumber();
								break;
							}
						}
					} else {
						if (normalList != null && normalList.size() != 0) {
							queueNumber = normalList.get(0).getQueueNumber();// 0->norCount++ //06/11/2018

							for (QueueNumberDTO dto : normalList) {
								if (dto.getSkipCount() < greatestSkipCountNormal) {
									queueNumber = dto.getQueueNumber();
									break;
								}
							}
						}
					}
					priority = true;
				}

				if (queueNumber != null && !queueNumber.isEmpty() && !queueNumber.trim().equalsIgnoreCase("")) {
					ParamerDTO changedParamDto = new ParamerDTO();
					changedParamDto.setIntValue(0);
					migratedService.updateNT_R_NT_R_PARAMETERS(changedParamDto, paramName);// update run value for
																							// paramName
					con.commit();
				} else {
					if (normal) {
						if (normalList != null && normalList.size() != 0) {
							queueNumber = normalList.get(0).getQueueNumber();// 0->norCount++ //06/11/2018

							for (QueueNumberDTO dto : normalList) {
								if (dto.getSkipCount() < greatestSkipCountNormal) {
									queueNumber = dto.getQueueNumber();
									break;
								}
							}
						} else {
							if (priorityList != null && priorityList.size() != 0) {
								queueNumber = priorityList.get(0).getQueueNumber();// 0->priCount++ //06/11/2018

								for (QueueNumberDTO dto : priorityList) {
									if (dto.getSkipCount() < greatestSkipCountPriority) {
										queueNumber = dto.getQueueNumber();
										break;
									}
								}
							}
						}
					}
					if (priority) {
						if (priorityList != null && priorityList.size() != 0) {
							queueNumber = priorityList.get(0).getQueueNumber();// 0->priCount++ //06/11/2018

							for (QueueNumberDTO dto : priorityList) {
								if (dto.getSkipCount() < greatestSkipCountPriority) {
									queueNumber = dto.getQueueNumber();
									break;
								}
							}
						} else {
							if (normalList != null && normalList.size() != 0) {
								queueNumber = normalList.get(0).getQueueNumber();// 0->norCount++ //06/11/2018
								for (QueueNumberDTO dto : normalList) {
									if (dto.getSkipCount() < greatestSkipCountNormal) {
										queueNumber = dto.getQueueNumber();
										break;
									}
								}
							}
						}
					}
				}

			}

		} catch (SQLException e) {
			logger.info("selectSkippedQueueNumberFromSkipList error: " + e.toString());
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}
		logger.info("selectSkippedQueueNumberFromSkipList end");

		return queueNumber;
	}

	@Override
	public boolean validateApplicationNoIsBackLog(String applicationNo) {
		logger.info("validateApplicationNoIsBackLog start");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String isBackLogApp = null;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT pm_is_backlog_app FROM public.nt_t_pm_application WHERE pm_application_no=? ";
			ps = con.prepareStatement(sql1);

			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				isBackLogApp = rs.getString("pm_is_backlog_app");
			}

			if (isBackLogApp != null && !isBackLogApp.isEmpty() && !isBackLogApp.trim().equalsIgnoreCase("")) {
				if (isBackLogApp.equalsIgnoreCase("Y")) {
					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("validateApplicationNoIsBackLog end");
		return false;
	}

	@Override
	public boolean validateDuplicateQueueNoGenerateInQueuMaster(QueueNumberDTO queueNumberDTO) {
		logger.info("validateDuplicateQueueNoGenerate start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		QueueNumberDTO dto = null;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {
			con = ConnectionManager.getConnection();

			String whereClause = "";

			if (queueNumberDTO != null) {

				whereClause = " que_trn_type_code='" + queueNumberDTO.getTransTypeCode().trim() + "'";// transaction
																										// type can not
																										// be null in
																										// this point

				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					whereClause = whereClause + " and que_application_no='" + queueNumberDTO.getApplicationNo().trim()
							+ "'";
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {

					whereClause = whereClause + " and que_vehicle_no='" + queueNumberDTO.getVehicleNo().trim() + "'";

				}
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {

					whereClause = whereClause + " and que_permit_no='" + queueNumberDTO.getPermitNo().trim() + "'";

				}

			} else {
				return false;
			}

			String sql = "SELECT que_application_no,que_permit_no,que_vehicle_no FROM public.nt_m_queue_master WHERE "
					+ whereClause + " and que_date like '" + today + "%'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new QueueNumberDTO();
				dto.setApplicationNo(rs.getString("que_application_no"));
				dto.setPermitNo(rs.getString("que_permit_no"));
				dto.setVehicleNo(rs.getString("que_vehicle_no"));

				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("validateDuplicateQueueNoGenerate error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("validateDuplicateQueueNoGenerate end");
		return false;

	}

	public boolean validateTokenforSameDay(String vehicleNo) {
		logger.info("validateTokenforSameDay start");

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String isExist = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today = dateFormat.format(timestamp);

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "select tsd_task_code " + " from nt_t_task_det " + " where tsd_vehicle_no = ? "
					+ " and tsd_task_code in ('PM100','PM101','PM200','PR200','PM201','PM300','PM301','PM400')"
					+ " and to_char(created_date,'dd-MM-yyyy')= '" + today + "'";
			ps = con.prepareStatement(sql1);

			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				isExist = rs.getString("tsd_task_code");
			}

			if (isExist != null && !isExist.isEmpty() && !isExist.trim().equalsIgnoreCase("")) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("validateTokenforSameDay end");
		return false;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isNormalCheck() {
		return normalCheck;
	}

	public void setNormalCheck(boolean normalCheck) {
		this.normalCheck = normalCheck;
	}

	public boolean isPriorityCheck() {
		return priorityCheck;
	}

	public void setPriorityCheck(boolean priorityCheck) {
		this.priorityCheck = priorityCheck;
	}

	@Override
	public QueueNumberDTO validateUserInputValuesForRNAndNP(QueueNumberDTO queueNumberDTO) {
		logger.info("validateUserInputValuesForRenewal start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		QueueNumberDTO dto = null;

		try {
			con = ConnectionManager.getConnection();

			String whereClause = "";
			if (queueNumberDTO != null) {

				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					whereClause = whereClause + " pm_application_no='" + queueNumberDTO.getApplicationNo() + "'";
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					if (whereClause != null && !whereClause.isEmpty() && !whereClause.trim().equalsIgnoreCase("")) {
						whereClause = whereClause + " and pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
					} else {
						whereClause = whereClause + " pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
					}
				}
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					if (whereClause != null && !whereClause.isEmpty() && !whereClause.trim().equalsIgnoreCase("")) {
						whereClause = whereClause + " and pm_permit_no='" + queueNumberDTO.getPermitNo() + "'";
					} else {
						whereClause = whereClause + " pm_permit_no='" + queueNumberDTO.getPermitNo() + "'";
					}
				}

			} else {
				return null;
			}

			if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(RENEWAL)) {
				whereClause = whereClause + " and pm_status ='O' and pm_permit_no is not null";
			} else if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(PERMIT)) {
				whereClause = whereClause + " and pm_status ='O' and pm_permit_no is null";
			}

			String sql = "SELECT pm_application_no,pm_permit_no,pm_vehicle_regno,pm_status FROM public.nt_t_pm_application "
					+ "WHERE " + whereClause;

			// P.S- if pm_status in('I','R') should not let proceed this is check in backing
			// bean <- QueueManagementBackingBean - validateBeforeGenerate()

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new QueueNumberDTO();
				dto.setApplicationNo(rs.getString("pm_application_no"));
				dto.setPermitNo(rs.getString("pm_permit_no"));
				dto.setVehicleNo(rs.getString("pm_vehicle_regno"));
				dto.setApplicationPmStatus(rs.getString("pm_status"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("validateUserInputValuesForRenewal error: " + e.toString());
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("validateUserInputValuesForRenewal end");
		return dto;
	}

	@Override
	public List<String> callNextQueueNumbersForDisplayAction(String transactionType, String previousTransType) {
		logger.info("callNextQueueNumbersForDisplayAction start");
		// transactionType - 02-INSPECTION,03-NEW
		// PERMIT,04-RENEWAL,05-AMMENDMENT,06-PAYMENT,07-CANCEL,08-SURVEY,01-TENDER,09-INQUIRY

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String queueNumber = null;
		List<String> queueNoList = new ArrayList<String>();

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		String where = "";
		String table = "";
		String staticWhere = "IS null";
		String tenderWhere = "";

		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		try {
			con = ConnectionManager.getConnection();

			/**
			 * Check if transaction type is tender if so need to get ongoing as well bcz
			 * have to take second queue as well start
			 **/
			if (transactionType.equalsIgnoreCase(TENDER)) {
				staticWhere = staticWhere + " or task_status='O'";
				tenderWhere = " or (que_task_code='PM302' and que_task_status='C') ";
			}
			/**
			 * Check if transaction type is tender if so need to get ongoing as well bcz
			 * have to take second queue as well end
			 **/

			/**
			 * check transaction type for new permit and renewal to fill where and table
			 * start
			 **/
			if (transactionType.equalsIgnoreCase(PERMIT)) {
				where = " and pm_permit_no is null and pm_vehicle_regno=que_vehicle_no ";
				table = ", public.nt_t_pm_application";
			}
			if (transactionType.equalsIgnoreCase(RENEWAL)) {
				where = " and pm_permit_no is not null and pm_vehicle_regno=que_vehicle_no ";
				table = ", public.nt_t_pm_application";
			}
			/**
			 * check transaction type for new permit and renewal to fill where and table end
			 **/

			String sql = null;
			if (transactionType.equalsIgnoreCase(INQUIRY)) {
				sql = "select que_number,que_trn_type_code,que_date,que_curr_workflow_id from public.nt_m_queue_master where "
						+ "que_skip_count IS null and task_status IS null and que_trn_type_code=? "
						+ "and que_date like '" + today + "%' order by que_order asc LIMIT 5";

				ps = con.prepareStatement(sql);
				ps.setString(1, transactionType);

			} else {
				sql = "select distinct que_order,que_number,que_trn_type_code,que_date,que_curr_workflow_id from public.nt_m_queue_master "
						+ table + " where " + "que_skip_count IS null and (((task_status " + staticWhere
						+ ") and que_trn_type_code=?) or (que_trn_type_code=? and task_status='C' " + where + ") "
						+ tenderWhere + ") " + " and que_date like '" + today + "%' order by que_order asc LIMIT 5";

				ps = con.prepareStatement(sql);
				ps.setString(1, transactionType);
				ps.setString(2, previousTransType);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				queueNumber = rs.getString("que_number");
				queueNoList.add(queueNumber);
			}

		} catch (SQLException e) {
			logger.info("callNextQueueNumbersForDisplayAction error: " + e.toString());
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("callNextQueueNumbersForDisplayAction end");
		return queueNoList;
	}

	@Override
	public List<String> skippedQueueNumberForDisplay(Connection con, String transactionType, String previousTransType) {
		// if run value is 0 should give a normal skip number else should give a pr

		logger.info("selectSkippedQueueNumberFromSkipList start");

		PreparedStatement ps = null;
		ResultSet rs = null;
		String queueNumberNormal = null;
		String queueNumberPriority = null;
		List<String> skippedDisplayList = new ArrayList<String>();

		List<QueueNumberDTO> queueNoList = new ArrayList<QueueNumberDTO>();
		List<QueueNumberDTO> priorityList = new ArrayList<QueueNumberDTO>();
		List<QueueNumberDTO> normalList = new ArrayList<QueueNumberDTO>();

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		int skipCount = 0;
		try {

			/** get skip queue number count from parameter table start **/
			ParamerDTO paramDto = migratedService.retrieveParameterValuesForParamName("QUEUE_MASTER_SKIP_COUNT");
			skipCount = paramDto.getIntValue();
			/** get skip queue number count from parameter table end **/

			String sql = "";
			if (transactionType.equalsIgnoreCase(INSPECTION)) {

				sql = "select distinct que_number,que_trn_type_code,que_service_type,que_skip_count,que_vehicle_no,que_task_code, que_task_status \r\n"
						+ "from public.nt_m_queue_master,nt_r_counter \r\n" + "where que_skip_count < '"
						+ Integer.toString(skipCount) + "' and que_skip_count is not null \r\n" + "and que_date like '"
						+ today + "%' and ((que_task_code is null and que_task_status is null)\r\n"
						+ "or (que_task_code='PM101' and que_task_status='O'))\r\n"
						+ "and que_number not in(select cou_serving_queueno from nt_r_counter where cou_status='A' and cou_serving_queueno is not null )";

			}

			if (transactionType.equalsIgnoreCase(PERMIT)) {

				sql = "select A.que_number,A.que_vehicle_no,que_application_no\r\n"
						+ "from public.nt_m_queue_master A \r\n"
						+ "inner join public.nt_t_pm_application B on A.que_application_no = B.pm_application_no\r\n"
						+ "where A.que_date like '" + today + "%' and B.pm_permit_no is null and A.que_skip_count < '"
						+ Integer.toString(skipCount) + "' ";
			}

			if (transactionType.equalsIgnoreCase(RENEWAL)) {

				sql = "select distinct que_number,que_trn_type_code,que_service_type,que_skip_count,que_vehicle_no,que_task_code, que_task_status  \r\n"
						+ "from public.nt_m_queue_master, nt_t_pm_application, nt_r_counter \r\n"
						+ "where que_skip_count < '" + Integer.toString(skipCount)
						+ "' and que_skip_count is not null and que_date like '" + today + "%' \r\n"
						+ "and ((((que_task_code='PM101' and que_task_status='C') and pm_permit_no is not null) or (que_trn_type_code='04' and que_task_code is null and que_task_status is null))\r\n"
						+ " and que_number not in(select cou_serving_queueno from nt_r_counter where cou_status='A' and cou_serving_queueno is not null))\r\n"
						+ "and que_number=pm_queue_no ";

			}

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				QueueNumberDTO dto = new QueueNumberDTO();
				dto.setQueueNumber(rs.getString("que_number"));
				dto.setQueueService(rs.getString("que_service" + "_type"));
				dto.setSkipCount(Integer.parseInt(rs.getString("que_skip_count")));
				queueNoList.add(dto);// 1.get the list
			}

			if (queueNoList != null && queueNoList.size() != 0) {

				for (QueueNumberDTO dto : queueNoList) {
					if (dto.getQueueService().equalsIgnoreCase("N")) {
						normalList.add(dto);
					} else {
						priorityList.add(dto);
					}
				}

				/**
				 * check skipped count and add already skipped numbers into end of list start
				 **/
				int greatestSkipCountNormal = 0;
				boolean skipCountGetNor = false;
				for (QueueNumberDTO dto : normalList) {

					if (!skipCountGetNor) {
						greatestSkipCountNormal = dto.getSkipCount();
						skipCountGetNor = true;
					}

					if (dto.getSkipCount() > greatestSkipCountNormal) {
						greatestSkipCountNormal = dto.getSkipCount();
					}
				}

				int greatestSkipCountPriority = 0;
				boolean skipCountGetPrio = false;
				for (QueueNumberDTO dto : priorityList) {

					if (!skipCountGetPrio) {
						greatestSkipCountPriority = dto.getSkipCount();
						skipCountGetPrio = true;
					}

					if (dto.getSkipCount() > greatestSkipCountPriority) {
						greatestSkipCountPriority = dto.getSkipCount();
					}
				}
				/** check skipped count and add already skipped numbers into end of list end **/

				// ################################################ normal number start
				// ########################################//
				// normal
				if (normalList != null && normalList.size() != 0) {
					queueNumberNormal = normalList.get(0).getQueueNumber();// 0->norCount++ //06/11/2018

					for (QueueNumberDTO dto : normalList) {
						if (dto.getSkipCount() < greatestSkipCountNormal) {
							queueNumberNormal = dto.getQueueNumber();
							break;
						}
					}

				} else {
					if (priorityList != null && priorityList.size() != 0) {
						queueNumberPriority = priorityList.get(0).getQueueNumber();// 0->proCount++ //06/11/2018

						for (QueueNumberDTO dto : priorityList) {
							if (dto.getSkipCount() < greatestSkipCountPriority) {
								queueNumberPriority = dto.getQueueNumber();
								break;
							}
						}
					}
				}
				// ################################################ normal number end
				// ########################################//

				// ################################################ priority number start
				// ########################################//
				// priority
				if (priorityList != null && priorityList.size() != 0) {
					queueNumberPriority = priorityList.get(0).getQueueNumber();// 0->proCount++

					for (QueueNumberDTO dto : priorityList) {
						if (dto.getSkipCount() < greatestSkipCountPriority) {
							queueNumberPriority = dto.getQueueNumber();
							break;
						}
					}
				} else {
					if (normalList != null && normalList.size() != 0) {
						queueNumberNormal = normalList.get(0).getQueueNumber();// 0->norCount++

						for (QueueNumberDTO dto : normalList) {
							if (dto.getSkipCount() < greatestSkipCountNormal) {
								queueNumberNormal = dto.getQueueNumber();
								break;
							}
						}
					}
				}
				// ################################################ priority number end
				// ########################################//

				if (queueNumberNormal != null && !queueNumberNormal.isEmpty()
						&& !queueNumberNormal.trim().equalsIgnoreCase("") && queueNumberPriority != null
						&& !queueNumberPriority.isEmpty() && !queueNumberPriority.trim().equalsIgnoreCase("")) {

				} else {
					// ################################################ normal number start
					// ########################################//
					if (normalList != null && normalList.size() != 0) {
						queueNumberNormal = normalList.get(0).getQueueNumber();// 0->norCount++ //06/11/2018

						for (QueueNumberDTO dto : normalList) {
							if (dto.getSkipCount() < greatestSkipCountNormal) {
								queueNumberNormal = dto.getQueueNumber();
								break;
							}
						}
					} else {
						if (priorityList != null && priorityList.size() != 0) {
							queueNumberNormal = priorityList.get(0).getQueueNumber();// 0->priCount++ //06/11/2018

							for (QueueNumberDTO dto : priorityList) {
								if (dto.getSkipCount() < greatestSkipCountPriority) {
									queueNumberNormal = dto.getQueueNumber();
									break;
								}
							}
						}
					}
					// ################################################ normal number end
					// ########################################//

				}

			}

			if (queueNumberNormal != null && !queueNumberNormal.isEmpty()
					&& !queueNumberNormal.trim().equalsIgnoreCase("")) {
				skippedDisplayList.add(queueNumberNormal);
			}
			if (queueNumberPriority != null && !queueNumberPriority.isEmpty()
					&& !queueNumberPriority.trim().equalsIgnoreCase("")) {
				skippedDisplayList.add(queueNumberPriority);
			}

			if (skippedDisplayList.size() != 0 && skippedDisplayList.size() < 2) {
				if (normalList != null && normalList.size() != 0) {
					QueueNumberDTO dto = new QueueNumberDTO();
					for (QueueNumberDTO normalDto : normalList) {
						if (normalDto.getQueueNumber() != null && !normalDto.getQueueNumber().isEmpty()
								&& !normalDto.getQueueNumber().equalsIgnoreCase(skippedDisplayList.get(0))) {
							dto = normalDto;
							break;
						}
					}
					skippedDisplayList.add(dto.getQueueNumber());
				}
				if (priorityList != null && priorityList.size() != 0) {
					QueueNumberDTO dto = new QueueNumberDTO();
					for (QueueNumberDTO priorityDto : priorityList) {
						if (priorityDto.getQueueNumber() != null && !priorityDto.getQueueNumber().isEmpty()
								&& !priorityDto.getQueueNumber().equalsIgnoreCase(skippedDisplayList.get(0))) {
							dto = priorityDto;
							break;
						}
					}
					skippedDisplayList.add(dto.getQueueNumber());
				}
			}
			// add elements to all, including duplicates
			Set<String> hs = new HashSet<>();
			hs.addAll(skippedDisplayList);
			skippedDisplayList.clear();
			skippedDisplayList.addAll(hs);

			List<String> tempSkippedDisplayList = new ArrayList<String>();
			if (skippedDisplayList != null && !skippedDisplayList.isEmpty() && skippedDisplayList.size() != 0) {
				for (String s : skippedDisplayList) {
					if (s != null && !s.isEmpty() && !s.trim().equalsIgnoreCase("")) {
						tempSkippedDisplayList.add(s);
					}
				}
			}
			if (tempSkippedDisplayList != null && !tempSkippedDisplayList.isEmpty()
					&& tempSkippedDisplayList.size() != 0) {
				Collections.sort(tempSkippedDisplayList);
				skippedDisplayList = tempSkippedDisplayList;
			}

		} catch (SQLException e) {
			logger.info("selectSkippedQueueNumberFromSkipList error: " + e.toString());
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}
		logger.info("selectSkippedQueueNumberFromSkipList end");

		return skippedDisplayList;

	}

	@Override
	public boolean checkAmmenmentValidationForOngoingQueueNumbers(QueueNumberDTO queueNumberDTO) {
		logger.info("checkAmmenmentValidation start");

		Connection con = null;
		boolean valid = false;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String where = "";
		boolean whereFill = false;

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {
			con = ConnectionManager.getConnection();

			if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
					&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
				where = "que_permit_no='" + queueNumberDTO.getPermitNo() + "'";
				whereFill = true;
			}

			if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
					&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
				if (whereFill) {
					where = where + " and que_vehicle_no='" + queueNumberDTO.getVehicleNo() + "'";
				} else {
					where = "que_vehicle_no='" + queueNumberDTO.getVehicleNo() + "'";
				}
			}

			String sql = "select * from public.nt_m_queue_master where que_date like '" + today
					+ "%' and que_trn_type_code = '" + queueNumberDTO.getTransTypeCode() + "' and " + where;

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("retrieveCurrentQueueNumber error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("checkAmmenmentValidation end");
		return valid;
	}

	public static void main(String[] args) {
		QueueManagementServiceImpl d = new QueueManagementServiceImpl();
		String quenum = d.callNextQueueNumberAction("13,05,14", null);

	}

	/********************** start ***************************/

	@Override
	public List<DisplayQueueNumbersDTO> counterDetails() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<DisplayQueueNumbersDTO> queueDisplayBoard = new ArrayList<DisplayQueueNumbersDTO>();

		try {

			con = ConnectionManager.getConnection();
			// Get Counter,Counter No,Counter Name from nt_r_counter table

			String sql = "select code,description from public.nt_r_transaction_type order by code";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			DisplayQueueNumbersDTO tableRaw_obj = null;

			while (rs.next()) {
				// Set Counter,Counter No,Counter Name to the DTO
				if (rs.getString("code").equals("01") || rs.getString("code").equals("02")
						|| rs.getString("code").equals("03") || rs.getString("code").equals("04")
						|| rs.getString("code").equals("05") || rs.getString("code").equals("09")) {

					tableRaw_obj = new DisplayQueueNumbersDTO();

					switch (rs.getString("code")) {
					case "01":
						tableRaw_obj.setCounterName("TENDER");

						DisplayQueueNumbersDTO tender = getTableRaw("01", null, tableRaw_obj, con);
						if (validateRaw(tender)) {
							queueDisplayBoard.add(tender);
						}

						break;
					case "02":
						tableRaw_obj.setCounterName("INSPECTION");

						DisplayQueueNumbersDTO inspection = getTableRaw("02", "01", tableRaw_obj, con);
						if (validateRaw(inspection)) {
							queueDisplayBoard.add(inspection);
						}

						break;
					case "03":
						tableRaw_obj.setCounterName("NEW PERMIT");

						DisplayQueueNumbersDTO newPermit = getTableRaw("03", "02", tableRaw_obj, con);
						if (validateRaw(newPermit)) {
							queueDisplayBoard.add(newPermit);
						}
						break;
					case "04":
						tableRaw_obj.setCounterName("RENEWAL");

						DisplayQueueNumbersDTO renewal = getTableRaw("04", "02", tableRaw_obj, con);
						if (validateRaw(renewal)) {
							queueDisplayBoard.add(renewal);
						}
						break;
					case "05":
						tableRaw_obj.setCounterName("AMENDMENT");

						DisplayQueueNumbersDTO amendment = getTableRaw("13,05,14,15,16", null, tableRaw_obj, con);
						if (validateRaw(amendment)) {
							queueDisplayBoard.add(amendment);
						}
						break;
					case "09":
						tableRaw_obj.setCounterName("INQUIRY");

						DisplayQueueNumbersDTO inquiry = getTableRaw("09", null, tableRaw_obj, con);
						if (validateRaw(inquiry)) {
							queueDisplayBoard.add(inquiry);
						}
						break;
					}

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		ConnectionManager.close(rs);
		ConnectionManager.close(ps);
		ConnectionManager.close(con);

		return queueDisplayBoard;

	}

	public boolean validateRaw(DisplayQueueNumbersDTO tableRaw_ob) {
		if (tableRaw_ob.getQueueNo1() == null && tableRaw_ob.getQueueNo4() == null) {

			return false;

		} else {

			return true;

		}
	}

	public DisplayQueueNumbersDTO getTableRaw(String a, String b, DisplayQueueNumbersDTO tableRaw_obj, Connection con) {

		List<String> nextNumbers = null;
		List<String> skipedNumbers = null;

		nextNumbers = callNextQueueNumbersForDisplayAction(a, b);
		skipedNumbers = skippedQueueNumberForDisplay(con, a, b);

		for (int i = 0; i < nextNumbers.size(); i++) {

			if (i == 0)
				tableRaw_obj.setQueueNo1(nextNumbers.get(i));
			if (i == 1)
				tableRaw_obj.setQueueNo2(nextNumbers.get(i));
			if (i == 2)
				tableRaw_obj.setQueueNo3(nextNumbers.get(i));
		}

		for (int i = 0; i < skipedNumbers.size(); i++) {

			if (i == 0)
				tableRaw_obj.setQueueNo4(skipedNumbers.get(i));
			if (i == 1)
				tableRaw_obj.setQueueNo5(skipedNumbers.get(i));
		}
		return tableRaw_obj;
	}

	@Override
	public List<DisplayQueueNumbersDTO> currentNumbers() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<DisplayQueueNumbersDTO> queueDisplayBoard = new ArrayList<DisplayQueueNumbersDTO>();

		try {

			con = ConnectionManager.getConnection();
			// Get Counter,Counter No,Counter Name from nt_r_counter table

			String sql = "select cou_counter_id,cou_serving_queueno from nt_r_counter where cou_status='A' and cou_serving_queueno is not null   order by cou_counter_id";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				// Set Counter,Counter No,Counter Name to the DTO
				DisplayQueueNumbersDTO tableRaw_obj = new DisplayQueueNumbersDTO();
				tableRaw_obj.setCounterId(rs.getString("cou_counter_id"));
				tableRaw_obj.setCounterCurrentNo(rs.getString("cou_serving_queueno"));

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

	@Override
	public void updateCounter(String counterId, String counterStatus) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {

			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_r_counter \r\n" + "SET cou_serving_queueno = ? \r\n"
					+ "WHERE cou_counter_id = ? ";

			ps = con.prepareStatement(sql3);

			ps.setNull(1, Types.VARCHAR);
			ps.setString(2, counterId);

			ps.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public String checkTaskHistoryForInspection(String vehicleNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean valid = false;
		boolean validAnotherDay = false;
		String error = null;

		try {
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String dateStr = dateFormat.format(date);

			con = ConnectionManager.getConnection();

			// check data for today start
			String sql = "select * from public.nt_m_queue_master where que_vehicle_no ='" + vehicleNo + "' and "
					+ "que_task_code='PR200' and que_task_status='O' and que_date like '" + dateStr + "%'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			// check data for today end

			if (!valid) {
				String sql2 = "select * from public.nt_m_queue_master where que_vehicle_no ='" + vehicleNo + "' "
						+ "and que_task_code='PR200' and que_task_status='O' and que_date not like '" + dateStr + "%'";

				ps = con.prepareStatement(sql2);
				rs = ps.executeQuery();

				while (rs.next()) {
					validAnotherDay = true;
				}
			} else {
				error = "Renewal process is still ongoing for this vehicle number";
			}

			if (validAnotherDay) {
				error = "Inspection comepleted. Please take a renewal token";
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return error;
	}

	@Override
	public boolean checkTaskHistoryForInspectionUploadPhotosComplete(String vehicleNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean valid = false;

		try {

			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_m_queue_master where que_vehicle_no ='" + vehicleNo
					+ "' and que_task_code='PM101' and que_task_status='C'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return valid;
	}

	@Override
	public String getApplicationNumOfOldQueueFromAmendment(String permitNo, String vehicleNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String appNum = null;

		String where = null;

		try {

			if (permitNo != null && !permitNo.isEmpty() && !permitNo.trim().equalsIgnoreCase("")) {
				where = "amd_permit_no='" + permitNo + "'";
			}

			if (vehicleNo != null && !vehicleNo.isEmpty() && !vehicleNo.trim().equalsIgnoreCase("")) {
				if (where != null && !where.isEmpty()) {
					where = where + " and (amd_new_busno='" + vehicleNo + "' or amd_existing_busno='" + vehicleNo
							+ "')";
				} else {
					where = " amd_new_busno='" + vehicleNo + "' or amd_existing_busno='" + vehicleNo + "'";
				}
			}

			con = ConnectionManager.getConnection();

			String sql = "select amd_application_no from public.nt_m_amendments where " + where;

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				appNum = rs.getString("amd_application_no");
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return appNum;
	}

	@Override
	public boolean validateTasksOfApplicatinNum(String applicationNo) {
		boolean valid = false;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_task_det where tsd_app_no='" + applicationNo + "' "
					+ "and ((tsd_task_code='AM100' and tsd_status='C') or (tsd_task_code='AM106' and tsd_status='C') or (tsd_task_code='AM103' and tsd_status='C')) ";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			valid = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return valid;

	}

	@Override
	public boolean checkTaskHistoryForInspectionComplete(String vehicleNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean valid = false;

		try {

			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_m_queue_master where que_vehicle_no ='" + vehicleNo
					+ "' and que_task_code='PM100' and que_task_status='C'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return valid;
	}

	@Override
	public boolean checkInspectionTokeGenerated(String vehicleNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean valid = false;

		try {

			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String dateStr = dateFormat.format(date);

			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_m_queue_master where que_vehicle_no ='" + vehicleNo
					+ "' and que_trn_type_code='02' and que_date like '" + dateStr + "%'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return valid;
	}

	@Override
	public QueueNumberDTO validateUserInputValueForInspectionAmmendment(QueueNumberDTO queueNumberDTO) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		QueueNumberDTO dto = null;

		try {
			con = ConnectionManager.getConnection();

			// TODO if(tender) should check from tender table in DB and check validity

			String whereClause = "";
			if (queueNumberDTO != null) {

				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					whereClause = whereClause + " pm_application_no='" + queueNumberDTO.getApplicationNo() + "'";
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					if (whereClause != null && !whereClause.isEmpty() && !whereClause.trim().equalsIgnoreCase("")) {
						whereClause = whereClause + " and pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
					} else {
						whereClause = whereClause + " pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "'";
					}
				}
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					if (whereClause != null && !whereClause.isEmpty() && !whereClause.trim().equalsIgnoreCase("")) {
						whereClause = whereClause + " and pm_permit_no='" + queueNumberDTO.getPermitNo() + "'";
					} else {
						whereClause = whereClause + " pm_permit_no='" + queueNumberDTO.getPermitNo() + "'";
					}
				}

			} else {
				return null;
			}

			String sql = "";

			whereClause = " pm_vehicle_regno='" + queueNumberDTO.getVehicleNo() + "' and pm_status='P'";

			sql = "SELECT pm_application_no,pm_permit_no,pm_vehicle_regno,pm_status FROM public.nt_t_pm_application WHERE "
					+ whereClause;

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new QueueNumberDTO();
				dto.setApplicationNo(rs.getString("pm_application_no"));
				dto.setPermitNo(rs.getString("pm_permit_no"));
				dto.setVehicleNo(rs.getString("pm_vehicle_regno"));
				dto.setApplicationPmStatus(rs.getString("pm_status"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;

	}

	@Override
	public String checkProcessIsDoneForAmmendment(QueueNumberDTO queueNumberDTO) {
		boolean valid = false;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String where = "";
		String applicationNum = "";
		String returnVal = "";
		try {

			con = ConnectionManager.getConnection();
			if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
					&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
				where = " and amd_existing_busno='" + queueNumberDTO.getVehicleNo() + "'";
			}
			if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
					&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
				if (where != null && !where.isEmpty() && !where.trim().equalsIgnoreCase("")) {
					where = where + " and amd_permit_no='" + queueNumberDTO.getPermitNo() + "'";
				} else {
					where = " and amd_permit_no='" + queueNumberDTO.getPermitNo() + "'";
				}
			}

			// get application number start
			String query = "SELECT amd_application_no FROM public.nt_m_amendments where amd_trn_type='"
					+ queueNumberDTO.getTransTypeCode() + "' " + where;
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				applicationNum = rs.getString("amd_application_no");
			}

			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			// get application number end

			String sql = "select * from public.nt_t_task_det where tsd_app_no='" + applicationNum + "' "
					+ "and tsd_task_code='PM400' and tsd_status='C'";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				valid = true;
			}

			if (!valid) {
				returnVal = applicationNum;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			valid = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return returnVal;

	}

	@Override
	public boolean checkTaskHistoryForInspectionAmmendment(String vehicleNo) {
		// AM 100 C or AM106 C or AM102 C or AM101 C or AM103 C
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean alreadyTaken = false;

		try {

			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_m_queue_master where que_vehicle_no ='" + vehicleNo + "' and "
					+ "((que_task_code='AM100' and que_task_status='C') or (que_task_code='AM103' and que_task_status='C') or (que_task_code='AM106' and que_task_status='C') or (que_task_code='AM102' and que_task_status='C') "
					+ "or (que_task_code='AM101' and que_task_status='C'))";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				alreadyTaken = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return alreadyTaken;
	}

	@Override
	public void updateQueueNoInApplication(String queueNo, String appNo) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {

			con = ConnectionManager.getConnection();

			String sql3 = "UPDATE public.nt_t_pm_application  \r\n" + "SET pm_queue_no = ? \r\n"
					+ "WHERE pm_application_no = ? ";

			ps = con.prepareStatement(sql3);

			ps.setString(1, queueNo);
			ps.setString(2, appNo);

			ps.executeUpdate();
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	/*
	 * Description: KIOSK CHANGES Date: 01/19/2021
	 * 
	 */
	// Main Display-KIOSK 01
	@Override
	public QueueNumberDTO generateMainTokenNumber(String sectionCode) {
		logger.info("generateMainTokenNumber start");
		Connection con = null;
		QueueNumberDTO queueNumberDTO = new QueueNumberDTO();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String token = "";

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT prefix,normal_current_no as latestVal,section_des FROM public.nt_r_main_queue_number_run_value WHERE section_code=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, sectionCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				long latestVal = rs.getLong("latestVal");
				String prefix = rs.getString("prefix");
				queueNumberDTO.setTransTypeCode(rs.getString("section_des"));

				String strlatestVal = String.format("%03d", latestVal);
				if (strlatestVal.equals("999")) {
					token = "LIMIT_EXCEEDED";
					logger.info(
							"generateMainTokenNumber Token numbers limit per day exceeded for Type: " + sectionCode);
				} else {
					token = String.format("%03d", latestVal + 1);// Eg: 001
					updateCurrentMainQueueNumber(con, token, sectionCode);

					token = prefix + "-" + token;// Eg: OP-001
					insertToMainQueueMasterTable(con, token, sectionCode);
					con.commit();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("retrieveCurrentMainQueueNumber error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		queueNumberDTO.setQueueNumber(token);
		logger.info("generateMainTokenNumber TOKEN - " + token);
		logger.info("generateMainTokenNumber end");

		return queueNumberDTO;
	}

	private void updateCurrentMainQueueNumber(Connection con, String currentNo, String sectionType) {
		logger.info("updateCurrentMainQueueNumber start");
		PreparedStatement ps = null;
		try {
			String sql = "UPDATE public.nt_r_main_queue_number_run_value SET normal_current_no=? where section_code=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, currentNo);
			ps.setString(2, sectionType);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateCurrentMainQueueNumber error");
			logger.error(e);
		} finally {
			ConnectionManager.close(ps);
		}
		logger.info("updateCurrentMainQueueNumber end");
	}

	private void insertToMainQueueMasterTable(Connection con, String token, String sectionType) {
		logger.info("updateMainQueueMasterTable start");
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");

		try {
			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_main_queue_master");

			String sql = "INSERT INTO public.nt_t_main_queue_master"
					+ "(que_seq, que_date, queueno, token_created_date_time, section_code, counter_id, start_date_time, end_date_time, status, reference_no, completed_by, token_no)\r\n"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";

			ps = con.prepareStatement(sql);
			ps.setLong(1, seqNo);
			ps.setString(2, df.format(date));
			ps.setString(3, token);
			ps.setTimestamp(4, timestamp);
			ps.setString(5, sectionType);
			ps.setNull(6, Types.VARCHAR);
			ps.setTimestamp(7, null);
			ps.setTimestamp(8, null);
			ps.setString(9, "I");
			ps.setNull(10, Types.VARCHAR);
			ps.setNull(11, Types.VARCHAR);
			ps.setNull(12, Types.VARCHAR);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateMainQueueMasterTable error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}
		logger.info("updateMainQueueMasterTable end");
	}

	@Override
	public List<DivisionDTO> getAllDivisions() {
		logger.info("getAllDivisions start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<DivisionDTO> allDivisions = new ArrayList<DivisionDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT section_code, section_des FROM public.nt_r_main_queue_number_run_value order by section_des asc";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				DivisionDTO division = new DivisionDTO();
				division.setSectionCode(rs.getString("section_code"));
				division.setSectionDes(rs.getString("section_des"));
				allDivisions.add(division);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getAllDivisions error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getAllDivisions end");
		return allDivisions;
	}

	// QMS User Console
	@Override
	public List<MainCounterDTO> getActiveCountersOfDivision(String sectionCode) {
		logger.info("getActiveCountersOfDivision start for divisionCode: " + sectionCode);
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<MainCounterDTO> mainCounters = new ArrayList<MainCounterDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT cou_counter_id, cou_counter_name FROM public.nt_r_main_counters WHERE cou_section_code='"
					+ sectionCode + "' AND cou_status='A' order by cou_counter_id asc;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				MainCounterDTO mainCounter = new MainCounterDTO();
				mainCounter.setCounterId(rs.getString("cou_counter_id"));
				mainCounter.setCounterName(rs.getString("cou_counter_name"));
				mainCounters.add(mainCounter);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getActiveCountersOfDivision error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getActiveCountersOfDivision end");
		return mainCounters;
	}

	@Override
	public boolean isQueueNoProceed(String queueno) {
		logger.info("isQueueNoProceed start for queueno: " + queueno);
		if (queueno == null || queueno.isEmpty()) {
			return false;
		}
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		boolean proceeded = false;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM public.nt_t_main_queue_master WHERE status ='C' AND section_code ='OP' AND queueno='"
					+ queueno + "';";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				proceeded = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("isQueueNoProceed error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("isQueueNoProceed end");
		return proceeded;
	}

	@Override
	public int isValidQueueNo(String queueno) {

		logger.info("isValidQueueNo start for queueno: " + queueno);
		if (queueno == null || queueno.isEmpty()) {
			return 500; // queue number is null or empty
		}
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		int valid = 0; // Invalid token number.

		String createdDate = null;
		java.util.Date today = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		createdDate = formatter.format(today);

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM public.nt_t_main_queue_master WHERE queueno='" + queueno
					+ "' AND status IN ('I','O') AND token_created_date_time::date = date '" + createdDate
					+ "' LIMIT 1;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (rs.getString("status").equals("I")) {
					valid = 1; // valid queue number
				} else if (rs.getString("status").equals("O")) {
					valid = 2; // This queue number is already assigned to a counter.
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("isValidQueueNo error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("isValidQueueNo end");
		return valid;

	}

	@Override
	public String getNextToken(String sectionCode) {
		logger.info("getNextToken start for sectionCode: " + sectionCode);
		if (sectionCode == null || sectionCode.isEmpty()) {
			return null;
		}
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		String nextQueueNo = null;

		try {
			con = ConnectionManager.getConnection();
			String sql = "select * FROM public. nt_t_main_queue_master WHERE status ='I' AND section_code ='"
					+ sectionCode + "' ORDER BY token_created_date_time asc LIMIT 1;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				nextQueueNo = rs.getString("queueno");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getNextToken error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getNextToken end");
		return nextQueueNo;
	}

	@Override
	public boolean callTokenNo(String sectionCode, String counterId, String queueno) {
		logger.info("callTokenNo start");
		if (sectionCode == null || sectionCode.isEmpty() || counterId == null || counterId.isEmpty() || queueno == null
				|| queueno.isEmpty()) {
			return false;
		}
		Connection con = null;

		boolean returnVal = false;
		try {
			con = ConnectionManager.getConnection();
			boolean updateOne = updateCurrentServingQueueNumber(con, queueno, counterId, sectionCode);
			boolean updateTwo = updateMainQueueMasterTable(con, counterId, queueno, sectionCode);
			if (updateOne && updateTwo) {// if both returns true
				con.commit();
				returnVal = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("callTokenNo error");
			logger.error(e);
		} finally {
			ConnectionManager.close(con);
		}

		logger.info("callTokenNo end");
		return returnVal;
	}

	private boolean updateCurrentServingQueueNumber(Connection con, String queueno, String counterId,
			String sectionCode) {
		logger.info("updateCurrentServingQueueNumber start");
		PreparedStatement ps = null;
		boolean returnVal = false;
		try {
			String sql = "UPDATE public.nt_r_main_counters SET cou_serving_queueno =? WHERE cou_counter_id =? and cou_section_code =?;";

			ps = con.prepareStatement(sql);
			ps.setString(1, queueno);
			ps.setString(2, counterId);
			ps.setString(3, sectionCode);
			int update = ps.executeUpdate();
			if (update > 0) {
				returnVal = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateCurrentServingQueueNumber error");
			logger.error(e);
		} finally {
			ConnectionManager.close(ps);
		}

		logger.info("updateCurrentServingQueueNumber end");
		return returnVal;
	}

	private boolean updateMainQueueMasterTable(Connection con, String counterId, String queueno, String sectionCode) {
		logger.info("updateMainQueueMasterTable start");
		PreparedStatement ps = null;
		boolean returnVal = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			String sql = "UPDATE public.nt_t_main_queue_master SET start_date_time=?, counter_id=?, status='O' where queueno=? AND section_code=? AND status !='C'";

			ps = con.prepareStatement(sql);
			ps.setTimestamp(1, timestamp);
			ps.setString(2, counterId);
			ps.setString(3, queueno);
			ps.setString(4, sectionCode);
			int update = ps.executeUpdate();
			if (update > 0) {
				returnVal = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateMainQueueMasterTable error");
			logger.error(e);
		} finally {
			ConnectionManager.close(ps);
		}
		logger.info("updateMainQueueMasterTable end");
		return returnVal;
	}

	// For QMS Customer Dashboard Console
	@Override
	public List<DepartmentDTO> getAllActiveDepartments() {
		logger.info("getAllActiveDepartments start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<DepartmentDTO> allActiveDepartments = new ArrayList<DepartmentDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT code, description, active FROM public.nt_r_kdepartment WHERE active='A' order by description asc;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				DepartmentDTO department = new DepartmentDTO();
				department.setCode(rs.getString("code"));
				department.setDescription(rs.getString("description"));
				department.setActive(rs.getString("active"));
				allActiveDepartments.add(department);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getAllActiveDepartments error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getAllActiveDepartments end");
		return allActiveDepartments;
	}

	@Override
	public List<MainCounterDTO> getCountersOfDepartments(String departmentCode) {

		logger.info("getCountersOfDepartments start for departmentCode: " + departmentCode);
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<MainCounterDTO> mainCounters = new ArrayList<MainCounterDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "select distinct r.department, r.section_code, c.cou_counter_id , c.cou_counter_name, c.cou_serving_queueno from nt_r_main_counters c "
					+ "inner join nt_r_main_queue_number_run_value r on r.section_code = c.cou_section_code "
					+ "where c.cou_status ='A' and r.department = '" + departmentCode
					+ "' order by c.cou_counter_name;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				MainCounterDTO mainCounter = new MainCounterDTO();
				mainCounter.setCounterId(rs.getString("cou_counter_id"));
				mainCounter.setCounterName(rs.getString("cou_counter_name"));
				mainCounter.setServingQueueNo(rs.getString("cou_serving_queueno"));
				mainCounters.add(mainCounter);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getCountersOfDepartments error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getCountersOfDepartments end");
		return mainCounters;
	}

	// User Closed Console
	@Override
	public List<MainCounterDTO> getCountersOfDivision(String sectionCode) {
		logger.info("getCountersOfDivisions start for divisionCode: " + sectionCode);
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<MainCounterDTO> mainCounters = new ArrayList<MainCounterDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT cou_counter_id, cou_counter_name FROM public.nt_r_main_counters WHERE cou_section_code='"
					+ sectionCode + "' order by cou_counter_id asc;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				MainCounterDTO mainCounter = new MainCounterDTO();
				mainCounter.setCounterId(rs.getString("cou_counter_id"));
				mainCounter.setCounterName(rs.getString("cou_counter_name"));
				mainCounters.add(mainCounter);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getCountersOfDivisions error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getCountersOfDivisions end");
		return mainCounters;
	}

	@Override
	public List<UserClosedDTO> getOngoingQueueRecordsForClosedConsole() {
		logger.info("getOngoingQueueRecordsForClosedConsole start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<UserClosedDTO> searchResults = new ArrayList<UserClosedDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM public. nt_t_main_queue_master WHERE status!='C' order by token_created_date_time;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				UserClosedDTO result = new UserClosedDTO();
				result.setSectionCode(rs.getString("section_code"));
				result.setQueueno(rs.getString("queueno"));

				Timestamp tsStart = rs.getTimestamp("start_date_time");
				String formattedDateStart = "";
				if (tsStart != null) {
					Date dateStart = new Date();
					dateStart.setTime(tsStart.getTime());
					formattedDateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateStart);
				}
				result.setStartDateTime(formattedDateStart);

				Timestamp tsEnd = rs.getTimestamp("end_date_time");
				String formattedDateEnd = "";
				if (tsEnd != null) {
					Date dateEnd = new Date();
					dateEnd.setTime(tsEnd.getTime());
					formattedDateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateEnd);
				}
				result.setEndDateTime(formattedDateEnd);

				result.setReferenceNo(rs.getString("reference_no"));

				if (rs.getString("status").equals("I")) {
					result.setStatus("Initiated");
				} else if (rs.getString("status").equals("O")) {
					result.setStatus("Ongoing");
				} else if (rs.getString("status").equals("P")) {
					result.setStatus("Processing");
				} else if (rs.getString("status").equals("C")) {
					result.setStatus("Completed");
				}
				searchResults.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getOngoingQueueRecordsForClosedConsole error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getOngoingQueueRecordsForClosedConsole end");
		return searchResults;
	}

	@Override
	public List<UserClosedDTO> getUserClosedSearchResults(UserClosedDTO userClosedDTO) {
		logger.info("getUserClosedSearchResults start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<UserClosedDTO> searchResults = new ArrayList<UserClosedDTO>();
		String whereClause = "";
		if (userClosedDTO.getSectionCode() != null && !userClosedDTO.getSectionCode().isEmpty()) {
			whereClause = " WHERE section_code ='" + userClosedDTO.getSectionCode() + "' ";
		}
		if (userClosedDTO.getCounterId() != null && !userClosedDTO.getCounterId().isEmpty()) {
			if (!whereClause.equals("")) {
				whereClause = whereClause + " AND counter_id ='" + userClosedDTO.getCounterId() + "' ";
			} else {
				whereClause = " WHERE counter_id ='" + userClosedDTO.getCounterId() + "' ";
			}
		}
		if (userClosedDTO.getDate() != null) {
			String createdDate = null;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			createdDate = formatter.format(userClosedDTO.getDate());

			if (createdDate != null) {
				if (!whereClause.equals("")) {
					whereClause = whereClause + " AND token_created_date_time::date = date '" + createdDate + "' ";
				} else {
					whereClause = " WHERE token_created_date_time::date = date '" + createdDate + "' ";
				}
			}
		}
		if (userClosedDTO.getStatus() != null && !userClosedDTO.getStatus().isEmpty()) {
			if (!whereClause.equals("")) {
				whereClause = whereClause + " AND status ='" + userClosedDTO.getStatus() + "' ";
			} else {
				whereClause = " WHERE status ='" + userClosedDTO.getStatus() + "' ";
			}
		}
		whereClause = whereClause + " order by token_created_date_time; ";

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM public. nt_t_main_queue_master " + whereClause;

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				UserClosedDTO result = new UserClosedDTO();
				result.setSectionCode(rs.getString("section_code"));
				result.setQueueno(rs.getString("queueno"));

				Timestamp tsStart = rs.getTimestamp("start_date_time");
				String formattedDateStart = "";
				if (tsStart != null) {
					Date dateStart = new Date();
					dateStart.setTime(tsStart.getTime());
					formattedDateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateStart);
				}
				result.setStartDateTime(formattedDateStart);

				Timestamp tsEnd = rs.getTimestamp("end_date_time");
				String formattedDateEnd = "";
				if (tsEnd != null) {
					Date dateEnd = new Date();
					dateEnd.setTime(tsEnd.getTime());
					formattedDateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateEnd);
				}
				result.setEndDateTime(formattedDateEnd);

				result.setReferenceNo(rs.getString("reference_no"));

				if (rs.getString("status").equals("I")) {
					result.setStatus("Initiated");
				} else if (rs.getString("status").equals("O")) {
					result.setStatus("Ongoing");
				} else if (rs.getString("status").equals("P")) {
					result.setStatus("Processing");
				} else if (rs.getString("status").equals("C")) {
					result.setStatus("Completed");
				}

				searchResults.add(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getUserClosedSearchResults error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getUserClosedSearchResults end");
		return searchResults;
	}

	@Override
	public boolean completeMainQueue(UserClosedDTO userClosedDTO) {
		logger.info("completeMainQueue start");
		boolean returnVal = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_main_queue_master SET end_date_time=?, status='C', reference_no=?, completed_by=? WHERE queueno=? and section_code=? and status!='C';";

			ps = con.prepareStatement(sql);
			ps.setTimestamp(1, timestamp);
			ps.setString(2, userClosedDTO.getReferenceNo());
			ps.setString(3, userClosedDTO.getCompletedBy());
			ps.setString(4, userClosedDTO.getQueueno());
			ps.setString(5, userClosedDTO.getSectionCode());
			int success = ps.executeUpdate();

			if (success > 0) {
				returnVal = true;
			}

			String sql2 = "UPDATE public.nt_r_main_counters SET cou_serving_queueno=NULL WHERE cou_serving_queueno=?;";

			ps2 = con.prepareStatement(sql2);
			ps2.setString(1, userClosedDTO.getQueueno());
			ps2.executeUpdate();

			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("completeMainQueue error");
			logger.error(e);
			returnVal = false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		logger.info("completeMainQueue end");
		return returnVal;
	}

	/* New queue generate CR start here */
	@Override
	public List<QueueNumberDTO> getQueuesForTransactionType(String transactionType) {

		logger.info(" get active queue no for " + transactionType + " method start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<QueueNumberDTO> queueList = new ArrayList<QueueNumberDTO>();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT distinct queueno FROM nt_t_main_queue_master WHERE section_code =? AND status = 'O';";

			ps = con.prepareStatement(sql);
			ps.setString(1, transactionType);
			rs = ps.executeQuery();

			while (rs.next()) {
				QueueNumberDTO dto = new QueueNumberDTO();
				dto.setTokenNo(rs.getString("queueno"));
				queueList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error -> get active queue no for " + transactionType);
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info(" get active queue no for " + transactionType + " method end");

		return queueList;
	}

	@Override
	public boolean checkSelectedTransactionTypeValidForQueueNumberNewMethod(QueueNumberDTO queueNumberDTO,
			String transType) {
		return false;
	}

	@Override
	public boolean isVehicleDetailsFoundInTaskDetails(String vehicleNo) {
		logger.info(" check vehicle no " + vehicleNo + " in task details. method start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean isDataFound = false;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM public.nt_t_task_det WHERE tsd_vehicle_no =? ";

			ps = con.prepareStatement(sql);
			ps.setString(1, vehicleNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				isDataFound = true;
			} else {
				isDataFound = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error -> check vehicle no " + vehicleNo + " in task deatils");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info(" check vehicle no " + vehicleNo + " in task details. method end");

		return isDataFound;
	}

	@Override
	public boolean isInspectionComplete(String vehicleNo) {

		logger.info(" check task code no PM101 and PM200 in tasks details. method start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean isDataFound = false;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM nt_t_task_det WHERE tsd_vehicle_no = ? AND (tsd_task_code = 'PM101' AND  "
					+ "tsd_status =  'C'  OR tsd_task_code = 'PM200' AND  tsd_status =  'O' );";

			ps = con.prepareStatement(sql);
			ps.setString(1, vehicleNo);

			rs = ps.executeQuery();

			if (rs.next()) {
				isDataFound = true;
			} else {
				isDataFound = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error -> check task code no check in task deatils");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info(" check task code no PM101 and PM200 in tasks details. method start");

		return isDataFound;
	}

	@Override
	public QueueNumberDTO getPermitInfo(String permitNo) {

		logger.info(" get " + permitNo + " permit no information method start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		QueueNumberDTO dto = new QueueNumberDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT * FROM public.nt_t_pm_application "
					+ "WHERE pm_permit_no =? AND pm_status in ('A','O') ";

			ps = con.prepareStatement(sql);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				dto.setApplicationNo(rs.getString("pm_application_no"));
				dto.setVehicleNo(rs.getString("pm_vehicle_regno"));
				dto.setPermitInfoFound(true);
			} else {

				dto.setPermitInfoFound(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error -> get " + permitNo + " permit no info");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info(" get " + permitNo + " permit no information method end");

		return dto;
	}

	@Override
	public QueueNumberDTO getPermitInfoWithoutCheckingStatus(String permitNo) {

		logger.info("getPermitInfoWithoutStatus for: " + permitNo + " start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		QueueNumberDTO dto = new QueueNumberDTO();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT * FROM public.nt_t_pm_application " + "WHERE pm_permit_no =?";

			ps = con.prepareStatement(sql);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				dto.setApplicationNo(rs.getString("pm_application_no"));
				dto.setVehicleNo(rs.getString("pm_vehicle_regno"));
				dto.setPermitInfoFound(true);
			} else {
				dto.setPermitInfoFound(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("getPermitInfoWithoutStatus error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("getPermitInfoWithoutStatus end");
		return dto;
	}

	@Override
	public boolean isApplicatioinDetailsFoundInTaskDetails(String appNo) {

		logger.info(" check task code no PM101 and PM200 in tasks details. method start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean isDataFound = false;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM nt_t_task_det WHERE tsd_app_no = ? AND (tsd_task_code = 'PM101' AND  "
					+ "tsd_status =  'C'  OR tsd_task_code = 'PR200' AND  tsd_status =  'O' );";

			ps = con.prepareStatement(sql);
			ps.setString(1, appNo);

			rs = ps.executeQuery();

			if (rs.next()) {
				isDataFound = true;
			} else {
				isDataFound = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error -> check task code no check in task deatils");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info(" check task code no PM101 and PM200 in tasks details. method start");

		return isDataFound;
	}

	@Override
	public boolean isInspectionStatusNull(String appNo) {

		logger.info(" check inspection status null in the application table. method start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean isNull = false;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM nt_t_task_det WHERE tsd_app_no = ? AND (tsd_task_code = 'PM101' AND  "
					+ "tsd_task_code =  'C' ) OR (tsd_task_code = 'PM200' AND  tsd_task_code =  'O' );";

			ps = con.prepareStatement(sql);
			ps.setString(1, appNo);

			rs = ps.executeQuery();

			if (rs.next()) {
				isNull = true;
			} else {
				isNull = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error -> checking inspection status in application table");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info(" check inspection status null in the application table. method end");

		return isNull;
	}

	@Override
	public String insertQueueDataIntoQueueMasterTable(QueueNumberDTO queueNumberDTO, String loginUser) {

		logger.info("insertQueueDataIntoNT_M_QUEUE_MASTER start");

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String queueNo = null;
		long runningValue = 0;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		try {

			con = ConnectionManager.getConnection();

			if (queueNumberDTO.getTokenNo() == null) {
				long tempRunVal = retrieveCurrentQueueNumberNewMethod(con, queueNumberDTO.getTransTypeCode().trim(),
						queueNumberDTO.getQueueService());
				runningValue = tempRunVal + 1;
			} else {
				runningValue = Long.valueOf(queueNumberDTO.getTokenNo().substring(3, 6)).longValue();
			}
			/* end change */

			/** get current queue number start **/
			queueNo = getFormattedQueueNumberNewMethod(queueNumberDTO.getTransTypeCode().trim(), runningValue);
			/** get current queue number end **/

			if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(TENDER)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT)) {

				updateCurrentQueueNumberNewMethod(con, Long.toString(runningValue),
						queueNumberDTO.getTransTypeCode().trim(), queueNumberDTO.getQueueService());// queueService-Normal(N),Priority(P)
			}

			/** update current queue number nt_r_queue_number_run_value end **/

			/** select vehicle number and permit number start **/
			if (!queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INQUIRY)) {
				QueueNumberDTO dto = new QueueNumberDTO();
				if (queueNumberDTO.getTransTypeCode() != null && !queueNumberDTO.getTransTypeCode().isEmpty()
						&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("")
						&& (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(RENEWAL)
								|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(PERMIT))) {
					dto = validateUserInputValuesForRNAndNP(queueNumberDTO);
				} else {
					if (queueNumberDTO.getTransTypeCode() != null && !queueNumberDTO.getTransTypeCode().isEmpty()
							&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("")
							&& (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_BUS)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER_BUS)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE_BUS)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_SERVICE)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_ROUTE)
									|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_END))) {

					} else {
						dto = validateUserInputValues(queueNumberDTO);
					}

				}

				if (dto != null) {
					if (dto.getVehicleNo() != null && !dto.getVehicleNo().isEmpty()
							&& !dto.getVehicleNo().trim().equalsIgnoreCase("")) {
						queueNumberDTO.setVehicleNo(dto.getVehicleNo());
					}

					if (dto.getPermitNo() != null && !dto.getPermitNo().isEmpty()
							&& !dto.getPermitNo().trim().equalsIgnoreCase("")) {
						queueNumberDTO.setPermitNo(dto.getPermitNo());
					}
				}
			}
			/** select vehicle number and permit number end **/

			/**
			 * in inspection after input vehicle number get application number from
			 * nt_t_pm_application table start
			 **/
			if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INSPECTION)) {
				QueueNumberDTO dto = new QueueNumberDTO();
				dto = validateUserInputValues(queueNumberDTO);
				if (dto != null) {
					queueNumberDTO.setApplicationNo(dto.getApplicationNo());
					queueNumberDTO.setPermitNo(dto.getPermitNo());
				}
			}
			/**
			 * in inspection after input vehicle number get application number from
			 * nt_t_pm_application table end
			 **/

			/**
			 * in tender get permit and vehicle number both and insert to Queue Master table
			 * start
			 **/
			if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_BUS)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE_BUS)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER_BUS)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_SERVICE)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_ROUTE)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_END)) {

				if (queueNumberDTO.getVehicleNo() == null || queueNumberDTO.getVehicleNo().isEmpty()
						|| queueNumberDTO.getPermitNo() == null || queueNumberDTO.getPermitNo().isEmpty()) {
					QueueNumberDTO tempQueueNumberDTO = retrieveVehicleAndPermitNumberForAmendment(con, queueNumberDTO);
					if (tempQueueNumberDTO != null) {
						if (tempQueueNumberDTO.getVehicleNo() != null && !tempQueueNumberDTO.getVehicleNo().isEmpty()) {
							queueNumberDTO.setVehicleNo(tempQueueNumberDTO.getVehicleNo());
						}
						if (tempQueueNumberDTO.getPermitNo() != null && !tempQueueNumberDTO.getPermitNo().isEmpty()) {
							queueNumberDTO.setPermitNo(tempQueueNumberDTO.getPermitNo());
						}
					}
				}
			}
			/**
			 * in tender get permit and vehicle number both and insert to Queue Master table
			 * end
			 **/

			/*
			 * Changes : When generate the renewal token, old record should be delete from
			 * the nt_m_queue_master table and insert into nt_h_queue_master table, before
			 * inserting the new token in the nt_m_queue_master table. Done By : Dinushi
			 * Ranasinghe Date : 08/10/2019 --START--
			 */
			if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(RENEWAL)) {
				String strAppNo = queueNumberDTO.getApplicationNo();
				PreparedStatement ps0 = null;
				PreparedStatement ps1 = null;

				String strInsSQl = "INSERT INTO  public.nt_h_queue_master "
						+ " (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, que_issued_by, que_service_type, que_counter_id, que_curr_workflow_id, que_skip_count, created_by, created_date, modified_by, modified_date, que_permit_no, que_vehicle_no, que_application_no, tender_ref_no, task_status, que_order, que_order_set, que_task_code, que_task_status,que_inspection_type ) "
						+ " SELECT que_seq, que_trn_type_code, que_date, que_number, que_issued_date, que_issued_by, que_service_type, que_counter_id, que_curr_workflow_id, que_skip_count, created_by, created_date, modified_by, modified_date, que_permit_no, que_vehicle_no, que_application_no, tender_ref_no, task_status, que_order, que_order_set, que_task_code, que_task_status,que_inspection_type  "
						+ " FROM public.nt_m_queue_master where que_application_no = '" + strAppNo + "'";

				String strDetSql = "delete from public.nt_m_queue_master where que_application_no = '" + strAppNo + "'";

				ps0 = con.prepareStatement(strInsSQl);
				ps1 = con.prepareStatement(strDetSql);
				ps0.executeUpdate();
				ps1.executeUpdate();

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_queue_master");

				String sql = "INSERT INTO public.nt_m_queue_master (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, "
						+ "que_issued_by, que_service_type, created_by, created_date, que_permit_no, que_vehicle_no, que_application_no, que_counter_id, que_task_code, que_task_status) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,'PR200','O')";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, queueNumberDTO.getTransTypeCode().trim());
				ps.setString(3, df.format(date));
				ps.setString(4, queueNo);
				ps.setString(5, df.format(date));
				ps.setString(6, loginUser);
				if (queueNumberDTO.getQueueService().equalsIgnoreCase("priority")) {
					ps.setString(7, "P");
				} else {
					ps.setString(7, "N");
				}
				ps.setString(8, loginUser);
				ps.setTimestamp(9, timestamp);
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					ps.setString(10, queueNumberDTO.getPermitNo().trim());
				} else {
					ps.setNull(10, Types.VARCHAR);
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					ps.setString(11, queueNumberDTO.getVehicleNo().trim());
				} else {
					ps.setNull(11, Types.VARCHAR);
				}
				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					ps.setString(12, queueNumberDTO.getApplicationNo());
				} else {
					ps.setNull(12, Types.VARCHAR);
				}
				if (queueNumberDTO.getTenderRefNo() != null && !queueNumberDTO.getTenderRefNo().isEmpty()
						&& !queueNumberDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {
					ps.setString(13, queueNumberDTO.getTenderRefNo());
				} else {
					ps.setNull(13, Types.VARCHAR);
				}

				/*
				 * Changes : When generate the renewal token, old record should be delete from
				 * the nt_m_queue_master table and insert into nt_h_queue_master table, before
				 * inserting the new token in the nt_m_queue_master table. Done By : Dinushi
				 * Ranasinghe Date : 08/10/2019 --END--
				 */
			} else if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INSPECTION_AMMEND)) {

				String strAppNo = queueNumberDTO.getApplicationNo();
				PreparedStatement ps0 = null;
				PreparedStatement ps1 = null;

				String strInsSQl = "INSERT INTO  public.nt_h_queue_master "
						+ " (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, que_issued_by, que_service_type, que_counter_id, que_curr_workflow_id, que_skip_count, created_by, created_date, modified_by, modified_date, que_permit_no, que_vehicle_no, que_application_no, tender_ref_no, task_status, que_order, que_order_set, que_task_code, que_task_status,que_inspection_type ) "
						+ " SELECT que_seq, que_trn_type_code, que_date, que_number, que_issued_date, que_issued_by, que_service_type, que_counter_id, que_curr_workflow_id, que_skip_count, created_by, created_date, modified_by, modified_date, que_permit_no, que_vehicle_no, que_application_no, tender_ref_no, task_status, que_order, que_order_set, que_task_code, que_task_status,que_inspection_type  "
						+ " FROM public.nt_m_queue_master where que_application_no = '" + strAppNo + "'";

				String strDetSql = "delete from public.nt_m_queue_master where que_application_no = '" + strAppNo + "'";

				ps0 = con.prepareStatement(strInsSQl);
				ps1 = con.prepareStatement(strDetSql);
				ps0.executeUpdate();
				ps1.executeUpdate();

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_queue_master");

				String sql = "INSERT INTO public.nt_m_queue_master (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, "
						+ "que_issued_by, que_service_type, created_by, created_date, que_permit_no, que_vehicle_no, que_application_no, que_counter_id, que_inspection_type) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, queueNumberDTO.getTransTypeCode().trim());
				ps.setString(3, df.format(date));
				ps.setString(4, queueNo);
				ps.setString(5, df.format(date));
				ps.setString(6, loginUser);
				if (queueNumberDTO.getQueueService().equalsIgnoreCase("priority")) {
					ps.setString(7, "P");
				} else {
					ps.setString(7, "N");
				}
				ps.setString(8, loginUser);
				ps.setTimestamp(9, timestamp);
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					ps.setString(10, queueNumberDTO.getPermitNo().trim());
				} else {
					ps.setNull(10, Types.VARCHAR);
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					ps.setString(11, queueNumberDTO.getVehicleNo().trim());
				} else {
					ps.setNull(11, Types.VARCHAR);
				}
				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					ps.setString(12, queueNumberDTO.getApplicationNo());
				} else {
					ps.setNull(12, Types.VARCHAR);
				}
				if (queueNumberDTO.getTenderRefNo() != null && !queueNumberDTO.getTenderRefNo().isEmpty()
						&& !queueNumberDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {
					ps.setString(13, queueNumberDTO.getTenderRefNo());
				} else {
					ps.setNull(13, Types.VARCHAR);
				}

				ps.setString(14, "AI");

			} else if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
					|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT)) {

				String transType = queueNumberDTO.getTransTypeCode().trim();
				transType = OTHER_INSPECTION;// override the transaction type with other inspection

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_queue_master");

				String sql = "INSERT INTO public.nt_m_queue_master (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, "
						+ "que_issued_by, que_service_type, created_by, created_date, que_permit_no, que_vehicle_no, que_application_no, que_counter_id,que_inspection_type) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, transType);
				ps.setString(3, df.format(date));
				ps.setString(4, queueNo);
				ps.setString(5, df.format(date));
				ps.setString(6, loginUser);
				if (queueNumberDTO.getQueueService().equalsIgnoreCase("priority")) {
					ps.setString(7, "P");
				} else {
					ps.setString(7, "N");
				}
				ps.setString(8, loginUser);
				ps.setTimestamp(9, timestamp);
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					ps.setString(10, queueNumberDTO.getPermitNo().trim());
				} else {
					ps.setNull(10, Types.VARCHAR);
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					ps.setString(11, queueNumberDTO.getVehicleNo().trim());
				} else {
					ps.setNull(11, Types.VARCHAR);
				}
				if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
						&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
					ps.setString(12, queueNumberDTO.getApplicationNo());
				} else {
					ps.setNull(12, Types.VARCHAR);
				}
				if (queueNumberDTO.getTenderRefNo() != null && !queueNumberDTO.getTenderRefNo().isEmpty()
						&& !queueNumberDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {
					ps.setString(13, queueNumberDTO.getTenderRefNo());
				} else {
					ps.setNull(13, Types.VARCHAR);
				}
				if (queueNumberDTO.getInspectionType() != null && !queueNumberDTO.getInspectionType().isEmpty()
						&& !queueNumberDTO.getInspectionType().trim().equalsIgnoreCase("")) {
					ps.setString(14, queueNumberDTO.getInspectionType());
				} else {
					ps.setNull(14, Types.VARCHAR);
				}

			} else {
				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_queue_master");

				String sql = "INSERT INTO public.nt_m_queue_master (que_seq, que_trn_type_code, que_date, que_number, que_issued_date, "
						+ "que_issued_by, que_service_type, created_by, created_date, que_permit_no, que_vehicle_no, que_application_no, que_counter_id,que_inspection_type) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, queueNumberDTO.getTransTypeCode().trim());
				ps.setString(3, df.format(date));
				ps.setString(4, queueNo);
				ps.setString(5, df.format(date));
				ps.setString(6, loginUser);
				if (queueNumberDTO.getQueueService().equalsIgnoreCase("priority")) {
					ps.setString(7, "P");
				} else {
					ps.setString(7, "N");
				}
				ps.setString(8, loginUser);
				ps.setTimestamp(9, timestamp);
				if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
						&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
					ps.setString(10, queueNumberDTO.getPermitNo().trim());
				} else {
					ps.setNull(10, Types.VARCHAR);
				}
				if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
						&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
					ps.setString(11, queueNumberDTO.getVehicleNo().trim());
				} else {
					ps.setNull(11, Types.VARCHAR);
				}

				if (!queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INSPECTION)) {
					if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
							&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
						ps.setString(12, queueNumberDTO.getApplicationNo());
					} else {
						ps.setNull(12, Types.VARCHAR);
					}
				} else {
					ps.setNull(12, Types.VARCHAR);
				}
				if (queueNumberDTO.getTenderRefNo() != null && !queueNumberDTO.getTenderRefNo().isEmpty()
						&& !queueNumberDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {
					ps.setString(13, queueNumberDTO.getTenderRefNo());
				} else {
					ps.setNull(13, Types.VARCHAR);
				}

				if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INSPECTION)) {
					ps.setString(14, "PI");
				} else {
					ps.setNull(14, Types.VARCHAR);
				}

			}
			/* End Change */

			ps.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("insertQueueDataIntoNT_M_QUEUE_MASTER error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("insertQueueDataIntoNT_M_QUEUE_MASTER end");
		return queueNo;
	}

	public String getFormattedQueueNumberNewMethod(String transtype, long runningValue) {
		String formattedString = "";
		String prefix = null;

		if (transtype != null && !transtype.isEmpty() && (transtype.equalsIgnoreCase(AMEND_BUS)
				|| transtype.isEmpty() && transtype.equalsIgnoreCase(AMEND_SERVICE)
				|| transtype.equalsIgnoreCase(AMEND_OWNER) || transtype.equalsIgnoreCase(AMEND_SERVICE_BUS)
				|| transtype.equalsIgnoreCase(AMEND_OWNER_BUS) || transtype.equalsIgnoreCase(AMMENDMENT_SERVICE)
				|| transtype.equalsIgnoreCase(AMMENDMENT_ROUTE) || transtype.equalsIgnoreCase(AMMENDMENT_END))) {
			transtype = AMMENDMENT;
		}

		if (transtype != null && !transtype.isEmpty() && transtype.equalsIgnoreCase(INSPECTION_AMMEND)) {
			transtype = INSPECTION;
		}

		if (transtype != null && !transtype.isEmpty() && (transtype.equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
				|| transtype.equalsIgnoreCase(OTHER_INSPECTION_INQUIRY))) {
			transtype = INSPECTION;
		}
		if (transtype != null && !transtype.isEmpty() && transtype.equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT)) {
			transtype = OTHER_INSPECTION_SITE_VISIT;
		}

		if (transtype.equalsIgnoreCase(RENEWAL)) {// RENEWAL - 04
			prefix = "RN-OP";
		} else if (transtype.equalsIgnoreCase(PERMIT)) {// NEW PERMIT - 03
			prefix = "NP-OP";
		} else if (transtype.equalsIgnoreCase(INSPECTION)) {// INSPECTION - 02
			prefix = "IN-OP";
		} else if (transtype.equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT)) {// OTHER_INSPECTION_SITE_VISIT
			prefix = "IN";
		} else if (transtype.equalsIgnoreCase(CANCEL)) {// CANCEL - 07
			prefix = "CN";
		} else if (transtype.equalsIgnoreCase(PAYMENT)) {// PAYMENT - 06
			prefix = "PY";
		} else if (transtype.equalsIgnoreCase(AMMENDMENT)) {// Amendments - 05
			prefix = "AM-OP";
		} else if (transtype.equalsIgnoreCase(INQUIRY)) {// Inquiry - 09
			prefix = "INQ";
		} else if (transtype.equalsIgnoreCase(TENDER)) {// tender - 01
			prefix = "TN";
		}

		// runningValue++;
		formattedString = String.format("%03d", runningValue);

		formattedString = prefix + "" + formattedString;

		return formattedString;
	}

	public long retrieveCurrentQueueNumberNewMethod(Connection con, String transType, String serviceType) {

		logger.info("retrieveCurrentQueueNumber start");

		ResultSet rs = null;
		PreparedStatement ps = null;
		long currQueueNum = 0l;

		if (transType != null && !transType.isEmpty() && (transType.equalsIgnoreCase(AMEND_BUS)
				|| transType.isEmpty() && transType.equalsIgnoreCase(AMEND_SERVICE)
				|| transType.equalsIgnoreCase(AMEND_OWNER) || transType.equalsIgnoreCase(AMEND_SERVICE_BUS)
				|| transType.equalsIgnoreCase(AMEND_OWNER_BUS) || transType.equalsIgnoreCase(AMMENDMENT_SERVICE)
				|| transType.equalsIgnoreCase(AMMENDMENT_ROUTE) || transType.equalsIgnoreCase(AMMENDMENT_END))) {
			transType = AMMENDMENT;
		}

		if (transType != null && !transType.isEmpty() && transType.equalsIgnoreCase(INSPECTION_AMMEND)) {
			transType = INSPECTION;
		}

		if (transType != null && !transType.isEmpty()
				&& (transType.equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
						|| transType.equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
						|| transType.equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT))) {
			transType = INSPECTION;
		}

		try {
			String sql = "SELECT normal_current_no_new , priority_current_no_new FROM public.nt_r_queue_number_run_value WHERE trans_code=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, transType);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (serviceType.equalsIgnoreCase("priority")) {
					// priority
					currQueueNum = rs.getLong("priority_current_no_new");
				} else {
					// serviceType = "N"; normal
					currQueueNum = rs.getLong("normal_current_no_new");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("retrieveCurrentQueueNumber error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}
		logger.info("retrieveCurrentQueueNumber end");

		return currQueueNum;

	}

	private void updateCurrentQueueNumberNewMethod(Connection con, String queueNo, String transType,
			String queueService) {
		logger.info("updateCurrentQueueNumber start");

		PreparedStatement ps = null;
		ResultSet rs = null;
		String setStatement = null;
		try {

			if (transType != null && !transType.isEmpty() && (transType.equalsIgnoreCase(AMEND_BUS)
					|| transType.equalsIgnoreCase(AMEND_SERVICE) || transType.equalsIgnoreCase(AMEND_OWNER)
					|| transType.equalsIgnoreCase(AMEND_SERVICE_BUS) || transType.equalsIgnoreCase(AMEND_OWNER_BUS)
					|| transType.equalsIgnoreCase(AMMENDMENT_SERVICE) || transType.equalsIgnoreCase(AMMENDMENT_ROUTE)
					|| transType.equalsIgnoreCase(AMMENDMENT_END))) {
				transType = AMMENDMENT;
			}

			if (transType != null && !transType.isEmpty() && transType.equalsIgnoreCase(INSPECTION_AMMEND)) {
				transType = INSPECTION;
			}

			if (transType != null && !transType.isEmpty()
					&& (transType.equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
							|| transType.equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
							|| transType.equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT))) {
				transType = INSPECTION;
			}

			if (queueService.equalsIgnoreCase("priority")) {
				setStatement = "priority_current_no_new";
			} else {
				setStatement = "normal_current_no_new";
			}

			String sql = "UPDATE public.nt_r_queue_number_run_value SET " + setStatement + "=? where trans_code=?";

			ps = con.prepareStatement(sql);

			ps.setString(1, queueNo);
			ps.setString(2, transType);

			ps.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("updateCurrentQueueNumber error");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		logger.info("updateCurrentQueueNumber end");
	}

	@Override
	public void updateQueueMasterTable(QueueNumberDTO dto, String tokenNo) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		logger.info("update queue master table after token generate -> Start");
		try {

			con = ConnectionManager.getConnection();
			String query = "UPDATE public. nt_t_main_queue_master SET status = 'P' , start_date_time = ? , token_no = ? "
					+ " WHERE queueno = ? AND section_code = ? AND status!='C';";

			ps = con.prepareStatement(query);
			ps.setTimestamp(1, timestamp);
			ps.setString(2, tokenNo);
			ps.setString(3, dto.getTokenNo());

			if (dto.getTransTypeCode().equalsIgnoreCase(RENEWAL)) {// RENEWAL - 04
				ps.setString(4, "OP");
			} else if (dto.getTransTypeCode().equalsIgnoreCase(PERMIT)) {// NEW PERMIT - 03
				ps.setString(4, "OP");
			} else if (dto.getTransTypeCode().equalsIgnoreCase(INSPECTION)) {// INSPECTION - 02
				ps.setString(4, "OP");
			} else {
				ps.setString(4, "OP");
			}

			ps.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("ERROR -> update queue master table after token generate ");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		logger.info("update queue master table after token generate -> end");

	}

	@Override
	public QueueNumberDTO getAmendmentVehicleNo(String permitNo, String applicationNO) {

		logger.info("getAmendmentVehicleNo start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		QueueNumberDTO dto = new QueueNumberDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM public.nt_m_amendments a "
					+ "INNER JOIN public.nt_t_pm_application d ON d.pm_application_no = a.amd_application_no "
					+ "WHERE amd_permit_no=? and amd_application_no=? and d.pm_status='P'  ORDER BY amd_created_date DESC";

			ps = con.prepareStatement(sql);
			ps.setString(1, permitNo);
			ps.setString(2, applicationNO);

			rs = ps.executeQuery();

			if (rs.next()) {

				dto.setPermitInfoFound(true);

				String transactionType = rs.getString("amd_trn_type");
				dto.setVehicleNo(rs.getString("amd_new_busno"));
				dto.setApplicationNo(rs.getString("amd_application_no"));
				
				/****commented due to new function Bus Changes for Ongoing Amendment Transactions on 21/12/2021****/
			/*	if (transactionType.equals("13") ||transactionType.equals("15") || transactionType.equals("16")) {
					dto.setVehicleNo(rs.getString("amd_new_busno"));
					dto.setApplicationNo(rs.getString("amd_application_no"));
				} else {
					dto.setVehicleNo(rs.getString("amd_existing_busno"));
					dto.setApplicationNo(rs.getString("amd_application_no"));
				}*/

			} else {
				dto.setPermitInfoFound(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("getAmendmentVehicleNo error");
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("getAmendmentVehicleNo end");
		return dto;
	}

	/* New queue generate CR end here */

	/* Other inspection CR start here */

	@Override
	public QueueNumberDTO getCallNextQueueNo() {

		logger.info("other inspection find queue no for call next start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String today = df.format(date);

		QueueNumberDTO dto = new QueueNumberDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = "select que_number,que_trn_type_code,que_date,que_curr_workflow_id, task_status , que_permit_no from public.nt_m_queue_master "
					+ "where que_inspection_type in('II','CI','SI') and ( task_status='O' or task_status IS null) and que_skip_count is null and "
					+ "to_char(created_date,'dd-MM-yyyy')= ? order by que_order asc";

			ps = con.prepareStatement(sql);
			ps.setString(1, today);
			rs = ps.executeQuery();

			if (rs.next()) {

				dto.setQueueNoFound(true);
				dto.setQueueNumber(rs.getString("que_number"));
				dto.setPermitNo(rs.getString("que_permit_no"));

			} else {
				dto.setQueueNoFound(false);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("other inspection find queue no for call next end");
		return dto;
	}

	@Override
	public int updateQueueNoSkipCount(String queueNO, String permitNo, String loginuser) {

		logger.info("other inspection skip queue no start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		int queueOut = 0;

		try {

			con = ConnectionManager.getConnection();
			String sql = "select *  from public.nt_m_queue_master "
					+ "where que_number =? and que_permit_no =? and que_date like ? ";

			ps = con.prepareStatement(sql);
			ps.setString(1, queueNO);
			ps.setString(2, permitNo);
			ps.setString(3, today + "%");
			rs = ps.executeQuery();

			if (rs.next()) {

				int queueSkipCount = 0;
				String count = rs.getString("que_skip_count");
				if (count == null) {
					queueSkipCount++;
				} else {
					int value = Integer.parseInt(count);
					queueSkipCount = value + 1;
				}

				String updateQuery = "UPDATE public.nt_m_queue_master SET  que_skip_count=?,  modified_by=? , modified_date=? "
						+ "where que_number =? and que_permit_no =? and que_date like ? ";

				ps2 = con.prepareStatement(updateQuery);
				ps2.setString(1, String.valueOf(queueSkipCount));
				ps2.setString(2, loginuser);
				ps2.setTimestamp(3, timestamp);
				ps2.setString(4, queueNO);
				ps2.setString(5, permitNo);
				ps2.setString(6, today + "%");

				int i = ps2.executeUpdate();

				if (i > 0) {
					queueOut = 1;
				} else {
					queueOut = -1;
				}

				con.commit();

			} else {
				queueOut = 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("other inspection skip queue no end");

		return queueOut;

	}

	/* Other inspection CR end here */

	/* Inspection renewal CR start here */
	@Override
	public QueueNumberDTO getInspectionProceedStatus(String vehicleNo, String inspectionType) {

		logger.info("other inspection get inspection status and proceed status start");
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		QueueNumberDTO dto = new QueueNumberDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = "select  pm_inspection_status , proceed_status from public.nt_t_pm_application "
					+ "where pm_vehicle_regno =? and inspection_type =? order by pm_created_date  desc limit 1 ";

			ps = con.prepareStatement(sql);
			ps.setString(1, vehicleNo);
			ps.setString(2, inspectionType);
			rs = ps.executeQuery();

			if (rs.next()) {
				dto.setInspectionStatus(rs.getString("pm_inspection_status"));
				dto.setProceedStatus(rs.getString("proceed_status"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		logger.info("other inspection get inspection status and proceed status end");
		return dto;
	}
	/* Inspection renewal CR end here */

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		QueueManagementServiceImpl.logger = logger;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getTender() {
		return TENDER;
	}

	public static String getInspection() {
		return INSPECTION;
	}

	public static String getPermit() {
		return PERMIT;
	}

	public static String getRenewal() {
		return RENEWAL;
	}

	public static String getAmmendment() {
		return AMMENDMENT;
	}

	public static String getPayment() {
		return PAYMENT;
	}

	public static String getCancel() {
		return CANCEL;
	}

	public static String getSurvey() {
		return SURVEY;
	}

	public static String getInquiry() {
		return INQUIRY;
	}

	public static String getAmendBus() {
		return AMEND_BUS;
	}

	public static String getAmendService() {
		return AMEND_SERVICE;
	}

	public static String getAmendOwner() {
		return AMEND_OWNER;
	}

	public static String getAmendOwnerBus() {
		return AMEND_OWNER_BUS;
	}

	public static String getAmendServiceBus() {
		return AMEND_SERVICE_BUS;
	}

	public static String getInspectionAmmend() {
		return INSPECTION_AMMEND;
	}

	public static String getAmmendmentService() {
		return AMMENDMENT_SERVICE;
	}

	public static String getAmmendmentRoute() {
		return AMMENDMENT_ROUTE;
	}

	public static String getAmmendmentEnd() {
		return AMMENDMENT_END;
	}

	public static String getOtherInspection() {
		return OTHER_INSPECTION;
	}

	public static String getOtherInspectionComplain() {
		return OTHER_INSPECTION_COMPLAIN;
	}

	public static String getOtherInspectionInquiry() {
		return OTHER_INSPECTION_INQUIRY;
	}

	public static String getOtherInspectionSiteVisit() {
		return OTHER_INSPECTION_SITE_VISIT;
	}

	public static String getRenewalPreviousTask() {
		return RENEWAL_PREVIOUS_TASK;
	}

	@Override
	public boolean checkTokenNumberAvailableForQueueNoGenerate(String tokenNumber, String tokenStatus) {
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);
		
		String countQuery = "SELECT COUNT(*) as row_count FROM public.nt_t_main_queue_master " + 
				"where queueno=? and to_char(token_created_date_time, 'DD/MM/YYYY') like '" + today + "%' and status =?;";
		
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(countQuery);) {

			preparedStatement.setString(1, tokenNumber);
			preparedStatement.setString(2, tokenStatus);
			ResultSet resultSet = preparedStatement.executeQuery();
			connection.commit();

			if (resultSet.next()) {
				
				int count = resultSet.getInt("row_count");
				if(count > 0) {
					return true;
				}
		
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("checkQueueNumberIsPending" + e);
		}

		return false;
	}
	
	

}
