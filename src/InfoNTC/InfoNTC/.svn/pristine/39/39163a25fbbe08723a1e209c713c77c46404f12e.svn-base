package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lk.informatics.ntc.model.dto.DepartmentDTO;
import lk.informatics.ntc.model.dto.MainCounterDTO;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "customerDashboardBean")
@ViewScoped
public class CustomerDashboardBackingBean {

	private QueueManagementService queueManagementService;
	private DepartmentDTO departmentDTO;
	private List<DepartmentDTO> departmentList;
	private List<MainCounterDTO> counterList = new ArrayList<MainCounterDTO>();;

	@PostConstruct
	public void init() {
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		departmentDTO = new DepartmentDTO();
		departmentList = queueManagementService.getAllActiveDepartments();

	}

	public void onDepartmentChange() {

		counterList = queueManagementService.getCountersOfDepartments(departmentDTO.getCode());

	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public DepartmentDTO getDepartmentDTO() {
		return departmentDTO;
	}

	public void setDepartmentDTO(DepartmentDTO departmentDTO) {
		this.departmentDTO = departmentDTO;
	}

	public List<DepartmentDTO> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<DepartmentDTO> departmentList) {
		this.departmentList = departmentList;
	}

	public List<MainCounterDTO> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<MainCounterDTO> counterList) {
		this.counterList = counterList;
	}

}
