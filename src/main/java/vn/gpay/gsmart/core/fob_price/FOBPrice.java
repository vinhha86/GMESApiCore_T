package vn.gpay.gsmart.core.fob_price;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="fob_price")
@Entity
public class FOBPrice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fob_price_generator")
	@SequenceGenerator(name="fob_price_generator", sequenceName = "fob_price_id_seq", allocationSize=1)
	protected Long id;
	@Column(name ="name",length =100)
	private String name;
	@Column(name ="orgrootid_link")
	private Long orgrootid_link;
	@Column(name ="issystemfix")
	private boolean issystemfix;
	 
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIssystemfix() {
		return issystemfix;
	}
	public void setIssystemfix(boolean issystemfix) {
		this.issystemfix = issystemfix;
	}
	
	
}
