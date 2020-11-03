package vn.gpay.gsmart.core.security;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.org.Org;


@Entity
@Table(name = "app_user_org")
public class GpayUserOrg implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_org_generator")
	@SequenceGenerator(name="app_user_org_generator", sequenceName = "app_user_org_id_seq", allocationSize=1)
	protected Long id;
	
	private Long userid_link;
	private Long orgid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	//@BatchSize(size=10)
	@JoinColumn( name="orgid_link",insertable=false,updatable =false)
	private Org org ;
	
	@Transient
	public String getOrgcode() {
		if(org!=null)
			return org.getCode();
		return "";
	}
	@Transient
	public String getOrgname() {
		if(org!=null)
			return org.getName();
		return "";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserid_link() {
		return userid_link;
	}
	public void setUserid_link(Long userid_link) {
		this.userid_link = userid_link;
	}
	public Long getOrgid_link() {
		return orgid_link;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

}
