package vn.gpay.gsmart.core.pcontractconfigamount;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="pcontract_config_amount")
@Entity
public class ConfigAmount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_config_amount_generator")
	@SequenceGenerator(name="pcontract_config_amount_generator", sequenceName = "pcontract_config_amount_id_seq", allocationSize=1)
	private Long id;
	private Integer amount_from;
	private Integer amount_to;
	private Float amount_plus;
	private Integer type;
	public Long getId() {
		return id;
	}
	public Integer getAmount_from() {
		return amount_from;
	}
	public Integer getAmount_to() {
		return amount_to;
	}
	public Float getAmount_plus() {
		return amount_plus;
	}
	public Integer getType() {
		return type;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setAmount_from(Integer amount_from) {
		this.amount_from = amount_from;
	}
	public void setAmount_to(Integer amount_to) {
		this.amount_to = amount_to;
	}
	public void setAmount_plus(Float amount_plus) {
		this.amount_plus = amount_plus;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
