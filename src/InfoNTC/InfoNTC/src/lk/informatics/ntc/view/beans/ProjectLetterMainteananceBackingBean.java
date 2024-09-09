package lk.informatics.ntc.view.beans;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.TerminalPayCancellationDTO;
import lk.informatics.ntc.model.service.TerminalManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "projectLetterMainteananceBackingBean")
@ViewScoped

public class ProjectLetterMainteananceBackingBean {
	
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;
	
    //Services
		
	//DTOs
	
	//SelectedValues
		private String errorMsg;
		private String successMessage;
		private String selectedYear;
		private String selectedRefNo;
		private String selectedProvince;
		private String selectedDistrict;
		private String selectedEduZone;
		private Date selectedZoneLetIssueDate;
		private String selectedSchoolName;
		private Date selectedSchoolLetterDate;
		private String selectedPoliceName;
		private Date selectedPoloceLetterDate;
		
		
		@PostConstruct
		public void init() {
			
		}
		
//Methods
		public void saveBtn() {
			
			
		}
		public void clearFields() {
			
			
		}


 // getters setters
  

		public SessionBackingBean getSessionBackingBean() {
			return sessionBackingBean;
		}


		public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
			this.sessionBackingBean = sessionBackingBean;
		}


		public String getErrorMsg() {
			return errorMsg;
		}


		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}


		public String getSuccessMessage() {
			return successMessage;
		}


		public void setSuccessMessage(String successMessage) {
			this.successMessage = successMessage;
		}


		public String getSelectedYear() {
			return selectedYear;
		}


		public void setSelectedYear(String selectedYear) {
			this.selectedYear = selectedYear;
		}


		public String getSelectedRefNo() {
			return selectedRefNo;
		}


		public void setSelectedRefNo(String selectedRefNo) {
			this.selectedRefNo = selectedRefNo;
		}


		public String getSelectedProvince() {
			return selectedProvince;
		}


		public void setSelectedProvince(String selectedProvince) {
			this.selectedProvince = selectedProvince;
		}


		public String getSelectedDistrict() {
			return selectedDistrict;
		}


		public void setSelectedDistrict(String selectedDistrict) {
			this.selectedDistrict = selectedDistrict;
		}


		public String getSelectedEduZone() {
			return selectedEduZone;
		}


		public void setSelectedEduZone(String selectedEduZone) {
			this.selectedEduZone = selectedEduZone;
		}


		public Date getSelectedZoneLetIssueDate() {
			return selectedZoneLetIssueDate;
		}


		public void setSelectedZoneLetIssueDate(Date selectedZoneLetIssueDate) {
			this.selectedZoneLetIssueDate = selectedZoneLetIssueDate;
		}


		public String getSelectedSchoolName() {
			return selectedSchoolName;
		}


		public void setSelectedSchoolName(String selectedSchoolName) {
			this.selectedSchoolName = selectedSchoolName;
		}


		public Date getSelectedSchoolLetterDate() {
			return selectedSchoolLetterDate;
		}


		public void setSelectedSchoolLetterDate(Date selectedSchoolLetterDate) {
			this.selectedSchoolLetterDate = selectedSchoolLetterDate;
		}


		public String getSelectedPoliceName() {
			return selectedPoliceName;
		}


		public void setSelectedPoliceName(String selectedPoliceName) {
			this.selectedPoliceName = selectedPoliceName;
		}


		public Date getSelectedPoloceLetterDate() {
			return selectedPoloceLetterDate;
		}


		public void setSelectedPoloceLetterDate(Date selectedPoloceLetterDate) {
			this.selectedPoloceLetterDate = selectedPoloceLetterDate;
		}

	
		
	
}
