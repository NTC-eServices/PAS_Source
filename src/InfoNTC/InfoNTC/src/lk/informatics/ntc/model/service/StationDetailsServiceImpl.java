package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.StationDetailsDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class StationDetailsServiceImpl implements StationDetailsService, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public void saveAction(List<StationDetailsDTO> stationDetailsDTOList, String loginUSer) {

		Connection con = null;
		PreparedStatement ps = null;
		String status = "A";

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (StationDetailsDTO stationDetailsDTO : stationDetailsDTOList) {

				String sql = "INSERT INTO public.nt_r_station "
						+ "(seq, code, midpoint_name, midpoint_name_sin, midpoint_name_tamil, status, created_by, created_date) "
						+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

				ps = con.prepareStatement(sql);

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_r_station");
				ps.setLong(1, seqNo);
				ps.setString(2, stationDetailsDTO.getStationCode());
				ps.setString(3, stationDetailsDTO.getStationNameEn());
				ps.setString(4, stationDetailsDTO.getStationNameSin());
				ps.setString(5, stationDetailsDTO.getStationNameTam());
				if (stationDetailsDTO.getStatus().equalsIgnoreCase("Active")) {
					status = "A";
				} else if (stationDetailsDTO.getStatus().equalsIgnoreCase("Inactive")) {
					status = "I";
				}
				ps.setString(6, status);
				ps.setString(7, loginUSer);
				ps.setTimestamp(8, timestamp);
				ps.executeUpdate();
			}
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public List<StationDetailsDTO> searchStationData(StationDetailsDTO stationDetailsDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<StationDetailsDTO> stationDetailsList = new ArrayList<StationDetailsDTO>();

		try {
			con = ConnectionManager.getConnection();
			String where = "";
			if (stationDetailsDTO.getStationNameEn() != null && !stationDetailsDTO.getStationNameEn().isEmpty()
					&& stationDetailsDTO.getStationNameEn().equalsIgnoreCase("all")) {
				if (stationDetailsDTO.getStatus().equalsIgnoreCase("Active")) {
					where = "status = 'A'";
				} else {
					where = "status = 'I'";
				}
			} else {
				if (stationDetailsDTO.getStatus().equalsIgnoreCase("Active")) {
					where = "code='" + stationDetailsDTO.getStationNameEn() + "' and status = 'A'";
				} else {
					where = "code='" + stationDetailsDTO.getStationNameEn() + "' and status = 'I'";
				}
			}

			String query = "select * from public.nt_r_station where " + where;

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO dto = new StationDetailsDTO();
				dto.setStationCode(rs.getString("code"));
				dto.setStationNameEn(rs.getString("midpoint_name"));
				dto.setStationNameSin(rs.getString("midpoint_name_sin"));
				dto.setStationNameTam(rs.getString("midpoint_name_tamil"));
				String status = rs.getString("status");
				if (status != null && !status.isEmpty() && status.equalsIgnoreCase("A")) {
					dto.setStatus("Active");
				} else {
					dto.setStatus("Inactive");
				}

				stationDetailsList.add(dto);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return stationDetailsList;
	}

	@Override
	public void updateStationDetailsRecord(StationDetailsDTO stationDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		String status = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_r_station "
					+ "SET midpoint_name=?, midpoint_name_sin=?, midpoint_name_tamil=?, status=? " + "WHERE code=?";

			ps = con.prepareStatement(sql);

			ps.setString(1, stationDTO.getStationNameEn());
			ps.setString(2, stationDTO.getStationNameSin());
			ps.setString(3, stationDTO.getStationNameTam());
			if (stationDTO.getStatus().equalsIgnoreCase("Active")) {
				status = "A";
			} else if (stationDTO.getStatus().equalsIgnoreCase("Inactive")) {
				status = "I";
			} else {
				status = stationDTO.getStatus();
			}
			ps.setString(4, status);
			ps.setString(5, stationDTO.getStationCode());

			ps.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

	}

	@Override
	public boolean checkDuplicateData(StationDetailsDTO stationDTO, boolean stationCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exist = false;

		try {
			con = ConnectionManager.getConnection();

			String where = "";

			if (stationDTO != null) {

				if (stationDTO.getStationNameEn() != null && !stationDTO.getStationNameEn().isEmpty()
						&& !stationDTO.getStationNameEn().trim().equalsIgnoreCase("")) {
					if (where != null && !where.isEmpty()) {
						where = where + " and midpoint_name='" + stationDTO.getStationNameEn() + "'";
					} else {
						where = where + " midpoint_name='" + stationDTO.getStationNameEn() + "'";
					}
				}

				if (stationDTO.getStationNameSin() != null && !stationDTO.getStationNameSin().isEmpty()
						&& !stationDTO.getStationNameSin().trim().equalsIgnoreCase("")) {
					if (where != null && !where.isEmpty()) {
						where = where + " and midpoint_name_sin='" + stationDTO.getStationNameSin() + "'";
					} else {
						where = where + " midpoint_name_sin='" + stationDTO.getStationNameSin() + "'";
					}
				}

				if (stationDTO.getStationNameTam() != null && !stationDTO.getStationNameTam().isEmpty()
						&& !stationDTO.getStationNameTam().trim().equalsIgnoreCase("")) {
					if (where != null && !where.isEmpty()) {
						where = where + " and midpoint_name_tamil='" + stationDTO.getStationNameTam() + "'";
					} else {
						where = where + " midpoint_name_tamil='" + stationDTO.getStationNameTam() + "'";
					}
				}

				if (stationCode) {
					if (where != null && !where.isEmpty()) {
						where = where + " and code='" + stationDTO.getStationCode() + "'";
					} else {
						where = where + " code='" + stationDTO.getStationCode() + "'";
					}
				}

				if (stationDTO.getStatus() != null && !stationDTO.getStatus().isEmpty()
						&& !stationDTO.getStatus().trim().equalsIgnoreCase("")) {
					if (stationDTO.getStatus().equalsIgnoreCase("Active")) {
						stationDTO.setStatus("A");
					}
					if (stationDTO.getStatus().equalsIgnoreCase("Inactive")) {
						stationDTO.setStatus("I");
					}

					if (where != null && !where.isEmpty()) {
						where = where + " and status='" + stationDTO.getStatus() + "'";
					} else {
						where = where + " status='" + stationDTO.getStatus() + "'";
					}
				}

				String query = "select * from public.nt_r_station where " + where;

				ps = con.prepareStatement(query);

				rs = ps.executeQuery();

				while (rs.next()) {
					exist = true;
					break;
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return exist;
	}

	@Override
	public List<StationDetailsDTO> selectAllStations() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<StationDetailsDTO> stationDetailsList = new ArrayList<StationDetailsDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "select * from public.nt_r_station";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				StationDetailsDTO dto = new StationDetailsDTO();
				dto.setStationCode(rs.getString("code"));
				dto.setStationNameEn(rs.getString("midpoint_name"));
				dto.setStationNameSin(rs.getString("midpoint_name_sin"));
				dto.setStationNameTam(rs.getString("midpoint_name_tamil"));
				String status = rs.getString("status");
				if (status != null && !status.isEmpty() && status.equalsIgnoreCase("A")) {
					dto.setStatus("Active");
				} else {
					dto.setStatus("Inactive");
				}
				stationDetailsList.add(dto);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return stationDetailsList;
	}

	@Override
	public boolean checkDuplicateCode(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exist = false;

		try {
			con = ConnectionManager.getConnection();
			String query = "select * from public.nt_r_station where code='" + code + "'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				exist = true;
				break;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return exist;
	}

	@Override
	public boolean checkAssignedStationCode(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exist = false;

		try {
			con = ConnectionManager.getConnection();
			String query = "select * from nt_m_route_station where rs_station = '" + code + "'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next() == true) {
				exist = true;
				break;
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(rs);
			ConnectionManager.close(con);
		}

		return exist;
	}

}
