package vn.gpay.gsmart.core.position;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="personnel_position")
@Entity
public class Position implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "position_generator")
	@SequenceGenerator(name="position_generator", sequenceName = "position_id_seq", allocationSize=1)
	protected Long id;
	private String name;
	private String name_en;
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getName_en() {
		return name_en;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	
	
}
