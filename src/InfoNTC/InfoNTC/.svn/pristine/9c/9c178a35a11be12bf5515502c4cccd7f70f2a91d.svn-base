package lk.informatics.ntc.view.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 * General useful static utilities for working with JSF.
 * 
 * @author Duncan Mills <br>
 *         <i> $Id: JSFUtils.java,v 1.7 2006/01/17 15:36:32 duncan Exp $</i>
 */
public class JSFUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NO_RESOURCE_FOUND = "Missing resource: ";

	/**
	 * Return the FacesContext instance for the current request.
	 */
	protected static FacesContext context() {
		return (FacesContext.getCurrentInstance());

	}

	private static ValueExpression createValueExpression(FacesContext facesContext, String expression) {
		Application application = facesContext.getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		ValueExpression valueExpression = expressionFactory.createValueExpression(facesContext.getELContext(),
				expression, Object.class);
		return valueExpression;
	}

	/**
	 * Method for taking a reference to a JSF binding expression and returning the
	 * matching object (or creating it).
	 * 
	 * @param expression
	 * @return Managed object
	 */
	public static Object resolveExpression(String expression) {
		// Application app = context().getApplication();
		// ValueBinding bind = app.createValueBinding(expression);
		// return bind.getValue(context());
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return createValueExpression(facesContext, expression).getValue(facesContext.getELContext());
	}

	/**
	 * Convenience method for resolving a reference to a managed bean by name rather
	 * than by expression.
	 * 
	 * @param beanName
	 * @return Managed object
	 */
	public static Object getManagedBeanValue(String beanName) {
		StringBuffer buff = new StringBuffer("#{");
		buff.append(beanName);
		buff.append("}");
		return resolveExpression(buff.toString());
	}

	/**
	 * Method for setting a new object into a JSF managed bean. Note: will fail
	 * silently if the supplied object does not match the type of the managed bean
	 * 
	 * @param expression
	 * @param newValue
	 */
//    @SuppressWarnings("unchecked")
	public static void setExpressionValue(String expression, Object newValue) {
//        Application app = context().getApplication();
//        ValueBinding bind = app.createValueBinding(expression);
//
//        //Check that the input newValue can be cast to the property type
//        //expected by the managed bean. 
//        //If the managed Bean expects a primitive we rely on Auto-Unboxing
//        //I could do a more comprehensive check and conversion from the object 
//        //to the equivilent primitive but life is too short
//        Class bindClass = bind.getType(context());
//        if (bindClass.isPrimitive() || bindClass.isInstance(newValue)) {
//            bind.setValue(context(), newValue);
//        }
		FacesContext facesContext = FacesContext.getCurrentInstance();
		createValueExpression(facesContext, expression).setValue(facesContext.getELContext(), newValue);
	}

	/**
	 * Convenience method for setting the value of a managed bean by name rather
	 * than by expression.
	 * 
	 * @param beanName
	 * @param newValue
	 */
	public static void setManagedBeanValue(String beanName, Object newValue) {
		StringBuffer buff = new StringBuffer("#{");
		buff.append(beanName);
		buff.append("}");
		setExpressionValue(buff.toString(), newValue);
	}

	/**
	 * Convenience method for setting Session variable.
	 * 
	 * @param key    object key
	 * @param object value to store
	 */
	@SuppressWarnings("unchecked")
	public static void storeOnSession(String key, Object object) {
		Map sessionState = context().getExternalContext().getSessionMap();
		sessionState.put(key, object);
	}

	/**
	 * Convenience method for setting Session variables.
	 * 
	 * @param key    object key
	 * @param object value to store
	 */
	@SuppressWarnings("unchecked")
	public static void storeOnSession(Map map) {
		Map sessionState = context().getExternalContext().getSessionMap();
		sessionState.putAll(map);
	}

	/**
	 * Convenience method for setting request variables.
	 * 
	 * @param key    object key
	 * @param object value to store
	 */
	public static void storeOnRequest(String key, String value) {
		Map<String, String> requestParams = context().getExternalContext().getRequestParameterMap();
		requestParams.put(key, value);
	}

	public static void storeOnRequestMap(String key, Object value) {
		Map<String, Object> requestMap = context().getExternalContext().getRequestMap();
		requestMap.put(key, value);
	}

	public static Object getFromRequestMap(String key) {
		return context().getExternalContext().getRequestMap().get(key);
	}

	/**
	 * Convenience method for getting Session variables.
	 * 
	 * @param key object key
	 */
	@SuppressWarnings("unchecked")
	public static Object removeFromSession(String key) {
		Map sessionState = context().getExternalContext().getSessionMap();
		return sessionState.remove(key);
	}

	/**
	 * Convenience method for getting request variable.
	 * 
	 * @param key object key
	 */
	public static Object getFromRequest(String key) {
		return context().getExternalContext().getRequestParameterMap().get(key);
	}

	/**
	 * Convenience method for getting request variables.
	 * 
	 * @param key object key
	 */
	public static Map<String, String> getRequestParameterMap() {
		return context().getExternalContext().getRequestParameterMap();
	}

	/**
	 * Pulls a String resource from the property bundle that is defined under the
	 * application &lt;message-bundle&gt; element in the faces config. Respects
	 * Locale
	 * 
	 * @param key
	 * @return Resource value or placeholder error String
	 */
	public static String getStringFromBundle(String key) {
		ResourceBundle bundle = getBundle();
		return getStringSafely(bundle, key, null);
	}

	/**
	 * Convenience method to construct a <code>FacesMesssage</code> from a defined
	 * error key and severity This assumes that the error keys follow the convention
	 * of using <b>_detail</b> for the detailed part of the message, otherwise the
	 * main message is returned for the detail as well.
	 * 
	 * @param key      for the error message in the resource bundle
	 * @param severity
	 * @return Faces Message object
	 */
	public static FacesMessage getMessageFromBundle(String key, FacesMessage.Severity severity) {
		ResourceBundle bundle = getBundle();
		String summary = getStringSafely(bundle, key, null);
		String detail = getStringSafely(bundle, key + "_detail", summary);
		FacesMessage message = new FacesMessage(summary, detail);
		message.setSeverity(severity);
		return message;
	}

	/**
	 * This method will retrieve a message from the bundle and populate the message
	 * with the given String arguments.
	 * 
	 * @param key      The message key
	 * @param severity The Severity level.
	 * @param args     The Object arguments in an object array.
	 * @return The FacesMessage formated with the arguments
	 * @author Prashan
	 */
	public static FacesMessage getMessageFromBundleWithArgs(String key, FacesMessage.Severity severity, Object[] args) {
		ResourceBundle bundle = getBundle();
		String summary = getStringSafely(bundle, key, null);
		summary = MessageFormat.format(summary, args);
		String detail = getStringSafely(bundle, key + "_detail", summary);
		FacesMessage message = new FacesMessage(summary, detail);
		message.setSeverity(severity);
		return message;
	}

	public static void addFacesInfoMessage(String key) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, getMessageFromBundle(key, FacesMessage.SEVERITY_INFO));
	}

	public static void addFacesInfoMessage(String key, Object[] args) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, getMessageFromBundleWithArgs(key, FacesMessage.SEVERITY_INFO, args));
	}

	public static void addFacesInfoMessage(UIComponent component, String key) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(component.getClientId(facesContext),
				getMessageFromBundle(key, FacesMessage.SEVERITY_INFO));
	}

	public static void addFacesInfoMessage(UIComponent component, String key, Object[] args) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(component.getClientId(facesContext),
				getMessageFromBundleWithArgs(key, FacesMessage.SEVERITY_INFO, args));
	}

	public static void addFacesWarnMessage(String key) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, getMessageFromBundle(key, FacesMessage.SEVERITY_WARN));
	}

	public static void addFacesWarnMessage(String key, Object[] args) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, getMessageFromBundleWithArgs(key, FacesMessage.SEVERITY_WARN, args));
	}

	public static void addFacesErrorMessage(String key) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, getMessageFromBundle(key, FacesMessage.SEVERITY_ERROR));
	}

	public static void addFacesErrorMessage(String key, Object[] args) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, getMessageFromBundleWithArgs(key, FacesMessage.SEVERITY_ERROR, args));
	}

	public static void addFacesErrorMessage(UIComponent component, String key) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(component.getClientId(facesContext),
				getMessageFromBundle(key, FacesMessage.SEVERITY_ERROR));
	}

	public static void addFacesErrorMessage(UIComponent component, String key, Object[] args) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(component.getClientId(facesContext),
				getMessageFromBundleWithArgs(key, FacesMessage.SEVERITY_ERROR, args));
	}

	public static void addFacesErrorMessage(Exception exception) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String summary = exception.getMessage();
		FacesMessage message = new FacesMessage(summary);
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		facesContext.addMessage(null, message);
	}

	public static void addFacesErrorMessage(UIComponent component, Exception exception) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String summary = exception.getMessage();
		FacesMessage message = new FacesMessage(summary);
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		facesContext.addMessage(component.getClientId(facesContext), message);
	}

	/*
	 * Internal method to pull out the correct local message bundle
	 */
	private static ResourceBundle getBundle() {
		UIViewRoot uiRoot = context().getViewRoot();
		Locale locale = uiRoot.getLocale();
		ClassLoader ldr = Thread.currentThread().getContextClassLoader();
		return ResourceBundle.getBundle(context().getApplication().getMessageBundle(), locale, ldr);
	}

	/*
	 * Internal method to proxy for resource keys that don't exist
	 */
	private static String getStringSafely(ResourceBundle bundle, String key, String defaultValue) {
		String resource = null;
		try {
			resource = bundle.getString(key);
		} catch (MissingResourceException mrex) {
			if (defaultValue != null) {
				resource = defaultValue;
			} else {
				resource = NO_RESOURCE_FOUND + key;
			}
		}
		return resource;
	}

	/**
	 * Method to return a property from a property file
	 * 
	 * @param bundleName
	 * @param key
	 * @return
	 */
	public static String getStringFromBundle(String bundleName, String key) {
		String value = key;
		ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName);
		value = resourceBundle.getString(key);
		return value;
	}

	public static String getStringFromBundle(String bundleName, String key, Object[] args) {
		return MessageFormat.format(getStringFromBundle(bundleName, key), args);
	}

	/**
	 * Convenience method for getting Session variables.
	 * 
	 * @param key object key
	 */
	public static Object getRequestParameter(String key) {
		return context().getExternalContext().getRequestParameterMap().get(key);
	}

	/**
	 * Convenience method for getting Session variables. Please make sure to call
	 * this.removeFromSession method once session variable is used.
	 * 
	 * @param key object key
	 */
	@SuppressWarnings("unchecked")
	public static Object getFromSession(String key) {
		Map sessionState = context().getExternalContext().getSessionMap();
		return sessionState.get(key);
	}

	/**
	 * Method to return the selected object by Id
	 * 
	 * @param id
	 * @return selected object
	 */
	public static Object getSelectedObjectByID(String id) {
		Object obj = context().getExternalContext().getRequestMap().get(id);
		return (obj);
	}

	/**
	 * Check Managed Bean already exists. In either request or session scope
	 */
	public static boolean isManagedBeanExists(String backingBeanAlias) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().containsKey(backingBeanAlias)
				|| FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(backingBeanAlias);
	}

	/**
	 * Remove value associated to key from RequestMap.
	 * 
	 * @param key - Key of Object to remove.
	 */
	public static void removeFromRequestMap(String key) {
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().remove(key);
	}

	/**
	 * <p>
	 * This method will call <code>resetValue()</code> method on all input
	 * components in the root
	 * </p>
	 * 
	 * @param component The root component
	 * @author M. Isuru Tharanga Chrishantha Perera
	 */
	public static void resetValues(UIComponent component) {
		if (component == null) {
			return;
		}

		List<UIComponent> list = component.getChildren();
		for (UIComponent child : list) {
			resetValues(child);
		}

		if (component instanceof UIInput) {
			UIInput input = (UIInput) component;
			input.resetValue();
		}
	}

	public static void updateValues(UIComponent component, FacesContext fc) {
		if (component == null) {
			return;
		}

		List<UIComponent> list = component.getChildren();
		for (UIComponent child : list) {
			updateValues(child, fc);
		}

		if (component instanceof UIInput) {
			UIInput input = (UIInput) component;
			input.updateModel(fc);
		}
	}

	/**
	 * <p>
	 * Resets all <code>UIInput</code>s within the component.
	 * 
	 * @param component - <code>UIComponent</code> - Parent Component.
	 *                  </p>
	 * 
	 * @author Nuwan Dias
	 */
	public static void resetInputComponents(UIComponent component) {
		if (component instanceof UIInput) {
			UIInput uiInput = (UIInput) component;
			uiInput.resetValue();
		} else if (component instanceof UIData) {
			UIData uiData = (UIData) component;
			int first = uiData.getFirst();
			int rows = uiData.getRows();
			int last;
			if (rows == 0) {
				last = uiData.getRowCount();
			} else {
				last = first + rows;
			}
			for (int rowIndex = first; rowIndex < last; rowIndex++) {
				uiData.setRowIndex(rowIndex);
				if (uiData.isRowAvailable()) {
					for (Iterator<UIComponent> iter = uiData.getChildren().iterator(); iter.hasNext();) {
						resetInputComponents(iter.next());
					}
				}
			}
			uiData.setRowIndex(-1);
		} else {
			for (Iterator<UIComponent> iter = component.getChildren().iterator(); iter.hasNext();) {
				resetInputComponents(iter.next());
			}
		}
	}

	/**
	 * <p>
	 * Resets all <code>UIInput</code>s within the component and clear childrens
	 * 
	 * @param component - <code>UIComponent</code> - Parent Component.
	 *                  </p>
	 * 
	 * @author M. Isuru Tharanga Chrishantha Perera
	 */
	public static void resetAndClearChildComponents(UIComponent component) {
		if (component instanceof UIInput) {
			UIInput uiInput = (UIInput) component;
			uiInput.resetValue();
		} else if (component instanceof UIData) {
			UIData uiData = (UIData) component;
			int first = uiData.getFirst();
			int rows = uiData.getRows();
			int last;
			if (rows == 0) {
				last = uiData.getRowCount();
			} else {
				last = first + rows;
			}
			for (int rowIndex = first; rowIndex < last; rowIndex++) {
				uiData.setRowIndex(rowIndex);
				if (uiData.isRowAvailable()) {
					for (Iterator<UIComponent> iter = uiData.getChildren().iterator(); iter.hasNext();) {
						resetAndClearChildComponents(iter.next());
					}
				}
			}
			uiData.setRowIndex(-1);
		} else {
			for (Iterator<UIComponent> iter = component.getChildren().iterator(); iter.hasNext();) {
				resetAndClearChildComponents(iter.next());
			}
		}
		component.getChildren().clear();
	}

	public static boolean valueChanged(ValueChangeEvent event) {
		boolean contentChanged = true;
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		// Value change from null to empty string can be ignored.
		if (oldValue == null && newValue != null && newValue instanceof String) {
			String value = (String) newValue;
			contentChanged = !value.equals("");
		} else
		// Value change from empty string to null can be ignored.
		if (newValue == null && oldValue != null && oldValue instanceof String) {
			String value = (String) oldValue;
			contentChanged = !value.equals("");
		} else
		// equals method in BigDecimal cannot be used to check value is
		// changed.
		// Using compareTo method to check the value is actually changed.
		if (oldValue != null && oldValue instanceof BigDecimal) {
			if (newValue != null && newValue instanceof BigDecimal) {
				contentChanged = (((BigDecimal) oldValue).compareTo((BigDecimal) newValue) != 0);
			}
		}
		return contentChanged;
	}

}
