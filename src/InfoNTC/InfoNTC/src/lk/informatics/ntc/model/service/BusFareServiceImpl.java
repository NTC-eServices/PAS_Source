package lk.informatics.ntc.model.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.BusFareDTO;
import lk.informatics.ntc.model.dto.BusFareEquationDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class BusFareServiceImpl implements BusFareService {

	@Override
	public List<BusFareDTO> getBusCategory() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> data = new ArrayList<BusFareDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, description_si, description_ta, active, created_by, created_date, modified_by, modified_date\r\n"
					+ "FROM public.nt_r_bus_category order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {
				dto = new BusFareDTO();

				dto.setBusCategory(rs.getString("description"));
				dto.setBusCategoryCode(rs.getString("code"));

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
	public BusFareDTO getBusCategoryDescription(BusFareDTO dtos) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BusFareDTO dto = new BusFareDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, description_si, description_ta, active, created_by, created_date, modified_by, modified_date\r\n"
					+ "FROM public.nt_r_bus_category  where code=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, dtos.getBusCategoryCode());
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setSelectBusCategorySinhala(rs.getString("description_si"));
				dto.setSelectBusCategoryTamil(rs.getString("description_ta"));

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
	public String getBusCategoryDescription(String busCategoryCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String description = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_bus_category  where code=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, busCategoryCode);
			rs = ps.executeQuery();

			if (rs.next()) {
				description = rs.getString("description");
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

	@Override
	public List<BusFareDTO> getTempBusCategory() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> data = new ArrayList<BusFareDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, description_si, description_ta, active, created_by, created_date, modified_by, modified_date\r\n"
					+ "FROM public.nt_r_bus_category order by description";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {
				dto = new BusFareDTO();

				dto.setTempBusCategory(rs.getString("description"));
				dto.setTempBusCategoryCode(rs.getString("code"));

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
	public BusFareDTO getTempBusCategoryDescription(BusFareDTO dtos) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BusFareDTO dto = new BusFareDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, description_si, description_ta, active, created_by, created_date, modified_by, modified_date\r\n"
					+ "FROM public.nt_r_bus_category  where code=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, dtos.getTempBusCategoryCode());
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setTempSelectBusCategorySinhala(rs.getString("description_si"));
				dto.setTempSelectBusCategoryTamil(rs.getString("description_ta"));

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
	public List<BusFareDTO> getCurrentBusFare() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tfc_seq_no, tfc_stage,tfc_normal_new_fee, tfc_semi_luxury_new_fee, tfc_luxury_new_fee, "
					+ "tfc_super_luxury_new_fee,tfc_hway_new_fee, tfc_halfof_normal_fee, tfc_quarterof_normal_fee "
					+ "FROM public.nt_t_fee_circle; ";
			;
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();
				dto.setStageNo(rs.getInt("tfc_stage"));
				dto.setNormalCurrentFee(rs.getBigDecimal("tfc_normal_new_fee"));
				dto.setLuxuryCurrentFee(rs.getBigDecimal("tfc_luxury_new_fee"));
				dto.setSemiLuxuryCurrentFee(rs.getBigDecimal("tfc_semi_luxury_new_fee"));
				dto.setSuperLuxuryCurrentFee(rs.getBigDecimal("tfc_super_luxury_new_fee"));
				dto.setHighwayCurrentFee(rs.getBigDecimal("tfc_hway_new_fee"));
				dto.setSisuSariyaHalfNoramlFee(rs.getBigDecimal("tfc_halfof_normal_fee"));
				dto.setSisuSariyaQuarterNoramlFee(rs.getBigDecimal("tfc_quarterof_normal_fee"));

				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;

	}

	@Override
	public String generateFareReferenceNo() {
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

			String sql = "SELECT mfc_fare_reference_no " + " FROM public.nt_m_fee_circle_master "
					+ " ORDER BY mfc_created_date desc " + " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("mfc_fare_reference_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "FRN" + currYear + ApprecordcountN;
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
					strAppNo = "FRN" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "FRN" + currYear + "00001";

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
	public List<BusFareDTO> getDefaultBusCategory() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, description_si, description_ta, active, created_by, created_date, "
					+ "modified_by, modified_date, rate, is_fare_calculation, category_order "
					+ "FROM public.nt_r_bus_category order by category_order";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();

				dto.setBusCategory(rs.getString("description"));
				dto.setBusCategoryCode(rs.getString("code"));

				String statusCode = rs.getString("active");
				String status = "";

				if (statusCode.equals("A")) {
					status = "ACTIVE";
				} else {
					status = "INACTIVE";
				}

				dto.setFareCalculate(true);
				dto.setDisabledFareCalculate(false);
				dto.setStatus(status);
				dto.setBusOrder(rs.getString("category_order"));
				dto.setRate(rs.getDouble("rate"));
				dto.setSelectBusCategorySinhala(rs.getString("description_si"));
				dto.setSelectBusCategoryTamil(rs.getString("description_ta"));

				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return data;
	}

	@Override
	public List<BusFareDTO> getTempDefaultBusCategory() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description, description_si, description_ta, active, created_by, created_date, "
					+ "modified_by, modified_date, rate, is_fare_calculation,category_order "
					+ "FROM public.nt_r_bus_category order by category_order";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();

				dto.setTempBusCategory(rs.getString("description"));
				dto.setTempBusCategoryCode(rs.getString("code"));

				String statusCode = rs.getString("active");
				String status = "";

				if (statusCode.equals("A")) {
					status = "ACTIVE";
				} else {
					status = "INACTIVE";
				}

				dto.setTempStatus(status);
				dto.setTempRate(rs.getDouble("rate"));
				dto.setTempBusOrder(rs.getString("category_order"));
				dto.setTempSelectBusCategorySinhala(rs.getString("description_si"));
				dto.setTempSelectBusCategoryTamil(rs.getString("description_ta"));

				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return data;
	}

	@Override
	public int getStageCount() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT count(tfc_stage) as sategeCount FROM public.nt_t_fee_circle";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				count = rs.getInt("sategeCount");

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return count;
	}

	@Override
	public BusFareDTO getCurrentDetails() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BusFareDTO dto = new BusFareDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT mfc_cost_change_presentage, mfc_created_date, mfc_fare_reference_no FROM public.nt_m_fee_circle_master "
					+ "WHERE mfc_status='A';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setPerviousCostChange(rs.getDouble("mfc_cost_change_presentage"));

				Timestamp ts = rs.getTimestamp("mfc_created_date");
				Date date = new Date(ts.getTime());

				dto.setPerviousCostChangeDate(date);
				dto.setFareReferenceNo(rs.getString("mfc_fare_reference_no"));

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
	public boolean updateNormalBusRate(List<BusFareDTO> rateList, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temp_fee_circle "
						+ "SET tfc_normal_current_fee=?, tfc_normal_new_fee=?, tfc_normal_round_fee=?,tfc_modified_by=?, "
						+ "tfc_modified_date=? WHERE tfc_stage=?";

				BigDecimal x = rateList.get(a).getNormalCurrentFee();
				BigDecimal y = rateList.get(a).getNormalNewFee();
				BigDecimal z = rateList.get(a).getNormalRoundFee();
				int stage = rateList.get(a).getStageNo();

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, x);
				ps.setBigDecimal(2, y);
				ps.setBigDecimal(3, z);
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, stage);

				ps.executeUpdate();

			}
			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public boolean updateSemiLuxuryBusRate(List<BusFareDTO> rateList, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temp_fee_circle "
						+ "SET tfc_semi_luxury_current_fee= ?, tfc_semi_luxury_new_fee=?, tfc_semi_luxury_round_fee=?, "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getSemiLuxuryCurrentFee());
				ps.setBigDecimal(2, rateList.get(a).getSemiLuxuryNewFee());
				ps.setBigDecimal(3, rateList.get(a).getSemiLuxuryRoundFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());

				ps.executeUpdate();

			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public boolean updateLuxuryBusRate(List<BusFareDTO> rateList, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temp_fee_circle "
						+ "SET tfc_luxury_current_fee=? , tfc_luxury_new_fee=? , tfc_luxury_round_fee=? , "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getLuxuryCurrentFee());
				ps.setBigDecimal(2, rateList.get(a).getLuxuryNewFee());
				ps.setBigDecimal(3, rateList.get(a).getLuxuryRoundFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());

				ps.executeUpdate();

			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public boolean updateSuperLuxuryBusRate(List<BusFareDTO> rateList, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temp_fee_circle "
						+ "SET tfc_super_luxury_current_fee=?, tfc_super_luxury_new_fee=?, tfc_super_luxury_round_fee=?, "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getSuperLuxuryCurrentFee());
				ps.setBigDecimal(2, rateList.get(a).getSuperLuxuryNewFee());
				ps.setBigDecimal(3, rateList.get(a).getSuperLuxuryRoundFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return false;
	}

	@Override
	public boolean updateHighWayBusRate(List<BusFareDTO> rateList, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temp_fee_circle "
						+ "SET tfc_hway_current_fee=?, tfc_hway_new_fee=?, tfc_highway_round_fee=?, "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getHighwayCurrentFee());
				ps.setBigDecimal(2, rateList.get(a).getHighwayNewFee());
				ps.setBigDecimal(3, rateList.get(a).getHighwayRoundFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public boolean updatetHalfSisuSariyaBusRate(List<BusFareDTO> rateList, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temp_fee_circle "
						+ "SET tfc_halfof_normal_fee=?, tfc_half_adjuested=?, tfc_half_different=?, "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getSisuSariyaHalfNoramlFee());
				ps.setBigDecimal(2, rateList.get(a).getSisuSariyaHalfAdjestedFee());
				ps.setBigDecimal(3, rateList.get(a).getSisuSariyaHalfdiffrentFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return false;
	}

	@Override
	public boolean updateQuarterSisuSariyBusRate(List<BusFareDTO> rateList, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temp_fee_circle "
						+ "SET tfc_quarterof_normal_fee=?, tfc_quarter_adjuested=?, tfc_quarter_different=?, "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getSisuSariyaQuarterNoramlFee());
				ps.setBigDecimal(2, rateList.get(a).getSisuSariyaQuarterAdjestedFee());
				ps.setBigDecimal(3, rateList.get(a).getSisuSariyaQuarterdiffrentFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());

				ps.executeUpdate();

			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return false;
	}

	@Override
	public boolean insertReferenceData(String refNo, List<BusFareDTO> stageList, String user,
			List<BusFareDTO> newFeeList) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int i = 0; i < stageList.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_temp_fee_circle");

				String sql = "INSERT INTO public.nt_temp_fee_circle "
						+ "(tfc_seq_no, tfc_fare_reference_no, tfc_stage, tfc_created_by, tfc_created_date,tfc_difference_with_curr_fare) "
						+ "VALUES(?, ?, ?, ?, ?, ?);";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, refNo);
				ps.setInt(3, stageList.get(i).getStageNo());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);

				BigDecimal currentValue = stageList.get(i).getNormalCurrentFee();
				BigDecimal newValue = newFeeList.get(i).getNormalRoundFee();

				BigDecimal diffrentValue = new BigDecimal(0);

				int x = currentValue.compareTo(newValue);

				if (x == 1) {
					diffrentValue = currentValue.subtract(newValue);
				} else if (x == -1) {
					diffrentValue = newValue.subtract(currentValue);
				} else {
					diffrentValue = newValue.subtract(currentValue);
				}

				ps.setBigDecimal(6, diffrentValue);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return false;
	}

	@Override
	public boolean masterTableInjection(BusFareDTO dto, String fareReferenceNo, String user) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isupdate = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_fee_circle_master");

			String sql = "INSERT INTO public.nt_m_fee_circle_master "
					+ "(mfc_seq_no, mfc_fare_reference_no,mfc_re_ammendment_date, mfc_cost_change_presentage, mfc_status, mfc_created_by, mfc_created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";

			ps = con.prepareStatement(sql);

			ps.setLong(1, seqNo);
			ps.setString(2, fareReferenceNo);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String stringDate = dateFormat.format(timestamp);

			ps.setString(3, stringDate);
			ps.setDouble(4, dto.getCurentCostChange());
			ps.setString(5, "P");
			ps.setString(6, user);
			ps.setTimestamp(7, timestamp);
			int i = ps.executeUpdate();

			if (i > 0) {
				isupdate = true;
			} else {
				isupdate = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return isupdate;

	}

	@Override
	public int checkPendingRecordCount() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		try {

			con = ConnectionManager.getConnection();

			String query = "select count(mfc_status) as statusCounter from public.nt_m_fee_circle_master where mfc_status='P' or mfc_status='C' or mfc_status='M'";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			if (rs.next()) {
				count = rs.getInt("statusCounter");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return count;
	}

	@Override
	public List<BusFareDTO> getFareRefNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT mfc_fare_reference_no FROM public.nt_m_fee_circle_master  order by mfc_fare_reference_no;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setFareReferenceNo(rs.getString("mfc_fare_reference_no"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getDefaultList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT mfc_seq_no, mfc_fare_reference_no, mfc_re_ammendment_date, mfc_cost_change_presentage, mfc_status FROM public.nt_m_fee_circle_master where mfc_status='P' order  by mfc_fare_reference_no;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setFareReferenceNo(rs.getString("mfc_fare_reference_no"));
				busFareDTO.setAmmendmentsDateVal(rs.getString("mfc_re_ammendment_date"));
				busFareDTO.setCurrentCostPresentage(rs.getDouble("mfc_cost_change_presentage"));
				busFareDTO.setStatus(rs.getString("mfc_status"));

				String statusCode = busFareDTO.getStatus();
				if (statusCode.equals("A")) {
					busFareDTO.setStatusDes("Active");
				} else if (statusCode.equals("P")) {
					busFareDTO.setStatusDes("Pending");
				} else if (statusCode.equals("R")) {
					busFareDTO.setStatusDes("Rejected");
				} else if (statusCode.equals("I")) {
					busFareDTO.setStatusDes("Inactive");
				} else if (statusCode.equals("C")) {
					busFareDTO.setStatusDes("Checked");
				} else if (statusCode.equals("M")) {
					busFareDTO.setStatusDes("Recommended");
				} else {

				}
				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getStatusList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_emp_status where active='A' order by description;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStatus(rs.getString("code"));
				busFareDTO.setStatusDes(rs.getString("description"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getFilteredFareRefNoList(String selectedStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT mfc_fare_reference_no FROM public.nt_m_fee_circle_master where mfc_status=? order by mfc_fare_reference_no;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedStatus);
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setFareReferenceNo(rs.getString("mfc_fare_reference_no"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getFilteredDateList(Date startDateObj, Date endDateObj, String selectedStatus,
			String selectedFareRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		String WHERE_SQL = "";

		if (startDateObj != null && endDateObj != null) {
			Timestamp startDate = new Timestamp(startDateObj.getTime());
			Timestamp endDate = new Timestamp(endDateObj.getTime());

			WHERE_SQL = WHERE_SQL + "and mfc_created_date >= " + "' " + startDate + "' and mfc_created_date <= " + "'"
					+ endDate + "' ";

		}

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT mfc_seq_no, mfc_fare_reference_no, mfc_re_ammendment_date, mfc_cost_change_presentage, mfc_status  FROM public.nt_m_fee_circle_master where (mfc_status=? or mfc_fare_reference_no=? ) "
					+ WHERE_SQL + " order  by mfc_fare_reference_no";

			ps = con.prepareStatement(query2);
			ps.setString(1, selectedStatus);
			ps.setString(2, selectedFareRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setFareReferenceNo(rs.getString("mfc_fare_reference_no"));
				busFareDTO.setAmmendmentsDateVal(rs.getString("mfc_re_ammendment_date"));
				busFareDTO.setCurrentCostPresentage(rs.getDouble("mfc_cost_change_presentage"));
				busFareDTO.setStatus(rs.getString("mfc_status"));

				String statusCode = busFareDTO.getStatus();
				if (statusCode.equals("A")) {
					busFareDTO.setStatusDes("Active");
				} else if (statusCode.equals("P")) {
					busFareDTO.setStatusDes("Pending");
				} else if (statusCode.equals("R")) {
					busFareDTO.setStatusDes("Rejected");
				} else if (statusCode.equals("I")) {
					busFareDTO.setStatusDes("Inactive");
				} else if (statusCode.equals("C")) {
					busFareDTO.setStatusDes("Checked");
				} else if (statusCode.equals("M")) {
					busFareDTO.setStatusDes("Recommended");
				} else {

				}

				returnList.add(busFareDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return returnList;
	}

	@Override
	public boolean busFareChecked(BusFareDTO selectDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_fee_circle_master SET  mfc_status=?, mfc_checked_by=?, mfc_checked_by_date=?, mfc_modified_by=?, mfc_modified_date=? WHERE mfc_fare_reference_no=? ;";

			ps = con.prepareStatement(query);

			ps.setString(1, "C");
			ps.setString(2, loginUser);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestamp);
			ps.setString(6, selectDTO.getFareReferenceNo());

			int a = ps.executeUpdate();

			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public boolean busFareRecommended(BusFareDTO selectDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_fee_circle_master SET  mfc_status=?,  mfc_recommended_by=?, mfc_recommended_by_date=?, mfc_modified_by=?, mfc_modified_date=? WHERE mfc_fare_reference_no=?;";

			ps = con.prepareStatement(query);

			ps.setString(1, "M");
			ps.setString(2, loginUser);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestamp);
			ps.setString(6, selectDTO.getFareReferenceNo());

			int a = ps.executeUpdate();

			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public boolean busFareApproved(BusFareDTO selectDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_fee_circle_master SET  mfc_status=?,  mfc_approved_by=?, mfc_approved_by_date=? ,mfc_modified_by=?, mfc_modified_date=?  WHERE mfc_fare_reference_no=?;";

			ps = con.prepareStatement(query);

			ps.setString(1, "A");
			ps.setString(2, loginUser);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, loginUser);
			ps.setTimestamp(5, timestamp);
			ps.setString(6, selectDTO.getFareReferenceNo());

			int a = ps.executeUpdate();

			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public boolean busFareInactivePrevRecrd(BusFareDTO selectDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_fee_circle_master SET  mfc_status=?,  mfc_modified_by=?, mfc_modified_date=?  WHERE mfc_status='A';";

			ps = con.prepareStatement(query);

			ps.setString(1, "I");
			ps.setString(2, loginUser);
			ps.setTimestamp(3, timestamp);

			int a = ps.executeUpdate();

			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public BusFareDTO getPrevActiveRecordDetails(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BusFareDTO busFareDTO = new BusFareDTO();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct  mfc_fare_reference_no, mfc_status FROM public.nt_m_fee_circle_master where mfc_status='A';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				busFareDTO.setPrevActiveBusFareRefNo(rs.getString("mfc_fare_reference_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return busFareDTO;
	}

	@Override
	public List<BusFareDTO> getTFeeListForPrevActiveRecord(String prevActiveBusFareRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tfc_seq_no, tfc_fare_reference_no, tfc_stage, tfc_normal_current_fee, tfc_normal_new_fee, tfc_normal_round_fee, tfc_luxury_current_fee, tfc_luxury_new_fee, tfc_luxury_round_fee, tfc_semi_luxury_current_fee, tfc_semi_luxury_new_fee, tfc_semi_luxury_round_fee, tfc_super_luxury_current_fee, tfc_super_luxury_new_fee, tfc_super_luxury_round_fee, tfc_hway_current_fee, tfc_hway_new_fee, tfc_highway_round_fee, tfc_halfof_normal_fee, tfc_half_adjuested, tfc_half_different, tfc_quarterof_normal_fee, tfc_quarter_adjuested, tfc_quarter_different, tfc_created_by, tfc_created_date, tfc_modified_by, tfc_modified_date, tfc_difference_with_curr_fare FROM public.nt_t_fee_circle where tfc_fare_reference_no=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, prevActiveBusFareRefNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setPrevActiveBusFareRefNo(rs.getString("tfc_fare_reference_no"));
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setNormalCurrentFee(rs.getBigDecimal("tfc_normal_current_fee"));
				busFareDTO.setNormalNewFee(rs.getBigDecimal("tfc_normal_new_fee"));
				busFareDTO.setNormalRoundFee(rs.getBigDecimal("tfc_normal_round_fee"));
				busFareDTO.setLuxuryCurrentFee(rs.getBigDecimal("tfc_luxury_current_fee"));
				busFareDTO.setLuxuryNewFee(rs.getBigDecimal("tfc_luxury_new_fee"));
				busFareDTO.setLuxuryRoundFee(rs.getBigDecimal("tfc_luxury_round_fee"));
				busFareDTO.setSemiLuxuryCurrentFee(rs.getBigDecimal("tfc_semi_luxury_current_fee"));
				busFareDTO.setSemiLuxuryNewFee(rs.getBigDecimal("tfc_semi_luxury_new_fee"));
				busFareDTO.setSemiLuxuryRoundFee(rs.getBigDecimal("tfc_semi_luxury_round_fee"));
				busFareDTO.setSuperLuxuryCurrentFee(rs.getBigDecimal("tfc_super_luxury_current_fee"));
				busFareDTO.setSuperLuxuryNewFee(rs.getBigDecimal("tfc_super_luxury_new_fee"));
				busFareDTO.setSuperLuxuryRoundFee(rs.getBigDecimal("tfc_super_luxury_round_fee"));
				busFareDTO.setHighwayCurrentFee(rs.getBigDecimal("tfc_hway_current_fee"));
				busFareDTO.setHighwayNewFee(rs.getBigDecimal("tfc_hway_new_fee"));
				busFareDTO.setHighwayRoundFee(rs.getBigDecimal("tfc_highway_round_fee"));
				busFareDTO.setSisuSariyaHalfNoramlFee(rs.getBigDecimal("tfc_halfof_normal_fee"));
				busFareDTO.setSisuSariyaHalfAdjestedFee(rs.getBigDecimal("tfc_half_adjuested"));
				busFareDTO.setSisuSariyaHalfdiffrentFee(rs.getBigDecimal("tfc_half_different"));
				busFareDTO.setSisuSariyaQuarterNoramlFee(rs.getBigDecimal("tfc_quarterof_normal_fee"));
				busFareDTO.setSisuSariyaQuarterAdjestedFee(rs.getBigDecimal("tfc_quarter_adjuested"));
				busFareDTO.setSisuSariyaQuarterdiffrentFee(rs.getBigDecimal("tfc_quarter_different"));
				busFareDTO.setCreateBy(rs.getString("tfc_created_by"));
				busFareDTO.setCreatedDateTS(rs.getTimestamp("tfc_created_date"));
				busFareDTO.setModifiedBy(rs.getString("tfc_modified_by"));
				busFareDTO.setModifiedDateTS(rs.getTimestamp("tfc_modified_date"));
				busFareDTO.setDifferenceWithCurrentFare(rs.getBigDecimal("tfc_difference_with_curr_fare"));
				returnList.add(busFareDTO);
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
	public String generateTempFareReferenceNo() {
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

			String sql = "SELECT tfc_fare_reference_no " + " FROM public.nt_temporary_fee_circle "
					+ " ORDER BY tfc_created_date desc " + " LIMIT 1";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				result = rs.getString("tfc_fare_reference_no");
			}

			if (result != null) {

				lastyr = result.substring(3, 5);
				if (!lastyr.equals(currYear)) {
					String ApprecordcountN = "00001";
					strAppNo = "TPR" + currYear + ApprecordcountN;
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
					strAppNo = "TPR" + currYear + ApprecordcountN;
				}
			} else
				strAppNo = "TPR" + currYear + "00001";

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
	public boolean insertTempReferenceData(String refNo, List<BusFareDTO> stageList, String user,
			List<BusFareDTO> newFeeList, BusFareDTO busFareDTO) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isUpdate = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int i = 0; i < stageList.size(); i++) {

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_temporary_fee_circle");

				String sql = "INSERT INTO public.nt_temporary_fee_circle "
						+ "(tfc_seq_no, tfc_fare_reference_no, tfc_stage, tfc_created_by, tfc_created_date, tfc_difference_with_curr_fare,tfc_cost_change_presentage) "
						+ "VALUES(?, ?, ?, ?, ?, ?,?);";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setString(2, refNo);
				ps.setInt(3, stageList.get(i).getStageNo());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setDouble(7, busFareDTO.getTempCostChange());

				BigDecimal currentValue = stageList.get(i).getNormalCurrentFee();
				BigDecimal newValue = newFeeList.get(i).getNormalRoundFee();

				BigDecimal diffrentValue = new BigDecimal(0);

				int x = currentValue.compareTo(newValue);

				if (x == 1) {
					diffrentValue = currentValue.subtract(newValue);
				} else if (x == -1) {
					diffrentValue = newValue.subtract(currentValue);
				} else {
					diffrentValue = newValue.subtract(currentValue);
				}

				ps.setBigDecimal(6, diffrentValue);

				int c = ps.executeUpdate();

				if (c > 0) {
					isUpdate = true;
				} else {
					isUpdate = false;
				}

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return isUpdate;

	}

	@Override
	public boolean updateTempNormalBusRate(List<BusFareDTO> rateList, String user, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temporary_fee_circle "
						+ "SET tfc_normal_current_fee=?, tfc_normal_new_fee=?, tfc_normal_round_fee=?,tfc_modified_by=?, "
						+ "tfc_modified_date=? WHERE tfc_stage=? and tfc_fare_reference_no=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getNormalCurrentFee());
				ps.setBigDecimal(2, rateList.get(a).getNormalNewFee());
				ps.setBigDecimal(3, rateList.get(a).getNormalRoundFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());
				ps.setString(7, referenceNO);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public boolean updateTempSemiLuxuryBusRate(List<BusFareDTO> rateList, String user, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temporary_fee_circle "
						+ "SET tfc_semi_luxury_current_fee= ?, tfc_semi_luxury_new_fee=?, tfc_semi_luxury_round_fee=?, "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=? and tfc_fare_reference_no=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getSemiLuxuryCurrentFee());
				ps.setBigDecimal(2, rateList.get(a).getSemiLuxuryNewFee());
				ps.setBigDecimal(3, rateList.get(a).getSemiLuxuryRoundFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());
				ps.setString(7, referenceNO);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public boolean updateTempLuxuryBusRate(List<BusFareDTO> rateList, String user, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temporary_fee_circle "
						+ "SET tfc_luxury_current_fee=? , tfc_luxury_new_fee=? , tfc_luxury_round_fee=? , "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=? and tfc_fare_reference_no=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getLuxuryCurrentFee());
				ps.setBigDecimal(2, rateList.get(a).getLuxuryNewFee());
				ps.setBigDecimal(3, rateList.get(a).getLuxuryRoundFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());
				ps.setString(7, referenceNO);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public boolean updateTempSuperLuxuryBusRate(List<BusFareDTO> rateList, String user, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temporary_fee_circle "
						+ "SET tfc_super_luxury_current_fee=?, tfc_super_luxury_new_fee=?, tfc_super_luxury_round_fee=?, "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=? and tfc_fare_reference_no=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getSuperLuxuryCurrentFee());
				ps.setBigDecimal(2, rateList.get(a).getSuperLuxuryNewFee());
				ps.setBigDecimal(3, rateList.get(a).getSuperLuxuryRoundFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());
				ps.setString(7, referenceNO);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public boolean updateTempHighWayBusRate(List<BusFareDTO> rateList, String user, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temporary_fee_circle "
						+ "SET tfc_hway_current_fee=?, tfc_hway_new_fee=?, tfc_highway_round_fee=?, "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=? and tfc_fare_reference_no=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getHighwayCurrentFee());
				ps.setBigDecimal(2, rateList.get(a).getHighwayNewFee());
				ps.setBigDecimal(3, rateList.get(a).getHighwayRoundFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());
				ps.setString(7, referenceNO);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return false;
	}

	@Override
	public boolean updateTemptHalfSisuSariyaBusRate(List<BusFareDTO> rateList, String user, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temporary_fee_circle "
						+ "SET tfc_halfof_normal_fee=?, tfc_half_adjuested=?, tfc_half_different=?, "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=?  and tfc_fare_reference_no=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getSisuSariyaHalfNoramlFee());
				ps.setBigDecimal(2, rateList.get(a).getSisuSariyaHalfAdjestedFee());
				ps.setBigDecimal(3, rateList.get(a).getSisuSariyaHalfdiffrentFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());
				ps.setString(7, referenceNO);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return false;
	}

	@Override
	public boolean updateTempQuarterSisuSariyBusRate(List<BusFareDTO> rateList, String user, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			for (int a = 0; a < rateList.size(); a++) {

				String sql = "UPDATE public.nt_temporary_fee_circle "
						+ "SET tfc_quarterof_normal_fee=?, tfc_quarter_adjuested=?, tfc_quarter_different=?, "
						+ "tfc_modified_by=?, tfc_modified_date=? WHERE tfc_stage=?  and tfc_fare_reference_no=?";

				ps = con.prepareStatement(sql);
				ps.setBigDecimal(1, rateList.get(a).getSisuSariyaQuarterNoramlFee());
				ps.setBigDecimal(2, rateList.get(a).getSisuSariyaQuarterAdjestedFee());
				ps.setBigDecimal(3, rateList.get(a).getSisuSariyaQuarterdiffrentFee());
				ps.setString(4, user);
				ps.setTimestamp(5, timestamp);
				ps.setInt(6, rateList.get(a).getStageNo());
				ps.setString(7, referenceNO);

				ps.executeUpdate();

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return false;
	}

	@Override
	public String getBusOrderCode(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String orderCode = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT code,category_order FROM public.nt_r_bus_category where code=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, code);
			rs = ps.executeQuery();

			if (rs.next()) {
				orderCode = rs.getString("category_order");
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return orderCode;
	}

	@Override
	public boolean insertFareHistory(List<BusFareDTO> tFeeListForPrevActiveRef) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isApproved = false;
		try {
			for (int i = 0; i < tFeeListForPrevActiveRef.size(); i++) {
				con = ConnectionManager.getConnection();
				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_fee_circle");
				String query = "INSERT INTO public.nt_h_fee_circle (tfc_seq_no, tfc_fare_reference_no, tfc_stage, tfc_normal_current_fee, tfc_normal_new_fee, tfc_normal_round_fee, tfc_luxury_current_fee, tfc_luxury_new_fee, tfc_luxury_round_fee, tfc_semi_luxury_current_fee, tfc_semi_luxury_new_fee, tfc_semi_luxury_round_fee, tfc_super_luxury_current_fee, tfc_super_luxury_new_fee, tfc_super_luxury_round_fee, tfc_hway_current_fee, tfc_hway_new_fee, tfc_highway_round_fee, tfc_halfof_normal_fee, tfc_half_adjuested, tfc_half_different, tfc_quarterof_normal_fee, tfc_quarter_adjuested, tfc_quarter_different, tfc_created_by, tfc_created_date, tfc_modified_by, tfc_modified_date, tfc_difference_with_curr_fare) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
				ps = con.prepareStatement(query);
				ps.setLong(1, seqNo);
				ps.setString(2, tFeeListForPrevActiveRef.get(i).getPrevActiveBusFareRefNo());
				ps.setInt(3, tFeeListForPrevActiveRef.get(i).getStageNo());
				ps.setBigDecimal(4, tFeeListForPrevActiveRef.get(i).getNormalCurrentFee());
				ps.setBigDecimal(5, tFeeListForPrevActiveRef.get(i).getNormalNewFee());
				ps.setBigDecimal(6, tFeeListForPrevActiveRef.get(i).getNormalRoundFee());
				ps.setBigDecimal(7, tFeeListForPrevActiveRef.get(i).getLuxuryCurrentFee());
				ps.setBigDecimal(8, tFeeListForPrevActiveRef.get(i).getLuxuryNewFee());
				ps.setBigDecimal(9, tFeeListForPrevActiveRef.get(i).getLuxuryRoundFee());
				ps.setBigDecimal(10, tFeeListForPrevActiveRef.get(i).getSemiLuxuryCurrentFee());
				ps.setBigDecimal(11, tFeeListForPrevActiveRef.get(i).getSemiLuxuryNewFee());
				ps.setBigDecimal(12, tFeeListForPrevActiveRef.get(i).getSemiLuxuryRoundFee());
				ps.setBigDecimal(13, tFeeListForPrevActiveRef.get(i).getSuperLuxuryCurrentFee());
				ps.setBigDecimal(14, tFeeListForPrevActiveRef.get(i).getSuperLuxuryNewFee());
				ps.setBigDecimal(15, tFeeListForPrevActiveRef.get(i).getSuperLuxuryRoundFee());
				ps.setBigDecimal(16, tFeeListForPrevActiveRef.get(i).getHighwayCurrentFee());
				ps.setBigDecimal(17, tFeeListForPrevActiveRef.get(i).getHighwayNewFee());
				ps.setBigDecimal(18, tFeeListForPrevActiveRef.get(i).getHighwayRoundFee());
				ps.setBigDecimal(19, tFeeListForPrevActiveRef.get(i).getSisuSariyaHalfNoramlFee());
				ps.setBigDecimal(20, tFeeListForPrevActiveRef.get(i).getSisuSariyaHalfAdjestedFee());
				ps.setBigDecimal(21, tFeeListForPrevActiveRef.get(i).getSisuSariyaHalfdiffrentFee());
				ps.setBigDecimal(22, tFeeListForPrevActiveRef.get(i).getSisuSariyaQuarterNoramlFee());
				ps.setBigDecimal(23, tFeeListForPrevActiveRef.get(i).getSisuSariyaQuarterAdjestedFee());
				ps.setBigDecimal(24, tFeeListForPrevActiveRef.get(i).getSisuSariyaQuarterdiffrentFee());
				ps.setString(25, tFeeListForPrevActiveRef.get(i).getCreateBy());
				ps.setTimestamp(26, tFeeListForPrevActiveRef.get(i).getCreatedDateTS());
				ps.setString(27, tFeeListForPrevActiveRef.get(i).getModifiedBy());
				ps.setTimestamp(28, tFeeListForPrevActiveRef.get(i).getModifiedDateTS());
				ps.setBigDecimal(29, tFeeListForPrevActiveRef.get(i).getDifferenceWithCurrentFare());

				int a = ps.executeUpdate();

				if (a > 0) {
					isApproved = true;
				} else {
					isApproved = false;
				}

				con.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public List<BusFareDTO> getTempNormalFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_normal_current_fee, tfc_normal_new_fee, tfc_normal_round_fee FROM public.nt_temp_fee_circle where tfc_fare_reference_no=? and (tfc_normal_current_fee <> 0 or tfc_normal_new_fee <> 0 or tfc_normal_round_fee <> 0);";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setNormalCurrentFee(rs.getBigDecimal("tfc_normal_current_fee"));
				busFareDTO.setNormalNewFee(rs.getBigDecimal("tfc_normal_new_fee"));
				busFareDTO.setNormalRoundFee(rs.getBigDecimal("tfc_normal_round_fee"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getTempLuxeryFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_luxury_current_fee, tfc_luxury_new_fee, tfc_luxury_round_fee FROM public.nt_temp_fee_circle where tfc_fare_reference_no=? and (tfc_luxury_current_fee <> 0 or tfc_luxury_new_fee <> 0 or tfc_luxury_round_fee <> 0);";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setLuxuryCurrentFee(rs.getBigDecimal("tfc_luxury_current_fee"));
				busFareDTO.setLuxuryNewFee(rs.getBigDecimal("tfc_luxury_new_fee"));
				busFareDTO.setLuxuryRoundFee(rs.getBigDecimal("tfc_luxury_round_fee"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getBusFareReferenceNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> data = new ArrayList<BusFareDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct mfc_fare_reference_no from public.nt_m_fee_circle_master";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {
				dto = new BusFareDTO();
				dto.setFareReferenceNo(rs.getString("mfc_fare_reference_no"));
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
	public List<BusFareDTO> getTempBusFareReferenceNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> data = new ArrayList<BusFareDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct tfc_fare_reference_no  from public.nt_temporary_fee_circle";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {
				dto = new BusFareDTO();
				dto.setFareReferenceNo(rs.getString("tfc_fare_reference_no"));
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
	public List<BusFareDTO> getBusFareRateData(BusFareDTO dtos) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		String WHERE_SQL = "";
		boolean whereadded = false;

		if (dtos.getFareReferenceNo() != null && !dtos.getFareReferenceNo().equals("")) {
			WHERE_SQL = WHERE_SQL + " WHERE mfc_fare_reference_no = " + "'" + dtos.getFareReferenceNo() + "' ";
			whereadded = true;
		}
		if (dtos.getStatus() != null && !dtos.getStatus().equals("")) {
			if (whereadded) {
				WHERE_SQL = WHERE_SQL + " AND mfc_status = " + "'" + dtos.getStatus() + "' ";

			} else {
				WHERE_SQL = WHERE_SQL + " WHERE mfc_status = " + "'" + dtos.getStatus() + "' ";
				whereadded = true;
			}
		}

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct mfc_fare_reference_no,mfc_status, mfc_cost_change_presentage, mfc_created_by, mfc_created_date "
					+ "from public.nt_m_fee_circle_master " + WHERE_SQL;

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {
				dto = new BusFareDTO();
				dto.setFareReferenceNo(rs.getString("mfc_fare_reference_no"));
				dto.setCurentCostChange(rs.getDouble("mfc_cost_change_presentage"));
				dto.setCreateBy(rs.getString("mfc_created_by"));

				String approveStatusCode = rs.getString("mfc_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					} else if (approveStatusCode.equals("I")) {
						approveStatus = "INACTIVE";
					} else if (approveStatusCode.equals("C")) {
						approveStatus = "CHECKED";
					} else if (approveStatusCode.equals("M")) {
						approveStatus = "RECOMMENDED";
					}
				}

				dto.setStatus(approveStatus);

				Timestamp ts = rs.getTimestamp("mfc_created_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

					dto.setCreateDateString(formattedDate);
				} else {
					dto.setCreateDateString("N/A");
				}

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
	public List<BusFareDTO> getTempBusFareRateData(BusFareDTO busFareDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> data = new ArrayList<BusFareDTO>();
		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct tfc_fare_reference_no,tfc_cost_change_presentage, tfc_created_date,"
					+ " tfc_created_by from public.nt_temporary_fee_circle where tfc_fare_reference_no=? ";

			ps = con.prepareStatement(query);
			ps.setString(1, busFareDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {
				dto = new BusFareDTO();
				dto.setFareReferenceNo(rs.getString("tfc_fare_reference_no"));
				dto.setCurentCostChange(rs.getDouble("tfc_cost_change_presentage"));
				dto.setCreateBy(rs.getString("tfc_created_by"));
				dto.setStatus("TEMPORARY");
				Timestamp ts = rs.getTimestamp("tfc_created_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

					dto.setCreateDateString(formattedDate);
				} else {
					dto.setCreateDateString("N/A");
				}

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
	public List<BusFareDTO> getTempSemiLuxeryFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_semi_luxury_current_fee, tfc_semi_luxury_new_fee, tfc_semi_luxury_round_fee FROM public.nt_temp_fee_circle where tfc_fare_reference_no=? and (tfc_semi_luxury_current_fee <> 0 or tfc_semi_luxury_new_fee <> 0 or tfc_semi_luxury_round_fee <> 0) ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setSemiLuxuryCurrentFee(rs.getBigDecimal("tfc_semi_luxury_current_fee"));
				busFareDTO.setSemiLuxuryNewFee(rs.getBigDecimal("tfc_semi_luxury_new_fee"));
				busFareDTO.setSemiLuxuryRoundFee(rs.getBigDecimal("tfc_semi_luxury_round_fee"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getTempSuperLuxeryFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_super_luxury_current_fee, tfc_super_luxury_new_fee, tfc_super_luxury_round_fee FROM public.nt_temp_fee_circle where tfc_fare_reference_no=? and (tfc_super_luxury_current_fee <> 0 or tfc_super_luxury_new_fee <> 0 or tfc_super_luxury_round_fee <> 0)  ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setSuperLuxuryCurrentFee(rs.getBigDecimal("tfc_super_luxury_current_fee"));
				busFareDTO.setSuperLuxuryNewFee(rs.getBigDecimal("tfc_super_luxury_new_fee"));
				busFareDTO.setSuperLuxuryRoundFee(rs.getBigDecimal("tfc_super_luxury_round_fee"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getTempHighwayFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_hway_current_fee, tfc_hway_new_fee, tfc_highway_round_fee FROM public.nt_temp_fee_circle where tfc_fare_reference_no=? and (tfc_highway_round_fee <> 0 or tfc_hway_current_fee <> 0 or tfc_hway_new_fee <> 0) ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setHighwayCurrentFee(rs.getBigDecimal("tfc_hway_current_fee"));
				busFareDTO.setHighwayNewFee(rs.getBigDecimal("tfc_hway_new_fee"));
				busFareDTO.setHighwayRoundFee(rs.getBigDecimal("tfc_highway_round_fee"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getTempSisuSeriyaHalfFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_halfof_normal_fee, tfc_half_adjuested, tfc_half_different FROM public.nt_temp_fee_circle where tfc_fare_reference_no=? and (tfc_halfof_normal_fee <> 0 or tfc_half_adjuested <> 0 or tfc_half_different <> 0) ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setSisuSariyaHalfNoramlFee(rs.getBigDecimal("tfc_halfof_normal_fee"));
				busFareDTO.setSisuSariyaHalfAdjestedFee(rs.getBigDecimal("tfc_half_adjuested"));
				busFareDTO.setSisuSariyaHalfdiffrentFee(rs.getBigDecimal("tfc_half_different"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getTempSisuSeriyaQuaterFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_quarterof_normal_fee, tfc_quarter_adjuested, tfc_quarter_different FROM public.nt_temp_fee_circle where tfc_fare_reference_no=? and (tfc_quarterof_normal_fee <> 0 or tfc_quarter_adjuested <> 0 or tfc_quarter_different <> 0) ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setSisuSariyaQuarterNoramlFee(rs.getBigDecimal("tfc_quarterof_normal_fee"));
				busFareDTO.setSisuSariyaQuarterAdjestedFee(rs.getBigDecimal("tfc_quarter_adjuested"));
				busFareDTO.setSisuSariyaQuarterdiffrentFee(rs.getBigDecimal("tfc_quarter_different"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getPrevActiveNormalFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_normal_current_fee, tfc_normal_new_fee, tfc_normal_round_fee FROM public.nt_t_fee_circle where tfc_fare_reference_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setNormalCurrentFee(rs.getBigDecimal("tfc_normal_current_fee"));
				busFareDTO.setNormalNewFee(rs.getBigDecimal("tfc_normal_new_fee"));
				busFareDTO.setNormalRoundFee(rs.getBigDecimal("tfc_normal_round_fee"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getPrevActiveLuxeryFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_luxury_current_fee, tfc_luxury_new_fee, tfc_luxury_round_fee FROM public.nt_t_fee_circle where tfc_fare_reference_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setLuxuryCurrentFee(rs.getBigDecimal("tfc_luxury_current_fee"));
				busFareDTO.setLuxuryNewFee(rs.getBigDecimal("tfc_luxury_new_fee"));
				busFareDTO.setLuxuryRoundFee(rs.getBigDecimal("tfc_luxury_round_fee"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getPrevActiveSemiLuxeryFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_semi_luxury_current_fee, tfc_semi_luxury_new_fee, tfc_semi_luxury_round_fee FROM public.nt_t_fee_circle where tfc_fare_reference_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setSemiLuxuryCurrentFee(rs.getBigDecimal("tfc_semi_luxury_current_fee"));
				busFareDTO.setSemiLuxuryNewFee(rs.getBigDecimal("tfc_semi_luxury_new_fee"));
				busFareDTO.setSemiLuxuryRoundFee(rs.getBigDecimal("tfc_semi_luxury_round_fee"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getPrevActiveSuperLuxeryFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_super_luxury_current_fee, tfc_super_luxury_new_fee, tfc_super_luxury_round_fee FROM public.nt_t_fee_circle where tfc_fare_reference_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setSuperLuxuryCurrentFee(rs.getBigDecimal("tfc_super_luxury_current_fee"));
				busFareDTO.setSuperLuxuryNewFee(rs.getBigDecimal("tfc_super_luxury_new_fee"));
				busFareDTO.setSuperLuxuryRoundFee(rs.getBigDecimal("tfc_super_luxury_round_fee"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getPrevActiveHighwayFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_hway_current_fee, tfc_hway_new_fee, tfc_highway_round_fee FROM public.nt_t_fee_circle where tfc_fare_reference_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setHighwayCurrentFee(rs.getBigDecimal("tfc_hway_current_fee"));
				busFareDTO.setHighwayNewFee(rs.getBigDecimal("tfc_hway_new_fee"));
				busFareDTO.setHighwayRoundFee(rs.getBigDecimal("tfc_highway_round_fee"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getPrevActiveSisuSeriyaHalfFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_halfof_normal_fee, tfc_half_adjuested, tfc_half_different FROM public.nt_t_fee_circle where tfc_fare_reference_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setSisuSariyaHalfNoramlFee(rs.getBigDecimal("tfc_halfof_normal_fee"));
				busFareDTO.setSisuSariyaHalfAdjestedFee(rs.getBigDecimal("tfc_half_adjuested"));
				busFareDTO.setSisuSariyaHalfdiffrentFee(rs.getBigDecimal("tfc_half_different"));

				returnList.add(busFareDTO);

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
	public List<BusFareDTO> getPrevActiveSisuSeriyaQuaterFeeList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  tfc_stage, tfc_quarterof_normal_fee, tfc_quarter_adjuested, tfc_quarter_different FROM public.nt_t_fee_circle where tfc_fare_reference_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setStageNo(rs.getInt("tfc_stage"));
				busFareDTO.setSisuSariyaQuarterNoramlFee(rs.getBigDecimal("tfc_quarterof_normal_fee"));
				busFareDTO.setSisuSariyaQuarterAdjestedFee(rs.getBigDecimal("tfc_quarter_adjuested"));
				busFareDTO.setSisuSariyaQuarterdiffrentFee(rs.getBigDecimal("tfc_quarter_different"));

				returnList.add(busFareDTO);

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
	public boolean updateFareNormalDet(List<BusFareDTO> tempNormalFeeList, BusFareDTO selectDTO, String user) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isApproved = false;
		try {
			con = ConnectionManager.getConnection();
			for (int i = 0; i < tempNormalFeeList.size(); i++) {

				String query = "UPDATE public.nt_t_fee_circle SET tfc_fare_reference_no=?,  tfc_normal_current_fee=?, tfc_normal_new_fee=?, tfc_normal_round_fee=?,  tfc_modified_by=? , tfc_modified_date=? WHERE tfc_stage=? ;";
				ps = con.prepareStatement(query);
				ps.setString(1, selectDTO.getFareReferenceNo());
				ps.setBigDecimal(2, tempNormalFeeList.get(i).getNormalCurrentFee());
				ps.setBigDecimal(3, tempNormalFeeList.get(i).getNormalNewFee());
				ps.setBigDecimal(4, tempNormalFeeList.get(i).getNormalRoundFee());
				ps.setString(5, user);
				ps.setTimestamp(6, timestamp);
				ps.setInt(7, tempNormalFeeList.get(i).getStageNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isApproved = true;
				} else {
					isApproved = false;
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
		return isApproved;
	}

	@Override
	public boolean updateFareLuxeryDet(List<BusFareDTO> tempLuxeryFeeList, BusFareDTO selectDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isApproved = false;
		try {
			con = ConnectionManager.getConnection();
			for (int i = 0; i < tempLuxeryFeeList.size(); i++) {

				String query = "UPDATE public.nt_t_fee_circle SET tfc_fare_reference_no=?,  tfc_luxury_current_fee=? , tfc_luxury_new_fee=? , tfc_luxury_round_fee=? ,  tfc_modified_by=? , tfc_modified_date=? WHERE tfc_stage=? ;";
				ps = con.prepareStatement(query);
				ps.setString(1, selectDTO.getFareReferenceNo());
				ps.setBigDecimal(2, tempLuxeryFeeList.get(i).getLuxuryCurrentFee());
				ps.setBigDecimal(3, tempLuxeryFeeList.get(i).getLuxuryNewFee());
				ps.setBigDecimal(4, tempLuxeryFeeList.get(i).getLuxuryRoundFee());
				ps.setString(5, loginUser);
				ps.setTimestamp(6, timestamp);
				ps.setInt(7, tempLuxeryFeeList.get(i).getStageNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isApproved = true;
				} else {
					isApproved = false;
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
		return isApproved;
	}

	@Override
	public boolean updateFareSemiLuxeryDet(List<BusFareDTO> tempSemiLuxeryFeeList, BusFareDTO selectDTO,
			String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isApproved = false;
		try {
			con = ConnectionManager.getConnection();
			for (int i = 0; i < tempSemiLuxeryFeeList.size(); i++) {

				String query = "UPDATE public.nt_t_fee_circle SET tfc_fare_reference_no=?,  tfc_semi_luxury_current_fee=?, tfc_semi_luxury_new_fee=?, tfc_semi_luxury_round_fee=? ,  tfc_modified_by=? , tfc_modified_date=? WHERE tfc_stage=? ;";
				ps = con.prepareStatement(query);
				ps.setString(1, selectDTO.getFareReferenceNo());
				ps.setBigDecimal(2, tempSemiLuxeryFeeList.get(i).getSemiLuxuryCurrentFee());
				ps.setBigDecimal(3, tempSemiLuxeryFeeList.get(i).getSemiLuxuryNewFee());
				ps.setBigDecimal(4, tempSemiLuxeryFeeList.get(i).getSemiLuxuryRoundFee());
				ps.setString(5, loginUser);
				ps.setTimestamp(6, timestamp);
				ps.setInt(7, tempSemiLuxeryFeeList.get(i).getStageNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isApproved = true;
				} else {
					isApproved = false;
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
		return isApproved;
	}

	@Override
	public boolean updateFareSuperLuxeryDet(List<BusFareDTO> tempSuperLuxeryFeeList, BusFareDTO selectDTO,
			String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isApproved = false;
		try {
			con = ConnectionManager.getConnection();
			for (int i = 0; i < tempSuperLuxeryFeeList.size(); i++) {

				String query = "UPDATE public.nt_t_fee_circle SET tfc_fare_reference_no=?,  tfc_super_luxury_current_fee=?, tfc_super_luxury_new_fee=?, tfc_super_luxury_round_fee=? ,  tfc_modified_by=? , tfc_modified_date=? WHERE tfc_stage=? ;";
				ps = con.prepareStatement(query);
				ps.setString(1, selectDTO.getFareReferenceNo());
				ps.setBigDecimal(2, tempSuperLuxeryFeeList.get(i).getSuperLuxuryCurrentFee());
				ps.setBigDecimal(3, tempSuperLuxeryFeeList.get(i).getSuperLuxuryNewFee());
				ps.setBigDecimal(4, tempSuperLuxeryFeeList.get(i).getSuperLuxuryRoundFee());
				ps.setString(5, loginUser);
				ps.setTimestamp(6, timestamp);
				ps.setInt(7, tempSuperLuxeryFeeList.get(i).getStageNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isApproved = true;
				} else {
					isApproved = false;
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
		return isApproved;
	}

	@Override
	public boolean updateFareHighwayDet(List<BusFareDTO> tempHighwayFeeList, BusFareDTO selectDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isApproved = false;
		try {
			con = ConnectionManager.getConnection();
			for (int i = 0; i < tempHighwayFeeList.size(); i++) {

				String query = "UPDATE public.nt_t_fee_circle SET tfc_fare_reference_no=?,  tfc_hway_current_fee=?, tfc_hway_new_fee=?, tfc_highway_round_fee=? ,  tfc_modified_by=? , tfc_modified_date=? WHERE tfc_stage=? ;";
				ps = con.prepareStatement(query);
				ps.setString(1, selectDTO.getFareReferenceNo());
				ps.setBigDecimal(2, tempHighwayFeeList.get(i).getHighwayCurrentFee());
				ps.setBigDecimal(3, tempHighwayFeeList.get(i).getHighwayNewFee());
				ps.setBigDecimal(4, tempHighwayFeeList.get(i).getHighwayRoundFee());
				ps.setString(5, loginUser);
				ps.setTimestamp(6, timestamp);
				ps.setInt(7, tempHighwayFeeList.get(i).getStageNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isApproved = true;
				} else {
					isApproved = false;
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
		return isApproved;
	}

	@Override
	public boolean updateFareSisuHalfDet(List<BusFareDTO> tempSisuSeriyaHalfFeeList, BusFareDTO selectDTO,
			String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isApproved = false;
		try {
			con = ConnectionManager.getConnection();
			for (int i = 0; i < tempSisuSeriyaHalfFeeList.size(); i++) {

				String query = "UPDATE public.nt_t_fee_circle SET tfc_fare_reference_no=?,  tfc_halfof_normal_fee=? , tfc_half_adjuested=? , tfc_half_different=? ,  tfc_modified_by=? , tfc_modified_date=? WHERE tfc_stage=? ;";
				ps = con.prepareStatement(query);
				ps.setString(1, selectDTO.getFareReferenceNo());
				ps.setBigDecimal(2, tempSisuSeriyaHalfFeeList.get(i).getSisuSariyaHalfNoramlFee());
				ps.setBigDecimal(3, tempSisuSeriyaHalfFeeList.get(i).getSisuSariyaHalfAdjestedFee());
				ps.setBigDecimal(4, tempSisuSeriyaHalfFeeList.get(i).getSisuSariyaHalfdiffrentFee());
				ps.setString(5, loginUser);
				ps.setTimestamp(6, timestamp);
				ps.setInt(7, tempSisuSeriyaHalfFeeList.get(i).getStageNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isApproved = true;
				} else {
					isApproved = false;
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
		return isApproved;
	}

	@Override
	public boolean updateFareSisuQuaterDet(List<BusFareDTO> tempSisuSeriyaQuaterFeeList, BusFareDTO selectDTO,
			String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean isApproved = false;
		try {
			con = ConnectionManager.getConnection();
			for (int i = 0; i < tempSisuSeriyaQuaterFeeList.size(); i++) {

				String query = "UPDATE public.nt_t_fee_circle SET tfc_fare_reference_no=?,  tfc_quarterof_normal_fee=? , tfc_quarter_adjuested=? , tfc_quarter_different=? ,  tfc_modified_by=? , tfc_modified_date=? WHERE tfc_stage=? ;";
				ps = con.prepareStatement(query);
				ps.setString(1, selectDTO.getFareReferenceNo());
				ps.setBigDecimal(2, tempSisuSeriyaQuaterFeeList.get(i).getSisuSariyaQuarterNoramlFee());
				ps.setBigDecimal(3, tempSisuSeriyaQuaterFeeList.get(i).getSisuSariyaQuarterAdjestedFee());
				ps.setBigDecimal(4, tempSisuSeriyaQuaterFeeList.get(i).getSisuSariyaQuarterdiffrentFee());
				ps.setString(5, loginUser);
				ps.setTimestamp(6, timestamp);
				ps.setInt(7, tempSisuSeriyaQuaterFeeList.get(i).getStageNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isApproved = true;
				} else {
					isApproved = false;
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
		return isApproved;
	}

	@Override
	public List<BusFareDTO> getDefaultBusFareRate() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "select distinct mfc_fare_reference_no,mfc_status, mfc_cost_change_presentage, mfc_created_by, mfc_created_date "
					+ "from public.nt_m_fee_circle_master where mfc_status='A' or  mfc_status='P' or  mfc_status='C' or  mfc_status='M' ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {
				dto = new BusFareDTO();
				dto.setFareReferenceNo(rs.getString("mfc_fare_reference_no"));
				dto.setCurentCostChange(rs.getDouble("mfc_cost_change_presentage"));
				dto.setCreateBy(rs.getString("mfc_created_by"));

				String approveStatusCode = rs.getString("mfc_status");
				String approveStatus = "";

				if (approveStatusCode != null) {
					if (approveStatusCode.equals("A")) {
						approveStatus = "APPROVED";
					} else if (approveStatusCode.equals("R")) {
						approveStatus = "REJECTED";
					} else if (approveStatusCode.equals("P")) {
						approveStatus = "PENDING";
					} else if (approveStatusCode.equals("I")) {
						approveStatus = "INACTIVE";
					} else if (approveStatusCode.equals("C")) {
						approveStatus = "CHECKED";
					} else if (approveStatusCode.equals("M")) {
						approveStatus = "RECOMMENDED";
					}
				}

				dto.setStatus(approveStatus);

				Timestamp ts = rs.getTimestamp("mfc_created_date");

				if (ts != null) {
					Date date = new Date();
					date.setTime(ts.getTime());
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

					dto.setCreateDateString(formattedDate);
				} else {
					dto.setCreateDateString("N/A");
				}

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
	public List<BusFareDTO> getNormalList(String tableName, String satge, boolean isHistory, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {
			con = ConnectionManager.getConnection();

				String query = "SELECT tfc_normal_current_fee, tfc_normal_new_fee, tfc_normal_round_fee FROM "
						+ tableName + " Where tfc_fare_reference_no=? order by " + satge + " ";

				ps = con.prepareStatement(query);
				ps.setString(1, referenceNO);
		
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();
				dto.setNormalCurrentFee(rs.getBigDecimal("tfc_normal_current_fee"));
				dto.setNormalNewFee(rs.getBigDecimal("tfc_normal_new_fee"));
				dto.setNormalRoundFee(rs.getBigDecimal("tfc_normal_round_fee"));
				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;
	}

	@Override
	public List<BusFareDTO> getSemiLuxuaryList(String tableName, String satge, boolean isHistory, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tfc_semi_luxury_current_fee, tfc_semi_luxury_new_fee, tfc_semi_luxury_round_fee FROM "
						+ tableName + " Where tfc_fare_reference_no=?  order by " + satge + " ";

			ps = con.prepareStatement(query);
			ps.setString(1, referenceNO);
			
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();
				dto.setSemiLuxuryCurrentFee(rs.getBigDecimal("tfc_semi_luxury_current_fee"));
				dto.setSemiLuxuryNewFee(rs.getBigDecimal("tfc_semi_luxury_new_fee"));
				dto.setSemiLuxuryRoundFee(rs.getBigDecimal("tfc_semi_luxury_round_fee"));
				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;
	}

	@Override
	public List<BusFareDTO> getLuxuaryList(String tableName, String satge, boolean isHistory, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {
			con = ConnectionManager.getConnection();


				String query = "SELECT tfc_luxury_current_fee, tfc_luxury_new_fee, tfc_luxury_round_fee FROM "
						+ tableName + " Where tfc_fare_reference_no=? order by " + satge + " ";

				ps = con.prepareStatement(query);
				ps.setString(1, referenceNO);

			

			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();
				dto.setLuxuryCurrentFee(rs.getBigDecimal("tfc_luxury_current_fee"));
				dto.setLuxuryNewFee(rs.getBigDecimal("tfc_luxury_new_fee"));
				dto.setLuxuryRoundFee(rs.getBigDecimal("tfc_luxury_round_fee"));
				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;
	}

	@Override
	public List<BusFareDTO> getHighWayList(String tableName, String satge, boolean isHistory, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {
			con = ConnectionManager.getConnection();

				String query = "SELECT tfc_hway_current_fee, tfc_hway_new_fee, tfc_highway_round_fee FROM " + tableName
						+ " Where tfc_fare_reference_no=? order by " + satge + " ";

				ps = con.prepareStatement(query);
				ps.setString(1, referenceNO);
			
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();
				dto.setHighwayCurrentFee(rs.getBigDecimal("tfc_hway_current_fee"));
				dto.setHighwayNewFee(rs.getBigDecimal("tfc_hway_new_fee"));
				dto.setHighwayRoundFee(rs.getBigDecimal("tfc_highway_round_fee"));
				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;
	}

	@Override
	public List<BusFareDTO> getSuperLuxuaryList(String tableName, String satge, boolean isHistory, String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {
			con = ConnectionManager.getConnection();

	
				String query = "SELECT tfc_super_luxury_current_fee, tfc_super_luxury_new_fee, tfc_super_luxury_round_fee FROM "
						+ tableName + " Where tfc_fare_reference_no=? order by " + satge + " ";

				ps = con.prepareStatement(query);
				ps.setString(1, referenceNO);

			
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();
				dto.setSuperLuxuryCurrentFee(rs.getBigDecimal("tfc_super_luxury_current_fee"));
				dto.setSuperLuxuryNewFee(rs.getBigDecimal("tfc_super_luxury_new_fee"));
				dto.setSuperLuxuryRoundFee(rs.getBigDecimal("tfc_super_luxury_round_fee"));
				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;
	}

	@Override
	public List<BusFareDTO> getSisuSariyaHalfList(String tableName, String satge, boolean isHistory,
			String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {
			con = ConnectionManager.getConnection();

		

				String query = "SELECT tfc_halfof_normal_fee, tfc_half_adjuested, tfc_half_different FROM " + tableName
						+ " Where tfc_fare_reference_no=? order by " + satge + " ";

				ps = con.prepareStatement(query);
				ps.setString(1, referenceNO);

			
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();
				dto.setSisuSariyaHalfNoramlFee(rs.getBigDecimal("tfc_halfof_normal_fee"));
				dto.setSisuSariyaHalfAdjestedFee(rs.getBigDecimal("tfc_half_adjuested"));
				dto.setSisuSariyaHalfdiffrentFee(rs.getBigDecimal("tfc_half_different"));
				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;
	}

	@Override
	public List<BusFareDTO> getSisuSariyaQuaterList(String tableName, String satge, boolean isHistory,
			String referenceNO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {
			con = ConnectionManager.getConnection();


				String query = "SELECT tfc_quarterof_normal_fee, tfc_quarter_adjuested, tfc_quarter_different FROM "
						+ tableName + " Where tfc_fare_reference_no=? order by " + satge + " ";

				ps = con.prepareStatement(query);
				ps.setString(1, referenceNO);
			
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();
				dto.setSisuSariyaQuarterNoramlFee(rs.getBigDecimal("tfc_quarterof_normal_fee"));
				dto.setSisuSariyaQuarterAdjestedFee(rs.getBigDecimal("tfc_quarter_adjuested"));
				dto.setSisuSariyaQuarterdiffrentFee(rs.getBigDecimal("tfc_quarter_different"));
				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;
	}

	@Override
	public List<BusFareDTO> getTableStageList(String tableName, String satge, boolean isHistory, String refeNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> data = new ArrayList<BusFareDTO>();

		try {

			con = ConnectionManager.getConnection();
			
				String query = "SELECT tfc_fare_reference_no, tfc_stage FROM " + tableName
						+ " Where tfc_fare_reference_no=?  order by tfc_stage";

				ps = con.prepareStatement(query);
				ps.setString(1, refeNo);
			
			rs = ps.executeQuery();

			BusFareDTO dto;

			while (rs.next()) {

				dto = new BusFareDTO();
				dto.setStageNo(rs.getInt("tfc_stage"));
				dto.setFareReferenceNo(rs.getString("tfc_fare_reference_no"));
				data.add(dto);

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return data;
	}

	@Override
	public boolean busFareRejected(BusFareDTO selectDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		boolean isApproved = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_m_fee_circle_master SET  mfc_status=?,  mfc_modified_by=?, mfc_modified_date=? WHERE mfc_fare_reference_no=?;";

			ps = con.prepareStatement(query);

			ps.setString(1, "R");
			ps.setString(2, loginUser);
			ps.setTimestamp(3, timestamp);
			ps.setString(4, selectDTO.getFareReferenceNo());

			int a = ps.executeUpdate();

			if (a > 0) {
				isApproved = true;
			} else {
				isApproved = false;
			}

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isApproved;
	}

	@Override
	public boolean saveEditBusRateFee(BusFareDTO busFareDTO, String referenceNO, String user, int StageNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean dataAdded = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String UPDATE_SQL = "";
		boolean updateAdded = false;

		if (busFareDTO.getEditNormalRoundFee() != null) {
			UPDATE_SQL = UPDATE_SQL + " Update public.nt_temp_fee_circle set tfc_normal_round_fee = " + "'"
					+ busFareDTO.getEditNormalRoundFee() + "', tfc_isedited_normal='Y', tfc_modified_by =" + "'" + user
					+ "', tfc_modified_date =" + "'" + timestamp + "' ";
			updateAdded = true;
		}
		if (busFareDTO.getEditSemiLuxuaryRoundFee() != null) {
			if (updateAdded) {
				UPDATE_SQL = UPDATE_SQL + " , tfc_semi_luxury_round_fee = " + "'"
						+ busFareDTO.getEditSemiLuxuaryRoundFee() + "', tfc_isedited_semiluxary='Y' ";

			} else {
				UPDATE_SQL = UPDATE_SQL + "Update public.nt_temp_fee_circle set tfc_semi_luxury_round_fee = " + "'"
						+ busFareDTO.getEditSemiLuxuaryRoundFee() + "', tfc_isedited_normal='Y', tfc_modified_by ="
						+ "'" + user + "', tfc_modified_date =" + "'" + timestamp + "' ";
				updateAdded = true;
			}
		}

		if (busFareDTO.getEditLuxuryRoundFee() != null) {
			if (updateAdded) {
				UPDATE_SQL = UPDATE_SQL + " , tfc_luxury_round_fee = " + "'" + busFareDTO.getEditLuxuryRoundFee()
						+ "', tfc_isedited_luxary='Y' ";

			} else {
				UPDATE_SQL = UPDATE_SQL + "Update public.nt_temp_fee_circle set tfc_luxury_round_fee = " + "'"
						+ busFareDTO.getEditLuxuryRoundFee() + "', tfc_isedited_normal='Y', tfc_modified_by =" + "'"
						+ user + "', tfc_modified_date =" + "'" + timestamp + "' ";
				updateAdded = true;
			}
		}

		if (busFareDTO.getEditSuperLuxuryRoundFee() != null) {
			if (updateAdded) {
				UPDATE_SQL = UPDATE_SQL + " , tfc_super_luxury_round_fee = " + "'"
						+ busFareDTO.getEditSuperLuxuryRoundFee() + "', tfc_isedited_superluxary='Y' ";

			} else {
				UPDATE_SQL = UPDATE_SQL + "Update public.nt_temp_fee_circle set tfc_super_luxury_round_fee = " + "'"
						+ busFareDTO.getEditSuperLuxuryRoundFee() + "', tfc_isedited_normal='Y', tfc_modified_by ="
						+ "'" + user + "', tfc_modified_date =" + "'" + timestamp + "' ";
				updateAdded = true;
			}
		}

		if (busFareDTO.getEdithighWayRoundFee() != null) {
			if (updateAdded) {
				UPDATE_SQL = UPDATE_SQL + " , tfc_highway_round_fee = " + "'" + busFareDTO.getEdithighWayRoundFee()
						+ "', tfc_isedited_expresswaybus='Y' ";

			} else {
				UPDATE_SQL = UPDATE_SQL + "Update public.nt_temp_fee_circle set tfc_highway_round_fee = " + "'"
						+ busFareDTO.getEdithighWayRoundFee() + "', tfc_isedited_normal='Y', tfc_modified_by =" + "'"
						+ user + "', tfc_modified_date =" + "'" + timestamp + "' ";
				updateAdded = true;
			}
		}

		if (busFareDTO.getEditSisuSariyaHalfAdjestFee() != null) {
			if (updateAdded) {
				UPDATE_SQL = UPDATE_SQL + " , tfc_half_adjuested = " + "'" + busFareDTO.getEditSisuSariyaHalfAdjestFee()
						+ "', tfc_isedited_sisuhalf='Y' ";

			} else {
				UPDATE_SQL = UPDATE_SQL + "Update public.nt_temp_fee_circle set tfc_half_adjuested = " + "'"
						+ busFareDTO.getEditSisuSariyaHalfAdjestFee() + "', tfc_isedited_normal='Y', tfc_modified_by ="
						+ "'" + user + "', tfc_modified_date =" + "'" + timestamp + "' ";
				updateAdded = true;
			}
		}

		if (busFareDTO.getEditSisuSariyaQuaterAdjestFee() != null) {
			if (updateAdded) {
				UPDATE_SQL = UPDATE_SQL + " , tfc_quarter_adjuested = " + "'"
						+ busFareDTO.getEditSisuSariyaQuaterAdjestFee() + "', tfc_isedited_sisuquarter='Y' ";

			} else {
				UPDATE_SQL = UPDATE_SQL + "Update public.nt_temp_fee_circle set tfc_quarter_adjuested = " + "'"
						+ busFareDTO.getEditSisuSariyaQuaterAdjestFee()
						+ "', tfc_isedited_normal='Y', tfc_modified_by =" + "'" + user + "', tfc_modified_date =" + "'"
						+ timestamp + "' ";
				updateAdded = true;
			}
		}

		try {
			con = ConnectionManager.getConnection();

			String sql = UPDATE_SQL + " Where tfc_fare_reference_no=? and tfc_stage=?";

			ps = con.prepareStatement(sql);
			ps.setString(1, referenceNO);
			ps.setInt(2, StageNo);

			int c = ps.executeUpdate();

			if (c > 0) {
				dataAdded = true;
			} else {
				dataAdded = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dataAdded;
	}

	@Override
	public List<BusFareDTO> getPrevActiveTempList(BusFareDTO selectDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<BusFareDTO> returnList = new ArrayList<BusFareDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT tfc_fare_reference_no FROM public.nt_temp_fee_circle where tfc_fare_reference_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectDTO.getFareReferenceNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				BusFareDTO busFareDTO = new BusFareDTO();
				busFareDTO.setFareReferenceNo(rs.getString("tfc_fare_reference_no"));

				returnList.add(busFareDTO);
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
	public boolean deleteTempList(List<BusFareDTO> viewTempListForPrevRefNo, BusFareDTO selectDTO, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isApproved = false;
		try {
			con = ConnectionManager.getConnection();
			for (int i = 0; i < viewTempListForPrevRefNo.size(); i++) {

				String query = "DELETE FROM public.nt_temp_fee_circle WHERE tfc_fare_reference_no=? ;";
				ps = con.prepareStatement(query);
				ps.setString(1, selectDTO.getFareReferenceNo());

				int a = ps.executeUpdate();

				if (a > 0) {
					isApproved = true;
				} else {
					isApproved = false;
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
		return isApproved;
	}

	@Override
	public String getActiveReferenceNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String referenceNo = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT mfc_fare_reference_no FROM public.nt_m_fee_circle_master where mfc_status='A'  order by mfc_created_date desc ;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			if (rs.next()) {
				referenceNo = rs.getString("mfc_fare_reference_no");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return referenceNo;
	}

	@Override
	public void insertFareEquationDetails(List<BusFareDTO> currentFareList, String fareReferenceNo) {

		String sql = "INSERT INTO public.nt_r_fare_equation "
				+ "(rfe_fare_reference_no, rfe_bus_category_code, rfe_equation, rfe_fare_calculated)VALUES(?, ?, ?, ?);";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);) {

			for (BusFareDTO fareDTO : currentFareList) {
				ps.setString(1, fareReferenceNo);
				ps.setString(2, fareDTO.getBusCategoryCode());
				ps.setString(3, fareDTO.getEquation());
				if (fareDTO.isFareCalculate()) {
					ps.setString(4, "Y");
				} else {
					ps.setString(4, "N");
				}
				ps.addBatch();
			}
			ps.executeBatch();
			connection.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<BusFareEquationDTO> getPreviosApprovedEquation(String referenceNo) {
		List<BusFareEquationDTO> list = new ArrayList<BusFareEquationDTO>();

		String countQuery = "SELECT *  FROM nt_r_fare_equation WHERE rfe_fare_reference_no = ?;";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(countQuery);) {

			preparedStatement.setString(1, referenceNo);

			ResultSet resultSet = preparedStatement.executeQuery();
			connection.commit();

			while (resultSet.next()) {
				BusFareEquationDTO busFareEquationDTO = new BusFareEquationDTO();
				busFareEquationDTO.setRfeBusCategoryCode(resultSet.getString("rfe_bus_category_code"));
				busFareEquationDTO.setRfeEquation(resultSet.getString("rfe_equation"));
				busFareEquationDTO.setRfeFareCalculated(resultSet.getString("rfe_fare_calculated"));
				list.add(busFareEquationDTO);
			}

			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

}
