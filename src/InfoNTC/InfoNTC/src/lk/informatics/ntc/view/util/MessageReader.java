package lk.informatics.ntc.view.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import lk.informatics.ntc.model.exception.ApplicationException;

public class MessageReader {

	public static Logger logger = Logger.getLogger(MessageReader.class);

	public static Properties loadPropertyFile() throws ApplicationException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream("/lk/informatics/roc/view/messages/Messages.properties");
		Properties properties = new Properties();
		try {
			properties.load(input);
		} catch (IOException e) {
			logger.error(CommonUtility.getStackTraceFromException(e));
			e.printStackTrace();
			throw new ApplicationException("Unable to Read Application Property File");

		}
		return properties;
	}

	public static String getPropertyValue(String key) throws ApplicationException {
		try {
			Properties properties = loadPropertyFile();
			String value = properties.getProperty(key);
			if (value == null) {
				throw new ApplicationException("Unable to Read  Property For Key " + key);
			}
			return value.trim();
		} catch (Exception e) {
			logger.error(CommonUtility.getStackTraceFromException(e));
			if (e instanceof ApplicationException) {
				throw (ApplicationException) e;
			} else {
				throw new ApplicationException("Unable to Read  Property For Key " + key);
			}
		}

	}

}
