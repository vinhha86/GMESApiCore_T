package vn.gpay.gsmart.core.approle;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="app_user_function")
@Entity
public class AppUserFunction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_function_generator")
	@SequenceGenerator(name="app_user_function_generator", sequenceName = "app_user_function_id_seq", allocationSize=1)
	private Long id;
	private Long userid_link;
	private Long functionid_link;
	private boolean ishidden;
	private boolean isreadonly;
	public Long getId() {
		return id;
	}
	public Long getUserid_link() {
		return userid_link;
	}
	public Long getFunctionid_link() {
		return functionid_link;
	}
	public boolean isIshidden() {
		return ishidden;
	}
	public boolean isIsreadonly() {
		return isreadonly;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setUserid_link(Long userid_link) {
		this.userid_link = userid_link;
	}
	public void setFunctionid_link(Long functionid_link) {
		this.functionid_link = functionid_link;
	}
	public void setIshidden(boolean ishidden) {
		this.ishidden = ishidden;
	}
	public void setIsreadonly(boolean isreadonly) {
		this.isreadonly = isreadonly;
	}
	
	
}
