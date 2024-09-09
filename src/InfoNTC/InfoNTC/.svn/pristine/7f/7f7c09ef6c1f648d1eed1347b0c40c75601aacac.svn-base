package lk.informatics.ntc.view.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContex implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		CONTEXT = ctx;
	}

	public static Object getBean(String name) {
		return CONTEXT.getBean(name);
	}

}
