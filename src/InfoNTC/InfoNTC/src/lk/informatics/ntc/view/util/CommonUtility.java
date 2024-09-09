package lk.informatics.ntc.view.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.EmailValidator;

public class CommonUtility {

	public static final String COMMON_DATE_PATTERN = "dd/MM/yyyy";

	public static String getRemoteAddress(HttpServletRequest req) {
		String ipAddress = req.getHeader("X-FORWARDED-FOR");
		if (ipAddress != null) {
			ipAddress = ipAddress.replaceFirst(",.*", "");
		} else {
			ipAddress = req.getRemoteAddr();
		}
		return ipAddress;
	}

	public static String getStackTraceFromException(Exception e) {
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		StringBuffer stackTrace = new StringBuffer(
				"################### Printing current stack trace into log file ################### \n\n");
		try {

			e.printStackTrace(printWriter);
			printWriter.flush();
			stackTrace.append(writer.toString());
			return stackTrace.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				writer.close();
				printWriter.close();
				stackTrace = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

	public static String generateToken(String candidateChars) {
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
		int len = 10;
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return sb.toString() + candidateChars;
	}

	public static boolean isValidEmailAddress(String email) {
		boolean valid = EmailValidator.getInstance().isValid(email);
		return valid;
	}

	public static String getDateString(Date date) {
		DateFormat df = new SimpleDateFormat(COMMON_DATE_PATTERN);
		String formatedDate = df.format(date);
		return formatedDate;
	}

}
