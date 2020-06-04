package vn.gpay.gsmart.core.porder_product_sku;

import javax.persistence.Transient;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="porders_product_skus")
@Entity
public class POrder_Product_SKU implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_product_sku_generator")
	@SequenceGenerator(name="porder_product_sku_generator", sequenceName = "porders_product_skus_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	private Long porderid_link;
	private Long productid_link;
	private Long skuid_link;
	private Integer pquantity_sample;
	private Integer pquantity_porder;
	private Integer pquantity_total;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@Transient
	public String getSkucode() {
		if(sku != null) {
			return sku.getCode();
		}
		return "";
	}
	@Transient
	public Integer getProducttypeid_link() {
		if(sku != null) {
			return sku.getProducttypeid_link();
		}
		return null;
	}
	@Transient
	public String getProduct_typeName() {
		if(sku != null) {
			return sku.getProducttype_name();
		}
		return "";
	}
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private POrder porder;
	
	@Transient
	public String getOrdercode() {
		if(porder != null) {
			return porder.getOrdercode();
		}
		return "";
	}
	
	@Transient
	public String getMauSanPham() {
		if(sku != null) {
			return sku.getMauSanPham();
		}
		return "";
	}
	
	@Transient
	public String getCoSanPham() {
		if(sku != null) {
			return sku.getCoSanPham();
		}
		return "";
	}
	
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
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Integer getPquantity_sample() {
		return pquantity_sample;
	}
	public void setPquantity_sample(Integer pquantity_sample) {
		this.pquantity_sample = pquantity_sample;
	}
	public Integer getPquantity_porder() {
		return pquantity_porder;
	}
	public void setPquantity_porder(Integer pquantity_porder) {
		this.pquantity_porder = pquantity_porder;
	}
	public Integer getPquantity_total() {
		return pquantity_total;
	}
	public void setPquantity_total(Integer pquantity_total) {
		this.pquantity_total = pquantity_total;
	}
	public SKU getSku() {
		return sku;
	}
	public void setSku(SKU sku) {
		this.sku = sku;
	}
}
