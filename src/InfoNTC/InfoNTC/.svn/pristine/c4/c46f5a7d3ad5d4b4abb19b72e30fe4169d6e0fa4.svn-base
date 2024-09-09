package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CommitteeOrBoardApprovalDTO;
import lk.informatics.ntc.model.dto.TenderApplicantDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class TenderServiceImpl implements TenderService {
	/* Start Issue Tender Application Methods */

	@Override
	public List<TenderDTO> getApplicationTypeList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TenderDTO> data = new ArrayList<TenderDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_tender_application_type "
					+ "WHERE active='A' and code != '' and code is not null and "
					+ "description is not null and description != '' order by code;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			TenderDTO dto;

			while (rs.next()) {
				dto = new TenderDTO();
				dto.setTenderAppType(rs.getString("description"));
				dto.setTenderAppTypeCode(rs.getString("code"));
				data.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public boolean insertTaskDetailsSurvey(TenderDTO dto, String loginUSer, String taskCode, String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_survey_task_det");

			String sql = "INSERT INTO public.nt_t_survey_task_det "
					+ "(tsd_seq, tsd_task_code, tsd_status, created_by, created_date, tsd_tender_refno) "
					+ "VALUES(?,?,?,?,?,?); ";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, taskCode);
			stmt.setString(3, status);
			stmt.setString(4, loginUSer);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, dto.getTenderRefNo());

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return false;
	}

	@Override
	public boolean insertTaskDetails(TenderDTO dto, String loginUSer, String taskCode, String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_t_task_det "
					+ "(tsd_seq, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, dto.getTenderAppNo());
			stmt.setString(3, taskCode);
			stmt.setString(4, status);
			stmt.setString(5, loginUSer);
			stmt.setTimestamp(6, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return false;
	}

	@Override
	public boolean checkTaskDetailsInSurvey(TenderDTO dto, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tsd_task_code, tsd_tender_refno FROM public.nt_t_survey_task_det "
					+ "WHERE tsd_task_code=? and tsd_status=? and tsd_tender_refno=?  ";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, status);
			ps.setString(3, dto.getTenderRefNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isTaskAvailable = true;
			} else {
				isTaskAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskAvailable;
	}

	@Override
	public boolean checkTaskDetails(TenderDTO dto, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_t_task_det "
					+ "where tsd_task_code=? and tsd_status=? and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, status);
			ps.setString(3, dto.getTenderAppNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isTaskAvailable = true;
			} else {
				isTaskAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskAvailable;
	}

	@Override
	public boolean saveIssuteTenderDetails(TenderDTO tenderDTO, String applicationNo, String logUser, String queueNo) {
		Connection con = null;
		PreparedStatement stmt = null, ps =null;
		ResultSet rs = null;
		boolean isInsert = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		int c = 0;
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_tender_applicant");

			String query2 = "INSERT INTO public.nt_m_tender_applicant "
					+ "(seqno, tap_application_no, tap_reference_no,tap_remark,tap_status, tap_address01, "
					+ "tap_address02, tap_city, tap_applicant_name,tap_title, tap_nic, tap_contact_no, "
					+ "tap_application_receive_person, tap_application_receive_person_id_type,tap_application_receive_person_id_no, "
					+ "tap_created_by, tap_create_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(query2);
			stmt.setLong(1, seqNo);
			stmt.setString(2, applicationNo);
			stmt.setString(3, tenderDTO.getTenderRefNo());

			if (tenderDTO.getRemark() != null) {
				stmt.setString(4, tenderDTO.getRemark());
			} else {
				stmt.setString(4, null);
			}
			stmt.setString(5, "O");

			stmt.setString(6, tenderDTO.getAddressOne());
			stmt.setString(7, tenderDTO.getAddressTwo());
			stmt.setString(8, tenderDTO.getCity());
			stmt.setString(9, tenderDTO.getApplicantName());
			stmt.setString(10, tenderDTO.getTitleCode());
			stmt.setString(11, tenderDTO.getNicNo());
			stmt.setString(12, tenderDTO.getContactNo());

			if (tenderDTO.getApplicationRevPerson() != null) {
				stmt.setString(13, tenderDTO.getApplicationRevPerson());
			} else {
				stmt.setString(13, null);
			}

			if (tenderDTO.getRecPersonIdTypeCode() != null) {
				stmt.setString(14, tenderDTO.getRecPersonIdTypeCode());
			} else {
				stmt.setString(14, null);
			}

			if (tenderDTO.getRecPersonIdNo() != null) {
				stmt.setString(15, tenderDTO.getRecPersonIdNo());
			} else {
				stmt.setString(15, null);
			}

			stmt.setString(16, logUser);
			stmt.setTimestamp(17, timestamp);

			c = stmt.executeUpdate();

			if (c > 0) {
				isInsert = true;
				String sql3 = "update nt_m_queue_master " + "set que_skip_count= 0 "
						+ " WHERE que_number=? and que_date like '" + today + "%'";
				
				ps = con.prepareStatement(sql3);

				ps.setString(1, queueNo);

				ps.executeUpdate();
				
			} else {
				isInsert = false;
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isInsert;
	}

	@Override
	public String generateApplicationNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;
		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strAppNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tap_application_no, tap_create_date FROM public.nt_m_tender_applicant ORDER BY tap_create_date desc  LIMIT 1 ;";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("tap_application_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "TAP" + currYear + ApprecordcountN;
				} else {
					String number = result.substring(5, 10);
					int returncountvalue = Integer.valueOf(number) + 1;

					String ApprecordcountN = "";

					if (returncountvalue >= 10 && returncountvalue < 100) {
						ApprecordcountN = "000" + returncountvalue;
					} else if (returncountvalue >= 100 && returncountvalue < 1000) {
						ApprecordcountN = "00" + returncountvalue;
					} else if (returncountvalue >= 1000 && returncountvalue < 10000) {
						ApprecordcountN = "0" + returncountvalue;
					} else if (returncountvalue >= 10000 && returncountvalue < 100000) {
						ApprecordcountN = Integer.toString(returncountvalue);
					} else {
						ApprecordcountN = "0000" + returncountvalue;
					}
					strAppNo = "TAP" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "TAP" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	@Override
	public boolean isVoucherGenerated(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query2 = "SELECT tap_application_no, tap_reference_no, tap_voucher_no FROM public.nt_m_tender_applicant "
					+ "WHERE tap_reference_no=? ";

			stmt = con.prepareStatement(query2);

			stmt.setString(1, tenderDTO.getVoucherNo());

			int c = stmt.executeUpdate();

			if (c > 0) {
				isGenerated = true;
			} else {
				isGenerated = false;
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isGenerated;
	}

	@Override
	public String getTenderRefNo(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String tenderReFno = null;
		try {

			con = ConnectionManager.getConnection();
			String query = "SELECT tap_reference_no FROM public.nt_m_tender_applicant WHERE tap_application_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTenderAppNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				tenderReFno = rs.getString("tap_reference_no");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderReFno;
	}

	@Override
	public TenderDTO fillApplicantDetails(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TenderDTO dto = new TenderDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tap_remark, tap_address01, tap_address02, tap_city, tap_applicant_name, "
					+ "tap_title, tap_nic, tap_contact_no, tap_application_receive_person, tap_application_receive_person_id_type, "
					+ "tap_application_receive_person_id_no " + "FROM public.nt_m_tender_applicant where tap_nic=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getNicNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setTitleCode(rs.getString("tap_title"));
				dto.setAddressOne(rs.getString("tap_address01"));
				dto.setAddressTwo(rs.getString("tap_address02"));
				dto.setCity(rs.getString("tap_city"));
				dto.setApplicantName(rs.getString("tap_applicant_name"));
				dto.setContactNo(rs.getString("tap_contact_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public TenderDTO getApplicantDetails(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TenderDTO dto = new TenderDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tap_application_no, tap_remark, tap_voucher_no, tap_address01, tap_address02, tap_city, tap_applicant_name, "
					+ "tap_title, tap_nic, tap_contact_no, tap_application_receive_person, tap_application_receive_person_id_type, "
					+ "tap_application_receive_person_id_no "
					+ "FROM public.nt_m_tender_applicant where tap_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTenderAppNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setTitleCode(rs.getString("tap_title"));
				dto.setTenderAppNo(rs.getString("tap_application_no"));
				dto.setRemark(rs.getString("tap_remark"));
				dto.setVoucherNo(rs.getString("tap_voucher_no"));
				dto.setAddressOne(rs.getString("tap_address01"));
				dto.setAddressTwo(rs.getString("tap_address02"));
				dto.setCity(rs.getString("tap_city"));
				dto.setApplicantName(rs.getString("tap_applicant_name"));
				dto.setNicNo(rs.getString("tap_nic"));
				dto.setContactNo(rs.getString("tap_contact_no"));
				dto.setApplicationRevPerson(rs.getString("tap_application_receive_person"));
				dto.setRecPersonIdTypeCode(rs.getString("tap_application_receive_person_id_type"));
				dto.setRecPersonIdNo(rs.getString("tap_application_receive_person_id_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public boolean isReciptGenerated(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		boolean wasVoucherGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_application_no, pav_voucher_no, pav_payment_type FROM public.nt_m_payment_voucher "
					+ "WHERE pav_application_no=? and pav_payment_type='V' and pav_approved_status='A' and pav_receipt_no is not null;";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				wasVoucherGenerated = true;

			} else {
				wasVoucherGenerated = false;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return wasVoucherGenerated;
	}

	@Override
	public boolean isPaymentDone(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		boolean wasVoucherGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_application_no, pav_voucher_no, pav_payment_type FROM public.nt_m_payment_voucher "
					+ "WHERE pav_application_no=? and pav_payment_type='V' and pav_approved_status='A';";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				wasVoucherGenerated = true;

			} else {
				wasVoucherGenerated = false;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return wasVoucherGenerated;
	}

	@Override
	public boolean isVoucherGenerated(String appNo) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		boolean wasVoucherGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT pav_application_no, pav_voucher_no, pav_payment_type FROM public.nt_m_payment_voucher "
					+ "WHERE pav_application_no=? and pav_payment_type='V';";

			ps = con.prepareStatement(query);
			ps.setString(1, appNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				wasVoucherGenerated = true;

			} else {
				wasVoucherGenerated = false;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return wasVoucherGenerated;
	}

	@Override
	public TenderDTO getTenderDetails(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TenderDTO dto = new TenderDTO();
		Timestamp ts = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tdm_reference_no, tdm_description, tdm_closing_date, tdm_initiate_closing_date, tdm_time,tdm_publish_date "
					+ "FROM public.nt_m_tender_management where tdm_reference_no=?  ;";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTenderRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				ts = rs.getTimestamp("tdm_closing_date");

				Date date = new Date();
				date.setTime(ts.getTime());
				String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);

				dto.setStringCloseDate(formattedDate);

				dto.setTenderDes(rs.getString("tdm_description"));
				dto.setTime(rs.getString("tdm_time"));
				dto.setPublishDate(rs.getTimestamp("tdm_publish_date"));
				dto.setIniCloseDate(rs.getString("tdm_initiate_closing_date"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public boolean alreadyGeneratedTenderApplication(TenderDTO tenderDTO) {

		return false;
	}

	public String getreceivefromcode(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String retReceive = "";
		try {

			con = ConnectionManager.getConnection();

			String query = "select description " + "from nt_r_tender_application_type " + "where code='" + code + "'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				retReceive = rs.getString("description");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return retReceive;
	}

	@Override
	public String getTitlefromcode(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String retTitle = "";
		try {

			con = ConnectionManager.getConnection();

			String query = "select description " + "from nt_r_title " + "where code='" + code + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				retTitle = rs.getString("description");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return retTitle;
	}

	@Override
	public List<TenderDTO> getTitleList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TenderDTO> data = new ArrayList<TenderDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, active FROM public.nt_r_title "
					+ "WHERE  active='A' and description != '' and description is not null order by description ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			TenderDTO dto;

			while (rs.next()) {
				dto = new TenderDTO();
				dto.setTitleCode(rs.getString("code"));
				dto.setTitleDescription(rs.getString("description"));
				data.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<TenderDTO> getIdTypeList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TenderDTO> data = new ArrayList<TenderDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, active FROM public.nt_r_person_id_types "
					+ "WHERE description != '' and description is not null and active ='A' order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			TenderDTO dto;

			while (rs.next()) {
				dto = new TenderDTO();
				dto.setRecPersonIdTypeCode(rs.getString("code"));
				dto.setRecPersonIdTypeDescription(rs.getString("description"));
				data.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<TenderDTO> getTenderRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TenderDTO> data = new ArrayList<TenderDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tdm_reference_no FROM public.nt_m_tender_management "
					+ "where tdm_reference_no != '' and tdm_reference_no is not null  and tdm_ad_approve_status='A' order by tdm_reference_no";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			TenderDTO dto;

			while (rs.next()) {
				dto = new TenderDTO();
				dto.setTenderRefNo(rs.getString("tdm_reference_no"));
				data.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	/* End Issue Tender Application Methods */

	/* Start Create Tender Methods */

	@Override
	public boolean updateTrafficProposalNo(TenderDTO dto, String logingUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isUpdate = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			String query2 = "UPDATE public.nt_t_traffic_proposal SET status='C', modify_by=?, nodify_date=? where proposal_no= ? ";

			stmt = con.prepareStatement(query2);

			stmt.setString(3, dto.getTrafficProposalNo());
			stmt.setString(1, logingUser);
			stmt.setTimestamp(2, timestamp);

			int c = stmt.executeUpdate();

			if (c > 0) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isUpdate;
	}

	@Override
	public String getLastSerialNo(TenderDTO dto) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;
		String strAppNo = "";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tnd_serial_no FROM public.nt_t_tender_details ORDER BY tnd_create_date desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("tnd_serial_no");
			}

			if (result != null) {

				String number = result.substring(3, 6);
				int returncountvalue = Integer.valueOf(number) + 1;

				String ApprecordcountN = "";

				if (returncountvalue >= 1 && returncountvalue < 10) {
					ApprecordcountN = "00" + returncountvalue;
				} else if (returncountvalue >= 10 && returncountvalue < 100) {
					ApprecordcountN = "0" + returncountvalue;
				} else {
					ApprecordcountN = "00" + returncountvalue;
				}
				strAppNo = "SLN" + ApprecordcountN;

			} else
				strAppNo = "SLN" + "001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	@Override
	public String generateReferenceNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;
		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strAppNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tdm_reference_no FROM public.nt_m_tender_management ORDER BY tdm_created_date desc  LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("tdm_reference_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "TRN" + currYear + ApprecordcountN;
				} else {
					String number = result.substring(5, 10);
					int returncountvalue = Integer.valueOf(number) + 1;

					String ApprecordcountN = "";

					if (returncountvalue >= 10 && returncountvalue < 100) {
						ApprecordcountN = "000" + returncountvalue;
					} else if (returncountvalue >= 100 && returncountvalue < 1000) {
						ApprecordcountN = "00" + returncountvalue;
					} else if (returncountvalue >= 1000 && returncountvalue < 10000) {
						ApprecordcountN = "0" + returncountvalue;
					} else if (returncountvalue >= 10000 && returncountvalue < 100000) {
						ApprecordcountN = Integer.toString(returncountvalue);
					} else {
						ApprecordcountN = "0000" + returncountvalue;
					}
					strAppNo = "TRN" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "TRN" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	@Override
	public boolean insertINTOTenderDetails(TenderDTO tenderDTO, String logedUser, List<TenderDTO> advertismentList) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isInsert = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		int c = 0;

		try {

			con = ConnectionManager.getConnection();

			for (int i = 0; i < advertismentList.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_tender_details");

				String query2 = "INSERT INTO public.nt_t_tender_details "
						+ "(seqno, tnd_reference_no,tnd_serial_no, tnd_route_no, tnd_via, tnd_type_of_service, tnd_one_way_fare,"
						+ "tnd_turns_perday, tnd_parallel_roads, tnd_effective_routes, tnd_created_by, tnd_create_date,tnd_departure, tnd_arrival,tnd_no_of_permits,tnd_treasure_holder_price) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

				stmt = con.prepareStatement(query2);
				stmt.setLong(1, seqNo);
				stmt.setString(2, tenderDTO.getTenderRefNo());
				stmt.setString(3, advertismentList.get(i).getSlNumber());
				stmt.setString(4, advertismentList.get(i).getRouteNo());
				stmt.setString(5, advertismentList.get(i).getVia());
				stmt.setString(6, advertismentList.get(i).getTypeOfServices());
				stmt.setBigDecimal(7, advertismentList.get(i).getOneWayBusFare());
				stmt.setInt(8, advertismentList.get(i).getTurnsPerDay());
				stmt.setString(9, advertismentList.get(i).getParallelRoads());
				stmt.setString(10, advertismentList.get(i).getEffectiveRoutes());
				stmt.setString(11, logedUser);
				stmt.setTimestamp(12, timestamp);
				stmt.setString(13, advertismentList.get(i).getDeparture());
				stmt.setString(14, advertismentList.get(i).getArrival());
				stmt.setInt(15, advertismentList.get(i).getNoOfPermits());
				stmt.setBigDecimal(16, advertismentList.get(i).getTreasureHolderPrice());

				c = stmt.executeUpdate();

			}

			if (c > 0) {
				isInsert = true;
			} else {
				isInsert = false;
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isInsert;
	}

	@Override
	public boolean insertINTOTenderManagement(TenderDTO tenderDTO, String logUser, String dateString) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isInsert = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_m_tender_management");

			String query2 = "INSERT INTO public.nt_m_tender_management "
					+ "(seqno, tdm_traffic_proposal_no, tdm_reference_no, tdm_description, tdm_closing_date, tdm_time,"
					+ "tdm_status,"
					+ "tdm_ad_approve_status ,tdm_created_by, tdm_created_date, tdm_surveyno,tdm_initiate_closing_date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(query2);
			stmt.setLong(1, seqNo);
			stmt.setString(2, tenderDTO.getTrafficProposalNo());
			stmt.setString(3, tenderDTO.getTenderRefNo());
			stmt.setString(4, tenderDTO.getTenderDes());
			stmt.setTimestamp(5, tenderDTO.getCloseDate());
			stmt.setString(6, tenderDTO.getTime());
			stmt.setString(7, "O");
			stmt.setString(8, null);
			stmt.setString(9, logUser);
			stmt.setTimestamp(10, timestamp);
			stmt.setString(11, tenderDTO.getSurveyNo());
			stmt.setString(12, dateString);

			int c = stmt.executeUpdate();

			if (c > 0) {
				isInsert = true;
			} else {
				isInsert = false;
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isInsert;
	}

	@Override
	public List<TenderDTO> getRouteDetails(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderDTO> paymentVoucherList = new ArrayList<TenderDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.proposal_no, a.survey_no, b.srd_route_no, b.srd_via, g.description as ini_service_type,"
					+ "c.ini_one_way_fare, b.srd_no_of_permits, b.srd_effective_route, "
					+ "b.srd_origin,b.srd_destination FROM public.nt_t_traffic_proposal a "
					+ "inner join public.nt_t_survey_route_detail b on b.srd_survey_no=a.survey_no "
					+ "inner join public.nt_t_initiate_survey c on c.ini_surveyno=a.survey_no "
					+ "left outer join public.nt_r_service_types g on g.code=c.ini_service_type where a.proposal_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTrafficProposalNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderDTO e = new TenderDTO();
				e.setTrafficProposalNo(rs.getString("proposal_no"));
				e.setSurveyNo(rs.getString("survey_no"));
				e.setRouteNo(rs.getString("srd_route_no"));
				e.setVia(rs.getString("srd_via"));
				e.setTypeOfServices(rs.getString("ini_service_type"));
				e.setOneWayBusFare(rs.getBigDecimal("ini_one_way_fare"));
				e.setNoOfPermits(rs.getInt("srd_no_of_permits"));
				e.setDeparture(rs.getString("srd_destination"));
				e.setArrival(rs.getString("srd_origin"));
				e.setEffectiveRoutes(rs.getString("srd_effective_route"));
				paymentVoucherList.add(e);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return paymentVoucherList;
	}

	@Override
	public String getSurveyNo(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String surveyNo = null;

		try {

			con = ConnectionManager.getConnection();
			String query = "SELECT survey_no  FROM public.nt_t_traffic_proposal " + " where proposal_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTrafficProposalNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				surveyNo = rs.getString("survey_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return surveyNo;
	}

	@Override
	public List<TenderDTO> getTrafficProposeNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TenderDTO> data = new ArrayList<TenderDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select proposal_no, status FROM public.nt_t_traffic_proposal "
					+ "inner join public.nt_t_initiate_survey on nt_t_initiate_survey.ini_surveyno=nt_t_traffic_proposal.survey_no "
					+ "where nt_t_traffic_proposal.status='O' and nt_t_initiate_survey.trp_is_board_approve='A' and proposal_no is not null and proposal_no !='' and "
					+ "nt_t_initiate_survey.ini_tender_process_require='Y' order by proposal_no ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			TenderDTO dto;

			while (rs.next()) {
				dto = new TenderDTO();
				dto.setTrafficProposalNo(rs.getString("proposal_no"));
				data.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	/* End Create Tender Methods */

	// Print Agreement

	public List<TenderDTO> getTenderApplicationNoList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TenderDTO> tenderAppNoList = new ArrayList<TenderDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tap_application_no,tap_reference_no FROM public.nt_m_tender_applicant "
					+ "where tap_board_approval_status='A'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			TenderDTO dto;

			while (rs.next()) {
				dto = new TenderDTO();
				dto.setTenderAppNo(rs.getString("tap_application_no"));
				dto.setTenderRefNo(rs.getString("tap_reference_no"));
				tenderAppNoList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderAppNoList;
	}

	@Override
	public boolean updatePrintAgreementData(TenderDTO tenderDto, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_tender_applicant SET tap_bank_slip_reference_no= ?,tap_modified_by= ?,tap_modified_date =? WHERE tap_application_no=? and tap_board_approval_status='A';";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, tenderDto.getTenderBankSlipRefNo());
			stmt.setString(2, user);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, tenderDto.getTenderAppNo());

			int data = stmt.executeUpdate();

			if (data > 0) {
				success = true;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return success;
	}

	/// publish Tender Management
	@Override
	public List<TenderDTO> updatePostponeReason(String selectTenderDate, TenderDTO tenderDTO, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date dateT = new java.util.Date();
		Timestamp timestamp = new Timestamp(dateT.getTime());
		Date newTenderDate = (Date) tenderDTO.getNewTenderDate();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = dateFormat.format(newTenderDate);

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_tender_manage_postpone");
			String query2 = "INSERT INTO  public.nt_m_tender_manage_postpone (seqno, tmd_closing_date,tmd_reference_no,tmd_postponed_reason,tmd_ad_approve_status,tmd_created_by,tmd_created_date) values(?,?,?,?,?,?,?) ; ";

			ps = con.prepareStatement(query2);
			ps.setLong(1, seqNo);
			ps.setString(2, strDate);
			ps.setString(3, tenderDTO.getTenderRefNo());
			ps.setString(4, tenderDTO.getPostPoneReason());
			ps.setString(5, "P");
			ps.setString(6, loginUser);
			ps.setTimestamp(7, timestamp);

			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;
	}

	// publish tender management
	@Override
	public List<TenderDTO> showDataToGrid(TenderDTO tenderDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderDTO> showDetails = new ArrayList<TenderDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tmd_postponed_reason,tmd_closing_date,tmd_ad_approve_status  from public.nt_m_tender_manage_postpone where tmd_reference_no=?order by tmd_closing_date; ";
			TenderDTO e;
			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTenderRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				e = new TenderDTO();

				e.setPostPoneReason(rs.getString("tmd_postponed_reason"));
				String new_tender_dateString = rs.getString("tmd_closing_date");
				if (new_tender_dateString != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date setNewTenderDate = originalFormat.parse(new_tender_dateString);
					e.setNewTenderDate(setNewTenderDate);
					e.setNewTenderDateString(new_tender_dateString);
					;
				}
				if (rs.getString("tmd_ad_approve_status").equals("A")) {

					e.setTenderStatus("Approved");

				} else if (rs.getString("tmd_ad_approve_status").equals("R")) {
					e.setTenderStatus("Rejected");
				} else {

					e.setTenderStatus("Pending");
				}

				showDetails.add(e);

			}
			return showDetails;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public String getApproveRejectStatus(String tenderRefNo, String newTenderDateString) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {
			con = ConnectionManager.getConnection();

			String query = " select tmd_ad_approve_status  from public.nt_m_tender_manage_postpone where tmd_reference_no =? and tmd_closing_date=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderRefNo);
			ps.setString(2, newTenderDateString);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("tmd_ad_approve_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return status;
	}

	@Override
	public List<TenderDTO> updateApprovedData(TenderDTO tenderDTO, String Status, String strDate, String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String WHERE_SQL = "";

		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tmd_reference_no  = " + "'" + tenderDTO.getTenderRefNo() + "'";
		}
		if (strDate != null && !strDate.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND tmd_closing_date  = " + "'" + strDate + "'";
		}

		try {
			con = ConnectionManager.getConnection();
			String query2 = "Update public.nt_m_tender_manage_postpone SET tmd_ad_approve_status =?,tmd_ad_approve_by  =?,tmd_ad_approve_date  =?  "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			ps.setString(1, Status);

			ps.setString(2, loginUser);
			ps.setTimestamp(3, timestamp);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;
	}

	/*  8.9.5.3 Refunding Non-Elected Bidders */

	@Override
	public List<TenderDTO> getRefundableDetails(TenderDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderDTO> list = new ArrayList<TenderDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct tap_application_no,tap_second_env_remark, tap_reference_no, "
					+ "tap_applicant_name,tap_bid_price, "
					+ "nt_t_tender_details.tnd_no_of_permits, tap_board_approval_status,nt_t_tender_details.tnd_departure, nt_t_tender_details.tnd_arrival, "
					+ "nt_t_tender_details.tnd_treasure_holder_price, nt_t_tender_details.tnd_serial_no, nt_t_tender_details.tnd_route_no "
					+ "FROM public.nt_m_tender_applicant "
					+ "inner join public.nt_t_tender_details on nt_t_tender_details.seqno = nt_m_tender_applicant.tap_app_pre_route_seq "
					+ "WHERE ((tap_reference_no=? ) and (tap_board_approval_status is null or tap_board_approval_status= 'R')); ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getTenderRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderDTO e = new TenderDTO();
				e.setTenderAppNo(rs.getString("tap_application_no"));
				e.setSpecialRemark(rs.getString("tap_second_env_remark"));
				e.setTenderRefNo(rs.getString("tap_reference_no"));
				e.setTreasureHolderPrice(rs.getBigDecimal("tnd_treasure_holder_price"));
				e.setApplicantName(rs.getString("tap_applicant_name"));
				e.setBidPrice(rs.getBigDecimal("tap_bid_price"));
				e.setDeparture(rs.getString("tnd_departure"));
				e.setArrival(rs.getString("tnd_arrival"));
				e.setSlNumber(rs.getString("tnd_serial_no"));
				e.setRouteNo(rs.getString("tnd_route_no"));
				e.setNoOfPermits(rs.getInt("tnd_no_of_permits"));
				list.add(e);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	/* 8.9.5.3 Generate Refund Voucher */

	@Override
	public List<TenderDTO> getAccountNo(TenderDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderDTO> list = new ArrayList<TenderDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tct_trn_type_code,  tct_account_no, tct_amount FROM public.nt_r_trn_charge_type "
					+ "WHERE tct_trn_type_code=? order by tct_account_no ; ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getTransCode());
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderDTO e = new TenderDTO();
				e.setAccountNo(rs.getString("tct_account_no"));
				list.add(e);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public List<TenderDTO> getTenderRefNoForRefund(TenderDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderDTO> list = new ArrayList<TenderDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct tap_reference_no FROM public.nt_m_tender_applicant "
					+ "WHERE tap_reference_no != '' and tap_reference_no is not null order by tap_reference_no;  ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderDTO e = new TenderDTO();
				e.setTenderRefNo(rs.getString("tap_reference_no"));
				list.add(e);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public List<TenderDTO> getRejectedApplicationNo(TenderDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderDTO> list = new ArrayList<TenderDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct tap_application_no, tap_reference_no FROM public.nt_m_tender_applicant "
					+ "WHERE ((tap_board_approval_status is null or  tap_board_approval_status !='Y') and tap_reference_no=?)";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getTenderRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderDTO e = new TenderDTO();
				e.setTenderAppNo(rs.getString("tap_application_no"));
				list.add(e);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public BigDecimal getAmount(TenderDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BigDecimal price = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select  tap_refundable_deposit_amount FROM public.nt_m_tender_applicant "
					+ "WHERE tap_application_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getTenderAppNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				price = rs.getBigDecimal("tap_refundable_deposit_amount");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return price;
	}
	// get edit postpone date for publish tender management

	@Override
	public boolean isAlreadyGenerateVoucher(TenderDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;

		boolean wasVoucherGenerated = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_tender_applicant "
					+ "WHERE tap_application_no=? and tap_refund_voucher_generated='Y'";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getTenderAppNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {

				wasVoucherGenerated = true;

			} else {

				wasVoucherGenerated = false;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return wasVoucherGenerated;
	}

	@Override
	public String generateReferenceNoForRefund() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strAppNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT pav_voucher_no " + " FROM public.nt_m_payment_voucher "
					+ " ORDER BY pav_created_date desc " + " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("pav_voucher_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "VRN" + currYear + ApprecordcountN;
				} else {
					String number = result.substring(5, 10);
					int returncountvalue = Integer.valueOf(number) + 1;

					String ApprecordcountN = "";

					if (returncountvalue >= 10 && returncountvalue < 100) {
						ApprecordcountN = "000" + returncountvalue;
					} else if (returncountvalue >= 100 && returncountvalue < 1000) {
						ApprecordcountN = "00" + returncountvalue;
					} else if (returncountvalue >= 1000 && returncountvalue < 10000) {
						ApprecordcountN = "0" + returncountvalue;
					} else if (returncountvalue >= 10000 && returncountvalue < 100000) {
						ApprecordcountN = Integer.toString(returncountvalue);
					} else {
						ApprecordcountN = "0000" + returncountvalue;
					}
					strAppNo = "VRN" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "VRN" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strAppNo;
	}

	@Override
	public boolean generateVoucher(TenderDTO dto, String value, String logUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isVousherGenerated = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_payment_voucher");

			String query = "INSERT INTO public.nt_m_payment_voucher " + "(pav_seq_no, pav_application_no, "
					+ "pav_voucher_no,  pav_is_voucher_generated, "
					+ "pav_total_amount,pav_tran_type, pav_created_by, pav_created_date,pav_payment_type,pav_approved_status) "
					+ "VALUES(? , ? , ? , ? , ? ,  ? , ? , ? , ?,'P'); ";

			ps = con.prepareStatement(query);

			ps.setLong(1, seqNo);

			ps.setString(2, dto.getTenderAppNo());
			ps.setString(3, value);

			ps.setString(4, "Y");
			ps.setBigDecimal(5, dto.getAmount());
			ps.setString(6, dto.getTransCode());
			ps.setString(7, logUser);
			ps.setTimestamp(8, timestamp);

			ps.setString(9, "V");

			int insert = ps.executeUpdate();

			if (insert > 0) {
				isVousherGenerated = true;
			} else {
				isVousherGenerated = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isVousherGenerated;
	}

	@Override
	public boolean updateTenderApplicant(TenderDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isUpdated = false;

		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_tender_applicant " + "SET  tap_refund_voucher_generated='Y' "
					+ "WHERE tap_application_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, dto.getTenderAppNo());

			int insert = ps.executeUpdate();

			if (insert > 0) {
				isUpdated = true;
			} else {
				isUpdated = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isUpdated;
	}

	@Override
	public TenderDTO getGeneratedData(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TenderDTO dto = new TenderDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT  refund_voucher_no, refund_account_no, refund_amount, refund_remarks "
					+ "FROM public.nt_t_fund_voucher where refund_application_no=? ; ";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTenderAppNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setRefundvoucherNo(rs.getString("refund_voucher_no"));
				dto.setAccountNo(rs.getString("refund_account_no"));
				dto.setAmount(rs.getBigDecimal("refund_amount"));
				dto.setVoucherRemark(rs.getString("refund_remarks"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public boolean updateVoucherDetails(TenderDTO dto, String value, String logUser, LocalDateTime ldt) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isUpdateTask = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "public.seq_nt_t_fund_voucher");

			String query2 = "INSERT INTO public.nt_t_fund_voucher "
					+ "(seqno, refund_transaction_type, refund_application_no, refund_date, refund_voucher_no, refund_account_no, "
					+ "refund_amount, refund_remarks, created_by, created_date, modify_by, modify_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(query2);

			stmt.setLong(1, seqNo);
			stmt.setString(2, "01");
			stmt.setString(3, dto.getTenderAppNo());
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, value);
			stmt.setString(6, dto.getAccountNo());
			stmt.setBigDecimal(7, dto.getAmount());
			stmt.setString(8, dto.getVoucherRemark());
			stmt.setString(9, logUser);
			stmt.setTimestamp(10, timestamp);
			stmt.setString(11, null);
			stmt.setTimestamp(12, null);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isUpdateTask = true;
			} else {
				isUpdateTask = false;
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isUpdateTask;
	}

	@Override
	public TenderDTO getEditPostpone(TenderDTO tenderDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select   tmd_postponed_reason,tmd_closing_date ,tmd_ad_approve_status  from public.nt_m_tender_manage_postpone where tmd_reference_no=?   order by tmd_closing_date desc limit 1  ";

			ps = con.prepareStatement(query);

			ps.setString(1, tenderDTO.getTenderRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				tenderDTO.setPostPoneReason(rs.getString("tmd_postponed_reason"));
				tenderDTO.setNewTenderDateString(rs.getString("tmd_closing_date"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderDTO;
	}

	// publish tender management
	@Override
	public List<TenderDTO> updateEditData(String selectTenderDate, String postponeReason, TenderDTO editValues,
			String loginUser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		Date newTenderDate = (Date) editValues.getNewTenderDate();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = dateFormat.format(newTenderDate);
		String WHERE_SQL = "";

		if (editValues.getTenderRefNo() != null && !editValues.getTenderRefNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tmd_reference_no  = " + "'" + editValues.getTenderRefNo() + "'";
		}
		if (selectTenderDate != null && !selectTenderDate.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND tmd_closing_date  = " + "'" + selectTenderDate + "'";
		}
		if (postponeReason != null && !postponeReason.equals("")) {
			WHERE_SQL = WHERE_SQL + " AND tmd_postponed_reason  = " + "'" + postponeReason + "'";
		}

		try {
			con = ConnectionManager.getConnection();
			String query2 = "Update public.nt_m_tender_manage_postpone SET tmd_postponed_reason=?,tmd_closing_date=?,tmd_modify_by=? ,tmd_modify_date=?  "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			ps.setString(1, editValues.getPostPoneReason());
			ps.setString(2, strDate);
			ps.setString(3, loginUser);

			ps.setTimestamp(4, timestamp);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;

	}

	// update publish closing date
	@Override
	public List<TenderDTO> updateDates(TenderDTO tenderDTO, String loginuser) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String WHERE_SQL = "";

		if (tenderDTO.getTenderRefNo() != null && !tenderDTO.getTenderRefNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE tdm_reference_no  = " + "'" + tenderDTO.getTenderRefNo() + "'";
		}

		try {
			con = ConnectionManager.getConnection();
			String query2 = "Update public.nt_m_tender_management SET tdm_old_close_date =?,tdm_closing_date=?,tdm_modify_by=?,tdm_modify_date=?  "
					+ WHERE_SQL;

			ps = con.prepareStatement(query2);
			java.util.Date utilDate = tenderDTO.getNewTenderDate();
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

			ps.setString(1, tenderDTO.getStringCloseDate());
			ps.setDate(2, sqlDate);
			ps.setString(3, loginuser);
			ps.setTimestamp(4, timestamp);
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return null;
	}

	@Override
	public String getApproveAgainstTenderRefNo(String tenderRefNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {
			con = ConnectionManager.getConnection();

			String query = " select tmd_ad_approve_status  from public.nt_m_tender_manage_postpone where tmd_reference_no =? and tmd_ad_approve_status SIMILAR TO'(A|R)%' ";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderRefNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("tmd_ad_approve_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return status;
	}

	// added by gayathra. Print offer letter methods

	@Override
	public List<TenderDTO> getBoardApprovedTenderRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TenderDTO> data = new ArrayList<TenderDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct tap_reference_no from public.nt_m_tender_applicant "
					+ "inner join public.nt_m_tender_management on nt_m_tender_management.tdm_reference_no = nt_m_tender_applicant.tap_reference_no "
					+ "where tap_reference_no !='' and tap_reference_no is not null and tap_status !='C' and nt_m_tender_management.tdm_status ='C' and tap_board_approval_status='A' order by tap_reference_no";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			TenderDTO dto;

			while (rs.next()) {
				dto = new TenderDTO();
				dto.setTenderRefNo(rs.getString("tap_reference_no"));
				data.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<TenderDTO> getrouteNoFillterdByTenderRefNo(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TenderDTO> data = new ArrayList<TenderDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct nt_t_tender_details.tnd_route_no from public.nt_m_tender_applicant "
					+ "inner join public.nt_t_tender_details on nt_t_tender_details.seqno=nt_m_tender_applicant.tap_app_pre_route_seq "
					+ "where tap_reference_no=? and tap_board_approval_status='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTenderRefNo());
			rs = ps.executeQuery();

			TenderDTO dto;

			while (rs.next()) {
				dto = new TenderDTO();
				dto.setRouteNo(rs.getString("tnd_route_no"));
				data.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public TenderDTO fillRouteDetails(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TenderDTO dto = new TenderDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct tnd_serial_no, tnd_departure, tnd_arrival from public.nt_t_tender_details "
					+ "where tnd_route_no=? and tnd_reference_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getRouteNo());
			ps.setString(2, tenderDTO.getTenderRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setArrival(rs.getString("tnd_arrival"));
				dto.setDeparture(rs.getString("tnd_departure"));
				dto.setSlNumber(rs.getString("tnd_serial_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public List<TenderDTO> getPrintOfferLetterDetails(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TenderDTO> data = new ArrayList<TenderDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct tap_offer_letter_count, tap_status, tap_reference_no, nt_t_tender_details.tnd_route_no, nt_t_tender_details.tnd_departure, nt_t_tender_details.tnd_arrival, tnd_effective_routes, tap_application_no, tap_applicant_name,"
					+ "tap_bid_price, nt_t_tender_details.tnd_treasure_holder_price, tap_threshold_percentage,nt_t_tender_details.tnd_no_of_permits "
					+ ", tap_remark " + "from public.nt_m_tender_applicant "
					+ "inner join public.nt_m_tender_management on nt_m_tender_management.tdm_reference_no = nt_m_tender_applicant.tap_reference_no  "
					+ "inner join public.nt_t_tender_details on nt_t_tender_details.seqno=nt_m_tender_applicant.tap_app_pre_route_seq "
					+ "where nt_t_tender_details.tnd_route_no=? and tap_reference_no=?   and tap_board_approval_status='A' and nt_m_tender_management.tdm_status ='C'";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getRouteNo());
			ps.setString(2, tenderDTO.getTenderRefNo());
			rs = ps.executeQuery();

			TenderDTO dto;

			while (rs.next()) {
				dto = new TenderDTO();
				dto.setLetterCountString(rs.getInt("tap_offer_letter_count"));

				String approveStatusCode = rs.getString("tap_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("F")) {
						approveStatus = "FINISHED";
					} else if (approveStatusCode.equals("O")) {
						approveStatus = "ONGOING";
					} else {
						approveStatus = "N/A";
					}
				}

				dto.setTenderApplicationStatus(approveStatus);

				dto.setTenderRefNo(rs.getString("tap_reference_no"));
				dto.setRouteNo(rs.getString("tnd_route_no"));
				dto.setEffectiveRoutes(rs.getString("tnd_effective_routes"));
				dto.setTenderAppNo(rs.getString("tap_application_no"));
				dto.setApplicantName(rs.getString("tap_applicant_name"));
				dto.setBidPrice(rs.getBigDecimal("tap_bid_price"));
				dto.setTreasureHolderPrice(rs.getBigDecimal("tnd_treasure_holder_price"));
				dto.setTreasureHolderPricePercentage(rs.getBigDecimal("tap_threshold_percentage"));
				dto.setNoOfPermits(rs.getInt("tnd_no_of_permits"));
				dto.setRemark(rs.getString("tap_remark"));

				data.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	public List<CommitteeOrBoardApprovalDTO> getTenderApplicationNoForCommitteeApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> tenderNoList = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct tsd_app_no FROM public.nt_t_task_det where tsd_task_code='TD013' and tsd_status='C'\r\n"
					+ "or tsd_task_code='TD014' and tsd_status='O'\r\n"
					+ "or tsd_task_code='TD014' and tsd_status='C'\r\n"
					+ "or tsd_task_code='TD015' and tsd_status='O'\r\n"
					+ "or tsd_task_code='TD015' and tsd_status='C'\r\n"
					+ "or tsd_task_code='TD017' and tsd_status='O' order by tsd_app_no ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();

				committeeApprovalDTO.setCommitteeTenderApplicationNo(rs.getString("tsd_app_no"));

				tenderNoList.add(committeeApprovalDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderNoList;
	}

	public List<CommitteeOrBoardApprovalDTO> getViewTenderApplicationNoForCommitteeApproval() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommitteeOrBoardApprovalDTO> tenderNoList = new ArrayList<CommitteeOrBoardApprovalDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tap_application_no FROM public.nt_m_tender_applicant \r\n"
					+ "where tap_first_committee_status='A' or tap_second_committee_status='A' or tap_board_approval_status='A'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommitteeOrBoardApprovalDTO committeeApprovalDTO = new CommitteeOrBoardApprovalDTO();

				committeeApprovalDTO.setCommitteeTenderApplicationNo(rs.getString("tap_application_no"));

				tenderNoList.add(committeeApprovalDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderNoList;
	}

	public String getRefNo(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String appNo = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tap_reference_no FROM public.nt_m_tender_applicant where tap_application_no='"
					+ applicationNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				appNo = rs.getString("tap_reference_no");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return appNo;

	}

	public String getFirstApprovalStatus(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tap_first_committee_status FROM public.nt_m_tender_applicant where tap_application_no='"
					+ applicationNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("tap_first_committee_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return status;

	}

	public String getSecondApprovalStatus(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tap_second_committee_status FROM public.nt_m_tender_applicant where tap_application_no='"
					+ applicationNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("tap_second_committee_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return status;

	}

	public String getBoardApprovalStatus(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tap_board_approval_status FROM public.nt_m_tender_applicant where tap_application_no='"
					+ applicationNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("tap_board_approval_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return status;

	}

	public int insertFirstApproveStatus(String applicationNo, String remark, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_tender_applicant\r\n"
					+ "SET tap_first_committee_status='A',tap_modified_by='" + user + "', tap_modified_date='"
					+ timestamp + "',tap_first_committee_remark='" + remark + "'\r\n" + "WHERE tap_application_no='"
					+ applicationNo + "';";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

			return -1;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return 0;

	}

	public int insertSecondApproveStatus(String applicationNo, String remark, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_tender_applicant\r\n"
					+ "SET tap_second_committee_status='A',tap_modified_by='" + user + "', tap_modified_date='"
					+ timestamp + "',tap_second_committee_remark='" + remark + "'\r\n" + "WHERE tap_application_no='"
					+ applicationNo + "';";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

			return -1;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return 0;

	}

	public int insertTenderBoardStatus(String applicationNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_tender_applicant\r\n" + "SET tap_board_approval_status='S'\r\n"
					+ "WHERE tap_application_no='" + applicationNo + "';";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

			return -1;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return 0;

	}

	public int insertBoardStatus(String applicationNo, String status, String remark, String user) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_tender_applicant\r\n" + "SET tap_board_approval_status='" + status
					+ "',tap_modified_by='" + user + "', tap_modified_date='" + timestamp + "' , tap_board_remark='"
					+ remark + "'\r\n" + "WHERE tap_application_no='" + applicationNo + "';";

			stmt = con.prepareStatement(sql);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

			return -1;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return 0;

	}

	@Override
	public List<String> getOnGoingReferenceNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> referenceNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tdm_reference_no \r\n" + "FROM nt_m_tender_management \r\n"
					+ "WHERE tdm_status= 'C' AND tdm_ad_approve_status = 'A' AND tdm_reference_no IS NOT NULL\r\n"
					+ "ORDER BY tdm_reference_no;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				referenceNoList.add(rs.getString("tdm_reference_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return referenceNoList;
	}

	@Override
	public List<String> getOnGoingApplicationNoList(String referenceNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> applicationNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tap_application_no " + "FROM public.nt_m_tender_applicant "
					+ "WHERE tap_reference_no = ? AND tap_status = 'O' " + "ORDER BY tap_application_no;";
			ps = con.prepareStatement(query);

			ps.setString(1, referenceNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				applicationNoList.add(rs.getString("tap_application_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return applicationNoList;
	}

	@Override
	public TenderDTO getDetailsByReferenceNo(TenderDTO tenderDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tdm_description,tdm_closing_date,tdm_time " + "FROM public.nt_m_tender_management "
					+ "WHERE tdm_reference_no = ? ";

			ps = con.prepareStatement(query);

			ps.setString(1, tenderDTO.getTenderRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				tenderDTO.setTenderDes(rs.getString("tdm_description"));
				tenderDTO.setTenClosingDate(rs.getString("tdm_closing_date").substring(0, 10));
				tenderDTO.setTenClosingTime(rs.getString("tdm_time"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderDTO;
	}

	@Override
	public TenderApplicantDTO getDetailsByApplicationNo(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		TenderApplicantDTO tenderApplicantDTO = new TenderApplicantDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tap_title,tap_applicant_name,tap_nic,tap_contact_no,tap_tender_application_remark,"
					+ "tap_voucher_no,tap_refundable_deposit_amount,tap_recite_no,tap_app_pre_route_seq,"
					+ "tap_is_doc_available," + "tap_refundable_deposit_amount,tap_recite_no,"
					+ "tap_is_doc_available,tap_bank,tap_first_env_remark,"
					+ "tap_is_signature_available,tap_is_parallel_permit_available,tap_bid_price,tap_owners_capability,"
					+ "tap_second_env_remark,tap_threshold_percentage " + "FROM public.nt_m_tender_applicant "
					+ "WHERE tap_application_no = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				tenderApplicantDTO.setApplicationNo(applicationNo);
				tenderApplicantDTO.setTitle(rs.getString("tap_title"));
				tenderApplicantDTO.setBidderName(rs.getString("tap_applicant_name"));
				tenderApplicantDTO.setNic(rs.getString("tap_nic"));
				tenderApplicantDTO.setContactNo(rs.getString("tap_contact_no"));
				tenderApplicantDTO.setRemark(rs.getString("tap_tender_application_remark"));
				tenderApplicantDTO.setVoucherNo(rs.getString("tap_voucher_no"));

				tenderApplicantDTO.setRefundableDepositAmount(rs.getBigDecimal("tap_refundable_deposit_amount"));

				tenderApplicantDTO.setReciptNo(rs.getString("tap_recite_no"));

				tenderApplicantDTO.setRouteSeqNo(rs.getInt("tap_app_pre_route_seq"));

				tenderApplicantDTO.setDocumentAvailable(rs.getBoolean("tap_is_doc_available"));
				tenderApplicantDTO.setBankName(rs.getString("tap_bank"));

				tenderApplicantDTO.setRefundableDepositAmount(rs.getBigDecimal("tap_refundable_deposit_amount"));
				tenderApplicantDTO.setReciptNo(rs.getString("tap_recite_no"));

				tenderApplicantDTO.setDocumentAvailable(rs.getBoolean("tap_is_doc_available"));
				tenderApplicantDTO.setBankName(rs.getString("tap_bank"));
				tenderApplicantDTO.setFirstEnvRemark(rs.getString("tap_first_env_remark"));

				tenderApplicantDTO = getDetailsByRouteNo(tenderApplicantDTO);

				tenderApplicantDTO.setOwnersSignature(rs.getBoolean("tap_is_signature_available"));
				tenderApplicantDTO.setParallelRootAvai(rs.getBoolean("tap_is_parallel_permit_available"));
				tenderApplicantDTO.setAcceptanceOfPermit(rs.getBoolean("tap_is_parallel_permit_available"));
				tenderApplicantDTO.setBidPrice(rs.getBigDecimal("tap_bid_price"));
				tenderApplicantDTO.setOwnersCapability(rs.getString("tap_owners_capability"));
				tenderApplicantDTO.setSecondEnvRemark(rs.getString("tap_second_env_remark"));
				tenderApplicantDTO.setThPricePercentage(rs.getDouble("tap_threshold_percentage"));

				tenderApplicantDTO = getDetailsByRouteNo(tenderApplicantDTO);

			}
			
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);

			String query1 = "SELECT pav_application_no, pav_voucher_no, pav_payment_type "
					+ "FROM public.nt_m_payment_voucher " + "WHERE pav_application_no=? and pav_payment_type='V'; ";

			ps = con.prepareStatement(query1);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				tenderApplicantDTO.setVoucherNo(rs.getString("pav_voucher_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderApplicantDTO;
	}

	@Override
	public List<TenderApplicantDTO> getRouteNoList(String tenderRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderApplicantDTO> routeNoList = new ArrayList<TenderApplicantDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno,tnd_route_no,tnd_serial_no,tnd_departure,tnd_arrival "
					+ "FROM public.nt_t_tender_details " + "WHERE tnd_reference_no = ? " + "ORDER BY tnd_route_no;";
			ps = con.prepareStatement(query);

			ps.setString(1, tenderRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderApplicantDTO routeDet = new TenderApplicantDTO();
				routeDet.setRouteSeqNo(rs.getInt("seqno"));
				routeDet.setRouteNo(rs.getString("tnd_route_no"));
				routeDet.setSerialNo(rs.getString("tnd_serial_no"));
				routeDet.setDeparture(rs.getString("tnd_departure"));
				routeDet.setArrival(rs.getString("tnd_arrival"));

				routeNoList.add(routeDet);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return routeNoList;
	}

	@Override
	public TenderApplicantDTO getDetailsByRouteNo(TenderApplicantDTO tenderApplicantDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tnd_serial_no,tnd_departure,tnd_arrival,tnd_treasure_holder_price,tnd_no_of_permits,tnd_effective_routes "
					+ "FROM public.nt_t_tender_details " + "WHERE seqno = ? ";

			ps = con.prepareStatement(query);

			ps.setInt(1, tenderApplicantDTO.getRouteSeqNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				tenderApplicantDTO.setSerialNo(String.valueOf(rs.getString("tnd_serial_no")));
				tenderApplicantDTO.setDeparture(rs.getString("tnd_departure"));
				tenderApplicantDTO.setArrival(rs.getString("tnd_arrival"));
				tenderApplicantDTO.setThBidPriceAnnum(rs.getBigDecimal("tnd_treasure_holder_price"));
				tenderApplicantDTO.setNoOfPermits(rs.getInt("tnd_no_of_permits"));
				tenderApplicantDTO.setEffectiveRoutes(rs.getString("tnd_effective_routes"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderApplicantDTO;
	}

	@Override
	public boolean saveFirstEvelop(TenderApplicantDTO tenderApplicantDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_m_tender_applicant "
					+ " SET tap_refundable_deposit_amount=?,tap_recite_no =?, "
					+ "tap_app_pre_route_seq = ? ,tap_is_doc_available=?,tap_bank=?,tap_first_env_remark=?,tap_serial_no=?"
					+ " WHERE tap_application_no= ?;";

			stmt = con.prepareStatement(sql);

			stmt.setBigDecimal(1, tenderApplicantDTO.getRefundableDepositAmount());
			stmt.setString(2, tenderApplicantDTO.getReciptNo());

			stmt.setInt(3, tenderApplicantDTO.getRouteSeqNo());
			stmt.setBoolean(4, tenderApplicantDTO.isDocumentAvailable());
			stmt.setString(5, tenderApplicantDTO.getBankName());

			if (tenderApplicantDTO.getFirstEnvRemark().equals(null) || tenderApplicantDTO.getFirstEnvRemark().equals("")
					|| tenderApplicantDTO.getFirstEnvRemark().isEmpty()) {
				stmt.setNull(6, java.sql.Types.VARCHAR);
			} else {
				stmt.setString(6, tenderApplicantDTO.getFirstEnvRemark());
			}
			stmt.setString(7, tenderApplicantDTO.getSerialNo());
			stmt.setString(8, tenderApplicantDTO.getApplicationNo());
			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return true;
	}

	@Override
	public boolean saveSecondEvelop(TenderApplicantDTO tenderApplicantDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Double thPricePercentage = null;
		thPricePercentage = (tenderApplicantDTO.getThBidPriceAnnum().doubleValue()
				/ tenderApplicantDTO.getBidPrice().doubleValue()) * 100;

		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.FLOOR);
		thPricePercentage = new Double(df.format(thPricePercentage));

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_tender_applicant "
					+ " SET tap_is_signature_available=?,tap_is_parallel_permit_available = ?, tap_is_cancellation_on_parallel_permit = ?, "
					+ " tap_bid_price=?, tap_owners_capability=?,tap_second_env_remark=?,tap_threshold_percentage = ? "
					+ " WHERE tap_application_no = ?;";

			stmt = con.prepareStatement(sql);

			stmt.setBoolean(1, tenderApplicantDTO.isOwnersSignature());
			stmt.setBoolean(2, tenderApplicantDTO.isParallelRootAvai());
			stmt.setBoolean(3, tenderApplicantDTO.isAcceptanceOfPermit());
			stmt.setBigDecimal(4, tenderApplicantDTO.getBidPrice());

			if (tenderApplicantDTO.getOwnersCapability().equals(null)
					|| tenderApplicantDTO.getOwnersCapability().equals("")
					|| tenderApplicantDTO.getOwnersCapability().isEmpty()) {
				stmt.setNull(5, java.sql.Types.VARCHAR);
			} else {
				stmt.setString(5, tenderApplicantDTO.getOwnersCapability());
			}

			if (tenderApplicantDTO.getSecondEnvRemark().equals(null)
					|| tenderApplicantDTO.getSecondEnvRemark().equals("")
					|| tenderApplicantDTO.getSecondEnvRemark().isEmpty()) {
				stmt.setNull(6, java.sql.Types.VARCHAR);
			} else {
				stmt.setString(6, tenderApplicantDTO.getSecondEnvRemark());
			}

			stmt.setDouble(7, thPricePercentage);
			stmt.setString(8, tenderApplicantDTO.getApplicationNo());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return true;

	}

	// tender evaluation
	@Override
	public List<TenderApplicantDTO> getSelectingApplications(String serialNo, String tenderRefNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderApplicantDTO> selectingApplicationList = new ArrayList<TenderApplicantDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.tap_app_pre_route_seq AS preRouteNo,A.tap_application_no AS applicationNo,A.tap_applicant_name AS bidderName,A.tap_bid_price AS bidPrice,\r\n"
					+ "A.tap_threshold_percentage AS bidPricePercentage,A.tap_second_env_remark AS specialRemarks\r\n"
					+ "FROM public.nt_m_tender_applicant A \r\n"
					+ "WHERE A.tap_serial_no = ? AND A.tap_reference_no = ? "
					+ "ORDER BY A.tap_threshold_percentage DESC ";

			ps = con.prepareStatement(query);

			ps.setString(1, serialNo);
			ps.setString(2, tenderRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderApplicantDTO applications = new TenderApplicantDTO();
				applications.setApplicationNo(rs.getString("applicationNo"));
				applications.setBidderName(rs.getString("bidderName"));
				applications.setBidPrice(rs.getBigDecimal("bidPrice"));
				applications.setThPricePercentage(rs.getDouble("bidPricePercentage"));
				applications.setSecondEnvRemark(rs.getString("specialRemarks"));

				selectingApplicationList.add(applications);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return selectingApplicationList;
	}

	@Override
	public List<TenderApplicantDTO> getSelectedApplications(String tenderRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderApplicantDTO> selectedApplicationList = new ArrayList<TenderApplicantDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select A.tap_reference_no as tenderNo, A.tap_app_pre_route_seq as routeSeqNo,A.tap_application_no as applicationNo,A.tap_applicant_name as applicantName ,\r\n"
					+ "A.tap_bid_price as bidPrice,A.tap_threshold_percentage as thPricePercentage,\r\n"
					+ "A.tap_serial_no as serialNo,A.tap_second_env_remark as specialRemarks,A.tap_ten_evaluation_remark as evaluationMark,\r\n"
					+ "B.tnd_route_no as routeNO,tnd_effective_routes as effectiveRoutes,B.tnd_arrival as arrival,B.tnd_departure as departure,B.tnd_no_of_permits as permitCount,\r\n"
					+ "B.tnd_treasure_holder_price as thPrice\r\n" + "from public.nt_m_tender_applicant A\r\n"
					+ "inner join public.nt_t_tender_details B on A.tap_app_pre_route_seq = B.seqno\r\n"
					+ "where A.tap_reference_no = ? and A.tap_tender_evalution_status = 'S'"
					+ "group by A.tap_reference_no,A.tap_app_pre_route_seq,A.tap_application_no,A.tap_applicant_name,\r\n"
					+ "A.tap_bid_price,A.tap_threshold_percentage,A.tap_serial_no,A.tap_second_env_remark,A.tap_ten_evaluation_remark,\r\n"
					+ "B.tnd_route_no ,tnd_effective_routes ,B.tnd_arrival ,B.tnd_departure ,B.tnd_no_of_permits,\r\n"
					+ "B.tnd_treasure_holder_price\r\n" + "order by A.tap_threshold_percentage desc; ";

			ps = con.prepareStatement(query);

			ps = con.prepareStatement(query);

			ps.setString(1, tenderRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderApplicantDTO application = new TenderApplicantDTO();
				application.setRouteNo(rs.getString("routeNO"));
				application.setApplicationNo(rs.getString("applicationNo"));
				application.setBidderName(rs.getString("applicantName"));
				application.setBidPrice(rs.getBigDecimal("bidPrice"));
				application.setThPricePercentage(rs.getDouble("thPricePercentage"));
				application.setThBidPriceAnnum(rs.getBigDecimal("thPrice"));
				application.setSerialNo(rs.getString("serialNo"));
				application.setArrival(rs.getString("arrival"));
				application.setDeparture(rs.getString("departure"));
				application.setSecondEnvRemark(rs.getString("specialRemarks"));
				application.setEffectiveRoutes(rs.getString("effectiveRoutes"));
				application.setNoOfPermits(rs.getInt("permitCount"));
				application.setEvaluationRemark(rs.getString("evaluationMark"));

				selectedApplicationList.add(application);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return selectedApplicationList;
	}

	@Override
	public boolean completeTender(String tenderRefNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_m_tender_management set tdm_tender_evaluation_status = 'F' \r\n"
					+ "where tdm_reference_no = ? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, tenderRefNo);
			stmt.executeUpdate();
			con.commit();

			String q2 = "update public.nt_t_survey_route_detail\r\n"
					+ "SET srd_route_committee_evaluation_done = 'Y' \r\n" + "where srd_survey_no = (\r\n"
					+ "select B.survey_no as surveyNo\r\n" + "from nt_m_tender_management A\r\n"
					+ "inner join nt_t_traffic_proposal B on A.tdm_traffic_proposal_no = B.proposal_no\r\n"
					+ "where A.tdm_reference_no = ?);";
			ps1 = con.prepareStatement(q2);
			ps1.setString(1, tenderRefNo);

			ps1.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return true;

	}

	@Override
	public boolean addApplications(List<TenderApplicantDTO> temptSelectedApplicationList) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String applicationNo = null;

		try {
			con = ConnectionManager.getConnection();

			for (TenderApplicantDTO tenderApplicantDTO : temptSelectedApplicationList) {

				applicationNo = tenderApplicantDTO.getApplicationNo();

				String sql = "update public.nt_m_tender_applicant set tap_tender_evalution_status = 'S' \r\n"
						+ "where tap_application_no = ? ;";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, applicationNo);
				stmt.executeUpdate();
				con.commit();

			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return true;
	}

	@Override
	public boolean removeSelectedApplication(String applicationNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_m_tender_applicant set tap_tender_evalution_status = ? \r\n"
					+ "where tap_application_no = ? ;";

			stmt = con.prepareStatement(sql);

			stmt.setNull(1, java.sql.Types.VARCHAR);
			stmt.setString(2, applicationNo);
			stmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return true;
	}

	// 2019/02/06 - Print Agreement
	@Override
	public TenderDTO getPrintDetails(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		TenderDTO tenderDTO = new TenderDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "select A.tap_application_no as applicationNo,A.tap_reference_no as reference_no,A.tap_print_agreement_count,\r\n"
					+ "					A.tap_applicant_name as bidder_name,A.tap_nic as nic,A.tap_serial_no as serialNo,\r\n"
					+ "					A.tap_bank_slip_reference_no as bankSlipRefNo,A.tap_app_pre_route_seq ,A.tap_offer_letter_count as offer_letter_count,\r\n"
					+ "					B.tdm_description as description,\r\n"
					+ "					C.tnd_arrival as arrival,C.tnd_departure as departure,C.tnd_route_no as routeNo\r\n"
					+ "					from nt_m_tender_applicant A\r\n"
					+ "					inner join nt_m_tender_management B on B.tdm_reference_no = A.tap_reference_no\r\n"
					+ "					inner join nt_t_tender_details C on C.seqno = A.tap_app_pre_route_seq\r\n"
					+ "					where A.tap_application_no =? ";
			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				tenderDTO.setTenderRefNo(rs.getString("reference_no"));
				tenderDTO.setTenderDes(rs.getString("description"));
				tenderDTO.setApplicantName(rs.getString("bidder_name"));
				tenderDTO.setNicNo(rs.getString("nic"));
				tenderDTO.setSlNumber(rs.getString("serialNo"));
				tenderDTO.setRouteNo(rs.getString("routeNo"));
				tenderDTO.setArrival(rs.getString("arrival"));
				tenderDTO.setDeparture(rs.getString("departure"));
				tenderDTO.setTenderBankSlipRefNo(rs.getString("bankSlipRefNo"));
				tenderDTO.setAgreementIssuedCheck(rs.getBoolean("offer_letter_count"));
				tenderDTO.setTenderAppNo(applicationNo);
				int printedCount = rs.getInt("tap_print_agreement_count");
				if (printedCount == 0) {
					tenderDTO.setAgreementIssuedCheck(false);
				} else if (printedCount > 0) {
					tenderDTO.setAgreementIssuedCheck(true);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderDTO;
	}

	public boolean checkTenderEvalStatus(String tenderReferanceNo, String status) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();
			String query = "select  tdm_reference_no,tdm_tender_evaluation_status "
					+ "FROM public.nt_m_tender_management "
					+ " WHERE tdm_reference_no = ? and tdm_tender_evaluation_status = ?  ;";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderReferanceNo);
			ps.setString(2, status);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isTaskAvailable = true;
			} else {
				isTaskAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskAvailable;

	}

	@Override
	public int getOfferLetterCount(TenderDTO dto) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tap_offer_letter_count FROM public.nt_m_tender_applicant "
					+ "where tap_application_no=? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, dto.getTenderAppNo());
			rs = stmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt("tap_offer_letter_count");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return count;
	}

	@Override
	public boolean updateOfferLetterCountandApplicationNoStatus(TenderDTO dto, int count) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query2 = "update public.nt_m_tender_applicant set tap_offer_letter_count=?, tap_status='F' "
					+ "where tap_application_no=?";

			stmt = con.prepareStatement(query2);
			stmt.setInt(1, count);
			stmt.setString(2, dto.getTenderAppNo());
			int c = stmt.executeUpdate();

			if (c > 0) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isUpdate;
	}

	public List<TenderDTO> checkingForTenderClosing(TenderDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderDTO> list = new ArrayList<TenderDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tap_application_no FROM public.nt_m_tender_applicant where tap_reference_no=? and tap_status!='F'";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getTenderRefNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderDTO e = new TenderDTO();
				e.setTenderAppNo(rs.getString("tap_application_no"));
				list.add(e);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	@Override
	public boolean closeTenderReferenceNo(TenderDTO dto) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isUpdate = false;
		try {

			con = ConnectionManager.getConnection();

			String query2 = "update public.nt_m_tender_management set tdm_status='F' where tdm_reference_no=? ";

			stmt = con.prepareStatement(query2);
			stmt.setString(1, dto.getTenderRefNo());
			int c = stmt.executeUpdate();

			if (c > 0) {
				isUpdate = true;
			} else {
				isUpdate = false;
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return isUpdate;
	}

	@Override
	public boolean isTenderRefNoClosed(TenderDTO dto) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isClosed = false;
		try {

			con = ConnectionManager.getConnection();

			String query2 = "select * from public.nt_m_tender_management "
					+ "where tdm_reference_no=? and tdm_status='F' ";

			stmt = con.prepareStatement(query2);
			stmt.setString(1, dto.getTenderRefNo());
			rs = stmt.executeQuery();

			if (rs.next()) {
				isClosed = true;
			} else {
				isClosed = false;
			}

			con.commit();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return isClosed;
	}

	public String getApplicationNoFromRefNo(String refNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String appNo = null;
		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tap_application_no FROM public.nt_m_tender_applicant where tap_reference_no='" + refNo
					+ "'";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				appNo = rs.getString("tap_application_no");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return appNo;
	}

	@Override
	public TenderDTO getDetails_tender_management(String tenderRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		TenderDTO tenderDTO = new TenderDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select  tdm_description, tdm_closing_date, tdm_time from public.nt_m_tender_management where tdm_reference_no=?";
			ps = con.prepareStatement(query);
			ps.setString(1, tenderRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				tenderDTO.setTenderDes(rs.getString("tdm_description"));
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = format.format(rs.getDate("tdm_closing_date"));

				tenderDTO.setTenClosingDate(dateString);
				tenderDTO.setTenClosingTime(rs.getString("tdm_time"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderDTO;
	}

	@Override
	public List<TenderDTO> getDetails_tender_details(String tenderRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderDTO> list = new ArrayList<TenderDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select seqno,tnd_serial_no, tnd_route_no, tnd_via, tnd_type_of_service, tnd_one_way_fare, tnd_turns_perday, tnd_parallel_roads, "
					+ "tnd_effective_routes, tnd_departure, tnd_arrival, tnd_no_of_permits, tnd_treasure_holder_price, tnd_created_by, tnd_create_date from public.nt_t_tender_details where tnd_reference_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {

				TenderDTO e = new TenderDTO();
				e.setSlNumber(rs.getString("tnd_serial_no"));
				e.setRouteNo(rs.getString("tnd_route_no"));
				e.setVia(rs.getString("tnd_via"));
				e.setTypeOfServices(rs.getString("tnd_type_of_service"));
				e.setOneWayBusFare(rs.getBigDecimal("tnd_one_way_fare"));
				e.setTurnsPerDay(rs.getInt("tnd_turns_perday"));
				e.setParallelRoads(rs.getString("tnd_parallel_roads"));
				e.setEffectiveRoutes(rs.getString("tnd_effective_routes"));
				e.setDeparture(rs.getString("tnd_departure"));
				e.setArrival(rs.getString("tnd_arrival"));
				e.setNoOfPermits(rs.getInt("tnd_no_of_permits"));
				e.setTreasureHolderPrice(rs.getBigDecimal("tnd_treasure_holder_price"));

				list.add(e);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return list;
	}

	public String getsecondCommitteeApprovalStatus(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String status = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tap_second_committee_status FROM public.nt_m_tender_applicant \r\n"
					+ "where tap_application_no = '" + applicationNo + "'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("tap_second_committee_status");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return status;

	}

	// Print Agreement -Mushtharq
	@Override
	public TenderDTO checkedIsPrinted(String tenderAppNo, String tenderRefNo, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean checkPrinted = false;
		Boolean success = false;
		int printedCount = 0;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		TenderDTO dto = new TenderDTO();
		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tap_print_agreement_count FROM public.nt_m_tender_applicant where tap_application_no=? and tap_reference_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, tenderAppNo);
			stmt.setString(2, tenderRefNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				dto = new TenderDTO();
				printedCount = rs.getInt("tap_print_agreement_count");
				if (printedCount == 0) {
					checkPrinted = false;
					dto.setAgreementIssuedCheck(checkPrinted);
					printedCount++;
				} else if (printedCount > 0) {
					checkPrinted = true;
					dto.setAgreementIssuedCheck(checkPrinted);
					printedCount++;
				}
			}

			ConnectionManager.close(stmt);
			
			if (printedCount > 0) {
				String sql1 = "UPDATE public.nt_m_tender_applicant SET tap_print_agreement_count = ?,  tap_modified_by=? , tap_modified_date =? WHERE tap_application_no=? and tap_board_approval_status='A';";
				stmt = con.prepareStatement(sql1);
				stmt.setInt(1, printedCount);
				stmt.setString(2, user);
				stmt.setTimestamp(3, timestamp);
				stmt.setString(4, tenderAppNo);
				int data = stmt.executeUpdate();
				if (data > 0) {
					success = true;
					dto.setSavedAgreementPrintedCount(success);
				}
			} else {

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return dto;
	}

	@Override
	public List<TenderDTO> getTenderRouteDetails(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderDTO> tenderList = new ArrayList<TenderDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.proposal_no, a.survey_no, b.srd_route_no, b.srd_via, c.ini_service_type,"
					+ "c.ini_one_way_fare, b.srd_no_of_permits, b.srd_effective_route, "
					+ "b.srd_origin,b.srd_destination FROM public.nt_t_traffic_proposal a "
					+ "inner join public.nt_t_survey_route_detail b on b.srd_survey_no=a.survey_no "
					+ "inner join public.nt_t_initiate_survey c on c.ini_surveyno=a.survey_no where a.proposal_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderDTO.getTrafficProposalNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderDTO e = new TenderDTO();
				e.setTrafficProposalNo(rs.getString("proposal_no"));
				e.setSurveyNo(rs.getString("survey_no"));
				e.setRouteNo(rs.getString("srd_route_no"));
				e.setVia(rs.getString("srd_via"));
				e.setTypeOfServices(rs.getString("ini_service_type"));
				e.setOneWayBusFare(rs.getBigDecimal("ini_one_way_fare"));
				e.setNoOfPermits(rs.getInt("srd_no_of_permits"));
				e.setDeparture(rs.getString("srd_destination"));
				e.setArrival(rs.getString("srd_origin"));
				e.setEffectiveRoutes(rs.getString("srd_effective_route"));
				tenderList.add(e);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return tenderList;
	}

	@Override
	public List<TenderDTO> getDefaultPrintOfferLetterDetails(TenderDTO tenderDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TenderDTO> data = new ArrayList<TenderDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct tap_offer_letter_count,tap_status, tap_reference_no, nt_t_tender_details.tnd_route_no, nt_t_tender_details.tnd_departure, nt_t_tender_details.tnd_arrival, tnd_effective_routes, tap_application_no, tap_applicant_name,"
					+ "tap_bid_price, nt_t_tender_details.tnd_treasure_holder_price, tap_threshold_percentage,nt_t_tender_details.tnd_no_of_permits "
					+ ", tap_remark " + "from public.nt_m_tender_applicant "
					+ "inner join public.nt_m_tender_management on nt_m_tender_management.tdm_reference_no = nt_m_tender_applicant.tap_reference_no "
					+ "inner join public.nt_t_tender_details on nt_t_tender_details.seqno=nt_m_tender_applicant.tap_app_pre_route_seq "
					+ "where  tap_board_approval_status='A'  and nt_m_tender_management.tdm_status ='C' ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			TenderDTO dto;

			while (rs.next()) {
				dto = new TenderDTO();
				dto.setLetterCountString(rs.getInt("tap_offer_letter_count"));

				String approveStatusCode = rs.getString("tap_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("F")) {
						approveStatus = "FINISHED";
					} else if (approveStatusCode.equals("O")) {
						approveStatus = "ONGOING";
					} else {
						approveStatus = "N/A";
					}
				}

				dto.setTenderApplicationStatus(approveStatus);
				dto.setTenderRefNo(rs.getString("tap_reference_no"));
				dto.setRouteNo(rs.getString("tnd_route_no"));
				dto.setEffectiveRoutes(rs.getString("tnd_effective_routes"));
				dto.setTenderAppNo(rs.getString("tap_application_no"));
				dto.setApplicantName(rs.getString("tap_applicant_name"));
				dto.setBidPrice(rs.getBigDecimal("tap_bid_price"));
				dto.setTreasureHolderPrice(rs.getBigDecimal("tnd_treasure_holder_price"));
				dto.setTreasureHolderPricePercentage(rs.getBigDecimal("tap_threshold_percentage"));
				dto.setNoOfPermits(rs.getInt("tnd_no_of_permits"));
				dto.setRemark(rs.getString("tap_remark"));

				data.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return data;
	}

	@Override
	public List<String> getNonCompletedOnGoingApplicationNoList(String tenderRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> applicationNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tap_application_no \r\n" + "FROM public.nt_m_tender_applicant\r\n"
					+ "WHERE tap_reference_no = ? AND tap_status = 'O'\r\n"
					+ "and tap_application_no not in (SELECT A.tap_application_no \r\n"
					+ "FROM public.nt_m_tender_applicant A \r\n"
					+ "inner join public.nt_h_task_his B on A.tap_application_no= B.tsd_app_no\r\n"
					+ "WHERE A.tap_reference_no=? and tsd_task_code ='TD013' and tsd_status='O')";
			ps = con.prepareStatement(query);

			ps.setString(1, tenderRefNo);
			ps.setString(2, tenderRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				applicationNoList.add(rs.getString("tap_application_no"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return applicationNoList;
	}

}
