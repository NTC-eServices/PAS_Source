package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.informatics.ntc.model.dto.CommonInquiryDTO;
import lk.informatics.ntc.model.dto.ComplaintActionDTO;
import lk.informatics.ntc.model.dto.ComplaintRequestDTO;
import lk.informatics.ntc.model.dto.DepartmentDTO;
import lk.informatics.ntc.model.dto.ManageInquiryDTO;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class GrievanceManagementDashboardServiceImpl implements GrievanceManagementDashboardService {

	@Override
	public List<DepartmentDTO> actionDepartmentList() {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<DepartmentDTO> departmentList = new ArrayList<DepartmentDTO>();

		try {

			con = ConnectionManager.getConnection();

			String sql = "SELECT code, description FROM public.nt_r_action_department WHERE status='A' ORDER BY description;";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				DepartmentDTO dto = new DepartmentDTO();
				dto.setCode(rs.getString("code"));
				dto.setDescription(rs.getString("description"));
				departmentList.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return departmentList;
	}

	@Override
	public List<ComplaintActionDTO> getComplaintActionData(String actionDepartmentCode, Date actionStartDate,
			Date actionEndDate) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<ComplaintActionDTO> complaintActionList = new ArrayList<ComplaintActionDTO>();

		String where = "";
		String fromDate = null;
		String toDate = null;

		try {
			con = ConnectionManager.getConnection();

			if (actionDepartmentCode != null && !actionDepartmentCode.isEmpty() && !actionDepartmentCode.equals("")) {
				where = " WHERE assigned_department_code='" + actionDepartmentCode + "'";
			}

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			if (actionStartDate != null && actionEndDate != null) {

				fromDate = df.format(actionStartDate);
				toDate = df.format(actionEndDate);

				where = where + " AND to_date(created_date,'dd/MM/yyyy') >= to_date('" + fromDate
						+ "','dd/MM/yyyy') AND to_date(created_date,'dd/MM/yyyy') <= to_date('" + toDate
						+ "','dd/MM/yyyy') ";

			}

			String sql = "SELECT A.*,B.description FROM public.nt_m_complain_action A INNER JOIN public.nt_r_action_department B ON A.assigned_department_code=B.code "
					+ where + " ORDER BY A.complaint_no DESC";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				ComplaintActionDTO dto = new ComplaintActionDTO();
				dto.setComplainNo(rs.getString("complaint_no"));
				dto.setDepartment(rs.getString("description"));
				dto.setDepartmentCode(rs.getString("assigned_department_code"));
				dto.setStatusCode(rs.getString("status"));

				if (rs.getString("status") != null && !rs.getString("status").trim().isEmpty()) {

					if (rs.getString("status").equalsIgnoreCase("O")) {

						dto.setStatus("Ongoing");
					} else if (rs.getString("status").equalsIgnoreCase("C")) {

						dto.setStatus("Closed");
					} else if (rs.getString("status").equalsIgnoreCase("P")) {

						dto.setStatus("Pending");
					}
				}

				dto.setAction(rs.getString("action"));

				dto.setActionDate(rs.getString("action_taken_date"));

				String action_completed_date = rs.getString("action_completed_date");
				if (action_completed_date != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date actionCompletedDate;
					try {
						actionCompletedDate = originalFormat.parse(action_completed_date);
						dto.setActionCompletedDate(actionCompletedDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}

				}

				complaintActionList.add(dto);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return complaintActionList;

	}

	@Override
	public void updateComplainAction(ComplaintActionDTO complaintAction, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;

		try {

			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String currentDate = dateFormat.format(timestamp);

			String query = "UPDATE public.nt_m_complain_action SET action_taken_date=?, action_taken_by=?, status =?, action=?, action_completed_date=?, action_taken_by_department=? "
					+ "WHERE complaint_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, currentDate);
			ps.setString(2, loginUser);
			ps.setString(3, complaintAction.getStatus());
			ps.setString(4, complaintAction.getAction());

			if (complaintAction.getActionCompletedDate() != null) {
				String actionCompletedDate = (dateFormat.format(complaintAction.getActionCompletedDate()));
				ps.setString(5, actionCompletedDate);
			} else {
				ps.setString(5, null);
			}

			ps.setString(6, complaintAction.getDepartmentCode());

			ps.setString(7, complaintAction.getComplainNo());
			ps.executeUpdate();

			con.commit();
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public void actionTaken(String loginUser, String complainNo, String actionDepartment, String action) {
		Connection con = null;
		PreparedStatement ps = null;

		try {

			con = ConnectionManager.getConnection();

			java.util.Date date = new java.util.Date();
			Timestamp timestamp = new Timestamp(date.getTime());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String currentDate = dateFormat.format(timestamp);

			// save data to nt_m_action_taken table
			long seqNoTaskTaken = Utils.getNextValBySeqName(con, "seq_nt_m_action_taken");

			String query = "INSERT INTO public.nt_m_action_taken "
					+ "(seq, complaint_no, department_code, action_taken_by_assigned_department, action_taken_date_by_assigned_department) "
					+ "VALUES(?,?,?,?,?); ";

			ps = con.prepareStatement(query);
			ps.setLong(1, seqNoTaskTaken);
			ps.setString(2, complainNo);
			ps.setString(3, actionDepartment);
			ps.setString(4, action);
			ps.setTimestamp(5, timestamp);
			ps.executeUpdate();

			con.commit();
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public void complainActionHistory(ComplaintActionDTO complaintAction, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		try {

			con = ConnectionManager.getConnection();

			String query = "INSERT into public.nt_h_complain_action  "
					+ " (SELECT * FROM public.nt_m_complain_action WHERE complaint_no = ?) ";

			ps = con.prepareStatement(query);
			ps.setString(1, complaintAction.getComplainNo());
			ps.executeUpdate();

			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public ComplaintActionDTO getComplaintActionDataByComplainNo(String complainNo) {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		ComplaintActionDTO dto = new ComplaintActionDTO();

		String where = "";

		try {
			con = ConnectionManager.getConnection();

			where = " WHERE complaint_no='" + complainNo + "'";

			String sql = "SELECT A.*,B.description FROM public.nt_m_complain_action A INNER JOIN public.nt_r_action_department B ON A.assigned_department_code=B.code "
					+ where + " ORDER BY A.complaint_no DESC";

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {

				dto.setComplainNo(rs.getString("complaint_no"));
				dto.setDepartment(rs.getString("description"));
				dto.setDepartmentCode(rs.getString("assigned_department_code"));
				dto.setStatusCode(rs.getString("status"));

				if (rs.getString("status") != null && !rs.getString("status").trim().isEmpty()) {

					if (rs.getString("status").equalsIgnoreCase("O")) {

						dto.setStatus("Ongoing");
					} else if (rs.getString("status").equalsIgnoreCase("C")) {

						dto.setStatus("Closed");
					} else if (rs.getString("status").equalsIgnoreCase("P")) {

						dto.setStatus("Pending");
					}
				}

				dto.setAction(rs.getString("action"));

				dto.setActionDate(rs.getString("action_taken_date"));

				String action_completed_date = rs.getString("action_completed_date");
				if (action_completed_date != null) {
					DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date actionCompletedDate;
					try {
						actionCompletedDate = originalFormat.parse(action_completed_date);
						dto.setActionCompletedDate(actionCompletedDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}

				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return dto;

	}
	
	
	private void insertTaskInquiryRecord(Connection con, ComplaintActionDTO  selectedComplaintAction,
			Timestamp timestamp, String status, String function,String user,String functiondes) {
		
		try {
			String insertSQL = null;
			PreparedStatement psInsert = null;

			insertSQL = "INSERT INTO public.nt_t_task_inquiry (created_by, created_date, status, complaint_no,function_name,function_des) "
					+ "VALUES(?, ?, ?, ?, ?, ?)";

			psInsert = con.prepareStatement(insertSQL);

			psInsert.setString(1, user);
			psInsert.setTimestamp(2, timestamp);
			psInsert.setString(3, status);
			psInsert.setString(4, selectedComplaintAction.getComplainNo());
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
	public void beanLinkMethod(ComplaintActionDTO  selectedComplaintAction,String user,String des,String funDes) {
		Connection con = null;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		try {
			con = ConnectionManager.getConnection();
			
			insertTaskInquiryRecord(con, selectedComplaintAction, timestamp, des, "Grievances Management",user,funDes);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public List<CommonInquiryDTO> searchDataForCommonInquiry(String complaintNo, String busNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps= null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();
		
		try {
			con = ConnectionManager.getConnection();

			if((complaintNo != null && busNo != "" && permitNo != "")) {
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Grievances Management' "
						+ "AND (complaint_no = ? AND bus_no = ? AND permit_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, complaintNo);
				ps.setString(2, busNo);
				ps.setString(3, permitNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setComplaintNo(rs.getString("complaint_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					
					dataList.add(data);
				}
			}else if((complaintNo != null && busNo != "")){
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Grievances Management' "
						+ "AND (complaint_no = ? AND bus_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, complaintNo);
				ps.setString(2, busNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setComplaintNo(rs.getString("complaint_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					
					dataList.add(data);
				}
				
			}else if((complaintNo != null && permitNo != "")) {
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Grievances Management' "
						+ "AND (complaint_no = ? AND permit_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, complaintNo);
				ps.setString(2, permitNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setComplaintNo(rs.getString("complaint_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					
					dataList.add(data);
				}
			}else {
				String sql = null;
				sql = "SELECT * FROM public.nt_t_task_inquiry WHERE function_name = 'Grievances Management' "
						+ "AND (complaint_no = ? OR bus_no = ? OR permit_no = ?)";
				
				ps = con.prepareStatement(sql);
				ps.setString(1, complaintNo);
				ps.setString(2, busNo);
				ps.setString(3, permitNo);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					CommonInquiryDTO data = new CommonInquiryDTO();
					
					data.setPermitNo(rs.getString("permit_no"));
					data.setBusNo(rs.getString("bus_no"));
					data.setComplaintNo(rs.getString("complaint_no"));
					data.setStatusDes(rs.getString("status"));
					data.setCreatedBy(rs.getString("created_by"));
					data.setCreatedDate(rs.getTimestamp("created_date"));
					data.setFunctionDes(rs.getString("function_des"));
					
					dataList.add(data);
				}
				
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		
		return dataList;
	}
	
	@Override
	public List<CommonInquiryDTO> getComNoListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT complaint_no FROM public.nt_t_task_inquiry WHERE function_name = 'Grievances Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				String complaintNo = rs.getString("complaint_no");
				if (complaintNo != null && !complaintNo.isEmpty()) {
			        data.setComplaintNo(complaintNo);
			        dataList.add(data);
			    }
				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		
		return dataList;
	}
	
	@Override
	public List<CommonInquiryDTO> getBusNoListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT bus_no FROM public.nt_t_task_inquiry WHERE function_name = 'Grievances Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				if(rs.getString("bus_no") != null && !rs.getString("bus_no").isEmpty()) {
					data.setBusNo(rs.getString("bus_no"));	
					dataList.add(data);
				}
				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		
		return dataList;
	}
	
	@Override
	public List<CommonInquiryDTO> getPermitNoListForCommonInquiry(){
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CommonInquiryDTO> dataList = new ArrayList<CommonInquiryDTO>();
		
		try {
			con = ConnectionManager.getConnection();
			
			String sql = null;
			sql = "SELECT DISTINCT permit_no FROM public.nt_t_task_inquiry WHERE function_name = 'Grievances Management'";
			
			ps = con.prepareStatement(sql);
			
			rs = ps.executeQuery();
		
			while(rs.next()) {
				CommonInquiryDTO data = new CommonInquiryDTO();
				if(rs.getString("permit_no") != null && !rs.getString("permit_no").isEmpty()) {
					data.setPermitNo(rs.getString("permit_no"));	
					dataList.add(data);
				}
				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		
		
		return dataList;
	}
	
	
	


}
