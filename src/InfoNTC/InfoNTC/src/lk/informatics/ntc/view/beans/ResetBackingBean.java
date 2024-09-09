package lk.informatics.ntc.view.beans;

import java.io.Serializable;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.service.UserService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "resetBean")
@ViewScoped
public class ResetBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String email;
	private String mobileNo;
	private boolean isUserIdEmpty;
	private boolean isEmailEmpty;
	private String resetErrorMessage;
	private String successMessage;
	private UserService userService;

	private String username;

	public ResetBackingBean() {
		userService = (UserService) SpringApplicationContex.getBean("userService");
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public boolean isUserIdEmpty() {
		return isUserIdEmpty;
	}

	public void setUserIdEmpty(boolean isUserIdEmpty) {
		this.isUserIdEmpty = isUserIdEmpty;
	}

	public boolean isEmailEmpty() {
		return isEmailEmpty;
	}

	public void setEmailEmpty(boolean isEmailEmpty) {
		this.isEmailEmpty = isEmailEmpty;
	}

	public String getResetErrorMessage() {
		return resetErrorMessage;
	}

	public void setResetErrorMessage(String resetErrorMessage) {
		this.resetErrorMessage = resetErrorMessage;
	}

	public String showResetDialog() {
		FacesContext fc = FacesContext.getCurrentInstance();
		this.username = getCountryParam(fc);

		return "result";
	}

	public String showLoginPage() {

		return "login";
	}

	public String getCountryParam(FacesContext fc) {

		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();

		if (getEmail() != null || mobileNo != null || getResetErrorMessage() != null || getSuccessMessage() != null) {

			clearAction();
			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			setUserId(params.get("username"));
			return params.get("username");

		} else {

			RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			setUserId(params.get("username"));
			return params.get("username");
		}

	}

	public void viewResetPasswordDialog() {
		boolean hasMobile = false;
		boolean isValidMobile = false;
		try {

			if (userId != null && !userId.trim().equalsIgnoreCase("")) {

				if (email != null && !email.trim().equalsIgnoreCase("")) {

					if (mobileNo != null && !mobileNo.trim().equalsIgnoreCase("")) {

						/** modifications added 2021-01-25 **/
						hasMobile = userService.userHaveMobile(userId);
						isValidMobile = userService.validMobileNoFOrUser(userId, mobileNo);
						if (hasMobile) {
							if (isValidMobile) {

								if (userService.resetPassword(userId, email, mobileNo) == true) {
									setSuccessMessage("Password Reset Successfully.");
									RequestContext.getCurrentInstance().execute("PF('successSve').hide()");
									RequestContext.getCurrentInstance().update("frmresetsuccessSve");
									RequestContext.getCurrentInstance().execute("PF('resetsuccessSve').show()");

								} else {
									setResetErrorMessage("Invalid Username or Email Address");
								}

							} else {
								setResetErrorMessage("Invalid Mobile Number");
							}
						} else {
							setResetErrorMessage("Currently User has not Mobile Number");
						}
						/** modifications added end 2021-01-25 **/

					} else {
						setResetErrorMessage("Mobile No. should be entered");
					}

				} else {
					setResetErrorMessage("Email Address should be entered");
				}

			} else {
				setResetErrorMessage("User ID should be entered");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void clearAction() {

		userId = null;
		email = null;
		mobileNo = null;
		resetErrorMessage = null;
		successMessage = null;

	}

}
