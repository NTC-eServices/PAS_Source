package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.SearchEmployeeDTO;
import lk.informatics.ntc.model.service.EmployeeProfileService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "searchEmployee")

@ViewScoped
public class SearchBackingBean {
	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	public List<String> epfNumberList = new ArrayList<String>(0);
	public List<String> empNumberList = new ArrayList<String>(0);
	public List<SearchEmployeeDTO> dataList = new ArrayList<SearchEmployeeDTO>();
	public String emptextread;
	public String urlsearch;
	public boolean view;
	public boolean edit;

	@PostConstruct
	public void init() {

		if (sessionBackingBean.searchURLStatus) {
			loadValues();
		}
		if (!sessionBackingBean.searchURLStatus) {
			sessionBackingBean.setSearchURL(null);
			dataList = sessionBackingBean.getTempDataList();
			sessionBackingBean.setSearchURLStatus(true);
			RequestContext.getCurrentInstance().update("abc");
		}
	}

	public void loadValues() {
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");
		epfNumberList = employeeProfileService.getAllEpfNubers();
		empNumberList = employeeProfileService.getAllEmpNumbers();
		this.edit = false;
		this.view = false;
	}

	public SearchBackingBean() {
		employeeProfileService = (EmployeeProfileService) SpringApplicationContex.getBean("employeeProfileService");

	}

	public boolean getView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	public boolean getEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public String getUrlsearch() {
		return urlsearch;
	}

	public void setUrlsearch(String urlsearch) {
		this.urlsearch = urlsearch;
	}

	public String finalurl;

	public List<SearchEmployeeDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<SearchEmployeeDTO> dataList) {
		this.dataList = dataList;
	}

	private SearchEmployeeDTO selectedEmp;

	private EmployeeProfileService employeeProfileService;

	private SearchEmployeeDTO searchEmployeeDTO = new SearchEmployeeDTO();

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public List<String> getEpfNumberList() {
		return epfNumberList;
	}

	public void setEpfNumberList(List<String> epfNumberList) {
		this.epfNumberList = epfNumberList;
	}

	public EmployeeProfileService getEmployeeProfileService() {
		return employeeProfileService;
	}

	public void setEmployeeProfileService(EmployeeProfileService employeeProfileService) {
		this.employeeProfileService = employeeProfileService;
	}

	public SearchEmployeeDTO getSearchEmployeeDTO() {
		return searchEmployeeDTO;
	}

	public void setSearchEmployeeDTO(SearchEmployeeDTO searchEmployeeDTO) {
		this.searchEmployeeDTO = searchEmployeeDTO;
	}

	public List<String> completeTheme(String query) {
		List<String> allThemes = empNumberList;
		List<String> filteredThemes = new ArrayList<String>();
		for (int i = 0; i < allThemes.size(); i++) {
			if (allThemes.get(i).contains(query)) {
				filteredThemes.add(allThemes.get(i));
			}
		}

		return filteredThemes;
	}

	public String getEmptextread() {
		return emptextread;
	}

	public void setEmptextread(String emptextread) {
		this.emptextread = emptextread;
	}

	public void searchclear() {
		searchEmployeeDTO = new SearchEmployeeDTO();
		dataList.clear();

	}

	public void searchaction() {
		dataList = new ArrayList<>(0);
		dataList = employeeProfileService.searchEmployee(searchEmployeeDTO);

		String userID = sessionBackingBean.loginUser;

	}

	public SearchEmployeeDTO getSelectedEmp() {
		return selectedEmp;
	}

	public void setSelectedEmp(SearchEmployeeDTO selectedEmp) {
		this.selectedEmp = selectedEmp;
	}

	public String viewaction() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		sessionBackingBean.setSearchURL(request.getRequestURL().toString());
		sessionBackingBean.setApproveURL(null);

		sessionBackingBean.setSearchURLStatus(true);

		sessionBackingBean.tempDataList = dataList;

		sessionBackingBean.setEmployeeNo(selectedEmp.getEmpNo());
		sessionBackingBean.setPageMode("V");
		return "/pages/um/employeeProfile.xhtml";
	}

	public String editaction() {

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		sessionBackingBean.setSearchURL(request.getRequestURL().toString());

		sessionBackingBean.setApproveURL(null);

		sessionBackingBean.setSearchURLStatus(true);

		sessionBackingBean.tempDataList = dataList;

		sessionBackingBean.setEmployeeNo(selectedEmp.getEmpNo());
		sessionBackingBean.setPageMode("E");
		return "/pages/um/employeeProfile.xhtml";
	}
}
