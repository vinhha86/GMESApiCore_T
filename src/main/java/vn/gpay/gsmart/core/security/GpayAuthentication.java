package vn.gpay.gsmart.core.security;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class GpayAuthentication extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 19998888L;

	public GpayAuthentication(Object principal, Object credentials, long userid, long orgid) {
        super(principal, credentials);
        this.userId = userid;
        this.orgId = orgid;
        this.rootorgid_link = orgid;
    }

    public GpayAuthentication(Object principal, Object credentials,  Collection<? extends GrantedAuthority> authorities,long userid, long orgid,long rootorgid_link,int org_type) {
        super(principal, credentials, authorities);
        this.userId = userid;
        this.orgId = orgid;
        this.rootorgid_link = rootorgid_link;
        this.org_type = org_type;
    }
    
    private Long orgId;
    private Long userId;
    private Long rootorgid_link;
    private Integer org_type;
    
    public Long getUserId() {
        return userId;
    }   
    public Long getOrgId() {
        return orgId;
    }   
    public Long getRootorgid_link() {
        return rootorgid_link;
    }  
    
    public Integer getOrg_type() {
        return org_type;
    }  
    
    public boolean isOrgRoot() {
    	return this.rootorgid_link ==this.orgId ? true:false; 
    }
    
}
