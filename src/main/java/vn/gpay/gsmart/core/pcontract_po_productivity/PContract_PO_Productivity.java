package vn.gpay.gsmart.core.pcontract_po_productivity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="pcontract_po_productivity")
@Entity
public class PContract_PO_Productivity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_po_productivity_generator")
	@SequenceGenerator(name="pcontract_po_productivity_generator", sequenceName = "pcontract_po_productivity_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long pcontract_poid_link;
	private Long productid_link;
	private Integer plan_productivity;
	private Float plan_linerequired;
	private Integer amount;
	private Integer productiondays_ns;
	
	
	
	public Integer getProductiondays_ns() {
		return productiondays_ns == null ? -1 : productiondays_ns;
	}
	public void setProductiondays_ns(Integer productiondays_ns) {
		this.productiondays_ns = productiondays_ns;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Long getId() {
		return id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public Integer getPlan_productivity() {
		return plan_productivity;
	}
	public Float getPlan_linerequired() {
		return plan_linerequired;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public void setPlan_productivity(Integer plan_productivity) {
		this.plan_productivity = plan_productivity;
	}
	public void setPlan_linerequired(Float plan_linerequired) {
		this.plan_linerequired = plan_linerequired;
	}
	
	
}
