package vn.gpay.gsmart.core.personnel_notmap;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="personnel_notmap")
@Entity
public class Personnel_notmap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personnel_notmap_generator")
	@SequenceGenerator(name="personnel_notmap_generator", sequenceName = "personnel_notmap_id_seq", allocationSize=1)
	protected Long id;
	private String register_code;
	private String register_name;
	public Long getId() {
		return id;
	}
	public String getRegister_code() {
		return register_code;
	}
	public String getRegister_name() {
		return register_name;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setRegister_code(String register_code) {
		this.register_code = register_code;
	}
	public void setRegister_name(String register_name) {
		this.register_name = register_name;
	}
	
	
}
