package lk.informatics.ntc.view.util.validators;

import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.validate.ClientValidator;

@FacesValidator(value = "textValidator")
public class TextValidator implements Validator, ClientValidator {

	private Pattern pattern;

	private static final String PATTERN = "^[-a-zA-Z .,*!@0-9_:;(){}'%?/]*$";

	public TextValidator() {
		pattern = Pattern.compile(PATTERN);
	}

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value == null) {
			return;
		}

		if (!pattern.matcher(value.toString()).matches()) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error",
					"Invalid Character"));
		}
	}

	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public String getValidatorId() {
		return "textValidator";
	}
}
