package vn.gpay.gsmart.core.approle;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="app_role_menu")
@Entity
public class AppRoleMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_role_menu_generator")
	@SequenceGenerator(name="app_role_menu_generator", sequenceName = "app_role_menu_id_seq", allocationSize=1)
	private Long id;
	private String menuid_link;
	private Long roleid_link;
	public Long getId() {
		return id;
	}
	public String getMenuid_link() {
		return menuid_link;
	}
	public Long getRoleid_link() {
		return roleid_link;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setMenuid_link(String menuid_link) {
		this.menuid_link = menuid_link;
	}
	public void setRoleid_link(Long roleid_link) {
		this.roleid_link = roleid_link;
	}
	
	
}
