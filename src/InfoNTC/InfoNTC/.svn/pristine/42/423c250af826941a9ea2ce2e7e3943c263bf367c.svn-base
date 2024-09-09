package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.TenderApplicantDTO;
import lk.informatics.ntc.model.dto.TenderDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class ApproveElectedBidderServiceImpl implements ApproveElectedBidderService {

	@Override
	public List<String> getReferenceNoList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> referenceNoList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tdm_reference_no \r\n" + "FROM nt_m_tender_management \r\n"
					+ "WHERE tdm_status='C' AND tdm_ad_approve_status = 'A' AND tdm_tender_evaluation_status = 'F' AND tdm_tender_approved_status is null AND tdm_reference_no IS NOT NULL\r\n"
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
	public boolean addApplications(List<TenderApplicantDTO> temptSelectedApplicationList) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String applicationNo = null;

		try {
			con = ConnectionManager.getConnection();

			for (TenderApplicantDTO tenderApplicantDTO : temptSelectedApplicationList) {

				applicationNo = tenderApplicantDTO.getApplicationNo();

				String sql = "update public.nt_m_tender_applicant set tap_board_approval_status = 'S' \r\n"
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
	public List<TenderApplicantDTO> getSelectingApplications(String serialNo, String tenderRefNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TenderApplicantDTO> selectingApplicationList = new ArrayList<TenderApplicantDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT A.tap_app_pre_route_seq as pre_Route_No,A.tap_application_no as application_No,A.tap_applicant_name as bidder_name,A.tap_bid_price as bid_price,A.tap_threshold_percentage as bid_price_percentage,A.tap_second_env_remark as special_remarks\r\n"
					+ "FROM public.nt_m_tender_applicant A \r\n"
					+ "WHERE  A.tap_serial_no = ? AND A.tap_reference_no = ? AND tap_board_approval_status is null AND tap_tender_evalution_status is null ";

			ps = con.prepareStatement(query);

			ps.setString(1, serialNo);
			ps.setString(2, tenderRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderApplicantDTO applications = new TenderApplicantDTO();
				applications.setApplicationNo(rs.getString("application_No"));
				applications.setBidderName(rs.getString("bidder_name"));
				applications.setBidPrice(rs.getBigDecimal("bid_price"));
				applications.setThPricePercentage(rs.getDouble("bid_price_percentage"));
				applications.setSecondEnvRemark(rs.getString("special_remarks"));

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

			String query = "select A.tap_app_pre_route_seq as route_no,A.tap_application_no as application_no,A.tap_applicant_name as applicant_name,A.tap_bid_price as bid_price,A.tap_threshold_percentage as th_price_percentage,A.tap_second_env_remark as special_remarks,B.tnd_treasure_holder_price as th_price,B.tnd_serial_no as serial_no,B.tnd_arrival as arrival,B.tnd_departure as departure,B.tnd_effective_routes as effective_routes\r\n"
					+ "from public.nt_m_tender_applicant A	inner join public.nt_t_tender_details B on A.tap_app_pre_route_seq=B.seqno\r\n"
					+ "where A.tap_reference_no=? and (A.tap_tender_evalution_status='S' or A.tap_board_approval_status='S')";

			ps = con.prepareStatement(query);

			ps = con.prepareStatement(query);

			ps.setString(1, tenderRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TenderApplicantDTO application = new TenderApplicantDTO();
				application.setRouteNo(rs.getString("route_no"));
				application.setApplicationNo(rs.getString("application_no"));
				application.setBidderName(rs.getString("applicant_name"));
				application.setBidPrice(rs.getBigDecimal("bid_price"));
				application.setThPricePercentage(rs.getDouble("th_price_percentage"));
				application.setThBidPriceAnnum(rs.getBigDecimal("th_price"));
				application.setSerialNo(rs.getString("serial_no"));
				application.setArrival(rs.getString("arrival"));
				application.setDeparture(rs.getString("departure"));
				application.setSecondEnvRemark(rs.getString("special_remarks"));
				application.setEffectiveRoutes(rs.getString("effective_routes"));

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
	public boolean removeSelectedApplication(String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_m_tender_applicant set tap_tender_evalution_status = ?, tap_board_approval_status = ? \r\n"
					+ "where tap_application_no = ? ;";

			ps = con.prepareStatement(sql);

			ps.setNull(1, java.sql.Types.VARCHAR);
			ps.setNull(2, java.sql.Types.VARCHAR);
			ps.setString(3, applicationNo);
			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return true;
	}

	@Override
	public boolean approveElectedBidder(String tenderRefNo, String logingUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "update public.nt_m_tender_management set tdm_tender_approved_status = 'C', tdm_modify_by=? ,tdm_modify_date=?  "
					+ "where tdm_reference_no = ? ;";

			ps = con.prepareStatement(sql);
			ps.setString(1, logingUser);
			ps.setTimestamp(2, timestamp);
			ps.setString(3, tenderRefNo);
			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return true;

	}

	@Override
	public String getTrafficProposalNoFromTenderRefNo(String tenderRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String trafficProposalNo = null;
		try {
			con = ConnectionManager.getConnection();

			String sql = "select tdm_traffic_proposal_no from public.nt_m_tender_management where tdm_reference_no=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, tenderRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				trafficProposalNo = rs.getString("tdm_traffic_proposal_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return trafficProposalNo;
	}

	@Override
	public boolean isApproved(String tenderRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isTaskSU008C = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tdm_tender_approved_status FROM public.nt_m_tender_management where tdm_reference_no=?  ;";

			ps = con.prepareStatement(query);
			ps.setString(1, tenderRefNo);
			rs = ps.executeQuery();

			if (rs.next()) {
				
				String approvedStatus=rs.getString("tdm_tender_approved_status");
				if(approvedStatus.equals("C")){
					isTaskSU008C=true;
							
				}else if(approvedStatus.equals(null)){
					isTaskSU008C=false;
				}else{
					isTaskSU008C=false;
				}
			}

			
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskSU008C;
	}

}
