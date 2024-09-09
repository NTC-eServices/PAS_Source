package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import lk.informatics.ntc.model.dto.FlyingSquadGroupsDTO;
import lk.informatics.ntc.model.dto.FlyingSquadSheduleDTO;
import lk.informatics.ntc.model.dto.RosterDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class FlyingSquadScheduleServiceImpl implements FlyingSquadScheduleService {

	// get all active groups for initial stage.
	public ArrayList<FlyingSquadSheduleDTO> getGroupcode() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadSheduleDTO> flyingSquadSheduleDTOList = new ArrayList<FlyingSquadSheduleDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT code, group_name\r\n" + "FROM public.nt_r_group \r\n" + "where  status = 'A'\r\n"
					+ "order by code";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadSheduleDTO flyingSquadSheduleDTO = new FlyingSquadSheduleDTO();
				flyingSquadSheduleDTO.setGroupCd(rs.getString("code"));
				flyingSquadSheduleDTO.setGroupDes(rs.getString("group_name"));
				flyingSquadSheduleDTOList.add(flyingSquadSheduleDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return flyingSquadSheduleDTOList;
	}

	// get details from the nt_t_flying_squad_shedule
	public ArrayList<FlyingSquadSheduleDTO> genarateDetails(int year, int month) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadSheduleDTO> flyingSquadSheduleDTOList = new ArrayList<FlyingSquadSheduleDTO>();
		ArrayList<FlyingSquadSheduleDTO> flyingSquadSheduleDTOListt = new ArrayList<FlyingSquadSheduleDTO>();
		ArrayList<FlyingSquadSheduleDTO> finalFlyingSquadSheduleDTOList = new ArrayList<FlyingSquadSheduleDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT fly_sch_groupcd,b.group_name,fly_sch_day1, fly_sch_day2, fly_sch_day3, fly_sch_day4, fly_sch_day5, fly_sch_day6, fly_sch_day7,\r\n"
					+ "fly_sch_day8, fly_sch_day9, fly_sch_day10, fly_sch_day11, fly_sch_day12, fly_sch_day13, fly_sch_day14, \r\n"
					+ "fly_sch_day15, fly_sch_day16, fly_sch_day17, fly_sch_day18, fly_sch_day19, fly_sch_day20, fly_sch_day21, \r\n"
					+ "fly_sch_day22, fly_sch_day23, fly_sch_day24, fly_sch_day25, fly_sch_day26, fly_sch_day27, fly_sch_day28, \r\n"
					+ "fly_sch_day29, fly_sch_day30, fly_sch_day31\r\n"
					+ "FROM public.nt_t_flying_squad_shedule , public.nt_r_group b\r\n" + "where fly_sch_year = ?\r\n"
					+ "and fly_sch_month = ? \r\n" + "and  nt_t_flying_squad_shedule.fly_sch_groupcd = b.code\r\n"
					+ "order by fly_sch_groupcd";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, year);
			stmt.setInt(2, month);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadSheduleDTO flyingSquadSheduleDTO = new FlyingSquadSheduleDTO();
				flyingSquadSheduleDTO.setGroupCd(rs.getString("fly_sch_groupcd"));
				flyingSquadSheduleDTO.setGroupDes(rs.getString("group_name"));
				flyingSquadSheduleDTO.setDay1(rs.getString("fly_sch_day1"));
				flyingSquadSheduleDTO.setDay2(rs.getString("fly_sch_day2"));
				flyingSquadSheduleDTO.setDay3(rs.getString("fly_sch_day3"));
				flyingSquadSheduleDTO.setDay4(rs.getString("fly_sch_day4"));
				flyingSquadSheduleDTO.setDay5(rs.getString("fly_sch_day5"));
				flyingSquadSheduleDTO.setDay6(rs.getString("fly_sch_day6"));
				flyingSquadSheduleDTO.setDay7(rs.getString("fly_sch_day7"));
				flyingSquadSheduleDTO.setDay8(rs.getString("fly_sch_day8"));
				flyingSquadSheduleDTO.setDay9(rs.getString("fly_sch_day9"));
				flyingSquadSheduleDTO.setDay10(rs.getString("fly_sch_day10"));
				flyingSquadSheduleDTO.setDay11(rs.getString("fly_sch_day11"));
				flyingSquadSheduleDTO.setDay12(rs.getString("fly_sch_day12"));
				flyingSquadSheduleDTO.setDay13(rs.getString("fly_sch_day13"));
				flyingSquadSheduleDTO.setDay14(rs.getString("fly_sch_day14"));
				flyingSquadSheduleDTO.setDay15(rs.getString("fly_sch_day15"));
				flyingSquadSheduleDTO.setDay16(rs.getString("fly_sch_day16"));
				flyingSquadSheduleDTO.setDay17(rs.getString("fly_sch_day17"));
				flyingSquadSheduleDTO.setDay18(rs.getString("fly_sch_day18"));
				flyingSquadSheduleDTO.setDay19(rs.getString("fly_sch_day19"));
				flyingSquadSheduleDTO.setDay20(rs.getString("fly_sch_day20"));
				flyingSquadSheduleDTO.setDay21(rs.getString("fly_sch_day21"));
				flyingSquadSheduleDTO.setDay22(rs.getString("fly_sch_day22"));
				flyingSquadSheduleDTO.setDay23(rs.getString("fly_sch_day23"));
				flyingSquadSheduleDTO.setDay24(rs.getString("fly_sch_day24"));
				flyingSquadSheduleDTO.setDay25(rs.getString("fly_sch_day25"));
				flyingSquadSheduleDTO.setDay26(rs.getString("fly_sch_day26"));
				flyingSquadSheduleDTO.setDay27(rs.getString("fly_sch_day27"));
				flyingSquadSheduleDTO.setDay28(rs.getString("fly_sch_day28"));
				flyingSquadSheduleDTO.setDay29(rs.getString("fly_sch_day29"));
				flyingSquadSheduleDTO.setDay30(rs.getString("fly_sch_day30"));
				flyingSquadSheduleDTO.setDay31(rs.getString("fly_sch_day31"));
				flyingSquadSheduleDTO.setInsert(false);

				flyingSquadSheduleDTOList.add(flyingSquadSheduleDTO);
			}

			flyingSquadSheduleDTOListt = newlyaddedgroups(year, month);
			finalFlyingSquadSheduleDTOList.addAll(flyingSquadSheduleDTOList);
			finalFlyingSquadSheduleDTOList.addAll(flyingSquadSheduleDTOListt);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return finalFlyingSquadSheduleDTOList;
	}

	// ---------------------------
	// --newly added groups---
	public ArrayList<FlyingSquadSheduleDTO> newlyaddedgroups(int year, int month) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadSheduleDTO> flyingSquadSheduleDTOList = new ArrayList<FlyingSquadSheduleDTO>();

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT code,group_name\r\n" + "FROM public.nt_r_group\r\n" + "where  status = 'A'\r\n"
					+ "and code not in ( SELECT fly_sch_groupcd \r\n"
					+ "                    FROM public.nt_t_flying_squad_shedule \r\n"
					+ "                    where fly_sch_year = ? \r\n"
					+ "                    and fly_sch_month = ? \r\n"
					+ "                    order by fly_sch_groupcd\r\n" + "                  )";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, year);
			stmt.setInt(2, month);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadSheduleDTO flyingSquadSheduleDTO = new FlyingSquadSheduleDTO();
				flyingSquadSheduleDTO.setGroupCd(rs.getString("code"));
				flyingSquadSheduleDTO.setGroupDes(rs.getString("group_name"));
				flyingSquadSheduleDTO.setInsert(true);
				flyingSquadSheduleDTOList.add(flyingSquadSheduleDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return flyingSquadSheduleDTOList;
	}

	// ------------------------------

	// get rosterList
	public ArrayList<RosterDTO> getRosterCd() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		ArrayList<RosterDTO> rosterList = new ArrayList<RosterDTO>(0);
		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT code\r\n" + "FROM public.nt_r_actionpoints\r\n" + "where status = 'A'\r\n";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				RosterDTO rosterDTO = new RosterDTO();
				rosterDTO.setRostercd(rs.getString("code"));
				rosterList.add(rosterDTO);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return rosterList;
	}

	// check existing records had or not
	public Integer getcount(int year, int month) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int count = 0;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select count(fly_sch_groupcd) as tol \r\n" + "from nt_t_flying_squad_shedule\r\n"
					+ "where fly_sch_year = ? \r\n" + "and fly_sch_month = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, year);
			stmt.setInt(2, month);
			rs = stmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt("tol");
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

	// Insert shedule details
	public void insertDetails(ArrayList<FlyingSquadSheduleDTO> flyingSquadSheduleDTOList, String user, int year,
			int month) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			for (FlyingSquadSheduleDTO flyingSquadSheduleDTO : flyingSquadSheduleDTOList) {

				java.util.Date date = new java.util.Date();
				Timestamp timeStamp = new Timestamp(date.getTime());

				String sql = "INSERT INTO public.nt_t_flying_squad_shedule\r\n"
						+ "(fly_sch_groupcd, fly_sch_year, fly_sch_month, fly_sch_day1, fly_sch_day2, fly_sch_day3, fly_sch_day4, fly_sch_day5, fly_sch_day6, fly_sch_day7, fly_sch_day8, fly_sch_day9, fly_sch_day10, fly_sch_day11, fly_sch_day12, fly_sch_day13, fly_sch_day14, fly_sch_day15, fly_sch_day16, fly_sch_day17, fly_sch_day18, fly_sch_day19, fly_sch_day20, fly_sch_day21, fly_sch_day22, fly_sch_day23, fly_sch_day24, fly_sch_day25, fly_sch_day26, fly_sch_day27, fly_sch_day28, fly_sch_day29, fly_sch_day30, fly_sch_day31, fly_sch_createdby, fly_sch_createddate)\r\n"
						+ "VALUES(?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?)\r\n";

				stmt = con.prepareStatement(sql);
				stmt.setString(1, flyingSquadSheduleDTO.getGroupCd());
				stmt.setInt(2, year);
				stmt.setInt(3, month);
				stmt.setString(4, flyingSquadSheduleDTO.getDay1());
				stmt.setString(5, flyingSquadSheduleDTO.getDay2());
				stmt.setString(6, flyingSquadSheduleDTO.getDay3());
				stmt.setString(7, flyingSquadSheduleDTO.getDay4());
				stmt.setString(8, flyingSquadSheduleDTO.getDay5());
				stmt.setString(9, flyingSquadSheduleDTO.getDay6());
				stmt.setString(10, flyingSquadSheduleDTO.getDay7());
				stmt.setString(11, flyingSquadSheduleDTO.getDay8());
				stmt.setString(12, flyingSquadSheduleDTO.getDay9());
				stmt.setString(13, flyingSquadSheduleDTO.getDay10());
				stmt.setString(14, flyingSquadSheduleDTO.getDay11());
				stmt.setString(15, flyingSquadSheduleDTO.getDay12());
				stmt.setString(16, flyingSquadSheduleDTO.getDay13());
				stmt.setString(17, flyingSquadSheduleDTO.getDay14());
				stmt.setString(18, flyingSquadSheduleDTO.getDay15());
				stmt.setString(19, flyingSquadSheduleDTO.getDay16());
				stmt.setString(20, flyingSquadSheduleDTO.getDay17());
				stmt.setString(21, flyingSquadSheduleDTO.getDay18());
				stmt.setString(22, flyingSquadSheduleDTO.getDay19());
				stmt.setString(23, flyingSquadSheduleDTO.getDay20());
				stmt.setString(24, flyingSquadSheduleDTO.getDay21());
				stmt.setString(25, flyingSquadSheduleDTO.getDay22());
				stmt.setString(26, flyingSquadSheduleDTO.getDay23());
				stmt.setString(27, flyingSquadSheduleDTO.getDay24());
				stmt.setString(28, flyingSquadSheduleDTO.getDay25());
				stmt.setString(29, flyingSquadSheduleDTO.getDay26());
				stmt.setString(30, flyingSquadSheduleDTO.getDay27());
				stmt.setString(31, flyingSquadSheduleDTO.getDay28());
				stmt.setString(32, flyingSquadSheduleDTO.getDay29());
				stmt.setString(33, flyingSquadSheduleDTO.getDay30());
				stmt.setString(34, flyingSquadSheduleDTO.getDay31());
				stmt.setString(35, user);
				stmt.setTimestamp(36, timeStamp);

				stmt.executeUpdate();

				con.commit();

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
	}

	// update schedule table
	public void updateShedule(ArrayList<FlyingSquadSheduleDTO> flyingSquadSheduleDTOList, String user, int year,
			int month) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			for (FlyingSquadSheduleDTO flyingSquadSheduleDTO : flyingSquadSheduleDTOList) {

				if (flyingSquadSheduleDTO.isInsert() == false) {
					java.util.Date date = new java.util.Date();
					Timestamp timeStamp = new Timestamp(date.getTime());

					String sql = "update nt_t_flying_squad_shedule\r\n"
							+ "SET  fly_sch_day1=?, fly_sch_day2=?, fly_sch_day3=?, \r\n"
							+ "fly_sch_day4=?, fly_sch_day5=?, fly_sch_day6=?, fly_sch_day7=?, fly_sch_day8=?, fly_sch_day9=?, fly_sch_day10=?,\r\n"
							+ "fly_sch_day11=?, fly_sch_day12=?, fly_sch_day13=?, fly_sch_day14=?, fly_sch_day15=?, fly_sch_day16=?, fly_sch_day17=?, \r\n"
							+ "fly_sch_day18=?, fly_sch_day19=?, fly_sch_day20=?, fly_sch_day21=?, fly_sch_day22=?, fly_sch_day23=?, fly_sch_day24=?, \r\n"
							+ "fly_sch_day25=?, fly_sch_day26=?, fly_sch_day27=?, fly_sch_day28=?, fly_sch_day29=?, fly_sch_day30=?, fly_sch_day31=?, \r\n"
							+ "fly_sch_modifiedby=?, fly_sch_modifieddate=?\r\n" + "where fly_sch_groupcd=?\r\n"
							+ "and fly_sch_year= ?\r\n" + "and fly_sch_month= ?";

					stmt = con.prepareStatement(sql);

					stmt.setString(1, flyingSquadSheduleDTO.getDay1());
					stmt.setString(2, flyingSquadSheduleDTO.getDay2());
					stmt.setString(3, flyingSquadSheduleDTO.getDay3());
					stmt.setString(4, flyingSquadSheduleDTO.getDay4());
					stmt.setString(5, flyingSquadSheduleDTO.getDay5());
					stmt.setString(6, flyingSquadSheduleDTO.getDay6());
					stmt.setString(7, flyingSquadSheduleDTO.getDay7());
					stmt.setString(8, flyingSquadSheduleDTO.getDay8());
					stmt.setString(9, flyingSquadSheduleDTO.getDay9());
					stmt.setString(10, flyingSquadSheduleDTO.getDay10());
					stmt.setString(11, flyingSquadSheduleDTO.getDay11());
					stmt.setString(12, flyingSquadSheduleDTO.getDay12());
					stmt.setString(13, flyingSquadSheduleDTO.getDay13());
					stmt.setString(14, flyingSquadSheduleDTO.getDay14());
					stmt.setString(15, flyingSquadSheduleDTO.getDay15());
					stmt.setString(16, flyingSquadSheduleDTO.getDay16());
					stmt.setString(17, flyingSquadSheduleDTO.getDay17());
					stmt.setString(18, flyingSquadSheduleDTO.getDay18());
					stmt.setString(19, flyingSquadSheduleDTO.getDay19());
					stmt.setString(20, flyingSquadSheduleDTO.getDay20());
					stmt.setString(21, flyingSquadSheduleDTO.getDay21());
					stmt.setString(22, flyingSquadSheduleDTO.getDay22());
					stmt.setString(23, flyingSquadSheduleDTO.getDay23());
					stmt.setString(24, flyingSquadSheduleDTO.getDay24());
					stmt.setString(25, flyingSquadSheduleDTO.getDay25());
					stmt.setString(26, flyingSquadSheduleDTO.getDay26());
					stmt.setString(27, flyingSquadSheduleDTO.getDay27());
					stmt.setString(28, flyingSquadSheduleDTO.getDay28());
					stmt.setString(29, flyingSquadSheduleDTO.getDay29());
					stmt.setString(30, flyingSquadSheduleDTO.getDay30());
					stmt.setString(31, flyingSquadSheduleDTO.getDay31());
					stmt.setString(32, user);
					stmt.setTimestamp(33, timeStamp);
					stmt.setString(34, flyingSquadSheduleDTO.getGroupCd());
					stmt.setInt(35, year);
					stmt.setInt(36, month);

					stmt.executeUpdate();

					con.commit();
				}

				else {

					java.util.Date date = new java.util.Date();
					Timestamp timeStamp = new Timestamp(date.getTime());

					String sql = "INSERT INTO public.nt_t_flying_squad_shedule\r\n"
							+ "(fly_sch_groupcd, fly_sch_year, fly_sch_month, fly_sch_day1, fly_sch_day2, fly_sch_day3, fly_sch_day4, fly_sch_day5, fly_sch_day6, fly_sch_day7, fly_sch_day8, fly_sch_day9, fly_sch_day10, fly_sch_day11, fly_sch_day12, fly_sch_day13, fly_sch_day14, fly_sch_day15, fly_sch_day16, fly_sch_day17, fly_sch_day18, fly_sch_day19, fly_sch_day20, fly_sch_day21, fly_sch_day22, fly_sch_day23, fly_sch_day24, fly_sch_day25, fly_sch_day26, fly_sch_day27, fly_sch_day28, fly_sch_day29, fly_sch_day30, fly_sch_day31, fly_sch_createdby, fly_sch_createddate)\r\n"
							+ "VALUES(?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?)\r\n";

					stmt = con.prepareStatement(sql);
					stmt.setString(1, flyingSquadSheduleDTO.getGroupCd());
					stmt.setInt(2, year);
					stmt.setInt(3, month);
					stmt.setString(4, flyingSquadSheduleDTO.getDay1());
					stmt.setString(5, flyingSquadSheduleDTO.getDay2());
					stmt.setString(6, flyingSquadSheduleDTO.getDay3());
					stmt.setString(7, flyingSquadSheduleDTO.getDay4());
					stmt.setString(8, flyingSquadSheduleDTO.getDay5());
					stmt.setString(9, flyingSquadSheduleDTO.getDay6());
					stmt.setString(10, flyingSquadSheduleDTO.getDay7());
					stmt.setString(11, flyingSquadSheduleDTO.getDay8());
					stmt.setString(12, flyingSquadSheduleDTO.getDay9());
					stmt.setString(13, flyingSquadSheduleDTO.getDay10());
					stmt.setString(14, flyingSquadSheduleDTO.getDay11());
					stmt.setString(15, flyingSquadSheduleDTO.getDay12());
					stmt.setString(16, flyingSquadSheduleDTO.getDay13());
					stmt.setString(17, flyingSquadSheduleDTO.getDay14());
					stmt.setString(18, flyingSquadSheduleDTO.getDay15());
					stmt.setString(19, flyingSquadSheduleDTO.getDay16());
					stmt.setString(20, flyingSquadSheduleDTO.getDay17());
					stmt.setString(21, flyingSquadSheduleDTO.getDay18());
					stmt.setString(22, flyingSquadSheduleDTO.getDay19());
					stmt.setString(23, flyingSquadSheduleDTO.getDay20());
					stmt.setString(24, flyingSquadSheduleDTO.getDay21());
					stmt.setString(25, flyingSquadSheduleDTO.getDay22());
					stmt.setString(26, flyingSquadSheduleDTO.getDay23());
					stmt.setString(27, flyingSquadSheduleDTO.getDay24());
					stmt.setString(28, flyingSquadSheduleDTO.getDay25());
					stmt.setString(29, flyingSquadSheduleDTO.getDay26());
					stmt.setString(30, flyingSquadSheduleDTO.getDay27());
					stmt.setString(31, flyingSquadSheduleDTO.getDay28());
					stmt.setString(32, flyingSquadSheduleDTO.getDay29());
					stmt.setString(33, flyingSquadSheduleDTO.getDay30());
					stmt.setString(34, flyingSquadSheduleDTO.getDay31());
					stmt.setString(35, user);
					stmt.setTimestamp(36, timeStamp);

					stmt.executeUpdate();

					con.commit();

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	public ArrayList<FlyingSquadGroupsDTO> getServiceDetails() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<FlyingSquadGroupsDTO> serviceList = new ArrayList<FlyingSquadGroupsDTO>(0);

		try {
			con = ConnectionManager.getConnection();
			stmt = con.prepareStatement(
					"SELECT code,description" + " FROM nt_r_actionpoints" + " WHERE status='A'" + "	ORDER BY code");

			rs = stmt.executeQuery();

			while (rs.next()) {
				FlyingSquadGroupsDTO dto = new FlyingSquadGroupsDTO();
				dto.setGroupCd(rs.getString("code"));
				dto.setGroupName(rs.getString("description"));
				serviceList.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return serviceList;

	}

}
