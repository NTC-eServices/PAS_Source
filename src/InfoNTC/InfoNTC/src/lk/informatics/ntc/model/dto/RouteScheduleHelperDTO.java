package lk.informatics.ntc.model.dto;

import java.util.LinkedHashSet;
import java.util.List;

public class RouteScheduleHelperDTO {
	private LinkedHashSet<String> busSet;
    private List<String> busList;
    
    
	public LinkedHashSet<String> getBusSet() {
		return busSet;
	}
	public void setBusSet(LinkedHashSet<String> busSet) {
		this.busSet = busSet;
	}
	public List<String> getBusList() {
		return busList;
	}
	public void setBusList(List<String> busList) {
		this.busList = busList;
	}
    
}
