package vn.gpay.gsmart.core.pcontractproductsku;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="pcontract_product_skus")
@Entity
public class PContractProductSKU implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_product_skus_generator")
	@SequenceGenerator(name="pcontract_product_skus_generator", sequenceName = "pcontract_product_skus_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private Long productid_link;
	private Long skuid_link;
	private Integer pquantity_sample;//SL mau
	private Integer pquantity_porder;//SL don
	private Integer pquantity_total;//SL tong sx
	
	
	private Integer pquantity_granted;//SL da phan chuyen
	
	private Integer pquantity_production;//SL yeu cau sx
	private Long pcontract_poid_link;
	
	@Transient
	private Integer pquantity_lenhsx = 0;//SL da tao lenh sx
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontract_poid_link",insertable=false,updatable =false)
    private PContract_PO po;
	
	@Transient
	public String getPo_buyer() {
		if(po!=null)
			return po.getPo_buyer();
		return "";
	}
	
	@Transient
	public Integer getPquantity() {
		return pquantity_total;
	}
	@Transient
	public String getProductcode() {
		if(product!=null)
			return product.getBuyercode();
		return "";
	}
	
	@Transient
	public String getProductname() {
		if(product!=null)
			return product.getBuyername();
		return "";
	}
	
	@Transient
	public Long getUnitid_link() {
		if(product!=null)
			return product.getUnitid_link();
		return null;
	}
	
	@Transient
	public String getUnitname() {
		if(product!=null)
			return product.getUnitName();
		return "";
	}
	
	@Transient
	public String getSkuName() {
		if(sku!=null) {
			return sku.getName();
		}
		return "";
	}
	
	@Transient
	public int getSort_value() {
		if(sku!=null) {
			return sku.getSort_size();
		}
		return 0;
	}
	
	@Transient
	public String getSkuCode() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
	}
	
	@Transient
	public String getSkuBarCode() {
		if(sku!=null) {
			return sku.getBarcode();
		}
		return "";
	}
	
	@Transient
	public String getMauSanPham() {
		if(sku!=null) {
			return sku.getMauSanPham();
		}
		return "";
	}
	
	@Transient
	public String getCoSanPham() {
		if(sku!=null) {
			return sku.getCoSanPham();
		}
		return "";
	}
	
	@Transient
	public Long getSizeid_link() {
		if(sku!=null) {
			return sku.getSize_id();
		}
		return (long)0;
	}
	
	@Transient
	public Long getColor_id() {
		if(sku!=null) {
			return sku.getColor_id();
		}
		return (long)0;
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

	public Integer getPquantity_granted() {
		return pquantity_granted;
	}

	public void setPquantity_granted(Integer pquantity_granted) {
		this.pquantity_granted = pquantity_granted;
	}

	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}

	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}

	public Integer getPquantity_production() {
		return pquantity_production;
	}

	public void setPquantity_production(Integer pquantity_production) {
		this.pquantity_production = pquantity_production;
	}

	public Integer getPquantity_lenhsx() {
		return pquantity_lenhsx;
	}

	public void setPquantity_lenhsx(Integer pquantity_lenhsx) {
		this.pquantity_lenhsx = pquantity_lenhsx;
	}


}
