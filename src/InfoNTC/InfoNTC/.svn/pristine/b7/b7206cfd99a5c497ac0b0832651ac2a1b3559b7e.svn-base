package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "templateBackingBean")
@ApplicationScoped
public class TemplateBackingBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	Map counterMap;
	Set set;
	
	@javax.annotation.PostConstruct
	public void init() {
		counterMap = new HashMap();  
	}
	
	private int number;
	 
    public int getNumber() {
        return number;
    }
    
    public void addCountersToMap(String counterId) {
    	counterMap.put(counterId,"open"); 
    	set=counterMap.entrySet();
    }
 
    public void closeCounters() {
    	if(set!=null && !set.isEmpty()) {
    		Iterator itr=set.iterator();  
            while(itr.hasNext()){ 
            	Map.Entry entry=(Map.Entry)itr.next();  
            	counterMap.put(entry.getKey(),"close"); 
            }
    	}
    }
}
