package vn.gpay.gsmart.core.laborlevel;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="laborlevel")
@Entity
public class LaborLevel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "laborlevel_generator")
	@SequenceGenerator(name="laborlevel_generator", sequenceName = "laborlevel_id_seq", allocationSize=1)
	protected Long id;
	
	private Long orgrootid_link;
	private String name;
	private String code;
	private String comment;
	public Long getId() {
		return id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	public String getComment() {
		return comment;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
