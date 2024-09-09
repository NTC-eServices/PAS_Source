package lk.informatics.ntc.model.dto;

import java.io.Serializable;

public class RouteSetUpModifiedDataDTO  implements Serializable{
	
	private String key;
    private boolean routeFlag;
	
    
    
    public RouteSetUpModifiedDataDTO() {}

	public RouteSetUpModifiedDataDTO(String key, boolean routeFlag) {
		this.key = key;
		this.routeFlag = routeFlag;
	}
    
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public boolean isRouteFlag() {
		return routeFlag;
	}
	public void setRouteFlag(boolean routeFlag) {
		this.routeFlag = routeFlag;
	} 

}
