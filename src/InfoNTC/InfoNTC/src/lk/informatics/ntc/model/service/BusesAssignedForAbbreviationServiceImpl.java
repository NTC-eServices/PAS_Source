package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.RouteScheduleDTO;
import lk.informatics.ntc.model.dto.TimeTableDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class BusesAssignedForAbbreviationServiceImpl implements Serializable, BusesAssignedForAbbreviationService {

	private static final long serialVersionUID = 1L;

	@Override
	public RouteScheduleDTO retrieveStartEndDateOfTimeTableDateRange(String generatedRefNo, String groupNo,
			String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RouteScheduleDTO routeScheduleDTO = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select rs_start_date, rs_end_date FROM public.nt_m_route_schedule_generator "
					+ "where rs_generator_ref_no=? and rs_group_no=? and rs_trip_type=? ";

			ps = con.prepareStatement(query2);

			ps.setString(1, generatedRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				routeScheduleDTO = new RouteScheduleDTO();
				Date startDate = null;
				if (rs.getString("rs_start_date") != null && !rs.getString("rs_start_date").isEmpty()) {
					startDate = formatter.parse(rs.getString("rs_start_date"));
				}
				routeScheduleDTO.setStartDate(startDate);
				Date endDate = null;
				if (rs.getString("rs_end_date") != null && !rs.getString("rs_end_date").isEmpty()) {
					endDate = formatter.parse(rs.getString("rs_end_date"));
				}
				routeScheduleDTO.setEndDate(endDate);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeScheduleDTO;
	}

	@Override
	public List<RouteScheduleDTO> retrieveLeavePositionDetails(String generatedRefNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> routeScheduleList = new ArrayList<RouteScheduleDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select leave_position FROM public.nt_t_route_schedule_generator_det02 "
					+ "where generator_ref_no=? and trip_type=?";

			ps = con.prepareStatement(query);

			ps.setString(1, generatedRefNo);
			ps.setString(2, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				RouteScheduleDTO routeScheduleDTO = new RouteScheduleDTO();
				routeScheduleDTO.setLeavePosition(Integer.valueOf(rs.getString("leave_position")));
				routeScheduleList.add(routeScheduleDTO);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeScheduleList;
	}

	@Override
	public void updateBusNoInTimetableGeneratorDet(List<RouteScheduleDTO> busNoList, String loginUser,
			String generatedRefNum, String groupNum, String tripType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			for (RouteScheduleDTO dto : busNoList) {
				if (dto.getBusNo1() != null && !dto.getBusNo1().isEmpty() && dto.getFormerBusNo1() != null
						&& !dto.getFormerBusNo1().isEmpty() && !dto.getBusNo1().equals(dto.getFormerBusNo1())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo1(), dto.getBusNo1(),
							con, ps, rs);
				}
				if (dto.getBusNo2() != null && !dto.getBusNo2().isEmpty() && dto.getFormerBusNo2() != null
						&& !dto.getFormerBusNo2().isEmpty() && !dto.getBusNo2().equals(dto.getFormerBusNo2())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo2(), dto.getBusNo2(),
							con, ps, rs);
				}
				if (dto.getBusNo3() != null && !dto.getBusNo3().isEmpty() && dto.getFormerBusNo3() != null
						&& !dto.getFormerBusNo3().isEmpty() && !dto.getBusNo3().equals(dto.getFormerBusNo3())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo3(), dto.getBusNo3(),
							con, ps, rs);
				}
				if (dto.getBusNo4() != null && !dto.getBusNo4().isEmpty() && dto.getFormerBusNo4() != null
						&& !dto.getFormerBusNo4().isEmpty() && !dto.getBusNo4().equals(dto.getFormerBusNo4())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo4(), dto.getBusNo4(),
							con, ps, rs);
				}
				if (dto.getBusNo5() != null && !dto.getBusNo5().isEmpty() && dto.getFormerBusNo5() != null
						&& !dto.getFormerBusNo5().isEmpty() && !dto.getBusNo5().equals(dto.getFormerBusNo5())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo5(), dto.getBusNo5(),
							con, ps, rs);
				}
				if (dto.getBusNo6() != null && !dto.getBusNo6().isEmpty() && dto.getFormerBusNo6() != null
						&& !dto.getFormerBusNo6().isEmpty() && !dto.getBusNo6().equals(dto.getFormerBusNo6())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo6(), dto.getBusNo6(),
							con, ps, rs);
				}
				if (dto.getBusNo7() != null && !dto.getBusNo7().isEmpty() && dto.getFormerBusNo7() != null
						&& !dto.getFormerBusNo7().isEmpty() && !dto.getBusNo7().equals(dto.getFormerBusNo7())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo7(), dto.getBusNo7(),
							con, ps, rs);
				}
				if (dto.getBusNo8() != null && !dto.getBusNo8().isEmpty() && dto.getFormerBusNo8() != null
						&& !dto.getFormerBusNo8().isEmpty() && !dto.getBusNo8().equals(dto.getFormerBusNo8())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo8(), dto.getBusNo8(),
							con, ps, rs);
				}
				if (dto.getBusNo9() != null && !dto.getBusNo9().isEmpty() && dto.getFormerBusNo9() != null
						&& !dto.getFormerBusNo9().isEmpty() && !dto.getBusNo9().equals(dto.getFormerBusNo9())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo9(), dto.getBusNo9(),
							con, ps, rs);
				}
				if (dto.getBusNo10() != null && !dto.getBusNo10().isEmpty() && dto.getFormerBusNo10() != null
						&& !dto.getFormerBusNo10().isEmpty() && !dto.getBusNo10().equals(dto.getFormerBusNo10())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo10(),
							dto.getBusNo10(), con, ps, rs);
				}
				if (dto.getBusNo11() != null && !dto.getBusNo11().isEmpty() && dto.getFormerBusNo11() != null
						&& !dto.getFormerBusNo11().isEmpty() && !dto.getBusNo11().equals(dto.getFormerBusNo11())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo11(),
							dto.getBusNo11(), con, ps, rs);
				}
				if (dto.getBusNo12() != null && !dto.getBusNo12().isEmpty() && dto.getFormerBusNo12() != null
						&& !dto.getFormerBusNo12().isEmpty() && !dto.getBusNo12().equals(dto.getFormerBusNo12())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo12(),
							dto.getBusNo12(), con, ps, rs);
				}
				if (dto.getBusNo13() != null && !dto.getBusNo13().isEmpty() && dto.getFormerBusNo13() != null
						&& !dto.getFormerBusNo13().isEmpty() && !dto.getBusNo13().equals(dto.getFormerBusNo13())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo13(),
							dto.getBusNo13(), con, ps, rs);
				}
				if (dto.getBusNo14() != null && !dto.getBusNo14().isEmpty() && dto.getFormerBusNo14() != null
						&& !dto.getFormerBusNo14().isEmpty() && !dto.getBusNo14().equals(dto.getFormerBusNo14())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo14(),
							dto.getBusNo14(), con, ps, rs);
				}
				if (dto.getBusNo15() != null && !dto.getBusNo15().isEmpty() && dto.getFormerBusNo15() != null
						&& !dto.getFormerBusNo15().isEmpty() && !dto.getBusNo15().equals(dto.getFormerBusNo15())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo15(),
							dto.getBusNo15(), con, ps, rs);
				}
				if (dto.getBusNo16() != null && !dto.getBusNo16().isEmpty() && dto.getFormerBusNo16() != null
						&& !dto.getFormerBusNo16().isEmpty() && !dto.getBusNo16().equals(dto.getFormerBusNo16())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo16(),
							dto.getBusNo16(), con, ps, rs);
				}
				if (dto.getBusNo17() != null && !dto.getBusNo17().isEmpty() && dto.getFormerBusNo17() != null
						&& !dto.getFormerBusNo17().isEmpty() && !dto.getBusNo17().equals(dto.getFormerBusNo17())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo17(),
							dto.getBusNo17(), con, ps, rs);
				}
				if (dto.getBusNo18() != null && !dto.getBusNo18().isEmpty() && dto.getFormerBusNo18() != null
						&& !dto.getFormerBusNo18().isEmpty() && !dto.getBusNo18().equals(dto.getFormerBusNo18())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo18(),
							dto.getBusNo18(), con, ps, rs);
				}
				if (dto.getBusNo19() != null && !dto.getBusNo19().isEmpty() && dto.getFormerBusNo19() != null
						&& !dto.getFormerBusNo19().isEmpty() && !dto.getBusNo19().equals(dto.getFormerBusNo19())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo19(),
							dto.getBusNo19(), con, ps, rs);
				}
				if (dto.getBusNo20() != null && !dto.getBusNo20().isEmpty() && dto.getFormerBusNo20() != null
						&& !dto.getFormerBusNo20().isEmpty() && !dto.getBusNo20().equals(dto.getFormerBusNo20())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo20(),
							dto.getBusNo20(), con, ps, rs);
				}
				if (dto.getBusNo21() != null && !dto.getBusNo21().isEmpty() && dto.getFormerBusNo21() != null
						&& !dto.getFormerBusNo21().isEmpty() && !dto.getBusNo21().equals(dto.getFormerBusNo21())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo21(),
							dto.getBusNo21(), con, ps, rs);
				}
				if (dto.getBusNo22() != null && !dto.getBusNo22().isEmpty() && dto.getFormerBusNo22() != null
						&& !dto.getFormerBusNo22().isEmpty() && !dto.getBusNo22().equals(dto.getFormerBusNo22())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo22(),
							dto.getBusNo22(), con, ps, rs);
				}
				if (dto.getBusNo23() != null && !dto.getBusNo23().isEmpty() && dto.getFormerBusNo23() != null
						&& !dto.getFormerBusNo23().isEmpty() && !dto.getBusNo23().equals(dto.getFormerBusNo23())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo23(),
							dto.getBusNo23(), con, ps, rs);
				}
				if (dto.getBusNo24() != null && !dto.getBusNo24().isEmpty() && dto.getFormerBusNo24() != null
						&& !dto.getFormerBusNo24().isEmpty() && !dto.getBusNo24().equals(dto.getFormerBusNo24())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo24(),
							dto.getBusNo24(), con, ps, rs);
				}
				if (dto.getBusNo25() != null && !dto.getBusNo25().isEmpty() && dto.getFormerBusNo25() != null
						&& !dto.getFormerBusNo25().isEmpty() && !dto.getBusNo25().equals(dto.getFormerBusNo25())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo25(),
							dto.getBusNo25(), con, ps, rs);
				}
				if (dto.getBusNo26() != null && !dto.getBusNo26().isEmpty() && dto.getFormerBusNo26() != null
						&& !dto.getFormerBusNo26().isEmpty() && !dto.getBusNo26().equals(dto.getFormerBusNo26())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo26(),
							dto.getBusNo26(), con, ps, rs);
				}
				if (dto.getBusNo27() != null && !dto.getBusNo27().isEmpty() && dto.getFormerBusNo27() != null
						&& !dto.getFormerBusNo27().isEmpty() && !dto.getBusNo27().equals(dto.getFormerBusNo27())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo27(),
							dto.getBusNo27(), con, ps, rs);
				}
				if (dto.getBusNo28() != null && !dto.getBusNo28().isEmpty() && dto.getFormerBusNo28() != null
						&& !dto.getFormerBusNo28().isEmpty() && !dto.getBusNo28().equals(dto.getFormerBusNo28())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo28(),
							dto.getBusNo28(), con, ps, rs);
				}
				if (dto.getBusNo29() != null && !dto.getBusNo29().isEmpty() && dto.getFormerBusNo29() != null
						&& !dto.getFormerBusNo29().isEmpty() && !dto.getBusNo29().equals(dto.getFormerBusNo29())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo29(),
							dto.getBusNo29(), con, ps, rs);
				}
				if (dto.getBusNo30() != null && !dto.getBusNo30().isEmpty() && dto.getFormerBusNo30() != null
						&& !dto.getFormerBusNo30().isEmpty() && !dto.getBusNo30().equals(dto.getFormerBusNo30())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo30(),
							dto.getBusNo30(), con, ps, rs);
				}
				if (dto.getBusNo31() != null && !dto.getBusNo31().isEmpty() && dto.getFormerBusNo31() != null
						&& !dto.getFormerBusNo31().isEmpty() && !dto.getBusNo31().equals(dto.getFormerBusNo31())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo31(),
							dto.getBusNo31(), con, ps, rs);
				}
				if (dto.getBusNo32() != null && !dto.getBusNo32().isEmpty() && dto.getFormerBusNo32() != null
						&& !dto.getFormerBusNo32().isEmpty() && !dto.getBusNo32().equals(dto.getFormerBusNo32())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo32(),
							dto.getBusNo32(), con, ps, rs);
				}
				if (dto.getBusNo33() != null && !dto.getBusNo33().isEmpty() && dto.getFormerBusNo33() != null
						&& !dto.getFormerBusNo33().isEmpty() && !dto.getBusNo33().equals(dto.getFormerBusNo33())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo33(),
							dto.getBusNo33(), con, ps, rs);
				}
				if (dto.getBusNo34() != null && !dto.getBusNo34().isEmpty() && dto.getFormerBusNo34() != null
						&& !dto.getFormerBusNo34().isEmpty() && !dto.getBusNo34().equals(dto.getFormerBusNo34())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo34(),
							dto.getBusNo34(), con, ps, rs);
				}
				if (dto.getBusNo35() != null && !dto.getBusNo35().isEmpty() && dto.getFormerBusNo35() != null
						&& !dto.getFormerBusNo35().isEmpty() && !dto.getBusNo35().equals(dto.getFormerBusNo35())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo35(),
							dto.getBusNo35(), con, ps, rs);
				}
				if (dto.getBusNo36() != null && !dto.getBusNo36().isEmpty() && dto.getFormerBusNo36() != null
						&& !dto.getFormerBusNo36().isEmpty() && !dto.getBusNo36().equals(dto.getFormerBusNo36())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo36(),
							dto.getBusNo36(), con, ps, rs);
				}
				if (dto.getBusNo37() != null && !dto.getBusNo37().isEmpty() && dto.getFormerBusNo37() != null
						&& !dto.getFormerBusNo37().isEmpty() && !dto.getBusNo37().equals(dto.getFormerBusNo37())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo37(),
							dto.getBusNo37(), con, ps, rs);
				}
				if (dto.getBusNo38() != null && !dto.getBusNo38().isEmpty() && dto.getFormerBusNo38() != null
						&& !dto.getFormerBusNo38().isEmpty() && !dto.getBusNo38().equals(dto.getFormerBusNo38())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo38(),
							dto.getBusNo38(), con, ps, rs);
				}
				if (dto.getBusNo39() != null && !dto.getBusNo39().isEmpty() && dto.getFormerBusNo39() != null
						&& !dto.getFormerBusNo39().isEmpty() && !dto.getBusNo39().equals(dto.getFormerBusNo39())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo39(),
							dto.getBusNo39(), con, ps, rs);
				}
				if (dto.getBusNo40() != null && !dto.getBusNo40().isEmpty() && dto.getFormerBusNo40() != null
						&& !dto.getFormerBusNo40().isEmpty() && !dto.getBusNo40().equals(dto.getFormerBusNo40())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo40(),
							dto.getBusNo40(), con, ps, rs);
				}
				if (dto.getBusNo41() != null && !dto.getBusNo41().isEmpty() && dto.getFormerBusNo41() != null
						&& !dto.getFormerBusNo41().isEmpty() && !dto.getBusNo41().equals(dto.getFormerBusNo41())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo41(),
							dto.getBusNo41(), con, ps, rs);
				}
				if (dto.getBusNo42() != null && !dto.getBusNo42().isEmpty() && dto.getFormerBusNo42() != null
						&& !dto.getFormerBusNo42().isEmpty() && !dto.getBusNo42().equals(dto.getFormerBusNo42())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo42(),
							dto.getBusNo42(), con, ps, rs);
				}
				if (dto.getBusNo43() != null && !dto.getBusNo43().isEmpty() && dto.getFormerBusNo43() != null
						&& !dto.getFormerBusNo43().isEmpty() && !dto.getBusNo43().equals(dto.getFormerBusNo43())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo43(),
							dto.getBusNo43(), con, ps, rs);
				}
				if (dto.getBusNo44() != null && !dto.getBusNo44().isEmpty() && dto.getFormerBusNo44() != null
						&& !dto.getFormerBusNo44().isEmpty() && !dto.getBusNo44().equals(dto.getFormerBusNo44())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo44(),
							dto.getBusNo44(), con, ps, rs);
				}
				if (dto.getBusNo45() != null && !dto.getBusNo45().isEmpty() && dto.getFormerBusNo45() != null
						&& !dto.getFormerBusNo45().isEmpty() && !dto.getBusNo45().equals(dto.getFormerBusNo45())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo45(),
							dto.getBusNo45(), con, ps, rs);
				}
				if (dto.getBusNo46() != null && !dto.getBusNo46().isEmpty() && dto.getFormerBusNo46() != null
						&& !dto.getFormerBusNo46().isEmpty() && !dto.getBusNo46().equals(dto.getFormerBusNo46())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo46(),
							dto.getBusNo46(), con, ps, rs);
				}
				if (dto.getBusNo47() != null && !dto.getBusNo47().isEmpty() && dto.getFormerBusNo47() != null
						&& !dto.getFormerBusNo47().isEmpty() && !dto.getBusNo47().equals(dto.getFormerBusNo47())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo47(),
							dto.getBusNo47(), con, ps, rs);
				}
				if (dto.getBusNo48() != null && !dto.getBusNo48().isEmpty() && dto.getFormerBusNo48() != null
						&& !dto.getFormerBusNo48().isEmpty() && !dto.getBusNo48().equals(dto.getFormerBusNo48())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo48(),
							dto.getBusNo48(), con, ps, rs);
				}
				if (dto.getBusNo49() != null && !dto.getBusNo49().isEmpty() && dto.getFormerBusNo49() != null
						&& !dto.getFormerBusNo49().isEmpty() && !dto.getBusNo49().equals(dto.getFormerBusNo49())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo49(),
							dto.getBusNo49(), con, ps, rs);
				}
				if (dto.getBusNo50() != null && !dto.getBusNo50().isEmpty() && dto.getFormerBusNo50() != null
						&& !dto.getFormerBusNo50().isEmpty() && !dto.getBusNo50().equals(dto.getFormerBusNo50())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo50(),
							dto.getBusNo50(), con, ps, rs);
				}
				if (dto.getBusNo51() != null && !dto.getBusNo51().isEmpty() && dto.getFormerBusNo51() != null
						&& !dto.getFormerBusNo51().isEmpty() && !dto.getBusNo51().equals(dto.getFormerBusNo51())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo51(),
							dto.getBusNo51(), con, ps, rs);
				}
				if (dto.getBusNo52() != null && !dto.getBusNo52().isEmpty() && dto.getFormerBusNo52() != null
						&& !dto.getFormerBusNo52().isEmpty() && !dto.getBusNo52().equals(dto.getFormerBusNo52())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo52(),
							dto.getBusNo52(), con, ps, rs);
				}
				if (dto.getBusNo53() != null && !dto.getBusNo53().isEmpty() && dto.getFormerBusNo53() != null
						&& !dto.getFormerBusNo53().isEmpty() && !dto.getBusNo53().equals(dto.getFormerBusNo53())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo53(),
							dto.getBusNo53(), con, ps, rs);
				}
				if (dto.getBusNo54() != null && !dto.getBusNo54().isEmpty() && dto.getFormerBusNo54() != null
						&& !dto.getFormerBusNo54().isEmpty() && !dto.getBusNo54().equals(dto.getFormerBusNo54())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo54(),
							dto.getBusNo54(), con, ps, rs);
				}
				if (dto.getBusNo55() != null && !dto.getBusNo55().isEmpty() && dto.getFormerBusNo55() != null
						&& !dto.getFormerBusNo55().isEmpty() && !dto.getBusNo55().equals(dto.getFormerBusNo55())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo55(),
							dto.getBusNo55(), con, ps, rs);
				}
				if (dto.getBusNo56() != null && !dto.getBusNo56().isEmpty() && dto.getFormerBusNo56() != null
						&& !dto.getFormerBusNo56().isEmpty() && !dto.getBusNo56().equals(dto.getFormerBusNo56())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo56(),
							dto.getBusNo56(), con, ps, rs);
				}
				if (dto.getBusNo57() != null && !dto.getBusNo57().isEmpty() && dto.getFormerBusNo57() != null
						&& !dto.getFormerBusNo57().isEmpty() && !dto.getBusNo57().equals(dto.getFormerBusNo57())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo57(),
							dto.getBusNo57(), con, ps, rs);
				}
				if (dto.getBusNo58() != null && !dto.getBusNo58().isEmpty() && dto.getFormerBusNo58() != null
						&& !dto.getFormerBusNo58().isEmpty() && !dto.getBusNo58().equals(dto.getFormerBusNo58())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo58(),
							dto.getBusNo58(), con, ps, rs);
				}
				if (dto.getBusNo59() != null && !dto.getBusNo59().isEmpty() && dto.getFormerBusNo59() != null
						&& !dto.getFormerBusNo59().isEmpty() && !dto.getBusNo59().equals(dto.getFormerBusNo59())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo59(),
							dto.getBusNo59(), con, ps, rs);
				}
				if (dto.getBusNo60() != null && !dto.getBusNo60().isEmpty() && dto.getFormerBusNo60() != null
						&& !dto.getFormerBusNo60().isEmpty() && !dto.getBusNo60().equals(dto.getFormerBusNo60())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo60(),
							dto.getBusNo60(), con, ps, rs);
				}
				if (dto.getBusNo61() != null && !dto.getBusNo61().isEmpty() && dto.getFormerBusNo61() != null
						&& !dto.getFormerBusNo61().isEmpty() && !dto.getBusNo6().equals(dto.getFormerBusNo61())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo61(),
							dto.getBusNo61(), con, ps, rs);
				}
				if (dto.getBusNo62() != null && !dto.getBusNo62().isEmpty() && dto.getFormerBusNo62() != null
						&& !dto.getFormerBusNo62().isEmpty() && !dto.getBusNo62().equals(dto.getFormerBusNo62())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo62(),
							dto.getBusNo62(), con, ps, rs);
				}
				if (dto.getBusNo63() != null && !dto.getBusNo63().isEmpty() && dto.getFormerBusNo63() != null
						&& !dto.getFormerBusNo63().isEmpty() && !dto.getBusNo63().equals(dto.getFormerBusNo63())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo63(),
							dto.getBusNo63(), con, ps, rs);
				}
				if (dto.getBusNo64() != null && !dto.getBusNo64().isEmpty() && dto.getFormerBusNo64() != null
						&& !dto.getFormerBusNo64().isEmpty() && !dto.getBusNo64().equals(dto.getFormerBusNo64())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo64(),
							dto.getBusNo64(), con, ps, rs);
				}
				if (dto.getBusNo65() != null && !dto.getBusNo65().isEmpty() && dto.getFormerBusNo65() != null
						&& !dto.getFormerBusNo65().isEmpty() && !dto.getBusNo65().equals(dto.getFormerBusNo65())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo65(),
							dto.getBusNo65(), con, ps, rs);
				}
				if (dto.getBusNo66() != null && !dto.getBusNo66().isEmpty() && dto.getFormerBusNo66() != null
						&& !dto.getFormerBusNo66().isEmpty() && !dto.getBusNo66().equals(dto.getFormerBusNo66())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo66(),
							dto.getBusNo66(), con, ps, rs);
				}
				if (dto.getBusNo67() != null && !dto.getBusNo67().isEmpty() && dto.getFormerBusNo67() != null
						&& !dto.getFormerBusNo67().isEmpty() && !dto.getBusNo67().equals(dto.getFormerBusNo67())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo67(),
							dto.getBusNo67(), con, ps, rs);
				}
				if (dto.getBusNo68() != null && !dto.getBusNo68().isEmpty() && dto.getFormerBusNo68() != null
						&& !dto.getFormerBusNo68().isEmpty() && !dto.getBusNo68().equals(dto.getFormerBusNo68())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo68(),
							dto.getBusNo68(), con, ps, rs);
				}
				if (dto.getBusNo69() != null && !dto.getBusNo69().isEmpty() && dto.getFormerBusNo69() != null
						&& !dto.getFormerBusNo69().isEmpty() && !dto.getBusNo69().equals(dto.getFormerBusNo69())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo69(),
							dto.getBusNo69(), con, ps, rs);
				}
				if (dto.getBusNo70() != null && !dto.getBusNo70().isEmpty() && dto.getFormerBusNo70() != null
						&& !dto.getFormerBusNo70().isEmpty() && !dto.getBusNo70().equals(dto.getFormerBusNo70())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo70(),
							dto.getBusNo70(), con, ps, rs);
				}
				if (dto.getBusNo71() != null && !dto.getBusNo71().isEmpty() && dto.getFormerBusNo71() != null
						&& !dto.getFormerBusNo71().isEmpty() && !dto.getBusNo71().equals(dto.getFormerBusNo71())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo71(),
							dto.getBusNo71(), con, ps, rs);
				}
				if (dto.getBusNo72() != null && !dto.getBusNo72().isEmpty() && dto.getFormerBusNo72() != null
						&& !dto.getFormerBusNo72().isEmpty() && !dto.getBusNo72().equals(dto.getFormerBusNo72())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo72(),
							dto.getBusNo72(), con, ps, rs);
				}
				if (dto.getBusNo73() != null && !dto.getBusNo73().isEmpty() && dto.getFormerBusNo73() != null
						&& !dto.getFormerBusNo73().isEmpty() && !dto.getBusNo73().equals(dto.getFormerBusNo73())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo73(),
							dto.getBusNo73(), con, ps, rs);
				}
				if (dto.getBusNo74() != null && !dto.getBusNo74().isEmpty() && dto.getFormerBusNo74() != null
						&& !dto.getFormerBusNo74().isEmpty() && !dto.getBusNo74().equals(dto.getFormerBusNo74())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo74(),
							dto.getBusNo74(), con, ps, rs);
				}
				if (dto.getBusNo75() != null && !dto.getBusNo75().isEmpty() && dto.getFormerBusNo75() != null
						&& !dto.getFormerBusNo75().isEmpty() && !dto.getBusNo75().equals(dto.getFormerBusNo75())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo75(),
							dto.getBusNo75(), con, ps, rs);
				}
				if (dto.getBusNo76() != null && !dto.getBusNo76().isEmpty() && dto.getFormerBusNo76() != null
						&& !dto.getFormerBusNo76().isEmpty() && !dto.getBusNo76().equals(dto.getFormerBusNo76())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo76(),
							dto.getBusNo76(), con, ps, rs);
				}
				if (dto.getBusNo77() != null && !dto.getBusNo77().isEmpty() && dto.getFormerBusNo77() != null
						&& !dto.getFormerBusNo77().isEmpty() && !dto.getBusNo77().equals(dto.getFormerBusNo77())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo77(),
							dto.getBusNo77(), con, ps, rs);
				}
				if (dto.getBusNo78() != null && !dto.getBusNo78().isEmpty() && dto.getFormerBusNo78() != null
						&& !dto.getFormerBusNo78().isEmpty() && !dto.getBusNo78().equals(dto.getFormerBusNo78())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo78(),
							dto.getBusNo78(), con, ps, rs);
				}
				if (dto.getBusNo79() != null && !dto.getBusNo79().isEmpty() && dto.getFormerBusNo79() != null
						&& !dto.getFormerBusNo79().isEmpty() && !dto.getBusNo79().equals(dto.getFormerBusNo79())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo79(),
							dto.getBusNo79(), con, ps, rs);
				}
				if (dto.getBusNo80() != null && !dto.getBusNo80().isEmpty() && dto.getFormerBusNo80() != null
						&& !dto.getFormerBusNo80().isEmpty() && !dto.getBusNo80().equals(dto.getFormerBusNo80())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo80(),
							dto.getBusNo80(), con, ps, rs);
				}
				if (dto.getBusNo81() != null && !dto.getBusNo81().isEmpty() && dto.getFormerBusNo81() != null
						&& !dto.getFormerBusNo81().isEmpty() && !dto.getBusNo81().equals(dto.getFormerBusNo81())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo81(),
							dto.getBusNo81(), con, ps, rs);
				}
				if (dto.getBusNo82() != null && !dto.getBusNo82().isEmpty() && dto.getFormerBusNo82() != null
						&& !dto.getFormerBusNo82().isEmpty() && !dto.getBusNo82().equals(dto.getFormerBusNo82())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo82(),
							dto.getBusNo82(), con, ps, rs);
				}
				if (dto.getBusNo83() != null && !dto.getBusNo83().isEmpty() && dto.getFormerBusNo83() != null
						&& !dto.getFormerBusNo83().isEmpty() && !dto.getBusNo83().equals(dto.getFormerBusNo83())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo83(),
							dto.getBusNo83(), con, ps, rs);
				}
				if (dto.getBusNo84() != null && !dto.getBusNo84().isEmpty() && dto.getFormerBusNo84() != null
						&& !dto.getFormerBusNo84().isEmpty() && !dto.getBusNo84().equals(dto.getFormerBusNo84())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo84(),
							dto.getBusNo84(), con, ps, rs);
				}
				if (dto.getBusNo85() != null && !dto.getBusNo85().isEmpty() && dto.getFormerBusNo85() != null
						&& !dto.getFormerBusNo85().isEmpty() && !dto.getBusNo85().equals(dto.getFormerBusNo85())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo85(),
							dto.getBusNo85(), con, ps, rs);
				}
				if (dto.getBusNo86() != null && !dto.getBusNo86().isEmpty() && dto.getFormerBusNo86() != null
						&& !dto.getFormerBusNo86().isEmpty() && !dto.getBusNo86().equals(dto.getFormerBusNo86())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo86(),
							dto.getBusNo86(), con, ps, rs);
				}
				if (dto.getBusNo87() != null && !dto.getBusNo87().isEmpty() && dto.getFormerBusNo87() != null
						&& !dto.getFormerBusNo87().isEmpty() && !dto.getBusNo87().equals(dto.getFormerBusNo87())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo87(),
							dto.getBusNo87(), con, ps, rs);
				}
				if (dto.getBusNo88() != null && !dto.getBusNo88().isEmpty() && dto.getFormerBusNo88() != null
						&& !dto.getFormerBusNo88().isEmpty() && !dto.getBusNo88().equals(dto.getFormerBusNo88())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo88(),
							dto.getBusNo88(), con, ps, rs);
				}
				if (dto.getBusNo89() != null && !dto.getBusNo89().isEmpty() && dto.getFormerBusNo89() != null
						&& !dto.getFormerBusNo89().isEmpty() && !dto.getBusNo89().equals(dto.getFormerBusNo89())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo89(),
							dto.getBusNo89(), con, ps, rs);
				}
				if (dto.getBusNo90() != null && !dto.getBusNo90().isEmpty() && dto.getFormerBusNo90() != null
						&& !dto.getFormerBusNo90().isEmpty() && !dto.getBusNo90().equals(dto.getFormerBusNo90())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo90(),
							dto.getBusNo90(), con, ps, rs);
				}
				if (dto.getBusNo91() != null && !dto.getBusNo91().isEmpty() && dto.getFormerBusNo91() != null
						&& !dto.getFormerBusNo91().isEmpty() && !dto.getBusNo91().equals(dto.getFormerBusNo91())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo91(),
							dto.getBusNo91(), con, ps, rs);
				}
				if (dto.getBusNo92() != null && !dto.getBusNo92().isEmpty() && dto.getFormerBusNo92() != null
						&& !dto.getFormerBusNo92().isEmpty() && !dto.getBusNo92().equals(dto.getFormerBusNo92())) {

					updateQuery(loginUser, generatedRefNum, groupNum, tripType, dto.getFormerBusNo92(),
							dto.getBusNo92(), con, ps, rs);
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
	}

	public void updateQuery(String loginUser, String generatedRefNum, String groupNum, String tripType,
			String formerBus, String busNo, Connection con, PreparedStatement ps, ResultSet rs) throws SQLException {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean busNumCol = false;

		/** check whether bus num in bus num colum or assigned bus num col **/

		String select = "SELECT * FROM public.nt_m_timetable_generator_det WHERE generator_ref_no=? and bus_num=? and group_no=? ";
		ps = con.prepareStatement(select);

		ps.setString(1, generatedRefNum);
		ps.setString(2, formerBus);
		ps.setString(3, groupNum);

		rs = ps.executeQuery();
		while (rs.next()) {
			busNumCol = true;
		}
		/** check whether bus num in bus num colum or assigned bus bum col **/
		String where = "";
		if (busNumCol) {
			where = " and bus_num='" + formerBus + "'";
		} else {
			where = " and assigned_bus_no='" + formerBus + "'";
		}

		String query = "UPDATE public.nt_m_timetable_generator_det SET assigned_bus_no=?, modify_by=?, modify_date=? "
				+ "WHERE generator_ref_no=? and group_no=? " + where;

		ps = con.prepareStatement(query);

		ps.setString(1, busNo);
		ps.setString(2, loginUser);
		ps.setTimestamp(3, timestamp);
		ps.setString(4, generatedRefNum);
		ps.setString(5, groupNum);

		ps.executeUpdate();

		/** insert into history table start **/
		TimeTableDTO dataTO = new TimeTableDTO();
		String fixedTime = null;
		String pvtBus = null;

		String selectQ = "SELECT * FROM public.nt_m_timetable_generator_det WHERE generator_ref_no=? and bus_num=? and group_no=?"
				+ where;
		ps = con.prepareStatement(selectQ);

		ps.setString(1, generatedRefNum);
		ps.setString(2, formerBus);
		ps.setString(3, groupNum);

		rs = ps.executeQuery();
		while (rs.next()) {
			dataTO.setMasterSeq(rs.getInt("seq_no"));
			dataTO.setStartTime(rs.getString("start_time_slot"));
			dataTO.setEndTime(rs.getString("end_time_slot"));
			dataTO.setTripId(rs.getString("trip_id"));
			fixedTime = rs.getString("is_fixed_time");
			pvtBus = rs.getString("is_pvt_bus");
		}

		long seqNo = Utils.getNextValBySeqName(con, "seq_nt_h_bus_assign_for_abbreviation");

		String queryh = "INSERT INTO public.nt_h_bus_assign_for_abbreviation "
				+ "(seq_no, ref_seq_no, generator_ref_no, bus_num, start_time_slot, end_time_slot, assigned_bus_no, trip_id, "
				+ " is_fixed_time, group_no, trip_type, created_by, created_date, is_pvt_bus) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" + " ";

		ps = con.prepareStatement(queryh);

		ps.setLong(1, seqNo);
		ps.setLong(2, dataTO.getMasterSeq());
		ps.setString(3, generatedRefNum);
		ps.setString(4, formerBus);
		ps.setString(5, dataTO.getStartTime());
		ps.setString(6, dataTO.getEndTime());
		ps.setString(7, busNo);
		if (dataTO.getTripId() != null && !dataTO.getTripId().isEmpty()
				&& !dataTO.getTripId().trim().equalsIgnoreCase("")) {
			ps.setInt(8, Integer.valueOf(dataTO.getTripId()));
		} else {
			ps.setNull(8, java.sql.Types.INTEGER);
		}

		ps.setString(9, fixedTime);
		ps.setString(10, groupNum);
		ps.setString(11, tripType);
		ps.setString(12, loginUser);
		ps.setTimestamp(13, timestamp);
		ps.setString(14, pvtBus);

		ps.executeUpdate();
		/** insert into history table end **/

	}

	@Override
	public List<RouteScheduleDTO> getBusNoList(String routeNo, String refNo, String groupNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT distinct bus_num, assigned_bus_no,  generator_ref_no, group_no, trip_type FROM public.nt_m_timetable_generator_det "
					+ "where generator_ref_no=? and group_no=? and trip_type=? and is_fixed_time='N'and "
					+ "bus_num is not null and bus_num !='' order by bus_num ";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			for (int i = 0; rs.next(); i++) {

				e = new RouteScheduleDTO();
				String id = String.valueOf(i + 1);
				e.setTripId(id);

				if (rs.getString("assigned_bus_no") != null && !rs.getString("assigned_bus_no").isEmpty()
						&& !rs.getString("assigned_bus_no").trim().equals("")) {
					e.setBusNo(rs.getString("assigned_bus_no"));
				} else {
					e.setBusNo(rs.getString("bus_num"));
				}

				busNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public String retrieveOriginDestinationSwap(String routeNo, String generatedRefNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String swap = null;

		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT rs_is_swap FROM public.nt_m_route_schedule_generator where rs_generator_ref_no=? and rs_route_no=?";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, routeNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				swap = rs.getString("rs_is_swap");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return swap;
	}

	@Override
	public List<RouteScheduleDTO> getBusNoListWithSelectedBuses(Object object, String generatedRefNo, String groupNo,
			String tripType, String routeNum, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		String originAbbr = null;
		String destAbbr = null;
		int privateBusOrigin = 0;
		int privateBusDestination = 0;
		int seqMaster = 0;
		String permitExpireDate = "";

		String busNumLike = null;
		try {
			con = ConnectionManager.getConnection();

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNum);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';
			}
			String query2 = "SELECT distinct bus_num, assigned_bus_no, generator_ref_no, group_no, trip_type, seq_master FROM public.nt_m_timetable_generator_det "
					+ "where generator_ref_no=? and group_no=? and trip_type=? and is_fixed_time='N'and "
					+ "bus_num is not null and bus_num !='' and  bus_num like " + "'" + busNumLike + "' "
					+ " order by bus_num  ";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			for (int i = 0; rs.next(); i++) {

				e = new RouteScheduleDTO();
				String id = String.valueOf(i + 1);
				e.setTripId(id);

				e.setBusNo(rs.getString("bus_num"));
				if (rs.getString("assigned_bus_no") != null && !rs.getString("assigned_bus_no").isEmpty()
						&& !rs.getString("assigned_bus_no").trim().equals("")) {
					permitExpireDate = getExpireDate(con, rs.getString("assigned_bus_no"), routeNum, busCategory,
							tripType);
					if (permitExpireDate != null && !permitExpireDate.isEmpty()
							&& !permitExpireDate.trim().equals("")) {
						e.setSelectedBusNum(rs.getString("assigned_bus_no") + "-" + permitExpireDate);
					} else {
						e.setSelectedBusNum(rs.getString("assigned_bus_no"));
					}

				}
				e.setCurrentSelectedBusNo(rs.getString("assigned_bus_no"));

				seqMaster = rs.getInt("seq_master");

				busNoList.add(e);
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// get private buses for origin start
			if (tripType != null && !tripType.isEmpty() && tripType.equalsIgnoreCase("O")) {
				String originPVTQuery = "select a.no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
						+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
						+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

				ps = con.prepareStatement(originPVTQuery);
				ps.setString(1, generatedRefNo);
				ps.setString(2, groupNo);
				ps.setString(3, "O");
				rs = ps.executeQuery();

				while (rs.next()) {
					privateBusOrigin = rs.getInt("no_of_pvt_bus");
				}

				try {
					if (rs != null)
						rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// get private buses for origin end

			// get private buses for destination start
			if (tripType != null && !tripType.isEmpty() && tripType.equalsIgnoreCase("D")) {
				String destinationPVTQuery = "select a.no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
						+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
						+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

				ps = con.prepareStatement(destinationPVTQuery);
				ps.setString(1, generatedRefNo);
				ps.setString(2, groupNo);
				ps.setString(3, "D");
				rs = ps.executeQuery();

				while (rs.next()) {
					privateBusDestination = rs.getInt("no_of_pvt_bus");
				}

				try {
					if (rs != null)
						rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			// get private buses for destination end
			
			// get fixed bus numbers start
			List<String> fixedBusNoList = new ArrayList<String>();
			String fixedBusNoQuery = "SELECT distinct bus_num FROM public.nt_m_timetable_generator_det\r\n"
					+ "WHERE generator_ref_no=? and group_no=? and is_fixed_time=?;";

			ps = con.prepareStatement(fixedBusNoQuery);
			ps.setString(1, generatedRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, "Y");
			rs = ps.executeQuery();

			while (rs.next()) {
				fixedBusNoList.add(rs.getString("bus_num"));
			}
			
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			// get fixed bus numbers end

			/** create temp origin and destination bus numbers with leave buses start **/
			// origin buses
			List<String> originBusList = new ArrayList<String>();
			for (int i = 0; i < privateBusOrigin; i++) {
				int count = i + 1;
				if(!fixedBusNoList.contains(originAbbr + count)) {
					originBusList.add(originAbbr + count);
				}
			}

			// destination buses
			List<String> destBusList = new ArrayList<String>();
			for (int i = 0; i < privateBusDestination; i++) {
				int count = i + 1;
				if(!fixedBusNoList.contains(destAbbr + count)) {
					destBusList.add(destAbbr + count);
				}
			}
			/** create temp origin and destination bus numbers with leave buses end **/

			// add both lists together start
			List<String> tempAllBusNums = new ArrayList<String>();
			if (originBusList != null && originBusList.size() != 0) {
				tempAllBusNums.addAll(originBusList);
			}
			if (destBusList != null && destBusList.size() != 0) {
				tempAllBusNums.addAll(destBusList);
			}
			// add both lists together end

			/** add missing busses to main bus list start **/
			int tempCount = 0;
			List<RouteScheduleDTO> missingBusses = new ArrayList<RouteScheduleDTO>();
			List<String> leaveBuses = new ArrayList<String>();
			boolean found = false;
			for (String s : tempAllBusNums) {
				found = false;
				for (RouteScheduleDTO dto : busNoList) {
					if (s.equals(dto.getBusNo())) {
						found = true;
						if (dto.getAssignedBusNo() != null && !dto.getAssignedBusNo().isEmpty()) {
							dto.setBusNo(dto.getAssignedBusNo());
							missingBusses.add(dto);
						} else {
							missingBusses.add(dto);
						}

						break;
					}
				}
				if (!found) {
					e = new RouteScheduleDTO();
					tempCount = tempCount + 1;
					String id = String.valueOf(tempCount);
					e.setTripId(id);
					e.setBusNo(s);
					missingBusses.add(e);
					leaveBuses.add(s);
				}
			}

			busNoList = new ArrayList<RouteScheduleDTO>();
			busNoList = missingBusses;

			/** add missing busses to main bus list end **/

			// insert leave buses to nt_m_timetable_generator_det start
			for (String s : leaveBuses) {
				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_timetable_generator_det");

				String sql = "INSERT INTO public.nt_m_timetable_generator_det(seq_no,seq_master,generator_ref_no,group_no,trip_type,is_fixed_time,bus_num) VALUES (?,?,?,?,?,?,?)";

				ps = con.prepareStatement(sql);

				ps.setLong(1, seqNo);
				ps.setInt(2, seqMaster);
				ps.setString(3, generatedRefNo);
				ps.setString(4, groupNo);
				ps.setString(5, tripType);
				ps.setString(6, "N");
				ps.setString(7, s);

				ps.executeUpdate();
			}
			con.commit();
			// insert leave buses to nt_m_timetable_generator_det end

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public List<RouteScheduleDTO> getAssignedBuses(Object object, String generatedRefNo, String groupNo,
			String tripType, String routeNum, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		String permitExpireDate = "";

		String originAbbr = null;
		String destAbbr = null;
		String busNumLike = null;
		try {
			con = ConnectionManager.getConnection();

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps1 = con.prepareStatement(abbrQuery);
			ps1.setString(1, routeNum);
			ps1.setString(2, busCategory);
			rs1 = ps1.executeQuery();

			while (rs1.next()) {
				originAbbr = rs1.getString("rc_abbriviation_ltr_start");
				destAbbr = rs1.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps1 != null)
					ps1.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			if (tripType.equalsIgnoreCase("O")) {
				busNumLike = originAbbr + '%';

			} else {
				busNumLike = destAbbr + '%';
			}

			String query2 = "SELECT distinct bus_num, assigned_bus_no, generator_ref_no, group_no, trip_type, seq_master FROM public.nt_m_timetable_generator_det "
					+ "where generator_ref_no=? and group_no=? and trip_type=? and is_fixed_time='N' and assigned_bus_no is not null and "
					+ "bus_num is not null and bus_num !=''  and  bus_num like " + "'" + busNumLike + "' "
					+ "order by bus_num ";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			for (int i = 0; rs.next(); i++) {

				e = new RouteScheduleDTO();
				String id = String.valueOf(i + 1);
				e.setTripId(id);

				e.setBusNo(rs.getString("bus_num"));
				if (rs.getString("assigned_bus_no") != null && !rs.getString("assigned_bus_no").isEmpty()
						&& !rs.getString("assigned_bus_no").trim().equals("")) {
					permitExpireDate = getExpireDate(con, rs.getString("assigned_bus_no"), routeNum, busCategory,
							tripType);
					if (permitExpireDate != null && !permitExpireDate.isEmpty()
							&& !permitExpireDate.trim().equals("")) {
						e.setSelectedBusNum(rs.getString("assigned_bus_no") + "-" + permitExpireDate);
					} else {
						e.setSelectedBusNum(rs.getString("assigned_bus_no"));
					}
				}

				e.setCurrentSelectedBusNo(rs.getString("assigned_bus_no"));

				busNoList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	private String getExpireDate(Connection con, String vehicleNum, String routeNum, String serviceType,
			String tripType) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String permitExpireDate = "";
		try {

			// y- d to o and n - o to d

			String query2 = "select pm_permit_expire_date from public.nt_t_pm_application where pm_vehicle_regno=? and pm_status='A' and pm_route_no=? and pm_service_type=? ";

			ps = con.prepareStatement(query2);

			ps.setString(1, vehicleNum);
			ps.setString(2, routeNum);
			ps.setString(3, serviceType);

			rs = ps.executeQuery();

			while (rs.next()) {
				permitExpireDate = rs.getString("pm_permit_expire_date");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
		}

		return permitExpireDate;
	}

	@Override
	public RouteScheduleDTO retrieveStartEndDateOfLastPanelDateRange(String generatedRefNo, String groupNo,
			String routeNo, String tripType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		RouteScheduleDTO routeScheduleDTO = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			con = ConnectionManager.getConnection();

			String query2 = "select rs_start_date, rs_end_date FROM public.nt_m_route_schedule_generator "
					+ "where rs_generator_ref_no=? and rs_group_no=? and rs_route_no=? and rs_trip_type=?";

			ps = con.prepareStatement(query2);

			ps.setString(1, generatedRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, routeNo);
			ps.setString(4, tripType);

			rs = ps.executeQuery();

			while (rs.next()) {
				routeScheduleDTO = new RouteScheduleDTO();
				Date startDate = null;
				if (rs.getString("rs_start_date") != null && !rs.getString("rs_start_date").isEmpty()) {
					startDate = formatter.parse(rs.getString("rs_start_date"));
				}
				routeScheduleDTO.setStartDate(startDate);
				Date endDate = null;
				if (rs.getString("rs_end_date") != null && !rs.getString("rs_end_date").isEmpty()) {
					endDate = formatter.parse(rs.getString("rs_end_date"));
				}
				routeScheduleDTO.setEndDate(endDate);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return routeScheduleDTO;
	}

	@Override
	public List<RouteScheduleDTO> getBusNoListWithoutCTBBuses(String routeNo, String refNo, String groupNo,
			String tripType, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		int privateBusOrigin = 0;
		int privateBusDestination = 0;
		String originAbbr = null;
		String destAbbr = null;
		int tempCount = 0;
		try {
			con = ConnectionManager.getConnection();

			// get abbreviation for origin and destination start
			String abbrQuery = "SELECT rc_abbriviation_ltr_start, rc_abbriviation_ltr_end FROM public.nt_t_route_creator "
					+ "where rc_route_no=? and rc_bus_type=?";

			ps = con.prepareStatement(abbrQuery);
			ps.setString(1, routeNo);
			ps.setString(2, busCategory);
			rs = ps.executeQuery();

			while (rs.next()) {
				originAbbr = rs.getString("rc_abbriviation_ltr_start");
				destAbbr = rs.getString("rc_abbriviation_ltr_end");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// get abbreviation for origin and destination end

			// get private buses for origin start
			// if(tripType!=null && !tripType.isEmpty() && tripType.equalsIgnoreCase("O")) {
			String originPVTQuery = "select a.no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
					+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
					+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

			ps = con.prepareStatement(originPVTQuery);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, "O");
			rs = ps.executeQuery();

			while (rs.next()) {
				privateBusOrigin = rs.getInt("no_of_pvt_bus");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// }
			// get private buses for origin end

			// get private buses for destination start
			// if(tripType!=null && !tripType.isEmpty() && tripType.equalsIgnoreCase("D")) {
			String destinationPVTQuery = "select a.no_of_pvt_bus from public.nt_t_trips_generator_det02 a "
					+ "left join public.nt_m_trips_generator b " + "on a.trips_ref_code=b.tg_trips_ref_code "
					+ "where b.tg_generator_ref_no=? " + "and a.group_no=? and a.trip_type=?";

			ps = con.prepareStatement(destinationPVTQuery);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, "D");
			rs = ps.executeQuery();

			while (rs.next()) {
				privateBusDestination = rs.getInt("no_of_pvt_bus");
			}

			try {
				if (rs != null)
					rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// }
			// get private buses for destination end

			/** create temp origin and destination bus numbers with leave buses start **/
			// origin buses
			List<String> originBusList = new ArrayList<String>();
			for (int i = 0; i < privateBusOrigin; i++) {
				int count = i + 1;
				originBusList.add(originAbbr + count);
			}

			// destination buses
			List<String> destBusList = new ArrayList<String>();
			for (int i = 0; i < privateBusDestination; i++) {
				int count = i + 1;
				destBusList.add(destAbbr + count);
			}
			/** create temp origin and destination bus numbers with leave buses end **/

			// add both lists together start
			List<String> tempAllBusNums = new ArrayList<String>();
			if (originBusList != null && originBusList.size() != 0) {
				tempAllBusNums.addAll(originBusList);
			}
			if (destBusList != null && destBusList.size() != 0) {
				tempAllBusNums.addAll(destBusList);
			}
			// add both lists together end

			String query2 = "SELECT distinct bus_num, generator_ref_no, group_no, trip_type,is_pvt_bus, assigned_bus_no "
					+ "FROM public.nt_m_timetable_generator_det "
					+ "where generator_ref_no=? and group_no=? and trip_type=? and is_fixed_time='N' "
					+ "and bus_num is not null and bus_num !='' "
					+ "and bus_num not in('SLTB-O','SLTB-D','ETC-O','ETC-D') order by bus_num ";

			ps = con.prepareStatement(query2);
			ps.setString(1, refNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			for (int i = 0; rs.next(); i++) {

				e = new RouteScheduleDTO();
				String id = String.valueOf(i + 1);
				tempCount = i + 1;
				e.setTripId(id);

				if (rs.getString("assigned_bus_no") != null && !rs.getString("assigned_bus_no").isEmpty()
						&& !rs.getString("assigned_bus_no").trim().equals("")) {
					e.setAssignedBusNo(rs.getString("assigned_bus_no"));
					e.setBusNo(rs.getString("bus_num"));
				} else {
					e.setBusNo(rs.getString("bus_num"));
				}

				busNoList.add(e);

			}

			/** add missing busses to main bus list start **/
			List<RouteScheduleDTO> missingBusses = new ArrayList<RouteScheduleDTO>();
			boolean found = false;
			for (String s : tempAllBusNums) {
				found = false;
				for (RouteScheduleDTO dto : busNoList) {
					if (s.equals(dto.getBusNo())) {
						found = true;
						if (dto.getAssignedBusNo() != null && !dto.getAssignedBusNo().isEmpty()) {
							dto.setBusNo(dto.getAssignedBusNo());
							missingBusses.add(dto);
						} else {
							missingBusses.add(dto);
						}

						break;
					}
				}
				if (!found) {
					e = new RouteScheduleDTO();
					tempCount = tempCount + 1;
					String id = String.valueOf(tempCount);
					e.setTripId(id);
					e.setBusNo(s);
					missingBusses.add(e);
				}
			}

			for (RouteScheduleDTO s : missingBusses) {
				busNoList.add(s);
			}

			/** add missing busses to main bus list end **/

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public List<RouteScheduleDTO> getBusNoListWithSelectedBusesDestination(Object object, String generatedRefNo,
			String groupNo, String tripType, String routeNum, String busCategory) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<RouteScheduleDTO> busNoList = new ArrayList<RouteScheduleDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query2 = "SELECT distinct bus_num, assigned_bus_no, generator_ref_no, group_no, trip_type, seq_master FROM public.nt_m_timetable_generator_det "
					+ "where generator_ref_no=? and group_no=? and trip_type=? and is_fixed_time='N'and "
					+ "bus_num is not null and bus_num !='' order by bus_num ";

			ps = con.prepareStatement(query2);
			ps.setString(1, generatedRefNo);
			ps.setString(2, groupNo);
			ps.setString(3, tripType);
			rs = ps.executeQuery();
			RouteScheduleDTO e;

			for (int i = 0; rs.next(); i++) {

				e = new RouteScheduleDTO();
				String id = String.valueOf(i + 1);
				e.setTripId(id);

				e.setBusNo(rs.getString("bus_num"));
				e.setSelectedBusNum(rs.getString("assigned_bus_no"));
				e.setCurrentSelectedBusNo(rs.getString("assigned_bus_no"));

				busNoList.add(e);
			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}

	@Override
	public boolean updateBusNoInTimetableGeneratorDetNew(List<RouteScheduleDTO> modifyNoList, String loginUser,
			String generatedRefNum, String groupNum, String tripType) {

		Connection con = null;
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null;
		String updateSql = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		boolean saved = false;
		try {
			con = ConnectionManager.getConnection();

			for (RouteScheduleDTO dto1 : modifyNoList) {
				if (dto1.getBusNo() != null && !dto1.getBusNo().isEmpty()) {
					String queryh = "insert into public.nt_h_bus_assign_for_abbreviation (select *\r\n"
							+ "from public.nt_m_timetable_generator_det\r\n" + "where generator_ref_no =?\r\n"
							+ "and trip_type = ?\r\n" + "and bus_num =? and group_no=? ) ";

					ps1 = con.prepareStatement(queryh);

					ps1.setString(1, generatedRefNum);
					ps1.setString(2, tripType);
					ps1.setString(3, dto1.getBusNo());
					ps1.setString(4, groupNum);

					ps1.executeUpdate();

				}
			}

			for (RouteScheduleDTO dto : modifyNoList) {
				if (dto.getBusNo() != null && !dto.getBusNo().isEmpty()) {

					updateSql = "UPDATE public.nt_m_timetable_generator_det\r\n"
							+ "SET  assigned_bus_no=? , modify_by=?, modify_date=? \r\n"
							+ "WHERE  bus_num=?  and group_no=? and generator_ref_no =?  ;";

				}
				ps = con.prepareStatement(updateSql);
				ps.setString(1, dto.getSelectedBusNum().substring(0, dto.getSelectedBusNum().length() - 11));
				ps.setString(2, loginUser);
				ps.setTimestamp(3, timestamp);
				ps.setString(4, dto.getBusNo());
				ps.setString(5, groupNum);
				ps.setString(6, generatedRefNum);
				int i = ps.executeUpdate();

				if (i > 0) {
					saved = true;
				}

			}

			con.commit();
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(con);
		}
		return saved;
	}

	@Override
	public List<String> getAllAssignedBusNoForNTCFixedBuses(String generatedRefNo, String groupNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> busNoList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct assigned_bus_no "
					+ "FROM public.nt_m_timetable_generator_det "
					+ "WHERE generator_ref_no='"+generatedRefNo+"' and group_no= '"+groupNo+"' and assigned_bus_no  != '';";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				busNoList.add(rs.getString("assigned_bus_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}
	
	@Override
	public List<TimeTableDTO> getAllBusNoForFixedBusesWithoutAssignedNTCBuses(String busRoute, String serviceType,
			String originDestinationFlag, String generatedRefNo, String groupNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<TimeTableDTO> busNoList = new ArrayList<TimeTableDTO>();
		String routeFlag = null;
		if (originDestinationFlag.equalsIgnoreCase("O")) {
			routeFlag = "N";

		} else if (originDestinationFlag.equalsIgnoreCase("D")) {
			routeFlag = "Y";
		}
		try {
			con = ConnectionManager.getConnection();

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -2);
			Date pyear = cal.getTime();

			String today = dateFormat.format(new Date());
			String yearBeforeToday = dateFormat.format(pyear);

			String query2 = "select pm_vehicle_regno, pm_permit_expire_date,pm_status,pm_route_no,pm_permit_no from public.nt_t_pm_application "
					+ "where (pm_status in('A') and pm_route_flag ='" + routeFlag + "' and pm_route_no='" + busRoute
					+ "'  and pm_service_type='" + serviceType + "' " + "or (pm_status in('A') and pm_route_no='"
					+ busRoute + "' and pm_service_type='" + serviceType + "' " + " and pm_route_flag ='" + routeFlag
					+ "' and to_date(pm_permit_expire_date, 'DD/MM/YYYY') " + "BETWEEN to_date('" + yearBeforeToday
					+ "', 'DD/MM/YYYY') AND to_date('" + today + "', 'DD/MM/YYYY'))) and pm_vehicle_regno not in ("
							+ "SELECT distinct assigned_bus_no "
							+ "FROM public.nt_m_timetable_generator_det "
							+ "WHERE generator_ref_no='"+generatedRefNo+"' and group_no= '"+groupNo+"' and assigned_bus_no  != ''"
							+ ") order by pm_permit_no";

			ps = con.prepareStatement(query2);
			rs = ps.executeQuery();
			TimeTableDTO e;

			while (rs.next()) {
				e = new TimeTableDTO();
				e.setAssigneBusNo(rs.getString("pm_vehicle_regno") + "-" + rs.getString("pm_permit_expire_date"));
				busNoList.add(e);

			}

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return busNoList;
	}
}
