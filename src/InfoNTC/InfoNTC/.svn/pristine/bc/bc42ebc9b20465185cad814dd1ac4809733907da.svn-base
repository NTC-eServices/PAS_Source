package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.MidPointSurveyDTO;
import lk.informatics.ntc.model.dto.TimeBandDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class EnterSurveyDataServiceImpl implements EnterSurveyDataService {

	private static final long serialVersionUID = 1L;

	@Override
	public List<FormDTO> retrieveFormsList() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FormDTO> formList = new ArrayList<FormDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "select distinct a.form_id,a.form_description from public.nt_m_form a, public.nt_t_survey_task_det b "
					+ "where ((b.tsd_task_code='SU005' and b.tsd_status='C') or (b.tsd_task_code='SU006' and b.tsd_status='O')) "
					+ "and b.tsd_survey_no=a.survey_no";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				FormDTO dto = new FormDTO();
				dto.setFormId(rs.getString("form_id"));
				dto.setFormDescription(rs.getString("form_description"));

				formList.add(dto);
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return formList;

	}

	@Override
	public FormDTO retrieveFormDataFromFormId(String selectedFormID) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FormDTO formFto = new FormDTO();
		MidPointSurveyDTO midPointSurvey = new MidPointSurveyDTO();
		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT a.form_description,a.survey_no,a.location, a.direction_from, a.direction_to, a.name_of_recorder, a.remarks, a.date_val, "
					+ "b.ini_surveyno, b.ini_survey_type, b.ini_survey_method "
					+ "FROM public.nt_m_form a,  nt_t_initiate_survey b "
					+ "where a.form_id=? and a.survey_no=b.ini_surveyno";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, selectedFormID);
			rs = stmt.executeQuery();

			while (rs.next()) {

				formFto.setFormDescription(rs.getString("form_description"));
				formFto.setSurveyNo(rs.getString("survey_no"));
				formFto.setSurveyType(rs.getString("ini_survey_type"));
				formFto.setSurveyMethod(rs.getString("ini_survey_method"));

				midPointSurvey.setLocation(rs.getString("location"));
				midPointSurvey.setSurveyNo(rs.getString("survey_no"));
				midPointSurvey.setDirectinFrom(rs.getString("direction_from"));
				midPointSurvey.setDirectionTo(rs.getString("direction_to"));
				midPointSurvey.setNameOfRecorder(rs.getString("name_of_recorder"));
				midPointSurvey.setRemarks(rs.getString("remarks"));

				Date timestamp = rs.getTimestamp("date_val");
				if (timestamp != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					String strDate = sdf.format(timestamp);

					String[] parts = strDate.split(" ");

					midPointSurvey.setDateStr(parts[0]);
					midPointSurvey.setTimeStr(parts[1]);

					Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(parts[0]);
					midPointSurvey.setDate(date1);

					Date date2 = new SimpleDateFormat("hh:mm").parse(parts[1]);
					midPointSurvey.setTime(date2);
				}

				formFto.setMidpointSurvey(midPointSurvey);

				break;
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return formFto;
	}

	@Override
	public List<IndicatorsDTO> retrieveIndicatorsForFormId(String formId) {

		List<IndicatorsDTO> indicatorList = new ArrayList<IndicatorsDTO>();

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_indicators where form_id=? order by display_order";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, formId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				IndicatorsDTO dto = new IndicatorsDTO();
				dto.setFieldNameEn(rs.getString("field_name"));
				dto.setFieldNameSin(rs.getString("field_name_sinhala"));
				dto.setFieldNameTam(rs.getString("field_name_tamil"));
				dto.setFieldType(rs.getString("field_type"));
				dto.setFieldLength(rs.getString("field_length"));
				dto.setMandatoryField(rs.getString("mandatory_field"));
				dto.setActive(rs.getString("active"));
				dto.setLovField(rs.getString("lov_field"));

				if (dto.getFieldType() != null && !dto.getFieldType().isEmpty()
						&& !dto.getFieldType().trim().equalsIgnoreCase("")) {
					if (dto.getFieldType().equalsIgnoreCase("FT03")) {// date field
						dto.setDateField(true);
						Date today = new Date();
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						dto.setFieldInputVal(df.format(today));
					} else {
						dto.setDateField(false);
						dto.setFieldInputVal("");
					}
				}

				dto.setFieldSeqNo(rs.getInt("seqno"));
				if (dto.getLovField() != null && !dto.getLovField().isEmpty()
						&& dto.getLovField().equalsIgnoreCase("Y")) {

					List<IndicatorsDTO> indicatorLOVList = new ArrayList<IndicatorsDTO>();

					String sql2 = "select * from public.nt_t_indicator_lov where form_id=? and indicators_seqno=? order by seqno";
					stmt1 = con.prepareStatement(sql2);
					stmt1.setString(1, formId);
					stmt1.setInt(2, dto.getFieldSeqNo());
					rs1 = stmt1.executeQuery();

					while (rs1.next()) {
						IndicatorsDTO lovDto = new IndicatorsDTO();
						lovDto.setLovCode(rs1.getString("code"));
						lovDto.setFieldNameEn(rs1.getString("description"));
						lovDto.setFieldNameSin(rs1.getString("description_sinhala"));
						lovDto.setFieldNameTam(rs1.getString("description_tamil"));

						indicatorLOVList.add(lovDto);
					}

					dto.setIndicatorLOV(indicatorLOVList);
					dto.setLovFieldEnable(true);
				} else {
					dto.setLovFieldEnable(false);
				}

				dto.setDisplayAfter(rs.getString("display_after"));
				dto.setDisplayOrder(rs.getInt("display_order"));
				dto.setFieldIndicatorSeq(rs.getInt("seqno"));

				indicatorList.add(dto);
			}
			/** add start and end timeband start **/

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return indicatorList;

	}

	@Override
	public void insertDataIntoNt_t_survey_print_tracking(MidPointSurveyDTO midPointSurvey, String loginUser) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_survey_print_tracking");

			String sql = "INSERT INTO public.nt_t_survey_print_tracking (seqno, sur_prnt_survey_no, sur_prnt_form_id, sur_prnt_no_of_copies, sur_prnt_created_by, sur_prnt_create_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, midPointSurvey.getSurveyNo());
			stmt.setString(3, midPointSurvey.getFormId());
			stmt.setInt(4, midPointSurvey.getNoOfCopies());
			stmt.setString(5, loginUser);
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
	}

	@Override
	public void insertDataIntoNt_t_indicators(FormDTO formDto, String loginUser,
			List<List<IndicatorsDTO>> indicatorList, boolean editVal) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			/** insert added survey data start **/
			for (List<IndicatorsDTO> dtoList : indicatorList) {
				for (IndicatorsDTO dto : dtoList) {

					if (editVal) {
						// only need to insert newly added rows. previously added rows has sequence
						// numbers. if sequence number is 0 means that's a new row
						if (dto.getFieldSeqNo() == 0) {
							long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_indicator_values");

							String sql = "INSERT INTO public.nt_t_indicator_values "
									+ "(seqno, form_id, indicator_val, created_by, created_date, indicators_seqno, indicator_val_sinhala, indicator_val_tamil) "
									+ "VALUES(?,?,?,?,?,?,?,?)";

							stmt = con.prepareStatement(sql);

							stmt.setLong(1, seqNo);
							stmt.setString(2, formDto.getFormId());
							if (dto.getLovField() != null && !dto.getLovField().isEmpty()
									&& dto.getLovField().equalsIgnoreCase("Y")) {
								stmt.setString(3, dto.getLovSelectedVal());
							} else {
								stmt.setString(3, dto.getFieldInputVal());
							}
							stmt.setString(4, loginUser);
							stmt.setTimestamp(5, timestamp);
							stmt.setInt(6, dto.getFieldIndicatorSeq());
							stmt.setString(7, dto.getFieldNameSin());
							stmt.setString(8, dto.getFieldNameTam());
							stmt.executeUpdate();
						}

					} else {

						// if this is not an edit. all records should be inserted

						long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_indicator_values");

						String sql = "INSERT INTO public.nt_t_indicator_values "
								+ "(seqno, form_id, indicator_val, created_by, created_date, indicators_seqno, indicator_val_sinhala, indicator_val_tamil) "
								+ "VALUES(?,?,?,?,?,?,?,?)";

						stmt = con.prepareStatement(sql);

						stmt.setLong(1, seqNo);
						stmt.setString(2, formDto.getFormId());
						stmt.setString(3, dto.getFieldInputVal());
						stmt.setString(4, loginUser);
						stmt.setTimestamp(5, timestamp);
						stmt.setInt(6, dto.getFieldIndicatorSeq());
						stmt.setString(7, dto.getFieldNameSin());
						stmt.setString(8, dto.getFieldNameTam());
						stmt.executeUpdate();
					}

				}
			}
			ConnectionManager.close(stmt);

			/** insert added survey data end **/

			/** update nt_m_form table data start **/
			Timestamp timestmpUserInput = null;
			if (formDto.getMidpointSurvey().getDate() != null) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				String dateStr = df.format(formDto.getMidpointSurvey().getDate());

				DateFormat df1 = new SimpleDateFormat("hh:mm");
				String timeStr = df1.format(formDto.getMidpointSurvey().getTime());

				String dateString = dateStr + " " + timeStr;

				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");

				Date dateDt = formatter.parse(dateString);

				timestmpUserInput = new Timestamp(dateDt.getTime());
			}
			String sql = "UPDATE public.nt_m_form SET location=?, date_val=?, remarks=?, direction_from=?, direction_to=?, "
					+ "name_of_recorder=?, modify_by=?, modify_date=? " + "WHERE form_id=?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, formDto.getMidpointSurvey().getLocation());
			if (timestmpUserInput != null) {
				stmt.setTimestamp(2, timestmpUserInput);
			} else {
				stmt.setNull(2, Types.TIMESTAMP);
			}
			stmt.setString(3, formDto.getMidpointSurvey().getRemarks());
			stmt.setString(4, formDto.getMidpointSurvey().getDirectinFrom());
			stmt.setString(5, formDto.getMidpointSurvey().getDirectionTo());
			stmt.setString(6, formDto.getMidpointSurvey().getNameOfRecorder());
			stmt.setString(7, loginUser);
			stmt.setTimestamp(8, timestamp);
			stmt.setString(9, formDto.getFormId());

			stmt.executeUpdate();
			/** update nt_m_form table data end **/

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
	}

	@Override
	public List<List<IndicatorsDTO>> retrieveIndicatorListDataFromFormId(String selectedFormID) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt1 = null;
		ResultSet rs1 = null;

		List<IndicatorsDTO> indicatorList = new ArrayList<IndicatorsDTO>();
		List<IndicatorsDTO> tempIndicatorList = new ArrayList<IndicatorsDTO>();
		List<List<IndicatorsDTO>> indicatorDtoList = new ArrayList<List<IndicatorsDTO>>();
		IndicatorsDTO timeBanIndDto = new IndicatorsDTO();

		indicatorList = retrieveIndicatorsForFormId(selectedFormID);

		try {

			con = ConnectionManager.getConnection();

			/** check whether time band is exist in the data start **/
			boolean timeBand = false;
			String select = "select * from public.nt_t_indicator_values where form_id ='" + selectedFormID
					+ "' and indicators_seqno='0'";
			stmt = con.prepareStatement(select);
			rs = stmt.executeQuery();

			while (rs.next()) {
				timeBand = true;
				break;
			}
			if (timeBand) {
				timeBanIndDto.setActive("Y");
				timeBanIndDto.setDisplayOrder(indicatorList.size() + 1);
				timeBanIndDto.setFieldIndicatorSeq(0);
				timeBanIndDto.setFieldNameEn("Time Band");
				timeBanIndDto.setFieldSeqNo(0);
				timeBanIndDto.setLovField("Y");
				timeBanIndDto.setMandatoryField("Y");

				indicatorList.add(timeBanIndDto);
			}
			/** check whether time band is exist in the data end **/

			for (IndicatorsDTO dto : indicatorList) {

				if (dto.getFieldIndicatorSeq() != 0) {

					String query = "select * from public.nt_t_indicator_values a, public.nt_t_indicators b "
							+ "where a.form_id=? and indicators_seqno=? and indicators_seqno=b.seqno order by a.seqno ASC";

					stmt = con.prepareStatement(query);
					stmt.setString(1, selectedFormID);
					stmt.setInt(2, dto.getFieldIndicatorSeq());
					rs = stmt.executeQuery();

					while (rs.next()) {
						IndicatorsDTO indDto = new IndicatorsDTO();

						indDto.setFieldIndicatorSeq(rs.getInt("indicators_seqno"));
						indDto.setFieldNameEn(rs.getString("field_name"));
						indDto.setFieldNameSin(rs.getString("field_name_sinhala"));
						indDto.setFieldNameTam(rs.getString("field_name_tamil"));
						indDto.setFieldType(rs.getString("field_type"));
						indDto.setFieldLength(rs.getString("field_length"));
						indDto.setMandatoryField(rs.getString("mandatory_field"));
						indDto.setActive(rs.getString("active"));
						indDto.setLovField(rs.getString("lov_field"));
						indDto.setFieldInputVal(rs.getString("indicator_val"));

						if (indDto.getLovField() != null && !indDto.getLovField().isEmpty()
								&& indDto.getLovField().equalsIgnoreCase("Y")) {

							List<IndicatorsDTO> indicatorLOVList = new ArrayList<IndicatorsDTO>();

							String sql2 = "select * from public.nt_t_indicator_lov where form_id=? and indicators_seqno=? order by seqno";
							stmt1 = con.prepareStatement(sql2);
							stmt1.setString(1, selectedFormID);
							stmt1.setInt(2, indDto.getFieldIndicatorSeq());
							rs1 = stmt1.executeQuery();

							while (rs1.next()) {
								IndicatorsDTO lovDto = new IndicatorsDTO();
								lovDto.setLovCode(rs1.getString("code"));
								lovDto.setFieldNameEn(rs1.getString("description"));
								lovDto.setFieldNameSin(rs1.getString("description_sinhala"));
								lovDto.setFieldNameTam(rs1.getString("description_tamil"));

								indicatorLOVList.add(lovDto);
							}

							dto.setIndicatorLOV(indicatorLOVList);

							for (IndicatorsDTO obj : indicatorLOVList) {
								if (indDto.getFieldInputVal() != null && !indDto.getFieldInputVal().isEmpty()
										&& indDto.getFieldInputVal().equalsIgnoreCase(obj.getLovCode())) {
									indDto.setFieldInputVal(obj.getFieldNameEn());
								}
							}
						}

						indDto.setDisplayAfter(rs.getString("display_after"));
						indDto.setDisplayOrder(rs.getInt("display_order"));

						indDto.setFieldSeqNo(rs.getInt("seqno"));

						tempIndicatorList.add(indDto);
					}
					indicatorDtoList.add(tempIndicatorList);
					tempIndicatorList = new ArrayList<IndicatorsDTO>();

				} else {
					String query = "select * from public.nt_t_indicator_values a where a.form_id=? and indicators_seqno=0 order by a.seqno ASC";

					stmt = con.prepareStatement(query);
					stmt.setString(1, selectedFormID);
					rs = stmt.executeQuery();

					while (rs.next()) {
						IndicatorsDTO indDto = new IndicatorsDTO();

						indDto.setFieldIndicatorSeq(rs.getInt("indicators_seqno"));
						indDto.setFieldNameEn("Time Band");
						indDto.setFieldNameSin(null);
						indDto.setFieldNameTam(null);
						indDto.setFieldType(null);
						indDto.setFieldLength(null);
						indDto.setMandatoryField("Y");
						indDto.setActive("Y");
						indDto.setLovField("Y");
						indDto.setFieldInputVal(rs.getString("indicator_val"));

						if (indDto.getLovField() != null && !indDto.getLovField().isEmpty()
								&& indDto.getLovField().equalsIgnoreCase("Y")) {
							List<IndicatorsDTO> indicatorLOVList = new ArrayList<IndicatorsDTO>();

							String sql2 = "select * from public.nt_r_time_band";
							stmt1 = con.prepareStatement(sql2);
							rs1 = stmt1.executeQuery();

							while (rs1.next()) {
								IndicatorsDTO lovDto = new IndicatorsDTO();

								lovDto.setLovCode(rs1.getString("time_band"));
								lovDto.setFieldNameEn(rs1.getString("start_time") + " - " + rs1.getInt("end_time"));

								indicatorLOVList.add(lovDto);
							}

							dto.setIndicatorLOV(indicatorLOVList);

							for (IndicatorsDTO obj : indicatorLOVList) {
								if (indDto.getFieldInputVal() != null && !indDto.getFieldInputVal().isEmpty()
										&& indDto.getFieldInputVal().equalsIgnoreCase(obj.getLovCode())) {
									indDto.setFieldInputVal(obj.getFieldNameEn() + ".00");
								}
							}
						}

						indDto.setFieldSeqNo(rs.getInt("seqno"));

						tempIndicatorList.add(indDto);
					}
					indicatorDtoList.add(tempIndicatorList);
					tempIndicatorList = new ArrayList<IndicatorsDTO>();
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return indicatorDtoList;

	}

	@Override
	public void deleteSelectedTableRow(IndicatorsDTO selectedDto) {

		Connection con = null;
		PreparedStatement stmt = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "DELETE from public.nt_t_indicator_values WHERE seqno =?";

			stmt = con.prepareStatement(query);
			stmt.setInt(1, selectedDto.getFieldSeqNo());
			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

	}

	@Override
	public String getSIRFromSIPNumber(String sipReqNumber) {
		String sirRequestNo = "";

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT ini_isu_requestno FROM public.nt_t_initiate_survey WHERE ini_surveyno=?";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, sipReqNumber);
			rs = stmt.executeQuery();

			while (rs.next()) {
				sirRequestNo = rs.getString("ini_isu_requestno");
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return sirRequestNo;
	}

	@Override
	public List<TimeBandDTO> retrieveTimeBandList() {
		List<TimeBandDTO> timeBandList = new ArrayList<TimeBandDTO>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT * FROM public.nt_r_time_band";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				TimeBandDTO dto = new TimeBandDTO();
				dto.setStartTime(rs.getString("start_time"));
				dto.setEndTime(rs.getString("end_time"));
				dto.setTimeBand(rs.getInt("time_band"));

				timeBandList.add(dto);
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return timeBandList;
	}

}
