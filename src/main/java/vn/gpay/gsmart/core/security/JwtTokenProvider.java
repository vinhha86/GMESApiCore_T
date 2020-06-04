package vn.gpay.gsmart.core.security;

import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenProvider {

  /**
   * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
   * microservices environment, this key would be kept on a config-server.
   */
  //@Value("${security.jwt.token.secret-key:secret-key}")
  private String secretKey = "password";
  private static final Logger logger = LogManager.getLogger(JwtTokenProvider.class);

  @Autowired
  private GpayUserDetails myUserDetails;

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  /*public String createToken(String username, List<GpayRole> roles) {

    Claims claims = Jwts.claims().setSubject(username);
    claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));

    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()//
        .setClaims(claims)//
        .setIssuedAt(now)//
        .setExpiration(validity)//
        .signWith(SignatureAlgorithm.HS256, secretKey)//
        .compact();
  }*/

  public Authentication getAuthentication(String token) {
	  
	  
	  GpayUser userDetails = myUserDetails.loadUserByUserId(getUserId(token));
	 // UserDetails userDetails = (UserDetails)user;
	  //return new GpayAuthentication(userDetails, "", userDetails.getAuthorities(), userDetails.getId(), userDetails.getOrgid_link(),userDetails.getRootorgid_link(),userDetails.getOrg_type());
	  return new GpayAuthentication(userDetails, "", userDetails.getAuthorities(), userDetails.getId(), userDetails.getOrgid_link(),userDetails.getRootorgid_link(),userDetails.getOrg_type());
	  
  }

  public String getUsername(String token) {
	//  Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	  return "thieutv123@gmail.com";
  }
  public Long getUserId(String token) {
	Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	Long userid = Long.valueOf( claim.get("userid", Integer.class));
	return userid;
  }
  public Integer getOrgId(String token) {
	Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	Integer orgid = claim.get("orgid", Integer.class);
	return orgid;
  }
  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
    	//Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    	Date expDate = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
    	/* Compare with current time to validate token */
    	Date now = new Date();
    	logger.info(expDate.getTime());
    	long diff = now.getTime() - (long)(expDate.getTime()/1000);
    	if(diff > 300000L) { /* 5 min diff allow */
    		throw new CustomException("Expired JWT token", HttpStatus.UNAUTHORIZED);
    	}
    	return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new CustomException("Expired or invalid JWT token", HttpStatus.UNAUTHORIZED);
    }
  }

}
