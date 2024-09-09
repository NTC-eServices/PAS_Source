package lk.informatics.ntc.model.service;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lk.informatics.ntc.model.dto.UserDTO;
import lk.informatics.ntc.model.exception.UserNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

	private UserService userService;
	public static Logger logger = Logger.getLogger("UserDetailsServiceImpl");


	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		UserDTO user = null;

		try {

			user = userService.findUser(username);

			user.setAccountNonLocked(true);
			user.setAccountNonExpired(true);
			user.setCredentialsNonExpired(true);
			user.setEnabled(true);

		} catch (UserNotFoundException e) {
			logger.info("User " + username + " Not Found in User Tables");
		}

		return user;

	}

}
