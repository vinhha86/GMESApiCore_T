package vn.gpay.gsmart.core.approle;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="app_function")
@Entity
public class AppFunction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_function_generator")
	@SequenceGenerator(name="app_function_generator", sequenceName = "app_function_id_seq", allocationSize=1)
	private Long id;
	private String refid_view;
	private String refid_item;
	private String name;
	private String name_en;
	private String icon;
	private String menuid_link;
	public Long getId() {
		return id;
	}
	public String getRefid_view() {
		return refid_view;
	}
	public String getRefid_item() {
		return refid_item;
	}
	public String getName() {
		return name;
	}
	public String getName_en() {
		return name_en;
	}
	public String getIcon() {
		return icon;
	}
	public String getMenuid_link() {
		return menuid_link;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setRefid_view(String refid_view) {
		this.refid_view = refid_view;
	}
	public void setRefid_item(String refid_item) {
		this.refid_item = refid_item;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void setMenuid_link(String menuid_link) {
		this.menuid_link = menuid_link;
	}
	
	
}
