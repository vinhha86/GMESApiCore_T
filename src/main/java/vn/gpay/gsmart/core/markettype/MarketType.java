package vn.gpay.gsmart.core.markettype;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="markettype")
@Entity
public class MarketType implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "markettype_generator")
	@SequenceGenerator(name="markettype_generator", sequenceName = "markettype_id_seq", allocationSize=1)
	private Long id;
	private String code;
	private String name;
	private String name_en;
	private Integer status;
	public Long getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public String getName_en() {
		return name_en;
	}
	public Integer getStatus() {
		return status;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
}
