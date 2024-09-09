package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.GrantApprovalforBusChangeDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class GrantApprovalforBusChangeServiceImpl implements GrantApprovalforBusChangeService {

	@Override
	public List<GrantApprovalforBusChangeDTO> getPermitNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GrantApprovalforBusChangeDTO> returnList = new ArrayList<GrantApprovalforBusChangeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_permit_no FROM public.nt_t_pm_application "
					+ "where pm_status='O' and pm_permit_no is not null and pm_permit_no !='' "
					+ "order by pm_permit_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				GrantApprovalforBusChangeDTO grantApprovalforBusChangeDTO = new GrantApprovalforBusChangeDTO();
				grantApprovalforBusChangeDTO.setPermitNo(rs.getString("pm_permit_no"));
				returnList.add(grantApprovalforBusChangeDTO);
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
	public List<GrantApprovalforBusChangeDTO> getApplicationNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GrantApprovalforBusChangeDTO> returnList = new ArrayList<GrantApprovalforBusChangeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_application_no FROM public.nt_t_pm_application "
					+ "where pm_status='O' and pm_application_no is not null and pm_application_no !='' "
					+ "order by pm_application_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				GrantApprovalforBusChangeDTO grantApprovalforBusChangeDTO = new GrantApprovalforBusChangeDTO();
				grantApprovalforBusChangeDTO.setApplicationNo(rs.getString("pm_application_no"));
				returnList.add(grantApprovalforBusChangeDTO);
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
	public List<GrantApprovalforBusChangeDTO> getServiceType() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<GrantApprovalforBusChangeDTO> returnList = new ArrayList<GrantApprovalforBusChangeDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT DISTINCT  s.description as serviceTypeDes, pm_service_type "
					+ "FROM public.nt_t_pm_application " + "inner join nt_r_service_types s "
					+ "on s.code=nt_t_pm_application.pm_service_type where pm_status='O' and pm_service_type is not null and pm_service_type !='' "
					+ "order by serviceTypeDes; ";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				GrantApprovalforBusChangeDTO grantApprovalforBusChangeDTO = new GrantApprovalforBusChangeDTO();
				grantApprovalforBusChangeDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				grantApprovalforBusChangeDTO.setServiceTypeDes(rs.getString("serviceTypeDes"));
				returnList.add(grantApprovalforBusChangeDTO);
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

}
