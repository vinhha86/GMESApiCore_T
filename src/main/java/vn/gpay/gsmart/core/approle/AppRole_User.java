package vn.gpay.gsmart.core.approle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

@Entity
@Table(name = "app_role_user")
public class AppRole_User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_role_user_generator")
	@SequenceGenerator(name="app_role_user_generator", sequenceName = "app_role_user_id_seq", allocationSize=1)
	private Long id;
	
	private Long user_id;
	private Long role_id;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	//@BatchSize(size=10)
	@JoinColumn( name="role_id",insertable=false,updatable =false)
	private AppRole approle ;
	
	@Transient
	public List<AppRoleFunction> getList_function(){
		if(approle!=null)
			return approle.getList_function();
		return new ArrayList<AppRoleFunction>();
	}
	
	@Transient
	public String getRolename() {
		return approle.getName();
	}
	
	public Long getUser_id() {
		return user_id;
	}
	public Long getRole_id() {
		return role_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	

}
