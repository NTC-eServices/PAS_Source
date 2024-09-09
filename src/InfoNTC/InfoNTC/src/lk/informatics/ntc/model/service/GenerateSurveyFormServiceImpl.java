package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.GenerateSurveyFormDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class GenerateSurveyFormServiceImpl implements GenerateSurveyFormService {

	@Override
	public List<GenerateSurveyFormDTO> getApprovedSurveyNoList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GenerateSurveyFormDTO> approvedSurveyNoList = new ArrayList<GenerateSurveyFormDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "Select ini_surveyno, ini_survey_type, b.description as ini_survey_type_des, ini_survey_method, c.description as ini_survey_method_des "
					+ "From public.nt_t_initiate_survey a "
					+ "inner join nt_r_survey_types b on b.code=a.ini_survey_type "
					+ "inner join nt_r_survey_methods c on c.code=a.ini_survey_method "
					+ "inner join nt_t_survey_task_det d on d.tsd_survey_no=a.ini_surveyno "
					+ "Where ini_status='A' and ini_surveyno is not null "
					+ "and (( tsd_task_code = 'SU003' and tsd_status = 'C' ) or ( tsd_task_code = 'SU004' and tsd_status = 'O' ) or ( tsd_task_code = 'SU004' and tsd_status = 'C' )) "
					+ "order by ini_surveyno";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GenerateSurveyFormDTO data = new GenerateSurveyFormDTO();
				data.setSurveyNo(rs.getString("ini_surveyno"));
				data.setSurveyType(rs.getString("ini_survey_type"));
				data.setSurveyMethod(rs.getString("ini_survey_method"));
				data.setSurveyType_des(rs.getString("ini_survey_type_des"));
				data.setSurveyMethod_des(rs.getString("ini_survey_method_des"));

				approvedSurveyNoList.add(data);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return approvedSurveyNoList;

	}

	@Override
	public List<GenerateSurveyFormDTO> getFormIDList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GenerateSurveyFormDTO> formIDList = new ArrayList<GenerateSurveyFormDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select form_id, form_description from public.nt_m_form order by form_id";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GenerateSurveyFormDTO data = new GenerateSurveyFormDTO();
				data.setFormID(rs.getString("form_id"));
				data.setFormDescription(rs.getString("form_description"));

				formIDList.add(data);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return formIDList;

	}

	@Override
	public List<GenerateSurveyFormDTO> getFieldTypeList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GenerateSurveyFormDTO> fieldTypeList = new ArrayList<GenerateSurveyFormDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select code, description " + "from public.nt_r_field_type "
					+ "where status='A' order by description; ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GenerateSurveyFormDTO data = new GenerateSurveyFormDTO();
				data.setFieldType(rs.getString("code"));
				data.setFieldType_des(rs.getString("description"));

				fieldTypeList.add(data);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return fieldTypeList;

	}

	@Override
	public List<GenerateSurveyFormDTO> getValidationMethodList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GenerateSurveyFormDTO> validationMethodList = new ArrayList<GenerateSurveyFormDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select code, description " + "from public.nt_r_field_validation "
					+ "where status='A' order by description; ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GenerateSurveyFormDTO data = new GenerateSurveyFormDTO();
				data.setValidationMethod(rs.getString("code"));
				data.setValidationMethod_des(rs.getString("description"));

				validationMethodList.add(data);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return validationMethodList;

	}

	@Override
	public boolean check_LOV_code_duplicate(String LOV_code) {
		boolean LOV_code_duplicate = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT code FROM public.nt_t_indicator_lov WHERE code=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, LOV_code);
			rs = ps.executeQuery();

			if (rs.next()) {
				LOV_code_duplicate = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return LOV_code_duplicate;
	}

	@Override
	public boolean saveForm(GenerateSurveyFormDTO generateSurveyFormDTO, List<GenerateSurveyFormDTO> indicators_list,
			String user) {
		boolean success = false;
		Connection con = null;
		PreparedStatement ps = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			// save form details
			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_form");
			String query = "INSERT INTO public.nt_m_form (seqno, survey_no, form_id, temp_id, form_description, header_label, header_label_sinhala, header_label_tamil, created_by, created_date) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?);";

			ps = con.prepareStatement(query);
			ps.setLong(1, seqNo);
			ps.setString(2, generateSurveyFormDTO.getSurveyNo());
			ps.setString(3, generateSurveyFormDTO.getFormID());
			ps.setString(4, generateSurveyFormDTO.getCopyTemplateID());
			ps.setString(5, generateSurveyFormDTO.getFormDescription());
			ps.setString(6, generateSurveyFormDTO.getHeaderLabel());
			ps.setString(7, generateSurveyFormDTO.getHeaderLabel_sinhala());
			ps.setString(8, generateSurveyFormDTO.getHeaderLabel_tamil());
			ps.setString(9, user);
			ps.setTimestamp(10, timestamp);
			ps.executeUpdate();

			// save indicators
			for (int i = 0; i < indicators_list.size(); i++) {
				long seqNo_indicator = Utils.getNextValBySeqName(con, "seq_nt_t_indicators");
				String query2 = "INSERT INTO public.nt_t_indicators(seqno, form_id, field_name, field_name_sinhala, field_name_tamil, field_type,"
						+ " validation_method,  field_length, mandatory_field,"
						+ " active, lov_field, created_by, created_date,display_after,field_definition,display_order) "
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

				ps = con.prepareStatement(query2);
				ps.setLong(1, seqNo_indicator);
				ps.setString(2, generateSurveyFormDTO.getFormID());
				ps.setString(3, indicators_list.get(i).getFieldName());
				ps.setString(4, indicators_list.get(i).getFieldName_sinhala());
				ps.setString(5, indicators_list.get(i).getFieldName_tamil());
				ps.setString(6, indicators_list.get(i).getFieldType());
				ps.setString(7, indicators_list.get(i).getValidationMethod());
				ps.setInt(8, indicators_list.get(i).getFieldLength());
				if (indicators_list.get(i).getMandatoryField() == true) {
					ps.setString(9, "Y");
				} else if (indicators_list.get(i).getMandatoryField() == false) {
					ps.setString(9, "N");
				}
				if (indicators_list.get(i).getActive() == true) {
					ps.setString(10, "Y");
				} else if (indicators_list.get(i).getActive() == false) {
					ps.setString(10, "N");
				}
				if (indicators_list.get(i).getFieldType().equals("FT01")) {
					ps.setString(11, "Y");
				} else if (!indicators_list.get(i).getFieldType().equals("FT01")) {
					ps.setString(11, "N");
				}
				ps.setString(12, user);
				ps.setTimestamp(13, timestamp);
				ps.setString(14, indicators_list.get(i).getDisplayAfter());
				ps.setString(15, indicators_list.get(i).getFieldDefinition());
				ps.setInt(16, indicators_list.get(i).getDisplayOrder());
				ps.executeUpdate();

				if (indicators_list.get(i).getFieldType().equals("FT01")) {
					// save indicator values
					for (int j = 0; j < indicators_list.get(i).getIndicator_values_list().size(); j++) {
						long seqNo_indicator_values = Utils.getNextValBySeqName(con, "seq_nt_t_indicator_lov");
						String query3 = "INSERT INTO public.nt_t_indicator_lov (seqno, form_id, code, description, description_sinhala, description_tamil, created_by, created_date, indicators_seqno) "
								+ "VALUES (?,?,?,?,?,?,?,?,?);";

						ps = con.prepareStatement(query3);
						ps.setLong(1, seqNo_indicator_values);
						ps.setString(2, generateSurveyFormDTO.getFormID());
						ps.setString(3, indicators_list.get(i).getIndicator_values_list().get(j).getLOV_code());
						ps.setString(4, indicators_list.get(i).getIndicator_values_list().get(j).getLOV_description());
						ps.setString(5,
								indicators_list.get(i).getIndicator_values_list().get(j).getLOV_description_sinhala());
						ps.setString(6,
								indicators_list.get(i).getIndicator_values_list().get(j).getLOV_description_tamil());
						ps.setString(7, user);
						ps.setTimestamp(8, timestamp);
						ps.setLong(9, seqNo_indicator);
						ps.executeUpdate();
					}
				}
			}
			con.commit();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public GenerateSurveyFormDTO getFormDetails(GenerateSurveyFormDTO generateSurveyFormDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		GenerateSurveyFormDTO data = new GenerateSurveyFormDTO();

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, form_id, survey_no, temp_id, form_description, header_label, header_label_sinhala, header_label_tamil "
					+ "FROM public.nt_m_form " + "WHERE survey_no=? order by seqno limit 1 ";
			ps = con.prepareStatement(query);
			ps.setString(1, generateSurveyFormDTO.getSurveyNo());
			rs = ps.executeQuery();
			while (rs.next()) {
				data.setSurveyNo(rs.getString("survey_no"));
				data.setFormID(rs.getString("form_id"));
				data.setCopyTemplateID(rs.getString("temp_id"));
				data.setFormDescription(rs.getString("form_description"));
				data.setHeaderLabel(rs.getString("header_label"));
				data.setHeaderLabel_sinhala(rs.getString("header_label_sinhala"));
				data.setHeaderLabel_tamil(rs.getString("header_label_tamil"));
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
	public List<GenerateSurveyFormDTO> getIndicatorsList(GenerateSurveyFormDTO generateSurveyFormDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateSurveyFormDTO> data = new ArrayList<GenerateSurveyFormDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT a.seqno, a.form_id, b.survey_no, a.field_name, a.field_name_sinhala, a.field_name_tamil, a.field_type, c.description as field_type_des, a.validation_method, "
					+ "d.description as validation_method_des, a.field_length, a.mandatory_field, "
					+ "a.active, a.lov_field, a.display_after, a.display_order, a.field_definition, e.description as field_definition_des FROM public.nt_t_indicators a inner join nt_m_form b on a.form_id=b.form_id inner join nt_r_field_type c on a.field_type = c.code "
					+ "left outer join nt_r_field_validation d on a.validation_method = d.code left outer join nt_r_field_definition e on a.field_definition = e.code WHERE b.survey_no=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, generateSurveyFormDTO.getSurveyNo());
			rs = ps.executeQuery();
			GenerateSurveyFormDTO p;

			while (rs.next()) {
				p = new GenerateSurveyFormDTO();
				p.setSeqNo(rs.getLong("seqno"));
				p.setFieldName(rs.getString("field_name"));
				p.setFieldName_sinhala(rs.getString("field_name_sinhala"));
				p.setFieldName_tamil(rs.getString("field_name_tamil"));
				p.setFieldType(rs.getString("field_type"));
				p.setFieldType_des(rs.getString("field_type_des"));
				p.setValidationMethod(rs.getString("validation_method"));
				p.setValidationMethod_des(rs.getString("validation_method_des"));
				p.setFieldDefinition(rs.getString("field_definition"));
				p.setFieldDefinition_des(rs.getString("field_definition_des"));
				p.setFieldLength(rs.getInt("field_length"));
				p.setDisplayAfter(rs.getString("display_after"));
				p.setDisplayOrder(rs.getInt("display_order"));

				if (rs.getString("mandatory_field").equals("Y")) {
					p.setMandatoryField(true);
				} else if (rs.getString("mandatory_field").equals("N")) {
					p.setMandatoryField(false);
				}
				if (rs.getString("active").equals("Y")) {
					p.setActive(true);
				} else if (rs.getString("active").equals("N")) {
					p.setActive(false);
				}
				data.add(p);

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
	public boolean check_FormID_duplicate(String formID) {
		boolean FormID_duplicate = false;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT form_id FROM public.nt_m_form WHERE form_id=? LIMIT 1;";

			ps = con.prepareStatement(query);
			ps.setString(1, formID);
			rs = ps.executeQuery();

			if (rs.next()) {
				FormID_duplicate = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return FormID_duplicate;
	}

	@Override
	public void updateSurveyTaskDetails(String surveyNo, String taskCode, String taskStatus) {
		
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT * " + "FROM public.nt_t_survey_task_det " + "where tsd_survey_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				if (!rs.getString("tsd_task_code").equals(taskCode) || !rs.getString("tsd_status").equals(taskStatus)) {
					// insert to history table
					String q = "INSERT INTO public.nt_h_survey_task_his "
							+ "(tsd_seq, tsd_request_no, tsd_survey_no, tsd_task_code, tsd_status, created_by, created_date) "
							+ "VALUES(?, ?, ?, ?, ?, ?, ?); ";

					ps2 = con.prepareStatement(q);
					ps2.setInt(1, rs.getInt("tsd_seq"));
					ps2.setString(2, rs.getString("tsd_request_no"));
					ps2.setString(3, rs.getString("tsd_survey_no"));
					ps2.setString(4, rs.getString("tsd_task_code"));
					ps2.setString(5, rs.getString("tsd_status"));
					ps2.setString(6, rs.getString("created_by"));
					ps2.setTimestamp(7, rs.getTimestamp("created_date"));
					ps2.executeUpdate();
					con.commit();
					try {
						if (ps2 != null)
							ps2.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

					// update task details table
					String q2 = "UPDATE public.nt_t_survey_task_det "
							+ "SET tsd_task_code=?, tsd_status=?, tsd_survey_no=? " + "WHERE tsd_survey_no=?; ";
					ps3 = con.prepareStatement(q2);
					ps3.setString(1, taskCode);
					ps3.setString(2, taskStatus);
					ps3.setString(3, surveyNo);
					ps3.setString(4, surveyNo);

					ps3.executeUpdate();
					con.commit();
					ConnectionManager.close(ps3);

					con.commit();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		
	}

	@Override
	public List<GenerateSurveyFormDTO> getFieldDefinitionList() {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GenerateSurveyFormDTO> definitionList = new ArrayList<GenerateSurveyFormDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select code, description " + "from public.nt_r_field_definition "
					+ "where status='A' order by description; ";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				GenerateSurveyFormDTO data = new GenerateSurveyFormDTO();
				data.setFieldDefinition(rs.getString("code"));
				data.setFieldDefinition_des(rs.getString("description"));

				definitionList.add(data);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return definitionList;

	}

	@Override
	public List<GenerateSurveyFormDTO> getIndicatorsValueList(GenerateSurveyFormDTO generateSurveyFormDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateSurveyFormDTO> data = new ArrayList<GenerateSurveyFormDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, form_id, code, description, indicators_seqno, description_sinhala, description_tamil "
					+ "FROM public.nt_t_indicator_lov " + "WHERE indicators_seqno =? ";
			ps = con.prepareStatement(query);
			ps.setLong(1, generateSurveyFormDTO.getSeqNo());
			rs = ps.executeQuery();
			GenerateSurveyFormDTO p;

			while (rs.next()) {
				p = new GenerateSurveyFormDTO();
				p.setLOV_seqNo(rs.getLong("seqno"));
				p.setLOV_code(rs.getString("code"));
				p.setLOV_description(rs.getString("description"));
				p.setLOV_description_sinhala(rs.getString("description_sinhala"));
				p.setLOV_description_tamil(rs.getString("description_tamil"));

				data.add(p);
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
	public boolean updateForm(GenerateSurveyFormDTO generateSurveyFormDTO, List<GenerateSurveyFormDTO> indicators_list,
			String user) {
		boolean success = false;
		Connection con = null;
		PreparedStatement ps = null, ps2 = null, ps3 = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {

			con = ConnectionManager.getConnection();

			// update form details
			String query = "Update public.nt_m_form SET temp_id=?, form_description=?, header_label=?, header_label_sinhala=?, header_label_tamil=?, modify_by=?, modify_date=? WHERE survey_no =? and gmf_form_id=?";

			ps = con.prepareStatement(query);
			ps.setString(1, generateSurveyFormDTO.getCopyTemplateID());
			ps.setString(2, generateSurveyFormDTO.getFormDescription());
			ps.setString(3, generateSurveyFormDTO.getHeaderLabel());
			ps.setString(4, generateSurveyFormDTO.getHeaderLabel_sinhala());
			ps.setString(5, generateSurveyFormDTO.getHeaderLabel_tamil());
			ps.setString(6, user);
			ps.setTimestamp(7, timestamp);
			ps.setString(8, generateSurveyFormDTO.getSurveyNo());
			ps.setString(9, generateSurveyFormDTO.getFormID());
			ps.executeUpdate();

			// delete indicator process
			List<GenerateSurveyFormDTO> dataIndicatorList = new ArrayList<GenerateSurveyFormDTO>();
			// get indicators from database
			dataIndicatorList = getIndicatorsList(generateSurveyFormDTO);

			for (int x = 0; x < dataIndicatorList.size(); x++) {
				Boolean removed = true;
				for (int i = 0; i < indicators_list.size(); i++) {
					if (indicators_list.get(i).getSeqNo() != null) {
						if (dataIndicatorList.get(x).getSeqNo().equals(indicators_list.get(i).getSeqNo())) {
							removed = false;
							break;
						}
					}
				}
				if (removed) {
					// delete removed indicators from database
					String queryDel = "DELETE FROM public.nt_t_indicators WHERE seqno=?;";
					ps3 = con.prepareStatement(queryDel);
					ps3.setLong(1, dataIndicatorList.get(x).getSeqNo());
					ps3.executeUpdate();
					
					ConnectionManager.close(ps3);
					// delete indicators LOV from database
					String queryDel2 = "DELETE FROM public.nt_t_indicator_lov WHERE indicators_seqno=?;";
					ps3 = con.prepareStatement(queryDel2);
					ps3.setLong(1, dataIndicatorList.get(x).getSeqNo());
					ps3.executeUpdate();
					
				}
			}

			for (int i = 0; i < indicators_list.size(); i++) {

				// update indicators
				if (indicators_list.get(i).getSeqNo() != null) {
					String query2 = "Update public.nt_t_indicators SET field_name = ?, field_name_sinhala = ?, field_name_tamil = ?, field_type = ?, "
							+ "validation_method = ?, field_length = ?, mandatory_field = ?, "
							+ "active = ?, lov_field = ?, modify_by = ?, modify_date = ?, display_after = ?, field_definition = ?, display_order = ? where seqno = ? ";

					ps = con.prepareStatement(query2);
					ps.setString(1, indicators_list.get(i).getFieldName());
					ps.setString(2, indicators_list.get(i).getFieldName_sinhala());
					ps.setString(3, indicators_list.get(i).getFieldName_tamil());
					ps.setString(4, indicators_list.get(i).getFieldType());
					ps.setString(5, indicators_list.get(i).getValidationMethod());
					ps.setInt(6, indicators_list.get(i).getFieldLength());
					if (indicators_list.get(i).getMandatoryField() == true) {
						ps.setString(7, "Y");
					} else if (indicators_list.get(i).getMandatoryField() == false) {
						ps.setString(7, "N");
					}
					if (indicators_list.get(i).getActive() == true) {
						ps.setString(8, "Y");
					} else if (indicators_list.get(i).getActive() == false) {
						ps.setString(8, "N");
					}
					if (indicators_list.get(i).getFieldType().equals("FT01")) {
						ps.setString(9, "Y");
					} else if (!indicators_list.get(i).getFieldType().equals("FT01")) {
						ps.setString(9, "N");
					}
					ps.setString(10, user);
					ps.setTimestamp(11, timestamp);
					ps.setString(12, indicators_list.get(i).getDisplayAfter());
					ps.setString(13, indicators_list.get(i).getFieldDefinition());
					ps.setInt(14, indicators_list.get(i).getDisplayOrder());
					ps.setLong(15, indicators_list.get(i).getSeqNo());
					ps.executeUpdate();

					List<GenerateSurveyFormDTO> dataIndicatorValuesList = new ArrayList<GenerateSurveyFormDTO>();
					if (indicators_list.get(i).getFieldType().equals("FT01")) {
						// update indicator values function
						// get indicator values from database
						dataIndicatorValuesList = getIndicatorsValueList(indicators_list.get(i));
						for (int x = 0; x < dataIndicatorValuesList.size(); x++) {
							Boolean removed = true;
							for (int y = 0; y < indicators_list.get(i).getIndicator_values_list().size(); y++) {
								if (indicators_list.get(i).getIndicator_values_list().get(y).getLOV_seqNo() != null) {
									if (dataIndicatorValuesList.get(x).getLOV_seqNo().equals(
											indicators_list.get(i).getIndicator_values_list().get(y).getLOV_seqNo())) {
										removed = false;
										break;
									}
								}
							}
							if (removed) {
								// delete removed indicator values from database
								String queryDel = "DELETE FROM public.nt_t_indicator_lov WHERE seqno=?;";
								ps3 = con.prepareStatement(queryDel);
								ps3.setLong(1, dataIndicatorValuesList.get(x).getLOV_seqNo());
								ps3.executeUpdate();
							}
						}

						for (int j = 0; j < indicators_list.get(i).getIndicator_values_list().size(); j++) {
							if (indicators_list.get(i).getIndicator_values_list().get(j).getLOV_seqNo() == null) {

								long seqNo_indicator_values = Utils.getNextValBySeqName(con, "seq_nt_t_indicator_lov");
								String query3 = "INSERT INTO public.nt_t_indicator_lov (seqno, form_id, code, description, description_sinhala, description_tamil, created_by, created_date, indicators_seqno) "
										+ "VALUES (?,?,?,?,?,?,?,?,?);";

								ps = con.prepareStatement(query3);
								ps.setLong(1, seqNo_indicator_values);
								ps.setString(2, generateSurveyFormDTO.getFormID());
								ps.setString(3, indicators_list.get(i).getIndicator_values_list().get(j).getLOV_code());
								ps.setString(4,
										indicators_list.get(i).getIndicator_values_list().get(j).getLOV_description());
								ps.setString(5, indicators_list.get(i).getIndicator_values_list().get(j)
										.getLOV_description_sinhala());
								ps.setString(6, indicators_list.get(i).getIndicator_values_list().get(j)
										.getLOV_description_tamil());
								ps.setString(7, user);
								ps.setTimestamp(8, timestamp);
								ps.setLong(9, indicators_list.get(i).getSeqNo());
								ps.executeUpdate();
							}
						}
					}

				} else {
					// insert new indicators
					long seqNo_indicator = Utils.getNextValBySeqName(con, "seq_nt_t_indicators");
					String query2 = "INSERT INTO public.nt_t_indicators(seqno, form_id, field_name, field_name_sinhala, field_name_tamil, field_type,"
							+ " validation_method, field_length, mandatory_field,"
							+ " active, lov_field, created_by, created_date,display_after,field_definition,display_order) "
							+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

					ps = con.prepareStatement(query2);
					ps.setLong(1, seqNo_indicator);
					ps.setString(2, generateSurveyFormDTO.getFormID());
					ps.setString(3, indicators_list.get(i).getFieldName());
					ps.setString(4, indicators_list.get(i).getFieldName_sinhala());
					ps.setString(5, indicators_list.get(i).getFieldName_tamil());
					ps.setString(6, indicators_list.get(i).getFieldType());
					ps.setString(7, indicators_list.get(i).getValidationMethod());
					ps.setInt(8, indicators_list.get(i).getFieldLength());
					if (indicators_list.get(i).getMandatoryField() == true) {
						ps.setString(9, "Y");
					} else if (indicators_list.get(i).getMandatoryField() == false) {
						ps.setString(9, "N");
					}
					if (indicators_list.get(i).getActive() == true) {
						ps.setString(10, "Y");
					} else if (indicators_list.get(i).getActive() == false) {
						ps.setString(10, "N");
					}
					if (indicators_list.get(i).getFieldType().equals("FT01")) {
						ps.setString(11, "Y");
					} else if (!indicators_list.get(i).getFieldType().equals("FT01")) {
						ps.setString(11, "N");
					}
					ps.setString(12, user);
					ps.setTimestamp(13, timestamp);
					ps.setString(14, indicators_list.get(i).getDisplayAfter());
					ps.setString(15, indicators_list.get(i).getFieldDefinition());
					ps.setInt(16, indicators_list.get(i).getDisplayOrder());
					ps.executeUpdate();

					if (indicators_list.get(i).getFieldType().equals("FT01")) {
						// save indicator values
						for (int j = 0; j < indicators_list.get(i).getIndicator_values_list().size(); j++) {
							long seqNo_indicator_values = Utils.getNextValBySeqName(con, "seq_nt_t_indicator_lov");
							String query3 = "INSERT INTO public.nt_t_indicator_lov (seqno, form_id, code, description, description_sinhala, description_tamil, created_by, created_date, indicators_seqno) "
									+ "VALUES (?,?,?,?,?,?,?,?,?);";

							ps = con.prepareStatement(query3);
							ps.setLong(1, seqNo_indicator_values);
							ps.setString(2, generateSurveyFormDTO.getFormID());
							ps.setString(3, indicators_list.get(i).getIndicator_values_list().get(j).getLOV_code());
							ps.setString(4,
									indicators_list.get(i).getIndicator_values_list().get(j).getLOV_description());
							ps.setString(5, indicators_list.get(i).getIndicator_values_list().get(j)
									.getLOV_description_sinhala());
							ps.setString(6, indicators_list.get(i).getIndicator_values_list().get(j)
									.getLOV_description_tamil());
							ps.setString(7, user);
							ps.setTimestamp(8, timestamp);
							ps.setLong(9, seqNo_indicator);
							ps.executeUpdate();
						}
					}
				}
			}

			con.commit();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(ps2);
			ConnectionManager.close(ps3);
			ConnectionManager.close(con);
		}
		return success;
	}

	@Override
	public List<GenerateSurveyFormDTO> getIndicatorsListFromTempID(String copyTemplateID) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<GenerateSurveyFormDTO> data = new ArrayList<GenerateSurveyFormDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT a.seqno, a.form_id, b.survey_no, a.field_name, a.field_name_sinhala, a.field_name_tamil, a.field_type, c.description as field_type_des, a.validation_method, "
					+ "d.description as validation_method_des, a.field_length, a.mandatory_field, "
					+ "a.active, a.lov_field, a.display_after, a.display_order, a.field_definition, e.description as field_definition_des FROM public.nt_t_indicators a inner join nt_m_form b on a.form_id=b.form_id inner join nt_r_field_type c on a.field_type = c.code "
					+ "left outer join nt_r_field_validation d on a.validation_method = d.code left outer join nt_r_field_definition e on a.field_definition = e.code WHERE a.form_id=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, copyTemplateID);
			rs = ps.executeQuery();
			GenerateSurveyFormDTO p;

			while (rs.next()) {
				p = new GenerateSurveyFormDTO();
				p.setSeqNo(rs.getLong("seqno"));
				p.setFieldName(rs.getString("field_name"));
				p.setFieldName_sinhala(rs.getString("field_name_sinhala"));
				p.setFieldName_tamil(rs.getString("field_name_tamil"));
				p.setFieldType(rs.getString("field_type"));
				p.setFieldType_des(rs.getString("field_type_des"));
				p.setValidationMethod(rs.getString("validation_method"));
				p.setValidationMethod_des(rs.getString("validation_method_des"));
				p.setFieldDefinition(rs.getString("field_definition"));
				p.setFieldDefinition_des(rs.getString("field_definition_des"));
				p.setFieldLength(rs.getInt("field_length"));
				p.setDisplayAfter(rs.getString("display_after"));
				p.setDisplayOrder(rs.getInt("display_order"));

				if (rs.getString("mandatory_field").equals("Y")) {
					p.setMandatoryField(true);
				} else if (rs.getString("mandatory_field").equals("N")) {
					p.setMandatoryField(false);
				}
				if (rs.getString("active").equals("Y")) {
					p.setActive(true);
				} else if (rs.getString("active").equals("N")) {
					p.setActive(false);
				}
				data.add(p);

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
	public String getTaskCode(String surveyNo) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String taskCode = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select tsd_task_code from public.nt_t_survey_task_det where tsd_survey_no = ?";

			ps = con.prepareStatement(query);
			ps.setString(1, surveyNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				taskCode = rs.getString("tsd_task_code");
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return taskCode;

	}
}
