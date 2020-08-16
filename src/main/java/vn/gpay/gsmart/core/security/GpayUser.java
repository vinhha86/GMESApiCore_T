package vn.gpay.gsmart.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import vn.gpay.gsmart.core.approle.AppRoleFunction;
import vn.gpay.gsmart.core.approle.AppRole_User;
import vn.gpay.gsmart.core.org.Org;

@Entity
@Table(name = "app_user")
public class GpayUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
//	@SequenceGenerator(name="user_generator", sequenceName = "user_id_seq", allocationSize=1)
	protected Long id;
	
	private String email;
	
	private String username;
	
	private String password;
	
	private String firstname;
	
	private String middlename;
	private String fullname;
	private String lastname;
	private Integer status;
	private String useremail;
	
	private Long orgid_link;
	
	private Long rootorgid_link;
	
	private Integer org_type;
	
	private Boolean enabled;
	private String userrole;
	private String tel_mobile;
	private String tel_office;
	private Long org_grant_id_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
	//@BatchSize(size=10
	@JoinColumn( name="user_id", referencedColumnName="id")
	private List<AppRole_User>  list_menu  = new ArrayList<AppRole_User>();
	
	@Transient
	@JsonProperty("Id")
	public Long Id() {
		return id;
	}
	
	@Transient
	@JsonProperty("Name")
	public String Name() {
		return fullname;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	//@BatchSize(size=10)
	@JoinColumn( name="orgid_link",insertable=false,updatable =false)
	private Org org ;
	
	@Transient
	public String getOrgname() {
		if(org!=null)
			return org.getName();
		return "";
	}
	
	@Transient
	public List<AppRoleFunction> getList_function(){
		List<AppRoleFunction> list = new ArrayList<AppRoleFunction>();
		for(AppRole_User role: list_menu) {
			list.addAll(role.getList_function());
		}
		return list;
	}
	
	@Transient
	public String getUsergroup_name() {
		String listmenu = "";
		for(AppRole_User menu : list_menu) {
			
			if(listmenu=="")
				listmenu += menu.getRolename();
			else
				listmenu += ", " + menu.getRolename();
		}
		
		return listmenu;
	}
	@Transient
	
	public Long index;
	
	@Transient
	public String getFullName() {
		String name = firstname;
		if(middlename != null)
			name += " "+ middlename;
		name += " " + lastname;
		return name;
	}
	
	public String getTel_mobile() {
		return tel_mobile;
	}

	public String getTel_office() {
		return tel_office;
	}

	public void setTel_mobile(String tel_mobile) {
		this.tel_mobile = tel_mobile;
	}

	public void setTel_office(String tel_office) {
		this.tel_office = tel_office;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "app_role_user", joinColumns = {
	@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
	@JoinColumn(name = "role_id", referencedColumnName = "id") })
	private List<GpayRole> roles = new ArrayList<GpayRole>();
	
	@Column(name = "account_locked")
	private boolean accountNonLocked;

	@Column(name = "account_expired")
	private boolean accountNonExpired;

	@Column(name = "credentials_expired")
	private boolean credentialsNonExpired;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_user", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", referencedColumnName = "id") })

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	
	
	public String getUserrole() {
		return userrole;
	}

	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !accountNonExpired;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialsNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountNonLocked;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

		roles.forEach(r -> {
			authorities.add(new SimpleGrantedAuthority(r.getName()));
		});

		return authorities;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public Long getRootorgid_link() {
		return rootorgid_link;
	}

	public void setRootorgid_link(Long rootorgid_link) {
		this.rootorgid_link = rootorgid_link;
	}

	public Integer getOrg_type() {
		return org_type;
	}

	public void setOrg_type(Integer org_type) {
		this.org_type = org_type;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<GpayRole> getRoles() {
		return roles;
	}

	public void setRoles(List<GpayRole> roles) {
		this.roles = roles;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public Long getOrg_grant_id_link() {
		return org_grant_id_link;
	}

	public void setOrg_grant_id_link(Long org_grant_id_link) {
		this.org_grant_id_link = org_grant_id_link;
	}
	
	
}
