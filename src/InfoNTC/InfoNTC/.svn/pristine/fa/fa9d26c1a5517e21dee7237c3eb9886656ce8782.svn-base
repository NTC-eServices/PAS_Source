package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.FormDTO;
import lk.informatics.ntc.model.dto.IndicatorsDTO;
import lk.informatics.ntc.model.dto.MidPointSurveyDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class GenerateDraftSurveyServiceImpl implements GenerateDraftSurveyService {

	private static final long serialVersionUID = 1L;

	@Override
	public List<FormDTO> retrieveFormsList() {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FormDTO> formList = new ArrayList<FormDTO>();
		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT * FROM public.nt_m_form";

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

				formFto.setFormId(selectedFormID);
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

					Date date2 = new SimpleDateFormat("HH:mm").parse(parts[1]);
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

		try {
			con = ConnectionManager.getConnection();

			String sql = "select * from public.nt_t_indicators where form_id=? order by display_order";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, formId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				IndicatorsDTO dto = new IndicatorsDTO();
				dto.setFormId(formId);
				dto.setFieldIndicatorSeq(rs.getInt("seqno"));
				dto.setFieldNameEn(rs.getString("field_name"));
				dto.setFieldNameSin(rs.getString("field_name_sinhala"));
				dto.setFieldNameTam(rs.getString("field_name_tamil"));
				dto.setFieldType(rs.getString("field_type"));
				dto.setFieldLength(rs.getString("field_length"));
				dto.setMandatoryField(rs.getString("mandatory_field"));
				dto.setActive(rs.getString("active"));
				dto.setLovField(rs.getString("lov_field"));
				dto.setDisplayAfter(rs.getString("display_after"));
				dto.setDisplayOrder(rs.getInt("display_order"));

				indicatorList.add(dto);
			}

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

//draft survey form report
	@Override
	public List<Integer> insertEnteredRows(String selectedFormID, List<Integer> newInsert) {

		Connection con = null;
		PreparedStatement ps = null, ps1 = null, ps3 = null;
		ResultSet rs = null, rs1 = null;
		String tempRows = null;

		try {

			con = ConnectionManager.getConnection();
			String query1 = "Select * from public.nt_temp_sur_form_rows;";

			ps1 = con.prepareStatement(query1);
			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				tempRows = rs1.getString("temp_rows");

			}
			if (tempRows == null) {
				for (int i = 1; i < newInsert.size(); i++) {
					long seqNo = Utils.getNextValBySeqName(con, "seq_nt_temp_sur_form_rows");

					String query = "INSERT INTO public.nt_temp_sur_form_rows " + "(seqno, temp_form_id, temp_rows) "
							+ "VALUES(?,?,?); ";

					ps = con.prepareStatement(query);

					ps.setLong(1, seqNo);
					ps.setString(2, selectedFormID);
					ps.setLong(3, newInsert.get(i));

					ps.executeUpdate();

					con.commit();

				}
			} else {
				String query2 = "Delete  from public.nt_temp_sur_form_rows;";

				ps3 = con.prepareStatement(query2);
				ps3.executeUpdate();

				for (int i = 1; i < newInsert.size(); i++) {
					long seqNo = Utils.getNextValBySeqName(con, "seq_nt_temp_sur_form_rows");
					String query = "INSERT INTO public.nt_temp_sur_form_rows " + "(seqno, temp_form_id, temp_rows) "
							+ "VALUES(?,?,?); ";
					ps = con.prepareStatement(query);

					ps.setLong(1, seqNo);
					ps.setString(2, selectedFormID);
					ps.setLong(3, newInsert.get(i));

					ps.executeUpdate();
				}
				con.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps1);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return newInsert;

	}

	@Override
	public void deleteEnterdRows(String selectedFormID) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			if (selectedFormID == null) {
				String sql = "Delete  from public.nt_temp_sur_form_rows;";
				stmt = con.prepareStatement(sql);

				stmt.executeUpdate();

				con.commit();
			} else {
				String sql = "Delete  from public.nt_temp_sur_form_rows where temp_form_id=?;";
				stmt = con.prepareStatement(sql);

				stmt.setString(1, selectedFormID);

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

}
