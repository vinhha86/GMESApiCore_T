package vn.gpay.gsmart.core.pcontract_product_bom_log;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="pcontract_bom2_sku_log")
@Entity
public class PContract_bom2_sku_log implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_bom2_sku_log_generator")
	@SequenceGenerator(name="pcontract_bom2_sku_log_generator", sequenceName = "pcontract_bom2_sku_log_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private Long productid_link;
	private Long material_skuid_link;
	private Long product_skuid_link;
	private Float amount_old;
	private Float amount;
	private Long userupdateid_link;
	private Date timeupdate;
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
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public Long getMaterial_skuid_link() {
		return material_skuid_link;
	}
	public void setMaterial_skuid_link(Long material_skuid_link) {
		this.material_skuid_link = material_skuid_link;
	}
	public Long getProduct_skuid_link() {
		return product_skuid_link;
	}
	public void setProduct_skuid_link(Long product_skuid_link) {
		this.product_skuid_link = product_skuid_link;
	}
	public Float getAmount_old() {
		return amount_old;
	}
	public void setAmount_old(Float amount_old) {
		this.amount_old = amount_old;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public Long getUserupdateid_link() {
		return userupdateid_link;
	}
	public void setUserupdateid_link(Long userupdateid_link) {
		this.userupdateid_link = userupdateid_link;
	}
	public Date getTimeupdate() {
		return timeupdate;
	}
	public void setTimeupdate(Date timeupdate) {
		this.timeupdate = timeupdate;
	}
	
	
}
