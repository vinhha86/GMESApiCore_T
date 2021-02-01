package vn.gpay.gsmart.core.cutplan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="cutplan_size")
@Entity
public class CutPlan_Size implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cutplan_generator")
	@SequenceGenerator(name="cutplan_generator", sequenceName = "cutplan_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long product_skuid_link;
	private Long cutplanrowid_link;
	private Integer amount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public Long getProduct_skuid_link() {
		return product_skuid_link;
	}
	public void setProduct_skuid_link(Long product_skuid_link) {
		this.product_skuid_link = product_skuid_link;
	}
	public Long getCutplanrowid_link() {
		return cutplanrowid_link;
	}
	public void setCutplanrowid_link(Long cutplanrowid_link) {
		this.cutplanrowid_link = cutplanrowid_link;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	
	
	
}
