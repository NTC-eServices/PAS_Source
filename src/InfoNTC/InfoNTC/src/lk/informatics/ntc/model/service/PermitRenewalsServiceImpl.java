package lk.informatics.ntc.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedProperty;

import lk.informatics.ntc.model.dto.CommonDTO;
import lk.informatics.ntc.model.dto.OminiBusDTO;
import lk.informatics.ntc.model.dto.PermitRenewalsDTO;
import lk.informatics.ntc.view.beans.SessionBackingBean;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.roc.utils.common.Utils;

public class PermitRenewalsServiceImpl implements PermitRenewalsService {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@Override
	public List<PermitRenewalsDTO> getAllPermitNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_permit_no, pm_queue_no FROM public.nt_t_pm_application where pm_status='O' and pm_permit_no is not null and pm_permit_no !='' and  pm_isnew_permit='Y' order by pm_permit_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setQueueNo(rs.getString("pm_queue_no"));
				returnList.add(permitRenewalsDTO);

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
	public PermitRenewalsDTO getSearchedDetailsWithPermitNoOrQueueNo(String selectedPermitNo, String queueNo,
			String selectedApplicationNo, String currentStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		Date todaysDate = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String currentDateString = df.format(todaysDate);

		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

		if (!selectedPermitNo.equals("") && queueNo.isEmpty() && queueNo.equals("") && queueNo.isEmpty()) {

			WHERE_SQL = WHERE_SQL + " and a.pm_permit_no=? ";
			
		}
		if (!queueNo.equals("") && !queueNo.isEmpty() && queueNo != null && selectedPermitNo.equals("")) {

			WHERE_SQL = " " + " and a.pm_queue_no=? ";
			
		}

		if (!selectedPermitNo.isEmpty() && !selectedPermitNo.equals("") && selectedPermitNo != null
				&& !queueNo.isEmpty() && !queueNo.equals("") && queueNo != null && !selectedApplicationNo.isEmpty()
				&& !selectedApplicationNo.equals("") && selectedApplicationNo != null) {

			WHERE_SQL = WHERE_SQL + " and a.pm_permit_no=? and a.pm_application_no=? ";
			
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pm_next_ins_date_sec1_2 as pm_next_ins_date_sec1_2,a.pm_next_ins_date_sec3 as pm_next_ins_date_sec3,a.seq as seqno, a.pm_application_no as applicationno, a.pm_queue_no as queueno, a.pm_permit_no as permitno, a.pm_vehicle_regno as vehicleregno,  a.pm_special_remark as specialremark,  a.pm_service_type as servicetypecode, a.pm_route_no as routeno,  a.pm_permit_expire_date as permitexpiredate, a.pm_renewal_period as renewalperiod,b.description as servicetypedes,c.rou_service_origine as serviceorigine, c.rou_service_destination as servicedestination, d.pmo_preferred_language as preferredlanguage, d.pmo_title as title, d.pmo_full_name as fullname, d.pmo_full_name_sinhala as fullnamesin, d.pmo_full_name_tamil as fullnametamil, d.pmo_name_with_initial as nameinitials, d.pmo_nic as nic, d.pmo_gender as gender, d.pmo_dob as dob, d.pmo_marital_status as martialstatus, d.pmo_telephone_no as teleno, d.pmo_mobile_no as mobileno, d.pmo_province as province, d.pmo_district as district, d.pmo_div_sec as divisionalsec, d.pmo_address1 as address1, d.pmo_address1_sinhala as address1sin, d.pmo_address1_tamil as address1tamil, d.pmo_address2 as address2, d.pmo_address2_sinhala as address2sin, d.pmo_address2_tamil as address2tamil,  d.pmo_city as city, d.pmo_city_sinhala as citysin, d.pmo_city_tamil as citytamil,a.pm_valid_to as validtodate,a.pm_valid_from as fromtodate,a.pm_is_backlog_app as backlogvalue,a.pm_route_flag as routeflagevalue, a.pm_new_permit_expiry_date as newpermitexpirydate  FROM public.nt_t_pm_application a,public.nt_r_service_types b,public.nt_r_route c,public.nt_t_pm_vehi_owner d where  a.pm_service_type=b.code and a.pm_route_no=c.rou_number and a.pm_application_no=d.pmo_application_no and  a.pm_permit_no=d.pmo_permit_no and a.pm_status=? "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			if (!selectedPermitNo.equals("") && queueNo.isEmpty() && queueNo.equals("") && queueNo.isEmpty()) {

				ps.setString(1, currentStatus);
				ps.setString(2, selectedPermitNo);
			}
			if (!queueNo.equals("") && !queueNo.isEmpty() && queueNo != null && selectedPermitNo.equals("")) {

				ps.setString(1, currentStatus);
				ps.setString(2, queueNo);
			}
			if (!selectedPermitNo.isEmpty() && !selectedPermitNo.equals("") && selectedPermitNo != null
					&& !queueNo.isEmpty() && !queueNo.equals("") && queueNo != null) {

				ps.setString(1, currentStatus);
				ps.setString(2, selectedPermitNo);

				ps.setString(3, selectedApplicationNo);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setInspectionDate1(rs.getString("pm_next_ins_date_sec1_2"));
				permitRenewalsDTO.setInspectionDate2(rs.getString("pm_next_ins_date_sec3"));
				permitRenewalsDTO.setSeqno(rs.getLong("seqno"));
				permitRenewalsDTO.setApplicationNo(rs.getString("applicationno"));
				permitRenewalsDTO.setQueueNo(rs.getString("queueno"));
				permitRenewalsDTO.setPermitNo(rs.getString("permitno"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("vehicleregno"));
				permitRenewalsDTO.setSpecialRemark(rs.getString("specialremark"));
				permitRenewalsDTO.setServiceTypeCode(rs.getString("servicetypecode"));
				permitRenewalsDTO.setRouteNo(rs.getString("routeno"));
				permitRenewalsDTO.setPermitExpiryDate(rs.getString("permitexpiredate"));
				permitRenewalsDTO.setRequestRenewPeriod(rs.getInt("renewalperiod"));
				permitRenewalsDTO.setServiceTypeDescription(rs.getString("servicetypedes"));
				permitRenewalsDTO.setPlaceOfOrginOfTheService(rs.getString("serviceorigine"));
				permitRenewalsDTO.setPlaceOfDestination(rs.getString("servicedestination"));
				permitRenewalsDTO.setPreferedLanguageCode(rs.getString("preferredlanguage"));
				permitRenewalsDTO.setTitleCode(rs.getString("title"));
				permitRenewalsDTO.setFullName(rs.getString("fullname"));
				permitRenewalsDTO.setFullNameSinhala(rs.getString("fullnamesin"));
				permitRenewalsDTO.setFullNameTamil(rs.getString("fullnametamil"));
				permitRenewalsDTO.setNameWithInitials(rs.getString("nameinitials"));
				permitRenewalsDTO.setNic(rs.getString("nic"));
				permitRenewalsDTO.setGenderCode(rs.getString("gender"));
				permitRenewalsDTO.setDob(rs.getString("dob"));
				permitRenewalsDTO.setMaterialStatusId(rs.getString("martialstatus"));
				permitRenewalsDTO.setTeleNo(rs.getString("teleno"));
				permitRenewalsDTO.setMobileNo(rs.getString("mobileno"));
				permitRenewalsDTO.setProvinceCode(rs.getString("province"));
				permitRenewalsDTO.setDistrictCode(rs.getString("district"));
				permitRenewalsDTO.setDivisionalSecretariatDivision(rs.getString("divisionalsec"));
				permitRenewalsDTO.setAddressOne(rs.getString("address1"));
				permitRenewalsDTO.setAddressOneSinhala(rs.getString("address1sin"));
				permitRenewalsDTO.setAddressOneTamil(rs.getString("address1tamil"));
				permitRenewalsDTO.setAddressTwo(rs.getString("address2"));
				permitRenewalsDTO.setAddressTwoSinhala(rs.getString("address2sin"));
				permitRenewalsDTO.setAddressTwoTamil(rs.getString("address2tamil"));
				permitRenewalsDTO.setCity(rs.getString("city"));
				permitRenewalsDTO.setCitySinhala(rs.getString("citysin"));
				permitRenewalsDTO.setCityTamil(rs.getString("citytamil"));
				permitRenewalsDTO.setValidToDate(rs.getString("validtodate"));
				permitRenewalsDTO.setFromToDate(rs.getString("fromtodate"));
				permitRenewalsDTO.setBacklogAppValue(rs.getString("backlogvalue"));
				permitRenewalsDTO.setNewPermitExpirtDate(rs.getString("newpermitexpirydate"));

				String backlogValue = permitRenewalsDTO.getBacklogAppValue();
				if (backlogValue == null || backlogValue.isEmpty() || backlogValue.equals(null)) {
					permitRenewalsDTO.setCheckBacklogValue(true);

				} else if (backlogValue.equals("Y")) {
					permitRenewalsDTO.setCheckBacklogValue(true);
				} else if (backlogValue.equals("N")) {
					permitRenewalsDTO.setCheckBacklogValue(false);
				}

				permitRenewalsDTO.setRouteFlageValue(rs.getString("routeflagevalue"));
				if (permitRenewalsDTO.getRouteFlageValue().equals("Y")) {
					permitRenewalsDTO.setRouteFlagChecked(true);
				} else if (permitRenewalsDTO.getRouteFlageValue().equals("N")) {
					permitRenewalsDTO.setRouteFlagChecked(false);
				}

				if (permitRenewalsDTO.getPermitExpiryDate() != null) {
					Date currentDateObj = frm.parse(currentDateString);
					Date currentPermitExpiredDateObj = frm.parse(permitRenewalsDTO.getPermitExpiryDate());
					if (currentPermitExpiredDateObj.compareTo(currentDateObj) > 0) {

						permitRenewalsDTO.setBeforePermitExpiredDate(null);
					} else if (currentPermitExpiredDateObj.compareTo(currentDateObj) < 0) {

						permitRenewalsDTO.setBeforePermitExpiredDate("Expired Permit");
					} else if (currentPermitExpiredDateObj.compareTo(currentDateObj) == 0) {
						permitRenewalsDTO.setBeforePermitExpiredDate(null);
					}
				} else {

				}

				if (currentStatus.equals("E")) {
					permitRenewalsDTO.setBeforePermitExpiredDate("Expired Permit");
				} else {
					permitRenewalsDTO.setBeforePermitExpiredDate(null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public List<PermitRenewalsDTO> getAllProvincesList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_province  order by description;";

			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setProvinceCode(rs.getString("code"));
				permitRenewalsDTO.setProvinceDescription(rs.getString("description"));
				returnList.add(permitRenewalsDTO);

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
	public List<PermitRenewalsDTO> getAllDistrictsForCurrentProvince(String selectedProvinceCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_district where province_code=? order by description;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedProvinceCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setDistrictCode(rs.getString("code"));
				permitRenewalsDTO.setDistrictDescription(rs.getString("description"));
				returnList.add(permitRenewalsDTO);

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
	public List<PermitRenewalsDTO> getAllMaterialStatusList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT code, description FROM public.nt_r_marital_status  where active='A' order by description;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setMaterialStatusId(rs.getString("code"));
				permitRenewalsDTO.setMaterialStatusDescription(rs.getString("description"));
				returnList.add(permitRenewalsDTO);

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
	public List<PermitRenewalsDTO> getAllDivisionSections(String selectedDistricCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ds_code, ds_description FROM public.nt_r_div_sec  where ds_district_code=? order by ds_description;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedDistricCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setDivisionalSecretariatDivision(rs.getString("ds_code"));
				permitRenewalsDTO.setDivisionSectionDescription(rs.getString("ds_description"));
				returnList.add(permitRenewalsDTO);

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
	public String getLangaugeDescription(String preferedLanguageCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String currentLanguageDescription = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  description FROM public.nt_r_language where code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, preferedLanguageCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				currentLanguageDescription = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return currentLanguageDescription;
	}

	@Override
	public String getGenderDescription(String genderCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String currentGenderDescription = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  description FROM public.nt_r_gender where code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, genderCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				currentGenderDescription = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return currentGenderDescription;
	}

	@Override
	public String getTitleDescription(String titleCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String currentTitleDescription = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  description FROM public.nt_r_title where code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, titleCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				currentTitleDescription = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return currentTitleDescription;
	}

	@Override
	public PermitRenewalsDTO getCurrentSeqNoForPermitNo(String selectedPermitNo, String queueNo, String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq, pm_application_no FROM public.nt_t_pm_application where pm_permit_no=? and (pm_queue_no=? or pm_queue_no  is null) and pm_application_no=?;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			ps.setString(2, queueNo);
			ps.setString(3, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setSeqno(rs.getLong("seq"));
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public PermitRenewalsDTO getCurrentOwnerSeqNo(String selectedPermitNo, String queueNo, String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seq FROM public.nt_t_pm_vehi_owner where pmo_permit_no=? and pmo_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			ps.setString(2, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setOwnerSeqNo(rs.getLong("seq"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public int updatePermitRenewalOwnerRecord(PermitRenewalsDTO permitRenewalsDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();
			String sql = "UPDATE public.nt_t_pm_vehi_owner SET pmo_marital_status=?, pmo_telephone_no=?, pmo_mobile_no=?, pmo_province=?, pmo_district=?, pmo_div_sec=?, pmo_address1=?, pmo_address1_sinhala=?, pmo_address1_tamil=?, pmo_address2=?, pmo_address2_sinhala=?, pmo_address2_tamil=?,  pmo_city=?, pmo_city_sinhala=?, pmo_city_tamil=?,  pm_modified_by=?, pm_modified_date=? WHERE pmo_application_no=? and pmo_permit_no=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, permitRenewalsDTO.getMaterialStatusId());
			stmt.setString(2, permitRenewalsDTO.getTeleNo());
			stmt.setString(3, permitRenewalsDTO.getMobileNo());
			stmt.setString(4, permitRenewalsDTO.getProvinceCode());
			stmt.setString(5, permitRenewalsDTO.getDistrictCode());
			stmt.setString(6, permitRenewalsDTO.getDivisionalSecretariatDivision());
			stmt.setString(7, permitRenewalsDTO.getAddressOne());
			stmt.setString(8, permitRenewalsDTO.getAddressOneSinhala());
			stmt.setString(9, permitRenewalsDTO.getAddressOneTamil());
			stmt.setString(10, permitRenewalsDTO.getAddressTwo());
			stmt.setString(11, permitRenewalsDTO.getAddressTwoSinhala());
			stmt.setString(12, permitRenewalsDTO.getAddressTwoTamil());
			stmt.setString(13, permitRenewalsDTO.getCity());
			stmt.setString(14, permitRenewalsDTO.getCitySinhala());
			stmt.setString(15, permitRenewalsDTO.getCityTamil());
			stmt.setString(16, permitRenewalsDTO.getModifyBy());
			stmt.setTimestamp(17, timestamp);
			stmt.setString(18, permitRenewalsDTO.getApplicationNo());
			stmt.setString(19, permitRenewalsDTO.getPermitNo());

			stmt.executeUpdate();
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return 0;
	}

	@Override
	public List<PermitRenewalsDTO> getAllRecordsForDocumentsCheckings() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select doc_code, doc_document_des FROM public.nt_m_document where doc_transaction_type='04' order by doc_code;";

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setDocumentCode(rs.getString("doc_code"));
				permitRenewalsDTO.setDocumentDescription(rs.getString("doc_document_des"));
				returnList.add(permitRenewalsDTO);

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
	public boolean checkIsSumbiited(String currentDocCode, String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isDocCodeHasRecord = false;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT seqno FROM public.nt_t_application_document where apd_application_no=? and apd_permit_no=? and apd_transaction_type='04' and apd_doc_code=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, applicationNo);
			stmt.setString(2, permitNo);
			stmt.setString(3, currentDocCode);
			rs = stmt.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setDocSeqChecked(rs.getLong("seqno"));
			}

			if (permitRenewalsDTO.getDocSeqChecked().equals("") || permitRenewalsDTO.getDocSeqChecked() == null) {
				isDocCodeHasRecord = false;
			} else {
				isDocCodeHasRecord = true;

			}

		} catch (Exception e) {

			isDocCodeHasRecord = false;

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isDocCodeHasRecord;
	}

	@Override
	public PermitRenewalsDTO getRemarkDetails(String currentDocCode, String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT seqno, apd_file_path, apd_remark FROM public.nt_t_application_document where apd_application_no=? and apd_permit_no=? and apd_transaction_type='04' and apd_doc_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			ps.setString(2, permitNo);
			ps.setString(3, currentDocCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setDocSeqChecked(rs.getLong("seqno"));
				permitRenewalsDTO.setDocFilePath(rs.getString("apd_file_path"));
				permitRenewalsDTO.setRemark(rs.getString("apd_remark"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public int updateDocumentRemark(Long currentRecordSeqNo, String currentUploadFilePath, String modifyBy,
			String currentRemark) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_application_document SET  apd_modified_by=?, apd_modified_date=?, apd_remark=? where seqno=? ;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, modifyBy);
			stmt.setTimestamp(2, timestamp);
			stmt.setString(3, currentRemark);
			stmt.setLong(4, currentRecordSeqNo);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return 0;
	}

	@Override
	public List<PermitRenewalsDTO> getAllPaymentHistoryList(String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  a.pav_seq_no as paymentvoucherseqno,   a.pav_voucher_no as vouchernoval, a.pav_total_amount as totalamount, a.pav_receipt_no as reciptno, c.rep_created_date as txndate FROM public.nt_m_payment_voucher a, public.nt_t_receipt c where  a.pav_application_no=?  and a.pav_receipt_no=c.rec_receipt_no;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setPaymentVocherSeq(rs.getLong("paymentvoucherseqno"));
				permitRenewalsDTO.setVoucherNo(rs.getString("vouchernoval"));
				permitRenewalsDTO.setAmount(rs.getDouble("totalamount"));
				permitRenewalsDTO.setReciptNo(rs.getString("reciptno"));
				permitRenewalsDTO.setTxnDate(rs.getString("txndate"));
				NumberFormat formatter = new DecimalFormat("##,###.00");
				String displayAmount = formatter.format(permitRenewalsDTO.getAmount());
				permitRenewalsDTO.setDisplayAmount(displayAmount);
				returnList.add(permitRenewalsDTO);

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
	public List<PermitRenewalsDTO> getAllChargeTypeRecords(String selectedVoucherNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.vod_seq_no as detailsseqno, a.vod_charge_type as chargetypecode, a.vod_amount as chargetypeamount,b.description as chargetypedescription FROM public.nt_t_voucher_details a, public.nt_r_charge_type b where a.vod_voucher_no=? and a.vod_charge_type=b.code;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedVoucherNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setVoucherDetailsSeqNo(rs.getLong("detailsseqno"));
				permitRenewalsDTO.setChargeTypeCode(rs.getString("chargetypecode"));
				permitRenewalsDTO.setChargeTypeAmount(rs.getDouble("chargetypeamount"));
				permitRenewalsDTO.setChargeTypeDescription(rs.getString("chargetypedescription"));
				NumberFormat formatter = new DecimalFormat("##,###.00");
				String displayAmount = formatter.format(permitRenewalsDTO.getChargeTypeAmount());
				permitRenewalsDTO.setChargeTypeDisplayAmount(displayAmount);
				returnList.add(permitRenewalsDTO);

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
	public PermitRenewalsDTO getRecordDetailsForCurrentQueueNo(String callingQueueNo,
			String currentAppNoForCallingQueueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_next_ins_date_sec1_2,pm_next_ins_date_sec3,pm_application_no, pm_permit_no, pm_vehicle_regno FROM public.nt_t_pm_application where   pm_application_no=? and pm_status='O'";

			ps = con.prepareStatement(query);
			ps.setString(1, currentAppNoForCallingQueueNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setInspectionDate1(rs.getString("pm_next_ins_date_sec1_2"));
				permitRenewalsDTO.setInspectionDate2(rs.getString("pm_next_ins_date_sec3"));
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public PermitRenewalsDTO getAllDetailsForSearchedPermitNo(String selectedPermitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno FROM public.nt_t_pm_application where pm_permit_no=? and pm_status='O';";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
				permitRenewalsDTO.setQueueNo(rs.getString("pm_queue_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public void counterStatus(String counterId, String loginUser) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = ConnectionManager.getConnection();

			String query = "UPDATE public.nt_r_counter "
					+ "SET cou_status='A',cou_serving_queueno=NULL, cou_assigned_userid='" + loginUser + "' "
					+ "WHERE cou_counter_id='" + counterId + "'";
			ps = con.prepareStatement(query);
			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
	}

	@Override
	public List<CommonDTO> counterdropdown() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<CommonDTO> returnList = new ArrayList<CommonDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  cou_counter_id, cou_counter_name " + "FROM public.nt_r_counter "
					+ "WHERE cou_status='I' AND cou_trn_type='04'";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				CommonDTO commonDTO = new CommonDTO();

				commonDTO.setCounter(rs.getString("cou_counter_name"));
				commonDTO.setCounterId(rs.getString("cou_counter_id"));

				returnList.add(commonDTO);

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
	public List<PermitRenewalsDTO> getAllApplicationNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_application_no, pm_queue_no FROM public.nt_t_pm_application where pm_status='O' and pm_permit_no is not null and pm_application_no !='' and pm_isnew_permit='Y' order by pm_application_no;";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitRenewalsDTO.setQueueNo(rs.getString("pm_queue_no"));
				returnList.add(permitRenewalsDTO);

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
	public PermitRenewalsDTO getAllDetailsForSearchedApplicationNo(String selectedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_application_no, pm_queue_no, pm_permit_no, pm_vehicle_regno FROM public.nt_t_pm_application where pm_application_no=? and pm_status='O';";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
				permitRenewalsDTO.setQueueNo(rs.getString("pm_queue_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public List<PermitRenewalsDTO> getAllOtherOptionalDocumentsList(String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  seqno,apd_doc_code, apd_document_des, apd_file_path, apd_remark FROM public.nt_t_application_document  where apd_transaction_type='04' and apd_application_no=? and apd_permit_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, applicationNo);
			ps.setString(2, permitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setDocSeqChecked(rs.getLong("seqno"));
				permitRenewalsDTO.setDocumentCode(rs.getString("apd_doc_code"));
				permitRenewalsDTO.setDocumentDescription(rs.getString("apd_document_des"));
				permitRenewalsDTO.setDocFilePath(rs.getString("apd_file_path"));
				permitRenewalsDTO.setRemark(rs.getString("apd_remark"));
				if (permitRenewalsDTO.getDocSeqChecked().equals("") || permitRenewalsDTO.getDocSeqChecked() == null) {
					permitRenewalsDTO.setExists(false);
				} else {
					permitRenewalsDTO.setExists(true);
				}
				returnList.add(permitRenewalsDTO);

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
	public boolean checkTaskCodeForCurrentAppNo(String selectedApplicationNo, String regNoOfBus, String taskCode,
			String taskStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_t_task_det "
					+ "where tsd_task_code=? and tsd_status=? and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, taskStatus);
			ps.setString(3, selectedApplicationNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isTaskAvailable = true;
			} else {
				isTaskAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskAvailable;
	}

	@Override
	public boolean insertTaskDetails(String selectedApplicationNo, String regNoOfBus, String loginUser, String taskCode,
			String status) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			long seqNo = Utils.getNextValBySeqName(con, "seq_nt_t_task_det");

			String sql = "INSERT INTO public.nt_t_task_det "
					+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?);";

			stmt = con.prepareStatement(sql);

			stmt.setLong(1, seqNo);
			stmt.setString(2, regNoOfBus);
			stmt.setString(3, selectedApplicationNo);
			stmt.setString(4, taskCode);
			stmt.setString(5, status);
			stmt.setString(6, loginUser);
			stmt.setTimestamp(7, timestamp);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}
		return false;

	}

	@Override
	public boolean checkTaskDetails(String selectedApplicationNo, String regNoOfBus, String taskCode, String status) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "select  tsd_app_no, tsd_task_code, tsd_status " + "FROM public.nt_t_task_det "
					+ "where tsd_task_code=? and tsd_status=? and tsd_app_no=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, taskCode);
			ps.setString(2, status);
			ps.setString(3, selectedApplicationNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isTaskAvailable = true;
			} else {
				isTaskAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskAvailable;
	}

	@Override
	public boolean CopyTaskDetailsANDinsertTaskHistory(String selectedApplicationNo, String regNoOfBus,
			String loginUser, String taskCode) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = null;

		boolean isUpdate = false;

		try {
			con = ConnectionManager.getConnection();

			String sql = "SELECT tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date "
					+ "FROM public.nt_t_task_det " + "WHERE tsd_app_no=? AND tsd_task_code=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, selectedApplicationNo);
			stmt.setString(2, taskCode);

			rs = stmt.executeQuery();

			if (rs.next()) {

				isUpdate = true;

				permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setTaskSeq(rs.getLong("tsd_seq"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("tsd_vehicle_no"));
				permitRenewalsDTO.setApplicationNo(rs.getString("tsd_app_no"));
				permitRenewalsDTO.setTaskCode(rs.getString("tsd_task_code"));
				permitRenewalsDTO.setTaskStatus(rs.getString("tsd_status"));
				permitRenewalsDTO.setCreateBy(rs.getString("created_by"));
				permitRenewalsDTO.setCreateDate(rs.getTimestamp("created_date"));

				String sql2 = "INSERT INTO public.nt_h_task_his "
						+ "(tsd_seq, tsd_vehicle_no, tsd_app_no, tsd_task_code, tsd_status, created_by, created_date) "
						+ "VALUES(? , ?, ?, ?, ?, ?, ?); " + "";

				stmt2 = con.prepareStatement(sql2);

				stmt2.setLong(1, permitRenewalsDTO.getTaskSeq());
				stmt2.setString(2, permitRenewalsDTO.getRegNoOfBus());
				stmt2.setString(3, permitRenewalsDTO.getApplicationNo());
				stmt2.setString(4, permitRenewalsDTO.getTaskCode());
				stmt2.setString(5, permitRenewalsDTO.getTaskStatus());
				stmt2.setString(6, permitRenewalsDTO.getCreateBy());
				stmt2.setTimestamp(7, permitRenewalsDTO.getCreateDate());

				stmt2.executeUpdate();

			} else {
				isUpdate = false;

			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);

		}
		return isUpdate;
	}

	@Override
	public boolean deleteTaskDetails(String selectedApplicationNo, String regNoOfBus, String taskCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isTaskPM101Delete = false;

		try {
			con = ConnectionManager.getConnection();

			String query = "DELETE FROM public.nt_t_task_det WHERE tsd_app_no=? AND tsd_task_code=? ;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			ps.setString(2, taskCode);
			int i = ps.executeUpdate();

			if (i > 0) {
				isTaskPM101Delete = true;

			} else {
				isTaskPM101Delete = false;

			}

			con.commit();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskPM101Delete;
	}

	@Override
	public boolean updateTaskDetails(String selectedApplicationNo, String regNoOfBus, String loginUser, String taskCode,
			String taskStatus) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_task_det SET tsd_task_code=?, tsd_status=?, created_by=? , created_date=?   where tsd_vehicle_no=? and tsd_app_no=? and tsd_task_code=? and tsd_status='O';";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, taskCode);
			stmt.setString(2, taskStatus);
			stmt.setString(3, loginUser);
			stmt.setTimestamp(4, timestamp);
			stmt.setString(5, regNoOfBus);
			stmt.setString(6, selectedApplicationNo);
			stmt.setString(7, taskCode);

			stmt.executeUpdate();

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}
		return false;
	}

	@Override
	public PermitRenewalsDTO getApplicationNoForSelectetedQueueNo(String callingQueueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String today = df.format(date);

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT   que_application_no FROM public.nt_m_queue_master where que_number=? and que_date like '"
					+ today + "%'";

			ps = con.prepareStatement(query);
			ps.setString(1, callingQueueNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setApplicationNo(rs.getString("que_application_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public PermitRenewalsDTO getRecordDetailsForCurrentAppNo(String currentAppNoForCallingQueueNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_next_ins_date_sec1_2,pm_next_ins_date_sec3,pm_application_no, pm_permit_no, pm_vehicle_regno FROM public.nt_t_pm_application where pm_application_no=?";

			ps = con.prepareStatement(query);
			ps.setString(1, currentAppNoForCallingQueueNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setInspectionDate1(rs.getString("pm_next_ins_date_sec1_2"));
				permitRenewalsDTO.setInspectionDate2(rs.getString("pm_next_ins_date_sec3"));
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public boolean isCheckedRecordInTaskHistory(String selectedApplicationNo, String taskCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT tsd_seq, tsd_app_no FROM public.nt_h_task_his where tsd_app_no=? and tsd_task_code=? and tsd_status='O';";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			ps.setString(2, taskCode);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isTaskAvailable = true;
			} else {
				isTaskAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskAvailable;
	}

	@Override
	public OminiBusDTO getVehiDetailsYr(String selectedApplicationNo, String regNoOfBus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		OminiBusDTO ominiBusDTO = new OminiBusDTO();

		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pmb_seating_capacity, pmb_no_of_doors, pmb_height, pmb_production_date, pmb_weight, pmb_serial_no, pmb_certificate_date, pmb_garage_reg_no, pmb_insurance_company, pmb_insurance_cat, pmb_insurance_expire_date,pmb_first_reg_date,pmb_policy_no,pmb_garage_name, pmb_revenue_license_exp_date, pmb_emission_test_exp_date FROM public.nt_t_pm_omini_bus1 where pmb_application_no=? and pmb_vehicle_regno=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedApplicationNo);
			ps.setString(2, regNoOfBus);
			rs = ps.executeQuery();

			while (rs.next()) {
				ominiBusDTO.setSeating(rs.getString("pmb_seating_capacity"));
				ominiBusDTO.setNoofDoors(rs.getString("pmb_no_of_doors"));
				ominiBusDTO.setHeight(rs.getString("pmb_height"));
				ominiBusDTO.setManufactureDate(rs.getString("pmb_production_date"));
				ominiBusDTO.setWeight(rs.getString("pmb_weight"));
				ominiBusDTO.setSerialNo(rs.getString("pmb_serial_no"));
				String fitDtateVal = rs.getString("pmb_certificate_date");
				if (fitDtateVal != null) {
					Date fitnessDateObj = frm.parse(rs.getString("pmb_certificate_date"));
					ominiBusDTO.setFitnessCertiDate(fitnessDateObj);
				} else {

				}
				ominiBusDTO.setGarageRegNo(rs.getString("pmb_garage_reg_no"));
				ominiBusDTO.setInsuCompName(rs.getString("pmb_insurance_company"));
				ominiBusDTO.setInsuCat(rs.getString("pmb_insurance_cat"));
				String insuExDateVal = rs.getString("pmb_insurance_expire_date");

				if (insuExDateVal != null) {
					Date insuExpDateObj = frm.parse(rs.getString("pmb_insurance_expire_date"));
					ominiBusDTO.setInsuExpDate(insuExpDateObj);
				} else {

				}
				String firstRegDateVal = rs.getString("pmb_first_reg_date");
				if (firstRegDateVal != null) {
					Date firstRegDateObj = frm.parse(rs.getString("pmb_first_reg_date"));
					ominiBusDTO.setDateOfFirstReg(firstRegDateObj);
					ominiBusDTO.setRegistrationDate(firstRegDateObj);
				} else {

				}
				ominiBusDTO.setPolicyNo(rs.getString("pmb_policy_no"));
				ominiBusDTO.setGarageName(rs.getString("pmb_garage_name"));

				// add new two fields

				String revenueExpDateVal = rs.getString("pmb_revenue_license_exp_date");
				if (revenueExpDateVal != null) {
					Date revenueExpDateObj = frm.parse(rs.getString("pmb_revenue_license_exp_date"));
					ominiBusDTO.setRevenueExpDate(revenueExpDateObj);
				} else {

				}

				String emissionExpDateVal = rs.getString("pmb_emission_test_exp_date");
				if (emissionExpDateVal != null) {
					Date emissionExpDateObj = frm.parse(rs.getString("pmb_emission_test_exp_date"));
					ominiBusDTO.setEmissionExpDate(emissionExpDateObj);
				} else {

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return ominiBusDTO;
	}

	@Override
	public boolean isMandatory(String currentDocCode, String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean ismandatory = false;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT  distinct * FROM public.nt_m_document "
					+ "where doc_transaction_type='04' and doc_code=? and doc_mandatory_doc='true'";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, currentDocCode);
			rs = stmt.executeQuery();

			if (rs.next()) {
				ismandatory = true;
			} else {
				ismandatory = false;
			}

		} catch (Exception e) {

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return ismandatory;
	}

	@Override
	public boolean isPhysicallyExit(String currentDocCode, String applicationNo, String permitNo) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean isPhysicallyExits = false;

		try {
			con = ConnectionManager.getConnection();
			String sql = "SELECT * FROM public.nt_m_document_check "
					+ "where  dck_application_no=? and dck_permit_no =? and dck_transaction_type='04' and dck_code=? and dck_physicaly_exist='true'; ";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, applicationNo);
			stmt.setString(2, permitNo);
			stmt.setString(3, currentDocCode);
			rs = stmt.executeQuery();

			if (rs.next()) {
				isPhysicallyExits = true;
			} else {
				isPhysicallyExits = false;
			}

		} catch (Exception e) {

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return isPhysicallyExits;
	}

	@Override
	public boolean insertDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		boolean isPhysicallyExits = false;
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		try {
			con = ConnectionManager.getConnection();

			String selectSQL = "SELECT dck_transaction_type, dck_code, dck_application_no, dck_permit_no, dck_physicaly_exist "
					+ "FROM public.nt_m_document_check "
					+ "where dck_transaction_type='04' and dck_code=? and dck_application_no=? and dck_permit_no=? "
					+ "and dck_physicaly_exist='true';";

			stmt2 = con.prepareStatement(selectSQL);

			stmt2.setString(1, currentDocCode);
			stmt2.setString(2, applicationNo);
			stmt2.setString(3, permitNo);
			rs = stmt2.executeQuery();

			if (rs.next() == true) {

			} else {

				String sql = "INSERT INTO public.nt_m_document_check "
						+ "(seqno, dck_transaction_type, dck_code, dck_application_no, dck_permit_no, dck_physicaly_exist, dck_created_by, dck_created_date) "
						+ "VALUES(?, ?, ?,?, ?, ?, ?, ? );";

				long seqNo = Utils.getNextValBySeqName(con, "seq_nt_m_document_check");

				stmt = con.prepareStatement(sql);
				stmt.setLong(1, seqNo);
				stmt.setString(2, "04");
				stmt.setString(3, currentDocCode);
				stmt.setString(4, applicationNo);
				stmt.setString(5, permitNo);
				stmt.setString(6, "true");
				stmt.setString(7, logUser);
				stmt.setTimestamp(8, timestamp);
				int i = stmt.executeUpdate();

				if (i > 0) {
					isPhysicallyExits = true;
				} else {
					isPhysicallyExits = false;
				}

				con.commit();

			}

		} catch (Exception e) {

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);

		}

		return isPhysicallyExits;
	}

	@Override
	public boolean deleteDocumentPhysicallyExitsStatus(boolean exits, String currentDocCode, String applicationNo,
			String permitNo, String logUser) {
		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		boolean isDeletePhysicallyExits = false;

		try {
			con = ConnectionManager.getConnection();

			String selectSQL = "SELECT dck_transaction_type, dck_code, dck_application_no, dck_permit_no, dck_physicaly_exist "
					+ "FROM public.nt_m_document_check "
					+ "where dck_transaction_type='04' and dck_code=? and dck_application_no=? "
					+ "and dck_physicaly_exist='true';";

			stmt2 = con.prepareStatement(selectSQL);

			stmt2.setString(1, currentDocCode);
			stmt2.setString(2, applicationNo);

			rs = stmt2.executeQuery();

			if (rs.next() == false) {

			} else {

				String sql = "DELETE FROM public.nt_m_document_check "
						+ "WHERE dck_transaction_type='04' and dck_code=? and dck_application_no=? and dck_physicaly_exist='true'";

				stmt = con.prepareStatement(sql);

				stmt.setString(1, currentDocCode);
				stmt.setString(2, applicationNo);

				int i = stmt.executeUpdate();

				if (i > 0) {
					isDeletePhysicallyExits = true;
				} else {
					isDeletePhysicallyExits = false;
				}

				con.commit();

			}

		} catch (Exception e) {

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(stmt2);
			ConnectionManager.close(con);

		}

		return isDeletePhysicallyExits;
	}

	@Override
	public List<PermitRenewalsDTO> getEditingApplicationNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct tsd_app_no,tsd_vehicle_no FROM public.nt_t_task_det where (tsd_task_code ='PR200' and tsd_status='O') or (tsd_task_code='PR200' and tsd_status='C') or (tsd_task_code='PM201' and tsd_status='O') order by tsd_app_no;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setApplicationNo(rs.getString("tsd_app_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("tsd_vehicle_no"));
				returnList.add(permitRenewalsDTO);
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

	@SuppressWarnings("resource")
	@Override
	public PermitRenewalsDTO getAllDetailsForSelectedEditingAppNo(String selectedApplicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();
			String query1 = "SELECT tsd_vehicle_no FROM public.nt_t_task_det where tsd_app_no=?;";

			ps = con.prepareStatement(query1);
			ps.setString(1, selectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setRegNoOfBus(rs.getString("tsd_vehicle_no"));

			}

			String query2 = "SELECT pm_application_no, pm_queue_no, pm_permit_no FROM public.nt_t_pm_application where pm_application_no=? and pm_vehicle_regno=?;";

			ps = con.prepareStatement(query2);
			ps.setString(1, selectedApplicationNo);
			ps.setString(2, permitRenewalsDTO.getRegNoOfBus());
			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitRenewalsDTO.setQueueNo(rs.getString("pm_queue_no"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public String getPreSpecialRemark(String selectedPermitNo, String busRegNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String preSpecialRemark = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_special_remark FROM public.nt_t_pm_application where pm_permit_no=? and pm_status='A' and pm_vehicle_regno=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			ps.setString(2, busRegNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				preSpecialRemark = rs.getString("pm_special_remark");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return preSpecialRemark;
	}

	@Override
	public String getCurrentStatusForSelectedRd(String selectedApplicationNo, String selectedPermitNo, String queueNo,
			String regNoOfBus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectedStatus = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pm_status FROM public.nt_t_pm_application where pm_permit_no=? and pm_vehicle_regno=? and pm_application_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			ps.setString(2, regNoOfBus);
			ps.setString(3, selectedApplicationNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				selectedStatus = rs.getString("pm_status");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return selectedStatus;
	}

	@Override
	public List<PermitRenewalsDTO> getLatestInactiveAppNo(String selectedPermitNo, String regNoOfBus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "select distinct to_char(pm_modified_date,'dd/MM/yyyy') as modify_date,pm_application_no FROM public.nt_t_pm_application where pm_permit_no=? and pm_vehicle_regno=? and pm_status='I';";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			ps.setString(2, regNoOfBus);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setModifyDate(rs.getString("modify_date"));
				permitRenewalsDTO.setLatestPreAppNo(rs.getString("pm_application_no"));
				returnList.add(permitRenewalsDTO);

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
	public PermitRenewalsDTO getSearchedRequestRenewalDetForCurrentQueueNoOrPermitNo(String selectedPermitNo,
			String queueNo, String selectedApplicationNo, String currentStatus) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String WHERE_SQL = "";
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		Date todaysDate = new Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String currentDateString = df.format(todaysDate);

		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

		if (!selectedPermitNo.equals("") && queueNo.isEmpty() && queueNo.equals("") && queueNo.isEmpty()) {

			WHERE_SQL = WHERE_SQL + " and a.pm_permit_no=? ";
			
		}
		if (!queueNo.equals("") && !queueNo.isEmpty() && queueNo != null && selectedPermitNo.equals("")) {

			WHERE_SQL = " " + " and a.pm_queue_no=? ";
			
		}

		if (!selectedPermitNo.isEmpty() && !selectedPermitNo.equals("") && selectedPermitNo != null
				&& !queueNo.isEmpty() && !queueNo.equals("") && queueNo != null && !selectedApplicationNo.isEmpty()
				&& !selectedApplicationNo.equals("") && selectedApplicationNo != null) {

			WHERE_SQL = WHERE_SQL + " and a.pm_permit_no=? and a.pm_application_no=? ";
			
		}

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT   a.pm_special_remark as specialremark,  a.pm_permit_expire_date as permitexpiredate, a.pm_renewal_period as renewalperiod, a.pm_valid_to as validtodate,a.pm_valid_from as fromtodate,a.pm_is_backlog_app as backlogvalue, a.pm_new_permit_expiry_date as newpermitexpirydate FROM public.nt_t_pm_application a where a.pm_status=? "
					+ WHERE_SQL;

			ps = con.prepareStatement(query);
			if (!selectedPermitNo.equals("") && queueNo.isEmpty() && queueNo.equals("") && queueNo.isEmpty()) {

				ps.setString(1, currentStatus);
				ps.setString(2, selectedPermitNo);
			}
			if (!queueNo.equals("") && !queueNo.isEmpty() && queueNo != null && selectedPermitNo.equals("")) {

				ps.setString(1, currentStatus);
				ps.setString(2, queueNo);
			}
			if (!selectedPermitNo.isEmpty() && !selectedPermitNo.equals("") && selectedPermitNo != null
					&& !queueNo.isEmpty() && !queueNo.equals("") && queueNo != null) {

				ps.setString(1, currentStatus);
				ps.setString(2, selectedPermitNo);

				ps.setString(3, selectedApplicationNo);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setSpecialRemark(rs.getString("specialremark"));
				permitRenewalsDTO.setPermitExpiryDate(rs.getString("permitexpiredate"));
				permitRenewalsDTO.setRequestRenewPeriod(rs.getInt("renewalperiod"));
				permitRenewalsDTO.setValidToDate(rs.getString("validtodate"));
				permitRenewalsDTO.setFromToDate(rs.getString("fromtodate"));
				permitRenewalsDTO.setBacklogAppValue(rs.getString("backlogvalue"));
				permitRenewalsDTO.setNewPermitExpirtDate(rs.getString("newpermitexpirydate"));

				String backlogValue = permitRenewalsDTO.getBacklogAppValue();
				if (backlogValue == null || backlogValue.isEmpty() || backlogValue.equals(null)) {
					permitRenewalsDTO.setCheckBacklogValue(true);

				} else if (backlogValue.equals("Y")) {
					permitRenewalsDTO.setCheckBacklogValue(true);
				} else if (backlogValue.equals("N")) {
					permitRenewalsDTO.setCheckBacklogValue(false);
				}

				if (permitRenewalsDTO.getPermitExpiryDate() != null) {
					Date currentDateObj = frm.parse(currentDateString);
					Date currentPermitExpiredDateObj = frm.parse(permitRenewalsDTO.getPermitExpiryDate());
					if (currentPermitExpiredDateObj.compareTo(currentDateObj) > 0) {

						permitRenewalsDTO.setBeforePermitExpiredDate(null);
					} else if (currentPermitExpiredDateObj.compareTo(currentDateObj) < 0) {

						permitRenewalsDTO.setBeforePermitExpiredDate("Expired Permit");
					} else if (currentPermitExpiredDateObj.compareTo(currentDateObj) == 0) {
						permitRenewalsDTO.setBeforePermitExpiredDate(null);
					}
				} else {

				}

				if (currentStatus.equals("E")) {
					permitRenewalsDTO.setBeforePermitExpiredDate("Expired Permit");
				} else {
					permitRenewalsDTO.setBeforePermitExpiredDate(null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public List<PermitRenewalsDTO> getAllVehicleNoList() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct pm_vehicle_regno FROM public.nt_t_pm_application where pm_vehicle_regno is not null order by pm_vehicle_regno;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
				returnList.add(permitRenewalsDTO);
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
	public List<PermitRenewalsDTO> getAllPermitNoListInApplicationTable() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "select distinct pm_permit_no FROM public.nt_t_pm_application where pm_permit_no is not null order by pm_permit_no;";
			ps = con.prepareStatement(query);

			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				returnList.add(permitRenewalsDTO);
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
	public List<PermitRenewalsDTO> getStatusList(String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  distinct to_char(pm_modified_date,'dd/MM/yyyy') as created_date, pm_permit_no, pm_vehicle_regno, pm_status, pm_application_no FROM public.nt_t_pm_application where pm_vehicle_regno=?"
					+ " and pm_status in ('A','O','P') order by pm_status asc;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
				permitRenewalsDTO.setStatus(rs.getString("pm_status"));
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				returnList.add(permitRenewalsDTO);

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
	public List<PermitRenewalsDTO> getLatestInactiveAppNo(String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();
			/**(pm_modified_date,'dd/MM/yyyy') change into (pm_modified_date,'yyyy/MM/dd') on 26.08.2022 tharushi.e **/
			String query = "select distinct to_char(pm_modified_date,'yyyy/MM/dd') as modify_date,pm_application_no FROM public.nt_t_pm_application where pm_vehicle_regno=? and pm_status='I' order by modify_date desc limit 1;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setModifyDate(rs.getString("modify_date"));
				permitRenewalsDTO.setLatestPreAppNo(rs.getString("pm_application_no"));
				returnList.add(permitRenewalsDTO);

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
	public PermitRenewalsDTO getActiveDetailsForSelectedVehicleNo(String needVehicleNo, String neededStatus,
			String selectedAppNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT a.pm_application_no,  a.pm_permit_no, a.pm_vehicle_regno, a.pm_permit_issue_date, a.pm_tender_ref_no, a.pm_status, a.pm_tender_annual_fee,  a.pm_valid_from,  a.pm_is_tender_permit, a.pm_service_type, a.pm_route_no, a.pm_permit_expire_date, a.pm_renewal_period,  a.pm_route_flag,  a.pm_isnew_permit,  a.pm_new_permit_expiry_date, b.description as service_des,c.rou_description as route_des, d.pmo_title, d.pmo_full_name,  d.pmo_name_with_initial, d.pmo_nic, d.pmo_gender, d.pmo_dob, d.pmo_marital_status, d.pmo_mobile_no, d.pmo_province, d.pmo_district, d.pmo_div_sec,  d.pmo_address1, d.pmo_address2, d.pmo_city, e.description as title_des,  g.description as province_des, c.rou_service_origine as rou_origin_des, c.rou_service_destination as rou_destination_des, c.rou_via as rou_via"
					+ " FROM public.nt_t_pm_application a left outer join  public.nt_r_service_types b on  a.pm_service_type=b.code left outer join "
					+ " public.nt_r_route c on  a.pm_route_no=c.rou_number left outer join "
					+ " public.nt_t_pm_vehi_owner d on a.pm_application_no=d.pmo_application_no   left outer join "
					+ " public.nt_r_title e on d.pmo_title=e.code  left outer join "
					+ " public.nt_r_province g on d.pmo_province = g.code "
					+ " WHERE  a.pm_application_no=? and a.pm_vehicle_regno= ?  ";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedAppNo);
			ps.setString(2, needVehicleNo);

			rs = ps.executeQuery();

			while (rs.next()) {

				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
				permitRenewalsDTO.setTenderRefNo(rs.getString("pm_tender_ref_no"));
				if (permitRenewalsDTO.getTenderRefNo() != null) {
					permitRenewalsDTO.setTenderFeeValidValue("Yes");
				} else {
					permitRenewalsDTO.setTenderFeeValidValue("No");
				}
				permitRenewalsDTO.setStatus(rs.getString("pm_status"));
				if (permitRenewalsDTO.getStatus().equals("A")) {
					permitRenewalsDTO.setStatusDes("Active");
				} else if (permitRenewalsDTO.getStatus().equals("O")) {
					permitRenewalsDTO.setStatusDes("Ongoing");
				} else if (permitRenewalsDTO.getStatus().equals("P")) {
					permitRenewalsDTO.setStatusDes("Pending");
				} else if (permitRenewalsDTO.getStatus().equals("I")) {
					permitRenewalsDTO.setStatusDes("Inactive");
				} else if (permitRenewalsDTO.getStatus().equals("E")) {
					permitRenewalsDTO.setStatusDes("Expired");
				} else if (permitRenewalsDTO.getStatus().equals("TC")) {
					permitRenewalsDTO.setStatusDes("Temporary Cancelled");
				} else if (permitRenewalsDTO.getStatus().equals("C")) {
					permitRenewalsDTO.setStatusDes("Cancelled");
				} else if (permitRenewalsDTO.getStatus().equals("H")) {
					permitRenewalsDTO.setStatusDes("Hold");
				} else if (permitRenewalsDTO.getStatus().equals("R")) {
					permitRenewalsDTO.setStatusDes("Rejected");
				}
				permitRenewalsDTO.setTenderAnualFee(rs.getBigDecimal("pm_tender_annual_fee"));
				permitRenewalsDTO.setValidFromDate(rs.getString("pm_valid_from"));
				permitRenewalsDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				permitRenewalsDTO.setRouteNo(rs.getString("pm_route_no"));
				permitRenewalsDTO.setPermitExpiryDate(rs.getString("pm_permit_expire_date"));
				permitRenewalsDTO.setRouteFlageValue(rs.getString("pm_route_flag"));

				if (permitRenewalsDTO.getRouteFlageValue() == null || permitRenewalsDTO.getRouteFlageValue().isEmpty()
						|| permitRenewalsDTO.getRouteFlageValue().equals(null)) {
					permitRenewalsDTO.setRouteFlagChecked(false);
				} else if (permitRenewalsDTO.getRouteFlageValue().equals("Y")) {
					permitRenewalsDTO.setRouteFlagChecked(true);
				} else if (permitRenewalsDTO.getRouteFlageValue().equals("N")) {
					permitRenewalsDTO.setRouteFlagChecked(false);
				}

				if (permitRenewalsDTO.isRouteFlagChecked() == true) {
					permitRenewalsDTO.setPlaceOfOrginOfTheService(rs.getString("rou_destination_des"));
					permitRenewalsDTO.setPlaceOfDestination(rs.getString("rou_origin_des"));
					permitRenewalsDTO.setVia(rs.getString("rou_via"));
				} else if (permitRenewalsDTO.isRouteFlagChecked() == false) {
					permitRenewalsDTO.setPlaceOfOrginOfTheService(rs.getString("rou_origin_des"));
					permitRenewalsDTO.setPlaceOfDestination(rs.getString("rou_destination_des"));
					permitRenewalsDTO.setVia(rs.getString("rou_via"));
				}
				permitRenewalsDTO.setServiceTypeDescription(rs.getString("service_des"));
				permitRenewalsDTO.setTitleCode(rs.getString("pmo_title"));
				permitRenewalsDTO.setFullName(rs.getString("pmo_full_name"));
				permitRenewalsDTO.setNameWithInitials(rs.getString("pmo_name_with_initial"));
				permitRenewalsDTO.setNic(rs.getString("pmo_nic"));
				permitRenewalsDTO.setGenderCode(rs.getString("pmo_gender"));
				permitRenewalsDTO.setDob(rs.getString("pmo_dob"));
				permitRenewalsDTO.setMaterialStatusId(rs.getString("pmo_marital_status"));
				permitRenewalsDTO.setMobileNo(rs.getString("pmo_mobile_no"));
				permitRenewalsDTO.setProvinceCode(rs.getString("pmo_province"));
				permitRenewalsDTO.setDistrictCode(rs.getString("pmo_district"));
				permitRenewalsDTO.setDivisionalSecretariatDivision(rs.getString("pmo_div_sec"));
				permitRenewalsDTO.setAddressOne(rs.getString("pmo_address1"));
				permitRenewalsDTO.setAddressTwo(rs.getString("pmo_address2"));
				permitRenewalsDTO.setCity(rs.getString("pmo_city"));
				permitRenewalsDTO.setTitleDescription(rs.getString("title_des"));
				permitRenewalsDTO.setProvinceDescription(rs.getString("province_des"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public String getCurrentDistrictDes(String provinceCode, String districtCode) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String currentDistrictDescription = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select description FROM public.nt_r_district where province_code=? and code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, provinceCode);
			ps.setString(2, districtCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				currentDistrictDescription = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return currentDistrictDescription;
	}

	@Override
	public String getCurrentDivSectionDes(String provinceCode, String districtCode,
			String divisionalSecretariatDivision) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String currentDivsionDescription = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT ds_description FROM public.nt_r_div_sec where ds_district_code=? and ds_code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, districtCode);
			ps.setString(2, divisionalSecretariatDivision);
			rs = ps.executeQuery();

			while (rs.next()) {
				currentDivsionDescription = rs.getString("ds_description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return currentDivsionDescription;
	}

	@Override
	public List<PermitRenewalsDTO> getStatusListForSelectedPermitNo(String selectedPermitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  distinct to_char(pm_modified_date,'dd/MM/yyyy') as created_date, pm_permit_no, pm_vehicle_regno, pm_status, pm_application_no FROM public.nt_t_pm_application where pm_permit_no=? "
					+ " and pm_status in ('A','O','P') order by pm_status asc;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
				permitRenewalsDTO.setStatus(rs.getString("pm_status"));
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				returnList.add(permitRenewalsDTO);

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
	public PermitRenewalsDTO getNewPermitDetails(String needVehicleNo, String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pm_permit_no FROM public.nt_t_pm_application where pm_old_permit_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);

			rs = ps.executeQuery();

			while (rs.next()) {
				permitRenewalsDTO.setNewPermitNo(rs.getString("pm_permit_no"));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public PermitRenewalsDTO getAttorneyDetailsForSelectedVehicleNo(String currentPermitNo, String regNoOfBus,
			String status, String applicationNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  a.ath_permit_no, a.ath_application_no, a.ath_vehicle_regno, a.ath_preferred_lan, a.ath_curr_owner_full_name, a.ath_curr_owner_add_1, a.ath_curr_owner_add_2, a.ath_curr_owner_city, a.ath_title, a.ath_gender, a.ath_dob, a.ath_nic_no, a.ath_fullname, a.ath_full_name_sinhala, a.ath_full_name_tamil, a.ath_name_with_initial, a.ath_tel_no, a.ath_mobile_no, a.ath_status, a.ath_address1, a.ath_address1_sinhala, a.ath_address1_tamil, a.ath_address2, a.ath_address2_sinhala, a.ath_address2_tamil, a.ath_city, a.ath_city_sinhala, a.ath_city_tamil, a.ath_valid_from, a.ath_valid_to FROM public.nt_m_atterny_holder a where ath_status='A' and ath_permit_no=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, currentPermitNo);

			rs = ps.executeQuery();
			while (rs.next()) {
				permitRenewalsDTO.setPermitNo(rs.getString("ath_permit_no"));
				permitRenewalsDTO.setApplicationNo(rs.getString("ath_application_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("ath_vehicle_regno"));
				permitRenewalsDTO.setStatus(rs.getString("ath_status"));
				permitRenewalsDTO.setTitleCode(rs.getString("ath_title"));
				permitRenewalsDTO.setFullName(rs.getString("ath_fullname"));
				permitRenewalsDTO.setFullNameSinhala(rs.getString("ath_full_name_sinhala"));
				permitRenewalsDTO.setFullNameTamil(rs.getString("ath_full_name_tamil"));
				permitRenewalsDTO.setNameWithInitials(rs.getString("ath_name_with_initial"));
				permitRenewalsDTO.setNic(rs.getString("ath_nic_no"));
				permitRenewalsDTO.setGenderCode(rs.getString("ath_gender"));
				permitRenewalsDTO.setDob(rs.getString("ath_dob"));
				permitRenewalsDTO.setMobileNo(rs.getString("ath_mobile_no"));
				permitRenewalsDTO.setAddressOne(rs.getString("ath_address1"));
				permitRenewalsDTO.setAddressTwo(rs.getString("ath_address2"));
				permitRenewalsDTO.setCity(rs.getString("ath_city"));
				permitRenewalsDTO.setAddressOneSinhala(rs.getString("ath_address1_sinhala"));
				permitRenewalsDTO.setAddressOneTamil(rs.getString("ath_address1_tamil"));
				permitRenewalsDTO.setAddressTwoSinhala(rs.getString("ath_address2_sinhala"));
				permitRenewalsDTO.setAddressTwoTamil(rs.getString("ath_address2_tamil"));
				permitRenewalsDTO.setCitySinhala(rs.getString("ath_city_sinhala"));
				permitRenewalsDTO.setCityTamil(rs.getString("ath_city_tamil"));
				permitRenewalsDTO.setTeleNo(rs.getString("ath_tel_no"));
				permitRenewalsDTO.setCurrentAttorneyFullName(rs.getString("ath_curr_owner_full_name"));
				permitRenewalsDTO.setCurrentAttorneyAddressOne(rs.getString("ath_curr_owner_add_1"));
				permitRenewalsDTO.setCurrentAttorneyAddressTwo(rs.getString("ath_curr_owner_add_2"));
				permitRenewalsDTO.setCurrentAttorneyCity(rs.getString("ath_curr_owner_city"));
				permitRenewalsDTO.setAttorneyHolderStartDate(rs.getString("ath_valid_from"));
				permitRenewalsDTO.setAttorneyHolderEndDate(rs.getString("ath_valid_to"));

				if (permitRenewalsDTO.getStatus().equals("A")) {
					permitRenewalsDTO.setStatusDes("Active");
				} else if (permitRenewalsDTO.getStatus().equals("O")) {
					permitRenewalsDTO.setStatusDes("Ongoing");
				} else if (permitRenewalsDTO.getStatus().equals("P")) {
					permitRenewalsDTO.setStatusDes("Pending");
				} else if (permitRenewalsDTO.getStatus().equals("I")) {
					permitRenewalsDTO.setStatusDes("Active");
				} else if (permitRenewalsDTO.getStatus().equals("E")) {
					permitRenewalsDTO.setStatusDes("Expired");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return permitRenewalsDTO;
	}

	@Override
	public String getCurrentMaterialStatusDes(String materialStatusId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String currentMarterialDescription = null;
		try {
			con = ConnectionManager.getConnection();

			String query = "select description FROM public.nt_r_marital_status where code=?;";

			ps = con.prepareStatement(query);
			ps.setString(1, materialStatusId);

			rs = ps.executeQuery();

			while (rs.next()) {
				currentMarterialDescription = rs.getString("description");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}

		return currentMarterialDescription;
	}

	@Override
	public List<PermitRenewalsDTO> getPermitHistoryListForSelectedVehicleNo(String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct a.pm_created_date, a.pm_application_no, a.pm_permit_no, a.pm_vehicle_regno, a.pm_permit_issue_date, a.pm_status, a.pm_valid_from, a.pm_valid_to, a.pm_special_remark, a.pm_service_type, a.pm_route_no, a.pm_permit_expire_date, a.pm_route_flag, a.pm_old_permit_no, a.pm_temp_permit_no, b.description as service_type_des, c.rou_service_origine as rou_origin_des, c.rou_service_destination as rou_destination_des, d.pmo_full_name, a.pm_old_permit_no , c.rou_via "
					+ "FROM public.nt_t_pm_application a left outer join public.nt_r_service_types b"
					+ " on  a.pm_service_type=b.code  left outer join public.nt_r_route c"
					+ " on  a.pm_route_no=c.rou_number left outer join public.nt_t_pm_vehi_owner d "
					+ " on  d.pmo_application_no=a.pm_application_no " + " where a.pm_vehicle_regno= ? "
					+ " order by a.pm_created_date DESC;";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
				permitRenewalsDTO.setPermitIssueDateVal(rs.getString("pm_permit_issue_date"));
				permitRenewalsDTO.setStatus(rs.getString("pm_status"));
				permitRenewalsDTO.setValidToDate(rs.getString("pm_valid_to"));
				permitRenewalsDTO.setSpecialRemark(rs.getString("pm_special_remark"));
				permitRenewalsDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				permitRenewalsDTO.setRouteNo(rs.getString("pm_route_no"));
				permitRenewalsDTO.setPermitExpiryDate(rs.getString("pm_permit_expire_date"));
				permitRenewalsDTO.setRouteFlageValue(rs.getString("pm_route_flag"));
				permitRenewalsDTO.setOldPermitNo(rs.getString("pm_old_permit_no"));
				permitRenewalsDTO.setTempPermitNo(rs.getString("pm_temp_permit_no"));
				permitRenewalsDTO.setServiceTypeDescription(rs.getString("service_type_des"));
				permitRenewalsDTO.setFullName(rs.getString("pmo_full_name"));
				permitRenewalsDTO.setOldPermitNo(rs.getString("pm_old_permit_no"));
				permitRenewalsDTO.setVia(rs.getString("rou_via"));

				if (permitRenewalsDTO.getRouteFlageValue() == null || permitRenewalsDTO.getRouteFlageValue().isEmpty()
						|| permitRenewalsDTO.getRouteFlageValue().equals(null)) {
					permitRenewalsDTO.setRouteFlagChecked(false);
				} else if (permitRenewalsDTO.getRouteFlageValue().equals("Y")) {
					permitRenewalsDTO.setRouteFlagChecked(true);
				} else if (permitRenewalsDTO.getRouteFlageValue().equals("N")) {
					permitRenewalsDTO.setRouteFlagChecked(false);
				}

				if (permitRenewalsDTO.isRouteFlagChecked() == true) {
					permitRenewalsDTO.setPlaceOfOrginOfTheService(rs.getString("rou_destination_des"));
					permitRenewalsDTO.setPlaceOfDestination(rs.getString("rou_origin_des"));
				} else if (permitRenewalsDTO.isRouteFlagChecked() == false) {
					permitRenewalsDTO.setPlaceOfOrginOfTheService(rs.getString("rou_origin_des"));
					permitRenewalsDTO.setPlaceOfDestination(rs.getString("rou_destination_des"));
				}

				String validFromDate = rs.getString("pm_valid_from");
				if (validFromDate == null || validFromDate.isEmpty() || validFromDate.equals("")) {
					permitRenewalsDTO.setValidFromDate(rs.getString("pm_permit_expire_date"));
				} else {
					permitRenewalsDTO.setValidFromDate(rs.getString("pm_valid_from"));
				}

				if (permitRenewalsDTO.getStatus().equals("A")) {
					permitRenewalsDTO.setStatusDes("Active");
				} else if (permitRenewalsDTO.getStatus().equals("O")) {
					permitRenewalsDTO.setStatusDes("Ongoing");
				} else if (permitRenewalsDTO.getStatus().equals("P")) {
					permitRenewalsDTO.setStatusDes("Pending");
				} else if (permitRenewalsDTO.getStatus().equals("I")) {
					permitRenewalsDTO.setStatusDes("Inactive");
				} else if (permitRenewalsDTO.getStatus().equals("E")) {
					permitRenewalsDTO.setStatusDes("Expired");
				} else if (permitRenewalsDTO.getStatus().equals("R")) {
					permitRenewalsDTO.setStatusDes("Rejected");
				} else if (permitRenewalsDTO.getStatus().equals("C")) {
					permitRenewalsDTO.setStatusDes("Cancelled");
				} else if (permitRenewalsDTO.getStatus().equals("TC")) {
					permitRenewalsDTO.setStatusDes("Temporary Cancelled");
				} else if (permitRenewalsDTO.getStatus().equals("H")) {
					permitRenewalsDTO.setStatusDes("Hold");
				} else if (permitRenewalsDTO.getStatus().equals("G")) {
					permitRenewalsDTO.setStatusDes("Other Inspection");
				}
				returnList.add(permitRenewalsDTO);

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
	public List<PermitRenewalsDTO> getPermitHistoryListForSelectedPermitNo(String selectedPermitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT distinct a.pm_created_date, a.pm_application_no, a.pm_permit_no, a.pm_vehicle_regno, a.pm_permit_issue_date, a.pm_status, a.pm_valid_from, a.pm_valid_to, a.pm_special_remark, a.pm_service_type, a.pm_route_no, a.pm_permit_expire_date, a.pm_route_flag, a.pm_old_permit_no, a.pm_temp_permit_no, b.description as service_type_des, c.rou_service_origine as rou_origin_des, c.rou_service_destination as rou_destination_des, d.pmo_full_name, a.pm_old_permit_no , c.rou_via "
					+ " FROM public.nt_t_pm_application a left outer join public.nt_r_service_types b "
					+ " on  a.pm_service_type=b.code left outer join public.nt_r_route c "
					+ " on a.pm_route_no=c.rou_number  left outer join public.nt_t_pm_vehi_owner d "
					+ " on d.pmo_application_no=a.pm_application_no " + " where   a.pm_permit_no= ? "
					+ " order by a.pm_created_date DESC ";
			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setApplicationNo(rs.getString("pm_application_no"));
				permitRenewalsDTO.setPermitNo(rs.getString("pm_permit_no"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("pm_vehicle_regno"));
				permitRenewalsDTO.setPermitIssueDateVal(rs.getString("pm_permit_issue_date"));
				permitRenewalsDTO.setStatus(rs.getString("pm_status"));
				permitRenewalsDTO.setValidToDate(rs.getString("pm_valid_to"));
				permitRenewalsDTO.setSpecialRemark(rs.getString("pm_special_remark"));
				permitRenewalsDTO.setServiceTypeCode(rs.getString("pm_service_type"));
				permitRenewalsDTO.setRouteNo(rs.getString("pm_route_no"));
				permitRenewalsDTO.setPermitExpiryDate(rs.getString("pm_permit_expire_date"));
				permitRenewalsDTO.setRouteFlageValue(rs.getString("pm_route_flag"));
				permitRenewalsDTO.setOldPermitNo(rs.getString("pm_old_permit_no"));
				permitRenewalsDTO.setTempPermitNo(rs.getString("pm_temp_permit_no"));
				permitRenewalsDTO.setServiceTypeDescription(rs.getString("service_type_des"));
				permitRenewalsDTO.setFullName(rs.getString("pmo_full_name"));
				permitRenewalsDTO.setOldPermitNo(rs.getString("pm_old_permit_no"));
				permitRenewalsDTO.setVia(rs.getString("rou_via"));

				if (permitRenewalsDTO.getRouteFlageValue() == null || permitRenewalsDTO.getRouteFlageValue().isEmpty()
						|| permitRenewalsDTO.getRouteFlageValue().equals(null)) {
					permitRenewalsDTO.setRouteFlagChecked(false);
				} else if (permitRenewalsDTO.getRouteFlageValue().equals("Y")) {
					permitRenewalsDTO.setRouteFlagChecked(true);
				} else if (permitRenewalsDTO.getRouteFlageValue().equals("N")) {
					permitRenewalsDTO.setRouteFlagChecked(false);
				}

				if (permitRenewalsDTO.isRouteFlagChecked() == true) {
					permitRenewalsDTO.setPlaceOfOrginOfTheService(rs.getString("rou_destination_des"));
					permitRenewalsDTO.setPlaceOfDestination(rs.getString("rou_origin_des"));
				} else if (permitRenewalsDTO.isRouteFlagChecked() == false) {
					permitRenewalsDTO.setPlaceOfOrginOfTheService(rs.getString("rou_origin_des"));
					permitRenewalsDTO.setPlaceOfDestination(rs.getString("rou_destination_des"));
				}

				String validFromDate = rs.getString("pm_valid_from");
				if (validFromDate == null || validFromDate.isEmpty() || validFromDate.equals("")) {
					permitRenewalsDTO.setValidFromDate(rs.getString("pm_permit_expire_date"));
				} else {
					permitRenewalsDTO.setValidFromDate(rs.getString("pm_valid_from"));
				}

				if (permitRenewalsDTO.getStatus().equals("A")) {
					permitRenewalsDTO.setStatusDes("Active");
				} else if (permitRenewalsDTO.getStatus().equals("O")) {
					permitRenewalsDTO.setStatusDes("Ongoing");
				} else if (permitRenewalsDTO.getStatus().equals("P")) {
					permitRenewalsDTO.setStatusDes("Pending");
				} else if (permitRenewalsDTO.getStatus().equals("I")) {
					permitRenewalsDTO.setStatusDes("Inactive");
				} else if (permitRenewalsDTO.getStatus().equals("E")) {
					permitRenewalsDTO.setStatusDes("Expired");
				} else if (permitRenewalsDTO.getStatus().equals("C")) {
					permitRenewalsDTO.setStatusDes("Cancelled");
				} else if (permitRenewalsDTO.getStatus().equals("TC")) {
					permitRenewalsDTO.setStatusDes("Temporary Cancelled");
				} else if (permitRenewalsDTO.getStatus().equals("H")) {
					permitRenewalsDTO.setStatusDes("Hold");
				} else if (permitRenewalsDTO.getStatus().equals("G")) {
					permitRenewalsDTO.setStatusDes("Other Inspection");
				}
				returnList.add(permitRenewalsDTO);

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
	public List<PermitRenewalsDTO> getVoucherNoList(PermitRenewalsDTO selectPemrmitHisDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT pav_permit_no, pav_application_no, pav_voucher_no FROM public.nt_m_payment_voucher WHERE pav_application_no=? ;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectPemrmitHisDTO.getApplicationNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setPermitNo(rs.getString("pav_permit_no"));
				permitRenewalsDTO.setApplicationNo(rs.getString("pav_application_no"));
				permitRenewalsDTO.setVoucherNo(rs.getString("pav_voucher_no"));
				returnList.add(permitRenewalsDTO);

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
	public List<PermitRenewalsDTO> getSelectedPaymentTypeList(PermitRenewalsDTO selectPemrmitHisDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pav_payment_type,pav_seq_no,pav_receipt_no,pav_voucher_no FROM public.nt_m_payment_voucher   WHERE pav_application_no =?  AND pav_permit_no =?;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectPemrmitHisDTO.getApplicationNo());
			ps.setString(2, selectPemrmitHisDTO.getPermitNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setPaymentType(rs.getString("pav_payment_type"));
				permitRenewalsDTO.setReciptNo(rs.getString("pav_receipt_no"));
				permitRenewalsDTO.setVoucherNo(rs.getString("pav_voucher_no"));
				returnList.add(permitRenewalsDTO);

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
	public List<PermitRenewalsDTO> getSelectedPaymentTypeListWithOldPermitNo(PermitRenewalsDTO selectPemrmitHisDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();

			String query = "SELECT  pav_payment_type,pav_seq_no,pav_receipt_no, pav_voucher_no FROM public.nt_m_payment_voucher   WHERE pav_application_no =?  AND pav_permit_no =?;";
			ps = con.prepareStatement(query);
			ps.setString(1, selectPemrmitHisDTO.getApplicationNo());
			ps.setString(2, selectPemrmitHisDTO.getOldPermitNo());
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();
				permitRenewalsDTO.setPaymentType(rs.getString("pav_payment_type"));
				permitRenewalsDTO.setReciptNo(rs.getString("pav_receipt_no"));
				permitRenewalsDTO.setVoucherNo(rs.getString("pav_voucher_no"));
				returnList.add(permitRenewalsDTO);

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
	public boolean isCheckActiveAttorneyHolder(String permitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		boolean isTaskAvailable = false;
		try {

			con = ConnectionManager.getConnection();

			String query = "SELECT ath_permit_no FROM public.nt_m_atterny_holder where ath_permit_no=? and ath_status='A';";

			ps = con.prepareStatement(query);
			ps.setString(1, permitNo);
			rs = ps.executeQuery();

			if (rs.next() == true) {
				isTaskAvailable = true;
			} else {
				isTaskAvailable = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);
		}
		return isTaskAvailable;
	}

	@Override
	public int updateTranstractionType(String transtractionTypeCode, String currentAppNo, String loginUSer) {
		java.util.Date date = new java.util.Date();

		Timestamp timestamp = new Timestamp(date.getTime());

		Connection con = null;
		PreparedStatement stmt = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int saved = -1;

		try {

			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_application SET pm_tran_type=?, pm_modified_by=? , pm_modified_date=?  WHERE pm_application_no=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, transtractionTypeCode);
			stmt.setString(2, loginUSer);
			stmt.setTimestamp(3, timestamp);
			stmt.setString(4, currentAppNo);
			int i = stmt.executeUpdate();

			if (i > 0) {
				saved = 0;

			} else {
				saved = -1;
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(ps);
			ConnectionManager.close(con);

		}
		return saved;

	}

	@Override
	public List<PermitRenewalsDTO> getOldHistoryDataWithPermitNo(String selectedPermitNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();
			String query = "SELECT permit_number,  "
					+ "       bus_number,  "
					+ "       route_number,  "
					+ "       origin,  "
					+ "       destination,  "
					+ "       CONCAT(initials, ' ', surname) as full_name, "
					+ "       'type' as service_type,  "
					+ "       change_date,  "
					+ "       'comments'  "
					+ "FROM public.t_permits_log  "
					+ "WHERE permit_number LIKE '%' || SUBSTRING(? FROM 2) "
					+ "UNION  "
					+ "SELECT permit_number,  "
					+ "       bus_number,  "
					+ "       route_number,  "
					+ "       origin,  "
					+ "       destination,  "
					+ "       CONCAT(initials, ' ', surname) as full_name,  "
					+ "       'type',  "
					+ "       change_date,  "
					+ "       'comments'  "
					+ "FROM public.t_permits_log  "
					+ "WHERE comments LIKE '%' || SUBSTRING(? FROM 2) || '%';";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedPermitNo);
			ps.setString(2, selectedPermitNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

				permitRenewalsDTO.setPermitNo(rs.getString("permit_number"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("bus_number"));
				permitRenewalsDTO.setRouteNo(rs.getString("route_number"));
				permitRenewalsDTO.setPlaceOfOrginOfTheService(rs.getString("origin"));
				permitRenewalsDTO.setPlaceOfDestination(rs.getString("destination"));
				permitRenewalsDTO.setFullName(rs.getString("full_name"));
				permitRenewalsDTO.setServiceTypeCode(rs.getString("service_type"));
				String validityDate = rs.getString("change_date");
				if (validityDate != null) {

					String format = "dd/MM/yyyy";
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					Date dateObj = dateFormat.parse(validityDate);
					String validDateStr = dateFormat.format(dateObj);
					permitRenewalsDTO.setValidFromDate(validDateStr);
				} else {
					permitRenewalsDTO.setValidFromDate(validityDate);
				}

				permitRenewalsDTO.setSpecialRemark(rs.getString("comments"));
				returnList.add(permitRenewalsDTO);

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
	public List<PermitRenewalsDTO> getOldHistoryDataWithBusNo(String selectedVehicleNo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<PermitRenewalsDTO> returnList = new ArrayList<PermitRenewalsDTO>();

		try {
			con = ConnectionManager.getConnection();
			String ii = " ";
			String query = "((SELECT permit_number,bus_number,route_number,origin,destination,CONCAT(initials,'" + ii
					+ "',surname ) as full_name, "
					+ "\"type\",  change_date, \"comments\" FROM public.t_permits_log where bus_number = ?) UNION (SELECT permit_number,bus_number,route_number,origin,destination,CONCAT(initials,'  ',surname ) as full_name, \"type\",  change_date, \"comments\" FROM public.t_permits_log where comments like '%"
					+ selectedVehicleNo + "%'));";

			ps = con.prepareStatement(query);
			ps.setString(1, selectedVehicleNo);
			rs = ps.executeQuery();

			while (rs.next()) {
				PermitRenewalsDTO permitRenewalsDTO = new PermitRenewalsDTO();

				permitRenewalsDTO.setPermitNo(rs.getString("permit_number"));
				permitRenewalsDTO.setRegNoOfBus(rs.getString("bus_number"));
				permitRenewalsDTO.setRouteNo(rs.getString("route_number"));
				permitRenewalsDTO.setPlaceOfOrginOfTheService(rs.getString("origin"));
				permitRenewalsDTO.setPlaceOfDestination(rs.getString("destination"));
				permitRenewalsDTO.setFullName(rs.getString("full_name"));
				permitRenewalsDTO.setServiceTypeCode(rs.getString("type"));
				String validityDate = rs.getString("change_date");
				if (validityDate != null) {

					String format = "dd/MM/yyyy";
					SimpleDateFormat dateFormat = new SimpleDateFormat(format);
					Date dateObj = dateFormat.parse(validityDate);
					String validDateStr = dateFormat.format(dateObj);
					permitRenewalsDTO.setValidFromDate(validDateStr);
				} else {
					permitRenewalsDTO.setValidFromDate(validityDate);
				}
				permitRenewalsDTO.setSpecialRemark(rs.getString("comments"));
				returnList.add(permitRenewalsDTO);

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
	public int savePermitRenewalRecordAllinOne(OminiBusDTO ominiBusDTO, PermitRenewalsDTO permitRenewalsDTO) {

		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);

		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_omini_bus1 SET pmb_seating_capacity=?, pmb_no_of_doors=?, pmb_production_date=?, pmb_weight=?,pmb_serial_no=?, pmb_certificate_date=?, pmb_garage_reg_no=?, pmb_insurance_company=?, pmb_insurance_cat=?, pmb_insurance_expire_date=?,pmb_policy_no=?, pmb_modified_by=?, pmb_modified_date=?,pmb_permit_no=?,pmb_first_reg_date=?,pmb_garage_name=?, pmb_revenue_license_exp_date=?, pmb_emission_test_exp_date=? WHERE pmb_application_no= ? and pmb_vehicle_regno=?;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ominiBusDTO.getSeating());
			stmt.setString(2, ominiBusDTO.getNoofDoors());
			stmt.setString(3, ominiBusDTO.getManufactureDate());
			stmt.setString(4, ominiBusDTO.getWeight());
			stmt.setString(5, ominiBusDTO.getSerialNo());
			if (ominiBusDTO.getFitnessCertiDate() != null) {
				String fitnessDateVal = frm.format(ominiBusDTO.getFitnessCertiDate());
				stmt.setString(6, fitnessDateVal);
			} else {
				stmt.setString(6, null);
			}
			stmt.setString(7, ominiBusDTO.getGarageRegNo());
			stmt.setString(8, ominiBusDTO.getInsuCompName());
			stmt.setString(9, ominiBusDTO.getInsuCat());
			if (ominiBusDTO.getInsuExpDate() != null) {
				String getInsuExpDate = frm.format(ominiBusDTO.getInsuExpDate());
				stmt.setString(10, getInsuExpDate);
			} else {
				stmt.setString(10, null);
			}
			stmt.setString(11, ominiBusDTO.getPolicyNo());
			stmt.setString(12, ominiBusDTO.getModifiedBy());
			stmt.setTimestamp(13, timestamp);
			stmt.setString(14, ominiBusDTO.getPermitNo());
			if (ominiBusDTO.getDateOfFirstReg() != null) {
				String getDateOfFirstReg = frm.format(ominiBusDTO.getDateOfFirstReg());
				stmt.setString(15, getDateOfFirstReg);
			} else {
				stmt.setString(15, null);
			}
			stmt.setString(16, ominiBusDTO.getGarageName());

			if (ominiBusDTO.getRevenueExpDate() != null) {
				String revenueExpDateVal = frm.format(ominiBusDTO.getRevenueExpDate());
				stmt.setString(17, revenueExpDateVal);
			} else {
				stmt.setString(17, null);
			}

			if (ominiBusDTO.getEmissionExpDate() != null) {
				String emissionExpDateVal = frm.format(ominiBusDTO.getEmissionExpDate());
				stmt.setString(18, emissionExpDateVal);
			} else {
				stmt.setString(18, null);
			}

			stmt.setString(19, ominiBusDTO.getApplicationNo());
			stmt.setString(20, ominiBusDTO.getVehicleRegNo());
			stmt.executeUpdate();
			
			
			//update permit renewal record
			updatePermitRenewalRecord(con, permitRenewalsDTO);
			
			ConnectionManager.commit(con);

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;

	}
	
	private int updatePermitRenewalRecord(Connection con, PermitRenewalsDTO permitRenewalsDTO) throws Exception {
		PreparedStatement psUpdateRenewal = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String sql = "UPDATE public.nt_t_pm_application  SET pm_special_remark=?, pm_permit_expire_date=?, pm_renewal_period=?,  pm_modified_by=?, pm_modified_date=?,pm_valid_from=?,pm_valid_to=?, pm_new_permit_expiry_date=?, pm_isnew_permit=?  WHERE pm_application_no=?;";

		psUpdateRenewal = con.prepareStatement(sql);
		psUpdateRenewal.setString(1, permitRenewalsDTO.getSpecialRemark());
		psUpdateRenewal.setString(2, permitRenewalsDTO.getPermitExpiryDate());
		psUpdateRenewal.setInt(3, permitRenewalsDTO.getRequestRenewPeriod());
		psUpdateRenewal.setString(4, permitRenewalsDTO.getModifyBy());
		psUpdateRenewal.setTimestamp(5, timestamp);
		psUpdateRenewal.setString(6, permitRenewalsDTO.getFromToDate());
		psUpdateRenewal.setString(7, permitRenewalsDTO.getValidToDate());
		psUpdateRenewal.setString(8, permitRenewalsDTO.getNewPermitExpirtDate());
		psUpdateRenewal.setString(9, "N");
		psUpdateRenewal.setString(10, permitRenewalsDTO.getApplicationNo());
		psUpdateRenewal.executeUpdate();

		ConnectionManager.close(psUpdateRenewal);

		return 0;
	}

	@Override
	public int updatePermitRenewalRecordAllinOne(OminiBusDTO ominiBusDTO, PermitRenewalsDTO permitRenewalsDTO) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());

		String dateFormat = "dd/MM/yyyy";
		SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
		try {
			con = ConnectionManager.getConnection();

			String sql = "UPDATE public.nt_t_pm_omini_bus1 SET pmb_seating_capacity=?, pmb_no_of_doors=?,pmb_production_date=?, pmb_weight=?,pmb_serial_no=?, pmb_certificate_date=?, pmb_garage_reg_no=?, pmb_insurance_company=?, pmb_insurance_cat=?, pmb_insurance_expire_date=?, pmb_policy_no=?, pmb_modified_by=?, pmb_modified_date=?, pmb_garage_name=?, pmb_revenue_license_exp_date=?, pmb_emission_test_exp_date=?  WHERE pmb_application_no=? and pmb_vehicle_regno=? ;";

			stmt = con.prepareStatement(sql);

			stmt.setString(1, ominiBusDTO.getSeating());
			stmt.setString(2, ominiBusDTO.getNoofDoors());
			stmt.setString(3, ominiBusDTO.getManufactureDate());
			stmt.setString(4, ominiBusDTO.getWeight());
			stmt.setString(5, ominiBusDTO.getSerialNo());
			if (ominiBusDTO.getFitnessCertiDate() != null) {
				String fitnessDateVal = frm.format(ominiBusDTO.getFitnessCertiDate());
				stmt.setString(6, fitnessDateVal);
			} else {
				stmt.setString(6, null);
			}
			stmt.setString(7, ominiBusDTO.getGarageRegNo());
			stmt.setString(8, ominiBusDTO.getInsuCompName());
			stmt.setString(9, ominiBusDTO.getInsuCat());

			if (ominiBusDTO.getInsuExpDate() != null) {
				String getInsuExpDate = frm.format(ominiBusDTO.getInsuExpDate());
				stmt.setString(10, getInsuExpDate);
			} else {
				stmt.setString(10, null);
			}

			stmt.setString(11, ominiBusDTO.getPolicyNo());
			stmt.setString(12, ominiBusDTO.getModifiedBy());
			stmt.setTimestamp(13, timestamp);
			stmt.setString(14, ominiBusDTO.getGarageName());

			// add new 2 fields
			if (ominiBusDTO.getRevenueExpDate() != null) {
				String revenueExpDateVal = frm.format(ominiBusDTO.getRevenueExpDate());
				stmt.setString(15, revenueExpDateVal);
			} else {
				stmt.setString(15, null);
			}

			if (ominiBusDTO.getEmissionExpDate() != null) {
				String emissionExpDateVal = frm.format(ominiBusDTO.getEmissionExpDate());
				stmt.setString(16, emissionExpDateVal);
			} else {
				stmt.setString(16, null);
			}

			stmt.setString(17, ominiBusDTO.getApplicationNo());
			stmt.setString(18, ominiBusDTO.getVehicleRegNo());

			stmt.executeUpdate();
			
			//update permit renewal record
			updatePermitRenewalRecord(con, permitRenewalsDTO);
			
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			ConnectionManager.close(rs);
			ConnectionManager.close(stmt);
			ConnectionManager.close(con);
		}

		return 0;
	}

}
