package vn.gpay.gsmart.core.approle;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="app_role_function")
@Entity
public class AppRoleFunction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_role_function_generator")
	@SequenceGenerator(name="app_role_function_generator", sequenceName = "app_role_function_id_seq", allocationSize=1)
	private Long id;
	private Long roleid_link;
	private Long functionid_link;
	private Boolean ishidden;
	private Boolean isreadonly;
	
	
	public Long getId() {
		return id;
	}
	public Long getRoleid_link() {
		return roleid_link;
	}
	public Long getFunctionid_link() {
		return functionid_link;
	}
	public Boolean getIshidden() {
		return ishidden;
	}
	public Boolean getIsreadonly() {
		return isreadonly;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setRoleid_link(Long roleid_link) {
		this.roleid_link = roleid_link;
	}
	public void setFunctionid_link(Long functionid_link) {
		this.functionid_link = functionid_link;
	}
	public void setIshidden(Boolean ishidden) {
		this.ishidden = ishidden;
	}
	public void setIsreadonly(Boolean isreadonly) {
		this.isreadonly = isreadonly;
	}
	
	
}
