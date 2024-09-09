package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class CodeDescriptionDTO  implements Serializable{
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  
  private String code;
  private String description_sinhala;
  private String description_english;
  private String description_tamil;
  private String status;
  private String statusDescription;
  
  
  
  public String getStatusDescription() {
	return statusDescription;
}
public void setStatusDescription(String statusDescription) {
	this.statusDescription = statusDescription;
}
private boolean searchedRecord;
  private boolean freshRecord;
  
  
  
  
  
public boolean isFreshRecord() {
	return freshRecord;
}
public void setFreshRecord(boolean freshRecord) {
	this.freshRecord = freshRecord;
}
public static long getSerialversionuid() {
	return serialVersionUID;
}
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}
 
public boolean isSearchedRecord() {
	return searchedRecord;
}
public void setSearchedRecord(boolean searchedRecord) {
	this.searchedRecord = searchedRecord;
}

public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getDescription_sinhala() {
	return description_sinhala;
}
public void setDescription_sinhala(String description_sinhala) {
	this.description_sinhala = description_sinhala;
}
public String getDescription_english() {
	return description_english;
}
public void setDescription_english(String description_english) {
	this.description_english = description_english;
}
public String getDescription_tamil() {
	return description_tamil;
}
public void setDescription_tamil(String description_tamil) {
	this.description_tamil = description_tamil;
}
  
  
  
}
