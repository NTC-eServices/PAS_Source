package lk.informatics.ntc.model.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import lk.informatics.ntc.model.dto.InvestigationReportDTO;
import lk.informatics.ntc.model.dto.OffenceManagementDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class OffenceManagementServiceImpl implements OffenceManagementService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8504104780343288049L;

	@Override
	public List<OffenceManagementDTO> getExistingOffenceCode() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<OffenceManagementDTO> offenceCodeList = new ArrayList<OffenceManagementDTO>();
		try {
			con = ConnectionManager.getConnection();

			String query = "select ofm_offence_code,ofm_offence_desc,ofm_offence_desc_tamil,ofm_offence_desc_sinhala from public.nt_m_offence_management where status='A'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				OffenceManagementDTO offenceCode = new OffenceManagementDTO();
				offenceCode.setOffenceCode(rs.getString("ofm_offence_code"));
				offenceCode.setOffenceDes(rs.getString("ofm_offence_desc"));
				offenceCode.setOffenceDesSin(rs.getString("ofm_offence_desc_sinhala"));
				offenceCode.setOffenceDesTamil(rs.getString("ofm_offence_desc_tamil"));

				offenceCodeList.add(offenceCode);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return offenceCodeList;
	}

	@Override
	public OffenceManagementDTO getDescriptionForCodes(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OffenceManagementDTO offenceCode1 = new OffenceManagementDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "select ofm_offence_desc,ofm_offence_desc_tamil,ofm_offence_desc_sinhala from public.nt_m_offence_management where ofm_offence_code=? ";
			ps = con.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				offenceCode1.setOffenceDes(rs.getString("ofm_offence_desc"));
				offenceCode1.setOffenceDesSin(rs.getString("ofm_offence_desc_sinhala"));
				offenceCode1.setOffenceDesTamil(rs.getString("ofm_offence_desc_tamil"));
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return offenceCode1;
	}

	@Override
	public List<OffenceManagementDTO> getOffenceCodeDataForGrid() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OffenceManagementDTO> data = new ArrayList<OffenceManagementDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "select ofm_offence_code,status,ofm_offence_desc,ofm_offence_desc_tamil,ofm_offence_desc_sinhala from public.nt_m_offence_management order by ofm_seq";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			OffenceManagementDTO dto;

			while (rs.next()) {
				dto = new OffenceManagementDTO();
				dto.setOffenceCode(rs.getString("ofm_offence_code"));
				if (rs.getString("status").equals("A")) {
					dto.setOffenceCodeStatus("Active");
				} else if (rs.getString("status").equals("I")) {
					dto.setOffenceCodeStatus("Inactive");
				}
				dto.setOffenceDes(rs.getString("ofm_offence_desc"));
				dto.setOffenceDesSin(rs.getString("ofm_offence_desc_sinhala"));
				dto.setOffenceDesTamil(rs.getString("ofm_offence_desc_tamil"));
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
	public String generateOffenceCode() {

		String last = retrieveLastNoForNumberGeneration("OFM");
		long no = Long.parseLong(last.substring(3));
		String next = String.valueOf((no + 1));
		String appNo = "OFM" + StringUtils.leftPad(next, 5, "0");

		return appNo;
	}

	@Override
	public void insertOffenceCodeDesriptionDet(OffenceManagementDTO offenceMgtDto, String loginUser) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_offence_management");

			String sql = "INSERT INTO public.nt_m_offence_management"
					+ "(ofm_seq, ofm_offence_code, ofm_offence_desc, ofm_offence_desc_tamil, ofm_offence_desc_sinhala, ofm_created_by, ofm_created_date,status)"
					+ "VALUES(?,?, ?,?,?,?, ?,?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, offenceMgtDto.getOffenceCode());
			stmt.setString(3, offenceMgtDto.getOffenceDes());
			stmt.setString(4, offenceMgtDto.getOffenceDesTamil());
			stmt.setString(5, offenceMgtDto.getOffenceDesSin());
			stmt.setString(6, loginUser);
			stmt.setTimestamp(7, timestamp);
			stmt.setString(8, offenceMgtDto.getOffenceCodeStatus());

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
	public void updateOffenceCodeInNumberGenTable(String code, String loginUser) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_r_number_generation" + " SET  app_no=?,  modified_by=?, modified_date=?"
					+ " WHERE code=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, code);

			stmt.setString(2, loginUser);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, "OFM");

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
	public String retrieveLastNoForNumberGeneration(String code) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String appNo = null;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT app_no FROM nt_r_number_generation WHERE code=?";
			ps = con.prepareStatement(sql1);

			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				appNo = rs.getString("app_no");

			}

			if (ps != null) {
				ps.close();
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}

		return appNo;
	}

	@Override
	public boolean deleteSelectedOffenceCode(String offenceCode) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDeleted = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM  public.nt_m_offence_management" + " WHERE ofm_offence_code=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, offenceCode);

			int i = stmt.executeUpdate();

			if (i > 0) {
				isDeleted = true;
			} else {
				isDeleted = false;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isDeleted;

	}

	@Override
	public void updateEditDetails(OffenceManagementDTO offenceDto, String loginUser) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_m_offence_management"
					+ " SET  ofm_offence_desc=?, ofm_offence_desc_tamil=?, ofm_offence_desc_sinhala=?,  ofm_modified_by=?, ofm_modified_date=?, status=?"
					+ " WHERE ofm_offence_code=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, offenceDto.getOffenceDes());
			stmt.setString(2, offenceDto.getOffenceDesTamil());
			stmt.setString(3, offenceDto.getOffenceDesSin());
			stmt.setString(4, loginUser);
			stmt.setTimestamp(5, timestamp);
			stmt.setString(6, offenceDto.getOffenceCodeStatus());
			stmt.setString(7, offenceDto.getOffenceCode());

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
	public void insertOffenceDetailInDetTable(OffenceManagementDTO offenceDto, String loginUser, String code) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_offence_management_det");

			String sql = "INSERT INTO public.nt_t_offence_management_det\r\n"
					+ "(omd_seq,omd_offence_code, omd_attempt_code, omd_is_public_complain, omd_is_investigation, \r\n"
					+ "omd_is_amount, omd_is_action_take, omd_no_of_demerit_points, omd_charge_amount, omd_action,\r\n"
					+ "omd_staus, omd_created_by, omd_created_date)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?);\r\n" + "";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, code);
			stmt.setString(3, offenceDto.getNoOfAttempts());
			if (offenceDto.getPublicComplain().equals("true")) {
				stmt.setString(4, "Y");
			} else {

				stmt.setString(4, "N");
			}
			if (offenceDto.getInvestigation().equals("true")) {
				stmt.setString(5, "Y");
			} else {
				stmt.setString(5, "N");
			}
			if (offenceDto.getAmount().equals("true")) {
				stmt.setString(6, "Y");
			} else {
				stmt.setString(6, "N");
			}
			if (offenceDto.getAction().equals("true")) {
				stmt.setString(7, "Y");
			} else {
				stmt.setString(7, "N");
			}
			if(offenceDto.getNoOfDermitPoints() ==null) {
				stmt.setInt(8, 0);
			}else {
			stmt.setInt(8, offenceDto.getNoOfDermitPoints());
			}
			if(offenceDto.getNoOfDermitPoints() ==null) {
			stmt.setInt(9, 0);
			}
			else {
				stmt.setInt(9, offenceDto.getChargeAmount());
			}
			stmt.setString(10, offenceDto.getActionTxt());
			stmt.setString(11, offenceDto.getStatus());
			stmt.setString(12, loginUser);
			stmt.setTimestamp(13, timestamp);

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
	public List<OffenceManagementDTO> getOffenceDetailOnSecondGrid(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OffenceManagementDTO> data = new ArrayList<OffenceManagementDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT omd_attempt_code,public.nt_r_attempts.description , omd_is_public_complain,\r\n"
					+ "omd_is_investigation, omd_is_amount, omd_is_action_take, omd_no_of_demerit_points,\r\n"
					+ "omd_charge_amount, omd_action, omd_staus\r\n" + "FROM public.nt_t_offence_management_det\r\n"
					+ "inner join public.nt_r_attempts on  public.nt_r_attempts.code=omd_attempt_code\r\n"
					+ "where omd_offence_code=?\r\n" + "and public.nt_r_attempts.active='A'\r\n" + "order by omd_seq;";

			ps = con.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();

			OffenceManagementDTO dto;

			while (rs.next()) {
				dto = new OffenceManagementDTO();
				dto.setNoOfAttemptsDes(rs.getString("description"));
				dto.setNoOfAttempts(rs.getString("omd_attempt_code"));
				if (rs.getString("omd_is_amount") != null) {
					if (rs.getString("omd_is_amount").equals("Y")) {
						dto.setIsAmountAllowed("YES");
						dto.setAmount("true");
					} else if (rs.getString("omd_is_amount").equals("N")) {
						dto.setIsAmountAllowed("NO");
						dto.setAmount("false");
					}

					else {
						dto.setIsAmountAllowed("");

					}
				}
				if (rs.getString("omd_is_action_take") != null) {
					if (rs.getString("omd_is_action_take").equals("Y")) {
						dto.setIsActionAllowed("YES");
						dto.setAction("true");
					} else if (rs.getString("omd_is_action_take").equals("N")) {
						dto.setIsActionAllowed("NO");
						dto.setAction("false");
					}

					else {
						dto.setIsAmountAllowed("");

					}
				}

				if (rs.getString("omd_is_investigation") != null) {
					if (rs.getString("omd_is_investigation").equals("Y")) {
						dto.setIsInvestigation("YES");
						dto.setInvestigation("true");
					} else if (rs.getString("omd_is_investigation").equals("N")) {
						dto.setIsInvestigation("No");
						dto.setInvestigation("false");
					}

					else {
						dto.setIsInvestigation("");

					}
				}

				if (rs.getString("omd_is_public_complain") != null) {
					if (rs.getString("omd_is_public_complain").equals("Y")) {
						dto.setIsPublicComplain("YES");
						dto.setPublicComplain("true");
					} else if (rs.getString("omd_is_public_complain").equals("N")) {
						dto.setIsPublicComplain("No");
						dto.setPublicComplain("false");
					}

					else {
						dto.setIsPublicComplain("");

					}
				}

				dto.setChargeAmount(rs.getInt("omd_charge_amount"));
				dto.setActionTxt(rs.getString("omd_action"));
				dto.setNoOfDermitPoints(rs.getInt("omd_no_of_demerit_points"));
				if (rs.getString("omd_staus").equals("A")) {
					dto.setStatus("Active");
				} else {
					dto.setStatus("Inactive");

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
	public void updateOffenceDetailInDetTable(OffenceManagementDTO offenceMangDTO, String loginUser,
			String selectedNoOFAttempt, String offenceCode) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_offence_management_det\r\n"
					+ "SET omd_attempt_code=?, omd_is_public_complain=?, omd_is_investigation=?, omd_is_amount=?,\r\n"
					+ "omd_is_action_take=?, omd_no_of_demerit_points=?, omd_charge_amount=?, omd_action=?,\r\n"
					+ "omd_staus=?,  omd_modified_by=?, omd_modified_date=?\r\n"
					+ "WHERE omd_offence_code=? and omd_attempt_code=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, offenceMangDTO.getNoOfAttempts());
			if (offenceMangDTO.getPublicComplain().equals("true")) {
				stmt.setString(2, "Y");
			} else {
				stmt.setString(2, "N");
			}
			if (offenceMangDTO.getInvestigation().equals("true")) {
				stmt.setString(3, "Y");
			} else {
				stmt.setString(3, "N");
			}
			if (offenceMangDTO.getAmount().equals("true")) {
				stmt.setString(4, "Y");
			} else {
				stmt.setString(4, "N");
			}
			if (offenceMangDTO.getAction().equals("true")) {
				stmt.setString(5, "Y");
			} else {
				stmt.setString(5, "N");
			}
			stmt.setInt(6, offenceMangDTO.getNoOfDermitPoints());
			stmt.setInt(7, offenceMangDTO.getChargeAmount());
			stmt.setString(8, offenceMangDTO.getActionTxt());

			stmt.setString(9, offenceMangDTO.getStatus());
			stmt.setString(10, loginUser);
			stmt.setTimestamp(11, timestamp);
			stmt.setString(12, offenceCode);
			stmt.setString(13, selectedNoOFAttempt);

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
	public List<String> getNoOfAttemptForCheckDuplicates(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> data = new ArrayList<String>();

		try {

			con = ConnectionManager.getConnection();

			String query = "select omd_attempt_code from public.nt_t_offence_management_det\r\n"
					+ "where omd_offence_code=? and omd_staus='A'";

			ps = con.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				String NoOfAttempt = rs.getString("omd_attempt_code");

				data.add(NoOfAttempt);

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
	public List<OffenceManagementDTO> getAttemptsListDropDown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<OffenceManagementDTO> noOfAttempsList = new ArrayList<OffenceManagementDTO>();
		try {
			con = ConnectionManager.getConnection();
			String query = "select code ,description from public.nt_r_attempts where active='A'";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				OffenceManagementDTO attempsDTO = new OffenceManagementDTO();
				attempsDTO.setNoOfAttempts(rs.getString("code"));
				attempsDTO.setNoOfAttemptsDes(rs.getString("description"));

				noOfAttempsList.add(attempsDTO);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return noOfAttempsList;
	}

	@Override
	public List<OffenceManagementDTO> getOffenceCodeDataForGridForDropDown(String code) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<OffenceManagementDTO> data = new ArrayList<OffenceManagementDTO>();

		try {

			con = ConnectionManager.getConnection();

			String query = "select ofm_offence_code,status,ofm_offence_desc,ofm_offence_desc_tamil,ofm_offence_desc_sinhala from public.nt_m_offence_management where ofm_offence_code=? order by ofm_seq";

			ps = con.prepareStatement(query);
			ps.setString(1, code);
			rs = ps.executeQuery();

			OffenceManagementDTO dto;

			while (rs.next()) {
				dto = new OffenceManagementDTO();
				dto.setOffenceCode(rs.getString("ofm_offence_code"));
				if (rs.getString("status").equals("A")) {
					dto.setOffenceCodeStatus("Active");
				} else if (rs.getString("status").equals("I")) {
					dto.setOffenceCodeStatus("Inactive");
				}
				dto.setOffenceDes(rs.getString("ofm_offence_desc"));
				dto.setOffenceDesSin(rs.getString("ofm_offence_desc_sinhala"));
				dto.setOffenceDesTamil(rs.getString("ofm_offence_desc_tamil"));
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

	public String checkDuplicate(String offenceCode) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String strCode = null;

		try {
			con = ConnectionManager.getConnection();

			String sql1 = "SELECT ofm_offence_code FROM public.nt_m_offence_management WHERE ofm_offence_code= ? ";
			ps = con.prepareStatement(sql1);

			ps.setString(1, offenceCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				strCode = rs.getString("ofm_offence_code");

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return strCode;
	}

	@Override
	public List<String> getActiveOffences() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
        String offenceCode = null;
        List<String> codeList = new ArrayList<>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select ofm_offence_code from public.nt_m_offence_management where status='A' and ofm_offence_code is not null and ofm_offence_code !='' ";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				offenceCode = rs.getString("ofm_offence_code");
		     	codeList.add(offenceCode);

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return codeList;
	}

	@Override
	public void createTempTable(String offence, String startDate, String endDate) {
		Connection con = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps4 = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		PreparedStatement stmt4 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		String  query1 ,query2,query3,query4 =null;
		InvestigationReportDTO completeOffenceDTO = new InvestigationReportDTO() ;
   
		List<InvestigationReportDTO> completeOffenceCountList = new ArrayList<InvestigationReportDTO>();
		List<InvestigationReportDTO> completeOffenceList = new ArrayList<InvestigationReportDTO>();
		List<InvestigationReportDTO> closeOffenceCountList = new ArrayList<InvestigationReportDTO>();
		List<InvestigationReportDTO> closeOffenceAmountList = new ArrayList<InvestigationReportDTO>();
		try {
			con = ConnectionManager.getConnection();
			
			if(offence != null && !offence.trim().isEmpty()
					&& startDate == null && endDate ==null) {
			 query1 = "SELECT ofm_offence_code FROM public.nt_m_offence_management where status ='A' and ofm_offence_code is not null and ofm_offence_code != ''\r\n" + 
					"and ofm_offence_code = ?\r\n" + 
					"order by ofm_offence_code ";
			 
			   ps1 = con.prepareStatement(query1);
                ps1.setString(1, offence);
				rs1 = ps1.executeQuery();
			}
			
			if(offence == null || offence.trim().isEmpty()
					&& startDate != null && endDate !=null) {
			 query1 = "SELECT ofm_offence_code FROM public.nt_m_offence_management where status ='A' and ofm_offence_code is not null and ofm_offence_code != ''\r\n" + 
			 		  "order by ofm_offence_code  ";
			 
			   ps1 = con.prepareStatement(query1);
                
				rs1 = ps1.executeQuery();
			}
			
			if(offence != null && !offence.trim().isEmpty()
					&& startDate != null && endDate !=null) {
			 query1 = "SELECT ofm_offence_code FROM public.nt_m_offence_management where status ='A' and ofm_offence_code is not null and ofm_offence_code != ''\r\n" + 
			 		"and ofm_offence_code = ?\r\n"+
			 		"order by ofm_offence_code  ";
			 
			   ps1 = con.prepareStatement(query1);
                ps1.setString(1, offence);
                
				rs1 = ps1.executeQuery();
			}
			

			while (rs1.next()) {
			 completeOffenceDTO = new InvestigationReportDTO() ;
			 completeOffenceDTO.setOffenceCode(rs1.getString("ofm_offence_code")); 
			 completeOffenceList.add(completeOffenceDTO);

			}
// insert first column
			
			for(InvestigationReportDTO s :completeOffenceList) {
			String sql = "INSERT INTO public.nt_temp_investigation_report\r\n" + 
					"(inv_offence_type_code)\r\n" + 
					"VALUES(?);";

			stmt1 = con.prepareStatement(sql);

			stmt1.setString(1, s.getOffenceCode());
			stmt1.executeUpdate();
			}
			con.commit();
		
			if(offence != null && !offence.trim().isEmpty()
					&& startDate == null && endDate ==null) {
				
				 query2 = "select count(offence_type_code) as completeCount,offence_type_code  from public.nt_t_grievance_commited_offence\r\n" + 
							"where is_applicable  ='Y' and offence_type_code=? \r\n" + 
							"group by offence_type_code\r\n" + 
							"order by offence_type_code; ";
					ps2 = con.prepareStatement(query2);
                    ps2.setString(1, offence);
					rs2 = ps2.executeQuery();

			}
			
			if(offence == null || offence.trim().isEmpty()
					&& startDate != null && endDate !=null) {

				 query2 = "select count(offence_type_code) as completeCount,offence_type_code  from public.nt_t_grievance_commited_offence\r\n" + 
							"where is_applicable  ='Y'  and to_char(created_date ,'yyyy-MM-dd')>=?\r\n" + 
							"and to_char(created_date ,'yyyy-MM-dd')<=? \r\n" + 
							"group by offence_type_code\r\n" + 
							"order by offence_type_code; ";
					ps2 = con.prepareStatement(query2);
                   ps2.setString(1, startDate);
                   ps2.setString(2, endDate);
             
					rs2 = ps2.executeQuery();
			}
			
			if(offence != null && !offence.trim().isEmpty()
					&& startDate != null && endDate !=null) {

				 query2 = "select count(offence_type_code) as completeCount,offence_type_code  from public.nt_t_grievance_commited_offence\r\n" + 
							"where is_applicable  ='Y'  and to_char(created_date ,'yyyy-MM-dd')>=?\r\n" + 
							"and to_char(created_date ,'yyyy-MM-dd')<=? and offence_type_code=? \r\n" + 
							"group by offence_type_code\r\n" + 
							"order by offence_type_code; ";
					ps2 = con.prepareStatement(query2);
                  ps2.setString(1, startDate);
                  ps2.setString(2, endDate);
                  ps2.setString(3, offence);
					rs2 = ps2.executeQuery();
			}
			
			while (rs2.next()) {
			 completeOffenceDTO = new InvestigationReportDTO() ;
			 completeOffenceDTO.setOffenceCode(rs2.getString("offence_type_code")); 
			 completeOffenceDTO.setComplteOffenceCodeCount(rs2.getString("completeCount"));
			 completeOffenceCountList.add(completeOffenceDTO);

			}
			for(InvestigationReportDTO s2 :completeOffenceCountList) {
			String sql2 = "UPDATE public.nt_temp_investigation_report\r\n"
					+ "SET inv_complete_offence_count =?\r\n"
					+ "WHERE inv_offence_type_code =? ;";

			stmt2 = con.prepareStatement(sql2);

			stmt2.setString(1, s2.getComplteOffenceCodeCount());
			stmt2.setString(2, s2.getOffenceCode());
		
			stmt2.executeUpdate();
			}
			con.commit();
			
				
				
				if(offence != null && !offence.trim().isEmpty()
						&& startDate == null && endDate ==null) {
					
					 query3 = "select count(offence_type_code) as closedCount,offence_type_code  from public.nt_t_grievance_commited_offence o\r\n" + 
						 		"left outer join   public.nt_m_complain_request c on c.seq =o.complain_seq \r\n" + 
						 		"where o.is_applicable  ='Y' and c.process_status ='C' and offence_type_code= ?\r\n" + 
						 		"group by offence_type_code ";
							ps3 = con.prepareStatement(query3);
                            ps3.setString(1,offence );
							rs3 = ps3.executeQuery();

				}
				
				if(offence == null || offence.trim().isEmpty()
						&& startDate != null && endDate !=null) {
					
					
					 query3 = "select count(offence_type_code) as closedCount,offence_type_code  from public.nt_t_grievance_commited_offence o\r\n" + 
						 		"left outer join   public.nt_m_complain_request c on c.seq =o.complain_seq \r\n" + 
						 		"where o.is_applicable  ='Y' and c.process_status ='C' and to_char(o.created_date ,'yyyy-MM-dd')>=?\r\n" + 
						 		"and to_char(o.created_date ,'yyyy-MM-dd')<=? \r\n" + 
						 		"group by offence_type_code ";
							ps3 = con.prepareStatement(query3);
							  ps3.setString(1, startDate);
			                   ps3.setString(2, endDate);
							rs3 = ps3.executeQuery();

				
				}
				
				if(offence != null && !offence.trim().isEmpty()
						&& startDate != null && endDate !=null) {

					 query3 = "select count(offence_type_code) as closedCount,offence_type_code  from public.nt_t_grievance_commited_offence o\r\n" + 
						 		"left outer join   public.nt_m_complain_request c on c.seq =o.complain_seq \r\n" + 
						 		"where o.is_applicable  ='Y' and c.process_status ='C' and to_char(o.created_date ,'yyyy-MM-dd')>=?\r\n" + 
						 		"and to_char(o.created_date ,'yyyy-MM-dd')<=? and offence_type_code= ?\r\n" + 
						 		"group by offence_type_code ";
							ps3 = con.prepareStatement(query3);
							  ps3.setString(1, startDate);
			                   ps3.setString(2, endDate);
			                   ps3.setString(3,offence );
							rs3 = ps3.executeQuery();
				}
				
	
				while (rs3.next()) {
				 completeOffenceDTO = new InvestigationReportDTO() ;
				 completeOffenceDTO.setOffenceCode(rs3.getString("offence_type_code")); 
				 completeOffenceDTO.setClosedOffenceCodeCount(rs3.getString("closedCount"));
				 closeOffenceCountList.add(completeOffenceDTO);

				}
				for(InvestigationReportDTO s3 :closeOffenceCountList) {
				String sql3 = "UPDATE public.nt_temp_investigation_report\r\n"
						+ "SET inv_closed_offence_count  =?\r\n"
						+ "WHERE inv_offence_type_code =? ;";

				stmt3 = con.prepareStatement(sql3);

				stmt3.setString(1, s3.getClosedOffenceCodeCount());
				stmt3.setString(2, s3.getOffenceCode());
			
				stmt3.executeUpdate();
				}
				con.commit();

				
			

				if(offence != null && !offence.trim().isEmpty()
						&& startDate == null && endDate ==null) {
					
					 query4 = "select offence_type_code,sum(v.rec_total_fee) as ammount  from public.nt_t_grievance_commited_offence o\r\n" + 
						 		"inner join   public.nt_m_complain_request c on c.seq =o.complain_seq \r\n" + 
						 		"inner join  public.nt_t_grievance_chargesheet_receipt v on v.rec_complaint_no  =c.complain_no and v.rec_receipt_no is not null\r\n" + 
						 		"where o.is_applicable  ='Y' and c.process_status ='C' and offence_type_code= ?\r\n" + 
						 		"group by offence_type_code\r\n" + 
						 		"order by offence_type_code; ";
							ps4 = con.prepareStatement(query4);
                            ps4.setString(1,offence);
							rs4 = ps4.executeQuery();

				}
				
				if(offence == null || offence.trim().isEmpty()
						&& startDate != null && endDate !=null) {
					
	
							
							 query4 = "select offence_type_code,sum(v.rec_total_fee) as ammount  from public.nt_t_grievance_commited_offence o\r\n" + 
								 		"inner join   public.nt_m_complain_request c on c.seq =o.complain_seq \r\n" + 
								 		"inner join  public.nt_t_grievance_chargesheet_receipt v on v.rec_complaint_no  =c.complain_no and v.rec_receipt_no is not null\r\n" + 
								 		"where o.is_applicable  ='Y' and c.process_status ='C' and to_char(o.created_date ,'yyyy-MM-dd')>=?\r\n" + 
								 		"and to_char(o.created_date ,'yyyy-MM-dd')<=?  \r\n" + 
								 		"group by offence_type_code\r\n" + 
								 		"order by offence_type_code; ";
									ps4 = con.prepareStatement(query4);
									  ps4.setString(1, startDate);
					                   ps4.setString(2, endDate);
					                 
									rs4 = ps4.executeQuery();

				
				}
				
				if(offence != null && !offence.trim().isEmpty()
						&& startDate != null && endDate !=null) {

					
					 query4 = "select offence_type_code,sum(v.rec_total_fee) as ammount  from public.nt_t_grievance_commited_offence o\r\n" + 
						 		"inner join   public.nt_m_complain_request c on c.seq =o.complain_seq \r\n" + 
						 		"inner join  public.nt_t_grievance_chargesheet_receipt v on v.rec_complaint_no  =c.complain_no and v.rec_receipt_no is not null\r\n" + 
						 		"where o.is_applicable  ='Y' and c.process_status ='C' and to_char(o.created_date ,'yyyy-MM-dd')>=?\r\n" + 
						 		"and to_char(o.created_date ,'yyyy-MM-dd')<=?  and offence_type_code= ?\r\n" + 
						 		"group by offence_type_code\r\n" + 
						 		"order by offence_type_code; ";
							ps4 = con.prepareStatement(query4);
							  ps4.setString(1, startDate);
			                   ps4.setString(2, endDate);
			                   ps4.setString(3,offence);
							rs4 = ps4.executeQuery();
				}
				
	
			
		

					while (rs4.next()) {
					 completeOffenceDTO = new InvestigationReportDTO() ;
					 completeOffenceDTO.setOffenceCode(rs4.getString("offence_type_code")); 
					 completeOffenceDTO.setReceiptAmmount(rs4.getInt("ammount"));
					 closeOffenceAmountList.add(completeOffenceDTO);

					}
					for(InvestigationReportDTO s4 :closeOffenceAmountList) {
					String sql4 = "UPDATE public.nt_temp_investigation_report\r\n"
							+ "SET inv_ammount  =?\r\n"
							+ "WHERE inv_offence_type_code =? ;";

					stmt4 = con.prepareStatement(sql4);

					stmt4.setInt(1, s4.getReceiptAmmount());
					stmt4.setString(2, s4.getOffenceCode());
				
					stmt4.executeUpdate();
					}
					con.commit();

				
			
		
			
			

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(stmt4);
			ConnectionManager.close(stmt3);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(stmt1);
			
			ConnectionManager.close(rs1);
			ConnectionManager.close(ps1);
			ConnectionManager.close(rs2);
			ConnectionManager.close(ps2);
			ConnectionManager.close(rs3);
			ConnectionManager.close(ps3);
			ConnectionManager.close(rs4);
			ConnectionManager.close(ps4);
			ConnectionManager.close(con);
		}
		
	}

	@Override
	public void deleteInvestigationData() {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String sql = "DELETE FROM  public.nt_temp_investigation_report ;";

			stmt = con.prepareStatement(sql);
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
	
	private void insertTaskInquiryRecord(Connection con, String code,
			Timestamp timestamp, String status, String function,String user,String funDes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, ofm_offence_code,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, code);
			psInsert.setString(5, function);
			psInsert.setString(6, function);

			psInsert.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void beanLinkMethod(String code,String user,String status) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, code, timestamp, status, "Investigation Management", user," Investigation Report");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
