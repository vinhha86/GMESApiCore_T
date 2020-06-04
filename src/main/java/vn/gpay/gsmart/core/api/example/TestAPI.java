package vn.gpay.gsmart.core.api.example;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.security.GpayAuthentication;

@RestController
@RequestMapping("/api/v1/test")

public class TestAPI {

	@RequestMapping(value = "/user",method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('role_admin')")
	public GpayAuthentication user(Principal principal) {
			
			GpayAuthentication auth = (GpayAuthentication)	 principal;
	        return auth;
	}
	@RequestMapping(value = "/user1",method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('role_user')")
	public GpayAuthentication user1(Principal principal) {
		GpayAuthentication auth = (GpayAuthentication)	 principal;
	    return auth;
	 }
	  @RequestMapping(value = "/usertest",method = RequestMethod.GET)
	  public String usertest() {
			
	        return "hahah";
	  }
}
