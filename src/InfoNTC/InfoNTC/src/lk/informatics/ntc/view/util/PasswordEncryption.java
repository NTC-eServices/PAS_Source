package lk.informatics.ntc.view.util;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import lk.informatics.ntc.model.exception.PasswordLengthNotEnoughException;

public class PasswordEncryption implements PasswordEncoder {

	public String encryptPassword(String password) {
		int addedasciivalue = 0;
		try {
			if (password != null) {
				password = password.toUpperCase();
			}
			char wk_character;
			if (password == null || password.equalsIgnoreCase("")) {
				throw new PasswordLengthNotEnoughException();
			}
			if (password.length() == 1) {
				wk_character = password.toUpperCase().charAt(0);
				int asciivalue = (int) wk_character;
				addedasciivalue = asciivalue + 7;
				if ((asciivalue >= 65 && asciivalue <= 90) && (addedasciivalue > 90)) {
					addedasciivalue = 65 + (addedasciivalue - 90);
				} else {
					if ((asciivalue >= 48 && asciivalue <= 57) && (addedasciivalue > 57)) {
						addedasciivalue = 48 + (addedasciivalue - 57);
					}
				}
			} else {
				String firstCharacter = password.substring(0, 1);
				wk_character = firstCharacter.toUpperCase().charAt(0);
				int asciivalue = (int) wk_character;
				addedasciivalue = asciivalue + 7;

				if ((asciivalue >= 65 && asciivalue <= 90) && (addedasciivalue > 90)) {
					addedasciivalue = 65 + (addedasciivalue - 90);
				} else {
					if ((asciivalue >= 48 && asciivalue <= 57) && (addedasciivalue > 57)) {
						addedasciivalue = 48 + (addedasciivalue - 57);
					}
				}
				char charpassword = (char) addedasciivalue;
				String stringPassword = String.valueOf(charpassword);

				return stringPassword + encryptPassword(password.substring(1, password.length()));
			}
			char charpassword = (char) addedasciivalue;
			return String.valueOf(charpassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		;
		return null;

	}

	@Override
	public String encodePassword(String arg0, Object arg1) throws DataAccessException {

		return encryptPassword(arg0);
	}

	@Override
	public boolean isPasswordValid(String arg0, String arg1, Object arg2) throws DataAccessException {

		String encryptedPwd = encryptPassword(arg1.toUpperCase());
		if (arg0 != null) {
			arg0 = arg0.toUpperCase();
		}
		if (arg0.equalsIgnoreCase(encryptedPwd)) {
			return true;
		} else {
			return false;
		}

	}

}
