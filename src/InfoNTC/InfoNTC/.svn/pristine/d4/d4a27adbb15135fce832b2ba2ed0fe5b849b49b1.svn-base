package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.AdvertisementDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class TenderAdvertisementServiceImpl implements TenderAdvertisementService {

	// ref no list
	public List<AdvertisementDTO> refNodropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AdvertisementDTO> returnList = new ArrayList<AdvertisementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tdm_reference_no, tdm_description FROM public.nt_m_tender_management "
					+ "WHERE tdm_reference_no is not null and coalesce(tdm_ad_approve_status,'X') <> 'A' ORDER BY tdm_reference_no";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				AdvertisementDTO advertisementDTO = new AdvertisementDTO();
				advertisementDTO.setTenderNo(rs.getString("tdm_reference_no"));
				advertisementDTO.setDescription(rs.getString("tdm_description"));

				returnList.add(advertisementDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return returnList;
	}

	public List<AdvertisementDTO> approvalrefNodropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AdvertisementDTO> returnList = new ArrayList<AdvertisementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tdm_reference_no,tdm_description " + "from nt_m_tender_management "
					+ "where tdm_ad_approve_status ='P'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				AdvertisementDTO advertisementDTO = new AdvertisementDTO();
				advertisementDTO.setTenderNo(rs.getString("tdm_reference_no"));
				advertisementDTO.setDescription(rs.getString("tdm_description"));

				returnList.add(advertisementDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return returnList;
	}

	// data table
	public List<AdvertisementDTO> dataTable(String refno) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<AdvertisementDTO> dataList = new ArrayList<AdvertisementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tnd_serial_no,tnd_route_no,tnd_arrival,tnd_departure,tnd_no_of_permits,tnd_via,tnd_type_of_service,tnd_one_way_fare, "
					+ "tnd_turns_perday,tnd_turns_perday,tnd_treasure_holder_price,tnd_parallel_roads "
					+ "from nt_t_tender_details " + "where tnd_reference_no='" + refno + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				AdvertisementDTO advertisementDTO = new AdvertisementDTO();

				advertisementDTO.setSsiNo(rs.getString("tnd_serial_no"));
				advertisementDTO.setRouteNo(rs.getString("tnd_route_no"));
				advertisementDTO.setArrival(rs.getString("tnd_arrival"));
				advertisementDTO.setDeparture(rs.getString("tnd_departure"));
				advertisementDTO.setNoofpermits(rs.getInt("tnd_no_of_permits"));
				advertisementDTO.setVia(rs.getString("tnd_via"));
				advertisementDTO.setTypeofService(rs.getString("tnd_type_of_service"));
				advertisementDTO.setOnewayfare(rs.getBigDecimal("tnd_one_way_fare"));
				advertisementDTO.setNoofTurns(rs.getInt("tnd_turns_perday"));
				advertisementDTO.setChargersoftechevaluation(rs.getBigDecimal("tnd_treasure_holder_price"));
				advertisementDTO.setParallelRoads(rs.getString("tnd_parallel_roads"));

				dataList.add(advertisementDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dataList;
	}

	public String description(String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String description = "";

		try {
			con = ConnectionManager.getConnection();

			String query = " select tdm_description" + " from nt_m_tender_management" + " where tdm_reference_no='"
					+ refNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				description = rs.getString("tdm_description");

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return description;

	}

	// headerfooter default english
	public AdvertisementDTO headerfooter(String refno) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AdvertisementDTO advertisementDTO = new AdvertisementDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tad_header_des_english,tad_footer_des_english " + "from nt_t_tender_advertistment "
					+ "where tad_reference_no='" + refno + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				advertisementDTO.setHeader(rs.getString("tad_header_des_english"));
				advertisementDTO.setFooter(rs.getString("tad_footer_des_english"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return advertisementDTO;
	}

	// header footer for sinhala
	public AdvertisementDTO headerfootersinhala(String refno) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AdvertisementDTO advertisementDTO = new AdvertisementDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tad_header_des_sinhala,tad_footer_des_sinhala "
					+ "from public.nt_t_tender_advertistment " + "where tad_reference_no='" + refno + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				advertisementDTO.setHeader(rs.getString("tad_header_des_sinhala"));
				advertisementDTO.setFooter(rs.getString("tad_footer_des_sinhala"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return advertisementDTO;
	}

	// header footer for tamil
	public AdvertisementDTO headerfootertamil(String refno) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		AdvertisementDTO advertisementDTO = new AdvertisementDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tad_header_des_tamil,tad_footer_des_tamil " + "from nt_t_tender_advertistment "
					+ "where tad_reference_no='" + refno + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				advertisementDTO.setHeader(rs.getString("tad_header_des_tamil"));
				advertisementDTO.setFooter(rs.getString("tad_footer_des_tamil"));

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return advertisementDTO;
	}

	public boolean insertHeaderFooter(AdvertisementDTO dto, int language, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		boolean result = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String header = "";
		String footer = "";

		if (language == 1) {

			header = "tad_header_des_english";
			footer = "tad_footer_des_english";
		}

		if (language == 2) {

			header = "tad_header_des_sinhala";
			footer = "tad_footer_des_sinhala";
		}

		if (language == 3) {

			header = "tad_header_des_tamil";
			footer = "tad_header_des_tamil";
		}
		// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_tender_advertistment");

			String sql = "INSERT INTO public.nt_t_tender_advertistment " + "(seqno, tad_reference_no, " + header + ", "
					+ footer + ", tad_created_by,  tad_create_date) " + "VALUES(?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, seqNo); // long
			stmt.setString(2, dto.getTenderNo());
			stmt.setString(3, dto.getHeader());
			stmt.setString(4, dto.getFooter());
			stmt.setString(5, user);
			stmt.setTimestamp(6, timestamp);
			int i = stmt.executeUpdate();

			if (i > 0) {
				result = true;
			} else {
				result = false;
			}

			if (stmt != null) {
				stmt.close();
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return result;
	}

	public boolean insertDataTable(AdvertisementDTO dto, String tenderNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query =

					"update nt_t_tender_details " + "set " + "tnd_route_no=?, " + "tnd_arrival=?, "
							+ "tnd_departure=?, " + "tnd_no_of_permits=?, " + "tnd_via=?, " + "tnd_type_of_service=?, "
							+ "tnd_one_way_fare=?, " + "tnd_turns_perday=?, " + "tnd_treasure_holder_price=?, "
							+ "tnd_parallel_roads=? " + "where tnd_serial_no =? and tnd_reference_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getRouteNo());
			ps.setString(2, dto.getArrival());
			ps.setString(3, dto.getDeparture());
			ps.setInt(4, dto.getNoofpermits());
			ps.setString(5, dto.getVia());
			ps.setString(6, dto.getTypeofService());
			ps.setBigDecimal(7, dto.getOnewayfare());
			ps.setInt(8, dto.getNoofTurns());
			ps.setBigDecimal(9, dto.getChargersoftechevaluation());
			ps.setString(10, dto.getParallelRoads());
			ps.setString(11, dto.getSsiNo());
			ps.setString(12, tenderNo);
			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	public String approvalStatus(String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "select tdm_ad_approve_status" + " from nt_m_tender_management" + " where tdm_reference_no='"
					+ refNo + "'";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				status = rs.getString("tdm_ad_approve_status");

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

	public void updateTenderStatus(AdvertisementDTO dto) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query =

					"update nt_m_tender_management " + "set " + "tdm_ad_approve_status='P' "
							+ "where tdm_reference_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getTenderNo());

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	public void updateStatus(AdvertisementDTO dto, String status, String rejectReason) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Timestamp ts = new Timestamp(dto.getPublishDate().getTime());
		try {
			con = ConnectionManager.getConnection();

			String query =

					"update nt_m_tender_management " + "set " + "tdm_ad_approve_status=?," + "tdm_status=?,"
							+ "tdm_publish_date=?," + "tdm_ad_reject_reason=?" + "where tdm_reference_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, "C");

			ps.setTimestamp(3, ts);

			if (!rejectReason.equals("")) {
				ps.setString(4, rejectReason);
			} else {
				ps.setString(4, "");
			}
			ps.setString(5, dto.getTenderNo());

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	public List publishTable() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Date date = new Date();
		List<AdvertisementDTO> dataList = new ArrayList<AdvertisementDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tdm_reference_no,tdm_description,tdm_publish_date " + "from nt_m_tender_management "
					+ "where tdm_status='C' and tdm_ad_approve_status='A' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				AdvertisementDTO advertisementDTO = new AdvertisementDTO();
				advertisementDTO.setTenderNo(rs.getString("tdm_reference_no"));
				advertisementDTO.setDescription(rs.getString("tdm_description"));
				advertisementDTO.setDate(rs.getTimestamp("tdm_publish_date"));

				date.setTime(advertisementDTO.getDate().getTime());
				String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
				advertisementDTO.setDatestring(formattedDate);

				dataList.add(advertisementDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dataList;
	}

	@Override
	public boolean checkTenderStatus(AdvertisementDTO dto, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isFound = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select * from public.nt_m_tender_management "
					+ "where tdm_ad_approve_status='P' and tdm_reference_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, dto.getTenderNo());
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isFound = true;
			} else {
				isFound = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isFound;
	}

}
