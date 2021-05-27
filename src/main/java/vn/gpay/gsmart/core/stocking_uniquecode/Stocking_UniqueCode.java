package vn.gpay.gsmart.core.stocking_uniquecode;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="stocking_uniquecode")
@Entity
public class Stocking_UniqueCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stocking_uniquecode_generator")
	@SequenceGenerator(name="stocking_uniquecode_generator", sequenceName = "stocking_uniquecode_id_seq", allocationSize=1)
	private Long id;
	
	private Long stocking_inout;
	
	private Integer stocking_type;
	private String stocking_prefix;
	private Integer stocking_max;
	public Long getStocking_inout() {
		return stocking_inout;
	}
	public void setStocking_inout(Long stocking_inout) {
		this.stocking_inout = stocking_inout;
	}
	public Integer getStocking_type() {
		return stocking_type;
	}
	public void setStocking_type(Integer stocking_type) {
		this.stocking_type = stocking_type;
	}
	public String getStocking_prefix() {
		return stocking_prefix;
	}
	public void setStocking_prefix(String stocking_prefix) {
		this.stocking_prefix = stocking_prefix;
	}
	public Integer getStocking_max() {
		return stocking_max;
	}
	public void setStocking_max(Integer stocking_max) {
		this.stocking_max = stocking_max;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
