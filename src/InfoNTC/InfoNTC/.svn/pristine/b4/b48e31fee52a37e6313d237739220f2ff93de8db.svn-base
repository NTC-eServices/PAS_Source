package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.BlockRouteDetailDTO;
import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.CommonInquiryDTO;
import lk.informatics.ntc.model.dto.MainStationDetailsDTO;
import lk.informatics.ntc.model.dto.PaymentTypeDTO;
import lk.informatics.ntc.model.dto.PermitDTO;
import lk.informatics.ntc.model.dto.TerminalArrivalDepartureDTO;
import lk.informatics.ntc.model.dto.TerminalArrivalDepartureTimeDTO;
import lk.informatics.ntc.model.dto.TerminalBlockDetailsDTO;
import lk.informatics.ntc.model.dto.TerminalDetailsDTO;
import lk.informatics.ntc.model.dto.TerminalPayCancellationDTO;
import lk.informatics.ntc.model.dto.TerminalPaymentDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class TerminalManagementServiceImpl implements TerminalManagementService {

	@Override
	public List<MainStationDetailsDTO> selectAllMainStations() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MainStationDetailsDTO> mainStationDetailsList = new ArrayList<MainStationDetailsDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct * from public.nt_r_main_station";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				MainStationDetailsDTO dto = new MainStationDetailsDTO();
				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));
				dto.setDescription_sinhala(rs.getString("description_sinhala"));
				dto.setDescription_tamil(rs.getString("description_tamil"));
				String status = rs.getString("active");
				if (status != null && !status.isEmpty() && status.equalsIgnoreCase("A")) {
					dto.setActive("Active");
				} else {
					dto.setActive("Inactive");
				}
				mainStationDetailsList.add(dto);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return mainStationDetailsList;
	}

	@Override
	public List<TerminalDetailsDTO> selectTerminalsByStation(String stationCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TerminalDetailsDTO> terminalDetailsDTOList = new ArrayList<TerminalDetailsDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT nt_t_station_terminal_det.seq, nt_m_station_terminal.station_code, nt_t_station_terminal_det.terminal, nt_t_station_terminal_det.line, nt_t_station_terminal_det.status "
					+ "FROM nt_m_station_terminal RIGHT JOIN nt_t_station_terminal_det ON nt_m_station_terminal.seq = nt_t_station_terminal_det.station_terminal_seq where nt_m_station_terminal.station_code=?"
					+ "ORDER BY nt_t_station_terminal_det.seq";

			ps = con.prepareStatement(query);
			ps.setString(1, stationCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				TerminalDetailsDTO dto = new TerminalDetailsDTO();
				dto.setSeq(rs.getLong("seq"));
				dto.setStationCode(rs.getString("station_code"));
				dto.setTerminal(rs.getString("terminal"));
				dto.setPlatform(rs.getString("line"));
				String status = rs.getString("status");
				if (status != null && !status.isEmpty() && status.equalsIgnoreCase("A")) {
					dto.setStatus("Active");
				} else {
					dto.setStatus("Inactive");
				}
				terminalDetailsDTOList.add(dto);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return terminalDetailsDTOList;
	}

	public List<TerminalDetailsDTO> selectTerminalDetailsByTerminal(String terminal, String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TerminalDetailsDTO> terminalDetailsDTOList = new ArrayList<TerminalDetailsDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT nt_t_station_terminal_det.seq, nt_m_station_terminal.station_code, nt_t_station_terminal_det.terminal, nt_t_station_terminal_det.line, nt_t_station_terminal_det.status "
					+ "FROM nt_m_station_terminal RIGHT JOIN nt_t_station_terminal_det ON nt_m_station_terminal.seq = nt_t_station_terminal_det.station_terminal_seq where nt_t_station_terminal_det.status='A' "
					+ "and nt_m_station_terminal.station_code=? and nt_t_station_terminal_det.terminal=? "
					+ "ORDER BY nt_t_station_terminal_det.seq";

			ps = con.prepareStatement(query);
			ps.setString(1, code);
			ps.setString(2, terminal);
			rs = ps.executeQuery();

			while (rs.next()) {
				TerminalDetailsDTO dto = new TerminalDetailsDTO();
				dto.setSeq(rs.getLong("seq"));
				dto.setStationCode(rs.getString("station_code"));
				dto.setTerminal(rs.getString("terminal"));
				dto.setPlatform(rs.getString("line"));
				String status = rs.getString("status");
				if (status != null && !status.isEmpty() && status.equalsIgnoreCase("A")) {
					dto.setStatus("Active");
				} else {
					dto.setStatus("Inactive");
				}
				terminalDetailsDTOList.add(dto);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return terminalDetailsDTOList;
	}

	@Override
	public List<String> selectDistinctTerminalsByStation(String stationCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> terminalDetailsDTOList = new ArrayList<String>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct nt_t_station_terminal_det.terminal "
					+ "FROM nt_t_station_terminal_det LEFT JOIN nt_m_station_terminal ON nt_m_station_terminal.seq = nt_t_station_terminal_det.station_terminal_seq "
					+ "where nt_m_station_terminal.station_code=? " + "ORDER BY nt_t_station_terminal_det.terminal";

			ps = con.prepareStatement(query);
			ps.setString(1, stationCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				terminalDetailsDTOList.add(rs.getString("terminal"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return terminalDetailsDTOList;
	}

	// Insert Terminal Header
	public long saveTerminalHeader(TerminalDetailsDTO terminalDetailsDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long seq = 0;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_station_terminal");
			seq = (int) seqNo;
			String sql = "INSERT INTO public.nt_m_station_terminal"
					+ "(seq, station_code, no_of_terminals, terminal_display_type, terminal_id_start_with, "
					+ "no_of_lines_per_terminal, line_display_type, line_id_start_with, status, created_by, created_date) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, terminalDetailsDTO.getStationCode());
			stmt.setInt(3, terminalDetailsDTO.getNoOfTerminals());
			stmt.setString(4, terminalDetailsDTO.getTerminalDisplayType());
			stmt.setString(5, terminalDetailsDTO.getTerminalIdStartWith());
			stmt.setInt(6, terminalDetailsDTO.getNoOfPlatforms());
			stmt.setString(7, terminalDetailsDTO.getPlatformDisplayType());
			stmt.setString(8, terminalDetailsDTO.getPlatformIdStartWith());
			stmt.setString(9, terminalDetailsDTO.getStatus());
			stmt.setString(10, user);
			stmt.setTimestamp(11, timestamp);

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

		return seq;
	}

	// Insert Terminal Details
	public int saveTerminalDetails(TerminalDetailsDTO terminalDetailsDTO, long stationSeqno, String terminal,
			String platform, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long stationTerminalSeq = stationSeqno;
		String platformCode = terminal.concat(platform);
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_station_terminal_det");

			String sql = "INSERT INTO public.nt_t_station_terminal_det"
					+ "(seq, station_terminal_seq, terminal, line, status,created_by,created_date) "
					+ "VALUES(?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, stationTerminalSeq);
			stmt.setString(3, terminal); // terminal
			stmt.setString(4, platformCode); // line
			stmt.setString(5, terminalDetailsDTO.getStatus());
			stmt.setString(6, user);
			stmt.setTimestamp(7, timestamp);

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

	// Select Terminal Details By Sequence
	@Override
	public List<TerminalBlockDetailsDTO> selectTerminalBlocksBySequence(Long blockSeq) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TerminalBlockDetailsDTO> terminalBlockDetailsDTOList = new ArrayList<TerminalBlockDetailsDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "select nt_t_terminal_block_det.* from public.nt_t_terminal_block_det "
					+ "LEFT JOIN nt_m_terminal_block ON nt_m_terminal_block.seq = nt_t_terminal_block_det.terminal_block_seq "
					+ "where nt_m_terminal_block.station_terminal_det_seq=? ORDER BY nt_t_terminal_block_det.seq";

			ps = con.prepareStatement(query);
			ps.setLong(1, blockSeq);
			rs = ps.executeQuery();

			while (rs.next()) {
				TerminalBlockDetailsDTO blockDetailsDTO = new TerminalBlockDetailsDTO();
				blockDetailsDTO.setSeq(rs.getLong("seq"));
				blockDetailsDTO.setBlock(rs.getString("block"));
				blockDetailsDTO.setCreatedBy(rs.getString("created_by"));
				blockDetailsDTO.setCreatedDate(rs.getTimestamp("created_date"));
				blockDetailsDTO.setModifiedBy(rs.getString("modified_by"));
				blockDetailsDTO.setModifiedDate(rs.getTimestamp("modified_date"));
				String status = rs.getString("status");
				if (status.equalsIgnoreCase("A")) {
					blockDetailsDTO.setStatus("Active");
				} else {
					blockDetailsDTO.setStatus("Inactive");
				}

				terminalBlockDetailsDTOList.add(blockDetailsDTO);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return terminalBlockDetailsDTOList;
	}

	// Insert Terminal Details History
	public int saveTerminalDetailsHistory(TerminalDetailsDTO terminalDetailsDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_station_terminal_det");

			String sql = "INSERT INTO public.nt_h_station_terminal_det"
					+ "(seq, terminal_det_seq, station_terminal_seq, terminal, line, status,created_by,created_date,modified_by,modified_date) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, terminalDetailsDTO.getSeq());
			stmt.setLong(3, terminalDetailsDTO.getStationTerminalseq());
			stmt.setString(4, terminalDetailsDTO.getTerminal()); // terminal
			stmt.setString(5, terminalDetailsDTO.getPlatform()); // line
			stmt.setString(6, terminalDetailsDTO.getStatus()); // status
			stmt.setString(7, terminalDetailsDTO.getCreatedBy()); // created by
			stmt.setTimestamp(8, terminalDetailsDTO.getCreatedDate()); // created date
			stmt.setString(9, terminalDetailsDTO.getModifiedBy()); // modified_by
			stmt.setTimestamp(10, terminalDetailsDTO.getModifiedDate()); // modified date

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

	// Update Status in Address Detail
	public int terminalDetailStatusUpdate(long stationSeq, String status, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_station_terminal_det " + "SET status = ?, modified_by=?, modified_date=?"
					+ "WHERE seq = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setLong(4, stationSeq);

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return 0;

	}

	@Override
	public boolean deleteSelectedBusTerminal(long terminalSeqNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDeletedOwnerRd = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM  public.nt_t_station_terminal_det WHERE seq=?;";

			ps = con.prepareStatement(query);
			ps.setLong(1, terminalSeqNo);

			int i = ps.executeUpdate();

			if (i > 0) {
				isDeletedOwnerRd = true;
			} else {
				isDeletedOwnerRd = false;
			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isDeletedOwnerRd;
	}

	// Insert Block Header
	public int saveBlockHeader(TerminalBlockDetailsDTO terminalBlockDetailsDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int seq = 0;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_station_terminal");
			seq = (int) seqNo;
			String sql = "INSERT INTO public.nt_m_terminal_block"
					+ "(seq, station_terminal_det_seq, no_of_blocks, block_display_type, block_id_start_with, status, created_by, created_date) "
					+ "VALUES(?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			Long stationTerminalDetSeq = Long.parseLong(terminalBlockDetailsDTO.getTerminalDetailsId());
			stmt.setLong(2, stationTerminalDetSeq);
			stmt.setInt(3, terminalBlockDetailsDTO.getNoOfBlocks());
			stmt.setString(4, terminalBlockDetailsDTO.getBlockDisplayType());
			stmt.setString(5, terminalBlockDetailsDTO.getBlockIdStartWith());
			stmt.setString(6, terminalBlockDetailsDTO.getStatus());
			stmt.setString(7, user);
			stmt.setTimestamp(8, timestamp);

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

		return seq;
	}

	// Insert Terminal Details
	public int saveTerminalBlockDetails(TerminalBlockDetailsDTO terminalBlockDetailsDTO, String stationSeqno,
			String platform, String block, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long stationTerminalSeq = Long.parseLong(stationSeqno);
		String blockCode = platform.concat(block);
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_terminal_block_det");

			String sql = "INSERT INTO public.nt_t_terminal_block_det"
					+ "(seq, terminal_block_seq, block, status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, stationTerminalSeq);
			stmt.setString(3, blockCode); // block
			stmt.setString(4, terminalBlockDetailsDTO.getStatus());
			stmt.setString(5, user);
			stmt.setTimestamp(6, timestamp);

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

	// Select Terminal Details By Sequence
	@Override
	public TerminalDetailsDTO selectTerminalBySequence(Long seq) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TerminalDetailsDTO dto = new TerminalDetailsDTO();
		try {
			con = ConnectionManager.getConnection();
			String query = "select * from public.nt_t_station_terminal_det where seq=?";

			ps = con.prepareStatement(query);
			ps.setLong(1, seq);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setSeq(rs.getLong("seq"));
				dto.setStationTerminalseq(rs.getLong("station_terminal_seq"));
				dto.setTerminal(rs.getString("terminal"));
				dto.setPlatform(rs.getString("line"));
				dto.setCreatedBy(rs.getString("created_by"));
				dto.setCreatedDate(rs.getTimestamp("created_date"));
				dto.setModifiedBy(rs.getString("modified_by"));
				dto.setModifiedDate(rs.getTimestamp("modified_date"));
				String status = rs.getString("status");
				dto.setStatus(status);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dto;
	}

	// Update Status in terminalBlock
	public int terminalBlockDetailStatusUpdate(long stationSeq, String status, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_terminal_block_det " + "SET status = ?, modified_by=?, modified_date=?"
					+ "WHERE seq = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, status);
			ps.setString(2, user);
			ps.setTimestamp(3, timestamp);
			ps.setLong(4, stationSeq);

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return 0;

	}

	// Select Terminal Block Details By Sequence
	@Override
	public TerminalBlockDetailsDTO selectTerminalBlockBySequence(Long seq) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TerminalBlockDetailsDTO dto = new TerminalBlockDetailsDTO();
		try {
			con = ConnectionManager.getConnection();
			String query = "select * from public.nt_t_terminal_block_det where seq=?";

			ps = con.prepareStatement(query);
			ps.setLong(1, seq);
			rs = ps.executeQuery();

			while (rs.next()) {
				dto.setSeq(rs.getLong("seq"));
				dto.setTerminalBlockSeq(rs.getLong("terminal_block_seq"));
				dto.setBlock(rs.getString("block"));
				dto.setCreatedBy(rs.getString("created_by"));
				dto.setCreatedDate(rs.getTimestamp("created_date"));
				dto.setModifiedBy(rs.getString("modified_by"));
				dto.setModifiedDate(rs.getTimestamp("modified_date"));
				String status = rs.getString("status");
				dto.setStatus(status);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dto;
	}

	// Insert Terminal Block Details History
	public int saveTerminalBlockDetailsHistory(TerminalBlockDetailsDTO terminalBlockDetailsDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_terminal_block_det");

			String sql = " public.nt_h_terminal_block_det"
					+ "(seq, block_seq, terminal_block_seq, block, status, created_by, created_date, modified_by, modified_date)"
					+ " VALUES(?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, terminalBlockDetailsDTO.getSeq());
			stmt.setLong(3, terminalBlockDetailsDTO.getTerminalBlockSeq());
			stmt.setString(4, terminalBlockDetailsDTO.getBlock()); // block
			stmt.setString(5, terminalBlockDetailsDTO.getStatus()); // status
			stmt.setString(6, terminalBlockDetailsDTO.getCreatedBy()); // created by
			stmt.setTimestamp(7, terminalBlockDetailsDTO.getCreatedDate()); // created date
			stmt.setString(8, terminalBlockDetailsDTO.getModifiedBy()); // modified_by
			stmt.setTimestamp(9, terminalBlockDetailsDTO.getModifiedDate()); // modified date

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
	public boolean deleteSelectedTerminalBlock(long terminalBlockSeqNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isDeletedOwnerRd = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM  public.nt_t_terminal_block_det WHERE seq=?;";

			ps = con.prepareStatement(query);
			ps.setLong(1, terminalBlockSeqNo);

			int i = ps.executeUpdate();

			if (i > 0) {
				isDeletedOwnerRd = true;
			} else {
				isDeletedOwnerRd = false;
			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isDeletedOwnerRd;
	}

	// Get Status
	public List<CommonDTO> getServiceTypeToDropdown() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code,description " + " FROM public.nt_r_service_types " + " WHERE active = 'A' "
					+ " ORDER BY code ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();
				commonDTO.setCode(rs.getString("code"));
				commonDTO.setDescription(rs.getString("description"));

				returnList.add(commonDTO);

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

	@Override
	public long saveAssignedBusRouteHeader(long terminalBlockSeq, String stationId, long terminalId, String lineNo,
			String blockName, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		long seqNo = 0;

		try {
			con = ConnectionManager.getConnection();
			seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_block_route");

			String sql = "INSERT INTO public.nt_m_block_route(br_seq, br_terminal_block_seq, br_station_code, br_terminal_id, br_line_no, br_block_id, br_status, br_created_by, br_created_date) "
					+ "VALUES (?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, terminalBlockSeq);
			stmt.setString(3, stationId);
			stmt.setLong(4, terminalId);
			stmt.setString(5, lineNo);
			stmt.setString(6, blockName);
			stmt.setString(7, "A");
			stmt.setString(8, user);
			stmt.setTimestamp(9, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return seqNo;
	}

	@Override
	public boolean saveAssignedBusRouteDetails(long blockRouteSeq, String serviceType, String routeNo, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_block_route_det");

			String sql = "INSERT INTO public.nt_t_block_route_det(bd_seq, bd_block_route_seq, bd_service_type_code, bd_route_no, bd_status, bd_created_by, bd_created_date) "
					+ "VALUES (?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setLong(2, blockRouteSeq);
			stmt.setString(3, serviceType);
			stmt.setString(4, routeNo);
			stmt.setString(5, "A");
			stmt.setString(6, user);
			stmt.setTimestamp(7, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return true;
	}

	@Override
	public List<BlockRouteDetailDTO> selectBlockRoutes(String stationCode, String platformId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BlockRouteDetailDTO> routeDetailsDTOList = new ArrayList<BlockRouteDetailDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM public.nt_m_block_route, public.nt_t_block_route_det"
					+ " WHERE nt_t_block_route_det.bd_block_route_seq IN "
					+ " (SELECT br_seq FROM public.nt_m_block_route WHERE nt_m_block_route.br_line_no = ? AND nt_m_block_route.br_station_code = ?)"
					+ " AND nt_m_block_route.br_seq = nt_t_block_route_det.bd_block_route_seq "
					+ " AND nt_m_block_route.br_status = 'A' " + " ORDER BY nt_m_block_route.br_block_id;";

			ps = con.prepareStatement(query);
			ps.setString(1, platformId);
			ps.setString(2, stationCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				BlockRouteDetailDTO dto = new BlockRouteDetailDTO();
				dto.setDetailSeq(rs.getLong("bd_seq"));
				dto.setSeq(rs.getLong("br_seq"));
				dto.setServiceTypeCode(rs.getString("bd_service_type_code"));
				dto.setRouteNo(rs.getString("bd_route_no"));
				dto.setStatus(rs.getString("bd_status"));
				dto.setSeq(rs.getLong("bd_block_route_seq"));
				dto.setBlockName(rs.getString("br_block_id"));
				routeDetailsDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return routeDetailsDTOList;
	}

	@Override
	public boolean isBlockAssigned(String route, String service, String stationCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean assigned = false;
		int i = -1;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT COUNT(*) " + " FROM nt_m_block_route, nt_t_block_route_det "
					+ " WHERE nt_m_block_route.br_seq = nt_t_block_route_det.bd_block_route_seq "
					+ " AND nt_t_block_route_det.bd_route_no = ? "
					+ " AND nt_t_block_route_det.bd_service_type_code = ?"
					+ " AND nt_m_block_route.br_station_code = ? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, route);
			ps.setString(2, service);
			ps.setString(3, stationCode);
			rs = ps.executeQuery();

			while (rs.next())
				i = rs.getInt(1);

			if (i > 0)
				assigned = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return assigned;
	}

	@Override
	public boolean updateAssignedBusRouteDetails(BlockRouteDetailDTO oldRouteDTO, BlockRouteDetailDTO newRouteDTO,
			String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			// backup data
			String sql_history = "INSERT INTO public.nt_h_block_route_det(bd_seq, bd_block_route_seq, bd_service_type_code, bd_route_no, bd_status, bd_created_by, bd_created_date) "
					+ "VALUES (?,?,?,?,?,?,?); ";

			stmt = con.prepareStatement(sql_history);
			stmt.setLong(1, oldRouteDTO.getDetailSeq());
			stmt.setLong(2, oldRouteDTO.getSeq());
			stmt.setString(3, oldRouteDTO.getServiceTypeCode());
			stmt.setString(4, oldRouteDTO.getRouteNo());
			stmt.setString(5, oldRouteDTO.getStatus());
			stmt.setString(6, user);
			stmt.setTimestamp(7, timestamp);

			stmt.executeUpdate();

			// insert modified data
			String sql = "UPDATE public.nt_t_block_route_det " + "SET bd_service_type_code = ?," + "bd_route_no = ?,"
					+ "bd_modified_by = ?," + "bd_modified_date = ?" + " WHERE bd_seq = ?;";

			stmt2 = con.prepareStatement(sql);
			stmt2.setString(1, newRouteDTO.getServiceTypeCode());
			stmt2.setString(2, newRouteDTO.getRouteNo());
			stmt2.setString(3, user);
			stmt2.setTimestamp(4, timestamp);
			stmt2.setLong(5, newRouteDTO.getDetailSeq());
			stmt2.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
		return true;
	}

	@Override
	public boolean removeAssignedBusRoute(BlockRouteDetailDTO oldRouteDTO, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			// backup data
			String sql_history = "INSERT INTO public.nt_h_block_route_det(bd_seq, bd_block_route_seq, bd_service_type_code, bd_route_no, bd_status, bd_created_by, bd_created_date) "
					+ "VALUES (?,?,?,?,?,?,?); ";

			stmt = con.prepareStatement(sql_history);
			stmt.setLong(1, oldRouteDTO.getDetailSeq());
			stmt.setLong(2, oldRouteDTO.getSeq());
			stmt.setString(3, oldRouteDTO.getServiceTypeCode());
			stmt.setString(4, oldRouteDTO.getRouteNo());
			stmt.setString(5, "I");
			stmt.setString(6, user);
			stmt.setTimestamp(7, timestamp);

			stmt.executeUpdate();

			String query = "DELETE FROM public.nt_t_block_route_det WHERE bd_seq=?;";

			stmt2 = con.prepareStatement(query);
			stmt2.setLong(1, oldRouteDTO.getDetailSeq());
			stmt2.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
		return true;
	}

	@Override
	public List<PaymentTypeDTO> getPaymentTypes() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PaymentTypeDTO> paymentDTOList = new ArrayList<PaymentTypeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_r_terminal_payment_type WHERE active = 'A';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeDTO dto = new PaymentTypeDTO();
				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));
				paymentDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return paymentDTOList;
	}

	@Override
	public List<PaymentTypeDTO> getResciptNoList(String paymentTypeCode, String terminalCode) {

		List<PaymentTypeDTO> list = new ArrayList<PaymentTypeDTO>();
		String query = "select distinct  tep_seq as seq,  tep_receipt_no as description FROM nt_t_terminal_payment where tep_payment_type_code=? and tep_terminal_location=?;";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(query);) {

			ps.setString(1, paymentTypeCode);
			ps.setString(2, terminalCode);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				PaymentTypeDTO dto = new PaymentTypeDTO();
				dto.setSeq(rs.getLong("seq"));
				dto.setDescription(rs.getString("description"));
				list.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public double[] getIssuanceAmt(String paymentType, String serviceType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		double[] amounts = new double[2];

		try {
			con = ConnectionManager.getConnection();

			String query;

			if (serviceType == null) {
				query = "SELECT b.amount,b.penalty_fee FROM nt_r_terminal_payment_type a, nt_r_terminal_payment_det b "
						+ "WHERE a.active = 'A' " + "AND b.payment_type = ? " + "AND a.code = b.payment_type;";

				ps = con.prepareStatement(query);
				ps.setString(1, paymentType);
				rs = ps.executeQuery();

			} else {
				query = "SELECT b.amount,b.penalty_fee FROM nt_r_terminal_payment_type a, nt_r_terminal_payment_det b "
						+ "WHERE a.active = 'A' " + "AND b.payment_type = ? " + "AND b.service_type = ? "
						+ "AND a.code = b.payment_type;";

				ps = con.prepareStatement(query);
				ps.setString(1, paymentType);
				ps.setString(2, serviceType);
				rs = ps.executeQuery();
			}

			while (rs.next()) {
				amounts[0] = rs.getDouble("amount");
				amounts[1] = rs.getDouble("penalty_fee");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return amounts;
	}

	@Override
	public boolean validateVehicleOrPermitNo(String type, String no) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean valid = false;

		try {
			con = ConnectionManager.getConnection();

			String query = null;
			int count = 0;

			if (type.equals("VEHICLE"))
				query = "SELECT COUNT(*) FROM nt_t_pm_application WHERE pm_vehicle_regno = ?;";

			else if (type.equals("PERMIT"))
				query = "SELECT COUNT(*) FROM nt_t_pm_application WHERE pm_permit_no = ?;";

			if (query != null) {
				stmt = con.prepareStatement(query);
				stmt.setString(1, no.toUpperCase());
				rs = stmt.executeQuery();

				while (rs.next())
					count = rs.getInt(1);

				if (count > 0)
					valid = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			valid = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return valid;
	}

	@Override
	public PermitDTO getPermitInfoByBusNoPermitNo(String permitNo, String busNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitDTO permitDTO = new PermitDTO();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			if (permitNo != null) {
				String query = "SELECT c.seq, c.pm_permit_no, c.pm_vehicle_regno, a.rou_service_origine,a.rou_service_destination, a.rou_via, b.pmo_full_name, b.pmo_gender, b.pmo_mobile_no, b.pmo_telephone_no, a.rou_number, pmo_province ,c.pm_permit_expire_date, c.pm_service_type , d.description  "
						+ " FROM nt_r_route a, nt_t_pm_vehi_owner b, nt_t_pm_application c, nt_r_service_types d "
						+ " WHERE c.pm_permit_no = ? AND c.pm_status in ('A' ,'O') "
						+ " AND a.rou_number = c.pm_route_no "
						+ " AND b.pmo_application_no = c.pm_application_no AND c.pm_service_type = d.code;";

				ps = con.prepareStatement(query);
				ps.setString(1, permitNo);
				rs = ps.executeQuery();

			} else {
				String query = "SELECT c.seq, c.pm_permit_no, c.pm_vehicle_regno, a.rou_service_origine,a.rou_service_destination, a.rou_via, b.pmo_full_name, b.pmo_gender, b.pmo_mobile_no, b.pmo_telephone_no, a.rou_number, pmo_province ,c.pm_permit_expire_date, c.pm_service_type , d.description  "
						+ " FROM nt_r_route a, nt_t_pm_vehi_owner b, nt_t_pm_application c, nt_r_service_types d "
						+ " WHERE c.pm_vehicle_regno = ? AND c.pm_status in ('A' ,'O') "
						+ " AND a.rou_number = c.pm_route_no "
						+ " AND b.pmo_application_no = c.pm_application_no  AND c.pm_service_type = d.code;";

				ps = con.prepareStatement(query);
				ps.setString(1, busNo);
				rs = ps.executeQuery();
			}

			while (rs.next()) {
				permitDTO.setSeq(rs.getLong(1));
				permitDTO.setPermitNo(rs.getString(2));
				permitDTO.setBusRegNo(rs.getString(3));
				permitDTO.setOrigin(rs.getString(4));
				permitDTO.setDestination(rs.getString(5));
				permitDTO.setVia(rs.getString(6));
				permitDTO.setVehicleOwner(rs.getString(7));
				permitDTO.setOwnerGender(rs.getString(8));
				permitDTO.setMobileNo(rs.getString(9));
				permitDTO.setTelephoneNo(rs.getString(10));
				permitDTO.setRouteNo(rs.getString(11));
				permitDTO.setProvince(rs.getString(12));

				Date expireDate = formatter.parse(rs.getString("pm_permit_expire_date"));
				permitDTO.setNewExpirDate(expireDate);

				permitDTO.setServiceType(rs.getString(14));
				permitDTO.setServiceTypeDesc(rs.getString(15));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return permitDTO;
	}

	@Override
	public TerminalPaymentDTO getTerminalPaymentInfoBySeq(long seq) {
		TerminalPaymentDTO terminalPaymentDTO = new TerminalPaymentDTO();
		String query = "select * FROM nt_t_terminal_payment where tep_seq=?;";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(query);) {

			ps.setLong(1, seq);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				terminalPaymentDTO.setTepSeq(rs.getLong("tep_seq"));
				terminalPaymentDTO.setTepPaymentTypeCode(rs.getString("tep_payment_type_code"));
				terminalPaymentDTO.setTepVehicleNo(rs.getString("tep_vehicleno"));
				terminalPaymentDTO.setTepIssuedDate(rs.getString("tep_issued_date"));
				terminalPaymentDTO.setTepIssuedMonth(rs.getString("tep_issued_month"));
				terminalPaymentDTO.setTepReceiptNo(rs.getString("tep_receipt_no"));
				terminalPaymentDTO.setTepAmountPaid(rs.getDouble("tep_ammount_paid"));
				terminalPaymentDTO.setTepPermitNo(rs.getString("tep_permit_no"));
				terminalPaymentDTO.setTepOrigin(rs.getString("tep_origin"));
				terminalPaymentDTO.setTepDestination(rs.getString("tep_destination"));
				terminalPaymentDTO.setTepOwner(rs.getString("tep_owner"));
				terminalPaymentDTO.setTepGender(rs.getString("tep_gender"));
				terminalPaymentDTO.setTepContactNo(rs.getString("tep_contact_no"));
				terminalPaymentDTO.setTepCreatedBy(rs.getString("tep_created_by"));
				terminalPaymentDTO.setTepCreatedDate(rs.getDate("tep_created_date"));
				terminalPaymentDTO.setTepModifiedBy(rs.getString("tep_modified_by"));
				terminalPaymentDTO.setTepModifiedDate(rs.getDate("tep_modified_date"));
				terminalPaymentDTO.setTepNoOfTurns(rs.getLong("tep_no_of_turns"));
				terminalPaymentDTO.setTepValidFrom(rs.getString("tep_valid_from"));
				terminalPaymentDTO.setTepValidTo(rs.getString("tep_valid_to"));
				terminalPaymentDTO.setTepPenaltyAmt(rs.getDouble("tep_penaltyamt"));
				terminalPaymentDTO.setTepTerminalPayCode(rs.getString("tep_terminal_pay_code"));
				terminalPaymentDTO.setTepChargeAmt(rs.getDouble("tep_chargeamt"));
				terminalPaymentDTO.setTepDueDate(rs.getString("tep_due_date"));
				terminalPaymentDTO.setTepPaidDate(rs.getString("tep_paid_date"));
				terminalPaymentDTO.setTepPayMode(rs.getString("tep_pay_mode"));
				terminalPaymentDTO.setTepRouteVia(rs.getString("tep_route_via"));
				terminalPaymentDTO.setTepServiceType(rs.getString("tep_service_type"));
				terminalPaymentDTO.setTepHirePermitRemark(rs.getString("tep_hirepermit_remark"));
				terminalPaymentDTO.setTepStOrigin(rs.getString("tep_st_origin"));
				terminalPaymentDTO.setTepStDestination(rs.getString("tep_st_destination"));
				terminalPaymentDTO.setTepStVia(rs.getString("tep_st_via"));
				terminalPaymentDTO.setTepStOwnerName(rs.getString("tep_st_owner_name"));
				terminalPaymentDTO.setTepStGender(rs.getString("tep_st_gender"));
				terminalPaymentDTO.setTepStContactNo(rs.getString("tep_st_contact_no"));
				terminalPaymentDTO.setTepStServiceType(rs.getString("tep_st_service_type"));
				terminalPaymentDTO.setTepExpireDate(rs.getString("tep_expire_date"));
				terminalPaymentDTO.setTepRouteNo(rs.getString("tep_route_no"));
				terminalPaymentDTO.setTepStRouteNo(rs.getString("tep_st_route_no"));
				terminalPaymentDTO.setTepOrgPermitNo(rs.getString("tep_org_permit_no"));
				terminalPaymentDTO.setTepTerminalLocation(rs.getString("tep_terminal_location"));
			}
			return terminalPaymentDTO;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public long getTotalCollectionAmt() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long count = 0;
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tps_total_collection_amt  FROM nt_t_terminal_payment_summary"
					+ " WHERE tps_issue_date = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, dateFormat.format(date));
			rs = ps.executeQuery();

			while (rs.next()) {
				count = rs.getLong(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return count;
	}

	@Override
	public boolean saveTerminalPaymentInfo(String paymentType, String serviceType, String vehihcleNo, Date issuedDate,
			String issuedMonth, String payMode, String receiptRefNo, long noOfTurn, Date fromDate, Date toDate,
			double basicAmt, double penaltyAmt, double paidAmt, double totalCollection, String permitNo, String origin,
			String destination, String via, String owner, String gender, String contact, String user, Date dueDate,
			Date paidDate, Date expireDateN, String hirePermitRemark, String tsOrigin, String tsDestination,
			String tsVia, String tsOwner, String tsGender, String tsContactNo, String tsServiceType, String tsRouteNo,
			String routeNo, String tsPermitNo, String selectedTerminalLocation) {

		Connection con = null;
		PreparedStatement stmt_retrive = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String payCode = null;

		try {
			con = ConnectionManager.getConnection();
			long terminal_payment_seq = Utils.getNextValBySeqName(con, "seq_nt_t_terminal_payment");

			if (paymentType != "001") {
				String query = "SELECT code FROM public.nt_r_terminal_payment_det WHERE payment_type = ?;";

				stmt_retrive = con.prepareStatement(query);
				stmt_retrive.setString(1, paymentType);
				ResultSet rs_retrive = stmt_retrive.executeQuery();

				while (rs_retrive.next())
					payCode = rs_retrive.getString(1);

				ConnectionManager.close(rs_retrive);

			} else {
				String query = "SELECT code FROM public.nt_r_terminal_payment_det WHERE payment_type = ? AND service_type = ?;";

				stmt_retrive = con.prepareStatement(query);
				stmt_retrive.setString(1, paymentType);
				stmt_retrive.setString(2, serviceType);
				ResultSet rs_retrive = stmt_retrive.executeQuery();

				while (rs_retrive.next())
					payCode = rs_retrive.getString(1);

				ConnectionManager.close(rs_retrive);
			}

			if (paymentType.equalsIgnoreCase("001")) {
				String sql = "INSERT INTO nt_t_terminal_payment(tep_seq, tep_payment_type_code, tep_vehicleno, tep_issued_date, tep_issued_month, tep_receipt_no, tep_ammount_paid, tep_no_of_turns, tep_valid_from, tep_valid_to, "
						+ "tep_permit_no, tep_origin, tep_destination, tep_owner, tep_gender, tep_contact_no, tep_created_by, tep_created_date, tep_penaltyamt, tep_chargeamt, tep_due_date, tep_paid_date, tep_terminal_pay_code, tep_route_via, tep_pay_mode,tep_expire_date,tep_service_type,tep_route_no,tep_terminal_location ) "
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

				stmt = con.prepareStatement(sql);

				stmt.setLong(1, terminal_payment_seq);
				stmt.setString(2, paymentType);
				stmt.setString(3, vehihcleNo);
				stmt.setString(4, issuedDate != null ? dateFormat.format(issuedDate) : null);
				stmt.setString(5, issuedMonth);
				stmt.setString(6, receiptRefNo);
				stmt.setDouble(7, paidAmt);
				stmt.setLong(8, noOfTurn);
				stmt.setString(9, fromDate != null ? dateFormat.format(fromDate) : null);
				stmt.setString(10, toDate != null ? dateFormat.format(toDate) : null);
				stmt.setString(11, permitNo);
				stmt.setString(12, origin);
				stmt.setString(13, destination);
				stmt.setString(14, owner);
				stmt.setString(15, gender);
				stmt.setString(16, contact);
				stmt.setString(17, user);
				stmt.setTimestamp(18, timestamp);
				stmt.setDouble(19, penaltyAmt);
				stmt.setDouble(20, basicAmt);
				stmt.setString(21, dueDate != null ? dateFormat.format(dueDate) : null);
				stmt.setString(22, paidDate != null ? dateFormat.format(paidDate) : null);
				stmt.setString(23, payCode);
				stmt.setString(24, via);
				stmt.setString(25, payMode);

				stmt.setString(26, expireDateN != null ? dateFormat.format(expireDateN) : null);
				stmt.setString(27, serviceType);
				stmt.setString(28, routeNo);
				stmt.setString(29, selectedTerminalLocation);

				stmt.executeUpdate();
			} else {
				String sql1 = "INSERT INTO nt_t_terminal_payment(tep_seq, tep_payment_type_code, tep_vehicleno, tep_issued_date, tep_issued_month, tep_receipt_no, tep_ammount_paid, tep_no_of_turns, tep_valid_from, tep_valid_to, "
						+ "tep_permit_no, tep_origin, tep_destination, tep_owner, tep_gender, tep_contact_no, tep_created_by, tep_created_date, tep_penaltyamt, tep_chargeamt, tep_due_date, tep_paid_date, tep_terminal_pay_code, tep_route_via, tep_pay_mode,tep_expire_date,tep_service_type, "
						+ "tep_hirepermit_remark, tep_st_origin, tep_st_destination, tep_st_via, tep_st_owner_name, tep_st_gender, tep_st_contact_no, tep_st_service_type,tep_st_route_no,tep_route_no,tep_org_permit_no ,tep_terminal_location ) "
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

				stmt3 = con.prepareStatement(sql1);

				stmt3.setLong(1, terminal_payment_seq);
				stmt3.setString(2, paymentType);
				stmt3.setString(3, vehihcleNo);
				stmt3.setString(4, issuedDate != null ? dateFormat.format(issuedDate) : null);
				stmt3.setString(5, issuedMonth);
				stmt3.setString(6, receiptRefNo);
				stmt3.setDouble(7, paidAmt);
				stmt3.setLong(8, noOfTurn);
				stmt3.setString(9, fromDate != null ? dateFormat.format(fromDate) : null);
				stmt3.setString(10, toDate != null ? dateFormat.format(toDate) : null);
				stmt3.setString(11, tsPermitNo);
				stmt3.setString(12, origin);
				stmt3.setString(13, destination);
				stmt3.setString(14, owner);
				stmt3.setString(15, gender);
				stmt3.setString(16, contact);
				stmt3.setString(17, user);
				stmt3.setTimestamp(18, timestamp);
				stmt3.setDouble(19, penaltyAmt);
				stmt3.setDouble(20, basicAmt);
				stmt3.setString(21, dueDate != null ? dateFormat.format(dueDate) : null);
				stmt3.setString(22, paidDate != null ? dateFormat.format(paidDate) : null);
				stmt3.setString(23, payCode);
				stmt3.setString(24, via);
				stmt3.setString(25, payMode);

				stmt3.setString(26, expireDateN != null ? dateFormat.format(expireDateN) : null);
				stmt3.setString(27, serviceType);
				stmt3.setString(28, hirePermitRemark);
				stmt3.setString(29, tsOrigin);
				stmt3.setString(30, tsDestination);
				stmt3.setString(31, tsVia);
				stmt3.setString(32, tsOwner);
				stmt3.setString(33, tsGender);
				stmt3.setString(34, tsContactNo);
				stmt3.setString(35, tsServiceType);
				stmt3.setString(36, tsRouteNo);
				stmt3.setString(37, routeNo);
				stmt3.setString(38, permitNo);
				stmt3.setString(39, selectedTerminalLocation);

				stmt3.executeUpdate();

			}

			// update payment_voucher
			long terminal_voucher_seq = Utils.getNextValBySeqName(con, "seq_nt_t_terminal_voucher");

			String query = "INSERT INTO nt_t_terminal_voucher (tev_seq, tev_terminal_payment_seq, tev_status, tev_amount, tev_is_receipt_generated, tev_created_by, tev_created_date)"
					+ " VALUES(?,?,?,?,?,?,?);";

			stmt2 = con.prepareStatement(query);
			stmt2.setLong(1, terminal_voucher_seq);
			stmt2.setLong(2, terminal_payment_seq);
			stmt2.setString(3, "A");
			stmt2.setDouble(4, totalCollection);
			stmt2.setString(5, "N");
			stmt2.setString(6, user);
			stmt2.setTimestamp(7, timestamp);
			stmt2.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(con);
		}
		return true;
	}

	public boolean saveTerminalPaymentInfoP(String paymentType, String serviceType, String vehihcleNo, Date issuedDate,
			String issuedMonth, String payMode, String receiptRefNo, long noOfTurn, Date fromDate, Date toDate,
			double basicAmt, double penaltyAmt, double paidAmt, double totalCollection, String permitNo, String origin,
			String destination, String via, String owner, String gender, String contact, String user, Date dueDate,
			Date paidDate) {

		Connection con = null;
		PreparedStatement stmt_retrive = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String payCode = null;

		try {
			con = ConnectionManager.getConnection();
			long terminal_payment_seq = Utils.getNextValBySeqName(con, "seq_nt_t_terminal_payment");

			if (serviceType == null) {
				String query = "SELECT code FROM public.nt_r_terminal_payment_det WHERE payment_type = ?;";

				stmt_retrive = con.prepareStatement(query);
				stmt_retrive.setString(1, paymentType);
				ResultSet rs_retrive = stmt_retrive.executeQuery();

				while (rs_retrive.next())
					payCode = rs_retrive.getString(1);

				ConnectionManager.close(rs_retrive);

			} else {
				String query = "SELECT code FROM public.nt_r_terminal_payment_det WHERE payment_type = ? AND service_type = ?;";

				stmt_retrive = con.prepareStatement(query);
				stmt_retrive.setString(1, paymentType);
				stmt_retrive.setString(2, serviceType);
				ResultSet rs_retrive = stmt_retrive.executeQuery();

				while (rs_retrive.next())
					payCode = rs_retrive.getString(1);

				ConnectionManager.close(rs_retrive);
			}

			String sql = "INSERT INTO nt_t_terminal_payment(tep_seq, tep_payment_type_code, tep_vehicleno, tep_issued_date, tep_issued_month, tep_receipt_no, tep_ammount_paid, tep_no_of_turns, tep_valid_from, tep_valid_to, "
					+ "tep_permit_no, tep_origin, tep_destination, tep_owner, tep_gender, tep_contact_no, tep_created_by, tep_created_date, tep_penaltyamt, tep_chargeamt, tep_due_date, tep_paid_date, tep_terminal_pay_code, tep_route_via, tep_pay_mode) "
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, terminal_payment_seq);
			stmt.setString(2, paymentType);
			stmt.setString(3, vehihcleNo);
			stmt.setString(4, issuedDate != null ? dateFormat.format(issuedDate) : null);
			stmt.setString(5, issuedMonth);
			stmt.setString(6, receiptRefNo);
			stmt.setDouble(7, paidAmt);
			stmt.setLong(8, noOfTurn);
			stmt.setString(9, fromDate != null ? dateFormat.format(fromDate) : null);
			stmt.setString(10, toDate != null ? dateFormat.format(toDate) : null);
			stmt.setString(11, permitNo);
			stmt.setString(12, origin);
			stmt.setString(13, destination);
			stmt.setString(14, owner);
			stmt.setString(15, gender);
			stmt.setString(16, contact);
			stmt.setString(17, user);
			stmt.setTimestamp(18, timestamp);
			stmt.setDouble(19, penaltyAmt);
			stmt.setDouble(20, basicAmt);
			stmt.setString(21, dueDate != null ? dateFormat.format(dueDate) : null);
			stmt.setString(22, paidDate != null ? dateFormat.format(paidDate) : null);
			stmt.setString(23, payCode);
			stmt.setString(24, via);
			stmt.setString(25, payMode);
			stmt.executeUpdate();

			// update payment_voucher
			long terminal_voucher_seq = Utils.getNextValBySeqName(con, "seq_nt_t_terminal_voucher");

			String query = "INSERT INTO nt_t_terminal_voucher (tev_seq, tev_terminal_payment_seq, tev_status, tev_amount, tev_is_receipt_generated, tev_created_by, tev_created_date)"
					+ " VALUES(?,?,?,?,?,?,?);";

			stmt2 = con.prepareStatement(query);
			stmt2.setLong(1, terminal_voucher_seq);
			stmt2.setLong(2, terminal_payment_seq);
			stmt2.setString(3, "A");
			stmt2.setDouble(4, totalCollection);
			stmt2.setString(5, "N");
			stmt2.setString(6, user);
			stmt2.setTimestamp(7, timestamp);
			stmt2.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
		return true;
	}

	@Override
	public long getPaymentSummarySequence(String paymentType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		long seq = 0;
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(date);

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tps_seq FROM nt_t_terminal_payment_summary " + "WHERE tps_payment_type_code = ? "
					+ "AND tps_issue_date = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, paymentType);
			ps.setString(2, strDate);
			rs = ps.executeQuery();

			while (rs.next()) {
				seq = rs.getLong(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return seq;
	}

	@Override
	public boolean updatePaymentSummary(long seq, String paymentType, double totalCollection, long receiptCount,
			boolean receiptGenerated, boolean insert, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(date);

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			if (insert) {
				seq = Utils.getNextValBySeqName(con, "seq_nt_t_terminal_payment_summary");

				query = "INSERT INTO nt_t_terminal_payment_summary(tps_seq, tps_payment_type_code, tps_issue_date, tps_total_collection_amt, tps_receipt_count, tps_is_receipt_generated, tps_created_by, tps_created_date) "
						+ "VALUES (?,?,?,?,?,?,?,?);";

				stmt = con.prepareStatement(query);
				stmt.setLong(1, seq);
				stmt.setString(2, paymentType);
				stmt.setString(3, strDate);
				stmt.setDouble(4, totalCollection);
				stmt.setLong(5, receiptCount);
				stmt.setString(6, receiptGenerated ? "Y" : "N");
				stmt.setString(7, user);
				stmt.setTimestamp(8, timestamp);
				stmt.executeUpdate();

			} else {
				query = "UPDATE nt_t_terminal_payment_summary " + "SET tps_total_collection_amt = ?, "
						+ "tps_receipt_count = ?, " + "tps_is_receipt_generated = ?, " + "tps_modified_by = ?, "
						+ "tps_modified_date = ? " + "WHERE tps_payment_type_code = ?" + "AND tps_issue_date = ? ;";

				stmt2 = con.prepareStatement(query);
				stmt2.setDouble(1, totalCollection);
				stmt2.setLong(2, receiptCount);
				stmt2.setString(3, receiptGenerated ? "Y" : "N");
				stmt2.setString(4, user);
				stmt2.setTimestamp(5, timestamp);
				stmt2.setString(6, paymentType);
				stmt2.setString(7, strDate);
				stmt2.executeUpdate();
			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);
		}
		return output;
	}

	@Override
	public String generatePermitNo() {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		String format = null;
		long cnt = 1;
		String generatedCode = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT string_value, type FROM nt_r_parameters WHERE param_name = 'TEMPORARY_PERMIT_NUMBER';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				format = rs.getString("string_value");
				cnt = rs.getLong("type");
			}

			if (format != null) {
				cnt += 1;
				generatedCode = format + cnt;

				query = "UPDATE nt_r_parameters SET type = ? WHERE param_name='TEMPORARY_PERMIT_NUMBER'; ";
				ps2 = con.prepareStatement(query);
				ps2.setString(1, String.valueOf(cnt));
				ps2.executeUpdate();

				con.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return generatedCode;
	}

	@Override
	public boolean updatePermitNoGenerateSeq() {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		String format = null;
		long cnt = 1;
		boolean success = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT string_value, type FROM nt_r_parameters WHERE param_name = 'TEMPORARY_PERMIT_NUMBER';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				format = rs.getString("string_value");
				cnt = rs.getLong("type");
			}

			if (format != null) {
				cnt += 1;

				query = "UPDATE nt_r_parameters SET type = ? WHERE param_name='TEMPORARY_PERMIT_NUMBER'; ";
				ps2 = con.prepareStatement(query);
				ps2.setString(1, String.valueOf(cnt));
				ps2.executeUpdate();

				con.commit();
			}
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return success;
	}

	/* Special Permit */
	public String generateSpecialPermitNo() {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		String format = null;
		long cnt = 1;
		String generatedCode = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT string_value, type FROM nt_r_parameters WHERE param_name = 'SPECIAL_PERMIT_NUMBER';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				format = rs.getString("string_value");
				cnt = rs.getLong("type");
			}

			if (format != null) {
				cnt += 1;
				generatedCode = format + cnt;

				query = "UPDATE nt_r_parameters SET type = ? WHERE param_name='SPECIAL_PERMIT_NUMBER'; ";
				ps2 = con.prepareStatement(query);
				ps2.setString(1, String.valueOf(cnt));
				ps2.executeUpdate();

				con.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return generatedCode;
	}

	public boolean updateSpecialPermitNoGenerateSeq() {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		String format = null;
		long cnt = 1;
		boolean success = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT string_value, type FROM nt_r_parameters WHERE param_name = 'SPECIAL_PERMIT_NUMBER';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				format = rs.getString("string_value");
				cnt = rs.getLong("type");
			}

			if (format != null) {
				cnt += 1;

				query = "UPDATE nt_r_parameters SET type = ? WHERE param_name='SPECIAL_PERMIT_NUMBER'; ";
				ps2 = con.prepareStatement(query);
				ps2.setString(1, String.valueOf(cnt));
				ps2.executeUpdate();

				con.commit();
			}
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public String getServiceTypeByPermitNo(String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String serviceType = null;

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "SELECT pm_service_type FROM nt_t_pm_application WHERE pm_permit_no = ? AND pm_status ='A' ";

			if (query != null) {
				stmt = con.prepareStatement(query);
				stmt.setString(1, permitNo);
				rs = stmt.executeQuery();

				while (rs.next())
					serviceType = rs.getString(1);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return serviceType;
	}

	@Override
	public String generateVoucherNo() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String result = null;

		int year = Year.now().getValue();
		String currYear = Integer.toString(year).substring(2, 4);
		String strVtnNo = "";
		String lastyr = "00";

		try {
			con = ConnectionManager.getConnection();

			String sql = "select tev_terminal_voucher_no FROM public.nt_t_terminal_voucher where tev_terminal_voucher_no is not null "
					+ " ORDER BY tev_created_date desc " + " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("tev_terminal_voucher_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strVtnNo = "VTN" + currYear + ApprecordcountN;
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
					strVtnNo = "VTN" + currYear + ApprecordcountN;
				}
			} else
				strVtnNo = "VTN" + currYear + "00001";

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return strVtnNo;
	}

	@Override
	public boolean updateVoucherNumber(String receiptVal, String vehicleRegNo, String payReciptRef, String modiFyName,
			boolean receiptGenerated) {
		Connection con = null;
		PreparedStatement stmt = null, stmt1 = null;
		ResultSet rs = null;
		boolean output = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		long masterSeq = 0;
		try {
			con = ConnectionManager.getConnection();

			String query = null;
			String query1 = "select tep_seq from public.nt_t_terminal_payment\r\n" + "where tep_vehicleno=?\r\n"
					+ "and tep_receipt_no=?";

			stmt1 = con.prepareStatement(query1);
			stmt1.setString(1, vehicleRegNo);
			stmt1.setString(2, payReciptRef);
			rs = stmt1.executeQuery();

			while (rs.next()) {

				masterSeq = rs.getLong("tep_seq");

			}

			query = "UPDATE public.nt_t_terminal_voucher " + "SET tev_terminal_voucher_no  = ?, "
					+ " tev_is_receipt_generated = ?, " + "tev_modified_by = ?, " + "tev_modified_date = ? "
					+ "WHERE tev_terminal_payment_seq  = ?";

			stmt = con.prepareStatement(query);
			stmt.setString(1, receiptVal);
			stmt.setString(2, receiptGenerated ? "Y" : "N");
			stmt.setString(3, modiFyName);
			stmt.setTimestamp(4, timestamp);
			stmt.setLong(5, masterSeq);

			stmt.executeUpdate();

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			output = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt1);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return output;
	}

	// Get Details by Voucher No
	public List<TerminalPayCancellationDTO> getDetailsByVoucherNo(String voucherNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<TerminalPayCancellationDTO> paymentDetList = new ArrayList<TerminalPayCancellationDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = " SELECT nt_r_terminal_payment_type.description as paymentType,tep_vehicleno as vehicleNo,tep_owner as owner,"
					+ " tep_penaltyamt as penaltyAmt,tep_chargeAmt as chargeAmt,tep_ammount_paid as paidAmt,tep_receipt_no as receiptNo, "
					+ " nt_t_terminal_voucher.tev_seq as seq, case when nt_t_terminal_voucher.tev_status ='A' then 'Active' "
					+ " when nt_t_terminal_voucher.tev_status ='C'  then 'Canceled' else 'N/A' end as status "
					+ " FROM nt_t_terminal_payment inner join nt_t_terminal_voucher on "
					+ " nt_t_terminal_payment.tep_seq = nt_t_terminal_voucher.tev_terminal_payment_seq inner join nt_r_terminal_payment_type on"
					+ " nt_r_terminal_payment_type.code = nt_t_terminal_payment.tep_payment_type_code "
					+ " WHERE nt_t_terminal_voucher.tev_status = 'A' "
					+ " and nt_t_terminal_voucher.tev_terminal_voucher_no = ? ";

			ps = con.prepareStatement(query);
			ps.setString(1, voucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TerminalPayCancellationDTO payCancellationDTO = new TerminalPayCancellationDTO();
				payCancellationDTO.setPaymentTypeDesc(rs.getString("paymentType"));
				payCancellationDTO.setVehicleNo(rs.getString("vehicleNo"));
				payCancellationDTO.setOwner(rs.getString("owner"));
				payCancellationDTO.setPaneltyAmt(rs.getBigDecimal("penaltyAmt"));
				payCancellationDTO.setChargeAmt(rs.getBigDecimal("chargeAmt"));
				payCancellationDTO.setPaidAmt(rs.getBigDecimal("paidAmt"));
				payCancellationDTO.setReceiptNo(rs.getString("receiptNo"));
				payCancellationDTO.setSeq(rs.getLong("seq"));
				payCancellationDTO.setStatus(rs.getString("status"));
				paymentDetList.add(payCancellationDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return paymentDetList;

	}

	// Cancelled Voucher No
	public boolean cancelledPayment(long seq, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isCancelled = false;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE nt_t_terminal_voucher " + " SET tev_status = 'C', tev_modified_by = ? , "
					+ " tev_modified_date= ? " + " WHERE tev_seq = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, user);
			stmt.setTimestamp(2, timestamp);
			stmt.setLong(3, seq);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isCancelled = true;
			} else {
				isCancelled = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isCancelled;
	}

	@Override
	public boolean referenceNoExists(String payReciptRef) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exist = false;
		int i = -1;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_t_terminal_payment WHERE tep_receipt_no = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, payReciptRef);
			rs = ps.executeQuery();

			while (rs.next())
				i = rs.getInt(1);

			if (i > 0)
				exist = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return exist;
	}

	@Override
	public TerminalArrivalDepartureDTO getDetailsByBusNo(String busRegNo, String currentStation) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps_owner = null;
		PreparedStatement ps_terminal = null;
		ResultSet rs = null;
		ResultSet rs_owner = null;
		ResultSet rs_terminal = null;
		TerminalArrivalDepartureDTO busDTO = new TerminalArrivalDepartureDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pm_application_no, a.pm_permit_no, a.pm_permit_issue_date, a.pm_permit_expire_date, "
					+ " a.pm_service_type, serv.description, a.pm_route_no, route.rou_description, route.rou_service_origine, route.rou_service_destination"
					+ " FROM nt_t_pm_application a, nt_r_route route, nt_r_service_types serv"
					+ " WHERE a.pm_vehicle_regno = ? AND a.pm_status = 'A'" + " AND a.pm_route_no = route.rou_number"
					+ " AND a.pm_service_type = serv.code" + " AND serv.active = 'A';";

			ps = con.prepareStatement(query);
			ps.setString(1, busRegNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				busDTO.setApplicationNo(rs.getString(1));
				busDTO.setPermitNo(rs.getString(2));
				busDTO.setPermitIssueDate(rs.getString(3));
				busDTO.setPermitExpireDate(rs.getString(4));
				busDTO.setServiceTypeCode(rs.getString(5));
				busDTO.setServiceType(rs.getString(6));
				busDTO.setRouteNo(rs.getString(7));
				busDTO.setRouteDesc(rs.getString(8));
				busDTO.setOriginDesc(rs.getString(9));
				busDTO.setDestinationDesc(rs.getString(10));
			}
			busDTO.setBusRegNo(busRegNo);

			// get Owner details
			String query_owner = "SELECT vehi.pmo_full_name, vehi.pmo_address1, vehi.pmo_address2, vehi.pmo_address3, vehi.pmo_city, vehi.pmo_mobile_no, vehi.pmo_telephone_no "
					+ "FROM nt_t_pm_vehi_owner vehi, nt_t_pm_application app " + "WHERE vehi.pmo_vehicle_regno = ? "
					+ "AND vehi.pmo_application_no = app.pm_application_no " + "AND app.pm_status ='A'; ";

			ps_owner = con.prepareStatement(query_owner);
			ps_owner.setString(1, busRegNo);
			rs_owner = ps_owner.executeQuery();

			while (rs_owner.next()) {
				busDTO.setOwner(rs_owner.getString(1));
				busDTO.setAddress1(rs_owner.getString(2));
				busDTO.setAddress2(rs_owner.getString(3));
				busDTO.setAddress3(rs_owner.getString(4));
				busDTO.setCity(rs_owner.getString(5));
				busDTO.setMobileNo(rs_owner.getString(6));
				busDTO.setTelephoneNo(rs_owner.getString(7));
			}

			// Get Station, terminal details
			String query_terminal = " SELECT m.br_station_code, station.description, m.br_line_no, m.br_block_id "
					+ " FROM public.nt_m_block_route m, public.nt_t_block_route_det det, nt_r_main_station station "
					+ " WHERE m.br_seq = det.bd_block_route_seq AND station.code = m.br_station_code "
					+ " AND det.bd_status = 'A' AND m.br_status = 'A' "
					+ " AND det.bd_route_no = ? AND det.bd_service_type_code = ? " + " AND br_station_code = ? ;";

			ps_terminal = con.prepareStatement(query_terminal);
			ps_terminal.setString(1, busDTO.getRouteNo());
			ps_terminal.setString(2, busDTO.getServiceTypeCode());
			ps_terminal.setString(3, currentStation);
			rs_terminal = ps_terminal.executeQuery();

			while (rs_terminal.next()) {
				busDTO.setStationCode(rs_terminal.getString(1));
				busDTO.setStationDesc(rs_terminal.getString(2));
				busDTO.setTerminal(rs_terminal.getString(3));
				busDTO.setBlock(rs_terminal.getString(4));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps_owner);
			ConnectionManager.close(ps_terminal);
			ConnectionManager.close(rs);
			ConnectionManager.close(rs_owner);
			ConnectionManager.close(rs_terminal);
			ConnectionManager.close(con);
		}
		return busDTO;
	}

	@Override
	public List<TerminalArrivalDepartureTimeDTO> getScheduledArrivalForDay(
			TerminalArrivalDepartureDTO terminalArrivalData, int dayOfWeek) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TerminalArrivalDepartureTimeDTO> arrivalTimeList = new ArrayList<TerminalArrivalDepartureTimeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String day_clause = "";
			String trip_type_clause = "";

			switch (dayOfWeek) {
			case 0:
				day_clause = "AND d_sunday = 'Y'";
				break;
			case 1:
				day_clause = "AND d_monday = 'Y'";
				break;
			case 3:
				day_clause = "AND d_tuesday = 'Y'";
				break;
			case 4:
				day_clause = "AND d_wednesday = 'Y'";
				break;
			case 5:
				day_clause = "AND d_thursday = 'Y'";
				break;
			case 6:
				day_clause = "AND d_friday = 'Y'";
				break;
			case 7:
				day_clause = "AND d_saturday = 'Y'";
				break;
			default:
			}
			// Currently check origin side only. in future it should be change
			if (terminalArrivalData.getOrigin() != null && terminalArrivalData.getStationCode() != null
					&& terminalArrivalData.getOrigin().equals(terminalArrivalData.getStationCode()))
				trip_type_clause = " AND trip_type = 'D'";
			else if (terminalArrivalData.getDestination() != null && terminalArrivalData.getStationCode() != null
					&& terminalArrivalData.getDestination().equals(terminalArrivalData.getStationCode()))
				trip_type_clause = " AND trip_type = 'O'";

			String query = "SELECT * FROM public.nt_m_timetable_generator_det "
					+ " WHERE assigned_bus_no = ? and trip_type  ='O'  "
					+ " AND generator_ref_no IN (SELECT ref_no FROM nt_m_panelgenerator WHERE route_no = ? AND bus_category = ? AND status = 'A')"
					+ " AND group_no IN (SELECT group_no FROM nt_t_panelgenerator_det "
					+ "				WHERE seq_panelgenerator IN (SELECT seq FROM nt_m_panelgenerator WHERE route_no = ? AND bus_category = ? AND status = 'A') "
					+ day_clause + ")" + " " + trip_type_clause + "ORDER BY end_time_slot;";

			ps = con.prepareStatement(query);
			ps.setString(1, terminalArrivalData.getBusRegNo());
			ps.setString(2, terminalArrivalData.getRouteNo());
			ps.setString(3, terminalArrivalData.getServiceTypeCode());
			ps.setString(4, terminalArrivalData.getRouteNo());
			ps.setString(5, terminalArrivalData.getServiceTypeCode());
			rs = ps.executeQuery();

			while (rs.next()) {
				TerminalArrivalDepartureTimeDTO dto = new TerminalArrivalDepartureTimeDTO();
				dto.setStartTime(rs.getString("start_time_slot"));
				dto.setEndTime(rs.getString("end_time_slot"));
				arrivalTimeList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return arrivalTimeList;
	}

	@Override
	public List<TerminalArrivalDepartureTimeDTO> getScheduledDepartureForDay(
			TerminalArrivalDepartureDTO terminalDepartureData, int dayOfWeek) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TerminalArrivalDepartureTimeDTO> departureTimeList = new ArrayList<TerminalArrivalDepartureTimeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String day_clause = "";
			String trip_type_clause = "";

			switch (dayOfWeek) {
			case 0:
				day_clause = "AND d_sunday = 'Y'";
				break;
			case 1:
				day_clause = "AND d_monday = 'Y'";
				break;
			case 3:
				day_clause = "AND d_tuesday = 'Y'";
				break;
			case 4:
				day_clause = "AND d_wednesday = 'Y'";
				break;
			case 5:
				day_clause = "AND d_thursday = 'Y'";
				break;
			case 6:
				day_clause = "AND d_friday = 'Y'";
				break;
			case 7:
				day_clause = "AND d_saturday = 'Y'";
				break;
			default:
			}

			if (terminalDepartureData.getOrigin() != null && terminalDepartureData.getStationCode() != null
					&& terminalDepartureData.getOrigin().equals(terminalDepartureData.getStationCode()))
				trip_type_clause = " AND trip_type = 'O'";
			else if (terminalDepartureData.getDestination() != null && terminalDepartureData.getStationCode() != null
					&& terminalDepartureData.getDestination().equals(terminalDepartureData.getStationCode()))
				trip_type_clause = " AND trip_type = 'D'";

			String query = "SELECT * FROM public.nt_m_timetable_generator_det "
					+ " WHERE assigned_bus_no  = ? and trip_type  ='O' "
					+ " AND generator_ref_no IN (SELECT ref_no FROM nt_m_panelgenerator WHERE route_no = ? AND bus_category = ? AND status = 'A')"
					+ " AND group_no IN (SELECT group_no FROM nt_t_panelgenerator_det "
					+ "				WHERE seq_panelgenerator IN (SELECT seq FROM nt_m_panelgenerator WHERE route_no = ? AND bus_category = ? AND status = 'A') "
					+ day_clause + ")" + " " + trip_type_clause + "ORDER BY start_time_slot;";

			ps = con.prepareStatement(query);
			ps.setString(1, terminalDepartureData.getBusRegNo());
			ps.setString(2, terminalDepartureData.getRouteNo());
			ps.setString(3, terminalDepartureData.getServiceTypeCode());
			ps.setString(4, terminalDepartureData.getRouteNo());
			ps.setString(5, terminalDepartureData.getServiceTypeCode());
			rs = ps.executeQuery();

			while (rs.next()) {
				TerminalArrivalDepartureTimeDTO dto = new TerminalArrivalDepartureTimeDTO();
				dto.setStartTime(rs.getString("start_time_slot"));
				dto.setEndTime(rs.getString("end_time_slot"));
				departureTimeList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return departureTimeList;
	}

	@Override
	public TerminalArrivalDepartureDTO insertTerminalArrivalLog(TerminalArrivalDepartureDTO scheduleListForDay,
			int dayOfWeek, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps_cnt = null;
		ResultSet rs_cnt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<TerminalArrivalDepartureTimeDTO> timeList = new ArrayList<TerminalArrivalDepartureTimeDTO>();
		int cnt = 0;

		try {
			con = ConnectionManager.getConnection();

			if (scheduleListForDay == null || scheduleListForDay.getScheduledTimeListForDay() == null)
				return null;

			// exit if records found for the same day
			String query_cnt = "SELECT seq, trip_starttime, estimated_arrival, actual_arrival, trip_type, authorized, authfail_reasons FROM nt_t_terminal_arrival_log WHERE bus_no = ? AND service_type = ? AND route_no = ? AND station = ? AND arrival_date = ? ORDER BY estimated_arrival;";
			ps_cnt = con.prepareStatement(query_cnt);
			ps_cnt.setString(1, scheduleListForDay.getBusRegNo());
			ps_cnt.setString(2, scheduleListForDay.getServiceTypeCode());
			ps_cnt.setString(3, scheduleListForDay.getRouteNo());
			ps_cnt.setString(4, scheduleListForDay.getStationCode());
			ps_cnt.setString(5, dateFormat.format(date));
			rs_cnt = ps_cnt.executeQuery();

			while (rs_cnt.next()) {
				TerminalArrivalDepartureTimeDTO data = new TerminalArrivalDepartureTimeDTO();
				data.setSequence(rs_cnt.getLong(1));
				data.setStartTime(rs_cnt.getString(2));
				data.setEndTime(rs_cnt.getString(3));
				data.setActualArrivalDeparture(rs_cnt.getString(4));
				data.setTripType(rs_cnt.getString(5));
				data.setAuthorized(rs_cnt.getString(6));
				data.setAuthorizationFailReasons(rs_cnt.getString(7));
				timeList.add(data);
				cnt++;
			}

			ConnectionManager.close(rs_cnt);

			if (cnt > 0) {
				scheduleListForDay.setScheduledTimeListForDay(timeList);
				return scheduleListForDay;
			}

			// insert data
			for (TerminalArrivalDepartureTimeDTO schedule : scheduleListForDay.getScheduledTimeListForDay()) {
				long seq = Utils.getNextValBySeqName(con, "seq_nt_t_terminal_arrival_log");
				schedule.setSequence(seq);

				String query = "INSERT INTO public.nt_t_terminal_arrival_log "
						+ "(seq, bus_no, service_type, permit_no, route_no, origin_code, destination_code, station, terminal, block, "
						+ " trip_type, arrival_date, trip_starttime, estimated_arrival, actual_arrival, turn_no, authorized, authfail_reasons, arrival_day, created_by, created_date,isDepart) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

				ps = con.prepareStatement(query);
				ps.setLong(1, seq);
				ps.setString(2, scheduleListForDay.getBusRegNo());
				ps.setString(3, scheduleListForDay.getServiceTypeCode());
				ps.setString(4, scheduleListForDay.getPermitNo());
				ps.setString(5, scheduleListForDay.getRouteNo());
				ps.setString(6, scheduleListForDay.getOriginDesc());
				ps.setString(7, scheduleListForDay.getDestinationDesc());
				ps.setString(8, scheduleListForDay.getStationCode());
				ps.setString(9, scheduleListForDay.getTerminal());
				ps.setString(10, scheduleListForDay.getBlock());
				ps.setString(11, schedule.getTripType());
				ps.setString(12, dateFormat.format(date));
				ps.setString(13, schedule.getStartTime());
				ps.setString(14, schedule.getStartTime());
				ps.setString(15, schedule.getActualArrivalDeparture());
				ps.setInt(16, scheduleListForDay.getTurnNo());
				ps.setString(17, schedule.getAuthorized());
				ps.setString(18, schedule.getAuthorizationFailReasons());
				ps.setInt(19, dayOfWeek);
				ps.setString(20, user);
				ps.setTimestamp(21, timestamp);
				ps.setString(22, "N");
				ps.executeUpdate();
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs_cnt);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps_cnt);
			ConnectionManager.close(con);
		}
		return scheduleListForDay;
	}

	@Override
	public TerminalArrivalDepartureDTO insertTerminalDepartureLog(TerminalArrivalDepartureDTO scheduleListForDay,
			int dayOfWeek, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps_cnt = null;
		ResultSet rs_cnt = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<TerminalArrivalDepartureTimeDTO> timeList = new ArrayList<TerminalArrivalDepartureTimeDTO>();
		int cnt = 0;

		try {
			con = ConnectionManager.getConnection();

			if (scheduleListForDay == null || scheduleListForDay.getScheduledTimeListForDay() == null)
				return null;

			// exit if records found for the same day
			String query_cnt = "SELECT seq, estimated_departure, actual_departure, trip_endtime, trip_type, authorized, authfail_reasons FROM nt_t_terminal_departure_log WHERE bus_no = ? AND service_type = ? AND route_no = ? AND station = ? AND departure_date = ? ;";
			ps_cnt = con.prepareStatement(query_cnt);
			ps_cnt.setString(1, scheduleListForDay.getBusRegNo());
			ps_cnt.setString(2, scheduleListForDay.getServiceTypeCode());
			ps_cnt.setString(3, scheduleListForDay.getRouteNo());
			ps_cnt.setString(4, scheduleListForDay.getStationCode());
			ps_cnt.setString(5, dateFormat.format(date));
			rs_cnt = ps_cnt.executeQuery();

			while (rs_cnt.next()) {
				TerminalArrivalDepartureTimeDTO data = new TerminalArrivalDepartureTimeDTO();
				data.setSequence(rs_cnt.getLong(1));
				data.setStartTime(rs_cnt.getString(2));
				data.setActualArrivalDeparture(rs_cnt.getString(3));
				data.setEndTime(rs_cnt.getString(4));
				data.setTripType(rs_cnt.getString(5));
				data.setAuthorized(rs_cnt.getString(6));
				data.setAuthorizationFailReasons(rs_cnt.getString(7));
				timeList.add(data);
				cnt++;
			}

			ConnectionManager.close(rs_cnt);

			if (cnt > 0) {
				scheduleListForDay.setScheduledTimeListForDay(timeList);
				return scheduleListForDay;
			}

			// insert data
			for (TerminalArrivalDepartureTimeDTO schedule : scheduleListForDay.getScheduledTimeListForDay()) {
				long seq = Utils.getNextValBySeqName(con, "seq_nt_t_terminal_departure_log");
				schedule.setSequence(seq);

				String query = "INSERT INTO public.nt_t_terminal_departure_log "
						+ "(seq, bus_no, service_type, permit_no, route_no, origin, destination, station, terminal, block, "
						+ " trip_type, departure_date, estimated_departure, actual_departure, trip_endtime, turn_no, authorized, authfail_reasons, departure_day, created_by, created_date) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

				ps = con.prepareStatement(query);
				ps.setLong(1, seq);
				ps.setString(2, scheduleListForDay.getBusRegNo());
				ps.setString(3, scheduleListForDay.getServiceTypeCode());
				ps.setString(4, scheduleListForDay.getPermitNo());
				ps.setString(5, scheduleListForDay.getRouteNo());
				ps.setString(6, scheduleListForDay.getOriginDesc());
				ps.setString(7, scheduleListForDay.getDestinationDesc());
				ps.setString(8, scheduleListForDay.getStationCode());
				ps.setString(9, scheduleListForDay.getTerminal());
				ps.setString(10, scheduleListForDay.getBlock());
				ps.setString(11, schedule.getTripType());
				ps.setString(12, dateFormat.format(date));
				ps.setString(13, schedule.getStartTime());
				ps.setString(14, schedule.getActualArrivalDeparture());
				// ps.setString(14, schedule.getStartTime());
				ps.setString(15, schedule.getEndTime());
				ps.setInt(16, scheduleListForDay.getTurnNo());
				ps.setString(17, schedule.getAuthorized());
				ps.setString(18, schedule.getAuthorizationFailReasons());
				ps.setInt(19, dayOfWeek);
				ps.setString(20, user);
				ps.setTimestamp(21, timestamp);
				ps.executeUpdate();
			}
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs_cnt);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps_cnt);
			ConnectionManager.close(con);
		}
		return scheduleListForDay;
	}

	@Override
	public boolean updateTerminalArrivalLog(Long sequence, String actualArrivalTime, int turnNo, String authorized,
			String errorMsgs, int dayOfWeek, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_terminal_arrival_log "
					+ " SET actual_arrival = ? , turn_no = ? , authorized = ?, authfail_reasons = ?, modified_by = ?, modified_date = ?"
					+ " WHERE seq = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, actualArrivalTime);
			ps.setInt(2, turnNo);
			ps.setString(3, authorized);
			ps.setString(4, errorMsgs);
			ps.setString(5, user);
			ps.setTimestamp(6, timestamp);
			ps.setLong(7, sequence);
			ps.executeUpdate();

			con.commit();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean updateTerminalDepartureLog(Long arrivalSeq, Long sequence, String actualDepartureTime, int turnNo,
			String authorized, String errorMsgs, int dayOfWeek, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_t_terminal_departure_log "
					+ " SET actual_departure = ? , turn_no = ? , authorized = ?, authfail_reasons = ?, modified_by = ?, modified_date = ?,arrival_seq = ?"
					+ " WHERE seq = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, actualDepartureTime);
			ps.setInt(2, turnNo);
			ps.setString(3, authorized);
			ps.setString(4, errorMsgs);
			ps.setString(5, user);
			ps.setTimestamp(6, timestamp);
			ps.setLong(7, arrivalSeq);
			ps.setLong(8, sequence);
			int i = ps.executeUpdate();

			if (i > 0) {
				String query2 = "UPDATE public.nt_t_terminal_arrival_log " + " SET isdepart = ? " + " WHERE seq = ?;";

				ps2 = con.prepareStatement(query2);
				ps2.setString(1, "Y");
				ps2.setLong(2, arrivalSeq);
				ps2.executeUpdate();

			}

			con.commit();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public boolean updateServiceAmounts(String paymentType, String serviceType, double basicAmountOld,
			double penaltyOld, double basicAmount, double penalty, String user) {
		Connection con = null;
		PreparedStatement ps_update_h = null;
		PreparedStatement ps_update = null;
		boolean success = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			if (serviceType == null) {
				// update history
				String query_update_h = "UPDATE nt_h_terminal_payment_det SET amount = ?, penalty_fee = ?, modified_by = ?, modified_date = ?"
						+ " WHERE payment_type = ? AND active = 'A';";
				ps_update_h = con.prepareStatement(query_update_h);
				ps_update_h.setDouble(1, basicAmountOld);
				ps_update_h.setDouble(2, penaltyOld);
				ps_update_h.setString(3, user);
				ps_update_h.setTimestamp(4, timestamp);
				ps_update_h.setString(5, paymentType);
				ps_update_h.executeUpdate();

				// update
				String query_update = "UPDATE nt_r_terminal_payment_det SET amount = ?, penalty_fee = ?, modified_by = ?, modified_date = ?"
						+ " WHERE payment_type = ? AND active = 'A';";

				ps_update = con.prepareStatement(query_update);
				ps_update.setDouble(1, basicAmount);
				ps_update.setDouble(2, penalty);
				ps_update.setString(3, user);
				ps_update.setTimestamp(4, timestamp);
				ps_update.setString(5, paymentType);
				ps_update.executeUpdate();

			} else {

				// update history
				String query_update_h = "UPDATE nt_h_terminal_payment_det SET amount = ?, penalty_fee = ?, modified_by = ?, modified_date = ?"
						+ " WHERE payment_type = ?  AND service_type = ?  AND active = 'A';";
				ps_update_h = con.prepareStatement(query_update_h);
				ps_update_h.setDouble(1, basicAmountOld);
				ps_update_h.setDouble(2, penaltyOld);
				ps_update_h.setString(3, user);
				ps_update_h.setTimestamp(4, timestamp);
				ps_update_h.setString(5, paymentType);
				ps_update_h.setString(6, serviceType);
				ps_update_h.executeUpdate();

				// update
				String query_update = "UPDATE nt_r_terminal_payment_det SET amount = ?, penalty_fee = ?, modified_by = ?, modified_date = ?"
						+ " WHERE payment_type = ?  AND service_type = ?  AND active = 'A';";

				ps_update = con.prepareStatement(query_update);
				ps_update.setDouble(1, basicAmount);
				ps_update.setDouble(2, penalty);
				ps_update.setString(3, user);
				ps_update.setTimestamp(4, timestamp);
				ps_update.setString(5, paymentType);
				ps_update.setString(6, serviceType);
				ps_update.executeUpdate();

			}

			con.commit();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			ConnectionManager.close(ps_update_h);
			ConnectionManager.close(ps_update);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public List<TerminalDetailsDTO> getRecieptRefList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TerminalDetailsDTO> TerminalDetailsDTO = new ArrayList<TerminalDetailsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tep_receipt_no from public.nt_t_terminal_payment;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				TerminalDetailsDTO dto = new TerminalDetailsDTO();
				dto.setReceiptRefNo(rs.getString("tep_receipt_no"));
				TerminalDetailsDTO.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return TerminalDetailsDTO;
	}

	@Override
	public List<TerminalDetailsDTO> getReceiptRefAggainstPayType(String selectedPayType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TerminalDetailsDTO> TerminalDetailsDTO = new ArrayList<TerminalDetailsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tep_receipt_no from public.nt_t_terminal_payment\r\n"
					+ "where tep_payment_type_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedPayType);
			rs = ps.executeQuery();

			while (rs.next()) {
				TerminalDetailsDTO dto = new TerminalDetailsDTO();
				dto.setReceiptRefNo(rs.getString("tep_receipt_no"));
				TerminalDetailsDTO.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return TerminalDetailsDTO;
	}

	@Override
	public TerminalDetailsDTO showsearchedData(String selectedPayType, String recieptRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TerminalDetailsDTO TMdto = new TerminalDetailsDTO();
		String query = null;

		try {
			con = ConnectionManager.getConnection();

			if (selectedPayType.equalsIgnoreCase("001")) // logsheet issuance
			{
				query = "select tep_permit_no,tep_origin,tep_destination,tep_owner,tep_gender,b.tev_terminal_voucher_no,tep_valid_to ,\r\n"
						+ "tep_contact_no,tep_no_of_turns,tep_vehicleno,tep_issued_date,tep_issued_month,tep_valid_from,tep_penaltyamt,tep_ammount_paid,tep_paid_date,tep_due_date,s.description \r\n"
						+ "from public.nt_t_terminal_payment\r\n"
						+ "inner join public.nt_t_terminal_voucher b on b.tev_terminal_payment_seq=public.nt_t_terminal_payment.tep_seq\r\n"
						+ "left outer join public.nt_r_main_station  s on s.code =public.nt_t_terminal_payment.tep_terminal_location\r\n"
						+ "where tep_payment_type_code=? and tep_receipt_no=?;";
			} else // special hire or temporary permit
			{
				query = "select tep_permit_no,tep_st_origin as tep_origin, tep_st_destination as tep_destination,tep_st_owner_name as tep_owner,"
						+ " tep_st_gender as tep_gender,b.tev_terminal_voucher_no,tep_valid_to ,\r\n"
						+ " tep_st_contact_no as tep_contact_no,tep_no_of_turns,tep_vehicleno,tep_issued_date,tep_issued_month,tep_valid_from,tep_penaltyamt,tep_ammount_paid,tep_paid_date,tep_due_date,s.description \r\n"
						+ "from public.nt_t_terminal_payment\r\n"
						+ "inner join public.nt_t_terminal_voucher b on b.tev_terminal_payment_seq=public.nt_t_terminal_payment.tep_seq\r\n"
						+ "left outer join public.nt_r_main_station  s on s.code =public.nt_t_terminal_payment.tep_terminal_location\r\n"
						+ "where tep_payment_type_code=? and tep_receipt_no=?;";
			}

			ps = con.prepareStatement(query);
			ps.setString(1, selectedPayType);
			ps.setString(2, recieptRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				TMdto.setPermitNo(rs.getString("tep_permit_no"));
				TMdto.setRouteOrigin(rs.getString("tep_origin"));
				TMdto.setRouteDestination(rs.getString("tep_destination"));
				TMdto.setOwner(rs.getString("tep_owner"));
				if (rs.getString("tep_gender").equalsIgnoreCase("F")) {
					TMdto.setGender("Female");
				} else if (rs.getString("tep_gender").equalsIgnoreCase("M")) {
					TMdto.setGender("Male");
				}
				TMdto.setVoucherNO(rs.getString("tev_terminal_voucher_no"));
				TMdto.setContactNo(rs.getString("tep_contact_no"));
				TMdto.setValidTo(rs.getString("tep_valid_to"));
				TMdto.setNoOfTurns(rs.getString("tep_no_of_turns"));
				TMdto.setVehiNo(rs.getString("tep_vehicleno"));
				TMdto.setIssuDate(rs.getString("tep_issued_date"));
				TMdto.setIssueMont(rs.getString("tep_issued_month"));
				TMdto.setDueDate(rs.getString("tep_valid_to"));
				TMdto.setValidFrom(rs.getString("tep_valid_from"));
				TMdto.setPenalty(Integer.toString(rs.getInt("tep_penaltyamt")));
				TMdto.setAmountPaid(Integer.toString(rs.getInt("tep_ammount_paid")));
				TMdto.setPaidDate(rs.getString("tep_paid_date"));
				TMdto.setDueDate(rs.getString("tep_due_date"));
				TMdto.setTerminalLocation(rs.getString("description"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return TMdto;
	}

	@Override
	public boolean checkReceiptCanceled(String selectedRfNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isCanceled = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select b.tev_status\r\n" + "from public.nt_t_terminal_voucher b\r\n"
					+ "inner join public.nt_t_terminal_payment a on a.tep_seq=b.tev_terminal_payment_seq\r\n"
					+ "where a.tep_receipt_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedRfNo);

			rs = ps.executeQuery();

			if (rs.next()) {
				if (rs.getString("tev_status").equals("C"))
					isCanceled = true;
			} else {
				isCanceled = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isCanceled;
	}

	@Override
	public int getActiveTerminalBlockCount(long sequence) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int cnt = 0;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT COUNT(*) FROM public.nt_m_terminal_block m, nt_t_terminal_block_det t"
					+ " WHERE m.seq = t.terminal_block_seq " + " AND t.status = 'A'"
					+ " AND m.station_terminal_det_seq = ?";

			ps = con.prepareStatement(query);
			ps.setLong(1, sequence);
			rs = ps.executeQuery();

			if (rs.next())
				cnt = rs.getInt(1);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return cnt;
	}

	@Override
	public List<CommonDTO> getPaymentModes() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonDTO> paymentDTOList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT * FROM nt_r_terminal_payment_mode WHERE active = 'A';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO dto = new CommonDTO();
				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));
				paymentDTOList.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return paymentDTOList;
	}

	@Override
	public PermitDTO getTemporaryPermitInfoByBusNoPermitNo(String permitNo, String busNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitDTO permitDTO = new PermitDTO();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			if (permitNo != null) {
				String query = "select tep_seq ,tep_permit_no , tep_vehicleno, tep_st_origin ,tep_st_destination , tep_st_via ,tep_st_owner_name ,\r\n"
						+ "tep_st_gender , tep_st_route_no ,  tep_service_type , d.description\r\n"
						+ " from nt_t_terminal_payment  left outer join nt_r_service_types d on d.code =tep_service_type \r\n"
						+ "where tep_payment_type_code = '002' \r\n" + "and tep_permit_no =?;";
				ps = con.prepareStatement(query);
				ps.setString(1, permitNo);
				rs = ps.executeQuery();

			} else {
				String query = "select tep_seq ,tep_permit_no , tep_vehicleno, tep_st_origin ,tep_st_destination , tep_st_via ,tep_st_owner_name ,\r\n"
						+ "  tep_st_gender  , tep_st_route_no ,  tep_service_type , d.description\r\n"
						+ " from nt_t_terminal_payment left outer join nt_r_service_types d on d.code =tep_service_type \r\n"
						+ "where tep_payment_type_code = '002' \r\n" + "and tep_vehicleno =?;";

				ps = con.prepareStatement(query);
				ps.setString(1, busNo);
				rs = ps.executeQuery();
			}

			while (rs.next()) {
				permitDTO.setSeq(rs.getLong(1));
				permitDTO.setPermitNo(rs.getString(2));
				permitDTO.setBusRegNo(rs.getString(3));
				permitDTO.setOrigin(rs.getString(4));
				permitDTO.setDestination(rs.getString(5));
				permitDTO.setVia(rs.getString(6));
				permitDTO.setVehicleOwner(rs.getString(7));
				permitDTO.setOwnerGender(rs.getString(8));
				permitDTO.setRouteNo(rs.getString(9));
				permitDTO.setServiceType(rs.getString(10));
				permitDTO.setServiceTypeDesc(rs.getString(11));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return permitDTO;
	}

	@Override
	public String getRouteAndServiceTypeByBusNo(String busNo, boolean bool) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String returnVal = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_route_no , pm_service_type  FROM public.nt_t_pm_application x\r\n"
					+ "WHERE pm_vehicle_regno =? and pm_status='A';";
			ps = con.prepareStatement(query);
			ps.setString(1, busNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				if (bool) {
					returnVal = rs.getString("pm_route_no");
				} else {
					returnVal = rs.getString("pm_service_type");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return returnVal;
	}

	@Override
	public TerminalArrivalDepartureDTO getPanelGeneratorDetails(String refNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		TerminalArrivalDepartureDTO dto = new TerminalArrivalDepartureDTO();
		String query = null;

		try {
			con = ConnectionManager.getConnection();

			query = "select distinct rs_generator_ref_no ,rs_end_date ,rs_start_date ,rs_no_of_dates  from public.nt_m_route_schedule_generator  "
					+ "where rs_generator_ref_no=?  ;";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				dto = new TerminalArrivalDepartureDTO();
				dto.setPanelGenNumber(rs.getString("rs_generator_ref_no"));
				dto.setStartDate(rs.getString("rs_start_date"));
				dto.setEndDate(rs.getString("rs_end_date"));
				dto.setNoOfDays(rs.getString("rs_no_of_dates"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}
		return dto;
	}

	@Override
	public boolean checkEnteredBusLeave(String busNo, String tripType, String refNo, int dayNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> dayBusList = new ArrayList<>();
		boolean leave = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select tr_bus_no FROM public.nt_t_terminal_time_table_schedule where tr_panel_ref_no = ? and tr_day = ?;";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setInt(2, dayNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				dayBusList.add(rs.getString("tr_bus_no"));
			}

			if (!dayBusList.contains(busNo)) {
				leave = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return leave;
	}

	@Override
	public List<String> checkEnteredBusForInOut(String busNo, String tripType, String refNo, int dayNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String enterTime = null;
		;
		List<String> enterTimeList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();
			// trip Type D means for arrival terminal side
			if (tripType.equals("D")) {

				String query = "select t.bus_num ,t.start_time_slot as start_time_slot  ,r.bus_no \r\n"
						+ "from public.nt_m_timetable_generator_det t\r\n"
						+ "inner join public.nt_t_route_schedule_generator_det01 r on r.generator_ref_no =t.generator_ref_no and t.trip_type=r.trip_type \r\n"
						+ "and r.bus_no =t.bus_num \r\n"
						+ "where t.generator_ref_no =? and t.assigned_bus_no =? and r.day_no =?and t.trip_type=? order by t.trip_id;";

				ps = con.prepareStatement(query);
				ps.setString(1, refNo);
				ps.setString(2, busNo);
				ps.setInt(3, dayNo);
				ps.setString(4, "O");
			}
			// trip Type O means for departure terminal side
			else if (tripType.equals("O")) {
				String query = "select t.bus_num ,t.start_time_slot  as start_time_slot ,r.bus_no \r\n"
						+ "from public.nt_m_timetable_generator_det t\r\n"
						+ "inner join public.nt_t_route_schedule_generator_det01 r on r.generator_ref_no =t.generator_ref_no and t.trip_type=r.trip_type \r\n"
						+ "and r.bus_no =t.bus_num \r\n"
						+ "where t.generator_ref_no =? and t.assigned_bus_no =? and r.day_no =? and t.trip_type=? order by t.trip_id;";

				ps = con.prepareStatement(query);
				ps.setString(1, refNo);
				ps.setString(2, busNo);
				ps.setInt(3, dayNo);
				ps.setString(4, "O");
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				enterTime = null;
				enterTime = rs.getString("start_time_slot");
				if (enterTime != null && !enterTime.isEmpty()) {
					enterTimeList.add(enterTime);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return enterTimeList;
	}

	@Override
	public String getParamTimeAdded(String paramName) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String number = null;
		;
		try {

			con = ConnectionManager.getConnection();

			String query = "select number_value  from public.nt_r_parameters where param_name =?;";

			ps = con.prepareStatement(query);
			ps.setString(1, paramName);

			rs = ps.executeQuery();

			while (rs.next()) {
				number = rs.getString("number_value");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return number;
	}

	@Override
	public List<String> getActivePanelgeneratorNumber(String busNum,int day) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> refNo = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct tr_panel_ref_no from public.nt_t_terminal_time_table_schedule where	tr_bus_no = ? and tr_day = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, busNum);
			ps.setInt(2, day);

			rs = ps.executeQuery();

			while (rs.next()) {
				refNo.add(rs.getString("tr_panel_ref_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return refNo;
	}

	@Override
	public boolean checkPermitStatusInActive(String busNumber) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean valid = false;

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "SELECT  pm_status , pm_service_type  FROM public.nt_t_pm_application x\r\n"
					+ "WHERE pm_vehicle_regno =? and  pm_status  in('A','P') limit 1;";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNumber);
			rs = stmt.executeQuery();

			while (rs.next()) {
				if (rs.getString("pm_status").equalsIgnoreCase("A")
						|| rs.getString("pm_status").equalsIgnoreCase("P")) {

					valid = true;
				} else {
					valid = false;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			valid = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return valid;
	}

	@Override
	public boolean checkRouteCorrectForStation(String route, String service, String stationCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean valid = false;

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "select bd_route_no from public.nt_t_block_route_det bd \r\n"
					+ "   inner join public.nt_m_block_route bm on bm.br_seq =bd.bd_block_route_seq \r\n"
					+ "   inner join nt_r_main_station s on s.code = bm.br_station_code  and s.active ='A'\r\n"
					+ "   where bd.bd_route_no =?and bd.bd_service_type_code =? and bm.br_station_code =? ;";

			stmt = con.prepareStatement(query);
			stmt.setString(1, route);
			stmt.setString(2, service);
			stmt.setString(3, stationCode);
			rs = stmt.executeQuery();

			while (rs.next()) {
				if (rs.getString("bd_route_no") != null) {

					valid = true;
				} else {
					valid = false;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			valid = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return valid;
	}

	@Override
	public String getNoOfCouplesForRefNo(String refNo, String busNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String couples = null;
		;
		try {

			con = ConnectionManager.getConnection();

			String query = "select no_of_couples_per_one_bus from public.nt_m_timetable_generator where generator_ref_no =? and status ='A' and no_of_couples_per_one_bus is not null ;";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				couples = rs.getString("no_of_couples_per_one_bus");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return couples;
	}

	@Override
	public int getNoOfTrips(String busNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int trips = 0;
		;
		try {

			con = ConnectionManager.getConnection();

			String query = "select count(*) as tripCount from public.nt_t_terminal_arrival_log where bus_no =? and arrival_date= ? and actual_arrival  is not null;";

			ps = con.prepareStatement(query);
			ps.setString(1, busNo);
			ps.setString(2, dateFormat.format(date));

			rs = ps.executeQuery();

			while (rs.next()) {
				trips = rs.getInt("tripCount");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return trips;
	}

	@Override
	public boolean checkDriverConductorSuspend(String busNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean blackListed = false;

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "select count(*) as blackListedCount from public.nt_t_driver_conductor_registration where dcr_vehicle_reg_no =? and dcr_status ='B'";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				if (rs.getInt("blackListedCount") > 0) {

					blackListed = true;
				} else {
					blackListed = false;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			blackListed = false;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return blackListed;
	}

	@Override
	public boolean isPaymentDoneForCurrentMonth(String busNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean paymentDOne = false;
		LocalDate currentdate = LocalDate.now();
		int currentMonth = currentdate.getMonthValue();
		int currentYear = currentdate.getYear();
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "  select tep_issued_month from public.nt_t_terminal_payment where tep_vehicleno =? and tep_payment_type_code ='001'";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				String monthYear = rs.getString("tep_issued_month");

				String yearS = (monthYear.trim()).substring(0, 4);
				String currentYearS = Integer.toString(currentYear).trim();

				String monthS = (monthYear.trim()).substring(6, 7);
				String currentMonthS = Integer.toString(currentMonth).trim();
				if (yearS.equals(currentYearS) && monthS.equals(currentMonthS)) {

					paymentDOne = true;

				} else {
					paymentDOne = false;
				}

				/*
				 * if ((((monthYear.trim().substring(0,
				 * 4)).trim()).equals(Integer.toString(currentYear).trim())) &&
				 * ((monthYear.trim().substring(5,
				 * 7)).trim()).equals(Integer.toString(currentMonth).trim())) {
				 * 
				 * paymentDOne = true; System.out.println("year" + monthYear.substring(0, 3) +
				 * "month" + monthYear.substring(5, 6)); System.out.println("cyear" +
				 * currentYear + "cmonth" + currentMonth);
				 * 
				 * } else { paymentDOne = false; }
				 */

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return paymentDOne;
	}

	@Override
	public boolean checkBusIsArrived(String busNo, String busRoute, String busService, String station) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean busArrived = false;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cDate);
		calendar.add(Calendar.DATE, -1);
		Date yDate = calendar.getTime();

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strtoday = dateFormat.format(cDate);
		String strDatePrevious = dateFormat.format(yDate);
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "select arrival_date  from nt_t_terminal_arrival_log where bus_no =? and route_no =? and service_type =? and station =? and actual_arrival is not null order by modified_date desc ";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			stmt.setString(2, busRoute);
			stmt.setString(3, busService);
			stmt.setString(4, station);

			rs = stmt.executeQuery();

			while (rs.next()) {

				if (rs.getString("arrival_date").trim().equals(strtoday.trim())
						|| rs.getString("arrival_date").trim().equals(strDatePrevious.trim())) {

					busArrived = true;

				} else {
					busArrived = false;
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return busArrived;
	}

	@Override
	public String getStationNameByID(String code) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String name = null;
		;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT description FROM public.nt_r_main_station where code =? and active ='A';";

			ps = con.prepareStatement(query);
			ps.setString(1, code);

			rs = ps.executeQuery();

			while (rs.next()) {
				name = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return name;
	}

	@Override
	public boolean checkBusDepartured(String busNo, String busRoute, String busService, String station) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean busDepart = false;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cDate);
		calendar.add(Calendar.DATE, -1);
		Date yDate = calendar.getTime();

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strtoday = dateFormat.format(cDate);
		String strDatePrevious = dateFormat.format(yDate);
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "select a.arrival_date  from nt_t_terminal_arrival_log a\r\n"
					+ "inner join nt_t_terminal_departure_log d on d.bus_no =a.bus_no  and d.service_type =a.service_type \r\n"
					+ "and d.route_no =a.route_no and d.station =a.station \r\n"
					+ "where a.bus_no =? and a.route_no =? and a.seq =d.arrival_seq \r\n"
					+ "and a.service_type =? and a.station =? and actual_arrival is not null and d.actual_departure  is not null; ";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			stmt.setString(2, busRoute);
			stmt.setString(3, busService);
			stmt.setString(4, station);

			rs = stmt.executeQuery();

			while (rs.next()) {
				if (rs.getString("arrival_date").equals(null) || rs.getString("arrival_date").isEmpty()) {
					busDepart = false;
				} else if (rs.getString("arrival_date").trim().equals(strtoday.trim())
						|| rs.getString("arrival_date").trim().equals(strDatePrevious.trim())) {

					busDepart = true;

				} else {
					busDepart = false;
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return busDepart;
	}

	@Override
	public int getNoOfTurns(String busNo, String busRoute, String busService, String station) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int turnNo = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cDate);
		calendar.add(Calendar.DATE, -1);
		Date yDate = calendar.getTime();

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strtoday = dateFormat.format(cDate);
		String strDatePrevious = dateFormat.format(yDate);
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "select max(turn_no ) as turn_no    from nt_t_terminal_arrival_log where bus_no =? and route_no =? and service_type =? and station =? and actual_arrival is not null and to_char(created_date,'yyyy-MM-dd')=? ";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			stmt.setString(2, busRoute);
			stmt.setString(3, busService);
			stmt.setString(4, station);
			stmt.setString(5, strtoday.trim());

			rs = stmt.executeQuery();

			while (rs.next()) {
				turnNo = rs.getInt("turn_no");
				turnNo++;

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return turnNo;

	}

	@Override
	public int getNoOfTurnsForDepart(String busNo, String busRoute, String busService, String station) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int turnNo = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cDate);
		calendar.add(Calendar.DATE, -1);
		Date yDate = calendar.getTime();

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strtoday = dateFormat.format(cDate);
		String strDatePrevious = dateFormat.format(yDate);
		Timestamp timestamp = new Timestamp(date.getTime());
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "select max(turn_no ) as turn_no   from nt_t_terminal_departure_log where bus_no =? and route_no =? and \r\n"
					+ "service_type =? and station =? and actual_departure is not null  and to_char(created_date,'yyyy-MM-dd')=?; ";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			stmt.setString(2, busRoute);
			stmt.setString(3, busService);
			stmt.setString(4, station);
			stmt.setString(5, strtoday.trim());

			rs = stmt.executeQuery();

			while (rs.next()) {
				turnNo = rs.getInt("turn_no");
				turnNo++;

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return turnNo;

	}

	@Override
	public long getarrivallatestSeq(String busNo, String busRoute, String busService, String station) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long seq = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cDate);
		calendar.add(Calendar.DATE, -1);
		Date yDate = calendar.getTime();

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strtoday = dateFormat.format(cDate);
		String strDatePrevious = dateFormat.format(yDate);
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "select seq from nt_t_terminal_arrival_log where bus_no =? and route_no =? and service_type =? and station=?  and modified_date is not null\r\n"
					+ "order by modified_date  desc limit 1";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			stmt.setString(2, busRoute);
			stmt.setString(3, busService);
			stmt.setString(4, station);

			rs = stmt.executeQuery();

			while (rs.next()) {
				seq = rs.getLong("seq");

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return seq;

	}

	@Override
	public boolean getIsBusDepartCheck(String busNo, String busRoute, String busService, String station,
			String stationSide, String paramTime) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean busDepart = false;
		boolean busArrival = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cDate);
		calendar.add(Calendar.DATE, -1);
		Date yDate = calendar.getTime();

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateTimeFormatter dateFormat2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String strtoday = dateFormat.format(cDate);
		String strDatePrevious = dateFormat.format(yDate);
		String actualArrival = null;
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "SELECT isdepart, actual_arrival,arrival_date,modified_date FROM public.nt_t_terminal_arrival_log \r\n"
					+ "WHERE bus_no =? \r\n" + "and route_no =? and service_type =? and station =?  \r\n"
					+ "ORDER BY modified_date desc nulls last limit 1; ";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			stmt.setString(2, busRoute);
			stmt.setString(3, busService);
			stmt.setString(4, station);
			// stmt.setString(5, strtoday.trim());

			rs = stmt.executeQuery();

			while (rs.next()) {
				actualArrival = rs.getString("actual_arrival");
				if (rs.getString("arrival_date").trim().equals(strtoday.trim())) {

					if (rs.getString("actual_arrival") == null && rs.getString("isdepart").equalsIgnoreCase("N")) {

						if (stationSide.equals("D")) {
							return busDepart = false;
						} else if (stationSide.equals("O")) {
							return busArrival = true;
						}
					}

					else if (rs.getString("actual_arrival") != null && rs.getString("isdepart").equalsIgnoreCase("N")) {

						if (stationSide.equals("D")) {
							return busDepart = true;
						} else if (stationSide.equals("O")) {
							return busArrival = false;
						}
					}

					else if (rs.getString("actual_arrival") != null && rs.getString("isdepart").equalsIgnoreCase("Y")) {
						if (stationSide.equals("D")) {
							return busDepart = false;
						} else if (stationSide.equals("O")) {
							return busArrival = true;
						}
					}

				}
				/**
				 * if bus came yesterday but not depart.then check depart time is in the
				 * parameterized time if it is yes let to depart else no
				 **/
				else if (rs.getString("arrival_date").trim().equals(strDatePrevious.trim())
						&& stationSide.equals("D")) {
					if (rs.getString("actual_arrival") != null && rs.getString("isdepart").equalsIgnoreCase("N")) {
						DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
						LocalTime arrivalTImeL = LocalTime.parse(actualArrival, format);
						LocalDate yesterday = LocalDate.parse(strDatePrevious, dateFormat2);

						LocalDateTime arrivalDateWIthTIme = LocalDateTime.of(yesterday, arrivalTImeL);

						LocalDateTime currentDateTime = LocalDateTime.now();
						LocalDateTime paramPlusArrivalTImeL = arrivalDateWIthTIme
								.plusMinutes(Long.parseLong(paramTime));

						if (paramPlusArrivalTImeL.isAfter(arrivalDateWIthTIme)
								&& paramPlusArrivalTImeL.isBefore(currentDateTime)) {
							return busDepart = true;

						}

					}

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return busDepart;
	}

	@Override
	public boolean checkFirstArrive(String busNo, String busRoute, String busService, String station) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean firstArrive = true;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cDate);
		calendar.add(Calendar.DATE, -1);
		Date yDate = calendar.getTime();

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strtoday = dateFormat.format(cDate);
		String strDatePrevious = dateFormat.format(yDate);
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "SELECT isdepart, actual_arrival,arrival_date,modified_date FROM public.nt_t_terminal_arrival_log \r\n"
					+ "WHERE bus_no =? \r\n" + "and route_no =? and service_type =? and station =? \r\n"
					+ "ORDER BY modified_date desc nulls last limit 1; ";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			stmt.setString(2, busRoute);
			stmt.setString(3, busService);
			stmt.setString(4, station);

			rs = stmt.executeQuery();

			while (rs.next()) {

				if (rs.getString("arrival_date").trim().equals(strtoday.trim())) {
					if (rs.getString("actual_arrival") != null) {
						firstArrive = false;
					} else {
						firstArrive = true;
					}

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return firstArrive;
	}

	@Override
	public boolean checkLatestActualArrivedBusIdDepartued(String busNo, String busRoute, String busService,
			String station) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean busDepart = false;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cDate);
		calendar.add(Calendar.DATE, -1);
		Date yDate = calendar.getTime();

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strtoday = dateFormat.format(cDate);
		String strDatePrevious = dateFormat.format(yDate);
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "SELECT isdepart, actual_arrival,arrival_date,modified_date FROM public.nt_t_terminal_arrival_log \r\n"
					+ "WHERE bus_no =? \r\n"
					+ "and route_no =? and service_type =? and station =? and actual_arrival is not null \r\n"
					+ "ORDER BY modified_date desc nulls last limit 1; ";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			stmt.setString(2, busRoute);
			stmt.setString(3, busService);
			stmt.setString(4, station);

			rs = stmt.executeQuery();

			while (rs.next()) {

				if (rs.getString("arrival_date").trim().equals(strtoday.trim())
						|| rs.getString("arrival_date").trim().equals(strDatePrevious.trim())) {

					if (rs.getString("isdepart").equals("Y")) {
						busDepart = true;
					}
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return busDepart;
	}

	@Override
	public boolean checkEntererdBusArriveToday(String busNo, String busRoute, String busService, String station) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean busArriveToday = false;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cDate);
		calendar.add(Calendar.DATE, -1);
		Date yDate = calendar.getTime();

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strtoday = dateFormat.format(cDate);
		String strDatePrevious = dateFormat.format(yDate);
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "SELECT isdepart, actual_arrival,arrival_date,modified_date FROM public.nt_t_terminal_arrival_log \r\n"
					+ "WHERE bus_no =? \r\n" + "and route_no =? and service_type =? and station =? \r\n"
					+ "ORDER BY modified_date desc nulls last limit 1; ";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			stmt.setString(2, busRoute);
			stmt.setString(3, busService);
			stmt.setString(4, station);

			rs = stmt.executeQuery();

			while (rs.next()) {

				if (rs.getString("arrival_date").trim().equals(strtoday.trim())
						|| rs.getString("arrival_date").trim().equals(strDatePrevious.trim())) {
					if (rs.getString("isdepart").trim().equals("N")) {
						busArriveToday = true;
					}

				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return busArriveToday;
	}

	@Override
	public TerminalArrivalDepartureDTO insertTerminalDepartureLogForYersterday(
			TerminalArrivalDepartureDTO scheduleListForDay, int dayOfWeek, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps_cnt = null;
		ResultSet rs_cnt = null;

		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		cal.add(Calendar.DATE, -1);

		List<TerminalArrivalDepartureTimeDTO> timeList = new ArrayList<TerminalArrivalDepartureTimeDTO>();
		int cnt = 0;

		try {
			con = ConnectionManager.getConnection();

			if (scheduleListForDay == null || scheduleListForDay.getScheduledTimeListForDay() == null)
				return null;

			// exit if records found for the same day
			String query_cnt = "SELECT seq, estimated_departure, actual_departure, trip_endtime, trip_type, authorized, authfail_reasons FROM nt_t_terminal_departure_log WHERE bus_no = ? AND service_type = ? AND route_no = ? AND station = ? AND departure_date = ? ;";
			ps_cnt = con.prepareStatement(query_cnt);
			ps_cnt.setString(1, scheduleListForDay.getBusRegNo());
			ps_cnt.setString(2, scheduleListForDay.getServiceTypeCode());
			ps_cnt.setString(3, scheduleListForDay.getRouteNo());
			ps_cnt.setString(4, scheduleListForDay.getStationCode());
			ps_cnt.setString(5, dateFormat.format(cal.getTime()));
			rs_cnt = ps_cnt.executeQuery();

			while (rs_cnt.next()) {
				TerminalArrivalDepartureTimeDTO data = new TerminalArrivalDepartureTimeDTO();
				data.setSequence(rs_cnt.getLong(1));
				data.setStartTime(rs_cnt.getString(2));
				data.setActualArrivalDeparture(rs_cnt.getString(3));
				data.setEndTime(rs_cnt.getString(4));
				data.setTripType(rs_cnt.getString(5));
				data.setAuthorized(rs_cnt.getString(6));
				data.setAuthorizationFailReasons(rs_cnt.getString(7));
				timeList.add(data);
				cnt++;
			}

			ConnectionManager.close(rs_cnt);

			if (cnt > 0) {
				scheduleListForDay.setScheduledTimeListForDay(timeList);
				return scheduleListForDay;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs_cnt);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps_cnt);
			ConnectionManager.close(con);
		}
		return scheduleListForDay;
	}

	@Override
	public String getBusAbbrivationForEnteredBus(String busNo, String refNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String busAbrr = null;

		try {

			con = ConnectionManager.getConnection();

			// select bus abbrivation number for entered busNo
			String query = "select abbreviation  from public.panel_generator_origin_trip_details  where bus_no =? and ref_no = ? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, busNo);
			ps.setString(2, refNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				busAbrr = rs.getString("abbreviation");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busAbrr;

	}

	@Override
	public List<String> getAlltimeSlotsForTheDay(String route, String service, String refNo, String trip, int day) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select tr_schedule_time_table_time  FROM public.nt_t_terminal_time_table_schedule where tr_panel_ref_no = ? and tr_day = ?  order by seq ";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setInt(2, day);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("tr_schedule_time_table_time");
				returnList.add(value);
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

	@Override
	public List<String> getLeaveBusAbbriviationListForTheDay(String refNo, int day) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> leaveBusAbbrList = new ArrayList<>();

		try {

			con = ConnectionManager.getConnection();

			// get Leave bus abbreviation list
			String query2 = "select distinct d2.leave_position,d1.trip_id,d1.bus_no \r\n"
					+ "from public.nt_t_route_schedule_generator_det02 d2\r\n"
					+ "inner join public.nt_t_route_schedule_generator_det01 d1 on d1.generator_ref_no =d2.generator_ref_no and d1.trip_type =d2.trip_type \r\n"
					+ "and d2.leave_position =d1.trip_id \r\n" + "where d2.generator_ref_no =? \r\n"
					+ "and d1.day_no =?  ";
			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setInt(2, day);
			rs = ps.executeQuery();

			while (rs.next()) {
				String busName = null;
				busName = rs.getString("bus_no");
				leaveBusAbbrList.add(busName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return leaveBusAbbrList;

	}

	@Override
	public List<String> getBusAbbriviationListForTheDay(String refNo, int day, String trip) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<String> busAbbrList = new ArrayList<>();
		try {

			con = ConnectionManager.getConnection();

			// select bus abbreviation list for the day
			String query1 = "select tr_bus_no FROM public.nt_t_terminal_time_table_schedule where tr_panel_ref_no = ? and tr_day = ?  order by seq ";
			ps = con.prepareStatement(query1);
			ps.setString(1, refNo);
			ps.setInt(2, day);
			rs = ps.executeQuery();

			while (rs.next()) {
				String busName = null;
				busName = rs.getString("tr_bus_no");
				busAbbrList.add(busName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busAbbrList;

	}

	@Override
	public List<String> getAllEndtimeSlotsForTheDay(String route, String service, String refNo, String trip, int day) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String value = "";
		List<String> returnList = new ArrayList<String>();

		try {
			con = ConnectionManager.getConnection();
			String query = "select tr_schedule_time_table_time_end_time  FROM public.nt_t_terminal_time_table_schedule where tr_panel_ref_no = ? and tr_day = ?  order by seq ";

			ps = con.prepareStatement(query);
			ps.setString(1, refNo);
			ps.setInt(2, day);
			rs = ps.executeQuery();

			while (rs.next()) {
				value = rs.getString("tr_schedule_time_table_time_end_time");
				returnList.add(value);
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

	@Override
	public String getChargeAmmount(String receiptNo) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String ammount = null;

		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "select tep_chargeamt from public.nt_t_terminal_payment \r\n" + "where tep_receipt_no=?    \r\n"
					+ "and tep_payment_type_code='003'";

			stmt = con.prepareStatement(query);
			stmt.setString(1, receiptNo);

			rs = stmt.executeQuery();

			while (rs.next()) {
				double ammountVal = rs.getDouble("tep_chargeamt");
				DecimalFormat df = new DecimalFormat("0.00");
				ammount = df.format(ammountVal);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return ammount;

	}

	@Override
	public boolean checkYerterDayArriveNDepart(String busNo, String busRoute, String busService, String station) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date cDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(cDate);
		calendar.add(Calendar.DATE, -1);
		Date yDate = calendar.getTime();

		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strtoday = dateFormat.format(cDate);
		String strDatePrevious = dateFormat.format(yDate);
		boolean returnVal = true;
		try {
			con = ConnectionManager.getConnection();

			String query = null;

			query = "SELECT isdepart, actual_arrival,arrival_date,modified_date FROM public.nt_t_terminal_arrival_log \r\n"
					+ "WHERE bus_no =? \r\n"
					+ "and route_no =? and service_type =? and station =? and actual_arrival is not null \r\n"
					+ "ORDER BY modified_date desc nulls last limit 1; ";

			stmt = con.prepareStatement(query);
			stmt.setString(1, busNo);
			stmt.setString(2, busRoute);
			stmt.setString(3, busService);
			stmt.setString(4, station);

			rs = stmt.executeQuery();

			while (rs.next()) {

				if (rs.getString("arrival_date").trim().equals(strDatePrevious.trim())) {

					if (rs.getString("isdepart").equals(null) || !(rs.getString("isdepart").equalsIgnoreCase("Y"))) {
						returnVal = false;
					}
				} else {
					returnVal = true;
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return returnVal;

	}

	@Override
	public int updateTerminalPayment(TerminalPaymentDTO paymentDTO, String paymentType) {
		int updatedCount = 0;
		String updateQuery = null;
		if (paymentType.equals("002")) {

			updateQuery = "UPDATE nt_t_terminal_payment SET tep_vehicleno =?, tep_due_date=?, tep_paid_date=?, tep_pay_mode=?, tep_receipt_no=?, tep_valid_from=?, "
					+ "tep_valid_to=?, tep_chargeamt=?, tep_penaltyamt=?, tep_ammount_paid=?, tep_permit_no=?, tep_st_origin=?, tep_st_destination=?, tep_st_via=?, "
					+ "tep_st_owner_name=?, tep_st_gender=?, tep_st_contact_no=?, tep_st_service_type=?, tep_modified_by=?, tep_modified_date=?, tep_st_route_no=? WHERE tep_seq=?";

		} else if (paymentType.equals("003")) {

			updateQuery = "UPDATE nt_t_terminal_payment SET tep_vehicleno =?, tep_due_date=?, tep_paid_date=?, tep_pay_mode=?, tep_receipt_no=?, tep_valid_from=?, "
					+ "tep_valid_to=?, tep_chargeamt=?, tep_penaltyamt=?, tep_ammount_paid=?, tep_permit_no=?, tep_st_owner_name=?, tep_st_gender=?, tep_st_contact_no=?, "
					+ "tep_st_service_type=? , tep_modified_by=?, tep_modified_date=? WHERE tep_seq=?";

		} else {
			updateQuery = "UPDATE nt_t_terminal_payment SET tep_vehicleno =?, tep_due_date=?, tep_paid_date=?, tep_pay_mode=?, tep_receipt_no=?, tep_valid_from=?, "
					+ "tep_valid_to=?, tep_chargeamt=?, tep_penaltyamt=?, tep_ammount_paid=? , tep_modified_by=?, tep_modified_date=? WHERE tep_seq=?";
		}

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);) {

			if (paymentType.equals("002")) {

				preparedStatement.setString(1, paymentDTO.getTepVehicleNo());
				preparedStatement.setString(2, paymentDTO.getTepDueDate());
				preparedStatement.setString(3, paymentDTO.getTepPaidDate());
				preparedStatement.setString(4, paymentDTO.getTepPayMode());
				preparedStatement.setString(5, paymentDTO.getTepReceiptNo());
				preparedStatement.setString(6, paymentDTO.getTepValidFrom());
				preparedStatement.setString(7, paymentDTO.getTepValidTo());

				preparedStatement.setDouble(8, paymentDTO.getTepChargeAmt());
				preparedStatement.setDouble(9, paymentDTO.getTepPenaltyAmt());
				preparedStatement.setDouble(10, paymentDTO.getTepAmountPaid());

				preparedStatement.setString(11, paymentDTO.getTepPermitNo());
				preparedStatement.setString(12, paymentDTO.getTepStOrigin());
				preparedStatement.setString(13, paymentDTO.getTepStDestination());
				preparedStatement.setString(14, paymentDTO.getTepStVia());
				preparedStatement.setString(15, paymentDTO.getTepStOwnerName());
				preparedStatement.setString(16, paymentDTO.getTepStGender());
				preparedStatement.setString(17, paymentDTO.getTepStContactNo());
				preparedStatement.setString(18, paymentDTO.getTepServiceType());

				preparedStatement.setString(19, paymentDTO.getTepModifiedBy());
				preparedStatement.setTimestamp(20, new java.sql.Timestamp(paymentDTO.getTepModifiedDate().getTime()));

				preparedStatement.setString(21, paymentDTO.getTepStRouteNo());
				preparedStatement.setLong(22, paymentDTO.getTepSeq());

			} else if (paymentType.equals("003")) {

				preparedStatement.setString(1, paymentDTO.getTepVehicleNo());
				preparedStatement.setString(2, paymentDTO.getTepDueDate());
				preparedStatement.setString(3, paymentDTO.getTepPaidDate());
				preparedStatement.setString(4, paymentDTO.getTepPayMode());
				preparedStatement.setString(5, paymentDTO.getTepReceiptNo());
				preparedStatement.setString(6, paymentDTO.getTepValidFrom());
				preparedStatement.setString(7, paymentDTO.getTepValidTo());

				preparedStatement.setDouble(8, paymentDTO.getTepChargeAmt());
				preparedStatement.setDouble(9, paymentDTO.getTepPenaltyAmt());
				preparedStatement.setDouble(10, paymentDTO.getTepAmountPaid());

				preparedStatement.setString(11, paymentDTO.getTepPermitNo());
				preparedStatement.setString(12, paymentDTO.getTepStOwnerName());
				preparedStatement.setString(13, paymentDTO.getTepStGender());
				preparedStatement.setString(14, paymentDTO.getTepStContactNo());
				preparedStatement.setString(15, paymentDTO.getTepServiceType());

				preparedStatement.setString(16, paymentDTO.getTepModifiedBy());
				preparedStatement.setTimestamp(17, new java.sql.Timestamp(paymentDTO.getTepModifiedDate().getTime()));

				preparedStatement.setLong(18, paymentDTO.getTepSeq());

			} else {
				preparedStatement.setString(1, paymentDTO.getTepVehicleNo());
				preparedStatement.setString(2, paymentDTO.getTepDueDate());
				preparedStatement.setString(3, paymentDTO.getTepPaidDate());
				preparedStatement.setString(4, paymentDTO.getTepPayMode());
				preparedStatement.setString(5, paymentDTO.getTepReceiptNo());
				preparedStatement.setString(6, paymentDTO.getTepValidFrom());
				preparedStatement.setString(7, paymentDTO.getTepValidTo());

				preparedStatement.setDouble(8, paymentDTO.getTepChargeAmt());
				preparedStatement.setDouble(9, paymentDTO.getTepPenaltyAmt());
				preparedStatement.setDouble(10, paymentDTO.getTepAmountPaid());

				preparedStatement.setString(11, paymentDTO.getTepModifiedBy());
				preparedStatement.setTimestamp(12, new java.sql.Timestamp(paymentDTO.getTepModifiedDate().getTime()));

				preparedStatement.setLong(13, paymentDTO.getTepSeq());
			}

			updatedCount = preparedStatement.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return updatedCount;
	}

	private void insertTaskInquiryRecord(Connection con, TerminalDetailsDTO terminalDetailsDTO, Timestamp timestamp,
			String status, String function, String user, String functiondes) {

		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, terminal_id, permit_no,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, terminalDetailsDTO.getVehiNo());
			psInsert.setString(5, terminalDetailsDTO.getTerminal());
			psInsert.setString(6, terminalDetailsDTO.getPermitNo());
			psInsert.setString(7, function);
			psInsert.setString(8, functiondes);

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void beanLinkMethod(TerminalDetailsDTO terminalDetailsDTO, String user, String des, String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			insertTaskInquiryRecord(con, terminalDetailsDTO, timestamp, des, "Terminal Management", user, funDes);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void insertTaskInquiryRecord(Connection con, TerminalBlockDetailsDTO terminalDetailsDTO,
			Timestamp timestamp, String status, String function, String user, String functiondes) {

		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, terminal_id,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, terminalDetailsDTO.getTerminal());
			psInsert.setString(5, function);
			psInsert.setString(6, functiondes);

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void beanLinkMethod(TerminalBlockDetailsDTO terminalDetailsDTO, String user, String des, String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			insertTaskInquiryRecord(con, terminalDetailsDTO, timestamp, des, "Terminal Management", user, funDes);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void insertTaskInquiryRecord(Connection con, Timestamp timestamp, String status, String function,
			String user, String functiondes, String busNo, String permitNo, String payType) {

		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, bus_no, payment_type, permit_no,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, busNo);
			psInsert.setString(5, payType);
			psInsert.setString(6, permitNo);
			psInsert.setString(7, function);
			psInsert.setString(8, functiondes);

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void beanLinkMethod(String status, String user, String functiondes, String busNo, String permitNo,
			String payType) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			insertTaskInquiryRecord(con, timestamp, status, "Terminal Management", user, functiondes, busNo, permitNo,
					payType);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void insertTaskInquiryRecord(Connection con, Timestamp timestamp, String status, String function,
			String user, String functiondes, String voucherNo) {

		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, vou_id, function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, voucherNo);
			psInsert.setString(5, function);
			psInsert.setString(6, functiondes);

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void beanLinkMethod(String status, String user, String functiondes, String voucherNo) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			insertTaskInquiryRecord(con, timestamp, status, "Terminal Management", user, functiondes, voucherNo);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<CommonInquiryDTO> getTerminalNoListForCommonInquiry() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = null;
			sql = "SELECT DISTINCT terminal_id FROM public.nt_t_task_inquiry WHERE function_name = 'Terminal Management'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				if (rs.getString("terminal_id") != null && !rs.getString("terminal_id").isEmpty()) {
					data.setTerminalId(rs.getString("terminal_id"));
					dataList.add(data);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
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
	public List<CommonInquiryDTO> getBusNoListForCommonInquiry() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = null;
			sql = "SELECT DISTINCT bus_no FROM public.nt_t_task_inquiry WHERE function_name = 'Terminal Management'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				if (rs.getString("bus_no") != null && !rs.getString("bus_no").isEmpty()) {
					data.setBusNo(rs.getString("bus_no"));
					dataList.add(data);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
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
	public List<CommonInquiryDTO> getVoucherNoListForCommonInquiry() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = null;
			sql = "SELECT DISTINCT vou_id FROM public.nt_t_task_inquiry WHERE function_name = 'Terminal Management'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				if (rs.getString("vou_id") != null && !rs.getString("vou_id").isEmpty()) {
					data.setVoucherNo(rs.getString("vou_id"));
					dataList.add(data);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
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
	public List<CommonInquiryDTO> getPaymentTypeListForCommonInquiry() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = null;
			sql = "SELECT DISTINCT payment_type FROM public.nt_t_task_inquiry WHERE function_name = 'Terminal Management'";

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				if (rs.getString("payment_type") != null && !rs.getString("payment_type").isEmpty()) {
					data.setPayType(rs.getString("payment_type"));
					dataList.add(data);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
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
	public List<CommonInquiryDTO> searchTerminalDataForCommonInquiry(String terminalNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = null;
			sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Terminal Management' "
					+ "AND terminal_id = ? ";

			ps = con.prepareStatement(sql);
			ps.setString(1, terminalNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();

				data.setTerminalId(rs.getString("terminal_id"));
				data.setStatusDes(rs.getString("status"));
				data.setCreatedBy(rs.getString("created_by"));
				data.setCreatedDate(rs.getTimestamp("created_date"));
				data.setFunctionDes(rs.getString("function_des"));

				dataList.add(data);
			}

		} catch (SQLException e) {
			e.printStackTrace();
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
	public List<CommonInquiryDTO> searchVoucherDataForCommonInquiry(String voucherNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = null;
			sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Terminal Management' "
					+ "AND vou_id = ? ";

			ps = con.prepareStatement(sql);
			ps.setString(1, voucherNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();

				data.setVoucherNo(rs.getString("vou_id"));
				data.setStatusDes(rs.getString("status"));
				data.setCreatedBy(rs.getString("created_by"));
				data.setCreatedDate(rs.getTimestamp("created_date"));
				data.setFunctionDes(rs.getString("function_des"));

				dataList.add(data);
			}

		} catch (SQLException e) {
			e.printStackTrace();
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
	public List<CommonInquiryDTO> searchDataForCommonInquiry(String busNo, String paymentType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();

		try {
			con = ConnectionManager.getConnection();

			if (busNo != "" && paymentType != "") {
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Terminal Management' "
						+ "AND (bus_no = ? AND payment_type = ?) ";

				ps = con.prepareStatement(sql);
				ps.setString(1, busNo);
				ps.setString(2, paymentType);

				rs = ps.executeQuery();

				while (rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();

					data.setBusNo(rs.getString("bus_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setPermitNo(rs.getString("permit_no"));
					data.setPayType(rs.getString("payment_type"));

					dataList.add(data);
				}
			} else if (busNo != "") {
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Terminal Management' "
						+ "AND bus_no = ? ";

				ps = con.prepareStatement(sql);
				ps.setString(1, busNo);

				rs = ps.executeQuery();

				while (rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();

					data.setBusNo(rs.getString("bus_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));

					dataList.add(data);
				}
			} else {
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Terminal Management' "
						+ "AND (payment_type = ?) ";

				ps = con.prepareStatement(sql);
				ps.setString(1, paymentType);

				rs = ps.executeQuery();

				while (rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();

					data.setBusNo(rs.getString("bus_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					data.setPermitNo(rs.getString("permit_no"));
					data.setPayType(rs.getString("payment_type"));

					dataList.add(data);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dataList;
	}
}