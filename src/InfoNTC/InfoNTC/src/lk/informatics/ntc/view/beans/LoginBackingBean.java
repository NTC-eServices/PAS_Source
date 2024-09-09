package lk.informatics.ntc.view.beans;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lk.informatics.ntc.model.dto.AttorneyHolderDTO;
import lk.informatics.ntc.model.dto.ChangePWDTO;
import lk.informatics.ntc.model.dto.UserDTO;
import lk.informatics.ntc.model.dto.UserLoginHistoryDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.UserService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.AES;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBackingBean {

	private String username;
	private String password;
	private boolean userNameEmpty;
	private boolean passWordEmpty;
	private boolean userNameOrPassWordEmpty;
	private boolean login = true;

	private ChangePWDTO changePWDTO;
	private AttorneyHolderDTO attorneyDTO;
	private String registermsg;
	private UserService userService;
	private UserDTO userDTO;
	private String dialogMessage;
	private String loginErrorMessage;
	private String changePWDErrorMessage;
	private String msgPassword = " ";

	private boolean userIdEmpty;
	private boolean newPasswordEmpty;
	private boolean comPasswordEmpty;

	private VehicleInspectionService vehicleInspectionService;

	private int number;
	private boolean countDownStop;
	
	public static final String STRENGTH_BLANK = "blank";
	public static final String STRENGTH_STRONG = "strong";
	private String pwdStrength = STRENGTH_BLANK;

	public static Logger logger = Logger.getLogger(LoginBackingBean.class);
	
	private int maximumFailedLoginAttempts = 3;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	@ManagedProperty("#{authenticationManager}")
	private AuthenticationManager authenticationManager;

	@javax.annotation.PostConstruct
	public void init() {

		username = "";
		password = "";

		userService = (UserService) SpringApplicationContex.getBean("userService");
		Locale locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		sessionBackingBean.setLocale(locale);

		changePWDTO = new ChangePWDTO();
		pwdStrength = STRENGTH_BLANK;
		
		try {
			maximumFailedLoginAttempts = Integer.valueOf(PropertyReader.getPropertyValue("maximum.failed.login.attempts"));
		} catch (NumberFormatException e) {
			logger.error(e);
		} catch (ApplicationException e) {
			logger.error(e);
		}

	}

	public void onIdle() {
		number = 15;
		countDownStop = false;

		RequestContext.getCurrentInstance().execute("PF('dlgSessionTimeout').show()");
	}

	public void goToLoginPage() {

		try {
			userService.updateCounterStatusForUser(sessionBackingBean.getLoginUser());
			FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/login.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void increment() {
		if (!countDownStop) {
			number--;
			if (number <= 0) {
				RequestContext.getCurrentInstance().execute("PF('dlgSessionTimeout').hide()");
				try {
					userService.updateCounterStatusForUser(sessionBackingBean.getLoginUser());
					FacesContext.getCurrentInstance().getExternalContext().redirect("/InfoNTC/login.xhtml");
					vehicleInspectionService.updateCounter(sessionBackingBean.getLoginUser());
					setVehicleInspectionService(
							(VehicleInspectionService) SpringApplicationContex.getBean("vehicleInspectionService"));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void increaseNumber() {

		countDownStop = true;
		RequestContext.getCurrentInstance().execute("PF('dlgSessionTimeout').hide()");
	}

	public String save() {

		try {
			setChangePWDErrorMessage(null);

			if (changePWDTO.newPassword == null || changePWDTO.getNewPassword().trim().equalsIgnoreCase("")
					|| changePWDTO.confirmNewPW == null || changePWDTO.getConfirmNewPW().trim().equalsIgnoreCase("")) {

				newPasswordEmpty = true;
				comPasswordEmpty = true;

				setChangePWDErrorMessage("Please Enter a Valid Password");

				changePWDTO.newPassword = null;
				changePWDTO.confirmNewPW = null;

			} else if (changePWDTO.getNewPassword().trim().length() <= 7) {
				setChangePWDErrorMessage("Password should contain at least 8 characters.");

			} else if (!pwdStrength.equalsIgnoreCase(STRENGTH_STRONG)) {
				setChangePWDErrorMessage(
						"Password must contain at least one uppercase, lowercase, digit and a special character.");

			} else if (!ChangePWDTO.newPassword.equals(ChangePWDTO.confirmNewPW)) {

				newPasswordEmpty = true;
				comPasswordEmpty = true;
				setChangePWDErrorMessage("Password do not match");

				changePWDTO.newPassword = null;
				changePWDTO.confirmNewPW = null;

			} else {

				boolean value = userService.ChangePassword(username);

				return "login";

			}

		} catch (Exception e) {

		}
		return "";

	}

	public void passwordChange() {

		String username = sessionBackingBean.loginUser;

		String oldpass = userService.getOldPasswordForUsername(username);
		String secretKey = null;
		try {
			secretKey = PropertyReader.getPropertyValue("password.secretKey");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		String decript = AES.decrypt(oldpass, secretKey);

		UserService us = new UserService();
		String encriptPass = us.generatePassword(changePWDTO.getNewPassword());

		int count = changePWDTO.getNewPassword().length();

		if (username == null) {
			clear();
			setLoginErrorMessage("No User Found");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		}

		else if (changePWDTO.getOldPassword() == null || changePWDTO.getOldPassword().trim().equalsIgnoreCase("")
				|| changePWDTO.getNewPassword() == null || changePWDTO.getNewPassword().trim().equalsIgnoreCase("")
				|| changePWDTO.getConfirmNewPW() == null || changePWDTO.getConfirmNewPW().trim().equalsIgnoreCase("")) {

			clear();
			setLoginErrorMessage("Please Enter a Valid Password");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (count <= 7) {
			clear();
			setLoginErrorMessage("Password should contain at least 8 characters.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");
		} else if (!pwdStrength.equalsIgnoreCase(STRENGTH_STRONG)) {
			setLoginErrorMessage(
					"Password must contain at least one uppercase, lowercase, digit and a special character.");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (!changePWDTO.getNewPassword().equals(changePWDTO.getConfirmNewPW())) {
			clear();
			setLoginErrorMessage("Password do not match");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (!changePWDTO.getOldPassword().equals(decript)) {
			clear();
			setLoginErrorMessage("Old Password is Incorrect");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else if (changePWDTO.getOldPassword().equals(changePWDTO.getNewPassword())) {
			clear();
			setLoginErrorMessage("You can't use current password as new password");
			RequestContext.getCurrentInstance().update("frmrequiredField");
			RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

		} else {

			int pass = userService.updateChangePassword(encriptPass, username);
			if (pass == 0) {
				clear();

				RequestContext.getCurrentInstance().execute("PF('successSve').show()");
			} else

			{
				setLoginErrorMessage("Password Didn't changed");
				RequestContext.getCurrentInstance().update("frmrequiredField");
				RequestContext.getCurrentInstance().execute("PF('requiredField').show()");

			}

		}

	}

	public void clear() {

		changePWDTO.setNewPassword(null);
		changePWDTO.setOldPassword(null);
		changePWDTO.setConfirmNewPW(null);
		pwdStrength = STRENGTH_BLANK;
	}

	public void clearFormAction() {

		RequestContext context = RequestContext.getCurrentInstance();
		context.update(":loginForm");

		context.execute("PF('lostpwddlg').show();");
	}

	public String getChangePWDErrorMessage() {
		return changePWDErrorMessage;
	}

	public void setChangePWDErrorMessage(String changePWDErrorMessage) {
		this.changePWDErrorMessage = changePWDErrorMessage;
	}

	public String getDialogMessage() {
		return dialogMessage;
	}

	public void setDialogMessage(String dialogMessage) {
		this.dialogMessage = dialogMessage;
	}

	public boolean isUserNameEmpty() {
		return userNameEmpty;
	}

	public void setUserNameEmpty(boolean userNameEmpty) {
		this.userNameEmpty = userNameEmpty;
	}

	public boolean isPassWordEmpty() {
		return passWordEmpty;
	}

	public void setPassWordEmpty(boolean passWordEmpty) {
		this.passWordEmpty = passWordEmpty;
	}

	public String login() {
		return "loginSuccess";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isUserIdEmpty() {
		return userIdEmpty;
	}

	public void setUserIdEmpty(boolean userIdEmpty) {
		this.userIdEmpty = userIdEmpty;
	}

	public boolean isNewPasswordEmpty() {
		return newPasswordEmpty;
	}

	public void setNewPasswordEmpty(boolean newPasswordEmpty) {
		this.newPasswordEmpty = newPasswordEmpty;
	}

	public String getMsgPassword() {
		return msgPassword;
	}

	public void setMsgPassword(String msgPassword) {
		this.msgPassword = msgPassword;
	}

	public boolean isComPasswordEmpty() {
		return comPasswordEmpty;
	}

	public void setComPasswordEmpty(boolean comPasswordEmpty) {
		this.comPasswordEmpty = comPasswordEmpty;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isCountDownStop() {
		return countDownStop;
	}

	public void setCountDownStop(boolean countDownStop) {
		this.countDownStop = countDownStop;
	}

	public String doLogin() {
		login = true;
		try {

			setChangePWDErrorMessage(null);
			setLoginErrorMessage(null);

			String status = userService.getStatus(username);

			if (username == null || username.trim().equalsIgnoreCase("")) {
				userNameEmpty = true;
				passWordEmpty = false;
				userNameOrPassWordEmpty = true;
				setLoginErrorMessage("Username should be entered");
				
			} else if (password == null || password.trim().equalsIgnoreCase("")) {
				passWordEmpty = true;
				userNameEmpty = false;
				userNameOrPassWordEmpty = true;
				setLoginErrorMessage("Password should be entered");

			} else if (status.equals("R")) {
				passWordEmpty = true;
				userNameEmpty = false;
				userNameOrPassWordEmpty = true;
				login = false;
				setLoginErrorMessage("This user is no-longer available");
				
			} else if (status.equals("B")) {
				passWordEmpty = false;
				userNameEmpty = false;
				userNameOrPassWordEmpty = false;
				login = false;
				setLoginErrorMessage("This user is blocked! Contact administrator.");
				
			} else if (status.equals("A")) {
				passWordEmpty = false;
				userNameEmpty = false;
				userNameOrPassWordEmpty = false;
				login = true;

				LoginBackingBean logBean = new LoginBackingBean();
				String decryptedString = logBean.generatePassword(password);

				userNameOrPassWordEmpty = false;
				sessionBackingBean.setPassword(decryptedString);

				if (username != null) {
					username = username.trim();
				}

				Authentication request = new UsernamePasswordAuthenticationToken(username, decryptedString);
				authenticationManager = (AuthenticationManager) SpringApplicationContex
						.getBean("authenticationManager");
				Authentication result = authenticationManager.authenticate(request);
				SecurityContextHolder.getContext().setAuthentication(result);

				if (!(result instanceof AnonymousAuthenticationToken) && result.isAuthenticated()) {

					userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

					if (userDTO != null) {

						sessionBackingBean.setInitialPasswordChange(userDTO.getInitialPasswordChange());
						sessionBackingBean.setLoginUser(userDTO.getUsername());

						if (userDTO.getInitialPasswordChange() != null && !userDTO.getInitialPasswordChange().isEmpty()
								&& userDTO.getInitialPasswordChange().equalsIgnoreCase("Y") && login == true) {

							RequestContext.getCurrentInstance().execute("PF('changePWD').show()");
							ChangePWDTO.userName = username;
							pwdStrength = STRENGTH_BLANK;

						} else if (login == true) {

							UserLoginHistoryDTO userLoginHistoryDTO = new UserLoginHistoryDTO();
							userLoginHistoryDTO.setUserName(userDTO.getUsername());
							HttpServletRequest requst = (HttpServletRequest) FacesContext.getCurrentInstance()
									.getExternalContext().getRequest();
							String ipAddress = requst.getRemoteAddr();
							sessionBackingBean.setIpAddress(ipAddress);
							userLoginHistoryDTO.setMachineIp(ipAddress);

							// change for insert record into
							// nt_t_user_login_detail
							HttpSession session = requst.getSession();
							String sessionId = session.getId();

							userDTO.setSessionId(sessionId);
							sessionBackingBean.setCurrentSessionId(sessionId);

							long seq = userService.insertLoginHistory(userLoginHistoryDTO);
							sessionBackingBean.setLogHistorySeq(seq);

							if (userDTO.getFunctionList() != null) {
								Class<? extends SessionBackingBean> cls = sessionBackingBean.getClass();
								Object obj = sessionBackingBean;
								Class[] paramBool = new Class[1];
								paramBool[0] = boolean.class;
								for (Map.Entry<String, String> entry : userDTO.getFunctionList().entrySet()) {
									try {
										Method method = cls.getDeclaredMethod("set" + entry.getKey(), paramBool);
										method.invoke(obj, true);
									} catch (Exception e) {
										logger.error(entry.getValue() + " role not found in SessionBackingBean");
									}
								}
							}

							FacesContext fcontext = FacesContext.getCurrentInstance();
							fcontext.getExternalContext().getSessionMap().put("username", username);
							fcontext.getExternalContext().getSessionMap().put("pwd", password);

							/** update user assigned counters start **/
							userService.updateCounterStatusForUser(username);
							/** update user assigned counters end **/

							/** Insert user in nt_t_user_login_detail **/
							userService.insertLoginDetails(username, userDTO);

							// set failed login attempts to null
							userService.updateLoginAttempts(username, 0);

							pwdStrength = STRENGTH_BLANK;
							return "welcomePage";
						} else {
							passWordEmpty = true;
							userNameEmpty = false;
							userNameOrPassWordEmpty = true;
							setLoginErrorMessage("This User is no-longer available");
						}
					}

				} else {
					setLoginErrorMessage("User Not Authenticated. " + result);
				}

			} else {
				passWordEmpty = true;
				userNameEmpty = false;
				userNameOrPassWordEmpty = true;
				setLoginErrorMessage("Invalid Username/Password");
			}

		} catch (BadCredentialsException e) {
			// get failed login attempts counts of the user
			int faildLoginAttemptsPrev = userService.getFaildLoginAttempts(username);
			int faildLoginAttemptsNow = faildLoginAttemptsPrev +1;
			
			// increment failed login attempts by one (save faildLoginAttemptsNow to database)
			userService.updateLoginAttempts(username, faildLoginAttemptsNow);
			
			if(faildLoginAttemptsNow==maximumFailedLoginAttempts) {
				// block the user
				userService.blockUser(username);
				setLoginErrorMessage("This user is blocked! Contact administrator.");
				
			} else {
				int remaningAttempts = maximumFailedLoginAttempts-faildLoginAttemptsNow;
				setLoginErrorMessage("Incorrect password. Only "+remaningAttempts+" attempt/attempts remaining!");
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}

	public boolean isUserNameOrPassWordEmpty() {
		return userNameOrPassWordEmpty;
	}

	public void setUserNameOrPassWordEmpty(boolean userNameOrPassWordEmpty) {
		this.userNameOrPassWordEmpty = userNameOrPassWordEmpty;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public AttorneyHolderDTO getAttorneyDTO() {
		return attorneyDTO;
	}

	public void setAttorneyDTO(AttorneyHolderDTO attorneyDTO) {
		this.attorneyDTO = attorneyDTO;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public String doLogout() throws IOException {

		setVehicleInspectionService(
				(VehicleInspectionService) SpringApplicationContex.getBean("vehicleInspectionService"));

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Object session = externalContext.getSession(false);
		HttpSession httpSession = (HttpSession) session;

		try {

			/** Inactive login user counters start **/
			if (sessionBackingBean.getLoginUser() != null && !sessionBackingBean.getLoginUser().isEmpty()
					&& !sessionBackingBean.getLoginUser().trim().equalsIgnoreCase("")) {
				userService.updateCounterStatusForUser(sessionBackingBean.getLoginUser());
				vehicleInspectionService.updateCounter(sessionBackingBean.getLoginUser());
			}
			/** Inactive login user counters end **/

			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
				userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				SecurityContextHolder.getContext().setAuthentication(null);
				HttpServletResponse hsr = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
						.getResponse();
				hsr.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP
																						// 1.1.
				hsr.setHeader("Pragma", "no-cache"); // HTTP 1.0.
				hsr.setDateHeader("Expires", 0); // Proxies.

			}

			/** Update nt_t_user_login_detail table --> logOutTime **/
			userService.updateUserDetails(sessionBackingBean.getLoginUser(), sessionBackingBean.getCurrentSessionId());

		} catch (NullPointerException e) {
			logger.error(e.toString());
		}

		if (httpSession != null) {
			httpSession.invalidate();
		}
		return "/login.xhtml?faces-redirect=true";

	}

	public String getRegistermsg() {
		return registermsg;
	}

	public void setRegistermsg(String registermsg) {
		this.registermsg = registermsg;
	}

	public void setLanguage(String language) {
		// language en,si,ta
		Locale locale = new Locale(language);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	public String getLoginErrorMessage() {
		return loginErrorMessage;
	}

	public void setLoginErrorMessage(String loginErrorMessage) {
		this.loginErrorMessage = loginErrorMessage;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public ChangePWDTO getChangePWDTO() {
		return changePWDTO;
	}

	public void setChangePWDTO(ChangePWDTO changePWDTO) {
		this.changePWDTO = changePWDTO;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		LoginBackingBean.logger = logger;
	}

	public String cancel() {
		setChangePWDErrorMessage(null);
		return "login";

	}

	public String generatePassword(String password) {

		String generatedPassword;

		String secretKey = null;
		try {
			secretKey = PropertyReader.getPropertyValue("password.secretKey");
		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		generatedPassword = AES.encrypt(password, secretKey);
		String decryptedString = AES.decrypt(generatedPassword, secretKey);

		return generatedPassword;
	}

	public VehicleInspectionService getVehicleInspectionService() {
		return vehicleInspectionService;
	}

	public void setVehicleInspectionService(VehicleInspectionService vehicleInspectionService) {
		this.vehicleInspectionService = vehicleInspectionService;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public String getPwdStrength() {
		return pwdStrength;
	}

	public void setPwdStrength(String pwdStrength) {
		this.pwdStrength = pwdStrength;
	}

	public void checkStrength() {
		pwdStrength = "weak";
		// Checking lower alphabet in string
		if (changePWDTO.getNewPassword() != null) {
			int n = changePWDTO.getNewPassword().length();
			boolean hasLower = false, hasUpper = false, hasDigit = false, specialChar = false;
			Set<Character> set = new HashSet<Character>(
					Arrays.asList('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+'));
			for (char i : changePWDTO.getNewPassword().toCharArray()) {
				if (Character.isLowerCase(i))
					hasLower = true;
				if (Character.isUpperCase(i))
					hasUpper = true;
				if (Character.isDigit(i))
					hasDigit = true;
				if (set.contains(i))
					specialChar = true;
			}

			// Strength of password
			if (n == 0) {
				pwdStrength = STRENGTH_BLANK;
			} else if (hasDigit && hasLower && hasUpper && specialChar && (n >= 8)) {
				pwdStrength = STRENGTH_STRONG;
			} else if ((hasLower || hasUpper || specialChar) && (n >= 6)) {
				pwdStrength = "medium";
			} else {
				pwdStrength = "weak";
			}
		}
	}

}
