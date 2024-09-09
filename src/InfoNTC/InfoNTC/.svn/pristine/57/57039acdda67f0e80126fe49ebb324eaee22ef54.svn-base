package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lk.informatics.ntc.model.dto.ExpiredPermitDTO;
import lk.informatics.ntc.view.util.ConnectionManager;

public class ExpiredPermitsApprovalServiceImpl implements ExpiredPermitsApprovalService {

	@Override
	public List<ExpiredPermitDTO> getPermitNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ExpiredPermitDTO> returnList = new ArrayList<ExpiredPermitDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_permit_no FROM public.nt_t_pm_application "
					+ "where pm_status='O' and pm_permit_no is not null and pm_permit_no !='' "
					+ "order by pm_permit_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ExpiredPermitDTO expiredPermitDTO = new ExpiredPermitDTO();
				expiredPermitDTO.setPermitNo(rs.getString("pm_permit_no"));
				returnList.add(expiredPermitDTO);
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
	public List<ExpiredPermitDTO> getApplicationNo() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ExpiredPermitDTO> returnList = new ArrayList<ExpiredPermitDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_application_no FROM public.nt_t_pm_application "
					+ "where pm_status='O' and pm_application_no is not null and pm_application_no !='' "
					+ "order by pm_application_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				ExpiredPermitDTO expiredPermitDTO = new ExpiredPermitDTO();
				expiredPermitDTO.setRenewalApplicationNo(rs.getString("pm_application_no"));
				returnList.add(expiredPermitDTO);
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
