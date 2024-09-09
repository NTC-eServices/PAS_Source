package lk.informatics.ntc.view.util;

import java.util.regex.Pattern;

public class UtilityClass {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static boolean isValidEmailAddress(String inputEmail) {

		try {

			if (inputEmail != null) {
				Pattern ptr = Pattern.compile(EMAIL_PATTERN);
				boolean valid = ptr.matcher(inputEmail).matches();
				return valid;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isValidPhoneNum(String phoneNum) {

		try {

			if (phoneNum != null) {
				Pattern ptr = Pattern.compile("^[0-9]{10}$");
				boolean valid = ptr.matcher(phoneNum).matches();
				return valid;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
