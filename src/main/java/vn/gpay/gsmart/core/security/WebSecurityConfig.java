package vn.gpay.gsmart.core.security;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
	//@Autowired
    //private GpayUserRepository userRepository;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
    
    @Override
	public void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
    	
    	// No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
    	http.authorizeRequests()
        .antMatchers("/api/v1/test/**").permitAll()
        .anyRequest().authenticated()
        .and()
    	.exceptionHandling().accessDeniedHandler(new AccessDeniedExceptionHandler());
    	
    	// Apply JWT
    	http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    	//.exceptionHandling().accessDeniedHandler(new AccessDeniedExceptionHandler())
    	//http.httpBasic().authenticationEntryPoint(getBasicAuthEntryPoint());
	}
    
    /* To allow Pre-flight [OPTIONS] request from browser */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**")
        .and().ignoring().
         antMatchers("/v2/api-docs")//
        .antMatchers("/swagger-resources/**")//
        .antMatchers("/swagger-ui.html")//
        .antMatchers("/configuration/**")//
        .antMatchers("/webjars/**")//
        .antMatchers("/public");

    }
    
    public class AccessDeniedExceptionHandler implements AccessDeniedHandler
    {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response,
                AccessDeniedException ex) throws IOException, ServletException {
        	String uri = request.getServletPath();
        	GpayAuthentication auth = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Long userid = auth.getUserId();
			
            response.setStatus(403/*HttpStatus.FORBIDDEN*/);
            response.getWriter().print("{\"error\":\""+ex.getMessage()+"\", \"uri\":\""+uri+"\"" +",\"userid\":\""+ userid+"\"}");
        }
    }
}
