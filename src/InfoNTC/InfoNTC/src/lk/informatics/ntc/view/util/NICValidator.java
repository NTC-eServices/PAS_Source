package lk.informatics.ntc.view.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NICValidator {
	public static boolean validateNIC(String nic, String gender, Date dob) {
		boolean validNic = false;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		List<Integer> daysofLeapYrList = new ArrayList<Integer>(
				Arrays.asList(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31));

		String dateOfBirth = simpleDateFormat.format(dob);

		if (nic.length() == 10) {// old nic

			if (nic.substring(9).matches("^[A-Za-z]{1}$")) {
				if (nic.substring(0, 9).matches("^[0-9]{9}$")) {

					// check digit validation
					int tot = 0;
					int[] checkConstant = { 3, 2, 7, 6, 5, 4, 3, 2 };
					for (int i = 0; i < 8; i++) {
						tot += Integer.parseInt(nic.substring(i, i + 1)) * checkConstant[i];
					}
					int checkDigit = Math.abs(tot % 11);
					String checkDig = null;
					if (checkDigit < 2) {
						checkDig = Integer.toString(0);
					}

					else {
						checkDig = Integer.toString(11 - checkDigit);
					}

					if ((nic.substring(8, 9)).equalsIgnoreCase(checkDig)) {

						int dayOfYear = Integer.parseInt(nic.substring(2, 5));
						int month = Integer.parseInt(dateOfBirth.substring(3, 5));
						int days = Integer.parseInt(dateOfBirth.substring(0, 2));

						int numofmonths = month - 2;

						int sum = 0;

						for (int i = 0; i <= numofmonths; i++) {
							sum = sum + daysofLeapYrList.get(i);
						}

						int compute = sum + days;

						if (gender.equalsIgnoreCase("M") && dayOfYear < 367) {

							if ((compute == dayOfYear)
									&& (nic.substring(0, 2).equalsIgnoreCase(dateOfBirth.substring(8)))) {

								validNic = true;

							}
						}
						if (gender.equalsIgnoreCase("F") && dayOfYear > 500) {

							if ((compute == dayOfYear - 500)
									&& (nic.substring(0, 2).equalsIgnoreCase(dateOfBirth.substring(8)))) {
								validNic = true;
							}
						}
					}

				}
			}
		} else if (nic.length() == 12) {// new nic
			if (nic.matches("^[0-9]{12}$")) {

				// check digit validation
				int tot = 0;
				int[] checkConstant = { 8, 4, 3, 2, 7, 6, 5, 8, 4, 3, 2 };
				for (int i = 0; i < 11; i++) {
					tot += Integer.parseInt(nic.substring(i, i + 1)) * checkConstant[i];
				}

				int checkDigit = Math.abs(tot % 11);
				String checkDig = null;
				if (checkDigit < 2) {
					checkDig = Integer.toString(0);
				}

				else {
					checkDig = Integer.toString(11 - checkDigit);
				}

				if ((nic.substring(11)).equalsIgnoreCase(checkDig)) {

					int dayOfYear = Integer.parseInt(nic.substring(4, 7));

					int month = Integer.parseInt(dateOfBirth.substring(3, 5));
					int days = Integer.parseInt(dateOfBirth.substring(0, 2));

					int numofmonths = month - 2;

					int sum = 0;

					for (int i = 0; i <= numofmonths; i++) {
						sum = sum + daysofLeapYrList.get(i);
					}

					int compute = sum + days;

					if (gender.equalsIgnoreCase("M") && dayOfYear < 367) {

						if (compute == dayOfYear && nic.substring(0, 4).equalsIgnoreCase(dateOfBirth.substring(6))) {

							validNic = true;
						}
					}
					if (gender.equalsIgnoreCase("F") && dayOfYear > 500) {

						if (compute == (dayOfYear - 500)
								&& nic.substring(0, 4).equalsIgnoreCase(dateOfBirth.substring(6))) {

							validNic = true;
						}
					}
				}
			}
		}
		return validNic;
	}
}