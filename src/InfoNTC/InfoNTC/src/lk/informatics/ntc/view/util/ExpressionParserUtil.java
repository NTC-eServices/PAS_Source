package lk.informatics.ntc.view.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class ExpressionParserUtil {

	public static void main(String[] args) {
		ExpressionParser parser = new SpelExpressionParser();

		StandardEvaluationContext context = new StandardEvaluationContext();
		double value = 17.05;
		context.setVariable("N", value);
		String expression = "#N*2";

		Expression exp = parser.parseExpression(expression);
		BigDecimal result = exp.getValue(context, BigDecimal.class);

		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		String formattedResult = decimalFormat.format(result);
		System.out.println("Result: " + formattedResult);
		System.out.println("Result: " + result);

		BigDecimal value1 = new BigDecimal("17.40");
		BigDecimal value2 = new BigDecimal("17.00");

		// Calculate the difference
		BigDecimal difference = value1.subtract(value2);

		System.out.println("Difference: " + difference);
	}

	// String expression = "17.05*2";
	// System.out.println(validateExpression(expression));
	/* check # tag found before every abbreviation */
	public static boolean isHashFoundBeforeAbbreviation(String expression) {
		boolean validExpression = true;
		if (expression.contains("#")) {
			for (int i = 0; i < expression.toCharArray().length; i++) {
				char c = expression.toCharArray()[i];
				if (Character.isAlphabetic(c)) {
					try {
						if (expression.toCharArray()[i - 1] != '#'
								&& !Character.isAlphabetic(expression.toCharArray()[i - 1])) {
							validExpression = false;
							return validExpression;
						}
					} catch (Exception e) {
						validExpression = false;
						return validExpression;
					}
				}
			}
		}
		return validExpression;
	}

	public static String validateExpression(String expression) {

		String message = "success";
		List<String> allowedLetters = Arrays.asList("N", "SM", "L", "SL", "EB", "SH", "SQ");
		List<String> abbreviationList = new ArrayList<String>();

		boolean validExpression = isHashFoundBeforeAbbreviation(expression);

		if (validExpression) {
			for (int i = 0; i < expression.toCharArray().length; i++) {
				char character = expression.toCharArray()[i];
				if (character == '#') {

					String abbreviation = null;

					try {

						if (Character.isAlphabetic(expression.toCharArray()[i + 1])) {
							abbreviation = Character.toString(expression.toCharArray()[i + 1]);
						} else {
							message = "An abbreviation required after the hash tag.";
							return message;
						}
						if (expression.toCharArray().length > i + 2) {
							if (Character.isAlphabetic(expression.toCharArray()[i + 2])) {
								abbreviation = abbreviation + Character.toString(expression.toCharArray()[i + 2]);
							}
						}
						if (expression.toCharArray().length > i + 3) {
							if (Character.isAlphabetic(expression.toCharArray()[i + 3])) {
								message = "Invalid abbreviation length.";
								return message;
							}
						}

					} catch (ArrayIndexOutOfBoundsException e) {
						message = "An abbreviation required after hash tag.";
						return message;
					}

					if (abbreviation != null) {
						if (allowedLetters.contains(abbreviation)) {
							abbreviationList.add(abbreviation);
							continue;
						} else {
							message = "Invalid abbreviation found in the expression.";
							return message;
						}
					}
				}
			}

			System.out.println(abbreviationList);

			if (expression.contains("#")) {
				Map<String, Object> contextMap = new HashMap<>();
				for (String abbreviation : abbreviationList) {
					contextMap.put(abbreviation, 1);
				}
				StandardEvaluationContext context = new StandardEvaluationContext();
				context.setVariables(contextMap);
				Double rate = expressionParser(expression, context);

				if (rate == null) {
					message = "Invalid expression. Try Again!";
					return message;
				}
			} else {
				Double rate = expressionParser(expression, new StandardEvaluationContext());
				if (rate == null) {
					message = "Invalid expression. Try Again!";
					return message;
				}
			}
		} else {
			message = "Invalid expression!";
			return message;
		}
		return message;
	}

	public static Double expressionParser(String expression, StandardEvaluationContext context) {
		try {
			ExpressionParser parser = new SpelExpressionParser();
			Expression exp = parser.parseExpression(expression);
			Double result = exp.getValue(context, Double.class);
			System.out.println("result " + result);
			return result;
		} catch (ParseException e) {
			return null;
		} catch (EvaluationException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}
