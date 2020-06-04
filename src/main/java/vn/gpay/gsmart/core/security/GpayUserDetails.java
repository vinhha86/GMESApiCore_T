package vn.gpay.gsmart.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class GpayUserDetails implements UserDetailsService {

  @Autowired
  private IGpayUserService gpayUserService;
   
	
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	  
	  GpayUser user = null;

		if (username.contains("@"))
			user = gpayUserService.findByEmail(username);
		else
			user = gpayUserService.findByUsername(username);

		if (user == null)
			throw new BadCredentialsException("Bad credentials");

		new AccountStatusUserDetailsChecker().check(user);

		return user;
  }
  public GpayUser loadUserByUserId(Long userid) throws UsernameNotFoundException {
	  
	  	GpayUser user = gpayUserService.findOne(userid);
		if (user == null)
			throw new BadCredentialsException("Bad credentials");

		new AccountStatusUserDetailsChecker().check(user);

		return user;
  }
  
  
}
