package lk.informatics.ntc.model.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CombineControlSheetMasterDTO implements Serializable {

	private static final long serialVersionUID = -2315729604070350813L;

	private LocalDate date;
	private List<CombineControlSheetDetailsDTO> detailsList;
	private List<CombineControlSheetDetailsDTO> leaveDetailsList;

	public CombineControlSheetMasterDTO(LocalDate date) {
		this.date = date;
		this.detailsList = new ArrayList<CombineControlSheetDetailsDTO>();
		this.leaveDetailsList = new ArrayList<CombineControlSheetDetailsDTO>();
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<CombineControlSheetDetailsDTO> getDetailsList() {
		return detailsList;
	}

	public void setDetailsList(List<CombineControlSheetDetailsDTO> detailsList) {
		this.detailsList = detailsList;
	}

	public List<CombineControlSheetDetailsDTO> getLeaveDetailsList() {
		return leaveDetailsList;
	}

	public void setLeaveDetailsList(List<CombineControlSheetDetailsDTO> leaveDetailsList) {
		this.leaveDetailsList = leaveDetailsList;
	}
	
	public List<CombineControlSheetDetailsDTO> orderListByLocalTime() {
        List<CombineControlSheetDetailsDTO> orderedList = new ArrayList<>(detailsList);
        
        Collections.sort(orderedList, new Comparator<CombineControlSheetDetailsDTO>() {
            @Override
            public int compare(CombineControlSheetDetailsDTO o1, CombineControlSheetDetailsDTO o2) {
                return o1.getLocalTime().compareTo(o2.getLocalTime());
            }
        });
        
        return orderedList;
    }

}
