package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CodeDescriptionDTO;
import lk.informatics.ntc.model.dto.ValueTypeDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.view.util.ConnectionManager;

public class ReferenceDataService {

	public boolean isCodeDuplicate(String code, String tableName) throws ApplicationException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getConnection();
			String sql = "SELECT CODE FROM " + tableName + " WHERE CODE=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, code);
			rs = stmt.executeQuery();
			while (rs.next()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("SEARCH ERROR");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
	}

	public int saveRecord(CodeDescriptionDTO codeDescriptionDTO, String tableName, String username) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = ConnectionManager.getConnection();
			if (codeDescriptionDTO.isSearchedRecord() == false) {
				sql = "SELECT CODE FROM " + tableName
						+ " WHERE CODE=? OR DESCRIPTION=? OR  DESCRIPTION_SI=? OR DESCRIPTION_TA=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, codeDescriptionDTO.getCode());
				stmt.setString(2, codeDescriptionDTO.getDescription_english());
				stmt.setString(3, codeDescriptionDTO.getDescription_sinhala());
				stmt.setString(4, codeDescriptionDTO.getDescription_tamil());
				rs = stmt.executeQuery();
				while (rs.next()) {
					String code = rs.getString("CODE");
					if (code.equalsIgnoreCase(codeDescriptionDTO.getCode())) {
						return 1;
					} else {
						return 2;
					}
				}
			} else {
				sql = "SELECT CODE FROM " + tableName
						+ " WHERE  DESCRIPTION=? OR  DESCRIPTION_SI=? OR DESCRIPTION_TA=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, codeDescriptionDTO.getDescription_english());
				stmt.setString(2, codeDescriptionDTO.getDescription_sinhala());
				stmt.setString(3, codeDescriptionDTO.getDescription_tamil());
				rs = stmt.executeQuery();
				while (rs.next()) {
					String dbcode = rs.getString("CODE");
					if (!dbcode.equalsIgnoreCase(codeDescriptionDTO.getCode())) {
						return 2;
					}

				}
			}

			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (codeDescriptionDTO.isSearchedRecord() == false) {
				sql = "INSERT INTO " + tableName
						+ " (CODE,DESCRIPTION,DESCRIPTION_SI,DESCRIPTION_TA,ACTIVE,CREATED_BY,CREATED_DATE) VALUES (?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, codeDescriptionDTO.getCode());
				stmt.setString(2, codeDescriptionDTO.getDescription_english());
				stmt.setString(3, codeDescriptionDTO.getDescription_sinhala());
				stmt.setString(4, codeDescriptionDTO.getDescription_tamil());
				stmt.setString(5, codeDescriptionDTO.getStatus());
				stmt.setString(6, username);
				stmt.setTimestamp(7, new Timestamp(new Date().getTime()));
			} else {
				sql = "UPDATE " + tableName
						+ " SET DESCRIPTION=?,DESCRIPTION_SI=?,DESCRIPTION_TA=?,ACTIVE=?,modified_by=?,modified_date=? WHERE CODE=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, codeDescriptionDTO.getDescription_english());
				stmt.setString(2, codeDescriptionDTO.getDescription_sinhala());
				stmt.setString(3, codeDescriptionDTO.getDescription_tamil());
				stmt.setString(4, codeDescriptionDTO.getStatus());
				stmt.setString(5, username);
				stmt.setTimestamp(6, new Timestamp(new Date().getTime()));
				stmt.setString(7, codeDescriptionDTO.getCode());
			}
			stmt.executeUpdate();
			conn.commit();

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
	}

	public List<CodeDescriptionDTO> searchRecords(CodeDescriptionDTO codeDescriptionDTO, String tableName) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<CodeDescriptionDTO> data = new ArrayList<CodeDescriptionDTO>(0);
		try {
			conn = ConnectionManager.getConnection();
			String sql = "SELECT CODE,DESCRIPTION,DESCRIPTION_SI,DESCRIPTION_TA,ACTIVE FROM " + tableName;
			boolean codeexist = false;
			if (codeDescriptionDTO.getCode() != null && !codeDescriptionDTO.getCode().trim().equalsIgnoreCase("")) {
				sql = sql + " WHERE CODE LIKE ?";
				codeexist = true;
			}
			boolean edesexist = false;
			if (codeDescriptionDTO.getDescription_english() != null
					&& !codeDescriptionDTO.getDescription_english().trim().equalsIgnoreCase("")) {
				edesexist = true;
				if (codeexist) {
					sql = sql + " AND DESCRIPTION LIKE ?";
				} else {
					sql = sql + " WHERE DESCRIPTION LIKE ?";
				}
			}
			boolean sdesexist = false;
			if (codeDescriptionDTO.getDescription_sinhala() != null
					&& !codeDescriptionDTO.getDescription_sinhala().trim().equalsIgnoreCase("")) {
				sdesexist = true;
				if (codeexist || edesexist) {
					sql = sql + " AND DESCRIPTION_SI LIKE ?";
				} else {
					sql = sql + " WHERE DESCRIPTION_SI LIKE ?";
				}
			}
			boolean tdesexist = false;
			if (codeDescriptionDTO.getDescription_tamil() != null
					&& !codeDescriptionDTO.getDescription_tamil().trim().equalsIgnoreCase("")) {
				tdesexist = true;
				if (codeexist || edesexist || sdesexist) {
					sql = sql + " AND DESCRIPTION_TA LIKE ?";
				} else {
					sql = sql + " WHERE DESCRIPTION_TA LIKE ?";
				}
			}
			boolean statucexist = false;
			if (codeDescriptionDTO.getStatus() != null && !codeDescriptionDTO.getStatus().trim().equalsIgnoreCase("")) {
				statucexist = true;
				if (codeexist || edesexist || sdesexist || tdesexist) {
					sql = sql + " AND ACTIVE LIKE ?";
				} else {
					sql = sql + " WHERE ACTIVE LIKE ?";
				}
			}

			int count = 0;
			sql = sql + " ORDER BY CODE";
			stmt = conn.prepareStatement(sql);
			if (codeexist) {
				count += 1;
				stmt.setString(count, "%" + codeDescriptionDTO.getCode() + "%");
			}
			if (edesexist) {
				count += 1;
				stmt.setString(count, "%" + codeDescriptionDTO.getDescription_english() + "%");
			}
			if (sdesexist) {
				count += 1;
				stmt.setString(count, "%" + codeDescriptionDTO.getDescription_sinhala() + "%");
			}
			if (tdesexist) {
				count += 1;
				stmt.setString(count, "%" + codeDescriptionDTO.getDescription_tamil() + "%");
			}
			if (statucexist) {
				count += 1;
				stmt.setString(count, "%" + codeDescriptionDTO.getStatus() + "%");
			}

			rs = stmt.executeQuery();
			while (rs.next()) {
				CodeDescriptionDTO obj = new CodeDescriptionDTO();
				obj.setCode(rs.getString("CODE"));
				obj.setDescription_english(rs.getString("DESCRIPTION"));
				obj.setDescription_sinhala(rs.getString("DESCRIPTION_SI"));
				obj.setDescription_tamil(rs.getString("DESCRIPTION_TA"));
				obj.setStatus(rs.getString("ACTIVE"));
				if (obj.getStatus().equalsIgnoreCase("A")) {
					obj.setStatusDescription("ACTIVE");
				} else if (obj.getStatus().equalsIgnoreCase("I")) {
					obj.setStatusDescription("INACTIVE");
				} else {
					obj.setStatusDescription(obj.getStatus());
				}
				obj.setSearchedRecord(true);
				data.add(obj);
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return data;
	}

	public List<CodeDescriptionDTO> findAllRecords(String tableName) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<CodeDescriptionDTO> data = new ArrayList<CodeDescriptionDTO>(0);
		try {
			conn = ConnectionManager.getConnection();
			String sql = "SELECT CODE,DESCRIPTION,DESCRIPTION_SI,DESCRIPTION_TA,ACTIVE FROM " + tableName;
			sql = sql + " ORDER BY CODE";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				CodeDescriptionDTO obj = new CodeDescriptionDTO();
				obj.setCode(rs.getString("CODE"));
				obj.setDescription_english(rs.getString("DESCRIPTION"));
				obj.setDescription_sinhala(rs.getString("DESCRIPTION_SI"));
				obj.setDescription_tamil(rs.getString("DESCRIPTION_TA"));
				obj.setStatus(rs.getString("ACTIVE"));
				if (obj.getStatus().equalsIgnoreCase("A")) {
					obj.setStatusDescription("ACTIVE");
				} else if (obj.getStatus().equalsIgnoreCase("I")) {
					obj.setStatusDescription("INACTIVE");
				} else {
					obj.setStatusDescription(obj.getStatus());
				}
				obj.setSearchedRecord(true);
				data.add(obj);
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return data;
	}

	public int deleteRecord(CodeDescriptionDTO codeDescriptionDTO, String tableName) {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = null;
		try {
			conn = ConnectionManager.getConnection();
			sql = "DELETE FROM " + tableName + " WHERE CODE=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, codeDescriptionDTO.getCode());
			stmt.executeUpdate();
			conn.commit();

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
	}

	public boolean isValueTypeDuplicate(String value, String type, String tableName) throws ApplicationException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionManager.getConnection();
			String sql = "SELECT * FROM " + tableName + " WHERE VALUE=? AND TYPE = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, value);
			stmt.setString(2, type);
			rs = stmt.executeQuery();
			while (rs.next()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("SEARCH ERROR");
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
	}

	public int saveValueTypeRecord(ValueTypeDTO valueTypeDTO, String tableName, String username) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = ConnectionManager.getConnection();
			if (valueTypeDTO.isSearchedRecord() == false) {
				sql = "SELECT VALUE,TYPE FROM " + tableName + " WHERE VALUE = ? AND TYPE = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, valueTypeDTO.getValue());
				stmt.setString(2, valueTypeDTO.getType());
				rs = stmt.executeQuery();
				while (rs.next()) {
					String code = rs.getString("VALUE");
					if (code.equalsIgnoreCase(valueTypeDTO.getValue())) {
						return 1;
					} else {
						return 2;
					}
				}
			} else {
				sql = "SELECT VALUE,TYPE FROM " + tableName + " WHERE  TYPE=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, valueTypeDTO.getType());
				rs = stmt.executeQuery();
				while (rs.next()) {
					String dbValue = rs.getString("VALUE");
					if (!dbValue.equalsIgnoreCase(valueTypeDTO.getValue())) {
						return 2;
					}

				}
			}

			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (valueTypeDTO.isSearchedRecord() == false) {
				sql = "INSERT INTO " + tableName + " (VALUE,TYPE,CREATED_BY,CREATED_DATE) VALUES (?,?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, valueTypeDTO.getValue());
				stmt.setString(2, valueTypeDTO.getType());
				stmt.setString(3, username);
				stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
				stmt.executeUpdate();
				conn.commit();
			} else {
				/*
				 * sql="UPDATE "
				 * +tableName+" SET TYPE=?,modified_by=?,modified_date=? WHERE VALUE=? AND TYPE = ?"
				 * ; stmt=conn.prepareStatement(sql); stmt.setString(1, valueTypeDTO.getType());
				 * stmt.setString(2, username); stmt.setTimestamp(3, new Timestamp(new
				 * Date().getTime())); stmt.setString(4, valueTypeDTO.getValue());
				 */
			}

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
	}

	public List<ValueTypeDTO> searchValueTypeRecords(ValueTypeDTO valueTypeDTO, String tableName) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ValueTypeDTO> data = new ArrayList<ValueTypeDTO>(0);
		try {
			conn = ConnectionManager.getConnection();
			String sql = "SELECT VALUE,TYPE FROM " + tableName;
			boolean valueExist = false;
			if (valueTypeDTO.getValue() != null && !valueTypeDTO.getValue().trim().equalsIgnoreCase("")) {
				sql = sql + " WHERE VALUE LIKE ?";
				valueExist = true;
			}
			boolean typeExist = false;
			if (valueTypeDTO.getType() != null && !valueTypeDTO.getType().trim().equalsIgnoreCase("")) {
				typeExist = true;
				if (valueExist) {
					sql = sql + " AND TYPE LIKE ?";
				} else {
					sql = sql + " WHERE TYPE LIKE ?";
				}
			}
			sql = sql + " ORDER BY VALUE";
			stmt = conn.prepareStatement(sql);
			if (valueExist) {
				stmt.setString(1, "%" + valueTypeDTO.getValue() + "%");
			}
			if (typeExist) {
				stmt.setString(2, "%" + valueTypeDTO.getType() + "%");
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				ValueTypeDTO obj = new ValueTypeDTO();
				obj.setValue(rs.getString("VALUE"));
				obj.setType(rs.getString("TYPE"));
				obj.setSearchedRecord(true);
				data.add(obj);
			}
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
		return data;
	}

	public int deleteValueTypeRecord(ValueTypeDTO valueTypeDTO, String tableName) {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = null;
		try {
			conn = ConnectionManager.getConnection();
			sql = "DELETE FROM " + tableName + " WHERE VALUE=? AND TYPE = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, valueTypeDTO.getValue());
			stmt.setString(2, valueTypeDTO.getType());
			stmt.executeUpdate();
			conn.commit();

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(stmt);
			ConnectionManager.close(conn);
		}
	}

}
